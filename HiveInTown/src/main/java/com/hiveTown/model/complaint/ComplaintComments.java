package com.hiveTown.model.complaint;

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

@Entity
@Table(name="complaint_comments")
public class ComplaintComments {
	
    private int id;	
	private ComplaintRegistry complaintReg;
	private ComplaintComments previousComment;
	private String comments;
	private Timestamp createTime;
	private Timestamp updateTime;
	
	@Id
    @Column(name="idComplaintComments")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@ManyToOne
	@JoinColumn(name = "complaintId")
	public ComplaintRegistry getComplaint() {
		return complaintReg;
	}
	public void setComplaint(ComplaintRegistry complaint) {
		this.complaintReg = complaint;
	}
	
	@ManyToOne
	@JoinColumn(name = "previousCommentId")
	public ComplaintComments getPreviousComment() {
		return previousComment;
	}
	public void setPreviousComment(ComplaintComments previousComment) {
		this.previousComment = previousComment;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

}
