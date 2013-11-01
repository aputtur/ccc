<%@ page language="java"%>
<%@ page errorPage="/jspError.do" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/cc2-util.tld" %>
<%@ page import="java.util.List" %>
<%@ page import="com.copyright.ccc.web.util.PermissionCategoryDisplay" %>
<%@ page import="com.copyright.ccc.web.util.PermSummaryTouDisplay" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>

<link href="<html:rewrite page="/resources/commerce/css/perms.css"/>" rel="stylesheet" type="text/css" />

<script type="text/javascript">
//<![CDATA[

var gVarPermissionLevel="";
           
function toggle_vis(e,show,hide) {
    var s = document.getElementById(show);
	var h = document.getElementById(hide);
	var listings = e.parentNode;

	listings = listings.parentNode.getElementsByTagName("li");

	for (var i = 0; i < listings.length; i++) {
		listings[i].className="";
	}
	e.parentNode.className="active";

	if(s.style.display == 'none') {
		s.style.display = 'block';
		h.style.display = 'none';
	}  
}
function setTouId(permissionLevel,tpuInst,rightId,rightsholderInst,rrTou, rrTouId, categoryId, rlPermissionType, offerChannel, rlPubCode)
{
	// set the permission level -added for NRLS
	gVarPermissionLevel=permissionLevel;
	
	//added for pub to pub
	document.getElementById("selectedRlPubCode").value=rlPubCode;
	document.getElementById("selectedRlPermissionType").value=rlPermissionType;
	document.getElementById("selectedOfferChannel").value=offerChannel;
	
	document.getElementById("selectedTpuInst").value=tpuInst;
	document.getElementById("selectedRightInst").value=rightId;
	document.getElementById("selectedRightHolderInst").value=rightsholderInst;
	document.getElementById("selectedRRTou").value=rrTou;
	document.getElementById("selectedRRTouId").value=rrTouId;
	document.getElementById("selectedCategoryId").value=categoryId;
	
}

function addToCart(action,curretCartId,item,idx,perm,wrkInst)
{
	
	 varArticleLevelPermission= 'initArticleSearch.do?operation=initSearchParms';
	 varChapterLevelPermission= 'initChapterSearch.do?operation=initSearchParms';
	 varTitleLevelPermission='search.do?operation=addToCartRlnk';
	 
	 if(action!='' && gVarPermissionLevel!='' && gVarPermissionLevel=='Article level'){
		 action="ArticleLevelPermission";
	 }else if(gVarPermissionLevel!='' && gVarPermissionLevel=='Title level'){
		 action="TitleLevelPermission";
	 }else if(gVarPermissionLevel!='' && gVarPermissionLevel=='Chapter level'){
		 action="ChapterLevelPermission";
	 }
	        		
	if(isCorrectPermissionChecked(wrkInst,curretCartId)){
	// if contact rights holder display message
	if(isContactRightsHolder()){
		showContactRightsholder();
		return false;
	}
	
			var default_action="/search.do?operation=addToCart";
			document.getElementById("selectedPerm").value=perm;
			document.getElementById("selectedIdx").value=idx;
			document.getElementById("selectedItem").value=item;
			if(action=="" || action=="TF"){
				$("#priceOrderForm").attr("action",default_action);
			}else{
				if(action=='ArticleLevelPermission'){
					$("#priceOrderForm").attr("action",varArticleLevelPermission);
				}else if(action=='ChapterLevelPermission'){
					$("#priceOrderForm").attr("action",varChapterLevelPermission);
				}else if(action=='TitleLevelPermission'){
					$("#priceOrderForm").attr("action",varTitleLevelPermission);	
				}
				
				
			}

			$("#priceOrderForm").submit();
		}

	}


function isCorrectPermissionChecked(wrkInst,categoryId){
	var radioBtn="radio_"+wrkInst+"_"+categoryId;
	var obj="input[id='"+radioBtn+"']:checked";
	if(undefined == $(obj).val()){
		alert("Please select a type of use and click Price and Order to continue.");
		return false;
	}else{
		$(obj).click();
		document.getElementById("selectedTou").value=$(obj).val();
	}
	return true;
}
//]]>
</script>

<script type="text/javascript">
//<![CDATA[
function go(lnk) 
{
    openPopup(lnk, "request_coverage", "width=410,height=430,scrollbars,resizable");
}

function toggleGroupDisplay(toggleLinkId, divId)
{
    var passedDivId = document.getElementById(divId);
	var toggleLink = document.getElementById(toggleLinkId);
	var collapse = (toggleLink.className == "collapseArrow");
	
	toggleLink.className = (collapse) ? "expandArrow" : "collapseArrow";

	if (collapse) {
        toggleLink.innerHTML = "View details";
	}
	else {
		toggleLink.innerHTML = "Hide details";
	}
	if (collapse) {
	   $(passedDivId).hide();
    }
	else {
       $(passedDivId).show();
	}

}
function toggleSingleRadioGroupDisplay(toggleLinkId, divId)
{
	var toggleLink = document.getElementById(toggleLinkId);
	var collapse = (toggleLink.className == "collapseArrow");
	
	toggleLink.className = (collapse) ? "expandArrow" : "collapseArrow";
	
	if (collapse) {
        toggleLink.innerHTML = "Select a type of use";
	}
	else {
		toggleLink.innerHTML = "Hide types of use";
	}
	
	var display = (collapse) ? "none" : "";
	var div = document.getElementById(divId);
	div.style.display = display;
}
//]]>
</script>
<jsp:useBean id="searchForm" scope="session" class="com.copyright.ccc.web.forms.SearchForm"/>
<bean:define id="termsTitle">Rightsholder Terms</bean:define>
<bean:define id="emailTitle">Request This Work</bean:define>
<bean:define id="item" name="searchForm" property="selectedItem" />
<bean:define id="wrkInst" name="searchForm" property="selectedItem.wrkInst"/>

<html:form action="/search.do?operation=addToCart" styleId="priceOrderForm">
    <!--  add for pub to pub -->
    <html:hidden name="searchForm" property="selectedRlPermissionType" styleId="selectedRlPermissionType"/>
    <html:hidden name="searchForm" property="selectedOfferChannel" styleId="selectedOfferChannel"/>
    <html:hidden name="searchForm" property="selectedRlPubCode" styleId="selectedRlPubCode"/>
    <!-- -->
    <html:hidden name="searchForm" property="selectedRightInst" styleId="selectedRightInst"/>
    <html:hidden name="searchForm" property="selectedTpuInst" styleId="selectedTpuInst"/>
    <html:hidden name="searchForm" property="selectedRightHolderInst" styleId="selectedRightHolderInst"/>
    <html:hidden name="searchForm" property="selectedRRTou" styleId="selectedRRTou"/>
    <html:hidden name="searchForm" property="selectedRRTouId" styleId="selectedRRTouId"/>
    <html:hidden name="searchForm" property="selectedCategoryId" styleId="selectedCategoryId"/>
    <input type="hidden" name="perm" id="selectedPerm" value=""/>
    <input type="hidden" name="idx" id="selectedIdx" value=""/>
    <input type="hidden" name="selectedTou" id="selectedTou" value=""/>
    <input type="hidden" name="item" id="selectedItem" value=""/>
    <html:hidden property="operation" styleId="operation"/>
</html:form>

<logic:equal name="searchForm" property="isPPU" value="true">
	<!-- Rights Qualifying statement -->
	<logic:notEmpty name="searchForm" property="rightsQualifyingStatement">
	    <div class="icon-alert" style="float:left;">
			<strong><bean:write name="searchForm" property="rightsQualifyingStatement"/></strong>
	    </div>
	</logic:notEmpty>
</logic:equal> 

<div class="clearer"></div>
    					
<logic:equal name="item" property="isPublicDomainNotBiactive" value="true">
    <div class="srpaysummary-line-alt">
        <p align="center">
            <i>This work is in the public domain of the United States.</i><br />
            <i>You may use the work without restriction.</i><br /><br />
        </p>
    </div>
    <div class="clearer"></div>
</logic:equal>

<div>
 	<logic:equal name="searchForm" property="isTraditionalPPUSelected" value="true">
		<div style="display: block" class="tab-content" id="pay_per_use_<%=wrkInst%>">
			<logic:equal name="searchForm" property="isBiactive" value="true">
				<div class="title"><h4 style="font-size:14px">Publication year:&nbsp;<bean:write name="searchForm" property="selectedPubYear"/></h4>
				</div>
			</logic:equal>
			<div class="title">
				<h4 style="font-size:13px;">Permission Type</h4>
				<h4 style="font-size:13px">Availability</h4>
			</div>
			<!--  all contents here -->
			<tiles:insert page="/WEB-INF/jsp-modules/search/tiles/payPerUseOptions.jsp"/>	
		</div>
	</logic:equal>
</div>
		
<script type="text/javascript">
//<![CDATA[
function isContactRightsHolder() {
    var selectedRightHolderInst = document.getElementById("selectedRightHolderInst");
    if(selectedRightHolderInst.value>0) return true;
    return false;
}
function showContactRightsholder() {
    var selectedRightHolderInst = document.getElementById("selectedRightHolderInst");
    openContactInfoByRH(selectedRightHolderInst.value);
}
//]]>
</script>
<!-- Webtrends tags for capturing search results -->
    <META name="WT.si_n" content="Checkout">
    <META name="WT.si_x" content="2">
    <META name="WT.tx_e" content="v">
    <META name="WT.pn_sku" content='<bean:write name="searchForm" property="selectedItem.work.idno" />'>      
<!-- end Webtrends tags -->