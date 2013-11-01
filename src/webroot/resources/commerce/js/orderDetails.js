//<!--
function initPage() {
   MM_preloadImages('/media/images/nav_business_on.gif','/media/images/nav_academic_on.gif','/media/images/nav_publishers_on.gif','/media/images/nav_authors_on.gif','/media/images/nav_partners_on.gif','/media/images/nav_home_on.gif','/media/images/calendar.gif');
   updateInputField();
   var mainForm = document.getElementById('orderDetailActionForm');
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
function focusDate()
{
  var beginDate = document.getElementById('beginDate');
  if (beginDate) { beginDate.focus(); }
}
function focusText() {
  var txtFld = document.getElementById('srchTxt');
  if (txtFld) { txtFld.focus(); }
}
function focusGo() {
  var btn = document.getElementById('goBtn');
  if (btn) { btn.focus(); }
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
  var selectedValue = searchFld.options[selectedIndex].value;
  var dateFlds = document.getElementById('dateFlds'); // the input date fields for search
  var srchTxt = document.getElementById('srchTxt');   // the search text input field
  var permType = document.getElementById('permType'); // Permission Type
  var permStatus = document.getElementById('permStatus'); // Permission Status
  var billStatus = document.getElementById('billStatus'); // Permission Status
  var specialOrderUpdate = document.getElementById('specialOrderUpdate'); // Special Order Update

    // Now make visible one TD tag based on user search selection
    switch(selectedValue) {
     case "0": // no filter selected
       dateFlds.style.display="none";
       srchTxt.style.display="";
       permType.style.display="none";
       permStatus.style.display="none";
       billStatus.style.display="none";
       specialOrderUpdate.style.display="none";
       focusText();
     break;
     case "2": // Order Date Range or Order ID selection
     case "15": // Republication Date
       dateFlds.style.display="";
       srchTxt.style.display="none";
       permType.style.display="none";
       permStatus.style.display="none";
       billStatus.style.display="none";
       specialOrderUpdate.style.display="none";
       focusDate();
     break;
     case "10": // Permission Type
       dateFlds.style.display="none";
       srchTxt.style.display="none";
       permType.style.display="";
       permStatus.style.display="none";
       billStatus.style.display="none";
       specialOrderUpdate.style.display="none";
       focusGo();
     break;
     case "11": // Permission Status
       dateFlds.style.display="none";
       srchTxt.style.display="none";
       permType.style.display="none";
       permStatus.style.display="";
       billStatus.style.display="none";
       specialOrderUpdate.style.display="none";
       focusGo();
     break;
     case "12": // Billing Status
       dateFlds.style.display="none";
       srchTxt.style.display="none";
       permType.style.display="none";
       permStatus.style.display="none";
       specialOrderUpdate.style.display="none";
       billStatus.style.display="";
       focusGo();
     break;
     case "29": // Billing Status
         dateFlds.style.display="none";
         srchTxt.style.display="none";
         permType.style.display="none";
         permStatus.style.display="none";
         specialOrderUpdate.style.display="";
         billStatus.style.display="none";
         focusGo();
       break;
     default: 
       dateFlds.style.display="none";
       srchTxt.style.display="";
       permType.style.display="none";
       permStatus.style.display="none";
       billStatus.style.display="none";
       specialOrderUpdate.style.display="none";
       focusText();
     break;
  }
}
function scroll(page) 
{
   var currPage = document.getElementById('currPage');
   currPage.value = page;
   document.orderDetailActionForm.action = 'sortOrderDetail.do';
   document.orderDetailActionForm.submit();
}
function newSort(radioBtn) {
  var lastSortDir = document.getElementById('lastDir');
  if (lastSortDir.value != radioBtn.value) { 
     document.orderDetailActionForm.action = 'sortOrderDetail.do';
     document.orderDetailActionForm.submit(); 
  }
}
function sortList() {
  document.orderDetailActionForm.action = 'sortOrderDetail.do';
  document.orderDetailActionForm.submit();
}
function setSortOrder(sortControl) {
  var ascRadio = document.getElementById('ASC');
  var dscRadio = document.getElementById('DSC');

  var option = sortControl.value;
  switch(option) {
     case '-1': // Word match Relevance
     case '0': // Order Date
     case '9': // Order Detail ID
     case '13': // Invoice Number
     case '15': // Publication Date
       ascRadio.checked = false;
       dscRadio.checked = true;     
     break;
     case '7': // Your Ref.
     case '8': // Publication Title
     case '10': // Permission Type
     case '11': // Permission Status
     case '12': // Billing Status
     case '14': // Republication Title
     case '16': // Publication Org
       ascRadio.checked = true;
       dscRadio.checked = false;
     break;
  }
}
function popupText(text) {
var url = 'popupText.do?txt=' + escape(text);
openPopup(url,"myWindow","status,location=0,height=290,width=600,resizable=1");
}
function copyLicAcademic(purId, id) {
  var cartStatus = document.getElementById('cartAcademic');
  var isAcademic = cartStatus.value;
//Validations for copy non acad to acad and vice versa removed 
 // if (isAcademic == 'true') {
    copy(purId, id);
  //} else {
   // alert('You may only copy academic orders to an empty cart or to a cart with other academic orders. Please purchase or empty the existing items in your cart.'); // debug
 // }
}
function copyLicNonacademic(purId, id) {
  var cartStatus = document.getElementById('cartAcademic');
  var isAcademic = cartStatus.value;
//Validations for copy non acad to acad and vice versa removed 
// if (isAcademic == 'false') {
     copy(purId, id);
 // } else {
   // alert('You may only copy non-academic orders to an empty cart or to a cart with other non-academic orders. Please purchase or empty the existing items in your cart.'); // debug
 // }
}
function copy(purId, id) {
  document.forms[0].action = 'copyLicense.do?purid='+purId + '&id=' + id;
  document.forms[0].submit();
}
function checkEnter(e) {
  if (window.event) { keynum = window.event.keyCode } // IE
  else if (e.which) { keynum = e.which } // Netscape/Firefox/Opera
  
  if (keynum == 13) { alert('Enter Pressed'); }
}
//-->
