<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ page errorPage="/jspError.do" %>
<%@ page import="com.copyright.ccc.business.security.UserContextService" %>
<%@ page import="com.copyright.ccc.web.forms.coi.SelectCartPaymentActionForm"%>
<%@ page import="com.copyright.ccc.web.actions.coi.SelectCartPaymentAction"%>
<%@ page import="com.copyright.ccc.business.services.user.User"%>
<%@ page import="com.copyright.svc.ldapuser.api.data.LdapUser"%>
<%@ page import="com.copyright.ccc.business.data.CCUser"%>
<%@ page import="com.copyright.svc.telesales.api.data.Location"%>
<%@ page import="com.copyright.ccc.business.services.ServiceLocator"%>
<%@ page import="com.copyright.svc.userInfo.api.data.UserInfoConsumerContext"%>
<%@ page import="com.copyright.svc.userInfo.api.cyberSource.data.PaymentProfileInfo"%>
<%@ page import="com.copyright.svc.userInfo.api.cyberSource.data.PaymentProfileInfo.PaymentProfileInfoKeys"%>
<%@ page import="com.copyright.svc.userInfo.api.cyberSource.data.SortCriteria"%>
<%@ page import="org.apache.commons.codec.binary.Base64"%>
<%@ page import="java.util.*"%>
<%@ taglib prefix="util" uri="/WEB-INF/tld/cc2-util.tld" %>




<script src="<html:rewrite page="/resources/commerce/js/contextualHelp.js"/>" type="text/javascript"></script>
<link href="<html:rewrite page="/resources/commerce/css/contextualHelp.css"/>" rel="stylesheet" type="text/css" />
<link href="<html:rewrite page="/resources/commerce/css/ccc-new.css"/>" rel="stylesheet" type="text/css" />
<link href="<html:rewrite page="/resources/commerce/css/rightslink_font.css"/>" rel="stylesheet" type="text/css" />
<link href="<html:rewrite page="/resources/commerce/css/impromptu.css"/>" rel="stylesheet" type="text/css" />


<script type="text/javascript" src="<html:rewrite page="/resources/commerce/js/contentLoader.js"/>" ></script>
<script type="text/javascript" src="<html:rewrite page="/resources/commerce/js/eventLogger.js"/>" ></script>
<script type="text/javascript" src="<html:rewrite page="/resources/commerce/js/help.js"/>" ></script>
<!--
<script type="text/javascript" src="<html:rewrite page="/resources/ordermgmt/scripts/jquery-1.3.2.min.js"/>" ></script>
<script type="text/javascript" src="<html:rewrite page="/resources/ordermgmt/scripts/jquery-ui-1.7.2.custom.min.js"/>" ></script>
-->
<script type="text/javascript" src="<html:rewrite page="/resources/ordermgmt/scripts/jquery-impromptu.2.8.js"/>" ></script>
<!--
<link href="<html:rewrite page="/resources/ordermgmt/style/smoothness/jquery-ui-1.7.2.custom.css"/>" rel="stylesheet" type="text/css" />
-->

<script src="<html:rewrite page="/resources/commerce/js/paymentScripts.js"/>" type="text/javascript"></script>


<script type="text/javascript">
if (browserInfo.isNetscape4)
{
	document.write ("<link href='<html:rewrite page='/resources/commerce/css/rightslink_layout_ns4.css'/>' rel='stylesheet' type='text/css' />");
}
else if (browserInfo.isIE)
{
	document.write ("<link href='<html:rewrite page='/resources/commerce/css/rightslink_layout_msie.css'/>' rel='stylesheet' type='text/css' />");
}
else
{
	document.write ("<link href='<html:rewrite page='/resources/commerce/css/rightslink_layout_ns6.css'/>' rel='stylesheet' type='text/css' />");
}

  var eventLogger;
  function initializeEventLogger(){
    var eventLoggerServiceURL = "<html:rewrite page="/advisor/eventLogger.do?operation=logEvent" />";
    eventLogger = new EventLogger( eventLoggerServiceURL );
    var contactLibraryLink = document.getElementById("contactLibraryLink");
    
    if( contactLibraryLink ){
      contactLibraryLink.onclick = performLogContactLibrary;
    }
  }
    
  function performLogContactLibrary(){
     var contactLibraryLink = document.getElementById("contactLibraryLink");
     var communicationValue = new String( escape( contactLibraryLink.href ) );   
     eventLogger.logEvent( EVENT_CONTACT_SME_CHECKOUT , communicationValue );
  }
var rdioCC = document.getElementById('ccSelect');
var rdioInv = document.getElementById('invSelect');


  //---------------------------------------------------------
function getStyleObject(objectId) {
//---------------------------------------------------------
// Gets an object's style object by its id.
//---------------------------------------------------------
    if(document.getElementById && document.getElementById(objectId)) {
        return document.getElementById(objectId).style;
    }
    else if (document.all && document.all(objectId)) {
        return document.all(objectId).style;
    }
    else if (document.layers && document.layers[objectId]) {
        return document.layers[objectId];
    }
    else {
        return false;
    }
}



function ClickCheckBox(fld) {
    if (fld.checked)
  {
    var myFrm = document.getElementById('frm');
    //alert("Always Invoice Before Checked: " + myFrm.alwaysInvoice.value);
    myFrm.alwaysInvoice.value = "true";
    myFrm.alwaysInvoiceFlag.value = "true";
    myFrm.alwaysInvoice.checked = true;
    
    //alert("Always Invoice After Checked: " + myFrm.alwaysInvoice.value);
  }
  else
  {
    var myFrm = document.getElementById('frm');
    //alert("Always Invoice Before Unchecked: " + myFrm.alwaysInvoice.value);
    myFrm.alwaysInvoice.value = "false";
    myFrm.alwaysInvoiceFlag.value = "false";
    myFrm.alwaysInvoice.checked = false;
    //alert("Always Invoice After Unchecked: " + myFrm.alwaysInvoice.value);
  } 

}
</script>

  
    
<div class="clearer"></div>
<!-- Begin Progress -->
<div id="ecom-progress"><html:img src="/media/images/ecomprogress-payment-on.gif" alt="Payment" width="60" height="24" /><html:img src="/media/images/ecomprogress-review.gif" alt="Review" width="52" height="24" /><html:img src="/media/images/ecomprogress-confirmation.gif" alt="Confirmation" width="92" height="24" /></div>

<!-- Begin Left Content -->
<div id="ecom-boxcontent">
<div id="cartOrderTotal" style="height:50px;position:relative;width:694px;">
<div class="floatleft">
<h1>Step 1: Enter Payment Information<span class="subtitle"><a href="/content/cc3/en/toolbar/productsAndSolutions/payPerUsePermissionServices/credit_and_paymentpolicy.html" target="_blank"><u>Review credit and payment policy</u></a></span></h1>

<!-- <a href="#" class="back-link">Cancel and return to cart </a> -->
</div>

<logic:equal name="selectCartPaymentActionForm" property="hasOnlyNonPricedOrders" value="false">


<logic:equal value="true" name="selectCartPaymentActionForm"  property="displayChargeTotal">
<span class="orderPriceTotal">
<h2 >
Order Total:	<span class="price"><bean:write name="selectCartPaymentActionForm" property="cartTotalWithNoDollarSign" /> USD</span>
									<logic:equal name="selectCartPaymentActionForm" property="showExcludeTBDItemText" value="true">
								    <br/><span class="smalltype defaultweight">(Excludes TBD items)</span>
								    </logic:equal>

<div id="creditCartSelectedBlock" style="width:100%;display:none">

									Charge Total:	<span id="displayCartTotalPriceIdUSD" ><bean:write name="selectCartPaymentActionForm" property="cartChargeTotalWithNoDollarSign" /> USD</span>
									<span style="display: none;" id="displayCartTotalPriceIdNonUSD" class="price"><bean:write name="selectCartPaymentActionForm" property="cartChargeTotal" /></span><br/>
									<span style="display: none;" id="displayRefCartTotalPriceIdUSD" >(<bean:write name="selectCartPaymentActionForm" property="cartChargeTotalWithNoDollarSign" /> USD)</span><br/>

</h2>
</div>

</span>
</logic:equal>

<logic:equal value="false" name="selectCartPaymentActionForm"  property="displayChargeTotal">
<br/>
<span class="orderPriceTotal" style="width:400px">
<h2>Order Total:	<span id="displayCartTotalPriceIdUSD" ><bean:write name="selectCartPaymentActionForm" property="cartTotalWithNoDollarSign" /> USD</span>
								<span style="display: none;" id="displayCartTotalPriceIdNonUSD" ><bean:write name="selectCartPaymentActionForm" property="cartTotal" /></span><br/>
								<logic:equal name="selectCartPaymentActionForm" property="showExcludeTBDItemText" value="true">
								    <span   class="smalltype defaultweight">(Excludes TBD items)</span><br/>
								    </logic:equal>
									
								<span style="display: none;" id="displayRefCartTotalPriceIdUSD" >(<bean:write name="selectCartPaymentActionForm" property="cartTotalWithNoDollarSign" /> USD)</span><br />
								    </h2>
</span>
<div id="creditCartSelectedBlock" style="display:none"></div>

</logic:equal>


</logic:equal>

<logic:equal name="selectCartPaymentActionForm" property="hasOnlyNonPricedOrders" value="true">
<span class="orderPriceTotal"><h2>Order Total: <span class="largeType">$TBD</span><br /></h2>
</span>
</logic:equal>
</div>
<div class="horiz-rule"></div>

<a href="cart.do" class="back-link" id="998">Cancel and return to cart </a>



<logic:notEmpty name="selectCartPaymentActionForm" property="hopURL">
	<bean:define id="hopURL" name="selectCartPaymentActionForm" property="hopURL"/>
	<bean:define id="URL" name="selectCartPaymentActionForm" property="cccURL"/>
	<bean:define id="userid" name="selectCartPaymentActionForm" property="userName"/>
	<bean:define id="responseEmail" name="selectCartPaymentActionForm" property="responseEmail"/>
</logic:notEmpty>


<html:form action="selectCoiPaymentType.do?operation=reviewCart" method="post" styleId="frm">
<html:hidden name="selectCartPaymentActionForm" property="hasSpecialOrders"/>
<html:hidden name="selectCartPaymentActionForm" property="cartTotal"/>
<html:hidden name="selectCartPaymentActionForm" property="cartChargeTotal"/>
<html:hidden name="selectCartPaymentActionForm" property="alwaysInvoiceFlag" styleId="alwaysInvoiceFlag" />
<html:hidden name="selectCartPaymentActionForm" property="canOnlyBeInvoiced" styleId="canOnlyBeInvoiced" />



		<%@ include file="/WEB-INF/jsp-modules/payment/tiles/cartPaymentOptions.jsp"%>
		<%@ include file="/WEB-INF/jsp-modules/payment/paymentMainContent.jsp"%>
</html:form>

<%@ include file="/WEB-INF/jsp-modules/payment/addNewCreditCard.jsp"%>


<!-- *************************************************************** -->

<div class="clearer"></div>

<p>&nbsp;</p>
<div class="horiz-rule"></div>
<div class="txtRight" style="display: block;" id="displayContinue">
<html:img src="/media/images/btn_continue.gif" alt="Continue" width="75" height="18" onclick="submitPayment();" styleId="999" /></div>

</div>



<script type="text/javascript">
$(document).ready(function(){
	// initialize modal property
	paymentScreenProps=new ccPaymentDefaultProps("selectCoiPaymentType.do","Normal","null","null","null");
	//override with modal window
	//$("#displayContinue").hide();
	paymentType_RadioToggle();
			 
});
  


  function setUpContactLibrary() {
    setUpContactLibraryLink();
    initializeEventLogger();    
  }

  function setUpContactLibraryLink() {
  
    var contactLibraryEmailLink="";
          
    var contactLibraryLink = document.getElementById("contactLibraryLink");
    
    if( contactLibraryLink ){
      contactLibraryLink.href = contactLibraryEmailLink;
    } 
  }
// Hook called from paymentscript.jsp
	function HOOK_IFrameShowEvent(iframeState){
		if(iframeState=='show'){
			$('#cancelButton').show();
		}else{
			$('#cancelButton').hide();
		}
	return;
	}
	function HOOK_BeforePaymentSubmit(){
		return;
	}

	
</script>











<!-- Webtrends tags for capturing scenarios -->
<META name="WT.si_n" content="Checkout">
<META name="WT.si_x" content="6">
<!-- end Webtrends tags -->

