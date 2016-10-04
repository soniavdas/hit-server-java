package com.hiveTown.model;

import java.sql.Date;
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
import javax.persistence.Table;

// Removing this until we have proper way of setting these records.
//@Entity
//@Table(name="apartment")
public class Apartment {
	
	  
	private int id;
	private String apartmentNum;
	private Date createTime;
	private Date updateTime;
	private Block block;
	private ApartmentUsageTypeEnum apartmentUsageType;
	private Set<UserApartment> userApartments = new HashSet<UserApartment>();
	
	@Id
	@Column(name="idApartment")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getApartmentNum() {
		return apartmentNum;
	}
	public void setApartmentNum(String apartmentNum) {
		this.apartmentNum = apartmentNum;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	@ManyToOne
	@JoinColumn(name = "fk_blockId")
	public Block getBlock() {
		return block;
	}
	public void setBlock(Block block) {
		this.block = block;
	}
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "fk_apartmentUsageTypeEnum")
	public ApartmentUsageTypeEnum getApartmentUsageType() {
		return apartmentUsageType;
	}
	public void setApartmentUsageType(ApartmentUsageTypeEnum apartmentUsageType) {
		this.apartmentUsageType = apartmentUsageType;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "apartment", cascade=CascadeType.ALL)
	public Set<UserApartment> getUserApartments() {
		return userApartments;
	}
	public void setUserApartments(Set<UserApartment> userApartments) {
		this.userApartments = userApartments;
	}

}
