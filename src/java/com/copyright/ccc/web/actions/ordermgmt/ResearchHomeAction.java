package com.copyright.ccc.web.actions.ordermgmt;

public class ResearchHomeAction extends OMBaseAction {


	private static final long serialVersionUID = 1L;
	
	@Override
	public String execute()
	{		
		super.handleQuickTabSelected();
		
		return SUCCESS;
	}
	
	@Override
	public QuickTabSelect getDefaultQuickTabSelect() {
		return QuickTabSelect.ordermgmt_menu_research;
	}
}
