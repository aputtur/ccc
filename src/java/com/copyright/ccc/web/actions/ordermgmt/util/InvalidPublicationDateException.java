package com.copyright.ccc.web.actions.ordermgmt.util;

import com.copyright.base.CCCRuntimeException;

/**
 * Exception for invalid publication dates
 * 
 * @author gcuevas
 *
 */
public class InvalidPublicationDateException extends CCCRuntimeException {

	private static final long serialVersionUID = 6084746397181222913L;
	
	private String _publicationDate;
	
	public InvalidPublicationDateException(String publicationDate)
	{
		_publicationDate = publicationDate;
	}

	public String getPublicationDate()
	{
		return _publicationDate;
	}

}
