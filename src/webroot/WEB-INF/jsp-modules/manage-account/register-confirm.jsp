<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/tld/struts-logic.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld"  prefix="bean" %>

<%@ page errorPage="/jspError.do" %>

<%@ page import="com.copyright.ccc.business.services.cart.CartServices" %>
<%@ page import="com.copyright.ccc.business.security.UserContextService" %>


<%@ page import="com.copyright.ccc.config.CC2Configuration" %>
<tiles:useAttribute id="registrationType" name="registrationType" ignore="true" />

<bean:define id="help_url"><%= CC2Configuration.getInstance().getHelpURL() %></bean:define>

<h3>Your pay-per-use account has been successfully created</h3>
        <hr>
        <p>Congratulations, you have successfully created a pay-per-use account on copyright.com. Now you can place orders and get instant permission to use and share content wherever and whenever you need it.</p>

<br>
<p><ul>

<%

if (UserContextService.isInRlnkQuickPricePage() )
{
%>
    <li> <html:link action="/rlQuickPriceLogin.do">Continue to submit your order.</html:link></li>
    Or
    <br/>
    <br/>
    
<%
}
if (!CartServices.isCartEmpty())
{
%>

    <li>Purchase the items in your cart now. <html:link action="/selectCoiPaymentType.do"><html:img src="/media/images/btn_checkout.gif" align="top" alt="Checkout"/></html:link></li>
    Or
    <br/>
    <br/>
    
<%
}
%>

<li><a href="search.do?operation=show&page=ppu">Start using your pay-per-use account</a>.</li>

<li><a href="/content/cc3/en/toolbar/productsAndSolutions/payPerUsePermissionServices/welcome_to_copyrightcom.html">Learn more about copyright.com</a>.</li>

<li><a href="/content/cc3/en/toolbar/education.html">Visit Copyright Central for copyright news and information</a>.</li>

<li><a href="home.do"> Go to the copyright.com Home page</a>.</li>  
       	
</ul></p>

    <p><br>Need assistance?  Visit the <a href="<bean:write name="help_url" />">help</a> area or <a href="mailto:info@copyright.com">contact customer support</a>.</p>		
    
<!-- Webtrends tags for capturing scenarios -->
<logic:equal name="registrationType" value="I">
    <META name="WT.si_n" content="Indv_reg">
    <META name="WT.si_p" content="Indv_reg_complete">
</logic:equal>
<logic:equal name="registrationType" value="O">
    <META name="WT.si_n" content="Org_reg">
    <META name="WT.si_p" content="Org_reg_complete">
</logic:equal>
<!-- end Webtrends tags -->