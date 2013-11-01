package com.copyright.ccc.business.services.adjustment;

import java.math.BigDecimal;

/**
 * Class that represents summaries for an order adjustment.
 */
public class OrderAdjustmentSummary
{
  private OrderAdjustmentSummaryDetail _originalDetails;
  private OrderAdjustmentSummaryDetail _adjustmentsNetDetails;
  private OrderAdjustmentSummaryDetail _currentAdjustmentDetails;
  
  private OrderAdjustmentSummary(){}
  
  OrderAdjustmentSummary( OrderAdjustmentSummaryDetail originalDetails,
                          OrderAdjustmentSummaryDetail adjustmentNetDetails,
                          OrderAdjustmentSummaryDetail currentAdjustmentDetails )
  {
    
    if( originalDetails == null || adjustmentNetDetails == null || currentAdjustmentDetails == null )
    {
      throw new IllegalArgumentException("Unable to instantiate OrderAdjustmentSummary: non-null set of details must be provided.");
    }
    
    setOriginalDetails( originalDetails );
    setAdjustmentsNetDetails( adjustmentNetDetails );
    setCurrentAdjustmentDetails( currentAdjustmentDetails );
    
  }

  private void setOriginalDetails(OrderAdjustmentSummaryDetail originalDetails)
  {
    this._originalDetails = originalDetails;
  }

  /**
   * Returns the original order summary details.
   */
  public OrderAdjustmentSummaryDetail getOriginalDetails()
  {
    return _originalDetails;
  }

  private void setAdjustmentsNetDetails(OrderAdjustmentSummaryDetail adjustmentsNetDetails)
  {
    this._adjustmentsNetDetails = adjustmentsNetDetails;
  }

  /**
   * Returns the net adjustments summary details.
   */
  public OrderAdjustmentSummaryDetail getAdjustmentsNetDetails()
  {
    return _adjustmentsNetDetails;
  }

  private void setCurrentAdjustmentDetails(OrderAdjustmentSummaryDetail currentAdjustmentDetails)
  {
    this._currentAdjustmentDetails = currentAdjustmentDetails;
  }

  public OrderAdjustmentSummaryDetail getCurrentAdjustmentDetails()
  {
    return _currentAdjustmentDetails;
  }
  
  /**
   * Returns the total summary details.
   */
  public OrderAdjustmentSummaryDetail getTotal()
  {
    BigDecimal totalLicenseeFee = new BigDecimal( getOriginalDetails().getLicenseeFee() )
                                  .add( new BigDecimal( getAdjustmentsNetDetails().getLicenseeFee() ) )
                                  .add( new BigDecimal( getCurrentAdjustmentDetails().getLicenseeFee() ) );
    
    BigDecimal totalRoyaltyComposite = new BigDecimal( getOriginalDetails().getRoyaltyComposite() )
                                       .add( new BigDecimal( getAdjustmentsNetDetails().getRoyaltyComposite() ) )
                                       .add( new BigDecimal( getCurrentAdjustmentDetails().getRoyaltyComposite() ) );
                                   
    BigDecimal totalDiscount = new BigDecimal( getOriginalDetails().getDiscount() )
                              .add( new BigDecimal( getAdjustmentsNetDetails().getDiscount() ) )
                              .add( new BigDecimal(  getCurrentAdjustmentDetails().getDiscount() ) );                            
  
  
    return new OrderAdjustmentSummaryDetail( totalLicenseeFee.doubleValue(), 
                                             totalRoyaltyComposite.doubleValue(), 
                                             totalDiscount.doubleValue() );
  }
}
