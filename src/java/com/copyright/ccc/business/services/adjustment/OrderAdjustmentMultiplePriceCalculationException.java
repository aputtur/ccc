package com.copyright.ccc.business.services.adjustment;

import java.util.Map;

/**
 * Exception class to indicate error occurence when calculating the 
 * price for multiple adjustments.
 */
public class OrderAdjustmentMultiplePriceCalculationException extends Exception
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<String,OrderAdjustmentPriceCalculationException> _individualOrderAdjustmentPriceCalculationExceptions;

  
  private OrderAdjustmentMultiplePriceCalculationException(){}
  

  OrderAdjustmentMultiplePriceCalculationException( Map<String,OrderAdjustmentPriceCalculationException> individualOrderAdjustmentPriceCalculationExceptions )
  {
    
     if( individualOrderAdjustmentPriceCalculationExceptions == null )
     {
       throw new IllegalArgumentException( "Provided individualOrderAdjustmentPriceCalculationExceptions cannot be null" );
     }
     
     setIndividualOrderAdjustmentPriceCalculationExceptions( individualOrderAdjustmentPriceCalculationExceptions );
  }
    

  public Map<String,OrderAdjustmentPriceCalculationException> getIndividualOrderAdjustmentPriceCalculationExceptions()
  {
    return _individualOrderAdjustmentPriceCalculationExceptions;
  }
  
  
  private void setIndividualOrderAdjustmentPriceCalculationExceptions( Map<String,OrderAdjustmentPriceCalculationException> individualOrderAdjustmentPriceCalculationExceptions )
  {
    this._individualOrderAdjustmentPriceCalculationExceptions = individualOrderAdjustmentPriceCalculationExceptions;
  }
}
