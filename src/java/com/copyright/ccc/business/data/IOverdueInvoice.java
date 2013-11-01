package com.copyright.ccc.business.data;

import java.util.Date;

public interface  IOverdueInvoice
{
	public String getCustomerName();
	public void setCustomerName(String customerName);
	public Long getPartyId();
	public void setPartyId(Long partyId);
	public Long getCustomerNumber();
	public void setCustomerNumber(Long customerNumber);
	public String getInvoiceNumber();
	public void setInvoiceNumber(String invoiceNumber);
	public Date getInvoiceDate();
	public void setInvoiceDate(Date invoiceDate);
	public Date getGlDate();
	public void setGlDate(Date glDate);
	public String getTrxType();
	public void setTrxType(String trxType);
	public String getOrderNumber();
	public void setOrderNumber(String orderNumber);
	public Double getInvoiceAmount();
	public void setInvoiceAmount(Double invoiceAmount);
	public Double getTaxAmount();
	public void setTaxAmount(Double taxAmount);
	public Double getOutstandingAmount();
	public void setOutstandingAmount(Double outstandingAmount);
	public Double getAcctdOutstandingAmount();
	public void setAcctdOutstandingAmount(Double acctdOutstandingAmount);
	public Date getDueDate();
	public void setDueDate(Date dueDate);
	public Integer getDaysOutstanding();
	public void setDaysOutstanding(Integer daysOutstanding);
	public Long getInstallmentNumber();
	public void setInstallmentNumber(Long installmentNumber);
	public String getBillToName();
	public void setBillToName(String billToName);
	public String getBillToAddress();
	public void setBillToAddress(String billToAddress);
	public String getAdminName();
	public void setAdminName(String adminName);
	public String getAdminAddress();
	public void setAdminAddress(String adminAddress);
        
}
