package com.copyright.ccc.business.security;

import com.copyright.ccc.CCTestCase;
import com.copyright.ccc.business.data.CCUser;
import com.copyright.ccc.business.services.user.UserServices;

import com.copyright.workbench.security.HashFunctions;

import junit.extensions.TestSetup;

import junit.framework.Test;
import junit.framework.TestSuite;

public class SystemServicesTest extends CCTestCase
{
    String testNewUsernamePrefix = "unit_test_user_";

    /**
     * System services test does not establish a UserContext in this
     * method, as most other tests in CC2 will.  This is because System
     * services are, by definition, meant to function without a 
     * UserContext on the thread of execution.
     */
    public static Test suite()
    {
        TestSuite suite = new TestSuite( SystemServicesTest.class );
        return new TestSetup( suite );
    }
    
    public void testCreateUser()
    {
        CCUser ccUser = UserServices.createCCUser();

        String testNewUserUsername = testNewUsernamePrefix + HashFunctions.SHA1();

        ccUser.setUsername( testNewUserUsername );

        //ccUser = SystemServices.saveNewCCUserWithAUID( ccUser );
        
        String testNewUserAUID = ccUser.getAuid();

        //CCUser retrievedUser = SystemServices.getCCUserForAUID( testNewUserAUID );

        //assertEquals( retrievedUser.getUsername(), testNewUserUsername );
        //assertEquals( retrievedUser.getID(), ccUser.getID() );
    }
}
