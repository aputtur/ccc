package com.copyright.ccc.web.actions.ordermgmt.data;

import java.io.Serializable;

public class AddNewItem implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String ID;
	private String bundleID;
	private String confNumber;
	
	private String publicationTitle;
	private String workInst;
	private String wrWorkInst;
	private String standardNumber;
	private String itemSubDescription;
	private String idnoLabel;
	private String publisher;
	
	private Long product;
	private Long tou;
	
	/*	private String editor;
	private String dateOfIssue;
	private String numberofPages;
	private String edition;
	private String volume;
	private String numberOfCopies;
	private String chapterArticle;
	private String author;
	private String pageRange;
	private String rhRefNum;
	
	private String publicationYearOfUse;
	private String estNumOfStudents; */


	
	
	public String getID() {
		return ID;
	}
	public Long getProduct() {
		return product;
	}
	public void setProduct(Long product) {
		this.product = product;
	}
	public Long getTou() {
		return tou;
	}
	public void setTou(Long tou) {
		this.tou = tou;
	}
	public void setID(String id) {
		ID = id;
	}
	public String getBundleID() {
		return bundleID;
	}
	public void setBundleID(String bundleID) {
		this.bundleID = bundleID;
	}
	public String getConfNumber() {
		return confNumber;
	}
	public void setConfNumber(String confNumber) {
		this.confNumber = confNumber;
	}
	public String getPublicationTitle() {
		return publicationTitle;
	}
	public void setPublicationTitle(String publicationTitle) {
		this.publicationTitle = publicationTitle;
	}
	public String getWorkInst() {
		return workInst;
	}
	public void setWorkInst(String workInst) {
		this.workInst = workInst;
	}
	public String getWrWorkInst() {
		return wrWorkInst;
	}
	public void setWrWorkInst(String wrWorkInst) {
		this.wrWorkInst = wrWorkInst;
	}
	public String getStandardNumber() {
		return standardNumber;
	}
	public void setStandardNumber(String standardNumber) {
		this.standardNumber = standardNumber;
	}
	public String getItemSubDescription() {
		return itemSubDescription;
	}
	public void setItemSubDescription(String itemSubDescription) {
		this.itemSubDescription = itemSubDescription;
	}
	public String getIdnoLabel() {
		return idnoLabel;
	}
	public void setIdnoLabel(String idnoLabel) {
		this.idnoLabel = idnoLabel;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	

}
