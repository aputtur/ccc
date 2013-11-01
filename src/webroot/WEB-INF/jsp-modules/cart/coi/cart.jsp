<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/tld/struts-bean.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/tld/struts-logic.tld" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/cc2-util.tld" %>
<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>

<%@ page errorPage="/jspError.do" %>
<%@ page import="com.copyright.ccc.web.transaction.RepublicationConstants" %>
<%@ page import="com.copyright.ccc.business.services.cart.PurchasablePermission" %>
<%@ page import="com.copyright.ccc.business.data.UpdatedCartItem"%>
<%@ page import="com.copyright.ccc.web.WebConstants"%>

<%@ page import="com.copyright.data.account.Address" %>
<%@ page import="com.copyright.data.account.Country" %>
<%@ page import="java.util.HashMap" %>

<script src="<html:rewrite page="/resources/commerce/js/util.js"/>" type="text/javascript"></script>
<script src="<html:rewrite page="/resources/commerce/js/contextualHelp.js"/>" type="text/javascript"></script>
<link href="<html:rewrite page="/resources/commerce/css/contextualHelp.css"/>" rel="stylesheet" type="text/css" />
<link href="<html:rewrite page="/resources/commerce/css/rightslink_font.css"/>" rel="stylesheet" type="text/css" />
<link type="text/css" rel="stylesheet" href="/resources/commerce/css/cart_all.css"/>
<!--[if lt IE 8]><link rel="stylesheet" type="text/css" href="/resources/commerce/css/css/cart_ie.css"/><![endif]-->
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

div.priceUpdate{
color: red; 
 text-align: center; 
 width: 60px; 
 position: relative; 
 bottom: 20px;
}

</style>
<script type="text/javascript">

    function editAcademicCartItem(index)
    {
        document.getElementById("selectedItemIndex").value = index;
        document.getElementById("selectedItem").value = "academic";
        submitForm(document.getElementById("cartFormCOI"), "editCartItem");
    }
    
    function removeAcademicCartItem(index)
    {
        if( confirm("Are you sure that you want to remove this item from your cart?") )
        {
            document.getElementById("selectedItemIndex").value = index;
            document.getElementById("selectedItem").value = "academic";
            submitForm(document.getElementById("cartFormCOI"), "removeCartItem");
        }
    }

    var clickEditCount=0;
    function editNonAcademicCartItem(index)
    {
        if (clickEditCount==1) {
	        return;
        }
        clickEditCount=1;
        document.getElementById("selectedItemIndex").value = index;
        document.getElementById("selectedItem").value = "nonacademic";
        submitForm(document.getElementById("cartFormCOI"), "editCartItem");
    }
    
    function removeNonAcademicCartItem(index)
    {
        if( confirm("Are you sure that you want to remove this item from your cart?") )
        {
        	document.getElementById("selectedItemIndex").value = index;
            document.getElementById("selectedItem").value = "nonacademic";
            submitForm(document.getElementById("cartFormCOI"), "removeCartItem");
        }
    }
    
    function emptyCart()
    {
        if( confirm("Are you sure you want to remove all of the items from this cart?") )
        {
            submitForm(document.getElementById("cartFormCOI"), "emptyCart");
        }
    }

    function removeCourse()
    {
        if( confirm("Are you sure you want to remove this course and all its items from this cart?") )
        {
            submitForm(document.getElementById("cartFormCOI"), "removeCourse");
        }
    }
    
    function selectPaymentType()
    {
	   document.cartFormCOI.action="/selectCoiPaymentType.do?enforcePwdChg="
	   							+document.cartFormCOI.enforcePwdChg.checked;
	   document.cartFormCOI.submit();
	}
	
	function emulatedUserSendEmail()
    {
	   document.cartFormCOI.action="/emulatedUserSendEmail.do?enforcePwdChg="
	   							+document.cartFormCOI.enforcePwdChg.checked;
	   document.cartFormCOI.submit();
	}
    
</script>

<html:form action="/cart.do" styleId="cartFormCOI">

<bean:define id="updatedPriceCartItems"  type="HashMap" name="cartFormCOI" property="priceUpdatedItem"></bean:define>
    <bean:define id="termsTitle">Rightsholder Terms</bean:define>
    <html:hidden name="cartFormCOI" property="selectedItemIndex" value="" styleId="selectedItemIndex" />
    <html:hidden name="cartFormCOI" property="selectedItem" value="" styleId="selectedItem" />
    <!-- Show price change messages -->
    <%@ include file = "/WEB-INF/jsp-modules/cart/coi/tiles/cartUpdateMessage.jsp" %>
    <!-- Begin Content -->
    <div >

        
        <logic:notEmpty name="cartFormCOI" property="unableToModifyCartErrorDisplayStrings">
            <div style="width:100%" align="center">
                <div class="calloutbox" style="width:80%" align="left">
                    <p>
                        <b>NOTE: </b><bean:message name="cartFormCOI" property="unableToModifyCartErrorHeaderMessageKey" /><br><br>
                        <logic:iterate name="cartFormCOI" property="unableToModifyCartErrorDisplayStrings" id="errorString">
                            <bean:write name="errorString" /><br>
                        </logic:iterate>
                    </p>
                </div>
                <br>
            </div>
        </logic:notEmpty>
        
              
        <logic:equal name="cartFormCOI" property="chkRightsLnkDown" value="true">
        <br/>
            <div style="width:100%" align="center">
                <div class="calloutbox" style="width:80%" align="left">
                
                    <p>
                    <bean:message key="rightslink.rightslnk.down.msg"/>
                    </p>
                </div>
                <br>
            </div>
        </logic:equal>

		<bean:define id="sessionExpiredMsg" value='<%=request.getAttribute(WebConstants.RequestKeys.SESSION_EXPIRED_MSG)==null?"":request.getAttribute(WebConstants.RequestKeys.SESSION_EXPIRED_MSG).toString()%>'/>
		<logic:notEmpty name="sessionExpiredMsg">
			
			<div style="color: red; padding-left: 10px; margin-right: 30%;">
			<p>
				<bean:write name="sessionExpiredMsg"/>
				</p>
			</div>
			
			<br/>
		</logic:notEmpty>
       
<!-- SHOPPING CART STARTS HERE -->

			<div class="holder">
				<div id="main">
					<div class="heading">
						<div class="name-box">
							<div class="title">
                <h4>Shopping Cart</h4>
                <logic:greaterThan value="0" name="cartFormCOI" property="numberOfCartItems">
                  <a href="javascript:emptyCart()" class="empty">Empty this cart</a>
                </logic:greaterThan>
                <logic:notEmpty name="cartFormCOI" property="courseHeader">
                  <logic:lessEqual value="0" name="cartFormCOI" property="numberOfCartItems">
                    <a href="javascript:removeCourse()" class="subtitle" id="997">Remove Course Information</a>
                  </logic:lessEqual>
                </logic:notEmpty>    
              </div>
              <span class="btn-search">
                <logic:greaterThan value="0" name="cartFormCOI" property="numberOfCartItems">
                  <html:link page="/search.do?operation=show&page=last" >
                    <logic:equal name="cartFormCOI" property="academicCart" value="true">
                    Search for another publication
                    </logic:equal>	
                    <logic:notEqual name="cartFormCOI" property="academicCart" value="true">
                    Search for another publication
                    </logic:notEqual>
                  </html:link>
                </logic:greaterThan>
              </span>
              <logic:greaterThan name="cartFormCOI" property="numberOfCartItems" value="0">
                <span class="special-order-text">
                  <p class="icon-alert" style="padding: 0px 20px 0px; font-size:12px; font-weight:normal; color:#000000;vertical-align:middle;">
                  <span style="background-color: #FFFF00">Your order is not complete</span>  until you checkout.
                  </p>
                </span>
              </logic:greaterThan>
						</div>
						<div class="checkout-box">
              <logic:greaterThan value="1" name="cartFormCOI" property="numberOfCartItems">
                <span class="btn-checkout">
                  <html:link action="/selectCoiPaymentType.do">checkout</html:link>
                </span>
                <logic:greaterThan value="1" name="cartFormCOI" property="numberOfCartItems">
                  <logic:equal name="cartFormCOI" property="hasOnlyNonPricedOrders" value="false">
                    <strong><span>Cart total: </span><bean:write name="cartFormCOI" property="cartTotal" />
                      <logic:equal name="cartFormCOI" property="showExcludeTBDItemText" value="true">
                        <br/><div style="text-align:right;font-size:12px;color:#666666;font-weight:normal;">(Excludes $TBD items)</div>
                      </logic:equal>
                    </strong>
                  </logic:equal>
                  <logic:equal name="cartFormCOI" property="hasOnlyNonPricedOrders" value="true">
                    <strong><span>Cart total: </span>$TBD</strong>
                  </logic:equal>
                </logic:greaterThan>
              </logic:greaterThan>
						</div>
					</div>

				<logic:equal value="false" name="cartFormCOI" property="showSectionHeaders">   
              		<h3 class="single"></h3>                	      
          		</logic:equal>
			    <logic:equal value="true" name="cartFormCOI" property="showSectionHeaders">   
              		<h3>Course items</h3>                	      
          		</logic:equal>
          		
          		
          		<logic:notEmpty name="cartFormCOI" property="courseHeader">
            
                <logic:equal name="cartFormCOI" property="academicCart" value="true" >
                	<div class="choise-block">
						<div class="tilte">
							<strong>Course: <bean:write name="cartFormCOI" property="courseHeader.courseName" /></strong>
							<ul>
								<li><a href="/editCartCoiCourseDetails.do?operation=defaultOperation" Id="998">Edit course</a></li>
								<li><a href="javascript:removeCourse()" id="998">Remove Course</a></li>
							</ul>
						</div>
							<div class="parameter-list">
							<dl>
							  <dt>University/Institution: </dt><dd>
							  <logic:empty name="cartFormCOI" property="courseHeader.schoolCollege">&nbsp;</logic:empty>
							  <bean:write name="cartFormCOI" property="courseHeader.schoolCollege" />
							  </dd>
                        	 <dt>Start of term: </dt><dd>
							  <logic:empty name="cartFormCOI" property="courseHeader.startOfTermDate">&nbsp;</logic:empty>
                        	 <bean:write name="cartFormCOI" property="courseHeader.startOfTermDate" format="MM/dd/yyyy"/>
                        	 </dd>
                             <dt>Course number: </dt><dd>
							  <logic:empty name="cartFormCOI" property="courseHeader.courseNumber">&nbsp;</logic:empty>
                             <bean:write name="cartFormCOI" property="courseHeader.courseNumber" /></dd>
                             <dt>Number of students: </dt><dd>
							  <logic:empty name="cartFormCOI" property="courseHeader.numberOfStudents">&nbsp;</logic:empty>
                             <bean:write name="cartFormCOI" property="courseHeader.numberOfStudents" /></dd>
							</dl>
							<dl>
							<dt>Instructor: </dt><dd><logic:empty name="cartFormCOI" property="courseHeader.instructor">&nbsp;</logic:empty>
							<bean:write name="cartFormCOI" property="courseHeader.instructor" /></dd>
                        <dt>Your reference: </dt><dd>
							  <logic:empty name="cartFormCOI" property="courseHeader.reference">&nbsp;</logic:empty>
                        <bean:write name="cartFormCOI" property="courseHeader.reference" /></dd>
                        <dt>Accounting reference: </dt><dd>
							  <logic:empty name="cartFormCOI" property="courseHeader.accountingReference">&nbsp;</logic:empty>
							  <bean:write name="cartFormCOI" property="courseHeader.accountingReference" /></dd>
                        <dt>Order entered by: </dt><dd>
							  <logic:empty name="cartFormCOI" property="courseHeader.orderEnteredBy">&nbsp;</logic:empty>
							  <bean:write name="cartFormCOI" property="courseHeader.orderEnteredBy" />
                        			</dd>
								
							</dl>
						</div>
						
						
						</div>
                
              
                    
                </logic:equal>
            </logic:notEmpty>
          		
          		
          			
	<!-- Shopping cart ends here -->						
            <!-- Begin Display Academic Course Items -->
               
     		   <%@ include file = "/WEB-INF/jsp-modules/cart/coi/tiles/cartAcademicItems.jsp" %>
     
         <logic:equal value="true" name="cartFormCOI" property="showSectionHeaders">  
                      <div class="total" style="border-top:1px solid #CCCCCC;" >
						<strong class="number" style="color:#666666">Course items: <span><bean:write name="cartFormCOI" property="numberOfAcademicCartItems" /></span></strong>
						<logic:equal name="cartFormCOI" property="hasOnlyNonPricedAcademicOrders" value="false">
                 				<strong class="total-price"  style="color:#666666">Course total: <span style="font-size:14px"><bean:write name="cartFormCOI" property="academicCartTotal" /></span>
                 				<logic:equal name="cartFormCOI" property="showExcludeTBDItemAcademicText" value="true">
                        		<br/><div style="text-align:right;font-size:12px;color:#666666;">(Excludes $TBD items)</div>
                    			</logic:equal>
                    			</strong>
                 				</logic:equal>
                 			
                 			<logic:equal name="cartFormCOI" property="hasOnlyNonPricedAcademicOrders" value="true">
                    			<strong class="total-price" style="color:#666666">Course total: <span style="font-size:14px">$TBD</span>
                    				
                    			</strong>
                			</logic:equal>
                			
                 				
                    			
					</div>
     		<h3>Single Items</h3>
            </logic:equal>   
            
            
     	   <!-- Show Non  Academic Cart Items -->    
	        <%@ include file = "/WEB-INF/jsp-modules/cart/coi/tiles/cartNonAcademicItems.jsp" %>
						
						
					<logic:equal value="true" name="cartFormCOI" property="showSectionHeaders">  	
          			<div class="total" style="border-top:1px solid #CCCCCC;" >
						<strong class="number"  style="color:#666666">Single items: <span><bean:write name="cartFormCOI" property="numberOfNonAcademicCartItems" /></span></strong>
						
						
                 			<logic:equal name="cartFormCOI" property="hasOnlyNonPricedNonAcademicOrders" value="false">
                 					<strong class="total-price" style="color:#666666">Single item total: <span><bean:write name="cartFormCOI" property="nonAcademicCartTotal" /></span>
                 					<logic:equal name="cartFormCOI" property="showExcludeTBDItemNonAcademicText" value="true">
                 					<br/><div style="text-align:right;font-size:12px;color:#666666;">(Excludes $TBD items)</div>
                 					</logic:equal>
                 					</strong>
                 				</logic:equal>
                 			
                 			<logic:equal name="cartFormCOI" property="hasOnlyNonPricedNonAcademicOrders" value="true">
                    				<strong class="total-price" style="color:#666666">Single item total: <span>$TBD</span>
                    				</strong>
                			</logic:equal>
                			
					</div>
					</logic:equal>
					<logic:equal value="false" name="cartFormCOI" property="showSectionHeaders">
					
					</logic:equal>
					
					<logic:greaterThan name="cartFormCOI" property="numberOfCartItems" value="0">
            <div class="total-sum">
              <strong class="number"  style="color:#666666">Cart items: <span><bean:write name="cartFormCOI" property="numberOfCartItems" /></span></strong>
              <logic:equal name="cartFormCOI" property="hasOnlyNonPricedOrders" value="false">
                <strong class="total-price"  style="color:#666666">Cart total:
                  <span><bean:write name="cartFormCOI" property="cartTotal" />
                    <logic:equal name="cartFormCOI" property="showExcludeTBDItemText" value="true">
                      <div style="text-align:right;font-size:12px;color:#666666;font-weight:normal;">(Excludes $TBD items)</div>
                    </logic:equal>
                  </span>
                </strong>
              </logic:equal>
              <logic:equal name="cartFormCOI" property="hasOnlyNonPricedOrders" value="true">
                <strong class="total-price" style="color:#666666">Cart total: <span>$TBD</span></strong>
              </logic:equal>
            </div>

            <div class="footing">
              <span class="btn-search">
                <html:link page="/search.do?operation=show&page=last"  styleId="998">
                  <logic:equal name="cartFormCOI" property="academicCart" value="true">
                  Search for another publication
                  </logic:equal>
                  <logic:notEqual name="cartFormCOI" property="academicCart" value="true">
                  Search for another publication
                  </logic:notEqual>
                </html:link>
              </span>
              <font size=2><b>Your order is not complete.</b></font> 
              <html:link action="/selectCoiPaymentType.do" styleId="999"  styleClass="btn-checkout"> checkout</html:link>
            </div>
					</logic:greaterThan>

					 <logic:lessEqual name="cartFormCOI" property="numberOfCartItems" value="0">
            There are no items in your shopping cart.
        </logic:lessEqual>
        
          	<logic:greaterThan name="cartFormCOI" property="numberOfCartItems" value="0">
 
            
            <div class="clearer"></div>

			<logic:equal name="emulating" value="true">
            	<table><tr>
				<td width="65%">
            		 <input type="checkbox" name="enforcePwdChg" checked="checked" />
            		 Enforce Password Change
            	</td>
				<td width="13%">&nbsp;</td>
				<td width="10%">
				<html:link href="javascript:emulatedUserSendEmail()" styleId="999"><html:img src="/media/images/btn-email.gif" alt="Email to User" styleClass="floatleft"/></html:link></td>
				<td width="2%">&nbsp;</td>
            	<td width="10%" align="right"><html:link href="javascript:selectPaymentType()" styleId="999"><html:img src="/media/images/btn_checkout.gif" alt="Checkout" styleClass="floatright"/></html:link>
				</td></tr></table>
            </logic:equal>	

        
        </logic:greaterThan>
        
					<!-- end all main wrap-->
					</div>
			</div>
		</div>

</html:form>

<!-- Webtrends tags for capturing scenarios -->
<bean:parameter id="actionType" name="actionType" value="view" />
<logic:equal name="actionType" value="add">
    <META name="WT.si_n" content="Checkout">
    <META name="WT.si_x" content="4">
    <META name="WT.tx_e" content="a">
    <META name="WT.pn_sku" content='<bean:write name="cartFormCOI" property="idnoWT" />'>
</logic:equal>
<logic:notEqual name="actionType" value="add">
	<META name="WT.si_n" content="Checkout">
    <META name="WT.si_x" content="4">
    <META name="WT.tx_e" content="v">
    <META name="WT.pn_sku" content='<bean:write name="cartFormCOI" property="idnoWT" />'>
</logic:notEqual>

<logic:notEmpty name="cartFormCOI" property="removedIdnoWT">
	<META name="WT.si_n" content="Checkout">
    <META name="WT.si_x" content="4">
    <META name="WT.tx_e" content="r">
    <META name="WT.pn_sku" content='<bean:write name="cartFormCOI" property="removedIdnoWT" />'>
</logic:notEmpty>

<!-- end Webtrends tags -->
<script>
function toggleDetails(link,divBlock){
	var liBlock='li_'+divBlock;
	
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

