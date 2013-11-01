package com.copyright.ccc.business.data;

import java.util.List;

import com.copyright.ccc.business.services.cart.CreditCardDetails;

/**
 * Abstraction that represents the results of a successful checkout transaction.
 */
public interface CheckoutResults
{

  /**
   * Returns the confirmation number of the recently created order.
   */
  public long getOrderConfirmationNumber();
  
  /**
   * Returns the date of the recently created order.
   */
  public String getOrderDate();
  
  /**
    * Returns a <code>List</code> containing the items in the recently created order.
    */
  public List<OrderLicense> getOrderItems();


  /**
   * Returns the total fees for the recently created order.
   */
  public String getOrderTotal();
  
  public CreditCardDetails getCreditCardDetails();
    
}
