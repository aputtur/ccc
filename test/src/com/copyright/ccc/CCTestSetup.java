package com.copyright.ccc;

import com.copyright.ccc.business.security.CCUserContext;

import com.copyright.ccc.business.security.UnitTestLoginHelper;
import com.copyright.ccc.business.security.UserContextService;

import junit.extensions.TestSetup;

import junit.framework.Test;

/**
 * Performs setup and teardown functions for CC2 test cases.
 */
public class CCTestSetup extends TestSetup
{

  private static final String UNIT_TEST_USER_ID = "flyboy2@copyright.com";
  private static final String UNIT_TEST_USER_AUID = "98caa8c6e055c2ac48b385df6eb8b9d25b3ec427";

  /**
   * Constructs an instance of <code>CCTestSetup</code>.
   */
  public CCTestSetup( Test test )
  {
      super( test );
  }
  
  /**
   * This method is executed before every test case.
   */
  @Override
  public void setUp()
  {
      // Subclasses may choose to refrain from resetting the UserContext 
      // before every test by overriding setUp().
      establishUserContext();
  }

  /**
     * This method is executed after every test case.
     */
  @Override
  public void tearDown()
  {
    // Nothing yet.
  }

  /**
    * Establishes a <code>CCUserContext</code> for a business service call.
    */
   private void establishUserContext()
   {
    
     CCUserContext ctxt = UserContextService.createUserContext( null,null );
     
     UserContextService.setCurrent( ctxt );
     
     UnitTestLoginHelper.setupAuthenticatedUserContext( UNIT_TEST_USER_AUID, UNIT_TEST_USER_ID );    
       
   }
}
