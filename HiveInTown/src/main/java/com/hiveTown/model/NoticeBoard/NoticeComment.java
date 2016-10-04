package com.hiveTown.model.NoticeBoard;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hiveTown.model.User;

@Entity
@Table(name="noticeboard_comment")
public class NoticeComment {
	private int id;
	private Notice notice;
	private String comment;
	private Date date;
	private User commentedByUser;
 	private Timestamp createTime;
 	private Timestamp updateTime;
 	
 	@Id
    @Column(name="idNoticeBoardComment")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name = "fk_noticeBoardId")
	public Notice getNotice() {
		return notice;
	}
	public void setNotice(Notice notice) {
		this.notice = notice;
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
	
	@ManyToOne
	@JoinColumn(name = "fk_commentedByUserId")
	public User getCommentedByUser() {
		return commentedByUser;
	}
	public void setCommentedByUser(User commentedByUser) {
		this.commentedByUser = commentedByUser;
	}
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
}
