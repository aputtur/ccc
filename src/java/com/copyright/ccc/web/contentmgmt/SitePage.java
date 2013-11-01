package com.copyright.ccc.web.contentmgmt;

import java.util.HashMap;

public class SitePage extends AbstractPageMapperBase
{
    private String _pageCode=null;

    public SitePage()
    {
    }

    public SitePage(String pageCode, String content)
    {
        _pageCode=pageCode;
        setContent(content);
    }

    @Override
    public String toXML(String leading) 
    {
        StringBuffer buf = new StringBuffer(leading+"<page code=\"");
        buf.append(getPageCode());
        buf.append("\" ");
        buf.append(paramsToXML());
        buf.append(" />\n");
        return buf.toString();
    }


    public String getContent() { return getParam("Content");}
    public void setContent(String content)           { putParam("Content", content);}

    // Trivial Accessors
    public String getPageCode() { return _pageCode;}
    public void setPageCode(String pageCode) { _pageCode = pageCode; }

    @Override
    public void populatePageCodeMap(HashMap<String,SitePage> pageMap)
    {
        pageMap.put(getPageCode(),this);
    }
}
