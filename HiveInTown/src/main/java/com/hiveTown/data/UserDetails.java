package com.hiveTown.data;

import java.io.Serializable;

import com.hiveTown.model.RoleType;

public class UserDetails implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2809965001307464946L;
	
	/*FirstName of the User*/
	private String firstName;
	
	/*FirstName of the User*/
	private String lastName;
	
	private String displayName;
	
	private String email;
	
	private int userId;
	
	private RoleType role;

	private Boolean isVerified;
	
	private String token;
	
	private Boolean unsubscribedComments;
	
	private Boolean unsubscribedAll;
	
	private String communityUrlKeyword;
	
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	
	/**
	 * @return the userId
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Boolean getIsVerified() {
		return isVerified;
	}

	public void setIsVerified(Boolean isVerified) {
		this.isVerified = isVerified;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Boolean getUnsubscribedComments() {
		return unsubscribedComments;
	}

	public void setUnsubscribedComments(Boolean unsubscribedComments) {
		this.unsubscribedComments = unsubscribedComments;
	}

	public Boolean getUnsubscribedAll() {
		return unsubscribedAll;
	}

	public void setUnsubscribedAll(Boolean unsubscribedAll) {
		this.unsubscribedAll = unsubscribedAll;
	}

	public RoleType getRole() {
		return role;
	}

	public void setRole(RoleType role) {
		this.role = role;
	}

	public String getCommunityUrlKeyword() {
		return communityUrlKeyword;
	}

	public void setCommunityUrlKeyword(String communityUrlKeyword) {
		this.communityUrlKeyword = communityUrlKeyword;
	}

}
