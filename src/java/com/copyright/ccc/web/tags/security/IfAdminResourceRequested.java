package com.copyright.ccc.web.tags.security;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.tags.ConditionTestTag;

public class IfAdminResourceRequested extends ConditionTestTag
{

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private static final String ADMIN_RESOURCE_PREFIX = "/admin/";
  private static final String ADJUSTMENT_RESOURCE_PREFIX = "/adjustment/";

  @Override
  public boolean testCondition()
  {
    HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
    
    @SuppressWarnings("unchecked")
    List<String> requestURISequence = (List<String>) request.getAttribute( WebConstants.RequestKeys.REQUEST_URI_SEQUENCE );
    
    boolean isAdminResourceRequested = false;

    boolean requestURISequencePresent = requestURISequence != null;
    
    if ( requestURISequencePresent )
    {
      String firstURIInSequence = requestURISequence.get( 0 );
      
      if ( StringUtils.isNotEmpty( firstURIInSequence ) )
      {
        String adminResourceURIPattern = request.getContextPath() + ADMIN_RESOURCE_PREFIX;
        String adjustmentResourceURIPattern = request.getContextPath() + ADJUSTMENT_RESOURCE_PREFIX;
        
        isAdminResourceRequested = firstURIInSequence.indexOf( adminResourceURIPattern ) == 0 ||
                                   firstURIInSequence.indexOf( adjustmentResourceURIPattern ) == 0;
      }
      
    }
    
    return isAdminResourceRequested;
  }
}
