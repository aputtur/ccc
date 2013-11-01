package com.copyright.ccc.web.actions.ordermgmt;

import java.io.Serializable;
import java.util.Date;


public class SpecialOrdersSearchCriteria implements Serializable
{

	    private static final long serialVersionUID = 1L;

	    private String orderNumber;
	    private String orderDetailNumber;
	    private Date startDate;
	    private Date endDate;
	    private String dateType; //maybe should be int/enum
	    private String productTOU; //maybe should be int/enum
	    private String detailStatus; //maybe should be int/enum
	    private String assignedTo;
	    
	    
	    private String standardNumber;
	    private String rightsholderName;
	    private String courseName;
	    private String publicationName;
	    private String institution;
	    private String tfWorkID;
	    private String wrWorkID;
	    private String title;
	    
	    private String sortCriteria;
	    private String sortDir = "Ascending";  //maybe should be int/enum
	    
	    private boolean advCriteriaEmpty;    
	   
		public String getOrderNumber() {
			return orderNumber;
		}
		public void setOrderNumber(String orderNumber) {
			this.orderNumber = orderNumber;
		}
		public String getOrderDetailNumber() {
			return orderDetailNumber;
		}
		public void setOrderDetailNumber(String orderDetailNumber) {
			this.orderDetailNumber = orderDetailNumber;
		}
		public Date getStartDate() {
			return startDate;
		}
		public void setStartDate(Date startDate) {
			this.startDate = startDate;
		}
		public Date getEndDate() {
			return endDate;
		}
		public void setEndDate(Date endDate) {
			this.endDate = endDate;
		}
		public String getDateType() {
			return dateType;
		}
		public void setDateType(String dateType) {
			this.dateType = dateType;
		}
		public String getProductTOU() {
			return productTOU;
		}
		public void setProductTOU(String productTOU) {
			this.productTOU = productTOU;
		}
		public String getDetailStatus() {
			return detailStatus;
		}
		public void setDetailStatus(String detailStatus) {
			this.detailStatus = detailStatus;
		}
		public String getAssignedTo() {
			return assignedTo;
		}
		public void setAssignedTo(String assignedTo) {
			this.assignedTo = assignedTo;
		}
		public String getStandardNumber() {
			return standardNumber;
		}
		public void setStandardNumber(String standardNumber) {
			this.standardNumber = standardNumber;
		}
		public String getRightsholderName() {
			return rightsholderName;
		}
		public void setRightsholderName(String rightsholderName) {
			this.rightsholderName = rightsholderName;
		}
		public String getCourseName() {
			return courseName;
		}
		public void setCourseName(String courseName) {
			this.courseName = courseName;
		}
		public String getPublicationName() {
			return publicationName;
		}
		public void setPublicationName(String publicationName) {
			this.publicationName = publicationName;
		}
		public String getInstitution() {
			return institution;
		}
		public void setInstitution(String institution) {
			this.institution = institution;
		}
		public String getTfWorkID() {
			return tfWorkID;
		}
		public void setTfWorkID(String tfWorkID) {
			this.tfWorkID = tfWorkID;
		}
		public String getWrWorkID() {
			return wrWorkID;
		}
		public void setWrWorkID(String wrWorkID) {
			this.wrWorkID = wrWorkID;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
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

		//private boolean excludeCancelled = true;
	    //private boolean showOrderHeaders = true;
	    //private boolean showOrderDetails;
	    //private boolean showCourseInfo;
	    
		public boolean isAdvCriteriaEmpty() {
		    if ((standardNumber == null || "".equals(standardNumber)) &&
		    	(rightsholderName == null || "".equals(rightsholderName)) &&
		    	(courseName == null || "".equals(courseName)) &&
		    	(publicationName == null || "".equals(publicationName)) &&
		    	(institution == null || "".equals(institution)) &&
		    	(tfWorkID == null || "".equals(tfWorkID)) &&
		    	(wrWorkID == null || "".equals(wrWorkID)) &&
		    	(title == null || "".equals(title)))
		    		return true;

		    return false;
		
		}
	    
		
	    
}