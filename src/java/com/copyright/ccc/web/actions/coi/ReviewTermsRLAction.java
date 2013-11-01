package com.copyright.ccc.web.actions.coi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.business.data.TransactionItem;
import com.copyright.ccc.business.services.cart.CartServices;
import com.copyright.ccc.business.services.cart.PurchasablePermission;
import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.RightsLinkQuickPriceActionForm;
import com.copyright.ccc.web.forms.SearchForm;


public class ReviewTermsRLAction extends CCAction
{
    private final static  String FORWARD = "forward";
    
    public ReviewTermsRLAction() { }
    
    /**
     * If no operation is specified, go to <code>reviewTerms()</code>.
     */
    public ActionForward defaultOperation( ActionMapping mapping, 
                                           ActionForm form, 
                                           HttpServletRequest request, 
                                           HttpServletResponse response )
    {
        return reviewTermsRL( mapping, form, request, response );
    }
    
    /**
    * Struts action that displays a list of order item terms and conditions. 
    * @param mapping is the struts ActionMapping passed in by the struts controller
    * @param form is the struts ActionForm passed in by the struts controller
    * @param request is the HttpServletRequest object
    * @param response is the HttpServletResponse object
    * @return the ActionForward object to the "forward" action mapping.
    */
    public ActionForward reviewTermsRL(ActionMapping mapping, 
                                         ActionForm form, 
                                         HttpServletRequest request, 
                                         HttpServletResponse response) 
    {
        ActionForward forward = mapping.findForward(FORWARD);
        
        RightsLinkQuickPriceActionForm rLinkFormSession = (RightsLinkQuickPriceActionForm) request.getSession().getAttribute( "rlForm" );
        RightsLinkQuickPriceActionForm rLinkForm = castForm( RightsLinkQuickPriceActionForm.class, form );
        rLinkForm.setTerms(rLinkFormSession.getTerms());
        rLinkForm.setSelectedCategory(rLinkFormSession.getSelectedCategory());
        rLinkForm.setSelectedTou(rLinkFormSession.getSelectedTou());
        rLinkForm.getWork().setFullTitle(rLinkFormSession.getWork().getFullTitle());
               
                
        return forward;
    }
     
}
