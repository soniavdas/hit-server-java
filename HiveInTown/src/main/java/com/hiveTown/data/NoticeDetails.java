package com.hiveTown.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hiveTown.model.User;
import com.hiveTown.model.NoticeBoard.Notice;
import com.hiveTown.model.NoticeBoard.NoticeComment;

public class NoticeDetails implements Serializable {

	private static final long serialVersionUID = 2L;
	
	public int noticeId;
	public String subject;
	public String details;
	//public Date fromDate;
	//public Date toDate;
	public int statusId;
	public int categoryId;
	public String urlKeyword;
	public List<NoticeCommentDetails> comments;
	public UserData createdBy;
	public Date dateStatusUpdated;
	public UserData statusUpdatedBy;
	
	public static NoticeDetails convert(Notice notice) {
		NoticeDetails data = new NoticeDetails();
		data.subject = notice.getSubject();
		data.details = notice.getDetails();
		//data.fromDate = notice.getFromDate();
		//data.toDate = notice.getToDate();
		data.statusId = notice.getStatus().getId();
		data.categoryId = notice.getCategory().getId();
		data.urlKeyword = notice.getCommunity().getUrlKeyword();
		data.noticeId = notice.getId();
		data.dateStatusUpdated = notice.getDateStatusUpdated();
		
		if (notice.getStatusUpdatedBy() != null) {
			UserData u = new UserData();
			u.setName(notice.getStatusUpdatedBy().getPerson().getDisplayName());
			u.setEmail(notice.getStatusUpdatedBy().getEmail());
			u.setProfileUrl(notice.getStatusUpdatedBy().getProfileUrl());
			u.setUserId(notice.getStatusUpdatedBy().getId());
			data.statusUpdatedBy = u;
		}
				
		if (notice.getCreatedByUser() != null) {
			UserData u = new UserData();
			u.setName(notice.getCreatedByUser().getPerson().getDisplayName());
			u.setEmail(notice.getCreatedByUser().getEmail());
			u.setProfileUrl(notice.getCreatedByUser().getProfileUrl());
			u.setUserId(notice.getCreatedByUser().getId());
			data.createdBy = u;
		}
		
		data.comments = new ArrayList<NoticeCommentDetails>();
		
		for (NoticeComment comment : notice.getComments()) {
			User user = comment.getCommentedByUser();
			UserData u = new UserData();
			if (user != null) {
				u.setName(user.getPerson().getDisplayName());
				u.setProfileUrl(user.getProfileUrl());
				u.setEmail(user.getEmail());
				u.setUserId(user.getId());
			}
			NoticeCommentDetails nc = new NoticeCommentDetails(comment.getId(), comment.getComment(), comment.getDate(), u);
			data.comments.add(nc);
		}
		return data;
	}
}

