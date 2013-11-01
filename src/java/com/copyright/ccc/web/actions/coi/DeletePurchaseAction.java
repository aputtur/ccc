package com.copyright.ccc.web.actions.coi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.copyright.ccc.business.data.DisplaySpec;
import com.copyright.ccc.business.data.OrderLicense;
import com.copyright.ccc.business.data.OrderLicenses;
import com.copyright.ccc.business.data.OrderPurchase;
import com.copyright.ccc.business.data.OrderSearchResult;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.DisplaySpecServices;
import com.copyright.ccc.business.services.RightSourceEnum;
import com.copyright.ccc.business.services.order.OrderLicenseServices;
import com.copyright.ccc.business.services.order.OrderLicensesException;
import com.copyright.ccc.business.services.order.OrderPurchaseServices;
import com.copyright.ccc.business.services.order.OrderPurchasesException;

public class DeletePurchaseAction extends Action 
{
    public DeletePurchaseAction() { }
    
    @Override
    public ActionForward execute( ActionMapping mapping, ActionForm form, 
        HttpServletRequest request, HttpServletResponse response ) 
    {
        ActionForward forward = mapping.findForward("continue");

        ActionMessages errors = new ActionMessages();
        String idStr = request.getParameter("id"); // Confirmation number of Purchase
        OrderLicenses licenses = null;
        OrderLicense aLicense = null;
        String rlItem = "";
        int rlCounter = 0;
        

        if (idStr != null && !"".equals(idStr))
        {
           try {
              long confNum = Long.parseLong(idStr);
              OrderPurchase aPurchase = OrderPurchaseServices.getOrderPurchaseForConfirmationNumber(confNum);
              DisplaySpec licSpec = UserContextService.getLicenseDisplaySpec();
              licSpec.setResultsPerPage(2000); // Limit to 2000 per Keith
              licSpec.setPurchaseId(confNum);
              licSpec.setSearch(idStr);
              licSpec.setSearchBy(DisplaySpecServices.CONFNUMFILTER);
              
              OrderSearchResult orderSearchResult = OrderLicenseServices.searchOrderDetailsForDisplaySpec();
              ArrayList<String> rlItemTitlesThatCannotBeDeleted = new ArrayList<String>();
              licenses = orderSearchResult.getOrderLicensesObject();
              List<OrderLicense> orderLicenses = licenses.getOrderLicenseList();
              Iterator<OrderLicense> itr = orderLicenses.iterator();
              while (itr.hasNext()) 
              {
              aLicense = itr.next();
                 
           if(aLicense != null && aLicense.getRightSourceCd().equals(RightSourceEnum.RL.name())){
               	  
               	  rlItem = rlItem +", "+ aLicense.getPublicationTitle();
               	  rlCounter++;
               	 rlItemTitlesThatCannotBeDeleted.add(aLicense.getPublicationTitle());
                 }
                 
              }
              
              if(rlItemTitlesThatCannotBeDeleted.size() > 0 && orderLicenses.size() == rlItemTitlesThatCannotBeDeleted.size()){
            	  ActionMessage error = new ActionMessage("oh.error.delete.purchase.rlnk", rlItem);
                  errors.add("ope", error); 
                  this.saveErrors(request, errors);
            	  
              }
              

              aPurchase.setShowPurchase(false); // remove from display list
              
              try { 
            	  OrderPurchaseServices.updateOrderPurchase(aPurchase);
              } catch (OrderPurchasesException ope) {
                 // Unable to access Purchase for deletion 
                 ActionMessage error = new ActionMessage("oh.error.delete.purchase");
                 errors.add("ope", error);
                 this.saveErrors(request, errors);
                 // TODO: Log update failure.
              }

              
              // The user can only delete visible OrderPurchases so only retrive display Purchases
//              List<OrderPurchase> purchList = purchases.getDisplayPurchaseList();
//              Iterator<OrderPurchase> itr = purchList.iterator();

//              while (itr.hasNext()) 
//              {
//                 OrderPurchase aPurchase = itr.next(); 
//                 aPurchase.setShowPurchase(false); // remove from display list
//                 try { 
//                    service.updatePurchase(aPurchase);
//                 } catch (OrderPurchasesException ope) {
                    // Unable to access Purchase for deletion 
//                    ActionMessage error = new ActionMessage("oh.error.delete.purchase");
//                    errors.add("ope", error);
//                    this.saveErrors(request, errors);
                    // TODO: Log update failure.
//                 }
//              }
           } catch (NumberFormatException nfe) {
               // Unable to access Purchase for deletion
               ActionMessage error = new ActionMessage("oh.error.delete.purchase");
               errors.add("nfex", error);
               this.saveErrors(request, errors);
           } catch (OrderPurchasesException ope) {
               // Unable to access Purchase for deletion 
               ActionMessage error = new ActionMessage("oh.error.delete.purchase");
               errors.add("ope", error);
               this.saveErrors(request, errors);
           } catch (OrderLicensesException opx) {
               ActionMessage error = new ActionMessage("ov.error.copy.license.general");
               errors.add("general", error);
               this.saveErrors(request, errors);
               forward = mapping.findForward("error"); 
           }
        }

        return forward;
    }
}
