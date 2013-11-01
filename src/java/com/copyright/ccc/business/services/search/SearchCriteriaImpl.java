package com.copyright.ccc.business.services.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.copyright.ccc.business.data.CCSearchCriteria;
import com.copyright.svc.searchRetrieval.api.data.PublicationSearch;

/**
 * @author  Mike Tremblay
 * @version 1.0
 * Created 30-Oct-2006
 */
final public class SearchCriteriaImpl implements CCSearchCriteria {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String _titleOrStdNoCriteria;
    private int _searchType;
    private String _publisher;
    private String _authorEditor;
    private String _volume;
    private String _edition;
    private int _sortType;
    private String _wrWrkInst;




    //  2009-12-07  MSJ
    //  Adding fields for summit; metadata search limiters.
    
    private List<String> _countries;
    private List<String> _languages;
    private List<String> _pub_types;
    private String _series;
    private Map<Long,String> displayablePermissions;
    
    //  Constructors, accessors.
    
    public SearchCriteriaImpl() {
    }

    /**
     *
     * @param searchCriteria
     */
    public void setTitleOrStdNo(String searchCriteria) {
        this._titleOrStdNoCriteria = searchCriteria;
    }

    /**
     *
     * @param searchType
     */
    public void setSearchType(int searchType) {
        this._searchType = searchType;
    }

    /**
     *
     * @param publisher
     */
    public void setPublisher(String publisher) {
        this._publisher = publisher;
    }

    /**
     *
     * @param authorEditor
     */
    public void setAuthorEditor(String authorEditor) {
        this._authorEditor = authorEditor;
    }

    /**
     *
     * @param volume
     */
    public void setVolume(String volume) {
        this._volume = volume;
    }

    /**
     *
     * @param edition
     */
    public void setEdition(String edition) {
        this._edition = edition;
    }

    public void clear() {
        this._titleOrStdNoCriteria = "";
        this._searchType = 0;
        this._publisher = "";
        this._authorEditor = "";
        this._volume = "";
        this._edition = "";
        this._sortType = 0;
        this._wrWrkInst = null;
    }

    /**
     *
     * @param sortType
     */
    public void setSortType(int sortType) {
        this._sortType = sortType;
    }

    public String getTitleOrStdNo() {
        return _titleOrStdNoCriteria;
    }

    public int getSearchType() {
        return _searchType;
    }

    public String getPublisher() {
        return _publisher;
    }

    public String getAuthorEditor() {
        return _authorEditor;
    }

    public String getVolume() {
        return _volume;
    }

    public String getEdition() {
        return _edition;
    }

    public int getSortType() {
        return _sortType;
    }

    //  2009-12-07  MSJ
    //  Additional accessors for added metadata limiters.
    //  Note the difference in the data types being consumed/
    //  returned in setters vs. getters.  I do this because the
    //  searchForm deals strictly with arrays and it will always
    //  (AFAIK for now) be the one populating the criteria.
    //
    //  The WebServiceSearch class will want lists back to make
    //  its life a but easier when constructing the actual search
    //  parameters.  This is probably considered bad form, forgive
    //  me...
    
    public void setCountries(String[] countries)
    {
        if (countries != null && countries.length > 0) {
            _countries = new ArrayList<String>(countries.length);
            
            for (int i = 0; i < countries.length; i++) {
                _countries.add(i, countries[i]);
            }
        }
        else {
            _countries = new ArrayList<String>(1);
            _countries.add(0, "all");
        }
    }
    public void setLanguages(String[] languages)
    {
        if (languages != null && languages.length > 0) {
            _languages = new ArrayList<String>(languages.length);
            
            for (int i = 0; i < languages.length; i++) {
                _languages.add(i, languages[i]);
            }
        }
        else {
            _languages = new ArrayList<String>(1);
            _languages.add(0, "all");
        }
    }
    public void setPubTypes(String[] pubTypes)
    {
        if (pubTypes != null && pubTypes.length > 0) {
            _pub_types = new ArrayList<String>(pubTypes.length);
            
            for (int i = 0; i < pubTypes.length; i++) {
                _pub_types.add(i, pubTypes[i]); 
            }
        }
        else {
            _pub_types = new ArrayList<String>(1);
            _pub_types.add(0, "all");
        }
    }
    public void setSeries(String series)
    {
        _series = series;
    }
    public List<String> getCountries() { return _countries; }
    public List<String> getLanguages() { return _languages; }
    public List<String> getPubTypes()  { return _pub_types; }
    public String       getSeries()    { return _series; }
    
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        
        buf.append("title:         ").append(_titleOrStdNoCriteria).append("\n");
        buf.append("publisher:     ").append(_publisher).append("\n");
        buf.append("author/editor: ").append(_authorEditor).append("\n");
        buf.append("volume:        ").append(_volume).append("\n");
        buf.append("edition:       ").append(_edition).append("\n");
        buf.append("search type:   ").append(_searchType).append("\n");
        buf.append("sort type:     ").append(_sortType).append("\n");
        buf.append("---limiters--- ").append("\n");
        buf.append("series:        ").append(_series).append("\n");
        buf.append("countries:     ").append("\n");
        
        for (String country : _countries) {
            buf.append("........").append(country).append("\n");  
        }
        buf.append("languages:     ").append("\n");
        
        for (String language : _languages) {
            buf.append("........").append(language).append("\n");  
        }
        buf.append("pub types:     ").append("\n");
        
        for (String pubType : _pub_types) {
            buf.append("........").append(pubType).append("\n");  
        }
        return buf.toString();
    }
    
    public PublicationSearch getPublicationSearch() {
        //  Build a PublicationSearch object out of our
        //  search criteria.
        
        PublicationSearch ps = new PublicationSearch();
        
        ps.setPubTitleOrStdNumber(_titleOrStdNoCriteria);
        ps.setPublisherOrRightsholder(_publisher);
        ps.setAuthorOrEditor(_authorEditor);
        ps.setSeriesName(_series);
        ps.setWrWrkInst(_wrWrkInst);
        //  Turn our arrays of limiters into -OR- query
        //  substrings.
        
        List<String> items = _countries;
        StringBuffer buf = new StringBuffer();
        
        ctry: if (items != null) {
            //  I just want to say that I cannot believe that
            //  the think tank at Sun did not make the new for
            //  loop perform an automatic null check and skip
            //  the loop.  Unless of course I missed an update
            //  notice in a recent release.  Then I say "yay!"
            
            for (String item : items) {
                if ("all".equalsIgnoreCase(item)) break ctry;

                String preparedItem = item;
                if(item != null && item.contains(" "))
                {
                	// wrap it with quotes to make it an exact phrase
                	preparedItem = "\"" + item + "\"";
                }
                buf.append(preparedItem).append(" OR ");  
            }
            ps.setCountry(buf.substring(0, buf.length() - 4));
        }
        
        items = _languages;
        buf = new StringBuffer();
        
        lang: if (items != null) {
            for (String item : items) {
                if ("all".equalsIgnoreCase(item)) break lang;
                
                String preparedItem = item;
                if(item != null && item.contains(" "))
                {
                	// wrap it with quotes to make it an exact phrase
                	preparedItem = "\"" + item + "\"";
                }
                buf.append(preparedItem).append(" OR ");  
            }
            ps.setLanguage(buf.substring(0, buf.length() - 4));
        }
        
        items = _pub_types;
        buf = new StringBuffer();
        
        pubt: if (items != null) {
            for (String item : items) {
                if ("all".equalsIgnoreCase(item)) break pubt;
                
                String preparedItem = item;
                if(item != null && item.contains(" "))
                {
                	// wrap it with quotes to make it an exact phrase
                	preparedItem = "\"" + item + "\"";
                }
                buf.append(preparedItem).append(" OR ");  
            }
            ps.setPubType(buf.substring(0, buf.length() - 4));
        }
        //searches should always have rights
        ps.setHasRights("Y");
        return ps;
    }

	@Override
	public Map<Long, String> getDisplayablePermissions() {
		return displayablePermissions;
	}
	
	public void setDisplayablePermissions(Map<Long,String>displayablePermissions){
		this.displayablePermissions = displayablePermissions;
	}

	@Override
	public String getWrWrkInst() {
		return _wrWrkInst;
	}

	@Override
	public void setWrWrkInst(String wrWrkInst) {
		this._wrWrkInst = wrWrkInst;	
	}
}
