package com.copyright.ccc.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.business.data.PublicationPermission;
import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.SampleTransactionStartPageForm;
import com.copyright.ccc.web.mock.MockPublicationPermissionServices;
import com.copyright.ccc.web.util.WebUtils;

//TODO: gcuevas 11/3/06: remove when integration with search is ready
public class SampleTransactionStartPageAction extends CCAction
{
    private static final String BASIC_ADD_TO_CART = "basicAddToCart";
    private static final String BASIC_ADD_TO_ORDER = "basicAddToOrder";
    private static final String SPECIAL_ADD_TO_CART = "specialAddToCart";
    private static final String SPECIAL_ADD_TO_ORDER = "specialAddToOrder";
    private static final String SPECIAL_SCRATCH_ADD_TO_CART = "specialScratchAddToCart";
    private static final String SPECIAL_SCRATCH_ADD_TO_ORDER = "specialScratchAddToOrder";
  
    public ActionForward defaultOperation( ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response )
    {
        return mapping.findForward( SHOW_MAIN );
    }
    
    public ActionForward createRegularOrder( ActionMapping mapping,
                                ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response )
    {
        SampleTransactionStartPageForm myForm = castForm( SampleTransactionStartPageForm.class, form );
        
        PublicationPermission publicationPermission = 
            MockPublicationPermissionServices.createPublicationPermission(myForm.getTypeOfUse(), myForm.getSampleValue(), false);
        request.setAttribute(WebConstants.RequestKeys.PUBLICATION_PERMISSION, publicationPermission);
       
        WebUtils.clearActionFormFromSession(request, mapping);
  
        if(myForm.getAction().equals("addToCart"))
            return mapping.findForward( BASIC_ADD_TO_CART );
        else 
            return mapping.findForward( BASIC_ADD_TO_ORDER );
    }
    
    public ActionForward createSpecialOrderWithTitle( ActionMapping mapping,
                                ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response )
    {
        SampleTransactionStartPageForm myForm = castForm( SampleTransactionStartPageForm.class, form );
      
        PublicationPermission publicationPermission = 
            MockPublicationPermissionServices.createPublicationPermission(myForm.getTypeOfUse(), myForm.getSampleValue(), true);
        request.setAttribute(WebConstants.RequestKeys.PUBLICATION_PERMISSION, publicationPermission);
     
        WebUtils.clearActionFormFromSession(request, mapping);
    
        if(myForm.getAction().equals("addToCart"))
            return mapping.findForward( SPECIAL_ADD_TO_CART );
        else
            return mapping.findForward( SPECIAL_ADD_TO_ORDER );
    }
    
    public ActionForward createSpecialOrderFromScratch( ActionMapping mapping,
                                ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response )
    {
        SampleTransactionStartPageForm myForm = castForm( SampleTransactionStartPageForm.class, form );
       
        PublicationPermission publicationPermission = 
            MockPublicationPermissionServices.createPublicationPermission(myForm.getTypeOfUse(), myForm.getSampleValue(), true);
        request.setAttribute(WebConstants.RequestKeys.PUBLICATION_PERMISSION, publicationPermission);
        
        WebUtils.clearActionFormFromSession(request, mapping);
        
        if(myForm.getAction().equals("addToCart"))
            return mapping.findForward( SPECIAL_SCRATCH_ADD_TO_CART );
        else
            return mapping.findForward( SPECIAL_SCRATCH_ADD_TO_ORDER );
    }
    
    
}
