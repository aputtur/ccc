package com.copyright.ccc.oue.client;

import java.util.HashMap;
import java.util.Map;

public class OpenUrlExtensionsSessionData
{
	protected Map<String, String> mDataMap = new HashMap<String, String>();
		
	public void setValue(String key, String value)
	{
		if (key != null)
		{
			key = key.toLowerCase();
		}
		mDataMap.put(key, value);
	}
	public String getValue(String key)
	{
		if (key != null)
		{
			key = key.toLowerCase();
		}
		return mDataMap.get(key);
	}
	public Map<String, String> getDataMap()
	{
		return mDataMap;
	}
	
	
	
}
