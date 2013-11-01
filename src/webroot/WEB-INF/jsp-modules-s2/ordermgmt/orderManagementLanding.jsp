<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<div>

<s:url id="orderURL" action="searchOrder" namespace="/om/history">
   <s:param name="quickTabSelected" value="ordermgmt.menu.order"/>
</s:url>
<s:url id="publisherURL" includeParams="none" value="/admin/publisherMaintenance.do">
	<s:param name="operation" value="findPublishers"/>
</s:url>
<s:url id="activityReportURL" includeParams="none" value="/viewReports.do"/>
<s:url id="autoDunningURL" includeParams="none" value="/admin/autoDunning.do"/>
<s:url id="viewInvoiceURL" includeParams="none" value="/admin/viewPaidInvoiceReports.do"/>
<s:url id="emulateURL" includeParams="none" value="/admin/startEmulationForm.do"/>
<s:url id="roleURL" includeParams="none" value="/admin/userRolesForm.do" />
<s:url id="userAdminURL" includeParams="none" value="/admin/userAdmin.do">
</s:url>

<div style="margins: 40px;padding: 50px">
									
	<table cellpadding="0" cellspacing="0" border="1" bordercolor="white">
	
	<s:if test="(hasEnterAdjPrivelege || hasCommitAdjPrivelege || hasOrderSearchPrivelege)">
		<tr><td style="background-color: #CCCCCC; width: 150px; height: 50px; text-align: center;"><a style="font-weight: bold;" href="<s:property value="#orderURL"/>"><s:text name="ordermgmt.landing.menu.management"/></a></td></tr>
	</s:if>
	<s:if test="hasManagePubInfoPrivelege">
		<tr><td style="background-color: #CCCCCC; width: 150px; height: 50px; text-align: center;"><a style="font-weight: bold;" target="_blank" href="<s:property value="#publisherURL"/>"><s:text name="ordermgmt.landing.menu.publisher"/></a></td></tr>
	</s:if>
	<s:if test="hasViewReportsPrivelege">
		<tr><td style="background-color: #CCCCCC; width: 150px; height: 50px; text-align: center;"><a style="font-weight: bold;" target="_blank" href="<s:property value="#activityReportURL"/>"><s:text name="ordermgmt.landing.menu.activity.report"/></a></td></tr>
	</s:if>
	<s:if test="hasAutoDunningPrivelege">
		<tr><td style="background-color: #CCCCCC; width: 150px; height: 50px; text-align: center;"><a style="font-weight: bold;" target="_blank" href="<s:property value="#autoDunningURL"/>"><s:text name="ordermgmt.landing.menu.auto.dunning"/></a></td></tr>
	</s:if>
	<s:if test="hasViewReportsPrivelege">
		<tr><td style="background-color: #CCCCCC; width: 150px; height: 50px; text-align: center;"><a style="font-weight: bold;" target="_blank" href="<s:property value="#viewInvoiceURL"/>"><s:text name="ordermgmt.landing.menu.view.invoice"/></a></td></tr>
	</s:if>
	<s:if test="hasEmulatePrivelege">
		<tr><td style="background-color: #CCCCCC; width: 150px; height: 50px; text-align: center;"><a style="font-weight: bold;" target="_blank" href="<s:property value="#emulateURL"/>"><s:text name="ordermgmt.landing.menu.emulate"/></a></td></tr>
	</s:if>
	<s:if test="hasManageRolesPrivelege">
		<tr><td style="background-color: #CCCCCC; width: 150px; height: 50px; text-align: center;"><a style="font-weight: bold;" target="_blank" href="<s:property value="#roleURL"/>"><s:text name="ordermgmt.landing.menu.roles"/></a></td></tr>
	</s:if>
	<s:if test="hasManageUserInfoPrivelege">
		<tr><td style="background-color: #CCCCCC; width: 150px; height: 50px; text-align: center;"><a style="font-weight: bold;" target="_blank" href="<s:property value="#userAdminURL"/>"><s:text name="ordermgmt.landing.menu.userAdmin"/></a></td></tr>
	</s:if>
	</table>

</div>
</div>