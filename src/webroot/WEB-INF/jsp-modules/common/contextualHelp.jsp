<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ taglib prefix="bean" uri="/WEB-INF/tld/struts-bean.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<%@ page errorPage="/jspError.do" %>

<html>

<head>
    <title></title>
    <link href="<html:rewrite page="/resources/commerce/css/ccc-new.css"/>" rel="stylesheet" type="text/css" />
    <link href="<html:rewrite page="/resources/commerce/css/default.css"/>" rel="stylesheet" type="text/css" />
    <link href="<html:rewrite page="/resources/commerce/css/contextualHelp.css"/>" rel="stylesheet" type="text/css" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>


<body class='cc2'>

    <div id="popup_pane">
        <div id="popup_wrapper">
            <div id="popup_inner">
                <div id="popup">
                    <h1 id="help-title"></h1>
                    <div id="popup-body"></div>
                </div>
                <div class="clearer"></div>
                <div id="popup-close"><a href="javascript:window.close()">Close</a></div>
            </div>
        </div>
        <div id="popup_bottomcorners"></div>
        <div id="footer_popup">
            <html:img src="/media/images/footer_logo.gif" alt="Copyright Clearance Center" width="194" height="24"/>
        </div>
    </div>
</body>
</html>

<script type="text/javascript">
    document.getElementById("help-title").innerHTML = opener.<bean:write name="contextualHelpForm" property="helpTitleId" />;
    document.getElementById("popup-body").innerHTML = opener.<bean:write name="contextualHelpForm" property="helpBodyId" />;
</script>
