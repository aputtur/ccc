/*
 * RLinkPublisherException.java
 * Copyright (c) 2009, Copyright Clearance Center, Inc. All rights reserved.
 * ----------------------------------------------------------------------------
 * Revision History
 * 2009-04-27	ppai	Created.
 * ----------------------------------------------------------------------------
 */
package com.copyright.ccc.business.data;

import com.copyright.base.CCCRuntimeException;


public class AutoDunningExpectedException extends CCCRuntimeException
{
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new <code>OverdueInvoiceExpectedException</code> object.
	 */
	public AutoDunningExpectedException(String MsgCode)
	{
		super();

		//String msgCode = "";
                
                

		this.setMessageCode(MsgCode);

		setSeverity(SEVERITY_LOW);
	}
}
