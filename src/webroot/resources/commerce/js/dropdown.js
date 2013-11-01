var ua = navigator.userAgent.toLowerCase();
        isIE = ((ua.indexOf("msie") != -1) && (ua.indexOf("opera") == -1) && (ua.indexOf("webtv") == -1));

var menu_debug = false;



function getByID( id, doc )
{

    if (menu_debug == true) {
        setTimeout("throw Error('GetByID: Enter')", 0);
    }
    if ( doc == null ) doc = 'document';

    if (eval(doc).getElementById) {
    // Newer browsers
    return eval(doc).getElementById(id);
    } else if (eval(doc).all) {
    // IE4
    return eval(doc).all[id];
    } else if (eval(doc).layers) {
    // NS4
    return eval(doc).layers[id];
    } else {
    alert( "We couldn't recognize your browser.  Please use a different one or upgrade to the newest version." );
    window.close();
    }
    return "";
}

var menuopening='';

function mouseover( id )
{
    if (menu_debug == true) {
        setTimeout("throw Error('mouseover: Enter')", 0);
    }
    if ( menuopening != '') {
        //setTimeout("throw Error('Clearing: TImeOut1')", 0);
        clearTimeout(menuopening)
    }

    if (getByID( id ).style.display == '') {
        mouseoverhelper ( id )
    } else {
        menuopening=setTimeout('mouseoverhelper("'+id+'")', 250);
    }
}

function mouseoverhelper ( id ) {
        if (menu_debug == true) {
        setTimeout("throw Error('mouseoverhelper: Enter')", 0);
        }
        menuopening='';

    var elem = getByID( id );

    elem.style.display="";

    displayHelperIFrame( elem );
}

function mouseover2( obj )
{
    if (menu_debug == true) {
    setTimeout("throw Error('mouseover2: Enter')", 0);
    }

    var objs = obj.getElementsByTagName("table");
    for (var j=0; j < objs.length; j++) {
        if (objs[j].className='menu') {
            mouseover(objs[j].id);
    }
    }
}

function mouseout2( obj )
{   if (menu_debug == true) {
    setTimeout("throw Error('mouseout2: Enter')", 0);
    }

    var objs = obj.getElementsByTagName("table");
    for (var j=0; j < objs.length; j++) {
        if (objs[j].className='menu') {
      mouseout(objs[j].id);
        }
    }
}


function mouseout( id )
{
    if (menu_debug == true) {
    setTimeout("throw Error('mouseout: Enter')", 0);
    }

    if ( menuopening != '') {
            //setTimeout("throw Error('Clearing: TImeOut2')", 0);
        clearTimeout(menuopening)
    }
    menuopening='';
  getByID( id ).style.display="none";

  hideHelperIFrame();
}

function definemenu( )
{
    if (menu_debug == true) {
    setTimeout("throw Error('definemenu: Enter')", 0);
    }
    var tds=getByID("menu").getElementsByTagName("td");
    for(var i=0;i< tds.length;i++) {
        var a_td=tds[i];
        if (a_td.className=="menuover") {
            var menus=a_td.getElementsByTagName("table");

            for(var j=0;j< menus.length;j++) {
                var a_menu=menus[j];

                if (a_menu.className=="menu") {
                    a_menu.onmouseover=function () {mouseoverhelper(this.id)};
                    a_menu.onmouseout=function () {mouseout(this.id)};
                    var cells=a_menu.getElementsByTagName("td");

                    for(var k=0;k< cells.length;k++) {
                        var a_cell = cells[k];
                        a_cell.onmouseover=function() {this.style.backgroundColor="#0F367B"; this.style.color="#FFF";}
                        a_cell.onmouseout=function() {this.style.backgroundColor=""; this.style.color="";}
                    }
                    if(!isIE) {
                        a_td.onmouseover= new Function("mouseover('" + a_menu.id + "')");
                        a_td.onmouseout= new Function("mouseout('" + a_menu.id + "')");
                    } else {
                        a_td.onmouseover = function() {mouseover2(this)};
                        a_td.onmouseout = function() {mouseout2(this)};


                    }
                    a_td.style.postion='relative';

                }

            }
        }
    }
}

function openInMinimalWindow(theURL) {
  openPopup(theURL,"media_window","height=200, width=600, resizeable=yes");
}

function MM_openBrWindow(theURL,winName,features) {//v2.0
  openPopup(theURL,winName,features);
  }


function getHelperIFrame(){
  return document.getElementById("helperIFrame");
}

/* Code for solving IE6 bug related to z-Index and SELECT lists - BEGIN */
function getElementCoords( elem )
{

    elemTop = getElementTop( elem );
    elemLeft = getElementLeft( elem );

    elemBottom = elemTop + elem.offsetHeight;
    elemRight = elemLeft + elem.offsetWidth;

    return [ elemTop, elemRight, elemBottom, elemLeft ];
}


function getElementCoordsById ( elemId )
{

  elem = document.getElementById( elemId );

  return getElementCoords( elem );

}


function elementsOverlapById(idElem1, idElem2){
    var elem1 = document.getElementById( idElem1 );
    var elem2 = document.getElementById( idElem2 );

    return elementsOverlap(elem1, elem2);
}


function elementsOverlap(elem1, elem2){

    var coordsElem1 = getElementCoords( elem1 );
    var coordsElem2 = getElementCoords( elem2 );

    var elem1Top = coordsElem1[0];
    var elem1Right = coordsElem1[1];
    var elem1Bottom = coordsElem1[2];
    var elem1Left = coordsElem1[3];

    var elem2Top = coordsElem2[0];
    var elem2Right = coordsElem2[1];
    var elem2Bottom = coordsElem2[2];
    var elem2Left = coordsElem2[3];

    return ( isInRange(elem1Top, elem2Top, elem2Bottom)
           || isInRange(elem1Bottom, elem2Top, elem2Bottom) )
           && ( isInRange(elem1Left, elem2Left, elem2Right)
           || isInRange(elem1Right, elem2Left, elem2Right)
           || ( ( elem1Left <= elem2Left ) && ( elem1Right >= elem2Right ) && (elem1Bottom > elem2Top)  ) );

}

function isInRange(value, rangeBegin, rangeEnd){
    return ( value >= rangeBegin ) && ( value <= rangeEnd  );
}



function getElementLeft( eElement )
{
    var nLeftPos = eElement.offsetLeft;
    var eParElement = eElement.offsetParent;

    while (eParElement != null)
    {
        nLeftPos += eParElement.offsetLeft;
        eParElement = eParElement.offsetParent;
    }

    return nLeftPos;
}


function getElementRight( elem )
{
    elemLeft = getElementLeft( elem );
    elemRight = elemLeft + elem.offsetWidth;

    return elemRight;
}


function getElementTop( eElement )
{
    var nTopPos = eElement.offsetTop;
    var eParElement = eElement.offsetParent;

    while (eParElement != null)
    {
        nTopPos += eParElement.offsetTop;
        eParElement = eParElement.offsetParent;
    }

    return nTopPos;
}

function getElementBottom( elem )
{
    elemTop = getElementTop( elem );
    elemBottom = elemTop + elem.offsetHeight;

    return elemBottom;
}

function displayHelperIFrame( underneathOfElement ){
  var helperIFrame = getHelperIFrame();

  var areThereOverlappingSelects = false;

  var selects = document.getElementsByTagName("select");

  if( selects )
  {
    for( var i = 0; i < selects.length; i++ ){
      if( elementsOverlap( selects[i], underneathOfElement ) ){
        areThereOverlappingSelects = true;
        break;
      }
    }
  }


  if( isIE6() == true && helperIFrame && areThereOverlappingSelects ){
    helperIFrame.style.width =  underneathOfElement.width +  "px";
    helperIFrame.style.height = ( getElementBottom( underneathOfElement ) -  getElementTop( underneathOfElement ) ) + "px"; //( getElementBottom( underneathOfElement ) -  getElementTop( underneathOfElement ) )
    helperIFrame.style.top = getElementTop( underneathOfElement ) + "px";
    helperIFrame.style.left = getElementLeft( underneathOfElement ) + "px";

    helperIFrame.style.display = "";
  }

}


function hideHelperIFrame(){
  var helperIFrame = getHelperIFrame();

  if( isIE6() == true && helperIFrame ){
    helperIFrame.style.display = "none";
  }
}


function isIE6(){
  return navigator.userAgent.indexOf("MSIE 6.0") > -1;
}



/* Code for solving IE6 bug related to z-Index and SELECT lists - END */