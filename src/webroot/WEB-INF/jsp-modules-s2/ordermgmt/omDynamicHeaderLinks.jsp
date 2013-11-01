
<%@ page import="com.copyright.ccc.business.security.UserContextService" %>
<%@ page import="com.copyright.ccc.web.CC2RequestProcessor" %>
<%@ page import="com.copyright.ccc.business.services.cart.CartServices" %>

<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="security" uri="/WEB-INF/tld/cc2-security.tld" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/cc2-util.tld" %>
<%@ page import="com.copyright.ccc.business.security.CCUserContext" %>
<% 
    /* 
     * possible format for login link using "bounce" action:
     * <html:form method="POST" action="/loginBounce.do"><a href="#" onclick="document.forms[0].submit()">Log in</a><input type="hidden" name="bounceFrom" value="%= request.getRequestURI() %"/></html:form> |
     */
%>

    <security:ifUserAnonymous not="true">
        <span style="font-size: 10px;font-weight: bold;" id="welcome">Welcome, 
            <% CCUserContext ccUserContext = UserContextService.getContextFromSession();
                   String userFirstName = ccUserContext == null || ccUserContext.getActiveUser() == null ? "" : ccUserContext.getActiveUser().getFirstName(); 
            %>
            <%= userFirstName %>
        </span>&nbsp;
        <span id="not-you">(<a style="font-size: 9px;" href="<s:url value="/logout.do?operation=removeCookies"/>">Not you?</a>)</span>
    </security:ifUserAnonymous>

    <util:else>
        <span style="font-size: 10px;font-weight: bold;" id="welcome">Welcome</span>
    </util:else>
<security:ifUserAnonymous>
    &nbsp;<a style="font-size: 9px;" href="<s:url value="/loginBounce.do"/>">Log in</a>
</security:ifUserAnonymous>

<util:else>
    <security:ifUserAuthenticated>
        &nbsp;<a style="font-size: 9px;" href="<s:url value="/logout.do?operation=logoutAuthorizedUser"/>">Log out</a>
    </security:ifUserAuthenticated>
    
    <util:else>
        &nbsp;<a style="font-size: 9px;" href="<s:url value="/loginBounce.do"/>">Log in</a>
    </util:else>
</util:else>


