package com.copyright.ccc.business.services.order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.copyright.ccc.business.data.OrderBundle;
import com.copyright.ccc.business.data.OrderPurchase;
import com.copyright.ccc.business.services.OrderDataSourceEnum;
import com.copyright.ccc.business.services.user.User;
import com.copyright.data.order.CoursePack;
import com.copyright.data.order.Purchase;
import com.copyright.svc.order.api.data.Bundle;
import com.copyright.svc.order.api.data.OrderHeader;

public class OrderHeaderImpl implements OrderPurchase {

	private static final long serialVersionUID = 1L;
	
    OrderHeader _orderHeader;
    public String _purchaseTitles;
    User _user;

  
    public void setPurchase (Purchase purchase) {
//  	TODO This is here to meet the interface spec which needs to be
//		TODO eliminated or refactored to Object
    }
    
    public Purchase getPurchase() {
//  	TODO This is here to meet the interface spec which needs to be
//		TODO eliminated or refactored to Object
//        if (_purchase == null) {
//           _purchase = new Purchase();
//        }
        return new Purchase();
    }
    
    public void setOrderHeader (OrderHeader orderHeader) {
//  	TODO This is here to meet the interface spec which needs to be
//		TODO eliminated or refactored to Object
    	_orderHeader = orderHeader;
    }
    
    // KM: I'm thinking the if logic below is not needed
    public OrderHeader getOrderHeader() {
        if (_orderHeader == null) {
           // Keith: Is this OK? It keeps reporting from choking.
           _orderHeader = new OrderHeader();
        }
        return _orderHeader;
    }
    
    public User getUser() {
        return _user;
    }

    public void setUser(User user) {
        _user = user;
    }
    
    public boolean isOpen() {
     	if (getOrderHeader().getOpenCount() > 0) {
    		return true;
    	}
    	
    	return false;
    }

    public boolean isClosed() {
        if (this.isOpen()) {
            return false; }
        else {
            return true;
        }
    }

    public boolean isAcademic() {

    	return !this.getOrderBundles().isEmpty();
   
/*    	return this.getPurchase().isAcademic();
//        if ( (this.getCoursePack().getNumStudents() == null || this.getCoursePack().getNumStudents().longValue() == 0) &&
//            this.getCoursePack().getCourseNumber() == null) {
//            return false; }
//        else {
//            return true;
//        }
*/        
    }

    public boolean isCancelable() {
    	return this.isOpen();
    }
        
    public String getPurchaseTitles() {
    	
    	StringBuffer singleTitles = new StringBuffer();
    	boolean firstTitle = true;
    	if (getOrderHeader().getSingleTitles() != null) {
    		for (String singleTitle : getOrderHeader().getSingleTitles()) {
    			if (firstTitle == true) {
    				singleTitles.append(singleTitle);
    				firstTitle = false;
    			} else {
    				singleTitles.append(", " + singleTitle);
    				break;
    			}
    		}
    	}
    	if (singleTitles.length() > 0) {
            return singleTitles.toString();
    	}
    	return null;
    }
    
//    public void setPurchaseTitles (String purchaseTitles) {
//        _purchaseTitles = purchaseTitles;
//    }
           
    public String getBillingReference() {
    	return this.getOrderHeader().getBillingReference();
    }    

    public void setBillingReference(String billingReference) {
    	this.getOrderHeader().setBillingReference(billingReference);
    }
    
    public long getByrInst() {
    	//TODO this is null is some cases
    	// return should probably be Long
    	return this.getOrderHeader().getBuyerPtyInst();
//        return this.getPurchase().getByrInst();
    }    

    public long getDetailCount() {
    	return this.getOrderHeader().getSingleItemCount();
    }    

//    public boolean getModified() {
//        return _purchase.getIsModified();
//    }    

    public String getPoNumber() {
        return this.getOrderHeader().getPoNum();
    }    

    public void setPoNumber(String poNumber) {
    	this.getOrderHeader().setPoNum(poNumber);
    }
    
    public Date getPurchaseDate() {
    	//TODO Activate the order date
    	return this.getOrderHeader().getOrderDtm();
    }    

    // Use getConfirmNumber
//	Deprecated
    public long getPurInst() {
    	Long confirmNumber = this.getOrderHeader().getConfirmNumber();
    	
		if (confirmNumber != null) {
			  return confirmNumber.longValue();
		} else {
	  		return 0;
	  	}
    } 

    public Long getOrderHeaderId() {
        return getOrderHeader().getOrderHeaderId();
    } 

    public String getOrderStatusCd() {
        return getOrderHeader().getOrderStatus().getCode();
    } 

    public String getOrderStatusName() {
        return getOrderHeader().getOrderStatus().name();
    } 

    public String getOrderStatusDisplay() {
        return getOrderHeader().getOrderStatus().getDescription();
    } 

    
    public long getConfirmationNumber() {
    	if (this.getOrderHeader().getConfirmNumber() != null) {
        	return this.getOrderHeader().getConfirmNumber().longValue();   		
    	} else {
    		return 0L;
    	}
    	
    } 

    
    /**
    * isShowPurchase is a boolean used by the UI to determine if the Purchase
    * has been Deleted.  A Deleted Purchase should not be shown in the UI.
    * 
    * @return true if the OrderPurchase should still be visible to the user in the UI.
    * Otherwise return false;
    */
    public boolean isShowPurchase() {
    	if (getOrderHeader().getHidden() != null && getOrderHeader().getHidden().booleanValue() == true) {
    		return false;
    	} else {
    		return true;
    	}
    	
    	//TODO Logic based on status
//        return this.getPurchase().isShowPurchase();
    }    

    /** 
    * Setter for the Purchase showPurchase flag to determine if the Purchase
    * should be visible to the user in the UI.
    * @param showPurchaseFlag is true if the Purchase is not Deleted and false if 
    * the Purchase has been deleted and should not be shown in Order History
    */
    public void setShowPurchase(boolean showPurchaseFlag)
    {
    	if (showPurchaseFlag == false) {
    		getOrderHeader().setHidden(Boolean.TRUE);
    	} else {
    		getOrderHeader().setHidden(Boolean.FALSE);
    	}
    	
    	//TODO Logic based on status
//       if (_purchase != null) {
//          _purchase.setShowPurchase(showPurchaseFlag);
//       }           
    }
        
//    public String getTransactionId() {
//        return _purchase.getTransactionId();
//    }    
    
    @Deprecated
    public CoursePack getCoursePack() {
    	
    	return new CoursePack();
/*        if (_purchase != null) {
           if (_purchase.getSingleCoursePack() == null) {
              _purchase.setSingleCoursePack(new CoursePack());
           }
        } else {
            _purchase = new Purchase();
            _purchase.setSingleCoursePack(new CoursePack());
        }
        
        return _purchase.getSingleCoursePack();
*/
    }

    public ArrayList<OrderBundle> getOrderBundles() {
    	ArrayList<OrderBundle> orderBundles = new ArrayList<OrderBundle>();
    	for (Bundle bundle : getOrderHeader().getBundles()) {
    		OrderItemBundleImpl orderBundleImpl = new OrderItemBundleImpl();
    		orderBundleImpl.setBundle(bundle);
    		orderBundles.add(orderBundleImpl);
    	}
    	return orderBundles;
    }
    
// The following deprecated fields are now sourced from the bundle and can be deleted.    
    @Deprecated
    public String getCourseName() { 

    	return null;
/*        return this.getCoursePack().getCourseName();
//       String name = "";
//       CoursePack pack = _purchase.getSingleCoursePack();
//       if (pack != null) {
//         name = pack.getCourseName();
//       }
//       return name; 
 */
    }
    
    @Deprecated
    public String getCourseNumber() 
    { 
    	return null;
//    	return this.getCoursePack().getCourseNumber();
    }

    @Deprecated
    public long getNumberOfStudents() 
    {
    	return 0;
/*    	if (this.getCoursePack().getNumStudents() == null) {
            return 0;
        } else {
            return this.getCoursePack().getNumStudents().longValue();
        }
*/
    }
    
//    public String getNumStudSameFlg() {        
//        return null;
//    }

    @Deprecated
    public boolean isNumStudentsSame() {
    	return true;
//        return this.getPurchase().isNumStudentsSame();
    }
    
    @Deprecated
    public String getPromoCode() {
    	return null;
    }
    
    @Deprecated
    public String getSchool() 
    { 
    	return null;
//        return this.getCoursePack().getUniversityName();
    }
       
    @Deprecated
    public String getInstructor() 
    { 
    	return null;
//    	return this.getCoursePack().getInstructorName();
    }
       
    @Deprecated
    public String getYourReference() { 
    	return null;
//    	return this.getCoursePack().getHeaderRef();   // ?? changed from _purchase.getLcnHeaderRefNum();  
    }

    @Deprecated
    public String getComments() { 
    	return null;
//    	return this.getCoursePack().getOrderEnteredBy(); 
    }
    
    @Deprecated
    public Date getStartOfTerm() 
    {      
    	return null;
//       return this.getCoursePack().getStartOfTerm(); 
    }

    // Should also be deprecated/removed
    public String getStartOfTermStr() {
    	return null;
    	
/*    	String termStartStr = "";
       Date startOfTerm = this.getStartOfTerm();
       if (startOfTerm != null) {
           SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
           termStartStr = format.format(startOfTerm);                   
       }

       return termStartStr;
*/
    }
    
    @Deprecated
    public String getAccountingReference() 
    { 
    	return this.getOrderHeader().getLcnAcctRefNum();
//    	return this.getCoursePack().getAcctingRef();
    }
        
//    public String getOrderEnteredBy() { 
//    	return this.getOrderHeader().getOrderEnteredBy();
//    }

    // This is a bundle attribute and can be removed from here
    public String getOrderEnteredBy() { 
    	return null;
    }
    
    // This field is not referenced in the UI and can be removed
    public String getOrderEnteredByUserIdentifier() { 
    	return this.getOrderHeader().getOrderEnteredBy();
    }

    public String getOrderCreatedBy() {
    	return this.getOrderHeader().getCreateUser();
    }
    
    public long getLicenseePartyId() { 
    	
    	Long licenseePartyId = this.getOrderHeader().getLicenseePartyId();
    	
		if (licenseePartyId != null) {
			  return licenseePartyId.longValue();
		} else {
	  		return 0;
	  	}
    	
    }

    public void setLicenseePartyId(long licenseePartyId) { 
    	this.getOrderHeader().setLicenseePartyId(licenseePartyId);
    }
    
    public long getLicenseePtyInst() { 
    	
    	Long licenseePtyInst = this.getOrderHeader().getLicenseePtyInst();
    	
		if (licenseePtyInst != null) {
			  return licenseePtyInst.longValue();
		} else {
	  		return 0;
	  	}
    	
    }

    public void setLicenseePtyInst(long licenseePtyInst) { 
    	this.getOrderHeader().setLicenseePtyInst(licenseePtyInst);
    }

    /**
    * This method supports the UI order date display field.  It gets
    * the Purchase Date from this class' aggregate Purchase object
    * and formats it to a display string for the JSP
    * @return the purchase order date as a String
    */
     public String getOrderDateStr() {
        Date orderDate = this.getOrderHeader().getOrderDtm();
        String orderDateStr = "";
        if (orderDate != null) {
           SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy");
           orderDateStr = format.format(orderDate);            
        }
    
        return orderDateStr;
     }
    
    public String getOrderSourceCd() {
        return this.getOrderHeader().getOrderSource().getCode();
    }
     
    public Date getOrderDate() {
       return this.getOrderHeader().getOrderDtm();
    }

    // The following deprecated attributes can be removed from here
    @Deprecated
    public void setSchool (String school)  { 
//    	this.getCoursePack().setUniversityName(StringUtils.upperCase(school)); 
    }
    
    @Deprecated
    public void setCourseNumber (String courseNumber)  { 
//        this.getCoursePack().setCourseNumber(StringUtils.upperCase(courseNumber)); 
    }
    
    @Deprecated
    public void setCourseName (String courseName)  { 
//        this.getCoursePack().setCourseName(StringUtils.upperCase(courseName)); 
    }
    
    @Deprecated
    public void setNumberOfStudents (long numberOfStudents)  {
//        this.getCoursePack().setNumStudents(numberOfStudents); 
    }
    
    @Deprecated
    public void setInstructor (String instructor)  {
//        this.getCoursePack().setInstructorName(StringUtils.upperCase(instructor)); 
    }
    
    @Deprecated
    public void setYourReference (String yourReference)  {
//        this.getCoursePack().setHeaderRef(StringUtils.upperCase(yourReference)); 
    }

    @Deprecated
    public void setComments (String comments)  {
//        this.getCoursePack().setOrderEnteredBy(comments); 
    }
    
    @Deprecated
    public void setAccountingReference (String accountingReference)  {
//        this.getCoursePack().setAcctingRef(StringUtils.upperCase(accountingReference)); 
    }
    
    // This is now a bundle attribute and can be deprecated / removed
    public void setOrderEnteredBy (String orderEnteredBy)  {
        this.getOrderHeader().setOrderEnteredBy(orderEnteredBy); 
    }
    
    @Deprecated
    public void setStartOfTerm(Date startOfTerm) {
//        this.getCoursePack().setStartOfTerm(startOfTerm); 
    }
    
	public Long getLicenseeAccount() {
		return Long.valueOf(_orderHeader.getLicenseeAccount());
	}
	
	public String getLicenseeName() {
		return _orderHeader.getLicenseeName();
	}

	public String getBuyerUserIdentifier() {
		return _orderHeader.getBuyerUserIdentifier();
	}
	public int getOrderDataSource() {
		return OrderDataSourceEnum.OMS.getOrderDataSourceId().intValue();
	}
	public String getOrderDataSourceDisplay() {
		return OrderDataSourceEnum.OMS.name();		
	}

	public Long getBuyerPartyId() {
		return getOrderHeader().getBuyerPartyId();
	}

	public void setBuyerPartyId(Long buyerPartyId) {
		getOrderHeader().setBuyerPartyId(buyerPartyId);
	}

	public Long getBuyerPtyInst() {
		return getOrderHeader().getBuyerPtyInst();
	}

	public void setBuyerPtyInst(Long buyerPtyInst) {
		getOrderHeader().setBuyerPtyInst(buyerPtyInst);
	}

	public String getBuyerName() {
		return getOrderHeader().getBuyerName();
	}

	public void setBuyerName(String buyerName) {
		getOrderHeader().setBuyerName(buyerName);
	}

	public Boolean getPurchaseOnHold() {
		return getOrderHeader().getPurchaseOnHold();
	}

	public void setPurchaseOnHold(Boolean purchaseOnHold) {
		getOrderHeader().setPurchaseOnHold(purchaseOnHold);
	}

	public void setBuyerUserIdentifier(String buyerUserIdentifier) {
		getOrderHeader().setBuyerUserIdentifier(buyerUserIdentifier);
	}

	public void setPromoCode(String promoCode) {
		getOrderHeader().setPromoCode(promoCode);
	}

	public void setLicenseeName(String licenseeName) {
		getOrderHeader().setLicenseeName(licenseeName);
	}

	public void setLicenseeAccount(String licenseeAccount) {
		getOrderHeader().setLicenseeAccount(licenseeAccount);
	}
	
}