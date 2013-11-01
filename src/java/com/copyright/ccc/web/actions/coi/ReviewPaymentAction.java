package com.copyright.ccc.web.actions.coi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.coi.SelectCartPaymentActionForm;



/**
 * <code>Action</code> class that sets up for display of the select payment 
 * JSP. 
 */
public class ReviewPaymentAction extends CCAction 
{
    private static final String REVIEW_PAYMENT = "reviewPayment";
    private static final String CREDIT = "credit-card";
    private final static String CONFIRM_CART_SESSION_EXPIRED_MSG_KEY = "revieworder.session.expired.msg";

    public ReviewPaymentAction() { }

    /**
     * If no operation is specified, go to <code>selectPayment()</code>.
     */
    public ActionForward defaultOperation( ActionMapping mapping, ActionForm form, 
        HttpServletRequest request, HttpServletResponse response ) 
    {
        return reviewPayment( mapping, form, request, response );
    }
    
    /*
    * Default action method for use by non-dispatch execution pattern 
    */
    public ActionForward executePerform( ActionMapping mapping, ActionForm form, 
        HttpServletRequest request, HttpServletResponse response ) 
    {
        return this.reviewPayment(mapping, form, request, response);
    }

    /**
    * Primary action method sets up for displaying the select payment JSP.
    * @param mapping is the struts ActionMapping passed in by the struts controller
    * @param form is the struts ActionForm passed in by the struts controller
    * @param req is the HttpServletRequest object
    * @param resp is the HttpServletResponse object
    * @return the ActionForward object forwards to the select payment JSP
    */
    public ActionForward reviewPayment(ActionMapping mapping, ActionForm form, 
                                       HttpServletRequest req, 
                                       HttpServletResponse resp) 
    {                             
    	SelectCartPaymentActionForm cartForm = castForm( SelectCartPaymentActionForm.class, form );
		/*
		 * if the session was newly created, it must have just expired.
		 * Send back to cart page.
		 * the "nested if" handles the case where the user visits the page without a properly
		 * populated session AND the session is known to be new.
		 */
        if (cartForm.getCartItems()==null || cartForm.getCartItems().isEmpty()) {
        	ActionForward af = mapping.findForward("viewCart");			
        	if (UserContextService.isSessionNewlyCreated()) {
        		req.setAttribute(WebConstants.RequestKeys.SESSION_EXPIRED_MSG,
        				getResources(req).getMessage(CONFIRM_CART_SESSION_EXPIRED_MSG_KEY));
        	}
    		return af;
        }  	
		//cartForm.setStatus("reSubmit");

        if (cartForm.getPaymentType().equalsIgnoreCase(ReviewPaymentAction.CREDIT))
        {
            cartForm.setCreditCardType("");
            cartForm.setCreditCardNameOn("");
            cartForm.setCreditCardNumber("");
           cartForm.setExpMonth("");
            cartForm.setExpYear("");           
        }
                                
        return mapping.findForward(REVIEW_PAYMENT);
    }

    
}