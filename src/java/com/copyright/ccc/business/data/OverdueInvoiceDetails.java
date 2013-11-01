/**
 * 
 */
package com.copyright.ccc.business.data;

/**
 * @author wlee
 * java object to hold the data retrieving from stored procedure
 * APPS.CCC_TRANS_DUNNING_EMAIL_PKG.MAIN()
 */

import java.util.Date;

public class OverdueInvoiceDetails
{
	protected String customerName;
	protected Long partyId;
	protected Long customerNumber;
	protected String invoiceNumber;
    protected Date invoiceDate;
    protected Date glDate;
    protected String trxType;
    protected String orderNumber;
    protected Double invoiceAmount;
    protected Double taxAmount;
    protected Double outstandingAmount;
    protected Double acctdOutstandingAmount;
    protected Date dueDate;
    protected Integer daysOutstanding;
    protected Long installmentNumber;
    protected String billToName;
    protected String billToAddress;
    protected String adminName;
    protected String adminAddress;
	public String getCustomerName()
	{
		return customerName;
	}
	public void setCustomerName(String customerName)
	{
		this.customerName = customerName;
	}
	public Long getPartyId()
	{
		return partyId;
	}
	public void setPartyId(Long partyId)
	{
		this.partyId = partyId;
	}
	public Long getCustomerNumber()
	{
		return customerNumber;
	}
	public void setCustomerNumber(Long customerNumber)
	{
		this.customerNumber = customerNumber;
	}
	public String getInvoiceNumber()
	{
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber)
	{
		this.invoiceNumber = invoiceNumber;
	}
	public Date getInvoiceDate()
	{
		return invoiceDate;
	}
	public void setInvoiceDate(Date invoiceDate)
	{
		this.invoiceDate = invoiceDate;
	}
	public Date getGlDate()
	{
		return glDate;
	}
	public void setGlDate(Date glDate)
	{
		this.glDate = glDate;
	}
	public String getTrxType()
	{
		return trxType;
	}
	public void setTrxType(String trxType)
	{
		this.trxType = trxType;
	}
	public String getOrderNumber()
	{
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber)
	{
		this.orderNumber = orderNumber;
	}
	public Double getInvoiceAmount()
	{
		return invoiceAmount;
	}
	public void setInvoiceAmount(Double invoiceAmount)
	{
		this.invoiceAmount = invoiceAmount;
	}
	public Double getTaxAmount()
	{
		return taxAmount;
	}
	public void setTaxAmount(Double taxAmount)
	{
		this.taxAmount = taxAmount;
	}
	public Double getOutstandingAmount()
	{
		return outstandingAmount;
	}
	public void setOutstandingAmount(Double outstandingAmount)
	{
		this.outstandingAmount = outstandingAmount;
	}
	public Double getAcctdOutstandingAmount()
	{
		return acctdOutstandingAmount;
	}
	public void setAcctdOutstandingAmount(Double acctdOutstandingAmount)
	{
		this.acctdOutstandingAmount = acctdOutstandingAmount;
	}
	public Date getDueDate()
	{
		return dueDate;
	}
	public void setDueDate(Date dueDate)
	{
		this.dueDate = dueDate;
	}
	public Integer getDaysOutstanding()
	{
		return daysOutstanding;
	}
	public void setDaysOutstanding(Integer daysOutstanding)
	{
		this.daysOutstanding = daysOutstanding;
	}
	public Long getInstallmentNumber()
	{
		return installmentNumber;
	}
	public void setInstallmentNumber(Long installmentNumber)
	{
		this.installmentNumber = installmentNumber;
	}
	public String getBillToName()
	{
		return billToName;
	}
	public void setBillToName(String billToName)
	{
		this.billToName = billToName;
	}
	public String getBillToAddress()
	{
		return billToAddress;
	}
	public void setBillToAddress(String billToAddress)
	{
		this.billToAddress = billToAddress;
	}
	public String getAdminName()
	{
		return adminName;
	}
	public void setAdminName(String adminName)
	{
		this.adminName = adminName;
	}
	public String getAdminAddress()
	{
		return adminAddress;
	}
	public void setAdminAddress(String adminAddress)
	{
		this.adminAddress = adminAddress;
	}
}
