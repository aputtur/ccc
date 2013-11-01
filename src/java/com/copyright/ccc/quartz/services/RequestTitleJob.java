/*
 * RequestTitleJob.java
 * Copyright (c) 2011, Copyright Clearance Center, Inc. All rights reserved.
 * ----------------------------------------------------------------------------
 * Revision History
 * 2011.07.18	tmckinney	Integrated into CC3 from Siva's work.
 * ----------------------------------------------------------------------------
 */
package com.copyright.ccc.quartz.services;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.copyright.ccc.config.CC2Configuration;
import com.copyright.mail.MailDispatcherImpl;
import com.copyright.workbench.io.IOUtils;
import com.copyright.workbench.logging.LoggerHelper;
import com.copyright.workbench.sql.ConnectionWrapper;
import com.copyright.workbench.sql.JNDIConnectionWrapper;

public class RequestTitleJob implements Job
{
	private static Logger logger = LoggerHelper.getLogger();
	private static CC2Configuration config = CC2Configuration.getInstance();
	
	
	public void execute(JobExecutionContext context) throws JobExecutionException
	{

		logger.info("CC3 --> RequestTitleJob entered.");
		
		RequestTitleHandler handler = getRequestTitleHandler();
		List<RequestTitleData> requests = handler.getRequests();
		
		//create email message
		RequestTitleMessage message = new RequestTitleMessage();
		addContent(message, requests);
		
		//send message
		logger.info("RequestTitleJob --> about to send " + message.toString());
		MailDispatcherImpl mailer = new MailDispatcherImpl();
		mailer.send(message);
	
	}
	

	/* (non-Javadoc)
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 * @throws 
	 */
	public void executeImpl()
	{}
	
	
	/**
	 * Create an attachment and attach it to the message.
	 * @param message
	 */
	private void addContent(RequestTitleMessage message, List<RequestTitleData> requests)
	{
		RequestTitleXLSFromHTMLAttachment attachment = new RequestTitleXLSFromHTMLAttachment();
		attachment.addHTMLContent(RequestTitleXLSFromHTMLAttachment.STD_HTML_HEADER);
		attachment.addHTMLContent(RequestTitleMessage.HEADER);
		
		for ( RequestTitleData req : requests )
		{
			attachment.addHTMLContent(req.getHTMLRepresentation());
		}
		
		attachment.addHTMLContent(RequestTitleXLSFromHTMLAttachment.STD_HTML_FOOTER);
		
		try
		{
			File file = writeAttachmentToDisk(attachment);
			File[] files = { file };
			message.setAttachments(files);
		}
		catch (IOException ioe)
		{
			logger.error("Unable to create attachment.", ioe);
		}
	}
	
	
	/**
	 * Create attachment as file on disk.
	 * @param att
	 * @return
	 * @throws IOException
	 */
	private File writeAttachmentToDisk(RequestTitleXLSFromHTMLAttachment att)
		throws IOException
	{
		String directory = config.getReqtitleTmpDir();
		String filename = config.getReqtitleAttachmentName();
		File file = new File(directory,filename);
		
		if (file.exists())
		{
			file.delete();
		}
		
		IOUtils.stringToFile(att.getContent(), file);

		return file;
	}
	
		
	private RequestTitleHandler getRequestTitleHandler()
	{
		/*
		 * We could make this more dynamic to allow passing in test credentials.
		 * 
		 * See the "comm-mod-contact" module for an example.
		 */
		
		String datasource = config.getDatasourceJNDIName();
			
		ConnectionWrapper wrapper = new JNDIConnectionWrapper(datasource);
		
		return new RequestTitleHandler(wrapper);
	}




}
