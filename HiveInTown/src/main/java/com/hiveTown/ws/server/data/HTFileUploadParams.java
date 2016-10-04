package com.hiveTown.ws.server.data;

import com.hiveTown.util.HTUtil;



public class HTFileUploadParams {

	/* email address of the resident */
	private String email;

	/* Name of the resident */
	private String name;

	/* contactNo of the resident */
	private String contactNo;

	/* appartment no. */
	private String appartment;

	/* Block no. */
	private String block;
	
	private int lineNo;
	
	private String urlKeyword;
	

	

	/**
	 * @return the urlKeyword
	 */
	public String getUrlKeyword() {
		return urlKeyword;
	}

	/**
	 * @param communityName the communityName to set
	 */
	public void setUrlKeyword(String urlKeyword) {
		this.urlKeyword = urlKeyword;
	}

	public int getLineNo() {
		return lineNo;
	}

	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the contactNo
	 */
	public String getContactNo() {
		return contactNo;
	}

	/**
	 * @param contactNo
	 *            the contactNo to set
	 */
	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	/**
	 * @return the appartment
	 */
	public String getAppartment() {
		return appartment;
	}

	/**
	 * @param appartment
	 *            the appartment to set
	 */
	public void setAppartment(String appartment) {
		this.appartment = appartment;
	}

	/**
	 * @return the block
	 */
	public String getBlock() {
		return block;
	}

	/**
	 * @param block
	 *            the block to set
	 */
	public void setBlock(String block) {
		this.block = block;
	}

	@Override
	public boolean equals(Object obj) {
		
		
		
		System.out.println("here");
		if (obj == this) {
			return true;
		}

		else if ((obj == null) || (obj.getClass() != this.getClass())) {
			return false;
		} else {

			// return false;
			HTFileUploadParams htFile = (HTFileUploadParams) obj;
			if ((htFile.email.equals(this.email))) {
				//HTUtil.rejectedId.put(this.lineNo, this.email);
				return true;
			}

		}
		return false;

	}

	public HTFileUploadParams(String email, String name, String contactNo,
			String appartment, String block,int lineNo , String urlKeyword) {
		//super();
		this.email = email;
		this.name = name;
		this.contactNo = contactNo;
		this.appartment = appartment;
		this.block = block;
		this.lineNo = lineNo;
		this.urlKeyword = urlKeyword;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((email == null) ? 0 : email.hashCode());
	//	result = prime * result + contactNo.hashCode();
		//
		return result;
	}

	public String toString() {
		return String.format("%s %s", this.email, this.name);
	}
}
