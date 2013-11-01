package com.copyright.ccc.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.xml.serialize.OutputFormat;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.copyright.ccc.CCRuntimeException;

/**
 * XMLTools provides static methods useful for working with XML.
 *
 * 
 */
public class XMLTools
{
	static Logger _logger = Logger.getLogger(XMLTools.class );
/**
 * Searches text nodes in provided tags for ampersands, and determines whether
 * or not they should be escaped, i.e. if they are not already part of another
 * escape sequence. The file is then saved in its updated condition.
 *
 * @param xmlFileName File to be searched.
 * @param nonEscapedTags  List of tags to check for ampersands. (Literally, these
 *                        tags will not be automatically escaped in serialization.)
 * @param systemID  systemID for this source file; used for relative
 *                  URI resolution.
 * @return boolean  <code>true</code> if operation is a success, <code>false</code> otherwise.
 */
	private static final String START_TAG = "<";
	private static final String START_END_TAG = "</";
	private static final String END_TAG = ">";

	public static boolean escapeAmpersandsInFile(String xmlFileName, String systemID, String[] nonEscapedTags)
	{
		DOMDocumentMgr docMgr = new DOMDocumentMgr();
		try
		{
			docMgr.load(xmlFileName,systemID);
			DOMElementMgr eleMgr = docMgr.getElementMgr();
			for (int i = 0; i<nonEscapedTags.length ; i++)
			{
				Iterator<DOMElementMgr> children = eleMgr.getChildren(nonEscapedTags[i]).iterator();
				if (!children.hasNext())
					children = eleMgr.getChildrenRecursive(nonEscapedTags[i]).iterator();
				while (children.hasNext())
				{
					DOMElementMgr child = children.next();
					escapeAmpersandsInElement(child);
				}
			}
		}
		catch (SAXException se)
		{
			_logger.warn( ExceptionUtils.getFullStackTrace( se ) );
			return false;
		}
		catch (IOException ioe)
		{
			_logger.warn( ExceptionUtils.getFullStackTrace( ioe ) );
			return false;
		}
		
		try
		{
			OutputFormat format = new OutputFormat( docMgr.getDocument() );
			//format.setEncoding( "ISO-8859-1" );
			format.setNonEscapingElements(nonEscapedTags);
			docMgr.store(format);
		}
		catch (IOException ioe)
		{
			//source file may be in an invalid state.
			// TODO: why do we return boolean instead of propagating the exception?
			_logger.warn( ExceptionUtils.getFullStackTrace( ioe ) );
			return false;
		}
		return true;
	}
	
	/**
	 * Searches text node in provided element for ampersands, and determines whether
	 * or not they should be escaped, i.e. if they are not already part of another
	 * escape sequence.
	 *
	 * @param element  Element to be searched.
	 */
	public static void escapeAmpersandsInElement(DOMElementMgr element)
	{
		/* SS - We want to escape ampersands, but only if they are not valid.
		 * Thus we don't want to escape &amp; or &ndash; so something similar.
		 * We assume that these are distinct words. Hence escape each word, and only if it DOES NOT
		 * Start with '&' AND end with '; '
		 * 
		 * The other way is to use RegEx. which is probably preferable, but I don't know Regular expressions 
		 * well enough to achieve this. Will get to it, once i master regular expressions.
		 */
		String text = (String)element.get(DOMConstants.NODETEXT,null);
		_logger.debug("Text in = " + text);
		StringTokenizer strok = new StringTokenizer(text);
		StringBuffer retVal = new StringBuffer();
		while (strok.hasMoreTokens())
		{
			String word = strok.nextToken();
			if (word.startsWith("&"))
			{
				if (word.endsWith(";"))
				{
					//do nothing
				}
				else
				{
					if (word.equals("&"))
						word = "&amp;";
				}
			}
			retVal.append(" ");
			retVal.append(word);
		}
		text = retVal.toString(); //escapeAmpersands(text);
		_logger.debug("Text out = " + text);
		element.set(DOMConstants.NODETEXT,null,text);
	}

		
	/**
	 * Searches text for ampersands, and determines whether or not they should be
	 * escaped, i.e. if they are not already part of another escape sequence.
	 *
	 * @param text String to be searched.
	 * @return String containing corrected entities.
	 */
	/*
	public static String escapeAmpersands(String text)
	{
		return RegExpTools.conditionalReplace(text, "&", "&amp;", "&[^; \t\r\n]*[ \t\r\n]");
	} */
	
	public static void appendTextElement(StringBuffer request, String elementName, String elementValue)
	{
		startTag(request, elementName);
		request.append(elementValue);
		endTag(request, elementName);
	}
	
	public static void appendTextElement(StringBuffer request, String elementName, char[] elementValue)
	{
		startTag(request, elementName);
		request.append(elementValue);
		endTag(request, elementName);
	}
	
	public static void appendElementWithAttribute(StringBuffer request, String elementName, 
			String attrName, String attrValue)
	{
		startTagWithAttribute(request, elementName, attrName, attrValue);
		endTag(request, elementName);
	}
	
	public static void appendTextElementWithAttribute(StringBuffer request, String elementName, 
			String elementValue, String attrName, 
			String attrValue )
	{
		startTagWithAttribute(request, elementName, attrName, attrValue);
		request.append(elementValue);
		endTag(request, elementName);
	}
	
	public static void startTag(StringBuffer request, String elementName)
	{
		request.append(START_TAG);
		request.append(elementName);
		request.append(END_TAG);		
	}
	
	public static void endTag(StringBuffer request, String elementName)
	{
		request.append(START_END_TAG);
		request.append(elementName);
		request.append(END_TAG);
	}
	
	public static Document buildDocument(StringBuffer request) 
	{
		ByteArrayInputStream bais;
		try {
			bais = new ByteArrayInputStream(request.toString().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException uee) {
			throw new CCRuntimeException(uee);
		}		   	
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder builder;
		try {
			builder = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException pce) {
			throw new CCRuntimeException(pce);
		}
		Document document;
		try {
			document = builder.parse(bais);
			bais.close();
		} catch (SAXException se) {
			throw new CCRuntimeException(se);
		} catch (IOException ioe) {
			throw new CCRuntimeException(ioe);
		}
		return document;
	}
	
	public static void startTagWithAttribute(StringBuffer request, String elementName, 
			String attrName, String attrValue)
	{
		request.append(START_TAG);
		request.append(elementName);
		request.append(" ");
		request.append(attrName);
		request.append("=\"");
		request.append(attrValue);
		request.append("\"");
		request.append(END_TAG);		
	}
	
	public static String getTagValue(Element element, String tagName)
	{
    	try
    	{
    		if(element != null)
    		{
	    		NodeList list = element.getElementsByTagName(tagName);
	    		if(list != null)
	    		{
	    			Node node = list.item(0);
	    			if(node != null)
	    			{
	    				Node child = node.getFirstChild();
	    				if(child != null)
	    				{
	    					return node.getFirstChild().getNodeValue();
	    				}
	    			}
	    		}
    		}
    	}
    	catch (Exception ex) {
    		_logger.warn( ExceptionUtils.getFullStackTrace(ex));
    	    		
    	}
    	return "";
	}
	
	public static Element getElement(Element element, String tagName)
	{
    	try
    	{
    		NodeList list = element.getElementsByTagName(tagName);
        	Node node = list.item(0);
        	return (Element)node;
    	}
    	catch (Exception ex) {
    		_logger.warn( ExceptionUtils.getFullStackTrace(ex) );
    		throw new CCRuntimeException(ex);
    	}
    	//return null;
    	
		
	}	
	
	public static String encodeAmpersands(String input)
	{
		int x1 = -1;
		int x2 = 0;
		try
		{
			do
			{
				x1 = input.indexOf('&', x1+1);
				x2 = input.indexOf("&amp;", x1+1);
				if (x1 != x2)
				{
					input = input.substring(0, x1) + "&amp;" + input.substring(x1+1);
				}
			}
			while (x1 >= 0);
		}
	    catch (Exception e){
	    	throw new CCRuntimeException(e);
	    }
	    return input;
	}

}