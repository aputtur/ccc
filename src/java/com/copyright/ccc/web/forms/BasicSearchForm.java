package com.copyright.ccc.web.forms;

import java.util.ArrayList;
import java.util.Collection;

import com.copyright.ccc.web.CCValidatorForm;
import com.copyright.ccc.web.util.DropDownElement;
import com.copyright.ccc.web.util.WebUtils;

public class BasicSearchForm extends CCValidatorForm {

	private static final long serialVersionUID = 1L;
	
    private int                 _pageSize        = 0;
    private int                 _count           = 0;
    private int                 _page            = 0;
    private int                 _pageRangeHigh   = 0;
    private int                 _pageRangeLow    = 0;
    private int                 _pageRangeTotal  = 0;
    private String              _lastPage        = null;

    
    private Collection<DropDownElement> _sortOptions = null;
    private Collection<DropDownElement> _pageOptions = null;

    private String[] _searchInstead = null;

    public String getCount()          { return Integer.toString(_count);        }
    public String getPageSize()       { return String.valueOf(_pageSize);       }
    public String getPageRangeTotal() { return String.valueOf(_pageRangeTotal); }
    public String getPageNo()         { return String.valueOf(_page);           }
    public String getLastPage()       { return _lastPage;                       }

    public Collection<DropDownElement> getSortOptions() { return _sortOptions; }
    public Collection<DropDownElement> getPageOptions() { return _pageOptions; }

    public String[] getSearchInstead() { return _searchInstead; }

    public void setSearchInstead( String[] items )
    {
        _searchInstead = items;
    }
    
    public int getLowItem()
    {
        int low = ((_pageSize * _page) - _pageSize) + 1;
        return low;
    }
    
    public int getHighItem()
    {
        int high = _pageSize * _page;
        if (high > _count)
        {
            high = _count;
        }
        return high;
    }
    
    public Collection<Integer> getPageRange()
    {
        calculatePageRange();
        int size = (_pageRangeHigh - _pageRangeLow) + 1;
        
        ArrayList<Integer> pr = new ArrayList<Integer>( size );
        
        for (int i = 0; i < size; i++)
        {
            pr.add( i, i + _pageRangeLow );
        }
        
        return pr;
    }
    
    public String getIsMore()
    {
        int pageRangeTotal = 0;
        
        //  Total possible number of pages based on
        //  our pageSize...
        
        pageRangeTotal = (_count / _pageSize);
        if ((_count % _pageSize) > 0) pageRangeTotal += 1;
        
        if (pageRangeTotal > _pageRangeHigh)
            return "yes";
        else 
            return "no";
    }
    
    public String getIsLess()
    {
        if (_pageRangeLow > 1)
            return "yes";
        else 
            return "no";
    }
    
    public void setCount( String s )             { _count = Integer.parseInt( s );    }
    public void setPageSize( String s )          { _pageSize = Integer.parseInt( s ); }
    public void setPageNo( String s )            { _page = Integer.parseInt( s );     }
    public void setPageRangeTotal( String s )    { _pageRangeTotal = Integer.parseInt( s ); }
    public void setLastPage( String s )          { _lastPage = s;                     }

    
    //  Just two little helpers to manage page numbers.
    //  NOTE:   Java 5.0 allows you to add int primitives to
    //          Integer objects.  1.4 apparently does not.
    
    public void incrPageNo()
    {
        _page += 1;
        if (_page>_pageRangeHigh)
        {
        	incrPageRange();
        }
    }
    
    public void decrPageNo()
    {
        _page -= 1;
        if (_page<_pageRangeLow){
        	decrPageRange();
        }
    }
    
    //  Calculate paging stuff...
    
    public void calculatePageRange()
    {
        int pageRangeTotal = 0;
        
        if (_count != 0)
        {
            if (_pageRangeLow == 0) _pageRangeLow = 1;
            //_page = _pageRangeLow;
            
            //  Total possible number of pages based on
            //  our pageSize...
            
            pageRangeTotal = (_count / _pageSize);
            if ((_count % _pageSize) > 0)
            {
            	pageRangeTotal += 1;
            }
            
            //  Calculate the high page...
            
            _pageRangeHigh = _pageRangeLow + 3;
            if (_pageRangeHigh > pageRangeTotal) _pageRangeHigh = pageRangeTotal;
            _pageRangeTotal = pageRangeTotal;
        }
        else
        {
            _pageRangeLow = 0;
            _pageRangeHigh = 0;
        }
    }
    
    public void incrPageRange()
    {
        _pageRangeLow += 1;
        calculatePageRange();
    }
    
    public void decrPageRange()
    {
        _pageRangeLow -= 1;
         calculatePageRange();
    }
    
    public void clearPageRange() {
    	_pageRangeHigh = 0;
    	_pageRangeLow = 0;   	
    }
    
    //  Clear out our form, reset it to a nascient state.
    
    public void clearState()
    {    	    	
    	_count = 0;
    	_page = 1;
    	_pageSize = 25;
    	_pageRangeHigh = 0;
    	_pageRangeLow = 0;

        if (this instanceof ArticleSearchActionForm){
           _sortOptions = WebUtils.getArticleSortOptions();
           _pageOptions = WebUtils.getArticlePageSizeOptions();
        }
        else {
    	   _sortOptions = WebUtils.getSRSortOptions();
    	   _pageOptions = WebUtils.getSRPageSizeOptions();
        }
    }
    
    protected int getPageRangeHigh() {
    	return _pageRangeHigh;
    }
}
