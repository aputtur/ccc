package com.copyright.ccc.business.services.payment;

//import java.util.Map;
//import java.util.StringTokenizer;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import sun.misc.BASE64Encoder;

import com.copyright.base.config.AppServerConfiguration;
import com.copyright.ccc.CCRuntimeException;
import com.copyright.ccc.business.services.cart.CreditCardAuthorizationException;
import com.copyright.ccc.business.services.cart.CreditCardDetails;
import com.copyright.ccc.business.services.cart.CreditCardValidationException;
import com.copyright.ccc.business.services.user.User;
import com.copyright.ccc.config.CC2Configuration;
import com.copyright.ccc.config.CybersourceConfiguration;
import com.copyright.data.payment.AuthorizationException;

public class CyberSourceUtils
{
    private static final Logger _logger = Logger.getLogger(CyberSourceUtils.class);

    public static String getResponseUrlBase( HttpServletRequest request )
	{
		String scheme = "";
		String port = "";
		
		if ( AppServerConfiguration.isLOCAL() )
		{
			// LOCAL environments run on http, and need a port to be specified.
			scheme = "http";
			port = ":" + request.getServerPort() ;
		}
		else
		{
			// Workaround for the fact that request.getScheme() is returning "http"
			// regardless of whether the request came through the http or https port.
			// [dstine, mkolbert 11/9/10]
			scheme = 
				request.getLocalPort() == CC2Configuration.getInstance().getProtocolSwitchingHTTPPort()?
				"http" : "https";
		
			// Deployed environments do not need port to be specified.
		}
		
		String url = scheme + "://" + request.getServerName() + port;
		
		return url;
	}
	
    
    /**
     * @param map - Map containing fields that are to be signed.
     * Can only contain fields and values that should not be changed.
     * At the very minimum, map should contain 'amount', 'currency', and 'orderPage_transactionType'
     * if 'orderPage_transactionType' is 'subscription' or 'subscription_modify', the following are also required:
     * 'recurringSubscriptionInfo_amount', 'recurringSubscriptionInfo_numberOfPayments', 'recurringSubscriptionInfo_frequency',
     * 'recurringSubscriptionInfo_startDate', 'recurringSubscriptionInfo_automaticRenew'
     * if 'orderPage_transactionType' is 'subscription_modify' then 'paySubscriptionCreateReply_subscriptionID' is also required
     * @return html of hidden fields
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static String insertSignature(Map map) {
      if (map == null) {
        return "";
      }
      try {
        map.put("merchantID", getMerchantID());
        map.put("orderPage_timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("orderPage_version", "7");
        map.put("orderPage_serialNumber", getSerialNumber());
        Set keys = map.keySet();
        StringBuffer customFields = new StringBuffer();
        StringBuffer dataToSign = new StringBuffer();
        StringBuffer output = new StringBuffer();
        for (Iterator i = keys.iterator(); i.hasNext();) {
          String key = (String) i.next();
          customFields.append(key);
          dataToSign.append(key + "=" + String.valueOf(map.get(key)));
          if (i.hasNext()) {
            customFields.append(',');
            dataToSign.append(',');
          }

          output.append("<input type=\"hidden\" name=\"");
          output.append(key);
          output.append("\" value=\"");
          output.append(String.valueOf(map.get(key)));
          output.append("\">\n");
        }
        if(customFields.length() > 0) {
            dataToSign.append(',');
        }
        dataToSign.append("signedFieldsPublicSignature=");
        dataToSign.append(getPublicDigest(customFields.toString()).trim());
        output.append("<input type=\"hidden\" name=\"orderPage_signaturePublic\" value=\"" + getPublicDigest(dataToSign.toString()) + "\">\n");
        output.append("<input type=\"hidden\" name=\"orderPage_signedFields\" value=\"" + customFields.toString() + "\">\n");
        return output.toString();
      } catch (Exception e) {
    	  _logger.info( ExceptionUtils.getFullStackTrace( e ) );
			return "";
      }
    }
    
	public static String getPublicDigest(String customValues) throws Exception {
		String pub =getPublicKey();
		 BASE64Encoder encoder = new BASE64Encoder();
		Mac sha1Mac = Mac.getInstance("HmacSHA1");
		SecretKeySpec publicKeySpec = new SecretKeySpec(pub.getBytes(), "HmacSHA1");
		sha1Mac.init(publicKeySpec);
		byte[] publicBytes = sha1Mac.doFinal(customValues.getBytes());
		String publicDigest =  encoder.encodeBuffer(publicBytes);
		return publicDigest.replaceAll("\n", "").trim();
	}
	
	
	
	public static String insertSignature(String amount, String currency)
	{
		try 
		{
			if (amount == null)
 			{
   				 amount = "0.00";
			}
			if (currency == null)
			{
	 			currency = "usd";
			}
  			String time = String.valueOf(System.currentTimeMillis());
  			String merchantID = getMerchantID();
  			String data = merchantID + amount + currency + time;
  			String publicDigest=getPublicDigest(data);

  			String serialNumber = getSerialNumber();
  			
  			StringBuffer sb = new StringBuffer();
  			sb.append("<input type=\"hidden\" name=\"amount\" value=\"");
  			sb.append(amount);
  			sb.append("\">\n<input type=\"hidden\" name=\"currency\" value=\"");
  			sb.append(currency);
  			sb.append("\">\n<input type=\"hidden\" name=\"orderPage_timestamp\" value=\"");
  			sb.append(time);
  			sb.append("\">\n<input type=\"hidden\" name=\"merchantID\" value=\"");
  			sb.append(merchantID);
  			sb.append("\">\n<input type=\"hidden\" name=\"orderPage_signaturePublic\" value=\"");
  			sb.append(publicDigest);
  			sb.append("\">\n<input type=\"hidden\" name=\"orderPage_version\" value=\"4\">\n");
  			sb.append("<input type=\"hidden\" name=\"orderPage_serialNumber\" value=\"");
  			sb.append(serialNumber);
  			sb.append("\">\n");
  			return sb.toString();
		}
		catch (Exception e)
		{
			_logger.info( ExceptionUtils.getFullStackTrace( e ) );
  			return "";
		}
	}

	public static String insertSignature(String amount, String currency, String orderPage_transactionType)
	{
		try 
		{
  			if (amount == null) 
  			{
  				amount = "0.00";
		    }
  			if (currency == null) 
  			{
   				currency = "usd";
  			}
  			String serialNumber = getSerialNumber();
  			String time = String.valueOf(System.currentTimeMillis());
  			String merchantID = getMerchantID();
  			String data = merchantID + amount + currency + time + orderPage_transactionType;
  			String publicDigest=getPublicDigest(data);
  			StringBuffer sb = new StringBuffer();
  			sb.append("<input type=\"hidden\" name=\"amount\" value=\"");
  			sb.append(amount);
   			sb.append("\">\n<input type=\"hidden\" name=\"orderPage_transactionType\" value=\"");
  			sb.append(orderPage_transactionType);
  			sb.append("\">\n<input type=\"hidden\" name=\"currency\" value=\"");
  			sb.append(currency);
  			sb.append("\">\n<input type=\"hidden\" name=\"orderPage_timestamp\" value=\"");
  			sb.append(time);
  			sb.append("\">\n<input type=\"hidden\" name=\"merchantID\" value=\"");
  			sb.append(merchantID);
  			sb.append("\">\n<input type=\"hidden\" name=\"orderPage_signaturePublic\" value=\"");
  			sb.append(publicDigest);
  			sb.append("\">\n<input type=\"hidden\" name=\"orderPage_version\" value=\"4\">\n");
  			sb.append("<input type=\"hidden\" name=\"orderPage_serialNumber\" value=\"");
  			sb.append(serialNumber);
  			sb.append("\">\n");
  			return sb.toString();
	    } 
		catch (Exception e)
		{
			_logger.info( ExceptionUtils.getFullStackTrace( e ) );
	    	return "";
	    }
	}

	public static String insertSubscriptionSignature(String subscriptionAmount, 
			String subscriptionStartDate, String subscriptionFrequency,
            String subscriptionNumberOfPayments, String subscriptionAutomaticRenew)
	{

	    if (subscriptionFrequency == null)
	    {
  			return "";
	    }
		if (subscriptionAmount == null)
		{
			subscriptionAmount = "0.00";
	    }
		if (subscriptionStartDate == null)
		{
  			subscriptionStartDate = "00000000";
	    }
	    if (subscriptionNumberOfPayments == null)
	    {
		      subscriptionNumberOfPayments = "0";
		}
	    if (subscriptionAutomaticRenew == null)
	    {
  			subscriptionAutomaticRenew = "true";
	    }
	    try 
	    {
			String data = subscriptionAmount + subscriptionStartDate + subscriptionFrequency + subscriptionNumberOfPayments +
			subscriptionAutomaticRenew;
  			String pub = getPublicKey();
  			Mac sha1Mac = Mac.getInstance("HmacSHA1");
			SecretKeySpec publicKeySpec = new SecretKeySpec(pub.getBytes(), "HmacSHA1");
			sha1Mac.init(publicKeySpec);
			byte[] publicBytes = sha1Mac.doFinal(data.getBytes());
			String publicDigest = Base64.encodeBase64String(publicBytes);
			publicDigest = publicDigest.replaceAll("\n", "");
			StringBuffer sb = new StringBuffer();
			sb.append("<input type=\"hidden\" name=\"recurringSubscriptionInfo_amount\" value=\"");
			sb.append(subscriptionAmount);
			sb.append("\">\n<input type=\"hidden\" name=\"recurringSubscriptionInfo_numberOfPayments\" value=\"");
			sb.append(subscriptionNumberOfPayments);
			sb.append("\">\n<input type=\"hidden\" name=\"recurringSubscriptionInfo_frequency\" value=\"");
			sb.append(subscriptionFrequency);
			sb.append("\">\n<input type=\"hidden\" name=\"recurringSubscriptionInfo_automaticRenew\" value=\"");
			sb.append(subscriptionAutomaticRenew);
			sb.append("\">\n<input type=\"hidden\" name=\"recurringSubscriptionInfo_startDate\" value=\"");
			sb.append(subscriptionStartDate);
			sb.append("\">\n<input type=\"hidden\" name=\"card_cardType\" value=\"");
			sb.append("");
			sb.append("\">\n<input type=\"hidden\" name=\"card_expirationMonth\" value=\"");
			sb.append("");
			sb.append("\">\n<input type=\"hidden\" name=\"card_expirationYear\" value=\"");
			sb.append("");	  
			sb.append("\">\n<input type=\"hidden\" name=\"recurringSubscriptionInfo_signaturePublic\" value=\"");	  
			sb.append(publicDigest);
			sb.append("\">\n");
			return sb.toString();
	    } 
	    catch (Exception e)
	    {
			_logger.info( ExceptionUtils.getFullStackTrace( e ) );
			return "";
		}
	}
	

	public static String insertSubscriptionIDSignature(String subscriptionID)
	{
		if (subscriptionID == null) 
		{
  			return "";
		}
		try 
		{
			String publicDigest=getPublicDigest(subscriptionID);
  			StringBuffer sb = new StringBuffer();
  			sb.append("<input type=\"hidden\" id=\"subscriptionToEdit\" name=\"paySubscriptionCreateReply_subscriptionID\" value=\"" );
  			sb.append(subscriptionID);
  			sb.append("\">\n<input type=\"hidden\" name=\"paySubscriptionCreateReply_subscriptionIDPublicSignature\" value=\"");
  			sb.append(publicDigest);
  			sb.append("\">\n");
  			return sb.toString();
		}
		catch (Exception e) 
		{
			_logger.info( ExceptionUtils.getFullStackTrace( e ) );
  			return "";
	    }
	}
	public static String insertModifySubscriptionIDSignature(String subscriptionID)
	{
		if (subscriptionID == null) 
		{
  			return "";
		}
		try 
		{
			Map<String, String> signedFieldsMap = new HashMap<String, String>();
			
			signedFieldsMap.put("amount", "0.00");
			signedFieldsMap.put("currency", "usd");
			signedFieldsMap.put("orderPage_transactionType", "subscription_modify");
			signedFieldsMap.put("recurringSubscriptionInfo_amount", "0");
			signedFieldsMap.put("recurringSubscriptionInfo_numberOfPayments", "0");
			signedFieldsMap.put("recurringSubscriptionInfo_frequency", "on-demand");
			signedFieldsMap.put("recurringSubscriptionInfo_startDate", "20190731");
			signedFieldsMap.put("recurringSubscriptionInfo_automaticRenew", "false");
			signedFieldsMap.put("paySubscriptionCreateReply_subscriptionID", subscriptionID);
			
  			return insertSignature(signedFieldsMap);
		}
		catch (Exception e) 
		{
			_logger.info( ExceptionUtils.getFullStackTrace( e ) );
  			return "";
	    }
	}
/*	
	public static boolean verifySignature(String data, String signature)
	{
		if (data == null || signature == null)
		{
  			return false;
		}
		try 
		{
  			String pub = getPublicKey();
  			Mac sha1Mac = Mac.getInstance("HmacSHA1");
  			SecretKeySpec publicKeySpec = new SecretKeySpec(pub.getBytes(), "HmacSHA1");
  			sha1Mac.init(publicKeySpec);
  			byte[] publicBytes = sha1Mac.doFinal(data.getBytes());
  			String publicDigest = Base64.encodeBase64String(publicBytes);
  			publicDigest = publicDigest.replaceAll("[\r\n\t]", "");
  			return signature.equals(publicDigest);
		}
		catch (Exception e)
		{
			_logger.info( ExceptionUtils.getFullStackTrace( e ) );
  			return false;
		}
	}

	public static boolean verifyTransactionSignature(Map<String,String> map)
	{
		if (map == null)
		{
  			return false;
		}
		String transactionSignature = map.get("transactionSignature");
		if (transactionSignature == null)
		{
			return false;
		}
	    String transactionSignatureFields = map.get("signedFields");
		if (transactionSignatureFields == null)
		{
  			return false;
	    }
		StringTokenizer tokenizer = new StringTokenizer(transactionSignatureFields, ",", false);
		StringBuffer data = new StringBuffer();
		while (tokenizer.hasMoreTokens())
		{
  			data.append(map.get(tokenizer.nextToken()));
	    }
	    return verifySignature(data.toString(), transactionSignature);
	}
*/
    private static String getPublicKey()
    {
    	 String publicKey = "";
    	 try{
    		 publicKey = CybersourceConfiguration.getInstance().getPublicKey();
    	 }catch(Exception e){
    		_logger.error(ExceptionUtils.getFullStackTrace(e));
			throw new CCRuntimeException(e);
    	}
    	return publicKey;  
    }
   
    private static String getMerchantID()
    {
		String merchantID = "";
 		try{
	 		merchantID = CybersourceConfiguration.getInstance().getMerchantID();
		}catch(Exception e){
			_logger.error(ExceptionUtils.getFullStackTrace(e));
			throw new CCRuntimeException(e);
 		}
		return merchantID;
    }
   
    private static String getSerialNumber()
    {
		String serialNumber = "";
 		try{
			serialNumber = CybersourceConfiguration.getInstance().getSerialNumber();
		}catch(Exception e){
			_logger.error(ExceptionUtils.getFullStackTrace(e));
			throw new CCRuntimeException(e);
		}
		return serialNumber;
	}
	
    public static String getHopURL() {
		String hopURL = "";
		try{
			hopURL = CybersourceConfiguration.getInstance().getHopUrl();
		}catch(Exception e){
			_logger.error(ExceptionUtils.getFullStackTrace(e));
			throw new CCRuntimeException(e);
		}
		return hopURL;
	}
  
    public static String getResponseEmail() {
		String responseEmail = "";
		try{
			responseEmail = CybersourceConfiguration.getInstance().getSubscriptionTxResponseEmail();
		}catch(Exception e){
			_logger.error(ExceptionUtils.getFullStackTrace(e));
			throw new CCRuntimeException(e);
		}
		return responseEmail;
	}
    
    
	public static boolean isCreditCardExpired(String expiryDate)
    {
		String expDateYear = expiryDate.substring(3);
		String expDateMonth = expiryDate.substring(0,2);
		int year = Integer.valueOf(expDateYear);
		int month = Integer.valueOf(expDateMonth);
		int daysInM = daysInMonth(month, year);

		Calendar expCal = Calendar.getInstance();
		expCal.set(Calendar.YEAR, year);
		expCal.set(Calendar.MONTH, month - 1);
		expCal.set(Calendar.DAY_OF_MONTH, daysInM);
		expCal.set(Calendar.HOUR_OF_DAY, 23);
		expCal.set(Calendar.MINUTE, 59);
		expCal.set(Calendar.SECOND, 59);
		
		Date expDate = expCal.getTime();
		Date today = new Date();
		if (today.after(expDate))
		{
			return true;
		}
		else
		{
			return false;
		}
    }
    
    private static int daysInMonth(int month, int year)
    {
    	Integer[] m = {31,28,31,30,31,30,31,31,30,31,30,31};
    	
    	if (month != 2)
    	{
    		return m[month - 1];
    	}
    	
    	// the February case...
    	if (year%4 != 0)
    	{
    		return m[1];
    	}
    	if (year%100 == 0 && year%400 != 0)
    	{
    		return m[1];
    	}
    	
    	return m[1] + 1;
    }
    
    public static PaymentResponse authorizeCreditCard(CreditCardDetails creditCardDetails,PaymentRequest paymentRequest) {
    	PaymentResponse paymentResponse=null;
    	try 
    	{
    		paymentResponse = CreditCardPaymentService.authorizeCreditCard(paymentRequest);
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
       	if(paymentResponse != null && paymentResponse.getAuthSuccess())
		{
			creditCardDetails.setAuthRequestId(paymentResponse.getAuthRequestID());
			creditCardDetails.setAuthRequestToken(paymentResponse.getAuthRequestToken());
			creditCardDetails.setAuthStatus(paymentResponse.getAuthStatus());
			creditCardDetails.setAuthSuccess(paymentResponse.getAuthSuccess());

			creditCardDetails.setCcAuthNo(paymentRequest.getAuthCode());
			creditCardDetails.setCcTrxDate(new Date());
			creditCardDetails.setPurchaseDate(new Date());
			creditCardDetails.setReconciliationID(paymentResponse.getReconciliationID());
			creditCardDetails.setMerchantRefId(paymentResponse.getMerchantRefId());
			creditCardDetails.setCurrencyType(paymentRequest.getCurrencyCode());
		}
       	
       	return paymentResponse;
    }
    
    public static PaymentResponse chargeCreditCard(CreditCardDetails creditCardDetails,PaymentResponse paymentResponse, PaymentRequest paymentRequest) {
	    try {
			paymentResponse = CreditCardPaymentService.chargeCreditCard(paymentResponse, paymentRequest);
			
			creditCardDetails.setCcAuthNo(paymentRequest.getAuthCode());
			creditCardDetails.setCcTrxDate(new Date());
			creditCardDetails.setPurchaseDate(new Date());
			creditCardDetails.setReconciliationID(paymentResponse.getReconciliationID());
			creditCardDetails.setMerchantRefId(paymentResponse.getMerchantRefId());
			creditCardDetails.setCurrencyType(paymentRequest.getCurrencyCode());
			creditCardDetails.setRequestId(paymentResponse.getRequestID());
    			try {
    				BigDecimal requestIdBigDecimal = new BigDecimal(paymentResponse.getRequestID().trim());
    		        creditCardDetails.setCcTrxId(requestIdBigDecimal);
    		    }catch (NumberFormatException nfe) {
    		    _logger.error( ExceptionUtils.getFullStackTrace(nfe)) ;
    		    	throw new CCRuntimeException(nfe);
    		    } 
			creditCardDetails.setRequestToken(paymentResponse.getRequestToken());
			creditCardDetails.setStatus(paymentResponse.getStatus());
			creditCardDetails.setSuccess(paymentResponse.getSuccess());
			
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
	       	return paymentResponse;
    }
    
    public static CreditCardDetails updateCreditCardDetails(PaymentRequest paymentRequest,PaymentResponse paymentResponse,CreditCardDetails creditCardDetails) {
    	creditCardDetails.setCcAuthNo(paymentRequest.getAuthCode());
		creditCardDetails.setCcTrxDate(new Date());
		creditCardDetails.setPurchaseDate(new Date());
		creditCardDetails.setReconciliationID(paymentResponse.getReconciliationID());
		creditCardDetails.setMerchantRefId(paymentResponse.getMerchantRefId());
		creditCardDetails.setCurrencyType(paymentRequest.getCurrencyCode());
		creditCardDetails.setRequestId(paymentResponse.getRequestID());
			try {
				BigDecimal requestIdBigDecimal = new BigDecimal(paymentResponse.getRequestID().trim());
		        creditCardDetails.setCcTrxId(requestIdBigDecimal);
		    }catch (NumberFormatException nfe) {
		    _logger.error( ExceptionUtils.getFullStackTrace(nfe)) ;
		    	throw new CCRuntimeException(nfe);
		    } 
		creditCardDetails.setRequestToken(paymentResponse.getRequestToken());
		creditCardDetails.setStatus(paymentResponse.getStatus());
		creditCardDetails.setSuccess(paymentResponse.getSuccess());
		return creditCardDetails;
    }
    
    public static PaymentRequest setUserInfoOnPaymentRequest(User cccUser, PaymentRequest paymentRequest )
	{
		String firstName = null;
		String lastName = null;
		String userid = null;
		long acctNum =-1 ;
		long CCCacctNum =-1;

		InternetAddress email = cccUser.getEmailAddress();

		if ( email != null)
		{
			userid = email.getAddress();
		}
		else
		{
			userid = "";
		}

		firstName = cccUser.getFirstName();
		lastName = cccUser.getLastName();
		acctNum = cccUser.getAccountNumber();
		CCCacctNum = cccUser.getAccountNumber();
		String fundingCurrencyAmount = paymentRequest.getAmount();
		paymentRequest.setFundingAmount(fundingCurrencyAmount);
		paymentRequest.setUserID(userid);
		paymentRequest.setAcctNum(acctNum);
		paymentRequest.setCCCacctNum(CCCacctNum);
		paymentRequest.setFirstName(firstName);
		paymentRequest.setLastName(lastName);
		paymentRequest.setEmailAddress(userid);
		return paymentRequest;
	}
    
    public static PaymentRequest getPaymentRequest(User cccUser,CreditCardDetails creditCardDetails) {
		String orderAmountString="0";
		if (creditCardDetails.getCurrencyPaidTotal() != null) {
		BigDecimal amount = creditCardDetails.getCurrencyPaidTotal().setScale( 2, BigDecimal.ROUND_HALF_EVEN );
		 orderAmountString = amount.toString().trim();
		}
		
		
    	
    	String paymentProfileCccIdString = null;
    	if (creditCardDetails.getPaymentProfileCccId() != null) {
    		long cccProfileId = creditCardDetails.getPaymentProfileCccId().longValue();
    		paymentProfileCccIdString = String.valueOf(cccProfileId);
    	}
		
    	 PaymentRequest paymentRequest=  new PaymentRequest(orderAmountString,creditCardDetails.getPayment().getPaymentId().toString(), creditCardDetails.getCurrencyType(),creditCardDetails.getCardHolderName(),
				creditCardDetails.getCardType(),creditCardDetails.getPaymentProfileIdentifier(),
				paymentProfileCccIdString,creditCardDetails.getCardExpirationDate(),creditCardDetails.getLastFourCc(),creditCardDetails.getMerchantRefId(),creditCardDetails.getExchangeRate().doubleValue());
		
		
		return setUserInfoOnPaymentRequest( cccUser, paymentRequest );
		
		
	}

}
