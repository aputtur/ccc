package com.copyright.ccc.web.actions;

import java.text.NumberFormat;
import java.util.Collection;

import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.web.CCAction;
import com.copyright.data.order.Cart;
import com.copyright.data.order.PermissionRequest;
import com.copyright.workbench.i18n.Money;

public abstract class BaseSubmitCartAction extends CCAction
{
    public BaseSubmitCartAction() 
    { 
    }
    
    /*
    * Returns the Cart object in the UserContext.
    * @return a UserContext Cart object.
    */
    protected Cart getCart() 
    {
        Cart cart = UserContextService.getCart();
        return cart;
    }
    
    /*
    * Get the Collection of regular order items from the Cart object
    * in the UserContext.
    * @return a Collection of Cart regular order items 
    */
    protected Collection<PermissionRequest> getRegularItems()
    {
        Cart cart = UserContextService.getCart();
        Collection<PermissionRequest> cartItems = cart.getRegularRequests();
        return cartItems;
    }
    
    /*
    * Get the Collection of special order items from the Cart object
    * in the UserContext.
    * @return a Collection of Cart special order items 
    */
    protected Collection<PermissionRequest> getSpecialItems() 
    {
        Cart cart = UserContextService.getCart();
        Collection<PermissionRequest> cartItems = cart.getSpecialOrderRequests();
        return cartItems;
    }

    /*
    * Generates a locale specific monetary value based on a 
    * long value.
    * @param price is the double price value to be formatted as a monetary value 
    * according to the default locale.
    */
    protected String getFormattedPrice(double price) 
    {
       String priceString = "";
       NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
       priceString = numberFormat.format(price);
       
       return priceString;
    }
    
    /*
    * Get the total fees for the cart as an instance of Money
    * @return the total fees for the cart items as a Money object
    */
    protected Money getTotalFees() 
    {
         Cart cart = UserContextService.getCart();
        Money totalFees = cart.getTotalFees();
        return totalFees;   
    }
}
