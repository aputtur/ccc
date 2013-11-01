/*
 * CC2Configuration.java
 * Copyright (c) 2006, Copyright Clearance Center, Inc. All rights reserved.
 * ----------------------------------------------------------------------------
 * Revision History
 *
 * 2007-10-15   tmckinney    Added 'Not Available' Email properties.
 * 2006-12-04   ccollier     Created.
 * ----------------------------------------------------------------------------
 */
package com.copyright.ccc.config;

import org.apache.commons.lang.StringUtils;

import com.copyright.crs.client.api.CRSConfiguration;
import com.copyright.workbench.net.SimpleIPAddressRange;

public class CC2Configuration extends CRSConfiguration 
{
	private static final long serialVersionUID = 1L;
    private static CC2Configuration _instance;
    
	public static final String MODULE_NAME = "CCC";
	public static final String MODULE_VERSION = "3.13";
    public static final String CONFIG_FILE = "CC2Configuration.properties";
    
    /*
     * Cempro Content URL's
     */
    private static final String CEMPRO_MAIN_MENU_URL = "cempro.main.menu.url";
    private static final String CEMPRO_HEADER_URL = "cempro.header.url";
    private static final String CEMPRO_RIGHT_URL = "cempro.right.url";
    private static final String CEMPRO_FOOTER_URL = "cempro.footer.url";
    
    private static final String MAIN_DROPDOWN_MENU_URL = "main.dropdown.menu.url";
    private static final String MAIN_FOOTER_LINKS_URL = "main.footer.links.url";
    private static final String NOT_AUTHORIZED_ACCESS_URL = "not.authorized.access.url";

    private static final String DATASOURCE_JNDI_NAME = "db.datasource.jndi";
    private static final String SCHEMA_OWNER = "db.schema.owner";

    private static final String COOKIE_AUID_EXPIRATION_DAYS = "cookie.auid.expiration.days";
    
    private static final String IP_INTERNAL_RANGE = "ip.internal.range";
    
    private static final String STATIC_CONTENT_ROOT = "static.content.root";
    private static final String STATIC_CONTENT_NOT_FOUND_MESSAGE_FMT = "static.content.not.found.message.fmt";

    private static final String GPO_URL = "gpo.URL";
    private static final String GPO_LOG = "gpo.LOG";
    
    private static final String HELP_URL = "help.url";

    private static final String GUIDE_ADDRESS = "email.form.guide.to";
    private static final String RESUME_ADDRESS = "email.form.resume.to";
    private static final String POSTER_ADDRESS = "email.form.poster.to";
    private static final String FEEDBACK_ADDRESS = "email.form.feedback.to";
    private static final String HA_ADDRESS = "email.form.ha.to";
    private static final String IMAGES_ADDRESS = "email.form.images.to";
    
    // OpenURLImageServer
    private static final String IMAGE_MIME_TYPE = "resolver.image.mimetype";
    private static final String IMAGE_AVAILABLE = "resolver.image.available";
    private static final String IMAGE_UNAVAILABLE = "resolver.image.unavailable";
    private static final String CRITERIA_DEFAULT = "resolver.criteria.default";
    private static final String CRITERIA_INST = "resolver.criteria.inst";
    private static final String CRITERIA_ABRV = "resolver.criteria.abrv";
    private static final String PRODUCT_INST = "resolver.product.inst";
    private static final String PRODUCT_ABRV = "resolver.product.abrv";
    private static final String PRODUCT_DEF_INST = "resolver.product.default.inst";
    private static final String PRODUCT_DEF_ABRV = "resolver.product.default.abrv";
    private static final String PRODUCT_ACADEMIC_INST = "resolver.product.academic.inst";
    private static final String PRODUCT_ACADEMIC_ABRV = "resolver.product.academic.abrv";
    private static final String PRODUCT_BUSINESS_INST = "resolver.product.business.inst";
    private static final String PRODUCT_BUSINESS_ABRV = "resolver.product.business.abrv";
    private static final String NETWORK_PROTOCOL = "resolver.network.protocol";
    private static final String PATH_SEPARATOR="resolver.path.separator";
    private static final String HOST_URL = "resolver.host.url";
    private static final String IMAGE_DIR = "resolver.image.dir";
    
    // Services
    private static final String rightsWebServiceURL = "svcRights.rights.endpointURL";
    private static final String arTransactionServiceURL = "svcArTransaction.transaction.endpointURL";
    private static final String searchRetrievalWebServiceURL = "svcSearchRetrieval.searchRetrieve.endpointURL";
    private static final String rlOrderServiceURL = "svcRLOrders.order.endpointURL";
    private static final String rlUserServiceURL = "svcRLUser.user.endpointURL";
    private static final String AdjustmentServiceURL = "svcAdjustment.adjustment.endpointURL";
    private static final String CartServiceURL = "svcOrder.cart.endpointURL";
    private static final String OrderServiceURL = "svcOrder.order.endpointURL";
    private static final String tfWebServiceURL = "svcTf.tf.endpointURL";
    private static final String ldapServiceURL = "svcLdapUser.ldap.endpointURL";
    private static final String telesalesWebServiceURL = "svcTelesales.telesales.endpointURL";
    private static final String telesalesCompositeServiceURL = "svcTelesales.telesalesComposite.endpointURL";
    private static final String telesalesRightsholderServiceURL = "svcTelesales.telesalesRightsholder.endpointURL";
    private static final String centralQueueServiceURL = "svcCentralQueue.cq.endpointURL";
    private static final String rightsResolverServiceURL = "svcRightsResolver.rr.endpointURL";
    private static final String rightsProcessorServiceURL = "svcRightsResolver.rp.endpointURL";
    private static final String worksRemoteSearchServiceURL = "svcWorksRemote.search.endpointUrl";
    private static final String cyberSourceServiceURL="svcUserInfo.cyberSource.endpointUrl";
    private static final String accessControlServiceURL="svcUserInfo.accessControl.endpointUrl";
    private static final String extEmailServiceURL = "svcExtEmail.send.endpointURL";
    private static final String openUrlExtensionsServiceURL = "svc-rest-openUrlExtensions.endpointURL";

    // Auto-dunning
    private static final String quartzEmailSchedule = "autodunning.quartz.email.schedule";
    private static final String quartzSchedulerEnable = "autodunning.quartz.scheduler.enable";
    private static final String autoDunningTestModeEnable = "autodunning.test.mode.auto.dunning.enable";
    private static final String testDefaultEmailRecipient = "autodunning.test.default.email.recipient";
    private static final String testDefaultTotalNumberEmailSent = "autodunning.test.default.total.number.email.sent";
    private static final String overdueInvoiceEmailSender = "autodunning.overdue.invoice.email.sender";    
    private static final String overdueInvoiceDaysPastDue = "autodunning.overdue.invoice.days.past.due";
    private static final String overdueInvoiceEmailTemplate = "autodunning.overdue.invoice.email.template";
    private static final String autoDunningStatusLogReceiver = "autodunning.status.log.receiver";
    private static final String autoDunningStatusLogLocation = "autodunning.status.log.location";
    private static final String freemarkerTemplateLocation = "autodunning.freemarker.template.location";
    
    // Requested Title Report
    private static final String reqtitleEmailSubject = "reqtitle.email.subject";
    private static final String reqtitleEmailFrom = "reqtitle.email.from";
    private static final String reqtitleEmailBody = "reqtitle.email.body";
    private static final String reqtitleEmailTo = "reqtitle.email.to";
    private static final String reqtitleTmpDir = "reqtitle.tmp.dir";
    private static final String reqtitleAttachmentName = "reqtitle.attachment.name";
    private static final String reqtitleQuartzSchedule = "reqtitle.quartz.schedule";
    private static final String reqtitleJobEnabled = "reqtitle.job.enabled";

    // Email Verification
    private static final String EMAIL_VERIFICATION_ENABLED = "email.verification.enabled";
    private static final String EMAIL_VERFICATION_INTRANET_CHECK_ENABLED = "email.verification.intranet.check.enabled";
    private static final String EMAIL_VERFICATION_TIMEOUT = "email.verification.timeout";
    private static final String EMAIL_VERFICATION_USERNAME = "email.verification.username";
    private static final String EMAIL_VERFICATION_PASSWORD = "email.verification.password";
    private static final String EMAIL_VERFICATION_SERVICE_URL = "email.verification.service_endpoint";
    private static final String EMAIL_VERFICATION_SERVICE_OPERATION = "email.verification.service_operaton";
    
    // Search secondary search stop-word list and other search constants.
    private static final String MANUAL_STOPWORDS_LIST = "search.manual.stopwords";
    private static final String AUTOSUGGEST_SVC_ENDPOINT = "autosuggest.svc_endpoint";
    private static final String AUTOSUGGEST_ITEMS = "autosuggest.items";
    private static final String AUTOSUGGEST_COUNT = "autosuggest.count";
    private static final String SEARCHINSTEAD_ITEMS = "searchinstead.items";
    private static final String SEARCHINSTEAD_COUNT = "searchinstead.count";
     
    // System status
    private static final String SIMLUATE_RIGHTSLINK_DOWN = "simluate.rightslink.down";
    
    // Authentication
    private static final String RIGHTSLINK_AUTHENTICATION_TOKEN = "rightslink.authentication.token";
     
    // Prefetch links
    private static final String RIGHTSLINK_LINKS_PREFETCH = "rightslink.links.prefetch";

    // Max # of times to retry rightslink request after new login
    private static final String RIGHTSLINK_REQUEST_MAX_RETRIES = "rightslink.request.max-retries";

    // # of millis to sleep between each rightslink request retry
    private static final String RIGHTSLINK_REQUEST_RETRY_SLEEP_MILLIS = "rightslink.request.retry.sleep.millis";

    /* Do we support OMS adjustment app functionality?
     * Note: this only affects OMS,CyberSource adjustments, the TF-backed payflowpro
     * adjustment functionality (only used by Rightsphere) will still need to be used.
     */ 
    private static final String DISABLE_OMS_ADJUSTMENTS_KEY = "adjustments.oms.disable";
    /*
     * Indicates whether the forcible simulation of Rightslink being down
     * is turned on.
     * This variable allows us to test scenarios where RL goes down during a users
     * session without having to reload the properties file - which today
     * requires a restart of the app. Please see the SystemStatusAction for more
     * details.
     */
    private Boolean forciblySimulateRLDown = false;
    
    private static final String NRLS_USER_FEE = "nrls.user.fee";
    
    private static final String RLINK_SPECIALORDER_IDNO = "rlink.specialOrder.idno" ;
    
    private CC2Configuration()
    {
    	super(CONFIG_FILE, MODULE_NAME, MODULE_VERSION, null);
    }
    

    /**
     * Returns true if we are forcing the simulation of Rightslink
     * being down
     * @return
     */
    public Boolean getForciblySimulateRLDown() {
		return forciblySimulateRLDown;
	}

    /**
     * Turns on/off the forcible simulation of Rightslink being down.
     * This allows us to test scenarios where RL goes down during a users
     * session without having to reload the properties file - which today
     * requires a restart of the app. Please see the SystemStatusAction for more
     * details.
     */
	public void setForciblySimulateRLDown(Boolean downSimulationForced) {
		forciblySimulateRLDown = downSimulationForced;
	}


	/**
     * Client code should use this method to get a handle on the 
     * <code>CC2Config</code> object.
     */
    public static synchronized CC2Configuration getInstance()
    {
        if ( _instance == null )
        {
        	_instance = new CC2Configuration();
        }
        
        return _instance;
    }
    
    /**
     * Returns the NRLS user fee value
     */
    public String getNrlsUserFeeValue()
    {
        return this.getString( NRLS_USER_FEE );
    }
    
    /**
     * Indicates if the OMS adjustment functionality should be disabled.
     * <p>
     * If this is false, OMS adjustment functionality should not be made available.
     * </p>
     * @return {@code true} if OMS CyberSourceAdjustments should be disabled, {@code false} otherwise.
     */
    public boolean isDisableOmsAdjustments()
    {
    	return this.getBoolean( DISABLE_OMS_ADJUSTMENTS_KEY );
    	
    }
    
    /**
     * Returns the JNDI name to be used to look up the data source.
     * It is important to ensure that this results in pooled connections.  The EJB-Location
     * of an emulated data source (such as the evermind data source) provides connection pooling.
     * See additional comments in CopyrightDotCom.properties
     */
    public String getDatasourceJNDIName()
    {
        return this.getString( DATASOURCE_JNDI_NAME );
    }
        
    /**
     * Returns the owner of the schema in which the packages and types reside.
     */
    public String getSchemaOwner()
    {
        // If the schema owner has been specified on the command line, use 
        // that value.
        if ( ! StringUtils.isEmpty( CC2Constants.SCHEMA_OWNER ) )
        {
            return CC2Constants.SCHEMA_OWNER;
        }

        return this.getString( SCHEMA_OWNER );
    }
    
    public int getCookieAUIDExpirationDays()
    {
        return this.getInt( COOKIE_AUID_EXPIRATION_DAYS );
    }
    
    public String getNotAuthorizedAccessURL()
    {
        return this.getString( NOT_AUTHORIZED_ACCESS_URL );
    }
    
    /**
     * Returns the range of internal CCC IP addresses.
     */
    public SimpleIPAddressRange getIPInternalRange() {
        return new SimpleIPAddressRange( this.getString( IP_INTERNAL_RANGE ) );
    }

    public String getStaticContentRoot()
    {
        return this.getString( STATIC_CONTENT_ROOT );
    }
    
    public String getStaticContentNotFoundMessageFmt()
    {
        return this.getString( STATIC_CONTENT_NOT_FOUND_MESSAGE_FMT );
    }

    public String getGPOURL()
    {
        return this.getString( GPO_URL );
    }
    
    public String getGPOLOG()
    {
        return this.getString( GPO_LOG );
    }
    
    public String getHelpURL() { return this.getString( ConfigurationHelper.getExposureBasedKey(HELP_URL)); }
    
    public String getGuideEmailTo() {
        return this.getString( GUIDE_ADDRESS );
    }
    
    public String getResumeEmailTo() {
        return this.getString( RESUME_ADDRESS ); 
    }
    
    public String getPosterEmailTo() {
        return this.getString( POSTER_ADDRESS );
    }
    
    public String getFeedbackEmailTo() {
        return this.getString( FEEDBACK_ADDRESS ); 
    }
    
    public String getHAEmailTo() {
        return this.getString( HA_ADDRESS );
    }
    
    public String getImagesEmailTo() {
        return this.getString( IMAGES_ADDRESS );
    }
    
    // Not Available Email
    private static final String NOTAVAILABLE_TO = "email.notavailable.recipient";
    private static final String NOTAVAILABLE_FROM = "email.notavailable.from";
    private static final String NOTAVAILABLE_SUBJECT = "email.notavailable.subject";
    public String getNotAvailableTo() { return this.getString( NOTAVAILABLE_TO ); }
    public String getNotAvailableFrom() { return this.getString( NOTAVAILABLE_FROM ); }
    public String getNotAvailableSubject() { return this.getString( NOTAVAILABLE_SUBJECT ); }
    
    //  Image Resolver Methods
    public String getMimetype() { return this.getString( IMAGE_MIME_TYPE ); }
    public String getURLImageAvailable() { return this.getString( IMAGE_AVAILABLE ); }
    public String getURLImageUnavailable() { return this.getString( IMAGE_UNAVAILABLE ); }
    public String getCriteriaDefault() { return this.getString( CRITERIA_DEFAULT ); }
    public String getProtocol() { return this.getString( NETWORK_PROTOCOL ); }    
    public String getHostURL() { return this.getString( ConfigurationHelper.getExposureBasedKey(HOST_URL) ); } 
    public String getImageDir() { return this.getString( IMAGE_DIR ); }
    public String getPathSeparator() { return this.getString( PATH_SEPARATOR ); }
    public String getCriteriaInst() { return this.getString( CRITERIA_INST ); }
    public String getCriteriaAbrv() { return this.getString( CRITERIA_ABRV ); }
    public String getProductInst() { return this.getString( PRODUCT_INST ); }
    public String getProductAbrv() { return this.getString( PRODUCT_ABRV ); }
    public String getProductDefaultInst() { return this.getString( PRODUCT_DEF_INST ); }
    public String getProductDefaultAbrv() { return this.getString( PRODUCT_DEF_ABRV ); }
    public String getProductAcademicInst() { return this.getString( PRODUCT_ACADEMIC_INST ); }
    public String getProductAcademicAbrv() { return this.getString( PRODUCT_ACADEMIC_ABRV ); }
    public String getProductBusinessInst() { return this.getString( PRODUCT_BUSINESS_INST ); }
    public String getProductBusinessAbrv() { return this.getString( PRODUCT_BUSINESS_ABRV ); }
    
    // Services
    public String getRightsWebServiceURL() {return this.getString(rightsWebServiceURL);}
    public String getRLOrderServiceURL() { return this.getString(rlOrderServiceURL); }
    public String getRLUserServiceURL() { return this.getString(rlUserServiceURL); }
    public String getTelesalesWebServiceURL() { return this.getString(telesalesWebServiceURL); }
    public String getTelesalesCompositeServiceURL() { return this.getString(telesalesCompositeServiceURL); }
    public String getTelesalesRightsholderServiceURL() { return this.getString(telesalesRightsholderServiceURL); }
    public String getSearchRetrievalWebServiceURL() { return this.getString(searchRetrievalWebServiceURL); }
    public String getAdjustmentServiceURL() { return this.getString(AdjustmentServiceURL); }
    public String getCartServiceURL() { return this.getString(CartServiceURL); }
    public String getOrderServiceURL() { return this.getString(OrderServiceURL); }
    public String getTfWebServiceURL() {return this.getString(tfWebServiceURL);}
    public String getLdapServiceURL() {return this.getString(ldapServiceURL);}
    public String getCentralQueueServiceURL() {return this.getString(centralQueueServiceURL);}
    public String getARTransactionServiceURL() { return this.getString(arTransactionServiceURL); }
    public String getRightsResolverServiceURL() { return this.getString(rightsResolverServiceURL); }
    public String getRightsProcessorServiceURL() { return this.getString(rightsProcessorServiceURL); }
	public String getSvcWorksRemoteSearchEndpointUrl() { return this.getString( worksRemoteSearchServiceURL); }
	public String getExtEmailServiceUrl() { return this.getString(extEmailServiceURL); }
	public String getCyberSourceServiceURL() { return this.getString(cyberSourceServiceURL); }
	public String getAccessControlServiceURL() { return this.getString(accessControlServiceURL); }
	public String getOpenUrlExtensionsServiceURL() { return this.getString(openUrlExtensionsServiceURL); }
    
    //Protocol switching properties
    private static final String PROTOCOL_SWITCHING_ENABLED = "protocol.switching.enabled";
    private static final String PROTOCOL_SWITCHING_HTTP_PORT = "protocol.switching.http.port";
    private static final String PROTOCOL_SWITCHING_HTTPS_PORT = "protocol.switching.https.port";
    private static final String PROTOCOL_SWITCHING_HTTP_URL = "protocol.switching.http.url";
    private static final String PROTOCOL_SWITCHING_HTTPS_URL = "protocol.switching.https.url";
 
    public boolean isProtocolSwitchingEnabled() { return this.getBoolean(PROTOCOL_SWITCHING_ENABLED); }
    public int getProtocolSwitchingHTTPPort() { return this.getInt(PROTOCOL_SWITCHING_HTTP_PORT); }
    public int getProtocolSwitchingHTTPSPort() { return this.getInt(PROTOCOL_SWITCHING_HTTPS_PORT); }
    public String getProtocolSwitchingHTTPURL() { return this.getString(ConfigurationHelper.getExposureBasedKey(PROTOCOL_SWITCHING_HTTP_URL)); }
    public String getProtocolSwitchingHTTPSURL() { return this.getString(ConfigurationHelper.getExposureBasedKey(PROTOCOL_SWITCHING_HTTPS_URL)); }
          
    public Boolean isEmailVerifcationEnabled()
    { 
    	return getBoolean(EMAIL_VERIFICATION_ENABLED); 
    } 
    
    public Boolean isEmailVerifcationIntranetCheckEnabled()
    { 
    	return getBoolean(EMAIL_VERFICATION_INTRANET_CHECK_ENABLED); 
    }
    
    public String getEmailVerifcationTimeout() 
    { 
    	return getString(EMAIL_VERFICATION_TIMEOUT); 
    } 
    
    public String getEmailVerifcationUsername()
    { 
    	return getString(EMAIL_VERFICATION_USERNAME); 
    }
    
    public String getEmailVerifcationPassword() 
    { 
    	return getString(EMAIL_VERFICATION_PASSWORD); 
    }
    
    public String getEmailVerificationServiceEndpoint()
    { 
    	return getString(EMAIL_VERFICATION_SERVICE_URL); 
    }
    
    public String getEmailVerificationServiceOperation() 
    { 
    	return getString(EMAIL_VERFICATION_SERVICE_OPERATION); 
    }
    
    
    //  Online Invoice Payment.
    private static final String PAY_INVOICE_ONLINE_EMAIL_TO_FINANCE = "email.invpmt.to_finance";
    private static final String PAY_INVOICE_ONLINE_EMAIL_SUBJ = "email.invpmt.subj";
    public String getInvoicePaymentEmailToFinance() { return this.getString(PAY_INVOICE_ONLINE_EMAIL_TO_FINANCE); }
    public String getInvoicePaymentEmailSubj() { return this.getString(PAY_INVOICE_ONLINE_EMAIL_SUBJ); }

    public boolean getQuartzSchedulerEnable()
    {
        return this.getBoolean(quartzSchedulerEnable);
    }
    
    public String getQuartzEmailSchedule() 
    {
    	return this.getString(quartzEmailSchedule);
    }
    
    public String getOverdueInvoiceDaysPastDue() 
    {
    	return this.getString(overdueInvoiceDaysPastDue);
    }
    
    public String getFreemarkerTemplateLocation()
    {
    	return this.getString(freemarkerTemplateLocation);
    }
    
    public String getOverDueInvoiceEmailTemplate()
    {
    	return this.getString(overdueInvoiceEmailTemplate);
    }
    public String getTestDefaultEmailRecipient()
    {
    	return this.getString(testDefaultEmailRecipient);
    }
    public String getTestDefaultTotalNumberEmailSent()
    {
    	return this.getString(testDefaultTotalNumberEmailSent);
    }
    public String getOverdueInvoiceEmailSender()
    {
    	return this.getString(overdueInvoiceEmailSender);
    }
    public boolean getAutoDunningTestModeEnable()
    {
    	return this.getBoolean(autoDunningTestModeEnable);
    }
	public String getAutoDunningStatusLogReceiver()
	{
		return this.getString(autoDunningStatusLogReceiver);
	}
	public String getAutoDunningStatusLogLocation() {
		return this.getString(autoDunningStatusLogLocation);
	}
	
	
	// Requested Title report methods.
	
	public String getReqtitleEmailSubject() {
		return this.getString(reqtitleEmailSubject);
	}
	public String getReqtitleEmailFrom() {
		return this.getString(reqtitleEmailFrom);
	}
	public String getReqtitleEmailBody() {
		return this.getString(reqtitleEmailBody);
	}
	public String getReqtitleEmailTo() {
		return this.getString(reqtitleEmailTo);
	}
	public String getReqtitleTmpDir() {
		return this.getString(reqtitleTmpDir);
	}
	public String getReqtitleAttachmentName() {
		return this.getString(reqtitleAttachmentName);
	}
	public String getReqtitleQuartzSchedule() {
		return this.getString(reqtitleQuartzSchedule);
	}
	public boolean getReqtitleJobEnabled() {
		return this.getBoolean(reqtitleJobEnabled);
	}
	
	
	/**
	 * Returns true if the system property simulate.rightslink.down=true
	 * or if setForciblySimulateRLDown has been set to true. The latter
	 * allows for testing of RL Down scenarios that occur during a users
	 * session.
	 * @return
	 */
	public boolean simluateRightslinkDown()
	{
		if (this.getForciblySimulateRLDown()) {
			return true;
		}
		return this.getBoolean( SIMLUATE_RIGHTSLINK_DOWN );
	}
	public String getRightslinkAuthenticationToken()
	{
		return this.getString( RIGHTSLINK_AUTHENTICATION_TOKEN );
	}
	/**
	 * returns true if rightslink link prefetching is enabled,
	 * false otherwise
	 * @return
	 */
	public boolean isRightslinkLinkPrefetchEnabled() {
		return this.getBoolean( RIGHTSLINK_LINKS_PREFETCH );
	}
	/**
	 * Returns the maximum # of runRightsRequest retries to make when
	 * a runRightsRequest returns a hasErrors=true after a new 
	 * registration
	 * @return
	 */
	public int getRightslinkRequestMaxRetries() {
		return this.getInt( RIGHTSLINK_REQUEST_MAX_RETRIES );
	}
	/**
	 * returns the # of millis to sleep between each runRightsRequest retry
	 * attempt.
	 * @return
	 */
	public int getRightslinkRequestRetrySleepMillis() {
		return this.getInt( RIGHTSLINK_REQUEST_RETRY_SLEEP_MILLIS );
	}
	
	/**
	 * Cybersource related constants and variables
	 */
    private static final String SIMULATE_CYBERSOURCE_SITE_DOWN = "simulate.cybersource.site.down";
    private static final String CYBERSOURCE_SITE_TEST_TIMEOUT = "cybersource.site.timeout.millis";
    private static final String CYBERSOURCE_RETRIES_COUNT = "cybersource.retries.count";
    private static final String CYBERSOURCE_RETRIES_DELAY_MILLIS = "cybersource.retries.delay.millis";
    private static final String SIMULATE_CYBERSOURCE_ROLLBACK = "simulate.cybersource.rollback";
    
	public boolean simulateCybersourceSiteDown() { return this.getBoolean( SIMULATE_CYBERSOURCE_SITE_DOWN ); }
	public int getCybersourceSiteTestTimeoutMillis() throws NumberFormatException
	{
		return this.getInt(CYBERSOURCE_SITE_TEST_TIMEOUT);
	}
	public int getCybersourceRetriesCount() throws NumberFormatException
	{
		return this.getInt(CYBERSOURCE_RETRIES_COUNT);
	}
	public int getCybersourceRetriesDelayMillis() throws NumberFormatException
	{
		return this.getInt(CYBERSOURCE_RETRIES_DELAY_MILLIS);
	}
    public boolean simulateCybersourceRollback() 
    { 
        return this.getBoolean( SIMULATE_CYBERSOURCE_ROLLBACK );
    }
	
	/**
	 * Marketing Main Drop down menu
	 */
    public String getMainDropDownMenuURL()
    {
        return this.getString( MAIN_DROPDOWN_MENU_URL );
    }
        
    public String getFooterLinksURL()
    {
    	return this.getString(MAIN_FOOTER_LINKS_URL);
    }
    
    public String[] getManualStopwords()
    {
        //  Comma delimited list, we want to split them out before
        //  we return them.
        
        String initialValues = this.getString(MANUAL_STOPWORDS_LIST);
        return initialValues.split(",");
    }
    
    public int getAutoSuggestItems() throws NumberFormatException
    {
        //  How many we want to return to the UI.
        return this.getInt(AUTOSUGGEST_ITEMS);
    }
    
    public int getAutoSuggestCount() throws NumberFormatException
    {
        //  Represents the number of times a work would have been
        //  successfully searched on.
        return this.getInt(AUTOSUGGEST_COUNT);
    }
    
    public String getAutoSuggestSvcEndpoint()
    {
        return this.getString(AUTOSUGGEST_SVC_ENDPOINT);
    }
    
    public int getSearchInsteadItems() throws NumberFormatException
    {
        //  How many we want to return to the UI.
        return this.getInt(SEARCHINSTEAD_ITEMS);
    }
    
    public int getSearchInsteadCount() throws NumberFormatException
    {
        //  Represents the number of times a work would have been
        //  successfully searched on.
        return this.getInt(SEARCHINSTEAD_COUNT);
    }


	public String getRlinkSpecialOrderIdno() {
		return this.getString( RLINK_SPECIALORDER_IDNO );
	}
	
	/**
	 * Marketing Cempro Main Menu URL
	 */
    public String getCemproMainMenuURL()
    {
        return this.getString( CEMPRO_MAIN_MENU_URL );
    }

	/**
	 * Marketing Cempro Header URL
	 */
    public String getCemproHeaderURL()
    {
        return this.getString( CEMPRO_HEADER_URL );
    }	
    
    /**
	 * Marketing Cempro Right URL
	 */
    public String getCemproRightURL()
    {
        return this.getString( CEMPRO_RIGHT_URL );
    }

    /**
	 * Marketing Cempro Footer URL
	 */
    public String getCemproFooterURL()
    {
        return this.getString( CEMPRO_FOOTER_URL );
    }
}
