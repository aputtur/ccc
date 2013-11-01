package com.copyright.ccc.web.actions;

import com.copyright.ccc.business.services.order.OrderPurchaseServices;
import com.copyright.ccc.business.services.order.OrderPurchasesException;
import com.copyright.ccc.web.forms.OrderViewActionForm;
import com.copyright.ccc.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class OrderCancelAction extends OrderViewAction
{
    private Logger _logger = Logger.getLogger( this.getClass() );
    
    public OrderCancelAction() { }
    
    @Override
    public ActionForward execute( ActionMapping mapping, ActionForm form, 
        HttpServletRequest request, HttpServletResponse response ) 
    {
        OrderViewActionForm ovForm = WebUtils.castForm( OrderViewActionForm.class, form );
        String ordId = ovForm.getId(); // order id

        if (ordId != null && !"".equals(ordId)) 
        {
            try {
               long orderId = Long.parseLong(ordId);
               OrderPurchaseServices.cancelPurchase(orderId);
            } catch (OrderPurchasesException ole) {
               // TODO: Add a message id here
               _logger.error("OrderCancelAction.execute()", ole); 
            } catch (NumberFormatException nfe) {
               // TODO: Add a message id here
               _logger.error("OrderCancelAction.execute()", nfe); 
            }        
        }

        ActionForward forward = super.execute(mapping, form, request, response);

        return forward;
    }
}
