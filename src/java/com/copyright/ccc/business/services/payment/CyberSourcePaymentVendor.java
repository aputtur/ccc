/**
  *   This class interfaces directly with the Cybersource  API.  It sends transactions to the tellan
  *   server which provides responses.
  *
  *	 
*/

package com.copyright.ccc.business.services.payment;

import java.io.StringWriter;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.copyright.ccc.CCRuntimeException;
import com.copyright.ccc.config.CC2Configuration;
import com.copyright.ccc.config.CybersourceConfiguration;
import com.copyright.ccc.util.LogUtil;
import com.copyright.ccc.xml.XMLTools;
import com.cybersource.ws.client.ClientException;
import com.cybersource.ws.client.FaultException;
import com.cybersource.ws.client.Utility;
import com.cybersource.ws.client.XMLClient;

public class CyberSourcePaymentVendor extends PaymentVendor
	implements CyberSourceRequestInterface, CyberSourceResponseInterface
{
	private final String LINE_SEPARATOR = System.getProperty("line.separator");
	private static final String VISA_DISPLAY = "Visa";
	private static final String MASTERCARD_DISPLAY = "Master Card";
	private static final String AMERICAN_EXPRESS_DISPLAY = "American Express";
	private static final String VISA_CODE = "001";
	private static final String MASTERCARD_CODE = "002";
	private static final String AMERICAN_EXPRESS_CODE = "003";
	private PaymentResponse paymentResponse = null;
	private static Logger _logger = Logger.getLogger(CyberSourcePaymentVendor.class );
	
	// defaults for Cybersource transactions # of retries and delay between retries
	private static int retriesCount = 0;
	private static int retriesDelayMillis = 1000;
	
	public CyberSourcePaymentVendor()
	{
		setConfigurationItems();
	}
	
	public CyberSourcePaymentVendor(PaymentRequest paymentRequest)
	{
		this.paymentRequest = paymentRequest;
		setConfigurationItems();
	}

	public static String toCardTypeName(String cardType_code) {
		if (cardType_code != null) {
			if (cardType_code.equalsIgnoreCase(VISA_CODE))
				return VISA_DISPLAY;
			else if (cardType_code.equalsIgnoreCase(MASTERCARD_CODE))
				return MASTERCARD_DISPLAY;
			else if (cardType_code.equalsIgnoreCase(AMERICAN_EXPRESS_CODE))
				return AMERICAN_EXPRESS_DISPLAY;
		}
		return "";
	}
	
	public static String toCardTypeCode(String cardType_name) {
		if (cardType_name != null) {
			if (cardType_name.equalsIgnoreCase(VISA_DISPLAY))
				return VISA_CODE;
			else if (cardType_name.equalsIgnoreCase(MASTERCARD_DISPLAY))
				return MASTERCARD_CODE;
			else if (cardType_name.equalsIgnoreCase(AMERICAN_EXPRESS_DISPLAY))
				return AMERICAN_EXPRESS_CODE;
		}
		return "";
	}
	
	@Override
	public PaymentResponse preAuthorize()
	{
   		Document request = null;

   		CyberSourceRequest csr = new CyberSourceRequest(paymentRequest);
			
		csr.setReferenceCode(String.valueOf(paymentRequest.getMerchantRefID()));
		csr.setField1(String.valueOf(paymentRequest.getPaymentId()));
		csr.setField2(CybersourceConfiguration.getInstance().getEnvironment());
		csr.setField3(paymentRequest.getFundingAmount());
		csr.setField4(String.valueOf(paymentRequest.getCCCacctNum()));
				
		String nameSpaceURI;
		try {
			nameSpaceURI = XMLClient.getEffectiveNamespaceURI(
					CybersourceConfiguration.getInstance().getProperties(), null);
		} catch (ClientException ce) {
			_logger.error(LogUtil.getStack(ce));
			throw new CCRuntimeException(ce);
		}
					
		if (paymentRequest instanceof InvoicePaymentRequest){
			request = csr.buildAuthorizationReceiptRequest(nameSpaceURI);
		}else{
			request = csr.buildAuthorizationRequest(nameSpaceURI);
			
		}

		if ( _logger.isDebugEnabled() )
		{
			_logger.debug("Authorization Request " + xmlToString(request));
		}
   		paymentResponse = new PaymentResponse();
		Document reply = null;
		try
		{
			reply = runTransaction(request);
		}
		catch(CCRuntimeException ccre)
		{
			paymentResponse.setStatus(CyberSourceCreditCardResponses.TRANSACTION_EXCEPTION + ": " + ccre.getMessage());
			paymentResponse.setSuccess(false);
		}
		
		if ( _logger.isDebugEnabled() )
		{
			_logger.debug("Authorization Response " + xmlToString(reply));
		}
		
		if(reply != null)
		{
	    	Element rootElement = reply.getDocumentElement();
	    	paymentResponse.setAuthRequestID(XMLTools.getTagValue(rootElement, REQUEST_ID_TAG));
	    	String status = XMLTools.getTagValue(rootElement, REASON_CODE_TAG);
	    	paymentResponse.setAuthStatus(status);
	   		paymentResponse.setAuthSuccess(checkSuccessAndNotify(status));
	    	paymentResponse.setAuthRequestToken(XMLTools.getTagValue(rootElement, REQUEST_TOKEN_TAG));
	    	Element tempElement = XMLTools.getElement(rootElement, AUTH_REPLY_TAG);
	    	paymentResponse.setAuthNum(XMLTools.getTagValue(tempElement, AUTHORIZATION_CODE_TAG));
		}
		paymentResponse.setCardType(paymentRequest.getCardType());
		paymentResponse.setMerchantRefId(paymentRequest.getMerchantRefID());
    	paymentRequest.setAuthCode(paymentResponse.getAuthNum());
   		return paymentResponse;
	}
	
	@Override
	public PaymentResponse credit()
	{
		_logger.info("\nCyberSourcePayment.credit() BEGIN\n");
   		Document request = null;
   		CyberSourceRequest csr = new CyberSourceRequest(paymentRequest);
   		csr.setReferenceCode(String.valueOf(paymentRequest.getMerchantRefID()));
   		csr.setField1(String.valueOf(paymentRequest.getPaymentId()));
   		csr.setField2(String.valueOf(paymentRequest.getRequestID()));
   		csr.setField3(String.valueOf(paymentRequest.getFundingAmount()));
   		csr.setField4(paymentRequest.getCreditMemoNumber() + "/" + CybersourceConfiguration.getInstance().getEnvironment());
   		String nameSpaceURI;
		try {
			nameSpaceURI = XMLClient.getEffectiveNamespaceURI(
					CybersourceConfiguration.getInstance().getProperties(), null);
		} catch (ClientException ce) {
			_logger.debug("CyberSourcePaymentVendor: Unable to build sales request document." + LogUtil.appendableStack(ce));
			throw new CCRuntimeException(ce);
		}
		request = csr.buildCreditRequest(nameSpaceURI);
		paymentResponse = new PaymentResponse();
		Document reply = null;
		_logger.info("\nCyberSourcePayment.credit() PERFORM CYBERSOURCE REQUEST\n");
		try
		{
			reply = runTransaction(request);
		}
		catch(CCRuntimeException ccre)
		{
			paymentResponse.setStatus(CyberSourceCreditCardResponses.TRANSACTION_EXCEPTION + ": " + ccre.getMessage());
			paymentResponse.setSuccess(false);
		}
		if(reply != null)
		{
	    	Element rootElement = reply.getDocumentElement();
	    	paymentResponse.setRequestID(XMLTools.getTagValue(rootElement, REQUEST_ID_TAG));
	    	String status = XMLTools.getTagValue(rootElement, REASON_CODE_TAG);
	    	paymentResponse.setStatus(status);
	   		paymentResponse.setSuccess(checkSuccessAndNotify(status));
	    	paymentResponse.setRequestToken(XMLTools.getTagValue(rootElement, REQUEST_TOKEN_TAG));
	    	Element tempElement = XMLTools.getElement(rootElement, CREDIT_REPLY_TAG);
	    	paymentResponse.setReconciliationID(XMLTools.getTagValue(tempElement, RECONCILIATION_ID_TAG));
		}
		_logger.info("\nCyberSourcePayment.credit() END\n");

    	return paymentResponse;
 	}
	
	
	@Override
	public PaymentResponse charge()
	{
  		Document request = null;
		CyberSourceRequest csr = new CyberSourceRequest(paymentRequest);
		csr.setReferenceCode(String.valueOf(paymentRequest.getMerchantRefID()));
		csr.setField1(String.valueOf(paymentRequest.getPaymentId()));
		csr.setField2(CybersourceConfiguration.getInstance().getEnvironment());
		csr.setField3(String.valueOf(paymentRequest.getFundingAmount()));
		csr.setField4(String.valueOf(paymentRequest.getCCCacctNum()));
		String nameSpaceURI;
		try {
			nameSpaceURI = XMLClient.getEffectiveNamespaceURI(
					CybersourceConfiguration.getInstance().getProperties(), null);
		} catch (ClientException ce) {
			_logger.error(LogUtil.getStack(ce));
			throw new CCRuntimeException(ce);
		}
		
		if (paymentRequest instanceof InvoicePaymentRequest)
		{
			request = 	csr.buildSalesReceiptRequest(nameSpaceURI);
		}
		else
		{
			request = csr.buildSalesRequest(nameSpaceURI);
		}

		if(paymentResponse == null)
		{
			paymentResponse = new PaymentResponse();
		}
		Document reply = null;
		try
		{
			reply = runTransaction(request);
		}
		catch(CCRuntimeException ccre)
		{
			paymentResponse.setStatus(CyberSourceCreditCardResponses.TRANSACTION_EXCEPTION + ": " + ccre.getMessage());
			paymentResponse.setSuccess(false);
		}
   		paymentResponse.setCardType(paymentRequest.getCardType());
   		
		if(reply != null)
		{
	    	Element rootElement = reply.getDocumentElement();
	    	paymentResponse.setRequestID(XMLTools.getTagValue(rootElement, REQUEST_ID_TAG));
	    	String status = XMLTools.getTagValue(rootElement, REASON_CODE_TAG);
	    	paymentResponse.setStatus(status);
	    	paymentResponse.setSuccess(checkSuccessAndNotify(status));
	    	paymentResponse.setRequestToken(XMLTools.getTagValue(rootElement, REQUEST_TOKEN_TAG));
	    	
	    	if ( CybersourceConfiguration.getInstance().isPROD() )
	    	{
	    		// Authorization is only available when CyberSource interface is in live mode
	        	Element authElement = XMLTools.getElement(rootElement, AUTH_REPLY_TAG);
		    	paymentResponse.setAuthNum(XMLTools.getTagValue(authElement, AUTHORIZATION_CODE_TAG));
	    	}
	    	
	    	Element replyElement = XMLTools.getElement(rootElement, CAPTURE_REPLY_TAG);
	    	paymentResponse.setReconciliationID(XMLTools.getTagValue(replyElement, RECONCILIATION_ID_TAG));
	    	paymentResponse.setAuthDate(XMLTools.getTagValue(replyElement, AUTHDATETIME_TAG));
		}
    	paymentResponse.setMerchantRefId(paymentRequest.getMerchantRefID());
    	paymentResponse.setFundingCurrency(paymentRequest.getCurrencyCode());
    	paymentResponse.setCccProfileId(paymentRequest.getCccPaymentProfileID());
		
    	return paymentResponse;
	}
	
	private boolean checkSuccessAndNotify(String status)
    {
    	if (CyberSourceCreditCardResponses.notifyOnError(status))
    	{
    		logTransactionError(CyberSourceCreditCardResponses.getErrorMessage(status));
    	}
    	return checkSuccess(status);
    }
	
	private void logTransactionError(String message)
    {    	
    	StringBuffer transactionErrorMessage = new StringBuffer("CyberSource Error: ");
    	transactionErrorMessage.append(message).append(LINE_SEPARATOR);
    	if (paymentRequest != null)
    	{
        	transactionErrorMessage.append("----------PaymentRequest: ").append(LINE_SEPARATOR);
        	transactionErrorMessage.append(paymentRequest.toString());
    	}
    	if (paymentResponse != null)
    	{
        	transactionErrorMessage.append("----------PaymentResponse: ").append(LINE_SEPARATOR);
        	transactionErrorMessage.append(paymentResponse.toString());
    	}

		_logger.info("Credit Card Processing Failure Notification.\n");
		_logger.info(transactionErrorMessage);
    }
	
	private boolean checkSuccess(String status)
    {
        return CyberSourceCreditCardResponses.SUCCESS.equals(status) || 
        		CybersourceConfiguration.getInstance().getIgnoreErrors();    	
    }
	
	public static String xmlToString(Node node)
	{
		try
		{
			Source source = new DOMSource(node);
			StringWriter stringWriter = new StringWriter();
			Result result = new StreamResult(stringWriter);
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer();
			transformer.transform(source, result);
			return stringWriter.getBuffer().toString();
		}
		catch (TransformerConfigurationException tce)
		{
			_logger.error(LogUtil.getStack(tce));
			throw new CCRuntimeException( tce );
		}
		catch (TransformerException te)
		{
			_logger.error(LogUtil.getStack(te));
			throw new CCRuntimeException( te );
		}
	}
	 
	private Document runTransaction(Document request) throws CCRuntimeException
    {
		displayDocument("CREDIT CARD AUTHORIZATION REQUEST:", request);
		Document reply = null;
		
		/*
		 * Retry transaction up to MAX_RETRIES times
		 */
		for(int retry = 0; retry <= retriesCount; )
		{
			try 
			{
				reply = XMLClient.runTransaction(request, 
						CybersourceConfiguration.getInstance().getProperties());
			} 
			catch ( FaultException fe ) 
			{
				_logger.error(LogUtil.getStack(fe));
				throw new CCRuntimeException( fe );
			} 
			catch ( ClientException ce ) 
			{
				if(retry++ < retriesCount)
				{
					_logger.warn("CyberSourcePaymentVendor.runTransaction(): retrying transaction " + retry + " time(s):");
					_logger.warn("Reason: " + ce.getLogString());
					_logger.warn("Critical: " + ce.isCritical());
				    _logger.warn(Utility.nodeToString(request));
				    try
				    {
						Thread.sleep(retriesDelayMillis);
					} 
				    catch (InterruptedException e)
				    {
					    _logger.error("runTransaction(): " + LogUtil.appendableStack(e));
					}
					continue;
				}
				throw new CCRuntimeException( ce );
			}
			break;
		}
		
		displayDocument("CREDIT CARD AUTHORIZATION REPLY:", reply);
		return reply;
    }
	 
	/**
	 * Displays the content of the Document object.
	 *
	 * @param header	Header text.
	 * @param doc		Document object to display.
	 */
    private static void displayDocument(String header, Document doc)
    {
	    _logger.debug(header);
	    _logger.debug(Utility.nodeToString(doc));
    }
    
    private static void setConfigurationItems()
    {
		try
		{
			retriesCount = CC2Configuration.getInstance().getCybersourceRetriesCount();
			retriesDelayMillis = CC2Configuration.getInstance().getCybersourceRetriesDelayMillis();
		}
		catch(NumberFormatException nfe)
		{
			_logger.error("setConfigurationItems(): " + LogUtil.appendableStack(nfe));
		}
    }
}

