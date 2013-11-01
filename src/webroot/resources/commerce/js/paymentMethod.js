function setInvoice() {
  var invRadio = document.getElementById('invSelect');
  if (invRadio) {
     invRadio.checked = true;
  }
}
function setCC() {
  var ccRadio = document.getElementById('ccSelect');
  if (ccRadio) {
    ccRadio.checked = true;
  }
} 
function sbmtPage() {
  var myFrm = document.getElementById('frm');
  myFrm.status.value = "submit";
  myFrm.submit();
}
