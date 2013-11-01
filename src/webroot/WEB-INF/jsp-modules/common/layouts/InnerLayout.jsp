<%@ page language="java" pageEncoding="utf8" contentType="text/html; charset=UTF8"%>
<%@ page errorPage="/jspError.do" %>

<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>


<div style="height: 400px;margin-left:201px">
    <tiles:insert attribute="innerContent"/>
</div>