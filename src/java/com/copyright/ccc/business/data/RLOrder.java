/*
 * RLOrder.java
 */
package com.copyright.ccc.business.data;

import java.io.Serializable;
import java.util.Date;

public interface RLOrder extends Serializable
{
    public enum OrderTypes { LICENSE, CREDITED, PENDING, CANCELED, DENIED }

    public String getCreateDate();
	public Date getRawCreateDate();
	public void setCreateDate(Date createDate);

	public String getJobTicketID();
	public void setJobTicketID(String jobTicketID);

	public String getLicenseNo();
	public void setLicenseNo(long licenseNo);

	public String getPublication();
	public void setPublication(String publication);

	public String getTitle();
	public void setTitle(String title);

	public String getTypeOfUse();
	public void setTypeOfUse(String typeOfUse);

	public String getTotalFee();
	public void setTotalFee(String totalFee);

	public String getCurrencyType();
	public void setCurrencyType(String currencyType);

	public String getRightslinkURL();
	public void setRightslinkURL(String rightslinkURL);

	public String getLicenseOID();
	public void setLicenseOID(String licenseOID);

	public String getPublisher();
	public void setPublisher(String publisher);

	public RLOrder.OrderTypes getOrderType();
	public void setOrderType(RLOrder.OrderTypes type);
}