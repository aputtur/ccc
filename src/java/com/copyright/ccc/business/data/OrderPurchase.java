package com.copyright.ccc.business.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import com.copyright.ccc.business.services.user.User;
import com.copyright.data.order.CoursePack;
import com.copyright.data.order.Purchase;


public interface OrderPurchase  extends Serializable
{
	@Deprecated
	public void setPurchase (Purchase purchase);

	@Deprecated
	public Purchase getPurchase(); 
    
    public User getUser();

    public void setUser(User user);
    
    public boolean isOpen();

    public boolean isClosed();
    
    public boolean isAcademic();
    
    public boolean isCancelable();
            
    public String getPurchaseTitles();
    
    public ArrayList<OrderBundle> getOrderBundles();
               
    public String getBillingReference();
    
    public void setBillingReference(String billingReference);
    
    public long getByrInst();
    
    public long getDetailCount();

    public String getPoNumber();

    public void setPoNumber(String poNumber);
    
    public Date getPurchaseDate();

    // This is now getConfirmationNumber
    @Deprecated
    public long getPurInst(); 
    
    public Long getOrderHeaderId();
    
    public long getConfirmationNumber();
    
    public boolean isShowPurchase();

    public void setShowPurchase(boolean showPurchaseFlag);
    
    // All of the deprecated attributes below can be removed as they are now bundle attributes
    @Deprecated
    public CoursePack getCoursePack();
    
    @Deprecated
    public String getCourseName();
    
    @Deprecated
    public String getCourseNumber();
    
    @Deprecated
    public long getNumberOfStudents();

//    public boolean isNumStudentsSame();
    
    public String getPromoCode();
      
    @Deprecated
    public String getSchool();
       
    @Deprecated
    public String getInstructor(); 
        
    @Deprecated
    public String getYourReference();

//    public String getComments();
    
    @Deprecated
    public Date getStartOfTerm(); 

//    public String getStartOfTermStr();
    
    @Deprecated
    public String getAccountingReference(); 
        
    @Deprecated
    public String getOrderEnteredBy(); 
    
	public String getOrderSourceCd();
    
    public String getOrderDateStr();
      
    public Date getOrderDate(); 

    @Deprecated
    public void setSchool (String school);
    
    @Deprecated
    public void setCourseNumber (String courseNumber);
    
    @Deprecated   
    public void setCourseName (String courseName);
    
    @Deprecated
    public void setNumberOfStudents (long numberOfStudents);
    
    @Deprecated
    public void setInstructor (String instructor); 
    
	@Deprecated
	public void setYourReference (String yourReference);

//    public void setComments (String comments);
    
	@Deprecated
	public void setAccountingReference (String accountingReference);
         
	@Deprecated
	public void setOrderEnteredBy (String orderEnteredBy);
    
    @Deprecated
    public void setStartOfTerm(Date startOfTerm);    
    
	public Long getLicenseeAccount();
	
	public String getLicenseeName();

	public String getBuyerUserIdentifier();
	
	public int getOrderDataSource();
	
	public String getOrderDataSourceDisplay();
	
	public String getOrderEnteredByUserIdentifier();
	
	public String getOrderStatusCd();
	
	public String getOrderStatusDisplay();
	
}
