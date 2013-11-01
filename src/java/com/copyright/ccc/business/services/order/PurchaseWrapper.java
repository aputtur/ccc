package com.copyright.ccc.business.services.order;
import java.sql.Date;

import com.copyright.ccc.business.data.OrderPurchase;
import com.copyright.data.order.CoursePack;
import com.copyright.data.order.Purchase;

public class PurchaseWrapper extends Purchase{
	
	private static final long serialVersionUID = 1L;
	private OrderPurchase orderPurchase;
	
	public PurchaseWrapper(OrderPurchase orderPurchase){
		this.orderPurchase = orderPurchase;
	}
	
	@Override
	public boolean isAcademic()
	{
		return orderPurchase.isAcademic();
	}
	
	@Override
	public String getBillingReference()
	{
		return orderPurchase.getBillingReference();
	}

	@Override
	public String getPoNumber()
	{
		return orderPurchase.getPoNumber();
	}

	@Override
	public long getByrInst()
	{
		return orderPurchase.getByrInst();
	}

	public static long getSerialVersionUID()
	{
		return serialVersionUID;
	}

	@Override
	public Date getPurchaseDate()
	{
		 
		return new java.sql.Date(orderPurchase.getPurchaseDate().getTime());
	    
	}

	@Override
	public long getPurInst()
	{
		return orderPurchase.getPurInst();
	}

	@Override
	public long getDetailCount()
	{
		return orderPurchase.getDetailCount();
	}
	
	@Override
	public boolean isShowPurchase()
	{
		return orderPurchase.isShowPurchase();
	}

	
	
	@Override	
	public boolean isOpen()
	{
		return orderPurchase.isOpen();
	}
	
	@Override
	public boolean isCancelable()
	{
		return orderPurchase.isCancelable();
	}

//	public boolean isNumStudentsSame()
	//{
	//	return orderPurchase.getNumberOfStudents();
	//}

	
	@Override
	public String getPromoCode()
	{
		return orderPurchase.getPromoCode();
	}

	@Override
	public CoursePack getSingleCoursePack()
	{
		return orderPurchase.getCoursePack();
	}

	

	

	//public String getTitleList()
	//{
		//return orderPurchase.get;
	//}

	@Override
	public String getCreUser()
	{
		return orderPurchase.getUser().getUsername();
	}

	

}
