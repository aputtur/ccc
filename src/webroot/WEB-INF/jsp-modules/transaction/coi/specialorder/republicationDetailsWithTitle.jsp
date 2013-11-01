<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/tld/struts-logic.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/tld/struts-bean.tld" %>
<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/cc2-util.tld" %>

<%@ page errorPage="/jspError.do" %>
<%@ page import="com.copyright.ccc.web.transaction.TransactionConstants" %>
<%@ page import="com.copyright.ccc.web.transaction.RepublicationConstants" %>

<!-- client side validation -->
<util:ccJavascript formName="specialOrderForm_republication" />
<!-- end client side validation -->
<script type="text/javascript" language="javascript" src="<html:rewrite page="/resources/commerce/js/roman.js"/>"></script>
<script type="text/javascript" language="javascript" src="<html:rewrite page="/resources/commerce/js/parser.js"/>"></script>

<style type="text/css">

    input.smaller { width: 155px; }

</style>

<jsp:include page="/WEB-INF/jsp-modules/common/jQueryDatepickerSupport.jsp"/>

<script type="text/javascript">

    var currentActiveElement = "";
    var origStartOfTermDateText = "";
    var isInSubmit = false;
    
    function setOnFocusForAllElements()
    {
        //var allElements = document.getElementById("basicTransactionForm").elements;
        var allElements = document.getElementById("specialOrderFormCOI").elements;
        
        for( var i = 0; i < allElements.length; i++ )
        {
            allElements[i].onfocus = function(){ setOnFocusForElement( this ); };
        }
    }
    
    function setOnFocusForElement( element )
    {
        var elementId = element.id;
        
        if( elementId == "republicationDateText" )
            origStartOfTermDateText = element.value;
        else
        {
            //note: i don't need to check for the calendar link b/c links are not included 
            //in the elements array, so this won't be called by the calendar link
            if( currentActiveElement == "republicationDateText" )
                validateStartOfTermDate();
        }
        
        currentActiveElement = elementId;
    }
    
    function validateStartOfTermDate()
    {
        var startOfTermDate = document.getElementById("republicationDateText").value;
        
        if( startOfTermDate != origStartOfTermDateText )
        {
            if ( !isValidCCDate( startOfTermDate, false ) )
                alert("Please enter a valid date this content will be republished, in the format mm/dd/yyyy or m/d/yyyy");
        }
    }
    
    function validateForm(form)
    {
        return validateSpecialOrderForm_republication(form);
    }
    
    function toggleNumberOfPagesVisibility()
    {
        document.getElementById("numberOfPagesRow").style.display = 
            (document.getElementById("typeOfContentSelect").value == "<%= RepublicationConstants.CONTENT_SELECTED_PAGES %>") ? "" : "none";
    }
    
    function disableRepublicationTypeOfUseChange()
    {
        document.getElementById("republicationTypeOfUseSelect").disabled = true;
        document.getElementById("republicationTypeOfUseLabel").className = "greytype";
        document.getElementById("republicationTypeOfUseSelect").className = "greytype";
        document.getElementById("businessSelect").disabled = true;
        document.getElementById("businessLabel").className = "greytype";
        document.getElementById("businessSelect").className = "greytype";
    }
    
    function validatePageRange(pageRangeField) {
      var pageCount = -1, flds;
  
      if (pageRangeField.value == null || pageRangeField.value == "") {
        alert("Page Range(s) is a required field.");
        pageRangeField.focus();
      }
      else {
        pageCount = getPageCount(pageRangeField.value);
        if (pageCount != -1) {
          flds = document.getElementsByName("specialOrderItem.numberOfPages");
          flds[0].value = pageCount;
        }
        else {
          alert("You have entered an invalid page range.");
          pageRangeField.focus();
        }
      }
    }
    
</script>

<%-- tiles declarations --%>
<tiles:useAttribute id="allowPermissionChange" name="allowPermissionChange" ignore="true" />
<%-- end tiles declarations --%>

<bean:define id="isBiactive" name="specialOrderFormCOI" property="isBiactive"/>

<h2 class="indent-1">Information about the new work you are creating:</h2>

<table border="0" class="indent-2">
  <tr>
    <td width="239" id="republicationTypeOfUseLabel">I plan to republish this content in a(n):<span class="importanttype">*</span></td>
    <td>
        <html:select styleClass="select01" name="specialOrderFormCOI" property="specialTypeOfUseCode" styleId="republicationTypeOfUseSelect">
            <html:option value="-1">Choose one</html:option>
            <html:option value="<%= String.valueOf(TransactionConstants.TYPE_OF_USE_BROCHURE) %>">Brochure</html:option>
            <html:option value="<%= String.valueOf(TransactionConstants.TYPE_OF_USE_CDROM) %>">CD-ROM</html:option>
            <html:option value="<%= String.valueOf(TransactionConstants.TYPE_OF_USE_DISSERTATION) %>">Dissertation</html:option>
            <html:option value="<%= String.valueOf(TransactionConstants.TYPE_OF_USE_DVD) %>">DVD</html:option>
            <html:option value="<%= String.valueOf(TransactionConstants.TYPE_OF_USE_JOURNAL) %>">Journal</html:option>
            <html:option value="<%= String.valueOf(TransactionConstants.TYPE_OF_USE_MAGAZINE) %>">Magazine</html:option>
            <html:option value="<%= String.valueOf(TransactionConstants.TYPE_OF_USE_NEWSLETTER) %>">Newsletter/E-Newsletter</html:option>
            <html:option value="<%= String.valueOf(TransactionConstants.TYPE_OF_USE_NEWSPAPER) %>">Newspaper</html:option>
            <html:option value="<%= String.valueOf(TransactionConstants.TYPE_OF_USE_OTHERBOOK) %>">Other Book</html:option>
            <html:option value="<%= String.valueOf(TransactionConstants.TYPE_OF_USE_PAMPHLET) %>">Pamphlet</html:option>
            <html:option value="<%= String.valueOf(TransactionConstants.TYPE_OF_USE_PRESENTATION) %>">PowerPoint Presentation</html:option>
            <html:option value="<%= String.valueOf(TransactionConstants.TYPE_OF_USE_TEXTBOOK) %>">Textbook</html:option>
            <html:option value="<%= String.valueOf(TransactionConstants.TYPE_OF_USE_TRADEBOOK) %>">Trade Book</html:option>
        </html:select>
        <logic:equal name="allowPermissionChange" value="false">
            <span style="padding-left:60px"><util:contextualHelp helpId="30" rollover="false">Why can't I change this?</util:contextualHelp></span>
        </logic:equal>
    </td>
  </tr>
  <tr>
    <td><div align="left">The total circulation/distribution will be: <span class="importanttype">*</span></div></td>
    <td><div align="left">
        <logic:greaterThan value="0" name="specialOrderFormCOI" property="specialOrderItem.circulationDistribution">
            <html:text name="specialOrderFormCOI" property="specialOrderItem.circulationDistribution" size="6" maxlength="6"/>
        </logic:greaterThan>
        <logic:lessEqual value="0" name="specialOrderFormCOI" property="specialOrderItem.circulationDistribution">
            <html:text name="specialOrderFormCOI" property="specialOrderItem.circulationDistribution" value=""  size="6" maxlength="6"/>
            <util:contextualHelp titleName="specialOrderFormCOI" titleProperty="circulationDistributionTitle" bodyName="specialOrderFormCOI" bodyProperty="circulationDistributionText" rollover="false"><u>What's this?</u></util:contextualHelp>
        </logic:lessEqual>
    </div></td>
  </tr>
  <tr>
    <td id="businessLabel">Republishing publisher is: <span class="importanttype">*</span></td>
    <td><div align="left">
        <html:select styleClass="select01" name="specialOrderFormCOI" property="specialOrderItem.business" styleId="businessSelect">
            <html:option value="<%= RepublicationConstants.BUSINESS_FOR_PROFIT %>">For-profit</html:option>
            <html:option value="<%= RepublicationConstants.BUSINESS_NON_FOR_PROFIT %>">Non-profit 501(c)(3)</html:option>
        </html:select>
        <logic:equal name="allowPermissionChange" value="false">
            <span style="padding-left:60px"><util:contextualHelp helpId="30" rollover="false">Why can't I change this?</util:contextualHelp></span>
        </logic:equal>
    </div></td>
  </tr>
  <tr>
    <td><div align="left">Organization republishing this work: <span class="importanttype">*</span></div></td>
    <td><div align="left">
        <html:text name="specialOrderFormCOI" property="specialOrderItem.republishingOrganization" styleClass="normal" maxlength="250"/>
    </div></td>
  </tr>
  <tr>
    <td><div align="left">Title of the new publication: </div></td>
    <td><div align="left">
        <html:text name="specialOrderFormCOI" property="specialOrderItem.newPublicationTitle" styleClass="normal" maxlength="250"/>
    </div></td>
  </tr>
  <tr>
    <td><div align="left">Date this content will be republished: <span class="importanttype">*</span></div></td>
    <td>
        <logic:empty name="specialOrderFormCOI" property="specialOrderItem.republicationDate">
            <html:text name="specialOrderFormCOI" property="specialOrderItem.republicationDate" styleClass="small" styleId="republicationDateText" value="MM/DD/YYYY"/>
        </logic:empty>
        <logic:notEmpty name="specialOrderFormCOI" property="specialOrderItem.republicationDate">
            <bean:define id="formattedRepublicationDate">
                <bean:write name="specialOrderFormCOI" property="specialOrderItem.republicationDate" format="MM/dd/yyyy" />
            </bean:define>
            <html:text name="specialOrderFormCOI" property="specialOrderItem.republicationDate" value="<%= formattedRepublicationDate %>" 
                styleClass="small" styleId="republicationDateText"/>
        </logic:notEmpty>
      </td>
  </tr>
  <tr>
    <td><div align="left">I am translating this material to: </div></td>
    <td><div align="left">
        <html:select name="specialOrderFormCOI" property="specialOrderItem.translationLanguage" styleClass="select01">
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
        <html:text name="specialOrderFormCOI" property="specialOrderItem.yourReference" styleClass="normal" maxlength="50"/>
        <span class="smalltype">(Example: "Mary's thesis, chapter 7")</span>
    </div></td>
  </tr>
</table>

<h2 class="indent-1">Usage information about content to be republished:</h2>

<table border="0" class="indent-2">
    <tr>
        <td width="330">Portion to be used: <span class="importanttype">*</span></td>
        <td><div align="left">
            <html:select styleClass="select01" name="specialOrderFormCOI" property="specialOrderItem.typeOfContent" styleId="typeOfContentSelect"
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
            <util:contextualHelp titleName="specialOrderFormCOI" titleProperty="republishPortionTitle" bodyName="specialOrderFormCOI" bodyProperty="republishPortionText" rollover="false"><u>What's this?</u></util:contextualHelp>
        </div></td>
        
    </tr>
    <tr>
        <td>I am the author of this content: <span class="importanttype">*</span></td>
        <td>
            <html:select styleClass="select01" name="specialOrderFormCOI" property="specialOrderItem.submitterAuthor">
                <html:option value="true">Yes</html:option>
                <html:option value="false">No</html:option>
            </html:select>
        </td>
    </tr>
    <tr>
        <td>Publication date of the content:<span class="importanttype">*</span></td>
        <td>
            <logic:empty name="specialOrderFormCOI" property="specialOrderItem.contentsPublicationDate">
                <html:text name="specialOrderFormCOI" property="specialOrderItem.contentsPublicationDate" styleClass="small" styleId="contentsPublicationDateText" value="MM/DD/YYYY"/>
            </logic:empty>
            <logic:notEmpty name="specialOrderFormCOI" property="specialOrderItem.contentsPublicationDate">
                <bean:define id="formattedContentsPublicationDate">
                    <bean:write name="specialOrderFormCOI" property="specialOrderItem.contentsPublicationDate" format="MM/dd/yyyy" />
                </bean:define>
                <html:text disabled="${isBiactive}" name="specialOrderFormCOI" property="specialOrderItem.contentsPublicationDate" 
                    value="<%= formattedContentsPublicationDate %>" styleClass="small" styleId="contentsPublicationDateText"/>
            </logic:notEmpty>
         </td>
    </tr>

    <tr id="numberOfPagesRow">
        <td>Number of pages to be used: <span class="importanttype">*</span></td>
        <td>
            <logic:greaterThan value="0" name="specialOrderFormCOI" property="specialOrderItem.numberOfPages">
                <html:text name="specialOrderFormCOI" property="specialOrderItem.numberOfPages" styleClass="normal" />
            </logic:greaterThan>
            <logic:lessEqual value="0" name="specialOrderFormCOI" property="specialOrderItem.numberOfPages">
                <html:text name="specialOrderFormCOI" property="specialOrderItem.numberOfPages" styleClass="normal" value="" />
            </logic:lessEqual>
        </td>
    </tr>
    <tr>
        <td>Title or description of content you intend to use: <span class="importanttype">*</span></td>
        <td><div align="left">
            <html:text name="specialOrderFormCOI" property="specialOrderItem.chapterArticle" styleClass="smaller" maxlength="250" />
        </div></td>
    </tr>
    
    <tr>
        <td>Author or creator of content you intend to use: <span class="importanttype">*</span></td>
        <td>
            <html:text name="specialOrderFormCOI" property="specialOrderItem.customAuthor" styleClass="smaller" maxlength="250" />
        </td>
    </tr>
    <tr id="pageRangeRow">
        <td>Page range of content that you intend to use: <span class="importanttype">*</span></td>
        <td>
            <html:text name="specialOrderFormCOI" property="specialOrderItem.pageRange" styleClass="smaller" maxlength="80" />
            <span class="smalltype">(Examples: ii, iv-vi or 3, 7-10) </span>
        </td>
    </tr>
</table>

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
