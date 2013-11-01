/**
 * 
 */
package com.copyright.ccc.business.services.invoice;

import com.copyright.ccc.business.data.IAutoDunningParam;
import com.copyright.opi.data.StandardDO;

/**
 * @author wlee
 *
 */
public class AutoDunningParamImpl extends StandardDO implements IAutoDunningParam
{
	private static final long serialVersionUID = 1L;
	
	protected String productType;
	protected Integer daysPastDue;
	protected Integer enabled;
	
	public String getProductType()
	{
		return productType;
	}
	public void setProductType(String productType)
	{
		this.productType = productType;
	}
	public Integer getDaysPastDue()
	{
		return daysPastDue;
	}
	public void setDaysPastDue(Integer daysPastDue)
	{
		this.daysPastDue = daysPastDue;
	}
	public Integer getEnabled()
	{
		return enabled;
	}
	public void setEnabled(Integer enabled)
	{
		this.enabled = enabled;
	}

}
