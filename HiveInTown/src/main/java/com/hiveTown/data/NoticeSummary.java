package com.hiveTown.data;

import java.io.Serializable;

import com.hiveTown.model.RoleType;

public class NoticeSummary implements Serializable {
	
	private static final long serialVersionUID = -2809965001307464946L;
	
	private int total;
	
	private int draft;
	
	private int pending;
	
	private int rejected;
	
	private int expired;
	
	private int approved;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getDraft() {
		return draft;
	}

	public void setDraft(int draft) {
		this.draft = draft;
	}

	public int getRejected() {
		return rejected;
	}

	public void setRejected(int rejected) {
		this.rejected = rejected;
	}

	public int getExpired() {
		return expired;
	}

	public void setExpired(int expired) {
		this.expired = expired;
	}

	public int getApproved() {
		return approved;
	}

	public void setApproved(int approved) {
		this.approved = approved;
	}

	public int getPending() {
		return pending;
	}

	public void setPending(int pending) {
		this.pending = pending;
	}

}