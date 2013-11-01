	<%@ page language="java" pageEncoding="utf8" contentType="text/html; charset=UTF8"
	import="java.util.Calendar"
	%>
	<%@ page errorPage="/jspError.do" %>
	<%@ taglib uri="/WEB-INF/tld/struts-html.tld"  prefix="html" %>

	<%
		String currentYear = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
	%>

<!-- Footer Region -->

    <!--<div id="footer-wrap" class="region">
     	<div id="footer-content" class="sub-region">-->
		  	<p>
				<a href="<html:rewrite page='/viewPage.do?pageCode=gp1' />" rel="nofollow">Get Permission</a> | <a href="https://rightscentral.copyright.com/pp/home.do" rel="nofollow" target="_blank">License Your Content</a> | <a href="<html:rewrite page='/viewPage.do?pageCode=au1' />" rel="nofollow">Products &amp; Solutions</a> | <a href="<html:rewrite page='/viewPage.do?pageCode=ci1-n' />" rel="nofollow">Partners</a> | <a href="<html:rewrite page='/viewPage.do?pageCode=pu3-n' />" rel="nofollow">Education</a> | <a href="<html:rewrite page='/content/cc3/en/toolbar/aboutUs.html' />" rel="nofollow">About CCC</a><br />
				<a href="<html:rewrite page='/content/cc3/en/tools/footer/privacypolicy.html' />">Privacy Policy</a> | <a href="<html:rewrite page='/content/cc3/en/tools/footer/termsconditions.html' />">Terms &amp; Conditions</a> | <a href="<html:rewrite page='/content/cc3/en/tools/footer/site_index.html' />">Site Index</a> | <a href="http://copyrightlabs.com" rel="nofollow" target="_blank">Copyright Labs</a>
			</p>
			<div class="clearer"></div>
			<div id="footer-content-copyright">
				<a href="<html:rewrite page='/content/cc3/en/tools/copyright.html' />" rel="nofollow">Copyright</a> <%=currentYear%> Copyright Clearance Center
			</div>
			<div class="clearer"></div>
		<!--</div>
    </div> -->
    
    <script type="text/javascript">
        var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");

        document.write(unescape("%3Cscript src='" + gaJsHost +
            "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
    </script>

    <script type="text/javascript">
        try {
            var pageTracker = _gat._getTracker("UA-9376938-1");
            pageTracker._trackPageview();
        } 
        catch(err) {}
    </script>