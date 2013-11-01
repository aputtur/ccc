package com.copyright.ccc.web.forms.coi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.copyright.ccc.business.data.UpdatedCartItem;
import com.copyright.ccc.business.services.cart.PurchasablePermission;

public class CartForm extends CartBaseForm
{
    /**
	 * 
	 */
    private static final Logger LOGGER = Logger.getLogger( CartForm.class );
	private static final long serialVersionUID = 1L;
    private String _selectedItem;
    private List<PurchasablePermission> _cartItems;
    
    
    private String _unableToModifyCartErrorHeaderMessageKey; //can happen when adding or updating cart, copying cart, cascade update to cart
    
    private List<PurchasablePermission> _preUpdatedCartItems=new ArrayList<PurchasablePermission>();
    private List<UpdatedCartItem> _updatedAcademicCartItems=new ArrayList<UpdatedCartItem>();;
    private List<UpdatedCartItem> _updatedNonAcademicCartItems=new ArrayList<UpdatedCartItem>();;
    private List<String> _unableToModifyCartErrorDisplayStrings;
    
    private String _removedIdnoWT;
    private boolean isRemovedIdnoWT=false;
    private boolean _chkRightsLnkDown=false; 
    private boolean _isCartItemEdited=false;
    private boolean _hasCartErrors=false;

    
    private Map<Long,String> _priceUpdatedCartItems=new HashMap<Long,String>();
    
    
 
    public int getNumberOfCartItems()
    {
        if(_cartItems == null) return 0;
        else return _cartItems.size();
    }
 
    public void setSelectedItem(String selectedItem)
    {
        this._selectedItem = selectedItem;
    }

    public String getSelectedItem()
    {
        return _selectedItem;
    }
    public void setCartItems(List<PurchasablePermission> cartItems)
    {
        this._cartItems = cartItems;
        LOGGER.debug(" * MSJ * In CartForm.setCartItems(List)");
    }

    public List<PurchasablePermission> getCartItems()
    {
        return _cartItems;
    }
  


    public void setUnableToModifyCartErrorDisplayStrings(List<String> unableToModifyCartErrorDisplayStrings)
    {
        this._unableToModifyCartErrorDisplayStrings = unableToModifyCartErrorDisplayStrings;
    }

    public List<String> getUnableToModifyCartErrorDisplayStrings()
    {
        return _unableToModifyCartErrorDisplayStrings;
    }

    public void setUnableToModifyCartErrorHeaderMessageKey(String unableToModifyCartErrorHeaderMessageKey)
    {
        this._unableToModifyCartErrorHeaderMessageKey = unableToModifyCartErrorHeaderMessageKey;
    }

    public String getUnableToModifyCartErrorHeaderMessageKey()
    {
        return _unableToModifyCartErrorHeaderMessageKey;
    }
    
   
	public void setPriceUpdatedCartItems(Map<Long,String> priceUpdatedCartItems) {
		_priceUpdatedCartItems = priceUpdatedCartItems;
	}

	public Map<Long,String> getPriceUpdatedCartItems() {
		return _priceUpdatedCartItems;
	}
	public String getRePriceMsg(Long itemId){
		if(getPriceUpdatedCartItems().get(itemId)!=null){
			return getPriceUpdatedCartItems().get(itemId);
		
		}
		return "";
	}

	public void setPreUpdatedCartItems(List<PurchasablePermission> _preUpdatedCartItems) {
		this._preUpdatedCartItems = _preUpdatedCartItems;
	}

	public List<PurchasablePermission> getPreUpdatedCartItems() {
		if(_preUpdatedCartItems==null)
			_preUpdatedCartItems=new ArrayList<PurchasablePermission>();
		return _preUpdatedCartItems;
	}

	

	
	public Boolean getHasCartItemUpdates() {
		if(getUpdatedAcademicCartItems().size()>0 || getUpdatedNonAcademicCartItems().size()>0){
			return true;
		}
		
	
		return false;
	}
	
	public Boolean getHasCartUpdatedToPriceChangedItems() {
		return getHasAcademicPriceUpdates() ||  getHasNonAcademicPriceUpdates();
	}
	public Boolean getHasCartUpdatedToSpecialOrderItems() {
		return getHasAcademicChangedToSOItems() ||  getHasNonAcademicChangedToSOItems();
	}
	public Boolean getHasCartUpdatedToRemovedItems() {
		return getHasAcademicRemovedItems() ||  getHasNonAcademicRemovedItems();
	}
    public  Boolean  getHasAcademicPriceUpdates(){
    	for(UpdatedCartItem updatePermissions:getUpdatedAcademicCartItems()){
    		if(updatePermissions.getIsUpdatedToPriceChange()){
    			return true;
    		}
    	}
    	return false;
    }
    public Boolean  getHasNonAcademicPriceUpdates(){
    	for(UpdatedCartItem updatePermissions:getUpdatedNonAcademicCartItems()){
    		if(updatePermissions.getIsUpdatedToPriceChange()){
    			return true;
    		}
    	}
    	return false;
    }
    public Boolean  getHasAcademicRemovedItems(){
    	for(UpdatedCartItem updatePermissions:getUpdatedAcademicCartItems()){
    		if(updatePermissions.getIsUpdatedToRemoveItems()){
    			return true;
    		}
    	}
    	return false;
    }
    public Boolean  getHasNonAcademicRemovedItems(){
    	for(UpdatedCartItem updatePermissions:getUpdatedNonAcademicCartItems()){
    		if(updatePermissions.getIsUpdatedToRemoveItems()){
    			return true;
    		}
    	}
    	return false;
    }
    public Boolean  getHasAcademicChangedToSOItems(){
    	for(UpdatedCartItem updatePermissions:getUpdatedAcademicCartItems()){
    		if(updatePermissions.getIsUpdatedToSpecialOrder()){
    			return true;
    		}
    	}
    	return false;
    }
    public Boolean getHasNonAcademicChangedToSOItems(){
    	for(UpdatedCartItem updatePermissions:getUpdatedNonAcademicCartItems()){
    		if(updatePermissions.getIsUpdatedToSpecialOrder()){
    			return true;
    		}
    	}
    	return false;
    }
    public Boolean getIsPriceUpdated(long ItemId){
    	for(UpdatedCartItem updatePermissions:getUpdatedAcademicCartItems()){
    		if((updatePermissions.getIsUpdatedToPriceChange() || updatePermissions.getIsUpdatedToSpecialOrder()) && updatePermissions.getCartItem().getItem().getItemId().compareTo(ItemId)==0){
    			return true;
    		}
    	}
    	for(UpdatedCartItem updatePermissions:getUpdatedNonAcademicCartItems()){
    		if((updatePermissions.getIsUpdatedToPriceChange() || updatePermissions.getIsUpdatedToSpecialOrder()) && updatePermissions.getCartItem().getItem().getItemId().compareTo(ItemId)==0){
    			return true;
    		}
    	}
    	return false;
    	
    }
    
     private Map<Long,String> priceUpdatedItem = new HashMap<Long,String>();  
        
     public Map<Long,String> getPriceUpdatedItem() {  
     return priceUpdatedItem;  
     }  
        
     public void setPriceUpdatedItem(Map<Long,String> priceUpdatedItem) {  
     this.priceUpdatedItem = priceUpdatedItem;  
     }

	public void setUpdatedAcademicCartItems(
			List<UpdatedCartItem> updatedAcademicCartItems) {
		this._updatedAcademicCartItems = updatedAcademicCartItems;
	}

	public List<UpdatedCartItem> getUpdatedAcademicCartItems() {
		
		return _updatedAcademicCartItems;
	}

	public void setUpdatedNonAcademicCartItems(
			List<UpdatedCartItem> updatedNonAcademicCartItems) {
		this._updatedNonAcademicCartItems = updatedNonAcademicCartItems;
	}

	public List<UpdatedCartItem> getUpdatedNonAcademicCartItems() {
		
		return _updatedNonAcademicCartItems;
	}
	
	public String getIdnoWT()
	{
		String idno = "";
		if (getCartItems() != null)
		{
			for (PurchasablePermission perm:getCartItems())
			{						
				if (idno != null && !idno.equals(""))
				{
					idno = idno + ";" + perm.getStandardNumber();
				}
				else
				{
					idno = perm.getStandardNumber();
				}
			}
			
		}
		
		return idno;
	}

	public void setRemovedIdnoWT(String removedIdnoWT) {
		this._removedIdnoWT = removedIdnoWT;
	}

	public String getRemovedIdnoWT() {
		return _removedIdnoWT;
	}

	public void setIsRemovedIdnoWT(boolean isRemovedIdnoWT) {
		this.isRemovedIdnoWT = isRemovedIdnoWT;
	}

	public boolean getIsRemovedIdnoWT() {
		return isRemovedIdnoWT;
	}

	public void setChkRightsLnkDown(boolean _chkRightsLnkDown) {
		this._chkRightsLnkDown = _chkRightsLnkDown;
	}

	public boolean getChkRightsLnkDown() {
		return _chkRightsLnkDown;
	}

	public String getFundingCurrencyAmount() {
		return null;
	}

	public String getFundingCurrencyType() {
		return null;
	}

	public void setIsCartItemEdited(boolean isCartItemEdited) {
		_isCartItemEdited = isCartItemEdited;
	}

	public boolean getIsCartItemEdited() {
		return _isCartItemEdited;
	}

	public void setHasCartErrors(boolean hasCartErrors) {
		this._hasCartErrors = hasCartErrors;
	}

	public boolean getHasCartErrors() {
		return _hasCartErrors;
	}

        
    /* public LabelValueBean getPriceUpdatedItem(String key) {
    	 if(priceUpdatedItem.containsKey(Long.valueOf(key)))
    		 	return  priceUpdatedItem.get(key);
    		 	else
    		 		return new LabelValueBean("0","false");
     } 
    */
    

}
