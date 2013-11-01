package com.copyright.ccc.web.actions;

import com.copyright.ccc.business.data.OrderLicense;
import com.copyright.ccc.business.data.OrderSearchResult;
import com.copyright.ccc.business.services.order.OrderLicenseServices;
import com.copyright.ccc.business.services.order.OrderLicensesException;
import com.copyright.ccc.web.forms.OrderViewActionForm;
import com.copyright.ccc.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class LicenseCancelAction extends OrderViewAction
{
    private Logger _logger = Logger.getLogger( this.getClass() );
    
    public LicenseCancelAction() { }
    
    @Override
    public ActionForward execute( ActionMapping mapping, ActionForm form, 
        HttpServletRequest request, HttpServletResponse response ) 
    {
        OrderViewActionForm ovForm = WebUtils.castForm( OrderViewActionForm.class, form );
        String id = ovForm.getLicId();
        
        if (id != null && !"".equals(id)) 
        {
            try {
               long licenseId = Long.parseLong(id);
              // OrderLicenseServices service = new OrderLicenseServices();
               OrderLicense orderLicense;
               for (Object obj : ovForm.getOrderDetails()) {
               	orderLicense = (OrderLicense) obj;
            	if (orderLicense.getID() == licenseId) {
            		OrderSearchResult orderSearchResult = OrderLicenseServices.searchOrderLicensesForDetailNumber(new Long(licenseId));
            		OrderLicense cancelOrderLicense = orderSearchResult.getOrderLicenseByItemId(licenseId);
            		if (cancelOrderLicense != null) {
            			OrderLicenseServices.cancelLicense(cancelOrderLicense);
            		}
                    break;
               	}
               }
            } catch (NumberFormatException nfe) {
               // TODO: Add a message id here
               _logger.error("LicenseCancelAction.execute()", nfe); 
            } catch (OrderLicensesException ope) {
               // TODO: Add a message id here
               _logger.error("LicenseCancelAction.execute()", ope); 
            }              
        }

        ActionForward forward = super.execute(mapping, form, request, response);

        return forward;
    }
}
