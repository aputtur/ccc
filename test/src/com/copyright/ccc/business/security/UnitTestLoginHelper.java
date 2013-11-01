package com.copyright.ccc.business.security;

/**
 * Login services for unit tests.
 */
public class UnitTestLoginHelper
{
    private UnitTestLoginHelper()
    {
    }

    /**
     * Logs a user with userName/password
     */
    public static void login()
    {
        authenticateUser();
    }

    public static void authenticateUser()
    {
        CCUserContext ctxt = UserContextService.getUserContext();

        ctxt.setAuthenticatedAppUser( ctxt.getActiveAppUser() );
        ctxt.setAuthenticatedUser( ctxt.getActiveUser() );
    }


    public static void setupAuthenticatedUserContext( String auid, String username )
    {
        CCUserContext context = SystemServices.createUserContextForAUID( auid );

        UserContextService.setCurrent( context );

        SystemServices.authenticateUserContext( username );

        
    }

    public static void setupRecognizedUserContext( String auid )
    {
        CCUserContext context = SystemServices.createUserContextForAUID( auid );

        UserContextService.setCurrent( context );
    }
}
