package com.copyright.ccc.web.contentmgmt;

import java.util.HashMap;
import java.util.Iterator;

public abstract class AbstractPageMapperBase
{
    private HashMap<String,String> _params = null;
    private SiteSection _parent=null;

    public AbstractPageMapperBase()
    {
        _params = new HashMap<String,String>();
    }

    protected SiteSection getParent()
    {
        return _parent;
    }

    public String getParam(String key)
    {
        if (_params.containsKey(key))
        {
            return _params.get(key);
        } else if (getParent() != null)
        {
            return getParent().getParam(key);
        } else
            return null;
    }

    public void putParam(String key, String value)
    {
        _params.put(key, value);
    }

    protected String paramsToXML() 
    {
        StringBuffer buf = new StringBuffer();
        Iterator<String> i=_params.keySet().iterator();
        String key, value;
        while (i.hasNext()) 
        {
            key = i.next();
            value = getParam(key);
            buf.append(key.toLowerCase());
            buf.append("=\"");
            buf.append(value);
            buf.append("\" ");
        }
        return buf.toString();
    }


    public String getBanner() { return getParam("Banner");}
    public String getLayout() { return getParam("Layout");}
    public String getRightSidebar() { return getParam("RightSidebar");}
    public String getLeftSidebar() { return getParam("LeftSidebar");}

    public void setBanner(String banner)             { putParam("Banner", banner);}
    public void setLayout(String layout)             { putParam("Layout", layout);}
    public void setRightSidebar(String rightSidebar) { putParam("RightSidebar", rightSidebar);}
    public void setLeftSidebar(String leftSidebar)   { putParam("LeftSidebar",leftSidebar);}


    public abstract String toXML(String string);

    public void setParent(SiteSection parent)
    {
        _parent=parent;
    }

    public abstract void populatePageCodeMap(HashMap<String,SitePage> pageMap);}
