package com.copyright.ccc.web.actions.ordermgmt;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.util.LabelValueBean;

import com.copyright.ccc.business.data.OrderBundle;
import com.copyright.ccc.business.data.OrderLicense;
import com.copyright.ccc.business.data.OrderPurchase;
import com.copyright.ccc.business.data.OrderSearchCriteriaSortColumnEnum;
import com.copyright.ccc.business.data.OrderSearchResult;
import com.copyright.ccc.business.services.ProductEnum;
import com.copyright.ccc.business.services.ProductTouHelper;
import com.copyright.ccc.business.services.cart.PricingServices;
import com.copyright.ccc.business.services.order.OrderLicenseServices;
import com.copyright.ccc.business.services.order.OrderLicensesException;
import com.copyright.ccc.web.actions.ordermgmt.data.AddNewItem;
import com.copyright.ccc.web.actions.ordermgmt.data.ConfirmationWrapper;
import com.copyright.ccc.web.actions.ordermgmt.data.CustomPageControl;
import com.copyright.ccc.web.actions.ordermgmt.data.ItemWrapper;
import com.copyright.ccc.web.actions.ordermgmt.data.OrderSearchCriteriaWrapper;
import com.copyright.ccc.web.actions.ordermgmt.data.OrderSearchResultWrapper;
import com.copyright.ccc.web.actions.ordermgmt.data.OrderServicesHelper;

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

public class AddDetailAction extends OMBaseAction implements
		CustomPageControlAware {

	private static final long serialVersionUID = 1L;

	private boolean rePagingOrSizing = false;
	private boolean pagingDetails = false;
	private String newPage;
	private String newPageSize;

	private Long searchConfirmNumber;
	private Long searchBundleNumber;
	private Long searchDetailNumber;
	
	private int numAddedDetails;

	private OrderSearchCriteriaWrapper searchCriteria = new OrderSearchCriteriaWrapper(
			true, true /* use default sort, for view */);
	private ConfirmationWrapper selectedOrder;
	private OrderPurchase editOrder;
	private OrderBundle bundle;
	//private OrderLicense newItem;

	private List<LabelValueBean> acadProductList = ProductEnum.getAcademicProductCodes();   //EnumHelper.getProductsForAddDetail(); 
	private List<LabelValueBean> touListAPS = ProductTouHelper.getTousForProduct(ProductEnum.APS);
	private List<LabelValueBean> touListDPS = ProductTouHelper.getTousForProduct(ProductEnum.DPS);
	private List<LabelValueBean> touListTRS = ProductTouHelper.getTousForProduct(ProductEnum.TRS);
	private List<LabelValueBean> touListECCS = ProductTouHelper.getTousForProduct(ProductEnum.ECC);
	private List<LabelValueBean> touListRLS = ProductTouHelper.getTousForProduct(ProductEnum.RLS);
	private List<LabelValueBean> touListRL = ProductTouHelper.getTousForProduct(ProductEnum.RL);
	private List<LabelValueBean> touListRLR = ProductTouHelper.getTousForProduct(ProductEnum.RLR);

	private List<LabelValueBean> nonAcadProductList = ProductEnum.getNonAcademicProductCodes();
	private List<ItemWrapper> addedDetails = new ArrayList<ItemWrapper>();
	
	private Long lastAddedProductSourceKey;
	private Long lastAddedTou;
	
/*	public OrderLicense getNewItem() {
		return newItem;
	}
	public void setNewItem(OrderLicense newItem) {
		this.newItem = newItem;
	} */

	private ItemWrapper newDetail;
	private AddNewItem newItem;   //= new AddNewItem();

	private boolean addingDetail;
	private boolean addingProject;
	private boolean disableSelections;

	@Override
	public QuickTabSelect getDefaultQuickTabSelect() {
		return QuickTabSelect.ordermgmt_menu_order;
	}
	
	
	public List<LabelValueBean> getTouListAPS() {
		return touListAPS;
	}
	public List<LabelValueBean> getTouListDPS() {
		return touListDPS;
	}
	public List<LabelValueBean> getTouListTRS() {
		return touListTRS;
	}
	public List<LabelValueBean> getTouListECCS() {
		return touListECCS;
	}
	public List<LabelValueBean> getTouListRLS() {
		return touListRLS;
	}
	public List<LabelValueBean> getTouListRL() {
		return touListRL;
	}
	public List<LabelValueBean> getTouListRLR() {
		return touListRLR;
	}
	public List<LabelValueBean> getAcadProductList() {
		return acadProductList;
	}
	public void setAcadProductList(List<LabelValueBean> acadProductList) {
		this.acadProductList = acadProductList;
	}
	public List<LabelValueBean> getNonAcadProductList() {
		return nonAcadProductList;
	}
	public void setNonAcadProductList(List<LabelValueBean> nonAcadProductList) {
		this.nonAcadProductList = nonAcadProductList;
	}
	public Long getLastAddedProductSourceKey() {
		return lastAddedProductSourceKey;
	}
	public void setLastAddedProductSourceKey(Long lastAddedProductSourceKey) {
		this.lastAddedProductSourceKey = lastAddedProductSourceKey;
	}
	public Long getLastAddedTou() {
		return lastAddedTou;
	}
	public void setLastAddedTou(Long lastAddedTou) {
		this.lastAddedTou = lastAddedTou;
	}
	public AddNewItem getNewItem() {
		return newItem;
	}
	public void setNewItem(AddNewItem detailToAdd) {
		this.newItem = detailToAdd;
	}
	
	public boolean getShowBundleInfo() {
		return (bundle != null);
	}
	
	public Long getSearchConfirmNumber() {
		return searchConfirmNumber;
	}
	public void setSearchConfirmNumber(Long searchConfirmNumber) {
		this.searchConfirmNumber = searchConfirmNumber;
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
	
	public OrderBundle getBundle() {
		return bundle;
	}

	public void setBundle(OrderBundle bundle) {
		this.bundle = bundle;
	}

	public int getNumAddedDetails() {
		return numAddedDetails;
	}

	public void setNumAddedDetails(int numAddedDetails) {
		this.numAddedDetails = numAddedDetails;
	}

	public ItemWrapper getNewDetail() {
		return newDetail;
	}

	public void setNewDetail(ItemWrapper newDetail) {
		this.newDetail = newDetail;
	}

	public List<ItemWrapper> getAddedDetails() {
		return addedDetails;
	}

	public boolean isDisableSelections() {
		return disableSelections;
	}

	public void setDisableSelections(boolean disableSelections) {
		this.disableSelections = disableSelections;
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

	
	 public boolean isAddingProject() { return addingProject; }
	 
	 public void setAddingProject(boolean addingProject) 
	 	{ this.addingProject= addingProject; }

	public OrderPurchase getEditOrder() {
		return editOrder;
	}

	public void setEditOrder(OrderPurchase editOrder) {
		this.editOrder = editOrder;
	}

	public ConfirmationWrapper getSelectedOrder() {
		return selectedOrder;
	}

	/*
	 * public ConfirmationWrapper getSelectedOrderTemp() { //ConfirmationWrapper
	 * order = new ConfirmationWrapper(); OrderSearchResult searchResult = new
	 * OrderSearchResult(); try { searchCriteria.setConfNumber("2319459");
	 * searchResult =
	 * OrderLicenseServices.searchOrderLicensesWithUser(searchCriteria
	 * .getServiceSearchCriteria()); OrderSearchResultWrapper wrappedResults =
	 * new OrderSearchResultWrapper( searchResult , true /* Hierarchical order
	 */// );
	/*
	 * selectedOrder = wrappedResults.getOrderListInHierarchicalOrder().get(0);
	 * editOrder = selectedOrder.getConfirmation(); return order; } catch
	 * (Exception ex) { return null; } }
	 */

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
		// this.includeBundles = false;
		// if ( pagingDetails ) {
		// this.includeBundles = true;
		// }
		this.pagingDetails = pagingDetails;
	}

	public String rePageResults() {
		int nPage = -1;
		if (StringUtils.isNotEmpty(getNewPage())) {
			if (StringUtils.isNumeric(getNewPage().trim())) {
				nPage = Integer.valueOf(getNewPage().trim()).intValue();
			}
		}
		pageControl = getCustomPageControlInSession();
		if (nPage > 0) {
			pageControl.setPage(nPage);
		}
		searchCriteria = super
				.getOrderSearchCriteriaInSession(OMBaseAction.VIEW_ORDER_SEARCH_CRITERIA_KEY);
		setSearching(true);
		setResorting(false);
		setRePagingOrSizing(true);

		execute();

		return SUCCESS;

	}

	public String reSizePageResults() {

		int nPageSize = -1;
		if (StringUtils.isNotEmpty(getNewPageSize())) {
			if (StringUtils.isNumeric(getNewPageSize().trim())) {
				nPageSize = Integer.valueOf(getNewPageSize().trim()).intValue();
			}
		}
		pageControl = getCustomPageControlInSession();
		if (nPageSize > 0) {
			pageControl.setPageSize(nPageSize);
			pageControl.setPage(pageControl
					.getPageAfterHandlingPageSizeChange());
			pageControl.setLastPageSize(nPageSize);
		}
		searchCriteria = super
				.getOrderSearchCriteriaInSession(OMBaseAction.VIEW_ORDER_SEARCH_CRITERIA_KEY);
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
	public String execute() {
		super.handleQuickTabSelected();
		
		if ( StringUtils.isNotEmpty(getSelectedConfirmNumber() ) ) {
			if ( StringUtils.isNumeric(getSelectedConfirmNumber()) ) {
				setSearchConfirmNumber(Long.valueOf(getSelectedConfirmNumber()));
			}
		}
		
		//setSearchBundleNumber(null);
		
		if ( StringUtils.isNotEmpty(getSelectedBundleNumber() ) ) {
			if ( StringUtils.isNumeric(getSelectedBundleNumber()) ) {
				setSearchBundleNumber(Long.valueOf(getSelectedBundleNumber()));
			}
		}
		
		if ( StringUtils.isNotEmpty(getSelectedDetailNumber() ) ) {
			if ( StringUtils.isNumeric(getSelectedDetailNumber()) ) {
				setSearchDetailNumber(Long.valueOf(getSelectedDetailNumber()));
			}
		}

		addedDetails = getAddedDetailsInSession();
		Long lastProd = null;
		if (addedDetails != null && addedDetails.size() >0) {
			for (int i = addedDetails.size()-1; i >= 0; i--) {
				lastProd = addedDetails.get(i).getItem().getProductSourceKey();
				
				if (getShowBundleInfo() && ProductEnum.getEnumForProductSourceKey(lastProd).getIsAcademic() ||
					(!getShowBundleInfo() && !ProductEnum.getEnumForProductSourceKey(lastProd).getIsAcademic())) {
						lastAddedProductSourceKey = lastProd;
				}
			}
		}
		if (lastProd == null) {
			//search all details of this conf number to find last added detail of the right acad. vs. non-acad. type 
			OrderSearchCriteriaWrapper sc = new OrderSearchCriteriaWrapper();
			sc.setConfNumber(Long.valueOf(getSearchConfirmNumber()).toString());
			sc.setSortCriteriaBy(OrderSearchCriteriaSortColumnEnum.SORT_BY_ORDER_DETAIL_ID.name());
			sc.setSortOrder(OrderSearchCriteriaSortColumnEnum.DESCENDING_SORTORDER.name());
			sc.setResultsPerPage(1);
			
			
			OrderSearchResult svcSearchResult = null;
			//OrderSearchResultWrapper searchResults = new OrderSearchResultWrapper(
			//		svcSearchResult);
			try {
				svcSearchResult = OrderLicenseServices
						.searchOrderLicenses(sc.getServiceSearchCriteriaForSearch());
				if (svcSearchResult != null) {
					if (svcSearchResult.getOrderLicenses() != null
							&& !svcSearchResult.getOrderLicenses().isEmpty()) {
						OrderSearchResultWrapper sr= new OrderSearchResultWrapper(
								svcSearchResult);
						//getSearchCriteria().setLastServiceSearchCritera(svcSearchResult.getOrderSearchCriteria());
						ItemWrapper det = sr.getDetailsList().get(0);
						if (det != null)
							lastProd = det.getItem().getProductSourceKey();
					}
				}

			} catch (OrderLicensesException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		}
		
		if (lastProd != null) {
			String prodName = ProductEnum.getEnumForProductSourceKey(lastProd).getProductName();
			//String prodName = ProductEnum.getEnumForProductSourceKey(lastProd).getProductName();
			//newItem.setProduct(lastProd);
		}
			
		
			
		searchCriteria.setConfNumber(Long.valueOf(getSearchConfirmNumber()).toString());
		searchCriteria
				.setSortCriteriaBy(OrderSearchCriteriaSortColumnEnum.SORT_BY_ORDER_DATE
						.name());
		searchCriteria
				.setSortOrder(OrderSearchCriteriaSortColumnEnum.DESCENDING_SORTORDER
						.name());
		searchCriteria.setProductTOU(null);
		if (numAddedDetails > 0) {
			searchCriteria.setResultsPerPage(numAddedDetails);
		} else {
			searchCriteria.setResultsPerPage(1);
		}

		OrderSearchResultWrapper osrw = searchForAddedDetails(searchCriteria);
		List<ItemWrapper> res = osrw.getDetailsList();
		selectedOrder = null;
		bundle = null;
		if (res != null && res.size() > 0) {
			selectedOrder = res.get(0).getMyConfirmation();
			// TODO: once there can be >1 bundle in order, change this!!
			if (res.get(0).getMyBundle()!= null)
				bundle = res.get(0).getMyBundle().getBundle();
			
			//try {
			//	newItem = OrderLicenseServices.initCoiLicense(selectedOrder.getConfirmation().getOrderHeaderId());
			//}
			//catch (Exception e) {
			//	addActionError("Couldn't init OrderLicense: "+ e);
			//}
			
		}
		if (addingDetail) {
			//AddNewItem newItem = new AddNewItem();
			// make a call to service to add detail

			// add detail to the displayed list
			// newDetail = new ItemWrapper();
			//populateNewDetailFromUI();
			try {
				Long header = selectedOrder.getConfirmation().getOrderHeaderId();
				OrderLicense ol = OrderLicenseServices.initCoiLicense(header);
				populateNewDetailFromUI(ol);
				
				//price the detail
				ol = PricingServices.updateItemPrice(ol); 
				OrderLicense ol2 = OrderLicenseServices.createCoiLicense(ol);
				addedDetails.add(new ItemWrapper(ol2));
				storeAddedDetailsInSession(addedDetails);
			}
			catch (Exception e) {
				addActionError("Couldn't create OrderLicense: "+ e);
			}
			
			//numAddedDetails = numAddedDetails + 1;
			// if we get the added detail back from the service, add it to list
			// addedDetails.add(newDetail);
		} else {

		}
		//if (numAddedDetails > 0) {
			// if when detail is added, you don't get the object back and can't
			// add it to list,
			// then search for details
			//addedDetails = osrw.getDetailsList();

		//}

		return SUCCESS;
	}

	private OrderSearchResultWrapper searchForAddedDetails(
			OrderSearchCriteriaWrapper osc) {
		OrderSearchResult svcSearchResult = null;
		OrderSearchResultWrapper searchResults = new OrderSearchResultWrapper(
				svcSearchResult);
		try {
			svcSearchResult = OrderLicenseServices
					.searchOrderLicenses(searchCriteria
							.getServiceSearchCriteriaForSearch());
			if (svcSearchResult != null) {
				if (svcSearchResult.getOrderLicenses() != null
						&& !svcSearchResult.getOrderLicenses().isEmpty()) {
					searchResults = new OrderSearchResultWrapper(
							svcSearchResult);
					getSearchCriteria().setLastServiceSearchCritera(svcSearchResult.getOrderSearchCriteria());

				}
			}

		} catch (OrderLicensesException e) {
			addActionError("A system error has prevented retrieving the requested confirmation");
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		// addedDetails = searchResults.getDetailsList();
		return searchResults;

	}

	public void populateNewDetailFromUI(OrderLicense lic) {
		if (getShowBundleInfo()) {
			lic.setBundleId(bundle.getBundleId());
		}

		//lic.setTouName()
		
		lic.setPublicationTitle( newItem.getPublicationTitle() );
		if ( OrderServicesHelper.trimmedLongValue( newItem.getWrWorkInst()) != null ) {
			lic.setWorkInst( OrderServicesHelper.trimmedLongValue(newItem.getWrWorkInst() ) );
		}
		if ( OrderServicesHelper.trimmedLongValue( newItem.getWorkInst()) != null ) {
			lic.setWorkInst( OrderServicesHelper.trimmedLongValue(newItem.getWorkInst() ) );
		}
		lic.setItemSubDescription( newItem.getItemSubDescription() );
		lic.setIdnoLabel( newItem.getIdnoLabel() );
		lic.setStandardNumber( newItem.getStandardNumber() );
		lic.setPublisher( newItem.getPublisher() );				

		//TODO: set other fields!	
	}
	
	@Override
	public void validate() {

		if (addingDetail)
		{			
			super.validate();	
		}
	}

}
