/*
 * NotAvailableEmailAction.java
 * Copyright (c) 2007, Copyright Clearance Center, Inc. All rights reserved.
 * ----------------------------------------------------------------------------
 * Revision History
 * 2007-03-31   mjessop      Created.
 * 2007-10-15   tmckinney    Read email config data from CC2Configuration.
 * 2010-02-12   tmckinney    Removed send email code [CC-755].
 * ----------------------------------------------------------------------------
 */
package com.copyright.ccc.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.CCException;
import com.copyright.ccc.business.data.Publication;
import com.copyright.ccc.business.data.PublicationPermission;
import com.copyright.ccc.business.services.search.RightAdapter;
import com.copyright.ccc.business.services.search.WebServiceSearch;
import com.copyright.ccc.business.services.search.WorkRightsAdapter;
import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.NotAvailableEmailForm;
import com.copyright.ccc.web.forms.SearchForm;
import com.copyright.ccc.web.util.CatalogRequestDAO;
import com.copyright.ccc.web.util.NotAvailable;

public class NotAvailableEmailAction extends CCAction
{
	private static Logger _logger = Logger.getLogger(NotAvailableEmailAction.class);
	
    //  defaultOperation is called when no operation was
    //  specified for this class to perform.
    
    public ActionForward defaultOperation( ActionMapping       mapping
                                         , ActionForm          form
                                         , HttpServletRequest  request
                                         , HttpServletResponse response )
    {
        return displayForm( mapping, form, request, response );
    }
    
    public ActionForward displayForm( ActionMapping       mapping
                                    , ActionForm          form
                                    , HttpServletRequest  request
                                    , HttpServletResponse response )
    {
        StringBuffer obuf = new StringBuffer();
        
        //SearchServices _service = new SearchServices();
        
         WebServiceSearch webServiceSearch = new WebServiceSearch();
        
        //  Grab our form, create a new one if we must.
        
        NotAvailableEmailForm emailForm = castForm( NotAvailableEmailForm.class, form );
        if (emailForm == null) emailForm = new NotAvailableEmailForm();
    
        //  Gather data, stuff in form, display the input form
        //  to gather more info. from the user.  Start by grabbing
        //  our URL parameters.
        
        String item = request.getParameter( "item" );
        String perm = request.getParameter( "perm" );
        
        if (_logger.isDebugEnabled()) {
            _logger.info("\nNotAvailableEmailAction: ITEM = " + item + "; PERM = " + perm + "\n");   
        }
        
        //  They can't be null.
        
        if ((item == null) || (perm == null))
        {
            return mapping.findForward( "failure" );
        }
        
        //  Convert our numeric string to an actual numeric
        //  value.
        
        long item_no;

        try
        {
        	item_no = Long.parseLong( item );
        }
        catch (Exception e)
        {
        	return mapping.findForward( "failure" );
        }

        //  Get our publication and the correct permission.

        Publication pub = webServiceSearch.getSingleItemByKey( item_no );
        
        SearchForm searchForm = new SearchForm();
        searchForm.setSelectedItem( pub );
        searchForm.setAll("on");
        
        PublicationPermission publicationPermission = null;
    	for (RightAdapter right : ((WorkRightsAdapter)pub).getAdaptedRights()){
    		if (right.getRgtInst()==searchForm.getSelectedRightInst()){
    			publicationPermission = right;
    			break;
    		}
    	}

        request.setAttribute( WebConstants.RequestKeys.PUBLICATION_PERMISSION, publicationPermission );

        if (_logger.isDebugEnabled()) _logger.info(obuf.toString());

        //  Create our NotAvailable object and populate it.

        String rgthldr = "";
        
        if (publicationPermission != null){
        	rgthldr = publicationPermission.getRightsholderName();
        }

        NotAvailable itemData = new NotAvailable();

        itemData.setProduct( perm);
        itemData.setTitle( pub.getMainTitle() );
        
        String author = pub.getMainAuthor();
        if (author == null || author.equals("")) author = pub.getMainEditor();
        
        itemData.setAuthor( author );
        itemData.setVolume( pub.getVolume() + "/" + pub.getEdition() );
        itemData.setStandardNumber( pub.getMainIDNo() );
        itemData.setPublisher( pub.getMainPublisher() );
        
        if (rgthldr == null || rgthldr.equals("")) rgthldr = pub.getRightsholderNames();
        
        itemData.setRightsholder( rgthldr );
        itemData.setPublicationYear( pub.getPublicationYearRange() );
        
        //  Set our additional metadata fields for the Summit project.
        
        itemData.setIdnoLabel( pub.getIdnoLabel() );
        itemData.setSeries( pub.getSeries() );
        itemData.setSeriesNumber( pub.getSeriesNumber() );
        itemData.setPublicationType( pub.getPublicationType() );
        itemData.setPages( pub.getPages() );
        itemData.setCountry( pub.getCountry() );
        itemData.setLanguage( pub.getLanguage() );
    
        //  Stuff this into our form, stuff our form into the session,
        //  and move onto the page itself!
    
        emailForm.setData( itemData );
        request.getSession().setAttribute( WebConstants.SessionKeys.NOT_AVAILABLE_EMAIL_FORM, emailForm );
            
        return mapping.findForward( "showMain" );
    }
    
    public ActionForward sendRequest( ActionMapping       mapping
                                    , ActionForm          form
                                    , HttpServletRequest  request
                                    , HttpServletResponse response )
    {
        //  Pull together the details of the item the user is
        //  requesting, then send a message to the business folk.    	
    	
        NotAvailableEmailForm emailForm = castForm( NotAvailableEmailForm.class, form );
        
        //store the message in the catalog request table, scr1113       
        persistCatalogRequest(emailForm);//SCR1113
        return mapping.findForward( "success" );
    }
    
    private void persistCatalogRequest (NotAvailableEmailForm catalogrequest)
    {
    	String err = null;
    	CatalogRequestDAO _dao = new CatalogRequestDAO();

		try
		{
			_dao.persist(
    		    catalogrequest.getProduct(), catalogrequest.getTitle(),
    		    catalogrequest.getPublisher(), catalogrequest.getAuthor(),
    		    catalogrequest.getVolume(), catalogrequest.getStandardNumber(),
   			    catalogrequest.getRightsholder(), catalogrequest.getPublicationYear(),
   			    catalogrequest.getRequesterName(), catalogrequest.getRequesterEmailAddress(),
   			    catalogrequest.getRequesterCompany(), catalogrequest.getRequesterPhoneNumber(),
   			    catalogrequest.getRequesterCity(), catalogrequest.getRequesterState(),
   			    catalogrequest.getRequesterAnnualLicense(),
                catalogrequest.getAdditionalInfo());
		}
		catch(CCException e)
		{
			err = "Product=" + catalogrequest.getProduct() + ", Title=" + catalogrequest.getTitle()
					+ ", Publisher=" + catalogrequest.getPublisher() + ": ";
			_logger.error("CatalogRequest failed to persist parameters for " + err + e.getMessage());
		
		}
    }
    
}