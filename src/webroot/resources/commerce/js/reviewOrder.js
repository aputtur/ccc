var bNoCancel = true;
function cancel(bCancel)
{
  bNoCancel = !bCancel;
}
function validateTerms(){
var msg1="Please accept the following to continue \n";
var msg0="Please accept the terms and conditions to continue.";
var displaymsg1=false;
var validateTermsFailed=false;
   if (bNoCancel) {
      termBox = document.getElementById("termz");
      rlrTermBox = document.getElementById("rlrtermz");
     // spTermBox = document.getElementById("sptermz");
      termValue = termBox.value;
     /* if(spTermBox!=null && !spTermBox.checked){
    	  displaymsg1=true;
    	  msg1=msg1 +"--For permission requests. \n"
    	  validateTermsFailed=true;
      }*/
      if(rlrTermBox!=null && !rlrTermBox.checked){
    	  displaymsg1=true;
    	  msg1=msg1 +"--For reprints. \n"
         validateTermsFailed=true;
      }
    
      if (!termBox.checked) { 
    	  msg1=msg1 +"--Terms and Conditions. \n"
         validateTermsFailed=true;
      }
      
      if (validateTermsFailed) { 
    	  if(displaymsg1){
    		  alert(msg1);
    	  }else{
          alert(msg0);
    	  }
          var sbBtn = document.getElementById('submitBtn');
          sbBtn.disabled = false;
          return false;
      } else { return true; } 
   } else return true; 
}
function terms()
{
   openPopup('reviewTerms.do?operation=reviewTerms',"","");
}
function viewTerm(idx, type) {
  var url = 'popupTerm.do?operation=popupTerm&idx=' + idx + '&type=' + type;
  openPopup(url,"myWindow","status,height=320,width=600,resizable=1");
}
function popupHelp(url) {
  openPopup(url,"myWindow","status,location=1,screenX=50,screenY=50,scrollbars=1,height=560,width=760,resizable=1");
}
// Note this function does not behave the same in FF vs IE.  IE raises a dialog confirming
// the window close action.  FF immediately closes the window without a dialog.  Coding a 
// confirm test in the method results in two dialogs for IE.  The compromise is to just close
// the window with no confirmation in FF.
function closeWdw() {
  openPopup('','_parent',''); // Firefox hack to close non-script opened windows
  window.close();
}
function edtPage() {
  var edtPymnt = document.getElementById('payment');
  edtPymnt.value = 'Edit'; // non-null non-empty goes to payment edit
  cancel('true');
  var myFrm = document.getElementById('sbmtFrm');
  if (myFrm) {
    myFrm.submit();
  }
}
function sbmtPage() {
  var edtPymnt = document.getElementById('payment');
  edtPymnt.value = ''; // empty value goes to payment processor
  if (validateTerms()) {
      var myFrm = document.getElementById('sbmtFrm');
      if (validateTerms()) {
         if (myFrm) {
            myFrm.submit();
         }
      }
  }
}
