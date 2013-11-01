/*
 * CC2Constants.java
 * Copyright (c) 2006, Copyright Clearance Center, Inc. All rights reserved.
 * ----------------------------------------------------------------------------
 * Revision History
 * 2006.10.23   ccollier     Created.
 * 2007.10.15   tmckinney    Removed CONFIG_FILE, CONFIG_DIR.
 * 2009.05.14   tmckinney    Removed BUILD_VERSION, RELEASE_VERSION.
 * 2009.07.01   tmckinney    Added LOG4J_RESOURCE.
 * ----------------------------------------------------------------------------
 */
package com.copyright.ccc.config;

/**
 * Global (app-wide) configuration constants go here
 */
public class CC2Constants
{
    /**
     * Initial portion of application build version.
     */
    public static final String APPLICATION_VERSION_PREFIX = "build_";

    /**
     * Initial portion of database schema version.
     */
    public static final String SCHEMA_VERSION_PREFIX = "CC_";

    /**
     * Initial portion of data patch level.
     */
    public static final String DATA_PATCH_LEVEL_PREFIX = "patch";

    /**
     * System property (set via command-line parameter -D) that specifies the
     * test mode of the application.
     */
    public static final String TEST_MODE = System.getProperty( "cc2.test.mode" );

    /**
     * The application is being tested by the JUnit framework.
     */
    public static final String TEST_MODE_JUNIT = "junit";

    /**
     * System property that specifies the db url to use when in test mode.
     */
    public static final String TEST_DB_URL = System.getProperty( "cc2.test.db.url" );

    /**
     * System property that specifies the db username to use when in test mode.
     */
    public static final String TEST_DB_USERNAME = System.getProperty( "cc2.test.db.username" );

    /**
     * System property that specifies the db password to use when in test mode.
     */
    public static final String TEST_DB_PASSWORD = System.getProperty( "cc2.test.db.password" );

    /**
     * System property that specifies the data directory to use when in test mode.
     */
    public static final String TEST_DATA = System.getProperty( "cc2.test.data" );

    /**
     * System property that overrides the schema owner specified in the
     * CC2Configuration.properties file.
     */
    public static final String SCHEMA_OWNER = System.getProperty( "cc2.schema.owner" );

    /**
     * Name of lo4j properties file to load at app startup time.
     */
    public static final String LOG4J_RESOURCE = "ccc-log4j.properties";
    
    /**
     * default platform newline character, equivalent to System.getProperty("line.separator")
     */
    public static final String NEWLINE = System.getProperty("line.separator");
}
