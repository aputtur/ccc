  <%
  response.setHeader("Cache-Control","no-cache");
  response.setHeader("Pragma","no-cache");
  %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page errorPage="/jspError.do" %>

<%
/*
 * This is the layout for all  pages contained in order management
 * browser windows.  It composes the inserted content:
 * (attribute "pageContent") with a 
 * header across the top (attribute "header"),
 * and a footer across the bottom (attribute "footer").  
 */
%>

<html>
    <head>
        
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    
            <title><tiles:getAsString name="pageTitle" ignore="true" /></title>
            
        	<script src="<s:url value="/resources/ordermgmt/scripts/jquery-1.3.2.min.js" includeParams="none"/>" type="text/javascript"></script> 
        	<script src="<s:url value="/resources/ordermgmt/scripts/jquery-ui-1.7.2.custom.min.js" includeParams="none"/>" type="text/javascript"></script> 
            <link href="<s:url value="/resources/ordermgmt/style/smoothness/jquery-ui-1.7.2.custom.css" includeParams="none"/>" rel="stylesheet" type="text/css" />
            <link href="<s:url value="/resources/ordermgmt/style/ordermgmt.css" includeParams="none"/>" rel="stylesheet" type="text/css" />
            
            <link rel="SHORTCUT ICON" href="<s:url value="/media/images/favicon.ico" includeParams="none"/>" />

    </head>

    <body>
        <center>
        
		<img src="<s:url value="/resources/ordermgmt/images/spacer.gif"/>" height="5px" width="0" />
        
        <div id="wrapper">
        
          <div id="mainContent">
        
            <table width="100%" style="border: 1px solid #E2E9F0;">
            <tr>
            	<td style="width: 261px;">
					<a href="<s:url includeParams="none" value="/home.do"/>">
			    		<img src="<s:url includeParams="none" value="/media/images/CCC_logo3_RGB_72dpi.jpg"/>" alt="Copyright Clearance Center" class="headerTitleTopCCCLogo" id="img_ccc_logo" />    
					</a>
				</td>
				<td align="left" style="background-color: #E2E9F0;">
					<h1 class="headerTitleTop"><s:text name="ordermgmt.landing.title"/></h1>
				</td>
				<td align="right" style="padding-right: 10px; background-color: #E2E9F0;">
					<jsp:include page="/WEB-INF/jsp-modules-s2/ordermgmt/omDynamicHeaderLinks.jsp" />
				</td>
			</tr>
			</table> 
			
			  <div >
			  					 
					    <tiles:insertAttribute name="topMenu"/>
			  </div>
			
			
			<% //IE6 bug BEGIN %>
			<iframe id="helperIFrame" src="<s:url includeParams="none" value="/pages/62167c1239i93851aee9451eff4728944fbf8356.html"/>" frameborder="0" scrolling="no" style="position: absolute; width: 0px; height; 0px; top: 0px; left: 0px;"></iframe>
			<% //IE6 bug END %>
                         
            <div style="float: left;"><img src="<s:url value="/resources/ordermgmt/images/spacer.gif"/>" height="400px" width="0" /></div>
            <div style="float: left;">           

				<s:if test="fieldErrors != null && !fieldErrors.isEmpty()">
					<div class="errors">
						<h4>Please fix the following errors:</h4>
						<s:fielderror />
					</div>
				</s:if>
				
				<s:if test="actionErrors != null && !actionErrors.isEmpty()">
					<div class="errors">
						<h4>Please fix the following errors:</h4>
						<s:actionerror />
					</div>
				</s:if>
				
				<s:if test="actionMessages != null && !actionMessages.isEmpty()">
					<div class="errors">
						<h4>Informational messages:</h4>
						<s:actionmessage />
					</div>
				</s:if>
			
            	<tiles:insertAttribute name="pageContent"/>
            	
            </div>
        </div>

        <div class="clearer"></div>
        
        </div>
        <div id="mainFooter">
        <tiles:insertAttribute name="footer"/>
        </div>
        </center>
 </body>

</html>
