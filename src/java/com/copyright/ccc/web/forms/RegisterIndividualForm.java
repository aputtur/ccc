package com.copyright.ccc.web.forms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.copyright.ccc.business.data.CCCState;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.business.services.user.User;
import com.copyright.ccc.web.util.WebUtils;
import com.copyright.svc.ldapuser.api.LdapUserService;
import com.copyright.svc.ldapuser.api.data.LdapUserConsumerContext;
import com.copyright.svc.rlUser.api.RLUserServicesInterface;
import com.copyright.svc.rlUser.api.data.RlnkUser;
import com.copyright.svc.tf.api.TFService;
import com.copyright.svc.tf.api.data.Country;
import com.copyright.svc.tf.api.data.Province;
import com.copyright.svc.tf.api.data.State;
import com.copyright.svc.tf.api.data.TFConsumerContext;

public class RegisterIndividualForm extends BaseRegisterForm
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
  private String firstName;
  private String middleName;
  private String lastName;
  private String title;
  private String jobTitle;
  private String userName;
  private String password;
  private String confirmPassword;
  private String address1;
  private String address2;
  private String address3;
  private String zipcode;
  private String zipPlus4;
  private String city;
  private String state;
  private String phone;
  private String phoneExtension;
  private String fax;
  private String country = "US"; //default
  private String personType;
  private String status = "CREATE";
  private String mailingPersonPartyId = "";
  private String zipCodeChanged = "F";
  private String submitFlag = "F";
  private String isLoggedIn = "FALSE";
  private String taxId;
  private boolean registeredRlnkUser=false;
  
  private List<Country> countryList;
  private List<CCCState> stateList;
  private List<CCCState> provinceList;
  
  
  private String account = "N/A";
  private String mainContactLastName;
  
  public RegisterIndividualForm()
  {
    super();
  }
      
   
  public void setRegisterIndividualForm(User regUser)
    {
      //super();
         TFService tfService = ServiceLocator.getTFService();
         List<State> stateList = tfService.getAllStates(new TFConsumerContext());
         List<Province> provinceList = tfService.getAllProvinces(new TFConsumerContext());
         List<CCCState> stateList2 = new ArrayList <CCCState>() ;
         List<CCCState> provinceList2 = new ArrayList <CCCState>() ;
         
         
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
        
         State state = null;
         Province province = null;
        
           for (Iterator <State> i = stateList.iterator(); i.hasNext();)
           {          
               state = i.next();
               
               if (state != null)
               {
                    CCCState cccState = new CCCState();
                    cccState.setCode(state.getStateCode());
                    cccState.setName(state.getStateName());
               
                    stateList2.add(cccState);
                    
               }    
            }  
            
        for (Iterator <Province> j = provinceList.iterator(); j.hasNext();)
        {
            //    RLinkPublisher singlePub = (RLinkPublisher) i.next();
            
            province = j.next();
            
            if (province != null)
            {
                 CCCState cccProvince = new CCCState();
                 cccProvince.setCode(province.getProvinceCode());
                 cccProvince.setName(province.getProvinceName());
            
                 provinceList2.add(cccProvince);
                 
            }    
         } 
                     
        this.setCountryList( tfService.getAllCountries(new TFConsumerContext()) ) ;
        this.setStateList( stateList2 );
        this.setProvinceList( provinceList2 );
        
        this.title = regUser.getTitle();
        this.firstName = regUser.getFirstName();
        this.middleName = regUser.getMiddleName();
        this.lastName = regUser.getLastName();
        this.mainContactLastName = regUser.getLastName();
        //this.jobTitle = regUser.getJobTitle();
        this.phone = regUser.getPhoneNumber();
        this.phoneExtension = regUser.getExtension();
        this.fax = regUser.getFaxNumber();
        this.userName = regUser.getUsername();
        if (regUser.getMailingAddress().getCountry() != null)
        {
          this.country  = regUser.getMailingAddress().getCountry();
        }
        else
        {
          this.country = "US";   
        }
        this.address1 = regUser.getMailingAddress().getAddress1();
        this.address2 = regUser.getMailingAddress().getAddress2();
        this.address3 = regUser.getMailingAddress().getAddress3();
        this.city     = regUser.getMailingAddress().getCity();
        this.state    = regUser.getMailingAddress().getState();
        
        if (this.country.equals("US")){
        	this.zipcode  = regUser.getMailingAddress().getPostalCode() == null ? "": regUser.getMailingAddress().getPostalCode().substring(0,5);
        	this.zipPlus4 = regUser.getMailingAddress().getPostalCode() == null ? "": regUser.getMailingAddress().getPostalCode().substring(5);
        }
        else {
           	this.zipcode  = regUser.getMailingAddress().getPostalCode();       	
        }
        
        if (regUser.getAccount() != null)
        {
            if ( regUser.getAccount().getAccountNumber() != null )
            {
                this.account = regUser.getAccount().getAccountNumber();
            }
            else
            {
                this.account = "N/A";
            }
        }
      
        this.mailingPersonPartyId = regUser.getPartyId().toString();
        this.personType = regUser.getMarketSegment();
        
        //Get the Always Invoice Flag
        checkUserPreferenceSetting();
        
        
    }  
  
  public List<Country> getCountryList()
    {
      return countryList;
    }
  
  public void setCountryList(List<Country> lst)
    {
      countryList = lst;
    }
    
  public List<CCCState> getStateList()
     {
       return stateList;
     }
   
  public void setStateList(List<CCCState> lst)
     {
       stateList = lst;
     }
     
  
  public List<CCCState> getProvinceList()
     {
       return provinceList;
     }
   
  public void setProvinceList(List<CCCState> lst)
     {
       provinceList = lst;
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
    
  public String getAddress1()
    {
      return address1;
    }
  
  public void setAddress1(String addr1)
    {
      address1 = addr1;
    }
    
  public String getAddress2()
    {
      return address2;
    }
  
  public void setAddress2(String addr2)
    {
      address2 = addr2;
    }
    
  public String getAddress3()
    {
      return address3;
    }
  
  public void setAddress3(String addr3)
    {
      address3 = addr3;
    }
    
  public String getZipcode()
    {
      return zipcode;
    }
  
  public void setZipcode(String zip)
    {
      zipcode = zip;
    }
    
  public String getZipPlus4()
  {
    return zipPlus4;
  }

  public void setZipPlus4(String plus4)
  {
    zipPlus4 = plus4;
  }
  public String getCity()
    {
      return city;
    }
  
  public void setCity(String cit)
    {
      city = cit;
    }
    
  public String getState()
    {
      return state;
    }
  
  public void setState(String st)
    {
      state = st;
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
    
  public String getCountry()
    {
      return country;
    }
  
  public void setCountry(String cntry)
    {
      country = cntry;
    }
    
  public String getPersonType()
    {
      return personType;
    }
  
  public void setPersonType(String pType)
    {
      personType = pType;
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
    
  public String getZipCodeChanged()
    {
      return zipCodeChanged;
    }
    
  public void setZipCodeChanged(String zipChange)
    {
      zipCodeChanged = zipChange;
    }

  public String getSubmitFlag()
    {
      return submitFlag;
    }
    
  public void setSubmitFlag(String subFlag)
    {
      submitFlag = subFlag;
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
      
      if (!mapping.getPath().equalsIgnoreCase("/redisplayRegistration") )
      {
      
          //loggedIn = (String) request.getSession().getAttribute("loggedIn");
          
          //if (loggedIn == null) loggedIn = new String("false");
          
           // if (!UserContextService.isUserAuthenticated()) 
           // {
                
                if (this.status.equalsIgnoreCase("CREATE"))
                {
                	if ((!userName.contains("@")) || (!userName.contains("."))) {
                    	errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.invalid","Email")); 
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
                
                if (country.equalsIgnoreCase("US"))
                {
                    //  Need to add validation for zip code and state here.

                    if (zipcode == null || zipcode.equals("") )
                    {
                    	errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.required", "Zip Code"));
                    }
                   
                if (state == null || state.equals(""))
                    {
                    	errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.required", "State"));
                    }

                }
                
                if (country.equalsIgnoreCase("CA"))
                {
                	if (zipcode == null || zipcode.equals(""))
                    {
                      errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("errors.required", "Postal Code"));
                    }
                                       
                	if (state == null || state.equals(""))
                    {
                    	errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.required", "Province"));
                    }

                }
            
            /*    if (!WebUtils.isUSAscii(firstName)){
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
                if (!WebUtils.isUSAscii(address1)) {
                	errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.ascii","Address 1"));
                }
                if (!WebUtils.isUSAscii(address2)) {
                	errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.ascii","Address 2"));
                }           
                if (!WebUtils.isUSAscii(address3)) {
                	errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.ascii","Address 3"));
                }  
                if (!WebUtils.isUSAscii(city)) {
                	errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.ascii","City"));
                } 
                if (!WebUtils.isUSAscii(phoneExtension)) {
                	errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.ascii","Phone Extension"));
                }
                if (!WebUtils.isUSAscii(fax)) {
                	errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.ascii","Fax"));
                } 
                if (!WebUtils.isUSAscii(userName)){
                    errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.ascii","Email"));                	
                } */
            
           // }      
        }
        return errors;
      
  }


public void setTaxId(String taxId) {
	this.taxId = taxId;
}


public String getTaxId() {
	return taxId;
}
public void setRegisteredRlnkUser(boolean registeredRlnkUser) {
	this.registeredRlnkUser = registeredRlnkUser;
}

public boolean getIsRegisteredRlnkUser() {
	return registeredRlnkUser;
}
  
}