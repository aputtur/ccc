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
		<meta http-equiv="Page-Enter" content="blendTrans(Duration=0)">
		<meta http-equiv="Page-Exit" content="blendTrans(Duration=0)">
        
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    
            <title><tiles:getAsString name="pageTitle" ignore="true" /></title>
            
        	<script src="<s:url value="/resources/ordermgmt/scripts/jquery-1.3.2.min.js" includeParams="none"/>" type="text/javascript"></script> 
        	<script src="<s:url value="/resources/ordermgmt/scripts/jquery-ui-1.7.2.custom.min.js" includeParams="none"/>" type="text/javascript"></script> 
            <link href="<s:url value="/resources/ordermgmt/style/smoothness/jquery-ui-1.7.2.custom.css" includeParams="none"/>" rel="stylesheet" type="text/css" />
            <link href="<s:url value="/resources/ordermgmt/style/ordermgmt.css" includeParams="none"/>" rel="stylesheet" type="text/css" />
            
            <link rel="SHORTCUT ICON" href="<s:url value="/media/images/favicon.ico" includeParams="none"/>" />
        	<script src="<s:url value="/resources/ordermgmt/scripts/customPageControl.js" includeParams="none"/>" type="text/javascript"></script> 

    </head>

    <body>
        <center>
        
		<img src="<s:url value="/resources/ordermgmt/images/spacer.gif"/>" height="1" width="100%" />
        
        <div id="wrapper" >
        
          <div id="mainContent">
        
            <table style="border: 1px solid #E2E9F0;">
            <tr>
            	<td style="width: 261px;">
					<a href="<s:url includeParams="none" value="/home.do"/>">
			    		<img src="<s:url includeParams="none" value="/media/images/CCC_logo3_RGB_72dpi.jpg"/>" alt="Copyright Clearance Center" class="headerTitleTopCCCLogo" id="img_ccc_logo" />    
					</a>
				</td>
				<td align="left" style="width: 60%; background-color: #E2E9F0;">
					<h1 class="headerTitleTop"><s:text name="ordermgmt.top.title"/></h1>
				</td>
				<td align="right" style="width: 30%; padding-right: 10px;background-color: #E2E9F0;">
				<jsp:include page="/WEB-INF/jsp-modules-s2/ordermgmt/omDynamicHeaderLinks.jsp" />
				</td>
			</tr>
			</table> 
			
			<tiles:insertAttribute name="topMenu"/>
			                         
            <div>           

				<s:if test="(fieldErrors != null && !fieldErrors.isEmpty()) || (actionErrors != null && !actionErrors.isEmpty())">
					<div class="errors">
						<h4>Unable to complete request...</h4>
						<s:if test="fieldErrors != null && !fieldErrors.isEmpty()">
							<s:fielderror theme="simple" />
						</s:if>
						<s:if test="actionErrors != null && !actionErrors.isEmpty()">
							<s:actionerror  theme="simple" />
						</s:if>
					</div>
				</s:if>				
				<s:if test="actionMessages != null && !actionMessages.isEmpty()">
					<div class="errors">
						<h4>Please note...</h4>
						<s:actionmessage  theme="simple" />
					</div>
				</s:if>
			
            	<tiles:insertAttribute name="pageContent"/>
            	
            </div>
        </div>

        </div>
        <div id="mainFooter">
        <tiles:insertAttribute name="footer"/>
        </div>
        </center>
        
<s:component template='messagedialog.ftl'>
	<s:param name="dialogTitle" value="' '" />
	<s:param name="closeButton" value="'Close'" />
</s:component>	

<div id="waiting_for_search" style="display: none;">
 	<img src="<s:url includeParams='none' value='/resources/ordermgmt/images/progressbar.gif'/>" alt=""/>&nbsp;Processing, please wait...    
</div>
<div id="waiting_for_sort" style="display: none;">
	<img src="<s:url includeParams='none' value='/resources/ordermgmt/images/progressbar.gif'/>" alt=""/>&nbsp;Re-Sorting, please wait...    
</div>
<div id="waiting_for_page" style="display: none;">
	<img src="<s:url includeParams='none' value='/resources/ordermgmt/images/progressbar.gif'/>" alt=""/>&nbsp;Re-Paging, please wait...    
</div>
        
 </body>

</html>
