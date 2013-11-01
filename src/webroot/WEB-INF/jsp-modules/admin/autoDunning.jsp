<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib prefix="logic" uri="/WEB-INF/tld/struts-logic.tld"%>

<logic:messagesPresent message="true">
	<div class="error">
		<html:errors />
	</div>
</logic:messagesPresent>

<style type="text/css">
div#body {
	margin: 0px;
}

div#headerAutoDun {
	padding-top: 10px;
	padding-bottom: 20px;
	font-size: 12px;
	font-weight: bold;
}

div#content {
	
}

div#contentAutoDun {
	
}

div#leftAutoDun {
	font-weight: bold;
	width: 250px;
	float: left;
}

div#rightAutoDun {
	width: 300px;
	float: left;
}

div#footerAutoDun {
	
}

div#clearAutoDun {
	clear: both;
}
</style>
<script type="text/javascript">
	function doOperation(operationName) {
		document.getElementById("operation").value = operationName;
		document.getElementById("autoDunningForm").submit();
	}
</script>


<html:form styleId="autoDunningForm" action="/admin/autoDunning">
	<div id="clearAutoDun"></div>
	<div id="headerAutoDun">
		Past Due Invoice Auto-Dunning Email Notification
	</div>
	<div class="horiz-rule">
	</div>
	<span style="font-weight: bold;">Enter the following details:</span>
	<span style="color: red; font-size: 10px; font-weight: bold;">* required field</span>
	
	<div id="leftAutoDun"></div>
	<div id="clearAutoDun"></div>
	
	<div id="leftAutoDun">
		Number of days past due:
	</div>
	<div id="rightAutoDun">
		<html:text property="daysPastDue" />
		<span style="color: red; font-size: 10px; font-weight: bold;">*</span>
	</div>
	<div id="clearAutoDun"></div>
	<div id="leftAutoDun">
		Customer account number:
	</div>
	<div id="rightAutoDun">
		<html:text property="customerId" disabled="false" />
		<logic:notEqual name="autoDunningForm" property="UITestMode" value="true">
		<span style="color: red; font-size: 10px; font-weight: bold;">*</span>
		</logic:notEqual>
	</div>
	<div id="clearAutoDun"></div>
	<div id="leftAutoDun">
			Override email recipient:
		</div>
	<div id="rightAutoDun">
		<html:text property="sentEmailTo" />
	</div>
	<div id="clearAutoDun"></div>
	<span style="color: red; font-weight: bold;">If no email recipient overrides here, email will send to customer directly.</span><br/>
	
	
	<logic:equal name="autoDunningForm" property="UITestMode" value="true">
		<div style="padding-top: 30px; color: red; font-weight: bold;">
			The following parameters are for QA TEST ONLY, it will not be shown in
			production.
		</div>
		
		<div id="leftAutoDun">
			Override total number of email sent:
		</div>
		<div id="rightAutoDun">
			<html:text property="totalNumEmailSent" />
		</div>
	</logic:equal>	
	<div id="clearAutoDun"></div>
	<div style="padding: 10px;">
		<html:submit value="Send Mail" />
	</div>


<logic:notEmpty name="DAYSPASTDUE" scope="request">
	<p style="margin-top: 18px;">
		<span style="color: blue; font-weight: 600;"> Invoice(s)
			overdue for	<bean:write name="DAYSPASTDUE" /> days have been sent to customer.</span>
	</p>
</logic:notEmpty>
	<html:hidden styleId="operation" property="operation"
		value="autoDunning" />
</html:form>
