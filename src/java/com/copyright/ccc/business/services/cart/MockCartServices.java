package com.copyright.ccc.business.services.cart;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.web.transaction.TransactionConstants;
import com.copyright.ccc.web.util.WebUtils;
import com.copyright.data.ValidationException;
import com.copyright.data.order.Cart;
import com.copyright.data.order.PermissionRequest;
import com.copyright.service.order.CartServiceAPI;
import com.copyright.service.order.CartServiceFactory;
import com.copyright.workbench.i18n.Money;


public class MockCartServices
{
    private static CourseDetails _courseDetails = null;
    
    public static Cart getCart()
    {
        Cart cart = UserContextService.getCart();
        
        if(cart == null)
        {
            cart = CartServiceFactory.getInstance().getService().getNewCart(TransactionConstants.CC_ORDER_SOURCE_CODE);
            setCart(cart);
            return cart;
        }
        else return cart;
    }
    
    public static void setCart(Cart cart)
    {
        UserContextService.setCart(cart);
    }
    
    public static boolean isAcademicCart()
    {
        List<PurchasablePermission> items = getItemsInCart();
        
        if(items.size() == 0) 
            return false;
        else
        {
            PurchasablePermission curr = items.get(0);
            return curr.isAcademic();
        }
    }
    
    public static List<PurchasablePermission> getItemsInCart()
    {
        ArrayList<PurchasablePermission> items = new ArrayList<PurchasablePermission>();
        
        Cart cart = getCart();
      
        if( cart != null )
        {
            List<PermissionRequest> requests = cart.getAllRequests();
        
            Iterator<PermissionRequest> iterator = requests.iterator();
            while(iterator.hasNext())
            {
                PermissionRequest request = iterator.next();
//                items.add(PurchasablePermissionFactory.createPurchasablePermission(request));
            }
        }
            
        return items;
    }
    
    public static boolean canAddToCart( PurchasablePermission purchasablePermission )
    {
        List<PurchasablePermission> items = getItemsInCart();
        
        if(items.size() == 0) 
            return true;
        else
        {
            PurchasablePermission curr = items.get(0);
            boolean isSame = (curr.isAcademic() == purchasablePermission.isAcademic());
            return isSame;
        }
    }
    
 
    public static void addItemToCart ( PurchasablePermission purchasablePermission ) 
        throws CannotBeAddedToCartException, 
           CourseNotDefinedException, 
           InvalidAttributesException, 
           DeniedLimitsExceededException,
           SpecialOrderLimitsExceededException,
           ContactRHDirectlyLimitsExceededException
    {
        
        if( purchasablePermission.isAcademic() && _courseDetails == null)
        {
            throw new CourseNotDefinedException( purchasablePermission );
        }
        
        if( !canAddToCart(purchasablePermission) )
        {
          throw new CannotBeAddedToCartException( purchasablePermission );
        }
        
        try
        {
          Cart cart = getCart();
          
          PermissionRequest permissionRequest = purchasablePermission.getPermissionRequest();
          
          CartServiceAPI cartService =  CartServiceFactory.getInstance().getService();
          
          cart = cartService.addToCart( permissionRequest, cart );
          
          setCart(cart);
        }
        catch( ValidationException validationException )
        {
          throw new InvalidAttributesException( validationException, purchasablePermission );
        }
        
        catch ( com.copyright.data.pricing.DeniedLimitsExceededException deniedLimitsExceededException )
        {
          throw new DeniedLimitsExceededException( purchasablePermission, deniedLimitsExceededException );
        }
        
        catch ( com.copyright.data.pricing.SpecialOrderLimitsExceededException specialOrderLimitsExceededException )
        {
          throw new SpecialOrderLimitsExceededException( purchasablePermission, specialOrderLimitsExceededException );
        }
        
        catch( com.copyright.data.pricing.ContactRHDirectlyLimitsExceededException contactRHDirectlyLimitsExceededException )
        {
          throw new ContactRHDirectlyLimitsExceededException ( purchasablePermission, contactRHDirectlyLimitsExceededException );
        }
    }
    
    /**
     * Updates an instance of <code>PurchasablePermission</code> in the shopping cart.
     */
      public static void updateCartItem( PurchasablePermission purchasablePermission ) 
      throws InvalidAttributesException, 
             DeniedLimitsExceededException, 
             SpecialOrderLimitsExceededException, 
             ContactRHDirectlyLimitsExceededException
     {
       
        try
        {
            Cart cart = getCart();
          
            PermissionRequest permissionRequest = purchasablePermission.getPermissionRequest();
          
            cart = CartServiceFactory.getInstance().getService().updateRequest( permissionRequest, cart );
          
            setCart(cart);
        }
        
        catch( ValidationException validationException )
        {
          throw new InvalidAttributesException( validationException, purchasablePermission );
        }
        
        catch ( com.copyright.data.pricing.DeniedLimitsExceededException deniedLimitsExceededException )
        {
          throw new DeniedLimitsExceededException( purchasablePermission, deniedLimitsExceededException );
        }
        
        catch ( com.copyright.data.pricing.SpecialOrderLimitsExceededException specialOrderLimitsExceededException )
        {
          throw new SpecialOrderLimitsExceededException( purchasablePermission, specialOrderLimitsExceededException );
        }
        
        catch( com.copyright.data.pricing.ContactRHDirectlyLimitsExceededException contactRHDirectlyLimitsExceededException )
        {
          throw new ContactRHDirectlyLimitsExceededException ( purchasablePermission, contactRHDirectlyLimitsExceededException );
        }
      }
      
    /**
    * Returns the total fees for the shopping cart (e.g. $ 12.34).
    */
    public static String getCartTotal(){
    
        Money cartTotal = ECommerceConstants.NO_ITEMS_IN_CART_AMOUNT;
        
        cartTotal = getCart().getTotalFees();
        
        return WebUtils.formatMoney( cartTotal );
    }
    
    public static CourseDetails getCourseDetails()
    {
        return _courseDetails;
    }
    
    public static void setCourseDetails(CourseDetails courseDetails)
    {
        _courseDetails = courseDetails;
    }

}
