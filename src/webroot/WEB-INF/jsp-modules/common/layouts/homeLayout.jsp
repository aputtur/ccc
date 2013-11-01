<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page language="java" pageEncoding="utf8" contentType="text/html; charset=UTF8"%>
<%@ page errorPage="/jspError.do" %>
<%@ page import="com.copyright.ccc.config.CC2Configuration" %>

<%@ taglib prefix="static" uri="/WEB-INF/tld/cc2-static.tld" %>
<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/tld/struts-logic.tld" %>


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


<html>
    <head>
        
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    
            <% String homePageDir = CC2Configuration.getInstance().getStaticContentRoot() + "/home-page/"; %>
            <static:import file="homepage-meta.html" root="<%=homePageDir%>" />
            
            <script src="<html:rewrite page="/resources/commerce/js/scripts.js"/>" type="text/javascript"></script>
            <script src="<html:rewrite page="/resources/commerce/js/webtrends.js"/>" type="text/javascript"></script>
            <script src="<html:rewrite page="/resources/commerce/js/util.js"/>" type="text/javascript"></script>
            <script type="text/javascript" language="javascript" src="<html:rewrite page="/resources/commerce/js/new_site.js"/>"></script>
            
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
                //Adapted from http://simonwillison.net/2004/May/26/addLoadEvent/
                function addOnLoadEvent( functionToBeExecutedOnLoad ) {
                    
                    if (!functionToBeExecutedOnLoad || (typeof functionToBeExecutedOnLoad != 'function') ) return;
                    
                    var oldonload = window.onload;
                    if (typeof window.onload != 'function') {
                        window.onload = functionToBeExecutedOnLoad;
                    } else {
                        window.onload = function() {
                            if (oldonload) {
                                oldonload();
                            }
                            functionToBeExecutedOnLoad();
                        }
                    }
                }
                                
                if( typeof addOnLoadEvent == 'function' && typeof randoho == 'function' ){
                  addOnLoadEvent( randoho );
                }
            </script>

            <link href="<html:rewrite page="/resources/commerce/css/default.css"/>" rel="stylesheet" type="text/css" />
            <link href="<html:rewrite page="/resources/commerce/css/ccc-new.css"/>" rel="stylesheet" type="text/css" />
            

            <link rel="SHORTCUT ICON" href="/media/images/favicon.ico" />


    </head>

    <body class='<tiles:getAsString name="bodyStyle" ignore="true" />' onload='randoho();    MM_preloadImages("/media/images/nav_business_on.gif","/media/images/nav_academic_on.gif","/media/images/nav_publishers_on.gif","/media/images/nav_authors_on.gif","/media/images/nav_partners_on.gif","/media/images/nav_home_on.gif","/media/images/home_splash-photo-business.jpg","/media/images/home-splash-academic-on.gif","/media/images/home_splash-photo-academic.jpg" ,"/media/images/home-splash-copyright-on.gif" ,"/media/images/home_splash-photo-copyright.jpg","/media/images/home-splash-business-on.gif");' >



        <div id="wrapper">

            <tiles:insert attribute="header"/>
            
            <tiles:useAttribute name="includeContentWrapper" id="includeContentWrapper" />
            <tiles:useAttribute name="includeUserAlert" id="includeUserAlert" />
            
            <logic:equal name="includeContentWrapper" value="true">
                <div id="ecom-content-wrapper">
            </logic:equal>
            
                    <html:errors />
                    
                    <!-- Begin includeUserAlert -->
                    <logic:equal name="includeUserAlert" value="true">
                        <!-- USER ALERTS -->
                        <tiles:insert page="/WEB-INF/jsp-modules/common/userAlert.jsp" />
                    </logic:equal>
                    <!-- End includeUserAlert -->
               
                    <tiles:insert attribute="pageContent"/>
            
            <logic:equal name="includeContentWrapper" value="true">
                </div>
            </logic:equal>

            <div class="clearer"></div>

        </div>

        <div id="bottomcorners"></div>
 
        <jsp:include page="/WEB-INF/jsp-modules/common/dcs_tag_js.jsp" />   
        
        <tiles:insert attribute="footer"/>

        <!-- POPUP -->
        <script language="javascript" type="text/javascript">
            //  Uncomment for popup on all pages used by this layout.
            //  popup_new_site_message();
        </script>
</body>

</html>
