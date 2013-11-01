<%@ page contentType="text/html;charset=windows-1252"
import="java.util.Calendar"
%>
<%@ page errorPage="/jspError.do" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>  
<%@ taglib uri="/WEB-INF/tld/struts-tiles.tld" prefix="tiles" %>
<%--@ taglib uri="/WEB-INF/taglibs-string.tld" prefix="str" --%>
<link href="/ccc/default.css" rel="stylesheet" type="text/css" />
<link href="/ccc/ccc-new.css" rel="stylesheet" type="text/css" />
<script src="/ccc/dropdown.js" type="text/javascript"></script>
<% String currentYear = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
%>
<!-- Begin Footer -->
<div id="footer-wrap">
 <div id="footer-content">
 		<img src="<html:rewrite href="/media/images/footer_logo.gif"/>" alt="Copyright Clearance Center" width="194" height="24" />
		<p><a href="<html:rewrite page='/content/cc3/en/toolbar/aboutUs.html' />">About Us</a> | <a href="<html:rewrite page='/content/cc3/en/toolbar/aboutUs/contact_us.html' />">Contact Us</a> | <a href="<html:rewrite page='/viewPage.do?pageCode=au5-n' />">Careers</a> | <a href="<html:rewrite page='/content/cc3/en/tools/footer/privacypolicy.html' />">Privacy Policy</a> | <a href="<html:rewrite page='/content/cc3/en/tools/footer/termsconditions.html' />">Terms &amp; Conditions</a><br />
                <a href="<html:rewrite page='/content/cc3/en/tools/footer/site_index.html' />">Site Index</a> | <a href="http://copyrightlabs.com" target="_blank">Copyright Labs</a></p>
                <div class="clearer"></div>
                <div id="footer-content-copyright">
		<a href="<html:rewrite page='/content/cc3/en/tools/copyright.html' />" style="">Copyright</a> <%=currentYear%> Copyright Clearance Center</div>
		<div class="clearer"></div>
 </div>
</div>
<!-- End Footer -->

	
