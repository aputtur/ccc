package com.copyright.ccc.business.security;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Class used to list secured actions from web.xml when protocol is not secured i.e. http
 * @author asaxena
 *
 */
public class ListSecuredActions extends DefaultHandler
{

	private StringBuffer accumulator;
	static List<String> patterns;

	boolean timeForPatternCollection;

	private static final String RESOURCE_COLLECTION = "web-resource-collection";
	private static final String SECURED_URL_PATTERNS = "url-pattern";
	
	private static final Logger _logger = Logger.getLogger(ListSecuredActions.class); 
	
	/**
	 * Collects secured actions
	 * @param fileName 
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public static void collectSecuredActions(String fileName) throws IOException, SAXException,
			ParserConfigurationException
	{
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(false);
		factory.setNamespaceAware(false);
		SAXParser parser = factory.newSAXParser();
		parser.parse(new File(fileName), new ListSecuredActions());
		
	}

	private ListSecuredActions(){}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startDocument()
	 */
	@Override
	public void startDocument()
	{
		accumulator = new StringBuffer();
		timeForPatternCollection = false;
		reintializePatterns();
	}
	
	
	/**
	 * Intialize patterns so that it can store new patterns
	 */
	private static void reintializePatterns()
	{
		patterns = new ArrayList<String>();
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	@Override
	public void characters(char[] buffer, int start, int length)
	{
		accumulator.append(buffer, start, length);
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String namespaceURL, String localName,
			String qname, Attributes attributes)
	{
		accumulator.setLength(0);
		if (qname.equals(RESOURCE_COLLECTION))
		{
			timeForPatternCollection = Boolean.TRUE;
		}
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String namespaceURL, String localName, String qname)
	{
		if (qname.equals(RESOURCE_COLLECTION))
		{
			timeForPatternCollection = Boolean.FALSE;
		}

		if (timeForPatternCollection)
		{
			if (qname.equals(SECURED_URL_PATTERNS))
			{
				String pattern = accumulator.toString().trim();
				patterns.add(pattern);
				if(pattern.endsWith("*"))
				{
					patterns.add(pattern.replace("/*", ""));
				}
			}
			

		}
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#warning(org.xml.sax.SAXParseException)
	 */
	@Override
	public void warning(SAXParseException exception)
	{
		_logger.warn(exception.getLineNumber() + ": " + exception.getMessage());
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#error(org.xml.sax.SAXParseException)
	 */
	@Override
	public void error(SAXParseException exception)
	{
		_logger.error(exception.getLineNumber() + ": " + exception.getMessage());
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#fatalError(org.xml.sax.SAXParseException)
	 */
	@Override
	public void fatalError(SAXParseException exception) throws SAXException
	{
		_logger.error(exception.getLineNumber() + ": " + exception.getMessage());
		throw (exception);
	}
}
