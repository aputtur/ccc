/*
 * RequestTitleMessage.java
 * Copyright (c) 2011, Copyright Clearance Center, Inc. All rights reserved.
 * ----------------------------------------------------------------------------
 * Revision History
 * 2011.07.18	tmckinney	Integrated into CC3 from Siva's work.
 * ----------------------------------------------------------------------------
 */
package com.copyright.ccc.quartz.services;

import com.copyright.base.config.AppServerConfiguration;
import com.copyright.ccc.config.CC2Configuration;
import com.copyright.mail.MailMessageWithAttachments;

/**
 * @author svenkatanarayana
 *
 */
public class RequestTitleMessage extends MailMessageWithAttachments
{
	

	private static final long serialVersionUID = 1L;
	private static CC2Configuration config = CC2Configuration.getInstance();
	
	private static String SUBJECT =
		AppServerConfiguration.stamp( config.getReqtitleEmailSubject() );
	private static String TO = config.getReqtitleEmailTo();
	private static String FROM = config.getReqtitleEmailFrom();
	private static String BODY = config.getReqtitleEmailBody();
	
	public RequestTitleMessage()
	{
		this.setSubject(SUBJECT);
		this.setRecipient(TO);
		this.setFromEmail(FROM);
		this.setBody(BODY);
	}
	
	//Specifies HTML header columns for report.
	public static final String HEADER = 
		"<tr><td colspan=\"15\" align=\"center\"><b>Daily Title Request Report</b></td></tr>"
		+ "<tr>"
		+ "<td align=\"left\"><b>Product<b></td>"
		+ "<td align=\"left\"><b>Title<b></td>"
		+ "<td align=\"left\"><b>Publisher<b></td>"
		+ "<td align=\"left\"><b>Author<b></td>"
		+ "<td align=\"left\"><b>Volume<b></td>"
		+ "<td align=\"left\"><b>Standard Number<b></td>"
		+ "<td align=\"left\"><b>Rightsholder<b></td>"
		+ "<td align=\"left\"><b>Publication Year<b></td>"
		+ "<td align=\"left\"><b>Your Name<b></td>"
		+ "<td align=\"left\"><b>Email Address<b></td>"
		+ "<td align=\"left\"><b>Company<b></td>"
		+ "<td align=\"left\"><b>Phone Number<b></td>"
		+ "<td align=\"left\"><b>City<b></td>"
		+ "<td align=\"left\"><b>State<b></td>"
		+ "<td align=\"left\"><b>Annual License<b></td>"
		+ "<td align=\"left\"><b>Additional Info<b></td>"
		+ "</tr>";



}
