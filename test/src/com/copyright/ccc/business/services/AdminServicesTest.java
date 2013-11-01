package com.copyright.ccc.business.services;

import com.copyright.ccc.config.CC2Constants;
import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Contains unit tests for <code>AdminServices</code> functionality.
 */
public class AdminServicesTest extends TestCase
{
    private static Logger _logger = Logger.getLogger( AdminServicesTest.class );

    /**
     * Returns the suite of all tests in this class.
     */
    public static Test suite()
    {
        TestSuite suite = new TestSuite( AdminServicesTest.class );
        return new TestSetup( suite );
    }

    /**
     * Test for {@link com.copyright.ccc.business.services.AdminServices#getDatabaseSchemaVersion()}.
     */
    public void testGetDefaultSchemaVersion()
    {
        String version = AdminServices.getDatabaseSchemaVersion();
        
        assertFalse( "Received empty schema version.", StringUtils.isEmpty( version ) );
        
        assertTrue( 
            "Schema version does not start with " + CC2Constants.SCHEMA_VERSION_PREFIX, 
            version.startsWith( CC2Constants.SCHEMA_VERSION_PREFIX ) );
    }
}
