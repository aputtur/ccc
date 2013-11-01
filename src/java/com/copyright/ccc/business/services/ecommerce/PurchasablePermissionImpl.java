package com.copyright.ccc.business.services.ecommerce;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.copyright.base.CCCRuntimeException;
import com.copyright.base.Constants;
import com.copyright.ccc.CCRuntimeException;
import com.copyright.ccc.business.data.WRStandardWork;
import com.copyright.ccc.business.services.ItemAvailabilityEnum;
import com.copyright.ccc.business.services.ItemConstants;
import com.copyright.ccc.business.services.ProductEnum;
import com.copyright.ccc.business.services.RightSourceEnum;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.business.services.cart.ECommerceConstants;
import com.copyright.ccc.business.services.cart.ECommerceUtils;
import com.copyright.ccc.business.services.search.RightAdapter;
import com.copyright.ccc.web.transaction.RepublicationConstants;
import com.copyright.ccc.web.transaction.TransactionConstants;
import com.copyright.ccc.web.util.WebUtils;
import com.copyright.data.account.Address;
import com.copyright.data.inventory.StandardWork;
import com.copyright.data.inventory.Work;
import com.copyright.data.order.PermissionRequest;
import com.copyright.data.order.UsageData;
import com.copyright.data.order.UsageDataAcademic;
import com.copyright.data.order.UsageDataEmail;
import com.copyright.data.order.UsageDataNet;
import com.copyright.data.order.UsageDataPhotocopy;
import com.copyright.data.order.UsageDataRepublication;
import com.copyright.data.pricing.RightFees;
import com.copyright.service.pricing.PricingServiceAPI;
import com.copyright.service.pricing.PricingServiceFactory;
import com.copyright.svc.rightsResolver.api.data.TouCategoryTypeEnum;
import com.copyright.svc.tf.api.TFService;
import com.copyright.svc.tf.api.data.TFConsumerContext;
import com.copyright.workbench.i18n.Money;

/*  Change Log
 *  ==========
 *  Please list your changes here.  If you list them most
 *  recent at the top, it saves scanning down the notes to
 *  find out what was most recently changed.
 *
 *  Date        Initials    What Changed
 *  ----------  --------    ------------------------------------------
 *  2009-08-27  MSJ         Modified the get/setTypeOfContent.  Added
 *                          debug statements and changed set code, it
 *                          was not actually setting the content type
 *                          for some reason.
 */

/**
 * Implementation of <code>PurchasablePermission</code>
 */
final class PurchasablePermissionImpl extends PurchasablePermission
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

private static final Logger _logger = Logger.getLogger( PurchasablePermissionImpl.class );

  private static final int RLS_PAGES_ZERO_QUANTITY = 0;
  private static final long INVALID_RIGHTSHOLDER_INST = -1L;

  private PermissionRequest _permissionRequest;
  
  private int       _duration = ECommerceConstants.INVALID_DURATION;
  private String    _webAddress = Constants.EMPTY_STRING;
  private long      _numberOfRecipients = ECommerceConstants.INVALID_NUMBER_OF_RECIPIENTS;
  private String    _rightsholder = Constants.EMPTY_STRING;
  private String    _rightsQualifyingStatement = Constants.EMPTY_STRING;
  private Date      _publicationStartDate = null;
  private Date      _publicationEndDate = null;
  private RightFees _fees = null;
  private long      _rightsholderInst = INVALID_RIGHTSHOLDER_INST;
  private String    _externalCommentTerm = null;
  private boolean   pushToTFFailed = false;
  private RightAdapter  rightAdapter;
  
  private PurchasablePermissionImpl(){}
  
  PurchasablePermissionImpl( PermissionRequest permissionRequest )
  {
    setPermissionRequest( permissionRequest );
    
     TFService tfService = ServiceLocator.getTFService();
    
       if (permissionRequest.getRight() != null) {
       // if (permissionRequest.getRight().getRightsholder() != null) {
            setRightsholder(tfService.getOrgRightsholderPartyByPartyId
                    ( new TFConsumerContext(), permissionRequest.getRight().getRightsHolderPartyId()).getOrgName());
       // }
        if (permissionRequest.getRight().getExternalCommentTerm() != null) {
            setExternalCommentTerm(permissionRequest.getRight().getExternalCommentTerm().getTermText());
        }
        if (permissionRequest.getRight().getRightQualifierTerm()!= null) {
            setRightsQualifyingStatement(permissionRequest.getRight().getRightQualifierTerm().getTermText());
        }
        //setRightsholderInst(permissionRequest.getRight().getRightsHolderInst());
        setPublicationStartDate(permissionRequest.getRight().getPublicationBeginDate());
        setPublicationEndDate(permissionRequest.getRight().getPublicationEndDate());
    }
    calculateRightFees();
    
  }

  

  public String getPublicationTitle()
  {
    String publicationTitle = getPermissionRequest().getWork().getMainTitle();
    
    return publicationTitle;
  }
  
  //  2010-01-07  MSJ
  //  Truncating title string.  Can't be more than 255 or
  //  an error occurs when persisting the order.
  
  public void setPublicationTitle(String publicationTitle)
  {
      String pt = publicationTitle;
      pt = pt != null && pt.length() > 255 ? pt.substring(0, 255) : pt;
      getPermissionRequest().getWork().setMainTitle(StringUtils.upperCase(pt));
  }

  public String getItemDescription()
  {
    String publicationTitle = getPermissionRequest().getWork().getMainTitle();
    
    return publicationTitle;
  }
  
  public String getItemTypeCd() {
	  return null;
  }
  
  public void setItemTypeCd(String itemTypeCd) {
	  // Needed to meet interface
  }

  
  //  2010-01-07  MSJ
  //  Truncating title string.  Can't be more than 255 or
  //  an error occurs when persisting the order.
  
  public void setItemDescription(String publicationTitle)
  {
      String pt = publicationTitle;
      pt = pt != null && pt.length() > 255 ? pt.substring(0, 255) : pt;
      getPermissionRequest().getWork().setMainTitle(StringUtils.upperCase(pt));
  }
  
  public String getItemSubDescription()
  {
    return null;
  }
  
  //  2010-01-07  MSJ
  //  Truncating title string.  Can't be more than 255 or
  //  an error occurs when persisting the order.
  
  public void setItemSubDescription(String publicationTitle)
  {
	  // Needed for signature, not for TF items
  }
  
  public String getItemSourceCd() {
		return null;
  }
	
  public void setItemSourceCd(String itemSourceCd) {
  	  // Needed for signature, not for TF items
  }

  public Long getItemSourceKey() {
	return null;
  }

  public void setItemSourceKey(Long itemSourceKey) {
  	  // Needed for signature, not for TF items
  }

  public String getItemSubSourceCd() {
		return null;
  }
	
  public void setItemSubSourceCd(String itemSubSourceCd) {
  	  // Needed for signature, not for TF items
  }

  public Long getItemSubSourceKey() {
	return null;
  }

  public void setItemSubSourceKey(Long itemSubSourceKey) {
  	  // Needed for signature, not for TF items
  }
  
  public String getGranularWorkAuthor() {
	  return null;
  }

  public void setGranularWorkAuthor(String granularWorkAuthor) {
	  // Needed for signature, not for TF items
  }
	    
  public String getGranularWorkDoi() {
	  return null;
  }

  public void setGranularWorkDoi(String granularWorkDoi) {
	  // Needed for signature, not for TF items
  }

  public String getGranularWorkStartPage() {
	  return null;
  }

  public void setGranularWorkStartPage(String granularWorkStartPage) {
	  // Needed for signature, not for TF items
  }

  public String getGranularWorkEndPage() {
	  return null;
  }

  public void setGranularWorkEndPage(String granularWorkEndPage) {
	  // Needed for signature, not for TF items
  }

  public String getGranularWorkPageRange() {
	  return null;
  }

  public void setGranularWorkPageRange(String granularWorkPageRange) {
	  // Needed for signature, not for TF items
  }

  public Date getGranularWorkPublicationDate() {
	  return null;
  }

  public void setGranularWorkPublicationDate(Date granularWorkPublicationDate) {
	  // Needed for signature, not for TF items
  }

  public String getGranularWorkVolume() {
	  return null;
  }

  public void setGranularWorkVolume(String granularWorkVolume) {
	  // Needed for signature, not for TF items
  }
	    
  public String getGranularWorkIssue() {
	  return null;
  }

  public void setGranularWorkIssue(String granularWorkIssue) {
	  // Needed for signature, not for TF items
  }

  public String getGranularWorkNumber() {
	  return null;
  }

  public void setGranularWorkNumber(String granularWorkNumber) {
	  // Needed for signature, not for TF items
  }

  public String getGranularWorkSeason() {
	  return null;
  }

  public void setGranularWorkSeason(String granularWorkSeason) {
	  // Needed for signature, not for TF items
  }

  public String getGranularWorkQuarter() {
	  return null;
  }

  public void setGranularWorkQuarter(String granularWorkQuarter) {
	  // Needed for signature, not for TF items
  }
  
  public String getGranularWorkWeek() {
	  return null;
  }

  public void setGranularWorkWeek(String granularWorkWeek) {
	  // Needed for signature, not for TF items
  }

  public String getGranularWorkSection() {
	  return null;
  }

  public void setGranularWorkSection(String granularWorkSection) {
	  // Needed for signature, not for TF items
  }

  
  public String getStandardNumber()
  {
    String standardNumber = getPermissionRequest().getWork().getStandardNumber();
    
    return standardNumber;
  }
  
  public void setStandardNumber(String standardNumber)
  {
    getPermissionRequest().getWork().setStandardNumber( standardNumber );
  }
  
  public Long getWorkInst()
  {
    return new Long(getPermissionRequest().getWork().getWrkInst());
  }
  
  public void setWorkInst(Long wrkInst)
  {
		if (wrkInst != null) {
			getPermissionRequest().getWork().setWrkInst(wrkInst.longValue());
		} else {
			getPermissionRequest().getWork().setWrkInst(0L);
		}
	  
  }
  
  public Work getWork() {
      
	  WRStandardWork work = new WRStandardWork();
	  
      work.setWrkInst( this.getWorkInst() );
      work.setStandardNumber( this.getStandardNumber() );
      work.setMainTitle( this.getPublicationTitle() );
      work.setMainAuthor( this.getAuthor() );
      work.setMainEditor( this.getEditor());
      work.setVolume( this.getVolume() );
      work.setEdition( this.getEdition() );
      work.setPublisher( this.getPublisher() );
//      work.setTfWksInstList(this.getTfWksInstList()); 
      work.setWrwrkinst(this.getWrWorkInst());
      
     
      work.setSeries(this.getSeries());
      work.setSeriesNumber(this.getSeriesNumber());
      work.setPublicationType(this.getPublicationType());
      work.setCountry(this.getCountry());
      work.setLanguage(this.getLanguage());
      work.setIdnoLabel(this.getIdnoLabel());
      work.setPages(this.getPages());
	  
	  return work;

  }

  public Long getExternalItemId()
  {
    return Long.valueOf(getPermissionRequest().getWork().getWrkInst());
  }
  
  public void setExternalItemId(Long wrkInst)
  {
    getPermissionRequest().getWork().setWrkInst(wrkInst.longValue());
  }
  
  
  public Long getWrWorkInst() {
      //if this is just a Standard Work object, then we need to create a WrStandardWork object and replace this in
      //the permissionRequest object
      if (!(getPermissionRequest().getWork() instanceof WRStandardWork)) {
           WRStandardWork wrStandardWork = PurchasablePermissionFactory.buildWRWork(((StandardWork)getPermissionRequest().getWork()));
           getPermissionRequest().setWork(wrStandardWork);
           return new Long(wrStandardWork.getWrWrkInst());
      }
      return new Long(((WRStandardWork)getPermissionRequest().getWork()).getWrWrkInst());
  }

  public long getRgtInst()
  {  
    return getPermissionRequest().getRgtInst();
  }
 
  public Long getRightId()
  {  
    return Long.valueOf(getPermissionRequest().getRgtInst());
  }

  public Long getExternalRightId() {
	    return Long.valueOf(getPermissionRequest().getRgtInst());
  }

  public String getPublisher()
  {
    String publisher = getPermissionRequest().getWork().getPublisher();
    
    return publisher;
  }
  
  public void setPublisher(String publisher)
  {
    getPermissionRequest().getWork().setPublisher( publisher );
  }
  
  public String getPublicationYear()
  {
    String publicationYear = getPermissionRequest().getWork().getPublicationYear();
    
    return publicationYear;
  }

  public void setPublicationYear(String publicationYear)
  {
    getPermissionRequest().getWork().setPublicationYear( publicationYear );
  }
  
    //  2009-03-04  MSJ
    //  In the case of the card, this structure is built from the
    //  permissionRequest.  Since all of the right-related fields
    //  are returned from THIS right, right information cannot be
    //  seen (at least not the information I need[ed]) in the cart.
    //  Because the two types of right data structures are not
    //  perfectly compatible, I decided to simply make the changes
    //  per method instead of messing with this method, which is
    //  used all over the place.  Seems counter-intuitive, but...
    //  also safer in this case.
    
    @Override
    public RightAdapter getRightFromWeb() 
    {     
    	if (rightAdapter == null ) return null;
        return rightAdapter;

    }

  public String getItemAvailabilityCd()
  {
	    String availabilityCd = null;
	    	    
	    if ( getRightFromWeb()!=null )
	    {
	      ItemAvailabilityEnum itemAvailabilityEnum = ItemAvailabilityEnum.getEnumForTFPermissionCd(getRightFromWeb().getPermission().getCode());
	      if (itemAvailabilityEnum != null) {
		      availabilityCd = itemAvailabilityEnum.getStandardPermissionCd();
	      }
	    }	    
	    
	    return availabilityCd;
  }
  
  public String getItemAvailabilityDescription()
  {
	  
	  ItemAvailabilityEnum itemAvailabilityEnum = ItemAvailabilityEnum.getEnumForItemAvailabilityCd(this.getItemAvailabilityCd());
	  if (itemAvailabilityEnum != null) {
		  return itemAvailabilityEnum.getExternalOrderLabel();
	  }
	  return null;
  }
  
  public String getItemOrigAvailabilityCd() {  
	    return getItemAvailabilityCd();
  }

  public String getItemOrigAvailabilityDescription() {
  	  	return getItemAvailabilityDescription();
  }

  public String getItemAvailabilityDescriptionInternal()
  {
	  ItemAvailabilityEnum itemAvailabilityEnum = ItemAvailabilityEnum.getEnumForItemAvailabilityCd(this.getItemAvailabilityCd());
	  if (itemAvailabilityEnum != null) {
		  return itemAvailabilityEnum.getOrderLabel();
	  }
	  return null;
	  
  }
  
  public String getItemOrigAvailabilityDescriptionInternal() {
	  return getItemAvailabilityDescriptionInternal();
  }

  public void setItemOrigAvailabilityCd(String itemOrigAvailabilityCd)
  {
    	// Does not happen here for TF
  }
  
  public String getRightsholder()
  {
    String rightsholder = _rightsholder;
    
    if ( getRightFromWeb()!=null )
    {
      rightsholder = getRightFromWeb().getRightsholderName();
    }
    
    return rightsholder;
  }
  
  void setRightsholder( String rightsholder )
  {
    _rightsholder = rightsholder;
  }

    public String getRightsQualifyingStatement()
    {    
        String rightsQualifyingStatement = _rightsQualifyingStatement;
        
        boolean rightQualifyingTermPresentInPermissionRequest = getRightFromWeb() != null && 
                                                                getRightFromWeb().getQualifyingStmt() != null;
        
        if ( rightQualifyingTermPresentInPermissionRequest )
        {
            rightsQualifyingStatement = getRightFromWeb().getQualifyingStmt();
        }
        else {
            //  2009-03-04  MSJ
            //  We need to check the permissionRequest object as well,
            //  in the case of the cart, the comment is not present in
            //  the other two places it exists in this class.
            try {
                if (rightsQualifyingStatement == null) {
                    if (_permissionRequest != null && _permissionRequest.getRight() != null) {
                        _rightsQualifyingStatement = _permissionRequest.getRight().getRightQualifierTerm().getTermText();
                        rightsQualifyingStatement = _rightsQualifyingStatement;
                        
                        try {
                            _logger.debug("RightsQualifyingStatement in PPI: " + rightsQualifyingStatement);
                        }
                        catch(Exception e) {
                            _logger.debug("RightsQualifyingStatement in PPI: null");
                        }
                    }
                }
            }
            catch (Exception e) {
                _logger.debug("OUCH!", e);   
            }
        }
        
        return rightsQualifyingStatement;
    }

    public String getRightQualifierTerms()
    {    
        String rightsQualifyingStatement = _rightsQualifyingStatement;
        
        boolean rightQualifyingTermPresentInPermissionRequest = getRightFromWeb() != null && 
                                                                getRightFromWeb().getQualifyingStmt() != null;
        
        if ( rightQualifyingTermPresentInPermissionRequest )
        {
            rightsQualifyingStatement = getRightFromWeb().getQualifyingStmt();
        }
        else {
            //  2009-03-04  MSJ
            //  We need to check the permissionRequest object as well,
            //  in the case of the cart, the comment is not present in
            //  the other two places it exists in this class.
            try {
                if (rightsQualifyingStatement == null) {
                    if (_permissionRequest != null && _permissionRequest.getRight() != null) {
                        _rightsQualifyingStatement = _permissionRequest.getRight().getRightQualifierTerm().getTermText();
                        rightsQualifyingStatement = _rightsQualifyingStatement;
                        
                        try {
                            _logger.debug("RightsQualifyingStatement in PPI: " + rightsQualifyingStatement);
                        }
                        catch(Exception e) {
                            _logger.debug("RightsQualifyingStatement in PPI: null");
                        }
                    }
                }
            }
            catch (Exception e) {
                _logger.debug("OUCH!", e);   
            }
        }
        
        return rightsQualifyingStatement;
    }
  
    
  void setRightsQualifyingStatement( String rightsQualifyingStatement )
  {
    _rightsQualifyingStatement = rightsQualifyingStatement;
  }
 
  public String getResolutionTerms() {
	  // Needed to meet interface
	  return null;
  }
  
  public String getLicenseTerms() {
      // Needed to meet interface
	  return null;
  }
  
  
  public String getVolume()
  {
    String volume = getPermissionRequest().getWork().getVolume();
    
    return volume;
  }
  
  public void setVolume(String volume)
  {
    getPermissionRequest().getWork().setVolume( volume );
  }

  public String getEdition()
  {
    String edition = getPermissionRequest().getWork().getEdition();
    
    return edition;
  }
  
  public void setEdition(String edition)
  {
    getPermissionRequest().getWork().setEdition( edition );
  }

  public String getPermissionSelected()
  {
    String permissionSelected = getPermissionRequest().getUsageData().getTypeOfUseDisplay();
    
    return permissionSelected;
  }
  
  public void setPermissionSelected(String permissionSelected)
  {
    getPermissionRequest().getUsageData().setTypeOfUseDisplay( permissionSelected );
  }

  public String getPrice()
  {
    Money price = getPermissionRequest().getPrice();
    
    if( price == null )
    {
      price = new Money();
    }
        
    String priceString = WebUtils.formatMoney( price );
    
    return priceString;
  }

  public double getPriceValue() {
	  return getPermissionRequest().getPrice().getValue();
  }

  public double getPriceValueRaw() {
	  return getPermissionRequest().getPrice().getValue();
  }
  
  public BigDecimal getTotalPriceValue() {
	  return new BigDecimal(getPermissionRequest().getPrice().getValue()).setScale(2, BigDecimal.ROUND_HALF_EVEN);
  }

  public String getCustomerReference()
  {
    String yourReference = getPermissionRequest().getCustomerRef();
    
    return yourReference;
  }

  public void setCustomerReference(String yourReference)
  {
    getPermissionRequest().setCustomerRef( yourReference );
  }

  public String getPublicationYearOfUse()
  {
    String publicationYearBeingUsed = Constants.EMPTY_STRING;
    
    boolean operationSupported = isAcademic() || 
                                 isEmail() ||
                                 isPhotocopy() ||
                                 isNet() ||
                                 isRepublication();
                                 
    if( operationSupported )
    {

      try
      {
        Date permissionRequestPublicationDate = getPermissionRequest().getUsageData().getPublicationDate();
        
        if ( permissionRequestPublicationDate != null )
        {
          publicationYearBeingUsed = ECommerceUtils.getYearYYYY( permissionRequestPublicationDate );
        }
      } catch (ParseException e)
      {
        throw new CCCRuntimeException("Could not parse Publication Date", e);
      }
      
    }else
    {
      throw new UnsupportedOperationException();
    }

    return publicationYearBeingUsed;
  }

  public void setPublicationYearOfUse( String publicationYearBeingUsed )
  {
    boolean operationSupported = isAcademic() || 
                                 isEmail() ||
                                 isPhotocopy() ||
                                 isNet() ||
                                 isRepublication();
    
    if( operationSupported )
    {
      Date publicationDate = null;
      try
      {
        if ( StringUtils.isNotEmpty( publicationYearBeingUsed ) )
        {
          publicationDate = ECommerceUtils.transformYearToDate( publicationYearBeingUsed );
        }else
        {
          throw new IllegalArgumentException( "Invalid publication year provided: " + publicationYearBeingUsed );
        }
      
        getPermissionRequest().getUsageData().setPublicationDate( publicationDate );  
      
      } catch ( ParseException e )
      {
        throw new CCRuntimeException("Could not parse Publication Year: " + publicationYearBeingUsed, e);
      }
      
    }else
    {
      throw new UnsupportedOperationException();
    }
  }

  public long getNumberOfPages()
  {
    long numberOfPages = ECommerceConstants.INVALID_NUMBER_OF_PAGES;
    
    boolean operationSupported = isAcademic() || 
                                 isPhotocopy() ||
                                 isRepublication();
    
    if( operationSupported )
    {
      if( isAcademic() )
      {
        UsageDataAcademic usageDataAcademic = (UsageDataAcademic) getPermissionRequest().getUsageData();
        
        if(usageDataAcademic.getNumPages() != null)
            numberOfPages = usageDataAcademic.getNumPages().longValue();
      }
      
      if( isPhotocopy() )
      {
        UsageDataPhotocopy usageDataPhotocopy = (UsageDataPhotocopy) getPermissionRequest().getUsageData();
        
        Long permissionRequestNumPages = usageDataPhotocopy.getNumPages();
        
        if ( permissionRequestNumPages != null )
        {
          numberOfPages = permissionRequestNumPages.longValue();
        }
      }
      
      if( isRepublication() )
      {
        UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();
        
        numberOfPages = usageDataRepublication.getRlsPages();
        
      }
      
    }else
    {
      throw new UnsupportedOperationException();
    }
    
    return numberOfPages;
  }

  public void setNumberOfPages( long numberOfPages )
  {
    boolean operationSupported = isAcademic() || 
                                 isPhotocopy() ||
                                 isRepublication();
    
    if( operationSupported )
    {
      if( isAcademic() )
      {
        UsageDataAcademic usageDataAcademic = (UsageDataAcademic) getPermissionRequest().getUsageData();
        
        usageDataAcademic.setNumPages( numberOfPages );
        
      }
      
      if( isPhotocopy() )
      {
        UsageDataPhotocopy usageDataPhotocopy = (UsageDataPhotocopy) getPermissionRequest().getUsageData();
        
        usageDataPhotocopy.setNumPages( numberOfPages );
        
      }
      
      if( isRepublication() )
      {
        UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();
        
        usageDataRepublication.setRlsPages( numberOfPages );

        if ( numberOfPages > 0 )
        {
          usageDataRepublication = ECommerceUtils.zeroContentTypeQuantities( usageDataRepublication );
        }
        
      }
    }else
    {
      throw new UnsupportedOperationException();
    }
  }

  public long getNumberOfCopies()
  {
    long numberOfCopies = ECommerceConstants.INVALID_NUMBER_OF_COPIES;
    
    boolean operationSupported = isPhotocopy();
    
    if( operationSupported )
    {
      UsageDataPhotocopy usageDataPhotocopy = (UsageDataPhotocopy) getPermissionRequest().getUsageData();
      
      Long permissionRequestNumCopies = usageDataPhotocopy.getNumCopies();
      
      if ( permissionRequestNumCopies != null )
      {
        numberOfCopies = permissionRequestNumCopies.longValue();
      }
    }else
    {
      throw new UnsupportedOperationException();
    }
    
    return numberOfCopies;
  }

  public void setNumberOfCopies(long numberOfCopies)
  {
    boolean operationSupported = isPhotocopy();
    
    if( operationSupported )
    {
      UsageDataPhotocopy usageDataPhotocopy = (UsageDataPhotocopy) getPermissionRequest().getUsageData();
      
      usageDataPhotocopy.setNumCopies( numberOfCopies );
      
    }else
    {
      throw new UnsupportedOperationException();
    }
  }

  public int getNumberOfStudents()
  {
    int numberOfStudents = ECommerceConstants.INVALID_NUMBER_OF_STUDENTS;
    
    boolean operationSupported = isAPS() ||
                                 isECCS();
    
    if( operationSupported )
    {
      UsageDataAcademic usageDataAcademic = (UsageDataAcademic) getPermissionRequest().getUsageData();
      
      if(usageDataAcademic.getNumStudents() != null)
        numberOfStudents = usageDataAcademic.getNumStudents().intValue();
    }else
    {
      throw new UnsupportedOperationException();
    }
    
    return numberOfStudents;
  }

  public void setNumberOfStudents(int numberOfStudents)
  {
    boolean operationSupported = isAPS() ||
                                 isECCS();
    
    if( operationSupported )
    {
      UsageDataAcademic usageDataAcademic = (UsageDataAcademic) getPermissionRequest().getUsageData();
            
      usageDataAcademic.setNumStudents( (long) numberOfStudents );
      
    }else
    {
      throw new UnsupportedOperationException();
    }
  }

  public int getCirculationDistribution()
  {
    int circulationDistribution = ECommerceConstants.INVALID_CIRCULATION_DISTRIBUTION;
    
    boolean operationSupported = isRepublication();
    
    if( operationSupported )
    {
      UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();
      
      //TODO 01/26/2007 lalberione: is this cast safe?      
      circulationDistribution = (int) usageDataRepublication.getCirculation();
      
    }else
    {
      throw new UnsupportedOperationException();
    }
    
    return circulationDistribution;
  }

  public void setCirculationDistribution( int circulationDistribution )
  {
    boolean operationSupported = isRepublication();
    
    if( operationSupported )
    {
      UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();
            
      usageDataRepublication.setCirculation( circulationDistribution );
      
    }else
    {
      throw new UnsupportedOperationException();
    }
  }

  public String getBusiness()
  {
    String business = Constants.EMPTY_STRING;
    
    boolean operationSupported = isRepublication();
    
    if( operationSupported )
    {
      UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();
      
      if( usageDataRepublication.getForProfit() == null )
        business = RepublicationConstants.BUSINESS_FOR_PROFIT;
    
    else
    {
      if( usageDataRepublication.getForProfit().equalsIgnoreCase( RepublicationConstants.FOR_PROFIT ) )
      {
        business = RepublicationConstants.BUSINESS_FOR_PROFIT ;
        
      }else
      {
        business = RepublicationConstants.BUSINESS_NON_FOR_PROFIT;
      }
    }
            
    }else
    {
      throw new UnsupportedOperationException();
    }
    
    return business;
  }

  public String getBusinessDescription()
  {
      String rtnValue = "";
      String business = this.getBusiness();
      if (RepublicationConstants.BUSINESS_FOR_PROFIT.equals(business)) {
          rtnValue = TransactionConstants.FOR_PROFIT;
      } else if (RepublicationConstants.BUSINESS_NON_FOR_PROFIT.equals(business)) {
          rtnValue = TransactionConstants.NOT_FOR_PROFIT;
      }

      return rtnValue;
  }
    
  public void setBusiness( String business )
  {
    boolean operationSupported = isRepublication();
    
    if( operationSupported )
    {
      UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();
      
      if( business.equals( RepublicationConstants.BUSINESS_FOR_PROFIT ) )
      {
        usageDataRepublication.setForProfit( RepublicationConstants.FOR_PROFIT );
      } else 
      {
        usageDataRepublication.setForProfit( RepublicationConstants.NON_FOR_PROFIT );
      }
      
    }else
    {
      throw new UnsupportedOperationException();
    }
  }

    public String getTypeOfContent() {
        String typeOfContent = Constants.EMPTY_STRING;
    
        if (isRepublication()) {
            UsageDataRepublication usageDataRepublication = 
                (UsageDataRepublication) getPermissionRequest().getUsageData();
                
            typeOfContent = ECommerceUtils.getTypeOfContent( usageDataRepublication );
            
            if (_logger.isDebugEnabled()) {
                _logger.debug("GET Type Of Content: " + typeOfContent);
                _logger.debug("GET Usage Data Charts: " + 
                    Long.toString(usageDataRepublication.getNumCharts()));
            }
        }
        else {
            //throw new UnsupportedOperationException();
        }
        return typeOfContent;
    }
    
    public void setTypeOfContent( String typeOfContent ) {
        if (isRepublication()) {
            UsageDataRepublication usageDataRepublication = 
                (UsageDataRepublication) getPermissionRequest().getUsageData();
      
            usageDataRepublication = ECommerceUtils.zeroContentTypeQuantities( usageDataRepublication );
      
            if (!typeOfContent.equals(RepublicationConstants.CONTENT_SELECTED_PAGES)) {
                usageDataRepublication.setRlsPages( 0 );
            }
            usageDataRepublication = ECommerceUtils.setTypeOfContent( usageDataRepublication, typeOfContent );
            
            if (_logger.isDebugEnabled()) {
                _logger.debug("SET Type Of Content: " + typeOfContent);
                _logger.debug("SET Usage Data Charts: " + 
                    Long.toString(usageDataRepublication.getNumCharts()));
            }
            //  2009-08-27  MSJ
            //  Added this line to actually set the usageData with the
            //  content information.
            
            getPermissionRequest().setUsageData(usageDataRepublication);
        }
        else {
            throw new UnsupportedOperationException();
        }
    }

  public String getTypeOfContentDescription()
  {
      String typeOfContentDescr = Constants.EMPTY_STRING;
      
      boolean operationSupported = isRepublication();
      
      if( operationSupported )
      {
        UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();
              
        typeOfContentDescr = ECommerceUtils.getTypeOfContentDescription( usageDataRepublication );
        
      }else
      {
        throw new UnsupportedOperationException();
      }
      
      return typeOfContentDescr;
  }

  public boolean isSubmitterAuthor()
  {
    boolean isSubmitterAuthor = false;
    
    boolean operationSupported = isRepublication();
    
    if( operationSupported )
    {
      UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();

      boolean submmiterIsAuthor = usageDataRepublication.getOrigAuthor() != null && usageDataRepublication.getOrigAuthor().equalsIgnoreCase(RepublicationConstants.SUBMITTER_IS_AUTHOR);
      
      if ( submmiterIsAuthor )
      {
        isSubmitterAuthor = true;
      }
            
    }else
    {
      throw new UnsupportedOperationException();
    }
    
    return isSubmitterAuthor;
  }

  public String getSubmitterAuthor()
  {
    String isSubmitterAuthor = ItemConstants.NO_CD;
    
    boolean operationSupported = isRepublication();
    
    if( operationSupported )
    {
      UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();

      boolean submmiterIsAuthor = usageDataRepublication.getOrigAuthor() != null && usageDataRepublication.getOrigAuthor().equalsIgnoreCase(RepublicationConstants.SUBMITTER_IS_AUTHOR);
      
      if ( submmiterIsAuthor )
      {
        isSubmitterAuthor = ItemConstants.YES_CD;
      }
            
    }else
    {
      throw new UnsupportedOperationException();
    }
    
    return isSubmitterAuthor;
  }
  
  public void setSubmitterAuthor( boolean isSubmitterAuthor )
  {
    boolean operationSupported = isRepublication();
    
    if( operationSupported )
    {
      UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();
      
      if( isSubmitterAuthor )
      {
        usageDataRepublication.setOrigAuthor( RepublicationConstants.SUBMITTER_IS_AUTHOR );
      }else
      {
        usageDataRepublication.setOrigAuthor( RepublicationConstants.SUBMITTER_IS_NOT_AUTHOR );
      }
      
    }else
    {
      throw new UnsupportedOperationException();
    }
  }

  public Date getContentsPublicationDate()
  {
    Date contentsPublicationDate = null;
    
    boolean operationSupported = isRepublication();
    
    if( operationSupported )
    {
      UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();
      
      contentsPublicationDate = usageDataRepublication.getPublicationDate();
      
    }else
    {
      throw new UnsupportedOperationException();
    }
    
    return contentsPublicationDate;
  }

  public void setContentsPublicationDate(Date contentsPublicationDate)
  {
    boolean operationSupported = isRepublication();
    
    if( operationSupported )
    {
      UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();
      
      usageDataRepublication.setPublicationDate( contentsPublicationDate );
      
    }else
    {
      throw new UnsupportedOperationException();
    }
  }

  public Date getDateOfUse()
  {
    Date dateOfUse = null;
    
    boolean operationSupported = isEmail() ||
                                 isNet() ;
    
    if( operationSupported )
    {
      if ( isEmail() )
      {
        UsageDataEmail usageDataEmail = (UsageDataEmail) getPermissionRequest().getUsageData();
        
        dateOfUse = usageDataEmail.getDateOfUse();
      }
      
      if ( isNet() )
      {
        UsageDataNet usageDataNet = (UsageDataNet) getPermissionRequest().getUsageData();
        
        dateOfUse = usageDataNet.getDateOfUse();
      }
    }else
    {
      throw new UnsupportedOperationException();
    }
    
    return dateOfUse;
  }

  public void setDateOfUse(Date dateOfUse)
  {
    boolean operationSupported = isEmail() ||
                                 isNet();
    
    if( operationSupported )
    {
      if (isEmail())
      {
        UsageDataEmail usageDataEmail = (UsageDataEmail) getPermissionRequest().getUsageData();
        
        usageDataEmail.setDateOfUse( dateOfUse );
        
      }
      
      if (isNet())
      {
        UsageDataNet usageDataNet = (UsageDataNet) getPermissionRequest().getUsageData();
        
        usageDataNet.setDateOfUse( dateOfUse );
        
      }
          
    }else
    {
      throw new UnsupportedOperationException();
    }
  }

  public long getNumberOfRecipients()
  {
    long numberOfRecipients = ECommerceConstants.INVALID_NUMBER_OF_RECIPIENTS;
    
    boolean operationSupported = isEmail();
    
    if( operationSupported )
    {
      UsageDataEmail usageDataEmail = (UsageDataEmail) getPermissionRequest().getUsageData();
      
      Long permissionRequestNumRecipients = usageDataEmail.getNumRecipients();
      
      if ( permissionRequestNumRecipients != null )
      {
        numberOfRecipients = permissionRequestNumRecipients.longValue();
      }
      
    }else
    {
      if( isNet() )
      {
        numberOfRecipients = this._numberOfRecipients;
      }else
      {
        throw new UnsupportedOperationException();
      }
    }
    
    return numberOfRecipients;
  }

  public void setNumberOfRecipients(long numberOfRecipients)
  {
  
    boolean operationSupported = isEmail();
    
    if( operationSupported )
    {
      UsageDataEmail usageDataEmail = (UsageDataEmail) getPermissionRequest().getUsageData();
      
      usageDataEmail.setNumRecipients( numberOfRecipients );
     
    }else
    {
      if( isNet() )
      {
        this._numberOfRecipients = numberOfRecipients;
      }else
      {
        throw new UnsupportedOperationException();
      }
    }
  
  }

  public int getDuration()
  {
    int duration = ECommerceConstants.INVALID_DURATION;
    
    boolean operationSupported = isNet();
    
    if( operationSupported )
    {
      UsageDataNet usageDataNet = (UsageDataNet) getPermissionRequest().getUsageData();
      
      duration = usageDataNet.getDuration();
    }else
    {
      if( isEmail() )
      {
        duration = this._duration ;
      }else
      {
        throw new UnsupportedOperationException();
      }
    }
    
    return duration;
  }
  
  

  public void setDuration(int duration)
  {
    boolean operationSupported = isNet();
    
    if( operationSupported )
    {
      UsageDataNet usageDataNet = (UsageDataNet) getPermissionRequest().getUsageData();
      
      usageDataNet.setDuration( duration );
      
    }else
    {
      if( isEmail() )
      {
        this._duration = duration;
      }else
      {
        throw new UnsupportedOperationException();
      }
    }
  }
  
  public String getDurationString()
  {
  
    String durationString = Constants.EMPTY_STRING;
  
    boolean operationSupported = isNet();
    
    if( operationSupported )
    {
      UsageDataNet usageDataNet = (UsageDataNet) getPermissionRequest().getUsageData();
      
      durationString = usageDataNet.getDurationString();
    }else
    {
      throw new UnsupportedOperationException();
    }
  
    return durationString;
  }

  public String getWebAddress()
  {
    String webAddress = Constants.EMPTY_STRING;
    
    boolean operationSupported = isInternet();
    
    if( operationSupported )
    {
      UsageDataNet usageDataNet = (UsageDataNet) getPermissionRequest().getUsageData();
      
      webAddress = usageDataNet.getWebAddress();
    }else
    {
      if( isEmail() || isExtranet() || isIntranet() )
      {
        webAddress = this._webAddress;
      }else
      {
        throw new UnsupportedOperationException();
      }
    }
    
    return webAddress;
  }

  public void setWebAddress(String webAddress)
  {
    boolean operationSupported = isInternet();
    
    if( operationSupported )
    {
      UsageDataNet usageDataNet = (UsageDataNet) getPermissionRequest().getUsageData();
      
      usageDataNet.setWebAddress( webAddress );
      
    }else
    {
      if( isEmail() || isExtranet() || isIntranet() )
      {
        this._webAddress = webAddress;
      }else
      {
        throw new UnsupportedOperationException();
      }
    }
  }
  
  public String getChapterArticle()
  {
    String chapterArticle = Constants.EMPTY_STRING;
    
    boolean operationSupported = isAcademic() ||
                                 isPhotocopy() ||
                                 isEmail() ||
                                 isNet() ||
                                 isRepublication() ;
    
    if( operationSupported )
    {
      if ( isAcademic() )
      {
        UsageDataAcademic usageDataAcademic = (UsageDataAcademic) getPermissionRequest().getUsageData();
        
        chapterArticle = usageDataAcademic.getChapterArticle();
      }
      
      if( isPhotocopy() )
      {
        UsageDataPhotocopy usageDataPhotocopy = (UsageDataPhotocopy) getPermissionRequest().getUsageData();
        
        chapterArticle = usageDataPhotocopy.getChapterArticle();
      }
      
      if( isEmail() )
      {
        UsageDataEmail usageDataEmail = (UsageDataEmail) getPermissionRequest().getUsageData();
        
        chapterArticle = usageDataEmail.getChapterArticle();
      }
      
      if( isNet() )
      {
        UsageDataNet usageDataNet = (UsageDataNet) getPermissionRequest().getUsageData();
        
        chapterArticle = usageDataNet.getChapterArticle();
      }
      
      if( isRepublication() )
      {
        UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();
        
        chapterArticle = usageDataRepublication.getChapterArticle();
      }
    }else
    {
      throw new UnsupportedOperationException();
    }
    
    return chapterArticle;
  }

  public void setChapterArticle(String chapterArticle)
  {
       
    boolean operationSupported = isAcademic() ||
                                 isPhotocopy() ||
                                 isEmail() ||
                                 isNet() || 
                                 isRepublication();
    
    if( operationSupported )
    {
      if ( isAcademic() )
      {
        UsageDataAcademic usageDataAcademic = (UsageDataAcademic) getPermissionRequest().getUsageData();
        
        usageDataAcademic.setChapterArticle( chapterArticle );
        
      }
      
      if( isPhotocopy() )
      {
        UsageDataPhotocopy usageDataPhotocopy = (UsageDataPhotocopy) getPermissionRequest().getUsageData();
        
        usageDataPhotocopy.setChapterArticle( chapterArticle );
        
      }
      
      if( isEmail() )
      {
        UsageDataEmail usageDataEmail = (UsageDataEmail) getPermissionRequest().getUsageData();
        
        usageDataEmail.setChapterArticle( chapterArticle );
        
      }
      
      if( isNet() )
      {
        UsageDataNet usageDataNet = (UsageDataNet) getPermissionRequest().getUsageData();
        
        usageDataNet.setChapterArticle( chapterArticle );
      }
      
      if( isRepublication() )
      {
        UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();
        
        usageDataRepublication.setChapterArticle( chapterArticle );
      }
       
    }else
    {
      throw new UnsupportedOperationException();
    }
}
  
  public String getPageRange()
  {
    String pageRange = Constants.EMPTY_STRING;
    
    boolean operationSupported = isAcademic() ||
                                 isRepublication();
    
    if( operationSupported )
    {
      if( isAcademic() )
      {
        UsageDataAcademic usageDataAcademic = (UsageDataAcademic) getPermissionRequest().getUsageData();
        
        pageRange = usageDataAcademic.getPageRanges();
      }
      
      if( isRepublication() )
      {
        UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();
        
        pageRange = usageDataRepublication.getPageRanges();
      }
    }else
    {
      throw new UnsupportedOperationException();
    }
    
    return pageRange;
  }

  public void setPageRange( String pageRange )
  {
  
    boolean operationSupported = isAcademic() ||
                                 isRepublication();
    
    if( operationSupported )
    {
      if( isAcademic() )
      {
        UsageDataAcademic usageDataAcademic = (UsageDataAcademic) getPermissionRequest().getUsageData();
        
        usageDataAcademic.setPageRanges( pageRange );
        
      }
      
      if( isRepublication() )
      {
        UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();
        
        usageDataRepublication.setPageRanges( pageRange );
        
      }
    }else
    {
      throw new UnsupportedOperationException();
    }
    
  }
  
  public String getRepublishingOrganization()
  {
    String republishingOrganization = Constants.EMPTY_STRING;
    
    boolean operationSupported = isRepublication();
    
    if( operationSupported )
    {
      UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();
      
      republishingOrganization = usageDataRepublication.getHdrRepubPub();
      
    }else
    {
      throw new UnsupportedOperationException();
    }
    
    return republishingOrganization;
  }

  public void setRepublishingOrganization( String republishingOrganization )
  {
    boolean operationSupported = isRepublication();
    
    if( operationSupported )
    {
      UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();
      
      usageDataRepublication.setHdrRepubPub( StringUtils.upperCase(republishingOrganization) );
      
    }else
    {
      throw new UnsupportedOperationException();
    }
  }
  
  public String getNewPublicationTitle()
  {
    String newPublicationTitle = Constants.EMPTY_STRING;
    
    boolean operationSupported = isRepublication();
    
    if( operationSupported )
    {
      UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();
      
      newPublicationTitle = usageDataRepublication.getRepubTitle();
      
    }else
    {
      throw new UnsupportedOperationException();
    }
    
    return newPublicationTitle;
  }

  public void setNewPublicationTitle(String newPublicationTitle)
  {
    boolean operationSupported = isRepublication();
    
    if( operationSupported )
    {
      UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();
      
      usageDataRepublication.setRepubTitle( StringUtils.upperCase(newPublicationTitle) );
      
    }else
    {
      throw new UnsupportedOperationException();
    }
  }
  
  public Date getRepublicationDate()
  {
    Date republicationDate = null;
    
    boolean operationSupported = isRepublication();
    
    if( operationSupported )
    {
      UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();
      
      republicationDate = usageDataRepublication.getRepubDate();
      
    }else
    {
      throw new UnsupportedOperationException();
    }
    
    return republicationDate;
  }

  public void setRepublicationDate( Date republicationDate )
  {
    boolean operationSupported = isRepublication();
    
    if( operationSupported )
    {
      UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();
      
      usageDataRepublication.setRepubDate( republicationDate );
      
    }else
    {
      throw new UnsupportedOperationException();
    }
  }

  public String getTranslated() 
  {
      String translated = "";

      boolean operationSupported = isRepublication();
      
      if( operationSupported ) {

    	  UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();

    	  translated = usageDataRepublication.getTranslated();
      }

      return translated;
  }

  public String getTranslationLanguage()
  {
    String translationLanguage = RepublicationConstants.NO_TRANSLATION;
    
    boolean operationSupported = isRepublication();
    
    if( operationSupported )
    {
      UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();
      
      if ( StringUtils.isNotEmpty( usageDataRepublication.getLanguage() ) )
      {
        translationLanguage = usageDataRepublication.getLanguage();
      }
    }else
    {
      throw new UnsupportedOperationException();
    }
    
    return translationLanguage;
  }

  public void setTranslationLanguage( String translationLanguage )
  {
    boolean operationSupported = isRepublication();
    
    if( operationSupported )
    {
      UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();
      
      if( translationLanguage.equals( RepublicationConstants.NO_TRANSLATION ) )
      {
        usageDataRepublication.setLanguage( Constants.EMPTY_STRING );
        usageDataRepublication.setTranslated( RepublicationConstants.NOT_TRANSLATED );
      }else
      {
        usageDataRepublication.setLanguage( translationLanguage );
        usageDataRepublication.setTranslated( RepublicationConstants.TRANSLATED );
      }
      
    }else
    {
      throw new UnsupportedOperationException();
    }
  }

  /**
  * Display the translation language with the first character capitalized and all
  * remaining characters lower case.  Return an empty string if the translation 
  * language indicates there is no translation language.
  * @return
  */
  public String getTranslationLanguageDescription()
  {
      String translationLanguage = "";
      boolean operationSupported = isRepublication();

      if( operationSupported )
      {
        UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();
        String language = usageDataRepublication.getLanguage();

        if ( StringUtils.isNotEmpty(language) )
        {
          translationLanguage = usageDataRepublication.getLanguage();
          if (!RepublicationConstants.NO_TRANSLATION.equals(translationLanguage)) {
             translationLanguage = WebUtils.makeDisplayable(language);
          }
        }
      }else
      {
        throw new UnsupportedOperationException();
      }

      return translationLanguage;
  }

  public String getRepublicationDestination()
  {
    String republicationDestination = Constants.EMPTY_STRING;
    
    boolean operationSupported = isRepublication();
    
    if( operationSupported )
    {
      //UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();
      
      //TODO 10/26/06 lalberione: wait for UsageDataRepublication member to be implemented.
    }else
    {
      throw new UnsupportedOperationException();
    }
    
    return republicationDestination;
  }

  public void setRepublicationDestination(String republicationDestination)
  {
    boolean operationSupported = isRepublication();
    
    if( operationSupported )
    {
      getPermissionRequest().getUsageData();
      
      //TODO 10/26/06 lalberione: wait for UsageDataRepublication member to be implemented.
      
    }else
    {
      throw new UnsupportedOperationException();
    }
  }
  
  public String getAuthor()
  {
    
    String author = getPermissionRequest().getWork().getMainAuthor();
    
    return author;
  }

  public void setAuthor(String author)
  {
    getPermissionRequest().getWork().setMainAuthor( author );
  }

  public String getEditor()
  {
    String editor = getPermissionRequest().getWork().getMainEditor();
    
    return editor;
  }
  
  public void setEditor(String editor)
  {
    getPermissionRequest().getWork().setMainEditor( editor );
  }
  

  public Date getPublicationDateOfUse()
  {
    Date publicationDateOfUse = null;
    
    boolean operationSupported = isAcademic() || 
                                 isEmail() ||
                                 isPhotocopy() ||
                                 isNet();
    
    if ( operationSupported )
    {
      publicationDateOfUse = getPermissionRequest().getUsageData().getPublicationDate();
    }
    
    return publicationDateOfUse;
  }
  
  public void setPublicationDateOfUse(Date publicationDateOfUse)
  {
    boolean operationSupported = isAcademic() || 
                                 isEmail() ||
                                 isPhotocopy() ||
                                 isNet();
    if ( operationSupported )
    {
      UsageData usagedata = getPermissionRequest().getUsageData();
      
      usagedata.setPublicationDate( publicationDateOfUse );
      
    }
  }
  
  public String getDateOfIssue()
  {
    
    String dateOfIssue = null;
    
    boolean operationSupported = isAPS() ||
                                 isECCS();
                                 
    if( operationSupported )
    {
      UsageDataAcademic usageDataAcademic = (UsageDataAcademic) getPermissionRequest().getUsageData();
      
      dateOfIssue = usageDataAcademic.getDateOfIssue();
    }else
    {
      throw new UnsupportedOperationException();
    }
    
    return dateOfIssue;
  }

  public void setDateOfIssue( String dateOfIssue )
  {
    
    
    boolean operationSupported = isAPS() ||
                                 isECCS();
                                 
    if( operationSupported )
    {
      UsageDataAcademic usageDataAcademic = (UsageDataAcademic) getPermissionRequest().getUsageData();
      
      usageDataAcademic.setDateOfIssue( dateOfIssue );
      
    }else
    {
      throw new UnsupportedOperationException();
    }
    
  }

  public boolean isAcademic()
  {
    
    boolean isAcademic = isAPS() ||
                         isECCS();
    
    return isAcademic;
  }

  public boolean isPhotocopy()
  {
    boolean isPhotocopy = getPermissionRequest().getUsageData().getProduct() == ECommerceConstants.TRS_PRODUCT_CODE &&
                          getPermissionRequest().getUsageData().getTpuInst() == ECommerceConstants.PHOTOCOPY_TPU_CODE;
    
    return isPhotocopy;
  }
  
  public boolean isEmail()
  {
    boolean isEmail = getPermissionRequest().getUsageData().getProduct() == ECommerceConstants.DPS_PRODUCT_CODE &&
                      getPermissionRequest().getUsageData().getTpuInst() == ECommerceConstants.EMAIL_TPU_CODE;
    
    return isEmail;
  }

  public boolean isRepublication()
  {
    boolean isRepublication = getPermissionRequest().getUsageData().getProduct() == ECommerceConstants.RLS_PRODUCT_CODE;
    
    return isRepublication;
  }

  public boolean isNet()
  {
    boolean isNet = isInternet() ||
                    isIntranet() ||
                    isExtranet();
    
    return isNet;
  }
  
  public boolean isIntranet()
  {
    boolean isIntranet = getPermissionRequest().getUsageData().getProduct() == ECommerceConstants.DPS_PRODUCT_CODE &&
                         getPermissionRequest().getUsageData().getTpuInst() == ECommerceConstants.INTRANET_TPU_CODE;
       
    return isIntranet;
  }

  public boolean isExtranet()
  {
    
    boolean isExranet = getPermissionRequest().getUsageData().getProduct() == ECommerceConstants.DPS_PRODUCT_CODE &&
                        getPermissionRequest().getUsageData().getTpuInst() == ECommerceConstants.EXTRANET_TPU_CODE;
    
    
    return isExranet;
  }
  

  public boolean isInternet()
  {
        
    boolean isInternet = getPermissionRequest().getUsageData().getProduct() == ECommerceConstants.DPS_PRODUCT_CODE &&
                         getPermissionRequest().getUsageData().getTpuInst() == ECommerceConstants.INTERNET_TPU_CODE;
    
    return isInternet;
  }

  public boolean isSpecialOrder()
  {
    
    boolean isSpecialOrder = getPermissionRequest().isSpecialOrder();
    
    return isSpecialOrder;
  }  

  public void setSpecialOrderTypeCd(String specialOrderTypeCd)
  {
	  // Nothing to do here, skeleton to meet transaction item interface  
  }

  public String getSpecialOrderTypeCd()
  {
	  return null;
  }
  
  public boolean isDigital()
  {
    boolean isDigital = isNet() || 
                        isEmail();
    
    return isDigital;
  }
  
  public boolean isRightsLink()
  {
	  boolean isRightsLink = false;
	  
	  return isRightsLink;
	  
  }

	public boolean isReprint() {

		return false;
	}

	public boolean isPricedReprint() {

		return false;
	}

  
  public boolean isAPS()
  {
    boolean isAPS = getPermissionRequest().getUsageData().getProduct() == ECommerceConstants.APS_PRODUCT_CODE;
    
    return isAPS;
  }

  public boolean isECCS()
  {
     boolean isECCS = getPermissionRequest().getUsageData().getProduct() == ECommerceConstants.ECCS_PRODUCT_CODE;

     return isECCS;
  }
  
  //    *********************************************
  //    MSJ Just highlighting this because it is used
  //    throughout the code.
  
  @Override
  public PermissionRequest getPermissionRequest()
  {
    return _permissionRequest;
  }

  @Override
  void setPermissionRequest(PermissionRequest permissionRequest)
  {
    _permissionRequest = permissionRequest;
  }


  public boolean isSpecialOrderFromScratch()
  {
    boolean isSpecialOrderFromScratch = getPermissionRequest().isManualSpecialOrder();
    
    return isSpecialOrderFromScratch;
  }
  
  public boolean isContactRightsholder()
  {
    return getPermissionRequest().isContactRHPermission();
  }

  public boolean isManualSpecialOrder()
  {
    return getPermissionRequest().isManualSpecialOrder();
  }


  public Date getPublicationStartDate()
  {
    Date publicationStartDate = null;
    
    boolean rightPresentInPermissionRequest = getRightFromWeb() != null;
    
    if( rightPresentInPermissionRequest )
    {
      publicationStartDate = formatDate(getRightFromWeb().getPubBegDtm());
    }else
    {
      publicationStartDate = _publicationStartDate;
    } 
    
    return publicationStartDate;
  }

  @Override
  public void setPublicationStartDate( Date publicationStartDate )
  {
    _publicationStartDate = publicationStartDate;
  }
  

  public Date getPublicationEndDate()
  {
    Date publicationEndDate = null;
    
    boolean rightPresentInPermissionRequest = getRightFromWeb() != null;
    
    if( rightPresentInPermissionRequest )
    {
      publicationEndDate = formatDate(getRightFromWeb().getPubEndDtm());;
    }else
    {
      publicationEndDate = _publicationEndDate;
    }
    
    return publicationEndDate;
  }
  
  @Override
  public void setPublicationEndDate( Date publicationEndDate )
  {
    _publicationEndDate = publicationEndDate;
  }
  
    private Date formatDate(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            //if date in database can't be parsed correctly, just return the current date.
            return new Date();
        }
    }

  public String getCustomAuthor()
  {
    String customAuthor = Constants.EMPTY_STRING;
    
    boolean operationSupported = isAPS() ||
                                 isECCS() ||
                                 isRepublication() ||
                                 isEmail() ||
                                 isIntranet() ||
                                 isExtranet() ||
                                 isInternet();
                                 
    if ( operationSupported)
    {
      customAuthor = getPermissionRequest().getUsageData().getAuthor();
    }
    else
    {
      throw new UnsupportedOperationException();
    }
    
    return customAuthor;
  }
  

  public void setCustomAuthor( String customAuthor )
  {
        
    boolean operationSupported = isAPS() ||
                                 isECCS() ||
                                 isRepublication() ||
                                 isEmail() ||
                                 isIntranet() ||
                                 isExtranet() ||
                                 isInternet();
                                 
    if ( operationSupported )
    {
      getPermissionRequest().getUsageData().setAuthor( customAuthor );
    }
    else
    {
      throw new UnsupportedOperationException();
    }
    
  }
  

  public String getYourReference()
  {
    String yourReference = Constants.EMPTY_STRING;
    
    boolean operationSupported = isRepublication();
    
    if( operationSupported )
    {
      UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();
      
      yourReference = usageDataRepublication.getLcnHdrRefNum();
      
    }else
    {
      throw new UnsupportedOperationException();
    }
    
    return yourReference;
  }
  
  public String getHasVolPriceTiers() {
      return getRightFromWeb().getHasVolumePrice()?"Y":"N";    
  }
    

  public void setYourReference( String yourReference )
  {
    boolean operationSupported = isRepublication();
    
    if( operationSupported )
    {
      UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();
      
      usageDataRepublication.setLcnHdrRefNum( StringUtils.upperCase(yourReference) );
      
    }else
    {
      throw new UnsupportedOperationException();
    }
    
  }

  public String getRepublicationTypeOfUse()
  {
    
    boolean operationSupported = isRepublication();
    
    if( operationSupported )
    {
      String republicationTypeOfUse = Constants.EMPTY_STRING;
      long tpuInst = getPermissionRequest().getUsageData().getTpuInst();
      
      
      if( tpuInst == ECommerceConstants.REPUBLICATION_ADVERTISEMENT_TPU_CODE )
      {
        republicationTypeOfUse = RepublicationConstants.REPUBLICATION_ADVERTISEMENT;
      }
      
      if( tpuInst == ECommerceConstants.REPUBLICATION_BROCHURE_TPU_CODE )
      {
        republicationTypeOfUse = RepublicationConstants.REPUBLICATION_BROCHURE;
      }
      
      if( tpuInst == ECommerceConstants.REPUBLICATION_CDROM_TPU_CODE )
      {
        republicationTypeOfUse = RepublicationConstants.REPUBLICATION_CDROM;
      }
      
      if( tpuInst == ECommerceConstants.REPUBLICATION_DISSERTATION_TPU_CODE )
      {
        republicationTypeOfUse = RepublicationConstants.REPUBLICATION_DISSERTATION;
      }
      
      if( tpuInst == ECommerceConstants.REPUBLICATION_DVD_TPU_CODE )
      {
        republicationTypeOfUse = RepublicationConstants.REPUBLICATION_DVD;
      }
      
      if( tpuInst == ECommerceConstants.REPUBLICATION_EMAIL_TPU_CODE )
      {
        republicationTypeOfUse = RepublicationConstants.REPUBLICATION_EMAIL;
      }
      
      if( tpuInst == ECommerceConstants.REPUBLICATION_FRAMING_TPU_CODE )
      {
        republicationTypeOfUse = RepublicationConstants.REPUBLICATION_FRAMING;
      }
      
      if( tpuInst == ECommerceConstants.REPUBLICATION_INTERNET_TPU_CODE )
      {
        republicationTypeOfUse = RepublicationConstants.REPUBLICATION_INTERNET;
      }
      
      if( tpuInst == ECommerceConstants.REPUBLICATION_INTRANET_TPU_CODE )
      {
        republicationTypeOfUse = RepublicationConstants.REPUBLICATION_INTRANET;
      }
      
      if( tpuInst == ECommerceConstants.REPUBLICATION_JOURNAL_TPU_CODE )
      {
        republicationTypeOfUse = RepublicationConstants.REPUBLICATION_JOURNAL;
      }
      
      if( tpuInst == ECommerceConstants.REPUBLICATION_LINKING_TPU_CODE )
      {
        republicationTypeOfUse = RepublicationConstants.REPUBLICATION_LINKING;
      }
      
      if( tpuInst == ECommerceConstants.REPUBLICATION_MAGAZINE_TPU_CODE )
      {
        republicationTypeOfUse = RepublicationConstants.REPUBLICATION_MAGAZINE;
      }
      
      if( tpuInst == ECommerceConstants.REPUBLICATION_NEWSLETTER_TPU_CODE )
      {
        republicationTypeOfUse = RepublicationConstants.REPUBLICATION_NEWSLETTER;
      }
      
      if( tpuInst == ECommerceConstants.REPUBLICATION_NEWSPAPER_TPU_CODE )
      {
        republicationTypeOfUse = RepublicationConstants.REPUBLICATION_NEWSPAPER;
      }
      
      if( tpuInst == ECommerceConstants.REPUBLICATION_OTHERBOOK_TPU_CODE )
      {
        republicationTypeOfUse = RepublicationConstants.REPUBLICATION_OTHERBOOK;
      }
      
      if( tpuInst == ECommerceConstants.REPUBLICATION_PAMPHLET_TPU_CODE )
      {
        republicationTypeOfUse = RepublicationConstants.REPUBLICATION_PAMPHLET;
      }
      
      if( tpuInst == ECommerceConstants.REPUBLICATION_PRESENTATION_TPU_CODE )
      {
        republicationTypeOfUse = RepublicationConstants.REPUBLICATION_PRESENTATION;
      }
      
      if( tpuInst == ECommerceConstants.REPUBLICATION_TEXTBOOK_TPU_CODE )
      {
        republicationTypeOfUse = RepublicationConstants.REPUBLICATION_TEXTBOOK;
      }
      
      if( tpuInst == ECommerceConstants.REPUBLICATION_TRADEBOOK_TPU_CODE )
      {
        republicationTypeOfUse = RepublicationConstants.REPUBLICATION_TRADEBOOK;
      }
      
            
      return republicationTypeOfUse;
      
    }else
    {
      throw new UnsupportedOperationException();
    }
    
  }

  public String getRepublishInVolEd() 
  {
      String republishInVolEd = "";

      boolean operationSupported = isRepublication();
      
      if( operationSupported ) {

    	  UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();

    	  republishInVolEd = usageDataRepublication.getRepubInVolEd();
      }

      return republishInVolEd;
  }

  public void setRepublishInVolEd(String republishInVolEd) {
	  // Not supporting this on TF side
  }
  
  public String getRepublishFullArticle() 
  {
      String republishFullArticle = "";

      boolean operationSupported = isRepublication();
      
      if( operationSupported ) {

    	  UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();

    	  republishFullArticle = usageDataRepublication.getFullArticle();
      }

      return republishFullArticle;
  }
  
  public String getRepublishNonTextPortion() 
  {
      String republishNonTextPortion = "";

      boolean operationSupported = isRepublication();
      
      if( operationSupported ) {

    	  UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();

    	  republishNonTextPortion = usageDataRepublication.getNonTextPortion();
      }

      return republishNonTextPortion;
  }

  public String getRepublishSection() 
  {
      String republishSection = "";

      boolean operationSupported = isRepublication();
      
      if( operationSupported ) {

    	  UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();

    	  republishSection = usageDataRepublication.getSection();
      }

      return republishSection;
  }

  public String getRepublishPoNumDtl() 
  {
      String republishPoNumDt = "";

      boolean operationSupported = isRepublication();
      
      if( operationSupported ) {

    	  UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();

    	  republishPoNumDt = usageDataRepublication.getPoNumDtl();
      }

      return republishPoNumDt;
  }
  
  public long getNumberOfCartoons()
  {
    long numberOfCartoons = ECommerceConstants.INVALID_RLS_QUANTITY;
    
    boolean operationSupported = isRepublication();
    
    if( operationSupported )
    {
      UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();
      
      long permissionRequestNumCartoons = usageDataRepublication.getNumCartoons();
      
      numberOfCartoons = permissionRequestNumCartoons;
     
    }else
    {
      throw new UnsupportedOperationException();
    }
    
    return numberOfCartoons;
  }

  public void setNumberOfCartoons(long numberOfCartoons)
  {
    boolean operationSupported = isRepublication();
    
    if( operationSupported )
    {
      UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();
      
      usageDataRepublication.setNumCartoons( numberOfCartoons );
      
    }else
    {
      throw new UnsupportedOperationException();
    }
  }

  public long getNumberOfCharts()
  {
    long numberOfCharts = ECommerceConstants.INVALID_RLS_QUANTITY;
    
    boolean operationSupported = isRepublication();
    
    if( operationSupported )
    {
      UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();
      
      long permissionRequestNumCharts = usageDataRepublication.getNumCharts();
      
      numberOfCharts = permissionRequestNumCharts;
      
    }else
    {
      throw new UnsupportedOperationException();
    }
    
    return numberOfCharts;
  }

  public void setNumberOfCharts(long numberOfCharts)
  {
    boolean operationSupported = isRepublication();
    
    if( operationSupported )
    {
      UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();
      
      usageDataRepublication.setNumCharts( numberOfCharts );
      
    }else
    {
      throw new UnsupportedOperationException();
    }
  }

  public long getNumberOfExcerpts()
  {
    long numberOfExcerpts = ECommerceConstants.INVALID_RLS_QUANTITY;
    
    boolean operationSupported = isRepublication();
    
    if( operationSupported )
    {
      UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();
      
      long permissionRequestNumExcerpts = usageDataRepublication.getNumExcerpts();
      
      numberOfExcerpts = permissionRequestNumExcerpts;
      
    }else
    {
      throw new UnsupportedOperationException();
    }
    
    return numberOfExcerpts;
  }

  public void setNumberOfExcerpts(long numberOfExcerpts)
  {
    boolean operationSupported = isRepublication();
    
    if( operationSupported )
    {
      UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();
      
      usageDataRepublication.setNumExcerpts( numberOfExcerpts );
      
    }else
    {
      throw new UnsupportedOperationException();
    }
  }

  public long getNumberOfFigures()
  {
    long numberOfFigures = ECommerceConstants.INVALID_RLS_QUANTITY;
    
    boolean operationSupported = isRepublication();
    
    if( operationSupported )
    {
      UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();
      
      long permissionRequestNumFigures = usageDataRepublication.getNumFigures();
      
      numberOfFigures = permissionRequestNumFigures;
      
    }else
    {
      throw new UnsupportedOperationException();
    }
    
    return numberOfFigures;
  }

  public void setNumberOfFigures(long numberOfFigures)
  {
    boolean operationSupported = isRepublication();
    
    if( operationSupported )
    {
      UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();
      
      usageDataRepublication.setNumFigures( numberOfFigures );
      
    }else
    {
      throw new UnsupportedOperationException();
    }
  }

  public long getNumberOfGraphs()
  {
    long numberOfGraphs = ECommerceConstants.INVALID_RLS_QUANTITY;
    
    boolean operationSupported = isRepublication();
    
    if( operationSupported )
    {
      UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();
      
      long permissionRequestNumGraphs = usageDataRepublication.getNumGraphs();
      
      numberOfGraphs = permissionRequestNumGraphs;
      
    }else
    {
      throw new UnsupportedOperationException();
    }
    
    return numberOfGraphs;
  }

  public void setNumberOfGraphs(long numberOfGraphs)
  {
    boolean operationSupported = isRepublication();
    
    if( operationSupported )
    {
      UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();
      
      usageDataRepublication.setNumGraphs( numberOfGraphs );
      
    }else
    {
      throw new UnsupportedOperationException();
    }
  }

  public long getNumberOfIllustrations()
  {
    long numberOfIllustrations = ECommerceConstants.INVALID_RLS_QUANTITY;
    
    boolean operationSupported = isRepublication();
    
    if( operationSupported )
    {
      UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();
      
      long permissionRequestNumIllustrations = usageDataRepublication.getNumIllustrations();
      
      numberOfIllustrations = permissionRequestNumIllustrations;
      
    }else
    {
      throw new UnsupportedOperationException();
    }
    
    return numberOfIllustrations;
  }

  public void setNumberOfIllustrations(long numberOfIllustrations)
  {
    boolean operationSupported = isRepublication();
    
    if( operationSupported )
    {
      UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();
      
      usageDataRepublication.setNumIllustrations( numberOfIllustrations );
      
    }else
    {
      throw new UnsupportedOperationException();
    }
  }

  public long getNumberOfLogos()
  {
    long numberOfLogos = ECommerceConstants.INVALID_RLS_QUANTITY;
    
    boolean operationSupported = isRepublication();
    
    if( operationSupported )
    {
      UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();
      
      long permissionRequestNumLogos = usageDataRepublication.getNumLogos();
      
      numberOfLogos = permissionRequestNumLogos;
      
    }else
    {
      throw new UnsupportedOperationException();
    }
    
    return numberOfLogos;
  }

  public void setNumberOfLogos(long numberOfLogos)
  {
    boolean operationSupported = isRepublication();
    
    if( operationSupported )
    {
      UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();
      
      usageDataRepublication.setNumLogos( numberOfLogos );
      
    }else
    {
      throw new UnsupportedOperationException();
    }
  }

  public long getNumberOfPhotos()
  {
    long numberOfPhotos = ECommerceConstants.INVALID_RLS_QUANTITY;
    
    boolean operationSupported = isRepublication();
    
    if( operationSupported )
    {
      UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();
      
      long permissionRequestNumPhotos = usageDataRepublication.getNumPhotos();
      
      numberOfPhotos = permissionRequestNumPhotos;
      
    }else
    {
      throw new UnsupportedOperationException();
    }
    
    return numberOfPhotos;
  }

  public void setNumberOfPhotos(long numberOfPhotos)
  {
    boolean operationSupported = isRepublication();
    
    if( operationSupported )
    {
      UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();
      
      usageDataRepublication.setNumPhotos( numberOfPhotos );
      
    }else
    {
      throw new UnsupportedOperationException();
    }
  }

  public long getNumberOfQuotes()
  {
    long numberOfQuotes = ECommerceConstants.INVALID_RLS_QUANTITY;
    
    boolean operationSupported = isRepublication();
    
    if( operationSupported )
    {
      UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();
      
      long permissionRequestNumQuotes = usageDataRepublication.getNumQuotes();
      
      numberOfQuotes = permissionRequestNumQuotes;
      
    }else
    {
      throw new UnsupportedOperationException();
    }
    
    return numberOfQuotes;
  }

  public void setNumberOfQuotes(long numberOfQuotes)
  {
    boolean operationSupported = isRepublication();
    
    if( operationSupported )
    {
      UsageDataRepublication usageDataRepublication = (UsageDataRepublication) getPermissionRequest().getUsageData();
      
      usageDataRepublication.setNumQuotes( numberOfQuotes );
      
    }else
    {
      throw new UnsupportedOperationException();
    }
  }

  public double getLicenseeFee()
  {
    return getPermissionRequest().getLicenseeFee();
  }

  public BigDecimal getTotalLicenseeFeeValue() {
 	return new BigDecimal(getPermissionRequest().getLicenseeFee()).setScale(2, BigDecimal.ROUND_HALF_EVEN);
  }

  
  public void setLicenseeFee(double licenseeFee)
  {
    getPermissionRequest().setLicenseeFee( licenseeFee );
  }

  public double getDiscount()
  {
    return getPermissionRequest().getDiscount();
  }

  public BigDecimal getTotalDiscountValue() {
 	    return new BigDecimal(getPermissionRequest().getDiscount()).setScale(2, BigDecimal.ROUND_HALF_EVEN);
  }
  
  public void setDiscount(double discount)
  {
    getPermissionRequest().setDiscount( discount );
  }

  public double getRoyalty()
  {
    return getPermissionRequest().getRoyaltyPayable();
  }

  public BigDecimal getTotalDistributionPayableValue() {
 	    return new BigDecimal(getPermissionRequest().getRoyaltyPayable()).setScale(2, BigDecimal.ROUND_HALF_EVEN);
  }

  public void setRoyalty(double royalty)
  {
    getPermissionRequest().setRoyaltyPayable( royalty );
  }
  
  public double getRoyaltyComposite()
  {
    return getRoyalty() + 
           getRightsholderFee();
  }

  public BigDecimal getRoyaltyCompositeValue() {
	  return new BigDecimal(this.getRoyaltyComposite()).setScale(2, BigDecimal.ROUND_HALF_EVEN);
  }
  
  public long getReasonCd()
  {
    return getPermissionRequest().getReasonCd();
  }

  public void setReasonCd(long reasonCd)
  {
    getPermissionRequest().setReasonCd(reasonCd);
  }

  public String getReasonDesc()
  {
    return getPermissionRequest().getReasonDesc();
  }

  public void setReasonDesc(String reasonDesc)
  {
    getPermissionRequest().setReasonDesc(reasonDesc);
  }

  public double getRightsholderFee()
  {
    return getPermissionRequest().getRightsholderFee();
  }

  public BigDecimal getTotalRightsholderFeeValue() {
	return new BigDecimal(getPermissionRequest().getRightsholderFee()).setScale(2, BigDecimal.ROUND_HALF_EVEN);
}

  @Override
  public void setRightholderFee(double rightholderFee)
  {
    getPermissionRequest().setRightsholderFee( rightholderFee );
  }

  public void setSpecialOrderLimitsExceeded( boolean specialOrderLimitsExceeded )
  {
    getPermissionRequest().setSpecialOrderLimitsExceeded( specialOrderLimitsExceeded );
  }

  private void setFees(RightFees fees)
  {
    this._fees = fees;
  }

  private RightFees getFees()
  {
    return _fees;
  }

  public String getBaseFee()
  {
    String baseFee = null;

    boolean feesPresent = getFees() != null;
    
    if (feesPresent)
    {
      baseFee = WebUtils.formatMoney( getFees().getBaseFee(), false );
    }
    
    return baseFee;
  }

  public String getFlatFee()
  {
    String flatFee = null;

    boolean feesPresent = getFees() != null;
    
    if (feesPresent)
    {
      flatFee = WebUtils.formatMoney( getFees().getFlatFee(), false );
    }
    
    return flatFee;
  }

  public String getPerPageFee()
  {
    String perPageFee = null;

    boolean feesPresent = getFees() != null;
    
    if (feesPresent)
    {
      perPageFee = WebUtils.formatMoney( getFees().getPerPageFee(), false );
    }
    
    return perPageFee;
  }

  public String getPerPageFeeMoneyFormat()
  {
	  return getPerPageFee();
  }
  
  public BigDecimal getPerPageFeeValue()
  {
	  if (getFees() != null) {
		  Money perPageFee = getFees().getPerPageFee();
		  if (perPageFee != null) {
			  return new BigDecimal (perPageFee.getValue());
		  }
	  }
	  
	  return null;
  }
  
  public String getBaseFeeMoneyFormat()
  {
	  return getBaseFee();
  }
  
  public BigDecimal getBaseFeeValue()
  {
	  if (getFees() != null) {
		  Money baseFee = getFees().getBaseFee();
		  if (baseFee != null) {
			  return new BigDecimal (baseFee.getValue());
		  }
	  }
	  
	  return null;
  }
  
  public String getFlatFeeMoneyFormat()
  {
	return getFlatFee();
  }
  
  public BigDecimal getFlatFeeValue()
  {
	  if (getFees() != null) {
		  Money flatFee = getFees().getFlatFee();
		  if (flatFee != null) {
			  return new BigDecimal (flatFee.getValue());
		  }
	  }
	  
	  return null;
  }
  
  private void calculateRightFees()
  {
    long rightInst = getPermissionRequest().getRgtInst();
    
    boolean validRightInst = rightInst > 0;
    
    if( validRightInst )
    {
      PricingServiceAPI pricingService = PricingServiceFactory.getInstance().getService();

      try
      {
        RightFees fees = pricingService.getFeesForRight( rightInst );

        boolean feesHaveBeenCalculated = fees != null;
        
        if ( feesHaveBeenCalculated )
        {
          setFees( fees );
        }
        
      }
      catch (Exception e)
      {
        setFees( null );
      }

    }
  }

  public String getCustomVolume()
  {
    String customVolume = null;
    
    boolean isOperationAllowed = isAcademic() ||
                                 isPhotocopy() ||
                                 isRepublication();
                                 
    if( isOperationAllowed )
    {
      
      if( isAcademic() )
      {
        customVolume = ((UsageDataAcademic)getPermissionRequest().getUsageData()).getVolume();
      }
      
      if( isPhotocopy() )
      {
        customVolume = ((UsageDataPhotocopy)getPermissionRequest().getUsageData()).getVolume();
      }
      
      if( isRepublication() )
      {
        customVolume = ((UsageDataRepublication)getPermissionRequest().getUsageData()).getVolume();
      }
      
    }else
    {
      throw new UnsupportedOperationException();
    }
    
    return customVolume;
  }

  public void setCustomVolume(String customVolume)
  {
    boolean isOperationAllowed = isAcademic() ||
                                 isPhotocopy() ||
                                 isRepublication();
                                 
    if( isOperationAllowed )
    {
      
      if( isAcademic() )
      {
        ((UsageDataAcademic)getPermissionRequest().getUsageData()).setVolume(customVolume);
      }
      
      if( isPhotocopy() )
      {
        ((UsageDataPhotocopy)getPermissionRequest().getUsageData()).setVolume(customVolume);
      }
      
      if( isRepublication() )
      {
        ((UsageDataRepublication)getPermissionRequest().getUsageData()).setVolume(customVolume);
      }
      
    }else
    {
      throw new UnsupportedOperationException();
    }
  }

  public String getCustomEdition()
  {
    String customEdition = null;
    
    boolean isOperationAllowed = isAcademic() ||
                                 isPhotocopy();
                                 
    if( isOperationAllowed )
    {
        if( isAcademic() )
        {
          customEdition = ((UsageDataAcademic)getPermissionRequest().getUsageData()).getEdition();
        }
        
        if( isPhotocopy() )
        {
          customEdition = ((UsageDataPhotocopy)getPermissionRequest().getUsageData()).getEdition();
        }
    }else
    {
      throw new UnsupportedOperationException();
    }
    
    return customEdition;
  }

  public void setCustomEdition(String customEdition)
  {
    boolean isOperationAllowed = isAcademic() ||
                                 isPhotocopy();
                                 
    if( isOperationAllowed )
    {
        if( isAcademic() )
        {
          ((UsageDataAcademic)getPermissionRequest().getUsageData()).setEdition(customEdition);
        }
        
        if( isPhotocopy() )
        {
          ((UsageDataPhotocopy)getPermissionRequest().getUsageData()).setEdition(customEdition);
        }
    }else
    {
      throw new UnsupportedOperationException();
    }
  }

    public String getExternalCommentTerm()
    {
        String externalCommentTerm = _externalCommentTerm;
        
        boolean termPresent = getRightFromWeb() != null &&
                              getRightFromWeb().getExternalComments() != null;
        
        if ( termPresent )
        {
            externalCommentTerm = getRightFromWeb().getExternalComments();
        }
        else {
            //  2009-03-04  MSJ
            //  We need to check the permissionRequest object as well,
            //  in the case of the cart, the comment is not present in
            //  the other two places it exists in this class.
            
            if (externalCommentTerm == null || "".equals(externalCommentTerm)) {
                if (_permissionRequest != null && _permissionRequest.getRight() != null) {
                    _externalCommentTerm = _permissionRequest.getRight().getExternalCommentTerm().getTermText();
                    externalCommentTerm = _externalCommentTerm;
                    
                    try {
                        _logger.debug("External Comment in PPI: " + externalCommentTerm);
                    }
                    catch(Exception e) {
                        _logger.debug("External Comment in PPI: null");
                    }
                }
            }
        }
        
        return externalCommentTerm;
    }
  
  @Override
  void setExternalCommentTerm( String term )
  {
    _externalCommentTerm = term;
  }

  public long getRightsholderInst()
  {
    if (getRightFromWeb() == null) {
        if (_permissionRequest.getRight() == null){
            return -1;
        }
        return _permissionRequest.getRight().getRightsHolderInst();
    }
    return getRightFromWeb().getRightsholderId();
  }

  void setRightsholderInst(long rightsholderInst)
  {
    getRightFromWeb().setRightsholderId(rightsholderInst);
  }
  
    public String getRightPermissionType() 
    {
        boolean rightPresent = getRightFromWeb() != null;
       String rightPermType = null;
        
        if( rightPresent )
        {
          rightPermType = getRightFromWeb().getPermission().getCode();
        }
        
        return rightPermType;
    }
    
    public void setIsSpecialOrder(boolean isSpecialOrder) {
        
    }

    @Override
    public void setPushToTFFailed(boolean pushToTFFailed) {
        this.pushToTFFailed = pushToTFFailed;
    }

    @Override
    public boolean isPushToTFFailed() {
        return pushToTFFailed;
    }

    @Override
    public void setRightFromWeb(RightAdapter rightAdapter) {
        this.rightAdapter = rightAdapter;
    }
        
    public com.copyright.data.inventory.Right getRight() {
        return null;
    }
    
    public String getRightSourceCd () {
    	return RightSourceEnum.TF.name();
    }
    
    public void setRight(com.copyright.data.inventory.Right right) {
        //empty put here to satisfy interface implementation
    }

    public String getProductCd() {
    	  if (getProductSourceKey() == ECommerceConstants.APS_PRODUCT_CODE ) {
    		  return ECommerceConstants.APS_PRODUCT_NAME;
    	  } else if (getProductSourceKey() == ECommerceConstants.DPS_PRODUCT_CODE) {
    		  return ECommerceConstants.DPS_PRODUCT_NAME;  		  
  	  } else if (getProductSourceKey() == ECommerceConstants.TRS_PRODUCT_CODE) {
    		  return ECommerceConstants.TRS_PRODUCT_NAME;  		  
    	  } else if (getProductSourceKey() == ECommerceConstants.ECCS_PRODUCT_CODE) {
  		  return ECommerceConstants.ECCS_PRODUCT_NAME;  		  
  	  } else if (getProductSourceKey() == ECommerceConstants.RLS_PRODUCT_CODE) {
  		  return ECommerceConstants.RLS_PRODUCT_NAME;  		  
  	  }
    	  return null;
      }
    
      public Long getProductSourceKey() {
    	  if (getPermissionRequest().getUsageData() != null) {
        	  return Long.valueOf(getPermissionRequest().getUsageData().getProduct());    		  
    	  }
    	  return null;
      }

      public String getProductName() {
      	if (getProductSourceKey() == ECommerceConstants.APS_PRODUCT_CODE ) {
      		return ECommerceConstants.APS_PRODUCT_NAME;
        	} else if (getProductSourceKey() == ECommerceConstants.DPS_PRODUCT_CODE) {
        		return ECommerceConstants.DPS_PRODUCT_NAME;  		  
      	} else if (getProductSourceKey() == ECommerceConstants.TRS_PRODUCT_CODE) {
        		return ECommerceConstants.TRS_PRODUCT_NAME;  		  
        	} else if (getProductSourceKey() == ECommerceConstants.ECCS_PRODUCT_CODE) {
      		return ECommerceConstants.ECCS_PRODUCT_NAME;  		  
      	} else if (getProductSourceKey() == ECommerceConstants.RLS_PRODUCT_CODE) {
      		return ECommerceConstants.RLS_PRODUCT_NAME;  		  
      	}
      	return null;
      }

      public Long getCategoryId() {
    	  return null;
      }
      
      public void setCategoryId(Long categoryId) {
    	  // Only needed for interface
      }
      
      public void setCategoryName(String categoryName) {
    	  // Only needed for interface
      }
      
      public void setTouSourceKey(Long touSourceKey) {
    	  // Only needed for interface
      }

      public void setTouName(String touName) {
    	  // Only needed for interface
      }
       
      
  	public String getCategoryCd() {

  		Long categoryId = null;
  		
	    for (ProductEnum productEnum : ProductEnum.values()) {
	    	if (this.getProductSourceKey() == productEnum.getProductSourceKey().longValue()) {
	    		categoryId = productEnum.getCategoryId();
	    	}
	    }
	    
	    for (TouCategoryTypeEnum touCategoryTypeEnum : TouCategoryTypeEnum.values()) {
	    	if (categoryId.equals(touCategoryTypeEnum.getCategoryId())) {
	    	    return touCategoryTypeEnum.name();
	    	}
	    }

//		ProductEnum productEnum = ProductEnum.getEnumForProductSourceKey(this.getProductSourceKey());

//		if (productEnum != null) {
//			return productEnum.getCategoryCode();
//		}
		return null;
	}
	
	public String getCategoryName() {

  		Long categoryId = null;
  		
	    for (ProductEnum productEnum : ProductEnum.values()) {
	    	if (this.getProductSourceKey() == productEnum.getProductSourceKey().longValue()) {
	    		categoryId = productEnum.getCategoryId();
	    	}
	    }
	    
	    for (TouCategoryTypeEnum touCategoryTypeEnum : TouCategoryTypeEnum.values()) {
	    	if (categoryId.equals(touCategoryTypeEnum.getCategoryId())) {
	    	    return touCategoryTypeEnum.getDesc();
	    	}
	    }
		
		
//		ProductEnum productEnum = ProductEnum.getEnumForProductSourceKey(this.getProductSourceKey());

//		if (productEnum != null) {
//			return productEnum.getCategoryName();
//		}
		return null;
	}
	
	public String getProductAndCategoryName() {
		return this.getProductCd() + " / " + this.getCategoryName();
	}
     
      public Long getTouSourceKey()
      {
//    	  if (getPermissionRequest().getUsageData() != null) {
//    	   	  return Long.valueOf(getPermissionRequest().getUsageData().getTpuInst());    		  
//    	  }
    	  return null;
      }
      
      public Long getExternalTouId()
      {
    	  if (getPermissionRequest().getUsageData() != null) {
    	   	  return Long.valueOf(getPermissionRequest().getUsageData().getTpuInst());    		  
    	  }
    	  return null;
       }
      
      public String getTouName()
      {
    	  return getPermissionRequest().getTypeOfUseDescription(); 
      }
    
    
    //  2009-10-22 MSJ  Matching functionality to the Publication class.
    //  Starting with getSeries... mapped new WR Work fields for the first
    //  sprint of the summit project.  These fields are temporal... they
    //  exist in the WRStandardWork object which extends our StandardWork
    //  used in data writing.  These fields are just along for the ride.
    
    public String getSeries() { 
        if (!(getPermissionRequest().getWork() instanceof WRStandardWork)) {
           WRStandardWork wrStandardWork = PurchasablePermissionFactory.buildWRWork(((StandardWork)getPermissionRequest().getWork()));
           getPermissionRequest().setWork(wrStandardWork);
        }
        return ((WRStandardWork)getPermissionRequest().getWork()).getSeries();
    }
    public String getSeriesNumber() { 
        if (!(getPermissionRequest().getWork() instanceof WRStandardWork)) {
           WRStandardWork wrStandardWork = PurchasablePermissionFactory.buildWRWork(((StandardWork)getPermissionRequest().getWork()));
           getPermissionRequest().setWork(wrStandardWork);
        }
        return ((WRStandardWork)getPermissionRequest().getWork()).getSeriesNumber();
    }
    public String getPublicationType() { 
        if (!(getPermissionRequest().getWork() instanceof WRStandardWork)) {
           WRStandardWork wrStandardWork = PurchasablePermissionFactory.buildWRWork(((StandardWork)getPermissionRequest().getWork()));
           getPermissionRequest().setWork(wrStandardWork);
        }
        return ((WRStandardWork)getPermissionRequest().getWork()).getPublicationType();
    }
    public String getCountry(){
        if (!(getPermissionRequest().getWork() instanceof WRStandardWork)) {
           WRStandardWork wrStandardWork = PurchasablePermissionFactory.buildWRWork(((StandardWork)getPermissionRequest().getWork()));
           getPermissionRequest().setWork(wrStandardWork);
        }
        return ((WRStandardWork)getPermissionRequest().getWork()).getCountry();
    }
    public String getLanguage() {
        if (!(getPermissionRequest().getWork() instanceof WRStandardWork)) {
           WRStandardWork wrStandardWork = PurchasablePermissionFactory.buildWRWork(((StandardWork)getPermissionRequest().getWork()));
           getPermissionRequest().setWork(wrStandardWork);
        }
        return ((WRStandardWork)getPermissionRequest().getWork()).getLanguage();
    }
    public String getIdnoLabel() {
        if (!(getPermissionRequest().getWork() instanceof WRStandardWork)) {
           WRStandardWork wrStandardWork = PurchasablePermissionFactory.buildWRWork(((StandardWork)getPermissionRequest().getWork()));
           getPermissionRequest().setWork(wrStandardWork);
        }
        return ((WRStandardWork)getPermissionRequest().getWork()).getIdnoLabel();
    }
    public String getIdnoTypeCd() {
        if (!(getPermissionRequest().getWork() instanceof WRStandardWork)) {
           WRStandardWork wrStandardWork = PurchasablePermissionFactory.buildWRWork(((StandardWork)getPermissionRequest().getWork()));
           getPermissionRequest().setWork(wrStandardWork);
        }
        return ((WRStandardWork)getPermissionRequest().getWork()).getIdnoTypeCd();
    }
    public String getPages() {
        if (!(getPermissionRequest().getWork() instanceof WRStandardWork)) {
           WRStandardWork wrStandardWork = PurchasablePermissionFactory.buildWRWork(((StandardWork)getPermissionRequest().getWork()));
           getPermissionRequest().setWork(wrStandardWork);
        }
        return ((WRStandardWork)getPermissionRequest().getWork()).getPages();
    }
    
    // RL Fee Fields not applicable to TF Cart
	public BigDecimal getHardCopyCost() {
		return null;
	}

	public void setHardCopyCost(BigDecimal hardCopyCost) {
		// Only needed to fulfill interface, not for TF cart item
	}

	public BigDecimal getPriceAdjustment() {
		return null;
	}

	public void setPriceAdjustment(BigDecimal priceAdjustment) {
		// Only needed to fulfill interface, not for TF cart item
	}

	public BigDecimal getShippingFee1() {
		return null;
	}

	public void setShippingFee1(BigDecimal shippingFee1) {
		// Only needed to fulfill interface, not for TF cart item
	}

	public BigDecimal getShippingFee2() {
		return null;
	}

	public void setShippingFee2(BigDecimal shippingFee2) {
		// Only needed to fulfill interface, not for TF cart item
	}

	public BigDecimal getShippingFee3() {
		return null;
	}

	public void setShippingFee3(BigDecimal shippingFee3) {
		// Only needed to fulfill interface, not for TF cart item
	}

	public BigDecimal getShippingFee4() {
		return null;
	}

	public void setShippingFee4(BigDecimal shippingFee4) {
		// Only needed to fulfill interface, not for TF cart item
	}

	public BigDecimal getShippingFee5() {
		return null;
	}

	public void setShippingFee5(BigDecimal shippingFee5) {
		// Only needed to fulfill interface, not for TF cart item
	}

	public BigDecimal getShippingFee6() {
		return null;
	}

	public void setShippingFee6(BigDecimal shippingFee6) {
		// Only needed to fulfill interface, not for TF cart item
	}
	
    public Long getRlCustomerId() {
    	return null;
		// Only needed to fulfill interface, not for TF cart item
    }
    
    public void setRlCustomerId(Long rlCustomerId) {
		// Only needed to fulfill interface, not for TF cart item
    }
    
    public List<Address> getShippingAddress()
    {
    	return null;
    }
    
    public BigDecimal getPerLogoFee() {
	    return null;
    }
  
    public BigDecimal getPerGraphFee() {
	    return null;
    }
  		
    public BigDecimal getPerCartoonFee() {
	    return null;
    }
    
    public BigDecimal getPerExcerptFee() {
	    return null;
    }
    
    public BigDecimal getPerQuoteFee() {
	    return null;
    }	
    
    public BigDecimal getPerChartFee() {
	    return null;
    }
    
    public BigDecimal getPerPhotographFee() {
	    return null;
    }
    
    public BigDecimal getPerIllustrationFee() {
	    return null;
    }
    
    public BigDecimal getPerFigureFee() {
	    return null;
    }
   	
    public BigDecimal getMaxRoyaltyFee() {
	    return null;
    }
  	
    public BigDecimal getPerArticleAuthorFee() {
	    return null;
    }
  
    public BigDecimal getPerArticleNonAuthorFee() {
	    return null;
    }
    
    public BigDecimal getTo30DaysFee() {
	    return null;
    }
  	
    public BigDecimal getTo180DaysFee() {
	    return null;
    }
  	
    public BigDecimal getTo365DaysFee() {
	    return null;
    }
  	
    public BigDecimal getUnlimitedDaysFee() {
	    return null;
    }
  	
    public BigDecimal getTo49RecipientsFee() {
	    return null;
    }
  
    public BigDecimal getTo249RecipientsFee() {
	    return null;
    }
  
    public BigDecimal getTo499RecipientsFee() {
	    return null;
    }
  
    public BigDecimal getTo500pRecipientsFee() {
	    return null;
    }
    
	
	public String getRlDetailHtml() {
  	  return null;
    }
    
    public void setRlDetailHtml(String rlDetailHtml) {
  	  // Needed for interface only 
    }
    
    public String getManagedRedirectLink() {
    	  return null;
    }
    
    public void setManagedRedirectLink(String managedRedirectLink) {
    	  // Needed for interface only 
    }
    
    public void setTfWksInst(Long tfWksInst) {
    	  // Needed for interface only 
    }

    public Long getTfWksInst() {
    	  return null;
    }
     public String getPreviewPDFUrl (){
    return null;	
    }
     public boolean isReprints() {
 		return false;
 	}
    
     public boolean getLicenseeRequestedEntireWork() {
   	  return false;
     }
     public void setLicenseeRequestedEntireWork(boolean licenseeEntireWork){
   	  
     }
       
     public String getEntireBookFeeMoneyFormat()
     {
   	  return null;
   	
     }
     
     public BigDecimal getEntireBookFeeValue()
     {
   	  return null;
     }

}
