package com.copyright.ccc.web.actions;

import java.util.List;

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

import com.copyright.ccc.web.forms.UnpaidInvoiceForm;
import com.copyright.ccc.web.util.WebUtils;
import com.copyright.svc.artransaction.api.data.ARTransaction;
import com.copyright.workbench.logging.LoggerHelper;

public class ViewPaymentFormAction extends BasePaymentAction
{
    //  These are our mappings to the various pages in our
    //  unpaid invoice flow.
    
	private static final long serialVersionUID = 1L;
	private static final Logger _logger = LoggerHelper.getLogger();

	@Override
    public ActionForward execute( ActionMapping       mapping
                                , ActionForm          form
                                , HttpServletRequest  request
                                , HttpServletResponse response )
    {
        
        UnpaidInvoiceForm unpaidInvoiceForm = WebUtils.castForm( UnpaidInvoiceForm.class, form );
        if (unpaidInvoiceForm == null) unpaidInvoiceForm = new UnpaidInvoiceForm();
        
        if (request.getParameter("operation") == null) {
		        List<ARTransaction> invoices = unpaidInvoiceForm.getInvoicesToCredit();
		        if (invoices == null || invoices.size() < 1)
		        {
		            //  If nothing was selected, go back.
		            ActionErrors errors = new ActionErrors();
		            errors.add(
		                ActionMessages.GLOBAL_MESSAGE,
		                new ActionMessage("invoice.error.zeroselected")
		            );
		            request.setAttribute(Globals.ERROR_KEY, errors);
		            return mapping.findForward("failure");
		        }
		        unpaidInvoiceForm.clearSecureData();
		      //  request.getSession().setAttribute(WebConstants.SessionKeys.UNPAID_INVOICE_FORM, unpaidInvoiceForm);
		        
	        //  Load the unpaid invoices for the current active user.
	        
	        if (_logger.isDebugEnabled()) {
	            StringBuffer obuf = new StringBuffer();
	            obuf.append("\nNumber of Selected Unpaid Invoices: ");
	            
	            if (invoices == null || invoices.size() == 0) {
	                obuf.append("0");
	            }
	            else {
	                obuf.append(invoices.size());
	            }
	            obuf.append("\n");
	            _logger.info(obuf.toString());
	        }
        }
        return super.execute(mapping, unpaidInvoiceForm, request, response);
    }

  	@Override
	public String getPaymentFormActionPath() {

		return "viewPaymentForm.do";
	}
}