/*
 * RequestTitleHandler.java
 * Copyright (c) 2007, Copyright Clearance Center, Inc. All rights reserved.
 * ----------------------------------------------------------------------------
 * Revision History
 * 2007.10.24	tmckinney	Created.
 * 2009.07.08   tmckinney   Manage OracleCallableStatment in JBoss.
 * 2011.07.18	tmckinney	Integrated into CC3 from Siva's work.
 * ----------------------------------------------------------------------------
 */
package com.copyright.ccc.quartz.services;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.copyright.base.CCCRuntimeException;
import com.copyright.workbench.logging.LoggerHelper;
import com.copyright.workbench.sql.ConnectionUtils;
import com.copyright.workbench.sql.ConnectionWrapper;


/**
 * JDBC connector to the CCCTOOLS.CATALOG_REQUEST_TABLE.
 */
public class RequestTitleHandler
{
	
	private static Logger logger = LoggerHelper.getLogger();
	
	
	private ConnectionWrapper connWrapper;
	
	public RequestTitleHandler(ConnectionWrapper connWrapper)
	{
		this.connWrapper = connWrapper;
	}
	
	
	protected Connection getConnection()
	{
		return this.connWrapper.createConnection(false);
	}
        
	/**
	 * Queries jobs by submission date.
	 * @param submitDate
	 * @return
	 */
	List<RequestTitleData> getRequests()
	{
        if (logger.isDebugEnabled()) 
        {
        	logger.debug("RequestTitleHandler.getRequests() entered.");
        }
        
		Connection conn = null;
		CallableStatement stmt = null;
		Statement s2 = null;
		ResultSet rs = null;
		
		List<RequestTitleData> result = new ArrayList<RequestTitleData>();
		
		try
        {
			conn = getConnection();
			
			/*
			 * 1. Remove old requests
			 */
			
            stmt = conn.prepareCall(
            		"{ ? = call CCCTOOLS.CCC_CATALOG_REQUEST_PKG.f_remove_request_sent }");
            
            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.execute();     

            //handle error
            int errorcode = stmt.getInt(1);
            if (errorcode != 0)
            {
                String errMsg = "Error in CCCTOOLS.CCC_CATALOG_REQUEST_PKG.f_remove_request_sent (SQL return):" +
                	Integer.toString(errorcode);
                
                throw new CCCRuntimeException(errMsg);
            }
            
            
            /*
             * 2. Set flag on requests to be collected into a message now
             */
            
            stmt = conn.prepareCall(
            		"{ ? = call CCCTOOLS.CCC_CATALOG_REQUEST_PKG.f_update_request_sent }");
            
            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.execute();     

            //handle error
            errorcode = stmt.getInt(1);
            if (errorcode != 0)
            {
                String errMsg = "Error in CCCTOOLS.CCC_CATALOG_REQUEST_PKG.f_update_request_sent (SQL return):" +
                	Integer.toString(errorcode);
                
                throw new CCCRuntimeException(errMsg);
            }
            
            
            
            /*
             * 3. Collect the flagged requests
             */
            
            String query = "select product, title, publisher, author, volume, standard_number, rightsholder, publication_year, your_name, email_address, company, phone_number, city, state, annual_license, additional_info"
              + " from ccctools.ccc_catalog_requests"
              + " where upper(sent_flag)='Y'";
            
            s2 = conn.createStatement();
            s2.execute(query);
            
            rs = s2.getResultSet();
            
            while (rs.next())
            {
            	RequestTitleData data = new RequestTitleData();
            	data.setProduct(rs.getString("product"));
            	data.setTitle(rs.getString("title"));
            	data.setPublisher(rs.getString("publisher"));
            	data.setAuthor(rs.getString("author"));
            	data.setVolume(rs.getString("volume"));
            	data.setStandardNumber(rs.getString("standard_number"));
            	data.setRightsholder(rs.getString("rightsholder"));
            	data.setPublicationYear(rs.getString("publication_year"));
            	data.setYourName(rs.getString("your_name"));
            	data.setEmailAddress(rs.getString("email_address"));
            	data.setCompany(rs.getString("company"));
            	data.setPhoneNumber(rs.getString("phone_number"));
            	data.setCity(rs.getString("city"));
            	data.setState(rs.getString("state"));
            	data.setAnnualLicense(rs.getString("annual_license"));
            	data.setAdditionalInfo(rs.getString("additional_info"));
            	
            	result.add(data);
            }
            
    		return result;

        }
        catch (SQLException se)
        {
        	logger.warn(
        		"Exception while attempting to read a table of Catalog Requests from DB.",se);
        	throw new CCCRuntimeException(se);
        }
        finally 
        {
        	ConnectionUtils.closeQuietly(rs);
        	ConnectionUtils.closeQuietly(s2);
        	ConnectionUtils.closeQuietly(stmt);
	    	ConnectionUtils.closeQuietly(conn);
        }
		

	}
	
	

}