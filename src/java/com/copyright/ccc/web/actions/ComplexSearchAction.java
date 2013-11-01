package com.copyright.ccc.web.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.business.data.CCSearchCriteria;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.search.SearchCriteriaImpl;
import com.copyright.ccc.web.forms.SearchForm;
import com.copyright.ccc.web.transaction.TransactionConstants;

public class ComplexSearchAction extends BaseSearchAction
{
    //*******************************************************************
    //  complex performs an initial search with multiple parameters.
    
	public ActionForward go( ActionMapping       mapping
			, ActionForm          form
			, HttpServletRequest  request
			, HttpServletResponse response )
	{
		SearchForm searchForm = castForm( SearchForm.class, form );
		String destination;
		int count = 0;

		if (searchForm != null)
		{

			if (searchForm.getSearchCriteria().getTitleOrStdNo().equalsIgnoreCase("rightslink") 
					|| searchForm.getSearchCriteria().getPublisher().equalsIgnoreCase("rightslink"))
			{
				return mapping.findForward( "rightslink" );
			}
			/*
			 * added to erase any prior permissions direct search that left it's 
			 * specific WrWrkInst value on the form.
			 */
			searchForm.setWrWrkInst(null);
			/*
			 * this following is used to signal the searchResult.jsp to 
			 * expand the permission options for the first item
			 * in the results list. This was added to satisfy the use of the ppuSearch.do url
			 * that SFX is using as a sort of "permissions direct".
			 */			
			if(mapping.getPath()!=null && mapping.getPath().equals("/ppuSearch")) {
				searchForm.setSearchFlags("ppu");
				request.setAttribute("DISPLAY_FIRST_RESULTS_PERMISSIONS", Boolean.TRUE);
			}
			searchForm.persistTypesOfUse();

			String si = UserContextService.getSessionInitiator();
			if ((si != null) && !si.equals( "" )) {
				UserContextService.setSessionInitiator( TransactionConstants.CC_ORDER_SOURCE_CODE );
			}
			_logger.debug("\n\nSEARCH PHASE 1\n\n");
			
			count = doSearch(searchForm);
			searchForm.setLastSearch(SearchForm.ADVANCED_SEARCH);

			if (count > 0)
			{
				getResults(searchForm);
				destination = FWD_SUCCESS;
				updateFormValues( request, searchForm, count );
				searchForm.setLastPage(destination);
			}
			else
			{
			    _logger.debug("\n\nSEARCH PHASE 2\n\n");
			    
			    //  2011-07-06 MSJ
			    //  We need to try a couple other options before giving up on this search.
			    //  We need to do this because NOW we are offering alternative searches for
			    //  the user should their search return no results.  Start by trying to
			    //  strip the provided title of any words that might be extraneous.
                
                String oldTitle = searchForm.getTitleOrStdNo();
                String newTitle = stripStopwords(oldTitle);

                _logger.debug("\n\n**** NEW TITLE TO SEARCH:  " + newTitle + "\n\n");
                
                count = doSecondarySearch(searchForm, newTitle, null);
			    
			    if (count > 0)
			    {
    				getResults(searchForm);
    				destination = FWD_SUCCESS;
    				updateFormValues( request, searchForm, count );
    				searchForm.setLastPage(destination);
			    }
			    else
			    {
			        _logger.debug("\n\nSEARCH PHASE 3 " + searchForm.getTitleOrStdNo() + "\n\n");
			        
			        //  Finally, if stopwords fail, we must try to perform a
			        //  a search with the limiters and additional search parameter
			        //  fields cleared out.  If THIS returns no results, we then
			        //  obtain a list of potential titles that the user might
			        //  want to search instead.
			        
                    count = doSecondarySearch(searchForm, newTitle, Boolean.TRUE);
                    
                    if (count > 0)
                    {
                        getResults(searchForm);
        				destination = FWD_SUCCESS;
        				updateFormValues( request, searchForm, count );
        				searchForm.setLastPage(destination);
                    }
                    else
                    {
                        _logger.debug("\n\nSEARCH INSTEAD LOOKUP " + searchForm.getTitleOrStdNo() + "\n\n");
                        
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
		}
		else
		{
			//  Log this... and redirect to the simple search page, for now.

			destination = FWD_FAILURE;
			addError( request, "Search failed.  Reason:  Search form was null." );
		}
		putForms( request, searchForm, null);
		return mapping.findForward( destination );
	}
    
    public ActionForward clearParameters( ActionMapping       mapping
            , ActionForm          form
            , HttpServletRequest  request
            , HttpServletResponse response )
    {

    	SearchForm searchForm = castForm( SearchForm.class, form );
    	searchForm.clearSearchCriteria();
    	
    	searchForm.setSelectedCountries(null);
    	searchForm.setSelectedPubTypes(null);
    	searchForm.setSelectedLanguages(null);
    	return mapping.findForward( FWD_SUCCESS );
    }
}
