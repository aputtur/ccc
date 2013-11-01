package com.copyright.ccc.business.services.cart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.mail.internet.InternetAddress;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.copyright.base.enums.PaymentGateway;
import com.copyright.base.svc.ServiceRuntimeException;
import com.copyright.ccc.CCRuntimeException;
import com.copyright.ccc.config.CC2Configuration;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.business.services.payment.CyberSourceCreditCardResponses;
import com.copyright.ccc.business.services.payment.PaymentRequest;
import com.copyright.ccc.business.services.payment.PaymentResponse;
import com.copyright.ccc.business.services.user.User;
import com.copyright.ccc.util.ARTransactionByDateComparator;
import com.copyright.ccc.util.LogUtil;
import com.copyright.data.ValidationException;
import com.copyright.data.payment.AuthorizationException;
import com.copyright.data.payment.PaymentData;
import com.copyright.svc.artransaction.api.ARTransactionService;
import com.copyright.svc.artransaction.api.data.ARTransaction;
import com.copyright.svc.artransaction.api.data.ARTransactionServiceConsumerContext;
import com.copyright.svc.artransaction.api.data.enums.ARCurrencyCodeEnum;
import com.copyright.svc.artransaction.api.data.enums.ARStatusEnum;
import com.copyright.svc.artransaction.api.data.enums.ReceiptMethodEnum;
import com.copyright.svc.artransaction.api.data.enums.TransactionClassEnum;
import com.copyright.svc.artransaction.api.data.enums.TransactionTypeEnum;
import com.copyright.svc.order.api.data.InvoicePaymentReceipts;
import com.copyright.svc.order.api.data.InvoiceTypeEnum;
import com.copyright.svc.order.api.data.OrderConsumerContext;
import com.copyright.svc.order.api.data.Payment;
import com.copyright.svc.order.api.data.PaymentTypeEnum;
import com.copyright.svc.rlOrder.api.data.RightslinkServiceRuntimeException;
import com.copyright.workbench.security.SecurityRuntimeException;

public class InvoiceUtils
{
    //  Declare and initialize our class-static variables.
	
    private static final String NO_PROFILE_ID = "-9999";
    private static final String VISA_DISPLAY = "Visa";
    private static final String MASTERCARD_DISPLAY = "Master Card";
    private static final String AMERICAN_EXPRESS_DISPLAY = "American Express";
    private static final String VISA_CODE = "001";
    private static final String MASTERCARD_CODE = "002";
    private static final String AMERICAN_EXPRESS_CODE = "003";

	private InvoiceUtils(){}

    private static Logger _logger = Logger.getLogger(InvoiceUtils.class);
    

    //  Hopefully do this rarely.  Grab a reference to the
    //  Accounts Receivable web service.  The static portion
    //  of this class should keep it hanging around for awhile.

 
    /**
     *  Retrieve a list of unpaid invoices for the active, admin user.
     */
    public static List<ARTransaction> getUnpaidInvoicesForUser()
    {
        User user = null;
        String arAccountNumber = null;
        List<ARTransaction> arTransactions = null;

        //  The user manage account page attempts to weed out non-admin
        //  users for the unpaid invoice functionality, but we still want
        //  to check here as well to make sure the caller has the
        //  right to do so.

        user = validateAdminUser();
        if (user == null) return null;

        //  We have a valid, admin user.  First get our user and
        //  said person's account number, then grab the invoices.

        arAccountNumber = user.getArAccountNumber();

        if (arAccountNumber != null) {
            //  2012-11-29  MSJ
            //  I've separated out most of the invoice retrieval code
            //  so that I could call it by account number only for
            //  anonymous retrieval.

            arTransactions = getUnpaidInvoicesForAccount( arAccountNumber );
        }

        return arTransactions;
    }

    /*
     * Retrieve all valid invoices for a given account.
     */
    public static List<ARTransaction> getUnpaidInvoicesForAccount( String arAccountNumber )
    {
        List<ARTransaction> arTransactions = null;
        List<ARTransaction> arTransactionsOther = null;
        List<ARTransaction> unwantedItems = new ArrayList<ARTransaction>();
        List<TransactionTypeEnum> validTransactionTypes = new ArrayList<TransactionTypeEnum>();

        ARTransactionService arTransaction = ServiceLocator.getARTransactionService();

        //  Build our valid transaction type list that will be used to filter
        //  our undesirable invoice types.  This is to avoid attempted invoice
        //  payments against certain types of invoices that cause the AR
        //  update to fail.

        validTransactionTypes.add( TransactionTypeEnum.APS             );
        validTransactionTypes.add( TransactionTypeEnum.DPS             );
        validTransactionTypes.add( TransactionTypeEnum.ECC             );
        validTransactionTypes.add( TransactionTypeEnum.RLS             );
        validTransactionTypes.add( TransactionTypeEnum.TRS             );
        validTransactionTypes.add( TransactionTypeEnum.RIGHTSLINK      );
        validTransactionTypes.add( TransactionTypeEnum.RIGHTSLINKDM    );
        validTransactionTypes.add( TransactionTypeEnum.RIGHTSPHERETRX  );
        validTransactionTypes.add( TransactionTypeEnum.RLNKCCOM        );
        validTransactionTypes.add( TransactionTypeEnum.RLNKGIN         );
        validTransactionTypes.add( TransactionTypeEnum.RLNKPURCHASE    );
        validTransactionTypes.add( TransactionTypeEnum.RLNKRENTAL      );
        validTransactionTypes.add( TransactionTypeEnum.RLNKREPUB       );

        try {
            //  NOTE:  In the future we will likely change this call
            //  to getARTransactionsByARAccountAndStatusAndDateRange().

            arTransactions =
                arTransaction.getARTransactionsByARAccountAndStatus(
                    new ARTransactionServiceConsumerContext(),
                    arAccountNumber,
                    ARStatusEnum.OPEN,
                    TransactionClassEnum.INVOICE
                );
                
            arTransactionsOther =
                arTransaction.getARTransactionsByARAccountAndStatus(
                    new ARTransactionServiceConsumerContext(),
                    arAccountNumber,
                    ARStatusEnum.OPEN,
                    TransactionClassEnum.DEBIT_MEMO
                );
                
            if (arTransactions != null) {
                arTransactions.addAll( arTransactionsOther );
                //  Sort them.
                
                Collections.sort( arTransactions, new ARTransactionByDateComparator() );

                for (ARTransaction x : arTransactions)
                {
                    //  Weed out certain transactions by scanning
                    //  our list and earmarking them.
    
                    if ((!validTransactionTypes.contains( x.getTransactionType() )) ||
                        (x.getCreditCardInvoice() != null) ||
                        (x.getBalanceDue().doubleValue() <= 0D) ||
                        (x.getCurrencyCode() != ARCurrencyCodeEnum.USD))
                    {
                        unwantedItems.add( x );
                    }
                }
                for (ARTransaction x : unwantedItems)
                {
                    //  Remove the items from the list.
    
                    arTransactions.remove( x );
                }
            }     
            if (_logger.isDebugEnabled()) {
                StringBuffer obuf = new StringBuffer();

                obuf.append( "\nCall to getARTransactionsByARAccountAndStatus yielded:\n" );
                obuf.append( "\nusing account number " ).append( arAccountNumber )
                    .append( " with " ).append( " and " ).append( ARStatusEnum.OPEN )
                    .append( " and " ).append( TransactionClassEnum.INVOICE ).append( "\n" )
                    .append( " as well as " ).append( TransactionClassEnum.DEBIT_MEMO ).append( "\n" )
                    .append( " we get " );

                if (arTransactions == null) obuf.append("0 items\n");
                else obuf.append(arTransactions.size()).append(" items\n");

                _logger.info( obuf.toString() );
            }
        }
        catch (com.copyright.base.svc.ServiceRuntimeException e) {
            _logger.error( "An error occurred while attempting to retrieve invoices.  SQL query failed?" 
                + LogUtil.appendableStack( e ) );
        }
        catch (java.lang.IllegalArgumentException e) {
            _logger.error( "An error occurred while attempting to retrieve invoices.  Illegal Argument." 
                + LogUtil.appendableStack( e ) );
        }
        return arTransactions;
    }
    


    /**
     *  Accepts a list of ARTransactions and PaymentMethod, sums the transaction
     *  amounts and applies payment to specified payment method (credit card).
     *
     *  2012-11-29 MSJ  Modified parameter list to more explicitly pass user data.
     *                  Created a wrapper (see below) to mimic original footprint.
     */
    public static String[] applyPaymentToUnpaidInvoices( List<ARTransaction> transactions
                                                       , CreditCardDetails creditCardDetails
                                                       , PaymentResponse paymentResponse
                                                       , String accountNumber
                                                       , String firstName
                                                       , String lastName
                                                       , String emailAddress
                                                       , String userName
                                                       , boolean authorized )
    throws ValidationException
    {
        User authenticatedUser = null;
        boolean rollbackCharge = false;
        PaymentData pData = new PaymentData();
        Double totalAmt = 0.00D;
        String arReceipt = null;
        String arResponse = null;
        String[] response = new String[2];
        String receipt = null;
        ARTransactionService arTransaction = ServiceLocator.getARTransactionService();

        response[0] = null;
        response[1] = null;

        //  First thing we want to do is create an instance of
        //  a PaymentData class, then check it with the payment
        //  service.
        
        _logger.info( "\nUnpaid Invoice Payment STEP 1.  Totaling.\n" );
        
        for (ARTransaction transaction : transactions)
        {
            totalAmt += transaction.getBalanceDue();                
        }
        _logger.info( "\nUnpaid Invoice Payment STEP 2. Process Payment.\n" );
        
        String orderAmountString = "";
        
        if (creditCardDetails.getCurrencyPaidTotal() != null) {
            BigDecimal amount = creditCardDetails.getCurrencyPaidTotal().setScale( 2, BigDecimal.ROUND_HALF_EVEN );
            orderAmountString = amount.toString().trim();
        }
        
        String paymentProfileCccIdString = null;

        if (creditCardDetails.getPaymentProfileCccId() != null) {
            long cccProfileId = creditCardDetails.getPaymentProfileCccId().longValue();
            paymentProfileCccIdString = String.valueOf( cccProfileId );
        }
        String paymentId= "";
        String merchantRefIdCyb = null;
        String merchantRefId = null;
        String currencyType = creditCardDetails.getCurrencyType();
        double exchangeRate = creditCardDetails.getExchangeRate().doubleValue();
            
        creditCardDetails.setPaymentGateway( PaymentGateway.CYBERSOURCE );

        try
        {
            receipt = arTransaction.getNextARReceiptNumber( new ARTransactionServiceConsumerContext() );
        }
        catch (Exception e)
        {
            //  This could be a service error, or illegal argument.
            //  Since this will likely be a service error (this call
            //  doesn't have any arguments) shoot a blurb into the
            //  log and set a flag that lets us know we need to back
            //  out our charge to the credit card.

            rollbackCharge = true;
            _logger.error( "AR service failed while trying to credit an invoice." + LogUtil.appendableStack(e) );
        }
        merchantRefId = receipt;
        merchantRefIdCyb = merchantRefId;
        
        try {
            Payment payment = ServiceLocator.getCartService()
            	.saveNewPayment( new OrderConsumerContext(), creditCardDetails.getPayment() );
            	
            creditCardDetails.setPayment( payment );
            paymentId = creditCardDetails.getPaymentId().toString();
        } 
        catch (Exception e) {
            _logger.error( ExceptionUtils.getFullStackTrace( e ) );
        }
        
        PaymentRequest _paymentRequest = new PaymentRequest(
            orderAmountString, 
            paymentId, 
            currencyType, 
            creditCardDetails.getCardHolderName(),
            creditCardDetails.getCardType(), 
            creditCardDetails.getPaymentProfileIdentifier(),
            paymentProfileCccIdString, 
            creditCardDetails.getCardExpirationDate(), 
            creditCardDetails.getLastFourCc(), 
            merchantRefIdCyb, 
            exchangeRate
        );

        PaymentResponse _paymentResponse;

        if (!authorized) {
            _paymentResponse = new PaymentResponse();
        }
        else {
            _paymentResponse = paymentResponse;
        }
        
        try 
        {
            CheckoutServices.setUserInfoOnPaymentRequest( 
                accountNumber,
                firstName,
                lastName,
                emailAddress,
                _paymentRequest 
            );
            
            /*
             * This is just authorization step...  If we are coming from the anonymous branch
             * of code, we will want to skip this part, as it should already have been done.
             */

            if (!authorized)
            {
                _paymentResponse = CheckoutServices.authorizeCreditCard( _paymentRequest );
            }
            else
            {
                _paymentRequest.setAuthCode( _paymentResponse.getAuthNum() );
            }
            
            if (_paymentResponse != null && _paymentResponse.getAuthSuccess())
            {
                creditCardDetails.setAuthRequestId( _paymentResponse.getAuthRequestID() );
                creditCardDetails.setAuthRequestToken( _paymentResponse.getAuthRequestToken() );
                creditCardDetails.setAuthStatus( _paymentResponse.getAuthStatus() );
                creditCardDetails.setAuthSuccess( _paymentResponse.getAuthSuccess() );
                if (!authorized)
                {
                    //  This is set during the anonymous authorization; the value
                    //  is derived from the ccAuthReply_authorizationCode field.

                	creditCardDetails.setCcAuthNo( _paymentRequest.getAuthCode() );
            	}
                creditCardDetails.setCcTrxDate( new Date() );
                creditCardDetails.setPurchaseDate( new Date() );
                creditCardDetails.setReconciliationID( _paymentResponse.getReconciliationID() );
                creditCardDetails.setMerchantRefId( _paymentResponse.getMerchantRefId() );
                creditCardDetails.setCurrencyType( _paymentRequest.getCurrencyCode() );
                
                try 
                {
                    //Defect:This should be update payment , New payment was already created in previous step
                    Payment payment = ServiceLocator.getCartService().updatePayment( 
                        new OrderConsumerContext(), 
                        creditCardDetails.getPayment() 
                    );
                    creditCardDetails.setPayment( payment );
                    paymentId = creditCardDetails.getPaymentId().toString();
                } 
                catch (Exception e) {
                    _logger.error( ExceptionUtils.getFullStackTrace(e) );
                }
                
                /*
                 *  This is where the card is charged...
                 */
                _paymentResponse = CheckoutServices.chargeCreditCard( _paymentResponse, _paymentRequest );
            
                creditCardDetails.setCcAuthNo( _paymentRequest.getAuthCode() );
                creditCardDetails.setCcTrxDate( new Date() );
                creditCardDetails.setPurchaseDate( new Date() );
                creditCardDetails.setReconciliationID( _paymentResponse.getReconciliationID() );
                creditCardDetails.setMerchantRefId( _paymentResponse.getMerchantRefId() );
                creditCardDetails.setCurrencyType( _paymentRequest.getCurrencyCode() );
                creditCardDetails.setRequestId( _paymentResponse.getRequestID() );

                if (authorized) {
                    //  For credit process if we fail in our receipt/invoicing
                    //  calls...

                    _paymentRequest.setRequestID( _paymentResponse.getRequestID() );
                }

                try {
                    BigDecimal requestIdBigDecimal = new BigDecimal( _paymentResponse.getRequestID().trim() );
                    creditCardDetails.setCcTrxId( requestIdBigDecimal );
                }
                catch (NumberFormatException nfe) {
                    _logger.error( LogUtil.getStack( nfe ) );
                    throw new CCRuntimeException( nfe );
                }

                creditCardDetails.setRequestToken( _paymentResponse.getRequestToken() );
                creditCardDetails.setStatus( _paymentResponse.getStatus() );
                creditCardDetails.setSuccess( _paymentResponse.getSuccess() );
            }
        }
        catch ( CreditCardValidationException e )
        {
            _logger.error( ExceptionUtils.getFullStackTrace(e) );
            //rollbackCharge = true;
            //throw e;
        }
        catch ( CreditCardAuthorizationException e )
        {
            _logger.error( ExceptionUtils.getFullStackTrace(e) );
            //rollbackCharge = true;
            //throw e;
        }
        catch ( AuthorizationException e )
        {
            _logger.error( ExceptionUtils.getFullStackTrace(e) );
            //rollbackCharge = true;
            //throw e;
        } 
        catch( CCRuntimeException e )
        {
            _logger.error( ExceptionUtils.getFullStackTrace(e) );
            //rollbackCharge = true;
            //throw e;
        } 
            
        if (_paymentResponse != null && !_paymentResponse.getSuccess())
        {
            AuthorizationException a = new AuthorizationException( "CheckoutServices.checkoutCart: error during checkout using credit card as payment method." );
            String cybersourceErrorMessage = CyberSourceCreditCardResponses.getErrorMessage( creditCardDetails.getStatus() );
            creditCardDetails.setCybersourceErrorMessage( cybersourceErrorMessage );
            a.setMessageCode( cybersourceErrorMessage );

            _logger.info( "Credit Card Auth Failed: " + UserContextService.getActiveAppUser().getUsername() + " PMT rec: " + paymentId + " "
                + "(" + creditCardDetails.getStatus() + ")" + cybersourceErrorMessage );
        }

        String reasonCode = _paymentResponse.getStatus();

        if ( _logger.isDebugEnabled() )
        {
            if (reasonCode == "100") {
                _logger.debug("Cybersource Successfuly charged the card");
            }
            else if (reasonCode == "203") {
                _logger.debug("Cybersource rejected charging the card due to the Amount being charged is = >$1001 and <$2000 ");            
            } 
            else {
                _logger.debug("Cybersource rejected charging the card due to the Amount being charged is = >$2001");
            }
        }       
                
        if (_paymentResponse.getSuccess())
        {
            _logger.info("\nUnpaid Invoice Payment STEP 3.  Get Receipt #.\n");
            
            arResponse = _paymentResponse.getStatus();
            
            ServiceLocator.getCartService().updatePayment(new OrderConsumerContext() , 
                                                            creditCardDetails.getPayment());

            if (CC2Configuration.getInstance().simulateCybersourceRollback()) {
                //  If the appropriate property is set in CC2Configuration.properties,
                //  the value simulate.cybersource.rollback=true, then we set our
                //  rollback flag to TRUE and skip the invoicing and move on to
                //  the credit card refund stage.

                rollbackCharge = true;
            }
                            
            //  If we got here our CC transaction was approved.
            //  Now we need to update our invoice transactions.
            //  The TRICKY part is if this update fails, we must
            //  CREDIT the CC we just DEBITed.
                  
            if (!rollbackCharge)
            {
                //  If we got here all is well so far.  Things could still
                //  fail but let us hope this is not the case.
                
                _logger.info("\nUnpaid Invoice Payment STEP 4.  Apply receipt to invoices.\n");

                try
                {
                    arReceipt = arTransaction.createAndApplyARReceiptToMultipleInvoices(
                        new ARTransactionServiceConsumerContext(),
                        transactions,
                        accountNumber,
                        totalAmt,
                        ReceiptMethodEnum.CCC_CREDIT_CARD,
                        _paymentResponse.getRequestID(),
                        toCardTypeName(creditCardDetails.getCardType()),
                        receipt
                    );
                }
                catch (ServiceRuntimeException e)
                {
                    arResponse = null;
                    rollbackCharge = true;
                    _logger.error( "AR service failed while trying to credit an invoice." + LogUtil.appendableStack( e ) );
                }
                catch (IllegalArgumentException e)
                {
                    arResponse = null;
                    rollbackCharge = true;
                    _logger.error( "AR service did not like the parameters it received." + LogUtil.appendableStack( e ) );

                    StringBuffer obuf = new StringBuffer();
                    obuf.append( "\nInvoice Data:\n" );

                    for (ARTransaction x : transactions) {
                        obuf.append("\nTrx No: ").append( x.getTransactionNumber() );
                    }
                    obuf.append( "\nAcct: " ).append( accountNumber )
                        .append( "\nAmount: " ).append( totalAmt )
                        .append( "\nMethod: " ).append( ReceiptMethodEnum.CCC_CREDIT_CARD )
                        .append( "\nAuth: " ).append( creditCardDetails.getCcAuthNo() )
                        .append( "\nCC: " ).append( creditCardDetails.getCardHolderName() )
                        .append( "\nRecpt: " ).append( receipt );
                    _logger.error( obuf.toString() );
                }
                catch (Exception e)
                {
                    arResponse = null;
                    rollbackCharge = true;
                    _logger.error( "AR service error." + LogUtil.appendableStack( e ) );
                }
                response[0] = arReceipt;
                response[1] = arResponse;
            }
            
            _logger.info("\nUnpaid Invoice Payment rollbackCharge : " + Boolean.toString( rollbackCharge ) + "\n");
            
            if (!rollbackCharge)
            {
                //Add a row to the invoice_payment_receipts table
                for (ARTransaction x : transactions)
                {
                    InvoicePaymentReceipts  invReceipt = new InvoicePaymentReceipts();

                    if (_logger.isDebugEnabled())
                    {
                        _logger.info( "\nAnonymousInvoicePayment transactionNumber : " + x.getTransactionNumber() + "\n" );
                    }
                
                    if (x.getTransactionNumber().startsWith( "RLNK" ))
                    {
                        invReceipt.setInvoiceType( InvoiceTypeEnum.RIGHTSLINK );
                    }
                    else
                    {
                        invReceipt.setInvoiceType( InvoiceTypeEnum.TRANSACTIONAL );
                    }
                    invReceipt.setInvoiceNumber( x.getTransactionNumber() );
                    invReceipt.setAccountNumber( accountNumber );
                    invReceipt.setReceiptNumber( arReceipt );
                    invReceipt.setUsername( userName );
                    invReceipt.setPaymentType( PaymentTypeEnum.CREDITCARD );
                    invReceipt.setPaymentGateway( PaymentGateway.CYBERSOURCE );
                    invReceipt.setMerchantRequestId( _paymentResponse.getRequestID() );
                    invReceipt.setAmount( x.getTotalAmt() );
                    invReceipt.setCurrencyCode( x.getCurrencyCode().name() );
                    invReceipt.setTransactionType( x.getTransactionType().getTransactionTypeId() );
                    
                    try
                    {
                        ServiceLocator.getOrderService().insertInvoicePaymentReceipt( new OrderConsumerContext(), invReceipt );
                    }
                    catch(IllegalArgumentException iae)
                    {
                        _logger.error( "AR service did not like the parameters it received." + LogUtil.appendableStack( iae ) );
                        
                        StringBuffer obuf = new StringBuffer();
                        obuf.append( "\nInvoice Data:\n" );
                        obuf.append( "\nTrx No: " ).append( invReceipt.getInvoiceNumber() )
                            .append( "\nAcct: " ).append( accountNumber )
                            .append( "\nAmount: " ).append( totalAmt )
                            .append( "\nMethod: " ).append( ReceiptMethodEnum.CCC_CREDIT_CARD )
                            .append( "\nAuth: " ).append( creditCardDetails.getCcAuthNo() )
                            .append( "\nCC: " ).append( creditCardDetails.getCardHolderName() )
                            .append( "\nRecpt: " ).append( receipt );
                        _logger.error( obuf.toString() );
                        
                    }
                                    
                    if (x.getTransactionNumber().startsWith( "RLNK" ))
                    {
                        _logger.info( "\nInvoiceUtils updating invoice for RLNK.\n" );

                        com.copyright.rightslink.base.data.PaymentData paymentData = 
                            new com.copyright.rightslink.base.data.PaymentData();
                            
                        //Update the RLink invoices TBD
                        paymentData.setAuthNum( creditCardDetails.getCcAuthNo() );
                        paymentData.setCardType( creditCardDetails.getCardType() );
                        paymentData.setCardHolderName( creditCardDetails.getCardHolderName() );
                        paymentData.setPlainTextExpirationDate( creditCardDetails.getCardExpirationDate() );
                        paymentData.setPlainTextLast4Digits( creditCardDetails.getLastFourCc().toString() );
                        paymentData.setCurrencyRateId( creditCardDetails.getCurrencyRateId().intValue() );
                        paymentData.setReconciliationID( creditCardDetails.getReconciliationID() );
                        paymentData.setRequestID( _paymentResponse.getRequestID() );
                        paymentData.setRequestToken( creditCardDetails.getRequestToken() );

                        if (!authorized) {
                            //  Use the same logic as above, check the "authorized" flag.  If
                            //  it is TRUE that means we are coming via anonymous, if FALSE we
                            //  are following the normal path.

                            paymentData.setRlProfileId( creditCardDetails.getPaymentProfileCccId().toString() );
                        }
                        else {
                            paymentData.setRlProfileId( NO_PROFILE_ID );
                        }
                        paymentData.setStatus( creditCardDetails.getStatus() );
                        paymentData.setFundingCurrency( creditCardDetails.getCurrencyType() );
                        paymentData.setFundingValue( x.getTotalAmt() );
                        paymentData.setSuccess( true );
                        
                        String invoice = x.getTransactionNumber();
                        invoice = invoice.replaceFirst( "RLNK", "" );
                        
                        try
                        {
                            ServiceLocator.getRLOrderService().updateInvoice( Long.valueOf(invoice), paymentData );
                        }
                        catch (IllegalArgumentException e)
                        {
                            _logger.error( "Illegal argument for RightLink paymentData." + LogUtil.appendableStack( e ) );
                            StringBuffer obuf = new StringBuffer();
                            obuf.append( "\nRightsLink reconciliation failed. Invoice Data:\n" )
                                .append( "\nTrx No: " ).append( x.getTransactionNumber() )
                                .append( "\nAcct: " ).append( accountNumber )
                                .append( "\nAmount: " ).append( x.getTotalAmt() )
                                .append( "\nMethod: " ).append( ReceiptMethodEnum.CCC_CREDIT_CARD )
                                .append( "\nAuth: " ).append( creditCardDetails.getCcAuthNo() )
                                .append( "\nReconciliation id: " ).append( creditCardDetails.getReconciliationID() )
                                .append( "\nRequest id: " ).append( creditCardDetails.getRequestId() )
                                .append( "\nProfile id: " ).append( creditCardDetails.getPaymentProfileCccId().toString() )
                                .append( "\nCC: " ).append( creditCardDetails.getCardHolderName() )
                                .append( "\nRecpt: " ).append( receipt );
                            _logger.error( obuf.toString() );
                        }
                        catch (RightslinkServiceRuntimeException ex)
                        {
                            _logger.error( "\nRightsLink Service runtime error." + LogUtil.appendableStack( ex ) );
                        }
                        catch (Exception e)
                        {
                            _logger.error( "\nAn unchecked error occurred while updating RLNK invoice.  " + LogUtil.appendableStack( e ) );
                        }
                    }
                }
            }          
            if (rollbackCharge)
            {
                //  This stinks.  Apparently our service is down.
                //  We need to credit the user and get out of Dodge.
                //  It might be good to send an email message to finance,
                //  but for now this is all we will do.

                _logger.error( "InvoiceUtils.applyPaymentToUnpaidInvoices:  ROLLING BACK CHARGE." );
                
                PaymentResponse _paymentResponseBack = new PaymentResponse();
                
                _paymentResponseBack = CheckoutServices.issueCredit( _paymentRequest );

                if (_logger.isDebugEnabled()) {
                    _logger.error(
                        "\nAUTH STATUS: " + _paymentResponseBack.getAuthStatus() +
                        ", REQUEST ID: " + _paymentResponseBack.getAuthRequestID() + 
                        "\nAUTH CODE: " + _paymentResponseBack.getAuthNum() +
                        ", AUTH DATE: " + _paymentResponseBack.getAuthDate()
                    );
                }
                
                response[0] = null;
                response[1] = "Credited back to the Customer due to problems";
            }  
        }
        return response;
    }

    private static User validateAdminUser()
    {
        User authenticatedUser = null;

        try {
            _logger.info("\nVerifying " + UserContextService.getActiveAppUser().getUsername());

            //Remove ADMIN user restriction
           /* if (!UserContextService.isAdminUser() && !UserContextService.isEmulating()) {
                _logger.info("\nNOT the ADMIN USER!\n");
                return null;
            } */

            //  You have to be logged in to emulate, so this should be safe.
            //  If the user is emulating, be sure to return the ACTIVE user.
            
            if (UserContextService.isEmulating()) {
                authenticatedUser = UserContextService.getActiveSharedUser();
            }
            else {
                authenticatedUser = UserContextService.getAuthenticatedSharedUser();
            }
        }
        catch (SecurityRuntimeException e) {
            _logger.error("\nUnable to validate user.  Service may be down." + LogUtil.appendableStack(e));
            return null;
        }
        return authenticatedUser;
    }

    //  2012-05-09  MSJ
    //  We are allowing non-account members of c.com to make payments on
    //  behalf of other people.  To do this we require them to provide us
    //  with an invoice number and the exact amount to be paid.  If these
    //  criteria (and the much of the criteria of getUnpaidInvoicesForUser)
    //  are met we will return a single transaction.  NOTE:  The transaction
    //  data that is returned is NOT COMPLETE.  It is minimal, what is required
    //  to perform an account lookup of the user, only.

    public static ARTransaction getUnpaidInvoiceByNumberAndFee( String invoiceNumber
                                                              , String originalAmount )
    {
        ARTransaction result = null;
        ARTransactionService arTransaction = ServiceLocator.getARTransactionService();

        try {
            result = arTransaction.getARTransactionByTransactionNumberAndAmount(
                new ARTransactionServiceConsumerContext(),
                invoiceNumber, Double.valueOf( originalAmount )
            );
        }
        catch (java.lang.NumberFormatException e) {
            _logger.error( "An error occurred while attempting to retrieve an invoice.  Invalid Argument."
                + LogUtil.appendableStack( e ) );
        }
        catch (com.copyright.base.svc.ServiceRuntimeException e) {
            _logger.error( "An error occurred while attempting to retrieve an invoice.  SQL query failed?"
                + LogUtil.appendableStack( e ) );
        }
        return result;
    }

    //  2012-11-29  MSJ
    //  Modified the original method to use more explicit parameters.  Wrapped
    //  it with this method for regular payments, vs. anonymous payments.

    public static String[] applyPaymentToUnpaidInvoices( List<ARTransaction> transactions
                                                       , CreditCardDetails creditCardDetails )
    throws ValidationException
    {
        User authenticatedUser = null;
        String[] response = new String[2];

        response[0] = null;
        response[1] = null;

        authenticatedUser = validateAdminUser();

        if (authenticatedUser == null)
        {
            _logger.error( "\nUnauthorized user attempted to make invoice payment." );
            return response;
        }
        InternetAddress iAddr = authenticatedUser.getEmailAddress();
        String accountNumber = authenticatedUser.getAccountNumber().toString();
        String emailAddress = "";

        if (iAddr.getAddress() != null) {
            emailAddress = iAddr.getAddress();
        }

        response = applyPaymentToUnpaidInvoices(
            transactions,
            creditCardDetails,
            null,
            accountNumber,
            authenticatedUser.getFirstName(),
            authenticatedUser.getLastName(),
            emailAddress,
            authenticatedUser.getUsername(),
            false
        );
        return response;
    }
    
    public static String toCardTypeName(String cardType_code) 
    {
        if (_logger.isDebugEnabled()) _logger.info("\nCard Type Code: " + cardType_code);

        if (cardType_code != null) 
        {
            if (cardType_code.equalsIgnoreCase(VISA_CODE))
                return VISA_DISPLAY;
            else if (cardType_code.equalsIgnoreCase(MASTERCARD_CODE))
                return MASTERCARD_DISPLAY;
            else if (cardType_code.equalsIgnoreCase(AMERICAN_EXPRESS_CODE))
                return AMERICAN_EXPRESS_DISPLAY;
        } else {
        	return "";
        }
        
        return cardType_code;
    }
}