package com.copyright.ccc.oue.connector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import com.copyright.ccc.CCRuntimeException;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.oue.client.OpenUrlExtensionsSessionData;

public class OpenUrlExtensionServiceConnector
{
	public static final String ERROR_RESPONSE_PREFIX = "OUE_ERROR";
	
	public static String execute(String operationName, OpenUrlExtensionsSessionData data) throws OperationException
	{	
		String result = null;
		String url = getUrl(operationName, data);
		
		result = getContentAsObject(executeGetRequest(url));
		
		if (result != null && result.startsWith(ERROR_RESPONSE_PREFIX))
		{
			throw new OperationException(result);
		}
		
		return result;
		
	}
	

	public static InputStream executeGetRequest(String resourceRef)
	{

	    try
		{
	    	URL url = new URL(resourceRef);
	    	return url.openStream();

		}
		catch (IOException e)
		{
			
			throw new RuntimeException("Error obtaining REST resource: " + resourceRef, e);
		}	
		

	}	
   public  static String getContentAsObject(InputStream response) 
    {
		StringBuffer responseBuffer = new StringBuffer();
		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"));
			String line = reader.readLine();
					
			for (; line != null; line = reader.readLine())
			{
				responseBuffer.append(line);
			}
		}
		catch (IOException e1)
		{
			throw new RuntimeException("Unable to read response", e1);
		} 		
		
		return responseBuffer.toString();
		
    }		
	protected static String getUrl(String operationName, OpenUrlExtensionsSessionData data)
	{
		StringBuffer buff = new StringBuffer();
		buff.append(ServiceLocator.getOpenUrlExtensionsEndpointUrl());
		buff.append("/oue/" + operationName + "?");
		
		for (Map.Entry<String, String> entry: data.getDataMap().entrySet())
		{ 
			
			try
			{
				if (entry.getValue() != null)
				{
					buff.append("&" + entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), "UTF-8"));
				}
			}
			catch (UnsupportedEncodingException e)
			{
				throw new CCRuntimeException( e );
			}
		}
		
		return buff.toString();
	}
}
