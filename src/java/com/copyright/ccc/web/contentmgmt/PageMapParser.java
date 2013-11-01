package com.copyright.ccc.web.contentmgmt;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

// TODO might need to use HandlerBase or find out why DefaultHandler isn't working
public class PageMapParser extends DefaultHandler
{

	protected Logger _logger = Logger.getLogger( this.getClass() );
    List<SiteSection> rootSections;
    String tempVal;
    SiteSection tempSection;
    SitePage tempPage;

    public PageMapParser()
    {
        rootSections = new ArrayList<SiteSection>();
        tempSection = null;
    }

    public List<SiteSection> parse(File filename)
    {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try
        {
            SAXParser sp = spf.newSAXParser();
            sp.parse(filename, this);
        }
        catch (SAXException se)
        {
    		_logger.error( ExceptionUtils.getFullStackTrace(se) );
        }
        catch (ParserConfigurationException pce)
        {
    		_logger.error( ExceptionUtils.getFullStackTrace(pce) );
        }
        catch (IOException ie)
        {
    		_logger.error( ExceptionUtils.getFullStackTrace(ie) );
        }
        return rootSections;
    }

    @Override
    public void startElement(String uri, String localName, String qName, 
                             Attributes attributes) throws SAXException
    {
        tempVal = "";
        if (qName.equalsIgnoreCase("Section"))
        {
            SiteSection current=tempSection;
            tempSection = new SiteSection();
            tempSection.setParent(current);
            tempSection.setLabel(attributes.getValue("label"));
            setCommonAttributes(tempSection, attributes);
        } else if (qName.equalsIgnoreCase("Page"))
        {
            tempPage = new SitePage();
            tempPage.setParent(tempSection);
            tempPage.setPageCode(attributes.getValue("code"));
            tempPage.setContent(attributes.getValue("content"));
            setCommonAttributes(tempPage, attributes);
        }
    }
    
    @Override
    public void endElement(String uri, String localName, 
                           String qName) throws SAXException
    {
        if (qName.equalsIgnoreCase("Section"))
        {
            if (tempSection.getParent() == null)
                rootSections.add(tempSection);
            else
                tempSection.getParent().addChild(tempSection);
            tempSection = tempSection.getParent();
        } else if (qName.equalsIgnoreCase("Page"))
        {
            tempSection.addChild(tempPage);
        }
    }

    private void setCommonAttributes(AbstractPageMapperBase sectionOrPage, 
                                     Attributes attributes)
    {
        if (attributes.getValue("banner")!=null)
            sectionOrPage.setBanner(attributes.getValue("banner"));
        if (attributes.getValue("layout")!=null)
            sectionOrPage.setLayout(attributes.getValue("layout"));
        if (attributes.getValue("rightsidebar")!=null)
            sectionOrPage.setRightSidebar(attributes.getValue("rightsidebar"));
        if (attributes.getValue("leftsidebar")!=null)
            sectionOrPage.setLeftSidebar(attributes.getValue("leftsidebar"));
    }
}
