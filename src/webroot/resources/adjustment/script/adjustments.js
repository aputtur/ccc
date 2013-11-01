var ALLOW_DECIMAL_POINT = true;
var DO_NOT_ALLOW_DECIMAL_POINT = false;
var COLOR_FIELD_DISABLED = "#C0C0C0";
var COLOR_FIELD_ENABLED = "#FFFFFF";
var NUMBER_OF_DECIMALS = 2;

function numbersOnly(e, elem, acceptDot)
{
	var key;
	var keychar;
	
	if (window.event)
		key = window.event.keyCode;
	else if (e)
		key = e.which;
	else
		return true;
	
	keychar = String.fromCharCode(key);

	elem.focus();
	
	var mask = "0123456789-";
	
	if (acceptDot && acceptDot == true) mask = "0123456789-.";
	
	var isNumber = mask.indexOf(keychar) > -1;
					
	var isBackspace = key == 8;
  var isTab = key == 9;
  var isRightArrow = key == 39;
  var isLeftArrow = key == 37;
  var isHome = key == 36;
  var isEnd = key == 35;
	
  var isAllowedKey = isNumber || isBackspace || isTab || isRightArrow || isLeftArrow || isHome || isEnd;
  
	if ( isAllowedKey ){
		return true;
	}
			
	return false;
}


function isNumeric( sText )
{
  var numericRegex = /^-?[0-9]*\.?[0-9]+$/;
  
  return numericRegex.test( sText );
 
}


function formatNumber( value ){
					
	if ( isNumeric( value ) ){
		return new Number(value).toFixed( NUMBER_OF_DECIMALS );
	}
	
	return value;
}


function decimalFormat(value){
	document.write( formatNumber( value ) );
}


function formatDecimalInputs( className ){
	var inputs = document.getElementsByTagName("input");
	
	for( var i = 0; i < inputs.length; i++ ){
		var input = inputs[i];
		var isTextInput = input.type == "text";
    var SPACE = " ";
		var applyDecimalFormatting =  input.className.indexOf( SPACE + className ) > -1 ||
                                  input.className.indexOf( className + SPACE ) > -1 ||
                                  input.className.indexOf( SPACE + className + SPACE ) > -1 || 
                                  input.className == className;
		
		if( isTextInput && applyDecimalFormatting ) input.value = formatNumber(input.value);
	}
}

function organizePageLayout(){
  
  var navigationDiv = document.getElementById( "navigation" );
  var titlebarDiv = document.getElementById( "titlebar" );
  var wrapperDiv = document.getElementById( "wrapper" );
  var bottomcornersDiv = document.getElementById( "bottomcorners" );
  
  var allDivsFound = navigationDiv &&
                     titlebarDiv &&
                     wrapperDiv && 
                     bottomcornersDiv;
                               
                       
  if( allDivsFound ){
    navigationDiv.style.visibility = "hidden";
    navigationDiv.style.height = "0px";
    
    titlebarDiv.style.display = "none";
    
    bottomcornersDiv.style.visibility = "hidden";
    
    wrapperDiv.style.width = "880px";
    wrapperDiv.style.borderBottom = "1px solid #c0c0c0";
  }
 
 
}

//Last visited anchor - BEGIN

  
function setLastVisitedAnchor( newLastVisitedAnchor ){
  if( typeof lastVisitedAnchor != "undefined" ) lastVisitedAnchor = newLastVisitedAnchor;
}

function scrollToLastVisitedAnchor(){

  //Go to errors section, if there are any.
  var errorsSection = document.getElementById( "errors" );
  var errorsSectionPresent = errorsSection && 
                             (/div/i).test( errorsSection.tagName );
  if( errorsSectionPresent ){
    errorsSection.scrollIntoView(); 
    return;
  }

  var lastVisitedAnchorID = getCurrentURLParameter( "scrollTo" );
  
  if( lastVisitedAnchorID != null ){
    var newLastVisitedAnchor = document.getElementById( lastVisitedAnchorID );
    if( newLastVisitedAnchor ) newLastVisitedAnchor.scrollIntoView();
  }

}

function addScrollToURLParameter( URL ){
  
  if( !lastVisitedAnchor || lastVisitedAnchor == null ) return URL;
  
  var urlParamDelimiter = "?";
  
  if( hasQueryStringParams( URL ) ) urlParamDelimiter = "&";
  
  var updatedURL = URL + urlParamDelimiter + "scrollTo=" + lastVisitedAnchor;
  
  return updatedURL;
}

function hasQueryStringParams( URL ){
  
  if( !URL ) return false;
  
  urlRegex = /^([^?]+)(\?)(.*)$/;
  
  return urlRegex.test( URL );
}

//Last visited anchor - END

//Refresh and Reset - BEGIN

function resetAdjustmentValues( elementID ){
  var element = document.getElementById( elementID );
  
  if( element ){
    var inputs = element.getElementsByTagName( "input" );
    
    for( i = 0; i < inputs.length; i++ ){
      var currentInput = inputs[i];
      
      var adjustmentInputRegex = /^(.*\s)?adjustmentInput(\s.*)?$/;
      
      var isAdjustmentInput = currentInput.type == "text" &&
                              adjustmentInputRegex.test( currentInput.className );
      
      if ( isAdjustmentInput == true ){
        
        var value = new Number(0);
        
        var decimalFormatRegex = /^(.*\s)?decimalFormat(\s.*)?$/;
        var isDecimalFormat = decimalFormatRegex.test( currentInput.className );
        
        if( isDecimalFormat ) value = value.toFixed( NUMBER_OF_DECIMALS );
        
        currentInput.value = value;
      
        enableElement( currentInput );
        
      }
    }
    
    var selects = element.getElementsByTagName( "select" );
    
     for( i = 0; i < selects.length; i++ ){
      var currentSelect = selects[i];
      currentSelect.selectedIndex = 0;
    }
  }
}


function initExclusiveQtyAndFeeGroups(){

  var currentAdjustmentRows = getCurrentAdjustmentRows();
  
  for( var i = 0; i < currentAdjustmentRows.length; i++ ){
   
    var currentAdjustmentRow = currentAdjustmentRows[i];
    var qtyInputs = getQtyAndFeeInputs( currentAdjustmentRow )["qty"];
    var feeInputs = getQtyAndFeeInputs( currentAdjustmentRow )["fee"];
        
    for( var q = 0; q < qtyInputs.length; q++ ){
      var currentInput = qtyInputs[q];
      
      if ( checkTagName( currentInput, "input" ) ){
      
        currentInput.onkeydown = function(){
                                  if( this.readOnly == true ) return;
                                  var myContainer = this.parentNode.parentNode.parentNode; //tr element
                                  var myFeeInputs = getQtyAndFeeInputs( myContainer )["fee"];
                                  disableElements( myFeeInputs );
                                  zeroTextElements( myFeeInputs );
                                 };
      }
      
      if ( checkTagName( currentInput, "select" ) ){//Duration for net products is a select element
      
        currentInput.onchange = function(){
                                  var myContainer = this.parentNode.parentNode.parentNode; // tr > td > span > input
                                  var myFeeInputs = getQtyAndFeeInputs( myContainer )["fee"];
                                  disableElements( myFeeInputs );
                                  zeroTextElements( myFeeInputs );
                                 };
      }
         
      
    }
    
    for( var q = 0; q < feeInputs.length; q++ ){
      var currentInput = feeInputs[q];
              
        currentInput.onkeydown = function(){
                                    if( this.readOnly == true ) return;
                                    var myContainer = this.parentNode.parentNode.parentNode; // tr > td > span > input
                                    var myQtyInputs = getQtyAndFeeInputs( myContainer )["qty"];
                                    disableElements( myQtyInputs );
                                    zeroTextElements( myQtyInputs );
                                 };
      
    }
     
  }
  
}

function getQtyAndFeeInputs( containerElement ){
    
    if(!containerElement) alert("getQtyAndFeeInputs: containerElement not provided");
    
    var qtyInputs = new Array();
    var feeInputs = new Array();
    
    var allSpans = containerElement.getElementsByTagName( "span" );
    
    for( var j = 0; j < allSpans.length; j++ ){
      var currentSpan = allSpans[j];
      var isQtySpan = currentSpan.id == "qty";
      var isFeeSpan = currentSpan.id == "fee";
      
      if( isQtySpan ) qtyInputs.push( currentSpan.firstChild );
      if( isFeeSpan ) feeInputs.push( currentSpan.firstChild );
    }
    
    var inputs = new Array();
    inputs["qty"] = qtyInputs;
    inputs["fee"] = feeInputs;
    
    return inputs;
}

function getCurrentAdjustmentRows(){
  var currentAdjustmentRows = new Array();
  var allRows = document.getElementsByTagName("tr");
  
  for( var i = 0; i < allRows.length; i++ ){
    var currentRow = allRows[i];
    var isCurrentAdjustmentRow = currentRow.id.indexOf("currentAdjustmentRow_") == 0;
    if( isCurrentAdjustmentRow ) currentAdjustmentRows.push( currentRow );
  }
  
  return currentAdjustmentRows;
}

function disableElements( elements ){
   for( var i = 0; i < elements.length; i++ ){
    disableElement( elements[i] );
   }
}

function disableElement( element ){
  if(!element) return;
  element.readOnly = true;
  element.style.backgroundColor = COLOR_FIELD_DISABLED;
}

function enableElement( element ){
  if(!element) return;
  element.readOnly = false;
  element.style.backgroundColor = COLOR_FIELD_ENABLED;
}

function zeroTextElements( elements ){
  
  if(!elements) alert("zeroTextElements: no elements provided.")
  
  for( i = 0; i < elements.length; i++ ){
      
      var currentElement = elements[i];
      
      var isInput = checkTagName( currentElement, "input") &&
                    currentElement.type == "text";
                         
      
      if ( isInput == true ){
        var value = new Number(0);
        
        var decimalFormatRegex = /^(.*\s)?decimalFormat(\s.*)?$/;
        var isDecimalFormatInput = decimalFormatRegex.test( currentElement.className );
        
        if( isDecimalFormatInput ) value = value.toFixed( NUMBER_OF_DECIMALS );
        
        currentElement.value = value;
      }
      
      var isSelectElement = checkTagName( currentElement, "select");
      
      if( isSelectElement ) currentElement.selectedIndex = 0;
      
    }
}

function checkTagName(element, tagName){
  if(!element || !tagName) return false;
  var tagNameRegex = new RegExp("^\s*" + tagName + "\s*$", "i" );
  return tagNameRegex.test( element.tagName );
}


//Refresh and Reset - END


//Global Adjustment - BEGIN

function handleFieldToAdjustChange(){
          
          var fieldToAdjustDropDown = document.getElementById("fieldToAdjustDropDown");
          
          if(fieldToAdjustDropDown){
            
            var durationSelected = fieldToAdjustDropDown.value == "Duration";//"<%=OrderAdjustmentConstants.DURATION%>";
            
            var currentValueInput = document.getElementById("currentValueInput");
            var currentValueDropDown = document.getElementById("currentValueDropDown");
            var newValueInput = document.getElementById("newValueInput");
            var newValueDropDown = document.getElementById("newValueDropDown");
            
            if( durationSelected && currentValueInput && 
                currentValueDropDown && newValueInput && 
                newValueDropDown )
            {
              currentValueInput.style.display = "none";
              currentValueInput.name = "none";
              newValueInput.style.display = "none";
              newValueInput.name = "none";
              
              currentValueDropDown.style.display = "";
              currentValueDropDown.name = "currentValue";
              newValueDropDown.style.display = "";
              newValueDropDown.name = "newValue";
            
            }else{
            
              currentValueInput.style.display = "";
              currentValueInput.name = "currentValue";
              newValueInput.style.display = "";
              newValueInput.name = "newValue";
                                          
              currentValueDropDown.style.display = "none";
              currentValueDropDown.name = "none";
              newValueDropDown.style.display = "none";
              newValueDropDown.name = "none";
            }
          }
          
          var licenseeFeeSelected = fieldToAdjustDropDown.value == "Licensee Fee";
          var feeSelected = licenseeFeeSelected;
          var quantitySelected = !feeSelected;
          var valuesHaveDecimalPoint = currentValueInput.value.indexOf(".") > -1 ||
                                       newValueInput.value.indexOf(".") > -1;
          
          /* We don't want to display decimal points when 
           * globally adjusting quantities (quantities accept integer values only)
           */
          if( quantitySelected && valuesHaveDecimalPoint ){
            currentValueInput.value = 0;
            newValueInput.value = 0;
          }
        }
        
        

//Global Adjustment - END