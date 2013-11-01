package com.copyright.ccc.web.actions;

/*
 *  title:          ShowGPOAgreementAction
 *  author:         MSJ
 *  date:           10/2005
 *
 *  description:    I think this action's name speaks
 *                  for itself.
 */

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.RegisterGPOUserForm;

/**
 * Display the GPO Online User Agreement.
 *
 * @author Mike Jessop
 * @version 10/25/2005
 */
public class ShowGPOAgreementAction extends CCAction
{
    private static final Logger LOGGER = Logger.getLogger( ShowGPOAgreementAction.class );
    public ActionForward defaultOperation(
        ActionMapping       mapping,
        ActionForm          form,
        HttpServletRequest  request,
        HttpServletResponse response
    )
        throws IOException, ServletException
    {
        
        
        RegisterGPOUserForm frm = castForm( RegisterGPOUserForm.class, form );
        String status = frm.getStatus();
        
        LOGGER.info("Status: " + status);
        
		//  Frankly this action does jack squat.  I just needed
        //  a way to collect the GPO Registration form information
        //  so I would not lose it and the registration action
        //  would fail.
        
         
         //request.getSession().setAttribute( "RegisterGPOUserForm", form );
         request.getSession().setAttribute( WebConstants.SessionKeys.REGISTER_GPO_USER_FORM, frm );
        
               
        //  Now display the page to reset our password.
        
        if ((status != null) && !status.equals("CREATE"))
        {
            return( mapping.findForward( "skip" ) );
        }
        else
        {
            return( mapping.findForward( "success" ) );
        }
    }
}