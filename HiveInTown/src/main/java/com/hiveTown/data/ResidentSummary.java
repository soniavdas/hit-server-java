package com.hiveTown.data;

import java.io.Serializable;

import com.hiveTown.model.RoleType;

public class ResidentSummary implements Serializable {
	
	private static final long serialVersionUID = -2809965001307464946L;
	
	private int total;
	
	private int totalAdmins;
	
	private int totalResidents;
	
	private int totalVerified;
	
	private int totalUnVerified;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getTotalAdmins() {
		return totalAdmins;
	}

	public void setTotalAdmins(int totalAdmins) {
		this.totalAdmins = totalAdmins;
	}

	public int getTotalResidents() {
		return totalResidents;
	}

	public void setTotalResidents(int totalResidents) {
		this.totalResidents = totalResidents;
	}

	public int getTotalVerified() {
		return totalVerified;
	}

	public void setTotalVerified(int totalVerified) {
		this.totalVerified = totalVerified;
	}

	public int getTotalUnVerified() {
		return totalUnVerified;
	}

	public void setTotalUnVerified(int totalUnVerified) {
		this.totalUnVerified = totalUnVerified;
	}
	
}