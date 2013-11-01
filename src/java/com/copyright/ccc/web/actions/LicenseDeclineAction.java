package com.copyright.ccc.web.actions;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.business.data.OrderLicense;
import com.copyright.ccc.business.data.OrderSearchResult;
import com.copyright.ccc.business.services.order.OrderLicenseServices;
import com.copyright.ccc.business.services.order.OrderLicensesException;
import com.copyright.ccc.util.LogUtil;
import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.forms.LicenseDeclineForm;
import com.copyright.ccc.web.util.WebUtils;
import com.copyright.svc.order.api.data.DeclineReasonEnum;

public class LicenseDeclineAction extends CCAction
{
    private Logger _logger = Logger.getLogger( this.getClass() );
    
    
    
    
    public ActionForward defaultOperation( ActionMapping mapping, ActionForm form, 
        HttpServletRequest request, HttpServletResponse response ) 
    {
        StringBuilder sb = new StringBuilder();
    	response.setContentType("text/html");
 		PrintWriter out;
		try {
			out = response.getWriter();
		} catch (IOException e) {
 				_logger.error(LogUtil.getStack(e));
 				return mapping.findForward("failiure");
		}
    	
        LicenseDeclineForm odDeclineForm = WebUtils.castForm( LicenseDeclineForm.class, form );
        if (odDeclineForm.getLicenseId() != null && !"".equals(odDeclineForm.getLicenseId())) 
        {
            try {
               long licenseId = Long.parseLong(odDeclineForm.getLicenseId());
               	OrderSearchResult orderSearchResult = OrderLicenseServices.searchOrderLicensesForDetailNumber(new Long(licenseId));
            		OrderLicense cancelOrderLicense = orderSearchResult.getOrderLicenseByItemId(licenseId);
            		 if (cancelOrderLicense != null) {
            			if (odDeclineForm.getDeclineCd().equalsIgnoreCase(DeclineReasonEnum.TOO_EXPENSIVE.name())) {
            				cancelOrderLicense.setDeclineReasonCd(DeclineReasonEnum.TOO_EXPENSIVE);
            			} else if (odDeclineForm.getDeclineCd().equalsIgnoreCase(DeclineReasonEnum.TOOK_TOO_LONG.name())) {
            				cancelOrderLicense.setDeclineReasonCd(DeclineReasonEnum.TOOK_TOO_LONG);
            			} else if (odDeclineForm.getDeclineCd().equalsIgnoreCase(DeclineReasonEnum.USED_ALTERNATE.name())) {
            				cancelOrderLicense.setDeclineReasonCd(DeclineReasonEnum.USED_ALTERNATE);
            			} else if (odDeclineForm.getDeclineCd().equalsIgnoreCase(DeclineReasonEnum.NO_LONGER_NEED.name())) {
            				cancelOrderLicense.setDeclineReasonCd(DeclineReasonEnum.NO_LONGER_NEED);
            			} else if (odDeclineForm.getDeclineCd().equalsIgnoreCase(DeclineReasonEnum.RIGHTS_NO_GOOD.name())) {
            				cancelOrderLicense.setDeclineReasonCd(DeclineReasonEnum.RIGHTS_NO_GOOD);
            			} else if (odDeclineForm.getDeclineCd().equalsIgnoreCase(DeclineReasonEnum.GOT_DIRECTLY.name())) {
                			cancelOrderLicense.setDeclineReasonCd(DeclineReasonEnum.GOT_DIRECTLY);
            			} else if (odDeclineForm.getDeclineCd().equalsIgnoreCase(DeclineReasonEnum.ALTER_REQUEST.name())) {
                			cancelOrderLicense.setDeclineReasonCd(DeclineReasonEnum.ALTER_REQUEST);
            			} else if (odDeclineForm.getDeclineCd().equalsIgnoreCase(DeclineReasonEnum.OTHER.name())) {
            				cancelOrderLicense.setDeclineReasonCd(DeclineReasonEnum.OTHER);
            			}
            			OrderLicenseServices.cancelLicense(cancelOrderLicense);
            			sb.append("Your decline  of the license for Order Detail ID: ");
            	 		sb.append(odDeclineForm.getLicenseId());
            	 		sb.append(" in the amount of $");
            	 		sb.append(odDeclineForm.getTotalAmount());
            	 		sb.append(" has been successfully submitted.");
            		 
            	}			
            } catch (NumberFormatException nfe) {
            	sb.append("Error while declining your license for Order Detail ID: ");
         		sb.append(odDeclineForm.getLicenseId());
         		sb.append(" in the amount of $");
         		sb.append(odDeclineForm.getTotalAmount());
               _logger.error("LicenseCancelAction.execute()", nfe); 
            } catch (OrderLicensesException ope) {
            	sb.append("Error while declining your license for Order Detail ID: ");
         		sb.append(odDeclineForm.getLicenseId());
         		sb.append(" in the amount of $");
         		sb.append(odDeclineForm.getTotalAmount());
               _logger.error("LicenseCancelAction.execute()", ope); 
            }              
        }else {
        	sb.append("Error while declining your license.");
     		 _logger.error("License ID is null to Decline"); 
        }
  
 		
	 		out.println(sb);
	 		out.flush();
	
	
	return null;
        
    }
}
