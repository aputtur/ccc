package com.copyright.ccc.quartz.services;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.SQLData;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.copyright.ccc.CCRuntimeException;
import com.copyright.ccc.business.data.IAutoDunningParam;
import com.copyright.ccc.business.data.IOverdueInvoice;
import com.copyright.ccc.business.data.access.CC2DataAccessConstants;
import com.copyright.ccc.business.data.access.CC2OADOracleProcedureInvokerFactory;
import com.copyright.ccc.business.data.access.CC2OracleProcedureInvokerFactory;
import com.copyright.ccc.business.services.invoice.AutoDunningParamResultTypeDTO;
import com.copyright.ccc.business.services.invoice.OverdueInvoiceResultTypeDTO;
import com.copyright.ccc.business.services.invoice.OverdueInvoiceSummaryImpl;
import com.copyright.ccc.config.CC2Configuration;
import com.copyright.mail.MailDispatcher;
import com.copyright.mail.MailDispatcherImpl;
import com.copyright.mail.MailMessage;
import com.copyright.mail.MailMessageWithAttachments;
import com.copyright.opi.InvocationExpectedEventRuntimeException;
import com.copyright.opi.InvocationRuntimeException;
import com.copyright.opi.InvocationUnexpectedErrorRuntimeException;
import com.copyright.opi.SingleDTOProcedureInvoker;
import com.copyright.opi.TypedParameter;
import com.copyright.workbench.logging.LoggerHelper;
import com.copyright.workbench.util.ArrayUtils2;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class AutoDunningHandler
{
	private static final Logger _logger = LoggerHelper.getLogger();

	Map<OverdueInvoiceSummaryImpl, String> mapEmail = new HashMap<OverdueInvoiceSummaryImpl, String>();
	Map<OverdueInvoiceSummaryImpl, String> mapNondelivery = new HashMap<OverdueInvoiceSummaryImpl, String>();
	
	public Map<Long, List<IOverdueInvoice>> retrieveOverdueInvoiceByCustomer(IOverdueInvoice[] invoices)
	{
		Map<Long, List<IOverdueInvoice>> map = new TreeMap<Long, List<IOverdueInvoice>>();
		for (IOverdueInvoice invoice : invoices)
		{
			Long customerNumber = invoice.getCustomerNumber();
			if (map.containsKey(customerNumber))
			{
				map.get(customerNumber).add(invoice);
			}
			else
			{
				List<IOverdueInvoice> list = new ArrayList<IOverdueInvoice>();
				list.add(invoice);
				map.put(customerNumber, list);
			}
		}

		return map;
	}

	public IOverdueInvoice[] retrieveOverdueInvoice(String daysPastDue, String customerAcctId)
	{
		SingleDTOProcedureInvoker invoker = CC2OADOracleProcedureInvokerFactory.getInstance().singleDTOInvoker();

		TypedParameter[] inputParameters = new TypedParameter[2];
		inputParameters[0] = new TypedParameter(Long.parseLong(daysPastDue));
		inputParameters[1] = new TypedParameter(customerAcctId);
		invoker.configure(CC2DataAccessConstants.CustUser.GET_OVERDUE_INVOICES, OverdueInvoiceResultTypeDTO
				.getRefInstance(), inputParameters);

		invoker.setNoDataFoundAcceptable(true);

		try
		{
			invoker.invoke();
		}
		catch (InvocationExpectedEventRuntimeException e)
		{
			throw new CCRuntimeException( invoker.getReturnCodes().getOracleDescription(), e );
		}
		catch (InvocationUnexpectedErrorRuntimeException e)
		{
			throw new CCRuntimeException( e.getMessageCode(), e );
		}

		catch (InvocationRuntimeException e)
		{
			throw new CCRuntimeException( invoker.getReturnCodes().getOracleDescription(), e );
		}

		OverdueInvoiceResultTypeDTO overdueInvoiceData = (OverdueInvoiceResultTypeDTO) invoker.getDTO();

		SQLData[] datas = null;
		if (overdueInvoiceData != null)
		{
			datas = (SQLData[]) overdueInvoiceData.getOverdueInvoices();
		}

		IOverdueInvoice[] overdueInvoices = null;
		overdueInvoices = (IOverdueInvoice[]) ArrayUtils2.convertArray(datas, IOverdueInvoice.class);

		//for (IOverdueInvoice overdueInvoice : overdueInvoices)
		//{
		//	_logger.info("*** " + overdueInvoice.getCustomerName());
		//}

		return overdueInvoices;

	}
	
	public IAutoDunningParam[] retrieveAutoDunningParams()
	{
		SingleDTOProcedureInvoker invoker = CC2OracleProcedureInvokerFactory.getInstance().singleDTOInvoker();

		invoker.configure(CC2DataAccessConstants.CCUser.GET_AUTODUNNING_PARAMS, AutoDunningParamResultTypeDTO
				.getRefInstance());

		invoker.setNoDataFoundAcceptable(true);

		try
		{
			invoker.invoke();
		}
		catch (InvocationExpectedEventRuntimeException e)
		{
			throw new CCRuntimeException( invoker.getReturnCodes().getOracleDescription(), e );

		}
		catch (InvocationUnexpectedErrorRuntimeException e)
		{
			throw new CCRuntimeException( e.getMessageCode(), e );
		}

		catch (InvocationRuntimeException e)
		{
			throw new CCRuntimeException( invoker.getReturnCodes().getOracleDescription(), e );
		}

		AutoDunningParamResultTypeDTO paramsData = (AutoDunningParamResultTypeDTO) invoker.getDTO();

		SQLData[] datas = null;
		if (paramsData != null)
		{
			datas = (SQLData[]) paramsData.getAutoDunningParams();
		}

		IAutoDunningParam[] params = null;
		params = (IAutoDunningParam[]) ArrayUtils2.convertArray(datas, IAutoDunningParam.class);

		for (IAutoDunningParam param : params)
		{
			_logger.info("*** run overdue invoices for product type = " + param.getProductType());
		}

		return params;

	}
	
	//filter by autodunning params
	public IOverdueInvoice[] filterByAutoDunningParams(IOverdueInvoice[] overdueInvoices, Map<String, IAutoDunningParam> params ){
		List<IOverdueInvoice> list = new ArrayList<IOverdueInvoice>();
		
		for(IOverdueInvoice invoice: overdueInvoices){
			if(params.containsKey(invoice.getTrxType())){
				list.add(invoice);
			}
		}
		IOverdueInvoice[] array = new IOverdueInvoice[list.size()];
		return list.toArray(array);
	} 

	// returns Map<OverdueInvoiceSummaryImpl overdueSummary, String emailBody> group by customer
	public Map<OverdueInvoiceSummaryImpl, String> generateEmailByCustomer(Map<Long, List<IOverdueInvoice>> mapInvoices)
	{
		
		for (Iterator<Map.Entry<Long, List<IOverdueInvoice>>> iter = mapInvoices.entrySet().iterator(); iter.hasNext();)
		{
			Map.Entry<Long, List<IOverdueInvoice>> entry = iter.next();
			List<IOverdueInvoice> overdueInvoices = entry.getValue();
			OverdueInvoiceSummaryImpl invoiceSummary = new OverdueInvoiceSummaryImpl();
			Double totalInvoiceAmount = 0.0;
			for (IOverdueInvoice overdueInvoice : overdueInvoices)
			{
				if (StringUtils.isNotEmpty(overdueInvoice.getBillToName()))
				{
					invoiceSummary.setBillToName(overdueInvoice.getBillToName());
				}
				
				if (StringUtils.isNotEmpty(overdueInvoice.getBillToAddress()))
				{
				invoiceSummary.setBillToAddress(overdueInvoice.getBillToAddress());
				}
				
				if (StringUtils.isNotEmpty(overdueInvoice.getAdminName()))
				{
					invoiceSummary.setAdminName(overdueInvoice.getAdminName());
					
				}
				if (StringUtils.isNotEmpty(overdueInvoice.getAdminAddress()))
				{
					invoiceSummary.setAdminAddress(overdueInvoice.getAdminAddress());					
				}
				if (StringUtils.isNotEmpty(overdueInvoice.getCustomerName()))
				{
					invoiceSummary.setCustomerName(overdueInvoice.getCustomerName());					
				}
				invoiceSummary.setCustomerNumber(overdueInvoice.getCustomerNumber());					
				
				totalInvoiceAmount += overdueInvoice.getOutstandingAmount();

			}

			boolean checked = false;
			if((StringUtils.isNotEmpty(invoiceSummary.getAdminName())||StringUtils.isNotEmpty(invoiceSummary.getBillToName())) 
					&& (StringUtils.isNotEmpty(invoiceSummary.getAdminAddress())||StringUtils.isNotEmpty(invoiceSummary.getBillToAddress())) 
					&& StringUtils.isNotEmpty(invoiceSummary.getCustomerName()) 
					&& invoiceSummary.getCustomerNumber()!=null)
			{
				checked = true;
			}
			invoiceSummary.setTotalInvoiceAmount(totalInvoiceAmount);

			if(checked)
			{	
				String strEmailBody = this.generateEmail(overdueInvoices, invoiceSummary, "email-text.ftl");
				mapEmail.put(invoiceSummary, strEmailBody);
			}
			else
			{
				mapNondelivery.put(invoiceSummary, null);
			}	

		}
		return mapEmail;
	}

	public void sendMail(Map<OverdueInvoiceSummaryImpl, String> mapEmail, String overrideEmailRecipient,
			String overrideTotalNumEmailSent)
	{

		boolean testMode = CC2Configuration.getInstance().getAutoDunningTestModeEnable();
		String emailSender = CC2Configuration.getInstance().getOverdueInvoiceEmailSender();

		if (mapEmail != null && mapEmail.size() > 0)
		{
			_logger.info("Run Email Overdue Invoice Notification Job: " + new Date());
		
		int counter = 0;
		String ID = "E005.123";

		MailMessage message = new MailMessage();
		
		MailDispatcher dispatcher = new MailDispatcherImpl();
		
				for (Iterator<Map.Entry<OverdueInvoiceSummaryImpl, String>> iter = mapEmail.entrySet().iterator(); iter.hasNext();)
				{
					Map.Entry<OverdueInvoiceSummaryImpl, String> entry = iter.next();
					OverdueInvoiceSummaryImpl overdueInvoiceSummary = entry.getKey();
					String strEmailBody = entry.getValue();
		
					String _emailRecipient = null;
					// extra measure: only send email to customer in production
					if (!testMode && StringUtils.isEmpty(overrideEmailRecipient))
					{
						String emailSentAddress = overdueInvoiceSummary.getAdminAddress()!=null?overdueInvoiceSummary.getAdminAddress():overdueInvoiceSummary.getBillToAddress();
						_emailRecipient = emailSentAddress;
					}
					else
					{
						_emailRecipient = overrideEmailRecipient;
					}
					
					int _totalNumEmailSent = 0;
					
					if (overrideTotalNumEmailSent != null && StringUtils.isNumeric(overrideTotalNumEmailSent))
					{
						_totalNumEmailSent = Integer.parseInt(overrideTotalNumEmailSent);
					}
					
					message.setSubject(overdueInvoiceSummary.getCustomerNumber() + " " + overdueInvoiceSummary.getCustomerName() + " - Unpaid Invoice(s) from Copyright Clearance Center");
					message.setBody(strEmailBody);
					message.setRecipient(_emailRecipient);
					message.setFromEmail(emailSender);
					message.setCustomMessageID(ID);
		
					try
					{
						long start = System.currentTimeMillis();
						dispatcher.send(message);
						// avoid racing condition for generating emails and sending emails
						Thread.sleep(1000);
						_logger.info("time elapsed: " + (System.currentTimeMillis() - start));
					}
					catch (InterruptedException e)
					{
						// do nothing
					}
		
					counter++;
					// send only the total number of "TEST" email specified by user or by default value in properties file
					if (testMode && (counter >= _totalNumEmailSent))
						break;
		
				}
		}
		else
		{
			_logger.info("No invoice found, stop running email: " + new Date());
		}
	}
	
	public void sendAutoDunningStatusLog()
	{

		String statusLogReceiver = CC2Configuration.getInstance().getAutoDunningStatusLogReceiver();
		String mailSender = CC2Configuration.getInstance().getOverdueInvoiceEmailSender();

		String emailBody = "";
		String mailLogStr = "";
		String nonDeliverableLogStr = "";
		String ID = "E005.123";
		 SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMdd_HHmm");
		 Date currentTime = new Date();
		 String dateString = formatter.format(currentTime);

		Map<OverdueInvoiceSummaryImpl, String> _mapNondelivery = this.getMapNondelivery();
		Map<OverdueInvoiceSummaryImpl, String> _mapEmail = this.getMapEmail();

		MailMessageWithAttachments message = new MailMessageWithAttachments();
		MailDispatcher dispatcher = new MailDispatcherImpl();
		if (_mapEmail != null && _mapEmail.size() > 0)
		{
			mailLogStr = this.generateStatusLog(_mapEmail, "statusLog-text.ftl");
			emailBody += "Autodunning email sent: " + _mapEmail.size() + "\n";

		}
		else
		{
			emailBody += "No autodunning email sent \n";
		}

		if (_mapNondelivery != null && _mapNondelivery.size() > 0)
		{
//			for (Iterator iter = _mapNondelivery.entrySet().iterator(); iter.hasNext();)
//			{
//				Map.Entry<OverdueInvoiceSummaryImpl, String> entry = 
//					(Entry<OverdueInvoiceSummaryImpl, String>) iter.next();
//				OverdueInvoiceSummaryImpl overdueInvoiceSummary = entry.getKey();
//				nonDeliverableLog.append(overdueInvoiceSummary.getCustomerNumber() + delimiter
//						+ enclosedBy + overdueInvoiceSummary.getCustomerName()  + enclosedBy + delimiter
//						+ enclosedBy + (overdueInvoiceSummary.getAdminName()!=null?overdueInvoiceSummary.getAdminName():overdueInvoiceSummary.getBillToName()) + enclosedBy + delimiter
//						+ enclosedBy + (overdueInvoiceSummary.getAdminAddress()!=null?overdueInvoiceSummary.getAdminAddress():overdueInvoiceSummary.getBillToAddress()) + enclosedBy + delimiter
//						+ overdueInvoiceSummary.getTotalInvoiceAmount() + delimiter
//						+ "\n");
//			}
			nonDeliverableLogStr = this.generateStatusLog(_mapNondelivery, "statusLog-text.ftl");
			emailBody += "Overdue invoices didn't send due to missing information: "
					+ _mapNondelivery.size() + "\n";
		}
		File logFolder = new File(CC2Configuration.getInstance().getAutoDunningStatusLogLocation());
		File emailsSentLog = new File(logFolder, "autoDunningEmailsSent_" + dateString + ".csv");
		File unsendInvoicesLog = new File(logFolder, "missingDataAutoDunningEmails_" + dateString + ".csv");
		_logger.info("file path for emailsSentLog="+emailsSentLog.getAbsolutePath());
		_logger.info("file path for unsendInvoicesLog="+unsendInvoicesLog.getAbsolutePath());

		BufferedOutputStream bos1 = null;
		BufferedOutputStream bos2 = null;
		try
		{
			bos1 = new BufferedOutputStream(new FileOutputStream(emailsSentLog));
			bos1.write(mailLogStr.getBytes());
			
			bos2 = new BufferedOutputStream(new FileOutputStream(unsendInvoicesLog));
			bos2.write(nonDeliverableLogStr.getBytes());

		}
		catch (IOException ioe)
		{
			_logger.warn( ExceptionUtils.getFullStackTrace(ioe) );
		} finally {
	        //Close the BufferedOutputStream
	        try {
	            if (bos1 != null) {
	            	bos1.flush();
	            	bos1.close();
	            }
	            if (bos2 != null) {
	            	bos2.flush();
	            	bos2.close();
	            }
	        } catch (IOException ex) {
				_logger.warn( ExceptionUtils.getFullStackTrace(ex) );
	        }
	    }
		File[] attachments = new File[2];
		attachments[0] = emailsSentLog;
		attachments[1] = unsendInvoicesLog;
		message.setSubject("AutoDunning Batch Process Status Log from Copyright Clearance Center");
		message.setBody(emailBody);
		message.setAttachments(attachments);
		message.setRecipient(statusLogReceiver);
		message.setFromEmail(mailSender);
		message.setCustomMessageID(ID);

		try
		{
			long start = System.currentTimeMillis();
			dispatcher.send(message);
			// avoid racing condition for generating emails and sending emails
			Thread.sleep(1000);
			_logger.info("time elapsed: " + (System.currentTimeMillis() - start));
		}
		catch (InterruptedException e)
		{
			// do nothing
		}

	}

	private String generateEmail(List<IOverdueInvoice> overdueInvoices, OverdueInvoiceSummaryImpl invoiceSummary,
			String templateName)
	{
		Configuration cfg = new Configuration();

		try
		{
			cfg.setDirectoryForTemplateLoading(new File(CC2Configuration.getInstance()
							.getFreemarkerTemplateLocation()));
		}
		catch (IOException e)
		{
			throw new CCRuntimeException(e);
		}

		cfg.setObjectWrapper(new DefaultObjectWrapper());

		Template template = null;
		Map<String,Object> dataModel = new HashMap<String,Object>();
		dataModel.put("invoiceSummary", invoiceSummary);
		dataModel.put("overdueInvoices", overdueInvoices);
		StringWriter writer = new StringWriter();

		try
		{
			template = cfg.getTemplate(templateName);
			template.process(dataModel, writer);
		}
		catch (TemplateException e)
		{
			throw new CCRuntimeException(e);
		}
		catch (IOException e)
		{
			throw new CCRuntimeException(e);
		}

		return writer.getBuffer().toString();
	}

	private String generateStatusLog(Map<OverdueInvoiceSummaryImpl, String> map, String templateName)
	{
		List<OverdueInvoiceSummaryImpl> list = new ArrayList<OverdueInvoiceSummaryImpl>();
		for (Iterator<Map.Entry<OverdueInvoiceSummaryImpl, String>> iter = map.entrySet().iterator(); iter.hasNext();)
		{
			Map.Entry<OverdueInvoiceSummaryImpl, String> entry = iter.next();
			OverdueInvoiceSummaryImpl overdueInvoiceSummary = entry.getKey();
			list.add(overdueInvoiceSummary);
		}
		
		Configuration cfg = new Configuration();

		try
		{
			cfg.setDirectoryForTemplateLoading(new File(CC2Configuration.getInstance()
							.getFreemarkerTemplateLocation()));
		}
		catch (IOException e)
		{
			throw new CCRuntimeException(e);
		}

		cfg.setObjectWrapper(new DefaultObjectWrapper());

		Template template = null;
		Map<String,List<OverdueInvoiceSummaryImpl>> dataModel = 
			new HashMap<String,List<OverdueInvoiceSummaryImpl>>();
		dataModel.put("overdueInvoiceSummary", list);
		
		StringWriter writer = new StringWriter();

		try
		{
			template = cfg.getTemplate(templateName);
		
			template.process(dataModel, writer);
		}
		catch (TemplateException e)
		{
			throw new CCRuntimeException(e);
		}
		catch (IOException e)
		{
			throw new CCRuntimeException(e);
		}

		return writer.getBuffer().toString();
	}

	
	public Map<OverdueInvoiceSummaryImpl, String> getMapEmail()
	{
		return mapEmail;
	}

	public Map<OverdueInvoiceSummaryImpl, String> getMapNondelivery()
	{
		return mapNondelivery;
	}

	
}
