package com.copyright.ccc.web.actions.ordermgmt;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.copyright.ccc.business.security.CCPrivilegeCode;
import com.copyright.ccc.web.actions.ordermgmt.data.ConfirmationWrapper;
import com.copyright.ccc.web.actions.ordermgmt.data.CustomPageControl;
import com.copyright.ccc.web.actions.ordermgmt.data.EditConfirmation;
import com.copyright.ccc.web.actions.ordermgmt.data.EditItemWrapper;
import com.copyright.ccc.web.actions.ordermgmt.data.OrderSearchCriteriaWrapper;
import com.copyright.ccc.web.actions.ordermgmt.data.OrderSearchResultWrapper;
import com.copyright.ccc.web.actions.ordermgmt.data.OrderServicesHelper;
import com.copyright.ccc.web.actions.ordermgmt.data.ProcessMessage;
import com.copyright.ccc.web.actions.ordermgmt.util.MiscUtils;

public class ViewOrderStoredInOmsHistoryAction extends ViewOrderHistoryMultiPagingBaseAction implements CustomPageControlArrayAware{

	private static final long serialVersionUID = 1L;

	/**
	 * Default action - executed when page is first reached
	 */
	
	@Override
	public String execute()
	{		
		if (!super.verifyPrivilege(CCPrivilegeCode.SEARCH_ORDERS))
			return NOT_AUTHORIZED;
		
		int pgIdx = 0;
		if ( StringUtils.isNotEmpty(getRequestedPagingIndex())) {
			pgIdx = Integer.valueOf(getRequestedPagingIndex()).intValue();
		} else {
			pgIdx = -1;
		}
		super.execute(pgIdx);
								
		List<ProcessMessage> prMessages = getProcessMessagesListInSession();
		List<EditItemWrapper> prEdits = getProcessEditsListInSession();
		EditConfirmation cfEdits = getConfirmEditsListInSession();
		
		boolean postProcessMessages = (prMessages != null && !prMessages.isEmpty()) || (prEdits != null && !prEdits.isEmpty());
		if ( postProcessMessages ) {
		   	if ( isShowAllProcessMesssages() ) {
		   		setAllProcessMessages(prMessages);
		   	} 
		}
   		boolean processedConfirmation = false;
   		int pagingIdx = 0;
   		int haveNonInvoicedDetailsCnt = 0;
   		int haveDetailsStoredInTFCnt = 0;
   		int haveDetailsStoredInOMSCnt = 0;
   		int haveAdjustedDetailsCnt = 0;
   		int haveOpenDetailsCnt = 0;
  		for ( OrderSearchResultWrapper orderResults : getWrappedResultsArray() ) {
  			if ( orderResults.isHaveNonInvoicedDetails() ) { haveNonInvoicedDetailsCnt++; }
  			if ( orderResults.isHaveDetailsStoredInTF() )  { haveDetailsStoredInTFCnt++; }
  			if ( orderResults.isHaveDetailsStoredInOMS() ) { haveDetailsStoredInOMSCnt++; }
  			if ( orderResults.isHaveAdjustedDetails() ) { haveAdjustedDetailsCnt++; }
  			if ( orderResults.isHaveOpenDetails() )        { haveOpenDetailsCnt++; }
   			if ( postProcessMessages ) {
   				postProcessMessagesToDetails(orderResults.getDetailsList(), prMessages, prEdits);
   				postProcessMessagesToOrder(orderResults.getOrderListInHierarchicalOrder(), prMessages, prEdits);
   			}
   			if ( !processedConfirmation ) {
   				processedConfirmation = true;
   				List<ConfirmationWrapper> orders = orderResults.getOrderListInHierarchicalOrder();
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
   			} else {
   				List<ConfirmationWrapper> orders = orderResults.getOrderListInHierarchicalOrder();
   				if ( orders != null && !orders.isEmpty() ) {	
   					//setSelectedOrder( orders.get(0) );
   					ConfirmationWrapper nextBundleOrder = orders.get(0);
   					if (getSelectedOrder()!= null)
   						getSelectedOrder().getMyBundles().addAll(nextBundleOrder.getMyBundles());
   				}   				
   			}
   			pagingIdx++;
   		}
  		
  		setHaveDetailsStoredInOMS(haveDetailsStoredInOMSCnt>0);
  		setHaveAdjustedDetails(haveAdjustedDetailsCnt>0);
  		setHaveDetailsStoredInTF(haveDetailsStoredInTFCnt>0);
  		setHaveNonInvoicedDetails(haveNonInvoicedDetailsCnt>0);
  		setHaveOpenDetails(haveOpenDetailsCnt>0);
		
		removeProcessEditsListFromSession();
		removeProcessMessagesListFromSession();
		removeConfirmEditsListFromSession();
		
		setStartRenderTime(MiscUtils.getNow());
		storeOrderSearchResultsWrapperArrayInSession(getWrappedResultsArray());
		storeOrderSearchCriteriaArrayInSession(getSearchCriteria());
		storeCustomPageControlArrayInSession(getPageControl());
		
		for ( boolean reps : getRePagingOrSizing() ) {
			if ( reps ) { reps = false; }
		}
		return SUCCESS;
	}

	public String resortSearchResults() {
		int pgIdx = 0;
		if ( StringUtils.isNotEmpty(getRequestedPagingIndex())) {
			pgIdx = Integer.valueOf(getRequestedPagingIndex()).intValue();
		}
		return resortSearchResults(pgIdx);
	}
	
	public String resortSearchResults(int pagingIndex) {
		// Get sort column to sort by and order 
		String sortBy = getNewSortBy()[pagingIndex];
		String sortOrder = getNewSortOrder()[pagingIndex];
		// get saved criteria
		setPageControl( getCustomPageControlArrayInSession() );
		setSearchCriteria( getOrderSearchCriteriaArrayInSession() );
		if ( getSearchCriteria() != null ) {
			getSearchCriteria()[pagingIndex] = getPagingOrderSearchCriteriaInSession(pagingIndex);
			getSearchCriteria()[pagingIndex].setSortCriteriaBy(sortBy);
			getSearchCriteria()[pagingIndex].setSortOrder(sortOrder);
			getSearchCriteria()[pagingIndex].getPageControl().setPage(1);
			getRePagingOrSizing()[pagingIndex] = true;
		} else {
			setRequestedPagingIndex("-1");
		}			
		
		String executeResult = execute();
		
		if (executeResult == NOT_AUTHORIZED)
			return NOT_AUTHORIZED;
		
		return SUCCESS;
		
	}

	public String reSizePageResults() {
		int pgIdx = 0;
		if ( StringUtils.isNotEmpty(getRequestedPagingIndex())) {
			pgIdx = Integer.valueOf(getRequestedPagingIndex()).intValue();
		}
		return reSizePageResults(pgIdx);
	}
	
	public String reSizePageResults(int pagingIndex) {
		
		int nPageSize = -1;
		if ( StringUtils.isNotEmpty(getNewPageSize()[pagingIndex]) ) {
			if ( StringUtils.isNumeric(getNewPageSize()[pagingIndex].trim()) ) {
				nPageSize = Integer.valueOf(getNewPageSize()[pagingIndex].trim()).intValue();
			}
		}
		setPageControl( getCustomPageControlArrayInSession() );
		if ( nPageSize > 0 ) {
			getPageControl()[pagingIndex].setPageSize(nPageSize);
			getPageControl()[pagingIndex].setPage(getPageControl()[pagingIndex].getPageAfterHandlingPageSizeChange());
			getPageControl()[pagingIndex].setLastPageSize(nPageSize);
		}
		setSearchCriteria( getOrderSearchCriteriaArrayInSession() );
		if ( getSearchCriteria() != null ) {
			getSearchCriteria()[pagingIndex] = getPagingOrderSearchCriteriaInSession(pagingIndex);
			getSearchCriteria()[pagingIndex].setPageControl(CustomPageControl.copy(getPageControl()[pagingIndex]));
			setSearching(true);
			setResorting(false);
			getRePagingOrSizing()[pagingIndex] = true;
			execute(pagingIndex);
		} else {
			setRequestedPagingIndex("-1");
		}			
		
		String executeResult = execute();
		
		if (executeResult == NOT_AUTHORIZED)
			return NOT_AUTHORIZED;
		
		return SUCCESS;
	
	}	
	public String rePageResults() {
		int pgIdx = 0;
		if ( StringUtils.isNotEmpty(getRequestedPagingIndex())) {
			pgIdx = Integer.valueOf(getRequestedPagingIndex()).intValue();
		}
		return rePageResults(pgIdx);
	}
	public String rePageResults(int pagingIndex) {
		int nPage = -1;
		if ( StringUtils.isNotEmpty(getNewPage()[pagingIndex]) ) {
			if ( StringUtils.isNumeric(getNewPage()[pagingIndex].trim()) ) {
				nPage = Integer.valueOf(getNewPage()[pagingIndex].trim()).intValue();
			}
		}
		setPageControl( getCustomPageControlArrayInSession() );
		if ( nPage > 0 ) {
			getPageControl()[pagingIndex].setPage(nPage);
		}
		setSearchCriteria( getOrderSearchCriteriaArrayInSession() );
		if ( getSearchCriteria() != null ) {
			getSearchCriteria()[pagingIndex] = getPagingOrderSearchCriteriaInSession(pagingIndex);
			getSearchCriteria()[pagingIndex].setPageControl(CustomPageControl.copy(getPageControl()[pagingIndex]));
			setStartEndDisplayRows(getSearchCriteria()[pagingIndex], pagingIndex);
			setSearching(true);
			setResorting(false);
			getRePagingOrSizing()[pagingIndex] = true;
			execute(pagingIndex);
		} else {
			setRequestedPagingIndex("-1");
		}			
		
		String executeResult = execute();
		
		if (executeResult == NOT_AUTHORIZED)
			return NOT_AUTHORIZED;	
		
		return SUCCESS;
	
	}
	
/*	public String saveDetails() {
		
		super.handleQuickTabSelected();
		
		setSelectedConfirmBundleInvoiceDetailFields();

		List<ProcessMessage> processMessages = new ArrayList<ProcessMessage>();
		List<EditItemWrapper> processEdits = new ArrayList<EditItemWrapper>();
		removeProcessMessagesListFromSession();
		
		EditItemWrapper jumpToFirstDetail = null;
		EditItemWrapper jumpToFirstErrorDetail = null;
				
		for ( EditItemWrapper eiw : getEditDetails() ) {
			if ( eiw.isToBeSaved() ) {
				if ( jumpToFirstDetail == null ) { jumpToFirstDetail = eiw; }
				List<ProcessMessage> detailMessages = OrderServicesHelper.saveOrderDetailOnConfirmationPage( eiw, false );
				
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
	
	public String saveBundleHeader(int pagingIndex) {
		
		super.handleQuickTabSelected();
		
		setSelectedConfirmBundleInvoiceDetailFields();
		
		List<ProcessMessage> processMessages = new ArrayList<ProcessMessage>();
		removeProcessMessagesListFromSession();
		
		processMessages.add(new ProcessMessage(getSelectedConfirmNumber(), "ERROR", "Project (bundle) header save service is not yet available"));
		
		getSavingBundle()[pagingIndex] = getSelectedBundleNumber();
		setSelectedItem(OrderManagementConstants.BUNDLE_HEADER);
		
		storeProcessMessagesListInSession(processMessages);

		return SAVE_BUNDLE_HEADER_COMPLETE;
	}
*/	
	public String unCancelOrderDetail() {
		
		super.handleQuickTabSelected();
		
		setSelectedConfirmBundleInvoiceDetailFields();

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
	public int getIndividualDetailsIndex() {
		int pagingIndex = 0;
		for ( OrderSearchCriteriaWrapper srw : getSearchCriteria() ) {
			if ( srw.getPagingGroupKey().equals(INDIVIDUAL_GROUP_ID) ) {
				return pagingIndex;
			}
			pagingIndex++;
		}
		return -1;
	}
	
	public int getBundleDetailsIndexCount() {
		int pagingIndexCount = 0;
		for ( OrderSearchCriteriaWrapper srw : getSearchCriteria() ) {
			if ( !srw.getPagingGroupKey().equals(INDIVIDUAL_GROUP_ID) ) {
				pagingIndexCount++;
			}
		}
		return pagingIndexCount;
	}
	public int getSelectedBundleIndex() {
		int pagingIndex = 0;
		for ( OrderSearchCriteriaWrapper srw : getSearchCriteria() ) {
			if ( srw.getPagingGroupKey().equals(getSearchBundleNumber().toString()) 
				 && !srw.getPagingGroupKey().equals(INDIVIDUAL_GROUP_ID)) {
				return pagingIndex;
			}
			pagingIndex++;
		}
		return -1;
	}	
	public boolean getSelectedBundlePaging() {
		int pagingIndex = 0;
		for ( OrderSearchCriteriaWrapper srw : getSearchCriteria() ) {
			if ( srw.getPagingGroupKey().equals(getSearchBundleNumber().toString()) ) {
				return getPagingDetails()[pagingIndex];
			}
			pagingIndex++;
		}
		return false;
	}
	public boolean getExpandAnyBundleDetail() {
		for ( boolean isToBeExpanded : getExpandFirstBundleDetail() ) {
			if ( isToBeExpanded ) {
				return isToBeExpanded;
			}
		}
		return false;
	}	
	
	public boolean getIndividualDetailsPaging() {
		int pagingIndex = 0;
		for ( OrderSearchCriteriaWrapper srw : getSearchCriteria() ) {
			if ( srw.getPagingGroupKey().equals(INDIVIDUAL_GROUP_ID) ) {
				return getPagingDetails()[pagingIndex];
			}
			pagingIndex++;
		}
		return false;
	}	

}
