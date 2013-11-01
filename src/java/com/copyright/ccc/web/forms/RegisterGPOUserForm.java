package com.copyright.ccc.web.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.copyright.ccc.business.services.user.User;
import com.copyright.ccc.web.CCValidatorForm;

public class RegisterGPOUserForm extends CCValidatorForm
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private String firstName;
  private String middleName;
  private String lastName;
  private String title;
  private String userName;
  private String password;
  private String confirmPassword;
  private String phone;
  private String phoneExtension;
  private String fax;
  private String personType;
  private String account;
  private String mainContactLastName;
  private String status = "CREATE";
  private String mailingPersonPartyId = "";
  private String submitFlag = "F";
  private String isLoggedIn = "FALSE";
  
  public RegisterGPOUserForm()
  {
    super();
  }
  
    public void reSet()
    {
        this.title = "";
        this.firstName = "";
        this.middleName = "";
        this.lastName = "";
        this.phone = "";
        this.phoneExtension = "";
        this.fax = "";
        this.userName = "";
        this.mailingPersonPartyId = "";
        this.personType = "";
        this.mainContactLastName = "";
        this.account = "";
        this.status = "CREATE";
        this.isLoggedIn = "FALSE";
        this.submitFlag = "F";
    }
  
    public RegisterGPOUserForm(User regUser)
        {
          super();
          this.title = regUser.getTitle();
          this.firstName = regUser.getFirstName();
          this.middleName = regUser.getMiddleName();
          this.lastName = regUser.getLastName();
          this.phone = regUser.getPhoneNumber();
          this.phoneExtension = regUser.getExtension();
          //this.phone = regUser.getPrimaryPhone().getPhoneNumber();
          //this.phoneExtension = regUser.getPrimaryPhone().getExtension();
          this.fax = regUser.getFaxNumber();
          //this.fax = regUser.getFax().getPhoneNumber();
          this.userName = regUser.getUsername();
          this.mailingPersonPartyId = regUser.getPartyId().toString();
          this.personType = regUser.getMarketSegment();
          this.mainContactLastName = regUser.getAccountContactName();
          this.account = regUser.getAccount().getAccountNumber();
            
                  
        }
   
  public String getIsLoggedIn()
        {
          return isLoggedIn;
        }
      
  public void setIsLoggedIn(String log)
        {
          isLoggedIn = log;
        }
    
  public String getFirstName()
    {
      return firstName;
    }
  
  public void setFirstName(String fName)
    {
      firstName = fName;
    }
    
  public String getMiddleName()
    {
      return middleName;
    }
  
  public void setMiddleName(String mName)
    {
      middleName = mName;
    }
    
  public String getLastName()
    {
      return lastName;
    }
  
  public void setLastName(String lName)
    {
      lastName = lName;
    }
    
  public String getTitle()
    {
      return title;
    }
  
  public void setTitle(String ttl)
    {
      title = ttl;
    }
    
  public String getUserName()
    {
      return userName;
    }
  
  public void setUserName(String uName)
    {
      userName = uName;
    }
    
  public String getPassword()
    {
      return password;
    }
    
  public void setPassword(String pword)
    {
      password = pword;
    }
    
  public String getConfirmPassword()
    {
      return confirmPassword;
    }
    
  public void setConfirmPassword(String confpword)
    {
      confirmPassword = confpword;
    }
      
  public String getPhone()
    {
      return phone;
    }
  
  public void setPhone(String phn)
    {
      phone = phn;
    }
    
  public String getPhoneExtension()
    {
      return phoneExtension;
    }
  
  public void setPhoneExtension(String phnExt)
    {
      phoneExtension = phnExt;
    }
    
  public String getFax()
    {
      return fax;
    }
  
  public void setFax(String fx)
    {
      fax = fx;
    }
        
  public String getPersonType()
    {
      return personType;
    }
  
  public void setPersonType(String pType)
    {
      personType = pType;
    }
    
  public String getAccount()
    {
      return account;
    }
  
  public void setAccount(String acct)
    {
      account = acct;
    }
    
  public String getMainContactLastName()
    {
      return mainContactLastName;
    }
  
  public void setMainContactLastName(String mainContact)
    {
      mainContactLastName = mainContact;
    }
    
  public String getStatus()
    {
      return status;
    }
  
  public void setStatus(String stat)
    {
      status = stat;
    }
    
  public String getMailingPersonPartyId()
    {
      return mailingPersonPartyId;
    }
  
  public void setMailingPersonPartyId(String mailPartyId)
    {
      mailingPersonPartyId = mailPartyId;
    }

  public String getSubmitFlag()
    {
      return submitFlag;
    }
    
  public void setSubmitFlag(String subFlag)
    {
      submitFlag = subFlag;
    }
    
  private boolean isNumber(String s)
  {
    String validChars = "0123456789";
    boolean isNumber = true;
 
    for (int i = 0; i < s.length() && isNumber==true; i++) 
    { 
      char c = s.charAt(i); 
      if (validChars.indexOf(c) == -1) 
      {
        isNumber = false;
      }
    }
    return isNumber;
  }    
  
  @Override
  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
  {
      ActionErrors errors = super.validate(mapping, request);
      
      //String    loggedIn = (String) request.getSession().getAttribute("loggedIn");
      //if (loggedIn == null) loggedIn = new String("false");
      
      if (!mapping.getPath().equalsIgnoreCase("/redisplayRegistration"))
      {
      
          //loggedIn = (String) request.getSession().getAttribute("loggedIn");
          
          //if (loggedIn == null) loggedIn = new String("false");
          
           if (this.status.equalsIgnoreCase("CREATE"))
          //if (!UserContextService.isUserAuthenticated())
          //if (loggedIn.equalsIgnoreCase("false")) 
            {
                if (account == null || account.equals("")) 
                {
                    errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.required","Account Number"));
                }
                
                if (account!= null && isNumber(account) == false)
                {
                    errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.accountnumfmt", "Account Number"));  
                }
                
                if (mainContactLastName == null || mainContactLastName.equals("")) 
                {
                    errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.required","Primary Contact Last Name"));
                } 
                
                if (password == null || password.equals("")) 
                {
                    errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.required","Password"));
                } 
                else if (password.length() < 6 )   //|| !containsNumeric(password))
                {
                    errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.invalidPassword","Password"));
                }
                if (confirmPassword == null || confirmPassword.equals("")) 
                {
                    errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.required","Re-enter Password"));
                } 
                else if (confirmPassword.length() < 6) // || !containsNumeric(confirmPassword))
                {
                    errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.invalidPassword","Re-enter Password"));
                } 
                if (password != null && !password.equals(confirmPassword)) 
                {
                        errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.unmatchedPassword","Password", "Re-enter Password"));
                }
            }      
        }
        return errors;
      
  }
  
}