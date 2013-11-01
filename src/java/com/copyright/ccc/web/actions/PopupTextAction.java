package com.copyright.ccc.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.copyright.ccc.web.util.WebUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.actions.TilesAction;

import com.copyright.ccc.web.forms.PopupTextActionForm;

public class PopupTextAction extends TilesAction
{
    private static final String FORWARD = "continue";

    public PopupTextAction() { }
    
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
        ActionForward forward = mapping.findForward(FORWARD);
        PopupTextActionForm termsForm = WebUtils.castForm( PopupTextActionForm.class, form );
        
        String termText = "No Terms Available";        
        String txt = request.getParameter("txt");
        
        if (txt != null && !"".equals(txt)) {
           termText = txt;
        }
        
        //termText = "B & O is a > stereo system. <a href=\"javascript:hack()\">link</a>";
        termsForm.setText(termText);
        return forward;
    }   
}
