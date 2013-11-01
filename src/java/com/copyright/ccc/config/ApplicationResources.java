package com.copyright.ccc.config;

import org.apache.commons.configuration.ConfigurationException;

import com.copyright.base.CCCRuntimeException;
import com.copyright.base.config.ClasspathConfiguration;

/**
 * Makes available the property values from 
 * <code>ApplicationResources.properties</code>.  Useful when writing code 
 * outside of the context of the Struts messages construct.
 */
public class ApplicationResources extends ClasspathConfiguration
{

	private static final long serialVersionUID = 1L;

	/**
     * Singleton instance of this class.
     */
    private static ApplicationResources _instance;
    
    /**
     * Path to the underlying properties file, relative to this class.  If 
     * either the properties file or this class moves, this value must be 
     * updated.
     */
    private static String PROPS_FILE = "ApplicationResources.properties";
    
    /**
     * Private constructor to ensure singleton access.
     */
    private ApplicationResources() throws ConfigurationException
    {
        super( PROPS_FILE );
    }
    
    /**
     * Client code should use this method to get a handle on the 
     * <code>ApplicationResources</code> object.
     */
    public static synchronized ApplicationResources getInstance()
    {
        if ( _instance == null )
        {
            try
            {
                _instance = new ApplicationResources();
            }
            catch ( ConfigurationException ce )
            {
                String msg = "ConfigurationException while instantiating ApplicationResources";
                throw new CCCRuntimeException( msg, ce );
            }
        }
        
        return _instance;
    }
    
    //  2009-09-16  MSJ
    //  IDNO Type Code retrieval and mapping.  The parameter
    //  idnoTypeCode should be the value from IdnoTypeCdEnum.getCode().
    
    private static final String IDNO_TYPE_CD_PREFIX = "idno.typecd.";
    private static final String IDNO_TYPE_CD_NOIDNO = "idno.typecd.noidno";
    
    public String getIdnoLabel(String idnoTypeCode) {
        //  This code is not ideal.  A utility would, perhaps
        //  be better, but it seemed a fit simply because the
        //  properties are based on the type code IDs.  In the
        //  future, beyond January 2010 release, perhaps we should
        //  consider moving all properties to XML and create
        //  multiple classes that interface with multiple XML
        //  property files?
        
        String key = IDNO_TYPE_CD_PREFIX + idnoTypeCode.toLowerCase();
        String retval = this.getString(key);
        
        if (key == null) {
            retval = this.getString(IDNO_TYPE_CD_NOIDNO, "");
        }
        return retval;
    }
}
