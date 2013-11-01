package com.copyright.ccc.business.services.cart;


import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.copyright.ccc.business.services.BundleParmEnum;
import com.copyright.ccc.business.services.ItemHelperServices;
import com.copyright.data.order.CoursePack;
import com.copyright.svc.order.api.data.Bundle;

public class CourseDetailsImpl extends CourseDetails
{
    private static final long serialVersionUID = 1L;
   
//  private CoursePack _coursePack;
	private Bundle _bundle;
	
  CourseDetailsImpl(){
    _bundle = new Bundle();
    this.setEstimatedQty(0);
  }
  
  CourseDetailsImpl( Set<Bundle> bundles ){

	  if (bundles == null || bundles.size() == 0 ) {
		  _bundle = new Bundle();
	  } else if (bundles.size() == 1){
		  Iterator<Bundle> bundlesIterator = bundles.iterator();
		  if (bundlesIterator.hasNext()) {
			  _bundle = bundlesIterator.next(); 
		  }
	  }	else {
		  throw new IllegalArgumentException("Cannot construct CourseDetails from more than one bundle");
	  }
  }
  
  CourseDetailsImpl( Bundle bundle ){
	  
	     if( bundle == null )
	     {
	       throw new IllegalArgumentException("Bundle cannot be null");
	     }
	    
	    _bundle = bundle;
	  }

  
  @Deprecated
  @Override
  CoursePack getCoursePack()
  {
    return null;
  }

  public Bundle getBundle() {
	  return _bundle;
  }
  
  public void setBundle (Bundle bundle) {
	  _bundle = bundle;
  }
  
  @Override
  public Long getBundleId() {
	  return _bundle.getBundleId();
  }
  
  @Override
  public int getNumberOfStudents()
  {

	  int numberOfStudents = ECommerceConstants.INVALID_NUMBER_OF_STUDENTS;

	  BigDecimal numStudents = ItemHelperServices.getBundleParmNumber(getBundle(), BundleParmEnum.NUM_STUDENTS.name());
	  
	  if( numStudents != null )
	  {
	    numberOfStudents = numStudents.intValue();
	  }
	  
	  return numberOfStudents;
  }
  
  @Override
  public void setNumberOfStudents(int numberOfStudents)
  {
    this.setNumberOfStudentsLong(new Long(numberOfStudents));    
  }

  public void setNumberOfStudentsLong(Long numberOfStudents)
  {
	if (numberOfStudents != null) {
		ItemHelperServices.setBundleParmNumber(getBundle(), BundleParmEnum.NUM_STUDENTS.name(), new BigDecimal (numberOfStudents.longValue()));		
	} else {
		return;
	}

//    BigDecimal estimatedQty = ItemHelperServices.getBundleParmNumber(getBundle(), BundleParmEnum.ESTIMATED_COPIES.name());
//    if (estimatedQty == null) {
//   	ItemHelperServices.setBundleParmNumber(getBundle(), BundleParmEnum.ESTIMATED_COPIES.name(), new BigDecimal (numberOfStudents.longValue()));    	
//    }

  }

  @Override
  public int getEstimatedQty()
  {

    int estimatedQtyInt = ECommerceConstants.INVALID_NUMBER_OF_STUDENTS;

    BigDecimal estimatedQty = ItemHelperServices.getBundleParmNumber(getBundle(), BundleParmEnum.ESTIMATED_COPIES.name());
  
    if( estimatedQty != null )
    {
    	estimatedQtyInt = estimatedQty.intValue();
    }
  
    return estimatedQtyInt;
  }


  @Override
  public void setEstimatedQty(int estimatedQty)
  {
	ItemHelperServices.setBundleParmNumber(getBundle(), BundleParmEnum.ESTIMATED_COPIES.name(), new BigDecimal (estimatedQty));
  }

  public void setEstimatedQtyLong(Long estimatedQty)
  {
	if (estimatedQty != null) {
		ItemHelperServices.setBundleParmNumber(getBundle(), BundleParmEnum.ESTIMATED_COPIES.name(), new BigDecimal (estimatedQty.longValue()));		
	}
  }

  @Override
  public String getSchoolCollege()
  {
    return _bundle.getOrganization();
  }

  @Override
  public void setSchoolCollege(String schoolCollege)
  {
    _bundle.setOrganization( StringUtils.upperCase(schoolCollege) );
  }

  @Override
  public String getCourseName()
  {
    return _bundle.getBundleName();
  }

  @Override
  public void setCourseName(String courseName)
  {
    _bundle.setBundleName( StringUtils.upperCase(courseName) );
  }

  @Override
  public String getCourseNumber()
  {
	  return getBundle().getRefNumber();
  }

  @Override
  public void setCourseNumber(String courseNumber)
  {
    _bundle.setRefNumber( StringUtils.upperCase(courseNumber) );
  }

  @Override
  public String getInstructor()
  {
	  return ItemHelperServices.getBundleParmString(getBundle(), BundleParmEnum.INSTRUCTOR.name());     
  }

  @Override
  public void setInstructor(String instructor)
  {
  	ItemHelperServices.setBundleParmString(getBundle(), BundleParmEnum.INSTRUCTOR.name(), instructor);
  }

  @Override
  public String getReference()
  {
//    return _bundle.getRefNumber();
    return ItemHelperServices.getBundleParmString(getBundle(), BundleParmEnum.LCN_BUNDLE_REFERENCE.name());     

  }

  @Override
  public void setReference(String reference)
  {
  	ItemHelperServices.setBundleParmString(getBundle(), BundleParmEnum.LCN_BUNDLE_REFERENCE.name(), reference);
  }

  @Override
  public String getAccountingReference()
  {
	  return ItemHelperServices.getBundleParmString(getBundle(), BundleParmEnum.LCN_BUNDLE_ACCOUNTING_REFERENCE.name());     
//	  _bundle.return _coursePack.getAcctingRef();
  }

  @Override
  public void setAccountingReference(String accountingReference)
  {
	  ItemHelperServices.setBundleParmString(getBundle(), BundleParmEnum.LCN_BUNDLE_ACCOUNTING_REFERENCE.name(), accountingReference);
//    _coursePack.setAcctingRef( StringUtils.upperCase(accountingReference) );
  }

  @Override
  public String getOrderEnteredBy()
  {
     return _bundle.getContactName();
  }

  @Override
  public void setOrderEnteredBy(String orderEnteredBy)
  {
    _bundle.setContactName( orderEnteredBy );
  }

  @Override
  public Date getStartOfTermDate()
  {
    return _bundle.getStartOfUseDtm();
  }

  @Override
  public void setStartOfTermDate(Date startOfTermDate)
  {
    _bundle.setStartOfUseDtm( startOfTermDate );
  }
}
