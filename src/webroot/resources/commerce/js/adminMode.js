var ADMIN_MODE_YES = "adminMode=yes";

function performAdminModeCheckup(){
  
  if( isAdminMode() == false ) return;
  
  disableNonAdminElements();
  updateFormActions();
  updateLinkHrefs();
}

function isAdminMode(){
  
  if( typeof getCurrentURLParameter != 'function' ){
    alert( "getCurrentURLParameter() not found" );
    return false;
  }
  
  var isAdminMode = getCurrentURLParameter("adminMode") != null &&
                    getCurrentURLParameter("adminMode") == "yes";
  
  return isAdminMode;
}

function disableNonAdminElements(){
  
  if( isAdminMode() ){
    var navigationDiv = document.getElementById("navigation");
    var titlebarDiv = document.getElementById("titlebar");
    var cart_manageaccount_help_links = document.getElementById("cart_manageaccount_help_links"); 
    
    if( navigationDiv && titlebarDiv ){
      titlebarDiv.style.display="none";
      
      navigationDiv.style.visibility="hidden";
      navigationDiv.style.height = "0px";
          
    }
    
    if( cart_manageaccount_help_links ){
      cart_manageaccount_help_links.style.display = "none";
    }
  }
}

function hasQueryStringParams( URL ){
  if(!URL) return false;
  
  urlRegex = /^([^?]+)(\?)(.*)$/;
  
  return urlRegex.test( URL );
}

function addAdminModeParamToURL( url ){
  if(! url ) return null;
  
  var paramDelimiter = "?";
  
  if( hasQueryStringParams( url ) ) paramDelimiter = "&";
  
  return url + paramDelimiter + ADMIN_MODE_YES;
}

function updateElementDestinations( tagName, idsArray ){

  if( !tagName || !idsArray ) return;
  
  var isFormTag = ( /form/i ).test( tagName );
  var isATag = ( /a/i ).test( tagName );
  
  if( !isFormTag && !isATag ) return;
     
  var elements = document.getElementsByTagName( tagName );
  
  for( var i = 0; i < elements.length; i++ ){
    
    var element = elements[i];
    
    var elementID = element.id;
    
    //id property is not a string (e.g. a form has a field named "id"). Use the element's name in such cases.
    if( typeof element.id != "string" ) elementID = element.name;
    
    if ( isInArray( elementID, idsArray ) ){
      
      var hasQueryParams = false;
      
      if( isFormTag ) hasQueryParams = hasQueryStringParams( element.action );
      if( isATag ) hasQueryParams = hasQueryStringParams( element.href );
      
      if( hasQueryParams ){
        if( isFormTag ) element.action += ("&" + ADMIN_MODE_YES);
        if( isATag ) element.href += ("&" + ADMIN_MODE_YES);
      }else{
        if( isFormTag ) element.action += ("?" + ADMIN_MODE_YES);
        if( isATag ) element.href += ("?" + ADMIN_MODE_YES);
      }
      
    }
  }
}

function updateFormActions(){
  var formIDs = new Array("orderHistoryActionForm", 
                          "orderViewActionForm", 
                          "orderDetailActionForm"); 
  
  return updateElementDestinations( "form", formIDs );
}

function updateLinkHrefs(){
  var linkIDs = new Array("link_view_orders", 
                          "link_view_order_details", 
                          "link_back_to_view_orders", 
                          "~~startsWith~~link_order_detail_"); 
  
  return updateElementDestinations( "a", linkIDs );
}

function isInArray(text, array){

  if(!text || !array || !array.length ) return false;
  
  for(var i = 0; i < array.length; i++){
  
    var startsWithRegex = /^~~startsWith~~(.+)$/;
    var startsWithMatch = array[i].match( startsWithRegex );
    
    if ( startsWithMatch ){
      
      var startsWithStr = startsWithMatch[1];
      
      var reStr = "^" + startsWithStr + ".*$";
      
      if( new RegExp( reStr ).test( text ) ) return true;
      
    }else{
       if( array[i] == text ) return true;
    }
     
  }
  
  return false;
  
}

function isAdminURL(){
    
    var adminURLRegex = /^https?:\/\/[^\/]+\/cc2\/admin\/.*$/i;

    return adminURLRegex.test( window.location.href );
}