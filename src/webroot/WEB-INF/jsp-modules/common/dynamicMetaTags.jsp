<%@ page contentType="text/html;charset=windows-1252"  language="java" %>
<%@ page errorPage="/jspError.do" %>

<%@ page import="com.copyright.ccc.business.security.UserContextService" %>
<%@ page import="com.copyright.ccc.business.security.CCUserContext"      %>

<%@ taglib uri="/WEB-INF/tld/struts-html.tld"  prefix="html"     %>  
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld"  prefix="bean"     %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"    %>
<%@ taglib uri="/WEB-INF/tld/cc2-security.tld" prefix="security" %>
<%@ taglib uri="/WEB-INF/tld/cc2-util.tld"     prefix="util"     %>

<security:ifUserAnonymous not="true">
    <% 
        CCUserContext ccUserContext = UserContextService.getUserContext();

        String userName = ccUserContext == null || 
            ccUserContext.getActiveAppUser() == null ? 
                "" : ccUserContext.getActiveAppUser().getUsername();
    %>
    <bean:define id="currentUsername"><%= userName %></bean:define>
    <meta name="DCS.dcsaut" content="<bean:write name="currentUsername" />" />
</security:ifUserAnonymous>