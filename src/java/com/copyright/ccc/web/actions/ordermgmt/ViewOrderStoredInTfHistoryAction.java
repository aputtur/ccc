package com.copyright.ccc.web.actions.ordermgmt;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.copyright.ccc.business.security.CCPrivilegeCode;
import com.copyright.ccc.web.actions.ordermgmt.data.ConfirmationWrapper;
import com.copyright.ccc.web.actions.ordermgmt.data.EditConfirmation;
import com.copyright.ccc.web.actions.ordermgmt.data.EditItemWrapper;
import com.copyright.ccc.web.actions.ordermgmt.data.OrderServicesHelper;
import com.copyright.ccc.web.actions.ordermgmt.data.ProcessMessage;
import com.copyright.ccc.web.actions.ordermgmt.util.MiscUtils;

public class ViewOrderStoredInTfHistoryAction extends ViewOrderHistoryBaseAction implements CustomPageControlAware{


	private static final long serialVersionUID = 1L;
	
	/**
	 * Default action - executed when page is first reached
	 */
	
	@Override
	public String execute()
	{	
		if (!super.verifyPrivilege(CCPrivilegeCode.SEARCH_ORDERS))
			return NOT_AUTHORIZED;
		
		boolean makeSureSelectedDetailIsPresent = true;
		setExpandHeaderAndDetailFlags();
		
		super.execute(makeSureSelectedDetailIsPresent);
		
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
		   	if ( StringUtils.isNotEmpty( getSavingConfirmation() ) ) {
		   		getSelectedOrder().setProcessMessages(prMessages);
		   		if ( cfEdits != null ) {
		   			getSelectedOrder().setLastEdit(cfEdits);
		   		}
		   	}
		}
		
		setHaveDetailsStoredInOMS(getWrappedResults().isHaveDetailsStoredInOMS());
		setHaveDetailsStoredInTF(getWrappedResults().isHaveDetailsStoredInTF());
		setHaveNonInvoicedDetails(getWrappedResults().isHaveNonInvoicedDetails());
		setHaveOpenDetails(getWrappedResults().isHaveOpenDetails());
		
		removeProcessEditsListFromSession();
		removeProcessMessagesListFromSession();
		removeConfirmEditsListFromSession();
		
		setStartRenderTime(MiscUtils.getNow());
		storeOrderSearchResultsWrapperInSession(getWrappedResults());
		storeOrderSearchCriteriaInSession(getSearchCriteria(), OMBaseAction.VIEW_ORDER_SEARCH_CRITERIA_KEY);
		storeCustomPageControlInSession(getPageControl());
		setRePagingOrSizing(false);

		return SUCCESS;
	}
	
/*	public String saveDetails() {
		
		super.handleQuickTabSelected();
		
		setSelectedConfirmBundleInvoiceDetailFields();
		setExpandHeaderAndDetailFlags();

		List<ProcessMessage> processMessages = new ArrayList<ProcessMessage>();
		List<EditItemWrapper> processEdits = new ArrayList<EditItemWrapper>();
		removeProcessMessagesListFromSession();
		
		EditItemWrapper jumpToFirstDetail = null;
		EditItemWrapper jumpToFirstErrorDetail = null;
				
		for ( EditItemWrapper eiw : getEditDetails() ) {
			if ( eiw.isToBeSaved() ) {
				if ( jumpToFirstDetail == null ) { jumpToFirstDetail = eiw; }
				List<ProcessMessage> detailMessages = OrderServicesHelper.saveOrderDetailOnConfirmationPage( eiw, isInvoiceView() );
				
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
		
		if ( jumpToFirstErrorDetail != null ) {
			setSelectedFieldsFor( jumpToFirstErrorDetail );
		} else if ( jumpToFirstDetail != null ) {
			setSelectedFieldsFor( jumpToFirstDetail );
		}
		
		storeProcessMessagesListInSession(processMessages);
		storeProcessEditsListInSession(processEdits);
		
		return SAVE_COMPLETE;
		
		
	}
	public String saveConfirmationHeader() {
		super.handleQuickTabSelected();
		
		setSelectedConfirmBundleInvoiceDetailFields();
		setExpandHeaderAndDetailFlags();
		
		List<ProcessMessage> processMessages = new ArrayList<ProcessMessage>();
		removeProcessMessagesListFromSession();
		
		OrderPurchase op = OrderServicesHelper.getConfirmationOrderPurchaseFor(getSelectedConfirmNumber());
		
		if ( op == null ) {
			processMessages.add(new ProcessMessage(getSelectedConfirmNumber(), "ERROR", "Unable to locate Order Purchase record."));			
		} else {
			// TODO ADD UPDATE HERE...
			// REMOVE NEXT LINE...
			processMessages.add(new ProcessMessage(getSelectedConfirmNumber(), "ERROR", "Confirmation save service is not yet available"));
		}
		
		setSavingConfirmation( getSelectedConfirmNumber() );
		setSelectedItem(OrderManagementConstants.ORDER_HEADER);
		
		if ( !processMessages.isEmpty() ) {
			for ( ProcessMessage pr : processMessages ) {
				if ( pr.isError() ) {
					storeConfirmEditsListInSession(getEditConfirm());
					break;
				}
			}
		}
		storeProcessMessagesListInSession(processMessages);

		return SAVE_CONFIRM_HEADER_COMPLETE;
	}
	
	public String saveBundleHeader() {
		
		super.handleQuickTabSelected();
		
		setSelectedConfirmBundleInvoiceDetailFields();
		setExpandHeaderAndDetailFlags();
		
		List<ProcessMessage> processMessages = new ArrayList<ProcessMessage>();
		removeProcessMessagesListFromSession();
		
		processMessages.add(new ProcessMessage(getSelectedConfirmNumber(), "ERROR", "Project (bundle) header save service is not yet available"));
		
		setSavingBundle( getSelectedBundleNumber() );
		setSelectedItem(OrderManagementConstants.BUNDLE_HEADER);
		
		storeProcessMessagesListInSession(processMessages);

		return SAVE_BUNDLE_HEADER_COMPLETE;
	}
*/		
	public String unCancelOrderDetail() {
		
		super.handleQuickTabSelected();
		
		setSelectedConfirmBundleInvoiceDetailFields();
		setExpandHeaderAndDetailFlags();

		List<ProcessMessage> processMessages = new ArrayList<ProcessMessage>();

		removeProcessMessagesListFromSession();
			
		if ( getSearchDetailNumber() != null && getSearchDetailNumber().longValue() > 0 ) {
			processMessages.addAll(OrderServicesHelper.unCancelOrderLicenseForDetailId(getSearchDetailNumber()));
		} else {
			processMessages.add(new ProcessMessage("Unable to uncancel -  selected detail number is either missing or invalid"));
		}
		
		if ( getSearchBundleNumber() != null && getSearchBundleNumber() > 0 ) {
			setSelectedItem(OrderManagementConstants.BUNDLE_DETAIL);			
		} else {
			setSelectedItem(OrderManagementConstants.ORDER_DETAIL);
		}
		
		storeProcessMessagesListInSession(processMessages);
		
		return SAVE_COMPLETE;	
		
	}
	
	public String cancelOrderDetail() {
		
		super.handleQuickTabSelected();
		
		setSelectedConfirmBundleInvoiceDetailFields();
		setExpandHeaderAndDetailFlags();

		List<ProcessMessage> processMessages = new ArrayList<ProcessMessage>();

		removeProcessMessagesListFromSession();
			
		if ( getSearchDetailNumber() != null && getSearchDetailNumber().longValue() > 0 ) {
			processMessages.addAll(OrderServicesHelper.cancelOrderLicenseForDetailId(getSearchDetailNumber()));
		} else {
			processMessages.add(new ProcessMessage("Unable to cancel -  selected detail number is either missing or invalid"));
		}
		
		if ( getSearchBundleNumber() != null && getSearchBundleNumber() > 0 ) {
			setSelectedItem(OrderManagementConstants.BUNDLE_DETAIL);			
		} else {
			setSelectedItem(OrderManagementConstants.ORDER_DETAIL);
		}
								
		storeProcessMessagesListInSession(processMessages);
		
		return SAVE_COMPLETE;
		
		
	}
	
	public String cancelOpenItemDetails() {
		
		super.handleQuickTabSelected();
		
		setSelectedConfirmBundleInvoiceDetailFields();
		setExpandHeaderAndDetailFlags();
		
		List<ProcessMessage> processMessages = new ArrayList<ProcessMessage>();
		
		if ( getSearchOrderId() != null && getSearchOrderId() > 0 ) {
			
			removeProcessMessagesListFromSession();
		    processMessages.addAll( OrderServicesHelper.cancelOpenDetailsFor( getSearchOrderId().toString() ) );

		} else {
			addActionError("Must supply a confirmation number");
		}
				
		setSelectedItem(OrderManagementConstants.ORDER_HEADER);

		storeProcessMessagesListInSession(processMessages);
		
		return SAVE_ALL_COMPLETE;
		
	}

	@Override
	public String rePageResults() {
		int nPage = -1;
		if ( StringUtils.isNotEmpty(getNewPage()) ) {
			if ( StringUtils.isNumeric(getNewPage().trim()) ) {
				nPage = Integer.valueOf(getNewPage().trim()).intValue();
			}
		}
		setPageControl( getCustomPageControlInSession() );
		if ( nPage > 0 ) {
			getPageControl().setPage(nPage);
		}
		setSearchCriteria( super.getOrderSearchCriteriaInSession(OMBaseAction.VIEW_ORDER_SEARCH_CRITERIA_KEY) );
		setSearching(true);
		setResorting(false);
		setRePagingOrSizing(true);
		
		String executeResult = execute();
		
		if (executeResult == NOT_AUTHORIZED)
			return NOT_AUTHORIZED;
		
		return SUCCESS;
	
	}

	@Override
	public String reSizePageResults() {
		
		int nPageSize = -1;
		if ( StringUtils.isNotEmpty(getNewPageSize()) ) {
			if ( StringUtils.isNumeric(getNewPageSize().trim()) ) {
				nPageSize = Integer.valueOf(getNewPageSize().trim()).intValue();
			}
		}
		setPageControl( getCustomPageControlInSession() );
		if ( nPageSize > 0 ) {
			getPageControl().setPageSize(nPageSize);
			getPageControl().setPage(getPageControl().getPageAfterHandlingPageSizeChange());
			getPageControl().setLastPageSize(nPageSize);
		}
		setSearchCriteria( super.getOrderSearchCriteriaInSession(OMBaseAction.VIEW_ORDER_SEARCH_CRITERIA_KEY) );
		setSearching(true);
		setResorting(false);
		setRePagingOrSizing(true);
		
		String executeResult = execute();
		
		if (executeResult == NOT_AUTHORIZED)
			return NOT_AUTHORIZED;
		
		return SUCCESS;
	
	}

	@Override
	public String resortSearchResults() {
		// Get sort column to sort by and order 
		String sortBy = getSearchCriteria().getSortCriteriaBy();
		String sortOrder = getSearchCriteria().getSortOrder();
		// get saved criteria
		setSearchCriteria( super.getOrderSearchCriteriaInSession(OMBaseAction.VIEW_ORDER_SEARCH_CRITERIA_KEY) );
		getSearchCriteria().setSortCriteriaBy(sortBy);
		getSearchCriteria().setSortOrder(sortOrder);
		super.storeOrderSearchCriteriaInSession(getSearchCriteria());
		setResorting(true);
		setRePagingOrSizing(false);
		setSearching(true);
		
		String executeResult = execute();
		
		if (executeResult == NOT_AUTHORIZED)
			return NOT_AUTHORIZED;
		
		return SUCCESS;
		
	}
	
	


}
