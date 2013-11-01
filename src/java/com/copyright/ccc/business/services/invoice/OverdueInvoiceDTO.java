package com.copyright.ccc.business.services.invoice;

import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

import com.copyright.base.Constants;
import com.copyright.opi.DTO;
import com.copyright.opi.DTOMap;
import com.copyright.opi.SQLUtils;

/**
 * Data Transfer Object for the <code>IOverdueInvoice</code> object.
 */
public class OverdueInvoiceDTO extends OverdueInvoiceImpl implements DTO  
{
	private static final long serialVersionUID = 1L;
	
    	/**
     * Singleton instance used only for static configuration information.
     */
    private static DTO _dto = new OverdueInvoiceDTO();
    
    /**
     * Name of the custom Oracle object type represented by this java class.
     */
    private static final String ORACLE_TYPE = "CCC_OVERDUE_INVOICE_TYPE";
    
    /**
     * Name of the custom Oracle object type that contains a table of instances 
     * represented by this java class.
     */
    private static final String ORACLE_TYPE_TABLE = "CCC_OUVERDUE_INVOICE_LIST_TYPE";
    
    /**
     * Top level <code>DTOMap</code> for this java class.
     */
    private static final DTOMap DTO_MAP = new DTOMap( ORACLE_TYPE, OverdueInvoiceDTO.class );
            
    /**
     * Owner of the schema in which the object type of this DTO resides.
     */
    private String _schemaOwner = Constants.EMPTY_STRING;

    
    /**
     * Null constructor.
     */
    public OverdueInvoiceDTO()
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
    	setCustomerName(stream.readString());
    	setPartyId(stream.readLong());
    	setCustomerNumber(stream.readLong());
    	setInvoiceNumber(stream.readString());
    	setInvoiceDate(stream.readDate());
    	setGlDate(stream.readDate());
    	setTrxType(stream.readString());
    	setOrderNumber(stream.readString());
    	setInvoiceAmount(stream.readDouble());
    	setTaxAmount(stream.readDouble());
    	setOutstandingAmount(stream.readDouble());
    	setAcctdOutstandingAmount(stream.readDouble());
    	setDueDate(stream.readDate());
    	setDaysOutstanding(stream.readInt());
    	setInstallmentNumber(stream.readLong());
    	setBillToName(stream.readString());
    	setBillToAddress(stream.readString());
    	setAdminName(stream.readString());
    	setAdminAddress(stream.readString());
                
    } 

    /**
     * Implementation of <code>SQLData.writeSQL()</code>.  Allows transfer of 
     * data from java object to Oracle object type.
     */
    public final void writeSQL( SQLOutput stream ) throws SQLException 
    {
    	
    	stream.writeString(getCustomerName());
    	stream.writeLong(getPartyId());
    	stream.writeLong(getCustomerNumber());
    	stream.writeString(getInvoiceNumber());
    	stream.writeTimestamp( SQLUtils.getTimestampFromDate(getInvoiceDate()));
    	stream.writeTimestamp( SQLUtils.getTimestampFromDate(getGlDate()));
    	stream.writeString(getTrxType());
    	stream.writeString(getOrderNumber());
    	stream.writeDouble(getInvoiceAmount());
    	stream.writeDouble(getTaxAmount());
    	stream.writeDouble(getOutstandingAmount());
    	stream.writeDouble(getAcctdOutstandingAmount());
    	stream.writeTimestamp( SQLUtils.getTimestampFromDate(getDueDate()));
    	stream.writeInt(getDaysOutstanding());
    	stream.writeLong(getInstallmentNumber());
    	stream.writeString(getBillToName());
    	stream.writeString(getBillToAddress());
    	stream.writeString(getAdminName());
    	stream.writeString(getAdminAddress());
                      
    }
}

