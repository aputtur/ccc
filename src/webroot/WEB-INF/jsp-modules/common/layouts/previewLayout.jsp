<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page language="java" pageEncoding="utf8" contentType="text/html; charset=UTF8"%>
<%@ page errorPage="/jspError.do" %>

<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>


<%
/*
 * This is the layout for all  pages contained in main browser
 * windows.  It composes the inserted content (attribute "pageContent") with a 
 * header across the top (attribute "header").  The CSS styles that control this layout are 
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

           
            <link rel="SHORTCUT ICON" href="/media/images/favicon.ico" />


    </head>

    <body>

    <div id="preview" >
        
                     
    <table align="left" width="450" border="0" cellpadding="0" cellspacing="0">
    <tr>
      <td align="center"><html:img src="/media/images/home-headerlogo.gif"/></td>
      <td align="center"><html:img src="/media/images/popup-red-spacer.gif" width="28" height="34"/></td>
      <td align="center"><html:img src="/media/images/popup-close.gif" width="211" onclick="window.close();"/></td>
    </tr>
    </table>

                                
            <div id="ecom-content-wrapper">
 
                <tiles:insert attribute="pageContent"/>
            
            </div>
            
            <div class="clearer"></div>
                                     
                                                                     
        </div>
        
        <div id="bottomcorners_preview"></div> 
        
                
               
    </body>

</html>
