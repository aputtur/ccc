package com.copyright.ccc.business.services;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.copyright.base.CCCRuntimeException;
import com.copyright.ccc.config.CC2Configuration;
import com.copyright.ccc.config.CybersourceConfiguration;
import com.copyright.ccc.util.LogUtil;
import com.copyright.svc.telesales.api.data.TelesalesServiceConsumerContext;
import com.copyright.workbench.logging.LoggerHelper;

/**
 * This class should be used by all other parts of the code to check if a particular system (CCC or 3rd party)
 * is available (up or down).  This lets us simulate system status changes by editing configuration
 * properties, which is useful for testing purposes.
 *  
 * @author dstine
 */
public class SystemStatus
{
	private static Logger sLogger = LoggerHelper.getLogger();
	
	/**
	 * All normal business logic should use this method.
	 */
	public static Boolean isTelesalesUp()
	{
		return ServiceLocator.getTelesalesService().isTelesalesUp( new TelesalesServiceConsumerContext() );
	}
	
	/**
	 * All normal business logic should use this method.
	 */
	public static Boolean isRightslinkUp()
	{
		if ( CC2Configuration.getInstance().simluateRightslinkDown() ) return false;
		
		return isRightslinkUpInternal();
	}

	private static Boolean isRightslinkUpInternal()
	{
		try
		{
			ServiceLocator.getRLUserService().isRightslinkUp();
			return true;
		}
		catch ( Exception e )
		{
			Throwable rootCause = ExceptionUtils.getRootCause( e );
			
			// Do not import this exception since there are two in different packages!
			if ( rootCause instanceof com.caucho.burlap.client.BurlapProtocolException )
			{
				com.caucho.burlap.client.BurlapProtocolException bpe = 
					(com.caucho.burlap.client.BurlapProtocolException) rootCause;
				if ( StringUtils.contains( bpe.getMessage(), "302" ) )
				{
					// When Rightslink goes down for planned maintenance, HTTP requests
					// to the app return a redirect (302) to the maintenance page.
					// Since the services are deployed into the same container, HTTP/Burlap
					// requests to the services also return a redirect.
					sLogger.error("Rightslink Status: Down due to 302 Error " + ExceptionUtils.getFullStackTrace(e));
					return false;
				}
				else if ( StringUtils.contains( bpe.getMessage(), "500" ) )
				{
					// If the call is made while RL is in the act of coming up or coming
					// down, it is possible that we won't get the maintenance page.  We
					// instead get a 500 error.  So treat this case as RL down, also.
					// CC-1965
					
					sLogger.error("Rightslink Status: Down due to 500 Error " + ExceptionUtils.getFullStackTrace(e));
							 
					return false;
				}
			}
			
			throw new CCCRuntimeException( e );
		}
	}
	
	/**
	 * All normal business logic should use this method.
	 */
	public static Boolean isCybersourceSiteUp()
	{
		if ( CC2Configuration.getInstance().simulateCybersourceSiteDown() ) return false;
		
		return isCybersourceSiteUpInternal();
	}

	private static boolean isCybersourceSiteUpInternal()
	{
		boolean isUp = false;

		// Read timeout from configuration. If it is zero, don't perform the test
		// and return status "OK".
		int siteTestTimeout = 5000;			// default 5 seconds
		try
		{
			siteTestTimeout = CC2Configuration.getInstance().getCybersourceSiteTestTimeoutMillis();
		}
		catch(NumberFormatException nfe)
		{
			sLogger.error("NumberFormatException while reading cybersource.site.timeout value: " + nfe.getMessage()
							+ "; default of 5 seconds used." );
		}
		if(siteTestTimeout == 0)
		{
			return true;
		}
		
		HttpClient client = new HttpClient();
		client.getParams().setParameter("http.socket.timeout", new Integer(siteTestTimeout));
		GetMethod method = new GetMethod(CybersourceConfiguration.getInstance().getHopUrl());
	    
		try
		{
			int statusCode = client.executeMethod(method);

			if (statusCode == HttpStatus.SC_OK)
			{
				isUp = true;
			}
		}
		catch (HttpException e)
		{
			sLogger.error("Fatal protocol violation: " + e.getMessage() + LogUtil.appendableStack(e));
		}
		catch (IOException e)
		{
			sLogger.error("Fatal transport error: " + e.getMessage() + LogUtil.appendableStack(e));
		}
		finally
		{
			method.releaseConnection();
		}  
		
		return isUp;
	}
}
