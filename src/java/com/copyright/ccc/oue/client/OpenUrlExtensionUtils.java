package com.copyright.ccc.oue.client;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.copyright.ccc.oue.connector.OpenUrlExtensionServiceConnector;
import com.copyright.ccc.oue.connector.OperationException;
import com.copyright.workbench.logging.LoggerHelper;

public class OpenUrlExtensionUtils
{
	private static final Logger sLogger = LoggerHelper.getLogger();
	
	public static final String SESSION_KEY = "open_url_extension_data";
	
	public static final String ERROR_HTML = 
		"<div class='multimedia-details' style='border: .1em solid #999999; height:110px' width:690px'><strong>A problem occurred with rendering the details of this work.</strong></div>";	
	public static interface ParameterKeys
	{
		String MEDIA_TYPE = "mediaType";
		String CONTENT_NAME = "contentName";
		String THUMBNAIL_URL = "thumbnailUrl";
		String DOWNLOAD_URL = "downloadUrl";
		String INFO_URL = "infoUrl";
		String CONTENT_REF = "contentRef";
		String CONTENT_DATE = "date";
		String SERVICE_NAME = "serviceName";
		String PERMISSION_NAME = "permissionName";
		String TOU_NAME = "touName";
	}

	public static void updateSession(HttpServletRequest request)
	{
		if (isExtensionUrl(request))
        {
             OpenUrlExtensionsSessionData data = new OpenUrlExtensionsSessionData();

             for (Object key : request.getParameterMap().keySet())
             {
                  String pName = (String) key;
                  String value = request.getParameter(pName);
                  if ( sLogger.isDebugEnabled() )
                  {
                	  sLogger.debug( pName + " = " + value );
                  }
                  if (value != null && !"".equals(value.trim()))
                  {
                        data.setValue(pName, value);
                  }
             }
             if (validateExtensionData(data))
             {
                  request.getSession().setAttribute(SESSION_KEY, data);
             }
             else
             {
                  request.getSession().setAttribute(SESSION_KEY, null);
             }
        }
        else
        {
             request.getSession().setAttribute(SESSION_KEY, null);
        }

	}

	public static OpenUrlExtensionsSessionData getExtensionData(HttpSession session)
	{
		return (OpenUrlExtensionsSessionData) session.getAttribute(SESSION_KEY);
	}
	

	public static String getDetailsHtml(HttpSession session)
	{
		OpenUrlExtensionsSessionData data = getExtensionData(session);
		String html = null;
	
		
		if (data != null)
		{
			try
			{
				html = OpenUrlExtensionServiceConnector.execute("detailHtml", data);
			}
			catch (OperationException e)
			{
				html = ERROR_HTML;
			}
		}
		
		return html;
		
	}

	protected static boolean validateExtensionData(OpenUrlExtensionsSessionData data)
	{
		try
		{
			return Boolean.valueOf(OpenUrlExtensionServiceConnector.execute("validate", data));
		}
		catch (OperationException e)
		{
			return false;
		}
		
	}

	protected static boolean isExtensionUrl(HttpServletRequest request)
	{
		boolean isExtension = false;
		/*
		 * The rest of the parameters become non-case sensitive since they are
		 * stored in an internal map within the session data in lowercase form. 
		 * This parameter, however, is pulled directly off the request prior to the session
		 * data object being created, so in order to avoid case-sensitivity,
		 * it is "found" by looping rather than a direct hash lookup.
		 */
		for (Object key: request.getParameterMap().keySet())
		{
			String pName = (String) key;
			if (ParameterKeys.MEDIA_TYPE.equalsIgnoreCase(pName))
			{
				String value = request.getParameter(pName);
				if (value != null)
				{
					isExtension = true;
					break;
				}
			}
		}
		
		return isExtension;
	}

		

}
