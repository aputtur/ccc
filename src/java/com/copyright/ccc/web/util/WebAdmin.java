package com.copyright.ccc.web.util;

import org.apache.log4j.Logger;

import com.copyright.ccc.CCException;
import com.copyright.ccc.util.LogUtil;

public class WebAdmin {

	private WebAdminDAO _dao = null;
	static Logger _log = Logger.getLogger( WebAdmin.class );

	//	These are ITEMs that can be INSerted/UPDated in table.
	//	These should probably NEVER be POPped or DELeted.  All of
	//	them can be retrieved.

	public static final String CUSTOMER_EMAIL_TEXT        = "CustomerEMail_Text";
	public static final String RIGHTSHOLDER_EMAIL_TEXT    = "RHolderEMail_Text";
	public static final String UNAVAILABLE_EMAIL_TEXT     = "NotAvailableEMail_Text";
	public static final String CONTACTRHDIRECT_EMAIL_TEXT = "ContactRHDirectEMail_Text";
	public static final String MAINTSTART_MESSAGE_TEXT    = "MaintStartMsg_Text";
	public static final String MAINTEND_MESSAGE_TEXT      = "MaintEndMsg_Text";
	public static final String USERALERT_FLAG             = "UserAlert_Flag";
	public static final String USERALERT_MESSAGE_TEXT     = "UserAlertMsg_Text";
	public static final String WEBSITE_STATUS_FLAG        = "WebSiteStatus_Flag";
	public static final String TIMEOUTAMOUNT_VALUE        = "TimeOutAmount_Value";
	public static final String TIMEOUTAMOUNT_MESSAGE_TEXT = "TimeOutAmountMsg_Text";

	//	This class really just amounts to nothing more than
	//	a wrapper for the DAO, which interfaces with PL/SQL
	//	procedures (in a package).  Start with our very
	//  simple constructor methods...
	
	public WebAdmin() {
        _log.debug("W E B   A D M I N :  Object created.");
        _dao = new WebAdminDAO();
    }
	
	public void insert(String itm, String val) {
		try {
			_dao.insert(itm, val);
		}
		catch(CCException e) {
			_log.warn(LogUtil.getStack(e));
			//	Do nothing.  This method will fail silently
			//	if the item already exists, btw.
		}		
	}
	
	public void update(String itm, String val) {
		try {
			_dao.update(itm, val);
		}
		catch(CCException e) {
			_log.warn(LogUtil.getStack(e));
			//	Do nothing.
		}		
	}
	
	//	Next is our sole accessor.
	
	public String get(String itm) {
		String val = null;
		
        _log.debug("W E B   A D M I N :  Getting " + itm);
        
		try {
			val = _dao.get(itm);
		}
		catch(CCException e) {
            _log.error("WEB ADMIN : error getting item " + itm + LogUtil.appendableStack(e));
			val = "";
		}
		return val;
	}
	
	//	Next come our destructors...  Well, technically
	//	pop would be an accessor I guess...
	
	public String pop(String itm) {
		String val = null;
		
		try {
			val = _dao.pop(itm);
		}
		catch(CCException e) {
			val = "";
		}
		return val;
	}
	
	public void delete(String itm) {
		try {
			_dao.delete(itm);
		}
		catch(CCException e) {
			_log.warn(LogUtil.getStack(e));
			//	Do nothing.
		}
	}
}