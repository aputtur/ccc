package com.copyright.ccc.web.actions.ordermgmt;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;

import com.copyright.base.enums.DurationEnum;
import com.copyright.ccc.business.data.OrderLicense;
import com.copyright.ccc.business.data.OrderPurchase;
import com.copyright.ccc.business.data.OrderSearchCriteriaSortColumnEnum;
import com.copyright.ccc.business.data.OrderSearchCriteriaSortDirectionEnum;
import com.copyright.ccc.business.data.OrderSearchResult;
import com.copyright.ccc.business.security.CCPrivilegeCode;
import com.copyright.ccc.business.services.BusinessProfitEnum;
import com.copyright.ccc.business.services.OrderDataSourceEnum;
import com.copyright.ccc.business.services.ProductEnum;
import com.copyright.ccc.business.services.TranslatedEnum;
import com.copyright.ccc.business.services.order.OrderLicenseServices;
import com.copyright.ccc.business.services.order.OrderLicensesException;
import com.copyright.ccc.business.services.order.OrderPurchaseImpl;
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
import com.copyright.ccc.web.transaction.RepublicationConstants;
import com.copyright.ccc.web.transaction.TransactionConstants;

public class ViewOrderHistoryMultiPagingBaseAction extends OMBaseAction {


	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ViewOrderHistoryMultiPagingBaseAction.class);
	
	protected static final String INDIVIDUAL_GROUP_ID = "-1";
	
	private boolean expandOrderHeader = false;
	private boolean expandInvoiceHeader = false;
	private boolean[] expandBundleHeader = new boolean[10];

	private boolean[] expandFirstBundleDetail = new boolean[10];
	private boolean expandFirstOrderDetail = false;
	private boolean showAllProcessMesssages = false;
	
	private boolean haveNonInvoicedDetails = false;
	private boolean haveNonCanceledDetails = false;
	private boolean haveNonRLDetails = false;
	private boolean haveDetailsStoredInTF = false;
	private boolean haveDetailsStoredInOMS = false;
	private boolean haveOpenDetails = false;
	private boolean haveAdjustedDetails = false;
	private boolean invoiceView = false;

	List<ProcessMessage> allProcessMessages = new ArrayList<ProcessMessage>();
	
    private boolean includeExpandedResults = true;
    private boolean includeConfirmPageLinks = false;
    private boolean includeBundles = false;
 	
	private ConfirmationWrapper selectedOrder;
	
	private OrderSearchCriteriaWrapper[] searchCriteria = new OrderSearchCriteriaWrapper[100];
	
	private boolean[] rePagingOrSizing = new boolean[100];
	private boolean resizing = false;
	private boolean[] pagingDetails;
	private String pagingIndex = null;
	private String requestedPagingIndex = null;
	
	private String[] newPage = new String[100];
	private String[] newPageSize = new String[100];
	private String[] newSortBy = new String[100];
	private String[] newSortOrder = new String[100];

    private List<LabelValueBean> viewOrderSortByList = OrderSearchCriteriaSortColumnEnum.getViewOrderSortFieldSelects();
    private List<LabelValueBean> sortDirectionList = OrderSearchCriteriaSortDirectionEnum.getSortDirectionFieldSelects();
    private List<LabelValueBean> searchProductList = ProductEnum.getProductCodes();
    private List<LabelValueBean> businessProfitList = BusinessProfitEnum.getBusinessProfits();
    private List<LabelValueBean> contentTypeList;
    private List<LabelValueBean> translatedList = TranslatedEnum.getTranslatedList();
    private List<LabelValueBean> translatedLanguageList = TranslatedEnum.getTranslatedLanguageList();
    
    private List<LabelValueBean> durationList;
    private List<LabelValueBean> businessList;
    private List<LabelValueBean> submitterAuthorList;

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
   
	private CustomPageControl[] pageControl = new CustomPageControl[100];


	public ViewOrderHistoryMultiPagingBaseAction() {
		super();
		// need to default the custom page control to something non-null
		for (int i=0; i<pageControl.length; i++){
			pageControl[i] = new CustomPageControl();			
		}
	}
	/**
	 * Default action - executed when page is first reached
	 */
	@Override
	public String execute()
	{
		return execute(-1);
	}
	public String execute(int pagingIndex)
	{		
		super.handleQuickTabSelected();
		
		if (!super.verifyPrivilege(CCPrivilegeCode.SEARCH_ORDERS))
			return NOT_AUTHORIZED;
		
		setSelectedConfirmBundleInvoiceDetailFields();
		
		if ( pagingIndex < 0 ) {
			setPagingSearchCriteria();
		}
		
		try {
			setStartSearchTime(MiscUtils.getNow());
			double beginTime =(double) getStartSearchTime().getTime(); //start time
			
			performSearchForPagingCriteria(pagingIndex);
			
			setEndSearchTime(MiscUtils.getNow());
			double endTime = (double)getEndSearchTime().getTime(); //end time
			
			setElapsedSeconds((endTime-beginTime)/1000);
			
		} catch (OrderLicensesException e) {
			addActionError("A system error has prevented retrieving the requested confirmation");
		}
		
		setStartRenderTime(MiscUtils.getNow());

		return SUCCESS;
	}
	
	protected void setPagingSearchCriteria() {
		
		OrderSearchCriteriaWrapper searchOrderCriteria = super.getOrderSearchCriteriaInSession(OMBaseAction.VIEW_ORDER_SEARCH_CRITERIA_KEY);
		searchOrderCriteria.clearFilterCriteria();
		searchOrderCriteria.setSortCriteriaBy(OrderSearchCriteriaSortColumnEnum.SORT_BY_VIEW_DEFAULT.name());
		searchOrderCriteria.setSortOrder(OrderSearchCriteriaSortDirectionEnum.ASC.getUserText());
		
//		When showing details for an order header, show cancelled as well as non cancelled.
//		searchOrderCriteria.setShowCancelled(isIncludeCancelledOrders());
		searchOrderCriteria.setShowCancelled(true);
		
		searchOrderCriteria.setConfNumber(Long.valueOf(getSearchConfirmNumber()).toString());

		searchOrderCriteria.setPageControl(new CustomPageControl(getDetailPagingThreshold()));
		
		setPagingSearchCriteriaForOrder(searchOrderCriteria);

	}

	protected void setPagingSearchCriteriaForOrder(OrderSearchCriteriaWrapper searchCriteria) {
				
		List<Long> bundles = null;
		
		OrderPurchase orderPurchase;
		try {
			orderPurchase = OrderPurchaseServices.getOrderPurchaseForConfirmationNumber( getSearchConfirmNumber());
			if (orderPurchase != null) {
				if (orderPurchase.getOrderDataSource() == OrderDataSourceEnum.OMS.getOrderDataSourceId().intValue()) {
					this.setSelectedItemStoredInOMS(true);
				} else {
					this.setSelectedItemStoredInOMS(false);	
				}
			}
		} catch (OrderPurchasesException e) {
			LOGGER.error( "Error retrieving order purchase by confirm number to determine source: " + getSearchConfirmNumber() + " " +
					"Message Code: " + e.getMessageCode() + " Message: " + e.getMessage() + " "	 +
					"Stack Trace: " + ExceptionUtils.getFullStackTrace(e));

		}
		
		if ( isSelectedItemStoredInOMS() ) {
			bundles = OrderServicesHelper.getBundleIdsForOMSConfirmNumber( getSearchConfirmNumber() );
		} else {
			bundles = OrderServicesHelper.getBundleIdsForTFConfirmNumber( getSearchConfirmNumber() );			
		}
		setSearchCriteria(new OrderSearchCriteriaWrapper[bundles.size()+1]);

		int pagingIndex = 0;	
		
		boolean tfHasBundles = false;
		
		if ( bundles.size() > 0 ) {
			
			// initialize bundle paged arrays
			setExpandBundleHeader(new boolean[bundles.size()]);
			setExpandFirstBundleDetail(new boolean[bundles.size()]);
//			setSavingBundle(new String[bundles.size()]);
		
			for ( Long bid : bundles ) {
				OrderSearchCriteriaWrapper bundleCriteria = OrderSearchCriteriaWrapper.copy(searchCriteria, true, true);
				bundleCriteria.setBundleNumber(bid.toString());
				if ( isSelectedItemStoredInOMS() ) {
					bundleCriteria.setIncludeOnlyBundledItems(true);
					bundleCriteria.setShowTfOrders(false);
					bundleCriteria.setShowCoiOrders(true);
				} else {
					bundleCriteria.setIncludeOnlyBundledItems(false);
					bundleCriteria.setShowTfOrders(true);
					bundleCriteria.setShowCoiOrders(false);
					tfHasBundles = true;
				}
				bundleCriteria.setPagingGroupKey(bid.toString());
				getSearchCriteria()[pagingIndex] = bundleCriteria;
				getExpandBundleHeader()[pagingIndex] = true;
				getExpandFirstBundleDetail()[pagingIndex] = false;
				pagingIndex++;
				if ( tfHasBundles ) {
					break;
				} 
			}
		}
		OrderSearchCriteriaWrapper individualCriteria = OrderSearchCriteriaWrapper.copy(searchCriteria, true, true);
		if ( isSelectedItemStoredInOMS() ) {
			individualCriteria.setIncludeOnlyUnBundledItems(true);
			individualCriteria.setShowTfOrders(false);
			individualCriteria.setShowCoiOrders(true);
		} else {
			individualCriteria.setIncludeOnlyUnBundledItems(false);
			individualCriteria.setShowCoiOrders(false);
			individualCriteria.setShowTfOrders(true); 
			if (tfHasBundles ) {
				individualCriteria.setPagingGroupKeyUsed(false);
			}
		}
		individualCriteria.setPagingGroupKey(INDIVIDUAL_GROUP_ID);
		getSearchCriteria()[pagingIndex] = individualCriteria;
		// initialize paged arrays
		setPagingDetails(new boolean[pagingIndex+1]);
		setPageControl(new CustomPageControl[pagingIndex+1]);
		setNewPage(new String[pagingIndex+1]);
		setNewPageSize(new String[pagingIndex+1]);
		setRePagingOrSizing(new boolean[pagingIndex+1]);
		setWrappedResultsArray(new OrderSearchResultWrapper[pagingIndex+1]);
		for ( boolean pagingOrSizing : getRePagingOrSizing() ) {
			if ( pagingOrSizing ) { pagingOrSizing = false; }
		}
	}
	
	protected int performSearchForOneCriteria ( OrderSearchCriteriaWrapper searchCriteria, int pagingIndex, int detailStartingIndex) throws OrderLicensesException {
	
		OrderSearchResult searchResult = new OrderSearchResult();
		boolean crMapKeyIsBundle = !searchCriteria.getPagingGroupKey().equals(INDIVIDUAL_GROUP_ID);
		if ( searchCriteria.isPagingGroupKeyUsed() ) {
			searchResult = OrderLicenseServices.searchOrderLicenses(searchCriteria.getServiceSearchCriteriaForSearch());
		}
		searchCriteria.setDisplayFromRow(searchCriteria.getPageControl().getPageSize() * ( searchCriteria.getPageControl().getPage() - 1 ) + 1 );
		searchCriteria.setDisplayToRow(searchCriteria.getPageControl().getPageSize() * searchCriteria.getPageControl().getPage());
		searchCriteria.getPageControl().setListSize(Long.valueOf(searchResult.getTotalRows()).intValue());
		searchCriteria.getPageControl().setTotalRecords(Long.valueOf(searchResult.getTotalRows()).intValue());

		if ( !getRePagingOrSizing()[pagingIndex] ) {
			setExpandHeaderAndDetailFlags(pagingIndex, searchCriteria.getPagingGroupKey());
			if ( crMapKeyIsBundle && getExpandFirstBundleDetail()[pagingIndex] ) {
				searchResult = makeSureDetailIsInCurrentBundleResults( searchResult, pagingIndex );
			} else if ( !crMapKeyIsBundle && isExpandFirstOrderDetail()  ) {
					searchResult = makeSureDetailIsInCurrentIndividualResults( searchResult, pagingIndex );
			}
		}
		searchCriteria.setLastServiceSearchCritera(searchResult.getOrderSearchCriteria());
		getPagingDetails()[pagingIndex] = searchResult.getTotalRows() > getDetailPagingThreshold();

		//prevent spurious ArrayIndexOutOfBounds exception
		if (getPageControl().length >= pagingIndex) {
			getPageControl()[pagingIndex] = CustomPageControl.copy( searchCriteria.getPageControl() );
		}
		OrderSearchResultWrapper osrw = new OrderSearchResultWrapper( detailStartingIndex, searchResult , true /* Hierarchical order */);
		osrw.setSearchCriteria(searchCriteria);
		getWrappedResultsArray()[pagingIndex] = osrw;
		
		
		return getWrappedResultsArray()[pagingIndex].getDetailsList().size() + detailStartingIndex;
		
	}
	
	protected void performSearchForPagingCriteria(int pagingIndex) throws OrderLicensesException {

		int pagingIdx = 0;
		boolean doPagingSearch = !(pagingIndex < 0);
		int detailStartingIndex = 0;
		if ( doPagingSearch || isUseWrappedResultsFromSession()) {
			OrderSearchResultWrapper[] priorResults = getOrderSearchResultsWrapperArrayInSession();
			if ( priorResults != null ) {
				setWrappedResultsArray(priorResults);
				setPagingDetails(new boolean[getWrappedResultsArray().length]);
				int pgDx = 0;
				for ( OrderSearchResultWrapper priorResult : getWrappedResultsArray()) {
					if ( priorResult != null ) {
						getPagingDetails()[pgDx] = priorResult.getSvcSearchResults().getTotalRows() > getDetailPagingThreshold();
						pgDx++;
						if (priorResult.isHasResults()) {
							this.setExpandFirstOrderDetail(true);
						}
					}
				}
			} else {
				doPagingSearch = false;
			}
		} 
		if ( !doPagingSearch ) {
			if (!isUseWrappedResultsFromSession()) {
				for ( OrderSearchCriteriaWrapper searchCriteria : getSearchCriteria() ) {
					detailStartingIndex = performSearchForOneCriteria ( searchCriteria, pagingIdx, detailStartingIndex);
					pagingIdx++;
				}
			}
		} else {
			for ( OrderSearchCriteriaWrapper searchCriteria : getSearchCriteria() ) {
				if ( pagingIdx == pagingIndex ) {
					detailStartingIndex = performSearchForOneCriteria ( searchCriteria, pagingIdx , detailStartingIndex);
				} else {
					getWrappedResultsArray()[pagingIdx] = getPagingOrderSearchResultsWrapperInSession(pagingIdx);
					if (!this.resizing) {
						detailStartingIndex = getWrappedResultsArray()[pagingIndex].reIndexDetails(detailStartingIndex);				
					}
				}
				pagingIdx++;
			}
		}
		this.storeUseWrappedResultsFromSession(Boolean.FALSE);

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
				setSearchInvoiceNumber(getSelectedInvoiceNumber().trim());
				setInvoiceView(true);
		}
				
	}
	protected void setExpandHeaderAndDetailFlags(int pagingIndex, String groupKey) {		
	
		if ( !getRePagingOrSizing()[pagingIndex] ) {
			boolean crMapKeyIsBundle = !groupKey.equals(INDIVIDUAL_GROUP_ID);
			
			setExpandFirstOrderDetail( getSelectedItem().equalsIgnoreCase(OrderManagementConstants.ORDER_DETAIL) );
			setExpandOrderHeader( getSelectedItem().equalsIgnoreCase(OrderManagementConstants.ORDER_HEADER) );

			if ( StringUtils.isNotEmpty( getSelectedItem() ) ) {
				if ( crMapKeyIsBundle ) {
					if ( pagingIndex < getExpandBundleHeader().length ) {
						getExpandFirstBundleDetail()[pagingIndex] = getSelectedItem().equalsIgnoreCase(OrderManagementConstants.BUNDLE_DETAIL);
						getExpandBundleHeader()[pagingIndex] = getExpandFirstBundleDetail()[pagingIndex]  
						       || getSelectedItem().equalsIgnoreCase(OrderManagementConstants.BUNDLE_HEADER)
						       || isExpandOrderHeader();
					} 
				}
			}
		}
	}
	
	protected OrderSearchResult makeSureDetailIsInCurrentResults( OrderSearchResult currentSearchResult, int pagingIndex, boolean checkOrder) throws OrderLicensesException {
		
		boolean detailIsPresent = false;
		if ( !getSearchCriteria()[pagingIndex].isPagingGroupKeyUsed() ) {
			return currentSearchResult;
		}
		if ( getSearchCriteria() == null || pagingIndex > getSearchCriteria().length ) {
			return currentSearchResult;
		}
		if ( getSearchBundleNumber() == null || !getSearchBundleNumber().equals( Long.valueOf( getSearchCriteria()[pagingIndex].getPagingGroupKey() ) ) ) {
			return currentSearchResult;
		}
		
		if ( checkOrder ) {
			if ( isExpandFirstOrderDetail() ) {
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
			
		} else {
	    
			if ( getExpandFirstBundleDetail()[pagingIndex] || isExpandFirstOrderDetail() ) {
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
		}
			
		OrderSearchResult searchResult = new OrderSearchResult();
		
		while (! detailIsPresent ) {
			
			int nextFromRow = getSearchCriteria()[pagingIndex].getDisplayToRow() + 1;
			int nextToRow = getSearchCriteria()[pagingIndex].getDisplayToRow() + getSearchCriteria()[pagingIndex].getPageControl().getPageSize();
			if ( nextToRow > currentSearchResult.getTotalRows() ) {
				nextToRow = currentSearchResult.getTotalRows();
			}
			getSearchCriteria()[pagingIndex].setDisplayFromRow(nextFromRow);
			getSearchCriteria()[pagingIndex].setDisplayToRow(nextToRow);
			getSearchCriteria()[pagingIndex].getPageControl().setPage( getSearchCriteria()[pagingIndex].getPageControl().getPage() + 1 );
			
			searchResult = OrderLicenseServices.searchOrderLicenses(getSearchCriteria()[pagingIndex].getServiceSearchCriteriaForSearch());
			getSearchCriteria()[pagingIndex].setLastServiceSearchCritera(searchResult.getOrderSearchCriteria());
			getSearchCriteria()[pagingIndex].setDisplayFromRow(getSearchCriteria()[pagingIndex].getPageControl().getPageSize() * ( getSearchCriteria()[pagingIndex].getPageControl().getPage() - 1 ) + 1 );
			getSearchCriteria()[pagingIndex].setDisplayToRow(getSearchCriteria()[pagingIndex].getPageControl().getPageSize() * getSearchCriteria()[pagingIndex].getPageControl().getPage());
			getSearchCriteria()[pagingIndex].getPageControl().setListSize(Long.valueOf(searchResult.getTotalRows()).intValue());
			getSearchCriteria()[pagingIndex].getPageControl().setTotalRecords(Long.valueOf(searchResult.getTotalRows()).intValue());
	
			for ( OrderLicense ol : searchResult.getOrderLicenses() ) {
				if ( ol.getID() == getSearchDetailNumber().longValue() ) {
					detailIsPresent = true;
					break;
				}
			}
			if ( !detailIsPresent && nextToRow == searchResult.getTotalRows() ) {
				if ( checkOrder ) {
					throw new IllegalArgumentException("Unable to find detail # " + getSearchDetailNumber().toString()+ " for individual details");					
				} else {
					throw new IllegalArgumentException("Unable to find detail # " + getSearchDetailNumber().toString()+ " for bundle # " + getSearchBundleNumber().toString() );
				}
			}
		}
		
		return searchResult;
		
	}
	protected OrderSearchResult makeSureDetailIsInCurrentIndividualResults(OrderSearchResult currentSearchResult, int pagingIndex) throws OrderLicensesException {
		
		return makeSureDetailIsInCurrentResults( currentSearchResult, pagingIndex, true);

	}
	
	protected OrderSearchResult makeSureDetailIsInCurrentBundleResults(OrderSearchResult currentSearchResult, int pagingIndex) throws OrderLicensesException {
		
		return makeSureDetailIsInCurrentResults( currentSearchResult, pagingIndex, false);

	}

	protected void setStartEndDisplayRows(OrderSearchCriteriaWrapper searchCriteria, int pagingIndex) {
		if ( !getPageControl()[pagingIndex].isPageSizeChanged() ) {
			searchCriteria.setDisplayFromRow(getPageControl()[pagingIndex].getPageSize() * ( getPageControl()[pagingIndex].getPage() - 1 ) + 1 );
			searchCriteria.setDisplayToRow(getPageControl()[pagingIndex].getPageSize() * getPageControl()[pagingIndex].getPage());
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

	protected void postProcessMessagesToBundle(ConfirmationWrapper order, List<ProcessMessage> prMessages, BundleWrapper bundleWrapper, EditBundle editBundle) {

		bundleWrapper.setProcessMessages(prMessages);
		bundleWrapper.setToBeSaved(true);
		bundleWrapper.setToBeReturned(true);
		bundleWrapper.setLastEdit(editBundle);			

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
	
	public static final String CUSTOM_PAGE_CONTROL_ARRAY_KEY = "CUSTOM-PAGE-CONTROL-ARRAY-KEY";

	protected CustomPageControl getPagingCustomPageControlInSession(int pagingIndex) {
		CustomPageControl[] pageControl = getCustomPageControlArrayInSession();
		if ( pageControl != null ) {
			if ( pagingIndex <= pageControl.length ) {
				return pageControl[pagingIndex];
			}
		}
		return new CustomPageControl(getDetailPagingThreshold());
	}
	
	protected CustomPageControl[] getCustomPageControlArrayInSession() {
		if ( getSession().containsKey(CUSTOM_PAGE_CONTROL_ARRAY_KEY)) {
			Object sessionObj = getSession().get(CUSTOM_PAGE_CONTROL_ARRAY_KEY);
			if ( sessionObj instanceof CustomPageControl[] ) {
				return (CustomPageControl[])getSession().get(CUSTOM_PAGE_CONTROL_ARRAY_KEY);
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	protected void storeCustomPageControlArrayInSession( CustomPageControl[] customPageControl) {
		if ( customPageControl != null ) {
			removeCustomPageControlArrayFromSession();
			getSession().put(CUSTOM_PAGE_CONTROL_ARRAY_KEY, customPageControl );
		}
	}
	
	protected void removeCustomPageControlArrayFromSession() {
		if ( getSession().containsKey(CUSTOM_PAGE_CONTROL_ARRAY_KEY)) {
			getSession().remove(CUSTOM_PAGE_CONTROL_ARRAY_KEY);
		}
	}

	public static final String VIEW_ORDER_SEARCH_CRITERIA_ARRAY_KEY = "VIEW-ORDER-SEARCH-CRITERIA-ARRAY--KEY";

	protected OrderSearchCriteriaWrapper getPagingOrderSearchCriteriaInSession(int pagingIndex) {
		OrderSearchCriteriaWrapper[] searchCriteria = getOrderSearchCriteriaArrayInSession();
		if ( searchCriteria != null ) {
			if ( pagingIndex <= searchCriteria.length ) {
				return searchCriteria[pagingIndex];
			}
		}
		return new OrderSearchCriteriaWrapper(true, true /* use default sort, for view */); 
	}
	
	protected OrderSearchCriteriaWrapper[] getOrderSearchCriteriaArrayInSession() {
		if ( getSession().containsKey(VIEW_ORDER_SEARCH_CRITERIA_ARRAY_KEY)) {
			Object sessionObj = getSession().get(VIEW_ORDER_SEARCH_CRITERIA_ARRAY_KEY);
			if ( sessionObj instanceof OrderSearchCriteriaWrapper[] ) {
				return (OrderSearchCriteriaWrapper[])getSession().get(VIEW_ORDER_SEARCH_CRITERIA_ARRAY_KEY);
			}
		}
		return null;
	}
		
	@SuppressWarnings("unchecked")
	protected void storeOrderSearchCriteriaArrayInSession( OrderSearchCriteriaWrapper[] orderSearchCriteria ) {
		if ( orderSearchCriteria != null ) {
			removeOrderSearchCriteriaArrayFromSession();
			getSession().put(VIEW_ORDER_SEARCH_CRITERIA_ARRAY_KEY, orderSearchCriteria );
		}
	}
	
	protected void removeOrderSearchCriteriaArrayFromSession() {
		if ( getSession().containsKey(VIEW_ORDER_SEARCH_CRITERIA_ARRAY_KEY)) {
			getSession().remove(VIEW_ORDER_SEARCH_CRITERIA_ARRAY_KEY);
		}
	}

	public List<LabelValueBean> getSearchProductList() {
		return searchProductList;
	}

	public void setSearchProductList(List<LabelValueBean> searchProductList) {
		this.searchProductList = searchProductList;
	}

	public String[] getNewPage() {
		return newPage;
	}

	public void setNewPage(String[] newPage) {
		this.newPage = newPage;
	}

	public String[] getNewPageSize() {
		return newPageSize;
	}

	public void setNewPageSize(String[] newPageSize) {
		this.newPageSize = newPageSize;
	}

	
	public CustomPageControl[] getPageControl() {
		if (pageControl == null || pageControl[0] == null) {
			CustomPageControl[] sessionPageControl = (CustomPageControl[])getSession().get(CUSTOM_PAGE_CONTROL_ARRAY_KEY);
			if (sessionPageControl != null) {
				setPageControl(sessionPageControl);
			}
		} 
		return pageControl;
	}

	public void setPageControl(CustomPageControl[] pageControl) {
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

	public boolean[] getExpandBundleHeader() {
		return expandBundleHeader;
	}

	public void setExpandBundleHeader(boolean[] expandBundleHeader) {
		this.expandBundleHeader = expandBundleHeader;
	}
		
	public boolean[] getExpandFirstBundleDetail() {
		return expandFirstBundleDetail;
	}

	public void setExpandFirstBundleDetail(boolean[] expandFirstBundleDetail) {
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
	
	public boolean[] getRePagingOrSizing() {
		return rePagingOrSizing;
	}
	public void setRePagingOrSizing(boolean[] rePagingOrSizing) {
		this.rePagingOrSizing = rePagingOrSizing;
	}

	public boolean getResizing() {
		return resizing;
	}
	public void setResizing(boolean resizing) {
		this.resizing = resizing;
	}
	public boolean[] getPagingDetails() {
		return pagingDetails;
	}

	public void setPagingDetails(boolean[] pagingDetails) {
		this.pagingDetails = pagingDetails;
	}

	public OrderSearchCriteriaWrapper[] getSearchCriteria() {
		return searchCriteria;
	}

	public void setSearchCriteria(OrderSearchCriteriaWrapper[] searchCriteria) {
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


	public String getPagingIndex() {
		return pagingIndex;
	}
	public void setPagingIndex(String pagingIndex) {
		this.pagingIndex = pagingIndex;
	}
	public String getRequestedPagingIndex() {
		return requestedPagingIndex;
	}
	public void setRequestedPagingIndex(String requestedPagingIndex) {
		this.requestedPagingIndex = requestedPagingIndex;
	}
	public String[] getNewSortBy() {
		return newSortBy;
	}
	public void setNewSortBy(String[] newSortBy) {
		this.newSortBy = newSortBy;
	}
	public String[] getNewSortOrder() {
		return newSortOrder;
	}
	public void setNewSortOrder(String[] newSortOrder) {
		this.newSortOrder = newSortOrder;
	}
	public boolean isHaveNonInvoicedDetails() {
		return haveNonInvoicedDetails;
	}
	public void setHaveNonInvoicedDetails(boolean haveNonInvoicedDetails) {
		this.haveNonInvoicedDetails = haveNonInvoicedDetails;
	}
	public boolean isHaveNonCanceledDetails() {
		return haveNonCanceledDetails;
	}
	public void setHaveNonCanceledDetails(boolean haveNonCanceledDetails) {
		this.haveNonCanceledDetails = haveNonCanceledDetails;
	}	
	public boolean isHaveNonRLDetails() {
		return haveNonRLDetails;
	}
	public void setHaveNonRLDetails(boolean haveNonRLDetails) {
		this.haveNonRLDetails = haveNonRLDetails;
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
	public boolean isHaveOpenDetails() {
		return haveOpenDetails;
	}
	public void setHaveOpenDetails(boolean haveOpenDetails) {
		this.haveOpenDetails = haveOpenDetails;
	}
	public boolean isHaveAdjustedDetails() {
		return haveAdjustedDetails;
	}
	public void setHaveAdjustedDetails(boolean haveAdjustedDetails) {
		this.haveAdjustedDetails = haveAdjustedDetails;
	}
	@Override
	public QuickTabSelect getDefaultQuickTabSelect() {
		return QuickTabSelect.ordermgmt_menu_order;
	}
	public void setInvoiceView(boolean invoiceView) {
		this.invoiceView = invoiceView;
	}
	public boolean isInvoiceView() {
		return invoiceView;
	}
	
	public List<LabelValueBean> getDurationList() 
	{		
		durationList = new ArrayList<LabelValueBean>();
		int d = 0;
		for (DurationEnum durationEnum : DurationEnum.values()) {	
			durationList.add(new LabelValueBean(durationEnum.getDesc(),Integer.toString(d)));
			d++;
		}
		
		return durationList;
	}	
	
	public List<LabelValueBean> getContentTypeList()
	{
		contentTypeList = new ArrayList<LabelValueBean>();
		
		contentTypeList.add(new LabelValueBean(TransactionConstants.CONTENT_FULL_ARTICLE_CHAPTER, RepublicationConstants.CONTENT_FULL_ARTICLE_CHAPTER));
		contentTypeList.add(new LabelValueBean(TransactionConstants.CONTENT_EXCERPT, RepublicationConstants.CONTENT_EXCERPT));
		contentTypeList.add(new LabelValueBean(TransactionConstants.CONTENT_QUOTATION,RepublicationConstants.CONTENT_QUOTATION));
		contentTypeList.add(new LabelValueBean(TransactionConstants.CONTENT_CHART, RepublicationConstants.CONTENT_CHART));
		contentTypeList.add(new LabelValueBean(TransactionConstants.CONTENT_FIGURE_DIAGRAM_TABLE, RepublicationConstants.CONTENT_FIGURE_DIAGRAM_TABLE));
		contentTypeList.add(new LabelValueBean(TransactionConstants.CONTENT_PHOTOGRAPH, RepublicationConstants.CONTENT_PHOTOGRAPH));
		contentTypeList.add(new LabelValueBean(TransactionConstants.CONTENT_CARTOONS, RepublicationConstants.CONTENT_CARTOONS));
		contentTypeList.add(new LabelValueBean(TransactionConstants.CONTENT_ILLUSTRATION, RepublicationConstants.CONTENT_ILLUSTRATION));
		contentTypeList.add(new LabelValueBean(TransactionConstants.CONTENT_GRAPH, RepublicationConstants.CONTENT_GRAPH));
		contentTypeList.add(new LabelValueBean(TransactionConstants.CONTENT_SELECTED_PAGES, RepublicationConstants.CONTENT_SELECTED_PAGES));
	
		return contentTypeList;

	}
	
	public List<LabelValueBean> getBusinessList()
	{
		businessList = new ArrayList<LabelValueBean>();
		
		businessList.add(new LabelValueBean("Yes", RepublicationConstants.BUSINESS_FOR_PROFIT));
		businessList.add(new LabelValueBean("No", RepublicationConstants.BUSINESS_NON_FOR_PROFIT));
	
		return businessList;

	}
	
	public List<LabelValueBean> getSubmitterAuthorList()
	{
		submitterAuthorList = new ArrayList<LabelValueBean>();
		
		submitterAuthorList.add(new LabelValueBean("Yes", "true"));
		submitterAuthorList.add(new LabelValueBean("No", "false"));
	
		return submitterAuthorList;

	}

}
