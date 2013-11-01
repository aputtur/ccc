<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ page errorPage="/jspError.do" %>
<%@ page import="com.copyright.ccc.business.security.UserContextService" %>
<%@ page import="com.copyright.ccc.web.forms.coi.SelectPaymentActionForm"%>
<%@ page import="com.copyright.ccc.web.actions.coi.SelectPaymentAction"%>
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


<jsp:useBean id="selectPaymentActionFormCOI" scope="session" class="com.copyright.ccc.web.forms.coi.SelectPaymentActionForm"/>

<script src="<html:rewrite page="/resources/commerce/js/contextualHelp.js"/>" type="text/javascript"></script>
<link href="<html:rewrite page="/resources/commerce/css/contextualHelp.css"/>" rel="stylesheet" type="text/css" />
<link href="<html:rewrite page="/resources/commerce/css/ccc-new.css"/>" rel="stylesheet" type="text/css" />
<link href="<html:rewrite page="/resources/commerce/css/rightslink_font.css"/>" rel="stylesheet" type="text/css" />
<link href="<html:rewrite page="/resources/commerce/css/impromptu.css"/>" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<html:rewrite page="/resources/commerce/js/paymentMethod.js"/>" ></script>
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

<script type="text/javascript">

function handleCreditCardTxFailure(){
	$('#hop').hide();
	$('#errorSection').html("An error has occurred in processing the Credit Card information. Please confirm the Credit Card data is correct and resend the request. If you continue to experience difficulties please contact Rightslink Customer Service at <br>+1-877/622-5543 (toll free) or <br>+1-978/646-2777 or email at " +
	"<a href='mailto:customercare@copyright.com?subject=Rightslink Change Credit Card Error'>customercare@copyright.com</a>.");
}

$(document).ready(function(){	
	init();
});

function saveProfileValues(selectedRadio) {
	if (!selectedRadio.selected ) {
		var prf = selectedRadio.id;
		$("#displayContinue").show();
		//alert(prf);
		var frm = 'span[id^='+selectedRadio.id+"]'";
		//alert(frm);
		// frm_property.val( this.val )
		$(frm).each(function(i){
			var frmid = $(this).prop('id');
			var tov = '#'+frmid.replace(prf,'frm');
			$(tov).val($(this).prop('innerHTML'));
			//alert('SpanID='+frmid+' Form value='+$(tov).val());
		});
	}
}

function getSelectedCurrency(val){
	var currencyType = val;
	var appender;
	if(currencyType =="EUR"){
		appender = "&nbsp;EUR";
	}else if(currencyType =="GBP"){
		appender = "&nbsp;GBP";
	}else if(currencyType =="JPY"){
		appender = "&nbsp;JPY";
	}else if(currencyType =="CAD"){
		appender = "&nbsp;CAD";
	}else if(currencyType =="USD"){
		appender = "&nbsp;USD";
	}else if(currencyType =="CHF"){
        appender = "&nbsp;CHF";
    }
	$.ajax({
			type: "POST",
			url: "/selectCoiPaymentType.do?operation=multiCurrencyRates",
			data: "currencyType=" +val,
			success: 
			function(data){
				if(currencyType !="USD"){
					data = data+appender;
					$("#displayTotalPriceIdNonUSD").html(data);
					$("#displayTotalPriceIdNonUSD").show();
					$("#displayTotalPriceIdUSD").hide();
					$("#displayCartTotalPriceIdNonUSD").html(data);
					$("#displayCartTotalPriceIdNonUSD").show();
					$("#displayCartTotalPriceIdUSD").hide();
					$("#displayRefCartTotalPriceIdUSD").show();
				}else{
					$("#displayTotalPriceIdNonUSD").hide();
					$("#displayTotalPriceIdUSD").show();
					$("#displayCartTotalPriceIdNonUSD").hide();
					$("#displayCartTotalPriceIdUSD").show();
					$("#displayRefCartTotalPriceIdUSD").hide();
				}
											
			}
		});
}

function init(){
	$('#errorSection').html("");
	
	$('#hop').hide();	
	
	$('table.striped tbody tr:not([th]):even').css("background-color", "#d0d9de");
	
	$('#cancelButton').prop("disabled", true);
	$('#cancelButton').css("font-size","14px").css("font-weight","bold");		
	$('#cancelButton').click(function(){
		toggleFrame('hide');
		$("#displayContinue").show();
	});
			
	$('#addCardButton').click(function(){
		$('#errorSection').html("");
		$("#displayContinue").hide();
		toggleFrame('show', 'new');
	});	
	$('#addCardButton').css("font-size", "10px").css("font-weight", "bold");

	$('.selectable').bind("click", function(){
		$('#errorSection').html("");		
	});		
	
	$('a.modifiable').bind("click", function(){
		$('#errorSection').html("");
		$("#displayContinue").hide();
		var profileid = $(this).attr("value");
		$.ajax({
   			type: "POST",
   			url: "/selectCoiPaymentType.do?operation=buildSubscriptionSignature",
   			data: "profileid=" + profileid,
   			success: 
				function(data){
					//$('#modifySignatureOutput').replaceWith("<div id='modifySignatureOutput'>" + data + "</div>");
					$('#modifySignatureOutput').html(data);
					toggleFrame('show', 'update');
   				}
 		});
	});
		
	$('a.deleteable').bind("click", function(){
		$('#errorSection').html("");
		toggleFrame('hide');
		clearPaymentMethod();
		var profileid = $(this).attr("value");
		$.prompt('Are you sure you want to delete this credit card?',{
      		buttons:{Ok:true,Cancel:false},
      		prefix:'cleanblue',
			callback: function(data){
				if(data){
					$.ajax({
						type: "POST",
						url: "/selectCoiPaymentType.do?operation=disableCreditCard",
						data: "profileid=" + profileid +"&userid=" + $('#theUserId').val(),
						success: 
							function(data){								
								getCreditCards("delete");
							}
					});					
				}
   			}				
		});
	});		
//	highlightExpiredCards();
	window.scrollTo(0,190);
}
function reloadPage(){	
	window.location.reload();
}

//triggered by iframe reload event on create and update
//called directly from delete script above

function getCreditCards(){
		$('#hop').fadeOut("slow");
		$('#hop').hide(4700);
		$('#cancelButton').prop("disabled", true);
		$.ajax({
 			type: "POST",
 			url: "/selectCoiPaymentType.do?operation=retrieveCreditCardList",
 			//url: "/showCoiCreditCards.do?operation=retrieveCreditCardList",
 			data: "userid=" + $('#theUserId').val(),
 			success: 
				function(data){
				//alert(data);			
					try	{		
						jsonData = JSON.parse(data);
						var validationFailed = (jsonData["error0"]);//convention
					}	catch(error)	{
						//don't care
						
					}	
					if(validationFailed != undefined){//have an error so reload page	
						reloadPage();
					}else{
						//$('#ccRows').replaceWith("<div>" + data + "</div>");
						$("#ccRows").html(data);
					    $("#profileRow1").prop("checked", "checked");
					    $("#profileRow1").click();
						init();	
					}
										
 				}
		});	
//		highlightExpiredCards();			
}


function toggleFrame(mode, txtype){	
	if(mode=='show'){	
		if(txtype=="update"){
			$.ajax({
				type: "POST",
				url: "/selectCoiPaymentType.do?operation=populateIframeInfo",
				success: 
				function(data){
					$('#theHOPUpdateDriverForm').submit();
				}
			});	
			//$('#theHOPUpdateDriverForm').submit();
			$('#hop').show(1400);	
			$('#cancelButton').prop("disabled", false); 
		}else if (txtype=="new"){
			$.ajax({
				type: "POST",
				url: "/selectCoiPaymentType.do?operation=populateIframeInfo",
				success: 
				function(data){
					$('#theHOPDriverForm').submit();
				}
			});			
			//$('#theHOPDriverForm').submit();
			$('#hop').show(1400);	
			$('#cancelButton').prop("disabled", false);
			
		}			
	}else if(mode=='hide'){
		$('#hop').hide(1300);
		$('#cancelButton').prop("disabled", true); 				
	}
	window.scrollTo(0,600);
}

//clearPaymentMethod when other action occurs
function clearPaymentMethod() {
	$(".selectable").each(function(){
		$(this).prop("checked", false);			
		isPaymentMethodSelected = false;
		paymentProfileId = "0";
	});	
}

/*
 * these functions are now disabled
 * this functionality is now done on the server side which is browser/java-script independent
 *
function daysInMonth(month,year) {
	var m = [31,28,31,30,31,30,31,31,30,31,30,31];
	if (month != 2) return m[month - 1];
	if (year%4 != 0) return m[1];
	if (year%100 == 0 && year%400 != 0) return m[1];
	return m[1] + 1;
}

function highlightExpiredCards(){
	$('[id^=ccExpirationDate]').each(function(){
		var expDateText = $(this).find('span').text();
		alert(expDateText);	
		var expDateYear = expDateText.substring(3);
		var expDateMonth = expDateText.substring(0,2);
		var expDate = new Date();
		var daysInM = daysInMonth(expDateMonth, expDateYear);
		expDate.setFullYear(expDateYear, expDateMonth-1, daysInM);
		var today = new Date();
		if (today > expDate){
			$(this).text($(this).find('span').text() +  '**');
			$(this).css('color', 'red').css('font-weight', 'bold');	
		}								
	});					
}	
*/

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

//alert("Radio Invoice: " rdioInv);
//alert("Radio CC: " + rdioCC);

  function toggleCCRadio(rdioBtn) {
	  
	if (rdioInv) {
    	  //alert("Radio CC Before: " + rdioCC);
       var myFrm = document.getElementById('frm');
       $("#creditCartSelectedBlock").show();
        myFrm.paymentType.value = "credit-card";
       rdioBtn.checked = true; 
       rdioCC = true;
       rdioInv = false;
       var invR = document.getElementById("invSelect");
       invR.checked = false;
       var reqdBodyStyle = getStyleObject("req1");
       var testBodyStyle = getStyleObject("test");
       var cTypeBodyStyle = getStyleObject("test2");
       
              
       cTypeBodyStyle.visibility == "";
       cTypeBodyStyle.display = "";
                    
       var poNum = document.getElementById("poNum");
       poNum.disabled = true;
       //poNum.fillColor = "red";
       
       var poNum = document.getElementById("alwaysInvoice");
       poNum.disabled = true;
       
       var poNumStyle = getStyleObject("poNum");
       poNumStyle.backgroundColor = "#CCCCCC";
       
       //cTypeBodyStyle.visibility == "";
       //cTypeBodyStyle.display = "";
       var cInvBodyStyle = getStyleObject("test3");
       cInvBodyStyle.visibility == "hidden";
       cInvBodyStyle.display = "none";
       //alert("Style : " + reqdBodyStyle.visibility);
       reqdBodyStyle.visibility == "";
       reqdBodyStyle.display = "";
       testBodyStyle.visibility == "";
       testBodyStyle.display = "";
       //alert("Radio CC After: " + rdioCC);
       //alert(invR.id);
              
     } else {
       rdioCC = true;
       
     }
	getCreditCards();
	$("#displayContinue").hide();
    window.scrollTo(0,190);
  }
  function toggleInvRadio(rdioBtn) {
	  $("#displayRefCartTotalPriceIdUSD").hide();
	  $("#displayCartTotalPriceIdNonUSD").hide();
	  $("#displayCartTotalPriceIdUSD").show();
	  $("#displayContinue").show();
     if (rdioCC) {
    	 $("#creditCartSelectedBlock").hide();
       //alert("Radio Inv Before: " + rdioInv);
       var myFrm = document.getElementById('frm');
        myFrm.paymentType.value = "invoice";
       rdioBtn.checked = true; 
       rdioInv = true;
       rdioCC = false;
       var ccR = document.getElementById("ccSelect");
       ccR.checked = false;
       ccR.visible = false;
       var reqd = document.getElementById("req1");
       var reqdBodyStyle = getStyleObject("req1");
       var testBodyStyle = getStyleObject("test");
       var cTypeBodyStyle = getStyleObject("test2");
       
       var poNum = document.getElementById("poNum");
       poNum.disabled = false;
       
       var poNum = document.getElementById("alwaysInvoice");
       poNum.disabled = false;
       
       var poNumStyle = getStyleObject("poNum");
       poNumStyle.backgroundColor = "#FFFFFF";
       
       //alert("Visib Info : " + cTypeBodyStyle.visibility);
       //alert("Display Info : " + cTypeBodyStyle.display);
       cTypeBodyStyle.visibility == "hidden";
       cTypeBodyStyle.display = "none";
       var cInvBodyStyle = getStyleObject("test3");
       cInvBodyStyle.visibility == "";
       cInvBodyStyle.display = "";
       //alert("After Visib Info : " + cTypeBodyStyle.visibility);
       //alert("After Display Info : " + cTypeBodyStyle.display);
       //alert("Style : " + reqdBodyStyle.visibility);
       reqdBodyStyle.visibility == "hidden";
       reqdBodyStyle.display = "none";
       testBodyStyle.visibility == "hidden";
       testBodyStyle.display = "none";
       //alert("Required: " + reqd.value);
       //reqd.visible = false;
       //document.getElementById("req1").style.visible = false;
       //frm.creditCardType.visible = false;
       //alert("Radio Inv After: " + rdioInv);
       //alert(ccR.id);
     } else {
       rdioInv = true;
     }
  }
  
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

function sbmtPage() {
  var myFrm = document.getElementById('frm');
  if(myFrm.paymentType.value == "credit-card"){
	  if($('#frm_paymentProfileId').val() == 'NONE'){
		  $("#displayContinue").hide();
		  $.prompt(
					'You must select a specific credit card to place your order.<br>Please make a selection.',
					{
						buttons:{Ok:true},
						prefix:'cleanblue'
					}
				);
		}else{
	  		myFrm.submit();
		}
	  
  }else{
	  myFrm.submit();
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

<html:form action="selectCoiPaymentType.do?operation=reviewCart" method="post" styleId="frm">
<input type="hidden" name="page" value="1">
<html:hidden name="selectPaymentActionFormCOI" property="userName"/>
<html:hidden name="selectPaymentActionFormCOI" property="company"/>
<html:hidden name="selectPaymentActionFormCOI" property="email"/>
<html:hidden name="selectPaymentActionFormCOI" property="phone"/>
<html:hidden name="selectPaymentActionFormCOI" property="address1"/>
<html:hidden name="selectPaymentActionFormCOI" property="address2"/>
<html:hidden name="selectPaymentActionFormCOI" property="city"/>
<html:hidden name="selectPaymentActionFormCOI" property="state"/>
<html:hidden name="selectPaymentActionFormCOI" property="zip"/>
<html:hidden name="selectPaymentActionFormCOI" property="country"/>
<html:hidden name="selectPaymentActionFormCOI" property="hasSpecialOrders"/>
<html:hidden name="selectPaymentActionFormCOI" property="cartTotal"/>
<html:hidden name="selectPaymentActionFormCOI" property="cartChargeTotal"/>
<html:hidden name="selectPaymentActionFormCOI" property="status" styleId="status2" />
<html:hidden name="selectPaymentActionFormCOI" property="paymentType" styleId="payType" />
<html:hidden name="selectPaymentActionFormCOI" property="alwaysInvoiceFlag" styleId="alwaysInvoiceFlag" />
<html:hidden name="selectPaymentActionFormCOI" property="canOnlyBeInvoiced" styleId="canOnlyBeInvoiced" />

<html:hidden styleId="frm_cardType" value="NONE" name="selectPaymentActionFormCOI" property="creditCardType" />
<html:hidden styleId="frm_paymentProfileId" value="NONE" name="selectPaymentActionFormCOI" property="paymentProfileId" />
<html:hidden styleId="frm_lastFourDigits" value="NONE" name="selectPaymentActionFormCOI" property="creditCardNumber" />
<html:hidden styleId="frm_cardholderName" value="NONE" name="selectPaymentActionFormCOI" property="creditCardNameOn" />
<html:hidden styleId="frm_expirationDate" value="NONE" name="selectPaymentActionFormCOI" property="expirationDate" />
<html:hidden styleId="frm_cccPaymentProfileId" value="NONE" name="selectPaymentActionFormCOI" property="cccPaymentProfileId" />

<!-- Begin Progress -->
<div id="ecom-progress"><html:img src="/media/images/ecomprogress-payment-on.gif" alt="Payment" width="60" height="24" /><html:img src="/media/images/ecomprogress-review.gif" alt="Review" width="52" height="24" /><html:img src="/media/images/ecomprogress-confirmation.gif" alt="Confirmation" width="92" height="24" /></div>

<!-- Begin Left Content -->
<div id="ecom-boxcontent">
<div id="cartOrderTotal" style="height:50px;position:relative;width:694px;">
<div class="floatleft">
<h1>Step 1: Enter Payment Information<span class="subtitle"><a href="/content/cc3/en/toolbar/productsAndSolutions/payPerUsePermissionServices/credit_and_paymentpolicy.html" target="_blank"><u>Review credit and payment policy</u></a></span></h1>

<!-- <a href="#" class="back-link">Cancel and return to cart </a> -->
</div>

<logic:equal name="selectPaymentActionFormCOI" property="hasOnlyNonPricedOrders" value="false">


<logic:equal value="true" name="selectPaymentActionFormCOI"  property="displayChargeTotal">
<span class="orderPriceTotal">
<h2 >
Order Total:	<span class="price"><bean:write name="selectPaymentActionFormCOI" property="cartTotalWithNoDollarSign" /> USD</span>
									<logic:equal name="selectPaymentActionFormCOI" property="showExcludeTBDItemText" value="true">
								    <br/><span class="smalltype defaultweight">(Excludes TBD items)</span>
								    </logic:equal>

<div id="creditCartSelectedBlock" style="width:100%;display:none">

									Charge Total:	<span id="displayCartTotalPriceIdUSD" ><bean:write name="selectPaymentActionFormCOI" property="cartChargeTotalWithNoDollarSign" /> USD</span>
									<span style="display: none;" id="displayCartTotalPriceIdNonUSD" class="price"><bean:write name="selectPaymentActionFormCOI" property="cartChargeTotal" /></span><br/>
									<span style="display: none;" id="displayRefCartTotalPriceIdUSD" >(<bean:write name="selectPaymentActionFormCOI" property="cartChargeTotalWithNoDollarSign" /> USD)</span><br/>

</h2>
</div>

</span>
</logic:equal>

<logic:equal value="false" name="selectPaymentActionFormCOI"  property="displayChargeTotal">
<br/>
<span class="orderPriceTotal" style="width:400px">
<h2>Order Total:	<span id="displayCartTotalPriceIdUSD" ><bean:write name="selectPaymentActionFormCOI" property="cartTotalWithNoDollarSign" /> USD</span>
								<span style="display: none;" id="displayCartTotalPriceIdNonUSD" ><bean:write name="selectPaymentActionFormCOI" property="cartTotal" /></span><br/>
								<logic:equal name="selectPaymentActionFormCOI" property="showExcludeTBDItemText" value="true">
								    <span   class="smalltype defaultweight">(Excludes TBD items)</span><br/>
								    </logic:equal>
									
								<span style="display: none;" id="displayRefCartTotalPriceIdUSD" >(<bean:write name="selectPaymentActionFormCOI" property="cartTotalWithNoDollarSign" /> USD)</span><br />
								    </h2>
</span>
<div id="creditCartSelectedBlock" style="display:none"></div>

</logic:equal>


</logic:equal>

<logic:equal name="selectPaymentActionFormCOI" property="hasOnlyNonPricedOrders" value="true">
<span class="orderPriceTotal"><h2>Order Total: <span class="largeType">$TBD</span><br /></h2>
</span>
</logic:equal>
</div>
<div class="horiz-rule"></div>

<a href="cart.do" class="back-link" id="998">Cancel and return to cart </a>

<logic:equal name="selectPaymentActionFormCOI" property="canOnlyBeInvoiced" value="false">
  <span class="largertype bold">Billing Method</span>
<table border="0" cellpadding="0" cellspacing="0" class="indent-1">
  <tr>
    <td><br />If you choose to be invoiced, you can change or cancel your order until the invoice is sent. <br />
    If you pay by credit card, your order will be finalized and your card will be charged within 24 hours. <br /></td>
  </tr>
  
</table>

<div class="horiz-rule"></div>
</logic:equal>

<br></br>

<logic:equal name="selectPaymentActionFormCOI" property="hasOnlySpecialOrders" value="true">
<br></br>
<span class="largertype bold">Pay by invoice options:</span>
</logic:equal>

<logic:equal name="selectPaymentActionFormCOI" property="canOnlyBeInvoiced" value="false">

<logic:equal name="selectPaymentActionFormCOI" property="cybersourceSiteUp" value="true">
<p class="indent-1"><br /> 
  <!-- <input name="radiobutton" type="radio" value="radiobutton" /> -->
  <html:radio name="selectPaymentActionFormCOI" styleId="ccSelect" property="paymentMethodRadioButton" value="credit-card" onclick="toggleCCRadio(this);" />
  <strong>Pay with a Credit Card</strong>
  <span id="req1" class="smalltype icon-alert" style="position: relative; bottom: 15px; left: 175px;">Special Orders in your order may still be invoiced.<util:contextualHelp helpId="45" rollover="true">&nbsp;&nbsp;More info</util:contextualHelp></span> <!--<span id="req1" class="required">*Required</span>--> 
</p>
</logic:equal>
<logic:equal name="selectPaymentActionFormCOI" property="cybersourceSiteUp" value="false">
<p class="indent-1"><br /> 
  <html:radio name="selectPaymentActionFormCOI" styleId="ccSelect" property="paymentMethodRadioButton" value="credit-card" onclick="toggleCCRadio(this);" disabled="true" />
  <strong><font color="red">We're sorry, the credit card service is currently unavailable. Please try again later.</font></strong>
</p>
</logic:equal>

<table border="0" cellpadding="0" cellspacing="0" class="indent-1">
  <tr>
    <td width="239">      
    <!-- <input name="radiobutton" type="radio" value="radiobutton" checked /> -->
    <html:radio name="selectPaymentActionFormCOI" styleId="invSelect" property="paymentMethodRadioButton" value="invoice" onclick="toggleInvRadio(this);" />
      <strong>Pay by invoice </strong><span class="importanttype"></span></td>
    <td>PO #: (optional)
        <html:text name="selectPaymentActionFormCOI" styleId="poNum" property="purchaseOrderNumber" size="30" maxlength="50" />
      </td>
  </tr>
  <tr>
    <td colspan="2"><div class="indent-2 smalltype">
    <!-- <input type="checkbox" name="checkbox" value="checkbox" /> -->
    <logic:equal name="selectPaymentActionFormCOI" property="alwaysInvoiceFlag" value="true">
    <html:checkbox name="selectPaymentActionFormCOI" styleId="alwaysInvoice" property="alwaysInvoice" value="true" onclick="ClickCheckBox(this)" />
    </logic:equal>
    <logic:equal name="selectPaymentActionFormCOI" property="alwaysInvoiceFlag" value="false">
    <html:checkbox name="selectPaymentActionFormCOI" styleId="alwaysInvoice" property="alwaysInvoice" onclick="ClickCheckBox(this)" />
    </logic:equal>
      Always invoice me and skip this step for future orders 
      <br>(You can change this billing preference in <a href="manageAccount.do"><u>Manage Account</u></a>)</div> </td>
    </tr>
</table>
</logic:equal>


<logic:equal name="selectPaymentActionFormCOI" property="canOnlyBeInvoiced" value="true">
<br></br>
<table border="0" cellpadding="0" cellspacing="0" class="indent-1">

  <tr>
    <td>PO #: (optional)
        <html:text name="selectPaymentActionFormCOI" styleId="poNum" property="purchaseOrderNumber" size="30" maxlength="50" />
      </td>
  </tr>
  <tr>
    <td colspan="2"><div class="indent-2 smalltype">
    
    <logic:equal name="selectPaymentActionFormCOI" property="alwaysInvoiceFlag" value="true">
    <html:checkbox name="selectPaymentActionFormCOI" styleId="alwaysInvoice" property="alwaysInvoice" value="true" onclick="ClickCheckBox(this)" />
    </logic:equal>
    <logic:equal name="selectPaymentActionFormCOI" property="alwaysInvoiceFlag" value="false">
    <html:checkbox name="selectPaymentActionFormCOI" styleId="alwaysInvoice" property="alwaysInvoice" onclick="ClickCheckBox(this)" />
    </logic:equal>
      Always invoice me and skip this step for future orders </div>
      	<logic:equal name="selectPaymentActionFormCOI" property="teleSalesUp" value="true">
      (You can change this billing preference in <a href="manageAccount.do"><u>Manage Account</u></a>)</logic:equal></td>
    </tr>
</table>

</logic:equal>
<br />

<table border="0" cellpadding="0" cellspacing="0" class="indent-1" id="test3">

<tr>

<logic:equal name="selectPaymentActionFormCOI" property="userChannel" value="ORGADD">
<td><br><strong>Billing address:</strong>
	Please contact your account administrator to edit the billing address. <br>Don't know your account administrator? <a href="mailto:info@copyright.com" ><u>Contact us</u></a> </td>
</logic:equal>

<logic:notEqual name="selectPaymentActionFormCOI" property="userChannel" value="ORGADD">
  <logic:equal name="selectPaymentActionFormCOI" property="teleSalesUp" value="true">
  <logic:notEmpty name="selectPaymentActionFormCOI" property="billToContactName">
    <td><br><strong>Billing address:</strong>
        Edit in <a href="manageAccount.do" ><u>Manage Account</u></a> </td>
  </logic:notEmpty>
  </logic:equal>
</logic:notEqual>


</tr>
<tr>
<td>
<logic:equal name="selectPaymentActionFormCOI" property="teleSalesUp" value="true">
<br/>
<table border="0" cellpadding="0" cellspacing="0" class="indent-2">
<logic:notEmpty name="selectPaymentActionFormCOI" property="billToContactName">
<tr><td><bean:write name="selectPaymentActionFormCOI" property="billToContactName"/></td></tr>
</logic:notEmpty>
<logic:notEmpty name="selectPaymentActionFormCOI" property="company">
    <tr><td><bean:write name="selectPaymentActionFormCOI" property="company"/></td></tr>
</logic:notEmpty>
<tr><td><bean:write name="selectPaymentActionFormCOI" property="address1"/></td></tr>
<logic:notEmpty name="selectPaymentActionFormCOI" property="address2">
<tr><td><bean:write name="selectPaymentActionFormCOI" property="address2"/></td></tr>
</logic:notEmpty>
<tr><td><bean:write name="selectPaymentActionFormCOI" property="city"/>,
<bean:write name="selectPaymentActionFormCOI" property="state"/>
<bean:write name="selectPaymentActionFormCOI" property="zip"/></td></tr>
<logic:notEmpty name="selectPaymentActionFormCOI" property="country">
    <tr><td><bean:write name="selectPaymentActionFormCOI" property="country"/></td></tr>
</logic:notEmpty>
<logic:notEmpty name="selectPaymentActionFormCOI" property="billToEmail">
    <tr><td><bean:write name="selectPaymentActionFormCOI" property="billToEmail"/></td></tr>
</logic:notEmpty>
<logic:notEmpty name="selectPaymentActionFormCOI" property="billToPhone">
    <tr><td><bean:write name="selectPaymentActionFormCOI" property="billToPhone"/></td></tr>
</logic:notEmpty>

</table>

</logic:equal>

</td>

</tr>

</table>


<logic:equal name="selectPaymentActionFormCOI" property="canOnlyBeInvoiced" value="false">
<logic:equal name="selectPaymentActionFormCOI" property="cybersourceSiteUp" value="true">
<table border="0" class="indent-2" id="test2">
  <tr>
    <td><b>Select Currency:</b><span id="test" class="importanttype">*</span>
    <html:select name="selectPaymentActionFormCOI" property="currencyType" styleId="cType" onchange ="getSelectedCurrency(this.value);">
    	<html:option value="USD">USD - $</html:option>
    	<html:option value="EUR">EUR - &#8364;</html:option>
    	<html:option value="GBP">GBP - &#163;</html:option>
    	<html:option value="JPY">JPY - &#165;</html:option>
    	<html:option value="CAD">CAD - $</html:option>
        <html:option value="CHF">CHF - &#8355;</html:option>
    </html:select>&nbsp;&nbsp;
    
    <logic:equal value="true" name="selectPaymentActionFormCOI"  property="displayChargeTotal">
        <b>Charge Total:</b>&nbsp;
		<span id="displayTotalPriceIdUSD" class="price"><bean:write name="selectPaymentActionFormCOI" property="cartChargeTotalWithNoDollarSign" /> USD</span>
	    <span style="display: none;" id="displayTotalPriceIdNonUSD" class="price"><bean:write name="selectPaymentActionFormCOI" property="cartChargeTotal" /></span>
	    </logic:equal>
	    <logic:equal value="false" name="selectPaymentActionFormCOI"  property="displayChargeTotal">
	    <b>Order Total:</b>&nbsp;
		<span id="displayTotalPriceIdUSD" class="price"><bean:write name="selectPaymentActionFormCOI" property="cartTotalWithNoDollarSign" /> USD</span>
	    <span style="display: none;" id="displayTotalPriceIdNonUSD" class="price"><bean:write name="selectPaymentActionFormCOI" property="cartTotal" /></span>
	    </logic:equal>
	    	<logic:equal name="selectPaymentActionFormCOI" property="showExcludeTBDItemText" value="true">
								    <span class="smalltype defaultweight">(Excludes TBD items)</span>
			</logic:equal>
	    
    </td></tr>
      
  <tr></tr>
  <tr></tr>
  <tr></tr>
  <tr>
  	<td colspan="6"><b>Select Credit Card:</b><span id="test" class="importanttype"></span></td>
  </tr>
  <tr>
  	<td colspan="2" class="DefaultLabel">
  		<!-- redundant code removed -->
  		<div id="ccRows" class="ccRows">
        	<table width="640" border="0" frame="box" rules="none" cellpadding="3" cellspacing="0" class="striped" id="ccTable">
                <tr>
                    <td height="25" colspan="2" class="PaymentHeaderCell"><p class="LayerLink">&nbsp;</p></td>
                    <td height="25" colspan="2" class="PaymentHeaderCell"><p class="LayerLink"><font color="white">Card Type</font></p></td>
                    <td width="92" height="25" align="left" class="PaymentHeaderCell"><p class="LayerLink"><font color="white">Card No.</font></p></td>
                    <td width="175" height="25" align="left" class="PaymentHeaderCell"><p class="LayerLink"><font color="white">Cardholder's Name</font></p></td>
                    <td width="88" height="25" align="left" class="PaymentHeaderCell"><p class="LayerLink"><font color="white">Exp. Date</font></p></td>
                    <td width="118" height="25" class="PaymentHeaderCell"><p class="LayerLink">&nbsp;</p></td>
                </tr>
                     
            <tr>
            <td height="25" colspan="2">&nbsp;</td>
            <td height="25" colspan="2">&nbsp;</td>
			<td height="25">&nbsp;</td>
            <td height="25" colspan="3" align="right"><input type="button" id="addCardButton" style="font-size: 10px; font-weight: bold;" value="Add new card"/></td>
            </tr>
                
              </table>
              </div>
  	</td>
  </tr>
              <tr>
              	
              	<td colspan="2" class="DefaultLabel" valign="top">
              		<div id="hop">
              		              		
        			<table width="640" cellpadding="3" cellspacing="0" border="1" frame="box" rules="none" >
						<tr>
            				<td valign="top">
                   	  			<iframe name="theHOPIFrame" width="600" height="490" id="theHOPIFrame" frameBorder="No" scrolling ="yes"></iframe>
                			</td>
            			</tr>
       	  				<tr>
       						<td align="right" valign="top"><input type="button" value="Cancel" id="cancelButton"/>
              					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>   
        				</tr>
        			</table>
      				</div>
               	</td>
              </tr>
</table>
</logic:equal>

</logic:equal>
  

<p>&nbsp;</p>
<div class="horiz-rule"></div>
<div class="txtRight" style="display: block;" id="displayContinue">
<html:img src="/media/images/btn_continue.gif" alt="Continue" width="75" height="18" onclick="sbmtPage();" styleId="999" /></div>

</div>

<!-- *************************************************************** -->

<div class="clearer"></div>


<!-- <div id="bottomcorners"></div> -->

<!-- End Main Content --> 
</html:form>

<!-- start of hosted order page Content -->
<%@ include file="hostedOrderPage.jsp"%>
<logic:notEmpty name="selectPaymentActionFormCOI" property="hopURL">
	<bean:define id="hopURL" name="selectPaymentActionFormCOI" property="hopURL"/>
	<bean:define id="signature" name="selectPaymentActionFormCOI" property="signature"/>
	<bean:define id="subscriptionSignature" name="selectPaymentActionFormCOI" property="subscriptionSignature"/>
	<bean:define id="URL" name="selectPaymentActionFormCOI" property="cccURL"/>
	<bean:define id="userid" name="selectPaymentActionFormCOI" property="userName"/>
	<bean:define id="responseEmail" name="selectPaymentActionFormCOI" property="responseEmail"/>
	    
</logic:notEmpty>


<form action='${hopURL}' method="post" target="theHOPIFrame" id="theHOPDriverForm" name="theHOPDriverForm">
					<%= insertSignature("1.00", "usd", "subscription") %>
                  	<%= insertSubscriptionSignature("0","20190731","on-demand","0","false") %>
                  	<input type="hidden" name="orderPage_receiptResponseURL" value="${URL}" />
                    <input type="hidden" name="orderPage_declineResponseURL" value="${URL}" />
                    <input type="hidden" name="billTo_firstName" value="" />
                    <input type="hidden" name="billTo_lastName" value="" />
                    <input type="hidden" name="billTo_street1" value="" />
                    <input type="hidden" name="billTo_street2" value="" />
                    <input type="hidden" name="billTo_city" value="" />
                    <input type="hidden" name="billTo_state" value="" />
                    <input type="hidden" name="billTo_postalCode" value="" />                   
                    <input type="hidden" name="billTo_customerID" value='${userid}' />                   
                    <input type="hidden" name="billTo_country" value="" />
	                <input type="hidden"  name="userid" value='${userid}' />
                    <input type="hidden" name="orderPage_merchantEmailAddress" value='${responseEmail}' />
                    <input type="hidden" name="orderPage_merchantEmailPostAddress" value='${responseEmail}' />
                    <input type="hidden" name="billTo_phoneNumber" value=" " />
                    <input type="hidden" name="billTo_email" value=" " />
                    
</form>

<form action='${hopURL}' method="post" target="theHOPIFrame" id="theHOPUpdateDriverForm" name="theHOPUpdateDriverForm">
					<%= insertSignature("1.00", "usd", "subscription_modify") %>
   					<div id="modifySignatureOutput"></div>
   					<input type="hidden" name="orderPage_receiptResponseURL" value="${URL}" />
                    <input type="hidden" name="orderPage_declineResponseURL" value="${URL}" />
   					<input type="hidden" name="orderPage_merchantEmailAddress" value='${responseEmail}' />
                    <input type="hidden" name="orderPage_merchantEmailPostAddress" value='${responseEmail}' />
                    <input type="hidden" name="billTo_email" value=" " />
</form>

<script type="text/javascript">

  window.onload = initPage;

  function initPage() {
  
        //This code was added by me 11/14/2006
        /* if (selectPaymentActionForm.status.value == "new")
        {
            selectPaymentActionForm.status.value = "reSubmit";
            rdioCC = true;
            toggleInvRadio(document.getElementById("invSelect"));
        } */
        
        var frmOnlySpecial = document.getElementById("canOnlyBeInvoiced");
        
        if (frmOnlySpecial.value == "false")
        {
        
            var frmStatus = document.getElementById("status2");
            var frmPayType = document.getElementById("payType");          
<%
//  MSJ - Added code to force credit card payment when requested.

if ("CC".equals(UserContextService.getUserContext().getMethodOfPmtRestrictor())) {
%>
    <%= "rdioInv = false;" %>
    <%= "frmPayType.value = \"credit-card\";" %>
    <%= "toggleCCRadio(document.getElementById(\"ccSelect\"));" %>
    <%= "document.getElementById(\"invSelect\").disabled = true;" %>
    <%= "document.getElementById(\"alwaysInvoiceFlag\").value = \"F\";" %>
<%
}
else {
%>
            //alert("Status Before: " + frmStatus.value);
            //alert("Pay Type Before: " + frmPayType.value);
        
            if (frmStatus.value != "reSubmit")
            {
                //alert("First Time");
                //alert("Status before: " + frmStatus.value);
                frmStatus.value = "reSubmit";
                //alert("Status after: " + frmStatus.value);
                rdioCC = true;
                toggleInvRadio(document.getElementById("invSelect"));
        
            }
<%
}
%>
        
                
            if (frmStatus.value == "reSubmit")
            {
            //alert("Resubmit");
            //var frmPmtRadioBtn = document.getElementById("paymentMethodRadioButton");
            
            //alert("Payment Type: " + frmPayType.value);
            if (frmPayType.value == "credit-card")            
                {
                    rdioInv = true;
                    toggleCCRadio(document.getElementById("ccSelect"));
                }
                
            if (frmPayType.value == "invoice")            
                {
                    rdioCC = true;
                    toggleInvRadio(document.getElementById("invSelect"));
                }
            }
            
        }
        
        
  }

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
</script>

<!-- Webtrends tags for capturing scenarios -->
<META name="WT.si_n" content="Checkout">
<META name="WT.si_x" content="6">
<!-- end Webtrends tags -->

