package com.copyright.ccc.business.services.order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.copyright.ccc.business.data.DisplaySpec;
import com.copyright.ccc.business.data.OrderBundle;
import com.copyright.ccc.business.data.OrderLicense;
import com.copyright.ccc.business.data.OrderLicenses;
import com.copyright.ccc.business.data.OrderPurchase;
import com.copyright.ccc.business.data.OrderPurchases;
import com.copyright.ccc.business.data.OrderSearchCriteria;
import com.copyright.ccc.business.data.OrderSearchCriteriaDataSourceEnum;
import com.copyright.ccc.business.data.OrderSearchCriteriaSortColumnEnum;
import com.copyright.ccc.business.data.OrderSearchCriteriaSortDirectionEnum;
import com.copyright.ccc.business.data.OrderSearchResult;
import com.copyright.ccc.business.data.OrderSearchRowNumbers;
import com.copyright.ccc.business.security.CCPrivilegeCode;
import com.copyright.ccc.business.security.CCUserContext;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.DisplaySpecServices;
import com.copyright.ccc.business.services.ItemConstants;
import com.copyright.ccc.business.services.OrderDataSourceEnum;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.business.services.user.User;
import com.copyright.ccc.business.services.user.UserServices;
import com.copyright.ccc.web.WebConstants;
import com.copyright.data.order.OrderMgmtException;
import com.copyright.data.order.OrderMgmtSearchSortConstants;
import com.copyright.data.order.Purchase;
import com.copyright.data.order.PurchasesResultsType;
import com.copyright.service.order.OrderMgmtServiceAPI;
import com.copyright.service.order.OrderMgmtServiceFactory;
import com.copyright.svc.order.api.data.Bundle;
import com.copyright.svc.order.api.data.Item;
import com.copyright.svc.order.api.data.ItemSearchCriteria;
import com.copyright.svc.order.api.data.ItemSearchResult;
import com.copyright.svc.order.api.data.OrderConsumerContext;
import com.copyright.svc.order.api.data.OrderHeader;
import com.copyright.svc.rightsResolver.api.data.TouCategoryTypeEnum;
import com.copyright.svc.tf.api.TFService;
import com.copyright.svc.tf.api.data.TFConsumerContext;

public class OrderPurchaseServices {

    // User Channel constants for organization or individual
    public static final String ORGADD = "ORGADD";
    public static final String ORG = "ORG";
    public static final String IND = "IND";

    private static final String SEARCH_CURRENT_USER = "Y";
    private static final String SEARCH_ALL_USERS = "N";

    private static final String INCLUDE_HIDDEN_PURCHASES = "Y";
    private static final String DO_NOT_INCLUDE_HIDDEN_PURCHASES = "N";

    private static Logger LOGGER = Logger.getLogger( OrderLicenseServices.class );
    
    private OrderPurchaseServices() {
    
    }
    
    public static OrderPurchase getOrderPurchaseForConfirmationNumber(long confirmationNumber) throws OrderPurchasesException {

    	OrderPurchase returnOrderPurchase = null;
    	
    	try {
    	   	returnOrderPurchase = getCOIOrderPurchaseForConfirmationNumber(confirmationNumber);
        	
        	if (returnOrderPurchase != null ) {
        		return returnOrderPurchase;
        	}
        	
//        	returnOrderPurchase = getTFOrderPurchaseForConfirmationNumber(confirmationNumber);
        		
    	} catch (OrderPurchasesException opex) {
//    		throw opex;
    	}

    	try {
        	returnOrderPurchase = getTFOrderPurchaseForConfirmationNumber(confirmationNumber);
    		
        	if (returnOrderPurchase != null ) {
        		return returnOrderPurchase;
        	}
        		
    	} catch (OrderPurchasesException opex) {
    		throw opex;
    	}

    	return returnOrderPurchase;

    }

    private static OrderPurchase getCOIOrderPurchaseForConfirmationNumber(long confirmationNumber) throws OrderPurchasesException {
    	
    	OrderPurchase orderPurchase = null;
    	OrderSearchResult orderSearchResult = null;
    	
    	try {
//    		orderHeaders = ServiceLocator.getOrderService().getOrdersByConfirmNumber(new OrderConsumerContext(), confirmationNumber);
    		OrderSearchCriteria orderSearchCriteria = new OrderSearchCriteria();
    		orderSearchCriteria.setConfirmNumber(new Long(confirmationNumber));
    		orderSearchCriteria.setDataSource(OrderDataSourceEnum.OMS.name());
    		orderSearchResult = searchOrderPurchases(orderSearchCriteria);
    		if (orderSearchResult.getOrderPurchaseList() != null && orderSearchResult.getOrderPurchaseList().size() > 0) {
    			orderPurchase = orderSearchResult.getOrderPurchaseList().get(0);
    		}
    	} catch (Exception e) {
            OrderPurchasesException orderPurchasesException = new OrderPurchasesException("Error Retrieving COI Order Purchases for Confirmation Number: " + confirmationNumber, e.getMessage(), e.getCause());
            throw orderPurchasesException;
    	}
    	
    	return orderPurchase;
    	
    }

    public static OrderPurchase getCOIOrderPurchaseForOrderHeaderId(Long orderHeaderId) throws OrderPurchasesException {
    	
    	OrderHeader orderHeader = null;
    	OrderPurchase orderPurchase = null;
    	OrderHeaderImpl orderHeaderImpl = new OrderHeaderImpl();
    	
    	try {
    		orderHeader = ServiceLocator.getOrderService().getOrderById(new OrderConsumerContext(), orderHeaderId);
    	} catch (Exception e) {
            OrderPurchasesException orderPurchasesException = new OrderPurchasesException("Error Retrieving COI Order Purchases for Order Header Number: " + orderHeaderId, e.getMessage(), e.getCause());
            throw orderPurchasesException;
    	}

   		orderHeaderImpl.setOrderHeader(orderHeader);
   		orderPurchase = orderHeaderImpl;
   		return orderPurchase;
    	
    }
    
    private static OrderPurchase getTFOrderPurchaseForConfirmationNumber(long confirmationNumber) throws OrderPurchasesException {
        CCUserContext userContext;
        
        // Temporary code to simulate an authenticated user
//        if (UserContextService.getUserContext().getActiveSharedUserID() == 0) {
//            UserContextService.getUserContext().getActiveUser().setID(UserContextService.getUserContext().getActiveUser().getPartyId().longValue());
//        }

        userContext = UserContextService.getUserContext();

        Long[] userList = new Long[1];
        userList[0] = userContext.getActiveSharedUserID();
       
        PurchasesResultsType prt = null;
        List<Purchase> purchaseList = null;
                      
        OrderPurchases orderPurchases = new OrderPurchases(); 
                        
        try {
            OrderMgmtServiceFactory orderFactory = OrderMgmtServiceFactory.getInstance();
            OrderMgmtServiceAPI orderService = orderFactory.getService();

            prt = orderService.getPurchasesWithPagingTFBorn(OrderMgmtSearchSortConstants.PURCHASE_SEARCH_TYPE_CONFIRM_NUM, String.valueOf(confirmationNumber), userList,
                                                     OrderMgmtSearchSortConstants.LIST_TYPE_INDIVIDUAL,
                                                     OrderMgmtSearchSortConstants.PURCHASE_SORT_TYPE_CONFIRM_NUM,
                                                     OrderMgmtSearchSortConstants.SORT_DIRECTION_DESCENDING,
                                                     OrderMgmtSearchSortConstants.ORDER_STATE_ALL_OPEN_CLOSED,
                                                     1, 2000, SEARCH_ALL_USERS, INCLUDE_HIDDEN_PURCHASES);
           
       //         orderPurchases.clearOrderLicenseList();
            purchaseList = Arrays.asList(prt.getPurchases());
            orderPurchases.setReadEndRow(purchaseList.size());
            orderPurchases.setTotalRowCount(prt.getNumPossibleRows().intValue());       
        } catch (OrderMgmtException omex) {
            OrderPurchasesException orderPurchasesException = new OrderPurchasesException("Error Retrieving TF Order Purchases for Confirmation Number: " + confirmationNumber, omex.getMessageCode(), omex.getCause());
            throw orderPurchasesException;
        }

            // Set the underlying Purchase object for each OrderPurchase
        int purchaseCtr = 0;
        if (purchaseList != null) {
        	if (purchaseList.size() > 0) {
        		OrderPurchaseImpl orderPurchaseImpl = new OrderPurchaseImpl();
        	    orderPurchaseImpl.setPurchase(purchaseList.get(0));       
        	    OrderPurchase orderPurchase = orderPurchaseImpl;
        	    return orderPurchase;
        	}
       }

        return null;
    }
    
    
    public static OrderPurchases getOrderPurchasesForConfNum(long confNum) throws OrderPurchasesException {
            
           CCUserContext userContext;
                        
            // Temporary code to simulate an authenticated user
//            if (UserContextService.getUserContext().getActiveSharedUserID() == 0) {
//                UserContextService.getUserContext().getActiveUser().setID(UserContextService.getUserContext().getActiveUser().getPartyId().longValue());
//            }

            userContext = UserContextService.getUserContext();

            Long[] userList = new Long[1];
            userList[0] = userContext.getActiveSharedUserID();
           
            PurchasesResultsType prt = null;
            List<Purchase> purchaseList = null;
                          
            OrderPurchases orderPurchases = new OrderPurchases(); 
                            
            try {
                OrderMgmtServiceFactory orderFactory = OrderMgmtServiceFactory.getInstance();
                OrderMgmtServiceAPI orderService = orderFactory.getService();

                prt = orderService.getPurchasesWithPagingTFBorn(OrderMgmtSearchSortConstants.PURCHASE_SEARCH_TYPE_CONFIRM_NUM, String.valueOf(confNum), userList,
                                                         OrderMgmtSearchSortConstants.LIST_TYPE_INDIVIDUAL,
                                                         OrderMgmtSearchSortConstants.PURCHASE_SORT_TYPE_CONFIRM_NUM,
                                                         OrderMgmtSearchSortConstants.SORT_DIRECTION_DESCENDING,
                                                         OrderMgmtSearchSortConstants.ORDER_STATE_ALL_OPEN_CLOSED,
                                                         1, 2000, SEARCH_ALL_USERS, INCLUDE_HIDDEN_PURCHASES);
               
           //         orderPurchases.clearOrderLicenseList();
                purchaseList = Arrays.asList(prt.getPurchases());
                orderPurchases.setReadEndRow(purchaseList.size());
                orderPurchases.setTotalRowCount(prt.getNumPossibleRows().intValue());       
            } catch (OrderMgmtException omex) {
                OrderPurchasesException orderPurchasesException = new OrderPurchasesException("Error Retrieving TF Order Purchases for Confirmation Number: " + confNum, omex.getMessageCode(), omex.getCause());
                throw orderPurchasesException;
            }

                // Set the underlying Purchase object for each OrderPurchase
            int purchaseCtr = 0;
            if (purchaseList != null) {
                for (purchaseCtr=0; purchaseCtr < purchaseList.size(); purchaseCtr++) {
                    orderPurchases.addOrderPurchase(purchaseList.get(purchaseCtr));
//                    OrderPurchaseServices.getLicenseTitles(orderPurchases.getOrderPurchase((purchaseCtr)));                    
                    orderPurchases.addDisplayPurchase( orderPurchases.getOrderPurchase(purchaseCtr));
                }
            }

            return orderPurchases;
    }
    
    public static User getUserForPurchase(OrderPurchase orderPurchase) {
        Long byrInstLong = orderPurchase.getByrInst();
        User orderPurchaseUser = UserServices.getUserByPtyInst( byrInstLong );
        orderPurchase.setUser(orderPurchaseUser);
        return orderPurchaseUser;
    }
    
    public static boolean cancelPurchase(long confirmationNumber) throws OrderPurchasesException {
    	
    	try {
    			OrderPurchase orderPurchase = getOrderPurchaseForConfirmationNumber(confirmationNumber);
    			if (orderPurchase.getOrderDataSource() == OrderSearchCriteriaDataSourceEnum.TF.getIntVaue()) {
            		return cancelTfPurchase(orderPurchase.getOrderHeaderId().longValue());
            	} else {
            		return cancelCoiPurchase(orderPurchase);
            	}
            	
            } catch (OrderMgmtException omex) {
                OrderPurchasesException orderPurchasesException = new OrderPurchasesException("Error Cancelling License " + confirmationNumber, omex.getMessageCode(), omex.getCause());
                throw orderPurchasesException;
            }

//            return false;
        }

    private static boolean isInvoiced(Item item) {
    	
    	TFService tfService = ServiceLocator.getTFService();
        
        String Status = tfService.getOderDetailStatusByOdtInst(new TFConsumerContext(), Long.valueOf(item.getItemId())).toString();
        
        if (Status.equalsIgnoreCase("2000"))
        {
        	return true;
        }
        else
        {
        	return false;
        }
    	
   }

    private static boolean cancelCoiPurchase(OrderPurchase orderPurchase) throws OrderPurchasesException {
	 	
		OrderLicense orderLicenseToCancel = null;
		List<String> errorsList = new ArrayList<String>();
		String message = "";
		int i = 0;
		
		try {
	       	Long confirmationNumber = new Long(orderPurchase.getConfirmationNumber());
	       	if (confirmationNumber > 0) {
	 			OrderSearchResult orderSearchResult = OrderLicenseServices.searchAllOrderLicensesForConfirmationNumber(confirmationNumber);
				OrderLicenses orderLicenses = orderSearchResult.getOrderLicensesObject();
				Set<Item> cancelItems = new HashSet<Item>();
				for (OrderLicense orderLicense : orderLicenses.getOrderLicenseList()) {
					orderLicenseToCancel = orderLicense;
					i++;
					if (orderLicenseToCancel.isCancelable()) {
						OrderItemImpl orderItemImpl = (OrderItemImpl) orderLicense;
						if (!isInvoiced(orderItemImpl.getItem())) {
							cancelItems.add(orderItemImpl.getItem());
							//OrderLicenseServices.cancelLicense(orderLicenseToCancel);
						}
						else
						{
							message = i + ". Item# " + orderItemImpl.getItem().getItemId().toString() + " is invoiced.";
							errorsList.add(message);
						}									
					}
				}	  
				if( !errorsList.isEmpty() )
		    		UserContextService.getHttpSession().setAttribute(WebConstants.RequestKeys.ORDER_CASCADE_CANCEL_INVOICED_STRINGS, errorsList);
				if (!cancelItems.isEmpty()) {
					ServiceLocator.getOrderService().cancelItems(new OrderConsumerContext(), cancelItems);
				}  
	       	}
 		} catch (Exception e) {
			OrderPurchasesException orderPurchasesException = new OrderPurchasesException("Error Cancelling License: " + orderLicenseToCancel.getOrderId(), e.getMessage(), e.getCause());
			throw orderPurchasesException;
		}
		UserContextService.getLicenseDisplaySpec().setForceReRead(true);
		UserContextService.getPurchaseDisplaySpec().setForceReRead(true);

		return true; 
	}    

    private static boolean cancelTfPurchase(long purchaseID) throws OrderPurchasesException {
        try {
            OrderMgmtServiceFactory orderFactory = OrderMgmtServiceFactory.getInstance();
            OrderMgmtServiceAPI orderService = orderFactory.getService();

            orderService.cancelPurchase(purchaseID);
        } catch (OrderMgmtException omex) {
            OrderPurchasesException orderPurchasesException = new OrderPurchasesException("Error Canceling Purchase: " + purchaseID, omex.getMessageCode(), omex.getCause());
            throw orderPurchasesException;
        }
        UserContextService.getLicenseDisplaySpec().setForceReRead(true);
        UserContextService.getPurchaseDisplaySpec().setForceReRead(true);
       
        return true;
    }

    public static OrderPurchase updateOrderPurchase (OrderPurchase orderPurchase) throws OrderPurchasesException {
    	boolean updateSuccess;
       	
    	OrderPurchase returnOrderPurchase = null;
    	try {
        	if (orderPurchase.getOrderDataSource() == OrderSearchCriteriaDataSourceEnum.TF.getIntVaue()) {
        		updateSuccess = updateTFOrderPurchase(orderPurchase);
        	} else {
        		updateSuccess = updateCOIOrderPurchase(orderPurchase);
        	}
        	
        } catch (OrderPurchasesException opex) {
            throw opex;
        }

    	if (updateSuccess) {
    		try {
        		returnOrderPurchase = getOrderPurchaseForConfirmationNumber(orderPurchase.getConfirmationNumber()); 
    		}
    		catch (OrderPurchasesException opex) {
                throw opex;
    		}

    	}
    	
    	return returnOrderPurchase;
    }
    
    
    private static boolean updateTFOrderPurchase(OrderPurchase orderPurchase) throws OrderPurchasesException {
        
        try {
            OrderMgmtServiceFactory orderFactory = OrderMgmtServiceFactory.getInstance();
            OrderMgmtServiceAPI orderService = orderFactory.getService();

            orderService.updatePurchase(orderPurchase.getPurchase());

        } catch (OrderMgmtException omex) {
            OrderPurchasesException orderPurchasesException = new OrderPurchasesException("Error Updating Purchase: " + orderPurchase.getPurInst(), omex.getMessageCode(), omex.getCause());
            throw orderPurchasesException;
        }
        UserContextService.getLicenseDisplaySpec().setForceReRead(true);
        UserContextService.getPurchaseDisplaySpec().setForceReRead(true);

        return true;
    }
    
    private static boolean updateCOIOrderPurchase(OrderPurchase orderPurchase) throws OrderPurchasesException {

//    	OrderPurchase currentOrderPurchase = null; 
    	
//    	try {
//    		currentOrderPurchase = getOrderPurchaseForConfirmationNumber(orderPurchase.getConfirmationNumber());
//    	} catch (OrderPurchasesException opex) {
//    		throw opex;
//    	}
    	
    	
//    	OrderHeaderImpl currentOrderHeaderImpl = (OrderHeaderImpl) currentOrderPurchase;
       	OrderHeaderImpl newOrderHeaderImpl = (OrderHeaderImpl) orderPurchase;
           	
//      currentOrderHeaderImpl.setPurchaseOnHold(newOrderHeaderImpl.getPurchaseOnHold());
     	 
//    	currentOrderHeaderImpl.setPoNumber(newOrderHeaderImpl.getPoNumber());
   	
    	try {
        	ServiceLocator.getOrderService().updateOrderHeader(new OrderConsumerContext(), newOrderHeaderImpl.getOrderHeader());            
        } catch (Exception e) {
            OrderPurchasesException orderPurchasesException = new OrderPurchasesException("Error Updating License: " + orderPurchase.getConfirmationNumber(), e.getMessage(), e.getCause());
            throw orderPurchasesException;
        }

    	return true;
    }

    
    
    public static OrderBundle updateOrderBundle(OrderPurchase orderPurchase, OrderBundle orderBundle) throws OrderPurchasesException {

    	OrderBundle returnOrderBundle = orderBundle;
    	try {
        	if (orderPurchase.getOrderDataSource() == OrderSearchCriteriaDataSourceEnum.TF.getIntVaue()) {
        		updateTFOrderPurchase(orderPurchase);
        	} else {
        		OrderItemBundleImpl bundleImpl = (OrderItemBundleImpl) orderBundle;
        		Bundle bundle = bundleImpl.getBundle();
            	try {
                	ServiceLocator.getOrderService().updateBundle(new OrderConsumerContext(), bundle);            
                } catch (Exception e) {
                    OrderPurchasesException orderPurchasesException = new OrderPurchasesException("Error Updating Purchase: " + orderPurchase.getConfirmationNumber(), e.getMessage(), e.getCause());
                    throw orderPurchasesException;
                }
        	}
        } catch (OrderPurchasesException opex) {
            throw opex;
        }
    	
		
		return returnOrderBundle;
		
    }

    public static OrderSearchResult searchCOIOrderPurchases(OrderSearchCriteria orderSearchCriteria) throws OrderPurchasesException {
        
    	OrderSearchResult orderPurchaseSearchResult = new OrderSearchResult();

    	int fromRow = orderSearchCriteria.getSearchFromRow();
    	int toRow = orderSearchCriteria.getSearchToRow();
    	int callFromRow = orderSearchCriteria.getOrderSearchRowNumbers().getTfStart();
    	int callToRow = orderSearchCriteria.getOrderSearchRowNumbers().getTfEnd();
    	
    	orderPurchaseSearchResult.setDisplayFromRow(fromRow);
    	orderPurchaseSearchResult.setDisplayToRow(toRow);
    	
    	ItemSearchCriteria itemSearchCriteria = orderSearchCriteria.getCoiItemSearchCriteria();
    	
	    ItemSearchResult orderSearchResult = ServiceLocator.getOrderService().searchOrdersOnlyByCriteria(new OrderConsumerContext(), itemSearchCriteria);
   	    LOGGER.debug("Got orderSearchResult: " + orderSearchResult.getTotalCount());
  	        	    
    	
    	initOrderPurchaseSearchResultFromOrderSearchResult(orderPurchaseSearchResult, orderSearchResult);
    	
        return orderPurchaseSearchResult;
    }
    
    public static OrderSearchResult searchFullCOIOrderPurchaseByConfirmNumber(Long confirmNumber) throws OrderPurchasesException {
        
    	OrderSearchResult orderSearchResult = new OrderSearchResult();
    	OrderHeader orderHeader = null;
    	
   		List<OrderHeader> orderHeaderList = ServiceLocator.getOrderService().getOrdersByConfirmNumber(new OrderConsumerContext(), confirmNumber);
   		if (orderHeaderList != null && orderHeaderList.size() == 1) {
   	    	orderHeader = ServiceLocator.getOrderService().getOrderById(new OrderConsumerContext(), orderHeaderList.get(0).getOrderHeaderId());
   		}
  
   		if (orderHeader != null) {
   			
   			OrderHeaderImpl orderHeaderImpl = new OrderHeaderImpl();
   			orderHeaderImpl.setOrderHeader(orderHeader);
   			orderSearchResult.addOrderPurchase(orderHeaderImpl);
   		
   			List<Item> itemList = new ArrayList<Item>(orderHeader.getItems());
   		
   			for (Item item : itemList) {
   				OrderItemImpl orderItemImpl = new OrderItemImpl ();
   				orderItemImpl.setItem(item);
   				orderSearchResult.addOrderLicense(orderItemImpl);
   			}
   		
   			List<Bundle> bundleList = new ArrayList<Bundle>(orderHeader.getBundles());
   			for (Bundle bundle : bundleList) {
   	   			OrderItemBundleImpl orderItemBundleImpl = new OrderItemBundleImpl();
   	   			orderItemBundleImpl.setBundle(bundle);
            	orderSearchResult.addOrderBundle(orderItemBundleImpl);
   			}
   		}
  		
   		return orderSearchResult;
    		
    }
    
    public static boolean updateFullCOIOrderPurchase(OrderPurchase orderPurchase) throws OrderPurchasesException {

    	OrderLicenseServices.initializeNullFeesForNonPurchaseOrderPurchaseUpdates (orderPurchase);
    	
       	OrderHeaderImpl orderHeaderImpl = (OrderHeaderImpl) orderPurchase;
           
    	try {
        	ServiceLocator.getOrderService().updateOrder(new OrderConsumerContext(), orderHeaderImpl.getOrderHeader());            
        } catch (Exception e) {
            OrderPurchasesException orderPurchasesException = new OrderPurchasesException("Error Updating License: " + orderPurchase.getConfirmationNumber(), e.getMessage(), e.getCause());
            throw orderPurchasesException;
        }

    	return true;
    }

    private static OrderSearchResult searchTfOrderPurchases(OrderSearchRowNumbers orderSearchRowNumbers ) throws OrderPurchasesException {

        CCUserContext userContext;
        
        userContext = UserContextService.getUserContext();
        
        OrderSearchResult orderSearchResult = new OrderSearchResult();
        
// Temporary code to simulate an authenticated user
//        if (UserContextService.getUserContext().getActiveSharedUserID() == 0) {
//            DisplaySpec tempDisplaySpec = UserContextService.getPurchaseDisplaySpec(); 
//            UserContextService.getUserContext().getAuthenticatedUser().setID(UserContextService.getUserContext().getActiveAppUser().getID());
//            UserContextService.getUserContext().getActiveUser().setID(UserContextService.getUserContext().getActiveAppUser().getID());
//            String usernm = UserContextService.getUserContext().getActiveUser().getUsername();
//          userContext = UserContextService.newUserContext("kmeyer@copyright.com",true);
//            userContext = UserContextService.newUserContext(usernm,true);
//            DisplaySpecServices.updateDisplaySpec(tempDisplaySpec);
//        }

        userContext = UserContextService.getUserContext();
         
        Long[] userList = new Long[1];
        userList[0] = userContext.getActiveAppUser().getPartyID();
        String hiddenPurchasesFlag = DO_NOT_INCLUDE_HIDDEN_PURCHASES;
        // Temporary Code
//        UserContextService.getUserContext().getActiveUser().setPartyId(userList[0]);
//        UserContextService.getUserContext().getAuthenticatedUser().setPartyId(userList[0]);
//        UserContextService.getUserContext().getActiveUser().setID(userList[0].longValue());
//        UserContextService.getUserContext().getAuthenticatedUser().setID(userList[0].longValue());
        
//        userList[0] = new Long(userContext.getActiveSharedUserID());

        boolean reReadFromDB = false;
        
        PurchasesResultsType prt = null;
        List<Purchase> purchaseList = null;
//        Purchase[] purchases =  null;
        
// Replace with get from User Context
        OrderPurchases orderPurchases = UserContextService.getOrderPurchases(); 
        DisplaySpec displaySpec = UserContextService.getPurchaseDisplaySpec(); 
        int readNumberOfPurchaseRows = displaySpec.getResultsPerPage();

        String searchUserScope = SEARCH_CURRENT_USER;
//        if (UserContextService.hasPrivilege(CCPrivilegeCode.ENTER_ORDER_ADJUSTMENT) && (!UserContextService.isEmulating())){
//            searchUserScope = SEARCH_ALL_USERS;
//           hiddenPurchasesFlag = INCLUDE_HIDDEN_PURCHASES;
//       } 

        if (searchUserScope == SEARCH_ALL_USERS && displaySpec.getSearchBy() == 0) {
            displaySpec.setSearchBy(DisplaySpecServices.ORDERDATEFILTER);
            GregorianCalendar defaultDate = new GregorianCalendar();
            defaultDate.add(GregorianCalendar.DATE, 1);
            displaySpec.setSearchFromDate(defaultDate.getTime());
            displaySpec.setSearchToDate(defaultDate.getTime());
        }
        
        if (orderPurchases.getTotalRowCount() == 0 ||
            displaySpec.isDisplaySpecParametersChanged()) {
                orderPurchases.setReadStartRow(1);
                orderPurchases.setReadEndRow(readNumberOfPurchaseRows);
                displaySpec.setDisplayFromRow(0);
                reReadFromDB = true;
        }
        if (displaySpec.getDisplayToRow() >= orderPurchases.getReadEndRow() &&
            orderPurchases.getTotalRowCount() > orderPurchases.getReadEndRow()) {
                orderPurchases.setReadStartRow(displaySpec.getDisplayFromRow() + 1);
                orderPurchases.setReadEndRow(orderPurchases.getReadStartRow() + (readNumberOfPurchaseRows - 1));
                reReadFromDB = true;
        }               
        if ((displaySpec.getDisplayFromRow() + 1) < orderPurchases.getReadStartRow()) {
                orderPurchases.setReadStartRow(displaySpec.getDisplayFromRow() + 1);
                orderPurchases.setReadEndRow(orderPurchases.getReadStartRow() + (readNumberOfPurchaseRows - 1));
                reReadFromDB = true;
        }               
        if (displaySpec.isForceReRead()) {
            reReadFromDB = true;
        }
               
        if (reReadFromDB) {
          String callSortBy = null;
          String callSortOrder = null;
          String callOrderState = null;
          String callSearchBy = null;
//          OrderMgmtSearchSortConstants o = null;
          
          switch (displaySpec.getSortBy()) {
              case DisplaySpecServices.ORDERDATESORT: callSortBy = OrderMgmtSearchSortConstants.PURCHASE_SORT_TYPE_PURCHASE_DATE; break;
              case DisplaySpecServices.CONFNUMSORT: callSortBy = OrderMgmtSearchSortConstants.PURCHASE_SORT_TYPE_CONFIRM_NUM; break;
              case DisplaySpecServices.SCHOOLSORT: callSortBy = OrderMgmtSearchSortConstants.PURCHASE_SORT_TYPE_UNIVERSITY; break;
              case DisplaySpecServices.STARTOFTERMSORT: callSortBy = OrderMgmtSearchSortConstants.PURCHASE_SEARCH_TYPE_START_OF_TERM;  break;
              case DisplaySpecServices.COURSENAMESORT: callSortBy = OrderMgmtSearchSortConstants.PURCHASE_SORT_TYPE_COURSE_NAME; break;
              case DisplaySpecServices.COURSENUMBERSORT: callSortBy = OrderMgmtSearchSortConstants.PURCHASE_SORT_TYPE_COURSE_NUMBER; break;
              case DisplaySpecServices.INSTRUCTORSORT: callSortBy = OrderMgmtSearchSortConstants.PURCHASE_SORT_TYPE_INSTRUCTOR; break;
              case DisplaySpecServices.YOURREFERENCESORT: callSortBy = OrderMgmtSearchSortConstants.PURCHASE_SORT_TYPE_LCN_HEADER_REF_NUM; break;
              case DisplaySpecServices.YOURACCTREFERENCESORT: callSortBy = OrderMgmtSearchSortConstants.PURCHASE_SORT_TYPE_LCN_ACCT_REF_NUM; break;
           default: callSortBy = OrderMgmtSearchSortConstants.PURCHASE_SORT_TYPE_PURCHASE_DATE; break;           
          }
 
          switch (displaySpec.getSortOrder()) {
                case DisplaySpecServices.SORTASCENDING: callSortOrder = OrderMgmtSearchSortConstants.SORT_DIRECTION_ASCENDING; break;
                case DisplaySpecServices.SORTDESCENDING: callSortOrder = OrderMgmtSearchSortConstants.SORT_DIRECTION_DESCENDING; break;
            default: callSortOrder = OrderMgmtSearchSortConstants.SORT_DIRECTION_ASCENDING; break;            
          }

          switch (displaySpec.getSearchBy()) {
                case DisplaySpecServices.CONFNUMFILTER: callSearchBy= OrderMgmtSearchSortConstants.PURCHASE_SEARCH_TYPE_CONFIRM_NUM; break;
                case DisplaySpecServices.ORDERDATEFILTER: callSearchBy = OrderMgmtSearchSortConstants.PURCHASE_SEARCH_TYPE_PURCHASE_DATE; break;
                case DisplaySpecServices.SCHOOLFILTER: callSearchBy = OrderMgmtSearchSortConstants.PURCHASE_SEARCH_TYPE_UNIVERSITY; break;
                case DisplaySpecServices.COURSENAMEFILTER: callSearchBy = OrderMgmtSearchSortConstants.PURCHASE_SEARCH_TYPE_COURSE_NAME; break;
                case DisplaySpecServices.COURSENUMBERFILTER: callSearchBy = OrderMgmtSearchSortConstants.PURCHASE_SEARCH_TYPE_COURSE_NUMBER; break;
                case DisplaySpecServices.INSTRUCTORFILTER: callSearchBy = OrderMgmtSearchSortConstants.PURCHASE_SEARCH_TYPE_INSTRUCTOR; break;
                case DisplaySpecServices.YOURREFERENCEFILTER: callSearchBy = OrderMgmtSearchSortConstants.PURCHASE_SEARCH_TYPE_LCN_HEADER_REF_NUM; break;
                case DisplaySpecServices.YOURACCTREFERENCEFILTER: callSearchBy = OrderMgmtSearchSortConstants.PURCHASE_SEARCH_TYPE_LCN_ACCT_REF_NUM; break;
                case DisplaySpecServices.STARTOFTERMFILTER: callSearchBy = OrderMgmtSearchSortConstants.PURCHASE_SEARCH_TYPE_START_OF_TERM; break;
                case DisplaySpecServices.INVOICENUMBERFILTER: callSearchBy = OrderMgmtSearchSortConstants.PURCHASE_SEARCH_TYPE_INVOICE_NUMBER; break;
                case DisplaySpecServices.LICENSENUMBERFILTER: callSearchBy = OrderMgmtSearchSortConstants.PURCHASE_SEARCH_TYPE_LICENSE_ID; break;
            default: callSearchBy = OrderMgmtSearchSortConstants.PURCHASE_SEARCH_TYPE_ALL; break;     
          }
 
        if (callSearchBy != OrderMgmtSearchSortConstants.PURCHASE_SEARCH_TYPE_ALL ||
            UserContextService.hasPrivilege(CCPrivilegeCode.ENTER_ORDER_ADJUSTMENT) && (!UserContextService.isEmulating())){
            callOrderState = OrderMgmtSearchSortConstants.ORDER_STATE_ALL_OPEN_CLOSED;
        }
        else { 
            switch (displaySpec.getOrderStateFilter()) {
                case DisplaySpecServices.ALLSTATES: callOrderState = OrderMgmtSearchSortConstants.ORDER_STATE_ALL_OPEN_CLOSED; break;
                case DisplaySpecServices.OPENSTATE: callOrderState = OrderMgmtSearchSortConstants.ORDER_STATE_OPEN; break;
                case DisplaySpecServices.CLOSEDSTATE: callOrderState = OrderMgmtSearchSortConstants.ORDER_STATE_CLOSED; break;
             default: callOrderState = OrderMgmtSearchSortConstants.ORDER_STATE_ALL_OPEN_CLOSED; break;
            }
        }

          try {
            OrderMgmtServiceFactory orderFactory = OrderMgmtServiceFactory.getInstance();
            OrderMgmtServiceAPI orderService = orderFactory.getService();

            prt = orderService.getPurchasesWithPagingTFBorn(callSearchBy, displaySpec.getSearch(),
                                                        userList, OrderMgmtSearchSortConstants.LIST_TYPE_INDIVIDUAL,
                                                        callSortBy,
                                                        callSortOrder,
                                                        callOrderState, orderSearchRowNumbers.getTfStart(), orderSearchRowNumbers.getTfEnd(), searchUserScope, hiddenPurchasesFlag);
            orderPurchases.clearOrderPurchaseList();
            purchaseList = Arrays.asList(prt.getPurchases());
            
            orderSearchResult.setTotalRows(prt.getNumPossibleRows().intValue());

            orderPurchases.setTotalRowCount(prt.getNumPossibleRows().intValue());  
            
          } catch (OrderMgmtException omex) {
                OrderPurchasesException orderPurchasesException = new OrderPurchasesException("Error Retrieving Order Purchases", omex.getMessageCode(), omex.getCause());
                throw orderPurchasesException;
          }
//          catch (OrderLicensesException ordLicenExp) {
//              OrderPurchasesException orderPurchasesException = new OrderPurchasesException("Error Retrieving Order Purchases", ordLicenExp.getMessageCode(), ordLicenExp.getCause());
//              throw orderPurchasesException;
//        }

        }

// 
//      Loop through orderPurchases and add rows to DisplayPurchases
        if (prt != null) {
        	if (prt.getPurchases() != null) {
            	for (Purchase purchase : prt.getPurchases()) {
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
        }
          
        DisplaySpecServices.updateDisplaySpec(displaySpec);
        return orderSearchResult;
    }
    
    private static void initOrderPurchaseSearchResultFromOrderSearchResult (OrderSearchResult orderSearchResult, ItemSearchResult itemSearchResult) {
	  	
       
/*        if (itemSearchResult.getOrderHeaderMap() != null) {
            Collection<OrderHeader> orderHeaderCollection = itemSearchResult.getOrderHeaderMap().values();         
        	for (OrderHeader orderHeader : orderHeaderCollection) {             
        		OrderHeaderImpl orderHeaderImpl = new OrderHeaderImpl();
                orderHeaderImpl.setOrderHeader(orderHeader);
                orderSearchResult.addOrderPurchase(orderHeaderImpl);
        	}
        }
*/        
        if (itemSearchResult.getOrderHeaderList() != null) {
        	for (OrderHeader orderHeader : itemSearchResult.getOrderHeaderList()) {             
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
    
    public static OrderSearchCriteria getOrderSearchCriteriaFromDisplaySpec(DisplaySpec displaySpec) {
    	
    	OrderSearchCriteria orderSearchCriteria = new OrderSearchCriteria();
    	
    	CCUserContext userContext;
    	userContext = UserContextService.getUserContext();
//    	OrderLicenses orderLicenses = UserContextService.getOrderLicenses(); 
//        DisplaySpec displaySpec = UserContextService.getLicenseDisplaySpec(); 
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

    	orderSearchCriteria.addPurchaseSortQualifierListForSortColumnAndDirection(orderSearchCriteria.getSortByColumn(), orderSearchCriteria.getSortDirection());

   	 	Long searchLong = null;
   	 	 Long categoryIdLong=null;
   	 	switch (displaySpec.getSearchBy()) {
   	 		case DisplaySpecServices.CONFNUMFILTER: 
   	 			 try {searchLong = new Long(displaySpec.getSearch());} catch (Exception e) { searchLong = null; }
   	 			 orderSearchCriteria.setConfirmNumber(searchLong);
   	 			 break; 
   	 		case DisplaySpecServices.PUBLICATIONTITLEFILTER: 
   	 			 orderSearchCriteria.setItemDescription(displaySpec.getSearch());
   	 			 break;
   	 		case DisplaySpecServices.ORDERDATEFILTER: 
   	 			 orderSearchCriteria.setOrderBegDate(displaySpec.getFromSearchDate());
   	 			 orderSearchCriteria.setOrderEndDate(displaySpec.getToSearchDate());
   	 			 break;
   	 		case DisplaySpecServices.STARTOFTERMFILTER: 
	 			 orderSearchCriteria.setUseBegDate(displaySpec.getFromSearchDate());
	 			 orderSearchCriteria.setUseEndDate(displaySpec.getToSearchDate());
	 			 break;
	 	    case DisplaySpecServices.ORDERDETAILIDFILTER: 
	 			 try {searchLong = new Long(displaySpec.getSearch());} catch (Exception e) { searchLong = null; }
   	 			 orderSearchCriteria.setItemId(searchLong);
   	 			 break; 
   	 		case DisplaySpecServices.PERMISSIONTYPEFILTER: 
   	 			 //orderSearchCriteria.setCategoryName(displaySpec.getSearch());
   	 			 
   	 		 try {categoryIdLong = new Long(displaySpec.getSearch());} catch (Exception e) { searchLong = null; }
	 			 if (categoryIdLong != null) {
	 				for (TouCategoryTypeEnum touCategoryTypeEnum : TouCategoryTypeEnum.values()) {
	 					if (categoryIdLong.equals(touCategoryTypeEnum.getCategoryId())) {
	 						orderSearchCriteria.setCategoryCd(touCategoryTypeEnum.name());
	 						break;
	 					}
	 				}  
	 			 }
	 			 break; 
   	 		case DisplaySpecServices.INSTRUCTORFILTER:
   	 			 orderSearchCriteria.setInstructorName(displaySpec.getSearch());
	 			 break; 
   	 		case DisplaySpecServices.PERMISSIONSTATUSFILTER: 
	 			 orderSearchCriteria.setItemAvailabilityCd(displaySpec.getSearch());
	 			 orderSearchCriteria.setPermissionStatus(displaySpec.getSearch());
	 			 break; 
   	 		case DisplaySpecServices.BILLINGSTATUSFILTER: 
//   	 			 orderSearchCriteria.getIsInvoiced()setsetsetOrderStatus_adv(displaySpec.getSearch());
   	 		     orderSearchCriteria.setBillingStatus(displaySpec.getSearch());
   	 			 break; 
   	 		case DisplaySpecServices.COURSENAMEFILTER: 
	 			 orderSearchCriteria.setBundleName(displaySpec.getSearch());
	 			 break;
   	 		case DisplaySpecServices.COURSENUMBERFILTER: 
   	 			 orderSearchCriteria.setBundleCourseNum(displaySpec.getSearch());
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
	 			 orderSearchCriteria.setUseBegDate(displaySpec.getFromSearchDate());
	 			 orderSearchCriteria.setUseEndDate(displaySpec.getToSearchDate());
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
   	 			 } catch (Exception e) {}
	 			 break;
   	 		case DisplaySpecServices.LICENSENUMBERFILTER: 
	 			 try { 
	 				 Long licenseNumberLong = new Long(displaySpec.getSearch());
	 				 orderSearchCriteria.setExternalDetailId(licenseNumberLong);
	 			 } catch (Exception e) {}
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
   	 	
        switch (displaySpec.getOrderStateFilter()) {
        case DisplaySpecServices.ALLSTATES: 
        	orderSearchCriteria.setOrderState(ItemConstants.ORDER_STATE_ALL);
        	break;
        case DisplaySpecServices.OPENSTATE: 
        	orderSearchCriteria.setOrderState(ItemConstants.ORDER_STATE_OPEN);
    	    break;
        case DisplaySpecServices.CLOSEDSTATE: 
        	orderSearchCriteria.setOrderState(ItemConstants.ORDER_STATE_CLOSED);
        	break;
        default:
       		orderSearchCriteria.setOrderState(ItemConstants.ORDER_STATE_ALL);
       		break;             
        }
   	 	
//   	 	if (displaySpec.getDisplayFromRow() == 0) {
//   	 		displaySpec.setDisplayFromRow(1);
//   	 		displaySpec.setDisplayToRow(displaySpec.getDisplayToRow());
//   	 	}
   	 	
   	 	orderSearchCriteria.setSearchFromRow(displaySpec.getDisplayFromRow() + 1);
   	 	orderSearchCriteria.setSearchToRow(displaySpec.getDisplayToRow() + 1);
   		orderSearchCriteria.setDataSource(OrderDataSourceEnum.ALL.name());
//   		OrderSearchCriteria orderSearchCriteria = searchCriteria.getServiceSearchCriteriaForSearch();
//      	Long[] userList = new Long[1];
//    	userList[0] = userContext.getActiveAppUser().getPartyID(); 

        boolean hasValidAccountNumber = false;
        if (userContext.getActiveUser().getAccountNumber() != null) {
            hasValidAccountNumber = true;
        }

   		if (hasValidAccountNumber && userContext.getActiveUser().getIsAdmin()) {
//          userList[0] = userContext.getActiveUser().getAccountNumber();
//          listType = OrderMgmtSearchSortConstants.LIST_TYPE_ORGANIZATION;
    		orderSearchCriteria.setLicenseeAccount(userContext.getActiveUser().getAccountNumber().toString());                
      } else {
//          userList[0] = userContext.getActiveAppUser().getPartyID();
//          listType = OrderMgmtSearchSortConstants.LIST_TYPE_INDIVIDUAL;
    		orderSearchCriteria.setBuyerPartyId(userContext.getActiveAppUser().getPartyID());
      }


   return orderSearchCriteria;   		
      		
}
    
    public static OrderSearchResult searchOrderPurchases(OrderSearchCriteria orderSearchCriteria) throws OrderPurchasesException {
        
    	//OrderSearchResult orderLicenseSearchResult = new OrderSearchResult();

//    	int fromRow = orderSearchCriteria.getSearchFromRow();
//    	int toRow = orderSearchCriteria.getSearchToRow();
    	
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

	    LOGGER.debug("Beginning TF Start: " + orderSearchCriteria.getOrderSearchRowNumbers().getTfStart() + " COI Start: " + orderSearchCriteria.getOrderSearchRowNumbers().getCoiStart());    	            	
	    LOGGER.debug("Beginning TF End: " + orderSearchCriteria.getOrderSearchRowNumbers().getTfEnd() + " COI End: " + orderSearchCriteria.getOrderSearchRowNumbers().getCoiEnd());    	            	

    	// If greater than max, don't do it
//    	orderLicenseSearchResult.setDisplayFromRow(fromRow);
//    	orderLicenseSearchResult.setDisplayToRow(toRow);

    	// If greater than max, don't do it

       	OrderSearchResult tfOrderSearchResult;
       	OrderSearchResult coiOrderSearchResult;

	    LOGGER.debug("DataSource " + orderSearchCriteria.getDataSource());    	            	
//	    if (orderSearchCriteria.getDataSource() == null) {
//	    	orderSearchCriteria.setDataSource(OrderSearchCriteriaDataSourceEnum.ALL.name());
//	    }
	  
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

        SearchThread omsSearchThread = new SearchThread(SearchThread.SEARCH_TYPE_PURCHASE, orderSearchCriteria);
        omsSearchThread.start();

  	    
/*       	if (orderSearchCriteria.getDataSource().equals(OrderSearchCriteriaDataSourceEnum.ALL.name()) ||
           	orderSearchCriteria.getDataSource().equals(OrderSearchCriteriaDataSourceEnum.OMS.name())) {        	
       		orderSearchCriteria.setSearchFromRow(orderSearchRowNumbers.getCoiStart());
       		orderSearchCriteria.setSearchToRow(orderSearchRowNumbers.getCoiEnd());   	    
    	    coiOrderSearchResult = searchCOIOrderPurchases(orderSearchCriteria);
       		LOGGER.debug("Found COI Purchases: " + coiOrderSearchResult.getTotalRows());
       	} else {
       		coiOrderSearchResult = new OrderSearchResult();
       		coiOrderSearchResult.setOrderBundles(new HashMap<Long,OrderBundle>());
       		coiOrderSearchResult.setOrderLicenses(new ArrayList<OrderLicense>());
       		coiOrderSearchResult.setOrderPurchases(new HashMap<Long, OrderPurchase>());
       		coiOrderSearchResult.setOrderPurchaseList(new ArrayList<OrderPurchase>());
       		coiOrderSearchResult.setDisplayFromRow(0);
       		coiOrderSearchResult.setDisplayToRow(0);
       		coiOrderSearchResult.setTotalRows(0);
       	}
*/       	
       	
       	
//       	if ((orderSearchCriteria.getConfirmNumber() != null ||
//       		orderSearchCriteria.getInvoiceNumber() != null ||
//       		orderSearchCriteria.getOrderBegDate() != null ||
//       		orderSearchCriteria.getItemId() != null ||
//       		orderSearchCriteria.getUseBegDate() != null) &&
       		if ((orderSearchCriteria.getJobTicketNumber() == null &&
		    	orderSearchCriteria.getItemSubDescription() == null) &&
		    	(orderSearchCriteria.getDataSource().equals(OrderSearchCriteriaDataSourceEnum.ALL.name()) ||
       			 orderSearchCriteria.getDataSource().equals(OrderSearchCriteriaDataSourceEnum.TF.name()))) {
       			 orderSearchCriteria.setSearchTfFromRow(orderSearchRowNumbers.getTfStart());
       			 orderSearchCriteria.setSearchTfToRow(orderSearchRowNumbers.getTfEnd());
       			 LOGGER.debug("Searching TF Purchases");
    	    try { 
    	        tfOrderSearchResult = searchTfOrderPurchases(orderSearchRowNumbers);
    	    	LOGGER.debug("Retrieved TF Purchases: " + tfOrderSearchResult.getNumberOfDisplayLicenses() + " out of " + tfOrderSearchResult.getTotalRows());
       		} catch (OrderPurchasesException opex) {
       			OrderPurchasesException orderPurchasesException = new OrderPurchasesException("Error Retrieving Order Purchases", opex.getMessageCode(), opex.getCause());
       			throw orderPurchasesException;
       		}
       	} else {
       		tfOrderSearchResult = new OrderSearchResult();
       		tfOrderSearchResult.setOrderBundles(new HashMap<Long,OrderBundle>());
       		tfOrderSearchResult.setOrderLicenses(new ArrayList<OrderLicense>());
       		tfOrderSearchResult.setOrderPurchases(new HashMap<Long, OrderPurchase>());
       		tfOrderSearchResult.setOrderPurchaseList(new ArrayList<OrderPurchase>());
       		tfOrderSearchResult.setDisplayFromRow(0);
       		tfOrderSearchResult.setDisplayToRow(0);
       		tfOrderSearchResult.setTotalRows(0);
       	}
	    
    	try {
    		omsSearchThread.join();
		} catch (InterruptedException e) {
			throw new OrderPurchasesException(e);
		}
		    
		if (omsSearchThread.isError()) {
			throw omsSearchThread.getOrderPurchasesException();
		}
			
		coiOrderSearchResult = omsSearchThread.getOrderSearchResult();	
       		
   		LOGGER.debug("Found COI Purchases: " + coiOrderSearchResult.getTotalRows());
	       		
    	OrderSearchResult combinedSearchResult = new OrderSearchResult();

		orderSearchCriteria.getOrderSearchRowNumbers().setTfRowsFound(tfOrderSearchResult.getTotalRows());
		orderSearchCriteria.getOrderSearchRowNumbers().setCoiRowsFound(coiOrderSearchResult.getTotalRows());       	
	
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
      	
//       	if (orderSearchCriteria.getOrderSearchRowNumbers().getSearchFromRow() > orderSearchCriteria.getOrderSearchRowNumbers().getLastSearchFromRow()) {
//       		displayReturnStart = orderSearchCriteria.getOrderSearchRowNumbers().getSearchFromRow() - orderSearchCriteria.getOrderSearchRowNumbers().getLastSearchToRow();
//       		displayReturnEnd = orderSearchCriteria.getOrderSearchRowNumbers().getSearchToRow() - orderSearchCriteria.getOrderSearchRowNumbers().getSearchFromRow();
//       	} else if (orderSearchCriteria.getOrderSearchRowNumbers().getSearchFromRow() < orderSearchCriteria.getOrderSearchRowNumbers().getLastSearchFromRow()) {
//       		displayReturnStart = 1;
//       		displayReturnEnd = orderSearchCriteria.getOrderSearchRowNumbers().getSearchToRow() - orderSearchCriteria.getOrderSearchRowNumbers().getSearchFromRow();
//       	}
       	
       	
    	while (compareComplete == false) {
   			if (c < coiOrderSearchResult.getNumberOfDisplayPurchases() && t < tfOrderSearchResult.getNumberOfDisplayPurchases()) {
   				if (compareTfCoiRowForNext(orderSearchCriteria, coiOrderSearchResult, c, tfOrderSearchResult, t).equals("C") ) {
//   					combinedSearchResult.addOrderLicense(coiOrderSearchResult.getOrderLicenseByDisplaySequence(c));
   					combinedSearchResult.addOrderPurchase(coiOrderSearchResult.getOrderPurchases().get(c));
//   					if (coiOrderSearchResult.getOrderBundleByDisplaySequence(c) != null) {
//   						combinedSearchResult.addOrderBundle(coiOrderSearchResult.getOrderBundleByDisplaySequence(c));
//   					}
   					c++;
   				} else { 	            	
  //					combinedSearchResult.addOrderLicense(tfOrderSearchResult.getOrderLicenseByDisplaySequence(t));
   					combinedSearchResult.addOrderPurchase(tfOrderSearchResult.getOrderPurchases().get(t));
//   					if (tfOrderSearchResult.getOrderBundleByDisplaySequence(t) != null) {
//   						combinedSearchResult.addOrderBundle(tfOrderSearchResult.getOrderBundleByDisplaySequence(t));
//   					}
   					t++;
   				}
   			} else if (c < coiOrderSearchResult.getNumberOfDisplayPurchases()) {
//   				combinedSearchResult.addOrderLicense(coiOrderSearchResult.getOrderLicenseByDisplaySequence(c));
   					combinedSearchResult.addOrderPurchase(coiOrderSearchResult.getOrderPurchases().get(c));
//					if (coiOrderSearchResult.getOrderBundleByDisplaySequence(c) != null) {
//						combinedSearchResult.addOrderBundle(coiOrderSearchResult.getOrderBundleByDisplaySequence(c));
//					}
					c++;
   			} else if (t < tfOrderSearchResult.getNumberOfDisplayPurchases()) {
//   				combinedSearchResult.addOrderLicense(tfOrderSearchResult.getOrderLicenseByDisplaySequence(t));
   					combinedSearchResult.addOrderPurchase(tfOrderSearchResult.getOrderPurchases().get(t));
//					if (tfOrderSearchResult.getOrderBundleByDisplaySequence(t) != null) {
//						combinedSearchResult.addOrderBundle(tfOrderSearchResult.getOrderBundleByDisplaySequence(t));
//					}
					t++;
   			} else {
   				compareComplete = true;
   			}
   			i++;
   		}

    	int searchCtr = 0;
    	int searchStart = 0;
    	int searchEnd = 0;
    	
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

	    LOGGER.debug("Search Start: " + searchStart + " Search End: " + searchEnd);    	            	
       	
       	
    	OrderSearchResult returnSearchResult = new OrderSearchResult();
    	boolean rowsRemain = true;
    	int ds = 0;
    	while (searchCtr <= searchEnd && ds < combinedSearchResult.getOrderPurchases().size()) {
    		if (combinedSearchResult.getOrderPurchases().get(ds).getOrderDataSource() == OrderDataSourceEnum.OMS.getOrderDataSourceId().intValue()) {
    			cUsed++;
    		} else {
    			tUsed++;
    		}
    		
    		if (searchCtr >= orderSearchCriteria.getSearchFromRow() || orderSearchCriteria.getConfirmNumber() != null) {
//    			returnSearchResult.addOrderLicense(combinedSearchResult.getOrderLicenseByDisplaySequence(ds));
    			returnSearchResult.addOrderPurchase(combinedSearchResult.getOrderPurchases().get(ds));
//					if (combinedSearchResult.getOrderBundleByDisplaySequence(ds) != null) {
//						returnSearchResult.addOrderBundle(combinedSearchResult.getOrderBundleByDisplaySequence(ds));        			
//					}
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
    	
//        UserContextService.setOrderPurchases(returnSearchResult.getOrderPurchasesObject()); 
    	
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
//			if (coiOrderSearchResult.getOrderLicenseByDisplaySequence(coiRow).getID() >= tfOrderSearchResult.getOrderLicenseByDisplaySequence(tfRow).getID()) {
			if (coiOrderSearchResult.getOrderPurchaseByListSequence(coiRow).getOrderDate() == null) {
				if (tfOrderSearchResult.getOrderPurchaseByListSequence(tfRow).getOrderDate() == null) {
					return getCoiGreaterReturnValue(orderSearchCriteria);
				} else {
					return getTfGreaterReturnValue(orderSearchCriteria);
				}
			} else if (tfOrderSearchResult.getOrderPurchaseByListSequence(tfRow).getOrderDate() == null) {
				return getCoiGreaterReturnValue(orderSearchCriteria);
			} else if (coiOrderSearchResult.getOrderPurchaseByListSequence(coiRow).getOrderDate().compareTo(tfOrderSearchResult.getOrderPurchaseByListSequence(tfRow).getOrderDate()) >= 0) {
				return getCoiGreaterReturnValue(orderSearchCriteria);
			} else {
				return getTfGreaterReturnValue(orderSearchCriteria);
			}
		} else if (orderSearchCriteria.getSortByColumn().getDisplayOrder() == OrderSearchCriteriaSortColumnEnum.SORT_BY_ORDER_DATE.getDisplayOrder()) {
			if (coiOrderSearchResult.getOrderPurchaseByListSequence(coiRow).getOrderDate() == null) {
				if (tfOrderSearchResult.getOrderPurchaseByListSequence(tfRow).getOrderDate() == null) {
					return getCoiGreaterReturnValue(orderSearchCriteria);
				} else {
					return getTfGreaterReturnValue(orderSearchCriteria);
				}
			} else if (tfOrderSearchResult.getOrderPurchaseByListSequence(tfRow).getOrderDate() == null) {
				return getCoiGreaterReturnValue(orderSearchCriteria);
			} else if (coiOrderSearchResult.getOrderPurchaseByListSequence(coiRow).getOrderDate().compareTo(tfOrderSearchResult.getOrderPurchaseByListSequence(tfRow).getOrderDate()) >= 0) {
				return getCoiGreaterReturnValue(orderSearchCriteria);
			} else {
				return getTfGreaterReturnValue(orderSearchCriteria);
			}
		} else if (orderSearchCriteria.getSortByColumn().getDisplayOrder() == OrderSearchCriteriaSortColumnEnum.SORT_BY_CONFIRM_NUMBER.getDisplayOrder()) {
			if (coiOrderSearchResult.getOrderPurchaseByListSequence(coiRow).getConfirmationNumber() >= tfOrderSearchResult.getOrderPurchaseByListSequence(tfRow).getConfirmationNumber()) {
	   				return getCoiGreaterReturnValue(orderSearchCriteria);
    		} else {
    			return getTfGreaterReturnValue(orderSearchCriteria);
    		}
		} else if (orderSearchCriteria.getSortByColumn().getDisplayOrder() == OrderSearchCriteriaSortColumnEnum.SORT_BY_START_OF_TERM.getDisplayOrder()) {
			boolean coiValueIsNull = false;
			if (coiOrderSearchResult.getOrderBundleByPurchaseSequence(coiRow) == null) {
				coiValueIsNull = true;
			} else if (coiOrderSearchResult.getOrderBundleByPurchaseSequence(coiRow).getStartOfTerm() == null) {
				coiValueIsNull = true;
			}
			boolean tfValueIsNull = false;
			if (tfOrderSearchResult.getOrderBundleByPurchaseSequence(tfRow) == null) {
				tfValueIsNull = true;
			} else if (tfOrderSearchResult.getOrderBundleByPurchaseSequence(tfRow).getStartOfTerm() == null) {
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
			} else if (coiOrderSearchResult.getOrderBundleByPurchaseSequence(coiRow).getStartOfTerm().compareTo(tfOrderSearchResult.getOrderBundleByPurchaseSequence(tfRow).getStartOfTerm()) >= 0) {
				return getCoiGreaterReturnValue(orderSearchCriteria);
			} else {
				return getTfGreaterReturnValue(orderSearchCriteria);
			}
		} else if (orderSearchCriteria.getSortByColumn().getDisplayOrder() == OrderSearchCriteriaSortColumnEnum.SORT_BY_COURSE_NAME.getDisplayOrder()) {
			boolean coiValueIsNull = false;
			if (coiOrderSearchResult.getOrderBundleByPurchaseSequence(coiRow) == null) {
				coiValueIsNull = true;
			} else if (coiOrderSearchResult.getOrderBundleByPurchaseSequence(coiRow).getCourseName() == null) {
				coiValueIsNull = true;
			}
			boolean tfValueIsNull = false;
			if (tfOrderSearchResult.getOrderBundleByPurchaseSequence(tfRow) == null) {
				tfValueIsNull = true;
			} else if (tfOrderSearchResult.getOrderBundleByPurchaseSequence(tfRow).getCourseName() == null) {
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
			} else if (coiOrderSearchResult.getOrderBundleByPurchaseSequence(coiRow).getCourseName().compareTo(tfOrderSearchResult.getOrderBundleByPurchaseSequence(tfRow).getCourseName()) >= 0) {
				return getCoiGreaterReturnValue(orderSearchCriteria);
			} else {
				return getTfGreaterReturnValue(orderSearchCriteria);
			}
		} else if (orderSearchCriteria.getSortByColumn().getDisplayOrder() == OrderSearchCriteriaSortColumnEnum.SORT_BY_COURSE_NUMBER.getDisplayOrder()) {
			boolean coiValueIsNull = false;
			if (coiOrderSearchResult.getOrderBundleByPurchaseSequence(coiRow) == null) {
				coiValueIsNull = true;
			} else if (coiOrderSearchResult.getOrderBundleByPurchaseSequence(coiRow).getCourseNumber() == null) {
				coiValueIsNull = true;
			}
			boolean tfValueIsNull = false;
			if (tfOrderSearchResult.getOrderBundleByPurchaseSequence(tfRow) == null) {
				tfValueIsNull = true;
			} else if (tfOrderSearchResult.getOrderBundleByPurchaseSequence(tfRow).getCourseNumber() == null) {
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
			} else if (coiOrderSearchResult.getOrderBundleByPurchaseSequence(coiRow).getCourseNumber().compareTo(tfOrderSearchResult.getOrderBundleByPurchaseSequence(tfRow).getCourseNumber()) >= 0) {
				return getCoiGreaterReturnValue(orderSearchCriteria);
			} else {
				return getTfGreaterReturnValue(orderSearchCriteria);
			}
		} else if (orderSearchCriteria.getSortByColumn().getDisplayOrder() == OrderSearchCriteriaSortColumnEnum.SORT_BY_INSTITUTION.getDisplayOrder()) {
			boolean coiValueIsNull = false;
			if (coiOrderSearchResult.getOrderBundleByPurchaseSequence(coiRow) == null) {
				coiValueIsNull = true;
			} else if (coiOrderSearchResult.getOrderBundleByPurchaseSequence(coiRow).getOrganization() == null) {
				coiValueIsNull = true;
			}
			boolean tfValueIsNull = false;
			if (tfOrderSearchResult.getOrderBundleByPurchaseSequence(tfRow) == null) {
				tfValueIsNull = true;
			} else if (tfOrderSearchResult.getOrderBundleByPurchaseSequence(tfRow).getOrganization() == null) {
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
			} else if (coiOrderSearchResult.getOrderBundleByPurchaseSequence(coiRow).getOrganization().compareTo(tfOrderSearchResult.getOrderBundleByPurchaseSequence(tfRow).getOrganization()) >= 0) {
				return getCoiGreaterReturnValue(orderSearchCriteria);
			} else {
				return getTfGreaterReturnValue(orderSearchCriteria);
			}
		} else if (orderSearchCriteria.getSortByColumn().getDisplayOrder() == OrderSearchCriteriaSortColumnEnum.SORT_BY_INSTRUCTOR.getDisplayOrder()) {
			boolean coiValueIsNull = false;
			if (coiOrderSearchResult.getOrderBundleByPurchaseSequence(coiRow) == null) {
				coiValueIsNull = true;
			} else if (coiOrderSearchResult.getOrderBundleByPurchaseSequence(coiRow).getInstructor() == null) {
				coiValueIsNull = true;
			}
			boolean tfValueIsNull = false;
			if (tfOrderSearchResult.getOrderBundleByPurchaseSequence(tfRow) == null) {
				tfValueIsNull = true;
			} else if (tfOrderSearchResult.getOrderBundleByPurchaseSequence(tfRow).getInstructor() == null) {
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
			} else if (coiOrderSearchResult.getOrderBundleByPurchaseSequence(coiRow).getInstructor().compareTo(tfOrderSearchResult.getOrderBundleByPurchaseSequence(tfRow).getInstructor()) >= 0) {
				return getCoiGreaterReturnValue(orderSearchCriteria);
			} else {
				return getTfGreaterReturnValue(orderSearchCriteria);
			}
		} else if (orderSearchCriteria.getSortByColumn().getDisplayOrder() == OrderSearchCriteriaSortColumnEnum.SORT_BY_YOUR_REFERENCE.getDisplayOrder()) {
//			if (coiOrderSearchResult.getOrderBundleByDisplaySequence(coiRow).getYourReference().compareTo(tfOrderSearchResult.getOrderBundleByDisplaySequence(tfRow).getYourReference()) >= 0) {
//	   				return getCompareReturnValue(orderSearchCriteria);
//				}
			boolean coiValueIsNull = false;
			if (coiOrderSearchResult.getOrderBundleByPurchaseSequence(coiRow) == null) {
				coiValueIsNull = true;
			} else if (coiOrderSearchResult.getOrderBundleByPurchaseSequence(coiRow).getYourReference() == null) {
				coiValueIsNull = true;
			}
			boolean tfValueIsNull = false;
			if (tfOrderSearchResult.getOrderBundleByDisplaySequence(tfRow) == null) {
				tfValueIsNull = true;
			} else if (tfOrderSearchResult.getOrderBundleByPurchaseSequence(tfRow).getYourReference() == null) {
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
			} else if (coiOrderSearchResult.getOrderBundleByPurchaseSequence(coiRow).getYourReference().compareTo(tfOrderSearchResult.getOrderBundleByPurchaseSequence(tfRow).getYourReference()) >= 0) {
				return getCoiGreaterReturnValue(orderSearchCriteria);
			} else {
				return getTfGreaterReturnValue(orderSearchCriteria);
			}
		} else if (orderSearchCriteria.getSortByColumn().getDisplayOrder() == OrderSearchCriteriaSortColumnEnum.SORT_BY_YOUR_ACC_REFERENCE.getDisplayOrder()) {
//			if (coiOrderSearchResult.getOrderBundleByDisplaySequence(coiRow).getAccountingReference().compareTo(tfOrderSearchResult.getOrderBundleByDisplaySequence(tfRow).getAccountingReference()) >= 0) {
//	   				return getCompareReturnValue(orderSearchCriteria);
//				}
			boolean coiValueIsNull = false;
			if (coiOrderSearchResult.getOrderBundleByPurchaseSequence(coiRow) == null) {
				coiValueIsNull = true;
			} else if (coiOrderSearchResult.getOrderBundleByPurchaseSequence(coiRow).getAccountingReference() == null) {
				coiValueIsNull = true;
			}
			boolean tfValueIsNull = false;
			if (tfOrderSearchResult.getOrderBundleByPurchaseSequence(tfRow) == null) {
				tfValueIsNull = true;
			} else if (tfOrderSearchResult.getOrderBundleByPurchaseSequence(tfRow).getAccountingReference() == null) {
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
			} else if (coiOrderSearchResult.getOrderBundleByPurchaseSequence(coiRow).getAccountingReference().compareTo(tfOrderSearchResult.getOrderBundleByPurchaseSequence(tfRow).getAccountingReference()) >= 0) {
				return getCoiGreaterReturnValue(orderSearchCriteria);
			} else {
				return getTfGreaterReturnValue(orderSearchCriteria);
			}
		} else if (orderSearchCriteria.getSortByColumn().getDisplayOrder() == OrderSearchCriteriaSortColumnEnum.SORT_BY_INTERNET_LOGIN.getDisplayOrder()) {
			if (coiOrderSearchResult.getOrderPurchaseByListSequence(coiRow).getBuyerUserIdentifier() == null) {
				if (tfOrderSearchResult.getOrderPurchaseByListSequence(tfRow).getBuyerUserIdentifier() == null) {
					return getCoiGreaterReturnValue(orderSearchCriteria);
				} else {
					return getTfGreaterReturnValue(orderSearchCriteria);
				}
			} else if (tfOrderSearchResult.getOrderPurchaseByListSequence(tfRow).getBuyerUserIdentifier() == null) {
				return getCoiGreaterReturnValue(orderSearchCriteria);
			} else if (coiOrderSearchResult.getOrderPurchaseByListSequence(coiRow).getBuyerUserIdentifier().compareTo(tfOrderSearchResult.getOrderPurchaseByListSequence(tfRow).getBuyerUserIdentifier()) >= 0) {
				return getCoiGreaterReturnValue(orderSearchCriteria);
			} else {
				return getTfGreaterReturnValue(orderSearchCriteria);
			}    			
			} else if (orderSearchCriteria.getSortByColumn().getDisplayOrder() == OrderSearchCriteriaSortColumnEnum.SORT_BY_LICENSEE_ACCOUNT.getDisplayOrder()) {
   			if (coiOrderSearchResult.getOrderPurchaseByListSequence(coiRow).getLicenseeName() == null) {
				if (tfOrderSearchResult.getOrderPurchaseByListSequence(tfRow).getLicenseeName() == null) {
					return getCoiGreaterReturnValue(orderSearchCriteria);
				} else {
					return getTfGreaterReturnValue(orderSearchCriteria);
				}
			} else if (tfOrderSearchResult.getOrderPurchaseByListSequence(tfRow).getLicenseeName() == null) {
				return getCoiGreaterReturnValue(orderSearchCriteria);
			} else if (coiOrderSearchResult.getOrderPurchaseByListSequence(coiRow).getLicenseeName().compareTo(tfOrderSearchResult.getOrderPurchaseByListSequence(tfRow).getLicenseeName()) >= 0) {
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
		return "C";
    }
}
