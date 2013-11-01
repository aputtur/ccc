<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/tld/struts-logic.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/tld/struts-bean.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/cc2-util.tld" %>

<%@ page errorPage="/jspError.do" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="com.copyright.ccc.business.data.RLinkPublisher" %>
<%@ page import="com.copyright.ccc.business.services.user.RLinkPublisherServices" %>
<%@ page import="com.copyright.ccc.web.transaction.RepublicationConstants" %>
<%@ page import="com.copyright.rightslink.html.data.HtmlScreenResponse" %>
<meta http-equiv="Page-Enter" content="blendTrans(Duration=0)">
<meta http-equiv="Page-Exit" content="blendTrans(Duration=0)">

<!-- end bean declarations -->
<script src="<html:rewrite page="/resources/commerce/js/util.js"/>" type="text/javascript"></script>
<script src="<html:rewrite page="/resources/commerce/js/contentLoader.js"/>" type="text/javascript"></script>
<script src="<html:rewrite page="/resources/commerce/js/priceCalculator.js"/>" type="text/javascript"></script>
<script src="<html:rewrite page="/resources/commerce/js/validators.js"/>" type="text/javascript"></script>
<script src="<html:rewrite page="/resources/commerce/js/dateUtils.js"/>" type="text/javascript"></script>
<link href="<html:rewrite page="/resources/commerce/css/contextualHelp.css"/>" rel="stylesheet" type="text/css" />
<link href="<html:rewrite page="/resources/commerce/css/rightslink_font.css"/>" rel="stylesheet" type="text/css" />

<script language="javascript">
if (browserInfo.isIE){
	document.write ("<link rel=\"stylesheet\" type=\"text/css\" href=\"/resources/commerce/css/rightslink_layout_msie.css\">");
}
else{ 
	document.write ("<link rel=\"stylesheet\" type=\"text/css\" href=\"/resources/commerce/css/rightslink_layout_ns6.css\">");
	}
	
</script>

<style>
#ecom-content-wrapper{
padding-bottom: 0px;
}
.DefaultButtons {
text-align:right;
padding-bottom: 2px;


}
.backButtonReAlign{
margin-bottom: 3px;
}
.DarkCellCenter {
vertical-align:middle !important;
		}
.LightCellCenter  {
vertical-align:middle !important;
		}
p{margin:6px 0 !important;}
.EmphasisNote{color:#000000;}
.ErrorCell .EmphasisNote{color:#CC3333}
@-moz-document url-prefix(){
p:empty {display: none;}
}
</style>

<jsp:useBean id="rightsLinkQuickPriceActionForm" scope="session" class="com.copyright.ccc.web.forms.RightsLinkQuickPriceActionForm"/>
<bean:define id="htmlScreenResponseObject"  type="com.copyright.rightslink.html.data.HtmlScreenResponse" name="rightsLinkQuickPriceActionForm" property="htmlScreenResponse"></bean:define>

<logic:notEmpty name="rightsLinkQuickPriceActionForm" property="htmlScreenResponse">

	<logic:notEmpty name="rightsLinkQuickPriceActionForm" property="htmlScreenResponse">
		<bean:write name="htmlScreenResponseObject" property="cssLinkReferenceHtml" filter="false"/>
	</logic:notEmpty>
	<logic:notEmpty name="htmlScreenResponseObject" property="scriptVarDeclarationHtml">
		<bean:write name="htmlScreenResponseObject" property="scriptVarDeclarationHtml" filter="false"/>
	</logic:notEmpty>
	<logic:notEmpty name="htmlScreenResponseObject" property="scriptDataHtml">
		<bean:write name="htmlScreenResponseObject" property="scriptDataHtml" filter="false"/>
	</logic:notEmpty>
</logic:notEmpty>






<logic:equal name="rightsLinkQuickPriceActionForm" property="isUpdateableCartItem" value="true">
<h1>Edit Item</h1>
<a class="icon-back" href="/cancelEdit.do">Cancel changes</a>
<div id="footerDivider" class="horiz-rule"></div>
</logic:equal>
<div id="formBlock">
<html:form action="/rlQuickPrice.do?operation=refreshPrice"  styleId="TheForm"   >
   <html:hidden property="readyForCart" styleId="readyForCart" name="rightsLinkQuickPriceActionForm" />
   <html:hidden property="selectedPubItemWrkInst" styleId="selectedPubItemWrkInst" name="rightsLinkQuickPriceActionForm" />
   
  <logic:notEmpty name="rightsLinkQuickPriceActionForm" property="selectedPubItem">
  <div class="item-details">
		  <tiles:insert page="/WEB-INF/jsp-modules/transaction/article-search/publication.jsp"/>
		  </div>
		 <div class="horiz-rule"></div>
		 <div style="clear:both">
		 	<div style="float:left;width:175px"><strong>Permission type selected:</strong></div>
		 	<div style="float:left;"> <span class="normaltype"><bean:write name="rightsLinkQuickPriceActionForm" property="selectedCategory"/></span></div>
		 </div>
		 <div class="clearer"></div>
		 <div style="padding-bottom: 10px;"></div>
		 <div style="clear:both">
		 	<div style="float:left;width:175px"><strong>Type of use selected:</strong></div>
		 	<div style="float:left;"> <span class="normaltype"><bean:write name="rightsLinkQuickPriceActionForm" property="selectedTou" /></span></div>
		 </div>
		 <div class="clearer"></div>
		 <div style="padding-bottom: 10px;"></div>
		 <logic:equal name="rightsLinkQuickPriceActionForm" property="rlinkSpecialOrderFromScratch" value="true">
                <html:link page="/search.do?operation=addSpecialFromScratch" styleClass="icon-back">Select different permission </html:link>
		     <div style="padding-bottom: 10px;"></div>
		 </logic:equal>
		 <logic:equal name="rightsLinkQuickPriceActionForm" property="rlinkSpecialOrderFromScratch" value="false">
		 	<logic:equal name="rightsLinkQuickPriceActionForm" property="isUpdateableCartItem" value="false">
                <html:link page="/search.do?operation=detail&detailType=basic" styleClass="icon-back" paramId="item" paramName="rightsLinkQuickPriceActionForm" paramProperty="selectedPubItemWrkInst">Select different permission </html:link>
		     	<div style="padding-bottom: 10px;"></div>
		 	</logic:equal>
		 </logic:equal>
		 
		 <!--  Article -->
		 <logic:notEmpty name="rightsLinkQuickPriceActionForm" property="selectedArticle">
			<tiles:insert
				page="/WEB-INF/jsp-modules/transaction/article-search/article.jsp" />

			<div style="padding-bottom: 10px;"></div>
			<logic:equal name="rightsLinkQuickPriceActionForm" property="isUpdateableCartItem" value="false">
				
				<a href="/articleSearch.do?operation=articleSearch&item=<bean:write name='rightsLinkQuickPriceActionForm' property='selectedPubItemWrkInst'/>"
					class="icon-back">Select different article </a>
					<br></br>
				
			</logic:equal>
		</logic:notEmpty>
		  
		  <!--  CHAPTER -->
		   <logic:notEmpty name="rightsLinkQuickPriceActionForm" property="selectedChapter">
			  <tiles:insert page="/WEB-INF/jsp-modules/transaction/article-search/chapter.jsp"/>
			  <div style="padding-bottom: 10px;"></div>
			 	<logic:equal name="rightsLinkQuickPriceActionForm" property="isUpdateableCartItem" value="false">
			    	<html:link page="/chapterSearch.do?operation=chapterSearch" styleClass="icon-back">Select different chapter </html:link>
			    	  
		
			    </logic:equal>
		  </logic:notEmpty>
		  
		<logic:equal name="rightsLinkQuickPriceActionForm" property="orderExists" value="true">
			<div style="padding-bottom: 10px;"></div>
            <div style="width:100%" align="center">
                <div class="calloutbox" style="background-color: #FFFFD6;width:80%" align="left">
                    <p class="smalltype icon-alert">
                       The type of use selected cannot be added to your existing order. Please continue through the order process to create a new order for this title and type of use.
                    </p>
                </div>
                
            </div>
        </logic:equal>
  
        <logic:equal name="rightsLinkQuickPriceActionForm" property="isUpdateableCartItem" value="true">
        		
            <div style="width:100%" align="center">
                <div class="calloutbox" style="background-color: #FFFFD6;width:80%" align="left">
                    <p class="smalltype icon-alert">
                       Please note that any changes you make to one item in your cart may affect the price or permission status of other items.
                       <bean:define id="editCartTitle">Edit cart item</bean:define>
                       <bean:define id="editCartBody">Some rights holders base their prices and permission availability on the number of requests for a work or group of works. If your changes affect the price or permission status of other items in your cart, you will see the updates in your shopping cart.</bean:define>
                       <util:contextualHelp bodyName="editCartBody" rollover="true">More...</util:contextualHelp>
                    </p>
                </div>
                <div style="padding-bottom: 10px;"></div>
                
            </div>
        </logic:equal>
     
     
 
    
<logic:notEmpty name="rightsLinkQuickPriceActionForm" property="quickPriceErrorMessage">
<div style="padding-bottom: 10px;"></div>
<div  class="callout-light indent-1">
<bean:write  name="rightsLinkQuickPriceActionForm" property="quickPriceErrorMessage"  filter="false"/>
</div>
</logic:notEmpty>

<logic:empty name="rightsLinkQuickPriceActionForm" property="quickPriceErrorMessage">
    	
		<logic:notEmpty name="rightsLinkQuickPriceActionForm" property="htmlScreenResponse">
		
		<logic:notEmpty name="rightsLinkQuickPriceActionForm" property="displayPrice" >   			 
		   		<table style="width: 100%;">
		    			<tr><td style="align:right">
		    				 <span  class="floatright" id="priceSection">
		   			 	<strong>Total Price: </strong><span class="price" id="price"><bean:write name="rightsLinkQuickPriceActionForm"  property="displayPrice"/></span>
		   			        </span>
		         	   </td>
		       		 </tr>
		       		 </table>
		</logic:notEmpty>
		
				
		<logic:empty name="rightsLinkQuickPriceActionForm" property="displayPrice" >
		       		 			<div style="padding-bottom: 10px;"></div>
			</logic:empty>		
					<logic:notEmpty name="htmlScreenResponseObject" property="hiddenVariableHtml">
						<bean:write name="htmlScreenResponseObject" property="hiddenVariableHtml" filter="false"  />
					</logic:notEmpty>		    
		    <div id="RightsLinkQuickPriceDisplayBlock" class="callout-light indent-1">
		 
		<logic:equal name="rightsLinkQuickPriceActionForm" property="repub" value="true">
		    
			<table style="width: 100%;">
		    			<tr><td style="align:right">
		    				 <span  class="floatright" id="priceSection">
		    				 <p class="smalltype icon-alert">
		   			 	Terms and conditions apply to this permission type<br/><a href="reviewTermsRL.do" target="_blank" >View details</a>
		   			 	</p> 
		   			        </span>
		         	   </td>
		       		 </tr>
		       		 
		 	</table>
		 	
		 </logic:equal> 
		 
		 <div id="waiting_for_search" style="display: none;">
       		<div id="progress-widecontent_ppu">
       			<h1 align="center" vertical-align="center">Processing... Please wait... </h1> 
			</div>
		</div>  
		 		              
				<table cellpadding="0" cellspacing="0" border="0" width="640px;" >	
					<logic:notEmpty name="htmlScreenResponseObject" property="screenHtml">
						<bean:write name="htmlScreenResponseObject" property="screenHtml" filter="false"/>
					</logic:notEmpty>
				</table>
			</div>
			
			<logic:notEmpty name="rightsLinkQuickPriceActionForm" property="displayPrice" >   			 
		   		<table style="width: 100%;">
		    			<tr><td style="align:right">
		    				 <span  class="floatright" id="priceSection">
		   			 	<strong>Total Price: </strong><span class="price" id="price"><bean:write name="rightsLinkQuickPriceActionForm"  property="displayPrice"/></span>
		   			        </span>
		         	   </td>
		       		 </tr>
		       		 <logic:equal name="rightsLinkQuickPriceActionForm" property="displayUpdatePriceBtn" value="true">
		       		 <tr><td>
						<span class="floatright" style="margin-top:5px"><a href="javascript:quickPrice(document.forms[rlFormName].offerName, 'quickprice');" id="updatePriceButton"   >Update Price&nbsp;<img src="/resources/commerce/images/refresh-arrow.png"/></a></span>
						</td></tr>
			        </logic:equal>
		       		 </table>
		</logic:notEmpty>
		
		
				
		</logic:notEmpty>
				<logic:equal name="rightsLinkQuickPriceActionForm" property="isUpdateableCartItem" value="true">
					<div style="overflow:hidden;width:100%;">
						<logic:notEmpty name="rightsLinkQuickPriceActionForm" property="rightsLinkButtonHTML">
						 <span style="float:right">
						<table class="RLQuickPriceTable" cellpadding="0" cellspacing="0" border="0" width="700">
						<tr>
						<td style="vertical-align:middle"><a class="icon-back" id="998" href="/cancelEdit.do">Cancel changes</a></td>
						<td>
						<table cellpadding="0" cellspacing="0" border="0" width="550">
						<tr><bean:write name="rightsLinkQuickPriceActionForm" property="rightsLinkButtonHTML" filter="false"/></tr>
						</table>
						</td>
						</tr></table>
						</span>
						</logic:notEmpty>
						</div>
				</logic:equal>
				
				<logic:equal name="rightsLinkQuickPriceActionForm" property="isUpdateableCartItem" value="false">
				<logic:notEmpty name="rightsLinkQuickPriceActionForm" property="rightsLinkButtonHTML">
				 <span style="float:right">
				<table cellpadding="0" cellspacing="0" border="0" width="640">
				<tr><bean:write name="rightsLinkQuickPriceActionForm" property="rightsLinkButtonHTML" filter="false"/></tr>
				</table>
				</span>
				</logic:notEmpty>
				</logic:equal>
			
	</logic:empty>
</logic:notEmpty> 

</html:form>
	 <logic:empty name="rightsLinkQuickPriceActionForm" property="rightsLinkButtonHTML">
	 <logic:equal name="rightsLinkQuickPriceActionForm" property="isUpdateableCartItem" value="true">
				<td style="text-align:center"><div  style="float: left; position: absolute; margin-top: 20px; "><a class="icon-back" id="998" href="/cancelEdit.do">Cancel changes</a></div></td>
				</logic:equal>
	</logic:empty>			
</div>	
        

<logic:notEmpty name="rightsLinkQuickPriceActionForm" property="selectedPubItem">
<script>
// rlqp delegate calls
var disableCartAction=false;
var cartButtonClickCount=0;

//add alt to image . 
//Image won't be ready  with document ready check hence bind on img load
$('img').each(fixImagAlt).bind('load',fixImagAlt);
function fixImagAlt(){
	 // add alt to image
  var img=$(this);
  if (!img.attr("alt") || img.attr("alt") == ""){
 	 if(img.attr("name") && img.attr("src")!="/media/images/spacer.gif"){
 		img.attr("alt", img.attr("name"));
  		}
  	}
}

function onBeforeUpdateShippingMethods(){
$("input#readyForCart").val("false");
updateShippingMethods();
}
function onBeforeAddToCart(){
		$("input#readyForCart").val("true");
		//$(waiting_for_search).show();
		document.getElementById('waiting_for_search').style.display = '';
		continueOrder();

$('a').mousedown(function() {
	$("input#readyForCart").val("false");
});
		
}

/******************************/
/* PREVENT ENTER KEY*/
$(document).ready(function() {
    $("#TheForm").bind("keypress", function(e) {
        if (e.keyCode == 13) {
            return false;
       }
    });
	//hide br tag and add space for field labels
    $(".RegularField br").filter(function () {
        $(this).after("&nbsp;");
    });

    
}); 
/*****************************************************************
 * Attemp to overrride Rightslnk submit event
 *
**********************************************************************/ 
var pageSubmitted=false;
 function OverrideRlnkSubmitCall(event) {
    var target = event ? event.target : this;
    // do anything you like here
    if(pageSubmitted==false){
    	pageSubmitted=true;
    	// call real submit function
    	this._submit();
      }else{
  	  	alert('The transaction is currently processing...');
  	  	return false;
      }
  	}
$(document).ready(function() {
	// capture the onsubmit event on all forms
	if (window.addEventListener){ // W3C standard
	  window.addEventListener('submit', OverrideRlnkSubmitCall, true);
	} 
	else if (window.attachEvent){ // Microsoft{
	  window.attachEvent('submit', OverrideRlnkSubmitCall);
	}
	// If a script calls someForm.submit(), the onsubmit event does not fire,
	// so we need to redefine the submit method of the HTMLFormElement class.
	if(window.HTMLElement){
		/* DOM-compliant Browsers */
		HTMLFormElement.prototype._submit = HTMLFormElement.prototype.submit;
		HTMLFormElement.prototype.submit = OverrideRlnkSubmitCall;
		} else {
		/* IE does not expose it's HTMLElement object or its children
		Let the document load so all forms are accounted for */
			for(var i = 0; i < document.forms.length; i++ ){
				document.forms[i]._submit = document.forms[i].submit;
				document.forms[i].submit = OverrideRlnkSubmitCall;
				}
		}
	 /*****************************
	 */
	
 });
/*****************************************************
 * GO BACK BUT SKIP LOGIN PAGE
 ********************************************************/
function goBack(){
	if (location.href.indexOf("rlQuickPrice.do?operation=processLogin")>0){
		history.go(-2);
	}else{
		history.back();
	}
}


var qsParm = new Array();
$(document).ready(function() {
 scrollToView();
 // added to fixe back button alignment
 var submitImageButton = $("img[src$='submit_button.gif'][name='submitRequest']");
 if(submitImageButton.attr("src")!=undefined){
	 var backImageButton = $("img[src$='btn_back.gif'][name='back']");
	  $(backImageButton).addClass('backButtonReAlign');
 }

});

//scrollToView();
// scroll quick to view 
function scrollToView(){
	qs();
	document.getElementById("RightsLinkQuickPriceDisplayBlock").focus();
	if(qsParm['operation']!=null && qsParm['operation']=='refreshPrice'){
	window.scrollTo(0, document.getElementById("RightsLinkQuickPriceDisplayBlock").offsetTop);
	}
}
// read query string to check for paramter
function qs() {
	var query = window.location.search.substring(1);
	var parms = query.split('&');
	for (var i=0; i<parms.length; i++) {
	var pos = parms[i].indexOf('=');
	if (pos > 0) {
		var key = parms[i].substring(0,pos);
		var val = parms[i].substring(pos+1);
		qsParm[key] = val;
	}
	}
}

</script>

</logic:notEmpty>
<script src="<html:rewrite page="/resources/commerce/js/contextualHelp.js"/>" type="text/javascript"></script>
