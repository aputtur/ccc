<%@ page errorPage="/jspError.do" %>
<%@ page import="com.copyright.appintegrity.version.VersionDataUtils"%>
<%@ page import="com.copyright.base.config.AppServerConfiguration" %>
<%@ page import="com.copyright.ccc.business.data.CCUser" %>
<%@ page import="com.copyright.ccc.business.security.SecurityUtils" %>
<%@ page import="com.copyright.ccc.business.security.UserContextService" %>
<%@ page import="com.copyright.ccc.business.services.AdminServices" %>
<%@ page import="com.copyright.ccc.config.CC2Configuration" %>
<%@ page import="com.copyright.ccc.config.CybersourceConfiguration"%>
<%@ page import="com.copyright.ccc.config.WWWBuildData" %>
<%@ page import="com.copyright.ccc.business.services.SystemStatus" %>
<%@ page import="com.copyright.ccc.business.services.user.User" %>
<%@ page import="com.copyright.service.ServiceInfoUtil" %>
<%@ page import="com.copyright.svc.artransaction.ARTransactionServiceBuildData"%>
<%@ page import="com.copyright.svc.centralQueue.CentralQueueServiceBuildData"%>
<%@ page import="com.copyright.svc.extEmail.ExtEmailBuildData" %>
<%@ page import="com.copyright.svc.ldapuser.LdapUserBuildData"%>
<%@ page import="com.copyright.svc.order.OrderServiceBuildData" %>
<%@ page import="com.copyright.svc.rights.RightsServiceBuildData" %>
<%@ page import="com.copyright.svc.rightsResolver.RightsResolverServiceBuildData" %>
<%@ page import="com.copyright.svc.rlOrder.api.RLOrderServicesInterface"%>
<%@ page import="com.copyright.svc.rlUser.api.RLUserServicesInterface"%>
<%@ page import="com.copyright.svc.searchRetrieval.SearchRetrievalServiceBuildData" %>
<%@ page import="com.copyright.svc.telesales.TelesalesServiceBuildData"%>
<%@ page import="com.copyright.svc.tf.TFServiceBuildData"%>
<%@ page import="com.copyright.svc.userInfo.UserInfoServiceBuildData" %>
<%@ page import="com.copyright.svc.worksremote.WorksRemoteServiceBuildData" %>
<%@ page import="com.copyright.workbench.io.FileUtils"%>
<%@ page import="com.copyright.workbench.net.SimpleIPAddress" %>
<%@ page import="com.copyright.workbench.sql.DBConnectionInfo" %>
<%@ page import="com.copyright.workbench.sql.DBUtils" %>

<%@ page import="java.util.jar.Attributes" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.LinkedHashMap" %>

<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib prefix="cfg" uri="http://xmlns.copyright.com/tld/config.tld" %>
<%@ taglib prefix="crs" uri="http://xmlns.copyright.com/tld/crs.tld" %>


<style type="text/css">
#wrapper{
	width: 1000px;
}

body {
    font-family: sans-serif;
    font-size: 11px;
}

table.dhTable td {
    padding: 2px;
    border: 1px solid #999999;
    background-color: white;
    font-size: 11px;
}

table.dhTable {
    padding: 0px;
    border-collapse: collapse;
}

</style>

<%
    CCUser ccUser = UserContextService.getActiveAppUser();
    User sharedUser = UserContextService.getActiveSharedUser();
    CCUser authUser = UserContextService.getAuthenticatedAppUser();
    
	String rlOrderFilename = 
		RLOrderServicesInterface.class.getProtectionDomain().getCodeSource().getLocation().getFile();
	rlOrderFilename = FileUtils.stripPath( rlOrderFilename );

	String rlUserFilename = 
		RLUserServicesInterface.class.getProtectionDomain().getCodeSource().getLocation().getFile();
	rlUserFilename = FileUtils.stripPath( rlUserFilename );
%>
<%
    SimpleIPAddress ip = new SimpleIPAddress( SecurityUtils.determineClientIP( request ) );
    boolean showPage = SecurityUtils.isInternalIP( ip ) || SecurityUtils.isLoopbackIP( ip );
    
    if ( showPage ) 
    {
%>

<div id="developerHomeMessage" style="width: 100%">

    <h2>WWW Developer Home Page</h2>
    
    <br/>


	<table border="1" cellpadding="1">
	<tr>
		<td><cfg:buildData buildData="<%= WWWBuildData.getInstance() %>" header="WWW Build Data" tableAttr="border=1 cellpadding=1"/></td>
		<td><cfg:deployment tableAttr="border=1 cellpadding=1"/></td>
	</tr>
	</table>
	<table border="1" cellpadding="1">
	<tr>
		<td><cfg:datasource jndiName="<%= CC2Configuration.getInstance().getDatasourceJNDIName() %>" header="CC Datasource" tableAttr="border=1 cellpadding=1"/></td>
		<td><table border="1" cellpadding="1">
			<tr><td colspan="2"><b>System Status</b></td></tr>
	        <tr>
	            <td><%= SystemStatus.isTelesalesUp()?"Telesales is up":"<span style=\"color: red;font-weight: bold;\">Telesales is down</span>" %></td>
	            <td></td>
	        </tr>
	        <tr>
	            <td><%= SystemStatus.isRightslinkUp()?"Rightslink is up":"<span style=\"color: red;font-weight: bold;\">Rightslink is down</span>" %></td>
				<td><%= !SystemStatus.isRightslinkUp() && CC2Configuration.getInstance().getForciblySimulateRLDown()?" (forcibly simulated)":"" %></td>
	        </tr>
	        <tr>
	            <td><%= SystemStatus.isCybersourceSiteUp()?"Cybersource is up":"<span style=\"color: red;font-weight: bold;\">Cybersource is down</span>" %></td>
	            <td></td>
	        </tr>
	    </table></td>
	</tr>
    </table>
	<hr/>

	<crs:configurationView module="<%= CC2Configuration.MODULE_NAME %>" version="<%= CC2Configuration.MODULE_VERSION %>" tableAttr="border=1 cellpadding=1"/>
	<hr/>

	<table><tr><td>

    <cfg:buildData buildData="<%= ARTransactionServiceBuildData.getInstance() %>" header="svc-arTransaction" tableAttr="border=1 cellpadding=1"/>
	<table border="1" cellpadding="1">
        <tr>
            <td>Endpoint URL</td>
            <td><%= CC2Configuration.getInstance().getARTransactionServiceURL() %></td>
        </tr>
    </table>
	<cfg:buildData buildData="<%= CentralQueueServiceBuildData.getInstance() %>" header="svc-centralQueue" tableAttr="border=1 cellpadding=1"/>
	<table border="1" cellpadding="1">
        <tr>
            <td>Endpoint URL</td>
            <td><%= CC2Configuration.getInstance().getCentralQueueServiceURL() %></td>
        </tr>
    </table>
    <cfg:buildData buildData="<%= ExtEmailBuildData.getInstance() %>" header="svc-extEmail" tableAttr="border=1 cellpadding=1"/>
	<table border="1" cellpadding="1">
        <tr>
            <td rowspan="2">Endpoint URL</td>
            <td><%= CC2Configuration.getInstance().getExtEmailServiceUrl() %></td>
        </tr>
    </table>
	<cfg:buildData buildData="<%= LdapUserBuildData.getInstance() %>" header="svc-ldapUser" tableAttr="border=1 cellpadding=1"/>
	<table border="1" cellpadding="1">
        <tr>
            <td>Endpoint URL</td>
            <td><%= CC2Configuration.getInstance().getLdapServiceURL() %></td>
        </tr>
    </table>
    <cfg:buildData buildData="<%= OrderServiceBuildData.getInstance() %>" header="svc-order" tableAttr="border=1 cellpadding=1"/>
	<table border="1" cellpadding="1">
		<tr>
            <td rowspan="2">Endpoint URLs</td>
            <td><%= CC2Configuration.getInstance().getCartServiceURL() %></td>
        </tr>
        <tr>
            <td><%= CC2Configuration.getInstance().getOrderServiceURL() %></td>
        </tr>
    </table>
	<cfg:buildData buildData="<%= RightsServiceBuildData.getInstance() %>" header="svc-rights" tableAttr="border=1 cellpadding=1"/>
	<table border="1" cellpadding="1">
        <tr>
            <td>Endpoint URL</td>
            <td><%= CC2Configuration.getInstance().getRightsWebServiceURL() %></td>
        </tr>
    </table>
    <cfg:buildData buildData="<%= RightsResolverServiceBuildData.getInstance() %>" header="svc-rightsResolver" tableAttr="border=1 cellpadding=1"/>
	<table border="1" cellpadding="1">
        <tr>
            <td rowspan="2">Endpoint URLs</td>
            <td><%= CC2Configuration.getInstance().getRightsResolverServiceURL() %></td>
        </tr>
        <tr>
            <td><%= CC2Configuration.getInstance().getRightsProcessorServiceURL() %></td>
        </tr>
    </table>
	<table border="1" cellpadding="1">
		<tr><td colspan="2"><b>svc-rlOrder</b></td></tr>
        <tr>
            <td>Filename</td>
            <td><%= rlOrderFilename %></td>
        </tr>
        <tr>
            <td>Endpoint URL</td>
            <td><%= CC2Configuration.getInstance().getRLOrderServiceURL() %></td>
        </tr>
    </table>
	<table border="1" cellpadding="1">
		<tr><td colspan="2"><b>svc-rlUser</b></td></tr>
        <tr>
            <td>Filename</td>
            <td><%= rlUserFilename %></td>
        </tr>
        <tr>
            <td>Endpoint URL</td>
            <td><%= CC2Configuration.getInstance().getRLUserServiceURL() %></td>
        </tr>
    </table>
	<cfg:buildData buildData="<%= SearchRetrievalServiceBuildData.getInstance() %>" header="svc-searchRetrieval" tableAttr="border=1 cellpadding=1"/>
	<table border="1" cellpadding="1">
        <tr>
            <td>Endpoint URL</td>
            <td><%= CC2Configuration.getInstance().getSearchRetrievalWebServiceURL() %></td>
        </tr>
    </table>
	<cfg:buildData buildData="<%= TelesalesServiceBuildData.getInstance() %>" header="svc-telesales" tableAttr="border=1 cellpadding=1"/>
	<table border="1" cellpadding="1">
        <tr>
            <td rowspan="3">Endpoint URLs</td>
            <td><%= CC2Configuration.getInstance().getTelesalesWebServiceURL() %></td>
        </tr>
        <tr>
            <td><%= CC2Configuration.getInstance().getTelesalesCompositeServiceURL() %></td>
        </tr>
        <tr>
            <td><%= CC2Configuration.getInstance().getTelesalesRightsholderServiceURL() %></td>
        </tr>
    </table>
	<cfg:buildData buildData="<%= TFServiceBuildData.getInstance() %>" header="svc-tf" tableAttr="border=1 cellpadding=1"/>
	<table border="1" cellpadding="1">
        <tr>
            <td>Endpoint URL</td>
            <td><%= CC2Configuration.getInstance().getTfWebServiceURL() %></td>
        </tr>
    </table>
    <cfg:buildData buildData="<%= UserInfoServiceBuildData.getInstance() %>" header="svc-userInfo" tableAttr="border=1 cellpadding=1"/>
	<table border="1" cellpadding="1">
        <tr>
            <td rowspan="2">Endpoint URLs</td>
            <td><%= CC2Configuration.getInstance().getCyberSourceServiceURL() %></td>
        </tr>
        <tr>
            <td><%= CC2Configuration.getInstance().getAccessControlServiceURL() %></td>
        </tr>
    </table>
    <cfg:buildData buildData="<%= WorksRemoteServiceBuildData.getInstance() %>" header="svc-worksremote" tableAttr="border=1 cellpadding=1"/>
	<table border="1" cellpadding="1">
        <tr>
            <td rowspan="2">Endpoint URL</td>
            <td><%= CC2Configuration.getInstance().getSvcWorksRemoteSearchEndpointUrl() %></td>
        </tr>
    </table>
    </td></tr></table>
	<hr/>
	
	<table border="1" cellpadding="1">
        <tr>
			<td><cfg:shared tableAttr="border=1 cellpadding=1"/></td>
			<td><cfg:datasource jndiName="<%= ServiceInfoUtil.getDefaultDatasource() %>" header="CCC Shared Datasource" tableAttr="border=1 cellpadding=1"/></td>
			 <%if (AppServerConfiguration.isInternal() ){ %>
			<td><cfg:datasource jndiName="<%= ServiceInfoUtil.getOraappsDatasource() %>" header="Oracle Apps Datasource" tableAttr="border=1 cellpadding=1"/></td>
			<%} %>
        </tr>
    </table>
	<table border="1" cellpadding="1">
        <tr>
			<td><cfg:versionData versionData="<%= VersionDataUtils.createVersionData( "com/copyright/rightslink/base/versiondata.txt" ) %>" header="RightslinkUiBase" tableAttr="border=1 cellpadding=1"/></td>
			<td><cfg:versionData versionData="<%= VersionDataUtils.createVersionData( "com/copyright/rightslink/html/versiondata.txt" ) %>" header="HtmlGenerator" tableAttr="border=1 cellpadding=1"/></td>
        </tr>
    </table>
	<table border="1" cellpadding="1">
        <tr>
			<td><cfg:cccBase tableAttr="border=1 cellpadding=1"/></td>
			<td><cfg:appIntegrity tableAttr="border=1 cellpadding=1"/></td>
			<td><cfg:svcCommon tableAttr="border=1 cellpadding=1"/></td>
        </tr>
    </table>
	<table border="1" cellpadding="1">
        <tr>
			<td><cfg:opi tableAttr="border=1 cellpadding=1"/></td>
			<td><cfg:cccMail tableAttr="border=1 cellpadding=1"/></td>
        </tr>
    </table>
	<hr/>
	
	<table><tr><td>
	<cfg:properties 
		properties="<%= CC2Configuration.getInstance().getProperties() %>"
		propertiesToMask="email.verification.password,rightslink.authentication.token" 
		header="<%= CC2Configuration.CONFIG_FILE %>" 
		tableAttr="border=1 cellpadding=1"/>
	<cfg:properties 
		properties="<%= CybersourceConfiguration.getInstance().getProperties() %>"
		header="<%= CybersourceConfiguration.getInstance().getFile() %>"
		propertiesToMask="publicKey" 
		tableAttr="border=1 cellpadding=1"/>
	<%
		Map<String,String> props = new LinkedHashMap<String,String>();
		props.put( "CC DB Schema Version", AdminServices.getDatabaseSchemaVersion() );
		props.put( "Shared Services DB Schema Version", ServiceInfoUtil.getDefaultSchemaVersion() );
	%>
	<cfg:map map="<%= props %>" header="Additional Properties" tableAttr="border=1 cellpadding=1" />
	<cfg:properties 
		properties="ccc-shared.properties"
		propertiesToMask="ldap.security.credentials,ldap.security.principal,comm.sendbatch.send.pwd,registration.db.password" 
		header="ccc-shared.properties" 
		tableAttr="border=1 cellpadding=1"/>
	</td></tr></table>
	<hr/>

	<%
		Map<String,String> oracleProps = new LinkedHashMap<String,String>();
		DBConnectionInfo ccConnInfo = DBUtils.getConnectionInfo( CC2Configuration.getInstance().getDatasourceJNDIName() );            	
		oracleProps.put( "CC", ccConnInfo.getMetaData().getDatabaseProductVersion() );
		DBConnectionInfo ssConnInfo = DBUtils.getConnectionInfo( ServiceInfoUtil.getDefaultDatasource() );            	
		oracleProps.put( "Shared Services", ssConnInfo.getMetaData().getDatabaseProductVersion() );
	%>
	<table><tr><td>
	<cfg:map map="<%= oracleProps %>" header="Oracle Databases" tableAttr="border=1 cellpadding=1"/>
	</td></tr></table>
	<hr/>
	
	<table><tr>
		<td><cfg:jboss tableAttr="border=1 cellpadding=1"/></td>
		<td><cfg:jdbc tableAttr="border=1 cellpadding=1"/></td>
	</tr></table>
	<table><tr><td>
	<cfg:jvm tableAttr="border=1 cellpadding=1"/>
	</td></tr></table>
	<hr/>

	<table><tr><td>
	<cfg:deployedApps header="Deployed Apps"/>
	</td></tr></table>
	<hr/>

    <br/>

</div>

<%
    }
    else
    {
%>
<div id="developerHomeMessage" style="width: 100%; height: 400px; background-color: #FFFFCC;overflow:auto">

    <br>
    You are not authorized to view the copyright.com development home page.

    <%
        if ( !AppServerConfiguration.isPRD() )
        {
    %>
    <br>
    <br><span class=instructions>(<%= SecurityUtils.determineClientIP( request ) %>)</span>
    <%
        }
    %>

</div>

<%
    }
%>
