<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/cc2-util.tld" prefix="util"%>

<%@ page errorPage="/jspError.do" %>
<%@ page import="com.copyright.ccc.web.transaction.RepublicationConstants" %>

<script src="<html:rewrite page="/resources/commerce/js/contextualHelp.js"/>" type="text/javascript"></script>
<link href="<html:rewrite page="/resources/commerce/css/contextualHelp.css"/>" rel="stylesheet" type="text/css" />



<script type="text/javascript" src="<html:rewrite page='/resources/commerce/js/util.js'/>"></script>

<link rel="stylesheet" type="text/css" href="<html:rewrite page='/resources/commerce/css/ccc-new.css'/>">
<link type="text/css" rel="stylesheet" href="/resources/commerce/css/cart_all.css"/>
<!--[if lt IE 8]><link rel="stylesheet" type="text/css" href="/resources/commerce/css/css/cart_ie.css"/><![endif]-->

<link rel="stylesheet" media="print" href="<html:rewrite page='/resources/styles/print.css'/>" type="text/css" />
<link href="<html:rewrite page="/resources/commerce/css/rightslink_font.css"/>" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<html:rewrite page="/resources/commerce/js/paymentMethod.js"/>" ></script>
<script type="text/javascript" src="<html:rewrite page="/resources/commerce/js/contentLoader.js"/>" ></script>
<script type="text/javascript" src="<html:rewrite page="/resources/commerce/js/eventLogger.js"/>" ></script>
<script type="text/javascript" src="<html:rewrite page="/resources/commerce/js/help.js"/>" ></script>
<script type="text/javascript">
if (browserInfo.isIE){
                document.write ("<LINK REL='stylesheet' type='text/css' href='/resources/commerce/css/rightslink_layout_msie.css'>");
}
else{
                document.write ("<LINK REL='stylesheet' type='text/css' href='/resources/commerce/css/rightslink_layout_ns6.css'>");
}
</script>
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
<!-- Begin Main Content -->

<!-- Begin Progress -->
<div id="ecom-progress"><html:img src="/media/images/ecomprogress-payment.gif" alt="Payment" width="60" height="24" /><html:img src="/media/images/ecomprogress-review.gif" alt="Review" width="52" height="24" /><html:img src="/media/images/ecomprogress-confirmation-on.gif" alt="Confirmation" width="92" height="24" /></div>

<!-- Begin Left Content -->

<div>
<div class="holder">
<div id="main">

<div class="floatleft">
	<h1>Step 3: Order Confirmation </h1>
</div>

<div class="noPrint floatright">
<html:img src="/media/images/printer.gif" width="23" height="18" /><a href="javascript:window.print()">Print order information: <br/>includes order confirmation, <br/>terms and conditions, and <br/>citation information</a><br>
</div>

<div class="clearer"></div>

    
<div class="calloutbox"><strong>Thank you for your order!</strong> A confirmation for your order will be sent to your account email address. If you have questions about your order, you can call us at +1.877.239.3415 Toll Free, M-F between 3:00 AM and 6:00 PM (Eastern), or write to us at <a href="mailto:info@copyright.com">info@copyright.com</a>. This is not an invoice.</div>
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
          
          <logic:notEmpty name="reviewSubmitCartActionFormCOI" property="purchaseOrderNumber">
          PO#: <bean:write name="reviewSubmitCartActionFormCOI" property="purchaseOrderNumber" /> <br />
          </logic:notEmpty>
          
          </logic:equal>
          
          <logic:equal name="reviewSubmitCartActionFormCOI" property="paymentType" value="n/a">
          <bean:write name="reviewSubmitCartActionFormCOI" property="paymentType" /> <br />
          
          <logic:notEmpty name="reviewSubmitCartActionFormCOI" property="purchaseOrderNumber">
          PO#: <bean:write name="reviewSubmitCartActionFormCOI" property="purchaseOrderNumber" /> <br />
          </logic:notEmpty>
          
          </logic:equal>
          
          <logic:equal name="reviewSubmitCartActionFormCOI" property="paymentType" value="credit-card">
          <bean:write name="reviewSubmitCartActionFormCOI" property="creditCardDisplay" /> <br />
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
        </logic:notEmpty>
    </logic:equal>   
  </logic:equal>
  
  <logic:equal name="reviewSubmitCartActionFormCOI" property="paymentType" value="credit-card">
    <logic:equal name="reviewSubmitCartActionFormCOI" property="hasSpecialOrders" value="true">
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
<!--  -->
<!-- ************************************************************************ -->
<bean:define id="skipItemForDebug">false</bean:define>
<bean:define id="processType">PRINT</bean:define>



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
							<ul>
								<li><a href="/editCartCoiCourseDetails.do?operation=defaultOperation" Id="998">Edit course</a></li>
								
							</ul>
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
	            <logic:equal name="item" property="academic" value="true"> <!-- Academic Orders -->
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
							<strong>Special Orders</strong>
						</div>    
					<bean:define id="academicSpecialBoxStyle">border-top:0px;</bean:define>                          
	            <logic:iterate name="reviewSubmitCartActionFormCOI" property="cartItems" id="item" indexId="index">
       				 <logic:equal name="item" property="academic" value="true"> <!--  Academic Orders -->
            	 <logic:equal name="item" property="specialOrder" value="true">  <!--Special Order -->
	             <div class="product-box" style="width:688px;<%=academicSpecialBoxStyle%>">   
								<%@ include file = "/WEB-INF/jsp-modules/cart/coi/tiles/confirmAcademicItems.jsp" %>
                 </div>
                 <br/>
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
	              <div class="product-box" style="<%=nonAcademicRegularBoxStyle%>">
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
							<strong>Special Orders</strong>
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
  <logic:equal name="reviewSubmitCartActionFormCOI" property="hasOnlySpecialOrders" value="false">
  		<logic:equal name="reviewSubmitCartActionFormCOI" property="fundingCurrencyType" value="USD">
    		Order Total: <bean:write name="reviewSubmitCartActionFormCOI" property="cartTotal" /><br />
					</logic:equal>
  		<logic:equal name="reviewSubmitCartActionFormCOI" property="fundingCurrencyType" value="EUR">
						<logic:equal name="reviewSubmitCartActionFormCOI" property="displayChargeTotal" value="true">
				Order Total: <bean:write name="reviewSubmitCartActionFormCOI" property="cartTotal" /><br />
				Charge Total: &#8364; <bean:write name="reviewSubmitCartActionFormCOI" property="fundingCurrencyAmount" /><br />
    					 (USD<bean:write name="reviewSubmitCartActionFormCOI" property="cartChargeTotal" />)<br />
			</logic:equal>
				<logic:equal name="reviewSubmitCartActionFormCOI" property="displayChargeTotal" value="false">
				Order Total:  &#8364; <bean:write name="reviewSubmitCartActionFormCOI" property="fundingCurrencyAmount" /><br />
    					 (USD<bean:write name="reviewSubmitCartActionFormCOI" property="cartTotal" />)<br />
				</logic:equal>

  		</logic:equal>
  		<logic:equal name="reviewSubmitCartActionFormCOI" property="fundingCurrencyType" value="GBP">
			<logic:equal name="reviewSubmitCartActionFormCOI" property="displayChargeTotal" value="true">
				Order Total: <bean:write name="reviewSubmitCartActionFormCOI" property="cartTotal" /><br />
				Charge Total: &#163; <bean:write name="reviewSubmitCartActionFormCOI" property="fundingCurrencyAmount" /><br />
    					 (USD<bean:write name="reviewSubmitCartActionFormCOI" property="cartChargeTotal" />)<br />
			</logic:equal>
				<logic:equal name="reviewSubmitCartActionFormCOI" property="displayChargeTotal" value="false">
				Order Total:  &#163; <bean:write name="reviewSubmitCartActionFormCOI" property="fundingCurrencyAmount" /><br />
    					 (USD<bean:write name="reviewSubmitCartActionFormCOI" property="cartTotal" />)<br />
				</logic:equal>
    		
  		</logic:equal>
  		<logic:equal name="reviewSubmitCartActionFormCOI" property="fundingCurrencyType" value="JPY">
				<logic:equal name="reviewSubmitCartActionFormCOI" property="displayChargeTotal" value="true">
				Order Total: <bean:write name="reviewSubmitCartActionFormCOI" property="cartTotal" /><br />
				Charge Total: &#165; <bean:write name="reviewSubmitCartActionFormCOI" property="fundingCurrencyAmount" /><br />
    					 (USD<bean:write name="reviewSubmitCartActionFormCOI" property="cartChargeTotal" />)<br />
			</logic:equal>
				<logic:equal name="reviewSubmitCartActionFormCOI" property="displayChargeTotal" value="false">
				Order Total:  &#165; <bean:write name="reviewSubmitCartActionFormCOI" property="fundingCurrencyAmount" /><br />
    					 (USD<bean:write name="reviewSubmitCartActionFormCOI" property="cartTotal" />)<br />
				</logic:equal>


  		</logic:equal>
  		<logic:equal name="reviewSubmitCartActionFormCOI" property="fundingCurrencyType" value="CAD">
			  <logic:equal name="reviewSubmitCartActionFormCOI" property="displayChargeTotal" value="true">
				Order Total: <bean:write name="reviewSubmitCartActionFormCOI" property="cartTotal" /><br />
				Charge Total:  CAD$ <bean:write name="reviewSubmitCartActionFormCOI" property="fundingCurrencyAmount" /><br />
    					 (USD<bean:write name="reviewSubmitCartActionFormCOI" property="cartChargeTotal" />)<br />
			  </logic:equal>
				<logic:equal name="reviewSubmitCartActionFormCOI" property="displayChargeTotal" value="false">
				Order Total:   CAD$ <bean:write name="reviewSubmitCartActionFormCOI" property="fundingCurrencyAmount" /><br />
    					 (USD<bean:write name="reviewSubmitCartActionFormCOI" property="cartTotal" />)<br />
				</logic:equal>
  		</logic:equal>
    		
      <logic:equal name="reviewSubmitCartActionFormCOI" property="fundingCurrencyType" value="CHF">
        <logic:equal name="reviewSubmitCartActionFormCOI" property="displayChargeTotal" value="true">
        Order Total: <bean:write name="reviewSubmitCartActionFormCOI" property="cartTotal" /><br />
        Charge Total:  CHF&#8355; <bean:write name="reviewSubmitCartActionFormCOI" property="fundingCurrencyAmount" /><br />
               (USD<bean:write name="reviewSubmitCartActionFormCOI" property="cartChargeTotal" />)<br />
        </logic:equal>
        <logic:equal name="reviewSubmitCartActionFormCOI" property="displayChargeTotal" value="false">
        Order Total:   CHF&#8355; <bean:write name="reviewSubmitCartActionFormCOI" property="fundingCurrencyAmount" /><br />
               (USD<bean:write name="reviewSubmitCartActionFormCOI" property="cartTotal" />)<br />
        </logic:equal>
      </logic:equal>
  </logic:equal>
  <logic:equal name="reviewSubmitCartActionFormCOI" property="hasOnlySpecialOrders" value="true">
	  	<logic:equal name="reviewSubmitCartActionFormCOI" property="containsPricedSpecialOrderItem" value="true">
	    		Order Total: <span class="largetype"><bean:write name="reviewSubmitCartActionFormCOI" property="cartTotal" /></span><br />
	    		<logic:equal name="reviewSubmitCartActionFormCOI" property="paymentType" value="credit-card">
	    		Charge Total: <span class="largetype"><bean:write name="reviewSubmitCartActionFormCOI" property="cartChargeTotal" /></span><br />
	    		</logic:equal>
		</logic:equal>
	    <logic:equal name="reviewSubmitCartActionFormCOI" property="containsPricedSpecialOrderItem" value="false">           			
	                    Order Total: <span class="largetype">$TBD</span><br />
	            		
	        </logic:equal>    			
                			
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
	
</div> <!-- main-->	
</div> <!-- holder-->  

</div>
<!-- End Main Content -->
<p style="page-break-before: always"/> 
<jsp:include page="/WEB-INF/jsp-modules/cart/coi/reviewTermsConfirm.jsp" flush="true"></jsp:include>

<jsp:include page="/WEB-INF/jsp-modules/cart/coi/showCitation.jsp" flush="true"></jsp:include>


<script type="text/javascript">
    function printPage()
    {
        bV = parseInt(navigator.appVersion);
        if (bV >= 4) window.print();
    }
    
    if( typeof addOnLoadEvent == 'function' && typeof printPage == 'function' ) addOnLoadEvent( printPage );
</script>













