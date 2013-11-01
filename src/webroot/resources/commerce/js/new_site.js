
function createCookie(name, value, days) 
{
    if (days) 
    {
        var date = new Date();
        date.setTime(date.getTime()+(days*24*60*60*1000));
        var expires = "; expires="+date.toGMTString();
    }
    else var expires = "";
    document.cookie = name+"="+value+expires+"; path=/";
}

function readCookie(name) 
{
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for(var i=0;i < ca.length;i++) 
    {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1,c.length);
        if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
    }
    return null;
}

function eraseCookie(name) 
{
    createCookie(name,"",-1);
}

//	These functions check/set our popup cookie.  The cookie just tells us whether
//	or not a user has been to our site, so we know whether or not to display the
//	popup window.

function been_here_before()
{
    return (readCookie("CC.POPUPALERT") != null);
}

function popup_begone()
{
    createCookie("CC.POPUPALERT", "Do not show popup.", 90);
}

//	This is the "main function".  Call this to display (or not) the popup
//	that hooks the user up with our pretty new site tutorial.

function popup_new_site_message()
{
    if (!been_here_before())
    {
	window_handle = window.open("/popup.html", "CCCPopup", "height=300, width=600" );
	popup_begone();
    }
}
