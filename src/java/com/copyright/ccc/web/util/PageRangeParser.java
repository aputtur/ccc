package com.copyright.ccc.web.util;

import java.util.ArrayList;
import java.util.List;

import com.copyright.ccc.CCException;

/*
 *  title:          PageRangeParser
 *  author:         Jessop
 *  date:           January 2008
 *
 *  description:    This module accepts a string that (should) represent a list
 *                  of page numbers and/or ranges of page numbers.  Each page number
 *                  or page range can be separated by a ";", "," or a " ".  The only
 *                  valid range indicator is the "-".  Roman numerals are also valid.
 *                  It will also try to clean up the list of pages in the case of some
 *                  duplicated separators, indicators and such.  It also tries to eliminate
 *                  extra spaces in advance of the parsing to make life easier for me.
 *
 *  examples:       This is a valid range    => 1, 2 3 5, 5-9, i-iii x xl
 *                  This is an invalid range => 1, 2, 3i, 7-1, x-v (you get the picture).
 */

public class PageRangeParser
{
    private static final String VALID_NUMERIC = "0123456789";
    private static final String VALID_ROMAN = "IVXLCDM";
    private static final String VALID_SEPARATORS = " ,";
    private static final String VALID_RANGE_INDICATORS = "-";
    
    private String   _raw = null;
    private List<String> _parsed = null;
    private boolean  _valid;
    
    //  Constructor...  takes the string straight from the
    //  form field.
    
    public PageRangeParser(String pageRanges) {
        _raw = preScan(pageRanges.toUpperCase());
        _valid = validate(_raw);
        
        if (_valid) {
            Status status = parseRanges(_raw);
        
            _valid = status.isValid();
            _parsed = status.getPages();
        }
    }
    
    //  Is our string of pages and page ranges valid?
    
    public boolean isValid() {
        return _valid;
    }

    //  By this time our range field should be all parsed and
    //  nicely tucked into a list.  We just need to return the
    //  size of the list to get a real page count.

    public int pageCount() throws CCException {
        if (_parsed != null) {
            return _parsed.size();
        }
        else {
            throw new CCException("The page range field was not successfully parsed.");
        }
    }
    
    //  This workhorse of a method parses out our ranges.  It also validates
    //  that ranges are in the proper order and that the roman numerals provided
    //  (if any) are valid.  If any criteria fails, the routine sets the result
    //  status (which is the first item in a List) to FALSE and exits prematurely.
    
    private Status parseRanges(String pageRanges) {
        String[] firstPass;
        String[] range;
        String[] secondPass;
        String item;
        Boolean valid = Boolean.TRUE;
        int i = 0;
        int j = 0;
        
        List<String> pages = new ArrayList<String>();
        firstPass = pageRanges.split(",");
        
        while (i < firstPass.length) {
            item = firstPass[i];
            
            if (item.indexOf("-") == -1) {
                //  We do not have a range, just a "single value" that
                //  we need to extract and stuff into the list.
                
                if (VALID_ROMAN.indexOf(item.charAt(0)) != -1) {
                    //  Confirm this is a valid Roman Numeral.
                    
                    try {
                        RomanNumeral a = new RomanNumeral(item);
                        item = a.toString();
                    }
                    catch(Exception e) {
                        valid = Boolean.FALSE;
                        break;
                    }
                }
                pages.add(item);
            }
            else {
                //  We had a range indicator so we must have a range of
                //  numbers.  Extract both numbers in the range.
                
                range = item.split("-");
                j = 0;
                
                if (VALID_ROMAN.indexOf(range[0]) != -1) {
                    //  Get both roman numerals (they should both be roman and
                    //  have already been successfully scanned at the prescan)
                    //  and calculate the values in the range.
                    
                    try {
                        RomanNumeral a = new RomanNumeral(range[0]);
                        RomanNumeral b = new RomanNumeral(range[1]);
                        secondPass = a.expand(b);
                    }
                    catch(Exception e) {
                        valid = Boolean.FALSE;
                        break;
                    }
                }
                else {
                    //  We have plain old integers.  Parse them out into a
                    //  range of values.
                    
                    try {
                        secondPass = expand(range[0], range[1]);
                    }
                    catch (CCException e) {
                        valid = Boolean.FALSE;
                        break;
                    }
                }
                //  Add our freshly parsed out range of values into our
                //  list.
                
                while (j < secondPass.length) {
                    pages.add(secondPass[j]);
                    j += 1;
                }
            }
            
            i += 1;
        }
        return new Status( valid, pages );
    }
    
    //  Little helper method to expand a range of integers.
    
    private String[] expand(String startAt, String endAt) throws CCException {
        int start = Integer.parseInt(startAt);
        int end = Integer.parseInt(endAt);
        int len = end - start + 1;
        int n = start;
        
        if (end > start) throw new CCException("Invalid page range.  Start value must be smaller than end value.");
        
        String[] arr = new String[len];
        
        for (int i = 0; i < len; i++) {
            arr[i] = String.valueOf(n++);
        }
        return arr;
    }
    
    //  Scan the string to make sure we have a well-formed
    //  set of pages and/or page ranges.
    
    private boolean validate(String s) {
        char a, b, c;
        int i = 0;
        
        String valid_chars = VALID_NUMERIC + VALID_ROMAN + VALID_SEPARATORS + VALID_RANGE_INDICATORS;
        
        if (s.startsWith("-") || s.startsWith(",")) return false;
        if (s.endsWith("-") || s.endsWith(",")) return false;
        
        while (i < s.length()) {
            a = s.charAt(i);
            
            if (valid_chars.indexOf(a) == -1) return false;
            
            if (i < (s.length() - 1)) {
                b = s.charAt(i + 1);
                
                if (i < (s.length() - 2)) {
                    c = s.charAt(i + 2);
                }
                else {
                    c = (char) 0;
                }
                if (!validSequence(a, b, c)) return false;
            }
            
            i += 1;
        }
        
        return true;
    }
    
    //  O.K.  This is a very ugly method.  I just want to
    //  replace certain combinations of characters to clean
    //  up the string.  This is a costly method with all sorts
    //  of string allocation...  sorry!
    
    private String preScan(String pageRanges) {
        pageRanges = pageRanges.trim();
        pageRanges = pageRanges.replaceAll(";", ",");
        pageRanges = pageRanges.replaceAll("  ", " ");
        pageRanges = pageRanges.replaceAll(" -", "-");
        pageRanges = pageRanges.replaceAll("- ", "-");
        pageRanges = pageRanges.replaceAll("--", "-");
        pageRanges = pageRanges.replaceAll(" ,", ",");
        pageRanges = pageRanges.replaceAll(", ", ",");
        pageRanges = pageRanges.replaceAll(",,", ",");
        return pageRanges;
    }
    
    //  Scan several characters to determine whether or not their combination in
    //  a page range string, or group of ranges, is valid.  This function REALLY looks
    //  for invalid sequences only.  Invalid includes numerics combined with roman in
    //  sequence or in a range.  You also do not want two range indicators together.
    //  Not overly concerned with extra range separators...
    
    private boolean validSequence(char a, char b, char c) {
        if ((VALID_RANGE_INDICATORS.indexOf(a) != -1) && (VALID_RANGE_INDICATORS.indexOf(b) != -1)) return false;
        if ((VALID_NUMERIC.indexOf(a) != -1) && (VALID_ROMAN.indexOf(b) != -1)) return false;
        if ((VALID_ROMAN.indexOf(a) != -1) && (VALID_NUMERIC.indexOf(b) != -1)) return false;
        if (c != (char) 0) {
            if (((VALID_ROMAN.indexOf(a) != -1) && (VALID_RANGE_INDICATORS.indexOf(b) != -1)) && (VALID_NUMERIC.indexOf(c) != -1)) return false;
            if (((VALID_NUMERIC.indexOf(a) != -1) && (VALID_RANGE_INDICATORS.indexOf(b) != -1)) && (VALID_ROMAN.indexOf(c) != -1)) return false;
            if ((VALID_RANGE_INDICATORS.indexOf(a) != -1) && (VALID_RANGE_INDICATORS.indexOf(c) != -1)) return false;
        }
        return true;
    }
    
    //  This doesn't do anything.  I just think it is a cute little
    //  piece of code.
    
    private boolean in(char left, String right) {
        for (int i = 0; i < right.length(); i++) {
            if (left == right.charAt(i)) return true;
        }
        return false;
    }
    
    private static class Status
    {
    	private Boolean mValid;
    	private List<String> mPages;
    	
    	private Status( Boolean valid, List<String> pages)
    	{
    		mValid = valid;
    		mPages = pages;
    	}
    	
		public Boolean isValid() { return mValid; }
		public List<String> getPages() { return mPages; }
    }
}