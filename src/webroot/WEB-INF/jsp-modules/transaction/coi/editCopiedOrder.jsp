<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/tld/struts-logic.tld" %>
<%@ taglib prefix="nested" uri="/WEB-INF/tld/struts-nested.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/tld/struts-bean.tld" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/cc2-util.tld" %>

<%@ page import="com.copyright.ccc.business.services.cart.PurchasablePermission" %>
<%@ page import="com.copyright.ccc.web.transaction.RepublicationConstants" %>
<%@ page import="com.copyright.data.order.UsageDataNet" %>

<style type="text/css">
    
    .itemFooter
    {
        padding-bottom:10px;
        padding-top:10px;
    }
    
    .footerAction
    {
        padding-top:7px;
    }
    
</style>

<script src="<html:rewrite page="/resources/commerce/js/util.js"/>" type="text/javascript"></script>
<script src="<html:rewrite page="/resources/commerce/js/validators.js"/>" type="text/javascript"></script>
<script type="text/javascript">

    var objectsToValidate = [];

    var isBeingSubmitted = false;
    
    function copyOrder()
    {
        if( !isBeingSubmitted )
        {
            isBeingSubmitted = true;
            
            var validationErrors = validateValues();
            
            if( validationErrors == null || validationErrors.length == 0)
            {
                hideValidationErrors("clientSideValidationErrorsSection");
                
                document.getElementById("operation").value = "copyOrder";
                document.getElementById("editCopiedOrderForm").submit();
            }
            else
            {
                isBeingSubmitted = false;
                displayValidationErrors(validationErrors, "clientSideValidationErrorsSection", "clientSideValidationErrorsListSection");
            }
        }
        
        return false;
    }
    
    var isBeingRemoved = false;
    
    function removePermission( permissionIndex )
    {
        if ( !isBeingRemoved )
        {
            isBeingRemoved = true;
            
            document.getElementById("indexOfPermissionToRemove").value = permissionIndex;
            document.getElementById("operation").value = "removePermission";
            document.getElementById("editCopiedOrderForm").submit();
        }
        
        return false;
    }
    
    function validateValues()
    {
        var validationErrors = [];
        var validationErrorsIndex = 0;
        
        for( var i = 0; i < objectsToValidate.length; i++ )
        {
            var currentObject = objectsToValidate[i];
            
            var input = document.getElementById(currentObject.inputId);
            var inputValue = trim(input.value);
            
            var isANumber= true;
            var isAValidNumber = true;
            if (!isAllDigits(inputValue)) {
                isANumber = false;
            } else {
                var iValue = parseInt(inputValue);
                if (isNaN(iValue) || !(iValue >= -2147483648 && iValue <= 2147483647))
                    isANumber = false;
                if( iValue <= currentObject.maxInvalidValue )
                    isAValidNumber = false;
            }
            
            if( !isANumber )
            {
                validationErrors[validationErrorsIndex] = "Please enter a number for the <b>" + currentObject.fieldName + "</b> for " + currentObject.title;
                validationErrorsIndex++;
            }
            else if( !isAValidNumber )
            {
                if( currentObject.isSelect )
                    validationErrors[validationErrorsIndex] = "Please select a value for the <b>" + currentObject.fieldName + "</b> for " + currentObject.title;
                else
                    validationErrors[validationErrorsIndex] = "Please enter a valid number for the <b>" + currentObject.fieldName + "</b> for " + currentObject.title;
                validationErrorsIndex++;
            }
        }
        
        return validationErrors;
    }
    
</script>

<html:form action="/editCopiedOrder.do" styleId="editCopiedOrderForm" >

<bean:define id="permissionsToEdit" name="editCopiedOrderForm" property="permissionsToEdit" type="PurchasablePermission[]" />
<bean:define id="permissionsToEditLength"><%= permissionsToEdit.length %></bean:define>

<html:hidden property="operation" styleId="operation" value="" />
<html:hidden name="editCopiedOrderForm" property="indexOfPermissionToRemove" styleId="indexOfPermissionToRemove"/>

<div style="color:red;padding-bottom:40px;padding-top:10px;display:none" id="clientSideValidationErrorsSection">
    <b>THERE WAS A PROBLEM COMPLETING YOUR REQUEST</b>
    <div style="color:red;padding-left:20px" id="clientSideValidationErrorsListSection"></div>
</div>

<div id="ecom-boxcontent">

    <h1>Edit Copied Items</h1>
    
    <html:link action="/orderHistory.do" styleClass="icon-back">Cancel</html:link>
    
    <logic:greaterThan value="0" name="permissionsToEditLength">
        <p>
            The following items were cancelled in your original order.<br>
            <br>
            Please indicate the correct quantity and click continue, or click "remove" if you do not wish to copy this item into your shopping cart.
        </p>
    </logic:greaterThan>
    
    <div class="clearer"></div>
    

    <table style="border-collapse:collapse;padding-top:30px">
    
        <tr>
            <td colspan="2">&nbsp;</td>
        </tr>

        <logic:equal value="0" name="permissionsToEditLength">
            <tr>
                <td colspan="2">All canceled items have been updated or removed.  Please select the continue button to view and checkout the remaining items in your shopping cart.</td>
            </tr>
        </logic:equal>
        
        <nested:iterate id="item" name="editCopiedOrderForm" property="permissionsToEdit" indexId="index" type="PurchasablePermission">
        
            <bean:define id="itemNumber"><%= index.intValue() + 1 %>.</bean:define>
        
            <!-- cart item title -->
            <tr class="search-result-title">
                <td class="search-result-basic"><h2><bean:write name="itemNumber"/></h2></td>
                <td class="search-result-basic"><h2>
                <logic:notEqual name="item" property="wrWorkInst" value="0">
                     <html:link action="/search.do?operation=detail&backlink=false" paramId="item" paramName="item" paramProperty="wrWorkInst">
                    <bean:write name="item" property="publicationTitle" /></html:link>
                </logic:notEqual>
                <logic:equal name="item" property="wrWorkInst" value="0">
                     <a href="#"><bean:write name="item" property="publicationTitle" /></a>
                </logic:equal>
                </h2></td>
            </tr>
            
            <!-- item details -->
            <tr>
                <td>&nbsp;</td>
                <td>
                    <table cellpadding="0" cellspacing="0" border="0" class="fixedWidth" style="width:680px">
                        <col style="width:280px">
                        <col style="width:400px">
                        <tr>
                            <td>
                                <logic:notEmpty name="item" property="standardNumber">
                                    <strong>ISBN/ISSN: </strong><bean:write name="item" property="standardNumber" /><br />
                                </logic:notEmpty>
                                <logic:equal name="item" property="republication" value="true">
                                    <logic:notEmpty name="item" property="contentsPublicationDate">
                                        <strong>Publication year: </strong><bean:write name="item" property="contentsPublicationDate" format="yyyy" /><br />
                                    </logic:notEmpty>
                                </logic:equal>
                                <logic:equal name="item" property="republication" value="false">
                                    <logic:notEmpty name="item" property="publicationYearOfUse">
                                        <strong>Publication year: </strong><bean:write name="item" property="publicationYearOfUse" /><br />
                                    </logic:notEmpty>
                                </logic:equal>
                                <logic:notEmpty name="item" property="publisher">
                                    <strong>Publisher: </strong><util:fieldWrap bean="item" property="publisher" width="20" filter="true" useWordBreak="true"/><br />
                                </logic:notEmpty>
                                <logic:notEmpty name="item" property="rightsholder">
                                    <strong>Rightsholder: </strong><util:fieldWrap bean="item" property="rightsholder" width="20" filter="true" useWordBreak="true"/><br />
                                </logic:notEmpty>
                                <% if( !item.isPhotocopy() ) { %>
                                    <logic:notEmpty name="item" property="customAuthor">
                                        <strong>Author/Editor: </strong><util:fieldWrap bean="item" property="customAuthor" width="20" filter="true" useWordBreak="true"/><br />
                                    </logic:notEmpty>
                                <% } %>
                                <% if(  item.isSpecialOrderFromScratch() && (item.isPhotocopy() || item.isRepublication() ) ) {%>
                                    <logic:notEmpty name="item" property="customVolume">
                                        <strong>Volume: </strong><util:fieldWrap bean="item" property="customVolume" width="20" filter="true" useWordBreak="true"/><br />
                                    </logic:notEmpty>
                                <% } %>
                                <% if( item.isSpecialOrderFromScratch() && item.isPhotocopy() ) { %>
                                    <logic:notEmpty name="item" property="customEdition">
                                        <strong>Edition: </strong><util:fieldWrap bean="item" property="customEdition" width="20" filter="true" useWordBreak="true"/>
                                    </logic:notEmpty>
                                <% } %>
                            </td>
                            <td>
                                
                                <logic:equal name="item" property="photocopy" value="true">
                                    <table class="pertype">
                                        <tr>
                                            <td class="pertypefield"><strong>Permission type:</strong></td>
                                            <td class="pertypetxt">
                                                Photocopy for general business use, library reserves, ILL/document delivery&hellip;
                                            </td>
                                        </tr>
                                    </table>
                                    <br />
                                
                                    <logic:notEmpty name="item" property="chapterArticle">
                                        <strong>&nbsp;Article/Chapter: </strong><util:fieldWrap bean="item" property="chapterArticle" width="20" filter="true" useWordBreak="true"/><br />
                                    </logic:notEmpty>
                                    <logic:notEmpty name="item" property="numberOfPages">
                                        <strong>&nbsp;Total number of pages: </strong><bean:write name="item" property="numberOfPages" /><br />
                                    </logic:notEmpty>
                                    <logic:notEmpty name="item" property="customerReference">
                                        <strong>&nbsp;Your line item reference: </strong><util:fieldWrap bean="item" property="customerReference" width="20" filter="true" useWordBreak="true"/>
                                    </logic:notEmpty>
                                </logic:equal>
                                
                                <logic:equal name="item" property="digital" value="true">
                                    <table class="pertype">
                                        <tr>
                                            <td class="pertypefield"><strong>Permission type:</strong></td>
                                            <td class="pertypetxt">
                                                Use in e-mail, intranet/extranet/Internet postings&hellip;
                                            </td>
                                        </tr>
                                    </table>
                                    <br />
                                    
                                    <bean:define id="digitalTypeOfUseString">
                                        <logic:equal name="item" property="email" value="true">E-mail</logic:equal>
                                        <logic:equal name="item" property="intranet" value="true">Intranet</logic:equal>
                                        <logic:equal name="item" property="extranet" value="true">Extranet</logic:equal>
                                        <logic:equal name="item" property="internet" value="true">Internet</logic:equal>
                                    </bean:define>
                                    <strong>&nbsp;Requested use: </strong><bean:write name="digitalTypeOfUseString"/><br />
                                    
                                    <logic:notEmpty name="item" property="chapterArticle">
                                        <strong>&nbsp;Article/Chapter: </strong><util:fieldWrap bean="item" property="chapterArticle" width="20" filter="true" useWordBreak="true"/><br />
                                    </logic:notEmpty>
                                    
                                    <logic:equal name="item" property="email" value="true">
                                        <logic:notEmpty name="item" property="dateOfUse">
                                            <strong>&nbsp;Date e-mail will be sent: </strong><bean:write name="item" property="dateOfUse" format="MM/dd/yyyy" /><br />
                                        </logic:notEmpty>
                                    </logic:equal>
                                    
                                    <logic:equal name="item" property="net" value="true">
                                        <logic:notEmpty name="item" property="dateOfUse">
                                            <strong>&nbsp;Date to be posted: </strong><bean:write name="item" property="dateOfUse" format="MM/dd/yyyy" /><br />
                                        </logic:notEmpty>
                                        
                                        <logic:equal name="item" property="internet" value="true">
                                            <logic:notEmpty name="item" property="webAddress">
                                                <strong>&nbsp;URL of posting: </strong><util:fieldWrap bean="item" property="webAddress" width="20" filter="true" useWordBreak="true"/><br />
                                            </logic:notEmpty>
                                        </logic:equal>
                                    </logic:equal>
                                    
                                    <logic:notEmpty name="item" property="customerReference">
                                        <strong>&nbsp;Your reference: </strong><util:fieldWrap bean="item" property="customerReference" width="20" filter="true" useWordBreak="true"/>
                                    </logic:notEmpty>
                                </logic:equal>
                                
                                <logic:equal name="item" property="republication" value="true">
                                    <table class="pertype">
                                        <tr>
                                            <td class="pertypefield"><strong>Permission type:</strong></td>
                                            <td class="pertypetxt">
                                                Republish into a book, journal, newsletter&hellip;
                                            </td>
                                        </tr>
                                    </table>
                                    <br />
                                    
                                    <logic:notEmpty name="item" property="republicationTypeOfUse">
                                        <bean:define id="republicationTypeOfUseString">
                                            <logic:equal name="item" property="republicationTypeOfUse" value="<%= RepublicationConstants.REPUBLICATION_BROCHURE %>">Brochure</logic:equal>
                                            <logic:equal name="item" property="republicationTypeOfUse" value="<%= RepublicationConstants.REPUBLICATION_CDROM %>">CD-ROM</logic:equal>
                                            <logic:equal name="item" property="republicationTypeOfUse" value="<%= RepublicationConstants.REPUBLICATION_DISSERTATION %>">Dissertation</logic:equal>
                                            <logic:equal name="item" property="republicationTypeOfUse" value="<%= RepublicationConstants.REPUBLICATION_DVD %>">DVD</logic:equal>
                                            <logic:equal name="item" property="republicationTypeOfUse" value="<%= RepublicationConstants.REPUBLICATION_JOURNAL %>">Journal</logic:equal>
                                            <logic:equal name="item" property="republicationTypeOfUse" value="<%= RepublicationConstants.REPUBLICATION_MAGAZINE %>">Magazine</logic:equal>
                                            <logic:equal name="item" property="republicationTypeOfUse" value="<%= RepublicationConstants.REPUBLICATION_NEWSLETTER %>">Newsletter/E-Newsletter</logic:equal>
                                            <logic:equal name="item" property="republicationTypeOfUse" value="<%= RepublicationConstants.REPUBLICATION_NEWSPAPER %>">Newspaper</logic:equal>
                                            <logic:equal name="item" property="republicationTypeOfUse" value="<%= RepublicationConstants.REPUBLICATION_OTHERBOOK %>">Other Book</logic:equal>
                                            <logic:equal name="item" property="republicationTypeOfUse" value="<%= RepublicationConstants.REPUBLICATION_PAMPHLET %>">Pamphlet</logic:equal>
                                            <logic:equal name="item" property="republicationTypeOfUse" value="<%= RepublicationConstants.REPUBLICATION_PRESENTATION %>">PowerPoint Presentation</logic:equal>
                                            <logic:equal name="item" property="republicationTypeOfUse" value="<%= RepublicationConstants.REPUBLICATION_TEXTBOOK %>">Textbook</logic:equal>
                                            <logic:equal name="item" property="republicationTypeOfUse" value="<%= RepublicationConstants.REPUBLICATION_TRADEBOOK %>">Trade Book</logic:equal>
                                        </bean:define>
                                        <strong>&nbsp;Requested use: </strong><bean:write name="republicationTypeOfUseString" /><br />
                                    </logic:notEmpty>
                                    <logic:notEmpty name="item" property="newPublicationTitle">
                                        <strong>&nbsp;Republication title: </strong><util:fieldWrap bean="item" property="newPublicationTitle" width="20" filter="true" useWordBreak="true"/><br />
                                    </logic:notEmpty>
                                    <logic:notEmpty name="item" property="republishingOrganization">
                                        <strong>&nbsp;Republishing organization: </strong><util:fieldWrap bean="item" property="republishingOrganization" width="20" filter="true" useWordBreak="true"/><br />
                                    </logic:notEmpty>
                                    <logic:notEmpty name="item" property="business">
                                        <bean:define id="businessString">
                                            <logic:equal name="item" property="business" value="<%= RepublicationConstants.BUSINESS_FOR_PROFIT %>">For-profit</logic:equal>
                                            <logic:notEqual name="item" property="business" value="<%= RepublicationConstants.BUSINESS_FOR_PROFIT %>">Non-profit 501(c)(3)</logic:notEqual>
                                        </bean:define>
                                        <strong>&nbsp;Organization status: </strong><bean:write name="businessString" /><br />
                                    </logic:notEmpty>
                                    <logic:notEmpty name="item" property="republicationDate">
                                        <strong>&nbsp;Republication date: </strong><bean:write name="item" property="republicationDate" format="MM/dd/yyyy" /><br />
                                    </logic:notEmpty>
                                    <logic:notEmpty name="item" property="typeOfContent">
                                        <bean:define id="typeOfContentString">
                                            <logic:equal name="item" property="typeOfContent" value="<%= RepublicationConstants.CONTENT_FULL_ARTICLE_CHAPTER %>">Full article/chapter</logic:equal>
                                            <logic:equal name="item" property="typeOfContent" value="<%= RepublicationConstants.CONTENT_EXCERPT %>">Excerpt</logic:equal>
                                            <logic:equal name="item" property="typeOfContent" value="<%= RepublicationConstants.CONTENT_QUOTATION %>">Quotation</logic:equal>
                                            <logic:equal name="item" property="typeOfContent" value="<%= RepublicationConstants.CONTENT_SELECTED_PAGES %>">Selected pages</logic:equal>
                                            <logic:equal name="item" property="typeOfContent" value="<%= RepublicationConstants.CONTENT_CHART %>">Chart</logic:equal>
                                            <logic:equal name="item" property="typeOfContent" value="<%= RepublicationConstants.CONTENT_GRAPH %>">Graph</logic:equal>
                                            <logic:equal name="item" property="typeOfContent" value="<%= RepublicationConstants.CONTENT_FIGURE_DIAGRAM_TABLE %>">Figure/ diagram/ table</logic:equal>
                                            <logic:equal name="item" property="typeOfContent" value="<%= RepublicationConstants.CONTENT_PHOTOGRAPH %>">Photograph</logic:equal>
                                            <logic:equal name="item" property="typeOfContent" value="<%= RepublicationConstants.CONTENT_CARTOONS %>">Cartoon</logic:equal>
                                            <logic:equal name="item" property="typeOfContent" value="<%= RepublicationConstants.CONTENT_ILLUSTRATION %>">Illustration</logic:equal>
                                        </bean:define>
                                        <strong>&nbsp;Type of content: </strong><bean:write name="typeOfContentString" /><br />
                                    </logic:notEmpty>
                                    <logic:notEmpty name="item" property="chapterArticle">
                                        <strong>&nbsp;Description of requested content: </strong><util:fieldWrap bean="item" property="chapterArticle" width="20" filter="true" useWordBreak="true"/><br />
                                    </logic:notEmpty>
                                    <logic:notEmpty name="item" property="pageRange">
                                        <strong>&nbsp;Page range(s): </strong><util:fieldWrap bean="item" property="pageRange" width="20" filter="true" useWordBreak="true"/><br />
                                    </logic:notEmpty>
                                    <logic:notEmpty name="item" property="numberOfPages">
                                        <logic:equal name="item" property="typeOfContent" value="<%= RepublicationConstants.CONTENT_SELECTED_PAGES %>">
                                            <strong>&nbsp;Number of pages: </strong><bean:write name="item" property="numberOfPages" /><br />
                                        </logic:equal>
                                    </logic:notEmpty>
                                    <logic:notEmpty name="item" property="translationLanguage">
                                        <bean:define id="translationLanguageString">
                                            <logic:equal name="item" property="translationLanguage" value="<%= RepublicationConstants.NO_TRANSLATION %>">No Translation</logic:equal>
                                            <logic:equal name="item" property="translationLanguage" value="<%= RepublicationConstants.TRANSLATED_LANGUAGE_CHINESE %>">Chinese</logic:equal>
                                            <logic:equal name="item" property="translationLanguage" value="<%= RepublicationConstants.TRANSLATED_LANGUAGE_ENGLISH %>">English</logic:equal>
                                            <logic:equal name="item" property="translationLanguage" value="<%= RepublicationConstants.TRANSLATED_LANGUAGE_FRENCH %>">French</logic:equal>
                                            <logic:equal name="item" property="translationLanguage" value="<%= RepublicationConstants.TRANSLATED_LANGUAGE_GERMAN %>">German</logic:equal>
                                            <logic:equal name="item" property="translationLanguage" value="<%= RepublicationConstants.TRANSLATED_LANGUAGE_ITALIAN %>">Italian</logic:equal>
                                            <logic:equal name="item" property="translationLanguage" value="<%= RepublicationConstants.TRANSLATED_LANGUAGE_JAPANESE %>">Japanese</logic:equal>
                                            <logic:equal name="item" property="translationLanguage" value="<%= RepublicationConstants.TRANSLATED_LANGUAGE_PORTUGUESE %>">Portuguese</logic:equal>
                                            <logic:equal name="item" property="translationLanguage" value="<%= RepublicationConstants.TRANSLATED_LANGUAGE_RUSSIAN %>">Russian</logic:equal>
                                            <logic:equal name="item" property="translationLanguage" value="<%= RepublicationConstants.TRANSLATED_LANGUAGE_SPANISH %>">Spanish</logic:equal>
                                            <logic:equal name="item" property="translationLanguage" value="<%= RepublicationConstants.TRANSLATED_LANGUAGE_OTHER %>">Other</logic:equal>
                                        </bean:define>
                                        <strong>&nbsp;Translating to: </strong><bean:write name="translationLanguageString" /><br />
                                    </logic:notEmpty>
                                    <logic:notEmpty name="item" property="contentsPublicationDate">
                                        <strong>&nbsp;Requested content's publication date: </strong><bean:write name="item" property="contentsPublicationDate" format="MM/dd/yyyy" /><br />
                                    </logic:notEmpty>
                                    <logic:notEmpty name="item" property="yourReference">
                                        <strong>&nbsp;Your reference: </strong><util:fieldWrap bean="item" property="yourReference" width="20" filter="true" useWordBreak="true"/>
                                    </logic:notEmpty>
                                </logic:equal>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            
            <!-- item footer -->
            <tr>
                <td class="itemFooter">&nbsp;</td>
                <td class="itemFooter">
                    <table style="width:100%" cellpadding="0" cellspacing="0" border="0">
                        <bean:define id="inputId">valueInput_<bean:write name="index"/></bean:define>
                        <tr>
                            <td>
                                <logic:equal name="item" property="photocopy" value="true">
                                    <strong>Number of copies:</strong>&nbsp;
                                    <nested:text property="numberOfCopies" styleClass="small" styleId="<%= inputId %>" />
                                    <script type="text/javascript">
                                        objectsToValidate[<bean:write name="index"/>] = {inputId: "<%= inputId %>", maxInvalidValue: 0, fieldName: "number of copies", title: '<bean:write name="item" property="publicationTitle" />', isSelect: false};
                                    </script>
                                </logic:equal>
                                <logic:equal name="item" property="republication" value="true">
                                    <strong>Circulation/Distribution:</strong>&nbsp;
                                    <nested:text property="circulationDistribution" styleClass="small" styleId="<%= inputId %>"/>
                                    <script type="text/javascript">
                                        objectsToValidate[<bean:write name="index"/>] = {inputId: "<%= inputId %>", maxInvalidValue: 0, fieldName: "circulation/distribution", title: '<bean:write name="item" property="publicationTitle" />', isSelect: false};
                                    </script>
                                </logic:equal>
                                <logic:equal name="item" property="email" value="true">
                                    <strong>Number of recipients:</strong>&nbsp;
                                    <nested:text property="numberOfRecipients" styleClass="small" styleId="<%= inputId %>"/>
                                    <script type="text/javascript">
                                        objectsToValidate[<bean:write name="index"/>] = {inputId: "<%= inputId %>", maxInvalidValue: 0, fieldName: "number of recipients", title: '<bean:write name="item" property="publicationTitle" />', isSelect: false};
                                    </script>
                                </logic:equal>
                                <logic:equal name="item" property="net" value="true">
                                    <strong>Duration of posting:</strong>&nbsp;
                                    <nested:select property="duration" styleId="<%= inputId %>">
                                        <html:option value="-1">Choose One</html:option>
                                        <html:option value="<%= String.valueOf(UsageDataNet.TO_30_DAYS_FEE) %>"><%= UsageDataNet.feeStringValues[UsageDataNet.TO_30_DAYS_FEE] %></html:option>
                                        <html:option value="<%= String.valueOf(UsageDataNet.TO_180_DAYS_FEE) %>"><%= UsageDataNet.feeStringValues[UsageDataNet.TO_180_DAYS_FEE] %></html:option>
                                        <html:option value="<%= String.valueOf(UsageDataNet.TO_365_DAYS_FEE) %>"><%= UsageDataNet.feeStringValues[UsageDataNet.TO_365_DAYS_FEE] %></html:option>
                                        <html:option value="<%= String.valueOf(UsageDataNet.UNLIMITED_DAYS_FEE) %>"><%= UsageDataNet.feeStringValues[UsageDataNet.UNLIMITED_DAYS_FEE] %></html:option>
                                    </nested:select>
                                    <script type="text/javascript">
                                        objectsToValidate[<bean:write name="index"/>] = {inputId: "<%= inputId %>", maxInvalidValue: -1, fieldName: "duration of posting", title: '<bean:write name="item" property="publicationTitle" />', isSelect: true};
                                    </script>
                                </logic:equal>
                            </td>
                        </tr>
                        <tr>
                            <td class="footerAction">
                                <a href="#" onclick="javascript:removePermission(<%= index %>)">Remove</a>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        
        </nested:iterate>
    
    </table>
    
    <div class="horiz-rule"></div>
        
    <a href="#" onclick="javascript:copyOrder()">
        <html:img src="/media/images/btn_continue.gif" alt="Continue" align="right" />
    </a>
    
    <br clear="all" />

</div>

<div class="clearer"></div>

</html:form>
