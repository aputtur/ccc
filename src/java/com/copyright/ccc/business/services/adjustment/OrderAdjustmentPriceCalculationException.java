package com.copyright.ccc.business.services.adjustment;

/**
 * Exception class to indicate error occurence when calculating the 
 * price for an adjustment.
 */
public class OrderAdjustmentPriceCalculationException extends Exception
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

OrderAdjustmentPriceCalculationException( String message )
  {
    super( message );
  }
  
  OrderAdjustmentPriceCalculationException( String message, Throwable cause ) 
  {
      super(message,cause);
  }
}
