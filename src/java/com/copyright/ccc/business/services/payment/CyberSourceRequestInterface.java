package com.copyright.ccc.business.services.payment;


public interface CyberSourceRequestInterface 
{
	public final String REQUEST_MESSAGE = "requestMessage";
	public final String MERCHANT_REFERENCE_CODE = "merchantReferenceCode";
	public final String BILL_TO = "billTo";
	public final String FIRST_NAME = "firstName";
	public final String LAST_NAME = "lastName";
	public final String STREET1 = "street1";
	public final String CITY = "city";
	public final String STATE = "state";
	public final String POSTAL_CODE = "postalCode";
	public final String COUNTRY = "country";
	public final String PHONE_NUMBER = "phoneNumber";
	public final String EMAIL = "email";
	public final String BUSINESS_RULES = "businessRules";
	public final String IGNORE_AVS_RESULT = "ignoreAVSResult";
	public final String PURCHASE_TOTALS = "purchaseTotals";
	public final String CURRENCY = "currency";
	public final String GRAND_TOTAL_AMOUNT = "grandTotalAmount";
	public final String CARD = "card";
	public final String ACCOUNT_NUMBER = "accountNumber";
	public final String EXPIRATION_MONTH = "expirationMonth";
	public final String EXPIRATION_YEAR = "expirationYear";
	public final String MERCHANT_DEFINED_DATA = "merchantDefinedData";
	public final String FIELD1 = "field1";
	public final String FIELD2 = "field2";
	public final String FIELD3 = "field3";
	public final String FIELD4 = "field4";
	public final String SUBSCRIPTION = "recurringSubscriptionInfo";
	public final String SUBSCRIPTION_ID = "subscriptionID";
	public final String CC_AUTH_SERVICE = "ccAuthService";
	public final String CC_CAPTURE_SERVICE = "ccCaptureService";
	public final String CC_CREDIT_SERVICE = "ccCreditService";
	public final String VOID_SERVICE = "voidService";
	public final String CAPTURE_AUTH_REQUEST_ID = "authRequestID";
	public final String CAPTURE_REQUEST_ID = "captureRequestID";
	public final String CAPTURE_REQUEST_TOKEN = "captureRequestToken";
	public final String AUTH_REQUEST_TOKEN = "authRequestToken";
	public final String AUTH_REQUEST_ID = "authRequestID";
	public final String RUN = "run";
	public final String TRUE = "true";
	public final String XMLNS = "xmlns";
}
