//  *****************************************************************
//  This module contains the code necessary to convert numeric values
//  to roman numerals and from roman numerals to numeric values.
//  Much of this code was taken directly from a Java module (GPL)
//  written by Maarten Bodewes, who developed the algorithm for fun
//  in his spare time.  I absconded with it and added a couple of
//  my own touches.
//  *****************************************************************

var romanDigits = "IVXLCDM";
var maxTenPower = (romanDigits.length - 1) / 2;
var maxDigit = 3 + ((romanDigits.length - 1) % 2) * 5;

function log(value, base) {
    return Math.log(value) / Math.log(base);
}

function getRomanDigitValue(romanDigit) {
    var romanDigitValue = 0;
    
    for (var i = 0; i < romanDigits.length; i++) {
        if (romanDigit == romanDigits.charAt(i)) {
            romanDigitValue = Math.round(Math.pow(10, Math.floor(i / 2)) * (((i % 2) == 1) ? 5 : 1));
            return romanDigitValue;
        }
    }
    throw("The digit " + romanDigit + " is not part of the Roman Numeral alphabet.");
}

function getRomanFromDigit(digit, tenPower) {
    var roman = "";
    var lowDigit, middleDigit, highDigit;
    
    if (tenPower > ((7 - 1) / 2)) {
        throw("The value is too high to be a roman numeral.");
    }
    else if (tenPower == maxTenPower) {
        if (digit > maxDigit) {
            throw("The value is too high to be a roman numeral.");
        }
    }
    lowDigit = romanDigits.charAt(tenPower * 2);
    
    switch(digit) {
        case 9: highDigit = romanDigits.charAt(tenPower * 2 + 2);
                roman = lowDigit + highDigit;
                break;
        case 8: roman = roman + lowDigit;
        case 7: roman = roman + lowDigit;
        case 6: roman = roman + lowDigit;
        case 5: middleDigit = romanDigits.charAt(tenPower * 2 + 1);
                roman = middleDigit + roman;
                break;
        case 4: middleDigit = romanDigits.charAt(tenPower * 2 + 1);
                roman = lowDigit + middleDigit;
                break;
        case 3: roman = roman + lowDigit;
        case 2: roman = roman + lowDigit;
        case 1: roman = roman + lowDigit;
        case 0: break;
    }
    return roman;
}

//  Get the roman numeral equivalent of an integer.

function getRoman(value) {
    var roman = "";
    var highestDigit = 0;
    var digit = 0;
    
    value = parseInt(value);
    
    if (value <= 0) {
        throw("The Romans did not have a numeric concept of zero or negative numbers.");
    }
    highestDigit = Math.floor(log(value, 10) + 0.01);
    for (var i = highestDigit; i >= 0; i--) {
        digit = Math.floor((value / Math.round(Math.pow(10, i))) % 10);
        roman = roman + getRomanFromDigit(digit, i);
    }
    return roman;
}

//  Get the integer value of a given roman numeral.

function getRomanValue(roman) {
    var romanValue = 0;
    var romanDigitValue = 0;
    var lastRomanDigitValue = 99999;
    
    roman = roman.toUpperCase();
    
    for (var i = 0; i < roman.length; i++) {
        romanDigitValue = getRomanDigitValue(roman.charAt(i));
        romanValue = romanValue + romanDigitValue;
        if (romanDigitValue > lastRomanDigitValue) {
            romanValue = romanValue - 2 * lastRomanDigitValue;
        }
        lastRomanDigitValue = romanDigitValue;
    }
    if (roman != getRoman(romanValue)) {
        throw("Badly formed Roman Numeral.");
    }
    return romanValue;
}

//  Take a starting value and an ending value and fill in the
//  missing bits...

function expandRomanRange(start, finish) {
    var a = getRomanValue(start);
    var b = getRomanValue(finish);
    var c = new Array(b - a + 1);
    
    for (var i = a; i <= b; i++) {
        c[i-a] = getRoman(i);
    }
    return c;
}

//  Same as above, but with numbers, not roman numerals.

function expandNumericRange(start, finish) {
    var c = new Array(finish - start + 1);
    
    for (var i = start; i <= finish; i++) {
        c[i-start] = i;
    }
    return c;
}