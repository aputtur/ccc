package com.copyright.ccc.web.actions.coi;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.copyright.ccc.business.data.DisplaySpec;
import com.copyright.ccc.business.data.OrderLicense;
import com.copyright.ccc.business.data.OrderSearchResult;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.DisplaySpecServices;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.business.services.order.OrderLicenseServices;
import com.copyright.ccc.business.services.order.OrderLicensesException;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.OrderHistoryActionForm;
import com.copyright.svc.tf.api.TFService;
import com.copyright.svc.tf.api.data.TFConsumerContext;

public class EditLicenseAction extends Action 
{
    private final static String ON_HOLD = "onHold";
    
    public EditLicenseAction() { }
    
    @Override
    public ActionForward execute( ActionMapping mapping, ActionForm form, 
        HttpServletRequest request, HttpServletResponse response ) 
    {
        ActionForward forward = mapping.findForward("continue");
        ActionMessages errors = new ActionMessages();
       
        //  2010-09-21  MSJ
        //  Do not allow license editing if the user's account
        //  is on hold.
        
        if (UserContextService.isUserAccountOnHold())
        {
            ActionErrors actionErrors = new ActionErrors();

            actionErrors.add(
                ActionMessages.GLOBAL_MESSAGE,
                new ActionMessage( "errors.accountOnHold" )
            );
            request.setAttribute( Globals.ERROR_KEY, actionErrors );
            
            return mapping.findForward( ON_HOLD );
        }

        String detailId = request.getParameter("id"); // Order Detail id of License
        String purId = request.getParameter("purid");
        List<String> errorsList = new ArrayList<String>();
        String message = "";
        Long detailIdLong;
        int i = 1;
        TFService tfService = ServiceLocator.getTFService();
        
        String Status = tfService.getOderDetailStatusByOdtInst(new TFConsumerContext(), new Long(detailId)).toString();
        
        if (Status.equalsIgnoreCase("2000"))
        {
        	message = i + ". Item# " + detailId + " is invoiced.";
        	errorsList.add(message);
        	UserContextService.getHttpSession().setAttribute(WebConstants.RequestKeys.ORDER_CASCADE_EDIT_INVOICED_STRINGS, errorsList);
		
        	String path = "orderView.do?id=" + purId + "&rp=main&sf=false&cp=1&viewOrder=1";
        
        	return new ActionForward(path,true);
        }
        //return forward;
		
        try {detailIdLong = new Long(detailId); } 
        catch (Exception e) 
        { 
    	    detailIdLong = 0L; 
            forward = mapping.findForward("error"); // couldn't find the license by idStr 
            ActionMessage error = new ActionMessage("ov.error.edit.license.missing", detailId);
            errors.add("Invalid Detail Id", error);
            forward = mapping.findForward("error"); // invalid non-numeric license id 
            return forward;
        }
    
       
       try 
       {
           OrderLicense aLicense = null;

           try 
           {
               this.initDisplaySpec(detailId);

               OrderSearchResult orderSearchResult = OrderLicenseServices.searchOrderLicensesForDetailNumber(detailIdLong);
               List<OrderLicense>orderLicenses = orderSearchResult.getOrderLicenses();
//               List orderLicenses = licenses.getOrderLicenseList();

               // Iterate through order licenses till you get the one with id = idStr
               if (orderLicenses != null && orderLicenses.size() > 0) 
               {    
                   aLicense = orderLicenses.get(0);

                   if (aLicense != null) 
                   {
                      // Put the license in the request
                      request.setAttribute(WebConstants.RequestKeys.TRANSACTION_ITEM, aLicense);
                   } else {
                       forward = mapping.findForward("error"); // couldn't find the license by idStr 
                       ActionMessage error = new ActionMessage("ov.error.edit.license.missing", detailId);
                       errors.add("general", error);  
                   }
               } else {
                   forward = mapping.findForward("error"); // couldn't find the license by idStr 
                   ActionMessage error = new ActionMessage("ov.error.edit.license.missing", detailId);
                   errors.add("general", error);
               }
           } catch (OrderLicensesException olex) {
             // TODO: Log the exception
              forward = mapping.findForward("error"); // couldn't find the license by idStr 
              ActionMessage error = new ActionMessage("ov.error.edit.license.missing", detailId);
              errors.add("general", error);  
           }
       } catch (NumberFormatException nfe) {
           forward = mapping.findForward("error"); // invalid non-numeric license id 
       }

       return forward;
    }
    
    /**
    * Find the Order Detail given by a detail id
    * 
    * @param id of the Order Detail to retrieve
    */
    private void initDisplaySpec(String id) 
    {
        DisplaySpec spec = UserContextService.getLicenseDisplaySpec();
        spec.setResultsPerPage(2000); // Limit to 2000 per Keith
        spec.setPurchaseId(0L);

        spec.setSearch(id);

        int sortDir = DisplaySpec.DESCENDING; // default sort option
        spec.setSortOrder(sortDir);
        spec.setOrderStateFilter(DisplaySpecServices.ALLSTATES);  

        int sortBy = DisplaySpecServices.ORDERDETAILIDSORT;
        spec.setSortBy(sortBy);

        int filterBy = DisplaySpecServices.ORDERDETAILIDFILTER;
        spec.setSearchBy(filterBy);

        spec.setResultsPerPage(OrderHistoryActionForm.RESULTS_PER_PAGE);
        DisplaySpecServices.setDisplayPage(spec, 1);
    }
}
