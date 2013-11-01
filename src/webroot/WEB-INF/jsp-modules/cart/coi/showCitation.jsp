<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ page errorPage="/jspError.do" %>
<%@ page import="com.copyright.ccc.business.data.OrderLicense" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite page='/resources/commerce/css/noPrint.css'/>">
<link rel="stylesheet" type="text/css" href="<html:rewrite page='/resources/commerce/css/terms.css'/>">
<link rel="stylesheet" type="text/css" href="<html:rewrite page='/resources/commerce/css/ccc-new.css'/>">

<table border="0" width="100%" cellpadding="0" style="border-collapse: collapse" id="table1">
<tr><td>
<%-- <h2>Confirmation Number: <bean:write name="reviewSubmitCartActionForm" property='confirmNumber'/></h2> --%>
<h2>Confirmation Number: <bean:write name="reviewSubmitCartActionFormCOI" property='termsForm.confirmNumber'/></h2>
</td>
</tr>
<tr><td>
<table border="0" cellpadding="0" style="border-collapse: collapse" width="100%" class="sectionTable">
    <tr><td><h2>Citation Information</h2></td></tr>
</table>
<br />
<% int rowIndex = 0; %>

<logic:equal name="reviewSubmitCartActionFormCOI" property="termsForm.hasTerms" value="true">

<div class="callout-light">

    
  <logic:iterate name="reviewSubmitCartActionFormCOI" property="termsForm.terms" id="item" indexId="idx" type="OrderLicense">
  
<%
rowIndex = idx.intValue();
String rowClass;
if (rowIndex%2 ==0) { rowClass = "termEven"; }
else { rowClass = "termOdd"; }
%>

        
                     
            <b>Order Detail ID: </b><bean:write name="item" property="ID" />
            <br /><br />
        
            <%--
            <logic:notEmpty name="item" property="rightsQualifyingStatement">
                <div><span class="floatleft"><img src="/media/images/icon_alert.gif" border="0" alt="alert">&nbsp;<bean:write name="item" property="rightsQualifyingStatement"/>&nbsp;&nbsp;&nbsp;</span></div>
                <br /><br />
            </logic:notEmpty>
            --%>
            <b>
                <bean:write name="item" property="publicationTitle" /> 
                
                <logic:notEmpty name="item" property="author">
                    by <bean:write name="item" property="author" />
                </logic:notEmpty>
            
                <logic:notEmpty name="item" property="publicationYear">
                    Copyright <bean:write name="item" property="publicationYear" />
                </logic:notEmpty>
            
                
                <logic:equal name="item" property="rightsLink" value="true">
                 	<logic:notEmpty name="item" property="publisher">
                		<logic:empty name="item" property="author">
                		by <bean:write name="item" property="publisher" />.   
                		</logic:empty>
	                Reproduced with permission of <bean:write name="item" property="publisher" />
	                </logic:notEmpty>	
                </logic:equal>
                
                <logic:equal name="item" property="rightsLink" value="false">
                <logic:notEmpty name="item" property="rightsholder">
                <logic:empty name="item" property="author">
                   by <bean:write name="item" property="rightsholder" />.
                  </logic:empty>    
                   Reproduced with permission of <bean:write name="item" property="rightsholder" />            
                </logic:notEmpty>
                </logic:equal>
                                               
                <logic:notEmpty name="item" property="touName">
                   in the format <bean:write name="item" property="touName" /> via Copyright Clearance Center.             
                </logic:notEmpty>
                                              
                
            </b>
            <br /><br />
        <logic:notEmpty name="item" property="externalCommentTerm">
              
            <b>Terms:</b> <bean:write name="item" property="externalCommentTerm" />        
            <br /><br />
 
        </logic:notEmpty>
 
        <div class="horiz-black-rule"></div>
        <br />

    </logic:iterate>
</div>
</logic:equal>

<logic:notEqual name="reviewSubmitCartActionFormCOI" property="termsForm.hasTerms" value="true">
<div class="callout-light">
  <H2>There are no Citations.</H2>
</div>
</logic:notEqual>

<br />

</td></tr>
</table>
<table border="0" width="100%" cellpadding="3">
  <tr>
    <td align="center">
      <input type="submit" name="exit" value="Close" onclick="window.close();" style="width: 120 px;">
    </td>
  </tr>
</table>


<!-- script type="text/javascript">

    bV = parseInt(navigator.appVersion);
    if (bV >= 4) window.print();

    //onLoad= window.print();
    //window.close();


</script-->
