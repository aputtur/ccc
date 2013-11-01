package com.copyright.ccc.business.services.adjustment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.copyright.ccc.CCRuntimeException;
import com.copyright.ccc.business.data.OrderLicense;
import com.copyright.ccc.business.data.OrderLicenses;
import com.copyright.ccc.business.security.CCPrivilegeCode;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.ecommerce.PricingServices;
import com.copyright.ccc.business.services.ecommerce.PurchasablePermission;
import com.copyright.ccc.business.services.ecommerce.PurchasablePermissionFactory;
import com.copyright.ccc.business.services.order.OrderLicenseImpl;
import com.copyright.ccc.business.services.order.OrderLicenseServices;
import com.copyright.ccc.business.services.order.OrderLicensesException;
import com.copyright.ccc.web.transaction.RepublicationConstants;
import com.copyright.data.order.PermissionRequest;
import com.copyright.data.order.UsageData;
import com.copyright.data.order.UsageDataAcademic;
import com.copyright.data.order.UsageDataEmail;
import com.copyright.data.order.UsageDataNet;
import com.copyright.data.order.UsageDataPhotocopy;
import com.copyright.data.order.UsageDataRepublication;
import com.copyright.workbench.i18n.Money;


/**
 * Class representing an adjustment's body item.
 */
public class OrderAdjustmentBodyItem
{
  private static final Logger _logger = Logger.getLogger( OrderAdjustmentBodyItem.class );
  private static final long INVALID_ADJUSTMENT_QUANTITY_LONG = -1;
 
  private static final String PAYMENT_METHOD_INVOICE = "I";
  private static final String PAYMENT_METHOD_CREDIT_CARD = "C";
  private static final int INVALID_ADJUSTMENT_QUANTITY_INT = -1;
  private static final int ADJUSTMENT_ZERO_QUANTITY = 0;
  private static final String BILLING_STATUS_CANCELED = "Canceled";

  private Long _originalLicenseePartyId;
  private OrderLicense _originalOrderDetails;
  private List<OrderLicense> _previousAdjustmentsDetails;
  private PurchasablePermission _currentAdjustmentsDetails;

  private OrderAdjustmentBodyItem(){}

  OrderAdjustmentBodyItem( OrderLicense originalOrderDetails )
  {
    UserContextService.checkPrivilege( CCPrivilegeCode.ENTER_ORDER_ADJUSTMENT );
    
    if( originalOrderDetails == null )
    {
      throw new IllegalArgumentException( "Original order details must be provided" );
    }
    
    //1- Original order details
    setOriginalOrderDetails( originalOrderDetails );
    
    //2- Previous adjustments details
    try
    {
      OrderLicenses previousAdjustments = OrderLicenseServices.getOrderLicensesForAdjustment( OrderLicenseServices.LICENSE_ADJUSTMENT_LICENSES,
                                                                                              String.valueOf( originalOrderDetails.getID() ) );

      List<OrderLicense> previousAdjustmentsDetails = previousAdjustments.getOrderLicenseList();
      
      boolean isLicenseListConsistent = previousAdjustmentsDetails != null;
                                        
      if( isLicenseListConsistent )
      {
        setPreviousAdjustmentsDetails( previousAdjustmentsDetails );
      }else
      {
        setPreviousAdjustmentsDetails( new ArrayList<OrderLicense>(0) );
      }     
      
    } catch ( OrderLicensesException ole )
    {
      throw new CCRuntimeException( "Could not create an instance of OrderAdjustmentBodyItem: " + ole.getMessage(), ole );
    }
       
    //3- Current adjustment details
    PurchasablePermission currentAdjustmentDetails = buildCurrentAdjustmentDetails( originalOrderDetails );
    setCurrentAdjustmentsDetails( currentAdjustmentDetails );
  
  }


  /**
   * Indicates if this body item is distributed.
   */
  public boolean isDistributed()
  {
    return getOriginalOrderDetails().isDistributed();
  }


  /**
   * Returns the original payment method for this body item.
   */
  public String getPaymentMethod()
  {
    
    String paymentMethod = null;
    
    OrderLicense originalOrderDetails = getOriginalOrderDetails();
    
    boolean creditCardDetailsProvided = originalOrderDetails.getLastFourCreditCard() != 0;
    
    if( creditCardDetailsProvided )
    {
      paymentMethod = PAYMENT_METHOD_CREDIT_CARD;
    }else
    {
      paymentMethod = PAYMENT_METHOD_INVOICE;
    }
    
    return paymentMethod;
  }

  void setOriginalLicenseePartyId( Long partyId ){
    this._originalLicenseePartyId = partyId;
  }
    
 /**
  * Returns the licensee's party ID.  The licensee that originally
  * placed the order.
  */
  public Long getOriginalLicenseePartyId(){
    return _originalLicenseePartyId;
  }
     
  private void setOriginalOrderDetails(OrderLicense originalOrderDetails)
  {
    this._originalOrderDetails = originalOrderDetails;
  }
  
 /**
  * Returns the original order details for this body item.
  */
  public OrderLicense getOriginalOrderDetails()
  {
    return _originalOrderDetails;
  }


  private void setPreviousAdjustmentsDetails(List<OrderLicense> previousAdjustmentsDetails)
  {
    this._previousAdjustmentsDetails = previousAdjustmentsDetails;
  }

  /**
   * Returns a <code>List</code> containing the previous adjustment details for this body item.
   */
  public List<OrderLicense> getPreviousAdjustmentsDetails()
  {
    return _previousAdjustmentsDetails;
  }


  void setCurrentAdjustmentsDetails(PurchasablePermission currentAdjustmentsDetails)
  {
    this._currentAdjustmentsDetails = currentAdjustmentsDetails;
  }

  /**
   * Returns the current adjustment details for this body item.
   */
  public PurchasablePermission getCurrentAdjustmentsDetails()
  {
    return _currentAdjustmentsDetails;
  }
  
  /**
   * Returns the original order details' ID for this body item.
   */
  public long getDetailID()
  {
    return getOriginalOrderDetails().getID();
  }
  
  /**
   * Returns the original order details' publication title for this body item.
   */
  public String getPublicationTitle()
  {
    return getOriginalOrderDetails().getPublicationTitle();
  }
  
  /**
   * Returns the original order details' publication's standard number for this body item.
   */
  public String getStandardNumber()
  {
    return getOriginalOrderDetails().getStandardNumber();
  }
  
  /**
   * Returns the original order details' publication's publisher for this body item.
   */
  public String getPublisher()
  {
    return getOriginalOrderDetails().getPublisher();
  }
     
  /**
   * Indicates if this is an APS body item.
   */  
  public boolean isAPS()
  {
    return getOriginalOrderDetails().isAPS();
  }
  
  /**
   * Indicates if this is an ECCS body item.
   */ 
  public boolean isECCS()
  {
    return getOriginalOrderDetails().isECCS();
  }
  
  /**
   * Indicates if this is a TRS body item.
   */ 
  public boolean isTRS()
  {
    return getOriginalOrderDetails().isPhotocopy();
  }
  
  /**
   * Indicates if this is an Email body item.
   */ 
  public boolean isDPSEMail()
  {
    return getOriginalOrderDetails().isEmail();
  }
  
  /**
   * Indicates if this is an Ijternet body item.
   */ 
  public boolean isDPSInternet()
  {
    return getOriginalOrderDetails().isInternet();
  }
  
  /**
   * Indicates if this is an Intranet body item.
   */ 
  public boolean isDPSIntranet()
  {
    return getOriginalOrderDetails().isIntranet();
  }
  
  /**
   * Indicates if this is an Extranet body item.
   */ 
  public boolean isDPSExtranet()
  {
    return getOriginalOrderDetails().isExtranet();
  }
  
  /**
   * Indicates if this is an RLS body item.
   */ 
  public boolean isRLS()
  {
    return getOriginalOrderDetails().isRepublication();
  }
  
  /**
   * Return the total number of pages for this body item.
   */ 
  public long getTotalNumberOfPages()
  {
    long totalNumberOfPages = INVALID_ADJUSTMENT_QUANTITY_LONG;
    
    if( isTRS() || isAPS() || isECCS() || isRLS() )
    {
      totalNumberOfPages = getSubTotalNumberOfPages() +
                           getCurrentAdjustmentsDetails().getNumberOfPages();
    }
    
    return totalNumberOfPages;
  }
  
  /**
   * Return the sub-total number of pages for this body item.
   */
  public long getSubTotalNumberOfPages()
  {
    long subTotalNumberOfPages = INVALID_ADJUSTMENT_QUANTITY_LONG;
    
    if( isTRS() || isAPS() || isECCS() || isRLS() )
    {
      subTotalNumberOfPages = getOriginalOrderDetails().getNumberOfPages();
      
      Iterator<OrderLicense> iterator = getPreviousAdjustmentsDetails().iterator();
      
      while( iterator.hasNext() )
      {
        OrderLicense previousAdjustment = iterator.next();
        
        subTotalNumberOfPages += previousAdjustment.getNumberOfPages();
      }
    }
     
    
    return subTotalNumberOfPages;
  }
  
  /**
   * Return the total number of copies for this body item.
   */ 
  public long getTotalNumberOfCopies()
  {
    long totalNumberOfCopies = INVALID_ADJUSTMENT_QUANTITY_LONG;
    
    if( isTRS() )
    {
      totalNumberOfCopies = getSubTotalNumberOfCopies() +
                            getCurrentAdjustmentsDetails().getNumberOfCopies();
    }
    
    return totalNumberOfCopies;
  }
  
  /**
   * Return the sub-total number of copies for this body item.
   */ 
  public long getSubTotalNumberOfCopies()
  {
    long subTotalNumberOfCopies = INVALID_ADJUSTMENT_QUANTITY_LONG;
    
    if( isTRS() )
    {
      subTotalNumberOfCopies = getOriginalOrderDetails().getNumberOfCopies();
      
      Iterator<OrderLicense> iterator = getPreviousAdjustmentsDetails().iterator();
      
      while( iterator.hasNext() )
      {
        OrderLicense previousAdjustment = iterator.next();
        
        subTotalNumberOfCopies += previousAdjustment.getNumberOfCopies();
      }
    }
    
    return subTotalNumberOfCopies;
  }
  
  /**
   * Return the total number of recipients for this body item.
   */ 
  public long getTotalNumberOfRecipients()
  {
    long totalNumberOfRecipients = INVALID_ADJUSTMENT_QUANTITY_LONG;
    
    if( isDPSEMail() )
    {
      totalNumberOfRecipients = getSubTotalNumberOfRecipients() +
                                getCurrentAdjustmentsDetails().getNumberOfRecipients();
    }
    
    return totalNumberOfRecipients;
  }
  
  /**
   * Return the sub-total number of recipients for this body item.
   */ 
  public long getSubTotalNumberOfRecipients()
  {
    long subTotalNumberOfRecipients = INVALID_ADJUSTMENT_QUANTITY_LONG;
    
    if( isDPSEMail() )
    {
      subTotalNumberOfRecipients = getOriginalOrderDetails().getNumberOfRecipients();
      
      Iterator<OrderLicense> iterator = getPreviousAdjustmentsDetails().iterator();
      
      while( iterator.hasNext() )
      {
        OrderLicense previousAdjustment = iterator.next();
        
        subTotalNumberOfRecipients += previousAdjustment.getNumberOfRecipients();
      }
    }
    
    return subTotalNumberOfRecipients;
  }
  
  /**
   * Return the total number of cartoons for this body item.
   */ 
  public long getTotalNumberOfCartoons()
  {
    long totalNumberOfCartoons = INVALID_ADJUSTMENT_QUANTITY_LONG;
    
    if( isRLS() )
    {
      totalNumberOfCartoons = getSubTotalNumberOfCartoons() +
                              getCurrentAdjustmentsDetails().getNumberOfCartoons();
    }
    
    return totalNumberOfCartoons;
  }
  
  /**
   * Return the sub-total number of cartoons for this body item.
   */ 
  public long getSubTotalNumberOfCartoons()
  {
    long subTotalNumberOfCartoons = INVALID_ADJUSTMENT_QUANTITY_LONG;
    
    if( isRLS() )
    {
      subTotalNumberOfCartoons = getOriginalOrderDetails().getNumberOfCartoons();
      
      Iterator<OrderLicense> iterator = getPreviousAdjustmentsDetails().iterator();
      
      while( iterator.hasNext() )
      {
        OrderLicense previousAdjustment = iterator.next();
        
        subTotalNumberOfCartoons += previousAdjustment.getNumberOfCartoons();
      }
    }
    
    return subTotalNumberOfCartoons;
  }
  
  /**
   * Return the total number of charts for this body item.
   */ 
  public long getTotalNumberOfCharts()
  {
    long totalNumberOfCharts = INVALID_ADJUSTMENT_QUANTITY_LONG;
    
    if( isRLS() )
    {
      totalNumberOfCharts = getSubTotalNumberOfCharts() +
                            getCurrentAdjustmentsDetails().getNumberOfCharts();
    }
    
    return totalNumberOfCharts;
  }
  
  /**
   * Return the sub-total number of charts for this body item.
   */ 
  public long getSubTotalNumberOfCharts()
  {
    long subTotalNumberOfCharts = INVALID_ADJUSTMENT_QUANTITY_LONG;
    
    if( isRLS() )
    {
      subTotalNumberOfCharts = getOriginalOrderDetails().getNumberOfCharts();
      
      Iterator<OrderLicense> iterator = getPreviousAdjustmentsDetails().iterator();
      
      while( iterator.hasNext() )
      {
        OrderLicense previousAdjustment = iterator.next();
        
        subTotalNumberOfCharts += previousAdjustment.getNumberOfCharts();
      }
    }
    
    return subTotalNumberOfCharts;
  }
  
  /**
   * Return the total number of excerpts for this body item.
   */ 
  public long getTotalNumberOfExcerpts()
  {
    long totalNumberOfExcerpts = INVALID_ADJUSTMENT_QUANTITY_LONG;
    
    if( isRLS() )
    {
      totalNumberOfExcerpts = getSubTotalNumberOfExcerpts() +
                              getCurrentAdjustmentsDetails().getNumberOfExcerpts();
    }
    
    return totalNumberOfExcerpts;
  }
  
  /**
   * Return the sub-total number of excerpts for this body item.
   */ 
  public long getSubTotalNumberOfExcerpts()
  {
    long subTotalNumberOfExcerpts = INVALID_ADJUSTMENT_QUANTITY_LONG;
    
    if( isRLS() )
    {
      subTotalNumberOfExcerpts = getOriginalOrderDetails().getNumberOfExcerpts();
      
      Iterator<OrderLicense> iterator = getPreviousAdjustmentsDetails().iterator();
      
      while( iterator.hasNext() )
      {
        OrderLicense previousAdjustment = iterator.next();
        
        subTotalNumberOfExcerpts += previousAdjustment.getNumberOfExcerpts();
      }
    }
    
    return subTotalNumberOfExcerpts;
  }
  
  /**
   * Return the total number of figures for this body item.
   */ 
  public long getTotalNumberOfFigures()
  {
    long totalNumberOfFigures = INVALID_ADJUSTMENT_QUANTITY_LONG;
    
    if( isRLS() )
    {
      totalNumberOfFigures = getSubTotalNumberOfFigures() +
                             getCurrentAdjustmentsDetails().getNumberOfFigures();
    }
    
    return totalNumberOfFigures;
  }
  
  /**
   * Return the sub-total number of figures for this body item.
   */ 
  public long getSubTotalNumberOfFigures()
  {
    long subTotalNumberOfFigures = INVALID_ADJUSTMENT_QUANTITY_LONG;
    
    if( isRLS() )
    {
      subTotalNumberOfFigures = getOriginalOrderDetails().getNumberOfFigures();
      
      Iterator<OrderLicense> iterator = getPreviousAdjustmentsDetails().iterator();
      
      while( iterator.hasNext() )
      {
        OrderLicense previousAdjustment = iterator.next();
        
        subTotalNumberOfFigures += previousAdjustment.getNumberOfFigures();
      }
    }
    
    return subTotalNumberOfFigures;
  }
  
  /**
   * Returns the total number of graphs for this body item.
   */ 
  public long getTotalNumberOfGraphs()
  {
    long totalNumberOfGraphs = INVALID_ADJUSTMENT_QUANTITY_LONG;
    
    if( isRLS() )
    {
      totalNumberOfGraphs = getSubTotalNumberOfGraphs() +
                            getCurrentAdjustmentsDetails().getNumberOfGraphs();
    }
    
    return totalNumberOfGraphs;
  }
  
  /**
   * Returns the sub-total number of graphs for this body item.
   */ 
  public long getSubTotalNumberOfGraphs()
  {
    long subTotalNumberOfGraphs = INVALID_ADJUSTMENT_QUANTITY_LONG;
    
    if( isRLS() )
    {
      subTotalNumberOfGraphs = getOriginalOrderDetails().getNumberOfGraphs();
      
      Iterator<OrderLicense> iterator = getPreviousAdjustmentsDetails().iterator();
      
      while( iterator.hasNext() )
      {
        OrderLicense previousAdjustment = iterator.next();
        
        subTotalNumberOfGraphs += previousAdjustment.getNumberOfGraphs();
      }
    }
    
    return subTotalNumberOfGraphs;
  }
  
  /**
   * Returns the total number of illustrations for this body item.
   */ 
  public long getTotalNumberOfIllustrations()
  {
    long totalNumberOfIllustrations = INVALID_ADJUSTMENT_QUANTITY_LONG;
    
    if( isRLS() )
    {
      totalNumberOfIllustrations = getSubTotalNumberOfIllustrations() +
                                   getCurrentAdjustmentsDetails().getNumberOfIllustrations();
    }
    
    return totalNumberOfIllustrations;
  }
  
  /**
   * Returns the sub-total number of illustrations for this body item.
   */ 
  public long getSubTotalNumberOfIllustrations()
  {
    long subTotalNumberOfIllustrations = INVALID_ADJUSTMENT_QUANTITY_LONG;
    
    if( isRLS() )
    {
      subTotalNumberOfIllustrations = getOriginalOrderDetails().getNumberOfIllustrations();
      
      Iterator<OrderLicense> iterator = getPreviousAdjustmentsDetails().iterator();
      
      while( iterator.hasNext() )
      {
        OrderLicense previousAdjustment = iterator.next();
        
        subTotalNumberOfIllustrations += previousAdjustment.getNumberOfIllustrations();
      }
    }
    
    return subTotalNumberOfIllustrations;
  }
  
  /**
   * Returns the total number of logos for this body item.
   */ 
  public long getTotalNumberOfLogos()
  {
    long totalNumberOfLogos = INVALID_ADJUSTMENT_QUANTITY_LONG;
    
    if( isRLS() )
    {
      totalNumberOfLogos = getSubTotalNumberOfLogos() +
                           getCurrentAdjustmentsDetails().getNumberOfLogos();
    }
    
    return totalNumberOfLogos;
  }
  
  /**
   * Returns the sub-total number of logos for this body item.
   */ 
  public long getSubTotalNumberOfLogos()
  {
    long subTotalNumberOfLogos = INVALID_ADJUSTMENT_QUANTITY_LONG;
    
    if( isRLS() )
    {
      subTotalNumberOfLogos = getOriginalOrderDetails().getNumberOfLogos();
      
      Iterator<OrderLicense> iterator = getPreviousAdjustmentsDetails().iterator();
      
      while( iterator.hasNext() )
      {
        OrderLicense previousAdjustment = iterator.next();
        
        subTotalNumberOfLogos += previousAdjustment.getNumberOfLogos();
      }
    }
    
    return subTotalNumberOfLogos;
  }
  
  /**
   * Returns the total number of photos for this body item.
   */ 
  public long getTotalNumberOfPhotos()
  {
    long totalNumberOfPhotos = INVALID_ADJUSTMENT_QUANTITY_LONG;
    
    if( isRLS() )
    {
      totalNumberOfPhotos = getSubTotalNumberOfPhotos() +
                            getCurrentAdjustmentsDetails().getNumberOfPhotos();
    }
    
    return totalNumberOfPhotos;
  }
  
  /**
   * Returns the sub-total number of photos for this body item.
   */ 
  public long getSubTotalNumberOfPhotos()
  {
    long subTotalNumberOfPhotos = INVALID_ADJUSTMENT_QUANTITY_LONG;
    
    if( isRLS() )
    {
      subTotalNumberOfPhotos = getOriginalOrderDetails().getNumberOfPhotos();
      
      Iterator<OrderLicense> iterator = getPreviousAdjustmentsDetails().iterator();
      
      while( iterator.hasNext() )
      {
        OrderLicense previousAdjustment = iterator.next();
        
        subTotalNumberOfPhotos += previousAdjustment.getNumberOfPhotos();
      }
    }
    
    return subTotalNumberOfPhotos;
  }
  
  /**
   * Returns the total number of quotes for this body item.
   */ 
  public long getTotalNumberOfQuotes()
  {
    long totalNumberOfQuotes = INVALID_ADJUSTMENT_QUANTITY_LONG;
    
    if( isRLS() )
    {
      totalNumberOfQuotes = getSubTotalNumberOfQuotes() +
                            getCurrentAdjustmentsDetails().getNumberOfQuotes();
    }
    
    return totalNumberOfQuotes;
  }
  
  /**
   * Returns the sub-total number of quotes for this body item.
   */ 
  public long getSubTotalNumberOfQuotes()
  {
    long subTotalNumberOfQuotes = INVALID_ADJUSTMENT_QUANTITY_LONG;
    
    if( isRLS() )
    {
      subTotalNumberOfQuotes = getOriginalOrderDetails().getNumberOfQuotes();
      
      Iterator<OrderLicense> iterator = getPreviousAdjustmentsDetails().iterator();
      
      while( iterator.hasNext() )
      {
        OrderLicense previousAdjustment = iterator.next();
        
        subTotalNumberOfQuotes += previousAdjustment.getNumberOfQuotes();
      }
    }
    
    return subTotalNumberOfQuotes;
  }
  
  /**
   * Returns the total number of students for this body item.
   */ 
  public long getTotalNumberOfStudents()
  {
    long totalNumberOfStudents = INVALID_ADJUSTMENT_QUANTITY_LONG;
    
    if( isAPS() || isECCS() )
    {
      totalNumberOfStudents = getSubTotalNumberOfStudents() +
                              getCurrentAdjustmentsDetails().getNumberOfStudents();
    }
    
    return totalNumberOfStudents;
  }
  
  /**
   * Returns the sub-total number of students for this body item.
   */ 
  public int getSubTotalNumberOfStudents()
  {
    int subTotalNumberOfStudents = INVALID_ADJUSTMENT_QUANTITY_INT;
    
    if( isAPS() || isECCS() )
    {
      subTotalNumberOfStudents = getOriginalOrderDetails().getNumberOfStudents();
      
      Iterator<OrderLicense> iterator = getPreviousAdjustmentsDetails().iterator();
      
      while( iterator.hasNext() )
      {
        OrderLicense previousAdjustment = iterator.next();
        
        subTotalNumberOfStudents += previousAdjustment.getNumberOfStudents();
      }
    }
    
    return subTotalNumberOfStudents;
  }
  
  /**
   * Returns the total licensee fee for this body item.
   */ 
  public double getTotalLicenseeFee()
  {
    BigDecimal totalLicenseeFee = new BigDecimal( getSubTotalLicenseeFee() )
                                  .add( new BigDecimal( getCurrentAdjustmentsDetails().getLicenseeFee() ) );
    
    return OrderAdjustmentUtils.roundToDefaultDecimalPlaces( totalLicenseeFee.doubleValue() );
  }
  
  /**
   * Returns the sub-total licensee fee for this body item.
   */ 
  public double getSubTotalLicenseeFee()
  {
    BigDecimal subTotalLicenseeFees = new BigDecimal( getOriginalOrderDetails().getLicenseeFee() );
    
    if( this.hasPreviousAdjustments() )
    {
      subTotalLicenseeFees = subTotalLicenseeFees.add( new BigDecimal( getPreviousAdjustmentsLicenseeFeeTotal() ) );
    }
    
    return OrderAdjustmentUtils.roundToDefaultDecimalPlaces( subTotalLicenseeFees.doubleValue() );
  }
  
  /**
   * Returns the total royalty composite for this body item.
   */ 
  public double getTotalRoyaltyComposite()
  {
    BigDecimal totalRoyaltyComposite = new BigDecimal( getSubTotalRoyaltyComposite() ).
                                       add( new BigDecimal( getCurrentAdjustmentsDetails().getRoyaltyComposite() ) );  
    
    return OrderAdjustmentUtils.roundToDefaultDecimalPlaces( totalRoyaltyComposite.doubleValue() );
  }

  /**
   * Returns the sub-total royalty composite for this body item.
   */ 
  public double getSubTotalRoyaltyComposite()
  {
    BigDecimal subTotalRoyaltyComposite = new BigDecimal( getOriginalOrderDetails().getRoyaltyComposite() );
        
    if( hasPreviousAdjustments() )
    {
      subTotalRoyaltyComposite = subTotalRoyaltyComposite.add( new BigDecimal( getPreviousAdjustmentsRoyaltyCompositeTotal() ) );
      
    }
    
    return OrderAdjustmentUtils.roundToDefaultDecimalPlaces( subTotalRoyaltyComposite.doubleValue() );
  }
  
  /**
   * Returns the sub-total royalty for this body item.
   */ 
  public double getSubTotalRoyalty()
  {
    BigDecimal subTotalRoyalty = new BigDecimal( getOriginalOrderDetails().getRoyalty() );
    
    if( hasPreviousAdjustments() )
    {
        subTotalRoyalty = subTotalRoyalty.add( new BigDecimal( getPreviousAdjustmentsRoyaltyTotal() ) );
    }
    
    return OrderAdjustmentUtils.roundToDefaultDecimalPlaces( subTotalRoyalty.doubleValue() );
  }
  
  /**
   * Returns the sub-total rightsholder fee for this body item.
   */ 
  public double getSubTotalRightholderFee()
  {
    BigDecimal subTotalRightholderFee = new BigDecimal( getOriginalOrderDetails().getRightsholderFee() );
        
    if( hasPreviousAdjustments() )
    {
      subTotalRightholderFee = subTotalRightholderFee.add( new BigDecimal( getPreviousAdjustmentsRightsholderFeeTotal() ) );
    }
    
    return OrderAdjustmentUtils.roundToDefaultDecimalPlaces( subTotalRightholderFee.doubleValue() );
  }
  
  /**
   * Returns the total discount for this body item.
   */   
  public double getTotalDiscount()
  {
    BigDecimal totalDiscount = new BigDecimal( getSubTotalDiscount() )
                               .add( new BigDecimal( getCurrentAdjustmentsDetails().getDiscount() ) );
    
    return OrderAdjustmentUtils.roundToDefaultDecimalPlaces( totalDiscount.doubleValue() );
    }
  
  /**
   * Returns the sub-total discount for this body item.
   */ 
  public double getSubTotalDiscount()
  {
    BigDecimal subTotalDiscount = new BigDecimal( getOriginalOrderDetails().getDiscount() );
    
    if ( this.hasPreviousAdjustments() )
    {
      subTotalDiscount = subTotalDiscount.add( new BigDecimal( getPreviousAdjustmentsDiscountTotal() ));
    }
    
    return OrderAdjustmentUtils.roundToDefaultDecimalPlaces( subTotalDiscount.doubleValue() );
  }
  
  /**
   * Returns the total for this body item.
   */ 
  public double getTotal()
  {

    BigDecimal total = new BigDecimal( getSubTotal() )
                       .add( new BigDecimal( getCurrentAdjustmentTotal() ) );
        
    return OrderAdjustmentUtils.roundToDefaultDecimalPlaces( total.doubleValue() );
  }
 
  /**
   * Returns the sub-total for this body item.
   */ 
  public double getSubTotal()
  {
    BigDecimal subTotal = new BigDecimal( 0 );
    
    OrderLicense originalOrderDetails = getOriginalOrderDetails();
    
    if( originalOrderDetails instanceof OrderLicenseImpl )
    {
      subTotal = new BigDecimal( ((OrderLicenseImpl)originalOrderDetails).getLicense().getPrice().getValue() );
    }
    
    Iterator<OrderLicense> iterator = getPreviousAdjustmentsDetails().iterator();
    
    while( iterator.hasNext() )
    {
      OrderLicense previousAdjustment = iterator.next();
      
      if( previousAdjustment instanceof OrderLicenseImpl )
      {
        subTotal = subTotal.add( new BigDecimal( ((OrderLicenseImpl)previousAdjustment).getLicense().getPrice().getValue() ) );
      }else{
        throw new CCRuntimeException("Could not calculate sub-total for body item: incompatible previous adjustment found (" + previousAdjustment.getClass().getName() + ")" );
      }
    }
    
    return OrderAdjustmentUtils.roundToDefaultDecimalPlaces( subTotal.doubleValue() );
  }
  
  /**
   * Returns the royalty composite for this body item's current adjustment.
   */   
  public double getCurrentAdjustmentRoyaltyComposite()
  {
    return getCurrentAdjustmentsDetails().getRoyaltyComposite();
  }
  
  /**
   * Sets the royalty composite (defined as royalty + rightsholder fee) 
   * for this body item's current adjustment.
   */ 
  public void setCurrentAdjustmentRoyaltyComposite( double royaltyComposite )
  {
    OrderLicense originalDetails = getOriginalOrderDetails();

    double originalDetailsRoyaltyComposite = originalDetails.getRoyaltyComposite();
    
    BigDecimal royaltyProportion = new BigDecimal( originalDetails.getRoyalty() ).divide( new BigDecimal( originalDetailsRoyaltyComposite ),4,  BigDecimal.ROUND_HALF_EVEN);
    royaltyProportion = royaltyProportion.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    
    BigDecimal rightsholderFeeProportion = new BigDecimal( 1.000D - royaltyProportion.doubleValue()); //Originally calculated as -> ( originalDetails.getRightsholderFee() / originalDetailsRoyaltyComposite );
    rightsholderFeeProportion = rightsholderFeeProportion.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    
    BigDecimal currentRoyalty = new BigDecimal( royaltyComposite ).multiply(  royaltyProportion  );
    currentRoyalty = currentRoyalty.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    BigDecimal currentRightsholderFees = new BigDecimal( royaltyComposite ).multiply(  rightsholderFeeProportion  );
    currentRightsholderFees = currentRightsholderFees.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    
    PurchasablePermission currentAdjustment = getCurrentAdjustmentsDetails();
    currentAdjustment.setRoyalty( currentRoyalty.doubleValue() );
    currentAdjustment.setRightholderFee( currentRightsholderFees.doubleValue() );
    
  }
    
  /**
   * Performs full credit on this body item, for the given <code>reasonCode</code>.
   */   
  public void performFullCredit( long reasonCode ) 
  throws OrderAdjustmentPriceCalculationException
  {
    UserContextService.checkPrivilege( CCPrivilegeCode.ENTER_ORDER_ADJUSTMENT );
    
    if( !isAdjustable() )
    {
      throw new IllegalStateException( "Cannot perform full credit to a not adjustable body item. Original order detail ID: " + getOriginalOrderDetails().getID() );  
    }
    
    PurchasablePermission currentAdjustment = getCurrentAdjustmentsDetails();
    
    boolean isValidProduct = isTRS() ||
                             isDPSEMail() ||
                             isDPSExtranet() ||
                             isDPSInternet() ||
                             isDPSIntranet() ||
                             isAPS() ||
                             isECCS() ||
                             isRLS();
  
    if ( isValidProduct )
    {
      long originalReasonCode = currentAdjustment.getReasonCd();
            
      UsageData originalUsageData = getDeepCopyOfCurrentAdjustmentUsageData();
      
      currentAdjustment.setReasonCd( reasonCode );
      
      
      //We set quantities accordingly...     
      if( isTRS() )
      {
        currentAdjustment.setNumberOfPages( getAmountForFullCredit( getSubTotalNumberOfPages() )  );
        currentAdjustment.setNumberOfCopies( getAmountForFullCredit( getSubTotalNumberOfCopies() ) );
      }
      
      if( isDPSEMail() )
      {
        currentAdjustment.setNumberOfRecipients( getAmountForFullCredit( getSubTotalNumberOfRecipients() ) );
      }
      
      if( isDPSExtranet() || isDPSInternet() || isDPSIntranet() )
      {
        //Set the same duration of the original order as there is no notion of full credit for Net orders.
        currentAdjustment.setDuration( getOriginalOrderDetails().getDuration() );
      }
      
      if( isAPS() || isECCS() )
      {
        currentAdjustment.setNumberOfStudents( getAmountForFullCredit( getSubTotalNumberOfStudents() ) );
        currentAdjustment.setNumberOfPages( getAmountForFullCredit( getSubTotalNumberOfPages() ) );
      }
      
      if( isRLS() )
      {
        String contentType = getOriginalOrderDetails().getTypeOfContent();
        
        if( contentType.equals( RepublicationConstants.CONTENT_CHART ) )
        {
          currentAdjustment.setNumberOfCharts( getAmountForFullCredit( getSubTotalNumberOfCharts() ) );
        }
        
        if( contentType.equals( RepublicationConstants.CONTENT_EXCERPT ) )
        {
          currentAdjustment.setNumberOfExcerpts( getAmountForFullCredit( getSubTotalNumberOfExcerpts() ) );
        }
        
        if( contentType.equals( RepublicationConstants.CONTENT_FIGURE_DIAGRAM_TABLE ) )
        {
          currentAdjustment.setNumberOfFigures( getAmountForFullCredit( getSubTotalNumberOfFigures() ) );
        }
        
        if( contentType.equals( RepublicationConstants.CONTENT_FULL_ARTICLE_CHAPTER ) )
        {
          //We don't need to do nothing in this case as quantity cannot be ajusted (changed).
        }
        
        if( contentType.equals( RepublicationConstants.CONTENT_GRAPH ) )
        {
          currentAdjustment.setNumberOfGraphs( getAmountForFullCredit( getSubTotalNumberOfGraphs() ) );
        }
        
        if( contentType.equals( RepublicationConstants.CONTENT_ILLUSTRATION ) )
        {
          currentAdjustment.setNumberOfIllustrations( getAmountForFullCredit( getSubTotalNumberOfIllustrations() ) );
        }
        
        if( contentType.equals( RepublicationConstants.CONTENT_PHOTOGRAPH ) )
        {
          currentAdjustment.setNumberOfPhotos( getAmountForFullCredit( getSubTotalNumberOfPhotos() ) );
        }
        
        if( contentType.equals( RepublicationConstants.CONTENT_QUOTATION ) )
        {
          currentAdjustment.setNumberOfQuotes( getAmountForFullCredit( getSubTotalNumberOfQuotes() ) );
        }
        
        if( contentType.equals( RepublicationConstants.CONTENT_SELECTED_PAGES ) )
        {
          currentAdjustment.setNumberOfPages( getAmountForFullCredit( getSubTotalNumberOfPages() ) );
        }
        
        if( contentType.equals( RepublicationConstants.CONTENT_CARTOONS ) )
        {
          currentAdjustment.setNumberOfCartoons( getAmountForFullCredit( getSubTotalNumberOfCartoons() ) );
        }
        
        if( contentType.equals( RepublicationConstants.CONTENT_LOGOS ) )
        {
          currentAdjustment.setNumberOfLogos( getAmountForFullCredit( getSubTotalNumberOfLogos() ) );
        }
      }
      
      
      //...and recalculate the price if needed
      if( areCurrentAdjustmentQuantitiesModified() )
      {
        //We want to recalculate prices to reflect changes in quantities
        try
        {
          recalculateCurrentAdjustmentPrice();
          
          //We have to do this becuase quick price shared service does not
          //reset licensee fee (e.g. it always returns a value of 3)
          currentAdjustment.setLicenseeFee( getAmountForFullCredit( getSubTotalLicenseeFee() ) );
          
        } catch ( OrderAdjustmentPriceCalculationException priceCalculationException )
        {
          //Rollback changes
          getCurrentAdjustmentsDetails().setReasonCd( originalReasonCode );
          PurchasablePermissionFactory.getPermissionRequest( getCurrentAdjustmentsDetails() ).setUsageData( originalUsageData );
          
          throw priceCalculationException; 
        }
      }else
      {
        //Override fees given that quantities have not been changed
        currentAdjustment.setLicenseeFee( getAmountForFullCredit( getSubTotalLicenseeFee() ) );
        currentAdjustment.setRoyalty( getAmountForFullCredit( getSubTotalRoyalty() ) );
        currentAdjustment.setDiscount(getAmountForFullCredit( getSubTotalDiscount() ) );
        currentAdjustment.setRightholderFee( getAmountForFullCredit( getSubTotalRightholderFee() ) );
      }
      
      
      
    }
    
  }
  
  /**
   * Indicates if either the quantities or fees this body item's current adjustment have been modified.
   */ 
  public boolean isCurrentAdjustmentModified()
  {
    boolean isCurrentAdjustmentModified = areCurrentAdjustmentQuantitiesModified() || 
                                          areCurrentAdjustmentFeesModified();
                                          
    return isCurrentAdjustmentModified;
  }
  
  /**
   * Indicates if the quantities this body item's current adjustment have been modified (non-zero).
   */ 
  public boolean areCurrentAdjustmentQuantitiesModified()
  {
       
    PurchasablePermission currentAdjustment = getCurrentAdjustmentsDetails();
    
    boolean areQuantitiesModified = false;
    
    if( isTRS() )
    {
      areQuantitiesModified = currentAdjustment.getNumberOfCopies() != ADJUSTMENT_ZERO_QUANTITY ||
                              currentAdjustment.getNumberOfPages() != ADJUSTMENT_ZERO_QUANTITY;
    }
    
    if( isDPSEMail() )
    {
      areQuantitiesModified = currentAdjustment.getNumberOfRecipients() != ADJUSTMENT_ZERO_QUANTITY;
    }
    
    if( isDPSExtranet() || isDPSInternet() || isDPSIntranet() )
    {
      int currentAdjustmentDuration = currentAdjustment.getDuration();
      int originalDetailsDuration = getOriginalOrderDetails().getDuration();
      
      areQuantitiesModified = currentAdjustmentDuration != OrderAdjustmentConstants.UNSPECIFIED_DURATION &&
                              currentAdjustmentDuration != originalDetailsDuration;
    }
    
    if( isAPS() || isECCS() )
    {
      areQuantitiesModified = currentAdjustment.getNumberOfStudents() != ADJUSTMENT_ZERO_QUANTITY ||
                              currentAdjustment.getNumberOfPages() != ADJUSTMENT_ZERO_QUANTITY;
    }
    
    if( isRLS() )
    {
      areQuantitiesModified = currentAdjustment.getNumberOfPages() != ADJUSTMENT_ZERO_QUANTITY ||
                              currentAdjustment.getNumberOfCartoons() != ADJUSTMENT_ZERO_QUANTITY ||
                              currentAdjustment.getNumberOfCharts() != ADJUSTMENT_ZERO_QUANTITY ||
                              currentAdjustment.getNumberOfExcerpts() != ADJUSTMENT_ZERO_QUANTITY ||
                              currentAdjustment.getNumberOfFigures() != ADJUSTMENT_ZERO_QUANTITY ||
                              currentAdjustment.getNumberOfGraphs() != ADJUSTMENT_ZERO_QUANTITY ||
                              currentAdjustment.getNumberOfIllustrations() != ADJUSTMENT_ZERO_QUANTITY ||
                              currentAdjustment.getNumberOfLogos() != ADJUSTMENT_ZERO_QUANTITY ||
                              currentAdjustment.getNumberOfPhotos() != ADJUSTMENT_ZERO_QUANTITY ||
                              currentAdjustment.getNumberOfQuotes() != ADJUSTMENT_ZERO_QUANTITY;
                              //RLS's "Full Chapter Article" content type cannot be adjusted so it shouldn't change.
    }
    
    return areQuantitiesModified;
  }
  
  /**
   * Indicates if the fees this body item's current adjustment have been modified.
   */ 
  public boolean areCurrentAdjustmentFeesModified()
  {
    
    PurchasablePermission currentAdjustment = getCurrentAdjustmentsDetails();
    
    boolean isLicenseeFeeModified = currentAdjustment.getLicenseeFee() != ADJUSTMENT_ZERO_QUANTITY;
    boolean isRoyaltyModified = currentAdjustment.getRoyalty() != ADJUSTMENT_ZERO_QUANTITY;
    boolean isDiscountModified = currentAdjustment.getDiscount() != ADJUSTMENT_ZERO_QUANTITY;
    boolean isRighstholderFeeModified = currentAdjustment.getRightsholderFee() != ADJUSTMENT_ZERO_QUANTITY;
    
    boolean areFeesModified = isLicenseeFeeModified ||
                              isRoyaltyModified ||
                              isDiscountModified ||
                              isRighstholderFeeModified;
    
    return areFeesModified;
  }
  
  /**
   * Indicates if this body item has previous adjustments.
   */ 
  public boolean isPreviousAdjustmentsPresent()
  {
    return getPreviousAdjustmentsDetails().size() > 0;
  }
  
  
  
  /**
   * Recalculate the price of this body item's current adjustment.
   */
  public void recalculateCurrentAdjustmentPrice()
  throws OrderAdjustmentPriceCalculationException
  {
    UserContextService.checkPrivilege( CCPrivilegeCode.ENTER_ORDER_ADJUSTMENT );
    
    PurchasablePermission currentAdjustment = getCurrentAdjustmentsDetails();
    
    if( isCurrentAdjustmentFullyAdjusted() )
    {
      fullyAdjustCurrentAdjustmentFeesAndPrice();
      
      return;
      
    }
    
    boolean priceCalculationPerformed = false;
    
    //We only calculate prices (via quick price shared service invocation) 
    //when quantities have changed (different than zero)...    
    if ( areCurrentAdjustmentQuantitiesModified() ) 
    {
      
      if( currentAdjustment.isPhotocopy() )
      {
        priceCalculationPerformed = recalculatePhotocopyCurrentAdjustmentPrice();
      }
      
      if( currentAdjustment.isEmail() )
      {
        priceCalculationPerformed = recalculateEmailCurrentAdjustmentPrice();
      }
      
      if( currentAdjustment.isNet() )
      {
         priceCalculationPerformed = recalculateNetCurrentAdjustmentPrice();
      }
      
      if( currentAdjustment.isAcademic() )
      {
          priceCalculationPerformed = recalculateAcademicCurrentAdjustmentPrice();
      }

      if( currentAdjustment.isRepublication() )
      {
        priceCalculationPerformed = recalculateRepublicationCurrentAdjustmentPrice();
      }
    
    } else //When current adjustment quantities are zero we do not need to call quick price shared service...
    {
      //...so we make everything zero.
      makeAllFeesAndPriceZero( currentAdjustment );
    }
    
    //Now, we recalculate fees...
    //Licensee fee
    double originalLicenseeFee = currentAdjustment.getLicenseeFee();
    double newLicenseeFee = originalLicenseeFee;
    if ( priceCalculationPerformed )
    {
      newLicenseeFee = originalLicenseeFee - getSubTotalLicenseeFee();
    }
    currentAdjustment.setLicenseeFee( newLicenseeFee );


    //Royalty
    double originalRoyalty = currentAdjustment.getRoyalty();
    double newRoyalty = originalRoyalty;
    if ( priceCalculationPerformed )
    {
          newRoyalty = originalRoyalty - getSubTotalRoyalty();
      }
    currentAdjustment.setRoyalty( newRoyalty );
    
    //Rightsholder fee
    double originalRightsholderFee = currentAdjustment.getRightsholderFee();
    double newRightsholderFee = originalRightsholderFee;
    
    if ( priceCalculationPerformed )
    {
      newRightsholderFee = originalRightsholderFee - getSubTotalRightholderFee();
    }
    currentAdjustment.setRightholderFee( newRightsholderFee );

    //Discount
    double originalDiscount = currentAdjustment.getDiscount();
    double newDiscount = originalDiscount;
    
    if ( priceCalculationPerformed )
    {
      newDiscount = originalDiscount - getSubTotalDiscount();
    }
    currentAdjustment.setDiscount( newDiscount );

    //Overall price
    if ( priceCalculationPerformed )
    {
      PermissionRequest permissionRequest = PurchasablePermissionFactory.getPermissionRequest( currentAdjustment );
      double originalPriceAmount = permissionRequest.getPrice().getValue();
      double newPriceAmount = originalPriceAmount - getSubTotal();
      permissionRequest.setPrice( new Money( newPriceAmount ) );
    }
    
  }

  

   
  /**
   * Determines if this <code>OrderAdjustmentBodyItem</code> has previous adjustments.
   */
  public boolean hasPreviousAdjustments()
  {
    boolean hasPreviousAdjustments = getPreviousAdjustmentsDetails() != null &&
                                     !getPreviousAdjustmentsDetails().isEmpty();
                                     
    return hasPreviousAdjustments;                                     
  }


  /**
   * Determines if this <code>OrderAdjustmentBodyItem</code> can be adjusted.
   * An item can be adjusted only if both the original order detail and the
   * previous adjustments have been invoiced (have invoice IDs assigned to them).
   */
  public boolean isAdjustable()
  {
    
    boolean isOriginalDetailsCancelled = BILLING_STATUS_CANCELED.equalsIgnoreCase( getOriginalOrderDetails().getBillingStatus() );
    
    //Cancelled orders cannot be adjusted.
    if( isOriginalDetailsCancelled )
    {
      return false;
    }
    
    boolean originalDetailsHaveInvoiceID = StringUtils.isNotEmpty( getOriginalOrderDetails().getInvoiceId() );
    
    boolean previousAdjustmentsHaveInvoiceID = true;
    
    Iterator<OrderLicense> previousAdjustmentsIterator = getPreviousAdjustmentsDetails().iterator();
    
    while( previousAdjustmentsIterator.hasNext() )
    {
      OrderLicense previousAdjustment = previousAdjustmentsIterator.next();
      
      boolean previousAdjustmentDoesNotHaveInvoiceID = StringUtils.isEmpty( previousAdjustment.getInvoiceId() );
      
      if( previousAdjustmentDoesNotHaveInvoiceID )
      {
        previousAdjustmentsHaveInvoiceID = false;
        break;
      }  
    }
    
    boolean isAdjustable = originalDetailsHaveInvoiceID &&
                           previousAdjustmentsHaveInvoiceID;
    
    return isAdjustable;
  }


  /**
   * Returns the total of the current adjustment.
   */
  public double getCurrentAdjustmentTotal()
  {
    BigDecimal currentAdjustmentDiscount = new BigDecimal( getCurrentAdjustmentsDetails().getDiscount() );
    BigDecimal currentAdjustmentRightsholderFee = new BigDecimal( getCurrentAdjustmentsDetails().getRightsholderFee() );
    BigDecimal currentAdjustmentRoyalty = new BigDecimal( getCurrentAdjustmentsDetails().getRoyalty() );
    BigDecimal currentAdjustmentLicenseeFee = new BigDecimal( getCurrentAdjustmentsDetails().getLicenseeFee() );
    
    BigDecimal currentAdjustmentTotal = currentAdjustmentDiscount.add( currentAdjustmentRightsholderFee )
                                                                 .add( currentAdjustmentRoyalty )
                                                                 .add( currentAdjustmentLicenseeFee );

                                    
    return OrderAdjustmentUtils.roundToDefaultDecimalPlaces( currentAdjustmentTotal.doubleValue() );
  }
  
  void resetCurrentAdjustment()
  {
    PurchasablePermission newCurrentAdjustment = buildCurrentAdjustmentDetails( getOriginalOrderDetails() );
    this.setCurrentAdjustmentsDetails( newCurrentAdjustment );
  }
  
  
  ////////////////////////////////////
  //Private methods
  ////////////////////////////////////
   
  private long getAmountForFullCredit( long qty )
  {
    return 0 - qty;
  }
  
  
  private int getAmountForFullCredit( int qty )
  {
    return 0 - qty;
  }
  
  
  private double getAmountForFullCredit( double qty )
  {
    return 0 - qty;
  }
  
  
  private PurchasablePermission buildCurrentAdjustmentDetails( OrderLicense orderLicense )
  {
    PurchasablePermission currentAdjustmentsDetails = null;
    
    if (orderLicense instanceof OrderLicenseImpl )
    {
      PermissionRequest permissionRequest = new PermissionRequest( ((OrderLicenseImpl)orderLicense).getLicense() , true );
            
      permissionRequest = resetPermissionRequest( permissionRequest );
      
      currentAdjustmentsDetails = PurchasablePermissionFactory.createPurchasablePermission( permissionRequest );
    }else
    {
      throw new CCRuntimeException("Unable to build current adjustment details: incompatible order license found (" + orderLicense.getClass().getName() +")" );
    }
    
    return currentAdjustmentsDetails;
  }


  private PermissionRequest resetPermissionRequest( PermissionRequest permissionRequest )
  {
    UsageData oldUsageData = permissionRequest.getUsageData();
    UsageData newUsageData = null;
    
    if( oldUsageData instanceof UsageDataPhotocopy )
    {
      UsageDataPhotocopy usageDataPhotocopy = new UsageDataPhotocopy();
      usageDataPhotocopy.setNumCopies( 0L );
      usageDataPhotocopy.setNumPages( 0L );
      
      newUsageData = usageDataPhotocopy;
    }
    
    if( oldUsageData instanceof UsageDataEmail )
    {
      UsageDataEmail usageDataEmail = new UsageDataEmail();
      usageDataEmail.setNumRecipients( 0L );
      
      newUsageData = usageDataEmail;
    }
    
    if( oldUsageData instanceof UsageDataNet )
    {
      UsageDataNet usageDataNet = new UsageDataNet();
      usageDataNet.setDuration( OrderAdjustmentConstants.UNSPECIFIED_DURATION );
      
      newUsageData = usageDataNet;
    }
    
    if( oldUsageData instanceof UsageDataAcademic )
    {
      UsageDataAcademic usageDataAcademic = new UsageDataAcademic();
      usageDataAcademic.setNumPages( 0L );
      usageDataAcademic.setNumStudents( 0L );
      
      newUsageData = usageDataAcademic;
    }
    
    if( oldUsageData instanceof UsageDataRepublication )
    {
      UsageDataRepublication usageDataRepublication = new UsageDataRepublication();
      usageDataRepublication.setNumCartoons(0);
      usageDataRepublication.setNumCharts(0);
      usageDataRepublication.setNumExcerpts(0);
      usageDataRepublication.setNumFigures(0);
      usageDataRepublication.setNumGraphs(0);
      usageDataRepublication.setNumIllustrations(0);
      usageDataRepublication.setNumLogos(0);
      usageDataRepublication.setNumPhotos(0);
      usageDataRepublication.setNumQuotes(0);
      
      newUsageData = usageDataRepublication;
    }
    
    
    if ( newUsageData != null )
    {
      newUsageData.setIntExt( oldUsageData.getIntExt() );
      newUsageData.setTypeOfUseDisplay( oldUsageData.getTypeOfUseDisplay() );
      newUsageData.setProduct( oldUsageData.getProduct() );
      newUsageData.setTpuInst( oldUsageData.getTpuInst() );
      
      permissionRequest.setUsageData( newUsageData );
      
      permissionRequest.setDiscount( 0 );
      permissionRequest.setRoyaltyPayable( 0 );
      permissionRequest.setLicenseeFee( 0 );
      permissionRequest.setRightsholderFee( 0 );
      
      permissionRequest.setPrice( new Money(0) );
    }
  
    return permissionRequest;
  }


  double getPreviousAdjustmentsLicenseeFeeTotal()
  {
    return getPreviousAdjustmentsFeeTotals().getLicenseeFee();
  }


  double getPreviousAdjustmentsRoyaltyTotal()
  {
    return getPreviousAdjustmentsFeeTotals().getRoyalty();
  }


  double getPreviousAdjustmentsRoyaltyCompositeTotal()
  {
    return getPreviousAdjustmentsFeeTotals().getRoyaltyComposite();
  }


  double getPreviousAdjustmentsDiscountTotal()
  {
    return getPreviousAdjustmentsFeeTotals().getDiscount();
  }
  
  
  double getPreviousAdjustmentsRightsholderFeeTotal()
  {
    return getPreviousAdjustmentsFeeTotals().getRightsholderFee();
  }
  
  
  private boolean areCurrentAdjustmentQuantitiesNotModified()
  {
    return !areCurrentAdjustmentQuantitiesModified();
  }
        
    
  private void makeAllFeesAndPriceZero( PurchasablePermission purchasablePermission )
  {
    purchasablePermission.setRightholderFee(0);
    purchasablePermission.setRoyalty(0);
    purchasablePermission.setLicenseeFee(0);
    purchasablePermission.setDiscount(0);
    
    PermissionRequest permissionRequest = PurchasablePermissionFactory.getPermissionRequest(purchasablePermission);
    permissionRequest.setPrice( new Money(0) );
  }
    
    
  private FeeTotals getPreviousAdjustmentsFeeTotals()
  {
    BigDecimal prevAdjsLicenseeFeeTotal = new BigDecimal(0);
    BigDecimal prevAdjsRoyaltyTotal = new BigDecimal(0);
    BigDecimal prevAdjsRoyaltyCompositeTotal = new BigDecimal(0);
    BigDecimal prevAdjsDiscountTotal = new BigDecimal(0);
    BigDecimal prevAdjRightsholderFeeTotal = new BigDecimal(0);
    
    Iterator<OrderLicense> iterator = getPreviousAdjustmentsDetails().iterator();
    
    while( iterator.hasNext() )
    {
      OrderLicense currentPreviousAdjustment = iterator.next();
      
      if( currentPreviousAdjustment != null )
      {
        prevAdjsLicenseeFeeTotal = prevAdjsLicenseeFeeTotal.add( new BigDecimal( currentPreviousAdjustment.getLicenseeFee() ) ) ;
        prevAdjsRoyaltyTotal = prevAdjsRoyaltyTotal.add( new BigDecimal( currentPreviousAdjustment.getRoyalty() ));
        prevAdjsRoyaltyCompositeTotal = prevAdjsRoyaltyCompositeTotal.add( new BigDecimal( currentPreviousAdjustment.getRoyaltyComposite() ) ) ;
        prevAdjsDiscountTotal = prevAdjsDiscountTotal.add( new BigDecimal( currentPreviousAdjustment.getDiscount() )  );
        prevAdjRightsholderFeeTotal = prevAdjRightsholderFeeTotal.add( new BigDecimal( currentPreviousAdjustment.getRightsholderFee() ) );
      }
    }
    
    return new FeeTotals( prevAdjsLicenseeFeeTotal.doubleValue() , 
                          prevAdjsRoyaltyTotal.doubleValue(), 
                          prevAdjsRoyaltyCompositeTotal.doubleValue(), 
                          prevAdjsDiscountTotal.doubleValue(), 
                          prevAdjRightsholderFeeTotal.doubleValue() ) ;
  }
  
  
  private void processOrderAdjustmentPriceCalculationError( Exception e ) 
  throws OrderAdjustmentPriceCalculationException
  {
    throw new OrderAdjustmentPriceCalculationException( "Exception thrown by shared services: " + e.getClass().getName() , e);
  }
  
  
  private UsageData getDeepCopyOfCurrentAdjustmentUsageData()
  {
            UsageData originalUsageData = PurchasablePermissionFactory.getPermissionRequest( getCurrentAdjustmentsDetails() ).getUsageData();
            
            Object copiedUsageData = null;
            ObjectOutputStream oos = null;
            ObjectInputStream ois = null;

            try
            {
               ByteArrayOutputStream bos = new ByteArrayOutputStream();

               oos = new ObjectOutputStream(bos);

               // serialize this object to the output stream
               oos.writeObject( originalUsageData );
               oos.flush();

               // read in a new object instance from the serialized one
               ByteArrayInputStream bin = new ByteArrayInputStream(bos.toByteArray());
               ois = new ObjectInputStream( bin );

               copiedUsageData = ois.readObject();

               oos.close();
               ois.close();

            }

            // Mainly - this will be java.io.NotSerializableException
            // but could be a handfull of other java.io exceptions.
            // We could return a null object, but ... ?
            catch( Exception e )
            {
           	    _logger.error( ExceptionUtils.getFullStackTrace(e) );
                throw new OrderAdjustmentException( e.getMessage(), e.getCause() );
            }

            return (UsageData) copiedUsageData;
    
  }
  
  private boolean isCurrentAdjustmentFullyAdjusted()
  {
    boolean isCurrentAdjustmentFullyAdjusted = false;
    
    PurchasablePermission currentAdjustment = getCurrentAdjustmentsDetails();
    
    boolean isInvalidProduct = !( currentAdjustment.isPhotocopy() ||
                                  currentAdjustment.isEmail() ||
                                  currentAdjustment.isNet() ||
                                  currentAdjustment.isAcademic() ||
                                  currentAdjustment.isRepublication() 
                                );
                             
    if( isInvalidProduct )
    {
      throw new IllegalStateException( "Inavlid product for current adjustment" );
    }
    
    if( currentAdjustment.isPhotocopy() )
    {
      boolean isFullyAdjustedNumCopies = getTotalNumberOfCopies() == ADJUSTMENT_ZERO_QUANTITY;
      boolean isFullyAdjustedNumPages =  getTotalNumberOfPages() == ADJUSTMENT_ZERO_QUANTITY;
      
      isCurrentAdjustmentFullyAdjusted = isFullyAdjustedNumCopies || isFullyAdjustedNumPages;
    }
    
    if( currentAdjustment.isEmail() )
    {
      boolean isFullyAdjustedNumRecipients = getTotalNumberOfRecipients() == ADJUSTMENT_ZERO_QUANTITY;
      
      isCurrentAdjustmentFullyAdjusted = isFullyAdjustedNumRecipients;
    }
    
    if( currentAdjustment.isNet() )
    {
       //Net products are never fully adjusted
       isCurrentAdjustmentFullyAdjusted = false;
    }
    
    if( currentAdjustment.isAcademic() )
    {
      boolean isFullyAdjustedNumPages =  getTotalNumberOfPages() == ADJUSTMENT_ZERO_QUANTITY;
      boolean isFullyAdjustedNumStudents = getTotalNumberOfStudents() == ADJUSTMENT_ZERO_QUANTITY;
      
      isCurrentAdjustmentFullyAdjusted = isFullyAdjustedNumPages || isFullyAdjustedNumStudents;
    }
    
    if( currentAdjustment.isRepublication() )
    {
      boolean isFullyAdjustedNumCartoon = getTotalNumberOfCartoons() == ADJUSTMENT_ZERO_QUANTITY;
      boolean isFullyAdjustedNumCharts = getTotalNumberOfCharts() == ADJUSTMENT_ZERO_QUANTITY;
      boolean isFullyAdjustedNumExcerpts = getTotalNumberOfExcerpts() == ADJUSTMENT_ZERO_QUANTITY;
      boolean isFullyAdjustedNumFigures = getTotalNumberOfFigures() == ADJUSTMENT_ZERO_QUANTITY;
      boolean isFullyAdjustedNumGraphs = getTotalNumberOfGraphs() == ADJUSTMENT_ZERO_QUANTITY;
      boolean isFullyAdjustedNumIllustrations = getTotalNumberOfIllustrations() == ADJUSTMENT_ZERO_QUANTITY;
      boolean isFullyAdjustedNumLogos = getTotalNumberOfLogos() == ADJUSTMENT_ZERO_QUANTITY;
      boolean isFullyAdjustedNumPhotos = getTotalNumberOfPhotos() == ADJUSTMENT_ZERO_QUANTITY;
      boolean isFullyAdjustedNumPages = getTotalNumberOfPages() == ADJUSTMENT_ZERO_QUANTITY;
      boolean isFullyAdjustedNumQuotes = getTotalNumberOfQuotes() == ADJUSTMENT_ZERO_QUANTITY;
      
      isCurrentAdjustmentFullyAdjusted = isFullyAdjustedNumCartoon && 
                                         isFullyAdjustedNumCharts && 
                                         isFullyAdjustedNumExcerpts && 
                                         isFullyAdjustedNumFigures && 
                                         isFullyAdjustedNumGraphs && 
                                         isFullyAdjustedNumIllustrations && 
                                         isFullyAdjustedNumLogos && 
                                         isFullyAdjustedNumPages && 
                                         isFullyAdjustedNumPhotos && 
                                         isFullyAdjustedNumQuotes;
    }
    
    return isCurrentAdjustmentFullyAdjusted;
  }
  
  private boolean recalculatePhotocopyCurrentAdjustmentPrice() 
  throws OrderAdjustmentPriceCalculationException
  {
    PurchasablePermission currentAdjustment = getCurrentAdjustmentsDetails();
    boolean priceCalculationPerformed = false;
    
    long originalNumberOfCopies = currentAdjustment.getNumberOfCopies();
    long originalNumberOfPages = currentAdjustment.getNumberOfPages();
    
    long newNumberOfCopies = getSubTotalNumberOfCopies() + originalNumberOfCopies;
    currentAdjustment.setNumberOfCopies( newNumberOfCopies );
    
    
    long newNumberOfPages = getSubTotalNumberOfPages() + originalNumberOfPages;
    currentAdjustment.setNumberOfPages( newNumberOfPages );
    
    
    try
    {
      currentAdjustment = PricingServices.updateItemPriceForLicenseeID( currentAdjustment, _originalLicenseePartyId );
      priceCalculationPerformed = true;
    } catch ( Exception e )
    {
      currentAdjustment.setNumberOfCopies(originalNumberOfCopies);
      currentAdjustment.setNumberOfPages(originalNumberOfPages);
      
      processOrderAdjustmentPriceCalculationError( e );
    }
    
    //TODO lalberione 05/21/2007 Should we put these 2 statements in a finally block?
    currentAdjustment.setNumberOfCopies(originalNumberOfCopies);
    currentAdjustment.setNumberOfPages(originalNumberOfPages);
    
    return priceCalculationPerformed;
  }
  
  private boolean recalculateEmailCurrentAdjustmentPrice() 
  throws OrderAdjustmentPriceCalculationException
  {
    PurchasablePermission currentAdjustment = getCurrentAdjustmentsDetails();
    boolean priceCalculationPerformed = false;
    
    long originalNumberOfRecipients = currentAdjustment.getNumberOfRecipients();
    
    long newNumberOfRecipients = getSubTotalNumberOfRecipients() + originalNumberOfRecipients;
    currentAdjustment.setNumberOfRecipients( newNumberOfRecipients );
    
    try
    {
      currentAdjustment = PricingServices.updateItemPriceForLicenseeID( currentAdjustment, _originalLicenseePartyId );
      priceCalculationPerformed = true;
    } catch ( Exception e )
    {
      currentAdjustment.setNumberOfRecipients(originalNumberOfRecipients);
      
      processOrderAdjustmentPriceCalculationError( e );
    }
    currentAdjustment.setNumberOfRecipients(originalNumberOfRecipients);
    
    return priceCalculationPerformed;
  }
  
  private boolean recalculateNetCurrentAdjustmentPrice() 
  throws OrderAdjustmentPriceCalculationException
  {
    PurchasablePermission currentAdjustment = getCurrentAdjustmentsDetails();
    boolean priceCalculationPerformed = false;
    
    //We do not need to calculate differences for Net products
    try
    {
      PricingServices.updateItemPriceForLicenseeID( currentAdjustment, _originalLicenseePartyId );
      priceCalculationPerformed = true;
    } catch ( Exception e )
    {
      processOrderAdjustmentPriceCalculationError( e );
    }
    
    return priceCalculationPerformed;
  }
  
  private boolean recalculateAcademicCurrentAdjustmentPrice() 
  throws OrderAdjustmentPriceCalculationException
  {
    PurchasablePermission currentAdjustment = getCurrentAdjustmentsDetails();
    boolean priceCalculationPerformed = false;
    
    long originalNumberOfPages = currentAdjustment.getNumberOfPages();
    int originalNumberOfStudents = currentAdjustment.getNumberOfStudents();
    
    long newNumberOfPages = getSubTotalNumberOfPages() + originalNumberOfPages;
    currentAdjustment.setNumberOfPages( newNumberOfPages );
    
    
    int newNumberOfStudents = getSubTotalNumberOfStudents() + originalNumberOfStudents;
    currentAdjustment.setNumberOfStudents( newNumberOfStudents );
    
    try
    {
      currentAdjustment = PricingServices.updateItemPriceForLicenseeID( currentAdjustment, _originalLicenseePartyId );
      priceCalculationPerformed = true;
    } catch ( Exception e )
    {
      currentAdjustment.setNumberOfPages(originalNumberOfPages);
      currentAdjustment.setNumberOfStudents(originalNumberOfStudents);
      
      processOrderAdjustmentPriceCalculationError( e );
    }
            
    currentAdjustment.setNumberOfPages(originalNumberOfPages);
    currentAdjustment.setNumberOfStudents(originalNumberOfStudents);
    
    return priceCalculationPerformed;
  }
  
  private boolean recalculateRepublicationCurrentAdjustmentPrice()
  throws OrderAdjustmentPriceCalculationException
  {
    PurchasablePermission currentAdjustment = getCurrentAdjustmentsDetails();
    boolean priceCalculationPerformed = false;
    
    long originalNumberOfPages = currentAdjustment.getNumberOfPages();
    long originalNumberOfCartoons = currentAdjustment.getNumberOfCartoons();
    long originalNumberOfCharts = currentAdjustment.getNumberOfCharts();
    long originalNumberOfExcerpts = currentAdjustment.getNumberOfExcerpts();
    long originalNumberOfFigures = currentAdjustment.getNumberOfFigures();
    long originalNumberOfGraphs = currentAdjustment.getNumberOfGraphs();
    long originalNumberOfIllustrations = currentAdjustment.getNumberOfIllustrations();
    long originalNumberOfLogos = currentAdjustment.getNumberOfLogos();
    long originalNumberOfPhotos = currentAdjustment.getNumberOfPhotos();
    long originalNumberOfQuotes = currentAdjustment.getNumberOfQuotes();
    
    long newNumberOfPages = getSubTotalNumberOfPages() + originalNumberOfPages;
    currentAdjustment.setNumberOfPages( newNumberOfPages );
    
    long newNumberOfCartoons = getSubTotalNumberOfCartoons() + originalNumberOfCartoons;
    currentAdjustment.setNumberOfCartoons(newNumberOfCartoons);
    
    long newNumberOfCharts = getSubTotalNumberOfCharts() + originalNumberOfCharts;
    currentAdjustment.setNumberOfCharts(newNumberOfCharts);
    
    long newNumberOfExcerpts = getSubTotalNumberOfExcerpts() + originalNumberOfExcerpts;
    currentAdjustment.setNumberOfExcerpts(newNumberOfExcerpts);
    
    long newNumberOfFigures = getSubTotalNumberOfFigures() + originalNumberOfFigures;
    currentAdjustment.setNumberOfFigures(newNumberOfFigures);
    
    long newNumberOfGraphs = getSubTotalNumberOfGraphs() + originalNumberOfGraphs;
    currentAdjustment.setNumberOfGraphs(newNumberOfGraphs);
    
    long newNumberOfIllustrations = getSubTotalNumberOfIllustrations() + originalNumberOfIllustrations;
    currentAdjustment.setNumberOfIllustrations(newNumberOfIllustrations);
    
    long newNumberOfLogos = getSubTotalNumberOfLogos() + originalNumberOfLogos;
    currentAdjustment.setNumberOfLogos(newNumberOfLogos);
    
    long newNumberOfPhotos = getSubTotalNumberOfPhotos() + originalNumberOfPhotos;
    currentAdjustment.setNumberOfPhotos(newNumberOfPhotos);
    
    long newNumberOfQuotes = getSubTotalNumberOfQuotes() + originalNumberOfQuotes;
    currentAdjustment.setNumberOfQuotes(newNumberOfQuotes);
    
    try
    {
      currentAdjustment = PricingServices.updateItemPriceForLicenseeID( currentAdjustment, _originalLicenseePartyId );
      priceCalculationPerformed = true;
    } catch ( Exception e )
    {
      currentAdjustment.setNumberOfPages(originalNumberOfPages);
      currentAdjustment.setNumberOfCartoons(originalNumberOfCartoons);
      currentAdjustment.setNumberOfCharts(originalNumberOfCharts);
      currentAdjustment.setNumberOfExcerpts(originalNumberOfExcerpts);
      currentAdjustment.setNumberOfFigures(originalNumberOfFigures);
      currentAdjustment.setNumberOfGraphs(originalNumberOfGraphs);
      currentAdjustment.setNumberOfIllustrations(originalNumberOfIllustrations);
      currentAdjustment.setNumberOfLogos(originalNumberOfLogos);
      currentAdjustment.setNumberOfPhotos(originalNumberOfPhotos);
      currentAdjustment.setNumberOfQuotes(originalNumberOfQuotes);
      
      processOrderAdjustmentPriceCalculationError( e );
    }
    
      currentAdjustment.setNumberOfPages(originalNumberOfPages);
      currentAdjustment.setNumberOfCartoons(originalNumberOfCartoons);
      currentAdjustment.setNumberOfCharts(originalNumberOfCharts);
      currentAdjustment.setNumberOfExcerpts(originalNumberOfExcerpts);
      currentAdjustment.setNumberOfFigures(originalNumberOfFigures);
      currentAdjustment.setNumberOfGraphs(originalNumberOfGraphs);
      currentAdjustment.setNumberOfIllustrations(originalNumberOfIllustrations);
      currentAdjustment.setNumberOfLogos(originalNumberOfLogos);
      currentAdjustment.setNumberOfPhotos(originalNumberOfPhotos);
      currentAdjustment.setNumberOfQuotes(originalNumberOfQuotes);
    
    return priceCalculationPerformed;
  }
  
  
  private void fullyAdjustCurrentAdjustmentFeesAndPrice()
  {
    PurchasablePermission currentAdjustment = getCurrentAdjustmentsDetails();
    
    currentAdjustment.setLicenseeFee( getAmountForFullCredit( getSubTotalLicenseeFee() ) );
    currentAdjustment.setRightholderFee( getAmountForFullCredit( getSubTotalRightholderFee() ) );
    currentAdjustment.setRoyalty( getAmountForFullCredit( getSubTotalRoyalty() ) );
    currentAdjustment.setDiscount( getAmountForFullCredit( getSubTotalDiscount() ) );

    PermissionRequest permissionRequest = PurchasablePermissionFactory.getPermissionRequest( currentAdjustment );
    
    BigDecimal licenseeFee = new BigDecimal( currentAdjustment.getLicenseeFee() );
    BigDecimal rightsholderFee = new BigDecimal( currentAdjustment.getRightsholderFee() );
    BigDecimal royalty = new BigDecimal( currentAdjustment.getRoyalty() );
    BigDecimal discount = new BigDecimal( currentAdjustment.getDiscount() );
    
    BigDecimal newPrice = licenseeFee.add( rightsholderFee ).add( royalty ).add( discount );
    
    Money newPriceMoney = new Money( OrderAdjustmentUtils.roundToDefaultDecimalPlaces( newPrice.doubleValue() ) );
    
    permissionRequest.setPrice( newPriceMoney );
  }
  
  private static class FeeTotals
  {
    private double _licenseeFee = 0;
    private double _royalty = 0;
    private double _royaltyComposite = 0;
    private double _discount = 0;
    private double _rightsholderFee = 0;
    
    private FeeTotals(){}
    
    FeeTotals( double licenseeFee, double royalty, double royaltyComposite, double discount, double rightsholderFee )
    {
      _licenseeFee = licenseeFee;
      _royaltyComposite = royaltyComposite;
      _royalty = royalty;
      _discount = discount;
      _rightsholderFee = rightsholderFee;
    }
    
    double getLicenseeFee()
    {
      return _licenseeFee;
    }
    
    double getRoyalty()
    {
      return _royalty;
    }
    
    double getRoyaltyComposite()
    {
      return _royaltyComposite;
    }
    
    double getDiscount()
    {
      return _discount;
    }
    
    double getRightsholderFee()
    {
      return _rightsholderFee;
    }
    
  }
}
