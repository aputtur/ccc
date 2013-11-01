<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page language="java" pageEncoding="utf8" contentType="text/html; charset=UTF8"%>
<%@ page errorPage="/jspError.do" %>

<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/tld/struts-logic.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/tld/struts-bean.tld" %>
<%@ taglib prefix="static" uri="/WEB-INF/tld/cc2-static.tld" %>
<%
/*
 * This is the layout for all  pages contained in main browser
 * windows.  It composes the inserted content (attribute "pageContent") with a 
 * header across the top (attribute "header"), and a footer across the bottom
 * (attribute "footer).  The CSS styles that control this layout are 
 * contained here inline to consolidate the HTML page layout specification in
 * one place.
 */
%>

<% String pageName = ( String ) request.getAttribute("Page"); %>

<html>

    <head>
       
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

            <static:importHeader file="<%= pageName %>" />

            <link href="<html:rewrite page="/resources/commerce/css/default.css"/>" rel="stylesheet" type="text/css" />
            <link href="<html:rewrite page="/resources/commerce/css/ccc-new-v2.css"/>" rel="stylesheet" type="text/css" />
            <link href="<html:rewrite page="/resources/commerce/css/static-print.css"/>" rel="stylesheet" type="text/css" />
            <link rel="SHORTCUT ICON" href="/media/images/favicon.ico" />
            <script type="text/javascript" language="javascript" src="<html:rewrite page="/resources/commerce/js/new_site.js"/>"></script>
	        <script src="<html:rewrite page="/resources/commerce/js/jquery.min.js"/>" type="text/javascript"></script>
	        
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

    </head>

    <body class='<tiles:getAsString name="bodyStyle" ignore="true" />'>

        <div id="wrapper">

            <bean:define scope="request" id="bannerImage" name="Banner" />
            <bean:define scope="request" id="bannerHeight" name="BannerHeight" />
            <bean:define scope="request" id="bannerName" name="BannerName" />
            <tiles:insert attribute="header">
                <tiles:put name="bannerImage" value="bannerImage" />
                <tiles:put name="bannerHeight" value="bannerHeight" />
                <tiles:put name="bannerName" value="bannerName" />
            </tiles:insert>
            <bean:define scope="request" id="layoutName" name="Layout" />
            <% String fullLayoutName = "/WEB-INF/jsp-modules/common/layouts/" + layoutName + ".jsp"; %>
            <jsp:include page="<%= fullLayoutName %>" />
        </div>

        <logic:notEqual value="blank" name="layoutName">
            <% String bottomCorners = layoutName+"-bottomcorners"; %>
            <div id="<%=bottomCorners%>"></div>
        </logic:notEqual>

        <tiles:insert attribute="footer"/>

        <!-- POPUP -->
        <script language="javascript" type="text/javascript">
            //  Uncomment for popup on all pages used by this layout.
            //  popup_new_site_message();
        </script>
</body>
</html>
