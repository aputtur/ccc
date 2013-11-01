package com.copyright.ccc.web.actions.ordermgmt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.copyright.ccc.business.data.OrderBundle;
import com.copyright.ccc.business.data.OrderPurchase;
import com.copyright.ccc.business.security.CCPrivilegeCode;
import com.copyright.ccc.business.services.OrderDataSourceEnum;
import com.copyright.ccc.business.services.order.OrderPurchaseServices;
import com.copyright.ccc.business.services.order.OrderPurchasesException;
import com.copyright.ccc.web.actions.ordermgmt.data.BundleWrapper;
import com.copyright.ccc.web.actions.ordermgmt.data.ConfirmationWrapper;
import com.copyright.ccc.web.actions.ordermgmt.data.CustomPageControl;
import com.copyright.ccc.web.actions.ordermgmt.data.EditBundle;
import com.copyright.ccc.web.actions.ordermgmt.data.EditConfirmation;
import com.copyright.ccc.web.actions.ordermgmt.data.EditItemWrapper;
import com.copyright.ccc.web.actions.ordermgmt.data.ItemWrapper;
import com.copyright.ccc.web.actions.ordermgmt.data.OrderSearchCriteriaWrapper;
import com.copyright.ccc.web.actions.ordermgmt.data.OrderSearchResultWrapper;
import com.copyright.ccc.web.actions.ordermgmt.data.OrderServicesHelper;
import com.copyright.ccc.web.actions.ordermgmt.data.ProcessMessage;
import com.copyright.ccc.web.actions.ordermgmt.util.MiscUtils;

public class ViewOrderHistoryAction extends ViewOrderHistoryMultiPagingBaseAction implements CustomPageControlArrayAware, ServletRequestAware, ServletResponseAware{

	private static final long serialVersionUID = 1L;
    protected static final Logger _logger = Logger.getLogger( ViewOrderHistoryAction.class );

    private HttpServletResponse response;    
    private HttpServletRequest request;    
  
	public void setServletResponse(HttpServletResponse response){     this.response = response;   }
    public HttpServletResponse getServletResponse(){     return response;   } 
    public void setServletRequest(HttpServletRequest request){     this.request = request;   }
    public HttpServletRequest getServletRequest(){     return request;   } 

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
								
		List<ProcessMessage> allPrMessages = getAllProcessMessagesListInSession();
		List<ProcessMessage> prMessages = getProcessMessagesListInSession();
		List<EditItemWrapper> prEdits = getProcessEditsListInSession();
		EditConfirmation cfEdits = getConfirmEditsListInSession();
		
		if (allPrMessages != null && !allPrMessages.isEmpty()) { 
			setAllProcessMessages(allPrMessages);
		}
		
		boolean postProcessMessages = (prMessages != null && !prMessages.isEmpty()) || (prEdits != null && !prEdits.isEmpty());
//		if ( postProcessMessages ) {
//		   	if ( isShowAllProcessMesssages() ) {
//		   		setAllProcessMessages(prMessages);
//		   	} 
//		}
   		boolean processedConfirmation = false;
   		int pagingIdx = 0;
   		int haveNonInvoicedDetailsCnt = 0;
   		int haveNonCanceledDetailsCnt = 0;
   		int haveNonRLDetailsCnt = 0;
   		int haveDetailsStoredInTFCnt = 0;
   		int haveDetailsStoredInOMSCnt = 0;
   		int haveOpenDetailsCnt = 0;
   		int haveAdjustedDetailsCnt = 0;
  		for ( OrderSearchResultWrapper orderResults : getWrappedResultsArray() ) {
  			if ( orderResults.isHaveNonInvoicedDetails() ) { haveNonInvoicedDetailsCnt++; }
  			if ( orderResults.isHaveNonCanceledDetails() ) { haveNonCanceledDetailsCnt++; }
  			if ( orderResults.isHaveNonRLDetails() ) { haveNonRLDetailsCnt++; }
  			if ( orderResults.isHaveDetailsStoredInTF() )  { haveDetailsStoredInTFCnt++; }
  			if ( orderResults.isHaveDetailsStoredInOMS() ) { haveDetailsStoredInOMSCnt++; }
  			if ( orderResults.isHaveOpenDetails() )        { haveOpenDetailsCnt++; }
  			if ( orderResults.isHaveAdjustedDetails() )        { haveAdjustedDetailsCnt++; }
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
   					if (getSavingBundleInSession() != null && !getSavingBundleInSession().isEmpty()) {
   	   					setEditBundles(getEditBundlesInSession());
   	   					for (BundleWrapper bundleWrapper : getSelectedOrder().getMyBundles()) {
   	   						for (EditBundle editBundle : getEditBundlesInSession()) {
   	   							if (bundleWrapper.getBundle().getBundleId() == editBundle.getBundleId()) {
   	   	    						postProcessMessagesToBundle(getSelectedOrder(), prMessages, bundleWrapper, editBundle);   							   								
   	   							}
   	   						}
   	   					}
   						setSavingBundle(getSavingBundleInSession());
   					}
   				}

//  				if ( ebEdits != null) {
//   					setSavingBundle(new Long(ebEdits.getBundleId()).toString());
//   					postProcessMessagesToBundle(getSelectedOrder(), prMessages, ebEdits);
//   				}

   			} else {
//   				List<ConfirmationWrapper> orders = orderResults.getOrderListInHierarchicalOrder();
//   				if ( orders != null && !orders.isEmpty() ) {	
//   					//setSelectedOrder( orders.get(0) );
//   					ConfirmationWrapper nextBundleOrder = orders.get(0);
//   					if (getSelectedOrder()!= null)
//   						getSelectedOrder().getMyBundles().addAll(nextBundleOrder.getMyBundles());
//   				}   				
   			}  			
   			
   			pagingIdx++;
   		}
  		
  		setHaveDetailsStoredInOMS(haveDetailsStoredInOMSCnt>0);
  		setHaveDetailsStoredInTF(haveDetailsStoredInTFCnt>0);
  		setHaveNonInvoicedDetails(haveNonInvoicedDetailsCnt>0);
  		setHaveNonCanceledDetails(haveNonCanceledDetailsCnt>0);
  		setHaveNonRLDetails(haveNonRLDetailsCnt>0);
  		setHaveOpenDetails(haveOpenDetailsCnt>0);
  		setHaveAdjustedDetails(haveAdjustedDetailsCnt>0);
		
		removeProcessEditsListFromSession();
		removeProcessMessagesListFromSession();
		removeAllProcessMessagesListFromSession();
		removeConfirmEditsListFromSession();
		removeEditBundlesFromSession();
		removeSavingBundleFromSession();

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
			setResizing(true);
			execute(pagingIndex);
		} else {
			setRequestedPagingIndex("-1");
		}			
		
		String executeResult = execute();
		
		if (executeResult == NOT_AUTHORIZED)
			return NOT_AUTHORIZED;
		
/*
 * Deb Fahey - added this method to re-index all details within 
 * the wrapped results to address JIRA
 * The paging/indexing logic was not working as desired, It was determined that 
 * it was too risky to try and change the existing logic therefore we introduced 
 * a method to do cleanup right before we send data back to the form
 */
		reIndexDetailsWithinWrappedResults();
		
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
				
		/*
		 * Deb Fahey - added this method to re-index all details within 
		 * the wrapped results to address JIRA
		 * The paging/indexing logic was not working as desired, It was determined that 
		 * it was too risky to try and change the existing logic therefore we introduced 
		 * a method to do cleanup right before we send data back to the form
		 */
				reIndexDetailsWithinWrappedResults();
				
		return SUCCESS;
	
	}
	
	public String saveDetails() {
		
		super.handleQuickTabSelected();
		
		setSelectedConfirmBundleInvoiceDetailFields();

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
						if (itemWrapper.getItem().getID() == Long.valueOf(eiw.getItem().getID()).longValue()) {
							if (itemWrapper.getItem().getOrderDataSource() == OrderDataSourceEnum.OMS.getOrderDataSourceId().intValue()) {
								savingCoiDetails = true;
							}
							detailMessages = OrderServicesHelper.saveOrderDetailOnConfirmationPage( eiw, itemWrapper, false, savingCoiDetails, savingMultipleDetails );								
							if (eiw.getItem().isUpdatePricingOnly()) {
								this.storeUseWrappedResultsFromSession(Boolean.TRUE);
								eiw.setToBeReturned(true);
//								itemWrapper.setUpdatePricingOnly(true);
//								itemWrapper.setToBeReturned(true);
								itemWrapper.getLastEdit().setToBeReturned(true);
								jumpToFirstDetail = null;
								setSelectedFieldsFor(eiw);
							}
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
//							processEdits.add(eiw);
							saveProcessHasErrors = true;
							break;
						}
					}
					processEdits.add(eiw);
					// save messages
					processMessages.addAll(detailMessages);
				} else {
					processEdits.add(eiw);
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
//		storeUseWrappedResultsFromSession(true);
		storeProcessMessagesListInSession(processMessages);
		storeProcessEditsListInSession(processEdits);
		storeOrderSearchResultsWrapperArrayInSession(getWrappedResultsArray());
		
		if (isInvoiceView()) {
			return SAVE_INVOICE_COMPLETE;
		} else {
			return SAVE_COMPLETE;
		}
		
	}
	

	
	public String saveConfirmationHeader() {
		super.handleQuickTabSelected();
		
		setSelectedConfirmBundleInvoiceDetailFields();
		
		List<ProcessMessage> processMessages = new ArrayList<ProcessMessage>();
		removeProcessMessagesListFromSession();
		
		OrderPurchase orderPurchase = OrderServicesHelper.getConfirmationOrderPurchaseFor(getSelectedConfirmNumber());
				
		if ( orderPurchase == null ) {
			processMessages.add(new ProcessMessage(getSelectedConfirmNumber(), "ERROR", "Unable to locate Order record."));			
		} else {
			try {
				orderPurchase.setPoNumber(getEditConfirm().getPoNumber());
				orderPurchase.setBillingReference(getEditConfirm().getBillingReference());
				OrderPurchaseServices.updateOrderPurchase(orderPurchase);
				processMessages.add(new ProcessMessage(getSelectedConfirmNumber(),"SAVE","Save confirmation header completed."));
			} catch (OrderPurchasesException e) {
				processMessages.add(new ProcessMessage(getSelectedConfirmNumber(), "ERROR", "Unable to update order header: " + e.getMessage()));
			}
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
	
/*	public String getPricingForDetail(Long itemId) {
		_logger.info("Got to get pricing for detail " + itemId);
		return SAVE_CONFIRM_HEADER_COMPLETE;
		
	}

	public String getPricingForDetail() {
	   	_logger.info("Got to get pricing for detail with no item id " );

		String xmlResponse = "This is the xmlResponse";

        response.setContentType("text/xml");

        PrintWriter out;
		try {
			out = response.getWriter();
			out.write(xmlResponse);
	        out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "success";
	}
*/
	
	
	public String saveBundleHeader() {
		
		super.handleQuickTabSelected();
		
		setSelectedConfirmBundleInvoiceDetailFields();
		setWrappedResultsArray(getOrderSearchResultsWrapperArrayInSession());
		
		List<ProcessMessage> processMessages = new ArrayList<ProcessMessage>();
		List<ProcessMessage> saveProcessMessages = new ArrayList<ProcessMessage>();
		
		removeProcessMessagesListFromSession();
		
		OrderBundle orderBundle = null;
		OrderPurchase orderPurchase = null;
		
		for (OrderSearchResultWrapper orderSearchResultWrapper : getWrappedResultsArray()) {
			if (orderSearchResultWrapper.getSvcSearchResults() != null && this.getSelectedBundleNumber() != null) {
				orderBundle = orderSearchResultWrapper.getSvcSearchResults().getOrderBundleByBundleId((Long.valueOf(this.getSelectedBundleNumber()) == -1) ? Long.valueOf(this.getSelectedBundleId()) : Long.valueOf(this.getSelectedBundleNumber()));
				orderPurchase = orderSearchResultWrapper.getSvcSearchResults().getOrderPurchaseByConfirmId(this.getSelectedConfirmNumber());
				if (orderBundle != null && orderPurchase != null) {
					for (EditBundle editBundle : this.getEditBundles()) {
						if (editBundle.getBundleId() == orderBundle.getBundleId()) {
							saveProcessMessages = OrderServicesHelper.saveBundleOnConfirmationPage(editBundle, orderBundle, orderPurchase);
							if (saveProcessMessages != null && !saveProcessMessages.isEmpty()) {
								storeEditBundlesInSession(this.getEditBundles());
								storeSavingBundleInSession(new Long(editBundle.getBundleId()).toString());
							} else {
								processMessages.add(new ProcessMessage(new Long(orderBundle.getBundleId()).toString(),"SAVE","completed - " + new Date().toString() ));
							}
							processMessages.addAll(saveProcessMessages);
						}
					}
				}
			}
		}
		

			
//		OrderServicesHelper.saveBundleOnConfirmationPage(this.getEditBundle(), orderBundle, isInvoiceView)
		
//		processMessages.add(new ProcessMessage(getSelectedConfirmNumber(), "ERROR", "Project (bundle) header save service is not yet available"));

//		String[] savingBundle = new String[1];
//		savingBundle[0] = getSelectedBundleNumber();
		
//		setSavingBundle(getSelectedBundleNumber().toString());
		
//		getSavingBundle()[0] = getSelectedBundleNumber();
		setSelectedItem(OrderManagementConstants.BUNDLE_HEADER);
		
		storeProcessMessagesListInSession(processMessages);

		
		
//		if (saveProcessMessages != null && !saveProcessMessages.isEmpty()) {
//			storeUseWrappedResultsFromSession(true);
//			storeOrderSearchResultsWrapperArrayInSession(getWrappedResultsArray());
//		}
		

		
		return SAVE_COMPLETE;
	}
		
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
		
		List<ProcessMessage> allProcessMessages = new ArrayList<ProcessMessage>();
		
		if ( getSearchOrderId() != null && getSearchOrderId() > 0 ) {
			
			removeProcessMessagesListFromSession();
			
			allProcessMessages = OrderServicesHelper.cancelOpenDetailsFor( getSearchOrderId().toString() ) ;
		    
		} else {
			addActionError("Must supply a confirmation number");
		}
				
		setSelectedItem(OrderManagementConstants.ORDER_HEADER);

		storeAllProcessMessagesListInSession(allProcessMessages);
		
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
	
	@Override
	public QuickTabSelect getDefaultQuickTabSelect() {
		return QuickTabSelect.ordermgmt_menu_order;
	}	
	
	private void reIndexDetailsWithinWrappedResults()
	{
		int detailIndex = 0;
		for (OrderSearchResultWrapper orderSearchResultWrapper : getWrappedResultsArray())
		{		
			for (ItemWrapper itemWrapper : orderSearchResultWrapper.getDetailsList())
			{
				itemWrapper.setDetailIndex(detailIndex);
				detailIndex++;
			}	
			
		}		
	}
}
