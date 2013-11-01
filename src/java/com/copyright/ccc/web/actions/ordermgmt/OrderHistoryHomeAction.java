package com.copyright.ccc.web.actions.ordermgmt;

public class OrderHistoryHomeAction extends OMBaseAction {


	private static final long serialVersionUID = 1L;
	
	/**
	 * Default action - executed when page is first reached
	 */
	@Override
	public String execute()
	{		
		super.handleQuickTabSelected();
		
		return SUCCESS;
	}
	
	@Override
	public QuickTabSelect getDefaultQuickTabSelect() {
		return QuickTabSelect.ordermgmt_menu_order;
	}

}
