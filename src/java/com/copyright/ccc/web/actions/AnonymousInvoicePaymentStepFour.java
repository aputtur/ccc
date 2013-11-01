package com.copyright.ccc.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

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
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.web.forms.AnonymousUnpaidInvoiceForm;
import com.copyright.ccc.web.util.WebUtils;

import com.copyright.ccc.business.services.cart.CreditCardDetails;
import com.copyright.ccc.business.services.cart.CheckoutServices;
import com.copyright.ccc.business.services.cart.InvoiceUtils;
import com.copyright.ccc.business.services.payment.PaymentResponse;

public class AnonymousInvoicePaymentStepFour extends Action
{
    //  These are our mappings to the various pages in our
    //  unpaid invoice flow.
    
    private static final String SUCCESS = "success";
    private static final String FAILURE = "failure";
    private static final String INVFORM = "anonymousUnpaidInvoiceForm";
    private static final String IFRAME_HTML_BEGIN = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\n<HTML>\n<HEAD><TITLE>Transaction Results</TITLE><STYLE>body { font-family: 'Arial'; }</STYLE><SCRIPT TYPE='text/javascript'>function show_continue() { var btn = parent.document.getElementById('displayContinue'); btn.style.display = ''; }</SCRIPT></HEAD>\n";
    private static final String IFRAME_HTML_BODY_SUCCESS = "<BODY onload='show_continue()'><BR/><BR/><BR/><BR/><BR/><BR/><P>Your credit card was authorized successfully<BR/> (you have not been charged).<BR/><BR/>Press 'continue' to confirm your transaction<BR/>and charge your credit card.</P></BODY>";
    private static final String IFRAME_HTML_BODY_FAILURE = "<BODY><BR/><P>We were unable to authorize your credit card.<BR/>Please click the 'Re-enter Credit Card' link and try again.</P></BODY>";
    private static final String IFRAME_HTML_END = "</HTML>";

    private static final Logger _logger = Logger.getLogger(AnonymousInvoicePaymentStepFour.class);

    @Override
    public ActionForward execute( ActionMapping       mapping
                                , ActionForm          form
                                , HttpServletRequest  request
                                , HttpServletResponse response )
    {
        String nextPage = SUCCESS;
        
        _logger.info("\nAnonymousInvoicePaymentStepFour.execute()\n");
        
        AnonymousUnpaidInvoiceForm frm = (AnonymousUnpaidInvoiceForm) 
            request.getSession().getAttribute( WebConstants.SessionKeys.ANONYMOUS_UNPAID_INVOICE_FORM );
        
        if (_logger.isDebugEnabled())
        {
            //  Dump our form data.
            
            _logger.info(frm.toString());
        }

        String operation = request.getParameter( "operation" );

        if ("cybersourceResponse".equalsIgnoreCase( operation ))
        {
            //  Pull out the response information sent to us from
            //  Cybersource.  The requestID is key to the continued
            //  processing of this authorization.

            PrintWriter out = null;

            try
            {
                out = response.getWriter();
            }
            catch (IOException e)
            {
                _logger.error( "\nAnonymousInvoicePaymentStepFour: Could not get printwriter." );
                addError( request, "An unexpected error occurred while trying to authorize your credit card." );
                return null;
            }

            if (_logger.isDebugEnabled())
            {
                Enumeration myEnum = request.getParameterNames();
                StringBuffer obuf = new StringBuffer();

                obuf.append( "\n\nCYBERSOURCE RESPONSE\n" );

                while(myEnum.hasMoreElements())
                {
                    String paramName = (String) myEnum.nextElement();
                    String paramValue = request.getParameter( paramName );

                    obuf.append( "\n" ).append( paramName )
                        .append( "    " ).append( paramValue );
                }
                _logger.info( obuf.toString() );
            }

            String ccAuthorized = IFRAME_HTML_BEGIN + IFRAME_HTML_BODY_SUCCESS + IFRAME_HTML_END;
            String ccRejected = IFRAME_HTML_BEGIN + IFRAME_HTML_BODY_FAILURE + IFRAME_HTML_END;

            String requestID = request.getParameter( "requestID" );
            String authorizationDecision = request.getParameter( "decision" );
            String orderPage_transactionType = request.getParameter( "orderPage_transactionType" );
            String orderAmount = request.getParameter( "orderAmount" );
            String orderCurrency = request.getParameter( "orderCurrency" );
            String authReplyAmount = request.getParameter( "ccAuthReply_amount" );
            String requestToken = request.getParameter( "orderPage_requestToken" );
            String reasonCode = request.getParameter( "reasonCode" );
            String authCode = request.getParameter( "ccAuthReply_authorizationCode" );
            String merchantID = request.getParameter( "merchantID" );
            String authDateTime = request.getParameter( "ccAuthReply_authorizedDateTime" );

            if (!"authorization".equalsIgnoreCase( orderPage_transactionType ))
            {
                //  Something is not right.  We should only ever receive a response
                //  for a transaction type of "authorization".  This should never happen.

                _logger.error( "\nrequestID = " + requestID + "\ntransaction type = " + orderPage_transactionType 
                    + "\n(transaction type should be authorization)" 
                );
                addError( request, "An unexpected error occurred while trying to authorize your credit card." );

                out.println( ccRejected );
                out.flush();
                return null;
            }

            String billTo_lastName = request.getParameter( "billTo_lastName" );
            String billTo_firstName = request.getParameter( "billTo_firstName" );
            String cardHolderName = billTo_firstName + " " + billTo_lastName;
            frm.setCardHolderDisplay( cardHolderName );

            String card_accountNumber = request.getParameter( "card_accountNumber" );
            String expirationDate = request.getParameter( "card_expirationMonth" ) 
                + "/" + request.getParameter( "card_expirationYear" );
            String card_cardType = request.getParameter( "card_cardType" );
            String lastFour = card_accountNumber == null ? "0000" : card_accountNumber.replace( "#", "" );
            frm.setCardLastFourDisplay( lastFour );
            frm.setCardTypeDisplay( card_cardType );

            if (authorizationDecision == null || "DECLINE".equalsIgnoreCase( authorizationDecision ))
            {
                _logger.info( "\nrequestID = " + requestID + "\ntransaction type = " + orderPage_transactionType 
                    + "\nreasonCode = " + reasonCode + "\n(DECLINE or null)" 
                );
                addError( request, "We were unable to place your order with the credit card information provided." );
                
                out.println( ccRejected );
                out.flush();
                return null;
            }

            if ("ACCEPT".equalsIgnoreCase( authorizationDecision ))
            {
                //  Fill in our form.  We will need the information to perform the
                //  final SALE...  build our creditCardDetails and paymentResponse
                //  objects.  I am over-stuffing the creditCardDetails object (see
                //  the paymentResponse object for overlap), but at worst some of 
                //  the fields will be overwritten in InvoiceUtilities.

                CreditCardDetails creditCardDetails = 
                    CheckoutServices.createCreditCardDetails();
                            
                creditCardDetails.setCurrencyType( orderCurrency );
                creditCardDetails.setExchangeRate( new BigDecimal( "1.0" ) );
                creditCardDetails.setCurrencyPaidTotal( new BigDecimal( orderAmount ) );
                creditCardDetails.setUsdPaidTotal( new BigDecimal( orderAmount ) );
                creditCardDetails.setCurrencyRateId( Long.valueOf( "1" ) );
                Date exchangeRateDate = new Date();
                creditCardDetails.setExchangeDate( exchangeRateDate );
                creditCardDetails.setCardHolderName( cardHolderName );
                creditCardDetails.setCardExpirationDate( expirationDate );
                creditCardDetails.setCardType( card_cardType );
                creditCardDetails.setLastFourCc( Integer.parseInt( lastFour ) );
                frm.setCardLastFourDisplay( lastFour );
                creditCardDetails.setAuthRequestId( requestID );
                creditCardDetails.setAuthRequestToken( requestToken );
                creditCardDetails.setAuthStatus( reasonCode );
                creditCardDetails.setAuthSuccess( "100".equalsIgnoreCase( reasonCode ) );
                creditCardDetails.setCcAuthNo( authCode );

                PaymentResponse paymentResponse = new PaymentResponse();

                paymentResponse.setAuthDate( authDateTime );
                paymentResponse.setAuthStatus( reasonCode );
                paymentResponse.setAuthSuccess( "100".equalsIgnoreCase( reasonCode ) );
                paymentResponse.setAuthRequestToken( requestToken );
                paymentResponse.setMerchantRefId( merchantID );
                paymentResponse.setCardType( card_cardType );
                paymentResponse.setRequestToken( requestToken );
                paymentResponse.setAuthRequestID( requestID );
                paymentResponse.setAuthNum( authCode );
                try {
                    paymentResponse.setFundingValue( Double.parseDouble( authReplyAmount ) );
                }
                catch (NumberFormatException e) {
                    paymentResponse.setFundingValue( 0.00D );
                }

                frm.setCreditCardDetails( creditCardDetails );
                frm.setPaymentResponse( paymentResponse );

                request.getSession().setAttribute( 
                    WebConstants.SessionKeys.ANONYMOUS_UNPAID_INVOICE_FORM, frm );
            }
            out.println( ccAuthorized );
            out.flush();
            return null;
        }

        //  The next step is to display the confirmation page.  If the user
        //  confirms, we finalize the charge to the card.

        return mapping.findForward( SUCCESS );
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

    /*
    Below is an example of the values returned from cybersource after an
    authorization request has been made...  This data was derived in
    DEV1...  I've left some fields as is, some are X'd out.  We do our best
    to map it into PaymentResponse and CreditCardDetail objects for
    the SALE phase which takes place in InvoiceUtils.
    
    field                           value
    ----------------------------    -----------------------------
    billTo_lastName                 Jessop
    billTo_country                  us
    ccAuthReply_cvCode              M
    signedDataPublicSignature       xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    reasonCode                      100
    billTo_hostname                 xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    decision                        ACCEPT
    card_expirationYear             2014
    ccAuthReply_reasonCode          100
    ccAuthReply_processorResponse   00
    orderPage_environment           TEST
    orderAmount                     15.00
    transactionSignature            xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    billTo_city                     Anthem
    billTo_postalCode               85086
    ccAuthReply_cvCodeRaw           M
    billTo_street1                  43373 N Vista Hills Dr
    card_accountNumber              ############1111
    orderAmount_publicSignature     xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    orderPage_requestToken          xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    orderPage_serialNumber          xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    card_expirationMonth            04
    ccAuthReply_amount              15.00
    orderCurrency_publicSignature   xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    merchantID                      xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    orderCurrency                   usd
    billTo_state                    AZ
    orderNumber_publicSignature     xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    requestID                       xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    orderPage_transactionType       authorization
    ccAuthReply_authorizationCode   xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    orderNumber                     xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    decision_publicSignature        xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    billTo_ipAddress                xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    billTo_firstName                Michael
    signedFields                    billTo_lastName,orderAmount,billTo_street1,card_accountNumber,
                                    orderAmount_publicSignature,orderPage_serialNumber,orderCurrency,
                                    ccAuthReply_cvCode,billTo_hostname,decision,
                                    ccAuthReply_processorResponse,ccAuthReply_cvCodeRaw,billTo_state,
                                    billTo_firstName,card_expirationYear,billTo_city,billTo_postalCode,
                                    orderPage_requestToken,ccAuthReply_amount,
                                    orderCurrency_publicSignature,orderPage_transactionType,
                                    ccAuthReply_authorizationCode,decision_publicSignature,
                                    ccAuthReply_avsCodeRaw,paymentOption,billTo_country,reasonCode,
                                    ccAuthReply_reasonCode,orderPage_environment,card_expirationMonth,
                                    merchantID,orderNumber_publicSignature,requestID,orderNumber,
                                    billTo_ipAddress,ccAuthReply_authorizedDateTime,card_cardType,
                                    ccAuthReply_avsCode
    card_cardType                   001
    ccAuthReply_authorizedDateTime  2012-12-06T183412Z
    ccAuthReply_avsCodeRaw          0
    ccAuthReply_avsCode             2
    paymentOption                   card
    VerifyTransactionSignature()    true
    */
}