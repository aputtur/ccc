package com.copyright.ccc.web.actions.ordermgmt;


public interface CustomPageControlAware {

	public String getNewPage();
	public void setNewPage(String newPage);

	public String getNewPageSize();
	public void setNewPageSize(String newPageSize);

	public abstract String rePageResults();

	public abstract String reSizePageResults();
			
}
