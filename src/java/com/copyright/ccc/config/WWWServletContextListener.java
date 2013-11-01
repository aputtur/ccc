/*
 * WWWServletContextListener.java
 * Copyright (c) 2009, Copyright Clearance Center, Inc. All rights reserved.
 * ----------------------------------------------------------------------------
 * Revision History
 * 2009.07.01   tmckinney    Created.
 * ----------------------------------------------------------------------------
 */
package com.copyright.ccc.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.copyright.ccc.business.security.ListSecuredActions;
import com.copyright.workbench.logging.LoggingUtil;

/**
 * Configuration that occurs at app startup or shutdown time.
 * 
 * @author tmckinney
 * @version 1.0
 */
public class WWWServletContextListener implements ServletContextListener
{
	
	private Logger logger = Logger.getLogger(WWWServletContextListener.class);

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0)
	{

	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent arg0)
	{
		try
		{
			LoggingUtil.auxLoggingProperties(logger, CC2Constants.LOG4J_RESOURCE);
	       	ListSecuredActions.collectSecuredActions(arg0.getServletContext().getRealPath("/WEB-INF/web.xml"));	
		}
		catch (Exception e)
		{
			logger.warn("Unable to load auxiliary logging properties.", e);
		}
	}
	

}
