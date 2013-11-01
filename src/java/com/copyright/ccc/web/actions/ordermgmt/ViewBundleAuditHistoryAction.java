package com.copyright.ccc.web.actions.ordermgmt;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.interceptor.SessionAware;

import com.copyright.ccc.business.data.OrderPurchase;
import com.copyright.ccc.business.services.order.BundleAuditServices;
import com.copyright.ccc.business.services.order.ItemAuditServices;
import com.copyright.ccc.web.actions.ordermgmt.data.ConfirmationWrapper;
import com.copyright.ccc.web.actions.ordermgmt.data.ItemWrapper;
import com.copyright.ccc.web.actions.ordermgmt.data.OrderSearchResultWrapper;
import com.copyright.svc.order.api.data.AuditRecord;
import com.opensymphony.xwork2.ActionSupport;

public class ViewBundleAuditHistoryAction extends ActionSupport 
{
	Long bundleId;
	Long confirmNumber;
	String projectName;
	List<AuditRecord> bundleAuditHistoryList = null;
		
	/**
	 * @return the bundleAuditHistoryList
	 */
	public List<AuditRecord> getBundleAuditHistoryList()
	{
		return bundleAuditHistoryList;
	}

	/**
	 * @param itemAuditHistoryList the itemAuditHistoryList to set
	 */
	public void setBundleAuditHistoryList(List<AuditRecord> bundleAuditHistoryList)
	{
		this.bundleAuditHistoryList = bundleAuditHistoryList;
	}

	/**
	 * @return the bundleId
	 */
	public Long getBundleId()
	{
		return bundleId;
	}

	/**
	 * @param bundleId the bundleId to set
	 */
	public void setBundleId(Long bundleId)
	{
		this.bundleId = bundleId;
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
	 * @return the projectName
	 */
	public String getProjectName()
	{
		return projectName;
	}

	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName)
	{
		this.projectName = projectName;
	}

	/**
	 * Default action - executed when page is first reached
	 */
	@Override
	public String execute()
	{
		BundleAuditServices baService = new BundleAuditServices();
		bundleAuditHistoryList = baService.getBundleAudit(bundleId);
		
		if (bundleAuditHistoryList.size() == 0)
		{
			this.addActionMessage("No history exists for this bundle.");
		}
		
		return SUCCESS;
	}
	
}