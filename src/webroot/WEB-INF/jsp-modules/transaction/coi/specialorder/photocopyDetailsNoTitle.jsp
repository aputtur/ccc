<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/tld/struts-logic.tld" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/cc2-util.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/tld/struts-bean.tld" %>
<%@ page errorPage="/jspError.do" %>


<!-- client side validation -->
<util:ccJavascript formName="specialOrderForm_photocopy" />
<!-- end client side validation -->



<script type="text/javascript">

    function validateForm(form)
    {
        return validateSpecialOrderForm_photocopy(form);
    }

</script>

<h2 class="clearer indent-1"><strong>Publication Information:</strong></h2>
<bean:define id="isBiactive" name="specialOrderFormCOI" property="isBiactive"/>
<table border="0" class="indent-2">
  <tr>
    <td width="239">Publication name:<span class="importanttype">*</span></td>
    <td><html:text name="specialOrderFormCOI" property="specialOrderItem.publicationTitle" styleClass="normal"  maxlength="250"/></td>
  </tr>
  <tr>
    <logic:notEmpty name="specialOrderFormCOI" property="specialOrderItem.idnoLabel">
      <td><bean:write name="specialOrderFormCOI" property="specialOrderItem.idnoLabel"/>: <span class="importanttype"></span></td>
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
    <td width="239"><div align="left">Total number of pages:<span class="importanttype">*</span>&nbsp;<util:contextualHelp helpId="27" rollover="false">More...</util:contextualHelp></div></td>
    <td><div align="left">
        <logic:greaterThan value="0" name="specialOrderFormCOI" property="specialOrderItem.numberOfPages">
            <html:text name="specialOrderFormCOI" property="specialOrderItem.numberOfPages" styleClass="normal" maxlength="5"/>
        </logic:greaterThan>
        <logic:lessEqual value="0" name="specialOrderFormCOI" property="specialOrderItem.numberOfPages">
            <html:text name="specialOrderFormCOI" property="specialOrderItem.numberOfPages" styleClass="normal" value="" maxlength="5"/>
        </logic:lessEqual>
    </div></td>
  </tr>
  <tr>
    <td><div align="left">Number of copies:<span class="importanttype">*</span> </div></td>
    <td><div align="left">
        <logic:greaterThan value="0" name="specialOrderFormCOI" property="specialOrderItem.numberOfCopies">
            <html:text name="specialOrderFormCOI" property="specialOrderItem.numberOfCopies" styleClass="normal" maxlength="5"/>
        </logic:greaterThan>
        <logic:lessEqual value="0" name="specialOrderFormCOI" property="specialOrderItem.numberOfCopies">
            <html:text name="specialOrderFormCOI" property="specialOrderItem.numberOfCopies" styleClass="normal" value="" maxlength="5" />
        </logic:lessEqual>
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