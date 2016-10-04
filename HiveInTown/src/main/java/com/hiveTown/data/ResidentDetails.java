package com.hiveTown.data;

import java.io.Serializable;
import java.util.Date;

import com.hiveTown.model.ResidentEnumType;
import com.hiveTown.model.RoleType;

public class ResidentDetails implements Serializable {
	
	private static final long serialVersionUID = -2809965001307464946L;
	
	private Integer userId;
	
	private String email;
	
	private String name;
	
	private RoleType role;
	
	private String apartmentNum;

	private Boolean isVerified;
	
	private String blockName;
	
	private String contactNum;
	
	private Integer userCommunityId;
	
	private Integer userApartmentId;
	
	private String profileUrl;
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getUserApartmentId() {
		return userApartmentId;
	}

	public void setUserApartmentId(Integer userApartmentId) {
		this.userApartmentId = userApartmentId;
	}

	public String getApartmentNum() {
		return apartmentNum;
	}

	public void setApartmentNum(String apartmentNum) {
		this.apartmentNum = apartmentNum;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Boolean getIsVerified() {
		return isVerified;
	}

	public void setIsVerified(Boolean isVerified) {
		this.isVerified = isVerified;
	}


	public String getBlockName() {
		return blockName;
	}

	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}

	public Integer getUserCommunityId() {
		return userCommunityId;
	}

	public void setUserCommunityId(Integer userCommunityId) {
		this.userCommunityId = userCommunityId;
	}

	public RoleType getRole() {
		return role;
	}

	public void setRole(RoleType role) {
		this.role = role;
	}

	public String getContactNum() {
		return contactNum;
	}

	public void setContactNum(String contactNum) {
		this.contactNum = contactNum;
	}

	public String getProfileUrl() {
		return profileUrl;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}

	/*public int getApartmentId() {
		return apartmentId;
	}

	public void setApartmentId(int apartmentId) {
		this.apartmentId = apartmentId;
	}*/
	
}
