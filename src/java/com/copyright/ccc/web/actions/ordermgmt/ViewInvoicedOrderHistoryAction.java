package com.copyright.ccc.web.actions.ordermgmt;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.copyright.ccc.business.security.CCPrivilegeCode;
import com.copyright.ccc.business.services.OrderDataSourceEnum;
import com.copyright.ccc.web.actions.ordermgmt.data.ConfirmationWrapper;
import com.copyright.ccc.web.actions.ordermgmt.data.EditConfirmation;
import com.copyright.ccc.web.actions.ordermgmt.data.EditItemWrapper;
import com.copyright.ccc.web.actions.ordermgmt.data.ItemWrapper;
import com.copyright.ccc.web.actions.ordermgmt.data.OrderSearchResultWrapper;
import com.copyright.ccc.web.actions.ordermgmt.data.OrderServicesHelper;
import com.copyright.ccc.web.actions.ordermgmt.data.ProcessMessage;
import com.copyright.ccc.web.actions.ordermgmt.util.MiscUtils;

public class ViewInvoicedOrderHistoryAction extends ViewOrderHistoryBaseAction implements CustomPageControlAware{


	private static final long serialVersionUID = 1L;	
	private ItemWrapper firstDetail;

	/**
	 * Default action - executed when page is first reached
	 */
	
	@Override
	public String execute()
	{				
		if (!super.verifyPrivilege(CCPrivilegeCode.SEARCH_ORDERS))
			return NOT_AUTHORIZED;
		
		super.execute();
		
		setExpandHeaderAndDetailFlags();

		setIncludeBundles(true);
		setExpandInvoiceHeader( true );
		setIncludeConfirmPageLinks( true );
		
		List<ProcessMessage> prMessages = getProcessMessagesListInSession();
		List<EditItemWrapper> prEdits = getProcessEditsListInSession();
		EditConfirmation cfEdits = getConfirmEditsListInSession();
		
		if ( (prMessages != null && !prMessages.isEmpty()) 
		      || (prEdits != null && !prEdits.isEmpty())) {
		   	if ( isShowAllProcessMesssages() ) {
		   		setAllProcessMessages(prMessages);
		   	} else {
		    	postProcessMessagesToDetails(getWrappedResults().getDetailsList(), prMessages, prEdits);
		   		postProcessMessagesToOrder(getWrappedResults().getOrderListInHierarchicalOrder(), prMessages, prEdits);
		   	}
		}
		
		List<ConfirmationWrapper> orders = getWrappedResults().getOrderListInHierarchicalOrder();
		if ( orders != null && !orders.isEmpty() ) {	
		   	setSelectedOrder( orders.get(0) );
		   	setEditOrder( getSelectedOrder().getConfirmation() );
		   	if ( StringUtils.isNotEmpty( getSavingInvoice() ) ) {
		   		getSelectedOrder().setProcessMessages(prMessages);
		   		if ( cfEdits != null ) {
		   			getSelectedOrder().setLastEdit(cfEdits);
		   		}
		   	}
		}
		List<ItemWrapper> detList = getWrappedResults().getDetailsList();
		if (detList != null && !detList.isEmpty()) {
		    setFirstDetail( detList.get(0) );
		}
		
		setHaveDetailsStoredInOMS(getWrappedResults().isHaveDetailsStoredInOMS());
		setHaveDetailsStoredInTF(getWrappedResults().isHaveDetailsStoredInTF());
		setHaveNonInvoicedDetails(getWrappedResults().isHaveNonInvoicedDetails());
		setHaveOpenDetails(getWrappedResults().isHaveOpenDetails());

		removeProcessEditsListFromSession();
		removeProcessMessagesListFromSession();
		removeConfirmEditsListFromSession();
		
		setStartRenderTime(MiscUtils.getNow());
		
//		storeOrderSearchResultsWrapperInSession(getWrappedResults());
	
		OrderSearchResultWrapper[] orderSearchResultWrapper = new OrderSearchResultWrapper[1];
		orderSearchResultWrapper[0] = getWrappedResults();
		
		storeOrderSearchResultsWrapperArrayInSession( orderSearchResultWrapper );
		
		storeOrderSearchCriteriaInSession(getSearchCriteria(), OMBaseAction.VIEW_ORDER_SEARCH_CRITERIA_KEY);
		storeCustomPageControlInSession(getPageControl());
		
		setRePagingOrSizing(false);

		return SUCCESS;
	}
	
	public String saveDetails() {
		
		super.handleQuickTabSelected();
		
		setSelectedConfirmBundleInvoiceDetailFields();
		setExpandHeaderAndDetailFlags();

		List<ProcessMessage> processMessages = new ArrayList<ProcessMessage>();
		List<EditItemWrapper> processEdits = new ArrayList<EditItemWrapper>();
		removeProcessMessagesListFromSession();
		setWrappedResultsArray(getOrderSearchResultsWrapperArrayInSession());
		
		EditItemWrapper jumpToFirstDetail = null;
		EditItemWrapper jumpToFirstErrorDetail = null;
		List<ProcessMessage> detailMessages = null;
		
		boolean saveProcessHasErrors = false;
		boolean savingMultipleDetails = false;
		boolean savingCoiDetails = false;
		int saveCtr = 0;

		for ( EditItemWrapper eiw : getEditDetails() ) {
			if ( eiw.isToBeSaved() ) {
				saveCtr++;
			}
		}
		if (saveCtr > 1) {
			savingMultipleDetails = true;
		}
		
		EditItemWrapper firstDetailToSave = null;
		for ( EditItemWrapper eiw : getEditDetails() ) {
			if ( eiw.isToBeSaved() ) {
				if (firstDetailToSave == null) {
					firstDetailToSave = eiw;
				}
				if ( jumpToFirstDetail == null ) { jumpToFirstDetail = eiw; }
				for (OrderSearchResultWrapper orderSearchResultWrapper : getWrappedResultsArray()) {
					for (ItemWrapper itemWrapper : orderSearchResultWrapper.getDetailsList()) {
						if (itemWrapper.getItem().getOrderDataSource() == OrderDataSourceEnum.OMS.getOrderDataSourceId().intValue()) {
							savingCoiDetails = true;
						}
						if (itemWrapper.getItem().getID() == Long.valueOf(eiw.getItem().getID()).longValue()) {
							detailMessages = OrderServicesHelper.saveOrderDetailOnConfirmationPage( eiw, itemWrapper, false, savingCoiDetails, savingMultipleDetails );								
						}
					}
				}				
				if ( detailMessages != null && !detailMessages.isEmpty() ) {
					// check for errors to restore edits 
					for ( ProcessMessage pm : detailMessages ) {
						if ( pm.isError() ) {
							if ( jumpToFirstErrorDetail == null ) { jumpToFirstErrorDetail = eiw; }
							// restore edits
							eiw.setToBeReturned(true);
							processEdits.add(eiw);
							break;
						}
					}
					// save messages
					processMessages.addAll(detailMessages);
				}
				
			} else if ( eiw.isToBeReturned() ) {
				processEdits.add(eiw);
			}
		}
		
		if (savingCoiDetails && savingMultipleDetails && !saveProcessHasErrors) {
			List<ProcessMessage> coiSaveProcessMessages = OrderServicesHelper.saveCoiDetailsInBulk(getWrappedResultsArray());
			if (coiSaveProcessMessages != null && !coiSaveProcessMessages.isEmpty()) {
				if ( jumpToFirstErrorDetail == null ) { jumpToFirstErrorDetail = firstDetailToSave; }				
				processMessages.clear();
				storeAllProcessMessagesListInSession(coiSaveProcessMessages);
			}
		}		
		
		if ( jumpToFirstErrorDetail != null ) {
			setSelectedFieldsFor( jumpToFirstErrorDetail );
		} else if ( jumpToFirstDetail != null ) {
			setSelectedFieldsFor( jumpToFirstDetail );
		}
		
		storeProcessMessagesListInSession(processMessages);
		storeProcessEditsListInSession(processEdits);
		
		return SAVE_COMPLETE;
		
		
	}

	public String saveInvoiceHeader() {
		
		super.handleQuickTabSelected();
		
		setSelectedConfirmBundleInvoiceDetailFields();
		setExpandHeaderAndDetailFlags();
		
		List<ProcessMessage> processMessages = new ArrayList<ProcessMessage>();
		removeProcessMessagesListFromSession();
		
		processMessages.add(new ProcessMessage(getSelectedConfirmNumber(), "ERROR", "Invoice header save service is not yet available"));
		
		setSavingInvoice( getSelectedInvoiceNumber() );
		setSelectedItem(OrderManagementConstants.ORDER_HEADER);
		
		storeProcessMessagesListInSession(processMessages);

		return SAVE_INVOICE_HEADER_COMPLETE;
	}
		
	public ItemWrapper getFirstDetail() {
		return firstDetail;
	}

	public void setFirstDetail(ItemWrapper firstDetail) {
		this.firstDetail = firstDetail;
	}
}
