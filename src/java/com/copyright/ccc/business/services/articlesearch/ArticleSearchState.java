package com.copyright.ccc.business.services.articlesearch;

import java.io.Serializable;
import java.util.List;

import com.copyright.ccc.business.services.cart.PurchasablePermission;
import com.copyright.domain.data.WorkExternal;
import com.copyright.svc.searchRetrieval.api.data.ArticleSearch;

public class ArticleSearchState implements Serializable {

	private static final long serialVersionUID = 1L;
	

	    private int _currentPage;
	    private int _pageSize;
	    private List <String> _sortList;

	    private ArticleSearch _articlesearchCriteria;
	    private PurchasablePermission _rlpurchasablePermission=null;
	    private List<WorkExternal> _searchResultWorks;
	    
	    private WorkExternal _selectedSubWork;

	    public ArticleSearchState() {
	    
	        _currentPage = 0;
	        _pageSize = 0;	        
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

	    public void setSearchCriteria(ArticleSearch articlesearchCriteria) {
	        _articlesearchCriteria = articlesearchCriteria;
	    }

	    public ArticleSearch getSearchCriteria() {
	        return _articlesearchCriteria;
	    }
	    
	    public void clear(){
	        _currentPage = 0;
	        _pageSize = 0;
	    }

		public List<String> getSortList() {
			return _sortList;
		}

		public void setSortList(List<String> list) {
			_sortList = list;
		}

		public List<WorkExternal> getSearchResultWorks() {
			return _searchResultWorks;
		}

		public void setSearchResultWorks(List<WorkExternal> resultWorks) {
			_searchResultWorks = resultWorks;
		}
		
		public void addSearchResultsWorks(List<WorkExternal> resultWorks){
			_searchResultWorks.addAll(resultWorks);
		}

		public WorkExternal getSelectedSubWork() {
			return _selectedSubWork;
		}

		public void setSelectedSubWork(WorkExternal work) {
			_selectedSubWork = work;
		}

		public void setRlpurchasablePermission(PurchasablePermission _rlpurchasablePermission) {
			this._rlpurchasablePermission = _rlpurchasablePermission;
		}

		public PurchasablePermission getRlpurchasablePermission() {
			return _rlpurchasablePermission;
		}
		
		
	    
}
