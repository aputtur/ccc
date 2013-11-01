package com.copyright.ccc.business.services.search;

import com.copyright.svc.rightsResolver.api.data.RightFee;

public class RightFeeAdapter {
	Long rightId;
	String feeName;
	String feeValue;
	Long sort;
	RightFee rrrightFee = null;
	boolean isRRRightFee = false;
	
	public RightFeeAdapter(RightFee rrrightFee){
		this.rrrightFee = rrrightFee;
		isRRRightFee = true;
	}
	public String getFeeName()
	{
		return this.rrrightFee.getFeeName();

	}
	public void setFeeName(String feeName)
	{
		this.feeName = feeName;
	}
	public String getFeeValue()
	{
		return feeValue;
	}
	public void setFeeValue(String feeValue)
	{
		this.feeValue = feeValue;
	}
	public Long getSort()
	{
		return sort;
	}
	public void setSort(Long sort)
	{
		this.sort = sort;
	}
	public Long getRightId()
	{
		return rightId;
	}
	public void setRightId(Long rightId)
	{
		this.rightId = rightId;
	}
	
	public boolean getIsRRRightFee(){
		return isRRRightFee;
	}
}
