package com.copyright.ccc.web.actions.ordermgmt.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.copyright.ccc.business.data.OrderLicense;
import com.copyright.ccc.business.data.OrderPurchase;
import com.copyright.ccc.business.data.OrderSearchResult;
import com.copyright.ccc.business.services.order.OrderLicenseServices;
import com.copyright.ccc.business.services.order.OrderLicensesException;
import com.copyright.data.order.OrderSourceEnum;

public class OrderSearchResultWrapper implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private OrderSearchResult svcSearchResults;
	
	private OrderSearchCriteriaWrapper searchCriteria;
	
	private String searchCounts = "";
	private List<ItemWrapper> detailsList = new ArrayList<ItemWrapper>();
	private List<ConfirmationWrapper> orderListInHierarchicalOrder = new ArrayList<ConfirmationWrapper>();
	private List<ConfirmationWrapper> orderListInDetailOrder = new ArrayList<ConfirmationWrapper>();
	
	private boolean useListInDetailOrder = false;
	private boolean useDetailsList = true;
	private boolean useListInHierarchicalOrder = false;
	private boolean haveNonInvoicedDetails = false;
	private boolean haveNonCanceledDetails = false;
	private boolean haveNonRLDetails = false;
	private boolean haveDetailsStoredInTF = false;
	private boolean haveDetailsStoredInOMS = false;
	private boolean haveOpenDetails = false;
	private boolean haveGWDetails = false;
	private boolean haveAdjustedDetails = false;

	private CustomPageControl[] pageControl;

	private int detailStartingIndex = 0;

	private int pageNumber = 1;
	private int recordsPerPage = OrderSearchCriteriaWrapper.DEFAULT_RESULTS_PER_PAGE;
		
	public OrderSearchResultWrapper() {
		super();
	}
	
	public boolean isHasResults() {
		if ( svcSearchResults != null ) {
			return svcSearchResults.getOrderLicenses() != null && !svcSearchResults.getOrderLicenses().isEmpty();
		}
		return false;
	}
	
	public String getSearchCounts() {
		return searchCounts;
	}
		
	private void setSearchCounts() {
		StringBuffer buf = new StringBuffer();
		if ( isHasResults() ) {
			buf.append("Results: showing ");
		    buf.append(getSvcSearchResults().getDisplayFromRow());
		    if ( getDetailsList().size() > 1 ) {
		    	if ( getDetailsList().size() > getSvcSearchResults().getDisplayToRow() ) {
			    	buf.append("-");
		    		buf.append(getSvcSearchResults().getDisplayToRow());
		    	} else {
			    	buf.append("-");
		    		buf.append(getDetailsList().size());		    		
		    	}
		    }
		    buf.append(" of ").append(getDetailsList().size());
		    buf.append(" Detail").append((getDetailsList().size()==1?"":"s"));
		} else {
			buf.append("Unable to find any results matching specified criteria.");
		}
		searchCounts = buf.toString();
	   
	}
	public OrderSearchResultWrapper( OrderSearchResult searchResults ) {
		super();
		this.svcSearchResults = searchResults;
		if ( useDetailsList ) { setDetailsList(); }
		if ( useListInHierarchicalOrder ) { setOrderListInHierarchicalOrder(); }
		if ( useListInDetailOrder ) { setOrderListInDetailOrder(); }
		setSearchCounts();
	}
	
	public OrderSearchResultWrapper( OrderSearchResult searchResults, boolean useListInHierarchicalOrder ) {
		super();
		this.useListInHierarchicalOrder = useListInHierarchicalOrder;
		this.useDetailsList = useListInHierarchicalOrder;
		this.svcSearchResults = searchResults;
		if ( this.useDetailsList ) { setDetailsList(); }
		if ( this.useListInHierarchicalOrder ) { setOrderListInHierarchicalOrder(); }
		if ( this.useListInDetailOrder ) { setOrderListInDetailOrder(); }
		setSearchCounts();
	}
	public int reIndexDetails( int detailStartingIndex ) {
		this.detailStartingIndex = detailStartingIndex;
		if ( getDetailsList().size() > 0 ) {
			int detailIndexCounter = getDetailStartingIndex();
			for ( ItemWrapper detail : getDetailsList() ) {
				detail.setDetailIndex(detailIndexCounter++);
			}
			return getDetailsList().size() + detailStartingIndex;
		}
		return detailStartingIndex;
	}
	public OrderSearchResultWrapper( int detailStartingIndex, OrderSearchResult searchResults ) {
		super();
		this.detailStartingIndex = detailStartingIndex;
		this.svcSearchResults = searchResults;
		if ( useDetailsList ) { setDetailsList(); }
		if ( useListInHierarchicalOrder ) { setOrderListInHierarchicalOrder(); }
		if ( useListInDetailOrder ) { setOrderListInDetailOrder(); }
		setSearchCounts();
	}
	
	public OrderSearchResultWrapper( int detailStartingIndex, OrderSearchResult searchResults, boolean useListInHierarchicalOrder ) {
		super();
		this.detailStartingIndex = detailStartingIndex;
		this.useListInHierarchicalOrder = useListInHierarchicalOrder;
		this.useDetailsList = useListInHierarchicalOrder;
		this.svcSearchResults = searchResults;
		if ( this.useDetailsList ) { setDetailsList(); }
		if ( this.useListInHierarchicalOrder ) { setOrderListInHierarchicalOrder(); }
		if ( this.useListInDetailOrder ) { setOrderListInDetailOrder(); }
		setSearchCounts();
	}
	
	private BundleWrapper getBundleWrapperFor( Long bundleId ) {
		BundleWrapper bw = null;
		Long id = validLongValue(bundleId);
		if ( isGreaterThanZero(id) ) {
			if ( getSvcSearchResults().getOrderBundles() != null && !getSvcSearchResults().getOrderBundles().isEmpty() ) {
			    if ( getSvcSearchResults().getOrderBundles().containsKey(bundleId) ) {
			    	bw = new BundleWrapper( getSvcSearchResults().getOrderBundles().get(bundleId) ); 
			    }
			}
		}
		return bw;
	}
	
	private ConfirmationWrapper getConfirmationWrapperFor( Long orderHeaderNumber ) {
		ConfirmationWrapper cw = null;
		Long id = validLongValue(orderHeaderNumber);
		if ( isGreaterThanZero(id) ) {
			if ( getSvcSearchResults().getOrderPurchasesMap() != null && !getSvcSearchResults().getOrderPurchasesMap().isEmpty() ) {
			    if ( getSvcSearchResults().getOrderPurchasesMap().containsKey(orderHeaderNumber) ) {
			    	cw = new ConfirmationWrapper( getSvcSearchResults().getOrderPurchasesMap().get(orderHeaderNumber) ); 
			    }
			}
		}
		return cw;
	}
	public List<ConfirmationWrapper> getOrderListInDetailOrder() {
		return orderListInDetailOrder;
	}
	private void setOrderListInDetailOrder() {
		
		orderListInDetailOrder = new ArrayList<ConfirmationWrapper>();
		if ( getSvcSearchResults() != null ) {
			int detailIndexCounter = getDetailStartingIndex();
			Long lastOrderHeaderId = -1L;
			Long lastBundleId = -1L;
			ConfirmationWrapper currentOrderHeader = null;
			BundleWrapper currentBundle = null;
			for ( OrderLicense ol : getSvcSearchResults().getOrderLicenses() ) {
				OrderLicenseServices.getOverrideFeesForOrderLicense(ol);
				ItemWrapper currentItem = new ItemWrapper(ol);
				currentItem.setPreAdjustmentItem(OrderLicenseServices.getPreAdjustmentOrderLicense(ol));
				currentItem.setDetailIndex(detailIndexCounter++);
				Long id = validLongValue(ol.getOrderHeaderId());
//				Long id = validLongValue(ol.getPurchaseId());
				if ( isGreaterThanZero(id) && !lastOrderHeaderId.equals(id) ) {
					ConfirmationWrapper nextOrderHeader = getConfirmationWrapperFor(id);
					if ( nextOrderHeader != null ) {
						currentOrderHeader = nextOrderHeader;
						orderListInDetailOrder.add(currentOrderHeader);
						lastOrderHeaderId = id;
					} else {
						throw new IllegalArgumentException("Missing confirmation for item");
					}
				}
				currentItem.setMyConfirmation(currentOrderHeader);
				currentItem.initOrderSource();
				Long bundleId = validLongValue(ol.getBundleId());
				if ( isGreaterThanZero(bundleId) ) {
					if ( !lastBundleId.equals( bundleId ) ) {
						BundleWrapper nextBundle = getBundleWrapperFor( bundleId );
						if ( nextBundle != null ) {
							currentBundle = nextBundle;
							currentOrderHeader.getMyBundles().add(nextBundle);
							lastBundleId = bundleId;
						} else {
							throw new IllegalArgumentException("Missing bundle for item");
						}
					}
					currentItem.setMyBundle(currentBundle);
					currentBundle.getMyItems().add(currentItem);
				} else {
					currentOrderHeader.getMyItems().add(currentItem);
				}
			}
		}
	}
	public List<ConfirmationWrapper> getOrderListInHierarchicalOrder() {
		return orderListInHierarchicalOrder;
	}
	
	
	public void setOrderListInHierarchicalOrder() {
		orderListInHierarchicalOrder = new ArrayList<ConfirmationWrapper>();
		if ( getSvcSearchResults() != null ) {
			Map<Long,ConfirmationWrapper> orderMap = new HashMap<Long,ConfirmationWrapper>();
			Map<Long,BundleWrapper> bundleMap = new HashMap<Long,BundleWrapper>();
			ConfirmationWrapper currentOrderHeader = null;
			BundleWrapper currentBundle = null;
			for ( OrderLicense ol : getSvcSearchResults().getOrderLicenses() ) {
				OrderLicenseServices.getOverrideFeesForOrderLicense(ol);
				ItemWrapper currentItem = new ItemWrapper(ol);
				currentItem.setPreAdjustmentItem(OrderLicenseServices.getPreAdjustmentOrderLicense(ol));
//				Long id = validLongValue(ol.getPurchaseId());
				Long id = validLongValue(ol.getOrderHeaderId());
				if ( isGreaterThanZero(id) && !orderMap.containsKey(id) ) {
					currentOrderHeader = getConfirmationWrapperFor(id);
					orderListInHierarchicalOrder.add(currentOrderHeader);
					orderMap.put(id, currentOrderHeader);
				} else {
					currentOrderHeader = orderMap.get(id);
				}
				currentItem.setMyConfirmation(currentOrderHeader);
				currentItem.initOrderSource();
				Long bundleId = validLongValue(ol.getBundleId());
				if ( isGreaterThanZero(bundleId) && !bundleMap.containsKey(bundleId) ) {
					BundleWrapper nextBundle = getBundleWrapperFor( bundleId );
					if ( nextBundle != null ) {
						currentBundle = nextBundle;
						currentOrderHeader.addBundleWrapper(currentBundle, bundleId);
						bundleMap.put(bundleId, currentBundle);
					} 
				} else {
					currentBundle = bundleMap.get(bundleId);
				}
				currentItem.setMyBundle(currentBundle);
				if ( currentBundle == null ) {
//					currentItem.getItem().setBundleId(-1L);
					currentOrderHeader.getMyItems().add(currentItem);
				} else {
					currentBundle.getMyItems().add(currentItem);
				} 
			}
			// set detailindexes for order to be displayed
			int detailIndexCount = 0;
			for ( ConfirmationWrapper cw : orderListInHierarchicalOrder) {
				for ( BundleWrapper bw : cw.getMyBundles() ) {
					for ( ItemWrapper iw : bw.getMyItems() ) {
						iw.setDetailIndex(detailIndexCount++);
					}
				}
				for ( ItemWrapper iw : cw.getMyItems() ) {
					iw.setDetailIndex(detailIndexCount++);
				}
			}
		}
	}

	public List<ItemWrapper> getDetailsList() {
		return detailsList;
	}
	
	private void setDetailsList() {
		detailsList = new ArrayList<ItemWrapper>();
		int notInvoicedCount = 0;
		int storedInTfCount = 0;
		int storedInOmsCount = 0;
		int nonCanceledCount = 0;
		int nonRLCount = 0;
		int openDetailsCount = 0;
		int adjustedCount = 0;
		int detailIndexCount = getDetailStartingIndex();
		if ( getSvcSearchResults() != null ) {
			for ( OrderLicense ol : getSvcSearchResults().getOrderLicenses() ) {
				OrderLicenseServices.getOverrideFeesForOrderLicense(ol);
				ItemWrapper currentItem = new ItemWrapper(ol);
				currentItem.setPreAdjustmentItem(OrderLicenseServices.getPreAdjustmentOrderLicense(ol));
				currentItem.setDetailIndex(detailIndexCount++);
				Long id = validLongValue(ol.getOrderHeaderId());
//				Long id = validLongValue(ol.getPurchaseId());
				if ( isGreaterThanZero(id) ) {
					ConfirmationWrapper nextOrderHeader = getConfirmationWrapperFor(id);
					if ( nextOrderHeader != null ) {
						currentItem.setMyConfirmation(nextOrderHeader);
						currentItem.initOrderSource();
					} else {
						throw new IllegalArgumentException("Missing confirmation for item");
					}
				}
				Long bundleId = validLongValue(ol.getBundleId());
				if ( isGreaterThanZero(bundleId) ) {
					BundleWrapper nextBundle = getBundleWrapperFor( bundleId );
					if ( nextBundle != null ) {
						currentItem.setMyBundle(nextBundle);
					} else {
						currentItem.setMyBundle(null);
//						currentItem.getItem().setBundleId(-1L);
					}
				} 

				if (!currentItem.isRightslink()) {
					nonRLCount++;
				}
				
				if ( !currentItem.isCanceled()) {
					nonCanceledCount++;
				}
				
				if ( !currentItem.isInvoiced() ) { notInvoicedCount++; }
				if ( currentItem.isStoredInTF() ) {
					storedInTfCount++; 
				} else if ( currentItem.isStoredInOMS() ) { 
					storedInOmsCount++; 
				}
				// if item is invoiced or canceled or paid by credit card (which is essentially invoiced)
				// then it is not considered open
				if ( currentItem.isEditable()) {
					openDetailsCount++;
				}

				if (currentItem.getItem().getOrderSourceCd().equals(OrderSourceEnum.GATEWAY.getCode())) {
					setHaveGWDetails(true);
				}
				
				if ( currentItem.isAdjusted()) {
					adjustedCount++;
				}
				
				detailsList.add(currentItem);
			}
			setHaveNonInvoicedDetails(notInvoicedCount>0);
			setHaveDetailsStoredInOMS(storedInOmsCount>0);
			setHaveDetailsStoredInTF(storedInTfCount>0);
			setHaveNonCanceledDetails(nonCanceledCount>0);
			setHaveNonRLDetails(nonRLCount>0);
			setHaveAdjustedDetails(adjustedCount>0);
			if ( svcSearchResults.getTotalRows() > svcSearchResults.getOrderLicenses().size() ) {
				setHaveOpenDetails(checkForOffPageOpenDetailsIfNecessary(openDetailsCount));
			} else {
				setHaveOpenDetails(openDetailsCount>0);
			}
		}
		
	}

	private boolean checkForOffPageOpenDetailsIfNecessary( int openDetailsCount ) {
		if ( openDetailsCount == 0 ) {
			if ( svcSearchResults.getOrderLicenses() != null ) {
				if ( svcSearchResults.getTotalRows() > svcSearchResults.getOrderLicenses().size() ) {
					if ( svcSearchResults.getOrderPurchasesMap() != null ) {
						if ( svcSearchResults.getOrderPurchasesMap().size() == 1 ) {
							try {
								for ( Map.Entry<Long, OrderPurchase> entry : svcSearchResults.getOrderPurchasesMap().entrySet() ) {
									OrderPurchase op = entry.getValue();
									Long confirmNumber = Long.valueOf( op.getConfirmationNumber());
									return OrderLicenseServices.orderHasOpenItemsForConfirmNumber(confirmNumber);
								}
							} catch (OrderLicensesException e) {
								return false;
							}
						}
					}
				}
			}
		} else {
			return true;
		}
		return false;
	}
	
	private Long validLongValue( Long longValue ) {
		if ( longValue != null ) {
			return longValue;
		} else {
			return Long.valueOf(0L);
		}
	}
	
	private boolean isGreaterThanZero( Long longValue ) {
		if ( longValue != null ) {
			return longValue.compareTo(Long.valueOf(0L)) > 0 ;
		} else {
			return false;
		}
	}
	
	public OrderSearchResult getSvcSearchResults() {
		return svcSearchResults;
	}

	public void setSvcSearchResults(OrderSearchResult searchResults) {
		this.svcSearchResults = searchResults;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getRecordsPerPage() {
		return recordsPerPage;
	}

	public void setRecordsPerPage(int recordsPerPage) {
		this.recordsPerPage = recordsPerPage;
	}

	public boolean isHaveNonInvoicedDetails() {
		return haveNonInvoicedDetails;
	}

	public void setHaveNonInvoicedDetails(boolean haveNonInvoicedDetails) {
		this.haveNonInvoicedDetails = haveNonInvoicedDetails;
	}
	
	public void setHaveNonCanceledDetails(boolean haveNonCanceledDetails) {
		this.haveNonCanceledDetails = haveNonCanceledDetails;
	}

	public boolean isHaveNonCanceledDetails() {
		return haveNonCanceledDetails;
	}
	
	public void setHaveNonRLDetails(boolean haveNonRLDetails) {
		this.haveNonRLDetails = haveNonRLDetails;
	}

	public boolean isHaveNonRLDetails() {
		return haveNonRLDetails;
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

	public boolean isHaveGWDetails() {
		return haveGWDetails;
	}

	public void setHaveGWDetails(boolean haveGWDetails) {
		this.haveGWDetails = haveGWDetails;
	}
	
	public boolean isHaveAdjustedDetails() {
		return haveAdjustedDetails;
	}

	public void setHaveAdjustedDetails(boolean haveAdjustedDetails) {
		this.haveAdjustedDetails = haveAdjustedDetails;
	}
	
	public OrderSearchCriteriaWrapper getSearchCriteria() {
		return searchCriteria;
	}

	public void setSearchCriteria(OrderSearchCriteriaWrapper searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	public int getDetailStartingIndex() {
		return detailStartingIndex;
	}

	public void setDetailStartingIndex(int detailStartingIndex) {
		this.detailStartingIndex = detailStartingIndex;
	}

	/**
	 * @param pageControl the pageControl to set
	 */
	public void setPageControl(CustomPageControl[] pageControl) {
		this.pageControl = pageControl;
	}

	/**
	 * @return the pageControl
	 */
	public CustomPageControl[] getPageControl() {
		if (pageControl == null || pageControl[0] == null) {
			pageControl = new CustomPageControl[10];
			for (int i=0; i<pageControl.length; i++) {
				pageControl[i] = new CustomPageControl();
			}
		}
		return pageControl;
	}

}
