package com.copyright.ccc.business.services.order;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.copyright.base.Constants;
import com.copyright.ccc.business.data.OrderLicense;
import com.copyright.ccc.business.services.ItemAvailabilityEnum;
import com.copyright.ccc.business.services.ItemConstants;
import com.copyright.ccc.business.services.OdtStatusEnum;
import com.copyright.ccc.business.services.OrderDataSourceEnum;
import com.copyright.ccc.business.services.ProductEnum;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.business.services.cart.ECommerceConstants;
import com.copyright.ccc.business.services.ecommerce.PurchasablePermissionFactory;
import com.copyright.ccc.config.ApplicationResources;
import com.copyright.ccc.web.transaction.RepublicationConstants;
import com.copyright.ccc.web.transaction.TransactionConstants;
import com.copyright.ccc.web.util.WebUtils;
import com.copyright.data.account.Address;
import com.copyright.data.inventory.Right;
import com.copyright.data.inventory.RightFee;
import com.copyright.data.inventory.Term;
import com.copyright.data.inventory.Work;
import com.copyright.data.order.License;
import com.copyright.data.order.OrderSourceEnum;
import com.copyright.data.order.UsageData;
import com.copyright.data.order.UsageDataAcademic;
import com.copyright.data.order.UsageDataEmail;
import com.copyright.data.order.UsageDataNet;
import com.copyright.data.order.UsageDataPhotocopy;
import com.copyright.data.order.UsageDataRepublication;
import com.copyright.data.pricing.RightFees;
import com.copyright.domain.data.WorkExternal;
import com.copyright.service.pricing.PricingServiceAPI;
import com.copyright.service.pricing.PricingServiceFactory;
import com.copyright.svc.order.api.data.DeclineReasonEnum;
import com.copyright.svc.order.api.data.ItemStatusEnum;
import com.copyright.svc.order.api.data.ItemStatusQualifierEnum;
import com.copyright.svc.rightsResolver.api.data.SpecialOrderTypeEnum;
import com.copyright.svc.rightsResolver.api.data.TouCategoryTypeEnum;
import com.copyright.svc.tf.api.data.Party;
import com.copyright.svc.tf.api.data.TFConsumerContext;
import com.copyright.workbench.i18n.Money;

public class OrderLicenseImpl implements OrderLicense {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(OrderLicenseImpl.class);
	
	private License _license;
	private UsageData _usageData;
	private int _usageType = 0;
	private UsageDataAcademic _usageDataAcademic;
	private UsageDataEmail _usageDataEmail;
	private UsageDataNet _usageDataNet;
	private UsageDataPhotocopy _usageDataPhotocopy;
	private UsageDataRepublication _usageDataRepublication;
	private Work _work;
	
	private BigDecimal _baseFeeOverrideValue;
	private BigDecimal _entireBookFeeOverrideValue;
	private BigDecimal _flatFeeOverrideValue;
	private BigDecimal _perPageFeeOverrideValue;
	private BigDecimal _maximumRoyaltyFeeOverrideValue;
	private BigDecimal _durationFeeOverrideValue;
	private BigDecimal _recipientsFeeOverrideValue;
	private BigDecimal _perContentFeeOverrideValue;
	
	private String _orderSourceCd = null;


	static final long INTRANET_TPU_CODE = 204;
	static final long EXTRANET_TPU_CODE = 134;
	static final long INTERNET_TPU_CODE = 203;

	static final long DPS_PRODUCT_CODE = 36;
	static final long TRS_PRODUCT_CODE = 1;
	static final long APS_PRODUCT_CODE = 2;
	static final long ECCS_PRODUCT_CODE = 12;

	static final String DPS_CATEGORY = "DPS";
	static final String ECCS_CATEGORY = "ECCS";
	static final String TRS_CATEGORY = "TRS";
	static final String APS_CATEGORY = "APS";
	static final String RLS_CATEGORY = "RLS";

	private static final int CONTENT_TYPE_UNITARY_QUANTITY = 1;
	private static final int CONTENT_TYPE_ZERO_QUANTITY = 0;
	private static final String FULL_ARTICLE_YES = "Y";
	private static final String FULL_ARTICLE_NO = "N";

	private static final int USAGE_TYPE_ACADEMIC = 1;
	private static final int USAGE_TYPE_EMAIL = 2;
	private static final int USAGE_TYPE_NET = 3;
	private static final int USAGE_TYPE_PHOTOCOPY = 4;
	private static final int USAGE_TYPE_REPUBLICATION = 5;

	private static final long REPUBLICATION_INTERNET_TPU_CODE = 203;
	private static final long REPUBLICATION_INTRANET_TPU_CODE = 204;
	private static final long REPUBLICATION_LINKING_TPU_CODE = 205;
	private static final long REPUBLICATION_FRAMING_TPU_CODE = 206;
	private static final long REPUBLICATION_EMAIL_TPU_CODE = 207;
	private static final long REPUBLICATION_CDROM_TPU_CODE = 189;
	private static final long REPUBLICATION_DVD_TPU_CODE = 190;
	private static final long REPUBLICATION_JOURNAL_TPU_CODE = 177;
	private static final long REPUBLICATION_MAGAZINE_TPU_CODE = 178;
	private static final long REPUBLICATION_NEWSPAPER_TPU_CODE = 184;
	private static final long REPUBLICATION_NEWSLETTER_TPU_CODE = 185;
	private static final long REPUBLICATION_DISSERTATION_TPU_CODE = 186;
	private static final long REPUBLICATION_BROCHURE_TPU_CODE = 187;
	private static final long REPUBLICATION_PAMPHLET_TPU_CODE = 188;
	private static final long REPUBLICATION_ADVERTISEMENT_TPU_CODE = 211;
	private static final long REPUBLICATION_PRESENTATION_TPU_CODE = 214;
	private static final long REPUBLICATION_TEXTBOOK_TPU_CODE = 172;
	private static final long REPUBLICATION_TRADEBOOK_TPU_CODE = 173;
	private static final long REPUBLICATION_OTHERBOOK_TPU_CODE = 209;

	private static final long INVALID_RIGHTSHOLDER_INST = -1L;

	private static final String FEE_FIELD_MAXIMUM_ROYALTY_FEE = "MAXIMUM_ROYALTY_FEE";
	
	private static final String FEE_FIELD_PER_ARTICLE_AUTHOR = "PER_ARTICLE_AUTHOR";
	private static final String FEE_FIELD_PER_ARTICLE_NON_AUTHOR = "PER_ARTICLE_NON_AUTHOR";
	private static final String FEE_FIELD_PER_CARTOON = "PER_CARTOON";
	private static final String FEE_FIELD_PER_CHART = "PER_CHART";
	private static final String FEE_FIELD_PER_EXCERPT = "PER_EXCERPT";
	private static final String FEE_FIELD_PER_FIGURE = "PER_FIGURE";
	private static final String FEE_FIELD_PER_GRAPH = "PER_GRAPH";
	private static final String FEE_FIELD_PER_ILLUSTRATION = "PER_ILLUSTRATION";
	private static final String FEE_FIELD_PER_LOGO = "PER_LOGO";
	private static final String FEE_FIELD_PER_PHOTOGRAPH = "PER_PHOTOGRAPH";
	private static final String FEE_FIELD_PER_QUOTE = "PER_QUOTE";

	private static final String FEE_FIELD_TO_30_DAYS_FEE = "TO_30_DAYS_FEE";
	private static final String FEE_FIELD_TO_180_DAYS_FEE = "TO_180_DAYS_FEE";
	private static final String FEE_FIELD_TO_365_DAYS_FEE = "TO_365_DAYS_FEE";
	private static final String FEE_FIELD_UNLIMITED_DAYS_FEE = "UNLIMITED_DAYS_FEE";
	private static final String FEE_FIELD_TO_49_RECIPIENTS_FEE = "TO_49_RECIPIENTS_FEE";
	private static final String FEE_FIELD_TO_249_RECIPIENTS_FEE = "TO_249_RECIPIENTS_FEE";
	private static final String FEE_FIELD_TO_499_RECIPIENTS_FEE = "TO_499_RECIPIENTS_FEE";
	private static final String FEE_FIELD_TO_500P_RECIPIENTS_FEE = "TO_500P_RECIPIENTS_FEE";
	
	
	private RightFees _fees = null;
	private WorkExternal wrWork = null;

	private boolean _requiresAcademicRepin = false;
	
	public OrderLicenseImpl() {
	}

	public void setLicense(License license) {
		_license = license;
		if (_license.getUsageData() instanceof UsageDataAcademic) {
			_usageDataAcademic = (UsageDataAcademic) license.getUsageData();
			_usageType = USAGE_TYPE_ACADEMIC;
		} else if (_license.getUsageData() instanceof UsageDataEmail) {
			_usageDataEmail = (UsageDataEmail) license.getUsageData();
			_usageType = USAGE_TYPE_EMAIL;
		} else if (_license.getUsageData() instanceof UsageDataNet) {
			_usageDataNet = (UsageDataNet) license.getUsageData();
			_usageType = USAGE_TYPE_NET;
		} else if (_license.getUsageData() instanceof UsageDataPhotocopy) {
			_usageDataPhotocopy = (UsageDataPhotocopy) license.getUsageData();
			_usageType = USAGE_TYPE_PHOTOCOPY;
		} else if (_license.getUsageData() instanceof UsageDataRepublication) {
			_usageDataRepublication = (UsageDataRepublication) license
					.getUsageData();
			_usageType = USAGE_TYPE_REPUBLICATION;
		}

		calculateRightFees();

	}


	public void calculateRightFees() {
		boolean licensePresent = _license != null;

		if (licensePresent) {

			long rightInst = _license.getRgtInst();

			boolean validRightInst = rightInst > 0;

			if (validRightInst) {
				PricingServiceAPI pricingService = PricingServiceFactory
						.getInstance().getService();

				try {
					RightFees fees = pricingService.getFeesForRight(rightInst);

					boolean feesHaveBeenCalculated = fees != null;

					if (feesHaveBeenCalculated) {
						setFees(fees);
					}
				} catch (Exception e) {
					setFees(null);
				}

			}

		}
	}

	public License getLicense() {
		return _license;
	}

	public boolean isAcademic() {
		return _usageType == USAGE_TYPE_ACADEMIC;
	}

	public boolean isEmail() {
		return _usageType == USAGE_TYPE_EMAIL;
	}

	public boolean isNet() {
		return _usageType == USAGE_TYPE_NET;
	}

	public boolean isPhotocopy() {
		return _usageType == USAGE_TYPE_PHOTOCOPY;
	}

	public boolean isRepublication() {
		return _usageType == USAGE_TYPE_REPUBLICATION;
	}

	public boolean isIntranet() {
		return this.getTpuInst() == OrderLicenseImpl.INTRANET_TPU_CODE;
	}

	public boolean isExtranet() {
		return this.getTpuInst() == OrderLicenseImpl.EXTRANET_TPU_CODE;
	}

	public boolean isInternet() {
		return this.getTpuInst() == OrderLicenseImpl.INTERNET_TPU_CODE;
	}

	public boolean isDistributed() {
		return _license.isDistributed();

	}

	public int getUsageType() {
		return _usageType;
	}

	public String getCustomerReference() {
		return _license.getCustomerRef();
	}

	public void setCustomerReference(String yourReference) {
		_license.setCustomerRef(StringUtils.upperCase(yourReference));
	}

	public String getCustomerRef() {
		return _license.getCustomerRef();
	}

	public void setCustomerRef(String yourReference) {
		_license.setCustomerRef(StringUtils.upperCase(yourReference));
	}

	public long getRgtInst() {
		return _license.getRgtInst();
	}

	public Long getRightId() {
		return Long.valueOf(_license.getRgtInst());
	}

	public Long getExternalRightId() {
		return Long.valueOf(_license.getRgtInst());
	}

	public long getTpuInst() {
		return _license.getUsageData().getTpuInst();
	}

	public Long getExternalTouId() {
		return Long.valueOf(_license.getUsageData().getTpuInst());
	}

	public long getPrdInst() {
		return _license.getUsageData().getProduct();
	}

	public String getProductCd() {
		if (getProductSourceKey() == ECommerceConstants.APS_PRODUCT_CODE) {
			return ECommerceConstants.APS_PRODUCT_CD;
		} else if (getProductSourceKey() == ECommerceConstants.DPS_PRODUCT_CODE) {
			return ECommerceConstants.DPS_PRODUCT_CD;
		} else if (getProductSourceKey() == ECommerceConstants.TRS_PRODUCT_CODE) {
			return ECommerceConstants.TRS_PRODUCT_CD;
		} else if (getProductSourceKey() == ECommerceConstants.ECCS_PRODUCT_CODE) {
			return ECommerceConstants.ECCS_PRODUCT_CD;
		} else if (getProductSourceKey() == ECommerceConstants.RLS_PRODUCT_CODE) {
			return ECommerceConstants.RLS_PRODUCT_CD;
		}
		return null;
	}

	public Long getProductSourceKey() {
		if (_license.getUsageData() != null) {
			return Long.valueOf(_license.getUsageData().getProduct());
		}
		return null;
	}

	public String getProductName() {
		if (getProductSourceKey() == ECommerceConstants.APS_PRODUCT_CODE) {
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

	public Long getTouSourceKey() {
		// if (_license.getUsageData() != null) {
		// return Long.valueOf(_license.getUsageData().getTpuInst());
		// }
		return null;
	}

	public String getTouName() {
		return _license.getTypeOfUseDescription();
	}

	public Work getWork() {
		return _license.getWork();
	}

	public UsageData getUsageData() {
		return _license.getUsageData();
	}

	public Right getRight() {
		return _license.getRight();
	}
	
	public String getBillingStatusCode() {
		return _license.getBillingStatus();
	}
	
	public String getBillingStatus() {
		return _license.getBillingStatus();
	}

    public String getBillingStatusCd() {
    	if (getBillingStatus().equalsIgnoreCase(ItemConstants.NOT_INVOICED)) {
    		return ItemConstants.BILLING_STATUS_NOT_BILLED_CD;
    	} else if (getBillingStatus().equalsIgnoreCase(ItemConstants.CANCELED)){
    		return ItemConstants.BILLING_STATUS_CANCELED_CD;
    	} else {	
    		return ItemConstants.BILLING_STATUS_BILLED_CD;
    	}

    }
	
	public String getTransactionId() {
		return _license.getTransactionId();
	}

	public Date getCreateDate() {
		return _license.getCreateDate();
	}

	/**
	 * A date formatter method to control the display format of the Creation
	 * Date
	 * 
	 * @return the Creation Date formatted to the simple date format:
	 *         "MM/dd/yyyy"
	 */
	public String getCreateDateStr() {
		String createDateString = "";

		if (_license.getCreateDate() != null) {
			if (_license.getCreateDate().getTime() > 0) {
				SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy"); // scr
																				// 7899
				createDateString = formatter.format(_license.getCreateDate());
			}
		}

		return createDateString;
	}

	public String getCreditAuth() {
		return _license.getCreditAuth();
	}

	public String getPaymentTypeDisplay() {
		if (getLicense().getCreditAuth() != null) {
			return ItemConstants.PAYMENT_TYPE_CREDIT_CARD;
		} else {
			return ItemConstants.PAYMENT_TYPE_INVOICE;
		}
		
	}
	
	// public String getPermissionSelected() {
	// return _license.getDisplayPermissionStatus();
	// }

	/*
	 * License ID or Order Detail ID
	 */
	public long getID() {
		return _license.getID();
	}

	public String getExternalCommentOverride() {
		return _license.getExternalCommentOverride();
	}

	/*
	 * Returns the External comment term from the Term on the Right TODO kmeyer:
	 * Should this be added to the OrderLicense interface? lalberione: Pulled up
	 * method declaration to TransactionItem
	 */
	public String getExternalCommentTerm() {
		String termStr = "";
		Right right = _license.getRight();
		if (right != null) {
			Term term = right.getExternalCommentTerm();
			if (term != null) {
				termStr = term.getTermText();
			}
		}

		return termStr;
	}

	public String getInvoiceId() {
		return _license.getInvoiceId();
	}

	public boolean isInvoiced() {
		if (this.getInvoiceId() != null && !this.getInvoiceId().isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isPaidByCreditCard() {
		if (this.getPaymentMethodCd().equals(ItemConstants.PAYMENT_METHOD_CREDIT_CARD)) {
			return true;
		} else {
			return false;
		}
	}
	
	public void setInvoiceId(String invoiceId) {
//		Needed for interface only.
	}
	
    public void setInvoiceDate(Date invoiceDate) {
//		Needed for interface only.
    }

    public Long getPaymentId() {
    	return null;
    }
	
	public String getPaymentMethodCd() {
		if (_license.getCreditAuth() != null) {
			return ItemConstants.PAYMENT_METHOD_CREDIT_CARD;
		} else {
			return ItemConstants.PAYMENT_METHOD_INVOICE;
		}
	}
	
	public String getCurrencyType() {
		return ItemConstants.CURRENCY_CODE_USD;
	}
	
	public String getCcAuthNo() {
		return _license.getCreditAuth();
	}
	
	public BigDecimal getCcTrxId() {
		BigDecimal ccTrxId = null;
		
		if (_license.getTransactionId() != null) {
			try {
			ccTrxId = new BigDecimal(_license.getTransactionId());
			} catch (Exception e) {
				return null;
			}
		}
		return ccTrxId;
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
	    
	public long getLicenseePartyId() {
		return _license.getLicenseePartyID();
	}

	public Date getInvoiceDate() {
		return _license.getInvoiceDate();
	}

	public boolean isEditable() {
		if (!isCanceled() && !isInvoiced() && !isPaidByCreditCard()) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isCancelable() {
		
		if (this.getOrderSourceCd() != null) {
			if (this.getOrderSourceCd().equals(OrderSourceEnum.GATEWAY.getCode())) {
				return false;
			}
		}
			
		return _license.isCancelable();
	}

	public boolean isCanceled() {

		if (_license.getOdtStatusCd().equals(OdtStatusEnum.N1900.getItemStatusCd())) {
			return true;
		}
		
		if ((_license.getOdtStatusCd().equals(OdtStatusEnum.N1010.getItemStatusCd()) || 
			 _license.getOdtStatusCd().equals(OdtStatusEnum.N2000A.getItemStatusCd())) &&
			 _license.getWithdrawnCode().equals(ItemConstants.WITHDRAWN_BY_CUST)) {
			return true;
		}
		
		return false;
				
	}
	
	public long getLastFourCreditCard() {
		return _license.getLastFourCreditCard();
	}

	public long getOrderId() {
		return _license.getOrderId();
	}

	public String getDisplayPermissionStatus() {
		return _license.getDisplayPermissionStatus();
	}

	public String getPermissionStatus() {
		// return _license.getPermissionStatus();
		return _license.getDisplayPermissionStatusCd();
	}

	public String getItemAvailabilityCd() {
		ItemAvailabilityEnum itemAvailabilityEnum = ItemAvailabilityEnum
				.getEnumForTFPermissionCd(this.getPermissionStatus());
		if (itemAvailabilityEnum != null) {
			return itemAvailabilityEnum.getStandardPermissionCd();
		}
		return null;
	}

	public String getItemAvailabilityDescription() {
		ItemAvailabilityEnum itemAvailabilityEnum = ItemAvailabilityEnum
				.getEnumForTFPermissionCd(this.getPermissionStatus());
		if (itemAvailabilityEnum != null) {
			return itemAvailabilityEnum.getExternalOrderLabel();
		}
		return null;
	}

	public String getItemOrigAvailabilityCd() {
		String originalAvailabilityCd = null;
		if (getRight() != null) {
			if (getRight().getPermission() != null) {
				originalAvailabilityCd = getRight().getPermission().getPermissionValueCode();				
			}
		}
		return originalAvailabilityCd;
	}

	public String getItemOrigAvailabilityDescription() {
		return getItemOrigAvailabilityDescriptionInternal();
	}

	public String getItemAvailabilityDescriptionInternal() {
		
		ItemAvailabilityEnum itemAvailabilityEnum = ItemAvailabilityEnum
				.getEnumForTFPermissionCd(this.getPermissionStatus());
		if (itemAvailabilityEnum != null) {
			return itemAvailabilityEnum.getOrderLabel();
		}
		return null;
	}

	public String getItemOrigAvailabilityDescriptionInternal() {
		String availabilityLabel = null;
		
		ItemAvailabilityEnum rightAvailabilityEnum = ItemAvailabilityEnum
				.getEnumForTFPermissionCd(getItemOrigAvailabilityCd()); // Right
		
//		if (itemAvailabilityEnum != null) {
//			return itemAvailabilityEnum.getOrderLabel();
//		}
		
		if (rightAvailabilityEnum != null) {
			availabilityLabel = rightAvailabilityEnum.getOrderLabel();

			if (rightAvailabilityEnum.equals(ItemAvailabilityEnum.PURCHASE) && 
				(getOverrideAvailabilityCd() != null ||
				 (getItemAvailabilityCd() != null &&
				  getItemAvailabilityCd().equals(ItemAvailabilityEnum.CONTACT_RH.getStandardPermissionCd())) ||
				 (getItemAvailabilityCd() != null &&
				  getItemAvailabilityCd().equals(ItemAvailabilityEnum.RESEARCH_FURTHER.getStandardPermissionCd())))) {
					availabilityLabel = ItemAvailabilityEnum.CONTACT_RH.getOrderLabel() + " " + ItemConstants.LIMITS_EXCEEDED_LABEL;
			}
			return availabilityLabel;
		}

		return null;
	}

	public void setItemOrigAvailabilityCd(String itemOrigAvailabilityCd) {
		// Does not happen here for TF
	}

	public String getDisplayPermissionStatusCd() {
		return _license.getDisplayPermissionStatusCd();
	}

	public Date getPinningDate() {
		return _license.getPinningDate();
	}

	public long getPurchaseId() {
		return _license.getPurchaseId();
	}

	public Long getOrderHeaderId() {
		return Long.valueOf(_license.getPurchaseId());
	}
	
	public String getExternalOrderId() {
		return Long.valueOf(_license.getOrderId()).toString();
	}
	
	public Long getBundleId() {
		return Long.valueOf(_license.getPurchaseId());
	}

	public void setBundleId(Long bundleId) {
		// Not valid is this scenario, needed to meet interface
	}
	
	// public Money getPrice() {
	// return _license.getPrice();
	// }

	public String getReDistFlag() {
		return _license.getReDistFlag();
	}

	public long getRefOdtInst() {
		return _license.getOrderDetailReferenceID();
	}

	public String getWithdrawnCode() {
		return _license.getWithdrawnCode();
	}

	public String getPublicationTitle() {
		return _license.getWork().getMainTitle();
	}

	public String getStandardNumber() {
		return _license.getWork().getStandardNumber();
	}

	public String getPublisher() {
		return _license.getWork().getPublisher();
	}

	public Party getRightsHolder() {
		return ServiceLocator.getTFService().getOrgRightsholderPartyByPartyId(
				new TFConsumerContext(), _license.getRight()
						.getRightsHolderPartyId());
	}

	public String getAuthor() {
		return _license.getWork().getMainAuthor();
	}

	public String getEditor() {
		return _license.getWork().getMainEditor();
	}

	public String getVolume() {
		return _license.getWork().getVolume();
	}

	public String getEdition() {
		return _license.getWork().getEdition();
	}

	public Date getPublicationDateOfUse() {
		switch (_usageType) {
		case USAGE_TYPE_ACADEMIC:
			return _usageDataAcademic.getPublicationDate();
		case USAGE_TYPE_EMAIL:
			return _usageDataEmail.getPublicationDate();
		case USAGE_TYPE_NET:
			return _usageDataNet.getPublicationDate();
		case USAGE_TYPE_PHOTOCOPY:
			return _usageDataPhotocopy.getPublicationDate();
		case USAGE_TYPE_REPUBLICATION:
			return _usageDataRepublication.getPublicationDate();
		default:
			return null;
		}
	}

	public String getPublicationDateStr() {
		String dateString = "";
		Date date = this.getPublicationDateOfUse();
		if (date != null) {
			if (date.getTime() > 0) {
				SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
				dateString = formatter.format(date);
			}
		}

		return dateString;
	}

	public String getPublicationYearOfUse() {
		switch (_usageType) {
		case USAGE_TYPE_ACADEMIC:
			return getYearFromDate(_usageDataAcademic.getPublicationDate());
		case USAGE_TYPE_EMAIL:
			return getYearFromDate(_usageDataEmail.getPublicationDate());
		case USAGE_TYPE_NET:
			return getYearFromDate(_usageDataNet.getPublicationDate());
		case USAGE_TYPE_PHOTOCOPY:
			return getYearFromDate(_usageDataPhotocopy.getPublicationDate());
		case USAGE_TYPE_REPUBLICATION:
			return getYearFromDate(_usageDataRepublication.getPublicationDate());
		default:
			return null;
		}
	}

	private String getYearFromDate(Date date) {
		if (date == null)
			return "";

		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return String.valueOf(calendar.get(GregorianCalendar.YEAR));
	}

	public void setPublicationYearOfUse(String yearOfUse) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.set(Integer.valueOf(yearOfUse).intValue(), 1, 1);

		switch (_usageType) {
		case USAGE_TYPE_ACADEMIC:	
			String yearOfUseString = null;
			if (_usageDataAcademic.getPublicationDate() != null) {
				GregorianCalendar currentPublicationCalendar = new GregorianCalendar();
				currentPublicationCalendar.setTime(_usageDataAcademic.getPublicationDate());
				int year = currentPublicationCalendar.get(GregorianCalendar.YEAR);
				Integer yearInteger = new Integer(year);
				yearOfUseString = yearInteger.toString();
			}
			if (yearOfUseString == null ||
					yearOfUse == null ||
					!yearOfUseString.equals(yearOfUse)) {
						setRequiresAcademicRepin(true);	
			}
			_usageDataAcademic.setPublicationDate(calendar.getTime());
			break;
		case USAGE_TYPE_EMAIL:
			_usageDataEmail.setPublicationDate(calendar.getTime());
			break;
		case USAGE_TYPE_NET:
			_usageDataNet.setPublicationDate(calendar.getTime());
			break;
		case USAGE_TYPE_PHOTOCOPY:
			_usageDataPhotocopy.setPublicationDate(calendar.getTime());
			break;
		case USAGE_TYPE_REPUBLICATION:
			_usageDataRepublication.setPublicationDate(calendar.getTime());
			break;
		}
		return;
	}

	public long getNumberOfPages() {
		switch (_usageType) {
		case USAGE_TYPE_ACADEMIC:
			return _usageDataAcademic.getNumPages().longValue();
		case USAGE_TYPE_EMAIL:
			return -1;
		case USAGE_TYPE_NET:
			return -1;
		case USAGE_TYPE_PHOTOCOPY:
			return _usageDataPhotocopy.getNumPages().longValue();
		case USAGE_TYPE_REPUBLICATION:
			return _usageDataRepublication.getRlsPages();
		default:
			return -1;
		}
	}

	public void setNumberOfPages(long numberOfPages) {
		switch (_usageType) {
		case USAGE_TYPE_ACADEMIC:
			if (_usageDataAcademic.getNumPages() == null ||
				numberOfPages != _usageDataAcademic.getNumPages().longValue()) {
				setRequiresAcademicRepin(true);	
			}
			_usageDataAcademic.setNumPages(numberOfPages);
			return;
		case USAGE_TYPE_EMAIL:
			return;
		case USAGE_TYPE_NET:
			return;
		case USAGE_TYPE_PHOTOCOPY:
			_usageDataPhotocopy.setNumPages(numberOfPages);
			return;
		case USAGE_TYPE_REPUBLICATION:
			_usageDataRepublication.setRlsPages(numberOfPages);
			if (numberOfPages > 0)
				_usageDataRepublication.resetFeeParameters();
			return;
		default:
			return;
		}
	}

	public long getNumberOfCopies() {
		switch (_usageType) {
		case USAGE_TYPE_ACADEMIC:
			return _usageDataAcademic.getNumStudents().longValue();
		case USAGE_TYPE_EMAIL:
			return -1;
		case USAGE_TYPE_NET:
			return -1;
		case USAGE_TYPE_PHOTOCOPY:
			return _usageDataPhotocopy.getNumCopies().longValue();
		case USAGE_TYPE_REPUBLICATION:
			return -1;
		default:
			return -1;

		}
	}

	public void setNumberOfCopies(long numberOfCopies) {
		switch (_usageType) {
		case USAGE_TYPE_ACADEMIC:
			_usageDataAcademic.setNumStudents(numberOfCopies);
			return;
		case USAGE_TYPE_EMAIL:
			return;
		case USAGE_TYPE_NET:
			return;
		case USAGE_TYPE_PHOTOCOPY:
			_usageDataPhotocopy.setNumCopies(numberOfCopies);
			return;
		case USAGE_TYPE_REPUBLICATION:
			return;
		default:
			return;
		}
	}

	public long getNumberOfRecipients() {
		switch (_usageType) {
		case USAGE_TYPE_ACADEMIC:
			return -1;
		case USAGE_TYPE_EMAIL:
			return _usageDataEmail.getNumRecipients().intValue();
		case USAGE_TYPE_NET:
			return -1;
		case USAGE_TYPE_PHOTOCOPY:
			return -1;
		case USAGE_TYPE_REPUBLICATION:
			return -1; // _usageDataRepublication.getCirculation();
		default:
			return -1;
		}
	}

	public void setNumberOfRecipients(long numberOfRecipients) {
		switch (_usageType) {
		case USAGE_TYPE_ACADEMIC:
			return;
		case USAGE_TYPE_EMAIL:
			_usageDataEmail.setNumRecipients(numberOfRecipients);
			return;
		case USAGE_TYPE_NET:
			return;
		case USAGE_TYPE_PHOTOCOPY:
			return;
		case USAGE_TYPE_REPUBLICATION:
			return; // _usageDataRepublication.setCirculation(numberOfRecipients);
					// return;
		default:
			return;
		}
	}

	public String getBundleComments() {

		return _license.getAcademicOrderHeaderComments();
		
	}
	public String getPermissionSelected() {
		switch (_usageType) {
		case USAGE_TYPE_ACADEMIC:
			return _usageDataAcademic.getTypeOfUseDisplay();
		case USAGE_TYPE_EMAIL:
			return _usageDataEmail.getTypeOfUseDisplay();
		case USAGE_TYPE_NET:
			return _usageDataNet.getTypeOfUseDisplay();
		case USAGE_TYPE_PHOTOCOPY:
			return _usageDataPhotocopy.getTypeOfUseDisplay();
		case USAGE_TYPE_REPUBLICATION:
			return _usageDataRepublication.getTypeOfUseDisplay();
		default:
			return null;
		}
	}

	public int getDuration() {
		switch (_usageType) {
		case USAGE_TYPE_ACADEMIC:
			return -1;
		case USAGE_TYPE_EMAIL:
			return -1;
		case USAGE_TYPE_NET:
			return _usageDataNet.getDuration();
		case USAGE_TYPE_PHOTOCOPY:
			return -1;
		case USAGE_TYPE_REPUBLICATION:
			return -1;
		default:
			return -1;
		}
	}

	public void setDuration(int duration) {
		switch (_usageType) {
		case USAGE_TYPE_ACADEMIC:
			return;
		case USAGE_TYPE_EMAIL:
			return;
		case USAGE_TYPE_NET:
			_usageDataNet.setDuration(duration);
			return;
		case USAGE_TYPE_PHOTOCOPY:
			return;
		case USAGE_TYPE_REPUBLICATION:
			return;
		default:
			return;
		}
	}

	public String getWebAddress() {
		switch (_usageType) {
		case USAGE_TYPE_ACADEMIC:
			return null;
		case USAGE_TYPE_EMAIL:
			return null;
		case USAGE_TYPE_NET:
			return _usageDataNet.getWebAddress();
		case USAGE_TYPE_PHOTOCOPY:
			return null;
		case USAGE_TYPE_REPUBLICATION:
			return null;
		default:
			return null;
		}
	}

	public void setWebAddress(String webAddress) {
		switch (_usageType) {
		case USAGE_TYPE_ACADEMIC:
			return;
		case USAGE_TYPE_EMAIL:
			return;
		case USAGE_TYPE_NET:
			_usageDataNet.setWebAddress(webAddress);
			return;
		case USAGE_TYPE_PHOTOCOPY:
			return;
		case USAGE_TYPE_REPUBLICATION:
			return;
		default:
			return;
		}
	}

	public String getRepublicationTitle() {
		switch (_usageType) {
		case USAGE_TYPE_ACADEMIC:
			return null;
		case USAGE_TYPE_EMAIL:
			return null;
		case USAGE_TYPE_NET:
			return null;
		case USAGE_TYPE_PHOTOCOPY:
			return null;
		case USAGE_TYPE_REPUBLICATION:
			return _usageDataRepublication.getRepubTitle();
		default:
			return null;
		}
	}

	public String getRepublicationAuthor() {
		switch (_usageType) {
		case USAGE_TYPE_ACADEMIC:
			return null;
		case USAGE_TYPE_EMAIL:
			return null;
		case USAGE_TYPE_NET:
			return null;
		case USAGE_TYPE_PHOTOCOPY:
			return null;
		case USAGE_TYPE_REPUBLICATION:
			return _usageDataRepublication.getAuthor();
		default:
			return null;
		}
	}

	public void setRepublicationAuthor(String republicationAuthor) {
		switch (_usageType) {
		case USAGE_TYPE_ACADEMIC:
			return;
		case USAGE_TYPE_EMAIL:
			return;
		case USAGE_TYPE_NET:
			return;
		case USAGE_TYPE_PHOTOCOPY:
			return;
		case USAGE_TYPE_REPUBLICATION:
			_usageDataRepublication.setAuthor(republicationAuthor);
			return;
		default:
			return;
		}
	}

	/*
	 * 
	 * numberOfStudents republicationDestination circulationDistribution
	 * Business typeOfContent isSubmitterAuthor contentsPublicationDate Duration
	 */

	public String getBusiness() {
		if (_usageType == USAGE_TYPE_REPUBLICATION) {
			if (_usageDataRepublication.getForProfit() == null)
				return RepublicationConstants.BUSINESS_FOR_PROFIT;
			else {
				if (_usageDataRepublication.getForProfit().equalsIgnoreCase(
						RepublicationConstants.FOR_PROFIT)) {
					return RepublicationConstants.BUSINESS_FOR_PROFIT;

				} else {
					return RepublicationConstants.BUSINESS_NON_FOR_PROFIT;
				}
			}
		} else
			return null;
	}

	public String getBusinessDescription() {
		if (_usageType == USAGE_TYPE_REPUBLICATION) {
			if (_usageDataRepublication.getForProfit() == null)
				return TransactionConstants.FOR_PROFIT;
			else {
				if (_usageDataRepublication.getForProfit().equalsIgnoreCase(
						RepublicationConstants.FOR_PROFIT)) {
					return TransactionConstants.FOR_PROFIT;

				} else {
					return TransactionConstants.NOT_FOR_PROFIT;
				}
			}
		} else
			return null;
	}

	public String getChapterArticle() {
		switch (_usageType) {
		case USAGE_TYPE_ACADEMIC:
			return _usageDataAcademic.getChapterArticle();
		case USAGE_TYPE_EMAIL:
			return _usageDataEmail.getChapterArticle();
		case USAGE_TYPE_NET:
			return _usageDataNet.getChapterArticle();
		case USAGE_TYPE_PHOTOCOPY:
			return _usageDataPhotocopy.getChapterArticle();
		case USAGE_TYPE_REPUBLICATION:
			return _usageDataRepublication.getChapterArticle();
		default:
			return null;
		}
	}

	public void setChapterArticle(String chapterArticle) {
		switch (_usageType) {
		case USAGE_TYPE_ACADEMIC:
			_usageDataAcademic.setChapterArticle(chapterArticle);
			return;
		case USAGE_TYPE_EMAIL:
			_usageDataEmail.setChapterArticle(chapterArticle);
			return;
		case USAGE_TYPE_NET:
			_usageDataNet.setChapterArticle(chapterArticle);
			return;
		case USAGE_TYPE_PHOTOCOPY:
			_usageDataPhotocopy.setChapterArticle(chapterArticle);
			return;
		case USAGE_TYPE_REPUBLICATION:
			_usageDataRepublication.setChapterArticle(chapterArticle);
			return;
		default:
			return;
		}
	}

	public int getCirculationDistribution() {
		switch (_usageType) {
		case USAGE_TYPE_ACADEMIC:
			return 0;
		case USAGE_TYPE_EMAIL:
			return 0;
		case USAGE_TYPE_NET:
			return 0;
		case USAGE_TYPE_PHOTOCOPY:
			return 0;
		case USAGE_TYPE_REPUBLICATION:
			return (int) _usageDataRepublication.getCirculation();
		default:
			return 0;
		}
	}

	public Date getContentsPublicationDate() {
		switch (_usageType) {
		case USAGE_TYPE_ACADEMIC:
			return null;
		case USAGE_TYPE_EMAIL:
			return null;
		case USAGE_TYPE_NET:
			return null;
		case USAGE_TYPE_PHOTOCOPY:
			return null;
		case USAGE_TYPE_REPUBLICATION:
			return _usageDataRepublication.getPublicationDate();
		default:
			return null;
		}
	}

	public Date getDateOfUse() {
		switch (_usageType) {
		case USAGE_TYPE_ACADEMIC:
			return null;
		case USAGE_TYPE_EMAIL:
			return _usageDataEmail.getDateOfUse();
		case USAGE_TYPE_NET:
			return _usageDataNet.getDateOfUse();
		case USAGE_TYPE_PHOTOCOPY:
			return null;
		case USAGE_TYPE_REPUBLICATION:
			return _usageDataRepublication.getRepubDate();
		default:
			return null;
		}
	}

	public String getDateOfUseStr() {
		String dateString = "";
		Date date = this.getDateOfUse();
		if (date != null) {
			if (date.getTime() > 0) {
				SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
				dateString = formatter.format(date);
			}
		}

		return dateString;
	}

	public boolean isSubmitterAuthor() {
		if (_usageType == USAGE_TYPE_REPUBLICATION) {
			if (_usageDataRepublication.getOrigAuthor() != null
					&& _usageDataRepublication.getOrigAuthor()
							.equalsIgnoreCase("Y")) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public String getSubmitterAuthor() {
		if (_usageType == USAGE_TYPE_REPUBLICATION) {
			if (_usageDataRepublication.getOrigAuthor() != null
					&& _usageDataRepublication.getOrigAuthor()
							.equalsIgnoreCase("Y")) {
				return ItemConstants.YES_CD;
			} else {
				return ItemConstants.NO_CD;
			}
		}
		return ItemConstants.NO_CD;
	}

	public String getNewPublicationTitle() {
		switch (_usageType) {
		case USAGE_TYPE_ACADEMIC:
			return null;
		case USAGE_TYPE_EMAIL:
			return null;
		case USAGE_TYPE_NET:
			return null;
		case USAGE_TYPE_PHOTOCOPY:
			return null;
		case USAGE_TYPE_REPUBLICATION:
			return _usageDataRepublication.getRepubTitle();
		default:
			return null;
		}
	}

	public int getNumberOfStudents() {
		switch (_usageType) {
		case USAGE_TYPE_ACADEMIC:
			return _usageDataAcademic.getNumStudents().intValue();
		case USAGE_TYPE_EMAIL:
			return 0;
		case USAGE_TYPE_NET:
			return 0;
		case USAGE_TYPE_PHOTOCOPY:
			return 0;
		case USAGE_TYPE_REPUBLICATION:
			return 0;
		default:
			return 0;
		}
	}

	public void setNumberOfStudents(int numberOfStudents) {
		switch (_usageType) {
		case USAGE_TYPE_ACADEMIC:
			_usageDataAcademic.setNumStudents((long) numberOfStudents);
			return;
		case USAGE_TYPE_EMAIL:
			return;
		case USAGE_TYPE_NET:
			return;
		case USAGE_TYPE_PHOTOCOPY:
			return;
		case USAGE_TYPE_REPUBLICATION:
			return;
		default:
			return;
		}
	}

	public String getPageRange() {
		switch (_usageType) {
		case USAGE_TYPE_ACADEMIC:
			return _usageDataAcademic.getPageRanges();
		case USAGE_TYPE_EMAIL:
			return "";
		case USAGE_TYPE_NET:
			return "";
		case USAGE_TYPE_PHOTOCOPY:
			return "";
		case USAGE_TYPE_REPUBLICATION:
			return _usageDataRepublication.getPageRanges();
			// _usageDataRepublication.getFirstPage() + " - " +
			// _usageDataRepublication.getLastPage();
		default:
			return "";
		}
	}

	public String getPrice() {
    	if (isSpecialOrder() && 
    		!getItemAvailabilityCd().equals(ItemAvailabilityEnum.PURCHASE.getStandardPermissionCd())) {
       		return ItemConstants.COST_TBD;
       	}
		
		return WebUtils.formatMoney(_license.getPrice());
	}

	public double getPriceValue() {
		return _license.getPrice().getValue();
	}

	public double getPriceValueRaw() {
		return _license.getPrice().getValue();
	}
	
	public String getPublicationYear() {
		return _license.getWork().getPublicationYear();
	}

	public String getPublicationYearStr() {
		return _license.getWork().getPublicationYear();
	}

	public Date getRepublicationDate() {
		switch (_usageType) {
		case USAGE_TYPE_ACADEMIC:
			return null;
		case USAGE_TYPE_EMAIL:
			return null;
		case USAGE_TYPE_NET:
			return null;
		case USAGE_TYPE_PHOTOCOPY:
			return null;
		case USAGE_TYPE_REPUBLICATION:
			return _usageDataRepublication.getRepubDate();
		default:
			return null;
		}
	}

	/**
	 * A date formatter method to control the display format of the
	 * Republication Date
	 * 
	 * @return the Republication Date formatted to the simple date format:
	 *         "MM/dd/yyyy"
	 */
	public String getRepublicationDateStr() {
		String dateString = "";

		Date date = this.getRepublicationDate();
		if (date != null) {
			if (date.getTime() > 0) {
				SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
				dateString = formatter.format(date);
			}
		}

		return dateString;
	}

	public String getRepublicationDestination() {
		switch (_usageType) {
		case USAGE_TYPE_ACADEMIC:
			return null;
		case USAGE_TYPE_EMAIL:
			return null;
		case USAGE_TYPE_NET:
			return null;
		case USAGE_TYPE_PHOTOCOPY:
			return null;
		case USAGE_TYPE_REPUBLICATION:
			return null;
		default:
			return null;
		}
	}

	public String getRepublishingOrganization() {
		if (_usageType == USAGE_TYPE_REPUBLICATION) {
			return _usageDataRepublication.getHdrRepubPub();
		}
		return null;
	}

	public String getRightsholder() {
		String rh = "";
		Right right = _license.getRight();
		if (right != null) {
			Party party = ServiceLocator.getTFService().getOrgRightsholderPartyByPartyId(
					new TFConsumerContext(), _license.getRight()
							.getRightsHolderPartyId());
			if (party != null) {
				rh = party.getOrgName();
			}
		}

		// return _license.getRight().getRightsholder().getName();
		return rh;
	}

	public String getRightQualifierTerms() {
		String termStr = "";
		Right right = _license.getRight();
		if (right != null) {
			Term term = right.getExternalCommentTerm();
			if (term != null) {
				termStr = term.getTermText();
			}
		}

		return termStr;

	}

	public String getResolutionTerms() {
		// Needed to meet interface
		return null;
	}

	public String getLicenseTerms() {
		// Needed to meet interface
		return null;
	}

	public String getRightsQualifyingStatement() {
		if (_license.getRight() != null) {
			if (_license.getRight().getRightQualifierTerm() != null) {
				return _license.getRight().getRightQualifierTerm()
						.getTermText();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public String getTranslationLanguage() {
		String translationLanguage = RepublicationConstants.NO_TRANSLATION;

		if (_usageType == USAGE_TYPE_REPUBLICATION) {
			if (StringUtils.isNotEmpty(_usageDataRepublication.getLanguage())) {
				translationLanguage = _usageDataRepublication.getLanguage();
			}
		}

		return translationLanguage;
	}

	/**
	 * Return a displayable version of the Language suitable for UI
	 * presentation.
	 * 
	 * @return language with first character capitalized.
	 */
	public String getTranslationLanguageDescription() {
		String translationLanguage = "";

		if (_usageType == USAGE_TYPE_REPUBLICATION) {
			String language = _usageDataRepublication.getLanguage();
			if (StringUtils.isNotEmpty(language)) {
				if (!RepublicationConstants.NO_TRANSLATION
						.equals(translationLanguage)) {
					translationLanguage = WebUtils.makeDisplayable(language);
				}
			}
		}

		return translationLanguage;
	}

	public String getTranslated() {
		String translated = "";

		if (_usageType == USAGE_TYPE_REPUBLICATION) {
			translated = _usageDataRepublication.getTranslated();
		}

		return translated;
	}

	public String getRepublishFullArticle() {
		String fullArticle = "";

		if (_usageType == USAGE_TYPE_REPUBLICATION) {
			fullArticle = _usageDataRepublication.getFullArticle();
		}

		return fullArticle;
	}

	public String getRepublishPoNumDtl() {
		String republishPoNumDtl = "";

		if (_usageType == USAGE_TYPE_REPUBLICATION) {
			republishPoNumDtl = _usageDataRepublication.getPoNumDtl();
		}

		return republishPoNumDtl;
	}

	/*
	 * public String getRepublishForProfit() { String republishForProfit = "";
	 * 
	 * if (_usageType == USAGE_TYPE_REPUBLICATION) { republishForProfit =
	 * _usageDataRepublication.getForProfit(); }
	 * 
	 * return republishForProfit; }
	 */
	public String getRepublishInVolEd() {
		String republishInVolEd = "";

		if (_usageType == USAGE_TYPE_REPUBLICATION) {
			republishInVolEd = _usageDataRepublication.getRepubInVolEd();
		}

		return republishInVolEd;
	}

	public void setRepublishInVolEd(String republishInVolEd) {
		// Not implemented on TF side
	}

	public String getRepublishNonTextPortion() {
		String republishNonTextPortion = "";

		if (_usageType == USAGE_TYPE_REPUBLICATION) {
			republishNonTextPortion = _usageDataRepublication
					.getNonTextPortion();
		}

		return republishNonTextPortion;
	}

	public String getRepublishSection() {
		String republishSection = "";

		if (_usageType == USAGE_TYPE_REPUBLICATION) {
			republishSection = _usageDataRepublication.getSection();
		}

		return republishSection;
	}

	public String getTypeOfContent() {
		String typeOfContent = Constants.EMPTY_STRING;

		if (_usageType == USAGE_TYPE_REPUBLICATION) {
			if (_usageDataRepublication.getFullArticle() != null
					&& _usageDataRepublication.getFullArticle()
							.equalsIgnoreCase(FULL_ARTICLE_YES)) {
				typeOfContent = RepublicationConstants.CONTENT_FULL_ARTICLE_CHAPTER;

			} else if (_usageDataRepublication.getNumExcerpts() >= CONTENT_TYPE_UNITARY_QUANTITY) {
				typeOfContent = RepublicationConstants.CONTENT_EXCERPT;

			} else if (_usageDataRepublication.getNumQuotes() >= CONTENT_TYPE_UNITARY_QUANTITY) {
				typeOfContent = RepublicationConstants.CONTENT_QUOTATION;

			} else if (_usageDataRepublication.getNumCharts() >= CONTENT_TYPE_UNITARY_QUANTITY) {
				typeOfContent = RepublicationConstants.CONTENT_CHART;

			} else if (_usageDataRepublication.getNumFigures() >= CONTENT_TYPE_UNITARY_QUANTITY) {
				typeOfContent = RepublicationConstants.CONTENT_FIGURE_DIAGRAM_TABLE;

			} else if (_usageDataRepublication.getNumPhotos() >= CONTENT_TYPE_UNITARY_QUANTITY) {
				typeOfContent = RepublicationConstants.CONTENT_PHOTOGRAPH;
			} else if (_usageDataRepublication.getNumCartoons() >= CONTENT_TYPE_UNITARY_QUANTITY) {
				typeOfContent = RepublicationConstants.CONTENT_CARTOONS;
			} else if (_usageDataRepublication.getNumIllustrations() >= CONTENT_TYPE_UNITARY_QUANTITY) {
				typeOfContent = RepublicationConstants.CONTENT_ILLUSTRATION;
			} else if (_usageDataRepublication.getNumGraphs() >= CONTENT_TYPE_UNITARY_QUANTITY) {
				typeOfContent = RepublicationConstants.CONTENT_GRAPH;

			} else if (_usageDataRepublication.getRlsPages() > 0) {
				typeOfContent = RepublicationConstants.CONTENT_SELECTED_PAGES;
			} else if (_usageDataRepublication.getNumLogos() > 0) {
				typeOfContent = RepublicationConstants.CONTENT_LOGOS;
			}

		}

		return typeOfContent;
	}

	public String getTypeOfContentDescription() {
		String typeOfContent = Constants.EMPTY_STRING;

		if (_usageType == USAGE_TYPE_REPUBLICATION) {
			if (_usageDataRepublication.getFullArticle() != null
					&& _usageDataRepublication.getFullArticle()
							.equalsIgnoreCase(FULL_ARTICLE_YES)) {
				typeOfContent = TransactionConstants.CONTENT_FULL_ARTICLE_CHAPTER;
			} else if (_usageDataRepublication.getNumExcerpts() >= CONTENT_TYPE_UNITARY_QUANTITY) {
				typeOfContent = TransactionConstants.CONTENT_EXCERPT;
			} else if (_usageDataRepublication.getNumQuotes() >= CONTENT_TYPE_UNITARY_QUANTITY) {
				typeOfContent = TransactionConstants.CONTENT_QUOTATION;
			} else if (_usageDataRepublication.getNumCharts() >= CONTENT_TYPE_UNITARY_QUANTITY) {
				typeOfContent = TransactionConstants.CONTENT_CHART;
			} else if (_usageDataRepublication.getNumFigures() >= CONTENT_TYPE_UNITARY_QUANTITY) {
				typeOfContent = TransactionConstants.CONTENT_FIGURE_DIAGRAM_TABLE;
			} else if (_usageDataRepublication.getNumPhotos() >= CONTENT_TYPE_UNITARY_QUANTITY) {
				typeOfContent = TransactionConstants.CONTENT_PHOTOGRAPH;
			} else if (_usageDataRepublication.getNumCartoons() >= CONTENT_TYPE_UNITARY_QUANTITY) {
				typeOfContent = TransactionConstants.CONTENT_CARTOONS;
			} else if (_usageDataRepublication.getNumIllustrations() >= CONTENT_TYPE_UNITARY_QUANTITY) {
				typeOfContent = TransactionConstants.CONTENT_ILLUSTRATION;
			} else if (_usageDataRepublication.getNumGraphs() >= CONTENT_TYPE_UNITARY_QUANTITY) {
				typeOfContent = TransactionConstants.CONTENT_GRAPH;
			} else if (_usageDataRepublication.getRlsPages() > 0) {
				typeOfContent = TransactionConstants.CONTENT_SELECTED_PAGES;
			}

		}

		return typeOfContent;
	}

	public String getTypeOfUseDescription() {
		return _license.getTypeOfUseDescription();
	}

	public boolean isSpecialOrder() {
		return _license.isSpecialOrder();
	}

	public boolean isUnresolvedSpecialOrder() {
		return _license.isSpecialOrder();
	}
	
	public void setSpecialOrderTypeCd(String specialOrderTypeCd) {
		// Nothing to do here, meeting transactionItem interface
	}

	public String getSpecialOrderTypeCd() {
		if (!getLicense().isSpecialOrder()) {
			return SpecialOrderTypeEnum.NOT_SPECIAL_ORDER.name();
		} else if (getLicense().isContactRHPermission()) {
			return SpecialOrderTypeEnum.CONTACT_RIGHTSHOLDER_SPECIAL_ORDER.name();
		} else if (getLicense().isManualSpecialOrder()) {
			return SpecialOrderTypeEnum.RIGHT_NOT_FOUND_SPECIAL_ORDER.name();			
		} else if (getLicense().isSpecialOrderResearch()) {
			return SpecialOrderTypeEnum.RESEARCH_FURTHER_SPECIAL_ORDER.name();						
		} else {
			return SpecialOrderTypeEnum.RESEARCH_FURTHER_SPECIAL_ORDER.name();						
		}
	}

	public void setAuthor(String author) {
		_license.getWork().setMainAuthor(author);
	}

	public void setBusiness(String business) {
		if (_usageType == USAGE_TYPE_REPUBLICATION) {
			if (business.equals(RepublicationConstants.BUSINESS_FOR_PROFIT))
				_usageDataRepublication
						.setForProfit(RepublicationConstants.FOR_PROFIT);
			else
				_usageDataRepublication
						.setForProfit(RepublicationConstants.NON_FOR_PROFIT);
		}
	}

	public void setCirculationDistribution(int circulationDistribution) {

		if (_usageType == USAGE_TYPE_REPUBLICATION) {
			_usageDataRepublication.setCirculation(circulationDistribution);
		}
	}

	public void setContentsPublicationDate(Date contentsPublicationDate) {
		if (_usageType == USAGE_TYPE_REPUBLICATION) {
			_usageDataRepublication.setPublicationDate(contentsPublicationDate);
		}
	}

	public void setDateOfUse(Date dateOfUse) {
		switch (_usageType) {
		case USAGE_TYPE_ACADEMIC:
			return;
		case USAGE_TYPE_EMAIL:
			_usageDataEmail.setDateOfUse(dateOfUse);
			return;
		case USAGE_TYPE_NET:
			_usageDataNet.setDateOfUse(dateOfUse);
			return;
		case USAGE_TYPE_PHOTOCOPY:
			return;
		case USAGE_TYPE_REPUBLICATION:
			return;
		default:
			return;
		}
	}

	public void setEdition(String edition) {
		_license.getWork().setEdition(edition);
	}

	public void setEditor(String editor) {
		_license.getWork().setMainEditor(editor);
	}

	public void setSubmitterAuthor(boolean isSubmitterAuthor) {
		if (_usageType == USAGE_TYPE_REPUBLICATION) {
			if (isSubmitterAuthor == true) {
				_usageDataRepublication
						.setOrigAuthor(RepublicationConstants.SUBMITTER_IS_AUTHOR);
			} else {
				_usageDataRepublication
						.setOrigAuthor(RepublicationConstants.SUBMITTER_IS_NOT_AUTHOR);
			}
		}
	}

	public void setNewPublicationTitle(String newPublicationTitle) {
		if (_usageType == USAGE_TYPE_REPUBLICATION) {
			_usageDataRepublication.setRepubTitle(StringUtils
					.upperCase(newPublicationTitle));
		}

	}

	public void setPageRange(String pageRange) {
		switch (_usageType) {
		case USAGE_TYPE_ACADEMIC:
			if (_usageDataAcademic.getPageRanges() == null ||
				pageRange == null || 
				!pageRange.equals(_usageDataAcademic.getPageRanges())) {
					setRequiresAcademicRepin(true);	
			}
			_usageDataAcademic.setPageRanges(pageRange);
			return;
		case USAGE_TYPE_EMAIL:
			return;
		case USAGE_TYPE_NET:
			return;
		case USAGE_TYPE_PHOTOCOPY:
			return;
		case USAGE_TYPE_REPUBLICATION:
			_usageDataRepublication.setPageRanges(pageRange);
			return;
		default:
			return;
		}
	}

	public void setPermissionSelected(String permissionSelected) {
	}

	public void setPublicationDateOfUse(Date publicationDateOfUse) {
	}

	public void setPublicationTitle(String publicationTitle) {
		_license.getWork().setMainTitle(publicationTitle);
	}

	public void setPublicationYear(String publicationYear) {
	}

	public void setPublisher(String publisher) {
		_license.getWork().setPublisher(publisher);
	}

	public void setRepublicationDate(Date republicationDate) {
		if (_usageType == USAGE_TYPE_REPUBLICATION) {
			_usageDataRepublication.setRepubDate(republicationDate);
		}

	}

	public void setRepublicationDestination(String republicationDestination) {
	}

	public void setRepublishingOrganization(String republishingOrganization) {
		if (_usageType == USAGE_TYPE_REPUBLICATION) {
			_usageDataRepublication.setHdrRepubPub(StringUtils
					.upperCase(republishingOrganization));
		}
	}

	public void setStandardNumber(String standardNumber) {
		_license.getWork().setStandardNumber(standardNumber);
	}

	public void setTranslationLanguage(String translationLanguage) {

		if (_usageType == USAGE_TYPE_REPUBLICATION) {

			if (translationLanguage
					.equals(RepublicationConstants.NO_TRANSLATION)) {
				_usageDataRepublication.setLanguage(Constants.EMPTY_STRING);
				_usageDataRepublication
						.setTranslated(RepublicationConstants.NOT_TRANSLATED);
			} else {
				_usageDataRepublication.setLanguage(translationLanguage);
				_usageDataRepublication
						.setTranslated(RepublicationConstants.TRANSLATED);
			}

		} else {
			throw new UnsupportedOperationException();
		}

	}

	public void setTypeOfContent(String typeOfContent) {

		if (_usageType == USAGE_TYPE_REPUBLICATION) {
			_usageDataRepublication.setFullArticle(FULL_ARTICLE_NO);
			_usageDataRepublication.setNumExcerpts(CONTENT_TYPE_ZERO_QUANTITY);
			_usageDataRepublication.setNumQuotes(CONTENT_TYPE_ZERO_QUANTITY);
			_usageDataRepublication.setNumCharts(CONTENT_TYPE_ZERO_QUANTITY);
			_usageDataRepublication.setNumGraphs(CONTENT_TYPE_ZERO_QUANTITY);
			_usageDataRepublication.setNumFigures(CONTENT_TYPE_ZERO_QUANTITY);
			_usageDataRepublication.setNumPhotos(CONTENT_TYPE_ZERO_QUANTITY);
			_usageDataRepublication
					.setNumIllustrations(CONTENT_TYPE_ZERO_QUANTITY);
			_usageDataRepublication.setNumCartoons(CONTENT_TYPE_ZERO_QUANTITY);
			_usageDataRepublication.setNumLogos(CONTENT_TYPE_ZERO_QUANTITY);

			if (!typeOfContent
					.equals(RepublicationConstants.CONTENT_SELECTED_PAGES)) {
				_usageDataRepublication.setRlsPages(CONTENT_TYPE_ZERO_QUANTITY);
			}

			if (typeOfContent
					.equals(RepublicationConstants.CONTENT_FULL_ARTICLE_CHAPTER)) {
				_usageDataRepublication.setFullArticle(FULL_ARTICLE_YES);
			} else if (typeOfContent
					.equals(RepublicationConstants.CONTENT_EXCERPT)) {
				_usageDataRepublication
						.setNumExcerpts(CONTENT_TYPE_UNITARY_QUANTITY);

			} else if (typeOfContent
					.equals(RepublicationConstants.CONTENT_QUOTATION)) {
				_usageDataRepublication
						.setNumQuotes(CONTENT_TYPE_UNITARY_QUANTITY);

			} else if (typeOfContent
					.equals(RepublicationConstants.CONTENT_CHART)) {
				_usageDataRepublication
						.setNumCharts(CONTENT_TYPE_UNITARY_QUANTITY);

			} else if (typeOfContent
					.equals(RepublicationConstants.CONTENT_GRAPH)) {
				_usageDataRepublication
						.setNumGraphs(CONTENT_TYPE_UNITARY_QUANTITY);

			} else if (typeOfContent
					.equals(RepublicationConstants.CONTENT_FIGURE_DIAGRAM_TABLE)) {
				_usageDataRepublication
						.setNumFigures(CONTENT_TYPE_UNITARY_QUANTITY);

			} else if (typeOfContent
					.equals(RepublicationConstants.CONTENT_PHOTOGRAPH)) {
				_usageDataRepublication
						.setNumPhotos(CONTENT_TYPE_UNITARY_QUANTITY);

			} else if (typeOfContent
					.equals(RepublicationConstants.CONTENT_CARTOONS)) {
				_usageDataRepublication
						.setNumCartoons(CONTENT_TYPE_UNITARY_QUANTITY);
			} else if (typeOfContent
					.equals(RepublicationConstants.CONTENT_ILLUSTRATION)) {
				_usageDataRepublication
						.setNumIllustrations(CONTENT_TYPE_UNITARY_QUANTITY);

			} else if (typeOfContent
					.equals(RepublicationConstants.CONTENT_SELECTED_PAGES)) {
				// do nothing
			} else {
				throw new IllegalArgumentException("Invalid content type.");
			}
		}

	}

	public void setVolume(String volume) {
		_license.getWork().setVolume(volume);
	}

	public boolean isDigital() {
		return isNet() || isEmail();
	}

	public boolean isRightsLink() {
		boolean isRightsLink = false;

		return isRightsLink;

	}

	public boolean isReprint() {

		return false;
	}

	public boolean isPricedReprint() {

		return false;
	}

	
	public boolean isAPS() {
		if (_license.getUsageData().getProduct() == OrderLicenseImpl.APS_PRODUCT_CODE) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isECCS() {
		if (_license.getUsageData().getProduct() == OrderLicenseImpl.ECCS_PRODUCT_CODE) {
			return true;
		} else {
			return false;
		}
	}

	public String getDateOfIssue() {
		switch (_usageType) {
		case USAGE_TYPE_ACADEMIC:
			return _usageDataAcademic.getDateOfIssue();
		case USAGE_TYPE_EMAIL:
			return null;
		case USAGE_TYPE_NET:
			return null;
		case USAGE_TYPE_PHOTOCOPY:
			return null;
		case USAGE_TYPE_REPUBLICATION:
			return null;
		default:
			return null;
		}
	}

	public void setDateOfIssue(String dateOfIssue) {
		switch (_usageType) {
		case USAGE_TYPE_ACADEMIC:
			_usageDataAcademic.setDateOfIssue(dateOfIssue);
			return;
		case USAGE_TYPE_EMAIL:
			return;
		case USAGE_TYPE_NET:
			return;
		case USAGE_TYPE_PHOTOCOPY:
			return;
		case USAGE_TYPE_REPUBLICATION:
			return;
		default:
			return;
		}
	}

	public Long getWorkInst() {
		
		if (_license.getWork().getWrkInst() <= 0) {
			return null;
		}
		
		return new Long(_license.getWork().getWrkInst());
	}

	public void setWorkInst(Long wrkInst) {
		if (wrkInst != null) {
			_license.getWork().setWrkInst(wrkInst.longValue());
		} else {
			_license.getWork().setWrkInst(0L);			
		}
	}

	public Long getExternalItemId() {
		
		if (_license.getWork().getWrkInst() <= 0) {
			return null;
		}
		
		return Long.valueOf(_license.getWork().getWrkInst());
		// return getPermissionRequest().getWork().getWrkInst();
	}

	public void setExternalItemId(Long wrkInst) {
		
		if (wrkInst != null) {
			_license.getWork().setWrkInst(wrkInst.longValue());
		} else {
			_license.getWork().setWrkInst(0L);			
		}

	}

	public boolean isSpecialOrderFromScratch() {
		return _license.isManualSpecialOrder();
	}

	public String getDurationString() {
		switch (_usageType) {
		case USAGE_TYPE_ACADEMIC:
			return null;
		case USAGE_TYPE_EMAIL:
			return null;
		case USAGE_TYPE_NET:
			return _usageDataNet.getDurationString();
		case USAGE_TYPE_PHOTOCOPY:
			return null;
		case USAGE_TYPE_REPUBLICATION:
			return null;
		default:
			return null;
		}
	}

	public Date getPublicationStartDate() {
		if (_license.getRight() != null) {
			return _license.getRight().getPublicationBeginDate();
		} else {
			return null;
		}

	}

	// TODO fully implement this method
	public Date getPublicationEndDate() {
		if (_license.getRight() != null) {
			return _license.getRight().getPublicationEndDate();
		} else {
			return null;
		}
	}

	public boolean isContactRightsholder() {
		return _license.isContactRHPermission();
	}

	public boolean isManualSpecialOrder() {
		return _license.isManualSpecialOrder();
	}

	public String getCustomAuthor() {
		switch (_usageType) {
		case USAGE_TYPE_ACADEMIC:
			return _usageDataAcademic.getAuthor();
		case USAGE_TYPE_EMAIL:
			return _usageDataEmail.getAuthor();
		case USAGE_TYPE_NET:
			return _usageDataNet.getAuthor();
		case USAGE_TYPE_PHOTOCOPY:
			return _usageDataPhotocopy.getAuthor();
		case USAGE_TYPE_REPUBLICATION:
			return _usageDataRepublication.getAuthor();
		default:
			return "";
		}
	}

	public void setCustomAuthor(String customAuthor) {
		switch (_usageType) {
		case USAGE_TYPE_ACADEMIC:
			_usageDataAcademic.setAuthor(customAuthor);
			return;
		case USAGE_TYPE_EMAIL:
			_usageDataEmail.setAuthor(customAuthor);
			return;
		case USAGE_TYPE_NET:
			_usageDataNet.setAuthor(customAuthor);
			return;
		case USAGE_TYPE_PHOTOCOPY:
			_usageDataPhotocopy.setAuthor(customAuthor);
			return;
		case USAGE_TYPE_REPUBLICATION:
			_usageDataRepublication.setAuthor(customAuthor);
			return;
		default:
			return;
		}
	}

	public String getYourReference() {
		switch (_usageType) {
		case USAGE_TYPE_ACADEMIC:
			return _license.getCustomerRef();
		case USAGE_TYPE_EMAIL:
			return _license.getCustomerRef();
		case USAGE_TYPE_NET:
			return _license.getCustomerRef();
		case USAGE_TYPE_PHOTOCOPY:
			return _license.getCustomerRef();
		case USAGE_TYPE_REPUBLICATION:
			return _usageDataRepublication.getLcnHdrRefNum();
		default:
			return "";
		}

	}

	public String getHasVolPriceTiers() {

		if (_license.getRight() != null)
		{
			return _license.getRight().getHasVolPriceTiers();
		}
		else
		{
			return "N";
		}
	}

	public void setYourReference(String yourReference) {
		if (_usageType == USAGE_TYPE_REPUBLICATION) {
			_usageDataRepublication.setLcnHdrRefNum(StringUtils
					.upperCase(yourReference));
		}
	}

	public String getRepublicationTypeOfUse() {

		if (this.isRepublication()) {
			String republicationTypeOfUse = Constants.EMPTY_STRING;
			long tpuInst = _license.getUsageData().getTpuInst();

			if (tpuInst == REPUBLICATION_ADVERTISEMENT_TPU_CODE) {
				republicationTypeOfUse = RepublicationConstants.REPUBLICATION_ADVERTISEMENT;
			}

			if (tpuInst == REPUBLICATION_BROCHURE_TPU_CODE) {
				republicationTypeOfUse = RepublicationConstants.REPUBLICATION_BROCHURE;
			}

			if (tpuInst == REPUBLICATION_CDROM_TPU_CODE) {
				republicationTypeOfUse = RepublicationConstants.REPUBLICATION_CDROM;
			}

			if (tpuInst == REPUBLICATION_DISSERTATION_TPU_CODE) {
				republicationTypeOfUse = RepublicationConstants.REPUBLICATION_DISSERTATION;
			}

			if (tpuInst == REPUBLICATION_DVD_TPU_CODE) {
				republicationTypeOfUse = RepublicationConstants.REPUBLICATION_DVD;
			}

			if (tpuInst == REPUBLICATION_EMAIL_TPU_CODE) {
				republicationTypeOfUse = RepublicationConstants.REPUBLICATION_EMAIL;
			}

			if (tpuInst == REPUBLICATION_FRAMING_TPU_CODE) {
				republicationTypeOfUse = RepublicationConstants.REPUBLICATION_FRAMING;
			}

			if (tpuInst == REPUBLICATION_INTERNET_TPU_CODE) {
				republicationTypeOfUse = RepublicationConstants.REPUBLICATION_INTERNET;
			}

			if (tpuInst == REPUBLICATION_INTRANET_TPU_CODE) {
				republicationTypeOfUse = RepublicationConstants.REPUBLICATION_INTRANET;
			}

			if (tpuInst == REPUBLICATION_JOURNAL_TPU_CODE) {
				republicationTypeOfUse = RepublicationConstants.REPUBLICATION_JOURNAL;
			}

			if (tpuInst == REPUBLICATION_LINKING_TPU_CODE) {
				republicationTypeOfUse = RepublicationConstants.REPUBLICATION_LINKING;
			}

			if (tpuInst == REPUBLICATION_MAGAZINE_TPU_CODE) {
				republicationTypeOfUse = RepublicationConstants.REPUBLICATION_MAGAZINE;
			}

			if (tpuInst == REPUBLICATION_NEWSLETTER_TPU_CODE) {
				republicationTypeOfUse = RepublicationConstants.REPUBLICATION_NEWSLETTER;
			}

			if (tpuInst == REPUBLICATION_NEWSPAPER_TPU_CODE) {
				republicationTypeOfUse = RepublicationConstants.REPUBLICATION_NEWSPAPER;
			}

			if (tpuInst == REPUBLICATION_OTHERBOOK_TPU_CODE) {
				republicationTypeOfUse = RepublicationConstants.REPUBLICATION_OTHERBOOK;
			}

			if (tpuInst == REPUBLICATION_PAMPHLET_TPU_CODE) {
				republicationTypeOfUse = RepublicationConstants.REPUBLICATION_PAMPHLET;
			}

			if (tpuInst == REPUBLICATION_PRESENTATION_TPU_CODE) {
				republicationTypeOfUse = RepublicationConstants.REPUBLICATION_PRESENTATION;
			}

			if (tpuInst == REPUBLICATION_TEXTBOOK_TPU_CODE) {
				republicationTypeOfUse = RepublicationConstants.REPUBLICATION_TEXTBOOK;
			}

			if (tpuInst == REPUBLICATION_TRADEBOOK_TPU_CODE) {
				republicationTypeOfUse = RepublicationConstants.REPUBLICATION_TRADEBOOK;
			}

			return republicationTypeOfUse;
		} else {
			return null;
		}

	}

	public Long getJobTicketNumber() {
		return null;
	}

	public void setJobTicketNumber(Long jobTicket) {
		// Only needed to meet interface
	}

	public Long getExternalDetailId() {
		return this.getID();
	}

	public void setExternalDetailId(Long detailId) {
		// Only needed to meet interface
	}

	// Getters for Adjustments
	public String getLicenseStatusCd() {
		return _license.getOdtStatusCd();
	}

	public String getLicenseStatusCdMsg() {
		return _license.getOdtStatusCdMsg();
	}

	public double getRoyalty() {
		return _license.getRoyaltyPayable();
	}

	public String getDistributionEvent() {
		
		if (getLicense().getEvtInst() > 0) {
			return String.valueOf( getLicense().getEvtInst());
		}
		return null;
	}

	public double getRightsholderFee() {
		return _license.getRightsholderFee();
	}

	public BigDecimal getTotalRightsholderFeeValue() {
		return new BigDecimal(_license.getRightsholderFee()).setScale(2,
				BigDecimal.ROUND_HALF_EVEN);
	}

	public double getLicenseeFee() {
		return _license.getLicenseeFee();
	}

	public BigDecimal getTotalLicenseeFeeValue() {
		return new BigDecimal(_license.getLicenseeFee()).setScale(2,
				BigDecimal.ROUND_HALF_EVEN);
	}

	public double getDiscount() {
		return _license.getDiscount();
	}

	public BigDecimal getTotalDiscountValue() {
		return new BigDecimal(_license.getDiscount()).setScale(2,
				BigDecimal.ROUND_HALF_EVEN);
	}

	public long getReasonCd() {
		return _license.getReasonCd();
	}

	public String getReasonDesc() {
		return _license.getReasonDesc();
	}

	public long getLicenseDetailReferenceID() {
		return _license.getOrderDetailReferenceID();
	}

	public long getNumberOfCartoons() {

		long numberOfCartoons = -1;

		if (isRepublication()) {
			UsageDataRepublication usageDataRepublication = (UsageDataRepublication) _license
					.getUsageData();

			numberOfCartoons = usageDataRepublication.getNumCartoons();
		}

		return numberOfCartoons;
	}

	public void setNumberOfCartoons(long numberOfCartoons) {
		if (isRepublication()) {
			UsageDataRepublication usageDataRepublication = (UsageDataRepublication) _license
					.getUsageData();

			usageDataRepublication.setNumCartoons(numberOfCartoons);
		}
	}

	public long getNumberOfCharts() {
		long numberOfCharts = -1;

		if (isRepublication()) {
			UsageDataRepublication usageDataRepublication = (UsageDataRepublication) _license
					.getUsageData();

			numberOfCharts = usageDataRepublication.getNumCharts();
		}

		return numberOfCharts;
	}

	public void setNumberOfCharts(long numberOfCharts) {
		if (isRepublication()) {
			UsageDataRepublication usageDataRepublication = (UsageDataRepublication) _license
					.getUsageData();

			usageDataRepublication.setNumCharts(numberOfCharts);
		}
	}

	public long getNumberOfExcerpts() {
		long numberOfExcerpts = -1;

		if (isRepublication()) {
			UsageDataRepublication usageDataRepublication = (UsageDataRepublication) _license
					.getUsageData();

			numberOfExcerpts = usageDataRepublication.getNumExcerpts();
		}

		return numberOfExcerpts;
	}

	public void setNumberOfExcerpts(long numberOfExcerpts) {
		if (isRepublication()) {
			UsageDataRepublication usageDataRepublication = (UsageDataRepublication) _license
					.getUsageData();

			usageDataRepublication.setNumExcerpts(numberOfExcerpts);
		}
	}

	public long getNumberOfFigures() {
		long numberOfFigures = -1;

		if (isRepublication()) {
			UsageDataRepublication usageDataRepublication = (UsageDataRepublication) _license
					.getUsageData();

			numberOfFigures = usageDataRepublication.getNumFigures();
		}

		return numberOfFigures;
	}

	public void setNumberOfFigures(long numberOfFigures) {
		if (isRepublication()) {
			UsageDataRepublication usageDataRepublication = (UsageDataRepublication) _license
					.getUsageData();

			usageDataRepublication.setNumFigures(numberOfFigures);
		}
	}

	public long getNumberOfGraphs() {
		long numberOfGraphs = -1;

		if (isRepublication()) {
			UsageDataRepublication usageDataRepublication = (UsageDataRepublication) _license
					.getUsageData();

			numberOfGraphs = usageDataRepublication.getNumGraphs();
		}

		return numberOfGraphs;
	}

	public void setNumberOfGraphs(long numberOfGraphs) {
		if (isRepublication()) {
			UsageDataRepublication usageDataRepublication = (UsageDataRepublication) _license
					.getUsageData();

			usageDataRepublication.setNumGraphs(numberOfGraphs);
		}
	}

	public long getNumberOfIllustrations() {
		long numberOfIllustrations = -1;

		if (isRepublication()) {
			UsageDataRepublication usageDataRepublication = (UsageDataRepublication) _license
					.getUsageData();

			numberOfIllustrations = usageDataRepublication
					.getNumIllustrations();
		}

		return numberOfIllustrations;
	}

	public void setNumberOfIllustrations(long numberOfIllustrations) {
		if (isRepublication()) {
			UsageDataRepublication usageDataRepublication = (UsageDataRepublication) _license
					.getUsageData();

			usageDataRepublication.setNumIllustrations(numberOfIllustrations);
		}
	}

	public long getNumberOfLogos() {
		long numberOfLogos = -1;

		if (isRepublication()) {
			UsageDataRepublication usageDataRepublication = (UsageDataRepublication) _license
					.getUsageData();

			numberOfLogos = usageDataRepublication.getNumLogos();
		}

		return numberOfLogos;
	}

	public void setNumberOfLogos(long numberOfLogos) {
		if (isRepublication()) {
			UsageDataRepublication usageDataRepublication = (UsageDataRepublication) _license
					.getUsageData();

			usageDataRepublication.setNumLogos(numberOfLogos);
		}
	}

	public long getNumberOfPhotos() {
		long numberOfPhotos = -1;

		if (isRepublication()) {
			UsageDataRepublication usageDataRepublication = (UsageDataRepublication) _license
					.getUsageData();

			numberOfPhotos = usageDataRepublication.getNumPhotos();
		}

		return numberOfPhotos;
	}

	public void setNumberOfPhotos(long numberOfPhotos) {
		if (isRepublication()) {
			UsageDataRepublication usageDataRepublication = (UsageDataRepublication) _license
					.getUsageData();

			usageDataRepublication.setNumPhotos(numberOfPhotos);
		}
	}

	public long getNumberOfQuotes() {
		long numberOfQuotes = -1;

		if (isRepublication()) {
			UsageDataRepublication usageDataRepublication = (UsageDataRepublication) _license
					.getUsageData();

			numberOfQuotes = usageDataRepublication.getNumQuotes();
		}

		return numberOfQuotes;
	}

	public void setNumberOfQuotes(long numberOfQuotes) {
		if (isRepublication()) {
			UsageDataRepublication usageDataRepublication = (UsageDataRepublication) _license
					.getUsageData();

			usageDataRepublication.setNumQuotes(numberOfQuotes);
		}
	}

	public void setLicenseeFee(double licenseeFee) {
		_license.setLicenseeFee(licenseeFee);
	}

	public void setDiscount(double discount) {
		_license.setDiscount(discount);
	}

	public void setRoyalty(double royalty) {
		_license.setRoyaltyPayable(royalty);
	}

	public void setReasonCd(long reasonCd) {
		_license.setReasonCd(reasonCd);
	}

	public void setReasonDesc(String reasonDesc) {
		_license.setReasonDesc(reasonDesc);
	}

	public double getRoyaltyComposite() {
		return getRoyalty() + getRightsholderFee();
	}

	public BigDecimal getRoyaltyCompositeValue() {
		return new BigDecimal(this.getRoyaltyComposite()).setScale(2,
				BigDecimal.ROUND_HALF_EVEN);
	}

	public void setSpecialOrderLimitsExceeded(boolean specialOrderLimitsExceeded) {
		_license.setSpecialOrderLimitsExceeded(specialOrderLimitsExceeded);
	}

	public String getBaseFee() {
		String baseFee = null;

		boolean feesPresent = getFees() != null;

		if (feesPresent) {
			Money baseFeeMoney = getFees().getBaseFee();
			if (baseFeeMoney != null)
				baseFee = WebUtils.formatMoney(baseFeeMoney, false);
		}

		return baseFee;
	}

	public String getFlatFee() {
		String flatFee = null;

		boolean feesPresent = getFees() != null;

		if (feesPresent) {
			Money flatFeeMoney = getFees().getFlatFee();
			if (flatFeeMoney != null)
				flatFee = WebUtils.formatMoney(flatFeeMoney, false);
		}

		return flatFee;
	}

	public String getPerPageFee() {
		String perPageFee = null;

		boolean feesPresent = getFees() != null;

		if (feesPresent) {
			Money perPageFeeMoney = getFees().getPerPageFee();
			if (perPageFeeMoney != null)
				perPageFee = WebUtils.formatMoney(perPageFeeMoney, false);
		}

		return perPageFee;
	}

	public String getPerPageFeeMoneyFormat() {
		return getPerPageFee();
	}

	public BigDecimal getPerPageFeeValue() {
		if (getFees() != null) {
			Money perPageFee = getFees().getPerPageFee();
			if (perPageFee != null) {
				return new BigDecimal(perPageFee.getValue());
			}
		}

		return null;
	}
	
	public String getEntireBookFee() {
		String entireBookFee = null;

		boolean feesPresent = getFees() != null;

		if (feesPresent) {
			Money entireBookFeeMoney = getFees().getEntireBookFee();
			if (entireBookFeeMoney != null)
				entireBookFee = WebUtils.formatMoney(entireBookFeeMoney, false);
		}

		return entireBookFee;
	}
	
	
	public String getEntireBookFeeMoneyFormat()
	  {
		return getEntireBookFee();
		
	  }
	  
	  public BigDecimal getEntireBookFeeValue()
	  {
		  if (getFees() != null) {
				Money entireBookFee = getFees().getEntireBookFee();
				if (entireBookFee != null) {
					return new BigDecimal(entireBookFee.getValue());
				}
			}

			return null;
	  }
	  
	  public BigDecimal getEntireBookFeeOverrideValue() {
			return _entireBookFeeOverrideValue;
		}

	  public void setEntireBookFeeOverrideValue(BigDecimal entireBookFeeOverrideValue) {
			this._entireBookFeeOverrideValue = entireBookFeeOverrideValue;
		}	  
	  
	public void setPerPageFeeOverrideValue(BigDecimal perPageFeeOverrideValue) {
		this._perPageFeeOverrideValue = perPageFeeOverrideValue;
	}

	public BigDecimal getPerPageFeeOverrideValue() {
		return _perPageFeeOverrideValue;
	}	
	
	public String getBaseFeeMoneyFormat() {
		return getBaseFee();
	}

	public BigDecimal getBaseFeeValue() {
		if (getFees() != null) {
			Money baseFee = getFees().getBaseFee();
			if (baseFee != null) {
				return new BigDecimal(baseFee.getValue());
			}
		}

		return null;
	}

	public void setBaseFeeOverrideValue(BigDecimal baseFeeOverrideValue) {
		this._baseFeeOverrideValue = baseFeeOverrideValue;
	}

	public BigDecimal getBaseFeeOverrideValue() {
		return _baseFeeOverrideValue;
	}

	public String getFlatFeeMoneyFormat() {
		return getFlatFee();
	}

	public BigDecimal getFlatFeeValue() {
		if (getFees() != null) {
			Money flatFee = getFees().getFlatFee();
			if (flatFee != null) {
				return new BigDecimal(flatFee.getValue());
			}
		}

		return null;
	}

	public void setFlatFeeOverrideValue(BigDecimal flatFeeOverrideValue) {
		this._flatFeeOverrideValue = flatFeeOverrideValue;
	}

	public BigDecimal getFlatFeeOverrideValue() {
		return _flatFeeOverrideValue;
	}

	public void setMaximumRoyaltyFeeOverrideValue(BigDecimal maximumRoyaltyFeeOverrideValue) {
		this._maximumRoyaltyFeeOverrideValue = maximumRoyaltyFeeOverrideValue;
	}

	public BigDecimal getMaximumRoyaltyFeeOverrideValue() {
		return _maximumRoyaltyFeeOverrideValue;
	}

	public void setDurationFeeOverrideValue(BigDecimal durationFeeOverrideValue) {
		this._durationFeeOverrideValue = durationFeeOverrideValue;
	}

	public BigDecimal getDurationFeeOverrideValue() {
		return _durationFeeOverrideValue;
	}
	
	public void setRecipientsFeeOverrideValue(BigDecimal recipientsFeeOverrideValue) {
		this._recipientsFeeOverrideValue = recipientsFeeOverrideValue;
	}

	public BigDecimal getRecipientsFeeOverrideValue() {
		return _recipientsFeeOverrideValue;
	}
	
	public void setPerContentFeeOverrideValue(BigDecimal perContentFeeOverrideValue) {
		this._perContentFeeOverrideValue = perContentFeeOverrideValue;
	}

	public BigDecimal getPerContentFeeOverrideValue() {
		return _perContentFeeOverrideValue;
	}
	
	public String getCustomVolume() {
		String customVolume = null;

		boolean isOperationAllowed = isAcademic() || isPhotocopy()
				|| isRepublication();

		if (isOperationAllowed) {

			if (isAcademic()) {
				customVolume = ((UsageDataAcademic) getLicense().getUsageData())
						.getVolume();
			}

			if (isPhotocopy()) {
				customVolume = ((UsageDataPhotocopy) getLicense()
						.getUsageData()).getVolume();
			}

			if (isRepublication()) {
				customVolume = ((UsageDataRepublication) getLicense()
						.getUsageData()).getVolume();
			}

//		} else {
//			throw new UnsupportedOperationException();
		}

		return customVolume;
	}

	public void setCustomVolume(String customVolume) {
		boolean isOperationAllowed = isAcademic() || isPhotocopy()
				|| isRepublication();

		if (isOperationAllowed) {

			if (isAcademic()) {
				((UsageDataAcademic) getLicense().getUsageData())
						.setVolume(customVolume);
			}

			if (isPhotocopy()) {
				((UsageDataPhotocopy) getLicense().getUsageData())
						.setVolume(customVolume);
			}

			if (isRepublication()) {
				((UsageDataRepublication) getLicense().getUsageData())
						.setVolume(customVolume);
			}

//		} else {
//			throw new UnsupportedOperationException();
		}
	}

	public String getCustomEdition() {
		String customEdition = null;

		boolean isOperationAllowed = isAcademic() || isPhotocopy();

		if (isOperationAllowed) {
			if (isAcademic()) {
				customEdition = ((UsageDataAcademic) getLicense()
						.getUsageData()).getEdition();
			}

			if (isPhotocopy()) {
				customEdition = ((UsageDataPhotocopy) getLicense()
						.getUsageData()).getEdition();
			}
//		} else {
//			throw new UnsupportedOperationException();
		}

		return customEdition;
	}

	public void setCustomEdition(String customEdition) {
		boolean isOperationAllowed = isAcademic() || isPhotocopy();

		if (isOperationAllowed) {
			if (isAcademic()) {
				((UsageDataAcademic) getLicense().getUsageData())
						.setEdition(customEdition);
			}

			if (isPhotocopy()) {
				((UsageDataPhotocopy) getLicense().getUsageData())
						.setEdition(customEdition);
			}
//		} else {
//			throw new UnsupportedOperationException();
		}
	}

	private void setFees(RightFees fees) {
		this._fees = fees;
	}

	private RightFees getFees() {
		return _fees;
	}

	public boolean isSpecialOrderLimitsExceeded() {
		return _license.getSpecialOrderLimitsExceeded();
	}

	  public long getRightsholderInst()
	  {
	    long rightsholderInst = INVALID_RIGHTSHOLDER_INST;
	    
	    boolean rightPresent = getLicense().getRight() != null;
	    
	    if( rightPresent )
	    {
	      rightsholderInst = getLicense().getRight().getRightsHolderPartyId();
	    }
	    
	    return rightsholderInst;
	  }
	  
	  public Long getRightsholderPartyId()
	  {
	    Long rightsholderPartyId = null;
	        
	    boolean rightPresent = getLicense().getRight() != null;
	    
	    if( rightPresent )
	    {
	      rightsholderPartyId = Long.valueOf(getLicense().getRight().getRightsHolderPartyId());
	    }
	    
	    return rightsholderPartyId;
	  }
	  
	  public Long getRightsholderPtyInst()
	  {
	    Long rightsholderInst = null;
	    
	    boolean rightPresent = getLicense().getRight() != null;
	    
	    if( rightPresent )
	    {
	      rightsholderInst = Long.valueOf(getLicense().getRight().getRightsHolderInst());
	    }
	    
	    return rightsholderInst;
	  }

	public String getRightPermissionType() {
		boolean rightPresent = getLicense().getRight() != null;
		String rightPermType = null;

		if (rightPresent) {
			rightPermType = getLicense().getRight().getPermission()
					.getPermissionValueCode();
		}

		return rightPermType;
	}

	public Right getSvcRight() {
		return null;
	}

	public Long getWrWorkInst() {
		// if this is just a Standard Work object, then we need to create a
		// WrStandardWork object and replace this in
		// the permissionRequest object
		if (PurchasablePermissionFactory.getWRWorkInst(getWorkInst()) <= 0) {
			return null;
		}
		
		return new Long(PurchasablePermissionFactory.getWRWorkInst(getWorkInst()));
	}

	// 2009-10-23 MSJ
	// Yeah, my fingers are everywhere. More changes for summit,
	// metadata being passed along from search to the order process.
	// Hoping the folks working on orders will roll the new metadata
	// into this code in a better way, somehow.

	public String getIdnoLabel() {
		try {
			if (wrWork == null) {
				wrWork = PurchasablePermissionFactory.getWRWork(getWorkInst());
			}
			return ApplicationResources.getInstance().getIdnoLabel(
					wrWork.getIdnoTypeCd().toString());
		} catch (Exception e) {
			return "ISBN/ISSN";
		}
	}

	public String getIdnoTypeCd() {
		try {
			if (wrWork == null) {
				wrWork = PurchasablePermissionFactory.getWRWork(getWorkInst());
			}
			return wrWork.getIdnoTypeCd().toString();
		} catch (Exception e) {
			return null;
		}
	}

	
	public void setIdnoLabel(String idnoLabel) {
		// Fit interface, can not update in TF
	}

	public String getPages() {
		try {
			if (wrWork == null) {
				wrWork = PurchasablePermissionFactory.getWRWork(getWorkInst());
			}
			return wrWork.getPages().toString();
		} catch (Exception e) {
			return null;
		}
	}

	public String getPublicationType() {
		try {
			if (wrWork == null) {
				wrWork = PurchasablePermissionFactory.getWRWork(getWorkInst());
			}
			return wrWork.getPublicationType();
		} catch (Exception e) {
			return null;
		}
	}

	public String getCountry() {
		try {
			if (wrWork == null) {
				wrWork = PurchasablePermissionFactory.getWRWork(getWorkInst());
			}
			return wrWork.getCountry();
		} catch (Exception e) {
			return null;
		}
	}

	public String getLanguage() {
		try {
			if (wrWork == null) {
				wrWork = PurchasablePermissionFactory.getWRWork(getWorkInst());
			}
			return wrWork.getLanguage();
		} catch (Exception e) {
			return null;
		}
	}

	public String getSeries() {
		try {
			if (wrWork == null) {
				wrWork = PurchasablePermissionFactory.getWRWork(getWorkInst());
			}
			return wrWork.getSeries();
		} catch (Exception e) {
			return null;
		}
	}


	public String getSeriesNumber() {
		try {
			if (wrWork == null) {
				wrWork = PurchasablePermissionFactory.getWRWork(getWorkInst());
			}
			return wrWork.getSeriesNumber();
		} catch (Exception e) {
			return null;
		}
	}

	public int getOrderDataSource() {
		return OrderDataSourceEnum.TF.getOrderDataSourceId().intValue();
	}

	public String getOrderDataSourceDisplay() {
		return OrderDataSourceEnum.TF.name();
	}

	public String getItemDescription() {
		return _license.getWork().getMainTitle();
	}

	public void setItemDescription(String itemDescription) {
		_license.getWork().setMainTitle(itemDescription);
	}

	public String getItemSubDescription() {
		return null;
	}

	public void setItemSubDescription(String itemSubDescription) {
		// Empty Method to satisfy interface
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

	public Date getLastUpdatedDate() {
		if (getLicense().getLastUpdateDate() != null) {
			return getLicense().getLastUpdateDate();
		} else {
			return getLicense().getCreateDate();
		}
	}

	public String getRhRefNum() {
		return getLicense().getRefRh();
	}

	public void setRhRefNum(String rhRefNum) {
		// Placeholder can not update for TF
	}

	public String getRightSourceCd() {
//		String rightsLinkCd = ECommerceConstants.RIGHT_DATA_SOURCE_TF;
//        if (_license instanceof LicenseWrapper) {
//			LicenseWrapper ordrLcn = (LicenseWrapper) _license;
//			rightsLinkCd = ordrLcn.getOrdrLicense().getRightSourceCd();
//		}
        return ECommerceConstants.RIGHT_DATA_SOURCE_TF;
	}

	public String getResearchUserIdentifier() {
		return null;
	}

	public String getItemStatusCd() {
		return getLicense().getOdtStatusCd();
	}

	public String getItemStatusDescription() {
		OdtStatusEnum odtStatusEnum = OdtStatusEnum.getEnumForOdtStatusCd(getLicense().getOdtStatusCd());

		if (odtStatusEnum != null) {
			return odtStatusEnum.getItemStatusDescription();
		}
		return null;
	}

	public String getItemStatusQualifierCd() {
		return null;
	}

	public String getItemStatusQualifierDescription() {
		OdtStatusEnum odtStatusEnum = OdtStatusEnum.getEnumForOdtStatusCd(getLicense().getOdtStatusCd());

		if (odtStatusEnum != null) {
			return odtStatusEnum.getItemCycleDescription();
		}
		return null;
	}

	public void setItemStatusCd(String itemStatusCd) {
//  Needed only for interface
	}
	 
	public void setItemStatusQualifierCd(String itemStatusQualifier) {
//  Needed only for interface
	}

	public Date getItemStatusDate() {
		return null;
	}
	
	public void setItemStatusDate(Date itemStatusDate) {
	//  Needed only for interface
	}	
	
	public String getItemStatusInternalDisplay() {
		OdtStatusEnum odtStatusEnum = OdtStatusEnum.getEnumForOdtStatusCd(getLicense().getOdtStatusCd());

		if (odtStatusEnum != null) {
			return odtStatusEnum.getItemStatusDescription();
		}
		
		return null;
	}
	
	public String getItemStatusExternalDisplay() {
		return getLicense().getOdtStatusCdMsg();
	}

	public String getItemCycleDisplay() {
		OdtStatusEnum odtStatusEnum = OdtStatusEnum.getEnumForOdtStatusCd(getLicense().getOdtStatusCd());

		if (odtStatusEnum != null) {
			return odtStatusEnum.getItemCycleDescription();
		}
		
		return null;
	}
	
	public String getItemErrorDescriptionDisplay() {
		OdtStatusEnum odtStatusEnum = OdtStatusEnum.getEnumForOdtStatusCd(getLicense().getOdtStatusCd());

		if (odtStatusEnum != null && odtStatusEnum.getItemStatusQualifier().equals(ItemStatusQualifierEnum.ERROR.name())) {
			return odtStatusEnum.getOdtStatusMsg();
		}
		return null;
	}
	
	public String getRightsholderAccount() {
		return null;
	}

	public Date getOverrideDate() {
		return getLicense().getOverrideCreateDate();
	}

	public String getOverrideComment() {
		return getLicense().getExternalCommentOverride();
	}

	public String getOverrideAvailabilityCd() {
		if (getOverrideDate() != null) {
			return this.getItemAvailabilityCd();
		}
		return null;
	}

	public String getOverrideAvailabilityDescription() {
		
		ItemAvailabilityEnum itemAvailabilityEnum = ItemAvailabilityEnum
				.getEnumForItemAvailabilityCd(getOverrideAvailabilityCd());
		if (itemAvailabilityEnum == null) {
			return null;
		}
		return itemAvailabilityEnum.getOrderLabel();
	}
	
	public Boolean isAdjusted() {
		if (getLicense().getAdjustmentLicenseList() != null) {
			if (getLicense().getAdjustmentLicenseList().size() > 0) {
				return true;
			}
		}
		return false;
	}
	
	// Can this item be adjusted
	public boolean isAdjustable(){
		boolean adjustable = true;
		adjustable =  (_license.getInvoiceId() != null);
		//adjustable = "SUCCESSFULLY INVOICED".equals(getLicenseStatusCdMsg());
		return adjustable;
	}
	
	public String getAdjustmentItems() {
		StringBuffer adjustmentDetails = new StringBuffer();
		boolean firstConcat = true;
		
		if (getLicense().getAdjustmentLicenseList() != null) {
			if (getLicense().getAdjustmentLicenseList().size() > 0) {
				for (Long adjustmentOdtInst : getLicense().getAdjustmentLicenseList()) {
					if (adjustmentOdtInst != null) {
						if (!firstConcat) {
							adjustmentDetails.append(", ");
						}
						adjustmentDetails.append(adjustmentOdtInst.toString());
						firstConcat = false;
					}
				}
			}
		}
		
		if (adjustmentDetails.length() > 0) {
			return adjustmentDetails.toString();
		}
		return null;
		
	}

	public BigDecimal getTotalPriceValue() {
		if (getLicense().getPrice() != null) {
			return new BigDecimal(getLicense().getPrice().getValue()).setScale(
					2, BigDecimal.ROUND_HALF_EVEN);
		}
		return null;
	}

	public BigDecimal getTotalDistributionPayableValue() {
		return new BigDecimal(getLicense().getRoyaltyPayable()).setScale(2,
				BigDecimal.ROUND_HALF_EVEN);
	}

	public BigDecimal getRightsholderPercent() {
		
		BigDecimal rightsholderPercent = new BigDecimal(0);
		BigDecimal pricePaid = null;
			
		if (this.getTotalPriceValue() != null
					&& this.getTotalDistributionPayableValue().compareTo(
							new BigDecimal(0)) > 0) {
				pricePaid = getTotalPriceValue().subtract(this.getTotalLicenseeFeeValue());
				pricePaid = pricePaid.add(this.getTotalDiscountValue());
				rightsholderPercent = this.getTotalDistributionPayableValue().divide(pricePaid, 2, BigDecimal.ROUND_HALF_EVEN);
				rightsholderPercent = rightsholderPercent.multiply(new BigDecimal(100));
				
			}
		if (rightsholderPercent != null){
			rightsholderPercent = rightsholderPercent.setScale(0, BigDecimal.ROUND_HALF_EVEN); 
		}
		return rightsholderPercent;
		
	}

	/*
	 * public String getCategoryCd() { if (this.isPhotocopy()) { return
	 * TRS_CATEGORY; } else if (this.isAPS()) { return APS_CATEGORY; } else if
	 * (this.isECCS()) { return ECCS_CATEGORY; } else if (this.isDigital()) {
	 * return DPS_CATEGORY; } else if (this.isRepublication()) { return
	 * RLS_CATEGORY; } return null; }
	 */
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

		return null;
	}

	public String getProductAndCategoryName() {
		return this.getProductCd() + " / " + this.getCategoryName();
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

	public void setExternalTouId(Long externalTouId) {
		// Only needed for interface
	}

	public void setProductSourceKey(Long productSourceKey) {
		// Only needed for interface
	}

	public void setProductName(String productName) {
		// Only needed for interface;
	}

	public void setProductCd(String productCd) {
		// Only needed for interface
	}

	public void setTouName(String touName) {
		// Only needed for interface
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

	public List<Address> getShippingAddress() {
		return null;
	}

	public BigDecimal getPerLogoFee() {
		if (_license.getRight() != null && _license.getRight().getRightFees() != null) {
			for (RightFee rightFee : _license.getRight().getRightFees()) {
				if (rightFee.getFeeName().equalsIgnoreCase(FEE_FIELD_PER_LOGO)) {
					try {return new BigDecimal(rightFee.getFeeValue()); } catch (Exception e) {return null;}
				}
			}
		}
		return null;
	}

	public BigDecimal getPerGraphFee() {
		if (_license.getRight() != null && _license.getRight().getRightFees() != null) {
			for (RightFee rightFee : _license.getRight().getRightFees()) {
				if (rightFee.getFeeName().equalsIgnoreCase(FEE_FIELD_PER_GRAPH)) {
					try {return new BigDecimal(rightFee.getFeeValue()); } catch (Exception e) {return null;}
				}
			}
		}
		return null;
	}

	public BigDecimal getPerCartoonFee() {
		if (_license.getRight() != null && _license.getRight().getRightFees() != null) {
			for (RightFee rightFee : _license.getRight().getRightFees()) {
				if (rightFee.getFeeName().equalsIgnoreCase(FEE_FIELD_PER_CARTOON)) {
					try {return new BigDecimal(rightFee.getFeeValue()); } catch (Exception e) {return null;}
				}
			}
		}
		return null;
	}

	public BigDecimal getPerExcerptFee() {
		if (_license.getRight() != null && _license.getRight().getRightFees() != null) {
			for (RightFee rightFee : _license.getRight().getRightFees()) {
				if (rightFee.getFeeName().equalsIgnoreCase(FEE_FIELD_PER_EXCERPT)) {
					try {return new BigDecimal(rightFee.getFeeValue()); } catch (Exception e) {return null;}
				}
			}
		}
		return null;
	}

	public BigDecimal getPerQuoteFee() {
		if (_license.getRight() != null && _license.getRight().getRightFees() != null) {
			for (RightFee rightFee : _license.getRight().getRightFees()) {
				if (rightFee.getFeeName().equalsIgnoreCase(FEE_FIELD_PER_QUOTE)) {
					try {return new BigDecimal(rightFee.getFeeValue()); } catch (Exception e) {return null;}
				}
			}
		}
		return null;
	}

	public BigDecimal getPerChartFee() {
		if (_license.getRight() != null && _license.getRight().getRightFees() != null) {
			for (RightFee rightFee : _license.getRight().getRightFees()) {
				if (rightFee.getFeeName().equalsIgnoreCase(FEE_FIELD_PER_CHART)) {
					try {return new BigDecimal(rightFee.getFeeValue()); } catch (Exception e) {return null;}
				}
			}
		}
		return null;
	}

	public BigDecimal getPerPhotographFee() {
		if (_license.getRight() != null && _license.getRight().getRightFees() != null) {
			for (RightFee rightFee : _license.getRight().getRightFees()) {
				if (rightFee.getFeeName().equalsIgnoreCase(FEE_FIELD_PER_PHOTOGRAPH)) {
					try {return new BigDecimal(rightFee.getFeeValue()); } catch (Exception e) {return null;}
				}
			}
		}
		return null;
	}

	public BigDecimal getPerIllustrationFee() {
		if (_license.getRight() != null && _license.getRight().getRightFees() != null) {
			for (RightFee rightFee : _license.getRight().getRightFees()) {
				if (rightFee.getFeeName().equalsIgnoreCase(FEE_FIELD_PER_ILLUSTRATION)) {
					try {return new BigDecimal(rightFee.getFeeValue()); } catch (Exception e) {return null;}
				}
			}
		}
		return null;
	}

	public BigDecimal getPerFigureFee() {
		if (_license.getRight() != null && _license.getRight().getRightFees() != null) {
			for (RightFee rightFee : _license.getRight().getRightFees()) {
				if (rightFee.getFeeName().equalsIgnoreCase(FEE_FIELD_PER_FIGURE)) {
					try {return new BigDecimal(rightFee.getFeeValue()); } catch (Exception e) {return null;}
				}
			}
		}
		return null;
	}

	public BigDecimal getMaxRoyaltyFee() {
//		if (_license.getRight() != null && _license.getRight().getRightFeeLimits() != null) {
//			for (RightFeeLimit rightFeeLimit : _license.getRight().getRightFeeLimits()) {
//				if (rightFeeLimit.getLimitName().equalsIgnoreCase(FEE_FIELD_MAXIMUM_ROYALTY_FEE)) {
//					try {return new BigDecimal(rightFeeLimit.getValueNum()); } catch (Exception e) {return null;}
//				}
//			}
//		}
//		return null;
		
		if (_license.getRight() != null && _license.getRight().getRightFees() != null) {
			for (RightFee rightFee : _license.getRight().getRightFees()) {
				if (rightFee.getFeeName().equalsIgnoreCase(FEE_FIELD_MAXIMUM_ROYALTY_FEE)) {
					try {return new BigDecimal(rightFee.getFeeValue()); } catch (Exception e) {return null;}
				}
			}
		}
		return null;
	}

	public BigDecimal getPerArticleAuthorFee() {
		if (_license.getRight() != null && _license.getRight().getRightFees() != null) {
			for (RightFee rightFee : _license.getRight().getRightFees()) {
				if (rightFee.getFeeName().equalsIgnoreCase(FEE_FIELD_PER_ARTICLE_AUTHOR)) {
					try {return new BigDecimal(rightFee.getFeeValue()); } catch (Exception e) {return null;}
				}
			}
		}
		return null;
	}

	public BigDecimal getPerArticleNonAuthorFee() {
		if (_license.getRight() != null && _license.getRight().getRightFees() != null) {
			for (RightFee rightFee : _license.getRight().getRightFees()) {
				if (rightFee.getFeeName().equalsIgnoreCase(FEE_FIELD_PER_ARTICLE_NON_AUTHOR)) {
					try {return new BigDecimal(rightFee.getFeeValue()); } catch (Exception e) {return null;}
				}
			}
		}
		return null;
	}

	public BigDecimal getTo30DaysFee() {
		if (_license.getRight() != null && _license.getRight().getRightFees() != null) {
			for (RightFee rightFee : _license.getRight().getRightFees()) {
				if (rightFee.getFeeName().equalsIgnoreCase(FEE_FIELD_TO_30_DAYS_FEE)) {
					try {return new BigDecimal(rightFee.getFeeValue()); } catch (Exception e) {return null;}
				}
			}
		}
		return null;
	}

	public BigDecimal getTo180DaysFee() {
		if (_license.getRight() != null && _license.getRight().getRightFees() != null) {
			for (RightFee rightFee : _license.getRight().getRightFees()) {
				if (rightFee.getFeeName().equalsIgnoreCase(FEE_FIELD_TO_180_DAYS_FEE)) {
					try {return new BigDecimal(rightFee.getFeeValue()); } catch (Exception e) {return null;}
				}
			}
		}
		return null;
	}

	public BigDecimal getTo365DaysFee() {
		if (_license.getRight() != null && _license.getRight().getRightFees() != null) {
			for (RightFee rightFee : _license.getRight().getRightFees()) {
				if (rightFee.getFeeName().equalsIgnoreCase(FEE_FIELD_TO_365_DAYS_FEE)) {
					try {return new BigDecimal(rightFee.getFeeValue()); } catch (Exception e) {return null;}
				}
			}
		}
		return null;
	}

	public BigDecimal getUnlimitedDaysFee() {
		if (_license.getRight() != null && _license.getRight().getRightFees() != null) {
			for (RightFee rightFee : _license.getRight().getRightFees()) {
				if (rightFee.getFeeName().equalsIgnoreCase(FEE_FIELD_UNLIMITED_DAYS_FEE)) {
					try {return new BigDecimal(rightFee.getFeeValue()); } catch (Exception e) {return null;}
				}
			}
		}
		return null;
	}

	public BigDecimal getTo49RecipientsFee() {
		if (_license.getRight() != null && _license.getRight().getRightFees() != null) {
			for (RightFee rightFee : _license.getRight().getRightFees()) {
				if (rightFee.getFeeName().equalsIgnoreCase(FEE_FIELD_TO_49_RECIPIENTS_FEE)) {
					try {return new BigDecimal(rightFee.getFeeValue()); } catch (Exception e) {return null;}
				}
			}
		}
		return null;
	}

	public BigDecimal getTo249RecipientsFee() {
		if (_license.getRight() != null && _license.getRight().getRightFees() != null) {
			for (RightFee rightFee : _license.getRight().getRightFees()) {
				if (rightFee.getFeeName().equalsIgnoreCase(FEE_FIELD_TO_249_RECIPIENTS_FEE)) {
					try {return new BigDecimal(rightFee.getFeeValue()); } catch (Exception e) {return null;}
				}
			}
		}
		return null;
	}

	public BigDecimal getTo499RecipientsFee() {
		if (_license.getRight() != null && _license.getRight().getRightFees() != null) {
			for (RightFee rightFee : _license.getRight().getRightFees()) {
				if (rightFee.getFeeName().equalsIgnoreCase(FEE_FIELD_TO_499_RECIPIENTS_FEE)) {
					try {return new BigDecimal(rightFee.getFeeValue()); } catch (Exception e) {return null;}
				}
			}
		}
		return null;
	}

	public BigDecimal getTo500pRecipientsFee() {
		if (_license.getRight() != null && _license.getRight().getRightFees() != null) {
			for (RightFee rightFee : _license.getRight().getRightFees()) {
				if (rightFee.getFeeName().equalsIgnoreCase(FEE_FIELD_TO_500P_RECIPIENTS_FEE)) {
					try {return new BigDecimal(rightFee.getFeeValue()); } catch (Exception e) {return null;}
				}
			}
		}
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
    
    public boolean isRequiresAcademicRepin() {
    	return _requiresAcademicRepin;
    }

	public void setRequiresAcademicRepin(boolean academicRepin) {
		_requiresAcademicRepin = academicRepin;
	}     
	public String getOrderRefNumber(){
	return null;
	}

	public boolean isAvailableToCopy() {
			if (getItemAvailabilityCd() != null){
				if(getItemAvailabilityCd().equals(ItemAvailabilityEnum.DENY.getStandardPermissionCd()) ||
						getItemAvailabilityCd().equals(ItemAvailabilityEnum.CONTACT_RH_DIRECTLY.getStandardPermissionCd()) ||
							getItemAvailabilityCd().equals(ItemAvailabilityEnum.HOLD_PENDING.getStandardPermissionCd())){
					return false;
				}
				
			}
			
			return true;
		
	}
	
	public boolean getLicenseeRequestedEntireWork() 
	{
		boolean value = false;
		switch (_usageType) 
		{
		case USAGE_TYPE_ACADEMIC:
			if (_usageDataAcademic.getLcnRequestedEntireBook() != null && _usageDataAcademic.getLcnRequestedEntireBook())
			{
				value = true;
			}
			else if (_usageDataAcademic.getLcnRequestedEntireBook() != null && !_usageDataAcademic.getLcnRequestedEntireBook())
			{
				value = false;
			}
			else
			{
				value = false;
			}
		}
		
		return value;
	}
	
	
	
	public void setLicenseeRequestedEntireWork(boolean licenseeEntireWork){
		_usageDataAcademic.setLcnRequestedEntireBook(licenseeEntireWork);
		
		this.setRequiresAcademicRepin(true);
	   	  
	}
	
	public void setOrderSourceCd(String orderSourceCd) {
		this._orderSourceCd = orderSourceCd;
	}

	public String getOrderSourceCd() {
		return _orderSourceCd;
	}
	
	public void setDeclineReasonCd(DeclineReasonEnum declineCd) {
		//this.setDeclineReasonCd(DeclineReasonEnum.NO_LONGER_NEED);
	}

	public String getDeclineReasonCd() {
		return null;
	}
	
	public String getPaymentMethod() {
		
		if (this.isPaidByCreditCard()) {
			
			return "CC ending in " + this.getLastFourCreditCard();
			
		}
		else {
			return ItemConstants.PAYMENT_TYPE_INVOICE;
		}
		
    }
	public boolean isAwaitingLicConfirm() {
		
		return false;
	}
}
