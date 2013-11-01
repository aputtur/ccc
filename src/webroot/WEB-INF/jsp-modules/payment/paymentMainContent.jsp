<%@ page language="java" %>
<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ page import="com.copyright.ccc.business.security.UserContextService" %>
<%@ page import="com.copyright.ccc.business.services.user.User"%>
<%@ page import="com.copyright.svc.ldapuser.api.data.LdapUser"%>
<%@ page import="com.copyright.ccc.business.data.CCUser"%>
<%@ page import="com.copyright.svc.telesales.api.data.Location"%>
<%@ page import="com.copyright.ccc.business.services.ServiceLocator"%>
<%@ page import="com.copyright.svc.userInfo.api.data.UserInfoConsumerContext"%>
<%@ page import="com.copyright.svc.userInfo.api.cyberSource.data.PaymentProfileInfo"%>
<%@ page import="com.copyright.svc.userInfo.api.cyberSource.data.PaymentProfileInfo.PaymentProfileInfoKeys"%>
<%@ page import="com.copyright.svc.userInfo.api.cyberSource.data.SortCriteria"%>
<%@ page import="org.apache.commons.codec.binary.Base64"%>
<%@ page import="com.copyright.ccc.business.services.SystemStatus" %>
<%@ page import="java.util.*"%>
<%@ page errorPage="/jspError.do" %>

<div id="errorSection"></div>

<div class="clearer"></div>
	
<div id="mainPaymentBlock"  style="width:670px">
  <%if(SystemStatus.isCybersourceSiteUp()){%>
  <div id="credit-card-block" style="display:none">
  <br/>
  <div class="clearer"></div>
  <table border="0" class="indent-2"  >
    <tr>
    	<td colspan="6"><b>Select Credit Card:</b><span  class="importanttype"></span></td>
    </tr>
    <tr>
    	<td colspan="2" class="DefaultLabel">
  			<div id="ccRows" class="ccRows">
  			<!--  Display list of credit cards -->
        </div>
      </td>
      </tr>
  </table>
  </div>
   <%}%>
  <p>&nbsp;</p>
  <br/>
  <div class="horiz-rule" id="displayContinue_horz_ruler" style="display:none"></div>
  <div class="txtRight" style="display: none;" id="displayContinue">            
      <html:img src="/media/images/btn_continue.gif" alt="Continue"  width="75" height="18" onclick="submitPayment();" styleId="999" />
  </div>
</div>  

<div class="clearer"></div>





