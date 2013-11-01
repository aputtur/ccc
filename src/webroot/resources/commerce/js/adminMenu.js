var disappeardelay=250  //menu disappear speed onMouseout (in miliseconds)
var enableanchorlink=0 //Enable or disable the anchor link when clicked on? (1=e, 0=d)
var hidemenu_onclick=1 //hide menu when user clicks within menu? (1=yes, 0=no)

/////No further editting needed

var ie5=document.all
var ns6=document.getElementById&&!document.all

var isIEUserAgent = navigator.userAgent.indexOf("MSIE") > -1;

function getposOffset(what, offsettype){
  var totaloffset=(offsettype=="left")? what.offsetLeft : what.offsetTop;
  var parentEl=what.offsetParent;
  while (parentEl!=null){
    totaloffset=(offsettype=="left")? totaloffset+parentEl.offsetLeft : totaloffset+parentEl.offsetTop;
    parentEl=parentEl.offsetParent;
  }
  return totaloffset;
}

function showhide(obj, e, visible, hidden){
  if (ie5||ns6)
    dropmenuobj.style.left=dropmenuobj.style.top=-500
  if (e.type=="click" && obj.visibility==hidden || e.type=="mouseover")
    obj.visibility=visible
  else if (e.type=="click")
    obj.visibility=hidden
}

function iecompattest(){
  return (document.compatMode && document.compatMode!="BackCompat")? document.documentElement : document.body
}

function clearbrowseredge(obj, whichedge){
  var edgeoffset=0
  if (whichedge=="rightedge"){
  var windowedge=ie5 && !window.opera? iecompattest().scrollLeft+iecompattest().clientWidth-15 : window.pageXOffset+window.innerWidth-15
  dropmenuobj.contentmeasure=dropmenuobj.offsetWidth
  if (windowedge-dropmenuobj.x < dropmenuobj.contentmeasure)
    edgeoffset=dropmenuobj.contentmeasure-obj.offsetWidth
  }
  else{
    var topedge=ie5 && !window.opera? iecompattest().scrollTop : window.pageYOffset
    var windowedge=ie5 && !window.opera? iecompattest().scrollTop+iecompattest().clientHeight-15 : window.pageYOffset+window.innerHeight-18
    dropmenuobj.contentmeasure=dropmenuobj.offsetHeight
    if (windowedge-dropmenuobj.y < dropmenuobj.contentmeasure){ //move up?
      edgeoffset=dropmenuobj.contentmeasure+obj.offsetHeight
    if ((dropmenuobj.y-topedge)<dropmenuobj.contentmeasure) //up no good either?
      edgeoffset=dropmenuobj.y+obj.offsetHeight-topedge
  }
  }
  return edgeoffset
}

function dropdownmenu(obj, e, dropmenuID){
  elem = obj;
  if (window.event) event.cancelBubble=true
  else if (e.stopPropagation) e.stopPropagation()
  if (typeof dropmenuobj!="undefined") //hide previous menu
  dropmenuobj.style.visibility="hidden"
  clearhidemenu()
  if (ie5||ns6){
  obj.onmouseout= delayhidemenu
    dropmenuobj=document.getElementById(dropmenuID)
    if (hidemenu_onclick) dropmenuobj.onclick=function(){dropmenuobj.style.visibility='hidden'}
    dropmenuobj.onmouseover=clearhidemenu
    dropmenuobj.onmouseout=ie5? function(){ dynamichide(event)} : function(event){ dynamichide(event)}
    showhide(dropmenuobj.style, e, "visible", "hidden")
    dropmenuobj.x=getposOffset(obj, "left")
    dropmenuobj.y=getposOffset(obj, "top")
    var extraOffsetLeft = ( isIEUserAgent == true )? 1 : 0;
    dropmenuobj.style.left=dropmenuobj.x-clearbrowseredge(obj, "rightedge")+extraOffsetLeft+"px"
    var extraOffsetHeight = ( isIEUserAgent == true )? 4 : 0;
    dropmenuobj.style.top=dropmenuobj.y-clearbrowseredge(obj, "bottomedge")+obj.offsetHeight+extraOffsetHeight+"px"
  }
  
  /* Code added to overcome <select> overlapping issue in IE - See below */
  if( isIEUserAgent == true ) checkForSelectOverlaping( dropmenuobj, 'helperIFrame' );
  
  return clickreturnvalue()
}

function clickreturnvalue(){
  if ((ie5||ns6) && !enableanchorlink) return false
  else return true
}

function contains_ns6(a, b) {
  while (b.parentNode)
  if ((b = b.parentNode) == a)
  return true;
  return false;
}

function dynamichide(e){
  if (ie5&&!dropmenuobj.contains(e.toElement))
    delayhidemenu()
  else if (ns6&&e.currentTarget!= e.relatedTarget&& !contains_ns6(e.currentTarget, e.relatedTarget))
    delayhidemenu()
}

function delayhidemenu(){ /* Code added to overcome <select> overlapping issue in IE - See below */
  
  delayhide=setTimeout("dropmenuobj.style.visibility='hidden'; if(ie5) hideIFrameUnder('helperIFrame'); doOnMouseOut(elem);",disappeardelay);
}

function clearhidemenu(){
  if (typeof delayhide!="undefined")
    clearTimeout(delayhide);
  
}

/*
* Added on 04-19-2006 to allow animation of menu cells.
*/

function inactivateMenuCells(elem){
  var cells = elem.getElementsByTagName("td");
  
  for( var i = 0; i < cells.length; i++ ){
    if( cells[i].className == 'menuCellActive' ) cells[i].className = 'menuCellInactive';
  }
}

 function doOnMouseOut(elem){
    elem.className = 'menuCellInactive';
  }
  
  function doOnMouseOver(elem){
    inactivateMenuCells( elem.parentNode );
    elem.className = 'menuCellActive';
  }

/*
 * Added on 05/01/2006 to solve bug related overlapping of <select> HTML tags with other elements in IE.
 *
 * Uses code in util.js
 *
 * Problem Description (from http://www.codetoad.com/forum/20_22736.asp):
 *    SELECT lists do not obey the normal z-index stacking order.
 *    Nothing can be placed "on top of" a SELECT list unless it is another SELECT list with a higher z-index.
 *    Other elements are always rendered below SELECT lists, even if they are given a higher z-index value than the SELECT
 */
 
 function checkForSelectOverlaping( menuElem, iFrameId ){
	
  var selects = document.getElementsByTagName("select");
	var i, currentSelect;
  var isOverlap = false;

	for ( i=0; i < selects.length; i++ ){
		currentSelect = selects[i];
		
		if( elementsOverlap( currentSelect , menuElem ) ){
      isOverlap = true;	
      break;
    }
    
	}
	
  if ( isOverlap ) {
    showIFrameUnder( menuElem, iFrameId );
  }
  else{
    hideIFrameUnder( iFrameId );
  }

}


function showIFrameUnder( elem, iFrameId ){

	var helperIFrame = document.getElementById( iFrameId );
	
  if( helperIFrame ){
  
      var coordsElem = getElementCoords( elem );
    
      var elemTop = coordsElem[0];
      var elemRight = coordsElem[1];
      var elemBottom = coordsElem[2];
      var elemLeft = coordsElem[3];
        
      helperIFrame.style.top = elemTop;
      helperIFrame.style.left = elemLeft;
      helperIFrame.style.width = elemRight - elemLeft;
      helperIFrame.style.height = elemBottom - elemTop;
      
      helperIFrame.style.visibility = 'visible';
  
  }
	 
}


function hideIFrameUnder( iFrameId ){
	
  var helperIFrame = document.getElementById( iFrameId );
	
  if( helperIFrame ) helperIFrame.style.visibility = 'hidden';

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


function elementsOverlapById( idElem1, idElem2 ){
	var elem1 = document.getElementById( idElem1 );
	var elem2 = document.getElementById( idElem2 );
	
	return elementsOverlap(elem1, elem2);
}


function elementsOverlap( elem1, elem2 ){

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