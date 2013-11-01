<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ page errorPage="/jspError.do" %>
<%@ page import="com.copyright.ccc.business.data.OrderLicense" %>
<style>
.formatPre {

font-family:verdana,sans-serif;
font-size:11px;
white-space: pre-wrap; /* css-3 */
white-space: -moz-pre-wrap !important; /* Mozilla, since 1999 */
white-space: -pre-wrap; /* Opera 4-6 */
white-space: -o-pre-wrap; /* Opera 7 */
word-wrap: break-word; /* Internet Explorer 5.5+*/
_white-space: pre; /* IE only hack to re-specify in addition to word-wrap */

 }

</style>
<link rel="stylesheet" type="text/css" href="<html:rewrite page='/resources/commerce/css/noPrint.css'/>">
<link rel="stylesheet" type="text/css" href="<html:rewrite page='/resources/commerce/css/terms.css'/>">
<link rel="stylesheet" type="text/css" href="<html:rewrite page='/resources/commerce/css/ccc-new.css'/>">

<table border="0" width="100%" cellpadding="0" style="border-collapse: collapse" id="table1">
<tr><td>
<%-- <h2>Confirmation Number: <bean:write name="reviewSubmitCartActionFormCOI" property='confirmNumber'/></h2> --%>
<h2>Confirmation Number: <bean:write name="reviewSubmitCartActionFormCOI" property='termsForm.confirmNumber'/></h2>
</td>
</tr>
<tr><td>
<table border="0" cellpadding="0" style="border-collapse: collapse" width="100%" class="sectionTable">
    <tr><td><h2>Special Rightsholder Terms & Conditions</h2></td></tr>
    <tr><td colspan="2" class="tableHeaderCell">The following terms & conditions apply to the specific publication under which they are listed</td></tr>
</table>
<br />
<% int rowIndex = 0; %>

<logic:notEqual name="reviewSubmitCartActionFormCOI" property="termsForm.hasTerms" value="true">
<div class="callout-light">
  <H2>There are no special terms.</H2>
</div>
</logic:notEqual>


<br />


<div class="callout-light">

    
  <logic:iterate name="reviewSubmitCartActionFormCOI" property="termsForm.terms" id="item" indexId="idx" type="OrderLicense">
  
<%
rowIndex = idx.intValue();
String rowClass;
if (rowIndex%2 ==0) { rowClass = "termEven"; }
else { rowClass = "termOdd"; }
%>

     <logic:equal name="item" property="rightsLink" value="true">
       <logic:notEmpty name="item" property="licenseTerms">
           <strong><bean:write name="item" property="publicationTitle" /></strong>
            <br/>
            <strong>Permission type:</strong> <bean:write name="item" property="categoryName" />
			  <br/>
			    <strong>Type of use:</strong>  <bean:write name="item" property="touName" />
			  <br/><br/>
           </logic:notEmpty>
        </logic:equal>
<logic:equal name="item" property="rightsLink" value="false">
                       
              <strong> <bean:write name="item" property="publicationTitle" /></strong>
			           <br/>
            				<strong>Permission type:</strong> <bean:write name="item" property="categoryName" />
			  			<br/>
			    			<strong>Type of use:</strong>  <bean:write name="item" property="touName" />
			            	<br /><br />
			              <div class="callout-light">	
			            	 <logic:notEmpty name="item" property="rightsQualifyingStatement">
			            <div><span class="floatleft"><img src="/media/images/icon_alert.gif" border="0" alt="alert">&nbsp;<bean:write name="item" property="rightsQualifyingStatement"/>&nbsp;&nbsp;&nbsp;</span></div>
			            <br /><br />
			        </logic:notEmpty>
			        
			        <logic:notEmpty name="item" property="externalCommentTerm">                                     
			            <bean:write name="item" property="externalCommentTerm" />        
			            <br /><br />
			        </logic:notEmpty>
			            
			            	
			       
						<logic:equal name="item" property="academic" value="true">
    						    						
    						<logic:equal name="item" property="productCd" value="ECC">
    							<jsp:include page="/WEB-INF/jsp-modules/common/terms-academic-ecc.jsp" flush="true"></jsp:include>
							</logic:equal>
						
							<logic:equal name="item" property="productCd" value="APS">
    							<jsp:include page="/WEB-INF/jsp-modules/common/terms-academic-aps.jsp" flush="true"></jsp:include>
							</logic:equal>
							
						</logic:equal>

					<logic:equal name="item" property="academic" value="false">
						<logic:equal name="item" property="rightsLink" value="false">
    						
    						<logic:equal name="item" property="productCd" value="TRS">
    								<jsp:include page="/WEB-INF/jsp-modules/common/terms-non-academic-trs.jsp" flush="true"></jsp:include>
    							</logic:equal>
    					
    							<logic:equal name="item" property="productCd" value="DPS">
    								<jsp:include page="/WEB-INF/jsp-modules/common/terms-non-academic-dps.jsp" flush="true"></jsp:include>
    							</logic:equal>
    					
    							<logic:equal name="item" property="productCd" value="RLS">
    								<jsp:include page="/WEB-INF/jsp-modules/common/terms-non-academic-rls.jsp" flush="true"></jsp:include>
    							</logic:equal>
    							
    					</logic:equal>
					</logic:equal>
					</div>
					<br />
        <div class="horiz-black-rule"></div>
			            <br />
        
</logic:equal>
        
     <logic:equal name="item" property="rightsLink" value="true">    
         <logic:notEmpty name="item" property="licenseTerms">
         <table style="table-layout: fixed;">
         <tr><td>
           <div class="callout-light">
          <pre class="formatPre"><bean:write name="item" property="licenseTerms" filter="false" /></pre>
          </div>
			            <br />
			              <div class="horiz-black-rule"></div><br />
			              </td></tr>
			</table>              
         </logic:notEmpty>
     </logic:equal>
        
       
                         
    </logic:iterate>
    
</div>  




</td></tr>
</table>
<table border="0" width="100%" cellpadding="3">
  <tr>
    <td align="center">
      <input type="submit" name="exit" value="Close" onclick="window.close();" style="width: 120 px;">
    </td>
  </tr>
</table><br />

<p style="page-break-before: always"/> 

<!--  script type="text/javascript">

    bV = parseInt(navigator.appVersion);
    if (bV >= 4) window.print();

    //onLoad= window.print();
    //window.close();


</script-->