<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page language="java" pageEncoding="utf8" contentType="text/html; charset=UTF8"%>
<%@ page errorPage="/jspError.do" %>

<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/tld/struts-logic.tld" %>
<%@ taglib prefix="static" uri="/WEB-INF/tld/cc2-static.tld" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <static:import file="home-page/homepage-meta.html" />
        <script src="<html:rewrite page="/resources/commerce/js/jquery.min.js"/>" type="text/javascript"></script>
        <script src="<html:rewrite page="/resources/commerce/js/ie6-nav-fix.js"/>" type="text/javascript"></script>
        <script src="<html:rewrite page="/resources/commerce/js/jquery.cycle.js"/>" type="text/javascript"></script>
        <script type="text/javascript" src="<html:rewrite page="/resources/commerce/js/webtrends.js"/>" ></script>
        
<script type="text/javascript">
var _gaq = _gaq || [];
_gaq.push(['_setAccount', 'UA-3607871-16']);
_gaq.push(['_trackPageview']);
(function() { var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true; ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js'; var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s); })();
</script>

<script type="text/javascript">
    var _elqQ = _elqQ || [];
    _elqQ.push(['elqSetSiteId', '2086772265']);
    _elqQ.push(['elqTrackPageView']);
    
    (function () {
        function async_load() {
            var s = document.createElement('script'); s.type = 'text/javascript'; s.async = true;
            s.src = '//img.en25.com/i/elqCfg.min.js';
            var x = document.getElementsByTagName('script')[0]; x.parentNode.insertBefore(s, x);
        }
        if (window.addEventListener) window.addEventListener('DOMContentLoaded', async_load, false);
        else if (window.attachEvent) window.attachEvent('onload', async_load); 
    })();
</script>        
        
        <script type="text/javascript">
	//<![CDATA[
	var _tag=new WebTrends();
	_tag.dcsGetId();
	var WT=_tag.WT;
	//]]>>
	</script>
	

        <link rel="SHORTCUT ICON" href="/media/images/favicon.ico" />
        <link href="/media/css/slidertest.css" rel="stylesheet" type="text/css" />
        <link href="/media/css/main.css" rel="stylesheet" type="text/css" />
    </head>

    <body class='<tiles:getAsString name="bodyStyle" ignore="true" />' >
        <div id="wrapper">
            <tiles:insert attribute="header"/>
                 <tiles:insert attribute="pageContent"/>
            <jsp:include page="/WEB-INF/jsp-modules/common/dcs_tag_js.jsp" />
            <tiles:insert attribute="footer"/>
            <!-- POPUP -->
            <script language="javascript" type="text/javascript">
                //  Uncomment for popup on all pages used by this layout.
                //  popup_new_site_message();
            </script>
        </div>
    </body>
</html>
