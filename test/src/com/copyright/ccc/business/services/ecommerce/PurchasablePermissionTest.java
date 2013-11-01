package com.copyright.ccc.business.services.ecommerce;

import com.copyright.ccc.CCTestCase;

import java.util.Date;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Contains unit tests for <code>PurchasablePermission</code> implementation.
 */
public class PurchasablePermissionTest extends CCTestCase
{
  private PurchasablePermission _pp = PurchasablePermissionFactory.createEmailSpecialOrderPurchasablePermission();
  
  /**
   * Returns the suite of all tests in this class.
   */
  public static Test suite()
  {
      return new TestSuite( PurchasablePermissionTest.class );
  }
  
  
  /**
   * Test for for publication data consistency in <code>PurchasablePermission</code> implementation.
   */
  public void testPublicationDataConsistency()
  {   
    
    String publicationTitle = "MY PUBLICATION";
    String standardNumber = "0123-4567-ABC";
    String publisher = "Klauss Van Der Publisher";
    String publicationYear = "2006";
    String rightsholder = "Boris Rightsholder";
    String rightsQualifyingStmt = "Images not to be used";
    String author = "Joe Author";
    String editor = "Moe Editor";
    String volume = "Volume III";
    String edition = "1st";
    
    
    _pp.setPublicationTitle( publicationTitle );
    assertEquals("Publication title should be: " + publicationTitle, _pp.getPublicationTitle(), publicationTitle);
    
    _pp.setStandardNumber(standardNumber);
    assertEquals("Std number should be: " + publicationTitle, _pp.getStandardNumber(), standardNumber);
    
    _pp.setPublisher(publisher);
    assertEquals("Publisher should be: " + publisher, _pp.getPublisher(), publisher);
    
    _pp.setPublicationYear(publicationYear);
    assertEquals("Publication Year should be: " + publicationYear, _pp.getPublicationYear(), publicationYear);

    //TODO 11/01/2006 lalberione: test rightsholder data when available;
    
    //TODO 11/01/2006 lalberione: test rights qualifying statement data when available;
    
    _pp.setAuthor(author);
    assertEquals("Author should be: " + author, _pp.getAuthor(), author);
    
    _pp.setEditor(editor);
    assertEquals("Editor should be: " + editor, _pp.getEditor(), editor);
    
    _pp.setVolume(volume);
    assertEquals("Volume should be: " + volume, _pp.getVolume(), volume);
    
    _pp.setEdition(edition);
    assertEquals("Edition should be: " + edition, _pp.getEdition(), edition);
    
  }
  
  /**
   * Test for extra common data consistency in <code>PurchasablePermission</code> implementation.
   */
  public void testExtraCommonDataConsistency()
  {   
    String price = "$ 0.00";
    String permissionSelected = "This is the permission I've selected";
    String customerReference = "Just a reference";
    
    assertEquals("Edition should be: " + price, _pp.getPrice(), price);
    
    _pp.setPermissionSelected(permissionSelected);
    assertEquals("Permission Selected should be: " + permissionSelected, _pp.getPermissionSelected(), permissionSelected);
    
    _pp.setCustomerReference(customerReference);
    assertEquals("Customer Reference should be: " + customerReference, _pp.getCustomerReference(), customerReference);
  }
  
  /**
   * Test for Internet type of use related data consistency in <code>PurchasablePermission</code> implementation.
   */
  public void testInternetDataConsistency()
  { 
    PurchasablePermission ppInternet = PurchasablePermissionFactory.createInternetSpecialOrderPurchasablePermission();
    PurchasablePermission ppPhotocopy = PurchasablePermissionFactory.createPhotocopySpecialOrderPurchasablePermission();
    
    String webAddress = "www.myfoowebadreess.com";
      
    ppInternet.setWebAddress(webAddress);
    assertEquals("Web Address should be: " + webAddress, ppInternet.getWebAddress(), webAddress);
    
    try
    {
      ppPhotocopy.setWebAddress(webAddress);
      fail("UnsupportedOperationException should have been thrown");
    }
    catch (UnsupportedOperationException e)
    {      
    } 
    
  }
  
  /**
   * Test for Photocopy type of use related data consistency in <code>PurchasablePermission</code> implementation.
   */
  public void testPhotocopyDataConsistency()
  {
    PurchasablePermission ppPhotocopy = PurchasablePermissionFactory.createPhotocopySpecialOrderPurchasablePermission();
    PurchasablePermission ppInternet = PurchasablePermissionFactory.createInternetSpecialOrderPurchasablePermission();
    PurchasablePermission ppEmail = PurchasablePermissionFactory.createEmailSpecialOrderPurchasablePermission();
    
    String publicationYearOfUse = "2006";
    int numberOfPages = 11;
    int numberOfCopies = 22;
    
    
    ppPhotocopy.setPublicationYearOfUse(publicationYearOfUse);
    assertEquals("PublicationYearOfUse should be: " + publicationYearOfUse, ppPhotocopy.getPublicationYearOfUse(), publicationYearOfUse);
    
    ppInternet.setPublicationYearOfUse(publicationYearOfUse);
    assertEquals("PublicationYearOfUse shouldn't be: " + ppInternet.getPublicationYearOfUse() + " it should be: " + publicationYearOfUse, ppInternet.getPublicationYearOfUse(), publicationYearOfUse);
    
    ppPhotocopy.setNumberOfPages(numberOfPages);
    assertTrue("NumberOfPages should be: " + numberOfPages, ppPhotocopy.getNumberOfPages() == numberOfPages);
    
    try
    {
      ppEmail.setNumberOfPages(numberOfPages);
      fail("UnsupportedOperationException should have been thrown");
    }
    catch (UnsupportedOperationException e)
    {      
    }    
    
    ppPhotocopy.setNumberOfCopies(numberOfCopies);
    assertTrue("NumberOfCopies should be: " + numberOfCopies, ppPhotocopy.getNumberOfCopies() == numberOfCopies);
    
    try
    {
      ppEmail.setNumberOfCopies(numberOfCopies);
      fail("UnsupportedOperationException should have been thrown");
    }
    catch (UnsupportedOperationException e)
    {      
    }
    
    try
    {
      ppInternet.setNumberOfCopies(numberOfCopies);
      fail("UnsupportedOperationException should have been thrown");
    }
    catch (UnsupportedOperationException e)
    {      
    }
    
  }
  
  /**
   * Test for Email type of use related data consistency in <code>PurchasablePermission</code> implementation.
   */
  public void testEmailDataConsistency()
  {
    PurchasablePermission ppEmail = PurchasablePermissionFactory.createEmailSpecialOrderPurchasablePermission();
    PurchasablePermission ppPhotocopy = PurchasablePermissionFactory.createPhotocopySpecialOrderPurchasablePermission();
    
    String publicationYearOfUse = "2006";
    int numberOfRecipients = 33;
    Date dateOfUse = new Date();
    
    ppEmail.setPublicationYearOfUse(publicationYearOfUse);
    assertEquals("PublicationYearOfUse should be: " + publicationYearOfUse, ppEmail.getPublicationYearOfUse(), publicationYearOfUse);
        
    
    ppEmail.setNumberOfRecipients(numberOfRecipients);
    assertEquals("NumberOfRecipients should be: " + numberOfRecipients, ppEmail.getNumberOfRecipients(), numberOfRecipients);
    
    try
    {
      ppPhotocopy.setNumberOfRecipients(numberOfRecipients);
      fail("UnsupportedOperationException should have been thrown");
    }
    catch (UnsupportedOperationException e)
    {      
    }
        
    ppEmail.setDateOfUse(dateOfUse);
    assertEquals("DateOfUse should be: " + dateOfUse.toString(), ppEmail.getDateOfUse(), dateOfUse);
    
    try
    {
      ppPhotocopy.setDateOfUse(dateOfUse);
      fail("UnsupportedOperationException should have been thrown");
    }
    catch (UnsupportedOperationException e)
    {
    }
    

  }
  
  /**
   * Test for Net type of use related data consistency in <code>PurchasablePermission</code> implementation.
   */
  public void testNetDataConsistency()
  {
    PurchasablePermission ppIntranet = PurchasablePermissionFactory.createIntranetSpecialOrderPurchasablePermission();
    PurchasablePermission ppExtranet = PurchasablePermissionFactory.createExtranetSpecialOrderPurchasablePermission();
    PurchasablePermission ppInternet = PurchasablePermissionFactory.createInternetSpecialOrderPurchasablePermission();
    PurchasablePermission ppPhotocopy = PurchasablePermissionFactory.createPhotocopySpecialOrderPurchasablePermission();
    
    String publicationYearOfUse = "2006";
    Date dateOfUse = new Date();
    int duration = 2;
    
    ppIntranet.setPublicationYearOfUse(publicationYearOfUse);
    assertEquals("PublicationYearOfUse should be: " + publicationYearOfUse, ppIntranet.getPublicationYearOfUse(), publicationYearOfUse);
    
    ppExtranet.setPublicationYearOfUse(publicationYearOfUse);
    assertEquals("PublicationYearOfUse should be: " + publicationYearOfUse, ppExtranet.getPublicationYearOfUse(), publicationYearOfUse);
    
    ppInternet.setPublicationYearOfUse(publicationYearOfUse);
    assertEquals("PublicationYearOfUse should be: " + publicationYearOfUse, ppInternet.getPublicationYearOfUse(), publicationYearOfUse);
    
    ppPhotocopy.setPublicationYearOfUse(publicationYearOfUse);
    assertEquals( "PublicationYearOfUse shouldn't be: " + ppPhotocopy.getPublicationYearOfUse() + " it should be: " + publicationYearOfUse, ppPhotocopy.getPublicationYearOfUse(), publicationYearOfUse );
    
    ppIntranet.setDateOfUse(dateOfUse);
    assertEquals("DateOfUse should be: " + dateOfUse.toString(), ppIntranet.getDateOfUse(), dateOfUse);
    
    ppExtranet.setDateOfUse(dateOfUse);
    assertEquals("DateOfUse should be: " + dateOfUse.toString(), ppExtranet.getDateOfUse(), dateOfUse);
    
    ppInternet.setDateOfUse(dateOfUse);
    assertEquals("DateOfUse should be: " + dateOfUse.toString(), ppInternet.getDateOfUse(), dateOfUse);
    
    try
    {
      ppPhotocopy.setDateOfUse(dateOfUse);
      fail("UnsupportedOperationException should have been thrown");
    }
    catch (UnsupportedOperationException e)
    {
    }
    
    ppIntranet.setDuration(duration);
    assertTrue("Duration should be: " + duration, ppIntranet.getDuration() == duration );
    
    ppExtranet.setDuration(duration);
    assertTrue("Duration should be: " + duration, ppExtranet.getDuration() == duration );
    
    ppInternet.setDuration(duration);
    assertTrue("Duration should be: " + duration, ppInternet.getDuration() == duration );
    
    try
    {
      ppPhotocopy.setDuration(duration);
      fail("UnsupportedOperationException should have been thrown");
    }
    catch (UnsupportedOperationException e)
    {
      
    }
  }
  
  /**
   * Test for Academic type of use related data consistency in <code>PurchasablePermission</code> implementation.
   */
  public void testAcademicDataConsistency()
  {
    //TODO 11/01/2006 lalberione: to be completed when UsageDataAcademic beomes available
  }
  
     /**
      * Test for Republication type of use related data consistency in <code>PurchasablePermission</code> implementation.
      */
     public void testRepublicationDataConsistency()
     {
       //TODO 11/01/2006 lalberione: to be completed when UsageDataRepublication beomes available
     }
}
