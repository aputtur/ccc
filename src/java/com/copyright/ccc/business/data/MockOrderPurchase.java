package com.copyright.ccc.business.data;

import com.copyright.ccc.business.services.mock.MockPurchase;
import com.copyright.ccc.business.services.order.OrderPurchaseImpl;
import com.copyright.data.order.Purchase;

public class MockOrderPurchase extends OrderPurchaseImpl implements OrderPurchase
{
	private static final long serialVersionUID = 1L;
	
    public MockOrderPurchase(Purchase purch, String titles) { 
       super.setPurchase(purch);
       this.setPurchaseTitles(titles);
    }

    public void setPurchaseTitles(String titles) {
       super._purchaseTitles = titles;
    }

    
    public void setClosed(boolean closed) {
       MockPurchase mockPurchase = (MockPurchase) super.getPurchase();
       mockPurchase.setOpen(!closed);
    }

    public void setCancelable(boolean isCancelable) {
       MockPurchase mockPurchase = (MockPurchase) super.getPurchase();
       mockPurchase.setCancelable(isCancelable);
    }

	@Override
	public long getConfirmationNumber() {
		// TODO Auto-generated method stub
		return 0;
	}
}
