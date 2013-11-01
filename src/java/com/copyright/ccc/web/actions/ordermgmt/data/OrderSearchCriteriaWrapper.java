package com.copyright.ccc.web.actions.ordermgmt.data;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.copyright.ccc.business.data.OrderSearchCriteria;
import com.copyright.ccc.business.data.OrderSearchCriteriaSortColumnEnum;
import com.copyright.ccc.business.data.OrderSearchCriteriaSortDirectionEnum;
import com.copyright.ccc.business.data.OrderSearchRowNumbers;
import com.copyright.ccc.business.services.OrderDataSourceEnum;
import com.copyright.ccc.web.actions.ordermgmt.util.MiscUtils;

public class OrderSearchCriteriaWrapper implements Serializable
{

	    private static final long serialVersionUID = 1L;
	    public static final int DEFAULT_RESULTS_PER_PAGE = 25;	    
	    
	    private CustomPageControl pageControl = new CustomPageControl(OrderSearchCriteriaWrapper.DEFAULT_RESULTS_PER_PAGE);
	    private OrderSearchCriteria serviceSearchCritera;	    
	    private OrderSearchCriteria lastServiceSearchCritera;

	    private String pagingGroupKey;
	    private boolean pagingGroupKeyUsed = true;
		
		private String newPage;
		private String newPageSize;
	    
	    private boolean viewSort = false;
	 
	    private int resultsPerPage = DEFAULT_RESULTS_PER_PAGE;
	    private int displayFromRow;
	    private int displayToRow;
	    
	    private String confNumber;
	    private String bundleNumber;
	    private String orderDetailNumber;
	    private String rlJobTicket;
	    private String invoiceNumber;
	    private String orderNumber;	    
	    private String permissionStatus; //maybe should be int/enum
	    private String productTOU; //maybe should be int/enum
	    private String acctNumber;
	    private String acctName;
	    private Date startDate;
	    private Date endDate;
	    private String dateType; //maybe should be int/enum
	    private String rhRefNumber;
	    private String rlLicenseNumber;
    
		private String last4digits_adv;
		private String tfWorkID_adv;
	    private String wrWorkID_adv;
	    private String institution_adv;
	    private String republPublisher_adv;
	    private String republWork_adv;	    
		private String courseName_adv;
	    private String publicationName_adv;	    
	    private String standardNumber_adv;
	    private String rightsholderName_adv;
	    private String internetLogin_adv;
	    //private OrderSource orderSource_adv;
	    private String orderSource_adv;
	    private String orderStatus_adv;
	    private String lastUpdatedBy_adv;
	    private String refData_adv;
	    private String refType_adv; //maybe should be int/enum
	    private String rhAccountNumber_adv;
	    //private String orderSourceString_adv;

	    private String sortCriteriaBy = OrderSearchCriteriaSortColumnEnum.SORT_BY_DEFAULT.name();
	    private String sortOrder = OrderSearchCriteriaSortDirectionEnum.DESC.getUserText();  

		private boolean showCancelled = true;
	    private boolean showCourseInfo = false; 
	    private boolean showTfOrders = true;
	    private boolean showCoiOrders = true;
	    
	    private boolean includeExpandedResults = false;
	    private boolean includeOnlyBundledItems = false;
	    private boolean includeOnlyUnBundledItems = false;
	    
	    private boolean includeOnlyInvoicedItems = false; // for adjustments
	    private boolean includeOnlyAdjustableItems = false; // for adjustments

	    
		public boolean isViewSort() {
			return viewSort;
		}

		public void setViewSort(boolean viewSort) {
			this.viewSort = viewSort;
		}

		public OrderSearchCriteriaWrapper() {
	    	super();
	    }
	    
	    public OrderSearchCriteriaWrapper(boolean setDefautSort) {
	    	super();
	    	setSortCriteriaDefault();
	    }
	    public OrderSearchCriteriaWrapper(boolean setDefautSort, boolean viewSort) {
	    	super();
	    	this.viewSort = viewSort;
	    	setSortCriteriaDefault();
	    }
	    
	    public static OrderSearchCriteriaWrapper copy( OrderSearchCriteriaWrapper copyWrapper, boolean setDefautSort, boolean viewSort) {
	    	OrderSearchCriteriaWrapper newWrapper = new OrderSearchCriteriaWrapper(setDefautSort,viewSort);
	    	newWrapper.setAcctName( copyWrapper.getAcctName() );
	    	newWrapper.setAcctNumber( copyWrapper.getAcctNumber() );
	    	newWrapper.setBundleNumber( copyWrapper.getBundleNumber() );
	    	newWrapper.setConfNumber( copyWrapper.getConfNumber() );
	    	newWrapper.setCourseName_adv( copyWrapper.getCourseName_adv() );
	    	newWrapper.setDateType( copyWrapper.getDateType() );
	    	newWrapper.setDisplayFromRow( copyWrapper.getDisplayFromRow() );
	    	newWrapper.setDisplayToRow( copyWrapper.getDisplayToRow() );
	    	newWrapper.setEndDate( copyWrapper.getEndDate() );
	    	newWrapper.setIncludeExpandedResults( copyWrapper.isIncludeExpandedResults() );
	    	newWrapper.setInstitution_adv( copyWrapper.getInstitution_adv() );
	    	newWrapper.setInternetLogin_adv( copyWrapper.getInternetLogin_adv() );
	    	newWrapper.setInvoiceNumber( copyWrapper.getInvoiceNumber() );
	    	newWrapper.setLast4digits_adv( copyWrapper.getLast4digits_adv() );
	    	newWrapper.setLastServiceSearchCritera( copyWrapper.getLastServiceSearchCritera() );
	    	newWrapper.setLastUpdatedBy_adv( copyWrapper.getLastUpdatedBy_adv() );
	    	newWrapper.setNewPage( copyWrapper.getNewPage() );
	    	newWrapper.setNewPageSize( copyWrapper.getNewPageSize() );
	    	newWrapper.setOrderDetailNumber( copyWrapper.getOrderDetailNumber() );
	    	newWrapper.setOrderNumber( copyWrapper.getOrderNumber() );
	    	newWrapper.setOrderSource_adv( copyWrapper.getOrderSource_adv() );
	    	newWrapper.setOrderStatus_adv( copyWrapper.getOrderStatus_adv() );
	    	newWrapper.setPageControl( copyWrapper.getPageControl() );
	    	newWrapper.setPermissionStatus( copyWrapper.getPermissionStatus() );
	    	newWrapper.setProductTOU( copyWrapper.getProductTOU() );
	    	newWrapper.setPublicationName_adv( copyWrapper.getPublicationName_adv() );
	    	newWrapper.setRefData_adv( copyWrapper.getRefData_adv() );
	    	newWrapper.setRefType_adv( copyWrapper.getRefType_adv() );
	    	newWrapper.setRepublPublisher_adv( copyWrapper.getRepublPublisher_adv() );
	    	newWrapper.setRepublWork_adv( copyWrapper.getRepublWork_adv() );
	    	newWrapper.setResultsPerPage( copyWrapper.getResultsPerPage() );
	    	newWrapper.setRhAccountNumber_adv( copyWrapper.getRhAccountNumber_adv() );
	    	newWrapper.setRhRefNumber( copyWrapper.getRhRefNumber() );
	    	newWrapper.setRightsholderName_adv( copyWrapper.getRightsholderName_adv() );
	    	newWrapper.setRlJobTicket( copyWrapper.getRlJobTicket() );
	    	newWrapper.setRlLicenseNumber( copyWrapper.getRlLicenseNumber() );
	    	newWrapper.setServiceSearchCritera( copyWrapper.getServiceSearchCritera() );
	    	newWrapper.setShowCancelled( copyWrapper.getShowCancelled() );
	    	newWrapper.setShowCoiOrders( copyWrapper.isShowCoiOrders() );
	    	newWrapper.setShowCourseInfo( copyWrapper.getShowCourseInfo() );
	    	newWrapper.setShowTfOrders( copyWrapper.isShowTfOrders() );
	    	newWrapper.setSortCriteriaBy( copyWrapper.getSortCriteriaBy() );
	    	newWrapper.setSortOrder( copyWrapper.getSortOrder() );
	    	newWrapper.setStandardNumber_adv( copyWrapper.getStandardNumber_adv() );
	    	newWrapper.setStartDate( copyWrapper.getStartDate() );
	    	newWrapper.setTfWorkID_adv( copyWrapper.getTfWorkID_adv() );
	    	newWrapper.setViewSort( copyWrapper.isViewSort() );
	    	newWrapper.setWrWorkID_adv( copyWrapper.getWrWorkID_adv() );
	    	newWrapper.setIncludeOnlyBundledItems( copyWrapper.isIncludeOnlyBundledItems() );
	    	newWrapper.setIncludeOnlyUnBundledItems( copyWrapper.isIncludeOnlyUnBundledItems() );
	    	newWrapper.setIncludeOnlyInvoicedItems( copyWrapper.isIncludeOnlyInvoicedItems() );
	    	newWrapper.setIncludeOnlyAdjustableItems( copyWrapper.isIncludeOnlyAdjustableItems() );
	    	return newWrapper;
	    }

	    public OrderSearchCriteria getServiceSearchCriteriaForSearch() {

	    	serviceSearchCritera = new OrderSearchCriteria();

	    	//Hard coding to show adjusted values
	    	serviceSearchCritera.setShowAdjustedValues(true);
	    	
	    	//setting criteria of type String
	    	serviceSearchCritera.setLicenseeName( checkedTrimmedValueOf(this.getAcctName()) );
	    	serviceSearchCritera.setLicenseeAccount( checkedTrimmedValueOf(this.getAcctNumber()));
	    	serviceSearchCritera.setInvoiceNumber( checkedTrimmedValueOf(this.getInvoiceNumber()) );
	    	serviceSearchCritera.setRhRefNum( checkedTrimmedValueOf(this.getRhRefNumber()) );
	    	serviceSearchCritera.setExternalOrderId( checkedTrimmedValueOf(this.getOrderNumber()) );
	    	serviceSearchCritera.setBundleName( checkedTrimmedValueOf(this.getCourseName_adv()) );
	    	serviceSearchCritera.setOrganizationName( checkedTrimmedValueOf(this.getInstitution_adv()) );
	    	serviceSearchCritera.setRightsholderName( checkedTrimmedValueOf(this.getRightsholderName_adv()) );
	    	serviceSearchCritera.setItemDescription( checkedTrimmedValueOf(this.getPublicationName_adv()) );
	    	serviceSearchCritera.setStandardNumber( checkedTrimmedValueOf(this.getStandardNumber_adv()) );
	    	serviceSearchCritera.setItemUpdUser( checkedTrimmedValueOf(this.getLastUpdatedBy_adv()) );
	    	serviceSearchCritera.setInternetLogin( checkedTrimmedValueOf(this.getInternetLogin_adv()) );
	    	serviceSearchCritera.setRepubPublisher( checkedTrimmedValueOf(this.getRepublPublisher_adv()));
	    	serviceSearchCritera.setRepubTitle( checkedTrimmedValueOf(this.getRepublWork_adv()));
	    	serviceSearchCritera.setRhAccountNumber(checkedTrimmedValueOf(this.getRhAccountNumber_adv()));
	
	    	
	    	//setting criteria of type Long	    	
	    	//since if the user doesn't set a criteria, it will be null in UI and it should be null in the service object -- 
	    	//there's no harm in setting on the service object smth that wasn't filled in by user in UI
	    	serviceSearchCritera.setConfirmNumber(checkedLongValueOf(this.getConfNumber())); 
	    	serviceSearchCritera.setItemId(checkedLongValueOf(this.getOrderDetailNumber())); 
	    	serviceSearchCritera.setJobTicketNumber(checkedLongValueOf(this.getRlJobTicket())); 	    	
	    	serviceSearchCritera.setLastFourCc(checkedLongValueOf(this.getLast4digits_adv()));		    	
	    	serviceSearchCritera.setExternalItemId(checkedLongValueOf(this.getTfWorkID_adv()));	
	    	serviceSearchCritera.setItemSourceKey(checkedLongValueOf(this.getWrWorkID_adv())); 
	    	serviceSearchCritera.setExternalDetailId(checkedLongValueOf(this.getRlLicenseNumber()));
	    	//serviceSearchCritera.setOrderHeaderId(checkedLongValueOf(this.getOrderNumber())); was supposed to replace
	    	//setExternalOrderId(), but decided to keep that one for now
	    	
	    	// setting criteria from dropdowns
	    	//TODO: figure out values (code strings as opposed to user-facing strings) and set
	    	serviceSearchCritera.setItemAvailabilityCd(checkedTrimmedValueOf(this.getPermissionStatus()));
	    	serviceSearchCritera.setProductCode(checkedTrimmedValueOf(this.getProductTOU()));
	    	//serviceSearchCritera.setTouName(this.getProductTOU());  
	    	serviceSearchCritera.setOrderSourceString( checkedTrimmedValueOf(this.getOrderSource_adv()));
	    	
	    	if (StringUtils.isNotBlank( this.getRefType_adv())) {
		    	if (this.getRefType_adv().equals(OrderSearchCriteriaRefNumSelectEnum.ACCT_REF.name()))
		    		serviceSearchCritera.setLcnAcctRefNum(this.getRefData_adv());
		    	if (this.getRefType_adv().equals(OrderSearchCriteriaRefNumSelectEnum.DOC_REF.name()))
		    		serviceSearchCritera.setLcnHdrRefNum(this.getRefData_adv());
		    	//if (this.getRefData_adv().equals(OrderSearchCriteriaRefNumSelectEnum.ORDER_REF))
		    	//	serviceSearchCritera.setLcnHdrRefNum(this.getRefData_adv());
		    	if (this.getRefType_adv().equals(OrderSearchCriteriaRefNumSelectEnum.ORDER_DET_REF.name()))
		    		serviceSearchCritera.setLicenseeRefNum(this.getRefData_adv());
		    	if (this.getRefType_adv().equals(OrderSearchCriteriaRefNumSelectEnum.PO_NUM.name()))
		    		serviceSearchCritera.setPoNum(this.getRefData_adv());
	    	}
	    	
	    	
	    	if (StringUtils.isNotBlank( this.getDateType())) {
		    	if (OrderSearchCriteriaDateSelectEnum.getSearchDateEnumFor(this.getDateType()).equals(OrderSearchCriteriaDateSelectEnum.ORDER_DATE)) {
		    		serviceSearchCritera.setOrderBegDate(this.getStartDate());
		    		serviceSearchCritera.setOrderEndDate(this.getEndDate());
		    	}
		    	if (OrderSearchCriteriaDateSelectEnum.getSearchDateEnumFor(this.getDateType()).equals(OrderSearchCriteriaDateSelectEnum.START_OF_TERM)) {
		    		serviceSearchCritera.setUseBegDate(this.getStartDate());
		    		serviceSearchCritera.setUseEndDate(this.getEndDate());
		    	}
		    	if (OrderSearchCriteriaDateSelectEnum.getSearchDateEnumFor(this.getDateType()).equals(OrderSearchCriteriaDateSelectEnum.INVOICE_DATE)) {
		    		serviceSearchCritera.setInvoiceBegDate(this.getStartDate());
		    		serviceSearchCritera.setInvoiceEndDate(this.getEndDate());
		    	}
		    	if (OrderSearchCriteriaDateSelectEnum.getSearchDateEnumFor(this.getDateType()).equals(OrderSearchCriteriaDateSelectEnum.LAST_UPDATE)) {
		    		serviceSearchCritera.setUpdateBegDate(this.getStartDate());
		    		serviceSearchCritera.setUpdateEndDate(this.getEndDate());
		    	}
	    	}

	    	if ( this.getDisplayFromRow() > 0 ) {
	    		serviceSearchCritera.setSearchFromRow( this.getDisplayFromRow() );
	    	} else {
	    		serviceSearchCritera.setSearchFromRow( 1 );
	    	}
	    	
	    	//scr.setItemId( this.getItemId() );
	    	if ( this.getDisplayToRow() > 0 ) {
	    		serviceSearchCritera.setSearchToRow(this.getDisplayToRow());
	    	} else {
	    		serviceSearchCritera.setSearchToRow(this.getPageControl().getPageSize());
	    	}
	    	
	    	if ( isShowTfOrders() && isShowCoiOrders() ) {
	    		serviceSearchCritera.setDataSource(OrderDataSourceEnum.ALL.name());
	    	} else if ( isShowTfOrders() ) {
	    		serviceSearchCritera.setDataSource(OrderDataSourceEnum.TF.name());
	    	} else if ( isShowCoiOrders() ) {
	    		serviceSearchCritera.setDataSource(OrderDataSourceEnum.OMS.name());
	    	}

	    	serviceSearchCritera.setIncludeCancelledItems(getShowCancelled());	    		
	    	serviceSearchCritera.setIncludeOnlyBundledItems(isIncludeOnlyBundledItems());	    		
	    	serviceSearchCritera.setIncludeOnlyUnBundledItems(isIncludeOnlyUnBundledItems());
	    	if (isIncludeOnlyInvoicedItems() == true) {	    	
	    		// isIncludeOnlyInvoicedItems returns all COI items which have an invoice number.
	    		// It does not go after the billing status. So, if the item was adjusted after
	    		// invoiced, it will show up search. Cant help. We will filter that in the UI for now.
	    		serviceSearchCritera.setIsInvoiced(isIncludeOnlyInvoicedItems());
	    		//BillingStatus works fine with COI. But no TF items show up!
	    		//serviceSearchCritera.setBillingStatus(ItemConstants.BILLING_STATUS_INVOICED_CD);
	    		// TF included cancelled in Invoiced. So filter that out.
	    		serviceSearchCritera.setIncludeCancelledItems(false);
	    	} else {
	    		serviceSearchCritera.setIsInvoiced(null);	    		
	    	}
	    	
	    	//for adjustments
	    	serviceSearchCritera.setIncludeOnlyAdjustableItems(isIncludeOnlyAdjustableItems());
	    		
	    	
	    	OrderSearchCriteriaSortColumnEnum oe = OrderSearchCriteriaSortColumnEnum.getEnumForName(this.getSortCriteriaBy());

	    	if ( oe != null ) {
	    		serviceSearchCritera.setSortByColumn(oe);
	    		if ( oe.equals(OrderSearchCriteriaSortColumnEnum.SORT_BY_DEFAULT) ) {
	    			// default is always descending
		    		serviceSearchCritera.setSortDirection(OrderSearchCriteriaSortDirectionEnum.DESC);
	    		} else if (oe.equals(OrderSearchCriteriaSortColumnEnum.SORT_BY_VIEW_DEFAULT) ) {
	    			// view default is always ascending
	    			serviceSearchCritera.setSortDirection(OrderSearchCriteriaSortDirectionEnum.ASC);
	    		} else {
	    	    	OrderSearchCriteriaSortDirectionEnum sd = OrderSearchCriteriaSortDirectionEnum.getSortDirectionEnumFor(getSortOrder());
			    	if ( sd != null ) {
			    		serviceSearchCritera.setSortDirection(sd);
			    	} else {
			    		serviceSearchCritera.setSortDirection(OrderSearchCriteriaSortDirectionEnum.ASC);
			    	}
	    			
	    		}
	    	} else {
	    		// set to default sort 
	    		if ( isViewSort() ) {
		    		serviceSearchCritera.setSortByColumn(OrderSearchCriteriaSortColumnEnum.SORT_BY_VIEW_DEFAULT);
		    		serviceSearchCritera.setSortDirection(OrderSearchCriteriaSortDirectionEnum.ASC);
	    		} else {
	    			serviceSearchCritera.setSortByColumn(OrderSearchCriteriaSortColumnEnum.SORT_BY_DEFAULT);
	    			serviceSearchCritera.setSortDirection(OrderSearchCriteriaSortDirectionEnum.DESC);
	    		}
	    	}
	    	
	    	serviceSearchCritera.addSortQualifierListForSortColumnAndDirection(serviceSearchCritera.getSortByColumn(), serviceSearchCritera.getSortDirection());

	    	//if ( isCriteriaChanged() ) {
	    		serviceSearchCritera.setOrderSearchRowNumbers(new OrderSearchRowNumbers());
	    	//} else {
	    	//	serviceSearchCritera.setOrderSearchRowNumbers(getLastServiceSearchCritera().getOrderSearchRowNumbers());
	    	//}

	    	return serviceSearchCritera;
	    }
	    private Long checkedLongValueOf( String str ) {
	    	if ( StringUtils.isNotEmpty(str) ) {
	    		String num = str.trim();
	    		if ( StringUtils.isNumeric(num) ) {
	    			return Long.valueOf(num);
	    		}
	    	}
	    	return null;
	    }
	    
	    private String checkedTrimmedValueOf( String str ) {
	    	if ( StringUtils.isNotEmpty(str) ) {
	    		if ( StringUtils.isNotEmpty( str.trim()) ) {
	    			//return OrderServicesHelper.doubleAnyEmbeddedQuotes(str.trim());
	    			//turn's out there's no need to double single quotes 
	    			return str.trim();
	    		}
	    	}
	    	return null;
	    }

	    private Integer checkedIntegerValueOf( String str ) {
	    	if ( StringUtils.isNotEmpty(str) ) {
	    		String num = str.trim();
	    		if ( StringUtils.isNumeric(num) ) {
	    			return Integer.valueOf(num);
	    		}
	    	}
	    	return null;
	    }

	    	
	/*	public String getOrderSourceString_adv() {
			return orderSourceString_adv;
		}

		public void setOrderSourceString_adv(String orderSourceString_adv) {
			this.orderSourceString_adv = orderSourceString_adv;
		}
*/		
	    public String getSortCriteriaBy() {
			return sortCriteriaBy;
		}

		public void setSortCriteriaBy(String sortCriteriaBy) {
			this.sortCriteriaBy = sortCriteriaBy;
		}

		public String getSortOrder() {
			return sortOrder;
		}

		public void setSortOrder(String sortOrder) {
			this.sortOrder = sortOrder;
		}
	    
	    public String getConfNumber() {
			return confNumber;
		}

		public void setConfNumber(String confNumber) {
			this.confNumber = confNumber;
		}
		
	    public String getInvoiceNumber() {
			return invoiceNumber;
		}

		public void setInvoiceNumber(String invoiceNumber) {
			this.invoiceNumber = invoiceNumber;
		}
		
	    public String getOrderNumber() {
			return orderNumber;
		}

		public void setOrderNumber(String orderNumber) {
			this.orderNumber = orderNumber;
		}
		
	    public String getOrderDetailNumber() {
			return orderDetailNumber;
		}

		public void setOrderDetailNumber(String orderDetailNumber) {
			this.orderDetailNumber = orderDetailNumber;
		}
		
		public String getRlJobTicket() {
			return rlJobTicket;
		}

		public void setRlJobTicket(String rlJobTicket) {
			this.rlJobTicket = rlJobTicket;
		}
		
	    public String getPermissionStatus() {
			return permissionStatus;
		}

		public void setPermissionStatus(String permissionStatus) {
			this.permissionStatus = permissionStatus;
		}
		
	    public String getProductTOU() {
			return productTOU;
		}

		public void setProductTOU(String productTOU) {
			this.productTOU = productTOU;
		}
		
	    public String getAcctNumber() {
			return acctNumber;
		}

		public void setAcctNumber(String acctNumber) {
			this.acctNumber = acctNumber;
		}
		
	    public String getAcctName() {
			return acctName;
		}

		public void setAcctName(String acctName) {
			this.acctName = acctName;
		}
		
	    public String getRhRefNumber() {
			return rhRefNumber;
		}

		public void setRhRefNumber(String rhRefNumber) {
			this.rhRefNumber = rhRefNumber;
		}
		
	    public Date getStartDate() {
			return startDate;
		}

		public void setStartDate(Date startDate) {
			this.startDate = startDate;
		}
		
	    public Date getEndDate() {
			return endDate;
		}

		public void setEndDate(Date endDate) {
			this.endDate = endDate;
		}
		
	    public String getDateType() {
			return dateType;
		}

		public void setDateType(String dateType) {
			this.dateType = dateType;
		}
		

		public String getLast4digits_adv() {
			return last4digits_adv;
		}

		public void setLast4digits_adv(String last4digits_adv) {
			this.last4digits_adv = last4digits_adv;
		}

		public String getInstitution_adv() {
			return institution_adv;
		}

		public void setInstitution_adv(String institution_adv) {
			this.institution_adv = institution_adv;
		}

		public String getRepublPublisher_adv() {
			return republPublisher_adv;
		}

		public void setRepublPublisher_adv(String republPublisher_adv) {
			this.republPublisher_adv = republPublisher_adv;
		}

		public String getRepublWork_adv() {
			return republWork_adv;
		}

		public void setRepublWork_adv(String republWork_adv) {
			this.republWork_adv = republWork_adv;
		}

		public String getCourseName_adv() {
			return courseName_adv;
		}

		public void setCourseName_adv(String courseName_adv) {
			this.courseName_adv = courseName_adv;
		}

		public String getPublicationName_adv() {
			return publicationName_adv;
		}

		public void setPublicationName_adv(String publicationName_adv) {
			this.publicationName_adv = publicationName_adv;
		}

		public String getTfWorkID_adv() {
			return tfWorkID_adv;
		}

		public void setTfWorkID_adv(String tfWorkID_adv) {
			this.tfWorkID_adv = tfWorkID_adv;
		}

		public String getWrWorkID_adv() {
			return wrWorkID_adv;
		}

		public void setWrWorkID_adv(String wrWorkID_adv) {
			this.wrWorkID_adv = wrWorkID_adv;
		}

		public String getStandardNumber_adv() {
			return standardNumber_adv;
		}

		public void setStandardNumber_adv(String standardNumber_adv) {
			this.standardNumber_adv = standardNumber_adv;
		}

		public String getRightsholderName_adv() {
			return rightsholderName_adv;
		}

		public void setRightsholderName_adv(String rightsholderName_adv) {
			this.rightsholderName_adv = rightsholderName_adv;
		}

		public String getInternetLogin_adv() {
			return internetLogin_adv;
		}

		public void setInternetLogin_adv(String internetLogin_adv) {
			this.internetLogin_adv = internetLogin_adv;
		}
		
		public String getOrderSource_adv() {
			return orderSource_adv;
		}

		public void setOrderSource_adv(String orderSource_adv) {
			this.orderSource_adv = orderSource_adv;
		}

		public String getOrderStatus_adv() {
			return orderStatus_adv;
		}

		public void setOrderStatus_adv(String orderStatus_adv) {
			this.orderStatus_adv = orderStatus_adv;
		}

		public String getLastUpdatedBy_adv() {
			return lastUpdatedBy_adv;
		}

		public void setLastUpdatedBy_adv(String lastUpdatedBy_adv) {
			this.lastUpdatedBy_adv = lastUpdatedBy_adv;
		}

		public String getRefData_adv() {
			return refData_adv;
		}

		public void setRefData_adv(String refData_adv) {
			this.refData_adv = refData_adv;
		}

		public String getRefType_adv() {
			return refType_adv;
		}

		public void setRefType_adv(String refType_adv) {
			this.refType_adv = refType_adv;
		}

		
		/////////////////////////////
		public boolean getShowCancelled() {
			return showCancelled;
		}

		public void setShowCancelled(boolean showCancelled) {
			this.showCancelled = showCancelled;
		}
	    
		public boolean getShowCourseInfo() {
			return showCourseInfo;
		}

		public void setShowCourseInfo(boolean showCourseInfo) {
			this.showCourseInfo = showCourseInfo;
		}
		
		public boolean isShowTfOrders() {
			return showTfOrders;
		}

		public void setShowTfOrders(boolean showTfOrders) {
			this.showTfOrders = showTfOrders;
		}

		public boolean isShowCoiOrders() {
			return showCoiOrders;
		}

		public void setShowCoiOrders(boolean showCoiOrders) {
			this.showCoiOrders = showCoiOrders;
		}

		public void clearFilterCriteria() {
			
			showCancelled = true;
		    showCourseInfo   = false;
		    showTfOrders = true;
		    showCoiOrders = true;
		    		    
		    confNumber = null;
		    invoiceNumber = null;
		    orderNumber = null;
		    orderDetailNumber = null;
		    permissionStatus = null; 
		    productTOU = null; 
		    acctNumber = null;
		    acctName = null;
		    startDate = null;
		    endDate = null;
		    dateType = null; 
		    rhRefNumber = null;
		    rlLicenseNumber = null;
		    
		    last4digits_adv = null;
		    institution_adv = null;
		    republPublisher_adv = null;
		    republWork_adv = null;
		    courseName_adv = null;
		    publicationName_adv = null;
		    tfWorkID_adv = null;
		    wrWorkID_adv = null;
		    standardNumber_adv = null;
		    rightsholderName_adv = null;
		    internetLogin_adv = null;
		    orderSource_adv = null;
		    orderStatus_adv = null;
		    lastUpdatedBy_adv = null;
		    refData_adv = null;
		    refType_adv = null; 
		    
		    includeOnlyBundledItems = false;
		    includeOnlyUnBundledItems = false;
		    includeOnlyInvoicedItems = false;
		    includeOnlyAdjustableItems = false;
		    		    
		}

		public boolean isAnyFilterCriteriaSet() {
			return 
			/*   isNotEmpty( acctName )
			|| isNotEmpty( acctNumber )
			|| isNotEmpty( confNumber )			
			|| isNotEmpty( dateType )
			|| isNotEmpty( endDate )			
			|| isNotEmpty( internetLogin_adv )
			|| isNotEmpty( invoiceNumber )
			|| isNotEmpty( last4digits_adv )
			|| isNotEmpty( lastUpdatedBy_adv )
			|| isNotEmpty( orderDetailNumber )
			|| isNotEmpty( orderNumber )
			|| isNotEmpty( orderSource_adv )
			|| isNotEmpty( orderStatus_adv )
			|| isNotEmpty( permissionStatus )
			|| isNotEmpty( productTOU )
			|| isNotEmpty( publicationName_adv )
			|| isNotEmpty( refData_adv )
			|| isNotEmpty( refType_adv )			
			|| isNotEmpty( rhAccountNumber_adv )
			|| isNotEmpty( rhRefNumber )
			|| isNotEmpty( rightsholderName_adv )			
			|| isNotEmpty( standardNumber_adv )
			|| isNotEmpty( startDate )
			|| isNotEmpty( tfWorkID_adv )*/	
			
			isAnyTFcriteriaSet()
						//OMS-only criteria
			|| isNotEmpty( republPublisher_adv )
			|| isNotEmpty( republWork_adv )
			|| isNotEmpty( wrWorkID_adv )
			|| isNotEmpty( institution_adv )
			|| isNotEmpty( courseName_adv )
			|| isNotEmpty( rlJobTicket )
			|| isNotEmpty( rlLicenseNumber )
			
			|| includeOnlyBundledItems
			|| includeOnlyUnBundledItems
			|| includeOnlyInvoicedItems
			|| includeOnlyAdjustableItems;
			
		}
		
		public boolean isAnyTFcriteriaSet() {
			return 
			   isNotEmpty( acctName )
			|| isNotEmpty( acctNumber )
			|| isNotEmpty( confNumber )			
			|| isNotEmpty( dateType )
			|| isNotEmpty( endDate )			
			|| isNotEmpty( internetLogin_adv )
			|| isNotEmpty( invoiceNumber )
			|| isNotEmpty( last4digits_adv )
			|| isNotEmpty( lastUpdatedBy_adv )
			|| isNotEmpty( orderDetailNumber )
			|| isNotEmpty( orderNumber )
			|| isNotEmpty( orderSource_adv )
			|| isNotEmpty( orderStatus_adv )
			|| isNotEmpty( permissionStatus )
			|| isNotEmpty( productTOU )
			|| isNotEmpty( publicationName_adv )
			|| isNotEmpty( refData_adv )
			|| isNotEmpty( refType_adv )			
			|| isNotEmpty( rhAccountNumber_adv )
			|| isNotEmpty( rhRefNumber )
			|| isNotEmpty( rightsholderName_adv )			
			|| isNotEmpty( standardNumber_adv )
			|| isNotEmpty( startDate )
			|| isNotEmpty( tfWorkID_adv );		
		}
		
		public void setSortCriteriaDefault() {
			
			if ( isViewSort() ) {
	    		sortCriteriaBy = OrderSearchCriteriaSortColumnEnum.SORT_BY_VIEW_DEFAULT.name();
	    		sortOrder = OrderSearchCriteriaSortDirectionEnum.ASC.getUserText(); 
			} else { 
				sortCriteriaBy = OrderSearchCriteriaSortColumnEnum.SORT_BY_DEFAULT.name();
				sortOrder = OrderSearchCriteriaSortDirectionEnum.DESC.getUserText(); 
			}
			
		}
		protected boolean isNotEmpty( Date date ) {
			return date != null;
		}

	/*	protected boolean isNotEmpty( OrderSource orderSource ) {
			return orderSource != null;
		} */
		
		protected boolean isNotEmpty( Long number ) {
			if ( number != null ) {
				return number > 0L;
			}
			return false;
		}
		
		protected boolean isNotEmpty( String text ){
			return StringUtils.isNotEmpty( text );
		}
		
		protected boolean isNotEmpty( int num ){
			return num > 0;
		}

		public int getResultsPerPage() {
			return resultsPerPage;
		}

		public void setResultsPerPage(int resultsPerPage) {
			this.resultsPerPage = resultsPerPage;
		}

		public int getDisplayFromRow() {
			return displayFromRow;
		}

		public void setDisplayFromRow(int displayFromRow) {
			this.displayFromRow = displayFromRow;
		}

		public int getDisplayToRow() {
			return displayToRow;
		}

		public void setDisplayToRow(int displayToRow) {
			this.displayToRow = displayToRow;
		}
		
		public String getStartDateFMT() {
			if ( getStartDate() != null ) {
				return MiscUtils.formatMMddyyyyDateSlashed(getStartDate());
			}
			return "";
		}
		public String getEndDateFMT() {
			if ( getEndDate() != null ) {
				return MiscUtils.formatMMddyyyyDateSlashed(getEndDate());
			}
			return "";
		}

		public boolean isIncludeExpandedResults() {
			return includeExpandedResults;
		}

		public void setIncludeExpandedResults(boolean includeExpandedResults) {
			this.includeExpandedResults = includeExpandedResults;
		}

		public String getBundleNumber() {
			return bundleNumber;
		}

		public void setBundleNumber(String bundleNumber) {
			this.bundleNumber = bundleNumber;
		}

		public CustomPageControl getPageControl() {
			if (pageControl == null) {
				// default page control so it is not null when jsp tries to set it
				setPageControl(new CustomPageControl());
			}
			return pageControl;
		}

		public void setPageControl(CustomPageControl pageControl) {
			this.pageControl = pageControl;
		}

		public String getRlLicenseNumber() {
			return rlLicenseNumber;
		}

		public void setRlLicenseNumber(String rlLicenseNumber) {
			this.rlLicenseNumber = rlLicenseNumber;
		}

		public String getRhAccountNumber_adv() {
			return rhAccountNumber_adv;
		}

		public void setRhAccountNumber_adv(String rhAccountNumber_adv) {
			this.rhAccountNumber_adv = rhAccountNumber_adv;
		}	

		public OrderSearchCriteria getLastServiceSearchCritera() {
			return lastServiceSearchCritera;
		}

		public void setLastServiceSearchCritera(
				OrderSearchCriteria lastServiceSearchCritera) {
			this.lastServiceSearchCritera = lastServiceSearchCritera;
		}
		
		public boolean isCriteriaChanged() {
			OrderSearchCriteria current = getServiceSearchCritera();
			OrderSearchCriteria last = getLastServiceSearchCritera();
			
			if ( current == null ) {
				if ( last == null ) {
					return false;
				} else {
					return true;
				}
			} else {
				if ( last == null ) {
					return true;
				} 
			}

			int dirty = 0;
			dirty += isChanged(current.getBundleName(),last.getBundleName());
			dirty += isChanged(current.getBundleRefNum(),last.getBundleRefNum());
			dirty += isChanged(current.getBuyerPartyId(),last.getBuyerPartyId());
			dirty += isChanged(current.getBuyerPtyInst(),last.getBuyerPtyInst());
			dirty += isChanged(current.getCategoryName(),last.getCategoryName());
			dirty += isChanged(current.getConfirmNumber(),last.getConfirmNumber());
			dirty += isChanged(current.getDataSource(),last.getDataSource());
			dirty += isChanged(current.getExternalOrderId(),last.getExternalOrderId());
			dirty += isChanged(current.getExternalItemId(),last.getExternalItemId());
			dirty += isChanged(current.getIncludeCancelledItems(),last.getIncludeCancelledItems());
			dirty += isChanged(current.getInternetLogin(),last.getInternetLogin());
			dirty += isChanged(current.getInvoiceBegDate(),last.getInvoiceBegDate());
			dirty += isChanged(current.getInvoiceEndDate(),last.getInvoiceEndDate());
			dirty += isChanged(current.getInvoiceNumber(),last.getInvoiceNumber());
			dirty += isChanged(current.getItemAvailabilityCd(),last.getItemAvailabilityCd());
			dirty += isChanged(current.getItemDescription(),last.getItemDescription());
			dirty += isChanged(current.getItemId(),last.getItemId());
			dirty += isChanged(current.getItemSourceKey(),last.getItemSourceKey());
			dirty += isChanged(current.getItemUpdUser(),last.getItemUpdUser());
			dirty += isChanged(current.getJobTicketNumber(),last.getJobTicketNumber());
			dirty += isChanged(current.getLastFourCc(),last.getLastFourCc());
			dirty += isChanged(current.getLcnAcctRefNum(),last.getLcnAcctRefNum());
			dirty += isChanged(current.getLcnHdrRefNum(),last.getLcnHdrRefNum());
			dirty += isChanged(current.getLicenseeAccount(),last.getLicenseeAccount());
			dirty += isChanged(current.getLicenseeName(),last.getLicenseeName());
			dirty += isChanged(current.getLicenseeRefNum(),last.getLicenseeRefNum());
			dirty += isChanged(current.getOrderBegDate(),last.getOrderBegDate());
			dirty += isChanged(current.getOrderEndDate(),last.getOrderEndDate());
			dirty += isChanged(current.getOrderSourceString(),last.getOrderSourceString());			
			dirty += isChanged(current.getOrganizationName(),last.getOrganizationName());
			dirty += isChanged(current.getPoNum(),last.getPoNum());
			dirty += isChanged(current.getResearchUserId(),last.getResearchUserId());
			dirty += isChanged(current.getRhRefNum(),last.getRhRefNum());
			dirty += isChanged(current.getRightAvailabilityCd(),last.getRightAvailabilityCd());
			dirty += isChanged(current.getRightsholderName(),last.getRightsholderName());
			dirty += isChanged(current.getStandardNumber(),last.getStandardNumber());
			//dirty += isChanged(current.getTouName(),last.getTouName());
			dirty += isChanged(current.getProductCode(),last.getProductCode());			
			dirty += isChanged(current.getRepubPublisher(),last.getRepubPublisher());
			dirty += isChanged(current.getRepubTitle(),last.getRepubTitle());
			dirty += isChanged(current.getRhAccountNumber(),last.getRhAccountNumber());
			//dirty += isChanged(current.getOrderHeaderId(),last.getOrderHeaderId());
			dirty += isChanged(current.getExternalDetailId(),last.getExternalDetailId());
			dirty += isChanged(current.getUpdateBegDate(),last.getUpdateBegDate());
			dirty += isChanged(current.getUpdateEndDate(),last.getUpdateEndDate());
			dirty += isChanged(current.getUseBegDate(),last.getUseBegDate());
			dirty += isChanged(current.getUseEndDate(),last.getUseEndDate());
			dirty += isChanged(current.getSortByColumn(),last.getSortByColumn());
			dirty += isChanged(current.getSortDirection(),last.getSortDirection());
			dirty += isGoingBackwards(Long.valueOf(current.getSearchToRow()).intValue(),Long.valueOf(last.getSearchToRow()).intValue());
			dirty += isGoingBackwards(Long.valueOf(current.getSearchFromRow()).intValue(),Long.valueOf(last.getSearchFromRow()).intValue());
			return dirty > 0;
		
		}

		private int isChanged(OrderSearchCriteriaSortColumnEnum current, OrderSearchCriteriaSortColumnEnum last) {
			if ( current == null ) {
				if ( last == null ) {
					return 0;
				} else {
					return 1;
				}
			} else {
				if ( last == null ) {
					return 1;
				} else {
					return (!(current.equals(last)))?1:0;
				}
			}
		}
		
		private int isChanged(OrderSearchCriteriaSortDirectionEnum current, OrderSearchCriteriaSortDirectionEnum last) {
			if ( current == null ) {
				if ( last == null ) {
					return 0;
				} else {
					return 1;
				}
			} else {
				if ( last == null ) {
					return 1;
				} else {
					return (!(current.equals(last)))?1:0;
				}
			}
		}

		private int isChanged(Boolean current, Boolean last) {
			if ( current == null ) {
				if ( last == null ) {
					return 0;
				} else {
					return 1;
				}
			} else {
				if ( last == null ) {
					return 1;
				} else {
					return (!(current.equals(last)))?1:0;
				}
			}
		}
		private int isGoingBackwards(Integer current, Integer last) {
			if ( current == null ) {
				if ( last == null ) {
					return 0;
				} else {
					return 1;
				}
			} else {
				if ( last == null ) {
					return 1;
				} else {
					if ( current < last ) {
						return 1;
					} else {
						return 0;
					}
				}
			}
		}
		private int isChanged(Long current, Long last) {
			if ( current == null ) {
				if ( last == null ) {
					return 0;
				} else {
					return 1;
				}
			} else {
				if ( last == null ) {
					return 1;
				} else {
					return (!(current.equals(last)))?1:0;
				}
			}
		}
		private int isChanged(Date current, Date last) {
			if ( current == null ) {
				if ( last == null ) {
					return 0;
				} else {
					return 1;
				}
			} else {
				if ( last == null ) {
					return 1;
				} else {
					return (!(current.equals(last)))?1:0;
				}
			}
		}
		
		private int isChanged(String current, String last) {
			if ( StringUtils.isEmpty(current) ) {
				if ( StringUtils.isEmpty(last) ) {
					return 0;
				} else {
					return 1;
				}
			} else {
				if ( StringUtils.isEmpty( last) ) {
					return 1;
				} else {
					return (!(current.equals(last)))?1:0;
				}
			}
		}

		public OrderSearchCriteria getServiceSearchCritera() {
			return serviceSearchCritera;
		}

		public void setServiceSearchCritera(OrderSearchCriteria serviceSearchCritera) {
			this.serviceSearchCritera = serviceSearchCritera;
		}

		private String trimmedValue(String value) {
			if ( StringUtils.isNotEmpty(value)) {
				return StringUtils.trim(value);
			}
			return "";
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

		public boolean isIncludeOnlyBundledItems() {
			return includeOnlyBundledItems;
		}

		public void setIncludeOnlyBundledItems(boolean includeOnlyBundledItems) {
			this.includeOnlyBundledItems = includeOnlyBundledItems;
		}

		public boolean isIncludeOnlyUnBundledItems() {
			return includeOnlyUnBundledItems;
		}

		public void setIncludeOnlyUnBundledItems(boolean includeOnlyUnBundledItems) {
			this.includeOnlyUnBundledItems = includeOnlyUnBundledItems;
		}
		
	    public boolean isIncludeOnlyInvoicedItems() {
			return includeOnlyInvoicedItems;
		}

		public void setIncludeOnlyInvoicedItems(boolean includeOnlyInvoicedItems) {
			this.includeOnlyInvoicedItems = includeOnlyInvoicedItems;
		}
		

		public boolean isIncludeOnlyAdjustableItems() {
			return includeOnlyAdjustableItems;
		}

		public void setIncludeOnlyAdjustableItems(boolean includeOnlyAdjustableItems) {
			this.includeOnlyAdjustableItems = includeOnlyAdjustableItems;
		}


		public String getPagingGroupKey() {
			return pagingGroupKey;
		}

		public void setPagingGroupKey(String pagingGroupKey) {
			this.pagingGroupKey = pagingGroupKey;
		}
		
		public boolean isPagingGroupKeyUsed() {
			return pagingGroupKeyUsed;
		}

		public void setPagingGroupKeyUsed( boolean pagingGroupKeyUsed) {
			this.pagingGroupKeyUsed = pagingGroupKeyUsed;
		}
}