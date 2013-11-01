package com.copyright.ccc.business.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.copyright.data.account.Address;
import com.copyright.data.inventory.Right;

/**
 * Interfaces that defines a generic abstraction of an item that
 * can participate in either an e-commerce or in an order history related transaction.
 */
public interface TransactionItem extends Serializable
{
  public String getPublicationTitle();
  
  public String getItemDescription();

  public void setItemDescription(String itemDescription);

  public String getItemSubDescription();  // Article

  public void setItemSubDescription(String itemSubDescription);

  public String getItemTypeCd();
  
  public void setItemTypeCd(String itemTypeCd);
  
  public String getItemSourceCd();
	
  public void setItemSourceCd(String itemSourceCd);  // ItemConstants.ITEM_SOURCE_CD_WR

  public Long getItemSourceKey();

  public void setItemSourceKey(Long itemSourceKey);

  public String getItemSubSourceCd();				
	
  public void setItemSubSourceCd(String itemSubSourceCd); // ItemConstants.ITEM_SOURCE_CD_WR
  
  public Long getItemSubSourceKey();

  public void setItemSubSourceKey(Long itemSubSourceKey);
   
  public String getGranularWorkAuthor();

  public void setGranularWorkAuthor(String granularWorkAuthor);
      
  public String getGranularWorkDoi();
  
  public void setGranularWorkDoi(String granularWorkDoi);
  
  public String getGranularWorkStartPage();
  
  public void setGranularWorkStartPage(String granularWorkStartPage);

  public String getGranularWorkEndPage();
  
  public void setGranularWorkEndPage(String granularWorkEndPage);
  
  public String getGranularWorkPageRange();
  
  public void setGranularWorkPageRange(String granularWorkPageRange);
  
  public Date getGranularWorkPublicationDate();
  
  public void setGranularWorkPublicationDate(Date granularWorkPublicationDate);
  
  public String getGranularWorkVolume();
  
  public void setGranularWorkVolume(String granularWorkVolume);
    
  public String getGranularWorkIssue();
  
  public void setGranularWorkIssue(String granularWorkIssue);
  
  public String getGranularWorkNumber();
  
  public void setGranularWorkNumber(String granularWorkNumber);
  
  public String getGranularWorkSeason();
  
  public void setGranularWorkSeason(String granularWorkSeason);
  
  public String getGranularWorkQuarter();
  
  public void setGranularWorkQuarter(String granularWorkQuarter);

  public String getGranularWorkWeek();

  public void setGranularWorkWeek(String granularWorkWeek);
 
  public String getGranularWorkSection();
  
  public void setGranularWorkSection(String granularWorkSection);

  
  /**
   * 
   * @param publicationTitle
   */
  public void setPublicationTitle(String publicationTitle);
  
  public String getStandardNumber();
  
  /**
   * 
   * @param standardNumber
   */
  public void setStandardNumber(String standardNumber);
  
  public String getPublisher();
  
  public Long getWorkInst();
  public void setWorkInst(Long wrkInst);
  
  public Right getRight();
  
  /**
   * 
   * @param publisher
   */
  public void setPublisher(String publisher);
  
  public String getPublicationYear();
  
  /**
   * 
   * @param publicationYear
   */
  public void setPublicationYear(String publicationYear);
  
  public String getRightsholder();

  public long getRgtInst();
  
  public Long getRightId();
  
  public Long getExternalRightId();

  public String getRightsQualifyingStatement();
  
  public String getEditor();
  
  /**
   * 
   * @param editor
   */
  public void setEditor(String editor);
  
  public String getAuthor();
  
  /**
   * 
   * @param author
   */
  public void setAuthor(String author);
  
  public String getVolume();
  
  /**
   * 
   * @param volume
   */
  public void setVolume(String volume);
  
  public String getEdition();
  
  /**
   * 
   * @param edition
   */
  public void setEdition(String edition);
  
  public String getPermissionSelected();
  
  /**
   * 
   * @param permissionSelected
   */
  public void setPermissionSelected( String permissionSelected );

  
  public String getPrice();
  
  public double getPriceValue();

  public double getPriceValueRaw();
  
  public String getCustomerReference();
  
  public String getProductCd();
  
  public String getRightSourceCd();
   
  public Long getProductSourceKey();
  
  public String getProductName();
  
  public Long getTouSourceKey();

  public void setTouSourceKey(Long touSourceKey);
  
  public Long getExternalTouId();
  
  public String getTouName();
  
  public void setTouName(String touName);
 
  public Long getCategoryId();

  public void setCategoryId(Long categoryId);
  
  public String getCategoryCd();
  
  public String getCategoryName();
  
  public void setCategoryName(String categoryName);
  
  public String getProductAndCategoryName();
  
  
  /**
   * 
   * @param yourReference
   */
  public void setCustomerReference(String yourReference);
  
  public String getPublicationYearOfUse();
  
  /**
   * 
   * @param publicationYearOfUse
   */
  public void setPublicationYearOfUse(String publicationYearOfUse);
  
  public Date getPublicationDateOfUse();
  
  /**
   * 
   * @param publicationDateOfUse
   */
  public void setPublicationDateOfUse(Date publicationDateOfUse);
  
  public long getNumberOfPages();

  /**
   * 
   * @param numberOfPages
   */
  public void setNumberOfPages(long numberOfPages);

  public long getNumberOfCopies();

  /**
   * 
   * @param numberOfCopies
   */
  public void setNumberOfCopies(long numberOfCopies);

  public int getNumberOfStudents();

  /**
   * 
   * @param numberOfStudents
   */
  public void setNumberOfStudents(int numberOfStudents);

  public int getCirculationDistribution();

  /**
   * 
   * @param circulationDistribution
   */
  public void setCirculationDistribution(int circulationDistribution);

  public String getBusiness();

  /*
   * For display in UI so we don't show an all caps constant
   */
  public String getBusinessDescription();
  
  /**
   * 
   * @param business
   */
  public void setBusiness(String business);

  public String getTypeOfContent();
  
  public String getTypeOfContentDescription();
    
//public String getTypeOfUseDescription();

  /**
   * 
   * @param typeOfContent
   */
  public void setTypeOfContent(String typeOfContent);

  public boolean isSubmitterAuthor();
  
  public String getSubmitterAuthor();
  
  public boolean isContactRightsholder();
  
  public boolean isManualSpecialOrder();

  /**
   * 
   * @param isSubmitterAuthor
   */
  public void setSubmitterAuthor(boolean isSubmitterAuthor);

  public Date getContentsPublicationDate();

  /**
   * 
   * @param contentsPublicationDate
   */
  public void setContentsPublicationDate(Date contentsPublicationDate);

  public Date getDateOfUse();

  /**
   * 
   * @param dateOfUse
   */
  public void setDateOfUse(Date dateOfUse);

  public long getNumberOfRecipients();

  /**
   * 
   * @param numberOfRecipients
   */
  public void setNumberOfRecipients(long numberOfRecipients);

  public int getDuration();

  /**
   * 
   * @param duration
   */
  public void setDuration(int duration);

  public String getWebAddress();

  /**
   * 
   * @param webAddress
   */
  public void setWebAddress(String webAddress);
  
  public String getChapterArticle();
  
  /**
   * 
   * @param chapterArticle
   */
  public void setChapterArticle(String chapterArticle);
  
  public String getPageRange();
  
  /**
   * 
   * @param pageRange
   */
  public void setPageRange(String pageRange);
  
  public String getRepublishingOrganization();
  
  /**
   * 
   * @param republishingOrganization
   */
  public void setRepublishingOrganization(String republishingOrganization);
  
    
  public String getNewPublicationTitle();
  
  /**
   * 
   * @param newPublicationTitle
   */
  public void setNewPublicationTitle(String newPublicationTitle);
  
  public Date getRepublicationDate();
  
  /**
   * 
   * @param republicationDate
   */
  public void setRepublicationDate(Date republicationDate);
    
  public String getTranslated();
  
  public String getTranslationLanguage();
  
  public String getTranslationLanguageDescription();
    
  /**
   * 
   * @param translationLanguage
   */
  public void setTranslationLanguage(String translationLanguage);
  
  public String getRepublicationDestination();
  
  /**
   * 
   * @param republicationDestination
   */
  public void setRepublicationDestination(String republicationDestination);
  
  public String getDateOfIssue();
  
  public String getDurationString();
  
  public Date getPublicationStartDate();
  
  public Date getPublicationEndDate();
  
  public String getCustomAuthor();
  
  public void setCustomAuthor( String customAuthor );
  
  public String getYourReference();
  
  public String getHasVolPriceTiers();
  
  public void setYourReference( String yourReference );
  
  public long getNumberOfCartoons();
  
  public void setNumberOfCartoons( long numberOfCartoons );
  
  public long getNumberOfCharts();
  
  public void setNumberOfCharts( long numberOfCharts );
  
  public long getNumberOfExcerpts();
  
  public void setNumberOfExcerpts( long numberOfExcerpts );
  
  public long getNumberOfFigures();
  
  public void setNumberOfFigures( long numberOfFigures );
  
  public long getNumberOfGraphs();
  
  public void setNumberOfGraphs( long numberOfGraphs );
  
  public long getNumberOfIllustrations();
  
  public void setNumberOfIllustrations( long numberOfIllustrations );
  
  public long getNumberOfLogos();
  
  public void setNumberOfLogos( long numberOfLogos );
  
  public long getNumberOfPhotos();
  
  public void setNumberOfPhotos( long numberOfPhotos );
  
  public long getNumberOfQuotes();
  
  public void setNumberOfQuotes( long numberOfQuotes );
  
//  public String getRlDetailHTML();
  
//  public void setRlDetailHTML();
  
  public double getLicenseeFee();
  
  public void setLicenseeFee( double licenseeFee );
  
  public double getDiscount();
  
  public void setDiscount( double discount );
  
  @Deprecated
  public double getRoyalty();
  
  @Deprecated
  public void setRoyalty( double royalty );
  
  public double getRoyaltyComposite();
  
  public BigDecimal getRoyaltyCompositeValue();
  
  public double getRightsholderFee();
  
  public long getReasonCd();
  
  public void setReasonCd( long reasonCd );

  public String getReasonDesc();
  
  public void setReasonDesc( String reasonDesc );
   
  /**
   * 
   * @param dateOfIssue
   */
  public void setDateOfIssue(String dateOfIssue);
  
  public String getRepublicationTypeOfUse();

  public String getRepublishFullArticle();
  
  public String getRepublishInVolEd();
  
  public void setRepublishInVolEd(String republishInVolEd);
  
  public String getRepublishNonTextPortion();
  
  public String getRepublishSection();
  
  public String getRepublishPoNumDtl();
  
  public boolean isAcademic();

  public boolean isPhotocopy();
  
  public boolean isEmail();

  public boolean isRepublication();
  
  public boolean isNet();
  
  public boolean isIntranet();
  
  public boolean isExtranet();

  public boolean isInternet();
  
  public boolean isDigital();
  
  public boolean isAPS();
  
  public boolean isECCS(); 
  
  public boolean isRightsLink();
  
  public boolean isReprint();
  
  public boolean isPricedReprint();
  
//  public boolean isReprints();

  public boolean isSpecialOrder();
  
  public boolean isSpecialOrderFromScratch();
  
  public void setSpecialOrderLimitsExceeded( boolean specialOrderLimitsExceeded );
  
  public void setSpecialOrderTypeCd(String specialOrderTypeCd);
 
  public String getSpecialOrderTypeCd();
  
  @Deprecated
  public String getBaseFee();
  
  @Deprecated
  public String getFlatFee();
  
  @Deprecated
  public String getPerPageFee();
 
  public String getBaseFeeMoneyFormat();
  
  public String getFlatFeeMoneyFormat();
  
  public String getPerPageFeeMoneyFormat();
  
  public String getEntireBookFeeMoneyFormat();
  
  public BigDecimal getBaseFeeValue();
  
  public BigDecimal getFlatFeeValue();
  
  public BigDecimal getPerPageFeeValue();
  
  public BigDecimal getEntireBookFeeValue();
  
  public String getCustomVolume();
  
  public void setCustomVolume( String customVolume );
  
  public String getCustomEdition();
  
  public void setCustomEdition( String customEdition );

  public String getExternalCommentTerm();
  
  public long getRightsholderInst();
  
  public String getRightPermissionType();
  
  public Long getWrWorkInst();
  
  public Long getExternalItemId();
  
  public void setExternalItemId(Long wrkInst);

    //  2009-10-22  MSJ  Matching up with Publication class.
    //  Methods added for Jan 2010 release.  GetIdnoLabel
    //  translates the getIdnoTypeCd value to a readable
    //  value mapped to the CC2Configuration.properties
    //  file.
    
  public String getIdnoLabel();
  public String getIdnoTypeCd();
  public String getSeries();
  public String getSeriesNumber();
  public String getPublicationType();
  public String getPages();
  public String getCountry();
  public String getLanguage();
  public String getItemAvailabilityCd();
  
  public String getItemOrigAvailabilityCd();

  public void setItemOrigAvailabilityCd(String itemOrigAvailabilityCd);
  
  public String getItemAvailabilityDescription();
 
  public String getItemOrigAvailabilityDescription();

  public String getItemAvailabilityDescriptionInternal();
  
  public String getItemOrigAvailabilityDescriptionInternal();

  public BigDecimal getTotalDistributionPayableValue();

  public BigDecimal getTotalLicenseeFeeValue();
	
  public BigDecimal getTotalDiscountValue();
	
  public BigDecimal getTotalPriceValue();  
    
  public BigDecimal getTotalRightsholderFeeValue();

  public String getRightQualifierTerms();
  public String getLicenseTerms();
//  public String getWorkQualifierTerms();
  public String getResolutionTerms();
//  public String getSpecialConfirmationTerms();
  // TODO Add other terms
  
  // RL Fee fields
  public BigDecimal getHardCopyCost();
  public BigDecimal getPriceAdjustment();
  public BigDecimal getShippingFee1();
  public BigDecimal getShippingFee2();
  public BigDecimal getShippingFee3();
  public BigDecimal getShippingFee4();
  public BigDecimal getShippingFee5();
  public BigDecimal getShippingFee6();
  
  public void setHardCopyCost(BigDecimal hardCopyCost);
  public void setPriceAdjustment(BigDecimal priceAdjustment);
  public void setShippingFee1(BigDecimal shippingFee1);
  public void setShippingFee2(BigDecimal shippingFee2);
  public void setShippingFee3(BigDecimal shippingFee3);
  public void setShippingFee4(BigDecimal shippingFee4);
  public void setShippingFee5(BigDecimal shippingFee5);
  public void setShippingFee6(BigDecimal shippingFee6);

  public void setRlCustomerId(Long RlCustomerID);
  public Long getRlCustomerId();
  
  public List<Address> getShippingAddress();
  
  public BigDecimal getPerLogoFee();			
  public BigDecimal getPerGraphFee();	
  public BigDecimal getPerCartoonFee();	
  public BigDecimal getPerExcerptFee();	
  public BigDecimal getPerQuoteFee();	
  public BigDecimal getPerChartFee();	
  public BigDecimal getPerPhotographFee();	
  public BigDecimal getPerIllustrationFee();	
  public BigDecimal getPerFigureFee();	

  public BigDecimal getMaxRoyaltyFee();		
  public BigDecimal getPerArticleAuthorFee();	
  public BigDecimal getPerArticleNonAuthorFee();	
  public BigDecimal getTo30DaysFee();		
  public BigDecimal getTo180DaysFee();		
  public BigDecimal getTo365DaysFee();		
  public BigDecimal getUnlimitedDaysFee();		
  public BigDecimal getTo49RecipientsFee();		
  public BigDecimal getTo249RecipientsFee();	
  public BigDecimal getTo499RecipientsFee();	
  public BigDecimal getTo500pRecipientsFee();	
  
  public String getRlDetailHtml();
  public void setRlDetailHtml(String rlDetailHtml);
  
  public Long getTfWksInst();
  public void setTfWksInst(Long tfWksInst);
  public String getManagedRedirectLink();
  public void setManagedRedirectLink(String managedRedirectLink);
  public String getPreviewPDFUrl ();
  public boolean getLicenseeRequestedEntireWork();
  public void setLicenseeRequestedEntireWork(boolean licenseeEntireWork);
    
}
