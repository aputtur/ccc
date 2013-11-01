package com.copyright.ccc.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.copyright.ccc.web.util.WebUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.actions.TilesAction;

import com.copyright.ccc.web.forms.GoogleBookPreviewForm;

public class GoogleBookPreviewAction extends TilesAction
{
    public GoogleBookPreviewAction() { }
    
    /**
    * Struts action that displays a the special term text for a License 
    * @param mapping is the struts ActionMapping passed in by the struts controller
    * @param form is the struts ActionForm passed in by the struts controller
    * @param request is the HttpServletRequest object
    * @param response is the HttpServletResponse object
    * @return the ActionForward object to the "forward" action mapping.
    */
    @Override
     public ActionForward execute( ActionMapping mapping, ActionForm form, 
                  HttpServletRequest request, HttpServletResponse response ) 
     {
        //ActionForward forward = mapping.findForward(FORWARD);
        GoogleBookPreviewForm previewForm = WebUtils.castForm( GoogleBookPreviewForm.class, form );
        
           
        String txt = request.getParameter("isbn");
                
        previewForm.setIsbn(txt);
        //return forward("showBookPreview");
        return mapping.findForward("showBookPreview");
    }   
}
