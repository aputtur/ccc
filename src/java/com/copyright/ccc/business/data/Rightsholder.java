package com.copyright.ccc.business.data;

import java.io.Serializable;

import com.copyright.ccc.business.services.user.User;
import com.copyright.svc.telesales.api.data.Location;
import com.copyright.svc.telesales.api.data.RightsholderOrganization;


public class Rightsholder implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long _partyId;
    private long _ptyInst;
    private RightsholderOrganization _rightsholderOrg;
    
    private String rhName;
    private String rhAddress1;
    private String rhAddress2;
    private String rhAddress3;
    private String rhAddress4;
    private String rhCity;
    private String rhState;
    private String rhCountry;
    private String rhPostalCode;
    private String rhPhone;
    private String rhEmail;
    private String rhFax;
    
    public Rightsholder()
    {
        _partyId = 0;
        _ptyInst = 0;
        _rightsholderOrg = null;
        rhName = "";
        rhAddress1 = "";
        rhAddress2 = "";
        rhAddress3 = "";
        rhAddress4 = "";
        rhCity = "";
        rhState = "";
        rhCountry = "";
        rhPostalCode = "";
        rhPhone = "";
        rhEmail = "";
        rhFax = "";
    }

    public Rightsholder(RightsholderOrganization org)
    {
        _rightsholderOrg = org;
        _partyId = 0;
        _ptyInst = 0;
        rhName = "";
        rhAddress1 = "";
        rhAddress2 = "";
        rhAddress3 = "";
        rhAddress4 = "";
        rhCity = "";
        rhState = "";
        rhCountry = "";
        rhPostalCode = "";
        rhPhone = "";
        rhEmail = "";
        rhFax = "";
        
        if (_rightsholderOrg != null) {
            _partyId = _rightsholderOrg.getOrgPartyId().longValue();
            
            rhEmail = _rightsholderOrg.getRhInfoEmailAddress();
            rhName = _rightsholderOrg.getOrganizationName();
            rhPhone = _rightsholderOrg.getRhInfoPhoneNumber();
            
            Location loc = _rightsholderOrg.getMailToAddress();
            
            if (loc != null) {
                rhAddress1 = loc.getAddress1();
                if (loc.getAddress2() != null) rhAddress2 = loc.getAddress2();
                if (loc.getAddress3() != null) rhAddress3 = loc.getAddress3();
                if (loc.getAddress4() != null) rhAddress4 = loc.getAddress4();
                if (loc.getCity() != null) rhCity = loc.getCity();
                if (loc.getState() != null) rhState = loc.getState();
                if (loc.getCountry() != null) rhCountry = loc.getCountry();
                if (loc.getPostalCode() != null) rhPostalCode = loc.getPostalCode();
            }
        }
    }
    
    public Rightsholder(User user)
    {
        _rightsholderOrg = null;
        _partyId = user.getPartyId().longValue();
        _ptyInst = user.getPtyInst().longValue();
        rhName = user.getDisplayName();
        
        Location addr = user.getMailingAddress();
        
        rhAddress1 = addr.getAddress1() != null ? addr.getAddress1() : "";
        rhAddress2 = addr.getAddress2() != null ? addr.getAddress2() : "";
        rhAddress3 = addr.getAddress3() != null ? addr.getAddress3() : "";
        rhAddress4 = addr.getAddress4() != null ? addr.getAddress4() : "";
        rhCity = addr.getCity() != null ? addr.getCity() : "";
        rhState = addr.getState() != null ? addr.getState() : "";
        rhCountry = addr.getCountry() != null ? addr.getCountry() : "";
        rhPostalCode = addr.getPostalCode() != null ? addr.getPostalCode() : "";
        rhPhone = user.getPhoneNumber() != null ? user.getPhoneNumber() : "";
        rhEmail = user.getEmailAddress().getAddress();
        rhFax = user.getFaxNumber() != null ? user.getFaxNumber() : "";
        
        if (rhEmail == null) rhEmail = "";
    }
    
    public String getRhFax() { return rhFax; }
    public void setFax(String fax) {
        this.rhFax = fax;
    } 
    
    public String getRhPhone() { return rhPhone; }
    public void setPhone(String ph) {
        this.rhPhone = ph;
    }
    
    public String getRhEmail() { return rhEmail; }
    public void setRhEmail(String em) {
        this.rhEmail = em;
    }

    public void setRhName(String rhName) {
        this.rhName = rhName;
    }
    public String getRhName() { return rhName; }          
        

    public void setRhAddress1(String rhAddress1) {
        this.rhAddress1 = rhAddress1;
    }
    public String getRhAddress1() { return rhAddress1; }
    
    public void setRhAddress2(String rhAddress2) {
        this.rhAddress2 = rhAddress2;
    }
    public String getRhAddress2() { return rhAddress2; }

    public void setRhAddress3(String rhAddress3) {
        this.rhAddress3 = rhAddress3;
    }
    public String getRhAddress3() { return rhAddress3; }

    public void setRhAddress4(String rhAddress4) {
        this.rhAddress4 = rhAddress4;
    }
    public String getRhAddress4() { return rhAddress4; }

    public void setRhCity(String rhCity) {
        this.rhCity = rhCity;
    }
    public String getRhCity() { return rhCity; }

    public void setRhState(String rhState) {
        this.rhState = rhState;
    }
    public String getRhState() { return rhState; }

    public void setRhCountry(String rhCountry) {
        this.rhCountry = rhCountry;
    }
    public String getRhCountryAbbrev() { return rhCountry; }
    public String getRhCountry() { return rhCountry; }

    public void setRhPostalCode(String rhPostalCode) {
        this.rhPostalCode = rhPostalCode;
    }
    public String getRhPostalCode() { return rhPostalCode; }

    public void setPartyId(long partyId) {
        this._partyId = partyId;
    }
    public long getPartyId() { return _partyId; }

    public void setPtyInst(long ptyInst) {
        this._ptyInst = ptyInst;
    }
    public long getPtyInst() { return _ptyInst; }
}
