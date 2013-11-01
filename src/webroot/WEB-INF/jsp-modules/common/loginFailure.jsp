<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>

<%@ page contentType="text/html" %>
<%@ page errorPage="/jspError.do" %>

<%/*
<bean:define id="contactURL">
    <bean:message bundle="naples" key="accessDenied.contact.url" />
</bean:define>

<bean:define id="contactPhone">
    <bean:message bundle="naples" key="accessDenied.contact.phone" />
</bean:define>

<bean:define id="contactEmail">
    <bean:message bundle="naples" key="accessDenied.contact.email" />
</bean:define>

<bean:define id="moreInfoURL">
    <bean:message bundle="naples" key="accessDenied.moreInfo.url" />
</bean:define>
*/%>
<div id="mainContainer">
       
    <div id="sectionTableContainer">
    
    <table class="sectionTable" style="width: 90%;">
    
        <tr>
        <td class="tableTextLeft">
        For more information, please contact Customer Relations at  
        <html:link action="home">Copyright Clearance Center</html:link>:<br>
        <br>
        &nbsp;&nbsp;&nbsp;&nbsp;Phone: 978-646-2600 <br>
        &nbsp;&nbsp;&nbsp;&nbsp;e-mail: <a href="mailto:info@copyright.com">info@copyright.com.</a><br>
        </td>
        </tr>
    
    </table>
    
    </div>
    
</div>
