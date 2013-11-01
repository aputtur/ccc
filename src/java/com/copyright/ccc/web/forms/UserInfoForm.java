package com.copyright.ccc.web.forms;

import com.copyright.ccc.web.CCValidatorForm;

/**
 * Hold data about a specific CCC user.
 * This is a user administrator form.
 */
public class UserInfoForm extends CCValidatorForm
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String ON = "ON";
    private static final String OFF = "OFF";
    
    private String _onHold;
    private String _isLocked;
    private String _isRightsholder;
    
    //  I am combining address info here I think, as well.  That way
    //  I can pass the data along within a group of pages that are only
    //  viable for users with a specific administrator role.  I could
    //  reuse existing pages but I would rather keep them separate.
    
    private String _title;
    private String _firstName;
    private String _lastName;
    private String _middleName;
    private String _address1;
    private String _address2;
    private String _address3;
    private String _country;
    private String _postalCode;
    private String _city;
    private String _state;
    private String _phone;     
    private String _extension;    
    private String _fax;   
    private String _personType;
    private String _invoiceFlag;
    private String _pwd;
    private String _pwdConfirm;
    private String _emailAddress;
    private String _department;
    private String _jobTitle;
    private String _username;
    
    //  Our constructor.  Initialize our object values.
    
    public UserInfoForm() {
        _onHold = OFF;
        _isLocked = OFF;
        _isRightsholder = OFF;
        _invoiceFlag = OFF;
        _pwd = null;
        _emailAddress = null;
    }
    
    //  Accessors...  start with the GETTERS...
    
    public String getOnHold() { 
        if (_onHold == null) _onHold = OFF;
        return _onHold; 
    }
    
    public String getIsLocked() { 
        if (_isLocked == null) _onHold = OFF;
        return _isLocked; 
    }
    
    public String getIsRightsholder() { 
        if (_isRightsholder == null) _isRightsholder = OFF;
        return _isRightsholder; 
    }
    
    public String getInvoiceFlag() {
        if (_invoiceFlag == null) _invoiceFlag = OFF;
        return _invoiceFlag;
    }
    
    public String getTitle() { return _title; }
    public String getFirstName() { return _firstName; }
    public String getLastName() { return _lastName; }
    public String getMiddleName() { return _middleName; }
    public String getAddress1() { return _address1; }
    public String getAddress2() { return _address2; }
    public String getAddress3() { return _address3; }
    public String getCity() { return _city; }
    public String getState() { return _state; }
    public String getZipCode() { return _postalCode; }
    public String getCountry() { return _country; }
    public String getPhone() { return _phone; }
    public String getExtension() { return _extension; }
    public String getFax() { return _fax; }
    public String getPersonType() { return _personType; }
    public String getPassword() { return null; }
    public String getPasswordConfirm() { return null; }
    public String getEmailAddress() { return _emailAddress; }
    public String getDepartment() { return _department; }
    public String getJobTitle() { return _jobTitle; }
    public String getUsername() { return _username; }
    
    //  Accessors... and finish with the SETTERS...
    
    public void setOnHold(String flag) {
        if (flag == null) {
            _onHold = OFF;
        }
        else {
            _onHold = flag;
        }
    }

    public void setIsLocked(String flag) {
        if (flag == null) {
            _isLocked = OFF;
        }
        else {
            _isLocked = flag;
        }
    }
    
    public void setIsRightsholder(String flag) {
        if (flag == null) {
            _isRightsholder = OFF;
        }
        else {
            _isRightsholder = flag;
        }
    }
    
    public void setInvoiceFlag(String flag) {
        if (flag == null) {
            _invoiceFlag = OFF;
        }
        else {
            _invoiceFlag = flag;
        }
    }
    
    public void setTitle(String title) { _title = title; }
    public void setFirstName(String firstName) { _firstName = firstName; }
    public void setLastName(String lastName) { _lastName = lastName; }
    public void setMiddleName(String middleName) { _middleName = middleName; }
    public void setAddress1(String address1) { _address1 = address1; }
    public void setAddress2(String address2) { _address2 = address2; }
    public void setAddress3(String address3) { _address3 = address3; }
    public void setCity(String city) { _city = city; }
    public void setState(String state) { _state = state; }
    public void setZipCode(String zipCode) { _postalCode = zipCode; }
    public void setCountry(String country) { _country = country; }
    public void setPhone(String phone) { _phone = phone; }
    public void setExtension(String extension) { _extension = extension; }
    public void setFax(String fax) { _fax = fax; }
    public void setPersonType(String personType) { _personType = personType; }
    public void setPassword(String pwd) { _pwd = pwd; }
    public void setPasswordConfirm(String pwdConfirm) { _pwdConfirm = pwdConfirm; }
    public void setEmailAddress(String emailAddress) { _emailAddress = emailAddress; }
    public void setDepartment(String department) { _department = department; }
    public void setJobTitle(String jobTitle) { _jobTitle = jobTitle; }
    public void setUsername(String username) { _username = username; }
    
    public boolean passwordsMatch() {
        return _pwd.equals(_pwdConfirm);
    }
    
    public boolean validPassword() {
        return (((_pwd != null) && (_pwd.length() > 5)) && ((_pwdConfirm != null) && (_pwdConfirm.length() > 5)));
    }
    
    public String tellMeASecret() {
        return _pwd;
    }
}