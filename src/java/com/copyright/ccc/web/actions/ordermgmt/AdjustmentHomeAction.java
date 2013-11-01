package com.copyright.ccc.web.actions.ordermgmt;

public class AdjustmentHomeAction extends OMBaseAction {


	private static final long serialVersionUID = 1L;

	/**
	 * Default action - executed when page is first reached
	 */
	@Override
	public String execute()
	{		
		return SUCCESS;
	}
	
	public String showActionMessageSamples() {
		
		addActionMessage("An Action Message Goes Here");
		
		return SUCCESS;
	}
	
	@Override
	public QuickTabSelect getDefaultQuickTabSelect() {
		return QuickTabSelect.ordermgmt_menu_adjustment;
	}

}
