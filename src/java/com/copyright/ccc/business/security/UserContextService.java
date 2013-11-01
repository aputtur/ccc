package com.copyright.ccc.business.security;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.copyright.ccc.business.data.CCUser;
import com.copyright.ccc.business.data.DisplaySpec;
import com.copyright.ccc.business.data.OrderLicenses;
import com.copyright.ccc.business.data.OrderPurchases;
import com.copyright.ccc.business.data.OrderSearchResult;
import com.copyright.ccc.business.services.articlesearch.ArticleSearchState;
import com.copyright.ccc.business.services.cart.CartServices;
import com.copyright.ccc.business.services.rlnk.RlnkConstants.RlnkSessionConstants;
import com.copyright.ccc.business.services.search.SearchState;
import com.copyright.ccc.business.services.user.User;
import com.copyright.ccc.business.services.user.UserServices;
import com.copyright.ccc.web.WebConstants;
import com.copyright.data.order.Cart;
import com.copyright.workbench.security.HashFunctions;
import com.copyright.workbench.security.Privilege;
import com.copyright.workbench.security.PrivilegeCode;
import com.copyright.workbench.security.SecurityRuntimeException;
import com.copyright.workbench.security.UserContextHelperBase;


public final class UserContextService extends UserContextHelperBase
{
    static Logger _logger = Logger.getLogger( UserContextService.class );

    /**
     * Thread local storage for the HttpSession.  The UserContextHelper needs 
     * knowledge of the session in the rare cases that client code edits an
     * attribute of the UserContext (e.g. Customer name).  In such cases, the 
     * client code must update not only the UserContext on thread-local storage
     * via setCurrent(), but must also update the UserContext stored in the 
     * session.
     */
    private static ThreadLocal<HttpSession> _session = new ThreadLocal<HttpSession>();

    /**
     * Key used to store the UserContext in the session.
     */
    private static final String USER_CONTEXT_SESSION_KEY = 
        "__CC2_USER_CONTEXT__";

    private static final int NAV_HISTORY_MAX_SIZE = 10;



    /*x**************************************
     *  PUBLIC METHODS (API)
     * **************************************/

    /**
     * Retrieves the current user's <code>UserContext</code> object from the 
     * thread of execution.  This method is guaranteed to either return a 
     * non-null <code>UserContext</code> object, or to throw a 
     * <code>SecurityRuntimeException</code>.
     */
    public static CCUserContext getUserContext()
    {
        return ( CCUserContext ) getCurrentAsInterface();
    }
    

    /*x**************************************
     *  USER-RELATED METHODS
     * **************************************/


    /**
     * getting the 'app user' should mean get the active app user to most
     * people.
     * 
     * TODO: This was originally intended as a convenience method but
     * on further consideration may be simply redundant.
     */
    public static CCUser getAppUser()
    {
        return getActiveAppUser();
    }

    public static CCUser getActiveAppUser()
    {
        return getUserContext().getActiveAppUser();
    }

    public static CCUser getAuthenticatedAppUser()
    {
        return getUserContext().getAuthenticatedAppUser();
    }

    /**
     * getting the 'shared user' should mean get the active shared user to most
     * people.
     * 
     * TODO: This was originally intended as a convenience method but
     * on further consideration may be simply redundant.
     */
    public static User getSharedUser()
    {
        return getActiveSharedUser();
    }

    public static User getSharedUserForAppUser (CCUser appUser ) {
        return UserServices.getSharedUserForCCUser(appUser);
    }

    public static User getActiveSharedUser()
    {
        return getUserContext().getActiveUser();
    }

    public static User getAuthenticatedSharedUser()
    {
        return getUserContext().getAuthenticatedUser();
    }

    public static boolean isUserAnonymous()
    {
        return getUserContext().getActiveAppUser().isAnonymous();
    }
    
    public static void authenticateUserContext( String username )
    {
        SystemServices.authenticateUserContext( username );
    }

    public static void establishUserContextWithoutAuthentication( String username )
    {
        SystemServices.establishUserContextWithoutAuthentication( username );
    }

    public static void unAuthenticateUserContext()
    {
        getUserContext().setAuthenticatedAppUser( null );
        getUserContext().setAuthenticatedUser( null );
    }

    public static boolean isUserAuthenticated()
    {
        return getUserContext().isAuthenticated();
    }
    
    public static boolean isAdminUser()
    {
        return getAuthenticatedSharedUser().getIsAdmin();
    }
    
    /**
     * Use this method to update the UserContext with a new <code>CCUser</code>
     * object.  This is needed when we update a user with the 
     * <code>UserServices.save()</code> method, which gives us a new instance.
     * We then need to set that new instance into <code>UserContext</code> to
     * reflect the changes there.
     * 
     * This method is careful to update the authenticated user only if we're
     * not emulating and we are authenticated.
     * 
     * @param ccUser
     */
    public static void updateAppUser( CCUser ccUser )
    {
        getUserContext().setActiveAppUser( ccUser );
        
        if ( ! isEmulating() && getUserContext().isAuthenticated() )
        {
            getUserContext().setAuthenticatedAppUser( ccUser );
        }
    }
    
    /**
     * See comments for <code>updateAppUser( CCUser )</code> above
     * 
     * @param partyId
     */
    public static void updateSharedUser( long partyId )
    {
        User sharedUser = UserServices.getSharedUserForPartyID( partyId );
        
        UserContextService.setActiveUser( sharedUser );

        if ( ! isEmulating() && getUserContext().isAuthenticated() )
        {
            getUserContext().setAuthenticatedUser( sharedUser );
        }
    }
    

    /*x**************************************
     *  METHODS FOR APP-SPECIFIC CONTEXT DATA
     * **************************************/


    public static DisplaySpec getPurchaseDisplaySpec()
    {
        return getUserContext().getPurchaseDisplaySpec();
    }

    public static void setPurchaseDisplaySpec( DisplaySpec purchaseDisplaySpec )
    {
        getUserContext().setPurchaseDisplaySpec( purchaseDisplaySpec );
    }

    public static void resetPurchaseDisplaySpec()
    {
        getUserContext().resetPurchaseDisplaySpec();
    }

    public static DisplaySpec getLicenseDisplaySpec()
    {
        return getUserContext().getLicenseDisplaySpec();
    }

    public static void setLicenseDisplaySpec( DisplaySpec licenseDisplaySpec )
    {
        getUserContext().setLicenseDisplaySpec( licenseDisplaySpec );
    }

    public static void resetLicenseDisplaySpec()
    {
        getUserContext().resetLicenseDisplaySpec();
    }

    public static Cart getCart()
    {
        return getUserContext().getCart();
    }

    public static void setCart( Cart cart )
    {
        getUserContext().setCart( cart );
    }
    
    public static com.copyright.svc.order.api.data.Cart getCOICart()
    {
        return getUserContext().getCOICart();
    }
    
    public static void setCOICart( com.copyright.svc.order.api.data.Cart cart )
    {
        getUserContext().setCOICart(cart);
    }

    public static OrderPurchases getOrderPurchases()
    {
        return getUserContext().getOrderPurchases();
    }

    public static void setOrderPurchases( OrderPurchases orderPurchases )
    {
        getUserContext().setOrderPurchases( orderPurchases );
    }

    public static OrderLicenses getOrderLicenses()
    {
        return getUserContext().getOrderLicenses();
    }

    public static void setOrderLicenses( OrderLicenses orderLicenses )
    {
        getUserContext().setOrderLicenses( orderLicenses );
    }

    public static OrderSearchResult getOrderSearchResult()
    {
        return getUserContext().getOrderSearchResult();
    }

    public static void setOrderSearchResult( OrderSearchResult orderSearchResult )
    {
        getUserContext().setOrderSearchResult( orderSearchResult );
    }

    
    public static SearchState getSearchState()
    {
        return getUserContext().getSearchState();
    }

    public static void setSearchState( SearchState searchState )
    {
        getUserContext().setSearchState( searchState );
    }
    
    public static ArticleSearchState getArticleSearchState()
    {
        return getUserContext().getArticleSearchState();
    }

    public static void setArticleSearchState( ArticleSearchState articleSearchState )
    {
        getUserContext().setArticleSearchState( articleSearchState );
    }
    
    public static String getLastRequestedWebAction()
    {
        return getUserContext().getLastRequestedWebAction();
    }
    
    public static String getLastRequestedWebModule()
    {
        return getUserContext().getLastRequestedWebModule();
    }

    public static void setSessionInitiatedWithAutoLogin( boolean initiatedWithAutoLogin )
    {
        getUserContext().setSessionInitiatedWithAutoLogin( initiatedWithAutoLogin );
    }
    
    public static boolean isSessionInitiatedWithAutoLogin()
    {
        return getUserContext().isSessionInitiatedWithAutoLogin();
    }
    
    public static void setSessionInitiator( String initiator )
    {
        getUserContext().setSessionInitiator( initiator );
    }
    
    public static String getSessionInitiator()
    {
        return getUserContext().getSessionInitiator();
    }

    public static Boolean isSessionNewlyCreated() {
        HttpSession session = _session.get();
    	
    	if (session!=null) {
    		return (Boolean) session.getAttribute(WebConstants.SessionKeys.SESSION_IS_NEWLY_CREATED);
    	}
    	return false;
    }
    
    public static void setSessionNewlyCreated(Boolean isNewlyCreated) {
        HttpSession session = _session.get();
        if (session!=null) {
        	if (_logger.isDebugEnabled()) {
        		_logger.debug("tagging session with: SESSION_IS_NEWLY_CREATED=" + isNewlyCreated + ", id=" + session.getId());
        	}
        	try {
        		session.setAttribute(WebConstants.SessionKeys.SESSION_IS_NEWLY_CREATED, isNewlyCreated);
        	} catch (IllegalStateException ie) {
        		_logger.debug("tagging of session failed, session previously invalidated. id="+session.getId());
        	}
        }
    }

    /*x**************************************
     *  END APP-SPECIFIC DATA METHODS
     * **************************************/
    
    
    public static CCUserContext createUserContext( CCUser appUser, User sharedUser )
    {
        CCUserContext context = new CCUserContext( appUser, sharedUser );

        return context;
    }

    public static CCUserContext createUserContext( CCUser appUser )
    {
        return new CCUserContext( appUser );
    }

    /**
     * Returns <code>true</code> if an authenticated user is emulating a 
     * different end-user.
     */
    public static boolean isEmulating()
    {
        return getUserContext().isEmulation();
    }
    
    public static void beginEmulation( String username )
    {
        CCUserContext context = getUserContext();
        
        if ( ! context.isAuthenticated() )
            throw new SecurityRuntimeException( "emulation attempted when not authenticated" );
        
        SystemServices.emulateUserContext( username );
    }
    
    /**
     * Returns <code>true</code> iff the active <code>User</code> has the 
     * specified <code>PrivilegeCode</code>.
     * <p>
     * One of the two primary mechanisms for enforcing access control, along 
     * with <code>checkPrivilege( PrivilegeCode )</code>.
     */
    public static boolean authenticatedUserHasPrivilege( PrivilegeCode privilegeCode )
    {
        if ( privilegeCode == null ) return true;
        
        Privilege[] privileges = getCurrentAsInterface().getAuthenticatedSharedUserPrivileges();
        
        if ( privileges == null ) return false;
        
        for ( int i=0; i < privileges.length; i++ )
        {
            Privilege privilege = privileges[i];
            if ( privilege.getPrivilegeCode().getCode().equals( privilegeCode.getCode() ) )
            {
                return true;
            }
        }
        
        return false;
    }
    
    public static void stopEmulation()
    {
        CCUserContext context = getUserContext();
        
        SystemServices.unEmulateUserContext( context );        
    }
    
    public static List<String> getNavigationHistory()
    {
        return getUserContext().getNavigationHistory();
    }

    /**
     * Return a context-relative path corresponding to the n-th last request serviced
     * within the current HttpSession.  The maintained history has a maximum size of 
     * @link{NAV_HISTORY_MAX_SIZE}, so requests for history items older than the
     * max size will return null, as will requests for history items with indices < 0.
     * 
     * @param index
     */
    public String getNavigationHistoryItem( int index )
    {
        List<String> apStack = getUserContext().getNavHistoryList();
        
        if ( index >= apStack.size() || index < 0 )
            return null;
            
        return apStack.get( index );
    }

    /*x**************************************
     *  PROTECTED METHODS
     * **************************************/
     
     static void setActiveAppUser( CCUser ccUser )
     {
         getUserContext().setActiveAppUser( ccUser );
     }
     
     static void setActiveUser( User user )
     {
         getUserContext().setActiveUser( user );
     }
     

    static void setAuthenticated()
    {
        CCUserContext context = getUserContext();
        
        CCUser ccUser = context.getActiveAppUser();
        
        if ( ccUser == null )
            throw new SecurityRuntimeException( "null app user at authentication time" );
            
        User sharedUser = context.getActiveUser();
        
        if ( sharedUser == null )
            throw new SecurityRuntimeException( "null shared user at authentication time" );
        
        context.setAuthenticatedAppUser( ccUser );
        context.setAuthenticatedUser( sharedUser );
        /* Initiate a refresh of the cart from the DB.  Now that
         * we've authenticated, this will ensure the cart reflects any user
         * price discounts */
        CartServices.refreshCart();
    }

    static void pushToNavHistory( String path )
    {
        _logger.debug( "pushing path: " + path );

        LinkedList<String> apStack = getUserContext().getNavHistoryList();

        apStack.addFirst( path );

        if ( apStack.size() > NAV_HISTORY_MAX_SIZE )
        {
            apStack.removeLast();
        }
    }

    /**
     * Stores the <code>HttpSession</code> in thread-local storage.
     */
    static void setHttpSession( HttpSession session )
    {
        _session.set( session );
    }

    /**
     * Returns the <code>HttpSesion</code> from thread-local storage.  
     * Guaranteed to return a non-null object or throw.
     */
    public static HttpSession getHttpSession()
    {
        HttpSession session = _session.get();
        
        if ( session == null )
        {
            throw new SecurityRuntimeException( "HTTP session was null when trying to retrieve it" );
        }
        
        return session;
    }

    /**
     * Sets the <code>UserContext</code> on the <code>HttpSession</code>.  
     */
    static void setContextOnSession( CCUserContext context )
    {
        HttpSession session = getHttpSession();
        setContextOnSession( session, context );
    }
    
    static void setContextOnSession( HttpSession session, CCUserContext context )
    {
        session.setAttribute( USER_CONTEXT_SESSION_KEY, context );        
    }

    /**
     * Returns the <code>UserContext</code> from the <code>HttpSession</code>.
     */
    public static CCUserContext getContextFromSession()
    {
    	try {
			HttpSession session = getHttpSession();
	    	return ( CCUserContext )session.getAttribute( USER_CONTEXT_SESSION_KEY );
    	}
    	catch (IllegalStateException exc)
    	{
    		//must be an invalidated session return null
    		return null;
    	}

    }
    
    public static void updateAuthenticatedUser( CCUser ccUser )
    {
        getUserContext().setAuthenticatedAppUser( ccUser );
    }
    public static  boolean isInRlnkQuickPricePage(){
    	if(getHttpSession().getAttribute(RlnkSessionConstants.RL_REQUEST_PARM_IN_SESSION)!=null){
    		return true;
    	}
    	return false;
    }
    public static void setCartItemEdited(boolean itemEdited){
    	 getUserContext().setCartItemEdited(itemEdited);
    }
    public static boolean isCartItemEdited(){
   	 return getUserContext().isCartItemEdited();
   }
    
    //  2010-09-17  MSJ
    //  Check account status.  Is the user's account on hold?
    //  This method should be used in key places in lieu of
    //  the check performed with every request (request processor
    //  was hitting SystemServices check which performed this
    //  same action and threw an error whenever an on hold
    //  account was encountered).  Also, I chose to use
    //  "getAuthenticatedSharedUser" because at the point this
    //  check is made, the user should be authenticated.  The
    //  point is to let a user log in and if there are out-
    //  standing bills, the user can apply payment towards
    //  them online.
    
    public static boolean isUserAccountOnHold()
    {
        return getAuthenticatedSharedUser().getOnHold();
    }
    
    /**
     * Returns the suppress NRLS fee flag value for
     * a licensee account
     * @return
     */
    public static boolean isSuppressNRLSFee()
    {
    	if (isUserAuthenticated()) {
    		return getAuthenticatedSharedUser().getSuppressNRLSFee();
    	}
    	else
    	{
    		return false;
    	}
    }
    /**
     * Returns the active RlnkSessionID if there is one. 
     * If there isn't one, this method will create and return a new RlnkSessionID. 
     * This method will never return null.
     * @return
     */
    public static String getRlnkSessionID(){
    	String httpSessionId = UserContextService.getHttpSession().getId();
    	String rlnkSessionId=UserContextService.getUserContext().getActiveAppUser().getRlnkSessionId();
    	boolean createNewSession = false;
    	
    	/*
    	 * We create a new Rightlink session id under the following conditions:
    	 * 1. we don't find one on the Active app user record (null)
    	 * 2. the one we find is based on the c.com session id (32 chars)
    	 * 3. the one we find does not match an md5 hash of the http session (plus an extra x)
    	 * 
    	 * The session id we create is an md5 hash of the j_session_id. Because md5
    	 * is deterministic, we can compare the output of the hash function to the
    	 * users current rl session id. If they're not equal, we know the j_session_id is
    	 * not the same one that was used to create the original rl session id. If
    	 * thats the case we'll use the new rl session id and update the users
    	 * db record to reflect it.
    	 */
    	String md5HttpSessionId = HashFunctions.MD5(httpSessionId);
    	if(StringUtils.isEmpty(rlnkSessionId)){
    		_logger.debug("generating RlnkSessionID because we don't have one yet");
    		createNewSession=true;
    	} else if (!rlnkSessionId.equals(md5HttpSessionId)) {
    		_logger.debug("generating RlnkSessionID because we're using a new http session id");
    		createNewSession=true;
    	}
    	if (createNewSession) {
    		rlnkSessionId = md5HttpSessionId;
    		_logger.debug("new RlnkSessionID " + rlnkSessionId);
    		UserServices.updateCurrentUserRLnkSessionID(rlnkSessionId); // update user context/db with new RL session id    		
    	}

		UserContextService.getHttpSession().setAttribute(WebConstants.SessionKeys.RIGHTSLINK_SESSION_ID, rlnkSessionId);
    	
    	return UserContextService.getUserContext().getActiveAppUser().getRlnkSessionId();
    }
    
 
}
