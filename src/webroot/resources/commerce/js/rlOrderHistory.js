<!--
function initPage() {
   MM_preloadImages('/media/images/nav_business_on.gif','/media/images/nav_academic_on.gif','/media/images/nav_publishers_on.gif','/media/images/nav_authors_on.gif','/media/images/nav_partners_on.gif','/media/images/nav_home_on.gif','/media/images/calendar.gif');

   var mainForm = document.getElementById('rlOrderHistoryForm');
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

function newSort(radioBtn) {
     document.rlOrderHistoryForm.action = 'rlOrderHistory.do';
     document.rlOrderHistoryForm.submit(); 
}
function sortList() {
  document.rlOrderHistoryForm.action = 'rlOrderHistory.do';
  document.rlOrderHistoryForm.submit();
}
function setSortOrder(sortControl) {
  // First check if the sort is set to relevance
 // var ascRadio = document.getElementById('ASC');
  //var dscRadio = document.getElementById('DSC');

  //var option = sortControl.value;
  //switch(option) {
   //  case '0': // order date
  //   case '1': // conf number
  //   case '3': // Start of Term
 //    case '5': // Course Number
 //      ascRadio.checked = false;
 //      dscRadio.checked = true;     
  //   break;
 //    case '2': // University/Inst.
 //    case '4': // Course Name
 //    case '6': // Instructor
 //    case '7': // Your Ref.
 //    case '17': // Your Acctg. Ref.
 //      ascRadio.checked = true;
//       dscRadio.checked = false;
//     break;
//  }
  
  sortList();
}

function view(status) {
  var filterStatus = document.getElementById('state');
  filterStatus.value = status;
  document.rlOrderHistoryForm.action = 'rlOrderHistory.do';
  
  document.rlOrderHistoryForm.submit();
}

function sort() {
  document.rlOrderHistoryForm.action = 'rlOrderHistory.do';
  
  document.rlOrderHistoryForm.submit();
}
function scroll(page) 
{
   var currPage = document.getElementById('currPage');
   currPage.value = page;
   // use non-validating action
   document.rlOrderHistoryForm.action = "rlOrderHistory.do";
   
   document.rlOrderHistoryForm.submit();
}
//-->

