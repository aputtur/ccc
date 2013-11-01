package com.copyright.ccc.web.actions.ordermgmt.util;


import org.apache.commons.lang.StringUtils;

/**
 * Contains publication date utility methods.
 * 
 * @author gcuevas
 *
 */
public class PublicationDateUtils {
	
	final static String DATE_SEPARATOR = "/";
	final static String EMPTY_DATE_VALUE = "00";
	
	/**
	 * Converts startPublicationDate and endPublicationDate from the database to a user friendly
	 * date range string.
	 * 
	 * @param rawStartPublicationDate
	 * @param rawEndPublicationDate
	 */
	public static String convertToDisplayFormat(String rawStartPublicationDate, String rawEndPublicationDate)
	{
		String startDate = PublicationDateUtils.convertToDisplayFormat(rawStartPublicationDate);
		String endDate = PublicationDateUtils.convertToDisplayFormat(rawEndPublicationDate);
		
		boolean startDateEmpty = StringUtils.isEmpty(startDate);
		boolean endDateEmpty = StringUtils.isEmpty(endDate);
		
		StringBuffer dateRange = new StringBuffer();
		
		if(!startDateEmpty)
			dateRange.append(startDate);
		
		if(!startDateEmpty && !endDateEmpty)
			dateRange.append(" -  ");
		
		if(!endDateEmpty)
			dateRange.append(endDate);
		
		return dateRange.toString();
	}

	/**
	 * Converts a single publication date from the database to a user friendly publication date string.
	 * 
	 * @param rawPublicationDate
	 */
	public static String convertToDisplayFormat(String rawPublicationDate)
	{
		rawPublicationDate = StringUtils.trimToNull(rawPublicationDate);
		
		if(rawPublicationDate == null)
			return "";

		int rawPublicationDateLength = rawPublicationDate.length();
		
		StringBuffer displayDate = new StringBuffer();
		
		if(rawPublicationDateLength > 0) //append year
			displayDate.append(rawPublicationDate.substring(0, Math.min(4, rawPublicationDateLength)));
		
		if(rawPublicationDateLength > 4) //append month
		{
			String month = rawPublicationDate.substring(4, Math.min(6, rawPublicationDateLength));
			if(!StringUtils.equals(month, EMPTY_DATE_VALUE))
				displayDate.append(DATE_SEPARATOR + month);
		}
		
		if(rawPublicationDateLength > 6) //append day
		{
			String day = rawPublicationDate.substring(6, Math.min(8, rawPublicationDateLength));
			if(!StringUtils.equals(day, EMPTY_DATE_VALUE))
				displayDate.append(DATE_SEPARATOR + day);
		}
		
		return displayDate.toString();
	}
	
	/**
	 * Determins if the publication date entered by the user is valid.
	 * 
	 * Valid pub dates are yyyy or yyyy/MM or yyyy/MM/dd
	 * 
	 * @param publicationDate
	 */
	public static boolean isPublicationDateValid(String publicationDate)
	{
		String trimmed = StringUtils.trimToEmpty(publicationDate);
		
		if(StringUtils.isEmpty(trimmed))
			return true;
		
		String[] tokens = trimmed.split(DATE_SEPARATOR);
		
		if(tokens.length > 3)
			return false;
		
		int month = 0;
		
		for(int i = 0; i < tokens.length; i++)
		{
			String token = tokens[i];
			
			if((i == 0 && token.length() != 4) || (i > 0 && token.length() != 2))
				return false;
			
			try
			{
				int tokenInt = Integer.parseInt(token);
				
				if(tokenInt < 0)
					return false;
				
				//year
				else if(i == 0 && (tokenInt < 1000 || tokenInt > 3000))
					return false;
				//month
				else if(i == 1)
				{
					month = tokenInt;
					
					if(tokenInt > 12)
						return false;
				}
				//date
				else if(i == 2)
				{
					if((tokenInt > 0 && month <= 0) || tokenInt > 31)
						return false;
				}
			}
			catch(NumberFormatException nfe)
			{
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Converts from a user entered publication date to a Long publication date used
	 * by the database.
	 * 
	 * @param publicationDate
	 * @throws InvalidPublicationDateException
	 */
	public static Long convertToRawPublicationDate(String publicationDate) throws InvalidPublicationDateException
	{
		String trimmed = StringUtils.trimToEmpty(publicationDate);
		
		if(StringUtils.isEmpty(trimmed))
			return null;
		
		if(!isPublicationDateValid(trimmed))
			throw new InvalidPublicationDateException(publicationDate);
		
		String[] tokens = trimmed.split(DATE_SEPARATOR);

		String appendedZeros = "";
		
		if(tokens.length == 1)
			appendedZeros = "0000";
		else if(tokens.length == 2)
			appendedZeros = "00";
		
		String rawDateString =  trimmed.replaceAll(DATE_SEPARATOR, "") + appendedZeros;
		
		try
		{
			long rawDate = Long.parseLong(rawDateString);
			return rawDate;
		}
		catch(NumberFormatException nfe)
		{
			throw new InvalidPublicationDateException(publicationDate);
		}
	}
	
}
