package com.copyright.ccc.business.data;

import java.io.Serializable;

import com.copyright.ccc.business.services.cart.PurchasablePermission;

public class UpdatedCartItem implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private PurchasablePermission cartItem=null;
	private Integer cartIndex=0;
	private boolean isUpdatedToSpecialOrder=false;
	private boolean isUpdatedToPriceChange=false;
	private boolean isUpdatedToRemoveItems=false;
	
	public UpdatedCartItem(){
		
	}
	public UpdatedCartItem(PurchasablePermission cartItem,boolean isUpdatedToSpecialOrder,boolean isUpdatedToPriceChange,boolean isUpdatedToRemoveItems,Integer cartIndex){
		this.cartItem=cartItem;
		this.isUpdatedToSpecialOrder=isUpdatedToSpecialOrder;
			this.isUpdatedToPriceChange=isUpdatedToPriceChange;
				this.isUpdatedToRemoveItems=isUpdatedToRemoveItems;
				this.cartIndex=cartIndex;
		
	}
	public PurchasablePermission getCartItem() {
		return cartItem;
	}
	public void setCartItem(PurchasablePermission cartItem) {
		this.cartItem = cartItem;
	}
	public Integer getCartIndex() {
		return cartIndex;
	}
	public void setCartIndex(Integer cartIndex) {
		this.cartIndex = cartIndex;
	}
	public boolean getIsUpdatedToSpecialOrder() {
		return isUpdatedToSpecialOrder;
	}
	public void setUpdatedToSpecialOrder(boolean isUpdatedToSpecialOrder) {
		this.isUpdatedToSpecialOrder = isUpdatedToSpecialOrder;
	}
	public boolean getIsUpdatedToPriceChange() {
		return isUpdatedToPriceChange;
	}
	public void setUpdatedToPriceChange(boolean isUpdatedToPriceChange) {
		this.isUpdatedToPriceChange = isUpdatedToPriceChange;
	}
	public boolean getIsUpdatedToRemoveItems() {
		return isUpdatedToRemoveItems;
	}
	public void setUpdatedToRemoveItems(boolean isUpdatedToRemoveItems) {
		this.isUpdatedToRemoveItems = isUpdatedToRemoveItems;
	}
	
	public String getPublicationTitle(){
		return cartItem.getPublicationTitle();
	}
	public Long getItemId(){
		return cartItem.getItem().getItemId();
	}
	
	
	

}
