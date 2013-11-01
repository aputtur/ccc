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
    
    <h1>Access Denied</h1>
    <br/>
    
    <div id="sectionTableContainer">
    
    <table class="sectionTable" style="width: 90%;">
    
        <tr>
        <td class="tableTextLeft">
        Thank you for your interest in copyright.com.<br>
        <br>
        <br>
        If you believe you should have access to information via 
        this website, please contact <html:link action="home">Copyright Clearance Center</html:link>:<br>
        <br>
        &nbsp;&nbsp;&nbsp;&nbsp;Phone: 978-750-8400<br>
        &nbsp;&nbsp;&nbsp;&nbsp;e-mail: <a href="mailto:info@copyright.com">info@copyright.com</a><br>
        <br>
        <br>
        Otherwise, please <html:link action="home">click here</html:link> for more information about copyright.com.<br>
        </td>
        </tr>
    
    </table>
    
    </div>
    
</div>
