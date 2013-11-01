package com.copyright.ccc.business.services.cart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.copyright.base.svc.ServiceRuntimeException;
import com.copyright.ccc.CCRuntimeException;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.ItemAvailabilityEnum;
import com.copyright.ccc.business.services.ItemConstants;
import com.copyright.ccc.business.services.ItemParmEnum;
import com.copyright.ccc.business.services.ProductEnum;
import com.copyright.ccc.business.services.RightSourceEnum;
import com.copyright.ccc.business.services.ServiceInvocationException;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.business.services.SystemStatus;
import com.copyright.ccc.business.services.rlnk.RlnkConstants;
import com.copyright.ccc.business.services.rlnk.RlnkRightsServices;
import com.copyright.ccc.business.services.user.UserServices;
import com.copyright.ccc.config.CC2Configuration;
import com.copyright.ccc.util.LogUtil;
import com.copyright.ccc.web.transaction.TransactionConstants;
import com.copyright.ccc.web.util.WebUtils;
import com.copyright.data.order.CartNotFoundException;
import com.copyright.data.order.CartStatusException;
import com.copyright.data.pricing.LimitsExceededException;
import com.copyright.rightslink.base.RepriceErrorTypeEnum;
import com.copyright.rightslink.base.data.CartResponse;
import com.copyright.rightslink.base.data.RepriceResponse;
import com.copyright.service.order.CartServiceFactory;
import com.copyright.svc.order.api.data.Bundle;
import com.copyright.svc.order.api.data.Cart;
import com.copyright.svc.order.api.data.Item;
import com.copyright.svc.order.api.data.ItemStatusQualifierEnum;
import com.copyright.svc.order.api.data.OrderConsumerContext;
import com.copyright.svc.order.api.data.OrderSource;
import com.copyright.svc.order.api.data.SpecialOrderTypeEnum;
import com.copyright.svc.rightsResolver.api.data.BootstrapRequest;
import com.copyright.svc.rightsResolver.api.data.ItemRequest;
import com.copyright.svc.rightsResolver.api.data.ItemRequestResponse;
import com.copyright.workbench.i18n.Money;


/**
 * Class containing a set of services responsible for shopping cart management.
 */
public final class CartServices
{
    private static final Logger LOGGER = Logger.getLogger( CartServices.class );
    private static final LimitsExceededException LIMITS_EXCEEDED_EXCEPTION_NOT_AVAILABLE = null;
    private static final String  SPECIAL_ORRDER_CART="specialOrder";
    private static final String  MIXED_CART="mixedCart";
    private static final String  CLEAN_CART="cleanCart";
    
    
  private CartServices()
  {
  }

  public static PurchasablePermission getPurchasablePermissionInCart(Cart cart,Long itemId)
  {
	if (cart != null) {
		List<Item> itemsInCart = getSortedCartItems(cart.getItems()); 
		
		 if( !itemsInCart.isEmpty() && itemsInCart!= null )
		  {
		   Iterator<Item> itemsIterator = itemsInCart.iterator();
		    
		    while( itemsIterator.hasNext() )
		    {
		      try
		      {
		        Item currentItem = itemsIterator.next();
		        if(currentItem.getItemId().compareTo(itemId)==0){
		                return PurchasablePermissionFactory.createPurchasablePermission( currentItem );
		        	
		        }
		      }
		      catch ( ClassCastException e )
		      {
		        LOGGER.error( "PurchasablePermissionFactory: could not cast to PermissionRequest " + LogUtil.appendableStack(e));
		        throw new UnableToBuildPurchasablePermissionException("PurchasablePermissionFactory: could not cast to PermissionRequest", e);
		      }
		    }
		  } 
		  

	}

	  return null;
  } 
  /**
     * Returns a <code>List</code> containing all the <code>PurchasablePermission</code>s
     * in the shopping cart.
     */
  public static List<PurchasablePermission> getPurchasablePermissionsInCart()
  {

	Cart cart = getCart();
	List<PurchasablePermission> purchasablePermissions = null;


	if (cart != null) {
		List<Item> itemsInCart =getSortedCartItems(cart.getItems()); 
		purchasablePermissions = PurchasablePermissionFactory.createPurchasablePermissions( itemsInCart);
	}

 	return purchasablePermissions;
  }
  public static RepriceResponse rePriceRightsLinkItem(PurchasablePermission purchasablePermission){
		ItemRequest itemRequest=PricingServices.getRLItemRequestFromPurchasablePermission(purchasablePermission);
		BootstrapRequest bootstrapRequest	=	new BootstrapRequest();
		bootstrapRequest.setItemRequest(itemRequest);
		bootstrapRequest.setSessionID(RlnkRightsServices.getRlnkSessionID());
		RepriceResponse repriceResponse =RlnkRightsServices.repriceOrder( bootstrapRequest);
		
		return repriceResponse;
	}
  /**
	 * Returns a <code>boolean</code> if there are only<code>RightsLink items</code>s
	 * in the shopping cart.
	 */
	public static boolean isCartOnlyRightsLink()
	{

	Cart cart = getCart();

	if (cart != null && !isCartEmpty() )
	{
		for (PurchasablePermission perm : getPurchasablePermissionsInCart()) {
			if (!perm.isRightsLink()) {
				return false;
			}
		}
		
		return true;
		
    }
	return false;
	}
	/**
	 * Returns a <code>List</code> containing all the Rightslink order <code>PurchasablePermission</code>s
	 * in the shopping cart.
	 */
	public static List<PurchasablePermission> getRightsLinkItemsInCart()
	{

	List<PurchasablePermission> rightsLinkItemsInCart = new ArrayList<PurchasablePermission>(0);

	Cart cart = getCart();

	if (cart != null && !isCartEmpty() )
	{
		for (PurchasablePermission perm : getPurchasablePermissionsInCart()) {
			if (perm.isRightsLink()) {
				rightsLinkItemsInCart.add(perm);
			}
		}
		
//		regularOrderItemsInCart = 
//	      PurchasablePermissionFactory.createPurchasablePermissions(cart.getRegularRequests());
	}

	LOGGER.debug("Returning " + rightsLinkItemsInCart.size() + 
	            " RightsLink item PurchasablePermissions");

	return rightsLinkItemsInCart;

	}
	
	
	/**
	 * Returns a <code>List</code> containing  Rightslink Special Permission <code>PurchasablePermission</code>s
	 * in the shopping cart.
	 */
	public static boolean hasRightsLinkSpecialPermissionItemsInCart(List<PurchasablePermission> rightslinkItems)
	{
		for (PurchasablePermission perm : rightslinkItems) {
			if (perm.getRightId()!=null && perm.getRightId().compareTo(2L)!=0) {
				return true;
			}
		}
	
	return false;

	}
	/**
	 * Returns a <code>List</code> containing  Rightslink Reprint Items  <code>PurchasablePermission</code>s
	 * in the shopping cart.
	 */
	public static boolean hasRightsLinkRePrintsItemsInCart(List<PurchasablePermission> rightslinkItems)
	{
		for (PurchasablePermission perm : rightslinkItems) {
			if (perm.getProductCd().equals(ProductEnum.RLR.name())){
				return true;
			}
		}
	return false;

	}
	/**
	 * Returns a <code>List</code> containing  Rightslink Reprint Items  <code>PurchasablePermission</code>s
	 * in the shopping cart.
	 */
	public static boolean hasRightsLinkRePrintsPreviewPDFItemsInCart(List<PurchasablePermission> rightslinkItems)
	{
		for (PurchasablePermission perm : rightslinkItems) {
			if (StringUtils.isNotEmpty(perm.getPreviewPDFUrl())){
				return true;
			}
		}
	return false;

	}
	 
/** Remove all invalid rights link cart items
 * 	
 */
	public static void  checkAndRemoveInvalidCartItems(){
		boolean errorInCart=false;
			for (PurchasablePermission perm : getRightsLinkItemsInCart()) {
				if(perm.getItem().getItemStatusQualifier()!=null && 
						perm.getItem().getItemStatusQualifier().equals(ItemStatusQualifierEnum.ERROR)){
					errorInCart=true;
					try {
						LOGGER.warn("removing item " + perm.getItem().getItemId() + " from cart");
						removeItemFromCart(perm);
					} catch (CannotBeRemovedFromCartException e) {
						LOGGER.error("item " + perm.getItem().getItemId() + " can't be removed from cart " + LogUtil.appendableStack(e));
					}
				}
			}
			if(errorInCart){
				resetCartPersistence();
			}
			
	}
	
	/* Returns a <code>List</code> containing all non rightslink <code>PurchasablePermission</code>s
	 * in the shopping cart.
	 */
	public static List<PurchasablePermission> getTFItemsInCart()
	{

	List<PurchasablePermission> nonRightsLinkItemsInCart = new ArrayList<PurchasablePermission>(0);

	Cart cart = getCart();

	if (cart != null && !isCartEmpty() )
	{
		for (PurchasablePermission perm : getPurchasablePermissionsInCart()) {
			if (!perm.isRightsLink()) {
				nonRightsLinkItemsInCart.add(perm);
			}
		}
		
//		regularOrderItemsInCart = 
//	      PurchasablePermissionFactory.createPurchasablePermissions(cart.getRegularRequests());
	}

	LOGGER.debug("Returning " + nonRightsLinkItemsInCart.size() + 
	            " RightsLink item PurchasablePermissions");

	return nonRightsLinkItemsInCart;

	}
	
	
  /**
   * Returns a <code>List</code> containing all the Academic order <code>PurchasablePermission</code>s
   * in the shopping cart.
   */
public static List<PurchasablePermission> getAcademicOrderItemsInCart()
{

  List<PurchasablePermission> academicOrderItemsInCart = new ArrayList<PurchasablePermission>(0);

  Cart cart = getExistingCart();

  if (cart != null && !isCartEmpty() )
  {
  	for (PurchasablePermission perm : getPurchasablePermissionsInCart()) {
  		if (perm.isAcademic()) {
  			academicOrderItemsInCart.add(perm);
  		}
  	}
  	
//  	regularOrderItemsInCart = 
//        PurchasablePermissionFactory.createPurchasablePermissions(cart.getRegularRequests());
  }

  LOGGER.debug("Returning " + academicOrderItemsInCart.size() + 
              " Academic order PurchasablePermissions");

  return academicOrderItemsInCart;

}
/**
 * Returns a <code>List</code> containing all the Academic order <code>PurchasablePermission</code>s
 * in the shopping cart.
 */
private static List<PurchasablePermission> getAcademicSpecialOrderItemsInCart()
{

List<PurchasablePermission> academicSpecialOrderItemsInCart = new ArrayList<PurchasablePermission>(0);

Cart cart = getCart();

if (cart != null && !isCartEmpty() )
{
	for (PurchasablePermission perm : getPurchasablePermissionsInCart()) {
		if (perm.isAcademic() && perm.isSpecialOrder()) {
			academicSpecialOrderItemsInCart.add(perm);
		}
	}
	
//	regularOrderItemsInCart = 
//      PurchasablePermissionFactory.createPurchasablePermissions(cart.getRegularRequests());
}

LOGGER.debug("Returning " + academicSpecialOrderItemsInCart.size() + 
            " Academic Special order PurchasablePermissions");

return academicSpecialOrderItemsInCart;

}

/**
 * Returns a <code>List</code> containing all the Academic order <code>PurchasablePermission</code>s
 * in the shopping cart.
 */
private static List<PurchasablePermission> getNonAcademicSpecialOrderItemsInCart()
{

List<PurchasablePermission> nonAcademicSpecialOrderItemsInCart = new ArrayList<PurchasablePermission>(0);

Cart cart = getCart();

if (cart != null && !isCartEmpty() )
{
	for (PurchasablePermission perm : getPurchasablePermissionsInCart()) {
		if (!perm.isAcademic() && perm.isSpecialOrder()) {
			nonAcademicSpecialOrderItemsInCart.add(perm);
		}
	}
	
//	regularOrderItemsInCart = 
//      PurchasablePermissionFactory.createPurchasablePermissions(cart.getRegularRequests());
}

LOGGER.debug("Returning " + nonAcademicSpecialOrderItemsInCart.size() + 
            " non Academic Special order PurchasablePermissions");

return nonAcademicSpecialOrderItemsInCart;

}
/**
 * Returns a <code>Number</code> of Academic order <code>PurchasablePermission</code>s
 * in the shopping cart.
 */
public static int getNumberOfAcademicOrderItemsInCart()
{

	List<PurchasablePermission> p = getAcademicOrderItemsInCart();
    
    return p.size();

}
private static int getNumberOfAcademicSpecialOrderItemsInCart()
{

	List<PurchasablePermission> p = getAcademicSpecialOrderItemsInCart();
    
    return p.size();

}

private static int getNumberOfNonAcademicSpecialOrderItemsInCart()
{

	List<PurchasablePermission> p = getNonAcademicSpecialOrderItemsInCart();
    
    return p.size();

}




/**
 * Returns a <code>Total Amount</code> for all the Academic Items in cart <code>PurchasablePermission</code>s
 * in the shopping cart.
 */
public static String getAcademicOrderItemsTotal()
{

List<PurchasablePermission> academicOrderItemsInCart = getAcademicOrderItemsInCart();

Iterator<PurchasablePermission> purchasablePermissionsIterator = 
	academicOrderItemsInCart.iterator();
  
BigDecimal academicTotalDecimal = new BigDecimal(0);;
Money academicCartTotal = ECommerceConstants.NO_ITEMS_IN_CART_AMOUNT;

while (purchasablePermissionsIterator.hasNext())
{
	PurchasablePermission currentPurchasablePermission = purchasablePermissionsIterator.next();
	
	if ( currentPurchasablePermission.getTotalPriceValue() != null)
	{
		academicTotalDecimal = academicTotalDecimal.add(currentPurchasablePermission.getTotalPriceValue());
	}
}

academicCartTotal = new Money(academicTotalDecimal.doubleValue());
String academicTotalInCart = WebUtils.formatMoney(academicCartTotal);

return academicTotalInCart;
}


/**
 * Returns a <code>List</code> containing all the Academic order <code>PurchasablePermission</code>s
 * in the shopping cart.
 */
public static List<PurchasablePermission> getNonAcademicOrderItemsInCart()
{

List<PurchasablePermission> nonAcademicOrderItemsInCart = new ArrayList<PurchasablePermission>(0);

Cart cart = getCart();

if (cart != null && !isCartEmpty() )
{
	for (PurchasablePermission perm : getPurchasablePermissionsInCart()) {
		if (!perm.isAcademic()) {
			nonAcademicOrderItemsInCart.add(perm);
		}
	}
	
//	regularOrderItemsInCart = 
//      PurchasablePermissionFactory.createPurchasablePermissions(cart.getRegularRequests());
}

LOGGER.debug("Returning " + nonAcademicOrderItemsInCart.size() + 
            " Academic order PurchasablePermissions");

return nonAcademicOrderItemsInCart;

}

/**
 * Returns a <code>Total Amount</code> for all the Non Academic Items in cart <code>PurchasablePermission</code>s
 * in the shopping cart.
 */
public static String getNonAcademicOrderItemsTotal()
{

List<PurchasablePermission> nonAcademicOrderItemsInCart = getNonAcademicOrderItemsInCart();

Iterator<PurchasablePermission> purchasablePermissionsIterator = 
	nonAcademicOrderItemsInCart.iterator();
  
BigDecimal nonAcademicTotalDecimal = new BigDecimal(0);
Money nonAcademicCartTotal = ECommerceConstants.NO_ITEMS_IN_CART_AMOUNT;

while (purchasablePermissionsIterator.hasNext())
{
	PurchasablePermission currentPurchasablePermission = purchasablePermissionsIterator.next();
	
	if ( currentPurchasablePermission.getTotalPriceValue() != null)
	{
		nonAcademicTotalDecimal = nonAcademicTotalDecimal.add(currentPurchasablePermission.getTotalPriceValue());
	}
	
}

nonAcademicCartTotal = new Money(nonAcademicTotalDecimal.doubleValue());
String nonAcademicTotalInCart = WebUtils.formatMoney(nonAcademicCartTotal);

return nonAcademicTotalInCart;
}
	

  /**
     * Returns a <code>List</code> containing all the regular order <code>PurchasablePermission</code>s
     * in the shopping cart.
     */
  public static List<PurchasablePermission> getRegularOrderItemsInCart()
  {

    List<PurchasablePermission> regularOrderItemsInCart = new ArrayList<PurchasablePermission>(0);

    Cart cart = getCart();

    if (cart != null && !isCartEmpty() )
    {
    	for (Item item : getSortedCartItems(cart.getItems())) {
    		if (item.getIsSpecialOrder() == false) {
    			regularOrderItemsInCart.add(PurchasablePermissionFactory.createPurchasablePermission(item));
    		}
    	}
    	
//    	regularOrderItemsInCart = 
//          PurchasablePermissionFactory.createPurchasablePermissions(cart.getRegularRequests());
    }

    LOGGER.debug("Returning " + regularOrderItemsInCart.size() + 
                " regular order PurchasablePermissions");

    return regularOrderItemsInCart;

  }
  private static List<PurchasablePermission> getRegularAcademicOrderItemsInCart()
  {

    List<PurchasablePermission> regularAcademicOrderItemsInCart = new ArrayList<PurchasablePermission>(0);

    for(PurchasablePermission item: getAcademicOrderItemsInCart()){
    	if(!item.isSpecialOrder()){
    		regularAcademicOrderItemsInCart.add(item);
    	}
    }
   
    return regularAcademicOrderItemsInCart;

  }
  private static List<PurchasablePermission> getRegularNonAcademicOrderItemsInCart()
  {

    List<PurchasablePermission> regularNonAcademicOrderItemsInCart = new ArrayList<PurchasablePermission>(0);

    for(PurchasablePermission item: getNonAcademicOrderItemsInCart()){
    	if(!item.isSpecialOrder()){
    		regularNonAcademicOrderItemsInCart.add(item);
    	}
    }
   
    return regularNonAcademicOrderItemsInCart;

  }
  /**
   * Returns a <code>List</code> containing all the special order <code>PurchasablePermission</code>s
   * in the shopping cart.
   */
  public static List<PurchasablePermission> getSpecialOrderItemsInCart()
  {

    List<PurchasablePermission> specialOrderItemsInCart = new ArrayList<PurchasablePermission>(0);

    Cart cart = getCart();

    if (cart != null && !isCartEmpty() )
    {
    	for (Item item : getSortedCartItems(cart.getItems())) {
    		if (item.getIsSpecialOrder() == true) {
    			specialOrderItemsInCart.add(PurchasablePermissionFactory.createPurchasablePermission(item));
    		}
    	}
    }
//    	specialOrderItemsInCart = 
//          PurchasablePermissionFactory.createPurchasablePermissions(cart.getSpecialOrderRequests());
   

    LOGGER.debug("Returning " + specialOrderItemsInCart.size() + 
                " special order PurchasablePermissions");

    return specialOrderItemsInCart;

  }

  /**
     * Returns the number of items in the shopping cart.
     */
  public static int getNumberOfItemsInCart()
  {

	  
    int numberOfItemsInCart = ECommerceConstants.NO_ITEMS_IN_CART_QUANTITY;
    boolean coiCartNotFound = false;
    
    if (isCartPresentInUserContext())
    {
      numberOfItemsInCart = getCartFromUserContext().getItems().size();
    }else
    {
      if( userHasLastCartID() )
      {
    	  try {
    		  numberOfItemsInCart = ServiceLocator.getCartService().getNumberOfCartItems(new OrderConsumerContext(), getUserLastCartId());
    	  } catch (ServiceRuntimeException svcOrderException ) {
    		  coiCartNotFound = true;
    		  LOGGER.error(LogUtil.getStack(svcOrderException));
    	  }
      }
    }

    if (coiCartNotFound) {
//    	Cart cart = getCart();
//    	numberOfItemsInCart = cart.getItems().size();
    	numberOfItemsInCart = 0;
    }
    
    return numberOfItemsInCart;
  }

  
 


  /**
     * Returns the number of special order items in the shopping cart.
     */
  private static int getNumberOfSpecialOrderItemsInCart()
  {
    List<PurchasablePermission> p = getSpecialOrderItemsInCart();
    
    return p.size();
  }
  
 
  /**
   * Returns the number of special order items in the shopping cart.
   */
private static int getNumberOfPricedItemsInCart()
{
  int numberOfPricedItems = 0;
	
  for (PurchasablePermission purchasablePermission : getPurchasablePermissionsInCart()) {
	  if (purchasablePermission.getRightSourceCd().equals(RightSourceEnum.RL.name())) {
		  if (purchasablePermission.getProductCd().equals(ProductEnum.RL.name())) {
			  if (purchasablePermission.getSpecialOrderTypeCd().equals(SpecialOrderTypeEnum.NOT_SPECIAL_ORDER.name())) {
				  numberOfPricedItems++;
			  }
		  } else if (purchasablePermission.getProductCd().equals(ProductEnum.RLR.name())) {
			  if (purchasablePermission.getPriceValueRaw() != ItemConstants.RL_NOT_PRICED ) {
				  numberOfPricedItems++;
			  }			  
		  }
	  } else if (purchasablePermission.getRightSourceCd().equals(RightSourceEnum.TF.name())) {
		  if (purchasablePermission.getSpecialOrderTypeCd().equals(SpecialOrderTypeEnum.NOT_SPECIAL_ORDER.name())) {
			  numberOfPricedItems++;
		  }
	  }	  
  }
  return numberOfPricedItems;
}

private static int getNumberOfPricedAcademicItemsInCart()
{
  int numberOfPricedItems = 0;
	
  for (PurchasablePermission purchasablePermission : getPurchasablePermissionsInCart()) {
	  if(purchasablePermission.isAcademic()){
			  if (purchasablePermission.getSpecialOrderTypeCd().equals(SpecialOrderTypeEnum.NOT_SPECIAL_ORDER.name())) {
				  numberOfPricedItems++;
			  }
  
	  }
  
  }
  return numberOfPricedItems;
}

private static int getNumberOfPricedNonAcademicItemsInCart()
{
  int numberOfPricedItems = 0;
	
  for (PurchasablePermission purchasablePermission : getPurchasablePermissionsInCart()) {
	  if(!purchasablePermission.isAcademic()){
		  if (purchasablePermission.getRightSourceCd().equals(RightSourceEnum.RL.name())) {
			  if (purchasablePermission.getProductCd().equals(ProductEnum.RL.name())) {
				  if (purchasablePermission.getSpecialOrderTypeCd().equals(SpecialOrderTypeEnum.NOT_SPECIAL_ORDER.name())) {
					  numberOfPricedItems++;
				  }
			  } else if (purchasablePermission.getProductCd().equals(ProductEnum.RLR.name())) {
				  if (purchasablePermission.getPriceValueRaw() != ItemConstants.RL_NOT_PRICED ) {
					  numberOfPricedItems++;
				  }			  
			  }
		  } else if (purchasablePermission.getRightSourceCd().equals(RightSourceEnum.TF.name())) {
			  if (purchasablePermission.getSpecialOrderTypeCd().equals(SpecialOrderTypeEnum.NOT_SPECIAL_ORDER.name())) {
				  numberOfPricedItems++;
			  }
		  }  
	  }
  
  }
  return numberOfPricedItems;
}
public static int getNumberOfChargeItemsInCart()
{
  int numberOfChargeItems = 0;
	
  for (PurchasablePermission purchasablePermission : getPurchasablePermissionsInCart()) {
	  if (purchasablePermission.getRightSourceCd().equals(RightSourceEnum.RL.name())) {
		  if (purchasablePermission.getProductCd().equals(ProductEnum.RL.name())) {
			  if (purchasablePermission.getSpecialOrderTypeCd().equals(SpecialOrderTypeEnum.NOT_SPECIAL_ORDER.name())) {
				  numberOfChargeItems++;
			  }
		  } 
	  } else if (purchasablePermission.getRightSourceCd().equals(RightSourceEnum.TF.name())) {
		  if (purchasablePermission.getSpecialOrderTypeCd().equals(SpecialOrderTypeEnum.NOT_SPECIAL_ORDER.name())) {
			  numberOfChargeItems++;
		  }
	  }	  
  }
  return numberOfChargeItems;
}

public static boolean containsSpecialOrders()
{
	if (getNumberOfSpecialOrderItemsInCart() > 0){
		return Boolean.TRUE;
	}
	 return Boolean.FALSE;
 
}

public static boolean containsAcademicSpecialOrders()
{
	if (getNumberOfAcademicSpecialOrderItemsInCart() > 0){
		return Boolean.TRUE;
	}
	 return Boolean.FALSE;
 
}
public static boolean containsNonAcademicSpecialOrders()
{if (getNumberOfNonAcademicSpecialOrderItemsInCart() > 0){
	return Boolean.TRUE;
}
 return Boolean.FALSE;
}


private static  boolean containsNonPricedNonAcademicItems(){
	for(PurchasablePermission aPerm:getNonAcademicOrderItemsInCart()){
		if(aPerm.getPrice()==ItemConstants.COST_TBD){
			return Boolean.TRUE;
		}
	}
	return Boolean.FALSE;
}

public static  boolean containsPricedSpecialOrderItem(){
	for(PurchasablePermission aPerm:getNonAcademicOrderItemsInCart()){
		if(aPerm.isSpecialOrder() && aPerm.getPrice()!=ItemConstants.COST_TBD){
			return Boolean.TRUE;
		}
	}
	return Boolean.FALSE;
}
public static  boolean containsNonPricedRightsLnkItem(){
	for(PurchasablePermission aPerm:getNonAcademicOrderItemsInCart()){
		if(aPerm.getPrice()==ItemConstants.COST_TBD){
			return Boolean.TRUE;
		}
	}
	return Boolean.FALSE;
}
public static  boolean containsChargeItems(){
	 if (CartServices.getNumberOfChargeItemsInCart() > 0) {
		 return Boolean.TRUE;
     }
     else {
    	 return Boolean.FALSE;
     }
}




  /**
   * Returns the number of regular order items in the shopping cart.
   */
  public static int getNumberOfRegularOrderItemsInCart()
  {

	    List<PurchasablePermission> p = getRegularOrderItemsInCart();
	    
	    return p.size();
  
  }
  private static int getNumberOfRegularAcademicOrderItemsInCart()
  {

	    List<PurchasablePermission> p = getRegularAcademicOrderItemsInCart();
	    
	    return p.size();
 
  }
  private static int getNumberOfRegularNonAcademicOrderItemsInCart()
  {

	    List<PurchasablePermission> p = getRegularNonAcademicOrderItemsInCart();
	    
	    return p.size();
 
  }

  /**
   * Returns the total fees for the shopping cart (e.g. $ 12.34).
   */
  public static String getCartTotal()
  {

    Money cartTotal = ECommerceConstants.NO_ITEMS_IN_CART_AMOUNT;
    BigDecimal cartTotalDecimal;
    
    if (isCartPresentInUserContext())
    {
//      cartTotal = getCartFromUserContext().getTotalFees();
    	cartTotalDecimal = getTotalPrice(getCartFromUserContext());
    	cartTotal = new Money(cartTotalDecimal.doubleValue());
    }
    
    String totalInCart = WebUtils.formatMoney(cartTotal);

    LOGGER.debug("Returning total in shopping cart " + totalInCart);

    return totalInCart;
  }
  
  public static String getCartChargeTotal()
  {

    Money cartTotal = ECommerceConstants.NO_ITEMS_IN_CART_AMOUNT;
    BigDecimal cartTotalDecimal;
    
    if (isCartPresentInUserContext())
    {
//      cartTotal = getCartFromUserContext().getTotalFees();
    	cartTotalDecimal = getTotalChargePrice(getCartFromUserContext());
    	cartTotal = new Money(cartTotalDecimal.doubleValue());
    }
    
    String totalInCart = WebUtils.formatMoney(cartTotal);

    LOGGER.debug("Returning total in shopping cart " + totalInCart);

    return totalInCart;
  }
  

    /**
     * Updates the cart in the database, and refreshes the cart
     * in the user context with the DB-backed cart.
     * 
     * @param cartContainingUpdates The cart containing updates.
     * @return The latest (updated) copy of the cart.
     */
    public static Cart updateCart( Cart cartContainingUpdates ){

    	Cart cart = ServiceLocator.getCartService().updateCart(
    	        new OrderConsumerContext(), 
    	        cartContainingUpdates);
    	    
    	assignCartToUserContext(cart);
    	
    	return ( getCart() );
    }
    /**
     * Add an instance of <code>PurchasablePermission</code> to the shopping cart.
     */
    public static Cart addItemToCart(PurchasablePermission purchasablePermission) 
    throws CourseNotDefinedException, 
    	   CannotBeAddedToCartException
    {
        if (purchasablePermission == null)
        {
            LOGGER.error("No PurchasablePermission found");
            throw new IllegalArgumentException("PurchasablePermission should not be null, ever.");
        }
        
        
        // if rightslink item comes into this path redirect
        if(purchasablePermission.isRightsLink()){
        	return addRightslnkItemToCart(purchasablePermission);
        }
        
        
        if (mustCollectCourseDetails(purchasablePermission))
        {
            LOGGER.debug("Course details must be provided before adding item to shopping cart");
            throw new CourseNotDefinedException(purchasablePermission);
        }
        
        
        Cart cart = getCart();
            
            String title = purchasablePermission.getPublicationTitle();
    
            if (title != null && (title.length() > 255)) {
                StringBuffer obuf = new StringBuffer();
                obuf.append("\n\nTruncating title prior to adding to cart.\n\n");
                obuf.append("title length: ").append(title.length()).append("\n\n");
                obuf.append(title).append("\n\n");

                LOGGER.warn(obuf.toString());

                title = title.substring(0, 230) + "[...]";
                purchasablePermission.setPublicationTitle(title);
                
                obuf = new StringBuffer();
                obuf.append("New Length: 235").append("\n\n");
                obuf.append(title).append("\n\n");
                LOGGER.warn(obuf.toString());
            }
            

            	cart = ServiceLocator.getCartService().addItemToCart(new OrderConsumerContext(), cart.getCartId(), purchasablePermission.getItem());
                LOGGER.debug("PurchasablePermission added to shopping cart (ID " + 
                        cart.getCartId() + ") successfully - Item ID: " + 
                        purchasablePermission.getItem().getItemId());
                assignCartToUserContext(cart);
       
        return cart;
        
    }
    public static Cart addRightslnkItemToCart(PurchasablePermission purchasablePermission) 
    throws   CannotBeAddedToCartException
    {
        if (purchasablePermission == null)
        {
            LOGGER.error("No PurchasablePermission found");
            throw new IllegalArgumentException("PurchasablePermission should not be null, ever.");
        }


        Cart cart = getCart();
        
//        try
//        {
//            PermissionRequest permissionRequest = 
//                purchasablePermission.getPermissionRequest();

//            CartServiceAPI cartService = getCartServiceAPI();
        
            //  2010-01-07  MSJ
            //  Anything with unicode and/or 255 or above in length
            //  doth wreaketh havoc on oracle.
            
            String title = purchasablePermission.getPublicationTitle();
    
            if (title != null && (title.length() > 255)) {
                StringBuffer obuf = new StringBuffer();
                obuf.append("\n\nTruncating title prior to adding to cart.\n\n");
                obuf.append("title length: ").append(title.length()).append("\n\n");
                obuf.append(title).append("\n\n");

                LOGGER.warn(obuf.toString());

                title = title.substring(0, 230) + "[...]";
                purchasablePermission.setPublicationTitle(title);
                
                obuf = new StringBuffer();
                obuf.append("New Length: 235").append("\n\n");
                obuf.append(title).append("\n\n");
                LOGGER.warn(obuf.toString());
            }
            
            if(purchasablePermission.isRightsLink()){
            	// add item to cart with invalid flag
                 purchasablePermission.getItem().setItemStatusQualifier(ItemStatusQualifierEnum.ERROR);
                 
                 cart = ServiceLocator.getCartService().addItemToCart(new OrderConsumerContext(), cart.getCartId(), purchasablePermission.getItem());
                assignCartToUserContext(cart);
                
            	PurchasablePermission newRightslnkPurchasablePermission=null;
            	Long maxItemID=0L;
            	// get highest rightslink purchasable item
            	for (PurchasablePermission perm : getRightsLinkItemsInCart()) {
            		if(perm.getItem().getItemId()>maxItemID){
            			maxItemID=perm.getItem().getItemId();
            			newRightslnkPurchasablePermission=perm;
            		}
            	}
            	// now add to rightslink cart
           	 	if(newRightslnkPurchasablePermission!=null){
           	 		ItemRequest itemRequest=PricingServices.getRLItemRequestFromPurchasablePermission(newRightslnkPurchasablePermission);
           	 		
           	 		//	CartResponse cartResponse=
           	 		CartResponse cartResponse=RlnkRightsServices.addCartItem( newRightslnkPurchasablePermission.getItem().getItemId(),  itemRequest);
           	 			if(cartResponse.isExceptionOccurred()){ /// remove coi cart
           	 				try {
								LOGGER.error("cannot add item to cart " + LogUtil.appendableStack(cartResponse.getThrowable()) );           	 					
								removeItemFromCart(newRightslnkPurchasablePermission);								
							} catch (CannotBeRemovedFromCartException e) {
								LOGGER.error("cannot remove item from cart " + LogUtil.appendableStack(e) );
								throw new CannotBeAddedToCartException(newRightslnkPurchasablePermission);								
							}
           	 			}else{
           	 				if(cartResponse.isWorkIDUpdated()){
           	 					
           	 					newRightslnkPurchasablePermission.setWorkId(Long.toString(cartResponse.getWorkID()));
           	 					newRightslnkPurchasablePermission.setWhichWork(cartResponse.getWhichWork());
           	 					
           	 				}
           	 			
           	 			    newRightslnkPurchasablePermission.getItem().setItemStatusQualifier(null);
		        			cart =ServiceLocator.getCartService().updateItemInCart(new OrderConsumerContext(), cart.getCartId(), newRightslnkPurchasablePermission.getItem());
		        			assignCartToUserContext(cart);
		        			// update will add cart to user context
           	 		      LOGGER.debug("PurchasablePermission added to Rights Link  cart (ID " + 
                                  cart.getCartId() + ") successfully - Item ID: " + 
                                  newRightslnkPurchasablePermission.getItem().getItemId());
           	 			}
           	 		
           	 	 }else{
           	 	 LOGGER.debug("PurchasablePermission failed to added to Rights Link  cart (ID " + 
                         cart.getCartId() + ") ");
           	 	 }
            }
       
        

        return  getCart();
        
    }

  /**
    * Removes an instance of <code>PurchasablePermission</code> from the shopping cart.
    */
  public static void removeItemFromCart(PurchasablePermission purchasablePermission) throws CannotBeRemovedFromCartException
  {

    if (purchasablePermission == null)
    {
      LOGGER.error("No PurchasablePermission found");

      throw new IllegalArgumentException("PurchasablePermission should not be null");
    }

    boolean cannotBeRemovedFromCart = 
      isCartEmpty() || isNotSuitableForTransaction(purchasablePermission);

    if (cannotBeRemovedFromCart)
    {
      LOGGER.warn("PurchasablePermission ( Item ID " + 
                  purchasablePermission.getItem().getItemId() + 
                  " ) could not be removed from shopping cart");

      throw new CannotBeRemovedFromCartException(purchasablePermission);
    }

    Cart cart = getCart();
    Cart newCart = null;

//    PermissionRequest permissionRequest = purchasablePermission.getPermissionRequest();

    try
    {
//      cart = getCartServiceAPI().removeFromCart(permissionRequest, cart);
    	
    	Long cartItemId=purchasablePermission.getItem().getItemId();
    	
        if(purchasablePermission.isRightsLink()){
        	purchasablePermission.getItem().setItemStatusQualifier(ItemStatusQualifierEnum.ERROR);
        	newCart=ServiceLocator.getCartService().removeItemFromCart(new OrderConsumerContext(),cart.getCartId(), purchasablePermission.getItem().getItemId());
        	RlnkRightsServices.deleteCartItem(cartItemId);
        }
        else{
        	newCart=ServiceLocator.getCartService().removeItemFromCart(new OrderConsumerContext(),cart.getCartId(), purchasablePermission.getItem().getItemId());
        }
        	
    	
    	
    	LOGGER.debug("PurchasablePermission removed from shopping cart ( ID " + 
                  cart.getCartId() + " ) successfully - ITem ID: " + 
                  purchasablePermission.getItem().getItemId());

      assignCartToUserContext(newCart);
    } catch ( CartStatusException cartStatusException ) 
    {
	  LOGGER.error(LogUtil.getStack(cartStatusException) );    	
      resetCartPersistence();
      throw new CartNotAvailableException( cartStatusException, cart.getCartId() );
      
    }
    
    //boolean isEmptyAcademicCart = newCart.getItems().size() == 0 && isAcademicCart();
    //boolean isEmptyAcademicCart = newCart.getItems().size() == 0 && purchasablePermission.isAcademic();
    boolean isEmptyAcademicCart = !isAcademicCart() && purchasablePermission.isAcademic() ;
    
    //Remove course information when removing last academic item from cart.
    if( isEmptyAcademicCart )
    {
      CartServices.resetCourseDetails();
    }
    
  }
  
  
  /**
   * Updates the cart in the database, and refreshes the cart
   * in the user context with the DB-backed cart.
   * 
   * @param cartContainingUpdates The cart containing updates.
   * @return The latest (updated) copy of the cart.
   */
  public static void updateRightslnkCart(PurchasablePermission purchasablePermission ){

	  if (purchasablePermission == null)
	    {
	      LOGGER.error("No PurchasablePermission found");

	      throw new IllegalArgumentException("PurchasablePermission should not be null");
	    }

	    boolean cannotBeUpdated = 
	      isNotSuitableForTransaction(purchasablePermission);

	    if (cannotBeUpdated)
	    {
	      LOGGER.error("PurchasablePermission ( Item ID " + 
	    		  purchasablePermission.getItem().getItemId() + 
	                   " ) cannot be updated in shopping cart; not compatible with shopping cart type");

	      throw new CCRuntimeException("PurchasablePermission cannot be updated in shopping cart. Not compatible with shopping cart type");
	    }
  	    
	    Cart cart = getCart();
	    
 
 		
          // invalidate cart
    	purchasablePermission.getItem().setItemStatusQualifier(ItemStatusQualifierEnum.ERROR);
		cart = ServiceLocator.getCartService().updateItemInCart(new OrderConsumerContext(), cart.getCartId(), purchasablePermission.getItem());
		assignCartToUserContext(cart);
		// get updated purchasable item from the cart
		PurchasablePermission updatedpurchasablePermission= getPurchasablePermissionInCart(cart,purchasablePermission.getItem().getItemId());
				 		
		//Update Rightslnk Cart		 			
		ItemRequest itemRequest=PricingServices.getRLItemRequestFromPurchasablePermission(updatedpurchasablePermission);
		CartResponse cartResponse= RlnkRightsServices.updateCartItem(updatedpurchasablePermission.getItem().getItemId(),  itemRequest);
		
					  		
				
		// if exception remove the COI  cart item
		if(cartResponse.isExceptionOccurred()){ /// remove coi cart
				try {
					removeItemFromCart(updatedpurchasablePermission);
					} catch (CannotBeRemovedFromCartException e) {
						throw new CartStatusException(ExceptionUtils.getFullStackTrace(e)); 
					}
			}else{
					// validate coi cart item
	 				if(cartResponse.isWorkIDUpdated()){
   	 					
	 					updatedpurchasablePermission.setWorkId(Long.toString(cartResponse.getWorkID()));
	 					updatedpurchasablePermission.setWhichWork(cartResponse.getWhichWork());
   	 					
   	 				}
				
					
				updatedpurchasablePermission.getItem().setItemStatusQualifier(null);
		  		cart = ServiceLocator.getCartService().updateItemInCart(new OrderConsumerContext(), cart.getCartId(), updatedpurchasablePermission.getItem());
		  		assignCartToUserContext(cart);
			}
		
		 

	      LOGGER.debug("PurchasablePermission ( Item ID " + 
	                  purchasablePermission.getItem().getItemId() + 
	                  " )  updated in shopping cart ( ID " + cart.getCartId() + 
	                  " ) successfully");
  }
  

  /**
   * Updates an instance of <code>PurchasablePermission</code> in the shopping cart.
   */
  public static void updateCartItem(PurchasablePermission purchasablePermission) 
  //		throws InvalidAttributesException,
  //		 	   DeniedLimitsExceededException, 
  //             SpecialOrderLimitsExceededException, 
  //             ContactRHDirectlyLimitsExceededException, 
  //             ChangedToRegularOrderException
  {
    if (purchasablePermission == null)
    {
      LOGGER.error("No PurchasablePermission found");

      throw new IllegalArgumentException("PurchasablePermission should not be null");
    }

    boolean cannotBeUpdated = 
      isNotSuitableForTransaction(purchasablePermission);

    if (cannotBeUpdated)
    {
      LOGGER.error("PurchasablePermission ( Item ID " + 
    		  purchasablePermission.getItem().getItemId() + 
                   " ) cannot be updated in shopping cart; not compatible with shopping cart type");

      throw new CCRuntimeException("PurchasablePermission cannot be updated in shopping cart. Not compatible with shopping cart type");
    }
//    if(!purchasablePermission.isRightsLink()){
//	    if ( changedFromSpecialOrderToRegularOrder(purchasablePermission) )
//	    {
//	      LOGGER.debug("PurchasablePermission ( Item ID " + 
//	                  purchasablePermission.getItem().getItemId() + 
//	                  " ) changed from Special Order to Regular Order");
	
	      //As per CC2 requirements, do not perform transaction.
//	      throw new ChangedToRegularOrderException(purchasablePermission);
//	    }
	
//	    if ( changedFromRegularOrderToSpecialOrder( purchasablePermission ) )
//	    {
	
//	      LOGGER.debug("PurchasablePermission ( Item ID " + 
//	    		  purchasablePermission.getItem().getItemId() + 
//	                  " ) changed from Regular Order to Special Order");
	
	      //As per CC2 requirements, do not perform transaction.
//	      throw new SpecialOrderLimitsExceededException( purchasablePermission, 
//	                                                     LIMITS_EXCEEDED_EXCEPTION_NOT_AVAILABLE );
//	    }
//   }


 
    
    Cart cart = getCart();

//    try
//    {

//      PermissionRequest permissionRequest = 
//        purchasablePermission.getPermissionRequest();

//      cart = getCartServiceAPI().updateRequest(permissionRequest, cart);
    
      
 	 if(purchasablePermission.isRightsLink()){
 		 			updateRightslnkCart(purchasablePermission);
	      }
 	 else{
 		cart = ServiceLocator.getCartService().updateItemInCart(new OrderConsumerContext(), cart.getCartId(), purchasablePermission.getItem()); 
 	 }
      
      assignCartToUserContext(cart);

      LOGGER.debug("PurchasablePermission ( Item ID " + 
                  purchasablePermission.getItem().getItemId() + 
                  " )  updated in shopping cart ( ID " + cart.getCartId() + 
                  " ) successfully");

       
//      LOGGER.debug("Items after update: " + cart.getItems().size());
//      for (Item item : cart.getItems()) {
//          LOGGER.debug("Item " + item.getItemId());    	  
//          LOGGER.debug("External Tou Id " + item.getExternalTouId());    	  
//      }
           
  }


  /**
    * Cascade-updates all the instance of <code>PurchasablePermission</code> in the shopping cart.
    */
  public static void cascadeUpdateCartItems( int numberOfStudents ) throws CascadeUpdateException
  {

    if (numberOfStudents <= 0)
    {
      LOGGER.error("Number of students should be greater than zero");

      throw new IllegalArgumentException("Number of students should be greater than zero");
    }

    boolean cascadeUpdateCannotBePerformed = 
      isNonAcademicCart() || isCartEmpty();

    if ( cascadeUpdateCannotBePerformed )
    {
      LOGGER.error("PurchasablePermission cannot be updated in shopping cart; not compatible with shopping cart type");

      throw new CCRuntimeException("PurchasablePermission cannot be updated in shopping cart. Not compatible with shopping cart type");
    }

    List<IndexedECommerceExceptionWrapper> deniedLimitsExceededExceptions = new ArrayList<IndexedECommerceExceptionWrapper>(0);
    List<IndexedECommerceExceptionWrapper> specialOrderLimitsExceededExceptions = new ArrayList<IndexedECommerceExceptionWrapper>(0);
    List<IndexedECommerceExceptionWrapper> contactRHDirectlyLimitsExceededExceptions = new ArrayList<IndexedECommerceExceptionWrapper>(0);
    List<IndexedECommerceExceptionWrapper> invalidAttributesExceptions = new ArrayList<IndexedECommerceExceptionWrapper>(0);
    List<IndexedECommerceExceptionWrapper> changedToRegularOrderExceptions = new ArrayList<IndexedECommerceExceptionWrapper>(0);

    List<PurchasablePermission> purchasablePermissions = getPurchasablePermissionsInCart();

    Iterator<PurchasablePermission> purchasablePermissionsIterator = 
      purchasablePermissions.iterator();
      
    int purchasablePermissionsIndex = 1;

    while (purchasablePermissionsIterator.hasNext())
    {
      PurchasablePermission currentPurchasablePermission = purchasablePermissionsIterator.next();
  
      	if (currentPurchasablePermission.isAcademic()) {
      		currentPurchasablePermission.setNumberOfStudents( numberOfStudents );
      		CartServices.updateCartItem( currentPurchasablePermission );
      	}
      	
   		purchasablePermissionsIndex++;
      	
    }

    boolean exceptionsPresent = !deniedLimitsExceededExceptions.isEmpty() || 
                                !specialOrderLimitsExceededExceptions.isEmpty() || 
                                !invalidAttributesExceptions.isEmpty() || 
                                !changedToRegularOrderExceptions.isEmpty();

    if ( exceptionsPresent )
    {
      LOGGER.warn("There were some errors performing cascade update to shopping cart");

      throw new CascadeUpdateException(invalidAttributesExceptions, 
                                       deniedLimitsExceededExceptions, 
                                       specialOrderLimitsExceededExceptions, 
                                       contactRHDirectlyLimitsExceededExceptions, 
                                       changedToRegularOrderExceptions);
    }

    LOGGER.debug("Cascade update applied to shopping cart successfully");
        
  }


  /**
   * Empties the shopping cart.
   */
  public static void emptyCart()
  {

    if (isCartPresentInUserContext())
    {
      Cart cart = getCartFromUserContext();
      
      // first clean up any rights link cart ignore errors
      for(PurchasablePermission p:getRightsLinkItemsInCart()){
    	  	RlnkRightsServices.deleteCartItem(p.getItem().getItemId());
      }
    

      try
      {
//        cart = getCartServiceAPI().emptyCart( cart );
    	  boolean deleteBundles = true;
    	  cart = ServiceLocator.getCartService().emptyCart(new OrderConsumerContext(), cart.getCartId(), deleteBundles);
        assignCartToUserContext( cart );
      }
      catch ( CartStatusException cartStatusException ) 
      {
		LOGGER.error(LogUtil.getStack(cartStatusException) );    	  
        resetCartPersistence();
        throw new CartNotAvailableException( cartStatusException, cart.getCartId() );
      }
      
    }

  }
  
  /**
   * Determines if the current shopping cart is academic.
   */
  public static boolean isAcademicCart()
  {
	  for (Object p : getPurchasablePermissionsInCart()) {
		PurchasablePermission purchasablePermission = (PurchasablePermission) p;
		if (purchasablePermission.isAcademic() ) {	
	  		return true;
	  	}
	  }
	  return false;
  }

  /**
   * Determines if the current shopping cart is non-academic.
   */
  private static boolean isNonAcademicCart()
  {
    return !isAcademicCart();
  }


  private static boolean isBundleExists () {

	  Set<Bundle> bundles = getCart().getBundles();
	  
	  if (bundles == null) {
		  return false;
	  }
	  if (bundles.size() > 0) {
		  return true;
	  } else {
		  return false;
	  }
	  
  }

  public static CourseDetails getCourseDetails () {

	  Bundle bundle = null;
	  CourseDetails courseDetails;
	  Set<Bundle> bundles = getCart().getBundles();

	  if (bundles == null) {
		  return new CourseDetailsImpl();
	  }
	  if (bundles.size() == 0) {
		  return new CourseDetailsImpl();
	  } else if (bundles.size() == 1){
		  Iterator<Bundle> bundlesIterator = bundles.iterator();
		  if (bundlesIterator.hasNext()) {
			  bundle = bundlesIterator.next();
			  return new CourseDetailsImpl(bundle);
		  }
	  } else {
		throw new IllegalArgumentException("More than one bundle exists for cart");
	  }
	  return null;
  }  
  
  public static int getNumberOfStudentsFromCourseDetails () {

	  Bundle bundle = null;
	  Cart cart = getExistingCart();
	  
	  if (cart == null) {
		  return 0;
	  }
	  
	  Set<Bundle> bundles = getCart().getBundles();

	  if (bundles == null) {
		  return 0;
	  }
	  if (bundles.size() == 0) {
		  return 0;
	  } else if (bundles.size() == 1){
		  Iterator<Bundle> bundlesIterator = bundles.iterator();
		  if (bundlesIterator.hasNext()) {
			  bundle = bundlesIterator.next();
			  CourseDetailsImpl courseDetailsImpl = new CourseDetailsImpl(bundle);
			  return courseDetailsImpl.getNumberOfStudents();
		  }
	  } else {
		throw new IllegalArgumentException("More than one bundle exists for cart");
	  }
	  return 0;
  }  
   
  
  public static CourseDetails setCourseDetails (CourseDetails courseDetails ) {

	   if (courseDetails == null)
	   {
	     LOGGER.error("No CourseDetails found");

	     throw new IllegalArgumentException("CourseDetails should not be null");
	   }

	  Cart cart = getCart();
	  Set<Bundle> bundles = cart.getBundles();  
	  CourseDetailsImpl courseDetailsImpl = (CourseDetailsImpl) courseDetails;
	  Bundle bundle = courseDetailsImpl.getBundle();
	  
	  boolean courseDetailsNotDefinedInCart = 
	        bundles == null || bundles.isEmpty();

	  if (courseDetailsNotDefinedInCart) {
		  cart = ServiceLocator.getCartService().addBundleToCart(new OrderConsumerContext(), bundle, getCart().getCartId());
		  Set<Bundle> returnBundles = cart.getBundles();  
		  Iterator<Bundle> bundlesIterator = returnBundles.iterator();
		  if (bundlesIterator.hasNext()) {
			  Bundle bundleInCart = bundlesIterator.next();  
			  bundle.setPrimaryKey(bundleInCart.getPrimaryKey());
		  }
	  } else if (cart.getBundles().size() == 1) {
		  Iterator<Bundle> bundlesIterator = bundles.iterator();
		  if (bundlesIterator.hasNext()) {
			  Bundle bundleInCart = bundlesIterator.next();  
			  bundle.setPrimaryKey(bundleInCart.getPrimaryKey());
			  cart = ServiceLocator.getCartService().updateCart(new OrderConsumerContext(), cart);
		  }
	  } else {
			throw new IllegalArgumentException("Cannot update coursedetails, more than one exists");

	  }
	  
      assignCartToUserContext(cart);

      return courseDetailsImpl;
      
  }
  
  /**
   * Creates an instance of <code>CourseDetails</code>
   */
  public static CourseDetails createCourseDetails()
  {
    return new CourseDetailsImpl();
  }


  /**
   * Indicate if (and updates) an instance of <code>PurchasablePermission</code> 
   * (due to a change in quantities) has changed from regular to special order.
   * This method relies on <code>PricingServices</code> to determine such condition.
   */
  public static boolean changedFromRegularOrderToSpecialOrder(PurchasablePermission purchasablePermission)
  {
    boolean changedFromRegularOrderToSpecialOrder = false;

    boolean isRegularOrder = !purchasablePermission.isSpecialOrder();

    if (isRegularOrder)
    {
    	try
    	{
    		PricingServices.getItemPrice(purchasablePermission);
    	} catch (InvalidAttributesException e)
    	{
    		LOGGER.error(LogUtil.getStack(e));    	  
    	} catch (ContactRHDirectlyLimitsExceededException e)
    	{
    		LOGGER.warn("limits exceeded " + LogUtil.getStack(e));    	  
    	} catch (DeniedLimitsExceededException e)
    	{
    		LOGGER.warn("denied limits exceeded " + LogUtil.getStack(e));    	  
    	} catch (SpecialOrderLimitsExceededException e)
    	{
    		LOGGER.warn("spec order limits exceeded " + LogUtil.getStack(e));    	  
    		purchasablePermission.setSpecialOrderLimitsExceeded(true);
    		changedFromRegularOrderToSpecialOrder = true;
    	}
    	catch (SystemLimitsExceededException e)
    	{
    		LOGGER.warn("system limits exceeded " + LogUtil.getStack(e));    	  
    	}
    	catch (ServiceInvocationException e) {
    		LOGGER.error("PricingServices.getItemPrice error " + LogUtil.getStack(e));    	  
    	}
    }

    return changedFromRegularOrderToSpecialOrder;

  }

  /**
   * Reset course details in current shopping cart.
   */
  public static void resetCourseDetails()
  {
	  Cart cart = getCart();
	  Set<Bundle> bundles = cart.getBundles();  
	  
	  boolean courseDetailsNotDefinedInCart = 
	        bundles == null || bundles.isEmpty();

	  if (courseDetailsNotDefinedInCart) {
			throw new IllegalArgumentException("There are no course details to remove");
	  } else {
		  for (Bundle bundle : cart.getBundles()) {
			 cart = ServiceLocator.getCartService().removeBundleFromCart(new OrderConsumerContext(), bundle.getBundleId(), cart.getCartId() );
		  }
	  }
      
      assignCartToUserContext( cart );
  }
  
  /**
   * Removes the Course Bundle and all the Academic items from the Cart
   */
  public static void removeCourse()
  {

  //List<PurchasablePermission> academicOrderItemsInCart = new ArrayList(0);

  Cart cart = getCart();

  if (cart != null && !isCartEmpty() )
  {
  	for (PurchasablePermission perm : getPurchasablePermissionsInCart()) {
  		if (perm.isAcademic()) {
  			try
  			{
  				removeItemFromCart(perm);
  			}
  			catch (CannotBeRemovedFromCartException e)
  			{
  				LOGGER.error("Could not remove Academic item " + perm.getPublicationTitle() + " from Cart " + LogUtil.appendableStack(e));
  			}
  		}
  	}
  	
  }
  
  }

  public static Cart getCart()
  {

    Cart cart = getCartFromUserContext();

    if (cart == null)
    {
      cart = getCartFromUserLastCartID();

      boolean cartRetrievedFromLastCartID = cart != null;
    
      if ( cartRetrievedFromLastCartID ) {
    	  assignCartToUserContext( cart );
      }
    }

    if (cart == null)
    {
      LOGGER.debug("No cart exists,  calling getNewCart");
      cart = getNewCart();

      boolean newCartCreated = cart != null;
      
      if ( newCartCreated ) {
    	assignCartToUserContext( cart );
        LOGGER.debug("Updating userLastCartId after creating new cart: " + cart.getCartId());
        updateUserLastCartID( cart.getCartId() );
      }
    }

    return cart;

  }
  
  private static Cart getExistingCart()
  {

    Cart cart = getCartFromUserContext();

    if (cart == null)
    {
      cart = getCartFromUserLastCartID();

      boolean cartRetrievedFromLastCartID = cart != null;
    
      if ( cartRetrievedFromLastCartID ) {
    	  assignCartToUserContext( cart );
      }
    }

    return cart;

  }
  
  /**
   * Refreshes the Cart in the user context from either the latest 
   * persisted cart from last cart ID or a new empty cart if last cart
   * ID doesn't exist.  Allows access to a "clean and validated" Cart 
   * via user context, without exposing an actual handle to CartImpl.
   */
  public static void refreshCart() {
      getPersistedCart();
  }
  
  private static Cart getPersistedCart()
  {

    Cart cart = getCartFromUserLastCartID();

    if (cart == null)
    {
      cart = getCart();
    }

    assignCartToUserContext(cart);

    return cart;

  }


  static void resetCartPersistence()
  {

    UserContextService.setCOICart( null );

   // CCUser user = UserContextService.getActiveAppUser();

    updateUserLastCartID( ECommerceConstants.INVALID_CART_ID );

    LOGGER.debug("Cart persistence reset successfully");

  }


  private static long getUserLastCartId()
  {

    long lastCartID = UserContextService.getAppUser().getLastCartID();

    return lastCartID;
  }


  private static void assignCartToUserContext(Cart cart)
  {
	  
	  
    UserContextService.setCOICart(cart);
  }


  private static Cart getNewCart()
  {
    String sourceCode = TransactionConstants.CC_ORDER_SOURCE_CODE;
   
    String sessionInitiator = UserContextService.getSessionInitiator();

    boolean isValidSourceCode =   StringUtils.isNotEmpty( sessionInitiator ) &&
                                ( sessionInitiator.equals( TransactionConstants.CC_ORDER_SOURCE_CODE ) ||
                                  sessionInitiator.equals( TransactionConstants.PERMISSIONS_DIRECT_ORDER_SOURCE_CODE )
                                );

    if( isValidSourceCode )
    {
      sourceCode = sessionInitiator;
    }
    
    Cart newCart = new Cart();

    // Get OrderSource for sourceCode

    OrderSource orderSource = OrderSource.COPYRIGHT_DOT_COM; // default to WWW
    for ( OrderSource os : OrderSource.values() ) {
    	if ( sourceCode.equalsIgnoreCase( os.getCode()) ) {
    		orderSource = os;
    		break;
    	}
    }
    
    newCart.setCartSource(orderSource);
    
    Cart cart = ServiceLocator.getCartService().saveNewCart(new OrderConsumerContext(), newCart);
    
    assignCartToUserContext(cart);

    LOGGER.debug("Returning new shopping cart instance with ID: " + 
                cart.getCartId());

    return cart;
  }


    /**
     * Determines if the current cart is empty.
     */
    public static boolean isCartEmpty()
  {
      Cart cart = getCart();
  	  
  	  if (cart == null) {
  		  return true;
  	  }
  	  if (cart.getItems().size() == 0) {
  		  return true;
  	  } else {
  		  return false;
  	  }
  }


  private static void updateUserLastCartID(long lastCartID)
  {
    UserServices.updateCurrentUserLastCartID( lastCartID );
  }

 
  private static Cart getCartFromUserContext()
  {
	if (UserContextService.getCOICart() != null) {
		LOGGER.debug("Cart in UserContext = " + UserContextService.getCOICart().getCartId());		
	} else {
		LOGGER.debug("No cart in user context");
    }
    return UserContextService.getCOICart();
  }


  private static Cart getCartFromUserLastCartID()
  {

    Cart cart = null;
    com.copyright.data.order.Cart legacyCart = null;
    boolean legacyCartFound = false;
    
    long userLastCartID = getUserLastCartId();

    LOGGER.debug("Getting Cart from UserLastCartId: " + getUserLastCartId());
    
    boolean validLastCartID = 
      userLastCartID > ECommerceConstants.INVALID_CART_ID;

    if (validLastCartID)
    {
      try
      {
    	  OrderConsumerContext orderUserConsumerContext = new OrderConsumerContext();
    	  cart = ServiceLocator.getCartService().getCartById(orderUserConsumerContext, userLastCartID);

    	  if ( cart != null ) { 
    	      LOGGER.debug("Persisted cart retrieved succesfully - ID: " + cart.getCartId());
    	      return cart;
    	  } else {
    		    try
    		    {
    		      legacyCart = getCartServiceAPI().getCartById(userLastCartID);
    		      LOGGER.debug("Legacy cart retrieved succesfully - ID: " + legacyCart.getID());
    		      legacyCartFound = true;
    		    } 
    		    catch (CartNotFoundException cnfe)
    		    {
    		      LOGGER.error("Could not find legacy cart with ID: " + userLastCartID + LogUtil.appendableStack(cnfe));
    		    }
    		    catch( CartStatusException cse )
    		    {
    		      LOGGER.warn("Invalid status for legacy cart with ID: " + userLastCartID + " - " + cse.getMessage() + LogUtil.appendableStack(cse));
    		    }
    	  }
      } 
      catch (CartNotFoundException cnfe)
      {
        LOGGER.warn("Could not find cart with ID: " + userLastCartID + LogUtil.appendableStack(cnfe));
      }
      catch( CartStatusException cse )
      {
        LOGGER.warn("Invalid status for cart with ID: " + userLastCartID + " - " + cse.getMessage() + LogUtil.appendableStack(cse) );
      }
    }
    
    if (legacyCartFound) {
    	cart = getNewCart();
    	cart = initCartFromLegacyCart(cart, legacyCart);
    	updateUserLastCartID(cart.getCartId());
    }

    return cart;
    
  }

  private static Cart initCartFromLegacyCart(Cart cart, com.copyright.data.order.Cart legacyCart) {

	  boolean bundleCreated = false;
	  Long bundleIdAdded = null;
	  PurchasablePermissionImpl ppImpl = null;
	  
	  for (com.copyright.data.order.PermissionRequest permissionRequest : legacyCart.getAllRequests()) {
		  PurchasablePermission purchasablePermission = PurchasablePermissionFactory.createPurchasablePermission(permissionRequest);
		  if (purchasablePermission.isAcademic() && !bundleCreated) {
			  Bundle bundle = new Bundle();
			  if (legacyCart.getCoursePack() != null) {
				  bundleCreated = true;
				  CourseDetailsImpl courseDetailsImpl = new CourseDetailsImpl();
				  courseDetailsImpl.setAccountingReference(legacyCart.getCoursePack().getAcctingRef());
//				  orderItemBundleImpl.setComments(legacyCart.getCoursePack().getHeaderRef());
				  courseDetailsImpl.setCourseName(legacyCart.getCoursePack().getCourseName());
				  courseDetailsImpl.setCourseNumber(legacyCart.getCoursePack().getCourseNumber());
				  courseDetailsImpl.setEstimatedQtyLong(legacyCart.getCoursePack().getNumStudents());
				  courseDetailsImpl.setInstructor(legacyCart.getCoursePack().getInstructorName());
				  courseDetailsImpl.setNumberOfStudentsLong(legacyCart.getCoursePack().getNumStudents());
				  courseDetailsImpl.setOrderEnteredBy(legacyCart.getCoursePack().getOrderEnteredBy());
				  courseDetailsImpl.setSchoolCollege(legacyCart.getCoursePack().getUniversityName());
				  courseDetailsImpl.setStartOfTermDate(legacyCart.getCoursePack().getStartOfTerm());
				  courseDetailsImpl.setReference(legacyCart.getCoursePack().getHeaderRef());
				  CourseDetails courseDetailsAdded = setCourseDetails(courseDetailsImpl);
				  bundleIdAdded = courseDetailsAdded.getBundleId();
			  }
		  }
		  ppImpl = (PurchasablePermissionImpl) purchasablePermission;
		  if (ppImpl.isAcademic()) {
			  ppImpl.setBundleId(bundleIdAdded);
		  }
		  try {
			cart = addItemToCart(purchasablePermission);
		} catch (CannotBeAddedToCartException e) {
//			// Simply don't convert item
			LOGGER.warn("can't convert item for cart " + LogUtil.appendableStack(e));
		} catch (CourseNotDefinedException e) {
			// Simply don't convert item
			LOGGER.warn("can't convert item for cart " + LogUtil.appendableStack(e));
		} 		  
	  }
	  
	  return cart;
	  
  }

//@Deprecated
  private static com.copyright.service.order.CartServiceAPI getCartServiceAPI()
  {
	  return CartServiceFactory.getInstance().getService();
  }


  private static boolean isCartPresentInUserContext()
  {
    return getCartFromUserContext() != null; 
  }

  private static boolean mustCollectCourseDetails(PurchasablePermission purchasablePermission)
  {

    boolean mustCollectCourseDetails = false;

    if (purchasablePermission.isAcademic() && isCoursePackNotPresentInCart())
    {
      mustCollectCourseDetails = true;
    }

    return mustCollectCourseDetails;

  }


  public static boolean isCoursePackNotPresentInCart()
  {
//    Cart cart = getCart();

//    CoursePack coursePack = cart.getCoursePack();
//    CourseDetails courseDetails = getCourseDetails();
    
    boolean coursePackNotPresentInCart = !isBundleExists();
//    || courseDetails.isEmpty();
    
    return coursePackNotPresentInCart;
  }


  private static boolean isSuitableForTransaction(PurchasablePermission purchasablePermission)
  {
//    boolean emptyCartAndConsistentPurchasablePermission = 
//      isCartEmpty() && purchasablePermission != null;

//    boolean areCartAndPurchasablePermissionCompatible = 
//      purchasablePermission != null && 
//      ((isAcademicCart() && purchasablePermission.isAcademic()) || 
//       (isNonAcademicCart() && !purchasablePermission.isAcademic()));

    boolean isSuitableForTransaction = purchasablePermission != null;
//      emptyCartAndConsistentPurchasablePermission || 
//      areCartAndPurchasablePermissionCompatible;

    return isSuitableForTransaction;
  }

  private static boolean isNotSuitableForTransaction(PurchasablePermission purchasablePermission)
  {
    return !isSuitableForTransaction(purchasablePermission);
  }
  
  private static boolean userHasLastCartID()
  {
    return getUserLastCartId() > 0;
  }

  private static BigDecimal getTotalPrice(Cart cart)
  {
	  BigDecimal totalPrice = new BigDecimal(0);
	  
	  for (Item item : cart.getItems()) {
		  
		  PurchasablePermission purchasablePermission = PurchasablePermissionFactory.createPurchasablePermission(item);
		  
		  totalPrice = totalPrice.add(purchasablePermission.getTotalPriceValue());		  
	  }
	  LOGGER.debug("Total Price for Cart: " + totalPrice);
	  return totalPrice.setScale(2, BigDecimal.ROUND_HALF_EVEN);
	  
  }
  
  public static BigDecimal getItemPrice(Item item)
  {
	  PurchasablePermission purchasablePermission = PurchasablePermissionFactory.createPurchasablePermission(item);
		   
	  return purchasablePermission.getTotalPriceValue().setScale(2, BigDecimal.ROUND_HALF_EVEN);  
  }
  
  public static BigDecimal getTotalChargePrice(Cart cart)
  {
	  BigDecimal totalChargePrice = new BigDecimal(0);
	  
	  for (Item item : cart.getItems()) {

		  PurchasablePermission purchasablePermission = PurchasablePermissionFactory.createPurchasablePermission(item);
		  
		  if (purchasablePermission.getRightSourceCd().equals(RightSourceEnum.RL.name())) {
			  if (purchasablePermission.getProductCd().equals(ProductEnum.RL.name())) {
				  if (purchasablePermission.getSpecialOrderTypeCd().equals(SpecialOrderTypeEnum.NOT_SPECIAL_ORDER.name())) {
					  totalChargePrice = totalChargePrice.add(purchasablePermission.getTotalPriceValue());		  
				  }
			  }
		  } else if (purchasablePermission.getRightSourceCd().equals(RightSourceEnum.TF.name())) {
			  if (purchasablePermission.getSpecialOrderTypeCd().equals(SpecialOrderTypeEnum.NOT_SPECIAL_ORDER.name())) {
				  totalChargePrice = totalChargePrice.add(purchasablePermission.getTotalPriceValue());		  
			  }
		  }	  

	  }
	  LOGGER.debug("Total Charge Price for Cart: " + totalChargePrice);
	  return totalChargePrice.setScale(2, BigDecimal.ROUND_HALF_EVEN);
	 
  }
  
  public static void initBundleIdForPurchasablePermission (PurchasablePermission purchasablePermission) {
  	
  	PurchasablePermissionImpl ppImpl = (PurchasablePermissionImpl) purchasablePermission;
  	
  	Cart cart = getCart();
  	
  	Long bundleId = null;
  	
	if (cart.getBundles().size() == 1) {
	  Iterator<Bundle> bundleIterator = cart.getBundles().iterator();
	  if (bundleIterator.hasNext()) {
		  Bundle bundle = bundleIterator.next();
		  bundleId = bundle.getBundleId();
	  }
	}

  	ppImpl.setBundleId(bundleId);
  	
  }
  
  
  

  public static boolean updateCartForRightsLinkPriceChange(){
  	// Get RightsLink cart item
      boolean cartItemUpdated				=	false;
         	// reprice for each i rl item
      if(!SystemStatus.isRightslinkUp()){
      	return false;
      }
      	
      	String nrlsUserFee;
      	
       	for(PurchasablePermission rlPurchasablePermission:CartServices.getRightsLinkItemsInCart()){
      			// Reprice cart iteM
     		//if(!rlPurchasablePermission.isSpecialOrder()){
      		RepriceResponse repriceResponse=CartServices.rePriceRightsLinkItem(rlPurchasablePermission);
      		
      		nrlsUserFee = CC2Configuration.getInstance().getNrlsUserFeeValue();
      		
      		if ( rlPurchasablePermission.getItem().getItemParms().get(ItemParmEnum.OFFERCHANNEL.name()) != null
					&& rlPurchasablePermission.getItem().getItemParms().get(ItemParmEnum.OFFERCHANNEL.name()).getParmValue().equalsIgnoreCase(RlnkConstants.PUB_TO_PUB)
					&& (!UserContextService.isSuppressNRLSFee()) 
					&& rlPurchasablePermission.getItem().getTotalPrice() != null 
					&& rlPurchasablePermission.getItem().getTotalPrice().compareTo(BigDecimal.ZERO) > 0){
				
					repriceResponse.getPriceData().setTotalPrice(repriceResponse.getPriceData().getTotalPrice().add(new BigDecimal(nrlsUserFee)));
			}
      		
	      		if(repriceResponse.isExceptionOccurred() && repriceResponse.getErrorType()==null){
			      				throw new CCRuntimeException(ExceptionUtils.getFullStackTrace(repriceResponse.getThrowable()));
	      		   }
      				if(repriceResponse.getErrorType().equals(RepriceErrorTypeEnum.NO_ERROR) && 
      						RlnkRightsServices.isPriceDifferent(repriceResponse.getPriceData())){
      					    cartItemUpdated=true;
	        				// get new ItemRequest from Rights Link
	        				ItemRequestResponse itemRequestResponse = RlnkRightsServices.getItemRequestFromSession(PricingServices.getRLItemRequestFromPurchasablePermission(rlPurchasablePermission));
	        				if (itemRequestResponse.hasErrors())
	        				{
	        					// this should not happen but if it does remove the item
	        					try {
	        						cartItemUpdated=true;
									removeItemFromCart(rlPurchasablePermission);
								} catch(CannotBeRemovedFromCartException  e){
									LOGGER.error(LogUtil.getStack(e));
									throw new CCRuntimeException(e);
									
								}
	        				}else{
			        				// Update Purchasable permission with new Item request
			        				PricingServices.getPurchasablePermissionFromRLItemRequest( rlPurchasablePermission, itemRequestResponse.getItemRequest());
			        				try {
			        					updateRightslnkCart(rlPurchasablePermission);
									} catch(Exception e){
										LOGGER.error(LogUtil.getStack(e));
										throw new CCRuntimeException(e);
									}
	        				}
      				}else if(repriceResponse.getErrorType().equals(RepriceErrorTypeEnum.NON_RECOVERABLE) ||
      						  repriceResponse.getErrorType().equals(RepriceErrorTypeEnum.RECOVERABLE)){
      					try {
      						cartItemUpdated=true;
								removeItemFromCart(rlPurchasablePermission);
							} catch(CannotBeRemovedFromCartException e){
								LOGGER.error(LogUtil.getStack(e));
								throw new CCRuntimeException(e);
							}
      					
      				}
      		//}
      	}
      return cartItemUpdated;
  }
  
  
  /**
   * 
   * @param request
   * @param cartForm
   */
  public static boolean updateCartForTFAcademicPriceChange(){
  	// Get RightsLink cart item
    boolean cartItemUpdated				 =	false;
    boolean removeCartItem				 =	false;
    BigDecimal totalPriceBeforeUpdate			 =	new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_EVEN);
    BigDecimal totalPriceAfterUpdate			 =	new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_EVEN);
    
     boolean isSpecialOrderBeforeUpdate	 =	false;
         	// reprice for each i rl item
     	for(PurchasablePermission tfPurchasablePermission:getAcademicOrderItemsInCart()){
//     		if(tfPurchaseablePermission.isRightsLink()){
//     			continue;
//     		}
      		totalPriceBeforeUpdate=new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_EVEN);
      		removeCartItem=false;
      		
      		isSpecialOrderBeforeUpdate=tfPurchasablePermission.isSpecialOrder();
      		
      		if(tfPurchasablePermission.getItem().getTotalPrice()!=null){
      			totalPriceBeforeUpdate=tfPurchasablePermission.getTotalPriceValue();
      				//Double.valueOf(tfPurchasablePermission.getItem().getTotalPrice().toString());	
      		}

      		//String itemPrice = null;
      		try{
      			PricingServices.getItemPrice(tfPurchasablePermission,UserContextService.getActiveAppUser().getPartyID());
      		} catch(SpecialOrderLimitsExceededException s){
      			if (!tfPurchasablePermission.getSpecialOrderTypeCd().equals(SpecialOrderTypeEnum.LIMITS_EXCEEDED_SPECIAL_ORDER.name()) &&
      				!tfPurchasablePermission.getSpecialOrderTypeCd().equals(SpecialOrderTypeEnum.BIBLIO_PAGES_MISSING_SPECIAL_ORDER.name())	) {
      				cartItemUpdated=true;
      			}	
      		}
      		catch(InvalidAttributesException i){
				LOGGER.warn(LogUtil.getStack(i));
      			removeCartItem=true;
      		}
      		catch(DeniedLimitsExceededException d){
				LOGGER.warn(LogUtil.getStack(d));
      			removeCartItem=true;
      		}
      		catch(ContactRHDirectlyLimitsExceededException c){
				LOGGER.warn(LogUtil.getStack(c));
      			removeCartItem=true;
      		}
      		catch(SystemLimitsExceededException s){
				LOGGER.warn(LogUtil.getStack(s));
      			removeCartItem=true;							
      		}
      		catch(ServiceInvocationException e) {
				LOGGER.error(LogUtil.getStack(e));
      			throw new CCRuntimeException(e);
      		}
      		
      		PurchasablePermission tfUpdatedPurchasablePermission = null;
      		
      		try {
      			tfUpdatedPurchasablePermission	 = PricingServices.updateItemPriceForLicenseeID(tfPurchasablePermission,UserContextService.getActiveAppUser().getPartyID());
      		} catch(ServiceInvocationException e) {
				LOGGER.error(LogUtil.getStack(e));
      			throw new CCRuntimeException(e);
      		}
      		
      		if (tfUpdatedPurchasablePermission.getItemAvailabilityCd() != null &&
      			ItemAvailabilityEnum.DENY.getStandardPermissionCd().equals(tfUpdatedPurchasablePermission.getItemAvailabilityCd())) {
      			removeCartItem=true;
      		}
      		
      		if (tfUpdatedPurchasablePermission.getItemAvailabilityCd() != null &&
      			ItemAvailabilityEnum.CONTACT_RH_DIRECTLY.getStandardPermissionCd().equals(tfUpdatedPurchasablePermission.getItemAvailabilityCd())) {
      			removeCartItem=true;
      		}

      		
      		if(removeCartItem){
      			cartItemUpdated=true;
      			try {
						removeItemFromCart(tfPurchasablePermission);
						continue;
					} catch(CannotBeRemovedFromCartException e){
						LOGGER.error(LogUtil.getStack(e));
						throw new CCRuntimeException(e);
					}
      		}
      		
      		
      		
      		if(!tfUpdatedPurchasablePermission.isSpecialOrder()){
      			
	        			//itemPrice = itemPrice.replaceAll( "\\$", "" ).trim().replace(",", "");;
	        			totalPriceAfterUpdate=tfUpdatedPurchasablePermission.getTotalPriceValue();
	        			if(totalPriceAfterUpdate.compareTo(totalPriceBeforeUpdate)!=0){
	        				cartItemUpdated=true;
	        				try {
								updateCartItem(tfUpdatedPurchasablePermission);
							} catch(Exception e){
								LOGGER.error(LogUtil.getStack(e));
								throw new CCRuntimeException(e);
							}
	        			}
	        }else if(tfUpdatedPurchasablePermission.isSpecialOrder() && !isSpecialOrderBeforeUpdate){
	        			cartItemUpdated=true;
      				try {
							updateCartItem(tfUpdatedPurchasablePermission);
						} catch(Exception e){
							LOGGER.error(LogUtil.getStack(e));
							throw new CCRuntimeException(e);
						}
	        		
      		}

      		
      	}
      return cartItemUpdated;
      
  }
  public static boolean updateCartForTFNonAcademicPriceChange(){
  	// Get RightsLink cart item
    boolean cartItemUpdated				 =	false;
    boolean removeCartItem				 =	false;
    BigDecimal totalPriceBeforeUpdate			 =	new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_EVEN);
    BigDecimal totalPriceAfterUpdate			 =	new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_EVEN);
     boolean isSpecialOrderBeforeUpdate	 =	false;
         	// reprice for each i rl item
     	for(PurchasablePermission tfPurchasablePermission:getNonAcademicOrderItemsInCart()){
     		if(tfPurchasablePermission.isRightsLink()){
     			continue;
     		}
//     		if(tfPurchaseablePermission.isRightsLink()){
// 			continue;
// 		}
  		totalPriceBeforeUpdate=new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_EVEN);
  		removeCartItem=false;
  		
  		isSpecialOrderBeforeUpdate=tfPurchasablePermission.isSpecialOrder();
  		
  		if(tfPurchasablePermission.getItem().getTotalPrice()!=null){
  			totalPriceBeforeUpdate=tfPurchasablePermission.getTotalPriceValue();
  				//Double.valueOf(tfPurchasablePermission.getItem().getTotalPrice().toString());	
  		}

  		//String itemPrice = null;
  		try{
  			PricingServices.getItemPrice(tfPurchasablePermission,UserContextService.getActiveAppUser().getPartyID());
  		} catch(SpecialOrderLimitsExceededException s){
  			//removeCartItem=true;
  			//this is special order
  			if (!tfPurchasablePermission.getSpecialOrderTypeCd().equals(SpecialOrderTypeEnum.LIMITS_EXCEEDED_SPECIAL_ORDER.name()) &&
  				!tfPurchasablePermission.getSpecialOrderTypeCd().equals(SpecialOrderTypeEnum.BIBLIO_PAGES_MISSING_SPECIAL_ORDER.name())	) {
  				cartItemUpdated=true;
  			}  			
  		}
  		catch(InvalidAttributesException i){
			LOGGER.warn(LogUtil.getStack(i));
  			removeCartItem=true;
  		}
  		catch(DeniedLimitsExceededException d){
  			removeCartItem=true;
  		}
  		
  		catch(ContactRHDirectlyLimitsExceededException c){
  			removeCartItem=true;
  		}
  		catch(SystemLimitsExceededException s){
  			removeCartItem=true;							
  		}
  		catch(ServiceInvocationException e) {
  			LOGGER.error(LogUtil.getStack(e));
  			throw new CCRuntimeException(e);
  		}
  		
  		PurchasablePermission tfUpdatedPurchasablePermission = null;
  		try {
  			tfUpdatedPurchasablePermission	 = PricingServices.updateItemPriceForLicenseeID(tfPurchasablePermission,UserContextService.getActiveAppUser().getPartyID());
  		} catch(ServiceInvocationException e) {
  			LOGGER.error(LogUtil.getStack(e));
  			throw new CCRuntimeException(e);
  		}
  		
  		if (tfUpdatedPurchasablePermission.getItemAvailabilityCd() != null &&
  			ItemAvailabilityEnum.DENY.getStandardPermissionCd().equals(tfUpdatedPurchasablePermission.getItemAvailabilityCd())) {
  			removeCartItem=true;
  		}

  		if (tfUpdatedPurchasablePermission.getItemAvailabilityCd() != null &&
  			ItemAvailabilityEnum.CONTACT_RH_DIRECTLY.getStandardPermissionCd().equals(tfUpdatedPurchasablePermission.getItemAvailabilityCd())) {
  			removeCartItem=true;
  		}
  		
  		if(removeCartItem){
  			cartItemUpdated=true;
  			try {
					removeItemFromCart(tfPurchasablePermission);
					continue;
				} catch(CannotBeRemovedFromCartException e){
		  			LOGGER.error(LogUtil.getStack(e));
					throw new CCRuntimeException(e);
				}
  		}
  		
  		
  		
  		if(!tfUpdatedPurchasablePermission.isSpecialOrder()){
  			
        			//itemPrice = itemPrice.replaceAll( "\\$", "" ).trim().replace(",", "");;
        			totalPriceAfterUpdate=tfUpdatedPurchasablePermission.getTotalPriceValue();
        			if(totalPriceAfterUpdate.compareTo(totalPriceBeforeUpdate)!=0){
        				cartItemUpdated=true;
        				try {
							updateCartItem(tfUpdatedPurchasablePermission);
						} catch(Exception e){
				  			LOGGER.error(LogUtil.getStack(e));
							throw new CCRuntimeException(e);
						}
        			}
        }else if(tfUpdatedPurchasablePermission.isSpecialOrder() && !isSpecialOrderBeforeUpdate){
        			cartItemUpdated=true;
  				try {
						updateCartItem(tfUpdatedPurchasablePermission);
					} catch(Exception e){
			  			LOGGER.error(LogUtil.getStack(e));
						throw new CCRuntimeException(e);
					}
        		
  		}
      		
      	}
      return cartItemUpdated;
      
  }
  
  
  
  //** CART ATTRIBUTES **/
  
  private static String getCartType(){
	if(hasOnlySpecialOrders()){
		return  SPECIAL_ORRDER_CART;	
	}else if(!hasOnlySpecialOrders() && containsSpecialOrders()){
		return MIXED_CART;
	}else if(!hasOnlySpecialOrders() && !containsSpecialOrders()){
		return CLEAN_CART;
	}
	return  SPECIAL_ORRDER_CART;	
}
  private static String getAcademicCartType(){
	if(hasOnlyAcademicSpecialOrders()){
		return  SPECIAL_ORRDER_CART;	
	}else if(!hasOnlyAcademicSpecialOrders() && containsAcademicSpecialOrders()){
		return MIXED_CART;
	}else if(!hasOnlyAcademicSpecialOrders() && !containsAcademicSpecialOrders()){
		return CLEAN_CART;
	}
	return  SPECIAL_ORRDER_CART;	
}

  private static String getNonAcademicCartType(){
	if(hasOnlyNonAcademicSpecialOrders() ){
		return  SPECIAL_ORRDER_CART;	
	}else if(!hasOnlyNonAcademicSpecialOrders() && containsNonAcademicSpecialOrders()){
		return MIXED_CART;
	}else if(!hasOnlyNonAcademicSpecialOrders() && !containsNonAcademicSpecialOrders()){
		return CLEAN_CART;
	}
	return  SPECIAL_ORRDER_CART;	
}


/**
 * Contains Academic Cart items with price TBD
 * @return
 */


/* Has special Order */
public static boolean  hasOnlySpecialOrders(){
	if ( (getNumberOfSpecialOrderItemsInCart() > 0) && (getNumberOfRegularOrderItemsInCart() == 0) ){
		return Boolean.TRUE;
	}
	 return Boolean.FALSE;
}
public static boolean  hasOnlyAcademicSpecialOrders(){
	 if ((getNumberOfAcademicSpecialOrderItemsInCart() > 0) && 
             (getNumberOfRegularAcademicOrderItemsInCart() == 0)){
		 return Boolean.TRUE;
	 }
	 return Boolean.FALSE;
}
public static boolean  hasOnlyNonAcademicSpecialOrders(){
	 if ((getNumberOfNonAcademicSpecialOrderItemsInCart() > 0) && 
            (getNumberOfRegularNonAcademicOrderItemsInCart() == 0)){
		 return Boolean.TRUE;
	 }
	 return Boolean.FALSE;
}




public static boolean  hasNonReprintSpecialOrders(){
	 for (PurchasablePermission purchasablePermission : getSpecialOrderItemsInCart()) {
		 if(!purchasablePermission.isAcademic() && !purchasablePermission.isReprint()){
			 return Boolean.TRUE;
		 }
	 }
	 return Boolean.FALSE;
	 
}
public static boolean  hasNonReprintRightslnkSpecialOrders(){
	 for (PurchasablePermission purchasablePermission : getSpecialOrderItemsInCart()) {
		 if( purchasablePermission.isRightsLink() && !purchasablePermission.isReprint()){
			 return Boolean.TRUE;
		 }
	 }
	 return Boolean.FALSE;
}
public static boolean  hasReprintSpecialOrders(){
	 for (PurchasablePermission purchasablePermission : getSpecialOrderItemsInCart()) {
		 if( purchasablePermission.isRightsLink() && purchasablePermission.isReprint()){
			 return Boolean.TRUE;
		 }
	 }
	 return Boolean.FALSE;
}
public static boolean  hasRightslnkSpecialOrders(){
	 for (PurchasablePermission purchasablePermission : getSpecialOrderItemsInCart()) {
		 if( purchasablePermission.isRightsLink()){
			 return Boolean.TRUE;
		 }
	 }
	 return Boolean.FALSE;
}


// fom methods
public static boolean showExcludeTBDAcademicItemText(){
	 if(getAcademicCartType()==MIXED_CART){
		 return true;
	 }
	 return false;
}
/** aputtur
 * SP - NOT ONLY- BUT HAS NON PRICED
 *    TBD  |  Priced | Only | Show
 *   ---------------------------
 * sp| Y   |	N	|	Y	| N
 * sp| Y   |	Y	|	Y	| Y *
 * sp| N   |	Y	|	Y	| N
 * -----------------------------
 * @return
 */
public static boolean showExcludeTBDNonAcademicItemText(){
	 if(getNonAcademicCartType()==MIXED_CART &&  containsNonPricedNonAcademicItems()){
		 return true;
	 }else if(getNonAcademicCartType()==SPECIAL_ORRDER_CART){
		 if(hasOnlyNonAcademicSpecialOrders() &&  containsPricedSpecialOrderItem() && containsNonPricedNonAcademicItems()){
			 return true;
		 }
	 }
	 return false;
	 
}
public static boolean showExcludeTBDItemText(){
	 if(getCartType()==MIXED_CART && ( containsAcademicSpecialOrders() || containsNonPricedNonAcademicItems())){
		 return true;
	 }else if (showExcludeTBDNonAcademicItemText() || showExcludeTBDNonAcademicItemText()){
		 return true;
	 }
	 return false;
}

public static boolean hasOnlyNonPricedOrders(){
	
	if (CartServices.getNumberOfPricedItemsInCart() == 0)
	{
		 return true;
	}
	return false;
}

public static boolean hasOnlyNonPricedAcademicOrders(){
	
	if (CartServices.getNumberOfPricedAcademicItemsInCart() == 0)
	{
		 return true;
	}
	return false;
}
public static boolean hasOnlyNonPricedNonAcademicOrders(){
	
	if (CartServices.getNumberOfPricedNonAcademicItemsInCart() == 0)
	{
		 return true;
	}
	return false;
}


private static List<Item> getSortedCartItems(Set<Item> itemsInCart){
	if(itemsInCart==null || itemsInCart.size()==0){
		return new ArrayList<Item>();
	}
	List<Item> sortedItems=new ArrayList<Item>();
	for(Item item:itemsInCart){
		sortedItems.add(item);
	}
    Collections.sort(sortedItems, new Comparator<Item>(){

        public int compare(Item sortedItem1, Item sortedItem2) {
           return sortedItem2.getItemId().compareTo(sortedItem1.getItemId());
        }

    });

return sortedItems;
	
}
}
