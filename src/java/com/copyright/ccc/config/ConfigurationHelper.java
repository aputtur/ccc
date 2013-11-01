package com.copyright.ccc.config;

import com.copyright.base.config.AppServerConfiguration;

/**
 * Utility method(s) for application configuration.
 */
public class ConfigurationHelper 
{
	private ConfigurationHelper(){}

	/**
	 * We would like to avoid generating exposure-specific .properties files.  The approach
	 * Copyright.com takes for exposure-specific properties is to provide two key/value pairs, 
	 * one for external and one for internal.  They key of the external property is suffixed 
	 * with ".EXT" and the key of the internal property is suffixed with ".INT".   
	 * 
	 * By calling this method, the property "getter" will retrieve the correct value at
	 * runtime.
	 */
    static String getExposureBasedKey( String key )
    {
    	return AppServerConfiguration.isExternal()? 
    			key + "." + AppServerConfiguration.EXTERNAL_EXPOSURE_PREFIX:
    			key + "." + AppServerConfiguration.INTERNAL_EXPOSURE_PREFIX;
    }
}
