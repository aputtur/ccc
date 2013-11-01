<%@ taglib prefix="bean" uri="/WEB-INF/tld/struts-bean.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/tld/struts-logic.tld" %>
<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/cc2-util.tld" %>

<%@ page errorPage="/jspError.do" %>
<%@ page import="com.copyright.ccc.web.transaction.TransactionConstants" %>
<%@ page import="com.copyright.ccc.web.transaction.RepublicationConstants" %>
<%@ page import="com.copyright.ccc.oue.client.OpenUrlExtensionJspUtils"%>
<%@ page import="com.copyright.ccc.oue.client.OpenUrlExtensionUtils" %>

<%-- variable declarations --%>

<tiles:useAttribute id="expanded" name="expanded" ignore="true" />
<tiles:useAttribute id="allowPermissionChange" name="allowPermissionChange" ignore="true" />

<bean:define id="pricingSectionStyleClass">
    <logic:equal name="expanded" value="true">callout-light indent-1</logic:equal>
    <logic:notEqual name="expanded" value="true">indent-1</logic:notEqual>
</bean:define>

<%-- end variable declarations --%>

<!-- client side validation -->
<util:ccJavascript formName="basicTransactionForm_republication" />
<!-- end client side validation -->

<script type="text/javascript" language="javascript" src="<html:rewrite page="/resources/commerce/js/roman.js"/>"></script>
<script type="text/javascript" language="javascript" src="<html:rewrite page="/resources/commerce/js/parser.js"/>"></script>

<jsp:include page="/WEB-INF/jsp-modules/common/jQueryDatepickerSupport.jsp"/>

<script type="text/javascript">

    var currentActiveElement = "";
    var origStartOfTermDateText = "";
    var isInSubmit = false;
    
    function setOnFocusForAllElements()
    {
        var allElements = document.getElementById("basicTransactionFormCOI").elements;
        
        for( var i = 0; i < allElements.length; i++ )
        {
            allElements[i].onfocus = function(){ setOnFocusForElement( this ); };
        }
    }
    
    function setOnFocusForElement( element )
    {
        var elementId = element.id;
        
        if( (elementId == "republicationDateText") || (elementId == "contentsPublicationDateText") )
            origStartOfTermDateText = element.value;
        else
        {
            if( currentActiveElement == "republicationDateText" ){
                validateStartOfTermDate(document.getElementById("republicationDateText").value);
            } else if (currentActiveElement == "contentsPublicationDateText"){
                validateStartOfTermDate(document.getElementById("contentsPublicationDateText").value);
            }
        }
        
        currentActiveElement = elementId;
    }
    
    function validateStartOfTermDate(startOfTermDate)
    {
        //var startOfTermDate = document.getElementById(elementId).value;
        
        if( startOfTermDate != origStartOfTermDateText )
        {
            if ( !isValidCCDate( startOfTermDate, false ) )
                alert("Please enter a valid date in the format mm/dd/yyyy or m/d/yyyy");
        }
       
    }
    
    function validateForm(form)
    {
        return validateBasicTransactionForm_republication(form);
    }

    function detailsExpand()
    {
        document.getElementById("pricingInstructions").style.display = "none";
        document.getElementById("pricingSection").className = "callout-light indent-1";
        document.getElementById("updatePriceRow").style.display = "";
        document.getElementById("updatePriceButton").style.display = "";
        document.getElementById("updatePriceButton").innerHTML = "Update Price";
        document.getElementById("detailsSection").style.display = "";
        location.href = "#detailsSectionAnchor";
    }
    
    function detailsError()
    {
        document.getElementById("pricingInstructions").style.display = "none";
        document.getElementById("pricingSection").className = "callout-light indent-1";
        document.getElementById("updatePriceRow").style.display = "";
        document.getElementById("updatePriceButton").style.display = "";
        document.getElementById("updatePriceButton").innerHTML = "Update";
        document.getElementById("detailsSection").style.display = "none";
    }
    
    function detailsHide()
    {
        document.getElementById("pricingInstructions").style.display = "none";
        document.getElementById("pricingSection").className = "callout-light indent-1";
        document.getElementById("updatePriceRow").style.display = "none";
        document.getElementById("detailsSection").style.display = "none";
    }
    
    function toggleNumberOfPagesVisibility()
    {
	    if (document.getElementById("typeOfContentSelect").value == "<%= RepublicationConstants.CONTENT_SELECTED_PAGES %>") {
		    document.getElementById("numberOfPagesRow").style.display = "";
		}
		else {
			document.getElementById("numberOfPagesRow").style.display = "none";
			flds = document.getElementsByName("transactionItem.numberOfPages");
            flds[0].value = 0;
		}
		
        //document.getElementById("numberOfPagesRow").style.display = 
        //  (document.getElementById("typeOfContentSelect").value == "<%= RepublicationConstants.CONTENT_SELECTED_PAGES %>") ? "" : "none";
    }
    
    function disableRepublicationTypeOfUseChange()
    {
        document.getElementById("businessSelect").disabled = true;
        document.getElementById("businessLabel").className = "greytype";
        document.getElementById("businessSelect").className = "greytype";
    }
    
    function validatePageRange(pageRangeField) {
    	var pageCount = -1;
    	
		if (pageRangeField.value == null || pageRangeField.value == "") {
			alert("Page Range(s) is a required field.");
			pageRangeField.focus();
		}
		else {
			pageCount = getPageCount(pageRangeField.value);
			if (pageCount != -1) {
			    tous = document.getElementsByName("transactionItem.typeOfContent");
			    
			    switch(tous[0].value) {
			        case "<%= RepublicationConstants.CONTENT_SELECTED_PAGES %>":
			        case "<%= RepublicationConstants.CONTENT_CARTOONS %>":
			            flds = document.getElementsByName("transactionItem.numberOfPages");
                                flds[0].value = pageCount;
                        break;
                    default:
                        //  Do nothing at all.
                        break;
			    }
			}
			else {
				alert("You have entered an invalid page range.");
				pageRangeField.focus();
			}
		}
    }
    
</script>



<%-- pricing section --%>

<%OpenUrlExtensionJspUtils jspUtils = new OpenUrlExtensionJspUtils(request.getSession());
	if(jspUtils.getExtensionData()!=null)
	{
		jspUtils.getExtensionData().setValue(OpenUrlExtensionUtils.ParameterKeys.PERMISSION_NAME,request.getParameter("perm"));
 	}
 %>

<h2 id="pricingInstructions" class="padtop">
    Please enter details of intended use to determine availability and price:<span class="required"> * Required</span>
</h2>

<div id="pricingSection" class="<%= pricingSectionStyleClass %>">

    <p class="normaltype bold">Information about the new work you are creating:</p>
		
    <table border="0" class="indent-1">
    
       	<tr id="updatePriceRow">
    	<td></td>
    	<td style="width:450px"><div style="float:left;text-align:left"><span class="required"> * Required</span></div><div style="float:right;text-align:right;"><a class="btn-yellow" href="javascript:updatePrice()" id="updatePriceButton">Update Price</a></div>
    	</td>
        </tr>
        
        
        <tr>
            <td>The total circulation/distribution will be: <span class="importanttype">*</span></td>
            <td>
                <logic:greaterThan value="0" name="basicTransactionFormCOI" property="transactionItem.circulationDistribution">
                    <html:text name="basicTransactionFormCOI" property="transactionItem.circulationDistribution" size="6" maxlength="6"/>
                </logic:greaterThan>
                <logic:lessEqual value="0" name="basicTransactionFormCOI" property="transactionItem.circulationDistribution">
                    <html:text name="basicTransactionFormCOI" property="transactionItem.circulationDistribution" value=""  size="6" maxlength="6"/>
                </logic:lessEqual>
                <util:contextualHelp titleName="basicTransactionFormCOI" titleProperty="circulationDistributionTitle" bodyName="basicTransactionFormCOI" bodyProperty="circulationDistributionText" rollover="false"><u>What's this?</u></util:contextualHelp>
            </td>
        </tr>
        
        <tr>
            <td id="businessLabel">Republishing publisher is: <span class="importanttype">*</span></td>
            <td>
                <html:select styleClass="select01" name="basicTransactionFormCOI" property="transactionItem.business" styleId="businessSelect">
                    <html:option value="<%= RepublicationConstants.BUSINESS_FOR_PROFIT %>">For-profit</html:option>
                    <html:option value="<%= RepublicationConstants.BUSINESS_NON_FOR_PROFIT %>">Non-profit 501(c)(3)</html:option>
                </html:select>
                <logic:equal name="allowPermissionChange" value="false">
                    <span style="padding-left:60px"><util:contextualHelp helpId="30" rollover="false">Why can't I change this?</util:contextualHelp></span>
                </logic:equal>
            </td>
        </tr>
    </table>

    <p class="normaltype bold">Information about content to be republished: </p>
		
    <table border="0" class="indent-1">
    
        <tr>
            <td width="330">Portion to be used: <span class="importanttype">*</span></td>
            <td><div align="left">
                <html:select styleClass="select01" name="basicTransactionFormCOI" property="transactionItem.typeOfContent" styleId="typeOfContentSelect"
                    onchange="javascript:toggleNumberOfPagesVisibility()">
                    <html:option value="">Choose one</html:option>
                    <html:option value="<%= RepublicationConstants.CONTENT_FULL_ARTICLE_CHAPTER %>">Full article/chapter (text only)</html:option>
                    <html:option value="<%= RepublicationConstants.CONTENT_EXCERPT %>">An excerpt</html:option>
                    <html:option value="<%= RepublicationConstants.CONTENT_QUOTATION %>">A quotation</html:option>
                    <html:option value="<%= RepublicationConstants.CONTENT_SELECTED_PAGES %>">Selected pages</html:option>
                    <html:option value="<%= RepublicationConstants.CONTENT_CHART %>">A chart</html:option>
                    <html:option value="<%= RepublicationConstants.CONTENT_GRAPH %>">A graph</html:option>
                    <html:option value="<%= RepublicationConstants.CONTENT_FIGURE_DIAGRAM_TABLE %>">A figure/ diagram/ table</html:option>
                    <html:option value="<%= RepublicationConstants.CONTENT_PHOTOGRAPH %>">A photograph</html:option>
                    <html:option value="<%= RepublicationConstants.CONTENT_CARTOONS %>">A cartoon</html:option>
                    <html:option value="<%= RepublicationConstants.CONTENT_ILLUSTRATION %>">An illustration</html:option>
                </html:select>
            </div></td>
            <td>
                   <util:contextualHelp titleName="basicTransactionFormCOI" titleProperty="republishPortionTitle" bodyName="basicTransactionFormCOI" bodyProperty="republishPortionText" rollover="false"><u>What's this?</u></util:contextualHelp>
            </td>
        </tr>
        
        <tr>
            <td>I am the author of this content: <span class="importanttype">*</span></td>
            <td>
                <html:select styleClass="select01" name="basicTransactionFormCOI" property="transactionItem.submitterAuthor">
                    <html:option value="true">Yes</html:option>
                    <html:option value="false">No</html:option>
                </html:select>
            </td>
        </tr>
        
        <tr>
            <td>Publication date of the content:<span class="importanttype">*</span></td>
            <td>
            	<% if(jspUtils.isValidOUESession()){ %>
            		<html:text name="basicTransactionFormCOI" property="transactionItem.contentsPublicationDate" styleClass="small" styleId="contentsPublicationDateText" value="<%=jspUtils.displayContentFullDate() %>"/>
                <%}else{ %>
                <logic:empty name="basicTransactionFormCOI" property="transactionItem.contentsPublicationDate">
                    <html:text name="basicTransactionFormCOI" property="transactionItem.contentsPublicationDate" styleClass="small" styleId="contentsPublicationDateText" value="MM/DD/YYYY"/>
                </logic:empty>
                <logic:notEmpty name="basicTransactionFormCOI" property="transactionItem.contentsPublicationDate">
                    <bean:define id="formattedContentsPublicationDate">
                        <bean:write name="basicTransactionFormCOI" property="transactionItem.contentsPublicationDate" format="MM/dd/yyyy" />
                    </bean:define>
                    <html:text name="basicTransactionFormCOI" property="transactionItem.contentsPublicationDate" 
                        value="<%= formattedContentsPublicationDate %>" styleClass="small" styleId="contentsPublicationDateText"/>
                </logic:notEmpty>
                <%} %>
             </td>
            
        </tr>
                
        <tr id="numberOfPagesRow">
            <td>Number of pages to be used: <span class="importanttype">*</span></td>
            <td>
                <logic:greaterThan value="0" name="basicTransactionFormCOI" property="transactionItem.numberOfPages">
                    <html:text name="basicTransactionFormCOI" property="transactionItem.numberOfPages" styleClass="normal" />
                </logic:greaterThan>
                <logic:lessEqual value="0" name="basicTransactionFormCOI" property="transactionItem.numberOfPages">
                    <html:text name="basicTransactionFormCOI" property="transactionItem.numberOfPages" styleClass="normal" value="" />
                </logic:lessEqual>
            </td>
        </tr>
        
        
                        
  </table>
  
</div>

<%-- end pricing section --%>

<a name="detailsSectionAnchor"></a>
<div id="detailsSection">

    <h2>Please enter the following details to continue:<span class="required"> * Required</span> </h2>

    <p class="indent-1"><em>Additional information about the new work you are creating:</em></p>
    
    <table border="0" class="indent-2">

        <tr>
            <td width="239"><div align="left">Organization republishing this work: <span class="importanttype">*</span></div></td>
            <td><div align="left">
                <html:text name="basicTransactionFormCOI" property="transactionItem.republishingOrganization" styleClass="normal" maxlength="250"/>
            </div></td>
        </tr>
        
        <tr>
            <td><div align="left">Title of the new publication: </div></td>
            <td>
                <html:text name="basicTransactionFormCOI" property="transactionItem.newPublicationTitle" styleClass="normal" maxlength="250"/>
            </td>
        </tr>
        
        <tr>
            <td><div align="left">Date this content will be republished: <span class="importanttype">*</span></div></td>
            <td><div align="left">
                <logic:empty name="basicTransactionFormCOI" property="transactionItem.republicationDate">
                    <html:text name="basicTransactionFormCOI" property="transactionItem.republicationDate" styleClass="small" styleId="republicationDateText" value="MM/DD/YYYY"/>
                </logic:empty>
                <logic:notEmpty name="basicTransactionFormCOI" property="transactionItem.republicationDate">
                    <bean:define id="formattedRepublicationDate">
                        <bean:write name="basicTransactionFormCOI" property="transactionItem.republicationDate" format="MM/dd/yyyy" />
                    </bean:define>
                    <html:text name="basicTransactionFormCOI" property="transactionItem.republicationDate" value="<%= formattedRepublicationDate %>" 
                        styleClass="small" styleId="republicationDateText"/>
                </logic:notEmpty>
            </div></td>
            
        </tr>
        
        <tr>
            <td><div align="left">I am translating this material to: </div></td>
            <td><div align="left">
                <html:select name="basicTransactionFormCOI" property="transactionItem.translationLanguage" styleClass="select01">
                    <html:option value="<%= RepublicationConstants.NO_TRANSLATION %>">No Translation</html:option>
                    <html:option value="<%= RepublicationConstants.TRANSLATED_LANGUAGE_CHINESE %>">Chinese</html:option>
                    <html:option value="<%= RepublicationConstants.TRANSLATED_LANGUAGE_ENGLISH %>">English</html:option>
                    <html:option value="<%= RepublicationConstants.TRANSLATED_LANGUAGE_FRENCH %>">French</html:option>
                    <html:option value="<%= RepublicationConstants.TRANSLATED_LANGUAGE_GERMAN %>">German</html:option>
                    <html:option value="<%= RepublicationConstants.TRANSLATED_LANGUAGE_ITALIAN %>">Italian</html:option>
                    <html:option value="<%= RepublicationConstants.TRANSLATED_LANGUAGE_JAPANESE %>">Japanese</html:option>
                    <html:option value="<%= RepublicationConstants.TRANSLATED_LANGUAGE_PORTUGUESE %>">Portuguese</html:option>
                    <html:option value="<%= RepublicationConstants.TRANSLATED_LANGUAGE_RUSSIAN %>">Russian</html:option>
                    <html:option value="<%= RepublicationConstants.TRANSLATED_LANGUAGE_SPANISH %>">Spanish</html:option>
                    <html:option value="<%= RepublicationConstants.TRANSLATED_LANGUAGE_OTHER %>">Other</html:option>
                </html:select>
            </div></td>
        </tr>
        
        <tr>
            <td><div align="left">Your reference:</div></td>
            <td><div align="left">
                <html:text name="basicTransactionFormCOI" property="transactionItem.yourReference" styleClass="normal" maxlength="50"/>
                <span class="smalltype">(Example: "Mary's thesis, chapter 7")</span>
            </div></td>
        </tr>
        
    </table>

    <p class="indent-1"><em>Additional information about content to be republished:</em></p>

    <table border="0" class="indent-2">
    
        <tr>
            <td width="290">Title or description of content you intend to use: <span class="importanttype">*</span></td>
            <td><div align="left">
                <html:text name="basicTransactionFormCOI" property="transactionItem.chapterArticle" value="<%=jspUtils.displayContentTitle()%>" styleClass="normal" maxlength="250" />
            </div></td>
        </tr>
        
        <tr>
            <td>Author or creator of content you intend to use: <span class="importanttype">*</span></td>
            <td>
            <%if(jspUtils.isValidOUESession()){ %>
                <html:text name="basicTransactionFormCOI" property="transactionItem.customAuthor" value="${transactionItem.publicationTitle}" styleClass="normal" maxlength="250" />
            <%}else{ %>
            	<html:text name="basicTransactionFormCOI" property="transactionItem.customAuthor" styleClass="normal" maxlength="250" />
            <%} %>    
                
            </td>
        </tr>
        
        <tr>
            <td>Page range of content that you intend to use: <span class="importanttype">*</span></td>
            <td>
                <html:text name="basicTransactionFormCOI" property="transactionItem.pageRange" value="<%=jspUtils.displayDefaultPageNumberRange()%>" styleClass="normal" maxlength="80" onchange="validatePageRange(this)"/>
                <span class="smalltype">(Examples: ii, iv-vi or 3, 7-10) </span>
            </td>
        </tr>
        
    </table>

</div>

<script type="text/javascript">

    var isIE = navigator.userAgent.indexOf("MSIE") > 0;
    
    //set global onfocus event to determine which element is focused
    setOnFocusForAllElements();


    toggleNumberOfPagesVisibility();
    
    <logic:equal name="allowPermissionChange" value="false">
        disableRepublicationTypeOfUseChange();
    </logic:equal>
    
 	$(document).ready(function(){

	 	jqCalendarDatepickerCreate('contentsPublicationDateText','<html:rewrite href='/media/images/calendar.gif'/>');
	 	jqCalendarDatepickerSetYearMenuRange('contentsPublicationDateText','-10:+0');
	 	jqCalendarDatepickerSetMaxDate( 'contentsPublicationDateText', '+0y' );

	 	jqCalendarDatepickerCreate('republicationDateText','<html:rewrite href='/media/images/calendar.gif'/>');
	 	jqCalendarDatepickerSetYearMenuRange('republicationDateText','-0:+10');
	 	jqCalendarDatepickerSetMinDate( 'republicationDateText', '+0y' );
	 	
	});
	
</script>

