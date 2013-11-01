package com.copyright.ccc.web.actions;

import java.math.BigDecimal;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.*;
import org.apache.log4j.Logger;

import com.copyright.ccc.CCRuntimeException;
import com.copyright.ccc.business.services.cart.CheckoutServices;
import com.copyright.ccc.business.services.cart.CreditCardDetails;
import com.copyright.ccc.business.services.cart.InvoiceUtils;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.config.CC2Configuration;
import com.copyright.ccc.util.NumberTools;
import com.copyright.ccc.web.forms.UnpaidInvoiceForm;
import com.copyright.ccc.web.util.WebUtils;
import com.copyright.svc.artransaction.api.data.ARTransaction;
import com.copyright.data.ValidationException;
import com.copyright.mail.MailDispatcher;
import com.copyright.mail.MailDispatcherImpl;
import com.copyright.mail.MailSendFailedException;
import com.copyright.mail.MailMessage;

public class PayInvoiceAndNotifyAction extends Action
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
        String arReceipt = null;
        String authMsg = null;
        String[] answer = null;
        String nextPage = FAILURE;
        Long acctNo = null;
        Double totalAmt = 0.00D;
        Double totalAmtUSD = 0.00D;
        String currencyCode = "";

        if (UserContextService.isEmulating()) {
             addError(request, "invoice.error.emulating");
             return mapping.findForward(FAILURE);
           }
                
        UnpaidInvoiceForm frm = WebUtils.castForm( UnpaidInvoiceForm.class, form );

        //  Load the unpaid invoices for the current active user.
        
        if (_logger.isDebugEnabled())
        {
            _logger.info("\n => Preparing to apply payment to unpaid invoices!\n");
        }
        
        for (ARTransaction transaction : frm.getInvoicesToCredit())
        {
            /*
             * djf = do I convert the total amount myself? verified that a non USD
             * credit card order converts the amount - so I did
             */
        	
        	Double amount =  NumberTools.getConvertedAmount( 
        			transaction.getBalanceDue(), 
        			transaction.getOriginalCurrencyCode() == null ? transaction.getCurrencyCode().getDesc() : transaction.getOriginalCurrencyCode().getDesc(),
        			transaction.getOriginalCurrencyRate() == null ? 1: Double.valueOf(transaction.getOriginalCurrencyRate()));
        	
        	totalAmt += amount;
        	totalAmtUSD += transaction.getBalanceDue();
            currencyCode = (transaction.getOriginalCurrencyCode() == null ? "USD" : transaction.getOriginalCurrencyCode().getDesc());
        }
        
        CreditCardDetails creditCardDetails = 
            CheckoutServices.createCreditCardDetails();
       			    
        creditCardDetails.setCurrencyType(currencyCode);
        /*
         * djf - what exchange rate do I use? Per Mike Tremblay 
         * leave at 1.0
         */
        creditCardDetails.setExchangeRate(new BigDecimal("1.0"));
        
        creditCardDetails.setCurrencyPaidTotal(new BigDecimal(totalAmt));
        creditCardDetails.setUsdPaidTotal(new BigDecimal(totalAmtUSD));
        /*
         * djf - Where do I get the rate id? verified that a non USD
         * credit card order sets this to "1" so I left it
         */
        creditCardDetails.setCurrencyRateId(Long.valueOf("1"));
        Date exchangeRateDate = new Date();
        creditCardDetails.setExchangeDate(exchangeRateDate);
        creditCardDetails.setCardHolderName(frm.getCreditCardNameOn());
        creditCardDetails.setCardExpirationDate(frm.getExpirationDate());
        creditCardDetails.setCardType(frm.getCreditCardType());
        String ccNumber = frm.getCreditCardNumber();
        
        try
        {
       	 creditCardDetails.setLastFourCc(new Integer(ccNumber));
        }catch (NumberFormatException nfe){
       	 throw new CCRuntimeException(nfe);
        }
        creditCardDetails.setPaymentProfileIdentifier(frm.getPaymentProfileId());
        
        try {
           creditCardDetails.setPaymentProfileCccId(new Long(frm.getCccPaymentProfileId()));
        }catch (NumberFormatException nfe) {
       	throw new CCRuntimeException(nfe);
        } 

       try
       {
            
    	   answer = InvoiceUtils.applyPaymentToUnpaidInvoices(frm.getInvoicesToCredit(), creditCardDetails); 

            arReceipt = answer[0];
            authMsg = answer[1];

            if (arReceipt == null)
            {
                //  This can't be good.

                ActionErrors errors = new ActionErrors();
                ActionMessage errorMessage = null;

                if (authMsg != null) {
                    errorMessage = new ActionMessage("errors.credit-card", authMsg);
                }
                else {
                    errorMessage = new ActionMessage("invoice.error.unknown");
                }
                errors.add(
                    ActionMessages.GLOBAL_MESSAGE,
                    errorMessage
                );
                request.setAttribute(Globals.ERROR_KEY, errors);
                return mapping.findForward(nextPage);
            }
            acctNo = UserContextService.getAuthenticatedSharedUser().getAccountNumber();

            //  Go ahead and send out our email message.  But since
            //  we got here, our payment was good, so let us set our
            //  flag to success.

            nextPage = SUCCESS;

            String email_to_user = frm.getUserEmail();
            String email_to_finance = CC2Configuration.getInstance().getInvoicePaymentEmailToFinance();
            String email_subj = CC2Configuration.getInstance().getInvoicePaymentEmailSubj();
            String email_body = buildMessage(frm);

            email_subj = email_subj + acctNo.toString();

            MailMessage msg = new MailMessage();
            msg.setFromEmail("donotreply@copyright.com");
            msg.setRecipient(email_to_user);
            msg.setBccRecipient(email_to_finance);
            msg.setSubject(email_subj);
            msg.setBody(email_body);
            
            //Disable the email send for now
            MailDispatcher email = new MailDispatcherImpl();

            email.send(msg); 
        }
        catch (ValidationException e)
        {
            String err = frm.getCreditCardName() + " ending in " + frm.getLastFourDigits();
            ActionErrors errors = new ActionErrors();

            errors.add(
                ActionMessages.GLOBAL_MESSAGE,
                new ActionMessage("errors.credit-card", err)
            );
            request.setAttribute(Globals.ERROR_KEY, errors);
        }
        catch (MailSendFailedException e)
        {
            _logger.error("\nInvoice Payment Transaction Completed but email failed.\n", e);

            String err = "Your transaction completed, but your email confirmation failed.";
            ActionErrors errors = new ActionErrors();

            errors.add(
                ActionMessages.GLOBAL_MESSAGE,
                new ActionMessage(err)
            );
            request.setAttribute(Globals.ERROR_KEY, errors);
        }
        catch (Exception e)
        {
            _logger.error("An unexpected error occurred.", e);

            String err = "An unexpected error occurred.";
            ActionErrors errors = new ActionErrors();

            errors.add(
                ActionMessages.GLOBAL_MESSAGE,
                new ActionMessage(err)
            );
            request.setAttribute(Globals.ERROR_KEY, errors);
        } 
        frm.clearSecureData();
        request.getSession().setAttribute(INVFORM, frm);

        return mapping.findForward(nextPage);
    }

    private String buildMessage(UnpaidInvoiceForm frm)
    {
        StringBuffer obuf = new StringBuffer();

        obuf.append("\nThank you for your recent online payment via Copyright.com.\n")
            .append("\nPlease see the payment details below.\n")
            .append("\nCopyright Clearance Center has charged your ")
            .append(frm.getCreditCardNameOn()).append(" card ")
            .append("[ending with ").append(frm.getCreditCardNumber()).append("] ")
            .append("and your credit card statement will reference ")
            .append("\"Copyright Clearance Center\".\n");

        obuf.append("\nPayment Details\n")
            .append("\nInvoice(s) Paid:  ");

        String currencyCode = null;
        
        for (ARTransaction x : frm.getInvoicesToCredit())
        {
            obuf.append(x.getTransactionNumber()).append(" ");
            currencyCode = x.getOriginalCurrencyRate() == null ? "USD" : x.getOriginalCurrencyCode().toString();       
        }
        obuf.append("\n\nTotal Payment:  ")
        	.append(currencyCode == null ? "USD" : currencyCode)
        	.append( " " )
            .append( (currencyCode != null && currencyCode.equals("JPY")) ? new java.text.DecimalFormat( "0" ).format( frm.getTotalAmount(true) ): new java.text.DecimalFormat( "0.00" ).format( frm.getTotalAmount(true) ) )
            .append("\n\n")
            .append("To view a list of unpaid invoices, please go to https://www.copyright.com/manageAccount.do, select View your Unpaid Invoices, and check the invoice number listed.")
            .append("\n\n")
            .append("If you need assistance, please visit our online help (www.copyright.com/help) where you will find answers to common questions. For further assistance, call +1-978-646-2600 (Mon-Fri, 8:00 am to 6:00 pm Eastern Time) to speak with a Customer Service Representative. Or, e-mail your questions and comments to: info@copyright.com.");

        obuf.append("\n\nCopyright Clearance Center")
            .append("\n222 Rosewood Drive")
            .append("\nDanvers, MA 01923")
            .append("\nTel:  +1-978-646-2600")
            .append("\nEmail:  info@copyright.com")
            .append("\nWeb:  http://www.copyright.com").append("\n")
            .append("Please do not reply to this message. This e-mail address is not monitored for responses.");

        return obuf.toString();
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