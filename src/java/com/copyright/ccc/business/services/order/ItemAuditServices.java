package com.copyright.ccc.business.services.order;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.copyright.svc.order.api.data.ItemStatusEnum;
import com.copyright.ccc.business.services.ItemConstants;
import com.copyright.ccc.business.services.OdtStatusEnum;
import com.copyright.ccc.business.services.ItemAvailabilityEnum;
import com.copyright.ccc.business.services.ProductEnum;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.util.LogUtil;
import com.copyright.svc.order.api.OrderService;
import com.copyright.svc.order.api.data.AuditRecord;
import com.copyright.svc.order.api.data.Item;
import com.copyright.svc.order.api.data.ItemFees;
import com.copyright.svc.order.api.data.ItemStatusQualifierEnum;
import com.copyright.svc.order.api.data.OrderConsumerContext;
import com.copyright.svc.order.api.data.SpecialOrderTypeEnum;

public class ItemAuditServices
{
	private static Logger LOGGER = Logger.getLogger(ItemAuditServices.class);
	OrderService orderSvc = ServiceLocator.getOrderService();
	
	//enumeration to define the itemFees property elements and their display values
	//If new properties are requested to show on the audit info then the columns
	//directly on the itemFees table should be added here.
	//One of the reasons for the enum is to match a display value with a parm/field
	//name in the ui.  The other is so that the fields being audited can be
	//manipulated if required in the future by breaking out the enums into
	//stand alone enums.

	private enum ItemFeesPropertyEnum
	{
		DISTIRBUTION_ACCOUNT("ActualDistAccount", "RH Account #"),
		DISTRIBUTION_PARTY_NAME("ActualDistPartyName", "Rightsholder"),
		DISTRIBUTION_PAYABLE("DistributionPayable", "Royalty"),
		LICENSEE_FEE("LicenseeFee", "Licensee Fee"),
		RIGHTSHOLDER_FEE("RightsholderFee", "Rightsholder Fee");
		
		
		private String valueName;
		private String displayValue;
		
		/**
		 * @return the value
		 */
		public String getValueName()
		{
			return valueName;
		}

		/**
		 * @param value the value to set
		 */
		public void setValueName(String valueName)
		{
			this.valueName = valueName;
		}

		/**
		 * @return the displayValue
		 */
		public String getDisplayValue()
		{
			return displayValue;
		}

		/**
		 * @param displayValue the displayValue to set
		 */
		public void setDisplayValue(String displayValue)
		{
			this.displayValue = displayValue;
		}

		private ItemFeesPropertyEnum(String valueName, String displayValue)
		{
			this.valueName = valueName;
			this.displayValue = displayValue;
		}
	}
	
	//enumeration to define the item property elements and their display values
	//If new properties are requested to show on the audit info then the columns
	//directly on the item table should be added here.
	//One of the reasons for the enum is to match a display value with a parm/field
	//name in the ui.  The other is so that the fields being audited can be
	//manipulated if required in the future by breaking out the enums into
	//stand alone enums.
	private enum ItemPropertyEnum
	{
		ITEM_ID("ItemId", "Detail #"),
		ITEM_STATUS_CD("ItemStatusCd", "Detail Status"),
		RH_REF_NUM("RhRefNum", "RH Ref #"),
		RIGHT_ID("RightId", "Right Inst"),
		TITLE("ItemDescription", "Title"),
		LINE_ITEM_REF_NUM("LicenseeRefNum", "Line Item Ref #"),
		OVERRIDE_COMMENT("OverrideComment", "Override Comment"),
		OVERRIDE_DATE("OverrideDtm", "Override Date"),
		OVERRIDE_PERM("OverrideAvailabilityCd", "Override Perm"),
		PERMISSION_TYPE("ItemAvailabilityCd", "Permission Status"),
		PINNING_DATE("PinningDtm", "Pinned"),
		RIGHTS_QUALIFIER("RightQualifierTerms", "Rights Qualifier"),
		TOTAL_PRICE("TotalPrice", "Total"),
		EXTERNAL_ITEM_STATUS_CD("ExternalItemStatusCd", "External Item Status Code"),
		BUYER_COMMENT("BuyerComment", "Buyer Comment");
			
		private String valueName;
		private String displayValue;
		
		/**
		 * @return the value
		 */
		public String getValueName()
		{
			return valueName;
		}

		/**
		 * @param value the value to set
		 */
		public void setValueName(String valueName)
		{
			this.valueName = valueName;
		}

		/**
		 * @return the displayValue
		 */
		public String getDisplayValue()
		{
			return displayValue;
		}

		/**
		 * @param displayValue the displayValue to set
		 */
		public void setDisplayValue(String displayValue)
		{
			this.displayValue = displayValue;
		}

		private ItemPropertyEnum(String valueName, String displayValue)
		{
			this.valueName = valueName;
			this.displayValue = displayValue;
		}
	}

	//enumeration to define the item parm elements and their display values.
	//If new properties are requested to show on the audit info then the values
	//from the parm_parm table should be added here.
	//One of the reasons for the enum is to match a display value with a parm/field
	//name in the ui.  The other is so that the fields being audited can be
	//manipulated if required in the future by breaking out the enums into
	//stand alone enums.
	private enum ItemParmEnum 
	{
		CIRCULATION_DISTRIBUTION("CIRCULATION_DISTRIBUTION", "Circ / Dist"),
		REPUBLICATION_DATE("REPUBLICATION_DATE", "Repub Date"),
		REPUBLISHING_ORGANIZATION("REPUBLISHING_ORGANIZATION", "Repub Pub"),
		NEW_PUBLICATION_TITLE("NEW_PUBLICATION_TITLE", "Repub Work"),
		CUSTOM_AUTHOR("CUSTOM_AUTHOR", "Author"),
		CHAPTER_ARTICLE("CHAPTER_ARTICLE", "Chapter/Article/Content Desc"),
		FULL_ARTICLE("FULL_ARTICLE", "Content Type - Full Article"),
		NUM_CARTOONS("NUM_CARTOONS", "Content Type - Cartoons"),
		NUM_CHARTS("NUM_CHARTS", "Content Type - Charts"),
		NUM_EXCERPTS("NUM_EXCERPTS", "Content Type - Excerpts"),
		NUM_FIGURES("NUM_FIGURES", "Content Type - Figures"),
		NUM_GRAPHS("NUM_GRAPHS", "Content Type - Graphs"),
		NUM_ILLUSTRATIONS("NUM_ILLUSTRATIONS", "Content Type - Illustrations"),
		NUM_LOGOS("NUM_LOGOS", "Content Type - Logos"),
		NUM_PHOTOS("NUM_PHOTOS", "Content Type - Photos"),
		NUM_QUOTES("NUM_QUOTES", "Content Type - Quotes"),
		SELECTED_PAGES("SELECTED_PAGES", "Content Type - Selected Pages"),
		DATE_OF_USE("DATE_OF_USE", "Date Used"),
		DURATION_CD("DURATION_CD", "Duration"),
		CUSTOM_VOLUME("CUSTOM_VOLUME", "Volume"),
		CUSTOM_EDITION("CUSTOM_EDITION", "Edition"),
		IS_AUTHOR("IS_AUTHOR", "Is perm requestor original author"),
		TRANSLATION_LANGUAGE("TRANSLATION_LANGUAGE", "Language"),
		NUM_PAGES("NUM_PAGES", "No. of Pages"),
		NUM_RECIPIENTS("NUM_RECIPIENTS", "No. of Recipients"),
		NUM_COPIES("NUM_COPIES", "No. of Copies"),
		PAGE_RANGE("PAGE_RANGE", "Page Ranges"),
		PUBLICATION_YEAR_OF_USE("DATE_OF_PUBLICATION_USED", "Pub Date"),
		IS_TRANSLATED("IS_TRANSLATED", "Translated"),
		DATE_OF_ISSUE("DATE_OF_ISSUE", "Date of Issue"),
		RLS_PAGES("RLS_PAGES", "Content Type - RLS Pages"),
		TYPE_OF_CONTENT("TYPE_OF_CONTENT", "Type of Content");
		
		private String valueName;
		private String displayValue;
		
		/**
		 * @return the valueName
		 */
		public String getValueName()
		{
			return valueName;
		}

		/**
		 * @param valueName the valueName to set
		 */
		public void setValueName(String valueName)
		{
			this.valueName = valueName;
		}

		/**
		 * @return the displayValue
		 */
		public String getDisplayValue()
		{
			return displayValue;
		}

		/**
		 * @param displayValue the displayValue to set
		 */
		public void setDisplayValue(String displayValue)
		{
			this.displayValue = displayValue;
		}

		private ItemParmEnum(String valueName, String displayValue)
		{
			this.valueName = valueName;
			this.displayValue = displayValue;
		}
	}
	
	//enumeration to define the item_description_parm elements and their display values.
	//If new properties are requested to show on the audit info then the values
	//from the item_description_parm table should be added here.
	//One of the reasons for the enum is to match a display value with a parm/field
	//name in the ui.  The other is so that the fields being audited can be
	//manipulated if required in the future by breaking out the enums into
	//stand alone enums.
	private enum ItemDescriptionParmEnum 
	{
		STANDARD_NUMBER("STANDARD_NUMBER", "IDNO");
		
		private String valueName;
		private String displayValue;
		
		/**
		 * @return the valueName
		 */
		public String getValueName()
		{
			return valueName;
		}

		/**
		 * @param valueName the valueName to set
		 */
		public void setValueName(String valueName)
		{
			this.valueName = valueName;
		}

		/**
		 * @return the displayValue
		 */
		public String getDisplayValue()
		{
			return displayValue;
		}

		/**
		 * @param displayValue the displayValue to set
		 */
		public void setDisplayValue(String displayValue)
		{
			this.displayValue = displayValue;
		}

		private ItemDescriptionParmEnum(String valueName, String displayValue)
		{
			this.valueName = valueName;
			this.displayValue = displayValue;
		}
	}
	
	public List<AuditRecord> getItemAudit(Long itemId)
	{
		if (itemId == null)
		{
			LOGGER.error("getItemAudit was passed a null itemId." );
			
			throw new IllegalArgumentException( "getItemAudit was passed a null itemId." );
		}
		
		List<AuditRecord> changeRecs = new ArrayList<AuditRecord>();
		OrderConsumerContext orderConsumerContext = new OrderConsumerContext();
		
		Method mtd = null;
		
		//item properties to audit
		List <ItemPropertyEnum> fieldNames = new ArrayList<ItemPropertyEnum>();
		
		fieldNames.add(ItemPropertyEnum.BUYER_COMMENT);
		fieldNames.add(ItemPropertyEnum.ITEM_ID);
		fieldNames.add(ItemPropertyEnum.ITEM_STATUS_CD);
		fieldNames.add(ItemPropertyEnum.EXTERNAL_ITEM_STATUS_CD);
		fieldNames.add(ItemPropertyEnum.LINE_ITEM_REF_NUM);
		fieldNames.add(ItemPropertyEnum.OVERRIDE_DATE);
		fieldNames.add(ItemPropertyEnum.OVERRIDE_COMMENT);
		fieldNames.add(ItemPropertyEnum.OVERRIDE_PERM);
		fieldNames.add(ItemPropertyEnum.PERMISSION_TYPE);
		fieldNames.add(ItemPropertyEnum.PINNING_DATE);
		fieldNames.add(ItemPropertyEnum.RH_REF_NUM);
		fieldNames.add(ItemPropertyEnum.RIGHT_ID);
		fieldNames.add(ItemPropertyEnum.RIGHTS_QUALIFIER);
		fieldNames.add(ItemPropertyEnum.TITLE);
		fieldNames.add(ItemPropertyEnum.TOTAL_PRICE);
		

		//itemParm properties to audit
		List <ItemParmEnum> parmNames = new ArrayList<ItemParmEnum>();
		parmNames.add(ItemParmEnum.CUSTOM_AUTHOR);
		parmNames.add(ItemParmEnum.NUM_CARTOONS);
		parmNames.add(ItemParmEnum.NUM_CHARTS);
		parmNames.add(ItemParmEnum.NUM_EXCERPTS);
		parmNames.add(ItemParmEnum.FULL_ARTICLE);
		parmNames.add(ItemParmEnum.NUM_FIGURES);
		parmNames.add(ItemParmEnum.NUM_GRAPHS);
		parmNames.add(ItemParmEnum.NUM_ILLUSTRATIONS);
		parmNames.add(ItemParmEnum.NUM_LOGOS);
		parmNames.add(ItemParmEnum.NUM_PHOTOS);
		parmNames.add(ItemParmEnum.NUM_QUOTES);
		parmNames.add(ItemParmEnum.CHAPTER_ARTICLE);
		parmNames.add(ItemParmEnum.CIRCULATION_DISTRIBUTION);
		parmNames.add(ItemParmEnum.DATE_OF_USE);
		parmNames.add(ItemParmEnum.DURATION_CD);
		parmNames.add(ItemParmEnum.CUSTOM_EDITION);
		parmNames.add(ItemParmEnum.IS_AUTHOR);
		parmNames.add(ItemParmEnum.NUM_COPIES);
		parmNames.add(ItemParmEnum.NUM_PAGES);
		parmNames.add(ItemParmEnum.NUM_RECIPIENTS);
		parmNames.add(ItemParmEnum.TRANSLATION_LANGUAGE);
		parmNames.add(ItemParmEnum.PAGE_RANGE);
		parmNames.add(ItemParmEnum.PUBLICATION_YEAR_OF_USE);
		parmNames.add(ItemParmEnum.REPUBLICATION_DATE);
		parmNames.add(ItemParmEnum.REPUBLISHING_ORGANIZATION);
		parmNames.add(ItemParmEnum.NEW_PUBLICATION_TITLE);
		parmNames.add(ItemParmEnum.IS_TRANSLATED);
		parmNames.add(ItemParmEnum.CUSTOM_VOLUME);
		parmNames.add(ItemParmEnum.DATE_OF_ISSUE);
		parmNames.add(ItemParmEnum.RLS_PAGES);
		parmNames.add(ItemParmEnum.TYPE_OF_CONTENT);
		
		//itemDescriptionParm properties to audit
		List <ItemDescriptionParmEnum> descParmNames = new ArrayList<ItemDescriptionParmEnum>();
		descParmNames.add(ItemDescriptionParmEnum.STANDARD_NUMBER);

		//itemFees properties to audit
		List <ItemFeesPropertyEnum> itemFeeFields = new ArrayList<ItemFeesPropertyEnum>();
		itemFeeFields.add(ItemFeesPropertyEnum.DISTIRBUTION_ACCOUNT);
		itemFeeFields.add(ItemFeesPropertyEnum.DISTRIBUTION_PARTY_NAME);
		itemFeeFields.add(ItemFeesPropertyEnum.DISTRIBUTION_PAYABLE);
		itemFeeFields.add(ItemFeesPropertyEnum.LICENSEE_FEE);
		itemFeeFields.add(ItemFeesPropertyEnum.RIGHTSHOLDER_FEE);

		List<Item> itemList = orderSvc.getItemHistory(
				orderConsumerContext, itemId);

		if (itemList.size() == 0)
		{
			return changeRecs;
		}
		
		// the first version is the baseline
		Item compItem = itemList.get(0); 

		for (int itmIdx = 1; itmIdx < itemList.size() + 1; itmIdx++)
		{

			Item nextItem;

			// if we reach the last item history record then we want to
			// compare it against the actual item record
			// (not a history record)
			if (itmIdx == itemList.size())
			{
				nextItem = orderSvc.getItemById(orderConsumerContext,
						itemId);
			} else
			{
				nextItem = (Item) itemList.get(itmIdx);
			}

			// loop through all properties directly on the item
			for ( ItemPropertyEnum fn : fieldNames)
			{
				String mName = "get" + fn.valueName;

				try
				{
					mtd = Item.class.getMethod(mName);
				} catch (Exception e)
				{
					LOGGER.error("Error getting property values from item history : " + itemId + " - " + LogUtil.appendableStack(e));
					RuntimeException re = new RuntimeException("Error getting property values from item history : " + itemId + " - " + LogUtil.appendableStack(e)); 
					throw re;
				}

				Object oldValue = null;

				try
				{
					oldValue = mtd.invoke(compItem);
				} catch (Exception e)
				{
					LOGGER.error("Error getting property values from item history : " + itemId + " - " + LogUtil.appendableStack(e));
					RuntimeException re = new RuntimeException("Error getting property values from item history : " + itemId + " - " + LogUtil.appendableStack(e)); 
					throw re;
				}

				Object newValue = null;

				try
				{
					newValue = mtd.invoke(nextItem);
				} catch (Exception e)
				{
					LOGGER.error("Error getting property values from item history : " + itemId + " - " + LogUtil.appendableStack(e));
					RuntimeException re = new RuntimeException("Error getting property values from item history : " + itemId + " - " + LogUtil.appendableStack(e)); 
					throw re;
				}

				//holds the text from looking up the enum
				Object holdValue = null;
				
				if ((oldValue != null && !oldValue.equals(newValue))
						|| (oldValue == null && newValue != null))
				{
					if (fn.name().equals(ItemPropertyEnum.OVERRIDE_PERM.name()))
					{
						if (oldValue != null && ItemAvailabilityEnum.getEnumForItemAvailabilityCd(oldValue.toString()) != null)
						{
							//get the desc text from the enum
							holdValue = ItemAvailabilityEnum.getEnumForItemAvailabilityCd(oldValue.toString()).getOrderLabel();
							
							if (holdValue == null)
							{
								oldValue = "Translation not found for value " + oldValue.toString();
							}
							else
							{
								oldValue = holdValue;
							}
						}
						else 
						{
							oldValue = null;
						}
						
						holdValue = null;
						
						if (newValue != null && ItemAvailabilityEnum.getEnumForItemAvailabilityCd(newValue.toString()) != null)
						{
							holdValue = ItemAvailabilityEnum.getEnumForItemAvailabilityCd(newValue.toString()).getOrderLabel();
							
							if (holdValue == null)
							{
								newValue = "Translation not found for value " + newValue.toString();
							}
							else
							{
								newValue = holdValue;
							}
						}
						else 
						{
							newValue = null;
						}
					}
					
					else if (fn.name().equals(ItemPropertyEnum.ITEM_STATUS_CD.name())) 
					{
						
						if (oldValue != null && this.getItemStatusEnumForName(oldValue.toString()) != null)
						{
							
							//get the desc text from the calc code
							holdValue = calcItemStatusDisplay(compItem);
							
							//get the desc text from the enum
							//holdValue = ItemStatusEnum.getEnumForName(oldValue.toString()).getDesc();
							
							if (holdValue == null)
							{
								oldValue = "Translation not found for value " + oldValue.toString();
							}
							else
							{
								oldValue = holdValue;
							}
						}
						else 
						{
							oldValue = null;
						}
						
						holdValue = null;
						
						
						if (newValue != null && this.getItemStatusEnumForName(newValue.toString()) != null)
						{
							//get the desc text from the calc code
							holdValue = calcItemStatusDisplay(nextItem);
							
							//get the desc text from the enum
							//holdValue = ItemStatusEnum.getEnumForName(newValue.toString()).getDesc();
							
							if (holdValue == null)
							{
								newValue = "Translation not found for value " + newValue.toString();
							}
							else
							{
								newValue = holdValue;
							}
						}
						else 
						{
							newValue = null;
						}
						
					}
					
					else if (fn.name().equals(ItemPropertyEnum.EXTERNAL_ITEM_STATUS_CD.name()))
					{
						if (oldValue != null && OdtStatusEnum.getEnumForOdtStatusCd(oldValue.toString()) != null)
						{
							holdValue = OdtStatusEnum.getEnumForOdtStatusCd(oldValue.toString()).getOdtStatusMsg();
							
							if (holdValue == null)
							{
								oldValue = "Translation not found for value " + oldValue.toString();
							}
							else
							{
								oldValue = holdValue;
							}
						}
						else 
						{
							oldValue = null;
						}
						
						holdValue = null;
						
						if (newValue != null && OdtStatusEnum.getEnumForOdtStatusCd(newValue.toString()) != null)
						{
							holdValue = OdtStatusEnum.getEnumForOdtStatusCd(newValue.toString()).getOdtStatusMsg();
							
							if (holdValue == null)
							{
								newValue = "Translation not found for value " + newValue.toString();
							}
							else
							{
								newValue = holdValue;
							}
						}
						else 
						{
							newValue = null;
						}
					}
					else if (fn.name().equals(ItemPropertyEnum.PERMISSION_TYPE.name()))
					{
						if (oldValue != null && ItemAvailabilityEnum.getEnumForItemAvailabilityCd(oldValue.toString()) != null)
						{
							holdValue = ItemAvailabilityEnum.getEnumForItemAvailabilityCd(oldValue.toString()).getOrderLabel();
							
							if (holdValue == null)
							{
								oldValue = "Translation not found for value " + oldValue.toString();
							}
							else
							{
								oldValue = holdValue;
							}
						}
						else 
						{
							oldValue = null;
						}
						
						holdValue = null;
						
						if (newValue != null && ItemAvailabilityEnum.getEnumForItemAvailabilityCd(newValue.toString()) != null)
						{
							holdValue = ItemAvailabilityEnum.getEnumForItemAvailabilityCd(newValue.toString()).getOrderLabel();
							
							if (holdValue == null)
							{
								newValue = "Translation not found for value " + newValue.toString();
							}
							else
							{
								newValue = holdValue;
							}
						}
						else 
						{
							newValue = null;
						}
					}
										
					AuditRecord ar = new AuditRecord(fn.getDisplayValue(),
							itemId, nextItem.getItemVersion(),
							oldValue, newValue, nextItem.getUpdateDate(),
							nextItem.getUpdateUser());

					changeRecs.add(ar);
				}
			}

			// item parm compare
			for (ItemParmEnum pn : parmNames )
			{	
				Object oldValue = null;
				
				if (compItem.getItemParms().get(pn.getValueName()) != null )
				{
					oldValue = compItem.getItemParms().get(pn.getValueName()).getParmValue(); 
				}
				
				Object newValue = null;

				if (nextItem.getItemParms().get(pn.getValueName()) != null)
				{
					newValue = nextItem.getItemParms().get(pn.valueName).getParmValue();
				}

				if ((oldValue != null && !oldValue.equals(newValue))
						|| (oldValue == null && newValue != null))
				{
					AuditRecord ar = new AuditRecord(pn.displayValue,
							itemId, nextItem.getItemVersion(),
							oldValue, newValue, nextItem.getUpdateDate(),
							nextItem.getUpdateUser());

					changeRecs.add(ar);
				}
			}

			// item parm description compare
			for (ItemDescriptionParmEnum dpn : descParmNames) 
			{
				Object oldValue = null;
				
				if (compItem.getItemDescriptionParms().get(
						dpn.valueName) != null )
				{
					oldValue = compItem.getItemDescriptionParms().get(dpn.getValueName()).getParmValue();
				}
								
				Object newValue = null;
				
				if(nextItem.getItemDescriptionParms().get(
						dpn.getValueName()) != null )
				{
					newValue = nextItem
					.getItemDescriptionParms().get(dpn.valueName).getParmValue();
				}
				
				if ((oldValue != null && !oldValue.equals(newValue))
						|| (oldValue == null && newValue != null))
				{
					AuditRecord ar = new AuditRecord(
							dpn.displayValue, itemId,
							nextItem.getItemVersion(), oldValue, newValue,
							nextItem.getUpdateDate(), nextItem.getUpdateUser());

					changeRecs.add(ar);
				}
			}

			// compare fees
			ItemFees compFees = null;
			if (!compItem.getAllFees().isEmpty())
			{
				compFees = (ItemFees) compItem.getAllFees().toArray()[0];
			}

			ItemFees nextFees = null;
			if (!nextItem.getAllFees().isEmpty())
			{
				nextFees = (ItemFees) nextItem.getAllFees().toArray()[0];
			}

			if (nextFees != null)
			{
				// loop through the fee type properties we want to compare
				// and call the methods to get them
				for (ItemFeesPropertyEnum iff : itemFeeFields)
				{
					String mName = "get" + iff.getValueName();

					try
					{
						mtd = ItemFees.class.getMethod(mName);
					} catch (Exception e)
					{
						LOGGER.error("Error getting property values from item fees history : " + itemId + " - " + LogUtil.appendableStack(e));
						RuntimeException re = new RuntimeException("Error getting property values from item fees history : " + itemId + " - " + LogUtil.appendableStack(e)); 
						throw re;
					}

					Object oldValue = null;
					if (compFees != null)
					{

						try
						{
							oldValue = mtd.invoke(compFees);

						} catch (Exception e)
						{
							LOGGER.error("Error getting property values from item fees history : " + itemId + " - " + LogUtil.appendableStack(e));
							RuntimeException re = new RuntimeException("Error getting property values from item fees  history : " + itemId + " - " + LogUtil.appendableStack(e)); 
							throw re;
						}
					}

					Object newValue = null;
					try
					{
						newValue = mtd.invoke(nextFees);
					} catch (Exception e)
					{
						LOGGER.error("Error getting property values from item fees history : " + itemId + " - " + LogUtil.appendableStack(e));
						RuntimeException re = new RuntimeException("Error getting property values from item fees  history : " + itemId + " - " + LogUtil.appendableStack(e)); 
						throw re;
					}

					if ((oldValue != null && !oldValue.equals(newValue))
							|| (oldValue == null && newValue != null))
					{
						AuditRecord ar = new AuditRecord(
								iff.getDisplayValue(), itemId,
								nextItem.getItemVersion(), oldValue, newValue,
								nextItem.getUpdateDate(), nextItem
										.getUpdateUser());

						changeRecs.add(ar);
					}
				}
			}
			compItem = nextItem;
		}
		
		Collections.sort(changeRecs);


		
		return changeRecs;
	}
	
	public String calcItemStatusDisplay(Item item) {

		if (item.getItemStatusCd() == null) {
			return  "";
		}

		if (item.getItemStatusCd().equals(ItemStatusEnum.INVOICE_READY)) {
			if (item.getItemAvailabilityCd() != null
					&& item.getItemAvailabilityCd()
							.equals(
									ItemAvailabilityEnum.DENY
											.getStandardPermissionCd())) {
				return  "Awaiting Invoicing";
				
			} else {
				if (item.getIsAdjusted() != null && item.getIsAdjusted().booleanValue()) {
					return  "Awaiting Adjustment Invoicing";					
				} else if (item.getItemStatusQualifier() == null) {
					return  "Awaiting Invoicing";
				} else {
					if (item.getItemStatusQualifier().equals(
							ItemStatusQualifierEnum.INVOICE_READY_CREDIT_CARD)) {
						return  "Awaiting Invoicing";
					}
					if (item.getItemStatusQualifier().equals(
							ItemStatusQualifierEnum.INVOICE_READY_INVOICE)) {
						return  "Awaiting Invoicing";
					}
				}
			}
		}

		if (item.getItemStatusCd().equals(
				ItemStatusEnum.INVOICING_IN_PROGRESS)) {
			if (item.getItemAvailabilityCd() != null
					&& item.getItemAvailabilityCd()
							.equals(
									ItemAvailabilityEnum.DENY
											.getStandardPermissionCd())) {
				return  "Invoicing in Progress";
			} else {
				if (item.getIsAdjusted() != null && item.getIsAdjusted().booleanValue()) {
					return  "Adjustment Invoicing in Progress";
				} else 	if (item.getItemStatusQualifier() == null) {
					return  "Invoicing in Progress";
				} else {
					if (item.getItemStatusQualifier().equals(
							ItemStatusQualifierEnum.INVOICE_READY_CREDIT_CARD)) {
						return  "Invoicing in Progress";
					}
					if (item.getItemStatusQualifier().equals(
							ItemStatusQualifierEnum.INVOICE_READY_INVOICE)) {
						return  "Invoicing in Progress";
					}
				}
			}
		}

		if (item.getItemStatusCd().equals(ItemStatusEnum.INVOICED)) {

			if (item.getItemAvailabilityCd() != null
					&& item.getItemAvailabilityCd()
							.equals(
									ItemAvailabilityEnum.DENY
											.getStandardPermissionCd())) {
				return  "Invoiced";
			} else {
				if (item.getItemStatusQualifier() == null) {
					return  "Invoiced";
				} else {
					if (item.getItemStatusQualifier().equals(
							ItemStatusQualifierEnum.INVOICED)) {
						return  "Invoiced";
					}
					if (item.getItemStatusQualifier().equals(
							ItemStatusQualifierEnum.PAID)) {
						return  "Invoiced";
					}
					if (item.getItemStatusQualifier().equals(
							ItemStatusQualifierEnum.PAID_CREDIT_CARD)) {
						return  "Invoiced";
					}
					if (item
							.getItemStatusQualifier()
							.equals(
									ItemStatusQualifierEnum.PAID_INVOICE_BY_CREDIT_CARD)) {
						return  "Invoiced";
					}
				}
			}
		}

		if (item.getItemStatusCd().equals(
				ItemStatusEnum.AWAITING_LCN_REPLY)) {
			return  "Requires Additional Licensee Information";
		}

		if (item.getItemStatusCd().equals(
				ItemStatusEnum.AWAITING_LCN_CONFIRM)) {
			return  "Awaiting Licensee";
			
		}

		if (item.getItemStatusCd().equals(ItemStatusEnum.AWAITING_RH)) {
			return  "Requires Rightsholder Response";
		}

		if (item.getItemStatusCd().equals(
				ItemStatusEnum.AWAITING_FULFILLMENT)) {
			return  "Awaiting Fulfillment";
		}

		if (item.getItemStatusCd()
				.equals(ItemStatusEnum.AWAITING_RESEARCH)) {
			return  "Requires Research";
		}

		if (item.getItemStatusCd().equals(ItemStatusEnum.CANCELLED)) {
			if (!item.getProductCd().equals(ProductEnum.RL.name()) &&
				!item.getProductCd().equals(ProductEnum.RLR.name())) {
				return  "Canceled";				
			} else {
				if (item.getItemAvailabilityCd() != null
						&& item.getItemAvailabilityCd().equals(ItemAvailabilityEnum.DENY.getStandardPermissionCd())) {
					return  "Invoiced";
				} else {
					return  "Canceled";								
				}
			}
		}

		if (item.getItemStatusCd().equals(ItemStatusEnum.DISTRIBUTED)) {
			return  "Distributed";
		}

		//if all else fails
		return  "Undefined";

	}

	private ItemStatusEnum getItemStatusEnumForName( String name ) 
	{
		if ( StringUtils.isNotEmpty(name) ) 
		{
			for ( ItemStatusEnum en : ItemStatusEnum.values() ) 
			{
				if ( name.equalsIgnoreCase(en.name())) 
				{
					return en;
				} 
			}
		}
		return null;
	}
	
}