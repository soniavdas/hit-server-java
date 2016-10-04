package com.hiveTown.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="session")
public class HTSession {
  	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int sessionId;
	
	private int userIdFk;
	
	private String token;
	
	private Timestamp created;
	
	private Timestamp Last_Updated;

	/**
	 * @return the sessionId
	 */
	public int getSessionId() {
		return sessionId;
	}

	/**
	 * @param sessionId the sessionId to set
	 */
	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}

	

	/**
	 * @return the userIdFk
	 */
	public int getUserIdFk() {
		return userIdFk;
	}

	/**
	 * @param userIdFk the userIdFk to set
	 */
	public void setUserIdFk(int userIdFk) {
		this.userIdFk = userIdFk;
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
	 * @return the created
	 */
	public Timestamp getCreated() {
		return created;
	}

	/**
	 * @param created the created to set
	 */
	public void setCreated(Timestamp created) {
		this.created = created;
	}

	/**
	 * @return the last_Updated
	 */
	public Timestamp getLast_Updated() {
		return Last_Updated;
	}

	/**
	 * @param last_Updated the last_Updated to set
	 */
	public void setLast_Updated(Timestamp last_Updated) {
		Last_Updated = last_Updated;
	}

	
	
	

}
