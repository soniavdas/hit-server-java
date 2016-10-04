package com.hiveTown.data;

import java.io.Serializable;
import java.util.Date;

public class NoticeCommentDetails implements Serializable {

	private static final long serialVersionUID = 3L;
	
	private Integer commentId;
	private String comment;
	private Date date;
	private UserData commentedBy;
	
	public NoticeCommentDetails(Integer commentId, String comment, Date date, UserData commentedBy) {
		this.setCommentId(commentId);
		this.setComment(comment);
		this.setDate(date);
		this.setCommentedBy(commentedBy);
	}

	public Integer getCommentId() {
		return commentId;
	}

	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public UserData getCommentedBy() {
		return commentedBy;
	}

	public void setCommentedBy(UserData commentedBy) {
		this.commentedBy = commentedBy;
	}
}
