<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
        
<%@ page language="java" pageEncoding="utf8" contentType="text/html; charset=UTF8"%>
<%@ page errorPage="/jspError.do" %>

<%@ page import="javax.mail.internet.InternetAddress"                    %>
<%@ page import="com.copyright.svc.telesales.api.data.Location"          %>
<%@ page import="com.copyright.svc.telesales.api.data.Organization"      %>
<%@ page import="com.copyright.ccc.business.security.UserContextService" %>
<%@ page import="com.copyright.ccc.business.security.CCUserContext"      %>
<%@ page import="com.copyright.ccc.business.security.UserContextService" %>
<%@ page import="com.copyright.ccc.business.services.user.User"          %>

<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/tld/struts-bean.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/tld/struts-logic.tld" %>

<%@ taglib uri="/WEB-INF/tld/cc2-security.tld" prefix="security" %>

<%
    String emailAddress = "";
    String yourName = "";
    String companyName = "";
    String city = "";
    String state = "";
    String phoneNumber = "";
%>
<security:ifUserAuthenticated>
    <% 
        CCUserContext ccUserContext = UserContextService.getUserContext();
        User usr = null;
        
        if (ccUserContext != null)
            usr = ccUserContext.getAuthenticatedUser();

        if (usr != null) {
            InternetAddress addr = usr.getEmailAddress();
            if (addr != null) emailAddress = addr.getAddress();
            yourName = usr.getDisplayName();
            Organization org = usr.getOrganization();
            if (org != null) companyName = org.getOrganizationName();
            Location loc = usr.getMailingAddress();
            if (loc != null) {
                city = loc.getCity();
                state = loc.getState();
            }
            phoneNumber = usr.getPhoneNumber();
        }
    %>
</security:ifUserAuthenticated>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Request Coverage</title>
    <link href="<html:rewrite page="/resources/commerce/css/ccc-new.css"/>" rel="stylesheet" type="text/css" />
    <link href="<html:rewrite page="/resources/commerce/css/default.css"/>" rel="stylesheet" type="text/css" />
    <link href="<html:rewrite page="/resources/commerce/css/contextualHelp.css"/>" rel="stylesheet" type="text/css" />
    <script type="text/javascript" language="javascript" src="<html:rewrite page="/resources/commerce/js/behavior.js"/>"></script>
    <script type="text/javascript" language="javascript" src="<html:rewrite page="/resources/commerce/js/textarea_maxlen.js"/>"></script>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body class="cc2">
  <div id="popup_pane">
    <div id="popup_wrapper">
      <div id="popup_inner">
        <div id="popup">
          <h1 id="help-title">Request This Work</h1>
          <div id="popup-body">
              <p>
                <html:errors/>
                 The title you have selected is not covered under
                your annual license(s).<br/><br/>We will contact the publisher in an effort to add this material to our catalog for use under your annual license agreements and will notify you if we are able to do so. Some titles may take time to add.<br/><br/>
              </p><p>
                Please include your contact information below.
              </p><p>
                <br/>
                 
                <jsp:useBean id="notAvailableEmailForm" scope="session"
                             class="com.copyright.ccc.web.forms.NotAvailableEmailForm"/>
                 
              </p><html:form action="makerequest.do?operation=sendRequest&com.copyright.ispopup=true" styleId="form">
           <table border="0" cellpadding="0" cellspacing="0">
            <tr><td>Usage: </td><td><bean:write name="notAvailableEmailForm" property="product" /></td></tr>
            <tr><td>Title: </td><td><bean:write name="notAvailableEmailForm" property="title" /></td></tr>
            <tr><td><bean:write name="notAvailableEmailForm" property="idnoLabel" />: </td><td><bean:write name="notAvailableEmailForm" property="standardNumber" /></td></tr>
            <logic:notEmpty name="notAvailableEmailForm" property="publicationType"><tr><td>Pub Type: </td><td><bean:write name="notAvailableEmailForm" property="publicationType" /></td></tr></logic:notEmpty>
            <logic:notEmpty name="notAvailableEmailForm" property="publicationYear">
                <logic:notEqual name="notAvailableEmailForm" property="publicationYear" value="Through present">
                    <logic:notEqual name="notAvailableEmailForm" property="publicationYear" value="-">
                        <tr><td>Pub'n Year: </td><td><bean:write name="notAvailableEmailForm" property="publicationYear" /></td></tr>
                    </logic:notEqual>
                </logic:notEqual>
            </logic:notEmpty>
            <logic:notEmpty name="notAvailableEmailForm" property="publisher"><tr><td>Publisher: </td><td><bean:write name="notAvailableEmailForm" property="publisher" /></td></tr></logic:notEmpty>
            <logic:notEmpty name="notAvailableEmailForm" property="author"><tr><td>Author/Editor: </td><td><bean:write name="notAvailableEmailForm" property="author" /></td></tr></logic:notEmpty>
            <tr><td>Rightsholder: </td><td><bean:write name="notAvailableEmailForm" property="rightsholder" /></td></tr>
           </table><br/>
           <table border="0" cellpadding="0" cellspacing="0">
            <tr><td>Your name:* </td> <td><html:text name="notAvailableEmailForm" property="requesterName" styleClass="normal" value="<%= yourName %>" /></td></tr>
            <tr><td>Your email<br>address:* </td> <td><html:text name="notAvailableEmailForm" property="requesterEmailAddress" styleClass="normal" value="<%= emailAddress %>" /></td></tr>
            <tr><td>Company:* </td><td><html:text name="notAvailableEmailForm" property="requesterCompany" styleClass="normal" value="<%= companyName %>" /></td></tr>
            <tr><td>Phone:* </td><td><html:text name="notAvailableEmailForm" property="requesterPhoneNumber" styleClass="normal" value="<%= phoneNumber %>" /></td></tr>
            <tr><td>City:* </td><td><html:text name="notAvailableEmailForm" property="requesterCity" styleClass="normal" value="<%= city %>" /></td></tr>
            <tr><td>State:* </td><td><html:text name="notAvailableEmailForm" property="requesterState" styleClass="normal" value="<%= state %>" /></td></tr>
            <tr><td>Do you currently have an annual license with CCC?:* </td>
             <td>
              <html:select name="notAvailableEmailForm" property="requesterAnnualLicense" styleClass="normal">
               <html:option value="Yes">Yes</html:option>
               <html:option value="No">No</html:option>
              </html:select>
             </td>
            </tr>
            <tr><td colspan="2"><br />Additional information to help us identify the work:<br />(Examples: 4th Edition, Volume 3)</td>
            </tr>
            <tr><td colspan="2"><br /><TEXTAREA name="additionalInfo" class="xlarge" maxlength="255" showremain="limit"></TEXTAREA><br/></td></tr>
            <tr><td>&nbsp;</td></tr>
           </table>
           <html:image src="/media/images/btn_continue.gif" alt="Submit" align="right" border="0" />
          </html:form>
          * Required fields
          </div>
        </div>
        <div class="clearer"></div>
        <div id="popup-close"><a href="javascript:window.close();">Close</a></div>
      </div>
    </div>
    <div id="popup_bottomcorners"></div>
    <div id="footer_popup">
      <html:img src="/media/images/footer_logo.gif" alt="Copyright Clearance Center" width="194" height="24"/>
    </div>
  </div>
</body>
</html>
