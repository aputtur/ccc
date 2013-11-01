package com.copyright.ccc.business.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.copyright.ccc.business.services.OrderDataSourceEnum;
import com.copyright.ccc.business.services.order.OrderItemImpl;
import com.copyright.ccc.business.services.order.OrderLicenseImpl;

public class OrderSearchResult implements Serializable {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	private HashMap<Long,OrderLicense> _orderLicenses;
	private List<OrderLicense> _orderLicenses = new ArrayList<OrderLicense>();
	private List<OrderPurchase> _orderPurchaseList = new ArrayList<OrderPurchase>();
	
	private HashMap<Long,OrderPurchase> _orderPurchases = new HashMap<Long,OrderPurchase>();
	private HashMap<Long,OrderBundle> _orderBundles = new HashMap<Long,OrderBundle>();
	
	private int _displayFromRow;
    private int _displayToRow;

    private int _totalRows;
    private OrderSearchCriteria orderSearchCriteria;

	public boolean isSufficientCriteria() {
		return orderSearchCriteria.isSufficientCriteria();
	}
	
	public OrderLicense getOrderLicenseByItemId(String itemId) {

		Long itemIdLong;
		try {
			itemIdLong = new Long (itemId);
		} catch (Exception e) {
			return null;
		}
		
		return getOrderLicenseByItemId(itemIdLong);
	}

	public OrderLicense getOrderLicenseByItemId(Long itemId) {
		for (OrderLicense orderLicense : _orderLicenses) {
			if (orderLicense.getID() == itemId.longValue()) {
				return orderLicense;
			}
		}
		
		return null;
	}
	
	public OrderPurchase getOrderPurchaseByConfirmId(String confirmId) {
		Long confirmIdLong;
		try {
			confirmIdLong = new Long (confirmId);
		} catch (Exception e) {
			return null;
		}
		
		return getOrderPurchaseByConfirmId(confirmIdLong);
	}

	public OrderPurchase getOrderPurchaseByConfirmId(Long confirmId) {
		
		for (OrderPurchase orderPurchase : _orderPurchases.values()) {
			if (orderPurchase.getConfirmationNumber() == confirmId.longValue()) {
				return orderPurchase;
			}
		}
		
		return null;
	}
	
	public OrderBundle getOrderBundleByBundleId(Long bundleId) {
		return _orderBundles.get(bundleId);
	}
	
	public OrderLicenses getOrderLicensesObject() {
		OrderLicenses orderLicenses = new OrderLicenses();
		
		orderLicenses.setOrderLicenseList(_orderLicenses);
		orderLicenses.setReadStartRow(_displayFromRow);
		orderLicenses.setReadEndRow(_displayToRow);
		orderLicenses.setTotalRowCount(_totalRows);
		
		return orderLicenses;
		
	}
	
	public OrderPurchases getOrderPurchasesObject() {
		OrderPurchases orderPurchases = new OrderPurchases();
		
		orderPurchases.setOrderPurchaseList(_orderPurchaseList);
		orderPurchases.setDisplayPurchaseList(_orderPurchaseList);
		orderPurchases.setReadStartRow(_displayFromRow);
		orderPurchases.setReadEndRow(_displayToRow);
		orderPurchases.setTotalRowCount(_totalRows);
		
		return orderPurchases;
		
	}
	
	public List<OrderLicense> getOrderLicenses() {
		return _orderLicenses;
	}

	public List<OrderPurchase> getOrderPurchaseList() {
		return _orderPurchaseList;
	}

	public void setOrderPurchaseList(List<OrderPurchase> purchaseList) {
		_orderPurchaseList = purchaseList;
	}

	
	public OrderLicense getOrderLicenseByDisplaySequence(int seqNum) {
		if (_orderLicenses == null || seqNum >= _orderLicenses.size())  {
			return null;
		}

		return _orderLicenses.get(seqNum);
	}
	
	public OrderPurchase getOrderPurchaseByListSequence(int seqNum) {
		if (_orderPurchaseList == null || seqNum >= _orderPurchaseList.size())  {
			return null;
		}
		return _orderPurchaseList.get(seqNum);
	}

	public OrderBundle getOrderBundleByPurchaseSequence(int seqNum) {
		if (_orderPurchases == null || seqNum >= _orderPurchases.size())  {
			return null;
		}
		
		ArrayList<OrderBundle> currentOrderBundles;
		
		OrderPurchase orderPurchase = _orderPurchaseList.get(seqNum);
		currentOrderBundles = orderPurchase.getOrderBundles();						

		if (currentOrderBundles != null) {
			for (OrderBundle orderBundle : currentOrderBundles) {
				return orderBundle;
			}
		}
		
		return null;
		
	}
	
	public OrderPurchase getOrderPurchaseByDisplaySequence(int seqNum) {
		if (_orderLicenses == null || seqNum >= _orderLicenses.size())  {
			return null;
		}
		return _orderPurchases.get(_orderLicenses.get(seqNum).getOrderHeaderId());
	}
	
	public OrderBundle getOrderBundleByDisplaySequence(int seqNum) {
		if (_orderLicenses == null || seqNum >= _orderLicenses.size())  {
			return null;
		}
		OrderLicense currentOrderLicense = _orderLicenses.get(seqNum);
				
		OrderBundle currentOrderBundle;
		
		if (currentOrderLicense.getOrderDataSource() == OrderDataSourceEnum.TF.getOrderDataSourceId().intValue()) {
			currentOrderBundle = _orderBundles.get(currentOrderLicense.getPurchaseId());			
		} else {
			currentOrderBundle = _orderBundles.get(currentOrderLicense.getBundleId());
		}		
		
		return currentOrderBundle;
	}
	
	public int getNumberOfDisplayLicenses () {
		if (_orderLicenses == null) {
			return 0;
		}
		return _orderLicenses.size();
	}
	
	public int getNumberOfDisplayPurchases () {
		if (_orderPurchases == null) {
			return 0;
		}
		return _orderPurchases.size();
	}
	
	public void setOrderLicenses(List<OrderLicense> licenses) {
		_orderLicenses = licenses;
	}

	public void initLicenseConfirmNumbers() {
		if (_orderPurchases == null) return;
		
		if (_orderPurchases.isEmpty()) return;
		
		if (_orderLicenses == null) return;
		
		if (_orderLicenses.isEmpty()) return;
		
		for (OrderLicense orderLicense : _orderLicenses) {
			if (orderLicense.getOrderDataSource() == OrderDataSourceEnum.OMS.getOrderDataSourceId().longValue()) {
				if (orderLicense.getPurchaseId() == 0) {
					OrderPurchase orderPurchase = _orderPurchases.get(orderLicense.getOrderHeaderId());
					if (orderPurchase != null) {
						OrderItemImpl orderItemImpl = (OrderItemImpl) orderLicense;
						orderItemImpl.setPurchaseId(orderPurchase.getConfirmationNumber());
					}
				}
			}
		}
	}
	
	public void initBundleComments() {
		if (_orderPurchases == null) return;
		
		if (_orderPurchases.isEmpty()) return;
		
		if (_orderLicenses == null) return;
		
		if (_orderLicenses.isEmpty()) return;
		
		for (OrderLicense orderLicense : _orderLicenses) {
			if (orderLicense.getOrderDataSource() == OrderDataSourceEnum.TF.getOrderDataSourceId().longValue()) {
				if (orderLicense.getBundleId() != null && orderLicense.getBundleId().longValue() > 0) {
					OrderBundle orderBundle = _orderBundles.get(orderLicense.getBundleId());
					if (orderBundle != null) {
						OrderLicenseImpl orderLicenseImpl = (OrderLicenseImpl) orderLicense;
						orderBundle.setComments(orderLicenseImpl.getBundleComments());
					}
				}
			}
		}
	}
	
	public void clearSearchResult() {
		_orderLicenses = new ArrayList<OrderLicense>(); 
		_orderPurchases = new HashMap<Long,OrderPurchase>();
		_orderBundles = new HashMap<Long,OrderBundle>();
		
	}
	
	public void addOrderLicense(OrderLicense orderLicense) {
		if (_orderLicenses == null) {
			_orderLicenses = new ArrayList<OrderLicense>(); 
		}
		_orderLicenses.add(orderLicense);
	}
	
	public void addOrderPurchase(OrderPurchase orderPurchase) {
		if (_orderPurchases == null) {
			_orderPurchases = new HashMap<Long, OrderPurchase>(); 
		}
		if (_orderPurchaseList == null) {
			_orderPurchaseList = new ArrayList<OrderPurchase>(); 
		}
		
		_orderPurchases.put(orderPurchase.getOrderHeaderId(), orderPurchase);
		_orderPurchaseList.add(orderPurchase);

	}
	
	public void addOrderBundle(OrderBundle orderBundle) {
		if (_orderBundles == null) {
			_orderBundles = new HashMap<Long, OrderBundle>(); 
		}
		Long bundleId = Long.valueOf(orderBundle.getBundleId());
		_orderBundles.put(bundleId, orderBundle);

	}
	
	public boolean isPurchaseExists (long confirmationNumber) {
		for (OrderPurchase orderPurchase : _orderPurchases.values()) {
			if (orderPurchase.getConfirmationNumber() == confirmationNumber) {
				return true;
			}
		}
		
		return false;
	}

	public boolean isBundleExists (long bundleId) {
		if (_orderPurchases.containsKey(bundleId)) {
			return true;
		}
		return false;
	}
	
	public HashMap<Long, OrderPurchase> getOrderPurchasesMap() {
		return _orderPurchases;
	}

	public List<OrderPurchase> getOrderPurchases() {
		return _orderPurchaseList;
	}
	
	public void setOrderPurchases(HashMap<Long, OrderPurchase> purchases) {
		_orderPurchases = purchases;
	}
	
	public HashMap<Long, OrderBundle> getOrderBundles() {
		return _orderBundles;
	}

	public void setOrderBundles(HashMap<Long, OrderBundle> bundles) {
		_orderBundles = bundles;
	}
    public int getDisplayFromRow() {
		return _displayFromRow;
	}

	public void setDisplayFromRow(int fromRow) {
		_displayFromRow = fromRow;
	}

	public int getDisplayToRow() {
		return _displayToRow;
	}

	public void setDisplayToRow(int toRow) {
		_displayToRow = toRow;
	}
	public int getTotalRows() {
		return _totalRows;
	}

	public void setTotalRows(int rows) {
		_totalRows = rows;
	}

	public OrderSearchCriteria getOrderSearchCriteria() {
		return orderSearchCriteria;
	}

	public void setOrderSearchCriteria(OrderSearchCriteria orderSearchCriteria) {
		this.orderSearchCriteria = orderSearchCriteria;
	}

}
