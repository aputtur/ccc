package com.copyright.ccc.business.services.invoice;

public class OverdueInvoiceSummaryImpl extends OverdueInvoiceImpl
{

	private static final long serialVersionUID = 1L;
	protected Double totalInvoiceAmount;

	public Double getTotalInvoiceAmount()
	{
		return totalInvoiceAmount;
	}

	public void setTotalInvoiceAmount(Double totalInvoiceAmount)
	{
		this.totalInvoiceAmount = totalInvoiceAmount;
	}

}
