package com.copyright.ccc.web.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.util.StrutsTypeConverter;

import com.opensymphony.xwork2.conversion.TypeConversionException;

public class Struts2CustomDateConverter extends StrutsTypeConverter{

	@Override
	@SuppressWarnings("unchecked")
	public Object convertFromString(Map context, String[] values, Class toClass) {
		String enteredDate = values[0];
		if ( StringUtils.isNotEmpty( enteredDate) ) {
			if ( StringUtils.isNotEmpty(enteredDate.trim() ) ) {
				Date dValue = parseDate(enteredDate.trim());
				if ( dValue != null ) {
					return dValue;
				} else {
					throw new TypeConversionException("Date is not in mm/dd/yyyy format");
				}
			}
		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public String convertToString(Map context, Object o) {
		if ( o != null ) {
			if ( o instanceof Date ) {
				Date dValue = (Date)o;
				return formatMMddyyyyDateSlashed(dValue);
			}
		}
		return null;
	}

	private static final String[] DEFAULT_DATE_FORMATS = {"MM/dd/yyyy","M/dd/yyyy","M/d/yyyy","MM/d/yyyy"};
	
	private static Date parseDate(String input) {
		return parseDate(input, DEFAULT_DATE_FORMATS);
	}
	
	private static Date parseDate(String input, String[] formats)
    {
        Date parsedDate = null;

		if (input != null && formats !=null)
		{
	        for (int i=0; i<formats.length && parsedDate == null; i++)
	        {
	        	if (formats[i] != null)
	        	{
		            try {
						SimpleDateFormat sdf = new SimpleDateFormat(formats[i]);
						sdf.setLenient(false);
						parsedDate = sdf.parse(input);
					} catch (ParseException e) {
						continue;
					}
	        	}
	        }
		}
		
        return parsedDate;
    }
	private static String formatMMddyyyyDateSlashed(Date asDate ) {
    	return formatDate(asDate, DEFAULT_DATE_FORMATS[0] ); 
    }
    
	private static String formatDate(Date asDate, String formatToString ) {
     	if ( asDate != null ) {
            SimpleDateFormat format = new SimpleDateFormat(formatToString);
            return format.format(asDate);            
    	}
    	return "";
    }

}
