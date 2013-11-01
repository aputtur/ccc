package com.copyright.ccc.web.actions;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.business.data.Publication;
import com.copyright.ccc.business.data.PublicationPermission;
import com.copyright.ccc.business.services.ecommerce.PurchasablePermission;
import com.copyright.ccc.business.services.search.RightAdapter;
import com.copyright.ccc.business.services.search.WorkRightsAdapter;
import com.copyright.ccc.util.LogUtil;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.SearchForm;

//  O.K.  Here is the idea of what this class is used for...
//
//  The user hits the PermissionsDirect or OpenURL action.  That action
//  strips some parameters, sets us up for the bomb, I mean, sets up some
//  form parameters, then zaps it off to this action.
//
//  This action performs the actual search, then determines where the user
//  should end up with these fancy, new search results.  For either type of
//  search, if more than one item is returned we must go to the simple search
//  results and let the user pick.
//
//  For PermissionsDirect, we want to try and put the user right into the 
//  shopping cart with the item they wanted.  If the permission does not 
//  allow this, we drop them into the landing page and let them decide
//  what to do next.
//
//  For OpenURL, we always want to fall into the landing (detail) page.

public class AutoSearchAction extends SearchAction
{
    
    //  defaultOperation is called when no operation was
    //  specified for this class to perform.  We are OVERRIDING
    //  the default operation in BaseSearchAction.
	@Override
    public ActionForward defaultOperation( ActionMapping       mapping
                                         , ActionForm          form
                                         , HttpServletRequest  request
                                         , HttpServletResponse response )
    {
        return go(mapping, form, request, response);
    }
    
    //*******************************************************************
    //  complex performs an initial search with multiple parameters.
    
    public ActionForward go( ActionMapping       mapping
               , ActionForm          form
               , HttpServletRequest  request
               , HttpServletResponse response )
    {
        String destination = BaseSearchAction.FWD_NO_RESULTS;
        SearchForm searchForm = (SearchForm) request.getSession().getAttribute( WebConstants.SessionKeys.SEARCH_FORM);
        PurchasablePermission purchasablePermission = null;
        Publication pubs[] = null;
        int count;
        int publicationIndex = 0;
        
        if (searchForm != null)
        {
            searchForm.synchronizeTypesOfUse();
            
            count = doComplexSearch(searchForm);
            
            if(_logger.isDebugEnabled()) 
            {
                _logger.debug("**** AutoSearchAction ****");
                _logger.debug("Form synchronization has occurred.");
            }
            
            if (count > 0) 
            {
                getResults(searchForm);
            }
            
            //  If the user specifies a year in their OpenURL query,
            //  we must check the result against it and adjust our
            //  result count accordingly.
            
            if (searchForm.getSource().equals( SearchForm.OPENURL_SOURCE ) &&
                (count == 1) && (searchForm.getYear() != null))
            {
                Publication[] pub = searchForm.getResults();
                
                if (!dateMatches( pub[0], searchForm.getYear() ))
                {
                    count = 0;
                }
                if (_logger.isDebugEnabled()) 
                {
                    _logger.debug(
                        "\nAutoSearch Count for year " 
                        + searchForm.getYear() + " is " 
                        + String.valueOf(count) + "."
                    ); 
                }
            }

            //  2012-11-12  MSJ
            //  There is a rare case where year ranges are included in work titles.
            //  The 9999-9999 format is identical to ISSN formats.  We have been
            //  getting ISSN searches that match the year in the titles and that
            //  has been mucking up OpenURL search results.  We need to filter
            //  against multiple search results if an ISSN has been specified
            //  and multiple results are returned.

            if (searchForm.getSource().equals( SearchForm.OPENURL_SOURCE ) &&
                searchForm.getIsIssnSearch() && count > 1)
            {
                Publication[] publications = searchForm.getResults();

                for (int i = 0; i < publications.length; i++)
                {
                    if (publications[i].getMainIDNoType().equals( "VALISSN" ) &&
                       (publications[i].getIdnoWop().equals( searchForm.getTitleOrStdNo() ) ||
                        publications[i].getMainIDNo().equals( searchForm.getTitleOrStdNo() )))
                    {
                        publicationIndex = i;
                        count = 1;
                        break;
                    }
                }
            }

            //  We need to act differently if we are PD vs. OpenURL.  PD wants to
            //  barge directly into the shopping cart while OpenURL wants to stop
            //  and smell the roses at the landing page.  Both of these depend on
            //  there being only one item, so first, let us eliminate the possibility
            //  that there are more than one search results...
            
            if (count > 1)
            {
                destination = FWD_SUCCESS_SIMPLE;
                searchForm.setLastPage( destination );
                updateFormValues(request, searchForm, count);
            }
            else
            {
                if (count == 0)
                {
                    destination = FWD_NO_RESULTS;
                }
                else
                {
                    if (searchForm.getSource().equals( SearchForm.PD_SOURCE ))
                    {   
                    	//setRightInstinUserContext(form);
                    	if (_logger.isDebugEnabled()) 
                        {
                            _logger.debug("Begin process of generating PurchasablePermission.");  
                        }
                        //  If we get here, the assumption is that we have
                        //  one and only one search result.  Yay!  Now we can
                        //  look at our source and determine where to go next.
                            
                        pubs = searchForm.getResults();
                        searchForm.setSelectedItem( pubs[0] );
                        searchForm.setSelectedWrkInst(Long.valueOf(pubs[0].getWrkInst()).toString());
                        Boolean isBiactive=checkIfBiactive(mapping, searchForm, request, response);
                        searchForm.setBiactive(isBiactive);
                        
                        //if(isBiactive && !StringUtils.isEmpty(searchForm.getSelectedPubYear())){
                        processOptions(mapping, searchForm, request,response);
                                        
                        //  Since PD defines that only one type of use be provided, we KNOW that
                        //  (ha ha, we know, I kill myself sometimes) we will get back only one
                        //  item in the permissions array, and that we need to stuff that baby
                        //  into the response object.
                        PublicationPermission publicationPermission = null;
                        if (searchForm.getSelectedRightInst()!=0){
                        	for (RightAdapter right : ((WorkRightsAdapter)pubs[0]).getAdaptedRights()){
                        		if (right.getRgtInst()==searchForm.getSelectedRightInst()){
                        			publicationPermission = right;
                        			break;
                        		}
                        	}
                        }
 
                        request.setAttribute( WebConstants.RequestKeys.PUBLICATION_PERMISSION, publicationPermission );
                                        
                        //  Pull out our parameters for the next step...
                                        
                        String chapter = (String) request.getSession().getAttribute( WebConstants.SessionKeys.CHAPTER_ARTICLE );
                        String fpage = (String) request.getSession().getAttribute( WebConstants.SessionKeys.FIRST_PAGE );
                        String lpage = (String) request.getSession().getAttribute( WebConstants.SessionKeys.LAST_PAGE );
                        String pages = (String) request.getSession().getAttribute( WebConstants.SessionKeys.PAGES );
                        String copies = (String) request.getSession().getAttribute( WebConstants.SessionKeys.COPIES );
                                        
                        int numpages = 0;
                        if (pages != null) numpages = Integer.parseInt( pages );
                                        
                        int numcopies = 0;
                        if (copies != null) numcopies = Integer.parseInt( copies );
                                        
                        String pagerange = null;
                        if (fpage != null) pagerange = fpage;
                        
                        if (lpage != null) 
                        {
                            if (fpage != null) pagerange = pagerange + "-" + lpage;
                            else pagerange = lpage;
                        }
                                   
                        if (_logger.isDebugEnabled()) 
                        {
                            _logger.debug("All results/parameters extracted.");
                        }
                        
                        if (searchForm.isTypesOfUseSelectedAllOn()) 
                        {
                            searchForm.setSearchFlags( FWD_SEARCH_SIMPLE );
                            searchForm.persistTypesOfUse();
                            destination = FWD_LANDING;
                            putForms(request, searchForm, null);
                            return mapping.findForward( destination );
                        }
                   	
                        boolean clickThroughToPriceAndOrderPageEnabled = false;
                        
                        // **** clickThroughToPriceAndOrderPageEnabled flag will force
                        // **** user to the permission summary page where the first 
                        // **** type of use is selected and the Price & Order button
                        // **** will be clicked to send the user to the pricing page
                        // **** with various parameters submitted with the /cccdirect
                        // **** URL.
                        // **** 9/15/2010 - Meeting w/ Jay, Nicole was decided to 
                        // **** disable this click-through. 
                        // **** The code is left here in place so that it can be
                        // **** enabled if needed.
                        // **** Bob D'Innocenzo
                        searchForm.makeSureRlrIsOff();
                       	searchForm.persistTypesOfUse();

                       	if ( clickThroughToPriceAndOrderPageEnabled ) {
                       		destination = FWD_PERMISSIONS_DIRECT;
                       	} else {
                       		// go to permissions page
                       		destination = FWD_LANDING;
                       	}
                    
                    } // END SearchForm.PD_SOURCE Processing
                    
                    if (searchForm.getSource().equals( SearchForm.OPENURL_SOURCE )) 
                    {
                        //  Let the user choose his or her type of use.
                        
                        destination = FWD_LANDING;
                        updateFormValues(request, searchForm, count);
                        searchForm.setLastPage( destination );
                        pubs = searchForm.getResults();
                    
                        if ((pubs != null) && (count == 1))
                        {
                            //  Tie-in for WORLDCAT!
                            
                            String mainIdType = pubs[publicationIndex].getMainIDNoType();
                            String worldCatUrl = "";
                            String oclc = pubs[publicationIndex].getOclcNum();
                        
                            if ( (mainIdType).equalsIgnoreCase("VALISSN") || (mainIdType).equalsIgnoreCase("SERIESISSN") || (mainIdType).equalsIgnoreCase("FORMISSN" )) {
                                worldCatUrl = "http://worldcat.org/issn/" + pubs[publicationIndex].getIdnoWop();
                                searchForm.setWorldCatDisplay(true);
                            }
                            else if ((mainIdType).equalsIgnoreCase("VALISBN10") || (mainIdType).equalsIgnoreCase("VALISBN13")) {
                                worldCatUrl = "http://worldcat.org/isbn/" + pubs[publicationIndex].getIdnoWop() ;
                                searchForm.setWorldCatDisplay(true);
                            }
                            else if ( oclc != null && !oclc.equals("")) {
                                worldCatUrl = "http://worldcat.org/oclc/" + pubs[publicationIndex].getOclcNum() ;
                                searchForm.setWorldCatDisplay(true); 
                            }
                            else 
                            {
                                searchForm.setWorldCatDisplay(false);
                            }
                            searchForm.setWorldCatUrl(worldCatUrl);
                        
                            searchForm.setSelectedItem( pubs[publicationIndex] );
                            searchForm.setSelectedWrkInst(Long.valueOf(pubs[publicationIndex].getWrkInst()).toString());
                        }
                        Boolean isBiactive=checkIfBiactive(mapping, searchForm, request, response);
                        searchForm.setBiactive(isBiactive);
                        
                        //if(isBiactive && !StringUtils.isEmpty(searchForm.getSelectedPubYear())){
                        processOptions(mapping, searchForm, request,response);
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
        putForms( request, searchForm, null );
        return mapping.findForward( destination );
    }

    private boolean dateMatches( Publication publication, String specifiedYear )
    {
        try 
        {
            String strDateLow = publication.getPublicationStartDate();
            String strDateHigh = publication.getPublicationEndDate();
            
            //  We need to make sure we have a valid year of some
            //  some sort for the date matching.  If our dates are
            //  NULL (which in my opinion they should not be) we 
            //  assume the lowest/highest values.  Per the format 
            //  I specified above (yyyyMMdd) we want to tack on 
            //  the "0101" for month and day...  Probably do not need 
            //  to do this but I feel safer specifying the month and
            //  day.
            
            if (strDateLow != null) {
                strDateLow = strDateLow.substring(0, 4);
                strDateLow += "0101";
            }
            else {
                strDateLow = "10000101";
            }
            if (strDateHigh != null) {
                strDateHigh = strDateHigh.substring(0, 4);
                strDateHigh += "0101";  
            }
            else {
                strDateHigh = "30000101";
            }
            
            //  Parse our strings into actual date objects now.
            
            SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");

            Date dl = fmt.parse( strDateLow, new ParsePosition( 0 ) );
            Date dh = fmt.parse( strDateHigh, new ParsePosition( 0 ) );
            
            //  Create our calendar instances and set them to 
            //  the dates we just created.  Then pull out the years
            //  and compare them to the year provided in the URL.
            
            Calendar pubBeginDate = Calendar.getInstance();
            Calendar pubEndDate = Calendar.getInstance();
            
            pubBeginDate.setTime( dl );
            pubEndDate.setTime( dh );
            
            int pubBeginYear = pubBeginDate.get( Calendar.YEAR );
            int pubEndYear = pubEndDate.get( Calendar.YEAR );
            int yearSpecified = new Integer( specifiedYear ).intValue();

            if ((yearSpecified < pubBeginYear) ||
                (yearSpecified > pubEndYear))
            {
                return false;
            }
        }
        catch(Exception e) 
        {
            //  Added this try block to catch the rare occasion
            //  where the dates in the publication are null.
            
            return true;
        }
        return true;
    }
}
