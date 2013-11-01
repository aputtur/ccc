package com.copyright.ccc.business.services.order;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.copyright.ccc.business.data.OrderBundle;
import com.copyright.data.order.CoursePack;
import com.copyright.data.order.Purchase;

public class OrderPurchaseBundleImpl implements OrderBundle {

	    Purchase _purchase;
	    String _comments;
   
	    public void setPurchase (Purchase purchase) {
	       _purchase = purchase;
	    }
	    
	    public Purchase getPurchase() {
	        if (_purchase == null) {
	           // Keith: Is this OK? It keeps reporting from choking.
           _purchase = new Purchase();
        }
        return _purchase;
    }
    	    
    public boolean isAcademic() {

        return this.getPurchase().isAcademic();
//		        if ( (this.getCoursePack().getNumStudents() == null || this.getCoursePack().getNumStudents().longValue() == 0) &&
//		            this.getCoursePack().getCourseNumber() == null) {
//		            return false; }
//		        else {
//		            return true;
//		        }
    }

    public long getBundleId() {
        return this.getPurchase().getPurInst();
    } 

    public CoursePack getCoursePack() {
        if (_purchase != null) {
           if (_purchase.getSingleCoursePack() == null) {
              _purchase.setSingleCoursePack(new CoursePack());
           }
        } else {
            _purchase = new Purchase();
            _purchase.setSingleCoursePack(new CoursePack());
        }
        
        return _purchase.getSingleCoursePack();
    }

    public String getCourseName() { 
    
        return this.getCoursePack().getCourseName();
//		       String name = "";
//		       CoursePack pack = _purchase.getSingleCoursePack();
//		       if (pack != null) {
//		         name = pack.getCourseName();
//		       }
//		       return name; 
    }
    
    public String getCourseNumber() 
    { 
        return this.getCoursePack().getCourseNumber();
    }

    public long getNumberOfStudents() 
    {
        if (this.getCoursePack().getNumStudents() == null) {
            return 0;
        } else {
            return this.getCoursePack().getNumStudents().longValue();
        }
    }
    
    public Long getNumberOfStudentsLong() {
        if (this.getCoursePack().getNumStudents() == null) {
            return 0L;
        } else {
            return this.getCoursePack().getNumStudents();
        }
    }
    
    public Long getEstimatedCopies() {
    	return null;
    }
    
//		    public String getNumStudSameFlg() {        
//		        return null;
//		    }

    public boolean isNumStudentsSame() {
        return this.getPurchase().isNumStudentsSame();
    }
    
    @Deprecated
    public String getSchool() 
    { 
        return this.getCoursePack().getUniversityName();
    }

    public String getOrganization() 
    { 
        return this.getCoursePack().getUniversityName();
    }
       
    public String getInstructor() 
    { 
        return this.getCoursePack().getInstructorName();
    }
       
    public String getYourReference() { 
       return this.getCoursePack().getHeaderRef();   // ?? changed from _purchase.getLcnHeaderRefNum();  
    }

    public String getComments() { 
    	return _comments; 
    }

    
    public Date getStartOfTerm() 
    {      
        return this.getCoursePack().getStartOfTerm(); 
    }

    public String getStartOfTermStr() {
       String termStartStr = "";
       Date startOfTerm = this.getStartOfTerm();
       if (startOfTerm != null) {
           SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
           termStartStr = format.format(startOfTerm);                   
       }

       return termStartStr;
    }
    
    public String getAccountingReference() 
    { 
        return this.getCoursePack().getAcctingRef();
    }
        
    public String getOrderEnteredBy() { 
       return this.getCoursePack().getOrderEnteredBy();
    }
    
    public long getDetailCount() {
        return this.getPurchase().getDetailCount();
    }    

    public String getBundleTitles() {
    	return this.getPurchase().getTitleList(); 
    }
    
    public void setOrganization (String school)  { 
        this.getCoursePack().setUniversityName(StringUtils.upperCase(school)); 
    }
    
    @Deprecated
    public void setSchool (String school)  { 
        this.getCoursePack().setUniversityName(StringUtils.upperCase(school)); 
    }
    
    public void setCourseNumber (String courseNumber)  { 
        this.getCoursePack().setCourseNumber(StringUtils.upperCase(courseNumber)); 
    }
    
    public void setCourseName (String courseName)  { 
        this.getCoursePack().setCourseName(StringUtils.upperCase(courseName)); 
    }
    
    public void setNumberOfStudents (long numberOfStudents)  {
        this.getCoursePack().setNumStudents(numberOfStudents); 
    }
    
    public void setNumberOfStudentsLong (Long numberOfStudents)  {
        this.getCoursePack().setNumStudents(numberOfStudents); 
    }
    
    public void setEstimatedCopies (Long estimatedCopies)  {
        //Placeholder to meet interface 
    }
 
    
    public void setInstructor (String instructor)  {
        this.getCoursePack().setInstructorName(StringUtils.upperCase(instructor)); 
    }
    
    public void setYourReference (String yourReference)  {
        this.getCoursePack().setHeaderRef(StringUtils.upperCase(yourReference)); 
    }

    public void setComments (String comments)  {
        _comments = comments; 
    }
    
    public void setAccountingReference (String accountingReference)  {
        this.getCoursePack().setAcctingRef(StringUtils.upperCase(accountingReference)); 
    }
    
    public void setOrderEnteredBy (String orderEnteredBy)  {
        this.getCoursePack().setOrderEnteredBy(orderEnteredBy); 
    }
    
    public void setStartOfTerm(Date startOfTerm) {
        this.getCoursePack().setStartOfTerm(startOfTerm); 
    }

	
}
