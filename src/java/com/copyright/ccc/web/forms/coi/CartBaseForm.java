package com.copyright.ccc.web.forms.coi;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.copyright.ccc.business.services.cart.CartServices;
import com.copyright.ccc.business.services.cart.CourseDetails;
import com.copyright.ccc.business.services.cart.PurchasablePermission;
import com.copyright.ccc.web.CCValidatorForm;

public class CartBaseForm  extends CCValidatorForm
{
    /**
	 * 
	 */
    protected static final Logger LOGGER = Logger.getLogger( CartBaseForm.class );
    protected static final long serialVersionUID = 1L;
  
    protected String _cartTotal;
    protected String _cartChargeTotal;
    protected String _cartTotalWithNoDollarSign;
    protected String _cartChargeTotalWithNoDollarSign;
    
    protected CourseDetails _courseHeader;
    protected boolean _academicCart;
    protected boolean _containsSpecialOrders;
    protected boolean _containsAcademicSpecialOrders;
    protected boolean _containsNonAcademicSpecialOrders;
    protected boolean _containsReprintSpecialOrders;
    protected boolean _containsPermissionSpecialOrders;   
    protected boolean _containsChargeItems;
    private boolean _containsPricedSpecialOrderItem=false;
    protected boolean _hasOnlySpecialOrders = false;
    protected boolean _hasOnlyAcademicSpecialOrders = false;
    protected boolean _hasOnlyNonAcademicSpecialOrders = false;
    protected boolean _hasNonReprintSpecialOrders = false;
    protected boolean _hasReprintSpecialOrders = false;
    
    
    protected boolean _hasOnlyNonPricedOrders = false;
    protected boolean _hasOnlyNonPricedAcademicOrders = false;
    protected boolean _hasOnlyNonPricedNonAcademicOrders = false;
    protected boolean _showExcludeTBDItemAcademicText = false;
    protected boolean _showExcludeTBDItemNonAcademicText = false;
    protected boolean _showExcludeTBDItemText = false;
    protected List<PurchasablePermission> _academicCartItems;
    protected List<PurchasablePermission> _nonAcademicCartItems;
    protected String _academicTotal;
    protected String _nonAcademicTotal;
    protected int _selectedItemIndex;
    protected CourseDetails _courseDetails;
 

    

    //private List _cascadeUpdateErrorItemDisplayStrings;

     public void setHasOnlySpecialOrders( boolean onlySpec )
     {
         this._hasOnlySpecialOrders = onlySpec;
     }

     public boolean getHasOnlySpecialOrders()
     {
         return _hasOnlySpecialOrders;
     }     

     public void setHasOnlyNonPricedOrders( boolean onlySpec )
     {
         this._hasOnlyNonPricedOrders = onlySpec;
     }

     public boolean getHasOnlyNonPricedOrders()
     {
         return _hasOnlyNonPricedOrders;
     }
     
     
     public void setHasOnlyNonPricedAcademicOrders( boolean hasOnlyNonPricedAcademicOrders )
     {
         this._hasOnlyNonPricedAcademicOrders = hasOnlyNonPricedAcademicOrders;
     }

     public boolean getHasOnlyNonPricedAcademicOrders()
     {
         return _hasOnlyNonPricedAcademicOrders;
     }
     
     
     
     public void setHasOnlyNonPricedNonAcademicOrders( boolean hasOnlyNonPricedNonAcademicOrders )
     {
         this._hasOnlyNonPricedNonAcademicOrders = hasOnlyNonPricedNonAcademicOrders;
     }

     public boolean getHasOnlyNonPricedNonAcademicOrders()
     {
         return _hasOnlyNonPricedNonAcademicOrders;
     }
     
     
     
     
     public void setShowExcludeTBDItemAcademicText( boolean showExcludeTBDItemAcademicText )
     {
         this._showExcludeTBDItemAcademicText = showExcludeTBDItemAcademicText;
     }

     public boolean getShowExcludeTBDItemAcademicText()
     {
         return _showExcludeTBDItemAcademicText;
     }
     
     
     public void setShowExcludeTBDItemNonAcademicText( boolean showExcludeTBDItemNonAcademicText )
     {
         this._showExcludeTBDItemNonAcademicText = showExcludeTBDItemNonAcademicText;
     }

     public boolean getShowExcludeTBDItemNonAcademicText()
     {
         return _showExcludeTBDItemNonAcademicText;
     }
     
     public void setShowExcludeTBDItemText( boolean showExcludeTBDItemAText )
     {
         this._showExcludeTBDItemText = showExcludeTBDItemAText;
     }

     public boolean getShowExcludeTBDItemText()
     {
         return _showExcludeTBDItemText;
     }     
     
     
     public void setHasOnlyAcademicSpecialOrders( boolean hasOnlyAcademicSpecialOrders )
     {
         this._hasOnlyAcademicSpecialOrders = hasOnlyAcademicSpecialOrders;
     }

     public boolean getHasOnlyAcademicSpecialOrders()
     {
         return _hasOnlyAcademicSpecialOrders;
     } 
     public void setHasOnlyNonAcademicSpecialOrders( boolean hasOnlyNonAcademicSpecialOrders )
     {
         this._hasOnlyNonAcademicSpecialOrders = hasOnlyNonAcademicSpecialOrders;
     }

     public boolean getHasOnlyNonAcademicSpecialOrders()
     {
         return _hasOnlyNonAcademicSpecialOrders;
     }   

     
    
    
    public void setAcademicCartItems(List<PurchasablePermission> academicCartItems)
    {
        this._academicCartItems = academicCartItems;
    }

    public List<PurchasablePermission> getAcademicCartItems()
    {
        return _academicCartItems;
    }
            
    public void setNonAcademicCartItems(List<PurchasablePermission> nonAcademicCartItems)
    {
        this._nonAcademicCartItems = nonAcademicCartItems;
    }

    public List<PurchasablePermission> getNonAcademicCartItems()
    {
        return _nonAcademicCartItems;
    }

    public int getNumberOfAcademicCartItems()
    {
        if(_academicCartItems == null) return 0;
        else return _academicCartItems.size();
    }
    
    public int getNumberOfNonAcademicCartItems()
    {
        if(_nonAcademicCartItems == null) return 0;
        else return _nonAcademicCartItems.size();
    }

    public void setCartTotal(String cartTotal)
    {
        this._cartTotal = cartTotal;
        setCartTotalWithNoDollarSign(cartTotal);
    }

    public String getCartTotal()
    {
        return _cartTotal;
    }
    
    public void setCartTotalWithNoDollarSign( String cartTotal )
    {
    	String formattedCartTotal = cartTotal.substring(1).trim();
        this._cartTotalWithNoDollarSign = formattedCartTotal;
                
    }

    public String getCartTotalWithNoDollarSign()
    {
    	return _cartTotalWithNoDollarSign;
    }
    public void setCartChargeTotal(String cartChargeTotal)
    {
        this._cartChargeTotal = cartChargeTotal;
        setCartChargeTotalWithNoDollarSign(cartChargeTotal);
    }

    public String getCartChargeTotal()
    {
        return _cartChargeTotal;
    }
    
    public void setCartChargeTotalWithNoDollarSign( String cartTotal )
    {
    	String formattedCartTotal = cartTotal.substring(1).trim();
        this._cartChargeTotalWithNoDollarSign = formattedCartTotal;
                
    }

    public String getCartChargeTotalWithNoDollarSign()
    {
    	return _cartChargeTotalWithNoDollarSign;
    }
    
    public void setAcademicCartTotal(String cartAcademicTotal)
    {
        this._academicTotal = cartAcademicTotal;
    }

    public String getAcademicCartTotal()
    {
        return _academicTotal;
    }
    
    public void setNonAcademicCartTotal(String cartNonAcademicTotal)
    {
        this._nonAcademicTotal = cartNonAcademicTotal;
    }

    public String getNonAcademicCartTotal()
    {
        return _nonAcademicTotal;
    }

    public void setAcademicCart(boolean academicCart)
    {
        this._academicCart = academicCart;
    }

    public boolean isAcademicCart()
    {
        return _academicCart;
    }

   

    public void setContainsSpecialOrders(boolean containsSpecialOrders)
    {
        this._containsSpecialOrders = containsSpecialOrders;
    }

    public boolean getContainsSpecialOrders()
    {
        return _containsSpecialOrders;
    }

    public void setContainsReprintSpecialOrders(boolean containsReprintSpecialOrders)
    {
        this._containsReprintSpecialOrders = containsReprintSpecialOrders;
    }

    public boolean getContainsReprintSpecialOrders()
    {
        return _containsReprintSpecialOrders;
    }
    public void setContainsAcademicSpecialOrders(boolean containsAcademicSpecialOrders)
    {
        this._containsAcademicSpecialOrders = containsAcademicSpecialOrders;
    }

    public boolean getContainsAcademicSpecialOrders()
    {
        return _containsAcademicSpecialOrders;
    }
    public void setContainsNonAcademicSpecialOrders(boolean containsNonAcademicSpecialOrders)
    {
        this._containsNonAcademicSpecialOrders = containsNonAcademicSpecialOrders;
    }

    public boolean getContainsNonAcademicSpecialOrders()
    {
        return _containsNonAcademicSpecialOrders;
    }
    
    
    public void setContainsPermissionSpecialOrders(boolean containsPermissionSpecialOrders)
    {
        this._containsPermissionSpecialOrders = containsPermissionSpecialOrders;
    }

    public boolean getContainsPermissionSpecialOrders()
    {
        return _containsPermissionSpecialOrders;
    }

    
    public void setHasNonReprintSpecialOrders(boolean hasNonReprintSpecialOrders)
    {
        this._hasNonReprintSpecialOrders = hasNonReprintSpecialOrders;
    }

    public boolean getHasNonReprintSpecialOrders()
    {
        return _hasNonReprintSpecialOrders;
    }

    
    
    public void setHasReprintSpecialOrders(boolean hasReprintSpecialOrders)
    {
        this._hasReprintSpecialOrders = hasReprintSpecialOrders;
    }

    public boolean getHasReprintSpecialOrders()
    {
        return _hasReprintSpecialOrders;
    }

    public void setContainsChargeItems(boolean containsChargeItems)
    {
        this._containsChargeItems = containsChargeItems;
    }

    public boolean getContainsChargeItems()
    {
        return _containsChargeItems;
    }
    
    
    public void setContainsPricedSpecialOrderItem(boolean containsPricedSpecialOrderItem)
    {
        this._containsPricedSpecialOrderItem = containsPricedSpecialOrderItem;
    }

    public boolean getContainsPricedSpecialOrderItem()
    {
        return _containsPricedSpecialOrderItem;
    }
    
    public void setCourseHeader(CourseDetails courseHeader)
    {
        this._courseHeader = courseHeader;
    }

    public CourseDetails getCourseHeader()
    {
        return _courseHeader;
    }
    public void setSelectedItemIndex(int selectedItemIndex)
    {
        this._selectedItemIndex = selectedItemIndex;
    }

    public int getSelectedItemIndex()
    {
        return _selectedItemIndex;
    }
    
    
    public void setCourseDetails( CourseDetails courseDetails )
    {
        this._courseDetails = courseDetails;
    }

    public CourseDetails getCourseDetails()
    {
        return _courseDetails;
    }

    
    public void intializeCartFormItems(){
    	
   
        
        if(CartServices.isAcademicCart()) {
           setCourseHeader(CartServices.getCourseDetails());
           setCourseDetails(CartServices.getCourseDetails());
            setAcademicCartTotal(CartServices.getAcademicOrderItemsTotal());
            setAcademicCartItems(CartServices.getAcademicOrderItemsInCart());
            setAcademicCart(true);
            }
        else {
           setCourseHeader(null);
           setCourseDetails(null);
          setAcademicCartItems(CartServices.getAcademicOrderItemsInCart());
          setAcademicCart(false);
          setAcademicCartItems(null);
         setAcademicCartTotal(null);
          
              }
        
        
        setNonAcademicCartTotal(CartServices.getNonAcademicOrderItemsTotal());
        setNonAcademicCartItems(CartServices.getNonAcademicOrderItemsInCart());

        setCartTotal(CartServices.getCartTotal());
        setCartChargeTotal(CartServices.getCartChargeTotal());
        
        setContainsAcademicSpecialOrders(CartServices.containsAcademicSpecialOrders());
        
        setContainsNonAcademicSpecialOrders(CartServices.containsNonAcademicSpecialOrders());
        setContainsSpecialOrders(CartServices.containsSpecialOrders());
        setHasNonReprintSpecialOrders(CartServices.hasNonReprintSpecialOrders());
        setHasReprintSpecialOrders(CartServices.hasReprintSpecialOrders());
        setHasOnlySpecialOrders(CartServices.hasOnlySpecialOrders());
        setHasOnlyAcademicSpecialOrders(CartServices.hasOnlyAcademicSpecialOrders());
        setHasOnlyNonAcademicSpecialOrders(CartServices.hasOnlyNonAcademicSpecialOrders());
        
        setHasOnlyNonPricedOrders(CartServices.hasOnlyNonPricedOrders());
        setHasOnlyNonPricedAcademicOrders(CartServices.hasOnlyNonPricedAcademicOrders());
        setHasOnlyNonPricedNonAcademicOrders(CartServices.hasOnlyNonPricedNonAcademicOrders());
        setContainsPricedSpecialOrderItem(CartServices.containsPricedSpecialOrderItem());
        setContainsChargeItems(CartServices.containsChargeItems());
        setShowExcludeTBDItemAcademicText(CartServices.showExcludeTBDAcademicItemText());
        setShowExcludeTBDItemNonAcademicText(CartServices.showExcludeTBDNonAcademicItemText());
        setShowExcludeTBDItemText(CartServices.showExcludeTBDItemText());

    }
    
    
    public  boolean getDisplayChargeTotal(){
    	if(getCartChargeTotal()!=null && StringUtils.isNotEmpty(getCartChargeTotal())  && 
    			getCartChargeTotal().compareToIgnoreCase(getCartTotal())!=0){
    		return true;
    	}
    	return false;
    }

    
    public boolean getShowSectionHeaders(){
    	if(getNumberOfAcademicCartItems()>0 && getNumberOfNonAcademicCartItems()>0){
    		return true;
    	}
    	return false;
    }
    

}
