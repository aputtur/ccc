<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/tld/struts-logic.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/tld/struts-bean.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/cc2-util.tld" %>
<%@ page errorPage="/jspError.do" %>

<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="com.copyright.ccc.web.transaction.TransactionConstants" %>
<%@ page import="com.copyright.ccc.web.WebConstants" %>

<jsp:include page="/WEB-INF/jsp-modules/common/jQueryDatepickerSupport.jsp"/>

<!-- tile attribute declarations -->


<tiles:useAttribute id="allowPermissionChange" name="allowPermissionChange" ignore="true"/>

<tiles:useAttribute id="showFooterCancel" name="showFooterCancel" ignore="true" />

<tiles:useAttribute id="showAddOrderFooterElements" name="showAddOrderFooterElements" ignore="true" />

<tiles:useAttribute id="submitButtonImage" name="submitButtonImage" ignore="true" classname="java.lang.String" />

<tiles:useAttribute id="submitButtonAlt" name="submitButtonAlt" ignore="true" classname="java.lang.String" />

<tiles:useAttribute id="addWebTrendsScenarios" name="addWebTrendsScenarios" ignore="true" />

<tiles:useAttribute id="addWebTrendsTypeOfUse" name="addWebTrendsTypeOfUse" ignore="true" />

<tiles:useAttribute id="mode" name="mode" ignore="true" />

<bean:define id="specialFormPath" name="rlinkSpecialOrderForm" property="specialFormPath" type="java.lang.String" />

<link href="<html:rewrite page="/resources/commerce/css/contextualHelp.css"/>" rel="stylesheet" type="text/css" />

<!-- end tile attribute declarations -->

<jsp:useBean id="rlinkSpecialOrderForm" scope="session" class="com.copyright.ccc.web.forms.coi.RLinkSpecialOrderForm"/>

<script src="<html:rewrite page="/resources/commerce/js/util.js"/>" type="text/javascript"></script>
<script src="<html:rewrite page="/resources/commerce/js/validators.js"/>" type="text/javascript"></script>
<script src="<html:rewrite page="/resources/commerce/js/dateUtils.js"/>" type="text/javascript"></script>
<script src="<html:rewrite page="/resources/commerce/js/contextualHelp.js"/>" type="text/javascript"></script>

<script type="text/javascript">

	function setTouId(permissionLevel,tpuInst,rightId,rightsholderInst,rrTou, rrTouId, categoryId, rlPermissionType, offerChannel, rlPubCode, obj)
{
		
	document.getElementById("selectedRlPubCode").value=rlPubCode;
	document.getElementById("selectedRlPermissionType").value="MR";
	document.getElementById("selectedOfferChannel").value=offerChannel;
	//alert("1");
	document.getElementById("touId").value=rrTouId;
	document.getElementById("touDescription").value=rrTou;
	document.getElementById("rightId").value=rightId;
		
}

    var isInSubmit = false;
    
    function submitTransaction()
    {
      document.getElementById("rlinkSpecialOrderForm").submit();
      $(waiting_for_search).show();
    }
    
    $(document).ready(function(){

	 	jqCalendarDatepickerCreate('dateOfUseText','<html:rewrite href='/media/images/calendar.gif'/>');
	 	jqCalendarDatepickerSetYearMenuRange('dateOfUseText','-0:+10');
	 	jqCalendarDatepickerSetMinDate( 'dateOfUseText', '+0y' );
	 	
	});

</script>


<html:form action="<%= specialFormPath %>" styleId="rlinkSpecialOrderForm">
    
    <html:hidden property="operation" styleId="operation" value="goToSpecialOrder" />
    
    <html:hidden name="rlinkSpecialOrderForm" property="selectedRlPermissionType" styleId="selectedRlPermissionType"/>
	<html:hidden name="rlinkSpecialOrderForm" property="selectedOfferChannel" styleId="selectedOfferChannel"/>
	<html:hidden name="rlinkSpecialOrderForm" property="selectedRlPubCode" styleId="selectedRlPubCode"/>
    
    <html:hidden name="rlinkSpecialOrderForm" property="typeOfUseDescription" styleId="touDescription"/>
	<html:hidden name="rlinkSpecialOrderForm" property="typeOfUseId" styleId="touId"/>
	<html:hidden name="rlinkSpecialOrderForm" property="catID" styleId="CategoryId"/>
	<html:hidden name="rlinkSpecialOrderForm" property="rightId" styleId="rightId"/>
       
    <bean:define id="termsTitle">Rightsholder Terms</bean:define>

     <!-- bean declarations -->
    
        
         
    <!-- end bean declarations -->
    
    <div style="color:red;padding-bottom:40px;padding-top:10px;display:none" id="clientSideValidationErrorsSection">
        <b>THERE WAS A PROBLEM COMPLETING YOUR REQUEST</b>
        <div style="color:red;padding-left:20px" id="clientSideValidationErrorsListSection"></div>
    </div>

    <!-- Begin Left Content -->
    
    <div id="ecom-boxcontent">
    
    	<logic:equal name="rlinkSpecialOrderForm" property="specialOrder"  value="false">
        <h1>
        	
            	Can't find the publication you're looking for?
                      <span class="subtitle"><html:link page="http://support.copyright.com/index.php?action=category&id=21" target="_blank">Learn more about how Special Orders work</html:link></span>
            
        </h1>
        </logic:equal>
        
        <logic:equal name="rlinkSpecialOrderForm" property="specialOrder"  value="false">
                    
        	<p>
        	<div style="color:red"> 
                Note: CCC has upgraded its republication service. If the rightsholder (copyright owner) isn't enrolled, your republication request may take at least 30 days for us to research and process. When applicable, providing complete bibliographic details in your request may help reduce processing time. If your request is time-sensitive, please contact the rightsholder directly.&nbsp;
            </div>    
            </p>
            
            
            </logic:equal>
        
        <logic:equal name="rlinkSpecialOrderForm" property="specialOrder"  value="true">
        <h1>Special Order</h1>
               
        <p>
                Permission for this title is not immediately available. However, you may place a Special Order for this title by completing the form below. Copyright Clearance Center will work on your behalf to obtain permission. We will let you know if we are able to obtain permission, usually within 2 to 15 business days.&nbsp;
                <html:link page="http://support.copyright.com/index.php?action=category&id=21" target="_blank"><b>Learn more about how Special Orders work</b></html:link>
            </p>
            
            </logic:equal>
          
   
            <html:link page="search.do?operation=show&page=last" styleClass="icon-back">
            	New search 
            </html:link>
            
            <br></br>
            
        
        
            
        <div class="horiz-rule"></div>
    
    	    
                       
            
        <table style="width:100%">
               
        <tr>
        	<td style="width:175px">
                <h2 class="floatleft"><strong>Permission type selected: </strong></h2>
            </td>
            <td>
            	<h2 style="margin-top:11px">
                    <span class="normaltype">
                    <bean:write name="rlinkSpecialOrderForm" property="categoryDescription" />
       
            	</span>
            </h2>
            	        
            </td>
            <logic:equal name="rlinkSpecialOrderForm" property="specialOrder"  value="true">
            <td>
            <span class="required"> * Required</span>
            </td>
            </logic:equal>
        </tr>
     </table>
     </br>
     
     <logic:equal name="rlinkSpecialOrderForm" property="specialOrder"  value="true">
     	<html:link href="javascript:history.go(-1);" styleClass="icon-back">
         Select different permission
         </html:link>
    </logic:equal>
     
    
    <logic:equal name="rlinkSpecialOrderForm" property="specialOrder"  value="false">
     	<html:link page="search.do?operation=addSpecialFromScratch" styleClass="icon-back">
            	Select different permission
        </html:link>
    </logic:equal>
            
            </br>
            </br>
            
     <div class="horiz-rule"></div>
            
     <logic:equal name="rlinkSpecialOrderForm" property="specialOrder"  value="false">
        	<h2>Enter the following details to place a Special Order (be as complete as possible):<span class="required"> * Required</span> </h2>
        	</logic:equal>
        	
        	
       	<div id="waiting_for_search" style="display: none;">
       		<div id="progress-widecontent_ppu">
       			<h1 align="center" vertical-align="center">Processing... Please wait... </h1> 
			</div>
		</div>
		        
        <logic:equal name="rlinkSpecialOrderForm" property="specialOrder"  value="true">
           
        <h2>Publication Information:</h2>
        </br>
        
        	
        	<table border="0" class="indent-1">
    
        <tr>
            <td width="120"><strong><div align="left">Publication name:</div></strong></td>
            <td><div align="left">
            	<bean:write name="rlinkSpecialOrderForm" property="publicationName"/>
            </div></td>
        </tr>
        
        <tr>
            <td><strong><div align="left">ISBN/ISSN:</div></strong></td>
            <td><div align="left">
                <bean:write name="rlinkSpecialOrderForm" property="issn"/>
            </div></td>
        </tr>
      
        <tr>
            <td><strong><div align="left">Publisher:</div></strong></td>
            <td><div align="left">
                <bean:write name="rlinkSpecialOrderForm" property="publisher"/>
            </div></td>
        </tr>
        
        <logic:notEmpty name="rlinkSpecialOrderForm" property="author">
        	<tr>
            	<td><strong><div align="left">Author/Editor:</div></strong></td>
            	<td><div align="left">
                	<bean:write name="rlinkSpecialOrderForm" property="author"/>
            	</div></td>
        	</tr>
        </logic:notEmpty>
        
        <logic:notEmpty name="rlinkSpecialOrderForm" property="volume">
        	<tr>
            	<td><strong><div align="left">Volume:</div></strong></td>
            	<td><div align="left">
                	<bean:write name="rlinkSpecialOrderForm" property="volume"/>
            	</div></td>
        	</tr>
        </logic:notEmpty>
        
        
        <logic:notEmpty name="rlinkSpecialOrderForm" property="edition">
        	<tr>
            	<td><strong><div align="left">Edition:</div></strong></td>
            	<td><div align="left">
                	<bean:write name="rlinkSpecialOrderForm" property="edition"/>
            	</div></td>
        	</tr>
        </logic:notEmpty>
        
        <logic:notEmpty name="rlinkSpecialOrderForm" property="pubDate">
        	<tr>
            	<td><strong><div align="left">Publication Date:</div></strong></td>
            	<td><div align="left">
                	<bean:write name="rlinkSpecialOrderForm" property="pubDate"/>
            	</div></td>
        	</tr>
        </logic:notEmpty>
        
    </table>
            
         
       </logic:equal>
     
     <logic:equal name="mode" value="edit">
            <div style="width:100%" align="center">
                <div class="calloutbox" style="background-color: #FFFFD6;width:80%" align="left">
                    <p class="smalltype icon-alert">
                       Please note that any changes you make to one item in your cart may affect the price or permission status of other items.
                       <bean:define id="editCartTitle">Edit cart item</bean:define>
                       <bean:define id="editCartBody">Some rights holders base their prices and permission availability on the number of requests for a work or group of works. If your changes affect the price or permission status of other items in your cart, you will see the updates in your shopping cart.</bean:define>
                       <util:contextualHelp bodyName="editCartBody" rollover="true">More...</util:contextualHelp>
                    </p>
                </div>
                
            </div>
        </logic:equal>
    
        
        
        <br />
        
        <div class="horiz-rule"></div>
    
        <h2>Type of use:<span class="importanttype"> *</span></h2>
        
        <bean:define id="permCat" name="rlinkSpecialOrderForm" property="permissionSummaryCat"/>
        
                
        <logic:iterate name="rlinkSpecialOrderForm" property="permissionSummaryTypeOfUse" id="permTou" indexId="permTouIdx">
             
                                                
                                
                                      
                   <!-- added for pub to pub -->
                   <bean:define id="rlPermissionType" name="permTou" property="rlPermissionType"/>
                   <bean:define id="offerChannel" name="permTou" property="offerChannel"/>
                   <bean:define id="rlPubCode" name="permTou" property="rlPubCode"/>
                   <bean:define id="permissionLevel" name="permTou" property="permissionLevel"/>
                   
                   
                   
	                <bean:define id="rgtInstRlOfferId" name="permTou" property="rgtInstRlOfferId"/>
	                <bean:define id="rightsholderInst" name="permTou" property="rightsholderInst"/>
	                <bean:define id="tpuInst" name="permTou" property="tpuInst"/>
	                <bean:define id="tpuDesc"   >
	                <bean:write name="permTou" property="description"  filter="true" />
	                </bean:define>
	                <bean:define id="tpuEscapedDesc"   >
	                <bean:write name="permTou" property="escapedDescription"  filter="true" />
	                </bean:define>
	                
	                <bean:define id="rrTouId" name="permTou" property="rrTouId"/>
	                <bean:define id="categoryId" name="permCat" property="categoryId"/>
	                
	                <!-- ONLY SHOW THE RADIO BUTTONS IF AT LEAST ONE OF THEM IS AVAILABLE FOR PURCHASE -->
	                <logic:match name="permCat" property="availability" location="start" value="Available for Purchase">
	                	                	            
	                <div class="row">
		                <!-- Check if contact rights holder directly -->
		                <logic:equal name="permTou" property="isNotAvailable" value="false">
			                   <logic:equal name="permTou" property="isContactRightsholder" value="true">
			                     
			                   <html:radio  styleId="radio_${categoryId}" name="rlinkSpecialOrderForm"  property="typeOfUseId" value='${rrTouId}'  onclick="setTouId('${permissionLevel}',${tpuInst}, ${rgtInstRlOfferId},'${rightsholderInst}','${tpuEscapedDesc}',${rrTouId},${categoryId}, '${rlPermissionType}', '${offerChannel}', '${rlPubCode}', this)"></html:radio>
			                   <bean:write name="permTou" property="description" />
		                          
		                       </logic:equal>
		                       <logic:notEqual name="permTou" property="isContactRightsholder" value="true">
			                       <html:radio  styleId="radio_${categoryId}" name="rlinkSpecialOrderForm"  property="typeOfUseId" value='${rrTouId}'  onclick="setTouId('${permissionLevel}',${tpuInst},${rgtInstRlOfferId},'0','${tpuEscapedDesc}',${rrTouId},${categoryId}, '${rlPermissionType}', '${offerChannel}', '${rlPubCode}')"></html:radio>
		                       <bean:write name="permTou" property="description" />
		                       </logic:notEqual>	
	                       </logic:equal>	                       	                                     
		               
		                
		                </div>
	                </logic:match>
	             
	             
	           
	           
	           
             </logic:iterate>
        
         <br />
        
        <logic:equal name="rlinkSpecialOrderForm" property="specialOrder"  value="false">
           
        <h2>Publication Information:</h2>
        
        	
        	<table border="0" class="indent-1">
    
        <tr>
            <td width="115"><div align="left">Publication name: <span class="importanttype">*</span></div></td>
            <td><div align="left">
            	<html:text name="rlinkSpecialOrderForm" property="publicationName" styleClass="normal" maxlength="250" styleId="publicationName"/>
            </div></td>
        </tr>
        
        <tr>
            <td><div align="left">ISBN/ISSN:</div></td>
            <td><div align="left">
                <html:text name="rlinkSpecialOrderForm" property="issn" styleClass="normal" maxlength="250"/>
            </div></td>
        </tr>
      
        <tr>
            <td><div align="left">Publisher: <span class="importanttype">*</span></div></td>
            <td><div align="left">
                <html:text name="rlinkSpecialOrderForm" property="publisher" styleClass="normal" maxlength="250" styleId="publisherName"/>
            </div></td>
        </tr>
        
        <tr>
            <td><div align="left">Author/Editor:</div></td>
            <td><div align="left">
                <html:text name="rlinkSpecialOrderForm" property="author" styleClass="normal" maxlength="250"/>
            </div></td>
        </tr>
        
        <tr>
            <td><div align="left">Volume:</div></td>
            <td><div align="left">
                <html:text name="rlinkSpecialOrderForm" property="volume" styleClass="normal" maxlength="250"/>
            </div></td>
        </tr>
        
        <tr>
            <td><div align="left">Edition:</div></td>
            <td><div align="left">
                <html:text name="rlinkSpecialOrderForm" property="edition" styleClass="normal" maxlength="250"/>
            </div></td>
        </tr>
        
        <tr>
            <td><div align="left">Publication Date:</div></td>
            <td><div align="left">
                <html:text name="rlinkSpecialOrderForm" property="pubDate" styleClass="normal" maxlength="250" styleId="dateOfUseText" value="MM/DD/YYYY"/>
            </div></td>
        </tr>
        
    </table>
            <br />
         <br />
       </logic:equal>    
                       
        <div class="horiz-rule"></div>
        
                    
    
        
        
        
        
        <span class="floatright">
            <a href="javascript:submitTransaction();" onclick="this.disabled=true;" id="submitButtonImage">
               
                    <html:img src="/media/images/btn_continue.gif" alt="Continue" align="right" />
                
                
            </a>
        </span>
        
        
        
        
        <br clear="all" />

    </div>
          

</html:form>

<!-- Webtrends tags for capturing scenarios -->

<!-- end Webtrends tags -->