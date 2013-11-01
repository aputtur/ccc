package com.copyright.ccc.business.services;

import java.sql.Types;

import com.copyright.ccc.business.data.access.CC2DataAccessConstants;
import com.copyright.ccc.business.data.access.CC2OracleProcedureInvokerFactory;
import com.copyright.opi.NativeTypeProcedureInvoker;

/**
 * Contains generic administrative services.
 */
public class AdminServices 
{
    private AdminServices() {}
    
    /**
     * Returns the schema version of the application database.
     */
    public static String getDatabaseSchemaVersion()
    {
        NativeTypeProcedureInvoker invoker = 
            CC2OracleProcedureInvokerFactory.getInstance().nativeTypeInvoker();
        invoker.configure(
            CC2DataAccessConstants.Admin.GET_SCHEMA_VERSION, 
            Types.VARCHAR 
        );
        
        invoker.invoke();
        
        return (String) invoker.getOutputValue();
    }
}