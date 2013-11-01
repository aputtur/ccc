package com.copyright.ccc.web.actions;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import org.json.JSONException;
import org.json.JSONStringer;

import com.copyright.base.enums.PaymentGateway;
import com.copyright.ccc.CCRuntimeException;
import com.copyright.ccc.business.data.CCUser;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.business.services.SystemStatus;
import com.copyright.ccc.business.services.payment.CyberSourcePaymentVendor;
import com.copyright.ccc.business.services.payment.CyberSourceUtils;
import com.copyright.ccc.business.services.user.User;
import com.copyright.ccc.config.CybersourceConfiguration;
import com.copyright.ccc.util.LogUtil;
import com.copyright.ccc.util.NumberTools;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.BasePaymentForm;
import com.copyright.ccc.web.forms.AnonymousUnpaidInvoiceForm;
import com.copyright.ccc.web.util.WebUtils;
import com.copyright.svc.ldapuser.api.data.LdapUser;
import com.copyright.svc.userInfo.api.cyberSource.data.CurrencyRateInfo;
import com.copyright.svc.userInfo.api.cyberSource.data.PaymentProfileInfo;
import com.copyright.svc.userInfo.api.cyberSource.data.SortCriteria;
import com.copyright.svc.userInfo.api.cyberSource.data.CurrencyRateInfo.CurrencyRateInfoKeys;
import com.copyright.svc.userInfo.api.cyberSource.data.PaymentProfileInfo.PaymentProfileInfoKeys;
import com.copyright.svc.userInfo.api.data.UserInfoConsumerContext;
import com.copyright.workbench.logging.LoggerHelper;

public abstract class AnonymousBasePaymentAction extends Action
{
    protected static final String SUCCESS = "success";
    protected static final String FAILURE = "failure";
    
    protected static final String ACCEPT = "ACCEPT";
    protected static final String REBUILD_CREDIT_CARD_LIST = "retrieveCreditCardList";
    protected static final String IFrameHTMLBegin = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\n<HTML>\n<HEAD><TITLE>Transaction Results</TITLE></HEAD>\n";
    
	private   static final String CREDIT_CARD_HTML = IFrameHTMLBegin + "<BODY onload='top.getCreditCards();' ></BODY></HTML>";
    protected static final String CREDIT_CARD_FAILURE_HTML = IFrameHTMLBegin + "<BODY onload='top.handleCreditCardTxFailure();'></BODY></HTML>";
    protected static final String MODIFY_SUBSCRIPTION = "subscription_modify";
    protected static final String NEW_SUBSCRIPTION = "subscription";
    protected static final String DISABLE_CREDIT_CARD = "disableCreditCard";
    protected static final String CYBERSOURCE_RESPONSE = "cyberSourceResponse";
    protected static final String BUILD_SUBSCRIPTION_SIGNATURE = "buildSubscriptionSignature";
    protected static final String MULTI_CURRENCY_RATES = "multiCurrencyRates";
    
	private static final long serialVersionUID = 1L;
	
	private static final Logger _logger = LoggerHelper.getLogger();

	/**
	 * This method rebuilds the html
	 * 
	 * 
	 * @return
	 */
    public ActionForward execute( ActionMapping       mapping
                                , BasePaymentForm     form
                                , HttpServletRequest  request
                                , HttpServletResponse response )
    {
        AnonymousUnpaidInvoiceForm frm = (AnonymousUnpaidInvoiceForm) form;

	    if (UserContextService.isEmulating()) 
        {
            addError( request, "invoice.error.emulating" );
            return mapping.findForward( FAILURE );
        }
        if (request.getParameter( "operation" ) != null)
        {
            String operation = request.getParameter( "operation" );

            if (operation.equalsIgnoreCase( BUILD_SUBSCRIPTION_SIGNATURE ))
            {
                try
                {
                    return this.buildSubscriptionSignature( mapping, frm, request, response );
                }
                catch (IOException e)
                {
                    _logger.error( LogUtil.getStack( e ) );
                    return mapping.findForward( FAILURE );
                }
            }
            else if (operation.equalsIgnoreCase( CYBERSOURCE_RESPONSE ))
            {
                try
                {
                    return this.cyberSourceResponse( mapping, frm, request, response ) ;
                }
                catch (IOException e)
                {
                    _logger.error( LogUtil.getStack( e ) );
                    return mapping.findForward( FAILURE );
                }
            }
            else if (operation.equalsIgnoreCase( MULTI_CURRENCY_RATES ))
            {
                try
                {
                    return this.multiCurrencyRates( mapping, frm, request, response );
                }
                catch (IOException e)
                {
                    _logger.error( LogUtil.getStack( e ) );
                    return mapping.findForward( FAILURE );
                }
            }
        }
     
        // if you come till here then handle it in inherited action

     	if (request.getParameter( "operation" ) != null)       
        {
       	    return null;
        }
   
        try
        {
            frm.clearSecureData();
            	
    	 	String url = CyberSourceUtils.getResponseUrlBase( request ) +
    	 	    "/" + getPaymentFormActionPath() + "?operation=cyberSourceResponse";

    		String hopURL = CybersourceConfiguration.getInstance().getHopUrl();
    		String responseEmail = CybersourceConfiguration.getInstance().getSubscriptionTxResponseEmail();
    		String subscriptionSignature = CyberSourceUtils.insertSubscriptionSignature( "0","20190731","on-demand","0","false" );

    		frm.setHopURL( hopURL );
    		frm.setCccURL( url );
    		frm.setResponseEmail( responseEmail );
    		frm.setSubscriptionSignature( subscriptionSignature );
    	    
    	    frm.setFirstName( "" );
    	    frm.setLastName( "" );
    	    frm.setAddress1( "" );
    	    frm.setAddress2( "" );
    	    frm.setCity( "" );
    	    frm.setState( "" );
    	    frm.setZip( "" );
    	    frm.setCountry( "" );
    	    frm.setUserName( "Anonymous" );
    	    frm.setUserEmail( frm.getEmailAddress() );
    	    frm.setUserPhone( "" );
        }
        catch (CCRuntimeException e)
        {
        	_logger.error( ExceptionUtils.getFullStackTrace( e ) );
    		throw e;
        }
        return mapping.findForward( SUCCESS );
    }
   
    /*
     * Default action method for use by cybersource response
     */
    
    public ActionForward cyberSourceResponse( ActionMapping       mapping
                                            , ActionForm          form
                                            , HttpServletRequest  request
                                            , HttpServletResponse response ) 
    throws IOException
    {    	 
        response.setContentType( "text/html" );
        response.setCharacterEncoding( "UTF-8" );

        AnonymousUnpaidInvoiceForm frm = (AnonymousUnpaidInvoiceForm) form;

        PrintWriter out = response.getWriter();

        //  The cybersource response contains a lot of information.  The subscription ID
        //  can be used (and should be used) throughout the process.  It's a sort of
        //  session key.

        String paySubscriptionCreateReply_subscriptionID = request.getParameter( "paySubscriptionCreateReply_subscriptionID" );
        String orderPage_transactionType = request.getParameter( "orderPage_transactionType" );
        String card_accountNumber = request.getParameter( "card_accountNumber" );
        String billTo_lastName = request.getParameter( "billTo_lastName" );
        String billTo_firstName = request.getParameter( "billTo_firstName" );
        String cardHolderName = billTo_firstName + " " + billTo_lastName;
        String expirationDate = request.getParameter( "card_expirationMonth" ) + "/" + request.getParameter( "card_expirationYear" );
        String card_cardType = request.getParameter( "card_cardType" );
        String cyberSourceProfileCreationDecision = request.getParameter( "decision" );
        String lastFour = "";
        if (card_accountNumber != null) lastFour = card_accountNumber.replace( "#", "" );

        frm.setCardTypeDisplay( card_cardType );
        frm.setCardHolderDisplay( cardHolderName );
        frm.setCardExpirationDisplay( expirationDate );
        frm.setCardLastFourDisplay( lastFour );

        //  Was this part of something else?

        String action = request.getParameter( "operation" );

        if (action == null)
        {
            action = "";
        }

        try
        {
            String userid = "";
            String cardTypeName = CyberSourcePaymentVendor.toCardTypeName( card_cardType );

            if (NEW_SUBSCRIPTION.equalsIgnoreCase( orderPage_transactionType ))
            {
                StringBuffer sb2 = new StringBuffer();

                if (ACCEPT.equalsIgnoreCase( cyberSourceProfileCreationDecision )) 
                {
                    _logger.info( "\nAnonymous Invoice Payment:  Cybersource subscription created." );
                    frm.setPaymentProfileId( paySubscriptionCreateReply_subscriptionID );
                    sb2.append( CREDIT_CARD_HTML );
                }
                else
                {
                    if ( _logger.isDebugEnabled() )
                    {
                        _logger.debug( "\nAnonymous Invoice Payment:  CyberSource Subscription Creation NOT successful!" );
                    }
                    sb2.append( CREDIT_CARD_FAILURE_HTML );
                }
                out.println( sb2 );
                out.flush();
                return null;
            }
            else if (MODIFY_SUBSCRIPTION.equalsIgnoreCase( orderPage_transactionType )) 
            {
                StringBuffer sb2 = new StringBuffer();

                if (ACCEPT.equalsIgnoreCase( cyberSourceProfileCreationDecision )) 
                {
                    _logger.info( "\nAnonymous Invoice Payment: temporary subscription modified." );
                    frm.setPaymentProfileId( paySubscriptionCreateReply_subscriptionID );
                    sb2.append( CREDIT_CARD_HTML );
                } 
                else 
                {   
                    // CyberSource did not accept the subscription modification transaction

                    if ( _logger.isDebugEnabled())
                    {
                        _logger.debug( "CyberSource Subscription Modification NOT successful ! {subscriptionid=" 
                                + paySubscriptionCreateReply_subscriptionID + "}" );
                    }		
                    sb2.append( CREDIT_CARD_FAILURE_HTML );
                }
                out.println( sb2 );
                out.flush();
                return null;
            }			
        } 
        catch (CCRuntimeException e)
        {
            _logger.error( ExceptionUtils.getFullStackTrace( e ) );
            throw new CCRuntimeException( e );
        }
        return null;
    }
     
   
    public ActionForward buildSubscriptionSignature( ActionMapping       mapping
                                                   , ActionForm          form
                                                   , HttpServletRequest  request
                                                   , HttpServletResponse response )
    throws IOException
    {
        String sbJson = "";

        response.setContentType( "text/html" );
        response.setCharacterEncoding( "UTF-8" );

        PrintWriter out = response.getWriter();
        StringBuilder sb = new StringBuilder();

        String csProfileid = request.getParameter( "profileid" );
        sb.append( CyberSourceUtils.insertModifySubscriptionIDSignature( csProfileid ) );
/*
        UserInfoConsumerContext consumerCtx = new UserInfoConsumerContext();

        try
        {
            List<PaymentProfileInfo> creditCards = null;
            PaymentProfileInfo query = new PaymentProfileInfo();
            query.setPaymentProfileId( csProfileid );

            if (csProfileid != null) 
            {
                creditCards =ServiceLocator.getCyberSourceService()
                    .findPaymentProfiles( consumerCtx, query, 0, 25, null );
            }
            if(creditCards==null)
            {
                return null;
            }
            // now build the hidden fields for card type, expiration, and cardholder name

            Iterator<PaymentProfileInfo> theCreditCards = creditCards.iterator();
            PaymentProfileInfo creditCard = null;
            int counter = 1;

            while (theCreditCards.hasNext()) 
            {
                creditCard = theCreditCards.next();
                String card_cardType = creditCard.getCardType();
                String expirationDate = creditCard.getExpirationDate();
                String cardTypeCode = CyberSourcePaymentVendor.toCardTypeCode( card_cardType );
                sb.append( "<input type=\"hidden\" name=\"card_cardType\" value=\"" + cardTypeCode + "\">" );
                String[] expDateParts = {};
                if (expirationDate != null) expDateParts = expirationDate.split("/");
                String expMM = "", expYYYY = "";
                if (expDateParts[0] != null && !"".equalsIgnoreCase(expDateParts[0]))
                expMM = expDateParts[0];
                if (expDateParts[1] != null && !"".equalsIgnoreCase(expDateParts[1]))
                expYYYY = expDateParts[1];
                sb.append("<input type=\"hidden\" name=\"card_expirationMonth\" value=\"" + expMM + "\">");
                sb.append("<input type=\"hidden\" name=\"card_expirationYear\" value=\"" + expYYYY + "\">");
                counter++;
            }
            out.println( sb );
            out.flush();
        }
        catch (CCRuntimeException e)
        {
            _logger.error( ExceptionUtils.getFullStackTrace( e ) );
            JSONStringer jsonErrors;
            try 
            {
                jsonErrors = new JSONStringer().object();
                jsonErrors.key( "error0" ).value( "reload" );
                sbJson = jsonErrors.endObject().toString();
            } 
            catch (JSONException e1) 
            {
                _logger.error( ExceptionUtils.getFullStackTrace( e1 ) );
                throw new CCRuntimeException( e1 );
            }
            sb.append( sbJson );
        }
*/
        return null;
    }

    protected void addError( HttpServletRequest r, String s )
    {
        ActionErrors errors = new ActionErrors();

        errors.add(
            ActionMessages.GLOBAL_MESSAGE,
            new ActionMessage( s )
        );
        r.setAttribute( Globals.ERROR_KEY, errors );
    }

    public ActionForward multiCurrencyRates( ActionMapping       mapping
                                           , ActionForm          form
                                           , HttpServletRequest  request
                                           , HttpServletResponse response )
    throws IOException
    {
        BasePaymentForm paymentForm = WebUtils.castForm( BasePaymentForm.class, form );

        response.setContentType( "text/html" );
        response.setCharacterEncoding( "UTF-8" );

        PrintWriter out = response.getWriter();

        String currencyType = request.getParameter( "currencyType" );
        String currencyAmount = request.getParameter( "totalAmountInUSD" ).trim();

        HttpSession session = request.getSession( true );
        session.setAttribute( WebConstants.SessionKeys.FUNDING_CURRENCY_TYPE, currencyType );

        double currencyRate;
        double currencyAmountDouble;
        double fundingCurrencyAmount;
        String fundingCurrencyAmountString = "";
        String currencyRateString = "";
        int currencyRateId;
        Date exchangeRateDate;

        UserInfoConsumerContext consumerCtx = new UserInfoConsumerContext();

        try
        {
            CurrencyRateInfo query = new CurrencyRateInfo();
            query.setIsoCode( currencyType );
            List<SortCriteria> sort = new ArrayList<SortCriteria>();
            SortCriteria sc = new SortCriteria( CurrencyRateInfoKeys.DATE_VALID );
            sc.setSequence(-1);
            sort.add(sc);

            List<CurrencyRateInfo> results = 
                ServiceLocator.getCyberSourceService()
                    .findCurrencyRates( consumerCtx, query, 0, 1, sort );

            if(results.size() == 1)
            {
                // now build the hidden fields currency type

                Iterator<CurrencyRateInfo> currencyRates = results.iterator();
                CurrencyRateInfo rateInfo = null;

                while (currencyRates.hasNext())
                {
                    rateInfo = currencyRates.next();
                    currencyRate = rateInfo.getRate();
                    currencyRateId = rateInfo.getCurrencyRateId();
                    exchangeRateDate = rateInfo.getDateValid();

                    if(currencyAmount.contains( "," ))
                    {
                        currencyAmount = currencyAmount.replace( ",", "" );
                    }
                    try 
                    {
                        currencyAmountDouble = Double.valueOf( currencyAmount.trim() ).doubleValue();
                    } 
                    catch (NumberFormatException nfe) 
                    {
                        throw new CCRuntimeException( nfe );
                    }
                    if(currencyType.equals( "JPY" ))
                    {
                        fundingCurrencyAmount = 
                            NumberTools.roundMoneyUp( 
                                NumberTools.multiplyAccurately( currencyAmountDouble, currencyRate ) 
                            );
                    }
                    else
                    {
                        fundingCurrencyAmount = 
                            NumberTools.roundMoney(
                                NumberTools.multiplyAccurately( currencyAmountDouble, currencyRate )
                            );
                    }
                    DecimalFormat df = new DecimalFormat("#####0.00");

                    paymentForm.setCurrencyRateId( currencyRateId );
                    fundingCurrencyAmountString = df.format( fundingCurrencyAmount );
                    currencyRateString = Double.toString( currencyRate );

                    session.setAttribute( WebConstants.SessionKeys.FUNDING_CURRENCY_AMOUNT, fundingCurrencyAmountString );
                    session.setAttribute( WebConstants.SessionKeys.FUNDING_CURRENCY_RATE_ID, Integer.valueOf( currencyRateId ) );
                    session.setAttribute( WebConstants.SessionKeys.EXCHANGE_RATE, currencyRateString );
                    session.setAttribute( WebConstants.SessionKeys.EXCHANGE_RATE_DATE, exchangeRateDate );

                    out.println( fundingCurrencyAmountString );
                    out.flush(); 
                }
            }
        }
        catch (CCRuntimeException e)
        {
            _logger.error( LogUtil.getStack( e ) );
            throw e;
        }
        return null; 
    }
 
	public abstract String getPaymentFormActionPath();	
}