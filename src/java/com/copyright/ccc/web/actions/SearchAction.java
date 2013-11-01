package com.copyright.ccc.web.actions;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.copyright.base.CCCRuntimeException;
import com.copyright.ccc.business.data.CCUser;
import com.copyright.ccc.business.data.OrderPurchase;
import com.copyright.ccc.business.data.Publication;
import com.copyright.ccc.business.data.PublicationPermission;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.OrderDataSourceEnum;
import com.copyright.ccc.business.services.SystemStatus;
import com.copyright.ccc.business.services.cart.PurchasablePermission;
import com.copyright.ccc.business.services.cart.PurchasablePermissionFactory;
import com.copyright.ccc.business.services.order.OrderPurchaseServices;
import com.copyright.ccc.business.services.order.OrderPurchasesException;
import com.copyright.ccc.business.services.search.RightAdapter;
import com.copyright.ccc.business.services.search.SearchState;
import com.copyright.ccc.business.services.search.WorkRightsAdapter;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.LoginForm;
import com.copyright.ccc.web.forms.SearchForm;
import com.copyright.ccc.web.forms.coi.CartForm;
import com.copyright.ccc.web.forms.coi.RLinkSpecialOrderForm;
import com.copyright.ccc.web.util.PermSummaryUtil;
import com.copyright.ccc.web.util.PermissionCategoryDisplay;
import com.copyright.ccc.web.util.RightsLnkQPriceUtil;
import com.copyright.svc.rightsResolver.api.data.PermissionSummaryCategory;

/*
    NOTES:  I would like to pseudo-document the flow during search and order.
    
            1.  Perform a basic or advanced search.  This particular action is
                for advanced search.
                a.  In the basic search, when the WebServiceSearch is invoked
                    it DOES NOT RETURN RIGHTS.  Why bother if you are heading
                    to the landing page anyway, right?
                b.  Advanced search DOES set the rights, you can see that at
                    around line 122 (at the time of this comment).
            2.  Once you have successfully searched you will end up with either
                basic or advanced search results, or you might (especially if
                coming from the basic search) already be on the search landing
                page.
            3.  From advanced search, or search landing page you can add items
                to the cart.  Doing so invokes either BasicAddCartItemAction or
                SpecialAddCartItemAction, which then carries you to the
                appropriate JSP.
            4.  From there you add your item to the cart, or you can return
                to search results to choose a different type of use, or start
                a whole new search.
                
    Change Log

    when        who         what
    ----------  ----------  --------------------------------------------------------
    2009-03-17  Jessop      Added notes on flow.
    2008-13-03  Jessop      Added code to recover from a loss of context when adding
                            to an existing order after going back to select a dif-
                            erent type of use.  Also did a little cleanup.
*/

public class SearchAction extends BaseSearchAction
{

    
    /**
     * handles the case when the user selects "more permission options" from the 
     * advanced search results page. This will pre-emptively turn on display 
     * for all permission categories
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward showAllPermissionOptions( ActionMapping       mapping
    		, ActionForm          form
    		, HttpServletRequest  request
    		, HttpServletResponse response
    )
    {
        CCUser usr = UserContextService.getActiveAppUser();
        usr.setAllTypesOfUseSelected();

        return detail(mapping,form,request,response);
    }
    
    //*******************************************************************
    //  detail...
    public ActionForward detail( ActionMapping       mapping
                               , ActionForm          form
                               , HttpServletRequest  request
                               , HttpServletResponse response
                               )
    {
        super.analyzeParameters( request );
 
        /*
         * try to get the searchForm from the session
         */
        SearchForm searchForm = super.getSearchForm( request );
        
        /*
         * if it's not available, use the one passed
         */
        if (searchForm == null) {
        	searchForm = castForm( SearchForm.class, form );
        }
        
        /*
         * update the SearchForm with the types of use currently selected
         * for display in the CCUser object  
         */
        searchForm.synchronizeTypesOfUse();        

        searchForm.setSelectedRightHolderInst(0);
        if(request.getParameter("detailType")!=null){
        	searchForm.setSelectedPubYear("");
        }
        //if(request.getParameter("detailType")!=null){
        searchForm.setSelectedTou("");
        //}
       
        searchForm.setSelectedWrkInst((String)request.getAttribute( ITEM ));
 
        Boolean isBiactive=checkIfBiactive(mapping, searchForm, request, response);
        searchForm.setBiactive(isBiactive);
        
        //if(isBiactive && !StringUtils.isEmpty(searchForm.getSelectedPubYear())){
        processOptions(mapping, searchForm, request,response);
        //}
    	return mapping.findForward(FWD_TOU_DETAIL);
    }
    
    /**
     * This method is used by the Advanced Search and called by a jquery.load() function to imbed the
     * permission summary Advanced Search results page when the user chooses to "View Selected Permissions Options"
     * If this is a biactive document then the user is send back a jsp to prompt for the Publication Year.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return I think this is never used So I am commenting to test
     */
/*   public ActionForward filterPermissions( ActionMapping       mapping
            , ActionForm          form
            , HttpServletRequest  request
            , HttpServletResponse response
            )
    {
  	  SearchForm searchForm = super.getSearchForm( request );
      long item = new Long((String) searchForm.getSelectedWrkInst()).longValue();
      //First check if the publication is biactive
     WorkRightsAdapter pub = (WorkRightsAdapter)searchForm.getPublication(item);
      
      if (pub == null){
     	 
     	  this only happens when we didn't find a pub in the users
     	  * session - such as when it has timed out.
     	  * We don't want TF rights to get loaded because we'll let
     	  * the RightsResolver service do that (see further down)
     	  
         pub = (WorkRightsAdapter)_WebSearchService.getSingleItemByKey( item, false );
      }

    	processDetail(mapping,searchForm,request,pub);   	
    	return mapping.findForward(PERM_OPTIONS_PAGE);
    }*/
    
    public ActionForward processOptions(ActionMapping       mapping
    , ActionForm          form
    , HttpServletRequest  request
    , HttpServletResponse response
    )
    {
    	SearchForm searchForm = super.getSearchForm( request );
    	String workInst = searchForm.getSelectedWrkInst();
        long item = new Long(workInst).longValue();
        WorkRightsAdapter pub = (WorkRightsAdapter)searchForm.getPublication(item);
        
        if (pub == null){
       	 /*
       	  * this only happens when we didn't find a pub in the users
       	  * session - such as when it has timed out.
       	  * We don't want TF rights to get loaded because we'll let
       	  * the RightsResolver service do that (see further down)
       	  */
           pub = (WorkRightsAdapter)_WebSearchService.getSingleItemByKey( item, false );
        }
        
      //selected year
      //  pub.filterRights(searchForm.getSelectedPubYear());
        
        //now process the detail to display the permission summary
        processDetail(mapping, searchForm, request,pub);
    	return mapping.findForward(PERM_OPTIONS_PAGE);
    }

    public ActionForward processPPUOptions( ActionMapping       mapping
                                          , ActionForm          form
                                          , HttpServletRequest  request
                                          , HttpServletResponse response )
    {
        SearchForm searchForm = super.getSearchForm( request );
        String workInst = searchForm.getSelectedWrkInst();
        long item = new Long(workInst).longValue();
        WorkRightsAdapter pub = (WorkRightsAdapter)searchForm.getPublication(item);
        
        if (pub == null) {
         /*
          * this only happens when we didn't find a pub in the users
          * session - such as when it has timed out.
          * We don't want TF rights to get loaded because we'll let
          * the RightsResolver service do that (see further down)
          */
           pub = (WorkRightsAdapter)_WebSearchService.getSingleItemByKey( item, false );
        }
        
        //now process the detail to display the permission summary
        processDetail(mapping, searchForm, request,pub);
        return mapping.findForward(PERM_OPTIONS_PAGE_PPU_ONLY);
    }
    
    private void processDetail( ActionMapping       mapping
            , SearchForm          form
            , HttpServletRequest  request
            , WorkRightsAdapter pub)   {
         
    	
    	//SearchForm searchForm = super.getSearchForm( request );
    	//filter the set of rights on the publication so we only have those for the
        //selected year
 
    	
    	if (pub != null){
    		pub.filterRights(form.getSelectedPubYear());	
    	
    	 	
         String mainIdType = pub.getMainIDNoType();
         String worldCatUrl = "";
         String oclc = pub.getOclcNum();
            if ( (mainIdType).equalsIgnoreCase("VALISSN") || (mainIdType).equalsIgnoreCase("SERIESISSN") || (mainIdType).equalsIgnoreCase("FORMISSN" )) 
                    {
                    worldCatUrl = "http://worldcat.org/issn/" + pub.getIdnoWop() ;
                    form.setWorldCatDisplay(true);
                    }
            else if ((mainIdType).equalsIgnoreCase("VALISBN10") || (mainIdType).equalsIgnoreCase("VALISBN13"))
                    {
                    worldCatUrl = "http://worldcat.org/isbn/" + pub.getIdnoWop() ;
                    form.setWorldCatDisplay(true);
                    }
            else if ( oclc != null && !oclc.equals(""))
                    {
                        worldCatUrl = "http://worldcat.org/oclc/" + pub.getOclcNum() ;
                        form.setWorldCatDisplay(true);
                        
                    }
            else {
                    form.setWorldCatDisplay(false);
                }
         
            form.setWorldCatUrl(worldCatUrl);
        }
         
        if (pub != null && 
            (pub.getAdaptedRights()==null || 
             pub.getAdaptedRights().size()==0)) 
        {
    		  //This call will establish Categories and TF Rights, if this is a TF work
              pub.setRRRights();
        }
        //Update display for form
        //build permission display for this one publication
        //Then place in SearchForm PermissionCategoryDisplay array 
        //which is used to re-layout the refreshed jsp
        if (pub != null)
        {
           List<PermissionSummaryCategory> lstPermSumCategories = pub.getPermCategories();
            
            	 
             if( UserContextService.getSearchState().getSearchCriteria().getDisplayablePermissions()!=null){ 	 
            	 Map <Long,String> displayablePermissions = UserContextService.getSearchState().getSearchCriteria().getDisplayablePermissions();
            	 List<PermissionCategoryDisplay> orderedPermSumDisplay = PermissionCategoryDisplay.createOrderedList(pub,lstPermSumCategories,displayablePermissions);
            	 form.setPermCatDisplay(orderedPermSumDisplay);
             }
        }

        
        //  We need to account for the possibility that we are being hit
        //  from the cart by a user who is adding to an existing order.
        //  I know this is a bit messy, but I didn't take into account
        //  all the possible ramifications of removing the flags set by
        //  orderHistory when performing an additional search.  See also
        //  basicTransaction.jsp.
        Boolean existingOrder = Boolean.valueOf(request.getParameter( EXISTING_ORDER ));
        if (existingOrder) {
            form.setOrderExists( true );
            request.getSession().setAttribute( WebConstants.SessionKeys.PARM_PURCHASE_ID, request.getParameter( WebConstants.RequestKeys.PURCHASE_ID ) );
            request.getSession().setAttribute( WebConstants.SessionKeys.PARM_EXISTING_ORDER, Boolean.TRUE );
        }
        
        form.setSelectedItem( pub );

     	form.setTeleSalesUp(SystemStatus.isTelesalesUp());
	    super.putForms(request, form, null);
    }
   
    //*******************************************************************
    //  add to cart...

    public ActionForward addToCart ( ActionMapping       mapping
            , ActionForm          form
            , HttpServletRequest  request
            , HttpServletResponse response)
    {
    	return addToCart(mapping, form, request, response,false);
    }
    
    public ActionForward addToCartRlnk(ActionMapping       mapping
            , ActionForm          form
            , HttpServletRequest  request
            , HttpServletResponse response)
    {
      	_logger.info( "addToCartRlnk" );
      	return initTitleSearchParms(mapping, form, request, response);
    	//return addToCart(mapping, form, request, response,true);
    }
    
    public ActionForward addToCartRlnkSpecialOrder(ActionMapping       mapping
            , ActionForm          form
            , HttpServletRequest  request
            , HttpServletResponse response)
    {
    	//This path is for Repub Special Orders from Perm Summary page
    	analyzeParameters( request );
        SearchForm searchForm  = getSearchForm( request );
        Long purchaseId = null;
        
        
     	UserContextService.getSearchState().setSelectedTou(null);
    	UserContextService.getSearchState().setSelectedTouId(null);
    	UserContextService.getSearchState().setSelectedRrTouId(null);
    	UserContextService.getSearchState().setSelectedCategoryId(null);
    	
    	searchForm.setSelectedRightInst(0);
    	
        PurchasablePermission purchasablePermission = null;
        String destination;
        PermissionSummaryCategory selectedCategory = null;
        
        searchForm.synchronizeTypesOfUse();

        /*
         * get the index number of the category the user clicked on
         */
        String selectedCategoryIndex = request.getParameter("idx");
        
        int idx = Integer.parseInt(selectedCategoryIndex);
                                
        if (selectedCategoryIndex != null) {
        	searchForm.setSelectedPermCatDisplayIndex(Integer.valueOf(selectedCategoryIndex));
        	selectedCategory = searchForm.getSelectedPermCatDisplay().getPermissionSummaryCategory();
        } else {
        	selectedCategory = null;
        }
        
        if (selectedCategory==null) {
        	throw new CCCRuntimeException("cannot determine which PermissionSummaryCategory was chosen by the user");
        }
        
        SearchState searchState = UserContextService.getSearchState();
        searchState.setSelectedPermissionType(searchForm.getSelectedPermCatDisplay().getCategoryDescription());
        UserContextService.setSearchState(searchState);
        
        long item = new Long((String) request.getAttribute( ITEM )).longValue();
        Publication pub = searchForm.getPublication(item);
        if (pub == null){
           pub = _WebSearchService.getSingleItemByKey( item );
        }
        
            
        searchForm.setSelectedItem( pub );
        
        request.getSession().setAttribute("PUBLICATION", pub);
        
        Boolean orderExists = (Boolean) request.getSession().getAttribute(WebConstants.SessionKeys.PARM_EXISTING_ORDER);
                
        if (Boolean.TRUE.equals(orderExists))
        {
            request.getSession().removeAttribute(WebConstants.SessionKeys.PARM_PURCHASE_ID);
            request.getSession().setAttribute(WebConstants.SessionKeys.PARM_EXISTING_ORDER, Boolean.FALSE);
          //*****************Fix for NRL-474 *******************************
            request.getSession().setAttribute("RLINK_SP_EXIST", Boolean.TRUE);
          //*****************END Fix for NRL-474 *******************************
        }
            
        
        	String path = "/specialOrderRLink.do?operation=defaultOperation";
            
            ActionForward forward = null;
            
            forward =  new ActionForward("submit", path, true, null);
            
            return forward;
               
    }
    
    public ActionForward addToCartRlnkSpecial(ActionMapping       mapping
            , ActionForm          form
            , HttpServletRequest  request
            , HttpServletResponse response)
    {
      	_logger.info( "addToCartRlnkSpecial" );
      	SearchForm searchForm = getSearchForm(request);
      	//request.setAttribute(ITEM, searchForm.getSelectedWrkInst());
      	//request.(URL_PARM_ITEMNO, searchForm.getSelectedWrkInst());
      	return initTitleSearchParmsSpecial(mapping, searchForm, request, response);
    	//return addToCart(mapping, form, request, response,true);
    }

    public ActionForward addToCart ( ActionMapping       mapping
                                   , ActionForm          form
                                   , HttpServletRequest  request
                                   , HttpServletResponse response
                                   , Boolean fromRightsLink)
    {
    	
    	Boolean orderExists = (Boolean) request.getSession().getAttribute(WebConstants.SessionKeys.PARM_EXISTING_ORDER);
    	Long purchaseId = null;
    	
    	
    	     	    	   	
    	setRightInstinUserContext(form);
        analyzeParameters( request );
        
        //setting userContext
        SearchForm searchForm = getSearchForm(request);
        PurchasablePermission pp = null;
        String destination;
        searchForm.synchronizeTypesOfUse();

        //save Permission Type in cache for Pricing page display
        String permType = (String)request.getAttribute(PERM);
        UserContextService.getSearchState().setSelectedPermissionType(permType);
        
   
        long item = new Long((String) request.getAttribute( ITEM )).longValue();
        //searchForm.getPublication(item);
        Publication pub =searchForm.getPublication(item);
        if (pub == null){
           pub = _WebSearchService.getSingleItemByKey( item );
        }
        
                
        if (pub==null && searchForm.getSource().equalsIgnoreCase(SearchForm.PD_SOURCE) ) 
        {
            LoginForm loginForm = new LoginForm();
            
            loginForm.setAutoLoginUsername(request.getParameter(WebConstants.RequestKeys.USERNAME));
            loginForm.setAutoLoginPassword(request.getParameter(WebConstants.RequestKeys.PASSWORD));
            loginForm.setAutoLoginForward("autoSearch.do");
            
            request.getSession().setAttribute("loginForm", loginForm);
            request.setAttribute("loginForm", loginForm);
            
            String autoLoginPath = "/autoLogin.do?operation=defaultOperation";
                
            if(_logger.isDebugEnabled())
            {
                _logger.debug( "PDAction: Auto login redirect!" );
                _logger.debug( "PDAction: Path = " + autoLoginPath );
            }
            request.getSession().setAttribute( "searchForm", searchForm );
            return new ActionForward( autoLoginPath );
        }
        
        if (pub==null)
        {
        	//return mapping.findForward(FWD_TOU_DETAIL);
        	ActionMessages errors = new ActionMessages();
            errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.searchAgain", "Incomplete Order data."));
            saveErrors(request, errors);
        	return mapping.findForward(FWD_SEARCH_SIMPLE);
        }
        
        searchForm.setSelectedItem( pub );
        
        PublicationPermission publicationPermission = null;
        int idx = ((Integer) request.getAttribute( IDX )).intValue();
        
        /*
         * get the index number of the category the user clicked on
         */
        if ( request.getAttribute( IDX ) != null) {
        	searchForm.setSelectedPermCatDisplayIndex(idx);
        }
        
        if (searchForm.getSelectedRightInst()!=0){
        	for (RightAdapter right : ((WorkRightsAdapter)pub).getAdaptedRights()){
        		if (right.getRgtInst()==searchForm.getSelectedRightInst() &&
        				( searchForm.getSelectedTpuInst()==0L || right.getTpuInst()==searchForm.getSelectedTpuInst()) ){
        			publicationPermission = right;
        			//redirect to special order if underlying TOU really has a special order permission
        			if (publicationPermission.isSpecialOrder()) {
        				searchForm.setSelectedRightHolderInst(0);
        				return addToCartSpecial(mapping, form, request, response);
        			}
        			else if (publicationPermission.isContactRHDirectly()){
        				return mapping.findForward(FWD_TOU_DETAIL);
        			}
        			else {
        				searchForm.setSelectedRightHolderInst(0);
        			}
        			break;
        		}
        	}
        }
        
             
        request.setAttribute(WebConstants.RequestKeys.PUBLICATION_PERMISSION, publicationPermission);
        
        //  Now we need to prepopulate the detail of the item if we are handling
        //  a PermissionsDirect or OpenURL request.
        
        if (searchForm.getSource().equals( SearchForm.OPENURL_SOURCE ) ||
            searchForm.getSource().equals( SearchForm.PD_SOURCE ))
        {        
            idx = ((Integer) request.getAttribute( IDX )).intValue();
            generatePurchasable( request, publicationPermission, searchForm, pp );
        }
        
                
        if (Boolean.TRUE.equals(orderExists)) 
        {
            purchaseId = (Long) request.getSession().getAttribute(WebConstants.SessionKeys.PARM_PURCHASE_ID);
            request.setAttribute( WebConstants.RequestKeys.PURCHASE_ID, purchaseId.toString() );
            
            if( publicationPermission!=null && publicationPermission.isSpecialOrder() )
                destination = FWD_ORDER_SPECIAL;
            else
                destination = FWD_ORDER;                            
        }
        else 
        {
            if( publicationPermission!=null && publicationPermission.isSpecialOrder() )
                destination = FWD_CART_SPECIAL_COI; // FWD_CART_SPECIAL;
            else if (fromRightsLink){
            	destination = FWD_CART_RLNK; 
            }else {
                destination = FWD_CART_COI; // FWD_CART;//  
            }
        }
        searchForm.setOrderExists( false );
        
        if (Boolean.TRUE.equals(orderExists)) 
        {
            //Long purchaseId = (Long) request.getSession().getAttribute(PARM_PURCHASE_ID);
            //request.setAttribute( WebConstants.PURCHASE_ID, purchaseId.toString() );
            
            OrderPurchase orderPurchase = null ;
            
            try {
    	    	orderPurchase = OrderPurchaseServices.getOrderPurchaseForConfirmationNumber(purchaseId);
    	    } catch (OrderPurchasesException opex) {
    	    	//throw new CheckoutException( "Unable to add item to order, couldn't find order: " + Long.parseLong(purchaseId.toString()));
    	    }
    	    
    	   //  if (orderPurchase==null) {
    	   // 	throw new CheckoutException( "null OrderPurchase returned for order " + purchaseId);
    	   // } 
    	    
    	    if (orderPurchase != null)
    	    {
    	    	if (orderPurchase.getOrderDataSource() == OrderDataSourceEnum.TF.getOrderDataSourceId().intValue()) 
    	    	{
    	    		PurchasablePermission purchasablePermission = PurchasablePermissionFactory.createPurchasablePermission( publicationPermission );
    	    		if (orderPurchase.isAcademic() && !purchasablePermission.isAcademic())
    	    		{
    	    			ActionMessages errors = new ActionMessages();
    	    			String acaMessage = "";
    	    			acaMessage = "<b><p>You can only add the following Permission Types:" +
    	    					"</p><p>1. Use in electronic course materials</p>" +
    	    					"<p>2. Use in Print Course materials</p></b>";
    	    	        errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.incorrectPermissionType", acaMessage));
    	    	        saveErrors(request, errors);
    	    			return mapping.findForward(FWD_TOU_DETAIL);
    	    		}
    	    		
    	    		if (!orderPurchase.isAcademic() && purchasablePermission.isAcademic())
    	    		{
    	    			ActionMessages errors = new ActionMessages();
    	    			String message = "";
    	    			message = "<b><p>You can only add the following Permission Types:" +
    	    					"</p><p>1. Deliver via Inter Library Loan (ILL) or document delivery</p>" +
    	    					"<p>2. Photocopy for general business or academic use</p></b>";
    	    	        errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.incorrectPermissionType", message));
    	    	        saveErrors(request, errors);
    	    			return mapping.findForward(FWD_TOU_DETAIL);
    	    		}
    	    	
    	    	}
    	    }
        }
        
        if (Boolean.TRUE.equals(orderExists)) 
        {                      
            request.getSession().removeAttribute(WebConstants.SessionKeys.PARM_PURCHASE_ID);
            request.getSession().setAttribute(WebConstants.SessionKeys.PARM_EXISTING_ORDER, Boolean.FALSE);
        }
                       
        return mapping.findForward( destination );
    }
    
    //*******************************************************************
    //  add to special order...

    public ActionForward addToCartSpecial ( ActionMapping       mapping
                                          , ActionForm          form
                                          , HttpServletRequest  request
                                          , HttpServletResponse response)
    {
        analyzeParameters( request );
        SearchForm searchForm  = getSearchForm( request );
        Long purchaseId = null;
        
        
     	UserContextService.getSearchState().setSelectedTou(null);
    	UserContextService.getSearchState().setSelectedTouId(null);
    	UserContextService.getSearchState().setSelectedRrTouId(null);
    	UserContextService.getSearchState().setSelectedCategoryId(null);
    	
    	searchForm.setSelectedRightInst(0);
    	
        PurchasablePermission purchasablePermission = null;
        String destination;
        PermissionSummaryCategory selectedCategory = null;
        
        searchForm.synchronizeTypesOfUse();

        /*
         * get the index number of the category the user clicked on
         */
        String selectedCategoryIndex = request.getParameter("idx");
        
        int idx = Integer.parseInt(selectedCategoryIndex);
                                
        if (selectedCategoryIndex != null) {
        	searchForm.setSelectedPermCatDisplayIndex(Integer.valueOf(selectedCategoryIndex));
        	selectedCategory = searchForm.getSelectedPermCatDisplay().getPermissionSummaryCategory();
        } else {
        	selectedCategory = null;
        }
        
        if (selectedCategory==null) {
        	throw new CCCRuntimeException("cannot determine which PermissionSummaryCategory was chosen by the user");
        }
        
        SearchState searchState = UserContextService.getSearchState();
        searchState.setSelectedPermissionType(searchForm.getSelectedPermCatDisplay().getCategoryDescription());
        UserContextService.setSearchState(searchState);
        
        long item = new Long((String) request.getAttribute( ITEM )).longValue();
        Publication pub = searchForm.getPublication(item);
        if (pub == null){
           pub = _WebSearchService.getSingleItemByKey( item );
        }
        
        if (pub == null)
        {
        	//return mapping.findForward(FWD_TOU_DETAIL);
        	ActionMessages errors = new ActionMessages();
            errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.searchAgain", "Incomplete Order data."));
            saveErrors(request, errors);
        	return mapping.findForward(FWD_SEARCH_SIMPLE);
        }
        
        searchForm.setSelectedItem( pub );
        
        PublicationPermission publicationPermission = null;
        
    	/*
    	 * if we have a selectedRightInst, we'll use it to determine which PublicationPermission
    	 * to use.
    	 */
        if (searchForm.getSelectedRightInst()!=0) {
        	for (RightAdapter right : ((WorkRightsAdapter)pub).getAdaptedRights()){
        		if (right.getRgtInst()==searchForm.getSelectedRightInst()){
        			publicationPermission = right;
        			if (publicationPermission.isContactRHDirectly()){
        				return mapping.findForward(FWD_TOU_DETAIL);
        			}
        			else {
        				searchForm.setSelectedRightHolderInst(0);
        			}
        			break;
        		}
        	}
        }

        /*
         * if it's a special order, the user is not presented with a list of types
         * of use. This means we won't have a rightInst available. In this case, the
         * PublicationPermission will still be null so we'll use the most permissable 
         * right for the category.
         */
        if (publicationPermission == null) {
        	publicationPermission = ((WorkRightsAdapter)pub).findMostPermissableRight(selectedCategory,searchForm.getSelectedPubYear());
        }
        
        if (publicationPermission == null) {
        	throw new CCCRuntimeException("unable to determine a PublicationPermission to use for category " + selectedCategory);
        }
        
        
        request.setAttribute(WebConstants.RequestKeys.PUBLICATION_PERMISSION, publicationPermission);

        if (searchForm.getSource().equals( SearchForm.OPENURL_SOURCE ) ||
            searchForm.getSource().equals( SearchForm.PD_SOURCE ))
        {        
            generatePurchasable( request, publicationPermission, searchForm, purchasablePermission );
        }
        
        Boolean orderExists = (Boolean) request.getSession().getAttribute(WebConstants.SessionKeys.PARM_EXISTING_ORDER);
        
        if (Boolean.TRUE.equals(orderExists)) 
        {
            purchaseId = (Long) request.getSession().getAttribute(WebConstants.SessionKeys.PARM_PURCHASE_ID);
            request.setAttribute( WebConstants.RequestKeys.PURCHASE_ID, purchaseId.toString() );
            
            destination = FWD_ORDER_SPECIAL;
            
        } else {
            destination = FWD_CART_SPECIAL_COI;
        }
        searchForm.setOrderExists( false );
        
        if (Boolean.TRUE.equals(orderExists)) 
        {
            //Long purchaseId = (Long) request.getSession().getAttribute(PARM_PURCHASE_ID);
            //request.setAttribute( WebConstants.PURCHASE_ID, purchaseId.toString() );
            
            OrderPurchase orderPurchase = null ;
            
            try {
    	    	orderPurchase = OrderPurchaseServices.getOrderPurchaseForConfirmationNumber(purchaseId);
    	    } catch (OrderPurchasesException opex) {
    	    	//throw new CheckoutException( "Unable to add item to order, couldn't find order: " + Long.parseLong(purchaseId.toString()));
    	    }
    	    
    	   //  if (orderPurchase==null) {
    	   // 	throw new CheckoutException( "null OrderPurchase returned for order " + purchaseId);
    	   // } 
    	    
    	    if (orderPurchase != null)
    	    {
    	    	if (orderPurchase.getOrderDataSource() == OrderDataSourceEnum.TF.getOrderDataSourceId().intValue()) 
    	    	{
    	    		PurchasablePermission specialPurchasablePermission = PurchasablePermissionFactory.createPurchasablePermission( publicationPermission );
    	    		if (orderPurchase.isAcademic() && !specialPurchasablePermission.isAcademic())
    	    		{
    	    			ActionMessages errors = new ActionMessages();
    	    			String acaMessage = "";
    	    			acaMessage = "<b><p>You can only add the following Permission Types:" +
    	    					"</p><p>1. Use in electronic course materials</p>" +
    	    					"<p>2. Use in Print Course materials</p></b>";
    	    	        errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.incorrectPermissionType", acaMessage));
    	    	        saveErrors(request, errors);
    	    			return mapping.findForward(FWD_TOU_DETAIL);
    	    		}
    	    		
    	    		if (!orderPurchase.isAcademic() && specialPurchasablePermission.isAcademic())
    	    		{
    	    			ActionMessages errors = new ActionMessages();
    	    			String message = "";
    	    			message = "<b><p>You can only add the following Permission Types:" +
    	    					"</p><p>1. Deliver via Inter Library Loan (ILL) or document delivery</p>" +
    	    					"<p>2. Photocopy for general business or academic use</p></b>";
    	    	        errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.incorrectPermissionType", message));
    	    	        saveErrors(request, errors);
    	    			return mapping.findForward(FWD_TOU_DETAIL);
    	    		}
    	    	
    	    	}
    	    }
        }
        
        if (Boolean.TRUE.equals(orderExists)) 
        {                 
            request.getSession().removeAttribute(WebConstants.SessionKeys.PARM_PURCHASE_ID);
            request.getSession().setAttribute(WebConstants.SessionKeys.PARM_EXISTING_ORDER, Boolean.FALSE);
        }
        return mapping.findForward( destination );
    }
    
    public ActionForward addSpecialFromScratch ( ActionMapping       mapping
                      , ActionForm          form
                      , HttpServletRequest  request
                      , HttpServletResponse response)
    {
        Boolean orderExists = (Boolean) request.getSession().getAttribute(WebConstants.SessionKeys.PARM_EXISTING_ORDER);
        String destination;
        
        //reset this to make sure the Special Order page behaves 
        request.getSession().setAttribute("PUBLICATION", null);
        
        if (Boolean.TRUE.equals(orderExists))
        {
            Long purchaseId = (Long) request.getSession().getAttribute(WebConstants.SessionKeys.PARM_PURCHASE_ID);
            request.setAttribute( WebConstants.RequestKeys.PURCHASE_ID, purchaseId.toString() );
            
            //destination = FWD_ORDER_SCRATCH;
            destination = FWD_ORDER_SCRATCH_COI;
            
            request.getSession().removeAttribute(WebConstants.SessionKeys.PARM_PURCHASE_ID);
            request.getSession().setAttribute(WebConstants.SessionKeys.PARM_EXISTING_ORDER, Boolean.FALSE);
          //*****************Fix for NRL-474 *******************************
            request.getSession().setAttribute("RLINK_SP_EXIST", Boolean.TRUE);
          //*****************END Fix for NRL-474 *******************************
        }
        else
        {
            //destination = FWD_CART_SCRATCH;
        	destination = FWD_CART_SCRATCH_COI;
        }
        return mapping.findForward( destination );
    }
    
    public void setRightInstinUserContext( 
             ActionForm          form
            
            )
    {
    	
    	
    	SearchForm searchForm = castForm( SearchForm.class, form );
    	searchForm.setSelectedTou(searchForm.getSelectedRRTou());
    	UserContextService.getSearchState().setSelectedTou(searchForm.getSelectedRRTou());
    	UserContextService.getSearchState().setSelectedTouId(searchForm.getSelectedRightInst());
    	if (!StringUtils.isNumeric(searchForm.getSelectedRRTouId())) {
    		String msg = "RRTou=" + searchForm.getSelectedRRTou() + "; RRTouId=" + searchForm.getSelectedRRTouId();
    		if ( searchForm.getSelectedItem() != null )
    		{
    			Publication pub = searchForm.getSelectedItem();
    			msg += "; WrkInst=" + pub.getWrkInst() + "; TFWrkInst=" + pub.getTFWrkInst() + 
    					"; MainIDNo=" + pub.getMainIDNo() + "; MainTitle=" + pub.getMainTitle();
    		}
    		throw new IllegalArgumentException("selectedRRTouId must be a number: " + msg);
    	}
    	UserContextService.getSearchState().setSelectedRrTouId(Long.parseLong(searchForm.getSelectedRRTouId()));
    	UserContextService.getSearchState().setSelectedCategoryId(searchForm.getSelectedCategoryId());
    	
    	//added for pub to pub
    	UserContextService.getSearchState().setSelectedRlPermissionType(searchForm.getSelectedRlPermissionType());
    	UserContextService.getSearchState().setSelectedOfferChannel(searchForm.getSelectedOfferChannel());
    	UserContextService.getSearchState().setSelectedRlPubCode(searchForm.getSelectedRlPubCode());
    	
    }
    
    /**
     * Not used
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @throws IOException
     */
    public void setRightInst( ActionMapping       mapping
            , ActionForm          form
            , HttpServletRequest  request
            , HttpServletResponse response) throws IOException
    {
    	String rightId = request.getParameter("rightInst");
    	String rightholderInst = request.getParameter("rightsholderInst");
    	String rrTou = request.getParameter("rrTou");
    	String rrTouId = request.getParameter("rrTouId");
    	String categoryId = request.getParameter("categoryId");
    	
    	UserContextService.getSearchState().setSelectedTou(rrTou);
    	UserContextService.getSearchState().setSelectedTouId(Long.parseLong(rightId));
    	UserContextService.getSearchState().setSelectedRrTouId(Long.parseLong(rrTouId));
    	UserContextService.getSearchState().setSelectedCategoryId(Long.parseLong(categoryId));
    	

    	SearchForm searchForm = castForm( SearchForm.class, form );
    	searchForm.setSelectedRightInst(Long.parseLong(rightId));
    	searchForm.setSelectedRightHolderInst(Long.parseLong(rightholderInst));
    	searchForm.setSelectedTou(rrTou);
    	
    }
    
    
    public void getIfBiactive(ActionMapping       mapping
            , ActionForm          form
            , HttpServletRequest  request
            , HttpServletResponse response) throws IOException 
    {   
        Boolean biactiveVal = checkIfBiactive(mapping,form, request, response);
        StringBuffer xmlResponseBuffer = new StringBuffer();
        xmlResponseBuffer.append("<ajaxResponse>");
        xmlResponseBuffer.append("<biactive>" + biactiveVal.toString() + "</biactive>");
        xmlResponseBuffer.append( "</ajaxResponse>" );
        
        response.setContentType("text/xml");
        response.setHeader( WebConstants.IS_AJAX_RESPONSE, "true" );
         
        PrintWriter out = response.getWriter();
	
        out.write(xmlResponseBuffer.toString());
        out.close();
    }
    
    public boolean checkIfBiactive(ActionMapping       mapping
            , ActionForm          form
            , HttpServletRequest  request
            , HttpServletResponse response) {
    	SearchForm searchForm = castForm( SearchForm.class, form );
        long item = new Long(searchForm.getSelectedWrkInst()).longValue();
        //check if the form has the publication first before the search
        WorkRightsAdapter pub = (WorkRightsAdapter)searchForm.getPublication(item);
       
        if (pub == null){
       	 /*
       	  * this only happens when we didn't find a pub in the users
       	  * session - such as when it has timed out.
       	  * We don't want TF rights to get loaded because we'll let
       	  * the RightsResolver service do that (see further down)
       	  */
           pub = (WorkRightsAdapter)_WebSearchService.getSingleItemByKey( item, false );
        }    
        
        searchForm.setSelectedItem( pub );
        Boolean biactiveVal = false;
        //pub.setRRRights();
        //get whole new set of rights and create permissions options display objects
        pub.setRRRights();// get the latest
        biactiveVal=  PermSummaryUtil.checkIfBiactive(pub.getRrTagList(), pub.getPermCategories());
        //processDetail(mapping,searchForm,request,pub);  
        
        
        
       /* for (PermissionCategoryDisplay permCatDisplay : searchForm.getPermCatDisplay()) {
        	//if we have both tf and rl rights then this is biactive
        	//if the Rl rights are all encompassing the TF rights will be ignored later in processing
        	if (permCatDisplay.hasTfRights() && permCatDisplay.hasRlRights() &&
        	!permCatDisplay.hasEncompassingRlRights()) {
        		biactiveVal = true;
        	}

        }*/
        
       /* if (!biactiveVal) {
        	biactiveVal = pub.getHasBiactiveTfRights();
        }
        */
        if (!biactiveVal) {
        	//clear chosen publication year
        	searchForm.setSelectedPubYear("");
        }
        return biactiveVal;
    }
       
    //*******************************************************************
    //  The following two methods increment or decrement
    //  our search results.  The action delivers us back to
    //  the same page we were on.
    
    public ActionForward prev( ActionMapping       mapping
                             , ActionForm          form
                             , HttpServletRequest  request
                             , HttpServletResponse response )
    {
        SearchForm searchForm = castForm( SearchForm.class, form );
        searchForm.synchronizeTypesOfUse();
        //searchForm.setResults( _service.getNextPage() );
         searchForm.setResults( _WebSearchService.getNextPage() );
        searchForm.decrPageNo();
        
        putForms( request, searchForm, null );
        
        return mapping.findForward( searchForm.getLastPage() );
    }
    
    public ActionForward next( ActionMapping       mapping
                             , ActionForm          form
                             , HttpServletRequest  request
                             , HttpServletResponse response )
    {
        SearchForm searchForm = castForm( SearchForm.class, form );
        searchForm.synchronizeTypesOfUse();
       // searchForm.setResults( _service.getNextPage() );
        searchForm.setResults( _WebSearchService.getNextPage() );
        searchForm.incrPageNo();
        
        putForms( request, searchForm, null );
        
        return mapping.findForward( searchForm.getLastPage() );
    }
    
    //******************************************************************
    //  The following two methods are for the "more" and "less" links
    //  on the search results page.
    
    public ActionForward more( ActionMapping       mapping
                             , ActionForm          form
                             , HttpServletRequest  request
                             , HttpServletResponse response )
    {
        SearchForm searchForm = castForm( SearchForm.class, form );
        searchForm.synchronizeTypesOfUse();
        //searchForm.incrPageRange();
        searchForm.incrPageNo();
        
//        if (_count > 0)
        if (Integer.parseInt(searchForm.getCount()) > 0)
        {
            getResults(searchForm);
        }
        putForms( request, searchForm, null );
        
        return mapping.findForward( searchForm.getLastPage() );
    }
    
    public ActionForward less( ActionMapping       mapping
                             , ActionForm          form
                             , HttpServletRequest  request
                             , HttpServletResponse response )
    {
        SearchForm searchForm = castForm( SearchForm.class, form );
        searchForm.synchronizeTypesOfUse();
        //searchForm.decrPageRange();
        searchForm.decrPageNo();
//        if (_count > 0)
        if (Integer.parseInt(searchForm.getCount()) > 0)
        {
            getResults(searchForm);
        }
        putForms( request, searchForm, null );
        
        return mapping.findForward( searchForm.getLastPage() );
    }
    
    //*******************************************************************
    //  The following two methods have to do with the sort and page
    //  size.  The action delivers us back to the same page we were on.
    
    public ActionForward sort( ActionMapping       mapping
                             , ActionForm          form
                             , HttpServletRequest  request
                             , HttpServletResponse response )
    {
        SearchForm searchForm = castForm( SearchForm.class, form );
        String destination;
        
        int  count = 0;
        
        if (searchForm != null)
        {
        	searchForm.synchronizeTypesOfUse();
        	if (searchForm.getLastSearch() == "simple") {
        	    searchForm.setSearchType(SIMPLE_SEARCH);
        		count = doSearch(searchForm);
        	}
        	else {
        	    searchForm.setSearchType(COMPLEX_SEARCH);
        		count = doComplexSearch(searchForm);
        	}
            if (count > 0)
            {
                    getResults(searchForm);
                    destination = searchForm.getLastPage();
                    updateFormValues(request, searchForm, count);
                    searchForm.setLastPage( destination );
            }
            else
            {
                    destination = FWD_NO_RESULTS;
            }
        }
        else
        {
            //  Log this... and redirect to the simple search page, for now.
    
            destination = FWD_FAILURE;
            //addError( "Sort failed.  Reason:  Search form was null." );
        }
        putForms(request, searchForm, null);
        return mapping.findForward( destination );
    }
    
    //*******************************************************************
    //  How many items to show per page.
    
    public ActionForward perPage( ActionMapping       mapping
                                , ActionForm          form
                                , HttpServletRequest  request
                                , HttpServletResponse response )
    {
        SearchForm searchForm = castForm( SearchForm.class, form );
        searchForm.synchronizeTypesOfUse();
        
        int numResults = Integer.parseInt(searchForm.getCount());
        int pageSize = Integer.parseInt(searchForm.getPageSize());
        int originalPageNo = Integer.parseInt(searchForm.getPageNo());
        
        //searchForm.setPageNo( String.valueOf(newPageNo) );

        if (!searchForm.getCount().equals("0"))
        {
        	searchForm.setPageNo("1");
            getResults(searchForm); 
            searchForm.clearPageRange();
            searchForm.calculatePageRange();
        }
        putForms( request, searchForm, null );
        
        return mapping.findForward( searchForm.getLastPage() );
    }
    
    //*******************************************************************
    //  Similar to prev and next, topage advances our search
    //  results to another page, but to a specified one, not
    //  a sequential one.
    
    public ActionForward topage( ActionMapping       mapping
                                                       , ActionForm          form
                                                       , HttpServletRequest  request
                                                       , HttpServletResponse response )
    {
        SearchForm searchForm = castForm( SearchForm.class, form );
        searchForm.synchronizeTypesOfUse();

        //  Grab the specified page number, if nothing
        //  is specified, default to 1.
        
        analyzeParameters( request );
        
        //  Get our results, and move on...
        
        //searchForm.setPageNo( _pno );
        searchForm.setPageNo((String) request.getAttribute("_pno"));

        if (!searchForm.getCount().equals("0"))
        {
            getResults(searchForm); 
        }
        putForms( request, searchForm, null);
        
        return mapping.findForward( searchForm.getLastPage() );
    }
        
    private void generatePurchasable( HttpServletRequest request
                                    , PublicationPermission perm
                                    , SearchForm form
                                    , PurchasablePermission pp ) 
    {
        String chapter = null;
        String fpage = null;
        String lpage = null;
        String pages = null;
        String copies = null;
        
        if (form.getSource().equals( SearchForm.PD_SOURCE ))
        {
            //      Pull out our parameters for the next step...
                            
            chapter = (String) request.getSession().getAttribute( WebConstants.SessionKeys.CHAPTER_ARTICLE );
            fpage = (String) request.getSession().getAttribute( WebConstants.SessionKeys.FIRST_PAGE );
            lpage = (String) request.getSession().getAttribute( WebConstants.SessionKeys.LAST_PAGE );
            pages = (String) request.getSession().getAttribute( WebConstants.SessionKeys.PAGES );
            copies = (String) request.getSession().getAttribute( WebConstants.SessionKeys.COPIES );
        }
        if (form.getSource().equals( SearchForm.OPENURL_SOURCE ))
        {
            //      Pull out our parameters for the next step...
                            
            chapter = (String) request.getSession().getAttribute( WebConstants.SessionKeys.CHAPTER_ARTICLE );
            fpage = (String) request.getSession().getAttribute( WebConstants.SessionKeys.FIRST_PAGE );
            lpage = (String) request.getSession().getAttribute( WebConstants.SessionKeys.LAST_PAGE );
            pages = (String) request.getSession().getAttribute( WebConstants.SessionKeys.PAGES );
            copies = (String) request.getSession().getAttribute( WebConstants.SessionKeys.COPIES );
        }                 
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
                        
        //      Alright, this part is hairy.  I need to create a purchasablePermission,
        //      but then I want to add in our extra bits passed along via the
        //      aforementioned parameters, then stuff it into the request.
        
         
        if (pp == null) {
            pp = PurchasablePermissionFactory.createPurchasablePermission( perm );
        }
        if ( pp != null ) {
	        if (pp.isAPS() || pp.isECCS())
	        {
	            //  Set values and stuff back into pr and pp.
	                                
	            pp.setChapterArticle( chapter );
	            pp.setNumberOfStudents( numcopies );
	            pp.setNumberOfPages( numpages );
	            pp.setPageRange( pagerange );
	        }
	        if (pp.isDigital())
	        {
	            //  Set values and stuff back into pr and pp.
	                                
	            pp.setChapterArticle( chapter );
	            pp.setNumberOfRecipients( numcopies );
	        }
	        if (pp.isRepublication())
	        {
	            //  Set values and stuff back into pr and pp.
	                                
	            pp.setChapterArticle( chapter );
	            pp.setCirculationDistribution( numcopies ); // nice consistency
	        }
	        if (pp.isPhotocopy())
	        {
	            //  Set values and stuff back into pr and pp.
	                                
	            pp.setChapterArticle( chapter );
	            pp.setNumberOfCopies( numcopies );
	            pp.setNumberOfPages( numpages );
	        }
	        request.setAttribute( WebConstants.RequestKeys.TRANSACTION_ITEM, pp ); 
	        request.setAttribute( WebConstants.RequestKeys.PERMISSIONS_DIRECT_TRANSACTION_ITEM, Boolean.TRUE ); 
	        // BOBD - Added this flag so that TOUs would be populated from user's 
	        //        choice see BasicTransactionAction where it reads the flag and
	        //        treats the request as a search request which it is except that
	        //        for permissions direct the transaction item is created to hold
	        //        the users parameter values provided on the PD URL.
	        //        11/10/2010
        }
    }
    
    public ActionForward initTitleSearchParms(ActionMapping       mapping
            , ActionForm          form
            , HttpServletRequest  request
            , HttpServletResponse response )
    {
//save the selected permission type and id of the selected work
    	setRightInstinUserContext(form);
    	 analyzeParameters( request );
    	 SearchState searchState = UserContextService.getSearchState();       
    	  UserContextService.setArticleSearchState(null);
    	 SearchForm searchForm = (SearchForm) request.getSession().getAttribute( WebConstants.SessionKeys.SEARCH_FORM );
         int selectedCategoryIndex = ((Integer) request.getAttribute( IDX )).intValue();
                         
         if (selectedCategoryIndex == 0)
         {
         	//return mapping.findForward(FWD_TOU_DETAIL);
         	ActionMessages errors = new ActionMessages();
             errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.searchAgain", "Incomplete Order data."));
             saveErrors(request, errors);
         	return mapping.findForward(FWD_SEARCH_SIMPLE);
         }
         /*
          * get the index number of the category the user clicked on
          */
         if ( request.getAttribute( IDX ) != null) {
        	  searchForm.setSelectedPermCatDisplayIndex(Integer.valueOf(selectedCategoryIndex));
         }
         long item = new Long((String) request.getAttribute( ITEM )).longValue();
         request.getSession().setAttribute(ITEM, item);
         // set the search state this will set selected publication
         RightsLnkQPriceUtil.getSelectedPublication(item);
         searchState.setSelectedPermissionType( searchForm.getSelectedPermCatDisplay().getCategoryDescription());
         return mapping.findForward( "rlQuickPrice" );
}
    
    public ActionForward initTitleSearchParmsSpecial(ActionMapping       mapping
            , ActionForm          form
            , HttpServletRequest  request
            , HttpServletResponse response )
    {
//save the selected permission type and id of the selected work
    	setRightInstinUserContext(form);
    	 analyzeParameters( request );
    	 SearchState searchState = UserContextService.getSearchState();       
    	  UserContextService.setArticleSearchState(null);
    	 //SearchForm searchForm = (SearchForm) request.getSession().getAttribute( WebConstants.SessionKeys.SEARCH_FORM );
    	 SearchForm searchForm = castForm( SearchForm.class, form );
         int selectedCategoryIndex = ((Integer) request.getAttribute( IDX )).intValue();
                         
         //long item = new Long((String) request.getAttribute( ITEM )).longValue();
         long item = Long.parseLong(searchForm.getSelectedWrkInst());
         request.getSession().setAttribute(ITEM, item);
         // set the search state this will set selected publication
         RightsLnkQPriceUtil.getSelectedPublication(item);
         //searchState.setSelectedPermissionType( searchForm.getSelectedPermCatDisplay().getCategoryDescription());
         searchState.setSelectedPermissionType( "Republish or display content");
         request.getSession().setAttribute(WebConstants.SessionKeys.SEARCH_FORM, searchForm);
         return mapping.findForward( "rlQuickPrice" );
}
}
