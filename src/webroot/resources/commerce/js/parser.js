//  ******************************************************************
//  This module is solely for parsing page ranges in our order forms.
//  It accepts a more-or-less free-formed string of page numbers and
//  ranges of pages in both numeric and roman numeral form.  There are
//  some rules to go along with this, of course:
//
//  Valid numeric characters are 0 1 2 3 4 5 6 7 8 9.
//  Valid roman numerals are I V X L C D M.
//  Roman numerals MUST be in a valid format.  For example, XVI is a
//  valid roman numeral, but IVX is not.
//  Ranges must be separated by a "-" (dash).
//  Ranges must be of the SAME numeral type (both numeric or both
//  roman, they cannot be interchanged).  1-5 is valid, 1-V is not.
//  Page numbers and page ranges can be separated by commas, semi-
//  colons or spaces.  1, 2 3 5, 5-6; i-v x xx   is valid.
//  Page ranges MUST have a valid start value and a valid end value.
//  Duplicate page values are not counter.  HOWEVER, I is not the same
//  as 1 and they will both be counted.
//  ******************************************************************

var validNumeric = "0123456789";
var validRoman = "IVXLCDM";
var validSeparators = " ,";
var validRangeIndicators = "-";

//  Clean of any spaces from the start and end of
//  a string.

function trim(str) {
    var len = str.length;
    var out = "";
    var startIdx = 0;
    var endIdx = 0;
    var i = 0;
    
    while (str.charAt(i) == " ") {
        i = i + 1;
    }
    startIdx = i;
    i = len - 1;
    while (str.charAt(i) == " ") {
        i = i - 1;
    }
    endIdx = i;
    return str.substring(startIdx, endIdx + 1);
}

//  This is not a very efficient method, but it does get
//  the job more or less done.  It sort of cleans up the
//  page range string and tries to make it more predictable
//  for the sake of parsing.  Don't mess with the order of
//  the "replace" calls...

function preScan(pageRanges) {
    pageRanges = trim(pageRanges);
    pageRanges = pageRanges.replace(/;/g, ",");
    pageRanges = pageRanges.replace(/  /g, " ");
    pageRanges = pageRanges.replace(/ -/g, "-");
    pageRanges = pageRanges.replace(/- /g, "-");
    pageRanges = pageRanges.replace(/--/g, "-");
    pageRanges = pageRanges.replace(/ ,/g, ",");
    pageRanges = pageRanges.replace(/, /g, ",");
    pageRanges = pageRanges.replace(/ /g, ",");
    pageRanges = pageRanges.replace(/,,/g, ",");
    return pageRanges;
}

function inItems(someChar, inString) {
    for (var i = 0; i < inString.length; i++) {
        if (someChar == inString.charAt(i)) return true;
    }
    return false;
}

function isNumeric(value) {
    var i = 0;
    
    for (var i = 0; i < value.length; i++) {
        if (!inItems(value.charAt(i), validNumeric)) return false;
    }
    return true;
}

//  Heh.  Just eliminate duplicates.

function uniquify(items) {
    var bucket = new Array();
    var duplicate = false;
    
    bucket[0] = items[0];
    
    for (var i = 0; i < items.length; i++) {
        duplicate = false;
        for (var j = 0; j < bucket.length; j++) {
            if (items[i] == bucket[j]) {
                duplicate = true;
            }
        }
        if (duplicate == false) bucket[bucket.length] = items[i];
    }
    return bucket;
}

//  At this point we should have a pretty clean series 
//  of pages and page ranges.  This method will break
//  them down and count them, but if the range is invalid
//  it will return a -1.

function parse(preScannedRanges) {
    var expanded = preScannedRanges.split(",");
    var range, start = 0, finish = 0;
    var items = new Array();
    var n = 0, m;

    //  JIRA CC-15, trailing separator causes miscount for page
    //  range[s].
    
    if (expanded[expanded.length - 1] == "" || expanded[expanded.length - 1] == null)
    {
        expanded.pop();
    }

    for (var i = 0; i < expanded.length; i++) {
        if (expanded[i].match(/-/)) {
            //  We have another range of values.  Split it
            //  out, see what kind of numerals we have, then
            //  expand it and process it.
            
            range = expanded[i].split("-");
            if (range[0] == "" || range[1] == "") return -1;
            
            if (isNaN(range[0])) {
                //  Assume we have a roman numeral.  Ranges must be of similar
                //  types (both numeric or both roman numeral).  If not we fail.
                
                if (isNumeric(range[1])) return -1;
                try {
                    start = getRomanValue(range[0]);
                    finish = getRomanValue(range[1]);
                    
                    if (start > finish) return -1;
                    
                    range = expandRomanRange(range[0], range[1]);
                    for (var j = 0; j < range.length; j++) {
                        items[n++] = range[j];
                    }
                }
                catch(err) {
                    return -1;
                }
            }
            else {
                if (isNaN(range[1])) return -1;
                try {
                    start = parseInt(range[0]);
                    finish = parseInt(range[1]);
                    
                    if (start > finish) return -1;
                    
                    range = expandNumericRange(start, finish);
                    for (var j = 0; j < range.length; j++) {
                        items[n++] = range[j];
                    }
                }
                catch(err) {
                    return -1;
                }
            }
        }
        else {
            if (isNaN(expanded[i])) {
                //  Roman?
                
                try {
                    m = getRomanValue(expanded[i]);
                    items[n++] = expanded[i];
                }
                catch(err) {
                    return -1;
                }
            }
            else {
                items[n++] = expanded[i];
            }
        }
    }
    items = uniquify(items);
    return items.length;
}

//  Call this function to parse a list of pages and page ranges
//  and return the final page count.

function getPageCount(pageRanges) {
    var cleaned = preScan(pageRanges.toUpperCase());
    return parse(cleaned);
}