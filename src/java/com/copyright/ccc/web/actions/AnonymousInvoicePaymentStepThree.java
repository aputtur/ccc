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

import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.AnonymousUnpaidInvoiceForm;
import com.copyright.ccc.web.util.WebUtils;
import com.copyright.svc.artransaction.api.data.ARTransaction;
import com.copyright.workbench.logging.LoggerHelper;

/**
 * This class takes the selected invoices, totals them, and displays the
 * step three tile, which is the credit card tile.
 */
public class AnonymousInvoicePaymentStepThree extends Action
{
    //  These are our mappings to the various pages in our
    //  unpaid invoice flow.

    private static final String SUCCESS = "success";
    private static final String FAILURE = "failure";
    private static final String INVFORM = "anonymousUnpaidInvoiceForm";
    
    private static final long serialVersionUID = 1L;
    private static final Logger _logger = LoggerHelper.getLogger();

    @Override
    public ActionForward execute( ActionMapping       mapping
                                , ActionForm          form
                                , HttpServletRequest  request
                                , HttpServletResponse response )
    {
        
        AnonymousUnpaidInvoiceForm unpaidInvoiceForm = 
            WebUtils.castForm( AnonymousUnpaidInvoiceForm.class, form );

        if (unpaidInvoiceForm == null) unpaidInvoiceForm = new AnonymousUnpaidInvoiceForm();

        _logger.info("\nAnonymousInvoicePaymentStepThree.execute()\n");

        if (_logger.isDebugEnabled())
        {
            //  Dump our form data.
            
            _logger.info(unpaidInvoiceForm.toString());
        }
        
        List<ARTransaction> invoices = unpaidInvoiceForm.getInvoicesToCredit();

        if (invoices == null || invoices.size() < 1)
        {
            //  If nothing was selected, go back.
            ActionErrors errors = new ActionErrors();
            errors.add(
                ActionMessages.GLOBAL_MESSAGE,
                new ActionMessage( "invoice.error.zeroselected" )
            );
            request.setAttribute(Globals.ERROR_KEY, errors);
            return mapping.findForward( FAILURE );
        }
        unpaidInvoiceForm.clearSecureData();

        //  Load the unpaid invoices for the current active user.
        
        if (_logger.isDebugEnabled()) 
        {
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
        request.getSession().setAttribute( 
            WebConstants.SessionKeys.ANONYMOUS_UNPAID_INVOICE_FORM, unpaidInvoiceForm );

        return mapping.findForward( SUCCESS );
    }
}