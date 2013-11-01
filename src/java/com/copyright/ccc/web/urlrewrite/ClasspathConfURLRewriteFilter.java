/*
 * ClasspathConfURLRewriteFilter.java
 * Copyright (c) 2009, Copyright Clearance Center, Inc. All rights reserved.
 * ----------------------------------------------------------------------------
 * Revision History
 * 20090.08.20   tmckinney    Created.
 * ----------------------------------------------------------------------------
 */
package com.copyright.ccc.web.urlrewrite;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.tuckey.web.filters.urlrewrite.Conf;
import org.tuckey.web.filters.urlrewrite.UrlRewriteFilter;

/**
 * Extends from version 3.2 of URLRewriteFilter to allow 
 * urlrewrite.xml to be loaded from the classpath, thereby allowing
 * that file to live outside the WAR deployable.
 * 
 * As of r272 (2009.08.10) revision of UrlRewriteFilter, classpath
 * loading can be accomplished without this custom extension. That
 * revision will presumably appear in version 3.3 of URLRewriteFilter.
 * 
 * @author tmckinney
 * @version 1.0
 */
public class ClasspathConfURLRewriteFilter extends UrlRewriteFilter
{
	@Override
	protected void loadUrlRewriter(FilterConfig filterConfig) throws ServletException
	{    
		try
        {
			ServletContext context = filterConfig.getServletContext();
			String confPath = filterConfig.getInitParameter("confPath");
			
	        InputStream inputStream = ClassLoader.getSystemResourceAsStream(confPath);
	        URL confUrl = null;
	        try {
	            confUrl = context.getResource(confPath);
	        } catch (MalformedURLException e) {
	            //log.debug(e);
	        }
	        String confUrlStr = null;
	        if (confUrl != null) {
	            confUrlStr = confUrl.toString();
	        }
	        if (inputStream == null) {
	            //log.error("unable to find urlrewrite conf file at " + confPath);
	            // set the writer back to null
//	            if (urlRewriter != null) {
//	                log.error("unloading existing conf");
//	                urlRewriter = null;
//	            }

	        } else {
	            Conf conf = new Conf(context, inputStream, confPath, confUrlStr, false /* must use XML conf for now */);
	            checkConf(conf);
	        }


        }
		catch(Throwable e)
        {
            throw new ServletException(e);
        }
    }

}
