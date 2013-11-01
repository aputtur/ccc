package com.copyright.ccc.web.tags;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.beanutils.PropertyUtils;

import com.copyright.ccc.business.data.CCUser;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.user.User;

public class UserInfoTag extends TagSupport
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String USER_TYPE_CC = "cc";
    private static final String USER_TYPE_SHARED = "shared";
    
    private String _propertyName;
    private String _userType = USER_TYPE_CC;
    
    @Override
    public int doStartTag() throws JspException
    {
        Object userObj = null;
        
        JspWriter writer = pageContext.getOut();
        
        if ( _userType.equals( USER_TYPE_CC ) )
        {
            CCUser user = UserContextService.getActiveAppUser();
            userObj = user;
        }
        else if ( _userType.equals( USER_TYPE_SHARED ) )
        {
            User user = UserContextService.getActiveSharedUser();
            userObj = user;
        }
        
        if ( userObj != null )
        {
            String value = null;

            try
            {
                {
                    Object userProp = PropertyUtils.getProperty( userObj, _propertyName );
                    value = userProp.toString();
                }
                
                writer.write( value );
            }
            catch ( IOException e )
            {
                throw new JspException( e );
            }
            catch ( InvocationTargetException e )
            {
                throw new JspException( e );                
            }
            catch ( IllegalAccessException e )
            {
                throw new JspException( e );
            }
            catch ( NoSuchMethodException e )
            {
                throw new JspException( e );
            }
        }
        
        return TagSupport.SKIP_BODY;
    }
    
    public String getPropertyName()
    {
        return _propertyName;
    }
    
    public void setPropertyName( String propertyName )
    {
        _propertyName = propertyName;
    }
    
    public String getUserType()
    {
        return _userType;
    }
    
    public void setUserType( String userType )
    {
        _userType = userType;
    }
}
