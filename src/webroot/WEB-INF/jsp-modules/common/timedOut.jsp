<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>

<%@ page contentType="text/html" %>
<%@ page errorPage="/jspError.do" %>

<div id="mainContainer">
    
    <div id="pageHeader">Session Timed Out</div>
    
    <div id="sectionTableContainer" style="padding-top: 20px;" >
    
        <table class="sectionTable" style="width: 670px;" >
            <tr>
                <td class="tableTextLeft">Your session has timed out.</td>
            </tr>
            <tr><td>
                <ul>

                <li><a href="home.do"> Go to the copyright.com Home page</a>.</li>  
       	
                </ul>
            </td></tr>
            <tr><td>
                <ul>

                <li><a href="loginBounce.do"> Login</a>.</li>  
       	
                </ul>
            </td></tr>
        </table>
        
    </div>
    
</div>
