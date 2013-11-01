package com.copyright.ccc.business.data.access;

import com.copyright.ccc.config.CC2Constants;
import com.copyright.internal.CCCSharedConfiguration;
import com.copyright.opi.DBContext;
import com.copyright.opi.OracleProcedureInvoker;

/**
 * Once CC2 adopts the Shared Services OPI data access framework, this will provide the means to get a handle on Oracle
 * Procedure Invoker instances.
 */
public class CC2OADOracleProcedureInvokerFactory extends com.copyright.opi.OracleProcedureInvokerFactoryBase
{

	private static CC2OADOracleProcedureInvokerFactory INSTANCE;

	/**
	 * Hide the default constructor to force use of the singleton pattern
	 */
	protected CC2OADOracleProcedureInvokerFactory()
	{
	}

	/**
	 * Provide a singleton instance that can create invokers pointing to the CC2 database
	 */
	public static synchronized CC2OADOracleProcedureInvokerFactory getInstance()
	{
		if (INSTANCE == null)
		{
			INSTANCE = new CC2OADOracleProcedureInvokerFactory();
		}
		return INSTANCE;
	}

	/**
	 * Configure the invoker to bind to the CC2 OAD database
	 */
	@Override
	protected void establishDBContext(OracleProcedureInvoker invoker)
	{
		// Get a connection pooled data source
		invoker.setDatasourceLocation(CCCSharedConfiguration.getInstance().getAppsDataSource());
		invoker.setSchemaOwner(CCCSharedConfiguration.getInstance().getAppsSchemaOwner());

		if (CC2Constants.TEST_MODE_JUNIT.equals(CC2Constants.TEST_MODE))
		{
			invoker.setUnitTestingDBConnection(CC2Constants.TEST_DB_URL, CC2Constants.TEST_DB_USERNAME,
					CC2Constants.TEST_DB_PASSWORD);
		}
	}

	/**
	 * Configure the invoker to bind to the specified <code>DBContext</code>.
	 */
	@Override
	protected void establishDBContext(OracleProcedureInvoker invoker, DBContext dbContext)
	{
		throw new UnsupportedOperationException();
	}
}
