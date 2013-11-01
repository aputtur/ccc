package com.copyright.ccc.business.services.adjustment;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import com.copyright.ccc.CCRuntimeException;
import com.copyright.ccc.business.data.OrderLicenses;
import com.copyright.ccc.business.security.CCPrivilegeCode;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.ecommerce.PurchasablePermission;
import com.copyright.ccc.business.services.ecommerce.PurchasablePermissionFactory;
import com.copyright.ccc.business.services.order.OrderLicenseServices;
import com.copyright.ccc.business.services.order.OrderLicensesException;
import com.copyright.ccc.web.transaction.TransactionConstants;
import com.copyright.data.ValidationException;
import com.copyright.data.order.Cart;
import com.copyright.data.order.CartConstants;
import com.copyright.data.order.CartDescriptor;
import com.copyright.data.order.CartNotFoundException;
import com.copyright.data.order.CartStatusException;
import com.copyright.data.order.PermissionRequest;
import com.copyright.data.payment.AuthorizationException;
import com.copyright.service.order.CartServiceAPI;
import com.copyright.service.order.CartServiceFactory;
import com.copyright.service.order.CashierServiceAPI;
import com.copyright.service.order.CashierServiceFactory;


/**
 * Class that provides several order ajustment related services.
 */
public final class AdjustmentServices
{

  private static final boolean DO_NOT_PERFORM_CHECKOUT = false;
  private static final boolean PERFORM_CHECKOUT = true;

  private AdjustmentServices(){}

  /**
   * Creates and invoice <code>OrderAdjustment</code> for a given invoice ID.
   */
  public static OrderAdjustment createNewInvoiceAdjustment( String invoiceID )
  throws OrderLicensesException
  {
    UserContextService.checkPrivilege( CCPrivilegeCode.ENTER_ORDER_ADJUSTMENT );
    
    if( StringUtils.isEmpty( invoiceID ) )
    {
      throw new IllegalArgumentException( "A non-empty invoice ID must be provided" );
    }
    
    OrderAdjustment invoiceAdjustment = null;

    OrderLicenses orderLicensesInvoice = OrderLicenseServices.getOrderLicensesForAdjustment( OrderLicenseServices.INVOICE_ADJUSTMENT_LICENSES, invoiceID );
      
    invoiceAdjustment = OrderAdjustmentFactory.createNewInvoiceOrderAdjustment(invoiceID, orderLicensesInvoice);
    
    return invoiceAdjustment;
    
  }
  
  
  /**
   * Creates and purchase <code>OrderAdjustment</code> for a given purchase ID.
   */  
  public static OrderAdjustment createNewPurchaseAdjustment( String purchaseID )
  throws OrderLicensesException
  {
    UserContextService.checkPrivilege( CCPrivilegeCode.ENTER_ORDER_ADJUSTMENT );
    
    if( StringUtils.isEmpty( purchaseID ) )
    {
      throw new IllegalArgumentException( "A non-empty purchase ID must be provided" );
    }
    
    OrderAdjustment purchaseAdjustment = null;
    
    OrderLicenses orderLicensesPurchase = OrderLicenseServices.getOrderLicensesForAdjustment( OrderLicenseServices.PURCHASE_ADJUSTMENT_LICENSES, purchaseID );
      
    purchaseAdjustment = OrderAdjustmentFactory.createNewPurchaseOrderAdjustment( purchaseID, orderLicensesPurchase);
    
    return purchaseAdjustment;
  }
  
  
  /**
   * Creates and detail <code>OrderAdjustment</code> for a given detail ID.
   */
  public static OrderAdjustment createNewDetailAdjustment( String orderDetailID )
  throws OrderLicensesException
  {
    UserContextService.checkPrivilege( CCPrivilegeCode.ENTER_ORDER_ADJUSTMENT );
    
    if( StringUtils.isEmpty( orderDetailID ) )
    {
      throw new IllegalArgumentException( "A non-empty order detail ID must be provided" );
    }
    
    OrderAdjustment detailAdjustment = null;
  
    OrderLicenses orderLicensesPurchase = OrderLicenseServices.getOrderLicensesForAdjustment( OrderLicenseServices.DETAIL_ADJUSTMENT_LICENSES, orderDetailID );
      
    detailAdjustment = OrderAdjustmentFactory.createNewDetailOrderAdjustment( orderDetailID, orderLicensesPurchase);
    
    return detailAdjustment;
    
  }
  

  /**
   * Saves an instance of <code>OrderAdjustment</code> to be reproccessed eventually.
   */
  public static void saveAdjustment( OrderAdjustment orderAdjustment )
  throws ValidationException,
         AuthorizationException,
         CartStatusException,
         com.copyright.data.pricing.LimitsExceededException
  {
    UserContextService.checkPrivilege( CCPrivilegeCode.ENTER_ORDER_ADJUSTMENT );
    
    if( orderAdjustment == null )
    {
      throw new IllegalArgumentException( "An instance of OrderAdjustment must be provided" );
    }
    
    processAdjustment( orderAdjustment, DO_NOT_PERFORM_CHECKOUT );
  }
  
  
  /**
   * Fully processes and commits an instance of <code>OrderAdjustment</code> via checkout. 
   * Returns the confirmation number of such transaction, if successful.
   */
  public static long commitAdjustment( OrderAdjustment orderAdjustment )
  throws ValidationException,
         AuthorizationException,
         CartStatusException,
         com.copyright.data.pricing.LimitsExceededException
  {
    UserContextService.checkPrivilege( CCPrivilegeCode.COMMIT_ORDER_ADJUSTMENT );
    
    if( orderAdjustment == null )
    {
      throw new IllegalArgumentException( "An instance of OrderAdjustment must be provided" );
    }
    
    Long confirmationNumber = processAdjustment( orderAdjustment, PERFORM_CHECKOUT );
    
    if( confirmationNumber != null )
    {
      return confirmationNumber.longValue();
    }else
    {
      throw new CCRuntimeException( "Unable to get a valid confimation number for checkout process" );
    }
    
  }
    
  
  /**
   * Returns a <code>Map</code> containing <code>OrderAdjustmentDescriptor</code>s previously saved by the current user. 
   */
  public static Map<String,OrderAdjustmentDescriptor> getAdjustmentDescriptorsForUser( long userPartyID )
  {
    UserContextService.checkPrivilege( CCPrivilegeCode.ENTER_ORDER_ADJUSTMENT );
    
    CartServiceAPI cartService = CartServiceFactory.getInstance().getService();

    List<CartDescriptor> cartDescriptors = cartService.getCartDescriptorsForUser( userPartyID, CartConstants.ADJUSTMENT_ORDER_TYPE );

    Iterator<CartDescriptor> iterator = cartDescriptors.iterator();

    Map<String,OrderAdjustmentDescriptor> adjustmentDescriptors = 
    	new TreeMap<String,OrderAdjustmentDescriptor>( new OrderAdjustmentBodyItemComparator() );

    while ( iterator.hasNext() )
    {
      CartDescriptor cartDescriptor = iterator.next();
      OrderAdjustmentDescriptor adjustmentDescriptor = new OrderAdjustmentDescriptor(cartDescriptor);
      adjustmentDescriptors.put( String.valueOf( adjustmentDescriptor.getID() ), adjustmentDescriptor );
    }
    
    return adjustmentDescriptors;
  }
  
  
  /**
   * Returns an instance of <code>OrderAdjustment</code> based on the provided <code>OrderAdjustmentDescriptor</code>. 
   */
  public static OrderAdjustment retrieveAdjustment( OrderAdjustmentDescriptor adjustmentDescriptor )
  throws OrderLicensesException
  {
    UserContextService.checkPrivilege( CCPrivilegeCode.ENTER_ORDER_ADJUSTMENT );
    
    if( adjustmentDescriptor == null )
    {
      throw new IllegalArgumentException( "An instance of OrderAdjustmentDescriptor must be provided" );
    }

    OrderAdjustment orderAdjustment = AdjustmentServices.createOrderAdjustmentFromDescriptor(adjustmentDescriptor);
    
    return orderAdjustment;
  }
  
  
  /**
   * Deletes a previously saved adjustment based on the provided <code>OrderAdjustmentDescriptor</code> instance. 
   */
  public static void deleteSavedAdjustment( OrderAdjustmentDescriptor adjustmentDescriptor )
  throws OrderAdjustmentException
  {
    UserContextService.checkPrivilege( CCPrivilegeCode.ENTER_ORDER_ADJUSTMENT );
    
    if( adjustmentDescriptor == null )
    {
      throw new IllegalArgumentException("An instance of OrderAdjustmentDescriptor must be provided");
    }
    
    long cartIDToDelete = adjustmentDescriptor.getCartDescriptor().getID();
    
    try
    {
      getCartService().deleteAdjustmentCart( cartIDToDelete );
    }
    catch ( CartNotFoundException cnfe )
    {
      throw new OrderAdjustmentException( "Adjustment to be deleted was not found", cnfe );
    }
    
  }
  
  
  ///////////////////////////////////////////
  //Private methods
  ///////////////////////////////////////////
  
  private static Long processAdjustment( OrderAdjustment orderAdjustment, boolean performCheckout )
  throws ValidationException,
         AuthorizationException,
         com.copyright.data.pricing.LimitsExceededException,
         CartStatusException
  {
    Long confirmationNumber = null;

    Cart cart = getCartForAdjustment( orderAdjustment );
    
    Iterator<Map.Entry<String,OrderAdjustmentBodyItem>> iterator = orderAdjustment.getBody().entrySet().iterator();
    
    while( iterator.hasNext() )
    {
      Map.Entry<String,OrderAdjustmentBodyItem> entry = iterator.next();
      OrderAdjustmentBodyItem bodyItem = entry.getValue();
      
      if( !bodyItem.isAdjustable() )
      {
        //We do not want to add/update non adjustable items to/in cart
        continue;
      }
      
      PurchasablePermission currentAdjustmentDetails = bodyItem.getCurrentAdjustmentsDetails();
      PermissionRequest adjustment = PurchasablePermissionFactory.getPermissionRequest(currentAdjustmentDetails);
      
      //We want to process only non-zero adjustments
      if ( bodyItem.isCurrentAdjustmentModified() )
      {
        
        //TODO lalberione 05/17/2007: I don't believe we need to recalculate price here...
        //bodyItem.recalculateCurrentAdjustmentPrice();
        
        boolean adjustmentAlreadyInCart = getCartService().isRequestInCart(adjustment, cart);
                
        if( adjustmentAlreadyInCart )
        {
          cart = getCartService().updateRequest( adjustment, cart );
        }else
        {
          cart = getCartService().addToCart( adjustment, cart );
        }
        
        
      }else //Adjustment is zeroed
      {
        //If there is a PermissionRequest with the same ID in cart, remove it.
        //We don't want to persist zeroed (meaningless) adjustments.
        boolean zeroedAdjustmentInCart = getCartService().isRequestInCart( adjustment, cart );
        
        if ( zeroedAdjustmentInCart )
        {
            cart = getCartService().removeFromCart( adjustment, cart );
                       
            bodyItem.resetCurrentAdjustment();
        }
      }
          
    }
    
    if( performCheckout )
    {
     confirmationNumber = checkoutAdjustmentCart( cart );
    }
    
    return confirmationNumber;
    
  }
  
  
  private static Cart getCartForAdjustment( OrderAdjustment orderAdjustment )
  {
    Cart cart = null;
    
    if( orderAdjustment.hasBeenSaved() )
    {
      long adjustmentCartID = orderAdjustment.getCartID();
      cart = getCartService().getCartById( adjustmentCartID );
    }
    
    if( cart == null )
    {       
       long createUserPartyID = UserContextService.getSharedUser().getPartyId().longValue();
       
       cart = getCartService().getNewAdjustmentCart( TransactionConstants.CC_ORDER_SOURCE_CODE, 
                                                     createUserPartyID, 
                                                     orderAdjustment.getAdjustmentType(), 
                                                     orderAdjustment.getSourceID() );

      orderAdjustment.setCartID( cart.getID() );                                                     
    }
    
    return cart;
  }
  
  
  private static CartServiceAPI getCartService()
  {
    return CartServiceFactory.getInstance().getService();
  }
  
  
  private static long checkoutAdjustmentCart( Cart cart ) 
  throws ValidationException, AuthorizationException
  {
    CashierServiceAPI cashierService = CashierServiceFactory.getInstance().getService();
       
    long confirmationNumber = cashierService.checkoutCart( cart ).longValue();
    
    return confirmationNumber;
    
  }
  
  
  private static PermissionRequest findPermissionRequestByOrderDetailRefID( Cart cart, long orderDetailRefID )
  {
    Iterator<PermissionRequest> cartItemIterator = cart.getAllRequests().iterator();
    
    while( cartItemIterator.hasNext() )
    {
      PermissionRequest permissionRequest = cartItemIterator.next();
      
      boolean orderDetailRefIDsMatch = permissionRequest.getOrderDetailReferenceID() == orderDetailRefID;
      
      if( orderDetailRefIDsMatch )
      {
        return permissionRequest;
      }
      
    }
    return null;
  }
  
  
  private static OrderAdjustment transferCartInformationToOrderAdjustment( OrderAdjustment orderAdjustment, Cart cart )
  {
    orderAdjustment.setCartID( cart.getID() );
        
    Iterator<Map.Entry<String,OrderAdjustmentBodyItem>> bodyItemIterator = orderAdjustment.getBody().entrySet().iterator();
    
    while( bodyItemIterator.hasNext() )
    {
      Map.Entry<String,OrderAdjustmentBodyItem> entry = bodyItemIterator.next();
      OrderAdjustmentBodyItem bodyItem = entry.getValue();
      
      long detailID = bodyItem.getOriginalOrderDetails().getID();
      
      PermissionRequest permissionRequest = findPermissionRequestByOrderDetailRefID( cart, detailID );

      boolean permissionRequestFound = permissionRequest != null;
      
      if ( permissionRequestFound )
      {
        PurchasablePermission purchasablePermission = PurchasablePermissionFactory.createPurchasablePermission( permissionRequest );
        
        bodyItem.setCurrentAdjustmentsDetails( purchasablePermission );
      }
    }
   return orderAdjustment;
  }
  
  /**
   * Creates and invoice <code>OrderAdjustment</code> from an instance of <code>OrderAdjustmentDescriptor</code>.
   */
  private static OrderAdjustment createOrderAdjustmentFromDescriptor( OrderAdjustmentDescriptor descriptor ) 
  throws OrderLicensesException
  {
    OrderAdjustment adjustment = null;
    
    if( descriptor.isInvoiceAdjustment() )
    {
      String invoiceID = String.valueOf( descriptor.getCartDescriptor().getAdjustmentID() );
      
      adjustment = createNewInvoiceAdjustment( invoiceID );
    }
    
    if( descriptor.isPurchaseAdjustment() )
    {
      String purchaseID = String.valueOf( descriptor.getCartDescriptor().getAdjustmentID() );
      
      adjustment = createNewPurchaseAdjustment( purchaseID );
    }
    
    if( descriptor.isDetailAdjustment() )
    {
      String detailID = String.valueOf( descriptor.getCartDescriptor().getAdjustmentID() );
      
      adjustment = createNewDetailAdjustment( detailID );
    }
    
    boolean adjustmentCreated = adjustment != null;
    
    if( adjustmentCreated )
    {
      adjustment.setUserPartyID( descriptor.getCartDescriptor().getCreateUserPartyID() );
      adjustment.setCreateDate( descriptor.getCartDescriptor().getCreateDate() );
      adjustment.setCreateUser( String.valueOf( descriptor.getCartDescriptor().getCreateUserPartyID() ) );
      adjustment.setModifyDate( descriptor.getCartDescriptor().getLastUpdated() );
      adjustment.setModifyUser( String.valueOf( descriptor.getCartDescriptor().getLicenseePartyID() ) );
      
      long cartID = descriptor.getCartDescriptor().getID();
      
      Cart cart = getCartService().getCartById(cartID);
      
      boolean cartFound = cart != null;
      
      if( cartFound )
      {
        adjustment = transferCartInformationToOrderAdjustment( adjustment, cart );
      }
    }
    
    return adjustment;
  }
  
}
