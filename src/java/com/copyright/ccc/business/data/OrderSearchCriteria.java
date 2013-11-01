package com.copyright.ccc.business.data;

import java.io.Serializable;
import java.util.Date;

import com.copyright.base.enums.OrderSourceEnum;
import com.copyright.base.enums.ProductEnum;
import com.copyright.ccc.business.services.ItemAvailabilityEnum;
import com.copyright.ccc.business.services.ItemConstants;
import com.copyright.ccc.business.services.user.User;
import com.copyright.ccc.business.services.user.UserServices;
import com.copyright.data.order.OrderDetailStateEnum;
import com.copyright.data.order.SourcedFromEnum;
import com.copyright.svc.order.api.data.BillingStatusEnum;
import com.copyright.svc.order.api.data.ItemSearchCriteria;
import com.copyright.svc.order.api.data.ItemSearchSortableFieldEnum;
import com.copyright.svc.order.api.data.ItemStatusEnum;
import com.copyright.svc.order.api.data.OrderSource;
import com.copyright.svc.order.api.data.SortTypeEnum;
import com.copyright.svc.rightsResolver.api.data.TouCategoryTypeEnum;

public class OrderSearchCriteria  implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	public static final int DEFAULT_RESULTS_PER_PAGE = 25;

    //public static final int ASCENDINGSORTORDER = 0;
    //public static final int DESCENDINGSORTORDER = 1;

    // Order Purchase Sorts
    //public static final int ORDERDATESORT = 0;
    //public static final int CONFNUMSORT = 1;
//    public static final int INSTITUTIONSORT = 2;
    //public static final int STARTOFTERMSORT = 3;
//    public static final int COURSENAMESORT = 4;
//    public static final int COURSENUMBERSORT = 5;
//    public static final int INSTRUCTORSORT = 6;
//    public static final int YOURREFERENCESORT = 7;
//    public static final int YOURACCTREFERENCESORT = 17;
    
// Order License Sorts
//  ORDERDATESORT = 0; 
//    public static final int PUBLICATIONTITLESORT = 8;
    //public static final int ORDERDETAILIDSORT = 9;
//    public static final int PERMISSIONTYPESORT = 10;
//    public static final int PERMISSIONSTATUSSORT = 11;
//    public static final int BILLINGSTATUSSORT = 12;
// YOURREFERENCESORT = 7;
//    public static final int INVOICENUMBERSORT = 13;
//    public static final int REPUBLICATIONTITLESORT = 14;
//    public static final int REPUBLICATIONDATESORT = 15;
//    public static final int REPUBLICATIONPUBLISHERSORT = 16;
    

//    private int _resultsPerPage = DEFAULT_RESULTS_PER_PAGE;
//    private int _displayFromRow = 0;
//    private int _displayToRow = DEFAULT_RESULTS_PER_PAGE - 1;

 
    ItemSearchCriteria coiItemSearchCriteria;
    
    com.copyright.data.order.ItemSearchCriteria tfItemSearchCriteria;
    
/*    private Long _buyerPtyInst;
	private Long _buyerPartyId;

	private Long _accountNumber;
	private Long _confirmNumber;
	private Long _itemId;
	
	private Date _searchFromStartOfUseDtm;
	private Date _searchToStartOfUseDtm;

    private Date _searchFromOrderDtm;
    private Date _searchToOrderDtm;
*/
    
    private String _categoryCd;
    private String _permissionStatus;
    private boolean _isSufficientTfCriteria = false;
    private boolean _isSufficientCoiCriteria = false;
    private boolean _validTfCriteria = false;
    private boolean _showAdjustedValues = false;
    private boolean _mergeResults = true;
    
    private boolean _buyerPartyIdSetByInternetLogin = false;
    private User _user;
    
    private OrderSearchCriteriaSortDirectionEnum sortDirection = OrderSearchCriteriaSortDirectionEnum.DESC;
    private OrderSearchCriteriaSortColumnEnum sortByColumn = OrderSearchCriteriaSortColumnEnum.SORT_BY_DEFAULT;
    
    private OrderSearchRowNumbers orderSearchRowNumbers;
    private String dataSource = OrderSearchCriteriaDataSourceEnum.ALL.name();
    private String orderSourceString = null;
    
    public OrderSearchCriteria() {
    	coiItemSearchCriteria = new ItemSearchCriteria();
    	tfItemSearchCriteria = new com.copyright.data.order.ItemSearchCriteria();
    	orderSearchRowNumbers = new OrderSearchRowNumbers();
    	this.resetDefaults();
	}

    public void resetDefaults() {
    	tfItemSearchCriteria.setSourcedFrom(SourcedFromEnum.TF);
        sortDirection = OrderSearchCriteriaSortDirectionEnum.DESC;
        sortByColumn = OrderSearchCriteriaSortColumnEnum.SORT_BY_DEFAULT;
        dataSource = OrderSearchCriteriaDataSourceEnum.ALL.name();
        setOrderSourceString(null);
        setSearchFromRow(1);
        setSearchToRow(24);
        orderSearchRowNumbers = new OrderSearchRowNumbers();
//    	_resultsPerPage = DEFAULT_RESULTS_PER_PAGE;
    	setLicenseeName(null);
    	setLicenseeAccount(null);
    	setConfirmNumber(null);
    	setInvoiceNumber(null);
    	setRhRefNum(null);
    	setRightAvailabilityCd(null);
    	setRightSourceCd(null);
    	setTouName(null);
    	setExternalOrderId(null); // wrWrkInst
    	setItemId(null); // searches both item_id and external_item_id aka odt_inst
    	setOrderBegDate(null);
    	setOrderEndDate(null);
    	setJobTicketNumber(null);
    	setLcnAcctRefNum(null); //Acct'g Reference #
    	setLcnHdrRefNum(null); //Doc Reference # (at order level)
//    	setBillingRefNum(null); //billingReference  only applies to TF
    	setLicenseeRefNum(null); //equivalent to lcnDtlRefNum(null);
    	setPoNum(null); //poNum(null);
    	setLastFourCc(null);
    	setBundleName(null); // use for course name or repub work title
    	setOrganizationName(null); // use for institution name or publisher name
    	setItemDescription(null);
    	setRightsholderName(null);
    	setItemSourceKey(null);
    	setExternalItemId(null);
    	setStandardNumber(null);
    	setOrderSourceCd(null);
    	setItemUpdUser(null);
    	setResearchUserId(null);
    	setUseBegDate(null);
    	setUseEndDate(null);
    	setItemAvailabilityCd(null);
		setIsOpenSpecialOrder(null);
        setCategoryName(null);
        setBuyerPtyInst(null);  
        setBuyerPartyId(null);  
    	setInternetLogin(null);
//        setBuyerName(null);
        setBundleRefNum(null);  
        setIncludeCancelledItems(true);
        setBundleId( null );
        setIncludeOnlyBundledItems( false );
        setIncludeOnlyUnBundledItems( false );
        setOrderHeaderId(null);
        setRepubPublisher(null);
        setRepubTitle(null);
        setRhAccountNumber(null);
        setExternalDetailId(null);
        setIsInvoiced(null);
        setIsConfirmed(true);
        setTfSourcedFrom(SourcedFromEnum.TF);
        setProductCode(null);
        setCategoryCd(null);
        setBillingStatus(null);
        setIncludeHiddenOrders(true);
        setOrderState(null);
//        setTfOrderDetailState(OrderDetailStateEnum.OPEN);
//        tfItemSearchCriteria.setOrderSource(com.copyright.data.order.OrderSourceEnum.COPYRIGHT_DOT_COM);

		setDefaultSort();
		setSufficientCriteria(false);
		setValidTfCriteria(true);
		setMergeResults(true);
		setInstructorName(null);
    }
    
    
    
    public ItemSearchCriteria getCoiItemSearchCriteria() {
    	return this.coiItemSearchCriteria;
    }
 
    public com.copyright.data.order.ItemSearchCriteria getTfItemSearchCriteria() {
    	return this.tfItemSearchCriteria;
    }
 
    public SourcedFromEnum getTfSourcedFrom() {
    	return tfItemSearchCriteria.getSourcedFrom();
    }
    
    public void setTfSourcedFrom(SourcedFromEnum sourcedFromEnum) {
    	tfItemSearchCriteria.setSourcedFrom(sourcedFromEnum);
    }
    
    public OrderDetailStateEnum getTfOrderDetailState() {
    	return tfItemSearchCriteria.getOrderDetailState();
    }
    
    public void setTfOrderDetailState(OrderDetailStateEnum orderDetailStateEnum) {
    	tfItemSearchCriteria.setOrderDetailState(orderDetailStateEnum);
    }
    
    @Deprecated
    public void setResultsPerPage(int r) {
		
	}

    public OrderSearchRowNumbers getOrderSearchRowNumbers() {
		return orderSearchRowNumbers;
	}

	public void setOrderSearchRowNumbers(OrderSearchRowNumbers orderSearchRowNumbers) {
		this.orderSearchRowNumbers = orderSearchRowNumbers;
	}

    
	public OrderSearchCriteriaSortDirectionEnum getSortDirection() {
		return sortDirection;
	}

	public void setSortDirection(OrderSearchCriteriaSortDirectionEnum sortDirection) {
		this.sortDirection = sortDirection;
	}

	public OrderSearchCriteriaSortColumnEnum getSortByColumn() {
		return sortByColumn;
	}

	public void setSortByColumn(OrderSearchCriteriaSortColumnEnum sortByColumn) {
		this.sortByColumn = sortByColumn;
	}
	
	public void setDefaultSort() {
		coiItemSearchCriteria.removeSortQualifiers();
		tfItemSearchCriteria.removeSortQualifiers();
		
		coiItemSearchCriteria.addSortQualifier( ItemSearchSortableFieldEnum.ORDER_DATE, SortTypeEnum.DESC );
		coiItemSearchCriteria.addSortQualifier( ItemSearchSortableFieldEnum.CONFIRM_NUMBER, SortTypeEnum.DESC );
		coiItemSearchCriteria.addSortQualifier( ItemSearchSortableFieldEnum.ORDER_DETAIL_ID, SortTypeEnum.ASC );

		tfItemSearchCriteria.addSortQualifier( com.copyright.data.order.ItemSearchSortableFieldEnum.ORDER_DATE,
			    com.copyright.data.order.SortTypeEnum.DESC);
		tfItemSearchCriteria.addSortQualifier( com.copyright.data.order.ItemSearchSortableFieldEnum.CONFIRM_NUMBER,
			    com.copyright.data.order.SortTypeEnum.DESC);
		tfItemSearchCriteria.addSortQualifier( com.copyright.data.order.ItemSearchSortableFieldEnum.ORDER_DETAIL_ID,
			    com.copyright.data.order.SortTypeEnum.ASC);
	}

	public void addSecondarySort() {
		
		coiItemSearchCriteria.addSortQualifier( ItemSearchSortableFieldEnum.ORDER_DATE, SortTypeEnum.DESC );
		coiItemSearchCriteria.addSortQualifier( ItemSearchSortableFieldEnum.CONFIRM_NUMBER, SortTypeEnum.DESC );
		coiItemSearchCriteria.addSortQualifier( ItemSearchSortableFieldEnum.ORDER_DETAIL_ID, SortTypeEnum.ASC );

		tfItemSearchCriteria.addSortQualifier( com.copyright.data.order.ItemSearchSortableFieldEnum.ORDER_DATE,
			    com.copyright.data.order.SortTypeEnum.DESC);
		tfItemSearchCriteria.addSortQualifier( com.copyright.data.order.ItemSearchSortableFieldEnum.CONFIRM_NUMBER,
			    com.copyright.data.order.SortTypeEnum.DESC);
		tfItemSearchCriteria.addSortQualifier( com.copyright.data.order.ItemSearchSortableFieldEnum.ORDER_DETAIL_ID,
			    com.copyright.data.order.SortTypeEnum.ASC);
	}

	public void addPurchaseSecondarySort() {
		
		coiItemSearchCriteria.addSortQualifier( ItemSearchSortableFieldEnum.ORDER_DATE, SortTypeEnum.DESC );
		coiItemSearchCriteria.addSortQualifier( ItemSearchSortableFieldEnum.CONFIRM_NUMBER, SortTypeEnum.DESC );

//		tfItemSearchCriteria.addSortQualifier( com.copyright.data.order.ItemSearchSortableFieldEnum.ORDER_DATE,
//			    com.copyright.data.order.SortTypeEnum.DESC);
//		tfItemSearchCriteria.addSortQualifier( com.copyright.data.order.ItemSearchSortableFieldEnum.CONFIRM_NUMBER,
//			    com.copyright.data.order.SortTypeEnum.ASC);
	}
	
	public void setOrderPurchaseDefaultSort() {
		coiItemSearchCriteria.removeSortQualifiers();
//		tfItemSearchCriteria.removeSortQualifiers();
		
		coiItemSearchCriteria.addSortQualifier( ItemSearchSortableFieldEnum.ORDER_DATE, SortTypeEnum.DESC );
		coiItemSearchCriteria.addSortQualifier( ItemSearchSortableFieldEnum.CONFIRM_NUMBER, SortTypeEnum.DESC );

//		tfItemSearchCriteria.addSortQualifier( com.copyright.data.order.ItemSearchSortableFieldEnum.ORDER_DATE,
//			    com.copyright.data.order.SortTypeEnum.DESC);
//		tfItemSearchCriteria.addSortQualifier( com.copyright.data.order.ItemSearchSortableFieldEnum.CONFIRM_NUMBER,
//			    com.copyright.data.order.SortTypeEnum.ASC);

	}
	
	
	public void addSortQualifierListForSortColumnAndDirection( OrderSearchCriteriaSortColumnEnum column, OrderSearchCriteriaSortDirectionEnum direction ) {
		
		SortTypeEnum coiSType = null;
		com.copyright.data.order.SortTypeEnum tfSType;
		
		coiItemSearchCriteria.removeSortQualifiers();
		tfItemSearchCriteria.removeSortQualifiers();
		
		//if ( direction.equals(OrderSearchCriteriaSortColumnEnum.ASCENDING_SORT) ) { Findbug error
		if ( sortDirection.equals(OrderSearchCriteriaSortDirectionEnum.ASC) ) {
				coiSType = SortTypeEnum.ASC;
				tfSType = com.copyright.data.order.SortTypeEnum.ASC;
			} else {
				coiSType = SortTypeEnum.DESC;
				tfSType = com.copyright.data.order.SortTypeEnum.DESC;
			}
		
		if ( column.equals(OrderSearchCriteriaSortColumnEnum.SORT_BY_ORDER_DATE) ) {
			coiItemSearchCriteria.addSortQualifier(ItemSearchSortableFieldEnum.ORDER_DATE, coiSType );
			tfItemSearchCriteria.addSortQualifier( com.copyright.data.order.ItemSearchSortableFieldEnum.ORDER_DATE,
			tfSType);
			
			coiItemSearchCriteria.addSortQualifier( ItemSearchSortableFieldEnum.CONFIRM_NUMBER, SortTypeEnum.DESC );
			coiItemSearchCriteria.addSortQualifier( ItemSearchSortableFieldEnum.ORDER_DETAIL_ID, SortTypeEnum.ASC );
			tfItemSearchCriteria.addSortQualifier( com.copyright.data.order.ItemSearchSortableFieldEnum.CONFIRM_NUMBER,
				    com.copyright.data.order.SortTypeEnum.DESC);
			tfItemSearchCriteria.addSortQualifier( com.copyright.data.order.ItemSearchSortableFieldEnum.ORDER_DETAIL_ID,
				    com.copyright.data.order.SortTypeEnum.ASC);
		} else if ( column.equals(OrderSearchCriteriaSortColumnEnum.SORT_BY_DEFAULT ) ) {
			setDefaultSort();
		} else if ( column.equals(OrderSearchCriteriaSortColumnEnum.SORT_BY_VIEW_DEFAULT ) ) {
			coiItemSearchCriteria.addSortQualifier(ItemSearchSortableFieldEnum.ORDER_DETAIL_ID, coiSType);
			tfItemSearchCriteria.addSortQualifier( com.copyright.data.order.ItemSearchSortableFieldEnum.ORDER_DETAIL_ID,
				    tfSType);
		} else if ( column.equals(OrderSearchCriteriaSortColumnEnum.SORT_BY_START_OF_TERM ) ) {
			coiItemSearchCriteria.addSortQualifier(ItemSearchSortableFieldEnum.START_OF_TERM, coiSType);
			tfItemSearchCriteria.addSortQualifier( com.copyright.data.order.ItemSearchSortableFieldEnum.START_OF_TERM,
				    tfSType);
			addSecondarySort();
		} else if ( column.equals(OrderSearchCriteriaSortColumnEnum.SORT_BY_CONFIRM_NUMBER ) ) {
			coiItemSearchCriteria.addSortQualifier(ItemSearchSortableFieldEnum.CONFIRM_NUMBER, coiSType);
			tfItemSearchCriteria.addSortQualifier( com.copyright.data.order.ItemSearchSortableFieldEnum.CONFIRM_NUMBER,
				    tfSType);	
			addSecondarySort();
		} else if ( column.equals(OrderSearchCriteriaSortColumnEnum.SORT_BY_ORDER_DETAIL_ID ) ) {
			coiItemSearchCriteria.addSortQualifier(ItemSearchSortableFieldEnum.ORDER_DETAIL_ID, coiSType);
			tfItemSearchCriteria.addSortQualifier( com.copyright.data.order.ItemSearchSortableFieldEnum.ORDER_DETAIL_ID,
				    tfSType);
		} else if ( column.equals(OrderSearchCriteriaSortColumnEnum.SORT_BY_INTERNET_LOGIN ) ) {
			coiItemSearchCriteria.addSortQualifier(ItemSearchSortableFieldEnum.INTERNET_LOGIN, coiSType);
			tfItemSearchCriteria.addSortQualifier( com.copyright.data.order.ItemSearchSortableFieldEnum.INTERNET_LOGIN,
				    tfSType);			
			addSecondarySort();
		} else if ( column.equals(OrderSearchCriteriaSortColumnEnum.SORT_BY_INVOICE_NUMBER ) ) {
			coiItemSearchCriteria.addSortQualifier(ItemSearchSortableFieldEnum.INVOICE_NUMBER, coiSType);
			tfItemSearchCriteria.addSortQualifier( com.copyright.data.order.ItemSearchSortableFieldEnum.INVOICE_NUMBER,
				    tfSType);	
			addSecondarySort();
		} else if ( column.equals(OrderSearchCriteriaSortColumnEnum.SORT_BY_LAST_UPDATE_DATE ) ) {
			coiItemSearchCriteria.addSortQualifier(ItemSearchSortableFieldEnum.LAST_UPDATE_DATE, coiSType);
			tfItemSearchCriteria.addSortQualifier( com.copyright.data.order.ItemSearchSortableFieldEnum.LAST_UPDATE_DATE,
				    tfSType);			
			addSecondarySort();
		} else if ( column.equals(OrderSearchCriteriaSortColumnEnum.SORT_BY_LICENSEE_ACCOUNT ) ) {
			coiItemSearchCriteria.addSortQualifier(ItemSearchSortableFieldEnum.LICENSEE_ACCOUNT, coiSType);
			tfItemSearchCriteria.addSortQualifier( com.copyright.data.order.ItemSearchSortableFieldEnum.LICENSEE_ACCOUNT,
				    tfSType);					
			addSecondarySort();
		} else if ( column.equals(OrderSearchCriteriaSortColumnEnum.SORT_BY_PUBLICATION_TITLE ) ) {
			coiItemSearchCriteria.addSortQualifier(ItemSearchSortableFieldEnum.PUBLICATION_TITLE, coiSType);
			tfItemSearchCriteria.addSortQualifier( com.copyright.data.order.ItemSearchSortableFieldEnum.PUBLICATION_TITLE,
				    tfSType);	
			addSecondarySort();
		} else if ( column.equals(OrderSearchCriteriaSortColumnEnum.SORT_BY_BILLING_STATUS ) ) {
			coiItemSearchCriteria.addSortQualifier(ItemSearchSortableFieldEnum.BILLING_STATUS, coiSType);
			tfItemSearchCriteria.addSortQualifier( com.copyright.data.order.ItemSearchSortableFieldEnum.BILLING_STATUS,
				    tfSType);	
			addSecondarySort();
		} else if ( column.equals(OrderSearchCriteriaSortColumnEnum.SORT_BY_PERMISSION_TYPE ) ) {
			coiItemSearchCriteria.addSortQualifier(ItemSearchSortableFieldEnum.PERMISSION_TYPE, coiSType);
			tfItemSearchCriteria.addSortQualifier( com.copyright.data.order.ItemSearchSortableFieldEnum.PERMISSION_TYPE_OF_USE,
				    tfSType);	
			addSecondarySort();
		} else if ( column.equals(OrderSearchCriteriaSortColumnEnum.SORT_BY_PERMISSION_STATUS ) ) {
			coiItemSearchCriteria.addSortQualifier(ItemSearchSortableFieldEnum.PERMISSION_STATUS, coiSType);
			tfItemSearchCriteria.addSortQualifier( com.copyright.data.order.ItemSearchSortableFieldEnum.PERMISSION_STATUS,
				    tfSType);	
			addSecondarySort();
		} else if ( column.equals(OrderSearchCriteriaSortColumnEnum.SORT_BY_REPUBLICATION_TITLE ) ) {
			coiItemSearchCriteria.addSortQualifier(ItemSearchSortableFieldEnum.REPUBLICATION_TITLE, coiSType);
			tfItemSearchCriteria.addSortQualifier( com.copyright.data.order.ItemSearchSortableFieldEnum.REPUB_IN_WORK,
				    tfSType);	
			addSecondarySort();
		} else if ( column.equals(OrderSearchCriteriaSortColumnEnum.SORT_BY_REPUBLICATION_DATE ) ) {
			coiItemSearchCriteria.addSortQualifier(ItemSearchSortableFieldEnum.REPUBLICATION_DATE, coiSType);
			tfItemSearchCriteria.addSortQualifier( com.copyright.data.order.ItemSearchSortableFieldEnum.REPUB_DATE,
				    tfSType);	
			addSecondarySort();
		} else if ( column.equals(OrderSearchCriteriaSortColumnEnum.SORT_BY_REPUBLICATION_PUBLISHER ) ) {
			coiItemSearchCriteria.addSortQualifier(ItemSearchSortableFieldEnum.REPUBLICATION_PUBLISHER, coiSType);
			tfItemSearchCriteria.addSortQualifier( com.copyright.data.order.ItemSearchSortableFieldEnum.REPUB_PUB,
				    tfSType);	
			addSecondarySort();
		} else if ( column.equals(OrderSearchCriteriaSortColumnEnum.SORT_BY_YOUR_REFERENCE ) ) {
			coiItemSearchCriteria.addSortQualifier(ItemSearchSortableFieldEnum.YOUR_REFERENCE, coiSType);
			tfItemSearchCriteria.addSortQualifier( com.copyright.data.order.ItemSearchSortableFieldEnum.LCN_HEADER_REF_NUM,
				    tfSType);	
			addSecondarySort();
		}
		


		
		
    }
	
	public void addPurchaseSortQualifierListForSortColumnAndDirection( OrderSearchCriteriaSortColumnEnum column, OrderSearchCriteriaSortDirectionEnum direction ) {
		
		SortTypeEnum coiSType = null;
		com.copyright.data.order.SortTypeEnum tfSType;
		
		coiItemSearchCriteria.removeSortQualifiers();
		tfItemSearchCriteria.removeSortQualifiers();
		
		//if ( direction.equals(OrderSearchCriteriaSortColumnEnum.ASCENDING_SORT) ) { Findbug error
		if ( sortDirection.equals(OrderSearchCriteriaSortDirectionEnum.ASC) ) {
				coiSType = SortTypeEnum.ASC;
				tfSType = com.copyright.data.order.SortTypeEnum.ASC;
			} else {
				coiSType = SortTypeEnum.DESC;
				tfSType = com.copyright.data.order.SortTypeEnum.DESC;
			}
		
		if ( column.equals(OrderSearchCriteriaSortColumnEnum.SORT_BY_ORDER_DATE) ) {
			coiItemSearchCriteria.addSortQualifier(ItemSearchSortableFieldEnum.ORDER_DATE, coiSType );
//			tfItemSearchCriteria.addSortQualifier( com.copyright.data.order.ItemSearchSortableFieldEnum.ORDER_DATE,
//			tfSType);
			coiItemSearchCriteria.addSortQualifier( ItemSearchSortableFieldEnum.CONFIRM_NUMBER, SortTypeEnum.DESC );
//			tfItemSearchCriteria.addSortQualifier( com.copyright.data.order.ItemSearchSortableFieldEnum.CONFIRM_NUMBER,
//				    com.copyright.data.order.SortTypeEnum.ASC);
		} else if ( column.equals(OrderSearchCriteriaSortColumnEnum.SORT_BY_DEFAULT ) ) {
			setDefaultSort();
		} else if ( column.equals(OrderSearchCriteriaSortColumnEnum.SORT_BY_VIEW_DEFAULT ) ) {
			coiItemSearchCriteria.addSortQualifier(ItemSearchSortableFieldEnum.ORDER_DETAIL_ID, coiSType);
//			tfItemSearchCriteria.addSortQualifier( com.copyright.data.order.ItemSearchSortableFieldEnum.ORDER_DETAIL_ID,
//				    tfSType);
		}else if ( column.equals(OrderSearchCriteriaSortColumnEnum.SORT_BY_ORDER_DETAIL_ID ) ) {
			coiItemSearchCriteria.addSortQualifier(ItemSearchSortableFieldEnum.ORDER_DETAIL_ID, coiSType);
//			tfItemSearchCriteria.addSortQualifier( com.copyright.data.order.ItemSearchSortableFieldEnum.ORDER_DETAIL_ID,
//				    tfSType);
		}else if ( column.equals(OrderSearchCriteriaSortColumnEnum.SORT_BY_START_OF_TERM ) ) {
			coiItemSearchCriteria.addSortQualifier(ItemSearchSortableFieldEnum.START_OF_TERM, coiSType);
			coiItemSearchCriteria.addSortQualifier( ItemSearchSortableFieldEnum.CONFIRM_NUMBER, SortTypeEnum.DESC );
//			tfItemSearchCriteria.addSortQualifier( com.copyright.data.order.ItemSearchSortableFieldEnum.START_OF_TERM,
//				    tfSType);
			addPurchaseSecondarySort();
		} else if ( column.equals(OrderSearchCriteriaSortColumnEnum.SORT_BY_CONFIRM_NUMBER ) ) {
			coiItemSearchCriteria.addSortQualifier(ItemSearchSortableFieldEnum.CONFIRM_NUMBER, coiSType);
//			tfItemSearchCriteria.addSortQualifier( com.copyright.data.order.ItemSearchSortableFieldEnum.CONFIRM_NUMBER,
//				    tfSType);	
			addPurchaseSecondarySort();
		} else if ( column.equals(OrderSearchCriteriaSortColumnEnum.SORT_BY_INTERNET_LOGIN ) ) {
			coiItemSearchCriteria.addSortQualifier(ItemSearchSortableFieldEnum.INTERNET_LOGIN, coiSType);
			coiItemSearchCriteria.addSortQualifier( ItemSearchSortableFieldEnum.CONFIRM_NUMBER, SortTypeEnum.DESC );
//			tfItemSearchCriteria.addSortQualifier( com.copyright.data.order.ItemSearchSortableFieldEnum.INTERNET_LOGIN,
//				    tfSType);			
			addPurchaseSecondarySort();
		} else if ( column.equals(OrderSearchCriteriaSortColumnEnum.SORT_BY_LAST_UPDATE_DATE ) ) {
			coiItemSearchCriteria.addSortQualifier(ItemSearchSortableFieldEnum.LAST_UPDATE_DATE, coiSType);
			coiItemSearchCriteria.addSortQualifier( ItemSearchSortableFieldEnum.CONFIRM_NUMBER, SortTypeEnum.DESC );
//			tfItemSearchCriteria.addSortQualifier( com.copyright.data.order.ItemSearchSortableFieldEnum.LAST_UPDATE_DATE,
//				    tfSType);			
			addPurchaseSecondarySort();
		} else if ( column.equals(OrderSearchCriteriaSortColumnEnum.SORT_BY_LICENSEE_ACCOUNT ) ) {
			coiItemSearchCriteria.addSortQualifier(ItemSearchSortableFieldEnum.LICENSEE_ACCOUNT, coiSType);
			coiItemSearchCriteria.addSortQualifier( ItemSearchSortableFieldEnum.CONFIRM_NUMBER, SortTypeEnum.DESC );
//			tfItemSearchCriteria.addSortQualifier( com.copyright.data.order.ItemSearchSortableFieldEnum.LICENSEE_ACCOUNT,
//				    tfSType);					
			addPurchaseSecondarySort();
		} else if ( column.equals(OrderSearchCriteriaSortColumnEnum.SORT_BY_YOUR_REFERENCE ) ) {
			coiItemSearchCriteria.addSortQualifier(ItemSearchSortableFieldEnum.YOUR_REFERENCE, coiSType);
			coiItemSearchCriteria.addSortQualifier( ItemSearchSortableFieldEnum.CONFIRM_NUMBER, SortTypeEnum.DESC );
//			tfItemSearchCriteria.addSortQualifier( com.copyright.data.order.ItemSearchSortableFieldEnum.ORDER_DATE,
//				    tfSType);					
			addPurchaseSecondarySort();
		} else if ( column.equals(OrderSearchCriteriaSortColumnEnum.SORT_BY_YOUR_ACC_REFERENCE ) ) {
			coiItemSearchCriteria.addSortQualifier(ItemSearchSortableFieldEnum.YOUR_ACC_REFERENCE, coiSType);
			coiItemSearchCriteria.addSortQualifier( ItemSearchSortableFieldEnum.CONFIRM_NUMBER, SortTypeEnum.DESC );
//			tfItemSearchCriteria.addSortQualifier( com.copyright.data.order.ItemSearchSortableFieldEnum.ORDER_DATE,
//				    tfSType);					
			addPurchaseSecondarySort();
		} else if ( column.equals(OrderSearchCriteriaSortColumnEnum.SORT_BY_INSTITUTION ) ) {
			coiItemSearchCriteria.addSortQualifier(ItemSearchSortableFieldEnum.INSTITUTION, coiSType);
			coiItemSearchCriteria.addSortQualifier( ItemSearchSortableFieldEnum.CONFIRM_NUMBER, SortTypeEnum.DESC );
//			tfItemSearchCriteria.addSortQualifier( com.copyright.data.order.ItemSearchSortableFieldEnum.ORDER_DATE,
//				    tfSType);					
			addPurchaseSecondarySort();
		} else if ( column.equals(OrderSearchCriteriaSortColumnEnum.SORT_BY_COURSE_NAME ) ) {
			coiItemSearchCriteria.addSortQualifier(ItemSearchSortableFieldEnum.COURSE_NAME, coiSType);
			coiItemSearchCriteria.addSortQualifier( ItemSearchSortableFieldEnum.CONFIRM_NUMBER, SortTypeEnum.DESC );
//			tfItemSearchCriteria.addSortQualifier( com.copyright.data.order.ItemSearchSortableFieldEnum.LICENSEE_ACCOUNT,
//				    tfSType);					
			addPurchaseSecondarySort();
		} else if ( column.equals(OrderSearchCriteriaSortColumnEnum.SORT_BY_COURSE_NUMBER ) ) {
			coiItemSearchCriteria.addSortQualifier(ItemSearchSortableFieldEnum.COURSE_NUMBER, coiSType);
			coiItemSearchCriteria.addSortQualifier( ItemSearchSortableFieldEnum.CONFIRM_NUMBER, SortTypeEnum.DESC );
//			tfItemSearchCriteria.addSortQualifier( com.copyright.data.order.ItemSearchSortableFieldEnum.LICENSEE_ACCOUNT,
//				    tfSType);					
			addPurchaseSecondarySort();
		} else if ( column.equals(OrderSearchCriteriaSortColumnEnum.SORT_BY_INSTRUCTOR ) ) {
			coiItemSearchCriteria.addSortQualifier(ItemSearchSortableFieldEnum.INSTRUCTOR, coiSType);
			coiItemSearchCriteria.addSortQualifier( ItemSearchSortableFieldEnum.CONFIRM_NUMBER, SortTypeEnum.DESC );
			tfItemSearchCriteria.addSortQualifier( com.copyright.data.order.ItemSearchSortableFieldEnum.INSTRUCTOR,
				    tfSType);					
			addPurchaseSecondarySort();
		} 
		
		
    }
	
	
//	public int getResultsPerPage() {
//		return _resultsPerPage;
//	}
//	public void setResultsPerPage(int perPage) {
//		_resultsPerPage = perPage;
//	}
	@Deprecated
	public int getDisplayFromRow() {
		return getCoiItemSearchCriteria().getSearchFromRow();
	}
	@Deprecated
	public void setDisplayFromRow(int fromRow) {
		getCoiItemSearchCriteria().setSearchFromRow(fromRow);
	}
	@Deprecated
	public int getDisplayToRow() {
		return getCoiItemSearchCriteria().getSearchToRow();
	}
	@Deprecated
	public void setDisplayToRow(int toRow) {
		getCoiItemSearchCriteria().setSearchToRow(toRow);
	}

	public Long getBuyerPtyInst() {
		return getCoiItemSearchCriteria().getBuyerPtyInst();
	}
	public void setBuyerPtyInst(Long ptyInst) {
		getCoiItemSearchCriteria().setBuyerPtyInst(ptyInst);
	}
	
	public Long getBuyerPartyId() {
		return getCoiItemSearchCriteria().getBuyerPartyId();
	}
	public void setBuyerPartyId(Long partyId) {
		getCoiItemSearchCriteria().setBuyerPartyId(partyId);
		getTfItemSearchCriteria().setBuyerPartyId(partyId);
		if (partyId != null) {
    		_buyerPartyIdSetByInternetLogin = false;
			setSufficientCriteria(true);
		}
	}

	@Deprecated
	public Long getAccountNumber() {
		if (getCoiItemSearchCriteria().getLicenseeAccount() == null) {
			return null;
		} else {		
			return Long.valueOf(getCoiItemSearchCriteria().getLicenseeAccount());
		}
	}
	
	@Deprecated
	public void setAccountNumber(Long number) {
		if (number != null) {
			getCoiItemSearchCriteria().setLicenseeAccount(number.toString());
			getTfItemSearchCriteria().setLicenseeAccount(number.toString());
		} else {
			getCoiItemSearchCriteria().setLicenseeAccount(null);			
			getTfItemSearchCriteria().setLicenseeAccount(null);
		}
	}

	public Long getConfirmNumber() {
		return getCoiItemSearchCriteria().getConfirmNumber();
	}

	public void setConfirmNumber(Long number) {
		getCoiItemSearchCriteria().setConfirmNumber(number);
		getTfItemSearchCriteria().setConfirmNumber(number);
		if (number != null) {
			setSufficientCriteria(true);
			setMergeResults(false);
		} else {
			setMergeResults(true);
		}
//		if (getInvoiceNumber()==null && getConfirmNumber()==null) {
//			setMergeResults(true);
//		} else {
//			setMergeResults(false);
//		}
	}

	public Long getItemId() {
		return getCoiItemSearchCriteria().getItemId();
	}

	public void setItemId(Long id) {
		getCoiItemSearchCriteria().setItemId(id);
		getTfItemSearchCriteria().setItemId(id);
		if (id != null) {
			setSufficientCriteria(true);
		}
	}

	@Deprecated
	public Date getSearchFromStartOfUseDtm() {
		return getCoiItemSearchCriteria().getUseBegDate();
	}

	@Deprecated
	public void setSearchFromStartOfUseDtm(Date fromStartOfUseDtm) {
		getCoiItemSearchCriteria().setUseBegDate(fromStartOfUseDtm);
	}

	@Deprecated
	public Date getSearchToStartOfUseDtm() {
		return getCoiItemSearchCriteria().getUseEndDate();
	}

	@Deprecated
	public void setSearchToStartOfUseDtm(Date toStartOfUseDtm) {
		getCoiItemSearchCriteria().setUseEndDate(toStartOfUseDtm);
	}

	@Deprecated
	public Date getSearchFromOrderDtm() {
		return getCoiItemSearchCriteria().getOrderBegDate();
	}

	@Deprecated
	public void setSearchFromOrderDtm(Date fromOrderDtm) {
		getCoiItemSearchCriteria().setOrderBegDate(fromOrderDtm);
	}

	@Deprecated
	public Date getSearchToOrderDtm() {
		return getCoiItemSearchCriteria().getOrderEndDate();
	}

	@Deprecated
	public void setSearchToOrderDtm(Date toOrderDtm) {
		getCoiItemSearchCriteria().setOrderEndDate(toOrderDtm);
	}

	public int getSearchFromRow() {
		return getCoiItemSearchCriteria().getSearchFromRow();
	}

	public int getSearchCoiFromRow() {
		return getCoiItemSearchCriteria().getSearchFromRow();
	}

	public int getSearchTfFromRow() {
		return getTfItemSearchCriteria().getSearchFromRow();
	}

	public void setSearchFromRow(int searchFromRow) {
		getCoiItemSearchCriteria().setSearchFromRow(searchFromRow);
		getTfItemSearchCriteria().setSearchFromRow(searchFromRow);
	}

	public void setSearchCoiFromRow(int searchFromRow) {
		getCoiItemSearchCriteria().setSearchFromRow(searchFromRow);
	}

	public void setSearchTfFromRow(int searchFromRow) {
		getTfItemSearchCriteria().setSearchFromRow(searchFromRow);
	}

	public int getSearchToRow() {
		return getCoiItemSearchCriteria().getSearchToRow();
	}

	public int getSearchCoiToRow() {
		return getCoiItemSearchCriteria().getSearchToRow();
	}

	public int getSearchTfToRow() {
		return getTfItemSearchCriteria().getSearchToRow();
	}

	public void setSearchToRow(int searchToRow) {
		getCoiItemSearchCriteria().setSearchToRow(searchToRow);
		getTfItemSearchCriteria().setSearchToRow(searchToRow);
	}

	public void setSearchCoiToRow(int searchToRow) {
		getCoiItemSearchCriteria().setSearchToRow(searchToRow);
	}

	public void setSearchTfToRow(int searchToRow) {
		getTfItemSearchCriteria().setSearchToRow(searchToRow);
	}
	
	public String getLicenseeName() {
		return getCoiItemSearchCriteria().getLicenseeName();
	}

	public void setLicenseeName(String licenseeName) {
		getCoiItemSearchCriteria().setLicenseeName(licenseeName);
		getTfItemSearchCriteria().setLicenseeName(licenseeName);
	}

	public String getLicenseeAccount() {
		return getCoiItemSearchCriteria().getLicenseeAccount();
	}

	public void setLicenseeAccount(String licenseeAccount) {
		if (licenseeAccount != null) {
			setSufficientCriteria(true);
		}
		getCoiItemSearchCriteria().setLicenseeAccount(licenseeAccount);
		getTfItemSearchCriteria().setLicenseeAccount(licenseeAccount);
	}

	public String getInvoiceNumber() {
		return getCoiItemSearchCriteria().getInvoiceNumber();
	}

	public void setInvoiceNumber(String invoiceNumber) {
		if (invoiceNumber != null) {
			setSufficientCriteria(true);
		} 
//		if (getInvoiceNumber()==null && getConfirmNumber()==null) {
//			setMergeResults(true);
//		} else {
//			setMergeResults(false);
//		}
		
		getCoiItemSearchCriteria().setInvoiceNumber(invoiceNumber);
		getTfItemSearchCriteria().setInvoiceNumber(invoiceNumber);
	}

	public String getRhRefNum() {
		return getCoiItemSearchCriteria().getRhRefNum();
	}

	public void setRhRefNum(String rhRefNum) {
		if (rhRefNum != null) {
			setSufficientCriteria(true);
		}
		getCoiItemSearchCriteria().setRhRefNum(rhRefNum);
		getTfItemSearchCriteria().setRhRefNum(rhRefNum);
	}

	public String getRightAvailabilityCd() {
		return getCoiItemSearchCriteria().getRightAvailabilityCd();
	}

	public void setRightAvailabilityCd(String rightAvailabilityCd) {
		getCoiItemSearchCriteria().setRightAvailabilityCd(rightAvailabilityCd);
//		getTfItemSearchCriteria().set.setRightAvailabilityCd(rightAvailabilityCd);
	}

	public String getRightSourceCd() {
		return getCoiItemSearchCriteria().getRightSourceCd();
	}

	public void setRightSourceCd(String rightSourceCd) {
		getCoiItemSearchCriteria().setRightSourceCd(rightSourceCd);
//		getTfItemSearchCriteria().set.setRightAvailabilityCd(rightAvailabilityCd);
	}
	
	public String getTouName() {
		return getCoiItemSearchCriteria().getTouName();
	}

	public void setTouName(String touName) {
		getCoiItemSearchCriteria().setTouName(touName);
	}

	public String getExternalOrderId() {
		return getCoiItemSearchCriteria().getExternalOrderId();
	}

	public void setProductCode(String productCode) {
		getCoiItemSearchCriteria().setProductCode(productCode);
		
		if (productCode == null) {
			getTfItemSearchCriteria().setProduct(null);
			return;
		}
		
		setValidTfCriteria(false);
		getTfItemSearchCriteria().setProduct(null);	
		for (ProductEnum productEnum : ProductEnum.values()) {
			if (productEnum.name().equalsIgnoreCase(productCode)) {
				getTfItemSearchCriteria().setProduct(productEnum);	
				setValidTfCriteria(true);
				break;
			}
		}
	}

	public void setProductCodeForId(Long productId) {
		getTfItemSearchCriteria().setProduct(null);	
		setValidTfCriteria(false);
		for (ProductEnum productEnum : ProductEnum.values()) {
			if (productId != null && productId.intValue() == productEnum.getId()) {
				getTfItemSearchCriteria().setProduct(productEnum);	
//				getCoiItemSearchCriteria().setProductCode(productEnum.name());
				setValidTfCriteria(true);
				break;
			}
		}
	}

	
	public String getProductCode() {
		return getCoiItemSearchCriteria().getProductCode();
	}
	
	public void setExternalOrderId(String externalOrderId) {
		getCoiItemSearchCriteria().setExternalOrderId(externalOrderId);
		if (externalOrderId != null) {
			setSufficientCoiCriteria(true);		
			try {
				Long orderHeaderId = new Long(externalOrderId);
				getTfItemSearchCriteria().setOrderHeaderId(orderHeaderId);		
				setSufficientTfCriteria(true);
			} catch (Exception e) {	}
		} else {
			getTfItemSearchCriteria().setOrderHeaderId(null);		
		}
		
	}

	public Date getOrderBegDate() {
		return getCoiItemSearchCriteria().getOrderBegDate();
	}

	public void setOrderBegDate(Date orderBegDate) {
		getCoiItemSearchCriteria().setOrderBegDate(orderBegDate);
		getTfItemSearchCriteria().setOrderBegDate(orderBegDate);
		if (orderBegDate != null) {
			setSufficientCriteria(true);
		}
	}

	public Date getOrderEndDate() {
		return getCoiItemSearchCriteria().getOrderEndDate();
	}

	public void setOrderEndDate(Date orderEndDate) {
		getCoiItemSearchCriteria().setOrderEndDate(orderEndDate);
		getTfItemSearchCriteria().setOrderEndDate(orderEndDate);
		if (orderEndDate != null) {
			setSufficientCriteria(true);
		}
	}

	public Long getJobTicketNumber() {
		return getCoiItemSearchCriteria().getJobTicketNumber();
	}

	public void setJobTicketNumber(Long jobTicketNumber) {
		if (jobTicketNumber != null) {
			setSufficientCoiCriteria(true);
			setValidTfCriteria(false);
		}
		getCoiItemSearchCriteria().setJobTicketNumber(jobTicketNumber);
	}

	public String getLcnAcctRefNum() {
		return getCoiItemSearchCriteria().getBundleAcctRefNum();
	}

	public void setLcnAcctRefNum(String lcnAcctRefNum) {
		if (lcnAcctRefNum != null) {
			setSufficientCriteria(true);
		}
		getCoiItemSearchCriteria().setBundleAcctRefNum(lcnAcctRefNum);
		getTfItemSearchCriteria().setLcnAcctRefNum(lcnAcctRefNum);
	}

	public String getLcnHdrRefNum() {
		return getCoiItemSearchCriteria().getLicenseeRefNum();
	}

	public void setLcnHdrRefNum(String lcnHdrRefNum) {
		if (lcnHdrRefNum != null) {
			setSufficientCriteria(true);
		}
		getCoiItemSearchCriteria().setBundleRefNum(lcnHdrRefNum);
		getTfItemSearchCriteria().setLcnHdrRefNum(lcnHdrRefNum);
	}

	public String getLicenseeRefNum() {
		return getCoiItemSearchCriteria().getLicenseeRefNum();
	}

	public void setLicenseeRefNum(String licenseeRefNum) {
		if (licenseeRefNum != null) {
			setSufficientCriteria(true);
		}
		getCoiItemSearchCriteria().setLicenseeRefNum(licenseeRefNum);
		getTfItemSearchCriteria().setLicenseeDtlRefNum(licenseeRefNum);
	}

	public String getPoNum() {
		return getCoiItemSearchCriteria().getPoNum();
	}

	public void setPoNum(String poNum) {
		if (poNum != null) {
			setSufficientCriteria(true);
		}
		getCoiItemSearchCriteria().setPoNum(poNum);
		getTfItemSearchCriteria().setPoNum(poNum);
	}

	public Long getLastFourCc() {
		return getCoiItemSearchCriteria().getLastFourCc();
	}

	public void setLastFourCc(Long lastFourCc) {
		getCoiItemSearchCriteria().setLastFourCc(lastFourCc);
		getTfItemSearchCriteria().setLastFourCc(lastFourCc);
	}

	public String getBundleName() {
		return getCoiItemSearchCriteria().getBundleName();
	}

	public void setBundleName(String bundleName) {
		getCoiItemSearchCriteria().setBundleName(bundleName);
		getTfItemSearchCriteria().setCourseName(bundleName);
	}

	public void setBundleCourseNum(String bundleCourseNum) {
		getCoiItemSearchCriteria().setBundleCourseNum(bundleCourseNum);
//		getTfItemSearchCriteria().setset.setCourseName(bundleName);
	}
	
	public String getOrganizationName() {
		return getCoiItemSearchCriteria().getOrganizationName();
	}

	public void setOrganizationName(String organizationName) {
		getCoiItemSearchCriteria().setOrganizationName(organizationName);
		getTfItemSearchCriteria().setInstitutionName(organizationName);
	}

	public String getItemDescription() {
		return getCoiItemSearchCriteria().getItemDescription();
	}

	public void setItemDescription(String itemDescription) {
		if (itemDescription != null && itemDescription.length() > 3) {
			setSufficientCriteria(true);
		}
		getCoiItemSearchCriteria().setItemDescription(itemDescription);
		if (itemDescription != null) {
			getTfItemSearchCriteria().setTitle("%" + itemDescription + "%");		
		} else {
			getTfItemSearchCriteria().setTitle(null);
		}
	}

	public String getItemSubDescription() {
		return getCoiItemSearchCriteria().getItemSubDescription();
	}

	public void setItemSubDescription(String itemSubDescription) {
		if (itemSubDescription != null) {
			setValidTfCriteria(false);
		}
		getCoiItemSearchCriteria().setItemSubDescription(itemSubDescription);
	}
	
	public String getRightsholderName() {
		return getCoiItemSearchCriteria().getRightsholderName();
	}

	public void setRightsholderName(String rightsholderName) {
		getCoiItemSearchCriteria().setRightsholderName(rightsholderName);
		getTfItemSearchCriteria().setRightsholderName(rightsholderName);
	}

	public Long getItemSourceKey() {
		return getCoiItemSearchCriteria().getItemSourceKey(); // WR wrk inst
	}

	public void setItemSourceKey(Long itemSourceKey) {
		if (itemSourceKey != null) {
			setSufficientCoiCriteria(true);
		}
		getCoiItemSearchCriteria().setItemSourceKey(itemSourceKey);
	}

	public Long getExternalItemId() {
		return getCoiItemSearchCriteria().getExternalItemId();  //TF wrk inst
	}

	public void setExternalItemId(Long externalItemId) {
		if (externalItemId != null) {
			setSufficientCriteria(true);
		}
		getCoiItemSearchCriteria().setExternalItemId(externalItemId);
		getTfItemSearchCriteria().setExternalItemId(externalItemId);
	}

	public String getStandardNumber() {
		return getCoiItemSearchCriteria().getStandardNumber();
	}

	public void setStandardNumber(String standardNumber) {
		if (standardNumber != null) {
			setSufficientCriteria(true);
		}
		getCoiItemSearchCriteria().setStandardNumber(standardNumber);
		getTfItemSearchCriteria().setStandardNumber(standardNumber);
	}

	public OrderSource getOrderSource() {
		return getCoiItemSearchCriteria().getOrderSource();
	}

	public void setOrderSourceString(String orderSourceString) {
		this.orderSourceString = orderSourceString;

		if (orderSourceString == null) {
			return;
		}

		if (orderSourceString.equalsIgnoreCase(OrderSourceEnum.WWW.name())) {
			setOrderSourceCd(OrderSource.COPYRIGHT_DOT_COM);
			setTfOrderSourceEnum(com.copyright.data.order.OrderSourceEnum.COPYRIGHT_DOT_COM);
		} else if (orderSourceString.equalsIgnoreCase(OrderSourceEnum.DE.name())) {
			setOrderSourceCd(OrderSource.DATA_ENTRY);
			setTfOrderSourceEnum(com.copyright.data.order.OrderSourceEnum.DATA_ENTRY);
		} else if (orderSourceString.equalsIgnoreCase(OrderSourceEnum.GW.name())) {
			setOrderSourceCd(OrderSource.GATEWAY);
			setTfOrderSourceEnum(com.copyright.data.order.OrderSourceEnum.GATEWAY);
		} else if (orderSourceString.equalsIgnoreCase(OrderSourceEnum.RSP.name())) {
			setOrderSourceCd(OrderSource.RIGHTSPHERE);
			setTfOrderSourceEnum(com.copyright.data.order.OrderSourceEnum.RIGHTSPHERE);
		} else {
			setOrderSourceCd(OrderSource.UNDEFINED);
			setTfOrderSourceEnum(com.copyright.data.order.OrderSourceEnum.UNDEFINED);
		}
	
	}
	
	public void setOrderSourceCd(OrderSource orderSource) {
		getCoiItemSearchCriteria().setOrderSource(orderSource);
	}

	public void setTfOrderSourceEnum(com.copyright.data.order.OrderSourceEnum orderSourceEnum) {
		getTfItemSearchCriteria().setOrderSource(orderSourceEnum);
	}
	
	public String getInternetLogin() {
		return getCoiItemSearchCriteria().getInternetLogin();
	}

	public void setInternetLogin(String internetLogin) {
		if (internetLogin == null) {
	   		getCoiItemSearchCriteria().setInternetLogin(null);
    		getTfItemSearchCriteria().setInternetLogin(null); 
    		if (_buyerPartyIdSetByInternetLogin) {
               	getCoiItemSearchCriteria().setBuyerPartyId(null);
            	getTfItemSearchCriteria().setBuyerPartyId(null);       	    			
    		}
    		return;
		} else {
			setSufficientCriteria(true);
 		}
		
		if (_user == null || 
			(_user != null && !_user.getUsername().equalsIgnoreCase(internetLogin))) {
	        _user = UserServices.getUserByUsername(internetLogin);
		}
		        
        if (_user != null && _user.getPartyId() != null) {
        	getCoiItemSearchCriteria().setBuyerPartyId(_user.getPartyId());
        	getTfItemSearchCriteria().setBuyerPartyId(_user.getPartyId());       	
    		getCoiItemSearchCriteria().setInternetLogin(null);
    		getTfItemSearchCriteria().setInternetLogin(null);   
    		_buyerPartyIdSetByInternetLogin = true;
        } else {
           	getCoiItemSearchCriteria().setBuyerPartyId(null);
        	getTfItemSearchCriteria().setBuyerPartyId(null);       	
    		getCoiItemSearchCriteria().setInternetLogin(internetLogin);
    		getTfItemSearchCriteria().setInternetLogin(internetLogin);        	
        }
		
	}

	public String getItemUpdUser() {
		return getCoiItemSearchCriteria().getItemUpdUser();
	}

	public void setItemUpdUser(String itemUpdUser) {
		if (itemUpdUser != null) {
			setSufficientCriteria(true);
		}
		getCoiItemSearchCriteria().setItemUpdUser(itemUpdUser);
		getTfItemSearchCriteria().setItemUpdUser(itemUpdUser);
	}

	public String getResearchUserId() {
		return getCoiItemSearchCriteria().getResearchUserId();
	}

	public void setResearchUserId(String researchUserId) {
		if (researchUserId != null) {
			setSufficientCoiCriteria(true);
		}
		getCoiItemSearchCriteria().setResearchUserId(researchUserId);
//		getTfItemSearchCriteria().setResearchUserId(researchUserId);
	}

	public Date getUseBegDate() {
		return getCoiItemSearchCriteria().getUseBegDate();
	}

	public void setUseBegDate(Date useBegDate) {
		getCoiItemSearchCriteria().setUseBegDate(useBegDate);
		getTfItemSearchCriteria().setStartOfTermBegDate(useBegDate);
	}

	public Date getUseEndDate() {
		return getCoiItemSearchCriteria().getUseEndDate();
	}

	public void setUseEndDate(Date useEndDate) {
		getCoiItemSearchCriteria().setUseEndDate(useEndDate);
		getTfItemSearchCriteria().setStartOfTermEndDate(useEndDate);
	}

	public String getItemAvailabilityCd() {
		return getCoiItemSearchCriteria().getItemAvailabilityCd();
	}
	
	public Boolean getIsOpenSpecialOrde() {
		return getCoiItemSearchCriteria().getIsOpenSpecialOrder();
	}

	public void setIsOpenSpecialOrder(Boolean isOpenSpecialOrder) {
		getCoiItemSearchCriteria().setIsOpenSpecialOrder(isOpenSpecialOrder);
	}

	public void setPermissionStatus(String permissionStatus) {
		
		_permissionStatus = permissionStatus;
		
		if (permissionStatus == null) {
			return;
		}
		
		if (permissionStatus.equals(ItemConstants.PERMISSION_STATUS_CHECKING_AVAILABILITY)) {
			getCoiItemSearchCriteria().setItemAvailabilityCd(null);
			getCoiItemSearchCriteria().setIsOpenSpecialOrder(true);
			getTfItemSearchCriteria().setItemAvailabilityCd(ItemAvailabilityEnum.CONTACT_RH.getTfPermissionCd());
	 	} else if (permissionStatus.equals(ItemConstants.PERMISSION_STATUS_DENY)) {
			getCoiItemSearchCriteria().setItemAvailabilityCd(ItemAvailabilityEnum.DENY.getStandardPermissionCd());
			getCoiItemSearchCriteria().setIsOpenSpecialOrder(null);
			getTfItemSearchCriteria().setItemAvailabilityCd(ItemAvailabilityEnum.DENY.getTfPermissionCd());
	 	} else if (permissionStatus.equals(ItemConstants.PERMISSION_STATUS_CONTACT_DIRECTLY)) {
			getCoiItemSearchCriteria().setItemAvailabilityCd(ItemAvailabilityEnum.CONTACT_RH_DIRECTLY.getStandardPermissionCd());
			getCoiItemSearchCriteria().setIsOpenSpecialOrder(null);
			getTfItemSearchCriteria().setItemAvailabilityCd(ItemAvailabilityEnum.CONTACT_RH_DIRECTLY.getTfPermissionCd());
	 	} else if (permissionStatus.equals(ItemConstants.PERMISSION_STATUS_GRANT)) {
			getCoiItemSearchCriteria().setItemAvailabilityCd(ItemAvailabilityEnum.PURCHASE.getStandardPermissionCd());
			getCoiItemSearchCriteria().setIsOpenSpecialOrder(null);
			getTfItemSearchCriteria().setItemAvailabilityCd(ItemAvailabilityEnum.PURCHASE.getTfPermissionCd());
	 	} else if (permissionStatus.equals(ItemConstants.PERMISSION_STATUS_CANCELED)) {
			getCoiItemSearchCriteria().setItemAvailabilityCd(null);
			getCoiItemSearchCriteria().setIsOpenSpecialOrder(null);
			setIncludeCancelledItems(true);
			getCoiItemSearchCriteria().setBillingStatus(BillingStatusEnum.CANCELLED);
			getTfItemSearchCriteria().setBillingStatus(com.copyright.data.order.BillingStatusEnum.CANCELED);
	 	}

	}
	
	public void setItemAvailabilityCd(String itemAvailabilityCd) {
		getCoiItemSearchCriteria().setItemAvailabilityCd(itemAvailabilityCd);
		getTfItemSearchCriteria().setItemAvailabilityCd(itemAvailabilityCd);
	}

	public String getCategoryCd() {
		return getCoiItemSearchCriteria().getCategoryCd();
	}
	
	public void setCategoryCd(String categoryCd) {
		getCoiItemSearchCriteria().setCategoryCd (categoryCd);
		if (categoryCd == null) {
			return;
		}
		
		for (TouCategoryTypeEnum touCategoryTypeEnum : TouCategoryTypeEnum.values()) {
			if (categoryCd.equals(touCategoryTypeEnum.name())) {
				this.setProductCodeForId(touCategoryTypeEnum.getProductId());
				break;
			}
		}  

	}
	
	
	public String getCategoryName() {
		return getCoiItemSearchCriteria().getCategoryName();
	}

	public void setCategoryName(String categoryName) {
		getCoiItemSearchCriteria().setCategoryName (categoryName);
//		getTfItemSearchCriteria().setCategoryName (categoryName);
	}

	public String getBundleRefNum() {
		return getCoiItemSearchCriteria().getBundleRefNum();
	}

	public void setBundleRefNum(String bundleRefNum) {
		getCoiItemSearchCriteria().setBundleRefNum(bundleRefNum);
		getTfItemSearchCriteria().setLcnHdrRefNum(bundleRefNum);
	}

	public Boolean getIncludeCancelledItems() {
		return getCoiItemSearchCriteria().getIncludeCancelledItems();
	}

	public void setInvoiceBegDate(Date invoiceBegDate) {
		if (invoiceBegDate != null) {
			setSufficientCriteria(true);
		}
		getCoiItemSearchCriteria().setInvBegDate(invoiceBegDate);
		getTfItemSearchCriteria().setInvBegDate(invoiceBegDate);
	}
	
	public Date getInvoiceBegDate() {
		return getCoiItemSearchCriteria().getInvBegDate();
	}

	public void setInvoiceEndDate(Date invoiceEndDate) {
		if (invoiceEndDate != null) {
			setSufficientCriteria(true);
		}
		getCoiItemSearchCriteria().setInvEndDate(invoiceEndDate);
		getTfItemSearchCriteria().setInvEndDate(invoiceEndDate);
	}
	
	public Date getInvoiceEndDate() {
		return getCoiItemSearchCriteria().getInvEndDate();
	}

	public void setUpdateBegDate(Date updateBegDate) {
		if (updateBegDate != null) {
			setSufficientCriteria(true);
		}
		getCoiItemSearchCriteria().setUpdBegDate(updateBegDate);
		getTfItemSearchCriteria().setUpdBegDate(updateBegDate);
	}
	
	public Date getUpdateBegDate() {
		return getCoiItemSearchCriteria().getUpdBegDate();
	}

	public void setUpdateEndDate(Date updateEndDate) {
		if (updateEndDate != null) {
			setSufficientCriteria(true);
		}
		getCoiItemSearchCriteria().setUpdEndDate(updateEndDate);
		getTfItemSearchCriteria().setUpdEndDate(updateEndDate);
	}
	
	public Date getUpdateEndDate() {
		return getCoiItemSearchCriteria().getUpdEndDate();
	}
	
	public void setIncludeCancelledItems(Boolean includeCancelledItems) {
		getCoiItemSearchCriteria().setIncludeCancelledItems(includeCancelledItems);

		if (getTfItemSearchCriteria().getOrderDetailState() != null &&
			(getTfItemSearchCriteria().getOrderDetailState().equals(OrderDetailStateEnum.OPEN) ||
			 getTfItemSearchCriteria().getOrderDetailState().equals(OrderDetailStateEnum.CLOSED) ||
		 	 getTfItemSearchCriteria().getOrderDetailState().equals(OrderDetailStateEnum.CANCELED))) {
			return;
		}

		if (includeCancelledItems == null &&
			getTfItemSearchCriteria().getOrderDetailState() != null &&
			getTfItemSearchCriteria().getOrderDetailState().equals(OrderDetailStateEnum.NOT_CANCELED)) {
			getTfItemSearchCriteria().setOrderDetailState(null);			
		}				
		
		if (includeCancelledItems != null && includeCancelledItems.booleanValue()==true && 
			getTfItemSearchCriteria().getOrderDetailState() != null &&
			getTfItemSearchCriteria().getOrderDetailState().equals(OrderDetailStateEnum.NOT_CANCELED)) {
			getTfItemSearchCriteria().setOrderDetailState(null);			
		}
		
		if (includeCancelledItems != null && includeCancelledItems.booleanValue()==false) {
			getTfItemSearchCriteria().setOrderDetailState(OrderDetailStateEnum.NOT_CANCELED);			
		}

	}
	
	public void setCoiItemSearchCriteria(ItemSearchCriteria coiItemSearchCriteria) {
		this.coiItemSearchCriteria = coiItemSearchCriteria;
	}

	public void setTfItemSearchCriteria(com.copyright.data.order.ItemSearchCriteria tfItemSearchCriteria) {
		this.tfItemSearchCriteria = tfItemSearchCriteria;
	}
	
	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String orderDataSource) {
		dataSource = orderDataSource;
	}

	public String getOrderSourceString() {
		return orderSourceString;
	}
	
	public Boolean getIncludeOnlyBundledItems() {
		return getCoiItemSearchCriteria().getIncludeOnlyBundledItems();
	}
	
	public void setIncludeOnlyBundledItems( boolean onlyBundledItems ) {
		getCoiItemSearchCriteria().setIncludeOnlyBundledItems( onlyBundledItems );
	}
	
	public Boolean getIncludeOnlyUnBundledItems() {
		return getCoiItemSearchCriteria().getIncludeOnlyUnBundledItems();
	}
	
	public void setIncludeOnlyUnBundledItems( boolean onlyUnBundledItems ) {
		getCoiItemSearchCriteria().setIncludeOnlyUnBundledItems( onlyUnBundledItems );
	}
	
	public Long getBundleId() {
		return getCoiItemSearchCriteria().getBundleId();
	}
	
	public void setBundleId( Long bundleId ) {
		getCoiItemSearchCriteria().setBundleId( bundleId );
		getTfItemSearchCriteria().setConfirmNumber(bundleId);
		if (bundleId != null) {
			setSufficientCriteria(true);
		}
	}
	
	public Long getOrderHeaderId() {
		try {
		return new Long(getCoiItemSearchCriteria().getExternalOrderId());
		} catch (Exception e) {
			return null;
		}
	}

	public void setOrderHeaderId(Long orderHeaderId) {
		if (orderHeaderId != null) {
			getCoiItemSearchCriteria().setExternalOrderId(orderHeaderId.toString());			
		}
		getTfItemSearchCriteria().setOrderHeaderId(orderHeaderId);
		if (orderHeaderId != null) {
			setSufficientCriteria(true);
		}
//		getTfItemSearchCriteria().setset.setUpdEndDate(updateEndDate);
	}
	
	public String getRepubPublisher() {
		return getCoiItemSearchCriteria().getRepubPublisher();
	}

	public void setRepubPublisher(String repubPublisher) {
		getCoiItemSearchCriteria().setRepubPublisher(repubPublisher);
		getTfItemSearchCriteria().setRepubPublisher(repubPublisher);
	}
	
	public String getRepubTitle() {
		return getCoiItemSearchCriteria().getRepubTitle();
	}

	public void setRepubTitle(String repubTitle) {
		getCoiItemSearchCriteria().setRepubTitle(repubTitle);
		getTfItemSearchCriteria().setRepubWork(repubTitle);
	}

	public Date getRepubBegDate() {
		return getCoiItemSearchCriteria().getRepubBegDate();
	}

	public void setRepubBegDate(Date repubBegDate) {
		getCoiItemSearchCriteria().setRepubBegDate(repubBegDate);
		getTfItemSearchCriteria().setRepubBegDate(repubBegDate);
		if (repubBegDate != null) {
			getTfItemSearchCriteria().setProduct(ProductEnum.RLS);
		}	
	}
	
	public Date getRepubEndDate() {
		return getCoiItemSearchCriteria().getRepubEndDate();
	}

	public void setRepubEndDate(Date repubEndDate) {
		getCoiItemSearchCriteria().setRepubEndDate(repubEndDate);
		getTfItemSearchCriteria().setRepubEndDate(repubEndDate);
		if (repubEndDate != null) {
			getTfItemSearchCriteria().setProduct(ProductEnum.RLS);
		}
	}
	
	public String getRhAccountNumber() {
		return getCoiItemSearchCriteria().getRhAccountNumber();
	}

	public void setRhAccountNumber(String rhAccountNumber) {
		if (rhAccountNumber != null) {
			setSufficientCriteria(true);
		}
		getCoiItemSearchCriteria().setRhAccountNumber(rhAccountNumber);
		getTfItemSearchCriteria().setRightsholderAccount(rhAccountNumber);
	}
	
	public Long getExternalDetailId() {
		return getCoiItemSearchCriteria().getExternalDetailId();
	}

	public void setExternalDetailId(Long externalDetailId) {
		if (externalDetailId != null) {
			setSufficientCoiCriteria(true);
			setValidTfCriteria(false);
		}
		getCoiItemSearchCriteria().setExternalDetailId(externalDetailId);
//		getTfItemSearchCriteria().setItemId(externalDetailId);  // This is intended to only go after RL License Number
	}

	
	public void setOrderState(String orderState) {
		if (orderState == null || orderState.equals(ItemConstants.ORDER_STATE_ALL)) {
			getCoiItemSearchCriteria().setIncludeOnlyOpenOrders(false);
			getCoiItemSearchCriteria().setIncludeOnlyClosedOrders(false);
			getCoiItemSearchCriteria().setIncludeCancelledItems(true);			
			getTfItemSearchCriteria().setOrderDetailState(null);
		} else if (orderState.equals(ItemConstants.ORDER_STATE_OPEN)) {
			getCoiItemSearchCriteria().setIncludeOnlyOpenOrders(true);
			getCoiItemSearchCriteria().setIncludeOnlyClosedOrders(false);
			getCoiItemSearchCriteria().setIncludeCancelledItems(false);
			getTfItemSearchCriteria().setOrderDetailState(OrderDetailStateEnum.OPEN);
		} else if (orderState.equals(ItemConstants.ORDER_STATE_CLOSED)) {
			getCoiItemSearchCriteria().setIncludeOnlyOpenOrders(false);
			getCoiItemSearchCriteria().setIncludeOnlyClosedOrders(true);		
			getCoiItemSearchCriteria().setIncludeCancelledItems(true);
			getTfItemSearchCriteria().setOrderDetailState(OrderDetailStateEnum.CLOSED);
		}	
	}
	
	public Boolean getIncludeOnlyClosedOrders() {
		return getCoiItemSearchCriteria().getIncludeOnlyClosedOrders();
	}

	public void setIncludeOnlyClosedOrders(Boolean onlyClosed) {
		getCoiItemSearchCriteria().setIncludeOnlyClosedOrders(onlyClosed);
		if (onlyClosed != null && onlyClosed.booleanValue() == true) {
			getTfItemSearchCriteria().setOrderDetailState(OrderDetailStateEnum.CLOSED);
		}
	}

	public Boolean getIncludeOnlyOpenOrders() {
		return getCoiItemSearchCriteria().getIncludeOnlyOpenOrders();
	}
	
	public void setIncludeOnlyOpenOrders(Boolean onlyOpen) {
		getCoiItemSearchCriteria().setIncludeOnlyOpenOrders(onlyOpen);
		if (onlyOpen != null && onlyOpen.booleanValue() == true) {
			getTfItemSearchCriteria().setOrderDetailState(OrderDetailStateEnum.OPEN);
		}
	}

	public void setIsInvoiced(Boolean isInvoiced) {
		getCoiItemSearchCriteria().setIsInvoiced(isInvoiced);
		if (isInvoiced == null) {
			if (getTfItemSearchCriteria().getOrderDetailState() != null && 
				!getTfItemSearchCriteria().getOrderDetailState().equals(OrderDetailStateEnum.NOT_CANCELED)) {
				getTfItemSearchCriteria().setOrderDetailState(null);
				return;
			}
			return;
		}
		if (isInvoiced.booleanValue() == true) {
			getTfItemSearchCriteria().setOrderDetailState(OrderDetailStateEnum.CLOSED);			
		}
		if (isInvoiced.booleanValue() == false) {
			getTfItemSearchCriteria().setOrderDetailState(OrderDetailStateEnum.OPEN);						
		}
		
	}

	public void setIncludeOnlyAdjustableItems(Boolean includeOnlyAdjustableItems) {
		getCoiItemSearchCriteria().setIncludeOnlyAdjustableItems(includeOnlyAdjustableItems);
		if ( includeOnlyAdjustableItems != null) {
			if (includeOnlyAdjustableItems ) {
				getCoiItemSearchCriteria().setIsInvoiced(includeOnlyAdjustableItems);
			} else {
				getCoiItemSearchCriteria().setIsInvoiced(null);		
			}
		}
		if ( includeOnlyAdjustableItems != null && includeOnlyAdjustableItems ) {
			getTfItemSearchCriteria().setOrderDetailState(OrderDetailStateEnum.NOT_CANCELED);
			getTfItemSearchCriteria().setIncludeOnlyAdjustableItems(includeOnlyAdjustableItems);
			// TODO At some point need to add isIncludeOnlyAdjustableItems as a flag to the
			//      TF Search to set the following two additional values as "OR".
			//getTfItemSearchCriteria().setOrderSource(com.copyright.data.order.OrderSourceEnum.RIGHTSPHERE);
			//getTfItemSearchCriteria().setBillingStatus(com.copyright.data.order.BillingStatusEnum.CHARGED_TO_CREDIT_CARD);					
		}
	}
	
	public void setSpecialOrderUpdate(String spOrderUpdate) {
		
		if (spOrderUpdate == null) {
			getCoiItemSearchCriteria().setItemStatusCd(null);
			getTfItemSearchCriteria().setItemAvailabilityCd(null);
			return;
		}
		
		if (spOrderUpdate.equalsIgnoreCase(ItemConstants.SPECIAL_ORDER_UPDATE_AWAITING_LCN_CONFIRM)) {
			getCoiItemSearchCriteria().setItemStatusCd(ItemStatusEnum.AWAITING_LCN_CONFIRM);
			//getCoiItemSearchCriteria().setBillingStatus(BillingStatusEnum.INVOICED);			
			getTfItemSearchCriteria().setBillingStatus(com.copyright.data.order.BillingStatusEnum.AWAITING_LCN_CONFIRM);
		} else if (spOrderUpdate.equalsIgnoreCase(ItemConstants.SPECIAL_ORDER_UPDATE_AWAITING_LCN_REPLY)) {
			getCoiItemSearchCriteria().setItemStatusCd(ItemStatusEnum.AWAITING_LCN_REPLY);			
			//getTfItemSearchCriteria().setBillingStatus(com.copyright.data.order.BillingStatusEnum.AWAITING_LCN_CONFIRM);
		} else if (spOrderUpdate.equalsIgnoreCase(ItemConstants.SPECIAL_ORDER_UPDATE_AWAITING_RH)) {
			getCoiItemSearchCriteria().setItemStatusCd(ItemStatusEnum.AWAITING_RH);			
			//getTfItemSearchCriteria().setBillingStatus(com.copyright.data.order.BillingStatusEnum.AWAITING_LCN_CONFIRM);
		} 
			
	}
	
	public String getSpecialOrderUpdate() {
		
		if (getCoiItemSearchCriteria().getItemStatusCd() == null) {
			return null;
		}
		
		if (getCoiItemSearchCriteria().getItemStatusCd().equals(ItemStatusEnum.AWAITING_LCN_CONFIRM)) {
			return ItemConstants.SPECIAL_ORDER_UPDATE_AWAITING_LCN_CONFIRM;
		} else if (getCoiItemSearchCriteria().getItemStatusCd().equals(ItemStatusEnum.AWAITING_LCN_REPLY)) {
			return ItemConstants.SPECIAL_ORDER_UPDATE_AWAITING_LCN_REPLY;
		} else if (getCoiItemSearchCriteria().getItemStatusCd().equals(ItemStatusEnum.AWAITING_RH)) {
			return ItemConstants.SPECIAL_ORDER_UPDATE_AWAITING_RH;
		}
						
		return null;
			
	} 
	
	public void setBillingStatus(String billingStatus) {
		
		if (billingStatus == null) {
			getCoiItemSearchCriteria().setBillingStatus(null);
			getTfItemSearchCriteria().setBillingStatus(null);
			return;
		}
		
		if (billingStatus.equalsIgnoreCase(ItemConstants.BILLING_STATUS_INVOICED_CD)) {
			getCoiItemSearchCriteria().setBillingStatus(BillingStatusEnum.INVOICED);			
			getTfItemSearchCriteria().setBillingStatus(com.copyright.data.order.BillingStatusEnum.INVOICED);
		} else if (billingStatus.equalsIgnoreCase(ItemConstants.BILLING_STATUS_CANCELED_CD)) {
			getCoiItemSearchCriteria().setBillingStatus(BillingStatusEnum.CANCELLED);			
			getTfItemSearchCriteria().setBillingStatus(com.copyright.data.order.BillingStatusEnum.CANCELED);
		} else if (billingStatus.equalsIgnoreCase(ItemConstants.BILLING_STATUS_CHARGED_TO_CREDIT_CARD)) {
			getCoiItemSearchCriteria().setBillingStatus(BillingStatusEnum.CHARGED);			
			getTfItemSearchCriteria().setBillingStatus(com.copyright.data.order.BillingStatusEnum.CHARGED_TO_CREDIT_CARD);
		} else if (billingStatus.equalsIgnoreCase(ItemConstants.BILLING_STATUS_AWAITING_LCN_CONFIRM)) {
			getCoiItemSearchCriteria().setBillingStatus(BillingStatusEnum.AWAITING_LCN_CONFIRM);			
			getTfItemSearchCriteria().setBillingStatus(com.copyright.data.order.BillingStatusEnum.AWAITING_LCN_CONFIRM);
		} if (billingStatus.equalsIgnoreCase(ItemConstants.BILLING_STATUS_NOT_BILLED_CD)) {
			getCoiItemSearchCriteria().setBillingStatus(BillingStatusEnum.NOT_BILLED);			
			getTfItemSearchCriteria().setBillingStatus(com.copyright.data.order.BillingStatusEnum.NOT_BILLED);
		}
			
	}
	
	public String getBillingStatus() {
		
		if (getCoiItemSearchCriteria().getBillingStatus() == null) {
			return null;
		}
		
		if (getCoiItemSearchCriteria().getBillingStatus().equals(BillingStatusEnum.INVOICED)) {
			return ItemConstants.BILLING_STATUS_INVOICED_CD;
		} else if (getCoiItemSearchCriteria().getBillingStatus().equals(BillingStatusEnum.CANCELLED)) {
			return ItemConstants.BILLING_STATUS_CANCELED_CD;
		} else if (getCoiItemSearchCriteria().getBillingStatus().equals(BillingStatusEnum.CHARGED)) {
			return ItemConstants.BILLING_STATUS_CHARGED_TO_CREDIT_CARD;
		} else if (getCoiItemSearchCriteria().getBillingStatus().equals(BillingStatusEnum.NOT_BILLED)) {
			return ItemConstants.BILLING_STATUS_NOT_BILLED_CD;
		}		
		
		return null;
			
	}
	
	public Boolean getIsConfirmed() {
		return getCoiItemSearchCriteria().getIsConfirmed();
	}

	public void setIsConfirmed(Boolean isConfirmed) {
		getCoiItemSearchCriteria().setIsConfirmed(isConfirmed);
	}

	public void setSufficientCriteria(boolean isSufficientCriteria) {
		this._isSufficientTfCriteria = isSufficientCriteria;
		this._isSufficientCoiCriteria = isSufficientCriteria;
	}

	public boolean isSufficientCriteria() {
		
		if (dataSource.equals(OrderSearchCriteriaDataSourceEnum.ALL.name())) {
			if (!isSufficientCoiCriteria() && !isSufficientTfCriteria()) {
				return false;
			} else {
				return true;
			}
		} else if (dataSource.equals(OrderSearchCriteriaDataSourceEnum.OMS.name())) {
			if (!isSufficientCoiCriteria()) {
				return false;
			} else {
				return true;
			}
		} else if (dataSource.equals(OrderSearchCriteriaDataSourceEnum.TF.name())){
			if (!isSufficientTfCriteria()) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}
	
	public boolean isSufficientTfCriteria() {
		return _isSufficientTfCriteria;
	}

	public void setSufficientTfCriteria(boolean isSufficientTfCriteria) {
		this._isSufficientTfCriteria = isSufficientTfCriteria;
	}

	public boolean isSufficientCoiCriteria() {
		return _isSufficientCoiCriteria;
	}

	public void setSufficientCoiCriteria(boolean isSufficientCoiCriteria) {
		this._isSufficientCoiCriteria = isSufficientCoiCriteria;
	}

	public Boolean getIncludeHiddenOrders() {
		return getCoiItemSearchCriteria().getIncludeHiddenOrders();
	}
	
	public void setIncludeHiddenOrders(Boolean includeHiddenOrders) {
		getCoiItemSearchCriteria().setIncludeHiddenOrders(includeHiddenOrders);
//		getTfItemSearchCriteria().set
	}

	public boolean isValidTfCriteria() {
		return _validTfCriteria;
	}

	public void setValidTfCriteria(boolean tfCriteria) {
		_validTfCriteria = tfCriteria;
	}

	public boolean isShowAdjustedValues() {
		return _showAdjustedValues;
	}

	public void setShowAdjustedValues(boolean showAdjustedValues) {
		this._showAdjustedValues = showAdjustedValues;
	}

	public boolean isMergeResults() {
		return _mergeResults;
	}

	public void setMergeResults(boolean mergeResults) {
		this._mergeResults = mergeResults;
	}
	
	public String getInstructorName() {
		return getCoiItemSearchCriteria().getInstructorName();
	}

	public void setInstructorName(String instructorName) {
		getCoiItemSearchCriteria().setInstructorName( instructorName );
		// BobD - Currently not supported in TF
		getTfItemSearchCriteria().setInstructor(instructorName);
	}
}
