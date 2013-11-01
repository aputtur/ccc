package com.copyright.ccc.web.actions.ordermgmt.util;

import com.copyright.base.CCCRuntimeException;

public class SessionExpiredException extends CCCRuntimeException {
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String exceptionMessage;
	private String exceptionRedirect;
	
	public SessionExpiredException(){
		super();
	}
	public SessionExpiredException(String exceptionMessage,String exceptionRedirect){
		super();
		this.setExceptionMessage(exceptionMessage);
		this.setExceptionRedirect(exceptionRedirect);
		
		
	}

	public void setExceptionRedirect(String exceptionRedirect) {
		this.exceptionRedirect = exceptionRedirect;
	}

	public String getExceptionRedirect() {
		return exceptionRedirect;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

}
