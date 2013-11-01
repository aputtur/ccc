package com.copyright.ccc.business.services.cart;

import com.copyright.data.ValidationException;

/**
 * Exception class indicating the occurrence of credit card validation issues
 * in a credit-card-related transaction (e.g. checkout a cart).
 */
public class CreditCardValidationException extends ECommerceValidationException
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private CreditCardDetails _creditCardDetails;
  
  private CreditCardValidationException(){}

  
  CreditCardValidationException( ValidationException validationException,
                                 CreditCardDetails creditCardDetails )
  {
    super(validationException);
    
    setCreditCardDetails(creditCardDetails);
  }


  /**
   * Returns the problemtic instance of <code>CreditCardDetails</code>
   * that threw this exception.
   */
  public CreditCardDetails getCreditCardDetails()
  {
    return _creditCardDetails;
  }

  
  private void setCreditCardDetails(CreditCardDetails creditCardDetails)
  {
    this._creditCardDetails = creditCardDetails;
  }
}
