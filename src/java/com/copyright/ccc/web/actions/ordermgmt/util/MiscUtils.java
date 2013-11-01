package com.copyright.ccc.web.actions.ordermgmt.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.copyright.base.svc.ServiceRuntimeException;
import com.copyright.workbench.time.DateUtils2;

public class MiscUtils {
	
	private static final Logger mLogger = Logger.getLogger(MiscUtils.class);
	
	public static Date getUpdateValidBeginDate()  {
		//12/01/3000 at 12:00:00.000 am
		Calendar calendar = Calendar.getInstance();
		calendar.set( Calendar.HOUR_OF_DAY, 0 );
		calendar.set( Calendar.MINUTE, 0 );
		calendar.set( Calendar.SECOND, 0 );
		calendar.set( Calendar.MILLISECOND, 0 );
		calendar.set( Calendar.DATE , 1);
		calendar.set( Calendar.MONTH , Calendar.DECEMBER);
		calendar.set( Calendar.YEAR , 3001);
        return calendar.getTime();
	}
	public static Date getUpdateValidEndDate()  {
		//12/31/3001 at 12:00:00.000 am
		Calendar calendar = Calendar.getInstance();
		calendar.set( Calendar.HOUR_OF_DAY, 0 );
		calendar.set( Calendar.MINUTE, 0 );
		calendar.set( Calendar.SECOND, 0 );
		calendar.set( Calendar.MILLISECOND, 0 );
		calendar.set( Calendar.DATE , 31);
		calendar.set( Calendar.MONTH , Calendar.DECEMBER);
		calendar.set( Calendar.YEAR , 3001);
        return calendar.getTime();
	}
	
	public static Date getDefaultValidBeginDate()  {
		return getBeginningOfTime();
	}
	
	public static Date getDefaultValidEndDate() {
		return getEndOfTime();
	}
	public static Date getPromoteValidEndDate() {
		return getEndOfTime();
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
	public static Date getNow() {
		//Now at current time
		Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
	}

	public static Date getEndOfTime() {
		//12/31/3000 at 12:00:00.000 am
		Calendar calendar = Calendar.getInstance();
		calendar.set( Calendar.HOUR_OF_DAY, 0 );
		calendar.set( Calendar.MINUTE, 0 );
		calendar.set( Calendar.SECOND, 0 );
		calendar.set( Calendar.MILLISECOND, 0 );
		calendar.set( Calendar.DATE , 31);
		calendar.set( Calendar.MONTH , Calendar.DECEMBER);
		calendar.set( Calendar.YEAR , 3000);
        return calendar.getTime();
	}
	
	public static Date getBeginningOfTime() {
		//1/1/1000 at 12:00:00.000 am
		Calendar calendar = Calendar.getInstance();
		calendar.set( Calendar.HOUR_OF_DAY, 0 );
		calendar.set( Calendar.MINUTE, 0 );
		calendar.set( Calendar.SECOND, 0 );
		calendar.set( Calendar.MILLISECOND, 0 );
		calendar.set( Calendar.DATE , 1);
		calendar.set( Calendar.MONTH , Calendar.JANUARY);
		calendar.set( Calendar.YEAR , 1000);
        return calendar.getTime();
	}
		
	private static String[] supportPropertyPatternKeys = {
				 "class"
				,"deleteStmtId"
				,"insertStmtId"
				,"queryAllStmtId"
				,"queryByPKStmtId"
				,"queryAllStmtId"
				,"queryByPKStmtId"
				,"updateStmtId"

			};
			
		private static boolean keepValue( String value ) {
			if ( StringUtils.isNotEmpty( value) ) {
				for ( String sk : supportPropertyPatternKeys) {
					if ( value.trim().equalsIgnoreCase(sk) ) {
						return false;
					}
				}
			} else {
				return false;
			}
			return true;
	}
		
	public static String stripSupportProperties( String inToStringValue ) {
		StringBuffer buf = new StringBuffer();
		
		String[] props = inToStringValue.split(",");
		int fCount = 0;
		for ( String prop : props ) {
			String[] pvals = prop.split("=");
			boolean isClass = false;
			if ( pvals.length > 0 ) {
				isClass = pvals[0].equalsIgnoreCase(supportPropertyPatternKeys[0]);
			}
			if ( isClass ) {
				String cName = pvals[1].substring( pvals[1].lastIndexOf(".") + 1) ;
				buf.append(cName+"[");
			} else {
				if ( pvals.length > 0 ) {
					if ( keepValue(pvals[0]) ) {
						buf.append((fCount>0?",":"")).append(prop.trim());
						fCount++;
					}
				}
			}
		}
		buf.append("] ");
	
	return buf.toString();
    }

	public static final int CHUNK_SIZE = 8000;
    public static final String DOT = ".";

	/**
	 * copyFile will copy the input to the output.
	 */
	public static void copyFile (long fileSize , InputStream input, OutputStream output) throws Exception {

        byte[] buf = new byte[CHUNK_SIZE];

        long sizeRead = 0;
        long total = fileSize;
        int num = 0;

        try {
		  while (sizeRead < total) {
			          num = input.read(buf);
			          sizeRead += num;
			          output.write(buf, 0, num);
		  }
		} catch (Exception e) {
			throw new ServiceRuntimeException(e);
		}
    }
	
	/**
	 * 
	 * @param filename
	 */
	public static OutputStream openFileOutputStream(String filename) {
		BufferedOutputStream fs;
		try {
			FileOutputStream ffs = new FileOutputStream(filename);
			fs = new BufferedOutputStream(ffs);
		} catch (IOException ioe) {
			throw new ServiceRuntimeException(ioe);
		}		
		return fs;
	}
	
	/**
	 * 
	 * @param file
	 */
	public static InputStream openFileInputStream(File file) {
		BufferedInputStream fs;
		try {
			FileInputStream ffs = new FileInputStream(file);
			fs = new BufferedInputStream(ffs);
		} catch (IOException ioe) {
			throw new ServiceRuntimeException(ioe);
		}		
		return fs;
	}
	
	public static File createDirectoryIfNotFound(String directoryPath) {
		File dir = new File(directoryPath);
		if (! dir.exists() ) {
			boolean ret = dir.mkdirs();
			if (ret == false)
			{
				mLogger.warn("Unable to create Directory : " + directoryPath);
			}
		}
		return dir;
	}

	public static String stripNonFileCharacters( String inString ) {
		String outString = inString;
		if ( outString != null ) {
			String nothing = "";
			String[] badChars = {"\\\\","/"," ","\\*","\\?","<",">","\\|","\\&"};
			for ( String badChar : badChars ) {
				outString = outString.replaceAll(badChar,nothing);
			}
		}
		return outString;
	}
	public static String getTimeStamp() {
        return DateUtils2.now("yyyyMMddHHmmssSSS");
	}


	/**
	 * parses input date string based on input list of formats
	 * will return null if the input string fails to match any
	 * of the formats.
	 */
    public static Date parseDate(String input, String[] formats)
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
    
    public static String formatDate(String datestring, String parseFromFormat, String formatToString ) {
    	Date asDate = MiscUtils.parseDate(datestring, new String[] {parseFromFormat});
    	if ( asDate != null ) {
            SimpleDateFormat format = new SimpleDateFormat(formatToString);
            return format.format(asDate);            
    	}
    	return "";
    }
    
    public static String formatMMddyyyyDateSlashed(Date asDate ) {
    	return formatDate(asDate, "MM/dd/yyyy" ); 
    }
    public static String formatMMddyyDateSlashed(Date asDate ) {
    	return formatDate(asDate, "MM/dd/yy" ); 
    }
    
    public static String formatDate(Date asDate, String formatToString ) {
     	if ( asDate != null ) {
            SimpleDateFormat format = new SimpleDateFormat(formatToString);
            return format.format(asDate);            
    	}
    	return "";
    }

}
