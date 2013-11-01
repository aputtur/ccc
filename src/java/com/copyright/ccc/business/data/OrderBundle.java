package com.copyright.ccc.business.data;

import java.util.Date;

public interface OrderBundle
{
      
	public long getBundleId();

    public String getYourReference(); 

    public String getAccountingReference(); 

	public String getCourseName();
    
    public String getCourseNumber();
    
    public long getNumberOfStudents();
    
    public Long getNumberOfStudentsLong();
    
    public Long getEstimatedCopies();

    public boolean isNumStudentsSame();
        
    @Deprecated
    public String getSchool();
    
    public String getOrganization();
    
    public String getInstructor(); 
        
    public String getComments();
    
    public Date getStartOfTerm(); 

    public String getStartOfTermStr();
    
    public String getOrderEnteredBy();
          
    public long getDetailCount();

    public String getBundleTitles(); 
    
    @Deprecated
    public void setSchool (String school);
    
    public void setOrganization (String school);
    
    public void setCourseNumber (String courseNumber);
    
    public void setCourseName (String courseName);
    
    public void setNumberOfStudents (long numberOfStudents);
    
    public void setEstimatedCopies (Long estimatedCopies);
    
    public void setNumberOfStudentsLong(Long numberOfStudents);
    
    public void setInstructor (String instructor); 
    
    public void setYourReference (String yourReference);

    public void setAccountingReference (String accountingReference);

    public void setComments (String comments);
               
    public void setStartOfTerm(Date startOfTerm); 
    
    public void setOrderEnteredBy(String orderEnteredBy);

}
