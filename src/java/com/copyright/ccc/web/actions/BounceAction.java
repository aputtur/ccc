package com.copyright.ccc.web.actions;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.web.CCAction;
import com.copyright.workbench.util.StringUtils2;

public class BounceAction extends CCAction
{
    static Logger _logger = Logger.getLogger( BounceAction.class );
    
    static Set<String> badPathSet = new HashSet<String>();
    
    static final String DEFAULT_BOUNCE_PATH = "/home.do";
    
    static String[] badPaths = { "/login.do", "/loginForm.do", "/loginFormRedirect.do", "/loginRetry.do", "/autosuggest.do" };
    
    static
    {
        for ( int i = 0; i < badPaths.length; i++ )
        {
            badPathSet.add( badPaths[i] );
        }
    }
    
    public ActionForward defaultOperation(ActionMapping mapping,
                                            ActionForm form,
                                            HttpServletRequest request,
                                            HttpServletResponse response )
    {

        badPathSet.add( mapping.getPath() );
        badPathSet.add( request.getServletPath() );
    

        String bouncePath = DEFAULT_BOUNCE_PATH;

        String bouncePathParam = request.getParameter( "bounceTo" );
        
        if ( ! StringUtils2.isNullOrEmpty( bouncePathParam ) )
        {
            bouncePath = bouncePathParam;
        }
        else
        {
            bouncePath = findLastPathInHistory();
            if ( StringUtils2.isNullOrEmpty( bouncePath )) {
                bouncePath = DEFAULT_BOUNCE_PATH;
            }
        }

        _logger.debug( "processing bounce from: " + bouncePath );
        
        ActionForward af = new ActionForward( bouncePath, true );
        
        return af;
    }
    
    private String findLastPathInHistory()
    {
        String bouncePath = null;

        List<String> pathHistory = UserContextService.getNavigationHistory();

        for ( Iterator<String> itr = pathHistory.iterator(); itr.hasNext(); )
        {
            String path = itr.next();
            
            int queryStringLoc = path.indexOf( '?' );
            String strippedPath = null;
            
            if ( queryStringLoc > -1 )
            {
                strippedPath = path.substring( 0, queryStringLoc );
            }
            else
            {
                strippedPath = path;
            }
            
            _logger.debug( "checking path for redirectability: " + strippedPath );
        
            if ( badPathSet.contains( strippedPath ) )
                continue;
        
            bouncePath = path;

            break;
        }
        
        return bouncePath;
    }
}
