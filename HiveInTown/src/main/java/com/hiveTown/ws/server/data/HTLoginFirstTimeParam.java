package com.hiveTown.ws.server.data;



public class HTLoginFirstTimeParam {
	
	/**emailId**/
	public String emailId;
	/*Token*/
	public String token;
	/*Login API Google/FB */
	public String loginAPI;
	/* the community name*/
	public String urlKeyword;
	
	/*unique ecode for each id*/
	public String ecode;
	
	/*role id*/
	public String role;
	
	public String socialEmail;
	
	private String profileUrl;
		
	/**
	 * @return the socialEmail
	 */
	public String getSocialEmail() {
		return socialEmail;
	}
	/**
	 * @param socialEmail the socialEmail to set
	 */
	public void setSocialEmail(String socialEmail) {
		this.socialEmail = socialEmail;
	}
	/**
	 * @return the urlKeyword
	 */
	public String getUrlKeyword() {
		return urlKeyword;
	}
	/**
	 * @param urlKeyword the urlKeyword to set
	 */
	public void setUrlKeyword(String urlKeyword) {
		this.urlKeyword = urlKeyword;
	}
	/**
	 * @return the ecode
	 */
	public String getEcode() {
		return ecode;
	}
	/**
	 * @param ecode the ecode to set
	 */
	public void setEcode(String ecode) {
		this.ecode = ecode;
	}
	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}
	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}
	/**
	 * @return the emailId
	 */
	public String getEmailId() {
		return emailId;
	}
	/**
	 * @param emailId the emailId to set
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}
	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}
	/**
	 * @return the loginAPI
	 */
	public String getLoginAPI() {
		return loginAPI;
	}
	/**
	 * @param loginAPI the loginAPI to set
	 */
	public void setLoginAPI(String loginAPI) {
		this.loginAPI = loginAPI;
	}
	public String getProfileUrl() {
		return profileUrl;
	}
	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}

	public String toString() {
		StringBuilder strb = new StringBuilder();
		strb.append("email:");
		strb.append(this.emailId);
		strb.append(" loginAPI:");
		strb.append(loginAPI);
		strb.append(" role:");
		strb.append(role);
		strb.append(" ecode:");
		strb.append(ecode);
		strb.append(" profileUrl:" + profileUrl);
		return strb.toString();
	}
	
}


