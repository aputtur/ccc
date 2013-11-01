package com.copyright.ccc.business.services.order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.copyright.ccc.business.data.DisplaySpec;
import com.copyright.ccc.business.data.OrderBundle;
import com.copyright.ccc.business.data.OrderLicense;
import com.copyright.ccc.business.data.OrderLicenses;
import com.copyright.ccc.business.data.OrderPurchase;
import com.copyright.ccc.business.data.OrderSearchCriteria;
import com.copyright.ccc.business.data.OrderSearchCriteriaDataSourceEnum;
import com.copyright.ccc.business.data.OrderSearchCriteriaSortColumnEnum;
import com.copyright.ccc.business.data.OrderSearchCriteriaSortDirectionEnum;
import com.copyright.ccc.business.data.OrderSearchResult;
import com.copyright.ccc.business.data.OrderSearchRowNumbers;
import com.copyright.ccc.business.security.CCUserContext;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.DisplaySpecServices;
import com.copyright.ccc.business.services.ItemAvailabilityEnum;
import com.copyright.ccc.business.services.ItemConstants;
import com.copyright.ccc.business.services.OrderDataSourceEnum;
import com.copyright.ccc.business.services.ProductEnum;
import com.copyright.ccc.business.services.RightSourceEnum;
import com.copyright.ccc.business.services.ServiceInvocationException;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.business.services.cart.PricingServices;
import com.copyright.ccc.business.services.user.User;
import com.copyright.ccc.business.services.user.UserServices;
import com.copyright.ccc.util.LogUtil;
import com.copyright.ccc.web.actions.ordermgmt.data.OrderSearchCriteriaWrapper;
import com.copyright.ccc.web.actions.ordermgmt.util.MiscUtils;
import com.copyright.ccc.web.transaction.TransactionConstants;
import com.copyright.data.order.License;
import com.copyright.data.order.LicensesPurchasesComposite;
import com.copyright.data.order.LicensesResultsType;
import com.copyright.data.order.OrderMgmtException;
import com.copyright.data.order.OrderMgmtSearchSortConstants;
import com.copyright.data.order.Purchase;
import com.copyright.service.order.OrderMgmtServiceAPI;
import com.copyright.service.order.OrderMgmtServiceFactory;
import com.copyright.svc.ldapuser.api.data.LdapUser;
import com.copyright.svc.order.api.data.Bundle;
import com.copyright.svc.order.api.data.Item;
import com.copyright.svc.order.api.data.ItemDescriptionParm;
import com.copyright.svc.order.api.data.ItemFees;
import com.copyright.svc.order.api.data.ItemParm;
import com.copyright.svc.order.api.data.ItemSearchCriteria;
import com.copyright.svc.order.api.data.ItemSearchResult;
import com.copyright.svc.order.api.data.ItemStatusEnum;
import com.copyright.svc.order.api.data.OrderConsumerContext;
import com.copyright.svc.order.api.data.OrderHeader;
import com.copyright.svc.order.api.data.Payment;
import com.copyright.svc.order.api.data.ResolutionLog;
import com.copyright.svc.order.api.data.SpecialOrderTypeEnum;
import com.copyright.svc.rights.api.data.OverrideFee;
import com.copyright.svc.rights.api.data.RightsConsumerContext;
import com.copyright.svc.rightsResolver.api.data.TouCategoryTypeEnum;



public class OrderLicenseServices {

    private static Logger LOGGER = Logger.getLogger( OrderLicenseServices.class );

    private static final String SEARCH_CURRENT_USER = "Y";
    private static final String SEARCH_ALL_USERS = "N";
    
    public static final int PURCHASE_ADJUSTMENT_LICENSES = 0;
    public static final int INVOICE_ADJUSTMENT_LICENSES = 1;
    public static final int DETAIL_ADJUSTMENT_LICENSES = 2;
    public static final int LICENSE_ADJUSTMENT_LICENSES = 3;
    
	private static final String FEE_FIELD_PER_ARTICLE_AUTHOR = "PER_ARTICLE_AUTHOR";
	private static final String FEE_FIELD_PER_ARTICLE_NON_AUTHOR = "PER_ARTICLE_NON_AUTHOR";
	private static final String FEE_FIELD_PER_CARTOON = "PER_CARTOON";
	private static final String FEE_FIELD_PER_CHART = "PER_CHART";
	private static final String FEE_FIELD_PER_EXCERPT = "PER_EXCERPT";
	private static final String FEE_FIELD_PER_FIGURE = "PER_FIGURE";
	private static final String FEE_FIELD_PER_GRAPH = "PER_GRAPH";
	private static final String FEE_FIELD_PER_ILLUSTRATION = "PER_ILLUSTRATION";
	private static final String FEE_FIELD_PER_LOGO = "PER_LOGO";
	private static final String FEE_FIELD_PER_PHOTOGRAPH = "PER_PHOTOGRAPH";
	private static final String FEE_FIELD_PER_QUOTE = "PER_QUOTE";

	private static final String FEE_FIELD_TO_30_DAYS_FEE = "TO_30_DAYS_FEE";
	private static final String FEE_FIELD_TO_180_DAYS_FEE = "TO_180_DAYS_FEE";
	private static final String FEE_FIELD_TO_365_DAYS_FEE = "TO_365_DAYS_FEE";
	private static final String FEE_FIELD_UNLIMITED_DAYS_FEE = "UNLIMITED_DAYS_FEE";
	private static final String FEE_FIELD_TO_49_RECIPIENTS_FEE = "TO_49_RECIPIENTS_FEE";
	private static final String FEE_FIELD_TO_249_RECIPIENTS_FEE = "TO_249_RECIPIENTS_FEE";
	private static final String FEE_FIELD_TO_499_RECIPIENTS_FEE = "TO_499_RECIPIENTS_FEE";
	private static final String FEE_FIELD_TO_500P_RECIPIENTS_FEE = "TO_500P_RECIPIENTS_FEE";
	
	private static Map<String,String>partnerIdMap;

    
	private static OrderSearchCriteriaWrapper searchCriteria = new OrderSearchCriteriaWrapper(
			true, true /* use default sort, for view */);

    private OrderLicenseServices() {
    }
    
    public static OrderLicenses getOrderLicensesForAdjustment(int adjustmentInquiryType, String adjustmentInquiryId) throws OrderLicensesException {
                             
                LicensesResultsType lrt = null;
                List<License> licenseList = null;
               
                String searchType;
                              
                OrderLicenses orderLicenses = new OrderLicenses(); 

                switch (adjustmentInquiryType) {
                    case PURCHASE_ADJUSTMENT_LICENSES: searchType = OrderMgmtSearchSortConstants.LICENSE_SEARCH_TYPE_CONFIRM_NUM; break;
                    case INVOICE_ADJUSTMENT_LICENSES: searchType = OrderMgmtSearchSortConstants.LICENSE_SEARCH_TYPE_INVOICE_NUMBER; break;
                    case DETAIL_ADJUSTMENT_LICENSES: searchType = OrderMgmtSearchSortConstants.LICENSE_SEARCH_TYPE_LICENSE_ID; break;
                    case LICENSE_ADJUSTMENT_LICENSES: searchType = OrderMgmtSearchSortConstants.LICENSE_SEARCH_TYPE_REF_LICENSE_ID; break;
                    default: searchType = OrderMgmtSearchSortConstants.LICENSE_SEARCH_TYPE_LICENSE_ID; }

                String searchId =adjustmentInquiryId;
                if (searchType.equalsIgnoreCase(OrderMgmtSearchSortConstants.LICENSE_SEARCH_TYPE_INVOICE_NUMBER)) {
                    searchId = formatInvoiceSearch(adjustmentInquiryId);
                }
                               
                try {
                    OrderMgmtServiceFactory orderFactory = OrderMgmtServiceFactory.getInstance();
                    OrderMgmtServiceAPI orderService = orderFactory.getService();

                    if (searchType == OrderMgmtSearchSortConstants.LICENSE_SEARCH_TYPE_REF_LICENSE_ID) {
                        lrt = orderService.getAdjustedLicensesWithPaging(searchType, searchId,  new Long[1],
                                                             OrderMgmtSearchSortConstants.LIST_TYPE_INDIVIDUAL,
                                                             OrderMgmtSearchSortConstants.LICENSE_SORT_TYPE_LICENSE_ID,
                                                             OrderMgmtSearchSortConstants.SORT_DIRECTION_ASCENDING,
                                                             OrderMgmtSearchSortConstants.ORDER_STATE_ALL_OPEN_CLOSED,
                                                             1, 10000, "N", 0, OrderMgmtSearchSortConstants.SHOW_HIDDEN_LICENSE_YES);
                    } else {
                        lrt = orderService.getLicensesWithPaging(searchType, searchId,  new Long[1],
                                                             OrderMgmtSearchSortConstants.LIST_TYPE_INDIVIDUAL,
                                                             OrderMgmtSearchSortConstants.LICENSE_SORT_TYPE_LICENSE_ID,
                                                             OrderMgmtSearchSortConstants.SORT_DIRECTION_ASCENDING,
                                                             OrderMgmtSearchSortConstants.ORDER_STATE_ALL_OPEN_CLOSED,
                                                             1, 10000, "N", 0, OrderMgmtSearchSortConstants.SHOW_HIDDEN_LICENSE_YES);                        
                    }
                   
               //         orderLicenses.clearOrderLicenseList();
                    licenseList = Arrays.asList(lrt.getLicenses());
                    orderLicenses.setReadEndRow(licenseList.size());
                    orderLicenses.setTotalRowCount(lrt.getPossibleRows().intValue());       
                } catch (OrderMgmtException omex) {
              	   LOGGER.error("Error Retrieving Order Licenses for ajd id: " + adjustmentInquiryId + LogUtil.appendableStack(omex));
                    OrderLicensesException orderLicensesException = new OrderLicensesException("Error Retrieving Order Licenses for id: " + adjustmentInquiryId, omex.getMessageCode(), omex.getCause());
                   throw orderLicensesException;
                }

                    // Set the underlying License object for each OrderLicense
                int licenseCtr = 0;
                if (licenseList != null) {
                    for (licenseCtr=0; licenseCtr < licenseList.size(); licenseCtr++) {
                        orderLicenses.addOrderLicense(licenseList.get(licenseCtr));
//                        orderLicenses.addDisplayLicense( (OrderLicenseImpl) orderLicenses.getOrderLicense(licenseCtr));           
                    }
                }

                return orderLicenses;
        }

        public static OrderSearchResult getOrderLicensesForActivityReport(String searchBy, String searchId) throws OrderLicensesException {
                        
            OrderSearchResult orderSearchResult = getOrderLicensesForActivityReport(searchBy, searchId, 1, 100000);
                    	
        	return orderSearchResult;

        }
        public static OrderSearchResult getOrderLicensesForActivityReport(String searchBy, String searchId, int fromRow, int toRow) throws OrderLicensesException {
            
        	OrderSearchResult orderSearchResult = new OrderSearchResult();
        	OrderSearchResult orderSearchResultCombined = new OrderSearchResult();
        	OrderSearchCriteria orderSearchCriteria = new OrderSearchCriteria();
//        	List<OrderReportResults> orderReportResults = new ArrayList<OrderReportResults>();
        	
        	if ( StringUtils.isEmpty(searchBy) ) {
        		throw new IllegalArgumentException("Missing searchBy (field)");
        	}

        	if ( StringUtils.isEmpty(searchId) ) {
        		throw new IllegalArgumentException("Missing searchId (values)");
        	}
        	        	
        	if ( fromRow > 0 ) { 
        		orderSearchCriteria.setSearchFromRow(fromRow);
        	}
        	if ( toRow > 0 ) { 
        		orderSearchCriteria.setSearchToRow(toRow);
        	}
        	
        	String[] invoiceNumbers = null;
        	String[] dates = null;
        	Date begDate = null;
        	Date endDate = null;
        
        	if ( StringUtils.isNotEmpty(searchId) ) {
        		invoiceNumbers = searchId.split(",");
        	}

        	if ( searchBy.equalsIgnoreCase( OrderMgmtSearchSortConstants.LICENSE_SEARCH_TYPE_INVOICE_NUMBER ) ) {
        		invoiceNumbers = searchId.split(",");
        	} else if ( searchBy.equalsIgnoreCase( OrderMgmtSearchSortConstants.LICENSE_SEARCH_TYPE_INVOICE_DATE ) ) {
        		dates = searchId.split(":");
        	} else if ( searchBy.equalsIgnoreCase( OrderMgmtSearchSortConstants.LICENSE_SEARCH_TYPE_PURCHASE_DATE ) ) {       
        		dates = searchId.split(":");
            } else if ( searchBy.equalsIgnoreCase( OrderMgmtSearchSortConstants.LICENSE_SEARCH_TYPE_START_OF_TERM ) ) {
            	dates = searchId.split(":");
            }

        	if ( dates != null && dates.length == 2 ) {
        		String[] formats = {"dd-MMM-yyyy"};
        		begDate = MiscUtils.parseDate(dates[0], formats);
        		endDate = MiscUtils.parseDate(dates[1], formats);
        	
        		if ( searchBy.equalsIgnoreCase( OrderMgmtSearchSortConstants.LICENSE_SEARCH_TYPE_INVOICE_DATE ) ) {
        			orderSearchCriteria.setInvoiceBegDate(begDate);
        			orderSearchCriteria.setInvoiceEndDate(endDate);
        		} else if ( searchBy.equalsIgnoreCase( OrderMgmtSearchSortConstants.LICENSE_SEARCH_TYPE_PURCHASE_DATE ) ) {
        			orderSearchCriteria.setOrderBegDate(begDate);
        			orderSearchCriteria.setOrderEndDate(endDate);
        		} else if ( searchBy.equalsIgnoreCase( OrderMgmtSearchSortConstants.LICENSE_SEARCH_TYPE_START_OF_TERM ) ) {
        			orderSearchCriteria.setUseBegDate(begDate);
        			orderSearchCriteria.setUseEndDate(endDate);
        		}
        	} else {
        		if ( invoiceNumbers != null && invoiceNumbers.length > 0 ) {
        			orderSearchCriteria.setInvoiceNumber(invoiceNumbers[0]);
        		} else {
        			throw new IllegalArgumentException("Missing search criteria");
        		}
        	}
        	
        	boolean useUserCriteria = true; // flag useful for testing!
        	if ( useUserCriteria ) {
	            CCUserContext userContext = UserContextService.getUserContext();
	
	            boolean hasValidAccountNumber = false;
	            if (userContext.getActiveUser().getAccountNumber() != null) {
	                 hasValidAccountNumber = true;
	            }
	                           
	            if (userContext.getActiveUser().getIsAdmin() && hasValidAccountNumber) {
	            	orderSearchCriteria.setLicenseeAccount(userContext.getActiveUser().getAccountNumber().toString());
	            } else {
	               	orderSearchCriteria.setBuyerPartyId(userContext.getActiveUser().getPartyId());
	            }
        	}
            orderSearchCriteria.addSortQualifierListForSortColumnAndDirection(OrderSearchCriteriaSortColumnEnum.SORT_BY_ORDER_DETAIL_ID, OrderSearchCriteriaSortDirectionEnum.DESC);

            if ( invoiceNumbers != null && invoiceNumbers.length > 1 ) {

            	// issue search for each invoice number collecting results
            	                
            	for ( String invNumber : invoiceNumbers ) {
            		
            		orderSearchCriteria.setInvoiceNumber(invNumber);
            		
            		orderSearchResult = searchOrderLicensesWithUser(orderSearchCriteria);
            		
            		if ( orderSearchResult != null ) {
            			for (OrderLicense orderLicense : orderSearchResult.getOrderLicenses()) {
            				orderSearchResultCombined.addOrderLicense(orderLicense);
            			}
            			for (OrderPurchase orderPurchase : orderSearchResult.getOrderPurchases()) {
            				orderSearchResultCombined.addOrderPurchase(orderPurchase);
            			}
            			for (OrderBundle orderBundle : orderSearchResult.getOrderBundles().values()) {
            				orderSearchResultCombined.addOrderBundle(orderBundle);
            			}           			
               		}
            	}
            } else {
            	
            	// run search for single invoice number or date range
            	
            	orderSearchResultCombined = searchOrderLicenses(orderSearchCriteria);
//           	OrderSearchResultWrapper wrappedResults = new OrderSearchResultWrapper( orderSearchResult );
//    			orderReportResults.add(packageResultsForReports( orderSearchResult, wrappedResults ) );
            }
           
            getUsersForPurchases(orderSearchResultCombined);
            
         	return orderSearchResultCombined;
        }

        private static OrderSearchResult searchTFOrderLicenses(OrderSearchCriteria orderSearchCriteria) throws OrderLicensesException {
  	
        	OrderSearchResult orderSearchResult = new OrderSearchResult();

        	int callFromRow = orderSearchCriteria.getOrderSearchRowNumbers().getTfStart();
        	int callToRow = orderSearchCriteria.getOrderSearchRowNumbers().getTfEnd();
        	        	        	
        	orderSearchResult.setDisplayFromRow(callFromRow);
        	orderSearchResult.setDisplayToRow(callToRow);

            LicensesPurchasesComposite lpc = null;
            List<License> licenseList = null;
            LicensesResultsType lrt = null;
        	
            try {
                OrderMgmtServiceFactory orderFactory = OrderMgmtServiceFactory.getInstance();
                OrderMgmtServiceAPI orderService = orderFactory.getService();

                lpc = orderService.getLicensesPurchasesCompositeForSearchCriteria(orderSearchCriteria.getTfItemSearchCriteria());
                 
                // orderLicenses.clearOrderLicenseList();
                lrt = lpc.getLicensesResultsType();

                licenseList = Arrays.asList(lrt.getLicenses());
//                orderLicenses.setReadEndRow(licenseList.size());
//                orderLicenses.setTotalRowCount(lrt.getPossibleRows().intValue());  	    
        	    
//                orderLicenses.clearOrderLicenseList();
 //                orderLicenses.setReadEndRow(licenseList.size());
                orderSearchResult.setTotalRows(lrt.getPossibleRows().intValue());       
            } catch (OrderMgmtException omex) {
            	LOGGER.error("Error Retrieving Order Licenses" + LogUtil.appendableStack(omex));
            	OrderLicensesException orderLicensesException = new OrderLicensesException("Error Retrieving Order Licenses. ", omex.getMessageCode(), omex.getCause());
            	throw orderLicensesException;
            }

    	    LOGGER.debug("Retrieved Licenses: " + licenseList.size());
            
            if (licenseList != null) {
            	for (License license : licenseList) {             
            		OrderLicenseImpl orderLicenseImpl = new OrderLicenseImpl();
                    orderLicenseImpl.setLicense(license);
                    orderSearchResult.addOrderLicense(orderLicenseImpl);
            	}
            }

            
            if (lpc.getPurchases() != null) {
                for (Purchase purchase : lpc.getPurchases()) {
                    	OrderPurchaseImpl orderPurchaseImpl = new OrderPurchaseImpl();
                    	orderPurchaseImpl.setPurchase(purchase);
                    	orderSearchResult.addOrderPurchase(orderPurchaseImpl);    
                    	OrderPurchaseBundleImpl orderPurchaseBundleImpl = new OrderPurchaseBundleImpl();
                    	orderPurchaseBundleImpl.setPurchase(purchase);
                    	if ( !orderPurchaseBundleImpl.getCoursePack().isEmpty() ) { 
                    		orderSearchResult.addOrderBundle(orderPurchaseBundleImpl);
                    	}
                }                        
            }

            orderSearchResult.initBundleComments();
            getUsersForPurchases(orderSearchResult);
            
          //If order is from Gateway then substitute alphanumberic Gateway id for the readable Gateway Partner Name
            setGateWayUserNames(orderSearchResult);
            
            return orderSearchResult;
     }
        	       
        private static void getUsersForPurchases(OrderSearchResult orderSearchResult) {
        	List<OrderPurchase> orderPurchaseList = orderSearchResult.getOrderPurchases();
        	TreeMap<Long, User> userMap = new TreeMap<Long, User>();
        	User purchaseUser;
        	Long byrInstLong = null;
        	
        	for (OrderPurchase orderPurchase : orderPurchaseList) {
        		
        		try {
                	
        			//TODO getByrInst() sometimes throws an exception on null to long conversion
        			byrInstLong = Long.valueOf(orderPurchase.getByrInst());
        			if (userMap.containsKey(byrInstLong)) {
        				orderPurchase.setUser(userMap.get(byrInstLong));
        			} else {
//                    byrInstLong = new Long (98); // For Dev Testing
                      purchaseUser = UserServices.getUserByPtyInst( byrInstLong );
                      userMap.put(byrInstLong, purchaseUser);
                      orderPurchase.setUser(purchaseUser);
        			}
        		}
        		catch (Exception e) {
                  LOGGER.error("PtyInst: " + byrInstLong + " Not Found from ConfNum: " + orderPurchase.getConfirmationNumber() + LogUtil.appendableStack(e));
                  purchaseUser = null;
        		}
        	}
        }
        
        private static void setGateWayUserNames(OrderSearchResult orderSearchResult) {
        	
        	List<OrderPurchase> orderPurchaseList = orderSearchResult.getOrderPurchases();
        	if (partnerIdMap == null)
        	{
        		partnerIdMap = getPartnerIDCodeMappings();
        	}
        	for (OrderPurchase orderPurchase : orderPurchaseList) {
        		OrderPurchaseImpl orderPurchaseImpl = (OrderPurchaseImpl)orderPurchase;
	        	if (orderPurchaseImpl.getOrderSourceCd().trim().equalsIgnoreCase(OrderPurchaseImpl.GateWaySourceCode) && orderPurchaseImpl.getUser() != null
	        			&& orderPurchase.getUser().getUsername() != null && !orderPurchase.getUser().getUsername().isEmpty()){
	        		if (partnerIdMap.containsKey(orderPurchase.getUser().getUsername().trim())) {
	        			orderPurchaseImpl.setBuyerUserIdentifier(partnerIdMap.get(orderPurchase.getUser().getUsername()));
	        		}
	        	}
        	}
        }
        
        public static OrderSearchResult searchCOIOrderLicenses(OrderSearchCriteria orderSearchCriteria) throws OrderLicensesException {
            
        	OrderSearchResult orderLicenseSearchResult = new OrderSearchResult();

        	int fromRow = orderSearchCriteria.getSearchFromRow();
        	int toRow = orderSearchCriteria.getSearchToRow();
        	int callFromRow = orderSearchCriteria.getOrderSearchRowNumbers().getTfStart();
        	int callToRow = orderSearchCriteria.getOrderSearchRowNumbers().getTfEnd();
        	
        	orderLicenseSearchResult.setDisplayFromRow(fromRow);
        	orderLicenseSearchResult.setDisplayToRow(toRow);
        	
        	ItemSearchCriteria itemSearchCriteria = initItemSearchCriteriaFromOrderSearchCriteria (orderSearchCriteria);
        	
    	    ItemSearchResult itemSearchResult = ServiceLocator.getOrderService().searchOrdersByCriteria(new OrderConsumerContext(), itemSearchCriteria);
       	    LOGGER.debug("Got itemSearchResult: " + itemSearchResult.getTotalCount());
      	    LOGGER.debug("Got Licenses: " + itemSearchResult.getItemList().size());
     	    LOGGER.debug("Got Bundles: " + itemSearchResult.getBundleMap().values().size());
      	        	    
        	
        	initOrderSearchResultFromItemSearchResult(orderLicenseSearchResult, itemSearchResult);
        	
            return orderLicenseSearchResult;
        }

/*        public static OrderSearchResult searchOriginalOrderLicensesForConfirmationNumber(Long orderConfirmationNumber) throws OrderLicensesException {
            
        	OrderSearchResult orderSearchResult = searchOrderLicensesForConfirmationNumber(orderConfirmationNumber);

        	if (orderSearchResult == null) {
        		return orderSearchResult;
        	}
        	
        	for (OrderLicense orderLicense : orderSearchResult.getOrderLicenses()) {
        		if (orderLicense.getOrderDataSource()==OrderDataSourceEnum.OMS.getOrderDataSourceId().intValue() &&
        			orderLicense.getRightSourceCd().equals(RightSourceEnum.TF.name()) &&
        			orderLicense.isAdjusted()){
        			List<Item> itemHistory = ServiceLocator.getOrderService().getItemHistory(new OrderConsumerContext(), orderLicense.getID());
        			SortedMap<Long,Item> sortedItemHistoryMap=new TreeMap<Long, Item>();
        			for (Item item : itemHistory) {
        				sortedItemHistoryMap.put(item.getItemVersion(), item);
        			}
        			List<Item> sortedItemHistoryList = new ArrayList<Item>(sortedItemHistoryMap.values()); 
        			for (int i=sortedItemHistoryList.size()-1; i > -1; i--) {
        				Item historyItem = sortedItemHistoryList.get(i);
   						OrderItemImpl historyOrderItemImpl = new OrderItemImpl();
   						OrderItemImpl orderItemImpl = (OrderItemImpl) orderLicense;
   						historyOrderItemImpl.setItem(historyItem);
   						if (!historyOrderItemImpl.isAdjusted()) {
    						orderItemImpl.getItem().setAllFees(historyItem.getAllFees());
        					if (orderItemImpl.getProductCd().equals(ProductEnum.APS.name())) {
        						orderItemImpl.setNumberOfPages(historyOrderItemImpl.getNumberOfPages());
        						orderItemImpl.setNumberOfStudents(historyOrderItemImpl.getNumberOfStudents());    
        						continue;
        					}
        					if (orderItemImpl.getProductCd().equals(ProductEnum.ECC.name())) {
        						orderItemImpl.setNumberOfPages(historyOrderItemImpl.getNumberOfPages());
        						orderItemImpl.setNumberOfStudents(historyOrderItemImpl.getNumberOfStudents());        							
        						continue;
        					}
        					if (orderItemImpl.getProductCd().equals(ProductEnum.TRS.name())) {
        						orderItemImpl.setNumberOfPages(historyOrderItemImpl.getNumberOfPages());
        						orderItemImpl.setNumberOfCopies(historyOrderItemImpl.getNumberOfCopies());        							
        						continue;
        					}
        					if (orderItemImpl.getProductCd().equals(ProductEnum.DPS.name())) {
        						orderItemImpl.setNumberOfRecipients(historyOrderItemImpl.getNumberOfRecipients());
        						orderItemImpl.setNumberOfStudents(historyOrderItemImpl.getNumberOfStudents());        							
        						continue;
        					}
        					if (orderItemImpl.getProductCd().equals(ProductEnum.RLS.name())) {
        						orderItemImpl.setNumberOfPages(historyOrderItemImpl.getNumberOfPages());
        						orderItemImpl.setCirculationDistribution(historyOrderItemImpl.getCirculationDistribution());        							
        						continue;
        					}
           				}
        			}
        		}
        	}
        	
        	return orderSearchResult;
        }
*/
        public static OrderSearchResult getOriginalOrderLicensesForOrderSearchResult (OrderSearchResult orderSearchResult) {

        	if (orderSearchResult == null) {
        		return orderSearchResult;
        	}
        	
        	for (OrderLicense orderLicense : orderSearchResult.getOrderLicenses()) {
        		if (orderLicense.getOrderDataSource()==OrderDataSourceEnum.OMS.getOrderDataSourceId().intValue() &&
        			orderLicense.getRightSourceCd().equals(RightSourceEnum.TF.name()) &&
        			orderLicense.isAdjusted()){
        			List<Item> itemHistory = ServiceLocator.getOrderService().getItemHistory(new OrderConsumerContext(), orderLicense.getID());
        			SortedMap<Long,Item> sortedItemHistoryMap=new TreeMap<Long, Item>();
        			for (Item item : itemHistory) {
        				sortedItemHistoryMap.put(item.getItemVersion(), item);
        			}
        			List<Item> sortedItemHistoryList = new ArrayList<Item>(sortedItemHistoryMap.values()); 
        			for (int i=sortedItemHistoryList.size()-1; i > -1; i--) {
        				Item historyItem = sortedItemHistoryList.get(i);
   						OrderItemImpl historyOrderItemImpl = new OrderItemImpl();
   						OrderItemImpl orderItemImpl = (OrderItemImpl) orderLicense;
   						historyOrderItemImpl.setItem(historyItem);
   						if (!historyOrderItemImpl.isAdjusted()) {
   							orderItemImpl.setItem(historyItem);
   							break;
/*   							orderItemImpl.getItem().setItemStatusCd(historyItem.getItemStatusCd());
   							orderItemImpl.getItem().setItemStatusQualifier(historyItem.getItemStatusQualifier());
   							orderItemImpl.getItem().setItemStatusDtm(historyItem.getItemStatusDtm());
    						orderItemImpl.getItem().setAllFees(historyItem.getAllFees());
        					if (orderItemImpl.getProductCd().equals(ProductEnum.APS.name())) {
        						orderItemImpl.setNumberOfPages(historyOrderItemImpl.getNumberOfPages());
        						orderItemImpl.setNumberOfStudents(historyOrderItemImpl.getNumberOfStudents());    
        						break;
        					}
        					if (orderItemImpl.getProductCd().equals(ProductEnum.ECC.name())) {
        						orderItemImpl.setNumberOfPages(historyOrderItemImpl.getNumberOfPages());
        						orderItemImpl.setNumberOfStudents(historyOrderItemImpl.getNumberOfStudents());        							
        						break;
        					}
        					if (orderItemImpl.getProductCd().equals(ProductEnum.TRS.name())) {
        						orderItemImpl.setNumberOfPages(historyOrderItemImpl.getNumberOfPages());
        						orderItemImpl.setNumberOfCopies(historyOrderItemImpl.getNumberOfCopies());        							
        						break;
        					}
        					if (orderItemImpl.getProductCd().equals(ProductEnum.DPS.name())) {
        						orderItemImpl.setNumberOfRecipients(historyOrderItemImpl.getNumberOfRecipients());
        						orderItemImpl.setNumberOfStudents(historyOrderItemImpl.getNumberOfStudents());        							
        						break;
        					}
        					if (orderItemImpl.getProductCd().equals(ProductEnum.RLS.name())) {
        						orderItemImpl.setNumberOfPages(historyOrderItemImpl.getNumberOfPages());
        						orderItemImpl.setCirculationDistribution(historyOrderItemImpl.getCirculationDistribution());        							
        						break;
        					}  */
						}
        			}
        		}
        	}
        	
        	return orderSearchResult;
        }

        public static OrderLicense getPreAdjustmentOrderLicense (OrderLicense orderLicense) {

        	if (orderLicense == null) {
        		return orderLicense;
        	}
        	
       		if (orderLicense.getOrderDataSource()==OrderDataSourceEnum.OMS.getOrderDataSourceId().intValue() &&
//       			orderLicense.getRightSourceCd().equals(RightSourceEnum.TF.name()) &&
       			orderLicense.isAdjusted()){
        			List<Item> itemHistory = ServiceLocator.getOrderService().getItemHistory(new OrderConsumerContext(), orderLicense.getID());
        			SortedMap<Long,Item> sortedItemHistoryMap=new TreeMap<Long, Item>();
        			for (Item item : itemHistory) {
        				sortedItemHistoryMap.put(item.getItemVersion(), item);
        			}
        			List<Item> sortedItemHistoryList = new ArrayList<Item>(sortedItemHistoryMap.values()); 
        			for (int i=sortedItemHistoryList.size()-1; i > -1; i--) {
        				Item historyItem = sortedItemHistoryList.get(i);
   						OrderItemImpl historyOrderItemImpl = new OrderItemImpl();
   						historyOrderItemImpl.setItem(historyItem);
   						if (!historyOrderItemImpl.isAdjusted()) {
   							return historyOrderItemImpl;
						}
        			}
        		}
        	
        	return null;
        }

        
        public static OrderSearchResult searchAllOrderLicensesForConfirmationNumber(Long orderConfirmationNumber) throws OrderLicensesException {
            
        	OrderSearchCriteria orderSearchCriteria = new OrderSearchCriteria();

        	orderSearchCriteria.setConfirmNumber(orderConfirmationNumber);
			orderSearchCriteria.setSearchToRow(ItemConstants.MAX_CONFIRM_ROWS);

        	OrderSearchResult orderSearchResult = new OrderSearchResult();
        	
//        	int fromRow = orderSearchCriteria.getDisplayFromRow();
//        	int toRow = orderSearchCriteria.getDisplayToRow();
        	       	
        	orderSearchResult.setDisplayFromRow(orderSearchCriteria.getSearchFromRow());
        	orderSearchResult.setDisplayToRow(orderSearchCriteria.getSearchToRow());

        	orderSearchResult = searchCOIOrderLicenses(orderSearchCriteria);
        	if (orderSearchResult.getTotalRows() > 0 || orderSearchResult.getOrderLicenses().size() > 0) {
        		return orderSearchResult;
        	} else {
        		orderSearchResult = searchTFOrderLicenses(orderSearchCriteria);
        		return orderSearchResult;
        	}
  	
        }
        
        public static OrderSearchResult searchOrderLicensesForDetailNumber(Long detailItemId) throws OrderLicensesException {
            
        	OrderSearchCriteria orderSearchCriteria = new OrderSearchCriteria();

        	orderSearchCriteria.setIncludeCancelledItems(true);
        	orderSearchCriteria.setItemId(detailItemId);
        	
        	OrderSearchResult orderSearchResult = new OrderSearchResult();
        	        	
        	int fromRow = 0;
        	int toRow   = 1;        	
        	
        	orderSearchResult.setDisplayFromRow(fromRow);
        	orderSearchResult.setDisplayToRow(toRow);

        	orderSearchResult = searchOrderLicenses(orderSearchCriteria);
  	
        	return orderSearchResult;
        }
        
    	public static boolean orderHasOpenItemsForConfirmNumber( Long confirmNumber ) throws OrderLicensesException {
    		
        	OrderSearchCriteria orderSearchCriteria = new OrderSearchCriteria();

        	orderSearchCriteria.setIncludeCancelledItems(false);
        	//TODO WHEN AVAILABLE ADD FOLLOWING TO CRITERIA...
        	//orderSearchCriteria.setIncludeInvoicedItems(false);
        	orderSearchCriteria.setIsInvoiced(false);
        	orderSearchCriteria.setIncludeOnlyOpenOrders(true);
        	orderSearchCriteria.setRightSourceCd(RightSourceEnum.TF.name());
        	orderSearchCriteria.setConfirmNumber(confirmNumber);
        	
        	OrderSearchResult orderSearchResult = new OrderSearchResult();
        	        	
        	int fromRow = 0;
        	int toRow   = 1;        	
        	
        	orderSearchResult.setDisplayFromRow(fromRow);
        	orderSearchResult.setDisplayToRow(toRow);

        	orderSearchResult = searchOrderLicenses(orderSearchCriteria);
        	
    		if ( orderSearchResult != null ) {
    			return orderSearchResult.getTotalRows() > 0;
    		}
    		return false;
    		
    	}
    	
    	public static Map<String,String> getPartnerIDCodeMappings() {
    		Map<String,String>partnerIDCodeMap = new HashMap<String,String>();
            try {
                OrderMgmtServiceFactory orderFactory = OrderMgmtServiceFactory.getInstance();
                OrderMgmtServiceAPI orderService = orderFactory.getService();
                partnerIDCodeMap = orderService.getPartnerIDCodeMappings();
            } catch (OrderMgmtException omex) {
       	        LOGGER.error("Error Retrieving Partner ID Code Mappings" + LogUtil.appendableStack(omex));
            }
            return partnerIDCodeMap;
    	}

        private static ItemSearchCriteria initItemSearchCriteriaFromOrderSearchCriteria (OrderSearchCriteria orderSearchCriteria) {
//        	ItemSearchCriteria itemSearchCriteria = new ItemSearchCriteria();
        	
//        	itemSearchCriteria.setConfirmNumber(orderSearchCriteria.getConfirmNumber());
//        	itemSearchCriteria.setOrderBegDate(orderSearchCriteria.getSearchFromOrderDtm());
//        	itemSearchCriteria.setOrderEndDate(orderSearchCriteria.getSearchToOrderDtm());  	

         	return orderSearchCriteria.getCoiItemSearchCriteria();
        }

        private static void initOrderSearchResultFromItemSearchResult (OrderSearchResult orderSearchResult, ItemSearchResult itemSearchResult) {
        	  	
            if (itemSearchResult.getItemList() != null) {
            	for (Item item : itemSearchResult.getItemList()) {             
            		OrderItemImpl orderItemImpl = new OrderItemImpl();
                    orderItemImpl.setItem(item);
                    if (item.getPaymentId() != null) {
                    	Payment payment = itemSearchResult.getPaymentMap().get(item.getPaymentId());
                    	orderItemImpl.setPayment(payment);
                    }
                    orderSearchResult.addOrderLicense(orderItemImpl);
                   // long confirmationNumber = orderItemImpl.getPurchaseId();
            	}
            }
            
            if (itemSearchResult.getOrderHeaderMap() != null) {
                Collection<OrderHeader> orderHeaderCollection = itemSearchResult.getOrderHeaderMap().values();         
            	for (OrderHeader orderHeader : orderHeaderCollection) {             
            		OrderHeaderImpl orderHeaderImpl = new OrderHeaderImpl();
                    orderHeaderImpl.setOrderHeader(orderHeader);
                    orderSearchResult.addOrderPurchase(orderHeaderImpl);
            	}
            }
 
            if (itemSearchResult.getBundleMap() != null) {
                Collection<Bundle> bundleCollection = itemSearchResult.getBundleMap().values();         
            	for (Bundle bundle : bundleCollection) {             
            		OrderItemBundleImpl orderItemBundleImpl = new OrderItemBundleImpl();
            		orderItemBundleImpl.setBundle(bundle);
                    orderSearchResult.addOrderBundle(orderItemBundleImpl);
            	}
            }

            orderSearchResult.setTotalRows(itemSearchResult.getTotalCount());
     
        }

        public static OrderSearchResult searchOrderLicenses(OrderSearchCriteria orderSearchCriteria) throws OrderLicensesException
        {
            
        	//OrderSearchResult orderLicenseSearchResult = new OrderSearchResult();

//        	int fromRow = orderSearchCriteria.getSearchFromRow();
//        	int toRow = orderSearchCriteria.getSearchToRow();
        	       	
        	OrderSearchRowNumbers orderSearchRowNumbers = orderSearchCriteria.getOrderSearchRowNumbers();
        	int fromRow = orderSearchCriteria.getSearchFromRow();
        	int toRow = orderSearchCriteria.getSearchToRow();
        	
        	if (fromRow == 1) {
           		orderSearchRowNumbers.setLastSearchFromRow(0);
        		orderSearchRowNumbers.setLastSearchToRow(0);
        		orderSearchRowNumbers.setLastTfEnd(0);
        		orderSearchRowNumbers.setLastCoiEnd(0);
        	}
        	
        	
        	int relFrom;
        	int relTo;

    	    LOGGER.debug("Search From Row: " + orderSearchCriteria.getSearchFromRow() + " Last From Row" + orderSearchRowNumbers.getLastSearchFromRow());    	            	
    	    LOGGER.debug("Search To Row: " + orderSearchCriteria.getSearchToRow() + " Last To Row" + orderSearchRowNumbers.getLastSearchToRow());    	            	

    	    if (orderSearchCriteria.isMergeResults()) {
    	    	if (orderSearchCriteria.getSearchFromRow() > orderSearchRowNumbers.getLastSearchFromRow()) {
    	    		orderSearchRowNumbers.setTfStart(orderSearchRowNumbers.getLastTfEnd() + 1);
    	    		orderSearchRowNumbers.setCoiStart(orderSearchRowNumbers.getLastCoiEnd() + 1);
    	    		relTo = orderSearchCriteria.getSearchToRow() - orderSearchRowNumbers.getLastSearchToRow();        		
    	    		LOGGER.debug("Rel To: " + relTo);    	            	
    	    		orderSearchRowNumbers.setTfEnd(orderSearchRowNumbers.getLastTfEnd() + relTo);
    	    		orderSearchRowNumbers.setCoiEnd(orderSearchRowNumbers.getLastCoiEnd() + relTo);
    	    		orderSearchRowNumbers.setAscendingMerge(true);
    	    	} else if (orderSearchCriteria.getSearchFromRow() < orderSearchRowNumbers.getLastSearchFromRow()) {
    	    		relFrom = orderSearchRowNumbers.getLastSearchFromRow() - orderSearchCriteria.getSearchFromRow();
    	    		LOGGER.debug("Rel From: " + relFrom);    	            	
    	    		orderSearchRowNumbers.setTfStart(orderSearchRowNumbers.getLastTfStart() - relFrom);
    	    		orderSearchRowNumbers.setCoiStart(orderSearchRowNumbers.getLastCoiStart() - relFrom);
    	    		orderSearchRowNumbers.setTfEnd(orderSearchRowNumbers.getLastTfStart() - 1);
    	    		orderSearchRowNumbers.setCoiEnd(orderSearchRowNumbers.getLastCoiStart() - 1);
    	    		orderSearchRowNumbers.setAscendingMerge(false);
    	    		if (orderSearchRowNumbers.getTfStart() < 1) orderSearchRowNumbers.setTfStart(1);
    	    		if (orderSearchRowNumbers.getCoiStart() < 1) orderSearchRowNumbers.setCoiStart(1);
    	    	} else {
    	    		orderSearchRowNumbers.setTfStart(orderSearchRowNumbers.getLastTfStart());
    	    		orderSearchRowNumbers.setTfEnd(orderSearchRowNumbers.getLastTfEnd());
    	    		orderSearchRowNumbers.setCoiStart(orderSearchRowNumbers.getLastCoiStart());
    	    		orderSearchRowNumbers.setCoiEnd(orderSearchRowNumbers.getLastCoiEnd());
    	    	}
    	    } else {
    	    	orderSearchRowNumbers.setTfStart(orderSearchCriteria.getSearchFromRow());
    	    	orderSearchRowNumbers.setTfEnd(orderSearchCriteria.getSearchToRow());
    	    	orderSearchRowNumbers.setCoiStart(orderSearchCriteria.getSearchFromRow());
    	    	orderSearchRowNumbers.setCoiEnd(orderSearchCriteria.getSearchToRow());
    	    }

    	    LOGGER.debug("Beginning TF Start: " + orderSearchCriteria.getOrderSearchRowNumbers().getTfStart() + " COI Start: " + orderSearchCriteria.getOrderSearchRowNumbers().getCoiStart());    	            	
    	    LOGGER.debug("Beginning TF End: " + orderSearchCriteria.getOrderSearchRowNumbers().getTfEnd() + " COI End: " + orderSearchCriteria.getOrderSearchRowNumbers().getCoiEnd());    	            	

        	// If greater than max, don't do it
//        	orderLicenseSearchResult.setDisplayFromRow(fromRow);
//        	orderLicenseSearchResult.setDisplayToRow(toRow);

        	// If greater than max, don't do it

           	OrderSearchResult tfOrderSearchResult;
           	OrderSearchResult coiOrderSearchResult;

    	    LOGGER.debug("DataSource " + orderSearchCriteria.getDataSource());    	            	
//    	    if (orderSearchCriteria.getDataSource() == null) {
//    	    	orderSearchCriteria.setDataSource(OrderSearchCriteriaDataSourceEnum.ALL.name());
//    	    }
    	  
    	    if (orderSearchCriteria.getItemAvailabilityCd() != null && 
    	    	orderSearchCriteria.getItemAvailabilityCd().isEmpty()) {
    	    	orderSearchCriteria.setItemAvailabilityCd(null);
    	    }
  
      	    if (orderSearchCriteria.getTouName() != null && 
      	    	orderSearchCriteria.getTouName().isEmpty()) {
    	    	orderSearchCriteria.setTouName(null);
    	    }
      	    
       		orderSearchCriteria.setSearchCoiFromRow(orderSearchRowNumbers.getCoiStart());
       		orderSearchCriteria.setSearchCoiToRow(orderSearchRowNumbers.getCoiEnd());

    	    LOGGER.debug("Search COI Licenses Starting Thread");

            SearchThread omsSearchThread = new SearchThread(SearchThread.SEARCH_TYPE_LICENSE, orderSearchCriteria);
            omsSearchThread.start();
     	    
       		orderSearchCriteria.setSearchTfFromRow(orderSearchRowNumbers.getTfStart());
    		orderSearchCriteria.setSearchTfToRow(orderSearchRowNumbers.getTfEnd());            
            
    	    if (orderSearchCriteria.isSufficientTfCriteria() &&
    	    	orderSearchCriteria.isValidTfCriteria() &&	
    	 	       (orderSearchCriteria.getDataSource().equals(OrderSearchCriteriaDataSourceEnum.ALL.name()) ||
    	         	orderSearchCriteria.getDataSource().equals(OrderSearchCriteriaDataSourceEnum.TF.name()))) {
   	     			tfOrderSearchResult = OrderLicenseServices.searchTFOrderLicenses(orderSearchCriteria);
    	    } else {
    	        	tfOrderSearchResult = new OrderSearchResult();
    	        	tfOrderSearchResult.setOrderBundles(new HashMap<Long,OrderBundle>());
    	        	tfOrderSearchResult.setOrderLicenses(new ArrayList<OrderLicense>());
    	        	tfOrderSearchResult.setOrderPurchases(new HashMap<Long, OrderPurchase>());
    	        	tfOrderSearchResult.setDisplayFromRow(0);
    	        	tfOrderSearchResult.setDisplayToRow(0);
    	        	tfOrderSearchResult.setTotalRows(0);
    	    }
		    
    	    try {
    	    	LOGGER.debug("Waiting for OMS Search results");
				omsSearchThread.join();
			} catch (InterruptedException e) {
				throw new OrderLicensesException(e);
			}
		    
			if (omsSearchThread.isError()) {
				throw omsSearchThread.getOrderLicensesException();
			}
			
			coiOrderSearchResult = omsSearchThread.getOrderSearchResult();
			
			if (tfOrderSearchResult != null) {
				LOGGER.debug("Retrieved TF Licenses: " + tfOrderSearchResult.getNumberOfDisplayLicenses() + " out of " + tfOrderSearchResult.getTotalRows());		
			}
			if (coiOrderSearchResult != null) {
			LOGGER.debug("Retrieved COI Licenses: " + coiOrderSearchResult.getNumberOfDisplayLicenses() + " out of " + coiOrderSearchResult.getTotalRows());
			}
			
       		OrderSearchResult combinedSearchResult = new OrderSearchResult();

			orderSearchCriteria.getOrderSearchRowNumbers().setTfRowsFound(tfOrderSearchResult.getTotalRows());
			if (coiOrderSearchResult == null) {
				orderSearchCriteria.getOrderSearchRowNumbers().setCoiRowsFound(0);
			}
			else {
			orderSearchCriteria.getOrderSearchRowNumbers().setCoiRowsFound(coiOrderSearchResult.getTotalRows());       	
			}
//			orderSearchCriteria.getOrderSearchRowNumbers().setCoiRowsFound(0);
			
        	orderSearchCriteria.setSearchFromRow(fromRow);
        	orderSearchCriteria.setSearchToRow(toRow);

        	boolean compareComplete = false;
        	int i = 0;
  //      	int c = orderSearchCriteria.getOrderSearchRowNumbers().getCoiStart();
        	int c = 0;
        	int cUsed = 0;
  //      	int t = orderSearchCriteria.getOrderSearchRowNumbers().getTfStart();
        	int t = 0;
        	int tUsed = 0;
          	
//           	if (orderSearchCriteria.getOrderSearchRowNumbers().getSearchFromRow() > orderSearchCriteria.getOrderSearchRowNumbers().getLastSearchFromRow()) {
//           		displayReturnStart = orderSearchCriteria.getOrderSearchRowNumbers().getSearchFromRow() - orderSearchCriteria.getOrderSearchRowNumbers().getLastSearchToRow();
//           		displayReturnEnd = orderSearchCriteria.getOrderSearchRowNumbers().getSearchToRow() - orderSearchCriteria.getOrderSearchRowNumbers().getSearchFromRow();
//           	} else if (orderSearchCriteria.getOrderSearchRowNumbers().getSearchFromRow() < orderSearchCriteria.getOrderSearchRowNumbers().getLastSearchFromRow()) {
//           		displayReturnStart = 1;
//           		displayReturnEnd = orderSearchCriteria.getOrderSearchRowNumbers().getSearchToRow() - orderSearchCriteria.getOrderSearchRowNumbers().getSearchFromRow();
//           	}
           	
           	
        	while (!compareComplete) {
       			if (c < coiOrderSearchResult.getNumberOfDisplayLicenses() && t < tfOrderSearchResult.getNumberOfDisplayLicenses()) {
       				if (compareTfCoiRowForNext(orderSearchCriteria, coiOrderSearchResult, c, tfOrderSearchResult, t).equals("C") ) {
//       					combinedSearchResult.addOrderLicense(coiOrderSearchResult.getOrderLicenseByDisplaySequence(c));
       					OrderItemImpl orderItemImpl = (OrderItemImpl) coiOrderSearchResult.getOrderLicenseByDisplaySequence(c);
       					orderItemImpl.setShowAdjustedValues(orderSearchCriteria.isShowAdjustedValues());
       					combinedSearchResult.addOrderLicense(orderItemImpl);
       					combinedSearchResult.addOrderPurchase(coiOrderSearchResult.getOrderPurchaseByDisplaySequence(c));
       					if (coiOrderSearchResult.getOrderBundleByDisplaySequence(c) != null) {
       						combinedSearchResult.addOrderBundle(coiOrderSearchResult.getOrderBundleByDisplaySequence(c));
       					}
       					c++;
       				} else { 	            	
      					combinedSearchResult.addOrderLicense(tfOrderSearchResult.getOrderLicenseByDisplaySequence(t));
       					combinedSearchResult.addOrderPurchase(tfOrderSearchResult.getOrderPurchaseByDisplaySequence(t));
       					if (tfOrderSearchResult.getOrderBundleByDisplaySequence(t) != null) {
       						combinedSearchResult.addOrderBundle(tfOrderSearchResult.getOrderBundleByDisplaySequence(t));
       					}
       					t++;
       				}
       			} else if (c < coiOrderSearchResult.getNumberOfDisplayLicenses()) {
//   					combinedSearchResult.addOrderLicense(coiOrderSearchResult.getOrderLicenseByDisplaySequence(c));
					OrderItemImpl orderItemImpl = (OrderItemImpl) coiOrderSearchResult.getOrderLicenseByDisplaySequence(c);
					orderItemImpl.setShowAdjustedValues(orderSearchCriteria.isShowAdjustedValues());
					combinedSearchResult.addOrderLicense(orderItemImpl);
       				combinedSearchResult.addOrderPurchase(coiOrderSearchResult.getOrderPurchaseByDisplaySequence(c));
   					if (coiOrderSearchResult.getOrderBundleByDisplaySequence(c) != null) {
   						combinedSearchResult.addOrderBundle(coiOrderSearchResult.getOrderBundleByDisplaySequence(c));
   					}
   					c++;
       			} else if (t < tfOrderSearchResult.getNumberOfDisplayLicenses()) {
       				combinedSearchResult.addOrderLicense(tfOrderSearchResult.getOrderLicenseByDisplaySequence(t));
       				combinedSearchResult.addOrderPurchase(tfOrderSearchResult.getOrderPurchaseByDisplaySequence(t));
   					if (tfOrderSearchResult.getOrderBundleByDisplaySequence(t) != null) {
   						combinedSearchResult.addOrderBundle(tfOrderSearchResult.getOrderBundleByDisplaySequence(t));
   					}
   					t++;
       			} else {
       				compareComplete = true;
       			}
       			i++;
       		}

        	int searchCtr = 0;
        	int searchStart = 0;
        	int searchEnd = 0;

        	if (orderSearchCriteria.isMergeResults()) {
        		if (orderSearchCriteria.getSearchFromRow() == orderSearchCriteria.getOrderSearchRowNumbers().getLastSearchFromRow()) {
        			searchCtr = orderSearchCriteria.getOrderSearchRowNumbers().getLastSearchFromRow();               	
        			searchStart = orderSearchCriteria.getOrderSearchRowNumbers().getLastSearchFromRow();
        			searchEnd = orderSearchCriteria.getSearchToRow();
        		} else {        	
        			if (orderSearchCriteria.getSearchFromRow() > orderSearchCriteria.getOrderSearchRowNumbers().getLastSearchFromRow()) {
        				searchCtr = orderSearchCriteria.getOrderSearchRowNumbers().getLastSearchToRow() + 1;               	
        				searchStart = orderSearchCriteria.getOrderSearchRowNumbers().getLastSearchToRow() + 1;
        				searchEnd = orderSearchCriteria.getSearchToRow();
        			} else {
        				searchCtr = orderSearchCriteria.getOrderSearchRowNumbers().getLastSearchFromRow() - 1;
        				searchStart = orderSearchCriteria.getOrderSearchRowNumbers().getLastSearchFromRow() - 1;
        				searchEnd = orderSearchCriteria.getSearchFromRow();
        			}
        		}
        	} else {
           		searchCtr = fromRow;
           		searchStart = fromRow;
           		searchEnd = toRow;
           	}

    	    LOGGER.debug("Search Start: " + searchStart + " Search End: " + searchEnd);    	            	
           	
           	
        	OrderSearchResult returnSearchResult = new OrderSearchResult();
        	boolean rowsRemain = true;
        	int ds = 0;
        	while (searchCtr <= searchEnd && ds < combinedSearchResult.getOrderLicenses().size()) {
        		if (combinedSearchResult.getOrderLicenseByDisplaySequence(ds).getOrderDataSource() == OrderDataSourceEnum.OMS.getOrderDataSourceId().intValue()) {
        			cUsed++;
        		} else {
        			tUsed++;
        		}
        		
        		if (searchCtr >= orderSearchCriteria.getSearchFromRow()) {
//        				|| orderSearchCriteria.getConfirmNumber() != null) {
        			returnSearchResult.addOrderLicense(combinedSearchResult.getOrderLicenseByDisplaySequence(ds));
        			returnSearchResult.addOrderPurchase(combinedSearchResult.getOrderPurchaseByDisplaySequence(ds));
   					if (combinedSearchResult.getOrderBundleByDisplaySequence(ds) != null) {
   						returnSearchResult.addOrderBundle(combinedSearchResult.getOrderBundleByDisplaySequence(ds));        			
   					}
        		}
        		
        		if (searchCtr == orderSearchCriteria.getSearchFromRow()) {
        			orderSearchCriteria.getOrderSearchRowNumbers().setTfStart((orderSearchCriteria.getOrderSearchRowNumbers().getTfStart() + tUsed)-1);
        			orderSearchCriteria.getOrderSearchRowNumbers().setCoiStart((orderSearchCriteria.getOrderSearchRowNumbers().getCoiStart() + cUsed)-1);
                	if (orderSearchRowNumbers.getTfStart() < 1) orderSearchRowNumbers.setTfStart(1);
                	if (orderSearchRowNumbers.getCoiStart() < 1) orderSearchRowNumbers.setCoiStart(1);
        		}
       		        		
        		searchCtr++; ds++;
        	}
        	
        	searchCtr--;
        	
    	    LOGGER.debug("TF Used: " + tUsed + " COI Used: " + cUsed);    	            	
        	
        	orderSearchCriteria.getOrderSearchRowNumbers().setTfEnd(((orderSearchCriteria.getOrderSearchRowNumbers().getLastTfEnd() + tUsed)));
			orderSearchCriteria.getOrderSearchRowNumbers().setCoiEnd(((orderSearchCriteria.getOrderSearchRowNumbers().getLastCoiEnd() + cUsed)));       	

    	    LOGGER.debug("Ending TF Start: " + orderSearchCriteria.getOrderSearchRowNumbers().getTfStart() + " COI Start: " + orderSearchCriteria.getOrderSearchRowNumbers().getCoiStart());    	            	
    	    LOGGER.debug("Ending TF End: " + orderSearchCriteria.getOrderSearchRowNumbers().getTfEnd() + " COI End: " + orderSearchCriteria.getOrderSearchRowNumbers().getCoiEnd());    	            	

			orderSearchCriteria.getOrderSearchRowNumbers().setLastTfEnd(orderSearchCriteria.getOrderSearchRowNumbers().getTfEnd());
			orderSearchCriteria.getOrderSearchRowNumbers().setLastCoiEnd(orderSearchCriteria.getOrderSearchRowNumbers().getCoiEnd());       	
			orderSearchCriteria.getOrderSearchRowNumbers().setLastTfStart(orderSearchCriteria.getOrderSearchRowNumbers().getTfStart());
			orderSearchCriteria.getOrderSearchRowNumbers().setLastCoiStart(orderSearchCriteria.getOrderSearchRowNumbers().getCoiStart());       	

			orderSearchCriteria.getOrderSearchRowNumbers().setLastSearchFromRow(orderSearchCriteria.getSearchFromRow());       	
			orderSearchCriteria.getOrderSearchRowNumbers().setLastSearchToRow(orderSearchCriteria.getSearchToRow());       	

        	returnSearchResult.setTotalRows(coiOrderSearchResult.getTotalRows() + tfOrderSearchResult.getTotalRows());
			
			returnSearchResult.setOrderSearchCriteria(orderSearchCriteria);

        	returnSearchResult.setDisplayFromRow(searchStart);
        	returnSearchResult.setDisplayToRow(searchStart + (searchCtr - 1));
        	returnSearchResult.initLicenseConfirmNumbers();
        	        	
            return returnSearchResult;
        
        }

        private static String getCoiGreaterReturnValue (OrderSearchCriteria orderSearchCriteria) {
        	
        	if (orderSearchCriteria.getSortDirection().getIntVaue() == OrderSearchCriteriaSortDirectionEnum.ASC.getIntVaue()) {
				return "T";	
			} else {
				return "C";
			}
        }

        private static String getTfGreaterReturnValue (OrderSearchCriteria orderSearchCriteria) {
        	
        	if (orderSearchCriteria.getSortDirection().getIntVaue() == OrderSearchCriteriaSortDirectionEnum.ASC.getIntVaue()) {
				return "C";	
			} else {
				return "T";
			}
        }

        
        private static String compareTfCoiRowForNext(OrderSearchCriteria orderSearchCriteria, OrderSearchResult coiOrderSearchResult, int coiRow, OrderSearchResult tfOrderSearchResult, int tfRow) {
    		if (orderSearchCriteria.getSortByColumn().getDisplayOrder() == OrderSearchCriteriaSortColumnEnum.SORT_BY_DEFAULT.getDisplayOrder()) {
//    			if (coiOrderSearchResult.getOrderLicenseByDisplaySequence(coiRow).getID() >= tfOrderSearchResult.getOrderLicenseByDisplaySequence(tfRow).getID()) {
    			if (coiOrderSearchResult.getOrderPurchaseByDisplaySequence(coiRow).getOrderDate() == null) {
    				if (tfOrderSearchResult.getOrderPurchaseByDisplaySequence(tfRow).getOrderDate() == null) {
    					return getCoiGreaterReturnValue(orderSearchCriteria);
    				} else {
    					return getTfGreaterReturnValue(orderSearchCriteria);
    				}
    			} else if (tfOrderSearchResult.getOrderPurchaseByDisplaySequence(tfRow).getOrderDate() == null) {
    				return getCoiGreaterReturnValue(orderSearchCriteria);
    			} else if (coiOrderSearchResult.getOrderPurchaseByDisplaySequence(coiRow).getOrderDate().compareTo(tfOrderSearchResult.getOrderPurchaseByDisplaySequence(tfRow).getOrderDate()) >= 0) {
    				return getCoiGreaterReturnValue(orderSearchCriteria);
    			} else {
					return getTfGreaterReturnValue(orderSearchCriteria);
    			}
    		} else if (orderSearchCriteria.getSortByColumn().getDisplayOrder() == OrderSearchCriteriaSortColumnEnum.SORT_BY_ORDER_DATE.getDisplayOrder()) {
    			if (coiOrderSearchResult.getOrderPurchaseByDisplaySequence(coiRow).getOrderDate() == null) {
    				if (tfOrderSearchResult.getOrderPurchaseByDisplaySequence(tfRow).getOrderDate() == null) {
    					return getCoiGreaterReturnValue(orderSearchCriteria);
    				} else {
    					return getTfGreaterReturnValue(orderSearchCriteria);
    				}
    			} else if (tfOrderSearchResult.getOrderPurchaseByDisplaySequence(tfRow).getOrderDate() == null) {
    				return getCoiGreaterReturnValue(orderSearchCriteria);
    			} else if (coiOrderSearchResult.getOrderPurchaseByDisplaySequence(coiRow).getOrderDate().compareTo(tfOrderSearchResult.getOrderPurchaseByDisplaySequence(tfRow).getOrderDate()) >= 0) {
    				return getCoiGreaterReturnValue(orderSearchCriteria);
    			} else {
    				return getTfGreaterReturnValue(orderSearchCriteria);
    			}
    		} else if (orderSearchCriteria.getSortByColumn().getDisplayOrder() == OrderSearchCriteriaSortColumnEnum.SORT_BY_ORDER_DETAIL_ID.getDisplayOrder()) {
        		if (coiOrderSearchResult.getOrderLicenseByDisplaySequence(coiRow).getID() >= tfOrderSearchResult.getOrderLicenseByDisplaySequence(tfRow).getID()) {
	   				return getCoiGreaterReturnValue(orderSearchCriteria);
        		} else {
        			return getTfGreaterReturnValue(orderSearchCriteria);
        		}
    		} else if (orderSearchCriteria.getSortByColumn().getDisplayOrder() == OrderSearchCriteriaSortColumnEnum.SORT_BY_CONFIRM_NUMBER.getDisplayOrder()) {
    			if (coiOrderSearchResult.getOrderPurchaseByDisplaySequence(coiRow).getConfirmationNumber() >= tfOrderSearchResult.getOrderPurchaseByDisplaySequence(tfRow).getConfirmationNumber()) {
   	   				return getCoiGreaterReturnValue(orderSearchCriteria);
        		} else {
        			return getTfGreaterReturnValue(orderSearchCriteria);
        		}
    		} else if (orderSearchCriteria.getSortByColumn().getDisplayOrder() == OrderSearchCriteriaSortColumnEnum.SORT_BY_START_OF_TERM.getDisplayOrder()) {
    			boolean coiValueIsNull = false;
    			if (coiOrderSearchResult.getOrderBundleByDisplaySequence(coiRow) == null) {
    				coiValueIsNull = true;
    			} else if (coiOrderSearchResult.getOrderBundleByDisplaySequence(coiRow).getStartOfTerm() == null) {
    				coiValueIsNull = true;
    			}
    			boolean tfValueIsNull = false;
    			if (tfOrderSearchResult.getOrderBundleByDisplaySequence(tfRow) == null) {
    				tfValueIsNull = true;
    			} else if (tfOrderSearchResult.getOrderBundleByDisplaySequence(tfRow).getStartOfTerm() == null) {
    				tfValueIsNull = true;
    			}    			
    			if (coiValueIsNull) {
    				if (tfValueIsNull) {
    					return getCoiGreaterReturnValue(orderSearchCriteria);
    				} else {
    					return getTfGreaterReturnValue(orderSearchCriteria);
    				}
    			} else if (tfValueIsNull) {
    				return getCoiGreaterReturnValue(orderSearchCriteria);
    			} else if (coiOrderSearchResult.getOrderBundleByDisplaySequence(coiRow).getStartOfTerm().compareTo(tfOrderSearchResult.getOrderBundleByDisplaySequence(tfRow).getStartOfTerm()) >= 0) {
    				return getCoiGreaterReturnValue(orderSearchCriteria);
    			} else {
					return getTfGreaterReturnValue(orderSearchCriteria);
    			}
    		} else if (orderSearchCriteria.getSortByColumn().getDisplayOrder() == OrderSearchCriteriaSortColumnEnum.SORT_BY_INVOICE_NUMBER.getDisplayOrder()) {
     			if (coiOrderSearchResult.getOrderLicenseByDisplaySequence(coiRow).getInvoiceId() == null) {
    				if (tfOrderSearchResult.getOrderLicenseByDisplaySequence(tfRow).getInvoiceId() == null) {
    					return getCoiGreaterReturnValue(orderSearchCriteria);
    				} else {
    					return getTfGreaterReturnValue(orderSearchCriteria);
    				}
    			} else if (tfOrderSearchResult.getOrderLicenseByDisplaySequence(tfRow).getInvoiceId() == null) {
    				return getCoiGreaterReturnValue(orderSearchCriteria);
    			} else if (coiOrderSearchResult.getOrderLicenseByDisplaySequence(coiRow).getInvoiceId().compareTo(tfOrderSearchResult.getOrderLicenseByDisplaySequence(tfRow).getInvoiceId()) >= 0) {
    				return getCoiGreaterReturnValue(orderSearchCriteria);
    			} else {
					return getTfGreaterReturnValue(orderSearchCriteria);
    			}

    		} else if (orderSearchCriteria.getSortByColumn().getDisplayOrder() == OrderSearchCriteriaSortColumnEnum.SORT_BY_PERMISSION_TYPE.getDisplayOrder()) {
     			if (coiOrderSearchResult.getOrderLicenseByDisplaySequence(coiRow).getCategoryName() == null) {
    				if (tfOrderSearchResult.getOrderLicenseByDisplaySequence(tfRow).getCategoryName() == null) {
    					return getCoiGreaterReturnValue(orderSearchCriteria);
    				} else {
    					return getTfGreaterReturnValue(orderSearchCriteria);
    				}
    			} else if (tfOrderSearchResult.getOrderLicenseByDisplaySequence(tfRow).getCategoryName() == null) {
    				return getCoiGreaterReturnValue(orderSearchCriteria);
    			} else if (coiOrderSearchResult.getOrderLicenseByDisplaySequence(coiRow).getCategoryName().compareTo(tfOrderSearchResult.getOrderLicenseByDisplaySequence(tfRow).getCategoryName()) >= 0) {
    				return getCoiGreaterReturnValue(orderSearchCriteria);
    			} else {
					return getTfGreaterReturnValue(orderSearchCriteria);
    			}

    		} else if (orderSearchCriteria.getSortByColumn().getDisplayOrder() == OrderSearchCriteriaSortColumnEnum.SORT_BY_PERMISSION_STATUS.getDisplayOrder()) {
     			if (coiOrderSearchResult.getOrderLicenseByDisplaySequence(coiRow).getItemAvailabilityDescription() == null) {
    				if (tfOrderSearchResult.getOrderLicenseByDisplaySequence(tfRow).getItemAvailabilityDescription() == null) {
    					return getCoiGreaterReturnValue(orderSearchCriteria);
    				} else {
    					return getTfGreaterReturnValue(orderSearchCriteria);
    				}
    			} else if (tfOrderSearchResult.getOrderLicenseByDisplaySequence(tfRow).getItemAvailabilityDescription() == null) {
    				return getCoiGreaterReturnValue(orderSearchCriteria);
    			} else if (coiOrderSearchResult.getOrderLicenseByDisplaySequence(coiRow).getItemAvailabilityDescription().compareTo(tfOrderSearchResult.getOrderLicenseByDisplaySequence(tfRow).getItemAvailabilityDescription()) >= 0) {
    				return getCoiGreaterReturnValue(orderSearchCriteria);
    			} else {
					return getTfGreaterReturnValue(orderSearchCriteria);
    			}
    		} else if (orderSearchCriteria.getSortByColumn().getDisplayOrder() == OrderSearchCriteriaSortColumnEnum.SORT_BY_BILLING_STATUS.getDisplayOrder()) {
//    			if (coiOrderSearchResult.getOrderLicenseByDisplaySequence(coiRow).getBillingStatus().compareTo(tfOrderSearchResult.getOrderLicenseByDisplaySequence(tfRow).getBillingStatus()) >= 0) {
//   	   				return getCompareReturnValue(orderSearchCriteria);
//   				}
     			if (coiOrderSearchResult.getOrderLicenseByDisplaySequence(coiRow).getBillingStatus() == null) {
    				if (tfOrderSearchResult.getOrderLicenseByDisplaySequence(tfRow).getBillingStatus() == null) {
    					return getCoiGreaterReturnValue(orderSearchCriteria);
    				} else {
    					return getTfGreaterReturnValue(orderSearchCriteria);
    				}
    			} else if (tfOrderSearchResult.getOrderLicenseByDisplaySequence(tfRow).getBillingStatus() == null) {
    				return getCoiGreaterReturnValue(orderSearchCriteria);
    			} else if (coiOrderSearchResult.getOrderLicenseByDisplaySequence(coiRow).getBillingStatus().compareTo(tfOrderSearchResult.getOrderLicenseByDisplaySequence(tfRow).getBillingStatus()) >= 0) {
    				return getCoiGreaterReturnValue(orderSearchCriteria);
    			} else {
					return getTfGreaterReturnValue(orderSearchCriteria);
    			}

    		} else if (orderSearchCriteria.getSortByColumn().getDisplayOrder() == OrderSearchCriteriaSortColumnEnum.SORT_BY_COURSE_NAME.getDisplayOrder()) {
    			boolean coiValueIsNull = false;
    			if (coiOrderSearchResult.getOrderBundleByDisplaySequence(coiRow) == null) {
    				coiValueIsNull = true;
    			} else if (coiOrderSearchResult.getOrderBundleByDisplaySequence(coiRow).getCourseName() == null) {
    				coiValueIsNull = true;
    			}
    			boolean tfValueIsNull = false;
    			if (tfOrderSearchResult.getOrderBundleByDisplaySequence(tfRow) == null) {
    				tfValueIsNull = true;
    			} else if (tfOrderSearchResult.getOrderBundleByDisplaySequence(tfRow).getCourseName() == null) {
    				tfValueIsNull = true;
    			}    			
    			if (coiValueIsNull) {
    				if (tfValueIsNull) {
    					return getCoiGreaterReturnValue(orderSearchCriteria);
    				} else {
    					return getTfGreaterReturnValue(orderSearchCriteria);
    				}
    			} else if (tfValueIsNull) {
    				return getCoiGreaterReturnValue(orderSearchCriteria);
    			} else if (coiOrderSearchResult.getOrderBundleByDisplaySequence(coiRow).getCourseName().compareTo(tfOrderSearchResult.getOrderBundleByDisplaySequence(tfRow).getCourseName()) >= 0) {
    				return getCoiGreaterReturnValue(orderSearchCriteria);
    			} else {
					return getTfGreaterReturnValue(orderSearchCriteria);
    			}
    		} else if (orderSearchCriteria.getSortByColumn().getDisplayOrder() == OrderSearchCriteriaSortColumnEnum.SORT_BY_COURSE_NUMBER.getDisplayOrder()) {
    			boolean coiValueIsNull = false;
    			if (coiOrderSearchResult.getOrderBundleByDisplaySequence(coiRow) == null) {
    				coiValueIsNull = true;
    			} else if (coiOrderSearchResult.getOrderBundleByDisplaySequence(coiRow).getCourseNumber() == null) {
    				coiValueIsNull = true;
    			}
    			boolean tfValueIsNull = false;
    			if (tfOrderSearchResult.getOrderBundleByDisplaySequence(tfRow) == null) {
    				tfValueIsNull = true;
    			} else if (tfOrderSearchResult.getOrderBundleByDisplaySequence(tfRow).getCourseNumber() == null) {
    				tfValueIsNull = true;
    			}    			
    			if (coiValueIsNull) {
    				if (tfValueIsNull) {
    					return getCoiGreaterReturnValue(orderSearchCriteria);
    				} else {
    					return getTfGreaterReturnValue(orderSearchCriteria);
    				}
    			} else if (tfValueIsNull) {
    				return getCoiGreaterReturnValue(orderSearchCriteria);
    			} else if (coiOrderSearchResult.getOrderBundleByDisplaySequence(coiRow).getCourseNumber().compareTo(tfOrderSearchResult.getOrderBundleByDisplaySequence(tfRow).getCourseNumber()) >= 0) {
    				return getCoiGreaterReturnValue(orderSearchCriteria);
    			} else {
					return getTfGreaterReturnValue(orderSearchCriteria);
    			}
    		} else if (orderSearchCriteria.getSortByColumn().getDisplayOrder() == OrderSearchCriteriaSortColumnEnum.SORT_BY_INSTITUTION.getDisplayOrder()) {
    			boolean coiValueIsNull = false;
    			if (coiOrderSearchResult.getOrderBundleByDisplaySequence(coiRow) == null) {
    				coiValueIsNull = true;
    			} else if (coiOrderSearchResult.getOrderBundleByDisplaySequence(coiRow).getOrganization() == null) {
    				coiValueIsNull = true;
    			}
    			boolean tfValueIsNull = false;
    			if (tfOrderSearchResult.getOrderBundleByDisplaySequence(tfRow) == null) {
    				tfValueIsNull = true;
    			} else if (tfOrderSearchResult.getOrderBundleByDisplaySequence(tfRow).getOrganization() == null) {
    				tfValueIsNull = true;
    			}    			
    			if (coiValueIsNull) {
    				if (tfValueIsNull) {
    					return getCoiGreaterReturnValue(orderSearchCriteria);
    				} else {
    					return getTfGreaterReturnValue(orderSearchCriteria);
    				}
    			} else if (tfValueIsNull) {
    				return getCoiGreaterReturnValue(orderSearchCriteria);
    			} else if (coiOrderSearchResult.getOrderBundleByDisplaySequence(coiRow).getOrganization().compareTo(tfOrderSearchResult.getOrderBundleByDisplaySequence(tfRow).getOrganization()) >= 0) {
    				return getCoiGreaterReturnValue(orderSearchCriteria);
    			} else {
					return getTfGreaterReturnValue(orderSearchCriteria);
    			}
    		} else if (orderSearchCriteria.getSortByColumn().getDisplayOrder() == OrderSearchCriteriaSortColumnEnum.SORT_BY_INSTRUCTOR.getDisplayOrder()) {
    			boolean coiValueIsNull = false;
    			if (coiOrderSearchResult.getOrderBundleByDisplaySequence(coiRow) == null) {
    				coiValueIsNull = true;
    			} else if (coiOrderSearchResult.getOrderBundleByDisplaySequence(coiRow).getInstructor() == null) {
    				coiValueIsNull = true;
    			}
    			boolean tfValueIsNull = false;
    			if (tfOrderSearchResult.getOrderBundleByDisplaySequence(tfRow) == null) {
    				tfValueIsNull = true;
    			} else if (tfOrderSearchResult.getOrderBundleByDisplaySequence(tfRow).getInstructor() == null) {
    				tfValueIsNull = true;
    			}    			
    			if (coiValueIsNull) {
    				if (tfValueIsNull) {
    					return getCoiGreaterReturnValue(orderSearchCriteria);
    				} else {
    					return getTfGreaterReturnValue(orderSearchCriteria);
    				}
    			} else if (tfValueIsNull) {
    				return getCoiGreaterReturnValue(orderSearchCriteria);
    			} else if (coiOrderSearchResult.getOrderBundleByDisplaySequence(coiRow).getInstructor().compareTo(tfOrderSearchResult.getOrderBundleByDisplaySequence(tfRow).getInstructor()) >= 0) {
    				return getCoiGreaterReturnValue(orderSearchCriteria);
    			} else {
					return getTfGreaterReturnValue(orderSearchCriteria);
    			}
    		} else if (orderSearchCriteria.getSortByColumn().getDisplayOrder() == OrderSearchCriteriaSortColumnEnum.SORT_BY_PUBLICATION_TITLE.getDisplayOrder()) {
     			if (coiOrderSearchResult.getOrderLicenseByDisplaySequence(coiRow).getItemDescription() == null) {
    				if (tfOrderSearchResult.getOrderLicenseByDisplaySequence(tfRow).getItemDescription() == null) {
    					return getCoiGreaterReturnValue(orderSearchCriteria);
    				} else {
    					return getTfGreaterReturnValue(orderSearchCriteria);
    				}
    			} else if (tfOrderSearchResult.getOrderLicenseByDisplaySequence(tfRow).getItemDescription() == null) {
    				return getCoiGreaterReturnValue(orderSearchCriteria);
    			} else if (coiOrderSearchResult.getOrderLicenseByDisplaySequence(coiRow).getItemDescription().compareTo(tfOrderSearchResult.getOrderLicenseByDisplaySequence(tfRow).getItemDescription()) >= 0) {
    				return getCoiGreaterReturnValue(orderSearchCriteria);
    			} else {
					return getTfGreaterReturnValue(orderSearchCriteria);
    			}
    		} else if (orderSearchCriteria.getSortByColumn().getDisplayOrder() == OrderSearchCriteriaSortColumnEnum.SORT_BY_YOUR_REFERENCE.getDisplayOrder()) {
//    			if (coiOrderSearchResult.getOrderBundleByDisplaySequence(coiRow).getYourReference().compareTo(tfOrderSearchResult.getOrderBundleByDisplaySequence(tfRow).getYourReference()) >= 0) {
//   	   				return getCompareReturnValue(orderSearchCriteria);
//   				}
    			boolean coiValueIsNull = false;
    			if (coiOrderSearchResult.getOrderBundleByDisplaySequence(coiRow) == null) {
    				coiValueIsNull = true;
    			} else if (coiOrderSearchResult.getOrderBundleByDisplaySequence(coiRow).getYourReference() == null) {
    				coiValueIsNull = true;
    			}
    			boolean tfValueIsNull = false;
    			if (tfOrderSearchResult.getOrderBundleByDisplaySequence(tfRow) == null) {
    				tfValueIsNull = true;
    			} else if (tfOrderSearchResult.getOrderBundleByDisplaySequence(tfRow).getYourReference() == null) {
    				tfValueIsNull = true;
    			}    			
    			if (coiValueIsNull) {
    				if (tfValueIsNull) {
    					return getCoiGreaterReturnValue(orderSearchCriteria);
    				} else {
    					return getTfGreaterReturnValue(orderSearchCriteria);
    				}
    			} else if (tfValueIsNull) {
    				return getCoiGreaterReturnValue(orderSearchCriteria);
    			} else if (coiOrderSearchResult.getOrderBundleByDisplaySequence(coiRow).getYourReference().compareTo(tfOrderSearchResult.getOrderBundleByDisplaySequence(tfRow).getYourReference()) >= 0) {
    				return getCoiGreaterReturnValue(orderSearchCriteria);
    			} else {
					return getTfGreaterReturnValue(orderSearchCriteria);
    			}
    		} else if (orderSearchCriteria.getSortByColumn().getDisplayOrder() == OrderSearchCriteriaSortColumnEnum.SORT_BY_YOUR_ACC_REFERENCE.getDisplayOrder()) {
//    			if (coiOrderSearchResult.getOrderBundleByDisplaySequence(coiRow).getAccountingReference().compareTo(tfOrderSearchResult.getOrderBundleByDisplaySequence(tfRow).getAccountingReference()) >= 0) {
//   	   				return getCompareReturnValue(orderSearchCriteria);
//   				}
    			boolean coiValueIsNull = false;
    			if (coiOrderSearchResult.getOrderBundleByDisplaySequence(coiRow) == null) {
    				coiValueIsNull = true;
    			} else if (coiOrderSearchResult.getOrderBundleByDisplaySequence(coiRow).getAccountingReference() == null) {
    				coiValueIsNull = true;
    			}
    			boolean tfValueIsNull = false;
    			if (tfOrderSearchResult.getOrderBundleByDisplaySequence(tfRow) == null) {
    				tfValueIsNull = true;
    			} else if (tfOrderSearchResult.getOrderBundleByDisplaySequence(tfRow).getAccountingReference() == null) {
    				tfValueIsNull = true;
    			}    			
    			if (coiValueIsNull) {
    				if (tfValueIsNull) {
    					return getCoiGreaterReturnValue(orderSearchCriteria);
    				} else {
    					return getTfGreaterReturnValue(orderSearchCriteria);
    				}
    			} else if (tfValueIsNull) {
    				return getCoiGreaterReturnValue(orderSearchCriteria);
    			} else if (coiOrderSearchResult.getOrderBundleByDisplaySequence(coiRow).getAccountingReference().compareTo(tfOrderSearchResult.getOrderBundleByDisplaySequence(tfRow).getAccountingReference()) >= 0) {
    				return getCoiGreaterReturnValue(orderSearchCriteria);
    			} else {
					return getTfGreaterReturnValue(orderSearchCriteria);
    			}
    		} else if (orderSearchCriteria.getSortByColumn().getDisplayOrder() == OrderSearchCriteriaSortColumnEnum.SORT_BY_INTERNET_LOGIN.getDisplayOrder()) {
    			if (coiOrderSearchResult.getOrderPurchaseByDisplaySequence(coiRow).getBuyerUserIdentifier() == null) {
    				if (tfOrderSearchResult.getOrderPurchaseByDisplaySequence(tfRow).getBuyerUserIdentifier() == null) {
    					return getCoiGreaterReturnValue(orderSearchCriteria);
    				} else {
    					return getTfGreaterReturnValue(orderSearchCriteria);
    				}
    			} else if (tfOrderSearchResult.getOrderPurchaseByDisplaySequence(tfRow).getBuyerUserIdentifier() == null) {
    				return getCoiGreaterReturnValue(orderSearchCriteria);
    			} else if (coiOrderSearchResult.getOrderPurchaseByDisplaySequence(coiRow).getBuyerUserIdentifier().compareTo(tfOrderSearchResult.getOrderPurchaseByDisplaySequence(tfRow).getBuyerUserIdentifier()) >= 0) {
    				return getCoiGreaterReturnValue(orderSearchCriteria);
    			} else {
					return getTfGreaterReturnValue(orderSearchCriteria);
    			}    			
   			} else if (orderSearchCriteria.getSortByColumn().getDisplayOrder() == OrderSearchCriteriaSortColumnEnum.SORT_BY_LICENSEE_ACCOUNT.getDisplayOrder()) {
	   			if (coiOrderSearchResult.getOrderPurchaseByDisplaySequence(coiRow).getLicenseeName() == null) {
    				if (tfOrderSearchResult.getOrderPurchaseByDisplaySequence(tfRow).getLicenseeName() == null) {
    					return getCoiGreaterReturnValue(orderSearchCriteria);
    				} else {
    					return getTfGreaterReturnValue(orderSearchCriteria);
    				}
    			} else if (tfOrderSearchResult.getOrderPurchaseByDisplaySequence(tfRow).getLicenseeName() == null) {
    				return getCoiGreaterReturnValue(orderSearchCriteria);
    			} else if (coiOrderSearchResult.getOrderPurchaseByDisplaySequence(coiRow).getLicenseeName().compareTo(tfOrderSearchResult.getOrderPurchaseByDisplaySequence(tfRow).getLicenseeName()) >= 0) {
    				return getCoiGreaterReturnValue(orderSearchCriteria);
    			} else {
					return getTfGreaterReturnValue(orderSearchCriteria);
    			}   			
   			} else if (orderSearchCriteria.getSortByColumn().getDisplayOrder() == OrderSearchCriteriaSortColumnEnum.SORT_BY_LAST_UPDATE_DATE.getDisplayOrder()) {
   				Date coiLastUpdateDate; Date tfLastUpdateDate;
   				if (coiOrderSearchResult.getOrderLicenseByDisplaySequence(coiRow).getLastUpdatedDate() == null) {
   					coiLastUpdateDate = coiOrderSearchResult.getOrderLicenseByDisplaySequence(coiRow).getCreateDate();
   				} else {
   					coiLastUpdateDate = coiOrderSearchResult.getOrderLicenseByDisplaySequence(coiRow).getLastUpdatedDate();   					
   				}
   				if (tfOrderSearchResult.getOrderLicenseByDisplaySequence(tfRow).getLastUpdatedDate() == null) {
   					tfLastUpdateDate = tfOrderSearchResult.getOrderLicenseByDisplaySequence(tfRow).getCreateDate();
   				} else {
   					tfLastUpdateDate = tfOrderSearchResult.getOrderLicenseByDisplaySequence(tfRow).getLastUpdatedDate();   					
   				}
    			if (coiLastUpdateDate == null) {
    				if (tfLastUpdateDate == null) {
    					return getCoiGreaterReturnValue(orderSearchCriteria);
    				} else {
    					return getTfGreaterReturnValue(orderSearchCriteria);
    				}
    			} else if (tfLastUpdateDate == null) {
    				return getCoiGreaterReturnValue(orderSearchCriteria);
    			} else if (coiLastUpdateDate.compareTo(tfLastUpdateDate) >= 0) {
    				return getCoiGreaterReturnValue(orderSearchCriteria);
    			} else {
					return getTfGreaterReturnValue(orderSearchCriteria);
    			}
    		} else if (orderSearchCriteria.getSortByColumn().getDisplayOrder() == OrderSearchCriteriaSortColumnEnum.SORT_BY_REPUBLICATION_DATE.getDisplayOrder()) {
    			if (coiOrderSearchResult.getOrderLicenseByDisplaySequence(coiRow).getRepublicationDate() == null) {
    				if (tfOrderSearchResult.getOrderLicenseByDisplaySequence(tfRow).getRepublicationDate() == null) {
    					return getCoiGreaterReturnValue(orderSearchCriteria);
    				} else {
    					return getTfGreaterReturnValue(orderSearchCriteria);
    				}
    			} else if (tfOrderSearchResult.getOrderLicenseByDisplaySequence(tfRow).getRepublicationDate() == null) {
    				return getCoiGreaterReturnValue(orderSearchCriteria);
    			} else if (coiOrderSearchResult.getOrderLicenseByDisplaySequence(coiRow).getRepublicationDate().compareTo(tfOrderSearchResult.getOrderLicenseByDisplaySequence(tfRow).getRepublicationDate()) >= 0) {
    				return getCoiGreaterReturnValue(orderSearchCriteria);
    			} else {
    				return getTfGreaterReturnValue(orderSearchCriteria);
    			}
    		} else if (orderSearchCriteria.getSortByColumn().getDisplayOrder() == OrderSearchCriteriaSortColumnEnum.SORT_BY_REPUBLICATION_PUBLISHER.getDisplayOrder()) {
    			if (coiOrderSearchResult.getOrderLicenseByDisplaySequence(coiRow).getRepublishingOrganization() == null) {
    				if (tfOrderSearchResult.getOrderLicenseByDisplaySequence(tfRow).getRepublishingOrganization() == null) {
    					return getCoiGreaterReturnValue(orderSearchCriteria);
    				} else {
    					return getTfGreaterReturnValue(orderSearchCriteria);
    				}
    			} else if (tfOrderSearchResult.getOrderLicenseByDisplaySequence(tfRow).getRepublishingOrganization() == null) {
    				return getCoiGreaterReturnValue(orderSearchCriteria);
    			} else if (coiOrderSearchResult.getOrderLicenseByDisplaySequence(coiRow).getRepublishingOrganization().compareTo(tfOrderSearchResult.getOrderLicenseByDisplaySequence(tfRow).getRepublishingOrganization()) >= 0) {
    				return getCoiGreaterReturnValue(orderSearchCriteria);
    			} else {
    				return getTfGreaterReturnValue(orderSearchCriteria);
    			}
    		} else if (orderSearchCriteria.getSortByColumn().getDisplayOrder() == OrderSearchCriteriaSortColumnEnum.SORT_BY_REPUBLICATION_TITLE.getDisplayOrder()) {
    			if (coiOrderSearchResult.getOrderLicenseByDisplaySequence(coiRow).getRepublicationDestination() == null) {
    				if (tfOrderSearchResult.getOrderLicenseByDisplaySequence(tfRow).getRepublicationDestination() == null) {
    					return getCoiGreaterReturnValue(orderSearchCriteria);
    				} else {
    					return getTfGreaterReturnValue(orderSearchCriteria);
    				}
    			} else if (tfOrderSearchResult.getOrderLicenseByDisplaySequence(tfRow).getRepublicationDestination() == null) {
    				return getCoiGreaterReturnValue(orderSearchCriteria);
    			} else if (coiOrderSearchResult.getOrderLicenseByDisplaySequence(coiRow).getRepublicationDestination().compareTo(tfOrderSearchResult.getOrderLicenseByDisplaySequence(tfRow).getRepublicationDestination()) >= 0) {
    				return getCoiGreaterReturnValue(orderSearchCriteria);
    			} else {
    				return getTfGreaterReturnValue(orderSearchCriteria);
    			}
    		} else if (orderSearchCriteria.getSortByColumn().getDisplayOrder() == OrderSearchCriteriaSortColumnEnum.SORT_BY_YOUR_REFERENCE.getDisplayOrder()) {
    			if (coiOrderSearchResult.getOrderLicenseByDisplaySequence(coiRow).getCustomerReference() == null) {
    				if (tfOrderSearchResult.getOrderLicenseByDisplaySequence(tfRow).getCustomerReference() == null) {
    					return getCoiGreaterReturnValue(orderSearchCriteria);
    				} else {
    					return getTfGreaterReturnValue(orderSearchCriteria);
    				}
    			} else if (tfOrderSearchResult.getOrderLicenseByDisplaySequence(tfRow).getCustomerReference() == null) {
    				return getCoiGreaterReturnValue(orderSearchCriteria);
    			} else if (coiOrderSearchResult.getOrderLicenseByDisplaySequence(coiRow).getCustomerReference().compareTo(tfOrderSearchResult.getOrderLicenseByDisplaySequence(tfRow).getCustomerReference()) >= 0) {
    				return getCoiGreaterReturnValue(orderSearchCriteria);
    			} else {
    				return getTfGreaterReturnValue(orderSearchCriteria);
    			}

    			
   				
   			}


    		
    		//TODO Finish off the rest of this criteria
//    		OrderSearchCriteriaSortColumnEnum.SORT_BY_PERMISSION_STATUS
//    		OrderSearchCriteriaSortColumnEnum.SORT_BY_PERMISSION_TYPE
//    		OrderSearchCriteriaSortColumnEnum.SORT_BY_INSTITUTION
//    		OrderSearchCriteriaSortColumnEnum.SORT_BY_INSTRUCTOR
//    		OrderSearchCriteriaSortColumnEnum.SORT_BY_REPUBLICATION_DATE
//    		OrderSearchCriteriaSortColumnEnum.SORT_BY_REPUBLICATION_PUBLISHER
//    		OrderSearchCriteriaSortColumnEnum.SORT_BY_REPUBLICATION_TITLE
    		
		
    		
//    		if (coiRow < 10) {          	
//		    	return "C";
//    		} else {
//    			return "T";
//    		}
    		
    		return "C";
        }
        
        public static OrderSearchResult searchOrderLicensesWithUser(OrderSearchCriteria orderSearchCriteria) throws OrderLicensesException {
            
       // 	OrderSearchResult orderLicenseSearchResult = new OrderSearchResult();

//        	int fromRow = orderSearchCriteria.getSearchFromRow();
//        	int toRow = orderSearchCriteria.getSearchToRow();
        	
       //  	orderLicenseSearchResult.setDisplayFromRow(fromRow);
       // 	orderLicenseSearchResult.setDisplayToRow(toRow);
        	
//        	OrderSearchResult coiOrderSearchResult = searchCOIOrderLicenses(orderSearchCriteria);
//			TODO Figure out the merge
        	OrderSearchResult orderSearchResult = searchOrderLicenses(orderSearchCriteria);
    	    OrderLicenseServices.getUsersForPurchases(orderSearchResult);
       	
            return orderSearchResult;

        }

        public static boolean cancelLicense(OrderLicense orderLicense) throws OrderLicensesException {
            try {
            	if (orderLicense.getOrderDataSource() == OrderSearchCriteriaDataSourceEnum.TF.getIntVaue()) {
            		return cancelTfLicense(orderLicense.getID());
            	} else {
            		return cancelCoiLicense(orderLicense);
            	}
            	
            } catch (OrderMgmtException omex) {
        		LOGGER.error("Error Updating License: " + orderLicense.getOrderId() + LogUtil.appendableStack(omex));
                OrderLicensesException orderLicensesException = new OrderLicensesException("Error Updating License: " + orderLicense.getOrderId(), omex.getMessageCode(), omex.getCause());
                throw orderLicensesException;
            }

//            return false;
        }

        
        private static boolean cancelCoiLicense(OrderLicense orderLicense) throws OrderLicensesException {
        	 
        	if (!orderLicense.isCancelable()) {
        		return false;
        	}
         	
        	try {
            	OrderItemImpl orderItemImpl = (OrderItemImpl) orderLicense;
            	ServiceLocator.getOrderService().cancelItem(new OrderConsumerContext(), orderItemImpl.getItem());            
            } catch (Exception e) {
        		LOGGER.error("Error Updating License: " + orderLicense.getOrderId() + LogUtil.appendableStack(e));
                OrderLicensesException orderLicensesException = new OrderLicensesException("Error Updating License: " + orderLicense.getOrderId(), e.getMessage(), e.getCause());
                throw orderLicensesException;
            }
            UserContextService.getLicenseDisplaySpec().setForceReRead(true);
            UserContextService.getPurchaseDisplaySpec().setForceReRead(true);

        	return true; 
        }
        
        
        private static boolean cancelTfLicense(long licenseID) throws OrderLicensesException {
          try {
                OrderMgmtServiceFactory orderFactory = OrderMgmtServiceFactory.getInstance();
                OrderMgmtServiceAPI orderService = orderFactory.getService();

                orderService.cancelLicense(licenseID);
            } catch (OrderMgmtException omex) {
        		LOGGER.error("Error canceling License: " + licenseID + LogUtil.appendableStack(omex));
                OrderLicensesException orderLicensesException = new OrderLicensesException("Error Canceling License: " + licenseID, omex.getMessageCode(), omex.getCause());
                throw orderLicensesException;
            }
            UserContextService.getLicenseDisplaySpec().setForceReRead(true);
            UserContextService.getPurchaseDisplaySpec().setForceReRead(true);

            return true;
        }

        public static boolean unCancelLicense(OrderLicense orderLicense) throws OrderLicensesException {
            try {
            	if (orderLicense.getOrderDataSource() == OrderSearchCriteriaDataSourceEnum.TF.getIntVaue()) {
            		return unCancelTfLicense(orderLicense.getID());
            	} else {
            		return unCancelCoiLicense(orderLicense);
            	}
            	
            } catch (OrderMgmtException omex) {
        		LOGGER.error("Error canceling License: " + orderLicense.getOrderId() + LogUtil.appendableStack(omex));
                OrderLicensesException orderLicensesException = new OrderLicensesException("Error Updating License: " + orderLicense.getOrderId(), omex.getMessageCode(), omex.getCause());
                throw orderLicensesException;
            }


//            return false;
        }

        private static boolean unCancelCoiLicense(OrderLicense orderLicense) throws OrderLicensesException {
        	
        	if (!orderLicense.getItemStatusCd().equals(ItemStatusEnum.CANCELLED.name())) {
        		return false;
        	}
        	
        	try {
            	OrderConsumerContext orderConsumerContext = new OrderConsumerContext();
            	OrderItemImpl orderItemImpl = (OrderItemImpl) orderLicense;
               	ServiceLocator.getOrderService().uncancelItem(orderConsumerContext, orderItemImpl.getItem());            
             } catch (Exception e) {
         		LOGGER.error("Error updating License: " + orderLicense.getOrderId() + LogUtil.appendableStack(e));
                OrderLicensesException orderLicensesException = new OrderLicensesException("Error Updating License: " + orderLicense.getOrderId(), e.getMessage(), e.getCause());
                throw orderLicensesException;
            }

            UserContextService.getLicenseDisplaySpec().setForceReRead(true);
            UserContextService.getPurchaseDisplaySpec().setForceReRead(true);

            
        	return true; 
        }
        
        private static boolean unCancelTfLicense(long licenseID) throws OrderLicensesException {
          try {
                OrderMgmtServiceFactory orderFactory = OrderMgmtServiceFactory.getInstance();
                OrderMgmtServiceAPI orderService = orderFactory.getService();

                orderService.uncancelLicense(licenseID);
            } catch (OrderMgmtException omex) {
        		LOGGER.error("Error uncanceling License: " + licenseID + LogUtil.appendableStack(omex));            	
                OrderLicensesException orderLicensesException = new OrderLicensesException("Error Un-Canceling License: " + licenseID, omex.getMessageCode(), omex.getCause());
                throw orderLicensesException;
            }
            UserContextService.getLicenseDisplaySpec().setForceReRead(true);
            UserContextService.getPurchaseDisplaySpec().setForceReRead(true);

            return true;
        }

        public static OrderLicense initCoiLicense(long orderHeaderId) throws OrderLicensesException {

        	OrderItemImpl orderItemImpl = new OrderItemImpl();
        	Item item = new Item();
        	Set<ResolutionLog> resolutionLogs = new HashSet<ResolutionLog>(0);
        	Set<ItemFees> allFees = new HashSet<ItemFees>(0);
        	Map<String, ItemDescriptionParm> itemDescriptionParms = new HashMap<String, ItemDescriptionParm>(0);
        	Map<String, ItemParm> itemParms = new HashMap<String, ItemParm>(0);
        	
        	item.setResolutionLogs(resolutionLogs);
        	item.setAllFees(allFees);
        	item.setItemDescriptionParms(itemDescriptionParms);
        	item.setItemParms(itemParms);
        	orderItemImpl.setItem(item);
           	orderItemImpl.setItemStatusCd(ItemStatusEnum.NOT_COMPLETELY_ENTERED.name());
           	orderItemImpl.setOrderId(orderHeaderId);

           	return orderItemImpl;
           	
        }
        
        public static OrderLicense createCoiLicense(OrderLicense orderLicense) throws OrderLicensesException {

        	OrderItemImpl orderItemImpl = (OrderItemImpl) orderLicense;
        	
        	try {
            	//TODO This should return an Item
        		ServiceLocator.getOrderService().addItemToOrder(new OrderConsumerContext(), orderItemImpl.getOrderIdLong(), orderItemImpl.getItem()); 
            } catch (Exception e) {
        		LOGGER.error("Error calling addItemToOrder " + orderItemImpl.getOrderId() + LogUtil.appendableStack(e));
                OrderLicensesException orderLicensesException = new OrderLicensesException("Error Creating License: " + orderItemImpl.getOrderId(), e.getMessage(), e.getCause());
                throw orderLicensesException;
            }

            return orderItemImpl;
//            return false;
        }

        private static void initializeNullFeesForNonPurchaseOrderLicenseUpdates (OrderLicense orderLicense) {
        	if (orderLicense.getOrderDataSource() == OrderDataSourceEnum.OMS.getOrderDataSourceId().longValue()) {
        		OrderItemImpl orderItemImpl = (OrderItemImpl) orderLicense;
        		initializeNullFeesForNonPurchaseItemUpdates (orderItemImpl.getItem());
        	}
        }

        public static void initializeNullFeesForNonPurchaseOrderPurchaseUpdates (OrderPurchase orderPurchase) {
        	if (orderPurchase.getOrderDataSource() == OrderDataSourceEnum.OMS.getOrderDataSourceId().longValue()) {
               	OrderHeaderImpl orderHeaderImpl = (OrderHeaderImpl) orderPurchase;
               	for (Item item : orderHeaderImpl.getOrderHeader().getItems()) {
               		initializeNullFeesForNonPurchaseItemUpdates (item);
               	}
        	}
        }

        private static void initializeNullFeesForNonPurchaseItemUpdates (Item item) {
       		if (item.getItemStatusCd().equals(ItemStatusEnum.CANCELLED) ||
       			item.getItemAvailabilityCd() == null ||
           		item.getItemAvailabilityCd().equals(ItemAvailabilityEnum.NO_CHARGE.getStandardPermissionCd()) ||
           		item.getItemAvailabilityCd().equals(ItemAvailabilityEnum.RESEARCH_FURTHER.getStandardPermissionCd()) ||
           		item.getItemAvailabilityCd().equals(ItemAvailabilityEnum.CONTACT_RH.getStandardPermissionCd()) ||
           		item.getItemAvailabilityCd().equals(ItemAvailabilityEnum.CONTACT_RH_DIRECTLY.getStandardPermissionCd()) ||
           		item.getItemAvailabilityCd().equals(ItemAvailabilityEnum.PUBLIC_DOMAIN.getStandardPermissionCd()) ||
        		item.getItemAvailabilityCd().equals(ItemAvailabilityEnum.DENY.getStandardPermissionCd())) {
        		if (item.getAllFees().size() > 0) {
           			for (ItemFees itemFees : item.getAllFees()) {
        				itemFees.setDistributionPayable(null);
        				itemFees.setRightsholderFee(null);
        				itemFees.setLicenseeFee(null);
        				itemFees.setDiscount(null);
        			}
        		}
        	}
        }        
        
        public static OrderLicense updateOrderLicense (OrderLicense orderLicense) throws OrderLicensesException {
        	boolean updateSuccess;
        	        	
        	try {
            	if (orderLicense.getOrderDataSource() == OrderSearchCriteriaDataSourceEnum.TF.getIntVaue()) {
            		updateSuccess = updateTFLicense(orderLicense);
            	} else {
	    			updateSuccess = updateCOILicense(orderLicense);
            	}
            	
            } catch (OrderMgmtException omex) {
        		LOGGER.error("error updating license " + orderLicense.getOrderId() + LogUtil.appendableStack(omex));
            	OrderLicensesException orderLicensesException = new OrderLicensesException("Error Updating License: " + orderLicense.getOrderId(), omex.getMessageCode(), omex.getCause());
                throw orderLicensesException;
            }

        	if (updateSuccess) {
        		OrderSearchResult orderSearchResult;
        		try {
            		orderSearchResult = searchOrderLicensesForDetailNumber(orderLicense.getID()); 
        		}
        		catch (OrderLicensesException omex) {
	        		LOGGER.error("error getting updated license " + orderLicense.getOrderId() + LogUtil.appendableStack(omex));
                    OrderLicensesException orderLicensesException = new OrderLicensesException("Error Getting Updated License: " + orderLicense.getOrderId(), omex.getMessageCode(), omex.getCause());
        			throw orderLicensesException;
        		}

        		for (OrderLicense returnOrderLicense : orderSearchResult.getOrderLicenses()) {
        			if (returnOrderLicense.getOrderDataSource() == orderLicense.getOrderDataSource()) {
        				return returnOrderLicense;
        			}
        		}
        	}
        	
        	return null;
        }
                
        public static boolean updateLicenseAndPricing(OrderLicense orderLicense) throws OrderLicensesException {
            try {
            	if (orderLicense.getOrderDataSource() == OrderSearchCriteriaDataSourceEnum.TF.getIntVaue()) {
//            		OrderLicenseImpl orderLicenseImpl = (OrderLicenseImpl) orderLicense;
//            		orderLicenseImpl.calculateRightFees();
            		return updateTFLicense(orderLicense);
            	} else {
            		try {
            			OrderItemImpl orderItemImpl = (OrderItemImpl) orderLicense;
            			if (orderItemImpl.isPricingFieldChanged()) {
                			orderLicense = PricingServices.updateItemPrice(orderLicense);           				
            			}
            			return updateCOILicense(orderLicense);
            		} catch (ServiceInvocationException e) {
    	        		LOGGER.error("error calling PricingServices.updateItemPrice for " + orderLicense.getOrderId() + LogUtil.appendableStack(e));
                        OrderLicensesException orderLicensesException = new OrderLicensesException("Error Updating License Pricing: " + orderLicense.getOrderId(), e.getMessageCode(), e.getCause());
            			throw orderLicensesException;
            		}
            	}
            	
            } catch (OrderMgmtException omex) {
        		LOGGER.error("error updating license " + orderLicense.getOrderId() + LogUtil.appendableStack(omex));
            	OrderLicensesException orderLicensesException = new OrderLicensesException("Error Updating License: " + orderLicense.getOrderId(), omex.getMessageCode(), omex.getCause());
                throw orderLicensesException;
            }

//            return false;
        }
        
        public static boolean updateLicense(OrderLicense orderLicense) throws OrderLicensesException {
            try {
            	if (orderLicense.getOrderDataSource() == OrderSearchCriteriaDataSourceEnum.TF.getIntVaue()) {
            		return updateTFLicense(orderLicense);
            	} else {
            		return updateCOILicense(orderLicense);
            	}
            	
            } catch (OrderMgmtException omex) {
        		LOGGER.error("error updating license " + orderLicense.getOrderId() + LogUtil.appendableStack(omex));
                OrderLicensesException orderLicensesException = new OrderLicensesException("Error Updating License: " + orderLicense.getOrderId(), omex.getMessageCode(), omex.getCause());
                throw orderLicensesException;
            }

//            return false;
        }
        
        private static boolean updateCOILicense(OrderLicense orderLicense) throws OrderLicensesException {
            try {
            	initializeNullFeesForNonPurchaseOrderLicenseUpdates(orderLicense);
            	OrderItemImpl orderItemImpl = (OrderItemImpl) orderLicense;
            	ServiceLocator.getOrderService().updateItemInOrder(new OrderConsumerContext(), orderItemImpl.getOrderHeaderId(), orderItemImpl.getItem());            
            } catch (Exception e) {
        		LOGGER.error("error updating license " + orderLicense.getOrderHeaderId() + LogUtil.appendableStack(e));
                OrderLicensesException orderLicensesException = new OrderLicensesException("Error Updating License: " + orderLicense.getOrderHeaderId(), e.getMessage(), e.getCause());
                throw orderLicensesException;
            }

        	return true;
        }
        
        private static boolean updateTFLicense(OrderLicense orderLicense) throws OrderLicensesException {
            try {
                  OrderLicenseImpl orderLicenseImpl = (OrderLicenseImpl) orderLicense;
                  
                  OrderMgmtServiceFactory orderFactory = OrderMgmtServiceFactory.getInstance();
                  OrderMgmtServiceAPI orderService = orderFactory.getService();

                  orderService.updateLicense(orderLicenseImpl.getLicense());
              } catch (OrderMgmtException omex) {
            	  LOGGER.error("error updating license " + orderLicense.getOrderId() + LogUtil.appendableStack(omex));
                  OrderLicensesException orderLicensesException = new OrderLicensesException("Error Updating License: " + orderLicense.getOrderId(), omex.getMessageCode(), omex.getCause());
                  throw orderLicensesException;
              }
            UserContextService.getLicenseDisplaySpec().setForceReRead(true);
            UserContextService.getPurchaseDisplaySpec().setForceReRead(true);

            return true;
        }

        private static String formatInvoiceSearch(String invoiceString) {
           if (invoiceString == null || invoiceString.equals("") || invoiceString.equals(",")) {
                return null;
            }
            
            String invoice[] = new String[10];
            int indexOfStart = 0;
            int indexOfEnd = -1;
            int invoiceCtr = 0;
            
            do {
                String invoiceId;
                indexOfEnd = invoiceString.indexOf(",", indexOfStart);

                if (indexOfEnd > -1) {
                    invoiceId = invoiceString.substring(indexOfStart, indexOfEnd).trim();
                    invoice[invoiceCtr] = invoiceId;
                    invoiceCtr = invoiceCtr + 1;
                    indexOfStart = indexOfEnd + 1;
                } else {
                    invoiceId = invoiceString.substring(indexOfStart, invoiceString.length()).trim();
                    invoice[invoiceCtr] = invoiceId;
                    invoiceCtr = invoiceCtr + 1;
                    indexOfStart = indexOfEnd + 1;
                }
            } while (indexOfEnd > -1 && invoiceCtr < 10 );
            
            
            int iCtr;
            StringBuffer invoiceStringBuffer = new StringBuffer();
//            invoiceStringBuffer.append("'");
            for (iCtr=0;iCtr < invoiceCtr;iCtr++) {
              if (invoice[iCtr].length() > 0) {
                if (iCtr > 0) invoiceStringBuffer.append(",");
                invoiceStringBuffer.append("'");
                invoiceStringBuffer.append(invoice[iCtr]);
                invoiceStringBuffer.append("'");
              }
            }
//            invoiceStringBuffer.append("'");
            return invoiceStringBuffer.toString().trim();
            
        }

        public static OrderSearchCriteria getOrderSearchCriteriaFromDisplaySpec(DisplaySpec displaySpec) {
        	
        	OrderSearchCriteria orderSearchCriteria = new OrderSearchCriteria();
        	
        	CCUserContext userContext;
        	userContext = UserContextService.getUserContext();
//        	OrderLicenses orderLicenses = UserContextService.getOrderLicenses(); 
//            DisplaySpec displaySpec = UserContextService.getLicenseDisplaySpec(); 
            int readNumberOfLicenseRows = displaySpec.getResultsPerPage();
        	OrderSearchResult searchResult = null;
        	String callSortBy = null;
        	String callSortOrder = null;
        	String callSearchBy = null;
        	String searchUserScope = SEARCH_CURRENT_USER;
        	
        	String showHiddenLicenses = OrderMgmtSearchSortConstants.SHOW_HIDDEN_LICENSE_NO;
        	orderSearchCriteria.setIncludeHiddenOrders(false);
       	
        	switch (displaySpec.getSortBy()) {
        		case DisplaySpecServices.ORDERDATESORT: 
        				callSortBy = OrderSearchCriteriaSortColumnEnum.SORT_BY_ORDER_DATE.name();
        				orderSearchCriteria.setSortByColumn(OrderSearchCriteriaSortColumnEnum.SORT_BY_ORDER_DATE);
        				break;
        		case DisplaySpecServices.CONFNUMSORT: 
						callSortBy = OrderSearchCriteriaSortColumnEnum.SORT_BY_CONFIRM_NUMBER.name();
						orderSearchCriteria.setSortByColumn(OrderSearchCriteriaSortColumnEnum.SORT_BY_CONFIRM_NUMBER);
						break;
        		case DisplaySpecServices.PUBLICATIONTITLESORT:
        				callSortBy = OrderSearchCriteriaSortColumnEnum.SORT_BY_PUBLICATION_TITLE.name();
        				orderSearchCriteria.setSortByColumn(OrderSearchCriteriaSortColumnEnum.SORT_BY_PUBLICATION_TITLE);
        				break;
        		case DisplaySpecServices.ORDERDETAILIDSORT: 
        				callSortBy = OrderSearchCriteriaSortColumnEnum.SORT_BY_ORDER_DETAIL_ID.name(); 
        				orderSearchCriteria.setSortByColumn(OrderSearchCriteriaSortColumnEnum.SORT_BY_ORDER_DETAIL_ID);
        				break; 
        		case DisplaySpecServices.PERMISSIONTYPESORT: 
        				callSortBy = OrderSearchCriteriaSortColumnEnum.SORT_BY_PERMISSION_TYPE.name(); 
        				orderSearchCriteria.setSortByColumn(OrderSearchCriteriaSortColumnEnum.SORT_BY_PERMISSION_TYPE);
        				break;
        		case DisplaySpecServices.PERMISSIONSTATUSSORT: 
        				callSortBy = OrderSearchCriteriaSortColumnEnum.SORT_BY_PERMISSION_STATUS.name(); 
        				orderSearchCriteria.setSortByColumn(OrderSearchCriteriaSortColumnEnum.SORT_BY_PERMISSION_STATUS);        				
        				break; 
        		case DisplaySpecServices.BILLINGSTATUSSORT: 
        				callSortBy = OrderSearchCriteriaSortColumnEnum.SORT_BY_BILLING_STATUS.name(); 
        				orderSearchCriteria.setSortByColumn(OrderSearchCriteriaSortColumnEnum.SORT_BY_BILLING_STATUS);
        				break;
        		case DisplaySpecServices.INVOICENUMBERSORT: 
        				callSortBy = OrderSearchCriteriaSortColumnEnum.SORT_BY_INVOICE_NUMBER.name(); 
        				orderSearchCriteria.setSortByColumn(OrderSearchCriteriaSortColumnEnum.SORT_BY_INVOICE_NUMBER);
        				break;
        		case DisplaySpecServices.YOURREFERENCESORT: 
        				callSortBy = OrderSearchCriteriaSortColumnEnum.SORT_BY_YOUR_REFERENCE.name(); 
        				orderSearchCriteria.setSortByColumn(OrderSearchCriteriaSortColumnEnum.SORT_BY_YOUR_REFERENCE);
        				break;
        		case DisplaySpecServices.YOURACCTREFERENCESORT: 
						callSortBy = OrderSearchCriteriaSortColumnEnum.SORT_BY_YOUR_ACC_REFERENCE.name(); 
						orderSearchCriteria.setSortByColumn(OrderSearchCriteriaSortColumnEnum.SORT_BY_YOUR_ACC_REFERENCE);
						break;
        		case DisplaySpecServices.REPUBLICATIONTITLESORT: 
        				callSortBy = OrderSearchCriteriaSortColumnEnum.SORT_BY_REPUBLICATION_TITLE.name(); 
        				orderSearchCriteria.setSortByColumn(OrderSearchCriteriaSortColumnEnum.SORT_BY_REPUBLICATION_TITLE);
        				break;
        		case DisplaySpecServices.REPUBLICATIONDATESORT: 
        				callSortBy = OrderSearchCriteriaSortColumnEnum.SORT_BY_REPUBLICATION_DATE.name(); 
        				orderSearchCriteria.setSortByColumn(OrderSearchCriteriaSortColumnEnum.SORT_BY_REPUBLICATION_DATE);
        				break;
        		case DisplaySpecServices.REPUBLICATIONPUBLISHERSORT: 
        				callSortBy = OrderSearchCriteriaSortColumnEnum.SORT_BY_REPUBLICATION_PUBLISHER.name();
        				orderSearchCriteria.setSortByColumn(OrderSearchCriteriaSortColumnEnum.SORT_BY_REPUBLICATION_PUBLISHER);
        				break;
        		case DisplaySpecServices.SCHOOLSORT: 
        				callSortBy = OrderSearchCriteriaSortColumnEnum.SORT_BY_INSTITUTION.name();
        				orderSearchCriteria.setSortByColumn(OrderSearchCriteriaSortColumnEnum.SORT_BY_INSTITUTION);
        				break;
        		case DisplaySpecServices.STARTOFTERMSORT: 
						callSortBy = OrderSearchCriteriaSortColumnEnum.SORT_BY_START_OF_TERM.name();
						orderSearchCriteria.setSortByColumn(OrderSearchCriteriaSortColumnEnum.SORT_BY_START_OF_TERM);
						break;
        		case DisplaySpecServices.COURSENAMESORT: 
						callSortBy = OrderSearchCriteriaSortColumnEnum.SORT_BY_COURSE_NAME.name();
						orderSearchCriteria.setSortByColumn(OrderSearchCriteriaSortColumnEnum.SORT_BY_COURSE_NAME);
						break;
        		case DisplaySpecServices.COURSENUMBERSORT: 
						callSortBy = OrderSearchCriteriaSortColumnEnum.SORT_BY_COURSE_NUMBER.name();
						orderSearchCriteria.setSortByColumn(OrderSearchCriteriaSortColumnEnum.SORT_BY_COURSE_NUMBER);
						break;
        		case DisplaySpecServices.INSTRUCTORSORT: 
        				callSortBy = OrderSearchCriteriaSortColumnEnum.SORT_BY_INSTRUCTOR.name();
        				orderSearchCriteria.setSortByColumn(OrderSearchCriteriaSortColumnEnum.SORT_BY_INSTRUCTOR);
        				break;
        		case DisplaySpecServices.LASTUPDATEDATESORT: 
    					callSortBy = OrderSearchCriteriaSortColumnEnum.SORT_BY_LAST_UPDATE_DATE.name();
    					orderSearchCriteria.setSortByColumn(OrderSearchCriteriaSortColumnEnum.SORT_BY_LAST_UPDATE_DATE);
    					break;
        		default: 
        				callSortBy = OrderSearchCriteriaSortColumnEnum.SORT_BY_DEFAULT.name();
						orderSearchCriteria.setSortByColumn(OrderSearchCriteriaSortColumnEnum.SORT_BY_DEFAULT);
        				break;            
        		}
       	 
       	 	switch (displaySpec.getSortOrder()) {
       	 		case DisplaySpecServices.SORTASCENDING: 
       	 			callSortOrder = OrderSearchCriteriaSortDirectionEnum.ASC.getUserText(); 
       	 			orderSearchCriteria.setSortDirection(OrderSearchCriteriaSortDirectionEnum.ASC);
       	 			break;
       	 		case DisplaySpecServices.SORTDESCENDING: 
       	 			callSortOrder = OrderSearchCriteriaSortDirectionEnum.DESC.getUserText(); 
       	 			orderSearchCriteria.setSortDirection(OrderSearchCriteriaSortDirectionEnum.DESC);
       	 			break;
       	 		default: callSortOrder = OrderMgmtSearchSortConstants.SORT_DIRECTION_ASCENDING; 
       	 			orderSearchCriteria.setSortDirection(OrderSearchCriteriaSortDirectionEnum.ASC);
       	 			break;            
       	 	}
       	 	
	    	orderSearchCriteria.addSortQualifierListForSortColumnAndDirection(orderSearchCriteria.getSortByColumn(), orderSearchCriteria.getSortDirection());
      	 	
       	 	Long searchLong = null;
       	 	Long categoryIdLong = null;
       	 	
       	 	switch (displaySpec.getSearchBy()) {
   	 		case DisplaySpecServices.CONFNUMFILTER: 
   	 			 try {
   	 				 searchLong = new Long(displaySpec.getSearch());
   	 			 } catch (Exception e) { 
   	 				 LOGGER.warn("error creating Long from " + displaySpec.getSearch() + LogUtil.appendableStack(e));
   	 				 searchLong = null; 
   	 			 }
   	 			 orderSearchCriteria.setConfirmNumber(searchLong);
   	 			 break; 
   	 		case DisplaySpecServices.PUBLICATIONTITLEFILTER: 
   	 			 orderSearchCriteria.setItemDescription(displaySpec.getSearch());
   	 			 break;
   	 		case DisplaySpecServices.ORDERDATEFILTER: 
   	 			 orderSearchCriteria.setOrderBegDate(displaySpec.getFromSearchDate());
   	 			 orderSearchCriteria.setOrderEndDate(displaySpec.getToSearchDate());
   	 			 //searchCriteria.setDateType("Order Date");
   	 			 break;
   	 		case DisplaySpecServices.ORDERDETAILIDFILTER: 
	 			 try {
	 				 searchLong = new Long(displaySpec.getSearch());
	 			 } catch (Exception e) { 
   	 				 LOGGER.warn("error creating Long from " + displaySpec.getSearch() + LogUtil.appendableStack(e));
   	 				 searchLong = null; 
   	 			 }
   	 			 orderSearchCriteria.setItemId(searchLong);
   	 			 break; 
   	 		case DisplaySpecServices.PERMISSIONTYPEFILTER:
   	 			 try {
   	 				 categoryIdLong = new Long(displaySpec.getSearch());
   	 			 } catch (Exception e) { 
   	 				 LOGGER.warn("error creating Long from " + displaySpec.getSearch() + LogUtil.appendableStack(e));
   	 				 searchLong = null; 
   	 			 }
   	 			 if (categoryIdLong != null) {
   	 				for (TouCategoryTypeEnum touCategoryTypeEnum : TouCategoryTypeEnum.values()) {
   	 					if (categoryIdLong.equals(touCategoryTypeEnum.getCategoryId())) {
   	 						orderSearchCriteria.setCategoryCd(touCategoryTypeEnum.name());
   	 						break;
   	 					}
   	 				}  
   	 			 }
 	 			 break; 
   	 		case DisplaySpecServices.PERMISSIONSTATUSFILTER: 
	 			 orderSearchCriteria.setIncludeCancelledItems(false);
 	 			 orderSearchCriteria.setPermissionStatus(displaySpec.getSearch());
	 			 break; 
   	 		case DisplaySpecServices.BILLINGSTATUSFILTER: 
   	 			 orderSearchCriteria.setBillingStatus(displaySpec.getSearch());
   	 			 break;
   	 	    case DisplaySpecServices.SPECIALORDERFILTER: 
   	 	    	 orderSearchCriteria.setSpecialOrderUpdate(displaySpec.getSearch());
   	 	    	 break;
   	 		
   	 		case DisplaySpecServices.COURSENAMEFILTER: 
	 			 orderSearchCriteria.setBundleName(displaySpec.getSearch());
	 			 break;
   	 		case DisplaySpecServices.COURSENUMBERFILTER: 
   	 			 orderSearchCriteria.setBundleCourseNum(displaySpec.getSearch());
   	 			 break;
   	 		case DisplaySpecServices.INSTRUCTORFILTER: 
  	 			 orderSearchCriteria.setInstructorName(displaySpec.getSearch());
  	 			 break;
   	 		case DisplaySpecServices.YOURREFERENCEFILTER: 
   	 			 orderSearchCriteria.setLcnHdrRefNum(displaySpec.getSearch());
   	 			 break;
   	 		case DisplaySpecServices.YOURACCTREFERENCEFILTER: 
	 			 orderSearchCriteria.setLcnAcctRefNum(displaySpec.getSearch());
	 			 break;
   	 		case DisplaySpecServices.INVOICENUMBERFILTER: 
   	 			 orderSearchCriteria.setInvoiceNumber(displaySpec.getSearch());
   	 			 break; 
   	 		case DisplaySpecServices.REPUBLICATIONTITLEFILTER: 
   	 			 orderSearchCriteria.setRepubTitle(displaySpec.getSearch());
   	 			 break;
   	 		case DisplaySpecServices.REPUBLICATIONDATEFILTER: 
	 			 orderSearchCriteria.setRepubBegDate(displaySpec.getFromSearchDate());
	 			 orderSearchCriteria.setRepubEndDate(displaySpec.getToSearchDate());
	 			 break; 
   	 		case DisplaySpecServices.REPUBLICATIONPUBLISHERFILTER: 
   	 			 orderSearchCriteria.setRepubPublisher(displaySpec.getSearch());
   	 			 break; 
   	 		case DisplaySpecServices.SCHOOLFILTER:
	 			 orderSearchCriteria.setOrganizationName(displaySpec.getSearch());
	 			 break; 
   	 		case DisplaySpecServices.YOURLINEITEMREFERENCEFILTER: 
   	 			 orderSearchCriteria.setLicenseeRefNum(displaySpec.getSearch());
   	 			 break; 
   	 		case DisplaySpecServices.INVOICEDATEFILTER: 
	 			 orderSearchCriteria.setInvoiceBegDate(displaySpec.getFromSearchDate());
	 			 orderSearchCriteria.setInvoiceEndDate(displaySpec.getToSearchDate());
   	 			 break;
   	 		case DisplaySpecServices.JOBTICKETNUMBERFILTER:  
   	 			 try { 
   	 				 Long jobTicketLong = new Long(displaySpec.getSearch());
   	 				 orderSearchCriteria.setJobTicketNumber(jobTicketLong);
   	 			 } catch (Exception e) {
   	 				LOGGER.warn("error creating jobTicketLong Long from " + displaySpec.getSearch() + LogUtil.appendableStack(e));
   	 			 }
	 			 break;
   	 		case DisplaySpecServices.LICENSENUMBERFILTER: 
	 			 try { 
	 				 Long licenseNumberLong = new Long(displaySpec.getSearch());
	 				 orderSearchCriteria.setExternalDetailId(licenseNumberLong);
	 			 } catch (Exception e) {
	 				LOGGER.warn("error creating Long for licenseNumberLong from " + displaySpec.getSearch() + LogUtil.appendableStack(e));	 				 
	 			 }
	 			 break;
   	 		case DisplaySpecServices.ARTICLETITLEFILTER: 
	 			 orderSearchCriteria.setItemSubDescription(displaySpec.getSearch());
	 			 break;
	 		case DisplaySpecServices.CHAPTERTITLEFILTER: 
			 	 orderSearchCriteria.setItemSubDescription(displaySpec.getSearch());
 			     break;
	 		default: 
   	 			 break;
    
       	 	}

            boolean hasValidAccountNumber = false;
            if (userContext.getActiveUser().getAccountNumber() != null) {
                    hasValidAccountNumber = true;
            }
                       
            if (userContext.getActiveUser().getIsAdmin() && hasValidAccountNumber) {
//                userList[0] = userContext.getActiveUser().getAccountNumber();
//                listType = OrderMgmtSearchSortConstants.LIST_TYPE_ORGANIZATION;
          		orderSearchCriteria.setLicenseeAccount(userContext.getActiveUser().getAccountNumber().toString());                
            } else {
//                userList[0] = userContext.getActiveAppUser().getPartyID();
//                listType = OrderMgmtSearchSortConstants.LIST_TYPE_INDIVIDUAL;
          		orderSearchCriteria.setBuyerPartyId(userContext.getActiveAppUser().getPartyID());
            }

       	 	
       	 	orderSearchCriteria.setSearchFromRow(displaySpec.getDisplayFromRow() + 1);
       	 	orderSearchCriteria.setSearchToRow(displaySpec.getDisplayToRow() + 1);
       		orderSearchCriteria.setDataSource(OrderDataSourceEnum.ALL.name());
//       		OrderSearchCriteria orderSearchCriteria = searchCriteria.getServiceSearchCriteriaForSearch();
//          	Long[] userList = new Long[1];
//          	userList[0] = userContext.getActiveAppUser().getPartyID(); 

       		orderSearchCriteria.setShowAdjustedValues(false);
       		
       		if (displaySpec.getSearchBy() == DisplaySpecServices.SPECIALORDERFILTER) {
       			//Disable search for TF Orders
       			orderSearchCriteria.setSufficientTfCriteria(false);
       		}
       		
       return orderSearchCriteria;   		
          		
  }
        

        public static Item getItemById(long Itemid) {
       	 return ServiceLocator.getOrderService().getItemById(new OrderConsumerContext(), Itemid);
        }
 
         public static Item updateItemWithNewPayment(Item item,Payment payment) {
        		return ServiceLocator.getOrderService().updateItemWithNewPayment(new OrderConsumerContext(), item,payment);
        	 
         }
        
        public static OrderSearchResult searchOrderDetailsForDisplaySpec() throws OrderLicensesException{
        	
        	CCUserContext userContext;
        	userContext = UserContextService.getUserContext();
        	OrderLicenses orderLicenses = UserContextService.getOrderLicenses(); 
            DisplaySpec displaySpec = UserContextService.getLicenseDisplaySpec(); 
            
            OrderSearchCriteria orderSearchCriteria = getOrderSearchCriteriaFromDisplaySpec(displaySpec);

            OrderSearchResult orderSearchResult = searchOrderLicenses(orderSearchCriteria);

          
            UserContextService.setOrderSearchResult(orderSearchResult);   	
           
           return orderSearchResult;
        	
        }
        
        
        public static LdapUser getLdapUserData(){
        	 CCUserContext userContext;
        	 userContext = UserContextService.getUserContext();
        	// userContext.getActiveAppUser().get
        	return userContext.getActiveUser().getLdapUser();
        	
        }
              
        public static String getOrderTotalStringForLicenses (List<OrderLicense> orderLicenses, String section) {
            BigDecimal totalOrderValue = new BigDecimal(0);
            boolean hasPricedItem = false;
            for (OrderLicense orderLicense : orderLicenses) {
            	if (section.equals(ItemConstants.TOTAL_ORDER) ||
            		(section.equals(ItemConstants.TOTAL_COURSE) && orderLicense.isAcademic()) ||
            		(section.equals(ItemConstants.TOTAL_SINGLE) && !orderLicense.isAcademic())) {
                	if (orderLicense.getItemStatusCd() != null && orderLicense.getItemStatusCd().equals(ItemStatusEnum.CANCELLED.name())) {
                		hasPricedItem = true;
                		continue;
                	}
            		if (orderLicense.getRightSourceCd().equals(RightSourceEnum.RL.name())) {
            			if ((orderLicense.getSpecialOrderTypeCd() != null && orderLicense.getSpecialOrderTypeCd().equals(SpecialOrderTypeEnum.NOT_SPECIAL_ORDER.name())) ||
            				(orderLicense.getItemAvailabilityCd() != null &&
            				 orderLicense.getItemAvailabilityCd().equals(ItemAvailabilityEnum.PURCHASE.getStandardPermissionCd()))) {
            				totalOrderValue = totalOrderValue.add(orderLicense.getTotalPriceValue());
            				hasPricedItem = true;        				
            			} else if (orderLicense.getProductCd().equals(ProductEnum.RLR.name())) {
            				if (orderLicense.getPriceValue() != ItemConstants.RL_NOT_PRICED) {
            					totalOrderValue = totalOrderValue.add(orderLicense.getTotalPriceValue());
            					hasPricedItem = true;
            				} 
            			}
            		} else if (orderLicense.getRightSourceCd().equals(RightSourceEnum.TF.name())) {
            			if ((orderLicense.getSpecialOrderTypeCd() != null && orderLicense.getSpecialOrderTypeCd().equals(SpecialOrderTypeEnum.NOT_SPECIAL_ORDER.name())) ||
            				(orderLicense.getItemAvailabilityCd() != null && 
            				 orderLicense.getItemAvailabilityCd().equals(ItemAvailabilityEnum.PURCHASE.getStandardPermissionCd()))) {
            				totalOrderValue = totalOrderValue.add(orderLicense.getTotalPriceValue());
            				hasPricedItem = true;        				
            			}
            		}        	   
            	}
            }
            if (!hasPricedItem) {
            	return ItemConstants.COST_TBD;
            }
            return "$" + totalOrderValue.setScale(2, BigDecimal.ROUND_HALF_EVEN).toString();
            
        }
        
        public static void getOverrideFeesForOrderLicense(OrderLicense orderLicense) {

        	String rlsTypeOfContentDescription;
        	if (orderLicense.getProductCd() != null && orderLicense.getProductCd().equals(ProductEnum.RLS.name())) {
        		rlsTypeOfContentDescription = orderLicense.getTypeOfContentDescription();
        	} else {
        		rlsTypeOfContentDescription = "";
        	}
        	
        	// Not throwing exception because only consequence is that override fees to not display on OMS UI
       		if (orderLicense.getOverrideAvailabilityCd() != null && orderLicense.getOverrideAvailabilityCd().equals(ItemAvailabilityEnum.PURCHASE.getStandardPermissionCd())) {
              	List<OverrideFee> overrideFeeList;
               	try {
                   	overrideFeeList = ServiceLocator.getRightsService().getOrverrideFeesForDetail(new RightsConsumerContext(), new Long(orderLicense.getID()));            
                } catch (Exception e) {
               		LOGGER.error("Retrieving Override Fees for License " + orderLicense.getID() + LogUtil.appendableStack(e));
               		overrideFeeList = null;
                }
                if (overrideFeeList != null) {
                   	for (OverrideFee overrideFee : overrideFeeList) {
                   		if (overrideFee.getFeeName() != null && overrideFee.getFeeValue() != null) {
                   			try {
                   				if (overrideFee.getFeeName().equals(ItemConstants.ENTIRE_BOOK_FEE)) {
                       				orderLicense.setEntireBookFeeOverrideValue(new BigDecimal(overrideFee.getFeeValue()));
                   				} else if (overrideFee.getFeeName().equals(ItemConstants.BASE_FEE)) {
                       				orderLicense.setBaseFeeOverrideValue(new BigDecimal(overrideFee.getFeeValue()));           					                   					
                   				} else if (overrideFee.getFeeName().equals(ItemConstants.FLAT_FEE)) {
                       				orderLicense.setFlatFeeOverrideValue(new BigDecimal(overrideFee.getFeeValue()));   
                   				} else if (overrideFee.getFeeName().equals(ItemConstants.PER_PAGE_FEE) && !orderLicense.getProductCd().equals(ProductEnum.RLS.name())) {
                       				orderLicense.setPerPageFeeOverrideValue(new BigDecimal(overrideFee.getFeeValue()));   
                   				} else if (overrideFee.getFeeName().equals(ItemConstants.MAXIMUM_ROYALTY_FEE)) {
                       				orderLicense.setMaximumRoyaltyFeeOverrideValue(new BigDecimal(overrideFee.getFeeValue()));  
                   				} else if (orderLicense.isRepublication()) {
                   					if (overrideFee.getFeeName().equals(FEE_FIELD_PER_CARTOON) && 
                   						   rlsTypeOfContentDescription.equals(TransactionConstants.CONTENT_CARTOONS)) {
                   						orderLicense.setPerContentFeeOverrideValue(new BigDecimal(overrideFee.getFeeValue()));   
                   					} else if (overrideFee.getFeeName().equals(FEE_FIELD_PER_CHART) && 
                						   rlsTypeOfContentDescription.equals(TransactionConstants.CONTENT_CHART)) {
                   						orderLicense.setPerContentFeeOverrideValue(new BigDecimal(overrideFee.getFeeValue()));   
                   					} else if (overrideFee.getFeeName().equals(FEE_FIELD_PER_LOGO) && 
                						   rlsTypeOfContentDescription.equals(TransactionConstants.CONTENT_LOGOS)) {
                   						orderLicense.setPerContentFeeOverrideValue(new BigDecimal(overrideFee.getFeeValue()));   
                   					} else if (overrideFee.getFeeName().equals(FEE_FIELD_PER_FIGURE) && 
             						       rlsTypeOfContentDescription.equals(TransactionConstants.CONTENT_FIGURE_DIAGRAM_TABLE)) {
                   						orderLicense.setPerContentFeeOverrideValue(new BigDecimal(overrideFee.getFeeValue()));   
                   					} else if (overrideFee.getFeeName().equals(FEE_FIELD_PER_ILLUSTRATION) && 
                						   rlsTypeOfContentDescription.equals(TransactionConstants.CONTENT_ILLUSTRATION)) {
                   						orderLicense.setPerContentFeeOverrideValue(new BigDecimal(overrideFee.getFeeValue()));   
                   					} else if (overrideFee.getFeeName().equals(FEE_FIELD_PER_GRAPH) && 
             						       rlsTypeOfContentDescription.equals(TransactionConstants.CONTENT_GRAPH)) {
                   						orderLicense.setPerContentFeeOverrideValue(new BigDecimal(overrideFee.getFeeValue()));   
                   					} else if (overrideFee.getFeeName().equals(FEE_FIELD_PER_QUOTE) && 
             						       rlsTypeOfContentDescription.equals(TransactionConstants.CONTENT_QUOTATION)) {
                   						orderLicense.setPerContentFeeOverrideValue(new BigDecimal(overrideFee.getFeeValue()));   
                   					} else if (overrideFee.getFeeName().equals(FEE_FIELD_PER_PHOTOGRAPH) && 
                						   rlsTypeOfContentDescription.equals(TransactionConstants.CONTENT_PHOTOGRAPH)) {
                   						orderLicense.setPerContentFeeOverrideValue(new BigDecimal(overrideFee.getFeeValue()));   
                   					} else if (overrideFee.getFeeName().equals(FEE_FIELD_PER_EXCERPT) && 
          						           rlsTypeOfContentDescription.equals(TransactionConstants.CONTENT_EXCERPT)) {
                   						orderLicense.setPerContentFeeOverrideValue(new BigDecimal(overrideFee.getFeeValue()));   
                   					} else if (overrideFee.getFeeName().equals(ItemConstants.PER_PAGE_FEE) && 
          						       rlsTypeOfContentDescription.equals(TransactionConstants.CONTENT_SELECTED_PAGES)) {
                   						orderLicense.setPerContentFeeOverrideValue(new BigDecimal(overrideFee.getFeeValue()));   
                   					} else if (overrideFee.getFeeName().equals(FEE_FIELD_PER_ARTICLE_NON_AUTHOR) && 
                   					   rlsTypeOfContentDescription.equals(TransactionConstants.CONTENT_FULL_ARTICLE_CHAPTER) &&
             						   !orderLicense.isSubmitterAuthor()) {
                   						orderLicense.setPerContentFeeOverrideValue(new BigDecimal(overrideFee.getFeeValue()));   
                   					} else if (overrideFee.getFeeName().equals(FEE_FIELD_PER_ARTICLE_AUTHOR) && 
                   					   rlsTypeOfContentDescription.equals(TransactionConstants.CONTENT_FULL_ARTICLE_CHAPTER) &&
              						   orderLicense.isSubmitterAuthor()) {
                    					orderLicense.setPerContentFeeOverrideValue(new BigDecimal(overrideFee.getFeeValue()));   
                   					}
                   				} else if (orderLicense.isNet()) {
                   					if (overrideFee.getFeeName().equals(FEE_FIELD_TO_30_DAYS_FEE) && orderLicense.getDuration() == 0) {
                   						orderLicense.setDurationFeeOverrideValue(new BigDecimal(overrideFee.getFeeValue()));  
                   					} else if (overrideFee.getFeeName().equals(FEE_FIELD_TO_180_DAYS_FEE) && orderLicense.getDuration() == 1) {
                   						orderLicense.setDurationFeeOverrideValue(new BigDecimal(overrideFee.getFeeValue()));  
                   					} else if (overrideFee.getFeeName().equals(FEE_FIELD_TO_365_DAYS_FEE) && orderLicense.getDuration() == 2) {
                   						orderLicense.setDurationFeeOverrideValue(new BigDecimal(overrideFee.getFeeValue()));  
                   					} else if (overrideFee.getFeeName().equals(FEE_FIELD_UNLIMITED_DAYS_FEE) && orderLicense.getDuration() == 3) {
                   						orderLicense.setDurationFeeOverrideValue(new BigDecimal(overrideFee.getFeeValue()));  
                   					}
                   				} else if (orderLicense.isEmail()) {
                   					if (overrideFee.getFeeName().equals(FEE_FIELD_TO_49_RECIPIENTS_FEE) && 
                   						orderLicense.getNumberOfRecipients() < 50) {
                   						orderLicense.setRecipientsFeeOverrideValue(new BigDecimal(overrideFee.getFeeValue()));  
                   					} else if (overrideFee.getFeeName().equals(FEE_FIELD_TO_249_RECIPIENTS_FEE) &&
                   						orderLicense.getNumberOfRecipients() >= 50 && orderLicense.getNumberOfRecipients() < 250) {
                   						orderLicense.setRecipientsFeeOverrideValue(new BigDecimal(overrideFee.getFeeValue()));  
                   					} else if (overrideFee.getFeeName().equals(FEE_FIELD_TO_499_RECIPIENTS_FEE) &&
                       						orderLicense.getNumberOfRecipients() >= 250 && orderLicense.getNumberOfRecipients() < 500) {
                   						orderLicense.setRecipientsFeeOverrideValue(new BigDecimal(overrideFee.getFeeValue()));  
                   					} else if (overrideFee.getFeeName().equals(FEE_FIELD_TO_500P_RECIPIENTS_FEE) &&
                       						orderLicense.getNumberOfRecipients() >= 500) {
                   						orderLicense.setRecipientsFeeOverrideValue(new BigDecimal(overrideFee.getFeeValue()));  
                   					}
                   				}                   				
                   			} catch (Exception e) {
               					LOGGER.error("Could not convert fee value from string " + orderLicense.getID() + "Fee: " + overrideFee.getFeeName() + "Value:" + overrideFee.getFeeValue() + LogUtil.appendableStack(e));
               				}
                   		}
                   	}
                }   	
       		}
        }

		public static void setPartnerIdMap(Map<String,String> partnerIdMap) {
			OrderLicenseServices.partnerIdMap = partnerIdMap;
		}

		public static Map<String,String> getPartnerIdMap() {
			return partnerIdMap;
		}
        
    }
    

