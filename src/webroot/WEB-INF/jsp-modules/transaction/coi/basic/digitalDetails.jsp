<%@ taglib prefix="bean" uri="/WEB-INF/tld/struts-bean.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/tld/struts-logic.tld" %>
<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/cc2-util.tld" %>
<%@ page import="com.copyright.ccc.oue.client.OpenUrlExtensionJspUtils" %>
<%@ page import="com.copyright.ccc.oue.client.OpenUrlExtensionUtils" %>

<%@ page errorPage="/jspError.do" %>
<%@ page import="com.copyright.ccc.web.transaction.TransactionConstants" %>
<%@ page import="com.copyright.data.order.UsageDataNet" %>

<jsp:include page="/WEB-INF/jsp-modules/common/jQueryDatepickerSupport.jsp"/>

<!-- client side validation -->
<util:ccJavascript formName="basicTransactionForm_email" staticJavascript="false"/>
<util:ccJavascript formName="basicTransactionForm_net" />
<!-- end client side validation -->

<bean:define id="isBiactive" name="basicTransactionFormCOI" property="isBiactive"/>


<script type="text/javascript">

    var currentActiveElement = "";
    var origStartOfTermDateText = "";
    var isInSubmit = false;
    
    function setOnFocusForAllElements()
    {
        var allElements = document.getElementById("basicTransactionFormCOI").elements;
        
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
    
    

    function detailsExpand()
    {
        document.getElementById("pricingInstructions").style.display = "none";
        document.getElementById("pricingSection").className = "callout-light indent-1";
        document.getElementById("updatePriceRow").style.display = "";
        document.getElementById("updatePriceButton").style.display = "";
        document.getElementById("updatePriceButton").innerHTML = "Update Price";
        document.getElementById("detailsSection").style.display = "";
        location.href = "#detailsSectionAnchor";
    }
    
    function detailsError()
    {
        document.getElementById("pricingInstructions").style.display = "none";
        document.getElementById("pricingSection").className = "callout-light indent-1";
        document.getElementById("updatePriceRow").style.display = "";
        document.getElementById("updatePriceButton").style.display = "";
        document.getElementById("updatePriceButton").innerHTML = "Update";
        document.getElementById("detailsSection").style.display = "none";
    }
    
    function detailsHide()
    {
        document.getElementById("pricingInstructions").style.display = "";
        document.getElementById("pricingSection").className = "callout-light indent-1";
        document.getElementById("updatePriceRow").style.display = "none";
        document.getElementById("detailsSection").style.display = "none";
    }
    
    function toggleConditionalFieldsVisibility()
    {
        //var emailChecked = document.getElementById("emailRadio").checked;
        //var intranetChecked = document.getElementById("intranetRadio").checked;
        //var extranetChecked = document.getElementById("extranetRadio").checked;
        //var internetChecked = document.getElementById("internetRadio").checked;
        
       /* var dateOfUseLabel = document.getElementById("dateOfUseLabel");
        var numberOfRecipientsRow = document.getElementById("numberOfRecipientsRow");
        var durationRow = document.getElementById("durationRow");
        var webAddressRow = document.getElementById("webAddressRow");
        
        dateOfUseLabel.innerHTML = emailChecked ? "Date e-mail will be sent:" : "Date to be posted:";
        numberOfRecipientsRow.style.display = emailChecked ? "" : "none";
        durationRow.style.display = ( intranetChecked || extranetChecked || internetChecked ) ? "" : "none";
        webAddressRow.style.display = internetChecked ? "" : "none"; */
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

<%OpenUrlExtensionJspUtils jspUtils = new OpenUrlExtensionJspUtils(request.getSession());
	if(jspUtils.getExtensionData()!=null)
	{
		jspUtils.getExtensionData().setValue(OpenUrlExtensionUtils.ParameterKeys.PERMISSION_NAME,request.getParameter("perm"));
 	}
 %>

<%-- variable declarations --%>

<tiles:useAttribute id="expanded" name="expanded" ignore="true" />
<tiles:useAttribute id="allowPermissionChange" name="allowPermissionChange" ignore="true" />

<bean:define id="pricingSectionStyleClass">
    <logic:equal name="expanded" value="true">callout-light indent-1</logic:equal>
    <logic:notEqual name="expanded" value="true">indent-2</logic:notEqual>
</bean:define>

<%-- end variable declarations --%>

<%-- pricing section --%>
<div id="pricingSection" class="<%= pricingSectionStyleClass %>"><br></br>
<h2 id="pricingInstructions" class="padtop">Enter the following details to determine a price:<span class="required"> * Required</span></h2>



    <table border="0">
    
    	<tr id="updatePriceRow">
    	<td></td>
            <td width="300" align="center"><span class="required"> * Required</span></td>
            <td width="150"><div align="right"><a class="btn-yellow" href="javascript:updatePrice()" id="updatePriceButton">Update Price</a></div></td>
        </tr>
    
        <tr>
            <td width="225">Publication year of title being used:<span class="importanttype">*</span></td>
            <td><div align="left">
                <%if(jspUtils.isValidOUESession()){ %>
            		<html:text disabled="${isBiactive}" name="basicTransactionFormCOI" property="transactionItem.publicationYearOfUse" value="<%=jspUtils.displayContentDate()%>" styleClass="small" maxlength="4" />
            	<%}else{ %>
                	<html:text disabled="${isBiactive}" name="basicTransactionFormCOI" property="transactionItem.publicationYearOfUse" styleClass="small" maxlength="4" />
            	<%} %>   
            </div></td>
        </tr>
        
                        
        <tr>
        	<logic:equal name="basicTransactionFormCOI" property="transactionItem.email" value="true">
            	<td><span id="dateOfUseLabel">Date e-mail will be sent:</span> <span class="importanttype">*</span></td>
            </logic:equal>
            <logic:notEqual name="basicTransactionFormCOI" property="transactionItem.email" value="true">
            	<td><span id="dateOfUseLabel">Date to be posted:</span> <span class="importanttype">*</span></td>
            </logic:notEqual>
            <td><div align="left">
                <logic:empty name="basicTransactionFormCOI" property="transactionItem.dateOfUse">
                    <html:text name="basicTransactionFormCOI" property="transactionItem.dateOfUse" styleClass="small"
                        styleId="dateOfUseText" value="MM/DD/YYYY" />
                </logic:empty>
                <logic:notEmpty name="basicTransactionFormCOI" property="transactionItem.dateOfUse">
                    <bean:define id="formattedDateOfUse">
                        <bean:write name="basicTransactionFormCOI" property="transactionItem.dateOfUse" format="MM/dd/yyyy" />
                    </bean:define>
                    <html:text name="basicTransactionFormCOI" property="transactionItem.dateOfUse" value="<%= formattedDateOfUse %>" styleClass="small" 
                        styleId="dateOfUseText" />
                </logic:notEmpty>
            </div></td>
        </tr>
        
       <logic:equal name="basicTransactionFormCOI" property="transactionItem.email" value="true"> 
        	<tr id="numberOfRecipientsRow">
            	<td>Number of recipients: <span class="importanttype">*</span></td>
            	<td><div align="left">
                	<logic:greaterThan value="0" name="basicTransactionFormCOI" property="transactionItem.numberOfRecipients">
                    	<html:text name="basicTransactionFormCOI" property="transactionItem.numberOfRecipients" styleClass="small" maxlength="5"/>
                	</logic:greaterThan>
                	<logic:lessEqual value="0" name="basicTransactionFormCOI" property="transactionItem.numberOfRecipients">
                    	<html:text name="basicTransactionFormCOI" property="transactionItem.numberOfRecipients" styleClass="small" value="" maxlength="5"/>
                	</logic:lessEqual>
            	</div></td>
        	</tr>
        </logic:equal>
        
        <logic:equal name="basicTransactionFormCOI" property="transactionItem.email" value="false">
        	<tr id="durationRow">
            	<td>Duration of posting: <span class="importanttype">*</span></td>
            	<td><div align="left">
                	<html:select styleClass="select01" name="basicTransactionFormCOI" property="transactionItem.duration">
                    	<html:option value="-1">Choose One</html:option>
                    	<html:option value="<%= String.valueOf(UsageDataNet.TO_30_DAYS_FEE) %>"><%= UsageDataNet.feeStringValues[UsageDataNet.TO_30_DAYS_FEE] %></html:option>
                    	<html:option value="<%= String.valueOf(UsageDataNet.TO_180_DAYS_FEE) %>"><%= UsageDataNet.feeStringValues[UsageDataNet.TO_180_DAYS_FEE] %></html:option>
                    	<html:option value="<%= String.valueOf(UsageDataNet.TO_365_DAYS_FEE) %>"><%= UsageDataNet.feeStringValues[UsageDataNet.TO_365_DAYS_FEE] %></html:option>
                    	<html:option value="<%= String.valueOf(UsageDataNet.UNLIMITED_DAYS_FEE) %>"><%= UsageDataNet.feeStringValues[UsageDataNet.UNLIMITED_DAYS_FEE] %></html:option>
                	</html:select>
            	</div></td>
        	</tr>
        </logic:equal>
        
        <logic:equal name="basicTransactionFormCOI" property="transactionItem.internet" value="true">
        	<tr id="webAddressRow">
           		<td>URL of posting: <span class="importanttype">*</span></td>
            	<td><div align="left">
                	<html:text name="basicTransactionFormCOI" property="transactionItem.webAddress" styleClass="large" maxlength="250"/>
            	</div></td>
        	</tr>
        </logic:equal>
        
                    
                                    
    </table>
    
</div>

<%-- end pricing section --%>

<a name="detailsSectionAnchor"></a>
<div id="detailsSection">

    <h2>Additional details:<span class="smalltype"> (Optional) </span> </h2>
    
    <table border="0" class="indent-1">
    
        <tr>
            <td width="115"><div align="left">Author/Editor:</div></td>
            <td><div align="left">
            	<%if(jspUtils.isValidOUESession()){ %>
            		<html:text name="basicTransactionFormCOI" property="transactionItem.customAuthor" value="${transactionItem.publicationTitle}" styleClass="normal" size="48" maxlength="250"/>
            	<%}else{ %>
                	<html:text name="basicTransactionFormCOI" property="transactionItem.customAuthor" styleClass="normal" size="48" maxlength="250"/>
            	<%} %>
            </div></td>
        </tr>
        
        <tr>
            <td><div align="left">Article/Chapter:</div></td>
            <td><div align="left">
                <html:text name="basicTransactionFormCOI" property="transactionItem.chapterArticle" value="<%=jspUtils.displayContentTitle()%>" styleClass="normal" size="48" maxlength="250"/>
            </div></td>
        </tr>
      
        <tr>
            <td><div align="left">Your reference: </div></td>
            <td>
                <html:text name="basicTransactionFormCOI" property="transactionItem.customerReference" styleClass="normal" size="48" maxlength="50"/>
                <span class="smalltype">(Example: "Mary's thesis, chapter 7")</span></td>
        </tr>
        
    </table>

</div>

<script type="text/javascript">

    var isIE = navigator.userAgent.indexOf("MSIE") > 0;
     
    //set global onfocus event to determine which element is focused
    setOnFocusForAllElements();

  
    toggleConditionalFieldsVisibility();
    
    <logic:equal name="allowPermissionChange" value="false">
        disableDigitalTypeOfUseChange();
    </logic:equal>
    function validateForm(form)
    {
        <bean:define id="isEmail">
    	<logic:equal name="basicTransactionFormCOI" property="transactionItem.email" value="true">true</logic:equal>
    	<logic:notEqual name="basicTransactionFormCOI" property="transactionItem.email" value="true">false</logic:notEqual>
    	</bean:define>
    	var isEmail='<%=isEmail%>';
    	
    	
    	 //if(document.getElementById("emailRadio").checked)
        if (isEmail=='true')
            return validateBasicTransactionForm_email(form);
        else
            return validateBasicTransactionForm_net(form);
    }
    
  	$(document).ready(function(){

	 	jqCalendarDatepickerCreate('dateOfUseText','<html:rewrite href='/media/images/calendar.gif'/>');
	 	jqCalendarDatepickerSetYearMenuRange('dateOfUseText','-0:+10');
	 	jqCalendarDatepickerSetMinDate( 'dateOfUseText', '+0y' );
	 	
	});
    
</script>
