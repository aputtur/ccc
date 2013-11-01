<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>  
<%@ taglib prefix="security" uri="/WEB-INF/tld/cc2-security.tld" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/cc2-util.tld" %>

<%@ page errorPage="/jspError.do" %>


    <div style="width:100%;background-color:#FFFF99;border:1px sold #999900;height:24px">
        <security:ifUserEmulating>
            <html:form action="/admin/emulation" method="post">
                <table width="100%" style="border:0;margin:0;padding:0">
                    <tr>
                        <td>&nbsp;</td>
                        <td width="100%" align="center">
                            <span style="color:red;font-weight:bold">An emulation session is active for:</span>
                            <span style="color:#777777"><util:userInfo userType="cc" propertyName="username"/></span>
                        </td>
                        <td>
                            <html:hidden property="operation" value="stop"/>
                            <html:submit value="Stop Emulation"/>
                        </td>
                    </tr>
                </table>
            </html:form>
        </security:ifUserEmulating>
    </div>

