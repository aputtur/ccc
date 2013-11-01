package com.copyright.ccc.web.actions;

//  STEP 1.  Check the user's information, then fetch our
//           invoices and display them.

import java.util.List;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.AddressException;
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

import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.business.services.SystemStatus;
import com.copyright.ccc.business.services.cart.InvoiceUtils;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.AnonymousUnpaidInvoiceForm;
import com.copyright.ccc.web.util.WebUtils;
import com.copyright.svc.artransaction.api.data.ARTransaction;
import com.copyright.svc.telesales.api.TelesalesService;
import com.copyright.svc.telesales.api.data.TelesalesServiceConsumerContext;
import com.copyright.svc.telesales.api.data.ARAccount;
import com.copyright.svc.telesales.api.data.ContactPoint;
import com.copyright.svc.telesales.api.data.ContactPointTypeEnum;
import com.copyright.svc.telesales.api.data.Location;
import com.copyright.svc.telesales.api.data.Name;
import com.copyright.svc.telesales.api.data.PhoneLineTypeEnum;
import com.copyright.svc.telesales.api.data.UserTypeEnum;

/**
 * This class checks the user provided information submitted by the
 * step 1 tile to make sure it is valid, then retrieves the invoices.
 * The step 2 tile displays the invoices for selection.
 */
public class AnonymousInvoicePaymentStepTwo extends Action
{
    private static final String SUCCESS = "success";
    private static final String FAILURE = "failure";
    
    private static final Logger _logger = Logger.getLogger( AnonymousInvoicePaymentStepTwo.class );

    @Override
    public ActionForward execute( ActionMapping       mapping
                                , ActionForm          form
                                , HttpServletRequest  request
                                , HttpServletResponse response )
    {
        String nextStep = SUCCESS;
        boolean isValidEmailAddress;
        boolean isValidEmailAddressConfirm;

        if (_logger.isDebugEnabled()) {
            _logger.info("\nAnonymous Pay Invoice Step 2: Check data, load invoices, display invoice selection form.");
        }

        AnonymousUnpaidInvoiceForm frm = (AnonymousUnpaidInvoiceForm) form;

        String emailAddress = frm.getEmailAddress();
        String emailAddressConfirm = frm.getEmailAddressConfirm();

        isValidEmailAddress = checkEmailAddress( emailAddress );
        isValidEmailAddressConfirm = checkEmailAddress( emailAddressConfirm );

        if (!isValidEmailAddress || !isValidEmailAddressConfirm)
        {
            if (_logger.isDebugEnabled()) {
                _logger.info( "\nAnonymous Pay Invoice Step 2: Invalid Email Address was specified." );
            }
            addError( request, "invoice.error.invalidemailaddress" );
            nextStep = FAILURE;
        }

        if (!emailAddress.equals( emailAddressConfirm ))
        {
            if (_logger.isDebugEnabled()) {
                _logger.info( "\nAnonymous Pay Invoice Step 2: Invalid Email Address was specified." );
            }
            addError( request, "invoice.error.match" );
            nextStep = FAILURE;
        }

        //  Grab our invoices and some other data and set them 
        //  into our form, if we did not already have a failure.

        if (!SUCCESS.equals(nextStep) || !populateForm( request, frm )) nextStep = FAILURE;

        return mapping.findForward( nextStep );
    }

    public static boolean checkEmailAddress(String email) 
    {
        boolean result = true;

        try {
            InternetAddress emailAddr = new InternetAddress( email );
            emailAddr.validate();
        } 
        catch (AddressException ex) {
            result = false;
        }
        return result;
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

    private boolean populateForm( HttpServletRequest r, AnonymousUnpaidInvoiceForm frm )
    {
        List<ARTransaction> invoices = null;
        ARTransaction transaction    = null;

        String invoiceNumber = frm.getInvoiceNumber();
        String invoiceAmount = frm.getOriginalInvoiceAmount();

        frm.clearSecureData();
        frm.clearSaveSelectedInvoices();
        frm.setSelectAll( null );

        //  Make sure our services are available before we begin.

        try
        {
            if (!SystemStatus.isCybersourceSiteUp() || 
                !SystemStatus.isRightslinkUp().booleanValue() ||
                !SystemStatus.isTelesalesUp().booleanValue() )
            {
                addError( r, "invoice.error.service_down" );
                return false;
            }
        }
        catch (Exception e) {
            addError( r, "invoice.error.service_down" );
            return false;
        }

        //  Start by performing a single transaction lookup based
        //  on the invoice # and original amount.  This is a semi-
        //  secure step to let us know that the person coming in
        //  has that information, at the very least, before we pull
        //  up ALL the invoices for the customer who owns the
        //  invoice[s].

        transaction = 
            InvoiceUtils.getUnpaidInvoiceByNumberAndFee(
                invoiceNumber,
                invoiceAmount
            );

        if (transaction != null)
        {
            frm.setArAccountNumber( transaction.getArAccountNumber() );

            //  With the single transaction, we can pull out the Accounts
            //  receivable account number and use it to pull back the
            //  rest of the open invoices for the customer.

            invoices = 
                InvoiceUtils.getUnpaidInvoicesForAccount( 
                    transaction.getArAccountNumber() 
                );

            if (invoices == null)
            {
                //  Should never happen because we KNOW there is at least
                //  ONE invoice at this point, but...  who knows.

                _logger.warn( 
                    "\nAnonymous Pay Invoice Step 2:  We found no invoices for " 
                    + invoiceNumber 
                    + " when there should have been at least one." 
                );

                addError( r, "invoice.error.notfound" );
                return false;
            }        
        }
        else
        {
            addError( r, "invoice.error.notfound" );
            return false;
        }
        frm.setMode( "0" );  // This means display the checklist of invoices.
        frm.setInvoices( invoices );

        //  Because the anonymous payer is not the actual customer, we
        //  need to do some leg work and find out more about the actual
        //  customer, this will be needed later in the payment process.

        TelesalesService telesales = 
            ServiceLocator.getTelesalesService();

        UserTypeEnum userType = 
            telesales.isOrganizationArAccount(
                new TelesalesServiceConsumerContext(),
                transaction.getArAccountNumber()
            ) 
            ? UserTypeEnum.ORG : UserTypeEnum.IND;

        ARAccount account = 
            telesales.getARAccountByARAccountNumber(
                new TelesalesServiceConsumerContext(),
                transaction.getArAccountNumber(),
                userType
            );

        Name name = account.getName();
        String who = null;

        if (name != null) {
            who = name.getFirstName() + " " + name.getLastName();
            
            frm.setFirstName( name.getFirstName() );
            frm.setLastName( name.getLastName() );
        }

        List<ContactPoint> cps = account.getContactPoints();

        String phone = null;
        String fax = null;
        String email = null;

        for (ContactPoint cp : cps) {
            if (cp.getContactPointType() == ContactPointTypeEnum.PHONE) 
            {
                if (cp.getPhoneLineType() == PhoneLineTypeEnum.GENERAL) {
                    phone = cp.getPhoneNumber();
                }
                else {
                    fax = cp.getPhoneNumber();
                }
            }
            if (cp.getContactPointType() == ContactPointTypeEnum.EMAIL) {
                email = cp.getEmailAddress();
            }
        }
        if (phone == null) phone = fax;

        //  All that above accumulates into these three setters.

        frm.setUserName( who );
        frm.setUserPhone( phone );
        frm.setUserEmail( email );

        return true;
    } 
}