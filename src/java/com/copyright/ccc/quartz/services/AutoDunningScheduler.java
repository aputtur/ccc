package com.copyright.ccc.quartz.services;

import java.util.Date;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionServlet;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.ee.servlet.QuartzInitializerServlet;
import org.quartz.impl.StdSchedulerFactory;

import com.copyright.ccc.CCRuntimeException;
import com.copyright.ccc.web.forms.admin.AutoDunningForm;

public class AutoDunningScheduler 
{
	private static AutoDunningScheduler sInstance;

	public static synchronized AutoDunningScheduler getInstance()
	{
		if (sInstance == null)
		{
			sInstance = new AutoDunningScheduler();
		}
		return sInstance;
	}

	private AutoDunningScheduler()
	{
	};

	public void runSchedule(ActionServlet actionServlet, AutoDunningForm form)
	{
		try
		{
			ServletContext ctx = actionServlet.getServletContext();
			// ** The Quartz Scheduler
			Scheduler scheduler = null;

			// ** Retrieve the factory from the ServletContext.
			// ** It will be put there by the Quartz Servlet
			StdSchedulerFactory factory = (StdSchedulerFactory) ctx
					.getAttribute(QuartzInitializerServlet.QUARTZ_FACTORY_KEY);
			// ** Retrieve the scheduler from the factory
			scheduler = factory.getScheduler();

			boolean m_startOnLoad = scheduler.isStarted();

			if (m_startOnLoad)
			{
				JobDetail jobDetail = new JobDetail("AutoDunningOnDemandJob", Scheduler.DEFAULT_MANUAL_TRIGGERS,
						AutoDunningOnDemandJob.class);
				
				jobDetail.getJobDataMap().put(ICC2Scheduler.DAYS_PAST_DUE, form.getDaysPastDue().trim());
				jobDetail.getJobDataMap().put(ICC2Scheduler.CUSTOMER_ACCOUNT_ID, form.getCustomerId().trim());
				if(!StringUtils.isEmpty(form.getSentEmailTo()))
				{
					jobDetail.getJobDataMap().put(ICC2Scheduler.OVERRIDE_EMAIL_RECIPIENT, form.getSentEmailTo().trim());
				}
				if(!StringUtils.isEmpty(form.getTotalNumEmailSent()))
				{
					jobDetail.getJobDataMap().put(ICC2Scheduler.OVERRIDE_TOTAL_NUM_EMAIL_SENT, form.getTotalNumEmailSent().trim());
				}
				SimpleTrigger trigger = new SimpleTrigger("mytrigger", Scheduler.DEFAULT_MANUAL_TRIGGERS, new Date(), null,
						0, 60L * 1000L);

				scheduler.scheduleJob(jobDetail, trigger);

			}
			else 
			{
				// System.out.println("Scheduler Will start in " + m_startupDelayString + " milliseconds!");
				// wait the specified amount of time before
				// starting the process.
				// Thread delayedScheduler = new Thread(new DelayedSchedulerStarted(scheduler, m_startupDelay));
				// give the scheduler a name. All good code needs a name
				// delayedScheduler.setName("Delayed_Scheduler");
				// Start out scheduler
				// delayedScheduler.start();
			}
		}
		catch (SchedulerException e)
		{
			throw new CCRuntimeException(e);
		}
	}
}
