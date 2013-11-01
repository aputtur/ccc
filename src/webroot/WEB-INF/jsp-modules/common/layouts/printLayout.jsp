<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page language="java" pageEncoding="utf8" contentType="text/html; charset=UTF8"%>
<%@ page errorPage="/jspError.do" %>

<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>


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
    
            <title>Copyright Clearance Center</title>
            
            <script src="<html:rewrite page="/resources/commerce/js/scripts.js"/>" type="text/javascript"></script>
            <script src="<html:rewrite page="/resources/commerce/js/webtrends.js"/>" type="text/javascript"></script>
            <script src="<html:rewrite page="/resources/commerce/js/util.js"/>" type="text/javascript"></script>
            
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
            <link href="<html:rewrite page="/resources/commerce/css/print-friendly.css"/>" rel="stylesheet" type="text/css" />

            <link rel="SHORTCUT ICON" href="/media/images/favicon.ico" />


    </head>

    <body class="cc2" >

        <div id="wrapper" style="border-bottom: solid 1px #B2B2B2;">
        
            <a  href="<html:rewrite page="/home.do"/>">
                <html:img src="/media/images/CCC_logo3_RGB_72dpi.jpg" alt="Copyright Clearance Center" style="border: 0; float: left; margin: 4px 0 6px 8px;" styleId="img_ccc_logo" />
            </a>
            
            <div class="clearer"></div>
        
            <div id="ecom-content-wrapper">
 
                <tiles:insert attribute="pageContent"/>
            
            </div>

            <div class="clearer"></div>
            
        </div>

        <tiles:insert attribute="footer"/>

    </body>

</html>
