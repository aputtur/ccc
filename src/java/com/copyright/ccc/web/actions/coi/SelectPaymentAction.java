package com.copyright.ccc.web.actions.coi;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;
import org.json.JSONException;
import org.json.JSONStringer;

import com.copyright.base.enums.PaymentGateway;
import com.copyright.ccc.CCRuntimeException;
import com.copyright.ccc.business.data.CCUser;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.ItemConstants;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.business.services.SystemStatus;
import com.copyright.ccc.business.services.cart.CartServices;
import com.copyright.ccc.business.services.payment.CyberSourcePaymentVendor;
import com.copyright.ccc.business.services.payment.CyberSourceUtils;
import com.copyright.ccc.business.services.user.User;
import com.copyright.ccc.business.services.user.UserServices;
import com.copyright.ccc.util.LogUtil;
import com.copyright.ccc.util.NumberTools;
import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.BasePaymentForm;
import com.copyright.ccc.web.forms.coi.SelectPaymentActionForm;
import com.copyright.data.account.RegistrationChannel;
import com.copyright.data.payment.CreditCard;
import com.copyright.svc.telesales.api.data.ARAccount;
import com.copyright.svc.telesales.api.data.Location;
import com.copyright.svc.telesales.api.data.Name;
import com.copyright.svc.telesales.api.data.Organization;
import com.copyright.svc.userInfo.api.cyberSource.data.CurrencyRateInfo;
import com.copyright.svc.userInfo.api.cyberSource.data.PaymentProfileInfo;
import com.copyright.svc.userInfo.api.cyberSource.data.SortCriteria;
import com.copyright.svc.userInfo.api.cyberSource.data.CurrencyRateInfo.CurrencyRateInfoKeys;
import com.copyright.svc.userInfo.api.cyberSource.data.PaymentProfileInfo.PaymentProfileInfoKeys;
import com.copyright.svc.userInfo.api.data.UserInfoConsumerContext;
import com.copyright.workbench.util.StringUtils2;


/**
 * ************************************************************
 * DO NOT USE THIS *** USE SelectCartPaymentAction.java ******
 * ************************************************************
 * 
 * <code>Action</code> class that sets up for display of the select payment
 * JSP.
 */
public class SelectPaymentAction extends CCAction
{
    private static final String SELECT_PAYMENT = "selectPayment";
    private static final String REVIEW_PAYMENT = "reviewPayment";
    private static final String REVIEW_RLINK_ONLY = "reviewRLinkOnly" ;
    private static final String ON_HOLD = "onHold";
    
    private static final String ACCEPT = "ACCEPT";
	private static final String IFrameHTMLBegin = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\n<HTML>\n<HEAD><TITLE>Rightslink Transaction Results</TITLE></HEAD>\n";
	private static final String CREDIT_CARD_HTML = IFrameHTMLBegin + "<BODY onload='top.getCreditCards();' ></BODY></HTML>";
	private static final String CREDIT_CARD_FAILURE_HTML = IFrameHTMLBegin + "<BODY onload='top.handleCreditCardTxFailure();'></BODY></HTML>";
	private static final String MODIFY_SUBSCRIPTION = "subscription_modify";
	private static final String NEW_SUBSCRIPTION = "subscription";
	private static final long serialVersionUID = 1L;
	
	static Logger _logger = Logger.getLogger( SelectPaymentAction.class );

    public SelectPaymentAction()
    {
    }
    
    /**
     * If no operation is specified, go to <code>selectPayment()</code>.
     * 
     */
    public ActionForward defaultOperation( ActionMapping mapping, 
                                           ActionForm form, 
                                           HttpServletRequest request, 
                                           HttpServletResponse response )
    {
    	if(CartAction.checkForCartUpdates()){
    		return mapping.findForward( "reviewCartUpdates" );
    	}
    	SelectPaymentActionForm cartForm = castForm( SelectPaymentActionForm.class, form );
    	try{
		 	String url = CyberSourceUtils.getResponseUrlBase(request)+
		 		"/selectCoiPaymentType.do?operation=cyberSourceResponse";
    		String hopURL = CyberSourceUtils.getHopURL();
			String responseEmail = CyberSourceUtils.getResponseEmail();
			String subscriptionSignature = CyberSourceUtils.insertSubscriptionSignature("0","20190731","on-demand","0","false");
			cartForm.setHopURL(hopURL);
			cartForm.setCccURL(url);
			cartForm.setResponseEmail(responseEmail);
			cartForm.setSubscriptionSignature(subscriptionSignature);
			
			CCUser ccUser = UserContextService.getActiveAppUser();
		    User user = UserContextService.getSharedUserForAppUser(ccUser);
		    		    			
		    String firstName = "";
		    String lastName = "";
		    String address1 = "";
		    String address2 = "";
		    String city = "";
		    String state = "";
		    String zip = "";
		    String country = "";
		    String email = "";
		    String userid = "";
		    String phone_number = "";
		    if (user.getBillingAddress() != null) {
		        firstName = user.getFirstName();
		        lastName = user.getLastName();
		        address1 = user.getBillingAddress().getAddress1();
		        address2 = StringUtils.defaultString(user.getBillingAddress().getAddress2());
		        city = user.getBillingAddress().getCity();
		        state = StringUtils.defaultString(user.getBillingAddress().getState());
		        zip = StringUtils.defaultString(user.getBillingAddress().getPostalCode());
		        country = user.getBillingAddress().getCountry();
		        userid = user.getUsername();
				        
		        InternetAddress userEmail = user.getBillingEmailAddress();
	            
                if ( userEmail != null)
                {
                    email =  userEmail.getAddress();
                }
                
                phone_number = user.getBillingPhoneNumber();
		        
		    } 
		    cartForm.setFirstName(firstName);
		    cartForm.setLastName(lastName);
		    cartForm.setAddress1(address1);
		    cartForm.setAddress2(address2);
		    cartForm.setCity(city);
		    cartForm.setState(state);
		    cartForm.setZip(zip);
		    cartForm.setCountry(country);
		    cartForm.setUserName(userid);
		    cartForm.setBillToEmail(email);
		    cartForm.setBillToPhone(phone_number);
		 
		 
	 }catch (CCRuntimeException e){
		 _logger.error(ExceptionUtils.getFullStackTrace(e));
			throw new CCRuntimeException(e);
	}
        return selectPayment( mapping, form, request, response );
    }

    /*
    * Default action method for use by non-dispatch execution pattern
    */

    public ActionForward executePerform( ActionMapping mapping, 
                                         ActionForm form, 
                                         HttpServletRequest request, 
                                         HttpServletResponse response )
    {
        return this.selectPayment( mapping, form, request, response );
    }
    
    /**
     * Primary action method sets up for displaying the select payment JSP.
     * @param mapping is the struts ActionMapping passed in by the struts controller
     * @param form is the struts ActionForm passed in by the struts controller
     * @param req is the HttpServletRequest object
     * @param resp is the HttpServletResponse object
     * @return the ActionForward object forwards to the select payment JSP
     */
     public ActionForward selectPayment( ActionMapping mapping, ActionForm form, 
                                         HttpServletRequest req, 
                                         HttpServletResponse resp )
     {
         SelectPaymentActionForm cartForm = castForm( SelectPaymentActionForm.class, form );
         //this.clear(req);
         
         String enforcePwdChg = req.getParameter(WebConstants.ENFORCE_PWD_CHG);

 		if(!StringUtils2.isNullOrEmpty(enforcePwdChg))
 		{
 			UserContextService.getAppUser().setEnforcePwdChg(Boolean.valueOf(enforcePwdChg));
 			UserServices.updatePasswordEnforcement(UserContextService
 				.getAppUser());
 		}
 		
         
         //  2010-09-17  MSJ
         //  Before we get too far, make sure the user's account is not
         //  on hold.
         
         if (UserContextService.isUserAccountOnHold())
         {
             //  Ruh roh.

             ActionErrors errors = new ActionErrors();

             errors.add(
                 ActionMessages.GLOBAL_MESSAGE,
                 new ActionMessage( "errors.accountOnHold" )
             );
             req.setAttribute( Globals.ERROR_KEY, errors );
             
             return mapping.findForward( ON_HOLD );
         }
         
          /***********************************************
          * Get the Always Invoice value. Depending on the flag, go to
          * Select Payment or Review Order
          ***********************************************/
          
          //boolean isAlwaysInvoice = UserContextService.getAuthenticatedAppUser().isAlwaysInvoice();
           boolean isAlwaysInvoice = UserContextService.getActiveAppUser().isAlwaysInvoice();
           
           //    MSJ 2008-12-02
           //    Added code to avoid invoicing when the MPR is set.
           
           String methodOfPmtRestrictor = UserContextService.getUserContext().getMethodOfPmtRestrictor();
           if (methodOfPmtRestrictor == null) methodOfPmtRestrictor = "";
           
           //boolean isAlwaysInvoice2 = UserContextService.getAppUser().isAlwaysInvoice();
           //boolean isAlwaysInvoice3 = UserContextService.getAuthenticatedAppUser().isAlwaysInvoice();
          
             if (!isAlwaysInvoice)
             {
                 cartForm.setAlwaysInvoice(Boolean.FALSE);
                 cartForm.setAlwaysInvoiceFlag(Boolean.FALSE);
             }
             else
             {
                 //  MSJ 2008-12-02
                 //  Added condition to override "always invoice."
                 
                 if (!"CC".equalsIgnoreCase(methodOfPmtRestrictor)) {
                     cartForm.setAlwaysInvoice(Boolean.TRUE);
                     cartForm.setAlwaysInvoiceFlag(Boolean.TRUE);
                 }
                 else {
                     cartForm.setAlwaysInvoice(Boolean.FALSE);
                     cartForm.setAlwaysInvoiceFlag(Boolean.FALSE);
                     isAlwaysInvoice = false;
                 }
             }
          
                              
             //cartForm.setPurchaseOrderNumber(""); 
             cartForm.reSet();
             this.setCCOptions( cartForm );
             this.setCurrencyOptions(cartForm);
             this.setExpMonthOptions( cartForm );
             this.setExpYearOptions( cartForm );
             this.getInvoiceData( cartForm, req );
             this.configureForm( cartForm, req );
             
             
             //RP OR TF special orders without priced Reprints
            return redirectToPaymentType(mapping, cartForm, req, resp,isAlwaysInvoice);
     }
    
    /**
	 * This method rebuilds the html
	 * 
	 * 
	 * @return
	 */
    private StringBuilder rebuildCreditCardListHTML(String userid, List<PaymentProfileInfo> creditCards) throws CCRuntimeException {

		StringBuilder sb = new StringBuilder();
		sb.append("<div id=\"ccRows\" class=\"ccRows\">");
		sb.append("<table class=\"striped\" width=\"640\" border=\"1\" frame=\"box\" rules=\"none\" cellpadding=\"3\" cellspacing=\"0\" >");
		sb.append("<tr>");
		sb.append("<td height=\"25\" class=\"PaymentHeaderCell\" colspan=\"2\"><p class=\"LayerLink\">&nbsp;</p></td>");
		sb.append("<td height=\"25\" colspan=\"2\" class=\"PaymentHeaderCell\"><p class=\"LayerLink\"><font color=\"white\">Card Type</p></td>");
		sb.append("<td width=\"92\" height=\"25\" align=\"left\" class=\"PaymentHeaderCell\"><p class=\"LayerLink\"><font color=\"white\">Card No.</p></td>");
		sb.append("<td width=\"175\" height=\"25\" align=\"left\" class=\"PaymentHeaderCell\"><p class=\"LayerLink\"><font color=\"white\">Cardholder's Name</p></td>");
		sb.append("<td width=\"88\" height=\"25\" align=\"left\" class=\"PaymentHeaderCell\"><p class=\"LayerLink\"><font color=\"white\">Exp. Date</p></td>");
		sb.append("<td width=\"118\" height=\"25\" class=\"PaymentHeaderCell\"><p class=\"LayerLink\">&nbsp;</p></td>");
		sb.append("</tr>");
		Iterator<PaymentProfileInfo> theCreditCards = creditCards.iterator();
		PaymentProfileInfo creditCard = null;
		int counter = 1;
		while (theCreditCards.hasNext())
		{
			creditCard = theCreditCards.next();
			boolean expired = CyberSourceUtils.isCreditCardExpired(creditCard.getExpirationDate());
			sb.append("<tr>");
			sb.append("<td height=\"30\" colspan=\"2\">&nbsp;</td>");
			if(expired)
			{
				// no radio button for expired card
				sb.append("<td align=\"left\"width=\"25\"><span style=\"display: none;\" id='profileRow"+counter+"_paymentProfileId'>"+creditCard.getPaymentProfileId()+"</span><span style=\"display: none;\" id='profileRow"+counter+"_cccPaymentProfileId'>"+creditCard.getCccProfileId()+"</span></td>");
			}
			else
			{
				sb.append("<td align=\"left\"width=\"25\"><input type=\"radio\" onclick=\"saveProfileValues(this);\" name=\"payByMethod\" class=\"selectable\"  value='" +creditCard.getPaymentProfileId()+ "' id='profileRow" + counter+"'/><span style=\"display: none;\" id='profileRow"+counter+"_paymentProfileId'>"+creditCard.getPaymentProfileId()+"</span><span style=\"display: none;\" id='profileRow"+counter+"_cccPaymentProfileId'>"+creditCard.getCccProfileId()+"</span></td>");
			}
			sb.append("<td height=\"30\"  align=\"left\" class=\"RegularNote\"><span id='profileRow"+counter+"_cardType'>"+creditCard.getCardType()+"</span></td>");
			sb.append("<td height=\"30\"  class=\"RegularNote\" align=\"left\"><span id='profileRow"+counter+"_lastFourDigits'>"+creditCard.getLastFourDigits()+"</span></td>");
			sb.append("<td height=\"30\"  class=\"RegularNote\" align=\"left\"><span id='profileRow"+counter+"_cardholderName'>"+creditCard.getCardholderName()+"</span></td>");
			if(expired)
			{
				// red font for expiry date
				sb.append("<td height=\"30\" style=\"color:red;font-weight:bold;\" class=\"RegularNote\" align=\"left\"class=\"expirable\" id=\"ccExpirationDate" + counter + "\">" + creditCard.getExpirationDate()+ "**<span style=\"display: none;\" id='profileRow"+counter+"_expirationDate'>"+creditCard.getExpirationDate()+"</span></td>");
			}
			else
			{
				sb.append("<td height=\"30\"  class=\"RegularNote\" align=\"left\"class=\"expirable\" id=\"ccExpirationDate" + counter + "\">" + creditCard.getExpirationDate()+ "<span style=\"display: none;\" id='profileRow"+counter+"_expirationDate'>"+creditCard.getExpirationDate()+"</span></td>");
			}
			sb.append("<td height=\"30\"  class=\"RegularNote\" align=\"left\"><a id=\"modifiable\" class=\"modifiable\" href=\"#\" value='" + creditCard.getPaymentProfileId()+ "'>Modify</a>");
			sb.append("&nbsp;&nbsp;&nbsp;<a id=\"deleteable\" class=\"deleteable\" href=\"#\" value='" + creditCard.getPaymentProfileId()+ "'>Delete</a></td>");
			sb.append("</tr>");
			counter++;
			
		}
		sb.append("<tr>");
		sb.append("<td height=\"25\" colspan=\"2\">&nbsp;</td>");
		sb.append("<td height=\"25\" colspan=\"2\">&nbsp;</td>");
		sb.append("<td height=\"25\">&nbsp;</td>");
		sb.append("<td height=\"25\" colspan=\"3\" align=\"right\"><input type=\"button\" style=\"font-size: 10px; font-weight: bold;\" value=\"Add new card\" id=\"addCardButton\" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>");
		sb.append("</tr>");
		sb.append("</table>");
		sb.append("</div>");
		return sb;
	}
    
    /*
     * Default action method for use by cybersource response
     */
    
     public ActionForward cyberSourceResponse(ActionMapping mapping, 
                                          ActionForm form, 
                                          HttpServletRequest request, 
                                          HttpServletResponse response ) throws IOException
     
     {    	 
 		 
 		 response.setContentType("text/html");

 		 PrintWriter out = response.getWriter();
		 		 
 		 String paySubscriptionCreateReply_subscriptionID = request.getParameter("paySubscriptionCreateReply_subscriptionID");
 		 String orderPage_transactionType = request.getParameter("orderPage_transactionType");
 		 String card_accountNumber = request.getParameter("card_accountNumber");
 		 String billTo_lastName = request.getParameter("billTo_lastName");
 		 String billTo_firstName = request.getParameter("billTo_firstName");
 		 String cardHolderName = billTo_firstName + " " + billTo_lastName;
 		
 		 
		String lastFour = "";
		if (card_accountNumber != null)
			lastFour = card_accountNumber.replace("#", "");
		String expirationDate = request.getParameter("card_expirationMonth") + "/" + request.getParameter("card_expirationYear");
		String card_cardType = request.getParameter("card_cardType");
		String cyberSourceProfileCreationDecision = request.getParameter("decision");
//		String ccAuthReply_reasonCode = request.getParameter("ccAuthReply_reasonCode");
//		String key = "CS_HOP_" + ccAuthReply_reasonCode;
		String action =request.getParameter("operation");
		if (action == null){
			action = "";
		}
				
		try{
			User cccUser = UserContextService.getSharedUser();
			String userid = "";
			InternetAddress email = cccUser.getEmailAddress();
		    
		    if ( email != null)
		    {
		        userid = email.getAddress() ;
		    }
		    else
		    {
		        userid = "";
		    }
		    //String serviceProviderId ="cybersource";
			String cardTypeName = CyberSourcePaymentVendor.toCardTypeName(card_cardType);
			if (NEW_SUBSCRIPTION.equalsIgnoreCase(orderPage_transactionType)){
				StringBuffer sb2 = new StringBuffer();
				if (ACCEPT.equalsIgnoreCase(cyberSourceProfileCreationDecision)) {
					UserInfoConsumerContext consumerCtx = new UserInfoConsumerContext();
					try {
						PaymentProfileInfo record = new PaymentProfileInfo();
						record.setAccountNumber(card_accountNumber);
						//record.setServiceProviderId(serviceProviderId);
						record.setServiceProviderId(PaymentGateway.CYBERSOURCE.getCode());
						record.setUserId(userid);
						record.setPaymentProfileId(paySubscriptionCreateReply_subscriptionID);
					    record.setCardType(cardTypeName);
					    record.setCardholderName(cardHolderName);
					    record.setExpirationDate(expirationDate);
					    record.setLastFourDigits(lastFour);
					    record.setStatus("enabled");
					    record.setCreateDate(new Date());
					    record.setUpdateDate(new Date());
					    
					    ServiceLocator.getCyberSourceService().insertPaymentProfile(consumerCtx, record, true);
					    if ( _logger.isDebugEnabled() )
						{
							_logger.debug("Subscription creation successful {subscriptionid=" + paySubscriptionCreateReply_subscriptionID + "}");
						}
					    sb2.append(CREDIT_CARD_HTML);
						
					}catch (CCRuntimeException e){
						_logger.error(ExceptionUtils.getFullStackTrace(e));
						_logger.debug("CyberSource Subscription Created but profile creation failed on copy right side !");
						throw new CCRuntimeException("CyberSource Subscription Created but profile creation failed on copy right side !");
					}
				}else{
					 if ( _logger.isDebugEnabled() )
						{
							_logger.debug("CyberSource Subscription Creation NOT successful!");
						}
					sb2.append(CREDIT_CARD_FAILURE_HTML);
				}
				out.println(sb2);
				out.flush();
			    return null;
							
			}else if (MODIFY_SUBSCRIPTION.equalsIgnoreCase(orderPage_transactionType)) {
				StringBuffer sb2 = new StringBuffer();
				if (ACCEPT.equalsIgnoreCase(cyberSourceProfileCreationDecision)) {
					try {
						UserInfoConsumerContext consumerCtx = new UserInfoConsumerContext();
			            PaymentProfileInfo query = new PaymentProfileInfo();
			            query.setPaymentProfileId(paySubscriptionCreateReply_subscriptionID);
			            List<PaymentProfileInfo> results = ServiceLocator.getCyberSourceService().findPaymentProfiles(consumerCtx,query,0, 1, null);
			            						
			            if(results.size() == 1){
			            	PaymentProfileInfo updatedRecord = new PaymentProfileInfo();
							updatedRecord.setCccProfileId(results.get(0).getCccProfileId());
							updatedRecord.setAccountNumber(card_accountNumber);
							updatedRecord.setCardholderName(cardHolderName);
							updatedRecord.setPaymentProfileId(results.get(0).getPaymentProfileId());
							updatedRecord.setServiceProviderId(results.get(0).getServiceProviderId());
							updatedRecord.setUserId(results.get(0).getUserId());
							updatedRecord.setCardType(cardTypeName);
							updatedRecord.setExpirationDate(expirationDate);
							updatedRecord.setLastFourDigits(lastFour);
							updatedRecord.setStatus("enabled");
							updatedRecord.setCreateDate(results.get(0).getCreateDate());
							updatedRecord.setUpdateDate(new Date());
							
							PaymentProfileInfo criteria = new PaymentProfileInfo();
							criteria.setCccProfileId(results.get(0).getCccProfileId());
							ServiceLocator.getCyberSourceService().updatePaymentProfiles(consumerCtx, criteria, updatedRecord, true);
			            }
						sb2.append(CREDIT_CARD_HTML);
						if ( _logger.isDebugEnabled()){
								_logger.debug("Modify Subscription is successful {subscriptionid=" + paySubscriptionCreateReply_subscriptionID + "}");
						}		
										
					} catch (CCRuntimeException e) {
						sb2.append(CREDIT_CARD_FAILURE_HTML);
						_logger.error(ExceptionUtils.getFullStackTrace(e));
						_logger.debug("CyberSource Subscription modified but profile update failed on copy right side !");
						throw new CCRuntimeException("CyberSource Subscription modified but profile update failed on copy right side !");
					}

				} else {// CyberSource did not accept the subscription modification transaction
					if ( _logger.isDebugEnabled()){
						_logger.debug("CyberSource Subscription Modification NOT successful ! {subscriptionid=" + paySubscriptionCreateReply_subscriptionID + "}");
					}		
					sb2.append(CREDIT_CARD_FAILURE_HTML);
				}
				out.println(sb2);
				out.flush();
				return null;
			}			
		} catch (CCRuntimeException e){
			_logger.error(ExceptionUtils.getFullStackTrace(e));
			throw new CCRuntimeException(e );
		}
        return null;
     }
    
     public ActionForward disableCreditCard(ActionMapping mapping, 
             ActionForm form, 
             HttpServletRequest request, 
             HttpServletResponse response ) throws IOException

             {
    	 		StringBuffer sb2 = new StringBuffer();
    	 		response.setContentType("text/html");
    	 		PrintWriter out = response.getWriter();
    	 		String profileid = request.getParameter("profileid");
    	 		    	 		
    	 		try{
    	 			UserInfoConsumerContext consumerCtx = new UserInfoConsumerContext();
					PaymentProfileInfo query = new PaymentProfileInfo();
					query.setPaymentProfileId(profileid);
					List<PaymentProfileInfo> results = ServiceLocator.getCyberSourceService().findPaymentProfiles(consumerCtx,query,0, 25,null);
					 if(results.size() == 1){
			            	PaymentProfileInfo updatedRecord = new PaymentProfileInfo();
			            	updatedRecord.setCccProfileId(results.get(0).getCccProfileId());
							updatedRecord.setAccountNumber(results.get(0).getAccountNumber());
							updatedRecord.setCardholderName(results.get(0).getCardholderName());
							updatedRecord.setPaymentProfileId(results.get(0).getPaymentProfileId());
							updatedRecord.setServiceProviderId(results.get(0).getServiceProviderId());
							updatedRecord.setUserId(results.get(0).getUserId());
							updatedRecord.setCardType(results.get(0).getCardType());
							updatedRecord.setExpirationDate(results.get(0).getExpirationDate());
							updatedRecord.setLastFourDigits(results.get(0).getLastFourDigits());
							updatedRecord.setCreateDate(results.get(0).getCreateDate());
							updatedRecord.setUpdateDate(results.get(0).getUpdateDate());
							updatedRecord.setStatus(results.get(0).getStatus());
							updatedRecord.setStatus("disabled");
							updatedRecord.setPaymentProfileId(profileid);
							updatedRecord.setUpdateDate(new Date());
							
							PaymentProfileInfo criteria = new PaymentProfileInfo();
							criteria.setCccProfileId(results.get(0).getCccProfileId());
							ServiceLocator.getCyberSourceService().updatePaymentProfiles(consumerCtx, criteria, updatedRecord, true);
			            	
			            }
						sb2.append(CREDIT_CARD_HTML);
					
				}catch (CCRuntimeException e){
					_logger.error(ExceptionUtils.getFullStackTrace(e));
					throw new CCRuntimeException(e );
    	 		}
				out.flush(); 	 		
    	 		return null;
    	 	}
     
     public ActionForward buildSubscriptionSignature(ActionMapping mapping, 
             ActionForm form, 
             HttpServletRequest request, 
             HttpServletResponse response ) throws IOException

             {
    	 		String sbJson = "";
    	 		response.setContentType("text/html");
    	 		PrintWriter out = response.getWriter();
    	 		StringBuilder sb = new StringBuilder();
    	 		String csProfileid = request.getParameter("profileid");
    	 		sb.append(CyberSourceUtils.insertModifySubscriptionIDSignature(csProfileid));
    	 		UserInfoConsumerContext consumerCtx = new UserInfoConsumerContext();
    	 		try{
    	 			List<PaymentProfileInfo> creditCards = null;
        	 		PaymentProfileInfo query = new PaymentProfileInfo();
        	 		query.setPaymentProfileId(csProfileid);
                                        
                    if (csProfileid != null) {
    						creditCards =ServiceLocator.getCyberSourceService().findPaymentProfiles(consumerCtx,query,0,25,null);
    				}
                    if(creditCards==null){
                    	return null;
                    }
                 // now build the hidden fields for card type, expiration, and cardholder name
                    Iterator<PaymentProfileInfo> theCreditCards = creditCards.iterator();
            		PaymentProfileInfo creditCard = null;
            		int counter = 1;
            		while (theCreditCards.hasNext()) {
            			creditCard = theCreditCards.next();
            			String card_cardType = creditCard.getCardType();
            			String expirationDate = creditCard.getExpirationDate();
            			String cardTypeCode = CyberSourcePaymentVendor.toCardTypeCode(card_cardType);
            			sb.append("<input type=\"hidden\" name=\"card_cardType\" value=\"" + cardTypeCode + "\">");
            			String[] expDateParts = {};
            			if (expirationDate != null)
            				expDateParts = expirationDate.split("/");
            			String expMM = "", expYYYY = "";
            			if (expDateParts[0] != null && !"".equalsIgnoreCase(expDateParts[0]))
            					expMM = expDateParts[0];
            			if (expDateParts[1] != null && !"".equalsIgnoreCase(expDateParts[1]))
            					expYYYY = expDateParts[1];
            			sb.append("<input type=\"hidden\" name=\"card_expirationMonth\" value=\"" + expMM + "\">");
            			sb.append("<input type=\"hidden\" name=\"card_expirationYear\" value=\"" + expYYYY + "\">");
            			counter++;
            		}
    				out.println(sb);
        	 		out.flush();
    	 		}catch (CCRuntimeException e){
    	 			_logger.error(ExceptionUtils.getFullStackTrace(e));
    	 			JSONStringer jsonErrors;
					try {
						jsonErrors = new JSONStringer().object();
						jsonErrors.key("error0").value("reload");
						sbJson = jsonErrors.endObject().toString();
					} catch (JSONException e1) {
						_logger.error(ExceptionUtils.getFullStackTrace(e1));
						throw new CCRuntimeException(e1);
					}
					sb.append(sbJson);
    	 		}
    	 		return null;
    	 	}
     
     public ActionForward retrieveCreditCardList(ActionMapping mapping, 
             ActionForm form, 
             HttpServletRequest request, 
             HttpServletResponse response ) throws IOException

             {
    	 		SelectPaymentActionForm cartForm = castForm( SelectPaymentActionForm.class, form );
    	 		String sbJson = "";
    	 		response.setContentType("text/html");
    	 		PrintWriter out = response.getWriter();
    	 		StringBuilder sb = new StringBuilder();
    	 		User cccUser = UserContextService.getSharedUser();
    			String userid = "";
    			    			
    			InternetAddress email = cccUser.getEmailAddress();
    		    
    		    if ( email != null)
    		    {
    		        userid = email.getAddress() ;
    		    }
    		    else
    		    {
    		        userid = "";
    		    }
    	 		
    	 		UserInfoConsumerContext consumerCtx = new UserInfoConsumerContext();
    	 		try{
    	 			List<PaymentProfileInfo> creditCards = null;
        	 		PaymentProfileInfo query = new PaymentProfileInfo();
        	 		query.setUserId(userid);
        	 		query.setStatus("enabled");
        	 		List<SortCriteria> sort = new ArrayList<SortCriteria>();
        	 		SortCriteria sc = new SortCriteria(PaymentProfileInfoKeys.CREATE_DATE);
        	 		sc.setSequence(-1);
                    sort.add(sc);
                    creditCards =ServiceLocator.getCyberSourceService().findPaymentProfiles(consumerCtx,query, 0, 25, sort);
                    cartForm.setCreditCards(creditCards);
                    sb = rebuildCreditCardListHTML(userid, creditCards);
        	 		out.println(sb);
                    out.flush();
    	 		}catch (CCRuntimeException e){
    	 			_logger.error(ExceptionUtils.getFullStackTrace(e));
    	 			JSONStringer jsonErrors;
					try {
						jsonErrors = new JSONStringer().object();
						jsonErrors.key("error0").value("reload");
						sbJson = jsonErrors.endObject().toString();
					} catch (JSONException e1) {
						_logger.error(ExceptionUtils.getFullStackTrace(e1));
						throw new CCRuntimeException(e1);
					}
					sb.append(sbJson);
    	 			
    	 		}
    	 		return null;
    	 	}
     
     public ActionForward populateIframeInfo(ActionMapping mapping, ActionForm form, 
             HttpServletRequest req,HttpServletResponse resp) throws IOException
    {
    	 SelectPaymentActionForm cartForm = castForm( SelectPaymentActionForm.class, form );
    	 resp.setContentType("text/html");
    	 PrintWriter out = resp.getWriter();
    	     	 
    	 try{
    		 	String url = CyberSourceUtils.getResponseUrlBase(req)+
    		 		"/selectCoiPaymentType.do?operation=cyberSourceResponse";
    		 	String hopURL = CyberSourceUtils.getHopURL();
    			String responseEmail = CyberSourceUtils.getResponseEmail();
    			String subscriptionSignature = CyberSourceUtils.insertSubscriptionSignature("0","20190731","on-demand","0","false");
    			cartForm.setHopURL(hopURL);
    			cartForm.setCccURL(url);
    			cartForm.setResponseEmail(responseEmail);
    			cartForm.setSubscriptionSignature(subscriptionSignature);
    			
//    			CCUser ccUser = UserContextService.getActiveAppUser();
//    		    User user = UserContextService.getSharedUserForAppUser(ccUser);
    		    
    		    // We may not need to set these, but it's too late to change.
    		    cartForm.setTeleSalesUp(SystemStatus.isTelesalesUp());
    		    cartForm.setFirstName("");
    		    cartForm.setLastName("");
    		    cartForm.setAddress1("");
    		    cartForm.setAddress2("");
    		    cartForm.setCity("");
    		    cartForm.setState("");
    		    cartForm.setZip("");
    		    cartForm.setCountry("");
    		    cartForm.setUserName("");
    		    cartForm.setBillToEmail("");
    		    cartForm.setBillToPhone("");
    		    out.flush(); 
        	 
    	 }catch (CCRuntimeException e){
    		 _logger.error(ExceptionUtils.getFullStackTrace(e));
				throw new CCRuntimeException(e);
    	}
    	return null;
    	
     }
     
     
     public ActionForward multiCurrencyRates(ActionMapping mapping, ActionForm form, 
             HttpServletRequest req,HttpServletResponse resp) throws IOException
    {
    	 SelectPaymentActionForm cartForm = castForm( SelectPaymentActionForm.class, form );
    	 resp.setContentType("text/html");
    	 PrintWriter out = resp.getWriter();
    	 String currencyType = req.getParameter("currencyType");
    	 HttpSession session = req.getSession(true);
    	 session.setAttribute(WebConstants.SessionKeys.FUNDING_CURRENCY_TYPE,currencyType);
    	 double currencyRate;
    	 double currencyAmountDouble;
    	 double fundingCurrencyAmount;
    	 String fundingCurrencyAmountString = "";
    	 String currencyRateString = "";
    	 int currencyRateId;
    	 Date exchangeRateDate;
    	 String currencyAmount = "";
    	 UserInfoConsumerContext consumerCtx = new UserInfoConsumerContext();
    	 try{
    		 CurrencyRateInfo query = new CurrencyRateInfo();
    		 query.setIsoCode(currencyType);
    		 List<SortCriteria> sort = new ArrayList<SortCriteria>();
    		 SortCriteria sc = new SortCriteria(CurrencyRateInfoKeys.DATE_VALID);
    		 sc.setSequence(-1);
             sort.add(sc);
             List<String> fields = new ArrayList<String>();
             fields.add(CurrencyRateInfoKeys.RATE);
             fields.add(CurrencyRateInfoKeys.CURRENCY_RATE_ID);
             fields.add(CurrencyRateInfoKeys.DATE_VALID);
             List<CurrencyRateInfo> results = ServiceLocator.getCyberSourceService().findCurrencyRates(consumerCtx,query,0,1, sort);
             if(results.size() == 1){
            	 // now build the hidden fields currency type
                 Iterator<CurrencyRateInfo> currencyRates = results.iterator();
                 CurrencyRateInfo rateInfo = null;
                 while (currencyRates.hasNext()){
                	 rateInfo = currencyRates.next();
                	 currencyRate = rateInfo.getRate();
                	 currencyRateId = rateInfo.getCurrencyRateId();
                	 exchangeRateDate = rateInfo.getDateValid();
                	 currencyAmount = cartForm.getCartChargeTotal().substring(1).trim();
                	 if(currencyAmount.contains(",")){
                		 currencyAmount = currencyAmount.replace(",", "");
                	 }
                	 try {
                		 currencyAmountDouble = Double.valueOf(currencyAmount.trim()).doubleValue();
                     } catch (NumberFormatException nfe) {
                    	 throw new CCRuntimeException(nfe);
                     }
                     if(currencyType.equals("JPY")){
                    	 fundingCurrencyAmount = NumberTools.roundMoneyUp(NumberTools.multiplyAccurately(currencyAmountDouble,currencyRate));
                     }else{
                    	 fundingCurrencyAmount = NumberTools.roundMoney(NumberTools.multiplyAccurately(currencyAmountDouble,currencyRate));
                     }
                     DecimalFormat df = new DecimalFormat("#####0.00");
                     cartForm.setCurrencyRateId(currencyRateId);
                     fundingCurrencyAmountString = df.format(fundingCurrencyAmount);
                	 //fundingCurrencyAmountString = df.format(Double.toString(fundingCurrencyAmount));
                	 currencyRateString = Double.toString(currencyRate);
                	 session.setAttribute(WebConstants.SessionKeys.FUNDING_CURRENCY_AMOUNT, fundingCurrencyAmountString);
                	 session.setAttribute(WebConstants.SessionKeys.FUNDING_CURRENCY_RATE_ID, Integer.valueOf(currencyRateId));
                	 session.setAttribute(WebConstants.SessionKeys.EXCHANGE_RATE,currencyRateString);
                	 session.setAttribute(WebConstants.SessionKeys.EXCHANGE_RATE_DATE, exchangeRateDate);
                	 out.println(fundingCurrencyAmountString);
                	 out.flush(); 
                 }
         	}
    	 }catch (CCRuntimeException e){
			_logger.error(LogUtil.getStack(e));
			throw e;
    	}
    	 return null; 
     }
 
    
    private void getInvoiceData( SelectPaymentActionForm form, 
                                 HttpServletRequest req )
    {

        User cccUser = UserContextService.getSharedUser();
        if(cccUser!=null){
        	cccUser.loadRegistrationData();
        }
        	
 	   form.setTeleSalesUp(SystemStatus.isTelesalesUp());
       
 	   // Check for a null user since the session may have timed out
        if ( cccUser != null )
        {
            if (cccUser.getUserChannel() != null)
            {
            	form.setUserChannel(cccUser.getUserChannel());
            }
            else
            {
            	form.setUserChannel(null);
            }
        	
            if ((cccUser.getUserChannel() != null) && (cccUser.getUserChannel() != ""))
            {
            if (cccUser.getUserChannel().equalsIgnoreCase(RegistrationChannel.COPYRIGHT_COM_IND.getCode()))
            {
            
             //Individual User Mail To and Bill To are the same
             String firstName = cccUser.getFirstName();
             String lastName = cccUser.getLastName();
             String custName = firstName + " " + lastName;
             form.setUserName( custName );
             form.setBillToContactName(custName);
             form.setCompany("");   //No Company name
             
             
              Location billToAddress = cccUser.getMailingAddress();
              
              if (billToAddress != null)
              {
            	  String address2 =  billToAddress.getAddress2();
            	  if(address2 == null){
            		  address2 = "";
            	  }
            	  String state =  billToAddress.getState();
            	  if(state == null){
            		  state = "";
            	  }
            	  form.setAddress1( billToAddress.getAddress1() );
                  form.setAddress2(address2 );
                  form.setCity( billToAddress.getCity() );
                  form.setState( state );
                  String zipCode = billToAddress.getPostalCode();
                  if ( zipCode == null ){
                	  zipCode = "";
                  }
                  form.setZip( zipCode );
                  String country = billToAddress.getCountry();
                  if ( country != null )
                  {
                        form.setCountry( country );
                  }
                                     
              }
              
                InternetAddress email = cccUser.getEmailAddress();
                
                if ( email != null)
                {
                    form.setEmail( email.getAddress() );
                }
                else
                {
                    form.setEmail("");
                }
                form.setPhone( cccUser.getPhoneNumber() );
                
                
            }
            else //ORG or ADD user
            {
            
              String firstName = cccUser.getFirstName();
              String lastName = cccUser.getLastName();
              String custName = firstName + " " + lastName;
              
              form.setUserName( custName );
              form.setEmail(cccUser.getUsername());
              form.setPhone(cccUser.getPhoneNumber());
              
              ARAccount account = cccUser.getAccount();
              Organization org = cccUser.getOrganization();
            
            if ( org != null)
            {
                //String comp = org.getName();
                String comp = org.getOrganizationName();
                form.setCompany( comp );  
                
            }
                     
            if (account != null)
            {

                Name billToPerson = account.getName();
            
                if (billToPerson != null)
                {
                    /* ************ Modified this SCR# 8750 *************** */
                    String billFirstName = billToPerson.getFirstName();
                    String billLastName = billToPerson.getLastName();
                    String billCustName = billFirstName + " " + billLastName;
                    form.setBillToContactName(billCustName);

                    InternetAddress email = cccUser.getBillingEmailAddress();
            
                    if ( email != null)
                    {
                        form.setBillToEmail( email.getAddress() );
                    }
                    else
                    {
                        form.setEmail("");
                    }
                    form.setBillToPhone( cccUser.getBillingPhoneNumber() );

                    Location billToAddress = cccUser.getBillingAddress();
                    
                    if (billToAddress != null)
                    {
                    	String address2 =  billToAddress.getAddress2();
                  	  	if(address2 == null){
                  	  		address2 = "";
                  	  	}
                  	  	String state =  billToAddress.getState();
                  	  	if(state == null){
                  	  		state = "";
                  	  	}
                    	form.setAddress1( billToAddress.getAddress1() );
                        form.setAddress2( address2 );
                        form.setCity( billToAddress.getCity() );
                        form.setState(state );
                        String zipCode = billToAddress.getPostalCode();
                        if ( zipCode == null ){
                      	  zipCode = "";
                        }
                        form.setZip( zipCode );
                        String country = billToAddress.getCountry();
                        if ( country != null )
                        {
                            form.setCountry( country );
                        }                         
                     
                    }
            
                }
            }
          }
            }
        }
        if(! SystemStatus.isTelesalesUp()){
        	form.setFirstName("");
		    form.setLastName("");
		    form.setAddress1("");
		    form.setAddress2("");
		    form.setCity("");
		    form.setState("");
		    form.setZip("");
		    form.setCountry("");
		    form.setBillToEmail("");
		    form.setBillToPhone("");
		            	
        }
    }

    /*
    * Set up a Collection for the Credit Card HTML Select tag Options for
    * the credit card type.  The first option is a label with an invalid
    * option value so the user is required to select a credit card type.
    * @param form is the SelectPaymentActionForm holding the credit card options
    * generated by this method.
    */

    private void setCCOptions( SelectPaymentActionForm form )
    {
        ArrayList<LabelValueBean> ccOptions = new ArrayList<LabelValueBean>();
        LabelValueBean bean = new LabelValueBean( "--choose--", "" );
        ccOptions.add( bean );
        bean = 
new LabelValueBean( BasePaymentForm.AMEX_NAME, CreditCard.CC_TYPE_AMEX );
        ccOptions.add( bean );
        bean = 
new LabelValueBean( BasePaymentForm.MC_NAME, CreditCard.CC_TYPE_MASTERCARD );
        ccOptions.add( bean );
        // Visa value is same as Visa name
        bean = 
new LabelValueBean( BasePaymentForm.VISA_NAME, CreditCard.CC_TYPE_VISA );
        ccOptions.add( bean );
        form.setCcOptions( ccOptions );
    }
    
    /*
     * Set up a Collection for the Credit Card HTML Select tag Options for
     * the multi currency type.  The first option is a label with a default USD.     * generated by this method.
     */
    
    private void setCurrencyOptions( SelectPaymentActionForm form )
    {
        ArrayList<String> currencyOptions = new ArrayList<String>();
        //LabelValueBean bean = new LabelValueBean( "USD - $", "" );
        //currencyOptions.add( bean );
        /*
        LabelValueBean bean = new LabelValueBean( ReviewSubmitCartActionForm.USD_NAME,"USD" );
        currencyOptions.add( bean );
        bean = new LabelValueBean( ReviewSubmitCartActionForm.EUR_NAME,"EUR" );
        currencyOptions.add( bean );
        bean = new LabelValueBean( ReviewSubmitCartActionForm.GBP_NAME,"GBP" );
        currencyOptions.add( bean );
        bean = new LabelValueBean( ReviewSubmitCartActionForm.JPY_NAME,"JPY" );
        currencyOptions.add( bean ); */
        currencyOptions.add(BasePaymentForm.USD_NAME);
        form.setCurrencyOptions(currencyOptions);
    }

    /*
    * Set up a Collection for the Credit Card HTML month Select tag Options
    * for the credit card expiration month.  The first option is a label with an
    * invalid option value so the user is required to select a valid month.
    * @param form is the SelectPaymentActionForm holding the credit card expiration
    * month options generated by this method.
    */

    private void setExpMonthOptions( SelectPaymentActionForm form )
    {
        ArrayList<LabelValueBean> monthOptions = new ArrayList<LabelValueBean>();
        LabelValueBean bean = new LabelValueBean( "MM", "" );
        monthOptions.add( bean );
        String zero = "0";
        for ( int i = 1; i < 10; i++ )
        {
            String label = zero + i;
            bean = new LabelValueBean( label, label );
            monthOptions.add( bean );
        }

        for ( int i = 10; i <= 12; i++ )
        {
            String s = String.valueOf( i );
            bean = new LabelValueBean( s, s ); // label and value are the same
            monthOptions.add( bean );
        }

        form.setExpMonthOptions( monthOptions );
    }

    /*
    * Set up a Collection for the Credit Card HTML Select tag Options for
    * the credit card expiration year.  Start with the current year and
    * provide a list of including 18 years in to the future. The first
    * option is a label with an invalid option value so the user is
    * required to select a valid month.
    * @param form is the SelectPaymentActionForm holding the credit card
    * expiration year options generated by this method.
    */

    private void setExpYearOptions( SelectPaymentActionForm form )
    {
        ArrayList<LabelValueBean> yearOptions = new ArrayList<LabelValueBean>();
        LabelValueBean bean = new LabelValueBean( "YYYY", "" );
        yearOptions.add( bean );
        Calendar calendar = new GregorianCalendar();
        int year = calendar.get( Calendar.YEAR );

        for ( int i = year; i < year + 18; i++ )
        {
            String value = String.valueOf( i );
            bean = new LabelValueBean( value, value );
            yearOptions.add( bean );
        }

        form.setExpYearOptions( yearOptions );
    }

    /*
    * Helper method setting the user invoice attributes so the various
    * UI JSP page configurations can be tested.
    * @param form is the SelectPaymentActionForm for the paymentMethod.jsp
    * @param req is the HttpServletRequest object
    */

    private void configureForm( SelectPaymentActionForm form, 
                                HttpServletRequest req )
    {


        //form.setCartItems( CartServices.getItemsInCart() );
    	form.setCartItems( CartServices.getRegularOrderItemsInCart() );
        form.setCartTotal( CartServices.getCartTotal() );
        form.setCartChargeTotal( CartServices.getCartChargeTotal() );
       form.setShowExcludeTBDItemText(CartServices.showExcludeTBDItemText());
       form.setHasOnlyNonPricedOrders(CartServices.hasOnlyNonPricedOrders());
    }

    /*
     * Clear the session based reviewSubmitCartActionForm since we use
     * declaritive validation on this form and the radio button control
     * won't be reset if no selection is made.  This could lead to incorrect
     * validation messages to the user so the form must be reset in case
     * it was already put in the session.
     * @input req is the HttpServletRequest object

    private void clear( HttpServletRequest req )
    {
        HttpSession session = req.getSession();
        session.setAttribute( "reviewSubmitCartActionForm", null );
    }
*/

    public ActionForward reviewCart( ActionMapping mapping, ActionForm form, 
                                     HttpServletRequest req, 
                                     HttpServletResponse resp )
    {
        return mapping.findForward( "reviewCart" );

    }
    
    private ActionForward redirectToPaymentType(ActionMapping mapping, SelectPaymentActionForm paymentForm, 
            HttpServletRequest req, 
            HttpServletResponse resp,boolean invOnly ){
    	
/*    
 * (1)All TBD orders except Reprints will result in an invoice only payment page.
 *	For reprint orders that have a price, the customer will be presented with the credit card option and the invoice option.
 * We will have to split the charge b/w charge now items and reprint items that are priced but the card is not hit until the item is shipped.
*
* (2)When all items in the cart are RL-backed and they total $0.00, 
*  the enter Payment Information page should skip and go right to the Review Order page.
*  On the Review Order page the value in the "payment method" field will be N/A



 * 	Special order
    	--------------------------------------------------------------------
    	 CART TOTAL -$TBD  (if Invoice only set in Profile then skip payment screen)
        ----------------------------------------------------------------
        (ONLY) 
    	 TF 					       -  	Show payment selection screen with INV only option
    	(RL:NRP)RL Non Reprint  	- 	Show payment selection screen with INV only option 
    	(RL:RP) RL Reprint   		 -   Show payment With CC and INVOICE OPTION
    	--------------------------------------------------------------------
    	(MIXED)
    	TF + RL:RP 	     -	 Show payment With CC and INVOICE OPTION
    	TF + RL:NRP      -	 Show payment selection screen with INV only option
    	RL:RP + RL:NRP   -	 Show payment With CC and INVOICE OPTION 
    	--------------------------------------------------------------------
    	CART TOTAL- $$$.$$
    	--------------------------------------------------------------------
    	     ---- Show payment With CC and INVOICE OPTION
		--------------------------------------------------------------------
    	CART TOTAL- $0.00
    	--------------------------------------------------------------------
		|	RL ONLY  --  SKIP PAYMENT SCREEN								|
		|	WITH other TBD- Show payment with invoice				
			
    	
  */  	
    	
    	 paymentForm.setHasSpecialOrders(CartServices.containsSpecialOrders());
    	 paymentForm.setHasOnlySpecialOrders(Boolean.FALSE);
    	 if(CartServices.hasOnlySpecialOrders() && !CartServices.containsPricedSpecialOrderItem()){
    	 paymentForm.setHasOnlySpecialOrders(Boolean.TRUE);
    	 }
    	 paymentForm.setCanOnlyBeInvoiced(false);
         if (invOnly)
         {    
      	   paymentForm.setPaymentMethodRadioButton("invoice");
      	   paymentForm.setPaymentType("invoice");
      	   paymentForm.setCanOnlyBeInvoiced(true);
           return mapping.findForward( REVIEW_PAYMENT );
         }
         
         
    	//TBD
//    	if(CartServices.getCartTotal().equalsIgnoreCase(ItemConstants.COST_TBD)){
         // zero dollared
         if(CartServices.isCartOnlyRightsLink() && !CartServices.containsNonPricedRightsLnkItem() && CartServices.getCartTotal().equalsIgnoreCase(ItemConstants.COST_ZERO_DOLLAR)){
	   		 paymentForm.setPaymentMethodRadioButton("n/a");
			 paymentForm.setPaymentType("n/a");
	          return mapping.findForward( REVIEW_RLINK_ONLY );
         } else if(CartServices.hasOnlySpecialOrders() && !CartServices.containsPricedSpecialOrderItem()){ // this says its TBD bcoz cart total always return $0.00 can't compare for TBD
    			// ONLY TBD
    		if(!CartServices.isCartOnlyRightsLink()){//MIXED TBD CART CAN HAVE RLNK SO either  ONLY TF or TF+non Reprints
    			if(!CartServices.hasRightslnkSpecialOrders() ||
    					(CartServices.hasNonReprintRightslnkSpecialOrders() && !CartServices.hasReprintSpecialOrders())){
		  	        	paymentForm.setPaymentMethodRadioButton("invoice");
		  	        	 paymentForm.setPaymentType("invoice");
		  	        	paymentForm.setCanOnlyBeInvoiced(true);
		  	        	return mapping.findForward( SELECT_PAYMENT );	
    			}
    			else{
    				return mapping.findForward( SELECT_PAYMENT );
    			}
    			
    		}else{ // ONLY RIGHTS SPECIAL ORDER
    			if(CartServices.hasNonReprintRightslnkSpecialOrders() && !CartServices.hasReprintSpecialOrders()){ // has only non reprint special order TBD
	  	        	   paymentForm.setPaymentMethodRadioButton("invoice");
	  	        	   paymentForm.setPaymentType("invoice");
	  	        	 paymentForm.setCanOnlyBeInvoiced(true);
	  	        	return mapping.findForward( SELECT_PAYMENT );
				}
    			else{ // can't fall here
					return mapping.findForward( SELECT_PAYMENT );
				}
    		}
    	}else if(CartServices.getCartTotal().equalsIgnoreCase(ItemConstants.COST_ZERO_DOLLAR)){ // with other TBD's
        			paymentForm.setPaymentMethodRadioButton("invoice");
 	                paymentForm.setPaymentType("invoice");
 	            	paymentForm.setCanOnlyBeInvoiced(true);
 	            	return mapping.findForward( SELECT_PAYMENT );	
    		}
    	   else{ // priced cart
    			return mapping.findForward( SELECT_PAYMENT );
    	   }
    		

    }
    
}
