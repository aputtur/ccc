package com.copyright.ccc.web.actions.ordermgmt.data;

import java.io.Serializable;

public class CustomPageControl implements Serializable {

	private static final long serialVersionUID = 1L;

	private int page = 1;
	private int listSize; //this is set to 1000 for searches that return >1000 results
	private int totalRecords;
	private int lastPageSize;
	private int pageSize;
	private boolean pageSizeChanged = false;
	private boolean rowsZeroBased = false;
	
	public static CustomPageControl copy( CustomPageControl copy ) {
		CustomPageControl newCopy = new CustomPageControl();
		newCopy.setLastPageSize(copy.getLastPageSize());
		newCopy.setListSize(copy.getListSize());
		newCopy.setTotalRecords(copy.getTotalRecords());
		newCopy.setPage(copy.getPage());
		newCopy.setPageSize(copy.getPageSize());
		newCopy.setPageSizeChanged(copy.isPageSizeChanged());
		newCopy.setRowsZeroBased(copy.isRowsZeroBased());
		return newCopy;
	}
	
	public boolean isRowsZeroBased() {
		return rowsZeroBased;
	}

	public void setRowsZeroBased(boolean rowsZeroBased) {
		this.rowsZeroBased = rowsZeroBased;
	}

	public CustomPageControl() {
		super();
	}
	
	public CustomPageControl(int pageSize) {
		super();
		this.lastPageSize = pageSize;
		this.pageSize = pageSize;
	}
	public CustomPageControl(int pageSize, boolean isZeroBased) {
		super();
		this.lastPageSize = pageSize;
		this.pageSize = pageSize;
		this.rowsZeroBased = isZeroBased;
	}
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}

	public int getTotalPages() {
		return (int)Math.ceil(listSize/(pageSize * 1.0));
	}
	
	public int getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public int getListSize() {
		return listSize;
	}
	public void setListSize(int listSize) {
		this.listSize = listSize;
	}
	public int getLastPageSize() {
		return lastPageSize;
	}
	public void setLastPageSize(int lastPageSize) {
		this.lastPageSize = lastPageSize;
	}
	public int getPageSize() {
		return pageSize;
	}

	public boolean isPageSizeChanged() {
		return pageSizeChanged;
	}

	public void setPageSizeChanged(boolean pageSizeChanged) {
		this.pageSizeChanged = pageSizeChanged;
	}
	public void setPageSize(int pageSize) {
		if (pageSize != lastPageSize)
		{
			this.pageSizeChanged = true;
		}
		
		this.pageSize = pageSize;		
	}
	
	public int getLastStartingRow() {
		return ( getPage() - 1) * getLastPageSize();										
	}
	
	public int getPageAfterHandlingPageSizeChange() {
		handlePageSizeChanged();
		return getPage(); 
	}
	
	private void handlePageSizeChanged() {
		
		if ( isPageSizeChanged() )
		{
			int lastStartingRow = ( getPage()-1) * getLastPageSize() + getPlusOneIfNeeded();	
			int lastStartingRowsNewPage = lastStartingRow / getPageSize() + getPlusOneIfNeeded();
			setPage( lastStartingRowsNewPage );
			setPageSizeChanged( false );
		}
	}
	
	public int getNextStartingRow() {
		return getPageSize() * ( getPage() - 1 ) + getPlusOneIfNeeded() ;
	}
	
	public int getNextRowsRequested() {
		return getPageSize() * getPage();
	}
	
	private int getPlusOneIfNeeded() {
		if ( isRowsZeroBased() ) {
			return 0;
		} else {
			return 1;
		}
	}
	
}
