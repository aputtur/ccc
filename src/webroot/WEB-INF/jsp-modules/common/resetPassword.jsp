<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>

<%@ page contentType="text/html" %>
<%@ page errorPage="/jspError.do" %>

<style>
  .footNote{
      color: gray;
      font-size: 7pt;
  }
</style>

<bean:define id="passwordInstructions" >
  <bean:message key="auth.pwd.instructions"  />
</bean:define>

<div id="mainContainer">
    
    <div id="pageHeader"><h2>Reset Password</h2></div>
    
    <div id="sectionTableContainer">

        <html:form action="resetPassword.do?operation=handleReset">
        
            <table class="sectionTable" style="width: 670px;" >
            
              <tr>
                
                <td colspan="2">
                  <div id="sectionHeader">Your password must be at least 6 characters and the first 10 characters<br />
                    cannot be the same as your username.  We recommend using upper and<br />
                    lowercase characters and at least one digit.
                  </div>
                </td>
              
              </tr>
              
              <tr>
                
                <td class="tableTextLeft" nowrap>
                  New Password:&nbsp;
                </td>
                
                <td class="tableTextRight" width="80%">
                  <html:password property="password" size="30" />
                  <br>
                   <span class="footNote">
                        &nbsp;
                    </span>
                </td>
              
              </tr>
              
              <tr>
                
                <td class="tableTextLeft" nowrap>
                  Re-enter Password:&nbsp;
                </td>
                
                <td class="tableTextRight">
                  <html:password property="passwordConfirmation" size="30" />
                </td>
              </tr>
              
              <tr>
                
                <td class="tableTextLeft">
                  &nbsp;
                </td>
                
                <td class="tableTextRight">
                  <html:submit value="Reset Password" />
                </td>
              
              </tr>
            
            </table>
                       
        </html:form>
        
    </div>
    
</div>

<script type="text/javascript">
  
  var initialElement = document.getElementById("newPassword");
  
  if( initialElement ){
    initialElement.focus();
  }

</script>
