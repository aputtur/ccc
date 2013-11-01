package com.copyright.ccc.business.services.adjustment;

import com.copyright.ccc.CCTestCase;
import com.copyright.ccc.CCTestSetup;

import com.copyright.ccc.business.data.OrderLicense;
import com.copyright.ccc.business.security.UnitTestLoginHelper;

import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.ecommerce.PurchasablePermission;
import com.copyright.ccc.business.services.order.OrderLicensesException;

import com.copyright.ccc.web.transaction.RepublicationConstants;
import com.copyright.data.order.UsageDataNet;

import java.io.FileInputStream;
import java.io.File;

import java.util.Iterator;

import java.util.Map;

import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Contains unit tests for <code>AdjustmentServices</code>.
 */
public class AdjustmentServicesTest extends CCTestCase 
{
  private static final int NOT_ADJUSTED_QUANTITY = 0;
  private static final Properties ADJUSTMENT_TEST_RESOURCES = new Properties();
      
  private OrderAdjustment _adjustment = null;
  private String _invoiceNumber;
  private String _purchaseNumber;
  private String _detailNumber;


  public static Test suite()
  {
      TestSuite suite = new TestSuite( AdjustmentServicesTest.class );
      return new CCTestSetup( suite );       
  }
  
  
  @Override
  protected void setUp() throws Exception
  {
    ADJUSTMENT_TEST_RESOURCES.load( new FileInputStream( new File( "..\\..\\src\\com\\copyright\\ccc\\business\\services\\adjustment\\AdjustmentTestResources.properties" ) ) );
    
    _invoiceNumber = getTestResource( "adj.test.invoice.number" );
    _purchaseNumber = getTestResource( "adj.test.purchase.number" );
    _detailNumber = getTestResource( "adj.test.detail.number" );
    
    UnitTestLoginHelper.login();    
  }
  
  
  public void testCreateDetailAdjustment() throws OrderLicensesException
  {
    _adjustment = AdjustmentServices.createNewDetailAdjustment( _detailNumber );
    assertNotNull("_adjustment shouldn't be null", _adjustment);
    assertTrue("_adjustment should be a detail adjustment", _adjustment.isDetailAdjustment());
    assertFalse("_adjustment shouldn't be an invoice adjustment", _adjustment.isInvoiceAdjustment());
    assertFalse("_adjustment shouldn't be a purchase adjustment", _adjustment.isPurchaseAdjustment());
    
    int numberOfBodyItems = _adjustment.getBody().size();
    assertEquals("Detail adjustments should have only one bodyItem, not: " + numberOfBodyItems, 1, numberOfBodyItems);
  }
  
  
  public void testCreatePurchaseAdjustment() throws OrderLicensesException
  {
    _adjustment = AdjustmentServices.createNewPurchaseAdjustment( _purchaseNumber );
    assertNotNull("_adjustment shouldn't be null", _adjustment);
    assertTrue("_adjustment should be a purchase adjustment", _adjustment.isPurchaseAdjustment());
    assertFalse("_adjustment shouldn't be an invoice adjustment", _adjustment.isInvoiceAdjustment());
    assertFalse("_adjustment shouldn't be a detail adjustment", _adjustment.isDetailAdjustment());
    
    assertFalse("An adjustment should have at least one bodyItem",  _adjustment.getBody().isEmpty() );
  }
  
  
  public void testCreateInvoiceAdjustment() throws OrderLicensesException
  {
    _adjustment = AdjustmentServices.createNewInvoiceAdjustment( _invoiceNumber );
    assertNotNull("_adjustment shouldn't be null", _adjustment);
    assertTrue("_adjustment should be an invoice adjustment", _adjustment.isInvoiceAdjustment());
    assertFalse("_adjustment shouldn't be a purchase adjustment", _adjustment.isPurchaseAdjustment());
    assertFalse("_adjustment shouldn't be a detail adjustment", _adjustment.isDetailAdjustment());
    assertFalse("An adjustment should have at least one bodyItem",  _adjustment.getBody().isEmpty() );
  }
  
  
  public void testSaveAndRetrieveAdjustment() throws OrderLicensesException
  {
    _detailNumber = getTestResource("adj.test.save.detail.number");
    _adjustment = AdjustmentServices.createNewDetailAdjustment( _detailNumber );
    long userPartyID = UserContextService.getSharedUser().getPartyId().longValue();
    
    int numSavedAdjustmentsBeforeSave = AdjustmentServices.getAdjustmentDescriptorsForUser( userPartyID ).size();
    
    long adjustedQty = performSimpleAdjustmentToFirstAdjustableBodyItem( _adjustment );
    assertFalse( "Adjustment should have been performed" , NOT_ADJUSTED_QUANTITY == adjustedQty);
    
    AdjustmentServices.saveAdjustment( _adjustment );
    
    Map<String,OrderAdjustmentDescriptor> adjustmentDescriptors = AdjustmentServices.getAdjustmentDescriptorsForUser( userPartyID );
    assertFalse( "adjustmentDescriptors shouldn't be empty", adjustmentDescriptors.isEmpty() );
    
    //We can add the following assertion to be more exhaustive
    assertEquals("adjustmentDescriptors' size should be " + (numSavedAdjustmentsBeforeSave + 1) + ", not " + adjustmentDescriptors.size() + ". " , adjustmentDescriptors.size(), (numSavedAdjustmentsBeforeSave + 1) );
    
    OrderAdjustmentDescriptor adjustmentDescriptor = adjustmentDescriptors.get( String.valueOf( _adjustment.getCartID() ) );
    assertNotNull( "adjustmentDescriptor shouldn't be null", adjustmentDescriptor );
    
    OrderAdjustment _savedAdjustment = AdjustmentServices.retrieveAdjustment(adjustmentDescriptor);
    assertNotNull( "_savedAdjustment shouldn't be null", _savedAdjustment );
    assertTrue( "_savedAdjustment should be a detail adjustment" , _savedAdjustment.isDetailAdjustment());
    assertEquals( "_adjustment and _savedAdjustment should have the same sourceID", _adjustment.getSourceID(), _savedAdjustment.getSourceID() );
  }

  
  public void testDeleteSavedAdjustment() throws OrderLicensesException
  {
    _detailNumber = getTestResource("adj.test.save.detail.number");
    _adjustment = AdjustmentServices.createNewDetailAdjustment( _detailNumber );
    long adjustedQty = performSimpleAdjustmentToFirstAdjustableBodyItem( _adjustment );
    assertFalse( "Adjustment should have been performed" , NOT_ADJUSTED_QUANTITY == adjustedQty);
    
    AdjustmentServices.saveAdjustment( _adjustment );
    
    
    long userPartyID = UserContextService.getSharedUser().getPartyId().longValue();
    Map<String,OrderAdjustmentDescriptor> adjustmentDescriptors = AdjustmentServices.getAdjustmentDescriptorsForUser( userPartyID );
    assertFalse( "adjustmentDescriptors shouldn't be empty", adjustmentDescriptors.isEmpty() );
    
    OrderAdjustmentDescriptor descriptorForDelete = adjustmentDescriptors.get( String.valueOf( _adjustment.getCartID() ) );
    assertNotNull("descriptorForDelete shouldn't be null", descriptorForDelete);
    
    AdjustmentServices.deleteSavedAdjustment(descriptorForDelete);
    Map<String,OrderAdjustmentDescriptor> adjustmentDescriptorsAfterDelete = AdjustmentServices.getAdjustmentDescriptorsForUser( userPartyID );
    OrderAdjustmentDescriptor deletedDescriptor = adjustmentDescriptorsAfterDelete.get( _adjustment.getSourceID() );
    
    assertNull("deletedDescriptor should have been deleted", deletedDescriptor);
    
    //We can add the following assertion to be more exhaustive
        assertEquals("adjustmentDescriptorsAfterDelete's size should be equal to " + (adjustmentDescriptors.size() - 1) + ", not to " + adjustmentDescriptorsAfterDelete.size() + ". ",
                adjustmentDescriptorsAfterDelete.size(),
                adjustmentDescriptors.size() - 1);
  }
  
  
  public void testCommitAdjustment() throws OrderLicensesException
  {
    _adjustment = AdjustmentServices.createNewPurchaseAdjustment( _purchaseNumber );
    long adjustedQty = performSimpleAdjustmentToFirstAdjustableBodyItem( _adjustment );
    assertFalse( "Adjustment should have been performed" , NOT_ADJUSTED_QUANTITY == adjustedQty);
    
    
    long confirmationNumber = AdjustmentServices.commitAdjustment( _adjustment );
    assertTrue("Invalid confirmation number received after commiting adjustment: " + confirmationNumber + ". " , confirmationNumber > -1 );
  }


  //////////////////////////////////////////
  //Private Methods
  //////////////////////////////////////////
  
  private String getTestResource( String resourceKey )
  {
    return ADJUSTMENT_TEST_RESOURCES.getProperty( resourceKey );
  }

  private long performSimpleAdjustmentToFirstAdjustableBodyItem( OrderAdjustment adjustment )
  {
    OrderAdjustmentBodyItem adjustableBodyItem = null;    
    Iterator<Map.Entry<String,OrderAdjustmentBodyItem>> iterator = 
    	adjustment.getBody().entrySet().iterator();
    
    while(iterator.hasNext())
    {
      OrderAdjustmentBodyItem currentBodyItem = iterator.next().getValue();
      
      if( currentBodyItem.isAdjustable() )
      {
        adjustableBodyItem = currentBodyItem;
        break;
      }
    }
    
    assertNotNull("adjustableBodyItem shouldn't be null. There should be at least one adjustable body item. ", adjustableBodyItem);
    
    OrderLicense originalLicense = adjustableBodyItem.getOriginalOrderDetails();
    PurchasablePermission currentAdjustment = adjustableBodyItem.getCurrentAdjustmentsDetails();

    long adjustedQuantity = NOT_ADJUSTED_QUANTITY;

    if ( currentAdjustment.isPhotocopy() )
    {
      adjustedQuantity = 0 - originalLicense.getNumberOfCopies();
      currentAdjustment.setNumberOfCopies(adjustedQuantity);
    }
    
    if( currentAdjustment.isEmail() )
    {
      adjustedQuantity = 0 - originalLicense.getNumberOfRecipients();
      currentAdjustment.setNumberOfRecipients(adjustedQuantity);
    }
    
    if( currentAdjustment.isNet() )
    {
      adjustedQuantity = UsageDataNet.TO_30_DAYS_FEE;
      currentAdjustment.setDuration((int)adjustedQuantity);
    }
    
    if( currentAdjustment.isAcademic() )
    {
      adjustedQuantity = 0 - originalLicense.getNumberOfStudents();
      currentAdjustment.setNumberOfStudents((int)adjustedQuantity);
    }
    
    if( currentAdjustment.isRepublication() )
    {
      if(currentAdjustment.getTypeOfContent() == RepublicationConstants.CONTENT_CARTOONS)
      {
        adjustedQuantity = 0 - originalLicense.getNumberOfCartoons();
        currentAdjustment.setNumberOfCartoons(adjustedQuantity);
      }
      
      if(currentAdjustment.getTypeOfContent() == RepublicationConstants.CONTENT_CHART)
      {
        adjustedQuantity = 0 - originalLicense.getNumberOfCharts();
        currentAdjustment.setNumberOfCharts(adjustedQuantity);
      }
      
      if(currentAdjustment.getTypeOfContent() == RepublicationConstants.CONTENT_EXCERPT)
      {
        adjustedQuantity = 0 - originalLicense.getNumberOfExcerpts();
        currentAdjustment.setNumberOfExcerpts(adjustedQuantity);
      }
      
      if(currentAdjustment.getTypeOfContent() == RepublicationConstants.CONTENT_FIGURE_DIAGRAM_TABLE)
      {
        adjustedQuantity = 0 - originalLicense.getNumberOfFigures();
        currentAdjustment.setNumberOfFigures(adjustedQuantity);
      }
      
      if(currentAdjustment.getTypeOfContent() == RepublicationConstants.CONTENT_GRAPH)
      {
        adjustedQuantity = 0 - originalLicense.getNumberOfGraphs();
        currentAdjustment.setNumberOfGraphs(adjustedQuantity);
      }
      
      if(currentAdjustment.getTypeOfContent() == RepublicationConstants.CONTENT_ILLUSTRATION)
      {
        adjustedQuantity = 0 - originalLicense.getNumberOfIllustrations();
        currentAdjustment.setNumberOfIllustrations(adjustedQuantity);
      }
      
      if(currentAdjustment.getTypeOfContent() == RepublicationConstants.CONTENT_PHOTOGRAPH)
      {
        adjustedQuantity = 0 - originalLicense.getNumberOfPhotos();
        currentAdjustment.setNumberOfPhotos(adjustedQuantity);
      }
      
      if(currentAdjustment.getTypeOfContent() == RepublicationConstants.CONTENT_QUOTATION)
      {
        adjustedQuantity = 0 - originalLicense.getNumberOfQuotes();
        currentAdjustment.setNumberOfQuotes(adjustedQuantity);
      }
      
      if(currentAdjustment.getTypeOfContent() == RepublicationConstants.CONTENT_SELECTED_PAGES)
      {
        adjustedQuantity = 0 - originalLicense.getNumberOfPages();
        currentAdjustment.setNumberOfPages(adjustedQuantity);
      }
    }
    
    assertTrue("Current adjustment should have been modified", adjustableBodyItem.isCurrentAdjustmentModified() );
        
    return adjustedQuantity;
  }

}
