package com.copyright.ccc.business.services.mock;

import java.sql.Date;

import com.copyright.data.order.Purchase;

/*
 * Mock Purchase class to allow setting of Purchase attributes in 
 * simulation to an Order Management Services call
 */
public class MockPurchase extends Purchase {

   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public MockPurchase(long purInst, long c, Date purchaseDate) {
      setPurInst(purInst);
      setDetailCount(c);
      setPurchaseDate(purchaseDate);
   }
   
    public MockPurchase(long purInst, long c, Date purchaseDate, String poNum) {
       this(purInst, c, purchaseDate);
       this.setPoNumber(poNum);
    }

    @Override
   public void setBillingReference(String ref)
   {
      super.billingReference = ref;
   }	

//   public void setTransactionId(String id)
//   {
//      super.transactionId = id;
//   }
   
   public void setPurchaseDate(Date d)
   {
      super.purchaseDate = d;
   }
   
    public void setPurInst(long id)
    {
       super.purInst = id;
    }
    
    public void setDetailCount(long count)
    {
       super.detailCount = count;
    }
    
    @Override
    public void setPoNumber(String poNum)
    {
       super.poNumber = poNum;
    }
    
    public void setOpen(boolean isOpen) {
        super.isOpen = isOpen;
    }

    public void setCancelable(boolean isCancelable) {
        super.isCancelable = isCancelable;
    }
}
