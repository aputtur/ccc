package com.copyright.ccc.web.actions;

import com.copyright.ccc.config.CC2Configuration;
import com.copyright.ccc.web.CCAction;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.*;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONStringer;

/**
* Return JSON list of possible items to search on based on
* the string provided.
*
* @author Jessop
* @version
*/
public class AutoSuggestAction extends CCAction
{
    public ActionForward defaultOperation( ActionMapping       mapping
                                         , ActionForm          form
                                         , HttpServletRequest  request
                                         , HttpServletResponse response )
    throws IOException
    {
        //  Simple URL query to Lech's autosuggest service.  It returns
        //  JSON so just spit back what we get.
        
        int c;
        int items = 10;
        int count = 3;
        
        StringBuilder buf = new StringBuilder();
        String query = CC2Configuration.getInstance().getAutoSuggestSvcEndpoint();
        String searchTerm = request.getParameter("q");
        
        try
        {
            items = CC2Configuration.getInstance().getAutoSuggestItems();
            count = CC2Configuration.getInstance().getAutoSuggestCount();
        }
        catch(Exception e)
        {
            //  Just some sensible defaults.

            items = 10;
            count = 3;
        }
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try
        {
            URL url = new URL(buildQuery(query, searchTerm, items, count));
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            
            while ((c = in.read()) > -1)
            {
                buf.append((char) c);
            }
            in.close();
            
            out.println(buf.toString());
            out.flush();
        }
        catch(Exception e)
        {
            _logger.info("\n\nAUTO SUGGEST ERROR:", e);
            out.println("{}");
            out.flush();
        }
        return null;
    }

    private String buildQuery(String baseURL, String term, int items, int count)
    {
        StringBuilder query = new StringBuilder(baseURL);
        
        term = term.replace(' ', '+');
        
        query.append("/select/?q=\"").append(term).append("\"")
            .append("+AND+count%3A[").append(count).append("+TO+*]")
            .append("&fl=title,count").append("&wt=json")
            .append("&version=2.2").append("&start=0").append("&rows=")
            .append(items).append("&indent=on").append("&sort=count%20desc");
        
        return query.toString();
    }
}