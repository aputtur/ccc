/**
 * 
 */
package com.copyright.ccc.business.data;

/**
 * @author wlee
 *
 */
public interface IAutoDunningParam
{
	public String getProductType();
	public void setProductType(String productType);
	public Integer getDaysPastDue();
	public void setDaysPastDue(Integer daysPastDue);
	public Integer getEnabled();
	public void setEnabled(Integer enabled);

}
