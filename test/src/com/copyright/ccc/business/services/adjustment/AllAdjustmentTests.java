package com.copyright.ccc.business.services.adjustment;

import com.copyright.ccc.CCTestCase;
import com.copyright.ccc.CCTestSetup;

import java.util.Enumeration;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AllAdjustmentTests extends CCTestCase
{
  public AllAdjustmentTests( String testCase )
  {
    super(testCase);
  }
  
  /**
   * Returns the suite of all adjustment tests in the application.
   */
  public static Test suite()
  {
      TestSuite adjustmentTests = new TestSuite();
           
      addTests( adjustmentTests, AdjustmentServicesTest.class );
            
      return new CCTestSetup( adjustmentTests );
  }

  
  
  /**
   * Adds all tests from the specified test case to the specified 
   * <code>TestSuite</code>.
   */
private static void addTests( TestSuite suite, Class<?> testCaseClass )
  {
      TestSuite aSuite = new TestSuite( testCaseClass );
      @SuppressWarnings("unchecked")
      Enumeration<TestCase> someTests = aSuite.tests();
      while ( someTests.hasMoreElements() )
      {
          TestCase aTest = someTests.nextElement();
          suite.addTest( aTest );
      }
  }
}
