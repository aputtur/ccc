<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ page errorPage="/jspError.do" %>
<style>
.formatPre {

font-family:verdana,sans-serif;
font-size:11px;
white-space: pre-wrap; /* css-3 */
white-space: -moz-pre-wrap; /* Mozilla, since 1999 */
white-space: -pre-wrap; /* Opera 4-6 */
white-space: -o-pre-wrap; /* Opera 7 */
word-wrap: break-word; /* Internet Explorer 5.5+ */
_white-space: pre; /* IE only hack to re-specify in addition to word-wrap */

 }

</style>


<link rel="stylesheet" type="text/css" href="<html:rewrite page='/resources/commerce/css/terms.css'/>">
<link rel="stylesheet" type="text/css" href="<html:rewrite page='/resources/commerce/css/ccc-new.css'/>">

<jsp:useBean id="rightsLinkQuickPriceActionForm" scope="session" class="com.copyright.ccc.web.forms.RightsLinkQuickPriceActionForm"/>

<table border="0" width="100%" cellpadding="0" style="border-collapse: collapse" id="table1">
<tr><td>
<table border="0" cellpadding="0" style="border-collapse: collapse" width="100%" class="sectionTable">
    <tr><td><h2>Special Rightsholder Terms & Conditions</h2></td></tr>
    <tr><td colspan="2" class="tableHeaderCell">The following terms & conditions apply to the specific publication under which they are listed</td></tr>
</table>
<br />

<div class="callout-light">

		<logic:notEmpty name="rightsLinkQuickPriceActionForm" property="terms">
           <strong><bean:write name="rightsLinkQuickPriceActionForm" property="work.fullTitle" /></strong>
            <br/>
            <strong>Permission type:</strong> <bean:write name="rightsLinkQuickPriceActionForm" property="selectedCategory" />
			  <br/>
			    <strong>Type of use:</strong>  <bean:write name="rightsLinkQuickPriceActionForm" property="selectedTou" />
			  <br/><br />
           </logic:notEmpty>
        

    
      
         <logic:notEmpty name="rightsLinkQuickPriceActionForm" property="terms">
         <table style="table-layout: fixed;">
         <tr><td>
           <div class="callout-light">
          <pre class="formatPre"><bean:write name="rightsLinkQuickPriceActionForm" property="terms" filter="false" /></pre>
          </div>
			            <br />
			              <div class="horiz-black-rule"></div><br />
			              </td></tr>
			</table>              
         </logic:notEmpty> 
         
        
			            	
			      		        		          
</div>  


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
