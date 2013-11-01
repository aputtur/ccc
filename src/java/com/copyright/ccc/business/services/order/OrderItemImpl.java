package com.copyright.ccc.business.services.order;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.copyright.ccc.business.data.OrderLicense;
import com.copyright.ccc.business.services.ItemAvailabilityEnum;
import com.copyright.ccc.business.services.ItemConstants;
import com.copyright.ccc.business.services.ItemDescriptionParmEnum;
import com.copyright.ccc.business.services.ItemHelperServices;
import com.copyright.ccc.business.services.ItemParmEnum;
import com.copyright.ccc.business.services.OdtStatusEnum;
import com.copyright.ccc.business.services.OrderDataSourceEnum;
import com.copyright.ccc.business.services.ProductEnum;
import com.copyright.ccc.business.services.RightSourceEnum;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.business.services.cart.ECommerceConstants;
import com.copyright.ccc.business.services.cart.ECommerceUtils;
import com.copyright.ccc.config.CC2Configuration;
import com.copyright.ccc.web.transaction.RepublicationConstants;
import com.copyright.ccc.web.transaction.TransactionConstants;
import com.copyright.ccc.web.util.WebUtils;
import com.copyright.data.account.Address;
import com.copyright.data.account.Country;
import com.copyright.data.inventory.Right;
import com.copyright.data.inventory.Work;
import com.copyright.data.order.License;
import com.copyright.data.order.OrderSourceEnum;
import com.copyright.data.order.UsageData;
import com.copyright.svc.order.api.data.DeclineReasonEnum;
import com.copyright.svc.order.api.data.ItemStatusEnum;
import com.copyright.svc.order.api.data.ItemStatusQualifierEnum;
import com.copyright.svc.order.api.data.OrderConsumerContext;
import com.copyright.svc.order.api.data.Payment;
import com.copyright.svc.order.api.data.SpecialOrderTypeEnum;
import com.copyright.svc.order.api.data.Item;
import com.copyright.svc.order.api.data.ItemFees;
import com.copyright.svc.order.api.data.ItemParm;
import com.copyright.workbench.i18n.Money;

public class OrderItemImpl implements OrderLicense {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(OrderItemImpl.class);

	Item _item;
	Long _purchaseConfirmationNumber;
	// License _license;
	// UsageData _usageData;
	int _usageType = 0;

	private boolean _showAdjustedValues = false;
	
	private String _itemStatusInternalDisplay = null;
	private String _itemStatusExternalDisplay = null;
	private String _itemCycleDisplay = null;
	private String _itemErrorDescription = null;
	
	private boolean _requiresAcademicRepin = false;
	private boolean _pricingFieldChanged = false;
	
	private BigDecimal _baseFeeOverrideValue;
	private BigDecimal _entireBookFeeOverrideValue;
	private BigDecimal _flatFeeOverrideValue;
	private BigDecimal _perPageFeeOverrideValue;
	private BigDecimal _maximumRoyaltyFeeOverrideValue;
	private BigDecimal _durationFeeOverrideValue;
	private BigDecimal _recipientsFeeOverrideValue;
	private BigDecimal _perContentFeeOverrideValue;
	
	private String _orderSourceCd = null;

	// UsageDataAcademic _usageDataAcademic;
	// UsageDataEmail _usageDataEmail;
	// UsageDataNet _usageDataNet;
	// UsageDataPhotocopy _usageDataPhotocopy;
	// UsageDataRepublication _usageDataRepublication;
	// Work _work;

	Payment _payment;

	private int _OrderDataSource = ECommerceConstants.ORDER_DATA_SOURCE_COI;

	static final long INTRANET_TPU_CODE = 204;
	static final long EXTRANET_TPU_CODE = 134;
	static final long INTERNET_TPU_CODE = 203;

	static final long DPS_PRODUCT_CODE = 36;
	static final long TRS_PRODUCT_CODE = 1;
	static final long APS_PRODUCT_CODE = 2;
	static final long ECCS_PRODUCT_CODE = 12;

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



	public OrderItemImpl() {
	}

	public void setItem(Item item) {
		_item = item;
		calcItemStatusDisplay();
	}

	public Item getItem() {
		return _item;
	}

	// Refactoring: This should be deleted
	public void setLicense(License license) {
		/*
		 * _license = license; if (_license.getUsageData() instanceof
		 * UsageDataAcademic) { _usageDataAcademic = (UsageDataAcademic)
		 * license.getUsageData(); _usageType = 1; } else if
		 * (_license.getUsageData() instanceof UsageDataEmail) { _usageDataEmail
		 * = (UsageDataEmail) license.getUsageData(); _usageType = 2; } else if
		 * (_license.getUsageData() instanceof UsageDataNet) { _usageDataNet =
		 * (UsageDataNet) license.getUsageData(); _usageType = 3; } else if
		 * (_license.getUsageData() instanceof UsageDataPhotocopy) {
		 * _usageDataPhotocopy = (UsageDataPhotocopy) license.getUsageData();
		 * _usageType = 4; } else if (_license.getUsageData() instanceof
		 * UsageDataRepublication) { _usageDataRepublication =
		 * (UsageDataRepublication) license.getUsageData(); _usageType = 5; }
		 * 
		 * calculateRightFees();
		 */
	}

	// Refactoring: This should be deleted
	@Deprecated
	public License getLicense() {
		return null;
	}

	// Refactoring: Move logic out to OrderHelper.jar so it can be shared across apps 
	// e.g. Method should remain here but return ItemHelperServices.isAcademic(Item item);
	public boolean isAcademic() {
		boolean isAcademic = isAPS() || isECCS();

		return isAcademic;
	}

	// Refactoring: Move logic out to OrderHelper.jar so it can be shared across apps 
	// e.g. Method should remain here but return ItemHelperServices.isEmail(Item item);
	public boolean isEmail() {
		boolean isEmail = false;
		if (getItem().getProductSourceKey() != null
				&& getItem().getExternalTouId() != null) {
			isEmail = getItem().getProductSourceKey().longValue() == ECommerceConstants.DPS_PRODUCT_CODE
					&& getItem().getExternalTouId() == ECommerceConstants.EMAIL_TPU_CODE;
		}

		return isEmail;
	}

	// Refactoring: Move logic out to OrderHelper.jar so it can be shared across apps 
	// e.g. Method should remain here but ItemHelperServices.isNet(Item item);
	public boolean isNet() {
		boolean isNet = isInternet() || isIntranet() || isExtranet();

		return isNet;
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above
	public boolean isPhotocopy() {
		boolean isPhotocopy = false;
		if (getItem().getProductSourceKey() != null
				&& getItem().getExternalTouId() != null) {
			isPhotocopy = getItem().getProductSourceKey().longValue() == ECommerceConstants.TRS_PRODUCT_CODE
					&& getItem().getExternalTouId() == ECommerceConstants.PHOTOCOPY_TPU_CODE;
		}

		return isPhotocopy;
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above
	public boolean isRepublication() {
		boolean isRepublication = false;
		if (getItem().getProductSourceKey() != null) {
			isRepublication = getItem().getProductSourceKey().longValue() == ECommerceConstants.RLS_PRODUCT_CODE;
		}

		return isRepublication;
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above
	public boolean isIntranet() {
		boolean isIntranet = false;
		if (getItem().getProductSourceKey() != null
				&& getItem().getExternalTouId() != null) {
			isIntranet = getItem().getProductSourceKey().longValue() == ECommerceConstants.DPS_PRODUCT_CODE
					&& getItem().getExternalTouId().longValue() == ECommerceConstants.INTRANET_TPU_CODE;
		}

		return isIntranet;
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above
	public boolean isExtranet() {
		boolean isExranet = false;
		if (getItem().getProductSourceKey() != null
				&& getItem().getExternalTouId() != null) {
			isExranet = getItem().getProductSourceKey().longValue() == ECommerceConstants.DPS_PRODUCT_CODE
					&& getItem().getExternalTouId().longValue() == ECommerceConstants.EXTRANET_TPU_CODE;

		}

		return isExranet;
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above
	public boolean isInternet() {
		boolean isInternet = false;
		if (getItem().getProductSourceKey() != null
				&& getItem().getExternalTouId() != null) {
			isInternet = getItem().getProductSourceKey().longValue() == ECommerceConstants.DPS_PRODUCT_CODE
					&& getItem().getExternalTouId().longValue() == ECommerceConstants.INTERNET_TPU_CODE;
		}

		return isInternet;
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above
	public boolean isSpecialOrder() {
		
		if (getItem().getIsSpecialOrder() != null) {
			return getItem().getIsSpecialOrder();
		} else {
			return false;
		}
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above
	public boolean isUnresolvedSpecialOrder() {

		if (isSpecialOrder()) {
			if (getItemAvailabilityCd() == null || getItemAvailabilityCd().isEmpty()) {
				return true;
			}
			if (getItemAvailabilityCd().equals(ItemAvailabilityEnum.NO_CHARGE.getStandardPermissionCd()) ||
				getItemAvailabilityCd().equals(ItemAvailabilityEnum.DENY.getStandardPermissionCd()) ||
				getItemAvailabilityCd().equals(ItemAvailabilityEnum.PURCHASE.getStandardPermissionCd()) ||
				getItemAvailabilityCd().equals(ItemAvailabilityEnum.CONTACT_RH_DIRECTLY.getStandardPermissionCd()) ||
				getItemAvailabilityCd().equals(ItemAvailabilityEnum.PUBLIC_DOMAIN.getStandardPermissionCd())) {
				return false;
			}
		}
		
		return isSpecialOrder();

	}

	
	public Boolean isAdjusted() {
		return getItem().getIsAdjusted();
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above
	// Can this item be adjusted
	public boolean isAdjustable(){
		boolean adjustible = false;
		
		if( CC2Configuration.getInstance().isDisableOmsAdjustments() )
			return false;
		
		LOGGER.debug("Item Status is " + getItem().getItemId() + " : " + getItem().getItemStatusCd());
		if ((ItemStatusEnum.INVOICED.equals(getItem().getItemStatusCd())) ||
			//(ItemStatusQualifierEnum.INVOICED.equals(getItem().getItemStatusQualifier())) ||
			(ItemStatusEnum.DISTRIBUTED.equals(getItem().getItemStatusCd())) ){
			//TODO is this correct for being invoiced
			if (
				(ItemConstants.PAYMENT_METHOD_CREDIT_CARD.equalsIgnoreCase(getPaymentMethodCd())) ||
				(isRightsLink() )
				)
			{
				adjustible = true;
			}
		}
		if (!adjustible){
			LOGGER.warn("Item not adjustable. Status is " + getItem().getItemId() + " : " + getItem().getItemStatusCd());
		}
		return adjustible;
	}

	// Refactoring: I think this can be deleted
	public String getAdjustmentItems() {
		return null;
	}

	
	
	// Refactoring: Move logic out to OrderHelper.jar following pattern described above
	public void setSpecialOrderTypeCd(String specialOrderTypeCd) {
		for (SpecialOrderTypeEnum specialOrderTypeEnum : SpecialOrderTypeEnum
				.values()) {
			if (specialOrderTypeEnum.name()
					.equalsIgnoreCase(specialOrderTypeCd)) {
				getItem().setSpecialOrderTypeCd(specialOrderTypeEnum);
				if (specialOrderTypeEnum
						.equals(SpecialOrderTypeEnum.NOT_SPECIAL_ORDER)) {
					getItem().setIsSpecialOrder(false);
				} else {
					getItem().setIsSpecialOrder(true);
				}
			}
		}
	}

	public String getSpecialOrderTypeCd() {
		if (getItem().getSpecialOrderTypeCd() != null) {
			return getItem().getSpecialOrderTypeCd().name();
		}
		return null;
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above
	public boolean isDigital() {
		boolean isDigital = isNet() || isEmail();

		return isDigital;
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above
	public boolean isRightsLink() {

		if (getProductCd() != null
				&& (getProductCd().equals(ProductEnum.RL.name()) || getProductCd()
						.equals(ProductEnum.RLR.name()))) {
			return true;
		}

		return false;

	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above
	public boolean isReprint() {

		if (getProductCd() != null
				&& getProductCd().equals(ProductEnum.RLR.name())) {
			return true;
		}
		return false;
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above
	public boolean isPricedReprint() {

		if (getProductCd() != null
				&& getProductCd().equals(ProductEnum.RLR.name()) 
				&& getPriceValueRaw() != ItemConstants.RL_NOT_PRICED) {
			return true;
		}
		return false;
	}

	
	
	// Refactoring: Move logic out to OrderHelper.jar following pattern described above
	public boolean isAPS() {
		boolean isAPS = false;
		if (getItem().getProductSourceKey() != null) {
			isAPS = getItem().getProductSourceKey().longValue() == ECommerceConstants.APS_PRODUCT_CODE;
		}

		return isAPS;
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above
	public boolean isECCS() {
		boolean isECCS = false;
		if (getItem().getProductSourceKey() != null) {
			isECCS = getItem().getProductSourceKey().longValue() == ECommerceConstants.ECCS_PRODUCT_CODE;
		}

		return isECCS;
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above
	public boolean isDistributed() {
		for (ItemFees itemFees : getItem().getAllFees()) {
			if (itemFees.getDistributionEventId() != null) {
				if (itemFees.getDistributionEventId()
						.compareTo(Long.valueOf(0)) > 0) {
					return true;
				}
			}
		}
		return false;
	}

	// Refactoring: This is an order header attribute and can be removed from the license level
	public long getLicenseePartyID() {
		// return _license.getlicenseePartyID();
		return 0;
	}

	// Refactoring: This can be deleted
	public int getUsageType() {
		return _usageType;
	}

	// public String getPermissionType() {
	// if (this.isAcademic() && this.isPhotocopy()) {
	// return ACADEMIC_PHOTOCOPY_DESCRIPTION;
	// } else if (this.isAcademic() && this.isAPS())
	// }

	public String getLicenseeRefNum() {
		return getItem().getLicenseeRefNum();
	}

	public void setLicenseeRefNum(String yourReference) {
		getItem().setLicenseeRefNum(StringUtils.upperCase(yourReference));
	}

	// Use getLicenseeRefNum
	@Deprecated
	public String getCustomerReference() {
		return getItem().getLicenseeRefNum();
	}

	// Use setLicenseeRefNum
	@Deprecated
	public void setCustomerReference(String yourReference) {
		getItem().setLicenseeRefNum(StringUtils.upperCase(yourReference));
	}

	// Use getLicenseeRefNum
	public String getCustomerRef() {
		return getItem().getLicenseeRefNum();
	}

	// Use setLicenseeRefNum
	public void setCustomerRef(String yourReference) {
		getItem().setLicenseeRefNum(StringUtils.upperCase(yourReference));
	}

	// This should be deprecated and getExternalRightId should be used in its place
	public long getRgtInst() {
		if (getItem().getExternalRightId() != null) {
			return getItem().getExternalRightId().longValue();
		} else {
			return 0;
		}
	}

	public Long getRightId() {
		return getItem().getRightId();
	}

	public Long getExternalRightId() {
		return getItem().getExternalRightId();
	}

	// This should be deprecated and getExternalTouId should be used in its place
	public long getTpuInst() {
		if (getItem().getExternalTouId() != null) {
			return getItem().getExternalTouId().longValue();
		} else {
			return 0;
		}
	}

	// This should be deprectated and getExternalTouId should be used in its place
	public Long getTpuInstLong() {
		return getItem().getExternalTouId();
	}

	// This should be deprecated and getProductSourceKey should be used in its place
	public long getPrdInst() {
		if (getItem().getProductSourceKey() != null) {
			return getItem().getProductSourceKey().longValue();
		} else {
			return 0;
		}
	}

	// This should be deprecated and getProductSourceKey should be used in its place
	public Long getPrdInstLong() {
		return getItem().getProductSourceKey();
	}

	// public String getRightPermissionType() {
	// return _license.getRightPermissionType();
	// }

	// The if statement below was for transition purposes and can be removed
	public String getProductCd() {

		if (getItem().getProductCd() != null && getItem().getProductCd().equalsIgnoreCase("ECCS")) {
			getItem().setProductCd("ECC");
		}

		return getItem().getProductCd();
	}

	public void setProductCd(String productCd) {
		getItem().setProductCd(productCd);
	}

	public Long getProductSourceKey() {
		return getItem().getProductSourceKey();
	}

	public void setProductSourceKey(long productSourceKey) {
		getItem().setProductSourceKey(Long.valueOf(productSourceKey));
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

	public Long getTouSourceKey() {
		return getItem().getTouSourceKey();
	}

	public void setTouSourceKey(Long touSourceKey) {
		getItem().setTouSourceKey(touSourceKey);
	}

	public Long getExternalTouId() {
		return getItem().getExternalTouId();
	}

	public void setExternalTouId(Long externalTouId) {
		getItem().setExternalTouId(externalTouId);
	}

	public String getTouName() {
		return getItem().getTouName();
	}

	public void setTouName(String touName) {
		getItem().setTouName(touName);
	}

	// These next 3 can be deleted
	@Deprecated
	public Work getWork() {
		return null;
	}

	@Deprecated
	public UsageData getUsageData() {
		return null;
	}

	@Deprecated
	public Right getRight() {
		return null;
	}
	
	public String getBillingStatusCode() {

		String itemStatusCd = ItemConstants.NOT_INVOICED;

		if (getItem().getItemStatusCd().equals(ItemStatusEnum.CANCELLED)) {
			return ItemConstants.CANCELED;
		}

		if (getItem().getItemStatusCd().equals(ItemStatusEnum.INVOICED) ||
			getItem().getInvoiceNumber() != null) {
			if (ItemStatusQualifierEnum.PAID_CREDIT_CARD
					.equals(getItem().getItemStatusQualifier())) {
				return ItemConstants.PAID_BY_CREDIT_CARD;
			}
			if (getTotalPriceValue().compareTo(new BigDecimal(0)) > 0) {
				return ItemConstants.INVOICED;				
			} else {
				return ItemConstants.PAYMENT_NA;
			}
		}

		if (getItem().getItemStatusCd().equals(ItemStatusEnum.INVOICING_IN_PROGRESS)) {
			if (ItemStatusQualifierEnum.INVOICE_READY_CREDIT_CARD
					.equals(getItem().getItemStatusQualifier())) {
				return ItemConstants.PAID_BY_CREDIT_CARD;
			}
		}

		if (getItem().getItemStatusCd().equals(ItemStatusEnum.INVOICE_READY)) {
			if (ItemStatusQualifierEnum.INVOICE_READY_CREDIT_CARD
					.equals(getItem().getItemStatusQualifier())) {
				return ItemConstants.PAID_BY_CREDIT_CARD;
			}
		}
		
		if (getItem().getItemStatusCd().equals(ItemStatusEnum.AWAITING_LCN_CONFIRM)) {
			return ItemConstants.BILLING_STATUS_AWAITING_LCN_CONFIRM;
		}

		return itemStatusCd;
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above
	public String getBillingStatus() {

		String billingStatus = ItemConstants.NOT_INVOICED;

		if (getItem().getItemStatusCd().equals(ItemStatusEnum.CANCELLED)) {
			return ItemConstants.CANCELED;
		}

		if (getItem().getItemStatusCd().equals(ItemStatusEnum.INVOICED) ||
		    getItem().getItemStatusCd().equals(ItemStatusEnum.DISTRIBUTED) ||	
			getItem().getInvoiceNumber() != null) {
			if (ItemStatusQualifierEnum.PAID_CREDIT_CARD
					.equals(getItem().getItemStatusQualifier())) {
				return ItemConstants.PAID_BY_CREDIT_CARD;
			}
			if (getTotalPriceValue().compareTo(new BigDecimal(0)) > 0) {
				return ItemConstants.INVOICED;				
			} else {
				return ItemConstants.PAYMENT_NA;
			}
		}

		if (getItem().getItemStatusCd().equals(ItemStatusEnum.INVOICING_IN_PROGRESS)) {
			if (ItemStatusQualifierEnum.INVOICE_READY_CREDIT_CARD
					.equals(getItem().getItemStatusQualifier())) {
				return ItemConstants.PAID_BY_CREDIT_CARD;
			}
		}

		if (getItem().getItemStatusCd().equals(ItemStatusEnum.INVOICE_READY)) {
			if (ItemStatusQualifierEnum.INVOICE_READY_CREDIT_CARD
					.equals(getItem().getItemStatusQualifier())) {
				return ItemConstants.PAID_BY_CREDIT_CARD;
			}
		}

		return billingStatus;
	}
	

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above
	public String getBillingStatusCd() {

		if (getBillingStatus().equalsIgnoreCase(ItemConstants.NOT_INVOICED)) {
			return ItemConstants.BILLING_STATUS_NOT_BILLED_CD;
		} else if (getBillingStatus().equalsIgnoreCase(ItemConstants.CANCELED)) {
			return ItemConstants.BILLING_STATUS_CANCELED_CD;
		} else {
			return ItemConstants.BILLING_STATUS_BILLED_CD;
		}

	}

	// Migrate to getItemId (optional)
	public String getTransactionId() {
		return getItem().getItemId().toString();
	}

	public Date getCreateDate() {
		return getItem().getCreateDate();
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

		if (this.getCreateDate() != null) {
			if (this.getCreateDate().getTime() > 0) {
				SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy"); // scr
				// 7899
				createDateString = formatter.format(this.getCreateDate());
			}
		}

		return createDateString;
	}

	// Delete this, it is now a payment attribute
	public String getCreditAuth() {
		//For COI the Credit Authorization Number is carried in the payment transaction id
		if (getCcTrxId()!=null) {
			return getCcTrxId().toString();
		}
		else {
		return null;
	}
	}

	// public String getPermissionSelected() {
	// return _license.getDisplayPermissionStatus();
	// }

	/*
	 * License ID or Order Detail ID
	 */
	// Migrate to getItemId (optional)
	public long getID() {
		return getItem().getItemId().longValue();
	}

	public Long getBundleId() {
		return getItem().getBundleId();
	}

	public void setBundleId(Long bundleId) {
		getItem().setBundleId(bundleId);
	}

	// Migrate to getOverrideComment
	public String getExternalCommentOverride() {
		return getItem().getOverrideComment();
	}

	/*
	 * Returns the External comment term from the Term on the Right TODO kmeyer:
	 * Should this be added to the OrderLicense interface? lalberione: Pulled up
	 * method declaration to TransactionItem
	 */

	public String getExternalCommentTerm() {
		return getItem().getRightQualifierTerms();
	}

	// Migrate to getInvoiceNumber 
	// This can now just be getItem.getInvoiceNumber
	public String getInvoiceId() {
		if (getItem().getInvoiceNumber() != null) {
			return getItem().getInvoiceNumber();
		}
		return null;
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above
	public boolean isInvoiced() {
		if (this.getInvoiceId() != null && !this.getInvoiceId().isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
	
	// Refactoring: Move logic out to OrderHelper.jar following pattern described above
	public boolean isPaidByCreditCard() {
		if (ItemConstants.PAYMENT_METHOD_CREDIT_CARD.equals(this.getPaymentMethodCd())) {
			return true;
		} else {
			return false;
		}
	}
	
	public void setInvoiceId(String invoiceId) {
		getItem().setInvoiceNumber(invoiceId);
	}

	public Long getPaymentId() {
		return getItem().getPaymentId();
	}

	// Delete this, it's now an order header attribute
	@Deprecated
	public long getLicenseePartyId() {
		return 0;
	}

	public Date getInvoiceDate() {
		return getItem().getInvoiceDate();
	}

	public void setInvoiceDate(Date invoiceDate) {
		getItem().setInvoiceDate(invoiceDate);
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above
	public boolean isEditable() {
		if (!isCanceled() && !isInvoiced() && !isPaidByCreditCard() && !isRightsLink()) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isAwaitingLicConfirm() {
		
		if (getItem().getItemStatusCd().equals(ItemStatusEnum.AWAITING_LCN_CONFIRM)) {
			return true;
		}
		
		return false;
	}
	
	// Refactoring: Move logic out to OrderHelper.jar following pattern described above
	public boolean isCancelable() {

		if (getItem().getProductCd().equals(ProductEnum.RL.name())
				|| getItem().getProductCd().equals(ProductEnum.RLR.name())) {
			return false;
		}

		if (getItem().getItemStatusCd() == null) {
			return false;
		}
						
		if (getItem().getItemStatusCd().equals(ItemStatusEnum.INVOICE_READY) &&
			getItem().getItemStatusQualifier() != null &&
			getItem().getItemStatusQualifier().equals(ItemStatusQualifierEnum.INVOICE_READY_CREDIT_CARD)) {
			return false;
		}
		
		if (getItem().getItemStatusCd().equals(ItemStatusEnum.INVOICED)) {
			return false;
		}
		
		if (getItem().getItemStatusCd().equals(ItemStatusEnum.INVOICING_IN_PROGRESS)) {
			return false;
		}
		
		if (getItem().getItemStatusCd().equals(ItemStatusEnum.CANCELLED)) {
			return false;
		}
		
		if (getItem().getItemStatusCd().equals(ItemStatusEnum.DISTRIBUTED)) {
			return false;
		} 
		
		if (this.getOrderSourceCd() != null) {
			if (this.getOrderSourceCd().equals(OrderSourceEnum.GATEWAY.getCode())) {
				return false;
			}
		}

		
//		if (getItem().getItemAvailabilityCd() == null) {
//			return false;
//		}
		
		if (getItem().getItemAvailabilityCd() != null)
		{
			if (getItem().getItemAvailabilityCd().equals(ItemAvailabilityEnum.DENY.getStandardPermissionCd())) {
				return false;
			}
		}

		return true;
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above
	public boolean isCanceled() {
		
		if (getItem().getItemStatusCd() == null) {
			return false;
		}
		
		if (getItem().getItemStatusCd().equals(ItemStatusEnum.CANCELLED)) {
			return true;
		}
		
		return false;
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public long getLastFourCreditCard() {
		if (getPayment() != null) {
			if (getPayment().getLastFourCc() != null) {
				return getPayment().getLastFourCc().longValue();
			} else {
				return 0L;
			}
		}
		return 0L;
	}

	// Migrate to getOrderHeaderId
	public long getOrderId() {
		if (getItem().getOrderHeaderId() != null) {
			return getItem().getOrderHeaderId().longValue();
		}
		return 0;
	}

	public Long getOrderHeaderId() {
		return getItem().getOrderHeaderId();
	}

	// Migrate to setOrderHeaderId
	public void setOrderId(long orderId) {
		getItem().setOrderHeaderId(Long.valueOf(orderId));
	}

	// Migrate to getOrderHeaderId
	public Long getOrderIdLong() {
		return getItem().getOrderHeaderId();
	}
	
	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public String getDisplayPermissionStatus() {
//		if (getItemStatusCd() != null && getItemStatusCd().equals(ItemStatusEnum.CANCELLED.name())) {
//			return ItemConstants.REQUEST_HAS_BEEN_CANCELED;
//		}		

//		if (getItemStatusCd() != null && getItemStatusCd().equals(ItemStatusEnum.AWAITING_FULFILLMENT.name())) {
//			return ItemConstants.REQUEST_AWAITING_FULFILLMENT;
//		}		

		
		ItemAvailabilityEnum itemAvailabilityEnum = ItemAvailabilityEnum
				.getEnumForItemAvailabilityCd(getItem().getItemAvailabilityCd());
		if (itemAvailabilityEnum != null) {
			return itemAvailabilityEnum.getOrderLabel();
		}
		return null;
	}

	// Migrate to getItemAvailabilityCd
	public String getDisplayPermissionStatusCd() {

		return getItemAvailabilityCd();

	}

	
	/*
	 * public String getDisplayOrigPermissionStatus() { ItemAvailabilityEnum
	 * itemAvailabilityEnum =
	 * ItemAvailabilityEnum.getEnumForItemAvailabilityCd(getItem
	 * ().getItemOrigAvailabilityCd()); if (itemAvailabilityEnum != null) {
	 * return itemAvailabilityEnum.getOrderLabel(); } return null; } public
	 * String getDisplayPermissionStatusExternal() { ItemAvailabilityEnum
	 * itemAvailabilityEnum =
	 * ItemAvailabilityEnum.getEnumForItemAvailabilityCd(getItem
	 * ().getItemAvailabilityCd()); if (itemAvailabilityEnum != null) { return
	 * itemAvailabilityEnum.getExternalOrderLabel(); } return null; }
	 * 
	 * public String getDisplayOrigPermissionStatusExternal() {
	 * ItemAvailabilityEnum itemAvailabilityEnum =
	 * ItemAvailabilityEnum.getEnumForItemAvailabilityCd
	 * (getItem().getItemOrigAvailabilityCd()); if (itemAvailabilityEnum !=
	 * null) { return itemAvailabilityEnum.getOrderLabel(); } return null; }
	 */

	// Migrate to getRightAvailabilityCd
	public String getPermissionStatus() {
		return getItem().getRightAvailabilityCd();
	}

	public void setRightAvailabilityCd(String rightAvailabilityCd) {
		getItem().setRightAvailabilityCd(rightAvailabilityCd);
	}

	public String getRightAvailabilityCd() {
		return getItem().getRightAvailabilityCd();
	}

	// The if statements below were transitional and can now be deleted
	public String getItemAvailabilityCd() {

		if (getItem().getItemAvailabilityCd() == "CR") {
			getItem().setItemAvailabilityCd("C");
		}
		if (getItem().getItemAvailabilityCd() == "CRD") {
			getItem().setItemAvailabilityCd("I");
		}

		return getItem().getItemAvailabilityCd();
	}

	public void setItemAvailabilityCd(String itemAvailabilityCd) {
		getItem().setItemAvailabilityCd(itemAvailabilityCd);
	}

	
	// The if statements below were transitional and can now be deleted
	public String getItemOrigAvailabilityCd() {

		if (getItem().getItemOrigAvailabilityCd() == "CR") {
			getItem().setItemOrigAvailabilityCd("C");
		}
		if (getItem().getItemOrigAvailabilityCd() == "CRD") {
			getItem().setItemOrigAvailabilityCd("I");
		}

		return getItem().getItemOrigAvailabilityCd();
	}

	public void setItemOrigAvailabilityCd(String itemOrigAvailabilityCd) {
		getItem().setItemOrigAvailabilityCd(itemOrigAvailabilityCd);
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public String getItemAvailabilityDescription() {
		ItemAvailabilityEnum itemAvailabilityEnum = ItemAvailabilityEnum.getEnumForItemAvailabilityCd(this.getItemAvailabilityCd());

		if (itemAvailabilityEnum != null) {
			return itemAvailabilityEnum.getExternalOrderLabel();
		}
		return null;

	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public String getItemOrigAvailabilityDescription() {
		ItemAvailabilityEnum itemAvailabilityEnum = ItemAvailabilityEnum.getEnumForItemAvailabilityCd(this.getItemOrigAvailabilityCd());

		if (itemAvailabilityEnum != null) {
			return itemAvailabilityEnum.getExternalOrderLabel();
		}
		return null;
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
    public String getPrice() {

    	Money moneyPrice;
    	
        if (getItem().getItemStatusCd() != null && getItem().getItemStatusCd().equals(ItemStatusEnum.CANCELLED)) {
            moneyPrice = new Money(0);
            return WebUtils.formatMoney(moneyPrice);
        }
    	
    	
//    	BigDecimal price = getItem().getTotalPrice();
    	if (RightSourceEnum.TF.name().equals(getRightSourceCd())) {
            if (getSpecialOrderTypeCd() != null && !getSpecialOrderTypeCd().equals(SpecialOrderTypeEnum.NOT_SPECIAL_ORDER.name())) {
                    return ItemConstants.COST_TBD;            
            }
//    		if (!getSpecialOrderTypeCd().equals(SpecialOrderTypeEnum.NOT_SPECIAL_ORDER.name())) {
//        		return ItemConstants.COST_TBD;  		
//    		}
    	}

    	if (RightSourceEnum.RL.name().equals(getRightSourceCd()) && 
    			ProductEnum.RL.name().equals(getProductCd()) && 
    		!ItemAvailabilityEnum.PURCHASE.getStandardPermissionCd().equals(getItemAvailabilityCd())) {
        		return ItemConstants.COST_TBD;
    	}

    	if (RightSourceEnum.RL.name().equals(getRightSourceCd()) && 
    			ProductEnum.RLR.name().equals(getProductCd()) && 
        		getPriceValueRaw() == ItemConstants.RL_NOT_PRICED) {
            		return ItemConstants.COST_TBD;
       	}
    	
    	BigDecimal price = getTotalPriceValue();
    	
    	
    	if (price != null ) {
        	moneyPrice = new Money(price.doubleValue());    		
    	} else {
    		moneyPrice = new Money(0);
    	}
	  
    	return WebUtils.formatMoney(moneyPrice);
    }
	
    // Migrate to getTotalPriceValue
    public double getPriceValue() {
    	if (getTotalPriceValue() != null) {
        	return getTotalPriceValue().doubleValue();
    	} else {
    		return 0;
    	}
    }

    // Migrate to getTotalPriceValueRaw
    public double getPriceValueRaw() {
    	if (getTotalPriceValue() != null) {
        	return getTotalPriceValueRaw().doubleValue();
    	} else {
    		return 0;
    	}
    }
    
	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
    public BigDecimal getTotalPriceValue() {

		if (RightSourceEnum.RL.name().equalsIgnoreCase(getItem().getRightSourceCd())) {
			if (getTotalPriceValueRaw().doubleValue() == ItemConstants.RL_NOT_PRICED) {
	    		return new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_EVEN);
			} 
		}

    	return getTotalPriceValueRaw();
    }
    
	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
    public BigDecimal getTotalPriceValueRaw() {
  	    	
    	if (RightSourceEnum.TF.name().equals(this.getRightSourceCd())) {
    		if (isAdjusted() && isShowAdjustedValues() == false) {
    	    	return getItem().getTotalPrice().setScale(2, BigDecimal.ROUND_HALF_EVEN);    			
    		}
    		if (getTotalRightsholderFeeValue()==null && getTotalLicenseeFeeValue()== null && 
        			getTotalDistributionPayableValue()==null && getTotalDiscountValue()==null	) {
        			return null;
        	}
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

    
	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public String getItemAvailabilityDescriptionInternal() {
		ItemAvailabilityEnum itemAvailabilityEnum = ItemAvailabilityEnum
				.getEnumForItemAvailabilityCd(this.getItemAvailabilityCd());

		if (itemAvailabilityEnum != null) {
			return itemAvailabilityEnum.getOrderLabel();
		}
		return null;

	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public String getItemOrigAvailabilityDescriptionInternal() {
		String availabilityLabel = null;
		
		ItemAvailabilityEnum itemAvailabilityEnum = ItemAvailabilityEnum
				.getEnumForItemAvailabilityCd(this.getItemOrigAvailabilityCd());

		if (itemAvailabilityEnum != null) {
			availabilityLabel = itemAvailabilityEnum.getOrderLabel();

			if (itemAvailabilityEnum.equals(ItemAvailabilityEnum.CONTACT_RH) && 
				getSpecialOrderTypeCd().equals(SpecialOrderTypeEnum.LIMITS_EXCEEDED_SPECIAL_ORDER.name())) {
				availabilityLabel = availabilityLabel + " " + ItemConstants.LIMITS_EXCEEDED_LABEL;
			}
		}
		return availabilityLabel;

	}

	// public String getDisplayPermissionStatusCd() {
	// ItemAvailabilityEnum itemAvailabilityEnum =
	// ItemAvailabilityEnum.getEnumForName(getItem().getItemAvailabilityCd());
	// ItemAvailabilityEnum itemAvailabilityEnum =
	// ItemAvailabilityEnum.getEnumForItemAvailabilityCd(getItem().getItemAvailabilityCd());
	// return itemAvailabilityEnum.getExternalOrderLabel();
	// }

	public Date getPinningDate() {
		return getItem().getPinningDtm();
	}

	public long getPurchaseId() {
		if (_purchaseConfirmationNumber != null) {
			return _purchaseConfirmationNumber;
		}
		return 0;
	}

	public void setPurchaseId(Long purchaseId) {
		_purchaseConfirmationNumber = purchaseId;
	}

	// public Money getPrice() {
	// return _license.getPrice();
	// }

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public String getReDistFlag() {
		// TODO Multiple RH logic
		for (ItemFees itemFees : getItem().getAllFees()) {
			if (!itemFees.getActualDistAccount().equalsIgnoreCase(
					itemFees.getOrigDistAccount())) {
				return "Y";
			}
		}
		return "N";
		// return _license.getReDistFlag();
	}

	// This is an interface field that supports logic of "old" adjustment app and can ultimately be removed when that app is obsoleted
	public long getRefOdtInst() {
		// TODO Multiple RH logic
		// return _license.getOrderDetailReferenceID();\
		return 0;
	}

	// Migrate to getCancellationReason
	public String getWithdrawnCode() {
		return getItem().getCancellationReason();
		// return _license.getWithdrawnCode();
	}

	// Migrate to getItemDescription
	public String getPublicationTitle() {
		return getItem().getItemDescription();
	}

	public String getStandardNumber() {
		return ItemHelperServices.getItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.STANDARD_NUMBER.name());
	}

	public String getItemTypeCd() {
		return getItem().getItemTypeCd();
	}

	public void setItemTypeCd(String itemTypeCd) {
		getItem().setItemTypeCd(itemTypeCd);
	}

	public String getPublisher() {
		return ItemHelperServices.getItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.PUBLISHER.name());

	}

	// public Term getRightsQualifyingStatement() {
	// return _license.getRight().getRightQualifierTerm();
	// }

	public String getAuthor() {
		return ItemHelperServices.getItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.AUTHOR.name());
	}

	public String getEditor() {
		return ItemHelperServices.getItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.EDITOR.name());
	}

	public String getVolume() {
		return ItemHelperServices.getItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.VOLUME.name());
	}

	public String getEdition() {
		return ItemHelperServices.getItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.EDITION.name());
	}

	public Date getPublicationDateOfUse() {
		return ItemHelperServices.getItemParmDate(getItem(),
				ItemParmEnum.DATE_OF_PUBLICATION_USED.name());
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

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public String getPublicationYearOfUse() {

		// return ItemHelperServices.getItemParmString(getItem(),
		// ItemParmEnum.PUBLICATION_YEAR_OF_USE.name());
		String publicationYearBeingUsed = Constants.EMPTY_STRING;

		Date publicationDateOfUse = getPublicationDateOfUse();

		if (publicationDateOfUse != null) {
			try {
				publicationYearBeingUsed = ECommerceUtils
						.getYearYYYY(publicationDateOfUse);
			} catch (ParseException e) {
				// Do nothing will return empty string
			}
		}

		return publicationYearBeingUsed;
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	private String getYearFromDate(Date date) {
		if (date == null)
			return "";

		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return String.valueOf(calendar.get(GregorianCalendar.YEAR));
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public void setPublicationYearOfUse(String publicationYearBeingUsed) {

		// ItemHelperServices.setItemParmString(getItem(),
		// ItemParmEnum.PUBLICATION_YEAR_OF_USE.name(),
		// publicationYearBeingUsed);

		Date dateOfPublicationUsed;
		try {
			dateOfPublicationUsed = ECommerceUtils
					.transformYearToDate(publicationYearBeingUsed);
		} catch (ParseException e) {
			dateOfPublicationUsed = null;
		}

		if (ItemHelperServices.getItemParmDate(getItem(), ItemParmEnum.DATE_OF_PUBLICATION_USED.name()) == null ||
				dateOfPublicationUsed == null || 
				!dateOfPublicationUsed.equals(ItemHelperServices.getItemParmDate(getItem(),ItemParmEnum.DATE_OF_PUBLICATION_USED.name()))) {
				setRequiresAcademicRepin(true);	
				setPricingFieldChanged(true);
		}
		
		ItemHelperServices.setItemParmDate(getItem(),
				ItemParmEnum.DATE_OF_PUBLICATION_USED.name(),
				dateOfPublicationUsed);
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public long getNumberOfPages() {
		BigDecimal numberOfPages;
		
		if (isRepublication()) {
				numberOfPages = ItemHelperServices.getItemParmNumber(
						getItem(), ItemParmEnum.RLS_PAGES.name());			
		} else {
				numberOfPages = ItemHelperServices.getItemParmNumber(
						getItem(), ItemParmEnum.NUM_PAGES.name());			
		}

		if (numberOfPages != null) {
			return numberOfPages.longValue();
		} else {
			return 0;
		}	
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public void setNumberOfPages(long numberOfPages) {

		if (isRepublication()) {
			
			setPricingFieldChanged(ItemHelperServices.isFieldChanged(getItem(), 
					   numberOfPages, ItemParmEnum.RLS_PAGES.name()));
			
			ItemHelperServices.setItemParmNumber(getItem(),
					ItemParmEnum.RLS_PAGES.name(),
					new BigDecimal(numberOfPages));
		} else {
			
			setPricingFieldChanged(ItemHelperServices.isFieldChanged(getItem(), 
					   numberOfPages, ItemParmEnum.NUM_PAGES.name()));

			if (ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.NUM_PAGES.name()) == null ||
				numberOfPages != ItemHelperServices.getItemParmNumber(getItem(), ItemParmEnum.NUM_PAGES.name()).intValue()) {
					setRequiresAcademicRepin(true);	
			}
			ItemHelperServices.setItemParmNumber(getItem(),
					ItemParmEnum.NUM_PAGES.name(),
					new BigDecimal(numberOfPages));
		}

	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public long getNumberOfCopies() {
		BigDecimal numberOfCopies;
		
		numberOfCopies = ItemHelperServices.getItemParmNumber(
					getItem(), ItemParmEnum.NUM_COPIES.name());			
		
		if (numberOfCopies != null) {
			return numberOfCopies.longValue();
		} else {
			return 0;
		}
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public void setNumberOfCopies(long numberOfCopies) {

		setPricingFieldChanged(ItemHelperServices.isFieldChanged(getItem(), 
				   numberOfCopies, ItemParmEnum.NUM_COPIES.name()));
		
		ItemHelperServices.setItemParmNumber(getItem(), ItemParmEnum.NUM_COPIES
				.name(), new BigDecimal(numberOfCopies));

	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public long getNumberOfRecipients() {
		BigDecimal numberOfRecipients;
		
		numberOfRecipients = ItemHelperServices.getItemParmNumber(
					getItem(), ItemParmEnum.NUM_RECIPIENTS.name());
	
		if (numberOfRecipients != null) {
			return numberOfRecipients.longValue();
		} else {
			return 0;
		}

	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public void setNumberOfRecipients(long numberOfRecipients) {

		setPricingFieldChanged(ItemHelperServices.isFieldChanged(getItem(), 
				   numberOfRecipients, ItemParmEnum.NUM_RECIPIENTS.name()));

		ItemHelperServices.setItemParmNumber(getItem(),
				ItemParmEnum.NUM_RECIPIENTS.name(), new BigDecimal(
						numberOfRecipients));

	}

	// Migrate to getTouName
	public String getPermissionSelected() {

		return getItem().getTouName();

	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public int getDuration() {

		BigDecimal duration = ItemHelperServices.getItemParmNumber(getItem(),
				ItemParmEnum.DURATION.name());

		if (duration != null) {
			return duration.intValue();
		} else {
			return 0;
		}

	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public void setDuration(int duration) {

		setPricingFieldChanged(ItemHelperServices.isFieldChanged(getItem(), 
				   duration, ItemParmEnum.DURATION.name()));
		
		ItemHelperServices.setItemParmNumber(getItem(), ItemParmEnum.DURATION
				.name(), new BigDecimal(duration));

		if (duration == 0) {
			ItemHelperServices.setItemParmString(getItem(),
					ItemParmEnum.DURATION_CD.name(), DurationEnum.TO_30_DAYS
							.getDesc());
		} else if (duration == 1) {
			ItemHelperServices.setItemParmString(getItem(),
					ItemParmEnum.DURATION_CD.name(), DurationEnum.TO_180_DAYS
							.getDesc());
		} else if (duration == 2) {
			ItemHelperServices.setItemParmString(getItem(),
					ItemParmEnum.DURATION_CD.name(), DurationEnum.TO_365_DAYS
							.getDesc());
		} else if (duration == 3) {
			ItemHelperServices.setItemParmString(getItem(),
					ItemParmEnum.DURATION_CD.name(),
					DurationEnum.UNLIMITED_DAYS.getDesc());
		}
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public String getWebAddress() {

		return ItemHelperServices.getItemParmString(getItem(),
				ItemParmEnum.WEB_ADDRESS.name());

	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public void setWebAddress(String webAddress) {

		setPricingFieldChanged(ItemHelperServices.isFieldChanged(getItem(), 
				   webAddress, ItemParmEnum.WEB_ADDRESS.name()));

		ItemHelperServices.setItemParmString(getItem(),
				ItemParmEnum.WEB_ADDRESS.name(), webAddress);

	}

	public String getRepublicationTitle() {

		return ItemHelperServices.getItemParmString(getItem(),
				ItemParmEnum.REPUBLICATION_DESTINATION.name());

	}

	public String getRepublicationAuthor() {

		return ItemHelperServices.getItemParmString(getItem(),
				ItemParmEnum.CUSTOM_AUTHOR.name());

	}

	public void setRepublicationAuthor(String republicationAuthor) {

		ItemHelperServices.setItemParmString(getItem(),
				ItemParmEnum.CUSTOM_AUTHOR.name(), republicationAuthor);

	}

	/*
	 * 
	 * numberOfStudents republicationDestination circulationDistribution
	 * Business typeOfContent isSubmitterAuthor contentsPublicationDate Duration
	 */

	public String getBusiness() {

		String businessType = ItemHelperServices.getItemParmString(getItem(),
				ItemParmEnum.BUSINESS.name());

		return businessType;

	}

	// Migrate to getBusiness
	@Deprecated
	public String getBusinessType() {

		String businessType = ItemHelperServices.getItemParmString(getItem(),
				ItemParmEnum.BUSINESS.name());

		return businessType;

	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public String getBusinessDescription() {
		String rtnValue = "";
		String business = this.getBusinessType();
		if (RepublicationConstants.BUSINESS_FOR_PROFIT.equals(business)) {
			rtnValue = TransactionConstants.FOR_PROFIT;
		} else if (RepublicationConstants.BUSINESS_NON_FOR_PROFIT
				.equals(business)) {
			rtnValue = TransactionConstants.NOT_FOR_PROFIT;
		}

		return rtnValue;
	}

	public String getChapterArticle() {

		return ItemHelperServices.getItemParmString(getItem(),
				ItemParmEnum.CHAPTER_ARTICLE.name());

	}

	public void setChapterArticle(String chapterArticle) {

		ItemHelperServices.setItemParmString(getItem(),
				ItemParmEnum.CHAPTER_ARTICLE.name(), chapterArticle);

	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public int getCirculationDistribution() {

		BigDecimal circulationDistribution;
		
		circulationDistribution = ItemHelperServices.getItemParmNumber(
					getItem(), ItemParmEnum.CIRCULATION_DISTRIBUTION.name());			
		
		if (circulationDistribution != null) {
			return circulationDistribution.intValue();
		} else {
			return 0;
		}

	}

	public Date getContentsPublicationDate() {

		Date contentsPublicationDate = ItemHelperServices.getItemParmDate(
				getItem(), ItemParmEnum.DATE_OF_PUBLICATION_USED.name());

		return contentsPublicationDate;
	}

	public Date getDateOfUse() {

		Date dateOfUse = ItemHelperServices.getItemParmDate(getItem(),
				ItemParmEnum.DATE_OF_USE.name());

		return dateOfUse;
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

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	// Migrate to isAuthor
	public boolean isSubmitterAuthor() {
		String isSubmitterAuthorYN = ItemHelperServices.getItemParmString(
				getItem(), ItemParmEnum.IS_AUTHOR.name());

		boolean isSubmitterAuthor = false;

		if (isSubmitterAuthorYN.equalsIgnoreCase(ItemConstants.YES_CD)) {
			isSubmitterAuthor = true;
		}

		return isSubmitterAuthor;
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	// Migrate to isAuthor
	public String getSubmitterAuthor() {

		String isSubmitterAuthorYN = ItemHelperServices.getItemParmString(
				getItem(), ItemParmEnum.IS_AUTHOR.name());

		return isSubmitterAuthorYN;
	}

	public String getNewPublicationTitle() {

		String newPublicationTitle = ItemHelperServices.getItemParmString(
				getItem(), ItemParmEnum.NEW_PUBLICATION_TITLE.name());

		return newPublicationTitle;
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public int getNumberOfStudents() {
		BigDecimal numberOfStudents;
		
		numberOfStudents = ItemHelperServices.getItemParmNumber(
					getItem(), ItemParmEnum.NUM_COPIES.name());			
		
		if (numberOfStudents != null) {
			return numberOfStudents.intValue();
		} else {
			return 0;
		}

	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public void setNumberOfStudents(int numberOfStudents) {
		
		setPricingFieldChanged(ItemHelperServices.isFieldChanged(getItem(), 
				   numberOfStudents, ItemParmEnum.NUM_COPIES.name()));
		
		ItemHelperServices.setItemParmNumber(getItem(), ItemParmEnum.NUM_COPIES
				.name(), new BigDecimal(numberOfStudents));
	}

	public String getPageRange() {

		String pageRange = ItemHelperServices.getItemParmString(getItem(),
				ItemParmEnum.PAGE_RANGE.name());

		return pageRange;
	}

	public String getPublicationYear() {
		return ItemHelperServices.getItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.PUBLICATION_YEAR.name());
	}

	public String getPublicationYearStr() {
		return ItemHelperServices.getItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.PUBLICATION_YEAR.name());
	}

	public Date getRepublicationDate() {

		Date republicationDate = ItemHelperServices.getItemParmDate(getItem(),
				ItemParmEnum.REPUBLICATION_DATE.name());

		return republicationDate;

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

		return ItemHelperServices.getItemParmString(getItem(),
				ItemParmEnum.REPUBLICATION_DESTINATION.name());

	}

	public String getRepublishingOrganization() {

		String republishingOrganization = ItemHelperServices.getItemParmString(
				getItem(), ItemParmEnum.REPUBLISHING_ORGANIZATION.name());

		return republishingOrganization;
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public String getRightsholder() {
		/*
		 * String rh = ""; Right right = _license.getRight(); if (right != null)
		 * { Party party = tfService.getOrgRightsholderPartyByPartyId (new
		 * TFConsumerContext(), (Long)
		 * _license.getRight().getRightsHolderPartyId()); if (party != null) {
		 * rh = party.getOrgName(); } }
		 * 
		 * //return _license.getRight().getRightsholder().getName(); return rh;
		 */

		if (getItem().getAllFees() == null) {
			return ItemConstants.NO_RIGHTSHOLDER;
		}
		if (getItem().getAllFees().size() == 0) {
			return ItemConstants.NO_RIGHTSHOLDER;
		}
		if (getItem().getAllFees().size() > 0) {
			StringBuffer RhNamesBuf = new StringBuffer();
			for (ItemFees itemFees : getItem().getAllFees()) {
				if (RhNamesBuf.length() > 0
						&& !itemFees.getOrigDistPartyName().isEmpty()) {
					RhNamesBuf.append(", ");
				}
				if (itemFees.getOrigDistPartyName() != null) {
					RhNamesBuf.append(itemFees.getOrigDistPartyName());
				}
			}
			if (RhNamesBuf.length()>0) {
				return RhNamesBuf.toString();
			} else {
				return null;
			}
		}
		return null;

	}

	public String getRightsQualifyingStatement() {
		return getItem().getWorkQualifierTerms();
	}

	public String getRightQualifierTerms() {
		return getItem().getWorkQualifierTerms();
	}

	public String getResolutionTerms() {
		return getItem().getResolutionTerms();
	}

	public String getLicenseTerms() {
		return getItem().getLicenseTerms();
	}

	public String getTranslationLanguage() {

		String translationLanguage = ItemHelperServices.getItemParmString(
				getItem(), ItemParmEnum.TRANSLATION_LANGUAGE.name());

		return translationLanguage;
	}

	/**
	 * Return a displayable version of the Language suitable for UI
	 * presentation.
	 * 
	 * @return language with first character capitalized.
	 */
	public String getTranslationLanguageDescription() {
		String translationLanguageDescription = Constants.EMPTY_STRING;
		String translationLanguage = ItemHelperServices.getItemParmString(
				getItem(), ItemParmEnum.TRANSLATION_LANGUAGE.name());

		if (StringUtils.isNotEmpty(translationLanguage)) {
			if (!RepublicationConstants.NO_TRANSLATION
					.equals(translationLanguage)) {
				translationLanguageDescription = WebUtils
						.makeDisplayable(translationLanguage);
			}
		} else {
			throw new UnsupportedOperationException();
		}

		return translationLanguageDescription;
	}

	public String getTypeOfContent() {
		String typeOfContent = ItemHelperServices.getItemParmString(getItem(),
				ItemParmEnum.TYPE_OF_CONTENT.name());

		return typeOfContent;
		
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public String getTypeOfContentDescription() {
		String typeOfContentDescription = Constants.EMPTY_STRING;

		if (getTypeOfContent().equalsIgnoreCase(
				RepublicationConstants.CONTENT_FULL_ARTICLE_CHAPTER)) {
			typeOfContentDescription = TransactionConstants.CONTENT_FULL_ARTICLE_CHAPTER;
		} else if (getTypeOfContent().equalsIgnoreCase(
				RepublicationConstants.CONTENT_EXCERPT)) {
			typeOfContentDescription = TransactionConstants.CONTENT_EXCERPT;
		} else if (getTypeOfContent().equalsIgnoreCase(
				RepublicationConstants.CONTENT_QUOTATION)) {
			typeOfContentDescription = TransactionConstants.CONTENT_QUOTATION;
		} else if (getTypeOfContent().equalsIgnoreCase(
				RepublicationConstants.CONTENT_CHART)) {
			typeOfContentDescription = TransactionConstants.CONTENT_CHART;
		} else if (getTypeOfContent().equalsIgnoreCase(
				RepublicationConstants.CONTENT_FIGURE_DIAGRAM_TABLE)) {
			typeOfContentDescription = TransactionConstants.CONTENT_FIGURE_DIAGRAM_TABLE;
		} else if (getTypeOfContent().equalsIgnoreCase(
				RepublicationConstants.CONTENT_PHOTOGRAPH)) {
			typeOfContentDescription = TransactionConstants.CONTENT_PHOTOGRAPH;
		} else if (getTypeOfContent().equalsIgnoreCase(
				RepublicationConstants.CONTENT_CARTOONS)) {
			typeOfContentDescription = TransactionConstants.CONTENT_CARTOONS;
		} else if (getTypeOfContent().equalsIgnoreCase(
				RepublicationConstants.CONTENT_ILLUSTRATION)) {
			typeOfContentDescription = TransactionConstants.CONTENT_ILLUSTRATION;
		} else if (getTypeOfContent().equalsIgnoreCase(
				RepublicationConstants.CONTENT_GRAPH)) {
			typeOfContentDescription = TransactionConstants.CONTENT_GRAPH;
		} else if (getTypeOfContent().equalsIgnoreCase(
				RepublicationConstants.CONTENT_SELECTED_PAGES)) {
			typeOfContentDescription = TransactionConstants.CONTENT_SELECTED_PAGES;
		}

		return typeOfContentDescription;
	}

	// Migrate to getTypeOfContentDescription
	public String getRepublishNonTextPortion() {
		return this.getTypeOfContentDescription();
	}

	// Migrate to getTouName
	public String getTypeOfUseDescription() {
		return getItem().getTouName();
	}

	public void setAuthor(String author) {
		ItemHelperServices.setItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.AUTHOR.name(), author);
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public void setBusiness(String business) {

		// ItemHelperServices.setItemParmString(getItem(),
		// ItemParmEnum.FOR_PROFIT.name(), business);
		if (business.equals(RepublicationConstants.BUSINESS_FOR_PROFIT)) {
			setPricingFieldChanged(ItemHelperServices.isFieldChanged(getItem(), 
					RepublicationConstants.BUSINESS_FOR_PROFIT, ItemParmEnum.BUSINESS.name()));

			ItemHelperServices.setItemParmString(getItem(),
					ItemParmEnum.BUSINESS.name(),
					RepublicationConstants.BUSINESS_FOR_PROFIT);
			ItemHelperServices.setItemParmString(getItem(),
					ItemParmEnum.FOR_PROFIT.name(), ItemConstants.YES_CD);
		} else {
			setPricingFieldChanged(ItemHelperServices.isFieldChanged(getItem(), 
					RepublicationConstants.BUSINESS_NON_FOR_PROFIT, ItemParmEnum.BUSINESS.name()));

			ItemHelperServices.setItemParmString(getItem(),
					ItemParmEnum.BUSINESS.name(),
					RepublicationConstants.BUSINESS_NON_FOR_PROFIT);
			ItemHelperServices.setItemParmString(getItem(),
					ItemParmEnum.FOR_PROFIT.name(), ItemConstants.NO_CD);
		}

	}

//	Migrate to setBusiness
	@Deprecated
	public void setBusinessType(String business) {
		ItemHelperServices.setItemParmString(getItem(), ItemParmEnum.FOR_PROFIT
				.name(), business);
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public void setCirculationDistribution(int circulationDistribution) {

		setPricingFieldChanged(ItemHelperServices.isFieldChanged(getItem(), 
				   circulationDistribution, ItemParmEnum.CIRCULATION_DISTRIBUTION.name()));
		
		ItemHelperServices.setItemParmNumber(getItem(),
				ItemParmEnum.CIRCULATION_DISTRIBUTION.name(), new BigDecimal(
						circulationDistribution));
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public void setContentsPublicationDate(Date contentsPublicationDate) {

		setPricingFieldChanged(ItemHelperServices.isFieldChanged(getItem(), 
				contentsPublicationDate, ItemParmEnum.DATE_OF_PUBLICATION_USED.name()));

		ItemHelperServices.setItemParmDate(getItem(),
				ItemParmEnum.DATE_OF_PUBLICATION_USED.name(),
				contentsPublicationDate);

		if (contentsPublicationDate != null) {
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(contentsPublicationDate);
			this.setPublicationYear(Integer
					.valueOf(calendar.get(Calendar.YEAR)).toString());
		}
	}

	public void setDateOfUse(Date dateOfUse) {
		ItemHelperServices.setItemParmDate(getItem(), ItemParmEnum.DATE_OF_USE
				.name(), dateOfUse);
	}

	public void setEdition(String edition) {
		ItemHelperServices.setItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.EDITION.name(), edition);
	}

	public void setEditor(String editor) {
		ItemHelperServices.setItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.EDITOR.name(), editor);
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public void setSubmitterAuthor(boolean isSubmitterAuthor) {
		
		String isSubmitterAuthorYN = ItemConstants.NO_CD;

		if (isSubmitterAuthor) {
			isSubmitterAuthorYN = ItemConstants.YES_CD;
		}

		setPricingFieldChanged(ItemHelperServices.isFieldChanged(getItem(), 
				isSubmitterAuthorYN, ItemParmEnum.IS_AUTHOR.name()));
		
		ItemHelperServices.setItemParmString(getItem(), ItemParmEnum.IS_AUTHOR
				.name(), isSubmitterAuthorYN);

	}

	public void setNewPublicationTitle(String newPublicationTitle) {

		ItemHelperServices.setItemParmString(getItem(),
				ItemParmEnum.NEW_PUBLICATION_TITLE.name(), newPublicationTitle);

	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public void setPageRange(String pageRange) {
				
		if (ItemHelperServices.getItemParmString(getItem(), ItemParmEnum.PAGE_RANGE.name()) == null ||
				pageRange == null || 
				!pageRange.equals(ItemHelperServices.getItemParmString(getItem(),
						ItemParmEnum.PAGE_RANGE.name()))) {
				setRequiresAcademicRepin(true);	
				setPricingFieldChanged(true);
		}
		
		ItemHelperServices.setItemParmString(getItem(), ItemParmEnum.PAGE_RANGE
				.name(), pageRange);

	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public void setPermissionSelected(String permissionSelected) {
		ItemAvailabilityEnum itemAvailabilityEnum = ItemAvailabilityEnum
				.getEnumForTFPermissionCd(permissionSelected);

		getItem().setRightAvailabilityCd(permissionSelected);
		getItem().setItemAvailabilityCd(
				itemAvailabilityEnum.getStandardPermissionCd());

	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public void setPublicationDateOfUse(Date publicationDateOfUse) {
		
		setPricingFieldChanged(ItemHelperServices.isFieldChanged(getItem(), 
				   publicationDateOfUse, ItemParmEnum.DATE_OF_PUBLICATION_USED.name()));

		ItemHelperServices.setItemParmDate(getItem(),
				ItemParmEnum.DATE_OF_PUBLICATION_USED.name(),
				publicationDateOfUse);
	}

	// Migrate to setItemDescription
	public void setPublicationTitle(String publicationTitle) {
		getItem().setItemDescription(publicationTitle);
	}

	public void setPublicationYear(String publicationYear) {
		ItemHelperServices.setItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.PUBLICATION_YEAR.name(),
				publicationYear);
	}

	public void setPublisher(String publisher) {
		ItemHelperServices.setItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.PUBLISHER.name(), publisher);
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public void setRepublicationDate(Date republicationDate) {
		
		setPricingFieldChanged(ItemHelperServices.isFieldChanged(getItem(), 
							   republicationDate, ItemParmEnum.REPUBLICATION_DATE.name()));
			
		ItemHelperServices.setItemParmDate(getItem(),
				ItemParmEnum.REPUBLICATION_DATE.name(), republicationDate);
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public void setRepublicationDestination(String republicationDestination) {
		
		setPricingFieldChanged(ItemHelperServices.isFieldChanged(getItem(), 
				   republicationDestination, ItemParmEnum.REPUBLICATION_DESTINATION.name()));
		
		ItemHelperServices.setItemParmString(getItem(),
				ItemParmEnum.REPUBLICATION_DESTINATION.name(),
				republicationDestination);
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public void setRepublishingOrganization(String republishingOrganization) {
		
		setPricingFieldChanged(ItemHelperServices.isFieldChanged(getItem(), 
				  republishingOrganization, ItemParmEnum.REPUBLISHING_ORGANIZATION.name()));

		ItemHelperServices.setItemParmString(getItem(),
				ItemParmEnum.REPUBLISHING_ORGANIZATION.name(),
				republishingOrganization);
	}

	public void setStandardNumber(String standardNumber) {
		ItemHelperServices.setItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.STANDARD_NUMBER.name(), standardNumber);
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public void setTranslationLanguage(String translationLanguage) {
		
		setPricingFieldChanged(ItemHelperServices.isFieldChanged(getItem(), 
				   translationLanguage, ItemParmEnum.TRANSLATION_LANGUAGE.name()));
		
		ItemHelperServices.setItemParmString(getItem(),
				ItemParmEnum.TRANSLATION_LANGUAGE.name(), translationLanguage);
		if (translationLanguage.equals(RepublicationConstants.NO_TRANSLATION)) {
			setTranslated(RepublicationConstants.NOT_TRANSLATED);
		} else {
			setTranslated(RepublicationConstants.TRANSLATED);
		}

	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public void setTypeOfContent(String typeOfContent) {

		setPricingFieldChanged(ItemHelperServices.isFieldChanged(getItem(), 
				   typeOfContent, ItemParmEnum.TYPE_OF_CONTENT.name()));
		
		ItemHelperServices.setItemParmString(getItem(),
				ItemParmEnum.TYPE_OF_CONTENT.name(), typeOfContent);

		this.setRepublishFullArticle(FULL_ARTICLE_NO);
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

		// if(
		// !typeOfContent.equals(RepublicationConstants.CONTENT_SELECTED_PAGES )
		// )
		// {
		// this.setRlsPages( CONTENT_TYPE_ZERO_QUANTITY );
		// }

		if (typeOfContent
				.equals(RepublicationConstants.CONTENT_FULL_ARTICLE_CHAPTER)) {
			this.setRepublishFullArticle(FULL_ARTICLE_YES);
		} else if (typeOfContent.equals(RepublicationConstants.CONTENT_EXCERPT)) {
			this.setNumberOfExcerpts(CONTENT_TYPE_UNITARY_QUANTITY);

		} else if (typeOfContent
				.equals(RepublicationConstants.CONTENT_QUOTATION)) {
			this.setNumberOfQuotes(CONTENT_TYPE_UNITARY_QUANTITY);

		} else if (typeOfContent.equals(RepublicationConstants.CONTENT_CHART)) {
			this.setNumberOfCharts(CONTENT_TYPE_UNITARY_QUANTITY);

		} else if (typeOfContent.equals(RepublicationConstants.CONTENT_GRAPH)) {
			this.setNumberOfGraphs(CONTENT_TYPE_UNITARY_QUANTITY);

		} else if (typeOfContent
				.equals(RepublicationConstants.CONTENT_FIGURE_DIAGRAM_TABLE)) {
			this.setNumberOfFigures(CONTENT_TYPE_UNITARY_QUANTITY);

		} else if (typeOfContent
				.equals(RepublicationConstants.CONTENT_PHOTOGRAPH)) {
			this.setNumberOfPhotos(CONTENT_TYPE_UNITARY_QUANTITY);

		} else if (typeOfContent
				.equals(RepublicationConstants.CONTENT_CARTOONS)) {
			this.setNumberOfCartoons(CONTENT_TYPE_UNITARY_QUANTITY);
		} else if (typeOfContent
				.equals(RepublicationConstants.CONTENT_ILLUSTRATION)) {
			this.setNumberOfIllustrations(CONTENT_TYPE_UNITARY_QUANTITY);

		} else if (typeOfContent
				.equals(RepublicationConstants.CONTENT_SELECTED_PAGES)) {
			// do nothing
		} else {
			throw new IllegalArgumentException("Invalid content type.");
		}

	}

	public void setVolume(String volume) {
		ItemHelperServices.setItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.VOLUME.name(), volume);
	}

	public String getDateOfIssue() {
		String dateOfIssue = ItemHelperServices.getItemParmString(getItem(),
				ItemParmEnum.DATE_OF_ISSUE.name());

		return dateOfIssue;
	}

	public void setDateOfIssue(String dateOfIssue) {
		ItemHelperServices.setItemParmString(getItem(),
				ItemParmEnum.DATE_OF_ISSUE.name(), dateOfIssue);
	}

	public Long getWorkInst() {
//		if (getItem().getExternalItemId() != null) {
			return getItem().getExternalItemId();
//		} else {
//			return 0L;
//		}
	}

	public void setWorkInst(Long wrkInst) {
//		if (wrkInst > 0) {
			getItem().setExternalItemId(wrkInst);
//		} else {
//			getItem().setExternalItemId(null);
//		}
	}

	// I think you can leave this as it's a c.com notion only
	public boolean isSpecialOrderFromScratch() {
		boolean isSpecialOrderFromScratch = false;

		if (getItem().getSpecialOrderTypeCd() != null
				&& (getItem().getSpecialOrderTypeCd().equals(
						SpecialOrderTypeEnum.RIGHT_NOT_FOUND_SPECIAL_ORDER) || (getItem()
						.getSpecialOrderTypeCd()
						.equals(SpecialOrderTypeEnum.MANUAL_SPECIAL_ORDER)))) {
			isSpecialOrderFromScratch = true;
		}

		return isSpecialOrderFromScratch;
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public String getDurationString() {
		if (getDuration() == 0) {
			return DurationEnum.TO_30_DAYS.getDesc();
		} else if (getDuration() == 1) {
			return DurationEnum.TO_180_DAYS.getDesc();
		} else if (getDuration() == 2) {
			return DurationEnum.TO_365_DAYS.getDesc();
		} else if (getDuration() == 3) {
			return DurationEnum.UNLIMITED_DAYS.getDesc();
		}

		return Constants.EMPTY_STRING;

	}

	// The following date calls should migrate to the equivalent item field 
	@Deprecated
	public Date getPublicationStartDate() {
		return getItem().getRightBeginDate();
	}

	@Deprecated
	public void setPublicationStartDate(Date publicationStartDate) {
		getItem().setRightBeginDate(publicationStartDate);
	}

	@Deprecated
	public Date getPublicationEndDate() {
		return getItem().getRightEndDate();
	}

	@Deprecated
	public void setPublicationEndDate(Date publicationEndDate) {
		getItem().setRightEndDate(publicationEndDate);
	}

	public Date getRightStartDate() {
		return getItem().getRightBeginDate();
	}

	public void setRightStartDate(Date rightStartDate) {
		getItem().setRightBeginDate(rightStartDate);
	}

	public Date getRightEndDate() {
		return getItem().getRightEndDate();
	}

	public void setRightEndDate(Date publicationEndDate) {
		getItem().setRightEndDate(publicationEndDate);
	}

	public boolean isContactRightsholder() {
		boolean isContactRightsholder = false;

		if (getItem().getSpecialOrderTypeCd().equals(
				SpecialOrderTypeEnum.CONTACT_RIGHTSHOLDER_SPECIAL_ORDER)) {
			isContactRightsholder = true;
		}

		return isContactRightsholder;
	}

	public boolean isManualSpecialOrder() {
		boolean isManualSpecialOrder = false;

		if (getItem().getSpecialOrderTypeCd().equals(
				SpecialOrderTypeEnum.MANUAL_SPECIAL_ORDER)) {
			isManualSpecialOrder = true;
		}

		return isManualSpecialOrder;
	}

	public String getCustomAuthor() {
		String customAuthor = ItemHelperServices.getItemParmString(getItem(),
				ItemParmEnum.CUSTOM_AUTHOR.name());

		return customAuthor;
	}

	public void setCustomAuthor(String customAuthor) {
		ItemHelperServices.setItemParmString(getItem(),
				ItemParmEnum.CUSTOM_AUTHOR.name(), customAuthor);
	}

	// Migrate to getLicenseeRefNum
	public String getYourReference() {
		return getItem().getLicenseeRefNum();

	}

	// Migreate to getHasVolumePricing
	public String getHasVolPriceTiers() {

		if (getItem().getHasVolumePricing() == null) {
			return ItemConstants.NO_CD;
		} else if (getItem().getHasVolumePricing().booleanValue()==true) {
			return ItemConstants.YES_CD;
		}

		return ItemConstants.NO_CD;
	}

	public void setYourReference(String yourReference) {
		getItem().setLicenseeRefNum(yourReference);
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public String getRepublicationTypeOfUse() {

		if (this.isRepublication()) {
			String republicationTypeOfUse = Constants.EMPTY_STRING;
			long tpuInst = getItem().getExternalTouId();

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

	public String getRepublishInVolEd() {
		String republishInVolEd = ItemHelperServices.getItemParmString(
				getItem(), ItemParmEnum.REPUBLISH_IN_VOL_ED.name());

		return republishInVolEd;
	}

	public void setRepublishInVolEd(String republishInVolEd) {
		ItemHelperServices.setItemParmString(getItem(),
				ItemParmEnum.REPUBLISH_IN_VOL_ED.name(), republishInVolEd);
	}

	public String getTranslated() {
		String isTranslatedYN = ItemHelperServices.getItemParmString(getItem(),
				ItemParmEnum.IS_TRANSLATED.name());

		return isTranslatedYN;
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public void setTranslated(String translated) {
		
		setPricingFieldChanged(ItemHelperServices.isFieldChanged(getItem(), 
				   translated, ItemParmEnum.IS_TRANSLATED.name()));
		
		ItemHelperServices.setItemParmString(getItem(),
				ItemParmEnum.IS_TRANSLATED.name(), translated);
	}

	// I believe this is no longer used in UI and can be deleted
	public String getRepublishSection() {
		String republishSection = "";

		return republishSection;
	}

	// I believe this is no longer used in UI and can be deleted
	public String getRepublishPoNumDtl() {
		String republishPoNumDt = "";

		return republishPoNumDt;
	}

	public Long getJobTicketNumber() {
		return getItem().getJobTicketNumber();
	}

	public Long getExternalDetailId() {
		return getItem().getExternalDetailId();
	}

	public String getExternalOrderId() {
		return getItem().getExternalOrderId();
	}

	public void setExternalDetailId(Long detailId) {
		getItem().setExternalDetailId(detailId);
	}

	public void setJobTicketNumber(Long jobTicket) {
		getItem().setJobTicketNumber(jobTicket);
	}

	// Getters for Adjustments
	// Migrete to getExternalItemStatusCd
	public String getLicenseStatusCd() {
		
		if (getItem().getExternalItemStatusCd() != null) {
			return getItem().getExternalItemStatusCd().toString();
		}

		return null;
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	// Change to getExternalItemStatusCdMsg
	public String getLicenseStatusCdMsg() {
		OdtStatusEnum odtStatusEnum = null;		
		
		if (getItem().getExternalItemStatusCd() != null) {
			odtStatusEnum = OdtStatusEnum.getEnumForOdtStatusCd(getItem().getExternalItemStatusCd().toString());	
			if (odtStatusEnum != null) {
				return odtStatusEnum.getOdtStatusMsg();
			}
		}

		return null;
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public double getRoyalty() {
		BigDecimal royalty = new BigDecimal(0);

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

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public double getRightsholderFee() {
		BigDecimal rightsholderFee = new BigDecimal(0);

		if (getItem().getAllFees().size() > 0) {
			for (ItemFees itemFees : getItem().getAllFees()) {
				if (itemFees.getRightsholderFee() != null) {
					rightsholderFee = rightsholderFee.add(itemFees
							.getRightsholderFee());
				}
			}
		} else {
			// TODO No FEE Exception
			return 0;
		}

		return rightsholderFee.doubleValue();
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public BigDecimal getTotalRightsholderFeeValue() {
		BigDecimal rightsholderFee = new BigDecimal(0);

		if (getItem().getAllFees().size() > 0) {
			for (ItemFees itemFees : getItem().getAllFees()) {
				if (itemFees.getRightsholderFee() != null) {
					rightsholderFee = rightsholderFee.add(itemFees
							.getRightsholderFee());
				}
			}
		} else {
			// TODO No FEE Exception
			return null;
		}

		return rightsholderFee;
	}

	
	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public double getLicenseeFee() {
		BigDecimal licenseeFee = new BigDecimal(0);

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

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public double getDiscount() {
		BigDecimal discount = new BigDecimal(0);

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

	/*
	 * public long getReasonCd() { return _license.getReasonCd(); }
	 * 
	 * public String getReasonDesc() { return _license.getReasonDesc(); }
	 */
	public long getLicenseDetailReferenceID() {
		// TODO Research this one\
		return 0;
		// return _license.getOrderDetailReferenceID();
	}

	public String getRepublishFullArticle() {
		String fullArticle = ItemHelperServices.getItemParmString(getItem(),
				ItemParmEnum.FULL_ARTICLE.name());

		return fullArticle;
	}

	public void setRepublishFullArticle(String fullArticle) {
		
		ItemHelperServices.setItemParmString(getItem(),
				ItemParmEnum.FULL_ARTICLE.name(), fullArticle);
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	// Create and use itemHelperServices.getItemParmPrimativeLong to simplify
	public long getNumberOfCartoons() {
		BigDecimal quantity = ItemHelperServices.getItemParmNumber(getItem(),
				ItemParmEnum.NUM_CARTOONS.name());

		if (quantity != null) {
			return quantity.longValue();
		} else {
			return 0;
		}

	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	// Create and use itemHelperServices.getItemParmPrimativeLong to simplify
	public void setNumberOfCartoons(long quantity) {
		
		setPricingFieldChanged(ItemHelperServices.isFieldChanged(getItem(), 
				   quantity, ItemParmEnum.NUM_CARTOONS.name()));
		
		ItemHelperServices.setItemParmNumber(getItem(),
				ItemParmEnum.NUM_CARTOONS.name(), new BigDecimal(quantity));
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	// Create and use itemHelperServices.getItemParmPrimativeLong to simplify
	public long getNumberOfCharts() {
				
		BigDecimal quantity = ItemHelperServices.getItemParmNumber(getItem(),
				ItemParmEnum.NUM_CHARTS.name());

		if (quantity != null) {
			return quantity.longValue();
		} else {
			return 0;
		}

	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	// Create and use itemHelperServices.getItemParmPrimativeLong to simplify
	public void setNumberOfCharts(long quantity) {

		setPricingFieldChanged(ItemHelperServices.isFieldChanged(getItem(), 
				   quantity, ItemParmEnum.NUM_CHARTS.name()));
		
		ItemHelperServices.setItemParmNumber(getItem(), ItemParmEnum.NUM_CHARTS
				.name(), new BigDecimal(quantity));
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	// Create and use itemHelperServices.getItemParmPrimativeLong to simplify
	public long getNumberOfExcerpts() {
		BigDecimal quantity = ItemHelperServices.getItemParmNumber(getItem(),
				ItemParmEnum.NUM_EXCERPTS.name());

		if (quantity != null) {
			return quantity.longValue();
		} else {
			return 0;
		}
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	// Create and use itemHelperServices.getItemParmPrimativeLong to simplify
	public void setNumberOfExcerpts(long quantity) {
		
		setPricingFieldChanged(ItemHelperServices.isFieldChanged(getItem(), 
				   quantity, ItemParmEnum.NUM_EXCERPTS.name()));
		
		ItemHelperServices.setItemParmNumber(getItem(),
				ItemParmEnum.NUM_EXCERPTS.name(), new BigDecimal(quantity));
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	// Create and use itemHelperServices.getItemParmPrimativeLong to simplify
	public long getNumberOfFigures() {
		BigDecimal quantity = ItemHelperServices.getItemParmNumber(getItem(),
				ItemParmEnum.NUM_FIGURES.name());

		if (quantity != null) {
			return quantity.longValue();
		} else {
			return 0;
		}

	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	// Create and use itemHelperServices.getItemParmPrimativeLong to simplify
	public void setNumberOfFigures(long quantity) {
		
		setPricingFieldChanged(ItemHelperServices.isFieldChanged(getItem(), 
				   quantity, ItemParmEnum.NUM_FIGURES.name()));

		
		ItemHelperServices.setItemParmNumber(getItem(),
				ItemParmEnum.NUM_FIGURES.name(), new BigDecimal(quantity));
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	// Create and use itemHelperServices.getItemParmPrimativeLong to simplify
	public long getNumberOfGraphs() {
		
		BigDecimal quantity = ItemHelperServices.getItemParmNumber(getItem(),
				ItemParmEnum.NUM_GRAPHS.name());

		if (quantity != null) {
			return quantity.longValue();
		} else {
			return 0;
		}

	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	// Create and use itemHelperServices.getItemParmPrimativeLong to simplify
	public void setNumberOfGraphs(long quantity) {

		setPricingFieldChanged(ItemHelperServices.isFieldChanged(getItem(), 
				   quantity, ItemParmEnum.NUM_GRAPHS.name()));
		
		ItemHelperServices.setItemParmNumber(getItem(), ItemParmEnum.NUM_GRAPHS
				.name(), new BigDecimal(quantity));
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	// Create and use itemHelperServices.getItemParmPrimativeLong to simplify
	public long getNumberOfIllustrations() {
		BigDecimal quantity = ItemHelperServices.getItemParmNumber(getItem(),
				ItemParmEnum.NUM_ILLUSTRATIONS.name());

		if (quantity != null) {
			return quantity.longValue();
		} else {
			return 0;
		}

	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	// Create and use itemHelperServices.getItemParmPrimativeLong to simplify
	public void setNumberOfIllustrations(long quantity) {
		
		setPricingFieldChanged(ItemHelperServices.isFieldChanged(getItem(), 
				   quantity, ItemParmEnum.NUM_ILLUSTRATIONS.name()));
		
		ItemHelperServices
				.setItemParmNumber(getItem(), ItemParmEnum.NUM_ILLUSTRATIONS
						.name(), new BigDecimal(quantity));
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	// Create and use itemHelperServices.getItemParmPrimativeLong to simplify
	public long getNumberOfLogos() {
		BigDecimal quantity = ItemHelperServices.getItemParmNumber(getItem(),
				ItemParmEnum.NUM_LOGOS.name());

		if (quantity != null) {
			return quantity.longValue();
		} else {
			return 0;
		}

	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	// Create and use itemHelperServices.getItemParmPrimativeLong to simplify
	public void setNumberOfLogos(long quantity) {

		setPricingFieldChanged(ItemHelperServices.isFieldChanged(getItem(), 
				   quantity, ItemParmEnum.NUM_LOGOS.name()));
		
		ItemHelperServices.setItemParmNumber(getItem(), ItemParmEnum.NUM_LOGOS
				.name(), new BigDecimal(quantity));
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	// Create and use itemHelperServices.getItemParmPrimativeLong to simplify
	public long getNumberOfPhotos() {
		BigDecimal quantity = ItemHelperServices.getItemParmNumber(getItem(),
				ItemParmEnum.NUM_PHOTOS.name());

		if (quantity != null) {
			return quantity.longValue();
		} else {
			return 0;
		}

	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	// Create and use itemHelperServices.getItemParmPrimativeLong to simplify
	public void setNumberOfPhotos(long quantity) {
		
		setPricingFieldChanged(ItemHelperServices.isFieldChanged(getItem(), 
				   quantity, ItemParmEnum.NUM_PHOTOS.name()));

		ItemHelperServices.setItemParmNumber(getItem(), ItemParmEnum.NUM_PHOTOS
				.name(), new BigDecimal(quantity));
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	// Create and use itemHelperServices.getItemParmPrimativeLong to simplify
	public long getNumberOfQuotes() {
		BigDecimal quantity = ItemHelperServices.getItemParmNumber(getItem(),
				ItemParmEnum.NUM_QUOTES.name());

		if (quantity != null) {
			return quantity.longValue();
		} else {
			return 0;
		}

	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	// Create and use itemHelperServices.getItemParmPrimativeLong to simplify
	public void setNumberOfQuotes(long quantity) {
		
		setPricingFieldChanged(ItemHelperServices.isFieldChanged(getItem(), 
				   quantity, ItemParmEnum.NUM_QUOTES.name()));
		
		ItemHelperServices.setItemParmNumber(getItem(), ItemParmEnum.NUM_QUOTES
				.name(), new BigDecimal(quantity));
	}

	// Delete all these "double" versions of fee getters/setters
	@Deprecated
	public void setLicenseeFee(double licenseeFee) {
		if (getItem().getAllFees() == null) {
			Set<ItemFees> allFees = new HashSet<ItemFees>();
			getItem().setAllFees(allFees);
		}
		if (getItem().getAllFees().size() == 0) {
			getItem().getAllFees().add(new ItemFees());
		}
		if (getItem().getAllFees().size() == 1) {
			Iterator<ItemFees> allFeesIterator = getItem().getAllFees()
					.iterator();
			if (allFeesIterator.hasNext()) {
				ItemFees itemFees = allFeesIterator.next();
				itemFees.setLicenseeFee(new BigDecimal(licenseeFee));
			}
		}

	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public void setLicenseeFee(BigDecimal licenseeFee) {
		if (getItem().getAllFees() == null) {
			Set<ItemFees> allFees = new HashSet<ItemFees>();
			getItem().setAllFees(allFees);
		}
		if (getItem().getAllFees().size() == 0) {
			getItem().getAllFees().add(new ItemFees());
		}
		if (getItem().getAllFees().size() == 1) {
			Iterator<ItemFees> allFeesIterator = getItem().getAllFees()
					.iterator();
			if (allFeesIterator.hasNext()) {
				ItemFees itemFees = allFeesIterator.next();
				itemFees.setLicenseeFee(licenseeFee);
			}
		}

	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public BigDecimal getTotalLicenseeFeeValue() {
		BigDecimal licenseeFee = new BigDecimal(0);

		if (getItem().getAllFees().size() > 0) {
			for (ItemFees itemFees : getItem().getAllFees()) {
				if (itemFees.getLicenseeFee() != null) {
					licenseeFee = licenseeFee.add(itemFees.getLicenseeFee());
				}
			}
		} else {
			// TODO No FEE Exception
			return null;
		}

		return licenseeFee.setScale(2, BigDecimal.ROUND_HALF_EVEN);
	}

	
	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public void setLicenseeFeeForRH(Long payeePartyId, BigDecimal licenseeFee) {
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

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public void setDiscount(BigDecimal discount) {
		if (getItem().getAllFees() == null) {
			Set<ItemFees> allFees = new HashSet<ItemFees>();
			getItem().setAllFees(allFees);
		}
		if (getItem().getAllFees().size() == 0) {
			getItem().getAllFees().add(new ItemFees());
		}
		if (getItem().getAllFees().size() == 1) {
			Iterator<ItemFees> allFeesIterator = getItem().getAllFees()
					.iterator();
			if (allFeesIterator.hasNext()) {
				ItemFees itemFees = allFeesIterator.next();
				itemFees.setDiscount(discount);
			}
		}

	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public void setDiscountForRH(Long payeePartyId, BigDecimal discount) {
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
	public void setDiscount(double discount) {
		if (getItem().getAllFees() == null) {
			Set<ItemFees> allFees = new HashSet<ItemFees>();
			getItem().setAllFees(allFees);
		}
		if (getItem().getAllFees().size() == 0) {
			getItem().getAllFees().add(new ItemFees());
		}
		if (getItem().getAllFees().size() == 1) {
			Iterator<ItemFees> allFeesesIterator = getItem().getAllFees()
					.iterator();
			if (allFeesesIterator.hasNext()) {
				ItemFees itemFees = allFeesesIterator.next();
				itemFees.setDiscount(new BigDecimal(discount));
			}
		}

	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public BigDecimal getTotalDiscountValue() {
		BigDecimal discount = new BigDecimal(0);

		if (getItem().getAllFees().size() > 0) {
			for (ItemFees itemFees : getItem().getAllFees()) {
				if (itemFees.getDiscount() != null) {
					discount = discount.add(itemFees.getDiscount());
				}
			}
		} else {
			// TODO No Discount Exception
			return null;
		}
		return discount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
	}
	
	@Deprecated
	public void setRoyalty(double royalty) {
		if (getItem().getAllFees() == null) {
			Set<ItemFees> allFees = new HashSet<ItemFees>();
			getItem().setAllFees(allFees);
		}
		if (getItem().getAllFees().size() == 0) {
			getItem().getAllFees().add(new ItemFees());
		}
		if (getItem().getAllFees().size() == 1) {
			Iterator<ItemFees> allFeesIterator = getItem().getAllFees()
					.iterator();
			if (allFeesIterator.hasNext()) {
				ItemFees itemFees = allFeesIterator.next();
				itemFees.setDistributionPayable(new BigDecimal(royalty));
			}
		}

	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public BigDecimal getTotalDistributionPayableValue() {
		BigDecimal distributionPayable = new BigDecimal(0);

		if (getItem().getAllFees().size() > 0) {
			for (ItemFees itemFees : getItem().getAllFees()) {
				if (itemFees.getDistributionPayable() != null) {
					distributionPayable = distributionPayable.add(itemFees
							.getDistributionPayable());
				}
			}
		} else {
			// TODO No Discount Exception
			return null;
		}

		return distributionPayable.setScale(2, BigDecimal.ROUND_HALF_EVEN);
	}


	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public BigDecimal getDistributionPayableForRH(Long payeePartyId) {
		// return getPermissionRequest().getLicenseeFee();

		if (getItem().getAllFees().size() > 0) {
			for (ItemFees itemFees : getItem().getAllFees()) {
				if (itemFees.getOrigDistPartyId().compareTo(payeePartyId) == 0) {
					if (itemFees.getDistributionPayable() != null) {
						return itemFees.getDistributionPayable();
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

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public void setDistributionPayable(BigDecimal distributionPayable) {
		if (getItem().getAllFees() == null) {
			Set<ItemFees> allFees = new HashSet<ItemFees>();
			getItem().setAllFees(allFees);
		}
		if (getItem().getAllFees().size() == 0) {
			getItem().getAllFees().add(new ItemFees());
		}
		if (getItem().getAllFees().size() == 1) {
			Iterator<ItemFees> allFeesIterator = getItem().getAllFees()
					.iterator();
			if (allFeesIterator.hasNext()) {
				ItemFees itemFees = allFeesIterator.next();
				itemFees.setDistributionPayable(distributionPayable);
			}
		}

	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public void setDistributionPayableForRH(Long payeePartyId,
			BigDecimal distributionPayable) {
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

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public String getDistributionEvent() {

		StringBuffer distributionEvent = new StringBuffer();

		if (getItem().getAllFees().size() > 0) {
			for (ItemFees itemFees : getItem().getAllFees()) {
				if (itemFees.getDistributionEventId() != null) {
					if (distributionEvent.length() > 0
							&& itemFees.getDistributionEventId() != null) {
						distributionEvent.append(", ");
					}
					distributionEvent.append(itemFees.getDistributionEventId()
							.toString());
				}
			}
		} else {
			// TODO No Discount Exception
			return null;
		}
		if (distributionEvent.length()>0) {
			return distributionEvent.toString();
		} else {
			return null;
		}		
	}

	// These next 4 are adjustment reason and are handled in adjItem
	// They are needed to support interface for "old" adjustment app
	// but can be deleted when that app is obsoleted
	public long getReasonCd() {
		// return getPermissionRequest().getReasonCd();
		// TODO Don't remember what this is
		return 0;
	}

	public void setReasonCd(long reasonCd) {
		// TODO Don't remember what this is
		// getPermissionRequest().setReasonCd(reasonCd);
	}

	public String getReasonDesc() {
		// TODO Don't remember what this is
		// return getPermissionRequest().getReasonDesc();
		return null;
	}

	public void setReasonDesc(String reasonDesc) {
		// TODO Don't remember what this is
		// getPermissionRequest().setReasonDesc(reasonDesc);
	}

	public double getRoyaltyComposite() {
		LOGGER.debug("Composite-Royalty: " + getRoyalty() + " plus "
				+ getRightsholderFee());

		return getRoyalty() + getRightsholderFee();
	}

	public BigDecimal getRoyaltyCompositeValue() {
		if (this.isRightsLink() && this.isCanceled()) {
			return new BigDecimal(0).setScale(2,
					BigDecimal.ROUND_HALF_EVEN);
		}
				
		return new BigDecimal(this.getRoyaltyComposite()).setScale(2,
				BigDecimal.ROUND_HALF_EVEN);
	}

	// Need to test but I believe pricing services now sets the isSpecialOrder flag and 
	// the setIsSpecial order flag can be removed
	public void setSpecialOrderLimitsExceeded(boolean specialOrderLimitsExceeded) {
		getItem().setSpecialOrderTypeCd(
				SpecialOrderTypeEnum.LIMITS_EXCEEDED_SPECIAL_ORDER);
		getItem().setIsSpecialOrder(true);
	}
	
	// The following fee getters and setters are only used by c.com so you can leave the logic here	
	public String getBaseFee() {
		
		BigDecimal baseFee = ItemHelperServices.getItemParmNumber(getItem(),
				ItemParmEnum.BASE_FEE.name());
		String baseFeeString = null;

		boolean feesPresent = baseFee != null;

		if (feesPresent) {
			Money baseFeeMoney = new Money(baseFee.doubleValue());
			if (baseFeeMoney != null)
				baseFeeString = WebUtils.formatMoney(baseFeeMoney, false);
		}

		return baseFeeString;
	}

	public String getFlatFee() {

		BigDecimal flatFee = ItemHelperServices.getItemParmNumber(getItem(),
				ItemParmEnum.FLAT_FEE.name());
		String flatFeeString = null;

		boolean feesPresent = flatFee != null;

		if (feesPresent) {
			Money baseFeeMoney = new Money(flatFee.doubleValue());
			if (baseFeeMoney != null)
				flatFeeString = WebUtils.formatMoney(baseFeeMoney, false);
		}

		return flatFeeString;

	}

	public String getPerPageFee() {
		BigDecimal perPageFee = ItemHelperServices.getItemParmNumber(getItem(),
				ItemParmEnum.PER_PAGE_FEE.name());
		String perPageFeeString = null;

		boolean feesPresent = perPageFee != null;

		if (feesPresent) {
			Money baseFeeMoney = new Money(perPageFee.doubleValue());
			if (baseFeeMoney != null)
				perPageFeeString = WebUtils.formatMoney(baseFeeMoney, false);
		}

		return perPageFeeString;
	}

	public String getPerPageFeeMoneyFormat() {
		BigDecimal perPageFee = ItemHelperServices.getItemParmNumber(getItem(),
				ItemParmEnum.PER_PAGE_FEE.name());
		Double perPageFeeDouble;

		if (perPageFee != null) {
			perPageFeeDouble = perPageFee.doubleValue();
			return WebUtils.formatMoney(new Money(perPageFeeDouble), false);
		}

		return null;

	}

	public BigDecimal getPerPageFeeValue() {
		BigDecimal perPageFee = ItemHelperServices.getItemParmNumber(getItem(),
				ItemParmEnum.PER_PAGE_FEE.name());

		return perPageFee;
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
	  
	  public void setEntireBookFeeOverrideValue(BigDecimal entireBookFeeOverrideValue) {
			this._entireBookFeeOverrideValue = entireBookFeeOverrideValue;
		}

		public BigDecimal getEntireBookFeeOverrideValue() {
			return _entireBookFeeOverrideValue;
		}
	  

	public void setPerPageFeeOverrideValue(BigDecimal perPageFeeOverrideValue) {
		this._perPageFeeOverrideValue = perPageFeeOverrideValue;
	}

	public BigDecimal getPerPageFeeOverrideValue() {
		return _perPageFeeOverrideValue;
	}

	
	public String getBaseFeeMoneyFormat() {
		BigDecimal baseFee = ItemHelperServices.getItemParmNumber(getItem(),
				ItemParmEnum.BASE_FEE.name());
		Double baseFeeDouble;

		if (baseFee != null) {
			baseFeeDouble = baseFee.doubleValue();
			return WebUtils.formatMoney(new Money(baseFeeDouble), false);
		}

		return null;

	}

	public BigDecimal getBaseFeeValue() {
		BigDecimal baseFee = ItemHelperServices.getItemParmNumber(getItem(),
				ItemParmEnum.BASE_FEE.name());

		return baseFee;
	}

	public void setBaseFeeOverrideValue(BigDecimal baseFeeOverrideValue) {
		this._baseFeeOverrideValue = baseFeeOverrideValue;
	}

	public BigDecimal getBaseFeeOverrideValue() {
		return _baseFeeOverrideValue;
	}

	
	public String getFlatFeeMoneyFormat() {
		BigDecimal flatFee = ItemHelperServices.getItemParmNumber(getItem(),
				ItemParmEnum.FLAT_FEE.name());
		Double flatFeeDouble;

		if (flatFee != null) {
			flatFeeDouble = flatFee.doubleValue();
			return WebUtils.formatMoney(new Money(flatFeeDouble), false);
		}

		return null;

	}

	public BigDecimal getFlatFeeValue() {
		BigDecimal flatFee = ItemHelperServices.getItemParmNumber(getItem(),
				ItemParmEnum.FLAT_FEE.name());

		return flatFee;
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
		String customVolume = ItemHelperServices.getItemParmString(getItem(),
				ItemParmEnum.CUSTOM_VOLUME.name());

		return customVolume;

	}

	public void setCustomVolume(String customVolume) {
		ItemHelperServices.setItemParmString(getItem(),
				ItemParmEnum.CUSTOM_VOLUME.name(), customVolume);
	}

	public String getCustomEdition() {
		String customEdition = ItemHelperServices.getItemParmString(getItem(),
				ItemParmEnum.CUSTOM_EDITION.name());

		return customEdition;
	}

	public void setCustomEdition(String customEdition) {
		ItemHelperServices.setItemParmString(getItem(),
				ItemParmEnum.CUSTOM_EDITION.name(), customEdition);
	}

	// private void setFees(RightFees fees)
	// {
	// this._fees = fees;
	// }

	// private RightFees getFees()
	// {
	// return _fees;
	// }

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public boolean isSpecialOrderLimitsExceeded() {
		if (getItem().getSpecialOrderTypeCd() != null) {
			if (getItem().getSpecialOrderTypeCd().equals(
					SpecialOrderTypeEnum.LIMITS_EXCEEDED_SPECIAL_ORDER)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	// Replaced by getRightsholderPartyId below
	@Deprecated
	public long getRightsholderInst() {
		long rightsholderInst = INVALID_RIGHTSHOLDER_INST;

		if (getItem().getAllFees() == null) {
			return INVALID_RIGHTSHOLDER_INST;
		}
		if (getItem().getAllFees().size() == 0) {
			return INVALID_RIGHTSHOLDER_INST;
		}
		if (getItem().getAllFees().size() == 1) {
			Iterator<ItemFees> allFeesIterator = getItem().getAllFees()
					.iterator();
			if (allFeesIterator.hasNext()) {
				ItemFees itemFees = allFeesIterator.next();
				if (itemFees.getOrigDistPartyId() != null) {
					return itemFees.getOrigDistPartyId().longValue();
				} else {
					return INVALID_RIGHTSHOLDER_INST;
				}
			}
		}
		if (getItem().getAllFees().size() > 1) {
			return INVALID_RIGHTSHOLDER_INST;
		}
		return INVALID_RIGHTSHOLDER_INST;

	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public Long getRightsholderPartyId() {

		if (getItem().getAllFees() == null) {
			return Long.valueOf(INVALID_RIGHTSHOLDER_INST);
		}
		if (getItem().getAllFees().size() == 0) {
			return Long.valueOf(INVALID_RIGHTSHOLDER_INST);
		}
		if (getItem().getAllFees().size() == 1) {
			Iterator<ItemFees> allFeesIterator = getItem().getAllFees()
					.iterator();
			if (allFeesIterator.hasNext()) {
				ItemFees itemFees = allFeesIterator.next();
				return itemFees.getOrigDistPartyId();
			}
		}
		if (getItem().getAllFees().size() > 1) {
			return Long.valueOf(INVALID_RIGHTSHOLDER_INST);
		}
		return Long.valueOf(INVALID_RIGHTSHOLDER_INST);

	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public Long getRightsholderPtyInst() {

		if (getItem().getAllFees() == null) {
			return Long.valueOf(INVALID_RIGHTSHOLDER_INST);
		}
		if (getItem().getAllFees().size() == 0) {
			return Long.valueOf(INVALID_RIGHTSHOLDER_INST);
		}
		if (getItem().getAllFees().size() == 1) {
			Iterator<ItemFees> allFeesIterator = getItem().getAllFees()
					.iterator();
			if (allFeesIterator.hasNext()) {
				ItemFees itemFees = allFeesIterator.next();
				return itemFees.getOrigDistPtyInst();
			}
		}
		if (getItem().getAllFees().size() > 1) {
			return Long.valueOf(INVALID_RIGHTSHOLDER_INST);
		}
		return Long.valueOf(INVALID_RIGHTSHOLDER_INST);

	}

	// Migrate to getItemAvailabilityCd
	public String getRightPermissionType() {

		return getItem().getItemAvailabilityCd();

		/*
		 * boolean rightPresent = getLicense().getRight() != null; String
		 * rightPermType = null;
		 * 
		 * if( rightPresent ) { rightPermType =
		 * getLicense().getRight().getPermission().getPermissionValueCode(); }
		 * 
		 * return rightPermType;
		 */
	}

	// I believe this can be removed, need to follow the code references
	public Right getSvcRight() {
		return null;
	}

	public Long getWrWorkInst() {
		// if this is just a Standard Work object, then we need to create a
		// WrStandardWork object and replace this in
		// the permissionRequest object

//		if (getItem().getItemSourceKey() != null) {
			return getItem().getItemSourceKey();
//		} else {
//			return 0L;
//		}

		// return PurchasablePermissionFactory.getWRWorkInst(getWorkInst());
	}

	// 2009-10-23 MSJ
	// Yeah, my fingers are everywhere. More changes for summit,
	// metadata being passed along from search to the order process.
	// Hoping the folks working on orders will roll the new metadata
	// into this code in a better way, somehow.

	public String getSeries() {
		return ItemHelperServices.getItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.SERIES.name());

	}

	public void setSeries(String series) {
		ItemHelperServices.setItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.SERIES.name(), series);
	}

	public String getSeriesNumber() {
		return ItemHelperServices.getItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.SERIES_NUMBER.name());

	}

	public void setSeriesNumber(String seriesNumber) {
		ItemHelperServices.setItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.SERIES_NUMBER.name(), seriesNumber);
	}

	public String getPublicationType() {
		return ItemHelperServices.getItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.PUBLICATION_TYPE.name());

	}

	public void setPublicationType(String publicationType) {
		ItemHelperServices.setItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.PUBLICATION_TYPE.name(),
				publicationType);
	}

	public String getCountry() {
		return ItemHelperServices.getItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.COUNTRY.name());

	}

	public void setCountry(String country) {
		ItemHelperServices.setItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.COUNTRY.name(), country);
	}

	public String getLanguage() {
		return ItemHelperServices.getItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.LANGUAGE.name());

	}

	public void setLanguage(String language) {
		ItemHelperServices.setItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.LANGUAGE.name(), language);
	}

	public String getIdnoLabel() {
		return ItemHelperServices.getItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.IDNO_LABEL.name());

	}

	public void setIdnoLabel(String idnoLabel) {
		ItemHelperServices.setItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.IDNO_LABEL.name(), idnoLabel);
	}

	public String getIdnoTypeCd() {
		return ItemHelperServices.getItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.IDNO_TYPE_CD.name());

	}

	public void setIdnoTypeCd(String idnoTypeCd) {
		ItemHelperServices.setItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.IDNO_TYPE_CD.name(), idnoTypeCd);
	}

	public String getPages() {
		return ItemHelperServices.getItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.PAGES.name());

	}

	public void setPages(String pages) {		
		ItemHelperServices.setItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.PAGES.name(), pages);
	}

	public int getOrderDataSource() {
		return OrderDataSourceEnum.OMS.getOrderDataSourceId().intValue();
	}

	public String getOrderDataSourceDisplay() {
		return OrderDataSourceEnum.OMS.name();
	}

	public Long getExternalItemId() {
		return getItem().getExternalItemId();
		// return getPermissionRequest().getWork().getWrkInst();
	}

	public void setExternalItemId(Long wrkInst) {
		getItem().setExternalItemId(wrkInst);
	}

	public String getItemDescription() {
		return getItem().getItemDescription();
	}

	public void setItemDescription(String itemDescription) {
		getItem().setItemDescription(itemDescription);
	}

	public String getItemSubDescription() {
		return getItem().getItemSubDescription(); // Article Title
	}

	public void setItemSubDescription(String itemSubDescription) {
		getItem().setItemSubDescription(itemSubDescription); // Article Title
	}

	public String getItemSourceCd() {
		return getItem().getItemSourceCd();
	}

	public void setItemSourceCd(String itemSourceCd) {
		getItem().setItemSourceCd(itemSourceCd);
	}

	public Long getItemSourceKey() {
		return getItem().getItemSourceKey(); // WR Work Inst
	}

	public void setItemSourceKey(Long itemSourceKey) {
		getItem().setItemSourceKey(itemSourceKey); // WR Work Inst
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
		return ItemHelperServices.getItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.GRANULAR_WORK_AUTHOR.name());
	}

	public void setGranularWorkAuthor(String granularWorkAuthor) {
		ItemHelperServices.setItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.GRANULAR_WORK_AUTHOR.name(),
				granularWorkAuthor);
	}

	public String getGranularWorkDoi() {
		return ItemHelperServices.getItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.GRANULAR_WORK_DOI.name());
	}

	public void setGranularWorkDoi(String granularWorkDoi) {
		ItemHelperServices.setItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.GRANULAR_WORK_DOI.name(),
				granularWorkDoi);
	}

	public String getGranularWorkStartPage() {
		return ItemHelperServices.getItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.GRANULAR_WORK_START_PAGE.name());
	}

	public void setGranularWorkStartPage(String granularWorkStartPage) {
		ItemHelperServices.setItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.GRANULAR_WORK_START_PAGE.name(),
				granularWorkStartPage);
	}

	public String getGranularWorkEndPage() {
		return ItemHelperServices.getItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.GRANULAR_WORK_END_PAGE.name());
	}

	public void setGranularWorkEndPage(String granularWorkEndPage) {
		ItemHelperServices.setItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.GRANULAR_WORK_END_PAGE.name(),
				granularWorkEndPage);
	}

	public String getGranularWorkPageRange() {
		return ItemHelperServices.getItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.GRANULAR_WORK_PAGE_RANGE.name());
	}

	public void setGranularWorkPageRange(String granularWorkPageRange) {
		ItemHelperServices.setItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.GRANULAR_WORK_PAGE_RANGE.name(),
				granularWorkPageRange);
	}

	public Date getGranularWorkPublicationDate() {
		return ItemHelperServices.getItemDescriptionParmDate(getItem(),
				ItemDescriptionParmEnum.GRANULAR_WORK_PUBLICATION_DATE.name());
	}

	public void setGranularWorkPublicationDate(Date granularWorkPublicationDate) {
		ItemHelperServices.setItemDescriptionParmDate(getItem(),
				ItemDescriptionParmEnum.GRANULAR_WORK_PUBLICATION_DATE.name(),
				granularWorkPublicationDate);
	}

	public String getGranularWorkVolume() {
		return ItemHelperServices.getItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.GRANULAR_WORK_VOLUME.name());
	}

	public void setGranularWorkVolume(String granularWorkVolume) {
		ItemHelperServices.setItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.GRANULAR_WORK_VOLUME.name(),
				granularWorkVolume);
	}

	public String getGranularWorkIssue() {
		return ItemHelperServices.getItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.GRANULAR_WORK_ISSUE.name());
	}

	public void setGranularWorkIssue(String granularWorkIssue) {
		ItemHelperServices.setItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.GRANULAR_WORK_ISSUE.name(),
				granularWorkIssue);
	}

	public String getGranularWorkNumber() {
		return ItemHelperServices.getItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.GRANULAR_WORK_NUMBER.name());
	}

	public void setGranularWorkNumber(String granularWorkNumber) {
		ItemHelperServices.setItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.GRANULAR_WORK_NUMBER.name(),
				granularWorkNumber);
	}

	public String getGranularWorkSeason() {
		return ItemHelperServices.getItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.GRANULAR_WORK_SEASON.name());
	}

	public void setGranularWorkSeason(String granularWorkSeason) {
		ItemHelperServices.setItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.GRANULAR_WORK_SEASON.name(),
				granularWorkSeason);
	}

	public String getGranularWorkQuarter() {
		return ItemHelperServices.getItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.GRANULAR_WORK_QUARTER.name());
	}

	public void setGranularWorkQuarter(String granularWorkQuarter) {
		ItemHelperServices.setItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.GRANULAR_WORK_QUARTER.name(),
				granularWorkQuarter);
	}

	public String getGranularWorkWeek() {
		return ItemHelperServices.getItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.GRANULAR_WORK_WEEK.name());
	}

	public void setGranularWorkWeek(String granularWorkWeek) {
		ItemHelperServices.setItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.GRANULAR_WORK_WEEK.name(),
				granularWorkWeek);
	}

	public String getGranularWorkSection() {
		return ItemHelperServices.getItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.GRANULAR_WORK_SECTION.name());
	}

	public void setGranularWorkSection(String granularWorkSection) {
		ItemHelperServices.setItemDescriptionParmString(getItem(),
				ItemDescriptionParmEnum.GRANULAR_WORK_SECTION.name(),
				granularWorkSection);
	}

	public Date getLastUpdatedDate() {
		if (getItem().getUpdateDate() != null) {
			return getItem().getUpdateDate();
		} else {
			return getItem().getCreateDate();
		}
	}

	public String getRhRefNum() {
		return getItem().getRhRefNum();
	}

	public void setRhRefNum(String rhRefNum) {
		getItem().setRhRefNum(rhRefNum);
	}

	public String getRightSourceCd() {
		return getItem().getRightSourceCd();
	}

	public String getResearchUserIdentifier() {
		return getItem().getResearchUserIdentifier();
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	// Need to move calcItemStatusDisplay out to helper class too
	public void setItemStatusCd(String itemStatusCd) {
		for (ItemStatusEnum itemStatusEnum : ItemStatusEnum.values()) {
			if (itemStatusEnum.name().equalsIgnoreCase(itemStatusCd)) {
				getItem().setItemStatusCd(itemStatusEnum);
				calcItemStatusDisplay();
				break;
			}
		}
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public void setItemStatusQualifierCd(String itemStatusQualifier) {
		if (itemStatusQualifier == null) {
			getItem().setItemStatusQualifier(null);
			calcItemStatusDisplay();
			return;
		}

		for (ItemStatusQualifierEnum itemStatusQualifierEnum : ItemStatusQualifierEnum
				.values()) {
			if (itemStatusQualifierEnum.name().equalsIgnoreCase(
					itemStatusQualifier)) {
				getItem().setItemStatusQualifier(itemStatusQualifierEnum);
				calcItemStatusDisplay();
				break;
			}
		}
	}

	public String getItemStatusCd() {
		if (getItem().getItemStatusCd() != null) {
			return getItem().getItemStatusCd().name();
		}
		return null;
	}

	public void setItemStatusCd(ItemStatusEnum itemStatus) {
		getItem().setItemStatusCd(itemStatus);
	}

	public void setItemStatusQualifier(
			ItemStatusQualifierEnum itemStatusQualifier) {
		getItem().setItemStatusQualifier(itemStatusQualifier);
	}

	public Date getItemStatusDate() {
		return getItem().getItemStatusDtm();
	}

	public void setItemStatusDate(Date itemStatusDate) {
		getItem().setItemStatusDtm(itemStatusDate);
	}

	public String getItemStatusDescription() {
		if (getItem().getItemStatusCd() != null) {
			return getItem().getItemStatusCd().getDesc();
		}
		return null;
	}

	public String getItemStatusQualifierCd() {
		if (getItem().getItemStatusQualifier().name() != null) {
			return getItem().getItemStatusQualifier().name();
		}
		return null;
	}

	public String getItemStatusQualifierDescription() {
		if (getItem().getItemStatusQualifier() != null) {
			return getItem().getItemStatusQualifier().getDesc();
		}
		return null;
	}

	public String getItemStatusInternalDisplay() {
		// calcItemStatusDisplay();
		return _itemStatusInternalDisplay;
	}

	public Long getExternalItemStatusCd() {
		return getItem().getExternalItemStatusCd();
	}
	
	public void setExternalItemStatusCd(Long externalItemStatusCd) {
		getItem().setExternalItemStatusCd(externalItemStatusCd);
	}
	
	// Refactoring: Move logic out to OrderHelper.jar	
	public void calcItemStatusDisplay() {
		_itemStatusExternalDisplay = null;
		_itemStatusInternalDisplay = null;
		_itemCycleDisplay = null;
		_itemErrorDescription = null;

		if (getItem().getItemStatusCd() == null) {
			return;
		}

		if (getItem().getItemStatusCd().equals(ItemStatusEnum.INVOICE_READY)) {
			if (getItem().getItemAvailabilityCd() != null
					&& getItem().getItemAvailabilityCd()
							.equals(
									ItemAvailabilityEnum.DENY
											.getStandardPermissionCd())) {
				_itemStatusInternalDisplay = "Awaiting Invoicing";
				if (getItem().getItemStatusQualifier() == null) {
					_itemCycleDisplay = "Denied by Rightsholder";
				} else {
					if (getItem().getItemStatusQualifier().equals(
							ItemStatusQualifierEnum.DENIED_BY_LCN_NO_RESPONSE)) {
						_itemCycleDisplay = "Licensee Not Responding";
					} else if (getItem().getItemStatusQualifier().equals(
							ItemStatusQualifierEnum.DENIED_BY_RH_NO_RESPONSE)) {
						_itemCycleDisplay = "Denied by Rightsholder";
					} else {
						_itemCycleDisplay = "Denied by Rightsholder";
					}
				}
			} else {
				if (getItem().getIsAdjusted() != null && getItem().getIsAdjusted().booleanValue()) {
					_itemStatusInternalDisplay = "Awaiting Adjustment Invoicing";					
				} else if (getItem().getItemStatusQualifier() == null) {
					_itemStatusInternalDisplay = "Awaiting Invoicing";
				} else {
					if (getItem().getItemStatusQualifier().equals(
							ItemStatusQualifierEnum.INVOICE_READY_CREDIT_CARD)) {
						_itemStatusInternalDisplay = "Awaiting Invoicing";
					}
					if (getItem().getItemStatusQualifier().equals(
							ItemStatusQualifierEnum.INVOICE_READY_INVOICE)) {
						_itemStatusInternalDisplay = "Awaiting Invoicing";
					}
				}
			}
		}

		if (getItem().getItemStatusCd().equals(
				ItemStatusEnum.INVOICING_IN_PROGRESS)) {
			if (getItem().getItemAvailabilityCd() != null
					&& getItem().getItemAvailabilityCd()
							.equals(
									ItemAvailabilityEnum.DENY
											.getStandardPermissionCd())) {
				_itemStatusInternalDisplay = "Invoicing in Progress";
				if (getItem().getItemStatusQualifier() == null) {
					_itemCycleDisplay = "Denied by Rightsholder";
				} else {
					if (getItem().getItemStatusQualifier().equals(
							ItemStatusQualifierEnum.DENIED_BY_LCN_NO_RESPONSE)) {
						_itemCycleDisplay = "Licensee Not Responding";
					} else if (getItem().getItemStatusQualifier().equals(
							ItemStatusQualifierEnum.DENIED_BY_RH_NO_RESPONSE)) {
						_itemCycleDisplay = "Denied by Rightsholder";
					} else {
						_itemCycleDisplay = "Denied by Rightsholder";
					}
				}
			} else {
				if (getItem().getIsAdjusted() != null && getItem().getIsAdjusted().booleanValue()) {
					_itemStatusInternalDisplay = "Adjustment Invoicing in Progress";
				} else 	if (getItem().getItemStatusQualifier() == null) {
					_itemStatusInternalDisplay = "Invoicing in Progress";
				} else {
					if (getItem().getItemStatusQualifier().equals(
							ItemStatusQualifierEnum.INVOICE_READY_CREDIT_CARD)) {
						_itemStatusInternalDisplay = "Invoicing in Progress";
					}
					if (getItem().getItemStatusQualifier().equals(
							ItemStatusQualifierEnum.INVOICE_READY_INVOICE)) {
						_itemStatusInternalDisplay = "Invoicing in Progress";
					}
				}
			}
		}

		if (getItem().getItemStatusCd().equals(ItemStatusEnum.INVOICED)) {

			if (getItem().getItemAvailabilityCd() != null
					&& getItem().getItemAvailabilityCd()
							.equals(
									ItemAvailabilityEnum.DENY
											.getStandardPermissionCd())) {
				_itemStatusInternalDisplay = "Invoiced";
				if (getItem().getItemStatusQualifier() == null) {
					_itemCycleDisplay = "Denied by Rightsholder";
				} else {
					if (getItem().getItemStatusQualifier().equals(
							ItemStatusQualifierEnum.DENIED_BY_LCN_NO_RESPONSE)) {
						_itemCycleDisplay = "Licensee Not Responding";

					} else if (getItem().getItemStatusQualifier().equals(ItemStatusQualifierEnum.DENIED_BY_RH_NO_RESPONSE)) {
						_itemCycleDisplay = "Denied by Rightsholder";
					} else {
						_itemCycleDisplay = "Denied by Rightsholder";
					}
				}
			} else {
				if (getItem().getItemStatusQualifier() == null) {
					_itemStatusInternalDisplay = "Invoiced";
				} else {
					if (getItem().getItemStatusQualifier().equals(
							ItemStatusQualifierEnum.INVOICED)) {
						_itemStatusInternalDisplay = "Invoiced";
					}
					if (getItem().getItemStatusQualifier().equals(
							ItemStatusQualifierEnum.PAID)) {
						_itemStatusInternalDisplay = "Invoiced";
					}
					if (getItem().getItemStatusQualifier().equals(
							ItemStatusQualifierEnum.PAID_CREDIT_CARD)) {
						_itemStatusInternalDisplay = "Invoiced";
					}
					if (getItem()
							.getItemStatusQualifier()
							.equals(
									ItemStatusQualifierEnum.PAID_INVOICE_BY_CREDIT_CARD)) {
						_itemStatusInternalDisplay = "Invoiced";
					}
				}
			}
		}

		if (getItem().getItemStatusCd().equals(
				ItemStatusEnum.AWAITING_LCN_REPLY)) {
			_itemStatusInternalDisplay = "Requires Additional Licensee Information";
			_itemCycleDisplay = "Awaiting Licensee Reply";
		}

		if (getItem().getItemStatusCd().equals(
				ItemStatusEnum.AWAITING_LCN_CONFIRM)) {
			_itemStatusInternalDisplay = "Awaiting Licensee";
			
		}

		if (getItem().getItemStatusCd().equals(ItemStatusEnum.AWAITING_RH)) {
			_itemStatusInternalDisplay = "Requires Rightsholder Response";
			if (getItem().getItemStatusQualifier() == null) {
				_itemCycleDisplay = null;
			} else {
				if (getItem().getItemStatusQualifier().equals(
						ItemStatusQualifierEnum.FIRST_REQUEST)) {
					_itemCycleDisplay = "Awaiting Rightsholder Reply/1st Request";
				}
				if (getItem().getItemStatusQualifier().equals(
						ItemStatusQualifierEnum.SECOND_REQUEST)) {
					_itemCycleDisplay = "Awaiting Rightsholder Reply/2nd Request";
				}
				if (getItem().getItemStatusQualifier().equals(
						ItemStatusQualifierEnum.FINAL_REQUEST)) {
					_itemCycleDisplay = "Awaiting Rightsholder Reply/Final Request";
				}
			}
		}

		if (getItem().getItemStatusCd().equals(
				ItemStatusEnum.AWAITING_FULFILLMENT)) {
			_itemStatusInternalDisplay = "Awaiting Fulfillment";
		}

		if (getItem().getItemStatusCd()
				.equals(ItemStatusEnum.AWAITING_RESEARCH)) {
			_itemStatusInternalDisplay = "Requires Research";
			if (getItem().getSpecialOrderTypeCd() == null) {
				_itemErrorDescription = null;
			} else if (getItem().getSpecialOrderTypeCd().equals(
					SpecialOrderTypeEnum.CONTACT_RIGHTSHOLDER_SPECIAL_ORDER)) {
				_itemErrorDescription = "Further Research Needed";
			} else if (getItem().getSpecialOrderTypeCd().equals(
					SpecialOrderTypeEnum.MANUAL_SPECIAL_ORDER)) {
				_itemErrorDescription = "Work Not Found";
			} else if (getItem().getSpecialOrderTypeCd().equals(
					SpecialOrderTypeEnum.LIMITS_EXCEEDED_SPECIAL_ORDER)) {
				_itemErrorDescription = "Missing Total Pages";
			} else if (getItem().getSpecialOrderTypeCd().equals(
					SpecialOrderTypeEnum.RIGHT_NOT_FOUND_SPECIAL_ORDER)) {
				_itemErrorDescription = "Right Not Found";
			} else if (getItem().getSpecialOrderTypeCd().equals(
					SpecialOrderTypeEnum.FEE_FORMULA_INCALCULABLE)) {
				_itemErrorDescription = "Fee Formula Incalculable";
			} else {
				_itemErrorDescription = "Refer to TF for more information";
			}
		}

		if (getItem().getItemStatusCd().equals(ItemStatusEnum.CANCELLED)) {
			if (!getItem().getProductCd().equals(ProductEnum.RL.name()) &&
				!getItem().getProductCd().equals(ProductEnum.RLR.name())) {
				_itemStatusInternalDisplay = "Canceled";				
			} else {
				if (getItem().getItemAvailabilityCd() != null
						&& getItem().getItemAvailabilityCd().equals(ItemAvailabilityEnum.DENY.getStandardPermissionCd())) {
					_itemStatusInternalDisplay = "Invoiced";
					_itemCycleDisplay = "Denied by Rightsholder";
				} else {
					_itemStatusInternalDisplay = "Canceled";								
				}
			}
		}

		if (getItem().getItemStatusCd().equals(ItemStatusEnum.DISTRIBUTED)) {
			_itemStatusInternalDisplay = "Distributed";
		}

		if (getItem().getItemStatusCd().equals(
				ItemStatusEnum.NOT_COMPLETELY_ENTERED)) {
			// NOTE: I think we only use this at the Order Header level, not
			// needed here.
		}

		if (getItem().getItemStatusCd().equals(
				ItemStatusEnum.NOT_COMPLETELY_ENTERED_HALT)) {
			// NOTE: I think we only use this at the Order Header level, not
			// needed here.
		}

		if (getItem().getItemStatusCd().equals(ItemStatusEnum.UNDEFINED)) {
			_itemStatusInternalDisplay = "Undefined";
		}

	}

	public String getItemStatusExternalDisplay() {
		// calcItemStatusDisplay();
		return _itemStatusExternalDisplay;
	}

	public String getItemCycleDisplay() {
		// calcItemStatusDisplay();
		return _itemCycleDisplay;
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public String getItemErrorDescriptionDisplay() {
		// calcItemStatusDisplay();
		OdtStatusEnum odtStatusEnum = null;		
		
		if (getItem().getExternalItemStatusCd() != null) {
			odtStatusEnum = OdtStatusEnum.getEnumForOdtStatusCd(getItem().getExternalItemStatusCd().toString());	
			if (odtStatusEnum != null && odtStatusEnum.getItemStatusQualifier().equals(ItemStatusQualifierEnum.ERROR.name())) {
				return odtStatusEnum.getOdtStatusMsg();
			}
		}

		return null;
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public String getPaymentTypeDisplay() {

		String paymentTypeDisplay = null;

		if (this.getPaymentMethodCd().equalsIgnoreCase(
				ItemConstants.PAYMENT_METHOD_INVOICE)) {
			return ItemConstants.PAYMENT_TYPE_INVOICE;
		} else if (this.getPaymentMethodCd().equalsIgnoreCase(
				ItemConstants.PAYMENT_METHOD_CREDIT_CARD)) {
			return ItemConstants.PAYMENT_TYPE_CREDIT_CARD;
		}

		return null;
		
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public String getRightsholderAccount() {

		StringBuffer RhAccountsBuf = new StringBuffer();
		for (ItemFees itemFees : getItem().getAllFees()) {
			if (RhAccountsBuf.length() > 0
					&& !itemFees.getOrigDistAccount().isEmpty()) {
				RhAccountsBuf.append(", ");
			}
			if (itemFees.getOrigDistAccount() != null) {
				RhAccountsBuf.append(itemFees.getOrigDistAccount());
			}
		}

		if (RhAccountsBuf.length() > 0) {
			return RhAccountsBuf.toString();
		} else {
			return null;
		}
	}

	public Date getOverrideDate() {
		return getItem().getOverrideDtm();
	}

	public void setOverrideDate(Date overrideDate) {
		getItem().setOverrideDtm(overrideDate);
	}

	public String getOverrideAvailabilityCd() {
		return getItem().getOverrideAvailabilityCd();
	}

	public void setOverrideAvailabilityCd(String overrideAvailabilityCd) {
		getItem().setOverrideAvailabilityCd(overrideAvailabilityCd);
	}
	
	public String getOverrideAvailabilityDescription() {
		ItemAvailabilityEnum itemAvailabilityEnum = ItemAvailabilityEnum
				.getEnumForItemAvailabilityCd(getItem()
						.getOverrideAvailabilityCd());
		if (itemAvailabilityEnum == null) {
			return null;
		}
		return itemAvailabilityEnum.getOrderLabel();
	}

	public String getOverrideComment() {
		return getItem().getOverrideComment();
	}

	public void setOverrideComment(String overrideComment) {
		getItem().setOverrideComment(overrideComment);
	}
	
	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public BigDecimal getRightsholderPercent() {
		BigDecimal rightsholderPercent = new BigDecimal(0);
		BigDecimal totalRoyalty = null;
		BigDecimal basePrice = null;
		
		if (this.getTotalRightsholderFeeValue()!= null &&
			this.getTotalDistributionPayableValue() != null &&
			this.getTotalRightsholderFeeValue().compareTo(new BigDecimal(0)) > 0 &&
			this.getTotalDistributionPayableValue().compareTo(new BigDecimal(0)) > 0 ) {
			totalRoyalty = this.getTotalDistributionPayableValue().add(this.getTotalRightsholderFeeValue());
			rightsholderPercent = this.getTotalDistributionPayableValue().divide(totalRoyalty, 2, BigDecimal.ROUND_HALF_EVEN);
			rightsholderPercent = rightsholderPercent.multiply(new BigDecimal(100));		
		}
		

		if (rightsholderPercent  != null){
			rightsholderPercent =rightsholderPercent.setScale(0, BigDecimal.ROUND_HALF_EVEN); 
		}
		return rightsholderPercent;
	}

	public String getCategoryCd() {
		return getItem().getCategoryCd();
	}

	public String getCategoryName() {
		return getItem().getCategoryName();
	}

	public Long getCategoryId() {
		return getItem().getCategoryId();
	}

	public void setCategoryId(Long categoryId) {
		getItem().setCategoryId(categoryId);
	}

	public void setCategoryName(String categoryName) {
		getItem().setCategoryName(categoryName);
	}

	public String getProductAndCategoryName() {
		return getItem().getProductCd() + " / " + getItem().getCategoryName();
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public BigDecimal getHardCopyCost() {
		BigDecimal hardCopyCost = new BigDecimal(0);

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

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public void setHardCopyCost(BigDecimal hardCopyCost) {
		if (getItem().getAllFees() == null) {
			Set<ItemFees> allFees = new HashSet<ItemFees>();
			getItem().setAllFees(allFees);
		}
		if (getItem().getAllFees().size() == 0) {
			getItem().getAllFees().add(new ItemFees());
		}
		if (getItem().getAllFees().size() == 1) {
			Iterator<ItemFees> allFeesIterator = getItem().getAllFees()
					.iterator();
			if (allFeesIterator.hasNext()) {
				ItemFees itemFees = allFeesIterator.next();
				itemFees.setHardCopyCost(hardCopyCost);
			}
		}

	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public BigDecimal getPriceAdjustment() {
		BigDecimal priceAdjustment = new BigDecimal(0);

		if (getItem().getAllFees().size() > 0) {
			for (ItemFees itemFees : getItem().getAllFees()) {
				if (itemFees.getPriceAdjustment() != null) {
					priceAdjustment = priceAdjustment.add(itemFees
							.getPriceAdjustment());
				}
			}
		} else {
			return priceAdjustment;
		}

		return priceAdjustment;
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public void setPriceAdjustment(BigDecimal priceAdjustment) {
		if (getItem().getAllFees() == null) {
			Set<ItemFees> allFees = new HashSet<ItemFees>();
			getItem().setAllFees(allFees);
		}
		if (getItem().getAllFees().size() == 0) {
			getItem().getAllFees().add(new ItemFees());
		}
		if (getItem().getAllFees().size() == 1) {
			Iterator<ItemFees> allFeesIterator = getItem().getAllFees()
					.iterator();
			if (allFeesIterator.hasNext()) {
				ItemFees itemFees = allFeesIterator.next();
				itemFees.setPriceAdjustment(priceAdjustment);
			}
		}

	}

	// Refactoring: Move all these shipping fees to OrderHelper.jar following pattern described above	
	public BigDecimal getShippingFee1() {
		BigDecimal shippingFee1 = new BigDecimal(0);

		if (getItem().getAllFees().size() > 0) {
			for (ItemFees itemFees : getItem().getAllFees()) {
				if (itemFees.getShippingFee1() != null) {
					shippingFee1 = shippingFee1.add(itemFees.getShippingFee1());
				}
			}
		} else {
			return shippingFee1;
		}

		return shippingFee1;
	}

	public void setShippingFee1(BigDecimal shippingFee1) {
		if (getItem().getAllFees() == null) {
			Set<ItemFees> allFees = new HashSet<ItemFees>();
			getItem().setAllFees(allFees);
		}
		if (getItem().getAllFees().size() == 0) {
			getItem().getAllFees().add(new ItemFees());
		}
		if (getItem().getAllFees().size() == 1) {
			Iterator<ItemFees> allFeesIterator = getItem().getAllFees()
					.iterator();
			if (allFeesIterator.hasNext()) {
				ItemFees itemFees = allFeesIterator.next();
				itemFees.setShippingFee1(shippingFee1);
			}
		}

	}

	public BigDecimal getShippingFee2() {
		BigDecimal shippingFee2 = new BigDecimal(0);

		if (getItem().getAllFees().size() > 0) {
			for (ItemFees itemFees : getItem().getAllFees()) {
				if (itemFees.getShippingFee2() != null) {
					shippingFee2 = shippingFee2.add(itemFees.getShippingFee2());
				}
			}
		} else {
			return shippingFee2;
		}

		return shippingFee2;
	}

	public void setShippingFee2(BigDecimal shippingFee2) {
		if (getItem().getAllFees() == null) {
			Set<ItemFees> allFees = new HashSet<ItemFees>();
			getItem().setAllFees(allFees);
		}
		if (getItem().getAllFees().size() == 0) {
			getItem().getAllFees().add(new ItemFees());
		}
		if (getItem().getAllFees().size() == 1) {
			Iterator<ItemFees> allFeesIterator = getItem().getAllFees()
					.iterator();
			if (allFeesIterator.hasNext()) {
				ItemFees itemFees = allFeesIterator.next();
				itemFees.setShippingFee2(shippingFee2);
			}
		}

	}

	public BigDecimal getShippingFee3() {
		BigDecimal shippingFee3 = new BigDecimal(0);

		if (getItem().getAllFees().size() > 0) {
			for (ItemFees itemFees : getItem().getAllFees()) {
				if (itemFees.getShippingFee3() != null) {
					shippingFee3 = shippingFee3.add(itemFees.getShippingFee3());
				}
			}
		} else {
			return shippingFee3;
		}

		return shippingFee3;
	}

	public void setShippingFee3(BigDecimal shippingFee3) {
		if (getItem().getAllFees() == null) {
			Set<ItemFees> allFees = new HashSet<ItemFees>();
			getItem().setAllFees(allFees);
		}
		if (getItem().getAllFees().size() == 0) {
			getItem().getAllFees().add(new ItemFees());
		}
		if (getItem().getAllFees().size() == 1) {
			Iterator<ItemFees> allFeesIterator = getItem().getAllFees()
					.iterator();
			if (allFeesIterator.hasNext()) {
				ItemFees itemFees = allFeesIterator.next();
				itemFees.setShippingFee3(shippingFee3);
			}
		}

	}

	public BigDecimal getShippingFee4() {
		BigDecimal shippingFee4 = new BigDecimal(0);

		if (getItem().getAllFees().size() > 0) {
			for (ItemFees itemFees : getItem().getAllFees()) {
				if (itemFees.getShippingFee4() != null) {
					shippingFee4 = shippingFee4.add(itemFees.getShippingFee4());
				}
			}
		} else {
			return shippingFee4;
		}

		return shippingFee4;
	}

	public void setShippingFee4(BigDecimal shippingFee4) {
		if (getItem().getAllFees() == null) {
			Set<ItemFees> allFees = new HashSet<ItemFees>();
			getItem().setAllFees(allFees);
		}
		if (getItem().getAllFees().size() == 0) {
			getItem().getAllFees().add(new ItemFees());
		}
		if (getItem().getAllFees().size() == 1) {
			Iterator<ItemFees> allFeesIterator = getItem().getAllFees()
					.iterator();
			if (allFeesIterator.hasNext()) {
				ItemFees itemFees = allFeesIterator.next();
				itemFees.setShippingFee4(shippingFee4);
			}
		}

	}

	public BigDecimal getShippingFee5() {
		BigDecimal shippingFee5 = new BigDecimal(0);

		if (getItem().getAllFees().size() > 0) {
			for (ItemFees itemFees : getItem().getAllFees()) {
				if (itemFees.getShippingFee5() != null) {
					shippingFee5 = shippingFee5.add(itemFees.getShippingFee5());
				}
			}
		} else {
			return shippingFee5;
		}

		return shippingFee5;
	}

	public void setShippingFee5(BigDecimal shippingFee5) {
		if (getItem().getAllFees() == null) {
			Set<ItemFees> allFees = new HashSet<ItemFees>();
			getItem().setAllFees(allFees);
		}
		if (getItem().getAllFees().size() == 0) {
			getItem().getAllFees().add(new ItemFees());
		}
		if (getItem().getAllFees().size() == 1) {
			Iterator<ItemFees> allFeesIterator = getItem().getAllFees()
					.iterator();
			if (allFeesIterator.hasNext()) {
				ItemFees itemFees = allFeesIterator.next();
				itemFees.setShippingFee5(shippingFee5);
			}
		}

	}

	public BigDecimal getShippingFee6() {
		BigDecimal shippingFee6 = new BigDecimal(0);

		if (getItem().getAllFees().size() > 0) {
			for (ItemFees itemFees : getItem().getAllFees()) {
				if (itemFees.getShippingFee6() != null) {
					shippingFee6 = shippingFee6.add(itemFees.getShippingFee6());
				}
			}
		} else {
			return shippingFee6;
		}

		return shippingFee6;
	}

	public void setShippingFee6(BigDecimal shippingFee6) {
		if (getItem().getAllFees() == null) {
			Set<ItemFees> allFees = new HashSet<ItemFees>();
			getItem().setAllFees(allFees);
		}
		if (getItem().getAllFees().size() == 0) {
			getItem().getAllFees().add(new ItemFees());
		}
		if (getItem().getAllFees().size() == 1) {
			Iterator<ItemFees> allFeesIterator = getItem().getAllFees()
					.iterator();
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

	public void setTfWksInst(Long tfWksInst) {
		getItem().setTfWksInst(tfWksInst);
	}

	public Long getTfWksInst() {
		return getItem().getTfWksInst();
	}

	public List<Address> getShippingAddress() {

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

		// Get # of Shipping addresses
		for (ItemParm itemParm2 : item.getItemParms().values()) {
			if (itemParm2.getParmName().equalsIgnoreCase("NUMSHIPLOCATIONS")) {
				numLoc = itemParm2.getParmValue();
			}

		}

		if (numLoc == "") {
			return null;
		}

		for (int i = 0; i < Integer.valueOf(numLoc); i++) {

			line1_parm_name = line1_parm + (i + 1);
			line2_parm_name = line2_parm + (i + 1);
			line3_parm_name = line3_parm + (i + 1);
			city_parm_name = city_parm + (i + 1);
			state_parm_name = state_parm + (i + 1);
			zip_parm_name = zip_parm + (i + 1);
			country_parm_name = country_parm + (i + 1);

			for (ItemParm itemParm : item.getItemParms().values()) {
				if (itemParm.getParmName().equalsIgnoreCase(line1_parm_name)) {
					// getItemRequestParmFromItemParm(itemRequestParm,
					// itemParm);
					line1 = itemParm.getParmValue();
				} else if (itemParm.getParmName().equalsIgnoreCase(
						line2_parm_name)) {
					line2 = itemParm.getParmValue();
				} else if (itemParm.getParmName().equalsIgnoreCase(
						line3_parm_name)) {
					line3 = itemParm.getParmValue();
				} else if (itemParm.getParmName().equalsIgnoreCase(
						city_parm_name)) {
					city1 = itemParm.getParmValue();
				} else if (itemParm.getParmName().equalsIgnoreCase(
						state_parm_name)) {
					state1 = itemParm.getParmValue();
				} else if (itemParm.getParmName().equalsIgnoreCase(
						country_parm_name)) {
					country1 = itemParm.getParmValue();
				} else if (itemParm.getParmName().equalsIgnoreCase(
						zip_parm_name)) {
					zip1 = itemParm.getParmValue();
				}

			}

			Country cntry = new Country();
			cntry.setName(country1);

			Address address = new Address(line1, line2, line3, "", city1,
					state1, zip1, cntry);

			addresses.add(address);

		}

		return addresses;

	}

	public BigDecimal getPerLogoFee() {
		return ItemHelperServices.getItemParmNumber(getItem(),
				ItemParmEnum.PER_LOGO.name());
	}

	public BigDecimal getPerGraphFee() {
		return ItemHelperServices.getItemParmNumber(getItem(),
				ItemParmEnum.PER_GRAPH.name());
	}

	public BigDecimal getPerCartoonFee() {
		return ItemHelperServices.getItemParmNumber(getItem(),
				ItemParmEnum.PER_CARTOON.name());
	}

	public BigDecimal getPerExcerptFee() {
		return ItemHelperServices.getItemParmNumber(getItem(),
				ItemParmEnum.PER_EXCERPT.name());
	}

	public BigDecimal getPerQuoteFee() {
		return ItemHelperServices.getItemParmNumber(getItem(),
				ItemParmEnum.PER_QUOTE.name());
	}

	public BigDecimal getPerChartFee() {
		return ItemHelperServices.getItemParmNumber(getItem(),
				ItemParmEnum.PER_CHART.name());
	}

	public BigDecimal getPerPhotographFee() {
		return ItemHelperServices.getItemParmNumber(getItem(),
				ItemParmEnum.PER_PHOTOGRAPH.name());
	}

	public BigDecimal getPerIllustrationFee() {
		return ItemHelperServices.getItemParmNumber(getItem(),
				ItemParmEnum.PER_ILLUSTRATION.name());
	}

	public BigDecimal getPerFigureFee() {
		return ItemHelperServices.getItemParmNumber(getItem(),
				ItemParmEnum.PER_FIGURE.name());
	}

	public BigDecimal getMaxRoyaltyFee() {
		return ItemHelperServices.getItemParmNumber(getItem(),
				ItemParmEnum.MAXIMUM_ROYALTY_FEE.name());
	}

	public BigDecimal getPerArticleAuthorFee() {
		return ItemHelperServices.getItemParmNumber(getItem(),
				ItemParmEnum.PER_ARTICLE_AUTHOR.name());
	}

	public BigDecimal getPerArticleNonAuthorFee() {
		return ItemHelperServices.getItemParmNumber(getItem(),
				ItemParmEnum.PER_ARTICLE_NON_AUTHOR.name());
	}

	public BigDecimal getTo30DaysFee() {
		return ItemHelperServices.getItemParmNumber(getItem(),
				ItemParmEnum.TO_30_DAYS_FEE.name());
	}

	public BigDecimal getTo180DaysFee() {
		return ItemHelperServices.getItemParmNumber(getItem(),
				ItemParmEnum.TO_180_DAYS_FEE.name());
	}

	public BigDecimal getTo365DaysFee() {
		return ItemHelperServices.getItemParmNumber(getItem(),
				ItemParmEnum.TO_365_DAYS_FEE.name());
	}

	public BigDecimal getUnlimitedDaysFee() {
		return ItemHelperServices.getItemParmNumber(getItem(),
				ItemParmEnum.UNLIMITED_DAYS_FEE.name());
	}

	public BigDecimal getTo49RecipientsFee() {
		return ItemHelperServices.getItemParmNumber(getItem(),
				ItemParmEnum.TO_49_RECIPIENTS_FEE.name());
	}

	public BigDecimal getTo249RecipientsFee() {
		return ItemHelperServices.getItemParmNumber(getItem(),
				ItemParmEnum.TO_249_RECIPIENTS_FEE.name());
	}

	public BigDecimal getTo499RecipientsFee() {
		return ItemHelperServices.getItemParmNumber(getItem(),
				ItemParmEnum.TO_499_RECIPIENTS_FEE.name());
	}

	public BigDecimal getTo500pRecipientsFee() {
		return ItemHelperServices.getItemParmNumber(getItem(),
				ItemParmEnum.TO_500P_RECIPIENTS_FEE.name());
	}
	
	public Payment getPayment() {
		return _payment;
	}

	public void setPayment(Payment payment) {
		this._payment = payment;
	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public String getPaymentMethodCd() {
		if (getPayment() != null) {
			return getPayment().getPaymentMethodCd();
		}
		
		if (getItem().getItemStatusCd() != null && 
			getItem().getItemStatusCd().equals(ItemStatusEnum.AWAITING_FULFILLMENT) &&
			getItem().getPayLaterProfileIdentifier() != null) {
			return ItemConstants.PAYMENT_METHOD_CREDIT_CARD;
		}

		return ItemConstants.PAYMENT_METHOD_INVOICE;

	}

	// Refactoring: Move logic out to OrderHelper.jar following pattern described above	
	public String getCurrencyType() {
		if (getPayment() == null) {
			return ItemConstants.CURRENCY_CODE_USD;
		}

		return getPayment().getCurrencyType();
	}

	public String getCcAuthNo() {
		if (getPayment() == null) {
			return null;
		}
		return getPayment().getCcAuthNo();
	}

	public BigDecimal getCcTrxId() {
		if (getPayment() == null) {
			return null;
		}
		return getPayment().getCcTrxId();
	}

	public Date getCcTrxDate() {
		if (getPayment() == null) {
			return null;
		}
		return getPayment().getCcTrxDate();
	}

	public BigDecimal getExchangeRate() {
		if (getPayment() == null) {
			return new BigDecimal(1);
		}
		return getPayment().getExchangeRate();
	}

	public Date getExchangeDate() {
		if (getPayment() == null) {
			return null;
		}
		return getPayment().getExchangeDate();
	}

	public String getMerchantRefId() {
		if (getPayment() == null) {
			return null;
		}
		return getPayment().getMerchantRefId();
	}

	public BigDecimal getUsdTotal() {
		if (getPayment() == null) {
			return null;
		}
		return getPayment().getUsdTotal();
	}

	public BigDecimal getCurrencyPaidTotal() {
		if (getPayment() == null) {
			return null;
		}
		return getPayment().getCurrencyPaidTotal();
		//TODO is this correct? If there are multiple items, this is the total of all
	}

	public Long getCccProfileId() {
		if (getPayment() == null) {
			return null;
		}
		return getPayment().getCcProfileId();
	}

	public String getPaymentProfileIdentifier() {
		if (getPayment() == null) {
			return null;
		}
		return getPayment().getPaymentProfileIdentifier();
	}

	public String getPreviewPDFUrl(){
	   	  if(getItem().getExternalRightId()!=null && getItem().getExternalRightId().compareTo(2L)==0 && getItem().getItemParms().containsKey("PREVIEWPDFURL")){
	  		  return getItem().getItemParms().get("PREVIEWPDFURL").getParmValue();
	   	  }
	    	  return null;
	      }

	public boolean isShowAdjustedValues() {
		return _showAdjustedValues;
	}

	public void setShowAdjustedValues(boolean showAdjustedValues) {
		this._showAdjustedValues = showAdjustedValues;
	}

	public boolean isRequiresAcademicRepin() {
		return _requiresAcademicRepin;
	}

	public void setRequiresAcademicRepin(boolean academicRepin) {
		_requiresAcademicRepin = academicRepin;
	}
	

	public String getOrderRefNumber(){
	   	  if( getItem().getItemParms().containsKey("PONUMBER")){
	  		  return getItem().getItemParms().get("PONUMBER").getParmValue();
	   	  }
	    	  return null;
	      }

	
	public boolean isAvailableToCopy() {
			if (getItem().getItemAvailabilityCd() != null){
				if(getItem().getItemAvailabilityCd().equals(ItemAvailabilityEnum.DENY.getStandardPermissionCd()) ||
						getItem().getItemAvailabilityCd().equals(ItemAvailabilityEnum.CONTACT_RH_DIRECTLY.getStandardPermissionCd()) ||
								getItem().getItemAvailabilityCd().equals(ItemAvailabilityEnum.HOLD_PENDING.getStandardPermissionCd())){
					return false;
				}
				
			}
			
			return true;
		
	}

	public boolean isPricingFieldChanged() {
		return _pricingFieldChanged;
	}

	public void setPricingFieldChanged(boolean fieldChanged) {
		if (fieldChanged == true) {
			_pricingFieldChanged = fieldChanged;
		}
	}
	
	public boolean getLicenseeRequestedEntireWork() {
  	  return ItemHelperServices.getItemParmBoolean(getItem(), ItemParmEnum.LCN_REQUESTED_ENTIRE_WORK.name());
    }
	
    public void setLicenseeRequestedEntireWork(boolean licenseeEntireWork){
      setPricingFieldChanged(ItemHelperServices.isFieldChanged(getItem(), 
    		  licenseeEntireWork, ItemParmEnum.LCN_REQUESTED_ENTIRE_WORK.name()));
    	
  	  ItemHelperServices.setItemParmBoolean(getItem(), ItemParmEnum.LCN_REQUESTED_ENTIRE_WORK.name(),licenseeEntireWork);
  	  
  	  this.setRequiresAcademicRepin(true);
    }

	public void setOrderSourceCd(String orderSourceCd) {
		this._orderSourceCd = orderSourceCd;
	}

	public String getOrderSourceCd() {
		return _orderSourceCd;
	}
	
	public void setDeclineReasonCd(DeclineReasonEnum declineCd) {
		getItem().setDeclineReasonCd(declineCd);
	}

	public String getDeclineReasonCd() {
		String result = null;
		if (getItem() != null && getItem().getDeclineReasonCd() != null) {
			result = getItem().getDeclineReasonCd().name();
		}
		return result;
	}
	
	public String getDeclineReason() {
		String result = null;
		if ( getItem() != null && getItem().getDeclineReasonCd() != null) {
			result = getItem().getDeclineReasonCd().getDeclineReason();
		}
		return result;
	}
	
	public String getPaymentMethod() {
		
		if (getItem().getPaymentId() != null) {
			
			Payment payment = ServiceLocator.getOrderService().getPaymentById(new OrderConsumerContext(), getItem().getPaymentId());
			
			if (payment.getLastFourCc() != null) {
				return "CC ending in " + payment.getLastFourCc().toString();
			}
		}
		else {
			if (getItem().getItemStatusCd().equals(ItemStatusEnum.INVOICE_READY)) {
				return ItemConstants.PAYMENT_TYPE_INVOICE;
			}
		}
		
		return null ;
	}

	public Date getBaseInvoiceDate() {
		Date result = null;
		if (_item != null) {
			result = _item.getBaseInvoiceDate();
		}
		return result;
	}
	public void setBaseInvoiceDate(Date baseInvoiceDate) {
		if (_item != null) {
			_item.setBaseInvoiceDate(baseInvoiceDate);
		}
	}
 	   
}
