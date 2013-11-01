package com.copyright.ccc.business.services.cart;

import java.io.Serializable;
import java.util.Date;

import com.copyright.data.order.CoursePack;

public abstract class CourseDetails implements Serializable
{
  private static final long serialVersionUID = 1L;

  abstract CoursePack getCoursePack();

  public abstract int getEstimatedQty();

  public abstract Long getBundleId();
  
  public abstract void setEstimatedQty( int estimatedQty );

  public abstract int getNumberOfStudents();
  
  public abstract void setNumberOfStudents( int numberOfStudents );
  
  public abstract String getSchoolCollege();
  
  public abstract void setSchoolCollege( String schoolCollege );
  
  public abstract String getCourseName();
  
  public abstract void setCourseName( String courseName );
  
  public abstract String getCourseNumber();

  public abstract void setCourseNumber( String courseNumber );
  
  public abstract String getInstructor();
  
  public abstract void setInstructor( String instructor );
  
  public abstract String getReference();  // YourReference
  
  public abstract void setReference( String reference );
  
  public abstract String getAccountingReference();
  
  public abstract void setAccountingReference( String accountingReference );
  
  public abstract String getOrderEnteredBy();
  
  public abstract void setOrderEnteredBy( String orderEnteredBy );
  
  public abstract Date getStartOfTermDate();
  
  public abstract void setStartOfTermDate( Date startOfTermDate );
  
}
