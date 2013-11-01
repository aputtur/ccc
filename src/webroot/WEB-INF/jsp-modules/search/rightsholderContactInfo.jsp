<%@ taglib prefix="bean" uri="/WEB-INF/tld/struts-bean.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/tld/struts-logic.tld" %>
<%@ page errorPage="/jspError.do" %>

<html>

<head>
    <title>Contact Rightsholder</title>
    <link href="<html:rewrite page="/resources/commerce/css/ccc-new.css"/>" rel="stylesheet" type="text/css" />
    <link href="<html:rewrite page="/resources/commerce/css/default.css"/>" rel="stylesheet" type="text/css" />
    <link href="<html:rewrite page="/resources/commerce/css/contextualHelp.css"/>" rel="stylesheet" type="text/css" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    
    <script src="<html:rewrite page="/resources/commerce/js/webtrends.js"/>" type="text/javascript"></script>
            
    <script type="text/javascript">
		//<![CDATA[
			var _tag=new WebTrends();
			_tag.dcsGetId();
			var WT=_tag.WT;
		//]]>>
	</script>

</head>

<body class='cc2'>

    <div id="popup_pane">
        <div id="popup_wrapper">
            <div id="popup_inner">
                <div id="popup">
                    <h1 id="help-title"></h1>
                    <div id="popup-body">Although Copyright Clearance Center may handle some requests on behalf of this rightsholder,
            we are unable to grant permission for the specific title and/or content usage you've requested. To pursue
            your inquiry further, please contact the rightsholder directly at the following address:

        <table style="border-collapse: collapse">
        <tr><td>&nbsp;</td></tr>
        <tr>
          <td><bean:write name="rightsholderContactInfoForm" property="rhName"/></td>
        </tr>
        <tr>
          <td> <bean:write name="rightsholderContactInfoForm" property="rhAddress1"/></td>
        </tr>
        <logic:notEmpty name="rightsholderContactInfoForm" property="rhAddress2">
            <tr>
              <td> <bean:write name="rightsholderContactInfoForm" property="rhAddress2"/></td>
            </tr>
        </logic:notEmpty>
        <logic:notEmpty name="rightsholderContactInfoForm" property="rhAddress3">
            <tr>
              <td> <bean:write name="rightsholderContactInfoForm" property="rhAddress3"/></td>
            </tr>
        </logic:notEmpty>
        <logic:notEmpty name="rightsholderContactInfoForm" property="rhAddress4">
            <tr>
              <td> <bean:write name="rightsholderContactInfoForm" property="rhAddress4"/></td>
            </tr>        
        </logic:notEmpty>
        <tr>
          <td><bean:write name="rightsholderContactInfoForm" property="rhCity"/>
              <logic:notEmpty name="rightsholderContactInfoForm" property="rhCity">
              <logic:notEmpty name="rightsholderContactInfoForm" property="rhState">
                ,
              </logic:notEmpty>
              </logic:notEmpty>
              <bean:write name="rightsholderContactInfoForm" property="rhState"/>&nbsp;
              <logic:notEmpty name="rightsholderContactInfoForm" property="rhCountryAbbrev">
                  <bean:write name="rightsholderContactInfoForm" property="rhCountryAbbrev"/>
              </logic:notEmpty>
              <bean:write name="rightsholderContactInfoForm" property="rhPostalCode"/>
          </td>
        </tr>
        
        <logic:notEmpty name="rightsholderContactInfoForm" property="rhPhone">
            <tr>
              <td>Phone: <bean:write name="rightsholderContactInfoForm" property="rhPhone"/></td>
            </tr>        
        </logic:notEmpty>
        
        <logic:notEmpty name="rightsholderContactInfoForm" property="rhFax">
            <tr>
              <td>Fax: <bean:write name="rightsholderContactInfoForm" property="rhFax"/></td>
            </tr>        
        </logic:notEmpty>
        
        <logic:notEmpty name="rightsholderContactInfoForm" property="rhEmail">
            <tr>
              <td>E-mail: <bean:write name="rightsholderContactInfoForm" property="rhEmail"/></td>
            </tr>        
        </logic:notEmpty>
        
        </table>
        <logic:equal value="true" name="rightsholderContactInfoForm" property="inCompleteAddressInfo">
            Note: Incomplete Address Information on File
        </logic:equal>

                    </div>
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
    document.getElementById("help-title").innerHTML = "Contact Rightsholder";
</script>

<jsp:include page="/WEB-INF/jsp-modules/common/dcs_tag_js.jsp" />

