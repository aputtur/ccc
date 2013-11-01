/*
 * ProtocolSwitcher.java
 * Copyright (c) 2009, Copyright Clearance Center, Inc. All rights reserved.
 * ----------------------------------------------------------------------------
 * Revision History
 * 2009.08.21   tmckinney    Created.
 * 2009.08.24   tmckinney    Use request.getLocalPort() to determine switch.
 * ----------------------------------------------------------------------------
 */
package com.copyright.ccc.web.urlrewrite;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.copyright.ccc.config.CC2Configuration;

/**
 * This class performs required switches from HTTP --> HTTPS and
 * vice versa. It is invoked by the URL Rewrite Filter when a
 * matching request is encountered.
 * 
 * To use this class, define a VirtualHost with an explicit
 * ServerName hostname:port definition.  E.g. in DEV2:
 * www.dev2.copryight.com:7782
 * and
 * www.dev2.copryight.com:7790
 * 
 * The 7782 and 7790 ports will show up in the getLocalPort() attribute
 * of the servlet request.
 * 
 * @author tmckinney
 * @version 1.0
 */
public class ProtocolSwitcher
{
	
	private static Logger LOGGER = Logger.getLogger(ProtocolSwitcher.class);
	
	public ProtocolSwitcher()
	{
		
	}
	
	/**
	 * Switch an HTTPS request to HTTP by sending a redirect.
	 * @param request
	 * @param response
	 */
	public void switchToHTTP(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		if (!CC2Configuration.getInstance().isProtocolSwitchingEnabled()) return;
		
		LOGGER.debug("In ProtocolSwitcher.switchToHTTP");
		LOGGER.debug("getServerPort = " + request.getServerPort());
		LOGGER.debug("getLocalPort = " + request.getLocalPort());
		LOGGER.debug("getRemotePort = " + request.getRemotePort());

		if (request.getLocalPort() == CC2Configuration.getInstance().getProtocolSwitchingHTTPSPort())
		{		
			response.sendRedirect( generateRedirectURL(request,false) );
		}
	}
	
	/**
	 * Switch an HTTP request to HTTPS by sending a redirect.
	 * @param request
	 * @param response
	 */
	public void switchToHTTPS(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		if (!CC2Configuration.getInstance().isProtocolSwitchingEnabled()) return;
		
		LOGGER.debug("In ProtocolSwitcher.switchToHTTPS");
		LOGGER.debug("getServerPort = " + request.getServerPort());
		LOGGER.debug("getLocalPort = " + request.getLocalPort());
		LOGGER.debug("getRemotePort = " + request.getRemotePort());
		
		if (request.getLocalPort() == CC2Configuration.getInstance().getProtocolSwitchingHTTPPort())
		{
			response.sendRedirect( generateRedirectURL(request,true) );
		}
	}
	
	
	private String generateRedirectURL(HttpServletRequest request, 
			boolean secure)
	{
		String redirectURL = secure ? CC2Configuration.getInstance().getProtocolSwitchingHTTPSURL()
				: CC2Configuration.getInstance().getProtocolSwitchingHTTPURL();
		redirectURL += request.getRequestURI();
		if (request.getQueryString() != null) redirectURL += "?" + request.getQueryString();
		return redirectURL;
	}
	
	
}
