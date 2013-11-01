<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>

<%@ page contentType="text/html" %>
<%@ page errorPage="/jspError.do" %>

<div id="mainContainer" style="width: 670px;">
    
    <div id="pageHeader">Request Forgotten Password</div>
    
    <logic:messagesPresent>
        <div id="sectionTableContainer" >
            <div class="error"><html:errors/></div>
        </div>
    </logic:messagesPresent>
    
    <div id="sectionTableContainer">
        
      <html:form action="forgotPassword.do?operation=handleRequest">
      
        <table class="sectionTable" style="width: 670px;">
        
          <tr>
          
            <td class="tableTextLeft" colspan="2">
            
              If you have forgotten your password, you may request that an e-mail containing 
            a reset-password link be sent to you.
            <br><br>
            Please enter the e-mail address that you have registered with Rightsphere.
            
            </td>
          
          </tr>
        
          <tr>
            
            <td class="tableTextLeft" style="padding-top: 20px;" nowrap >
              E-mail Address:&nbsp;
            </td>
            
            <td class="tableTextRight" style="padding-top: 20px;" width="85%" >
              <html:text property="emailAddress" size="30" />
            </td>
            
          </tr>
          
          <tr>
            
            <td class="tableTextLeft" >
              &nbsp;
            </td>
            
            <td class="tableTextRight" >
              <html:submit value="Submit" />
            </td>
            
          </tr>
          
        </table>
        
      </html:form>
        
    </div>
    
</div>

<script type="text/javascript">
  
  var initialElement = document.getElementById("emailAddress");
  
  if( initialElement ){
    initialElement.focus();
  }

</script>
