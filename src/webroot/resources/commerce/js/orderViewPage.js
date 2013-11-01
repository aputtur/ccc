//<!--
//var cal = new CalendarPopup("calendarBox");
//cal.offsetY = 30;
//cal.offsetX = -45
//cal.showNavigationDropdowns();
function scroll(page) 
{
   var currPage = document.getElementById('currPage');
   currPage.value = page;
   document.orderHistoryActionForm.submit();
}
function updateInputField() {
  var searchFld = document.getElementById('srchFld'); // the Search selection box
  var selectedIndex = searchFld.selectedIndex;
  var selectedValue = searchFld.options[selectedIndex].value;
  var dateFlds = document.getElementById('dateFlds'); // the input date fields for search
  var srchTxt = document.getElementById('srchTxt');   // the search text input field
  var permType = document.getElementById('permType'); // Permission Type
  var permStatus = document.getElementById('permStatus'); // Permission Status
  var billStatus = document.getElementById('billStatus'); // Permission Status

  // Now make visible one TD tag based on user search selection
  switch(selectedValue) {
   case "0": // no filter selected
       dateFlds.style.display="none";
       srchTxt.style.display="none";
       permType.style.display="none";
       permStatus.style.display="none";
       billStatus.style.display="none";
   break;
   case "2": // Order Date Range or Order ID selection
   case "15": // Republication Date
       dateFlds.style.display="";
       srchTxt.style.display="none";
       permType.style.display="none";
       permStatus.style.display="none";
       billStatus.style.display="none";
   break;
   case "10": // Permission Type
       dateFlds.style.display="none";
       srchTxt.style.display="none";
       permType.style.display="";
       permStatus.style.display="none";
       billStatus.style.display="none";
   break;
   case "11": // Permission Status
       dateFlds.style.display="none";
       srchTxt.style.display="none";
       permType.style.display="none";
       permStatus.style.display="";
       billStatus.style.display="none";
   break;
   case "12": // Billing Status
       dateFlds.style.display="none";
       srchTxt.style.display="none";
       permType.style.display="none";
       permStatus.style.display="none";
       billStatus.style.display="";
   break;
   default: 
       dateFlds.style.display="none";
       srchTxt.style.display="";
       permType.style.display="none";
       permStatus.style.display="none";
       billStatus.style.display="none";
   break;
  }
}
function initPage() {
   //MM_preloadImages('/media/images/nav_business_on.gif','/media/images/nav_academic_on.gif','/media/images/nav_publishers_on.gif','/media/images/nav_authors_on.gif','/media/images/nav_partners_on.gif','/media/images/nav_home_on.gif','/media/images/calendar.gif');
   updateInputField();
   initSort();

   var mainForm = document.getElementById('orderViewActionForm');
   var inputs = mainForm.getElementsByTagName('input');
   for (var j=0;j < inputs.length;j++) { addInputSubmitEvent(mainForm, inputs[j]); }
}
function addInputSubmitEvent(form, input) {
  input.onkeydown = function(e) {
    e = e || window.event;
    if (e.keyCode == 13) {
       form.submit();
       return false;
    }
  };
}
function initSort() {
  var sortFld = document.getElementById('sortFld');
  if (sortFld) {
    var radios = document.getElementById('radio');
    if (radios) {
        var option = sortFld.value;
        switch (option) {
          case '-1': // Choose One
            // suppress sort fields
            var radios = document.getElementById('radio');
            radios.style.display="none";     
          break;
          default: // your acctg. ref.
            var radios = document.getElementById('radio');
            radios.style.display="";
          break;
        }
    }
  }
}
function copyAcademic(purInst) {
  var cartStatus = document.getElementById('crtEmpty');
  var isEmpty = cartStatus.value;
//Validations for copy non acad to acad and vice versa removed 
 // if (isEmpty == 'true') {
    copy();
 // } else {
   // alert('You may only copy orders to an empty cart. Please purchase or empty the existing items in your cart.'); // debug
 // }
}
function copyNonacademic() {
  var cartStatus = document.getElementById('crtEmpty');
  var isEmpty = cartStatus.value;
  //Validations for copy non acad to acad and vice versa removed 
 // if (isEmpty == 'false') {
     copy();
 // } else {
 //   alert('You may only copy orders to an empty cart. Please purchase or empty the existing items in your cart.'); // debug
 // }
}
function showSortRadios() {
  // Only perform if the sort chosen is not "Choose One"
  var sortFld = document.getElementById('sortFld');
  if (sortFld) {
     if (sortFld.value != '-1') {
        var radios = document.getElementById('radio');
        if (radios) {
           radios.style.display="";
           sort();
        }
     }
  }
}
function sort() {
  document.orderViewActionForm.action = 'orderView.do';
  document.orderViewActionForm.submit();
}
function newSort(radioBtn) {
  var lastSortDir = document.getElementById('lastDir');
  if (lastSortDir.value != radioBtn.value) { 
     document.orderViewActionForm.action = 'orderView.do';
     document.orderViewActionForm.submit(); 
  }
}
//-->
