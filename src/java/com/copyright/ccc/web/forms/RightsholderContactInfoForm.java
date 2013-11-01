package com.copyright.ccc.web.forms;

import com.copyright.ccc.business.data.Rightsholder;
import com.copyright.ccc.web.CCValidatorForm;
import com.copyright.workbench.util.StringUtils2;


public class RightsholderContactInfoForm extends CCValidatorForm
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long _rhId;
    private long rgtInst;
    private long dateOfUse;
    private Rightsholder _rightsholder;
    private String rhName;
    private String rhAddress1;
    private String rhAddress2;
    private String rhAddress3;
    private String rhAddress4;
    private String rhCity;
    private String rhState;
    private String rhCountry;
    private String rhPostalCode;
    private boolean inCompleteAddressInfo = true;
    private String rhPhone = "";
    private String rhEmail = "";
    private String rhFax = "";
    
    public RightsholderContactInfoForm()
    {

    }
    
    public String getRhFax() {
        if (_rightsholder != null)
            return _rightsholder.getRhFax();

        return "";    
    }
    
    public void setRhFax(String fx) {
        this.rhFax = fx;
    } 
    
    public String getRhEmail() {
        if (_rightsholder != null)
            return _rightsholder.getRhEmail();

        return ""; 
    }
    
    public void setRhEmail(String em) {
        this.rhEmail = em;
    }
    
    public String getRhPhone() {
        if (_rightsholder != null)
            return _rightsholder.getRhPhone();

        return "";    
    }
    
    public void setRhPhone(String ph) {
        this.rhPhone = ph;
    } 
    
    public long getRhId() {
        return _rhId;        
    }

    public void setRhId(long rhId) {
        this._rhId = rhId;
    }


    public String getRhName() {
        if (_rightsholder != null)
            return _rightsholder.getRhName();

        return "";        
    }

    public void setRhAddress1(String rhAddress1) {
        this.rhAddress1 = rhAddress1;
    }

    public String getRhAddress1() {
        if (_rightsholder != null)
            return _rightsholder.getRhAddress1();

        return "";
    }

    public void setRhAddress2(String rhAddress2) {
        this.rhAddress2 = rhAddress2;
    }

    public String getRhAddress2() {
        if (_rightsholder != null)
            return _rightsholder.getRhAddress2();

        return "";
    }

    public void setRhAddress3(String rhAddress3) {
        this.rhAddress3 = rhAddress3;
    }

    public String getRhAddress3() {
        if (_rightsholder != null)
            return _rightsholder.getRhAddress3();

        return "";
    }

    public void setRhAddress4(String rhAddress4) {
        this.rhAddress4 = rhAddress4;
    }

    public String getRhAddress4() {
        if (_rightsholder != null)
            return _rightsholder.getRhAddress4();

        return "";
    }

    public void setRhCity(String rhCity) {
        this.rhCity = rhCity;
    }

    public String getRhCity() {
        if (_rightsholder != null)
            return _rightsholder.getRhCity();

        return "";
    }

    public void setRhState(String rhState) {
        this.rhState = rhState;
    }

    public String getRhState() {
        if (_rightsholder != null)
            return _rightsholder.getRhState();

        return "";
    }

    public void setRhCountry(String rhCountry) {
        this.rhCountry = rhCountry;
    }

    public String getRhCountry() {
        if (_rightsholder != null)
            return _rightsholder.getRhCountry();

        return "";
    }

    public String getRhCountryAbbrev() {
        if (_rightsholder != null)
            return _rightsholder.getRhCountryAbbrev();

        return "";
    }
    
    public void setRhPostalCode(String rhPostalCode) {
        this.rhPostalCode = rhPostalCode;
    }

    public String getRhPostalCode() {
        if (_rightsholder != null)
            return _rightsholder.getRhPostalCode();

        return "";
    }

    public void setRgtInst(long rgtInst) {
        this.rgtInst = rgtInst;
    }

    public long getRgtInst() {
        return rgtInst;
    }

    public void setDateOfUse(long dateOfUse) {
        this.dateOfUse = dateOfUse;
    }

    public long getDateOfUse() {
        return dateOfUse;
    }

    public void setRightsholder(Rightsholder rightsholder) {
        this._rightsholder = rightsholder;
    }

    public Rightsholder getRightsholder() {
        return _rightsholder;
    }

    public void setInCompleteAddressInfo(boolean inCompleteAddressInfo) {
        this.inCompleteAddressInfo = inCompleteAddressInfo;
    }

    public boolean isInCompleteAddressInfo() {
        return (StringUtils2.isNullOrEmpty(this.getRhAddress1()) ||
               (StringUtils2.isNullOrEmpty(this.getRhCity())) 
              // ||
              // (StringUtils2.isNullOrEmpty(this.getRhPostalCode()))
               );
    }
}
