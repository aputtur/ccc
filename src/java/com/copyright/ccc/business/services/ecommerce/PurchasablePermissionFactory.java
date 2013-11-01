package com.copyright.ccc.business.services.ecommerce;


import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.copyright.base.Constants;
import com.copyright.ccc.business.data.OrderLicense;
import com.copyright.ccc.business.data.Publication;
import com.copyright.ccc.business.data.PublicationPermission;
import com.copyright.ccc.business.data.WRStandardWork;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.business.services.cart.ECommerceConstants;
import com.copyright.ccc.business.services.cart.ECommerceUtils;
import com.copyright.ccc.business.services.search.MapperServices;
import com.copyright.ccc.business.services.search.RightAdapter;
import com.copyright.ccc.business.services.search.WorkRightsAdapter;
import com.copyright.ccc.config.ApplicationResources;
import com.copyright.ccc.web.transaction.RepublicationConstants;
import com.copyright.data.inventory.LicenseeClass;
import com.copyright.data.inventory.StandardWork;
import com.copyright.data.inventory.Work;
import com.copyright.data.order.PermissionRequest;
import com.copyright.data.order.UsageData;
import com.copyright.data.order.UsageDataAcademic;
import com.copyright.data.order.UsageDataEmail;
import com.copyright.data.order.UsageDataNet;
import com.copyright.data.order.UsageDataPhotocopy;
import com.copyright.data.order.UsageDataRepublication;
import com.copyright.domain.data.WorkExternal;
import com.copyright.svc.searchRetrieval.api.data.PublicationSearch;
import com.copyright.svc.searchRetrieval.api.data.SearchRetrievalConsumerContext;
import com.copyright.svc.searchRetrieval.api.data.SearchRetrievalResult;


/**
 * Set of factory methods reponsible for building
 * instances of <code>PurchasablePermission</code>
 */
public final class PurchasablePermissionFactory {

  private static final Logger LOGGER = Logger.getLogger( PurchasablePermissionFactory.class );

  private PurchasablePermissionFactory(){}
    
 /**
  * Creates an instance of <code>PurchasablePermission</code>
  * from an instance of <code>PublicationPermission</code>
  */
  public static PurchasablePermission createPurchasablePermission( PublicationPermission publicationPermission ) 
  throws UnableToBuildPurchasablePermissionException
  {
    
    if( publicationPermission == null )
    {
      LOGGER.error( "PublicationPermission cannot be null" );
      throw new IllegalArgumentException( "PublicationPermission cannot be null" );
    }
       
    PermissionRequest permissionRequest = buildPermissionRequest( publicationPermission );
   
    PurchasablePermission purchasablePermission = createPurchasablePermission( permissionRequest, 
                                                                               publicationPermission.getRightsholderName(),
                                                                               publicationPermission.getRightsholderInst(),
                                                                               publicationPermission.getRightsQualifyingStatement(),
                                                                               publicationPermission.getPubBeginDate(), 
                                                                               publicationPermission.getPubEndDate(),
                                                                               publicationPermission.getRightAdapter());
    
    purchasablePermission.setExternalCommentTerm( publicationPermission.getRHTerms() );
   
    return purchasablePermission;
  }
  
  /**
   * Creates a special order instance of <code>PurchasablePermission</code>, photocopy type.
   */	
	public static PurchasablePermission createPhotocopySpecialOrderPurchasablePermission(){
		
    PermissionRequest permissionRequest = new PermissionRequest(ECommerceConstants.SPECIAL_ORDER_RIGHT_INST);
    
    permissionRequest.setManualSpecialOrder( true );
    
    UsageDataPhotocopy usageDataPhotocopy =  new UsageDataPhotocopy();
	  usageDataPhotocopy.setTpuInst( ECommerceConstants.PHOTOCOPY_TPU_CODE );
	  usageDataPhotocopy.setProduct( ECommerceConstants.TRS_PRODUCT_CODE );
    
    permissionRequest.setUsageData( usageDataPhotocopy );
    
    permissionRequest.setWork( new StandardWork() );
    
	  PurchasablePermissionImpl purchasablePermission = new PurchasablePermissionImpl(permissionRequest);
	  
	  return purchasablePermission;
	}

   /**
    * Creates a special order instance of <code>PurchasablePermission</code>, e-mail type.
    */ 
	public static PurchasablePermission createEmailSpecialOrderPurchasablePermission(){
	  
    PermissionRequest permissionRequest = new PermissionRequest( ECommerceConstants.SPECIAL_ORDER_RIGHT_INST );
	  
	  permissionRequest.setManualSpecialOrder( true );
	  
	  UsageDataEmail usageDataEmail =  new UsageDataEmail();
	  usageDataEmail.setTpuInst( ECommerceConstants.EMAIL_TPU_CODE );
	  usageDataEmail.setProduct( ECommerceConstants.DPS_PRODUCT_CODE );
	  
	  permissionRequest.setUsageData( usageDataEmail );
	  
	  permissionRequest.setWork( new StandardWork() );
	  
	  PurchasablePermissionImpl purchasablePermission = new PurchasablePermissionImpl( permissionRequest );
	  
	  return purchasablePermission;
	}

  /**
   * Creates a special order instance of <code>PurchasablePermission</code>, republication type.
   */ 
	public static PurchasablePermission createRepublicationSpecialOrderPurchasablePermission( long tpuInst ){
  	      
    PermissionRequest permissionRequest = new PermissionRequest( ECommerceConstants.SPECIAL_ORDER_RIGHT_INST );
	  
	  permissionRequest.setManualSpecialOrder( true );
	  
	  UsageDataRepublication usageDataRepublication =  new UsageDataRepublication();
	  	  
	  usageDataRepublication.setProduct( ECommerceConstants.RLS_PRODUCT_CODE );
    usageDataRepublication.setTpuInst( tpuInst );
	  
	  permissionRequest.setUsageData( usageDataRepublication );
	  
	  permissionRequest.setWork( new StandardWork() );
	  
	  PurchasablePermissionImpl purchasablePermission = new PurchasablePermissionImpl( permissionRequest );
	  
	  return purchasablePermission;
	}

  /**
   * Creates a special order instance of <code>PurchasablePermission</code>, internet type.
   */
	public static PurchasablePermission createInternetSpecialOrderPurchasablePermission(){
  
	  PermissionRequest permissionRequest = new PermissionRequest( ECommerceConstants.SPECIAL_ORDER_RIGHT_INST );
	  
	  permissionRequest.setManualSpecialOrder( true );
	  
	  UsageDataNet usageDataNet =  new UsageDataNet();
	  usageDataNet.setTpuInst( ECommerceConstants.INTERNET_TPU_CODE );
	  usageDataNet.setProduct( ECommerceConstants.DPS_PRODUCT_CODE );
	  
	  permissionRequest.setUsageData( usageDataNet );
	  
	  permissionRequest.setWork( new StandardWork() );
	  
	  PurchasablePermissionImpl purchasablePermission = new PurchasablePermissionImpl( permissionRequest );
	  
	  return purchasablePermission;
	}

  /**
   * Creates a special order instance of <code>PurchasablePermission</code>, intranet type.
   */
	public static PurchasablePermission createIntranetSpecialOrderPurchasablePermission(){
  
	  PermissionRequest permissionRequest = new PermissionRequest( ECommerceConstants.SPECIAL_ORDER_RIGHT_INST );
	  
	  permissionRequest.setManualSpecialOrder( true );
	  
	  UsageDataNet usageDataNet =  new UsageDataNet();
	  usageDataNet.setTpuInst( ECommerceConstants.INTRANET_TPU_CODE );
	  usageDataNet.setProduct( ECommerceConstants.DPS_PRODUCT_CODE );
	  
	  permissionRequest.setUsageData( usageDataNet );
	
	  permissionRequest.setWork( new StandardWork() );
	  
	  PurchasablePermissionImpl purchasablePermission = new PurchasablePermissionImpl( permissionRequest );
	  
	  return purchasablePermission;
	}

  /**
   * Creates a special order instance of <code>PurchasablePermission</code>, extranet type.
   */
	public static PurchasablePermission createExtranetSpecialOrderPurchasablePermission(){
  
	  PermissionRequest permissionRequest = new PermissionRequest( ECommerceConstants.SPECIAL_ORDER_RIGHT_INST );
	  
	  permissionRequest.setManualSpecialOrder( true );
	  
	  UsageDataNet usageDataNet =  new UsageDataNet();
	  usageDataNet.setTpuInst( ECommerceConstants.EXTRANET_TPU_CODE );
	  usageDataNet.setProduct( ECommerceConstants.DPS_PRODUCT_CODE );
	  
	  permissionRequest.setUsageData( usageDataNet );
	     
	  permissionRequest.setWork( new StandardWork() );

    PurchasablePermissionImpl purchasablePermission = new PurchasablePermissionImpl(permissionRequest);
	  
    return purchasablePermission;
	}

  /**
   * Creates a special order instance of <code>PurchasablePermission</code>, APS type.
   */
  public static PurchasablePermission createAPSSpecialOrderPurchasablePermission(){
  
    PurchasablePermission purchasablePermission = createAcademicSpecialOrderPurchasablePermission();
    
    UsageData usageData = purchasablePermission.getPermissionRequest().getUsageData();
        
    usageData.setProduct( ECommerceConstants.APS_PRODUCT_CODE );
    
    usageData.setTpuInst( ECommerceConstants.APS_TPU_CODE );
    
    return purchasablePermission;
  }
  
  /**
   * Creates a special order instance of <code>PurchasablePermission</code>, ECCS type.
   */
  public static PurchasablePermission createECCSSpecialOrderPurchasablePermission(){
    
    PurchasablePermission purchasablePermission = createAcademicSpecialOrderPurchasablePermission();
    
    UsageData usageData = purchasablePermission.getPermissionRequest().getUsageData();
       
    usageData.setProduct( ECommerceConstants.ECCS_PRODUCT_CODE );
    usageData.setTpuInst( ECommerceConstants.ECCS_TPU_CODE );
    
    purchasablePermission.getPermissionRequest().setUsageData( usageData );
    
    return purchasablePermission;
  }
  
  /**
   * Creates a <code>List</code> of <code>PurchasablePermission</code> from a
   * <code>List</code> of <code>PermissionRequest</code>.
   */
  public static List<PurchasablePermission> createPurchasablePermissions( List<PermissionRequest> permissionRequests ){
    
    if( permissionRequests == null )
    {
      LOGGER.error( "PermissionRequest list cannot be null" );
      throw new IllegalArgumentException( "PermissionRequest list cannot be null" );
    }
    
    List<PurchasablePermission> purchasablePermissions = new ArrayList<PurchasablePermission>(0);
    
    if( permissionRequests.isEmpty() )
    {
      return purchasablePermissions;
    }
    
    if( !permissionRequests.isEmpty() && permissionRequests != null )
    {
     Iterator<PermissionRequest> permissionRequestsIterator = permissionRequests.iterator();
      
      while( permissionRequestsIterator.hasNext() )
      {
        try
        {
          PermissionRequest currentPermissionRequest = permissionRequestsIterator.next();
          
          PurchasablePermission currentPurchasablePermission =
                  PurchasablePermissionFactory.createPurchasablePermission( currentPermissionRequest );
          
          purchasablePermissions.add( currentPurchasablePermission );
        }
        catch ( ClassCastException e )
        {
          LOGGER.error( "PurchasablePermissionFactory: could not cast to PermissionRequest" );
          throw new UnableToBuildPurchasablePermissionException("PurchasablePermissionFactory: could not cast to PermissionRequest", e);
        }
      }
    } 
    
    return purchasablePermissions;
  }
  
  /**
   * Creates an instance of <code>PurchasablePermission</code>, based on an instance of <code>OrderLicense</code>.
   */
  public static PurchasablePermission createPurchasablePermission( OrderLicense orderLicense )
  {
      return createPurchasablePermission( orderLicense, false );
  }
  
  /**
   * Creates an instance of <code>PurchasablePermission</code>, based on an instance of <code>OrderLicense</code>.  If checkRightValidity
   * is true, then it checks to make sure that the rgtInst passed in is valid for the current date, i.e. if the rgtInst of the License
   * is no longer valid for today's date, then use -1 for the rgtInst.
   */
  public static PurchasablePermission createPurchasablePermission( OrderLicense orderLicense, boolean checkRightValidity )
  {
    if( orderLicense == null )
    {
      LOGGER.error( "OrderLicense cannot be null" );
      throw new IllegalArgumentException( "OrderLicense cannot be null" );
    }
    
    long rgtInst = orderLicense.getRgtInst();
    
    if( checkRightValidity && rgtInst > 0 )
    {
        PurchasablePermission purchasablePermission = getValidPurchasablePermissionForToday( orderLicense );
        
        if( purchasablePermission == null )
            throw new UnableToBuildPurchasablePermissionException("PurchasablePermissionFactory: could not build PurchasablePermission from OrderLicense");
        
        Work work = buildWork( orderLicense );
        UsageData usageData = buildUsageData( orderLicense );
        
        purchasablePermission.getPermissionRequest().setUsageData(usageData);
        purchasablePermission.getPermissionRequest().setWork(work);
        
        if (orderLicense.getCustomerRef() != null)
            purchasablePermission.setCustomerReference(orderLicense.getCustomerRef());
        
        return purchasablePermission;
    }
    else
    {
        PermissionRequest permissionRequest = new PermissionRequest( rgtInst );
        
        permissionRequest.setManualSpecialOrder( orderLicense.isManualSpecialOrder() );
        permissionRequest.setSpecialOrderLimitsExceeded( orderLicense.isSpecialOrderLimitsExceeded() );
        permissionRequest.setContactRHPermission( orderLicense.isContactRightsholder() );
        permissionRequest.setCustomerRef( orderLicense.getCustomerReference() );
        
        Work work = buildWork( orderLicense );
            
        permissionRequest.setWork( work );
        
        UsageData usageData = buildUsageData( orderLicense );
        
        permissionRequest.setUsageData( usageData );
        
        return createPurchasablePermission( permissionRequest );
    }
  }
  
  /**
   * Gets an instance of <code>PermissionRequest</code>
   * from an instance of <code>PurchasablePermission</code>
   */
  public static PermissionRequest getPermissionRequest( PurchasablePermission purchasablePermission )
  {
    return purchasablePermission.getPermissionRequest();
  }
    
  /**
   * Creates an instance of <code>PurchasablePermission</code>,
   * by encapsulating an instance of <code>PermissionRequest</code>
   */
  public static PurchasablePermission createPurchasablePermission( PermissionRequest permissionRequest ){
    
    if( permissionRequest == null )
    {
      LOGGER.error( "PermissionRequest cannot be null" );
      throw new IllegalArgumentException( "PermissionRequest cannot be null" );
    }
    if (LOGGER.isDebugEnabled()) {
        LOGGER.info("Creating PurchasablePermission via PermissionRequest.");
        LOGGER.info(permissionRequest.toString());
        if (permissionRequest.getRight() != null) {
            if (permissionRequest.getRight().getExternalCommentTerm() != null) {
                LOGGER.info("External Comment: " + permissionRequest.getRight().getExternalCommentTerm().getTermText());
            }
        }
        if (permissionRequest.getRight() != null) {
            if (permissionRequest.getRight().getRightQualifierTerm() != null) {
                LOGGER.info("Right Qualifier: " + permissionRequest.getRight().getRightQualifierTerm().getTermText());
            }
        }
    }
    return new PurchasablePermissionImpl( permissionRequest );
  }
  
  
  
  static PurchasablePermission createAcademicSpecialOrderPurchasablePermission(){
    PermissionRequest permissionRequest = new PermissionRequest( ECommerceConstants.SPECIAL_ORDER_RIGHT_INST );
    
    permissionRequest.setManualSpecialOrder( true );
    
    UsageDataAcademic usageDataAcademic =  new UsageDataAcademic();
       
    permissionRequest.setUsageData( usageDataAcademic );
  
    permissionRequest.setWork( new StandardWork() );
    
    return new PurchasablePermissionImpl( permissionRequest );
  }
  
  private static PurchasablePermission createPurchasablePermission( PermissionRequest permissionRequest, 
                                                                    String rightsholder,
                                                                    long rightsholderInst,
                                                                    String rightsQualifyingStatement,
                                                                    Date publicationStartDate,
                                                                    Date publicationEndDate,
                                                                    RightAdapter rightAdapter)
  {
    
    if( permissionRequest == null )
    {
      LOGGER.error( "PermissionRequest cannot be null" );
      throw new IllegalArgumentException( "PermissionRequest cannot be null" );
    }

    PurchasablePermissionImpl purchasablePermissionImpl = new PurchasablePermissionImpl( permissionRequest );
    purchasablePermissionImpl.setRightFromWeb(rightAdapter);
    purchasablePermissionImpl.setRightsholder( rightsholder );
    purchasablePermissionImpl.setRightsholderInst( rightsholderInst );
    purchasablePermissionImpl.setRightsQualifyingStatement( rightsQualifyingStatement );
    
    purchasablePermissionImpl.setPublicationStartDate( publicationStartDate );
    purchasablePermissionImpl.setPublicationEndDate( publicationEndDate );
    
    return purchasablePermissionImpl;

  }

  private static PermissionRequest buildPermissionRequest( PublicationPermission publicationPermission ) 
  throws UnableToBuildPurchasablePermissionException
  {

    LOGGER.info("\n* In buildPermissionRequest(publicationPermission)\n");
    
    PermissionRequest permissionRequest = new PermissionRequest( publicationPermission.getRgtInst() );
    
    permissionRequest.setContactRHPermission( publicationPermission.isSpecialOrder() );
    
    Work work = buildWork( publicationPermission );
    
    permissionRequest.setWork( work );
    
    UsageData usageData = buildUsageData( publicationPermission );
    
    permissionRequest.setUsageData( usageData );
    
    return permissionRequest;
  }

    /*  2009-10-23  MSJ
     *  Problematic.  We are trying to carry through information that
     *  is not really available in all our "duplicated" objects.  I will
     *  not be able (at this point in time) to guarantee that the data
     *  we want to carry through regarding new metadata fields in our
     *  works retrieved during search will be available in all the objects
     *  we use to populate work data.  Because of this I'll attempt to
     *  look up the work repository item via the work retriever (this was
     *  already partially done to pull back the wrWrkInst, I'm just taking
     *  it a step farther).
     */
    
    private static Work buildWork( PublicationPermission publicationPermission )
    {
        WRStandardWork work = new WRStandardWork();

        WorkRightsAdapter publication = (WorkRightsAdapter) publicationPermission.getPublication();
        
        work.setWrkInst( publication.getTFWrkInst() );
        work.setStandardNumber( publication.getMainIDNo() );
        work.setMainTitle( publication.getMainTitle() );
        work.setMainAuthor( publication.getMainAuthor() );
        work.setMainEditor( publication.getMainEditor() );
        work.setVolume( publication.getVolume() );
        work.setEdition( publication.getEdition() );
        work.setPublisher( publication.getMainPublisher() );
        work.setTfWksInstList(publication.getTfWksInstList()); 
        work.setWrwrkinst(publication.getWrkInst());
        
        //  2009-10-23  MSJ
        //  Additional fields mapped for summit project... to
        //  match new fields in works repository.  At some point
        //  we need to make this much more pervasive.  Since the
        //  publicationPermission object is the interface for the
        //  RightAdapter, and the RightAdapter was likely filled in
        //  by the WorkRightsAdapter, we should have a publication
        //  object containing the relevant data.
        
        work.setSeries(publication.getSeries());
        work.setSeriesNumber(publication.getSeriesNumber());
        work.setPublicationType(publication.getPublicationType());
        work.setCountry(publication.getCountry());
        work.setLanguage(publication.getLanguage());
        work.setIdnoLabel(publication.getIdnoLabel());
        work.setPages(publication.getPages());
        
        return work;
    }
      
    private static Work buildWork( OrderLicense orderLicense )
    {
        long tfWrkInst = orderLicense.getWorkInst();
        long wrWrkInst = 0;
        
        //  The WRStandardWork extends the StandardWork
        //  object which implements our shared library
        //  version of the "Work" object (as opposed to the
        //  WorkRetriever service version of Work).
        
        WRStandardWork work = new WRStandardWork();
        
        //  I am using the full path name as a reminder that
        //  this code should be revisited in the future.
        
        WorkExternal wrWork = getWRWork(tfWrkInst);
        
        if (wrWork != null) {
            wrWrkInst = wrWork.getPrimaryKey();
            work.setSeries(wrWork.getSeries());
            work.setSeriesNumber(wrWork.getSeriesNumber());
            work.setPublicationType(wrWork.getPublicationType());
            work.setCountry(wrWork.getCountry());
            work.setLanguage(wrWork.getLanguage());

            //  2012-04-05 MSJ
            //  Something has changed with the data we are getting back from
            //  our search retrieval service.  For whatever reason the IDNO
            //  TYPE CD is coming back null in GateWay works/orders.
            //  CC-3408.  I've chosen a reasonable default for the label.

            if (wrWork.getIdnoTypeCd() != null) {
                work.setIdnoLabel(ApplicationResources.getInstance().getIdnoLabel(wrWork.getIdnoTypeCd().toString()));
            }
            else {
                work.setIdnoLabel("ISBN/ISSN");
            }
            work.setPages(wrWork.getPages() != null ? wrWork.getPages().toString() : null);
        }
        else {
            LOGGER.warn("Additional Publication metadata is not available for order license.");
            work.setSeries(null);
            work.setSeriesNumber(null);
            work.setPublicationType(null);
            work.setCountry(null);
            work.setLanguage(null);
            work.setIdnoLabel("ISBN/ISSN");
            work.setPages(null);
        }
        work.setWrwrkinst(wrWrkInst);
        work.setWrkInst(tfWrkInst);
        work.setStandardNumber( orderLicense.getStandardNumber() );
        work.setMainTitle( StringUtils.upperCase(orderLicense.getPublicationTitle()) );
        work.setMainAuthor( orderLicense.getAuthor() );
        work.setMainEditor( orderLicense.getEditor() );
        work.setVolume( orderLicense.getVolume() );
        work.setEdition( orderLicense.getEdition() );
        work.setPublisher( orderLicense.getPublisher() );
        work.setPublicationYear(  orderLicense.getPublicationYear() );
         
        return work;
    }
    
    //  2010-01-05  MSJ
    //  Added this for special order cases.
    
    public static Work buildWork(Publication pub) {
        long tfWrkInst = pub.getTFWrkInst();
      //  long wrWrkInst = 0;
        
        //  The WRStandardWork extends the StandardWork
        //  object which implements our shared library
        //  version of the "Work" object (as opposed to the
        //  WorkRetriever service version of Work).
        
        WRStandardWork work = new WRStandardWork();
        
        //  I am using the full path name as a reminder that
        //  this code should be revisited in the future.
        
        WorkExternal wrWork = getWRWork(tfWrkInst);
        
        if (wrWork != null) {
         //   wrWrkInst = wrWork.getPrimaryKey();
            work.setSeries(wrWork.getSeries());
            work.setSeriesNumber(wrWork.getSeriesNumber());
            work.setPublicationType(wrWork.getPublicationType());
            work.setCountry(wrWork.getCountry());
            work.setLanguage(wrWork.getLanguage());
            work.setIdnoLabel(ApplicationResources.getInstance().getIdnoLabel(wrWork.getIdnoTypeCd().toString()));
            work.setPages(wrWork.getPages() != null ? wrWork.getPages().toString() : null);
        }
        else {
            LOGGER.warn("Additional metadata is not available for publication.");
            work.setSeries(null);
            work.setSeriesNumber(null);
            work.setPublicationType(null);
            work.setCountry(null);
            work.setLanguage(null);
            work.setIdnoLabel("ISBN/ISSN");
            work.setPages(null);
        }
        work.setWrkInst(pub.getTFWrkInst());
        work.setStandardNumber(pub.getMainIDNo());
        work.setMainTitle(pub.getMainTitle());
        work.setMainAuthor(pub.getMainAuthor());
        work.setMainEditor(pub.getMainEditor());
        work.setPublisher(pub.getMainPublisher());
        work.setVolume(pub.getVolume());
        work.setEdition(pub.getEdition());
            
        return work;
    }
      
    public static WRStandardWork buildWRWork(StandardWork standardWork) {
        long tfWrkInst = standardWork.getWrkInst();
        long wrWrkInst = 0;
        
        //  The WRStandardWork extends the StandardWork
        //  object which implements our shared library
        //  version of the "Work" object (as opposed to the
        //  WorkRetriever service version of Work).
        
        WRStandardWork work = new WRStandardWork();
        
        //  I am using the full path name as a reminder that
        //  this code should be revisited in the future.
        
        WorkExternal wrWork = getWRWork(tfWrkInst);
          
        if (wrWork != null) {
            wrWrkInst = wrWork.getPrimaryKey();
            work.setSeries(wrWork.getSeries());
            work.setSeriesNumber(wrWork.getSeriesNumber());
            work.setPublicationType(wrWork.getPublicationType());
            work.setCountry(wrWork.getCountry());
            work.setLanguage(wrWork.getLanguage());
            work.setIdnoLabel(ApplicationResources.getInstance().getIdnoLabel(wrWork.getIdnoTypeCd().toString()));
            work.setPages(wrWork.getPages() != null ? wrWork.getPages().toString() : null);
        }
        else {
            LOGGER.warn("Additional Publication metadata is not available for standard work.");
            work.setSeries(null);
            work.setSeriesNumber(null);
            work.setPublicationType(null);
            work.setCountry(null);
            work.setLanguage(null);
            work.setIdnoLabel("ISBN/ISSN");
            work.setPages(null);
        }
        work.setEdition(standardWork.getEdition());
        work.setMainAuthor(standardWork.getMainAuthor());
        work.setMainEditor(standardWork.getMainEditor());
        work.setMainTitle(standardWork.getMainTitle());
        work.setPublicationYear(standardWork.getPublicationYear());
        work.setPublisher(standardWork.getPublisher());
        work.setStandardNumber(standardWork.getStandardNumber());
        work.setVolume(standardWork.getVolume());
        work.setWrkInst(tfWrkInst);
        work.setTypeCd(standardWork.getTypeCd());
        work.setWrwrkinst(wrWrkInst);
        work.setTfWksInstList(null);
    
        return work;
    }
  
  public static long getWRWorkInst(long tfWorkInst) 
  {
	  List<String> sortList = new ArrayList<String>();
	  int page = 1;
	  int pageSize = 100;
	  
	  PublicationSearch publicationSearch = new PublicationSearch();
	  publicationSearch.setTfWrkInst(String.valueOf(tfWorkInst));
	  SearchRetrievalResult result = ServiceLocator.getSearchRetrievalService().searchPublication(new SearchRetrievalConsumerContext(), publicationSearch, sortList, page, pageSize);
	  List<WorkExternal> lstWork = result.getWorks();

	  if (lstWork == null || lstWork.size() == 0) {
          //didn't find the WR work inst
          return 0;
      }
	  
      return lstWork.get(0).getPrimaryKey();
  }
  
  //  2009-10-23  MSJ
  //  Looking for a more complete solution to populating the WRStandardWork.
  //  This will give me my metadata coming in from an order or any other direction
  //  I hope.
  
  public static WorkExternal getWRWork(long tfWorkInst) 
  {
	  List<String> sortList = new ArrayList<String>();
	  int page = 1;
	  int pageSize = 100;
	  
	  PublicationSearch publicationSearch = new PublicationSearch();
	  publicationSearch.setTfWrkInst(String.valueOf(tfWorkInst));
	  SearchRetrievalResult result = ServiceLocator.getSearchRetrievalService().searchPublication(new SearchRetrievalConsumerContext(), publicationSearch, sortList, page, pageSize);
	  List<WorkExternal> lstWork = result.getWorks();
      
      if (lstWork == null || lstWork.size() == 0) {
          //didn't find the WR work inst
          return null;
      }
      
      return lstWork.get(0);
  }

  private static UsageData buildUsageData( PublicationPermission publicationPermission ) 
  throws UnableToBuildPurchasablePermissionException
  {
    UsageData usageData = null;
    
    if( isAcademic( publicationPermission ) )
    {
      usageData = buildUsageDataAcademic( publicationPermission );  
    }
    
    if( isRepublication( publicationPermission ) )
    {
      usageData = buildUsageDataRepublication( publicationPermission );
    }
    
    if( isPhotocopy( publicationPermission ) )
    {
      usageData = buildUsageDataPhotocopy( publicationPermission );
    }
    
    if( isEmail( publicationPermission ) )
    {
      usageData = buildUsageDataEmail( publicationPermission );
    }
    
    if( isNet( publicationPermission ) )
    {
      usageData = buildUsageDataNet( publicationPermission );
    }
    
    if( usageData == null )
    {
      throw new UnableToBuildPurchasablePermissionException( "Unable to build an instance of UsageData" );
    }
    
    return usageData;
  }

  private static UsageDataAcademic buildUsageDataAcademic( PublicationPermission publicationPermission )
  {
    UsageDataAcademic usageDataAcademic = new UsageDataAcademic();
    
    usageDataAcademic = (UsageDataAcademic) populateCommonUsageData( usageDataAcademic, publicationPermission );
    
    usageDataAcademic.setAuthor( publicationPermission.getPublication().getMainAuthor() );
    usageDataAcademic.setTypeOfUseDisplay( publicationPermission.getTypeOfUse() );
    usageDataAcademic.setEdition(publicationPermission.getPublication().getEdition());
    usageDataAcademic.setVolume( publicationPermission.getPublication().getVolume() );
    
    return usageDataAcademic;
  }
  
  private static UsageData buildUsageDataRepublication( PublicationPermission publicationPermission )
  {
    
    UsageDataRepublication usageDataRepublication = new UsageDataRepublication();
    
    usageDataRepublication = (UsageDataRepublication) populateCommonUsageData( usageDataRepublication, publicationPermission );
    
    return usageDataRepublication;
  }
  
  private static UsageData buildUsageDataEmail(PublicationPermission publicationPermission )
  {
    UsageDataEmail usageDataEmail = new UsageDataEmail();
    
    usageDataEmail = (UsageDataEmail) populateCommonUsageData( usageDataEmail, publicationPermission );
    
    return usageDataEmail;
  }
  
  private static UsageData buildUsageDataNet(PublicationPermission publicationPermission)
  {
    UsageDataNet usageDataNet = new UsageDataNet();
    
    usageDataNet = (UsageDataNet) populateCommonUsageData( usageDataNet, publicationPermission );
    
    return usageDataNet;
  }
  
  private static UsageData buildUsageDataPhotocopy(PublicationPermission publicationPermission )
  {
    UsageDataPhotocopy usageDataPhotocopy = new UsageDataPhotocopy();
    
    usageDataPhotocopy = (UsageDataPhotocopy) populateCommonUsageData( usageDataPhotocopy, publicationPermission );
    
    return usageDataPhotocopy;
  }
  
  private static UsageData populateCommonUsageData( UsageData usageData, PublicationPermission publicationPermission )
  {
    UsageData updatedUsageData = usageData;
    
    updatedUsageData.setProduct( publicationPermission.getPrdInst() );
    updatedUsageData.setTpuInst( publicationPermission.getTpuInst() );
    updatedUsageData.setTypeOfUseDisplay( publicationPermission.getTypeOfUse() );
           
    return updatedUsageData;
  }
  
  private static UsageData buildUsageData( OrderLicense orderLicense )
  {
    UsageData usageData = null;
    
    if( orderLicense.isAcademic() )
    {
      usageData = buildUsageDataAcademic( orderLicense );  
    }
    
    if( orderLicense.isRepublication() )
    {
      usageData = buildUsageDataRepublication( orderLicense );
    }
    
    if( orderLicense.isPhotocopy() )
    {
      usageData = buildUsageDataPhotocopy( orderLicense );
    }
    
    if( orderLicense.isEmail() )
    {
      usageData = buildUsageDataEmail( orderLicense );
    }
    
    if( orderLicense.isNet() )
    {
      usageData = buildUsageDataNet( orderLicense );
    }
    
    if( usageData == null )
    {
     throw new UnableToBuildPurchasablePermissionException( "Unable to build an instance of UsageData" );
    }
    
    return usageData;
  }

  private static boolean isAcademic( PublicationPermission publicationPermission )
  {
    return isAPS( publicationPermission ) ||
           isECCS( publicationPermission );
  }
  
  private static boolean isAPS( PublicationPermission publicationPermission )
  {
    return publicationPermission.getPrdInst() == ECommerceConstants.APS_PRODUCT_CODE;
  }
  
  private static boolean isECCS( PublicationPermission publicationPermission )
  {
    return publicationPermission.getPrdInst() == ECommerceConstants.ECCS_PRODUCT_CODE;
  }
  
  private static boolean isRepublication( PublicationPermission publicationPermission )
  {
    return publicationPermission.getPrdInst() == ECommerceConstants.RLS_PRODUCT_CODE;
  }

  private static boolean isEmail( PublicationPermission publicationPermission )
  {
    return publicationPermission.getTpuInst() == ECommerceConstants.EMAIL_TPU_CODE &&
           publicationPermission.getPrdInst() == ECommerceConstants.DPS_PRODUCT_CODE;
  }

  private static boolean isPhotocopy( PublicationPermission publicationPermission )
  {
    return publicationPermission.getPrdInst() == ECommerceConstants.TRS_PRODUCT_CODE;
  }

  private static boolean isNet( PublicationPermission publicationPermission )
  {
    return   publicationPermission.getPrdInst() == ECommerceConstants.DPS_PRODUCT_CODE &&
           ( publicationPermission.getTpuInst() == ECommerceConstants.EXTRANET_TPU_CODE ||
             publicationPermission.getTpuInst() == ECommerceConstants.INTRANET_TPU_CODE ||
             publicationPermission.getTpuInst() == ECommerceConstants.INTERNET_TPU_CODE );
  }


  private static UsageData buildUsageDataAcademic( OrderLicense orderLicense )
  {   
    
    UsageDataAcademic usageDataAcademic = new UsageDataAcademic();
    
    populateCommonUsageData( usageDataAcademic, orderLicense );
    
    usageDataAcademic.setNumPages( orderLicense.getNumberOfPages() );
    usageDataAcademic.setNumStudents( (long) orderLicense.getNumberOfStudents() );
    usageDataAcademic.setChapterArticle( orderLicense.getChapterArticle() );
    usageDataAcademic.setEdition( orderLicense.getEdition() );
    usageDataAcademic.setVolume( orderLicense.getVolume() );
    usageDataAcademic.setPageRanges( orderLicense.getPageRange() );
    usageDataAcademic.setDateOfIssue( orderLicense.getDateOfIssue() );
    
    usageDataAcademic.setLcnRequestedEntireBook(orderLicense.getLicenseeRequestedEntireWork());
    
    return usageDataAcademic;
  }

  private static UsageData buildUsageDataRepublication( OrderLicense orderLicense )
  {
       
    UsageDataRepublication usageDataRepublication = new UsageDataRepublication();
    
    //common data
    populateCommonUsageData( usageDataRepublication, orderLicense );
    
    //rlsPages
    usageDataRepublication.setRlsPages( orderLicense.getNumberOfPages() );
    
    //chapterArticle
    usageDataRepublication.setChapterArticle( orderLicense.getChapterArticle() );
    
    //circulation
    usageDataRepublication.setCirculation( orderLicense.getCirculationDistribution() );
    
    //forProfit
    if( RepublicationConstants.BUSINESS_FOR_PROFIT.equals(orderLicense.getBusiness()) )
    {
      usageDataRepublication.setForProfit( RepublicationConstants.FOR_PROFIT );
      
    }else
    {
      usageDataRepublication.setForProfit( RepublicationConstants.NON_FOR_PROFIT );
    }
    
    //content types...
    usageDataRepublication = ECommerceUtils.setTypeOfContent( usageDataRepublication, orderLicense.getTypeOfContent() );
       
    //origAuthor
    if( orderLicense.isSubmitterAuthor() )
    {
      usageDataRepublication.setOrigAuthor( RepublicationConstants.SUBMITTER_IS_AUTHOR );           
    }else
    {
      usageDataRepublication.setOrigAuthor( RepublicationConstants.SUBMITTER_IS_NOT_AUTHOR );
    }
    
    //publicationDate
     usageDataRepublication.setPublicationDate( orderLicense.getContentsPublicationDate() );
     
    //hdrRepubPub
     usageDataRepublication.setHdrRepubPub( StringUtils.upperCase(orderLicense.getRepublishingOrganization()) );
    
    //repubTitle
    usageDataRepublication.setRepubTitle( StringUtils.upperCase(orderLicense.getNewPublicationTitle()) );
    
    //repubDate
     usageDataRepublication.setRepubDate( orderLicense.getRepublicationDate() );
     
    //language & translated
    if( orderLicense.getTranslationLanguage().equals( RepublicationConstants.NO_TRANSLATION ) )
    {
      usageDataRepublication.setLanguage( Constants.EMPTY_STRING );
      usageDataRepublication.setTranslated( RepublicationConstants.NOT_TRANSLATED );
    }else
    {
      usageDataRepublication.setLanguage( orderLicense.getTranslationLanguage() );
      usageDataRepublication.setTranslated( RepublicationConstants.TRANSLATED );
    }
    
    //lcnHdrRefNum
    usageDataRepublication.setLcnHdrRefNum( orderLicense.getYourReference() );
    
    //author
    usageDataRepublication.setAuthor( orderLicense.getCustomAuthor() );
    
    usageDataRepublication.setPageRanges( orderLicense.getPageRange() );
    
    return usageDataRepublication;
  }

  private static UsageData buildUsageDataPhotocopy(OrderLicense orderLicense)
  {
    UsageDataPhotocopy usageDataPhotocopy = new UsageDataPhotocopy();
    
    populateCommonUsageData( usageDataPhotocopy, orderLicense );
    
    usageDataPhotocopy.setNumCopies( orderLicense.getNumberOfCopies() );
    usageDataPhotocopy.setNumPages( orderLicense.getNumberOfPages() );
    usageDataPhotocopy.setChapterArticle( orderLicense.getChapterArticle() );
    usageDataPhotocopy.setEditor( orderLicense.getEditor() );
    usageDataPhotocopy.setEdition( orderLicense.getEdition() );
    usageDataPhotocopy.setVolume( orderLicense.getVolume() );
    
    return usageDataPhotocopy;
  }

  private static UsageData buildUsageDataEmail(OrderLicense orderLicense)
  {
    UsageDataEmail usageDataEmail = new UsageDataEmail();
    
    populateCommonUsageData( usageDataEmail, orderLicense );
    
    usageDataEmail.setChapterArticle( orderLicense.getChapterArticle() );
    usageDataEmail.setNumRecipients( orderLicense.getNumberOfRecipients() );
    usageDataEmail.setDateOfUse( orderLicense.getDateOfUse() );
    
    return usageDataEmail;
  
  }

  private static UsageData buildUsageDataNet(OrderLicense orderLicense)
  {
    UsageDataNet usageDataNet = new UsageDataNet();
    
    populateCommonUsageData( usageDataNet, orderLicense );
    
    usageDataNet.setChapterArticle( orderLicense.getChapterArticle() );
    usageDataNet.setDateOfUse( orderLicense.getDateOfUse() );
    usageDataNet.setDuration( orderLicense.getDuration() );
    usageDataNet.setWebAddress( orderLicense.getWebAddress() );
    
    return usageDataNet;
  }

  private static void populateCommonUsageData( UsageData usageData, OrderLicense orderLicense )
  {
    usageData.setProduct( orderLicense.getPrdInst() );
    usageData.setTpuInst( orderLicense.getTpuInst() );
    
    usageData.setTypeOfUseDisplay( orderLicense.getPermissionSelected() );

    usageData.setPublicationDate( orderLicense.getPublicationDateOfUse() ); 
  }
  
    private static PurchasablePermission getValidPurchasablePermissionForToday( OrderLicense orderLicense )
    {
        //  2009-09-18 MSJ
        //  The internal data WorkImpl class was eliminated and simply exposed
        //  as the Work class in the service's "api" classpath.
        //
        //  NEED TO Substitute since orderlicense does not carry a WR Work Inst
        //  first build a WorkImpl object which is associated with search, then 
        //  create a WorkRightAdapter
        
        //  2010-01-04  MSJ
        //  Adding code to look up the WR Work here because copies were failing
        //  during a call to buildWork from the faked publicationPermission.  If
        //  the work is STILL null, go ahead and build our fake.  In the future if
        //  TFWrkInst becomes only one of several possibilities of how work data
        //  is referenced (works from external sources, not in our database and
        //  ONLY referenced by a WR Work primary key) this will have to be
        //  revisited.
        
    	WorkExternal work = null;
        work = getWRWork(orderLicense.getWorkInst());
        
        if (work == null) {
            work = new WorkExternal();
            work.setTfWrkInst(orderLicense.getWorkInst());
            //  fake a wr work inst since it is only used as a hash key and we know we
            //  will only get back one result
            work.setPrimaryKey(12345678L);
            work.setAuthorName(orderLicense.getAuthor());
            work.setIdno(orderLicense.getStandardNumber());
            work.setEdition(orderLicense.getEdition());
            work.setEditorName(orderLicense.getEditor());
            work.setPublisherName(orderLicense.getPublisher());
            work.setFullTitle(orderLicense.getPublicationTitle());
        }
        
        WorkRightsAdapter workRightAdapter = new WorkRightsAdapter(work,true);
        Publication publication = workRightAdapter;

        int usageDescriptorTypeOfUseCode = MapperServices.getUsageDescriptorFromTpuInstAndPrdInst( 
                                          orderLicense.getTpuInst(), orderLicense.getPrdInst() );
      
        int licenseeClass = LicenseeClass.LIC_CLASS_ALL;
        if( orderLicense.isRepublication() )
            licenseeClass = orderLicense.getBusiness().equals( RepublicationConstants.BUSINESS_FOR_PROFIT ) ?
                LicenseeClass.LIC_CLASS_FOR_PROFIT : LicenseeClass.LIC_CLASS_NOT_FOR_PROFIT;
          
        PublicationPermission[] publicationPermissions = 
            publication.getPublicationPermissions( usageDescriptorTypeOfUseCode, licenseeClass );
          
        if( publicationPermissions == null ) return null;
        
        Date publicationDate = orderLicense.isRepublication() ? orderLicense.getContentsPublicationDate() : orderLicense.getPublicationDateOfUse();
          
        PublicationPermission publicationPermission = null;
        
        for( int i = 0; i < publicationPermissions.length; i++ )
        {
            if ( (publicationPermissions[i].getUsageDescriptor().getTypeOfUse() == usageDescriptorTypeOfUseCode ) &&
                //(publicationPermissions[i].getLicenseeClass() == licenseeClass) &&
                 (publicationPermissions[i].getPubBeginDate().compareTo( publicationDate ) <= 0) &&
                 (publicationPermissions[i].getPubEndDate().compareTo(publicationDate) >= 0))
            {
                publicationPermission = publicationPermissions[i];
            }
        }
        
        if( publicationPermission == null ) return null;
        
        return createPurchasablePermission( publicationPermission );
    }
}
