package com.copyright.ccc.business.data;

import java.util.List;

import com.copyright.domain.data.WorkExternal;



/**
 * @author  Mike Tremblay
 * @version 1.0
 * Created 26-Oct-2006 2:07:22 PM
 */
 
public interface PublicationPermissionSearchResult {

	public int getTotalResultsFound();

	public int getNumResultsReturned();

	public Publication[] getResultSet();
	
	public void appendNewResults(List<WorkExternal> works);

}