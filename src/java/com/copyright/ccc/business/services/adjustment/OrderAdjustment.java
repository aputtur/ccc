package com.copyright.ccc.business.services.adjustment;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.copyright.ccc.business.data.OrderLicense;
import com.copyright.ccc.business.security.CCPrivilegeCode;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.ecommerce.PurchasablePermission;
import com.copyright.ccc.business.services.ecommerce.PurchasablePermissionFactory;
import com.copyright.ccc.business.services.order.OrderLicenseImpl;
import com.copyright.data.order.AdjustmentSummary;
import com.copyright.data.order.Cart;
import com.copyright.data.order.CartNotFoundException;
import com.copyright.data.order.CartStatusException;
import com.copyright.data.order.OrderMgmtException;
import com.copyright.data.order.PermissionRequest;
import com.copyright.service.order.CartServiceAPI;
import com.copyright.service.order.CartServiceFactory;
import com.copyright.service.order.OrderMgmtServiceAPI;
import com.copyright.service.order.OrderMgmtServiceFactory;
import com.copyright.workbench.time.DateUtils2;


/**
 * Class representing an adjustment to an invoice, a purchase or detail.
 */
public final class OrderAdjustment implements  Serializable
{

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private static final long INVALID_BODY_ITEM_ID = -1;
  private static final long INVALID_ODT_INST = -1L;
  private static final boolean RECALCULATE_ON_FEE_ONLY_MODIFIED_ITEMS = true;
  private static final int INVALID_CART_ID = 0;

  private String _sourceID;
  private String _adjustmentType;
  private String _status;
  
  private Date _adjustmentDate;
  
  private OrderAdjustmentCustomerDetails _customer;
  
  private Map<String,OrderAdjustmentBodyItem> _body;
  
  private long _bodyItemIDToModify = INVALID_BODY_ITEM_ID;
       
  private long _cartID = INVALID_CART_ID;   
     
  private Date _createDate;
  private Date _modifyDate;
  
  private long _userPartyID;
  private String _createUser;
  private String _modifyUser;
  
  private AdjustmentSummary _persistedSummary = null;
  
  OrderAdjustment()
  {
    UserContextService.checkPrivilege( CCPrivilegeCode.ENTER_ORDER_ADJUSTMENT );
   
    Date todaysDate = new Date();
    
    setAdjustmentDate( todaysDate );
    setStatus( OrderAdjustmentConstants.ADJUSTMENT_STATUS_NEW );
    setCreateDate( todaysDate );
    
  }

  void setSourceID(String sourceID)
  {
    this._sourceID = sourceID;
  }

  /**
   * Returns the source (invoice, pruchase or detail) ID.
   */
  public String getSourceID()
  {
    return _sourceID;
  }

  void setAdjustmentType(String adjustmentType)
  {
    if( StringUtils.isEmpty(adjustmentType) )
    {
      throw new IllegalArgumentException("Adjustment type must be provided");
    }
    
    this._adjustmentType = adjustmentType;
  }

  /**
   * Returns the adjustment type (invoice, pruchase or detail).
   */
  public String getAdjustmentType()
  {
    return _adjustmentType;
  }

  void setStatus(String status)
  {
    this._status = status;
  }
  
  /**
   * Returns the status of the adjustment.
   */
  public String getStatus()
  {
    return _status;
  }

  void setAdjustmentDate(Date adjustmentDate)
  {
    if( adjustmentDate == null )
    {
      throw new IllegalArgumentException( "A non-null adjustment date must be provided" );
    }
    
    this._adjustmentDate = adjustmentDate;
  }

  
  /**
   * Returns the date of the adjustment.
   */
  public Date getAdjustmentDate()
  {
    return _adjustmentDate;
  }
  
  /**
   * Returns the date of the adjustment, fomatted as "yyyy-MM-dd".
   */
  public String getAdjustmentFormattedDate()
  {
    return DateUtils2.formatDate(_adjustmentDate, "yyyy-MM-dd");
  }

  void setCustomer(OrderAdjustmentCustomerDetails customer)
  {
    this._customer = customer;
  }

  /**
   * Returns the customer details for this adjustment.
   */
  public OrderAdjustmentCustomerDetails getCustomer()
  {
    return _customer;
  }

  void setBody(Map<String,OrderAdjustmentBodyItem> body)
  {
    if( body == null || body.isEmpty() )
    {
      throw new IllegalArgumentException( "A non-empty body must be provided" );
    }
    
    this._body = body;
  }

  /**
   * Returns the body of the adjustment, composed of instances of <code>OrderAdjustmentBodyItem</code>.
   */
  public Map<String,OrderAdjustmentBodyItem> getBody()
  {
    return _body;
  }

  void setCartID(long cartID)
  {
    this._cartID = cartID;
  }

  /**
   * Returns the cart ID asociated with this adjustment.
   */
  public long getCartID()
  {
    return _cartID;
  }

  void setCreateDate(Date createDate)
  {
    this._createDate = createDate;
  }

  /**
   * Returns the creation date of the adjustment.
   */
  public Date getCreateDate()
  {
    return _createDate;
  }

  void setModifyDate(Date modifyDate)
  {
    this._modifyDate = modifyDate;
  }

  /**
   * Returns the modification date of the adjustment.
   */
  public Date getModifyDate()
  {
    return _modifyDate;
  }

  void setUserPartyID(long userPartyID)
  {
    this._userPartyID = userPartyID;
  }

  /**
   * Returns the user party ID asociated with this adjustment.
   */
  public long getUserPartyID()
  {
    return _userPartyID;
  }

  void setCreateUser(String createUser)
  {
    this._createUser = createUser;
  }

  /**
   * Returns the user that created this adjustment.
   */
  public String getCreateUser()
  {
    return _createUser;
  }

  void setModifyUser(String modifyUser)
  {
    this._modifyUser = modifyUser;
  }

  /**
   * Returns the user that last modified this adjustment.
   */
  public String getModifyUser()
  {
    return _modifyUser;
  }
  
  /**
   * Returns if this is an invoice adjustment.
   */
  public boolean isInvoiceAdjustment()
  {
    return getAdjustmentType().equals( OrderAdjustmentConstants.ADJUSTMENT_TYPE_INVOICE );
  }
  
  
  /**
   * Returns if this is an purchase adjustment.
   */
  public boolean isPurchaseAdjustment()
  {
    return getAdjustmentType().equals( OrderAdjustmentConstants.ADJUSTMENT_TYPE_PURCHASE );
  }
  
  
  /**
   * Returns if this is an detail adjustment.
   */
  public boolean isDetailAdjustment()
  {
    return getAdjustmentType().equals( OrderAdjustmentConstants.ADJUSTMENT_TYPE_DETAIL );
  }

  /**
   * Sets the body item to modify ID.
   */
  public void setBodyItemIDToModify(long bodyItemIDToModify)
  {
    this._bodyItemIDToModify = bodyItemIDToModify;
  }

  public long getBodyItemIDToModify()
  {
    return _bodyItemIDToModify;
  }
  
  /**
   * Returns the body item to modify ID.
   */
  public void resetBodyItemIDToModify()
  {
    setBodyItemIDToModify( INVALID_BODY_ITEM_ID );
  }
  
  /**
   * Returns the body item to modify.
   */
  public OrderAdjustmentBodyItem getBodyItemToModify()
  {
    
    OrderAdjustmentBodyItem bodyItemToModify = null;
    
    boolean bodyItemIDToModifyPresent = getBodyItemIDToModify() != INVALID_BODY_ITEM_ID;
    
    if( bodyItemIDToModifyPresent )
    {
      bodyItemToModify = getBody().get( getBodyItemIDToModify() );
    }
    
    return null;
  }
  
  /**
   * Full credits this adjustment.
   */
  public void performFullCredit( long reasonCode ) 
  throws OrderAdjustmentMultiplePriceCalculationException
  {
    
    Map<String,OrderAdjustmentPriceCalculationException> individualPriceCalculationErrors = 
    	new HashMap<String,OrderAdjustmentPriceCalculationException>( 0 );
    
    Iterator<Map.Entry<String,OrderAdjustmentBodyItem>> iterator = getBody().entrySet().iterator();
    
    while( iterator.hasNext() )
    {
      Map.Entry<String,OrderAdjustmentBodyItem> entry = iterator.next();
      
      OrderAdjustmentBodyItem bodyItem = entry.getValue();
      
      //We want to full credit adjustable items only
      if ( bodyItem.isAdjustable() )
      {
        try
        {
          bodyItem.performFullCredit( reasonCode );
        } catch ( OrderAdjustmentPriceCalculationException priceCalculationException )
        {
          individualPriceCalculationErrors.put( entry.getKey(), priceCalculationException );
        }
      }
    }
    
    if( !individualPriceCalculationErrors.isEmpty() )
    {
      throw new OrderAdjustmentMultiplePriceCalculationException( individualPriceCalculationErrors );
    }
    
  }
 
  

  
 /**
  * Performs a global adjustment, based on the provided parameters.
  * Returns the number of adjusted order details.
  */
  public int performGlobalAdjustment(  String fieldToAdjust, 
                                       double currentValue, 
                                       double newValueAfterAdjustment,
                                       long reasonCode ) 
  throws OrderAdjustmentMultiplePriceCalculationException
  {
    
    if( StringUtils.isEmpty( fieldToAdjust ) )
    {
      throw new IllegalArgumentException( "A non-empty field to adjust value must be provided" );
    }
    
    if( newValueAfterAdjustment > currentValue )
    {
      throw new IllegalArgumentException( "New value after adjustment (" + newValueAfterAdjustment + ") cannot be greater than current value (" + currentValue + ")" );
    }
    
    Map<String,OrderAdjustmentPriceCalculationException> individualPriceCalculationErrors = 
    	new HashMap<String,OrderAdjustmentPriceCalculationException>( 0 );
    
    int numberOfAdjustedOrderDetails = 0;
    
    double newCurrentAdjustmentValue = newValueAfterAdjustment - currentValue;
       
    Iterator<Map.Entry<String,OrderAdjustmentBodyItem>> iterator = getBody().entrySet().iterator();
    
    while( iterator.hasNext() )
    {
      Map.Entry<String,OrderAdjustmentBodyItem> entry = iterator.next();
      
      OrderAdjustmentBodyItem bodyItem = entry.getValue();
      
      if( !bodyItem.isAdjustable() )
      {
        //We do not want to adjust non adjustable items
        continue;
      }
      
      if( fieldToAdjust.equals( OrderAdjustmentConstants.NUMBER_OF_COPIES ) )
      {
        boolean adjusted = conditionallyAdjustNumberOfCopies( bodyItem, (long)currentValue, (long)newCurrentAdjustmentValue, reasonCode );
        if (adjusted)
        {
          ++numberOfAdjustedOrderDetails;
        }
      }
      
      if( fieldToAdjust.equals( OrderAdjustmentConstants.NUMBER_OF_PAGES ) )
      {
        boolean adjusted = conditionallyAdjustNumberOfPages( bodyItem, (long)currentValue, (long)newCurrentAdjustmentValue, reasonCode );
        if (adjusted)
        {
          ++numberOfAdjustedOrderDetails;
        }
      }
      
      if( fieldToAdjust.equals( OrderAdjustmentConstants.NUMBER_OF_STUDENTS ) )
      {
        boolean adjusted = conditionallyAdjustNumberOfStudents( bodyItem, (long)currentValue, (long)newCurrentAdjustmentValue, reasonCode );
        if (adjusted)
        {
          ++numberOfAdjustedOrderDetails;
        }
      }
      
      if( fieldToAdjust.equals( OrderAdjustmentConstants.NUMBER_OF_RECIPIENTS ) )
      {
        boolean adjusted = conditionallyAdjustNumberOfRecipients( bodyItem, (long)currentValue, (long)newCurrentAdjustmentValue, reasonCode );
        if (adjusted)
        {
          ++numberOfAdjustedOrderDetails;
        }
      }
      
      if( fieldToAdjust.equals( OrderAdjustmentConstants.DURATION ) )
      {
        boolean adjusted = conditionallyAdjustDuration( bodyItem, (long)currentValue, (long)newCurrentAdjustmentValue, reasonCode );
        if (adjusted)
        {
          ++numberOfAdjustedOrderDetails;
        }
      }
      
      if( fieldToAdjust.equals( OrderAdjustmentConstants.NUMBER_OF_CHARTS ) )
      {
        boolean adjusted = conditionallyAdjustNumberOfCharts( bodyItem, (long)currentValue, (long)newCurrentAdjustmentValue, reasonCode );
        if (adjusted)
        {
          ++numberOfAdjustedOrderDetails;
        }
      }
      
      if( fieldToAdjust.equals( OrderAdjustmentConstants.NUMBER_OF_EXCERPTS ) )
      {
        boolean adjusted = conditionallyAdjustNumberOfExcerpts( bodyItem, (long)currentValue, (long)newCurrentAdjustmentValue, reasonCode );
        if (adjusted)
        {
          ++numberOfAdjustedOrderDetails;
        }
      }
      
      if( fieldToAdjust.equals( OrderAdjustmentConstants.NUMBER_OF_FIGURE_DIAGRAM_TABLES ) )
      {
        boolean adjusted = conditionallyAdjustNumberOfFiguresDiagramsTables( bodyItem, (long)currentValue, (long)newCurrentAdjustmentValue, reasonCode );
        if (adjusted)
        {
          ++numberOfAdjustedOrderDetails;
        }        
      }
      
      if( fieldToAdjust.equals( OrderAdjustmentConstants.NUMBER_OF_GRAPHS ) )
      {
        boolean adjusted = conditionallyAdjustNumberOfGraphs( bodyItem, (long)currentValue, (long)newCurrentAdjustmentValue, reasonCode );
        if (adjusted)
        {
          ++numberOfAdjustedOrderDetails;
        }  
      }
      
      if( fieldToAdjust.equals( OrderAdjustmentConstants.NUMBER_OF_ILLUSTRATIONS ) )
      {
        boolean adjusted = conditionallyAdjustNumberOfIllustrations( bodyItem, (long)currentValue, (long)newCurrentAdjustmentValue, reasonCode );
        if (adjusted)
        {
          ++numberOfAdjustedOrderDetails;
        } 
      }
      
      if( fieldToAdjust.equals( OrderAdjustmentConstants.NUMBER_OF_PHOTOGRAPHS ) )
      {
        boolean adjusted = conditionallyAdjustNumberOfPhotographs( bodyItem, (long)currentValue, (long)newCurrentAdjustmentValue, reasonCode );
        if (adjusted)
        {
          ++numberOfAdjustedOrderDetails;
        } 
      }
      
      if( fieldToAdjust.equals( OrderAdjustmentConstants.NUMBER_OF_QUOTATIONS ) )
      {
        boolean adjusted = conditionallyAdjustNumberOfQuotations( bodyItem, (long)currentValue, (long)newCurrentAdjustmentValue, reasonCode );
        if (adjusted)
        {
          ++numberOfAdjustedOrderDetails;
        }
      }
      
      if( fieldToAdjust.equals( OrderAdjustmentConstants.NUMBER_OF_CARTOONS ) )
      {
        boolean adjusted = conditionallyAdjustNumberOfCartoons( bodyItem, (long)currentValue, (long)newCurrentAdjustmentValue, reasonCode );
        
        if (adjusted)
        {
          ++numberOfAdjustedOrderDetails;
        }
      }
      
      if( fieldToAdjust.equals( OrderAdjustmentConstants.NUMBER_OF_LOGOS ) )
      {
        boolean adjusted = conditionallyAdjustNumberOfLogos( bodyItem, (long)currentValue, (long)newCurrentAdjustmentValue, reasonCode );
        
        if (adjusted)
        {
          ++numberOfAdjustedOrderDetails;
        }
      }
      
      if( OrderAdjustmentConstants.LICENSEE_FEE.equals( fieldToAdjust ) )
      {
        boolean adjusted = conditionallyAdjustLicenseeFee( bodyItem, currentValue, newCurrentAdjustmentValue, reasonCode );
        if (adjusted)
        {
          ++numberOfAdjustedOrderDetails;
        }
      }
      
     boolean isFieldToAdjustQuantity = !OrderAdjustmentConstants.LICENSEE_FEE.equals( fieldToAdjust ) &&
                                       !OrderAdjustmentConstants.ROYALTY.equals( fieldToAdjust ) &&
                                       !OrderAdjustmentConstants.DISCOUNT.equals( fieldToAdjust );
     
     if( bodyItem.areCurrentAdjustmentQuantitiesModified() && isFieldToAdjustQuantity )
     {
        try
        {
          bodyItem.recalculateCurrentAdjustmentPrice();
        } catch ( OrderAdjustmentPriceCalculationException priceCalculationException )
        {
           individualPriceCalculationErrors.put( entry.getKey(), priceCalculationException );
        }
      }
    
    }
    
    if( !individualPriceCalculationErrors.isEmpty() )
    {
      throw new OrderAdjustmentMultiplePriceCalculationException( individualPriceCalculationErrors );
    }
    
    return numberOfAdjustedOrderDetails;
    
  }
  
  
  /**
   * Returns if this adjustment has been previously saved.
   */
  public boolean hasBeenSaved()
  {
    // Having a valid reference to an existing cart 
    // means the adjustment has been previously saved.
    return isValidCartID(); 
  }
  
  
  
  /**
   * Deletes the current adjustment of the body item identified by <code>bodyItemID</code>.
   */
  public void deleteCurrentAdjustment( String bodyItemID )
  {
    if( StringUtils.isEmpty( bodyItemID ) )
    {
      throw new IllegalArgumentException( "A non-empty body item ID must be provided" );
    }
    
    OrderAdjustmentBodyItem bodyItem = getBody().get( bodyItemID );
    
    boolean bodyItemFound = bodyItem != null;
    
    if( bodyItemFound )
    {      
      OrderLicense originalOrderDetails = bodyItem.getOriginalOrderDetails();
      
      if( originalOrderDetails instanceof OrderLicenseImpl )
      {
        if( this.hasBeenSaved() )
        {
          PermissionRequest permissionRequestToRemoveFromCart = PurchasablePermissionFactory.getPermissionRequest( bodyItem.getCurrentAdjustmentsDetails() );
          
          long cartID = this.getCartID();
          
          try
          {
            Cart cart = getCartService().getCartById( cartID );
            if ( cart != null )
            {
              getCartService().removeFromCart( permissionRequestToRemoveFromCart, cart );
            }
          }
          catch ( CartNotFoundException cnfe )
          {
            //Nothing to do in here...
          }
          catch ( CartStatusException cse ) 
          {
            //TODO lalberione 04/17/2007: Do we need to do extra processing here?
          }
        }
        
        PermissionRequest permissionRequest = new PermissionRequest( ( (OrderLicenseImpl) originalOrderDetails ).getLicense() , true );
        
        //As fees doesn't seem to reset, do so...
        permissionRequest.setRightsholderFee(0);
        permissionRequest.setRoyaltyPayable(0);
        permissionRequest.setLicenseeFee(0);
        permissionRequest.setDiscount(0);
        
        //By deletion, we mean the creation a new current adjustment. 
        bodyItem.setCurrentAdjustmentsDetails( PurchasablePermissionFactory.createPurchasablePermission( permissionRequest ) );
      }     
    }
  }

  public void recalculateCurrentAdjustmentsPrices() 
  throws OrderAdjustmentMultiplePriceCalculationException
  {
    recalculateCurrentAdjustmentsPrices( RECALCULATE_ON_FEE_ONLY_MODIFIED_ITEMS );
  }
  
  
  /**
   * Recalculate prices for this adjustment's items.
   */
  public void recalculateCurrentAdjustmentsPrices( boolean recalculateOnFeeOnlyModifiedItems ) 
  throws OrderAdjustmentMultiplePriceCalculationException
  {
    Iterator<Map.Entry<String,OrderAdjustmentBodyItem>> iterator = getBody().entrySet().iterator();
    Map<String,OrderAdjustmentPriceCalculationException> individualPriceCalculationErrors = 
    	new HashMap<String,OrderAdjustmentPriceCalculationException>( 0 );
    
    while( iterator.hasNext() )
    {
      Map.Entry<String,OrderAdjustmentBodyItem> entry = iterator.next();
      OrderAdjustmentBodyItem bodyItem = entry.getValue();
      
      boolean mustPerformCalculation = bodyItem.areCurrentAdjustmentQuantitiesModified() ||
                                       ( bodyItem.areCurrentAdjustmentFeesModified() && recalculateOnFeeOnlyModifiedItems );

      if ( mustPerformCalculation )
      {
        try
        {
          bodyItem.recalculateCurrentAdjustmentPrice();
        } catch ( OrderAdjustmentPriceCalculationException priceCalculationException )
        {
          individualPriceCalculationErrors.put( entry.getKey(), priceCalculationException );
        }
      }
    }
    
    if( !individualPriceCalculationErrors.isEmpty() )
    {
      throw new OrderAdjustmentMultiplePriceCalculationException( individualPriceCalculationErrors );
    }
  }
  
  
  /**
   * Recalculate price for this adjustment's item, identified by the provided adjsutment body item ID.
   */
  public void recalculateCurrentAdjustmentPrice( long bodyItemID ) 
  throws OrderAdjustmentPriceCalculationException
  {
    OrderAdjustmentBodyItem bodyItem = getBody().get(String.valueOf(bodyItemID) );
    
    if( bodyItem != null )
    {
       bodyItem.recalculateCurrentAdjustmentPrice();
    }else
    {
      throw new OrderAdjustmentException( "Could not find body item for ID: " + bodyItemID );
    }

  }

  /**
   * Returns a <code>Map</code> of modified adjustable body items.
   */
  public Map<String,OrderAdjustmentBodyItem> getModifiedAdjustableBodyItems()
  {
    Map<String,OrderAdjustmentBodyItem> modifiedAdjustments = 
    	new HashMap<String,OrderAdjustmentBodyItem>(0);

    Iterator<Map.Entry<String,OrderAdjustmentBodyItem>> bodyIterator = getBody().entrySet().iterator();
    
    while( bodyIterator.hasNext() )
    {
      Map.Entry<String,OrderAdjustmentBodyItem> entry = bodyIterator.next();
      OrderAdjustmentBodyItem bodyItem = entry.getValue();
      
      if( bodyItem.isCurrentAdjustmentModified() && bodyItem.isAdjustable() )
      {
        modifiedAdjustments.put( entry.getKey(), entry.getValue() );
      }
    }
    
    return modifiedAdjustments;
  }


  /**
   * Indicates if there are modified body items in this <code>OrderAdjustment</code>.
   */
  public boolean areThereModifiedAdjustableBodyItems()
  {
    boolean areThereModifiedAdjustableBodyItems = !getModifiedAdjustableBodyItems().isEmpty();
    
    return areThereModifiedAdjustableBodyItems;
  }


  /**
   * Returns the summary for this adjustment.
   */
  public OrderAdjustmentSummary getSummary()
  {
    BigDecimal origLicenseeFeeTotal = new BigDecimal(0);
    BigDecimal origRoyaltyCompositeTotal = new BigDecimal(0);
    BigDecimal origDiscountTotal = new BigDecimal(0);
    
    BigDecimal adjsNetLicenseeFeeTotal = new BigDecimal(0);
    BigDecimal adjsNetRoyaltyCompositeTotal = new BigDecimal(0);
    BigDecimal adjsNetDiscountTotal = new BigDecimal(0);
    
    BigDecimal currAdjLicenseeFeeTotal = new BigDecimal(0);
    BigDecimal currAdjRoyaltyCompositeTotal = new BigDecimal(0);
    BigDecimal currAdjDiscountTotal = new BigDecimal(0);
    
    Iterator<Map.Entry<String,OrderAdjustmentBodyItem>> bodyIterator = getBody().entrySet().iterator();
    
    while( bodyIterator.hasNext() )
    {
      Map.Entry<String,OrderAdjustmentBodyItem> entry = bodyIterator.next();
      OrderAdjustmentBodyItem currentBodyItem = entry.getValue();
      
      origLicenseeFeeTotal = origLicenseeFeeTotal.add( new BigDecimal( currentBodyItem.getOriginalOrderDetails().getLicenseeFee() ) );
      origRoyaltyCompositeTotal = origRoyaltyCompositeTotal.add( new BigDecimal( currentBodyItem.getOriginalOrderDetails().getRoyaltyComposite() ) );
      origDiscountTotal = origDiscountTotal.add( new BigDecimal( currentBodyItem.getOriginalOrderDetails().getDiscount() ) );
      
      adjsNetLicenseeFeeTotal = adjsNetLicenseeFeeTotal.add( new BigDecimal( currentBodyItem.getPreviousAdjustmentsLicenseeFeeTotal() ) );
      adjsNetRoyaltyCompositeTotal = adjsNetRoyaltyCompositeTotal.add( new BigDecimal( currentBodyItem.getPreviousAdjustmentsRoyaltyCompositeTotal() ) );
      adjsNetDiscountTotal = adjsNetDiscountTotal.add( new BigDecimal( currentBodyItem.getPreviousAdjustmentsDiscountTotal() ) );
      
      currAdjLicenseeFeeTotal = currAdjLicenseeFeeTotal.add( new BigDecimal( currentBodyItem.getCurrentAdjustmentsDetails().getLicenseeFee() ) );
      currAdjRoyaltyCompositeTotal = currAdjRoyaltyCompositeTotal.add( new BigDecimal( currentBodyItem.getCurrentAdjustmentsDetails().getRoyaltyComposite() ) );
      currAdjDiscountTotal = currAdjDiscountTotal.add( new BigDecimal( currentBodyItem.getCurrentAdjustmentsDetails().getDiscount() ) );
      
    }
    
    OrderAdjustmentSummaryDetail originalDetailsSummary =           new OrderAdjustmentSummaryDetail( origLicenseeFeeTotal.doubleValue(), origRoyaltyCompositeTotal.doubleValue(), origDiscountTotal.doubleValue() );
    OrderAdjustmentSummaryDetail adjustmentsNetDetailsSummary =     new OrderAdjustmentSummaryDetail( adjsNetLicenseeFeeTotal.doubleValue(), adjsNetRoyaltyCompositeTotal.doubleValue(), adjsNetDiscountTotal.doubleValue() );
    OrderAdjustmentSummaryDetail currentAdjustmentsDetailsSummary = new OrderAdjustmentSummaryDetail( currAdjLicenseeFeeTotal.doubleValue(), currAdjRoyaltyCompositeTotal.doubleValue(), currAdjDiscountTotal.doubleValue() );
  
    return new OrderAdjustmentSummary( originalDetailsSummary, adjustmentsNetDetailsSummary, currentAdjustmentsDetailsSummary );
  }


  /**
   * Returns the invoice and purchase summaries for 
   * this <code>OrderAdjustment</code>.
   */
  public OrderAdjustmentSummaries getSummaries()
  {
    if( !isDetailAdjustment() )
    {
      throw new IllegalStateException( "Operation only available for Detail Order Adjustments" );
    }

    AdjustmentSummary summary = getPersistedSummary();
    
  /*  if( summary == null )
    {
      //TODO 04/12/2006 lalberione: Perform error processing here...
    }
    */
    BigDecimal currAdjsLicenseeFeeTotal = new BigDecimal(0);
    BigDecimal currAdjsRoyaltyCompositeTotal = new BigDecimal(0);
    BigDecimal currAdjsDiscountTotal = new BigDecimal(0);
    
    Iterator<Map.Entry<String,OrderAdjustmentBodyItem>> bodyIterator = getBody().entrySet().iterator();
    
    while( bodyIterator.hasNext() )
    {
      Map.Entry<String,OrderAdjustmentBodyItem> entry = bodyIterator.next();
      OrderAdjustmentBodyItem currentBodyItem = entry.getValue();
             
      currAdjsLicenseeFeeTotal = currAdjsLicenseeFeeTotal.add( new BigDecimal( currentBodyItem.getCurrentAdjustmentsDetails().getLicenseeFee() ) );
      currAdjsRoyaltyCompositeTotal = currAdjsRoyaltyCompositeTotal.add( new BigDecimal( currentBodyItem.getCurrentAdjustmentsDetails().getRoyaltyComposite() ) );
      currAdjsDiscountTotal = currAdjsDiscountTotal.add( new BigDecimal( currentBodyItem.getCurrentAdjustmentsDetails().getDiscount()) );
    }
    
    OrderAdjustmentSummaryDetail currentAdjustments = new OrderAdjustmentSummaryDetail( currAdjsLicenseeFeeTotal.doubleValue(), 
                                                                                        currAdjsRoyaltyCompositeTotal.doubleValue(),
                                                                                        currAdjsDiscountTotal.doubleValue() );
    
    OrderAdjustmentSummaryDetail invoiceOriginal = new OrderAdjustmentSummaryDetail( summary.getInvoiceLicenseeFee(), 
                                                                                     summary.getInvoiceRoyalty(), 
                                                                                     summary.getInvoiceDiscount() );
                                                                                     
    OrderAdjustmentSummaryDetail invoiceAdjustmentsNet = new OrderAdjustmentSummaryDetail( summary.getInvoiceAdjLicenseeFee(), 
                                                                                           summary.getInvoiceAdjRoyalty(),
                                                                                           summary.getInvoiceAdjDiscount() );
    
    
    OrderAdjustmentSummary invoiceSummary = new OrderAdjustmentSummary( invoiceOriginal,
                                                                        invoiceAdjustmentsNet, 
                                                                        currentAdjustments );
    
    OrderAdjustmentSummaryDetail purchaseOriginal = new OrderAdjustmentSummaryDetail( summary.getPurchaseLicenseeFee(), 
                                                                                      summary.getPurchaseRoyalty(), 
                                                                                      summary.getPurchaseDiscount() );
                                                                                     
    OrderAdjustmentSummaryDetail purchaseAdjustmentsNet = new OrderAdjustmentSummaryDetail( summary.getPurchaseAdjLicenseeFee(), 
                                                                                            summary.getPurchaseAdjRoyalty(),
                                                                                            summary.getPurchaseAdjDiscount() );
    
    OrderAdjustmentSummary purchaseSummary = new OrderAdjustmentSummary( purchaseOriginal,
                                                                         purchaseAdjustmentsNet,
                                                                         currentAdjustments );
    
    return new OrderAdjustmentSummaries( invoiceSummary, purchaseSummary );
    
  }
  
  void loadPersistedSummaryIfAppropiate()
  {
    if ( isDetailAdjustment() && _persistedSummary == null )
    {
      _persistedSummary = getPersistedSummary();
    }
  }
  
  //////////////////////////////////////////////////////
  //Private methods
  //////////////////////////////////////////////////////
   
  private AdjustmentSummary getPersistedSummary()
  {    
    if( !isDetailAdjustment() )
    {
      throw new IllegalStateException( "Operation only available for Detail Order Adjustments" );
    }
    
    if( _persistedSummary == null )
    {
      long odtInst = INVALID_ODT_INST;
            
      Iterator<Map.Entry<String,OrderAdjustmentBodyItem>> bodyIterator = getBody().entrySet().iterator();
      
      if( bodyIterator.hasNext() )
      {
        Map.Entry<String,OrderAdjustmentBodyItem> entry = bodyIterator.next();
        OrderAdjustmentBodyItem bodyItem = entry.getValue();
        
        odtInst = bodyItem.getOriginalOrderDetails().getID();
      }//TODO lalberione 07/17/2007: consider else case here
      
      try
      {
        //Retrieve from shared services...
        _persistedSummary = getOrderManagementService().getAdjustmentSummaryByOdtInst( odtInst );
      }
      catch ( OrderMgmtException ome )
      {
        throw new OrderAdjustmentException( ome.getClass().getName() + " thrown by shared services when retrieving persisted summary for order detail ID: " + odtInst,  ome);
      }
    }
    
    return _persistedSummary;
  }

  private OrderMgmtServiceAPI getOrderManagementService()
  {
    return OrderMgmtServiceFactory.getInstance().getService();
  }


  private CartServiceAPI getCartService()
  {
    return CartServiceFactory.getInstance().getService();
  }
  
  
  private boolean isValidCartID()
  {
    return getCartID() > INVALID_CART_ID;
  }
  
  
  private boolean conditionallyAdjustNumberOfCopies(  OrderAdjustmentBodyItem bodyItem, 
                                                      long currentValue, 
                                                      long newValue,
                                                      long reasonCode )
  {
    boolean adjusted = false;
    
    boolean productConditionsMet = bodyItem.isTRS();
    
    if (productConditionsMet)
    {
      OrderLicense originalDetails = bodyItem.getOriginalOrderDetails();
      boolean quantityConditionsMet = originalDetails.getNumberOfCopies() == currentValue;
      
      if( quantityConditionsMet )
      {
        PurchasablePermission currentAdjustment = bodyItem.getCurrentAdjustmentsDetails();
        currentAdjustment.setNumberOfCopies( newValue );
        currentAdjustment.setReasonCd( reasonCode );
        adjusted = true;
      }
    }
    
    return adjusted;
  }
  
  private boolean conditionallyAdjustNumberOfPages( OrderAdjustmentBodyItem bodyItem, 
                                                    long currentValue, 
                                                    long newValue,
                                                    long reasonCode )
  {
    boolean adjusted = false;
    
    boolean productConditionsMet = bodyItem.isTRS() ||
                                   bodyItem.isRLS() ||
                                   bodyItem.isAPS() ||
                                   bodyItem.isECCS();

    if (productConditionsMet)
    {
      OrderLicense originalDetails = bodyItem.getOriginalOrderDetails();
      boolean quantityConditionsMet = originalDetails.getNumberOfPages() == currentValue;
      
      if( quantityConditionsMet )
      {
        PurchasablePermission currentAdjustment = bodyItem.getCurrentAdjustmentsDetails();
        currentAdjustment.setNumberOfPages( newValue );
        currentAdjustment.setReasonCd( reasonCode );
        adjusted = true;
      }
    }
    
    return adjusted;
  }
  
  private boolean conditionallyAdjustNumberOfStudents(  OrderAdjustmentBodyItem bodyItem, 
                                                        long currentValue, 
                                                        long newValue,
                                                        long reasonCode )
  {
    boolean adjusted = false;
    
    boolean productConditionsMet = bodyItem.isAPS() ||
                                   bodyItem.isECCS();

    if (productConditionsMet)
    {
      OrderLicense originalDetails = bodyItem.getOriginalOrderDetails();
      boolean quantityConditionsMet = originalDetails.getNumberOfStudents() == currentValue;
      
      if( quantityConditionsMet )
      {
        PurchasablePermission currentAdjustment = bodyItem.getCurrentAdjustmentsDetails();
        //TODO 02/22/2007 lalberione: double check cast to int
        currentAdjustment.setNumberOfStudents( (int) newValue );
        currentAdjustment.setReasonCd( reasonCode );
        adjusted = true;
      }
    }
    
    return adjusted;
  }
  
  private boolean conditionallyAdjustNumberOfRecipients(  OrderAdjustmentBodyItem bodyItem, 
                                                          long currentValue, 
                                                          long newValue,
                                                          long reasonCode )
  {
    boolean adjusted = false;
    
    boolean productConditionsMet = bodyItem.isDPSEMail();
    
    if (productConditionsMet)
    {
      OrderLicense originalDetails = bodyItem.getOriginalOrderDetails();
      boolean quantityConditionsMet = originalDetails.getNumberOfRecipients() == currentValue;
      
      if( quantityConditionsMet )
      {
        PurchasablePermission currentAdjustment = bodyItem.getCurrentAdjustmentsDetails();
        currentAdjustment.setNumberOfRecipients( newValue );
        currentAdjustment.setReasonCd( reasonCode );
        adjusted = true;
      }
    }
    
    return adjusted;
  }
  
  
  private boolean conditionallyAdjustDuration(  OrderAdjustmentBodyItem bodyItem, 
                                                long currentValue, 
                                                long newValue,
                                                long reasonCode )
  {
    boolean adjusted = false;
    
    boolean productConditionsMet = bodyItem.isDPSExtranet() ||
                                   bodyItem.isDPSInternet() ||
                                   bodyItem.isDPSIntranet();
                                   
    if (productConditionsMet)
    {
      OrderLicense originalDetails = bodyItem.getOriginalOrderDetails();
      boolean quantityConditionsMet = originalDetails.getDuration() == currentValue;
      
      if( quantityConditionsMet )
      {
        PurchasablePermission currentAdjustment = bodyItem.getCurrentAdjustmentsDetails();
        //TODO 02/22/2007 lalberione: is this cast safe?
        currentAdjustment.setDuration( (int) newValue );
        currentAdjustment.setReasonCd( reasonCode );
        adjusted = true;
      }
    }
    
    return adjusted;
  }
  
  
  private boolean conditionallyAdjustNumberOfCharts(  OrderAdjustmentBodyItem bodyItem, 
                                                      long currentValue, 
                                                      long newValue,
                                                      long reasonCode )
  {
    boolean adjusted = false;
    
    boolean productConditionsMet = bodyItem.isRLS();
                                   
    if (productConditionsMet)
    {
      OrderLicense originalDetails = bodyItem.getOriginalOrderDetails();
      boolean quantityConditionsMet = originalDetails.getNumberOfCharts() == currentValue;
      
      if( quantityConditionsMet )
      {
        PurchasablePermission currentAdjustment = bodyItem.getCurrentAdjustmentsDetails();
        currentAdjustment.setNumberOfCharts( newValue );
        currentAdjustment.setReasonCd( reasonCode );
        adjusted = true;
      }
    }
    
    return adjusted;
  }
  
  private boolean conditionallyAdjustNumberOfExcerpts(  OrderAdjustmentBodyItem bodyItem, 
                                                        long currentValue, 
                                                        long newValue,
                                                        long reasonCode )
  {
    boolean adjusted = false;
    
    boolean productConditionsMet = bodyItem.isRLS();
                                   
    if (productConditionsMet)
    {
      OrderLicense originalDetails = bodyItem.getOriginalOrderDetails();
      boolean quantityConditionsMet = originalDetails.getNumberOfExcerpts() == currentValue;
      
      if( quantityConditionsMet )
      {
        PurchasablePermission currentAdjustment = bodyItem.getCurrentAdjustmentsDetails();
        currentAdjustment.setNumberOfExcerpts( newValue );
        currentAdjustment.setReasonCd( reasonCode );
        adjusted = true;
      }
    }
    
    
    return adjusted;
  }
  
  private boolean conditionallyAdjustNumberOfFiguresDiagramsTables( OrderAdjustmentBodyItem bodyItem, 
                                                                    long currentValue, 
                                                                    long newValue,
                                                                    long reasonCode )
  {
    boolean adjusted = false;
    
    boolean productConditionsMet = bodyItem.isRLS();
                                   
    if (productConditionsMet)
    {
      OrderLicense originalDetails = bodyItem.getOriginalOrderDetails();
      boolean quantityConditionsMet = originalDetails.getNumberOfFigures() == currentValue;
      
      if( quantityConditionsMet )
      {
        PurchasablePermission currentAdjustment = bodyItem.getCurrentAdjustmentsDetails();
        currentAdjustment.setNumberOfFigures( newValue );
        currentAdjustment.setReasonCd( reasonCode );
        adjusted = true;
      }
    }
    
    return adjusted;
  }
  
  private boolean conditionallyAdjustNumberOfGraphs(  OrderAdjustmentBodyItem bodyItem, 
                                                      long currentValue, 
                                                      long newValue,
                                                      long reasonCode )
  {
    boolean adjusted = false;
    
    boolean productConditionsMet = bodyItem.isRLS();
                                   
    if (productConditionsMet)
    {
      OrderLicense originalDetails = bodyItem.getOriginalOrderDetails();
      boolean quantityConditionsMet = originalDetails.getNumberOfGraphs() == currentValue;
      
      if( quantityConditionsMet )
      {
        PurchasablePermission currentAdjustment = bodyItem.getCurrentAdjustmentsDetails();
        currentAdjustment.setNumberOfGraphs( newValue );
        currentAdjustment.setReasonCd( reasonCode );
        adjusted = true;
      }
    }
    
    return adjusted;
  }
  
  private boolean conditionallyAdjustNumberOfIllustrations( OrderAdjustmentBodyItem bodyItem, 
                                                            long currentValue, 
                                                            long newValue,
                                                            long reasonCode )
  {
    boolean adjusted = false;
    
    boolean productConditionsMet = bodyItem.isRLS();
                                   
    if (productConditionsMet)
    {
      OrderLicense originalDetails = bodyItem.getOriginalOrderDetails();
      boolean quantityConditionsMet = originalDetails.getNumberOfIllustrations() == currentValue;
      
      if( quantityConditionsMet )
      {
        PurchasablePermission currentAdjustment = bodyItem.getCurrentAdjustmentsDetails();
        currentAdjustment.setNumberOfIllustrations( newValue );
        currentAdjustment.setReasonCd( reasonCode );
        adjusted = true;
      }
    }
    
    return adjusted;
  }
  
  private boolean conditionallyAdjustNumberOfPhotographs( OrderAdjustmentBodyItem bodyItem, 
                                                          long currentValue, 
                                                          long newValue,
                                                          long reasonCode )
  {
    boolean adjusted = false;
    
    boolean productConditionsMet = bodyItem.isRLS();
                                   
    if (productConditionsMet)
    {
      OrderLicense originalDetails = bodyItem.getOriginalOrderDetails();
      boolean quantityConditionsMet = originalDetails.getNumberOfPhotos() == currentValue;
      
      if( quantityConditionsMet )
      {
        PurchasablePermission currentAdjustment = bodyItem.getCurrentAdjustmentsDetails();
        currentAdjustment.setNumberOfPhotos( newValue );
        currentAdjustment.setReasonCd( reasonCode );
        adjusted = true;
      }
    }
    
    return adjusted;
  }
  
  private boolean conditionallyAdjustNumberOfQuotations(  OrderAdjustmentBodyItem bodyItem, 
                                                          long currentValue, 
                                                          long newValue,
                                                          long reasonCode )
  {
    boolean adjusted = false;
    
    boolean productConditionsMet = bodyItem.isRLS();
                                   
    if (productConditionsMet)
    {
      OrderLicense originalDetails = bodyItem.getOriginalOrderDetails();
      boolean quantityConditionsMet = originalDetails.getNumberOfQuotes() == currentValue;
      
      if( quantityConditionsMet )
      {
        PurchasablePermission currentAdjustment = bodyItem.getCurrentAdjustmentsDetails();
        currentAdjustment.setNumberOfQuotes( newValue );
        currentAdjustment.setReasonCd( reasonCode );
        adjusted = true;
      }
    } 
    
    return adjusted;
  }
  
  private boolean conditionallyAdjustNumberOfCartoons( OrderAdjustmentBodyItem bodyItem, 
                                                       long currentValue, 
                                                       long newValue,
                                                       long reasonCode )
  {
    boolean adjusted = false;
    
    boolean productConditionsMet = bodyItem.isRLS();
                                   
    if ( productConditionsMet )
    {
      OrderLicense originalDetails = bodyItem.getOriginalOrderDetails();
      boolean quantityConditionsMet = originalDetails.getNumberOfCartoons() == currentValue;
      
      if( quantityConditionsMet )
      {
        PurchasablePermission currentAdjustment = bodyItem.getCurrentAdjustmentsDetails();
        currentAdjustment.setNumberOfCartoons( newValue );
        currentAdjustment.setReasonCd( reasonCode );
        adjusted = true;
      }
    } 
    
    return adjusted;
  }
  
  private boolean conditionallyAdjustNumberOfLogos( OrderAdjustmentBodyItem bodyItem, 
                                                    long currentValue, 
                                                    long newValue,
                                                    long reasonCode )
  {
    boolean adjusted = false;
    
    boolean productConditionsMet = bodyItem.isRLS();
                                   
    if ( productConditionsMet )
    {
      OrderLicense originalDetails = bodyItem.getOriginalOrderDetails();
      boolean quantityConditionsMet = originalDetails.getNumberOfLogos() == currentValue;
      
      if( quantityConditionsMet )
      {
        PurchasablePermission currentAdjustment = bodyItem.getCurrentAdjustmentsDetails();
        currentAdjustment.setNumberOfLogos( newValue );
        currentAdjustment.setReasonCd( reasonCode );
        adjusted = true;
      }
    } 
    
    return adjusted;
  }
  
  private boolean conditionallyAdjustLicenseeFee(  OrderAdjustmentBodyItem bodyItem, 
                                                   double currentValue, 
                                                   double newValue,
                                                   long reasonCode )
  {
    //We don't need to perform product checkup here. Applicable to all.
    boolean adjusted = false;
    OrderLicense originalDetails = bodyItem.getOriginalOrderDetails();
    boolean quantityConditionsMet = originalDetails.getLicenseeFee() == currentValue;
    
    if( quantityConditionsMet )
    {
      PurchasablePermission currentAdjustment = bodyItem.getCurrentAdjustmentsDetails();
      currentAdjustment.setLicenseeFee( newValue );
      currentAdjustment.setReasonCd( reasonCode );
      adjusted = true;
    }
    
    return adjusted;
  }

  

  //////////////////////////////////////////////////////
  //Inner classes
  //////////////////////////////////////////////////////
  
  /**
   * Inner class used to group invoice and purchase summaries
   * for the containing <code>OrderAdjustment</code>.
   */
  public static class OrderAdjustmentSummaries
  {
    private OrderAdjustmentSummary _invoiceSummary;
    private OrderAdjustmentSummary _purchaseSummary;
    
    private OrderAdjustmentSummaries(){}
    
    private OrderAdjustmentSummaries( OrderAdjustmentSummary invoiceSummary,
                                      OrderAdjustmentSummary purchaseSummary )
    {
      if( invoiceSummary == null || purchaseSummary == null )
      {
        throw new IllegalArgumentException("Provided summaries cannot be null");
      }
      
      _invoiceSummary = invoiceSummary;
      _purchaseSummary = purchaseSummary;
    }

    /**
     * Returns the invoice summary contained by this instance 
     * of <code>OrderAdjustmentSummaries</code>
     */
    public OrderAdjustmentSummary getInvoiceSummary()
    {
      return _invoiceSummary;
    }
    
    /**
     * Returns the purchase summary contained by this instance 
     * of <code>OrderAdjustmentSummaries</code>
     */
    public OrderAdjustmentSummary getPurchaseSummary()
    {
      return _purchaseSummary;
    }
  }
  
}
