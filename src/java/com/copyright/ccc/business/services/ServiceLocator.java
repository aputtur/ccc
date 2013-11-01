package com.copyright.ccc.business.services;

import com.copyright.ccc.config.CC2Configuration;
import com.copyright.svc.artransaction.api.ARTransactionService;
import com.copyright.svc.centralQueue.api.CentralQueueService;
import com.copyright.svc.common.consumer.ServiceConsumerUtils;
import com.copyright.svc.extEmail.api.EmailSendService;
import com.copyright.svc.ldapuser.api.LdapUserService;
import com.copyright.svc.order.api.CartService;
import com.copyright.svc.order.api.OrderService;
import com.copyright.svc.rights.api.RightsService;
import com.copyright.svc.rightsResolver.api.RightsProcessorService;
import com.copyright.svc.rightsResolver.api.RightsResolverService;
import com.copyright.svc.rlOrder.api.RLOrderServicesInterface;
import com.copyright.svc.rlUser.api.RLUserServicesInterface;
import com.copyright.svc.searchRetrieval.api.SearchRetrievalService;
import com.copyright.svc.telesales.api.TelesalesCompositeService;
import com.copyright.svc.telesales.api.TelesalesRightsholderService;
import com.copyright.svc.telesales.api.TelesalesService;
import com.copyright.svc.tf.api.TFService;
import com.copyright.svc.userInfo.api.accessControl.AccessControlService;
import com.copyright.svc.userInfo.api.cyberSource.CyberSourceService;
import com.copyright.svc.worksremote.api.search.SearchServices;

/**
 * All service interfaces should be located via this class.  The locator
 * serves multiple purposes:
 * 1) reduces code duplication
 * 2) provides a single place to cache interface proxies as required
 * 3) provides a single place to reset the cache without a restart when 
 *    configuration changes, if CCC moves in that direction
 * 4) will allow us to switch between remote invocation and in-process invocation 
 *    more easily if required
 */
public class ServiceLocator 
{
	//private static  AdjustmentService sAdjustmentService;
	private static  ARTransactionService sARTransactionService;
	private static  CartService sCartService;
	private static  CentralQueueService sCentralQueueService;
	private static  EmailSendService sEmailSendService;
	private static  LdapUserService sLdapUserService;
	private static  OrderService sOrderService;
	private static  RightsResolverService sRightsResolverService;
	private static  RightsProcessorService sRightsProcessorService;
	private static  RightsService sRightsService;
	private static 	SearchRetrievalService sSearchRetrievalService;
	private static  SearchServices sSearchServices;
	private static	TFService sTFService;
	private static  TelesalesService sTelesalesService;
	private static  TelesalesCompositeService sTelesalesCompositeService;
	private static  TelesalesRightsholderService sTelesalesRightsholderService;
	private static  RLOrderServicesInterface sRLOrderService;
	private static  RLUserServicesInterface sRLUserService;
	private static  CyberSourceService  sCyberSourceService; 
	private static AccessControlService sAccessControlService;
	
	
	
	private ServiceLocator() {}
	
	public static synchronized AccessControlService getAccessControlService()
	{
		if ( sAccessControlService == null )
		{
			sAccessControlService = ServiceConsumerUtils.getService( 
					AccessControlService.class, 
					CC2Configuration.getInstance().getAccessControlServiceURL() );
		}
		
		return sAccessControlService;
	}
	public static synchronized ARTransactionService getARTransactionService()
	{
		if ( sARTransactionService == null )
		{
			sARTransactionService = ServiceConsumerUtils.getService( 
					ARTransactionService.class, 
					CC2Configuration.getInstance().getARTransactionServiceURL() );
		}
		
		return sARTransactionService;
	}
	
	public static synchronized CyberSourceService getCyberSourceService()
	{
		if ( sCyberSourceService == null )
		{
			sCyberSourceService = ServiceConsumerUtils.getService( 
					CyberSourceService.class, 
					CC2Configuration.getInstance().getCyberSourceServiceURL() );
		}
		
		return sCyberSourceService;
	}
	
	
	public static synchronized RightsResolverService getRightsResolverService()
	{
		if ( sRightsResolverService == null )
		{
			sRightsResolverService = ServiceConsumerUtils.getService( 
					RightsResolverService.class, 
					CC2Configuration.getInstance().getRightsResolverServiceURL() );
		}
		
		return sRightsResolverService;
	}

	 public  static synchronized RightsProcessorService getRightsProcessorService(){ 
		 if(sRightsProcessorService==null){
			 sRightsProcessorService = ServiceConsumerUtils.getService( 
						RightsProcessorService.class, 
						CC2Configuration.getInstance().getRightsProcessorServiceURL() );	 
		 }
		 return sRightsProcessorService;
	   }

	 public  static synchronized SearchServices  getWorksRemoteSearchService() { 
		 if(sSearchServices==null){
			 sSearchServices=ServiceConsumerUtils.getService( 
					SearchServices.class, 
					CC2Configuration.getInstance().getSvcWorksRemoteSearchEndpointUrl() );
		 }
		 return sSearchServices;
	   }
	 
	 public  static synchronized SearchRetrievalService  getSearchRetrievalService() { 
		 if(sSearchRetrievalService==null){
			 sSearchRetrievalService= ServiceConsumerUtils.getService( 
	    		 SearchRetrievalService.class,
	    		 CC2Configuration.getInstance().getSearchRetrievalWebServiceURL());
		 }
		 return sSearchRetrievalService;
	   }
	 public  static synchronized TFService  getTFService() { 
		 if(sTFService==null){
			 sTFService= ServiceConsumerUtils.getService( 
	    		 TFService.class,
	    		 CC2Configuration.getInstance().getTfWebServiceURL());
		 }
		 return sTFService;
	   }

	 public  static synchronized CentralQueueService  getCentralQueueService() { 
		 if(sCentralQueueService==null){
			 sCentralQueueService= ServiceConsumerUtils.getService( 
	    		 CentralQueueService.class,
	    		 CC2Configuration.getInstance().getCentralQueueServiceURL());
		 }
		 return sCentralQueueService;
	   }
	 
	 public  static synchronized TelesalesService getTelesalesService() { 
		 if(sTelesalesService==null){
			 sTelesalesService= ServiceConsumerUtils.getService( 
	    		 TelesalesService.class,
	    		 CC2Configuration.getInstance().getTelesalesWebServiceURL());
		 }
		 return sTelesalesService;
	   }
	 
	 public  static synchronized LdapUserService  getLdapUserService(){ 
		 if(sLdapUserService==null){
			 sLdapUserService= ServiceConsumerUtils.getService( 
	    		 LdapUserService.class,
	    		 CC2Configuration.getInstance().getLdapServiceURL());
		 }
		 return sLdapUserService;
	   }
	 
	 public  static synchronized RightsService  getRightsService() { 
		 if(sRightsService==null){
			 sRightsService= ServiceConsumerUtils.getService( 
	    		 RightsService.class,
	    		 CC2Configuration.getInstance().getRightsWebServiceURL());
		 }
		 return sRightsService;
	   }
	 public  static synchronized TelesalesCompositeService  getTelesalesCompositeService(){ 
		 if(sTelesalesCompositeService==null){
			 sTelesalesCompositeService= ServiceConsumerUtils.getService( 
	    		 TelesalesCompositeService.class,
	    		 CC2Configuration.getInstance().getTelesalesCompositeServiceURL());
		 }
		 return sTelesalesCompositeService;
	   }
	 
	 public  static synchronized OrderService  getOrderService(){ 
		 if(sOrderService==null){
			 sOrderService= ServiceConsumerUtils.getService( 
					 OrderService.class,
	    		 CC2Configuration.getInstance().getOrderServiceURL());
		 }
		 return sOrderService;
	   }
	 public  static synchronized CartService  getCartService(){ 
		 if(sCartService==null){
			 sCartService= ServiceConsumerUtils.getService( 
					 CartService.class,
	    		 CC2Configuration.getInstance().getCartServiceURL());
		 }
		 return sCartService;
	   }
	 
	 public  static synchronized TelesalesRightsholderService  getTelesalesRightsholderService(){ 
		 if(sTelesalesRightsholderService==null){
			 sTelesalesRightsholderService= ServiceConsumerUtils.getService( 
					 TelesalesRightsholderService.class,
	    		 CC2Configuration.getInstance().getTelesalesRightsholderServiceURL());
		 }
		 return sTelesalesRightsholderService;
	   }
	 
	 
	 public  static synchronized RLOrderServicesInterface  getRLOrderService(){ 
		 if(sRLOrderService==null){
			 sRLOrderService= ServiceConsumerUtils.getService( 
					 RLOrderServicesInterface.class,
	    		 CC2Configuration.getInstance().getRLOrderServiceURL());
		 }
		 return sRLOrderService;
	   }

	 public  static synchronized RLUserServicesInterface  getRLUserService(){ 
		 if(sRLUserService==null){
			 sRLUserService= ServiceConsumerUtils.getService( 
					 RLUserServicesInterface.class,
	    		 CC2Configuration.getInstance().getRLUserServiceURL());
		 }
		 return sRLUserService;
	   }
	 
	 public  static synchronized EmailSendService  getEmailSendService(){ 
		 if(sEmailSendService==null){
			 sEmailSendService= ServiceConsumerUtils.getService( 
					 EmailSendService.class,
					 CC2Configuration.getInstance()
						.getExtEmailServiceUrl());
		 }
		 return sEmailSendService;
	   }
	  
	 /**
	  * REST services do not have a client-side proxy object.  We still centralize
	  * access through the service locator so that we can track all service invocations.
	  */
	 public static String getOpenUrlExtensionsEndpointUrl()
	 {
		 return CC2Configuration.getInstance().getOpenUrlExtensionsServiceURL();
	 }
}
