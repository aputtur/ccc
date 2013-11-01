package com.copyright.ccc.web.forms;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.copyright.ccc.business.data.CCSearchCriteria;
import com.copyright.ccc.business.data.CCUser;
import com.copyright.ccc.business.data.Publication;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.CountryEnum;
import com.copyright.ccc.business.services.LangEnum;
import com.copyright.ccc.business.services.PermissionCategoryEnum;
import com.copyright.ccc.business.services.PubTypeEnum;
import com.copyright.ccc.business.services.search.SearchCriteriaImpl;
import com.copyright.ccc.business.services.search.WebServiceSearch;
import com.copyright.ccc.business.services.user.SearchPermissionTypeEnum;
import com.copyright.ccc.business.services.user.UserServices;
import com.copyright.ccc.web.util.PermSummaryTouDisplay;
import com.copyright.ccc.web.util.PermissionCategoryDisplay;

/*
 *  title:          SearchForm
 *  author:         Jessop
 *  date:           November 2006
 *
 *  description:    Form object to map to our various search forms.
 *                  SearchType should be one of PayPerUse, BusinessLic,
 *                  AcademicLic or Simple.  Or something like that anyway.
 *
 *		    Regarding state management:  In reality the state of
 *		    our search will be maintained by the search service
 *		    which is stashed somewhere in our user context.  For
 *		    the sake of ease-of-coding I am holding the search
 *		    results, sort value and page size in the form as well.
 *		    It will mirror what is in the state, and will make the
 *		    page display more consistent by referencing the form.
 *
 *		    Since the search results consist of EVERYTHING that
 *		    match (up to 100 of course) WE need to manage what
 *		    permissions are displayed to the user, hence all the
 *		    getAps, getDps, etc. stuff.
 *
 *	notes:	    I was going to have a few different forms for the
 *		    different phases of searching (search, search results
 *		    and permissions) but decided I might as well just use
 *		    the same form pushed along from page to page in the
 *		    session. 
 */

public class SearchForm extends BasicSearchForm
{
    //  Private constants...

	private static final long serialVersionUID = 1L;
	private static final String EMPTY_STRING    = "";
    private static final String ON              = "on";
    private static final String OFF             = "off";
    private static final int    MIN_FIELD_LEN   = 2;
    
    private static final Long APS_CATAGORYID   = 2L;
    private static final Long DPS_CATAGORYID   = 5L;
    private static final Long RLS_CATAGORYID   = 6L;
    private static final Long ECC_CATAGORYID   = 1L;
    private static final Long TRSILL_CATAGORYID   = 3L;
    private static final Long TRSPHOTO_CATAGORYID   = 4L;
    private static final Long AAS_CATAGORYID   = 8L;
    private static final Long DRA_CATAGORYID   = 9L;
    private static final Long ARS_CATAGORYID   = 10L;
    private static final Long RLR_CATAGORYID   = 7L;
    
    //  constants needed by Actions
    
    public static final String SIMPLE_SEARCH   = "simple";
    public static final String ADVANCED_SEARCH = "advanced";
    public static final String NORMAL_SOURCE   = "normal";
    public static final String OPENURL_SOURCE  = "openurl";
    public static final String PD_SOURCE       = "pd";
    
    // constants for Publication type
    public static final String ALL       = "all";
    public static final String BLOG       = "Blog";
    public static final String BOOK       = "Book";
    public static final String EBOOK       = "e-Book";
    public static final String EJOURNAL       = "e-Journal";
    public static final String JOURNAL       = "Journal";
    public static final String MONO       = "Monographic Series";
    public static final String NEWSLETTER       = "Newsletter";
    public static final String NEWSPAPER       = "Newspaper";
    public static final String OTHER       = "Other";
    public static final String WEBSITE       = "Website";
           
    //  Search Results and state management...
    
    private Publication[]       _results         = null;
    private transient List <PermissionCategoryDisplay> _permCatDisplay   = null;
    private transient PermissionCategoryDisplay _selectedPermCatDisplay = null;
    
    private String              _lastSearch      = null;
    private Publication         _selectedItem    = null;
    private String              _source          = null;
    private boolean             _orderExists     = false;
    private long                _purchaseId      = 0;
    private String selectedTou = null;
    private String selectedRRTou=null;
    private String selectedRRTouId=null;
    private long selectedRightInst = 0;
    private long selectedTpuInst = 0;
    
    private long selectedRightHolderInst = 0;
    private String selectedWrkInst = null;
    private long selectedCategoryId=0;
    
    private boolean permissionDirectAction = false;
    private String permissionDirectProduct;
    
    private String selectedPubYear = null;
    
    private boolean isBiactive = false;

    /*
     * the list of countries,languages,pub_types with which 
     * the user can filter a search
     */
    private String[] _countries = null;
    private String[] _languages = null;
    private String[] _pub_types = null;
    
    /*
     * the list of countries,languages,pub_types the user 
     * chose to use for filtering the search
     */
    private String[] _selectedCountries = null;
    private String[] _selectedPubTypes  = null;
    private String[] _selectedLanguages = null;

    private String[] _searchInstead = null;
    
    //  ***************************************************
    //  More form data...
    
    private CCSearchCriteria  _criteria = null;
    private String _year = null; //  Year of publication to look at.
    private boolean _isIssnSearch = false; //   Filter multiple results if ISSN.
    
    /*
     * the following replaces the individual variables that carried the on/off status of
     * each of the SearchPermissionType values
     */
    private Map<SearchPermissionTypeEnum, String> permissionTypeDisplaySetting = 
    	new HashMap<SearchPermissionTypeEnum, String>();
    
    //Added these 2 attributes for WorldCat button display in Summary page
    private String _worldCatUrl = ""; //Get the URL for WorldCat
    private boolean _worldCatDisplay = false; //To display button or not
        
    private String showPermissionTypes = "true";
    
    private boolean teleSalesUp = true;
    
    //added for pub to pub project
    private String selectedRlPermissionType = "";
    private String selectedOfferChannel = "";
    private String selectedRlPubCode = "";
  
	/**
	 * @return the selectedRlPubCode
	 */
	public String getSelectedRlPubCode()
	{
		return selectedRlPubCode;
	}

	/**
	 * @param selectedRlPubCode the selectedRlPubCode to set
	 */
	public void setSelectedRlPubCode(String selectedRlPubCode)
	{
		this.selectedRlPubCode = selectedRlPubCode;
	}

	public PermissionCategoryDisplay getSelectedPermCatDisplay() {
		return _selectedPermCatDisplay;
	}

	public void setSelectedPermCatDisplay(PermissionCategoryDisplay permCatDisplay) {
		_selectedPermCatDisplay = permCatDisplay;
	}
	public void setSelectedPermCatDisplayIndex(int index) {
		if (_permCatDisplay != null ) {
			if (_permCatDisplay.size() <= index) {
				_selectedPermCatDisplay = null;
			} else {
				_selectedPermCatDisplay = _permCatDisplay.get(index);
			}
		} else {
			_selectedPermCatDisplay = null;
		}
	}
	public boolean isTeleSalesUp() {
		return teleSalesUp;
	}
    
    public boolean getTeleSalesUp() {
    	return teleSalesUp;
    }

	public void setTeleSalesUp(boolean teleSalesUp) {
		this.teleSalesUp = teleSalesUp;
	}

	public String getShowPermissionTypes() {
		return showPermissionTypes;
	}

	public void setShowPermissionTypes(String showPermissionTypes) {
		this.showPermissionTypes = showPermissionTypes;
	}

	public SearchForm()
    {
        this.clearState();
    }

    public void setSearchInstead( String[] items )
    {
        _searchInstead = items;
    }
    
    //  Getters.
    
    public Publication[] getResults()          { return _results;                        }
    public Publication   getResult( String s ) { return _results[Integer.parseInt( s )]; }
    public Publication   getResult( int i )    { return _results[i];                     }
    public String        getLastSearch()       { return _lastSearch;                     }
    public Publication   getSelectedItem()     { 
    	return _selectedItem;
    }
    
    public String getSort()       { return String.valueOf(_criteria.getSortType());   }
    public String getSearchType() { return String.valueOf(_criteria.getSearchType()); }
    
    public String getTitleOrStdNo()  { return _criteria.getTitleOrStdNo(); }
    public String getPublisher()     { return _criteria.getPublisher();    }
    public String getAuthorEditor()  { return _criteria.getAuthorEditor(); }
    public String getVolume()        { return _criteria.getVolume();       }
    public String getEdition()       { return _criteria.getEdition();      }
    public String[] getSearchInstead() { return _searchInstead; }

    /**
     * Returns "on" if permission display for this category is turned on, null or "off" otherwise
     */
    public String getAps()	{ return permissionTypeDisplaySetting.get(SearchPermissionTypeEnum.ACADEMIC_TRX_PHOTOCOPY);  }
    /**
     * Returns "on" if permission display for this category is turned on, null or "off" otherwise
     */
    public String getDps()	{return permissionTypeDisplaySetting.get(SearchPermissionTypeEnum.BUSINESS_TRX_DIGITAL);}
    /**
     * Returns "on" if permission display for this category is turned on, null or "off" otherwise
     */
    public String getEcc()  { return permissionTypeDisplaySetting.get(SearchPermissionTypeEnum.ACADEMIC_TRX_DIGITAL);  }
    /**
     * Returns "on" if permission display for this category is turned on, null or "off" otherwise
     */
    public String getRls()	{ return permissionTypeDisplaySetting.get(SearchPermissionTypeEnum.REPUBLICATION); }
    /**
     * Returns "on" if permission display for this category is turned on, null or "off" otherwise
     */
    public String getTrsIll()	{ return permissionTypeDisplaySetting.get(SearchPermissionTypeEnum.BUSINESS_TRX_ILL);}
    /**
     * Returns "on" if permission display for this category is turned on, null or "off" otherwise
     */
    public String getTrsPhoto()	{ return permissionTypeDisplaySetting.get(SearchPermissionTypeEnum.BUSINESS_TRX_PHOTOCOPY);}
    /**
     * Returns "on" if permission display for this category is turned on, null or "off" otherwise
     */
    public String getAas()	{ return permissionTypeDisplaySetting.get(SearchPermissionTypeEnum.BUSINESS_LIC_PHOTOCOPY);}
    /**
     * Returns "on" if permission display for this category is turned on, null or "off" otherwise
     */
    public String getDra()	{ return permissionTypeDisplaySetting.get(SearchPermissionTypeEnum.BUSINESS_LIC_DIGITAL); }
    /**
     * Returns "on" if permission display for this category is turned on, null or "off" otherwise
     */
    public String getArs() { return permissionTypeDisplaySetting.get(SearchPermissionTypeEnum.ACADEMIC_LIC_DIGITAL);}
    /**
     * Returns "on" if permission display for this category is turned on, null or "off" otherwise
     */
    public String getRlr() { return permissionTypeDisplaySetting.get(SearchPermissionTypeEnum.RIGHTSLINK_REPRINT );}   
    
    /**
     * Returns true if permission display for this category is turned on
     */
    public Boolean getIsShowAps()	{ if (getAps()==null){ return false;} return getAps().equals(ON);  }
    /**
     * Returns true if permission display for this category is turned on
     */
    public Boolean getIsShowDps()	{ if (getDps()==null){ return false;} return getDps().equals(ON);  }
    /**
     * Returns true if permission display for this category is turned on
     */
    public Boolean getIsShowEcc()  { if (getEcc()==null){ return false;} return getEcc().equals(ON);  }
    /**
     * Returns true if permission display for this category is turned on
     */
    public Boolean getIsShowRls()	{ if (getRls()==null){ return false;} return getRls().equals(ON);  }
    /**
     * Returns true if permission display for this category is turned on
     */
    public Boolean getIsShowTrsIll()	{ if (getTrsIll()==null){ return false;} return getTrsIll().equals(ON);  }
    /**
     * Returns true if permission display for this category is turned on
     */
    public Boolean getIsShowTrsPhoto()	{ if (getTrsPhoto()==null){ return false;} return getTrsPhoto().equals(ON);  }
    /**
     * Returns true if permission display for this category is turned on
     */
    public Boolean getIsShowAas()	{ if (getAas()==null){ return false;}return getAas().equals(ON);  }
    /**
     * Returns true if permission display for this category is turned on
     */
    public Boolean getIsShowDra()	{ if (getDra()==null){ return false;}return getDra().equals(ON);  }
    /**
     * Returns true if permission display for this category is turned on
     */
    public Boolean getIsShowArs() { if (getArs()==null){ return false;}return getArs().equals(ON); }
    /**
     * Returns true if permission display for this category is turned on
     */
    public Boolean getIsShowRlr() { if (getRlr()==null){ return false;}return getRlr().equals(ON); }
    
    /**
     * returns an array of countries for display on the search criteria screen
     */
    public String[] getCountries()        { return _countries; }
    /**
     * returns an array of publication types for display on the search criteria screen
     */
    public String[] getPublicationTypes() { return _pub_types; }
    /**
     * returns an array of languages for display on the search criteria screen
     */
    public String[] getLanguages()        { return _languages; }
    
    /**
     * returns true if the countries list or pub_types or languages has not been
     * populated
     * @return
     */
    public boolean needsDropdownData() {
        return ((_countries == null || _countries.length == 0) ||
                (_pub_types == null || _pub_types.length == 0) ||
                (_languages == null || _languages.length == 0));
    }

    /**
     * array of which countries the user selected
     * @return
     */
    public String[] getSelectedCountries() { return _selectedCountries;    }
    /**
     * array of which languages the user selected
     * @return
     */
    public String[] getSelectedLanguages() { return _selectedLanguages;    }
    /**
     * array of which pub types the user selected
     * @return
     */
    public String[] getSelectedPubTypes()  { return _selectedPubTypes;     }

    public String   getSeries()            { return _criteria.getSeries(); }
    
    public String getSelectedPermissionTypesWT()
    {
    	//PermissionCategoryDisplay permCat = getSelectedPermCatDisplay() ;
    	Map<SearchPermissionTypeEnum, String> permCat = this.permissionTypeDisplaySetting;
    	String permTouSelected = "" ;
    	    	
    	if (permCat != null)
    	{
    		//Iterator it = permCat.i
    		//List<PermSummaryTouDisplay> permTOU =  permCat.getPermSummaryTouDisplays();
    		
    		//Set host = permCat.keySet();
    		//Iterator<SearchPermissionTypeEnum> it = host.iterator();
    		
    		for (SearchPermissionTypeEnum host: permCat.keySet())
    		{
    			if (permCat.get(host) != null && permCat.get(host).equalsIgnoreCase("on"))
    			{
    				if (host.compareTo(SearchPermissionTypeEnum.ACADEMIC_TRX_DIGITAL) == 0)
    				{
    					permTouSelected = permTouSelected + "0;";
    				}
    				else if (host.compareTo(SearchPermissionTypeEnum.ACADEMIC_TRX_PHOTOCOPY) == 0)
    				{
    					permTouSelected = permTouSelected + "1;";
    				}
    				else if (host.compareTo(SearchPermissionTypeEnum.BUSINESS_TRX_ILL) == 0)
    				{
    					permTouSelected = permTouSelected + "2;";
    				}
    				else if (host.compareTo(SearchPermissionTypeEnum.BUSINESS_TRX_PHOTOCOPY) == 0)
    				{
    					permTouSelected = permTouSelected + "3;";
    				}
    				else if (host.compareTo(SearchPermissionTypeEnum.BUSINESS_TRX_DIGITAL) == 0)
    				{
    					permTouSelected = permTouSelected + "4;";
    				}
    				else if (host.compareTo(SearchPermissionTypeEnum.REPUBLICATION) == 0)
    				{
    					permTouSelected = permTouSelected + "5;";
    				}
    				else if (host.compareTo(SearchPermissionTypeEnum.BUSINESS_LIC_PHOTOCOPY) == 0)
    				{
    					permTouSelected = permTouSelected + "6;";
    				}
    				else if (host.compareTo(SearchPermissionTypeEnum.BUSINESS_LIC_DIGITAL) == 0)
    				{
    					permTouSelected = permTouSelected + "7;";
    				}
    				else if (host.compareTo(SearchPermissionTypeEnum.ACADEMIC_LIC_DIGITAL) == 0)
    				{
    					permTouSelected = permTouSelected + "8;";
    				}
    			}
    		}
    	    	
    	}
    	if(permTouSelected != null && !permTouSelected.equalsIgnoreCase(""))
    	{
    		return permTouSelected.substring(0, (permTouSelected.length()-1));
    	}
    	else
    	{
    		return permTouSelected;
    	}
    }
    
    public String getSelectedLanguagesWT()  
    { 
    	String[] selectedLanguagesList = getSelectedLanguages();
    	String selectedLanguages = "";
    	    	
    	if (selectedLanguagesList != null) {
    		for(int i = 0; i < selectedLanguagesList.length; i++) 
    		{
    			if (selectedLanguagesList[i].toString().equalsIgnoreCase(ALL))
    	    	{
    	    		return "0";
    	    	}
    			
    		}
    	}
    	
    	if (selectedLanguagesList != null && selectedLanguagesList.length == 1)
    	{
    		return LangEnum.getLangCodeForName(selectedLanguagesList[0].toString());
    	}
    	
    	if (selectedLanguagesList != null) {
    		for(int i = 0; i < selectedLanguagesList.length; i++) 
    		{
    			if ( selectedLanguages != null && !selectedLanguages.equals(""))
    				{
    					selectedLanguages = selectedLanguages + ";" + 
    					LangEnum.getLangCodeForName(selectedLanguagesList[i].toString());
    				}
    				else
    				{
    					selectedLanguages = LangEnum.getLangCodeForName(selectedLanguagesList[i].toString());
    				}
    		}
    	}
    	
    	return selectedLanguages;    	        	    	
    }
    
    public String getSelectedCountriesWT()  
    { 
    	String[] selectedCountriesList = getSelectedCountries();
    	String selectedCountries = "";
    	String test = "";
    	String cntry = "";
    	
    	if (selectedCountriesList != null) {
    		for(int i = 0; i < selectedCountriesList.length; i++) 
    		{
    			if (selectedCountriesList[i].toString().equalsIgnoreCase(ALL))
    	    	{
    	    		return "0";
    	    	}
    			
    		}
    	}
    	
    	if (selectedCountriesList != null && selectedCountriesList.length == 1)
    	{
    		return CountryEnum.getCountryCodeForName(selectedCountriesList[0].toString());
    	}
    	
    	if (selectedCountriesList != null) {
    		for(int i = 0; i < selectedCountriesList.length; i++) 
    		{
    			if ( selectedCountries != null && !selectedCountries.equals(""))
    				{
    					selectedCountries = selectedCountries + ";" + 
    											CountryEnum.getCountryCodeForName(selectedCountriesList[i].toString());
    				}
    				else
    				{
    					selectedCountries = CountryEnum.getCountryCodeForName(selectedCountriesList[i].toString());
    				}
    		}
    	}
    	
    	return selectedCountries;    	        	
    }
    
    public String getSelectedPubTypesWT()  
    { 
    	String[] pubTypes = getSelectedPubTypes();
    	String pubTypesCode = "";
    	
    	if (pubTypes != null) {
    		for(int i = 0; i < pubTypes.length; i++) 
    		{
    			if (pubTypes[i].toString().equalsIgnoreCase("all"))
    	    	{
    	    		return "0";
    	    	}
    			
    		}
    	}
    	
    	if (pubTypes != null && pubTypes.length == 1)
    	{
    		return PubTypeEnum.getPubTypeCodeForName(pubTypes[0].toString());
    	}
    	
    	if (pubTypes != null) {
    		for(int i = 0; i < pubTypes.length; i++) 
    		{
    			if ( pubTypesCode != null && !pubTypesCode.equals(""))
    				{
    					pubTypesCode = pubTypesCode + ";" + 
    								PubTypeEnum.getPubTypeCodeForName(pubTypes[i].toString());
    				}
    				else
    				{
    					pubTypesCode = PubTypeEnum.getPubTypeCodeForName(pubTypes[i].toString());
    				}
    		}
    	}
    	
    	return pubTypesCode;    	    	     
   
    }
    
    public String getIsSelected(String[] items, String value) {
        if (items != null && value != null) {
            for (int i = 0; i < items.length; i++) {
                if (value.equals(items[i])) return "selected";   
            }
        }
        if (items == null && (value != null && "all".equals(value))) {
            return "selected";   
        }
        return "";
    }
    
    //  2012-11-12  MSJ
    //  Removed date defaulting to current year.  Allow null date... 
    //  this seems to only be used for OpenURL.

    public String getYear() 
    {
        return _year != null ? _year.substring(0, 4) : _year;
    }
    
    public CCSearchCriteria getSearchCriteria() 
    { 
        _criteria.setCountries(_selectedCountries);   
        _criteria.setLanguages(_selectedLanguages);
        _criteria.setPubTypes(_selectedPubTypes);
        
        computeAndSetCategoriesToDisplay();
        
        return _criteria;    
    }

    /**
     * Rebuilds the SearchCriteria object's displayable permissions list based on which 
     * types of use have been selected by the user for display.
     */
    private void computeAndSetCategoriesToDisplay() {
        HashMap<Long,String> displayablePermissions = new HashMap<Long,String>();

        displayablePermissions.put(PermissionCategoryEnum.APS_CATAGORYID.getId(), getAps());
        displayablePermissions.put(PermissionCategoryEnum.DPS_CATAGORYID.getId(), getDps());
        displayablePermissions.put(PermissionCategoryEnum.RLS_CATAGORYID.getId(), getRls());
        displayablePermissions.put(PermissionCategoryEnum.RLR_CATAGORYID.getId(), getRlr());
        displayablePermissions.put(PermissionCategoryEnum.ECC_CATAGORYID.getId(), getEcc());
        displayablePermissions.put(PermissionCategoryEnum.TRSILL_CATAGORYID.getId(), getTrsIll());
        displayablePermissions.put(PermissionCategoryEnum.TRSPHOTO_CATAGORYID.getId(), getTrsPhoto());
        displayablePermissions.put(PermissionCategoryEnum.AAS_CATAGORYID.getId(), getAas());
        displayablePermissions.put(PermissionCategoryEnum.DRA_CATAGORYID.getId(), getDra());
        displayablePermissions.put(PermissionCategoryEnum.ARS_CATAGORYID.getId(), getArs());
        
        _criteria.setDisplayablePermissions(displayablePermissions);
    }
    
    public String     getSource()      { return _source;      }
    public boolean    getOrderExists() { return _orderExists; }
    public long       getPurchaseId()  { return _purchaseId;  }
    
    
    public String getSearchText()
    {
        String txt = null;
        
        txt = _criteria.getTitleOrStdNo();
        
        if ((_criteria.getPublisher() != null) &&
            !_criteria.getPublisher().equals(""))
        {
        	if ((txt != null) && !txt.equals(""))
        	{
        		txt += " and ";
        		txt += _criteria.getPublisher();
        	}
        	else txt = _criteria.getPublisher();
        }

        return txt;
    }
    
    public String getSearchTextWT()
    {
        String txt = null;
        int i = 0;
        
        if ((_criteria.getTitleOrStdNo() != null) &&
                !_criteria.getTitleOrStdNo().equals(""))
            {
            	i++;
            }
        if ((_criteria.getSeries() != null) &&
                !_criteria.getSeries().equals(""))
            {
        		i++;
            }
        if ((_criteria.getAuthorEditor() != null) &&
                !_criteria.getAuthorEditor().equals(""))
            {
        		i++;
            }
        if ((_criteria.getPublisher() != null) &&
                !_criteria.getPublisher().equals(""))
            {
        		i++;
            }
                       
        if ((_criteria.getTitleOrStdNo() != null) &&
                !_criteria.getTitleOrStdNo().equals(""))
            {
            	txt = _criteria.getTitleOrStdNo() ;
            	
            	if ( i == 1)
            	{
            		return txt;
            	}
            }
                
        if ((_criteria.getSeries() != null) &&
                !_criteria.getSeries().equals(""))
            {
            	if ((txt != null) && !txt.equals(""))
            	{
            		txt += ";";
            		txt += _criteria.getSeries();
            	}
            	else 
            	{
            		txt = _criteria.getSeries();
            	}
            	
            	if ( i == 1)
            	{
            		return txt;
            	}
            	
            	
            }
        else if ((txt != null) && !txt.equals(""))
        {
        	txt = txt + ";";
        }
                
        if ((_criteria.getAuthorEditor() != null) &&
                !_criteria.getAuthorEditor().equals(""))
            {
            	if ((txt != null) && !txt.equals(""))
            	{
            		txt += ";";
            		txt += _criteria.getAuthorEditor();
            	}
            	else txt = _criteria.getAuthorEditor();
            	
            	if ( i == 1)
            	{
            		return txt;
            	}
            }
        else if ((txt != null) && !txt.equals(""))
        {
        	txt = txt + ";";
        }
        
        if ((_criteria.getPublisher() != null) &&
            !_criteria.getPublisher().equals(""))
        {
        	if ((txt != null) && !txt.equals(""))
        	{
        		txt += ";";
        		txt += _criteria.getPublisher();
        	}
        	else txt = _criteria.getPublisher();
        	
        	if ( i == 1)
        	{
        		return txt;
        	}
        }

        return txt;
    }
        
    public Publication getPublication(long workInst){
        if (_results == null) {
            return null;
        }
        for (Publication pub : _results) {
            if (pub.getWrkInst() == workInst){
                return pub;
            }
        }
        return null;
    }
    
   
    //*********************************
    //	This is used ONLY in the SearchAction.
    
    public void setPermCatDisplay(List <PermissionCategoryDisplay> permCatDisplay) { _permCatDisplay = permCatDisplay; }
    public List <PermissionCategoryDisplay> getPermCatDisplay() { return _permCatDisplay; }
    
    public String getWorldCatUrl() { return _worldCatUrl; }
    
    public boolean getWorldCatDisplay() { return _worldCatDisplay; }
    
    //*********************************
    // Setters.
    
    public void setWorldCatUrl( String worldCat ) { _worldCatUrl = worldCat; }
    public void setWorldCatDisplay(boolean worldCatDisplay) { _worldCatDisplay = worldCatDisplay; }
    
    public void setResults( Publication[] a )    { _results = a;                      }
    public void setLastSearch( String s )        { _lastSearch = s;                   }
    public void setSelectedItem( Publication p ) { _selectedItem = p;                 }
    
    //  Summit dropdown list setters...
    
    public void setCountries(String[] a)        { _countries = a; }
    public void setLanguages(String[] a)        { _languages = a; }
    public void setPublicationTypes(String[] a) { _pub_types = a; }
    
    //  Summit value setters...
    
    public void setSelectedCountries(String[] a) { _selectedCountries = a; }
    public void setSelectedLanguages(String[] a) { _selectedLanguages = a; }
    public void setSelectedPubTypes(String[] a)  { _selectedPubTypes = a;  }
    public void setSeries(String s)              { _criteria.setSeries(s); }
    
    public void setSort( String s ) 
    { 
        int st = 0;
        
        try
        {
            st = Integer.parseInt( s );
        }
        catch (NumberFormatException e)
        {
            //  No need to do anything.
        }
        _criteria.setSortType( st );
        
    }
    public void setSearchType( String s )   { _criteria.setSearchType( Integer.parseInt( s ) ); }
    public void setTitleOrStdNo( String s ) { _criteria.setTitleOrStdNo( s );                   }
    public void setPublisher( String s )    { _criteria.setPublisher( s );                      }
    public void setAuthorEditor( String s ) { _criteria.setAuthorEditor( s );                   }
    public void setVolume( String s )       { _criteria.setVolume( s );                         }
    public void setEdition( String s )      { _criteria.setEdition( s );                        }
    public void setWrWrkInst( String s )    { _criteria.setWrWrkInst( s );						}
    
    
    
    public String getSelectedTou() {
		return selectedTou;
	}

	public void setSelectedTou(String selectedTou) {
		this.selectedTou = selectedTou;
	}
	
	public String getSelectedRRTou() {
		return selectedRRTou;
	}

	public void setSelectedRRTou(String selectedRRTou) {
		this.selectedRRTou = selectedRRTou;
	}
	
	public long getSelectedRightInst() {
		return selectedRightInst;
	}
	
	
	public void setSelectedRightInst(long selectedRightInst) {
		this.selectedRightInst = selectedRightInst;
	}
	public long getSelectedTpuInst() {
		return selectedTpuInst;
	}
	
	
	public void setSelectedTpuInst(long selectedTpuInst) {
		this.selectedTpuInst = selectedTpuInst;
	}
	
	
	public long getSelectedRightHolderInst() {
		return selectedRightHolderInst;
	}

	public void setSelectedRightHolderInst(long selectedRightHolderInst) {
		this.selectedRightHolderInst = selectedRightHolderInst;
	}
	
	public void setSelectedRRTouId(String selectedRRTouId) {
		this.selectedRRTouId = selectedRRTouId;
	}

	public String getSelectedRRTouId() {
		return selectedRRTouId;
	}

	public void setSelectedCategoryId(long selectedCategoryId) {
		this.selectedCategoryId = selectedCategoryId;
	}

	public long getSelectedCategoryId() {
		return selectedCategoryId;
	}

	public String getSelectedWrkInst() {
		return selectedWrkInst;
	}

	public void setSelectedWrkInst(String selectedWrkInst) {
		this.selectedWrkInst = selectedWrkInst;
	}
	
	public String getSelectedPubYear() {
		return selectedPubYear;
	}

	public void setSelectedPubYear(String selectedPubYear) {
		this.selectedPubYear = selectedPubYear;
	}

	public void setSearchCriteria(CCSearchCriteria sc) 
    {
        int n = 0;
        
        //  2009-12-07  MSJ
        //  Because of how we handle list boxes in forms, we need to
        //  go ahead and remap the criteria data so we can also check for
        //  and set, previously selected items in our form.
        
        if (sc.getCountries() != null || sc.getCountries().size() > 0) {
            if (!sc.getCountries().get(0).equals("all")) {
                _selectedCountries = new String[sc.getCountries().size()];
                
                n = 0;
        
                for (String country : sc.getCountries()) {
                    _selectedCountries[n++] = country;
                }
            }
        }
        if (sc.getLanguages() != null || sc.getLanguages().size() > 0) {
            if (!sc.getLanguages().get(0).equals("all")) {
                _selectedLanguages = new String[sc.getLanguages().size()];
                
                n = 0;
        
                for (String language : sc.getLanguages()) {
                    _selectedLanguages[n++] = language;
                }
            }  
        }
        if (sc.getPubTypes() != null || sc.getPubTypes().size() > 0) {
            if (!sc.getPubTypes().get(0).equals("all")) {
                _selectedPubTypes = new String[sc.getPubTypes().size()];
                
                n = 0;
        
                for (String pubType : sc.getPubTypes()) {
                    _selectedPubTypes[n++] = pubType;
                }
            }  
        }
        
        _criteria = sc;       
    }
    public void setYear( String s)            { _year = s;            }
    public void setSource( String s )         { _source = s;          }
    public void setOrderExists( boolean b )   { _orderExists = b;     }
    public void setPurchaseId( long i )       { _purchaseId = i;      }
    public void setOpenURLCriteria( String s) { }

    public String getPermissionsSelectedAsQueryParameter() {
    	StringBuilder bldr = new StringBuilder();
    	String amp = "&";
    	for(PermissionCategoryEnum category: PermissionCategoryEnum.values()) {
			bldr.append(amp);
    		if (getIsPermissionSelected(category)) {
    			bldr.append(category.getCategoryCode()).append("=").append(ON);
    		} else {
    			bldr.append(category.getCategoryCode()).append("=").append(OFF);    			
    		}
    	}
    	return bldr.toString();
    }
    /**
     * Returns true if the supplied PermissionCategoryEnum is marked for display,
     * false otherwise.
     * @param permCatEnum
     * @return true if marked for display, false otherwise
     */
    public boolean getIsPermissionSelected(PermissionCategoryEnum permCatEnum) {
        Map<Long,String> displayablePermissions = _criteria.getDisplayablePermissions();
        String value = displayablePermissions.get(permCatEnum.getId());
        if (value!=null && value.equalsIgnoreCase(ON)) {
        	return true;
        }
        return false;
    }

    /**
     * Returns true if any of the "traditional" type
     * of use categories (including reprints) were selected for display. This includes APS,ECC,DPS,RLS,TRS &
     * RL reprints.
     * 
     * @return true if any were selected for display, false otherwise
     */
    public boolean getIsTraditionalPPUSelected() {
    	for(PermissionCategoryEnum permCatEnum: PermissionCategoryEnum.getTraditionalPpuCategories()) {
    		if (getIsPermissionSelected(permCatEnum)) {
    			return true;
    		}
    	}
    	if (getIsPermissionSelected(PermissionCategoryEnum.RLR_CATAGORYID)) {
    		return true;
    	}
    	
    	return false;
    }

    /**
     * Returns true if any of the license based type
     * of use categories were selected for display. This includes AAS,DRA,ARS.
     * 
     * @return true if any were selected for display, false otherwise
     */
    public boolean getIsLicenseSelected() {
    	for(PermissionCategoryEnum permCatEnum: PermissionCategoryEnum.getLicenseCategories()) {
    		if (getIsPermissionSelected(permCatEnum)) {
    			return true;
    		}
    	}
    	
    	return false;    	
    }
    
    /**
     * returns true if the underlying SearchCriteria object's displayable permissions
     * list contains any pay-per-use types of use - including the rightslink "reprint"
     * type of use category
     * @return
     */
    public boolean getIsPPU() {
       Map<Long,String> displayablePermissions = _criteria.getDisplayablePermissions();
       String apsVal = displayablePermissions.get(APS_CATAGORYID);
       String dpsVal = displayablePermissions.get(DPS_CATAGORYID);
       String eccVal = displayablePermissions.get(ECC_CATAGORYID);
       String rlsVal = displayablePermissions.get(RLS_CATAGORYID);
       String rlrVal = displayablePermissions.get(RLR_CATAGORYID);
       String trsIllVal = displayablePermissions.get(TRSILL_CATAGORYID);
       String trsPhotoVal = displayablePermissions.get(TRSPHOTO_CATAGORYID);
       
       boolean returnVal = (apsVal != null && apsVal.equalsIgnoreCase(ON)) ||
       (dpsVal != null && dpsVal.equalsIgnoreCase(ON)) ||
       (rlrVal != null && rlrVal.equalsIgnoreCase(ON)) ||
       (eccVal != null && eccVal.equalsIgnoreCase(ON)) ||
       (rlsVal != null && rlsVal.equalsIgnoreCase(ON)) ||
       (trsIllVal != null && trsIllVal.equalsIgnoreCase(ON)) ||
       (trsPhotoVal != null && trsPhotoVal.equalsIgnoreCase(ON));
       return returnVal;
       
    }

    /**
     * returns true if the Academic repertory license category is
     * selected for display
     * @return
     */
    public boolean getIsAcademic() {
    	Map<Long,String> displayablePermissions = _criteria.getDisplayablePermissions();
    	String arsVal = displayablePermissions.get(ARS_CATAGORYID);
    	return (arsVal != null && arsVal.equalsIgnoreCase(ON)); 
    }

    /**
     * returns true if the Rightslink category is
     * selected for display
     * @return
     */
    public boolean getIsReprints() {
    	Map<Long,String> displayablePermissions = _criteria.getDisplayablePermissions();
    	String arsVal = displayablePermissions.get(RLR_CATAGORYID);
    	return (arsVal != null && arsVal.equalsIgnoreCase(ON)); 
    }
    
    /**
     * returns true if either of the business repertory license categories is
     * selected for display (AAS or DRA)
     * @return
     */
    public boolean getIsBusiness() {
       Map<Long,String> displayablePermissions = _criteria.getDisplayablePermissions();
       String aasVal = displayablePermissions.get(AAS_CATAGORYID);
       String draVal = displayablePermissions.get(DRA_CATAGORYID);

       return  (aasVal != null && aasVal.equalsIgnoreCase(ON)) ||
       (draVal != null && draVal.equalsIgnoreCase(ON));
    }
    
    /**
     * returns true either the academic repertory license ARS or the 
     * business photocopy or business digital license is selected for
     * display
     * @return
     */
    public boolean getIsLicense() {
    	return getIsAcademic() || getIsBusiness();
    }

    /**
     * turns off display for all permission types (types of use)
     */
    public void unsetAll() {
    	for(SearchPermissionTypeEnum pType: SearchPermissionTypeEnum.values()) {
    		this.permissionTypeDisplaySetting.put(pType, null);
    	}
    }
    
    /**
     * Turns all of the permission types (types of use) off if either
     * null or "off" is passed. Turns on all of the same if "on" is passed.
     * @param s
     */
    public void setAll(String s)
    {
        if (s==null || s.equalsIgnoreCase(OFF)) {   
        	unsetAll();
        } else if (s != null && s.equalsIgnoreCase(ON)) {
            	for(SearchPermissionTypeEnum pType: SearchPermissionTypeEnum.values()) {
            		this.permissionTypeDisplaySetting.put(pType, ON);
            	}
        }
    }
    
    public void setSearchFlags( String searchType )
    {
        _lastSearch = searchType;
        
        if (!searchType.equals(ADVANCED_SEARCH)) {
            setAll(ON);
        }
        if ("ppu".equals(searchType)) {
            unsetAll();
            setAps(ON);
            setTrsIll(ON);
            setTrsPhoto(ON);
            setEcc(ON);
            setDps(ON);
            setRls(ON);
            setRlr(ON);
        }
        if ("annual".equals(searchType)) {
            unsetAll();
            setAas(ON);
            setDra(ON);   
        }
        if ("academic".equals(searchType)) {
            unsetAll();
            setArs(ON);   
        }
    }

    /**
     * turns off display for the specified permission type
     * @param pType
     */
    public void unset(SearchPermissionTypeEnum pType) {
    	this.permissionTypeDisplaySetting.put(pType, null);
    }
    /**
     * Turns on or off the display of the specified SearchPermissionTypeEnum. 
     * @param permTypeEnumVal
     * @param onOrOff must be either "on" or "off" (without the quotes)
     */
    public void setPermissionTypeDisplay(SearchPermissionTypeEnum permTypeEnumVal, String onOrOff) {
    	if (!onOrOff.equalsIgnoreCase(ON) && !onOrOff.equalsIgnoreCase(OFF)) {
    		throw new IllegalArgumentException("setPermissionTypeDisplay onOrOff arg must be either 'on' or 'off', received " + onOrOff);
    	}
    	unset(permTypeEnumVal);
    	this.permissionTypeDisplaySetting.put(permTypeEnumVal, onOrOff);    	
    }
    
    public void setAps(String s)
    {
    	unset(SearchPermissionTypeEnum.ACADEMIC_TRX_PHOTOCOPY);
        if (s != null)
        {
            if (s.equalsIgnoreCase( ON ))
            {
            	this.permissionTypeDisplaySetting.put(SearchPermissionTypeEnum.ACADEMIC_TRX_PHOTOCOPY, ON);
            }
        }
    }
    
    public void setDps(String s)
    {
    	unset(SearchPermissionTypeEnum.BUSINESS_TRX_DIGITAL);
        if (s != null)
        {
            if (s.equalsIgnoreCase( ON ))
            {
            	this.permissionTypeDisplaySetting.put(SearchPermissionTypeEnum.BUSINESS_TRX_DIGITAL, ON);
            }
        }
    }
    
    public void setEcc(String s)
    {
    	unset(SearchPermissionTypeEnum.ACADEMIC_TRX_DIGITAL);
        if (s != null)
        {
            if (s.equalsIgnoreCase( ON ))
            {
            	this.permissionTypeDisplaySetting.put(SearchPermissionTypeEnum.ACADEMIC_TRX_DIGITAL, ON);
            }
        }
    }
    
    public void setRls(String s)
    {
    	unset(SearchPermissionTypeEnum.REPUBLICATION);
        if (s != null)
        {
            if (s.equalsIgnoreCase( ON ))
            {
            	this.permissionTypeDisplaySetting.put(SearchPermissionTypeEnum.REPUBLICATION, ON);
            }
        }
    }
    public void setRlr(String s)
    {
    	unset(SearchPermissionTypeEnum.RIGHTSLINK_REPRINT);
    	unset(SearchPermissionTypeEnum.RIGHTSLINK_DIGITAL);
        if (s != null)
        {
            if (s.equalsIgnoreCase( ON ))
            {
            	this.permissionTypeDisplaySetting.put(SearchPermissionTypeEnum.RIGHTSLINK_REPRINT, ON);
            	this.permissionTypeDisplaySetting.put(SearchPermissionTypeEnum.RIGHTSLINK_DIGITAL, ON);
            }
        }
    }
    public void setTrsIll(String s)
    {
    	unset(SearchPermissionTypeEnum.BUSINESS_TRX_ILL);
        if (s != null)
        {
            if (s.equalsIgnoreCase( ON ))
            {
            	this.permissionTypeDisplaySetting.put(SearchPermissionTypeEnum.BUSINESS_TRX_ILL, ON);
            }
        }
    }
    
    public void setTrsPhoto(String s)
    {
    	unset(SearchPermissionTypeEnum.BUSINESS_TRX_PHOTOCOPY);
        if (s != null)
        {
            if (s.equalsIgnoreCase( ON ))
            {
            	this.permissionTypeDisplaySetting.put(SearchPermissionTypeEnum.BUSINESS_TRX_PHOTOCOPY, ON);
            }
        }
    }
    
    public void setAas(String s)
    {
        unset(SearchPermissionTypeEnum.BUSINESS_LIC_PHOTOCOPY);
        if (s != null)
        {
            if (s.equalsIgnoreCase( ON ))
            {
            	this.permissionTypeDisplaySetting.put(SearchPermissionTypeEnum.BUSINESS_LIC_PHOTOCOPY, ON);
            }
        }
    }
    
    public void setDra(String s)
    {
        unset(SearchPermissionTypeEnum.BUSINESS_LIC_DIGITAL);
        if (s != null)
        {
            if (s.equalsIgnoreCase( ON ))
            {
            	this.permissionTypeDisplaySetting.put(SearchPermissionTypeEnum.BUSINESS_LIC_DIGITAL, ON);
            }
        }
    }
    
    public void setArs(String s)
    {
        unset(SearchPermissionTypeEnum.ACADEMIC_LIC_DIGITAL);
        if (s != null)
        {
            if (s.equalsIgnoreCase( ON ))
            {
            	this.permissionTypeDisplaySetting.put(SearchPermissionTypeEnum.ACADEMIC_LIC_DIGITAL, ON);
            }
        }
    }
    
        
    private boolean isTypesOfUseSelectedAllOff() {
    	for(SearchPermissionTypeEnum pType: SearchPermissionTypeEnum.values()) {
    		String setting = permissionTypeDisplaySetting.get(pType);
    		if (setting!=null && setting.equalsIgnoreCase(ON)) {
    			return false;
    		}
    	}
    	return true;
    }
    
    public boolean isTypesOfUseSelectedAllOn() {
    	for(SearchPermissionTypeEnum pType: SearchPermissionTypeEnum.values()) {
    		String setting = permissionTypeDisplaySetting.get(pType);
    		if (setting==null || !setting.equalsIgnoreCase(ON)) {
    			return false;
    		}
    	}
    	return true;
    }
    
    /**
     * Synchronizes this SearchForm with the CCUser object found on the UserContext
     * with respect to the types of use that the user wants displayed. 
     * It does this by looking at the UserContext and updating this form to reflect the same.
     */
    public void synchronizeTypesOfUse() 
    {
        StringBuffer obuf = null;
        if (_logger.isDebugEnabled()) 
        {
            obuf = new StringBuffer();
            obuf.append("\n\nSYNCHRONIZING TYPES OF USE IN SEARCH FORM\n");
        }
        
        
        try {
            CCUser usr = UserContextService.getActiveAppUser();
            this.setAll(OFF);
            boolean isPPU_TPUSelected = false;
            for(SearchPermissionTypeEnum pType: SearchPermissionTypeEnum.values())
            {
                if (_logger.isDebugEnabled()) 
                {
                    obuf.append("\nDisplay Option ")
                        .append(pType.name())
                        .append(": ")
                        .append(usr.getSearchPreference(pType)?"on":"off");
                }  
                this.permissionTypeDisplaySetting.put(pType, usr.getSearchPreference(pType)?ON:null);
                if (usr.getSearchPreference(pType) && (pType == SearchPermissionTypeEnum.ACADEMIC_TRX_DIGITAL
                		|| pType == SearchPermissionTypeEnum.ACADEMIC_TRX_PHOTOCOPY || pType == SearchPermissionTypeEnum.BUSINESS_TRX_DIGITAL
                		|| pType == SearchPermissionTypeEnum.BUSINESS_TRX_ILL || pType == SearchPermissionTypeEnum.BUSINESS_TRX_PHOTOCOPY
                		|| pType == SearchPermissionTypeEnum.REPUBLICATION)) {
                	isPPU_TPUSelected = true;
                }
            }
           if ( !this.getSource().equals(SearchForm.PD_SOURCE ) && isPPU_TPUSelected) {            
        	   // except for permissions direct, show the reprint permission in the search results – no matter which combination of checkboxes is selected, unless 
        	   //no pay per use types are selected otherwise you are liable to see a blank PPU tab in the results set for all the publications that
        	   //do not have a RightsLink Reprint or Digital
        	   this.permissionTypeDisplaySetting.put(SearchPermissionTypeEnum.RIGHTSLINK_DIGITAL, ON);
        	   this.permissionTypeDisplaySetting.put(SearchPermissionTypeEnum.RIGHTSLINK_REPRINT, ON);
            }
        } catch (Exception e) {
            //  Really... there is nothing to do here.  Just
            //  let it slide.  This causes a problem during the
            //  cookies reset when you click "Not you?"
        	_logger.error(ExceptionUtils.getFullStackTrace(e));
        }
        
        computeAndSetCategoriesToDisplay();
        
        if (_logger.isDebugEnabled())
        {
            obuf.append("\n\n");
            _logger.debug(obuf.toString());
        }
    }

    /**
     * turns off the display of Rightslink related permissions if at least
     * one other transactional category is turned on for display.
     */
    public void makeSureRlrIsOff() {
    	
    	boolean rlrOn = getIsShowRlr();
    	boolean anotherOn = getIsShowTrsIll() 
		  || getIsShowTrsPhoto()
		  || getIsShowRls()
		  || getIsShowAps()
		  || getIsShowEcc()
		  || getIsShowDps();
    	
    	if ( anotherOn && rlrOn ) {
   			unset(SearchPermissionTypeEnum.RIGHTSLINK_REPRINT);
   			unset(SearchPermissionTypeEnum.RIGHTSLINK_DIGITAL);
   	        UserServices.updateCurrentUserSearchPreference(SearchPermissionTypeEnum.RIGHTSLINK_REPRINT, false);
   	        UserServices.updateCurrentUserSearchPreference(SearchPermissionTypeEnum.RIGHTSLINK_DIGITAL, false);
    	}
    }
    
    private boolean isSearchPermissionTypeOn(SearchPermissionTypeEnum pType) {
    	String setting = this.permissionTypeDisplaySetting.get(pType);
    	if (setting==null || !setting.equalsIgnoreCase(ON)) {
    		return false;
    	}
    	return true;
    }
    
    /**
     * Persists the identified types of use that the user wanted to
     * see in search results onto the CCUser record in the DB
     */
    public void persistTypesOfUse() 
    {
    	boolean isPPU_TPUSelected = false;
        if (_logger.isDebugEnabled()) {
            StringBuffer obuf = new StringBuffer();
            
            obuf.append("\n\nPERSISTING TYPES OF USE IN SEARCH FORM\n")
                .append("\n_ars = ").append(getArs())
                .append("\n_ecc  = ").append(getEcc())
                .append("\n_aps  = ").append(getAps())
                .append("\n_dra  = ").append(getDra())
                .append("\n_aas  = ").append(getAas())
                .append("\n_dps  = ").append(getDps())
                .append("\n_trsIll  = ").append(getTrsIll())
                .append("\n_trsPhoto  = ").append(getTrsPhoto())
                .append("\n_rls  = ").append(getRls())
                .append("\n_rlr  = ").append(getRlr())
                .append("\n\n");
                
            _logger.debug(obuf.toString());
        }
        UserServices.updateCurrentUserSearchPreference(
            SearchPermissionTypeEnum.ACADEMIC_LIC_DIGITAL,
            isSearchPermissionTypeOn(SearchPermissionTypeEnum.ACADEMIC_LIC_DIGITAL)
        );
        UserServices.updateCurrentUserSearchPreference(
            SearchPermissionTypeEnum.ACADEMIC_TRX_DIGITAL,
            isSearchPermissionTypeOn(SearchPermissionTypeEnum.ACADEMIC_TRX_DIGITAL)
        );
        UserServices.updateCurrentUserSearchPreference(
            SearchPermissionTypeEnum.ACADEMIC_TRX_PHOTOCOPY,
            isSearchPermissionTypeOn(SearchPermissionTypeEnum.ACADEMIC_TRX_PHOTOCOPY)
        );
        UserServices.updateCurrentUserSearchPreference(
            SearchPermissionTypeEnum.BUSINESS_LIC_DIGITAL,
            isSearchPermissionTypeOn(SearchPermissionTypeEnum.BUSINESS_LIC_DIGITAL)
        );
        UserServices.updateCurrentUserSearchPreference(
            SearchPermissionTypeEnum.BUSINESS_LIC_PHOTOCOPY,
            isSearchPermissionTypeOn(SearchPermissionTypeEnum.BUSINESS_LIC_PHOTOCOPY)
        );
        UserServices.updateCurrentUserSearchPreference(
            SearchPermissionTypeEnum.BUSINESS_TRX_DIGITAL,
            isSearchPermissionTypeOn(SearchPermissionTypeEnum.BUSINESS_TRX_DIGITAL)
        );
        UserServices.updateCurrentUserSearchPreference(
                SearchPermissionTypeEnum.BUSINESS_TRX_ILL,
                isSearchPermissionTypeOn(SearchPermissionTypeEnum.BUSINESS_TRX_ILL)
            );
        UserServices.updateCurrentUserSearchPreference(
            SearchPermissionTypeEnum.BUSINESS_TRX_PHOTOCOPY,
            isSearchPermissionTypeOn(SearchPermissionTypeEnum.BUSINESS_TRX_PHOTOCOPY)
        );
        UserServices.updateCurrentUserSearchPreference(
            SearchPermissionTypeEnum.REPUBLICATION,
            isSearchPermissionTypeOn(SearchPermissionTypeEnum.REPUBLICATION)
        );
        UserServices.updateCurrentUserSearchPreference(
                SearchPermissionTypeEnum.RIGHTSLINK_REPRINT,
                isSearchPermissionTypeOn(SearchPermissionTypeEnum.RIGHTSLINK_REPRINT)
            );
        UserServices.updateCurrentUserSearchPreference(
                SearchPermissionTypeEnum.RIGHTSLINK_DIGITAL,
                isSearchPermissionTypeOn(SearchPermissionTypeEnum.RIGHTSLINK_DIGITAL)
            );
        
        if (isSearchPermissionTypeOn(SearchPermissionTypeEnum.ACADEMIC_TRX_DIGITAL) || isSearchPermissionTypeOn(SearchPermissionTypeEnum.ACADEMIC_TRX_PHOTOCOPY)
        	|| isSearchPermissionTypeOn(SearchPermissionTypeEnum.BUSINESS_TRX_DIGITAL) || isSearchPermissionTypeOn(SearchPermissionTypeEnum.BUSINESS_TRX_ILL) ||
        	isSearchPermissionTypeOn(SearchPermissionTypeEnum.BUSINESS_TRX_PHOTOCOPY) || isSearchPermissionTypeOn(SearchPermissionTypeEnum.REPUBLICATION)) {
        	isPPU_TPUSelected = true;
        }
        
        if(!this.getSource().equals( SearchForm.PD_SOURCE ) && isPPU_TPUSelected){

        	// except for permissions direct, show the reprint permission in the search results – no matter which combination of checkboxes is selected.	
	        
        	UserServices.updateCurrentUserSearchPreference( SearchPermissionTypeEnum.RIGHTSLINK_REPRINT, true );
	        UserServices.updateCurrentUserSearchPreference( SearchPermissionTypeEnum.RIGHTSLINK_DIGITAL, true );
	        setRlr(ON);
         }
    
    }
    
    /**
     * clears the form
     */
    @Override
    public void clearState()
    {
    	super.clearState();
        try {
    	    WebServiceSearch svc = new WebServiceSearch();
    	    _criteria = svc.getSearchCriteria();
        } catch (Exception e) {
        	/* SecurityRuntimeException will get thrown if no UserContext is on the Thread.
        	 * not sure what we're guarding against with this so adding a
        	 * log message to see what this protects against.
        	 */
        	_logger.error(ExceptionUtils.getFullStackTrace(e));
    	    _criteria = new SearchCriteriaImpl();
    	}
    	
    	_lastSearch = SIMPLE_SEARCH;
    	setLastPage(_lastSearch);
    	_selectedItem = null;
    	
    	//  I think this needs to go into the catch block...
    	
    	_criteria.setSortType( CCSearchCriteria.RELEVANCE_SORT );
    	_criteria.setSearchType( CCSearchCriteria.BASIC_SEARCH_TYPE );

    	clearSearchCriteria();
    	_criteria.setCountries( null );
    	_criteria.setLanguages( null );
    	_criteria.setPubTypes( null );
     	
        for(SearchPermissionTypeEnum permType: SearchPermissionTypeEnum.values()) {
        	this.permissionTypeDisplaySetting.put(permType, OFF);
        }
    	_year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
    	_source = NORMAL_SOURCE;
        _orderExists = false;
    }
    
    @Override
    public void reset( ActionMapping      mapping
                     , HttpServletRequest request ) {
        for(SearchPermissionTypeEnum permType: SearchPermissionTypeEnum.values()) {
        	this.permissionTypeDisplaySetting.put(permType, null);
        }
    }
    
    /**
     * clears Title/StdNo field, Publisher, AuthorEditor, Volume,
     * Edition, & Series fields
     */
    public void clearSearchCriteria() {
    	_criteria.setTitleOrStdNo( EMPTY_STRING );
    	_criteria.setPublisher( EMPTY_STRING );
    	_criteria.setAuthorEditor( EMPTY_STRING );
    	_criteria.setVolume( EMPTY_STRING );
    	_criteria.setEdition( EMPTY_STRING );
       	_criteria.setSeries( EMPTY_STRING );
    }
    
    @Override
    public ActionErrors validate( ActionMapping      mapping
                                , HttpServletRequest request )
    {
    	//	Capture some of the values we need to look at, and
    	//	also create a new messages object.

    	ActionErrors messages = super.validate( mapping, request );

    	int searchType = _criteria.getSearchType();
    	String title = _criteria.getTitleOrStdNo();
    	String publisher = _criteria.getPublisher();
    	
    	//	Simple search (only one field to check.)
    	
    	if (searchType == 0) {
    		if ((title == null) || (title.equalsIgnoreCase("Publication Title or ISBN/ISSN")) || 
                    (title.length() < MIN_FIELD_LEN)) {
    			messages.add( ActionMessages.GLOBAL_MESSAGE
    			            , new ActionMessage( "search.error.message.required.two"
    			                               , "Publication Title", "ISBN/ISSN" ) );
    		}
    	}
    	
    	//	Complex search.  Combination of title or standard number and
    	//	publisher... and also types of use.
    	
    	if (searchType == 1) {
    		if (isNull( title )) {
    			messages.add( ActionMessages.GLOBAL_MESSAGE
    			            , new ActionMessage( "search.error.message.required.field"
                                               , "Publication Title or ISBN/ISSN") );    		    
    		} 
    		else {
				if (title.length() == 1) {
					messages.add( ActionMessages.GLOBAL_MESSAGE
								, new ActionMessage( "search.error.message.required.two"
												   , "Publication Title or ISBN/ISSN", "Standard Number") );    		
				}
				if ((publisher != null) && (publisher.length() == 1)) {
					messages.add( ActionMessages.GLOBAL_MESSAGE
								, new ActionMessage( "search.error.message.required.one"
												   , "Publisher" ) );  		
				}
    		}
    		if (isTypesOfUseSelectedAllOff()) {
					messages.add( ActionMessages.GLOBAL_MESSAGE
								, new ActionMessage( "search.error.message.required.field"
												   , "Permission type" ) );     		
    		}
    	}
    	return messages;
    }
    

    
    public boolean getIntegrates() {
        for (PermissionCategoryDisplay permCatDisplay : _permCatDisplay) {
    		if (permCatDisplay.getIntegrates()) return true;
    	}
        return false;
    }
    
    public String getPublisherURL() {
    	for (PermissionCategoryDisplay permCatDisplay : _permCatDisplay) {
    		if (permCatDisplay.getPublisherURL() != null && !permCatDisplay.getPublisherURL().isEmpty()){
    			return permCatDisplay.getPublisherURL();
    		}
    	}
    	return "";
    }

    public String getRLinkLearnMore() {
    	//should be the same publisher for each Category & TOU,
    	//return the first one we find
    	for (PermissionCategoryDisplay permCatDisplay : _permCatDisplay){
    		String learnMore = permCatDisplay.getLearnMore();
        	if (learnMore!=null && learnMore.length()>0) {
        		return learnMore;
        	}
        }
    	return null;
    }  
    
    public String getRLinkPublisherName() {
    	//should be the same publisher for each Category & TOU,
    	//return the first one we find
    	for (PermissionCategoryDisplay permCatDisplay : _permCatDisplay){
    		String name = permCatDisplay.getPublisherName();
        	if (name!=null && name.length()>0) {
        		return name;
        	}
        }
    	return null;
    }  
    
    /**
     * Iterates over each PermissionCategoryDisplay looking for RL permission
     * options. Returns the first one found. This is extremely inefficient and
     * should be refactored.
     * @return
     */
    public String getRLinkPermissionOptions() {
    	for (PermissionCategoryDisplay permCatDisplay : _permCatDisplay){
    		String permOptions = permCatDisplay.getPermissionOptions();
        	if (permOptions!=null && permOptions.length()>0) {
        		return permOptions;
        	}
        }
    	return null;
    }    
    public String getRightsQualifyingStatement() {
    	if(hasGlobalRightsQualifier()){
    		 for (PermissionCategoryDisplay permCatDisplay : _permCatDisplay) {
    	        	if(permCatDisplay.getGotRQualStmt()){
    	        		return permCatDisplay.getRightsQualifyingStatement();
    	        	}
    	        }
    	}
        return null;
    }
    
    private boolean hasGlobalRightsQualifier(){
    	boolean isGlobal=true;
    	 for (PermissionCategoryDisplay permCatDisplay : _permCatDisplay) {
         	if(!permCatDisplay.isGlobalRightsQualifier()){
         		isGlobal=false;
         	}
         }
    	 return isGlobal;
    }
    
    private boolean isNull( String s ) {
    	return ((s == null) || (s.equals( "" )));
    }

	public boolean getIsBiactive() {
		return isBiactive;
	}

	public void setBiactive(boolean isBiactive) {
		this.isBiactive = isBiactive;
	}

	public boolean getIsPermissionDirectAction() {
		return permissionDirectAction;
	}

	public void setIsPermissionDirectAction(boolean permissionDirectAction) {
		this.permissionDirectAction = permissionDirectAction;
	}

	public String getPermissionDirectProduct() {
		return permissionDirectProduct;
	}

	public void setPermissionDirectProduct(String permissionDirectProduct) {
		this.permissionDirectProduct = permissionDirectProduct;
	}
	
	public String getSearchTypeWT() 
	{ 
		return _criteria.getSearchType() == 1 ? "Advanced Search" : "Basic"; 
	
	}
    public Boolean getIsSessionNew() {
    	return UserContextService.isSessionNewlyCreated();
    }
    /**
     * Returns true if the search results are populated.
     * False otherwise.
     * @return
     */
    public Boolean getHasSearchResults() {
    	if (_results==null || _results.length==0) {
    		return Boolean.FALSE;
    	}
    	return Boolean.TRUE;
    }

	/**
	 * @return the selectedRlPermissionType
	 */
	public String getSelectedRlPermissionType()
	{
		return selectedRlPermissionType;
	}

	/**
	 * @param selectedRlPermissionType the selectedRlPermissionType to set
	 */
	public void setSelectedRlPermissionType(String selectedRlPermissionType)
	{
		this.selectedRlPermissionType = selectedRlPermissionType;
	}

	/**
	 * @return the selectedOfferChannel
	 */
	public String getSelectedOfferChannel()
	{
		return selectedOfferChannel;
	}

	/**
	 * @param selectedOfferChannel the selectedOfferChannel to set
	 */
	public void setSelectedOfferChannel(String selectedOfferChannel)
	{
		this.selectedOfferChannel = selectedOfferChannel;
	}
    
    //  2012-11-12  MSJ
    //  Adding flag for OpenURL searches.  We want to know
    //  if the user specifed an ISSN in the search URL.  This
    //  is to enable filtering if we get multiple results from
    //  our search (ISSN happens to match date ranges in titles).

    public void    setIsIssnSearch(boolean b) { _isIssnSearch = b;    }
    public boolean getIsIssnSearch()          { return _isIssnSearch; }
    
    //Check for NRLS Educational Program TOU
    public boolean getIsEducational() {
    	
    	for ( PermissionCategoryDisplay permCat : getPermCatDisplay()) {
    		
    		if (permCat != null) {
    			if (permCat.getIsRepublication()) {
    				
    				for ( PermSummaryTouDisplay permTou : permCat.getPermSummaryTouDisplays() ) {
    					
    					String permDesc = permTou.getDescription();
    					boolean test = permTou.getIsAvailable();
    					
    					if (permDesc.contains("Educational")) {
    						return true;
    					}
    					
    				}
    			
    			}
    		
    		}
    	}
    	
    	return false;
    	
    }
    //Get NRLS TOU's which will be displayed on the perm. summary page
    public int getRepubTouCount() {
    	
    	int i = 0;
    	
    	for ( PermissionCategoryDisplay permCat : getPermCatDisplay()) {
    		
    		if (permCat != null) {
    			if (permCat.getIsRepublication()) {
    				
    				for ( PermSummaryTouDisplay permTou : permCat.getPermSummaryTouDisplays() ) {
    					
    					String permDesc = permTou.getDescription();
    					boolean test = permTou.getIsAvailable();
    					
    					if ( (permTou.getIsValidTOU()) && (permTou.getRgtInstRlOfferId() != 0) && (!permTou.getIsNotAvailable())) {
    						i++;
    					}
    					
    				}
    			
    			}
    		
    		}
    	}
    	 return i;
    }
    
    
}
