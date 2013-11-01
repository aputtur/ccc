package com.copyright.ccc.business.services;


public class ItemConstants {
/*
// ItemParm Constants
	public static final String NUM_COPIES = "NUM_COPIES";
	public static final String NUM_PAGES = "NUM_PAGES";
	public static final String NUM_STUDENTS = "NUM_STUDENTS";
	public static final String NUM_RECIPIENTS = "NUM_RECIPIENTS";	
	public static final String PUBLICATION_YEAR_OF_USE = "PUBLICATION_YEAR_OF_USE";
	public static final String CIRCULATION_DISTRIBUTION = "CIRCULATION_DISTRIBUTION";
	public static final String BUSINESS_TYPE = "BUSINESS_TYPE";
	public static final String TYPE_OF_CONTENT = "TYPE_OF_CONTENT";
	public static final String IS_AUTHOR = "IS_AUTHOR";
	public static final String REPUBLICATION_DATE = "REPUBLICATION_DATE";
	public static final String CONTENTS_PUBLICATION_DATE = "CONTENTS_PUBLICATION_DATE";
	public static final String DATE_OF_USE = "DATE_OF_USE";
//	public static final String PUBLICATION_DATE_OF_USE = "DATE_OF_USE";  //TODO Can this be consolidated to date of use
//	public static final String NUMBER_OF_RECIPIENTS = "NUMBER_OF_RECIPIENTS";
	public static final String DURATION = "DURATION";
	public static final String WEB_ADDRESS = "WEB_ADDRESS";
	public static final String CHAPTER_ARTICLE = "CHAPTER_ARTICLE";
	public static final String CUSTOM_AUTHOR = "CUSTOM_AUTHOR";
	public static final String CUSTOM_VOLUME = "CUSTOM_VOLUME";
	public static final String CUSTOM_EDITION = "CUSTOM_EDITION";
	public static final String PAGE_RANGE = "PAGE_RANGE";
	public static final String REPUBLISHING_ORGANIZATION = "REPUBLISHING_ORGANIZATION";
	public static final String NEW_PUBLICATION_TITLE = "NEW_PUBLICATION_TITLE";
	public static final String TRANSLATION_LANGUAGE = "TRANSLATION_LANGUAGE";
	public static final String IS_TRANSLATED = "IS_TRANSLATED";
	public static final String REPUBLICATION_DESTINATION = "REPUBLICATION_DESTINATION";
	public static final String DATE_OF_ISSUE = "DATE_OF_ISSUE";
	public static final String NUM_CHARTS = "NUM_CHARTS";
	public static final String NUM_EXCERPTS = "NUM_EXCERPTS";
	public static final String NUM_CARTOONS = "NUM_CARTOONS";
	public static final String NUM_FIGURES = "NUM_FIGURES";
	public static final String NUM_GRAPHS = "NUM_GRAPHS";
	public static final String NUM_ILLUSTRATIONS = "NUM_ILLUSTRATIONS";
	public static final String NUM_LOGOS = "NUM_LOGOS";
	public static final String NUM_PHOTOS = "NUM_PHOTOS";
	public static final String NUM_QUOTES = "NUM_QUOTES";
	
	

	
// ItemDescriptionParm Constants	
	public static final String STANDARD_NUMBER = "STANDARD_NUMBER";
	public static final String PUBLISHER = "PUBLISHER";
	public static final String PUBLICATION_YEAR = "PUBLICATION_YEAR";
	public static final String AUTHOR = "AUTHOR";
	public static final String EDITOR = "EDITOR";
	public static final String VOLUME = "VOLUME";
	public static final String EDITION = "EDITION";
	public static final String SERIES = "SERIES";
	public static final String SERIES_NUMBER = "SERIES_NUMBER";
	public static final String PUBLICATION_TYPE = "PUBLICATION_TYPE";
	public static final String COUNTRY = "COUNTRY";
	public static final String LANGUAGE = "LANGUAGE";
	public static final String IDNO_LABEL = "IDNO_LABEL";
	public static final String IDNO_TYPE_CD = "IDNO_TYPE_CD";
	public static final String PAGES = "PAGES";
	public static final String PUB_START_DATE = "PUB_START_DATE";
	public static final String PUB_END_DATE = "PUB_END_DATE";
	public static final String OCLC_NUMBER = "OCLC_NUMBER";
	public static final String IDNO_WOP = "IDNO_WOP";
	// article related metadata fields
	public static final String GRANULAR_WORK_START_PAGE = "GRANULAR_WORK_START_PAGE";
	public static final String GRANULAR_WORK_END_PAGE = "GRANULAR_WORK_END_PAGE";
	public static final String GRANULAR_WORK_VOLUME = "GRANULAR_WORK_VOLUME";
	public static final String GRANULAR_WORK_ISSUE = "GRANULAR_WORK_ISSUE";
	public static final String GRANULAR_WORK_NUMBER = "GRANULAR_WORK_NUMBER";
	public static final String GRANULAR_WORK_PAGE_RANGE = "GRANULAR_WORK_PAGE_RANGE";
	public static final String GRANULAR_WORK_SEASON = "GRANULAR_WORK_SEASON";
	public static final String GRANULAR_WORK_QUARTER = "GRANULAR_WORK_QUARTER";
	public static final String GRANULAR_WORK_WEEK = "GRANULAR_WORK_WEEK";
	public static final String GRANULAR_WORK_SECTION = "GRANULAR_WORK_SECTION";
	
*/	
    public static final String ITEM_SOURCE_CD_WR = "WR";	

// Data Type Constants
	public static final String STRING = "STRING";
	public static final String INTEGER = "INTEGER";
	public static final String DATE = "DATE";
	public static final String NUMBER = "NUMBER";
	public static final String CLOB = "CLOB";
	public static final String BOOLEAN = "BOOLEAN";

// Special Order Type Code Constants
//	public static final String WORK_NOT_FOUND_SPECIAL_ORDER = "WNF";
//	public static final String RIGHT_NOT_FOUND_SPECIAL_ORDER = "RNF";
//	public static final String CONTACT_RIGHTSHOLDER_SPECIAL_ORDER = "CR";
//	public static final String LIMITS_EXCEEDED_SPECIAL_ORDER = "LIM";
//	public static final String NOT_SPECIAL_ORDER = "NSO";
	
// Bundle Constants
//	public static final String ESTIMATED_QTY = "ESTIMATED_QTY";
	
// Fee Constants
	public static final String ENTIRE_BOOK_FEE = "ENTIRE_BOOK_FEE";
	public static final String PER_PAGE_FEE = "PER_PAGE_FEE";
	public static final String BASE_FEE = "BASE_FEE";
	public static final String FLAT_FEE = "FLAT_FEE";
	public static final String MAXIMUM_ROYALTY_FEE = "MAXIMUM_ROYALTY_FEE";

// Yes/No constants
	public static final String YES_CD = "Y";
	public static final String NO_CD = "N";
	
// Item Status Cd
/*	public static final String AWAITING_RESEARCH = "AWAITING_RESEARCH";
	public static final String AWAITING_RIGHTSHOLDER = "AWAITING_RH";
	public static final String DENIED_BY_RIGHTSHOLDER = "DENIED_BY_RH";
	public static final String AWAITING_LCN = "AWAITING_LCN";
	public static final String CANCELLED_BY_LCN = "CANCELLED_BY_LCN";
	public static final String CANCELLED_BY_AGING = "CANCELLED_BY_AGING";
	public static final String INVOICE_READY = "INVOICE_READY";
	public static final String INVOICED = "INVOICED";
*/	
// Item Availability Cd	
	
// Rightholder Messages
	// Yes/No constants
	public static final String NO_RIGHTSHOLDER = "No Rightsholder";
	public static final String MULTIPLE_RIGHTSHOLDERS = "Multiple Rightsholders";

// Billing Status Constants for derived value	
	public static final String NOT_INVOICED = "Not Billed";
	
	public static final String INVOICED = "Invoiced";
	public static final String PAID_BY_CREDIT_CARD = "Charged to Credit Card";
	public static final String PAID = "Paid";
	public static final String CANCELED = "Canceled";
	public static final String PAYMENT_NA = "N/A";
	
	public static final String BILLING_STATUS_NOT_BILLED_CD = "NOT_BILLED";
	public static final String BILLING_STATUS_BILLED_CD = "BILLED";
	public static final String BILLING_STATUS_CANCELED_CD = "CANCELED";
	public static final String BILLING_STATUS_INVOICED_CD = "INVOICED";
	public static final String BILLING_STATUS_CHARGED_TO_CREDIT_CARD = "CHARGED_TO_CREDIT_CARD";
	public static final String BILLING_STATUS_AWAITING_LCN_CONFIRM = "AWAITING_LCN_CONFIRM";
	
	public static final String SPECIAL_ORDER_UPDATE_AWAITING_LCN_CONFIRM = "AWAITING_LCN_CONFIRM";
	public static final String SPECIAL_ORDER_UPDATE_AWAITING_LCN_REPLY = "AWAITING_LCN_REPLY";
	public static final String SPECIAL_ORDER_UPDATE_AWAITING_RH = "AWAITING_RH";
	
	public static final String WITHDRAWN_BY_CUST = "CUST";
	
// Payment Type Constants for derived value
	public static final String PAYMENT_TYPE_CREDIT_CARD = "Credit Card";
	public static final String PAYMENT_TYPE_INVOICE = "Invoice";
	public static final String PAYMENT_METHOD_CREDIT_CARD = "CREDIT_CARD";
	public static final String PAYMENT_METHOD_INVOICE = "INVOICE";
	
	public static final String PERMISSION_STATUS_CHECKING_AVAILABILITY = "CHECKING_AVAILABILITY";
	public static final String PERMISSION_STATUS_CONTACT_DIRECTLY = "CONTACT_DIRECTLY";
	public static final String PERMISSION_STATUS_DENY = "DENY";
	public static final String PERMISSION_STATUS_GRANT = "GRANT";
	public static final String PERMISSION_STATUS_CANCELED = "CANCELED";
	
	public static final String REQUEST_HAS_BEEN_CANCELED = "Request has been canceled";
	public static final String REQUEST_AWAITING_FULFILLMENT = "Awaiting fulfillment";
	
	public static final String CURRENCY_CODE_USD = "USD";

	public static final String RL_PRODUCT_PARM = "RIGHTID";
	public static final String RL_PRODUCT_REPRINT_PARM_VALUE = "2";
	public static final String RL_PUBLISHERID_PARM = "PUBLISHERID";
	public static final String RL_TYPEOFUSEID_PARM = "TYPEOFUSEID";
	public static final String RL_BMJ_PUBLISHERID = "57";
	public static final String RL_BMJ_TYPEOFUSEID = "105";
	public static final String RL_NYT_PUBLISHERID = "7";
	public static final String RL_NYT_TYPEOFUSEID = "105";
	
	public static final String PAID_BYCC_24HRS = "This item is charged to your credit card within 24 hours by our Rightslink service";
	public static final String INVOICED_24HRS = "This item is invoiced within 24 hours through our Rightslink service.";
	
	public static final String MERCHANT_REFERENCE_MULTIPLE = "MULTIPLE";

	public static final String COST_TBD = "$TBD";
	public static final String COST_ZERO_DOLLAR = "$ 0.00";
	
	public static final double RL_NOT_PRICED = -62543.89;
	
	public static final String TOTAL_COURSE = "COURSE";
	public static final String TOTAL_SINGLE = "SINGLE";
	public static final String TOTAL_ORDER = "ORDER";

	public static final String ORDER_STATE_OPEN = "OPEN";
	public static final String ORDER_STATE_CLOSED = "CLOSED";
	public static final String ORDER_STATE_ALL = "ALL";
	
	public static final String ORDER_STATUS_COMPLETE_CD = "COMPLETE";
	public static final String ORDER_STATUS_COMPLETE_DESC = "Complete";
	public static final String ORDER_STATUS_NOT_COMPLETE_CD = "COMPLETE";
	public static final String ORDER_STATUS_NOT_COMPLETE_DESC = "Not Complete";
	
	public static final String LIMITS_EXCEEDED_LABEL = "(Limits Exceeded)";
	
	public static final int MAX_CONFIRM_ROWS = 2000;
	
}
