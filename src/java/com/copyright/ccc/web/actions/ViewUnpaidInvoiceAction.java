package com.copyright.ccc.web.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
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
import com.copyright.ccc.business.services.SystemStatus;
import com.copyright.ccc.business.services.cart.InvoiceUtils;
import com.copyright.ccc.business.services.user.User;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.UnpaidInvoiceForm;
import com.copyright.ccc.web.util.WebUtils;
import com.copyright.svc.artransaction.api.data.ARTransaction;
import com.copyright.svc.telesales.api.data.Organization;

public class ViewUnpaidInvoiceAction extends Action
{
    //  These are our mappings to the various pages in our
    //  unpaid invoice flow.
    
    private static final String SUCCESS = "success";
    private static final String FAILURE = "failure";
    
    
    private static final Logger _logger = Logger.getLogger(ViewUnpaidInvoiceAction.class);

    @Override
    public ActionForward execute( ActionMapping       mapping
                                , ActionForm          form
                                , HttpServletRequest  request
                                , HttpServletResponse response )
    {
        String nextPage = FAILURE;
        List<ARTransaction> invoices = null;
        
        if(!SystemStatus.isCybersourceSiteUp() || !SystemStatus.isRightslinkUp())
        {
            //  Service is probably down.
            addError(request, "invoice.error.service_down");
            return mapping.findForward(nextPage);
        }

        //  Get the ball rolling, snag our form, assume success
        //  and grab the list of invoices and stuff them in our
        //  form; stuff the form back into the session.
        
        User usr = null;
        
        if (UserContextService.isEmulating()) {
            usr = UserContextService.getActiveSharedUser();
        }
        else {
            usr = UserContextService.getAuthenticatedSharedUser();
        }
        UnpaidInvoiceForm frm = WebUtils.castForm(UnpaidInvoiceForm.class, form);
        
        frm.clearSecureData();
        frm.clearSaveSelectedInvoices();
        frm.setSelectAll(null);

        usr.loadRegistrationData(); //  Refresh the user object.

            if (!isValidAccountNumber(request, usr.getArAccountNumber()))
            {
                //  If we got here we likely have a -pty_inst as our account
                //  number.   This means the person has come, for the first time,
                //  from a rightslink account to pay an invoice online.  A negative
                //  party inst means the account copy is still in progress in the
                //  central queue.  We don't want to continue through the payment
                //  process as it will cause a problem in a few steps.

                return mapping.findForward(nextPage);
            }
            frm.setUserCompany(usr.getOrganization().getOrganizationName());
            
            request.getSession().setAttribute(WebConstants.SessionKeys.UNPAID_INVOICE_FORM, frm);

            //  Service is probably down.
        try {
            invoices = InvoiceUtils.getUnpaidInvoicesForUser();
            nextPage = SUCCESS;
        }
        catch (Exception e) {
            //  Service is probably down.
            _logger.error(ExceptionUtils.getFullStackTrace( e ));
            addError(request, "invoice.error.service_down");
        }
        frm.setMode("0");
        frm.setInvoices(invoices);
        frm.setUserName(usr.getDisplayName());
        frm.setUserPhone(usr.getPhoneNumber());
        frm.setUserEmail(usr.getEmailAddress().getAddress());

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

    private boolean isValidAccountNumber(HttpServletRequest r, String accountNumber)
    {
        if (_logger.isDebugEnabled())
        {
            _logger.info("\nPay Invoices Online:  Account Number is " + accountNumber + ".");
        }
        if (accountNumber == null)
        {
            _logger.warn("\nPay Invoices Online:  Null account number.  " + 
                "Account creation still in progress.  [ViewUnpaidInvoiceAction]"
            );
            addError(r, "invoice.error.nullacct");
            return false;           
        }
        if (accountNumber.startsWith("-"))
        {
            _logger.warn("\nPay Invoices Online:  Negative account number " + accountNumber + 
                ".  Account creation still in progress.  [ViewUnpaidInvoiceAction]"
            );
            addError(r, "invoice.error.negacct");
            return false;
        }
        return true;
    }
}