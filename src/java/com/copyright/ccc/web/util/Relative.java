package com.copyright.ccc.web.util;

// import com.copyright.data.account.User;
import java.io.Serializable;

import org.apache.log4j.Logger;

import com.copyright.ccc.business.services.user.User;
import com.copyright.ccc.util.LogUtil;
import com.copyright.workbench.logging.LoggerHelper;

/**
 * This class is populated with the CONTACT_OF relatives to a given user.
 *
 * @author Michael Jessop &lt;&gt;
 * @version $Rev: 89646 $
 */
public final class Relative implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger sLogger = LoggerHelper.getLogger();
	
	//  The data in this class is set up sort of separately because
    //  it is used in two different ways.  First, to store some BASIC
    //  information about all the users in a given account.  Second,
    //  to actually store and deliver data contained in an actual
    //  User object.
    
	private long _id = 0;
	private long _rel_id = 0;
	private long _org_id = 0;
	private String _org_type = null;
	private String _fname = null;
	private String _mname = null;
	private String _lname = null;
	private String _pname = null;
	private User _user = null;
    
    //  Our constructors are limited...  one for our account query
    //  and one for a user wrapper.
    
    public Relative( long partyId
                   , long relPartyId
                   , long orgPartyId
                   , String orgType
                   , String firstName
                   , String middleName
                   , String lastName
                   , String partyName ) 
    {
        _id = partyId;
        _rel_id = relPartyId;
        _org_id = orgPartyId;
        _org_type = orgType;
        _fname = firstName;
        _mname = middleName;
        _lname = lastName;
        _pname = partyName;
    }
    
    public Relative(User user) { 
        _user = user;
        _fname = _user.getFirstName();
        _mname = _user.getMiddleName();
        _lname = _user.getLastName();
        
        if (_fname != null) _pname = _fname;
        if (_lname != null) _pname = _pname + " " + _lname;
    }
    
    public Relative() { 
        //  nothing. 
    }
    
    //  Mostly this class just needs "getters" because it is
    //  primarily for holding data that is to be displayed on
    //  a user admin page.  The first set are shared between
    //  functions.
    
    public long getPartyId()      { return _id;     }
    public long getRelPartyId()   { return _rel_id; }
    public long getOrgPartyId()   { return _org_id; }
    public String getOrgType()    { return _org_type; }
    public String getFirstName()  { return _fname;  }
    public String getMiddleName() { return _mname;  }
    public String getLastName()   { return _lname;  }
    public String getPartyName()  { return _pname;  }
    
    //  The second set are for the more advanced display of data.
    
    public String getStreet()      { return _user.getMailingAddress().getAddress1(); }
    public String getStreet2()     { return _user.getMailingAddress().getAddress2(); }
    public String getStreet3()     { return _user.getMailingAddress().getAddress3(); }
    public String getStreet4()     { return _user.getMailingAddress().getAddress4(); }
    public String getCity()        { return _user.getMailingAddress().getCity(); }
    public String getState()       { return _user.getMailingAddress().getState(); }
    public String getZipcode()     { return _user.getMailingAddress().getPostalCode(); }
    public String getCountry()     { return _user.getMailingAddress().getCountry(); }
    public String getPhone()       { return _user.getPhoneNumber(); }
    public String getExtension()   { return _user.getExtension(); }
    public String getFax()         { return _user.getFaxNumber(); }
    public String getEmail()       { return _user.getUsername(); }
    public String getJobTitle()    { return _user.getJobTitle(); }
    public String getDepartment()  { return _user.getDepartment(); }
    public String getContactName() { return _user.getAccountContactName(); }
    
    public String getIsAdmin() { 
        String ans = "NO";
        if (_user.getIsAdmin()) { ans = "YES"; }
        return ans;
    }
    
    public String getBillingStreet()    { return _user.getBillingAddress().getAddress1(); }
    public String getBillingStreet2()   { return _user.getBillingAddress().getAddress2(); }
    public String getBillingStreet3()   { return _user.getBillingAddress().getAddress3(); }
    public String getBillingStreet4()   { return _user.getBillingAddress().getAddress4(); }
    public String getBillingCity()      { return _user.getBillingAddress().getCity(); }
    public String getBillingState()     { return _user.getBillingAddress().getState(); }
    public String getBillingZipcode()   { return _user.getBillingAddress().getPostalCode(); }
    public String getBillingCountry()   { return _user.getBillingAddress().getCountry(); }
    public String getBillingPhone()     { return _user.getBillingPhoneNumber(); }
    public String getBillingExtension() { return _user.getBillingExtension(); }
    public String getBillingFax()       { return _user.getBillingFaxNumber(); }
    //public String getBillingEmail()     { return _user.getBillingEmailAddress().getAddress(); }
    
    //  The third set... I don't know.  They may be useful someday.
    
    public String getPartyIdAsString() { 
        String id = null;
        
        try {
            id = Long.toString(_id); 
        }
        catch(Exception e){
        	sLogger.info(LogUtil.getStack(e));
        }
        return id;
    }
    public String getRelPartyIdAsString() {
        String id = null;
        
        try {
            id = Long.toString(_rel_id);
        }
        catch(Exception e) {
        	sLogger.info(LogUtil.getStack(e));
        }
        return id; 
    }
    public String getOrgPartyIdAsString() { 
        String id = null;
        
        try {
            id = Long.toString(_org_id);
        }
        catch(Exception e) {
        	sLogger.info(LogUtil.getStack(e));
        }
        return id; 
    }
}
