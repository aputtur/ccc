package com.copyright.ccc.quartz.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.copyright.ccc.business.data.IAutoDunningParam;
import com.copyright.ccc.business.data.IOverdueInvoice;
import com.copyright.ccc.business.services.invoice.OverdueInvoiceSummaryImpl;
import com.copyright.ccc.config.CC2Configuration;
import com.copyright.workbench.logging.LoggerHelper;

public class AutoDunningBatchJob extends AutoDunningHandler implements Job
{
	private static final Logger _logger = LoggerHelper.getLogger();

	public void execute(JobExecutionContext arg0) throws JobExecutionException
	{

		// filter by the autodunning params, such as product type and days past due
		IAutoDunningParam[] params = this.retrieveAutoDunningParams();

		Map<String, IAutoDunningParam> paramsMap = new HashMap<String, IAutoDunningParam>();
		String daysPastDue = null;
		
		// DAYS PAST DUE is no longer setting in the properties file, instead, it is set by the
		// mimimun days past due in the table CC.AUTODUNNING_PARAMS
		int intDaysPastDue = 99999;

		for (IAutoDunningParam param : params)
		{
			if (param.getEnabled() != null && Integer.valueOf(param.getEnabled()) > 0)
			{
				paramsMap.put(param.getProductType(), param);
				if (param.getDaysPastDue() != null && param.getDaysPastDue().intValue() < intDaysPastDue)
				{
					intDaysPastDue = param.getDaysPastDue().intValue();
				}
			}
		}
		
		// String daysPastDue = (CC2Configuration.getInstance().getOverdueInvoiceDaysPastDue());

		daysPastDue = intDaysPastDue>0?String.valueOf(intDaysPastDue):null;  
		
		_logger.info("the minimum days of past due is set to.... " + daysPastDue );
		
		String overrideTotalNumEmailSent = CC2Configuration.getInstance().getTestDefaultTotalNumberEmailSent();;
		String overrideEmailRecipient = CC2Configuration.getInstance().getTestDefaultEmailRecipient();;
		
		IOverdueInvoice[] overdueInvoices = null;
		if (daysPastDue != null)
		{
			overdueInvoices = this.retrieveOverdueInvoice(daysPastDue, null);
		}

		if (overdueInvoices != null && overdueInvoices.length > 0)
		{
			_logger.info("*********** total number of invoices before filtering... " + overdueInvoices.length);

			IOverdueInvoice[] filteredOverdueInvoices = this.filterByAutoDunningParams(overdueInvoices, paramsMap);

			_logger.info("*********** total number of invoices after filtering... "
					+ filteredOverdueInvoices.length);

			Map<Long, List<IOverdueInvoice>> overdueInvoiceMap = this
					.retrieveOverdueInvoiceByCustomer(filteredOverdueInvoices);
			Map<OverdueInvoiceSummaryImpl, String> emailMap = this.generateEmailByCustomer(overdueInvoiceMap);
			
			// send the autodunning status log to Finance
			this.sendAutoDunningStatusLog();
			
			// check the properties file, if test mode, parameters can be overridden
			boolean testMode = CC2Configuration.getInstance().getAutoDunningTestModeEnable();
			if (testMode)
			{
				this.sendMail(emailMap, overrideEmailRecipient, overrideTotalNumEmailSent);
			}
			else
			{
				this.sendMail(emailMap, null, null);
			}

		}

	}

}
