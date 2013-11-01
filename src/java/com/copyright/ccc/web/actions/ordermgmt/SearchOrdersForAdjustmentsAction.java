package com.copyright.ccc.web.actions.ordermgmt;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.struts.util.LabelValueBean;

import com.copyright.ccc.business.data.OrderSearchCriteria;
import com.copyright.ccc.business.data.OrderSearchCriteriaSortColumnEnum;
import com.copyright.ccc.business.data.OrderSearchCriteriaSortDirectionEnum;
import com.copyright.ccc.business.data.OrderSearchResult;
import com.copyright.ccc.business.security.CCPrivilegeCode;
import com.copyright.ccc.business.services.order.OrderLicenseServices;
import com.copyright.ccc.business.services.order.OrderLicensesException;
import com.copyright.ccc.web.actions.ordermgmt.data.OrderSearchCriteriaWrapper;
import com.copyright.ccc.web.actions.ordermgmt.data.OrderSearchResultWrapper;
import com.copyright.ccc.web.actions.ordermgmt.util.MiscUtils;
import com.copyright.workbench.security.UserContextHelperBase;

public class SearchOrdersForAdjustmentsAction extends SearchOrdersAction {

	private static final long serialVersionUID = 1L;
	
	// If we just completed an adjustment, we need to show a message
	private boolean completedAdjustment = false;
    private List<LabelValueBean> searchAdjList = OrderSearchCriteriaSortColumnEnum.getAdjustmentSortFieldSelects();
    
	
	public boolean getCompletedAdjustment(){
		return completedAdjustment;
	}
	public void setCompletedAdjustment(boolean completedAdjustment){
		this.completedAdjustment = completedAdjustment;
	}

	/**
	 * Default action - executed when page is first reached
	 * and whenever new search or sort order is requested
	 */
	@Override
	public String execute()
	{	
		if (!super.verifyPrivilege(CCPrivilegeCode.SEARCH_ORDERS))
			return NOT_AUTHORIZED;
		
		super.handleQuickTabSelected();
		
		setSearchResults( new OrderSearchResultWrapper());
		setShowCriteria( true );
		setShowResults( false );
		
		if ( getQuickTabSelect() != null ) {			
			if ( getQuickTabSelect().isAdjustmentSearch() ) {
				
				if ( getQuickTabSelect().isSearchByAllCriteria() ) {
				    // check if user has requested search yet
					if ( !isSearching() ) {
						//go to order search (display criteria)
						return SUCCESS;
					}
					
				} else if ( getQuickTabSelect().isSearchByUser() ) {
					if ( !isResorting() ) {
						// do search by user
						setSearchCriteria( new OrderSearchCriteriaWrapper(true /* use default sort */) );
						getSearchCriteria().setInternetLogin_adv(UserContextHelperBase.getCurrentAsInterface().getAuthenticatedUser().getUsername());
						getSearchCriteria().setOrderStatus_adv("Not Completely Entered");
						getSearchCriteria().setSortCriteriaBy(OrderSearchCriteriaSortColumnEnum.SORT_BY_DEFAULT.name());
						getSearchCriteria().setSortOrder(OrderSearchCriteriaSortDirectionEnum.DESC.getUserText());
					} 
				} else {
					// Not a search request?
				}
		
				resetSortCriteria();
				
				setStartEndDisplayRows();
				
				getSearchCriteria().setIncludeOnlyAdjustableItems(true); // Invoiced only for adjustments
				
				setSearchResults( performSearch(getSearchCriteria()) );
				setIncludeCancelledOrders(getSearchCriteria().getShowCancelled());
				
				if (getSearchResults().isHasResults()) {
					setShowResults( true );
					if ( isReturningToSearch() ) {
						setShowCriteria( true );
					} else {
						setShowCriteria( false );
					}
				} else {
					getSearchCriteria().getPageControl().setListSize(0);
					getSearchCriteria().getPageControl().setTotalRecords(0);
					getSearchCriteria().getPageControl().setPage(1);
				}
			}
		} 
		
		setResorting(false);
		storeOrderSearchCriteriaInSession(getSearchCriteria(),OMBaseAction.ADJUSTMENT_ORDER_SEARCH_CRITERIA_KEY);
		
		setStartRenderTime(MiscUtils.getNow());
		
		return SUCCESS;
		
	}

	/**
	 * Does actual search process
	 * @param osc
	 * @return
	 */
	private OrderSearchResultWrapper performSearch(OrderSearchCriteriaWrapper osc) {
		OrderSearchResult svcSearchResult = null;
		setSearchResults( new OrderSearchResultWrapper( svcSearchResult ) );
		try {
			setStartSearchTime(MiscUtils.getNow());
			double beginTime =(double)getStartSearchTime().getTime(); //start time
			OrderSearchCriteria searchCriteria = osc.getServiceSearchCriteriaForSearch();
			if (searchCriteria.isSufficientCriteria()) {
				//searchCriteria.setIncludeOnlyAdjustableItems(true);
				svcSearchResult = OrderLicenseServices.searchOrderLicenses(searchCriteria);
				setEndSearchTime(MiscUtils.getNow());
				double endTime = (double)getEndSearchTime().getTime(); //end time
				setElapsedSeconds((endTime-beginTime)/1000);
				if ( svcSearchResult != null ) {
					if ( svcSearchResult.getOrderLicenses() != null && !svcSearchResult.getOrderLicenses().isEmpty() ) {
						setSearchResults( new OrderSearchResultWrapper( svcSearchResult  ) );
						int totalRecords = Long.valueOf(getSearchResults().getSvcSearchResults().getTotalRows()).intValue();
						getCustomPageControl().setTotalRecords(totalRecords);
						if (totalRecords > 1000) {
							addActionMessage("The first 1000 results can be viewed.  Please refine your search to narrow your results.");
							getCustomPageControl().setListSize(1000);
						}
						else {					
							getCustomPageControl().setListSize(totalRecords);
						}
						getSearchCriteria().setLastServiceSearchCritera(svcSearchResult.getOrderSearchCriteria());
					}
				} 
				setSearchExecuted( true );
			}
			else {
				addActionError("There is not enough criteria to perform your search.  Please enter additional criteria and try your search again. ");
				//showResults = false;
				setSearchExecuted( false );
			}
				
			
		} catch (OrderLicensesException e) {
			addActionError("A system error has prevented retrieving the requested confirmation");
		} catch (Exception e) {
			addActionError("Search failed: " + ExceptionUtils.getRootCauseMessage(e));
		}
		
		return getSearchResults();
	
	}
	/**
	 * When a view page returns to search results - will used persisted
	 * search criteria.
	 */
	@Override
	public String returnToSearchResults() {
		super.handleQuickTabSelected();
		
		setSearchCriteria( getOrderSearchCriteriaInSession(OMBaseAction.ADJUSTMENT_ORDER_SEARCH_CRITERIA_KEY) );
		setSearching(true);
		setResorting(true);
		setReturningToSearch(true);
		String quickTabSelect = getQuickTabSelectedInSessionDotFormattedSelectedValue();
		execute();
		if ( StringUtils.isNotEmpty(quickTabSelect) ) {
			if ( !quickTabSelect.equalsIgnoreCase(getQuickTabSelect().getDotFormattedSelectedValue())) {
				String tabText = getText(quickTabSelect);
				if ( StringUtils.isNotEmpty(tabText) ) {
					addActionMessage("Unable to return to "	+ tabText);
				}
			}
		}
		
		return SUCCESS;
	}

	@Override
	public String rePageResults() {
		int nPage = -1;
		if ( StringUtils.isNotEmpty(getNewPage()) ) {
			if ( StringUtils.isNumeric(getNewPage().trim()) ) {
				nPage = Integer.valueOf(getNewPage().trim()).intValue();
			}
		}
		setSearchCriteria( getOrderSearchCriteriaInSession(OMBaseAction.ADJUSTMENT_ORDER_SEARCH_CRITERIA_KEY) );
		if ( nPage > 0 ) {
			getSearchCriteria().getPageControl().setPage(nPage);
		}
		setSearching(true);
		setResorting(false);
		
		execute();
		
		return SUCCESS;
	
	}

	private void resetSortCriteria() {
		resetSortCriteria(isResorting());
	}

	private void resetSortCriteria(boolean includeNoCriteriaMessage) {
		String sortBy = getSearchCriteria().getSortCriteriaBy();
		String sortOrder = getSearchCriteria().getSortOrder();
		// clear sort criteria
		getSearchCriteria().setSortCriteriaDefault();
		// add sort criteria
		if ( StringUtils.isNotEmpty(sortBy) && StringUtils.isNotEmpty(sortOrder) ) {
			getSearchCriteria().setSortCriteriaBy( sortBy );
			getSearchCriteria().setSortOrder(sortOrder);
		} else {
			getSearchCriteria().setSortCriteriaDefault();
			if ( includeNoCriteriaMessage ) {
				addActionError("No sort criteria specified, using default");
			}
		}	
	}

	@Override
	public String reSizePageResults() {
		int nPageSize = -1;
		if ( StringUtils.isNotEmpty(getNewPageSize()) ) {
			if ( StringUtils.isNumeric(getNewPageSize().trim()) ) {
				nPageSize = Integer.valueOf(getNewPageSize().trim()).intValue();
			}
		}
		setSearchCriteria( getOrderSearchCriteriaInSession(OMBaseAction.ADJUSTMENT_ORDER_SEARCH_CRITERIA_KEY) );
		if ( nPageSize > 0 ) {
			getSearchCriteria().getPageControl().setPageSize(nPageSize);
			getSearchCriteria().getPageControl().setPage(getSearchCriteria().getPageControl().getPageAfterHandlingPageSizeChange());
			getSearchCriteria().getPageControl().setLastPageSize(nPageSize);
		}
		setSearching(true);
		setResorting(false);
		
		execute();
		
		return SUCCESS;
	
	}

	@Override
	public String resortSearchResults() {
		
		// Get sort column to sort by and order 
		String sortBy = getSearchCriteria().getSortCriteriaBy();
		String sortOrder = getSearchCriteria().getSortOrder();
		// get saved criteria
		setSearchCriteria( getOrderSearchCriteriaInSession(OMBaseAction.ADJUSTMENT_ORDER_SEARCH_CRITERIA_KEY) );
		getSearchCriteria().setSortCriteriaBy(sortBy);
		getSearchCriteria().setSortOrder(sortOrder);
		setSearching(true);
		setResorting(true);
		
		execute();
		
		return SUCCESS;
	
	}

	private void setStartEndDisplayRows() {
		if ( !getCustomPageControl().isPageSizeChanged() ) {
			getSearchCriteria().setDisplayFromRow(getCustomPageControl().getPageSize() * ( getCustomPageControl().getPage() - 1 ) + 1 );
			getSearchCriteria().setDisplayToRow(getCustomPageControl().getPageSize() * getCustomPageControl().getPage());
		} 
	}
	@Override
	public QuickTabSelect getDefaultQuickTabSelect() {
		return QuickTabSelect.ordermgmt_menu_adjustment;
	}	
	public List<LabelValueBean> getSearchAdjList() {
		return searchAdjList;
	}

	public void setSearchAdjList(List<LabelValueBean> searchAdjList) {
		this.searchAdjList = searchAdjList;
	}

}
