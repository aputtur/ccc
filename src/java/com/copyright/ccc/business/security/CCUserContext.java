package com.copyright.ccc.business.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.copyright.ccc.business.data.CCUser;
import com.copyright.ccc.business.data.DisplaySpec;
import com.copyright.ccc.business.data.OrderLicenses;
import com.copyright.ccc.business.data.OrderPurchases;
import com.copyright.ccc.business.data.OrderSearchResult;
import com.copyright.ccc.business.services.articlesearch.ArticleSearchState;
import com.copyright.ccc.business.services.search.SearchState;
import com.copyright.ccc.business.services.user.User;
import com.copyright.data.ConsumingAppCodes;
import com.copyright.data.order.Cart;
import com.copyright.workbench.security.Privilege;


/**
 * The princpal repository of a session with the CC business tier.
 * Session in this context does not imply any particular type of end-user
 * session such as a web session, a session via a rich-client application, or
 * some sort of series of interactions via a web service front-end.  All of
 * these end-user sessions would require a <code>UserContext</code> object.
 * <p>
 * The constructor(s) and several mutators are marked with default visibility.
 * This provides a measure of security by allowing only package members to
 * access these methods.  Client code should always create a
 * <code>UserContext</code> via the <code>UserContextHelper</code>.
 */
public final class CCUserContext implements com.copyright.workbench.security.UserContext ,Serializable
{

    /**
	 *
	 */
	private static final long serialVersionUID = 1L;

	static Logger _logger = Logger.getLogger( CCUserContext.class );

    /**
     * CC User object for the person using the application.
     */
    private CCUser _authenticatedAppUser;

    /**
     * CC User object for the person for whom data should be
     * retrieved; is equal to the <code>AuthenticatedUser</code> unless the
     * user is being emulated.
     */
    private CCUser _activeAppUser;

    /**
     * OID/Telesales User object for the person using the application.
     */
    private User _authenticatedUser;

    /**
     * OID/Telesales User object for the person for whom data should be
     * retrieved; is equal to the <code>AuthenticatedUser</code> unless the
     * user is being emulated.
     */
    private User _activeUser;

    /**
     * Maintains user's filter and sort criteria for CCPurchase objects
     * throughout session.
     */
    private DisplaySpec _purchaseDisplaySpec;

    /**
     * Maintains user's filter and sort criteria for CCLicense objects
     * throughout session.
     */
    private DisplaySpec _licenseDisplaySpec;

    /**
     * Maintains user's shopping cart throughout session.
     */
    private Cart _cart;

    private com.copyright.svc.order.api.data.Cart _COICart;

    private OrderSearchResult _orderSearchResult;
    /**
     *
     */
    private OrderLicenses _orderLicenses;

    /**
     *
     */
    private OrderPurchases _orderPurchases;

    /**
     *
     */
    private SearchState _searchState;

    private ArticleSearchState _articleSearchState;

    private String _lastRequestedWebAction;

    private String _lastRequestedWebModule;

    private LinkedList<String> _navHistoryList;

    private boolean _sessionInitiatedWithAutoLogin = false;
    private String _sessionInitiator;
    private String _method_of_pmt_restrictor = "NONE";

    /**
     * Inform if cart page is rendered after cart item got edited  
     */
    private boolean _cartItemEdited=false;
    /**
     * Constructs a <code>UserContext</code> object.  Default visibility
     * provides a measure of security.
     */
    private CCUserContext()
    {
    }

    CCUserContext( CCUser activeAppUser )
    {
        _activeAppUser = activeAppUser;
    }

    CCUserContext( CCUser activeAppUser, User activeUser )
    {
        _activeAppUser = activeAppUser;
        _activeUser = activeUser;
    }

    void setActiveUser( User activeUser )
    {
        _activeUser = activeUser;
    }

    void setAuthenticatedUser( User authenticatedUser )
    {
        _authenticatedUser = authenticatedUser;
    }

    void setActiveAppUser( CCUser activeAppUser )
    {
        _activeAppUser = activeAppUser;
    }

    void setAuthenticatedAppUser( CCUser authenticatedCCUser )
    {
        _authenticatedAppUser = authenticatedCCUser;
    }

    boolean isAuthenticated()
    {
        return getAuthenticatedAppUser() != null && getAuthenticatedUser() != null;
    }

    boolean isEmulation()
    {

        // To be very sure, check all types of objects that change when a user
        // is being emulated.

        // 1. Check the Shared User objects


        if ( _authenticatedUser == null || _activeUser == null) return false;

        if (_authenticatedUser.getPK() == null) return false;

        boolean sameUsers =
            _authenticatedUser.getPK().equals( _activeUser.getPK() );

        if ( sameUsers ) return false;

        // 2. Check the application's User objects

        if ( _authenticatedAppUser == null || getActiveAppUser() == null ) return false;

        boolean sameAppUsers = _authenticatedAppUser.getID() == getActiveAppUser().getID();

        if ( sameAppUsers ) return false;

        return true;
    }

    /*x************************************
    *
    *  CC2 app specific payload object access
    *  These objects live in CCUserContext but
    *  are accessed by Service tier through static
    *  methods here so as to give object associated
    *  with current session/thread
    *
    ***************************************/

    DisplaySpec getNewDisplaySpec()
    {
        return new DisplaySpec();
    }

    DisplaySpec getPurchaseDisplaySpec()
    {
        if ( _purchaseDisplaySpec == null )
            _purchaseDisplaySpec = getNewDisplaySpec();

        _purchaseDisplaySpec.setDisplaySpecType(1); //Purchase Type

        return _purchaseDisplaySpec;
    }

    void setPurchaseDisplaySpec( DisplaySpec purchaseDisplaySpec )
    {
        _purchaseDisplaySpec = purchaseDisplaySpec;
    }

    DisplaySpec resetPurchaseDisplaySpec()
    {
        return _purchaseDisplaySpec = getNewDisplaySpec();
    }

    DisplaySpec getLicenseDisplaySpec()
    {
        if ( _licenseDisplaySpec == null )
            _licenseDisplaySpec = getNewDisplaySpec();

        _licenseDisplaySpec.setDisplaySpecType(2); //License Type

        return _licenseDisplaySpec;
    }

    void setLicenseDisplaySpec( DisplaySpec licenseDisplaySpec )
    {
        _licenseDisplaySpec = licenseDisplaySpec;
    }

    DisplaySpec resetLicenseDisplaySpec()
    {
        return _licenseDisplaySpec = getNewDisplaySpec();
    }

    Cart getCart()
    {
        return _cart;
    }

    void setCart( Cart cart )
    {
        _cart = cart;
    }

    com.copyright.svc.order.api.data.Cart getCOICart()
    {
        return _COICart;
    }

    void setCOICart( com.copyright.svc.order.api.data.Cart cart )
    {
        _COICart = cart;
    }

    OrderPurchases getOrderPurchases()
    {
        if ( _orderPurchases == null )
            _orderPurchases = new OrderPurchases();

        return _orderPurchases;
    }

    void setOrderPurchases( OrderPurchases orderPurchases )
    {
        _orderPurchases = orderPurchases;
    }

    public OrderLicenses getOrderLicenses()
    {
        if ( _orderLicenses == null )
            _orderLicenses = new OrderLicenses();

        return _orderLicenses;
    }

    public void setOrderLicenses( OrderLicenses orderLicenses )
    {
        _orderLicenses = orderLicenses;
    }

    public OrderSearchResult getOrderSearchResult()
    {
        if ( _orderSearchResult == null )
            _orderSearchResult = new OrderSearchResult();

        return _orderSearchResult;
    }

    public void setOrderSearchResult( OrderSearchResult orderSearchResult )
    {
        _orderSearchResult = orderSearchResult;
    }


    /**
     * Returns the SearchState associated with the UserContext.
     * Creates a new SearchState and puts it on 
     * the UserContext if the existing searchState is null.
     * @return
     */
    public SearchState getSearchState()
    {
        if (_searchState == null) {
        	_searchState = new SearchState();
        }
           return _searchState;
    }


    public void setSearchState( SearchState searchState )
    {
        _searchState = searchState;
    }

    public ArticleSearchState getArticleSearchState()
    {
        return _articleSearchState;
    }

    public void setArticleSearchState( ArticleSearchState articleSearchState )
    {
        _articleSearchState = articleSearchState;
    }

    LinkedList<String> getNavHistoryList()
    {
        if ( _navHistoryList == null )
            _navHistoryList = new LinkedList<String>();

        return _navHistoryList;
    }

    public String getMethodOfPmtRestrictor() {
        return _method_of_pmt_restrictor;
    }

    public void setMethodOfPmtRestrictor( String methodOfPmt ) {
        String pmt;
        if (methodOfPmt != null) {
            pmt = methodOfPmt.toUpperCase();
            if (!"CC".equals(pmt) && !"INV".equals(pmt) && !"NONE".equals(pmt)) {
                _method_of_pmt_restrictor = "NONE";
            }
            else _method_of_pmt_restrictor = pmt;
        }
        else _method_of_pmt_restrictor = "NONE";
    }

    /**
     * Return a list of context-relative paths corresponding to the last N requests serviced
     * within the current HttpSession.
     */
    public List<String> getNavigationHistory()
    {
        return new ArrayList<String>( getNavHistoryList() );
    }

    public void setNavigationHistory(LinkedList<String> navHistoryList)
    {
        _navHistoryList = navHistoryList;
    }


    /*x************************************
    *
    *  end app specific objects
    *
    ***************************************/

    public User getAuthenticatedUser()
    {
        return _authenticatedUser;
    }

    public User getActiveUser()
    {
        return _activeUser;
    }

    public CCUser getActiveAppUser()
    {
        if (_activeAppUser.getPartyID() < 0) {
            try {
                _activeUser.refreshBasicData();

                if (_activeUser.getParty().getPartyId().longValue() > 0) {
                    _activeAppUser.setPartyID(_activeUser.getParty().getPartyId().longValue());
                }
            }
            catch(Exception e) {
                //  MT told me to ignore this.
            }
        }
        return _activeAppUser;
    }

    public CCUser getAuthenticatedAppUser()
    {
        return _authenticatedAppUser;
    }

    public long getAuthenticatedAppUserID()
    {
        if ( _authenticatedAppUser != null )
        {
            return _authenticatedAppUser.getID();
        }

        _logger.trace( "authenticated app user is null -- returning 0" );

        return 0;
    }

    public long getActiveAppUserID()
    {
        if ( _activeAppUser != null )
        {
            return _activeAppUser.getID();
        }

        _logger.trace( "active app user is null -- returning 0" );

        return 0;
    }

    public long getAuthenticatedSharedUserID()
    {
        if ( _authenticatedUser != null )
        {
            return _authenticatedUser.getID();
        }

        _logger.trace( "authenticated user is null -- returning 0" );

        return 0;
    }

    public long getActiveSharedUserID()
    {
        if ( _activeUser != null )
        {
            return _activeUser.getID();
        }

        _logger.trace( "active user is null -- returning 0" );

        return 0;
    }

    public Privilege[] getActiveSharedUserPrivileges()
    {
        User activeSharedUser = getActiveUser();

        if ( activeSharedUser == null )
            return new Privilege[0];
        else
            return activeSharedUser.getPrivileges();
    }

    public Privilege[] getAuthenticatedSharedUserPrivileges()
    {
        User authSharedUser = getAuthenticatedUser();

        if ( authSharedUser == null )
            return new Privilege[0];
        else
            return authSharedUser.getPrivileges();
    }

    /**
     * Required by Shared Services' UserContext interface
     * @return unique ID for the consuming application
     *
     * TODO: ask Tom about this -- looks like we need a unique ID
     */
    public String getConsumingApplication()
    {
        return ConsumingAppCodes.CCC;
    }

    public void setLastRequestedWebAction( String lastRequestedWebAction )
    {
        _lastRequestedWebAction = lastRequestedWebAction;
    }

    public String getLastRequestedWebAction()
    {
        return _lastRequestedWebAction;
    }

    public void setLastRequestedWebModule( String lastRequestedWebModule )
    {
        _lastRequestedWebModule = lastRequestedWebModule;
    }

    public String getLastRequestedWebModule()
    {
        return _lastRequestedWebModule;
    }

    public void setSessionInitiatedWithAutoLogin( boolean sessionInitiatedWithAutoLogin )
    {
        _sessionInitiatedWithAutoLogin = sessionInitiatedWithAutoLogin;
    }

    public boolean isSessionInitiatedWithAutoLogin()
    {
        return _sessionInitiatedWithAutoLogin;
    }

    public void setSessionInitiator( String sessionInitiator )
    {
        _sessionInitiator = sessionInitiator;
    }

    public String getSessionInitiator()
    {
        return _sessionInitiator;
    }
    
    
    public void setCartItemEdited( boolean cartItemEdited )
    {
        _cartItemEdited = cartItemEdited;
    }

    public boolean isCartItemEdited()
    {
        return _cartItemEdited;
    }
}
