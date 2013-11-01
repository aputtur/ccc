package com.copyright.ccc.business.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.copyright.ccc.business.services.order.OrderPurchaseImpl;
import com.copyright.data.order.Purchase;


public class OrderPurchases implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	OrderPurchase _orderPurchase;
    List<OrderPurchase> _orderPurchaseList = new ArrayList<OrderPurchase>();
    List<OrderPurchase> _displayPurchaseList = new ArrayList<OrderPurchase>();
    int _totalRowCount = 0;
    int _readStartRow = 1;
    int _readEndRow = 25;
    
    public OrderPurchases() {    
    }

    public OrderPurchase getOrderPurchase(int arrayItem) {
//        if ((arrayItem >= (_readStartRow - 1)) & (arrayItem < ((_readStartRow - 1) + _orderPurchaseList.size()))) {
        if (arrayItem < _orderPurchaseList.size() ) {
            return _orderPurchaseList.get(arrayItem);
        } else {
            return null;
        }
    }

    public OrderPurchase getDisplayPurchase(int arrayItem) {
        if (arrayItem < _displayPurchaseList.size()) {
            return _displayPurchaseList.get(arrayItem);
        } else {
            return null;
        }
    }

    public void addDisplayPurchase(OrderPurchase orderPurchase) {
        _displayPurchaseList.add(orderPurchase);        
    }

    public void addOrderPurchase(Purchase purchase) {
        OrderPurchaseImpl orderPurchase = new OrderPurchaseImpl();
        orderPurchase.setPurchase(purchase);       
        _orderPurchaseList.add(orderPurchase);
//        int count = _orderPurchaseList.size();
//        this.setTotalRowCount(count);
//        _orderPurchaseRowCount = _orderPurchaseList.size();
//        _readEndRow = _readStartRow + (_orderPurchaseRowCount - 1);

    }

    public void setOrderPurchaseList(List<OrderPurchase> orderPurchaseList) {
        _orderPurchaseList = orderPurchaseList;
//        int count = _orderPurchaseList.size();
//        this.setTotalRowCount(count);
    }

    public void setTotalRowCount(int totalRowCount) {
        _totalRowCount = totalRowCount;
    }

    public List<OrderPurchase> getOrderPurchaseList() {
        return _orderPurchaseList;
    }

    public List<OrderPurchase> getDisplayPurchaseList() {
        return _displayPurchaseList;
    }

    public void clearDisplayPurchaseList() {
        _displayPurchaseList.clear();
    }

    public void clearOrderPurchaseList() {
//        _readStartRow = 1;
//        _readEndRow = OrderPurchaseServices.READ_NUMBER_OF_PURCHASE_ROWS;
//        _totalRowCount = 0;
        _orderPurchaseList.clear();
    }
    

    public int getTotalRowCount() {
        return _totalRowCount;
    }

    public int getDisplayRowCount() {
        return _displayPurchaseList.size();
    }

    public int getReadStartRow() {
        return _readStartRow;
    }

    public int getReadEndRow() {
        return _readEndRow;
    }

    public void setReadStartRow(int readStartRow) {
        this._readStartRow = readStartRow;
    }

    public void setReadEndRow(int readEndRow) {
        this._readEndRow = readEndRow;
    }
 
    public void setDisplayPurchaseList(List<OrderPurchase> displayPurchaseList) {
        this._displayPurchaseList = displayPurchaseList;
    }

}
