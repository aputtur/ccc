package com.copyright.ccc.business.services.adjustment;

import java.math.BigDecimal;

/**
 * Utility class to be used exclusively by the Adjustents Application
 */
final class OrderAdjustmentUtils
{
  
  private OrderAdjustmentUtils(){}
  
  static double roundToDefaultDecimalPlaces( double valueToRound ){
    return round( valueToRound, OrderAdjustmentConstants.DEFAULT_DECIMAL_PLACES_TO_ROUND );
  }
  
  static double round( double valueToRound, int decimalPlaces ){
      
      if( decimalPlaces < 0 )
        throw new IllegalArgumentException("decimalPlaces must be equal or grater than zero");
      
      return new BigDecimal( valueToRound ).setScale( decimalPlaces, BigDecimal.ROUND_HALF_EVEN ).doubleValue();
    }
    
 
  
}
