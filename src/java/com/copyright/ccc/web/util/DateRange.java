package com.copyright.ccc.web.util;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

/**
 * Class to hold a date range of two dates.  The date range can
 * be specified by Date objects or date  String of the form:
 * form MM/dd/yy or MM/dd/yyyy in SimpleDateFormat.  Internal 
 * date Strings will be converted to MM/dd/yyyy format. Note that
 * the DateRange object does not care about or enforce any constraints
 * on the temporal order of the dates. 
 */
public class DateRange 
{
	protected Logger _logger = Logger.getLogger( this.getClass() );
	private String dateStr1;
	private String dateStr2;
	private Date   date1;
	private Date   date2;

    /**
     * Public constructor using date Strings to define the date range
     * @input date1 is the first Date String
     * @input date2 is the second Date String
     */
	public DateRange(String d1, String d2)
	{
	   DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);
	   NumberFormat numFormat = NumberFormat.getInstance();
	   numFormat.setMaximumIntegerDigits(4);
	   format.setNumberFormat(numFormat);
	   
	   SimpleDateFormat sformat = new SimpleDateFormat("MM/dd/yyyy");
	   try {		   
		  this.date1 = format.parse(d1);
		  this.dateStr1 = sformat.format(date1);
		  this.date2 = format.parse(d2);
		  this.dateStr2 = sformat.format(date2);
	   } catch (Exception pe) {
	   	 // Log error here.  Unparsable date string   
   		  _logger.error( ExceptionUtils.getFullStackTrace(pe) );
	   }
	}
	
	public DateRange(Date d1, Date d2)
	{   
	   //DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);
	   this(d1,d2,"MM/dd/yyyy");
	}
	
	public DateRange(Date d1, Date d2, String strFormat)
	{
	   SimpleDateFormat sformat = new SimpleDateFormat(strFormat);
	   this.date1 = d1;
	   this.dateStr1 = sformat.format(d1);
	   this.date2 = d2;
	   this.dateStr2 = sformat.format(d2);
	}
	
	public Date getFirstDate() { return this.date1; }
	
	public Date getSecondDate() { return this.date2; }
	
	public String getFirstDateString() { return this.dateStr1; }
	
	public String getSecondDateString() { return this.dateStr2; }
	
	@Override
	public String toString()
	{
	   return this.dateStr1 + ":" + this.dateStr2;
	}
}