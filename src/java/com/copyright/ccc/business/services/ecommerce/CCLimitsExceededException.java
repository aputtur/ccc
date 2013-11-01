package com.copyright.ccc.business.services.ecommerce;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.copyright.ccc.web.transaction.TransactionConstants;
import com.copyright.data.pricing.LimitsExceededException;

public abstract class CCLimitsExceededException extends PurchasablePermissionException
{
	private static final long serialVersionUID = 1L;
	
  private LimitsExceededException _limitsExceededException = null;
  
  CCLimitsExceededException( PurchasablePermission purchasablePermission,
                             LimitsExceededException limitsExceededException )
  {
    super( purchasablePermission );
    
    if ( limitsExceededException != null )
    {
      _limitsExceededException = limitsExceededException;
    }
  }

  private com.copyright.data.pricing.LimitsExceededException getSharedServicesLimitsExceededException()
  {
    return _limitsExceededException;
  }
  
  public String getExceedingAttribute()
  {
    String exceedingAttribute = TransactionConstants.ATTRIBUTE_NOT_PRESENT;
    
    boolean limitsExceededExceptionAvailable = getLimitsExceededException() != null;
    
    if ( limitsExceededExceptionAvailable )
    {
      
      String underlyingErrorMessage = getLimitsExceededException().getMessage();
      
      if( matchesNumberOfCopiesPattern( underlyingErrorMessage ) )
      {
        exceedingAttribute = TransactionConstants.ATTRIBUTE_NUMBER_OF_COPIES;
      }
      
      if( matchesNumberOfPagesPattern( underlyingErrorMessage ) )
      {
        exceedingAttribute = TransactionConstants.ATTRIBUTE_NUMBER_OF_PAGES;
      }
      
      if( matchesNumberOfRecipientsPattern( underlyingErrorMessage ) )
      {
        exceedingAttribute = TransactionConstants.ATTRIBUTE_NUMBER_OF_RECIPIENTS;
      }
      
      if( matchesDurationPattern( underlyingErrorMessage ) )
      {
        exceedingAttribute = TransactionConstants.ATTRIBUTE_DURATION;
      }
      
      if( matchesCirculationPattern( underlyingErrorMessage ) )
      {
        exceedingAttribute = TransactionConstants.ATTRIBUTE_CIRCULATION;
      }
      
      if( matchesNumberOfStudentsPattern( underlyingErrorMessage ) )
      {
          exceedingAttribute = TransactionConstants.ATTRIBUTE_NUMBER_OF_STUDENTS;
      }
      
    }
    
    return exceedingAttribute;
  }
  
  public LimitsExceededException getLimitsExceededException()
  {
    return _limitsExceededException;
  }

  private boolean matchesNumberOfCopiesPattern( String errorMessage )
  {
    String numberOfCopiesPattern = "number of copies";
    
    return matches( errorMessage, numberOfCopiesPattern );
    
  }
  
  private boolean matchesNumberOfPagesPattern( String errorMessage )
  {
    String numberOfPagesPattern = "number of pages";
    
    return matches( errorMessage, numberOfPagesPattern );
    
  }
  
  private boolean matchesNumberOfRecipientsPattern( String errorMessage )
  {
    String numberOfPagesPattern = "recipients";
    
    return matches( errorMessage, numberOfPagesPattern );
    
  }
  
  private boolean matchesDurationPattern( String errorMessage )
  {
    String durationPattern = "work to be posted for (up to (30|180|365) days|an indefinite period of time)\\.";
    
    return matches( errorMessage, durationPattern );
    
  }
  
  private boolean matchesCirculationPattern( String errorMessage )
  {
    String numberOfPagesPattern = "circulation";
    
    return matches( errorMessage, numberOfPagesPattern );
    
  }
  
  private boolean matchesNumberOfStudentsPattern( String errorMessage )
  {
      String numberOfStudentsPattern = "number of students";
      
      return matches( errorMessage, numberOfStudentsPattern );
  }

  private boolean matches( String text, String textPattern ) 
  {
        
    Pattern pattern = Pattern.compile( textPattern, Pattern.CASE_INSENSITIVE );
    
    Matcher matcher = pattern.matcher( text );
    
    boolean matchFound = matcher.find();
        
    return matchFound;
  }
}
