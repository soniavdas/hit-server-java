package com.hiveTown.dao;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.hiveTown.data.NoticeSummary;
import com.hiveTown.model.Community;
import com.hiveTown.model.UserCommunity;
import com.hiveTown.model.NoticeBoard.Notice;
import com.hiveTown.model.NoticeBoard.NoticeComment;
import com.hiveTown.model.NoticeBoard.NoticeStatus;


public class NoticeBoardDaoImpl implements NoticeBoardDao {

	private static final Logger LOGGER = Logger.getLogger(NoticeBoardDao.class);
	
	private SessionFactory sessionFactory;
    
	private static final String TOTAL_QUERY_BY_STATUS = 
	"select count(*) from Notice as n where n.community.id=:communityId and n.status=:status";
	
	private static final String TOTAL_QUERY = 
			"select count(*) from Notice as n where n.community.id=:communityId";
	
    @Autowired
    public void setSessionFactory(SessionFactory sf){
        this.sessionFactory = sf;
    }

	@Override
	public Notice getNotice(Integer id) {
		Session session = this.sessionFactory.getCurrentSession();   
        Notice notice = (Notice) session.get(Notice.class, id);
        if (notice != null) {
        	Hibernate.initialize(notice.getComments());
        }
        LOGGER.info("Notice loaded successfully, Notice details="+notice);
        return notice;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Notice> getNoticesByStatus(Community community, NoticeStatus status, int pageIndex, int maxResults) {
		LOGGER.info("pageIndex:" + pageIndex);
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = (Criteria) session.createCriteria(Notice.class);
		criteria.add(Restrictions.eq("community.id", community.getId()));
		
		if (status != NoticeStatus.INVALID) {
			criteria.add(Restrictions.eq("status", status));
		}
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.addOrder(Order.desc("dateStatusUpdated")).setFirstResult(pageIndex * maxResults).setMaxResults(maxResults);
		List<Notice> list =  (List<Notice>) criteria.list();
		if (list != null) {
			for(Notice n : list) { 
				Hibernate.initialize(n.getComments()); //load comments
			}
		}
		
		LOGGER.info("result count:" + list.size());
		return list;
	}
	
	@Override
	public int getCountOfNewNoticesForUser(Integer communityId, Integer userId) {
	/*	List<Notice> noticesForCommunity = getNoticesByStatus(communityId, NoticeStatus.APPROVED);
		
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = (Criteria) session.createCriteria(UserCommunity.class);
		criteria.add(Restrictions.eq("fk_communityId", communityId));
		criteria.add(Restrictions.eq("fk_userId", userId));
		UserCommunity uc = (UserCommunity) criteria.uniqueResult();
		
		Date lastViewed = Calendar.getInstance().getTime();
		int count = 0;
		for(Notice notice : noticesForCommunity) {
			if (lastViewed.before(notice.getDateStatusUpdated())) {
				count++;
			}
		} */
		return 0;
	}
	
	@Override
	public void addNotice(Notice notice) {
		Session session = this.sessionFactory.getCurrentSession();
		notice.setCreateTime(new Timestamp(System.currentTimeMillis()));
		notice.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        session.persist(notice);
        LOGGER.info("Notice saved successfully, Notice Details="+notice);
	}

	@Override
	public void updateNotice(Notice notice) {
		Session session = this.sessionFactory.getCurrentSession();
		notice.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        session.update(notice);
        LOGGER.info("Notice updated successfully, Notice Details="+notice);
	}

	
	@Override
	public void deleteNotice(Integer noticeId) {
		Session session = this.sessionFactory.getCurrentSession();
		Notice notice = (Notice) session.get(Notice.class, noticeId);
		if (notice != null) {
			session.delete(notice);
			LOGGER.info("Notice updated successfully, Notice Details="+notice);
		} else {
			LOGGER.info("Does not exist in db, Notice Details="+notice);
		}
	}
	
	@Override
	public void addComment(NoticeComment comment) {
		Session session = this.sessionFactory.getCurrentSession();
		comment.setCreateTime(new Timestamp(System.currentTimeMillis()));
		comment.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        session.persist(comment);
        LOGGER.info("Notice Comment saved successfully, Notice Comment Details="+comment);
	}

	@Override
	public void deleteComment(NoticeComment comment) {
		Session session = this.sessionFactory.getCurrentSession();
        session.delete(comment);
        LOGGER.info("Notice Comment deleted successfully, Notice Comment Details="+comment);
	}

	@Override
	public NoticeComment getComment(int commentId) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = (Criteria) session.createCriteria(NoticeComment.class);
		criteria.add(Restrictions.eq("id", commentId));
		return (NoticeComment) criteria.uniqueResult();
	}

	@Override
	public NoticeSummary getSummary(Community community) {
		Session session = this.sessionFactory.getCurrentSession();
		String query = TOTAL_QUERY_BY_STATUS;
		NoticeSummary summary = new NoticeSummary();
		
		int num = ( (Long) session.createQuery(TOTAL_QUERY)
				.setInteger("communityId", community.getId())
				.iterate().next()).intValue();
		
		summary.setTotal(num);
		 
		num = ( (Long) session.createQuery(query)
								.setInteger("communityId", community.getId())
								.setInteger("status", NoticeStatus.APPROVED.getId())
								.iterate().next()).intValue();
		summary.setApproved(num);
		
		num = ( (Long) session.createQuery(query)
				.setInteger("communityId", community.getId())
				.setInteger("status", NoticeStatus.PENDING.getId())
				.iterate().next()).intValue();
		summary.setPending(num);
		
		num = ( (Long) session.createQuery(query)
				.setInteger("communityId", community.getId())
				.setInteger("status", NoticeStatus.REJECTED.getId())
				.iterate().next()).intValue();
		summary.setRejected(num);

		num = ( (Long) session.createQuery(query)
				.setInteger("communityId", community.getId())
				.setInteger("status", NoticeStatus.DRAFT.getId())
				.iterate().next()).intValue();
		summary.setDraft(num);
		
		num = ( (Long) session.createQuery(query)
					.setInteger("communityId", community.getId())
					.setInteger("status", NoticeStatus.EXPIRED.getId())
					.iterate().next()).intValue();
		summary.setExpired(num);
		
		return summary;	
	}

	@Override
	public void updateComment(NoticeComment comment) {
		Session session = this.sessionFactory.getCurrentSession();
		comment.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        session.update(comment);
		
	}
}
