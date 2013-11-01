  <%
  response.setHeader("Cache-Control","no-cache");
  response.setHeader("Pragma","no-cache");
  %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
	<style>
		label.error { width: 10em; float: left; color: red; padding-left: .5em; vertical-align: top; width:100%}
		input.error {background-color:#FEF1EC;border:1px dotted red;}
	select.error {background-color:#FEF1EC;border:1px dotted red;}
	</style>
<s:head/>

	<tiles:insertAttribute name="pageContent"></tiles:insertAttribute>
