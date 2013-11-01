<%@ page contentType="text/html;charset=windows-1252"  language="java" %>
<%@ page errorPage="/jspError.do" %>

<%@ page import="com.copyright.ccc.business.security.UserContextService" %>
<%@ page import="com.copyright.ccc.web.CC2RequestProcessor" %>
<%@ page import="com.copyright.ccc.business.services.cart.CartServices" %>
<%@ page import="com.copyright.ccc.config.CC2Configuration" %>

<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>  
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>  
<%@ taglib prefix="security" uri="/WEB-INF/tld/cc2-security.tld" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/cc2-util.tld" %>
<%@ page import="com.copyright.ccc.business.security.CCUserContext" %>
<% 
    /* 
     * possible format for login link using "bounce" action:
     * <html:form method="POST" action="/loginBounce.do"><a href="#" onclick="document.forms[0].submit()">Log in</a><input type="hidden" name="bounceFrom" value="%= request.getRequestURI() %"/></html:form> |
     */
%>
<bean:define id="help_url"><%= CC2Configuration.getInstance().getHelpURL() %></bean:define>
<div style="height: 28px;">
<div class="tcol" style="text-align: right;">
    <security:ifUserAnonymous not="true">
        <span id="welcome">Welcome, 
            <% CCUserContext ccUserContext = UserContextService.getContextFromSession();
                   String userFirstName = ccUserContext == null || ccUserContext.getActiveUser() == null ? "" : ccUserContext.getActiveUser().getFirstName(); 
            %>
            <%= userFirstName %>
        </span><br />
        <span id="not-you"><html:link page="/logout.do?operation=removeCookies">Not you?</html:link></span>
    </security:ifUserAnonymous>

    <util:else>
        <span id="welcome">Welcome</span>
    </util:else>
</div>
<!-- <div class="tcol">|</div> -->
<div class="tcol">
<security:ifUserAnonymous>
    <html:link action="/loginBounce.do">Log in</html:link>
</security:ifUserAnonymous>

<util:else>
    <security:ifUserAuthenticated>
        <html:link page="/logout.do?operation=logoutAuthorizedUser">Log out</html:link>
    </security:ifUserAuthenticated>
    
    <util:else>
        <html:link action="/loginBounce.do">Log in</html:link>
    </util:else>
</util:else>
</div>
<security:ifAdminResourceRequested></security:ifAdminResourceRequested>
<util:else>
<div class="tcol">|</div>
<div class="tcol"><html:link action="/cart.do" styleClass="icon-cart">Cart (<%=CartServices.getNumberOfItemsInCart()%>)</html:link></div>
<div class="tcol">|</div>
<div class="tcol"><html:link action="/manageAccount.do">Manage</html:link><br /><html:link action="/manageAccount.do">Account</html:link></div>
<div class="tcol">|</div>
<div class="tcol"><a href="http://guest.cvent.com/d/bdqgxd" target="_blank">Feedback</a></div>
<div class="tcol">|</div>
<div class="tcol"><a href="<bean:write name="help_url" />" target="_blank">Help</a></div>
<div class="tcol">|</div>
<div class="tcol"><a href="http://chat.copyright.com/webchat/" target="_blank" class="icon-chat">&nbsp;</a></div>
<div class="tcol"><a href="http://chat.copyright.com/webchat/" target="_blank">Live</a><br /><a href="http://chat.copyright.com/webchat/" target="_blank">Help</a></div>
</util:else>
</div>