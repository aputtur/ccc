<%@ page contentType="text/html" %>
<%@ page errorPage="/jspError.do" %>
<%@ page import="com.copyright.base.Constants" %>
<%@ page import="com.copyright.base.config.AppServerConfiguration" %>
<%@ page import="com.copyright.ccc.web.WebConstants" %>

<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>

<%
    String notAuthorizedMsg = 
        (String) session.getAttribute( WebConstants.SessionKeys.NOT_AUTHORIZED_MSG );
    
    String visibleMsg = Constants.EMPTY_STRING;
    
    if ( AppServerConfiguration.isDEV() || AppServerConfiguration.isLOCAL() )
    {
        visibleMsg = notAuthorizedMsg;
    }
%>

<div id="mainContainer">

    <h2>Not Authorized</h2>
    
    <div id="sectionTableContainer">
    
    <table class="sectionTable" style="width: 90%;">

        <tr>
        <td class="tableTextLeft">
    
        <bean:message key="security.authorization.checkFailed" />
        <br>
        <br>
        <%= visibleMsg %>
        <br>
        <br>
        
        <!--
        <% // Dump message to a comment in the response for debugging purposes.
    
        String commentMsg = Constants.EMPTY_STRING;    
        if ( AppServerConfiguration.isDEV() || AppServerConfiguration.isLOCAL() )
        {
            // Message already printed visibly.
        }
        else if ( AppServerConfiguration.isPRD() )
        {
            // Don't expose messages to user in PROD.
        }
        else
        {
            commentMsg = visibleMsg;
        }
        %>
        
        <%= commentMsg %>
        -->
        
        </td>
        </tr>
    
    </table>
    
    </div>
    
</div>
