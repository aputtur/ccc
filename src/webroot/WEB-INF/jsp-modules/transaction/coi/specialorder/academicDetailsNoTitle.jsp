<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/tld/struts-logic.tld" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/cc2-util.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/tld/struts-bean.tld" %>
<%@ page errorPage="/jspError.do" %>

<!-- client side validation -->
<util:ccJavascript formName="specialOrderForm_academic" />
<!-- end client side validation -->
<script type="text/javascript" language="javascript" src="<html:rewrite page="/resources/commerce/js/roman.js"/>"></script>
<script type="text/javascript" language="javascript" src="<html:rewrite page="/resources/commerce/js/parser.js"/>"></script>

<bean:define id="isBiactive" name="specialOrderFormCOI" property="isBiactive"/>

<logic:equal name="specialOrderFormCOI" property="specialOrderItem.publicationType" value="Book">
	<html:hidden name="specialOrderFormCOI" property="specialOrderItem.licenseeRequestedEntireWork"/>
</logic:equal>

<logic:notEqual name="specialOrderFormCOI" property="specialOrderItem.licenseeRequestedEntireWork" value="true" >
	<bean:define id="disableBook" value="false"></bean:define>
</logic:notEqual>

<logic:empty name="specialOrderFormCOI" property="specialOrderItem.pages">
	<bean:define id="pages" value="0"></bean:define>
</logic:empty>

<logic:notEmpty name="specialOrderFormCOI" property="specialOrderItem.pages" >
	<bean:define id="pages" name="specialOrderFormCOI" property="specialOrderItem.pages"></bean:define>
</logic:notEmpty>

<logic:empty name="specialOrderFormCOI" property="specialOrderItem.pages">
	<bean:define id="pages" value="0"></bean:define>
</logic:empty>

<style type="text/css">

    input.normal { width: 155px; }

</style>

<script type="text/javascript">

    function validateForm(form)
    {
        return validateSpecialOrderForm_academic(form);
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
          document.getElementById("numberOfPagesField").value = pageCount;
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
	
					
			flds = document.getElementsByName("specialOrderItem.numberOfPages");
			fldsPageRange = document.getElementsByName("specialOrderItem.pageRange");
			
			if ( document.getElementsByName("specialOrderItem.licenseeRequestedEntireWork") != null)
			{
				fldsBookCheckBox = document.getElementsByName("specialOrderItem.licenseeRequestedEntireWork");
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
	  		//alert("Unchecked");
			flds = document.getElementsByName("specialOrderItem.numberOfPages");
			fldsPageRange = document.getElementsByName("specialOrderItem.pageRange");
    		flds[0].value = "";
    		fldsPageRange[0].value = "";
    		flds[0].disabled = false;
			fldsPageRange[0].disabled = false;
			
			if ( document.getElementsByName("specialOrderItem.licenseeRequestedEntireWork") != null)
			{
				fldsBookCheckBox = document.getElementsByName("specialOrderItem.licenseeRequestedEntireWork");
				fldsBookCheckBox[0].value = "";
				boxName.value = "";
			}
	}
	
  }

</script>

<h2 class="clearer indent-1"><strong>Publication Information:</strong></h2>

<table border="0" class="indent-2">
  <tr>
    <td  width="220">Publication name:<span class="importanttype">*</span></td>
    <td><html:text name="specialOrderFormCOI" property="specialOrderItem.publicationTitle" styleClass="normal"  maxlength="250"/></td>
  </tr>
  <tr>
    <logic:notEmpty name="specialOrderFormCOI" property="specialOrderItem.idnoLabel">
      <td><bean:write name="specialOrderFormCOI" property="specialOrderItem.idnoLabel"/>: <span class="importanttype">*</span></td>
    </logic:notEmpty>
    <logic:empty name="specialOrderFormCOI" property="specialOrderItem.idnoLabel">
      <td>ISBN/ISSN: <span class="importanttype">*</span></td>
    </logic:empty>
	<td><html:text name="specialOrderFormCOI" property="specialOrderItem.standardNumber" styleClass="normal"  maxlength="50"/></td>
  </tr>
  <tr>
    <td>Publication year of title being used:<span class="importanttype">*</span></td>
    <td><div align="left">
        <html:text disabled="${isBiactive}" name="specialOrderFormCOI" property="specialOrderItem.publicationYearOfUse" styleClass="normal"  maxlength="4"/>
    </div></td>
  </tr>
  <tr>
    <td>Publisher: <span class="importanttype">*</span></td>
    <td><html:text name="specialOrderFormCOI" property="specialOrderItem.publisher" styleClass="normal" maxlength="250"/></td>
  </tr>
  <tr>
    <td>Author/Editor:<span class="importanttype">*</span></td>
    <td><html:text name="specialOrderFormCOI" property="specialOrderItem.customAuthor" styleClass="normal" size="48" maxlength="250"/></td>
  </tr>
  <tr>
    <td>Volume:</td>
    <td><html:text name="specialOrderFormCOI" property="specialOrderItem.customVolume" styleClass="normal" maxlength="250"/></td>
  </tr>
  <tr>
    <td>Edition:</td>
    <td><html:text name="specialOrderFormCOI" property="specialOrderItem.customEdition" styleClass="normal" maxlength="250"/></td>
  </tr>
</table>

<h2 class="indent-1">Usage Information:</h2>

<table border="0" class="indent-2">
  <tr>
    <td width="220"><div align="left">Article/Chapter:</div></td>
    <td><div align="left">
        <html:text name="specialOrderFormCOI" property="specialOrderItem.chapterArticle" styleClass="normal" size="48" maxlength="250"/>
        <span class="smalltype">(Enter only <b>one</b> Article or Chapter per cart item)</span>
    </div></td>
  </tr>
  <tr>
    <td>Page range(s):<span class="importanttype">*</span></td>
    <td><html:text name="specialOrderFormCOI" property="specialOrderItem.pageRange" styleClass="normal" size="48" maxlength="80" onchange="validatePageRange(this)"/>
      <span class="smalltype">(Examples: ii, iv-vi or 3, 7-10) </span></td>
  </tr>
  <tr>
    <td><div align="left">Total number of pages:<span class="importanttype">*</span>&nbsp;<util:contextualHelp helpId="27" rollover="false">More...</util:contextualHelp></div></td>
    <td><div align="left">
        <logic:greaterThan value="0" name="specialOrderFormCOI" property="specialOrderItem.numberOfPages">
            <html:text name="specialOrderFormCOI" property="specialOrderItem.numberOfPages" styleId="numberOfPagesField" styleClass="normal" maxlength="5"/>
        </logic:greaterThan>
        <logic:lessEqual value="0" name="specialOrderFormCOI" property="specialOrderItem.numberOfPages">
            <html:text name="specialOrderFormCOI" property="specialOrderItem.numberOfPages" styleId="numberOfPagesField" styleClass="normal" value="" maxlength="5"/>
        </logic:lessEqual>
    </div></td>
  </tr>
  <logic:equal name="specialOrderFormCOI" property="specialOrderItem.publicationType" value="Book">
        	<tr>
        		<td>I want to reuse the entire book:<span class="importanttype"></span>&nbsp;<util:contextualHelp helpId="46" rollover="false">More...</util:contextualHelp></td>
  				<td><html:checkbox name="specialOrderFormCOI" property="specialOrderItem.licenseeRequestedEntireWork" value="true" onclick="clickOopBook(this);return true;"/></td> 
        	</tr>
  </logic:equal>
  <tr>
    <td><div align="left">Number of students:<span class="importanttype">*</span> </div></td>
    <td><div align="left">
        <logic:greaterThan value="0" name="specialOrderFormCOI" property="specialOrderItem.numberOfStudents">
            <html:text name="specialOrderFormCOI" property="specialOrderItem.numberOfStudents" styleClass="normal" maxlength="5"/>
        </logic:greaterThan>
        <logic:lessEqual value="0" name="specialOrderFormCOI" property="specialOrderItem.numberOfStudents">
            <html:text name="specialOrderFormCOI" property="specialOrderItem.numberOfStudents" styleClass="normal" value="" maxlength="5"/>
        </logic:lessEqual>
    </div></td>
  </tr>
  <tr>
    <td><div align="left">Date of issue:</div></td>
    <td><div align="left">
        <html:text name="specialOrderFormCOI" property="specialOrderItem.dateOfIssue" styleClass="normal" maxlength="30"/>
            <span class="smalltype">(For <util:contextualHelp helpId="28" rollover="false">serials</util:contextualHelp> only.  Examples: Fall 2004, 12/12/2006) </span>
    </div></td>
  </tr>
  <tr>
    <td><div align="left">Your line item reference: </div></td>
    <td><html:text name="specialOrderFormCOI" property="specialOrderItem.customerReference" styleClass="normal" size="48" maxlength="50"/>
        <span class="smalltype">(Example: prosmith456-1) </span></td>
  </tr>
</table>