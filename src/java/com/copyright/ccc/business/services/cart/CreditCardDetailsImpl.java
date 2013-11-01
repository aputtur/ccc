package com.copyright.ccc.business.services.cart;

import java.math.BigDecimal;
import java.util.Date;

import com.copyright.base.enums.PaymentGateway;
import com.copyright.ccc.business.services.ItemConstants;
import com.copyright.svc.order.api.data.Payment;

/**
 * Implementation of <code>CreditCardDetails</code>.
 */
class CreditCardDetailsImpl extends CreditCardDetails
{
  private Payment _payment;
  
  private String _promotionCode;
  private String _paymentProfileIdentifier;
  private Long _paymentProfileCccId;
  private String _ccExpirationDate;
  private String _cybersourceErrorMessage;
  
  private CreditCardDetailsImpl(){
	  _payment = new Payment();
	  _payment.setPaymentMethodCd(ItemConstants.PAYMENT_METHOD_CREDIT_CARD);
  }
  
  public CreditCardDetailsImpl( Payment payment ){
    setPayment( payment );
    _payment.setPaymentMethodCd(ItemConstants.PAYMENT_METHOD_CREDIT_CARD);
  }

  @Override
  public Long getPaymentId() {
	  return getPayment().getPaymentId();
  }
  
  @Override
  public String getCardType()
  {
    return getPayment().getCreditCardTypeCd();
  }

  @Override
  public void setCardType(String cardType)
  {
    getPayment().setCreditCardTypeCd( cardType );
  }
  
  @Override
  public String getMerchantRefId() {
	  return getPayment().getMerchantRefId();
  }
  
  @Override
  public void setMerchantRefId(String merchantRefId) {
	  getPayment().setMerchantRefId(merchantRefId);
  }
  
  @Override
  public String getCurrencyType() {
	  return getPayment().getCurrencyType();
  }
  
  @Override
  public void setCurrencyType(String currencyType) {
	  getPayment().setCurrencyType(currencyType);
  }

  @Override
  public BigDecimal getExchangeRate() {
	  return getPayment().getExchangeRate();
  }
  
  @Override
  public void setExchangeRate(BigDecimal exchangeRate) {
	  getPayment().setExchangeRate(exchangeRate);
  }

  @Override
  public Date getExchangeDate() {
	  return getPayment().getExchangeDate(); 
  }

  @Override
  public void setExchangeDate( Date exchangeDate) {
	  getPayment().setExchangeDate(exchangeDate);
  }
  
  @Override
  public Long getCurrencyRateId() {
	  return getPayment().getCurrencyRateId();
  }
  
  @Override
  public void setCurrencyRateId(Long currencyRateId) {
	  getPayment().setCurrencyRateId(currencyRateId);
  }
  
  @Override
  public BigDecimal getCurrencyPaidTotal() {
	  return getPayment().getCurrencyPaidTotal();
  }
  
  @Override
  public void setCurrencyPaidTotal(BigDecimal currencyPaidTotal) {
	  getPayment().setCurrencyPaidTotal(currencyPaidTotal);
  }

  @Override
  public BigDecimal getUsdPaidTotal() {
	  return getPayment().getUsdTotal();
  }
  
  @Override
  public void setUsdPaidTotal(BigDecimal usdPaidTotal) {
	  getPayment().setUsdTotal(usdPaidTotal);;
  }

  @Override
  public PaymentGateway paymentGateway() {
	  return getPayment().getPaymentGateway();
  }
  
  @Override
  public void setPaymentGateway(PaymentGateway paymentGateway) {
	  getPayment().setPaymentGateway(paymentGateway);
  }
  
  @Override
  public Date getPurchaseDate() {
	  return getPayment().getPurchaseDtm(); 
  }

  @Override
  public void setPurchaseDate( Date purchaseDate) {
	  getPayment().setPurchaseDtm(purchaseDate);
  }

  @Override
  public void setPayerPartyId( Long payerPartyId) {
	  getPayment().setPayerPartyId(payerPartyId);
  }
  
  @Override
  public Long getPayerPartyId() {
	  return getPayment().getPayerPartyId();
  }

  @Override
  public void setPayerPtyInst( Long payerPtyInst) {
	  getPayment().setPayerPtyInst(payerPtyInst);
  }
  
  @Override
  public Long getPayerPtyInst() {
	  return getPayment().getPayerPtyInst();
  }

  @Override
public  void setPayment(Payment payment)
  	{
	  this._payment = payment;
  }

  @Override
  public Payment getPayment()
  {
    return _payment;
  }
   
  @Override
  public Integer getLastFourCc()
  {
    return getPayment().getLastFourCc();
  }
  
  @Override
  public void setLastFourCc(Integer lastFourCc)
  {
    getPayment().setLastFourCc( lastFourCc );
  }
  
  @Override
  public String getCcAuthNo()
  {
    return getPayment().getCcAuthNo();
  }

  @Override
  public void setCcAuthNo(String ccAuthNo)
  {
    getPayment().setCcAuthNo( ccAuthNo );
  }

  public Date getPurchaseDtm()
  {
    return getPayment().getPurchaseDtm();
  }

  public void setPurchaseDtm(Date purchaseDtm)
  {
    getPayment().setPurchaseDtm( purchaseDtm );
  }
  
  @Override
  public Date getCcTrxDate()
  {
    return getPayment().getCcTrxDate();
  }

  @Override
  public void setCcTrxDate(Date CcTrxDate)
  {
    getPayment().setCcTrxDate( CcTrxDate );
  }
  
  @Override
  public BigDecimal getCcTrxId()
  {
    return getPayment().getCcTrxId();
  }

  @Override
  public void setCcTrxId(BigDecimal ccTrxId)
  {
    getPayment().setCcTrxId( ccTrxId );
  }
  
  @Override
  public String getNumber()
  {
	  return "XXXX";
  }

  @Override
  public void setNumber(String number)
  {
//    getCreditCard().setCcNumber( number );
  }

  @Override
  public String getExpirationMonth()
  {
    return "XX/XX";
  }

  @Override
  public void setExpirationMonth(String expirationMonth)
  {
//    getCreditCard().setCcExpirationMon( expirationMonth );
  }

  @Override
  public String getExpirationYear()
  {
    return "XXXX";
  }

  @Override
  public void setExpirationYear(String expirationYear)
  {
//    getCreditCard().setCcExpirationYear( expirationYear );
  }

  @Override
  public String getNameOnCard()
  {
    return "XXXX XXXX";
  }

  @Override
  public void setNameOnCard(String nameOnCard)
  {
//    getCreditCard().setCardholderName( nameOnCard );
  }

  @Override
  public String getCardExpirationDate() {
	  return getPayment().getCardExpirationDate();
  }
  
  @Override
  public void setCardExpirationDate(String cardExpirationDate) {
	  getPayment().setCardExpirationDate(cardExpirationDate);
  }
  
  @Override
  public String getCardHolderName() {
	  return getPayment().getCardHolderName();
  }
  
  @Override
  public void setCardHolderName(String cardHolderName) {
	  getPayment().setCardHolderName(cardHolderName);
  }
 
  @Override
  public String getPromotionCode()
  {
    return _promotionCode;
  }

  @Override
  public void setPromotionCode(String promotionCode)
  {
    _promotionCode = promotionCode;
  }
  
  @Override
  public void setPaymentProfileIdentifier(String paymentProfileIdentifier) {
	  getPayment().setPaymentProfileIdentifier(paymentProfileIdentifier);
  }
  
  @Override
  public String getPaymentProfileIdentifier() {
	  return getPayment().getPaymentProfileIdentifier();
  }

  @Override
  public void setPaymentProfileCccId(Long paymentProfileCccId) {
	  getPayment().setCcProfileId(paymentProfileCccId);
  }
  
  @Override
  public Long getPaymentProfileCccId() {
	  return getPayment().getCcProfileId();
  }

  @Override
  public void setCcExpirationDate(String ccExpirationDate) {
	  _ccExpirationDate = ccExpirationDate;
  }
  
  @Override
  public String getCcExpirationDate() {
	  return _ccExpirationDate;
  }

  @Override
  public String getRequestId() {
	  return getPayment().getRequestId();
  }

  @Override
  public void setRequestId(String requestId) {
	  getPayment().setRequestId(requestId);
  }

  @Override
  public String getAuthRequestId() {
	  return getPayment().getAuthRequestId();
  }

  @Override
  public void setAuthRequestId(String requestId) {
	  getPayment().setAuthRequestId(requestId);
  }

  @Override
  public String getRequestToken() {
	  return getPayment().getRequestToken();
  }

  @Override
  public void setRequestToken(String requestToken) {
	  getPayment().setRequestToken(requestToken);
  }

  @Override
  public String getAuthRequestToken() {
	  return getPayment().getAuthRequestToken();
  }

  @Override
  public void setAuthRequestToken(String requestToken) {
	  getPayment().setAuthRequestToken(requestToken);
  }

  @Override
  public String getReconciliationID() {
	  return getPayment().getReconciliationId();
  }

  @Override
  public void setReconciliationID(String reconciliationID) {
	  getPayment().setReconciliationId(reconciliationID);	
  }

  @Override
  public String getStatus() {
	  return getPayment().getStatusCd();
  }

  @Override
  public void setStatus(String status) {
	  getPayment().setStatusCd(status);
  }

  @Override
  public String getAuthStatus() {
	  return getPayment().getAuthStatusCd();
  }

  @Override
  public void setAuthStatus(String status) {
	  getPayment().setAuthStatusCd(status);
  }

  @Override
  public boolean isSuccess() {
	  return getPayment().getSuccess();
  }	

  @Override
  public void setSuccess(boolean success) {
	  getPayment().setSuccess(success);
  }
  
  @Override
  public boolean isAuthSuccess() {
	  return getPayment().getAuthSuccess();
  }	

  @Override
  public void setAuthSuccess(boolean success) {
	  getPayment().setAuthSuccess(success);
  }
  
  @Override
  public Date getCcAuthDate() {
	  return getPayment().getCcAuthDate();
  }

  @Override
  public void setCcAuthDate(Date ccAuthDate) {
	  getPayment().setCcAuthDate(ccAuthDate);
  }
   
  @Override
  public String getCcAuthDateString() {
	  return getPayment().getCcAuthDateString();
  }
 
  @Override
  public void setCcAuthDateString(String ccAuthDateString) {
	  getPayment().setCcAuthDateString(ccAuthDateString);  
  }

  @Override
  public String getCybersourceErrorMessage() {
	  return _cybersourceErrorMessage;
  }

  @Override
  public void setCybersourceErrorMessage(String errorMessage) {
	  _cybersourceErrorMessage = errorMessage;
  }

}
