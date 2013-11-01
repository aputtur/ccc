package com.copyright.ccc.web.util;

import com.copyright.ccc.business.data.OrderBundle;
import com.copyright.ccc.business.data.OrderLicense;
import com.copyright.ccc.business.data.OrderPurchase;
import com.copyright.ccc.business.services.user.User;

/*
 * Wrapper class for holding an OrderPurchase and OrderLicense object
 * for use in JSP processing
 */
public class ReportRow 
{
   private OrderPurchase orderPurchase;
   private OrderLicense orderLicense;  
   private OrderBundle orderBundle;
   private User user; 
//   private ItemWrapper itemWrapper;
       
   public ReportRow(OrderPurchase p, OrderBundle b, OrderLicense l, User u) {
      this.orderPurchase = p;
      this.orderBundle = b;
      this.orderLicense = l;
      this.user = u;
   }
       
   public void setOrderPurchase(OrderPurchase op) { this.orderPurchase = op; } 
   public OrderPurchase getOrderPurchase() { return this.orderPurchase; }

   public void setOrderBundle(OrderBundle ob) { this.orderBundle = ob; } 
   public OrderBundle getOrderBundle() { return this.orderBundle; }
       
   public void setOrderLicense(OrderLicense ol) { this.orderLicense = ol; } 
   public OrderLicense getOrderLicense() { return this.orderLicense; }

   public void setUser(User user) { this.user = user; }
   public User getUser() { return user; }


}
