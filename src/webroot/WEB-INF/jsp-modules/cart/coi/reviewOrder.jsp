<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/tld/struts-bean.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/tld/struts-logic.tld" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/cc2-util.tld" %>
<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>
<%@ page errorPage="/jspError.do" %>
<%@ page import="com.copyright.ccc.business.services.cart.PurchasablePermission" %>
<%@ page import="com.copyright.ccc.web.transaction.RepublicationConstants" %>
<%@ page import="com.copyright.data.account.Address" %>
<%@ page import="com.copyright.data.account.Country" %>
<script src="<html:rewrite page="/resources/commerce/js/contextualHelp.js"/>" type="text/javascript"></script>
<link href="<html:rewrite page="/resources/commerce/css/contextualHelp.css"/>" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="<html:rewrite page='/resources/commerce/css/ccc-new.css'/>">
<script type="text/javascript" src="<html:rewrite page="/resources/commerce/js/reviewOrder.js"/>" ></script>
<script type="text/javascript" src="<html:rewrite page="/resources/commerce/js/contentLoader.js"/>" ></script>
<script type="text/javascript" src="<html:rewrite page="/resources/commerce/js/eventLogger.js"/>" ></script>
<script type="text/javascript" src="<html:rewrite page="/resources/commerce/js/help.js"/>" ></script>
<link href="<html:rewrite page="/resources/commerce/css/rightslink_font.css"/>" rel="stylesheet" type="text/css" />
<link type="text/css" rel="stylesheet" href="/resources/commerce/css/cart_all.css"/>
<!--[if lt IE 8]><link rel="stylesheet" type="text/css" href="/resources/commerce/css/css/cart_ie.css"/><![endif]-->
<style>
#ecom-content-wrapper{
padding:0px;
}
h3{
text-transform:none;
}
#bottomcorners{
background:none;
}
</style>
<script type="text/javascript">
if (browserInfo.isIE){
                document.write ("<LINK REL='stylesheet' type='text/css' href='/resources/commerce/css/rightslink_layout_msie.css'>");
}
else{
                document.write ("<LINK REL='stylesheet' type='text/css' href='/resources/commerce/css/rightslink_layout_ns6.css'>");
}
  var eventLogger;
  function initializeEventLogger(){
    var eventLoggerServiceURL = "<html:rewrite page="/advisor/eventLogger.do?operation=logEvent" />";
    eventLogger = new EventLogger( eventLoggerServiceURL );
    var contactLibraryLink = document.getElementById("contactLibraryLink");
    
    if( contactLibraryLink ){
      contactLibraryLink.onclick = performLogContactLibrary;
    }
  }
   function performLogContactLibrary(){
     var contactLibraryLink = document.getElementById("contactLibraryLink");
     var communicationValue = new String( escape( contactLibraryLink.href ) );   
     eventLogger.logEvent( EVENT_CONTACT_SME_CHECKOUT , communicationValue );
  }
var rdioCC = document.getElementById('ccSelect');
var rdioInv = document.getElementById('invSelect');

  function toggleCCRadio(rdioBtn) {
     if (rdioCC) { 
       rdioBtn.checked = false; 
       rdioCC = false;
     } else {
       rdioCC = true;
     }
  }
  function toggleInvRadio(rdioBtn) {
     if (rdioInv) { 
       rdioBtn.checked = false; 
       rdioInv = false;
     } else {
       rdioInv = true;
     }
  }
</script>
 <%
           String rightslnkMoreInfo = null;
 				rightslnkMoreInfo = "Rightslink is a rights management service from Copyright Clearance Center. If you pay by credit card, your order will be finalized and your card will be charged within 24 hours. If you pay by invoice, you can change or cancel your order until the invoice is generated.";
            pageContext.setAttribute("rightslnkMoreInfoBody", rightslnkMoreInfo, PageContext.PAGE_SCOPE);
            
  %>
<bean:define id="moreInfoTitle">Rightslink</bean:define>
<bean:define id="termsTitle">Terms and Conditions</bean:define>
<html:form action="confirmCoiCartPurchase.do?operation=confirmPurchase" method="post" styleId="sbmtFrm">
<input type="hidden" name="page" value="1">
<html:hidden name="reviewSubmitCartActionFormCOI" property="userName"/>
<html:hidden name="reviewSubmitCartActionFormCOI" property="paymentType"/>
<!-- <html:hidden name="reviewSubmitCartActionFormCOI" property="purchaseOrderNumber"/> -->
<html:hidden name="reviewSubmitCartActionFormCOI" property="creditCardType"/>
<html:hidden name="reviewSubmitCartActionFormCOI" property="creditCardNumber"/>
<html:hidden name="reviewSubmitCartActionFormCOI" property="creditCardNameOn"/>
<html:hidden name="reviewSubmitCartActionFormCOI" property="paymentProfileId"/>
<html:hidden name="reviewSubmitCartActionFormCOI" property="cccPaymentProfileId"/>
<html:hidden name="reviewSubmitCartActionFormCOI" property="expirationDate"/>
<html:hidden name="reviewSubmitCartActionFormCOI" property="expMonth"/>
<html:hidden name="reviewSubmitCartActionFormCOI" property="expYear"/>
<html:hidden name="reviewSubmitCartActionFormCOI" property="alwaysInvoice"/>
<html:hidden name="reviewSubmitCartActionFormCOI" property="alwaysInvoiceFlag"/>
<input id="payment" type="hidden" name="editPayment">

<!-- Begin Main Content -->
<!-- <div id="ecom-content-wrapper"> -->

<!-- Begin Progress -->
<div id="ecom-progress"><html:img src="/media/images/ecomprogress-payment.gif" alt="Payment" width="60" height="24" /><html:img src="/media/images/ecomprogress-review-on.gif" alt="Review" width="52" height="24" /><html:img src="/media/images/ecomprogress-confirmation.gif" alt="Confirmation" width="92" height="24" /></div>
<!-- Begin Progress -->

<!-- Begin Left Content -->
<div >
<div class="holder">
<div id="main">
<div class="floatleft">
	<h1>Step 2: Review Your Order</h1>
<a href="cart.do" class="btn-yellow2 pad001" id="995">Cancel order</a>
<a href="search.do?operation=show&page=last" class="btn-yellow2 pad001" id="996">Add another item to this order</a>
</div>	
<div class="floatright"><p class="icon-alert clearer"><span style="background-color: #FFFF00"><font size=2>Your order is not complete.</font></span> <a href="#termsAnchor" ><u>Please accept terms and conditions and then Place your order.</u></a></p>	
</div>
<div class="clearer"></div>
<div class="horiz-rule"></div>
<div class="calloutboxnobackground floatright" style="clear:none; width: 400px;">If you pay by credit card, your order will be finalized and your card will be charged within 24 hours. If you pay by invoice, you can change or cancel your order until the invoice is generated. </div>	
<!-- <div class="calloutboxnobackground floatright" style="clear:none; width: 400px;">If you pay by credit card, your order will be finalized and your card will be charged within 24 hours. If you pay by invoice, you can change or cancel your order until the invoice is generated. </div> -->

<logic:equal name="reviewSubmitCartActionFormCOI" property="displayEditPaymentLink" value="true">	
 	<h2>Payment Information <a class="subtitle" href="reviewCoiPayment.do" onClick="" id="997"><u>Edit</u></a> </h2>
 	<a href="/content/cc3/en/toolbar/productsAndSolutions/payPerUsePermissionServices/credit_and_paymentpolicy.html" target="_blank"><u>Review Credit and Payment Policy</u></a>
</logic:equal>

<div class="item-details">
	<p class="indent-1">
          
      <br></br>
          <bean:write name="reviewSubmitCartActionFormCOI" property="userName" /> <br />
          
          <logic:notEmpty name="reviewSubmitCartActionFormCOI" property="company">
            <bean:write name="reviewSubmitCartActionFormCOI" property="company" /> <br />
          </logic:notEmpty>
          
          <logic:notEmpty name="reviewSubmitCartActionFormCOI" property="email">
            <bean:write name="reviewSubmitCartActionFormCOI" property="email" /> <br />
          </logic:notEmpty>
          <logic:notEmpty name="reviewSubmitCartActionFormCOI" property="phone">
            <bean:write name="reviewSubmitCartActionFormCOI" property="phone" /> <br />
          </logic:notEmpty>
          
          Payment Method:
          <!-- Invoice <br /> -->
          <logic:equal name="reviewSubmitCartActionFormCOI" property="paymentType" value="invoice">
          <bean:write name="reviewSubmitCartActionFormCOI" property="paymentType" /> <br />
          PO#:
            <html:text name="reviewSubmitCartActionFormCOI" property="purchaseOrderNumber" size="20" maxlength="50" />
            (optional)
          <!-- <input type="text" width="20" name="" /> -->
          
          </logic:equal>
          <logic:equal name="reviewSubmitCartActionFormCOI" property="paymentType" value="n/a">
          <bean:write name="reviewSubmitCartActionFormCOI" property="paymentType" /> <br />
          PO#:
            <html:text name="reviewSubmitCartActionFormCOI" property="purchaseOrderNumber" size="20" maxlength="50" />
            (optional)
          <!-- <input type="text" width="20" name="" /> -->
          
          </logic:equal>
          
          <logic:equal name="reviewSubmitCartActionFormCOI" property="paymentType" value="credit-card">
          <bean:write name="reviewSubmitCartActionFormCOI" property="creditCardDisplay" /> <br />
          </logic:equal>
       </p>   

        <logic:equal name="reviewSubmitCartActionFormCOI" property="paymentType" value="invoice">
          <logic:equal name="reviewSubmitCartActionFormCOI" property="teleSalesUp" value="true"> 
          <logic:notEmpty name="reviewSubmitCartActionFormCOI" property="address1">   
            <p>
         <b>Billing address:</b><br />
        
	        <logic:notEmpty name="reviewSubmitCartActionFormCOI" property="company">
	            <bean:write name="reviewSubmitCartActionFormCOI" property="company" /> <br />
	        </logic:notEmpty>
	        
	        <bean:write name="reviewSubmitCartActionFormCOI" property="address1" /> <br />
	        <logic:notEmpty name="reviewSubmitCartActionFormCOI" property="address2">
	            <bean:write name="reviewSubmitCartActionFormCOI" property="address2" /> <br />
	        </logic:notEmpty>
	        <bean:write name="reviewSubmitCartActionFormCOI" property="city" />, 
	        <bean:write name="reviewSubmitCartActionFormCOI" property="state" /> 
	        <bean:write name="reviewSubmitCartActionFormCOI" property="zip" /> <br />
	        <logic:notEmpty name="reviewSubmitCartActionFormCOI" property="country">
	            <bean:write name="reviewSubmitCartActionFormCOI" property="country" /> <br />
	        </logic:notEmpty>
	        </p>
	        </logic:notEmpty>
          </logic:equal>
        </logic:equal>
    <logic:equal name="reviewSubmitCartActionFormCOI" property="paymentType" value="credit-card">
    <logic:equal name="reviewSubmitCartActionFormCOI" property="containsSpecialOrders" value="true">
    <p>
        <b>Special order invoices will be sent to:</b><br />
        <bean:write name="reviewSubmitCartActionFormCOI" property="company" /> <br />
        <bean:write name="reviewSubmitCartActionFormCOI" property="address1" /> <br />
        <logic:notEmpty name="reviewSubmitCartActionFormCOI" property="address2">
            <bean:write name="reviewSubmitCartActionFormCOI" property="address2" /> <br />
        </logic:notEmpty>
        <bean:write name="reviewSubmitCartActionFormCOI" property="city" />, 
        <bean:write name="reviewSubmitCartActionFormCOI" property="state" /> 
        <bean:write name="reviewSubmitCartActionFormCOI" property="zip" /> <br />
        <logic:notEmpty name="reviewSubmitCartActionFormCOI" property="country">
            <bean:write name="reviewSubmitCartActionFormCOI" property="country" /> <br />
        </logic:notEmpty>
    </p>
   </logic:equal>        
    </logic:equal>
    
</div>
<div class="horiz-rule"></div>

<!-- ************************************************************************ -->
 <logic:greaterThan value="0" name="reviewSubmitCartActionFormCOI" property="numberOfCartItems">
       
            <%-- Callout: Course Information --%>
              <p class="largertype bold" style="height: 10px;">Order Details &nbsp;<a href="cart.do" class="normaltype" id="999"><u>Edit cart</u></a></p>

		<logic:equal value="false" name="reviewSubmitCartActionFormCOI" property="showSectionHeaders">   
              		<h3 class="single"></h3>                	      
          		</logic:equal>                    
         <logic:equal value="true" name="reviewSubmitCartActionFormCOI" property="showSectionHeaders">   
            <h3>Course items</h3>   
        	 
        	</logic:equal>
            
            <logic:notEmpty name="reviewSubmitCartActionFormCOI" property="courseDetails">
            
                <logic:equal name="reviewSubmitCartActionFormCOI" property="academicCart" value="true" >
                
                          	<div class="choise-block">
						<div class="tilte">
							<strong>Course: <bean:write name="reviewSubmitCartActionFormCOI" property="courseHeader.courseName" /></strong>
							<ul>
								<li><a href="/editCartCoiCourseDetails.do?operation=defaultOperation" Id="998">Edit course</a></li>
								
							</ul>
						</div>
							<div class="parameter-list">
							<dl>
							  <dt>University/Institution: </dt><dd>
							  <logic:empty name="reviewSubmitCartActionFormCOI" property="courseHeader.schoolCollege">&nbsp;</logic:empty>
							  <bean:write name="reviewSubmitCartActionFormCOI" property="courseHeader.schoolCollege" />
							  </dd>
                        	 <dt>Start of term: </dt><dd>
							  <logic:empty name="reviewSubmitCartActionFormCOI" property="courseHeader.startOfTermDate">&nbsp;</logic:empty>
                        	 <bean:write name="reviewSubmitCartActionFormCOI" property="courseHeader.startOfTermDate" format="MM/dd/yyyy"/>
                        	 </dd>
                             <dt>Course number: </dt><dd>
							  <logic:empty name="reviewSubmitCartActionFormCOI" property="courseHeader.courseNumber">&nbsp;</logic:empty>
                             <bean:write name="reviewSubmitCartActionFormCOI" property="courseHeader.courseNumber" /></dd>
                             <dt>Number of students: </dt><dd>
							  <logic:empty name="reviewSubmitCartActionFormCOI" property="courseHeader.numberOfStudents">&nbsp;</logic:empty>
                             <bean:write name="reviewSubmitCartActionFormCOI" property="courseHeader.numberOfStudents" /></dd>
							</dl>
							<dl>
							<dt>Instructor: </dt><dd><logic:empty name="reviewSubmitCartActionFormCOI" property="courseHeader.instructor">&nbsp;</logic:empty>
							<bean:write name="reviewSubmitCartActionFormCOI" property="courseHeader.instructor" /></dd>
                        <dt>Your reference: </dt><dd>
							  <logic:empty name="reviewSubmitCartActionFormCOI" property="courseHeader.reference">&nbsp;</logic:empty>
                        <bean:write name="reviewSubmitCartActionFormCOI" property="courseHeader.reference" /></dd>
                        <dt>Accounting reference: </dt><dd>
							  <logic:empty name="reviewSubmitCartActionFormCOI" property="courseHeader.accountingReference">&nbsp;</logic:empty>
							  <bean:write name="reviewSubmitCartActionFormCOI" property="courseHeader.accountingReference" /></dd>
                        <dt>Order entered by: </dt><dd>
							  <logic:empty name="reviewSubmitCartActionFormCOI" property="courseHeader.orderEnteredBy">&nbsp;</logic:empty>
							  <bean:write name="reviewSubmitCartActionFormCOI" property="courseHeader.orderEnteredBy" />
                        			</dd>
						
							</dl>
						</div>
						</div>
                </logic:equal>
            </logic:notEmpty>
        
            <!-- End Callout: CourseInformation -->
            <!--  *********************** Begin Academic Items ******************************************** -->
            <logic:notEmpty name="reviewSubmitCartActionFormCOI" property="academicCartItems">
                   <bean:define id="lisOfAcademicCartItems" name="reviewSubmitCartActionFormCOI" property="academicCartItems"></bean:define>
	               <bean:define id="academicRegularBoxStyle">border-top:0px;</bean:define>
	           <logic:iterate  name="reviewSubmitCartActionFormCOI" property="academicCartItems"  id="item" indexId="index" type="PurchasablePermission">
	               <logic:equal name="item" property="specialOrder" value="false">
					<div class="product-box"  style="<%=academicRegularBoxStyle%>">
	               <%@ include file = "/WEB-INF/jsp-modules/cart/coi/tiles/reviewAcademicItems.jsp" %>
					</div>
					<bean:define id="academicRegularBoxStyle">border-top:1px solid #CCCCCC;</bean:define>
	               </logic:equal>
		        </logic:iterate>
		            <!-- *************** END Regular Order Items *********************** -->
	            
	            <!-- *************** BEGIN Special Order Items *********************** -->
	            <logic:equal value="true" name="reviewSubmitCartActionFormCOI" property="containsAcademicSpecialOrders">
			     <div class="choise-block" style="padding: 4px 0px 0px;"> 
							<div class="tilte" style="padding-left: 4px; margin: 0px;">
								<strong>Special Orders</strong>
							</div>
				   <bean:define id="academicSpecialBoxStyle">border-top:0px;</bean:define>			
				<logic:iterate  name="reviewSubmitCartActionFormCOI" property="academicCartItems"  id="item" indexId="index" type="PurchasablePermission">
		  			<logic:equal name="item" property="specialOrder" value="true">
						<div class="product-box" style="width:688px;<%=academicSpecialBoxStyle%>">
							<%@ include file = "/WEB-INF/jsp-modules/cart/coi/tiles/reviewAcademicItems.jsp" %>
						</div>
						<br/>
							<bean:define id="academicSpecialBoxStyle">border-top:1px solid #CCCCCC;</bean:define>
					</logic:equal>
				</logic:iterate>
	            </div>
	            </logic:equal>
            </logic:notEmpty>
            <!-- *************** BEGIN Regular Order Items *********************** -->
            <logic:equal value="true" name="reviewSubmitCartActionFormCOI" property="showSectionHeaders">
                    <div class="total" style="border-top:1px solid #CCCCCC;" >
						<strong class="number" style="color:#666666">Course items: <span><bean:write name="reviewSubmitCartActionFormCOI" property="numberOfAcademicCartItems" /></span></strong>
						<logic:equal name="reviewSubmitCartActionFormCOI" property="hasOnlyNonPricedAcademicOrders" value="false">
                 				<strong class="total-price" style="color:#666666;" >Course total: <span><bean:write name="reviewSubmitCartActionFormCOI" property="academicCartTotal" /></span>
                 				<logic:equal name="reviewSubmitCartActionFormCOI" property="showExcludeTBDItemAcademicText" value="true">
                        		<br/><div style="text-align:right;font-size:12px;color:#666666;">(Excludes $TBD items)</div>
                    			</logic:equal>
                    			</strong>
                 				</logic:equal>
                 			<logic:equal name="reviewSubmitCartActionFormCOI" property="hasOnlyNonPricedAcademicOrders" value="true">
                    			<strong class="total-price" style="color:#666666">Course total: <span style="font-size:14px">$TBD</span>
                    			</strong>
                			</logic:equal>
					</div>
     		<h3>Single Items</h3>
        	</logic:equal>
            <!-- *************** END Special Order Items *********************** -->
            <!-- ************************ End Academic Items ********************************************** -->
            <!-- *************** BEGIN Regular Order Items *********************** -->
            <logic:notEmpty name="reviewSubmitCartActionFormCOI" property="nonAcademicCartItems">
                  <bean:define id="nonAcademicRegularBoxStyle">border-top:0px;</bean:define>                              
	            <logic:iterate name="reviewSubmitCartActionFormCOI" property="nonAcademicCartItems" id="item" indexId="index" type="PurchasablePermission">         
		            <logic:equal name="item" property="specialOrder" value="false"> <!-- Filter Out Special Order -->
						<div class="product-box" style="<%=nonAcademicRegularBoxStyle%>">
	    		           <%@ include file = "/WEB-INF/jsp-modules/cart/coi/tiles/reviewNonAcademicItems.jsp" %>
						</div>
						<bean:define id="nonAcademicRegularBoxStyle">border-top:1px solid #CCCCCC;</bean:define>
	                </logic:equal> <!--Filter Out Special Orders -->
	            </logic:iterate>
            <!-- *************** END Regular Order Items *********************** -->
            
            <!-- *************** BEGIN Non Academic Special Order Permission Items *********************** -->
            <logic:equal value="true" name="reviewSubmitCartActionFormCOI" property="hasNonReprintSpecialOrders">
					<div class="choise-block" style="padding: 4px 0px 0px;">
									<div class="tilte" style="padding-left: 4px; margin: 0px;">
										<strong>Special Orders</strong>
									</div>
						<bean:define id="nonAcademicSpecialBoxStyle">border-top:0px;</bean:define>  		
			            <logic:iterate name="reviewSubmitCartActionFormCOI" property="nonAcademicCartItems" id="item" indexId="index" type="PurchasablePermission">
			             <logic:equal name="item" property="specialOrder" value="true">  <!-- Filter Out Special Order -->
			            <logic:equal name="item" property="reprint" value="false">  <!-- Filter Out Non  Special Order -->
							<div class="product-box" style="width:688px;<%=nonAcademicSpecialBoxStyle%>">
								<%@ include file = "/WEB-INF/jsp-modules/cart/coi/tiles/reviewNonAcademicItems.jsp" %>
							</div>
							<br/>	
							<bean:define id="nonAcademicSpecialBoxStyle">border-top:1px solid #CCCCCC;</bean:define>
			                </logic:equal> <!--Filter Out Reprints -->
			                </logic:equal> <!--Filter Out Special Orders -->
			            </logic:iterate>
			        </div>	    
            </logic:equal>
            <!-- *************** END Non Academic Special Order Items *********************** -->

            <!-- *************** BEGIN Reprint Items *********************** -->
            <logic:equal value="true" name="reviewSubmitCartActionFormCOI" property="hasReprintSpecialOrders">
		<div class="choise-block" style="padding: 4px 0px 0px;">
						<div class="tilte" style="padding-left: 4px; margin: 0px;">
							<strong>Reprints</strong>
						</div>
			<bean:define id="rePrintBoxStyle">border-top:0px;</bean:define> 
            <logic:iterate name="reviewSubmitCartActionFormCOI" property="nonAcademicCartItems" id="item" indexId="index" type="PurchasablePermission">

            <logic:equal name="item" property="specialOrder" value="true">  <!-- Filter Out Special Order -->
            <logic:equal name="item" property="reprint" value="true">  <!-- Filter Out Non  Special Order -->
				<div class="product-box" style="width:688px;<%=rePrintBoxStyle%>">
               <%@ include file = "/WEB-INF/jsp-modules/cart/coi/tiles/reviewNonAcademicItems.jsp" %>
				</div>
				<br/>
				<bean:define id="rePrintBoxStyle">border-top:1px solid #CCCCCC;</bean:define>
                </logic:equal> <!--Filter Out Permisssions -->
                </logic:equal> <!--Filter Out Non Special Orders -->
            </logic:iterate>
            </div>
            </logic:equal>
            </logic:notEmpty>
            <!-- *************** END Reprint Items *********************** -->
        </logic:greaterThan>
 			<logic:equal value="true" name="reviewSubmitCartActionFormCOI" property="showSectionHeaders">
          			<div class="total" style="border-top:1px solid #CCCCCC;" >
						<strong class="number"  style="color:#666666">Single items: <span><bean:write name="reviewSubmitCartActionFormCOI" property="numberOfNonAcademicCartItems" /></span></strong>
                 			<logic:equal name="reviewSubmitCartActionFormCOI" property="hasOnlyNonPricedNonAcademicOrders" value="false">
                 					<strong class="total-price" style="color:#666666">Single item total: <span><bean:write name="reviewSubmitCartActionFormCOI" property="nonAcademicCartTotal" /></span>
                 					<logic:equal name="reviewSubmitCartActionFormCOI" property="showExcludeTBDItemNonAcademicText" value="true">
                 					<br/><div style="text-align:right;font-size:12px;color:#666666;">(Excludes $TBD items)</div>
                 					</logic:equal>
                 					</strong>
                 				</logic:equal>
                 			
                 			<logic:equal name="reviewSubmitCartActionFormCOI" property="hasOnlyNonPricedNonAcademicOrders" value="true">
                    				<strong class="total-price" style="color:#666666">Single item total: <span>$TBD</span>
                    				</strong>
                			</logic:equal>
					</div>
		</logic:equal>
        <logic:lessEqual name="reviewSubmitCartActionFormCOI" property="numberOfCartItems" value="0">
            <b>There are no items in your shopping cart.</b>
        </logic:lessEqual>
        <!-- Result -->
<!-- *********************************************************************** -->

	<div class="total-sum">
<div class="order_total">
<div class="floatleft">
<h2 style="margin-top:0px">Total order items:&nbsp;	<label class="largetype"><bean:write name="reviewSubmitCartActionFormCOI" property="numberOfCartItems" /></label>	
                		</h2>

</div>
  <div class="floatright largertype bold" style="font-size:16px;margin-top:0px">
  <logic:equal name="reviewSubmitCartActionFormCOI" property="hasOnlyNonPricedOrders" value="false">
   		<logic:equal name="reviewSubmitCartActionFormCOI" property="fundingCurrencyType" value="USD">
    		Order Total: <bean:write name="reviewSubmitCartActionFormCOI" property="cartTotalWithNoDollarSign" />  USD<br />
		</logic:equal>
  		<logic:equal name="reviewSubmitCartActionFormCOI" property="fundingCurrencyType" value="EUR">
			<logic:equal name="reviewSubmitCartActionFormCOI" property="displayChargeTotal" value="true">
				Order Total: <bean:write name="reviewSubmitCartActionFormCOI" property="cartTotalWithNoDollarSign" />  USD<br />
				Charge Total: <bean:write name="reviewSubmitCartActionFormCOI" property="fundingCurrencyAmount" />   EUR<br />
    					 (<bean:write name="reviewSubmitCartActionFormCOI" property="cartChargeTotalWithNoDollarSign" />  USD)<br />
			</logic:equal>
			<logic:equal name="reviewSubmitCartActionFormCOI" property="displayChargeTotal" value="false">
				Order Total: <bean:write name="reviewSubmitCartActionFormCOI" property="fundingCurrencyAmount" />  EUR<br />
    					 (<bean:write name="reviewSubmitCartActionFormCOI" property="cartTotalWithNoDollarSign" />  USD)<br />
			</logic:equal>
  		</logic:equal>
  		<logic:equal name="reviewSubmitCartActionFormCOI" property="fundingCurrencyType" value="GBP">
			<logic:equal name="reviewSubmitCartActionFormCOI" property="displayChargeTotal" value="true">
				Order Total: <bean:write name="reviewSubmitCartActionFormCOI" property="cartTotalWithNoDollarSign" />  USD<br />
				Charge Total: <bean:write name="reviewSubmitCartActionFormCOI" property="fundingCurrencyAmount" />  GBP<br />
    					 (<bean:write name="reviewSubmitCartActionFormCOI" property="cartChargeTotalWithNoDollarSign" />  USD)<br />
			</logic:equal>
				<logic:equal name="reviewSubmitCartActionFormCOI" property="displayChargeTotal" value="false">
				Order Total:  <bean:write name="reviewSubmitCartActionFormCOI" property="fundingCurrencyAmount" />  GBP<br />
    					 (<bean:write name="reviewSubmitCartActionFormCOI" property="cartTotalWithNoDollarSign" />  USD)<br />
				</logic:equal>
  		
  		</logic:equal>
  		<logic:equal name="reviewSubmitCartActionFormCOI" property="fundingCurrencyType" value="JPY">
				<logic:equal name="reviewSubmitCartActionFormCOI" property="displayChargeTotal" value="true">
				Order Total: <bean:write name="reviewSubmitCartActionFormCOI" property="cartTotalWithNoDollarSign" />  USD<br />
				Charge Total: <bean:write name="reviewSubmitCartActionFormCOI" property="fundingCurrencyAmount" />  JPY<br />
    					 (<bean:write name="reviewSubmitCartActionFormCOI" property="cartChargeTotalWithNoDollarSign" />  USD)<br />
			</logic:equal>
				<logic:equal name="reviewSubmitCartActionFormCOI" property="displayChargeTotal" value="false">
				Order Total:  <bean:write name="reviewSubmitCartActionFormCOI" property="fundingCurrencyAmount" />  JPY<br />
    					 (<bean:write name="reviewSubmitCartActionFormCOI" property="cartTotal" />  USD)<br />
				</logic:equal>
  		</logic:equal>
  		<logic:equal name="reviewSubmitCartActionFormCOI" property="fundingCurrencyType" value="CAD">
			  <logic:equal name="reviewSubmitCartActionFormCOI" property="displayChargeTotal" value="true">
				Order Total: <bean:write name="reviewSubmitCartActionFormCOI" property="cartTotalWithNoDollarSign" />  USD<br />
				Charge Total:  <bean:write name="reviewSubmitCartActionFormCOI" property="fundingCurrencyAmount" /> CAD<br />
    					 (<bean:write name="reviewSubmitCartActionFormCOI" property="cartChargeTotalWithNoDollarSign" />  USD)<br />
			  </logic:equal>
				<logic:equal name="reviewSubmitCartActionFormCOI" property="displayChargeTotal" value="false">
				Order Total:   <bean:write name="reviewSubmitCartActionFormCOI" property="fundingCurrencyAmount" />  CAD<br />
    					 (<bean:write name="reviewSubmitCartActionFormCOI" property="cartTotal" />  USD)<br />
				</logic:equal>
  		</logic:equal>
   		
      <logic:equal name="reviewSubmitCartActionFormCOI" property="fundingCurrencyType" value="CHF">
        <logic:equal name="reviewSubmitCartActionFormCOI" property="displayChargeTotal" value="true">
        Order Total: <bean:write name="reviewSubmitCartActionFormCOI" property="cartTotalWithNoDollarSign" />  USD<br />
        Charge Total:  <bean:write name="reviewSubmitCartActionFormCOI" property="fundingCurrencyAmount" /> CHF<br />
               (<bean:write name="reviewSubmitCartActionFormCOI" property="cartChargeTotalWithNoDollarSign" />  USD)<br />
        </logic:equal>
        <logic:equal name="reviewSubmitCartActionFormCOI" property="displayChargeTotal" value="false">
        Order Total:   <bean:write name="reviewSubmitCartActionFormCOI" property="fundingCurrencyAmount" />  CHF<br />
               (<bean:write name="reviewSubmitCartActionFormCOI" property="cartTotal" />  USD)<br />
        </logic:equal>
      </logic:equal>
  </logic:equal>
  
  <logic:equal name="reviewSubmitCartActionFormCOI" property="hasOnlyNonPricedOrders" value="true">
  	                    Order Total: <span class="largetype">$TBD</span><br />
  </logic:equal>
  <logic:equal name="reviewSubmitCartActionFormCOI" property="showExcludeTBDItemText" value="true">
                        			 <span class="smalltype normal" style="font-size:12px;font-weight:normal;color: #000000">(Excludes $TBD items)</span>
</logic:equal>

<logic:equal name="reviewSubmitCartActionFormCOI" property="paymentType" value="credit-card">
    <logic:equal name="reviewSubmitCartActionFormCOI" property="containsSpecialOrders" value="true">
  		<div class=" largertype normal" style="width:350px;font-weight:normal;color: #000000">
 	 <br/>
  <logic:equal name="reviewSubmitCartActionFormCOI" property="hasReprintSpecialOrders" value="true">
  <strong>Note:</strong> <bean:write name="reviewSubmitCartActionFormCOI" property="cartChargeTotal"/> of your total will be charged to your credit card immediately. Your card will be charged for Special Order and Reprint items later.
  </logic:equal>
    <logic:equal name="reviewSubmitCartActionFormCOI" property="hasReprintSpecialOrders" value="false">
    <logic:equal name="reviewSubmitCartActionFormCOI" property="hasNonReprintSpecialOrders" value="true">
  <strong>Note:</strong> <bean:write name="reviewSubmitCartActionFormCOI" property="cartChargeTotal"/> of your total will be charged to your credit card immediately. Your card will be charged for Special Order items later.
  </logic:equal>
  </logic:equal>
  </div>
  </logic:equal>
  </logic:equal>
</div>
	
<bean:define id="displayBothTC" >
<logic:equal name="reviewSubmitCartActionFormCOI" property="hasRePrintsItems" value="true">
	<logic:equal name="reviewSubmitCartActionFormCOI" property="hasPreviewPdfURL" value="true">
	true
	</logic:equal>
<logic:equal name="reviewSubmitCartActionFormCOI" property="hasPreviewPdfURL" value="false">
false
</logic:equal>
</logic:equal>
<logic:equal name="reviewSubmitCartActionFormCOI" property="hasRePrintsItems" value="false">
false
</logic:equal>
</bean:define>
<br/>
<br/>
Your order is not complete.
<logic:equal name="displayBothTC" value="true">
<div class="clearer"></div>

<bean:define id="moreInfoRLRTitle">For reprints</bean:define>
 <bean:define id="rightslnkRLRMoreInfoBody">Use the PREVIEW option in the order details of your reprint items to view a preview.</bean:define>
	<div class="floatleft largertype bold" >For reprints:</div><br/>
	<div class="floatleft largertype normal" ><input id="rlrtermz" type="checkbox" name="acceptRLRTerms" value="ON" /> I agree that I have previewed all my reprints orders <util:contextualHelp titleName="moreInfoRLRTitle" bodyName="rightslnkRLRMoreInfoBody" rollover="false">More Info<br/></util:contextualHelp>
	</div>
	<div class="clearer"></div>
	<div class="floatleft largertype bold" >Terms and Conditions:</div><br/>
	<div class="clearer"></div>
	
	<div class="floatleft largertype normal" ><input id="termz" type="checkbox" name="acceptTerms" value="ON" /> I accept the <a href="reviewCoiTerms.do" target="_blank" name="termsAnchor"><u>terms and conditions</u></a>
	</div>
</logic:equal>

<logic:equal name="displayBothTC" value="false">
<div class="clearer"></div>
	<div class="floatleft largertype normal" ><input id="termz" type="checkbox" name="acceptTerms" value="ON" /> I accept the <a href="reviewCoiTerms.do" target="_blank" name="termsAnchor"><u>terms and conditions</u></a>
	</div>
</logic:equal>
<html:img src="/media/images/btn_placeorder_dblue.gif" alt="Place Order" styleId="submitBtn" width="100" height="23" onclick="this.disabled=true;sbmtPage();" /><br />
	
	<div class="clearer"></div>	
</div>
</div>
<div class="clearer"></div>
</div> <!-- main-->	
</div> <!-- holder-->  
	
</div>
<!-- </div> -->

<div class="clearer"></div>

<!-- </div> -->
<!-- End Main Content --> 
</html:form>

<script type="text/javascript">

 // window.onload = initPage;

<%
  //String smeEmailAddress = "";
  
  //Comment for now 11/7/2006
  /* SME sme = UserContextHelper.getCurrent().getSME();
  
  if( sme != null )
  {
    smeEmailAddress = sme.getEmailAddress();
  } */
%>

  function initPage() {
 
  }
 function setUpContactLibrary() {
    setUpContactLibraryLink();
    initializeEventLogger();    
  }
 function setUpContactLibraryLink() {
      var contactLibraryEmailLink="";
      var contactLibraryLink = document.getElementById("contactLibraryLink");
    if( contactLibraryLink ){
      contactLibraryLink.href = contactLibraryEmailLink;
    } 
  }
</script>

<!-- Webtrends tags for capturing scenarios -->
    <META name="WT.si_n" content="Checkout">
    <META name="WT.si_x" content="7">
<!-- end Webtrends tags -->
<script>
function toggleDetails(link,divBlock){
	var liBlock='li_'+divBlock;
	//alert('Block: ' + divBlock);
	if($('#'+divBlock).css('display')=='none'){
		$('#'+liBlock).removeClass("collapseDetails");
		$('#'+liBlock).addClass("expandDetails");
		$(link).text('Hide details');
		$('#'+divBlock).show();
	}else{
		$('#'+liBlock).removeClass("expandDetails");
		$('#'+liBlock).addClass("collapseDetails");
		$(link).text('View details');
		$('#'+divBlock).hide();
	}
}
</script>