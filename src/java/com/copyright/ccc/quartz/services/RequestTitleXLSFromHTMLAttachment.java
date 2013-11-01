/*
 * RequestTitleXLSFromHTMLAttachment.java
 * Copyright (c) 2007, Copyright Clearance Center, Inc.
 * ----------------------------------------------------------------------------
 * Revision History
 * 2007.10.01  tmckinney		Created.
 * ----------------------------------------------------------------------------
 */
package com.copyright.ccc.quartz.services;

import com.copyright.workbench.util.StringUtils2;

/**
 * @author tmckinney
 * @version 1.0
 */
public class RequestTitleXLSFromHTMLAttachment
{
	public static final String PAGE_HEADER = "<%@ page contentType = \"text/html\"%> " +
	  "<%response.setContentType(\"application/vnd.ms-excel\");%> ";
    
    public static final String STD_HTML_HEADER =
    	"<html> <head> <title></title> </head> <body bgcolor=\"white\"> <br/> <table border=\"0\" cellpadding=\"1\" width=\"750\"> ";
    
    public static final String STD_HTML_FOOTER = "</table></body></html>";
    
    private StringBuffer content = new StringBuffer(PAGE_HEADER);
    
    public void addHTMLContent(String newContent)
    {
    	this.content.append(newContent);
    }
    
    public String getContent()
    {
    	return this.content.toString();
    }
    
    /**
     * Escapes a numeric string with a space to be
     * treated as a literal by Excel.
     */
    public static String escapeNumeric(String input)
    {
    	return StringUtils2.isNumeric(input) ? "&nbsp;" + input : input;
    }
}
