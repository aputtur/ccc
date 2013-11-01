package com.copyright.ccc.xml;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.copyright.ccc.CCRuntimeException;

/**
 * DOMElementMgr wraps a DOM-based <code>Element</code> object and provides a mechanism to create and edit these elements.
 * 
 * 
 */
public class DOMElementMgr
{

	private Element _element;
	private Document _ownerDocument;
	static Logger _logger = Logger.getLogger( DOMElementMgr.class );

	/**
	 * Simple constructor.
	 */
	public DOMElementMgr()
	{
	}

	public DOMElementMgr(Document document, Element element)
	{
		_ownerDocument = document;
		_element = element;
	}

	public Element getElement()
	{
		return _element;
	}

	public String getName()
	{
		return _element.getTagName();
	}

	public Object get(int constant, String name)
	{
		switch (constant)
		{
			case DOMConstants.ATTR:
				return _element.getAttribute(name);
			case DOMConstants.CHILD:
				return null; // todo
			case DOMConstants.NODETEXT:
				if (_element.getFirstChild() == null)
					return null;
				else
					return _element.getFirstChild().getNodeValue().trim();
			default:
				return null;
		}
	}

	public void set(int constant, String name, String value)
	{
		switch (constant)
		{
			case DOMConstants.ATTR:
				_element.setAttribute(name, value);
				break;
			case DOMConstants.CHILD:
				setChild(name, value, DOMConstants.APPEND);
				break;
			case DOMConstants.NODETEXT:
				Node oldNode = _element.getFirstChild();
				_logger.debug("Old Node = " + oldNode.getNodeName());
				Node newNode = _ownerDocument.createTextNode(value);
				if (oldNode.getNodeType() != Node.TEXT_NODE)
					_element.appendChild(newNode);
				else
					_logger.debug("Replacing old text node");
					_element.replaceChild(newNode, oldNode);
				break;
			default:

		}
	}

	public DOMElementMgr setChild(String name, String value, boolean state)
	{
		Element newChild = null;
		if (state == DOMConstants.APPEND)
		{
			newChild = _ownerDocument.createElement(name);

			if (value != null)
			{
				newChild.appendChild(_ownerDocument.createTextNode(value));
			}
			_element.appendChild(newChild);
		}
		if (state == DOMConstants.REPLACE)
		{
			// todo
		}
		return new DOMElementMgr(_ownerDocument, newChild);
	}

	public DOMElementMgr setChild(String name, String value, boolean state, List<String> attributeNames, List<String> attributeValues)
	{
		Element newChild = null;
		if (state == DOMConstants.APPEND)
		{
			newChild = _ownerDocument.createElement(name);

			if (value != null)
			{
				newChild.appendChild(_ownerDocument.createTextNode(value));
			}
			_element.appendChild(newChild);
		}
		if (state == DOMConstants.REPLACE)
		{
			// todo
		}
		int len = attributeNames.size();
		for (int i = 0; i < len; i++)
		{
			String attributeName = attributeNames.get(i);
			String attributeValue = attributeValues.get(i);
			newChild.setAttribute(attributeName, attributeValue);
		}
		return new DOMElementMgr(_ownerDocument, newChild);
	}

	public DOMElementMgr setChild(String name, String value, boolean state, String attributeName, String attributeValue)
	{
		Element newChild = null;
		if (state == DOMConstants.APPEND)
		{
			newChild = _ownerDocument.createElement(name);
			
			if (newChild == null)
				return null;
										
			if (value != null)
			{
				newChild.appendChild(_ownerDocument.createTextNode(value));
			}
			_element.appendChild(newChild);
		}
		if (state == DOMConstants.REPLACE)
		{
			// todo
		}
		if (newChild == null){
			return null;
		}else{
			newChild.setAttribute(attributeName, attributeValue);
			return new DOMElementMgr(_ownerDocument, newChild);
		}
	}

	public DOMElementMgr getChild(String name, int index)
	{
		Node childNode = null;
		NodeList list = _element.getChildNodes();

		int count = -1;
		Node node;
		for (int i = 0; i < list.getLength() && count < index; i++)
		{
			if ((node = list.item(i)).getNodeName().equals(name))
			{
				count++;
				if (count == index)
					childNode = node;
			}
		}

		if (childNode == null)
			return null;
		else
			return new DOMElementMgr(_ownerDocument, (Element) childNode);
	}

	public DOMElementMgr getLastChild()
	{
		Node _last = _ownerDocument.getLastChild();
		if (_last == null)
			return null;
		else
			return new DOMElementMgr(_ownerDocument, (Element) _last);
	}

	public DOMElementMgr getChild(String name)
	{
		return getChild(name, 0);
	}

	public DOMElementMgr getChildByAttr(String childName, String attrName, String attrValue)
	{
		ArrayList<DOMElementMgr> list = getChildren(childName);
		for (int i = 0; i < list.size(); i++)
		{
			DOMElementMgr mgr = list.get(i);
			if (((String) mgr.get(DOMConstants.ATTR, attrName)).equals(attrValue))
				return mgr;
		}
		return null;
	}

	public ArrayList<DOMElementMgr> getChildren()
	{
		ArrayList<DOMElementMgr> returnList = new ArrayList<DOMElementMgr>();
		NodeList list = _element.getChildNodes();
		
		Node childNode;
		for (int i = 0; i < list.getLength(); i++)
		{
			childNode = list.item(i);
			if (childNode.getNodeType() == Node.ELEMENT_NODE)
				returnList.add(new DOMElementMgr(_ownerDocument, (Element) childNode));
		}
		return returnList;
	}

	public ArrayList<DOMElementMgr> getChildren(String name)
	{
		ArrayList<DOMElementMgr> returnList = new ArrayList<DOMElementMgr>();
		NodeList list = _element.getChildNodes();

		Node childNode;
		for (int i = 0; i < list.getLength(); i++)
		{
			childNode = list.item(i);
			if (childNode.getNodeName().equals(name))
				returnList.add(new DOMElementMgr(_ownerDocument, (Element) childNode));
		}
		return returnList;
	}

	public ArrayList<DOMElementMgr> getChildrenRecursive(String name)
	{
		ArrayList<DOMElementMgr> returnList = new ArrayList<DOMElementMgr>();
		NodeList list = _element.getChildNodes();

		Node childNode;
		for (int i = 0; i < list.getLength(); i++)
		{
			childNode = list.item(i);
			if (childNode.getNodeName().equals(name))
				returnList.add(new DOMElementMgr(_ownerDocument, (Element) childNode));
		}

		int j = 0;
		while (returnList.size() == 0 && j < list.getLength())
		{
			childNode = list.item(j++);
			if (childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				DOMElementMgr childEle = new DOMElementMgr(_ownerDocument, (Element) childNode);
				returnList = childEle.getChildrenRecursive(name);
			}
		}
		return returnList;
	}

	// test code
	public static void main(String[] args)
	{
		try
		{
			DOMDocumentMgr docMgr = new DOMDocumentMgr();
			docMgr.load(args[0]);
			DOMElementMgr eleMgr = docMgr.getElementMgr();
			DOMElementMgr child = eleMgr.getChild(args[1], 0);
			String nodetext = (String) child.get(DOMConstants.NODETEXT, null);
			_logger.debug(nodetext); 
			
		}
		catch (Exception e)
		{
			throw new CCRuntimeException( e );
		}

	}

}