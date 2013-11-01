function isDateSixMonthsPrior( date )
{
    var dateRegexp = new RegExp("^.*/(\\d{4})$");

    var matched = dateRegexp.exec( date );
    
    if( matched == null || matched.length != 2 )
        return false;
    
    else
    {
        var year = matched[1];
        var currentDate = new Date();
        var inputDate = new Date(date);
        var sixMonthsPrior = new Date();
        sixMonthsPrior.setMonth(sixMonthsPrior.getMonth()-6);
        return (sixMonthsPrior <= inputDate);
    }
}
function isDateTenMonthsLater( date )
{
    //  Any date GREATER than 10 months from the current
    //  date is no good.

    var dateRegexp = new RegExp("^.*/(\\d{4})$");

    var matched = dateRegexp.exec( date );
    
    if( matched == null || matched.length != 2 )
    {
        return false;
    }
    else
    {
        var year = matched[1];
        var currentDate = new Date();
        var inputDate = new Date( date );
        var futureDate = new Date();
        futureDate.setMonth( futureDate.getMonth() + 10 );
        return (futureDate > inputDate);
    }
}
function isDatePriorToToday( date )
{
    var dateRegexp = new RegExp("^.*/(\\d{4})$");

    var matched = dateRegexp.exec( date );
    
    if( matched == null || matched.length != 2 )
        return false;
    
    else
    {
        var year = matched[1];
        var currentDate = new Date();
        var inputDate = new Date(date);

        return (currentDate > inputDate);
    }
}
//assumes date in MM/yy/dddd
function isDateInCurrentOrFutureYear( date )
{
    var dateRegexp = new RegExp("^.*/(\\d{4})$");

    var matched = dateRegexp.exec( date );
    
    if( matched == null || matched.length != 2 )
        return false;
    
    else
    {
        var year = matched[1];
        var currentYear = (new Date()).getFullYear();
        return (year >= currentYear);
    }
}

//assumes date in MM/yy/dddd
function isMonthAndDayOnOrAfterToday( date )
{
    var dateRegexp = new RegExp("^(\\d{1,2})/(\\d{1,2})/(\\d{4})$");
    
    var matched = dateRegexp.exec(date);
    
    if( matched == null || matched.length != 4 )
        return false;
    
    else
    {   
        var today = new Date();
        
        var year = matched[3];
        var yearToday = today.getFullYear();
        
        if( year < yearToday )
            return false;
        else if( year > yearToday )
            return true;
        else
        {
            var month = matched[1];
            var currentMonth = today.getMonth() + 1;
        
            if(month < currentMonth)
                return false;
            else if( month == currentMonth )
            {
                var day = matched[2];
                if( day < today.getDate() )
                    return false;
                else return true;
            }
            else return true;
        }
    }
}

/* A valid ccDate is a date in the format MM/dd/yyyy */
function isValidCCDate( date, strict )
{
    var dateRegexp = 
        strict ? new RegExp("^(\\d{2})/(\\d{2})/(\\d{4})$") : new RegExp("^(\\d{1,2})/(\\d{1,2})/(\\d{4})$")
            
    var matched = dateRegexp.exec(date);
            
    if( matched == null || matched.length != 4)
        return false;
    else
    {
        return isValidDate( matched[2], matched[1], matched[3] );
    }
}

function isValidDate(day, month, year) {
    if (month < 1 || month > 12) {
        return false;
    }
    if (day < 1 || day > 31) {
        return false;
    }
    if ((month == 4 || month == 6 || month == 9 || month == 11) &&
        (day == 31)) {
        return false;
    }
    if (month == 2) {
        var leap = (year % 4 == 0 &&
           (year % 100 != 0 || year % 400 == 0));
        if (day>29 || (day == 29 && !leap)) {
            return false;
        }
    }
    return true;
}
