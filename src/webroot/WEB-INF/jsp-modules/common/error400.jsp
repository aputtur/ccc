<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>

<%@ page contentType="text/html" %>
<%@ page errorPage="/jspError.do" %>
<%@ page import="com.copyright.ccc.web.WebConstants" %>
<div id="mainContainer">
    <div id="sectionTableContainer">
        <table class="sectionTable" style="width: 600px;">
            <tr>
                <td class="tableTextLeft">
                    <b><%= session.getAttribute(WebConstants.SessionKeys.CCC_APPLICATION_EXCEPTION_TRACE ) %></b>
                </td>
            </tr>
            <tr>
                <td class="tableTextLeft">
                    (400)
                </td>
            </tr>
        </table>
    </div>
</div>
