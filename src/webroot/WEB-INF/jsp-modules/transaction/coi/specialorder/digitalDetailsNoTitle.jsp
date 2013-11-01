<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/tld/struts-logic.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/tld/struts-bean.tld" %>
<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/cc2-util.tld" %>

<%@ page errorPage="/jspError.do" %>
<%@ page import="com.copyright.ccc.web.transaction.TransactionConstants" %>
<%@ page import="com.copyright.data.order.UsageDataNet" %>

<!-- client side validation -->
<util:ccJavascript formName="specialOrderForm_email" staticJavascript="false"/>
<util:ccJavascript formName="specialOrderForm_net" />
<!-- end client side validation -->

<bean:define id="isBiactive" name="specialOrderFormCOI" property="isBiactive"/>

<jsp:include page="/WEB-INF/jsp-modules/common/jQueryDatepickerSupport.jsp"/>

<script type="text/javascript">

    var currentActiveElement = "";
    var origStartOfTermDateText = "";
    var isInSubmit = false;

    function setOnFocusForAllElements()
    {
        var allElements = document.getElementById("specialOrderFormCOI").elements;

        for( var i = 0; i < allElements.length; i++ )
        {
            allElements[i].onfocus = function(){ setOnFocusForElement( this ); };
        }
    }

    function setOnFocusForElement( element )
    {
        var elementId = element.id;

        if( elementId == "dateOfUseText" )
            origStartOfTermDateText = element.value;
        else
        {
            //note: i don't need to check for the calendar link b/c links are not included
            //in the elements array, so this won't be called by the calendar link
            if( currentActiveElement == "dateOfUseText" )
                validateStartOfTermDate();
        }

        currentActiveElement = elementId;
    }

    function validateStartOfTermDate()
    {
        var startOfTermDate = document.getElementById("dateOfUseText").value;

        if( startOfTermDate != origStartOfTermDateText )
        {
            if ( !isValidCCDate( startOfTermDate, false ) )
                alert("Please enter a valid date e-mail will be sent, in the format mm/dd/yyyy or m/d/yyyy");
        }

    }

    function validateForm(form)
    {
        if(document.getElementById("emailRadio").checked)
            return validateSpecialOrderForm_email(form);
        else
            return validateSpecialOrderForm_net(form);
    }

    function toggleConditionalFieldsVisibility()
    {
        var emailChecked = document.getElementById("emailRadio").checked;
        var intranetChecked = document.getElementById("intranetRadio").checked;
        var extranetChecked = document.getElementById("extranetRadio").checked;
        var internetChecked = document.getElementById("internetRadio").checked;

        var dateOfUseLabel = document.getElementById("dateOfUseLabel");
        var numberOfRecipientsRow = document.getElementById("numberOfRecipientsRow");
        var durationRow = document.getElementById("durationRow");
        var webAddressRow = document.getElementById("webAddressRow");

        dateOfUseLabel.innerHTML = emailChecked ? "Date e-mail will be sent:" : "Date to be posted:";
        numberOfRecipientsRow.style.display = emailChecked ? "" : "none";
        durationRow.style.display = ( intranetChecked || extranetChecked || internetChecked ) ? "" : "none";
        webAddressRow.style.display = internetChecked ? "" : "none";
    }

    function disableDigitalTypeOfUseChange()
    {
        document.getElementById("emailRadio").disabled = true;
        document.getElementById("intranetRadio").disabled = true;
        document.getElementById("extranetRadio").disabled = true;
        document.getElementById("internetRadio").disabled = true;
        document.getElementById("digitalTypeOfUseLabel").className = "greytype";
        document.getElementById("emailLabel").className = "greytype";
        document.getElementById("intranetLabel").className = "greytype";
        document.getElementById("extranetLabel").className = "greytype";
        document.getElementById("internetLabel").className = "greytype";
    }


</script>

<%-- tiles declarations --%>
<tiles:useAttribute id="allowPermissionChange" name="allowPermissionChange" ignore="true" />
<%-- end tiles declarations --%>

<h2 class="clearer indent-1"><strong>Publication Information:</strong></h2>

<table border="0" class="indent-2">
    <tr>
        <td width="239">Publication name:<span class="importanttype">*</span></td>
        <td><html:text name="specialOrderFormCOI" property="specialOrderItem.publicationTitle" styleClass="normal" maxlength="250"/></td>
    </tr>
    <tr>
        <logic:notEmpty name="specialOrderFormCOI" property="specialOrderItem.idnoLabel">
		    <td><bean:write name="specialOrderFormCOI" property="specialOrderItem.idnoLabel"/>: <span class="importanttype">*</span></td>
		</logic:notEmpty>
		<logic:empty name="specialOrderFormCOI" property="specialOrderItem.idnoLabel">
		    <td>ISBN/ISSN: <span class="importanttype">*</span></td>
		</logic:empty>
        <td><html:text name="specialOrderFormCOI" property="specialOrderItem.standardNumber" styleClass="normal" maxlength="50"/></td>
    </tr>
    <tr>
        <td>Publication year of title being used:<span class="importanttype">*</span></td>
        <td><div align="left">
            <html:text disabled="${isBiactive}" name="specialOrderFormCOI" property="specialOrderItem.publicationYearOfUse" styleClass="normal" maxlength="4"/>
        </div></td>
    </tr>
    <tr>
        <td>Publisher: <span class="importanttype">*</span></td>
        <td><html:text name="specialOrderFormCOI" property="specialOrderItem.publisher" styleClass="normal" maxlength="250"/></td>
    </tr>
    <tr>
        <td>Author/Editor:</td>
        <td><html:text name="specialOrderFormCOI" property="specialOrderItem.customAuthor" styleClass="normal" maxlength="250"/></td>
    </tr>
</table>

<h2 class="indent-1">Usage Information:</h2>

<table border="0" class="indent-2">
    <tr>
        <td width="239"><div align="left" id="digitalTypeOfUseLabel">The content will be distributed via:<span class="importanttype">*</span> </div></td>
        <td style="width:360px"><div align="left">
            <html:radio name="specialOrderFormCOI" property="specialTypeOfUseCode" value="<%= String.valueOf(TransactionConstants.TYPE_OF_USE_EMAIL) %>"
                 onclick="javascript:toggleConditionalFieldsVisibility()" styleId="emailRadio" />
                 <span id="emailLabel"> E-mail&nbsp;</span>
                 <logic:notEqual name="allowPermissionChange" value="false"><util:contextualHelp helpId="26" rollover="true">What's this?</util:contextualHelp></logic:notEqual>
                 <logic:equal name="allowPermissionChange" value="false">
                    <span style="padding-left:60px"><util:contextualHelp helpId="30" rollover="false">Why can't I change this?</util:contextualHelp></span>
                </logic:equal>
                <br>
            <html:radio name="specialOrderFormCOI" property="specialTypeOfUseCode" value="<%= String.valueOf(TransactionConstants.TYPE_OF_USE_INTRANET) %>"
                 onclick="javascript:toggleConditionalFieldsVisibility()" styleId="intranetRadio" />
                 <span id="intranetLabel"> Intranet posting&nbsp;</span>
                 <logic:notEqual name="allowPermissionChange" value="false"><util:contextualHelp helpId="24" rollover="true">What's this?</util:contextualHelp></logic:notEqual>
                 <br>
            <html:radio name="specialOrderFormCOI" property="specialTypeOfUseCode" value="<%= String.valueOf(TransactionConstants.TYPE_OF_USE_EXTRANET) %>"
                 onclick="javascript:toggleConditionalFieldsVisibility()" styleId="extranetRadio" />
                 <span id="extranetLabel"> Extranet posting&nbsp;</span>
                 <logic:notEqual name="allowPermissionChange" value="false"><util:contextualHelp helpId="25" rollover="true">What's this?</util:contextualHelp></logic:notEqual>
                 <br>
            <html:radio name="specialOrderFormCOI" property="specialTypeOfUseCode" value="<%= String.valueOf(TransactionConstants.TYPE_OF_USE_INTERNET) %>"
                 onclick="javascript:toggleConditionalFieldsVisibility()" styleId="internetRadio" />
                 <span id="internetLabel"> Internet posting&nbsp;</span>
                <logic:notEqual name="allowPermissionChange" value="false"><util:contextualHelp helpId="23" rollover="true">What's this?</util:contextualHelp></logic:notEqual>
        </div></td>
    </tr>
    <tr>
        <td><span id="dateOfUseLabel">Date e-mail will be sent:</span> <span class="importanttype">*</span></td>
        <td><div align="left">
            <logic:empty name="specialOrderFormCOI" property="specialOrderItem.dateOfUse">
                <html:text name="specialOrderFormCOI" property="specialOrderItem.dateOfUse" styleClass="normal" styleId="dateOfUseText" value="MM/DD/YYYY"/>
            </logic:empty>
            <logic:notEmpty name="specialOrderFormCOI" property="specialOrderItem.dateOfUse">
                <bean:define id="formattedDateOfUse">
                    <bean:write name="specialOrderFormCOI" property="specialOrderItem.dateOfUse" format="MM/dd/yyyy" />
                </bean:define>
                <html:text name="specialOrderFormCOI" property="specialOrderItem.dateOfUse" value="<%= formattedDateOfUse %>" styleClass="normal" styleId="dateOfUseText"/>
            </logic:notEmpty>
        </div></td>
    </tr>
    <tr id="numberOfRecipientsRow">
        <td>Number of recipients: <span class="importanttype">*</span></td>
        <td><div align="left">
            <logic:greaterThan value="0" name="specialOrderFormCOI" property="specialOrderItem.numberOfRecipients">
                <html:text name="specialOrderFormCOI" property="specialOrderItem.numberOfRecipients" styleClass="normal" maxlength="5"/>
            </logic:greaterThan>
            <logic:lessEqual value="0" name="specialOrderFormCOI" property="specialOrderItem.numberOfRecipients">
                <html:text name="specialOrderFormCOI" property="specialOrderItem.numberOfRecipients" styleClass="normal" value="" maxlength="5"/>
            </logic:lessEqual>
        </div></td>
    </tr>
    <tr id="durationRow">
        <td>Duration of posting: <span class="importanttype">*</span></td>
        <td><div align="left">
            <html:select styleClass="select01" name="specialOrderFormCOI" property="specialOrderItem.duration">
                <html:option value="-1">Choose One</html:option>
                <html:option value="<%= String.valueOf(UsageDataNet.TO_30_DAYS_FEE) %>"><%= UsageDataNet.feeStringValues[UsageDataNet.TO_30_DAYS_FEE] %></html:option>
                <html:option value="<%= String.valueOf(UsageDataNet.TO_180_DAYS_FEE) %>"><%= UsageDataNet.feeStringValues[UsageDataNet.TO_180_DAYS_FEE] %></html:option>
                <html:option value="<%= String.valueOf(UsageDataNet.TO_365_DAYS_FEE) %>"><%= UsageDataNet.feeStringValues[UsageDataNet.TO_365_DAYS_FEE] %></html:option>
                <html:option value="<%= String.valueOf(UsageDataNet.UNLIMITED_DAYS_FEE) %>"><%= UsageDataNet.feeStringValues[UsageDataNet.UNLIMITED_DAYS_FEE] %></html:option>
            </html:select>
        </div></td>
    </tr>
    <tr id="webAddressRow">
        <td>URL of posting: <span class="importanttype">*</span></td>
        <td><div align="left">
            <html:text name="specialOrderFormCOI" property="specialOrderItem.webAddress" styleClass="large" maxlength="250"/>
        </div></td>
    </tr>
    <tr>
        <td><div align="left">Article/Chapter:</div></td>
        <td><div align="left">
            <html:text name="specialOrderFormCOI" property="specialOrderItem.chapterArticle" styleClass="normal" maxlength="250"/>
        </div></td>
    </tr>
    <tr>
        <td><div align="left">Your reference: </div></td>
        <td><html:text name="specialOrderFormCOI" property="specialOrderItem.customerReference" styleClass="normal" maxlength="50"/>
            <span class="smalltype">(Example: request789) </span></td>
    </tr>
</table>

<script type="text/javascript">

    var isIE = navigator.userAgent.indexOf("MSIE") > 0;

    //set global onfocus event to determine which element is focused
    setOnFocusForAllElements();


    <logic:equal name="allowPermissionChange" value="false">
        toggleConditionalFieldsVisibility();
        disableDigitalTypeOfUseChange();
    </logic:equal>
    <logic:notEqual name="allowPermissionChange" value="false">
        toggleConditionalFieldsVisibility();
    </logic:notEqual>

  	$(document).ready(function(){

	 	jqCalendarDatepickerCreate('dateOfUseText','<html:rewrite href='/media/images/calendar.gif'/>');
	 	jqCalendarDatepickerSetYearMenuRange('dateOfUseText','-0:+10');
	 	jqCalendarDatepickerSetMinDate( 'dateOfUseText', '+0y' );
	 	
	});

</script>
