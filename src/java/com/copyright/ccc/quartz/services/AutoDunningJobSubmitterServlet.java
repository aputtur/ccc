package com.copyright.ccc.quartz.services;

import java.text.ParseException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.ee.servlet.QuartzInitializerServlet;
import org.quartz.impl.StdSchedulerFactory;

import com.copyright.base.config.AppServerConfiguration;
import com.copyright.ccc.CCRuntimeException;
import com.copyright.ccc.config.CC2Configuration;
import com.copyright.workbench.logging.LoggerHelper;

/**
 * Submits the AutoDunning job to the QuartzScheduler. This servlet was
 * created to move the autoDunning job out of the struts-config file. 
 * cc3 is gradually moving to struts2 so we are removing dependencies on struts 1.x where possible.
 * <p>
 * Note that the initialization of this servlet depends on the QuartzInitializerServlet having already
 * been initialized. For that reason, the load-on-startup value of this servlet
 * must be numerically greater than that of the QuartzInitializerServlet to ensure that this
 * servlet gets init'd after the QuartzInitializerServlet. These values are
 * specified in web.xml.
 * 
 * @author jarbo
 *
 */
public class AutoDunningJobSubmitterServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger _logger = LoggerHelper.getLogger();

	@Override
	public void init(ServletConfig config) throws ServletException {
		// Only run AutoDunning in internal deployment; there is a single such node 
		// in all environments at this time.
		// [dstine 8/18/10]
		/**** Block this code since internal is going away in all environments ******/
		/* if ( AppServerConfiguration.isExternal() )
		{
			_logger.info("external deployment, will not submit AutoDunning job");
			return;
		} else {
			_logger.info("internal deployment, Submitting scheduled AutoDunning job");
			submitAutoDunningJob(config.getServletContext());
		} */
		_logger.info("Submitting scheduled AutoDunning job");
		submitAutoDunningJob(config.getServletContext());
	}

	private void submitAutoDunningJob(ServletContext ctx) {		
		try
		{
			//** The Quartz Scheduler
			Scheduler scheduler = null;

			//** Retrieve the factory from the ServletContext.
			//** It will be put there by the Quartz Servlet
			StdSchedulerFactory factory = (StdSchedulerFactory) ctx
					.getAttribute(QuartzInitializerServlet.QUARTZ_FACTORY_KEY);
			
			if (factory==null) {
				throw new CCRuntimeException("Quartz SchedulerFactory not found in ServletContext. Check the <load-on-startup> value to ensure that it gets loaded before the " + this.getClass().getSimpleName());
			}
			
			//** Retrieve the scheduler from the factory
			scheduler = factory.getScheduler();

			boolean m_startOnLoad = scheduler.isStarted();

			String quartzEmailSchedule = CC2Configuration.getInstance().getQuartzEmailSchedule();
			boolean quartzSchedulerEnable = CC2Configuration.getInstance().getQuartzSchedulerEnable();

			if (m_startOnLoad && quartzSchedulerEnable)
			{
				try
				{
					JobDetail jd = new JobDetail("AutoDunningBatchJob", Scheduler.DEFAULT_GROUP,
							AutoDunningBatchJob.class);
					CronTrigger ct = new CronTrigger("cronTrigger", Scheduler.DEFAULT_GROUP, quartzEmailSchedule);
					scheduler.scheduleJob(jd, ct);
				}
				catch (SchedulerException exc)
				{
					throw new CCRuntimeException(exc);
				}
				catch (ParseException pe)
				{
					throw new CCRuntimeException(pe);
				}

			}
			if (!m_startOnLoad)
			{
				// _logger.info("Scheduler Will start in " + m_startupDelayString + " milliseconds!");
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
