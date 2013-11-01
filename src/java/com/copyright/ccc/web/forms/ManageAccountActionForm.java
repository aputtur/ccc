package com.copyright.ccc.web.forms;

import com.copyright.ccc.web.CCValidatorForm;

public class ManageAccountActionForm extends CCValidatorForm 
{    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean loggedIn;
    private String payPerUseCheck;
    private String gpoCheck;
    private boolean hasCustResponseOrders = false;

    public ManageAccountActionForm() { }

    public void setHasCustResponseOrders(boolean hasSpecial)
    {
    	this.hasCustResponseOrders = hasSpecial;
    }
    
    public boolean getHasCustResponseOrders()
    {
    	return hasCustResponseOrders ;
    }
    
    public void setIsLoggedIn(boolean isLoggedIn) {
        this.loggedIn = isLoggedIn;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }
    
    public void setPayPerUse() {
        this.payPerUseCheck = "CHECKED";
        this.gpoCheck = "";    
    }
    
    public void setGPO() {
        this.payPerUseCheck = "";
        this.gpoCheck = "CHECKED";     
    }

    public void setPayPerUseCheck(String payPerUseCheck) {
        this.payPerUseCheck = payPerUseCheck;
    }

    public String getPayPerUseCheck() {
        return payPerUseCheck;
    }

    public void setGpoCheck(String gpoCheck) {
        this.gpoCheck = gpoCheck;
    }

    public String getGpoCheck() {
        return gpoCheck;
    }
    
    public String getDisabled() 
    {
       String disableAttribute = "";
       if (this.loggedIn) {
         disableAttribute = "disabled"; 
       }
       return disableAttribute;
    }
}
