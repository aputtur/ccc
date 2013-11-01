package com.copyright.ccc.web.forms;

import com.copyright.ccc.web.CCValidatorForm;
import com.copyright.ccc.web.util.NotAvailable;

public class NotAvailableEmailForm extends CCValidatorForm 
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private NotAvailable _data = null;

    public NotAvailableEmailForm() { _data = new NotAvailable(); }
    public NotAvailableEmailForm( NotAvailable obj )
    {
        _data = obj;
    }

    //  Use these to set/get the complete chunk of data that
    //  we use to send the email message...

    public void setData( NotAvailable obj ) { _data = obj; }
    public NotAvailable getData() { return _data; }
    
    //  Individual getters/setters that map to the same methods
    //  in the NotAvailable object.
    
    public void setProduct( String s )                { _data.setProduct( s );                }
    public void setTitle( String s )                  { _data.setTitle( s );                  }
    public void setPublisher( String s )              { _data.setPublisher( s );              }
    public void setAuthor( String s )                 { _data.setAuthor( s );                 }
    public void setVolume( String s )                 { _data.setVolume( s );                 }
    public void setStandardNumber( String s )         { _data.setStandardNumber( s );         }
    public void setRightsholder( String s )           { _data.setRightsholder( s );           }
    public void setPublicationYear( String s )        { _data.setPublicationYear( s );        }
    public void setRequesterName( String s )          { _data.setRequesterName( s );          }
    public void setRequesterEmailAddress( String s )  { _data.setRequesterEmailAddress( s );  }
    public void setRequesterCompany( String s )       { _data.setRequesterCompany( s );       }
    public void setRequesterPhoneNumber( String s )   { _data.setRequesterPhoneNumber( s );   }
    public void setRequesterCity( String s )          { _data.setRequesterCity( s );          }
    public void setRequesterState( String s )         { _data.setRequesterState( s );         }
    public void setRequesterAnnualLicense( String s ) { _data.setRequesterAnnualLicense( s ); }
    public void setAdditionalInfo( String s)          { _data.setAdditionalInfo( s ); }
    
    public String getProduct()                { return _data.getProduct();                }
    public String getTitle()                  { return _data.getTitle();                  }
    public String getPublisher()              { return _data.getPublisher();              }
    public String getAuthor()                 { return _data.getAuthor();                 }
    public String getVolume()                 { return _data.getVolume();                 }
    public String getStandardNumber()         { return _data.getStandardNumber();         }
    public String getRightsholder()           { return _data.getRightsholder();           }
    public String getPublicationYear()        { return _data.getPublicationYear();        }
    public String getRequesterName()          { return _data.getRequesterName();          }
    public String getRequesterEmailAddress()  { return _data.getRequesterEmailAddress();  }
    public String getRequesterCompany()       { return _data.getRequesterCompany();       }
    public String getRequesterPhoneNumber()   { return _data.getRequesterPhoneNumber();   }
    public String getRequesterCity()          { return _data.getRequesterCity();          }
    public String getRequesterState()         { return _data.getRequesterState();         }
    public String getRequesterAnnualLicense() { return _data.getRequesterAnnualLicense(); }
    public String getAdditionalInfo()         { return _data.getAdditionalInfo();         }
    
    //  2009-11-06  MSJ
    //  Adding getters to access the metadata being added
    //  for the Summit project.
    
    public String getIdnoLabel()       { return _data.getIdnoLabel();       }
    public String getSeries()          { return _data.getSeries();          }
    public String getSeriesNumber()    { return _data.getSeriesNumber();    }
    public String getPublicationType() { return _data.getPublicationType(); }
    public String getPages()           { return _data.getPages();           }
    public String getCountry()         { return _data.getCountry();         }
    public String getLanguage()        { return _data.getLanguage();        }
}