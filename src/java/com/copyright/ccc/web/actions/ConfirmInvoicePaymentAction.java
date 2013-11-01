package com.copyright.ccc.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.web.forms.UnpaidInvoiceForm;
import com.copyright.ccc.web.util.WebUtils;

public class ConfirmInvoicePaymentAction extends Action
{
    //  These are our mappings to the various pages in our
    //  unpaid invoice flow.
    
    private static final String SUCCESS = "success";
    private static final String FAILURE = "failure";
    private static final String INVFORM = "unpaidInvoiceForm";
    
    private static final Logger _logger = Logger.getLogger(ViewUnpaidInvoiceAction.class);

    @Override
    public ActionForward execute( ActionMapping       mapping
                                , ActionForm          form
                                , HttpServletRequest  request
                                , HttpServletResponse response )
    {
        String nextPage = SUCCESS;
        
        _logger.info("\nConfirmInvoicePaymentAction.execute()\n");
        
            if (UserContextService.isEmulating()) {
                addError(request, "invoice.error.emulating");
                return mapping.findForward(FAILURE);
            }
        
        
        UnpaidInvoiceForm frm = WebUtils.castForm( UnpaidInvoiceForm.class, form );
        
        if (_logger.isDebugEnabled())
        {
            //  Dump our form data.
            
            _logger.info(frm.toString());
        }
        return mapping.findForward(nextPage);
    }
    
    private void addError(HttpServletRequest r, String s)
    {
        ActionErrors errors = new ActionErrors();

        errors.add(
            ActionMessages.GLOBAL_MESSAGE,
            new ActionMessage(s)
        );
        r.setAttribute(Globals.ERROR_KEY, errors);
    }
}