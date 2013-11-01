package com.copyright.ccc.web.actions.coi;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.ItemConstants;
import com.copyright.ccc.business.services.SystemStatus;
import com.copyright.ccc.business.services.cart.CartServices;
import com.copyright.ccc.business.services.cart.PurchasablePermission;
import com.copyright.ccc.business.services.user.User;
import com.copyright.ccc.business.services.user.UserServices;
import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.BasePaymentForm;
import com.copyright.ccc.web.forms.coi.ReviewSubmitCartActionForm;
import com.copyright.data.account.RegistrationChannel;
import com.copyright.svc.telesales.api.data.ARAccount;
import com.copyright.svc.telesales.api.data.Location;
import com.copyright.svc.telesales.api.data.Organization;


/**
* <code>Action</code> class that sets up for display of the review cart
* checkout JSP.
*/
public class ReviewSubmitCartAction extends CCAction
{ 
    private static final String REVIEW_CART = "reviewCart";

    public ReviewSubmitCartAction() { }

    /**
     * If no operation is specified, go to <code>reviewCart()</code>.
     */
    public ActionForward defaultOperation( ActionMapping mapping, ActionForm form, 
        HttpServletRequest request, HttpServletResponse response ) 
    {
        return reviewCart( mapping, form, request, response );
    }
    
    /**********************************************************************
     * This method gets called directly (skip payment selection page) when 
     * there are only RightsLink item in the cart and they are all Special 
     * Orders so that the Cart Total is ZERO 
     **********************************************************************/
    
    public ActionForward reviewRLinkOnlyCartInvoice( ActionMapping mapping, ActionForm form, 
        HttpServletRequest request, HttpServletResponse response ) 
    {
        ReviewSubmitCartActionForm cartForm = castForm( ReviewSubmitCartActionForm.class, form );
        cartForm.setPaymentType("n/a");
        this.getInvoiceData( cartForm, request );
        HttpSession session = request.getSession(true);
        session.setAttribute(WebConstants.SessionKeys.FUNDING_CURRENCY_TYPE,"USD");
        session.setAttribute(WebConstants.SessionKeys.FUNDING_CURRENCY_RATE_ID,1);
        session.setAttribute(WebConstants.SessionKeys.EXCHANGE_RATE,"1.0");
        return reviewCart( mapping, form, request, response );
    }
    
    /**********************************************************************
     * This method gets called when the user has chosen the INVOICE as the 
     * method of payment instead of choosing Invoice/Credit-card payment 
     * option everytime before checkout. 
     *  
     **********************************************************************/
    
    public ActionForward reviewCartInvoice( ActionMapping mapping, ActionForm form, 
        HttpServletRequest request, HttpServletResponse response ) 
    {
                
        ReviewSubmitCartActionForm cartForm = castForm( ReviewSubmitCartActionForm.class, form );
        //cartForm.setPaymentMethodRadioButton("invoice");
        
        //Can not assume that Always Invoice is TRUE. We might have arrived at this path because the Choice may be INVOICE this time
        //because Cart has ALL Special Order Items
        if (UserContextService.getAuthenticatedAppUser().isAlwaysInvoice()) {
        	cartForm.setAlwaysInvoice("T");
        	cartForm.setAlwaysInvoiceFlag("T");
        } else {
        	cartForm.setAlwaysInvoice("F");
        	cartForm.setAlwaysInvoiceFlag("F");
        }
        this.getInvoiceData( cartForm, request );
        HttpSession session = request.getSession(true);
        session.setAttribute(WebConstants.SessionKeys.FUNDING_CURRENCY_TYPE,"USD");
        session.setAttribute(WebConstants.SessionKeys.FUNDING_CURRENCY_RATE_ID,1);
        session.setAttribute(WebConstants.SessionKeys.EXCHANGE_RATE,"1.0");
        
        return reviewCart( mapping, form, request, response );
    }
    
    /**
    * Primary action method validates the payment choices prior to displaying
    * the review order JSP.
    * @param mapping is the struts ActionMapping passed in by the struts controller
    * @param form is the struts ActionForm passed in by the struts controller
    * @param req is the HttpServletRequest object
    * @param resp is the HttpServletResponse object
    * @return the ActionForward object for reviewing the order data or the edit
    * billing page.
    */
    public ActionForward reviewCart( ActionMapping mapping, ActionForm form, 
        HttpServletRequest req, HttpServletResponse resp ) 
    {
        ReviewSubmitCartActionForm reviewCartForm = castForm( ReviewSubmitCartActionForm.class, form );
        HttpSession session = req.getSession(true);
    	reviewCartForm.setTeleSalesUp(SystemStatus.isTelesalesUp());
    	String defCurrencyType = "";
         //Set the Always Invoice Flag for the User.
    	    	
    	if (BasePaymentForm.CREDIT_TYPE.equalsIgnoreCase(reviewCartForm.getPaymentType())) {
    		if(req.getParameter("creditCardType")!=null){
        		reviewCartForm.setCreditCardType(req.getParameter("creditCardType"));	
        	}
        	if(req.getParameter("creditCardNumber")!=null){
        		reviewCartForm.setCreditCardNumber(req.getParameter("creditCardNumber"));
        	}
        	if(req.getParameter("creditCardNameOn")!=null){
        		reviewCartForm.setCreditCardNameOn(req.getParameter("creditCardNameOn"));
        	}
        	if(req.getParameter("expirationDate")!= null){
        		reviewCartForm.setExpirationDate(req.getParameter("expirationDate"));
        	}
        	if(req.getParameter("paymentProfileId")!= null){
        		reviewCartForm.setPaymentProfileId(req.getParameter("paymentProfileId"));
        	}
        	if(req.getParameter("cccPaymentProfileId")!= null){
        		reviewCartForm.setCccPaymentProfileId(req.getParameter("cccPaymentProfileId"));
        	}	
    	
	    	if((String)session.getAttribute(WebConstants.SessionKeys.FUNDING_CURRENCY_TYPE)== null ||(String)session.getAttribute(WebConstants.SessionKeys.FUNDING_CURRENCY_TYPE)== "" ){
	    		reviewCartForm.setFundingCurrencyType("USD");
	    		reviewCartForm.setFundingCurrencyRateId(1);
	    		reviewCartForm.setExchangeRate("1.0");
	     		reviewCartForm.setCurrencyRateDate(new Date());
	     		reviewCartForm.setFundingCurrencyAmount(CartServices.getCartChargeTotal().substring(1).trim());
	    	}       
	    	if((String)session.getAttribute(WebConstants.SessionKeys.FUNDING_CURRENCY_TYPE)!= null){
	    		reviewCartForm.setFundingCurrencyType((String)session.getAttribute(WebConstants.SessionKeys.FUNDING_CURRENCY_TYPE));
	    	}
	    	if((String)session.getAttribute(WebConstants.SessionKeys.FUNDING_CURRENCY_AMOUNT)!= null){
	    		reviewCartForm.setFundingCurrencyAmount((String)session.getAttribute(WebConstants.SessionKeys.FUNDING_CURRENCY_AMOUNT));
	    	}
	    	if((Integer)session.getAttribute(WebConstants.SessionKeys.FUNDING_CURRENCY_RATE_ID)!= null){
	    		reviewCartForm.setFundingCurrencyRateId((Integer)session.getAttribute(WebConstants.SessionKeys.FUNDING_CURRENCY_RATE_ID));
	    	}
	    	if((String)session.getAttribute(WebConstants.SessionKeys.EXCHANGE_RATE)!= null){
	    		reviewCartForm.setExchangeRate(((String)session.getAttribute(WebConstants.SessionKeys.EXCHANGE_RATE)));
	    	}
	    	if((Date)session.getAttribute(WebConstants.SessionKeys.EXCHANGE_RATE_DATE)!= null){
	    		reviewCartForm.setCurrencyRateDate((Date)session.getAttribute(WebConstants.SessionKeys.EXCHANGE_RATE_DATE));
	    	}
    	}else {
    		// non required information related to credit card
    		reviewCartForm.setFundingCurrencyType("USD");
    		reviewCartForm.setFundingCurrencyRateId(1);
    		reviewCartForm.setExchangeRate("1.0");
     		reviewCartForm.setCurrencyRateDate(new Date());
     		reviewCartForm.setFundingCurrencyAmount(CartServices.getCartChargeTotal().substring(1).trim());
     		session.removeAttribute(WebConstants.SessionKeys.FUNDING_CURRENCY_TYPE);
     		session.removeAttribute(WebConstants.SessionKeys.FUNDING_CURRENCY_TYPE);
     		session.removeAttribute(WebConstants.SessionKeys.FUNDING_CURRENCY_AMOUNT);
     		session.removeAttribute(WebConstants.SessionKeys.FUNDING_CURRENCY_RATE_ID);
     		session.removeAttribute(WebConstants.SessionKeys.EXCHANGE_RATE);
     		session.removeAttribute(WebConstants.SessionKeys.EXCHANGE_RATE_DATE);
     		
    	}
    	//session.invalidate();
        String alwaysInvoiceFlag = reviewCartForm.getAlwaysInvoiceFlag();
        boolean alwaysInvoice;
        
        if (alwaysInvoiceFlag.equalsIgnoreCase("true") || (alwaysInvoiceFlag.equalsIgnoreCase("T")))
        {
            alwaysInvoice = true;
        }
        else
        {
            alwaysInvoice = false;
        }
        
        //If this is different from the value in User Context, then update
        //with this new value
        
        if (alwaysInvoice != UserContextService.getAuthenticatedAppUser().isAlwaysInvoice())
        {
            UserServices.updateCurrentUserAlwaysInvoice(alwaysInvoice);   
        }
        String paymentType = reviewCartForm.getPaymentType();
        if (BasePaymentForm.CREDIT_TYPE.equalsIgnoreCase(paymentType)) {
           reviewCartForm.setRegularInvoice(false);
            reviewCartForm.setPaymentType("credit-card");
           reviewCartForm.setCreditCardDisplay(reviewCartForm.getCreditCardDisplay());
           defCurrencyType = reviewCartForm.getCurrencyType();
           if(defCurrencyType.equals("USD")){
        	   reviewCartForm.setFundingCurrencyType("USD");
        	   if (reviewCartForm.getCartChargeTotal() != null)
       			{
       				reviewCartForm.setFundingCurrencyAmount(reviewCartForm.getCartChargeTotal().substring(1).trim());
       			}
        	   reviewCartForm.setFundingCurrencyRateId(1);
        	   reviewCartForm.setExchangeRate("1.0");
        	   reviewCartForm.setCurrencyRateDate(new Date());
        	}
           getInvoiceData(reviewCartForm,req);
        } 
        else {
        	reviewCartForm.setRegularInvoice(true); 
            reviewCartForm.setPaymentType("invoice");
            reviewCartForm.setFundingCurrencyType("USD");
            if (reviewCartForm.getCartChargeTotal() != null)
    		{
    			reviewCartForm.setFundingCurrencyAmount(reviewCartForm.getCartChargeTotal().substring(1).trim());
    		}
            if (CartServices.isCartOnlyRightsLink() && !CartServices.containsNonPricedRightsLnkItem() && CartServices.getCartTotal().equalsIgnoreCase(ItemConstants.COST_ZERO_DOLLAR))
    		{
    			reviewCartForm.setPaymentType("n/a");
    		}
            
            getInvoiceData(reviewCartForm,req);
    		reviewCartForm.setFundingCurrencyRateId(1);
    		reviewCartForm.setExchangeRate("1.0");
    		reviewCartForm.setCurrencyRateDate(new Date());
        }
        
        reviewCartForm.setPromoCode("");
        
        // If the cart has special orders check for a complete 
        // invoice billing address
        //ActionErrors errors = cartForm.validate(mapping, req);
        ActionMessages errors = this.getErrors(req);
        if (errors.isEmpty()) 
        {
        	
            List<PurchasablePermission> itemsInCart = CartServices.getPurchasablePermissionsInCart();
            reviewCartForm.setCartItems( itemsInCart );
           
          //Set Non academic items and total in cart
             reviewCartForm.intializeCartFormItems();
      	     reviewCartForm.setHasRePrintsItems(Boolean.FALSE);
      	     reviewCartForm.setHasPreviewPdfURL(Boolean.FALSE);
      	     reviewCartForm.setDisplayEditPaymentLink(getDisplayEditPaymentLink());
      	     
      	   if(CartServices.getNumberOfRegularOrderItemsInCart() > 0){
     	   		reviewCartForm.setHasRegularOrders(true);	
        	}else{
        			reviewCartForm.setHasRegularOrders(false);	
        	}
             List<PurchasablePermission> permRightsLnkItems =CartServices.getRightsLinkItemsInCart();      	     
              if(permRightsLnkItems.size()>0){
            	  if(CartServices.hasRightsLinkSpecialPermissionItemsInCart(permRightsLnkItems)){
            		  reviewCartForm.setHasSpecialPermissions(Boolean.TRUE);  
            	  }
            	  if(CartServices.hasRightsLinkRePrintsItemsInCart(permRightsLnkItems)){
            	  reviewCartForm.setHasRePrintsItems(Boolean.TRUE);
            	  }
            	  if(CartServices.hasRightsLinkRePrintsPreviewPDFItemsInCart(permRightsLnkItems)){
                	  reviewCartForm.setHasPreviewPdfURL(Boolean.TRUE);
                	  }
        	}
              // set  academic special orders 
            // Set the struts submit token in the session to be
            // checked for a double submit in ConfirmCartPurchaseAction
            this.saveToken(req);
        } 
        
        return mapping.findForward(REVIEW_CART);
    }
    
    private boolean getDisplayEditPaymentLink()
    {
    	//String isAlwaysInvoice = UserContextService
    	
    	if(CartServices.hasOnlySpecialOrders() && !CartServices.containsPricedSpecialOrderItem()){
          	return false;

          } else if (!CartServices.containsPricedSpecialOrderItem() && 
          		 CartServices.getTotalChargePrice(CartServices.getCart()).compareTo(BigDecimal.ZERO) == 0)
          {
          	 return false;
          }
               
                                  
          if(CartServices.hasOnlySpecialOrders() && !CartServices.containsPricedSpecialOrderItem())
          {
              return false;
          }
          
          if(CartServices.getCartTotal().equalsIgnoreCase(ItemConstants.COST_ZERO_DOLLAR)){
       		return false;
           }
           
  
    	return true;
    }
    

    private void getInvoiceData( ReviewSubmitCartActionForm form, 
                                 HttpServletRequest req )
    {
        User cccUser = UserContextService.getSharedUser();
        // Check for a null user since the session may have timed out
        if ( cccUser != null )
        {
            //  Pile it on.  We will need the mailing and billing address
            //  data contained in the Person object, from the telesales
            //  service.
            cccUser.loadRegistrationData();
            String channel = cccUser.getUserChannel();
            form.setUserChannel(channel);
            //  Now that we have the data, let us populate our form with it.
            if ((channel != null) && (cccUser.getUserChannel() != ""))
            {
            if (channel.equalsIgnoreCase(RegistrationChannel.COPYRIGHT_COM_IND.getCode()))
            {
                //Individual User Mail To and Bill To are the same
                form.setUserName(cccUser.getDisplayName());
                form.setBillToContactName(cccUser.getDisplayName());
                form.setCompany("");   //No Company name
                Location billToAddress = cccUser.getMailingAddress();
                //  If we got something, extract the address information
                //  from it.
                if (billToAddress != null)
                {
                    form.setAddress1( billToAddress.getAddress1() );
                    form.setAddress2( billToAddress.getAddress2() );
                    form.setCity( billToAddress.getCity() );
                    form.setState( billToAddress.getState() );

                    String zipCode = billToAddress.getPostalCode();
                    if (zipCode != null) form.setZip( zipCode );

                    String country = billToAddress.getCountry();
                    if (country != null) form.setCountry( country );
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
                form.setUserName( cccUser.getDisplayName() );
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
                    /* ************ Modified this SCR# 8750 *************** */
                    form.setBillToContactName(cccUser.getBillingDisplayName());

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
                        form.setAddress1( billToAddress.getAddress1() );
                        form.setAddress2( billToAddress.getAddress2() );
                        form.setCity( billToAddress.getCity() );
                        form.setState( billToAddress.getState() );

                        String zipCode = billToAddress.getPostalCode();
                        if (zipCode != null) form.setZip( zipCode );

                        String country = billToAddress.getCountry();
                        if (country != null) form.setCountry( country );
                    }
                }
            }
        }
        }
    }
}
