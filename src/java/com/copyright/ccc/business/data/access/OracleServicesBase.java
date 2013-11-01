package com.copyright.ccc.business.data.access;

import java.lang.reflect.Array;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;

import com.copyright.opi.ArrayParameter;
import com.copyright.opi.DTO;
import com.copyright.opi.DTOArrayProcedureInvoker;
import com.copyright.opi.NoOutputProcedureInvoker;
import com.copyright.opi.Query;
import com.copyright.opi.SingleDTOProcedureInvoker;
import com.copyright.opi.TypedParameter;
import com.copyright.opi.data.ModState;
import com.copyright.opi.data.PersistentDO;
import com.copyright.workbench.util.ArrayUtils2;

/**
 * Abstract parent of all services classes.
 */
public abstract class OracleServicesBase 
{
    /**
     * Returns an array of <code>DTO</code> which is the output array of 
     * the specified type from the specified 
     * <code>DTOArrayProcedureInvoker</code>.
     */
    protected static DTO[] getTypedArray( DTOArrayProcedureInvoker invoker, Class<?> outputType )
    {
        DTO[] dtos = invoker.getDTOs();
        
        if ( ArrayUtils.isEmpty( dtos ) )
        {
            return (DTO[]) Array.newInstance( outputType, 0 );
        }
        else
        {
            return (DTO[]) ArrayUtils2.convertArray( dtos, outputType );
        }
    }
    
    /**
     * Save the specified data object to the database.
     */
    protected static PersistentDO saveDataObject( 
        PersistentDO dataObj, 
        Query insertQuery, 
        Query updateQuery, 
        Query deleteQuery, 
        DTO refDTO )
    {
        // Pass the data object to be saved.
        TypedParameter[] inputParameters = { new TypedParameter( dataObj ) };
        
        // Use ModState to determine the appropriate action.
        ModState modState = dataObj.getModState();
        if ( ModState.NEW.equals( modState ) )
        {
            SingleDTOProcedureInvoker invoker = 
                CC2OracleProcedureInvokerFactory.getInstance().singleDTOInvoker();
            invoker.configure(
                insertQuery, 
                refDTO, 
                inputParameters 
            );
            
            invoker.invoke();
            
            return (PersistentDO) invoker.getDTO();
        }
        else if ( ModState.DIRTY.equals( modState ) )
        {
            SingleDTOProcedureInvoker invoker = 
                CC2OracleProcedureInvokerFactory.getInstance().singleDTOInvoker();
            invoker.configure(
                updateQuery, 
                refDTO, 
                inputParameters 
            );
            
            invoker.invoke();
            
            return (PersistentDO) invoker.getDTO();
        }
        else if ( ModState.DELETED.equals( modState ) )
        {
            NoOutputProcedureInvoker invoker = 
                CC2OracleProcedureInvokerFactory.getInstance().noOutputInvoker();
            invoker.configure(
                deleteQuery, 
                inputParameters 
            );
            
            invoker.invoke();
            
            return null;
        }
        else if ( ModState.CLEAN.equals( modState ) )
        {
            // No-op.
            return null;
        }
        else
        {
            String msg = MessageFormat.format( 
                OracleServicesConstants.Messages.IMPOSSIBLE_MOD_STATE, new Object[]{ modState } );
            throw new IllegalArgumentException( msg );
        }
    }
    
    /**
     * Save the specified array of data objects to the database.
     */
    protected static void saveArray( 
        PersistentDO[] dataObjs, 
        Query saveQuery, 
        DTO refDTO )
    {
        // Lists for tracking inserts and deletes.
        List<PersistentDO> insertList = new ArrayList<PersistentDO>();
        List<PersistentDO> deleteList = new ArrayList<PersistentDO>();
        
        // Look at each object, and place it with the inserts or the deletes.
        for ( int i=0; i<dataObjs.length; i++ )
        {
            PersistentDO dataObj = dataObjs[i];
            ModState modState = dataObj.getModState();
            
            if ( ModState.NEW.equals( modState ) )
            {
                insertList.add( dataObj );
            }
            else if ( ModState.DELETED.equals( modState ) )
            {
                deleteList.add( dataObj );
            }
            else if ( ModState.CLEAN.equals( modState ) )
            {
                // No-op.
            }
            else if ( ModState.DIRTY.equals( modState ) )
            {
                String msg = MessageFormat.format( 
                    OracleServicesConstants.Messages.SHOULD_NOT_BE_CALLED_ON_DIRTY_OBJECTS, 
                    new Object[]{ dataObj } );
                throw new IllegalArgumentException( msg );
            }
            else
            {
                String msg = MessageFormat.format( 
                    OracleServicesConstants.Messages.IMPOSSIBLE_MOD_STATE, new Object[]{ modState } );
                throw new IllegalArgumentException( msg );
            }
        }
        
        // Convert the lists to arrays.
        PersistentDO[] inserts = new PersistentDO[insertList.size()];
        insertList.toArray( inserts );
        PersistentDO[] deletes = new PersistentDO[deleteList.size()];
        deleteList.toArray( deletes );
        
        // Construct the input parameters for the invocation.
        TypedParameter[] inputParameters = new TypedParameter[]{ 
            new ArrayParameter( refDTO.getSQLTableTypeName(), inserts ),
            new ArrayParameter( refDTO.getSQLTableTypeName(), deletes )
        };
        
        NoOutputProcedureInvoker invoker = 
            CC2OracleProcedureInvokerFactory.getInstance().noOutputInvoker();
        invoker.configure(
            saveQuery, 
            inputParameters 
        );
        
        invoker.invoke();
    }
    
    /**
     * Ensures that the specified database ID is >= 1.
     */
    protected static void validateArgument( long id, String argName )
    {
        validateArgument( id, argName, false );
    }

    /**
     * If <code>isNullAcceptable</code> is <code>true</code>, ensures that the 
     * specified database ID is >= 0; otherwise, ensures that specified database 
     * ID is >= 1.
     */
    protected static void validateArgument( long id, String argName, boolean isNullAcceptable )
    {
        // Flag database ID of zero (i.e. null) if it is not acceptable.
        if ( ! isNullAcceptable )
        {
            if ( id == 0)
            {
                throw new IllegalArgumentException( argName + " == 0" );
            }
        }
        
        // Database IDs of less than zero are never acceptable.
        if ( id < 0 )
        {
            throw new IllegalArgumentException( argName + " < 0" );
        }
    }

    /**
     * Ensures that the specified <code>Object</code> is not <code>null</code>.
     */
    protected static void validateArgument( Object obj, String argName )
    {
        if ( obj == null )
        {
            throw new NullArgumentException( argName );
        }
    }

    /**
     * Ensures that the specified <code>Object[]</code> is not 
     * <code>null</code>.
     */
    protected static void validateArgument( Object[] objs, String argName )
    {
        if ( objs == null )
        {
            throw new NullArgumentException( argName );
        }
    }
    
    /**
     * Ensures that the specified <code>String</code> is not <code>null</code>.
     */
    protected static void validateArgument( String str, String argName )
    {
        if ( str == null )
        {
            throw new NullArgumentException( argName );
        }
        
        if ( StringUtils.isBlank( str ) )
        {
            throw new IllegalArgumentException( argName + " is blank" );
        }
    }
}
