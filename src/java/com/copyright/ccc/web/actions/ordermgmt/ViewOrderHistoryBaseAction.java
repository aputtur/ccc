package com.copyright.ccc.web.actions.ordermgmt;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.util.LabelValueBean;

import com.copyright.ccc.business.data.OrderLicense;
import com.copyright.ccc.business.data.OrderPurchase;
import com.copyright.ccc.business.data.OrderSearchCriteriaSortColumnEnum;
import com.copyright.ccc.business.data.OrderSearchCriteriaSortDirectionEnum;
import com.copyright.ccc.business.data.OrderSearchResult;
import com.copyright.ccc.business.security.CCPrivilegeCode;
import com.copyright.ccc.business.services.BusinessProfitEnum;
import com.copyright.ccc.business.services.ProductEnum;
import com.copyright.ccc.business.services.TranslatedEnum;
import com.copyright.ccc.business.services.order.OrderLicenseServices;
import com.copyright.ccc.business.services.order.OrderLicensesException;
import com.copyright.ccc.business.services.order.OrderPurchaseImpl;
import com.copyright.ccc.web.actions.ordermgmt.data.BundleWrapper;
import com.copyright.ccc.web.actions.ordermgmt.data.ConfirmationWrapper;
import com.copyright.ccc.web.actions.ordermgmt.data.CustomPageControl;
import com.copyright.ccc.web.actions.ordermgmt.data.EditBundle;
import com.copyright.ccc.web.actions.ordermgmt.data.EditConfirmation;
import com.copyright.ccc.web.actions.ordermgmt.data.EditItemWrapper;
import com.copyright.ccc.web.actions.ordermgmt.data.ItemWrapper;
import com.copyright.ccc.web.actions.ordermgmt.data.OrderSearchCriteriaWrapper;
import com.copyright.ccc.web.actions.ordermgmt.data.OrderSearchResultWrapper;
import com.copyright.ccc.web.actions.ordermgmt.data.ProcessMessage;
import com.copyright.ccc.web.actions.ordermgmt.util.MiscUtils;

public class ViewOrderHistoryBaseAction extends OMBaseAction implements CustomPageControlAware{


	private static final long serialVersionUID = 1L;
	
	private boolean expandOrderHeader = false;
	private boolean expandInvoiceHeader = false;
	private boolean expandBundleHeader = false;
	private boolean expandFirstBundleDetail = false;
	private boolean expandFirstOrderDetail = false;
	private boolean invoiceView = false;
	private boolean showAllProcessMesssages = false;
	
	private boolean haveNonInvoicedDetails = false;
	private boolean haveDetailsStoredInTF = false;
	private boolean haveDetailsStoredInOMS = false;
	private boolean haveAdjustedDetails = false;
	private boolean haveOpenDetails = false;
	
	List<ProcessMessage> allProcessMessages = new ArrayList<ProcessMessage>();
	
    private boolean includeExpandedResults = true;
    private boolean includeConfirmPageLinks = false;
    private boolean includeBundles = false;
 	
	private OrderSearchResultWrapper wrappedResults;
	private ConfirmationWrapper selectedOrder;
	
	private OrderSearchCriteriaWrapper searchCriteria = new OrderSearchCriteriaWrapper(true, true /* use default sort, for view */);
	
	private boolean rePagingOrSizing = false;
	private boolean pagingDetails = false;
	private boolean pagingBundleDetails = false;
	private boolean pagingIndividualDetails = false;
	
	private String newPage;
	private String newPageSize;
	
    private List<LabelValueBean> viewOrderSortByList = OrderSearchCriteriaSortColumnEnum.getViewOrderSortFieldSelects();
    private List<LabelValueBean> sortDirectionList = OrderSearchCriteriaSortDirectionEnum.getSortDirectionFieldSelects();
    private List<LabelValueBean> searchProductList = ProductEnum.getProductCodes();
    private List<LabelValueBean> businessProfitList = BusinessProfitEnum.getBusinessProfits();
    private List<LabelValueBean> translatedList = TranslatedEnum.getTranslatedList();
    private List<LabelValueBean> translatedLanguageList = TranslatedEnum.getTranslatedLanguageList();
    private ProductEnum[] productDetails = ProductEnum.values();
	
    private OrderPurchase editOrder = new OrderPurchaseImpl();
	private List<EditItemWrapper> editDetails = new ArrayList<EditItemWrapper>();
	private EditConfirmation editConfirm = new EditConfirmation();
	private List<EditBundle> editBundles = new ArrayList<EditBundle>();
	
	private Long searchConfirmNumber;
	private Long searchBundleNumber;
	private Long searchDetailNumber;
	private String searchInvoiceNumber;
	private Long searchOrderId;
	private Long searchBundleId;
   
	private CustomPageControl pageControl = new CustomPageControl(Integer.valueOf(StringUtils.defaultString(getText("paging.threshold"),"5")).intValue());


	/**
	 * Default action - executed when page is first reached
	 */
	@Override
	public String execute()
	{
		return execute(false);
	}
	public String execute(boolean makeSureSelectedDetailIsPresent)
	{		
		super.handleQuickTabSelected();
		
		if (!super.verifyPrivilege(CCPrivilegeCode.SEARCH_ORDERS))
			return NOT_AUTHORIZED;
		
		setSelectedConfirmBundleInvoiceDetailFields();
				
		if ( !isRePagingOrSizing() && !isResorting() ) {
			searchCriteria = super.getOrderSearchCriteriaInSession(OMBaseAction.VIEW_ORDER_SEARCH_CRITERIA_KEY);
			searchCriteria.clearFilterCriteria();
			searchCriteria.setSortCriteriaBy(OrderSearchCriteriaSortColumnEnum.SORT_BY_VIEW_DEFAULT.name());
			searchCriteria.setSortOrder(OrderSearchCriteriaSortDirectionEnum.ASC.getUserText());
			searchCriteria.setShowCancelled(isIncludeCancelledOrders());
			if ( isInvoiceView() ) {
				searchCriteria.setInvoiceNumber(getSearchInvoiceNumber());
			} else {
				searchCriteria.setConfNumber(Long.valueOf(getSearchConfirmNumber()).toString());				
			}
		
			setPageControl(new CustomPageControl(getDetailPagingThreshold()));
		}
						
		OrderSearchResult searchResult = null;
		try {
			setStartSearchTime(MiscUtils.getNow());
			double beginTime =(double) getStartSearchTime().getTime(); //start time

			setStartEndDisplayRows( searchCriteria );
			
			searchResult = OrderLicenseServices.searchOrderLicenses(searchCriteria.getServiceSearchCriteriaForSearch());
			if ( searchResult == null ) {
				throw new OrderLicensesException("searchOrderLicenses returned a null OrderSearchResult object","SearchOrderFailied");
			}
			if ( makeSureSelectedDetailIsPresent) {
				searchResult = makeSureDetailIsInCurrentResults(searchCriteria, searchResult);
			}
			setEndSearchTime(MiscUtils.getNow());
			double endTime = (double)getEndSearchTime().getTime(); //end time
			setElapsedSeconds((endTime-beginTime)/1000);
							
			getSearchCriteria().setLastServiceSearchCritera(searchResult.getOrderSearchCriteria());
			getPageControl().setListSize(Long.valueOf(searchResult.getTotalRows()).intValue());
			getPageControl().setTotalRecords(Long.valueOf(searchResult.getTotalRows()).intValue());
			pagingDetails = searchResult.getTotalRows() > getDetailPagingThreshold();		
		
			if ( searchResult.getOrderLicenses() != null && !searchResult.getOrderLicenses().isEmpty() ) {
				wrappedResults = new OrderSearchResultWrapper( searchResult , true /* Hierarchical order */);
			}
			if ( wrappedResults != null ) {
				if ( wrappedResults.getSvcSearchResults().getOrderBundles().size() > 0 ) {
					pagingBundleDetails = pagingDetails;
				} else {
					pagingIndividualDetails = pagingDetails ;
				}
			}
		} catch (OrderLicensesException e) {
			addActionError("A system error has prevented retrieving the requested confirmation");
		}
		searchCriteria.setPageControl(getPageControl());		
		setStartRenderTime(MiscUtils.getNow());
		storeOrderSearchResultsWrapperInSession(wrappedResults);
		storeOrderSearchCriteriaInSession(searchCriteria, OMBaseAction.VIEW_ORDER_SEARCH_CRITERIA_KEY);
		storeCustomPageControlInSession(pageControl);
		setRePagingOrSizing(false);

		return SUCCESS;
	}
	
	public String resortSearchResults() {
		// Get sort column to sort by and order 
		String sortBy = searchCriteria.getSortCriteriaBy();
		String sortOrder = searchCriteria.getSortOrder();
		// get saved criteria
		searchCriteria = super.getOrderSearchCriteriaInSession(OMBaseAction.VIEW_ORDER_SEARCH_CRITERIA_KEY);
		searchCriteria.setSortCriteriaBy(sortBy);
		searchCriteria.setSortOrder(sortOrder);
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
		
		String executeResult = execute();
		
		if (executeResult == NOT_AUTHORIZED)
			return NOT_AUTHORIZED;
		
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
		pageControl = getCustomPageControlInSession();
		if ( nPage > 0 ) {
			pageControl.setPage(nPage);
		}
		searchCriteria = super.getOrderSearchCriteriaInSession(OMBaseAction.VIEW_ORDER_SEARCH_CRITERIA_KEY);
		setSearching(true);
		setResorting(false);
		setRePagingOrSizing(true);
		
		String executeResult = execute();
		
		if (executeResult == NOT_AUTHORIZED)
			return NOT_AUTHORIZED;
		
		return SUCCESS;
	
	}
	
	protected void setSelectedConfirmBundleInvoiceDetailFields() {
						
		if ( StringUtils.isNotEmpty(getSelectedConfirmNumber() ) ) {
			if ( StringUtils.isNumeric(getSelectedConfirmNumber().trim()) ) {
				setSearchConfirmNumber(Long.valueOf(getSelectedConfirmNumber().trim()));
			}
		}
				
		if ( StringUtils.isNotEmpty(getSelectedBundleNumber() ) ) {
			if ( getSelectedBundleNumber().trim().equalsIgnoreCase("-1") ) {
				setSearchBundleNumber(Long.valueOf(getSelectedBundleNumber().trim()));
			} else {
				if ( StringUtils.isNumeric(getSelectedBundleNumber().trim()) ) {
					setSearchBundleNumber(Long.valueOf(getSelectedBundleNumber().trim()));
				}
			}
		} else {
			setSearchBundleNumber(Long.valueOf(-1L));
		}
				
		if ( StringUtils.isNotEmpty(getSelectedDetailNumber() ) ) {
			if ( StringUtils.isNumeric(getSelectedDetailNumber().trim()) ) {
				setSearchDetailNumber(Long.valueOf(getSelectedDetailNumber().trim()));
			}
		}
				
		if ( StringUtils.isNotEmpty(getSelectedOrderId() ) ) {
			if ( StringUtils.isNumeric(getSelectedOrderId().trim()) ) {
				setSearchOrderId(Long.valueOf(getSelectedOrderId().trim()));
			}
		}
		
		if ( StringUtils.isNotEmpty(getSelectedBundleId() ) ) {
			if ( StringUtils.isNumeric(getSelectedBundleId().trim()) ) {
				setSearchBundleId(Long.valueOf(getSelectedBundleId().trim()));
			}
		}
		
		if ( StringUtils.isNotEmpty(getSelectedInvoiceNumber() ) ) {
			//commenting out check for numeric, since it should for non-numeric as well 
			//if ( StringUtils.isNumeric(getSelectedInvoiceNumber().trim()) ) {
				setSearchInvoiceNumber(getSelectedInvoiceNumber().trim());
				setInvoiceView(true);
			//}
		}
		
	}
	protected void setExpandHeaderAndDetailFlags() {		
	
		if ( !isRePagingOrSizing() ) {
			if ( StringUtils.isNotEmpty( getSelectedItem() ) ) {
				setExpandFirstBundleDetail( getSelectedItem().equalsIgnoreCase(OrderManagementConstants.BUNDLE_DETAIL) );
				setExpandBundleHeader( isExpandFirstBundleDetail() 
					|| getSelectedItem().equalsIgnoreCase(OrderManagementConstants.BUNDLE_HEADER) );
				setExpandFirstOrderDetail( getSelectedItem().equalsIgnoreCase(OrderManagementConstants.ORDER_DETAIL) );
				setExpandOrderHeader( getSelectedItem().equalsIgnoreCase(OrderManagementConstants.ORDER_HEADER) );
			}
		}
	}

	protected OrderSearchResult makeSureDetailIsInCurrentResults(OrderSearchCriteriaWrapper searchCriteria, OrderSearchResult currentSearchResult) throws OrderLicensesException {
		
		boolean detailIsPresent = false;
		
		if ( isExpandFirstBundleDetail() || isExpandFirstOrderDetail() ) {
			for ( OrderLicense ol : currentSearchResult.getOrderLicenses() ) {
				if ( ol.getID() == getSearchDetailNumber().longValue() ) {
					detailIsPresent = true;
					break;
				}
			}
		} else {
			detailIsPresent = true;
		}
		if ( detailIsPresent ) {
			return currentSearchResult;
		}
		
		int nextFromRow = searchCriteria.getDisplayToRow() + 1;
		int nextToRow = searchCriteria.getDisplayToRow() + getPageControl().getPageSize();
		if ( nextToRow > currentSearchResult.getTotalRows() ) {
			nextToRow = currentSearchResult.getTotalRows();
		}
		searchCriteria.setDisplayFromRow(nextFromRow);
		searchCriteria.setDisplayToRow(nextToRow);
		getPageControl().setPage( getPageControl().getPage() + 1 );
		
		OrderSearchResult searchResult = new OrderSearchResult();
		while (! detailIsPresent ) {
			searchResult = OrderLicenseServices.searchOrderLicenses(searchCriteria.getServiceSearchCriteriaForSearch());
			getSearchCriteria().setLastServiceSearchCritera(searchResult.getOrderSearchCriteria());

			if ( isExpandFirstBundleDetail() || isExpandFirstOrderDetail() ) {
				for ( OrderLicense ol : searchResult.getOrderLicenses() ) {
					if ( ol.getID() == getSearchDetailNumber().longValue() ) {
						detailIsPresent = true;
						break;
					}
				}
				if ( !detailIsPresent ) {
					nextFromRow = searchCriteria.getDisplayToRow() + 1;
					nextToRow = searchCriteria.getDisplayToRow() + getPageControl().getPageSize();
					if ( nextToRow > searchResult.getTotalRows() ) {
						nextToRow = searchResult.getTotalRows();
					}
					searchCriteria.setDisplayFromRow(nextFromRow);
					searchCriteria.setDisplayToRow(nextToRow);
					getPageControl().setPage( getPageControl().getPage() + 1 );
				}
			} else {
				detailIsPresent = true;
			}
		}
		    
		setPagingDetails( searchResult.getTotalRows() > getDetailPagingThreshold() );
		if ( isPagingDetails() ) {
			getPageControl().setListSize(Long.valueOf(searchResult.getTotalRows()).intValue());
			getPageControl().setTotalRecords(Long.valueOf(searchResult.getTotalRows()).intValue());
		}
		    
		return searchResult;

	}

	protected void setStartEndDisplayRows(OrderSearchCriteriaWrapper searchCriteria) {
		if ( !getPageControl().isPageSizeChanged() ) {
			searchCriteria.setDisplayFromRow(getPageControl().getPageSize() * ( getPageControl().getPage() - 1 ) + 1 );
			searchCriteria.setDisplayToRow(getPageControl().getPageSize() * getPageControl().getPage());
		} 
	}
	
	protected void setSelectedFieldsFor( EditItemWrapper jumpto ) {
		setSelectedBundleNumber(jumpto.getItem().getBundleId());
		setSelectedDetailNumber(jumpto.getItem().getID());
		if ( jumpto.getItem().isABundledDetail() ) {
			setSelectedItem(OrderManagementConstants.BUNDLE_DETAIL);
		} else {
			setSelectedItem(OrderManagementConstants.ORDER_DETAIL);
		}
	}
		
	protected void postProcessMessagesToDetails(List<ItemWrapper> details, List<ProcessMessage> messages, List<EditItemWrapper> prEdits) {
		for ( ItemWrapper detail : details ) {
			detail.setProcessMessages( getDetailsMessages(detail.getItem().getID(), messages) );
			detail.setLastEdit(getDetailEdit(detail.getItem().getID(), prEdits));
		}
	}
	
	protected void postProcessMessagesToOrder(List<ConfirmationWrapper> orders, List<ProcessMessage> messages, List<EditItemWrapper> prEdits) {
		for ( ConfirmationWrapper order : orders ) {
			for ( ItemWrapper detail : order.getMyItems() ) {
				detail.setProcessMessages( getDetailsMessages(detail.getItem().getID(), messages) );
				detail.setLastEdit(getDetailEdit(detail.getItem().getID(), prEdits));
			}
			for ( BundleWrapper bundle : order.getMyBundles() ) {
				for ( ItemWrapper detail : bundle.getMyItems() ) {
					detail.setProcessMessages( getDetailsMessages(detail.getItem().getID(), messages) );
					detail.setLastEdit(getDetailEdit(detail.getItem().getID(), prEdits));
				}
			}
		}
	}
	
	protected List<ProcessMessage> getDetailsMessages(long detailId, List<ProcessMessage> messages) {
		List<ProcessMessage> dlist = new ArrayList<ProcessMessage>();
		for ( ProcessMessage pm : messages ) {
			if ( pm.getDetailId().equals(Long.toString(detailId)) ) {
				dlist.add(pm);
			}
		}
		return dlist;
	}
	
	protected EditItemWrapper getDetailEdit(long detailId, List<EditItemWrapper> edits) {
		EditItemWrapper edit = new EditItemWrapper();
		for ( EditItemWrapper eiw : edits ) {
			if ( eiw.getItem().getID().equals(Long.toString(detailId)) ) {
				return eiw;
			}
		}
		return edit;
	}
	
	public List<LabelValueBean> getSearchProductList() {
		return searchProductList;
	}

	public void setSearchProductList(List<LabelValueBean> searchProductList) {
		this.searchProductList = searchProductList;
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

	
	public CustomPageControl getPageControl() {
		if (pageControl == null) {
			setPageControl(new CustomPageControl());
		}
		return pageControl;
	}

	public void setPageControl(CustomPageControl pageControl) {
		this.pageControl = pageControl;
	}

	public boolean isExpandOrderHeader() {
		return expandOrderHeader;
	}

	public void setExpandOrderHeader(boolean expandOrderHeader) {
		this.expandOrderHeader = expandOrderHeader;
	}

	public boolean isExpandInvoiceHeader() {
		return expandInvoiceHeader;
	}

	public void setExpandInvoiceHeader(boolean expandInvoiceHeader) {
		this.expandInvoiceHeader = expandInvoiceHeader;
	}

	public boolean isInvoiceView() {
		return invoiceView;
	}

	public void setInvoiceView(boolean invoiceView) {
		this.invoiceView = invoiceView;
	}

	public boolean isExpandBundleHeader() {
		return expandBundleHeader;
	}

	public void setExpandBundleHeader(boolean expandBundleHeader) {
		this.expandBundleHeader = expandBundleHeader;
	}

	public boolean isExpandFirstBundleDetail() {
		return expandFirstBundleDetail;
	}

	public void setExpandFirstBundleDetail(boolean expandFirstBundleDetail) {
		this.expandFirstBundleDetail = expandFirstBundleDetail;
	}

	public boolean isExpandFirstOrderDetail() {
		return expandFirstOrderDetail;
	}

	public void setExpandFirstOrderDetail(boolean expandFirstOrderDetail) {
		this.expandFirstOrderDetail = expandFirstOrderDetail;
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

	public String getSearchInvoiceNumber() {
		return searchInvoiceNumber;
	}

	public void setSearchInvoiceNumber(String searchInvoiceNumber) {
		this.searchInvoiceNumber = searchInvoiceNumber;
	}

	public boolean isIncludeExpandedResults() {
		return includeExpandedResults;
	}
	public void setIncludeExpandedResults(boolean includeExpandedResults) {
		this.includeExpandedResults = includeExpandedResults;
	}
	public boolean isIncludeConfirmPageLinks() {
		return includeConfirmPageLinks;
	}
	public void setIncludeConfirmPageLinks(boolean includeConfirmPageLinks) {
		this.includeConfirmPageLinks = includeConfirmPageLinks;
	}
	public boolean isIncludeBundles() {
		return includeBundles;
	}
	public void setIncludeBundles(boolean includeBundles) {
		this.includeBundles = includeBundles;
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
		this.includeBundles = false;
		if ( pagingDetails ) {
			this.includeBundles = true;
		} 
		this.pagingDetails = pagingDetails;
	}

	public boolean isPagingBundleDetails() {
		return pagingBundleDetails;
	}

	public void setPagingBundleDetails(boolean pagingBundleDetails) {
		this.pagingBundleDetails = pagingBundleDetails;
	}

	public boolean isPagingIndividualDetails() {
		return pagingIndividualDetails;
	}

	public void setPagingIndividualDetails(boolean pagingIndividualDetails) {
		this.pagingIndividualDetails = pagingIndividualDetails;
	}

	public OrderSearchCriteriaWrapper getSearchCriteria() {
		return searchCriteria;
	}

	public void setSearchCriteria(OrderSearchCriteriaWrapper searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	public List<LabelValueBean> getViewOrderSortByList() {
		return viewOrderSortByList;
	}

	public void setViewOrderSortByList(List<LabelValueBean> viewOrderSortByList) {
		this.viewOrderSortByList = viewOrderSortByList;
	}

	public List<LabelValueBean> getSortDirectionList() {
		return sortDirectionList;
	}

	public void setSortDirectionList(List<LabelValueBean> sortDirectionList) {
		this.sortDirectionList = sortDirectionList;
	}

	public ProductEnum[] getProductDetails() {
		return productDetails;
	}

	public void setProductDetails(ProductEnum[] productDetails) {
		this.productDetails = productDetails;
	}

	public List<EditItemWrapper> getEditDetails() {
		return editDetails;
	}

	public void setEditDetails(List<EditItemWrapper> editDetails) {
		this.editDetails = editDetails;
	}

	public List<LabelValueBean> getBusinessProfitList() {
		return businessProfitList;
	}

	public void setBusinessProfitList(List<LabelValueBean> businessProfitList) {
		this.businessProfitList = businessProfitList;
	}

	public List<LabelValueBean> getTranslatedList() {
		return translatedList;
	}

	public void setTranslatedList(List<LabelValueBean> translatedList) {
		this.translatedList = translatedList;
	}

	public List<LabelValueBean> getTranslatedLanguageList() {
		return translatedLanguageList;
	}

	public void setTranslatedLanguageList(
			List<LabelValueBean> translatedLanguageList) {
		this.translatedLanguageList = translatedLanguageList;
	}

	public Long getSearchOrderId() {
		return searchOrderId;
	}

	public void setSearchOrderId(Long searchOrderId) {
		this.searchOrderId = searchOrderId;
	}

	public boolean isShowAllProcessMesssages() {
		return showAllProcessMesssages;
	}

	public void setShowAllProcessMesssages(boolean showAllProcessMesssages) {
		this.showAllProcessMesssages = showAllProcessMesssages;
	}

	public List<ProcessMessage> getAllProcessMessages() {
		return allProcessMessages;
	}

	public void setAllProcessMessages(List<ProcessMessage> allProcessMessages) {
		this.allProcessMessages = allProcessMessages;
	}

	public Long getSearchBundleId() {
		return searchBundleId;
	}

	public void setSearchBundleId(Long searchBundleId) {
		this.searchBundleId = searchBundleId;
	}

	public EditConfirmation getEditConfirm() {
		return editConfirm;
	}

	public void setEditConfirm(EditConfirmation editConfirm) {
		this.editConfirm = editConfirm;
	}

	public List<EditBundle> getEditBundles() {
		return editBundles;
	}

	public void setEditBundles(List<EditBundle> editBundles) {
		this.editBundles = editBundles;
	}

	public boolean isHaveNonInvoicedDetails() {
		return haveNonInvoicedDetails;
	}
	public void setHaveNonInvoicedDetails(boolean haveNonInvoicedDetails) {
		this.haveNonInvoicedDetails = haveNonInvoicedDetails;
	}
	public boolean isHaveDetailsStoredInTF() {
		return haveDetailsStoredInTF;
	}
	public void setHaveDetailsStoredInTF(boolean haveDetailsStoredInTF) {
		this.haveDetailsStoredInTF = haveDetailsStoredInTF;
	}
	public boolean isHaveDetailsStoredInOMS() {
		return haveDetailsStoredInOMS;
	}
	public void setHaveDetailsStoredInOMS(boolean haveDetailsStoredInOMS) {
		this.haveDetailsStoredInOMS = haveDetailsStoredInOMS;
	}
	public boolean isHaveAdjustedDetails() {
		return haveAdjustedDetails;
	}
	public void setHaveAdjustedDetails(boolean haveAdjustedDetails) {
		this.haveAdjustedDetails = haveAdjustedDetails;
	}
	public boolean isHaveOpenDetails() {
		return haveOpenDetails;
	}
	public void setHaveOpenDetails(boolean haveOpenDetails) {
		this.haveOpenDetails = haveOpenDetails;
	}
	@Override
	public QuickTabSelect getDefaultQuickTabSelect() {
		return QuickTabSelect.ordermgmt_menu_order;
	}	

}
