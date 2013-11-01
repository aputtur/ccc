package com.copyright.ccc.web.forms;

import com.copyright.ccc.web.CCValidatorForm;


public class LicenseDeclineForm extends CCValidatorForm
{

    private static final long serialVersionUID = 1L;
    //private static final Logger _logger = Logger.getLogger(UnpaidInvoiceForm.class);

   
private String licenseId;
private String declineCd;

private String totalAmount;

public String getTotalAmount() {
	return totalAmount;
}
public void setTotalAmount(String totalAmount) {
	this.totalAmount = totalAmount;
}

public void setLicenseId(String licenseId) {
	this.licenseId = licenseId;
}
public String getLicenseId() {
	return licenseId;
}
public void setDeclineCd(String declineCd) {
	this.declineCd = declineCd;
}
public String getDeclineCd() {
	return declineCd;
}
	
 
}