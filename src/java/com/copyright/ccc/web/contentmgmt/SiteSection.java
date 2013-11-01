package com.copyright.ccc.web.contentmgmt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class SiteSection extends AbstractPageMapperBase
{
    private String _label=null;
    private List<AbstractPageMapperBase> _children;
    public SiteSection()
    {
        _children=new ArrayList<AbstractPageMapperBase>();
    }

    public SiteSection(String label)
    {
        this();
        _label=label;
    }

    public void addChild(AbstractPageMapperBase child)
    {
        child.setParent(this);
        _children.add(child);
    }

    public String toXML() 
    {
        return toXML("");
    }

    @Override
    public String toXML(String leading) 
    {
        StringBuffer buf = new StringBuffer(leading+"<section label=\"");
        buf.append(getLabel());
        buf.append("\" ");
        buf.append(paramsToXML());
        buf.append(">\n");
        AbstractPageMapperBase child=null;
        for(Iterator<AbstractPageMapperBase> i=_children.iterator(); i.hasNext(); ) 
        {
            child=i.next();
            buf.append(child.toXML(leading+"  "));
        }
        buf.append(leading + "</section>\n");
        return buf.toString();
    }

    // Trivial Accessors
    public String getLabel() { return _label; }
    public void setLabel(String label) { _label = label; }

    @Override
    public void populatePageCodeMap(HashMap<String,SitePage> pageMap)
    {
        AbstractPageMapperBase child=null;
        for(Iterator<AbstractPageMapperBase> i=_children.iterator(); i.hasNext(); ) {
            child=i.next();
            child.populatePageCodeMap(pageMap);
        }
    }
}
