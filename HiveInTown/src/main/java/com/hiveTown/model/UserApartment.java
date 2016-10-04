package com.hiveTown.model;

import java.util.Date;
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
@Table(name="user_apartment")
public class UserApartment {
	

	private int id;
	private User user;
//	private Apartment apartment;
	private ResidentEnumType residentEnumType;
	private Date fromDate;
	private Date toDate;
	private boolean isResiding;
	private Timestamp createTime;
	private Timestamp updateTime;
	
	private String apartmentNum;
	private String blockName;
	private Community community;
	
	@Id
	@Column(name="idUserApartment")
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
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	
/*	 @ManyToOne
	 @JoinColumn(name = "fk_apartmentId")
	public Apartment getApartment() {
		return apartment;
	}


	public void setApartment(Apartment apartment) {
		this.apartment = apartment;
	} */


	@Enumerated(EnumType.ORDINAL)
	@Column(name = "fk_residentEnumType")
	public ResidentEnumType getResidentEnumType() {
		return residentEnumType;
	}

	public void setResidentEnumType(ResidentEnumType residentEnumType) {
		this.residentEnumType = residentEnumType;
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

	public boolean getIsResiding() {
		return isResiding;
	}

	public void setIsResiding(boolean isResiding) {
		this.isResiding = isResiding;
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
	
	@ManyToOne
	@JoinColumn(name = "fk_communityId")
	public Community getCommunity() {
		return community;
	}
	public void setCommunity(Community community) {
		this.community = community;
	}

	public String getBlockName() {
		return blockName;
	}

	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}

	public String getApartmentNum() {
		return apartmentNum;
	}

	public void setApartmentNum(String apartmentNum) {
		this.apartmentNum = apartmentNum;
	}
	
}
