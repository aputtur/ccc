package com.copyright.ccc.business.services.cart;

//import com.copyright.data.payment.InvoiceRequest;

/**
 * Implementation of <code>InvoiceDetails</code>.
 */
class InvoiceDetailsImpl extends InvoiceDetails{

  private String _promotionCode;
  private String _poNumber;

  public InvoiceDetailsImpl(){}
  
  @Override
  public String getPONumber()
  {
    return _poNumber;
  }

  @Override
  public void setPONumber(String poNumber)
  {
    _poNumber = poNumber;
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
}
