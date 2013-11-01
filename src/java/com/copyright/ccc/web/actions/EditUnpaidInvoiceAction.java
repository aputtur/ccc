package com.copyright.ccc.web.actions;

import java.util.List;

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
import com.copyright.ccc.business.services.cart.InvoiceUtils;
import com.copyright.ccc.business.services.user.User;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.UnpaidInvoiceForm;
import com.copyright.ccc.web.util.WebUtils;
import com.copyright.svc.artransaction.api.data.ARTransaction;

public class EditUnpaidInvoiceAction extends Action
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
        String nextPage = FAILURE;
        
        //  First, is this an admin user?
        
        if (!UserContextService.isAdminUser()) {
            if (_logger.isDebugEnabled())
            {
                _logger.info("\nThe active user is not the administrator for this account.\n");
            }
            addError(request, "invoice.error.admin");
            return mapping.findForward(FAILURE);
        }
        
        //  Get the ball rolling, snag our form, assume success
        //  and grab the list of invoicesand stuff them in our
        //  form; stuff the form back into the session.
        
        User usr = UserContextService.getAuthenticatedSharedUser();
        UnpaidInvoiceForm frm = WebUtils.castForm( UnpaidInvoiceForm.class, form );
        
        nextPage = SUCCESS;
        
        List<ARTransaction> invoices = InvoiceUtils.getUnpaidInvoicesForUser();
        
        frm.setMode("0");
        frm.setInvoices(invoices);
        frm.clearSecureData();
        frm.rebuildSelectedInvoices();
        frm.setUserName(usr.getDisplayName());
        frm.setUserPhone(usr.getPhoneNumber());
        frm.setUserEmail(usr.getEmailAddress().getAddress());
        
        if (usr.getOrganization() == null) usr.loadRegistrationData();
        
        frm.setUserCompany(usr.getOrganization().getOrganizationName());
        
        request.getSession().setAttribute(WebConstants.SessionKeys.UNPAID_INVOICE_FORM, frm);
        
        if (_logger.isDebugEnabled()) {
            StringBuffer obuf = new StringBuffer();
            obuf.append("\nNumber of UNPAID INVOICES found: ");
            
            if (invoices == null || invoices.size() == 0) {
                obuf.append("0");
            }
            else {
                obuf.append(invoices.size());
            }
            obuf.append("\n");
            _logger.info(obuf.toString());
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