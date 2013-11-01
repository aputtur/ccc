package com.copyright.ccc.business.services.user;

import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

import com.copyright.base.Constants;
import com.copyright.opi.DTO;
import com.copyright.opi.DTOMap;
import com.copyright.opi.SQLUtils;

/**
 * Data Transfer Object for the <code>CCUser</code> object.
 */
public class CCUserDTO extends CCUserImpl implements DTO
{
    /**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Singleton instance used only for static configuration information.
     */
    private static DTO _dto = new CCUserDTO();

    /**
     * Name of the custom Oracle object type represented by this java class.
     */
    private static final String ORACLE_TYPE = "USER_TYPE";

    /**
     * Name of the custom Oracle object type that contains a table of instances
     * represented by this java class.
     */
    private static final String ORACLE_TYPE_TABLE = "USERS_TYPE";

    /**
     * Top level <code>DTOMap</code> for this java class.
     */
    private static final DTOMap DTO_MAP = new DTOMap( ORACLE_TYPE, CCUserDTO.class );

    /**
     * Owner of the schema in which the object type of this DTO resides.
     */
    private String _schemaOwner = Constants.EMPTY_STRING;


    /**
     * Null constructor.
     */
    public CCUserDTO()
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
        setID( stream.readLong() );
        setUserType( stream.readString() );
        setPartyID( stream.readLong() );
        setUsername( stream.readString() );
        setAuid( stream.readString() );
        setRlnkSessionId( stream.readString());
        setLastSessionStart( stream.readDate() );
        setLastCartID( stream.readLong() );
        setPK( stream.readString() );
        setAlwaysInvoice( SQLUtils.getBooleanForYN( stream.readString() ));

        for ( int i = 0; i < SearchPermissionType.getCardinality(); i++ )
        {
            setSearchPreference(
                SearchPermissionType.getByValue( i ),
                SQLUtils.getBooleanForYN( stream.readString() ));
        }

        setVersion( stream.readLong() );
        setRequestClientVersion( stream.readLong() );
        setModUserID( stream.readLong() );
        setModDateTime( stream.readTimestamp() );
        setSkipQuickprice( SQLUtils.getBooleanForYN( stream.readString() ));
        setAutoInvoiceSpecialOrder(SQLUtils.getBooleanForYN( stream.readString() ));
        String enforcePwdChg = stream.readString();
        setEnforcePwdChg(SQLUtils.getBooleanForYN(enforcePwdChg==null?"N":enforcePwdChg));
    }

    /**
     * Implementation of <code>SQLData.writeSQL()</code>.  Allows transfer of
     * data from java object to Oracle object type.
     */
    public final void writeSQL( SQLOutput stream ) throws SQLException
    {
        stream.writeLong( getID() );
        stream.writeString( getUserType() );
        stream.writeLong( getPartyID() );
        stream.writeString( getUsername() );
        stream.writeString( getAuid() );
        stream.writeString( getRlnkSessionId() );
        stream.writeTimestamp( SQLUtils.getTimestampFromDate( getLastSessionStart() ) );
        SQLUtils.writeNullableLong( stream, getLastCartID() );
        stream.writeString( getPK() );
        stream.writeString(SQLUtils.getYNForBoolean( isAlwaysInvoice() ) );

        for ( int i = 0; i < SearchPermissionType.getCardinality(); i++ )
        {

            stream.writeString(
                SQLUtils.getYNForBoolean(
                getSearchPreference( SearchPermissionType.getByValue( i ))));
        }

        stream.writeLong( getVersion() );
        stream.writeLong( getRequestClientVersion() );
        stream.writeLong( getModUserID() );
        stream.writeTimestamp( SQLUtils.getTimestampFromDate( getModDateTime() ) );
        stream.writeString(SQLUtils.getYNForBoolean( skipQuickprice() ) );
        stream.writeString(SQLUtils.getYNForBoolean( isAutoInvoiceSpecialOrder() ) );
        
        stream.writeString(SQLUtils.getYNForBoolean( getEnforcePwdChg() ) );
    }
}

