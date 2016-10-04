package com.hiveTown.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

//Removing this until we have proper way of setting these records.
//@Entity
//@Table(name="block")
public class Block {
	
	private int id;
	private String blockName;
	private Timestamp createTime;
	private Timestamp updateTime;
	private Community community;
	private Set<Apartment> apartments = new HashSet<Apartment>();
	
	@Id
	@Column(name="idBlock")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getBlockName() {
		return blockName;
	}
	public void setBlockName(String blockName) {
		this.blockName = blockName;
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
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "block", cascade=CascadeType.ALL)
	public Set<Apartment> getApartments() {
		return this.apartments;
	}
	
	public void setApartments(Set<Apartment> apartments) {
		this.apartments = apartments;
	}

}
