package com.copyright.ccc.business.services.adjustment;

/**
 * Class that represents a summary detail for a given order adjustment summary.
 */
public class OrderAdjustmentSummaryDetail
{
  private double _licenseeFee = 0;
  private double _royaltyComposite = 0;
  private double _discount = 0;
  
  private OrderAdjustmentSummaryDetail(){}
  
  OrderAdjustmentSummaryDetail( double licenseeFee,
                                double royaltyComposite,
                                double discount )
  {
    setLicenseeFee( OrderAdjustmentUtils.roundToDefaultDecimalPlaces( licenseeFee ) );
    setRoyaltyComposite( OrderAdjustmentUtils.roundToDefaultDecimalPlaces( royaltyComposite ) );
    setDiscount( OrderAdjustmentUtils.roundToDefaultDecimalPlaces( discount ) );
  }

  private void setLicenseeFee(double licenseeFee)
  {
    this._licenseeFee = licenseeFee;
  }

  /**
   * Returns the licensee fee
   */
  public double getLicenseeFee()
  {
    return _licenseeFee;
  }

  private void setRoyaltyComposite(double royaltyComposite)
  {
    this._royaltyComposite = royaltyComposite;
  }


  /**
   * Returns the royalty composite
   */
  public double getRoyaltyComposite()
  {
    return _royaltyComposite;
  }

  private void setDiscount(double discount)
  {
    this._discount = discount;
  }


  /**
   * Returns the discount
   */
  public double getDiscount()
  {
    return _discount;
  }
  
  
  /**
   * Returns the total
   */
  public double getTotal()
  {
    return getLicenseeFee() +
           getRoyaltyComposite() +
           getDiscount();
  }
}
