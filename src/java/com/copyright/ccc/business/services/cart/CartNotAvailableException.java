package com.copyright.ccc.business.services.cart;

import com.copyright.ccc.CCRuntimeException;
import com.copyright.data.order.CartStatusException;

/**
 * Exception class that indicates that a cart related transaction (e.g. addition of an item)
 * could not take place due to the fact that the involved cart is in a state not compatible
 * with the desired transaction (e.g. cart has been already checked out).
 */
public class CartNotAvailableException extends CCRuntimeException
{
  
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
CartStatusException _cartStatusException;
  long _unavailableCartID;
  
  private CartNotAvailableException(){}
  
  CartNotAvailableException( CartStatusException cartStatusException,
                             long unavailableCartID )
  {
    setCartStatusException( cartStatusException );
    setUnavailableCartID( unavailableCartID );
  }

  /**
   * Indicates if the cart has been checked out.
   */
  public boolean isCartCheckedOut()
  {
    return getCartStatusException().isCheckedOut();
  }
  
  /**
   * Indicates if the cart has been deleted.
   */
  public boolean isCartDeleted()
  {
    return getCartStatusException().isDeleted();
  }
  
  /**
   * Indicates if the cart has expired.
   */
  public boolean isCartExpired()
  {
    return getCartStatusException().isExpired();
  }
  
  /**
   * Indicates if the cart has been locked for checkout.
   */
  public boolean isCartLockedForCheckout()
  {
    return getCartStatusException().isLockedForCheckout();
  }
  
  /**
   * Returns the ID of the unavailable <code>Cart</code>.
   */
  public long getUnavailableCartID()
  {
    return _unavailableCartID;
  }
  
  private void setCartStatusException(CartStatusException cartStatusException)
  {
    this._cartStatusException = cartStatusException;
  }

  private CartStatusException getCartStatusException()
  {
    return _cartStatusException;
  }

  private void setUnavailableCartID(long unavailableCartID)
  {
    this._unavailableCartID = unavailableCartID;
  }

  
}
