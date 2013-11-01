package com.copyright.ccc.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.CCRuntimeException;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.forms.LoginForm;
import com.copyright.data.order.Cart;

public class AutoLoginAction extends CCAction
{
    public ActionForward defaultOperation(ActionMapping mapping,
                                            ActionForm form,
                                            HttpServletRequest request,
                                            HttpServletResponse response )
    {
        LoginForm loginForm = castForm( LoginForm.class, form );

        byte phase = loginForm.getAutoLoginPhase();

        if ( phase == LoginForm.AL_OFF && loginForm.getAutoLoginForward() != null )
        {
            loginForm.setAutoLoginPhase( phase = LoginForm.AL_PHASE_ONE );
        }

        if ( phase == LoginForm.AL_PHASE_ONE )
        {
            loginForm.setAutoLoginPhase( phase = LoginForm.AL_PHASE_TWO );
            

            if ( UserContextService.isUserAuthenticated() )
            {
                //UserContextService.unAuthenticateUserContext();
                //request.getSession().
            }
            
            Cart cart = UserContextService.getCart();
            
            if( cart != null )
            {
              loginForm.setCartID( cart.getID() );
            }
            
            return mapping.findForward( "phaseTwo" );
        }
        else if ( phase == LoginForm.AL_PHASE_TWO )
        {
            if ( request.getSession().isNew() )
                UserContextService.setSessionInitiatedWithAutoLogin( true );
            
            String forwardPath = loginForm.getAutoLoginForward();
            
            if ( forwardPath == null )
                throw new CCRuntimeException( "null autoLoginForward in autoLogin form" );
            
            ActionForward af = new ActionForward( forwardPath, true );

            loginForm.setAutoLoginPhase( LoginForm.AL_OFF );
            
            String sessionInitiator = loginForm.getSessionInitiator();
            
            UserContextService.getActiveAppUser().setLastCartID( loginForm.getCartID() );
            
            loginForm.setCartID( 0L );
            
            if ( sessionInitiator != null )
                UserContextService.setSessionInitiator( sessionInitiator );
            
            return af;
        }
        else
        {
            return null;
        }
    }
}
