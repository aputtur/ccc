package com.copyright.ccc.business.data;

import java.math.BigDecimal;
import java.util.Date;

import com.copyright.svc.order.api.data.DeclineReasonEnum;

public interface OrderLicense extends TransactionItem
{
        public int getUsageType();

        public String getCustomerRef();
        
        public long getRgtInst();
        
        public long getTpuInst();
        
        public long getPrdInst();

        public String getBillingStatus();
        
        public String getBillingStatusCd();
        
        public String getBillingStatusCode();
        
        public Date getCreateDate();
        
        public String getCreateDateStr();

        public String getCreditAuth();

        public String getExternalCommentOverride();

        public String getInvoiceId();
        
        public void setInvoiceId(String invoiceId);
        
        public boolean isInvoiced();
        
        public boolean isPaidByCreditCard();
        
        public long getID();
        
        public Long getBundleId();
        
        public void setBundleId(Long bundleId);

        public boolean isEditable();
        
        public boolean isCancelable();
        
        public boolean isCanceled();
        
        public boolean isRightsLink();

        public long getLastFourCreditCard();

        public long getOrderId();

        public String getPermissionStatus();

        public String getDisplayPermissionStatus();
        
        public String getDisplayPermissionStatusCd();
        
        public Date getPinningDate();

        public long getPurchaseId();
        
        public Long getOrderHeaderId();

        public String getExternalOrderId();
        
        public String getWithdrawnCode();
        
        public String getTransactionId();
        
        public double getPriceValue();

        public double getPriceValueRaw();
        
    	public Long getJobTicketNumber();
    	
    	public void setJobTicketNumber(Long jobTicket);

    	public Long getExternalDetailId();

    	public void setExternalDetailId(Long detailId);
        
        public String getLicenseStatusCd(); 
    
        public String getLicenseStatusCdMsg();
          
        public long getLicenseDetailReferenceID(); 
        
        public boolean isDistributed();

        public long getLicenseePartyId();
        
        public Date getInvoiceDate();
        
        public void setInvoiceDate(Date invoiceDate);
        
        public boolean isSpecialOrderLimitsExceeded();
        
        public boolean isUnresolvedSpecialOrder();
               
        public Long getWrWorkInst();
        
        public String getIdnoLabel();
        
        public void setIdnoLabel(String idNoLabel);
        
    	public int getOrderDataSource();
     	
    	public String getOrderDataSourceDisplay(); 
    	  	    	  	
    	public Date getLastUpdatedDate();
    	
    	public String getRhRefNum();
    	
    	public void setRhRefNum(String rhRedNum);
     	
    	public String getRightSourceCd();
    	
    	public String getResearchUserIdentifier();
    	
    	public String getItemStatusCd();
    	
    	public void setItemStatusCd(String itemStatus);
 
    	public String getItemStatusDescription();

    	public String getItemStatusQualifierCd();

    	public void setItemStatusQualifierCd(String itemStatusQualifier);

    	public Date getItemStatusDate();
    	
    	public void setItemStatusDate(Date itemStatusDate);
    	
    	public String getItemStatusQualifierDescription();
    	
    	public String getItemStatusInternalDisplay();
    	
    	public String getItemStatusExternalDisplay();
    	
    	public String getItemCycleDisplay();
    	
    	public String getItemErrorDescriptionDisplay();
    	
    	public String getPaymentTypeDisplay();
    	
    	public String getStandardNumber();
    	
    	public String getRightsholderAccount();
    	
    	public Long getRightsholderPartyId();

    	public Long getRightsholderPtyInst();
    	
    	public String getOverrideAvailabilityCd();

    	public String getOverrideAvailabilityDescription();
    	
    	public Date getOverrideDate();
    	
    	public String getOverrideComment();
    	
    	public Boolean isAdjusted();
    	
    	public BigDecimal getRightsholderPercent();
    	
    	public String getCategoryCd();
    	
    	public void setExternalTouId(Long externalTouId);
    	
    	public void setTouName(String TouName);
    	
    	public void setProductSourceKey(Long productSourceKey);
    	
    	public void setProductName(String productName);
    	
    	public void setProductCd(String productCd);
    	   	
    	public String getDistributionEvent();
    	
    	public Long getPaymentId();
    	
    	public String getPaymentMethodCd();
    	
    	public String getCurrencyType();
    	
    	public String getCcAuthNo();
    	
    	public BigDecimal getCcTrxId();
    	
    	public Date getCcTrxDate();
    	
    	public BigDecimal getExchangeRate();
    	
    	public Date getExchangeDate();
    	
    	public String getMerchantRefId();
    	
    	public BigDecimal getUsdTotal();
    	
    	public BigDecimal getCurrencyPaidTotal();
    	
    	public Long getCccProfileId();
    	
    	public String getPaymentProfileIdentifier();

    	// Can this item be adjusted
    	public boolean isAdjustable();
    	
    	public boolean isRequiresAcademicRepin();
    	
    	public String getOrderRefNumber();
    	
    	public boolean isAvailableToCopy();

    	public BigDecimal getEntireBookFeeOverrideValue();
    	   
     	public void setEntireBookFeeOverrideValue(BigDecimal entireBookFee);
    	
    	public BigDecimal getBaseFeeOverrideValue();
   
     	public void setBaseFeeOverrideValue(BigDecimal baseFee);

    	public BigDecimal getFlatFeeOverrideValue();
    	   
     	public void setFlatFeeOverrideValue(BigDecimal flatFee);

    	public BigDecimal getPerPageFeeOverrideValue();
    	   
     	public void setPerPageFeeOverrideValue(BigDecimal perPageFee);

    	public BigDecimal getMaximumRoyaltyFeeOverrideValue();
 	   
     	public void setMaximumRoyaltyFeeOverrideValue(BigDecimal maximumRoyaltyFee);
     	
    	public BigDecimal getDurationFeeOverrideValue();
  	   
     	public void setDurationFeeOverrideValue(BigDecimal durationFee);

    	public BigDecimal getRecipientsFeeOverrideValue();
   	   
     	public void setRecipientsFeeOverrideValue(BigDecimal recipientsFee);

    	public BigDecimal getPerContentFeeOverrideValue();
   	   
     	public void setPerContentFeeOverrideValue(BigDecimal durationFee);

     	public String getAdjustmentItems();
     	
     	public void setOrderSourceCd(String orderSourceCd);
     	
     	public String getOrderSourceCd();
     	
     	public void setDeclineReasonCd(DeclineReasonEnum declineCd);
     	
     	public String getDeclineReasonCd();
     	
	}

