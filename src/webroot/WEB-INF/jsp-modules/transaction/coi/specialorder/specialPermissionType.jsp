<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/tld/struts-bean.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/tld/struts-logic.tld" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/cc2-util.tld" %>

<%@ page errorPage="/jspError.do" %>
<%@ page import="com.copyright.ccc.web.WebConstants" %>

<link href="<html:rewrite page="/resources/commerce/css/contextualHelp.css"/>" rel="stylesheet" type="text/css" />

<script src="<html:rewrite page="/resources/commerce/js/util.js"/>" type="text/javascript"></script>
<script src="<html:rewrite page="/resources/commerce/js/contentLoader.js"/>" type="text/javascript"></script>
<script src="<html:rewrite page="/resources/commerce/js/contentsChecker.js"/>" type="text/javascript"></script>
<script src="<html:rewrite page="/resources/commerce/js/contextualHelp.js"/>" type="text/javascript"></script>

<!-- tile attribute declarations -->

<tiles:useAttribute id="itemType" name="itemType"/>

<!-- end tile attribute declarations -->

<bean:define id="urlForContentsChecker">
    <logic:equal name="itemType" value="cart">
        <html:rewrite action="/contentsCheckerCoi.do?operation=checkCartContentType" />
    </logic:equal>
    <logic:equal name="itemType" value="order">
        <html:rewrite action="/contentsCheckerCoi.do?operation=checkOrderContentType" />
    </logic:equal>
</bean:define>

<bean:define id="formPath" name="specialPermissionTypeFormCOI" property="formPath" type="java.lang.String" />

<script type="text/javascript">
	function submit() {
    	document.getElementById("specialPermissionTypeFormCOI").submit();
	}
    
    function checkContentTypeAndSubmit()
    {
        var contentsChecker = new ContentsChecker("<%= urlForContentsChecker %>");
        <logic:equal name="itemType" value="cart">
            contentsChecker.checkContents("<%= WebConstants.RequestKeys.SPECIAL_PERMISSION_TYPE %>=" + getCheckedPermissionTypeValue());
        </logic:equal>
        <logic:equal name="itemType" value="order">
            contentsChecker.checkContents('<%= WebConstants.RequestKeys.SPECIAL_PERMISSION_TYPE %>=' + getCheckedPermissionTypeValue() + '&<%= WebConstants.RequestKeys.PURCHASE_ID %>=<bean:write name="specialPermissionTypeFormCOI" property="purchaseId"/>');
        </logic:equal>
    }
    
    function getCheckedPermissionTypeValue()
    {
        var radioButtons = document.getElementById("specialPermissionTypeForm").elements["permissionType"];
        
        for(var i = 0; i < radioButtons.length; i++)
        {
            if(radioButtons[i].checked)
                return radioButtons[i].value;
        }
    }
    
    function getRadioChecked()
    {
    	var radioButtons = document.getElementById("specialPermissionTypeFormCOI").elements["permissionType"];
        
        for(var i = 0; i < radioButtons.length; i++)
        {
            if(radioButtons[i].checked)
            {   //alert("Selected button: " + radioButtons[i].id);
            	//alert("Vlaue of i: " + i);
              //document.getElementById("specialPermissionTypeFormCOI").elements("radioButton").value = radioButton[i].value;
              document.getElementById("radioButton").value = radioButtons[i].id;
              document.getElementById("specialPermissionTypeFormCOI").radioButton.value = radioButtons[i].id;
              //alert("Radio button value: " + document.getElementById("radioButton").value);
              var radio = "radio" + radioButtons[i].id;
              //alert("Radio: " + radio);
              var radioLabel = document.getElementById(radio).innerHTML;
              //alert("Radio button text: " + radioLabel);
              document.getElementById("radioButtonText").value = radioLabel;
              }
              
        }
    
    }

    
</script>


<html:form action="<%= formPath %>" styleId="specialPermissionTypeFormCOI">

 <html:hidden name="specialPermissionTypeFormCOI" property="radioButton" styleId="radioButton"/>

    <html:hidden property="operation" value="goToSpecialOrder" />
    <html:hidden styleId="radioButtonText" property="radioButtonText" value="Use in electronic course materials" />

    <div id="ecom-boxcontent">
        
        <a href="javascript: history.go(-1)" class="icon-back">
            Back
        </a>
        
        &nbsp;&nbsp;
        
        <html:link page="/search.do?operation=show&page=last" styleClass="icon-back">
            New Search
        </html:link>
        
        <br><br>
    
        <h1>
            Can't Find What You're Looking For?
        </h1>
        
        
        <br>
       
        <p>
            Copyright Clearance Center will work on your behalf to obtain permission.  We will let you know if we are able to obtain permission, usually within 2 to 10 business days.&nbsp;
            <html:link page="http://support.copyright.com/index.php?action=category&id=21" target="_blank">Learn more about how Special Orders work</html:link>
        </p>
        
        <br/>
            
        <div class="horiz-rule"></div>
    
        <h2>Please select the permission type you need:</h2>
    
        <p class="indent-2">
        	<html:radio name="specialPermissionTypeFormCOI" property="permissionType" value="<%= WebConstants.PERMISSION_TYPE_ECCS %>"
        		onclick="javascript:getRadioChecked()" styleId="ECC" />
                <span id="radioECC">Use in electronic course materials</span> &nbsp;<util:contextualHelp helpId="3" rollover="true">More...</util:contextualHelp>
                <input type="hidden" name="permType" value="ECC"/>
                <br/>
            <html:radio name="specialPermissionTypeFormCOI" property="permissionType" value="<%= WebConstants.PERMISSION_TYPE_APS %>"
            	onclick="javascript:getRadioChecked()" styleId="APS"/>
                <span id="radioAPS">Use in print course materials</span> &nbsp;<util:contextualHelp helpId="1" rollover="true">More...</util:contextualHelp>
                <input type="hidden" name="permType" value="APS"/>
                <br/>
            <html:radio name="specialPermissionTypeFormCOI" property="permissionType" value="<%= WebConstants.PERMISSION_TYPE_TRS %>"
            	onclick="javascript:getRadioChecked()" styleId="TRS2" />
                <span id="radioTRS2">Deliver via Interlibrary Loan (ILL) or document delivery</span> &nbsp;<util:contextualHelp helpId="2" rollover="true">More...</util:contextualHelp>
                <input type="hidden" name="permType" value="TRS2"/>
                <br/>
            <html:radio name="specialPermissionTypeFormCOI" property="permissionType" value="<%= WebConstants.PERMISSION_TYPE_TRS %>"
            	onclick="javascript:getRadioChecked()" styleId="TRS" />
                <span id="radioTRS">Photocopy for general business or academic use</span> &nbsp;<util:contextualHelp helpId="2" rollover="true">More...</util:contextualHelp>
                <input type="hidden" name="permType" value="TRS"/>
                <br/>
            <html:radio name="specialPermissionTypeFormCOI" property="permissionType" value="<%= WebConstants.PERMISSION_TYPE_DPS %>"
            	onclick="javascript:getRadioChecked()" styleId="DPS" />
                <span id="radioDPS">Share content electronically</span> &nbsp;<util:contextualHelp helpId="4" rollover="true">More...</util:contextualHelp>
                <input type="hidden" name="permType" value="DPS"/>
                <br/>
            <html:radio name="specialPermissionTypeFormCOI" property="permissionType" value="<%= WebConstants.PERMISSION_TYPE_RLS %>"
            	onclick="javascript:getRadioChecked()" styleId="RLS" />
                <span id="radioRLS">Republish or display content</span> &nbsp;<util:contextualHelp helpId="5" rollover="true">More...</util:contextualHelp>
                <input type="hidden" name="permType" value="RLS"/>
            
          </p>

        <div class="horiz-rule"></div>

        <span class="floatright">
            <a href="javascript:submit()" id="999">
                <html:img src="/media/images/btn_continue.gif" alt="Continue" align="right" />
            </a>
        </span>
        
        <br clear="all" />

    </div>
    
</html:form>