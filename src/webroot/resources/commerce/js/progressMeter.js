function growProgressBar( meterId, barId, stepSize, infinite ) {
    var bar = document.getElementById( barId );
    var meter = document.getElementById( meterId );

    if ( ! meter.maxWidth )
        meter.maxWidth = meter.offsetWidth;
    
    if ( ! bar.currWidth )
        bar.currWidth = parseInt(bar.offsetWidth);
    
    if ( bar.currWidth + stepSize > meter.maxWidth )
    {
        bar.currWidth += meter.maxWidth - bar.currWidth;
        bar.style.width = bar.currWidth + 'px';

        if ( infinite )
        {
            bar.currWidth = 0;
            bar.style.width = 0 + 'px';
            return true;
        }
        
        return false;
    }
    
    bar.currWidth += stepSize;
    bar.style.width = bar.currWidth + 'px';
    return true;
}

function startProgressBar( meterId, barId, stepSize, timeout, infinite )
{
    var keepGoing = growProgressBar( meterId, barId, stepSize, infinite );
    
    if ( keepGoing )
    {
        var callStr =  "startProgressBar( '" + meterId + "', '" + barId + "', " + stepSize + ", " + timeout + ", " + infinite + " );";

        setTimeout( callStr, timeout );
    }
}
