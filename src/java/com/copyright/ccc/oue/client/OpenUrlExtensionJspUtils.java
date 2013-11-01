package com.copyright.ccc.oue.client;

import javax.servlet.http.HttpSession;

import com.copyright.ccc.oue.connector.OpenUrlExtensionServiceConnector;
import com.copyright.ccc.oue.connector.OperationException;

public class OpenUrlExtensionJspUtils extends OpenUrlExtensionUtils
{

	private HttpSession session;

	public OpenUrlExtensionJspUtils(HttpSession session)
	{
		init(session);
	}

	public void init(HttpSession session)
	{
		this.session = session;
	}

	public OpenUrlExtensionsSessionData getExtensionData()
	{
		//return (OpenUrlExtensionsSessionData) session.getAttribute(SESSION_KEY);
		return OpenUrlExtensionUtils.getExtensionData(session);
	}

	public boolean isValidOUESession()
	{
		return getExtensionData() != null ? true : false;
	}

	public String runRemoteOperation(String operationName)
	{
		OpenUrlExtensionsSessionData data = OpenUrlExtensionUtils.getExtensionData(session);
		String resultString = null;

		if (data != null)
		{
			try
			{
				resultString = OpenUrlExtensionServiceConnector.execute(operationName, data);
			}
			catch (OperationException e)
			{
				resultString = ERROR_HTML;
			}
		}

		return resultString;

	}

	public String displayContentTitle()
	{
		return runRemoteOperation("identifier");
	}

	public String displayContentDate()
	{
		return runRemoteOperation("publishingYear");
	}

	public String displayContentFullDate()
	{
		return runRemoteOperation("fullPublishingYear");
	}

	public String displayDefaultTotalNumberPage()
	{
		return runRemoteOperation("numberPage");
	}

	public String displayDefaultTotalNumberCopy()
	{
		return runRemoteOperation("numberCopy");
	}

	public String displayDefaultPageNumberRange()
	{
		return runRemoteOperation("pageRange");
	}

}
