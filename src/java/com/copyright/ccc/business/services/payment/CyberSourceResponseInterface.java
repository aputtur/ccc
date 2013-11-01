package com.copyright.ccc.business.services.payment;

public interface CyberSourceResponseInterface 
{
	public String MERCHANT_REFERENCE_CODE_TAG = "c:merchantReferenceCode";
	public String REQUEST_ID_TAG = "c:requestID";
	public String DECISION_TAG = "c:decision";
	public String REASON_CODE_TAG = "c:reasonCode";
	public String REQUEST_TOKEN_TAG = "c:requestToken";
	public String AUTH_REPLY_TAG = "c:ccAuthReply";
	public String CAPTURE_REPLY_TAG = "c:ccCaptureReply";
	public String CREDIT_REPLY_TAG = "c:ccCreditReply";
	public String AUTHORIZATION_CODE_TAG = "c:authorizationCode";
	public String RECONCILIATION_ID_TAG = "c:reconciliationID";
	public String AUTHDATETIME_TAG = "c:requestDateTime";
}
