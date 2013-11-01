
function openHelpPopup( xPos, yPos, helpTitle, helpBody )
{
    var popup_pane = document.getElementById( "popup_pane" );
    
    if( popup_pane == null )
    {
        var popup_pane = document.createElement("div");
        popup_pane.id = "popup_pane";
        populatePopup( popup_pane, helpTitle, helpBody );
        
        document.body.appendChild( popup_pane );
    }
    else
    {
        document.getElementById("popup-title").innerHTML = helpTitle;
        document.getElementById("popup-body").innerHTML = helpBody;
    }
    
    with( popup_pane.style )
    { 
        display = "";
        position="absolute";
    }
   
    //need this to be called after appended to document otherwise it's style won't be defined yet
    setPopupPosition( popup_pane, xPos, yPos );
}

function populatePopup( popup_pane, helpTitle, helpBody )
{   
    var popup_wrapper = document.createElement("div");
    popup_wrapper.id = "popup_wrapper";
    
    var popup_inner = document.createElement("div");
    popup_inner.id = "popup_inner";
    
    var popup_content = document.createElement("div");
    popup_content.id = "popup";
    
    var help_title = document.createElement("h1");
    help_title.id = "popup-title";
    help_title.innerHTML = helpTitle;
    
    var help_body = document.createElement("div");
    help_body.id = "popup-body";
    help_body.innerHTML = helpBody;
    
    popup_content.appendChild( help_title );
    popup_content.appendChild( help_body );
    
    var clearer = document.createElement("div");
    clearer.className = "clearer";
    
    popup_inner.appendChild( popup_content );
    popup_inner.appendChild( clearer );
    
    popup_wrapper.appendChild( popup_inner );
    
    var popup_bottomcorners = document.createElement("div");
    popup_bottomcorners.id = "popup_bottomcorners";
    
    popup_pane.appendChild( popup_wrapper );
    popup_pane.appendChild( popup_bottomcorners );
}

function closeHelpPopup()
{
    document.getElementById( "popup_pane" ).style.display = "none";
}

function setPopupPosition( popup, xPos, yPos)
{ 
    var popupLeft = xPos + (window.scrollX || document.body.scrollLeft || document.body.parentNode.scrollLeft || 0 );
    var popupTop = yPos + (window.scrollY || document.body.scrollTop || document.body.parentNode.scrollTop || 0);
    
    //if the popup is past the page, then put it to the left of the mouse
    var pageWidth = document.body.offsetWidth;
    var popupWidth = popup.offsetWidth;
    if( popupLeft + popupWidth >= pageWidth )
        popupLeft = popupLeft - popupWidth;
    
    var pageHeight = document.body.offsetHeight;
    var popupHeight = popup.offsetHeight;
    if( popupTop + popupHeight >= pageHeight )
        popupTop = popupTop - popupHeight - 25;
            
    with( popup.style )
    {
        left = popupLeft + "px";
        top = popupTop + "px";
    }
}

function openHelpWindow( link )
{
    openPopup(link.href, link.target,"dependent=no,alwaysRaised=yes,menubar=no,personalbar=no,location=no,hotkeys,resizable=yes,height=390,width=430,scrollbars=yes,top=200,left=300");
}

//need setup the mouse overs here, after on load event, otherwise a div is created before the page loads which
//causes IE to throw an "operation aborted" bug
function setUpContextualHelpRollovers()
{
    var allLinks = document.links;
    
    for( i = 0; i < allLinks.length; i++ )
    {
        var contextualHelpRolloverClass = getContextualHelpRolloverClass( allLinks[i].className );
        if( contextualHelpRolloverClass.length > 0 )
        {
            allLinks[i].onmouseover = 
                function( e ){ 
                    if( !e ) e = event;
                    var contextualHelpClass = this.className;
                    openHelpPopup(e.clientX + 15, e.clientY + 15, getTitleText(contextualHelpClass), getBodyText(contextualHelpClass));
                }
            
            allLinks[i].onmouseout = closeHelpPopup;
        }
    }
}

function getTitleText( linkClass )
{
    var contextualHelpClass = getContextualHelpRolloverClass( linkClass );
    var rolloverId = getRolloverClassId( contextualHelpClass );
    return eval("contextualHelp_title_"+rolloverId);
}

function getBodyText( linkClass )
{
    var contextualHelpClass = getContextualHelpRolloverClass( linkClass );
    var rolloverId = getRolloverClassId( contextualHelpClass );
    return eval("contextualHelp_body_"+rolloverId);
}

function getRolloverClassId( contextualHelpClass )
{
    return contextualHelpClass.substring(24, contextualHelpClass.length);
}

function getContextualHelpRolloverClass( linkClass )
{
    var contextualHelpRolloverClass = "";
    
    var classNames = linkClass.split(" ");
    for( var i = 0; i < classNames.length; i++ )
    {
        if( classNames[i].indexOf("contextualHelp_rollover_") > -1 )
            return classNames[i];
    }
    
    return contextualHelpRolloverClass;
}

addOnLoadEvent( setUpContextualHelpRollovers );
