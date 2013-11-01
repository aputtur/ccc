<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
        
<%@ page language="java" pageEncoding="utf8" contentType="text/html; charset=UTF8"%>
<%@ page errorPage="/jspError.do" %>

<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<%@ taglib prefix="static" uri="/WEB-INF/tld/cc2-static.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/tld/struts-bean.tld" %>
        
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Request Coverage - Acknowledgement</title>
    <link href="<html:rewrite page="/resources/commerce/css/ccc-new.css"/>" rel="stylesheet" type="text/css" />
    <link href="<html:rewrite page="/resources/commerce/css/default.css"/>" rel="stylesheet" type="text/css" />
    <link href="<html:rewrite page="/resources/commerce/css/contextualHelp.css"/>" rel="stylesheet" type="text/css" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body class="cc2">
  <div id="popup_pane">
    <div id="popup_wrapper">
      <div id="popup_inner">
        <div id="popup">
          <h1 id="help-title">Request This Work - Acknowledgement</h1>
          <div id="popup-body">
          	Thank you for your request to add coverage for a title to the Annual Copyright License catalog.  
            The Publisher Relations staff at Copyright Clearance Center will contact the publisher for the title you requested and negotiate to cover the title under your License.  
            If we are successful, your Licensing Account Representative will contact you to let you know that the title has been made available for uses covered under your License.
          </div>
        </div>
        <div class="clearer"></div>
        <div id="popup-close"><a href="Javascript:window.close();">Close</a></div>
      </div>
    </div>
    <div id="popup_bottomcorners"></div>
    <div id="footer_popup">
      <html:img src="/media/images/footer_logo.gif" alt="Copyright Clearance Center" width="194" height="24"/>
    </div>
  </div>
</body>
</html>
