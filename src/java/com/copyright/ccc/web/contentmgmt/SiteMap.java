package com.copyright.ccc.web.contentmgmt;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.copyright.ccc.config.CC2Configuration;

public class SiteMap
{
    private static SiteMap _instance = new SiteMap();

    private HashMap<String,SitePage> _siteMap;
    private List<SiteSection> _siteRoots;
    private long _lastLoaded;

    private SiteMap()
    {
        _siteMap = null;
        _siteRoots = null;
        _lastLoaded = -1;
    }


    public static SiteMap getInstance()
    {
        return _instance;
    }

    public SitePage getPage(String pageCode) 
    {
        loadPageMapIfStale();
        return _siteMap.get(pageCode);
    }

    private String getPageMapFilename() 
    {
        // TODO replace with call to configuration
        return CC2Configuration.getInstance().getStaticContentRoot()
        	+ File.separator + "page_map.xml";
    }

    private void loadPageMapIfStale()
    {
        //TODO Should this method be synchronized?
        long fileModified = 
            new File(getPageMapFilename()).lastModified();
        if (_siteMap==null || (fileModified > _lastLoaded))
        {
            _lastLoaded = fileModified;
            PageMapParser parser = new PageMapParser();
            _siteRoots = parser.parse(new File(getPageMapFilename()));
            buildHashMap();
        }
    }

    private void buildHashMap()
    {
        HashMap<String,SitePage> temp = new HashMap<String,SitePage>();
        SiteSection aSection = null;
        for (Iterator<SiteSection> i = _siteRoots.iterator(); i.hasNext(); )
        {
            aSection = i.next();
            aSection.populatePageCodeMap(temp);
        }
        _siteMap = temp;

    }

    public void writePageCodeFile() throws IOException
    {
        FileWriter out = new FileWriter(getPageMapFilename());
        out.write(getXML(_siteRoots));
        out.close();
    }

    private String getXML(List<SiteSection> roots)
    {
        StringBuffer buf = new StringBuffer();
        buf.append("<page_map>\n");
        SiteSection aSection = null;
        for (Iterator<SiteSection> i = roots.iterator(); i.hasNext(); )
        {
            aSection = i.next();
            buf.append(aSection.toXML("  "));
        }
        buf.append("</page_map>\n");
        return buf.toString();
    }


}
