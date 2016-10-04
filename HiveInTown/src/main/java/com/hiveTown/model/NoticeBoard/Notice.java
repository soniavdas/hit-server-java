package com.hiveTown.model.NoticeBoard;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.hiveTown.model.Community;
import com.hiveTown.model.User;

@Entity
@Table(name="noticeboard")
public class Notice {
	
	  	private int id;
	 	private String subject;
	 	private String details;
	 	private Date fromDate;
	 	private Date toDate;
	 	private Date dateStatusUpdated;
	 	private User createdByUser;
	 	private User statusUpdatedBy;
	 	private Community community;
	 	private NoticeCategory category;
	 	private NoticeStatus status;
	 	private Set<NoticeAttachment> attachments = new HashSet<NoticeAttachment>();
	 	private Set<NoticeComment> comments = new HashSet<NoticeComment>();
	 	private Timestamp createTime;
	 	private Timestamp updateTime;
	 	private String uuid;
	 	
	 	@Id
	    @Column(name="idNoticeBoard")
	    @GeneratedValue(strategy=GenerationType.IDENTITY)
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getSubject() {
			return subject;
		}
		public void setSubject(String subject) {
			this.subject = subject;
		}
		public String getDetails() {
			return details;
		}
		public void setDetails(String details) {
			this.details = details;
		}
		
		@ManyToOne
		@JoinColumn(name = "fk_createdByUserId")
		public User getCreatedByUser() {
			return createdByUser;
		}
		public void setCreatedByUser(User createdByUser) {
			this.createdByUser = createdByUser;
		}
		
		@ManyToOne
		@JoinColumn(name = "fk_statusUpdatedByUserId")
		public User getStatusUpdatedBy() {
			return statusUpdatedBy;
		}
		public void setStatusUpdatedBy(User statusUpdatedBy) {
			this.statusUpdatedBy = statusUpdatedBy;
		}
		
		@ManyToOne
		@JoinColumn(name = "fk_communityId")
		public Community getCommunity() {
			return community;
		}
		public void setCommunity(Community community) {
			this.community = community;
		}
		
		@Enumerated(EnumType.ORDINAL)
		@Column(name = "fk_noticeBoardCategoryEnum")
		public NoticeCategory getCategory() {
			return category;
		}
		public void setCategory(NoticeCategory category) {
			this.category = category;
		}
		
		@Enumerated(EnumType.ORDINAL)
		@Column(name = "fk_noticeBoardStatusEnum")
		public NoticeStatus getStatus() {
			return status;
		}
		public void setStatus(NoticeStatus status) {
			this.status = status;
		}
		
		@OneToMany(fetch = FetchType.LAZY, mappedBy = "notice", cascade=CascadeType.ALL)
		public Set<NoticeAttachment> getAttachments() {
			return attachments;
		}
		public void setAttachments(Set<NoticeAttachment> attachments) {
			this.attachments = attachments;
		}
		
		@OneToMany(fetch = FetchType.LAZY, mappedBy = "notice", cascade=CascadeType.ALL)
		@OrderBy("date")
		public Set<NoticeComment> getComments() {
			return comments;
		}
		public void setComments(Set<NoticeComment> comments) {
			this.comments = comments;
		}
		public Date getFromDate() {
			return fromDate;
		}
		public void setFromDate(Date fromDate) {
			this.fromDate = fromDate;
		}
		public Date getToDate() {
			return toDate;
		}
		public void setToDate(Date toDate) {
			this.toDate = toDate;
		}
		public Timestamp getCreateTime() {
			return createTime;
		}
		public void setCreateTime(Timestamp createTime) {
			this.createTime = createTime;
		}
		public Timestamp getUpdateTime() {
			return updateTime;
		}
		public void setUpdateTime(Timestamp updateTime) {
			this.updateTime = updateTime;
		}
		public Date getDateStatusUpdated() {
			return dateStatusUpdated;
		}
		public void setDateStatusUpdated(Date dateStatusUpdated) {
			this.dateStatusUpdated = dateStatusUpdated;
		}
		public String getUuid() {
			return uuid;
		}
		public void setUuid(String uuid) {
			this.uuid = uuid;
		}
}
