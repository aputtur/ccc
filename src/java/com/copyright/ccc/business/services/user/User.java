package com.copyright.ccc.business.services.user;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.mail.internet.InternetAddress;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.business.services.SystemStatus;
import com.copyright.ccc.util.LogUtil;
import com.copyright.ccc.web.util.WebUtils;
import com.copyright.data.security.Privilege;
import com.copyright.svc.ldapuser.api.LdapUserService;
import com.copyright.svc.ldapuser.api.data.LdapUser;
import com.copyright.svc.ldapuser.api.data.LdapUserConsumerContext;
import com.copyright.svc.telesales.api.TelesalesService;
import com.copyright.svc.telesales.api.data.ARAccount;
import com.copyright.svc.telesales.api.data.Classification;
import com.copyright.svc.telesales.api.data.ContactPoint;
import com.copyright.svc.telesales.api.data.ContactPointTypeEnum;
import com.copyright.svc.telesales.api.data.CustomerCategoryEnum;
import com.copyright.svc.telesales.api.data.Location;
import com.copyright.svc.telesales.api.data.MarketSegmentEnum;
import com.copyright.svc.telesales.api.data.Organization;
import com.copyright.svc.telesales.api.data.PartySite;
import com.copyright.svc.telesales.api.data.PartySiteUse;
import com.copyright.svc.telesales.api.data.Person;
import com.copyright.svc.telesales.api.data.PhoneLineTypeEnum;
import com.copyright.svc.telesales.api.data.TelesalesServiceConsumerContext;
import com.copyright.svc.tf.api.TFService;
import com.copyright.svc.tf.api.data.ArAccountInfo;
import com.copyright.svc.tf.api.data.Party;
import com.copyright.svc.tf.api.data.TFConsumerContext;

/**
 * Replacement class for the old shared services shared user.
 * In reality this simply acts as a wrapper for the new tf/telesales/ldap
 * objects.  Hoping to replicate the structure of the original shared
 * user object to minimize changes to the rest of the app.
 *
 ************************************************************************
 * Changes can go here.  Please list most recent changes at the TOP of
 * the list.  That makes it easier after the code has been around for a
 * number of years.
 *
 * when         who         what
 * ----------   ----------  ---------------------------------------------
 * 2009-07-21   m.s.jessop  Created this class.
 */
public final class User implements com.copyright.workbench.security.User,Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//  Logging within what should be a simple data container is not
    //  really something I like, but in this case I think I will add
    //  this to log bad email addresses coming out of telesales.
    
    private static Logger _logger = Logger.getLogger(User.class);
    
    //  The problem with the shared user is the data is also shared
    //  from about 10,000,000,000,000 places - even if some of the
    //  data maps back to the same place.  Our primary focus and most
    //  commonly tapped object will be the LdapUser object.  It
    //  contains the basic ingredients for a typical CCC user.  It
    //  has the username/email address/password and partyId.  We
    //  will rarely need more than that to get by.  The Party object
    //  holds the TF information... it doesn't have much more data
    //  than the LdapUser but it could be useful.  The Person contains
    //  more data regarding the logged in user, including mailto
    //  addresses, contact points, etc.  Organization just ties us
    //  into our ARAccount, which contains our billto information.
    //  Our roles and privileges come from shared services still.
    //  Finally, dates for password reset requests...
    
    private LdapUser ldapuser;
    
    private Party party;
    private Party orgParty;
    private ArAccountInfo accountInfo;
    
    private Person person;
    private Person orgPerson;
    //private Person billPerson;
    private Organization organization;
    private ARAccount account;
    
    private Privilege[] privileges;
    
    private Date forgotPasswordRequestTime;
    private String forgotPasswordResponseCode;
    private String channel;
    
    private Location mailTo;
    private Location billTo;
    
    //  I reckon this is used during the reset password function...
    
    public static final long DEFAULT_FORGOT_PASSWORD_REQUEST_EXPIRATION_TIME =
        86400000L;
        
    //  Other constants.
    
    private static final int MAX_REFCOUNT = 20;
        
    //  Our constructors do not have to change, really.  Our new objects do not
    //  hold email addresses as InternetAddress (for example), but the class is
    //  useful for checking the passed in value to make sure it is really an
    //  email address.  I am adding constructors to make some of our job easier.
    
    public User()
    {
        this.ldapuser = null;
        this.party = null;
        this.orgParty = null;
        this.person = null;
        this.orgPerson = null;
        this.organization = null;
        this.account = null;
        this.privileges = null;
        this.forgotPasswordRequestTime = null;
        this.forgotPasswordResponseCode = null;
        this.mailTo = null;
        this.billTo = null;
        this.channel = null;
    }
    
    private TelesalesService getTelesalesService() {
            return  ServiceLocator.getTelesalesService();
    }
    
    private TFService getTFService() {
          	return  ServiceLocator.getTFService();
           
    }
    
    private LdapUserService getLDAPService() {
         return ServiceLocator.getLdapUserService();
         
    }
    
    
    
    private void loadPersonData() {
        //  We've decided to load all of our person data
        //  in one fell swoop.
        
        ARAccount tmp = null;
        List<ARAccount> accounts = null;
        
        try
        {
            if (this.getTelesalesIsUp()) 
            {
                String partyId = (this.ldapuser.getPartyID() == null) ? 
                        this.ldapuser.getRightsLinkPartyID() : this.ldapuser.getPartyID();
                
                if (partyId != null && Long.parseLong(partyId) > 0)
                {
                	this.person = getTelesalesService()
                    	.getPersonByPartyId(new TelesalesServiceConsumerContext(), new Long(partyId));
                                                
                	Party pty = getTFService().getIndLicenseePartyByPartyId(new TFConsumerContext(), 
                																	new Long(partyId));
                
                	this.organization = getTelesalesService()
                    	.getOrgByARAccountNumber(new TelesalesServiceConsumerContext(), 
                    			pty.getAccountNumber().toString()); 
                                                                
                	this.orgPerson = getTelesalesService()
                		.getPersonByPartyId(new TelesalesServiceConsumerContext(), this.person.getPartyId());
                
                	accounts = getTelesalesService()
                		.getARAccountByPartyId(new TelesalesServiceConsumerContext(), 
                				this.person.getPartyId(), this.person.getUserType());
                                                                       
                //  Accounts... yeah.  We just want one I think, yet we get a list
                //  back of who knows how many accounts?
            
                	Iterator<ARAccount> i = accounts.iterator();
            
                	while (i.hasNext()) {
                		tmp = i.next();
                		if (tmp.getAccountNumber().equals(pty.getAccountNumber().toString())) {
                        //  If the account numbers match, this should be a winner.
                    
                        this.account = tmp;
                        break;
                    }
                  }
                }
            }
            else {
                _logger.warn("User :: TELESALES SERVICE IS DOWN!");
            }
        }
        catch(Exception e) {
            _logger.warn("User :: Unable to load person data." + LogUtil.appendableStack(e));
        }
    }
    
    private void loadPartyData() {
        //  We've decided to load all of our party data
        //  in one fell swoop.
        
        try {
            String partyId = (this.ldapuser.getPartyID() == null) ? 
                    this.ldapuser.getRightsLinkPartyID() : this.ldapuser.getPartyID();
            if (partyId != null && Long.parseLong(partyId) > 0)
            {
            	party = getTFService().getIndLicenseePartyByPartyId(new TFConsumerContext(), 
            																	new Long(partyId));
            	orgParty = getTFService().getOrgLicenseePartyByAccountNumber(new TFConsumerContext(), 
            															this.party.getAccountNumber());
            }
        }
        catch(Exception e) {
            _logger.error("User :: Unable to load party and org. party data." + LogUtil.appendableStack(e));
        }
    }
    
   /* private void loadBillToData() {
        
        try {
            this.billPerson = getTelesalesService().getBillToPersonByArAccountNumber(new TelesalesServiceConsumerContext(), this.getArAccountNumber(), this.person.getUserType());
            }
        catch(Exception e) {
            _logger.warn("User :: Unable to load Bill To Person data", e);
        }
    } */
    
    private void buildMailToData() {
        PartySite site = null;
        PartySiteUse use = null;
        //List<PartySite> sites = null;
        List<PartySiteUse> uses = null;
        
        if (this.person == null) loadPersonData();
        
        if (this.person != null)
        {
        	List<PartySite> sites = this.person.getPartySites();
        
        	Iterator<PartySite> i = sites.iterator();
        	Iterator<PartySiteUse> j = null;
        
        	done: while (i.hasNext()) {
        		site = i.next();
        		uses = site.getUses();
        		j = uses.iterator();
        		while (j.hasNext()) {
        			use = j.next();
        			if (use.getUse().toString().equalsIgnoreCase("MailTo")) {
                    //  I am not convinced this is enough.  Should I be
                    //  checking the primaryFlag method?
                    
        				this.mailTo = site.getLocation();
        				break done;
                }
            }
          }
        }
    }
    
    private void buildBillToData() {
        if (this.person == null) loadPersonData();
        if (this.account != null)
        {
        	this.billTo = this.account.getLocation();
        }
    }
    
    public void loadRegistrationData() {
        loadPersonData();
        loadPartyData();
        buildMailToData();
        //buildBillToData();
    }
    
    public void refreshBasicData() {
        try {
            this.ldapuser = getLDAPService().getUser(new LdapUserConsumerContext(), this.ldapuser.getUsername());
            long ccPartyId = NumberUtils.toLong( this.ldapuser.getPartyID() );
            long rlPartyId = NumberUtils.toLong( this.ldapuser.getRightsLinkPartyID() );
            if ( ccPartyId > 0 || rlPartyId > 0 ) 
            {
                loadPartyData();
                
                long partyId = (ccPartyId > 0)? ccPartyId : rlPartyId;
                        
                party = getTFService().getIndLicenseePartyByPartyId(new TFConsumerContext(), partyId);
                this.accountInfo = getTFService().getArAccountInfoByAccount(
                    new TFConsumerContext(), party.getAccountNumber());
            }
        }
        catch(Exception e) {
        	// Let's log this for at least a little while, to see what kinds of 
        	// exceptions are being eaten. [dstine 8/6/10]
            _logger.warn("User :: Unable to refresh basic data." + LogUtil.appendableStack(e));
        }
    }
    
    //  Begin with our constructive methods.  I am hoping to use these instead
    //  of relying on constructors to populate the object data.
    
    public void setParty(Party party)           { this.party = party; }
    public void setAccountInfo(ArAccountInfo accountInfo) {
        this.accountInfo = accountInfo;
    }
    public void setLdapUser(LdapUser ldapuser)  { this.ldapuser = ldapuser; }
    public void setPerson(Person person)        { this.person = person; }
    public void setAccount(ARAccount account) { this.account = account; }
    public void setOrganization(Organization organization) { 
        this.organization = organization; 
    }
    public void setPrivileges(Privilege[] privileges) {
        this.privileges = privileges;
    }
    
    public Party getParty()               { return this.party; }
    public Party getOrgParty()			  { return this.orgParty; }
    public LdapUser getLdapUser()         { return this.ldapuser; }
    public ArAccountInfo getAccountInfo() { return this.accountInfo; }
    public Person getPerson()             { return this.person; }
    public Person getOrgPerson()          { return this.orgPerson; }
    public ARAccount getAccount()         { return this.account; }
    public Organization getOrganization() { return this.organization; }
    public Privilege[] getPrivileges()    { return this.privileges; }
           
    //  ********************************************************************
    //  Password stuff is a little special, does not necessarily tie back to
    //  our user objects.  This is pretty much copied from the shared user.
    
    /**
     * @return timestamp when user requested for password reset
     */
    public Date getForgottenPasswordRequestTime()
    {
        return this.forgotPasswordRequestTime;
    }
    
    /**
     * sets the password reset request timestamp
     * @param d timestamp when user requested for password reset 
     */
    public void setForgottenPasswordRequestTime(Date d)
    {
        this.forgotPasswordRequestTime = (Date) d.clone();
    }
    
    /**
     * @return true if the password request is expired based
     *         on the default duration
     */
    public boolean isForgottenPasswordRequestExpired()
    {
        return this.isForgottenPasswordRequestExpired(
                DEFAULT_FORGOT_PASSWORD_REQUEST_EXPIRATION_TIME);
    }
    
    /**
     * @param duration expiration duration in milliseconds
     * @return true if the password request is expired after
     *         <code>duration</code> milliseconds since the
     *         request
     */
    public boolean isForgottenPasswordRequestExpired(long duration)
    {
        if (null != this.forgotPasswordRequestTime)
        {
            Date expTime = new Date(this.forgotPasswordRequestTime.getTime() +
                    duration);
            Date now = new Date();
            return expTime.before(now);
        }
        else
            return true;
    }
    
    /**
     * @return a sepecial, and unique, code generated and stored
     *         when the user requested for password reset
     */
    public String getForgottenPasswordResponseCode()
    {
        return this.forgotPasswordResponseCode;
    }

    /**
     * sets the password reset request special code
     * @param code special code 
     */
    public void setForgottenPasswordResponseCode(String code)
    {
        this.forgotPasswordResponseCode = code;
    }
    
    //  End forgot password handling.
    //  ********************************************************************
    
    //  We should always have LDAP available to us, if we do not someone
    //  messed up and they should be populating the User object with some
    //  minimal values before referencing it.
    
    public String getUsername() { 
        //  LDAP... always ldap.
        
        return this.ldapuser.getUsername();
    }
    
    public String getPassword() {
        return this.ldapuser.getPassword();
    }
    
    //  While I think it unlikely, we should probably come up with some sort
    //  of error handling scheme for bad data coming back from ldap or telesales.
    
    public InternetAddress getEmailAddress() {
        boolean ldapOK = true;
        InternetAddress email = null;
        
        //  Grab our email address from the LDAP object...
        
        try {
            email = new InternetAddress(this.ldapuser.getUsername());
        }
        catch (javax.mail.internet.AddressException e)
        {
            //  We should never ever ever get here.
            
            _logger.warn("UserImpl Problem => We got a bad email address from ldap." + LogUtil.appendableStack(e));
            ldapOK = false;
        }
        //  However it is probably better to TRY and get it from our person
        //  object if it is available.  Since I am not really sure that even
        //  if we have a person object the underlying "stuff" will really
        //  be there, I do this secondarily to setting the address via the
        //  ldapuser object.
        
        if (this.person != null) {
            List<ContactPoint> cps = this.person.getContactPoints();
            Iterator<ContactPoint> i = cps.iterator();
            ContactPoint cp = null;
            
            while (i.hasNext()) {
                cp = i.next();
                if (cp.getContactPointType() == ContactPointTypeEnum.EMAIL 
                						&& cp.getStatus().equalsIgnoreCase("A")) 
                	{
                    try {
                        email = new InternetAddress(cp.getEmailAddress());
                    }
                    catch (javax.mail.internet.AddressException e)
                    {
                        //  This is impossible.  An invalid email
                        //  address could never have gotten into the
                        //  database in the first place, unless it was
                        //  done manually and a mistake was made.
                        
                        _logger.warn("UserImpl Problem => We got a bad email address from telesales: " + cp.getEmailAddress(),e);
                        if (ldapOK) {
                            _logger.warn("UserImpl Problem => email from ldap: " + this.ldapuser.getUsername());
                        }
                    }
                    break;
                }
            }
        }
        return email;
    }
    //  Not unlike email, we start with the value from LDAP for the person's
    //  first, middle and last names.  But we are probably still better off
    //  with whatever person or party contains, even tho' it SHOULD BE THE
    //  SAME!  This is probably processor-wasteful.  I might rethink this.
    
    public String getFirstName() {
        String fname = this.ldapuser.getFirstName();
        
        if (this.person != null) {
            fname = this.person.getName().getFirstName();
        }
        else if (this.party != null) {
            fname = this.party.getFirstName();
        }
        return fname;   
    }
    public String getLastName() {
        String lname = this.ldapuser.getLastName();
        
        if (this.person != null) {
            lname = this.person.getName().getLastName();
        }
        else if (this.party != null) {
            lname = this.party.getLastName();
        }
        return lname;
    }
    public String getMiddleName() {
        String mname = this.ldapuser.getMiddleName();
        
        if (this.person != null) {
            mname = this.person.getName().getMiddleName();
        }
        else if (this.party != null) {
            mname = this.party.getMiddleName();
        }
        return mname;
    }
    
    public String getTitle() {
        String ttl = null;
        
        if (person == null) loadPersonData();
        
        try {
            ttl = person.getName().getPrefix();
        }
        catch(Exception e) {
            //  Don't bother.
        	_logger.warn(LogUtil.getStack(e));
        }
        if (ttl==null){
        	return "";
        }
        return getDisplayTitle(ttl);
    }
    
    public String getJobTitle() {
        String ttl = null;
        
        if (person == null) loadPersonData();
        
        try {
            ttl = person.getOrganizationContact().getJobTitle();
        }
        catch(Exception e) {
        	_logger.warn(LogUtil.getStack(e));
        }
        return ttl;
    }
    
    public String getDepartment() {
        String dept = null;
        
        if (person == null) loadPersonData();
        
        try {
            dept = person.getOrganizationContact().getDepartment();
        }
        catch(Exception e) {
        	_logger.warn(LogUtil.getStack(e));
        }
        return dept;
    }
    
    public String getBillingPrefix() {
        String pfx = null;
        
        //if (person != null) loadPersonData();
        if (this.account == null) loadPersonData();
        
        try {
            pfx = this.account.getName().getPrefix();
        }
        catch(Exception e) {
        	_logger.warn(LogUtil.getStack(e));
        }
        if (pfx==null){
        	return "";
        }
        return getDisplayTitle(pfx);
    }
    
    public String getBillingJobTitle() {
        String ttl = null;
        
        //if (person != null) loadPersonData();
        if (this.account == null) loadPersonData();
        
        try {
            //ttl = orgPerson.getOrganizationContact().getJobTitle();
            ttl = this.account.getOrganizationContact().getJobTitle();
        }
        catch(Exception e) {
        	_logger.warn(LogUtil.getStack(e));
        }
        return ttl;
    }
    
    public String getBillingDepartment() {
        String dept = null;
        
        if (person != null) loadPersonData();
        
        try {
            //dept = orgPerson.getOrganizationContact().getDepartment();
            dept = this.account.getOrganizationContact().getDepartment();
        }
        catch(Exception e) {
        	_logger.warn(LogUtil.getStack(e));
        }
        return dept;
    }
    
    public Long getPartyId() {
        //Long partyId = new Long(this.ldapuser.getPartyID());
        
        String partyId = ldapuser.getPartyID();
        
        if (partyId == null || partyId == "" ) {
             if (this.ldapuser.getRightsLinkPartyID() != null)
             {
                 partyId = this.ldapuser.getRightsLinkPartyID();
             }
        }
        if (WebUtils.isAllDigit(partyId)) {
        	return Long.parseLong(partyId);
        }
        else {
        	return null;
        }
    }
    //Returns suppress NRLS fee flag value from ccc_ar_account_info table
    public boolean getSuppressNRLSFee() {
        
        if (this.accountInfo != null) {
            return this.accountInfo.getSuppressNRLSFeeViaCC();
        }
        return false;
    }
    //  Once we get past the username/password, email address and name
    //  we move into shadier territory.  Much of the data we might want
    //  really should be coming from telesales.  TF really doesn't have
    //  much to offer here.  Any overlapping data, which will be very
    //  little, we can consider taking from elsewhere.
    
    public String getArAccountNumber() {
        String acctNo = null;
        if (this.accountInfo != null) {
            acctNo = this.accountInfo.getArAccountNumber().toString();
        }
        if (acctNo == null && this.account != null) {
        	acctNo = this.account.getAccountNumber();
        }
        return acctNo;
    }
    
    //  Need to flesh this out.
    
    public boolean getOnHold() 
    {
        if (ldapuser.getPartyID() != null && Long.parseLong(ldapuser.getPartyID()) > 0)
        {	        	
        	party = getTFService().getIndLicenseePartyByPartyId(new TFConsumerContext(), 
                new Long(this.ldapuser.getPartyID()));
        }
        else if (ldapuser.getRightsLinkPartyID() != null && Long.parseLong(ldapuser.getRightsLinkPartyID()) > 0)
        {
            party = getTFService().getIndLicenseePartyByPartyId(new TFConsumerContext(), 
                new Long(this.ldapuser.getRightsLinkPartyID()));
        }
        
        if (party != null)
        {
            if (party.getAccountNumber() != null && party.getAccountNumber() > 0)
            {
                try
                {
                    return getTFService().getArAccountInfoByAccount(new TFConsumerContext(), 
                        party.getAccountNumber()).getCreditHold();
                }
                catch (Exception e)
                {
                    _logger.warn("User :: Unable to get Account On Hold Status for acct " + party.getAccountNumber() + LogUtil.appendableStack(e));
                }
            }
        } 
        return false;
    }
    
    public String getUserChannel()
    {
        if (this.person != null) {
        	if (this.person.getUserType() != null)
        	{
        		this.channel = this.person.getUserType().getUserType();
        	}
        	else
        	{
        		this.channel = null;
        	}
        }
        else {
            if (this.channel == null) {
                try {
                	String partyId = (this.ldapuser.getPartyID() == null) ? 
    						this.ldapuser.getRightsLinkPartyID() : this.ldapuser.getPartyID();
    						
			    	if(getTelesalesIsUp())
			    	{
	                    Person tmp = getTelesalesService()
	                        .getPersonByPartyId(new TelesalesServiceConsumerContext(), new Long(partyId));
	                    if (tmp != null)
	                    {
	                    	if (tmp.getUserType() != null)
	                    	{
	                    		this.channel = tmp.getUserType().getUserType();
	                    	}
	                    	else
	                    	{
	                    		this.channel = null ;
	                    	}
	                    }
	                    else
	                    {
	                    	this.channel = null;
	                    }
			    	}
			    	else
			    	{
			    		channel = null;
			    	}
                }
                catch(Exception e) {
                    _logger.warn("User :: Unable to get person object for specified party." + LogUtil.appendableStack(e));
                    return null;
                }
            }
        }
        return this.channel;
    }
    
    public Long getPtyInst() {
        Long ptyInst = null;
        
        if (this.party != null) {
            ptyInst = this.party.getPtyInst();
        }
        else {
            try {
            	String partyId = (this.ldapuser.getPartyID() == null) ? 
						this.ldapuser.getRightsLinkPartyID() : this.ldapuser.getPartyID();
						
                this.party = getTFService()
                    .getIndLicenseePartyByPartyId(new TFConsumerContext(), new Long(partyId));
                    
                ptyInst = this.party.getPtyInst();
            }
            catch(Exception e) {
                _logger.warn("User :: Unable to get person object for specified party." + LogUtil.appendableStack(e));
            }
        }
        return ptyInst;
    }
    
    public Long getOrgPtyInst() {
        Long ptyInst = null;
        
        if (this.orgParty != null) {
            ptyInst = this.orgParty.getPtyInst();
        }
        else {
            try {
            	String partyId = (this.ldapuser.getPartyID() == null) ? 
						this.ldapuser.getRightsLinkPartyID() : this.ldapuser.getPartyID();
						
                if (this.party == null) {
                    this.party = getTFService()
                        .getIndLicenseePartyByPartyId(new TFConsumerContext(), new Long(partyId));
                }
                    
                Party org = getTFService()
                    .getOrgLicenseePartyByAccountNumber(new TFConsumerContext(), this.party.getAccountNumber());
                    
                ptyInst = org.getPtyInst();
            }
            catch(Exception e) {
                _logger.warn("User :: Unable to get person object for specified party." + LogUtil.appendableStack(e));
            }
        }
        return ptyInst;
    }
    
    public Long getAccountNumber() {
    	String partyId = null;
    	Long acctNumber = null;
        if (party!=null) {
        	acctNumber=party.getAccountNumber();
        } else {
            try {
            	partyId = (this.ldapuser.getPartyID() == null) ? 
						this.ldapuser.getRightsLinkPartyID() : this.ldapuser.getPartyID();
				if (partyId==null) {
					_logger.warn("partyId and rlPartyId on ldapuser is null");
				} else {
					party = getTFService().getIndLicenseePartyByPartyId(new TFConsumerContext(), new Long(partyId));
				}
            } catch(Exception e) {
                _logger.error("User :: Unable to get person object for partyId " + partyId + LogUtil.appendableStack(e));
            }
        }
        return acctNumber;
    }
    
    public Long getAdminPartyId() {
        //  When creating a shared user the first thing we do is populate the
        //  ldapuser and accountInfo objects.  So this SHOULD BE HERE.  If not,
        //  we got problems.
        
    	if (this.accountInfo != null) {
            return this.accountInfo.getAdminPartyId();
    	} else {
    		return null;
    	}
    	
    }
    
    public boolean getIsAdmin() {
        return (this.getAdminPartyId() != null && 
            this.getAdminPartyId().equals(this.getPartyId()));
    }
    
    public String getPK() {
        //  This is cheating.  It used to be the oid_pk, in ldap...
        //  Just using a strange username/party_id combination to try
        //  and foobar it.
    	String partyId = (this.ldapuser.getPartyID() == null) ? 
				this.ldapuser.getRightsLinkPartyID() : this.ldapuser.getPartyID();
				
        return "[" + this.ldapuser.getUsername() + ":" + partyId + "]";
    }
    
    public Location getMailingAddress() {
        if (this.mailTo == null) {
            loadPersonData();
            buildMailToData();
        }
        return this.mailTo;
    }
    
    public Location getBillingAddress() {
        if (this.billTo == null) {
            loadPersonData();
            buildBillToData();
        }
        return this.billTo;
    }
    
    public String getPhoneNumber() {
        String phoneNumber = null;
        
        if (person == null) loadPersonData();
        
        ContactPoint cp;
        
        if (this.person != null)
        {
        List<ContactPoint> cps = this.person.getContactPoints();
        Iterator<ContactPoint> k = cps.iterator();
        
        while (k.hasNext()) {
            cp = k.next();
            if (cp.getContactPointType() == ContactPointTypeEnum.PHONE &&
                cp.getPhoneLineType() == PhoneLineTypeEnum.GENERAL &&
                cp.getStatus().equalsIgnoreCase("A")) 
            {
                //  This is probably lacking an additional check for the
                //  ContactPointPurposeEnum... for now it will do.
                
                phoneNumber = cp.getDisplayPhone();
                break;
            }
          }
        
        }
        return phoneNumber;
    }
    
    public String getExtension() {
        String ext = null;
        
        if (person == null) loadPersonData();
        
        ContactPoint cp;
        List<ContactPoint> cps = this.person.getContactPoints();
        Iterator<ContactPoint> k = cps.iterator();
        
        while (k.hasNext()) {
            cp = k.next();
            if (cp.getContactPointType() == ContactPointTypeEnum.PHONE &&
                cp.getPhoneLineType() == PhoneLineTypeEnum.GENERAL &&
                cp.getStatus().equalsIgnoreCase("A")) 
            {
                //  This is probably lacking an additional check for the
                //  ContactPointPurposeEnum... for now it will do.
                
                ext = cp.getPhoneExtension();
                break;
            }
        }
        return ext;
    }
    
    public String getFaxNumber() {
        String phoneNumber = null;
        
        if (person == null) loadPersonData();
        
        ContactPoint cp;
        List<ContactPoint> cps = this.person.getContactPoints();
        Iterator<ContactPoint> k = cps.iterator();
        
        while (k.hasNext()) {
            cp = k.next();
            if (cp.getContactPointType() == ContactPointTypeEnum.PHONE &&
                cp.getPhoneLineType() == PhoneLineTypeEnum.FAX &&
                cp.getStatus().equalsIgnoreCase("A")) 
            {
                //  This is probably lacking an additional check for the
                //  ContactPointPurposeEnum... for now it will do.
                
                phoneNumber = cp.getDisplayPhone();
                break;
            }
        }
        return phoneNumber;
    }
    
    public String getBillingPhoneNumber() {
        String phoneNumber = null;
        
        if (account == null) loadPersonData();
        
        ContactPoint cp;
        //List<ContactPoint> cps = this.orgPerson.getContactPoints();
        List<ContactPoint> cps = this.account.getContactPoints();
        Iterator<ContactPoint> k = cps.iterator();
        
        while (k.hasNext()) {
            cp = k.next();
            if (cp.getContactPointType() == ContactPointTypeEnum.PHONE &&
                cp.getPhoneLineType() == PhoneLineTypeEnum.GENERAL &&
                cp.getStatus().equalsIgnoreCase("A")) 
            {
                //  This is probably lacking an additional check for the
                //  ContactPointPurposeEnum... for now it will do.
                
                //phoneNumber = "+" + cp.getPhoneCountryCode() + " (" + cp.getAreaCode() + ") " + cp.getPhoneNumber();
                phoneNumber = cp.getDisplayPhone();
                break;
            }
        }
        return phoneNumber;
    }
    
    public String getBillingExtension() {
        String phoneNumber = null;
        
        if (account == null) loadPersonData();
        
        ContactPoint cp;
        //List<ContactPoint> cps = this.orgPerson.getContactPoints();
        List<ContactPoint> cps = this.account.getContactPoints();
        Iterator<ContactPoint> k = cps.iterator();
        
        while (k.hasNext()) {
            cp = k.next();
            if (cp.getContactPointType() == ContactPointTypeEnum.PHONE &&
                cp.getPhoneLineType() == PhoneLineTypeEnum.GENERAL &&
                cp.getStatus().equalsIgnoreCase("A")) 
            {
                //  This is probably lacking an additional check for the
                //  ContactPointPurposeEnum... for now it will do.
                
                phoneNumber = cp.getPhoneExtension();
                break;
            }
        }
        return phoneNumber;
    }
    
    public String getBillingFaxNumber() {
        String phoneNumber = null;
        
        if (account == null) loadPersonData();
        
        ContactPoint cp;
        //List<ContactPoint> cps = this.orgPerson.getContactPoints();
        List<ContactPoint> cps = this.account.getContactPoints();
        Iterator<ContactPoint> k = cps.iterator();
        
        while (k.hasNext()) {
            cp = k.next();
            if (cp.getContactPointType() == ContactPointTypeEnum.PHONE &&
                cp.getPhoneLineType() == PhoneLineTypeEnum.FAX &&
                cp.getStatus().equalsIgnoreCase("A")) 
            {
                //  This is probably lacking an additional check for the
                //  ContactPointPurposeEnum... for now it will do.
                
                //phoneNumber = "+" + cp.getPhoneCountryCode() + " (" + cp.getAreaCode() + ") " + cp.getPhoneNumber();
                phoneNumber = cp.getDisplayPhone();
                break;
            }
        }
        return phoneNumber;
    }
    
    public Long getBillingPartyId() {
        Long id = null;
        
        if (this.account == null) loadPersonData();
        
        if (this.account != null)
        {
        	id = this.account.getBillToPartyId();
        }
       /* Person pers = getTelesalesService().getBillToPersonByArAccountNumber(new TelesalesServiceConsumerContext(), 
                this.account.getAccountNumber(), this.person.getUserType());
        
        if (pers.getPartyId() != null && pers.getPartyId() > 0 )
        {
            id = pers.getPartyId();
        } */
                
        return id;
    }
    
    public InternetAddress getBillingEmailAddress() {
        String emailAddress = null;
        InternetAddress ia = null;
        
        if (account == null) loadPersonData();
        
        ContactPoint cp;
        //List<ContactPoint> cps = this.orgPerson.getContactPoints();
        List<ContactPoint> cps = this.account.getContactPoints();
        Iterator<ContactPoint> k = cps.iterator();
        
        while (k.hasNext()) {
            cp = k.next();
            if (cp.getContactPointType() == ContactPointTypeEnum.EMAIL && 
            		cp.getStatus().equalsIgnoreCase("A")) {
                //  This is probably lacking an additional check for the
                //  ContactPointPurposeEnum... for now it will do.
                
                emailAddress = cp.getEmailAddress();
                break;
            }
        }
        if (emailAddress != null) {
            //  Convert it to an InternetAddress for backward compatibility.
            
            try {
                ia = new InternetAddress(emailAddress);
            }
            catch(Exception e) {
                //  Should not fail.  Yeah.  What could possibly go wrong?
            	_logger.error(LogUtil.getStack(e));
            }
        }
        return ia;
    }
    
    public String getMarketSegment() {
        String marketSegment = null;
        
        if (person == null) loadPersonData();
        
        try {
            List<Classification> classes = person.getClassifications();
            Iterator<Classification> i = classes.iterator();
            Classification cls;
            MarketSegmentEnum seg;
            
            while (i.hasNext()) {
                cls = i.next();
                if (cls.getCategory() == CustomerCategoryEnum.MARKETSEGMENT) {
                    marketSegment = getSegment(cls.getCode().getClassCode());
                    break;
                }
            }
        }
        catch(Exception e) {
            _logger.warn("User :: Could not get classifications." + LogUtil.appendableStack(e));
        }
        return marketSegment;
    }
    
    public boolean getTelesalesIsUp() {
        boolean ok;
        try { 
            ok = SystemStatus.isTelesalesUp().booleanValue(); 
        }
        catch(Exception e) {
            ok = false;
        }
        return ok;
    }
    
    public long getID() {
        long id = 0;
        
        if (party == null && getTelesalesIsUp()) loadPartyData();
        if (party != null) {
            try {
                id = party.getPartyId();
            }
            catch(Exception e) {
                _logger.warn("User :: Could not get party primary key value from party object." + LogUtil.appendableStack(e));
            }
        } else if (ldapuser != null ) {
        	try {
            	id = new Long(ldapuser.getPartyID()).longValue();
        	} catch (Exception e) {
                _logger.warn("User :: Could not get party primary key value from ldap object." + LogUtil.appendableStack(e));        		
        	}
        }

        return id;
    }
    
    public String getDisplayName() {
        String name = "";
        
        if (person == null) loadPersonData();
        
        try {
            name = person.getName().getFirstName() + " " + person.getName().getLastName();
        }
        catch(Exception e) {
            _logger.warn(LogUtil.getStack(e));
        }
        return name;
    }
    
    public String getName() {
        return getDisplayName();
    }
    
    public String getBillingDisplayName() {
        String name = "";
        
        if (account == null) loadPersonData();
        
        try {
            name = account.getName().getFirstName() + " " + account.getName().getLastName();
        }
        catch(Exception e) {
            _logger.warn(LogUtil.getStack(e));
        }
        return name;
    }
    
    public String getAccountContactName() {
        Person admin = null;
        
        if (accountInfo == null) loadPartyData();
        
        try {
            admin = getTelesalesService()
                .getPersonByPartyId(new TelesalesServiceConsumerContext(), accountInfo.getAdminPartyId());
        }
        catch(Exception e) {
            _logger.warn(LogUtil.getStack(e));
        }
        return admin != null ? admin.getName().getLastName():"";
    }
    
    //  Setters...  These are not really here for anything more than the rare
    //  need to fill a blank user object with some data.  This stuff does not get
    //  written back in any way shape or form, I hope.  If someone wants to create
    //  or update a User, they should do so piecemeal through the service APIs.
    
    public void setFirstName(String fname) {
        if (this.ldapuser == null) this.ldapuser = new LdapUser();
        this.ldapuser.setFirstName(fname);
    }
    public void setMiddleName(String mname) {
        if (this.ldapuser == null) this.ldapuser = new LdapUser();
        this.ldapuser.setMiddleName(mname);
    }
    public void setLastName(String lname) {
        if (this.ldapuser == null) this.ldapuser = new LdapUser();
        this.ldapuser.setLastName(lname);
    }
    public void setBillingAddress(Location loc) {
        this.billTo = loc;
    }
    public void setMailingAddress(Location loc) {
        this.mailTo = loc;
    }
    
    public String getDisplayTitle(String ttl)
    {
    	if (ttl==null) {
    		return "";
    	}
        if (ttl.equalsIgnoreCase("MR."))
        { 
            return "Mr.";
        }
        else if (ttl.equalsIgnoreCase("MS."))
        {
            return "Ms.";
        }
        else if (ttl.equalsIgnoreCase("MISS"))
        {
            return "Miss";
        }
        else if (ttl.equalsIgnoreCase("MRS."))
        {
            return "Mrs.";
        }
        else if (ttl.equalsIgnoreCase("DR."))
        {
            return "Dr.";
        }
        else if (ttl.equalsIgnoreCase("PROF"))
        {
            return "Prof.";
        }
        else if (ttl.equalsIgnoreCase("SIR"))
        {
            return "Sir";
        }
        else
        {
            return ttl;
        }
        
    }
    
    private String getSegment(String type)
    {
          
      if (type.equalsIgnoreCase("academic")) 
      {
        return "Academic User";
      }
      else if (type.equalsIgnoreCase("business")) 
      {
            return "Business User";
      }
      else if (type.equalsIgnoreCase("author or creator")) 
      {
            return "Author or Creator";
      }
      else if (type.equalsIgnoreCase("publisher")) 
      {
            return "Publisher";
      }
      else if (type.equalsIgnoreCase("other")) 
      {
            return "Other";
      }
      else {
          return "Other";
      }       
      
    }
}