package com.copyright.ccc.web;


public class WebConstants
{
	private WebConstants() {}

    public static final String PERMISSION_TYPE_APS = "aps";
    public static final String PERMISSION_TYPE_ECCS = "eccs";
    public static final String PERMISSION_TYPE_TRS = "trs";
    public static final String PERMISSION_TYPE_DPS = "dps";
    public static final String PERMISSION_TYPE_RLS = "rls";

    public static final String APPLICATION_ERROR = "/applicationError.do";
    public static final String LOGIN_ERROR = "/loginFailure.do";

    public static final String IS_AJAX_RESPONSE = "com.copyright.isajax";
    public static final String IS_POPUP = "com.copyright.ispopup";

    public static final String RESET_PASSWORD_URL = "/resetPassword.do";

    public static class RequestKeys
    {
    	private RequestKeys() {}
    	
    	  /**
         * Request key used to store flag tracking whether or not the current
         * request is a request for authorization.
         */
    	
    	public static final String ISSN          = "issn";
    	public static final String ISBN          = "isbn";
    	public static final String DATE          = "date";
    	public static final String TITLE         = "title";
    	public static final String PNO 			 = "pno";
    	public static final String IDX 			 = "idx";
    	public final static String ITEM 		 = "item";
        public final static String PERM 	     = "perm";
        public final static String EMAIL 		 = "EMAIL";
    	
        public static final String HELP_TITLE_ID = "helpTitleId";
        public static final String HELP_BODY_ID  = "helpBodyId";
        
        public final static String CHAPTER_ARTICLE 			=	"chapterarticle";
        public final static String FIRST_PAGE 				=	"firstpage";
        public final static String LAST_PAGE  				=	"lastpage";
        public final static String PAGES 					=	"pages";
        public final static String COPIES 					=	"copies";
        
        
        public static final String USERNAME  				= "username";
        public static final String PASSWORD  				= "password";
    	
    	public static final String ORDER_PURCHASE 			= 	"orderPurchase";
    	public static final String PUBLICATION_PERMISSION 	= 	"publicationPermission";
        public static final String REQUEST_IS_AUTH 			= 	"__CC2_REQUEST_IS_AUTH__";
        public static final String PREV_REQUEST_PATH 		= 	"__CC2_PREV_REQUEST_PATH__";

        
        public static final String IS_CANCEL_VIA_HISTORY_ENABLED 	= "__CC2_WEB_IS_CANCEL_VIA_HISTORY_KEY__";
        
        public static final String LOGIN_EXCEPTION					= "__CC2_LOGIN_EXCEPTION__";
        
        public static final String REQUEST_URI_SEQUENCE 			= "__CC2_REQUEST_PATH_SEQUENCE__";
       
        public static final String RIGHTSLINK_INCOMPLETE_EXCEPTION 		= "__CC2_RLINK_INCOMPLETE_EXCEPTION__";
        
        public static final String SESSION_EXPIRED_MSG 				= "__CC2_WEB_SESSION_EXPIRED_MSG_KEY__";
        
        public static final String COURSE_DETAILS				 	= "courseDetails";
        public static final String TERMS_ITEM 						= "termsItem";
        public static final String CASCADE_UPDATE_EXCEPTION 		= "cascadeUpdateExceptions";
        public static final String RECALCULATE_PRICE 				= "recalculatePrice";
        public static final String MISSING_TF 						= "specialOrderMissingTF";
        
        public static final String PURCHASE_ID 						= "id";
        public static final String PURCHASABLE_PERMISSIONS_TO_COPY  = "purchPermsToCopy";
        public static final String PURCHASABLE_PERMISSION_IN_CART   = "purchPermInCart";
        public static final String PURCHASABLE_PERMISSION_TO_ADD    = "purchPermToAdd";
        
        public static final String TRANSACTION_ITEM 				= "transactionItem";
        public static final String PRICING_ERROR					= "pricingError";
        
        
        public static final String COPY_ORDER_ERROR_PUBLICATION_TITLES = "copyOrderErrorTitles";
        public static final String ORDER_CASCADE_UPDATE_ERROR_STRINGS  = "orderCascadeUpdateErrors";
        public static final String ORDER_CASCADE_UPDATE_CLOSED_STRINGS = "orderCascadeUpdateClosed";
        public static final String ORDER_CASCADE_CANCEL_INVOICED_STRINGS = "orderCascadeCancelInvoiced";
        public static final String ORDER_CASCADE_EDIT_INVOICED_STRINGS = "orderCascadeEditInvoiced";
        
        public static final String PERMISSIONS_DIRECT_TRANSACTION_ITEM 	=	 "isPermissionsDirectTransactionItem";
        public static final String SELECT_PERMISSION_PATH 				= 	"selectPermissionPath";
        public static final String SPECIAL_PERMISSION_TYPE 				= 	"specialPermissionType";
    }

    public static class SessionKeys
    {
    	private SessionKeys() {}
    	
    	/**
         * Session key used to store an uncaught <code>Exception</code>.
         */
        public static final String APPLICATION_EXCEPTION 								=	"__CC2_APPLICATION_EXCEPTION__";
        /**
         * Session key used to store a friendly message relating to an uncaught
         * <code>Exception</code>.
         */
        public static final String APPLICATION_EXCEPTION_FRIENDLY_MSG 					=	"__CC2_APPLICATION_EXCEPTION_FRIENDLY_MSG";
        /**
         * Session key used to store a message relating to a
         * <code>NotAuthorizedRuntimeException</code>.
         */
        public static final String NOT_AUTHORIZED_MSG 									=		"__CC2_NOT_AUTHORIZED_MSG";
        /**
         * Session key used to store flag tracking whether or not the user has
         * encountered a session timeout.
         */
        public static final String REDIRECTED_DUE_TO_SESSION_TIMEOUT 					=	"__CC2_REDIRECTED_DUE_TO_SESSION_TIMEOUT__";
        /**
         * Session key used to store flag indicating whether or not an invalid
         * session was invalidated directly by the CC application.
         */
        public static final String SESSION_INVALIDATED_BY_APPLICATION 					=	"__CC2_SESSION_INVALIDATED_BY_APPLICATION__";
        
        
        public static final String SESSION_IS_NEWLY_CREATED 							=	"__CC2_SESSION_IS_NEWLY_CREATED__";
        public static final String CC2_USER_CONTEXT 									=	"__CC2_USER_CONTEXT__";
                
        public static final String RIGHTSLINK_SESSION_ID 								= 	"__CC2_SESSION_RIGHTSLINK_SESSION_ID__";
        public static final String RIGHTSLINK_COPY_ORDER_ERROR_PUBLICATION_TITLES 		= 	"rlCopyOrderErrorTitles";
        
        public final static String SKIP_QUICKPRICE 										= 	"skip_quick_price";
        public static final String SELECTED_PUBLISHER 									=	"selectedPublisher";
        
        public final static String ITEM 						= 	"item";
        public final static String PERM 						=	"perm";
        
        public final static String CHAPTER_ARTICLE 				=	"chapterarticle";
        public final static String FIRST_PAGE		 			=	"firstpage";
        public final static String LAST_PAGE  					=	"lastpage";
        public final static String PAGES  						=	"pages";
        public final static String COPIES 						=	"copies";
        
        public final static String FUNDING_CURRENCY_TYPE 		= 	"fundingCurrencyType"; 
        public final static String FUNDING_CURRENCY_RATE_ID		= 	"fundingCurrencyRateId";
        public final static String FUNDING_CURRENCY_AMOUNT		= 	"fundingCurrencyAmount";  
        public final static String EXCHANGE_RATE 				= 	"exchangeRate";
        public final static String EXCHANGE_RATE_DATE 			= 	"exchangeRateDate";
        
                    
        // all forms 
        public static final String LANDING_FORM 					= 	"landingForm";
        public static final String LOGIN_FORM 						= 	"loginForm";
        public static final String NOT_AVAILABLE_EMAIL_FORM 		= 	"notAvailableEmailForm";
        public static final String RIGHTSLINK_OH_FORM 				= 	"rlOrderHistoryForm";
        public static final String RIGHTSLINK_LOGIN_MESSAGE 			= 	"rightslink_login_message";
        public static final String RIGHTSHOLDER_CONTACT_INFO_FORM	= 	"rightsholderContactInfoForm";
        public final static String REGISTER_INDV_FORM 				=	"RegisterIndividualForm";
        public final static String REGISTER_ORG_FORM 				=	"RegisterOrganizationForm";
        public final static String REGISTER_NEW_USER_FORM 			=	"RegisterAddUserForm";
        public final static String REGISTER_GPO_USER_FORM 			=	"RegisterGPOUserForm";
        public static final String SEARCH_FORM 						= 	"searchForm";
        public static final String UNPAID_INVOICE_FORM 				= 	"unpaidInvoiceForm";
        public static final String UNPAID_SPECIALORDER_FORM 		= 	"UnpaidSpecialOrderForm";
        public static final String MANAGE_CREDIT_CARDS_FORM         = "manageCreditCardsForm";
        
        public static final String PARM_EXISTING_ORDER				= 	"existingOrder";
        public static final String PARM_PURCHASE_ID  				= 	"purchaseId";
        
        public static final String PURCHASE_ID 						=	"id";
        
        public static final String STATUS_FILTER 					=	"statusFilter";

        public static final String MAIN_SEARCH_SPEC 				= 	"mainSearchSpec";
        public static final String DETAIL_SEARCH_SPEC 				= 	"detailSearchSpec";
        
        public static final String EMAIL_COUNT 						= 	"email_count";
        public static final String CCC_APPLICATION_EXCEPTION_TRACE 	= 	"cc_application_exception_trace";
        
        public static final String PURCHASABLE_PERMISSION_TO_ADD 				= 	"purchPermToAdd";
        public static final String PURCHASABLE_PERMISSIONS_TO_COPY 				= 	"purchPermsToCopy";
        public static final String PURCHASABLE_PERMISSIONS_TO_EDIT 				= 	"purchPermsToEdit";
        public static final String PURCHASABLE_PERMISSIONS_NON_ACADEMIC_TO_COPY = 	"purchPermsnNonAcademicToCopy";
        public static final String PURCHASABLE_PERMISSIONS_NON_ACADEMIC_TO_EDIT = 	"purchPermsNonAcademicToEdit";
        
        public static final String COPY_ORDER_ERROR_PUBLICATION_TITLES 			= 	"copyOrderErrorTitles";
        
        public static final String UTM_SOURCE = "utm_source";

        public static final String ANONYMOUS_UNPAID_INVOICE_FORM = "anonymousUnpaidInvoiceForm";

        public static final String IS_CEMPRO = "isCempro";
    }

    /**
     * Emulated user cart id.
     */
    public static final String EMULATED_USER_CART_ID = "cartId";

    /**
     * Emulated user cart.
     */
    public static final String EMULATED_USER_ID = "userid";

    /**
     * Enforce Password Change.
     */
    public static final String ENFORCE_PWD_CHG = "enforcePwdChg";
}
