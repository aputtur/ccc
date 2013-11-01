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
 * Access the WebAdmin table to query system announcements and the like.
 * This is basically a copy of the work done for the old site.  It allows
 * Mona and company to set up downtime announcements and stuff like that
 * on our home and search pages to give people some warning.
 *
 * Error handling for this is virtually null and void.  Frankly if something
 * fails we will hear from Mona, but I really don't want every page referring
 * to this (and calling) class[es] dying some ugly, painful death that
 * our customers are forced to endure.
 */
public class WebAdminDAO {

	private Connection _cnn = null;
	static Logger _log = Logger.getLogger(WebAdminDAO.class);
	
	//	Yes, I know.  The following constants belong in the database
	//	names package of the domain.  I did not want (at least for this
	//	iteration) to propose this for the sake of brevity.
	
	static final String _pkg = "CCCTOOLS.CCC_WEB_ADMIN_PROCEDURES";
	static final String _get = "GET_ITEM";
	static final String _ins = "INS_ITEM";
	static final String _upd = "UPD_ITEM";
	static final String _pop = "POP_ITEM";
	static final String _del = "DEL_ITEM";
	
	public WebAdminDAO() {
	}
	
	//	Action methods are below.  We can insert, get, update, delete
	//	and a combination of get and delete in the pop method.
	
	public void insert(String itm, String val) throws CCException {
		String sql = null;
		CallableStatement qry = null;
		
		_log.debug("Begin insert for WebAdmin, item/value : " + itm + "/" + val );
		
		sql = "{ call " + _pkg + "." + _ins + "(?, ?) }";
		
		try {
			connect();
			
			qry = _cnn.prepareCall(sql);
			qry.setString(1, itm);
			qry.setString(2, val);
			
			qry.execute();
		}
		catch(SQLException e) {
			_log.error(LogUtil.getStack(e));
			throw new CCException("Error inserting WebAdmin record " + itm + "," + val,e);
		}
		finally {
			closeStatement(qry);
			disconnect();
		}
	}
	
	public void update(String itm, String val) throws CCException {
		String sql = null;
		CallableStatement qry = null;
		
		_log.debug("Begin update for WebAdmin, item/value : " + itm + "/" + val );
		
		sql = "{ call " + _pkg + "." + _upd + "(?, ?) }";
		
		try	{
			connect();
			
			qry = _cnn.prepareCall(sql);
			qry.setString(1, itm);
			qry.setString(2, val);
			
			qry.execute();
		}
		catch(SQLException e) {
			_log.error(LogUtil.getStack(e));			
			throw new CCException("Error updating WebAdmin record for " + itm + "," + val,e);
		}
		finally	{
			closeStatement(qry);
			disconnect();
		}
	}
	
	public String get(String itm) throws CCException {
		String sql = null;
		String val = null;
		CallableStatement qry = null;
		
		_log.debug("Begin get for WebAdmin, item : " + itm);
		
		sql = "{ call " + _pkg + "." + _get + "(?, ?) }";
		
		_log.debug("sql = " + sql);
		
		try	{
			connect();
			
			qry = _cnn.prepareCall(sql);
			qry.registerOutParameter(1, Types.VARCHAR);
			qry.setString(2, itm);
			
			qry.execute();
			val = qry.getString(1);
		}
		catch(SQLException e) {
			_log.error(LogUtil.getStack(e));
			throw new CCException("Error retrieving WebAdmin record for " + itm,e);
		}
		finally	{
			closeStatement(qry);
			disconnect();
		}
		
		return val;
	}
	
	public void delete(String itm) throws CCException {
		String sql = null;
		CallableStatement qry = null;
		
		_log.debug("Begin delete for WebAdmin, item : " + itm);
		
		sql = "{ call " + _pkg + "." + _del + "(?) }";
		
		try	{
			connect();
			
			qry = _cnn.prepareCall(sql);
			qry.setString(1, itm);
			
			qry.execute();
		}
		catch(SQLException e) {
			_log.error(LogUtil.getStack(e));
			throw new CCException("Error deleting WebAdmin record for " + itm, e);
		}
		finally {
			closeStatement(qry);
			disconnect();
		}
	}
	
	public String pop(String itm) throws CCException {
		String sql = null;
		String val = null;
		CallableStatement qry = null;
		
		_log.debug("Begin pop (get, delete) for WebAdmin, item : " + itm);
		
		sql = "{ call " + _pkg + "." + _pop + "(?, ?) }";
		
		try {
			connect();
			
			qry = _cnn.prepareCall(sql);
			qry.registerOutParameter(1, Types.VARCHAR);
			qry.setString(2, itm);
			
			qry.execute();
			val = qry.getString(1);
		}
		catch(SQLException e) {
			_log.error(LogUtil.getStack(e));
			throw new CCException("Error popping WebAdmin record for " + itm, e);
		}
		finally {
			closeStatement(qry);
			disconnect();
		}
		
		return val;
	}
	
	//	Below are our helper methods...
	//	Connect to our database.
	
	private void connect() throws CCException {
		DataSource ds = null;
		
        try {
            InitialContext ic = new InitialContext();
			ds = (DataSource) ic.lookup(CC2Configuration.getInstance().getDatasourceJNDIName());
			_cnn = ds.getConnection();
        }
        catch (NamingException e) {
			_log.error(LogUtil.getStack(e));
            throw new CCException("Database connection failure[1]", e );
        }
        catch (SQLException e) {
			_log.error(LogUtil.getStack(e));
        	throw new CCException("Database connection failure[2]", e );
        }
	}
	
	//	I think you can guess what this method does, especially
	//	considering what the ^^^^^ previous method does.
	
	private void disconnect() {
		//	Swallow any errors.
		
		try {
			if ((_cnn != null) && !_cnn.isClosed()) {
				_cnn.close();
			}
		}
		catch (SQLException e) {
			_log.error(LogUtil.getStack(e));
		}
	}
	
	private void closeStatement(CallableStatement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
            }
        }
        catch (SQLException e) {
            _log.error(LogUtil.getStack(e));
        }
	}
}