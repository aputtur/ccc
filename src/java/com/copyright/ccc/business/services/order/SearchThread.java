package com.copyright.ccc.business.services.order;

import java.util.ArrayList;
import java.util.HashMap;

import com.copyright.ccc.business.data.OrderBundle;
import com.copyright.ccc.business.data.OrderLicense;
import com.copyright.ccc.business.data.OrderPurchase;
import com.copyright.ccc.business.data.OrderSearchCriteria;
import com.copyright.ccc.business.data.OrderSearchCriteriaDataSourceEnum;
import com.copyright.ccc.business.data.OrderSearchResult;

public class SearchThread extends Thread {

	public static final String SEARCH_TYPE_PURCHASE = "PURCHASE";
	public static final String SEARCH_TYPE_LICENSE = "LICENSE";
	public static final String SEARCH_SYSTEM_OMS = "OMS";
	public static final String SEARCH_SYSTEM_TF = "TF";
	
	private String _searchType;
	private OrderSearchResult _orderSearchResult;
	private OrderSearchCriteria _orderSearchCriteria;
	private boolean _error = false;
	private OrderPurchasesException _orderPurchasesException;
	private OrderLicensesException _orderLicensesException;
	
	SearchThread (String searchType, OrderSearchCriteria orderSearchCriteria) {
		_searchType = searchType;
		_orderSearchCriteria = orderSearchCriteria;
	}
	
	@Override
	public void run() {
    	if (_searchType.equals(SEARCH_TYPE_PURCHASE)) {
    		runOmsOrderPurchaseSearch();
     	} else if (_searchType.equals(SEARCH_TYPE_LICENSE)) {
    		runOmsOrderLicenseSearch();
    	}
    }
	
	public void runOmsOrderPurchaseSearch () {

       	if (_orderSearchCriteria.getDataSource().equals(OrderSearchCriteriaDataSourceEnum.ALL.name()) ||
            _orderSearchCriteria.getDataSource().equals(OrderSearchCriteriaDataSourceEnum.OMS.name())) {        	
    		try {
    			_orderSearchResult = OrderPurchaseServices.searchCOIOrderPurchases(_orderSearchCriteria);
    		} catch (OrderPurchasesException orderPurchasesException) {
    			// TODO Auto-generated catch block
    			_error = true;
    			_orderPurchasesException = orderPurchasesException;
    		}
    	} else {
        	_orderSearchResult = new OrderSearchResult();
        	_orderSearchResult.setOrderBundles(new HashMap<Long,OrderBundle>());
        	_orderSearchResult.setOrderLicenses(new ArrayList<OrderLicense>());
        	_orderSearchResult.setOrderPurchases(new HashMap<Long, OrderPurchase>());
        	_orderSearchResult.setOrderPurchaseList(new ArrayList<OrderPurchase>());
        	_orderSearchResult.setDisplayFromRow(0);
        	_orderSearchResult.setDisplayToRow(0);
        	_orderSearchResult.setTotalRows(0);
        }
		

	}
	
	public void runOmsOrderLicenseSearch () {

       	if ((_orderSearchCriteria.getDataSource().equals(OrderSearchCriteriaDataSourceEnum.ALL.name()) ||
           	_orderSearchCriteria.getDataSource().equals(OrderSearchCriteriaDataSourceEnum.OMS.name())) &&
           	_orderSearchCriteria.isSufficientCoiCriteria()) {        	

    		try {
    			_orderSearchResult = OrderLicenseServices.searchCOIOrderLicenses(_orderSearchCriteria);
    		} catch (OrderLicensesException orderLicensesException) {
    			// TODO Auto-generated catch block
    			_error = true;
    			_orderLicensesException = orderLicensesException;
    		}    	    
      	} else {
       		
       		_orderSearchResult = new OrderSearchResult();
       		_orderSearchResult.setOrderBundles(new HashMap<Long,OrderBundle>());
       		_orderSearchResult.setOrderLicenses(new ArrayList<OrderLicense>());
       		_orderSearchResult.setOrderPurchases(new HashMap<Long, OrderPurchase>());
       		_orderSearchResult.setDisplayFromRow(0);
       		_orderSearchResult.setDisplayToRow(0);
       		_orderSearchResult.setTotalRows(0);
       	}
		
		

	}
	
	public OrderPurchasesException getOrderPurchasesException() {
		return _orderPurchasesException;
	}

	public OrderLicensesException getOrderLicensesException() {
		return _orderLicensesException;
	}

	public OrderSearchResult getOrderSearchResult() {
		return _orderSearchResult;
	}

	public boolean isError() {
		return _error;
	}

	
}
