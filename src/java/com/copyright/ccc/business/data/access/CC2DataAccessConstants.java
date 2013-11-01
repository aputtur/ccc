package com.copyright.ccc.business.data.access;

import com.copyright.opi.Query;

public class CC2DataAccessConstants
{
    /**
     * Contains constant values pertaining to administrative services.
     */
    public static final class Admin
    {
    	private Admin() {}
    	
        public static final Query GET_SCHEMA_VERSION = 
            new Query( "admin_util_pkg", "get_schema_version", 2 );
    }
    
    /**
     * Contains constant values pertaining to <code>CCUser</code> objects.
     */
    public static final class CCUser
    {
    	private CCUser() {}
    	
        public static final Query READ_QUERY = new Query( "CCC_User_pkg", "ReadUser", 3 );
        
        public static final Query READ_FOR_AUID_QUERY = 
            new Query( "CCC_User_pkg", "ReadUserForAUID", 3 );
        public static final Query READ_FOR_PARTY_ID_QUERY = 
            new Query( "CCC_User_pkg", "ReadUserForPartyID", 3 );
        public static final Query READ_FOR_PK_QUERY = 
            new Query( "CCC_User_pkg", "ReadUserForOIDPK", 3 );
        public static final Query READ_FOR_IDENTIFIER_QUERY = 
            new Query( "CCC_User_pkg", "ReadUserForUserIdentifier", 3 );
        public static final Query READ_FOR_NEW_SESSION_QUERY = 
            new Query( "CCC_User_pkg", "ReadUserForNewSession", 3 );
        public static final Query READ_FOR_LOGIN_USERNAME_QUERY = 
            new Query( "CCC_User_pkg", "ReadUserForLoginUsername", 3 );

        public static final Query INSERT_QUERY = new Query( "CCC_User_pkg", "InsertUser", 3 );
        public static final Query UPDATE_QUERY = new Query( "CCC_User_pkg", "UpdateUser", 3 );
        public static final Query DELETE_QUERY = new Query( "CCC_User_pkg", "DeleteUser", 2 );

        public static final Query GET_AUTODUNNING_PARAMS = new Query("ccc_autodunning_on_demand_pkg", "get_autodunning_params", 2);
        /* public static final Query WRITE_INV_PMT_LOG = new Query("CCC_INV_PMT_LOG_PKG", "LOG_INVOICE_PAYMENT", 2);
        public static final Query READ_INV_PMT_LOGS_BY_DATE_RANGE = 
        	new Query( "CCC_INV_PMT_LOG_PKG", "READ_ENTRIES_BY_DATE_RANGE", 4); */
        
        
        public static final Query UPDATE_PASSWORD_ENFORCEMENT =
            new Query( "CCC_User_pkg", "UpdatePasswordEnforcement", 3 );
        
        public static final Query READ_USER_BY_USERID =
            new Query( "CCC_User_pkg", "ReadUserByUserID", 3 );
    }
    
    /**
     * Contains constant values pertaining to <code>RLinkPublisher</code> objects.
     */
    public static final class RLinkPublisher
    {
    	private RLinkPublisher() {}
    	
        public static final Query GET_PUBLISHER_BY_PTY_INST = 
            new Query( "CCC_RLINK_PUBLISHER_PKG", "get_rlink_pub_by_pty_inst", 3 );
            
        public static final Query GET_PUBLISHER_BY_PTY_WRK_INST = 
            new Query( "CCC_RLINK_PUBLISHER_PKG", "get_rlink_pub_by_pty_wrk_inst", 4 );
            
        public static final Query GET_PUBLISHER_BY_PUB_ID = 
            new Query( "CCC_RLINK_PUBLISHER_PKG", "get_rlink_publisher_by_pub_id", 3 );
            
        public static final Query CREATE_RLINK_PUBLISHER = 
            new Query( "CCC_RLINK_PUBLISHER_PKG", "create_rlink_publisher", 3 );
            
        public static final Query UPDATE_RLINK_PUBLISHER = 
            new Query( "CCC_RLINK_PUBLISHER_PKG", "update_rlink_publisher", 2 );
            
        public static final Query DELETE_RLINK_PUBLISHER = 
            new Query( "CCC_RLINK_PUBLISHER_PKG", "delete_rlink_publisher", 2 );
            
        public static final Query GET_PUBLISHER_BY_ACCOUNT = 
            new Query( "CCC_RLINK_PUBLISHER_PKG", "get_rightsholder_by_account", 3 );
            
        public static final Query GET_PUBLISHERS = 
                new Query( "CCC_RLINK_PUBLISHER_PKG", "get_publishers", 2 );
                
        public static final Query CREATE_RLINK_PUBLISHER_DETAIL = 
                new Query( "CCC_RLINK_PUBLISHER_PKG", "create_rlink_publisher_detail", 2 );
                
        public static final Query DELETE_RLINK_PUBLISHER_DETAIL = 
                new Query( "CCC_RLINK_PUBLISHER_PKG", "delete_rlink_publisher_detail", 2 );
                
        public static final Query GET_PUBLISHER_DETAIL_BY_ID = 
                new Query( "CCC_RLINK_PUBLISHER_PKG", "get_rlink_pub_detail_by_id", 3 );
                           
        }

    public static final class CustUser
    {
    	private CustUser() {}
    	
        public static final Query GET_OVERDUE_INVOICES = new Query("CCC_TRANS_DUNNING_EMAIL_PKG", "get_invoices", 4);

    }

    public static final class OrderPartnerXref
    {
        private OrderPartnerXref() {}

        public static final Query READ_XREF_BY_CONFIRM_NUMBER = new Query(
            "CCC_ORDER_PARTNER_PKG", "READ_XREF_BY_CONFIRM_NUMBER", 3
        );

        public static final Query WRITE_XREF = new Query(
            "CCC_ORDER_PARTNER_PKG", "WRITE_XREF", 2
        );
    }
}
