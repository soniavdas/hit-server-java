
package com.hiveTown.Exception;

/**
 * This provides exception handling methods for all DAO and agent methods.
 */
public class HTWebConsoleException extends Exception
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6307234894322820093L;

	/** The exp code. */
	private String expCode;

	/** The exp msg. */
	private String expMsg;

	/** The error code. */
	private int errorCode;

	/**
	 * InstantiHTes a new HT web console exception.
	 */
	public HTWebConsoleException()
	{
		super();
	}

	/**
	 * InstantiHTes a new HT web console exception.
	 * 
	 * @param string
	 *            the string
	 */
	public HTWebConsoleException(String string)
	{
		this.expCode = string;
	}

	/**
	 * InstantiHTes a new HT web console exception.
	 * 
	 * @param sCode
	 *            the s code
	 * @param sMsg
	 *            the s msg
	 */
	public HTWebConsoleException(String sCode, String sMsg)
	{
		this.expCode = sCode;
		this.expMsg = sMsg;
	}

	/**
	 * InstantiHTes a new HT web console exception.
	 * 
	 * @param sCode
	 *            the s code
	 * @param sMsg
	 *            the s msg
	 * @param e
	 *            the e
	 */
	public HTWebConsoleException(String sCode, String sMsg, Exception e)
	{
		this.expCode = sCode;
		this.expMsg = sMsg;
	}

	/**
	 * This constructor converts any exception to SQLException.
	 * 
	 * @param expCode
	 *            the exp code
	 * @param e
	 *            the e
	 */

	public HTWebConsoleException(String expCode, Exception e)
	{
		this.expCode = expCode;

	}

	/**
	 * This constructor converts any exception to Exception.
	 * 
	 * @param errorCode
	 *            the error code
	 * @param e
	 *            the e
	 */

	public HTWebConsoleException(int errorCode, Exception e)
	{
		this.errorCode = errorCode;

	}

	/**
	 * This method returns the user-friendly message for each exception's
	 * message code.
	 * 
	 * @param sExpCode
	 *            the s exp code
	 * @return the user message
	 */
	public String getUserMessage(String sExpCode)
	{
		String sExpMessage = "";
		return sExpMessage;
	}

	/**
	 * Gets the exp code.
	 * 
	 * @return String
	 */
	public String getExpCode()
	{
		return expCode;
	}

	/**
	 * Sets the exp code.
	 * 
	 * @param string
	 *            the new exp code
	 */
	public void setExpCode(String string)
	{
		expCode = string;
	}

	/**
	 * Gets the exp msg.
	 * 
	 * @return String
	 */
	public String getExpMsg()
	{
		return expMsg;
	}

	/**
	 * Sets the exp msg.
	 * 
	 * @param sMsg
	 *            the new exp msg
	 */
	public void setExpMsg(String sMsg)
	{
		expMsg = sMsg;
	}

	/**
	 * Gets the message.
	 * 
	 * @return String
	 */
	public String getMessage()
	{
		return expCode;
	}

	/**
	 * Gets the error code.
	 * 
	 * @return int
	 */
	public int getErrorCode()
	{
		return errorCode;
	}

	/**
	 * Sets the error code.
	 * 
	 * @param errorCode
	 *            the new error code
	 */
	public void setErrorCode(int errorCode)
	{
		this.errorCode = errorCode;
	}

}
