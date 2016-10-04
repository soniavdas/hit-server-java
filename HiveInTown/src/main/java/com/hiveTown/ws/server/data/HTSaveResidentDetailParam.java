package com.hiveTown.ws.server.data;

import com.hiveTown.data.ResidentDetails;

public class HTSaveResidentDetailParam {
	
	private ResidentDetails residentDetails;
	
	private String customMessage;

	public String getCustomMessage() {
		return customMessage;
	}

	public void setCustomMessage(String customMessage) {
		this.customMessage = customMessage;
	}

	public ResidentDetails getResidentDetails() {
		return residentDetails;
	}

	public void setResidentDetails(ResidentDetails residentDetails) {
		this.residentDetails = residentDetails;
	}
	
}
