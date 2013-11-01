package com.copyright.ccc.web.actions;

import com.copyright.ccc.web.util.PaidInvoiceReportUtil;
import com.copyright.ccc.web.util.ReportUtil;

public class ShowPaidInvoiceReportAction extends ShowReportAction
{
	@Override
	protected ReportUtil getReportUtil()
	{
		return new PaidInvoiceReportUtil();
	}
}
