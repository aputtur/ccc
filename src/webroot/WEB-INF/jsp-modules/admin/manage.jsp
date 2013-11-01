<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib prefix="logic" uri="/WEB-INF/tld/struts-logic.tld" %>
<%@ taglib uri="/WEB-INF/tld/cc2-security.tld" prefix="secure"%>
<%@ page errorPage="/jspError.do" %>
<%@ page import="com.copyright.ccc.config.CC2Configuration" %>
<title>CCC - Manage Account</title>
<link href="resources/commerce/css/default.css" rel="stylesheet" type="text/css" />
<link href="resources/commerce/css/ccc-new.css" rel="stylesheet" type="text/css" />
<link href="resources/commerce/css/manage-overrides.css" rel="stylesheet" type="text/css" />

<jsp:useBean id="manageAccountForm" scope="session" class="com.copyright.ccc.web.forms.ManageAccountActionForm"/>

<script src="resources/commerce/js/dropdown.js" type="text/javascript"></script>
<script type="text/javascript" src="resources/commerce/js/util.js"></script>
<script language="JavaScript" type="text/JavaScript">
<!--
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
function showRightslink() {
   openPopup('/content/cc3/en/toolbar/productsAndSolutions/rightslink.html',"mywindow","menubar=1,toolbar=1,location=1,scrollbars=1,resizable=1,width=800,height=600");
}
function gpoHide() {
  var orderBtm = document.getElementById('orders');
  if (orderBtm) {
     orderBtm.style.display="none";
  }
}
function gpoShow() {
  var orderBtm = document.getElementById('orders');
  if (orderBtm) {
     orderBtm.style.display="block";
  }
}
 function addUser() {
  var axshun = 'displayAddUserRegistration.do';
  //var gpoRadioCtl = document.getElementById('gpoRadio');
 // if (gpoRadioCtl.checked) {
 //   axshun = 'displayGPOUserRegistration.do';
 // }
  document.manageAccountForm.action = axshun;
  document.manageAccountForm.submit();
}

function updateAddress() {
  document.getElementById("operation").value = "updateAddress";
  document.manageAccountForm.submit();
}
//-->
</script>
<div id="two-column-main">
<h2>Manage Account</h2>

<br />
<div class="manage clearer" style="position:relative;">
<div class="manage-top"></div>	
<div class="manage-line">
<h3>Account Settings</h3>
<br />
<form name="manageAccountForm">
<!-- <input type="radio" name="radio" <bean:write name="manageAccountForm" property="disabled"/> <bean:write name="manageAccountForm" property="payPerUseCheck"/> onclick="gpoShow();"/>Pay-Per-Use account&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<input id="gpoRadio" type="radio" name="radio" <bean:write name="manageAccountForm" property="disabled"/> <bean:write name="manageAccountForm" property="gpoCheck"/> onclick="gpoHide();"/>Grant Permissions Online (GPO) account -->
</form>
<!-- <br /><br /> -->
<html:hidden property="operation" styleId="operation" value=""/>
<ul id="navlinks">
  <li><a href="updateAddressBilling.do">Update address and billing information</a></li>
  <!--  li><a href="updateAddressBilling.do">Update address and billing information</a>g</li-->
  <li><html:link action="/updateEmailAddress.do?operation=defaultOperation">Update email address or password</html:link></li>
  <li><a href="javascript:addUser();">Add additional user</a></li>
</ul>
 <br />
</div>
</div>
<div class="manage-btm"></div>	
<div id="orders">
<br />
    <div class="manage clearer" style="position:relative;">
    <div class="manage-top"></div>	
    <div class="manage-line">
    <h3>Orders (for Pay-Per-Use accounts only)</h3>
    <p></p>
    <logic:equal name="manageAccountForm" property="hasCustResponseOrders" value="true">
    	<p class="icon-alert"><strong>You have <html:link action="/orderDetail.do?filter=custResponseOrders">orders awaiting a response</html:link></strong></p>

    </logic:equal>
    <ul id="navlinks">
    <li><html:link action="orderHistory.do">View your Order History</html:link></li>
    <li><a href="viewReports.do">View account activity reports</a></li>
    <li><a href="viewUnpaidInvoices.do">View unpaid invoices</a> <span style="font-weight:normal"> (You can also </span><html:link action="anonymousInvoicePaymentStepOne">pay invoices without logging in</html:link> <span style="font-weight:normal">)</span></li>
    <!-- <li><a href="viewUnpaidInvoices.do">View unpaid invoices</a> <span style="font-weight:normal"> (click </span><a href="http://support.copyright.com/index.php?action=article&id=301&relid=11" target="_blank">here</a> <span style="font-weight:normal">for instructions on paying an invoice without logging in)</span></li> -->
    <li><a href="manageCreditCards.do">Manage Credit Cards</a></li>
    </ul>
<br />
    </div>
    </div>
    <div class="manage-btm"></div>
</div>
<br /><br />
<a href="/content/cc3/en/toolbar/aboutUs/contact_us.html"><img src="/media/images/btn_need_help.gif" alt="Need Help? Contact Us" align="right" /></a><br clear="all" />
</div>
<div id="two-column-sidebar">
<img src="/media/images/landingright-quicklinks.gif" alt="Quick Links" />

<div id="quick-links">
<span><a href="http://goto.copyright.com/LP=46?source=ccom-Mgacct"><b>Annual License Holder?</b><br />Contact a representative</a></span>
<span><a href="mailto:publishers@copyright.com"><b>Publisher?</b><br />Contact a representative</a></span>
<span><a href="mailto:rightsholders@copyright.com"><b>Author?</b><br />Contact a representative</a></span>
<img src="/media/images/landingright-bottomcorner.gif" /></div>



<table><tr><td>
<b>Are you a Rightslink customer?</b></td></tr>
<tr><td><a target="_blank" href="http://myaccount.copyright.com">Manage account info</a></td></tr>
<tr><td><a href="/content/cc3/en/toolbar/productsAndSolutions/rightslink.html">What is Rightslink?</a></td></tr>


</table>

<br />
<table><tr><td>
<b>Are you a GPO customer?</b></td></tr>
<tr><td><a href="<%= CC2Configuration.getInstance().getGPOLOG() %>">Log in</a></td></tr>
<tr><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td></tr>
</table>
</div>


<!-- End Main Content -->

