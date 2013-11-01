package com.copyright.ccc.web.actions.ordermgmt;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.interceptor.SessionAware;

import com.copyright.ccc.business.data.OrderPurchase;
import com.copyright.ccc.business.services.order.ItemAuditServices;
import com.copyright.ccc.web.actions.ordermgmt.data.ConfirmationWrapper;
import com.copyright.ccc.web.actions.ordermgmt.data.ItemWrapper;
import com.copyright.ccc.web.actions.ordermgmt.data.OrderSearchResultWrapper;
import com.copyright.svc.order.api.data.AuditRecord;
import com.opensymphony.xwork2.ActionSupport;

public class ViewDetailAuditHistoryAction extends ActionSupport 
{
	Long itemId;
	Long confirmNumber;
	List<AuditRecord> itemAuditHistoryList = null;
		
	/**
	 * @return the itemAuditHistoryList
	 */
	public List<AuditRecord> getItemAuditHistoryList()
	{
		return itemAuditHistoryList;
	}

	/**
	 * @param itemAuditHistoryList the itemAuditHistoryList to set
	 */
	public void setItemAuditHistoryList(List<AuditRecord> itemAuditHistoryList)
	{
		this.itemAuditHistoryList = itemAuditHistoryList;
	}

	/**
	 * @return the itemId
	 */
	public Long getItemId()
	{
		return itemId;
	}

	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(Long itemId)
	{
		this.itemId = itemId;
	}

	/**
	 * @return the confirmNumber
	 */
	public Long getConfirmNumber()
	{
		return confirmNumber;
	}

	/**
	 * @param confirmNumber the confirmNumber to set
	 */
	public void setConfirmNumber(Long confirmNumber)
	{
		this.confirmNumber = confirmNumber;
	}

	/**
	 * Default action - executed when page is first reached
	 */
	@Override
	public String execute()
	{
		ItemAuditServices iaService = new ItemAuditServices();
		
		itemAuditHistoryList = iaService.getItemAudit(itemId);
		
		if (itemAuditHistoryList.size() == 0)
		{
			this.addActionMessage("No history exists for this Detail.");
		}
		
		return SUCCESS;
	}
	
}