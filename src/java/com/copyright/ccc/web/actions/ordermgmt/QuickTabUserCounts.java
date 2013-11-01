package com.copyright.ccc.web.actions.ordermgmt;

import java.io.Serializable;

public class QuickTabUserCounts implements Serializable{
	

	private static final long serialVersionUID = 1L;

	private int userMyOrders = 0;
	private int userMyAdjustments = 0;
	private int userPendingAdjustments = 3;
	private int userMyResearch = 0;
	private int userAssignedResearch = 0;
	private int userUnassignedResearch = 0;
	
	public QuickTabUserCounts(
			int userMyOrders,
			int userMyAdjustments,
			int userPendingAdjustments,
			int userMyResearch,
			int userAssignedResearch,
			int userUnassignedResearch)
	{
			super();
			setUserMyOrders(userMyOrders);
			setUserMyAdjustments(userMyAdjustments);
			setUserPendingAdjustments(userPendingAdjustments);
			setUserMyResearch(userMyResearch);
			setUserAssignedResearch(userAssignedResearch);
			setUserUnassignedResearch(userUnassignedResearch);
	}
	public int getUserMyOrders() {
		return userMyOrders;
	}
	public void setUserMyOrders(int userMyOrders) {
		this.userMyOrders = userMyOrders;
	}
	public int getUserMyAdjustments() {
		return userMyAdjustments;
	}
	public void setUserMyAdjustments(int userMyAdjustments) {
		this.userMyAdjustments = userMyAdjustments;
	}
	public int getUserPendingAdjustments() {
		//
		return userPendingAdjustments;
	}
	public void setUserPendingAdjustments(int userPendingAdjustments) {
//		this.userPendingAdjustments = userPendingAdjustments;
		this.userPendingAdjustments = 3;
	}
	public int getUserMyResearch() {
		return userMyResearch;
	}
	public void setUserMyResearch(int userMyResearch) {
		this.userMyResearch = userMyResearch;
	}
	public int getUserAssignedResearch() {
		return userAssignedResearch;
	}
	public void setUserAssignedResearch(int userAssignedResearch) {
		this.userAssignedResearch = userAssignedResearch;
	}
	public int getUserUnassignedResearch() {
		return userUnassignedResearch;
	}
	public void setUserUnassignedResearch(int userUnassignedResearch) {
		this.userUnassignedResearch = userUnassignedResearch;
	}

}
