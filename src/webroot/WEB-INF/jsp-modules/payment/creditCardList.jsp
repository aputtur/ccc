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
<%@ page import="com.copyright.svc.userInfo.api.cyberSource.data.PaymentProfileInfo"%>
 
<%@ page import="java.util.*"%>
<%@ page errorPage="/jspError.do" %>

        	
        	          <logic:empty name="creditCardListForm" property="creditCards">
        	          <table width="640" border="0" frame="box" rules="none" cellpadding="3" cellspacing="0">
        	          <tr><td colspan="4" style="text-alignment:center">&nbsp;No credit cards currently on file. Please add a card.</td>
        	          </tr>
        	          	<tr><td height="25" colspan="2">&nbsp;</td>
				<td height="25" colspan="2">&nbsp;</td>
				<td height="25">&nbsp;</td>
				<td height="25" colspan="3" align="right"><input type="button" style="font-size: 10px; font-weight: bold;" value="Add new card" onlick ="CC_addNewCreditCard();" id="addCardButton" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				</tr>
				</table>
        	          </logic:empty>
        	
      
                
                
                <jsp:useBean id="creditCardListForm" scope="session" class="com.copyright.ccc.web.forms.CreditCardListForm"/>
                <logic:notEmpty name="creditCardListForm" property="creditCards">
                  	
        	<table width="640" border="0" frame="box" rules="none" cellpadding="3" cellspacing="0" class="striped" id="ccTable">
                <tr>
                    <th height="25"  class="PaymentHeaderCell"><p class="LayerLink">&nbsp;</p></th>
                    <th height="25"  class="PaymentHeaderCell"><p class="LayerLink">Card Type</p></th>
                    <th width="92" height="25" align="left" class="PaymentHeaderCell"><p class="LayerLink">Card No.</p></th>
                    <th width="175" height="25" align="left" class="PaymentHeaderCell"><p class="LayerLink">Cardholder's Name</p></th>
                    <th width="88" height="25" align="left" class="PaymentHeaderCell"><p class="LayerLink">Exp. Date</p></th>
                    <th width="118" height="25" class="PaymentHeaderCell"><p class="LayerLink">&nbsp;</p></th>
                </tr>
              
                  <logic:iterate name="creditCardListForm" property="creditCards" id="item" indexId="index" type="PaymentProfileInfo">
                  		<tr>
					
					<td align="left"width="25">
					<%if(!com.copyright.ccc.business.services.payment.CyberSourceUtils.isCreditCardExpired(item.getExpirationDate())){ %>
					<logic:equal value="true" name="creditCardListForm" property="displayRadioButtons">
							<input type="radio" onclick="saveProfileValues(this);" name="payByMethod" class="selectable"  value='<bean:write name="item" property="paymentProfileId" />' id='profileRow<%= index.intValue() + 1 %>'/>
							</logic:equal>
					 <%} %>
					 </td>
					 
					 
					 	<td height="30"  align="left" class="RegularNote">
					 	<span style="display: none;" id='profileRow<%= index.intValue() + 1 %>_paymentProfileId'><bean:write name="item" property="paymentProfileId"/></span>
					 <span style="display: none;" id='profileRow<%= index.intValue() + 1 %>_cccPaymentProfileId'><bean:write name="item" property="cccProfileId"/></span>
					 	<span id='profileRow<%= index.intValue() + 1 %>_cardType'><bean:write name="item" property="cardType"/></span></td>
						<td height="30"  class="RegularNote" align="left"><span id='profileRow<%= index.intValue() + 1 %>_lastFourDigits'><bean:write name="item" property="lastFourDigits"/></span></td>
						<td height="30"  class="RegularNote" align="left"><span id='profileRow<%= index.intValue() + 1 %>_cardholderName'><bean:write name="item" property="cardholderName"/></span></td>
					<%if(com.copyright.ccc.business.services.payment.CyberSourceUtils.isCreditCardExpired(item.getExpirationDate())){ %>
						<td height="30" style="color:red;font-weight:bold;" class="RegularNote" align="left" class="expirable" id="ccExpirationDate<%= index.intValue() + 1 %>"><bean:write name="item" property="expirationDate"/>**<span style="display: none;" id='profileRow<%= index.intValue() + 1 %>_expirationDate'><bean:write name="item" property="expirationDate"/></span></td>
						<%}else{ %>
						<td height="30" class="RegularNote" align="left" class="expirable" id="ccExpirationDate<%= index.intValue() + 1 %>"><bean:write name="item" property="expirationDate"/><span style="display: none;" id='profileRow<%= index.intValue() + 1 %>_expirationDate'><bean:write name="item" property="expirationDate"/></span></td>
						<%} %>
						
							<td height="30"  class="RegularNote" align="left">
							    <a id="modifiable" class="modifiable" href="#" 
							        value='<bean:write name="item" property="paymentProfileId"/>'>Modify</a>
							&nbsp;&nbsp;
							    <a id="deleteable" class="deleteable" href="#" 
							        value='<bean:write name="item" property="paymentProfileId"/>'>Delete</a>
						</td>
			</tr>
		
						
                  </logic:iterate>
                  			<tr><td height="25" colspan="2">&nbsp;</td>
				<td height="25" colspan="2">&nbsp;</td>
				<td height="25">&nbsp;</td>
				<td height="25" colspan="3" align="right"><input type="button" style="font-size: 10px; font-weight: bold;" value="Add new card" onlick ="CC_addNewCreditCard();" id="addCardButton" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				</tr>
				</table>
                  </logic:notEmpty>
                  
                  	
		
 
		





