<!--
function initPage() {
   MM_preloadImages('/media/images/nav_business_on.gif','/media/images/nav_academic_on.gif','/media/images/nav_publishers_on.gif','/media/images/nav_authors_on.gif','/media/images/nav_partners_on.gif','/media/images/nav_home_on.gif','/media/images/calendar.gif');
   updateInputField();

   var mainForm = document.getElementById('orderHistoryActionForm');
   var inputs = mainForm.getElementsByTagName('input');

   for (var j=0;j < inputs.length;j++)
        addInputSubmitEvent(mainForm, inputs[j]);
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
function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
function updateInputField() {
  var searchFld = document.getElementById('srchFld'); // the Search selection box
  var selectedIndex = searchFld.selectedIndex;
  var srchOptions = searchFld.options;
  var selectedValue = srchOptions[selectedIndex].value;
  var dateFlds = document.getElementById('dateFlds'); // the input date fields for search
  var srchTxt = document.getElementById('srchTxt');   // the search text input field
  // Now make visible one TD tag based on user search selection
  // Note constants defined in DisplaySpecServices
  switch(selectedValue) {
     case "2": // Order Date Range
     case "17": // Start of Term
       dateFlds.style.display="inline";
       srchTxt.style.display="none";
       focusDate();
     break;
     default: 
       dateFlds.style.display="none";
       srchTxt.style.display="";
       focusText();
     break;
  }
}
function focusDate()
{
  var beginDate = document.getElementById('beginDate');
  if (beginDate) { beginDate.focus(); }
}
function focusText() {
  var txtFld = document.getElementById('inputTxt');
  if (txtFld) { txtFld.focus(); }
}
function newSort(radioBtn) {
  var lastSortDir = document.getElementById('lastDir');
  if (lastSortDir.value != radioBtn.value) { 
     document.orderHistoryActionForm.action = 'sortOrderHistory.do';
     document.orderHistoryActionForm.submit(); 
  }
}
function sortList() {
  document.orderHistoryActionForm.action = 'sortOrderHistory.do';
  document.orderHistoryActionForm.submit();
}
function setSortOrder(sortControl) {
  // First check if the sort is set to relevance
  var ascRadio = document.getElementById('ASC');
  var dscRadio = document.getElementById('DSC');

  var option = sortControl.value;
  switch(option) {
     case '0': // order date
     case '1': // conf number
     case '3': // Start of Term
     case '5': // Course Number
       ascRadio.checked = false;
       dscRadio.checked = true;     
     break;
     case '2': // University/Inst.
     case '4': // Course Name
     case '6': // Instructor
     case '7': // Your Ref.
     case '17': // Your Acctg. Ref.
       ascRadio.checked = true;
       dscRadio.checked = false;
     break;
  }
  
  sortList();
}
function isTextNotEmpty() {
   var txt = document.getElementById('inputTxt');
   if (txt.value == null || txt.value == '') { return false; }
   else return true;
}
function areDatesNotEmpty() {
  var result = false;
  var begin = document.getElementById('beginDate');
  var end = document.getElementById('endDate');
  if (begin.value == '' || end.value == '') { return false; }
  else { result = true; }

  return result;  
}
function copyAcademic(purInst) {
  var cartEmpty = document.getElementById('cartEmpty');
  var isEmpty = cartEmpty.value;
//Validations for copy non acad to acad and vice versa removed 
 // if (isEmpty == 'true') {
     copy(purInst);
 // } else {
   //  alert('You may only copy orders to an empty cart. Please purchase or empty the existing items in your cart.'); // debug
 // }
}
function copyNonacademic(purInst) {
  var cartEmpty = document.getElementById('cartEmpty');
  var isEmpty = cartEmpty.value;
//Validations for copy non acad to acad and vice versa removed 
//  if (isEmpty == 'true') {
     copy(purInst);
 // } else {
  //   alert('You may only copy orders to an empty cart. Please purchase or empty the existing items in your cart.'); // debug
 // }
}

var isBeingCopied = false;

function copy(purInst) {
  if( !isBeingCopied )
  {
    isBeingCopied = true;
    document.forms[0].action = 'copyOrder.do?id=' + purInst;
    document.forms[0].submit();
  }
}
function deleteOrder(purInst) {
  if (confirm("Removing this order from view will remove it permanently from Order History. You will not be able to view it again. If you\'d like to continue\, select \"OK\". Otherwise\, select \"Cancel\".")) {
     document.forms[0].action = 'deleteOrder.do?id=' + purInst;
     document.forms[0].submit();
  }
}
function view(status) {
  var filterStatus = document.getElementById('status');
  filterStatus.value = status;
  document.orderHistoryActionForm.action = 'orderHistory.do';
  
  document.orderHistoryActionForm.submit();
}
function sort() {
  document.orderHistoryActionForm.action = 'orderHistory.do';
  
  document.orderHistoryActionForm.submit();
}
function scroll(page) 
{
   var currPage = document.getElementById('currPage');
   currPage.value = page;
   // use non-validating action
   document.orderHistoryActionForm.action = "orderHistory.do";
   
   document.orderHistoryActionForm.submit();
}
//-->

