package com.copyright.ccc.web.actions.ordermgmt;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.util.LabelValueBean;

import com.copyright.ccc.business.data.OrderPurchase;
import com.copyright.ccc.business.services.ProductEnum;
import com.copyright.ccc.web.actions.ordermgmt.data.ConfirmationWrapper;
import com.copyright.ccc.web.actions.ordermgmt.data.CustomPageControl;
import com.copyright.ccc.web.actions.ordermgmt.data.ItemWrapper;
import com.copyright.ccc.web.actions.ordermgmt.data.OrderSearchCriteriaWrapper;

/*
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.util.LabelValueBean;

import com.copyright.ccc.business.data.OrderLicense;
import com.copyright.ccc.business.data.OrderPurchase;
import com.copyright.ccc.business.data.OrderSearchCriteriaSortColumnEnum;
import com.copyright.ccc.business.data.OrderSearchCriteriaSortDirectionEnum;
import com.copyright.ccc.business.data.OrderSearchResult;
import com.copyright.ccc.business.services.ProductEnum;
import com.copyright.ccc.business.services.order.OrderLicenseServices;
import com.copyright.ccc.business.services.order.OrderLicensesException;
import com.copyright.ccc.web.actions.ordermgmt.data.ConfirmationWrapper;
import com.copyright.ccc.web.actions.ordermgmt.data.CustomPageControl;
import com.copyright.ccc.web.actions.ordermgmt.data.OrderSearchResultWrapper;
import com.copyright.ccc.web.actions.ordermgmt.data.OrderSearchCriteriaWrapper;
import com.copyright.ccc.web.actions.ordermgmt.util.MiscUtils;
*/

public class NewOrderAction extends OMBaseAction implements CustomPageControlAware {


	private static final long serialVersionUID = 1L;
	
	private boolean rePagingOrSizing = false;
	private boolean pagingDetails = false;
	private String newPage;
	private String newPageSize;
	
	private OrderSearchCriteriaWrapper searchCriteria = new OrderSearchCriteriaWrapper(true, true /* use default sort, for view */);
	private ConfirmationWrapper selectedOrder;
	private OrderPurchase editOrder;

	private List<LabelValueBean> productList = ProductEnum.getProductCodes();
	private List<LabelValueBean> touList = ProductEnum.getProductCodes(); //TODO: get right values!
	
	private List<ItemWrapper> addedDetails;
	
	//private Long selectedConfirmNumber;
	//private Long selectedBundleNumber;
	private boolean addingDetail;
	//private boolean addingProject;
	
	/*public Long getSelectedConfirmNumber() {
		return selectedConfirmNumber;
	}

	public void setSelectedConfirmNumber(Long selectedConfirmNumber) {
		this.selectedConfirmNumber = selectedConfirmNumber;
	}

	public Long getSelectedBundleNumber() {
		return selectedBundleNumber;
	}

	public void setSelectedBundleNumber(Long selectedBundleNumber) {
		this.selectedBundleNumber = selectedBundleNumber;
	} */

	public List<ItemWrapper> getAddedDetails() {
		return addedDetails;
	}

	public void setAddedDetails(List<ItemWrapper> addedDetails) {
		this.addedDetails = addedDetails;
	}

	public boolean isAddingDetail() {
		return addingDetail;
	}

	public void setAddingDetail(boolean addingDetail) {
		this.addingDetail = addingDetail;
	}

/*	public boolean isAddingProject() {
		return addingProject;
	}

	public void setAddingProject(boolean addingProject) {
		this.addingProject = addingProject;
	}
*/
	public List<LabelValueBean> getTouList() {
		return touList;
	}

	public void setTouList(List<LabelValueBean> list) {
		this.touList = list;
	}
	
	public List<LabelValueBean> getProductList() {
		return productList;
	}

	public void setProductList(List<LabelValueBean> list) {
		this.productList = list;
	}
	
	public OrderPurchase getEditOrder() {
		return editOrder;
	}

	public void setEditOrder(OrderPurchase editOrder) {
		this.editOrder = editOrder;
	}
	
	public ConfirmationWrapper getSelectedOrder() {
		return selectedOrder;
	}
	
	/*public ConfirmationWrapper getSelectedOrderTemp() {
		//ConfirmationWrapper order = new ConfirmationWrapper();
		OrderSearchResult searchResult = new OrderSearchResult();
		try {
			searchCriteria.setConfNumber("2319459");
			searchResult = OrderLicenseServices.searchOrderLicensesWithUser(searchCriteria.getServiceSearchCriteria());
			OrderSearchResultWrapper wrappedResults = new OrderSearchResultWrapper( searchResult , true /* Hierarchical order */ //);
		
		/*	selectedOrder = wrappedResults.getOrderListInHierarchicalOrder().get(0);
	    	editOrder = selectedOrder.getConfirmation();
			return order;
		}
		catch (Exception ex) {
			return null;
		}
	}*/
	
	public void setSelectedOrder(ConfirmationWrapper selectedOrder) {
		this.selectedOrder = selectedOrder;
	}
	
	public OrderSearchCriteriaWrapper getSearchCriteria() {
		return searchCriteria;
	}

	public void setSearchCriteria(OrderSearchCriteriaWrapper searchCriteria) {
		this.searchCriteria = searchCriteria;
	}
	
	public String getNewPage() {
		return newPage;
	}

	public void setNewPage(String newPage) {
		this.newPage = newPage;
	}

	public String getNewPageSize() {
		return newPageSize;
	}

	public void setNewPageSize(String newPageSize) {
		this.newPageSize = newPageSize;
	}

	private CustomPageControl pageControl = new CustomPageControl(5);
	
	public CustomPageControl getPageControl() {
		return pageControl;
	}

	public void setPageControl(CustomPageControl pageControl) {
		this.pageControl = pageControl;
	}

	public boolean isRePagingOrSizing() {
		return rePagingOrSizing;
	}
	public void setRePagingOrSizing(boolean rePagingOrSizing) {
		this.rePagingOrSizing = rePagingOrSizing;
	}

	public boolean isPagingDetails() {
		return pagingDetails;
	}

	public void setPagingDetails(boolean pagingDetails) {
		//this.includeBundles = false;
		//if ( pagingDetails ) {
		//	this.includeBundles = true;
		//} 
		this.pagingDetails = pagingDetails;
	}
	
	public String rePageResults() {
		int nPage = -1;
		if ( StringUtils.isNotEmpty(getNewPage()) ) {
			if ( StringUtils.isNumeric(getNewPage().trim()) ) {
				nPage = Integer.valueOf(getNewPage().trim()).intValue();
			}
		}
		pageControl = getCustomPageControlInSession();
		if ( nPage > 0 ) {
			pageControl.setPage(nPage);
		}
		searchCriteria = super.getOrderSearchCriteriaInSession(OMBaseAction.VIEW_ORDER_SEARCH_CRITERIA_KEY);
		setSearching(true);
		setResorting(false);
		setRePagingOrSizing(true);
		
		execute();
		
		return SUCCESS;
	
	}	
	
	public String reSizePageResults() {
		
		int nPageSize = -1;
		if ( StringUtils.isNotEmpty(getNewPageSize()) ) {
			if ( StringUtils.isNumeric(getNewPageSize().trim()) ) {
				nPageSize = Integer.valueOf(getNewPageSize().trim()).intValue();
			}
		}
		pageControl = getCustomPageControlInSession();
		if ( nPageSize > 0 ) {
			pageControl.setPageSize(nPageSize);
			pageControl.setPage(pageControl.getPageAfterHandlingPageSizeChange());
			pageControl.setLastPageSize(nPageSize);
		}
		searchCriteria = super.getOrderSearchCriteriaInSession(OMBaseAction.VIEW_ORDER_SEARCH_CRITERIA_KEY);
		setSearching(true);
		setResorting(false);
		setRePagingOrSizing(true);
		
		execute();
		
		return SUCCESS;
	
	}	
	

	
	/**
	 * Default action - executed when page is first reached
	 */
	@Override
	public String execute()
	{		
		super.handleQuickTabSelected();
		
		//setSelectedConfirmNumberAndExpandFlags();
		//setSearchBundleNumber(Long.valueOf(getSelectedBundleNumber()))
		
	/*	if ( !isRePagingOrSizing() ) {
			searchCriteria = super.getOrderSearchCriteriaInSession(OMBaseAction.VIEW_ORDER_SEARCH_CRITERIA_KEY);
			searchCriteria.clearFilterCriteria();
			searchCriteria.setConfNumber(Long.valueOf(getSelectedConfirmNumber()).toString());
			searchCriteria.setSortCriteriaBy(OrderSearchCriteriaSortColumnEnum.SORT_BY_VIEW_DEFAULT.name());
			searchCriteria.setSortOrder(OrderSearchCriteriaSortDirectionEnum.ASC.getUserText());
		
			setPageControl(new CustomPageControl(getDetailPagingThreshold()));
		}
						
		OrderSearchResult searchResult = null;
		try {
			//setStartSearchTime(MiscUtils.getNow());
			//long beginTime = getStartSearchTime().getTime(); //start time

			//setStartEndDisplayRows( searchCriteria );
			
			if ( isRePagingOrSizing() ) {
				searchResult = OrderLicenseServices.searchOrderLicensesWithUser(searchCriteria.getServiceSearchCriteria());				
				getPageControl().setListSize(Long.valueOf(searchResult.getTotalRows()).intValue());
				pagingDetails = searchResult.getTotalRows() > getDetailPagingThreshold();
			} else { 
				searchResult = makeSureDetailIsInCurrentResults(searchCriteria);
			//}
			
			//setEndSearchTime(MiscUtils.getNow());
			//long endTime = getEndSearchTime().getTime(); //end time
			
			//setElapsedSeconds((endTime-beginTime)/1000);
			OrderSearchResultWrapper wrappedResults = null;
			
			if ( searchResult != null ) {
				if ( searchResult.getOrderLicenses() != null && !searchResult.getOrderLicenses().isEmpty() ) {
					wrappedResults = new OrderSearchResultWrapper( searchResult , true /* Hierarchical order */  //);
		/*		}
			}
			if ( wrappedResults != null ) {
				List<ConfirmationWrapper> orders = wrappedResults.getOrderListInHierarchicalOrder();
			    if ( orders != null && !orders.isEmpty() ) {	
			    	selectedOrder = orders.get(0);
			    	editOrder = selectedOrder.getConfirmation();
			    	
			    }
			    //addActionMessage(wrappedResults.getSearchCounts());
			} else {
				addActionError("Unable to find confirmation");
			}
		} catch (OrderLicensesException e) {
			addActionError("A system error has prevented retrieving the requested confirmation");
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} */
		
		return SUCCESS;
	}
	
	@Override
	public QuickTabSelect getDefaultQuickTabSelect() {
		return QuickTabSelect.ordermgmt_menu_order_new;
	}
}
