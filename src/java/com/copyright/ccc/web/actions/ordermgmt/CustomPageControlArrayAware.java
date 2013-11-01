package com.copyright.ccc.web.actions.ordermgmt;


public interface CustomPageControlArrayAware {

	public String[] getNewPage();
	public void setNewPage(String[] newPage);

	public String[] getNewPageSize();
	public void setNewPageSize(String[] newPageSize);

	public abstract String rePageResults();

	public abstract String reSizePageResults();
	
	public abstract String rePageResults(int pagingIndex);

	public abstract String reSizePageResults(int pagingIndex);
			
}
