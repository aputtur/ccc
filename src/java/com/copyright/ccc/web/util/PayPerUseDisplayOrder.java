package com.copyright.ccc.web.util;

import com.copyright.base.enums.ProductEnum;

public enum PayPerUseDisplayOrder  {
	 APS(0,ProductEnum.APS),
	 TRS(1,ProductEnum.TRS),
	 ECC(2,ProductEnum.ECC),
	 DPS(3,ProductEnum.DPS),
	 RLS(4,ProductEnum.RLS);
	 
	private ProductEnum[] ppDisplayOrderList = {ProductEnum.APS,ProductEnum.TRS,ProductEnum.ECC,ProductEnum.DPS,ProductEnum.RLS};
	private ProductEnum[] annualDisplayOrderList = {ProductEnum.AAS,ProductEnum.DRA,ProductEnum.ARS};

	private ProductEnum productEnum;
	private int orderIndex;
	
	private PayPerUseDisplayOrder(int orderIndex, ProductEnum productEnum){
		
		this.orderIndex = orderIndex;
		this.productEnum = productEnum;
	}
	
	public ProductEnum getProductEnum() {
		return productEnum;
	}
	
	public int getOrderIndex() {
		return orderIndex;
	}
	
	public static PayPerUseDisplayOrder getDisplayOrder(ProductEnum productEnum){
		for (PayPerUseDisplayOrder payPerUseDisplayOrder : values()){
			if (payPerUseDisplayOrder.getProductEnum().equals(productEnum)){
				return payPerUseDisplayOrder;
			}
		}
		return null;
	}

}
