package com.copyright.ccc.web.actions.ordermgmt.util;

public class SessionExpiredExceptionOnPopUp extends SessionExpiredException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public SessionExpiredExceptionOnPopUp(){
		super();
	}
	public SessionExpiredExceptionOnPopUp(SessionExpiredException s){
		super();
		this.setExceptionMessage(s.getExceptionMessage());
		this.setExceptionRedirect(s.getExceptionRedirect());

	}
}
