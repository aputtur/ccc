package com.copyright.ccc.web.actions.coi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.copyright.ccc.business.data.OrderPurchase;
import com.copyright.ccc.business.services.order.OrderPurchaseServices;
import com.copyright.ccc.business.services.order.OrderPurchasesException;
import com.copyright.ccc.web.WebConstants;

/*
 * This action sets up the request object to hold an OrderPurchase so the 
 * /editOrderCourseDetails.do?operation=defaultOperation struts action can
 * edit the OrderPurchase Course Info.  If there is no available course 
 * information an error message will be returned to the calling JSP
 */
public class EditCourseInfoAction extends Action
{
    public EditCourseInfoAction() { }
    
    @Override
    public ActionForward execute( ActionMapping mapping, ActionForm form, 
        HttpServletRequest request, HttpServletResponse response ) 
    {
        ActionForward forward = mapping.findForward("continue");

        String orderId = request.getParameter("id"); // Order Purchase ID
        ActionMessages errors = new ActionMessages();
        long idLong;
        
        try 
        {
       //    OrderPurchase thePurchase = null;
           idLong = Long.parseLong(orderId);
            
           try 
           {
               OrderPurchase orderPurchase = OrderPurchaseServices.getOrderPurchaseForConfirmationNumber(idLong);
//               List<OrderPurchase> orderPurchases = purchases.getOrderPurchaseList();
 
               boolean found = false;
               if (orderPurchase != null) {
                   request.setAttribute(WebConstants.RequestKeys.ORDER_PURCHASE, orderPurchase);            	   
                   request.setAttribute(WebConstants.RequestKeys.IS_CANCEL_VIA_HISTORY_ENABLED, "true");
                   found = true;
               }
               
/*               // Iterate through order licenses till you get the one with id = idStr
               // TODO: Talk to Keith about a non-iterative way here
               if (orderPurchases != null && orderPurchases.size() > 0) 
               {    
                   Iterator<OrderPurchase> itr = orderPurchases.iterator();
                   boolean loop = itr.hasNext();
                   while (loop) 
                   {
                      thePurchase = itr.next();
                      long id = thePurchase.getPurInst();
                      if (id == idLong) { 
                         loop = false; 
                         found = true;
                         request.setAttribute(WebConstants.ORDER_PURCHASE, thePurchase);
                      }
                      else { loop = itr.hasNext(); }
                   }
               }
*/               
               if (!found) { 
                  forward = mapping.findForward("error"); 
                  ActionMessage error = new ActionMessage("ov.error.edit.course.info", orderId);
                  errors.add("notFound", error);
                  this.saveErrors(request, errors);
               }
           } catch (OrderPurchasesException opex) {
              ActionMessage error = new ActionMessage("ov.error.edit.course.info", orderId);
              errors.add("opex", error);
              this.saveErrors(request, errors);
           }
        } catch (NumberFormatException nfe) {
           forward = mapping.findForward("error");
           ActionMessage error = new ActionMessage("ov.error.edit.course.info", orderId);
           errors.add("nfe", error);
           this.saveErrors(request, errors);
        } catch (NullPointerException npe) {
           forward = mapping.findForward("error");
           ActionMessage error = new ActionMessage("ov.error.edit.course.info", orderId);
           errors.add("npe", error);
           this.saveErrors(request, errors);
        }

        return forward;
    }
}
