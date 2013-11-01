package com.copyright.ccc.business.services.payment;

import java.io.Serializable;

public class PaymentResponse implements Serializable
{
	private static final long serialVersionUID = 1L;
    private final String LINE_SEPARATOR = System.getProperty("line.separator");
	private String authStatus = null;
	private String status = null;
	private String authNum = null;
	private String authRequestID = null;
	private String requestID = null;
	private String authRequestToken = null;
	private String requestToken = null;
	private String reconciliationID = null;
	private String cardType = null;
	private double exchangeRate = 0.0;
	private double fundingValue = 0.0;
	private String fundingCurrency = null;
	private boolean authSuccess = false;
	private boolean success = false;
	private String cccProfileId = null;
	private String merchantRefId = null;
	private String authDateTime = null;

	public String getCccProfileId() {
		return cccProfileId;
	}
	
	public void setCccProfileId(String ccProfileId) {
		this.cccProfileId = ccProfileId;
	}
	/**
	 * @param authNum The authNum to set.
	 */
	public void setAuthNum(String authNum) {
		this.authNum = authNum;
	}
	/**
	 * @return Returns the authNum.
	 */
	public String getAuthNum() {
		return authNum;
	}
	/**
	 * @param requestID The requestID to set.
	 */
	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}
	/**
	 * @return Returns the referenceID.
	 */
	public String getRequestID() {
		return requestID;
	}
	/**
	 * @param exchangeRate The exchangeRate to set.
	 */
	public void setExchangeRate(double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	/**
	 * @return Returns the exchangeRate.
	 */
	public double getExchangeRate() {
		return exchangeRate;
	}
	/**
	 * @param fundingValue The fundingValue to set.
	 */
	public void setFundingValue(double fundingValue) {
		this.fundingValue = fundingValue;
	}
	/**
	 * @return Returns the fundingValue.
	 */
	public double getFundingValue() {
		return fundingValue;
	}
	/**
	 * @param fundingCurrency The fundingCurrency to set.
	 */
	public void setFundingCurrency(String fundingCurrency) {
		this.fundingCurrency = fundingCurrency;
	}
	/**
	 * @return Returns the fundingCurrency.
	 */
	public String getFundingCurrency() {
		return fundingCurrency;
	}
	/**
	 * @param value The success value to set.
	 */
	public void setSuccess(boolean value) {
		this.success = value;
	}
	/**
	 * @return Returns the success value.
	 */
	public boolean getSuccess() {
		return success;
	}
	/**
	 * @param status The status to set.
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return Returns the status.
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param requestToken The requestToken to set.
	 */
	public void setRequestToken(String requestToken) {
		this.requestToken = requestToken;
	}
	/**
	 * @return Returns the requestToken.
	 */
	public String getRequestToken() {
		return requestToken;
	}
	/**
	 * @param reconciliationID The reconciliationID to set.
	 */
	public void setReconciliationID(String reconciliationID) {
		this.reconciliationID = reconciliationID;
	}
	/**
	 * @return Returns the reconciliationID.
	 */
	public String getReconciliationID() {
		return reconciliationID;
	}
	/**
	 * @param cardType The cardType to set.
	 */
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	/**
	 * @return Returns the cardType.
	 */
	public String getCardType() {
		return cardType;
	}
	
	/**
	 * @param merchantRefId The merchantRefId to set.
	 */
	public void setMerchantRefId(String merchantRefId) {
		this.merchantRefId = merchantRefId;
	}
	/**
	 * @return Returns the cardType.
	 */
	public String getMerchantRefId() {
		return merchantRefId;
	}
	
	/**
	 * @param authDateTime The authDateTime to set.
	 */
	public void setAuthDate(String authDateTime) {
		this.authDateTime = authDateTime;
	}
	/**
	 * @return Returns the Date String.
	 */
	public String getAuthDate() {
		return authDateTime;
	}
	
	
	public String getAuthStatus() {
		return authStatus;
	}

	public void setAuthStatus(String authStatus) {
		this.authStatus = authStatus;
	}

	public String getAuthRequestID() {
		return authRequestID;
	}

	public void setAuthRequestID(String authRequestID) {
		this.authRequestID = authRequestID;
	}

	public String getAuthRequestToken() {
		return authRequestToken;
	}

	public void setAuthRequestToken(String authRequestToken) {
		this.authRequestToken = authRequestToken;
	}

	public boolean getAuthSuccess() {
		return authSuccess;
	}

	public void setAuthSuccess(boolean authSuccess) {
		this.authSuccess = authSuccess;
	}

	@Override
	public String toString()
	{
		StringBuffer _sb = new StringBuffer("status: ").append(getStatus()).append(LINE_SEPARATOR);
		_sb.append("requestID: ").append(getRequestID()).append(LINE_SEPARATOR);
		_sb.append("requestToken: ").append(getRequestToken()).append(LINE_SEPARATOR);
		_sb.append("reconciliationID: ").append(getReconciliationID()).append(LINE_SEPARATOR);
		_sb.append("cardType: ").append(getCardType()).append(LINE_SEPARATOR);
		_sb.append("exchangeRate: ").append(getExchangeRate()).append(LINE_SEPARATOR);
		_sb.append("fundingValue: ").append(getFundingValue()).append(LINE_SEPARATOR);
		_sb.append("fundingCurrency: ").append(getFundingCurrency()).append(LINE_SEPARATOR);
		_sb.append("success: ").append(getSuccess()).append(LINE_SEPARATOR);
		_sb.append("cccProfileId: ").append(getCccProfileId()).append(LINE_SEPARATOR);
		_sb.append("merchantRefId: ").append(getMerchantRefId()).append(LINE_SEPARATOR);
		_sb.append("authDateTime: ").append(getAuthDate()).append(LINE_SEPARATOR);
		_sb.append("authNum: ").append(getAuthNum()).append(LINE_SEPARATOR);
		_sb.append("authSuccess: ").append(getAuthSuccess()).append(LINE_SEPARATOR);
		_sb.append("authRequestID: ").append(getAuthRequestID()).append(LINE_SEPARATOR);
		_sb.append("authRequestToken: ").append(getAuthRequestToken()).append(LINE_SEPARATOR);
		_sb.append("authStatus: ").append(getAuthStatus()).append(LINE_SEPARATOR);
		return _sb.toString();
	}
	
}
