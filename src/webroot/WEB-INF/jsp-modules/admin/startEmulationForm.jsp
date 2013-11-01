<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib prefix="logic" uri="/WEB-INF/tld/struts-logic.tld" %>

<logic:messagesPresent message="true">
    <div class="error"><html:errors /></div>
</logic:messagesPresent>
<br />
<br />
<br />

<html:form action="/admin/emulation" >
Username of user to emulate:<br>
<br />
<html:text property="emulationUsername" />&nbsp;<html:submit value="Begin Emulation Session"/>
<html:hidden property="operation" value="start"/>
</html:form>
