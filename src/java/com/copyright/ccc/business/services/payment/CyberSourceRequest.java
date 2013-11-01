package com.copyright.ccc.business.services.payment;

import java.io.Serializable;

import org.w3c.dom.Document;

import com.copyright.ccc.xml.XMLTools;

/** This class indirectly uses DocumentBuilderFactory and DocumentBuilder).
 */
public class CyberSourceRequest implements CyberSourceRequestInterface, Serializable
{
	private static final long serialVersionUID = 1L;
	//private String MERCHANT_REFERENCE_PREFIX = "RLNK";
	private PaymentRequest paymentRequest = null; 

	private String referenceCode =  null;
	private String field1 = null;
	private String field2 = null;
	private String field3 = null;
	private String field4 = null;
	
	private StringBuffer _request = null;
	
	public CyberSourceRequest(PaymentRequest paymentRequest)
	{
		this.paymentRequest = paymentRequest;
	}
	
	public PaymentRequest getPaymentRequest()
	{
		return paymentRequest;
	}

	public Document buildAuthorizationRequest(String nameSpaceURI)
	{
		buildTypicalRequest(nameSpaceURI);
		XMLTools.appendElementWithAttribute(_request, CC_AUTH_SERVICE, RUN, TRUE);
		appendBusinessRulesElement();
	    endRequest();
        return XMLTools.buildDocument(_request);
	}
	
	public Document buildAuthorizationReceiptRequest(String nameSpaceURI)
	{
		buildTypicalReceiptRequest(nameSpaceURI);
		XMLTools.appendElementWithAttribute(_request, CC_AUTH_SERVICE, RUN, TRUE);
		appendBusinessRulesElement();
	    endRequest();
        return XMLTools.buildDocument(_request);
	}
	
	
	public Document buildSalesReceiptRequest(String nameSpaceURI)
	{
		buildTypicalReceiptRequest(nameSpaceURI);
		//XMLTools.appendElementWithAttribute(_request, CC_AUTH_SERVICE, RUN, TRUE);
		XMLTools.startTagWithAttribute(_request, CC_CAPTURE_SERVICE, RUN, TRUE);
		XMLTools.appendTextElement(_request, AUTH_REQUEST_ID, this.getRequestID());
		//XMLTools.appendTextElement(_request, AUTH_REQUEST_TOKEN, this.getRequestToken());
		XMLTools.endTag(_request, CC_CAPTURE_SERVICE);
		appendBusinessRulesElement();
	    endRequest();
		return XMLTools.buildDocument(_request);
	}
	
	
	
	public Document buildSalesRequest(String nameSpaceURI)
	{
		buildTypicalRequest(nameSpaceURI);
		//XMLTools.appendElementWithAttribute(_request, CC_AUTH_SERVICE, RUN, TRUE);
		XMLTools.startTagWithAttribute(_request, CC_CAPTURE_SERVICE, RUN, TRUE);
		XMLTools.appendTextElement(_request, AUTH_REQUEST_ID, this.getRequestID());
		//XMLTools.appendTextElement(_request, AUTH_REQUEST_TOKEN, this.getRequestToken());
		XMLTools.endTag(_request, CC_CAPTURE_SERVICE);
		appendBusinessRulesElement();
	    endRequest();
		return XMLTools.buildDocument(_request);
	}
	
	public Document buildAuthandSalesRequest(String nameSpaceURI)
	{
		buildTypicalRequest(nameSpaceURI);
		XMLTools.appendElementWithAttribute(_request, CC_AUTH_SERVICE, RUN, TRUE);
		XMLTools.appendElementWithAttribute(_request, CC_CAPTURE_SERVICE, RUN, TRUE);
		appendBusinessRulesElement();
	    endRequest();
		return XMLTools.buildDocument(_request);
	}
	
	/* Started void request development, but abandoned because global payment network does not 
	 * support void transactions. May pick up again, if we need to void domestic transactions for some reason
	 
	public Document buildVoidRequest(String nameSpaceURI)
	{
		buildFollowOnCreditRequest(nameSpaceURI);
		XMLTools.startTagWithAttribute(_request, VOID_SERVICE, RUN, TRUE);
		XMLTools.appendTextElement(_request, "voidRequestID", getRequestID());
		//XMLTools.appendTextElement(_request, "orderRequestToken", getRequestToken());
		XMLTools.endTag(_request, VOID_SERVICE);
	    endRequest();
		return XMLTools.buildDocument(_request);
	} */
	

	public Document buildCreditRequest(String nameSpaceURI)
	{
		if (paymentRequest.isFollowOnCredit())
		{
			buildFollowOnCreditRequest(nameSpaceURI);
			XMLTools.startTagWithAttribute(_request, CC_CREDIT_SERVICE, RUN, TRUE);
			XMLTools.appendTextElement(_request, CAPTURE_REQUEST_ID, getRequestID());
//			XMLTools.appendTextElement(_request, CAPTURE_REQUEST_TOKEN, getRequestToken());
			XMLTools.endTag(_request, CC_CREDIT_SERVICE);
		}
		else
		{
			buildTypicalRequest(nameSpaceURI);			
			XMLTools.startTagWithAttribute(_request, CC_CREDIT_SERVICE, RUN, TRUE);
			if (getCybersourceProfileID() == null || "".equals( getCybersourceProfileID() )) {
				XMLTools.appendTextElement(_request, CAPTURE_REQUEST_ID, getRequestID());
			}
			XMLTools.endTag(_request, CC_CREDIT_SERVICE);
		}
	    endRequest();
		return XMLTools.buildDocument(_request);
	}
	
	private void buildTypicalRequest(String nameSpaceURI)
	{
		beginRequest(nameSpaceURI);
		XMLTools.appendTextElement(_request, MERCHANT_REFERENCE_CODE,getReferenceCode());
		//appendBillToElement();
		appendPurchaseTotalsElement();

		if (getCybersourceProfileID() != null && !"".equals( getCybersourceProfileID() )) {
			appendPaymentProfileElement();
		}
		appendMerchantDefinedElement();
	}
	
	private void buildTypicalReceiptRequest(String nameSpaceURI)
	{
		beginRequest(nameSpaceURI);
		  //No need to prepend RLNK for receipts since they are already prefixed by ARService
		XMLTools.appendTextElement(_request, MERCHANT_REFERENCE_CODE,  getReferenceCode());
		//appendBillToElement();
		appendPurchaseTotalsElement();
		appendPaymentProfileElement();
		appendMerchantDefinedElement();
	}
	
	
	
	private void buildFollowOnCreditRequest(String nameSpaceURI)
	{
		beginRequest(nameSpaceURI);
		XMLTools.appendTextElement(_request, MERCHANT_REFERENCE_CODE,getReferenceCode());
		appendPurchaseTotalsElement();
		appendMerchantDefinedElement();
	}
	
	private void beginRequest(String nameSpaceURI)
	{
		_request = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		_request.append("<requestMessage xmlns=\"urn:schemas-cybersource-com:transaction-data-1.21\" " +
				"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
				"xsi:schemaLocation=\"urn:schemas-cybersource-com:transaction-data-1.21 " +
				"https://ics2ws.ic3.com/commerce/1.x/transactionProcessor/CyberSourceTransaction_1.21.xsd\">");
		//nameSpaceURI = "urn:schemas-cybersource-com:transaction-data-1.42";
		//XMLTools.startTagWithAttribute(_request, REQUEST_MESSAGE, XMLNS, nameSpaceURI);
	}
	
	private void endRequest()
	{
		XMLTools.endTag(_request, REQUEST_MESSAGE);
	}
		
	private void appendBillToElement()
	{
		XMLTools.startTag(_request, BILL_TO);
		XMLTools.appendTextElement(_request, FIRST_NAME, getFirstName());
		XMLTools.appendTextElement(_request, LAST_NAME, getLastName());
		XMLTools.appendTextElement(_request, STREET1, getStreetAddress1());
		XMLTools.appendTextElement(_request, CITY, getCity());
		XMLTools.appendTextElement(_request, STATE, getState());
		XMLTools.appendTextElement(_request, POSTAL_CODE, getPostalCode());
		XMLTools.appendTextElement(_request, COUNTRY, getCountry());
		XMLTools.appendTextElement(_request, EMAIL, getEmail());
		XMLTools.endTag(_request, BILL_TO);
	}
	
	private void appendPurchaseTotalsElement()
	{
		XMLTools.startTag(_request, PURCHASE_TOTALS);
		XMLTools.appendTextElement(_request, CURRENCY, getCurrency());
		XMLTools.appendTextElement(_request, GRAND_TOTAL_AMOUNT, this.getGrandTotalAmount());
		XMLTools.endTag(_request, PURCHASE_TOTALS);
	}
	
	private void appendBusinessRulesElement()
	{
		XMLTools.startTag(_request, BUSINESS_RULES);
		XMLTools.appendTextElement(_request, IGNORE_AVS_RESULT, "true");
		XMLTools.endTag(_request, BUSINESS_RULES);
	}
	
	/* This method is no longer being supported as we will use subrciption ids stored
	 * on cybersource instead of passing/storing locally encrcypted creditcard data
	 * @deprecated
	 * @since PubDec08
	 
	private void appendCardElement()
	{
		XMLTools.startTag(_request, CARD);
		XMLTools.appendTextElement(_request, ACCOUNT_NUMBER, getCardNumber());
		XMLTools.appendTextElement(_request, EXPIRATION_MONTH, getExpirationMonth());
		XMLTools.appendTextElement(_request, EXPIRATION_YEAR, getExpirationYear());
		XMLTools.endTag(_request, CARD);
	}*/
	
	private void appendPaymentProfileElement()
	{
		XMLTools.startTag(_request, SUBSCRIPTION);
		XMLTools.appendTextElement(_request, SUBSCRIPTION_ID, getCybersourceProfileID());
		XMLTools.endTag(_request, SUBSCRIPTION);
	}
	
	
	private void appendMerchantDefinedElement()
	{
		XMLTools.startTag(_request, MERCHANT_DEFINED_DATA);
		XMLTools.appendTextElement(_request, FIELD1, getField1());
		XMLTools.appendTextElement(_request, FIELD2, getField2());
		XMLTools.appendTextElement(_request, FIELD3, getField3());
		XMLTools.appendTextElement(_request, FIELD4, getField4());
		XMLTools.endTag(_request, MERCHANT_DEFINED_DATA);
	}
		
	
	/**
	 * @param referenceCode The referenceCode to set.
	 */
	public void setReferenceCode(String referenceCode) {
		this.referenceCode = referenceCode;
	}
	/**
	 * @return Returns the referenceCode.
	 */
	private String getReferenceCode()
	{
		return XMLTools.encodeAmpersands(referenceCode);
	}
	/**
	 * @return Returns the firstName.
	 */
	private String getFirstName() {
		return XMLTools.encodeAmpersands(paymentRequest.getFirstName());
	}
	/**
	 * @return Returns the lastName.
	 */
	private String getLastName() {
		return XMLTools.encodeAmpersands(paymentRequest.getLastName());
	}
	/**
	 * @return Returns the streetAddress1.
	 */
	private String getStreetAddress1() {
		return XMLTools.encodeAmpersands(paymentRequest.getStreetAddress1());
	}
	/**
	 * @return Returns the city.
	 */
	private String getCity() {
		return XMLTools.encodeAmpersands(paymentRequest.getCity());
	}
	/**
	 * @return Returns the state.
	 */
	private String getState() {
		return XMLTools.encodeAmpersands(paymentRequest.getState());
	}
	/**
	 * @return Returns the postalCode.
	 */
	private String getPostalCode() {
		return XMLTools.encodeAmpersands(paymentRequest.getPostalCode());
	}
	/**
	 * @return Returns the country.
	 */
	private String getCountry() 
	{
		String _countryString = paymentRequest.getCountry();
		
		//Country _country = CountryFactory.getCountry(_countryString);
		//_countryString = _country.getISOCode();
		
		return XMLTools.encodeAmpersands(_countryString);
	}
	/**
	 * @return Returns the email.
	 */
	private String getEmail() {
		return XMLTools.encodeAmpersands(paymentRequest.getEmailAddress());
	}
	/**
	 * @return Returns the currency.
	 */
	private String getCurrency() {
		return paymentRequest.getCurrencyCode();
	}
	/**
	 * @return Returns the grandTotalAmount.
	 */
	private String getGrandTotalAmount() {
		return String.valueOf(paymentRequest.getAmount());
		//return String.valueOf("1000");
	}
		
	private String getCybersourceProfileID() {
		return paymentRequest.getCybersourceProfileID();
	}
	/**
	 * @param field1 The field1 to set.
	 */
	public void setField1(String field1) {
		this.field1 = field1;
	}
	/**
	 * @return Returns the field1.
	 */
	private String getField1() {
		return XMLTools.encodeAmpersands(field1);
	}
	/**
	 * @param field2 The field2 to set.
	 */
	public void setField2(String field2) {
		this.field2 = field2;
	}
	/**
	 * @return Returns the field2.
	 */
	private String getField2() {
		return XMLTools.encodeAmpersands(field2);
	}
	/**
	 * @param field3 The field3 to set.
	 */
	public void setField3(String field3) {
		this.field3 = field3;
	}
	/**
	 * @return Returns the field3.
	 */
	private String getField3() {
		return XMLTools.encodeAmpersands(field3);
	}
	/**
	 * @param field4 The field4 to set.
	 */
	public void setField4(String field4) {
		this.field4 = field4;
	}
	/**
	 * @return Returns the field4.
	 */
	private String getField4() {
		return XMLTools.encodeAmpersands(field4);
	}
	/**
	 * @return Returns the requestID.
	 */
	private String getRequestID() {
		return paymentRequest.getRequestID();
	}
	/**
	 * @return Returns the requestToken.
	 */
	private String getRequestToken() {
		return paymentRequest.getRequestToken();
	}
	
}
