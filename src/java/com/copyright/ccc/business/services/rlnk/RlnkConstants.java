package com.copyright.ccc.business.services.rlnk;

public class RlnkConstants{
	private RlnkConstants() {}
	
	public static final String CHANNEL = "copyright.com";
	public static final String ITEM_TYPECD_JOURNAL = "JOURNAL";
	public static final String PUB_TYPE_ARTICLE = "article";
	public static final String PUB_TYPE_CHAPTER = "chapter";
	
	public static final String PUB_TO_PUB = "PUB_TO_PUB";
	
public static final class RlnkParmConstants{
	private RlnkParmConstants() {}
	
	public static final String PARM_NAME_TARGET_PAGE = "targetPage";
	public static final String PARM_NAME_LOGINID = "loginID";
	public static final String PARM_NAME_PASSWORD = "password";
	public static final String PARM_VALUE_QUICKPRICE = "quickprice";
	public static final String PARM_VALUE_CONFIRM_ORDER = "confirmOrder";
	public static final String PARM_VALUE_AUTH_TRUSTED = "authenticatetrusted";
	public static final String PARM_NAME_PONUMBER = "PONumber";
	
	
	
}
public static final class RlnkSessionConstants{
		private RlnkSessionConstants() {}
		
		public static final String RL_REQUEST_PARM_IN_SESSION  = "rlink_request_parms_in_session";
}

}