package com.copyright.ccc.web.actions.ordermgmt;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.struts.util.LabelValueBean;

import com.copyright.ccc.business.data.OrderSearchCriteria;
import com.copyright.ccc.business.data.OrderSearchCriteriaSortColumnEnum;
import com.copyright.ccc.business.data.OrderSearchCriteriaSortDirectionEnum;
import com.copyright.ccc.business.data.OrderSearchResult;
import com.copyright.ccc.business.security.CCPrivilegeCode;
import com.copyright.ccc.business.services.EnumHelper;
import com.copyright.ccc.business.services.ItemAvailabilityEnum;
import com.copyright.ccc.business.services.ProductEnum;
import com.copyright.ccc.business.services.order.OrderLicenseServices;
import com.copyright.ccc.business.services.order.OrderLicensesException;
import com.copyright.ccc.web.actions.ordermgmt.data.CustomPageControl;
import com.copyright.ccc.web.actions.ordermgmt.data.OrderSearchCriteriaDateSelectEnum;
import com.copyright.ccc.web.actions.ordermgmt.data.OrderSearchCriteriaRefNumSelectEnum;
import com.copyright.ccc.web.actions.ordermgmt.data.OrderSearchCriteriaWrapper;
import com.copyright.ccc.web.actions.ordermgmt.data.OrderSearchResultWrapper;
import com.copyright.ccc.web.actions.ordermgmt.util.MiscUtils;
import com.copyright.workbench.security.UserContextHelperBase;

public class SearchOrdersAction extends OMBaseAction implements CustomPageControlAware {

	private static final long serialVersionUID = 1L;	

	private OrderSearchCriteriaWrapper searchCriteria = new OrderSearchCriteriaWrapper(true /* default sort */);
    private OrderSearchResultWrapper searchResults;

    private List<LabelValueBean> searchOrderList = OrderSearchCriteriaSortColumnEnum.getOrderSortFieldSelects();
    private List<LabelValueBean> sortDirectionList = OrderSearchCriteriaSortDirectionEnum.getSortDirectionFieldSelects();
    private List<LabelValueBean> searchDateList = OrderSearchCriteriaDateSelectEnum.getSearchSelectDateFields();
    private List<LabelValueBean> searchRefNumList = OrderSearchCriteriaRefNumSelectEnum.getSearchSelectRefNumFields();
    private List<LabelValueBean> searchPermissionStatusList = ItemAvailabilityEnum.getOrderItemAvailablityCodes();
    private List<LabelValueBean> searchOrderSourceList = EnumHelper.getOrderSourcesForSearch();
    private List<LabelValueBean> searchProductList = ProductEnum.getProductCodes();

    private boolean includeConfirmPageLinks = true;
    
    private boolean showExpandableViewChoice = StringUtils.defaultString(getText("show.expandable.checkbox")).equalsIgnoreCase("true");
	private boolean showResults = false;
	private boolean showCriteria = true;
	private boolean searchExecuted = false;
	
    private String newPage;
	private String newPageSize;
	
	private boolean returningToSearch = false;
 	
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
		
		searchResults = new OrderSearchResultWrapper();
		showCriteria = true;
		showResults = false;
		
		//copy Partner Id Code table to session
		if (getPartnerIDCodeTableInSession() == null){
			// grab code table
			Map<String,String> partnerIdCodeTable = OrderLicenseServices.getPartnerIDCodeMappings();
			storePartnerIDCodeTableInSession( partnerIdCodeTable);
		}
		
		if ( getQuickTabSelect() != null ) {			
			if ( getQuickTabSelect().isOrderSearch() ) {
				
				if ( getQuickTabSelect().isSearchByAllCriteria() ) {
				    // check if user has requested search yet
					if ( !isSearching() ) {
						//go to order search (display criteria)
						return SUCCESS;
					}
					
				} else if ( getQuickTabSelect().isSearchByUser() ) {
					if ( !isResorting() ) {
						// do search by user
						searchCriteria = new OrderSearchCriteriaWrapper(true /* use default sort */);
						searchCriteria.setInternetLogin_adv(UserContextHelperBase.getCurrentAsInterface().getAuthenticatedUser().getUsername());
						searchCriteria.setOrderStatus_adv("Not Completely Entered");
						searchCriteria.setSortCriteriaBy(OrderSearchCriteriaSortColumnEnum.SORT_BY_DEFAULT.name());
						searchCriteria.setSortOrder(OrderSearchCriteriaSortDirectionEnum.DESC.getUserText());
					} 
				} else {
					// Not a search request?
				}
		
				resetSortCriteria();
				
				setStartEndDisplayRows();
				
				searchResults = performSearch(searchCriteria);
				
				setIncludeCancelledOrders(searchCriteria.getShowCancelled());
				
				if (searchResults.isHasResults()) {
					showResults = true;
					if ( isReturningToSearch() ) {
						showCriteria = true;
					} else {
						showCriteria = false;
					}
				} else {
					searchCriteria.getPageControl().setListSize(0);
					searchCriteria.getPageControl().setTotalRecords(0);
					searchCriteria.getPageControl().setPage(1);
				}
			}
		} 
		
		setResorting(false);
		storeOrderSearchCriteriaInSession(searchCriteria);
		
		setStartRenderTime(MiscUtils.getNow());
		
		return SUCCESS;
		
	}
	
	private Date reset2DigitYearTo4Digit(Date twoDigitYearDate) {
		if ( twoDigitYearDate != null ) {
			Calendar fourDigitYear = Calendar.getInstance();
			fourDigitYear.setTime(twoDigitYearDate);
			int twoDigitYear = fourDigitYear.get(Calendar.YEAR);
			if ( twoDigitYear < 100 ) {
				fourDigitYear.set(Calendar.YEAR, twoDigitYear+2000);
				Date twoToFourDigitYear = fourDigitYear.getTime();
				return twoToFourDigitYear;
			}
		}
		return twoDigitYearDate;
	}
	
	@Override
	public void validate() {

		if (isSearching())
		{			
		super.validate();
		
		if (!hasErrors()) {
			
			// Added check to reset 2 digit year dates to 4 digit year
			// Will reset criteria display to 4 digit year on completion of search
			Date dtStart = reset2DigitYearTo4Digit(searchCriteria.getStartDate());
			if ( dtStart != null ) { searchCriteria.setStartDate(dtStart); }
			Date dtEnd =  reset2DigitYearTo4Digit(searchCriteria.getEndDate());
			if ( dtEnd != null ) { searchCriteria.setEndDate(dtEnd); }

			if (dtEnd != null && dtStart == null) {
				dtStart = dtEnd;
				searchCriteria.setStartDate(dtEnd);
			}
	
			if (dtStart != null && dtEnd == null) {
				dtEnd = dtStart;
				searchCriteria.setEndDate(dtStart);
			}
	
			if (dtStart != null
					&& dtEnd != null
					&& (searchCriteria.getDateType() == null || searchCriteria
							.getDateType().isEmpty()))
				addActionError("Please select Date Range");
			
			if (dtStart == null
					&& dtEnd == null
					&& (searchCriteria.getDateType() != null && !searchCriteria
							.getDateType().isEmpty()))
				addActionError("Please select \"To\" and \"From\" dates");
	
	
			if (dtEnd != null && dtStart != null && dtEnd.before(dtStart))
				addActionError("\"From\" Date should be no later than \"To\" Date");
	
			if (!hasActionErrors() && !hasFieldErrors() && !searchCriteria.isAnyFilterCriteriaSet())
				addActionError("Please enter search criteria");
			
			if (!hasActionErrors() && !hasFieldErrors() && 
					!searchCriteria.isAnyTFcriteriaSet() && !searchCriteria.isShowCoiOrders())
				addActionError("This field can not be used for searching in TF");
			}
		}
		
	}

	/**
	 * When a view page returns to search results - will used persisted
	 * search criteria.
	 */
	public String returnToSearchResults() {
		super.handleQuickTabSelected();
		searchCriteria = getOrderSearchCriteriaInSession();
		setSearching(true);
		setResorting(true);
		setResetToPage1(false);
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
	
	public String resortSearchResults() {
		
		// Get sort column to sort by and order 
		String sortBy = searchCriteria.getSortCriteriaBy();
		String sortOrder = searchCriteria.getSortOrder();
		// get saved criteria
		searchCriteria = getOrderSearchCriteriaInSession();
		searchCriteria.setSortCriteriaBy(sortBy);
		searchCriteria.setSortOrder(sortOrder);
		setSearching(true);
		setResorting(true);
		
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
		searchCriteria = getOrderSearchCriteriaInSession();
		if ( nPageSize > 0 ) {
			searchCriteria.getPageControl().setPageSize(nPageSize);
			searchCriteria.getPageControl().setPage(searchCriteria.getPageControl().getPageAfterHandlingPageSizeChange());
			searchCriteria.getPageControl().setLastPageSize(nPageSize);
		}
		setSearching(true);
		setResorting(false);
		setResetToPage1(false);
		
		execute();
		
		return SUCCESS;
	
	}	
	
	public String rePageResults() {
		int nPage = -1;
		if ( StringUtils.isNotEmpty(getNewPage()) ) {
			if ( StringUtils.isNumeric(getNewPage().trim()) ) {
				nPage = Integer.valueOf(getNewPage().trim()).intValue();
			}
		}
		searchCriteria = getOrderSearchCriteriaInSession();
		if ( nPage > 0 ) {
			searchCriteria.getPageControl().setPage(nPage);
		}
		setSearching(true);
		setResetToPage1(false);
		setResorting(false);
		
		execute();
		
		return SUCCESS;
	
	}
	
	private void setStartEndDisplayRows() {
		if ( isResetToPage1() ) {
			searchCriteria.setPageControl(new CustomPageControl(searchCriteria.getPageControl().getPageSize()));
			searchCriteria.setDisplayFromRow(getCustomPageControl().getPageSize() * ( getCustomPageControl().getPage() - 1 ) + 1 );
			searchCriteria.setDisplayToRow(getCustomPageControl().getPageSize() * getCustomPageControl().getPage());
		} else {
			if ( !getCustomPageControl().isPageSizeChanged() ) {
				searchCriteria.setDisplayFromRow(getCustomPageControl().getPageSize() * ( getCustomPageControl().getPage() - 1 ) + 1 );
				searchCriteria.setDisplayToRow(getCustomPageControl().getPageSize() * getCustomPageControl().getPage());
			} 
		}
	}
	
	private void resetSortCriteria() {
		resetSortCriteria(isResorting());
	}
	
	private void resetSortCriteria(boolean includeNoCriteriaMessage) {
		String sortBy = searchCriteria.getSortCriteriaBy();
		String sortOrder = searchCriteria.getSortOrder();
		// clear sort criteria
		searchCriteria.setSortCriteriaDefault();
		// add sort criteria
		if ( StringUtils.isNotEmpty(sortBy) && StringUtils.isNotEmpty(sortOrder) ) {
			searchCriteria.setSortCriteriaBy( sortBy );
			searchCriteria.setSortOrder(sortOrder);
		} else {
			searchCriteria.setSortCriteriaDefault();
			if ( includeNoCriteriaMessage ) {
				addActionError("No sort criteria specified, using default");
			}
		}	
	}
	

	/**
	 * Does actual search process
	 * @param osc
	 * @return
	 */
	private OrderSearchResultWrapper performSearch(OrderSearchCriteriaWrapper osc) {
		OrderSearchResult svcSearchResult = null;
		searchResults = new OrderSearchResultWrapper( svcSearchResult );
		try {
			setStartSearchTime(MiscUtils.getNow());
			double beginTime =(double)getStartSearchTime().getTime(); //start time
			OrderSearchCriteria searchCriteria = osc.getServiceSearchCriteriaForSearch();
			if (searchCriteria.isSufficientCriteria()) {
				OrderLicenseServices.setPartnerIdMap(getPartnerIDCodeTableInSession());
				svcSearchResult = OrderLicenseServices.searchOrderLicenses(searchCriteria);
				setEndSearchTime(MiscUtils.getNow());
				double endTime = (double)getEndSearchTime().getTime(); //end time
				setElapsedSeconds((endTime-beginTime)/1000);
				if ( svcSearchResult != null ) {
					if ( svcSearchResult.getOrderLicenses() != null && !svcSearchResult.getOrderLicenses().isEmpty() ) {
						searchResults = new OrderSearchResultWrapper( svcSearchResult  );
						int totalRecords = Long.valueOf(searchResults.getSvcSearchResults().getTotalRows()).intValue();
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
				searchExecuted = true;
			}
			else {
				addActionError("There is not enough criteria to perform your search.  Please enter additional criteria and try your search again. ");
				//showResults = false;
				searchExecuted = false;
			}
				
			
		} catch (OrderLicensesException e) {
			addActionError("A system error has prevented retrieving the requested confirmation");
		} catch (Exception e) {
			addActionError("Search failed: " + ExceptionUtils.getRootCauseMessage(e));
		}
		
		return searchResults;

	}
	
	public CustomPageControl getCustomPageControl() {
		if ( getSearchCriteria()!= null ) {
			if ( getSearchCriteria().getPageControl() != null ) {
				return getSearchCriteria().getPageControl();
			}
		}
		return new CustomPageControl();
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

	public List<LabelValueBean> getSearchDateList() {
		return searchDateList;
	}

	public void setSearchDateList(List<LabelValueBean> searchDateList) {
		this.searchDateList = searchDateList;
	}
	
    public List<LabelValueBean> getSearchRefNumList() {
		return searchRefNumList;
	}

	public void setSearchRefNumList(List<LabelValueBean> list) {
		this.searchRefNumList = list;
	}
	
	public List<LabelValueBean> getSearchProductList() {
		return searchProductList;
	}

	public void setSearchProductList(List<LabelValueBean> list) {
		this.searchProductList = list;
	}
	
	public List<LabelValueBean> getSearchPermissionStatusList() {
		return searchPermissionStatusList;
	}

	public void setSearchPermissionStatusList(List<LabelValueBean> list) {
		this.searchPermissionStatusList = list;
	}
	
	public List<LabelValueBean> getSearchOrderSourceList() {
		return searchOrderSourceList;
	}
	
	public void setSearchOrderSourceList(List<LabelValueBean> list) {
		this.searchOrderSourceList = list;
	}

    
    public OrderSearchResultWrapper getSearchResults() {
		return searchResults;
	}

	public void setSearchResults(OrderSearchResultWrapper searchResults) {
		this.searchResults = searchResults;
	}


	public boolean isIncludeExpandedResults() {
		if ( getSearchCriteria() != null ) {
			return getSearchCriteria().isIncludeExpandedResults();
		}
		return false;
	}
    public boolean isSearchExecuted() {
		return searchExecuted;
	}

	public void setSearchExecuted(boolean searchExecuted) {
		this.searchExecuted = searchExecuted;
	}

	public List<LabelValueBean> getSearchOrderList() {
		return searchOrderList;
	}

	public void setSearchOrderList(List<LabelValueBean> searchOrderList) {
		this.searchOrderList = searchOrderList;
	}

	public List<LabelValueBean> getSortDirectionList() {
		return sortDirectionList;
	}

	public void setSortDirectionList(List<LabelValueBean> sortDirectionList) {
		this.sortDirectionList = sortDirectionList;
	}

	public boolean isDisplayCourseInfo() {
		return searchCriteria.getShowCourseInfo();
	}

	public OrderSearchCriteriaWrapper getSearchCriteria() {
		return searchCriteria;
	}

	public void setSearchCriteria(OrderSearchCriteriaWrapper searchCriteria) {
		this.searchCriteria = searchCriteria;
	}
    
    public boolean isShowResults() {
		return showResults;
	}

	public void setShowResults(boolean showResults) {
		this.showResults = showResults;
	}

	public boolean isShowCriteria() {
		return showCriteria;
	}

	public void setShowCriteria(boolean showCriteria) {
		this.showCriteria = showCriteria;
	}
	
	public boolean isIncludeConfirmPageLinks() {
		return includeConfirmPageLinks;
	}

	public void setIncludeConfirmPageLinks(boolean includeConfirmPageLinks) {
		this.includeConfirmPageLinks = includeConfirmPageLinks;
	}

	public boolean isIncludeBundles() {
		if ( searchCriteria != null ) {
			return searchCriteria.getShowCourseInfo();
		}
		return false;
	}

	public boolean isShowExpandableViewChoice() {
		return showExpandableViewChoice;
	}

	public void setShowExpandableViewChoice(boolean showExpandableViewChoice) {
		this.showExpandableViewChoice = showExpandableViewChoice;
	}

	public boolean isReturningToSearch() {
		return returningToSearch;
	}

	public void setReturningToSearch(boolean returningToSearch) {
		this.returningToSearch = returningToSearch;
	}

	@Override
	public QuickTabSelect getDefaultQuickTabSelect() {
		return QuickTabSelect.ordermgmt_menu_order;
	}	
	
}

