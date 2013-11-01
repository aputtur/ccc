/*
 * EmailVerificationService.java
 * Copyright (c) 2010, Copyright Clearance Center, Inc. All rights reserved.
 * ----------------------------------------------------------------------------
 * Revision History
 * 02-23-2010   ASaxena  Created.
 * ----------------------------------------------------------------------------
 */

package com.copyright.ccc.business.services.email.services;

import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Holder;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.copyright.ccc.config.CC2Configuration;
import com.strikeiron.EmailVerification;
import com.strikeiron.EmailVerificationSoap;
import com.strikeiron.SISubscriptionInfo;
import com.strikeiron.SIWsOutputOfVerifyEmailRecord;

/**
 * This service class is used to verify the email from strikeiron.
 * 
 */
public class EmailVerificationService
{

	private static Logger _logger = Logger
			.getLogger(EmailVerificationService.class);

	private static enum inValidResponse
	{
		EMAIL_NOT_VALID(201),  DOMAIN_NOT_FOUND(310), INVALID_LOCAL_NAME(401), INVALID_DOMAIN_NAME(402) ;

		private final int mStatusNum;

		inValidResponse(int statusNum)
		{
			mStatusNum = statusNum;
		}

		public boolean contains(int statusNum)
		{
			for (inValidResponse value : inValidResponse.values())
			{
				if (value.mStatusNum == statusNum)
					return Boolean.TRUE;
			}
			return Boolean.FALSE;
		}
	}

	/**
	 * Entry point method for calling classes, used to verify the supplied email
	 * address.
	 * 
	 * @param email
	 */
	public static boolean verifyEmail(String email)
	{

		/* Currently we are using stub generation to communicate to strikeiron */

		boolean isValidEmail = false;

		try
		{
			String userID = CC2Configuration.getInstance()
					.getEmailVerifcationUsername();
			String password = CC2Configuration.getInstance()
					.getEmailVerifcationPassword();
			String timeout = CC2Configuration.getInstance()
					.getEmailVerifcationTimeout();

			EmailVerification verification = new EmailVerification();
			Holder<SIWsOutputOfVerifyEmailRecord> emailVerfyResult = new Holder<SIWsOutputOfVerifyEmailRecord>();
			Holder<SISubscriptionInfo> subInfo = new Holder<SISubscriptionInfo>();
			EmailVerificationSoap verficationPort = verification
					.getEmailVerificationSoap();

			verficationPort.verifyEmail(null, userID, password, email,
					new Integer(timeout), emailVerfyResult, subInfo);

			if (_logger.isDebugEnabled())
			{
				_logger.debug("Strike iron soap response status: "
						+ emailVerfyResult.value.getServiceStatus()
								.getStatusNbr() + " for email: "+ email );
			}

			return !inValidResponse.EMAIL_NOT_VALID.contains(emailVerfyResult.value
					.getServiceStatus().getStatusNbr());

		}
		catch (Exception e)
		{
			_logger.error(ExceptionUtils.getCause(e));
			isValidEmail = Boolean.TRUE;
		}

		return isValidEmail;

		// We used the above approach instead of SAAJ, because, Jboss has its
		// own implmentation of saaj.
		// which gives us class cast exception, now we can not prevents this
		// currently
		// because MessageFactory class creates the instance of SOAPMessage
		// implementation.
		// during runtime it fails to create sun microsystems implementation.

		/*
		 * boolean isValidEmail = false; try { // First create the connection
		 * SOAPConnectionFactory soapConnFactory = SOAPConnectionFactory
		 * .newInstance(); SOAPConnection connection =
		 * soapConnFactory.createConnection();
		 * 
		 * // Next, create the request message MessageFactory messageFactory =
		 * MessageFactory.newInstance(); SOAPMessage message =
		 * messageFactory.createMessage();
		 * 
		 * SOAPPart soapPart = message.getSOAPPart(); SOAPEnvelope envelope =
		 * soapPart.getEnvelope(); envelope .addNamespaceDeclaration("web",
		 * "http://www.strikeiron.com");
		 * 
		 * // set request(MIME+SOAP) headers. setRequestHeaders(message,
		 * envelope);
		 * 
		 * // create request body with valid parameters
		 * createRequestBody(envelope, email);
		 * 
		 * if (message.saveRequired()) message.saveChanges();
		 * 
		 * if (_logger.isDebugEnabled()) _logger.debug("Verification request: "
		 * + message);
		 * 
		 * String serviceEndpoint = CC2Configuration.getInstance()
		 * .getEmailVerificationServiceEndpoint(); // get the response
		 * SOAPMessage reply = connection.call(message, serviceEndpoint);
		 * 
		 * isValidEmail = checkResponse(reply);
		 * 
		 * // Close the connection connection.close();
		 * 
		 * } catch (Exception e) { _logger.error("Verification error: " + e);
		 * 
		 * // if either of the below conditions are true, we need to set valid
		 * // flag true, so that it can // jump of the email verification if (e
		 * instanceof SOAPException &&
		 * (e.getMessage().contains("Message send failed"))) isValidEmail =
		 * Boolean.TRUE; else if (e instanceof PrivilegedActionException &&
		 * (e.getMessage().contains("Message send failed"))) isValidEmail =
		 * Boolean.TRUE; else if (e instanceof NoRouteToHostException)
		 * isValidEmail = Boolean.TRUE; else if (e instanceof SOAPException &&
		 * e.getCause() instanceof IOException) isValidEmail = Boolean.TRUE; }
		 */

	}

	// @private : method used to set request(HTTP+SOAP) headers
	private static void setRequestHeaders(SOAPMessage message,
			SOAPEnvelope envelope) throws SOAPException
	{
		// setting HTTP Header
		MimeHeaders mimeHeader = message.getMimeHeaders();
		mimeHeader.setHeader("SOAPAction",
				"http://www.strikeiron.com/VerifyEmail");

		// setting request Header
		SOAPHeader messageHeader = envelope.getHeader();
		SOAPElement licenseInfo = messageHeader.addChildElement("LicenseInfo",
				"web");
		SOAPElement registeredUser = licenseInfo.addChildElement(
				"RegisteredUser", "web");

		registeredUser.addChildElement("UserID", "web").addTextNode(
				CC2Configuration.getInstance().getEmailVerifcationUsername());
		registeredUser.addChildElement("Password", "web").addTextNode(
				CC2Configuration.getInstance().getEmailVerifcationPassword());
	}

	// @private : method to create the request body
	private static void createRequestBody(SOAPEnvelope envelope, String email)
			throws SOAPException
	{
		String operationName = CC2Configuration.getInstance()
				.getEmailVerificationServiceOperation();
		SOAPBody soapBody = envelope.getBody();
		SOAPElement operation = soapBody.addChildElement(operationName, "web");
		operation.addChildElement("Email", "web").addTextNode(email);
		String timeout = CC2Configuration.getInstance()
				.getEmailVerifcationTimeout();
		operation.addChildElement("Timeout", "web").addTextNode(timeout);
	}

	// @private : method to check the valid soap response
	private static boolean checkResponse(SOAPMessage response) throws Exception
	{
		// getting result the JAXM way
		SOAPEnvelope respEnvelope = response.getSOAPPart().getEnvelope();
		SOAPBody respBody = respEnvelope.getBody();
		int statusNbr = 0;
		if (respBody.hasFault())
		{
			// oops we have fault response, log it and let it proceed.
			if (_logger.isDebugEnabled())
				_logger.debug(respBody.getFault().getFaultString());
			return Boolean.TRUE;
		}
		else
		{
			// should have used auto-boxing, but auto-boxing have conversion
			// issues
			statusNbr = new Integer(respBody.getElementsByTagName(
					"StatusNbr").item(0).getTextContent()).intValue();
		}
		return (statusNbr != 200) ? Boolean.FALSE : Boolean.TRUE;
	}

}
