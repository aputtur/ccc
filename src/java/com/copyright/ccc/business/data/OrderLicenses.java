package com.copyright.ccc.business.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.copyright.ccc.business.services.order.OrderLicenseImpl;
import com.copyright.data.order.License;

public class OrderLicenses implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OrderLicense _orderLicense;
	private List<OrderLicense> _orderLicenseList = new ArrayList<OrderLicense>();
	private List<OrderLicense> _displayLicenseList = new ArrayList<OrderLicense>();
	private int _totalRowCount;
	private int _readStartRow = 1;
	private int _readEndRow = 25;

    public OrderLicenses() {
    }
    
    public OrderLicense getOrderLicense(int arrayItem) {
//        if ((arrayItem >= (_readStartRow - 1)) & (arrayItem < ((_readStartRow - 1) + _orderLicenseList.size()))) {
        if (arrayItem < _orderLicenseList.size() ) {
            return _orderLicenseList.get(arrayItem);
        } else {
            return null;
        }
    }

    public OrderLicense getDisplayLicense(int arrayItem) {

        if (arrayItem < _orderLicenseList.size() ) {
            return _orderLicenseList.get(arrayItem);
        } else {
            return null;
        }
        
//    	if (arrayItem < _displayLicenseList.size()) {
//            return (OrderLicense) _displayLicenseList.get(arrayItem);
//        } else {
//            return null;
//        }
    }
    
    public void addDisplayLicense(OrderLicenseImpl orderLicenseImpl) {
        _displayLicenseList.add(orderLicenseImpl);        
    }

    public void addOrderLicense(License license) {
        OrderLicenseImpl orderLicenseImpl = new OrderLicenseImpl();
        orderLicenseImpl.setLicense(license);       
        _orderLicenseList.add(orderLicenseImpl);  
//        _orderLicenseRowCount = _orderLicenseList.size();
//        _readEndRow = _readStartRow + (_orderLicenseRowCount - 1);
    }

    public void addOrderLicense(OrderLicense orderLicense) {
        _orderLicenseList.add(orderLicense);  
    }
  
    
    /**
    * Set the display license list as a List
    * @param orderLicenseList is the List to set for display licenses
    */
    public void setOrderLicenseList(List<OrderLicense> orderLicenseList) {
        _orderLicenseList = orderLicenseList;
//        _totalRowCount = _orderLicenseList.size();
    }
    
    /**
    * Add a List of Order Licenses to the current Order License List
    * @param orderLicenseList is the List to add to this object
    */
    public void addOrderLicenseList(List<OrderLicense> orderLicenseList) {
        _orderLicenseList.addAll(orderLicenseList);
//        _totalRowCount = _orderLicenseList.size();
    }
    
    public void setTotalRowCount(int totalRowCount) {
        _totalRowCount = totalRowCount;
    }

    public List<OrderLicense> getOrderLicenseList() {
        return _orderLicenseList;
    }

    /**
    * Set the display license list as a List
    * @param dispLicenseList is the List to set for display licenses
    */
    public void setDisplayLicenseList(List<OrderLicense> dispLicenseList) {
        _displayLicenseList = dispLicenseList;
    }
    
    public List<OrderLicense> getDisplayLicenseList() {
        return _orderLicenseList;
    	//        return _displayLicenseList;
    }

    public void clearDisplayLicenseList() {
        _displayLicenseList.clear();
        _orderLicenseList.clear();
    }

    public void clearOrderLicenseList() {
//        _readStartRow = 1;
//        _readEndRow = OrderLicenseServices.READ_NUMBER_OF_LICENSE_ROWS;
        _orderLicenseList.clear();
    }
    
    public int getTotalRowCount() {
        return _totalRowCount;
    }

    public int getDisplayRowCount() {
        return _displayLicenseList.size();
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

}
