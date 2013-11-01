package com.copyright.ccc.web.dispatcher;

/**
 * Provides a set of constant values that are used by the CC2 dispatcher.
 */
 public class DispatcherConstants {

    public static final String ND_DEFAULT_URL            = "/error404.do";

    public static final String ND_INTERNAL_REQUEST_URL   = "INTERNAL";

    public static final String ND_ARG_ORDERID            = "id";
    public static final String ND_ARG_PASSWORD           = "password";
    public static final String ND_ARG_TARGET             = "target";
    public static final String ND_ARG_TYPE               = "type";
    public static final String ND_ARG_USEREDIRECT        = "useredir";
    public static final String ND_ARG_USERNAME           = "username";
    
    public static final String ND_ARG_USERID             = "userid";
    public static final String ND_ARG_CARTID             = "cartid";
    
    public static final String TYPE_ORDER = "ORDER";
    public static final String TYPE_UPDATE_CART = "UPDATECART";
      
    public static final String TARGET_ORDER_HISTADMDET = "HISTADMDET";
    public static final String TARGET_ORDER_HISTADM = "HISTADM";
    public static final String TARGET_ORDER_HISTDET = "HISTDET";
    public static final String TARGET_ORDER_HISTDETID = "HISTDETID";
    public static final String TARGET_ORDER_HIST = "HIST";
    
    public static final String TARGET_USER_ID = "EMULATEDUSER";
    
    static final String[] OPTIONAL_PARMS_NOT_SPECIFIED = null;
    static final String[] AT_LEAST_ONE_PARMS_NOT_SPECIFIED = null;
}

