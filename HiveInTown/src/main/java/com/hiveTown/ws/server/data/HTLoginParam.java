package com.hiveTown.ws.server.data;



public class HTLoginParam {
	
	/**emailId**/
	public String emailId;
	/*Token*/
	public String token;
	/*Login API Google/FB */
	public String loginAPI;
	/* the community name*/
	public String urlKeyword;
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


	
}


