package com.hiveTown.model;
 

import java.sql.Timestamp;

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


@Entity
@Table(name="user_community")
public class UserCommunity {


	private int id;
    private RoleType role;
    private User user;
	private Community community;
	private Timestamp createTime;
 	private Timestamp updateTime;
 	
    @Id
	@Column(name="idUserCommunity")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "fk_roleType")  
    public RoleType getRole() {
    	return this.role;
    }
    
    public void setRole(RoleType role) {
    	this.role = role;
    }
    
    @ManyToOne
	@JoinColumn(name = "fk_communityId")
	public Community getCommunity() {
		return this.community;
	}

	public void setCommunity(Community community) {
		this.community = community;
	}

	@ManyToOne
	@JoinColumn(name = "fk_userId")
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
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
}