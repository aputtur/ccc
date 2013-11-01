package com.copyright.ccc.web.tags;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.copyright.ccc.config.CC2Configuration;
import com.copyright.ccc.web.contentmgmt.ContentManagementException;
import com.copyright.ccc.web.contentmgmt.ContentNotFoundException;


/** The <tt>ImportTag</tt> allows inclusion of static content from locations
 *  outside of the root of the current web-context on the local server or via 
 *  a URL from a remote server.
 */
public class ImportURL extends TagSupport 
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String _url;
    

	@Override
    public int doStartTag() throws JspException
    {
        JspWriter out = pageContext.getOut();

        try 
        {
            Reader in = getReaderForFile();
            char[] buf = new char[1024];
            int i = 0;
            while( ( i = in.read( buf, 0, 1024 ) ) != -1 ) 
            {
                out.write( buf, 0, i );
            }
            in.close();
        } 
        catch ( ContentManagementException ex )
        {
            try
            {
                out.write( MessageFormat.format(
                	CC2Configuration.getInstance().getStaticContentNotFoundMessageFmt(),
                    new Object[] { getUrl() != null ? getUrl() : "" } ) );
            }
            catch ( IOException e )
            {
                throw new JspException( e );
            }
        }
        catch ( IOException e  )
        {
            throw new JspException( e );
        }
        
        return SKIP_BODY;
    }
    
    protected void prepareForFileRead() throws ContentManagementException
    {

    }

    protected Reader getReaderForFile() throws ContentManagementException
    {
        BufferedReader br = null;
        
            try
            {
                URL url = new URL( getUrl());
                br = new BufferedReader( new InputStreamReader( url.openStream() ) );
            }
            catch ( MalformedURLException e )
            {
                throw new ContentManagementException( e );
            }
            catch ( IOException e )
            {
                throw new ContentNotFoundException( e );
            }
        
        return br;
    }

    @Override
    public int doEndTag() 
    {
        return EVAL_PAGE;
    }
    
    public void setUrl (String url) 
    {
        _url=url;
    }

    public String getUrl() 
    {
        return _url;
    }
}
