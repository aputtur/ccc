<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/tld/struts-logic.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/tld/struts-bean.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/cc2-util.tld" %>
<%@ taglib prefix="oue" uri="/WEB-INF/tld/ouextensions.tld" %>

<%@ page errorPage="/jspError.do" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="com.copyright.ccc.web.transaction.TransactionConstants" %>
<%@ page import="com.copyright.ccc.business.data.RLinkPublisher" %>
<%@ page import="com.copyright.ccc.business.services.user.RLinkPublisherServices" %>
<%@ page import="com.copyright.ccc.business.security.UserContextService" %>
<%@ page import="com.copyright.ccc.oue.client.OpenUrlExtensionJspUtils" %>
<%@ page import="com.copyright.ccc.oue.client.OpenUrlExtensionUtils" %>

<!-- tile attribute declarations -->

<tiles:useAttribute id="actionHeaderText" name="actionHeaderText" ignore="true" />
<tiles:useAttribute id="allowPermissionChange" name="allowPermissionChange" ignore="true"/>
<tiles:useAttribute id="submitButtonImage" name="submitButtonImage" ignore="true" classname="java.lang.String" />
<tiles:useAttribute id="submitButtonAlt" name="submitButtonAlt" ignore="true" classname="java.lang.String" />
<tiles:useAttribute id="showFooterCancel" name="showFooterCancel" ignore="true" />
<tiles:useAttribute id="showAddOrderFooterElements" name="showAddOrderFooterElements" ignore="true" />
<tiles:useAttribute id="addWebTrendsScenarios" name="addWebTrendsScenarios" ignore="true" />
<tiles:useAttribute id="addWebTrendsTypeOfUse" name="addWebTrendsTypeOfUse" ignore="true" />
<tiles:useAttribute id="mode" name="mode" ignore="true" />

<!-- end tile attribute declarations -->

<!-- bean declarations -->

<jsp:useBean id="basicTransactionFormCOI" scope="session" class="com.copyright.ccc.web.forms.coi.BasicTransactionForm"/>
<bean:define id="termsTitle">Rightsholder Terms</bean:define>
<bean:define id="transactionItem" name="basicTransactionFormCOI" property="transactionItem" 
    type="com.copyright.ccc.business.data.TransactionItem"/>

<bean:define id="detailsTileName">
    <logic:equal name="transactionItem" property="academic" value="true">academicDetails</logic:equal>
    <logic:equal name="transactionItem" property="photocopy" value="true">photocopyDetails</logic:equal>
    <logic:equal name="transactionItem" property="republication" value="true">republicationDetails</logic:equal>
    <logic:equal name="transactionItem" property="digital" value="true">digitalDetails</logic:equal>
</bean:define>

<bean:define id="detailsExpanded">
    <bean:write name="basicTransactionFormCOI" property="expanded" />
</bean:define>

<bean:define id="formPath" name="basicTransactionFormCOI" property="formPath" type="java.lang.String" />
<bean:define id="cancelPath" name="basicTransactionFormCOI" property="cancelPath" type="java.lang.String" />
<bean:define id="showRightslink" value="yes" type="java.lang.String" />
<%
    //  Do some work to determine whether or not we have a permission
    //  belonging to a rightsholder who is also a RIGHTSLINK consumer.

    long ptyInst = transactionItem.getRightsholderInst();
    RLinkPublisher rlpub = null;
    
    try {
        rlpub = RLinkPublisherServices.getRLinkPublisherByPtyInst(ptyInst);
    }
    catch(Exception e) {
        //  Nothing to do here.  Just swallow any errors.
    }
%>

<!-- end bean declarations -->

<link href="<html:rewrite page="/resources/commerce/css/contextualHelp.css"/>" rel="stylesheet" type="text/css" />

<script src="<html:rewrite page="/resources/commerce/js/util.js"/>" type="text/javascript"></script>
<script src="<html:rewrite page="/resources/commerce/js/contentLoader.js"/>" type="text/javascript"></script>
<script src="<html:rewrite page="/resources/commerce/js/priceCalculator.js"/>" type="text/javascript"></script>
<script src="<html:rewrite page="/resources/commerce/js/validators.js"/>" type="text/javascript"></script>
<script src="<html:rewrite page="/resources/commerce/js/dateUtils.js"/>" type="text/javascript"></script>
<script src="<html:rewrite page="/resources/commerce/js/contextualHelp.js"/>" type="text/javascript"></script>

<script type="text/javascript">

    var BASIC = 0;
    var EXPANDED = 1;
    var ERROR = 2;
    
    var submitButtonImage = "";
    var submitButtonAlt = "";
    var isInSubmit = false;

    //alert("Cancel path: " + "<%= cancelPath %>");
    //alert("Det expand: " + "<%= detailsExpanded %>");
    
    function updatePrice()
    {
        calculatePrice( setPriceAndExpandDetails );
        //return false;
    }
    
    function updatePriceOnLoad()
    {
        calculatePrice( showAndSetPrice );
    }
    
    function calculatePrice( callbackFunction )
    {
        //alert("test");
        var form = document.getElementById("basicTransactionFormCOI");
        
        //alert("form" + form);
                
        document.getElementById("operation").value = "calculatePrice";
        
        var errors = validateForm(form);
        //alert("errors" + errors);
        if(errors.length > 0)
        {
            displayValidationErrors(errors, "clientSideValidationErrorsSection", "clientSideValidationErrorsListSection");
        }
        else
        {
            var rightPermType = document.getElementById("rightPermissionType").value;
            //alert("Right Permission Type: " + rightPermType);
            
            //alert("form path: " + "<%= formPath %>");
                                  
            hideValidationErrors("clientSideValidationErrorsSection");         
                var priceCalculator = new PriceCalculator('<html:rewrite action="<%= formPath %>" />' + "?operation=calculatePrice", 
                                                        "pricingErrorSection");
               // alert("Before Calc price");
               // alert("String form: " + getFormAsString("basicTransactionFormCOI"));
                priceCalculator.calculatePrice( getFormAsString("basicTransactionFormCOI"), callbackFunction );
              //  alert("After Calc price");
            
            if (rightPermType == "C" || rightPermType == "R")          
            {
                showAndSetPrice( "$TBD" );
            }
        }
    }
    
    function placeSpecialOrder()
    {
        document.getElementById("operation").value = "placeSpecialOrder";
        document.getElementById("basicTransactionFormCOI").submit();
        
        return false;
    }
    
    function showAndSetPrice( price )
    {
        document.getElementById("priceSection").style.display = "";
        document.getElementById("price").innerHTML = price;
        document.getElementById("priceSection2").style.display = "";
        document.getElementById("price2").innerHTML = price;
        document.getElementById("updatePriceButton").innerHTML = "Update Price";
    }
    
    function setPriceAndExpandDetails( price )
    {
        document.getElementById("price").innerHTML = price;
        document.getElementById("price2").innerHTML = price;
        showExpandedMode();
    }
    
    function showBasicMode()
    {
        detailsHide();
        
        document.getElementById("priceSection").style.display = "none";
        document.getElementById("priceSection2").style.display = "none";
        document.getElementById("footerDivider").style.display = "";
        document.getElementById("submitButtonLink").style.display = "";
        document.getElementById("submitButtonLink").onclick = updatePrice;
        document.getElementById("submitButtonImage").src = '<html:rewrite href="/media/images/btn_get_price.gif"/>';
        document.getElementById("submitButtonImage").alt = "Get Price";
        document.getElementById("pricingErrorSection").style.display = "none";
        document.getElementById("pricingErrorLabel").style.display = "none";
        document.getElementById("rl").style.display = "none";
        document.getElementById("tileDivider").style.display = "none";
        toggleTermsAndConditionsVisibility( false );
        toggleUseOnlyInNAVisibility( false );
    }
    
    function showExpandedMode()
    {
        detailsExpand();
        
        document.getElementById("priceSection").style.display = "";
        document.getElementById("priceSection2").style.display = "";
        document.getElementById("footerDivider").style.display = "";
        document.getElementById("submitButtonLink").style.display = "";
        document.getElementById("submitButtonLink").onclick = submitTransaction;
        document.getElementById("submitButtonImage").src = submitButtonImage;
        document.getElementById("submitButtonImage").alt = submitButtonAlt;
        document.getElementById("pricingErrorSection").style.display = "none";
        document.getElementById("pricingErrorLabel").style.display = "none";
        document.getElementById("rl").style.display = "none";
        document.getElementById("tileDivider").style.display = "";
        toggleTermsAndConditionsVisibility( true );
        toggleUseOnlyInNAVisibility( true );
        
        return false;
    }
    
    function showErrorMode( errorActionMode )
    {
        detailsError();
        
        document.getElementById("priceSection").style.display = "none";
        document.getElementById("priceSection2").style.display = "none";
        document.getElementById("pricingErrorSection").style.display = "";
        document.getElementById("pricingErrorLabel").style.display = "";
        document.getElementById("rl").style.display = "";
        toggleTermsAndConditionsVisibility( false );
        toggleUseOnlyInNAVisibility( false );
        
        if( errorActionMode == "<%= TransactionConstants.PRICING_ERROR_ACTION_NONE %>" )
        {
            document.getElementById("footerDivider").style.display = "none";
            document.getElementById("submitButtonLink").style.display = "none";
        }
        else if( errorActionMode == "<%= TransactionConstants.PRICING_ERROR_ACTION_SPECIAL_ORDER %>" )
        {
            document.getElementById("footerDivider").style.display = "";
            document.getElementById("submitButtonLink").style.display = "";
            document.getElementById("submitButtonLink").onclick = placeSpecialOrder;
            document.getElementById("submitButtonImage").src = '<html:rewrite href="/media/images/btn_place_special.gif"/>';
            document.getElementById("submitButtonImage").alt = "Place a Special Order";
        }
        else if( errorActionMode == "<%= TransactionConstants.PRICING_ERROR_ACTION_REGULAR_ORDER %>" )
        {
            document.getElementById("footerDivider").style.display = "";
            document.getElementById("submitButtonLink").style.display = "";
            document.getElementById("submitButtonLink").onclick = showExpandedMode;
            document.getElementById("submitButtonImage").src = '<html:rewrite href="/media/images/btn_continue.gif"/>';
            document.getElementById("submitButtonImage").alt = "Continue";
        }
    }
    
    function toggleTermsAndConditionsVisibility( visible )
    {
        var termsAndConditionsSection = document.getElementById("termsAndConditionsSection");
        if( termsAndConditionsSection != null )
            termsAndConditionsSection.style.display = visible ? "" : "none";
    }
    
    function toggleUseOnlyInNAVisibility( visible )
    {
        var useOnlyInNASection = document.getElementById("useOnlyInNASection");
        var useOnlyInNAEmphasis = document.getElementById("useOnlyInNAEmphasis");
        //alert("In Use only in NA" + " Visibility: " + visible + " Null? : " + useOnlyInNASection);
        if( useOnlyInNASection != null )
            useOnlyInNASection.style.display = visible ? "" : "none";
            
        <logic:equal name="basicTransactionFormCOI" property="useOnlyInNA" value="true">
        if( useOnlyInNAEmphasis != null) {
            useOnlyInNAEmphasis.style.background = visible ? "#E8EBEF" : "white";
            useOnlyInNAEmphasis.style.padding = visible ? "17px 8px 18px 8px" : "";
            useOnlyInNAEmphasis.style.border = visible ? "1px solid #666666" : "";
        }
        </logic:equal>
    }
    
    function showValidationErrors( validationErrors )
    {
        displayValidationErrors(validationErrors, "clientSideValidationErrorsSection", "clientSideValidationErrorsListSection");
    }
  
    function getFormAsString(formName){
        
      //Setup the return String
      formString ="";
            
      //Get the form values
      var formElements = document.getElementById(formName).elements;
            
      //loop through the array, building up the url
      //in the format '/strutsaction.do&name=value'
     
      for(var i = 0; i < formElements.length; i++ ){
            
            if(formElements[i].tagName.toLowerCase() == "input")
            {
                if(formElements[i].getAttribute("type") == "radio")
                {
                    if(!formElements[i].checked)
                        continue;
                }
            }
            
            //escape (encode) each value
            formString += "&" 
                + escape(formElements[i].name) + "=" 
                + escape(formElements[i].value);
     }
            
     //return the values
     return formString; 
    }

    function submitTransaction()
    {
        //alert("In submit");
        if( !isInSubmit )
        {
            isInSubmit = true;

          //Need to enable these fields just in case they are disabled by OOP choice
            flds = document.getElementsByName("transactionItem.numberOfPages");
         	fldsPageRange = document.getElementsByName("transactionItem.pageRange");

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
             
             
             
             var form = document.getElementById("basicTransactionFormCOI");
             
             document.getElementById("operation").value = "submit";
            
             var errors = validateForm(form);
    
             if(errors.length > 0)
             {
                <logic:equal name="basicTransactionFormCOI" property="useOnlyInNA" value="true">
                if(!document.getElementById("useOnlyInNACheckbox").checked)
                {
                    alert("This content can only be licensed for reuse in the US or Canada. To confirm your agreement to abide by these terms, mark the confirmation box before continuing.");
                    isInSubmit = false;
                    //return;
                }
             </logic:equal>
             
                displayValidationErrors(errors, "clientSideValidationErrorsSection", "clientSideValidationErrorsListSection");
                isInSubmit = false;
             }
             else
             {
             
                <logic:equal name="basicTransactionFormCOI" property="useOnlyInNA" value="true">
                    if(!document.getElementById("useOnlyInNACheckbox").checked)
                    {
                        alert("This content can only be licensed for reuse in the US or Canada. To confirm your agreement to abide by these terms, mark the confirmation box before continuing.");
                        isInSubmit = false;
                        hideValidationErrors("clientSideValidationErrorsSection");
                        return false;
                    }
                </logic:equal>
             
                hideValidationErrors("clientSideValidationErrorsSection");
                document.getElementById('basicTransactionFormCOI').submit();
             }
         }
         
         return false;
    }
   
</script>



<html:form action="<%= formPath %>" styleId="basicTransactionFormCOI">

    <html:hidden property="operation" styleId="operation" value="" />
    <html:hidden name="basicTransactionFormCOI" property="rightPermissionType" styleId="rightPermissionType"/>

    <!-- Begin Main Content -->
    
    <div style="color:red;padding-bottom:40px;padding-top:10px;display:none" id="clientSideValidationErrorsSection">
        <b>THERE WAS A PROBLEM COMPLETING YOUR REQUEST</b>
        <div style="color:red;padding-left:20px" id="clientSideValidationErrorsListSection"></div>
    </div>
    
    <!-- Begin Left Content -->
    <div id="ecom-boxcontent">
         
    <logic:notEmpty name="actionHeaderText">
        <h1><bean:write name="actionHeaderText"/></h1>
    </logic:notEmpty>
      
    <html:link page="<%= cancelPath %>" styleClass="icon-back"><tiles:getAsString name="cancelText" ignore="true" /></html:link>
               
    <div class="horiz-rule" id="footerDivider"></div>

    <!-- Item Details -->
    <tiles:insert name="bibliographicInformationCOI">
        <tiles:put name="transactionItem" beanName="transactionItem" />
        <tiles:put name="couldShowFees" value="true" />
        <tiles:put name="displayPublicationYearRange" beanName="basicTransactionFormCOI" beanProperty="displayPublicationYearRange" />
    </tiles:insert>
    
    <!-- Item Details -->
    <%-- //TODO: gcuevas 11/1/06: test this once rightsQualifying statement is implemented --%>
    <%  
        //  2009-03-02  MSJ
        //  Adding code for rightsholder terms.
        //  Changed to continue to load generic stuff for DPS and RLS
        //  because we load once, and do not refresh.
        
        String txtTermsApply = null;
    %>
    <logic:notEmpty name="transactionItem" property="externalCommentTerm">
        <bean:define id="termsBody" name="transactionItem" property="externalCommentTerm"/>
        <logic:equal name="transactionItem" property="republication" value="true">
            <%
            txtTermsApply = "Special terms from the rightsholder may apply, depending on your type of use.<br /><br />Please continue order process and special terms will be provided during Order Review, if applicable.";
            pageContext.setAttribute("termsBody", txtTermsApply, PageContext.PAGE_SCOPE);
            %>
        </logic:equal>
        <logic:equal name="transactionItem" property="digital" value="true">
            <%
            txtTermsApply = "Special terms from the rightsholder may apply, depending on your type of use.<br /><br />Please continue order process and special terms will be provided during Order Review, if applicable.";
            pageContext.setAttribute("termsBody", txtTermsApply, PageContext.PAGE_SCOPE);
            %>
        </logic:equal>
    </logic:notEmpty>
    <%  if((transactionItem.isAcademic()
            || transactionItem.isPhotocopy()
            || transactionItem.isRepublication()
            || transactionItem.isDigital()) && transactionItem.getRightsQualifyingStatement() != null && transactionItem.getRightsQualifyingStatement().length() < 100)
        { %>
            <p class="icon-alert clearer">
                <strong><%= transactionItem.getRightsQualifyingStatement() %></strong>&nbsp;
                <logic:notEmpty name="transactionItem" property="externalCommentTerm">
                    <util:contextualHelp titleName="termsTitle" bodyName="termsBody" rollover="true">Rightsholder terms apply</util:contextualHelp>
                </logic:notEmpty>
            </p>
    <%  } %>
    <%  if((transactionItem.isAcademic()
            || transactionItem.isPhotocopy()
            || transactionItem.isRepublication()
            || transactionItem.isDigital()) && transactionItem.getRightsQualifyingStatement() != null && transactionItem.getRightsQualifyingStatement().length() >= 100)
        { %>
            <p class="icon-alert clearer">
                <%= transactionItem.getRightsQualifyingStatement() %>&nbsp;
                <logic:notEmpty name="transactionItem" property="externalCommentTerm">
                    <util:contextualHelp titleName="termsTitle" bodyName="termsBody" rollover="true">Rightsholder terms apply</util:contextualHelp>
                </logic:notEmpty>
            </p>
    <%  } %>
    <%  if(transactionItem.getRightsQualifyingStatement() == null) { %>
            <logic:notEmpty name="transactionItem" property="externalCommentTerm">
                <p class="icon-alert clearer"><util:contextualHelp titleName="termsTitle" bodyName="termsBody" rollover="true">Rightsholder terms apply</util:contextualHelp></p>
            </logic:notEmpty>
    <%  } %>

    
    <%OpenUrlExtensionJspUtils jspUtils = new OpenUrlExtensionJspUtils(request.getSession());
	if(jspUtils.getExtensionData()!=null)
	{
		jspUtils.getExtensionData().setValue(OpenUrlExtensionUtils.ParameterKeys.PERMISSION_NAME, transactionItem.getCategoryName());
		jspUtils.getExtensionData().setValue(OpenUrlExtensionUtils.ParameterKeys.TOU_NAME, transactionItem.getTouName());
 	}
 	
 
    %>  
    <oue:renderDetails/>
     
    <div class="horiz-rule"></div>

    <table style="width:100%">
               
        <tr>
        	<td style="width:175px">
                <h2 class="floatleft"><strong>Permission type selected: </strong></h2>
            </td>
            <td>
            	<h2 style="margin-top:11px">
                    <span class="normaltype">
            	<bean:write name="transactionItem" property="categoryName" />
            	</span>
            </h2>
            	        
            </td>
        
        
        </tr>
        
        <tr>
        	<td style="width:145px">
                <h2 class="floatleft"><strong>Type of use selected: </strong></h2>
            </td>
            <td>
            	<h2 style="margin-top:11px">
                    <span class="normaltype">
            	<bean:write name="transactionItem" property="touName" />
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

      if (basicTransactionFormCOI.getOrderPurchaseID() > 0) {
        existingOrderParameters = "&exists=1&id=" + basicTransactionFormCOI.getOrderPurchaseID();
      }
    %>
    
    <logic:equal name="allowPermissionChange" value="true">
        <p class="clearer">
            <logic:equal name="transactionItem" property="specialOrderFromScratch" value="true">
                <bean:define id="selectPermissionPath" name="basicTransactionFormCOI" property="selectPermissionPathFromSpecial" />
                <html:link page="<%= selectPermissionPath + existingOrderParameters %>" styleClass="icon-back" styleId="997">Select different permission</html:link>
            </logic:equal>
            <logic:equal name="transactionItem" property="specialOrderFromScratch" value="false">
                <%
                  existingOrderParameters = "/search.do?operation=detail"
                                          + existingOrderParameters
                                          + "&item=" + Long.toString(transactionItem.getWrWorkInst());
                %>
                <html:link action="<%= existingOrderParameters %>" styleClass="icon-back" styleId="997">Select different permission</html:link>
            </logic:equal>
            </p>
    </logic:equal>
    
	<table style="width:100%">
        <tr>
            <td align="right">
                
                    <h2 class="floatright" id="priceSection">
                
                        Total Price: <span class="price" id="price"><bean:write name="transactionItem" property="price"/></span>
                </h2>
            </td>
        </tr>
    </table>       
    
    <tiles:insert name="<%= detailsTileName %>">
        <tiles:put name="expanded" value="<%= detailsExpanded %>" />
        <tiles:put name="allowPermissionChange" beanName="allowPermissionChange" />
    </tiles:insert>
    
    <table style="width:100%">
        <tr>
            <td align="right">
                
                    <h2 class="floatright" id="priceSection2">
                
                        Total Price: <span class="price" id="price2"><bean:write name="transactionItem" property="price"/></span>
                </h2>
            </td>
        </tr>
    </table>
    
    <div class="horiz-rule" id="tileDivider"></div>
            
    <logic:notEmpty name="basicTransactionFormCOI" property="pricingError">
        <p id="pricingErrorLabel">
            <span class="importanttype">Based on your selections, the following applies:</span>
        </p>
        <div id="pricingErrorSection" class='<bean:write name="basicTransactionFormCOI" property="pricingError.errorIcon"/>'>
            <bean:define id="errorMessageArg" name="basicTransactionFormCOI" property="pricingError.errorMessageArg" type="java.lang.String" />
            <bean:message name="basicTransactionFormCOI" property="pricingError.errorMessageKey" arg0="<%= errorMessageArg %>" /><br />
        </div>
    </logic:notEmpty>
    <logic:empty name="basicTransactionFormCOI" property="pricingError">
        <p id="pricingErrorLabel" style="display:none"><span class="importanttype">Based on your selections, the following applies:</span></p>
        <div id="pricingErrorSection" style="display:none"></div>
    </logic:empty>
    
               
    <logic:equal name="showFooterCancel" value="true">
        <span class="floatleft">
            <html:link page="<%= cancelPath %>" styleClass="icon-back" styleId="998">Cancel changes</html:link>
        </span>
    </logic:equal>
    
  <div id="useOnlyInNAEmphasis" style="text-align: right; font-size: 11px; font-weight: bold;">
 
     <logic:equal name="showAddOrderFooterElements" value="true">
        <span class="floatleft" id="termsAndConditionsSection">
            <bean:define id="termsOperation">viewTermsAndConditions</bean:define>
            <input type="checkbox" id="termsAndConditionsCheckbox">&nbsp;I accept the <html:link action="<%= formPath %>" paramId="operation" paramName="termsOperation" target="_blank" >terms and conditions</html:link>
        </span>
    </logic:equal><br></br>
    
    <logic:equal name="basicTransactionFormCOI" property="useOnlyInNA" value="true">
        <span class="floatleft" id="useOnlyInNASection">
            <bean:define id="useOnlyInNAOperation">viewUseOnlyInNAConditions</bean:define>
            <input type="checkbox" id="useOnlyInNACheckbox">&nbsp;I confirm that I am going to reuse this content only in the US or Canada 
        </span>
    </logic:equal>
    
    <span class="floatright">
        <logic:equal name="basicTransactionFormCOI" property="firstAcademicItem" value="true">
            <a href="#" id="submitButtonLink">
                <html:img styleId="submitButtonImage" alt="Continue" src="/media/images/btn_continue.gif" align="right" />
            </a>
        </logic:equal>
        <logic:notEqual name="basicTransactionFormCOI" property="firstAcademicItem" value="true">
            <a href="#" id="submitButtonLink">
                <html:img styleId="submitButtonImage" alt="<%= submitButtonAlt %>" src="<%= submitButtonImage %>" align="right" />
            </a>
        </logic:notEqual>
    </span>
    
    <logic:equal name="showAddOrderFooterElements" value="true">
        <br><br>
        <span class="floatright">
            <html:link page="/orderHistory.do" styleClass="icon-forward">Cancel adding to existing order</html:link>
        </span>
    </logic:equal>
    
    <br clear="all" />

  </div> <!-- Use only in NA Div -->
  <span id="rl">
    <% if (rlpub != null) { %>
        <!-- BEGIN RIGHTSLINK INTEGRATION -->
        <%
            //  2009-06-04  MSJ
            //  "showRightslink" is a page-context defined variable
            //  and should be picked up by the logic tag.
            //  
            //  This segment of code is for RIGHTSLINK integration.
            //  We need to define some dynamic bits to fill in the
            //  the content of the popups and such.
        %>
        <bean:define id="rlLearnMoreTitle">Using RIGHTSLINK</bean:define>
        <bean:define id="rlLearnMoreBody">
        <p class="larger">
            <%= rlpub.getLearnMoreDesc() %>
        </p>
        <p class="larger">
            <a href="<%= rlpub.getPubUrl() %>" target="_blank">Go to the publisher&#39;s site now &#62;&#62;</a>
        </p>
        </bean:define>
        
        <bean:define id="rlOptionsTitle">RIGHTSLINK offers a variety of options for using content from <%= rlpub.getPubName() %></bean:define>
        <bean:define id="rlOptionsBody">
        
        <p class="larger">
            <%= rlpub.getPermOptionDesc() %>
        </p>
        </bean:define>
        <table cellpadding="0" cellspacing="0" width="100%" height="100%" style="background-color: #BBC4D0">
    	    <tr><td colspan="6" height="10">&#32;</td></tr>
    	    <tr>
    		    <td rowspan="2"><html:img srcKey="resource.image.redarrow" /></td>
    		    <td width="10">&#32;</td>
    		    <td align="left"><b>Can't find what you're looking for?</b></font></td>
    		    <td align="right">Find your article at the Publisher's site now.</td>
    		    <td width="10">&#32;</td>
    		    <td rowspan="2"><a href="<%= rlpub.getPubUrl() %>" target="_blank"><html:img srcKey="resource.button.godrk" /></a></td>
    	    </tr>
    	    <tr>
    		    <td></td>
    		    <!-- <td width="10">&#32;</td> -->
    		    <td><util:contextualHelp titleName="rlOptionsTitle" bodyName="rlOptionsBody" rollover="true">More permission options</util:contextualHelp> are available on the<br/>publisher&#39;s site using our RIGHTSLINK service.</td>
    		    <td align="right"><small><br/>New to using RIGHTSLINK?&nbsp;&nbsp;<util:contextualHelp titleName="rlLearnMoreTitle" bodyName="rlLearnMoreBody" rollover="false">Learn more.</util:contextualHelp></small></td>
    		    <!-- <td width="10">&#32;</td> -->
    		    <td></td>
    	    </tr>
    	    <tr><td colspan="6" height="10">&#32;</td></tr>
    	</table>
        <!-- END RIGHTSLINK INTEGRATION -->
    <% } %>
  </span>
</div>

<script type="text/javascript">

    //initialize submitButtonImage
    submitButtonImage = document.getElementById("submitButtonImage").src;
    submitButtonAlt = document.getElementById("submitButtonImage").alt;
  
    //set display mode
    <logic:empty name="basicTransactionFormCOI" property="pricingError">
        <logic:equal name="basicTransactionFormCOI" property="expanded" value="false">showBasicMode();</logic:equal>
        <logic:equal name="basicTransactionFormCOI" property="expanded" value="true">showExpandedMode();</logic:equal>
    </logic:empty>
    <logic:notEmpty name="basicTransactionFormCOI" property="pricingError">
        showErrorMode( '<bean:write name="basicTransactionFormCOI" property="pricingError.errorAction" />' );
    </logic:notEmpty>
    
    <logic:equal name="basicTransactionFormCOI" property="calculatePriceOnLoad" value="true">
        updatePriceOnLoad();
    </logic:equal>
    
</script>

</html:form>

<!-- Webtrends tags for capturing scenarios -->
<META name="WT.si_n" content="Checkout">
<logic:equal name="basicTransactionFormCOI" property="expanded" value="false">
	<META name="WT.si_x" content="3">
</logic:equal>
<logic:equal name="basicTransactionFormCOI" property="expanded" value="true">
	<META name="WT.si_x" content="4">
</logic:equal>
<!-- Webtrends tags for capturing types of use -->
<logic:equal name="addWebTrendsTypeOfUse" value="true">
    <logic:equal name="transactionItem" property="photocopy" value="true">
        <META name="WT.pn_fa" content="TRS">
    </logic:equal>
    <logic:equal name="transactionItem" property="APS" value="true">
        <META name="WT.pn_fa" content="APS">
    </logic:equal>
    <logic:equal name="transactionItem" property="ECCS" value="true">
        <META name="WT.pn_fa" content="ECCS">
    </logic:equal>
    <logic:equal name="transactionItem" property="republication" value="true">
        <META name="WT.pn_fa" content="RLS">
    </logic:equal>
    <logic:equal name="transactionItem" property="digital" value="true">
        <META name="WT.pn_fa" content="DPS">
    </logic:equal>
</logic:equal>
<!-- end Webtrends tags -->

