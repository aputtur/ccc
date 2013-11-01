package com.copyright.ccc.web.forms.coi;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;

import com.copyright.ccc.business.services.SystemStatus;
import com.copyright.ccc.business.services.cart.PurchasablePermission;
import com.copyright.ccc.web.forms.BasePaymentForm;

public class SelectCartPaymentActionForm extends BasePaymentForm
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private Boolean alwaysInvoice =false;
    private Boolean alwaysInvoiceFlag = false;
    private Boolean hasOnlySpecialOrders = false;
    private Boolean canOnlyBeInvoiced = false;

 
    private Collection<PurchasablePermission> cartItems;
    private String cartTotal;
    private String cartTotalWithNoDollarSign;
    private String cartChargeTotalWithNoDollarSign;
    private String cartChargeTotal;
    
    //private String[] checkedRow;

    // For validation page refresh persistence
      private boolean hasSpecialOrders; // UI control for Payment warning message

    
    private boolean cybersourceSiteUp = SystemStatus.isCybersourceSiteUp();
  
   private String userChannel;
   
   
   protected boolean _hasOnlyNonPricedOrders = false;
   protected boolean _showExcludeTBDItemText = false;
   
   private boolean teleSalesUp = true;

    public boolean isTeleSalesUp() {
    	return teleSalesUp;
	}
	
	public void setTeleSalesUp(boolean teleSalesUp) {
		this.teleSalesUp = teleSalesUp;
	}
	
	public boolean getTeleSalesUp() {
		return teleSalesUp;
	}
		
	public SelectCartPaymentActionForm()
    {
    }
    
    public void reSet()
    {
       super.reSet();
    }

    /**
    * Reset all properties to their default values.

    public void reset( ActionMapping mapping, HttpServletRequest request )
    {
       super.reset(mapping, request);
    }    */
     public void setHasOnlySpecialOrders( Boolean hasOnlySpecialOrders )
     {
         this.hasOnlySpecialOrders = hasOnlySpecialOrders;
     }

     public Boolean getHasOnlySpecialOrders()
     {
         return hasOnlySpecialOrders;
     }
     
    public void setAlwaysInvoice( Boolean alwaysInvoice )
    {
        this.alwaysInvoice = alwaysInvoice;
    }

    public Boolean getAlwaysInvoice()
    {
        return alwaysInvoice;
    }

    public void setAlwaysInvoiceFlag( Boolean alwaysInvoiceFlag )
    {
        this.alwaysInvoiceFlag = alwaysInvoiceFlag;
    }

    public Boolean getAlwaysInvoiceFlag()
    {
        return alwaysInvoiceFlag;
    }
        
   
  
    public void setCartTotal( String cartTotal )
    {
        this.cartTotal = cartTotal;
        setCartTotalWithNoDollarSign(cartTotal);
    }

    public String getCartTotal()
    {
    	return cartTotal;
    }
    
    public void setCartTotalWithNoDollarSign( String cartTotal )
    {
    	String formattedCartTotal = cartTotal.substring(1).trim();
        this.cartTotalWithNoDollarSign = formattedCartTotal;
                
    }

    public String getCartTotalWithNoDollarSign()
    {
    	return cartTotalWithNoDollarSign;
    }

    public void setCartChargeTotal( String cartChargeTotal )
    {
        this.cartChargeTotal = cartChargeTotal;
        setCartChargeTotalWithNoDollarSign( cartChargeTotal );
                
    }

    public String getCartChargeTotal()
    {
    	return cartChargeTotal;
    }
    
    public void setCartChargeTotalWithNoDollarSign( String cartTotal )
    {
    	String formattedCartChargeTotal = cartTotal.substring(1).trim();
        this.cartChargeTotalWithNoDollarSign = formattedCartChargeTotal;
                
    }

    public String getCartChargeTotalWithNoDollarSign()
    {
    	return cartChargeTotalWithNoDollarSign;
    }
    public void setCartItems( Collection<PurchasablePermission> cartItems )
    {
        this.cartItems = cartItems;
    }

    public Collection<PurchasablePermission> getCartItems()
    {
        return cartItems;
    }
  

    public void setHasSpecialOrders( boolean hasSpecialOrders )
    {
        this.hasSpecialOrders = hasSpecialOrders;
    }

    public boolean isHasSpecialOrders()
    {
        return hasSpecialOrders;
    }
    

    
    public void setUserChannel( String channel )
    {
        this.userChannel = channel;
    }

    public String getUserChannel()
    {
        return userChannel;
    }
    
    public void setHasOnlyNonPricedOrders( boolean onlySpec )
    {
        this._hasOnlyNonPricedOrders = onlySpec;
    }

    public boolean getHasOnlyNonPricedOrders()
    {
        return _hasOnlyNonPricedOrders;
    }
    public void setShowExcludeTBDItemText( boolean showExcludeTBDItemAText )
    {
        this._showExcludeTBDItemText = showExcludeTBDItemAText;
    }

    public boolean getShowExcludeTBDItemText()
    {
        return _showExcludeTBDItemText;
    }  

    public boolean getCybersourceSiteUp()
    {
		return cybersourceSiteUp;
	}

	public void setCybersourceSiteUp(boolean cybersourceSiteUp)
	{
		this.cybersourceSiteUp = cybersourceSiteUp;
	}

	
	public void setCanOnlyBeInvoiced(Boolean canOnlyBeInvoiced) {
		this.canOnlyBeInvoiced = canOnlyBeInvoiced;
	}

	public Boolean getCanOnlyBeInvoiced() {
		return canOnlyBeInvoiced;
	}


	 public  boolean getDisplayChargeTotal(){
	    	if(getCartChargeTotal()!=null && StringUtils.isNotEmpty(getCartChargeTotal())  && 
	    			getCartChargeTotal().compareToIgnoreCase(getCartTotal())!=0){
	    		return true;
	    	}
	    	return false;
	    }
}
