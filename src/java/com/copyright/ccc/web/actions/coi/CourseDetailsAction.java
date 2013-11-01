package com.copyright.ccc.web.actions.coi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.forms.coi.CourseDetailsForm;

public abstract class CourseDetailsAction extends CCAction
{
    protected final String SUBMIT = "submit";
    
    protected abstract void populateForm(CourseDetailsForm courseDetailsFormCOI, HttpServletRequest request);
   
    public ActionForward defaultOperation(ActionMapping mapping,
                                            ActionForm form,
                                            HttpServletRequest request,
                                            HttpServletResponse response )
    {
        CourseDetailsForm courseDetailsFormCOI = castForm( CourseDetailsForm.class, form );
        String cpParm = courseDetailsFormCOI.getCp();
        String rpParm = courseDetailsFormCOI.getRp();
        String sfParm = courseDetailsFormCOI.getSf();
        boolean firstParm = false;
        
        String path = mapping.getPath();
        if (path != null && !"".equals(path)) {
            int idx = path.indexOf("?");
            if (idx >= 0) {
               firstParm = true;
            }
        }
        if (cpParm != null && !"".equals(cpParm)) {
            if (firstParm) {
               cpParm = "&cp=" + cpParm;
            } else {
               cpParm = "?cp=" + cpParm;
               firstParm = true;
            }
        } else { 
           cpParm = ""; 
        }
        
        if (rpParm != null && !"".equals(rpParm)) {
            if (firstParm) {
               rpParm = "&rp=" + rpParm;
            } else {
                rpParm = "?rp=" + rpParm;
                firstParm = true;
            }
        } else { 
           rpParm = ""; 
        } 
        
        if (sfParm != null && !"".equals(sfParm)) {
            if (firstParm) {
               sfParm = "&sf=" + sfParm;
            } else {
                sfParm = "?sf=" + sfParm;
                firstParm = true;
            }
        } else { 
           sfParm = ""; 
        } 
        
        path = path + cpParm + rpParm + sfParm;
         
        courseDetailsFormCOI.setFormPath( path );
        
        //TODO: gcuevas: throw some exception if cart is not academic
        
        populateForm( courseDetailsFormCOI, request );
        
        ActionForward forward = mapping.findForward( SHOW_MAIN );
        
        //return mapping.findForward( SHOW_MAIN );
        
        return forward;
    }
    
    public abstract ActionForward submit(ActionMapping mapping, ActionForm form, 
        HttpServletRequest request, HttpServletResponse response);
    
    public abstract ActionForward cancel(ActionMapping mapping, ActionForm form, 
        HttpServletRequest request, HttpServletResponse response);

}
