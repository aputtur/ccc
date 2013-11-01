package com.copyright.ccc.web.actions.ordermgmt.data;

import java.io.Serializable;

public class MissingConfirmationFields implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String missing = "undefined";
	
	public String getPaymentMethod() 
	{
		return missing;
	}
	
}
