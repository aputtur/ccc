package com.copyright.ccc.web.actions.ordermgmt.data;

import java.io.Serializable;

public class EditConfirmation implements Serializable {

	private static final long serialVersionUID = 1L;

	private String  accountingReference;
	private String  billingReference;
	private String  poNumber;
	private String  documentRefNumber;
	
	public String getAccountingReference() {
		return accountingReference;
	}
	public void setAccountingReference(String accountingReference) {
		this.accountingReference = accountingReference;
	}
	public String getBillingReference() {
		return billingReference;
	}
	public void setBillingReference(String billingReference) {
		this.billingReference = billingReference;
	}
	public String getPoNumber() {
		return poNumber;
	}
	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}
	public String getDocumentRefNumber() {
		return documentRefNumber;
	}
	public void setDocumentRefNumber(String documentRefNumber) {
		this.documentRefNumber = documentRefNumber;
	}
	


}
