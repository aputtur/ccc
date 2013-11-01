package com.copyright.ccc.business.services.cart;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.copyright.base.Constants;
import com.copyright.base.enums.DurationEnum;
import com.copyright.ccc.business.data.WRStandardWork;
import com.copyright.ccc.business.services.ItemAvailabilityEnum;
import com.copyright.ccc.business.services.ItemConstants;
import com.copyright.ccc.business.services.ItemDescriptionParmEnum;
import com.copyright.ccc.business.services.ItemHelperServices;
import com.copyright.ccc.business.services.ItemParmEnum;
import com.copyright.ccc.business.services.ProductEnum;
import com.copyright.ccc.business.services.RightSourceEnum;
import com.copyright.ccc.business.services.search.RightAdapter;
import com.copyright.ccc.util.LogUtil;
import com.copyright.ccc.web.transaction.RepublicationConstants;
import com.copyright.ccc.web.transaction.TransactionConstants;
import com.copyright.ccc.web.util.WebUtils;
import com.copyright.data.account.Address;
import com.copyright.data.account.Country;
import com.copyright.data.order.PermissionRequest;
import com.copyright.data.pricing.RightFees;
import com.copyright.service.pricing.PricingServiceAPI;
import com.copyright.service.pricing.PricingServiceFactory;
import com.copyright.svc.order.api.data.Item;
import com.copyright.svc.order.api.data.ItemFees;
import com.copyright.svc.order.api.data.ItemParm;
import com.copyright.svc.order.api.data.ItemStatusEnum;
import com.copyright.svc.order.api.data.SpecialOrderTypeEnum;
import com.copyright.svc.rightsResolver.api.data.RlSourceBeanEnum;
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

private static final Logger LOGGER = Logger.getLogger( PurchasablePermissionImpl.class );

  private static final int RLS_PAGES_ZERO_QUANTITY = 0;
  private static final long INVALID_RIGHTSHOLDER_INST = -1L;
  private static final String productCdECCS = "ECCS";
  private static final String productCdECC = "ECC";

//  private PermissionRequest _permissionRequest;

  private Item _item;

  private int       _duration = ECommerceConstants.INVALID_DURATION;
  private String    _webAddress = Constants.EMPTY_STRING;
  private long      _numberOfRecipients = ECommerceConstants.INVALID_NUMBER_OF_RECIPIENTS;
  private String    _rightsholder = Constants.EMPTY_STRING;
  private String    _rightsQualifyingStatement = Constants.EMPTY_STRING;
  private Date      _publicationStartDate = null;
  private Date      _publicationEndDate = null;
  private long      _rightsholderInst = INVALID_RIGHTSHOLDER_INST;
  private String    _externalCommentTerm = null;
  private boolean   pushToTFFailed = false;
  private RightAdapter     rightAdapter;
   
  
  protected PurchasablePermissionImpl()
  {
	  _item = new Item();
  }
  
  PurchasablePermissionImpl( Item item )
  {
//    setPermissionRequest( permissionRequest );

	setItem (item);
	
//	calculateRightFees();
	     
  }
  
  public Long getBundleId()
  {
	  return _item.getBundleId();
  }
  
  public void setBundleId(Long bundleId)
  {
	  _item.setBundleId(bundleId);
  }
  
  @Deprecated 
  public String getPublicationTitle()
  {
//    String publicationTitle = getItem().getItemDescription();
   
    return getItem().getItemDescription();
  }
  
  @Deprecated 
  public void setPublicationTitle(String publicationTitle)
  {
	getItem().setItemDescription(publicationTitle);
//    getPermissionRequest().getWork().setMainTitle( StringUtils.upperCase(publicationTitle) );
  }
  
  
  public String getItemDescription()
  {
    String itemDescription = getItem().getItemDescription();
    
    return itemDescription;
  }

  public void setItemDescription(String itemDescription)
  {
    getItem().setItemDescription(itemDescription);
  }

  public String getItemSubDescription()
  {
    String itemSubDescription = getItem().getItemSubDescription();
    
    return itemSubDescription;
  }

  public void setItemSubDescription(String itemSubDescription)
  {
    getItem().setItemSubDescription(itemSubDescription);
  }

	public String getItemSourceCd() {
		return getItem().getItemSourceCd();
	}
	
    public void setItemSourceCd(String itemSourceCd) {
    	getItem().setItemSourceCd(itemSourceCd);
    }

	public String getItemSubSourceCd() {
		return getItem().getItemSubSourceCd();
	}
	
    public void setItemSubSourceCd(String itemSubSourceCd) {
     	getItem().setItemSubSourceCd(itemSubSourceCd);
    }

	public Long getItemSubSourceKey() {
		return getItem().getItemSubSourceKey(); // WR Work Inst
	}

    public void setItemSubSourceKey(Long itemSubSourceKey) {
    	getItem().setItemSubSourceKey(itemSubSourceKey); // WR Work Inst
    }

  
  public String getGranularWorkAuthor() {
  	return ItemHelperServices.getItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.GRANULAR_WORK_AUTHOR.name());
  }

  public void setGranularWorkAuthor(String granularWorkAuthor) {
  	ItemHelperServices.setItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.GRANULAR_WORK_AUTHOR.name(), granularWorkAuthor);
  }
    
  public String getGranularWorkDoi() {
  	return ItemHelperServices.getItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.GRANULAR_WORK_DOI.name());
  }

  public void setGranularWorkDoi(String granularWorkDoi) {
  	ItemHelperServices.setItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.GRANULAR_WORK_DOI.name(), granularWorkDoi);
  }

  public String getGranularWorkStartPage() {
  	return ItemHelperServices.getItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.GRANULAR_WORK_START_PAGE.name());
  }

  public void setGranularWorkStartPage(String granularWorkStartPage) {
  	ItemHelperServices.setItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.GRANULAR_WORK_START_PAGE.name(), granularWorkStartPage);
  }

  public String getGranularWorkEndPage() {
  	return ItemHelperServices.getItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.GRANULAR_WORK_END_PAGE.name());
  }

  public void setGranularWorkEndPage(String granularWorkEndPage) {
  	ItemHelperServices.setItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.GRANULAR_WORK_END_PAGE.name(), granularWorkEndPage);
  }

  public String getGranularWorkPageRange() {
  	return ItemHelperServices.getItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.GRANULAR_WORK_PAGE_RANGE.name());
  }

  public void setGranularWorkPageRange(String granularWorkPageRange) {
  	ItemHelperServices.setItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.GRANULAR_WORK_PAGE_RANGE.name(), granularWorkPageRange);
  }

  public Date getGranularWorkPublicationDate() {
  	return ItemHelperServices.getItemDescriptionParmDate(getItem(), ItemDescriptionParmEnum.GRANULAR_WORK_PUBLICATION_DATE.name());
  }

  public void setGranularWorkPublicationDate(Date granularWorkPublicationDate) {
  	ItemHelperServices.setItemDescriptionParmDate(getItem(), ItemDescriptionParmEnum.GRANULAR_WORK_PUBLICATION_DATE.name(), granularWorkPublicationDate);
  }

  public String getGranularWorkVolume() {
  	return ItemHelperServices.getItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.GRANULAR_WORK_VOLUME.name());
  }

  public void setGranularWorkVolume(String granularWorkVolume) {
  	ItemHelperServices.setItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.GRANULAR_WORK_VOLUME.name(), granularWorkVolume);
  }
    
  public String getGranularWorkIssue() {
  	return ItemHelperServices.getItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.GRANULAR_WORK_ISSUE.name());
  }

  public void setGranularWorkIssue(String granularWorkIssue) {
  	ItemHelperServices.setItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.GRANULAR_WORK_ISSUE.name(), granularWorkIssue);
  }

  public String getGranularWorkNumber() {
  	return ItemHelperServices.getItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.GRANULAR_WORK_NUMBER.name());
  }

  public void setGranularWorkNumber(String granularWorkNumber) {
  	ItemHelperServices.setItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.GRANULAR_WORK_NUMBER.name(), granularWorkNumber);
  }

  public String getGranularWorkSeason() {
  	return ItemHelperServices.getItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.GRANULAR_WORK_SEASON.name());
  }

  public void setGranularWorkSeason(String granularWorkSeason) {
  	ItemHelperServices.setItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.GRANULAR_WORK_SEASON.name(), granularWorkSeason);
  }

  public String getGranularWorkQuarter() {
  	return ItemHelperServices.getItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.GRANULAR_WORK_QUARTER.name());
  }

  public void setGranularWorkQuarter(String granularWorkQuarter) {
  	ItemHelperServices.setItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.GRANULAR_WORK_QUARTER.name(), granularWorkQuarter);
  }

  public String getGranularWorkWeek() {
  	return ItemHelperServices.getItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.GRANULAR_WORK_WEEK.name());
  }

  public void setGranularWorkWeek(String granularWorkWeek) {
  	ItemHelperServices.setItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.GRANULAR_WORK_WEEK.name(), granularWorkWeek);
  }

  public String getGranularWorkSection() {
  	return ItemHelperServices.getItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.GRANULAR_WORK_SECTION.name());
  }

  public void setGranularWorkSection(String granularWorkSection) {
  	ItemHelperServices.setItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.GRANULAR_WORK_SECTION.name(), granularWorkSection);
  }

  
  public long getTfWrkInst()
  {
	if (getItem().getExternalItemId() != null) {
		return getItem().getExternalItemId().longValue();
	} else {
  		return 0;
  	}
	  
  }
  
  public void setTfWrkInst(long wrkInst)
  {
    getItem().setExternalItemId(Long.valueOf(wrkInst));
  }
  
  public Long getExternalItemId()
  {
	  return getItem().getExternalItemId();
	  //    return getPermissionRequest().getWork().getWrkInst();
  }
  
  public void setExternalItemId(Long wrkInst)
  {
    getItem().setExternalItemId(wrkInst);
  }
  
  @Deprecated
  public Long getWorkInst()
  {
	  return getItem().getExternalItemId();
	  //    return getPermissionRequest().getWork().getWrkInst();
  }
  
  @Deprecated
  public void setWorkInst(Long wrkInst)
  {
    getItem().setExternalItemId(wrkInst);
  }
  
  @Override
  public WRStandardWork getWork() {
      
	  WRStandardWork work = new WRStandardWork();
	  
      work.setWrkInst( this.getWorkInst() );
      work.setStandardNumber( this.getStandardNumber() );
      work.setMainTitle( this.getPublicationTitle() );
      work.setMainAuthor( this.getAuthor() );
      work.setMainEditor( this.getEditor());
      work.setVolume( this.getVolume() );
      work.setEdition( this.getEdition() );
      work.setPublisher( this.getPublisher() );
      work.setWrwrkinst(this.getWrWorkInst());
      
     
      work.setSeries(this.getSeries());
      work.setSeriesNumber(this.getSeriesNumber());
      work.setPublicationType(this.getPublicationType());
      work.setCountry(this.getCountry());
      work.setLanguage(this.getLanguage());
      work.setIdnoLabel(this.getIdnoLabel());
      work.setIdnoTypeCd(this.getIdnoTypeCd());
      work.setPages(this.getPages());
	  
	  return work;

  }

  public void setTfWksInst(Long tfWksInst) {
      getItem().setTfWksInst(tfWksInst);
  }

  public Long getTfWksInst() {
      return getItem().getTfWksInst();
  }
  
  
  public Long getWrWorkInst()
  {
//		if (getItem().getItemSourceKey() != null) {
			  return getItem().getItemSourceKey();
//		} else {
//	  		return 0;
//	  	}

	  
  }
  
  public long getRgtInst()
  {
	  return getItem().getExternalRightId();
//    return getPermissionRequest().getRgtInst();
  }
  
  
  public String getPublisher()
  {
//    String publisher = getPermissionRequest().getWork().getPublisher();
	  String publisher = ItemHelperServices.getItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.PUBLISHER.name());

	  return publisher;
  }
  
  public void setPublisher(String publisher)
  {
//    getPermissionRequest().getWork().setPublisher( publisher );
	  ItemHelperServices.setItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.PUBLISHER.name(), publisher);
  }
  
  public String getPublicationYear()
  {
	  String publicationYear = ItemHelperServices.getItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.PUBLICATION_YEAR.name());
//	  String publicationYear = getPermissionRequest().getWork().getPublicationYear();
    
    return publicationYear;
  }

  public void setPublicationYear(String publicationYear)
  {
//    getPermissionRequest().getWork().setPublicationYear( publicationYear );
	  ItemHelperServices.setItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.PUBLICATION_YEAR.name(), publicationYear);
  }
  
/*
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

*/    
  @Deprecated
  @Override
  public RightAdapter getRightFromWeb() 
    {     
	  
	  if (rightAdapter != null)
        {
           return rightAdapter;
        }
        else {
            return null;
        }
    }

  public String getRightsQualifyingStatement()
  {
//      String rightsQualifyingStatement = _rightsQualifyingStatement;
      return getItem().getWorkQualifierTerms();
  }

  void setRightsQualifyingStatement( String rightsQualifyingStatement )
  {
    getItem().setWorkQualifierTerms(rightsQualifyingStatement);
  }

  public String getRightQualifierTerms() {
      return getItem().getRightQualifierTerms();

  }
  
  public void setRightQualifierTerms(String rightQualifierTerms) {
      getItem().setRightQualifierTerms(rightQualifierTerms);	  
  }
  
  public String getLicenseTerms() {
      return getItem().getLicenseTerms();	  
  }
  
  public void setLicenseTerms(String LicenseTerms) {
      getItem().setLicenseTerms(LicenseTerms);	  
  }

  public String getWorkQualifierTerms() {
      return getItem().getWorkQualifierTerms();	  
  }
  
  public void setWorkQualifierTerms(String WorkQualifierTerms) {
      getItem().setWorkQualifierTerms(WorkQualifierTerms);	  
  }

  public String getResolutionTerms() {
      return getItem().getResolutionTerms();	  
  }
  
  public void setResolutionTerms(String ResolutionTerms) {
      getItem().setResolutionTerms(ResolutionTerms);	  
  }

  public String getSpecialConfirmationTerms() {
      return getItem().getSpecialConfirmationTerms();	  
  }
  
  public void setSpecialConfirmationTerms(String SpecialConfirmationTerms) {
      getItem().setSpecialConfirmationTerms(SpecialConfirmationTerms);	  
  }

  public String getItemTypeCd()
  {
    return getItem().getItemTypeCd();
    
  }
  
  public void setItemTypeCd(String itemTypeCd)
  {
	  getItem().setItemTypeCd(itemTypeCd);
  }
  
  
  public String getStandardNumber()
  {
    String standardNumber = ItemHelperServices.getItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.STANDARD_NUMBER.name());
    
    return standardNumber;
  }
  
  public void setStandardNumber(String standardNumber)
  {
	  ItemHelperServices.setItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.STANDARD_NUMBER.name(), standardNumber);
	  //    getPermissionRequest().getWork().setStandardNumber( standardNumber );
  }
  
  public String getAuthor()
  {
    
	String author = ItemHelperServices.getItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.AUTHOR.name());
 //   String author = getPermissionRequest().getWork().getMainAuthor();
    
    return author;
  }

  public void setAuthor(String author)
  {
	  ItemHelperServices.setItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.AUTHOR.name(), author);
//	  getPermissionRequest().getWork().setMainAuthor( author );
  }

  public String getEditor()
  {
	String editor = ItemHelperServices.getItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.EDITOR.name());
//    String editor = getPermissionRequest().getWork().getMainEditor();
    
    return editor;
  }
  
  public void setEditor(String editor)
  {
	ItemHelperServices.setItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.EDITOR.name(), editor);
//    getPermissionRequest().getWork().setMainEditor( editor );
  }
    
  public String getVolume()
  {
	  String volume = ItemHelperServices.getItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.VOLUME.name());
    
	  return volume;
  }
  
  public void setVolume(String volume)
  {
	  ItemHelperServices.setItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.VOLUME.name(), volume);
  }

  public String getEdition()
  {
	  String edition = ItemHelperServices.getItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.EDITION.name());
    
	  return edition;
  }
  
  public void setEdition(String edition)
  {
	  ItemHelperServices.setItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.EDITION.name(), edition);
  }

  public String getSeries()
  {
	  return ItemHelperServices.getItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.SERIES.name());
    
  }
  
  public void setSeries(String series)
  {
	  ItemHelperServices.setItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.SERIES.name(), series);
  }

  public String getSeriesNumber()
  {
	  return ItemHelperServices.getItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.SERIES_NUMBER.name());
    
  }
  
  public void setSeriesNumber(String seriesNumber)
  {
	  ItemHelperServices.setItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.SERIES_NUMBER.name(), seriesNumber);
  }

  public String getPublicationType()
  {
	  return ItemHelperServices.getItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.PUBLICATION_TYPE.name());
    
  }
  
  public void setPublicationType(String publicationType)
  {
	  ItemHelperServices.setItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.PUBLICATION_TYPE.name(), publicationType);
  }
  
  public String getCountry()
  {
	  return ItemHelperServices.getItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.COUNTRY.name());
    
  }
  
  public void setCountry(String country)
  {
	  ItemHelperServices.setItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.COUNTRY.name(), country);
  }

  public String getLanguage()
  {
	  return ItemHelperServices.getItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.LANGUAGE.name());
    
  }
  
  public void setLanguage(String language)
  {
	  ItemHelperServices.setItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.LANGUAGE.name(), language);
  }
  
  public String getIdnoLabel()
  {
	  return ItemHelperServices.getItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.IDNO_LABEL.name());
    
  }
  
  public void setIdnoLabel(String idnoLabel)
  {
	  ItemHelperServices.setItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.IDNO_LABEL.name(), idnoLabel);
  }
  
  public String getIdnoTypeCd()
  {
	  return ItemHelperServices.getItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.IDNO_TYPE_CD.name());
    
  }
  
  public void setIdnoTypeCd(String idnoTypeCd)
  {
	  ItemHelperServices.setItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.IDNO_TYPE_CD.name(), idnoTypeCd);
  }

  public String getPages()
  {
	  return ItemHelperServices.getItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.PAGES.name());
    
  }
  
  public void setPages(String pages)
  {
	  ItemHelperServices.setItemDescriptionParmString(getItem(), ItemDescriptionParmEnum.PAGES.name(), pages);
  }

/*  TODO MAY NEED TO ADD THESE
	public static final String RUN_PUB_START_DATE = "RUN_PUB_START_DATE";
	public static final String RUN_PUB_END_DATE = "RUN_PUB_END_DATE";
	public static final String OCLC_NUMBER = "OCLC_NUMBER";
	public static final String IDNO_WOP = "IDNO_WOP";
*/  
  
  
  @Deprecated 
  public String getPermissionSelected()
  {
//    String permissionSelected = getPermissionRequest().getUsageData().getTypeOfUseDisplay();
	  String permissionSelected = getItem().getTouName();

	  return permissionSelected;
  }
  
  @Deprecated 
  public void setPermissionSelected(String permissionSelected)
  {
	  getItem().setTouName(permissionSelected);
	  
//	  getPermissionRequest().getUsageData().setTypeOfUseDisplay( permissionSelected );
  }

  public void setRightAvailabilityCd(String rightAvailabilityCd)
  {
	  getItem().setRightAvailabilityCd(rightAvailabilityCd);
  }

  public String getRightAvailabilityCd()
  {
	  return getItem().getRightAvailabilityCd();
  }
  
  public void setItemAvailabilityCd(String itemAvailabilityCd)
  {
	  getItem().setItemAvailabilityCd(itemAvailabilityCd);
	  
//	  getPermissionRequest().getUsageData().setTypeOfUseDisplay( permissionSelected );
  }

  public String getItemAvailabilityCd()
  {
	  
	  if (getItem().getItemAvailabilityCd() == "CR") {
		  getItem().setItemAvailabilityCd("C");
	  }
	  if (getItem().getItemAvailabilityCd() == "CRD") {
		  getItem().setItemAvailabilityCd("I");
	  }
	  return getItem().getItemAvailabilityCd();
//	  getPermissionRequest().getUsageData().setTypeOfUseDisplay( permissionSelected );
  }

  public String getItemOrigAvailabilityCd() {
	  
	  if (getItem().getItemOrigAvailabilityCd() == "CR") {
		  getItem().setItemOrigAvailabilityCd("C");
	  }
	  if (getItem().getItemOrigAvailabilityCd() == "CRD") {
		  getItem().setItemOrigAvailabilityCd("I");
	  }
	  
	  return getItem().getItemOrigAvailabilityCd();  
  }
 
  public void setItemOrigAvailabilityCd(String itemOrigAvailabilityCd)
  {
	  getItem().setItemOrigAvailabilityCd(itemOrigAvailabilityCd);
		  
//		  getPermissionRequest().getUsageData().setTypeOfUseDisplay( permissionSelected );
  }
  
  public String getItemStatusCd() {
	if (getItem().getItemStatusCd() != null) {
		return getItem().getItemStatusCd().name();
	}
	return null;
  }

  public void setItemStatusCd(String itemStatusCd) {
	  for (ItemStatusEnum itemStatusEnum : ItemStatusEnum.values()) {
		  if (itemStatusEnum.name().equalsIgnoreCase(itemStatusCd)) {
			  getItem().setItemStatusCd(itemStatusEnum);
		  }
	  }
  }
	
  public String getItemAvailabilityDescription()
  {
	  ItemAvailabilityEnum itemAvailabilityEnum = ItemAvailabilityEnum.getEnumForItemAvailabilityCd(this.getItemAvailabilityCd());

  	if (itemAvailabilityEnum != null) {    	
		return itemAvailabilityEnum.getExternalOrderLabel();
	}
	return null;
	  
  }
  
  public String getItemOrigAvailabilityDescription() {
		ItemAvailabilityEnum itemAvailabilityEnum = ItemAvailabilityEnum.getEnumForItemAvailabilityCd(this.getItemOrigAvailabilityCd());

	  	if (itemAvailabilityEnum != null) {    	
			return itemAvailabilityEnum.getExternalOrderLabel();
		}
		return null;
		  
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
		ItemAvailabilityEnum itemAvailabilityEnum = ItemAvailabilityEnum.getEnumForItemAvailabilityCd(this.getItemOrigAvailabilityCd());

	  	if (itemAvailabilityEnum != null) {    	
			return itemAvailabilityEnum.getOrderLabel();
		}
		return null;
		  
  }
  
  public String getTouCategoryCd()
  {
	  return getItem().getCategoryCd();
  }
  
  public void setCategoryCd(String CategoryCd) {
      getItem().setCategoryCd(CategoryCd);	  
  }
  
  public Long getCategoryId() {
	  return getItem().getCategoryId();
  }
  
  public void setCategoryId(Long categoryId) {
	  getItem().setCategoryId(categoryId);
  }
     
  public String getCategoryCd() {
	  return getItem().getCategoryCd(); 
  }
  
  public void setCategoryName(String CategoryName) {
      getItem().setCategoryName(CategoryName);	  
  }
  
  public String getCategoryName()
  {
	  return getItem().getCategoryName(); 
  }
 
  public String getProductAndCategoryName() {
	  return this.getProductCd() + " / " + this.getCategoryName();
  }

  
  public Long getTouSourceKey()
  {
	  return getItem().getTouSourceKey(); 
  }
 
  public void setTouSourceKey(Long touSourceKey) {
      getItem().setTouSourceKey(touSourceKey);	  
  } 
  
  public Long getExternalTouId()
  {
	  return getItem().getExternalTouId(); 
  }
 
  public void setExternalTouId(Long externalTouId) {
      getItem().setExternalTouId(externalTouId);	  
  } 
  
  public String getTouName()
  {
	  return getItem().getTouName(); 
  }
 
  public void setTouName(String touName) {
      getItem().setTouName(touName);	  
  } 
  
  public String getPrice() {
//  	BigDecimal price = getItem().getTotalPrice();
  	if (RightSourceEnum.TF.name().equals(getRightSourceCd())) {
		if (!getSpecialOrderTypeCd().equals(SpecialOrderTypeEnum.NOT_SPECIAL_ORDER.name())) {
    		return ItemConstants.COST_TBD;  		
		}
	}


  	if (getRightSourceCd().equals(RightSourceEnum.RL.name()) && 
  		getProductCd().equals(ProductEnum.RL.name()) && 
  		!getItemAvailabilityCd().equals(ItemAvailabilityEnum.PURCHASE.getStandardPermissionCd())) {
      		return ItemConstants.COST_TBD;
  	}

  	if (getRightSourceCd().equals(RightSourceEnum.RL.name()) && 
      		getProductCd().equals(ProductEnum.RLR.name()) && 
      		getPriceValueRaw() == ItemConstants.RL_NOT_PRICED) {
          		return ItemConstants.COST_TBD;
     	}
  	
  	BigDecimal price = getTotalPriceValue();
  	
  	Money moneyPrice;
  	
  	if (price != null ) {
      	moneyPrice = new Money(price.doubleValue());    		
  	} else {
  		moneyPrice = new Money(0);
  	}
	  
  	return WebUtils.formatMoney(moneyPrice);

  }

  public double getPriceValue() {
  	if (getTotalPriceValue() != null) {
    	return getTotalPriceValue().doubleValue();
	} else {
		return 0;
	}
  }

  public double getPriceValueRaw() {
  	if (getTotalPriceValue() != null) {
      	return getTotalPriceValueRaw().doubleValue();
  	} else {
  		return 0;
  	}
  }
  
  public BigDecimal getTotalPriceValue() {

		if (getItem().getRightSourceCd().equalsIgnoreCase(RightSourceEnum.RL.name())) {
			if (getTotalPriceValueRaw().doubleValue() == ItemConstants.RL_NOT_PRICED) {
	    		return new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_EVEN);
			} 
		}

  	return getTotalPriceValueRaw();
  }
  
  public BigDecimal getTotalPriceValueRaw() {
	    	
  	if (this.getRightSourceCd().equals(RightSourceEnum.TF.name())) {
      	BigDecimal totalPriceValue = new BigDecimal(0);   
		if (this.getTotalRightsholderFeeValue()!=null) {
           	totalPriceValue = totalPriceValue.add(this.getTotalRightsholderFeeValue());   			
		}
		if (this.getTotalLicenseeFeeValue()!=null) {
			totalPriceValue = totalPriceValue.add(this.getTotalLicenseeFeeValue());
		}
		if (this.getTotalDistributionPayableValue()!=null) {
			totalPriceValue = totalPriceValue.add(this.getTotalDistributionPayableValue());
		}
		if (this.getTotalDiscountValue()!=null) {
			totalPriceValue = totalPriceValue.add(this.getTotalDiscountValue());   
		}
      	return totalPriceValue.setScale(2, BigDecimal.ROUND_HALF_EVEN);    		
  	} 

  	if (getItem().getTotalPrice() == null) {
  		return new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_EVEN);
  	}
  	
  	return getItem().getTotalPrice().setScale(2, BigDecimal.ROUND_HALF_EVEN);
  	
  }
  
  
  public String getProductCd() {
	  if (getItem().getProductCd().equalsIgnoreCase("ECCS")) {
		  getItem().setProductCd("ECC");
	  }
	  
	  return getItem().getProductCd();
  }

  public void setProductCd(String productCd) {
	  if (productCd.equals(productCdECCS)) {
		  getItem().setProductCd(productCdECC);
		  return;
	  }

	  getItem().setProductCd(productCd);	  
	  	  
  }

  public Long getProductSourceKey() {
	  return getItem().getProductSourceKey();
  }

  public void setProductSourceKey(Long productSourceKey) {
	  getItem().setProductSourceKey(productSourceKey);
  }

  public String getProductName() {
	  return getItem().getProductName();
  }

  public void setProductName(String productName) {
	  getItem().setProductName(productName);
  }
  
  public String getRightSourceCd() {
	return getItem().getRightSourceCd();
  }

  public void setRightSourceCd(String rightSourceCd) {
	getItem().setRightSourceCd(rightSourceCd);
  }

  public Long getRightId() {
	return getItem().getRightId();
  }

  public Long getExternalRightId() {
 		return getItem().getExternalRightId();
  }
  
  public void setExternalRightId(Long externalRightId) {
		getItem().setExternalRightId(externalRightId);
  }
  
  public void setRightId(Long rightId) {
	getItem().setRightId(rightId);
  }
 
  public Date getPinningDate() {
      return getItem().getPinningDtm();
  }
 
  public void setPinningDate(Date pinningDtm) {
      getItem().setPinningDtm(pinningDtm);
  }

  

  public String getCustomerReference()
  {
    String yourReference = getItem().getLicenseeRefNum();
    
    return yourReference;
  }

  public void setCustomerReference(String yourReference)
  {
	  getItem().setLicenseeRefNum(yourReference);
  }

  public String getPublicationYearOfUse()
  {
	  String publicationYearBeingUsed = Constants.EMPTY_STRING;
	  
	  Date publicationDateOfUse = getPublicationDateOfUse();
	  
	  if (publicationDateOfUse != null) {
		  try {
	          publicationYearBeingUsed = ECommerceUtils.getYearYYYY( publicationDateOfUse );
	      } catch (ParseException e) {
	    	  LOGGER.warn("error parsing " + publicationDateOfUse + LogUtil.appendableStack(e));
	    	  // Do nothing will return empty string
	      }
	  }

	  return publicationYearBeingUsed;
  }
  
@Override
public void setWorkId(String workId){
	 ItemHelperServices.setItemParmString(getItem(), ItemParmEnum.WORKID.name(), workId);
	 getItem().getItemParms().get(ItemParmEnum.WORKID.name()).setRlSourceBeanCd(RlSourceBeanEnum.ORDER.name()) ;
}

@Override
public void setWhichWork(String whichWork){
	 ItemHelperServices.setItemParmString(getItem(), ItemParmEnum.WHICHWORK.name(), whichWork);
	 getItem().getItemParms().get(ItemParmEnum.WHICHWORK.name()).setRlSourceBeanCd(RlSourceBeanEnum.ORDER.name()) ;
}
  public void setPublicationYearOfUse( String publicationYearBeingUsed )
  {
//	  ItemHelperServices.setItemParmString(getItem(), ItemParmEnum.PUBLICATION_YEAR_OF_USE.name(), publicationYearBeingUsed);

	  Date dateOfPublicationUsed;
      try
      {
    	  	dateOfPublicationUsed = ECommerceUtils.transformYearToDate( publicationYearBeingUsed );
      } catch ( ParseException e ) {
    	  LOGGER.warn("error parsing " + publicationYearBeingUsed + LogUtil.appendableStack(e));
    	  dateOfPublicationUsed = null;
      }
      catch (Exception npe)
		{
    	  LOGGER.warn("error converting year to date " + publicationYearBeingUsed + LogUtil.appendableStack(npe));
    	  dateOfPublicationUsed = null;
		}

	  ItemHelperServices.setItemParmDate(getItem(), ItemParmEnum.DATE_OF_PUBLICATION_USED.name(), dateOfPublicationUsed);
  }

  public long getNumberOfPages()
  {
  	if (isRepublication()) {
   	    BigDecimal numberOfPages = ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.RLS_PAGES.name());
		if (numberOfPages != null) {
			  return numberOfPages.longValue();
		} else {
	  		return 0;
	  	}
	} else {
   	    BigDecimal numberOfPages = ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.NUM_PAGES.name());
		if (numberOfPages != null) {
			  return numberOfPages.longValue();
		} else {
	  		return 0;
	  	}
	}
  	

  }

  public void setNumberOfPages( long numberOfPages )
  {
	  if (isRepublication()) {
		  ItemHelperServices.setItemParmNumber(getItem(), ItemParmEnum.RLS_PAGES.name(), new BigDecimal (numberOfPages));		  		  
	  } else {
		  ItemHelperServices.setItemParmNumber(getItem(), ItemParmEnum.NUM_PAGES.name(), new BigDecimal (numberOfPages));		  
	  }
	 
  }

  public long getNumberOfCopies()
  {
    BigDecimal numberOfCopies = ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.NUM_COPIES.name());
    
    long numCopies = 0;
    
    if (numberOfCopies != null)
    {
	    numCopies = numberOfCopies.longValue();
    }
    
    return numCopies;

  }

  public void setNumberOfCopies(long numberOfCopies)
  {
	  ItemHelperServices.setItemParmNumber(getItem(), ItemParmEnum.NUM_COPIES.name(), new BigDecimal (numberOfCopies));
  }

  public int getNumberOfStudents()
  {
    BigDecimal numberOfStudents = ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.NUM_COPIES.name());
    
    int numStudents = 0;
    
    if (numberOfStudents != null)
    {
    	numStudents = numberOfStudents.intValue();
    }
	    
    return numStudents;


  }

  public void setNumberOfStudents(int numberOfStudents)
  {
	  ItemHelperServices.setItemParmNumber(getItem(), ItemParmEnum.NUM_COPIES.name(), new BigDecimal (numberOfStudents));
  }

  public int getCirculationDistribution()
  {
    BigDecimal circulationDistribution = ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.CIRCULATION_DISTRIBUTION.name());
    
    int circDist = 0;
    
    if (circulationDistribution != null)
    {	    
         circDist = circulationDistribution.intValue();
    }
    
    return circDist;

  }

  public void setCirculationDistribution( int circulationDistribution )
  {
	  ItemHelperServices.setItemParmNumber(getItem(), ItemParmEnum.CIRCULATION_DISTRIBUTION.name(), new BigDecimal (circulationDistribution));

  }


  public String getBusiness()
  {
  
	  String business = ItemHelperServices.getItemParmString(getItem(), ItemParmEnum.BUSINESS.name());

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
      if( business.equals( RepublicationConstants.BUSINESS_FOR_PROFIT ) )
      {
    	  ItemHelperServices.setItemParmString(getItem(), ItemParmEnum.BUSINESS.name(), RepublicationConstants.BUSINESS_FOR_PROFIT);
    	  ItemHelperServices.setItemParmString(getItem(), ItemParmEnum.FOR_PROFIT.name(), ItemConstants.YES_CD);
      } else 
      {
    	  ItemHelperServices.setItemParmString(getItem(), ItemParmEnum.BUSINESS.name(), RepublicationConstants.BUSINESS_NON_FOR_PROFIT);
    	  ItemHelperServices.setItemParmString(getItem(), ItemParmEnum.FOR_PROFIT.name(), ItemConstants.NO_CD);
      }
	  
  }

  public String getTypeOfContent()
  {
    String typeOfContent = ItemHelperServices.getItemParmString(getItem(), ItemParmEnum.TYPE_OF_CONTENT.name());
	    
    return typeOfContent;
  }
  
  public String getRepublishFullArticle()
  {
    String fullArticle = ItemHelperServices.getItemParmString(getItem(), ItemParmEnum.FULL_ARTICLE.name());
    
    return fullArticle;
  }

  public void setRepublishFullArticle(String fullArticle)
  {
	  ItemHelperServices.setItemParmString(getItem(), ItemParmEnum.FULL_ARTICLE.name(), fullArticle);
  }
  
  public void setTypeOfContent( String typeOfContent )
  {
	ItemHelperServices.setItemParmString(getItem(), ItemParmEnum.TYPE_OF_CONTENT.name(), typeOfContent);

	this.setRepublishFullArticle(ECommerceConstants.FULL_ARTICLE_NO);
    this.setNumberOfExcerpts( ECommerceConstants.CONTENT_TYPE_ZERO_QUANTITY );
    this.setNumberOfQuotes( ECommerceConstants.CONTENT_TYPE_ZERO_QUANTITY );
    this.setNumberOfCharts( ECommerceConstants.CONTENT_TYPE_ZERO_QUANTITY );
    this.setNumberOfGraphs( ECommerceConstants.CONTENT_TYPE_ZERO_QUANTITY );
    this.setNumberOfFigures( ECommerceConstants.CONTENT_TYPE_ZERO_QUANTITY );
    this.setNumberOfPhotos( ECommerceConstants.CONTENT_TYPE_ZERO_QUANTITY );
    this.setNumberOfCartoons( ECommerceConstants.CONTENT_TYPE_ZERO_QUANTITY );
    this.setNumberOfIllustrations( ECommerceConstants.CONTENT_TYPE_ZERO_QUANTITY );
    
    if( !typeOfContent.equals( RepublicationConstants.CONTENT_SELECTED_PAGES ) ) {
    	this.setNumberOfPages(ECommerceConstants.CONTENT_TYPE_ZERO_QUANTITY);
    }
    
    if( typeOfContent.equals( RepublicationConstants.CONTENT_FULL_ARTICLE_CHAPTER )  )
    {
      this.setRepublishFullArticle( ECommerceConstants.FULL_ARTICLE_YES );
    }
    else if( typeOfContent.equals( RepublicationConstants.CONTENT_EXCERPT ) )
    {
      this.setNumberOfExcerpts( ECommerceConstants.CONTENT_TYPE_UNITARY_QUANTITY );
  
    }else if( typeOfContent.equals( RepublicationConstants.CONTENT_QUOTATION ) )
    {
      this.setNumberOfQuotes( ECommerceConstants.CONTENT_TYPE_UNITARY_QUANTITY );
  
    }else if( typeOfContent.equals( RepublicationConstants.CONTENT_CHART ) )
    {
      this.setNumberOfCharts( ECommerceConstants.CONTENT_TYPE_UNITARY_QUANTITY );
  
    }else if( typeOfContent.equals( RepublicationConstants.CONTENT_GRAPH ) )
    {
      this.setNumberOfGraphs( ECommerceConstants.CONTENT_TYPE_UNITARY_QUANTITY );
  
    }else if( typeOfContent.equals( RepublicationConstants.CONTENT_FIGURE_DIAGRAM_TABLE ) )
    {
      this.setNumberOfFigures( ECommerceConstants.CONTENT_TYPE_UNITARY_QUANTITY );
  
    }else if( typeOfContent.equals( RepublicationConstants.CONTENT_PHOTOGRAPH ) )
    {
      this.setNumberOfPhotos( ECommerceConstants.CONTENT_TYPE_UNITARY_QUANTITY );
  
    }else if( typeOfContent.equals( RepublicationConstants.CONTENT_CARTOONS) )
    {
      this.setNumberOfCartoons( ECommerceConstants.CONTENT_TYPE_UNITARY_QUANTITY );
    }
    else if( typeOfContent.equals( RepublicationConstants.CONTENT_ILLUSTRATION ) )
    {
      this.setNumberOfIllustrations( ECommerceConstants.CONTENT_TYPE_UNITARY_QUANTITY );
  
    }else if( typeOfContent.equals( RepublicationConstants.CONTENT_SELECTED_PAGES ) )
    {
  //do nothing      
    }else
    {
      throw new IllegalArgumentException( "Invalid content type." );
    } 

  
  }  
  public String getTypeOfContentDescription()
  {

      
      String typeOfContentDescription = Constants.EMPTY_STRING;

      if (getTypeOfContent().equalsIgnoreCase(RepublicationConstants.CONTENT_FULL_ARTICLE_CHAPTER)) {
      	typeOfContentDescription = TransactionConstants.CONTENT_FULL_ARTICLE_CHAPTER;
      } else if (getTypeOfContent().equalsIgnoreCase(RepublicationConstants.CONTENT_EXCERPT)) {
      	typeOfContentDescription = TransactionConstants.CONTENT_EXCERPT;
      } else if (getTypeOfContent().equalsIgnoreCase(RepublicationConstants.CONTENT_QUOTATION)) {
      	typeOfContentDescription = TransactionConstants.CONTENT_QUOTATION;
      } else if (getTypeOfContent().equalsIgnoreCase(RepublicationConstants.CONTENT_CHART)) {
      	typeOfContentDescription = TransactionConstants.CONTENT_CHART;
      } else if (getTypeOfContent().equalsIgnoreCase(RepublicationConstants.CONTENT_FIGURE_DIAGRAM_TABLE)) {
      	typeOfContentDescription = TransactionConstants.CONTENT_FIGURE_DIAGRAM_TABLE;
      } else if (getTypeOfContent().equalsIgnoreCase(RepublicationConstants.CONTENT_PHOTOGRAPH)) {
      	typeOfContentDescription = TransactionConstants.CONTENT_PHOTOGRAPH;
      } else if (getTypeOfContent().equalsIgnoreCase(RepublicationConstants.CONTENT_CARTOONS)) {
      	typeOfContentDescription = TransactionConstants.CONTENT_CARTOONS;
      } else if (getTypeOfContent().equalsIgnoreCase(RepublicationConstants.CONTENT_ILLUSTRATION)) {
      	typeOfContentDescription = TransactionConstants.CONTENT_ILLUSTRATION;
      } else if (getTypeOfContent().equalsIgnoreCase(RepublicationConstants.CONTENT_GRAPH)) {
      	typeOfContentDescription = TransactionConstants.CONTENT_GRAPH;
      } else if (getTypeOfContent().equalsIgnoreCase(RepublicationConstants.CONTENT_SELECTED_PAGES)) {
      	typeOfContentDescription = TransactionConstants.CONTENT_SELECTED_PAGES;
      }

           
      return typeOfContentDescription;
  }

  public boolean isSubmitterAuthor()
  {
	  String isSubmitterAuthorYN = ItemHelperServices.getItemParmString(getItem(), ItemParmEnum.IS_AUTHOR.name());

	  boolean isSubmitterAuthor = false;

	  if (isSubmitterAuthorYN == null) {
		  return isSubmitterAuthor;
	  }
	  
	  if (isSubmitterAuthorYN.equalsIgnoreCase(ItemConstants.YES_CD)) {
		  isSubmitterAuthor = true;
	  }
	  
	  return isSubmitterAuthor;
  }
  
  public String getSubmitterAuthor() {

  	String isSubmitterAuthorYN = ItemHelperServices.getItemParmString(getItem(), ItemParmEnum.IS_AUTHOR.name());

	  	return isSubmitterAuthorYN;
  }
  
  public void setSubmitterAuthor( boolean isSubmitterAuthor )
  {
	  String isSubmitterAuthorYN = ItemConstants.NO_CD;
	  
	  if (isSubmitterAuthor) {
		  isSubmitterAuthorYN = ItemConstants.YES_CD;
	  }
	  
	  ItemHelperServices.setItemParmString(getItem(), ItemParmEnum.IS_AUTHOR.name(), isSubmitterAuthorYN);
  }  
  
  public Date getContentsPublicationDate()
  {
    Date contentsPublicationDate = ItemHelperServices.getItemParmDate(getItem(), ItemParmEnum.DATE_OF_PUBLICATION_USED.name());
	    
    return contentsPublicationDate;
  }
  
  public void setContentsPublicationDate( Date contentsPublicationDate )
  {
	  ItemHelperServices.setItemParmDate(getItem(), ItemParmEnum.DATE_OF_PUBLICATION_USED.name(), contentsPublicationDate);

	  if (contentsPublicationDate != null) {
  		  GregorianCalendar calendar = new GregorianCalendar();
  		  calendar.setTime(contentsPublicationDate);
  		  this.setPublicationYear(Integer.valueOf(calendar.get(Calendar.YEAR)).toString());
  	  }
  
  }  
  
  public Date getDateOfUse()
  {
    Date dateOfUse = ItemHelperServices.getItemParmDate(getItem(), ItemParmEnum.DATE_OF_USE.name());
	    
    return dateOfUse;
  }
  
  public void setDateOfUse( Date dateOfUse)
  {
	  ItemHelperServices.setItemParmDate(getItem(), ItemParmEnum.DATE_OF_USE.name(), dateOfUse);
  }  

  public long getNumberOfRecipients()
  {
    BigDecimal numberOfRecipients = ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.NUM_RECIPIENTS.name());
    
    long numRecipients = 0;
    
    if (numberOfRecipients != null)
    {	    
    	numRecipients = numberOfRecipients.longValue();
    }
    
    return numRecipients;

  }

  public void setNumberOfRecipients(long numberOfRecipients)
  {
	  ItemHelperServices.setItemParmNumber(getItem(), ItemParmEnum.NUM_RECIPIENTS.name(), new BigDecimal (numberOfRecipients));
  }
  
  
  
  public int getDuration()
  {
    BigDecimal duration = ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.DURATION.name());
    
    int intDuration = 0;
    
    if (duration != null)
    {    
    	intDuration = duration.intValue();
    }
    
    return intDuration;
  }

  public void setDuration(int duration)
  {
	  LOGGER.debug("DURATION set: " + duration);
	  ItemHelperServices.setItemParmNumber(getItem(), ItemParmEnum.DURATION.name(), new BigDecimal (duration));

	  if (duration == 0) {
		  ItemHelperServices.setItemParmString(getItem(), ItemParmEnum.DURATION_CD.name(), DurationEnum.TO_30_DAYS.getDesc());
	  } else if (duration == 1) {
		  ItemHelperServices.setItemParmString(getItem(), ItemParmEnum.DURATION_CD.name(), DurationEnum.TO_180_DAYS.getDesc());
	  } else if (duration == 2) {
		  ItemHelperServices.setItemParmString(getItem(), ItemParmEnum.DURATION_CD.name(), DurationEnum.TO_365_DAYS.getDesc());
	  }	else if (duration == 3) {
		  ItemHelperServices.setItemParmString(getItem(), ItemParmEnum.DURATION_CD.name(), DurationEnum.UNLIMITED_DAYS.getDesc());
	  }
  }
  public String getDurationString()
  {
	  		
	  if (getDuration() == 0) {
		  return DurationEnum.TO_30_DAYS.getDesc();
	  } else if (getDuration() == 1) {
		  return DurationEnum.TO_180_DAYS.getDesc();
	  } else if (getDuration() == 2) {
		  return DurationEnum.TO_365_DAYS.getDesc();
	  }	else if (getDuration() == 3) {
		  return DurationEnum.UNLIMITED_DAYS.getDesc();
	  }
	  
	  return Constants.EMPTY_STRING;  
         
  }

  public String getWebAddress()
  {
    String webAddress = ItemHelperServices.getItemParmString(getItem(), ItemParmEnum.WEB_ADDRESS.name());
	    
    return webAddress;
  }
  
  public void setWebAddress( String webAddress )
  {
	  ItemHelperServices.setItemParmString(getItem(), ItemParmEnum.WEB_ADDRESS.name(), webAddress);
  }  
  public String getChapterArticle()
  {
    String chapterArticle = ItemHelperServices.getItemParmString(getItem(), ItemParmEnum.CHAPTER_ARTICLE.name());
	    
    return chapterArticle;
  }
  
  public void setChapterArticle( String chapterArticle )
  {
	  ItemHelperServices.setItemParmString(getItem(), ItemParmEnum.CHAPTER_ARTICLE.name(), chapterArticle);
  }  
  public String getCustomAuthor()
  {
    String customAuthor = ItemHelperServices.getItemParmString(getItem(), ItemParmEnum.CUSTOM_AUTHOR.name());
	    
    return customAuthor;
  }
  
  public void setCustomAuthor( String customAuthor )
  {
	  ItemHelperServices.setItemParmString(getItem(), ItemParmEnum.CUSTOM_AUTHOR.name(), customAuthor);
  }  
  
  public String getCustomVolume()
  {
    String customVolume = ItemHelperServices.getItemParmString(getItem(), ItemParmEnum.CUSTOM_VOLUME.name());
	    
    return customVolume;
  }
  
  public void setCustomVolume( String customVolume )
  {
	  ItemHelperServices.setItemParmString(getItem(), ItemParmEnum.CUSTOM_VOLUME.name(), customVolume);
  }  
  
  public String getCustomEdition()
  {
    String customEdition = ItemHelperServices.getItemParmString(getItem(), ItemParmEnum.CUSTOM_EDITION.name());
	    
    return customEdition;
  }
  
  public void setCustomEdition( String customEdition )
  {
	  ItemHelperServices.setItemParmString(getItem(), ItemParmEnum.CUSTOM_EDITION.name(), customEdition);
  }  
  
 public String getPageRange()
  {
    String pageRange = ItemHelperServices.getItemParmString(getItem(), ItemParmEnum.PAGE_RANGE.name());
	    
    return pageRange;
  }
  
  public void setPageRange( String pageRange )
  {
	  ItemHelperServices.setItemParmString(getItem(), ItemParmEnum.PAGE_RANGE.name(), pageRange);
  }  
  
  public String getRepublishingOrganization()
  {
    String republishingOrganization = ItemHelperServices.getItemParmString(getItem(), ItemParmEnum.REPUBLISHING_ORGANIZATION.name());
	    
    return republishingOrganization;
  }
  
  public void setRepublishingOrganization( String republishingOrganization )
  {
	  ItemHelperServices.setItemParmString(getItem(), ItemParmEnum.REPUBLISHING_ORGANIZATION.name(), republishingOrganization);
  }  
  
  public String getNewPublicationTitle()
  {
    String newPublicationTitle = ItemHelperServices.getItemParmString(getItem(), ItemParmEnum.NEW_PUBLICATION_TITLE.name());
	    
    return newPublicationTitle;
  }
  
  public void setNewPublicationTitle( String newPublicationTitle )
  {
	  ItemHelperServices.setItemParmString(getItem(), ItemParmEnum.NEW_PUBLICATION_TITLE.name(), newPublicationTitle);
  }  
  
 public Date getRepublicationDate()
  {
    Date republicationDate = ItemHelperServices.getItemParmDate(getItem(), ItemParmEnum.REPUBLICATION_DATE.name());
	    
    return republicationDate;
  }
  
  public void setRepublicationDate( Date republicationDate )
  {
	  ItemHelperServices.setItemParmDate(getItem(), ItemParmEnum.REPUBLICATION_DATE.name(), republicationDate);
  }  
  
  public String getTranslationLanguage()
  {
    String translationLanguage = ItemHelperServices.getItemParmString(getItem(), ItemParmEnum.TRANSLATION_LANGUAGE.name());
	    
    return translationLanguage;
  }
  
  public void setTranslationLanguage( String translationLanguage )
  {
	  ItemHelperServices.setItemParmString(getItem(), ItemParmEnum.TRANSLATION_LANGUAGE.name(), translationLanguage);
	  if (translationLanguage.equals(RepublicationConstants.NO_TRANSLATION)) {
		  setTranslated(RepublicationConstants.NOT_TRANSLATED);
	  } else {
		  setTranslated(RepublicationConstants.TRANSLATED);
	  }
  }  
  
  public String getTranslated()
  {
	  String isTranslatedYN = ItemHelperServices.getItemParmString(getItem(), ItemParmEnum.IS_TRANSLATED.name());
  
	  return isTranslatedYN;
  }
  
  public void setTranslated ( String translated )
  {  
	  ItemHelperServices.setItemParmString(getItem(), ItemParmEnum.IS_TRANSLATED.name(), translated);
  }  
  
  /**
  * Display the translation language with the first character capitalized and all
  * remaining characters lower case.  Return an empty string if the translation 
  * language indicates there is no translation language.
  * @return
  */
  public String getTranslationLanguageDescription()
  {
	String translationLanguageDescription = Constants.EMPTY_STRING;
	String translationLanguage = ItemHelperServices.getItemParmString(getItem(), ItemParmEnum.TRANSLATION_LANGUAGE.name());

    if ( StringUtils.isNotEmpty(translationLanguage) )
    {
      if (!RepublicationConstants.NO_TRANSLATION.equals(translationLanguage)) {
         translationLanguageDescription = WebUtils.makeDisplayable(translationLanguage);
      }
    }else
      {
        throw new UnsupportedOperationException();
      }

      return translationLanguageDescription;
  }
  
 public String getRepublicationDestination()
  {
    String republicationDestination = ItemHelperServices.getItemParmString(getItem(), ItemParmEnum.REPUBLICATION_DESTINATION.name());
	    
    return republicationDestination;
  }
  
  public void setRepublicationDestination( String republicationDestination )
  {
	  ItemHelperServices.setItemParmString(getItem(), ItemParmEnum.REPUBLICATION_DESTINATION.name(), republicationDestination);
  }  
  
  public Date getPublicationDateOfUse()
  {
    Date dateOfUse = ItemHelperServices.getItemParmDate(getItem(), ItemParmEnum.DATE_OF_PUBLICATION_USED.name());
	    
    return dateOfUse;
  }
  
  
  public void setPublicationDateOfUse( Date dateOfUse)
  {
	  ItemHelperServices.setItemParmDate(getItem(), ItemParmEnum.DATE_OF_PUBLICATION_USED.name(), dateOfUse);
  }  
  
  
  public String getDateOfIssue()
  {
    String dateOfIssue = ItemHelperServices.getItemParmString(getItem(), ItemParmEnum.DATE_OF_ISSUE.name());
	    
    return dateOfIssue;
  }
  
  public void setDateOfIssue( String dateOfIssue)
  {
	  ItemHelperServices.setItemParmString(getItem(), ItemParmEnum.DATE_OF_ISSUE.name(), dateOfIssue);
  }    
 public boolean isRightsLink()
  {
	  boolean isRightsLink = false;
	  
	  if(getProductCd()!=null && (getProductCd().equals(ProductEnum.RL.name()) || getProductCd().equals(ProductEnum.RLR.name())))
	  {
		  isRightsLink = true;
	  }
	  
	  return isRightsLink;
	  
  }
  
  public boolean isReprint()
  {
	  boolean isReprint = false;
	  
	  if(getProductCd()!=null && getProductCd().equals(ProductEnum.RLR.name()))
	  {
		  isReprint = true;
	  }
	  
	  return isReprint;
	  
  }
  
  public boolean isPricedReprint() {

	  boolean isPricedReprint = false;
	  
	  if (getProductCd() != null
			&& getProductCd().equals(ProductEnum.RLR.name()) 
			&& getPriceValueRaw() != ItemConstants.RL_NOT_PRICED) {
		isPricedReprint = true;
	  }
	  
	  return isPricedReprint;
	}

  
  public boolean isAcademic()
  {
    
    boolean isAcademic = isAPS() ||
                         isECCS();
    
    return isAcademic;
  }

  public boolean isPhotocopy()
  {
	  boolean isPhotocopy =false;
	  if(getItem().getProductSourceKey()!=null && getItem().getExternalTouId()!=null){
		  isPhotocopy = getItem().getProductSourceKey().longValue() == ECommerceConstants.TRS_PRODUCT_CODE &&
                          getItem().getExternalTouId().longValue() == ECommerceConstants.PHOTOCOPY_TPU_CODE;
	  }
    
    return isPhotocopy;
  }
  
  public boolean isEmail()
  {
    boolean isEmail =false;
    if(getItem().getProductSourceKey()!=null && getItem().getExternalTouId()!=null){
    	isEmail=getItem().getProductSourceKey().longValue() == ECommerceConstants.DPS_PRODUCT_CODE &&
    getItem().getExternalTouId().longValue() == ECommerceConstants.EMAIL_TPU_CODE;
    }
    
    return isEmail;
  }

  public boolean isRepublication()
  {
    boolean isRepublication =false;
    if(getItem().getProductSourceKey()!=null){
    	isRepublication=getItem().getProductSourceKey().longValue() == ECommerceConstants.RLS_PRODUCT_CODE;
    }
    
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
    boolean isIntranet = false;
    if(getItem().getProductSourceKey()!=null && getItem().getExternalTouId()!=null){
    	isIntranet=getItem().getProductSourceKey().longValue() == ECommerceConstants.DPS_PRODUCT_CODE &&
        getItem().getExternalTouId().longValue() == ECommerceConstants.INTRANET_TPU_CODE;

    }
    	
    return isIntranet;
  }

  public boolean isExtranet()
  {
	  boolean isExranet =false;
	  if(getItem().getProductSourceKey()!=null && getItem().getExternalTouId()!=null){
		  isExranet = getItem().getProductSourceKey().longValue() == ECommerceConstants.DPS_PRODUCT_CODE &&
    					getItem().getExternalTouId().longValue() == ECommerceConstants.EXTRANET_TPU_CODE;
	  }
    
    
    return isExranet;
  }
  

  public boolean isInternet()
  {
	  boolean isInternet =false;
	  if(getItem().getProductSourceKey()!=null && getItem().getExternalTouId()!=null){  
		  isInternet = getItem().getProductSourceKey().longValue() == ECommerceConstants.DPS_PRODUCT_CODE &&
    					 getItem().getExternalTouId().longValue() == ECommerceConstants.INTERNET_TPU_CODE;
	  }
    
    return isInternet;
  }

  public boolean isSpecialOrder()
  { 
		  
	if (getItem().getIsSpecialOrder() != null)
		{
			LOGGER.debug("Checking isSpecialOrder, type: " + getItem().getIsSpecialOrder());
			return getItem().getIsSpecialOrder();
		}
		else
		{
			return false;
		}
  }  

  public boolean isDigital()
  {
    boolean isDigital = isNet() || 
                        isEmail();
    
    return isDigital;
  }

  public boolean isAPS()
  {
    boolean isAPS = false;
    if(getItem().getProductSourceKey()!=null){ 
    	isAPS=getItem().getProductSourceKey().longValue() == ECommerceConstants.APS_PRODUCT_CODE;
    }
    
    return isAPS;
  }

  public boolean isECCS()
  {
	  boolean isECCS=false;
	  if(getItem().getProductSourceKey()!=null){
		  isECCS= getItem().getProductSourceKey().longValue() == ECommerceConstants.ECCS_PRODUCT_CODE;
	  }
     return isECCS;
  }
  
  //    *********************************************
  //    MSJ Just highlighting this because it is used
  //    throughout the code.
  
  @Deprecated
  @Override
  public PermissionRequest getPermissionRequest()
  {
//   return _permissionRequest;
	 return null;
  }

  @Override
  public Item getItem()
  {
    return _item;
  }
  
  @Deprecated
  @Override
  void setPermissionRequest(PermissionRequest permissionRequest)
  {
//    _permissionRequest = permissionRequest;
  }

  public void setItem(Item item)
  {
    _item = item;
  }

  public Long getItemSourceKey()
  {
	  return getItem().getItemSourceKey();  
  }
  
  
  public void setItemSourceKey(Long itemSourceKey)
  {
	  getItem().setItemSourceKey(itemSourceKey);  
  }
  public String getPublisherCd(){
	 return  getItem().getPublisherCd();
  }
  public void setPublisherCd(String publisherCd){
	  getItem().setPublisherCd(publisherCd);  
  }
  public boolean isSpecialOrderFromScratch()
  {
//    boolean isSpecialOrderFromScratch = getPermissionRequest().isManualSpecialOrder();
	  boolean isSpecialOrderFromScratch = false;
	  
	  if (getItem().getSpecialOrderTypeCd()!= null && 
			  (getItem().getSpecialOrderTypeCd().equals(SpecialOrderTypeEnum.RIGHT_NOT_FOUND_SPECIAL_ORDER) || 
			  (getItem().getSpecialOrderTypeCd().equals(SpecialOrderTypeEnum.MANUAL_SPECIAL_ORDER)))) {
			  isSpecialOrderFromScratch = true;
	  }
	  
	  return isSpecialOrderFromScratch;
  }
  
  public boolean isContactRightsholder()
  {
	  boolean isContactRightsholder = false;
	  
	  if (getItem().getSpecialOrderTypeCd()!=null && getItem().getSpecialOrderTypeCd().equals(SpecialOrderTypeEnum.CONTACT_RIGHTSHOLDER_SPECIAL_ORDER)) {
		  isContactRightsholder = true;
	  }
	  
	  return isContactRightsholder;
  }

  public boolean isManualSpecialOrder()
  {
	  boolean isManualSpecialOrder = false;
	  
	
	  if (getItem().getSpecialOrderTypeCd() != null && getItem().getSpecialOrderTypeCd().equals(SpecialOrderTypeEnum.MANUAL_SPECIAL_ORDER)) {
		  isManualSpecialOrder = true;
	  }
	  
	  
	  return isManualSpecialOrder;
  }

  public void setSpecialOrderTypeCd(String specialOrderTypeCd)
  {
	  for (SpecialOrderTypeEnum specialOrderTypeEnum : SpecialOrderTypeEnum.values()) {
		  if (specialOrderTypeEnum.name().equalsIgnoreCase(specialOrderTypeCd)) {
			  getItem().setSpecialOrderTypeCd(specialOrderTypeEnum);
			  if (specialOrderTypeEnum.equals(SpecialOrderTypeEnum.NOT_SPECIAL_ORDER)) {
				  getItem().setIsSpecialOrder(false);
			  } else {
				  getItem().setIsSpecialOrder(true);
			  }
		  }
	  }	
  }

  public String getSpecialOrderTypeCd()
  {
	  if (getItem().getSpecialOrderTypeCd() != null) {
			return getItem().getSpecialOrderTypeCd().name();
	  }
	  return null;
  }

  public void setSpecialOrderTypeCdToManualSpecialOrder()
  {
	  getItem().setSpecialOrderTypeCd(SpecialOrderTypeEnum.MANUAL_SPECIAL_ORDER);
	  getItem().setIsSpecialOrder(true);
  }
  
  public void setSpecialOrderTypeCdToContactRightsholder()
  {
	  getItem().setSpecialOrderTypeCd(SpecialOrderTypeEnum.CONTACT_RIGHTSHOLDER_SPECIAL_ORDER);  
	  getItem().setIsSpecialOrder(true);
  }
  
  public void setSpecialOrderTypeCdToResearchFurtherSpecialOrder()
  {
	  getItem().setSpecialOrderTypeCd(SpecialOrderTypeEnum.RESEARCH_FURTHER_SPECIAL_ORDER);  
	  getItem().setIsSpecialOrder(true);
  }

  @Deprecated
  public void setSpecialOrderLimitsExceeded (boolean limitsExceeded) {
	  
	  if (limitsExceeded == true) {
		  getItem().setSpecialOrderTypeCd(SpecialOrderTypeEnum.LIMITS_EXCEEDED_SPECIAL_ORDER);  
		  getItem().setIsSpecialOrder(true);
	  } else {
		  getItem().setSpecialOrderTypeCd(SpecialOrderTypeEnum.NOT_SPECIAL_ORDER);  		  
		  getItem().setIsSpecialOrder(false);
	  }
  }
  
  public void setSpecialOrderTypeCdToLimitsExceeded()
  {
	  getItem().setSpecialOrderTypeCd(SpecialOrderTypeEnum.LIMITS_EXCEEDED_SPECIAL_ORDER);  
	  getItem().setIsSpecialOrder(true);
  }

  public void setSpecialOrderTypeCdToNotSpecialOrder()
  {
	  getItem().setSpecialOrderTypeCd(SpecialOrderTypeEnum.NOT_SPECIAL_ORDER);
	  getItem().setIsSpecialOrder(false);
  }

  public void setSpecialOrderTypeCdToRightNotFound()
  {
	  getItem().setSpecialOrderTypeCd(SpecialOrderTypeEnum.RIGHT_NOT_FOUND_SPECIAL_ORDER);
	  getItem().setIsSpecialOrder(true);
  }
  
  @Deprecated 
  public Date getPublicationStartDate()
  {
	  return getItem().getRightBeginDate();
  }
  
  @Deprecated 
  @Override
  public void setPublicationStartDate( Date publicationStartDate )
  {
      getItem().setRightBeginDate(publicationStartDate);
  }
  
  @Deprecated 
  public Date getPublicationEndDate()
  {
	  return getItem().getRightEndDate();
  }
  
  @Deprecated
  @Override
  public void setPublicationEndDate( Date publicationEndDate )
  {
	  getItem().setRightEndDate(publicationEndDate);
  }  
  
  public Date getRightStartDate()
  {
	  return getItem().getRightBeginDate();
  }
  
  public void setRightStartDate( Date rightStartDate )
  {
      getItem().setRightBeginDate(rightStartDate);
  }
  
  public Date getRightEndDate()
  {
	  return getItem().getRightEndDate();
  }
  public void setRightEndDate( Date publicationEndDate )
  {
	  getItem().setRightEndDate(publicationEndDate);
  }  

  public String getYourReference()
  {
	  return getItem().getLicenseeRefNum();
  }
  
  public void setYourReference( String yourReference)
  {
	  getItem().setLicenseeRefNum(yourReference);
  }    

  public void setHasVolPriceTiers(boolean hasVolPriceTiers) {
	  getItem().setHasVolumePricing(hasVolPriceTiers);
  }

  
  public String getHasVolPriceTiers() {
	  if (getItem().getHasVolumePricing() == null) {
		  return ItemConstants.NO_CD;
	  } else if (getItem().getHasVolumePricing().booleanValue() == true) {
		  return ItemConstants.YES_CD;		  
	  }
	  
	  return ItemConstants.NO_CD;
  }
  
  
  public String getRepublicationTypeOfUse()
  {
    
    boolean operationSupported = isRepublication();
    
    if( operationSupported )
    {
      String republicationTypeOfUse = Constants.EMPTY_STRING;
//      long tpuInst = getPermissionRequest().getUsageData().getTpuInst();

      LOGGER.debug ("Found Republication TOU ID: " + getItem().getExternalTouId());

      long tpuInst = getItem().getExternalTouId().longValue();
      
      
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
      
      LOGGER.debug ("Found Republication Type of Use: " + republicationTypeOfUse);
      return republicationTypeOfUse;
      
    }else
    {
      throw new UnsupportedOperationException();
    }
    
  }

  public String getRepublishInVolEd() 
  {
  		String republishInVolEd = ItemHelperServices.getItemParmString(getItem(), ItemParmEnum.REPUBLISH_IN_VOL_ED.name());

  		return republishInVolEd;
  }

  public void setRepublishInVolEd ( String republishInVolEd )
  {  
	  	ItemHelperServices.setItemParmString(getItem(), ItemParmEnum.REPUBLISH_IN_VOL_ED.name(), republishInVolEd);
  }  
  
  public String getRepublishNonTextPortion() 
  {
      String republishNonTextPortion = "";

      return republishNonTextPortion;
  }

  public String getRepublishSection() 
  {
      String republishSection = "";

      return republishSection;
  }

  public String getRepublishPoNumDtl() 
  {
      String republishPoNumDt = "";

      return republishPoNumDt;
  }
  
  public long getNumberOfCartoons()
  {
    BigDecimal quantity = ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.NUM_CARTOONS.name());
    
    long numCartoons = 0;
    
    if (quantity != null)
    {	    
    	numCartoons = quantity.longValue();
    }
    
    return numCartoons;

  }

  public void setNumberOfCartoons(long quantity)
  {
	  ItemHelperServices.setItemParmNumber(getItem(), ItemParmEnum.NUM_CARTOONS.name(), new BigDecimal (quantity));
  }
 
  public long getNumberOfCharts()
  {
    BigDecimal quantity = ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.NUM_CHARTS.name());
    
    long numCharts = 0;
    
    if (quantity != null)
    {	    
    	numCharts = quantity.longValue();
    }
    
    return numCharts;

  }

  public void setNumberOfCharts(long quantity)
  {
	  ItemHelperServices.setItemParmNumber(getItem(), ItemParmEnum.NUM_CHARTS.name(), new BigDecimal (quantity));
  }
  
  public long getNumberOfExcerpts()
  {
    BigDecimal quantity = ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.NUM_EXCERPTS.name());
    
    long numExcerpts = 0;
    
    if (quantity != null)
    {	    
    	numExcerpts = quantity.longValue();
    }
    
    return numExcerpts;

  }

  public void setNumberOfExcerpts(long quantity)
  {
	  ItemHelperServices.setItemParmNumber(getItem(), ItemParmEnum.NUM_EXCERPTS.name(), new BigDecimal (quantity));
  }  
  
  public long getNumberOfFigures()
  {
    BigDecimal quantity = ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.NUM_FIGURES.name());
    
    long numFigures = 0;
    
    if (quantity != null)
    {	    
    	numFigures = quantity.longValue();
    }
    
    return numFigures;

  }

  public void setNumberOfFigures(long quantity)
  {
	  ItemHelperServices.setItemParmNumber(getItem(), ItemParmEnum.NUM_FIGURES.name(), new BigDecimal (quantity));
  }  
  
  public long getNumberOfGraphs()
  {
    BigDecimal quantity = ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.NUM_GRAPHS.name());
    
    long numGraphs = 0;
    
    if (quantity != null)
    {	    
    	numGraphs = quantity.longValue();
    }
    
    return numGraphs;

  }

  public void setNumberOfGraphs(long quantity)
  {
	  ItemHelperServices.setItemParmNumber(getItem(), ItemParmEnum.NUM_GRAPHS.name(), new BigDecimal (quantity));
  }  

  public long getNumberOfIllustrations()
  {
    BigDecimal quantity = ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.NUM_ILLUSTRATIONS.name());
    
    long numIllustrations = 0;
    
    if (quantity != null)
    {	    
    	numIllustrations = quantity.longValue();
    }
    
    return numIllustrations;

  }

  public void setNumberOfIllustrations(long quantity)
  {
	  ItemHelperServices.setItemParmNumber(getItem(), ItemParmEnum.NUM_ILLUSTRATIONS.name(), new BigDecimal (quantity));
  }  

  public long getNumberOfLogos()
  {
    BigDecimal quantity = ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.NUM_LOGOS.name());
    
    long numLogos = 0;
    
    if (quantity != null)
    {	    
    	numLogos = quantity.longValue();
    }
    
    return numLogos;

  }

  public void setNumberOfLogos(long quantity)
  {
	  ItemHelperServices.setItemParmNumber(getItem(), ItemParmEnum.NUM_LOGOS.name(), new BigDecimal (quantity));
  }  
  
  public long getNumberOfPhotos()
  {
    BigDecimal quantity = ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.NUM_PHOTOS.name());
    
    long numPhotos = 0;
    
    if (quantity != null)
    {	    
    	numPhotos = quantity.longValue();
    }
    
    return numPhotos;

  }

  public void setNumberOfPhotos(long quantity)
  {
	  ItemHelperServices.setItemParmNumber(getItem(), ItemParmEnum.NUM_PHOTOS.name(), new BigDecimal (quantity));
  }  

  public long getNumberOfQuotes()
  {
    BigDecimal quantity = ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.NUM_QUOTES.name());
    
    long numQuotes = 0;
    
    if (quantity != null)
    {	    
    	numQuotes = quantity.longValue();
    }
    
    return numQuotes;

  }

  public void setNumberOfQuotes(long quantity)
  {
	  ItemHelperServices.setItemParmNumber(getItem(), ItemParmEnum.NUM_QUOTES.name(), new BigDecimal (quantity));
  }  

 public void setDistributionPartyNameForRH(Long payeePartyId, String distributionPartyName)
  {
	  boolean payeeFound = false;
	  
	  if (getItem().getAllFees() == null) {
		  Set<ItemFees> allFees = new HashSet<ItemFees>();
		  getItem().setAllFees(allFees);
	  }
	  if (getItem().getAllFees().size() == 0) {
		  ItemFees itemFees = new ItemFees();
		  itemFees.setOrigDistPartyId(payeePartyId);
		  itemFees.setOrigDistPartyName(distributionPartyName);
		  getItem().getAllFees().add(itemFees);
	  } else {
		  for (ItemFees itemFees : getItem().getAllFees()) {
			if (itemFees.getOrigDistPartyId().compareTo(payeePartyId) == 0) {
				payeeFound = true;
				itemFees.setOrigDistPartyName(distributionPartyName);
			}
		  }
		  if (payeeFound == false) {
			  ItemFees itemFees = new ItemFees();
			  itemFees.setOrigDistPartyId(payeePartyId);
			  itemFees.setOrigDistPartyName(distributionPartyName);
			  getItem().getAllFees().add(itemFees);
		  }
	  } 

  }  
  
  public String getDistributionPartyNameForRH(Long payeePartyId)
  {
//    return getPermissionRequest().getLicenseeFee();
	  
	  if (getItem().getAllFees().size() > 0) {
		  for (ItemFees itemFees : getItem().getAllFees()) {
			if (itemFees.getOrigDistPartyId().compareTo(payeePartyId) == 0) {
			  if (itemFees.getOrigDistPartyName() != null) {
				  return itemFees.getOrigDistPartyName();
			  } else {
				// No Party for rightsholder
				  return null;		  
			  }
			}
		  }
		// TODO Throw exception that RH not found
		  return null;		  
	  } else {
		// TODO Throw exception that no fees exist  
		return null;
	  }
  }

  
  public void setPtyInstForRH(Long payeePartyId, Long ptyInst)
  {
	  boolean payeeFound = false;
	  
	  if (getItem().getAllFees() == null) {
		  Set<ItemFees> allFees = new HashSet<ItemFees>();
		  getItem().setAllFees(allFees);
	  }
	  if (getItem().getAllFees().size() == 0) {
		  ItemFees itemFees = new ItemFees();
		  itemFees.setOrigDistPartyId(payeePartyId);
		  itemFees.setOrigDistPtyInst(ptyInst);
		  getItem().getAllFees().add(itemFees);
	  } else {
		  for (ItemFees itemFees : getItem().getAllFees()) {
			if (itemFees.getOrigDistPartyId().compareTo(payeePartyId) == 0) {
				payeeFound = true;
				itemFees.setOrigDistPtyInst(ptyInst);
			}
		  }
		  if (payeeFound == false) {
			  ItemFees itemFees = new ItemFees();
			  itemFees.setOrigDistPartyId(payeePartyId);
			  itemFees.setOrigDistPtyInst(ptyInst);
			  getItem().getAllFees().add(itemFees);
		  }
	  } 

  }  
  
  public Long getPtyInstForRH(Long payeePartyId)
  {
//    return getPermissionRequest().getLicenseeFee();
	  
	  if (getItem().getAllFees().size() > 0) {
		  for (ItemFees itemFees : getItem().getAllFees()) {
			if (itemFees.getOrigDistPartyId().compareTo(payeePartyId) == 0) {
			  if (itemFees.getLicenseeFee() != null) {
				  return itemFees.getOrigDistPtyInst();
			  } else {
				// No Party for rightsholder
				  return null;		  
			  }
			}
		  }
		// TODO Throw exception that RH not found
		  return null;		  
	  } else {
		// TODO Throw exception that no fees exist  
		return null;
	  }
  }
  @Deprecated     
  public double getLicenseeFee()
  {
//    return getPermissionRequest().getLicenseeFee();
	  BigDecimal licenseeFee = new BigDecimal (0);
	  
	  if (getItem().getAllFees().size() > 0) {
		  for (ItemFees itemFees : getItem().getAllFees()) {
			  if (itemFees.getLicenseeFee() != null) {
				  licenseeFee = licenseeFee.add(itemFees.getLicenseeFee());				  
			  }
		  }
	  } else {
		// TODO No FEE Exception
		  return 0;		  
	  }
	  
	  return licenseeFee.doubleValue();  
  }

  @Deprecated    
  public void setLicenseeFee(double licenseeFee)
  {
	  if (getItem().getAllFees() == null) {
		  Set<ItemFees> allFees = new HashSet<ItemFees>();
		  getItem().setAllFees(allFees);
	  }
	  if (getItem().getAllFees().size() == 0) {
		  getItem().getAllFees().add(new ItemFees());
	  }
	  if (getItem().getAllFees().size() == 1) {
		  Iterator<ItemFees> allFeesIterator = getItem().getAllFees().iterator();
		  if (allFeesIterator.hasNext()) {
			  ItemFees itemFees = allFeesIterator.next();
			  itemFees.setLicenseeFee(new BigDecimal(licenseeFee));
		  }
	  }
	  
  }

  public void setTotalPrice(BigDecimal totalPrice) {
	  getItem().setTotalPrice(totalPrice);
  }

  public BigDecimal getTotalLicenseeFeeValue()
  {
	  BigDecimal licenseeFee = new BigDecimal (0);
	  
	  if (getItem().getAllFees().size() > 0) {
		  for (ItemFees itemFees : getItem().getAllFees()) {
			  if (itemFees.getLicenseeFee() != null) {
				  licenseeFee = licenseeFee.add(itemFees.getLicenseeFee());				  
			  }
		  }
	  } else {
		// TODO No FEE Exception
		  return new BigDecimal(0);		  
	  }
	  
	    return licenseeFee.setScale(2, BigDecimal.ROUND_HALF_EVEN);	 
  }  


  public BigDecimal getLicenseeFeeForRH(Long payeePartyId)
  {
//    return getPermissionRequest().getLicenseeFee();
	  
	  if (getItem().getAllFees().size() > 0) {
		  for (ItemFees itemFees : getItem().getAllFees()) {
			if (itemFees.getOrigDistPartyId().compareTo(payeePartyId) == 0) {
			  if (itemFees.getLicenseeFee() != null) {
				  return itemFees.getLicenseeFee();
			  } else {
				// No Fee for rightsholder
				  return null;		  
			  }
			}
		  }
		// TODO Throw exception that RH not found
		  return null;		  
	  } else {
		// TODO Throw exception that no fees exist  
		return null;
	  }
  }

  public void setLicenseeFee(BigDecimal licenseeFee)
  {
	  if (getItem().getAllFees() == null) {
		  Set<ItemFees> allFees = new HashSet<ItemFees>();
		  getItem().setAllFees(allFees);
	  }
	  if (getItem().getAllFees().size() == 0) {
		  getItem().getAllFees().add(new ItemFees());
	  }
	  if (getItem().getAllFees().size() == 1) {
		  Iterator<ItemFees> allFeesIterator = getItem().getAllFees().iterator();
		  if (allFeesIterator.hasNext()) {
			  ItemFees itemFees = allFeesIterator.next();
			  itemFees.setLicenseeFee(licenseeFee);
		  }
	  }
  }

  public void setLicenseeFeeForRH(Long payeePartyId, BigDecimal licenseeFee)
  {
	  boolean payeeFound = false;
	  
	  if (getItem().getAllFees() == null) {
		  Set<ItemFees> allFees = new HashSet<ItemFees>();
		  getItem().setAllFees(allFees);
	  }
	  if (getItem().getAllFees().size() == 0) {
		  ItemFees itemFees = new ItemFees();
		  itemFees.setOrigDistPartyId(payeePartyId);
		  itemFees.setLicenseeFee(licenseeFee);
		  getItem().getAllFees().add(itemFees);
	  } else {
		  for (ItemFees itemFees : getItem().getAllFees()) {
			if (itemFees.getOrigDistPartyId().compareTo(payeePartyId) == 0) {
				payeeFound = true;
				itemFees.setLicenseeFee(licenseeFee);
			}
		  }
		  if (payeeFound == false) {
			  ItemFees itemFees = new ItemFees();
			  itemFees.setOrigDistPartyId(payeePartyId);
			  itemFees.setLicenseeFee(licenseeFee);
			  getItem().getAllFees().add(itemFees);
		  }
	  } 
  }

  @Deprecated     
  public double getDiscount()
  {
//    return getPermissionRequest().getLicenseeFee();
	  BigDecimal discount = new BigDecimal (0);
	  
	  if (getItem().getAllFees().size() > 0) {
		  for (ItemFees itemFees : getItem().getAllFees()) {
			  if (itemFees.getDiscount() != null) {
				  discount = discount.add(itemFees.getDiscount());				  
			  }
		  }
	  } else {
		// TODO No FEE Exception
		  return 0;		  
	  }
	  
	  return discount.doubleValue();  
  }

  @Deprecated    
  public void setDiscount(double discount)
  {
	  if (getItem().getAllFees() == null) {
		  Set<ItemFees> allFees = new HashSet<ItemFees>();
		  getItem().setAllFees(allFees);
	  }
	  if (getItem().getAllFees().size() == 0) {
		  getItem().getAllFees().add(new ItemFees());
	  }
	  if (getItem().getAllFees().size() == 1) {
		  Iterator<ItemFees> allFeesesIterator = getItem().getAllFees().iterator();
		  if (allFeesesIterator.hasNext()) {
			  ItemFees itemFees = allFeesesIterator.next();
			  itemFees.setDiscount(new BigDecimal(discount));
		  }
	  }
  }
  
  public BigDecimal getTotalDiscountValue()
  {
	  BigDecimal discount = new BigDecimal (0);
	  
	  if (getItem().getAllFees().size() > 0) {
		  for (ItemFees itemFees : getItem().getAllFees()) {
			  if (itemFees.getDiscount() != null) {
				  discount = discount.add(itemFees.getDiscount());				  
			  }
		  }
	  } else {
		// TODO No Discount Exception
		  return new BigDecimal(0);		  
	  }
	  
    return discount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
  }  


  public BigDecimal getDiscountForRH(Long payeePartyId)
  {
//    return getPermissionRequest().getLicenseeFee();
	  
	  if (getItem().getAllFees().size() > 0) {
		  for (ItemFees itemFees : getItem().getAllFees()) {
			if (itemFees.getOrigDistPartyId().compareTo(payeePartyId) == 0) {
			  if (itemFees.getDiscount() != null) {
				  return itemFees.getDiscount();
			  } else {
				// No Discount for rightsholder
				  return null;		  
			  }
			}
		  }
		// TODO Throw exception that RH not found
		  return null;		  
	  } else {
		// TODO Throw exception that no fees exist  
		return null;
	  }
  }

  public void setDiscount(BigDecimal discount)
  {
	  if (getItem().getAllFees() == null) {
		  Set<ItemFees> allFees = new HashSet<ItemFees>();
		  getItem().setAllFees(allFees);
	  }
	  if (getItem().getAllFees().size() == 0) {
		  getItem().getAllFees().add(new ItemFees());
	  }
	  if (getItem().getAllFees().size() == 1) {
		  Iterator<ItemFees> allFeesIterator = getItem().getAllFees().iterator();
		  if (allFeesIterator.hasNext()) {
			  ItemFees itemFees = allFeesIterator.next();
			  itemFees.setDiscount(discount);
		  }
	  }
  }

  public void setDiscountForRH(Long payeePartyId, BigDecimal discount)
  {
	  boolean payeeFound = false;
	  
	  if (getItem().getAllFees() == null) {
		  Set<ItemFees> allFees = new HashSet<ItemFees>();
		  getItem().setAllFees(allFees);
	  }
	  if (getItem().getAllFees().size() == 0) {
		  ItemFees itemFees = new ItemFees();
		  itemFees.setOrigDistPartyId(payeePartyId);
		  itemFees.setDiscount(discount);
		  getItem().getAllFees().add(itemFees);
	  } else {
		  for (ItemFees itemFees : getItem().getAllFees()) {
			if (itemFees.getOrigDistPartyId().compareTo(payeePartyId) == 0) {
				payeeFound = true;
				itemFees.setDiscount(discount);
			}
		  }
		  if (payeeFound == false) {
			  ItemFees itemFees = new ItemFees();
			  itemFees.setOrigDistPartyId(payeePartyId);
			  itemFees.setDiscount(discount);
			  getItem().getAllFees().add(itemFees);
		  }
	  } 
  }

  
  @Deprecated     
  public double getRoyalty()
  {
//    return getPermissionRequest().getLicenseeFee();
	  BigDecimal royalty = new BigDecimal (0);
	  
	  if (getItem().getAllFees().size() > 0) {
		  for (ItemFees itemFees : getItem().getAllFees()) {
			  if (itemFees.getDistributionPayable() != null) {
				  royalty = royalty.add(itemFees.getDistributionPayable());				  
			  }
		  }
	  } else {
		// TODO No FEE Exception
		  return 0;		  
	  }
	  
	  return royalty.doubleValue();  
  }

  @Deprecated    
  public void setRoyalty(double royalty)
  {
	  if (getItem().getAllFees() == null) {
		  Set<ItemFees> allFees = new HashSet<ItemFees>();
		  getItem().setAllFees(allFees);
	  }
	  if (getItem().getAllFees().size() == 0) {
		  getItem().getAllFees().add(new ItemFees());
	  }
	  if (getItem().getAllFees().size() == 1) {
		  Iterator<ItemFees> allFeesIterator = getItem().getAllFees().iterator();
		  if (allFeesIterator.hasNext()) {
			  ItemFees itemFees = allFeesIterator.next();
			  itemFees.setDistributionPayable(new BigDecimal(royalty));
		  }
	  }
  }
  
  public BigDecimal getTotalDistributionPayableValue()
  {
	  BigDecimal distributionPayable = new BigDecimal (0);
	  
	  if (getItem().getAllFees().size() > 0) {
		  for (ItemFees itemFees : getItem().getAllFees()) {
			  if (itemFees.getDistributionPayable() != null) {
				  distributionPayable = distributionPayable.add(itemFees.getDistributionPayable());				  
			  }
		  }
	  } else {
		// TODO No Discount Exception
		  return new BigDecimal(0);		  
	  }
	  
	    return distributionPayable.setScale(2, BigDecimal.ROUND_HALF_EVEN);
  }  


  public BigDecimal getDistributionPayableForRH(Long payeePartyId)
  {
//    return getPermissionRequest().getLicenseeFee();
	  
	  if (getItem().getAllFees().size() > 0) {
		  for (ItemFees itemFees : getItem().getAllFees()) {
			if (itemFees.getOrigDistPartyId().compareTo(payeePartyId) == 0) {
			  if (itemFees.getDistributionPayable() != null) {
				  return itemFees.getDistributionPayable();
			  } else {
				// No Distribution Payable for rightsholder
				  return null;		  
			  }
			}
		  }
		// TODO Throw exception that RH not found
		  return null;		  
	  } else {
		// TODO Throw exception that no fees exist  
		return null;
	  }
  }

  public void setDistributionPayable(BigDecimal distributionPayable)
  {
	  if (getItem().getAllFees() == null) {
		  Set<ItemFees> allFees = new HashSet<ItemFees>();
		  getItem().setAllFees(allFees);
	  }
	  if (getItem().getAllFees().size() == 0) {
		  getItem().getAllFees().add(new ItemFees());
	  }
	  if (getItem().getAllFees().size() == 1) {
		  Iterator<ItemFees> allFeesIterator = getItem().getAllFees().iterator();
		  if (allFeesIterator.hasNext()) {
			  ItemFees itemFees = allFeesIterator.next();
			  itemFees.setDistributionPayable(distributionPayable);
		  }
	  }
  }

  public void setDistributionPayableForRH(Long payeePartyId, BigDecimal distributionPayable)
  {
	  boolean payeeFound = false;
	  
	  if (getItem().getAllFees() == null) {
		  Set<ItemFees> allFees = new HashSet<ItemFees>();
		  getItem().setAllFees(allFees);
	  }
	  if (getItem().getAllFees().size() == 0) {
		  ItemFees itemFees = new ItemFees();
		  itemFees.setOrigDistPartyId(payeePartyId);
		  itemFees.setDistributionPayable(distributionPayable);
		  getItem().getAllFees().add(itemFees);
	  } else {
		  for (ItemFees itemFees : getItem().getAllFees()) {
			if (itemFees.getOrigDistPartyId().compareTo(payeePartyId) == 0) {
				payeeFound = true;
				itemFees.setDistributionPayable(distributionPayable);
			}
		  }
		  if (payeeFound == false) {
			  ItemFees itemFees = new ItemFees();
			  itemFees.setOrigDistPartyId(payeePartyId);
			  itemFees.setDistributionPayable(distributionPayable);
			  getItem().getAllFees().add(itemFees);
		  }
	  } 
  }  

  @Deprecated     
  public double getRightsholderFee()
  {
//    return getPermissionRequest().getLicenseeFee();
	  BigDecimal rightsholderFee = new BigDecimal (0);
	  
	  if (getItem().getAllFees().size() > 0) {
		  for (ItemFees itemFees : getItem().getAllFees()) {
			  if (itemFees.getRightsholderFee() != null) {
				  rightsholderFee = rightsholderFee.add(itemFees.getRightsholderFee());				  
			  }
		  }
	  } else {
		// TODO No FEE Exception
		  return 0;		  
	  }
	  
	  return rightsholderFee.doubleValue();  
  }

  @Deprecated    
  @Override
  public void setRightholderFee(double rightsholderFee)
  {
	  if (getItem().getAllFees() == null) {
		  Set<ItemFees> allFees = new HashSet<ItemFees>();
		  getItem().setAllFees(allFees);
	  }
	  if (getItem().getAllFees().size() == 0) {
		  getItem().getAllFees().add(new ItemFees());
	  }
	  if (getItem().getAllFees().size() == 1) {
		  Iterator<ItemFees> allFeesIterator = getItem().getAllFees().iterator();
		  if (allFeesIterator.hasNext()) {
			  ItemFees itemFees = allFeesIterator.next();
			  itemFees.setRightsholderFee(new BigDecimal(rightsholderFee));
		  }
	  }
  }
  
  public BigDecimal getTotalRightsholderFeeValue()
  {
	  BigDecimal rightsholderFee = new BigDecimal (0);
	  
	  if (getItem().getAllFees().size() > 0) {
		  for (ItemFees itemFees : getItem().getAllFees()) {
			  if (itemFees.getRightsholderFee() != null) {
				  rightsholderFee = rightsholderFee.add(itemFees.getRightsholderFee());				  
			  }
		  }
	  } else {
		// TODO No Discount Exception
		  return new BigDecimal(0);		  
	  }
	  
	  return rightsholderFee.setScale(2, BigDecimal.ROUND_HALF_EVEN);
  }  


  public BigDecimal getRightsholderFeeForRH(Long payeePartyId)
  {
//    return getPermissionRequest().getLicenseeFee();
	  
	  if (getItem().getAllFees().size() > 0) {
		  for (ItemFees itemFees : getItem().getAllFees()) {
			if (itemFees.getOrigDistPartyId().compareTo(payeePartyId) == 0) {
			  if (itemFees.getRightsholderFee() != null) {
				  return itemFees.getRightsholderFee();
			  } else {
				// No Discount for rightsholder
				  return null;		  
			  }
			}
		  }
		// TODO Throw exception that RH not found
		  return null;		  
	  } else {
		// TODO Throw exception that no fees exist  
		return null;
	  }
  }

  public void setRightsholderFee(BigDecimal rightsholderFee)
  {
	  if (getItem().getAllFees() == null) {
		  Set<ItemFees> allFees = new HashSet<ItemFees>();
		  getItem().setAllFees(allFees);
	  }
	  if (getItem().getAllFees().size() == 0) {
		  getItem().getAllFees().add(new ItemFees());
	  }
	  if (getItem().getAllFees().size() == 1) {
		  Iterator<ItemFees> allFeesIterator = getItem().getAllFees().iterator();
		  if (allFeesIterator.hasNext()) {
			  ItemFees itemFees = allFeesIterator.next();
			  LOGGER.debug("Setting RH Fee for Item: " + rightsholderFee);
			  itemFees.setRightsholderFee(rightsholderFee);
		  }
	  }
  }

  
  
  public void setRightsholderFeeForRH(Long payeePartyId, BigDecimal rightsholderFee)
  {
	  boolean payeeFound = false;
	  
	  if (getItem().getAllFees() == null) {
		  Set<ItemFees> allFees = new HashSet<ItemFees>();
		  getItem().setAllFees(allFees);
	  }
	  if (getItem().getAllFees().size() == 0) {
		  ItemFees itemFees = new ItemFees();
		  itemFees.setOrigDistPartyId(payeePartyId);
		  itemFees.setRightsholderFee(rightsholderFee);
		  getItem().getAllFees().add(itemFees);
	  } else {
		  for (ItemFees itemFees : getItem().getAllFees()) {
			if (itemFees.getOrigDistPartyId().compareTo(payeePartyId) == 0) {
				payeeFound = true;
				itemFees.setRightsholderFee(rightsholderFee);
			}
		  }
		  if (payeeFound == false) {
			  ItemFees itemFees = new ItemFees();
			  itemFees.setOrigDistPartyId(payeePartyId);
			  itemFees.setRightsholderFee(rightsholderFee);
			  getItem().getAllFees().add(itemFees);
		  }
	  } 
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
//    return getPermissionRequest().getReasonCd();
// TODO Don't remember what this is
	  return 0;
  }

  public void setReasonCd(long reasonCd)
  {
// TODO Don't remember what this is
//    getPermissionRequest().setReasonCd(reasonCd);
  }

  public String getReasonDesc()
  {
	// TODO Don't remember what this is
//    return getPermissionRequest().getReasonDesc();
	  return null;
  }

  public void setReasonDesc(String reasonDesc)
  {
// TODO Don't remember what this is
    getPermissionRequest().setReasonDesc(reasonDesc);
  }


  public boolean isSpecialOrderLimitsExceeded(  )
  {
	  if (getItem().getSpecialOrderTypeCd() != null)
	  {
		  if (getItem().getSpecialOrderTypeCd().equals(SpecialOrderTypeEnum.LIMITS_EXCEEDED_SPECIAL_ORDER)) {
			  return true;
		  } else {
			  return false;
		  }
	  }
	  else
	  {
		  return false;
	  }
  }
  
  @Deprecated    
  private void setFees(RightFees fees)
  {
//    this._fees = fees;
  }

  @Deprecated    
  private RightFees getFees()
  {
//    return _fees;
	  return null;
  }

  public String getBaseFee()
  {
	String flatFee = ItemHelperServices.getItemParmString(getItem(), ItemParmEnum.BASE_FEE.name());

	return flatFee;  
  }
  
  public void setBaseFeeValue(BigDecimal baseFee) {
	  ItemHelperServices.setItemParmNumber(getItem(), ItemParmEnum.BASE_FEE.name(), baseFee);
  }

  public String getFlatFee()
  {
	String flatFee = ItemHelperServices.getItemParmString(getItem(), ItemParmEnum.FLAT_FEE.name());

	return flatFee;  
  }
  
  public void setFlatFeeValue(BigDecimal flatFee) {
	  ItemHelperServices.setItemParmNumber(getItem(), ItemParmEnum.FLAT_FEE.name(), flatFee);
  }

  public void setPerPageFeeValue(BigDecimal perPageFee) {
	  ItemHelperServices.setItemParmNumber(getItem(), ItemParmEnum.PER_PAGE_FEE.name(), perPageFee);
  }
  
  public void setEntireBookFeeValue(BigDecimal perPageFee) {
	  ItemHelperServices.setItemParmNumber(getItem(), ItemParmEnum.ENTIRE_BOOK_FEE.name(), perPageFee);
  } 
  
  public String getPerPageFee()
  {
	String perPageFee = ItemHelperServices.getItemParmString(getItem(), ItemParmEnum.PER_PAGE_FEE.name());
	
	return perPageFee;
  }
  
  public String getEntireBookFee()
  {
	String perBookFee = ItemHelperServices.getItemParmString(getItem(), ItemParmEnum.ENTIRE_BOOK_FEE.name());
	
	return perBookFee;
  }
  
  public String getEntireBookFeeMoneyFormat()
  {
	BigDecimal entireBookFee = ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.ENTIRE_BOOK_FEE.name());
	Double entireBookFeeDouble;
	
	
	if (entireBookFee != null) {
		entireBookFeeDouble = entireBookFee.doubleValue();
		return WebUtils.formatMoney( new Money(entireBookFeeDouble), false );
	}

	return null;
	
  }
  
  public BigDecimal getEntireBookFeeValue()
  {
	BigDecimal entireBookFee = ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.ENTIRE_BOOK_FEE.name());
	
	return entireBookFee;
  }

  public String getPerPageFeeMoneyFormat()
  {
	BigDecimal perPageFee = ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.PER_PAGE_FEE.name());
	Double perPageFeeDouble;
	
	
	if (perPageFee != null) {
		perPageFeeDouble = perPageFee.doubleValue();
		return WebUtils.formatMoney( new Money(perPageFeeDouble), false );
	}

	return null;
	
  }
  
  public BigDecimal getPerPageFeeValue()
  {
	BigDecimal perPageFee = ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.PER_PAGE_FEE.name());
	
	return perPageFee;
  }
  
  public String getBaseFeeMoneyFormat()
  {
	BigDecimal baseFee = ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.BASE_FEE.name());
	Double baseFeeDouble;
	
	
	if (baseFee != null) {
		baseFeeDouble = baseFee.doubleValue();
		return WebUtils.formatMoney( new Money(baseFeeDouble), false );
	}

	return null;
	
  }
  
  public BigDecimal getBaseFeeValue()
  {
	BigDecimal baseFee = ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.BASE_FEE.name());
	
	return baseFee;
  }
  
  public String getFlatFeeMoneyFormat()
  {
	BigDecimal flatFee = ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.FLAT_FEE.name());
	Double flatFeeDouble;
	
	
	if (flatFee != null) {
		flatFeeDouble = flatFee.doubleValue();
		return WebUtils.formatMoney( new Money(flatFeeDouble), false );
	}

	return null;
	
  }
  
  public BigDecimal getFlatFeeValue()
  {
	BigDecimal flatFee = ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.FLAT_FEE.name());
	
	return flatFee;
  }
  
  private void calculateRightFees()
  {
// TODO New way of setting fees
	  
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
    	  LOGGER.error(LogUtil.getStack(e));    	  
        setFees( null );
      }

    }
  }


  public String getExternalCommentTerm() {
  
    	return getItem().getRightQualifierTerms();
  }
  
  @Override
  void setExternalCommentTerm( String term )
  {
    getItem().setRightQualifierTerms(term);
  }

  @Deprecated
  public long getRightsholderInst()
  {
	  if (getItem().getAllFees().size() == 1) {
		  Iterator<ItemFees> itemFeesesIterator = getItem().getAllFees().iterator();
		  if (itemFeesesIterator.hasNext()) {
			  ItemFees itemFees = itemFeesesIterator.next();
//TODO Keith put this back in
//			  return itemFees.getPayeePtyInst().longValue();
		  }
	  } else {
			  // multiple rightsholders exception
	  }

	  return 0;

  }
  
  public long getPayeePtyInst()
  {
	  if (getItem().getAllFees().size() == 1) {
		  Iterator<ItemFees> allFeesIterator = getItem().getAllFees().iterator();
		  if (allFeesIterator.hasNext()) {
			  ItemFees itemFees = allFeesIterator.next();
//TODO Keith put this back in
			  //			  return itemFees.getPayeePtyInst().longValue();
		  }
	  } else {
			  // multiple rightsholders exception
	  }

	  return 0;

  }

  void setPayeePtyInst(long payeePtyInst)
  {
// TODO I think we need a new field for Rightsholder INST and a new method
	  // for payee party id
	  
	  if (getItem().getAllFees() == null) {
		  Set<ItemFees> allFees = new HashSet<ItemFees>();
		  getItem().setAllFees(allFees);
	  }
	  if (getItem().getAllFees().size() == 0) {
		  getItem().getAllFees().add(new ItemFees());
	  }
	  if (getItem().getAllFees().size() == 1) {
		  Iterator<ItemFees> itemFeesesIterator = getItem().getAllFees().iterator();
		  if (itemFeesesIterator.hasNext()) {
			  ItemFees itemFees = itemFeesesIterator.next();
			  itemFees.setOrigDistPtyInst(payeePtyInst);
			  itemFees.setActualDistPtyInst(payeePtyInst);
		  }
	  }

// getRightFromWeb().setRightsholderId(rightsholderInst);
  }

  public long getPayeePartyId()
  {
	  if (getItem().getAllFees().size() == 1) {
		  Iterator<ItemFees> itemFeesesIterator = getItem().getAllFees().iterator();
		  if (itemFeesesIterator.hasNext()) {
			  ItemFees itemFees = itemFeesesIterator.next();
			  return itemFees.getOrigDistPartyId().longValue();
		  }
	  } else {
			  // multiple rightsholders exception
	  }

	  return 0;

  }

  void setPayeePartyId(long payeePartyId)
  {
	  
	  if (getItem().getAllFees() == null) {
		  Set<ItemFees> allFees = new HashSet<ItemFees>();
		  getItem().setAllFees(allFees);
	  }
	  if (getItem().getAllFees().size() == 0) {
		  getItem().getAllFees().add(new ItemFees());
	  }
	  if (getItem().getAllFees().size() == 1) {
		  Iterator<ItemFees> itemFeesesIterator = getItem().getAllFees().iterator();
		  if (itemFeesesIterator.hasNext()) {
			  ItemFees itemFees = itemFeesesIterator.next();
			  itemFees.setOrigDistPartyId(payeePartyId);
			  itemFees.setActualDistPartyId(payeePartyId);
		  }
	  }

//	  getRightFromWeb().setRightsholderId(rightsholderInst);
  }

  void setPayeeAccount(String account)
  {
	  
	  if (getItem().getAllFees() == null) {
		  Set<ItemFees> allFees = new HashSet<ItemFees>();
		  getItem().setAllFees(allFees);
	  }
	  if (getItem().getAllFees().size() == 0) {
		  getItem().getAllFees().add(new ItemFees());
	  }
	  if (getItem().getAllFees().size() == 1) {
		  Iterator<ItemFees> itemFeesesIterator = getItem().getAllFees().iterator();
		  if (itemFeesesIterator.hasNext()) {
			  ItemFees itemFees = itemFeesesIterator.next();
			  itemFees.setOrigDistAccount(account);
			  itemFees.setActualDistAccount(account);
		  }
	  }

//	  getRightFromWeb().setRightsholderId(rightsholderInst);
  }
  
  public String getPayeePartyName()
  {
	  if (getItem().getAllFees().size() == 1) {
		  Iterator<ItemFees> itemFeesesIterator = getItem().getAllFees().iterator();
		  if (itemFeesesIterator.hasNext()) {
			  ItemFees itemFees = itemFeesesIterator.next();
			  return itemFees.getOrigDistPartyName();
		  }
	  } else {
			  // multiple rightsholders exception
	  }

	  return null;

  }

  void setPayeePartyName(String payeePartyName)
  {
	  
	  if (getItem().getAllFees() == null) {
		  Set<ItemFees> allFees = new HashSet<ItemFees>();
		  getItem().setAllFees(allFees);
	  }
	  if (getItem().getAllFees().size() == 0) {
		  getItem().getAllFees().add(new ItemFees());
	  }
	  if (getItem().getAllFees().size() == 1) {
		  Iterator<ItemFees> itemFeesesIterator = getItem().getAllFees().iterator();
		  if (itemFeesesIterator.hasNext()) {
			  ItemFees itemFees = itemFeesesIterator.next();
			  itemFees.setOrigDistPartyName(payeePartyName);
			  itemFees.setActualDistPartyName(payeePartyName);
		  }
	  }

// getRightFromWeb().setRightsholderId(rightsholderInst);
  }

  
  public String getRightsholder() {
	  return getPayeePartyName();
  }

  public void setRightsholder(String rightsholder) {
	  setPayeePartyName(rightsholder);
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
        getItem().setIsSpecialOrder(isSpecialOrder);
    }

    @Override
    public void setPushToTFFailed(boolean pushToTFFailed) {
        this.pushToTFFailed = pushToTFFailed;
    }

    @Override
    public boolean isPushToTFFailed() {
        return pushToTFFailed;
    }

    @Deprecated
    @Override
    public void setRightFromWeb(RightAdapter rightAdapter) {
        this.rightAdapter = rightAdapter;
    }
    
    @Deprecated
    public com.copyright.data.inventory.Right getRight() {
        return null;
    }
    
    public BigDecimal getHardCopyCost() {
    	  BigDecimal hardCopyCost = new BigDecimal (0);
    	  
    	  if (getItem().getAllFees().size() > 0) {
    		  for (ItemFees itemFees : getItem().getAllFees()) {
    			  if (itemFees.getHardCopyCost() != null) {
    				  hardCopyCost = hardCopyCost.add(itemFees.getHardCopyCost());				  
    			  }
    		  }
    	  } else {
    		  return hardCopyCost;		  
    	  }
    	  
    	  return hardCopyCost;  
    	}

      public void setHardCopyCost(BigDecimal hardCopyCost)
      {
    	  if (getItem().getAllFees() == null) {
    		  Set<ItemFees> allFees = new HashSet<ItemFees>();
    		  getItem().setAllFees(allFees);
    	  }
    	  if (getItem().getAllFees().size() == 0) {
    		  getItem().getAllFees().add(new ItemFees());
    	  }
    	  if (getItem().getAllFees().size() == 1) {
    		  Iterator<ItemFees> allFeesIterator = getItem().getAllFees().iterator();
    		  if (allFeesIterator.hasNext()) {
    			  ItemFees itemFees = allFeesIterator.next();
    			  itemFees.setHardCopyCost(hardCopyCost);
    		  }
    	  }

     }
      
      public BigDecimal getPriceAdjustment() {
      	  BigDecimal priceAdjustment = new BigDecimal (0);
      	  
      	  if (getItem().getAllFees().size() > 0) {
      		  for (ItemFees itemFees : getItem().getAllFees()) {
      			  if (itemFees.getPriceAdjustment() != null) {
      				  priceAdjustment = priceAdjustment.add(itemFees.getPriceAdjustment());				  
      			  }
      		  }
      	  } else {
      		  return priceAdjustment;		  
      	  }
      	  
      	  return priceAdjustment;  
      	}

      public void setPriceAdjustment(BigDecimal priceAdjustment)
      {
    	  if (getItem().getAllFees() == null) {
    		  Set<ItemFees> allFees = new HashSet<ItemFees>();
    		  getItem().setAllFees(allFees);
    	  }
    	  if (getItem().getAllFees().size() == 0) {
    		  getItem().getAllFees().add(new ItemFees());
    	  }
    	  if (getItem().getAllFees().size() == 1) {
    		  Iterator<ItemFees> allFeesIterator = getItem().getAllFees().iterator();
    		  if (allFeesIterator.hasNext()) {
    			  ItemFees itemFees = allFeesIterator.next();
    			  itemFees.setPriceAdjustment(priceAdjustment);
    		  }
    	  }
    
     }
      
      public BigDecimal getShippingFee1() {
    	  BigDecimal shippingFee1 = new BigDecimal (0);
    	  
    	  if (getItem().getAllFees().size() > 0) {
    		  for (ItemFees itemFees : getItem().getAllFees()) {
    			  if (itemFees.getPriceAdjustment() != null) {
    				  shippingFee1 = shippingFee1.add(itemFees.getShippingFee1());				  
    			  }
    		  }
    	  } else {
    		  return shippingFee1;		  
    	  }
    	  
    	  return shippingFee1;  
    	}

      public void setShippingFee1(BigDecimal shippingFee1)
      {
    	  if (getItem().getAllFees() == null) {
    		  Set<ItemFees> allFees = new HashSet<ItemFees>();
    		  getItem().setAllFees(allFees);
    	  }
    	  if (getItem().getAllFees().size() == 0) {
    		  getItem().getAllFees().add(new ItemFees());
    	  }
    	  if (getItem().getAllFees().size() == 1) {
    		  Iterator<ItemFees> allFeesIterator = getItem().getAllFees().iterator();
    		  if (allFeesIterator.hasNext()) {
    			  ItemFees itemFees = allFeesIterator.next();
    			  itemFees.setShippingFee1(shippingFee1);
    		  }
    	  }

      }
      
      public BigDecimal getShippingFee2() {
      	  BigDecimal shippingFee2 = new BigDecimal (0);
      	  
      	  if (getItem().getAllFees().size() > 0) {
      		  for (ItemFees itemFees : getItem().getAllFees()) {
      			  if (itemFees.getPriceAdjustment() != null) {
      				  shippingFee2 = shippingFee2.add(itemFees.getShippingFee2());				  
      			  }
      		  }
      	  } else {
      		  return shippingFee2;		  
      	  }
      	  
      	  return shippingFee2;  
      	}

      public void setShippingFee2(BigDecimal shippingFee2)
      {
    	  if (getItem().getAllFees() == null) {
    		  Set<ItemFees> allFees = new HashSet<ItemFees>();
    		  getItem().setAllFees(allFees);
    	  }
    	  if (getItem().getAllFees().size() == 0) {
    		  getItem().getAllFees().add(new ItemFees());
    	  }
    	  if (getItem().getAllFees().size() == 1) {
    		  Iterator<ItemFees> allFeesIterator = getItem().getAllFees().iterator();
    		  if (allFeesIterator.hasNext()) {
    			  ItemFees itemFees = allFeesIterator.next();
    			  itemFees.setShippingFee2(shippingFee2);
    		  }
    	  }

      }
      
      public BigDecimal getShippingFee3() {
      	  BigDecimal shippingFee3 = new BigDecimal (0);
      	  
      	  if (getItem().getAllFees().size() > 0) {
      		  for (ItemFees itemFees : getItem().getAllFees()) {
      			  if (itemFees.getPriceAdjustment() != null) {
      				  shippingFee3 = shippingFee3.add(itemFees.getShippingFee3());				  
      			  }
      		  }
      	  } else {
      		  return shippingFee3;		  
      	  }
      	  
      	  return shippingFee3;  
      	}

      public void setShippingFee3(BigDecimal shippingFee3)
      {
    	  if (getItem().getAllFees() == null) {
    		  Set<ItemFees> allFees = new HashSet<ItemFees>();
    		  getItem().setAllFees(allFees);
    	  }
    	  if (getItem().getAllFees().size() == 0) {
    		  getItem().getAllFees().add(new ItemFees());
    	  }
    	  if (getItem().getAllFees().size() == 1) {
    		  Iterator<ItemFees> allFeesIterator = getItem().getAllFees().iterator();
    		  if (allFeesIterator.hasNext()) {
    			  ItemFees itemFees = allFeesIterator.next();
    			  itemFees.setShippingFee3(shippingFee3);
    		  }
    	  }

      }
      
      public BigDecimal getShippingFee4() {
      	  BigDecimal shippingFee4 = new BigDecimal (0);
      	  
      	  if (getItem().getAllFees().size() > 0) {
      		  for (ItemFees itemFees : getItem().getAllFees()) {
      			  if (itemFees.getPriceAdjustment() != null) {
      				  shippingFee4 = shippingFee4.add(itemFees.getShippingFee4());				  
      			  }
      		  }
      	  } else {
      		  return shippingFee4;		  
      	  }
      	  
      	  return shippingFee4;  
      	}
      
      public void setShippingFee4(BigDecimal shippingFee4)
      {
    	  if (getItem().getAllFees() == null) {
    		  Set<ItemFees> allFees = new HashSet<ItemFees>();
    		  getItem().setAllFees(allFees);
    	  }
    	  if (getItem().getAllFees().size() == 0) {
    		  getItem().getAllFees().add(new ItemFees());
    	  }
    	  if (getItem().getAllFees().size() == 1) {
    		  Iterator<ItemFees> allFeesIterator = getItem().getAllFees().iterator();
    		  if (allFeesIterator.hasNext()) {
    			  ItemFees itemFees = allFeesIterator.next();
    			  itemFees.setShippingFee4(shippingFee4);
    		  }
    	  }

      }
      
      public BigDecimal getShippingFee5() {
      	  BigDecimal shippingFee5 = new BigDecimal (0);
      	  
      	  if (getItem().getAllFees().size() > 0) {
      		  for (ItemFees itemFees : getItem().getAllFees()) {
      			  if (itemFees.getPriceAdjustment() != null) {
      				  shippingFee5 = shippingFee5.add(itemFees.getShippingFee5());				  
      			  }
      		  }
      	  } else {
      		  return shippingFee5;		  
      	  }
      	  
      	  return shippingFee5;  
      	}

      public void setShippingFee5(BigDecimal shippingFee5)
      {
    	  if (getItem().getAllFees() == null) {
    		  Set<ItemFees> allFees = new HashSet<ItemFees>();
    		  getItem().setAllFees(allFees);
    	  }
    	  if (getItem().getAllFees().size() == 0) {
    		  getItem().getAllFees().add(new ItemFees());
    	  }
    	  if (getItem().getAllFees().size() == 1) {
    		  Iterator<ItemFees> allFeesIterator = getItem().getAllFees().iterator();
    		  if (allFeesIterator.hasNext()) {
    			  ItemFees itemFees = allFeesIterator.next();
    			  itemFees.setShippingFee5(shippingFee5);
    		  }
    	  }

      }
      
      public BigDecimal getShippingFee6() {
      	  BigDecimal shippingFee6 = new BigDecimal (0);
      	  
      	  if (getItem().getAllFees().size() > 0) {
      		  for (ItemFees itemFees : getItem().getAllFees()) {
      			  if (itemFees.getPriceAdjustment() != null) {
      				  shippingFee6 = shippingFee6.add(itemFees.getShippingFee6());				  
      			  }
      		  }
      	  } else {
      		  return shippingFee6;		  
      	  }
      	  
      	  return shippingFee6;  
      	}

      public void setShippingFee6(BigDecimal shippingFee6)
      {
    	  if (getItem().getAllFees() == null) {
    		  Set<ItemFees> allFees = new HashSet<ItemFees>();
    		  getItem().setAllFees(allFees);
    	  }
    	  if (getItem().getAllFees().size() == 0) {
    		  getItem().getAllFees().add(new ItemFees());
    	  }
    	  if (getItem().getAllFees().size() == 1) {
    		  Iterator<ItemFees> allFeesIterator = getItem().getAllFees().iterator();
    		  if (allFeesIterator.hasNext()) {
    			  ItemFees itemFees = allFeesIterator.next();
    			  itemFees.setShippingFee6(shippingFee6);
    		  }
    	  }

      }

      public Long getRlCustomerId() {
      	return getItem().getRlCustomerId();
      }
      
      public void setRlCustomerId(Long rlCustomerId) {
      	getItem().setRlCustomerId(rlCustomerId);
      }
      
      public String getRlDetailHtml() {
    	  return getItem().getRlDetailHtml();
      }
      
      public void setRlDetailHtml(String rlDetailHtml) {
    	  getItem().setRlDetailHtml(rlDetailHtml); 
      }
      
      public String getManagedRedirectLink() {
    	  return getItem().getManagedRedirectLink();
      }
      
      public void setManagedRedirectLink(String managedRedirectLink) {
    	  getItem().setManagedRedirectLink(managedRedirectLink);
      }
      
      public List<Address> getShippingAddress()
      {
    	  
    	  Item item = getItem();
    	  
    	  String line1 = "";
    	  String line2 = "";
    	  String line3 = "";
    	  String city1 = "";
    	  String state1 = "";
    	  String zip1 = "";
    	  String country1 = "";
    	  String numLoc = "";
    	  String line1_parm = "ORDERLINE1SHIPPING";
    	  String line2_parm = "ORDERLINE2SHIPPING";
    	  String line3_parm = "ORDERLINE3SHIPPING";
    	  String city_parm = "ORDERCITYSHIPPING";
    	  String state_parm = "ORDERSTATESHIPPING";
    	  String zip_parm = "ORDERZIPSHIPPING";
    	  String country_parm = "ORDERCOUNTRYSHIPPING";
    	  
    	  String line1_parm_name = "";
    	  String line2_parm_name = "";
    	  String line3_parm_name = "";
    	  String city_parm_name = "";
    	  String state_parm_name = "";
    	  String zip_parm_name = "";
    	  String country_parm_name = "";
    	  
    	  List<Address> addresses = new ArrayList<Address>(0);
    	  
    	      	      	  
    	  //Get # of Shipping addresses
    	  for(ItemParm itemParm2:item.getItemParms().values()){
    		  if (itemParm2.getParmName().equalsIgnoreCase("NUMSHIPLOCATIONS"))
    		  {
    			  numLoc = itemParm2.getParmValue(); 
    		  }
    		  
    	  }
    	  
    	  if (numLoc == "")
    	  {
    		  return null;
    	  }
    	      		  
    	      	  
    	  for ( int i = 0; i < Integer.valueOf(numLoc); i++)
    	  {
    		  
    		   line1_parm_name = line1_parm + (i + 1);
        	   line2_parm_name = line2_parm + (i + 1) ;
        	   line3_parm_name = line3_parm + (i + 1) ;
        	   city_parm_name =  city_parm + (i + 1);
        	   state_parm_name = state_parm + (i + 1) ;
        	   zip_parm_name = zip_parm + (i + 1);
        	   country_parm_name = country_parm + (i + 1);
        	   
        	   for(ItemParm itemParm:item.getItemParms().values()){
         		  if (itemParm.getParmName().equalsIgnoreCase(line1_parm_name))
         		  {
         		 // getItemRequestParmFromItemParm(itemRequestParm, itemParm);
         		      line1 = itemParm.getParmValue();
         		  }
         		  else if (itemParm.getParmName().equalsIgnoreCase(line2_parm_name))
         		  {
         			  line2 = itemParm.getParmValue(); 
         		  }
         		  else if (itemParm.getParmName().equalsIgnoreCase(line3_parm_name))
         		  {
         			  line3 = itemParm.getParmValue(); 
         		  }
         		  else if (itemParm.getParmName().equalsIgnoreCase(city_parm_name))
         		  {
         			  city1 = itemParm.getParmValue(); 
         		  }
         		  else if (itemParm.getParmName().equalsIgnoreCase(state_parm_name))
         		  {
         			  state1 = itemParm.getParmValue(); 
         		  }
         		  else if (itemParm.getParmName().equalsIgnoreCase(country_parm_name))
         		  {
         			  country1 = itemParm.getParmValue();   
         		  }
         		  else if (itemParm.getParmName().equalsIgnoreCase(zip_parm_name))
         		  {
         			  zip1 = itemParm.getParmValue(); 
         		  }
         		     		  
         		  
         	  }
        	   
        	   Country cntry = new Country();
        	   cntry.setName(country1);
        	   
        	  Address address = new Address(line1, line2, line3, "", city1, state1, zip1, cntry);
        	  
        	  addresses.add(address);
        	  
        	      		  
    	  }
    	  
    	  
    	  
    	  return addresses;
    	  
    	 
      }
     

      public BigDecimal getPerLogoFee() {
    	    return ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.PER_LOGO.name());
      }
      
      public BigDecimal getPerGraphFee() {
    	    return ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.PER_GRAPH.name());
      }
      		
      public BigDecimal getPerCartoonFee() {
    	    return ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.PER_CARTOON.name());
      }
      	
      public BigDecimal getPerExcerptFee() {
  	    return ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.PER_EXCERPT.name());
      }
      
      public BigDecimal getPerQuoteFee() {
  	    return ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.PER_QUOTE.name());
      }	
      
      public BigDecimal getPerChartFee() {
  	    return ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.PER_CHART.name());
      }
      
      public BigDecimal getPerPhotographFee() {
  	    return ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.PER_PHOTOGRAPH.name());
      }
      
      public BigDecimal getPerIllustrationFee() {
  	    return ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.PER_ILLUSTRATION.name());
      }
      
      public BigDecimal getPerFigureFee() {
  	    return ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.PER_FIGURE.name());
      }
      
      public BigDecimal getMaxRoyaltyFee() {
    	    return ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.MAXIMUM_ROYALTY_FEE.name());
      }
      	
      public BigDecimal getPerArticleAuthorFee() {
    	    return ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.PER_ARTICLE_AUTHOR.name());
      }
  
      public BigDecimal getPerArticleNonAuthorFee() {
  	    return ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.PER_ARTICLE_NON_AUTHOR.name());
      }

      public BigDecimal getTo30DaysFee() {
    	    return ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.TO_30_DAYS_FEE.name());
      }
      	
      public BigDecimal getTo180DaysFee() {
    	    return ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.TO_180_DAYS_FEE.name());
      }
      	
      public BigDecimal getTo365DaysFee() {
    	    return ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.TO_365_DAYS_FEE.name());
      }
      	
      public BigDecimal getUnlimitedDaysFee() {
    	    return ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.UNLIMITED_DAYS_FEE.name());
      }
      	
      public BigDecimal getTo49RecipientsFee() {
    	    return ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.TO_49_RECIPIENTS_FEE.name());
      }
      
      public BigDecimal getTo249RecipientsFee() {
    	    return ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.TO_249_RECIPIENTS_FEE.name());
      }
      
      public BigDecimal getTo499RecipientsFee() {
    	    return ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.TO_499_RECIPIENTS_FEE.name());
      }
      
      public BigDecimal getTo500pRecipientsFee() {
    	    return ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.TO_500P_RECIPIENTS_FEE.name());
      }

      @Override
      public String getPreviewPDFUrl(){
    	  if(_item.getExternalRightId()!=null && _item.getExternalRightId().compareTo(2L)==0 && _item.getItemParms().containsKey("PREVIEWPDFURL")){
    		  return _item.getItemParms().get("PREVIEWPDFURL").getParmValue();
    	  }
    	  return null;
      }
      
      public boolean getLicenseeRequestedEntireWork() {
    	  return ItemHelperServices.getItemParmBoolean(getItem(), ItemParmEnum.LCN_REQUESTED_ENTIRE_WORK.name());
      }
      public void setLicenseeRequestedEntireWork(boolean licenseeEntireWork){
    	  ItemHelperServices.setItemParmBoolean(getItem(), ItemParmEnum.LCN_REQUESTED_ENTIRE_WORK.name(),licenseeEntireWork);
      }          
          
}
