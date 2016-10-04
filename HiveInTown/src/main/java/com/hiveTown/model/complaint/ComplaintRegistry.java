package com.hiveTown.model.complaint;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hiveTown.model.Community;
import com.hiveTown.model.User;

@Entity
@Table(name="complaint_registry")
public class ComplaintRegistry {
	
	 	
	    private int id;
	 	private User createdByUser;
	 	private User assignedUser;
	 	private ComplaintCategory category;
	 	private ComplaintStatus status;
	 	private String description;
	 	private Community community;
	 	private Timestamp expectedResolutionTime;
	 	private Timestamp createTime;
	 	private Timestamp updateTime;
	 	private String comment; 
	 	
	 	@Id
	    @Column(name="complaint_registry_id")
	    @GeneratedValue(strategy=GenerationType.IDENTITY)
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		
		@ManyToOne
		@JoinColumn(name = "fk_userId")
		public User getUser() {
			return createdByUser;
		}
		public void setUser(User user) {
			this.createdByUser = user;
		}
		
		@ManyToOne
		@JoinColumn(name = "fk_assignedUserId")
		public User getAssignedUser() {
			return assignedUser;
		}
		public void setAssignedUser(User assignedUser) {
			this.assignedUser = assignedUser;
		}
		
		@Enumerated(EnumType.ORDINAL)
		@Column(name = "fk_categoryId")
		public ComplaintCategory getCategory() {
			return category;
		}
		public void setCategory(ComplaintCategory category) {
			this.category = category;
		}
		
		@Enumerated(EnumType.ORDINAL)
		@Column(name = "fk_statusId")
		public ComplaintStatus getStatus() {
			return status;
		}
		public void setStatus(ComplaintStatus status) {
			this.status = status;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getComment() {
			return comment;
		}
		public void setComment(String comment) {
			this.comment = comment;
		}
		

		@ManyToOne
		@JoinColumn(name = "fk_communityId")
		public Community getCommunity() {
			return community;
		}
		public void setCommunity(Community community) {
			this.community = community;
		}
		public Date getExpectedResolutionTime() {
			return expectedResolutionTime;
		}
		public void setExpectedResolutionTime(Timestamp expectedResolutionTime) {
			this.expectedResolutionTime = expectedResolutionTime;
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
