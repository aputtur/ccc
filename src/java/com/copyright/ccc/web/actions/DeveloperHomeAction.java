package com.copyright.ccc.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.base.config.AppServerConfiguration;
import com.copyright.ccc.config.CC2Configuration;
import com.copyright.ccc.web.CCAction;

public class DeveloperHomeAction extends CCAction
{
    public ActionForward defaultOperation( ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response )
    {
        return mapping.findForward( SHOW_MAIN );
    }
    
    /**
     * Turns on the simulation of Rightslink being down without
     * requiring a re-load of the properties file.
     * This was created to test scenarios where RL goes down
     * while a user is moving through the steps of a purchase. 
     * For example...
     * <ol>
     * <li>RL is up
     * <li>User requests permission summary
     * <li>RL goes down
     * <li>User selects a type of use that requires RL to be up
     * <li>We should display appropriate messaging on subsequent page
     * </ol>
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward startSimulatingRLDown ( ActionMapping       mapping
    		, ActionForm          form
    		, HttpServletRequest  request
    		, HttpServletResponse response
    )
    {
    	if (isEnvDev()) {
    	    CC2Configuration.getInstance().setForciblySimulateRLDown(true);
    	}
        return mapping.findForward( SHOW_MAIN );    			
    }

    /**
     * Turns off the forcible simulation of Rightslink being down
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */    
    public ActionForward stopSimulatingRLDown ( ActionMapping       mapping
    		, ActionForm          form
    		, HttpServletRequest  request
    		, HttpServletResponse response
    )
    {
    	if (isEnvDev()) {
    		CC2Configuration.getInstance().setForciblySimulateRLDown(false);
    	}
        return mapping.findForward( SHOW_MAIN );    			
    }    
    
    private boolean isEnvDev() {
    	return AppServerConfiguration.isLOCAL() || AppServerConfiguration.isDEV();
    }
}
