
package com.copyright.ccc.business.services.user;
import com.copyright.ccc.CCTestCase;
import com.copyright.ccc.CCTestSetup;

import com.copyright.ccc.business.data.CCUser;

import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.user.CCUserImpl;
import com.copyright.ccc.business.services.user.SearchPermissionType;
import com.copyright.ccc.business.services.user.UserServices;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;

/**
 * Contains unit tests for <code>UserServices</code> functionality.
 */
public class UserServicesTest extends CCTestCase
{
    static Logger _logger = Logger.getLogger( CCTestCase.class );

    String testAUID = "024c345ecff931e34ef5f537129f29e047befb14";
    String RL_SESSION_ID = "024c345ecff931e34ef5f537129f29e047befb14";
    String testUsername = "aputtur@copyright.com";
    
    long testNewCartID = 1207;

    /**
     * Returns the suite of all tests in this class.
     */
    public static Test suite()
    {
        TestSuite suite = new TestSuite( UserServicesTest.class );
        suite.addTestSuite(UserServicesTest.class);
        return new CCTestSetup( suite );
    }


    public void testGetUserByAUID()
    {
        System.out.println( " ====> test output <====" );
        CCUser testUser = null;

        try
        {
            testUser = UserServices.getCCUserForAUID(testAUID);
        }
        catch ( Exception e )
        {
            fail( "getCCUserForAUID threw an exception: " +
                  e.getClass().getName() + " - " + e.getMessage() );
        }

        assertNotNull( "user retrieved by getCCUserForAUID", testUser );

        assertEquals( "", testUser.getUsername(),  testUsername );
    }

    private CCUser getActiveAppUser()
    {
        CCUser testUser = null;
        
        try
        {
            testUser = UserContextService.getActiveAppUser();
        }
        catch ( Exception e )
        {
            fail( "getActiveAppUser() threw an exception: " +
                  e.getClass().getName() + " - " + e.getMessage() );
        }

        assertNotNull( testUser );

        return testUser;        
    }

    public void testUpdateUser()
    {
        CCUser testUser = getActiveAppUser();
        
        String auid = testUser.getAuid();
        
        assertNotNull( auid );
        
        long oldCartID = testUser.getLastCartID();
        long newCartID = ( oldCartID + 1 ) % Integer.MAX_VALUE;
        
        boolean oldAlwaysInvoice = testUser.isAlwaysInvoice();
        
        boolean[] oldSearchPrefs = new boolean[ SearchPermissionType.getCardinality() ];
        
        for ( int i = 0; i < SearchPermissionType.getCardinality(); i++ )
        {
           oldSearchPrefs[i] = testUser.getSearchPreference( SearchPermissionType.getByValue( i ) );            
        }  
        
        UserServices.updateCurrentUserLastCartID( newCartID );
        UserServices.updateCurrentUserAlwaysInvoice( ! testUser.isAlwaysInvoice() );
        
        //UserServices.updateCurrentUserSearchPreference( SearchPermissionType.BUSINESS_LIC_PHOTOCOPY
          //  ! testUser.getSearchPreference( SearchPermissionType.BUSINESS_LIC_PHOTOCOPY ) );
        UserServices.updateCurrentUserSearchPreference( SearchPermissionType.BUSINESS_LIC_PHOTOCOPY,
                false );

        for ( int i = 0; i < SearchPermissionType.getCardinality(); i++ )
        {
            UserServices.updateCurrentUserSearchPreference( SearchPermissionType.getByValue( i ),
                ! testUser.getSearchPreference( SearchPermissionType.getByValue( i ) ) );            
        }  
        
        CCUser checkContextUser = getActiveAppUser();

        assertEquals( new Long( checkContextUser.getLastCartID() ), new Long( newCartID ) );
        
        assertFalse( ( checkContextUser.isAlwaysInvoice() == oldAlwaysInvoice ) );

        for ( int i = 0; i < SearchPermissionType.getCardinality(); i++ )
        {
            SearchPermissionType spType = SearchPermissionType.getByValue( i );
            boolean newSpValue = checkContextUser.getSearchPreference( spType );

            String debugMsg = "SearchPermissionType in context user: " +
                spType.toString() + ": " +
                "old=" + Boolean.toString( oldSearchPrefs[i] ) + 
                ", new=" + Boolean.toString( newSpValue );

            _logger.debug( debugMsg );
            
            assertFalse( ( oldSearchPrefs[i] == newSpValue ) );    
        }  
        
        CCUser checkDBUser = UserServices.getCCUserForAUID( auid );
        
        assertEquals( checkDBUser.getID(), testUser.getID() );

        assertFalse( ( oldCartID == checkDBUser.getLastCartID() ) );
        
        assertEquals( new Long( checkDBUser.getLastCartID() ), new Long( newCartID ) );
        
        assertFalse( ( checkDBUser.isAlwaysInvoice() == oldAlwaysInvoice ) );
        
        for ( int i = 0; i < SearchPermissionType.getCardinality(); i++ )
        {
            SearchPermissionType spType = SearchPermissionType.getByValue( i );
            boolean newSpValue = checkDBUser.getSearchPreference( spType );
            
            String debugMsg = "SearchPermissionType not different in DB-saved object: " +
                spType.toString() + ": " +
                "old=" + Boolean.toString( oldSearchPrefs[i] ) + 
                ", new=" + Boolean.toString( newSpValue );

            assertFalse( debugMsg, ( oldSearchPrefs[i] == newSpValue ) );            
        }  
    }
    
    public void testUpdateCartID()
    {
        UserServices.updateCurrentUserLastCartID( testNewCartID );
        
        CCUser contextUser = UserContextService.getActiveAppUser();
        
        CCUser updatedUser = UserServices.getCCUserForAUID( contextUser.getAuid() );
        
        assertEquals( updatedUser.getLastCartID(), testNewCartID );
        
        assertEquals( contextUser.getID(), updatedUser.getID() );
        assertEquals( ((CCUserImpl)contextUser).getVersion(), ((CCUserImpl)updatedUser).getVersion() );
        assertEquals( contextUser.getLastCartID(), updatedUser.getLastCartID() );
        
    }
    public void testClearRLSessionID()
    {
        UserServices.updateCurrentUserRLnkSessionID(null );
        
        CCUser contextUser = UserContextService.getActiveAppUser();
        
        CCUser updatedUser = UserServices.getCCUserForAUID( contextUser.getAuid() );
        
        assertNull( "Current RL Session ID is null" ,updatedUser.getRlnkSessionId());
        
        assertEquals( contextUser.getID(), updatedUser.getID() );
        assertEquals( ((CCUserImpl)contextUser).getVersion(), ((CCUserImpl)updatedUser).getVersion() );
        assertEquals( contextUser.getRlnkSessionId(), updatedUser.getRlnkSessionId() );
        
    }
    
    public void testUpdateRLSessionID()
    {
        UserServices.updateCurrentUserRLnkSessionID(RL_SESSION_ID );
        
        CCUser contextUser = UserContextService.getActiveAppUser();
        
        CCUser updatedUser = UserServices.getCCUserForAUID( contextUser.getAuid() );
        
        assertEquals( updatedUser.getRlnkSessionId(), RL_SESSION_ID );
        
        assertEquals( contextUser.getID(), updatedUser.getID() );
        assertEquals( ((CCUserImpl)contextUser).getVersion(), ((CCUserImpl)updatedUser).getVersion() );
        assertEquals( contextUser.getRlnkSessionId(), updatedUser.getRlnkSessionId() );
        
    }
}
