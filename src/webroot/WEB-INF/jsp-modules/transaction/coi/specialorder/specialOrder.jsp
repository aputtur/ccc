<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/tld/struts-logic.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/tld/struts-bean.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/cc2-util.tld" %>
<%@ page errorPage="/jspError.do" %>

<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="com.copyright.ccc.web.transaction.TransactionConstants" %>

<!-- tile attribute declarations -->

<tiles:useAttribute id="headerType" name="headerType" />

<tiles:useAttribute id="allowPermissionChange" name="allowPermissionChange" ignore="true"/>

<tiles:useAttribute id="showFooterCancel" name="showFooterCancel" ignore="true" />

<tiles:useAttribute id="showAddOrderFooterElements" name="showAddOrderFooterElements" ignore="true" />

<tiles:useAttribute id="submitButtonImage" name="submitButtonImage" ignore="true" classname="java.lang.String" />

<tiles:useAttribute id="submitButtonAlt" name="submitButtonAlt" ignore="true" classname="java.lang.String" />

<tiles:useAttribute id="addWebTrendsScenarios" name="addWebTrendsScenarios" ignore="true" />

<tiles:useAttribute id="addWebTrendsTypeOfUse" name="addWebTrendsTypeOfUse" ignore="true" />

<tiles:useAttribute id="mode" name="mode" ignore="true" />

<bean:define id="specialFormPath" name="specialOrderFormCOI" property="specialFormPath" type="java.lang.String" />

<bean:define id="specialCancelPath" name="specialOrderFormCOI" property="specialCancelPath" type="java.lang.String" />

<link href="<html:rewrite page="/resources/commerce/css/contextualHelp.css"/>" rel="stylesheet" type="text/css" />

<!-- end tile attribute declarations -->

<jsp:useBean id="specialOrderFormCOI" scope="session" class="com.copyright.ccc.web.forms.coi.SpecialOrderForm"/>

<script src="<html:rewrite page="/resources/commerce/js/util.js"/>" type="text/javascript"></script>
<script src="<html:rewrite page="/resources/commerce/js/validators.js"/>" type="text/javascript"></script>
<script src="<html:rewrite page="/resources/commerce/js/dateUtils.js"/>" type="text/javascript"></script>
<script src="<html:rewrite page="/resources/commerce/js/contextualHelp.js"/>" type="text/javascript"></script>

<script type="text/javascript">

    var isInSubmit = false;
    
    function submitTransaction()
    {
        if( !isInSubmit )
        {
            isInSubmit = true;
            
            //Need to enable these fields just in case they are disabled by OOP choice
            flds = document.getElementsByName("specialOrderItem.numberOfPages");
         	fldsPageRange = document.getElementsByName("specialOrderItem.pageRange");

         	if (flds[0] != null)
         	{
         		flds[0].disabled = false;
         	}

         	if (fldsPageRange[0] != null)
         	{
         		fldsPageRange[0].disabled = false;
         	}
            
             <logic:equal name="showAddOrderFooterElements" value="true">
                if(!document.getElementById("termsAndConditionsCheckbox").checked)
                {
                    alert("Please accept the terms and conditions to continue.");
                    isInSubmit = false;
                    return;
                }
             </logic:equal>
             
             var form = document.getElementById("specialOrderFormCOI");
             
             document.getElementById("operation").value = "submit";
            
             var errors = validateForm(form);
    
             if(errors.length > 0)
             {
                displayValidationErrors(errors, "clientSideValidationErrorsSection", "clientSideValidationErrorsListSection");
                isInSubmit = false;
             }
             else
             {
                hideValidationErrors("clientSideValidationErrorsSection");
                document.getElementById("specialOrderFormCOI").submit();
             }
         }
         
         return false;
    }

</script>


<html:form action="<%= specialFormPath %>" styleId="specialOrderFormCOI">
    
    <html:hidden property="operation" styleId="operation" value="" />
    <html:hidden name="specialOrderFormCOI" property="specialOrderItem.specialOrderFromScratch" />
    <bean:define id="termsTitle">Rightsholder Terms</bean:define>

     <!-- bean declarations -->
    
    <bean:define id="specialOrderItem" name="specialOrderFormCOI" property="specialOrderItem" 
        type="com.copyright.ccc.business.data.TransactionItem"/>
    
     <bean:define id="detailsTileName">
        <logic:equal name="specialOrderItem" property="specialOrderFromScratch" value="true">
            <logic:equal name="specialOrderItem" property="academic" value="true">academicDetailsNoTitle</logic:equal>
            <logic:equal name="specialOrderItem" property="photocopy" value="true">photocopyDetailsNoTitle</logic:equal>
            <logic:equal name="specialOrderItem" property="republication" value="true">republicationDetailsNoTitle</logic:equal>
            <logic:equal name="specialOrderItem" property="digital" value="true">digitalDetailsNoTitle</logic:equal>
        </logic:equal>
        <logic:notEqual name="specialOrderItem" property="specialOrderFromScratch" value="true">
            <logic:equal name="specialOrderItem" property="academic" value="true">academicDetailsWithTitle</logic:equal>
            <logic:equal name="specialOrderItem" property="photocopy" value="true">photocopyDetailsWithTitle</logic:equal>
            <logic:equal name="specialOrderItem" property="republication" value="true">republicationDetailsWithTitle</logic:equal>
            <logic:equal name="specialOrderItem" property="digital" value="true">digitalDetailsWithTitle</logic:equal>
        </logic:notEqual>
    </bean:define>
    
    <!-- end bean declarations -->
    
    <div style="color:red;padding-bottom:40px;padding-top:10px;display:none" id="clientSideValidationErrorsSection">
        <b>THERE WAS A PROBLEM COMPLETING YOUR REQUEST</b>
        <div style="color:red;padding-left:20px" id="clientSideValidationErrorsListSection"></div>
    </div>

    <!-- Begin Left Content -->
    
    <div id="ecom-boxcontent">
    
        <h1>
            <logic:equal name="headerType" value="add">
                <logic:equal name="specialOrderItem" property="specialOrderFromScratch" value="true">Can't find the publication you're looking for?</logic:equal>
                <logic:notEqual name="specialOrderItem" property="specialOrderFromScratch" value="true">Special Order</logic:notEqual>
            </logic:equal>
            <logic:equal name="headerType" value="edit">Edit Special Order</logic:equal>
            <logic:equal name="specialOrderItem" property="specialOrderFromScratch" value="true">
                <span class="subtitle"><html:link page="http://support.copyright.com/index.php?action=category&id=21" target="_blank">Learn more about how Special Orders work</html:link></span>
            </logic:equal>
        </h1>
        
        <html:link page="<%= specialCancelPath %>" styleClass="icon-back">
            <logic:equal name="headerType" value="add">New search</logic:equal>
            <logic:equal name="headerType" value="edit">Cancel changes</logic:equal>
        </html:link>
        
        <logic:equal name="specialOrderItem" property="specialOrderFromScratch" value="true">
            <br /><br />
        </logic:equal>
        <logic:notEqual name="specialOrderItem" property="specialOrderFromScratch" value="true">
            <p>
                Permission for this title is not immediately available. However, you may place a Special Order for this title by completing the form below. Copyright Clearance Center will work on your behalf to obtain permission. We will let you know if we are able to obtain permission, usually within 2 to 15 business days.&nbsp;
                <html:link page="http://support.copyright.com/index.php?action=category&id=21" target="_blank">Learn more about how Special Orders work</html:link>
            </p>
            <div class="horiz-rule"></div>
            <tiles:insert name="bibliographicInformationCOI">
                <tiles:put name="transactionItem" beanName="specialOrderItem" />
                <tiles:put name="couldShowFees" value="false" />
                <tiles:put name="displayPublicationYearRange" beanName="specialOrderFormCOI" beanProperty="specialDisplayPublicationYearRange" />
            </tiles:insert>
            <%  
                //  2009-03-02  MSJ
                //  Adding code for rightsholder terms.
                //  Changed to continue to load generic stuff for DPS and RLS
                //  because we load once, and do not refresh.
                
                String txtTermsApply = null;
            %>
            <logic:notEmpty name="specialOrderItem" property="externalCommentTerm">
                <bean:define id="termsBody" name="specialOrderItem" property="externalCommentTerm"/>
                <logic:equal name="specialOrderItem" property="republication" value="true">
                    <%
                    txtTermsApply = "Special terms from the rightsholder may apply, depending on your type of use.<br /><br />Please continue order process and special terms will be provided during Order Review, if applicable.";
                    pageContext.setAttribute("termsBody", txtTermsApply, PageContext.PAGE_SCOPE);
                    %>
                </logic:equal>
                <logic:equal name="specialOrderItem" property="digital" value="true">
                    <%
                    txtTermsApply = "Special terms from the rightsholder may apply, depending on your type of use.<br /><br />Please continue order process and special terms will be provided during Order Review, if applicable.";
                    pageContext.setAttribute("termsBody", txtTermsApply, PageContext.PAGE_SCOPE);
                    %>
                </logic:equal>
            </logic:notEmpty>
            <%  if((specialOrderItem.isAcademic()
                    || specialOrderItem.isPhotocopy()
                    || specialOrderItem.isRepublication() 
                    || specialOrderItem.isDigital()) && specialOrderItem.getRightsQualifyingStatement() != null && specialOrderItem.getRightsQualifyingStatement().length() < 100)
                { %>
                    <p class="icon-alert clearer">
                        <strong><%= specialOrderItem.getRightsQualifyingStatement() %></strong>&nbsp;
                        <logic:notEmpty name="specialOrderItem" property="externalCommentTerm">
                            <util:contextualHelp titleName="termsTitle" bodyName="termsBody" rollover="true">Rightsholder terms apply</util:contextualHelp>
                        </logic:notEmpty>
                    </p>
            <%  } %>
            <%  if((specialOrderItem.isAcademic()
                    || specialOrderItem.isPhotocopy()
                    || specialOrderItem.isRepublication()
                    || specialOrderItem.isDigital()) && specialOrderItem.getRightsQualifyingStatement() != null && specialOrderItem.getRightsQualifyingStatement().length() >= 100)
                { %>
                    <p class="icon-alert clearer">
                        <%= specialOrderItem.getRightsQualifyingStatement() %>&nbsp;
                        <logic:notEmpty name="specialOrderItem" property="externalCommentTerm">
                            <util:contextualHelp titleName="termsTitle" bodyName="termsBody" rollover="true">Rightsholder terms apply</util:contextualHelp>
                        </logic:notEmpty>
                    </p>
            <%  } %>
            <%  if(specialOrderItem.getRightsQualifyingStatement() == null) { %>
                    <logic:notEmpty name="specialOrderItem" property="externalCommentTerm">
                        <p class="icon-alert clearer"><util:contextualHelp titleName="termsTitle" bodyName="termsBody" rollover="true">Rightsholder terms apply</util:contextualHelp></p>
                    </logic:notEmpty>
            <%  } %>
        </logic:notEqual>
    
        <div class="horiz-rule"></div>
    
        <h2>Enter the following details to place a Special Order (be as complete as possible):<span class="required"> * Required</span> </h2>
    
        <table style="width:100%">
               
        <tr>
        	<td style="width:175px">
                <h2 class="floatleft"><strong>Permission type selected: </strong></h2>
            </td>
            <td>
            	<h2 style="margin-top:11px">
                    <span class="normaltype">
            	<bean:write name="specialOrderItem" property="categoryName" />
            	</span>
            </h2>
            	        
            </td>
        </tr>
     </table>
     
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
    
        <%
          //  We need to be able to differentiate between a new order and an existing
          //  order when we go back to select another permission.

          String existingOrderParameters = "";

          if (specialOrderFormCOI.getSpecialOrderPurchaseID() > 0) {
            existingOrderParameters = "&exists=1&id=" + specialOrderFormCOI.getSpecialOrderPurchaseID();
          }
        %>
    
        <logic:equal name="allowPermissionChange" value="true">
            <p class="indent-1">
                <bean:define id="selectPermissionPath" name="specialOrderFormCOI" property="selectPermissionPath" />
                <logic:notEmpty name="specialOrderMissingTF">
                    <%
                          existingOrderParameters = "/search.do?operation=detail"
                                                  + existingOrderParameters
                                                  + "&item=" + Long.toString(specialOrderItem.getWrWorkInst());
                        %>
                        <html:link action="<%= existingOrderParameters %>" paramId="item" styleClass="icon-back">Select different permission</html:link>
                </logic:notEmpty>

                <logic:empty name="specialOrderMissingTF">
                    <logic:equal name="specialOrderItem" property="specialOrderFromScratch" value="true">
                        <html:link page="<%= selectPermissionPath + existingOrderParameters %>" styleClass="icon-back">Select different permission</html:link>
                    </logic:equal>
                    <logic:equal name="specialOrderItem" property="specialOrderFromScratch" value="false">
                        <%
                          existingOrderParameters = "/search.do?operation=detail"
                                                  + existingOrderParameters
                                                  + "&item=" + Long.toString(specialOrderItem.getWrWorkInst());
                        %>
                        <html:link action="<%= existingOrderParameters %>" paramId="item" styleClass="icon-back">Select different permission</html:link>
                    </logic:equal>
                </logic:empty>
            </p>
        </logic:equal>
        
        <br />
        
        <tiles:insert name="<%= detailsTileName %>">
            <tiles:put name="allowPermissionChange" beanName="allowPermissionChange" />
        </tiles:insert>
     
        <div class="horiz-rule"></div>
        
                    
    <logic:notEmpty name="specialOrderFormCOI" property="pricingError">
        <p id="pricingErrorLabel">
            <span class="importanttype">Based on your selections, the following applies:</span>
        </p>
        <div id="pricingErrorSection" class='<bean:write name="specialOrderFormCOI" property="pricingError.errorIcon"/>'>
            <bean:define id="errorMessageArg" name="specialOrderFormCOI" property="pricingError.errorMessageArg" type="java.lang.String" />
            <bean:message name="specialOrderFormCOI" property="pricingError.errorMessageKey" arg0="<%= errorMessageArg %>" /><br />
        </div>
    </logic:notEmpty>
    <logic:empty name="specialOrderFormCOI" property="pricingError">
        <p id="pricingErrorLabel" style="display:none"><span class="importanttype">Based on your selections, the following applies:</span></p>
        <div id="pricingErrorSection" style="display:none"></div>
    </logic:empty>
        
        <logic:equal name="showFooterCancel" value="true">
            <span class="floatleft">
                <html:link page="<%= specialCancelPath %>" styleClass="icon-back">Cancel changes</html:link>
            </span>
        </logic:equal>
        
        <logic:equal name="showAddOrderFooterElements" value="true">
            <span class="floatleft">
                <bean:define id="termsOperation">viewTermsAndConditions</bean:define>
                <input type="checkbox" id="termsAndConditionsCheckbox">&nbsp;I accept the <html:link action="<%= specialFormPath %>" paramId="operation" paramName="termsOperation" target="_blank">terms and conditions</html:link>
            </span>
        </logic:equal>
        
        <logic:empty name="specialOrderFormCOI" property="pricingError">
        <span class="floatright">
            <a href="#" onclick="javascript:submitTransaction()" id="submitButtonImage">
                <logic:equal name="specialOrderFormCOI" property="specialFirstAcademicItem" value="true">
                    <html:img src="/media/images/btn_continue.gif" alt="Continue" align="right" />
                </logic:equal>
                <logic:notEqual name="specialOrderFormCOI" property="specialFirstAcademicItem" value="true">
                    <html:img src="<%= submitButtonImage %>" alt="<%= submitButtonAlt %>" align="right" />
                </logic:notEqual>
            </a>
        </span>
        </logic:empty>
        
        <logic:equal name="showAddOrderFooterElements" value="true">
            <br><br>
            <span class="floatright">
                <html:link page="/orderHistory.do" styleClass="icon-forward">Cancel adding to existing order</html:link>
            </span>
        </logic:equal>
        
        <br clear="all" />

    </div>

</html:form>

<!-- Webtrends tags for capturing scenarios -->
<logic:equal name="addWebTrendsScenarios" value="true">
    <logic:notEqual name="specialOrderFormCOI" property="specialFirstAcademicItem" value="true">
        <META name="WT.si_n" content="ShoppingCart">
        <META name="WT.si_p" content="CartView">
    </logic:notEqual>
</logic:equal>
<!-- Webtrends tags for capturing types of use -->
<logic:equal name="addWebTrendsTypeOfUse" value="true">
    <logic:equal name="specialOrderItem" property="photocopy" value="true">
        <META name="WT.pn_fa" content="TRS">
    </logic:equal>
    <logic:equal name="specialOrderItem" property="APS" value="true">
        <META name="WT.pn_fa" content="APS">
    </logic:equal>
    <logic:equal name="specialOrderItem" property="ECCS" value="true">
        <META name="WT.pn_fa" content="ECCS">
    </logic:equal>
    <logic:equal name="specialOrderItem" property="republication" value="true">
        <META name="WT.pn_fa" content="RLS">
    </logic:equal>
    <logic:equal name="specialOrderItem" property="digital" value="true">
        <META name="WT.pn_fa" content="DPS">
    </logic:equal>
</logic:equal>
<!-- end Webtrends tags -->