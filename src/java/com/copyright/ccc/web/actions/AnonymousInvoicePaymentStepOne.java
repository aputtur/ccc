package com.copyright.ccc.web.actions;

//  STEP 1.  Display form to gather customer information.

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

import com.copyright.ccc.business.services.SystemStatus;
import com.copyright.ccc.business.services.cart.InvoiceUtils;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.AnonymousUnpaidInvoiceForm;
import com.copyright.ccc.web.util.WebUtils;
import com.copyright.svc.artransaction.api.data.ARTransaction;

/**
 * This class begins the process for a 3rd party to pay for another
 * user's invoice anonymously.  Basically this action creates the
 * form used throughout the process and sends us to the first page.
 */
public class AnonymousInvoicePaymentStepOne extends Action
{
    private static final String SUCCESS = "success";
    private static final String FAILURE = "failure";
    
    private static final Logger _logger = Logger.getLogger( AnonymousInvoicePaymentStepOne.class );

    @Override
    public ActionForward execute( ActionMapping       mapping
                                , ActionForm          form
                                , HttpServletRequest  request
                                , HttpServletResponse response )
    {
        if (_logger.isDebugEnabled()) {
            _logger.info("\nAnonymous Pay Invoice Step 1: Initialize and show capture form.");
        }
        //  Start fresh each time.

        AnonymousUnpaidInvoiceForm frm = new AnonymousUnpaidInvoiceForm();
        request.getSession().setAttribute( 
            WebConstants.SessionKeys.ANONYMOUS_UNPAID_INVOICE_FORM, frm );
        
        return mapping.findForward( SUCCESS );
    }
}