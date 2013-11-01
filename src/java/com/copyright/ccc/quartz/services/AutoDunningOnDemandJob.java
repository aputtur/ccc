package com.copyright.ccc.quartz.services;

import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.copyright.ccc.business.data.IOverdueInvoice;
import com.copyright.ccc.business.services.invoice.OverdueInvoiceSummaryImpl;
import com.copyright.ccc.config.CC2Configuration;

public class AutoDunningOnDemandJob extends AutoDunningHandler implements Job
{

	public void execute(JobExecutionContext context) throws JobExecutionException
	{
		String daysPastDue = CC2Configuration.getInstance().getOverdueInvoiceDaysPastDue();
		String customerAcctId = null;

		// FOR QA TESTING ONLY
		// these are double measures, first assign the email recipient and total number of
		// email sent from properties file, then the values will be overridden by the user input values.
		// this will prevent the huge number of email accidentally sent to the customers during the QA testing.
		String overrideTotalNumEmailSent = CC2Configuration.getInstance().getTestDefaultTotalNumberEmailSent();
		String overrideEmailRecipient = CC2Configuration.getInstance().getTestDefaultEmailRecipient();

		JobDataMap dataMap = context.getJobDetail().getJobDataMap();

		// override the default parameters if given by user
		if (dataMap != null && dataMap.size() > 0)
		{
			if (dataMap.containsKey(ICC2Scheduler.DAYS_PAST_DUE))
			{
				daysPastDue = (String) dataMap.get(ICC2Scheduler.DAYS_PAST_DUE);
			}
			if (dataMap.containsKey(ICC2Scheduler.CUSTOMER_ACCOUNT_ID))
			{
				customerAcctId = (String) dataMap.get(ICC2Scheduler.CUSTOMER_ACCOUNT_ID);
			}
			if (dataMap.containsKey(ICC2Scheduler.OVERRIDE_TOTAL_NUM_EMAIL_SENT))
			{
				overrideTotalNumEmailSent = (String) dataMap.get(ICC2Scheduler.OVERRIDE_TOTAL_NUM_EMAIL_SENT);
			}
			if (dataMap.containsKey(ICC2Scheduler.OVERRIDE_EMAIL_RECIPIENT))
			{
				overrideEmailRecipient = (String) dataMap.get(ICC2Scheduler.OVERRIDE_EMAIL_RECIPIENT);
			}
		}
		
		IOverdueInvoice[] overdueInvoices = this.retrieveOverdueInvoice(daysPastDue, customerAcctId);
		Map<Long, List<IOverdueInvoice>> overdueInvoiceMap = this.retrieveOverdueInvoiceByCustomer(overdueInvoices);
		Map<OverdueInvoiceSummaryImpl, String> emailMap = this.generateEmailByCustomer(overdueInvoiceMap);
		
		// send the autodunning status log to Finance
		// no need to send status log for on demand job
		//this.sendAutoDunningStatusLog();
		
		// check the properties file, if test mode, override the total number of email sent
		boolean testMode = CC2Configuration.getInstance().getAutoDunningTestModeEnable();
		if (testMode)
		{
			this.sendMail(emailMap, overrideEmailRecipient, overrideTotalNumEmailSent);
		}
		else
		{
			this.sendMail(emailMap, overrideEmailRecipient, null);
		}

	}

}
