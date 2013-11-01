<%@ taglib prefix="bean" uri="/WEB-INF/tld/struts-bean.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/tld/struts-logic.tld" %>
<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/cc2-util.tld" %>
<%@ taglib prefix="oue" uri="/WEB-INF/tld/ouextensions.tld" %>
<%@ page import="com.copyright.ccc.oue.client.OpenUrlExtensionJspUtils" %>
<%@ page import="com.copyright.ccc.oue.client.OpenUrlExtensionUtils" %>


<%@ page errorPage="/jspError.do" %>

<%-- variable declarations --%>

<tiles:useAttribute id="expanded" name="expanded" ignore="true" />

<bean:define id="pricingSectionStyleClass">
    <logic:equal name="expanded" value="true">callout-light indent-1</logic:equal>
    <logic:notEqual name="expanded" value="true">indent-2</logic:notEqual>
</bean:define>

<bean:define id="isBiactive" name="basicTransactionFormCOI" property="isBiactive"/>

<logic:present name="basicTransactionFormCOI" property="transactionItem.publicationType">
	<bean:define id="PubType" name="basicTransactionFormCOI" property="transactionItem.publicationType"/>
</logic:present>


<logic:equal name="basicTransactionFormCOI" property="transactionItem.publicationType" value="Book">
	<html:hidden name="basicTransactionFormCOI" property="transactionItem.licenseeRequestedEntireWork"/>
	<logic:notEmpty name="basicTransactionFormCOI" property="transactionItem.entireBookFeeValue">
		<bean:define id="BookFee" name="basicTransactionFormCOI" property="transactionItem.entireBookFeeValue"/>
	</logic:notEmpty>
	<logic:empty name="basicTransactionFormCOI" property="transactionItem.entireBookFeeValue">
		<bean:define id="BookFee" value="0"/>
	</logic:empty>
</logic:equal>

<logic:notEqual name="basicTransactionFormCOI" property="transactionItem.publicationType" value="Book">
	<bean:define id="BookFee" value="0"/>
</logic:notEqual>

	<logic:notEmpty name="basicTransactionFormCOI" property="transactionItem.pages" >
		<bean:define id="pages" name="basicTransactionFormCOI" property="transactionItem.pages"></bean:define>
	</logic:notEmpty>

	<logic:empty name="basicTransactionFormCOI" property="transactionItem.pages">
		<bean:define id="pages" value="0"></bean:define>
	</logic:empty>


<%-- end variable declarations --%>

<!-- client side validation -->
<util:ccJavascript formName="basicTransactionForm_academic" />
<!-- end client side validation -->

<script type="text/javascript" language="javascript" src="<html:rewrite page="/resources/commerce/js/roman.js"/>"></script>
<script type="text/javascript" language="javascript" src="<html:rewrite page="/resources/commerce/js/parser.js"/>"></script>

<script type="text/javascript">

    function validateForm(form)
    {
        return validateBasicTransactionForm_academic(form);
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
        document.getElementById("updatePriceRow").style.display = "none";
        document.getElementById("detailsSection").style.display = "none";
    }
    
    function validatePageRange(pageRangeField) {
      var pageCount = -1, flds;
  
      if (pageRangeField.value == null || pageRangeField.value == "") {
        alert("Page Range(s) is a required field.");
        pageRangeField.focus();
      }
      else {
        pageCount = getPageCount(pageRangeField.value);
        if (pageCount != -1) {
          flds = document.getElementsByName("transactionItem.numberOfPages");
          flds[0].value = pageCount;
        }
        else {
          alert("You have entered an invalid page range.");
          pageRangeField.focus();
        }
      }
    }
    
    function clickOopBook(boxName) {
    
    if(boxName.checked == true) {
	//alert("Checked");
	
		if ( document.getElementById("biblio_price") != null)
		    {
		        //alert("Book fee: " + ${BookFee});
		    	if (${BookFee} > 0)
		    	{
					document.getElementById("biblio_price").style.display = "none" ;
					document.getElementById("oop_price").style.display = "" ;
				}
			}
			
			flds = document.getElementsByName("transactionItem.numberOfPages");
			fldsPageRange = document.getElementsByName("transactionItem.pageRange");
			
			if ( document.getElementsByName("transactionItem.licenseeRequestedEntireWork") != null)
			{
				fldsBookCheckBox = document.getElementsByName("transactionItem.licenseeRequestedEntireWork");
				fldsBookCheckBox[0].value = true;
				boxName.value = true;
			}
		
			if (${pages} > 0)
			{
				flds[0].value = "${pages}";
				fldsPageRange[0].value = "1-" + "${pages}";
				flds[0].disabled = true;
				fldsPageRange[0].disabled = true;
			}
		
	}
	else {
	 if ( document.getElementById("biblio_price") != null)
	 {
			document.getElementById("biblio_price").style.display = "" ;
			document.getElementById("oop_price").style.display = "none" ;
	 }
	 		//alert("Unchecked");
			flds = document.getElementsByName("transactionItem.numberOfPages");
			fldsPageRange = document.getElementsByName("transactionItem.pageRange");
    		flds[0].value = "";
    		fldsPageRange[0].value = "";
    		flds[0].disabled = false;
			fldsPageRange[0].disabled = false;
			
			if ( document.getElementsByName("transactionItem.licenseeRequestedEntireWork") != null)
			{
				fldsBookCheckBox = document.getElementsByName("transactionItem.licenseeRequestedEntireWork");
				fldsBookCheckBox[0].value = "";
				boxName.value = "";
			}
	}
	
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
    	<td style="width:450px"><div style="float:left;text-align:left"><span class="required"> * Required</span></div><div style="float:right;text-align:right;"><a class="btn-yellow" href="javascript:updatePrice()" id="updatePriceButton">Update Price</a></div>
    	</td>
        </tr>
    
        <tr>
            <td width="225">Publication year of title being used:<span class="importanttype">*</span></td>
            <td><div align="left">
            	<%if(jspUtils.isValidOUESession()){ %>
        			<html:text disabled="${isBiactive}" name="basicTransactionFormCOI" property="transactionItem.publicationYearOfUse" value="<%=jspUtils.displayContentDate()%>" styleClass="small" maxlength="4"/>
        		<%}else{ %>
            		<html:text disabled="${isBiactive}" name="basicTransactionFormCOI" property="transactionItem.publicationYearOfUse" styleClass="small" maxlength="4"/>
       	 		<%} %>
			</div></td>
        </tr>
        
    
      <tr>
          <td>Page range(s):<span class="importanttype">*</span></td>
          <td><div align="left">
              <%if(jspUtils.isValidOUESession()){ %>
              <html:text name="basicTransactionFormCOI" property="transactionItem.pageRange" value="<%=jspUtils.displayDefaultPageNumberRange() %>"
              styleClass="normal" size="48" maxlength="80" onchange="validatePageRange(this)"/>
              <%}else{ %>
              <html:text name="basicTransactionFormCOI" property="transactionItem.pageRange" styleClass="normal" size="48" maxlength="80" onchange="validatePageRange(this)"/>
               <%} %>
              <span class="smalltype">(Examples: ii, iv-vi or 3, 7-10) </span>
          </div></td>
      </tr>
      
      
        
        <tr>
            <td>Total number of pages:<span class="importanttype">*</span>&nbsp;<util:contextualHelp helpId="27" rollover="false">More...</util:contextualHelp></td>
            <td><div align="left">
                <%if(jspUtils.isValidOUESession()){ %>
                <html:text name="basicTransactionFormCOI" property="transactionItem.numberOfPages" value="<%=jspUtils.displayDefaultTotalNumberPage() %>" styleClass="small" maxlength="5"/>               
            	<%}else{ %>
                <logic:greaterThan value="0" name="basicTransactionFormCOI" property="transactionItem.numberOfPages">
                    <html:text name="basicTransactionFormCOI" property="transactionItem.numberOfPages" styleClass="small" maxlength="5"/>
                </logic:greaterThan>
                <logic:lessEqual value="0" name="basicTransactionFormCOI" property="transactionItem.numberOfPages">
                    <html:text name="basicTransactionFormCOI" property="transactionItem.numberOfPages" styleClass="small" value="" maxlength="5"/>
                </logic:lessEqual>
            	<%} %>    
            </div></td>
        </tr>
        
        <logic:equal name="basicTransactionFormCOI" property="transactionItem.publicationType" value="Book">
        	<tr>
        		<td>I want to reuse the entire book:<span class="importanttype"></span>&nbsp;<util:contextualHelp helpId="46" rollover="false">More...</util:contextualHelp></td>
  				<td><html:checkbox name="basicTransactionFormCOI" property="transactionItem.licenseeRequestedEntireWork" value="true" onclick="clickOopBook(this);return true;"/></td> 
        	</tr>
        </logic:equal>
        
             
        
        
        
        <tr>
            <td>Number of students: <span class="importanttype">*</span></td>
            <td><div align="left">
                <logic:greaterThan value="0" name="basicTransactionFormCOI" property="transactionItem.numberOfStudents">
                    <html:text name="basicTransactionFormCOI" property="transactionItem.numberOfStudents" styleClass="small" maxlength="5"/>
                </logic:greaterThan>
                <logic:lessEqual value="0" name="basicTransactionFormCOI" property="transactionItem.numberOfStudents">
                    <html:text name="basicTransactionFormCOI" property="transactionItem.numberOfStudents" styleClass="small" value="" maxlength="5"/>
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

    <h2>Please enter the following details to continue:<span class="required"> * Required</span> </h2>
    
    <table border="0" class="indent-1">
    
        <tr>
         <td  width="165"><div align="left">Article/Chapter:</div></td>
         <td><div align="left">
         	<html:text name="basicTransactionFormCOI" property="transactionItem.chapterArticle" value="<%=jspUtils.displayContentTitle()%>" styleClass="normal" size="48" maxlength="250"/>
            <span class="smalltype">(Enter only <b>one</b> Article or Chapter per cart item)</span>
         </div></td>
        </tr>
     
        <tr>
            <td><div align="left">Author/Editor:<span class="importanttype">*</span></div></td>
            <td><div align="left">
                <%if(jspUtils.isValidOUESession()){ %>
                	<html:text name="basicTransactionFormCOI" property="transactionItem.customAuthor" value="${transactionItem.publicationTitle}" styleClass="normal" size="48" maxlength="250"/>
            	<%} else { %>
            		<html:text name="basicTransactionFormCOI" property="transactionItem.customAuthor" styleClass="normal" size="48" maxlength="250"/>
            	<%} %>    
            </div></td>
        </tr>
        
        	
      
        <tr>
            <td><div align="left">Date of issue:</div></td>
            <td><div align="left">
                <html:text name="basicTransactionFormCOI" property="transactionItem.dateOfIssue" styleClass="normal" maxlength="30"/>
                <span class="smalltype">(For <util:contextualHelp helpId="28" rollover="false">serials</util:contextualHelp> only.  Examples: Fall 2004, 12/12/2006) </span>
            </div></td>
        </tr>
      
        <tr>
            <td><div align="left">Volume:</div></td>
            <td><div align="left">
                <html:text name="basicTransactionFormCOI" property="transactionItem.customVolume" styleClass="normal" size="48" maxlength="250"/>
            </div></td>
        </tr>
      
        <tr>
            <td><div align="left">Edition:</div></td>
            <td><div align="left">
                <html:text name="basicTransactionFormCOI" property="transactionItem.customEdition" styleClass="normal" size="48" maxlength="250"/>
            </div></td>
        </tr>
      
        <tr>
            <td><div align="left">Your line item reference: </div></td>
            <td>
                <html:text name="basicTransactionFormCOI" property="transactionItem.customerReference" styleClass="normal" size="48" maxlength="50"/>
                <span class="smalltype">(Example: prosmith456-1, prosmith456-2) </span></td>
        </tr>
        
    </table>

</div>

