/*
 * RLOrderImpl.java
 * This is a reimplementation for the UserOrder class provided
 * by Rightslink.
 */

package com.copyright.ccc.business.services.order;

import java.util.Calendar;
import java.util.Date;

import com.copyright.ccc.business.data.RLOrder;
import com.copyright.svc.rlOrder.api.data.UserOrder;

public class RLOrderImpl implements RLOrder
{
	private static final long serialVersionUID = 1L;
	
	private OrderTypes _orderType = null;
	private Date _createDate = null;
	private long _licenseNo = -1;
	private String _licenseOID = null;
	/**
	 * the currency type that the order was placed under.
	 */
	private String _currencyType = null;
	private String _title = null;
	private String _typeOfUse = null;
	private String _publisher = null;
	private String _publication = null;
	private String _jobTicketID = null;
	private String _totalFee = null;
	/**
	 * the url which will open up the printable form
	 */
	private String _rightslinkURL = null;

	public RLOrderImpl(UserOrder userOrder) {
		_createDate = userOrder.getCreateDate();
		_licenseNo = userOrder.getLicenseNo();
		_licenseOID = userOrder.getLicenseOID();
		_currencyType = userOrder.getCurrencyType();
		_title = userOrder.getTitle();
		_typeOfUse = userOrder.getTypeOfUse();
		_publisher = userOrder.getPublisher();
		_publication = userOrder.getPublication();
		_jobTicketID = userOrder.getJobTicketID();
		_totalFee = userOrder.getTotalFee();
		_rightslinkURL = userOrder.getRightslinkURL();

		if (userOrder.getOrderType() == UserOrder.orderTypes.LICENSE)
		{
			_orderType = RLOrder.OrderTypes.LICENSE;
		}
		else if (userOrder.getOrderType() == UserOrder.orderTypes.CREDITED)
		{
			_orderType = RLOrder.OrderTypes.CREDITED;
		}
		else if (userOrder.getOrderType() == UserOrder.orderTypes.PENDING)
		{
			_orderType = RLOrder.OrderTypes.PENDING;
		}
		else if (userOrder.getOrderType() == UserOrder.orderTypes.CANCELLED)
		{
			_orderType = RLOrder.OrderTypes.CANCELED;
		}
		else if (userOrder.getOrderType() == UserOrder.orderTypes.DENIED)
		{
			_orderType = RLOrder.OrderTypes.DENIED;
		}
	}

	public String getCreateDate() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(_createDate);

	       return String.format("%1$tm/%1$td/%1$tY",cal);
	}
	public Date getRawCreateDate()                     { return _createDate;       }
	public void setCreateDate(Date createDate)         { _createDate = createDate; }

	public String getJobTicketID()                     { return _jobTicketID; }
	public void setJobTicketID(String jobTicketID)     { _jobTicketID = jobTicketID; }

	public String getLicenseNo()                       { return String.valueOf(_licenseNo); }
	public void setLicenseNo(long licenseNo)           { _licenseNo = licenseNo; }

	public String getPublication()                     { return _publication; }
	public void setPublication(String publication)     { _publication = publication; }

	public String getTitle()                           { return _title; }
	public void setTitle(String title)                 { _title = title; }

	public String getTypeOfUse()                       { return _typeOfUse; }
	public void setTypeOfUse(String typeOfUse)         { _typeOfUse = typeOfUse; }

	public String getTotalFee()                        { return strip(_totalFee); }
	public void setTotalFee(String totalFee)           { _totalFee = totalFee; }

	public String getCurrencyType()                    { return _currencyType; }
	public void setCurrencyType(String currencyType)   { _currencyType = currencyType; }

	public String getRightslinkURL()                   { return _rightslinkURL; }
	public void setRightslinkURL(String rightslinkURL) { _rightslinkURL = rightslinkURL; }

	public String getLicenseOID()                      { return _licenseOID; }
	public void setLicenseOID(String licenseOID)       { _licenseOID = licenseOID; }

	public String getPublisher()                       { return _publisher; }
	public void setPublisher(String publisher)         { _publisher = publisher; }

	public RLOrder.OrderTypes getOrderType()           { return _orderType; }
	public void setOrderType(RLOrder.OrderTypes type)  { _orderType = type; }

	public String getOrderStatus() {
		String retval = "";

		if (_orderType == RLOrder.OrderTypes.LICENSE) retval = "License";
		else if (_orderType == RLOrder.OrderTypes.CREDITED) retval = "Credited";
		else if (_orderType == RLOrder.OrderTypes.PENDING) retval = "Pending";
		else if (_orderType == RLOrder.OrderTypes.CANCELED) retval = "Canceled";
		else if (_orderType == RLOrder.OrderTypes.DENIED) retval = "Denied";

		return retval;
	}
	
	private String strip(String feeString) {
		if (feeString == null || feeString.equals("")) {
			return "0.00";
		}

		char[] feeChars = feeString.toCharArray();
		StringBuffer stripped = new StringBuffer();

		for (int i = 0; i < feeChars.length; i++) {
			if ((feeChars[i] >= '0' && feeChars[i] <= '9') || feeChars[i] == '.' || feeChars[i] == ',') {
				stripped.append(feeChars[i]);
			}
		}
		return stripped.toString();
	}
}