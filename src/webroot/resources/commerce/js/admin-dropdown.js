
var am_VertOffset = isIE ? 4 : 0;
var am_HorzOffset = isIE ? 1 : 0;

var am_cursorPosition = {
    x:0,
    y:0
};

var activeMenu = null;

function cursorInBounds( bounds ) {
    var cx = am_cursorPosition.x;
    var cy = am_cursorPosition.y;
    
    if ( cx >= bounds.left && cx <= bounds.right && cy >= ( bounds.top - 4 ) && cy <= bounds.bottom )
        return true;
        
    return false;
}


function updateCursorPosition(e) {
  am_cursorPosition.x = (window.Event) ? e.pageX : event.clientX;
  am_cursorPosition.y = (window.Event) ? e.pageY : event.clientY;
}

if (window.Event) {
    document.captureEvents(Event.MOUSEMOVE);
}

document.onmousemove = updateCursorPosition;

function am_getElementLeft( eElement ) {
    var nLeftPos = eElement.offsetLeft;
    var eParElement = eElement.offsetParent;
    
    while (eParElement != null)
    {
        nLeftPos += eParElement.offsetLeft;
        eParElement = eParElement.offsetParent;
    }
    
    return nLeftPos;
}

function am_getElementTop( eElement ) {
    var nTopPos = eElement.offsetTop;
    var eParElement = eElement.offsetParent; 
    
    while (eParElement != null)
    {
        nTopPos += eParElement.offsetTop;
        eParElement = eParElement.offsetParent;
    }
    
    return nTopPos;
}

function am_getElementCoords( elem ) {
    elemTop = am_getElementTop( elem );
    elemLeft = am_getElementLeft( elem );
    
    elemBottom = elemTop + elem.offsetHeight;
    elemRight = elemLeft + elem.offsetWidth;
    
    return { top:elemTop, right:elemRight, bottom:elemBottom, left:elemLeft };
}

function am_toggleVisibility() {
    if ( this.style.display != "none" )
        this.style.display = "none";
    else
        this.style.display = "";
}

function am_isVisible() {
    return this.style.display != "none";
}


function toggleAdminMenu( source ) {
    var menu = document.getElementById( "adminMenu_" + this.id );

    if ( menu  == null )
        return;

    menu.style.top = ( am_getElementCoords( this ).bottom + am_VertOffset ) + 'px';
    menu.style.left = ( am_getElementCoords( this ).left + am_HorzOffset ) + 'px';
    
    if ( menu != activeMenu ) {
        if ( activeMenu != null )
            activeMenu.toggleVisibility();
        activeMenu = menu;
        
        this.className = "adminMenuBarItemOver";
        menu.toggleVisibility();
    }
}

function adminMenuBarItemOut( source ) {
    this.className = "adminMenuover";
}

function adminMenuItemOver( source ) {
    this.className = "adminMenuItemOver";
}

function adminMenuItemOut( source ) {
    this.className = "adminMenuItem";
}

function defineAdminMenu() {
    var menuBarItems = document.getElementById("adminMenuBar").getElementsByTagName("td");
    
    for( var i = 0; i < menuBarItems.length; i++ ) {
        var menuBarItem = menuBarItems[i];

        if ( menuBarItem.id == "" )
            continue;
        
        var menuID = "adminMenu_" + menuBarItem.id;
        
        var menu = document.getElementById( menuID );

        if ( ! menu )
            continue;

        menu.style.display = "none"
        menu.toggleVisibility = am_toggleVisibility;
        menu.isVisible = am_isVisible;
        var menuItems = menu.getElementsByTagName("div");
        
        if ( menuItems == null )
            continue;
            
        for ( var j = 0; j < menuItems.length; j++ ) {
            menuItems[j].onmouseover = adminMenuItemOver;
            menuItems[j].onmouseout = adminMenuItemOut;
        }

        menuBarItem.onmouseover = toggleAdminMenu;
        menuBarItem.onmouseout = adminMenuBarItemOut;
    }
    
    var adminMenuBar = document.getElementById("adminMenuBar");

    adminMenuBar.onmouseout = function() {

        if ( activeMenu != null )
        {
            var mb = am_getElementCoords( activeMenu );

            if ( ! cursorInBounds( mb ) && activeMenu.isVisible() )
            {
                activeMenu.toggleVisibility();
                activeMenu = null;
            }
        }
    };
}
