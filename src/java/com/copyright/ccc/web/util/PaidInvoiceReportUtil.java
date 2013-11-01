package com.copyright.ccc.web.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.web.forms.ViewReportsActionForm;
import com.copyright.svc.order.api.OrderService;
import com.copyright.svc.order.api.data.InvoicePaymentReceipts;
import com.copyright.svc.order.api.data.OrderConsumerContext;

public class PaidInvoiceReportUtil extends ReportUtil
{
	
	 public PaidInvoiceReportUtil()
	 {
		 lastRowCSV = "There are no paid invoices for the given search criteria";
		 lastRowHTML = "<tr><td colspan=\"12\">There are no paid invoices for the given search criteria</td></tr></table>";
	 }
	 
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.copyright.ccc.web.util.ReportUtil#getHTMLHeader()
	 */
	@Override
	protected void getHTMLHeader()
	{
		String header = "<table border=\\\"1\\\" align=\\\"center\\\"><tr><th>Logged Date</th><th>Account #</th><th>Receipt #</th><th>Invoice #</th><th>Payment Type</th><th>Transaction Type</th></th><th>Sent To</th><th>Currency</th><th>Sub Total Amount</th></tr>";
		// return buffer;
		reportData.add(header);
	}

	/* (non-Javadoc)
	 * @see com.copyright.ccc.web.util.ReportUtil#getCSVReport(com.copyright.ccc.web.forms.ViewReportsActionForm)
	 */
	@Override
	public void getCSVHeader()
	{
		String header = "Logged Date,Account #,Receipt #,Invoice #, Pay Type, Trans Type, Sent To, Curr Type, Sub Total Amount\n";
		// return buffer;
		reportData.add(header);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.copyright.ccc.web.util.ReportUtil#getReportRows(com.copyright.ccc
	 * .web.forms.ViewReportsActionForm)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List getReportRows(ViewReportsActionForm form)
	{
		DateRange dateRange = new DateRange(form.getBeginDate(), form
				.getEndDate());
		DateRange dateRangeWFormat = new DateRange(dateRange.getFirstDate(),dateRange.getSecondDate(),"yyyyMMddHHmmss");
				
		List<InvoicePaymentReceipts> invoiceList = new ArrayList<InvoicePaymentReceipts>();
		
		OrderService orderService = ServiceLocator.getOrderService();
		
		invoiceList = orderService.getInvoicePaymentReceiptsByDateRange(new OrderConsumerContext(), dateRangeWFormat.getFirstDate(),
				dateRangeWFormat.getSecondDate());
		
		return invoiceList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.copyright.ccc.web.util.ReportUtil#getRow(java.lang.Object)
	 */
	@Override
	public void getRow(Object objectRow)
	{
		//IPLEntry entry = (IPLEntry)objectRow;
		InvoicePaymentReceipts entry = (InvoicePaymentReceipts) objectRow;
		buffer.setLength(0); // clear the buffer

		try
		{
			if (formatIsHTML())
			{
				buffer.append("<tr><td>");
			}
			
			Date tempDate = entry.getCreateDate();
								
			buffer.append(StringUtils.defaultIfEmpty(this.dateFormat.format(tempDate),getEmptyString()));
			buffer.append(getFieldSeparator());
			
			buffer.append(entry.getAccountNumber());
			buffer.append(getFieldSeparator());
			
			buffer.append(entry.getReceiptNumber());
			buffer.append(getFieldSeparator());
			
			buffer.append(entry.getInvoiceNumber());
			buffer.append(getFieldSeparator());
			
			buffer.append(entry.getPaymentType().toString());
			buffer.append(getFieldSeparator());
			
			if (entry.getTransactionType() != null) {
				buffer.append(entry.getTransactionType());
			} else {
				buffer.append("");
			}
			buffer.append(getFieldSeparator());
			
			buffer.append(entry.getUsername());
			buffer.append(getFieldSeparator());
			
			if (entry.getCurrencyCode() != null) {
				buffer.append(entry.getCurrencyCode()); }
			else {
				buffer.append("");
			}
			buffer.append(getFieldSeparator());
			
			DecimalFormat df = new DecimalFormat("#.00");
			buffer.append(df.format(entry.getAmount()));
						
	       	if (!outputFormat)buffer.append(HTML_END_TABLE_ROW);
			else buffer.append("\r\n");
			reportData.add(buffer.toString());
			

		}
		catch (Exception exc)
		{
			_logger.error(ExceptionUtils.getFullStackTrace(exc));
		}

	}

}
