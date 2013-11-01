package com.copyright.ccc.business.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

public enum ItemAvailabilityEnum {
	
	//This is a RL pre-auth situation, which sometimes comes up in Rightslink as a “No Charge”.
	NO_CHARGE(true, "Granted", false, "", false, "Granted", "GRANT", "NC", "NC", "NC", false),
	
	PURCHASE(true, "Available for Purchase", true, "Approved by Righsholder for Purchase", true, "Grant", "Grant", "G", "G", "GRANT", false),
	DENY(true, "Denied", true, "Denied by Rightsholder", true, "Deny", "Deny", "D", "D", "", false),
	CONTACT_RH(true, "Available via Special Order", false, "", true, "Contact Rightsholder", "Checking availability", "C", "C", "", true),
//	CONTACT_RH_AUTHORIZED(false, "", true, "Granted", true, "Authorized via Special Order", "Special Order", "CRA", "G", ""),
//	CONTACT_RH_PURCHASED(false, "", false, "", true, "Granted via Special Order Purchase", "Granted", "CRP", "G", ""),
	CONTACT_RH_DIRECTLY(true, "Contact Rightsholder Directly", true, "", true, "Contact Rightsholder Directly", "Contact Rightsholder Directly",  "I", "I", "", false),
	RESEARCH_FURTHER(true, "Special Order", false, "", true, "Research Further", "Checking Availability", "R", "R", "", true),
	PUBLIC_DOMAIN(true, "Public Domain", false, "", true, "Public Domain", "Grant", "P", "P", "", false),
	HOLD_PENDING(false, "Hold Pending", true, "Hold Pending", false, "Hold Pending", "Checking Availability", "H", "H", "", true),
	CANCELED(false, "Canceled", false, "Canceled", false, "Canceled", "Canceled", "X", "X", "", false);
	

	private Boolean isOfferPermission;
	private String offerLabel;
	private Boolean isOverridePermission;
	private String overrideLabel;
	private Boolean isOrderPermission;
	private String orderLabel;
	private String externalOrderLabel;
	private String standardPermissionCd;
	private String tfPermissionCd;
	private String rlPermissionCd;
	private Boolean isSpecialOrder;

	
	private ItemAvailabilityEnum(Boolean isOfferPermission, String offerLabel, 
								 Boolean isOverridePermission, String overrideLabel, 
								 Boolean isOrderPermission, String orderLabel, String externalOrderLabel,
								 String standardPermissionCd, String tfPermissionCd, String rlPermissionCd,
								 Boolean isSpecialOrder)
	{
		this.isOfferPermission = isOfferPermission;
		this.offerLabel = offerLabel;
		this.isOverridePermission = isOverridePermission;
		this.overrideLabel = overrideLabel;
		this.isOrderPermission = isOrderPermission;
		this.orderLabel = orderLabel;
		this.externalOrderLabel = externalOrderLabel;
		this.standardPermissionCd = standardPermissionCd;
		this.tfPermissionCd = tfPermissionCd;
		this.rlPermissionCd = rlPermissionCd;
		this.isSpecialOrder = isSpecialOrder;
		
	}
	
	public static ItemAvailabilityEnum getEnumForName( String name ) {
		for ( ItemAvailabilityEnum ia : ItemAvailabilityEnum.values() ) {
			if ( name.equalsIgnoreCase(ia.name())) {
				return ia;
			} 
		}
		return null;
	}
	
	public static ItemAvailabilityEnum getEnumForTFPermissionCd( String tfPermissionCd  ) {
		for ( ItemAvailabilityEnum ia : ItemAvailabilityEnum.values() ) {
			if ( tfPermissionCd.equalsIgnoreCase(ia.getTfPermissionCd())) {
				return ia;
			} 
		}
		return null;
	}

	public static ItemAvailabilityEnum getEnumForItemAvailabilityCd( String itemAvailabilityCd  ) {
		if (itemAvailabilityCd == null) {
			return null;
		}
		for ( ItemAvailabilityEnum ia : ItemAvailabilityEnum.values() ) {
			if ( itemAvailabilityCd.equalsIgnoreCase(ia.getStandardPermissionCd())) {
				return ia;
			} 
		}
		return null;
	}

	
	public static ItemAvailabilityEnum getEnumForRLPermissionCd( String rlPermissionCd  ) {
		for ( ItemAvailabilityEnum ia : ItemAvailabilityEnum.values() ) {
			if ( rlPermissionCd.equalsIgnoreCase(ia.getRlPermissionCd())) {
				return ia;
			} 
		}
		return null;
	}
	
//	private static List<LabelValueBean> orderItemAvailabilityCodes = null;
	
	public static List<LabelValueBean> getOrderItemAvailablityCodes() {
		
		List<LabelValueBean> orderItemAvailabilityCodes = new ArrayList<LabelValueBean>();
		for ( ItemAvailabilityEnum ia : ItemAvailabilityEnum.values() ) {
			if (ia.isOrderPermission()) {
				LabelValueBean labelValueBean = new LabelValueBean();
				labelValueBean.setLabel(ia.getOrderLabel());
				labelValueBean.setValue(ia.getStandardPermissionCd());
				orderItemAvailabilityCodes.add(labelValueBean);
			}
		
		}		
		return orderItemAvailabilityCodes;
	}

	public static List<LabelValueBean> getOfferItemAvailablityCodes() {
		
		List<LabelValueBean> offerItemAvailabilityCodes = new ArrayList<LabelValueBean>();
		for ( ItemAvailabilityEnum ia : ItemAvailabilityEnum.values() ) {
			if (ia.isOfferPermission()) {
				LabelValueBean labelValueBean = new LabelValueBean();
				labelValueBean.setLabel(ia.getOfferLabel());
				labelValueBean.setValue(ia.getStandardPermissionCd());
				offerItemAvailabilityCodes.add(labelValueBean);
			}
		
		}		
		return offerItemAvailabilityCodes;
	}
	
	public static List<LabelValueBean> getOverrideItemAvailablityCodes() {
		
		List<LabelValueBean> overrideItemAvailabilityCodes = new ArrayList<LabelValueBean>();
		for ( ItemAvailabilityEnum ia : ItemAvailabilityEnum.values() ) {
			if (ia.isOverridePermission()) {
				LabelValueBean labelValueBean = new LabelValueBean();
				labelValueBean.setLabel(ia.getOverrideLabel());
				labelValueBean.setValue(ia.getStandardPermissionCd());
				overrideItemAvailabilityCodes.add(labelValueBean);
			}
		
		}		
		return overrideItemAvailabilityCodes;
	}
	
	
	public Boolean isOfferPermission() {
		return isOfferPermission;
	}

	public String getOfferLabel() {
		return offerLabel;
	}

	public Boolean isOverridePermission() {
		return isOverridePermission;
	}

	public String getOverrideLabel() {
		return overrideLabel;
	}

	public Boolean isOrderPermission() {
		return isOrderPermission;
	}

	public String getOrderLabel() {
		return orderLabel;
	}

	public String getExternalOrderLabel() {
		return externalOrderLabel;
	}

	public String getStandardPermissionCd() {
		return standardPermissionCd;
	}

	public String getTfPermissionCd() {
		return tfPermissionCd;
	}

	public String getRlPermissionCd() {
		return rlPermissionCd;
	}

	public Boolean isSpecialOrder() {
		return isSpecialOrder;
	}

	public void setIsSpecialOrder(Boolean isSpecialOrder) {
		this.isSpecialOrder = isSpecialOrder;
	}
	

}
