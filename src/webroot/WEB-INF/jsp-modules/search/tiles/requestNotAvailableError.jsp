<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
        
<%@ page language="java" pageEncoding="utf8" contentType="text/html; charset=UTF8"%>
<%@ page errorPage="/jspError.do" %>

<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<%@ taglib prefix="static" uri="/WEB-INF/tld/cc2-static.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/tld/struts-bean.tld" %>
        
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<title>Request This Work - Error</title>
	<link href="<html:rewrite page="/resources/commerce/css/default.css"/>" rel="stylesheet" type="text/css" />
    <link href="<html:rewrite page="/resources/commerce/css/ccc-new-v2.css"/>" rel="stylesheet" type="text/css" />
    <link href="<html:rewrite page="/resources/commerce/css/contextualHelp.css"/>" rel="stylesheet" type="text/css" />
</head>
<body class="cc2">
  <div id="popup_pane">
    <div id="popup_wrapper">
      <div id="popup_inner">
        <div id="popup">
          <h1>Request This Work - Error</h1>
          <div>
          	An error has occurred while trying to submit your request.  Please contact your Licensing Account Representative directly.
          </div>
        </div>
        <div class="clearer"></div>
        <div id="popup-close"><a href="#">Close</a></div>
      </div>
    </div>
    <div id="popup_bottomcorners"></div>
    <div id="footer_popup">
      <html:img srcKey="resource.image.footerlogo" alt="Copyright Clearance Center" width="194" height="24" />
    </div>
  </div>
</body>
</html>
