<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/cc2-util.tld" prefix="util"%>
<%@ taglib uri="/WEB-INF/tld/cc2-security.tld" prefix="security" %>
<%@ page import="com.copyright.ccc.web.transaction.RepublicationConstants" %>
<%@ page import="com.copyright.ccc.business.services.ItemConstants" %>

<%@ page errorPage="/jspError.do" %>

<link type="text/css" rel="stylesheet" href="<html:rewrite page="/resources/commerce/css/contextualHelp.css"/>"/>
<link type="text/css" rel="stylesheet" href="/resources/commerce/css/cart_all.css"/>
<!--[if lt IE 8]><link rel="stylesheet" type="text/css" href="/resources/commerce/css/css/cart_ie.css"/><![endif]-->
<link href="<html:rewrite page="/resources/commerce/css/rightslink_font.css"/>" rel="stylesheet" type="text/css" />

<style type="text/css">
#ecom-content-wrapper{
padding:0px;
}

h3{
text-transform:none;
}
#bottomcorners{
background:none;
}


hr { height:1px; color: #999999; }
#searchBar td {
   vertical-align:middle; 
}
.rule {
   border-top: solid 1px #999;
   height: 2px;
   padding: 0 0 0 0;
}
.box01 {
  padding: 5px 0 5px 0;
  border-bottom: 1px solid #999999;
}
.results {
font-family:Verdana, Arial, Helvetica, sans-serif;
font-size:13px;
text-align:center; 
margin:5px 0;
}
table.RLTable p {
	margin:0px 0;
}
</style>

<script type="text/javascript" src="<html:rewrite page='/resources/commerce/js/util.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page="/resources/commerce/js/contextualHelp.js"/>"></script>
<script type="text/javascript" src="<html:rewrite page="/resources/commerce/js/orderViewPage.js"/>"></script>

<script type="text/javascript">
if (browserInfo.isIE){
    document.write ("<LINK REL='stylesheet' type='text/css' href='/resources/commerce/css/rightslink_layout_msie.css'>");
}
else{
    document.write ("<LINK REL='stylesheet' type='text/css' href='/resources/commerce/css/rightslink_layout_ns6.css'>");
}


</script>
<bean:define id="processType">ORDER_PRINT</bean:define>
<div>
<div class="holder">
<div id="main">

    <div class="inbox07">
        <h2><span class="bold">Confirmation Number:</span> <bean:write name="orderViewActionForm" property="id"/><br>
        <span class="bold">Order Date:</span> <bean:write name="orderViewActionForm" property="orderDateStr"/></h2>
    </div>
    
    <div class="noPrint floatright">
    <html:img src="/media/images/printer.gif" width="23" height="18" /><a href="javascript:window.print()">Print this page</a><br>
    </div>
    
    <div class="horiz-rule"></div>
    <div class="clearer"></div>
    
    <h2>Customer Information</h2>
    <div class="item-details">
        <p class="indent-1">
          <logic:notEmpty name="orderViewActionForm" property="custName">
          <b>Customer:</b> <bean:write name="orderViewActionForm" property="custName"/><br />
          </logic:notEmpty>
          <logic:notEmpty name="orderViewActionForm" property="acctNumber">
          <b>Account Number:</b> <bean:write name="orderViewActionForm" property="acctNumber"/><br />
          </logic:notEmpty>
          <logic:notEmpty name="orderViewActionForm" property="custCompany">
          <b>Organization:</b> <bean:write name="orderViewActionForm" property="custCompany"/> <br />
          </logic:notEmpty>
          <logic:notEmpty name="orderViewActionForm" property="custEmail">
          <b>Email:</b> <bean:write name="orderViewActionForm" property="custEmail"/><br />
          </logic:notEmpty>
          <logic:notEmpty name="orderViewActionForm" property="custPhone">
          <b>Phone:</b> <bean:write name="orderViewActionForm" property="custPhone"/><br />
          </logic:notEmpty>
          <logic:notEmpty name="orderViewActionForm" property="payMethod">
          <b>Payment Method:</b> <bean:write name="orderViewActionForm" property="payMethod"/><br />
          </logic:notEmpty>
          <logic:notEmpty name="orderViewActionForm" property="poNumber">
          <b>PO#:</b> <bean:write name="orderViewActionForm" property="poNumber"/><br />
          </logic:notEmpty>
          <logic:notEmpty name="orderViewActionForm" property="promoCode">
          <b>Promotion code:</b> <bean:write name="orderViewActionForm" property="promoCode"/>
          </logic:notEmpty>
        </p>
    </div>

    <logic:equal name="orderViewActionForm" property="specialOrder" value="true">
        <logic:equal name="orderViewActionForm" property="CCOrder" value="true">
            <logic:notEmpty name="orderViewActionForm" property="billingAddress">
                <div class="item-details indent1">
                    <p>
                        <b>Special Order Invoices billed to:</b><br />
                        <!-- TODO: Ask Gen how to filter the billingAddress for scripts -->
                        <logic:notEmpty name="orderViewActionForm" property="billToPerson">
                            <bean:write name="orderViewActionForm" property="billToPerson" /><br />
                        </logic:notEmpty>
                        <logic:notEmpty name="orderViewActionForm" property="company">
                            <bean:write name="orderViewActionForm" property="company" /><br />
                        </logic:notEmpty>
                        <logic:notEmpty name="orderViewActionForm" property="street1">
                            <bean:write name="orderViewActionForm" property="street1" /><br />
                        </logic:notEmpty>
                        <logic:notEmpty name="orderViewActionForm" property="street2">
                            <bean:write name="orderViewActionForm" property="street2" /><br />
                        </logic:notEmpty>
                        <logic:notEmpty name="orderViewActionForm" property="street3">
                            <bean:write name="orderViewActionForm" property="street3" /><br />
                        </logic:notEmpty>
                        <logic:notEmpty name="orderViewActionForm" property="street4">
                            <bean:write name="orderViewActionForm" property="street4" /><br />
                        </logic:notEmpty>
                        <logic:notEmpty name="orderViewActionForm" property="city">
                            <bean:write name="orderViewActionForm" property="city" />
                        </logic:notEmpty>
                        <logic:notEmpty name="orderViewActionForm" property="state">
                            <logic:notEmpty name="orderViewActionForm" property="city">,&nbsp;</logic:notEmpty>
                            <bean:write name="orderViewActionForm" property="state" />
                        </logic:notEmpty>
                        <logic:notEmpty name="orderViewActionForm" property="zipcode">
                            &nbsp;<bean:write name="orderViewActionForm" property="zipcode" /><br />
                        </logic:notEmpty>
                    </p>
                </div>
            </logic:notEmpty>
        </logic:equal>
    </logic:equal>
    
    <div class="horiz-rule"></div>
    <div class="clearer"></div>
    <logic:greaterThan value="0" name="orderViewActionForm" property="numberOfOrderItems">
    <div style="text-align:center"><h1><b>This not an invoice</b></h1></div>
		<P class="largertype bold" style="HEIGHT: 10px">Order Details</P>
		</logic:greaterThan>
		<logic:equal name="orderViewActionForm" property="showHeaderSection" value="true"> 
<h3>Course items</h3>
</logic:equal>
		
    <logic:equal name="orderViewActionForm" property="academic" value="true">
   	<div class="choise-block">
						<div class="tilte">
							<strong>Course: <logic:notEmpty name="orderViewActionForm" property="orderBundle.courseName">
							<bean:write name="orderViewActionForm" property="orderBundle.courseName" />
							</logic:notEmpty> </strong>
							<logic:equal name="orderViewActionForm" property="closed" value="false">
							<logic:equal name="orderViewActionForm" property="courseInfo" value="true">
							<ul>
								<li>
								<html:link action="/editCourseInfo.do" name="orderViewActionForm" property="editCourseInfoMap" >Edit Course</html:link>
								</li></ul>
						</logic:equal>
							</logic:equal>
						
						</div>
							<div class="parameter-list">
							<dl>
							  <dt>University/Institution: </dt><dd>
							  <logic:empty name="orderViewActionForm" property="orderBundle.organization">&nbsp;</logic:empty>
							  <bean:write name="orderViewActionForm" property="orderBundle.organization" />
							  </dd>
                        	 <dt>Start of term: </dt><dd>
							  <logic:empty name="orderViewActionForm" property="orderBundle.startOfTerm">&nbsp;</logic:empty>
                        	 <bean:write name="orderViewActionForm" property="orderBundle.startOfTerm" format="MM/dd/yyyy"/>
                        	 </dd>
                             <dt>Course number: </dt><dd>
							  <logic:empty name="orderViewActionForm" property="orderBundle.courseNumber">&nbsp;</logic:empty>
                             <bean:write name="orderViewActionForm" property="orderBundle.courseNumber" /></dd>
                             <dt>Number of students: </dt><dd>
							  <logic:empty name="orderViewActionForm" property="orderBundle.numberOfStudents">&nbsp;</logic:empty>
                             <bean:write name="orderViewActionForm" property="orderBundle.numberOfStudents" /></dd>
							</dl>
							<dl>
							<dt>Instructor: </dt><dd><logic:empty name="orderViewActionForm" property="orderBundle.instructor">&nbsp;</logic:empty>
							<bean:write name="orderViewActionForm" property="orderBundle.instructor" /></dd>
                        <dt>Your reference: </dt><dd>
							  <logic:empty name="orderViewActionForm" property="orderBundle.yourReference">&nbsp;</logic:empty>
                        <bean:write name="orderViewActionForm" property="orderBundle.yourReference" /></dd>
                        <dt>Accounting reference: </dt><dd>
							  <logic:empty name="orderViewActionForm" property="orderBundle.accountingReference">&nbsp;</logic:empty>
							  <bean:write name="orderViewActionForm" property="orderBundle.accountingReference" /></dd>
                        <dt>Order entered by: </dt><dd>
							  <logic:empty name="orderViewActionForm" property="orderBundle.orderEnteredBy">&nbsp;</logic:empty>
							  <bean:write name="orderViewActionForm" property="orderBundle.orderEnteredBy" />
                        			</dd>
								
							</dl>
						</div>
						
						
						</div>

        
        
        <logic:equal value="true" name="orderViewActionForm" property="hasAcademicItems">
         <bean:define id="academicRegularBoxStyle">border-top:0px;</bean:define>
		<logic:iterate id="item" name="orderViewActionForm" property="orderDetails" indexId="index">
		<logic:equal name="item" property="academic" value="true"> 
	            <logic:equal name="item" property="unresolvedSpecialOrder" value="false"> <!-- Filter Out Special Order -->
	            <div class="product-box" style="<%=academicRegularBoxStyle%>">
	                <%@ include file = "/WEB-INF/jsp-modules/orderhistory/tiles/academicOrder.jsp" %>
	             </div>	
	              <bean:define id="academicRegularBoxStyle">border-top:1px solid #CCCCCC;</bean:define>
	            </logic:equal> <!--Filter Out Special Orders -->
	     </logic:equal> 
		</logic:iterate>


	            <!-- *************** BEGIN ACADEMIC Special Order Items *********************** -->
	            <logic:equal value="true" name="orderViewActionForm" property="hasAcademicSpecialOrders">
	                <div class="choise-block" style="padding: 4px 0px 0px;"> 
						<div class="tilte" style="padding-left: 4px; margin: 0px;">
							<strong>Special Orders</strong>
						</div> 
				<bean:define id="academicSpecialBoxStyle">border-top:0px;</bean:define>                     
	            <logic:iterate name="orderViewActionForm" property="orderDetails" id="item" indexId="index">
	            <logic:equal name="item" property="academic" value="true">
	            <logic:equal name="item" property="unresolvedSpecialOrder" value="true">  <!-- Filter in Special Order -->
	            <div class="product-box" style="width:688px;<%=academicSpecialBoxStyle%>">
	             <%@ include file = "/WEB-INF/jsp-modules/orderhistory/tiles/academicOrder.jsp" %>
	             </div>	
	             <br/>
	             <bean:define id="academicSpecialBoxStyle">border-top:1px solid #CCCCCC;</bean:define>
	                </logic:equal> <!--Filter Out Special Orders -->
	                </logic:equal>
	            </logic:iterate>
	                </div>        
	            </logic:equal>
	   </logic:equal> 
            <!-- *************** END Special Order Items *********************** -->
        
        
    </logic:equal>
    
                
            <logic:equal value="true" name="orderViewActionForm" property="showHeaderSection">
			<logic:equal value="false" name="orderViewActionForm" property="isFilterOn">
                     <div class="total">
                     
						<strong class="number" style="color:#666666">Course items: <span><bean:write name="orderViewActionForm" property="numberOfAcademicOrders" /></span></strong>
                    			<strong class="total-price" style="color:#666666">Course total: <span style="font-size:14px"><bean:write name="orderViewActionForm" property="courseTotal" /></span>
                    			<logic:equal name="orderViewActionForm" property="containsTBDAcademicOrder" value="true">
                        		<br/><span style="font-size:12px;color:#666666;">(Excludes $TBD items)</span>
                    			</logic:equal>
                    			</strong>
                				
					</div>
					</logic:equal>
     		<h3>Single Items</h3>
        	</logic:equal>
    
    <!-- ************************************************ Added Code********************************************************* -->  
<logic:equal value="true" name="orderViewActionForm" property="hasNonAcademicItems">
            <!-- *************** BEGIN Regular Order Items *********************** -->
                      <bean:define id="nonAcademicRegularBoxStyle">border-top:0px;</bean:define>  
        <logic:iterate name="orderViewActionForm" property="orderDetails" id="item" indexId="index">
	            <logic:equal name="item" property="academic" value="false"> <!-- Non Academic Orders -->
	            <logic:equal name="item" property="reprint" value="false">  <!-- Filter RePrint-->
	              <logic:equal name="item" property="unresolvedSpecialOrder" value="false"> <!-- Filter Out Special Order -->
	              <div class="product-box" style="<%=nonAcademicRegularBoxStyle%>">
	  					<%@ include file = "/WEB-INF/jsp-modules/orderhistory/tiles/nonAcademicOrder.jsp" %>
	  				</div>
	  				<bean:define id="nonAcademicRegularBoxStyle">border-top:1px solid #CCCCCC;</bean:define>
	  				</logic:equal> <!--Filter Out Reprints -->
	  		       </logic:equal> <!--Filter Out Special Orders -->
	             </logic:equal>                
	        </logic:iterate>
            
             <!-- *************** END Regular Order Items *********************** -->
            
            <!-- *************** BEGIN Special Order Items *********************** -->
            
                 <!-- *************** BEGIN Non Academic Non REPRINT Special Order Permission Items *********************** -->
            <logic:equal value="true" name="orderViewActionForm" property="hasNonReprintSpecialOrders">
           <div class="choise-block" style="padding: 4px 0px 0px;"> 
						<div class="tilte" style="padding-left: 4px; margin: 0px;">
							<strong>Special Orders</strong>
						</div>
		<bean:define id="nonAcademicSpecialBoxStyle">border-top:0px;</bean:define>
        <logic:iterate name="orderViewActionForm" property="orderDetails" id="item" indexId="index">
        <logic:equal name="item" property="academic" value="false"> <!-- Non Academic Orders -->
             <logic:equal name="item" property="unresolvedSpecialOrder" value="true">  <!-- Filter Out Special Order -->
                <logic:equal name="item" property="reprint" value="false">  <!-- Filter Out Re Print -->
	             <div class="product-box" style="width:688px;<%=nonAcademicSpecialBoxStyle%>">   
								<%@ include file = "/WEB-INF/jsp-modules/orderhistory/tiles/nonAcademicOrder.jsp" %>
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
            <logic:equal value="true" name="orderViewActionForm" property="hasReprintOrders">
            <div class="choise-block" style="padding: 4px 0px 0px;"> 
						<div class="tilte" style="padding-left: 4px; margin: 0px;">
							<strong>Reprints</strong>
						</div>                               
            <bean:define id="rePrintBoxStyle">border-top:0px;</bean:define>
           <logic:iterate name="orderViewActionForm" property="orderDetails" id="item" indexId="index">
        		<logic:equal name="item" property="academic" value="false"> <!-- Non Academic Orders -->
                <logic:equal name="item" property="reprint" value="true">  <!-- Reprint -->
                <div class="product-box" style="width:688px;<%=rePrintBoxStyle%>">
                <%@ include file = "/WEB-INF/jsp-modules/orderhistory/tiles/nonAcademicOrder.jsp" %>
                  </div>
                  <br/>
                  <bean:define id="rePrintBoxStyle">border-top:1px solid #CCCCCC;</bean:define>
                </logic:equal> <!--Filter Out Reprints -->
                </logic:equal>
                                                                       
            </logic:iterate>
                </div>   
            </logic:equal>
            <!-- *************** END  REPRINT  Special Order Items *********************** -->

<!-- ***********************************************End Added Code ******************************************************* -->   
 </logic:equal>
    
    		<logic:equal value="true" name="orderViewActionForm" property="showHeaderSection">
    		<logic:equal value="false" name="orderViewActionForm" property="isFilterOn">
    		
          			<div class="total">
						<strong class="number"  style="color:#666666">Single items: <span><bean:write name="orderViewActionForm" property="numberOfNonAcademicOrders" /></span></strong>
                    			<strong class="total-price" style="color:#666666">Single item total : <span style="font-size:14px"><bean:write name="orderViewActionForm" property="singleItemTotal" /></span>
 								<logic:equal name="orderViewActionForm" property="containsTBDNonAcademicOrder" value="true">
                        		<br/><span style="font-size:12px;color:#666666;">(Excludes $TBD items)</span>
                    			</logic:equal>                   			
                    			</strong> 
					</div>
        </logic:equal>
		</logic:equal> 
    <!-- Payment total -->
    <logic:greaterThan value="0" name="orderViewActionForm" property="numberOfOrderItems">
			<div class="total-sum">
			<div class="order_total">
			<logic:equal value="false" name="orderViewActionForm" property="isFilterOn">
			<div class="floatleft">
			<h2 style="margin-top:0px">Total order items:&nbsp;	<label class="largetype"><bean:write name="orderViewActionForm" property="numberOfOrderItems" /></label>	
			                		</h2>
			
			</div>
			<div class="floatright largertype bold" style="font-size:16px;margin-top:0px;">
			Order Total: <span class="largetype"><bean:write name="orderViewActionForm" property="orderTotal"/></span>
			<logic:equal name="orderViewActionForm" property="containsTBDOrder" value="true">
			                        		<br/><span style="font-size:12px;color:#666666;">(Excludes $TBD items)</span>
			                    			</logic:equal>
			
			</div>
			<div class="clearer"></div>
			</logic:equal>	
			</div>
			</div>
		</logic:greaterThan>
			    

</div><!--  class="holder">-->
 </div><!-- id="main">-->
</div>

<script type="text/javascript">
    function printPage()
    {
        bV = parseInt(navigator.appVersion);
        if (bV >= 4) window.print();
    }
    
    if( typeof addOnLoadEvent == 'function' && typeof printPage == 'function' ) addOnLoadEvent( printPage );
</script>
