package com.copyright.ccc.business.security;

import com.copyright.ccc.CCTestCase;
import com.copyright.ccc.CCTestSetup;

import junit.framework.Test;
import junit.framework.TestSuite;

public class UserContextTest extends CCTestCase
{
  /**
   * Returns the suite of all tests in this class.
   */
  public static Test suite()
  {
      TestSuite suite = new TestSuite( UserContextTest.class );
      return new CCTestSetup( suite );
  }
}
