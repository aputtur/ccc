<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="security" uri="/WEB-INF/tld/cc2-security.tld" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/cc2-util.tld" %>

<div id="mainTabs">

<s:form theme="simple" action="orderManagementHome" namespace="/om/home" >
 
  <s:hidden name="quickTabSelected"></s:hidden>

</s:form>
	<ul>
        <li class="empty">&nbsp;</li>
	</ul>	


	<SCRIPT type="text/javascript">
	 function submitForm( action, tab ) {
	    $('#orderManagementHome').attr('action', action);
	    $('#orderManagementHome_quickTabSelected').val(tab);
	    $('#orderManagementHome').submit();
	 }
	 
	</SCRIPT>						 

</div>
