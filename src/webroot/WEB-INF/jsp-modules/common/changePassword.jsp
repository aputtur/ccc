<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>

<%@ page contentType="text/html" %>
<%@ page errorPage="/jspError.do" %>

<div id="mainContainer">
    
    <div id="pageHeader">Change Password</div>
    
    <div id="sectionTableContainer">
    
        
        <form method="post"  name="ChangePassword" action="" /> 
  
                        
            <table class="sectionTable" style="width: 670px;" >
                <tr>
                  <td class="tableTextLeft" colspan="2">
                    You may use this page to change your password.
                  </td>
                </tr>
                
                <tr>
                  <td class="tableTextLeft" width="20%">
                    Old Password:&nbsp;
                  </td>
                  
                  <td class="tableTextRight">
                    <input type="password" name="p_old_password" size="30" />
                  </td>
                </tr>
                
                <tr>
                  <td class="tableTextLeft">
                    New Password:&nbsp;
                  </td>
                  
                  <td class="tableTextRight">
                    <input type="password" name="p_new_password" size="30" />
                  </td>
                </tr>
                
                <tr>
                  <td class="tableTextLeft">
                    Confirm New Password:&nbsp;
                  </td>
                  
                  <td class="tableTextRight">
                    <input type="password" name="p_new_password_confirm" size="30" />
                  </td>
                </tr>
                
                <tr>
                  <td class="tableTextLeft">
                    <a href=""  ><img name="Return" alt="Return" border="0" src='<html:rewrite page="/resources/images/ra_returnToList.gif" />' style="vertical-align: middle;" /></a>
                    <a href="">Return</a>
                  </td>
                  
                  <td class="tableTextRight">
                    <%
                    /** 
                      WARNING: We have so far empirically found that the change password function 
                      works only when the submit button has value="OK". If the submit button has 
                      value="Submit" or "Save", the password change function does not work 
                      properly.  This seems to be a limitation of the Oracle SSO infrastructure.
                      
                      If we wish to change this value to anything other than "OK", be sure to do 
                      a test in a deployed environment.  We may not get around this limitation 
                      until we upgrade to a newer version of OSSO.
                      
                      [dstine 8/9/06]
                     */
                    %>
                    <input type="Submit" name="p_action" value="OK" />
                  </td>
                </tr>
                
            </table>
        </form>
        
    </div>
    
</div>
