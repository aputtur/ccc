<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<div>

	<div id="clearer"></div>
	
	<s:if test="(fieldErrors != null && !fieldErrors.isEmpty() ) 
	          || (actionErrors != null && !actionErrors.isEmpty())
	          || (actionMessages != null && !actionMessages.isEmpty())">
		<s:form action="adjustmentHome!cancel" namespace="/om/adjust">
			<s:submit value="Clear Messages"></s:submit>
		</s:form>
	</s:if>
	<s:else>
		<s:form action="adjustmentHome!showActionMessageSamples" namespace="/om/adjust">
			<s:submit value="Show Action Message Samples"></s:submit>
		</s:form>
	</s:else>

</div>
