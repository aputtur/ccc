package com.copyright.ccc.web.actions.ordermgmt;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.copyright.ccc.business.data.OrderPurchase;
import com.copyright.ccc.web.actions.ordermgmt.data.ConfirmationWrapper;
import com.copyright.ccc.web.actions.ordermgmt.data.ItemWrapper;
import com.copyright.ccc.web.actions.ordermgmt.data.OrderSearchResultWrapper;

public class ViewExpandedDetailAction extends OMBaseAction {


	private static final long serialVersionUID = 1L;
		
	private OrderSearchResultWrapper wrappedResults;
	private ConfirmationWrapper selectedOrder;
	
	private OrderPurchase editOrder = null;
	private ItemWrapper myItem = null;
	
	private Long searchConfirmNumber;
	private Long searchBundleNumber;
	private Long searchDetailNumber;
	
	/**
	 * Default action - executed when page is first reached
	 */
	@Override
	public String execute()
	{		
		
		setSelectedFields();
		
		wrappedResults = super.getOrderSearchResultsWrapperInSession();
		
			if ( wrappedResults != null ) {
				List<ConfirmationWrapper> orders = wrappedResults.getOrderListInHierarchicalOrder();
			    if ( orders != null && !orders.isEmpty() ) {	
			    	selectedOrder = orders.get(0);
			    	editOrder = selectedOrder.getConfirmation();
			    	for ( ItemWrapper iw : wrappedResults.getDetailsList() ) {
			    		if ( searchDetailNumber.equals(iw.getItem().getID()) ) {
			    			myItem = iw;
			    			break;
			    		}
			    	}			    	
			    }
			} else {
				addActionError("Unable to find confirmation");
			}
		
		
		return SUCCESS;
	}
	public ItemWrapper getMyItem() {
		return myItem;
	}
	public void setMyItem(ItemWrapper myItem) {
		this.myItem = myItem;
	}
	/*
	public static final String ORDER_HEADER = "ORDER_HEADER";
	public static final String BUNDLE_HEADER = "BUNDLE_HEADER";
	public static final String BUNDLE_DETAIL = "BUNDLE_DETAIL";
	public static final String ORDER_DETAIL = "ORDER_DETAIL";
	*/
	private void setSelectedFields() {
				
		setSearchConfirmNumber(null);
		
		if ( StringUtils.isNotEmpty(getSelectedConfirmNumber() ) ) {
			if ( StringUtils.isNumeric(getSelectedConfirmNumber()) ) {
				setSearchConfirmNumber(Long.valueOf(getSelectedConfirmNumber()));
			}
		}
		
		setSearchBundleNumber(null);
		
		if ( StringUtils.isNotEmpty(getSelectedBundleNumber() ) ) {
			if ( StringUtils.isNumeric(getSelectedBundleNumber()) ) {
				setSearchBundleNumber(Long.valueOf(getSelectedBundleNumber()));
			}
		}
		
		setSearchDetailNumber(null);
		
		if ( StringUtils.isNotEmpty(getSelectedDetailNumber() ) ) {
			if ( StringUtils.isNumeric(getSelectedDetailNumber()) ) {
				setSearchDetailNumber(Long.valueOf(getSelectedDetailNumber()));
			}
		}
	}

	public Long getSearchConfirmNumber() {
		return searchConfirmNumber;
	}
	public void setSearchConfirmNumber(Long searchConfirmNumber) {
		this.searchConfirmNumber = searchConfirmNumber;
	}
	public OrderSearchResultWrapper getWrappedResults() {
		return wrappedResults;
	}
	public void setWrappedResults(OrderSearchResultWrapper wrappedResults) {
		this.wrappedResults = wrappedResults;
	}
	public ConfirmationWrapper getSelectedOrder() {
		return selectedOrder;
	}
	public void setSelectedOrder(ConfirmationWrapper selectedOrder) {
		this.selectedOrder = selectedOrder;
	}
	public OrderPurchase getEditOrder() {
		return editOrder;
	}
	public void setEditOrder(OrderPurchase editOrder) {
		this.editOrder = editOrder;
	}
	public Long getSearchBundleNumber() {
		return searchBundleNumber;
	}
	public void setSearchBundleNumber(Long searchBundleNumber) {
		this.searchBundleNumber = searchBundleNumber;
	}
	public Long getSearchDetailNumber() {
		return searchDetailNumber;
	}
	public void setSearchDetailNumber(Long searchDetailNumber) {
		this.searchDetailNumber = searchDetailNumber;
	}
	@Override
	public QuickTabSelect getDefaultQuickTabSelect() {
		return QuickTabSelect.ordermgmt_menu_order;
	}	

}
