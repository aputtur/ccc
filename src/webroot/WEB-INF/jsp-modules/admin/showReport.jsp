<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ page errorPage="/jspError.do" %>
<html>
<head>
 <style type="text/css">
#txt {
font-family:Verdana, Arial, Helvetica, sans-serif;
color:#333333;
font-size:14px;
text-align:center;
}
table td {
   font-family:Verdana, Arial, Helvetica, sans-serif;
   font-size:11px;
   color:#333333;
   text-transform: uppercase;
   text-align:center;
   position:relative;
}
</style>
<title>Report</title>
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
</head>
  <body>
  <br/><br/>
  <div id="txt" style="width: 100%; height: 100%;"> 
  <html:errors/>
    <logic:iterate name="viewReportsForm" property="reportData" id="aRow">
      <bean:write name="aRow" filter="false"/>
    </logic:iterate>
  </div>
  </body>
<HEAD>
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
</HEAD>
</html>
