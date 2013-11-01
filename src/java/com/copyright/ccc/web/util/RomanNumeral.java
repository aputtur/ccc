package com.copyright.ccc.web.util;

import org.apache.log4j.Logger;

/****************
 * Much of this package was taken from Roman.java written by Maarten Bodewes.
 * He released all sources under the General GNU Public License.  I took the
 * bits that mattered to me and reincorporated them as I saw fit.
 ****************/

/*
 *  title:          RomanNumeral
 *  author:         Jessop
 *  date:           January 2008
 *
 *  description:    Simple class to handle the parsing of roman numerals.  See the
 *                  note above - the basic algorithm was taken from Maarten Bodewes.
 *                  I liked his algorithm (bits of it are clever) and it saved me
 *                  from having to write it myself.  :)
 */

public class RomanNumeral
{
	private static final Logger _logger = Logger.getLogger( RomanNumeral.class ); //
    //  Constants defining roman numerals.
    
    private static final char[] DIGITS = {'I', 'V', 'X', 'L', 'C', 'D', 'M'};
    private static final int MAX_POWER = (DIGITS.length - 1) / 2;
    private static final int MAX_DIGIT = 3 + ((DIGITS.length - 1) % 2) * 5;
    
    //  We'll hold onto the value as an intenger.
    
    private int _internal_representation = 0;
    
    //  We can construct a roman numeral from an int, Integer or String.
    
    public RomanNumeral(int value) {
        _internal_representation = value;
    }
    
    public RomanNumeral(Integer value) {
        if (value != null) {
            _internal_representation = value.intValue();
        }
    }
    
    public RomanNumeral(String value) throws Exception {
        _internal_representation = rtoi(value);
    }
    
    //  Public methods.
    
    @Override
    public String toString() {
        String value = "";
        
        try {
            value = itor(_internal_representation);
        }
        catch(Exception e) {
            value = "Invalid";
        }
        return value;
    }
    
    public int intValue() {
        return _internal_representation;
    }
    
    public int total(RomanNumeral upTo) throws Exception {
        if (_internal_representation > upTo.intValue()) {
            throw new Exception("Invalid page range.  Second value must be greater than the first.");
        }
        return upTo.intValue() - _internal_representation + 1;
    }
    
    //  Return an array of roman numerals.  Eg.  i-iii would return i, ii, iii.
    //  If our range is not valid (ie. the end number is smaller than the start
    //  number) we throw an exception.  I am not reverse parsing a range.  :P
    
    public String[] expand(RomanNumeral upTo) throws Exception {
        int len = upTo.intValue() - _internal_representation + 1;
        int i = 0;
        int n = _internal_representation;
        
        if (len < 1) throw new Exception("Invalid range.  Start value must be smaller than end value.");
        
        String[] arr = new String[len];
        
        while (i < len) {
            arr[i] = itor(n);
            n += 1;
            i += 1;
        }
        return arr;
    }
    
    //  Private methods.
    
    //  This method is the work horse of this class.  It takes a digit and its place (power)
    //  and converts it to the appropriate roman numeral string.
    
    private String getRomanNumeralFromDigit(int value, int power) throws Exception {
        String romanNumeral = "";
        
        if (power > MAX_POWER) {
            throw new Exception("Illegal value for a Roman Numeral.");
        }
        else if (power == MAX_POWER) {
            if (value > MAX_DIGIT) {
                throw new Exception("Illegal value for a Roman Numeral.");
            }
        }
        
        //  The base digit is the primary value we will be working with.  This
        //  is necessary because of how numbers like 2, 3 and 4 (and others,
        //  obviously) are formed.
        
        char baseDigit = DIGITS[power * 2];
        char middleDigit;
        char upperDigit;
        
        switch(value) {
            case 9:
                upperDigit = DIGITS[power * 2 + 2];
                romanNumeral = "" + baseDigit + upperDigit;
                break;
            case 8: romanNumeral += baseDigit;
            case 7: romanNumeral += baseDigit;
            case 6: romanNumeral += baseDigit;
            case 5:
                middleDigit = DIGITS[power * 2 + 1];
                romanNumeral = "" + middleDigit + romanNumeral;
                break;
            case 4:
                middleDigit = DIGITS[power * 2 + 1];
                romanNumeral = "" + baseDigit + middleDigit;
                break;
            case 3: romanNumeral += baseDigit;
            case 2: romanNumeral += baseDigit;
            case 1: romanNumeral += baseDigit;
            case 0: break;
        }
        return romanNumeral;
    }
    
    //  Alright, I know the name is obtuse, but I had to do it as a throwback
    //  to the old days of programming in C.  This method converts an integer
    //  into a Roman Numeral String.
    
    private String itor(int value) throws Exception {
        if (value <= 0) {
            throw new Exception("Sorry, the Romans had no concept of 0 or negative numbers.");
        }
        StringBuffer romanNumeralBuff = new StringBuffer();

        
        int digit = 0;
        int highestDigit = (int) Math.floor(log(value,10) + 0.01);
        
        //  Gotta loop backwards through the digits in the number
        //  that was provided.
        
        for (int i = highestDigit; i >= 0; i--) {
            digit = (value / (int) Math.round(Math.pow(10,i))) % 10;
            romanNumeralBuff.append(getRomanNumeralFromDigit(digit, i));
        }
        return romanNumeralBuff.toString();
    }
    
    //  Trick used to calculate number of digits in an integer value.
    
    private double log(double value, double base) {
        return Math.log(value) / Math.log(base);
    }
    
    //  Translate a roman digit to an integer value.
    
    private int getRomanDigitValue(char digit) throws Exception {
        int value = 0;
        
        for (int i = 0; i < DIGITS.length; i++) {
            if (digit == DIGITS[i]) {
                value = (int) Math.round(Math.pow(10,(i / 2)) * (((i % 2) != 0) ? 5 : 1));
                return value;
            }
        }
        throw new Exception("The digit " + digit + " is not a Roman Numeral.");
    }
    
    //  Translate a roman numeral to an integer value.
    
    private int rtoi(String romanNumeral) throws Exception {
        int value = 0;
        int digitValue = 0;
        int lastDigitValue = 999999;
        
        romanNumeral = romanNumeral.toUpperCase();
        
        for (int i = 0; i < romanNumeral.length(); i++) {
            digitValue = getRomanDigitValue(romanNumeral.charAt(i));
            value += digitValue;
            if (digitValue > lastDigitValue) {
                value = value - 2 * lastDigitValue;
            }
            lastDigitValue = digitValue;
        }
        
        //  The trick to this is to compare the value we got with the
        //  roman numeral passed into us.  :)  This confirms that it was
        //  a well formed number.  I love it!
        
        if (!itor(value).equals(romanNumeral)) {
            throw new Exception(romanNumeral + " is a badly formed Roman Numeral and is therefore invalid.");
        }
        return value;
    }
    
    public static void selfTest()
    {
        RomanNumeral a = null;
        String s = "cii";
        int n = 27;
        
        try {
           a = new RomanNumeral(s);
        }
        catch(Exception e) {
            //
        }        
        RomanNumeral b = new RomanNumeral(n);
        
        if (a != null)
        	_logger.info(a.toString());
        _logger.info(b.toString());
    }
}