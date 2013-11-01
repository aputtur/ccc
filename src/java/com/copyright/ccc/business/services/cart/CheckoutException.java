package com.copyright.ccc.business.services.cart;

import com.copyright.ccc.CCRuntimeException;

public class CheckoutException extends CCRuntimeException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String logMessage="";

	/**
	 * Creates a new <code>CheckoutException</code> with <code>null</code> message.
	 */
	CheckoutException()
	{
		super();  
	}


	/**
	 * Creates a new <code>CheckoutException</code> with the given message.
	 *
	 * @param message  the error message.
	 */
	CheckoutException(String message)
	{
		super(message); 
	}

	/**
	 * Creates a new <code>CheckoutException</code> with the given message.
	 *
	 * @param message  the error message.
	 * @param logMessage the message that should get logged
	 * 
	 */
	CheckoutException(String message, String logMessage)
	{
		super(message); 
		this.logMessage = logMessage;
	}


	/**
	 * Creates a new <code>CheckoutException</code> with the given message and
	 * given cause exception.
	 *
	 * @param message  the error message.
	 * @param cause  the cause of this exception.
	 */
	CheckoutException(String message, Throwable cause) 
	{
		super(message,cause);
	}

	/**
	 * Creates a new <code>CheckoutException</code> with the given message and
	 * given cause exception and given message to be logged.
	 *
	 * @param message  the error message.
	 * @param cause  the cause of this exception.
	 * @param logMessage the message that should get logged
	 */
	CheckoutException(String message, Throwable cause, String logMessage) 
	{
		super(message,cause);
		this.logMessage = logMessage;
	}

	/**
	 * Creates a new <code>CheckoutException</code> with the
	 * given cause exception and its message.
	 *
	 * @param cause  the cause of this exception.
	 */
	CheckoutException(Throwable cause) 
	{
		super(cause);
	}
	/**
	 * Creates a new <code>CheckoutException</code> with the
	 * given cause exception and given message to be logged.
	 *
	 * @param cause  the cause of this exception.
	 * @param logMessage the message that should get logged
	 */
	CheckoutException(Throwable cause, String logMessage) 
	{
		super(cause);
		this.logMessage = logMessage;
	}

	/**
	 * Returns the message to write to the log for
	 * this exception
	 * @return
	 */
	public String getLogMessage() {
		return logMessage;
	}


	/**
	 * Sets the message to write to the log for
	 * this exception
	 * @return
	 */
	public void setLogMessage(String logMessage) {
		this.logMessage = logMessage;
	}
	
	
}
