package com.copyright.ccc.business.services.order;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.util.LogUtil;
import com.copyright.svc.order.api.OrderService;
import com.copyright.svc.order.api.OrderServiceLocator;
import com.copyright.svc.order.api.data.AuditRecord;
import com.copyright.svc.order.api.data.Bundle;
import com.copyright.svc.order.api.data.OrderConsumerContext;

public class BundleAuditServices
{
	private static Logger LOGGER = Logger.getLogger(BundleAuditServices.class);
	OrderService orderSvc = ServiceLocator.getOrderService();
	
	//enumeration to define the bundle property elements and their display values
	//If new properties are requested to show on the audit info then the columns
	//directly on the bundle table should be added here.
	//One of the reasons for the enum is to match a display value with a parm/field
	//name in the ui.  The other is so that the fields being audited can be
	//manipulated if required in the future by breaking out the enums into
	//stand alone enums.
	private enum BundlePropertyEnum
	{
		COMMENTS("Comments", "Comments"),
		BUNDLE_NAME("BundleName","Course Name"),
		REF_NUMBER("RefNumber", "Course Number"),
		START_OF_USE_DATE("StartOfUseDtm", "Start of Term"),
		ORGANIZATION("Organization", "University/Institution");
			
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

		private BundlePropertyEnum(String valueName, String displayValue)
		{
			this.valueName = valueName;
			this.displayValue = displayValue;
		}
	}
	
	//enumeration to define the bundle parm elements and their display values.
	//If new properties are requested to show on the audit info then the values
	//from the bunlde_parm table should be added here.
	//One of the reasons for the enum is to match a display value with a parm/field
	//name in the ui.  The other is so that the fields being audited can be
	//manipulated if required in the future by breaking out the enums into
	//stand alone enums.
	private enum BundleParmEnum 
	{
		ESTIMATED_COPIES("ESTIMATED_COPIES", "Est No of Students"),
		NUM_STUDENTS("NUM_STUDENTS", "No of Students"),
		INSTRUCTOR("INSTRUCTOR", "Instructor Name"),
		LCN_BUNDLE_REFERENCE("LCN_BUNDLE_REFERENCE","Document Ref Num"),
		LCN_BUNDLE_ACCOUNTING_REFERENCE("LCN_BUNDLE_ACCOUNTING_REFERENCE", "Accounting Ref Num");
		
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

		private BundleParmEnum(String valueName, String displayValue)
		{
			this.valueName = valueName;
			this.displayValue = displayValue;
		}
	}

	public List<AuditRecord> getBundleAudit(Long bundleId)
	{
		if (bundleId == null)
		{
			LOGGER.error("getBundleAudit was passed a null bundleId." );
			
			throw new IllegalArgumentException( "getBundleAudit was passed a null bundleId." );
		}
		
		//put fieldNames into a list so we can loop through them
		List <BundlePropertyEnum> fieldNames = new ArrayList <BundlePropertyEnum>();
		fieldNames.add(BundlePropertyEnum.COMMENTS);
		fieldNames.add(BundlePropertyEnum.BUNDLE_NAME);
		fieldNames.add(BundlePropertyEnum.REF_NUMBER);
		fieldNames.add(BundlePropertyEnum.START_OF_USE_DATE);
		fieldNames.add(BundlePropertyEnum.ORGANIZATION);
		
		//put parmNames into a list so we can loop through them
		List <BundleParmEnum> parmNames = new ArrayList <BundleParmEnum>();
		parmNames.add(BundleParmEnum.NUM_STUDENTS);
		parmNames.add(BundleParmEnum.INSTRUCTOR);
		parmNames.add(BundleParmEnum.LCN_BUNDLE_REFERENCE);
		parmNames.add(BundleParmEnum.LCN_BUNDLE_ACCOUNTING_REFERENCE);
		parmNames.add(BundleParmEnum.ESTIMATED_COPIES);
		
		List<AuditRecord> changeRecs = new ArrayList<AuditRecord>();

		OrderConsumerContext orderConsumerContext = new OrderConsumerContext();
		List<Bundle> bundleList = orderSvc.getBundleHistory(
				orderConsumerContext, bundleId);

		if (bundleList.size() == 0)
		{
			return changeRecs;
		}
		
		// the first version is the baseline
		Bundle compBundle = bundleList.get(0); 
		
		for (int bundleIdx = 1; bundleIdx < bundleList.size() + 1; bundleIdx++)
		{

			Bundle nextBundle;

			// if we reach the last bundle history record then we want to compare
			// it against the current bundle record
			// (not a history record)
			if (bundleIdx == bundleList.size())
			{
				nextBundle = orderSvc.getBundleById(orderConsumerContext,
						bundleId);
			} else
			{
				nextBundle = (Bundle) bundleList.get(bundleIdx);
			}
			
			//get the property values that are directly on the bundle
			for (BundlePropertyEnum bpe : fieldNames)
			{
				String mName = "get" + bpe.getValueName();
				
				Method mtd = null;
				try
				{
					mtd = Bundle.class.getMethod(mName);
				} catch (Exception e)
				{
					LOGGER.error("Error getting property values from bundle history : " + bundleId + " - " + LogUtil.appendableStack(e));
					
					RuntimeException re = new RuntimeException("Error getting property values from bundle history : " + bundleId + " - " + LogUtil.appendableStack(e)); 
					throw re;
				}

				Object oldValue = null;
				try
				{
					oldValue = mtd.invoke(compBundle);
				} catch (Exception e)
				{
					LOGGER.error("Error getting old property values from bundle history : " + bundleId + " - " + LogUtil.appendableStack(e));
					RuntimeException re = new RuntimeException("Error getting old property values from bundle history : " + bundleId + " - " + LogUtil.appendableStack(e)); 
					throw re;
				}

				Object newValue = null;
				try
				{
					newValue = mtd.invoke(nextBundle);
				} catch (Exception e)
				{
					LOGGER.error("Error getting new property values from bundle history : " + bundleId + " - " + LogUtil.appendableStack(e));
					RuntimeException re = new RuntimeException("Error getting new property values from bundle history : "  + bundleId + " - " + LogUtil.appendableStack(e)); 
					throw re;
				}
				
				if ((oldValue != null && !oldValue.equals(newValue))
						|| (oldValue == null && newValue != null))
				{
					AuditRecord ar = new AuditRecord(bpe.getDisplayValue(),
							bundleId, nextBundle
									.getBundleVersion(), oldValue, newValue,
							nextBundle.getUpdateDate(), nextBundle
									.getUpdateUser());

					changeRecs.add(ar);
				}
			}

			//get the bundle parm values that are associated with the bundle
			for (BundleParmEnum bpe : parmNames)
			{
				
				Object oldValue = null;
				
				if (compBundle.getBundleParms().get(
						bpe.getValueName()) != null )
				{
					oldValue = compBundle.getBundleParms().get(bpe.getValueName())
					.getParmValue();
				}
				
				Object newValue =  null;
				
				if (nextBundle.getBundleParms().get(
						bpe.getValueName()) != null )
				{
					newValue = nextBundle.getBundleParms().get(bpe.getValueName())
					.getParmValue();
				}
				
				if ((oldValue != null && !oldValue.equals(newValue))
						|| (oldValue == null && newValue != null))
				{
					AuditRecord ar = new AuditRecord(bpe.displayValue,
							bundleId, nextBundle
									.getBundleVersion(), oldValue, newValue,
							nextBundle.getUpdateDate(), nextBundle
									.getUpdateUser());

					changeRecs.add(ar);
				}
			}

			compBundle = nextBundle;
		}

		Collections.sort(changeRecs);
		
		return changeRecs;
	}
}
