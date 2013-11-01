package com.copyright.ccc.web.actions;


import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.business.data.Rightsholder;
import com.copyright.ccc.business.services.search.RightsholderServices;
import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.RightsholderContactInfoForm;

public class RightsholderContactInfoAction extends CCAction
{
    private static final String FORWARD = "contactInfo";
    
    public ActionForward defaultOperation( ActionMapping       mapping
                               , ActionForm          form
                               , HttpServletRequest  request
                               , HttpServletResponse response )
    {
        RightsholderContactInfoForm myForm = castForm( RightsholderContactInfoForm.class, form );
        
        if (myForm.getRhId() > 0) {
            return getContactInfoByPtyInst(mapping,form,request,response);
        } else {
            return getContactInfoByRgtInst(mapping,form,request,response);
        }
    }
    
    private ActionForward getContactInfoByRgtInst( ActionMapping       mapping
                               , ActionForm          form
			       , HttpServletRequest  request
			       , HttpServletResponse response )
    {
        RightsholderContactInfoForm myForm = castForm( RightsholderContactInfoForm.class, form );

        Rightsholder rh = RightsholderServices.getRightsholderByRgtInst(myForm.getRgtInst(),new Date(myForm.getDateOfUse()));

        myForm.setRightsholder(rh);
        
        return mapping.findForward( FORWARD );
    }
  
  
    private ActionForward getContactInfoByPtyInst( ActionMapping       mapping
                               , ActionForm          form
                               , HttpServletRequest  request
                               , HttpServletResponse response )
    {
         RightsholderContactInfoForm myForm = castForm( RightsholderContactInfoForm.class, form );
        
         Rightsholder rh = RightsholderServices.getRightsholderByPtyInst(myForm.getRhId());
        
         myForm.setRightsholder(rh);
         request.getSession().setAttribute(WebConstants.SessionKeys.RIGHTSHOLDER_CONTACT_INFO_FORM, myForm);
         
         return mapping.findForward( FORWARD );
    }
        
}
