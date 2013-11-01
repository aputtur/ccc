/*
 * RequestTitleData.java
 * Copyright (c) 2007, Copyright Clearance Center, Inc. All rights reserved.
 * ----------------------------------------------------------------------------
 * Revision History
 * 2007.10.24	tmckinney	Created.
 * ----------------------------------------------------------------------------
 */
package com.copyright.ccc.quartz.services;

import com.copyright.workbench.util.StringUtils2;

/**
 * @author tmckinney
 * @version 1.0
 */
public class RequestTitleData
{
	private String product;
	private String title;
	private String publisher;
	private String author;
	private String volume;
	private String standardNumber;
	private String rightsholder;
	private String publicationYear;
	private String yourName;
	private String emailAddress;
	private String company;
	private String phoneNumber;
	private String city;
	private String state;
	private String annualLicense;
	private String additionalInfo;
	
	

	
	public String getHTMLRepresentation()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<tr>");
		sb.append("<td>").append(this.product).append("</td>");
		sb.append("<td>").append(this.title).append("</td>");
		sb.append("<td>").append(StringUtils2.fixNull(this.publisher)).append("</td>");
		sb.append("<td>").append(StringUtils2.fixNull(this.author)).append("</td>");
		sb.append("<td>").append(StringUtils2.fixNull(this.volume)).append("</td>");
		sb.append("<td>").append(RequestTitleXLSFromHTMLAttachment.escapeNumeric(this.standardNumber)).append("</td>");
		sb.append("<td>").append(StringUtils2.fixNull(this.rightsholder)).append("</td>");
		sb.append("<td>").append(StringUtils2.fixNull(this.publicationYear)).append("</td>");
		sb.append("<td>").append(this.yourName).append("</td>");
		sb.append("<td>").append(this.emailAddress).append("</td>");
		sb.append("<td>").append(this.company).append("</td>");
		sb.append("<td>").append(this.phoneNumber).append("</td>");
		sb.append("<td>").append(this.city).append("</td>");
		sb.append("<td>").append(this.state).append("</td>");
		sb.append("<td>").append(this.annualLicense).append("</td>");
		sb.append("<td>").append(StringUtils2.fixNull(this.additionalInfo)).append("</td>");
		sb.append("</tr>");
		
		return sb.toString();
	}
	

	public String getProduct()
	{
		return product;
	}

	public void setProduct(String product)
	{
		this.product = product;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getPublisher()
	{
		return publisher;
	}

	public void setPublisher(String publisher)
	{
		this.publisher = publisher;
	}

	public String getAuthor()
	{
		return author;
	}

	public void setAuthor(String author)
	{
		this.author = author;
	}

	public String getVolume()
	{
		return volume;
	}

	public void setVolume(String volume)
	{
		this.volume = volume;
	}

	public String getStandardNumber()
	{
		return standardNumber;
	}

	public void setStandardNumber(String standardNumber)
	{
		this.standardNumber = standardNumber;
	}

	public String getRightsholder()
	{
		return rightsholder;
	}

	public void setRightsholder(String rightsholder)
	{
		this.rightsholder = rightsholder;
	}

	public String getPublicationYear()
	{
		return publicationYear;
	}

	public void setPublicationYear(String publicationYear)
	{
		this.publicationYear = publicationYear;
	}

	public String getYourName()
	{
		return yourName;
	}

	public void setYourName(String yourName)
	{
		this.yourName = yourName;
	}

	public String getEmailAddress()
	{
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress)
	{
		this.emailAddress = emailAddress;
	}

	public String getCompany()
	{
		return company;
	}

	public void setCompany(String company)
	{
		this.company = company;
	}

	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber)
	{
		this.phoneNumber = phoneNumber;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public String getState()
	{
		return state;
	}

	public void setState(String state)
	{
		this.state = state;
	}
	
	public String getAnnualLicense()
	{
		return annualLicense;
	}

	public void setAnnualLicense(String annualLicense)
	{
		this.annualLicense = annualLicense;
	}
	
	public String getAdditionalInfo()
	{
		return additionalInfo;
	}
	
	public void setAdditionalInfo(String additionalInfo)
	{
		this.additionalInfo = additionalInfo;
	}
}
