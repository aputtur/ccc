package com.copyright.ccc.web.mock;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.copyright.ccc.business.services.ItemConstants;
import com.copyright.ccc.business.services.cart.ECommerceConstants;
import com.copyright.ccc.web.transaction.RepublicationConstants;
import com.copyright.ccc.web.transaction.TransactionConstants;
import com.copyright.data.account.Address;
import com.copyright.data.inventory.Right;

public class MockOrderLicense //implements OrderLicense
{
	private static final long serialVersionUID = 1L;
	
    private long _Id;
    private long _orderId;
    private long _purchaseId;
    private long prdInst;
    private long tpuInst;
    private Long _externalTouId;
    private String _touName;
    private Date _createDate;
    private Date _invoiceDate = new Date();
    private String _permissionStatus;
    private String _displayPermissionStatus;
    private String _billingStatus;
    private String _invoiceId;
    private String _publicationTitle;
    private String _standardNumber;
    private String _publisher;
    private String _publicationYear;
    private String _productCd;
    private String _productName;
    private String _rightsholder;
    private String _rightsQualifyingStatement;
    private String _editor;
    private String _author;
    private String _volume;
    private String _edition;
    private String _permissionSelected;
    private String _price;
    private String _publicationYearOfUse;
    private String _customerReference;
    private Date _publicationDateOfUse;
    private long _numberOfPages;
    private long _numberOfCopies;
    private int _numberOfStudents;
    private int _circulationDistribution;
    private String _business;
    private String _typeOfContent;
    private boolean _isSubmitterAuthor;
    private Date _contentsPublicationDate;
    private Date _dateOfUse;
    private long _numberOfRecipients;
    private int _duration;
    private String _webAddress;
    private String _chapterArticle;
    private String _pageRange;
    private String _republishingOrganization;
    private String _newPublicationTitle;
    private Date _republicationDate;
    private String _translationLanguage;
    private String _republicationDestination;
    private String _dateOfIssue;
    private String permissionType; // for UI display
    private String poNumber; // for UI display
    private boolean _photocopy;
    private boolean _email;
    private boolean _republication;
    private boolean _intranet;
    private boolean _extranet;
    private boolean _internet;
    private boolean _APS;
    private boolean _ECCS;
    private boolean _rightsLink;
    private boolean _specialOrder;
    private String distributionEvent;
    private String rightQualifierTerms;
    private long _licenseePartyId;
    private Right _right;
	private BigDecimal hardCopyCost;
	private BigDecimal priceAdjustment;
	private BigDecimal shippingFee1;
	private BigDecimal shippingFee2;
	private BigDecimal shippingFee3;
	private BigDecimal shippingFee4;
	private BigDecimal shippingFee5;
	private BigDecimal shippingFee6;
    
    
    public void setRight(Right right)
    {
        this._right = right;
    }

    public Right getRight()
    {
        return _right;
    }

    public void setPublicationTitle(String publicationTitle)
    {
        this._publicationTitle = publicationTitle;
    }

    public String getPublicationTitle()
    {
        return _publicationTitle;
    }

	public String getItemDescription() {
		return null;
	}
	
    public void setItemDescription(String itemDescription) {
    	
    }

	public String getItemSubDescription() {
		return null;
	}

    public void setItemSubDescription(String itemSubDescription) {
    }

    public String getItemTypeCd() {
  	  return null;
    }
    
    public void setItemTypeCd(String itemTypeCd) {
  	  // Needed to meet interface
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

    
    public void setStandardNumber(String standardNumber)
    {
        this._standardNumber = standardNumber;
    }

    public String getStandardNumber()
    {
        return _standardNumber;
    }

    public void setPublisher(String publisher)
    {
        this._publisher = publisher;
    }

    public String getPublisher()
    {
        return _publisher;
    }

    public void setPublicationYear(String publicationYear)
    {
        this._publicationYear = publicationYear;
    }

    public String getPublicationYear()
    {
        return _publicationYear;
    }

    public void setRightsholder(String rightsholder)
    {
        this._rightsholder = rightsholder;
    }

    public String getRightsholder()
    {
        return _rightsholder;
    }

    public void setRightsQualifyingStatement(String rightsQualifyingStatement)
    {
        this._rightsQualifyingStatement = rightsQualifyingStatement;
    }

    public String getRightsQualifyingStatement()
    {
        return _rightsQualifyingStatement;
    }

    public void setEditor(String editor)
    {
        this._editor = editor;
    }

    public String getEditor()
    {
        return _editor;
    }

    public void setAuthor(String author)
    {
        this._author = author;
    }

    public String getAuthor()
    {
        return _author;
    }

    public void setVolume(String volume)
    {
        this._volume = volume;
    }

    public String getVolume()
    {
        return _volume;
    }

    public void setEdition(String edition)
    {
        this._edition = edition;
    }

    public String getEdition()
    {
        return _edition;
    }

    public void setPermissionSelected(String permissionSelected)
    {
        this._permissionSelected = permissionSelected;
    }

    public String getPermissionSelected()
    {
        return _permissionSelected;
    }

    public void setPrice(String price)
    {
        this._price = price;
    }

    public String getPrice()
    {
        return _price;
    }

    public double getPriceValue() {
        return 0;
    }


    public void setPublicationYearOfUse(String publicationYearOfUse)
    {
        this._publicationYearOfUse = publicationYearOfUse;
    }

    public String getTransactionId()
    {
        return null;
    }

    public String getPublicationYearOfUse()
    {
        return _publicationYearOfUse;
    }

    public void setCustomerReference(String customerReference)
    {
        this._customerReference = customerReference;
    }

    public String getCustomerReference()
    {
        return _customerReference;
    }

    public void setPublicationDateOfUse(Date publicationDateOfUse)
    {
        this._publicationDateOfUse = publicationDateOfUse;
    }

    public Date getPublicationDateOfUse()
    {
        return _publicationDateOfUse;
    }

    public void setNumberOfPages(long numberOfPages)
    {
        this._numberOfPages = numberOfPages;
    }

    public long getNumberOfPages()
    {
        return _numberOfPages;
    }

    public void setNumberOfCopies(long numberOfCopies)
    {
        this._numberOfCopies = numberOfCopies;
    }

    public long getNumberOfCopies()
    {
        return _numberOfCopies;
    }

    public void setNumberOfStudents(int numberOfStudents)
    {
        this._numberOfStudents = numberOfStudents;
    }

    public int getNumberOfStudents()
    {
        return _numberOfStudents;
    }

    public void setCirculationDistribution(int circulationDistribution)
    {
        this._circulationDistribution = circulationDistribution;
    }

    public int getCirculationDistribution()
    {
        return _circulationDistribution;
    }

    public void setBusiness(String business)
    {
        this._business = business;
    }

    public String getBusiness()
    {
        return _business;
    }

    public String getBusinessDescription()
    {
        String rtnValue = "";
        if (RepublicationConstants.BUSINESS_FOR_PROFIT.equals(_business)) {
            rtnValue = TransactionConstants.FOR_PROFIT;
        } else if (RepublicationConstants.BUSINESS_NON_FOR_PROFIT.equals(_business)) {
            rtnValue = TransactionConstants.NOT_FOR_PROFIT;
        }
        return rtnValue;
    }
    
    public void setTypeOfContent(String typeOfContent)
    {
        this._typeOfContent = typeOfContent;
    }

    public String getTypeOfContent()
    {
        return _typeOfContent;
    }

    // Just use the all caps version for mock order license
    public String getTypeOfContentDescription()
    {
        return _typeOfContent;
    }

    public void setSubmitterAuthor(boolean isSubmitterAuthor)
    {
        this._isSubmitterAuthor = isSubmitterAuthor;
    }

    public boolean isSubmitterAuthor()
    {
        return _isSubmitterAuthor;
    }

    public String getSubmitterAuthor()
    {
        return ItemConstants.NO_CD;
    }
    
    public void setContentsPublicationDate(Date contentsPublicationDate)
    {
        this._contentsPublicationDate = contentsPublicationDate;
    }

    public Date getContentsPublicationDate()
    {
        return _contentsPublicationDate;
    }

    public void setDateOfUse(Date dateOfUse)
    {
        this._dateOfUse = dateOfUse;
    }

    public Date getDateOfUse()
    {
        return _dateOfUse;
    }

    public void setNumberOfRecipients(long numberOfRecipients)
    {
        this._numberOfRecipients = numberOfRecipients;
    }

    public long getNumberOfRecipients()
    {
        return _numberOfRecipients;
    }

    public void setDuration(int duration)
    {
        this._duration = duration;
    }

    public int getDuration()
    {
        return _duration;
    }

    public void setWebAddress(String webAddress)
    {
        this._webAddress = webAddress;
    }

    public String getWebAddress()
    {
        return _webAddress;
    }

    public void setChapterArticle(String chapterArticle)
    {
        this._chapterArticle = chapterArticle;
    }

    public String getChapterArticle()
    {
        return _chapterArticle;
    }

    public void setPageRange(String pageRange)
    {
        this._pageRange = pageRange;
    }

    public String getPageRange()
    {
        return _pageRange;
    }

    public void setRepublishingOrganization(String republishingOrganization)
    {
        this._republishingOrganization = republishingOrganization;
    }

    public String getRepublishingOrganization()
    {
        return _republishingOrganization;
    }

    public void setNewPublicationTitle(String newPublicationTitle)
    {
        this._newPublicationTitle = newPublicationTitle;
    }

    public String getNewPublicationTitle()
    {
        return _newPublicationTitle;
    }

    public void setRepublicationDate(Date republicationDate)
    {
        this._republicationDate = republicationDate;
    }

    public Date getRepublicationDate()
    {
        return _republicationDate;
    }

    public void setTranslationLanguage(String translationLanguage)
    {
        this._translationLanguage = translationLanguage;
    }

    public String getTranslationLanguage()
    {
        return _translationLanguage;
    }

    public String getTranslationLanguageDescription()
    {
        return _translationLanguage;
    }

    public void setRepublicationDestination(String republicationDestination)
    {
        this._republicationDestination = republicationDestination;
    }

    public String getRepublicationDestination()
    {
        return _republicationDestination;
    }

    public void setDateOfIssue(String dateOfIssue)
    {
        this._dateOfIssue = dateOfIssue;
    }

    public String getDateOfIssue()
    {
        return _dateOfIssue;
    }

    public void setPhotocopy(boolean photocopy)
    {
        this._photocopy = photocopy;
    }

    public boolean isPhotocopy()
    {
        return _photocopy;
    }

    public void setEmail(boolean email)
    {
        this._email = email;
    }

    public boolean isEmail()
    {
        return _email;
    }

    public void setRepublication(boolean republication)
    {
        this._republication = republication;
    }

    public boolean isRepublication()
    {
        return _republication;
    }

    public void setIntranet(boolean intranet)
    {
        this._intranet = intranet;
    }

    public boolean isIntranet()
    {
        return _intranet;
    }

    public void setExtranet(boolean extranet)
    {
        this._extranet = extranet;
    }

    public boolean isExtranet()
    {
        return _extranet;
    }

    public void setInternet(boolean internet)
    {
        this._internet = internet;
    }

    public boolean isInternet()
    {
        return _internet;
    }
    public void setRightsLink(boolean rL)
    {
        this._rightsLink = rL;
    }

    public boolean isRightsLink()
    {
        return _rightsLink;
    }

    public void setAPS(boolean aPS)
    {
        this._APS = aPS;
    }

    public boolean isAPS()
    {
        return _APS;
    }

    public void setECCS(boolean eCCS)
    {
        this._ECCS = eCCS;
    }

    public boolean isECCS()
    {
        return _ECCS;
    }

    public void setSpecialOrder(boolean specialOrder)
    {
        this._specialOrder = specialOrder;
    }

    public boolean isSpecialOrder()
    {
        return _specialOrder;
    }
    
    public void setSpecialOrderTypeCd(String specialOrderTypeCd)
    {
  	  // Nothing to do here, meeting transactionItem interface  
    }

    public String getSpecialOrderTypeCd()
    {
  	  return null;  
    }
    
    public int getUsageType()
    {
        return 1;
    }

    public String getCustomerRef()
    {
        return _customerReference;
    }

    public long getRgtInst()
    {
        return 0;
    }

    public Long getRightId()
    {
        return null;
    }
    
    public String getBillingStatus()
    {
        return this._billingStatus;
    }
    public void setBillingStatus(String status)
    {
        this._billingStatus = status;
    }
	
    public String getItemStatusDescription() {
        return this._billingStatus;
	}
    
    public Date getCreateDate()
    {
        return this._createDate;
    }
    public String getCreateDateStr() {
       Date orderDate = this.getCreateDate();
       String orderDateStr = "";
       if (orderDate != null) {
          SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
          orderDateStr = format.format(orderDate);            
       }
    
       return orderDateStr;
    }
    
    public void setCreateDate(Date date)
    {
        this._createDate = date;
    }

    public String getCreditAuth()
    {
        return "";
    }

    public String getExternalCommentOverride()
    {
        return "";
    }
    
    public String getExternalCommentTerm()
    {
        return "";
    }
    
    public String getInvoiceId()
    {
        return this._invoiceId;
    }
 
    public Date getInvoiceDate()
    {
        return this._invoiceDate;
    } 
    
    public void setInvoiceId(String id)
    {
        this._invoiceId = id;
    }
    
    public Long getPaymentId() {
    	return null;
    }
    
    public String getPaymentMethodCd() {
    	return null;
	}
	
	public String getCurrencyType() {
		return ItemConstants.CURRENCY_CODE_USD;
	}
	
	public String getCcAuthNo() {
		return null;
	}
	
	public Long getCcTrxId() {
		return null;
	}
	
	public Date getCcTrxDate() {
		return null;
	}
	
	public BigDecimal getExchangeRate() {
		return new BigDecimal(1);
	}
	
	public Date getExchangeDate() {
		return null;
	}
	
	public String getMerchantRefId() {
		return null;
	}
	
	public BigDecimal getUsdTotal() {
		return null;
	}
	
	public BigDecimal getCurrencyPaidTotal() {
		return null;
	}
	
	public Long getCccProfileId() {
		return null;
	}
	
	public String getPaymentProfileIdentifier() {
		return null;
	}
	
    
    public boolean isCancelable()
    {
        return true;
    }

    public long getLastFourCreditCard()
    {
        return 1409;
    }

    public long getID()
    {
        return this._Id;
    }
    public void setID(long id)
    {
        this._Id = id;
    }
    
    public long getOrderId()
    {
        return this._orderId;
    }
    
    public Long getOrderHeaderId() {
   		return null;  	
    }
     
    public void setOrderId(long id)
    {
        this._orderId = id;
    }
    
    public String getPermissionStatus()
    {
        return this._permissionStatus;
    }

    public String getItemAvailabilityCd()
    {
        return this._permissionStatus;
    }

    public String getItemAvailabilityDescription()
    {
        return this._permissionStatus;
    }
    
    public String getItemOrigAvailabilityCd() {  
	    return getItemAvailabilityCd();
    }

    public String getItemOrigAvailabilityDescription() {
  	  	return getItemAvailabilityDescription();
    }
    
    public String getItemOrigAvailabilityDescriptionInternal() {
  	  	return getItemAvailabilityDescription();
    }
    
    public String getItemAvailabilityDescriptionInternal() {
  	  	return getItemAvailabilityDescription();
    }   
    
    public void setItemOrigAvailabilityCd(String itemOrigAvailabilityCd)
    {
    	// Does not happen here for TF
    }
    
    public String getDisplayPermissionStatus()
    {
        return this._displayPermissionStatus;
    }
    
    public void setPermissionStatus(String status)
    {
        this._permissionStatus = status;
    }

    public void setItemAvailabilitCd(String status)
    {
        this._permissionStatus = status;
    }
 
    
    public Date getPinningDate()
    {
        return new Date();
    }

    public long getPurchaseId()
    {
        return this._purchaseId;
    }
    
    public void setPurchaseId(long id)
    {
        this._purchaseId = id;
    }

    public Long getBundleId()
    {
        return this._purchaseId;
    }
    
    public void setBundleId(Long id)
    {
        // Not in use
    }
    
    
    public String getWithdrawnCode()
    {
        return "";
    }
    
    public long getWorkInst()
    {
        return 0;
    }
    
    public Long getExternalItemId() {
    	return null;
    }
    
    public void setExternalItemId(Long wrkInst)
    {
    	    
    }
    
    public void setWorkInst(long wrkInst)
    {
        //  Do nothing
    }
    
    public boolean isAcademic()
    {
        return _APS || _ECCS;
    }
    
    public boolean isNet()
    {
        return _intranet || _extranet || _internet;
    }
    
    public boolean isDigital()
    {
        return _email || isNet();
    }

    public boolean isSpecialOrderFromScratch()
    {
        return false;
    }


  public String getDurationString()
  {
    return null;
  }


    public void setPermissionType(String permissionType) {
        this.permissionType = permissionType;
    }

    public String getPermissionType() {
        return permissionType;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public String getPoNumber() {
        return poNumber;
    }
    
    public void setPrdInst(long inst) { this.prdInst = inst; }
    public long getPrdInst() { return this.prdInst; }
    
    public void setTpuInst(long inst) { this.tpuInst = inst; }
    public long getTpuInst() { return this.tpuInst; }

    public String getProductCd() {
  	  return _productCd;
    }

    public void setProductCd(String productCd) {
  	  this._productCd = productCd;
    }

    public Long getProductSourceKey() {
  	  return Long.valueOf(this.prdInst);
    }

    public void setProductSourceKey(Long productSourceKey) {
    	this.prdInst = productSourceKey;
    }

    public String getProductName() {
  	  return _productName;
    }

    public void setProductName(String productName) {
  	  this._productName = productName;
    }

    public Long getTouSourceKey()
    {
  	  return tpuInst; 
    }

    public Long getExternalTouId()
    {
  	  return null; 
    }

    
    public void setTouSourceKey(Long touSourceKey) {
        this.tpuInst = touSourceKey.longValue();	  
    } 
    
    public String getTouName()
    {
  	  return _touName; 
    }
   
    public void setTouName(String touName) {
        this._touName = touName;	  
    } 
    
  public Date getPublicationStartDate()
  {
    return null;
  }


  public Date getPublicationEndDate()
  {
    return null;
  }

  public boolean isContactRightsholder()
  {
    return false;
  }

  public boolean isManualSpecialOrder()
  {
    return false;
  }

  public String getCustomAuthor()
  {
    return null;
  }

  public void setCustomAuthor(String customAuthor)
  {
  }

  public String getYourReference()
  {
    return null;
  }
  
  public String getHasVolPriceTiers()
  {      
    return null;
  }
  
  public void setYourReference(String yourReference)
  {
  }
  

  public String getRepublicationTypeOfUse()
  {
    return null;
  }
  
  public String getRepublishInVolEd() 
  {
      String republishInVolEd = "";

      return republishInVolEd;
  }

  public void setRepublishInVolEd(String republishInVolEd) {
  	// Not implemented 
  }

  
  public String getTranslated() 
  {
      String translated = "";

      return translated;
  }

  public String getRepublishFullArticle() 
  {
      String republishFullArticle = "";

      return republishFullArticle;
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
  
  public long getLicenseDetailReferenceID() {
    return 0;
  }
  
  public String getReasonDesc()
  {
     return null;
  }

  public String getLicenseStatusCd()
  {
     return null;
  }

  public long getLicenseePartyId() {
      return 0;
  }
  
  public Long getJobTicketNumber() {
	  return null;
  }

  public Long getExternalDetailId() {
	  return null;
  }
  
  public void setExternalDetailId(Long detailId) {
		// Only needed to meet interface
  }

  public void setJobTicketNumber(Long jobTicket) {
		// Only needed to meet interface
  }
  
  public String getLicenseStatusCdMsg()
  {
     return null;
  }

  public long getReasonCd()
  {
     return 0;
  }
  
  public double getDiscount()
  {
     return 0;
  }
  
  public double getRoyaltyPayable() {
     return 0;
  }
    
  public double getRightsholderFee() {
     return 0;
  }
    
  public double getLicenseeFee() {
     return 0;
  }
  
  public boolean isDistributed() {
      return false;
  }

  public long getNumberOfCartoons()
  {
    return 0l;
  }

  public void setNumberOfCartoons(long numberOfCartoons)
  {
  }

  public long getNumberOfCharts()
  {
    return 0l;
  }

  public void setNumberOfCharts(long numberOfCharts)
  {
  }

  public long getNumberOfExcerpts()
  {
    return 0l;
  }

  public void setNumberOfExcerpts(long numberOfExcerpts)
  {
  }

  public long getNumberOfFigures()
  {
    return 0l;
  }

  public void setNumberOfFigures(long numberOfFigures)
  {
  }

  public long getNumberOfGraphs()
  {
    return 0l;
  }

  public void setNumberOfGraphs(long numberOfGraphs)
  {
  }

  public long getNumberOfIllustrations()
  {
    return 0l;
  }

  public void setNumberOfIllustrations(long numberOfIllustrations)
  {
  }

  public long getNumberOfLogos()
  {
    return 0l;
  }

  public void setNumberOfLogos(long numberOfLogos)
  {
  }

  public long getNumberOfPhotos()
  {
    return 0l;
  }

  public void setNumberOfPhotos(long numberOfPhotos)
  {
  }

  public long getNumberOfQuotes()
  {
    return 0l;
  }

  public void setNumberOfQuotes(long numberOfQuotes)
  {
  }

  public void setLicenseeFee(double licenseeFee)
  {
  }

  public void setDiscount(double discount)
  {
  }

  public double getRoyalty()
  {
    return 0.0;
  }

  public void setRoyalty(double royalty)
  {
  }

  public void setReasonCd(long reasonCd)
  {
  }

  public void setReasonDesc(String reasonDesc)
  {
  }

  public double getRoyaltyComposite()
  {
    return 0.0;
  }

  public BigDecimal getRoyaltyCompositeValue() {
	  return new BigDecimal(this.getRoyaltyComposite()).setScale(2, BigDecimal.ROUND_HALF_EVEN);
  }
  
  public void setSpecialOrderLimitsExceeded(boolean specialOrderLimitsExceeded)
  {
  }

  public String getBaseFee()
  {
    return null;
  }

  public String getFlatFee()
  {
    return null;
  }

  public String getPerPageFee()
  {
    return null;
  }

  public String getPerPageFeeMoneyFormat()
  {
	  return getPerPageFee();
  }
  
  public BigDecimal getPerPageFeeValue()
  {
	  return null;
  }
  
  public String getBaseFeeMoneyFormat()
  {
	  return getBaseFee();
  }
  
  public BigDecimal getBaseFeeValue()
  {
  
	  return null;
  }
  
  public String getFlatFeeMoneyFormat()
  {
	  return getFlatFee();
  }
  
  public BigDecimal getFlatFeeValue()
  {
	  return null;
  }
  
  
  public String getCustomVolume()
  {
    return null;
  }

  public void setCustomVolume(String customVolume)
  {
  }

  public String getCustomEdition()
  {
    return null;
  }

  public void setCustomEdition(String customEdition)
  {
  }

  public boolean isSpecialOrderLimitsExceeded()
  {
    return false;
  }

  public long getRightsholderInst()
  {
    return 0l;
  }
  
  public Long getRightsholderPartyId()
  {
    Long rightsholderPartyId = null;
        
    return rightsholderPartyId;
  }
  
  public Long getRightsholderPtyInst()
  {
    Long rightsholderInst = null;
       
    return rightsholderInst;
  }
  
    public String getRightPermissionType() 
  {
     return null ;      
  }
  
  public long getWrWorkInst() {
      return 0;
  }
  
    public String getIdnoLabel() { return null; }
    public String getSeries() { return null; }
    public String getSeriesNumber() { return null; }
    public String getPublicationType() { return null; }
    public String getPages() { return null; }
    public String getCountry() { return null; }
    public String getLanguage() { return null; }
    
    public void setIdnoLabel(String idnoLabel) {
    	// Fit interface, can not update in TF
    }
    
	public int getOrderDataSource() {
		return ECommerceConstants.ORDER_DATA_SOURCE_TF;
	}
	
	public String getOrderDataSourceDisplay() {
		return ECommerceConstants.ORDER_DATA_SOURCE_TF_DISPLAY;		
	}
	
	public Date getLastUpdatedDate() {
		return new Date();
	}
	
	public String getRhRefNum() {
		return null;
	}
	
  	public void setRhRefNum (String rhRefNum){
		// Placeholder can not update for TF
	}
	
	public String getRightSourceCd() {
		return null;
	}
	
	public String getResearchUserIdentifier() {
		return null;
	}
	
	public String getItemStatusCd() {
		return null;
	}
	
	public String getItemStatusQualifierCd() {
		return null;
	}
	
	public String getItemStatusQualifierDescription() {
		return null;
	}
	
	public String getItemStatusDisplay() {
		return null;
	}
	
	public String getItemStatusInternalDisplay() {
		return null;
	}
	
	public String getItemStatusExternalDisplay() {
		return null;
	}
	
	public String getItemCycleDisplay() {
		return null;
	}
	
	public String getItemErrorDescriptionDisplay() {
		return null;
	}
	
	public String getPaymentTypeDisplay() {
		return null;
	}
	
	public String getRightsholderAccount() {
		return null;
	}

	public Date getOverrideDate() {
		return new Date();
	}
	
	public String getOverrideComment() {
		return null;
	}
	
	public String getOverrideAvailabilityCd() {
		return null;
	}
	
	public Boolean isAdjusted() {
		return false;
	}
	
	public BigDecimal getRightsholderPercent() {
		return new BigDecimal(0);
	}
	
	public String getCategoryCd() {
		return null;
	}
	
	public String getCategoryName() {
		return null;
	}
	
	public String getProductAndCategoryName() {
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
    
	public BigDecimal getTotalPriceValue() {
		return null;
	}

	public BigDecimal getTotalDistributionPayableValue() {
		return null;
	}
	
	public BigDecimal getTotalDiscountValue() {
		return null;
	}

	public BigDecimal getTotalLicenseeFeeValue() {
		return null;
	}

	public BigDecimal getTotalRightsholderFeeValue() {
		return null;
	}

	public BigDecimal getHardCopyCost() {
		return hardCopyCost;
	}

	public void setHardCopyCost(BigDecimal hardCopyCost) {
		this.hardCopyCost = hardCopyCost;
	}

	public BigDecimal getPriceAdjustment() {
		return priceAdjustment;
	}

	public void setPriceAdjustment(BigDecimal priceAdjustment) {
		this.priceAdjustment = priceAdjustment;
	}

	public BigDecimal getShippingFee1() {
		return shippingFee1;
	}

	public void setShippingFee1(BigDecimal shippingFee1) {
		this.shippingFee1 = shippingFee1;
	}

	public BigDecimal getShippingFee2() {
		return shippingFee2;
	}

	public void setShippingFee2(BigDecimal shippingFee2) {
		this.shippingFee2 = shippingFee2;
	}

	public BigDecimal getShippingFee3() {
		return shippingFee3;
	}

	public void setShippingFee3(BigDecimal shippingFee3) {
		this.shippingFee3 = shippingFee3;
	}

	public BigDecimal getShippingFee4() {
		return shippingFee4;
	}

	public void setShippingFee4(BigDecimal shippingFee4) {
		this.shippingFee4 = shippingFee4;
	}

	public BigDecimal getShippingFee5() {
		return shippingFee5;
	}

	public void setShippingFee5(BigDecimal shippingFee5) {
		this.shippingFee5 = shippingFee5;
	}

	public BigDecimal getShippingFee6() {
		return shippingFee6;
	}

	public void setShippingFee6(BigDecimal shippingFee6) {
		this.shippingFee6 = shippingFee6;
	}

	public Long geExternalTouId() {
		return _externalTouId;
	}

	public void setExternalTouId(Long touId) {
		_externalTouId = touId;
	}
	
    public Long getRlCustomerId() {
    	return null;
		// Only needed to fulfill interface, not for TF cart item
    }
    
    public void setRlCustomerId(Long rlCustomerId) {
		// Only needed to fulfill interface, not for TF cart item
    }

	public String getDistributionEvent() {
		return distributionEvent;
	}

	public void setDistributionEvent(String distributionEvent) {
		this.distributionEvent = distributionEvent;
	}

	public String getRightQualifierTerms() {
		return rightQualifierTerms;
	}

	public void setRightQualifierTerms(String rightQualifierTerms) {
		this.rightQualifierTerms = rightQualifierTerms;
	}
	
    public String getResolutionTerms() {
    	  // Needed to meet interface
    	  return null;
    }
      
    public String getLicenseTerms() {
          // Needed to meet interface
    	  return null;
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
}


