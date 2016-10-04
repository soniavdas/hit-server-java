package com.hiveTown.data;

import java.io.Serializable;

public class UserData implements Serializable{

    
    /**
     * 
     */
    private static final long serialVersionUID = -2809965001307464946L;
    
    private Integer userId;
        
    private String name;
    
    private String email;
    
    private String profileUrl;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getProfileUrl() {
		return profileUrl;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
    
    
}