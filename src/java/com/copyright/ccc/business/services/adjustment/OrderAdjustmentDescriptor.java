package com.copyright.ccc.business.services.adjustment;

import java.io.Serializable;
import java.util.Date;

import com.copyright.ccc.business.services.user.User;
import com.copyright.ccc.business.services.user.UserServices;
import com.copyright.data.order.CartConstants;
import com.copyright.data.order.CartDescriptor;
import com.copyright.workbench.time.DateUtils2;

/**
 * Class used to describe an order adjustment.
 */
public class OrderAdjustmentDescriptor implements Serializable
{
  private static final long serialVersionUID = 1L;
  
  private static final String DATE_MASK = "MM/dd/yyyy hh:mm:ss";
  
  private CartDescriptor _cartDescriptor;
  private User _createUser = null;
  private User _lastUpdateUser = null;
  
  private OrderAdjustmentDescriptor(){}
  
  
  public OrderAdjustmentDescriptor( CartDescriptor cartDescriptor )
  {
    if( cartDescriptor == null ) 
    {
      throw new IllegalArgumentException("A CartDescriptor instance must be provided");
    }
    
    setCartDescriptor( cartDescriptor );
       
    User createUser = UserServices.getUserByPartyId( getCartDescriptor().getCreateUserPartyID() );
    
    if( createUser != null )
    {
      setCreateUser( createUser );
    }
 
    boolean previouslyUpdated = getCartDescriptor().getUpdateUserPartyID() > 0;
    
    if ( previouslyUpdated )
    {
     
      User lastUpdateUser = UserServices.getUserByPartyId( getCartDescriptor().getCreateUserPartyID() );
      
      if( lastUpdateUser != null )
      {
        setLastUpdateUser( lastUpdateUser );
      }
      
    }
  }

  /**
   * Returns this descriptor's ID.
   */
  public long getID()
  {
    return getCartDescriptor().getID();
  }

  /**
   * Returns cart descriptor.
   */
  public CartDescriptor getCartDescriptor()
  {
    return _cartDescriptor;
  }
  
  /**
   * Returns creation date.
   */
  public String getCreateDate()
  {
    return formatDate( getCartDescriptor().getCreateDate() );
  }
  
  
  /**
   * Returns creation user name.
   */
  public String getCreateUserName()
  {
    String createUserName = null;
    
    if( getCreateUser() != null )
    {
      createUserName = getCreateUser().getUsername();
    }
    
    return createUserName;
  }
  
  
  /**
   * Returns adjustment type.
   */
  public String getAdjustmentType()
  {
    String adjustmentType = null;
    
    if( isInvoiceAdjustment() )
    {
      adjustmentType = OrderAdjustmentConstants.ADJUSTMENT_TYPE_INVOICE_VERBOSE;
    }
    
    if( isPurchaseAdjustment() )
    {
      adjustmentType = OrderAdjustmentConstants.ADJUSTMENT_TYPE_PURCHASE_VERBOSE;
    }
    
    if( isDetailAdjustment() )
    {
      adjustmentType = OrderAdjustmentConstants.ADJUSTMENT_TYPE_DETAIL_VERBOSE;
    }
  
    return adjustmentType;
  }


  /**
   * Specifies if this descriptor is describing a detail adjustment.
   */
  public boolean isDetailAdjustment()
  {
    return getCartDescriptor().getAdjustmentType().equals( CartConstants.CART_ADJUSTMENT_TYPE_DETAIL );
  }


  /**
   * Specifies if this descriptor is describing a purchase adjustment.
   */
  public boolean isPurchaseAdjustment()
  {
    return getCartDescriptor().getAdjustmentType().equals( CartConstants.CART_ADJUSTMENT_TYPE_PURCHASE );
  }


  /**
   * Specifies if this descriptor is describing an invoice adjustment.
   */
  public boolean isInvoiceAdjustment()
  {
    return getCartDescriptor().getAdjustmentType().equals( CartConstants.CART_ADJUSTMENT_TYPE_INVOICE );
  }
  
  
  /**
   * Returns the date of last update for the adjustment being described by this descriptor.
   */
  public String getLastUpdateDate()
  {
    String lastUpdateDate = null;
    
    if( getLastUpdateUser() != null )
    {
      lastUpdateDate = formatDate( getCartDescriptor().getLastUpdated() );
    }
    
    return lastUpdateDate;
  }
  
  
  /**
   * Returns the user name who last updated the adjustment being described by this descriptor.
   */
  public String getLastUpdateUserName()
  {
    String lastUpdateUserName = null;
    
    if( getLastUpdateUser() != null )
    {
      lastUpdateUserName = getLastUpdateUser().getUsername();
    }
    
    return lastUpdateUserName;
  }
  
  
  ///////////////////////////////////////////////////////////
  //Private methods
  ///////////////////////////////////////////////////////////
   
  private void setCartDescriptor(CartDescriptor cartDescriptor)
  {
    this._cartDescriptor = cartDescriptor;
  }


  private void setCreateUser(User createUser)
  {
    this._createUser = createUser;
  }


  private User getCreateUser()
  {
    return _createUser;
  }


  private void setLastUpdateUser(User lastUpdateUser)
  {
    this._lastUpdateUser = lastUpdateUser;
  }


  private User getLastUpdateUser()
  {
    return _lastUpdateUser;
  }
  
  
  private String formatDate( Date date )
  {
    return DateUtils2.formatDate( date, DATE_MASK );
  }
}
