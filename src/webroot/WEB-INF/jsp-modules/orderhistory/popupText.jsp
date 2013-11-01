<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ page errorPage="/jspError.do" %>
<html>
<head>
<!--link rel="stylesheet" type="text/css" href="<html:rewrite page='/resources/styles/shoppingCart.css'/>">
<link rel="stylesheet" type="text/css" href="<html:rewrite page="/resources/styles/naples_base.css"/>"/-->
</head>
<body>
<!--div id="popupHeader">
<div id="mainHeaderContent">
    <div id="mainHeaderLeft">
        <img src="<html:rewrite page="/resources/images/logo_1a.gif"/>" />
    </div>
    <div id="mainHeaderRight">
        <img alt="Customer Logo" src='<html:rewrite page="/imageServer.do?operation=serveUserBrandingLogo" />' />
    </div>
</div>
</div-->
<table border="0" width="100%" cellpadding="3">
  <!--tr><td>Rightsholder Term Text</td></tr-->
  <tr>
    <td align="center">
      <html:textarea name="popupTextForm" property="text" readonly="true" rows="12" cols="60"/>
    </td>
  </tr>
</table>
<table border="0" width="100%" cellpadding="3">
  <tr>
    <td align="center">
      <input type="submit" name="exit" value="Close" onclick="window.close();">
    </td>
  </tr>
</table>
</body>
</html>
