package com.copyright.ccc.web.actions;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.business.data.Publication;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.web.forms.SearchForm;
import com.copyright.ccc.web.transaction.TransactionConstants;

public class BasicSearchAction extends BaseSearchAction
{
    //*******************************************************************
    //  simple performs an initial, basic, title or standard number search.
    
    public ActionForward go( ActionMapping mapping
                           , ActionForm form
                           , HttpServletRequest request
                           , HttpServletResponse response )
    {
        SearchForm searchForm = castForm( SearchForm.class, form ); 
        String destination;
        if (searchForm != null){
	        if (searchForm.getSearchCriteria().getTitleOrStdNo().equalsIgnoreCase("rightslink"))
	        {
	       	    return mapping.findForward( "rightslink" );
	        }
	        searchForm.setLastSearch( FWD_SEARCH_SIMPLE );
	        searchForm.setSearchType( SIMPLE_SEARCH);
	        searchForm.setAll( "on" );
	        searchForm.setSearchFlags( SIMPLE_SEARCH );        
	        searchForm.persistTypesOfUse();
	        searchForm.setPublisher( null );
	        searchForm.setAuthorEditor( null );
	        searchForm.setVolume( null );
	        searchForm.setEdition( null );
			/*
			 * added to erase any prior permissions direct search that left it's 
			 * specific WrWrkInst value on the form.
			 */
	        searchForm.setWrWrkInst(null);
	        //Reset Source
	        searchForm.setSource(SearchForm.NORMAL_SOURCE);
        }
        
        String si = UserContextService.getSessionInitiator();
        if ((si != null) && !si.equals( "" )) {
        	UserContextService.setSessionInitiator( TransactionConstants.CC_ORDER_SOURCE_CODE );
        }
        if (searchForm != null && notNull( searchForm.getTitleOrStdNo() ))
        {
            //  Perform our search...
            
            int count = doSearch(searchForm);
            
            searchForm.setLastSearch(SearchForm.SIMPLE_SEARCH);
            if (searchForm.getSource().equals( SearchForm.OPENURL_SOURCE ) && count == 1)
            {
                searchForm.synchronizeTypesOfUse();
                getResults(searchForm);
                destination = FWD_LANDING;
                updateFormValues( request, searchForm, count );
                searchForm.setLastPage( destination );
                Publication pubs[] = searchForm.getResults();
                searchForm.setSelectedItem( pubs[0] );
            }
            else
            {
                if (count > 0)
                {
                    getResults(searchForm);
                    destination = FWD_SUCCESS_SIMPLE;
                    updateFormValues(request, searchForm, count);
                    searchForm.setLastPage(destination);
                }
                else
                {
                    count = doSecondarySearch( 
                        searchForm,
                        stripStopwords(searchForm.getTitleOrStdNo()),
                        Boolean.TRUE
                    );
                    if (count > 0)
                    {
                        getResults(searchForm);
                        destination = FWD_SUCCESS_SIMPLE;
                        updateFormValues(request, searchForm, count);
                        searchForm.setLastPage(destination);
                    }
                    else
                    {
                        //  2011-06-28 MSJ
                        //  At this point we need to query our "search instead"
                        //  service to hopefully get back some alternative options
                        //  for the user.
                        
                        String[] items = null;
                        List<String> results = getSuggestions(searchForm);
                        
                        if (results != null)
                        {
                            items = results.toArray(new String[results.size()]);
                        }
                        searchForm.setSearchInstead(items);
    
                        destination = FWD_NO_RESULTS;
                    }
                }
            }
            searchForm.clearPageRange();
            searchForm.calculatePageRange();
        }
        else
        {
            //  Log this... and redirect to the simple search page, for now.
            
            destination = FWD_FAILURE;
            addError( request, "Search failed.  Reason:  Search form was null." );
        }
        putForms(request, searchForm, null);
        return mapping.findForward( destination );
    }
}
