package com.copyright.ccc.web.actions.ordermgmt.data.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public abstract class SearchParmsBase<E, K> implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Integer startingRow = 0;
	private Integer rowsRequested = 25;
	private Integer numFound;
	
	/*
	 * an array of the sort criteria
	 */
	private List<E> sortCriteria = new ArrayList<E>();
			
	/**
	 * adds a sort condition to the end of the list of existing
	 * sort conditions
	 * 
	 * @param field the field on which to sort
	 * @param direction the direction (ASCENDING or DESCENDING) of the sort
	 */
	public abstract void addSortField(K field, SortDirectionEnum direction);
	
	/**
	 * removes a sort condition to the end of the list of existing
	 * sort conditions
	 * 
	 * @param field the field to remove
	 */
	public abstract void removeSortField(K field);
	
	/**
	 * Checks filters to see if any are not empty
	 */
	public abstract boolean isAnyFilterCriteriaSet(); 
	
	/**
	 * Resets all filters to empty values
	 */
	public abstract void clearFilterCriteria();

	/**
	 * Clears the currently defined sort criteria on this object
	 */
	public void clearSortSpec() {
		sortCriteria.clear();
	}
	
	/**
	 * @return the sortCriteria
	 */
	public List<E> getSortCriteria() {
		return sortCriteria;
	}


	/**
	 * @return the startingRow
	 */
	public Integer getStartingRow() {
		return startingRow;
	}

	/**
	 * @param startingRow the startingRow to set
	 */
	public void setStartingRow(Integer startingRow) {
		this.startingRow = startingRow;
	}
	/**
	 * @return the numFound
	 */
	public Integer getNumFound() {
		return numFound;
	}

	/**
	 * @param numFound the numFound to set
	 */
	public void setNumFound(Integer numFound) {
		this.numFound = numFound;
	}

	/**
	 * @return the rowsRequested
	 */
	public Integer getRowsRequested() {
		return rowsRequested;
	}

	/**
	 * @param rowsRequested the rowsRequested to set
	 */
	public void setRowsRequested(Integer rowsRequested) {
		this.rowsRequested = rowsRequested;
	}

	
	public boolean isAnySortCriteriaSet() {
	
		return isNotEmpty( getSortCriteria() )
		;
	}

	protected boolean isNotEmpty( Date date ) {
		return date != null;
	}
	protected boolean isNotEmpty( Long number ) {
		if ( number != null ) {
			return number > 0L;
		}
		return false;
	}
	
	protected boolean isNotEmpty( List<E> sortCriteriaList ) {
		if ( sortCriteriaList != null ) {
				return sortCriteriaList.isEmpty();
		}
		return true;
	}
	
	protected boolean isNotEmpty( String text ){
		return StringUtils.isNotEmpty( text );
	}
	
	protected boolean isNotEmpty( int num ){
		return num > 0;
	}
	
}
