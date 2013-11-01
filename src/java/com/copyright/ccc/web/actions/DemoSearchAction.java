package com.copyright.ccc.web.actions;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.business.data.CCSearchCriteria;
import com.copyright.ccc.business.services.search.SearchCriteriaImpl;
import com.copyright.ccc.business.services.search.WebServiceSearch;
import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.forms.DemoSearchForm;

public class DemoSearchAction extends CCAction
{
    private static final String SHOW_RESULTS = "showResults";
    private static final String SHOW_MAIN = "showMain";
    
    public ActionForward defaultOperation(ActionMapping mapping,
                                            ActionForm form,
                                            HttpServletRequest request,
                                            HttpServletResponse response )
    { 
        return mapping.findForward( SHOW_MAIN );
    }
    
    public ActionForward executeBasicSearch(ActionMapping mapping,
                                            ActionForm form,
                                            HttpServletRequest request,
                                            HttpServletResponse response )
    {
        DemoSearchForm df = castForm( DemoSearchForm.class, form );
        
        //call search service
        //forward to results page
        WebServiceSearch webServiceSearch = new WebServiceSearch();
        String searchString = df.getSearchCriteria();
        CCSearchCriteria ccSearchCriteria = new SearchCriteriaImpl();
        ccSearchCriteria.setTitleOrStdNo(searchString);
        ccSearchCriteria.setSearchType(CCSearchCriteria.BASIC_SEARCH_TYPE);
        ccSearchCriteria.setSortType(CCSearchCriteria.RELEVANCE_SORT);
        //int x = 
        	webServiceSearch.performBasicSearch(ccSearchCriteria,25);

        return mapping.findForward(SHOW_RESULTS); //demoSearchResults;
    }
}
