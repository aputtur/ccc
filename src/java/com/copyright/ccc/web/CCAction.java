package com.copyright.ccc.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.copyright.base.config.AppServerConfiguration;
import com.copyright.ccc.CCRuntimeException;
import com.copyright.ccc.web.forms.SearchForm;
import com.copyright.ccc.web.util.WebUtils;
import com.copyright.workbench.security.NotAuthorizedRuntimeException;

public class CCAction extends DispatchAction
{
    /**
     * Name of unauthorized handling global-forward.
     */
    private static final String NOT_AUTHORIZED = "notAuthorized";
    private static final String SESSION_DATA_NOT_AVAILABLE = "sessionDataNotAvailable";
    private static final String ITEM="item";
    
    protected Logger _logger = Logger.getLogger( this.getClass() );
    
    public static final String SHOW_MAIN = "showMain";
    /**
     * Override of <code>DispatchAction.execute()</code> that invokes the 
     * method <code>defaultOperation()</code> when no other method is 
     * specified.  This provides analogous functionality to overriding 
     * <code>execute()</code>in "standard" Struts coding.
     * 
     * Declared final because overriding this method would cause
     * application-specific code in <tt>dispatchMethod()</tt> to be bypassed.
     */
    @Override
    public final ActionForward execute( ActionMapping mapping,
                                  ActionForm form,
                                  HttpServletRequest request,
                                  HttpServletResponse response )
        throws Exception
    {
        // TODO: If we upgrade Struts, revisit the copy of code from 
        // DispatchAction to see if the implementation of execute() has
        // changed. [dstine & ccollier 7/12/06]

        ///////////////////////////////////////////////////////
        ///// Modified version of DispatchAction.execute() /////
        ///////////////////////////////////////////////////////
        
        String name = getMethodName( mapping, request );
        
        try
        {
        	if (_logger.isDebugEnabled() ) {
        		_logger.debug("processing request for " + request.getRequestURI());
        	}
            return dispatchMethod( mapping, form, request, response, name );
        }
        catch ( NotAuthorizedRuntimeException nare )
        {
            // Catch NotAuthorizedRuntimeException here in CCAction because 
            // a) Rightsphere did this, and 
            // b) there are drawbacks to catching it in other places.
            // Cannot catch in UserSessionFilter, because Struts ends up 
            // throwing a ServletException.  Don't want to catch via 
            // struts-config <global-exceptions> because it enforces some 
            // visual artifacts (at least out of the box).
            // [dstine 3/19/07]
            String msg = nare.getMessage();
            
            if ( AppServerConfiguration.isPRD() )
            {
                if ( _logger.isInfoEnabled() )
                {
                    _logger.info( msg );
                }
            }
            else
            {
                if ( _logger.isDebugEnabled() )
                {
                    _logger.debug( msg );
                }
            }
            
            request.getSession().setAttribute( WebConstants.SessionKeys.NOT_AUTHORIZED_MSG, msg );
            
            return mapping.findForward( NOT_AUTHORIZED );
        }
    }
    
    /**
     * Override of <code>DispatchAction.dispatchMethod()</code> that catches 
     * any uncaught <code>Exception</code>, logs the stack trace at level 
     * ERROR, sets the <code>Exception</code> in the <code>HttpSession</code>, 
     * and forwards control to the application error page.
     */
    @Override
    protected ActionForward dispatchMethod( ActionMapping mapping,
                                            ActionForm form,
                                            HttpServletRequest request,
                                            HttpServletResponse response,
                                            String name ) throws Exception 
    {
        return super.dispatchMethod( mapping, form, request, response, name );
    }

    /**
     * Determines the value of the Struts "parameter", i.e. the method to which 
     * control should be dispatched.
     */
    private String getMethodName( ActionMapping mapping, HttpServletRequest request )
    {
        // If no parameter or operation is specified, call method named 
        // "defaultOperation".
        String name = "defaultOperation";
        
        // Does the definition of the action specify the parameter attribute?
        // The out-of-the-box DispatchAction will bomb out there is none, but 
        // we will run the default operation instead.
        String parameter = mapping.getParameter();
        if ( parameter != null ) 
        {
            // Did the request specify a value for the parameter named in 
            // the action definition?
            String parameterValue = request.getParameter( parameter );
            if ( parameterValue != null )
            {
                name = parameterValue;
            }
        }
        
        return name;
    }

    /**
     * @see WebUtils.castForm( Class<T> desired, ActionForm form )
     */
    protected <T extends ActionForm> T castForm( Class<T> desired, ActionForm form )
    {
    	return WebUtils.castForm( desired, form );
    }
    
    /*
     * Given a wrkInst, returns the ActionForward needed to send the user back
     * to the permission summary page. 
     */
    public static ActionForward sessionDataNotFound(ActionMapping mapping, HttpServletRequest request, String wrkInst) {
		request.setAttribute(ITEM, wrkInst );
    	return mapping.findForward(SESSION_DATA_NOT_AVAILABLE);
    }    
    
    public static Integer getSelectedPubYear(HttpServletRequest request) {
		SearchForm searchForm = (SearchForm) request.getSession().getAttribute(WebConstants.SessionKeys.SEARCH_FORM);
		Integer selectedPubYear=null;
		if (StringUtils.isNumeric(searchForm.getSelectedPubYear()) 
				&& StringUtils.isNotEmpty(searchForm.getSelectedPubYear())) {
			try {
				selectedPubYear=Integer.valueOf(searchForm.getSelectedPubYear());
			} catch (NumberFormatException e) {
				throw new CCRuntimeException("invalid pub year detected, is validation working properly? ",e);
			}
		}
		return selectedPubYear;
    }
    
    
    public static String getSelectedOfferChannel(HttpServletRequest request) {
		SearchForm searchForm = (SearchForm) request.getSession().getAttribute(WebConstants.SessionKeys.SEARCH_FORM);
		String selectedOfferChannel = searchForm.getSelectedOfferChannel();
		if (selectedOfferChannel != null)
		{
			return selectedOfferChannel;
		}
		else
		{
			return "";
		}
    }
    
    public static String getSelectedRlPermissionType(HttpServletRequest request) {
		SearchForm searchForm = (SearchForm) request.getSession().getAttribute(WebConstants.SessionKeys.SEARCH_FORM);
		String selectedRlPermissionType = searchForm.getSelectedRlPermissionType();
		
		if (selectedRlPermissionType != null)
		{
			return selectedRlPermissionType;
		}
		else
		{
			return "";
		}
    }
    
    public static String getSelectedRlPubCode(HttpServletRequest request) {
		SearchForm searchForm = (SearchForm) request.getSession().getAttribute(WebConstants.SessionKeys.SEARCH_FORM);
		String selectedRlPubCode = searchForm.getSelectedRlPubCode();
		
		if (selectedRlPubCode != null)
		{
			return selectedRlPubCode;
		}
		else
		{
			return "";
		}
    }
}
