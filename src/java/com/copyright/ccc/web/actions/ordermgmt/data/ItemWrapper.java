package com.copyright.ccc.web.actions.ordermgmt.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.copyright.ccc.business.data.OrderLicense;
import com.copyright.ccc.business.services.BusinessProfitEnum;
import com.copyright.ccc.business.services.ItemAvailabilityEnum;
import com.copyright.ccc.business.services.ItemConstants;
import com.copyright.ccc.business.services.OrderDataSourceEnum;
import com.copyright.ccc.business.services.ProductEnum;
import com.copyright.ccc.business.services.TranslatedEnum;
import com.copyright.ccc.web.actions.ordermgmt.util.MiscUtils;
import com.copyright.ccc.web.transaction.TransactionConstants;
import com.copyright.ccc.web.util.WebUtils;
import com.copyright.workbench.i18n.Money;

public class ItemWrapper implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public ItemWrapper ( OrderLicense item ) {
		super();
		this.item = item;
	}
	private int detailIndex = 0;
	private OrderLicense item;
	private OrderLicense preAdjustmentItem;
	private ConfirmationWrapper myConfirmation = null;	
	private BundleWrapper myBundle;
	
	private List<ProcessMessage> processMessages = new ArrayList<ProcessMessage>();
		
	private EditItemWrapper lastEdit = new EditItemWrapper();
	private boolean toBeSaved = false;
	private boolean toBeReturned = false;
	private boolean updatePricingOnly = false;
	
	public boolean isHasBundle() {
		return myBundle != null;
	}
	
	public void initOrderSource() {
		if (this.getMyConfirmation() != null) {
			item.setOrderSourceCd(this.getMyConfirmation().getConfirmation().getOrderSourceCd());
		}
	}
	
	public long getBundleId() {
		if ( isHasBundle() ) {
			return getMyBundle().getBundle().getBundleId();
		} else {
			return -1L;
		}
	}
	
	public long getConfNumber() {
		if ( getMyConfirmation() != null ) {
			if ( getMyConfirmation().getConfirmation() != null ) {
				return getMyConfirmation().getConfirmation().getConfirmationNumber();
			}
		}
		return -1L;
	}
	
	public boolean isRL() {
		if (item.getProductCd() != null && ProductEnum.RL.name().equals(item.getProductCd()))
			return true;
		
		return false;
	}
	
	public boolean isRLR() {
		if (item.getProductCd() != null && ProductEnum.RLR.name().equals(item.getProductCd()))
			return true;
		
		return false;
	}
	
	public boolean isRightslink(){
		if (isRLR() || isRL())
			return true;
	
		return false;
	}
	
	public boolean isUsesForeignCurrency() {
		if (item.getCurrencyType() == null)
			return false;
		
		boolean result = !(ItemConstants.CURRENCY_CODE_USD.equals(item.getCurrencyType()));
		return result;
	}
	
	/*public String getDurationLabel() {
		if (item.getDuration() == 0) {
            return DurationEnum.TO_30_DAYS.getDesc();
		} 
		if (item.getDuration() == 1) {
            return DurationEnum.TO_180_DAYS.getDesc();
		} 
		if (item.getDuration() == 2) {
            return DurationEnum.TO_365_DAYS.getDesc();
		}   
		if (item.getDuration() == 3) {
            return DurationEnum.UNLIMITED_DAYS.getDesc();
		}
		
		return "Per undefined duration";
		
		//String durStr = item.getDurationString();
		//if (durStr == null || durStr.isEmpty())
		//	return "Per undefined duration";
		//else
		//	return durStr;
	} */
	
	public String getDurationFeeFormatted() {
		if (item.getOverrideAvailabilityCd() != null &&
			item.getOverrideAvailabilityCd().equals(ItemAvailabilityEnum.PURCHASE.getStandardPermissionCd())) {
			return formatMoney(item.getDurationFeeOverrideValue());			
		} else {
			return formatMoney(getFeePerDuration());
		}
	}
	
	public BigDecimal getFeePerDuration() {
		if (item.getDuration() == 0) {
			return item.getTo30DaysFee();
		} 
		if (item.getDuration() == 1) {
			return item.getTo180DaysFee();
		} 
		if (item.getDuration() == 2) {
			return item.getTo365DaysFee();
		}   
		if (item.getDuration() == 3) {
			return item.getUnlimitedDaysFee();
		}
		
		return null;
		
		
		//if (item.getDurationString() == DurationEnum.TO_30_DAYS.getDesc())
		//	return item.getTo30DaysFee();
		//if (item.getDurationString() == DurationEnum.TO_180_DAYS.getDesc())
		//	return item.getTo180DaysFee();
		//if (item.getDurationString() == DurationEnum.TO_365_DAYS.getDesc())
		//	return item.getTo365DaysFee();
		//if (item.getDurationString() == DurationEnum.UNLIMITED_DAYS.getDesc())
		//	return item.getUnlimitedDaysFee();
		//		
		//return null;
	}
	
	public BigDecimal getFeePerNumRecipients() {
		Long numRec = item.getNumberOfRecipients();
		if (numRec == null) //shouldn't happen -- if this method is called, numRec should be set
			return null;
		
		if (numRec >=1 && numRec < 50)
			return item.getTo49RecipientsFee();
		if (numRec >=50 && numRec < 250)
			return item.getTo249RecipientsFee();
		if (numRec >=250 && numRec < 500)
			return item.getTo499RecipientsFee();
		if (numRec >= 500)
			return item.getTo500pRecipientsFee();
		//never happens
		return null;
	}
	
	public String getNumRecipientsFeeFormatted() {
		if (item.getOverrideAvailabilityCd() != null && item.getOverrideAvailabilityCd().equals(ItemAvailabilityEnum.PURCHASE.getStandardPermissionCd())) {
			return formatMoney(item.getRecipientsFeeOverrideValue());			
		} else {
			return formatMoney(getFeePerNumRecipients());
		}
	}
	
	public String getNumRecipientsLabel() {
		Long numRec = item.getNumberOfRecipients();
		if (numRec == null)
			return "<default label>";
		if (numRec >=1 && numRec < 50)
			return "1-49 Recip";
		if (numRec >=50 && numRec < 250)
			return "50-249 Recip";
		if (numRec >=250 && numRec < 500)
			return "250-499 Recip";
		if (numRec >= 500)
			return "500+ Recip";
		//never happens
		return null;
	}
	
	public boolean getContentSelectedPages() {
		if (item.isRepublication() && 
				item.getTypeOfContentDescription().equals(TransactionConstants.CONTENT_SELECTED_PAGES))
			return true;
		
		return false;
	}
	
	public boolean getContentTextType() {
		
    	String rlsTypeOfContentDescription;
    	if (item.getProductCd() != null && item.getProductCd().equals(ProductEnum.RLS.name())) {
    		rlsTypeOfContentDescription = item.getTypeOfContentDescription();
    	} else {
    		rlsTypeOfContentDescription = "";
    	}

		
		if (rlsTypeOfContentDescription.equals(TransactionConstants.CONTENT_FULL_ARTICLE_CHAPTER))
			return true;
		if (rlsTypeOfContentDescription.equals(TransactionConstants.CONTENT_SELECTED_PAGES))
			return true;
		if (rlsTypeOfContentDescription.equals(TransactionConstants.CONTENT_EXCERPT))
			return true;
		if (rlsTypeOfContentDescription.equals(TransactionConstants.CONTENT_QUOTATION))
			return true;
		
		return false;
	}
	
	public BigDecimal getPerContentItemFee() {
	
    	String rlsTypeOfContentDescription;
    	if (item.getProductCd() != null && item.getProductCd().equals(ProductEnum.RLS.name())) {
    		rlsTypeOfContentDescription = item.getTypeOfContentDescription();
    	} else {
    		rlsTypeOfContentDescription = "";
    	}
		
		if (rlsTypeOfContentDescription.equals(TransactionConstants.CONTENT_CARTOONS))
			return item.getPerCartoonFee();
		if (rlsTypeOfContentDescription.equals(TransactionConstants.CONTENT_CHART))
			return item.getPerChartFee();
		if (rlsTypeOfContentDescription.equals(TransactionConstants.CONTENT_LOGOS))
			return item.getPerLogoFee();
		if (rlsTypeOfContentDescription.equals(TransactionConstants.CONTENT_FIGURE_DIAGRAM_TABLE))
			return item.getPerFigureFee();	
		if (rlsTypeOfContentDescription.equals(TransactionConstants.CONTENT_ILLUSTRATION))
			return item.getPerIllustrationFee();	
		if (rlsTypeOfContentDescription.equals(TransactionConstants.CONTENT_GRAPH))
			return item.getPerGraphFee();	
		if (rlsTypeOfContentDescription.equals(TransactionConstants.CONTENT_QUOTATION))
			return item.getPerQuoteFee();	
		if (rlsTypeOfContentDescription.equals(TransactionConstants.CONTENT_PHOTOGRAPH))
			return item.getPerPhotographFee();	
		if (rlsTypeOfContentDescription.equals(TransactionConstants.CONTENT_EXCERPT))
			return item.getPerExcerptFee();	
		if (rlsTypeOfContentDescription.equals(TransactionConstants.CONTENT_SELECTED_PAGES))
			return item.getPerPageFeeValue();	
		if (rlsTypeOfContentDescription.equals(TransactionConstants.CONTENT_FULL_ARTICLE_CHAPTER) && !item.isSubmitterAuthor()) 
			return item.getPerArticleNonAuthorFee();	
		if (rlsTypeOfContentDescription.equals(TransactionConstants.CONTENT_FULL_ARTICLE_CHAPTER) && item.isSubmitterAuthor()) 
			return item.getPerArticleAuthorFee();	
		
		//all other cases -- shouldn't ever happen
		return null;
	}
	
	public String getPerContentLabel() {

    	String rlsTypeOfContentDescription;
    	if (item.getProductCd() != null && item.getProductCd().equals(ProductEnum.RLS.name())) {
    		rlsTypeOfContentDescription = item.getTypeOfContentDescription();
    	} else {
    		rlsTypeOfContentDescription = "";
    	}
		
		if (rlsTypeOfContentDescription.equals(TransactionConstants.CONTENT_CARTOONS))
			return "Per Cartoon";
		if (rlsTypeOfContentDescription.equals(TransactionConstants.CONTENT_CHART))
			return "Per Chart";
		if (rlsTypeOfContentDescription.equals(TransactionConstants.CONTENT_LOGOS))
			return "Per Logo";		
		if (rlsTypeOfContentDescription.equals(TransactionConstants.CONTENT_FIGURE_DIAGRAM_TABLE))
			return "Per Figure";
		if (rlsTypeOfContentDescription.equals(TransactionConstants.CONTENT_ILLUSTRATION))
			return "Per Illustration";
		if (rlsTypeOfContentDescription.equals(TransactionConstants.CONTENT_GRAPH))
			return "Per Graph";
		if (rlsTypeOfContentDescription.equals(TransactionConstants.CONTENT_QUOTATION))
			return "Per Quotation";
		if (rlsTypeOfContentDescription.equals(TransactionConstants.CONTENT_PHOTOGRAPH))
			return "Per Photo";
		if (rlsTypeOfContentDescription.equals(TransactionConstants.CONTENT_EXCERPT))
			return "Per Excerpt";
		if (rlsTypeOfContentDescription.equals(TransactionConstants.CONTENT_SELECTED_PAGES))
			return "Per Page";
		if (item.isSubmitterAuthor() && rlsTypeOfContentDescription.equals(TransactionConstants.CONTENT_FULL_ARTICLE_CHAPTER)) 
			return "Article Auth";
		if (!item.isSubmitterAuthor() && rlsTypeOfContentDescription.equals(TransactionConstants.CONTENT_FULL_ARTICLE_CHAPTER)) 
			return "Article Non-auth";

		
		//all other cases -- shouldn't ever happen
		return null;
		
	}
	
	public String getTotalLicenseeFeeValueFormatted() {
		return formatMoney(item.getTotalLicenseeFeeValue());
	}
	
	public String getTotalDiscountValueFormatted() {
		return formatMoney(item.getTotalDiscountValue());
	}
	
	public String getRoyaltyCompositeValueFormatted() {
		return formatMoney(item.getRoyaltyCompositeValue());
	}
	
	public String getPreAdjustmentTotalLicenseeFeeValueFormatted() {
		if (preAdjustmentItem != null) {
			return formatMoney(preAdjustmentItem.getTotalLicenseeFeeValue());
		}
		return null;
	}
	
	public String getPreAdjustmentTotalDiscountValueFormatted() {
		if (preAdjustmentItem != null) {
			return formatMoney(preAdjustmentItem.getTotalDiscountValue());
		}
		return null;
	}
	
	public String getPreAdjustmentRoyaltyCompositeValueFormatted() {
		if (preAdjustmentItem != null) {
			return formatMoney(preAdjustmentItem.getRoyaltyCompositeValue());
		}
		return null;
	}
	
	public String getPreAdjustmentPriceFormatted() {
		if (preAdjustmentItem != null) {
			return preAdjustmentItem.getPrice();
		}
		return null;
	}
	
	public String getPreAdjustmentPriceDifferenceFormatted() {
		if (preAdjustmentItem != null) {
			BigDecimal priceDifference = item.getTotalPriceValue().subtract(preAdjustmentItem.getTotalPriceValue());
			return formatMoney(priceDifference);
		}
		return null;
	}
	
	
	private String formatMoney(BigDecimal val) {
		if (val == null)
			return null;
		
		return WebUtils.formatMoney(new Money(val.doubleValue()),  false);
	}
	
	public String getFlatFeeFormatted() {
		if (item.getOverrideAvailabilityCd() != null && item.getOverrideAvailabilityCd().equals(ItemAvailabilityEnum.PURCHASE.getStandardPermissionCd())) {
			return formatMoney(item.getFlatFeeOverrideValue());			
		} else {
			return formatMoney(item.getFlatFeeValue());
		}
	
	}
	
	public String getEntireBookFeeFormatted() {
		if (item.getOverrideAvailabilityCd() != null && item.getOverrideAvailabilityCd().equals(ItemAvailabilityEnum.PURCHASE.getStandardPermissionCd())) {
			return formatMoney(item.getEntireBookFeeOverrideValue());			
		} else {
			return formatMoney(item.getEntireBookFeeValue());
		}
	
	}
	
	public String getBaseFeeFormatted() {
		if (item.getOverrideAvailabilityCd() != null && item.getOverrideAvailabilityCd().equals(ItemAvailabilityEnum.PURCHASE.getStandardPermissionCd())) {
			return formatMoney(item.getBaseFeeOverrideValue());			
		} else {
			return formatMoney(item.getBaseFeeValue());
		}
	}
	
	public String getMaxRoyaltyFormatted() {
		if (item.getOverrideAvailabilityCd() != null && item.getOverrideAvailabilityCd().equals(ItemAvailabilityEnum.PURCHASE.getStandardPermissionCd())) {
			return formatMoney(item.getMaximumRoyaltyFeeOverrideValue());			
		} else {
			return formatMoney(item.getMaxRoyaltyFee());
		}
	}
	
	public String getPerContentFeeFormatted() {
		if (item.getOverrideAvailabilityCd() != null && item.getOverrideAvailabilityCd().equals(ItemAvailabilityEnum.PURCHASE.getStandardPermissionCd())) {
			return formatMoney(item.getPerContentFeeOverrideValue());			
		} else {
			return formatMoney(getPerContentItemFee());
		}
	}
	
	public String getPerPageFeeFormatted() {
		if (item.getOverrideAvailabilityCd() != null && item.getOverrideAvailabilityCd().equals(ItemAvailabilityEnum.PURCHASE.getStandardPermissionCd())) {
			return formatMoney(item.getPerPageFeeOverrideValue());			
		} else {
			return formatMoney(item.getPerPageFeeValue());
		}	
	}

	public String getDetailDateFMT() {
		return MiscUtils.formatMMddyyyyDateSlashed(item.getCreateDate());
	}
	public ConfirmationWrapper getMyConfirmation() {
		return myConfirmation;
	}

	public void setMyConfirmation(ConfirmationWrapper myConfirmation) {
		this.myConfirmation = myConfirmation;
	}

	public BundleWrapper getMyBundle() {
		return myBundle;
	}

	public void setMyBundle(BundleWrapper myBundle) {
		this.myBundle = myBundle;
	}

	public OrderLicense getItem() {
		return item;
	}

	public void setItem(OrderLicense item) {
		this.item = item;
	}

	public OrderLicense getPreAdjustmentItem() {
		return preAdjustmentItem;
	}

	public void setPreAdjustmentItem(OrderLicense item) {
		this.preAdjustmentItem = item;
	}
	
	public Long getProductCodeKey() {
		if ( getItem() != null ) {
			if ( StringUtils.isNotEmpty( getItem().getProductCd()) ) {
				return ProductEnum.getEnumForName(getItem().getProductCd()).getProductSourceKey();
			}
		}
		return null;
	}
	
	//*************  MISSING ITEM FIELDS *************/

	private MissingItemFields missing = new MissingItemFields();

	public MissingItemFields getMissing() {
		return missing;
	}

	public void setMissing(MissingItemFields missing) {
		this.missing = missing;
	}

	//*************  PROCESS MESSAGES *************/

	public List<ProcessMessage> getProcessMessages() {
		return processMessages;
	}

	public void setProcessMessages(List<ProcessMessage> processMessages) {
		this.processMessages = processMessages;
	}
	
	public boolean isHasProcessMessageErrors() {
		if ( getProcessMessages() != null && !getProcessMessages().isEmpty() ) {
			return ProcessMessage.hasErrors(getProcessMessages());
		}
		return false;
	}
	public boolean isHasProcessMessages() {
		if ( getProcessMessages() != null && !getProcessMessages().isEmpty() ) {
			return true;
		}
		return false;
	}
	public boolean isHasLastEdit() {
		if ( getLastEdit() != null ) {
			return getLastEdit().isToBeReturned();
		}
		return false;
	}


	public EditItemWrapper getLastEdit() {
		return lastEdit;
	}

	public void setLastEdit(EditItemWrapper lastEdit) {
		this.lastEdit = lastEdit;
	}

	public boolean isToBeSaved() {
		return toBeSaved;
	}

	public void setToBeSaved(boolean toBeSaved) {
		this.toBeSaved = toBeSaved;
	}

	public boolean isToBeReturned() {
		return toBeReturned;
	}

	public void setToBeReturned(boolean toBeReturned) {
		this.toBeReturned = toBeReturned;
	}
	
	public boolean isInvoiced() {
		return getItem().isInvoiced();
	}
	
	public boolean isCanceled() {
		if ( getItem() != null ) {
			return getItem().isCanceled();
		}
		return false;
	}
	
	public boolean isEditable() {
		if ( getItem() != null ) {
			return getItem().isEditable();
		}
		return false;
	}
	
	public boolean isCancelable() {
		if ( getItem() != null ) {
			return getItem().isCancelable();
		}
		return false;
	}
	
	//seems like add detail is irrelevant in this context. SHould be on a bundle or a confirmation
	public boolean isAddDetailAllowed() {
		if ( getItem() != null ) {
			return isStoredInOMS()
			    && !isInvoiced();
		}
		return false;
	}
	
	public boolean isRePinAllowed() {

		// TODO BobD 7/9/2010 - Pam suggested this be not allowed for now...
		
//		if ( getItem() != null ) {
//			return isStoredInOMS()
//			    && !isInvoiced();
//		}
		return false;
	}
	
	public boolean isOverrideAllowed() {
		// Not currently implemented 7/7/2010 BobD
		return false;
	}
	
	public boolean isStoredInTF() {
		if ( getItem() != null ) {
			if ( getItem().getOrderDataSourceDisplay().equals(OrderDataSourceEnum.TF.name())) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isStoredInOMS() {
		if ( getItem() != null ) {
			if ( getItem().getOrderDataSourceDisplay().equals(OrderDataSourceEnum.OMS.name())) {
				return true;
			}
		}
		return false;		
	}
	public boolean isPermissionSourceTF() {
		if ( getItem() != null ) {
			if ( StringUtils.isNotEmpty( getItem().getRightSourceCd() ) ) {
				if ( getItem().getRightSourceCd().equalsIgnoreCase("TF") ) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isAdjusted() {
		if ( getItem() != null && getItem().isAdjusted()) {
			return true;
		}
		return false;
	}

	public int getDetailIndex() {
		return detailIndex;
	}

	public void setDetailIndex(int detailIndex) {
		this.detailIndex = detailIndex;
	}
	
	public String getTranslatedValue() {
		if ( getItem() != null ) {
			if ( StringUtils.isNotEmpty( getItem().getTranslated() ) ) {
				TranslatedEnum en = TranslatedEnum.getEnumForName(getItem().getTranslated() );
				if ( en != null ) {
					return en.getUserText();
				}
			}
		}
		return null;
	}
	public String getBusinessValue() {
		if ( getItem() != null ) {
			if ( StringUtils.isNotEmpty( getItem().getBusiness() ) ) {
				BusinessProfitEnum en = BusinessProfitEnum.getEnumForName(getItem().getBusiness() );
				if ( en != null ) {
					return en.getUserText();
				}
			}
		}
		return null;
	}
	public String getTranslationLanguageValue() {
		if ( getItem() != null ) {
			if ( StringUtils.isNotEmpty( getItem().getBusiness() ) ) {
				TranslatedEnum en = TranslatedEnum.getEnumForName(getItem().getTranslationLanguage() );
				if ( en != null ) {
					return en.getUserText();
				}
			}
		}
		return null;
	}

	public boolean isUpdatePricingOnly() {
		return updatePricingOnly;
	}

	public void setUpdatePricingOnly(boolean updPricingOnly) {
		this.updatePricingOnly = updPricingOnly;
		lastEdit.setUpdatePricingOnly(updPricingOnly);
	}
	
}
