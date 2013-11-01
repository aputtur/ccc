package com.copyright.ccc.web.util;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.copyright.ccc.CCException;
import com.copyright.ccc.config.CC2Configuration;
import com.copyright.ccc.util.LogUtil;


/**	
 *  Called from NotAvailableEmailAction.java, sendRequest after an individual 
	email is sent we want to store in a table to send later as an EXCEL spreadsheet 
	attachment of all the emails.(SCR 1113)
 */
public class CatalogRequestDAO
{
	//	Database name and methods.  I have limited them to the most simple
	//	few methods since this is not meant to be comprehensive.
	
	static final String PACKAGE = "CCCTOOLS.CCC_CATALOG_REQUEST_PKG";
	static final String PERSIST   = "p_persist_request_work";

	
	//	Log useful info...
	
	static Logger _log = Logger.getLogger(CatalogRequestDAO.class);
	
	//	Class objects.  Config gives us a good shared db name to lookup,
	//	and the connection, of course, lets us talk to the database.
	
	private Connection       _cnn    = null;
	
	
	//	Quickie constructor.
	
	public CatalogRequestDAO()
	{	
	}
	
	//	Establish a connection to the database.
	
	private void connect() throws CCException
	{
		DataSource ds = null;
		
        try
        {
            InitialContext ic = new InitialContext();
			ds = (DataSource) ic.lookup(CC2Configuration.getInstance().getDatasourceJNDIName());
			_cnn = ds.getConnection();
        }
        catch (NamingException e)
        {
            throw new CCException("Database connection failure[1]: " + e.getMessage() + LogUtil.appendableStack(e));
        }
        catch (SQLException e)
        {
        	throw new CCException("Database connection failure[2]: " + e.getMessage() + LogUtil.appendableStack(e));
        }
	}
	
	//	Disestablish a connection from the database.  :P
	
	private void disconnect()
	{
		//	Swallow any errors.
		
		try
		{
			if ((_cnn != null) && !_cnn.isClosed())
			{
				_cnn.close();
			}
		}
		catch (SQLException e)
		{
			_log.error("Failed to disconnect from database: " + e.getMessage() + LogUtil.appendableStack(e));
		}
	}
	
	public void persist(String _product, String _title, String _publisher, String _author,
					  String _volume, String _standard_number, String _rightsholder, 
					  String _publication_year ,String _your_name, String _email_address, 
					  String _company, String _phone_number, String _city, String _state,
					  String _annual_license, String _additional_info)
	throws CCException
	{
		CallableStatement qry = null;
		
		
		int status = 0;
		
		String sql = "{ call " + PACKAGE + "." + PERSIST + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";
		
		
		try
		{
			connect();
			
			qry = _cnn.prepareCall(sql);
			qry.registerOutParameter(1, Types.NUMERIC); 
			qry.setString(2, _product);
			qry.setString(3, _title);
			qry.setString(4, _publisher);
			qry.setString(5, _author);
			qry.setString(6, _volume);
			qry.setString(7, _standard_number);
			qry.setString(8, _rightsholder);
			qry.setString(9, _publication_year);
			qry.setString(10, _your_name);		
			qry.setString(11, _email_address);
			qry.setString(12, _company);
			qry.setString(13, _phone_number);
			qry.setString(14, _city);
			qry.setString(15, _state);
			qry.setString(16,_annual_license);
                        qry.setString(17, _additional_info);
			
			qry.execute();
			
			status = qry.getInt(1);
			
			if (status != 0)
			{
				throw new CCException("Error returned from database persist, err code =" + status);
			}
		}
		catch(SQLException e)
		{
			throw new CCException("Error inserting into catalog request table: " + e.getMessage() + LogUtil.appendableStack(e));
		}
		finally
		{
			closeStatement(qry);
			disconnect();
		}
	}
	
	
	private void closeStatement(CallableStatement stmt)
	{
        try
        {
            if (stmt != null)
            {
                stmt.close();
            }
        }
        catch (SQLException e)
        {
            _log.error(e.getMessage() + LogUtil.appendableStack(e));
        }
	}
	
}