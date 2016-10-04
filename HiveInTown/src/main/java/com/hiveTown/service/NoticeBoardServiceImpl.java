package com.hiveTown.service;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.hiveTown.Exception.HTClientServicesException;
import com.hiveTown.Exception.HTWebConsoleException;
import com.hiveTown.dao.CommunityDao;
import com.hiveTown.dao.NoticeBoardDao;
import com.hiveTown.dao.UserDao;
import com.hiveTown.data.NoticeCommentDetails;
import com.hiveTown.data.NoticeSummary;
import com.hiveTown.model.Community;
import com.hiveTown.model.RoleType;
import com.hiveTown.model.User;
import com.hiveTown.model.UserCommunity;
import com.hiveTown.model.NoticeBoard.*;

@Transactional(readOnly=true, rollbackFor = Exception.class)
public class NoticeBoardServiceImpl implements NoticeBoardService {
   
	private static final Logger LOGGER = Logger.getLogger(NoticeBoardService.class);
	
	private SessionFactory sessionFactory;
	private NoticeBoardDao noticeBoardDao;
 	private CommunityDao communityDao;
 	private EmailService emailService;
 	private UserDao userDao;
 	
 	@Autowired
    public void setSessionFactory(SessionFactory sf){
        this.sessionFactory = sf;
    }
 	
    @Autowired
    public void setNoticeBoardDao(NoticeBoardDao noticeDao){
        this.noticeBoardDao = noticeDao;
    }
    
    @Autowired
    public void setCommunityDao(CommunityDao communityDao){
        this.communityDao = communityDao;
    }
    
    @Autowired
    public void setUserDao(UserDao userDao){
        this.userDao = userDao;
    }
    
    @Autowired
    public void setEmailService(EmailService emailService){
        this.emailService = emailService;
    }
    
	@Override
	@Transactional(readOnly=false)
	public void addNotice(Notice notice, Community community) {
		notice.setCommunity(community);
		//community.getNotices().add(notice);
		this.noticeBoardDao.addNotice(notice);
	}
	
	@Override
	@Transactional(readOnly=false)
    public void updateNotice(Notice notice) {
		this.noticeBoardDao.updateNotice(notice);
	}
	
	@Override
	@Transactional
	public Notice getNoticeById(Integer id) {
		return this.noticeBoardDao.getNotice(id);
	}
	
	@Override
	@Transactional
	public List<Notice> getNoticesByStatus(Community community, NoticeStatus status, int pageIndex, int maxResults) {
		return this.noticeBoardDao.getNoticesByStatus(community, status, pageIndex, maxResults);
	}

	@Override
	@Transactional(readOnly=false)
	public int addComment(Integer noticeId, String comment, User user) throws HTWebConsoleException {
		//Session session = this.sessionFactory.getCurrentSession();
		Notice notice = this.noticeBoardDao.getNotice(noticeId);
		NoticeComment noticeComment = new NoticeComment();
		noticeComment.setCommentedByUser(user);
		noticeComment.setComment(comment);
		noticeComment.setDate(Calendar.getInstance().getTime());
		notice.getComments().add(noticeComment);
		noticeComment.setNotice(notice);
		try {
			this.noticeBoardDao.addComment(noticeComment);
		} catch(Exception e) {
			LOGGER.error(e);
			throw new HTWebConsoleException("ADD_COMMENT_FAILED");
		}
		//session.save(notice);
		return noticeComment.getId();
	}

	@Override
	@Transactional(readOnly=false)
	public void deleteComment(Integer commentId, User user, String urlKeyword) throws HTWebConsoleException {
		Session session = this.sessionFactory.getCurrentSession();
		NoticeComment comment = (NoticeComment) session.get(NoticeComment.class, commentId);
		Community community = this.communityDao.getCommunityByUrlKeyword(urlKeyword);
		RoleType role = this.userDao.getRole(user, community);
		
		if (comment.getCommentedByUser().getId() == user.getId() || role == RoleType.ADMIN) {
			Notice notice = comment.getNotice();
			comment.setNotice(null);
			comment.setCommentedByUser(null);
			notice.getComments().remove(comment);
			session.save(notice);
			session.delete(comment);
		} else {
			LOGGER.info("user does not have access to delete comment:" + user.getId());
			throw new HTWebConsoleException("ACCESS_DENIED");
		}
	}

	@Override
	@Transactional
	public List<Notice> getAllNotices(Community community, int pageIndex, int maxResults) {
		return this.noticeBoardDao.getNoticesByStatus(community, NoticeStatus.INVALID, pageIndex, maxResults);
	}

	@Override
	@Transactional
	public NoticeSummary getSummary(Community community) {
		return this.noticeBoardDao.getSummary(community);
	}
	
	@Override
	@Transactional(readOnly=false)
	public void deleteNotice(Integer noticeId) {
		this.noticeBoardDao.deleteNotice(noticeId);
	}
	
	@Override
	@Transactional(readOnly=false)
	public void publish(Integer noticeId, Integer communityId) throws HTWebConsoleException {
		try {
			Community c = this.communityDao.getCommunityById(communityId);
			Notice notice = this.noticeBoardDao.getNotice(noticeId);
			
			LOGGER.info("Publishing notice:" + notice.getId() + " subject:" + notice.getSubject());
			LOGGER.info("community:" + c.getCommunityName());
			//int success = 0, failed = 0;
			List<String> emailList = this.communityDao.getEmailList(c);
			LOGGER.info("No. of emails:" + emailList.size());
			String replyTo = c.getReplyToNoticeAddress(); //String.format(c.getReplyToNoticeAddress(), notice.getUuid()); 
			LOGGER.info("reply address:" + replyTo);
			String msgId = this.emailService.sendNotice(emailList, notice.getSubject(), notice.getDetails(), c.getFromAddress(), replyTo);
			if (msgId == null) {
				throw new HTWebConsoleException("ERROR_SENDING_NOTICE");
			} else {
				notice.setUuid(msgId);
			}
			
			notice.setStatus(NoticeStatus.APPROVED);
			this.noticeBoardDao.updateNotice(notice);
			
		} catch(Exception e) {
			LOGGER.error(e);
			throw new HTWebConsoleException("ERROR_SENDING_NOTICE");
		}
	}

	@Override
	@Transactional
	public NoticeComment getComment(Integer commentId) {
		return this.noticeBoardDao.getComment(commentId);
	}

	@Override
	@Transactional(readOnly=false)
	public void updateComment(Integer commentId, String commentText) {
		NoticeComment comment = this.noticeBoardDao.getComment(commentId);
		comment.setComment(commentText);
		this.noticeBoardDao.updateComment(comment);
	}
	
}
