package com.copyright.ccc.web.tags;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
public class ImportTag extends TagSupport 
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String _file;
    private String _root = null;    // optional: if null at run-time use 
    private String _server = null;  // configuration defaults
    private String _port = null;    //

    private static String defaultRoot;
    private static String defaultServer = "localhost";
    private static String defaultPort = "";
    
    private static final String HTTPS_PORT = "443";
    private static final String HTTPS_PROTOCOL = "https";
    private static final String HTTP_PROTOCOL = "http";
    
    static 
    {
        defaultRoot = CC2Configuration.getInstance().getStaticContentRoot();
    }

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
                    new Object[] { getFile() != null ? getFile() : "" } ) );
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
    
    protected String getContentFile()
    {
        return getRoot() + File.separator + getFile(); 
    }

    protected Reader getReaderForFile() throws ContentManagementException
    {
        BufferedReader br = null;
        
        if ( getServer().equals( "localhost" ) ) 
        {
            try
            {
                prepareForFileRead();
                br = new BufferedReader( new FileReader( getContentFile() ) );
            }
            catch ( IOException e )
            {
                throw new ContentNotFoundException( e );
            }
        }
        else
        {
            try
            {
                URL url = new URL( getProtocol(), getServer(),
                		getIntegerPort(), getRoot() + File.separator + getFile() );
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
        }
        
        return br;
    }

    @Override
    public int doEndTag() 
    {
        return EVAL_PAGE;
    }

    public void setFile(String file) 
    {
        _file=file;
    }
    public String getFile() 
    {
        return _file;
    }
    public void setRoot(String root) 
    {
        _root = root;
    }

    public String getRoot() 
    {
        return _root == null ?  defaultRoot : _root;
    }
    public void setServer(String server) 
    {
        _server = server;
    }
    public String getServer() 
    {
        return _server==null ? defaultServer : _server;
    }
    public void setPort(String port) 
    {
        _port=port;
    }
    public String getPort() 
    {
        return _port == null ? defaultPort : _port;
    }

    protected String getProtocol()
    {
        if ( getPort().equals( HTTPS_PORT ) )
        {
            return HTTPS_PROTOCOL;
        } 
        else
        {
            return HTTP_PROTOCOL;
        }
    }
    protected int getIntegerPort()
    {
        if ( getPort().equals("") )
        {
            return pageContext.getRequest().getServerPort();
        }
        else 
        {
            return new Integer( getPort() ).intValue();
        }
    }
}
