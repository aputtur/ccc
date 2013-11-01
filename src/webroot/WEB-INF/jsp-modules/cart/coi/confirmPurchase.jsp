<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/cc2-util.tld" prefix="util"%>

<%@ page errorPage="/jspError.do" %>
<%@ page import="com.copyright.ccc.web.transaction.RepublicationConstants" %>
<%@ page import="com.copyright.data.account.Address" %>
<%@ page import="com.copyright.data.account.Country" %>

<script src="<html:rewrite page="/resources/commerce/js/contextualHelp.js"/>" type="text/javascript"></script>
<link href="<html:rewrite page="/resources/commerce/css/contextualHelp.css"/>" rel="stylesheet" type="text/css" />


<link rel="stylesheet" type="text/css" href="<html:rewrite page='/resources/commerce/css/noPrint.css'/>">
<link rel="stylesheet" type="text/css" href="<html:rewrite page='/resources/commerce/css/ccc-new.css'/>">
<link rel="stylesheet" media="print" href="<html:rewrite page='/resources/styles/print.css'/>" type="text/css" />
<link type="text/css" rel="stylesheet" href="/resources/commerce/css/cart_all.css"/>
<link href="<html:rewrite page="/resources/commerce/css/rightslink_font.css"/>" rel="stylesheet" type="text/css" />
<!--[if lt IE 8]><link rel="stylesheet" type="text/css" href="/resources/commerce/css/css/cart_ie.css"/><![endif]-->


<STYLE type="text/css">

#ecom-content-wrapper{
padding:0px;
}

h3{
text-transform:none;
}
#bottomcorners{
background:none;
}

@media print {
    #inbox07 { display:none; }
    #inbox08 { display:none; }
    #inbox09 { display:none; }
    #edCart  { display:none; }
    #startNew  { display:none; }
}

</STYLE>

<script type="text/javascript" src="<html:rewrite page='/resources/commerce/js/util.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page="/resources/commerce/js/paymentMethod.js"/>" ></script>
<script type="text/javascript" src="<html:rewrite page="/resources/commerce/js/contentLoader.js"/>" ></script>
<script type="text/javascript" src="<html:rewrite page="/resources/commerce/js/eventLogger.js"/>" ></script>
<script type="text/javascript" src="<html:rewrite page="/resources/commerce/js/help.js"/>" ></script>
<style>



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
  
</script>

<!-- Begin Main Content -->

<!-- Begin Progress -->
<div id="ecom-progress"><html:img src="/media/images/ecomprogress-payment.gif" alt="Payment" width="60" height="24" /><html:img src="/media/images/ecomprogress-review.gif" alt="Review" width="52" height="24" /><html:img src="/media/images/ecomprogress-confirmation-on.gif" alt="Confirmation" width="92" height="24" /></div>

<!-- Begin Left Content -->

<div >
<div class="holder">
<div id="main">
<div class="floatleft">
	<h1>Step 3: Order Confirmation </h1>
		<div id="noPrint">    
		<a href="search.do" class="btn-yellow2 pad001">Start new search</a>
		<a href="orderHistory.do" class="btn-yellow2 pad001">View your Order History</a>
		</div>
</div>

<bean:define id="citationTitle">What's citation information?</bean:define>
<bean:define id="skipItemForDebug">false</bean:define>
<bean:define id="processType">CONFIRM</bean:define>
<div id="noPrint">
<div class="floatright">
<html:img src="/media/images/printer.gif" width="23" height="18" /><a href="printCoiConfirmPurchase.do?operation=defaultOperation&confirmNum=<bean:write name="reviewSubmitCartActionFormCOI" property='confirmNumber'/>&showTCCitation=TRUE" target="_blank">Print order information:<br/>includes order confirmation,<br/>terms and conditions, and <br/>citation information</a><br>
<!-- a href="reviewCoiTermsConfirm.do?confirmNum=<bean:write name="reviewSubmitCartActionFormCOI" property='confirmNumber'/>" target="_blank"> Print terms &amp; conditions</a><br-->
<!-- a href="showCoiCitation.do?confirmNum=<bean:write name="reviewSubmitCartActionFormCOI" property='confirmNumber'/>&showCitation=TRUE" target="_blank">Print citation information</a--> 
<util:contextualHelp titleName="citationTitle" bodyName="reviewSubmitCartActionFormCOI" bodyProperty="citationText" rollover="false"><u>(What's this?)</u></util:contextualHelp>
</div>
</div>
<div class="clearer"></div>
<div class="calloutbox" style="margin-top:4px;"><strong>Thank you for your order!</strong> A confirmation for your order will be sent to your account email address. If you have questions about your order, you can call us at +1.877.239.3415 Toll Free, M-F between 3:00 AM and 6:00 PM (Eastern), or write to us at <a href="mailto:info@copyright.com">info@copyright.com</a>. This is not an invoice.</div>
	<div class="clearer"></div>  

<!-- *********************************************************** -->

<html:hidden name="reviewSubmitCartActionFormCOI" property="paymentType"/>   
<div class="horiz-rule"></div>
<div class="floatleft">
	<h2><span class="bold">Confirmation Number: </span> 
    <bean:write name="reviewSubmitCartActionFormCOI" property="confirmNumber" /><br>
	<span class="bold">Order Date: </span><bean:write name="reviewSubmitCartActionFormCOI" property="orderDate" /></h2></div>
    <div class="calloutboxnobackground floatright" style="clear:none; width: 400px;">If you paid by credit card, your order will be finalized and your card will be charged within 24 hours. If you choose to be invoiced, you can change or cancel your order until the invoice is generated. </div>



<div class="clearer"></div>
	<h2>Payment Information</h2>
	<div class="item-details">
	<p class="indent-1">
        
          <bean:write name="reviewSubmitCartActionFormCOI" property="userName" /> <br/>
          
          <logic:notEmpty name="reviewSubmitCartActionFormCOI" property="company">
            <bean:write name="reviewSubmitCartActionFormCOI" property="company" /> <br/>
          </logic:notEmpty>
          
          <logic:notEmpty name="reviewSubmitCartActionFormCOI" property="email">
            <bean:write name="reviewSubmitCartActionFormCOI" property="email" /> <br/>
          </logic:notEmpty>
          
          <logic:notEmpty name="reviewSubmitCartActionFormCOI" property="phone">
            <bean:write name="reviewSubmitCartActionFormCOI" property="phone" /> <br/>
          </logic:notEmpty>
          
         Payment Method:
          <!-- Invoice <br /> -->
          <logic:equal name="reviewSubmitCartActionFormCOI" property="paymentType" value="invoice">
          <bean:write name="reviewSubmitCartActionFormCOI" property="paymentType" /> <br/>
          
          <logic:notEmpty name="reviewSubmitCartActionFormCOI" property="purchaseOrderNumber">
          PO#: <bean:write name="reviewSubmitCartActionFormCOI" property="purchaseOrderNumber" /> <br/>
          </logic:notEmpty>
          
          </logic:equal>
          
          <logic:equal name="reviewSubmitCartActionFormCOI" property="paymentType" value="n/a">
          <bean:write name="reviewSubmitCartActionFormCOI" property="paymentType" /> <br/>
          
          <logic:notEmpty name="reviewSubmitCartActionFormCOI" property="purchaseOrderNumber">
          PO#: <bean:write name="reviewSubmitCartActionFormCOI" property="purchaseOrderNumber" /> <br/>
          </logic:notEmpty>
          
          </logic:equal>
          
          <logic:equal name="reviewSubmitCartActionFormCOI" property="paymentType" value="credit-card">
          <bean:write name="reviewSubmitCartActionFormCOI" property="creditCardDisplay" /> <br/>
          </logic:equal>
         
         <logic:notEmpty name="reviewSubmitCartActionFormCOI" property="promoCode"> 
         Promotion Code: <bean:write name="reviewSubmitCartActionFormCOI" property="promoCode" />
         </logic:notEmpty>
                         
          </p>
      
  <logic:equal name="reviewSubmitCartActionFormCOI" property="paymentType" value="invoice">
    <logic:equal name="reviewSubmitCartActionFormCOI" property="teleSalesUp" value="true"> 
    	<logic:notEmpty name="reviewSubmitCartActionFormCOI" property="address1">   
        <p>
        
        <b>Billing address:</b><br />
        <bean:write name="reviewSubmitCartActionFormCOI" property="company" /> <br/>
        <bean:write name="reviewSubmitCartActionFormCOI" property="address1" /> <br/>
        <logic:notEmpty name="reviewSubmitCartActionFormCOI" property="address2">
            <bean:write name="reviewSubmitCartActionFormCOI" property="address2" /> <br/>
        </logic:notEmpty>
        <bean:write name="reviewSubmitCartActionFormCOI" property="city" />, 
        <bean:write name="reviewSubmitCartActionFormCOI" property="state" /> 
        <bean:write name="reviewSubmitCartActionFormCOI" property="zip" /> <br/>
        <logic:notEmpty name="reviewSubmitCartActionFormCOI" property="country">
            <bean:write name="reviewSubmitCartActionFormCOI" property="country" /> <br/>
        </logic:notEmpty>
        </p>
        </logic:notEmpty>
    </logic:equal>   
  </logic:equal>
  
  <logic:equal name="reviewSubmitCartActionFormCOI" property="paymentType" value="credit-card">
    <logic:equal name="reviewSubmitCartActionFormCOI" property="hasSpecialOrders" value="true">
    <p>
                
        <b>Special order invoices will be sent to:</b><br/>
        <bean:write name="reviewSubmitCartActionFormCOI" property="company" /> <br/>
        <bean:write name="reviewSubmitCartActionFormCOI" property="address1" /> <br/>
        <logic:notEmpty name="reviewSubmitCartActionFormCOI" property="address2">
            <bean:write name="reviewSubmitCartActionFormCOI" property="address2" /> <br/>
        </logic:notEmpty>
        <bean:write name="reviewSubmitCartActionFormCOI" property="city" />, 
        <bean:write name="reviewSubmitCartActionFormCOI" property="state" /> 
        <bean:write name="reviewSubmitCartActionFormCOI" property="zip" /> <br/>
        <logic:notEmpty name="reviewSubmitCartActionFormCOI" property="country">
            <bean:write name="reviewSubmitCartActionFormCOI" property="country" /> <br/>
        </logic:notEmpty>
    </p>
    
    </logic:equal>        
    </logic:equal>
  
</div>

<div class="horiz-rule"></div>
<!--  -->
<!-- ************************************************************************ -->

 <logic:greaterThan value="0" name="reviewSubmitCartActionFormCOI" property="numberOfCartItems">
        
            <!-- Callout: Course Information -->
            <p class="largertype bold" style="height: 10px;">Order Details &nbsp;</p>
            
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
							<strong>Course: <bean:write name="reviewSubmitCartActionFormCOI" property="courseDetails.courseName" /></strong>
						</div>
							<div class="parameter-list">
							<dl>
							  <dt>University/Institution: </dt><dd>
							  <logic:empty name="reviewSubmitCartActionFormCOI" property="courseDetails.schoolCollege">&nbsp;</logic:empty>
							  <bean:write name="reviewSubmitCartActionFormCOI" property="courseDetails.schoolCollege" />
							  </dd>
                        	 <dt>Start of term: </dt><dd>
							  <logic:empty name="reviewSubmitCartActionFormCOI" property="courseDetails.startOfTermDate">&nbsp;</logic:empty>
                        	 <bean:write name="reviewSubmitCartActionFormCOI" property="courseDetails.startOfTermDate" format="MM/dd/yyyy"/>
                        	 </dd>
                             <dt>Course number: </dt><dd>
							  <logic:empty name="reviewSubmitCartActionFormCOI" property="courseDetails.courseNumber">&nbsp;</logic:empty>
                             <bean:write name="reviewSubmitCartActionFormCOI" property="courseDetails.courseNumber" /></dd>
                             <dt>Number of students: </dt><dd>
							  <logic:empty name="reviewSubmitCartActionFormCOI" property="courseDetails.numberOfStudents">&nbsp;</logic:empty>
                             <bean:write name="reviewSubmitCartActionFormCOI" property="courseDetails.numberOfStudents" /></dd>
							</dl>
							<dl>
							<dt>Instructor: </dt><dd><logic:empty name="reviewSubmitCartActionFormCOI" property="courseDetails.instructor">&nbsp;</logic:empty>
							<bean:write name="reviewSubmitCartActionFormCOI" property="courseDetails.instructor" /></dd>
                        <dt>Your reference: </dt><dd>
							  <logic:empty name="reviewSubmitCartActionFormCOI" property="courseDetails.reference">&nbsp;</logic:empty>
                        <bean:write name="reviewSubmitCartActionFormCOI" property="courseDetails.reference" /></dd>
                        <dt>Accounting reference: </dt><dd>
							  <logic:empty name="reviewSubmitCartActionFormCOI" property="courseDetails.accountingReference">&nbsp;</logic:empty>
							  <bean:write name="reviewSubmitCartActionFormCOI" property="courseDetails.accountingReference" /></dd>
                        <dt>Order entered by: </dt><dd>
							  <logic:empty name="reviewSubmitCartActionFormCOI" property="courseDetails.orderEnteredBy">&nbsp;</logic:empty>
							  <bean:write name="reviewSubmitCartActionFormCOI" property="courseDetails.orderEnteredBy" />
                        			</dd>
								
							</dl>
						</div>
						
						
						</div>
                </logic:equal>
            </logic:notEmpty>
          
        
            <!-- End Callout: CourseInformation -->
            
        
            <!-- Cart Items -->
            
            <!-- *************** BEGIN ACADEMIC Order Items *********************** -->
            <logic:notEmpty name="reviewSubmitCartActionFormCOI" property="academicCartItems">
	              <bean:define id="academicRegularBoxStyle">border-top:0px;</bean:define>
	            <logic:iterate name="reviewSubmitCartActionFormCOI" property="cartItems" id="item" indexId="index">
	            <logic:equal name="item" property="academic" value="true"> 
	            <logic:equal name="item" property="specialOrder" value="false"> <!-- Filter Out Special Order -->
	            <div class="product-box" style="<%=academicRegularBoxStyle%>">
	                <%@ include file = "/WEB-INF/jsp-modules/cart/coi/tiles/confirmAcademicItems.jsp" %>
	                </div>	
	                 <META name="WT.z_issn" content="<bean:write name="item" property="standardNumber" />">
	                  <bean:define id="academicRegularBoxStyle">border-top:1px solid #CCCCCC;</bean:define> 
	                </logic:equal> <!--Filter Out Special Orders -->
	               </logic:equal> 
	            </logic:iterate>
	            <!-- *************** END ACADEMIC Regular Order Items *********************** -->
	            
	            <!-- *************** BEGIN ACADEMIC Special Order Items *********************** -->
	            <logic:equal value="true" name="reviewSubmitCartActionFormCOI" property="containsAcademicSpecialOrders">
	                <div class="choise-block" style="padding: 4px 0px 0px;"> 
						<div class="tilte" style="padding-left: 4px; margin: 0px;">
							<strong>Special Orders</strong><html:link page="http://support.copyright.com/index.php?action=category&id=21" target="_blank">Learn more about how Special Orders work</html:link>
						</div>         
				<bean:define id="academicSpecialBoxStyle">border-top:0px;</bean:define>		             
	            <logic:iterate name="reviewSubmitCartActionFormCOI" property="cartItems" id="item" indexId="index">
	            <logic:equal name="item" property="academic" value="true">
	            <logic:equal name="item" property="specialOrder" value="true">  <!-- Filter in Special Order -->
	            <div class="product-box" style="width:688px;<%=academicSpecialBoxStyle%>">
	             <%@ include file = "/WEB-INF/jsp-modules/cart/coi/tiles/confirmAcademicItems.jsp" %>
	             </div>	
	             <br/>
	               <META name="WT.z_issn" content="<bean:write name="item" property="standardNumber" />"> 
	               <bean:define id="academicSpecialBoxStyle">border-top:1px solid #CCCCCC;</bean:define>
	                </logic:equal> <!--Filter Out Special Orders -->
	                </logic:equal>
	            </logic:iterate>
	                </div>        
	            </logic:equal>
	    
            <!-- *************** END Special Order Items *********************** -->
            </logic:notEmpty>
 			<logic:equal value="true" name="reviewSubmitCartActionFormCOI" property="showSectionHeaders">
                     <div class="total" style="border-top:1px solid #CCCCCC;">
						<strong class="number" style="color:#666666">Course items: <span><bean:write name="reviewSubmitCartActionFormCOI" property="numberOfAcademicCartItems" /></span></strong>
						<logic:equal name="reviewSubmitCartActionFormCOI" property="hasOnlyNonPricedAcademicOrders" value="false">
                 				<strong class="total-price" style="color:#666666">Course total: <span><bean:write name="reviewSubmitCartActionFormCOI" property="academicCartTotal" /></span>
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
            
<!-- ************************************************ Added Code********************************************************* -->  

            <!-- *************** BEGIN Regular Order Items *********************** -->
		<bean:define id="nonAcademicRegularBoxStyle">border-top:0px;</bean:define>
        <logic:iterate name="reviewSubmitCartActionFormCOI" property="cartItems" id="item" indexId="index">
	            <logic:equal name="item" property="academic" value="false"> <!-- Non Academic Orders -->
	              <logic:equal name="item" property="specialOrder" value="false"> <!-- Filter Out Special Order -->
	              <div class="product-box"  style="<%=nonAcademicRegularBoxStyle%>">
	  					<%@ include file = "/WEB-INF/jsp-modules/cart/coi/tiles/confirmNonAcademicItems.jsp" %>
	  					</div>
	  				<META name="WT.z_issn" content="<bean:write name="item" property="standardNumber" />">
	  				<bean:define id="nonAcademicRegularBoxStyle">border-top:1px solid #CCCCCC;</bean:define>		
	                </logic:equal> <!--Filter Out Special Orders -->
	             </logic:equal>                
	        </logic:iterate>
            
             <!-- *************** END Regular Order Items *********************** -->
            
            <!-- *************** BEGIN Special Order Items *********************** -->
            
            
              <!-- *************** BEGIN Non Academic Non REPRINT Special Order Permission Items *********************** -->
            <logic:equal value="true" name="reviewSubmitCartActionFormCOI" property="hasNonReprintSpecialOrders">
           <div class="choise-block" style="padding: 4px 0px 0px;"> 
						<div class="tilte" style="padding-left: 4px; margin: 0px;">
							<strong>Special Orders</strong><html:link page="http://support.copyright.com/index.php?action=category&id=21" target="_blank">Learn more about how Special Orders work</html:link>
						</div>
			<bean:define id="nonAcademicSpecialBoxStyle">border-top:0px;</bean:define>
        <logic:iterate name="reviewSubmitCartActionFormCOI" property="cartItems" id="item" indexId="index">
        <logic:equal name="item" property="academic" value="false"> <!-- Non Academic Orders -->
             <logic:equal name="item" property="specialOrder" value="true">  <!-- Filter Out Special Order -->
                <logic:equal name="item" property="reprint" value="false">  <!-- Filter Out Non  Special Order -->
	             <div class="product-box" style="width:688px;<%=nonAcademicSpecialBoxStyle%>">   
								<%@ include file = "/WEB-INF/jsp-modules/cart/coi/tiles/confirmNonAcademicItems.jsp" %>
                 </div>
                 <br/>
                 <bean:define id="nonAcademicSpecialBoxStyle">border-top:1px solid #CCCCCC;</bean:define>
                </logic:equal> <!--Filter Out Reprints -->
                </logic:equal> <!--Filter Out Special Orders -->
			</logic:equal>
		</logic:iterate>
                </div>   
            </logic:equal>
            <!-- *************** END NON ACADEMIC NON REPRINT  Special Order Items *********************** -->
            
            
                 <!-- *************** BEGIN Non Academic  REPRINT Special Order Permission Items *********************** -->
            <logic:equal value="true" name="reviewSubmitCartActionFormCOI" property="hasReprintSpecialOrders">
            <div class="choise-block" style="padding: 4px 0px 0px;"> 
						<div class="tilte" style="padding-left: 4px; margin: 0px;">
							<strong>Reprints</strong>
						</div>                               
            <bean:define id="rePrintBoxStyle">border-top:0px;</bean:define>
           <logic:iterate name="reviewSubmitCartActionFormCOI" property="cartItems" id="item" indexId="index">
        		<logic:equal name="item" property="academic" value="false"> <!-- Non Academic Orders -->
               <logic:equal name="item" property="specialOrder" value="true">  <!-- Filter Out Special Order -->
                <logic:equal name="item" property="reprint" value="true">  <!-- Filter Out Non  Special Order -->
                <div class="product-box" style="width:688px;<%=rePrintBoxStyle%>">
                <%@ include file = "/WEB-INF/jsp-modules/cart/coi/tiles/confirmNonAcademicItems.jsp" %>
                  </div>
                  <br/>
                  <bean:define id="rePrintBoxStyle">border-top:1px solid #CCCCCC;</bean:define>
                </logic:equal> <!--Filter Out Reprints -->
                </logic:equal> <!--Filter Out Special Orders -->
                </logic:equal>
                                                                       
            </logic:iterate>
                </div>   
            </logic:equal>
            <!-- *************** END  REPRINT  Special Order Items *********************** -->

<!-- ***********************************************End Added Code ******************************************************* -->   
 </logic:greaterThan>
		<logic:equal value="true" name="reviewSubmitCartActionFormCOI" property="showSectionHeaders">
          			<div class="total" style="border-top:1px solid #CCCCCC;">
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
<div style="position:relative; right:300px;">This is not an invoice.</div>
<div class="floatleft">
<h2 style="margin-top:0px">Total order items:&nbsp;	<label class="largetype"><bean:write name="reviewSubmitCartActionFormCOI" property="numberOfCartItems" /></label>	
                		</h2>

</div>
  <div class="floatright largertype bold" style="font-size:16px;margin-top:0px;">
    <logic:equal name="reviewSubmitCartActionFormCOI" property="hasOnlyNonPricedOrders" value="false">
  		<logic:equal name="reviewSubmitCartActionFormCOI" property="fundingCurrencyType" value="USD">
    		Order Total: <bean:write name="reviewSubmitCartActionFormCOI" property="cartTotalWithNoDollarSign" /> USD<br />
			</logic:equal>
  		<logic:equal name="reviewSubmitCartActionFormCOI" property="fundingCurrencyType" value="EUR">
				<logic:equal name="reviewSubmitCartActionFormCOI" property="displayChargeTotal" value="true">
				Order Total: <bean:write name="reviewSubmitCartActionFormCOI" property="cartTotalWithNoDollarSign" /> USD<br />
				Charge Total:<bean:write name="reviewSubmitCartActionFormCOI" property="fundingCurrencyAmount" /> EUR<br />
    					 (<bean:write name="reviewSubmitCartActionFormCOI" property="cartChargeTotalWithNoDollarSign" /> USD)<br />
			  </logic:equal>
				<logic:equal name="reviewSubmitCartActionFormCOI" property="displayChargeTotal" value="false">
				Order Total:  <bean:write name="reviewSubmitCartActionFormCOI" property="fundingCurrencyAmount" /> EUR<br />
    					 (<bean:write name="reviewSubmitCartActionFormCOI" property="cartTotalWithNoDollarSign" /> USD)<br />
				</logic:equal>
  		</logic:equal>

  		<logic:equal name="reviewSubmitCartActionFormCOI" property="fundingCurrencyType" value="GBP">
			<logic:equal name="reviewSubmitCartActionFormCOI" property="displayChargeTotal" value="true">
				Order Total: <bean:write name="reviewSubmitCartActionFormCOI" property="cartTotalWithNoDollarSign" /> USD<br />
				Charge Total: <bean:write name="reviewSubmitCartActionFormCOI" property="fundingCurrencyAmount" /> GBP<br />
    					 (<bean:write name="reviewSubmitCartActionFormCOI" property="cartChargeTotalWithNoDollarSign" /> USD)<br />
			</logic:equal>
				<logic:equal name="reviewSubmitCartActionFormCOI" property="displayChargeTotal" value="false">
				Order Total:  <bean:write name="reviewSubmitCartActionFormCOI" property="fundingCurrencyAmount" /> GBP<br />
    					 (<bean:write name="reviewSubmitCartActionFormCOI" property="cartTotalWithNoDollarSign" /> USD)<br />
				</logic:equal>
  		</logic:equal>

  		<logic:equal name="reviewSubmitCartActionFormCOI" property="fundingCurrencyType" value="JPY">
				<logic:equal name="reviewSubmitCartActionFormCOI" property="displayChargeTotal" value="true">
				Order Total: <bean:write name="reviewSubmitCartActionFormCOI" property="cartTotalWithNoDollarSign" /> USD<br />
				Charge Total: <bean:write name="reviewSubmitCartActionFormCOI" property="fundingCurrencyAmount" /> JPY<br />
    					 (<bean:write name="reviewSubmitCartActionFormCOI" property="cartChargeTotalWithNoDollarSign" /> USD)<br />
			  </logic:equal>
				<logic:equal name="reviewSubmitCartActionFormCOI" property="displayChargeTotal" value="false">
				Order Total:  <bean:write name="reviewSubmitCartActionFormCOI" property="fundingCurrencyAmount" /> JPY<br />
    					 (<bean:write name="reviewSubmitCartActionFormCOI" property="cartTotalWithNoDollarSign" /> USD)<br />
				</logic:equal>
  		</logic:equal>
  		<logic:equal name="reviewSubmitCartActionFormCOI" property="fundingCurrencyType" value="CAD">
			  <logic:equal name="reviewSubmitCartActionFormCOI" property="displayChargeTotal" value="true">
				Order Total: <bean:write name="reviewSubmitCartActionFormCOI" property="cartTotalWithNoDollarSign" /> USD<br />
				Charge Total: <bean:write name="reviewSubmitCartActionFormCOI" property="fundingCurrencyAmount" /> CAD<br />
    					 (<bean:write name="reviewSubmitCartActionFormCOI" property="cartChargeTotalWithNoDollarSign" /> USD)<br />
			  </logic:equal>
				<logic:equal name="reviewSubmitCartActionFormCOI" property="displayChargeTotal" value="false">
				Order Total:  <bean:write name="reviewSubmitCartActionFormCOI" property="fundingCurrencyAmount" /> CAD<br />
    					 (<bean:write name="reviewSubmitCartActionFormCOI" property="cartTotalWithNoDollarSign" /> USD)<br />
				</logic:equal>
  		</logic:equal>
      <logic:equal name="reviewSubmitCartActionFormCOI" property="fundingCurrencyType" value="CHF">
        <logic:equal name="reviewSubmitCartActionFormCOI" property="displayChargeTotal" value="true">
        Order Total: <bean:write name="reviewSubmitCartActionFormCOI" property="cartTotalWithNoDollarSign" /> USD<br />
        Charge Total:<bean:write name="reviewSubmitCartActionFormCOI" property="fundingCurrencyAmount" /> CHF<br />
               (<bean:write name="reviewSubmitCartActionFormCOI" property="cartChargeTotalWithNoDollarSign" /> USD)<br />
        </logic:equal>
        <logic:equal name="reviewSubmitCartActionFormCOI" property="displayChargeTotal" value="false">
        Order Total:  <bean:write name="reviewSubmitCartActionFormCOI" property="fundingCurrencyAmount" /> CHF<br />
               (<bean:write name="reviewSubmitCartActionFormCOI" property="cartTotalWithNoDollarSign" /> USD)<br />
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
	<div class="clearer"></div>	
</div>
</div>




<div class="clearer"></div>
</div><!--  class="holder">-->
 </div><!-- id="main">-->

</div>
<div class="clearer"></div>
<!-- Webtrends tags for capturing scenarios -->
<META name="WT.si_n" content="Checkout">
<META name="WT.si_x" content="8">
<!-- Webtrends tags for capturing revenue -->
<META name="WT.tx_s" content="<bean:write name="reviewSubmitCartActionFormCOI" property="cartTotalWT" />">
<META name="WT.pn_sku" content='<bean:write name="reviewSubmitCartActionFormCOI" property="idnoWT" />'>
<META name="WT.tx_u" content="<bean:write name="reviewSubmitCartActionFormCOI" property="numberOfCartItems" />">
<META name="WT.tx_e" content="p">
<META name="WT.tx_i" content="<bean:write name="reviewSubmitCartActionFormCOI" property="confirmNumber" />">
<META name="WT.tx_t" content="<bean:write name="reviewSubmitCartActionFormCOI" property="paymentType" />">
<META name="WT.z_order_placed" content="1">
<!-- end Webtrends tags -->


<!-- End Main Content -->
<script type="text/javascript">

  window.onload = initPage;

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
<script>
function toggleDetails(link,divBlock){
	var liBlock='li_'+divBlock;
	//alert('Block: ' + divBlock);
	if($('#'+divBlock).css('display')=='none'){
		$('#'+liBlock).removeClass("collapseDetails");
		$('#'+liBlock).addClass("expandDetails");
		$(link).text('Hide details');
	}else{
		$('#'+liBlock).removeClass("expandDetails");
		$('#'+liBlock).addClass("collapseDetails");
		$(link).text('View details');
	}
			$('#'+divBlock).toggle();
			
}
</script>

<!-- Start Google Code for Copyright Sale Conversion Page -->
<script type="text/javascript">
/* <![CDATA[ */
var google_conversion_id = 1064532594;
var google_conversion_language = "en";
var google_conversion_format = "3";
var google_conversion_color = "ffffff";
var google_conversion_label = "za61CLa70gEQ8vTN-wM";
var google_conversion_value = 0;
if (1) {
  google_conversion_value = <bean:write name="reviewSubmitCartActionFormCOI" property="cartTotalWithNoDollarSign" />;
}
/* ]]> */
</script>
<script type="text/javascript" src="https://www.googleadservices.com/pagead/conversion.js">
</script>
<noscript>
<div style="display:inline;">
<img height="1" width="1" style="border-style:none;" alt="" src="https://www.googleadservices.com/pagead/conversion/1064532594/?value=<bean:write name="reviewSubmitCartActionFormCOI" property="cartTotalWithNoDollarSign" />&amp;label=za61CLa70gEQ8vTN-wM&amp;guid=ON&amp;script=0"/>
</div>
</noscript>
<!-- End Google Code for Copyright Sale Conversion Page -->
