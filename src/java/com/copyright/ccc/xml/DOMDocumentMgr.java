package com.copyright.ccc.xml;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;

import org.apache.xerces.dom.DocumentImpl;
import org.apache.xerces.parsers.DOMParser;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.copyright.ccc.CCRuntimeException;



/**
 * DOMDocumentMgr wraps a DOM-based <code>Document</code> object and provides
 * a mechanism to load, edit, and store these documents.
 *
 * 
 */
public class DOMDocumentMgr
{
    private DOMParser _parser = new DOMParser();
    private Document _document;              //internal representation of doc
    private String _filePath = null;         //file name of doc, if loaded.

    /**
     * Simple constructor.
     */
	public DOMDocumentMgr()
	{
	}

    /**
     * Resets the internal document data to its initial <code>null</code> state.
     */
    public void reset()
    {
        _document = null;
        _filePath = null;
    }

    /**
     * Loads the internal document data from a specified file.
     *
     * @param inputFileName  the name of the file to load.  This file is
     *                       expected to be in a DOM-parsable format, or
     *                       a <code>SAXException</code> will be thrown.
     * @param systemID    systemID for this source file; used for relative
     *                    URI resolution.
     * @throws FileNotFoundException
     * @throws IOException
     * @throws SAXException
     */
    public void load(String inputFileName,String systemID)
        throws FileNotFoundException, IOException, SAXException,CCRuntimeException
    {
        FileReader reader = new FileReader( inputFileName );
        InputSource source = new InputSource( reader );
        if (systemID != null)
          source.setSystemId(systemID);
        _parser.parse(source);
        _document = _parser.getDocument();
    	_filePath = inputFileName;
    }

    /**
     * Loads the internal document data from a specified file.
     *
     * @param xmlString  the name of the string to load.  This string is
     *                       expected to be in a DOM-parsable format, or
     *                       a <code>SAXException</code> will be thrown.
     * @throws FileNotFoundException
     * @throws IOException
     * @throws SAXException
     */
    public void loadXMLString(String xmlString)
        throws IOException, SAXException, CCRuntimeException
    {
        StringReader _stringReader = new StringReader(xmlString);
        InputSource _inputSource = new InputSource(_stringReader);
        _parser.parse(_inputSource);
        _document = _parser.getDocument();
    }

    /**
     * Loads the internal document data from a specified file.  Same as
     * calling load(inputFileName,null).
     *
     *
     * @param inputFileName
     * @throws FileNotFoundException
     * @throws IOException
     * @throws SAXException
     */
    public void load(String inputFileName)
        throws FileNotFoundException, IOException, SAXException, CCRuntimeException
    {
      load(inputFileName,null);
    }

    /**
     * Returns a <code>DOMElementMgr</code> based on the root element
     * of the internal document.
     * @return a DOMElementMgr.
     */
    public DOMElementMgr getElementMgr()
    {
        if (_document != null)
            return new DOMElementMgr(_document,_document.getDocumentElement());
        else
            return new DOMElementMgr();
    }


    /**
     * Sets the internal document data to the provided <code>Document</code>.
     *
     * @param document  the <code>Document</code> to use.
     */
    public void setDocument(Document document)
    {
        _document = document;
    }

    /**
     * Gets the internal document data.
     *
     * @return the <code>Document</code> object representing the internal document.
     */
    public Document getDocument()
    {
        return _document;
    }

    /**
     * Creates a root for the internal document.
     *
     * @param rootTagName  the name of the document root tag.
     */
    public void createDocumentRoot(String rootTagName)
    {
        if (_document == null)
            setDocument(new DocumentImpl( null ));
        Element root = _document.createElement( rootTagName );
        _document.appendChild( root );
    }

    
    /**
     * Creates a root for the internal document, with a document type.
     *
     * @param rootTagName  the name of the document root tag.
     */
    public void createDocumentRoot(String rootTagName, String qualifiedName, String publicID, String systemID)
    {
    	
        if (_document == null)
        {
        	DocumentImpl _cdi = new DocumentImpl();
            Element root = _cdi.createElement( qualifiedName );
            _cdi.appendChild( root );
            DocumentType _dt = _cdi.createDocumentType(qualifiedName, publicID, systemID);

            DocumentImpl _di = new DocumentImpl(_dt);
            setDocument(_di);
        }
        Element root = _document.createElement( rootTagName );
        _document.appendChild( root );
        
    }
    
    /**
     * Stores the internal document data to the originally loaded file,
     * if the data was loaded.  Uses the default format.
     *
     * @throws IOException
     */
    public void store() throws IOException, CCRuntimeException
    {
        if (_filePath != null)
        {
            store(_filePath);
        }
    }

    /**
     * Stores the internal document data to the originally loaded file,
     * if the data was loaded.  Uses the provided format.
     *
     * @param outFormat  the format to apply to the saved document.
     * @throws IOException
     */
    public void store(OutputFormat outFormat) throws CCRuntimeException,IOException
    {
        if (_filePath != null)
        {
            store(_filePath,outFormat);
        }
    }

    /**
     * Stores the internal document data to a file of the provided name,
     * using the default format.
     *
     * @param outputFileName  the file path to store XML data to.
     * @throws IOException
     */
    public void store(String outputFileName) throws CCRuntimeException,IOException
    {
        if (_document != null)
        {
            FileOutputStream fos = new FileOutputStream(outputFileName);
            try
            {
            	store(fos);
            }
            finally
            {
            	fos.close();
            }
        }
    }


    /**
     * Stores the internal document data to a file of the provided name,
     * using the provided format.
     *
     * @param outputFileName  the file path to store XML data to.
     * @param outFormat  the format to apply to the saved document.
     * @throws IOException
     */
    public void store(String outputFileName,OutputFormat outFormat) throws IOException, CCRuntimeException
    {
        if (_document != null)
        {
            FileOutputStream fos = new FileOutputStream(outputFileName);
            try
            {
            	store(fos,outFormat);
            }
            finally
            {
            	fos.close();
            }
        }
    }


    /**
     * Stores the internal document data to the provided output stream,
     * using the default format.
     *
     * @param outStream  the <code>OutputStream</code> to store XML data to.
     * @throws IOException
     */
    private void store(OutputStream outStream) throws IOException, CCRuntimeException
    {
        if (_document != null)
        {
        	OutputFormat outFormat = new OutputFormat(_document);
    		store(outStream,outFormat);
    	}
    }

    /**
     * Stores the internal document data to the provided output stream,
     * using the provided format.
     *
     * @param outStream  the <code>OutputStream</code> to store XML data to.
     * @param outFormat  the format to apply to the saved document.
     * @throws IOException
     */
    private void store(OutputStream outStream,OutputFormat outFormat) throws IOException, CCRuntimeException
    {
        if (_document != null)
        {
    		XMLSerializer serializer = new XMLSerializer(outStream,outFormat);
        	serializer.serialize(_document);
    	}
    }


    /**
     * Modified from the original XmlGenerator.getXml().
     * Returns all XML in the internal document; serialized.
     * Uses the default format.
     *
     * @throws IOException
     * @return XML, in serialized form.
     */
    public String getSerializedXML() throws IOException, CCRuntimeException
    {
    	OutputFormat format = new OutputFormat( _document );
        return getSerializedXML(format);
    }


    /**
     * Modified from the original XmlGenerator.getXml().
     * Returns all XML in the internal document; serialized.
     * Uses the provided format.
     *
     * @param outFormat  the format to apply to the saved document.
     * @throws IOException
     * @return XML, in serialized form.
     */
    public String getSerializedXML(OutputFormat outFormat) throws IOException, CCRuntimeException
    {
        String xml = null;
        StringWriter stringOut = new StringWriter();
        XMLSerializer serial = new XMLSerializer( stringOut, outFormat );
        serial.asDOMSerializer();
        serial.serialize( _document );
        xml = stringOut.toString();
        return xml;
    }



}