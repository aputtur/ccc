package com.copyright.ccc.web.forms;

import java.util.Collection;

import org.apache.struts.validator.ValidatorForm;

import com.copyright.ccc.web.util.DropDownElement;
import com.copyright.ccc.web.util.WebUtils;

/*
 *
 */
public class UpdateAddressActionForm extends ValidatorForm
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Select lists
    private Collection<DropDownElement> titles;
    private Collection<DropDownElement> states;
    private Collection<DropDownElement> users;
    private Collection<DropDownElement> countries;
    
    // form values
    private String title;
    private String firstName;
    private String lastName;
    private String middle;
    private String address1;
    private String address2;
    private String address3;
    private String country;
    private String zipCode;
    private String city;
    private String state;    
    private String phone;     
    private String extension;    
    private String fax;   
    private String user;
    private boolean invoice;
    
    private String sbmt; // if page was submitted value = "true"
    
    public UpdateAddressActionForm() { this.initUI(); }
    
    
    /*
    * Define the selection lists for struts select tag
    */
    private void initUI() {
       this.titles = WebUtils.getTitleOptions();
       this.states = WebUtils.getStateOptions();
       this.users = WebUtils.getUserOptions();
       this.countries = WebUtils.getCountryOptions();
    }

    public void setTitles(Collection<DropDownElement> titles) {
        this.titles = titles;
    }

    public Collection<DropDownElement> getTitles() {
        return titles;
    }

    public void setStates(Collection<DropDownElement> states) {
        this.states = states;
    }

    public Collection<DropDownElement> getStates() {
        return states;
    }

    public void setUsers(Collection<DropDownElement> users) {
        this.users = users;
    }

    public Collection<DropDownElement> getUsers() {
        return users;
    }

    public void setCountries(Collection<DropDownElement> countries) {
        this.countries = countries;
    }

    public Collection<DropDownElement> getCountries() {
        return countries;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setMiddle(String middle) {
        this.middle = middle;
    }

    public String getMiddle() {
        return middle;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getAddress3() {
        return address3;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getFax() {
        return fax;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setInvoice(boolean invoice) {
        this.invoice = invoice;
    }

    public boolean isInvoice() {
        return invoice;
    }

    public void setSbmt(String sbmt) {
        this.sbmt = sbmt;
    }

    public String getSbmt() {
        return sbmt;
    }
}
