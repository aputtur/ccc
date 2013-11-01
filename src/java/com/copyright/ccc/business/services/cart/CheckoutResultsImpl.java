package com.copyright.ccc.business.services.cart;

import java.util.List;

import com.copyright.ccc.business.data.CheckoutResults;
import com.copyright.ccc.business.data.OrderLicense;
import com.copyright.ccc.business.data.OrderPurchase;

/**
 *  Basic implementation of <code>CheckoutResults</code> interface.
 */
public final class CheckoutResultsImpl implements CheckoutResults
{
  private long _confirmationNumber;
  private String _orderDate;
  private OrderPurchase _orderPurchase;
  private List<OrderLicense> _orderItems;
  private String _orderTotal;
  private CreditCardDetails _creditCardDetails;
  
  private CheckoutResultsImpl(){}
  
  CheckoutResultsImpl( long confirmationNumber,
                       String orderDate,
                       OrderPurchase orderPurchase,
                       List<OrderLicense> orderItems,
                       String orderTotal )
  {
    setConfirmationNumber( confirmationNumber );
    setOrderDate( orderDate );
    setOrderPurchase( orderPurchase );
    setOrderItems( orderItems );
    setOrderTotal( orderTotal );
  }


  /**
   * See <code>CheckoutResults.getOrderConfirmationNumber()</code>
   */
  public long getOrderConfirmationNumber()
  {
    return _confirmationNumber;
  }

  /**
   * Implementation of <code>CheckoutResults.getOrderDate()</code>
   */
  public String getOrderDate()
  {
    return _orderDate;
  }

  /**
   * Implementation of <code>CheckoutResults.getOrderItems()</code>
   */
  public List<OrderLicense> getOrderItems()
  {
    return _orderItems;
  }

  public OrderPurchase getOrderPurchase()
  {
    return _orderPurchase;
  }

  
  /**
   * Implementation of <code>CheckoutResults.getOrderTotal()</code>
   */
  public String getOrderTotal()
  {
    return _orderTotal;
  }

  private void setConfirmationNumber( long confirmationNumber )
  {
    this._confirmationNumber = confirmationNumber;
  }

  private void setOrderDate( String orderDate )
  {
    this._orderDate = orderDate;
  }

  private void setOrderItems( List<OrderLicense> orderItems )
  {
    this._orderItems = orderItems;
  }

  private void setOrderPurchase( OrderPurchase orderPurchase )
  {
    this._orderPurchase = orderPurchase;
  }

  
  private void setOrderTotal( String orderTotal )
  {
    this._orderTotal = orderTotal;
  }

public void setCreditCardDetails(CreditCardDetails _creditCardDetails) {
	this._creditCardDetails = _creditCardDetails;
}

public CreditCardDetails getCreditCardDetails() {
	return _creditCardDetails;
}
 
}
