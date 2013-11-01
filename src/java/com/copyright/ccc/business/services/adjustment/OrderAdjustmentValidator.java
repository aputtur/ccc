package com.copyright.ccc.business.services.adjustment;

import java.util.Iterator;
import java.util.Map;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.copyright.ccc.business.services.ecommerce.PurchasablePermission;

/**
 * Class responsible for the validation of instances of <code>OrderAdjustment</code>.
 */
public class OrderAdjustmentValidator
{
  
  private static final long ZERO_LONG = 0L;
  private static double ZERO_DOUBLE = 0D;
  
  private OrderAdjustment _adjustment;

  private OrderAdjustmentValidator(){}

  public OrderAdjustmentValidator( OrderAdjustment adjustment )
  {
    
    boolean invalidAdjustment = adjustment == null ||
                                adjustment.getBody() == null ||
                                adjustment.getBody().isEmpty();
                                
    if( invalidAdjustment )
    {
      throw new IllegalArgumentException("Invalid OrderAdjustment provided to constructor");
    }
    
    setAdjustment( adjustment );
  }

  
  
  /**
   * Performs validation.
   */
  public ActionErrors validate()
  {
    ActionErrors validationErrors = new ActionErrors();

    ActionErrors quantitiesValidationErrors = validateQuantities();
    validationErrors.add( quantitiesValidationErrors );

    ActionErrors feesValidationErrors = validateFees();
    validationErrors.add( feesValidationErrors );
    
    return validationErrors;
  }
  
  
  
  /**
   * Returns the instance of <code>OrderAdjustment</code> being validated by this validator.
   */
  private OrderAdjustment getAdjustment()
  {
    return _adjustment;
  }

  
  private void setAdjustment(OrderAdjustment adjustment)
  {
    this._adjustment = adjustment;
  }


  private ActionErrors validateQuantities()
  {
    ActionErrors quantitiesValidationErrors = new ActionErrors();
    
    Iterator<Map.Entry<String,OrderAdjustmentBodyItem>> iterator = getAdjustment().getBody().entrySet().iterator();
    
    while( iterator.hasNext() )
    {
      Map.Entry<String,OrderAdjustmentBodyItem> entry = iterator.next();
      OrderAdjustmentBodyItem bodyItem = entry.getValue();
      
      if ( bodyItem.isAdjustable() )
      {
        quantitiesValidationErrors.add( validateQuantities( bodyItem ) );
      }
    }
    
    return quantitiesValidationErrors;
  }
  
  
  private ActionErrors validateQuantities( OrderAdjustmentBodyItem bodyItem )
  {
    ActionErrors quantitiesValidationErrors = new ActionErrors();
    
    PurchasablePermission adjustment = bodyItem.getCurrentAdjustmentsDetails();
    
    String orderDetailID = String.valueOf( bodyItem.getDetailID() );
    
    if( adjustment.isPhotocopy() )
    {
      long numCopiesMin = getValueForAdjustment( bodyItem.getSubTotalNumberOfCopies() );
      
      boolean invalidNumberOfCopies = notInRange( adjustment.getNumberOfCopies(), numCopiesMin, ZERO_LONG );
      
      if( invalidNumberOfCopies )
      {
        quantitiesValidationErrors.add( ActionMessages.GLOBAL_MESSAGE, 
                                        new ActionMessage("adj.error.field.not.in.range.indexed", orderDetailID, OrderAdjustmentConstants.NUMBER_OF_COPIES, String.valueOf(numCopiesMin), String.valueOf(ZERO_LONG) ) );
      }

      long numPagesMin = getValueForAdjustment( bodyItem.getSubTotalNumberOfPages() );
      
      boolean invalidNumberOfPages = notInRange( adjustment.getNumberOfPages(), numPagesMin, ZERO_LONG );
      
      if( invalidNumberOfPages )
      {
        quantitiesValidationErrors.add( ActionMessages.GLOBAL_MESSAGE, 
                                        new ActionMessage("adj.error.field.not.in.range.indexed", orderDetailID, OrderAdjustmentConstants.NUMBER_OF_PAGES, String.valueOf(numPagesMin), String.valueOf(ZERO_LONG) ) );
      }
    }
    
    if( adjustment.isEmail() )
    {
      long minNumRecipients = getValueForAdjustment( bodyItem.getSubTotalNumberOfRecipients() );
      boolean invalidNumberOfRecipients = notInRange( adjustment.getNumberOfRecipients(), minNumRecipients, ZERO_LONG );
      
      if( invalidNumberOfRecipients )
      {
        quantitiesValidationErrors.add( ActionMessages.GLOBAL_MESSAGE, 
                                        new ActionMessage("adj.error.field.not.in.range.indexed", orderDetailID, OrderAdjustmentConstants.NUMBER_OF_RECIPIENTS, String.valueOf(minNumRecipients), String.valueOf(ZERO_LONG) ) );
      }
    }
    
    if( adjustment.isNet() )
    {
      long adjustedDuration = adjustment.getDuration();
      
      boolean specifiedAdjustedDuration = adjustedDuration > OrderAdjustmentConstants.UNSPECIFIED_DURATION;
      
      if( specifiedAdjustedDuration )
      {
        long originalDuration = bodyItem.getOriginalOrderDetails().getDuration();
        
        boolean invalidAdjustedDuration = adjustedDuration > originalDuration;
        
        if( invalidAdjustedDuration )
        {
          quantitiesValidationErrors.add( ActionMessages.GLOBAL_MESSAGE, 
                                          new ActionMessage( "adj.error.freeform", "Order Detail ID " + orderDetailID + ": adjusted duration cannot be higher than \"" + bodyItem.getOriginalOrderDetails().getDurationString() + "\"" ) );
        }
      }
      
      
    }
    
    if( adjustment.isAcademic() )
    {
      long minNumStudents = getValueForAdjustment( bodyItem.getSubTotalNumberOfStudents() );
      boolean invalidNumberOfStudents = notInRange( adjustment.getNumberOfStudents(), minNumStudents, ZERO_LONG );
      
      if( invalidNumberOfStudents )
      {
        quantitiesValidationErrors.add( ActionMessages.GLOBAL_MESSAGE, 
                                        new ActionMessage("adj.error.field.not.in.range.indexed", orderDetailID, OrderAdjustmentConstants.NUMBER_OF_STUDENTS, String.valueOf(minNumStudents), String.valueOf(ZERO_LONG) ) );
      }

      long minNumPages = getValueForAdjustment( bodyItem.getSubTotalNumberOfPages() );
      boolean invalidNumberOfPages = notInRange( adjustment.getNumberOfPages(), minNumPages, ZERO_LONG );
      
      if( invalidNumberOfPages )
      {
        quantitiesValidationErrors.add( ActionMessages.GLOBAL_MESSAGE, 
                                        new ActionMessage("adj.error.field.not.in.range.indexed", orderDetailID, OrderAdjustmentConstants.NUMBER_OF_PAGES, String.valueOf(minNumPages), String.valueOf(ZERO_LONG) ) );
      }
    }
    
    if( adjustment.isRepublication() )
    {

      long minNumPages = getValueForAdjustment( bodyItem.getSubTotalNumberOfPages() );
      boolean invalidNumberOfPages = notInRange( adjustment.getNumberOfPages(), minNumPages, ZERO_LONG );
      
      if( invalidNumberOfPages )
      {
        quantitiesValidationErrors.add( ActionMessages.GLOBAL_MESSAGE, 
                                        new ActionMessage("adj.error.field.not.in.range.indexed", orderDetailID, OrderAdjustmentConstants.NUMBER_OF_PAGES, String.valueOf(minNumPages), String.valueOf(ZERO_LONG) ) );
      }

      long minNumCartoons = getValueForAdjustment( bodyItem.getSubTotalNumberOfCartoons() );
      boolean invalidNumberOfCartoons = notInRange( adjustment.getNumberOfCartoons(), minNumCartoons, ZERO_LONG );
      
      if( invalidNumberOfCartoons )
      {
        quantitiesValidationErrors.add( ActionMessages.GLOBAL_MESSAGE, 
                                        new ActionMessage("adj.error.field.not.in.range.indexed", orderDetailID, OrderAdjustmentConstants.NUMBER_OF_CARTOONS, String.valueOf(minNumCartoons), String.valueOf(ZERO_LONG) ) );
      }

      long minNumCharts = getValueForAdjustment( bodyItem.getSubTotalNumberOfCharts() );
      boolean invalidNumberOfCharts = notInRange( adjustment.getNumberOfCharts(), minNumCharts, ZERO_LONG );
      
      if( invalidNumberOfCharts )
      {
        quantitiesValidationErrors.add( ActionMessages.GLOBAL_MESSAGE, 
                                        new ActionMessage("adj.error.field.not.in.range.indexed", orderDetailID, OrderAdjustmentConstants.NUMBER_OF_CHARTS, String.valueOf(minNumCharts), String.valueOf(ZERO_LONG) ) );
      }

      long minNumExcerpts = getValueForAdjustment( bodyItem.getSubTotalNumberOfExcerpts() );
      boolean invalidNumberOfExcerpts = notInRange( adjustment.getNumberOfExcerpts(), minNumExcerpts, ZERO_LONG );
      
      if( invalidNumberOfExcerpts )
      {
        quantitiesValidationErrors.add( ActionMessages.GLOBAL_MESSAGE, 
                                        new ActionMessage("adj.error.field.not.in.range.indexed", orderDetailID, OrderAdjustmentConstants.NUMBER_OF_EXCERPTS, String.valueOf(minNumExcerpts), String.valueOf(ZERO_LONG) ) ); 
      }

      long minNumFigures = getValueForAdjustment( bodyItem.getSubTotalNumberOfFigures() );
      boolean invalidNumberOfFigures = notInRange( adjustment.getNumberOfFigures(), minNumFigures, ZERO_LONG );
      
      if( invalidNumberOfFigures )
      {
        quantitiesValidationErrors.add( ActionMessages.GLOBAL_MESSAGE, 
                                        new ActionMessage("adj.error.field.not.in.range.indexed", orderDetailID, OrderAdjustmentConstants.NUMBER_OF_FIGURE_DIAGRAM_TABLES, String.valueOf(minNumFigures), String.valueOf(ZERO_LONG) ) );
      }

      long minNumGraphs = getValueForAdjustment( bodyItem.getSubTotalNumberOfGraphs() );
      boolean invalidNumberOfGraphs = notInRange( adjustment.getNumberOfGraphs(), minNumGraphs, ZERO_LONG );
      
      if( invalidNumberOfGraphs )
      {
        quantitiesValidationErrors.add( ActionMessages.GLOBAL_MESSAGE, 
                                        new ActionMessage("adj.error.field.not.in.range.indexed", orderDetailID, OrderAdjustmentConstants.NUMBER_OF_GRAPHS, String.valueOf(minNumGraphs), String.valueOf(ZERO_LONG) ) );
      }

      long minNumIllustrations = getValueForAdjustment( bodyItem.getSubTotalNumberOfIllustrations() );
      boolean invalidNumberOfIllustrations = notInRange( adjustment.getNumberOfIllustrations(), minNumIllustrations, ZERO_LONG );
      
      if( invalidNumberOfIllustrations )
      {
        quantitiesValidationErrors.add( ActionMessages.GLOBAL_MESSAGE, 
                                        new ActionMessage("adj.error.field.not.in.range.indexed", orderDetailID, OrderAdjustmentConstants.NUMBER_OF_ILLUSTRATIONS, String.valueOf(minNumIllustrations), String.valueOf(ZERO_LONG) ) );
      }

      long minNumLogos = getValueForAdjustment( bodyItem.getSubTotalNumberOfLogos() );
      boolean invalidNumberOfLogos = notInRange( adjustment.getNumberOfLogos(), minNumLogos, ZERO_LONG );
      
      if( invalidNumberOfLogos )
      {
        quantitiesValidationErrors.add( ActionMessages.GLOBAL_MESSAGE, 
                                        new ActionMessage("adj.error.field.not.in.range.indexed", orderDetailID, OrderAdjustmentConstants.NUMBER_OF_LOGOS, String.valueOf(minNumLogos), String.valueOf(ZERO_LONG) ) );
      }

      long minNumPhotos = getValueForAdjustment( bodyItem.getSubTotalNumberOfPhotos() );
      boolean invalidNumberOfPhotos = notInRange( adjustment.getNumberOfPhotos(), minNumPhotos, ZERO_LONG );
      
      if( invalidNumberOfPhotos )
      {
        quantitiesValidationErrors.add( ActionMessages.GLOBAL_MESSAGE, 
                                        new ActionMessage("adj.error.field.not.in.range.indexed", orderDetailID, OrderAdjustmentConstants.NUMBER_OF_PHOTOGRAPHS, String.valueOf(minNumPhotos), String.valueOf(ZERO_LONG) ) );
      }
    }
    
    return quantitiesValidationErrors;
  }

  private ActionErrors validateFees()
  {
    ActionErrors feesValidationErrors = new ActionErrors();
    
    Iterator<Map.Entry<String,OrderAdjustmentBodyItem>> iterator = getAdjustment().getBody().entrySet().iterator();
    
    while( iterator.hasNext() )
    {
      Map.Entry<String,OrderAdjustmentBodyItem> entry = iterator.next();
      OrderAdjustmentBodyItem bodyItem = entry.getValue();
      
      if ( bodyItem.isAdjustable() )
      {
        feesValidationErrors.add( validateFees( bodyItem ) );
      }
    }
    
    return feesValidationErrors;
  }
  
  
  private ActionErrors validateFees( OrderAdjustmentBodyItem bodyItem )
  {
    ActionErrors feesValidationErrors = new ActionErrors();
    
    String orderDetailID = String.valueOf( bodyItem.getDetailID() );
    
    PurchasablePermission adjustment = bodyItem.getCurrentAdjustmentsDetails();

    double minLicenseeFee = getValueForAdjustment( bodyItem.getSubTotalLicenseeFee() );
    boolean invalidLicenseeFee = notInRange( adjustment.getLicenseeFee(), minLicenseeFee, ZERO_DOUBLE );
    
    if( invalidLicenseeFee )
    {
      feesValidationErrors.add( ActionMessages.GLOBAL_MESSAGE, 
                                new ActionMessage("adj.error.field.not.in.range.indexed", orderDetailID, OrderAdjustmentConstants.LICENSEE_FEE, String.valueOf(minLicenseeFee), String.valueOf(ZERO_DOUBLE) ) );
    }

    double minDiscount = getValueForAdjustment( bodyItem.getSubTotalDiscount() );
    boolean invalidDiscount = notInRange( adjustment.getDiscount(), minDiscount, ZERO_DOUBLE );
    
    if( invalidDiscount )
    {
      feesValidationErrors.add( ActionMessages.GLOBAL_MESSAGE, 
                                new ActionMessage("adj.error.field.not.in.range.indexed", orderDetailID, OrderAdjustmentConstants.DISCOUNT, String.valueOf(minDiscount), String.valueOf(ZERO_DOUBLE) ) );
    }

    double minRoyalty = getValueForAdjustment( bodyItem.getSubTotalRoyaltyComposite() );
    boolean invalidRoyalty = notInRange( adjustment.getRoyalty(), minRoyalty, ZERO_DOUBLE );
    
    if( invalidRoyalty )
    {
      feesValidationErrors.add( ActionMessages.GLOBAL_MESSAGE, 
                                new ActionMessage("adj.error.field.not.in.range.indexed", orderDetailID, OrderAdjustmentConstants.ROYALTY, String.valueOf(minRoyalty), String.valueOf(ZERO_DOUBLE) ) );
    }
    
    return feesValidationErrors;
  }


  private boolean inRange( long qty, long value1, long value2 )
  {
    boolean inRange = false;
    
    if( value1 < value2 )
    {
      inRange = qty >= value1 && qty <= value2;
    }
    
    if( value1 > value2 )
    {
      inRange = qty <= value1 && qty >= value2;
    }
    
    if( value1 == value2 )
    {
      inRange = qty == value1;
    }
    
    return inRange;
  }
  
  
  private boolean notInRange( long qty, long min, long max )
  {
    return !inRange(qty, min, max); 
  }
  
  
  private long getValueForAdjustment( long qty )
  {
    return 0L - qty;
  }
  
  
  private boolean inRange( double qty, double value1, double value2 )
  {
    
    boolean inRange = false;
    
    if( value1 < value2 )
    {
      inRange = qty >= value1 && qty <= value2;
    }
    
    if( value1 > value2 )
    {
      inRange = qty <= value1 && qty >= value2;
    }
    
    if( value1 == value2 )
    {
      inRange = qty == value1;
    }
    
    
    return inRange; 
  }
  
  
  private boolean notInRange( double qty, double min, double max )
  {
    return !inRange(qty, min, max); 
  }
  
  
  private double getValueForAdjustment( double qty )
  {
    return 0D - qty;
  }
  
}
