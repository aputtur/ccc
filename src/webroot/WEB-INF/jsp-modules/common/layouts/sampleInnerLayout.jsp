<%@ page language="java" pageEncoding="utf8" contentType="text/html; charset=UTF8"%>
<%@ page errorPage="/jspError.do" %>

<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>

<div id="developerHomeMenu" style="width:200px;height:400px;float:left;border-right:2px solid black;">
    <tiles:insert attribute="sampleMenu"/>
</div>

<div style="height: 400px;margin-left:201px">
    <tiles:insert attribute="sampleContent"/>
</div>