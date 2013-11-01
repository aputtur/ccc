package com.copyright.ccc.web;

import java.util.Date;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

import com.copyright.workbench.time.DateUtils2;

public class DateConverter implements Converter
{
    private final static String[] ACCEPTED_FORMATS =
    { "MM/dd/yyyy", "M/dd/yyyy", "M/d/yyyy", "MM/yyyy", "M/yyyy", "yyyy" };
    
    @SuppressWarnings("unchecked") // must annotate method since Class arg cannot be Class<?>
	public Object convert(Class type, Object value)
    {
        if (value == null) 
            throw new ConversionException("No value specified");
        
        if(value instanceof Date)
            return value;
        else if (value instanceof String)
        {
            String dateString = String.valueOf(value);
            Date date = DateUtils2.parseDate( dateString, ACCEPTED_FORMATS );
            return date;
        }
        else throw new ConversionException( value + "cannot be converted to java.util.Date" ); //TODO: gcuevas 11/8/06: make a better exception string
    }
    
   
}
