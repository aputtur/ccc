<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page language="java" pageEncoding="utf8" contentType="text/html; charset=UTF8"%>
<%@ page errorPage="/jspError.do" %>

<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<%@ taglib prefix="static" uri="/WEB-INF/tld/cc2-static.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/tld/struts-bean.tld" %>


<% String pageName = ( String ) request.getAttribute("Page"); %>

<html>

    <head>
        
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

            <static:importHeader file="<%= pageName %>" />

            <link href="<html:rewrite page="/resources/commerce/css/default.css"/>" rel="stylesheet" type="text/css" />
            <link href="<html:rewrite page="/resources/commerce/css/ccc-new-v2.css"/>" rel="stylesheet" type="text/css" />
            <link rel="SHORTCUT ICON" href="/favicon.ico" />

    </head>

    <body class="cc2">
  <div id="popup_pane">
    <div id="popup_wrapper">
      <div id="popup_inner">
        <div id="popup">
          <bean:define scope="request" id="popName" name="Page" type="java.lang.String"/> 
          <static:importContent file="<%= popName %>" />
        </div>
        <div class="clearer"></div>
        <div id="popup-close"><a href="javascript:window.close();">Close</a></div>
      </div>
    </div>
    <div id="popup_bottomcorners"></div>
    <div id="footer_popup">
      <img src="/media/images/footer_logo.gif" alt="Copyright Clearance Center" width="194" height="24" />
    </div>
  </div>
</body>
</html>

