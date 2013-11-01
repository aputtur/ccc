package com.copyright.ccc.business.services.cart;

//import com.copyright.data.payment.CreditCard;
import java.math.BigDecimal;
import java.util.Date;

import com.copyright.base.enums.PaymentGateway;
import com.copyright.svc.order.api.data.Payment;

/**
 * @author Lucas Alberione
 * @version 1.0
 * @created 26-Oct-2006 1:45:37 PM
 * Updated for Cybersource Fields - 9/17/2010
 */
 
 /**
  * Abstract class that defines an abstraction of the details
  * involved when using credit card as payment method in an
  * e-commerce transaction.
  */
public abstract class CreditCardDetails {

  /**
   * Returns the credit card type asociated with this <code>CreditCardDetails</code>.
   */
  public abstract String getCardType();

  public abstract void setCardType(String cardType);
  
  public abstract String getMerchantRefId(); 
  
  public abstract void setMerchantRefId(String merchantRefId);
  
  public abstract String getCurrencyType();
  
  public abstract void setCurrencyType(String currencyType);

  public abstract BigDecimal getExchangeRate();
  
  public abstract void setExchangeRate(BigDecimal exchangeRate);

  public abstract Date getExchangeDate(); 

  public abstract void setExchangeDate( Date exchangeDate);
  
  public abstract Long getCurrencyRateId();
  
  public abstract void setCurrencyRateId(Long currencyRateId);
  
  public abstract BigDecimal getCurrencyPaidTotal();
  
  public abstract void setCurrencyPaidTotal(BigDecimal currencyPaid);

  public abstract BigDecimal getUsdPaidTotal();
  
  public abstract void setUsdPaidTotal(BigDecimal usdPaidTotal);

  public abstract PaymentGateway paymentGateway();
  
  public abstract void setPaymentGateway(PaymentGateway paymentGateway);
  
  public abstract String getCcAuthNo();

  public abstract void setCcAuthNo( String ccAuthNo);

  public abstract void setCcTrxId( BigDecimal ccTrxId);
  
  public abstract BigDecimal getCcTrxId();

  public abstract void setCcTrxDate( Date ccTrxDate);
  
  public abstract Date getCcTrxDate();
  
  public abstract Integer getLastFourCc();
  
  public abstract void setLastFourCc(Integer lastFour);
  
  public abstract Date getPurchaseDate(); 

  public abstract void setPurchaseDate( Date purchaseDate);

  public abstract void setPayerPartyId( Long payerPartyId);
  
  public abstract Long getPayerPartyId();

  public abstract void setPayerPtyInst( Long payerPtyInst);
  
  public abstract Long getPayerPtyInst();

  public abstract String getRequestId();

  public abstract void setRequestId(String requestId);

  public abstract String getAuthRequestId();

  public abstract void setAuthRequestId(String requestId);

  public abstract String getRequestToken();
 
  public abstract void setRequestToken(String requestToken);
  
  public abstract String getAuthRequestToken();
  
  public abstract void setAuthRequestToken(String requestToken);
  
  public abstract String getReconciliationID();

  public abstract void setReconciliationID(String reconciliationID);

  public abstract String getStatus();

  public abstract void setStatus(String status);

  public abstract String getAuthStatus();

  public abstract void setAuthStatus(String status);

  public abstract boolean isSuccess();

  public abstract void setSuccess(boolean success);

  public abstract boolean isAuthSuccess();

  public abstract void setAuthSuccess(boolean success);

	/**
	 * Returns the credit card number asociated with this <code>CreditCardDetails</code>.
	 */
  @Deprecated
  public abstract String getNumber();

	 /**
	  * Sets the credit card number asociated with this <code>CreditCardDetails</code>.
	  */
  @Deprecated
  public abstract void setNumber(String number);

	/**
	 * Returns the credit card expiration month asociated with this <code>CreditCardDetails</code>.
	 */

  @Deprecated
  public abstract String getExpirationMonth();

	 /**
	  * Sets the credit card expiration month asociated with this <code>CreditCardDetails</code>.
	  */
  @Deprecated
  public abstract void setExpirationMonth(String expirationMonth);

	/**
	 * Returns the credit card expiration year asociated with this <code>CreditCardDetails</code>.
	 */

  @Deprecated
  public abstract String getExpirationYear();

  public abstract String getCardExpirationDate();
  
  public abstract void setCardExpirationDate(String cardExpirationDate);
  
  public abstract String getCardHolderName();
  
  public abstract void setCardHolderName(String cardHolderName);
  

  
	 /**
	  * Sets the credit card expiration year asociated with this <code>CreditCardDetails</code>.
	  */

  @Deprecated
  public abstract void setExpirationYear(String expirationYear);

	/**
	 * Returns the name on the credit card asociated with this <code>CreditCardDetails</code>.
	 */

  @Deprecated
  public abstract String getNameOnCard();

	 /**
	  * Sets the name on the credit card asociated with this <code>CreditCardDetails</code>.
	  */

  @Deprecated
  public abstract void setNameOnCard(String nameOnCard);
  	
	/**
	 * Returns the Promotion Code asociated with this <code>InvoiceDetails</code>.
	 */
  @Deprecated
  public abstract String getPromotionCode();
	
	/**
	 * Sets the Promotion Code asociated with this <code>InvoiceDetails</code>.
	 */
  @Deprecated
  public abstract void setPromotionCode( String promotionCode );
  
  public abstract void setPayment(Payment payment);

  public abstract Payment getPayment();
  
  public abstract void setPaymentProfileIdentifier(String paymentProfileIdentifier);
  
  public abstract String getPaymentProfileIdentifier();

  public abstract void setPaymentProfileCccId(Long paymentProfileCccId);
  
  public abstract Long getPaymentProfileCccId();

  @Deprecated
  public abstract void setCcExpirationDate(String ccExpirationDate);
  
  @Deprecated
  public abstract String getCcExpirationDate();
  
  public abstract Long getPaymentId();
  
  public abstract Date getCcAuthDate();

  public abstract void setCcAuthDate(Date ccAuthDate);
   
  public abstract String getCcAuthDateString();
 
  public abstract void setCcAuthDateString(String ccAuthDateString);  

  public abstract String getCybersourceErrorMessage();

  public abstract void setCybersourceErrorMessage(String errorMessage);
  
}