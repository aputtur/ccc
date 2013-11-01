package com.copyright.ccc.business.services.search;

import java.io.Serializable;

import com.copyright.ccc.business.data.CCSearchCriteria;
import com.copyright.ccc.business.data.PublicationPermissionSearchResult;
import com.copyright.domain.data.WorkExternal;


public class SearchState implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int _currentPage;
    private int _pageSize;
    private PublicationPermissionSearchResult _searchResults;
    private CCSearchCriteria _searchCriteria;
    private WorkExternal _selectedWork;
    private String _selectedTou;
    private Long _selectedTouId;
    private String _selectedPermissionType;
    private Long selectedCategoryId;
    private Long selectedRrTouId;
    private int _resultsTotalSize;
    private boolean _isBiactive=false;
    private int _biactivePubYear;
    
    
  //added for pub to pub project
    private String selectedRlPermissionType = "";
    private String selectedOfferChannel = "";
    private String selectedRlPubCode = "";

    public void setSelectedRlPubCode(String selectedRlPubCode)
	{
		this.selectedRlPubCode = selectedRlPubCode;
	}

	public String getSelectedRlPubCode()
	{
		return selectedRlPubCode;
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

	public SearchState() {
    
        _currentPage = 0;
        _pageSize = 0;
        _searchResults = new PublicationPermissionSearchResultImpl();
        _searchCriteria = new SearchCriteriaImpl();
        
    }

    public void setCurrentPage(int pageNum) {
        _currentPage = pageNum;
    }

    public int getCurrentPage() {
        return _currentPage;
    }

    public void setPageSize(int pageSize) {
        _pageSize = pageSize;
    }

    public int getPageSize() {
        return _pageSize;
    }

    public void setSearchResults(PublicationPermissionSearchResult searchResults) {
        _searchResults = searchResults;
    }

    public PublicationPermissionSearchResult getSearchResults() {
        return _searchResults;
    }

    public void setSearchCriteria(CCSearchCriteria searchCriteria) {
        _searchCriteria = searchCriteria;
    }

    public CCSearchCriteria getSearchCriteria() {
        return _searchCriteria;
    }
    
    public WorkExternal getSelectedWork() {
		return _selectedWork;
	}

	public void setSelectedWork(WorkExternal work) {
		_selectedWork = work;
	}

	public String getSelectedTou() {
		return _selectedTou;
	}

	public void setSelectedTou(String tou) {
		_selectedTou = tou;
	}

	public Long getSelectedTouId() {
		return _selectedTouId;
	}

	public void setSelectedTouId(Long touId) {
		_selectedTouId = touId;
	}

	public String getSelectedPermissionType() {
		return _selectedPermissionType;
	}

	public void setSelectedPermissionType(String permissionType) {
		_selectedPermissionType = permissionType;
	}

	
	public Long getSelectedCategoryId() {
		return selectedCategoryId;
	}

	public void setSelectedCategoryId(Long selectedCategoryId) {
		this.selectedCategoryId = selectedCategoryId;
	}

	public Long getSelectedRrTouId() {
		return selectedRrTouId;
	}

	public void setSelectedRrTouId(Long selectedRrTouId) {
		this.selectedRrTouId = selectedRrTouId;
	}

	public void clear(){
        _currentPage = 0;
        _pageSize = 0;
        _resultsTotalSize = 0;
        _searchResults = null;
        _searchCriteria = null;
        _selectedWork = null;
    }

	public int getResultsTotalSize() {
		return _resultsTotalSize;
	}

	public void setResultsTotalSize(int totalSize) {
		_resultsTotalSize = totalSize;
	}

	public void setBiactive(boolean isBiactive) {
		this._isBiactive = isBiactive;
	}

	public boolean isBiactive() {
		return _isBiactive;
	}

	public void setBiactivePubYear(int biactivePubYear) {
		this._biactivePubYear = biactivePubYear;
	}

	public int getBiactivePubYeatr() {
		return _biactivePubYear;
	}
	
}
