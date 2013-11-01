 <%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%@ page import="java.util.*, java.lang.*"%>

<table width="100%">
		<tr>
			<td>
				<div style="width: 972px; border: 0px solid rgb(0, 0, 0); margin: 5px 10px 0px; padding: 0px;" id="order_header">
					<div style="width: 30%; float: left; padding: 0px; margin: 0px;">
						<h1 style="display: inline; color: rgb(111, 151, 187); padding: 0px; margin: 0px;">
							Confirmation
						</h1>
						&nbsp;
						<span id="sectionHeaderInline" style="font-size: 18px; color: rgb(111, 151, 187);">#&nbsp; <s:property value="confirmNumber"/>
						</span>
					</div>
					<div style="width: 60%; float: right; padding: 0px; margin: 0px;">
					</div>
				</div>
			</td>
		</tr>
</table>					


<table width="100%" border="1">
	<tr>
		<td  style="background-color: rgb(111, 151, 187); font-weight: bold; padding-top: 4px; font-size: 14px; color: white; ">
		&nbsp;&nbsp;&nbsp;&nbsp;Project: <s:property value="projectName"/>
		</td>
	</tr>
	
</table>	
	
	<script type="text/javascript">
		var newChangeDate="1"; //default to different values
		var oldChangeDate="2"; //default to different values
	</script>


	
	<s:iterator value="bundleAuditHistoryList" status="iterStatus">
	
		<script type="text/javascript">
			
			//if this is not the first time through here and the date/time has changed
			if (newChangeDate !="1" && oldChangeDate != newChangeDate)
			{
				document.write
				(
					"</table>"
				);
			}
			
			newChangeDate = '<s:property value = "changeDate"/> - <s:property value = "changeDateTime"/>';
			
			if (oldChangeDate != newChangeDate)
			{
				
				//writes white space separator two row deep
				document.write
				(
					"<table width=100% border=0><tr><td colspan=2 >&nbsp;</td></tr><tr><td colspan=2 >&nbsp;</td></tr></table>"
				);
				
				//writes an edit date and edited by header
				document.write
				(	
					"<table width=100% border=0><tr><td colspan=2 style='color: rgb(74,115,154); font-size: 11px; font-weight: bold; background-color: rgb(226, 233, 240)';>&nbsp;&nbsp; Edit date:&nbsp;<s:property value = "changeDate"/> &nbsp; at &nbsp; <s:property value = "changeDateTime"/></td><td style='color: rgb(74,115,154); font-size: 11px; font-weight: bold; background-color: rgb(226, 233, 240)';> Edited by:&nbsp;<s:property value = "changeUser"/></td></tr></td></table>"
				);
			
				//writes a table colun header row
				document.write
				(
					"<table align='center' width='80%' border='1'><tr style='background-color: #C6C6C6'><td width='34%' style='font-weight: bold'>Field Name</td><td width='33%' style='font-weight: bold'>Old Value</td><td width='33%' style='font-weight: bold'>New Value</td></tr>"
				);
				
				
			}
			oldChangeDate = newChangeDate;
		</script>	
	
		<s:if test="#iterStatus.odd">
			<tr  style="background-color: rgb(226, 233, 240);">
		
		</s:if>
		<s:else> 
			<tr>
		</s:else>
			<!-- writes a row of audit data -->
			<td width='34%'><s:property value = "fieldName"/></td>
			<td width='33'><s:property value = "oldValue"/></td>
			<td width='33%'><s:property value = "newValue"/></td>
		</tr>
		
	</s:iterator> 
	
	
	

</table>


