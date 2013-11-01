package com.copyright.ccc.web.forms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

public class RegisterOrganizationForm extends BaseRegisterForm
{

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

  
  private String status = "CREATE";
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
  private String department;
  private String zipcode;
  private String zipPlus4;
  private String city;
  private String state;
  private String phone;
  private String phoneExtension;
  private String fax;
  private String country = "US"; //default
  private String personType;
  private String company;
  private boolean registeredRlnkUser=false;
  
    
  private String billingFirstName;
  private String billingMiddleName;
  private String billingLastName;
  private String billingTitle;
  private String billingJobTitle;
  private String billingAddress1;
  private String billingAddress2;
  private String billingAddress3;
  private String billingDepartment;
  private String billingZipcode;
  private String billingZipPlus4;
  private String billingCity;
  private String billingState;
  private String billingPhone;
  private String billingPhoneExtension;
  private String billingFax;
  private String billingCountry = "US"; //default
  private String billingEmail ;
  private String sameBilling = "F";
  private String mailingPersonPartyId = "";
  private String billingPersonPartyId = "";
  private String sameBillingFlag = "F";
  private String zipCodeChanged = "F";
  private String submitFlag = "F";
  private String isLoggedIn = "FALSE";
  
  private List<Country> countryList;
  private List<CCCState> stateList;
  private List<CCCState> provinceList;
  
  private Map<String,String> industryList;
  private Map<String,String> orgStatusList;
  private String industryType;
  private String orgStatus;
  private String taxId;
  
  private String account = "N/A";
  private String mainContactLastName;
  
  
  public RegisterOrganizationForm()
  {
    super();
  }
  
  public RegisterOrganizationForm(User regUser)
    {
      super();
         TFService tfService = ServiceLocator.getTFService();
         RLUserServicesInterface rlUserService= ServiceLocator.getRLUserService();
         LdapUserService ldapService = ServiceLocator.getLdapUserService();
         regUser.setLdapUser(ldapService.getUser(new LdapUserConsumerContext(), regUser.getUsername()));
         // check for rightslink user         
         String rightslnkPartyId=regUser.getLdapUser().getRightsLinkPartyID();
         if(StringUtils.isNotEmpty(rightslnkPartyId)){
        		 RlnkUser rlnkUser=rlUserService.getUserByUserName(regUser.getUsername());
                	 this.registeredRlnkUser=Boolean.TRUE;
                	 this.setIndustryType(rlnkUser.getIndustry());
                	 this.setOrgStatus(rlnkUser.getOrgStatus());
                	 this.setTaxId(rlnkUser.getTaxId());
                	 initIndustryList();
                     initOrgStatusList();
         }
         
        
         List<State> stateList = tfService.getAllStates(new TFConsumerContext() );
         List<Province> provinceList = tfService.getAllProvinces(new TFConsumerContext());
         List<CCCState> stateList2 = new ArrayList <CCCState>() ;
         List<CCCState> provinceList2 = new ArrayList <CCCState>() ;
        
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
      this.jobTitle = regUser.getJobTitle();
      
      if (regUser.getOrganization() != null)
      {
        this.company = regUser.getOrganization().getOrganizationName();
      }
      else
      {
          this.company = "";
      }
      
      this.department = regUser.getDepartment();
      this.phone = regUser.getPhoneNumber();
      this.phoneExtension = regUser.getExtension();
      this.fax = regUser.getFaxNumber();
      
      if (regUser.getMailingAddress().getCountry() != null)
      {
        this.country  = regUser.getMailingAddress().getCountry();
      }
      else
      {
          this.country = "US";
      }
            
      this.userName = regUser.getUsername();
      this.address1 = regUser.getMailingAddress().getAddress1();
      this.address2 = regUser.getMailingAddress().getAddress2();
      this.address3 = regUser.getMailingAddress().getAddress3();
      this.city     = regUser.getMailingAddress().getCity();
      this.state    = regUser.getMailingAddress().getState();
      if (this.country.equals("US")){
    	  this.zipcode = regUser.getMailingAddress().getPostalCode() == null ? "": regUser.getMailingAddress().getPostalCode().substring(0, 5);
    	  this.zipPlus4 = regUser.getMailingAddress().getPostalCode() == null ? "": regUser.getMailingAddress().getPostalCode().substring(5);
      }
      else {
    	  this.zipcode  = regUser.getMailingAddress().getPostalCode();
      }
      

      this.mailingPersonPartyId = regUser.getPartyId().toString();
      this.personType = regUser.getMarketSegment();
      
      if (regUser.getAccount() != null)        
      {
      
            if (regUser.getOrganization() != null)
            {
                this.company = regUser.getOrganization().getOrganizationName();
            }
            else
            {
                this.company = "";
            }
            
            if ( regUser.getAccount().getAccountNumber() != null)
            {
                this.account = regUser.getAccount().getAccountNumber();
            }
            else
            {
                this.account = "N/A";
            }
      
            if (regUser.getAccount().getName() != null)
            {
                //Billing Contact Data
                //this.billingTitle = regUser.getAccount().getName().getPrefix();
            	this.billingTitle = regUser.getBillingPrefix();
                this.billingFirstName = regUser.getAccount().getName().getFirstName();
                this.billingMiddleName = regUser.getAccount().getName().getMiddleName();
                this.billingLastName = regUser.getAccount().getName().getLastName();
                this.billingJobTitle = regUser.getBillingJobTitle();
                this.billingDepartment = regUser.getBillingDepartment();
                
                if (regUser.getBillingAddress().getCountry() != null)
                {
                    this.billingCountry = regUser.getBillingAddress().getCountry();
                }
                else
                {
                    this.billingCountry = "US";
                }
                
                this.billingAddress1 = regUser.getBillingAddress().getAddress1();
                this.billingAddress2 = regUser.getBillingAddress().getAddress2();
                this.billingAddress3 = regUser.getBillingAddress().getAddress3();
                this.billingCity = regUser.getBillingAddress().getCity();
                this.billingState = regUser.getBillingAddress().getState();
                
                if (this.billingCountry.equals("US")){
                	this.billingZipcode = regUser.getBillingAddress().getPostalCode() == null ? "" : regUser.getBillingAddress().getPostalCode().substring(0,5);
                	this.billingZipPlus4 = regUser.getBillingAddress().getPostalCode() == null ? "" : regUser.getBillingAddress().getPostalCode().substring(5);
                }
                else {
                	this.billingZipcode = regUser.getBillingAddress().getPostalCode();
                }
                
                
                                
                this.billingPhone = regUser.getBillingPhoneNumber();
                this.billingPhoneExtension = regUser.getBillingExtension();
                
                this.billingFax = regUser.getBillingFaxNumber();
                                               
                if (regUser.getBillingEmailAddress() != null)
                {
                    this.billingEmail = regUser.getBillingEmailAddress().getAddress();
                }
                else
                {
                    this.billingEmail = "";
                }
                this.billingPersonPartyId = regUser.getBillingPartyId().toString();
                
            }
            else
            {
                this.billingTitle = this.title;
                this.billingFirstName = this.firstName;
                this.billingMiddleName = this.middleName;
                this.billingLastName = this.lastName;
                this.billingJobTitle = this.jobTitle;
                this.billingDepartment = this.department;
                this.billingAddress1 = this.address1;
                this.billingAddress2 = this.address2;
                this.billingAddress3 = this.address3;
                this.billingCity = this.city;
                this.billingState = this.state;
                if (this.country.equals("US")) {
                	this.billingZipcode = this.zipcode == null ? "" : this.zipcode.substring(0,5);
                	this.billingZipPlus4 = this.zipcode == null ? "" : this.zipcode.substring(5);
                }
                else {
                	this.billingZipcode = this.zipcode;
                }
                this.billingCountry = this.country;
                this.billingPhone = this.phone;
                this.billingPhoneExtension = this.phoneExtension;
                this.billingFax = this.fax;
                this.billingEmail = this.userName;
                this.billingPersonPartyId = this.mailingPersonPartyId;
            }
      }
      else
      {
          this.billingTitle = this.title;
          this.billingFirstName = this.firstName;
          this.billingMiddleName = this.middleName;
          this.billingLastName = this.lastName;
          this.billingJobTitle = this.jobTitle;
          this.billingDepartment = this.department;
          this.billingAddress1 = this.address1;
          this.billingAddress2 = this.address2;
          this.billingAddress3 = this.address3;
          this.billingCity = this.city;
          this.billingState = this.state;
          if (this.country.equals("US")) {
          	this.billingZipcode = this.zipcode == null ? "" : this.zipcode.substring(0,5);
          	this.billingZipPlus4 = this.zipcode == null ? "" : this.zipcode.substring(5);
          }
          else {
          	this.billingZipcode = this.zipcode;
          }
          this.billingCountry = this.country;
          this.billingPhone = this.phone;
          this.billingPhoneExtension = this.phoneExtension;
          this.billingFax = this.fax;
          this.billingEmail = this.userName;
          this.billingPersonPartyId = this.mailingPersonPartyId;
          
      }
                
        if (mailingPersonPartyId.equalsIgnoreCase(billingPersonPartyId))      
        {
              //logger.info("Mail and Bill are same.....");
              this.sameBilling = RegisterOrganizationForm.TRUE;
              this.sameBillingFlag = RegisterOrganizationForm.TRUE;
              
        }
      else
        {
              //logger.info("Mail and Bill are different.....");
              this.sameBilling = RegisterOrganizationForm.FALSE;
              this.sameBillingFlag = RegisterOrganizationForm.FALSE;
        }
        
        checkUserPreferenceSetting();
        
        
      
    }
  
  
  public Map<String, String> getIndustryList() {
	  if(industryList==null){
		  industryList=new HashMap<String,String>();  
	  }
		
		return industryList;
	}

	public void setIndustryList(Map<String, String> industryList) {
		this.industryList = industryList;
	}

	public Map<String, String> getOrgStatusList() {
		  if(orgStatusList==null){
			  orgStatusList=new HashMap<String,String>();  
		  }
		return orgStatusList;
	}

	public void setOrgStatusList(Map<String, String> orgStatusList) {
		this.orgStatusList = orgStatusList;
	}

	public String getIndustryType() {
		return industryType;
	}

	public void setIndustryType(String industryType) {
		this.industryType = industryType;
	}

	public String getOrgStatus() {
		return orgStatus;
	}

	public void setOrgStatus(String orgStatus) {
		this.orgStatus = orgStatus;
	}

	public String getTaxId() {
		return taxId;
	}

	public void setTaxId(String taxId) {
		this.taxId = taxId;
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
    
  public String getJobTitle()
    {
      return jobTitle ;
    }
    
  public void setJobTitle(String jobttl)
    {
      jobTitle = jobttl ;
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
      return userName == null ? "": userName.trim();
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
    
  public String getDepartment()
    {
      return department;
    }
  
  public void setDepartment(String dept)
    {
      department = dept;
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

  public String getBillingPersonPartyId()
    {
      return billingPersonPartyId;
    }
  
  public void setBillingPersonPartyId(String billPartyId)
    {
      billingPersonPartyId = billPartyId;
    }
    
  //*******************************************************************************
  
  public String getBillingFirstName()
    {
      return billingFirstName;
    }
  
  public void setBillingFirstName(String fName)
    {
      billingFirstName = fName;
    }
    
  public String getBillingMiddleName()
    {
      return billingMiddleName;
    }
  
  public void setBillingMiddleName(String mName)
    {
      billingMiddleName = mName;
    }
    
  public String getBillingLastName()
    {
      return billingLastName;
    }
  
  public void setBillingLastName(String lName)
    {
      billingLastName = lName;
    }
    
  public String getBillingTitle()
    {
      return billingTitle;
    }
  
  public void setBillingTitle(String ttl)
    {
      billingTitle = ttl;
    }
    
  public String getBillingJobTitle()
    {
      return billingJobTitle ;
    }
    
  public void setBillingJobTitle(String jobttl)
    {
      billingJobTitle = jobttl ;
    }
    
      
  public String getBillingAddress1()
    {
      return billingAddress1;
    }
  
  public void setBillingAddress1(String addr1)
    {
      billingAddress1 = addr1;
    }
    
  public String getBillingAddress2()
    {
      return billingAddress2;
    }
  
  public void setBillingAddress2(String addr2)
    {
      billingAddress2 = addr2;
    }
    
  public String getBillingAddress3()
    {
      return billingAddress3;
    }
  
  public void setBillingAddress3(String addr3)
    {
      billingAddress3 = addr3;
    }
    
  public String getBillingZipcode()
    {
      return billingZipcode;
    }
  
  public void setBillingZipcode(String zip)
    {
      billingZipcode = zip;
    }
    
  public String getBillingZipPlus4()
  {
    return billingZipPlus4;
  }

  public void setBillingZipPlus4(String plus4)
  {
    billingZipPlus4 = plus4;
  }
 
  public String getBillingCity()
    {
      return billingCity;
    }
  
  public void setBillingCity(String cit)
    {
      billingCity = cit ;
    }
    
  public String getBillingState()
    {
      return billingState;
    }
  
  public void setBillingState(String st)
    {
      billingState = st;
    }
    
  public String getBillingPhone()
    {
      return billingPhone;
    }
  
  public void setBillingPhone(String phn)
    {
      billingPhone = phn;
    }
    
  public String getBillingPhoneExtension()
    {
      return billingPhoneExtension;
    }
  
  public void setBillingPhoneExtension(String billPhnExt)
    {
      billingPhoneExtension = billPhnExt;
    }
    
  public String getBillingFax()
    {
      return billingFax;
    }
  
  public void setBillingFax(String fx)
    {
      billingFax = fx;
    }
    
  public String getBillingCountry()
    {
      return billingCountry;
    }
  
  public void setBillingCountry(String cntry)
    {
      billingCountry = cntry;
    }
    
  public String getBillingEmail()
    {
      return billingEmail;
    }
  
  public void setBillingEmail(String email)
    {
      billingEmail = email;
    }
    
  public String getCompany()
    {
      
      return company ;
    }
    
  public void setCompany(String comp)
    {
      company = comp ;
    }
    
  public String getBillingDepartment()
    {
      return billingDepartment;
    }
  
  public void setBillingDepartment(String billDept)
    {
      billingDepartment = billDept;
    }
    
  public String getSameBilling()
    {
      return sameBilling;
    }
  
  public void setSameBilling(String sameBill)
    {
      sameBilling = sameBill;
    }
    
  public String getSameBillingFlag()
    {
      return sameBillingFlag;
    }
  
  public void setSameBillingFlag(String sameBillFlag)
    {
      sameBillingFlag = sameBillFlag;
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

  //*****************************************************************************

  @Override
  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
  {
      ActionErrors errors = super.validate(mapping, request);
      //ActionErrors errors = new ActionErrors() ;
     // String    loggedIn = (String) request.getSession().getAttribute("loggedIn");
//      if (loggedIn == null) loggedIn = new String("false");
      
      if (!mapping.getPath().equalsIgnoreCase("/redisplayRegistration") )
       {  
           // if (!UserContextService.isUserAuthenticated())
           // {
            if (this.status.equalsIgnoreCase("CREATE"))
            {
            	if ((!userName.contains("@")) || (!userName.contains("."))) {
                	errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.invalid","Email")); 
                }
            	
            	if (password == null || password.equals("")) 
                {
                    errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("errors.required","Password"));
                } 
                else if (password.length() < 6 )   //|| !containsNumeric(password))
                {
                    errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("errors.invalidPassword","Password"));
                }
                if (confirmPassword == null || confirmPassword.equals("")) 
                {
                    errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("errors.required","Re-enter Password"));
                } 
                else if (confirmPassword.length() < 6) // || !containsNumeric(confirmPassword))
                {
                    errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("errors.invalidPassword","Re-enter Password"));
                } 
                if (password != null && !password.equals(confirmPassword)) 
                {
                        errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("errors.unmatchedPassword","Password", "Re-enter Password"));
                }
                //Validate Zip Code/Postal Code for USA and CANADA
                if (country.equalsIgnoreCase("US"))
                {
                  if ( zipcode == null || zipcode.equals(""))
                  {
                    errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("errors.required", "Zip Code"));
                  }
		  
                  if (state == null || state.equals(""))
                  {
                    errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("errors.required", "State"));
                  }
                
                }
                if (billingCountry.equalsIgnoreCase("US"))
                {
                //  Need to add validation for Billing zip code and state here.
                  if ( billingZipcode == null || billingZipcode.equals(""))
                  {
                    errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("errors.required", "Billing Zip Code"));
                  }

                  if ( billingState == null || billingState.equals(""))
                  {
                    errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("errors.required", "Billing State"));
                    
                  }
                
                }
                
                if (country.equalsIgnoreCase("CA"))
            	{
                	if (zipcode == null || zipcode.equals(""))
                    {
                      errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("errors.required", "Postal Code"));
                    }
                	
                  //  Need to add validation for province here.
                  
                  if (state == null || state.equals(""))
                  {
                    errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("errors.required", "Province"));
                  }
            	}

            	if (billingCountry.equalsIgnoreCase("CA"))
            	{
            		if ( billingZipcode == null || billingZipcode.equals(""))
                    {
                      errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("errors.required", "Billing Postal Code"));
                    }
                  //  Need to add validation for province here.
                  if (billingState == null || billingState.equals(""))
                  {
                    errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("errors.required", "Billing Province"));
                  }
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
            if (!WebUtils.isUSAscii(jobTitle)) {
            	errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.ascii","Job Title"));
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
            if (!WebUtils.isUSAscii(department)) {
            	errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.ascii","Department"));
            }              
            if (!WebUtils.isUSAscii(city)) {
            	errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.ascii","City"));
            } 
            if (!WebUtils.isUSAscii(phone)) {
            	errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.ascii","Phone"));
            }
            if (!WebUtils.isUSAscii(phoneExtension)) {
            	errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.ascii","Phone Extension"));
            }
            if (!WebUtils.isUSAscii(fax)) {
            	errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.ascii","Fax"));
            }   
            if (!WebUtils.isUSAscii(company)) {
            	errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.ascii","Company"));
            } 

            
            if (!WebUtils.isUSAscii(billingFirstName)) {
            	errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.ascii","Billing First Name"));
            } 
            if (!WebUtils.isUSAscii(billingMiddleName)) {
            	errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.ascii","Billing Middle Name"));
            } 
            if (!WebUtils.isUSAscii(billingLastName)) {
            	errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.ascii","Billing Last Name"));
            }
            if (!WebUtils.isUSAscii(billingJobTitle)) {
            	errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.ascii","Billing Job Title"));
            }

            if (!WebUtils.isUSAscii(billingAddress1)) {
            	errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.ascii","Billing Address 1"));
            }
            if (!WebUtils.isUSAscii(billingAddress2)) {
            	errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.ascii","Billing Address 2"));
            }           
            if (!WebUtils.isUSAscii(billingAddress3)) {
            	errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.ascii","Billing Address 3"));
            } 
            if (!WebUtils.isUSAscii(billingDepartment)) {
            	errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.ascii","Billing Department"));
            } 
            if (!WebUtils.isUSAscii(billingCity)) {
            	errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.ascii","Billing City"));
            } 

            if (!WebUtils.isUSAscii(billingPhoneExtension)) {
            	errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.ascii","Billing Phone Extension"));
            } 
            if (!WebUtils.isUSAscii(billingEmail)) {
            	errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.ascii","Billing Email"));
            } */
      }
      return errors;
      
  }

   private void initIndustryList(){
	   //getIndustryList().put("NOTSELECTED", "Make selection");
	     getIndustryList().clear();
	   	 getIndustryList().put("5","Agriculture, Forestry and Fisheries");
	   	 getIndustryList().put("10","Broadcasting");
	     getIndustryList().put("15","Colleges, Universities and other Educational Services");
	     getIndustryList().put("20","Construction");
	     getIndustryList().put("25","Finance, Insurance and Real Estate");
	     getIndustryList().put("30","Government");
	     getIndustryList().put("35","Manufacturing");
	     getIndustryList().put("40","Mining");
	     getIndustryList().put("43","Pharmaceutical");
	     getIndustryList().put("45","Professional, Scientific and Technical Services");
	     getIndustryList().put("50","Public Administration");
	     getIndustryList().put("55","Publishing");
	     getIndustryList().put("60","Retail Trade");
	     getIndustryList().put("65","Services");
	     getIndustryList().put("70","Telecommunications");
	     getIndustryList().put("75","Transportation and Public Utilities");
	     getIndustryList().put("80","Warehousing");
	     getIndustryList().put("85","Wholesale Trade");
	     getIndustryList().put("90","All other");
   }
 private void initOrgStatusList(){
	 getOrgStatusList().clear();
	 getOrgStatusList().put("FORPROFIT","for profit");
	 getOrgStatusList().put("NONPROFIT","non-profit §501(c)(3) US only");
	 getOrgStatusList().put("NONPROFIT_NONUS","non-profit non-US");
   }

public void setRegisteredRlnkUser(boolean registeredRlnkUser) {
	this.registeredRlnkUser = registeredRlnkUser;
}

public boolean getIsRegisteredRlnkUser() {
	return registeredRlnkUser;
}
}