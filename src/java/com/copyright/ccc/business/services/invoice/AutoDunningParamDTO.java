package com.copyright.ccc.business.services.invoice;

import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

import com.copyright.base.Constants;
import com.copyright.opi.DTO;
import com.copyright.opi.DTOMap;
import com.copyright.opi.SQLUtils;

/**
 * Data Transfer Object for the <code>AutoDunningParam</code> object.
 */
public class AutoDunningParamDTO extends AutoDunningParamImpl implements DTO  
{
	private static final long serialVersionUID = 1L;
	
    	/**
     * Singleton instance used only for static configuration information.
     */
    private static DTO _dto = new AutoDunningParamDTO();
    
    /**
     * Name of the custom Oracle object type represented by this java class.
     */
    private static final String ORACLE_TYPE = "CCC_AUTODUNNING_PARAM_TYPE";
    
    /**
     * Name of the custom Oracle object type that contains a table of instances 
     * represented by this java class.
     */
    private static final String ORACLE_TYPE_TABLE = "CCC_AUTODUNNINGPARAM_LIST_TYPE";
    
    /**
     * Top level <code>DTOMap</code> for this java class.
     */
    private static final DTOMap DTO_MAP = new DTOMap( ORACLE_TYPE, AutoDunningParamDTO.class );
            
    /**
     * Owner of the schema in which the object type of this DTO resides.
     */
    private String _schemaOwner = Constants.EMPTY_STRING;

    
    /**
     * Null constructor.
     */
    public AutoDunningParamDTO()
    {
        super();
    }
    
    /**
     * Returns singleton instance to be used solely for reference.
     */
    public static DTO getRefInstance()
    {
        return _dto;
    }
    
    /**
     * Implementation of <code>SQLData.getSQLTypeName()</code>.
     */
    public final String getSQLTypeName() throws SQLException 
    {
        return SQLUtils.getSchemaQualifiedName( ORACLE_TYPE, _schemaOwner );
    }
    
    /**
     * Implementation of <code>DTO.getSQLTableTypeName()</code>.
     */
    public final String getSQLTableTypeName()
    {
        return SQLUtils.getSchemaQualifiedName( ORACLE_TYPE_TABLE, _schemaOwner );
    }
    
    /**
     * Implementation of <code>DTO.getDTOMap()</code>.
     */
    public final DTOMap getDTOMap()
    {
        return DTO_MAP;
    }

    /**
     * Implementation of <code>DTO.setSchemaOwner()</code>.
     */
    public void setSchemaOwner( String schemaOwner )
    {
        _schemaOwner = schemaOwner;
    }

    /**
     * Implementation of <code>DTO.getSchemaOwner()</code>.
     */
    public String getSchemaOwner()
    {
        return _schemaOwner;
    }
    
    /**
     * Implementation of <code>SQLData.readSQL()</code>.  Allows transfer of 
     * data from Oracle object type to java object.
     */
    public final void readSQL( SQLInput stream, String typeName ) throws SQLException 
    {
        setProductType( stream.readString());
        setDaysPastDue(stream.readInt());
        setEnabled(stream.readInt());                
    } 

    /**
     * Implementation of <code>SQLData.writeSQL()</code>.  Allows transfer of 
     * data from java object to Oracle object type.
     */
    public final void writeSQL( SQLOutput stream ) throws SQLException 
    {
        stream.writeString( getProductType());
        stream.writeInt( getDaysPastDue());
        stream.writeInt( getEnabled());
                      
    }
}

