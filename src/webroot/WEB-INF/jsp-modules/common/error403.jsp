<%@ page import="com.copyright.ccc.config.CC2Configuration" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld"  prefix="bean" %>

<bean:define id="help_url"><%= CC2Configuration.getInstance().getHelpURL() %></bean:define>
<% String onHold = request.getParameter( "oh" ); %>

<div style="padding: 10px;">
    <h2>Access Denied</h2>

    <% if ( onHold != null ) { %>
    <p>Your account has been placed on hold.  Please contact customer support.</p>
    <% } %>
    <br>
    <p>
        <ul>
            <li><a href="/home.do"> Go to the copyright.com Home page</a>.</li>      	
        </ul>
    </p>
    <p>
        <br />Need assistance?  Visit the <a href="<bean:write name="help_url" />">help</a> area or <a href="mailto:info@copyright.com">contact customer support</a>.
    </p>		
</div>