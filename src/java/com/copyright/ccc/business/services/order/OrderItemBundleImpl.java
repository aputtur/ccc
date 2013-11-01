package com.copyright.ccc.business.services.order;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.copyright.ccc.business.data.OrderBundle;
import com.copyright.ccc.business.services.BundleParmEnum;
import com.copyright.ccc.business.services.ItemHelperServices;
import com.copyright.svc.order.api.data.Bundle;

public class OrderItemBundleImpl implements OrderBundle {

	     Bundle _bundle;
   
	    public void setBundle (Bundle bundle) {
	       _bundle = bundle;
	    }
	    
	    public Bundle getBundle() {
	        if (_bundle == null) {
	           _bundle = new Bundle();
	        }
	        return _bundle;
	    }

	    public long getBundleId() { 
	    	
	    	Long bundleId = this.getBundle().getBundleId();
	    	
			if (bundleId != null) {
				  return bundleId.longValue();
			} else {
		  		return 0;
		  	}

	    }
	    
	    
	    
/*	    public CoursePack getCoursePack() {
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
*/
	    
	    public long getDetailCount() {
	    	return this.getBundle().getItemCount();
	    }    


	    public String getBundleTitles() {
	    	
	    	StringBuffer bundleTitles = new StringBuffer();
	    	boolean firstTitle = true;
	    	if (getBundle().getTitles() != null) {
	    		for (String bundleTitle : getBundle().getTitles()) {
	    			if (firstTitle == true) {
	    				bundleTitles.append(bundleTitle);
	    				firstTitle = false;
	    			} else {
	    				bundleTitles.append(", " + bundleTitle);
	    				break;
	    			}
	    		}
	    	}
	    	if (bundleTitles.length() > 0) {
	            return bundleTitles.toString();
	    	}
	    	return null;
	    }
	    
	    public String getCourseName() { 
	    
	        return this.getBundle().getBundleName();
//	       String name = "";
//	       CoursePack pack = _purchase.getSingleCoursePack();
//	       if (pack != null) {
//	         name = pack.getCourseName();
//	       }
//	       return name; 
	    }
    
	    public String getCourseNumber() 
	    { 
			return this.getBundle().getRefNumber();
	    }

	    public long getNumberOfStudents() 
	    {
	    	BigDecimal numberOfStudents = ItemHelperServices.getBundleParmNumber(getBundle(), BundleParmEnum.NUM_STUDENTS.name());
	    	
	    	if (numberOfStudents != null) {
	    		return numberOfStudents.longValue();
	    	}
	    	return 0;

/*	    	
	    	if (this.getBundle().getCoursePack().getNumStudents() == null) {
	            return 0;
	        } else {
	            return this.getCoursePack().getNumStudents().longValue();
	        }
*/
	    }
	    
	    public void setNumberOfStudents (long numberOfStudents)  {
	        this.setNumberOfStudentsLong(new Long(numberOfStudents));    
	    }

	    public void setNumberOfStudentsLong (Long numberOfStudents)  {
	    	BigDecimal numberOfStudentsBigDecimal = null;
	    	
	    	if (numberOfStudents != null) {
	    		numberOfStudentsBigDecimal = new BigDecimal( numberOfStudents.longValue());
	    	} else {
	    		return;
	    	}
	    	
	    	ItemHelperServices.setBundleParmNumber(getBundle(), BundleParmEnum.NUM_STUDENTS.name(), numberOfStudentsBigDecimal);
	    	
//	    	if (getEstimatedCopies() == null) {
//	    		setEstimatedCopies(numberOfStudents);
//	    	}
	    }
	    
	    public Long getNumberOfStudentsLong() {
	    	BigDecimal numberOfStudents = ItemHelperServices.getBundleParmNumber(getBundle(), BundleParmEnum.NUM_STUDENTS.name());
	    	
	    	if (numberOfStudents != null) {
	    		return Long.valueOf(numberOfStudents.longValue());
	    	} else {
	    		return null;
	    	}
	    	
	    }
	    
	    public Long getEstimatedCopies() {
	    	BigDecimal estimatedCopies = ItemHelperServices.getBundleParmNumber(getBundle(), BundleParmEnum.ESTIMATED_COPIES.name());
	    	
	    	if (estimatedCopies != null) {
	    		return Long.valueOf(estimatedCopies.longValue());
	    	} else {
	    		return null;
	    	}
	    }
	    
	    public void setEstimatedCopies (Long estimatedCopies) {
	    	BigDecimal estimatedCopiesBigDecimal = null;
	    	
	    	if (estimatedCopies != null) {
	    		estimatedCopiesBigDecimal = new BigDecimal( estimatedCopies.longValue());
	    	}
	    	ItemHelperServices.setBundleParmNumber(getBundle(), BundleParmEnum.ESTIMATED_COPIES.name(), estimatedCopiesBigDecimal);
	    }
	    

//	    public String getNumStudSameFlg() {        
//	        return null;
//	    }

	    // The UI should not be using this now.  It was a convenience field in old 
	    // shared services, but it should now be calculated based on interrogating items in the order
	    @Deprecated
	    public boolean isNumStudentsSame() {
	        return true;
	    }
	    
	    public String getSchool() 
	    { 
	        return this.getBundle().getOrganization();
	    }

	    public String getOrganization() 
	    { 
	        return this.getBundle().getOrganization();
	    }

	    public String getOrderEnteredBy() {
	    	return this.getBundle().getContactName();
	    }
	    
	    public String getInstructor() 
	    { 
	        return ItemHelperServices.getBundleParmString(getBundle(), BundleParmEnum.INSTRUCTOR.name());     
	    }
	       
	    public String getYourReference() { 
	        return ItemHelperServices.getBundleParmString(getBundle(), BundleParmEnum.LCN_BUNDLE_REFERENCE.name());     
	    }

	    public String getAccountingReference() { 
	        return ItemHelperServices.getBundleParmString(getBundle(), BundleParmEnum.LCN_BUNDLE_ACCOUNTING_REFERENCE.name());     
	    }

	    
	    public String getComments() { 
	        return this.getBundle().getComments(); 
	    }
	    
	    public Date getStartOfTerm() 
	    {      
	        return this.getBundle().getStartOfUseDtm(); 
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
	            	    
	    public void setSchool (String school)  { 
	        this.getBundle().setOrganization(school);
	    }

	    public void setOrganization (String school)  { 
	        this.getBundle().setOrganization(school);
	    }
	    
	    public void setCourseNumber (String courseNumber)  { 
	        this.getBundle().setRefNumber(StringUtils.upperCase(courseNumber));
	    }
	    
	    public void setCourseName (String courseName)  { 
	    	this.getBundle().setBundleName(StringUtils.upperCase(courseName));
	    }
	    	 
	    public void setOrderEnteredBy(String contactName) {
	    	this.getBundle().setContactName(contactName);
	    }

	    
	    public void setInstructor (String instructor)  {
	    	ItemHelperServices.setBundleParmString(getBundle(), BundleParmEnum.INSTRUCTOR.name(), instructor);
	    }
	    
	    public void setYourReference (String yourReference)  {
	    	ItemHelperServices.setBundleParmString(getBundle(), BundleParmEnum.LCN_BUNDLE_REFERENCE.name(), yourReference);
	    	//	    	this.getBundle().setRefNumber(yourReference);
//	    	this.getCoursePack().setHeaderRef(StringUtils.upperCase(yourReference)); 
	    }

	    public void setAccountingReference (String accountingReference)  {
	    	ItemHelperServices.setBundleParmString(getBundle(), BundleParmEnum.LCN_BUNDLE_ACCOUNTING_REFERENCE.name(), accountingReference);
	    	//	    	this.getBundle().setRefNumber(yourReference);
//	    	this.getCoursePack().setHeaderRef(StringUtils.upperCase(yourReference)); 
	    }

	    
	    public void setComments (String comments)  {
	    	this.getBundle().setComments(comments);
	    }
	        
//	    public void setOrderEnteredBy (String orderEnteredBy)  {
//	        this.getCoursePack().setOrderEnteredBy(orderEnteredBy); 
//	    }
	    
	    public void setStartOfTerm(Date startOfTerm) {
	        this.getBundle().setStartOfUseDtm(startOfTerm); 
	    }

	
}

