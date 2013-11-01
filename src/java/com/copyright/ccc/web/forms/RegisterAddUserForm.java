package com.copyright.ccc.web.forms;

import com.copyright.ccc.web.util.WebUtils;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.business.services.user.User;
import com.copyright.svc.ldapuser.api.LdapUserService;
import com.copyright.svc.ldapuser.api.data.LdapUserConsumerContext;
import com.copyright.svc.rlUser.api.RLUserServicesInterface;
import com.copyright.svc.rlUser.api.data.RlnkUser;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.*;

public class RegisterAddUserForm extends BaseRegisterForm
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
public static final String TRUE = "T";
  public static final String FALSE = "F";
    
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
  private String  taxId;
  private boolean registeredRlnkUser=false;
  
  
  
  
  public RegisterAddUserForm()
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
  
  public RegisterAddUserForm(User regUser)
      {
          
          super();
          this.title = regUser.getTitle();
          this.firstName = regUser.getFirstName();
          this.middleName = regUser.getMiddleName();
          this.lastName = regUser.getLastName();
          this.phone = regUser.getPhoneNumber();
          this.phoneExtension = regUser.getExtension();
          this.fax = regUser.getFaxNumber();
          this.userName = regUser.getUsername();
          this.mailingPersonPartyId = regUser.getPartyId().toString();
          this.personType = regUser.getMarketSegment();
          this.mainContactLastName = regUser.getAccountContactName();
          this.account = regUser.getAccount().getAccountNumber();
          
          RLUserServicesInterface rlUserService= ServiceLocator.getRLUserService();
          // check for rightslink user         
          LdapUserService ldapService = ServiceLocator.getLdapUserService();
          regUser.setLdapUser(ldapService.getUser(new LdapUserConsumerContext(), regUser.getUsername()));
          String rightslnkPartyId=regUser.getLdapUser().getRightsLinkPartyID();
          if(StringUtils.isNotEmpty(rightslnkPartyId)){
         		RlnkUser rlnkUser=rlUserService.getUserByUserName(regUser.getUsername());
                 	 this.registeredRlnkUser=Boolean.TRUE;
                 	 this.setTaxId(rlnkUser.getTaxId());
          }
          
         
          
          
          //Get the Always Invoice Flag
          checkUserPreferenceSetting();
                
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
  
    
    //  MSJ 2008/01/24
    //  This could break registration stuff.  Will have to watch and see.
    //  We need to make sure our checkbox is cleared to ensure that the
    //  user can uncheck the option.
    
    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
    }
    
  @Override
  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
  {
      ActionErrors errors = super.validate(mapping, request);
      
      //String    loggedIn = (String) request.getSession().getAttribute("loggedIn");
      //if (loggedIn == null) loggedIn = new String("false");
      
      if (request.getMethod().equalsIgnoreCase("GET"))
  	{
    	/*  this.setFirstName(null);
    	  this.setMiddleName(null);
    	  this.setLastName(null);
    	  this.setTitle(null);
    	  this.setConfirmPassword(null);
    	  this.setFax(null);
    	  this.setMainContactLastName(null);
    	  this.setPassword(null);
    	  this.setPersonType(null);
    	  this.setPhone(null);
    	  this.setPhoneExtension(null);
    	  this.setSubmitFlag("F");
    	  this.setUserName(null); */
    	  
    	  this.setPassword(null);
    	  this.setConfirmPassword(null);
    	  
    	  return null;
  	}
      
      if (!mapping.getPath().equalsIgnoreCase("/redisplayRegistration"))
      {
      
          //loggedIn = (String) request.getSession().getAttribute("loggedIn");
          
          //if (loggedIn == null) loggedIn = new String("false");
          
          if (this.status.equalsIgnoreCase("CREATE"))
          //if (!UserContextService.isUserAuthenticated())          
          //if (loggedIn.equalsIgnoreCase("false")) 
            {
        	  if ((!userName.contains("@")) || (!userName.contains("."))) {
              	errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.invalid","Email")); 
              }
        	  
        	  	if (account == null || account.equals("")) 
                {
                    errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.required","Account Number"));
                }
                
                if (account != null)
                {
                    if (isNumber(account) == false)
                    {
                        errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.accountnumfmt","Account Number"));  
                    }
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
                    errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.invalidPassword","Confirm Password"));
                } 
                if (password != null && !password.equals(confirmPassword)) 
                {
                        errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.unmatchedPassword","Password", "Confirm Password"));
                }
                              	
            } 
	      /*  if (!WebUtils.isUSAscii(account)){
	              errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.ascii","Account"));                	
	        } 
	        if (!WebUtils.isUSAscii(mainContactLastName)){
	              errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.ascii","Primary Contact First Name"));                	
	        } 

	        if (!WebUtils.isUSAscii(firstName)){
	              errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.ascii","First Name"));                	
	        } 
	        if (!WebUtils.isUSAscii(middleName)){
	              errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.ascii","Middle Name"));                	
	        } 
	        if (!WebUtils.isUSAscii(lastName)){
	              errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.ascii","Last Name"));                	
	        }
	          
	        if (!WebUtils.isUSAscii(userName)){
	              errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.ascii","Email"));                	
	        }
	        if (!WebUtils.isUSAscii(password)){
	              errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.ascii","Password"));
	  	    } 
	        if (!WebUtils.isUSAscii(confirmPassword)){
	              errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.ascii","Confirm Password"));
	  	    }
            if (!WebUtils.isUSAscii(phoneExtension)) {
            	errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.ascii","Phone Extension"));
            }
            if (!WebUtils.isUSAscii(fax)) {
            	errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.ascii","Fax"));
            } */
        }
        return errors;
      
  }
   
   public void setRegisteredRlnkUser(boolean registeredRlnkUser) {
		this.registeredRlnkUser = registeredRlnkUser;
	}

	public boolean getIsRegisteredRlnkUser() {
		return registeredRlnkUser;
	}
	
	public String getTaxId() {
		return taxId;
	}

	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}
  
}