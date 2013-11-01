package com.copyright.ccc.web.actions.coi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.ItemConstants;
import com.copyright.ccc.business.services.SystemStatus;
import com.copyright.ccc.business.services.cart.CartServices;
import com.copyright.ccc.business.services.user.User;
import com.copyright.ccc.business.services.user.UserServices;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.actions.BasePaymentAction;
import com.copyright.ccc.web.forms.coi.SelectCartPaymentActionForm;
import com.copyright.ccc.web.util.WebUtils;
import com.copyright.data.account.RegistrationChannel;
import com.copyright.workbench.util.StringUtils2;


/**
 * <code>Action</code> class that sets up for display of the select payment
 * JSP.
 */
public class SelectCartPaymentAction extends BasePaymentAction
{
    private static final String SELECT_PAYMENT = "selectPayment";
    private static final String REVIEW_PAYMENT = "reviewPayment";
    private static final String REVIEW_RLINK_ONLY = "reviewRLinkOnly" ;
    private static final String ON_HOLD = "onHold";
  
	private static final long serialVersionUID = 1L;
	
	static Logger _logger = Logger.getLogger( SelectCartPaymentAction.class );

    public SelectCartPaymentAction()
    {
    }
    
    /**
     * If no operation is specified, go to <code>selectPayment()</code>.
     * 
     */
    @Override
    public ActionForward execute( ActionMapping       mapping
                                , ActionForm          form
                                , HttpServletRequest  request
                                , HttpServletResponse response )
    {
    	SelectCartPaymentActionForm cartPaymentFrm = WebUtils.castForm( SelectCartPaymentActionForm.class, form );
        if (cartPaymentFrm == null) cartPaymentFrm = new SelectCartPaymentActionForm();
        
     if (request.getParameter("operation") == null) {
    	 if(CartAction.checkForCartUpdates()){
     		return mapping.findForward( "reviewCartUpdates" );
     	}
    	 super.execute(mapping, cartPaymentFrm, request, response);
    	    return selectPayment( mapping, cartPaymentFrm, request, response );
        }else {
        	ActionForward actionForward=null;
        	actionForward= super.execute(mapping, cartPaymentFrm, request, response);
        	if(actionForward==null && request.getParameter("operation") != null)
        	{
        		String operation = request.getParameter("operation");
        		if (operation.equalsIgnoreCase("reviewCart")) {
        			return reviewCart(mapping, cartPaymentFrm, request, response);
        		}//else don't know what is this,@TOdo extend CCAction
        	}
        	return mapping.findForward(FAILURE);
        }
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
    	 SelectCartPaymentActionForm cartPaymentFrm = WebUtils.castForm( SelectCartPaymentActionForm.class, form );
         //this.clear(req);
         String enforcePwdChg = req.getParameter(WebConstants.ENFORCE_PWD_CHG);
 		if(!StringUtils2.isNullOrEmpty(enforcePwdChg)){
 			UserContextService.getAppUser().setEnforcePwdChg(Boolean.valueOf(enforcePwdChg));
 			UserServices.updatePasswordEnforcement(UserContextService
 				.getAppUser());
 		}
         //  2010-09-17  MSJ
         //  Before we get too far, make sure the user's account is not
         //  on hold.
         if (UserContextService.isUserAccountOnHold()){
             //  Ruh roh.
             ActionErrors errors = new ActionErrors();
             errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage( "errors.accountOnHold" ));
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
             if (!isAlwaysInvoice){
                 cartPaymentFrm.setAlwaysInvoice(Boolean.FALSE);
                 cartPaymentFrm.setAlwaysInvoiceFlag(Boolean.FALSE);
             }
             else{
                 //  MSJ 2008-12-02
                 //  Added condition to override "always invoice."
                 if (!"CC".equalsIgnoreCase(methodOfPmtRestrictor)) {
                     cartPaymentFrm.setAlwaysInvoice(Boolean.TRUE);
                     cartPaymentFrm.setAlwaysInvoiceFlag(Boolean.TRUE);
                 }
                 else {
                     cartPaymentFrm.setAlwaysInvoice(Boolean.FALSE);
                     cartPaymentFrm.setAlwaysInvoiceFlag(Boolean.FALSE);
                     isAlwaysInvoice = false;
                 }
             }
             cartPaymentFrm.reSet();
             cartPaymentFrm.setCCOptions(  );
             cartPaymentFrm.setCurrencyOptions();
             cartPaymentFrm.setExpMonthOptions(  );
             cartPaymentFrm.setExpYearOptions(  );
             this.getInvoiceData( cartPaymentFrm, req );
             this.configureForm( cartPaymentFrm, req );
             //RP OR TF special orders without priced Reprints
            return redirectToPaymentType(mapping, cartPaymentFrm, req, resp,isAlwaysInvoice);
     }
    
  
     
   
    private void getInvoiceData( SelectCartPaymentActionForm form, 
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
            if (cccUser.getUserChannel() != null){
            	form.setUserChannel(cccUser.getUserChannel());
            }
            else{
            	form.setUserChannel(null);
            }
        	
            if ((cccUser.getUserChannel() != null) && (cccUser.getUserChannel() != ""))
            {
	            if (cccUser.getUserChannel().equalsIgnoreCase(RegistrationChannel.COPYRIGHT_COM_IND.getCode()))
	            {
	            	form.setINDUserInfo(cccUser);
	            }
	            else //ORG or ADD user
	            {
	             form.setOrgUserInfo(cccUser);
	            }
            }
        }
        if(! SystemStatus.isTelesalesUp()){
        	form.clearUserInfo();
		            	
        }
    }

    
    /*
    * Helper method setting the user invoice attributes so the various
    * UI JSP page configurations can be tested.
    * @param form is the SelectCartPaymentActionForm for the paymentMethod.jsp
    * @param req is the HttpServletRequest object
    */

    private void configureForm( SelectCartPaymentActionForm form, 
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
    
    private ActionForward redirectToPaymentType(ActionMapping mapping, SelectCartPaymentActionForm paymentForm, 
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
      	   paymentForm.setPaymentType("invoice");
      	   paymentForm.setCanOnlyBeInvoiced(true);
           return mapping.findForward( REVIEW_PAYMENT );
         }
         
         
         //Add new for Special order improvement project
         //has only TF backed Special Order, then skip payment screen
         if(CartServices.hasOnlySpecialOrders() && !CartServices.containsPricedSpecialOrderItem()){
        	 paymentForm.setPaymentType("invoice");
	          return mapping.findForward( REVIEW_PAYMENT );
         }
         
         
    	//TBD
//    	if(CartServices.getCartTotal().equalsIgnoreCase(ItemConstants.COST_TBD)){
         // zero dollared
         if(CartServices.isCartOnlyRightsLink() && !CartServices.containsNonPricedRightsLnkItem() && CartServices.getCartTotal().equalsIgnoreCase(ItemConstants.COST_ZERO_DOLLAR)){
			 paymentForm.setPaymentType("n/a");
	          return mapping.findForward( REVIEW_RLINK_ONLY );
         } else if(CartServices.hasOnlySpecialOrders() && !CartServices.containsPricedSpecialOrderItem()){ // this says its TBD bcoz cart total always return $0.00 can't compare for TBD
    			// ONLY TBD
    		if(!CartServices.isCartOnlyRightsLink()){//MIXED TBD CART CAN HAVE RLNK SO either  ONLY TF or TF+non Reprints
    			if(!CartServices.hasRightslnkSpecialOrders() ||
    					(CartServices.hasNonReprintRightslnkSpecialOrders() && !CartServices.hasReprintSpecialOrders())){
		  	        	 paymentForm.setPaymentType("invoice");
		  	        	paymentForm.setCanOnlyBeInvoiced(true);
		  	        	return mapping.findForward( SELECT_PAYMENT );	
    			}
    			else{
    				return mapping.findForward( SELECT_PAYMENT );
    			}
    			
    		}else{ // ONLY RIGHTS SPECIAL ORDER
    			if(CartServices.hasNonReprintRightslnkSpecialOrders() && !CartServices.hasReprintSpecialOrders()){ // has only non reprint special order TBD
	  	        	   paymentForm.setPaymentType("invoice");
	  	        	 paymentForm.setCanOnlyBeInvoiced(true);
	  	        	return mapping.findForward( SELECT_PAYMENT );
				}
    			else{ // can't fall here
					return mapping.findForward( SELECT_PAYMENT );
				}
    		}
    	}else if(CartServices.getCartTotal().equalsIgnoreCase(ItemConstants.COST_ZERO_DOLLAR)){ // with other TBD's
 	                paymentForm.setPaymentType("invoice");
 	            	paymentForm.setCanOnlyBeInvoiced(true);
 	            	return mapping.findForward( SELECT_PAYMENT );	
    		}
    	   else{ // priced cart
    			return mapping.findForward( SELECT_PAYMENT );
    	   }
    		

    }

	@Override
	public String getPaymentFormActionPath() {
		return "selectCoiPaymentType.do";

	}
    
}
