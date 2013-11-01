package com.copyright.ccc.web.forms;

import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.web.CCValidatorForm;

public class BaseRegisterForm extends CCValidatorForm{
	private static final long serialVersionUID = 1L;
	public static final String TRUE = "T";
	public static final String FALSE = "F";

	 private String alwaysInvoice = "F";
	 private String alwaysInvoiceFlag = "F";
	 private String autoInvoiceSpecialOrder = "F";
	 private String autoInvoiceSpecialOrderFlag = "F";
	  
	  
	  public String getAlwaysInvoice()
      {
        return alwaysInvoice;
      }
    
    public void setAlwaysInvoice(String always)
      {
        alwaysInvoice = always;
      }
      
    public String getAlwaysInvoiceFlag()
      {
        return alwaysInvoiceFlag;
      }
    
    public void setAlwaysInvoiceFlag(String invoiceFlag)
      {
        alwaysInvoiceFlag = invoiceFlag;
      }
    
    public String getAutoInvoiceSpecialOrder()
    {
      return autoInvoiceSpecialOrder;
    }
  
  public void setAutoInvoiceSpecialOrder(String autoInvoice)
    {
	  autoInvoiceSpecialOrder = autoInvoice;
    }
    
  public String getAutoInvoiceSpecialOrderFlag()
    {
      return autoInvoiceSpecialOrderFlag;
    }
  
  public void setAutoInvoiceSpecialOrderFlag(String autoInvoiceFlag)
    {
	  autoInvoiceSpecialOrderFlag = autoInvoiceFlag;
    }
  
  
  /**
   * Set/reset checkbox in user profile update screens
   */
  public void checkUserPreferenceSetting(){
	   boolean isAlwaysInvoice = UserContextService.getActiveAppUser().isAlwaysInvoice();
       boolean isAutoInvoiceSpecialOrder = UserContextService.getActiveAppUser().isAutoInvoiceSpecialOrder();
       
       if (isAlwaysInvoice)
       {
          setAlwaysInvoice(BaseRegisterForm.TRUE);
          setAlwaysInvoiceFlag(BaseRegisterForm.TRUE);
       }
       else
       {
       	setAlwaysInvoice(BaseRegisterForm.FALSE);
       	setAlwaysInvoiceFlag(BaseRegisterForm.FALSE);
       }
       
       if (isAutoInvoiceSpecialOrder)
       {
       	setAutoInvoiceSpecialOrder(BaseRegisterForm.TRUE);
       	setAutoInvoiceSpecialOrderFlag(BaseRegisterForm.TRUE);
       }
       else
       {
       	setAutoInvoiceSpecialOrder(BaseRegisterForm.FALSE);
       	setAutoInvoiceSpecialOrderFlag(BaseRegisterForm.FALSE);
       }
  }
    
	  
  
}