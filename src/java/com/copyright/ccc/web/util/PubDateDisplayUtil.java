package com.copyright.ccc.web.util;

import java.util.Calendar;
import java.util.Date;

import com.copyright.workbench.time.DateUtils2;

 /**
  * Helper class that formats publication date ranges into a string suitable for
  * display in a UI. Originally developed for the publicationSearch.jsp in Rightsphere
  */
 public class PubDateDisplayUtil 
 {
  /* Notes
   * This will change when rights dates are factored in...
   * Should be a taglib?
   */
   
   /* types of publication years */
     private static final int UNKNOWN_YEAR = 0;
     private static final int VALID_YEAR = 1;
     private static final int BOUND_YEAR = 2;
     private static final int EQUAL_YEARS = 3;
     
     /* the array that maps year types to display formats. */
     private static String[][] displayFormat = new String[4][4];

     /* load map of year types to correct display formats */
     static
     {
         displayFormat[UNKNOWN_YEAR][UNKNOWN_YEAR] = "-";
         displayFormat[UNKNOWN_YEAR][VALID_YEAR] = "Through <e>";
         displayFormat[UNKNOWN_YEAR][BOUND_YEAR] = "Through present";
         //displayFormat[VALID_YEAR][UNKNOWN_YEAR] = "<b> - present";
          displayFormat[VALID_YEAR][UNKNOWN_YEAR] = "<b>";
         displayFormat[VALID_YEAR][VALID_YEAR] = "<b> - <e>";
         displayFormat[VALID_YEAR][BOUND_YEAR] = "<b> - present";
         displayFormat[BOUND_YEAR][UNKNOWN_YEAR] = "Through present";
         displayFormat[BOUND_YEAR][VALID_YEAR] = "Through <e>";
         displayFormat[BOUND_YEAR][BOUND_YEAR] = "Through present";
         displayFormat[EQUAL_YEARS][EQUAL_YEARS] = "<b>";      
     }  
     
  
     /*
      * helper method that translates a year into its year type
      */
     private static int calcYearType(int year) 
     {
         if ((year == 0)) return UNKNOWN_YEAR;
         if ((year == 3000) || (year == 1000)) return BOUND_YEAR;
         if ((year > 0)) return VALID_YEAR;
         
         return 0;
     }
     
     /**
      * given two publication dates, returns a string representing
      * the range
      * @return a publication year range formatted for display
      * @param endDate
      * @param beginDate
      */
     public static String computeYearRangeDisplay(Date beginDate, Date endDate) 
     {
     
         /*
          * look up the year type for the begin and end year
          */
         int beginYear = parseYear(beginDate);
         int endYear = parseYear(endDate);
         int beginYearType = calcYearType(  beginYear );
         int endYearType = calcYearType( endYear );
         
         /*
          * handle special case in which both years are valid AND they are equal
          */
         if ((beginYearType == VALID_YEAR && endYearType == VALID_YEAR) && (beginYear == endYear))
         {
             beginYearType = EQUAL_YEARS;
             endYearType = EQUAL_YEARS;
         }
         
         /*
          * pull the correct format for these year types
          */
         String dispFmt = displayFormat[beginYearType][endYearType];

         /*
          * replace the place holders with the years.
          */
         return dispFmt.replaceAll("<b>",Integer.toString(beginYear)).replaceAll("<e>",Integer.toString( endYear ));
     }
     
     /**
      * accepts a Date, returns it's four digit year as an int
      * @return the 4 digit year
      * @param fullDate
      */
     public static int parseYear(Date fullDate) 
     {
         if (fullDate == null) 
         {
             return 0;
         }
         
         return DateUtils2.getYear(fullDate.getTime());
     }
     
     public static Date parseDate(String yyyyMMDD){
         if (yyyyMMDD == null || yyyyMMDD.equals("")) {
             return null;
         }
         Calendar calendar = Calendar.getInstance();
         int year = Integer.parseInt(yyyyMMDD.substring(0,4));
         int month = Integer.parseInt(yyyyMMDD.substring(4,6));
         month = month>0&&month<13?month:1;
         int day = Integer.parseInt(yyyyMMDD.substring(6));
         day = day>0&&day<32?day:1;
         calendar.set(year,month,day);
         return calendar.getTime();
     }
     
     public static Date parseDate(Long yyyyMMDD) {
         if (yyyyMMDD == null)
         {
            return null;
         }
         return parseDate(yyyyMMDD.toString());
     }

  }
