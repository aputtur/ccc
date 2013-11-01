package com.copyright.ccc.web.actions;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.base.enums.PaymentGateway;
import com.copyright.ccc.CCRuntimeException;
import com.copyright.ccc.business.data.OrderLicense;
import com.copyright.ccc.business.data.OrderSearchResult;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.business.services.cart.CreditCardDetails;
import com.copyright.ccc.business.services.order.OrderLicenseServices;
import com.copyright.ccc.business.services.order.OrderLicensesException;
import com.copyright.ccc.business.services.payment.CreditCardPaymentService;
import com.copyright.ccc.business.services.payment.CyberSourceCreditCardResponses;
import com.copyright.ccc.business.services.payment.CyberSourceUtils;
import com.copyright.ccc.business.services.payment.PaymentRequest;
import com.copyright.ccc.business.services.payment.PaymentResponse;
import com.copyright.ccc.business.services.user.User;
import com.copyright.ccc.util.LogUtil;
import com.copyright.ccc.web.forms.AcceptSpecialOrderPaymentForm;
import com.copyright.ccc.web.forms.BasePaymentForm;
import com.copyright.ccc.web.util.WebUtils;
import com.copyright.svc.order.api.data.Item;
import com.copyright.svc.order.api.data.ItemStatusEnum;
import com.copyright.svc.order.api.data.ItemStatusQualifierEnum;
import com.copyright.svc.order.api.data.OrderConsumerContext;
import com.copyright.svc.order.api.data.Payment;
import com.copyright.workbench.logging.LoggerHelper;

public class AcceptSpecialOrderPaymentAction extends BasePaymentAction
{
    //  These are our mappings to the various pages in our
    //  unpaid invoice flow.

    private static final long serialVersionUID = 1L;
    private static final Logger _logger = LoggerHelper.getLogger();

    @Override
    public ActionForward execute( 
        ActionMapping       mapping, 
        ActionForm          form, 
        HttpServletRequest  request, 
        HttpServletResponse response )
    {
        AcceptSpecialOrderPaymentForm frm = WebUtils.castForm( AcceptSpecialOrderPaymentForm.class, form );
        if (frm == null) frm = new AcceptSpecialOrderPaymentForm();

        try
        {
            if (request.getParameter( "operation" ) != null)
            {
                String operation = request.getParameter( "operation" );

                if (operation.equalsIgnoreCase( "processPayment" )) {
                    return processPayment( mapping, form, request,  response);
                }
                else if (operation.equalsIgnoreCase( "alwaysInvoice" )) {
                    frm.setOrderLicense( getOrderLicenseById( Long.valueOf( frm.getLicenseId() ) ) );
                    frm.setItem( getItemById( Long.valueOf( frm.getLicenseId() ) ) );

                    if(frm.getOrderLicense() != null ) {
                        frm.setTotalAmount( frm.getOrderLicense().getTotalPriceValue() );
                    }
                    return processInvoicePayment( mapping, frm, request, response );
                }
            }
            else if (request.getParameter( "operation" ) == null) {
                frm.setOrderLicense( getOrderLicenseById( Long.valueOf( frm.getLicenseId() ) ) );
                frm.setItem( getItemById( Long.valueOf( frm.getLicenseId() ) ) );

                if(frm.getOrderLicense() != null ) {
                    frm.setTotalAmount( frm.getOrderLicense().getTotalPriceValue() );
                }
            }
            return super.execute( mapping, frm, request, response );
        }
        catch (CCRuntimeException e) {
            _logger.error( ExceptionUtils.getFullStackTrace( e ) );
            throw e;
        }
        //  return mapping.findForward(SUCCESS);
    }

    public ActionForward processPayment ( 
        ActionMapping       mapping, 
        ActionForm          form, 
        HttpServletRequest  request, 
        HttpServletResponse response )
    {
        AcceptSpecialOrderPaymentForm frm = WebUtils.castForm( AcceptSpecialOrderPaymentForm.class, form );

        if (frm.getPaymentType().equals( BasePaymentForm.PAYMENT_TYPE_INVOICE )) {
            return processInvoicePayment( mapping, form, request, response );
        }
        else if (frm.getPaymentType().equals( BasePaymentForm.PAYMENT_TYPE_CREDIT_CARD )) {
            return processCreditCardPayment( mapping, form, request, response );
        }
        else {
            addError( request, "invoice.error.emulating" );
            return mapping.findForward( FAILURE );
        }
    }

    public ActionForward processInvoicePayment ( 
        ActionMapping       mapping, 
        ActionForm          form, 
        HttpServletRequest  request, 
        HttpServletResponse response ) 
    {
        if (UserContextService.isEmulating()) {
            addError( request, "invoice.error.emulating" );
            return mapping.findForward( FAILURE );
        }
        response.setContentType( "text/html" );
        PrintWriter out;

        try {
            out = response.getWriter();
        } 
        catch (IOException e) {
            _logger.error( LogUtil.getStack( e ) );
            return mapping.findForward( FAILURE );
        }
        AcceptSpecialOrderPaymentForm frm = WebUtils.castForm( AcceptSpecialOrderPaymentForm.class, form );
        
        Item updateItem =
            ServiceLocator.getOrderService().getItemById (
                new OrderConsumerContext(), 
                frm.getItem().getItemId()
            );

        frm.setItem( updateItem );
        frm.getItem().setBaseInvoiceDate( new Date() );
        frm.getItem().setItemStatusCd( ItemStatusEnum.INVOICE_READY );
        frm.getItem().setItemStatusQualifier( ItemStatusQualifierEnum.INVOICE_READY_INVOICE );
        
        Item item = 
            ServiceLocator.getOrderService().updateItem (
                new OrderConsumerContext(), 
                frm.getItem()
            );
        
        frm.clearSecureData();

        StringBuilder sb = new StringBuilder();
        sb.append( "Your acceptance of the license for Order Detail ID: " );
        sb.append( frm.getLicenseId() );
        sb.append( " in the amount of $" );
        sb.append( frm.getTotalAmount() );
        sb.append( " has been successfully submitted." );
        out.println( sb );
        out.flush();

        return null;
    }


    public ActionForward processCreditCardPayment ( 
        ActionMapping       mapping, 
        ActionForm          form, 
        HttpServletRequest  request, 
        HttpServletResponse response )
    {
        Long paymentId=0L;
        AcceptSpecialOrderPaymentForm frm = WebUtils.castForm( AcceptSpecialOrderPaymentForm.class, form );
        
        if (_logger.isDebugEnabled())
        {
            _logger.info( "\n => Preparing to apply payment !\n" );
            _logger.info( "\n => " + frm.getPaymentType() + "!\n" );
        }

        if (UserContextService.isEmulating()) {
            addError( request, "invoice.error.emulating" );
            return mapping.findForward( FAILURE );
        }
        response.setContentType( "text/html" );
        PrintWriter out;

        try {
            out = response.getWriter();
        } 
        catch ( IOException e ) {
            _logger.error( LogUtil.getStack( e ) );
            return mapping.findForward( FAILURE );
        }

        /***********************************************************************************/
        // BUILD CREDIT CARD DETAILS

        /***********************************************************************************/
        // get CreditCard Details;
        CreditCardDetails creditCardDetails = CreditCardPaymentService.getNewCreditCardDetails();
        creditCardDetails.setPaymentGateway( PaymentGateway.CYBERSOURCE );

        //add user details
        User user = UserContextService.getActiveSharedUser();
        creditCardDetails.getPayment().setPayerPartyId( user.getPartyId() );
        creditCardDetails.getPayment().setPayerPtyInst( user.getPtyInst() );  	

        creditCardDetails.setCurrencyType( "USD" );
        creditCardDetails.setExchangeRate( new BigDecimal("1.0") );
        creditCardDetails.setCurrencyPaidTotal( frm.getOrderLicense().getTotalPriceValue() );
        creditCardDetails.setUsdPaidTotal( frm.getOrderLicense().getTotalPriceValue() );
        creditCardDetails.setCurrencyRateId( Long.valueOf("1") );
        Date exchangeRateDate = new Date();
        creditCardDetails.setExchangeDate( exchangeRateDate );
        creditCardDetails.setCardHolderName( frm.getCreditCardNameOn() );
        creditCardDetails.setCardExpirationDate( frm.getExpirationDate() );
        creditCardDetails.setCardType( frm.getCreditCardType() );
        String ccNumber = frm.getCreditCardNumber();
        try
        {
            creditCardDetails.setLastFourCc( new Integer( ccNumber ) );
        } 
        catch (NumberFormatException nfe) {
            throw new CCRuntimeException( nfe );
        }
        creditCardDetails.setPaymentProfileIdentifier( frm.getPaymentProfileId() );

        try {
            creditCardDetails.setPaymentProfileCccId(new Long( frm.getCccPaymentProfileId() ));
        }
        catch (NumberFormatException nfe) {
            _logger.error( ExceptionUtils.getFullStackTrace( nfe ) );
            throw new CCRuntimeException( nfe );
        } 

        /***********************************************************************************/
        // MERCHANT REF

        /***********************************************************************************/
        String merchantRefId="";

        try {
            merchantRefId = 
                ServiceLocator.getOrderService()
                    .getNewMerchantReferenceIdentifier( new OrderConsumerContext() );

            creditCardDetails.setMerchantRefId( merchantRefId );
        //merchantRefIdCyb = merchantRefId;
        } 
        catch (Exception e) {
            _logger.error( ExceptionUtils.getFullStackTrace( e ) );
            throw  new CCRuntimeException( "error getting merchant reference id sequence", e );           
        }

        /***********************************************************************************/
        // SAVE NEW PAYMENT

        /***********************************************************************************/
        try {
            Payment payment = 
                ServiceLocator.getOrderService()
                    .saveNewPayment( new OrderConsumerContext(), creditCardDetails.getPayment() );

            creditCardDetails.setPayment( payment );
            paymentId = creditCardDetails.getPaymentId();
        } 
        catch (Exception e) {
            _logger.error( ExceptionUtils.getFullStackTrace( e ) );
            throw  new CCRuntimeException( "error setting up payment", e );   
        }

        /***********************************************************************************/
        // AUTHORIZE
        /***********************************************************************************/
        PaymentResponse paymentResponse = null;

        PaymentRequest paymentRequest =  
            CyberSourceUtils.getPaymentRequest (
                UserContextService.getSharedUser(),
                creditCardDetails
            );

        paymentResponse = CyberSourceUtils.authorizeCreditCard( creditCardDetails, paymentRequest );

        /***********************************************************************************/
        // IF SUCCESS UPDATE PAYMENT and CHARGE CREDIT CARD

        /***********************************************************************************/
        if(paymentResponse != null && paymentResponse.getAuthSuccess())
        {
            try 
            {
                Payment payment = 
                    ServiceLocator.getOrderService().updatePayment (
                        new OrderConsumerContext(), 
                        creditCardDetails.getPayment()
                    );

                creditCardDetails.setPayment( payment );
            } 
            catch (Exception e) {
                _logger.error( ExceptionUtils.getFullStackTrace(e) );
                throw new CCRuntimeException( "error updating payment", e );  
            }
            try {
                paymentResponse = 
                    CyberSourceUtils.chargeCreditCard (
                        creditCardDetails,
                        paymentResponse, 
                        paymentRequest
                    );
            }
            catch( CCRuntimeException e ) {
                _logger.error( ExceptionUtils.getFullStackTrace(e) );
            } 
        }

        /***********************************************************************************/
        // LOG THE FAILURE

        /***********************************************************************************/
        if (paymentResponse==null || (paymentResponse != null && !paymentResponse.getSuccess()))
        {
            String cybersourceErrorMessage = 
                CyberSourceCreditCardResponses.getErrorMessage( creditCardDetails.getStatus() );

            creditCardDetails.setCybersourceErrorMessage( cybersourceErrorMessage );

            _logger.info( "Credit Card Auth Failed: " + UserContextService.getActiveAppUser().getUsername() + " PMT rec: " + paymentId + " "
                + "(" + creditCardDetails.getStatus() + ")" + cybersourceErrorMessage );
            
            StringBuilder sb = new StringBuilder();
            sb.append( "Payment failed :" + cybersourceErrorMessage );
            out.println( sb );
            out.flush();
        }

        /***********************************************************************************/
        // LOG THE SUCCESS and Update payment and Item
        /***********************************************************************************/
        if (paymentResponse != null && paymentResponse.getSuccess())
        {
            ServiceLocator.getOrderService().updatePayment (
                new OrderConsumerContext(), 
                creditCardDetails.getPayment()
            );

            frm.getItem().setBaseInvoiceDate( new Date()  );
            frm.getItem().setItemStatusCd( ItemStatusEnum.INVOICE_READY );
            frm.getItem().setItemStatusQualifier( ItemStatusQualifierEnum.INVOICE_READY_CREDIT_CARD );
            frm.getItem().setPaymentId( creditCardDetails.getPayment().getPaymentId() );

            ServiceLocator.getOrderService().updateItem (
                new OrderConsumerContext(), 
                frm.getItem()
            );

            frm.clearSecureData();
            StringBuilder sb = new StringBuilder();
            sb.append("Your acceptance of the license for Order Detail ID: ");
            sb.append(frm.getLicenseId());
            sb.append(" in the amount of $");
            sb.append(frm.getTotalAmount());
            sb.append(" has been successfully submitted.");
            out.println(sb);
            out.flush();
        }
        return null;
    }

    @Override
    public String getPaymentFormActionPath() 
    {
        return "viewSpecialOrderPaymentModal.do";
    }

    public OrderLicense getOrderLicenseByItemId( Long licenseId ) 
    {
        OrderSearchResult orderSearchResult=null;
        try {
            orderSearchResult = 
                OrderLicenseServices.searchOrderLicensesForDetailNumber( new Long( licenseId ) );
        } 
        catch (OrderLicensesException e) {
            _logger.error(ExceptionUtils.getFullStackTrace(e));
        }
        OrderLicense cancelOrderLicense = orderSearchResult.getOrderLicenseByItemId( licenseId );
        return cancelOrderLicense;
    }

    public OrderLicense getOrderLicenseById(Long licenseId)
    {
        OrderLicense orderLicense=null;
        OrderSearchResult orderSearchResult=null;
        try {
            orderSearchResult = OrderLicenseServices.searchOrderLicensesForDetailNumber( new Long(licenseId) );
            orderLicense = orderSearchResult.getOrderLicenseByItemId( licenseId );
        } 
        catch (OrderLicensesException e) {
            _logger.error(ExceptionUtils.getFullStackTrace(e));
        }
        return orderLicense;
    }

    public Item getItemById(Long licenseId)
    {
        Item item=null;
        item = OrderLicenseServices.getItemById( licenseId );
        //_logger.error(ExceptionUtils.getFullStackTrace(e));

        return item;
    }
}