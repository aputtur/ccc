package com.copyright.ccc.business.services.ecommerce;

import com.copyright.ccc.CCTestCase;
import com.copyright.ccc.business.data.PublicationPermission;
import com.copyright.ccc.business.services.cart.ECommerceConstants;
import com.copyright.ccc.web.mock.MockPublicationPermissionServices;
import com.copyright.ccc.web.transaction.RepublicationConstants;
import com.copyright.data.inventory.StandardWork;
import com.copyright.data.inventory.UsageDescriptor;
import com.copyright.data.inventory.Work;
import com.copyright.data.order.PermissionRequest;
import com.copyright.data.order.UsageDataEmail;
import com.copyright.service.inventory.MapperServiceAPI;
import com.copyright.service.inventory.MapperServiceFactory;
import com.copyright.workbench.i18n.Money;

import java.util.Date;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.lang.StringUtils;


/**
 * Contains unit tests for <code>PurchasablePermissionFactory</code>.
 */
public class PurchasablePermissionFactoryTest extends CCTestCase
{
  
  /**
   * Returns the suite of all tests in this class.
   */
  public static Test suite()
  {
      return new TestSuite( PurchasablePermissionFactoryTest.class );
      
  }
  
  
  /**
   * Test for <code>PurchasablePermissionFactory.createRepublicationSpecialOrderPurchasablePermission()</code>.
   */
  public void testCreateRepublicationSpecialOrderPurchasablePermission()
  {
      MapperServiceAPI api = MapperServiceFactory.getInstance().getService();
      long tpuInst = api.getUnderlyingTpuInst(UsageDescriptor.NON_ACADEMIC_TRX_REPUB_BROCHURE);
      PurchasablePermission pp = PurchasablePermissionFactory.createRepublicationSpecialOrderPurchasablePermission(tpuInst);
       
      assertNotNull( "Expected to find an instance of PurchasablePermission" , pp );
      assertFalse("PurchasablePermission.isAcademic() should return FALSE" , pp.isAcademic());
      assertTrue("PurchasablePermission.isSpecialOrder() should return TRUE" , pp.isSpecialOrder());
      assertTrue("PurchasablePermission.isSpecialOrderFromScratch() should return TRUE" , pp.isSpecialOrderFromScratch());
      assertFalse("PurchasablePermission.isEmail() should return FALSE" , pp.isEmail());
      assertFalse("PurchasablePermission.isPhotocopy() should return FALSE" , pp.isPhotocopy());
      assertTrue("PurchasablePermission.isRepublication() should return TRUE" , pp.isRepublication());
      assertFalse("PurchasablePermission.isNet() should return FALSE" , pp.isNet());
      assertFalse("PurchasablePermission.isInternet() should return FALSE" , pp.isInternet());
      assertFalse("PurchasablePermission.isIntranet() should return FALSE" , pp.isIntranet());
      assertFalse("PurchasablePermission.isExtranet() should return FALSE" , pp.isExtranet());
      assertFalse("PurchasablePermission.isDigital() should return FALSE" , pp.isDigital());
      assertFalse("PurchasablePermission.isECCS() should return FALSE" , pp.isECCS());
      assertFalse("PurchasablePermission.isAPS() should return false" , pp.isAPS());
      
  }
  
  /**
   * Test for <code>PurchasablePermissionFactory.createEmailSpecialOrderPurchasablePermission()</code>.
   */
  public void testCreateEmailSpecialOrderPurchasablePermission()
  {
      PurchasablePermission pp = PurchasablePermissionFactory.createEmailSpecialOrderPurchasablePermission();
      
      assertNotNull( "Expected to find an instance of PurchasablePermission" , pp );
      assertFalse("PurchasablePermission.isAcademic() should return FALSE" , pp.isAcademic());
      assertTrue("PurchasablePermission.isSpecialOrder() should return TRUE" , pp.isSpecialOrder());
      assertTrue("PurchasablePermission.isSpecialOrderFromScratch() should return TRUE" , pp.isSpecialOrderFromScratch());
      assertTrue("PurchasablePermission.isEmail() should return TRUE" , pp.isEmail());
      assertFalse("PurchasablePermission.isPhotocopy() should return FALSE" , pp.isPhotocopy());
      assertFalse("PurchasablePermission.isRepublication() should return FALSE" , pp.isRepublication());
      assertFalse("PurchasablePermission.isNet() should return FALSE" , pp.isNet());
      assertFalse("PurchasablePermission.isInternet() should return FALSE" , pp.isInternet());
      assertFalse("PurchasablePermission.isIntranet() should return FALSE" , pp.isIntranet());
      assertFalse("PurchasablePermission.isExtranet() should return FALSE" , pp.isExtranet());
      assertTrue("PurchasablePermission.isDigital() should return TRUE" , pp.isDigital());
      assertFalse("PurchasablePermission.isECCS() should return FALSE" , pp.isECCS());
      assertFalse("PurchasablePermission.isAPS() should return false" , pp.isAPS());
      
  }
  
  /**
   * Test for <code>PurchasablePermissionFactory.createPhotocopySpecialOrderPurchasablePermission()</code>.
   */
  public void testCreatePhotocopySpecialOrderPurchasablePermission()
  {
      PurchasablePermission pp = PurchasablePermissionFactory.createPhotocopySpecialOrderPurchasablePermission();
      
      assertNotNull( "Expected to find an instance of PurchasablePermission" , pp );
      assertFalse("PurchasablePermission.isAcademic() should return FALSE" , pp.isAcademic());
      assertTrue("PurchasablePermission.isSpecialOrder() should return TRUE" , pp.isSpecialOrder());
      assertTrue("PurchasablePermission.isSpecialOrderFromScratch() should return TRUE" , pp.isSpecialOrderFromScratch());
      assertFalse("PurchasablePermission.isEmail() should return FALSE" , pp.isEmail());
      assertTrue("PurchasablePermission.isPhotocopy() should return TRUE" , pp.isPhotocopy());
      assertFalse("PurchasablePermission.isRepublication() should return FALSE" , pp.isRepublication());
      assertFalse("PurchasablePermission.isNet() should return FALSE" , pp.isNet());
      assertFalse("PurchasablePermission.isInternet() should return FALSE" , pp.isInternet());
      assertFalse("PurchasablePermission.isIntranet() should return FALSE" , pp.isIntranet());
      assertFalse("PurchasablePermission.isExtranet() should return FALSE" , pp.isExtranet());
      assertFalse("PurchasablePermission.isDigital() should return FALSE" , pp.isDigital());
      assertFalse("PurchasablePermission.isECCS() should return FALSE" , pp.isECCS());
      assertFalse("PurchasablePermission.isAPS() should return false" , pp.isAPS());
      
  }
  
  /**
   * Test for <code>PurchasablePermissionFactory.createInternetSpecialOrderPurchasablePermission()</code>.
   */
  public void testCreateInternetSpecialOrderPurchasablePermission()
  {
      PurchasablePermission pp = PurchasablePermissionFactory.createInternetSpecialOrderPurchasablePermission();
      
      assertNotNull( "Expected to find an instance of PurchasablePermission" , pp );
      assertFalse("PurchasablePermission.isAcademic() should return FALSE" , pp.isAcademic());
      assertTrue("PurchasablePermission.isSpecialOrder() should return TRUE" , pp.isSpecialOrder());
      assertTrue("PurchasablePermission.isSpecialOrderFromScratch() should return TRUE" , pp.isSpecialOrderFromScratch());
      assertFalse("PurchasablePermission.isEmail() should return FALSE" , pp.isEmail());
      assertFalse("PurchasablePermission.isPhotocopy() should return FALSE" , pp.isPhotocopy());
      assertFalse("PurchasablePermission.isRepublication() should return FALSE" , pp.isRepublication());
      assertTrue("PurchasablePermission.isNet() should return TRUE" , pp.isNet());
      assertTrue("PurchasablePermission.isInternet() should return TRUE" , pp.isInternet());
      assertFalse("PurchasablePermission.isIntranet() should return FALSE" , pp.isIntranet());
      assertFalse("PurchasablePermission.isExtranet() should return FALSE" , pp.isExtranet());
      assertTrue("PurchasablePermission.isDigital() should return TRUE" , pp.isDigital());
      assertFalse("PurchasablePermission.isECCS() should return FALSE" , pp.isECCS());
      assertFalse("PurchasablePermission.isAPS() should return false" , pp.isAPS());
      
  }
  
  /**
   * Test for <code>PurchasablePermissionFactory.createIntranetSpecialOrderPurchasablePermission()</code>.
   */
  public void testCreateIntranetSpecialOrderPurchasablePermission()
  {
      PurchasablePermission pp = PurchasablePermissionFactory.createIntranetSpecialOrderPurchasablePermission();
      
      assertNotNull( "Expected to find an instance of PurchasablePermission" , pp );
      assertFalse("PurchasablePermission.isAcademic() should return FALSE" , pp.isAcademic());
      assertTrue("PurchasablePermission.isSpecialOrder() should return TRUE" , pp.isSpecialOrder());
      assertTrue("PurchasablePermission.isSpecialOrderFromScratch() should return TRUE" , pp.isSpecialOrderFromScratch());
      assertFalse("PurchasablePermission.isEmail() should return FALSE" , pp.isEmail());
      assertFalse("PurchasablePermission.isPhotocopy() should return FALSE" , pp.isPhotocopy());
      assertFalse("PurchasablePermission.isRepublication() should return FALSE" , pp.isRepublication());
      assertTrue("PurchasablePermission.isNet() should return TRUE" , pp.isNet());
      assertFalse("PurchasablePermission.isInternet() should return FALSE" , pp.isInternet());
      assertTrue("PurchasablePermission.isIntranet() should return TRUE" , pp.isIntranet());
      assertFalse("PurchasablePermission.isExtranet() should return FALSE" , pp.isExtranet());
      assertTrue("PurchasablePermission.isDigital() should return TRUE" , pp.isDigital());
      assertFalse("PurchasablePermission.isECCS() should return FALSE" , pp.isECCS());
      assertFalse("PurchasablePermission.isAPS() should return false" , pp.isAPS());
      
  }
  
  /**
   * Test for <code>PurchasablePermissionFactory.createExtranetSpecialOrderPurchasablePermission()</code>.
   */
  public void testCreateExtranetSpecialOrderPurchasablePermission()
  {
      PurchasablePermission pp = PurchasablePermissionFactory.createExtranetSpecialOrderPurchasablePermission();
      
      assertNotNull( "Expected to find an instance of PurchasablePermission" , pp );
      assertFalse("PurchasablePermission.isAcademic() should return FALSE" , pp.isAcademic());
      assertTrue("PurchasablePermission.isSpecialOrder() should return TRUE" , pp.isSpecialOrder());
      assertTrue("PurchasablePermission.isSpecialOrderFromScratch() should return TRUE" , pp.isSpecialOrderFromScratch());
      assertFalse("PurchasablePermission.isEmail() should return FALSE" , pp.isEmail());
      assertFalse("PurchasablePermission.isPhotocopy() should return FALSE" , pp.isPhotocopy());
      assertFalse("PurchasablePermission.isRepublication() should return FALSE" , pp.isRepublication());
      assertTrue("PurchasablePermission.isNet() should return TRUE" , pp.isNet());
      assertFalse("PurchasablePermission.isInternet() should return FALSE" , pp.isInternet());
      assertFalse("PurchasablePermission.isIntranet() should return FALSE" , pp.isIntranet());
      assertTrue("PurchasablePermission.isExtranet() should return TRUE" , pp.isExtranet());
      assertTrue("PurchasablePermission.isDigital() should return TRUE" , pp.isDigital());
      assertFalse("PurchasablePermission.isECCS() should return FALSE" , pp.isECCS());
      assertFalse("PurchasablePermission.isAPS() should return false" , pp.isAPS());
      
  }
  
  /**
   * Test for <code>PurchasablePermissionFactory.createAPSSpecialOrderPurchasablePermission()</code>.
   */
  public void testCreateAPSSpecialOrderPurchasablePermission()
  {
    
    PurchasablePermission pp = PurchasablePermissionFactory.createAPSSpecialOrderPurchasablePermission();
    
    assertNotNull( "Expected to find an instance of PurchasablePermission" , pp );
    assertTrue("PurchasablePermission.isAcademic() should return TRUE" , pp.isAcademic());
    assertTrue("PurchasablePermission.isSpecialOrder() should return TRUE" , pp.isSpecialOrder());
    assertTrue("PurchasablePermission.isSpecialOrderFromScratch() should return TRUE" , pp.isSpecialOrderFromScratch());
    assertFalse("PurchasablePermission.isEmail() should return FALSE" , pp.isEmail());
    assertFalse("PurchasablePermission.isPhotocopy() should return FALSE" , pp.isPhotocopy());
    assertFalse("PurchasablePermission.isRepublication() should return FALSE" , pp.isRepublication());
    assertFalse("PurchasablePermission.isNet() should return FALSE" , pp.isNet());
    assertFalse("PurchasablePermission.isInternet() should return FALSE" , pp.isInternet());
    assertFalse("PurchasablePermission.isIntranet() should return FALSE" , pp.isIntranet());
    assertFalse("PurchasablePermission.isExtranet() should return FALSE" , pp.isExtranet());
    assertFalse("PurchasablePermission.isDigital() should return FALSE" , pp.isDigital());
    assertFalse("PurchasablePermission.isECCS() should return FALSE" , pp.isECCS());
    assertTrue("PurchasablePermission.isAPS() should return TRUE" , pp.isAPS());
  }
  
  /**
   * Test for <code>PurchasablePermissionFactory.createECCSSpecialOrderPurchasablePermission()</code>.
   */
  public void testCreateECCSSpecialOrderPurchasablePermission()
  {
  
    PurchasablePermission pp = PurchasablePermissionFactory.createECCSSpecialOrderPurchasablePermission();
  
    assertNotNull( "Expected to find an instance of PurchasablePermission" , pp );
    assertTrue("PurchasablePermission.isAcademic() should return TRUE" , pp.isAcademic());
    assertTrue("PurchasablePermission.isSpecialOrder() should return TRUE" , pp.isSpecialOrder());
    assertTrue("PurchasablePermission.isSpecialOrderFromScratch() should return TRUE" , pp.isSpecialOrderFromScratch());
    assertFalse("PurchasablePermission.isEmail() should return FALSE" , pp.isEmail());
    assertFalse("PurchasablePermission.isPhotocopy() should return FALSE" , pp.isPhotocopy());
    assertFalse("PurchasablePermission.isRepublication() should return FALSE" , pp.isRepublication());
    assertFalse("PurchasablePermission.isNet() should return FALSE" , pp.isNet());
    assertFalse("PurchasablePermission.isInternet() should return FALSE" , pp.isInternet());
    assertFalse("PurchasablePermission.isIntranet() should return FALSE" , pp.isIntranet());
    assertFalse("PurchasablePermission.isExtranet() should return FALSE" , pp.isExtranet());
    assertFalse("PurchasablePermission.isDigital() should return FALSE" , pp.isDigital());
    assertTrue("PurchasablePermission.isECCS() should return TRUE" , pp.isECCS());
    assertFalse("PurchasablePermission.isAPS() should return FALSE" , pp.isAPS());
  }
  
  /**
   * Test for <code>PurchasablePermissionFactory.createPurchasablePermission(PermissionRequest)</code>.
   */
  public void testCreatePublicationPermissionFromPermissionRequest()
  {
    PermissionRequest pr = getEmailPermissionRequest();
    
    PurchasablePermission pp = PurchasablePermissionFactory.createPurchasablePermission( pr );
    
    assertNotNull( "Expected to find an instance of PurchasablePermission" , pp );
    assertFalse("PurchasablePermission.isAcademic() should return FALSE" , pp.isAcademic());
    assertFalse("PurchasablePermission.isSpecialOrder() should return FALSE" , pp.isSpecialOrder());
    assertTrue("PurchasablePermission.isEmail() should return TRUE" , pp.isEmail());
    assertFalse("PurchasablePermission.isPhotocopy() should return FALSE" , pp.isPhotocopy());
    assertFalse("PurchasablePermission.isRepublication() should return FALSE" , pp.isRepublication());
    assertFalse("PurchasablePermission.isNet() should return FALSE" , pp.isNet());
    assertFalse("PurchasablePermission.isInternet() should return FALSE" , pp.isInternet());
    assertFalse("PurchasablePermission.isIntranet() should return FALSE" , pp.isIntranet());
    assertFalse("PurchasablePermission.isExtranet() should return FALSE" , pp.isExtranet());
    assertTrue("PurchasablePermission.isDigital() should return TRUE" , pp.isDigital());
    assertFalse("PurchasablePermission.isECCS() should return FALSE" , pp.isECCS());
    assertFalse("PurchasablePermission.isAPS() should return FALSE" , pp.isAPS());
    
    assertEquals( pp.getPrice(), "$ 123.00" );
    assertEquals( pp.getCustomerReference(), "This is a customer ref" );
    assertEquals( pp.getPublicationTitle(), "Main title" );
    assertEquals( pp.getAuthor(), "Author" );
    assertEquals( pp.getStandardNumber(), "123-456" );
    assertEquals( pp.getPublisher(), "Publisher" );
    assertEquals( pp.getNumberOfRecipients(), new Long(123).intValue() );
    
    
  }
  
  /**
   * Test for <code>PurchasablePermissionFactory.createPurchasablePermission(PublicationPermission)</code>.
   */
  public void testCreatePurchasablePermissionFromPublicationPermission()
  {    
     PublicationPermission publicationPermission = MockPublicationPermissionServices.createPublicationPermissionForUnitTest( "email", "american hospital formulary service a collection of drug monographs" , false );
     
     assertNotNull("PublicationPermission should not be null", publicationPermission );
     
     PurchasablePermission purchasablePermission = PurchasablePermissionFactory.createPurchasablePermission(publicationPermission);

     assertNotNull("PurchasablePermission should not be null", purchasablePermission );
          
     assertEquals( purchasablePermission.getPublicationTitle(), publicationPermission.getPublication().getMainTitle() );
     assertTrue( purchasablePermission.isEmail() );
     assertEquals( purchasablePermission.getEditor(), publicationPermission.getPublication().getMainEditor() );
     assertEquals( purchasablePermission.getAuthor(), publicationPermission.getPublication().getMainAuthor() );
     assertEquals( purchasablePermission.getPublisher(), publicationPermission.getPublication().getMainPublisher() );
     assertEquals( purchasablePermission.getWorkInst(), Long.valueOf(publicationPermission.getPublication().getWrkInst()) );
     assertEquals( purchasablePermission.getVolume(), publicationPermission.getPublication().getVolume() );
     assertEquals( purchasablePermission.getPermissionSelected(), publicationPermission.getTypeOfUse() );
     assertEquals( purchasablePermission.getRightsholder(), publicationPermission.getRightsholderName() );
     assertEquals( purchasablePermission.getPermissionRequest().getRgtInst(), publicationPermission.getRgtInst() );
     assertEquals( purchasablePermission.getPermissionRequest().getUsageData().getProduct(), publicationPermission.getPrdInst() );
     assertEquals( purchasablePermission.getPermissionRequest().getUsageData().getTpuInst(), publicationPermission.getTpuInst() );
     
  }
  
  public void testCreateRepublicationPurchasablePermissionFromPublicationPermission()
  {
        
    PublicationPermission publicationPermission = MockPublicationPermissionServices.createPublicationPermissionForUnitTest( "republication", "" , false );
    
    assertNotNull("PublicationPermission should not be null", publicationPermission );
    
    PurchasablePermission purchasablePermission = PurchasablePermissionFactory.createPurchasablePermission(publicationPermission);

    assertNotNull("PurchasablePermission should not be null", purchasablePermission );
    
    assertTrue( purchasablePermission.isRepublication() );
    assertFalse( purchasablePermission.isAcademic() );
    assertFalse( purchasablePermission.isAPS() );
    assertFalse( purchasablePermission.isECCS() );
    assertFalse( purchasablePermission.isDigital() );
    assertFalse( purchasablePermission.isEmail());
    assertFalse( purchasablePermission.isPhotocopy() );
    assertFalse( purchasablePermission.isInternet() );
    assertFalse( purchasablePermission.isIntranet() );
    assertFalse( purchasablePermission.isExtranet() );
    
    assertTrue( StringUtils.isNotEmpty( purchasablePermission.getRepublicationTypeOfUse() ) );
    
    assertTrue( purchasablePermission.getNumberOfPages() == 0  );
    purchasablePermission.setNumberOfPages( 2 );
    assertTrue( purchasablePermission.getTypeOfContent().equals( RepublicationConstants.CONTENT_SELECTED_PAGES ) );
    assertTrue( purchasablePermission.getNumberOfPages() == 2  );
    
    assertNull( purchasablePermission.getChapterArticle() );
    purchasablePermission.setChapterArticle("my_chapter_article");
    assertTrue( purchasablePermission.getChapterArticle().equals("my_chapter_article") );
    
    assertTrue( purchasablePermission.getCirculationDistribution() == 0 );
    purchasablePermission.setCirculationDistribution( 12345 );
    assertTrue( purchasablePermission.getCirculationDistribution() == 12345 );
        
    assertTrue( purchasablePermission.getBusiness().equals( RepublicationConstants.BUSINESS_FOR_PROFIT ) );
    purchasablePermission.setBusiness( RepublicationConstants.BUSINESS_NON_FOR_PROFIT  );
    assertTrue( purchasablePermission.getBusiness().equals( RepublicationConstants.BUSINESS_NON_FOR_PROFIT ) );
    
    assertTrue( purchasablePermission.getTypeOfContent().equals( RepublicationConstants.CONTENT_SELECTED_PAGES ) );
    purchasablePermission.setTypeOfContent( RepublicationConstants.CONTENT_CHART );
    assertTrue( purchasablePermission.getTypeOfContent().equals( RepublicationConstants.CONTENT_CHART ) );
    purchasablePermission.setTypeOfContent( RepublicationConstants.CONTENT_EXCERPT );
    assertTrue( purchasablePermission.getTypeOfContent().equals( RepublicationConstants.CONTENT_EXCERPT ) );
    purchasablePermission.setTypeOfContent( RepublicationConstants.CONTENT_FIGURE_DIAGRAM_TABLE );
    assertTrue( purchasablePermission.getTypeOfContent().equals( RepublicationConstants.CONTENT_FIGURE_DIAGRAM_TABLE ) );
    purchasablePermission.setTypeOfContent( RepublicationConstants.CONTENT_FULL_ARTICLE_CHAPTER );
    assertTrue( purchasablePermission.getTypeOfContent().equals( RepublicationConstants.CONTENT_FULL_ARTICLE_CHAPTER ) );
    purchasablePermission.setTypeOfContent( RepublicationConstants.CONTENT_GRAPH );
    assertTrue( purchasablePermission.getTypeOfContent().equals( RepublicationConstants.CONTENT_GRAPH ) );
    purchasablePermission.setTypeOfContent( RepublicationConstants.CONTENT_ILLUSTRATION );
    assertTrue( purchasablePermission.getTypeOfContent().equals( RepublicationConstants.CONTENT_ILLUSTRATION ) );
    purchasablePermission.setTypeOfContent( RepublicationConstants.CONTENT_PHOTOGRAPH );
    assertTrue( purchasablePermission.getTypeOfContent().equals( RepublicationConstants.CONTENT_PHOTOGRAPH ) );
    purchasablePermission.setTypeOfContent( RepublicationConstants.CONTENT_QUOTATION );
    assertTrue( purchasablePermission.getTypeOfContent().equals( RepublicationConstants.CONTENT_QUOTATION ) );
    
    purchasablePermission.setSubmitterAuthor( false );
    assertFalse( purchasablePermission.isSubmitterAuthor() );
    
    Date contentsPublicationDate = new Date();
    purchasablePermission.setContentsPublicationDate( contentsPublicationDate );    
    assertEquals( purchasablePermission.getContentsPublicationDate(), contentsPublicationDate );

    String republishingOrganization = "the republishing organization";
    purchasablePermission.setRepublishingOrganization( republishingOrganization );
    assertTrue( purchasablePermission.getRepublishingOrganization().equalsIgnoreCase( republishingOrganization ) );
    
    String newPublicationTitle = "new title";
    purchasablePermission.setNewPublicationTitle( newPublicationTitle );
    assertTrue( purchasablePermission.getNewPublicationTitle().equalsIgnoreCase( newPublicationTitle ) );
    
    Date republicationDate = new Date();
    purchasablePermission.setRepublicationDate( republicationDate );
    assertEquals( purchasablePermission.getRepublicationDate(), republicationDate );
    
    assertTrue( purchasablePermission.getTranslationLanguage().equals( RepublicationConstants.NO_TRANSLATION ) );
    purchasablePermission.setTranslationLanguage( RepublicationConstants.TRANSLATED_LANGUAGE_FRENCH );
    assertTrue( purchasablePermission.getTranslationLanguage().equals( RepublicationConstants.TRANSLATED_LANGUAGE_FRENCH ) );
    purchasablePermission.setTranslationLanguage( RepublicationConstants.NO_TRANSLATION );
    assertEquals( purchasablePermission.getTranslationLanguage(), RepublicationConstants.NO_TRANSLATION );
    purchasablePermission.setTranslationLanguage( RepublicationConstants.TRANSLATED_LANGUAGE_OTHER );
    assertEquals( purchasablePermission.getTranslationLanguage(), RepublicationConstants.TRANSLATED_LANGUAGE_OTHER );
    
    String yourReference = "this is your reference";
    purchasablePermission.setYourReference( yourReference );
    assertTrue( purchasablePermission.getYourReference().equalsIgnoreCase( yourReference ) );
    
    String customAuthor = "Author, Custom";
    purchasablePermission.setCustomAuthor( customAuthor );
    assertEquals( purchasablePermission.getCustomAuthor(), customAuthor );
  }
  
  
   /**
    * Test for <code>PurchasablePermissionFactory.createPurchasablePermission(OrderLicense)</code>.
    */
   public void testCreatePublicationPermissionFromOrderLicense()
   {
      //TODO 12/03/2006 lalberione: Use real classes when available.
      
      
      
//      MockPublicationPermission publicationPermission = MockPublicationPermissionFactory.createPublicationPermission("trs", false);
//      OrderLicense ol = MockOrderLicenseFactory.createOrderLicense( publicationPermission );
//      
//      PurchasablePermission pp = PurchasablePermissionFactory.createPurchasablePermission(ol);
//      
//      assertEquals( pp.getAuthor(), ol.getAuthor() );
      
      
   }
  
  private PermissionRequest getEmailPermissionRequest()
  {
    int rgtInst = 123456;
    int moneyAmount = 123;
    String customerRef = "This is a customer ref";
    String title = "Main title";
    String author = "Author";
    String stdnum = "123-456";
    String publisher = "Publisher";
    Long recipients = new Long(123);

    PermissionRequest pr = new PermissionRequest(rgtInst);
    
    //pr.setManualSpecialOrder(false);
    pr.setPrice( new Money(moneyAmount) );    
    pr.setCustomerRef(customerRef);
    
    Work work = new StandardWork();
    
    work.setMainTitle(title);
    work.setMainAuthor(author);    
    work.setStandardNumber(stdnum);
    work.setPublisher(publisher);
    
    pr.setWork(work);
    
    UsageDataEmail usageDataEmail = new UsageDataEmail();
    
    usageDataEmail.setTpuInst(ECommerceConstants.EMAIL_TPU_CODE);
    usageDataEmail.setProduct(ECommerceConstants.DPS_PRODUCT_CODE);
    usageDataEmail.setDateOfUse(new Date());
    usageDataEmail.setPublicationDate(new Date());
    usageDataEmail.setNumRecipients(recipients);
    
    pr.setUsageData( usageDataEmail );
    
    return pr;
  }
  
}
