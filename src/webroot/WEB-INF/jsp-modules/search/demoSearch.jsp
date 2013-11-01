<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page errorPage="/jspError.do" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
    <title>demoSearch</title>
  </head>
  <body>
    demo search page
    <html:form action="/demoSearch" >
        <input type="text" name="operation" value="executeBasicSearch"/>
        <html:text property="searchCriteria"/>
        <html:submit value="Go"/>
    </html:form>
  </body>
</html>