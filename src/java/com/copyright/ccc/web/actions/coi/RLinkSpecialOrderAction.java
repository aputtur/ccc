package com.copyright.ccc.web.actions.coi;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import com.copyright.base.CCCRuntimeException;
import com.copyright.ccc.business.data.CCSearchCriteria;
import com.copyright.ccc.business.data.Publication;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.search.WebServiceSearch;
import com.copyright.ccc.business.services.search.WorkRightsAdapter;
import com.copyright.ccc.config.CC2Configuration;
import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.SearchForm;
import com.copyright.ccc.web.forms.coi.RLinkSpecialOrderForm;
import com.copyright.ccc.web.util.PermissionCategoryDisplay;

import com.copyright.svc.rightsResolver.api.data.PermissionSummaryCategory;
import com.copyright.workbench.util.StringUtils2;

public class RLinkSpecialOrderAction extends CCAction
{
    protected static final WebServiceSearch _WebSearchService = new WebServiceSearch();
    
    protected static final String SPECIAL_ORDER = "specialOrder";
    protected static final String IDX = "_idx";
    protected static final String ITEM = "_item";
  //  private static final String FAILURE = "failure";
        
    public ActionForward defaultOperation(ActionMapping mapping,
                                            ActionForm form,
                                            HttpServletRequest request,
                                            HttpServletResponse response )
    {
        Publication pubSpecial = (Publication) request.getSession().getAttribute("PUBLICATION");
    	RLinkSpecialOrderForm rlinkSpecialOrderForm = castForm( RLinkSpecialOrderForm.class, form );
    	
    	resetForm(rlinkSpecialOrderForm);
    	
    	if (pubSpecial != null) {
    		rlinkSpecialOrderForm.setPublicationName(pubSpecial.getMainTitle());
    		rlinkSpecialOrderForm.setIssn(pubSpecial.getMainIDNo());
    		rlinkSpecialOrderForm.setPublisher(pubSpecial.getMainPublisher());
    		rlinkSpecialOrderForm.setAuthor((pubSpecial.getMainAuthor() != null && pubSpecial.getMainAuthor() != "") ? 
    				pubSpecial.getMainAuthor() :pubSpecial.getMainEditor());
    		rlinkSpecialOrderForm.setVolume(pubSpecial.getVolume());
    		rlinkSpecialOrderForm.setEdition(pubSpecial.getEdition());
    		rlinkSpecialOrderForm.setSpecialOrder(true);
    	}
    	else {
    		rlinkSpecialOrderForm.setSpecialOrder(false);
    	}
    	
    	//request.getSession().setAttribute("PUBLICATION", null);
        
        rlinkSpecialOrderForm.setFormPath( mapping.getPath() );
        rlinkSpecialOrderForm.setSpecialFormPath(mapping.getPath());
        rlinkSpecialOrderForm.setCategoryDescription("Republish or display content");
        rlinkSpecialOrderForm.setCatID("6");
        
      //*****************Fix for NRL-474 *******************************
        Boolean orderExists = (Boolean) request.getSession().getAttribute("RLINK_SP_EXIST");
                        
        if (Boolean.TRUE.equals(orderExists))
        {
        	request.getSession().setAttribute(WebConstants.SessionKeys.PARM_EXISTING_ORDER, Boolean.TRUE);
        	request.getSession().setAttribute("RLINK_SP_EXIST", Boolean.FALSE);
        }
        //********************** END FIX ******************************************
        
                
        //Use Test Repub as the Title for this and then populate all the values provided by the Customer
        String idno = CC2Configuration.getInstance().getRlinkSpecialOrderIdno();
        
        if (StringUtils2.isNullOrEmpty(idno)) {
        	idno = "111-1-256-29810-6";
        }
                      
        WorkRightsAdapter pub = null;
        pub =(WorkRightsAdapter)_WebSearchService.getSingleItem( idno );
                      
        rlinkSpecialOrderForm.setSelectedWrkInst(Long.toString(pub.getWrkInst()));
        
        pub.filterRights("");
        
        List<PermissionSummaryCategory> lstPermSumCategories = pub.getPermCategories();     
                
        List<PermissionCategoryDisplay> permCatDisplay = new ArrayList<PermissionCategoryDisplay>();
        
        int index=0;
        for(PermissionSummaryCategory permSumCat: lstPermSumCategories) {
        	if (permSumCat==null) {
        		throw new CCCRuntimeException("encountered a null PermissionSummaryCategory while building perm list for display");
        	}
        	if (permSumCat.getCategory()==null) {
        		throw new CCCRuntimeException("PermissionSummaryCategory.getCategory() returned null");        		
        	}
                                              
            PermissionCategoryDisplay permissionCategoryDisplay = new PermissionCategoryDisplay(pub,permSumCat,index++);
     		permCatDisplay.add(permissionCategoryDisplay);  
            
        
        }
         
        for(PermissionCategoryDisplay permSumCat: permCatDisplay) {
        	
        	if (permSumCat.getProduct().getAbbreviation().equalsIgnoreCase("RLS")){
        		rlinkSpecialOrderForm.setPermissionSummaryCat(permSumCat);
        		break;
        	}
        	
        	
        }
        
        request.getSession().setAttribute("rlinkSpecialOrderForm", rlinkSpecialOrderForm);
        
        return mapping.findForward(SHOW_MAIN);
    }
    
    public ActionForward goToSpecialOrder( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
    {    	 	
    	RLinkSpecialOrderForm rlinkSpecialOrderForm = (RLinkSpecialOrderForm) request.getSession().getAttribute("rlinkSpecialOrderForm");
    	
    	ActionMessages test = this.getErrors(request);
    	    		
    	if (StringUtils2.isNullOrEmpty(rlinkSpecialOrderForm.getSelectedWrkInst())) {
    		String idno = CC2Configuration.getInstance().getRlinkSpecialOrderIdno();
            
            if (idno == null || idno.isEmpty()) {
            	idno = "111-1-256-29810-6";
            }
            
            WorkRightsAdapter pub = null;
            pub =(WorkRightsAdapter)_WebSearchService.getSingleItem( idno );
            rlinkSpecialOrderForm.setSelectedWrkInst(Long.toString(pub.getWrkInst()));
    		
    	}
    	SearchForm searchForm = new SearchForm();
    	
    	//searchForm.getSelectedPermCatDisplay().setCategoryDescription(rlinkSpecialOrderForm.getCategoryDescription());
    	searchForm.setSelectedRRTouId(rlinkSpecialOrderForm.getTypeOfUseId());
    	searchForm.setSelectedRRTou(rlinkSpecialOrderForm.getTypeOfUseDescription());
    	//PermissionCategoryDisplay perm = rlinkSpecialOrderForm.getPermissionSummaryCat();
    	searchForm.setSelectedPermCatDisplay(rlinkSpecialOrderForm.getPermissionSummaryCat());
    	searchForm.setSelectedWrkInst(rlinkSpecialOrderForm.getSelectedWrkInst());
    	   
        UserContextService.getSearchState().setSelectedCategoryId(Long.parseLong("5"));
        UserContextService.getSearchState().setSelectedPermissionType(rlinkSpecialOrderForm.getPermissionType());
        UserContextService.getSearchState().setSelectedRlPermissionType(rlinkSpecialOrderForm.getSelectedRlPermissionType());
        UserContextService.getSearchState().setSelectedRrTouId(Long.parseLong(rlinkSpecialOrderForm.getTypeOfUseId()));
        UserContextService.getSearchState().setSelectedTou(rlinkSpecialOrderForm.getTypeOfUseDescription());
        UserContextService.getSearchState().setSelectedTouId(Long.parseLong(rlinkSpecialOrderForm.getTypeOfUseId())); 
        UserContextService.getSearchState().setSelectedOfferChannel(rlinkSpecialOrderForm.getSelectedOfferChannel());
        UserContextService.getSearchState().setSelectedRlPubCode(rlinkSpecialOrderForm.getSelectedRlPubCode());
        
        searchForm.setSelectedCategoryId(Long.parseLong("5"));
        searchForm.setSelectedRlPermissionType(rlinkSpecialOrderForm.getSelectedRlPermissionType());
        searchForm.setSelectedRRTouId(rlinkSpecialOrderForm.getTypeOfUseId());
        searchForm.setSelectedTou(rlinkSpecialOrderForm.getTypeOfUseDescription());
        searchForm.setSelectedOfferChannel(rlinkSpecialOrderForm.getSelectedOfferChannel());
        searchForm.setSelectedRlPubCode(rlinkSpecialOrderForm.getSelectedRlPubCode());
        searchForm.setSelectedRightInst(Long.parseLong(rlinkSpecialOrderForm.getRightId()));
        
                
        request.setAttribute( WebConstants.RequestKeys.SPECIAL_PERMISSION_TYPE, rlinkSpecialOrderForm.getPermissionType() );
        //request.setAttribute(IDX, "5");
        
        //request.setAttribute(ITEM, rlinkSpecialOrderForm.getSelectedWrkInst());
        
        request.getSession().setAttribute( WebConstants.SessionKeys.SEARCH_FORM, searchForm );
        
        rlinkSpecialOrderForm.setIsSpecialOrderFromScratch(true);
        
        request.getSession().setAttribute("rlinkSpecialOrderForm", rlinkSpecialOrderForm);
        
        //request.getSession().setAttribute("PUBLICATION", null);
        
        return mapping.findForward( SPECIAL_ORDER );
    }
    
    private void resetForm(RLinkSpecialOrderForm form)
    {
    	form.setPermissionSummaryCat(null);
    	form.setSelectedRlPubCode(null);
    	form.setSelectedWrkInst(null);
    	form.setTypeOfUseDescription(null);
    	form.setTypeOfUseId(null);
    	form.setPublicationName(null);
    	form.setPublisher(null);
    	form.setAuthor(null);
    	form.setVolume(null);
    	form.setEdition(null);
    	form.setPubDate(null);
    	form.setIssn(null);
    	    	
    }

}
