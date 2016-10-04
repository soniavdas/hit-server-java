package com.hiveTown.ws.server.data;

public class HTErrorResponse {

	private String code;
	private String message;
	
	
	public HTErrorResponse(String code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
