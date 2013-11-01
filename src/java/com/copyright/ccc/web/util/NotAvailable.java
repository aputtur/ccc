package com.copyright.ccc.web.util;

import com.copyright.opi.data.StandardDO;

/*
 * Below is the text for emails alerting our business
 * people that a customer would like to have us intercede
 * for them and try to get permission for a work that
 * is listed as unavailable.
 *
 * Product:  <PRODUCT>
 *
 * Title:  <TITLE>
 * Publisher:  <PUBLISHER>
 * Author/Editor:  <AUTHOR_ED>   Vol/Ed:  <VOL>
 * ISBN/ISSN:  <ISBN>
 * Rightsholder:  <RIGHTSHOLDER>
 * Pub'n Yr:  <PUB_YEAR>
 *
 * Name:  <NAME>
 * E-Mail:  <EMAIL_ADDRESS>
 * Company:  <COMPANY>
 * Phone:  <PHONE>
 * City:  <CITY>
 * State:  <STATE>
 * Annual License:  <LICENSE>
 * Additional Info: <ADDITIONAL_INFO>
 */

public class NotAvailable extends StandardDO
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String _product = null;
	private String _title = null;
	private String _publisher = null;
	private String _author = null;
	private String _volume = null;
	private String _standard_number = null;
	private String _rightsholder = null;
	private String _publication_year = null;
	private String _name = null;
	private String _email_address = null;
	private String _company = null;
	private String _phone_number = null;
	private String _city = null;
	private String _state = null;
	private String _annual_license = null;
	private String _additional_info = null;
    
    //  2009-11-05  MSJ
    //  Additional fields for display purposes only (at this point)
    //  for the Summit project.
    
	private String _idnoLabel = "ISBN/ISSN";
	private String _series = null;
	private String _seriesNumber = null;
	private String _publicationType = null;
	private String _pages = null;
	private String _country = null;
	private String _language = null;
    
    //  Accessors...  setters.

	public void setProduct( String s )                { _product = s;          }
	public void setTitle( String s )                  { _title = s;            }
	public void setPublisher( String s )              { _publisher = s;        }
	public void setAuthor( String s )                 { _author = s;           }
	public void setVolume( String s )                 { _volume = s;           }
	public void setStandardNumber( String s )         { _standard_number = s;  }
	public void setRightsholder( String s )           { _rightsholder = s;     }
	public void setPublicationYear( String s )        { _publication_year = s; }
	public void setRequesterName( String s )          { _name = s;             }
	public void setRequesterEmailAddress( String s )  { _email_address = s;    }
	public void setRequesterCompany( String s )       { _company = s;          }
	public void setRequesterPhoneNumber( String s )   { _phone_number = s;     }
	public void setRequesterCity( String s )          { _city = s;             }
	public void setRequesterState( String s )         { _state = s;            }
	public void setRequesterAnnualLicense( String s ) { _annual_license = s;   }
    public void setAdditionalInfo( String s )         { _additional_info = s;   }
	
	//  Additional metadata fields for Summit.
	
	public void setIdnoLabel(String s)       { _idnoLabel = s;       }
	public void setSeries(String s)          { _series = s;          }
	public void setSeriesNumber(String s)    { _seriesNumber = s;    }
	public void setPublicationType(String s) { _publicationType = s; }
	public void setPages(String s)           { _pages = s;           }
	public void setCountry(String s)         { _country = s;         }
	public void setLanguage(String s)        { _language = s;        }
	
	//  ...getters.
	
	public String getProduct()                { return _product;          }
	public String getTitle()                  { return _title;            }
	public String getPublisher()              { return _publisher;        }
	public String getAuthor()                 { return _author;           }
	public String getVolume()                 { return _volume;           }
	public String getStandardNumber()         { return _standard_number;  }
	public String getRightsholder()           { return _rightsholder;     }
	public String getPublicationYear()        { return _publication_year; }
	public String getRequesterName()          { return _name;             }
	public String getRequesterEmailAddress()  { return _email_address;    }
	public String getRequesterCompany()       { return _company;          }
	public String getRequesterPhoneNumber()   { return _phone_number;     }
	public String getRequesterCity()          { return _city;             }
	public String getRequesterState()         { return _state;            }
	public String getRequesterAnnualLicense() { return _annual_license;   }
    public String getAdditionalInfo()         { return _additional_info;  }
    
    //  Additional metadata fields for Summit.
    
    public String getIdnoLabel()       { return _idnoLabel;       }
    public String getSeries()          { return _series;          }
    public String getSeriesNumber()    { return _seriesNumber;    }
    public String getPublicationType() { return _publicationType; }
    public String getPages()           { return _pages;           }
    public String getCountry()         { return _country;         }
    public String getLanguage()        { return _language;        }
}