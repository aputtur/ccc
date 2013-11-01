<%@ taglib prefix="bean" uri="/WEB-INF/tld/struts-bean.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/tld/struts-logic.tld" %>
<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/cc2-util.tld" %>

<%@ page errorPage="/jspError.do" %>
<%@ page import="com.copyright.ccc.oue.client.OpenUrlExtensionJspUtils" %>
<%@ page import="com.copyright.ccc.oue.client.OpenUrlExtensionUtils" %>

<%-- variable declarations --%>

<tiles:useAttribute id="expanded" name="expanded" ignore="true" />

<bean:define id="pricingSectionStyleClass">
    <logic:equal name="expanded" value="true">callout-light indent-1</logic:equal>
    <logic:notEqual name="expanded" value="true">indent-2</logic:notEqual>
</bean:define>

<bean:define id="isBiactive" name="basicTransactionFormCOI" property="isBiactive"/>


<%-- end variable declarations --%>

<!-- client side validation -->
<util:ccJavascript formName="basicTransactionForm_photocopy" />
<!-- end client side validation -->


<script type="text/javascript">

    function validateForm(form)
    {
        return validateBasicTransactionForm_photocopy(form);
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
        document.getElementById("pricingInstructions").style.display = "none";
        document.getElementById("pricingSection").className = "callout-light indent-1";
        document.getElementById("updatePriceButton").style.display = "none";
        document.getElementById("detailsSection").style.display = "none";
    }
    
    
</script>

<%-- pricing section --%>

<%OpenUrlExtensionJspUtils jspUtils = new OpenUrlExtensionJspUtils(request.getSession());
	if(jspUtils.getExtensionData()!=null)
	{
		jspUtils.getExtensionData().setValue(OpenUrlExtensionUtils.ParameterKeys.PERMISSION_NAME,request.getParameter("perm"));
 	}
 %>


<h2 id="pricingInstructions" class="padtop">Enter the following details to determine a price:<span class="required"> * Required</span></h2>

<div id="pricingSection" class="<%= pricingSectionStyleClass %>">

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
            <td>Total number of pages:<span class="importanttype">*</span>&nbsp;<util:contextualHelp helpId="27" rollover="false">More...</util:contextualHelp></td>
            <td><div align="left">
            <%if(jspUtils.isValidOUESession()){ %>
            	<html:text name="basicTransactionFormCOI" property="transactionItem.numberOfPages" value="<%=jspUtils.displayDefaultTotalNumberPage() %>" styleClass="small" maxlength="5" />
            <%}else{ %>
               	<logic:greaterThan value="0" name="basicTransactionFormCOI" property="transactionItem.numberOfPages">
                    <html:text name="basicTransactionFormCOI" property="transactionItem.numberOfPages" styleClass="small" maxlength="5" />
                </logic:greaterThan>
                <logic:lessEqual value="0" name="basicTransactionFormCOI" property="transactionItem.numberOfPages">
                    <html:text name="basicTransactionFormCOI" property="transactionItem.numberOfPages" styleClass="small" value="" maxlength="5" />
                </logic:lessEqual>
            <%} %>
            </div></td>
        </tr>
        
        <tr>
            <td>Number of copies: <span class="importanttype">*</span></td>
            <td><div align="left">
                <logic:greaterThan value="0" name="basicTransactionFormCOI" property="transactionItem.numberOfCopies">
                    <html:text name="basicTransactionFormCOI" property="transactionItem.numberOfCopies" styleClass="small" maxlength="5"/>
                </logic:greaterThan>
                <logic:lessEqual value="0" name="basicTransactionFormCOI" property="transactionItem.numberOfCopies">
                    <html:text name="basicTransactionFormCOI" property="transactionItem.numberOfCopies" styleClass="small" value="" maxlength="5"/>
                </logic:lessEqual>
            </div></td>
        </tr>
        
                       
        
    </table>
    
</div>

<table style="margin-left:10px;">
	<logic:equal name="basicTransactionFormCOI" property="skipQuickprice" value="true">
          <html:hidden name="basicTransactionFormCOI" property="skipQuickprice" value="true"/>
        </logic:equal>
        
        <logic:equal name="basicTransactionFormCOI" property="skipQuickprice" value="false">
        <br/>
          <tr>
          	<td><div align="right" valign="top"><html:checkbox name="basicTransactionFormCOI" property="skipQuickprice"/></div></td>
            <td style="Vertical-align: middle;">Skip pricing for the rest of your session when available 
                       <util:contextualHelp helpId="36" rollover="false"><u>More info</u></util:contextualHelp>                               
            </td>
          </tr>
        </logic:equal>  

</table>

<%-- end pricing section --%>

<a name="detailsSectionAnchor"></a>
<div id="detailsSection">

    <h2>Additional details:<span class="smalltype"> (Optional) </span> </h2>
    
    <table border="0" class="indent-1">
    
        <tr>
            <td width="115"><div align="left">Article/Chapter:</div></td>
            <td><div align="left">
                <html:text name="basicTransactionFormCOI" property="transactionItem.chapterArticle" value="<%=jspUtils.displayContentTitle()%>" styleClass="normal" size="48" maxlength="250"/>
            </div></td>
        </tr>
      
        <tr>
            <td><div align="left">Your reference: </div></td>
            <td>
                <html:text name="basicTransactionFormCOI" property="transactionItem.customerReference" styleClass="normal" size="48" maxlength="50"/>
                <span class="smalltype">(Example: prosmith456-1, prosmith456-2) </span></td>
        </tr>
        
        <div class="horiz-rule"></div>
        
    </table>

</div>
