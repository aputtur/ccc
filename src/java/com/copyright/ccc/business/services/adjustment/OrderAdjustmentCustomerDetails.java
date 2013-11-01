package com.copyright.ccc.business.services.adjustment;

import java.io.Serializable;

/**
 * Class representing customer details in adjustments. 
 */
public class OrderAdjustmentCustomerDetails implements Serializable
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private long _accountNumber;
  private Long _partyId;
  private String _fullName;
  private String _companyName;
  private String _address1;
  private String _address2;
  private String _address3;
  private String _address4;
  private String _city;
  private String _state;
  private String _country;
  private String _zipCode;


  void setAccountNumber(long accountNumber)
  {
    this._accountNumber = accountNumber;
  }


  /**
   * Returns account number.
   */
  public long getAccountNumber()
  {
    return _accountNumber;
  }

  void setPartyId( Long partyId ) {
    this._partyId = partyId;
  }
  
  /**
    * Returns the party ID number.
    */
  public Long getPartyId() {
    return _partyId;
  }
  
  void setCompanyName(String companyName)
  {
    this._companyName = companyName;
  }
  
  
  /**
   * Returns company name.
   */
  public String getCompanyName()
  {
    return _companyName;
  }


  void setAddress1(String address1)
  {
    this._address1 = address1;
  }


  /**
   * Returns address line 1.
   */
  public String getAddress1()
  {
    return _address1;
  }


  void setAddress2(String address2)
  {
    this._address2 = address2;
  }


  /**
   * Returns address line 2.
   */
  public String getAddress2()
  {
    return _address2;
  }


  void setCity(String city)
  {
    this._city = city;
  }


  /**
   * Returns city.
   */
  public String getCity()
  {
    return _city;
  }


  void setState(String state)
  {
    this._state = state;
  }


  /**
   * Returns state code.
   */
  public String getState()
  {
    return _state;
  }


  void setCountry(String country)
  {
    this._country = country;
  }


  /**
   * Returns country.
   */
  public String getCountry()
  {
    return _country;
  }
  

  void setZipCode(String zipCode)
  {
    this._zipCode = zipCode;
  }


  /**
   * Returns zip code.
   */
  public String getZipCode()
  {
    return _zipCode;
  }

    
  void setFullName(String fullName)
  {
    this._fullName = fullName;
  }


  /**
   * Returns full name.
   */
  public String getFullName()
  {
    return _fullName;
  }


  void setAddress3(String address3)
  {
    this._address3 = address3;
  }


  /**
   * Returns address line 3.
   */
  public String getAddress3()
  {
    return _address3;
  }

  void setAddress4(String address4)
  {
    this._address4 = address4;
  }

  
  /**
   * Returns address line 4.
   */
  public String getAddress4()
  {
    return _address4;
  }
}
