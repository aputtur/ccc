package com.copyright.ccc.web.actions.ordermgmt;

import java.io.Serializable;
import java.util.Date;


public class AdjustmentsSearchCriteria implements Serializable
{

	    private static final long serialVersionUID = 1L;

	    private Date adjStartDate;
	    private Date adjEndDate;
	    private String acctName;
	    private String adjStatus; //maybe should be int/enum
	    private String creator;

	    private Date ordStartDate_adv;
	    private Date ordEndDate_adv;
	    private Date invStartDate_adv;
	    private Date invEndDate_adv;
	    private String refData_adv;
	    private String refType_adv; //maybe should be int/enum
	    private String reviewer_adv;
	    private String approver_adv;
	    private String title_adv;
	    private String loggedInUser_adv;
	    private long orderID_adv;
	    private long orderDetailID_adv;
	    private long invoiceID_adv;

	    public Date getAdjStartDate() {
			return adjStartDate;
		}

		public void setAdjStartDate(Date adjStartDate) {
			this.adjStartDate = adjStartDate;
		}

		public Date getAdjEndDate() {
			return adjEndDate;
		}

		public void setAdjEndDate(Date adjEndDate) {
			this.adjEndDate = adjEndDate;
		}

		public String getAdjStatus() {
			return adjStatus;
		}

		public void setAdjStatus(String adjStatus) {
			this.adjStatus = adjStatus;
		}

		public String getCreator() {
			return creator;
		}

		public void setCreator(String creator) {
			this.creator = creator;
		}

		public Date getOrdStartDate_adv() {
			return ordStartDate_adv;
		}

		public void setOrdStartDate_adv(Date ordStartDate_adv) {
			this.ordStartDate_adv = ordStartDate_adv;
		}

		public Date getOrdEndDate_adv() {
			return ordEndDate_adv;
		}

		public void setOrdEndDate_adv(Date ordEndDate_adv) {
			this.ordEndDate_adv = ordEndDate_adv;
		}

		public Date getInvStartDate_adv() {
			return invStartDate_adv;
		}

		public void setInvStartDate_adv(Date invStartDate_adv) {
			this.invStartDate_adv = invStartDate_adv;
		}

		public Date getInvEndDate_adv() {
			return invEndDate_adv;
		}

		public void setInvEndDate_adv(Date invEndDate_adv) {
			this.invEndDate_adv = invEndDate_adv;
		}

		public String getReviewer_adv() {
			return reviewer_adv;
		}

		public void setReviewer_adv(String reviewer_adv) {
			this.reviewer_adv = reviewer_adv;
		}

		public String getApprover_adv() {
			return approver_adv;
		}

		public void setApprover_adv(String approver_adv) {
			this.approver_adv = approver_adv;
		}

		public String getTitle_adv() {
			return title_adv;
		}

		public void setTitle_adv(String title_adv) {
			this.title_adv = title_adv;
		}

		public String getLoggedInUser_adv() {
			return loggedInUser_adv;
		}

		public void setLoggedInUser_adv(String loggedInUser_adv) {
			this.loggedInUser_adv = loggedInUser_adv;
		}

		public long getOrderID_adv() {
			return orderID_adv;
		}

		public void setOrderID_adv(long orderID_adv) {
			this.orderID_adv = orderID_adv;
		}

		public long getOrderDetailID_adv() {
			return orderDetailID_adv;
		}

		public void setOrderDetailID_adv(long orderDetailID_adv) {
			this.orderDetailID_adv = orderDetailID_adv;
		}

		public long getInvoiceID_adv() {
			return invoiceID_adv;
		}

		public void setInvoiceID_adv(long invoiceID_adv) {
			this.invoiceID_adv = invoiceID_adv;
		}

		private String sortCriteria;
	    private String sortDir = "Ascending";  //maybe should be int/enum

		//private boolean excludeCancelled = true;
	    //private boolean showOrderHeaders = true;
	    //private boolean showOrderDetails;
	    //private boolean showCourseInfo;
	    
	    
	    public String getSortCriteria() {
			return sortCriteria;
		}

		public void setSortCriteria(String sortCriteria) {
			this.sortCriteria = sortCriteria;
		}

		public String getSortDir() {
			return sortDir;
		}

		public void setSortDir(String sortDir) {
			this.sortDir = sortDir;
		}
		
	    public String getAcctName() {
			return acctName;
		}

		public void setAcctName(String acctName) {
			this.acctName = acctName;
		}
		
		public String getRefData_adv() {
			return refData_adv;
		}

		public void setRefData_adv(String refData_adv) {
			this.refData_adv = refData_adv;
		}

		public String getRefType_adv() {
			return refType_adv;
		}

		public void setRefType_adv(String refType_adv) {
			this.refType_adv = refType_adv;
		}


}