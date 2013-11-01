package com.copyright.ccc.web.actions.ordermgmt.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.util.DateFormatter;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;

/**
 * Utility methods for UI classes.
 * 
 * @author gcuevas
 *
 */
public class WebUtils {
	
	private static Logger sLogger = Logger.getLogger(WebUtils.class);

	public static final String DATE_AND_TIME_MASK = "MM/dd/yyyy hh:mm a";
	public static final String DATE_ONLY_MASK = "MM/dd/yyyy";
	
	/**
	 * Compares longs.  Longs can be null.
	 * @param long1
	 * @param long2
	 */
	public static boolean isEqualLongs(Long long1, Long long2)
	{
		if(long1 == null && long2 == null)
			return true;
		else if(long1 == null || long2 == null)
			return false;
		else
			return long1.equals(long2);
	}
	
	/**
	 * Converts longs to strings.  Longs can be null.
	 * 
	 * @param longToConvert
	 */
	public static String convertToString(Long longToConvert)
	{
		if(longToConvert == null)
			return null;
		else
			return longToConvert.toString();
	}
	
	/**
	 * Converts from string to long.  returns null if string is null.
	 * 
	 * @param stringToConvert
	 */
	public static Long convertToLong(String stringToConvert)
	{
		try
		{
			return new Long(StringUtils.trim(stringToConvert));
		}
		catch(NumberFormatException nfe)
		{
			return null;
		}
	}

	/**
	 * Converts Integers to strings.  Integers can be null.
	 * 
	 * @param integerToConvert
	 */
	public static String convertToString(Integer integerToConvert)
	{
		if(integerToConvert == null)
			return null;
		else
			return integerToConvert.toString();
	}
	
	/**
	 * Converts from string to Integer.  returns null if string is null.
	 * 
	 * @param stringToConvert
	 */
	public static Integer convertToInteger(String stringToConvert)
	{
		try
		{
			return new Integer(StringUtils.trim(stringToConvert));
		}
		catch(NumberFormatException nfe)
		{
			return null;
		}
	}
	/**
	 * Formats date into mm/dd/yyyy hh:mm a
	 * 
	 * @param date
	 */
	public static String formatDate(Date date)
	{
		return formatDate(date, DATE_AND_TIME_MASK);
	}
	
	/**
	 * Formats date into mm/dd/yyyy
	 * 
	 * @param date
	 */
	public static String formatDateOnly(Date date)
	{
		return formatDate(date, DATE_ONLY_MASK);
	}
	
	/**
	 * Formats date with given format.
	 * 
	 * @param date
	 * @param format
	 */
	public static String formatDate(Date date, String format)
	{
		if(date == null)
			return "";
		else
		{
			DateFormatter dateFormatter = new DateFormatter();
			dateFormatter.setFormat(format);
			dateFormatter.setDate(date);
			
			return dateFormatter.getFormattedDate();
		}
	}
	
	/**
	 * Formats dates into a date range with the given format.
	 * 
	 * @param startDate
	 * @param endDate
	 * @param dateFormat
	 */
	public static String formatDateRange(Date startDate, Date endDate, String dateFormat)
	{
		String start = formatDate(startDate, dateFormat);
		String end = formatDate(endDate, dateFormat);
		
		if(StringUtils.isEmpty(start) && StringUtils.isEmpty(end))
			return "";
		
		return start + " - " + end;
	}
	
	/**
	 * Determines if running the action's execute method.
	 */
	public static boolean isInExecuteMethod()
	{
		ActionInvocation invocation = ActionContext.getContext().getActionInvocation();
		
		String method = invocation.getProxy().getMethod();
		
		return StringUtils.equals("execute", method);
	}
	
	/** compares strings.  strings can include null values. ignores case**/
	public static int compareStrings(String string1, String string2)
	{
		String nonEmptyString1 = StringUtils.defaultString(string1);
		String nonEmptyString2 = StringUtils.defaultString(string2);
		
		return nonEmptyString1.compareToIgnoreCase(nonEmptyString2);
	}
	
	/** compares dates - nulls come first **/
	public static int compareDates(Date date1, Date date2)
	{
		if(date1 == null && date2 == null)
			return 0;
		else if(date1 == null)
			return -1;
		else if(date2 == null)
			return 1;
		else
			return date1.compareTo(date2);
	}
	
	/** compares longs **/
	public static int compareLongs(Long long1, Long long2)
	{
		if(long1 == null && long2 == null)
			return 0;
		else if(long1 == null)
			return 1;
		else if(long2 == null)
			return -1;
		else
			return long1.compareTo(long2);
		
	}
	
	/**
	 * Replaces single quotes with javascript escape single quotes.
	 * 
	 * @param stringToEscape
	 */
	public static String escapeForJavascript(String stringToEscape)
	{
		String escaped = "";
		if ( StringUtils.isNotEmpty(stringToEscape) ) {
			if ( stringToEscape.indexOf("'") != -1 ) {
				escaped = stringToEscape.replaceAll("'", "\\\\'");
			} else {
				return stringToEscape;
			}
		}
		return escaped;
	}
	
	/**
	 * Determines if list is empty.
	 * 
	 * @param list
	 */
	public static boolean isEmptyList(List<?> list)
	{
		return list == null || list.isEmpty();
	}
	
	/**
	 * Returns a date that is 1 day more than the date passed in
	 * @param date
	 */
	public static Date addDay(Date date)
	{
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1);
		return calendar.getTime();
	}
	
	public static Date getMidnightTonight() {
		//Tomorrow at 12:00:00.000 am
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 1);
		calendar.set( Calendar.HOUR_OF_DAY, 0 );
		calendar.set( Calendar.MINUTE, 0 );
		calendar.set( Calendar.SECOND, 0 );
		calendar.set( Calendar.MILLISECOND, 0 );
        return calendar.getTime();
	}

	public static int getDefaultPageSizeFromProperties( ResourceBundle properties, String sizeKey, String valueListKey, String actionName ) {
		
		String msgId = StringUtils.defaultString(actionName, "WebUtils") + ": getDefaultPageSizeFromProperties(): ";
		int defValue = 25;
		String msgUsing = ", using " + defValue;
		
		
		if ( properties == null ) {
			sLogger.warn(msgId + "No properties found" + msgUsing);
			return defValue;
		}
		
		if ( StringUtils.isEmpty( sizeKey ) ) {
			sLogger.warn(msgId + "No size key found" + msgUsing);
			return defValue;
		}
		
		String propSize = properties.getString( sizeKey );
		
		if ( StringUtils.isEmpty( propSize ) ) {
			sLogger.warn(msgId + "No default page size found for size key="+sizeKey + msgUsing);
			return defValue;
		}
		
		if ( !StringUtils.isNumeric( propSize ) ) {
			sLogger.error(msgId + "Non-numeric default page size="+propSize + msgUsing);
			return defValue;
		}
		
		int propertiesPageSize = (new Integer( propSize )).intValue();
		
		if ( StringUtils.isEmpty( valueListKey ) ) {
			sLogger.warn(msgId + "No values list key found, using input value= " + propertiesPageSize);
			return propertiesPageSize;
		}
		
		String sizeValues = properties.getString( valueListKey );

		if ( StringUtils.isEmpty( sizeValues ) ) {
			sLogger.warn(msgId + "No values found for values list key ="+valueListKey+", using input value= " + propertiesPageSize);
			return propertiesPageSize;
		}
		
		String[] values = sizeValues.split(",");
		
		for ( int i=0; i<values.length; i++) {
			if ( propSize.equals( values[i] ) ) {
				return propertiesPageSize;
			}
		}
		
		sLogger.error(msgId + "Unable to validate default page size="+propSize+" against value list="+sizeValues + msgUsing);
		return defValue;
				
	}
	
	public static boolean isAllDigits(String decimalString) {
		if (decimalString == null || decimalString.equals("")){
			return false;
		}
		char[] arrChar = decimalString.toCharArray();
		for (char character: arrChar){
			if (!Character.isDigit(character)) {
				return false;
			}
		}
		return true;
	}
	public static void fileCopy(File inputFile, File outputFile) throws IOException {
	    FileReader in;
	    FileWriter out;
    	in = new FileReader(inputFile);
    	out = new FileWriter(outputFile);
	    int c;

	    while ((c = in.read()) != -1)
	      out.write(c);

	    in.close();
	    out.close();
	}
}

