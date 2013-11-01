package com.copyright.ccc.web.actions.coi;

//import com.copyright.ccc.business.data.OrderLicense;
//import com.copyright.ccc.business.services.order.OrderLicenseServices;
//import com.copyright.ccc.business.services.order.OrderLicensesException;
//import com.copyright.ccc.web.forms.OrderViewActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.business.services.cart.CartServices;
import com.copyright.ccc.web.CCAction;

public class CancelEditAction extends CCAction
{
    private Logger _logger = Logger.getLogger( this.getClass() );
    
    public CancelEditAction() { }
    
    public ActionForward defaultOperation( ActionMapping mapping, ActionForm form, 
        HttpServletRequest request, HttpServletResponse response ) 
    {
      
    	CartServices.refreshCart();
        //return forward;
    	return mapping.findForward("cart");
    }
}
