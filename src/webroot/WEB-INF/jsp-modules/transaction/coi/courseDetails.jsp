<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/tld/struts-logic.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/tld/struts-bean.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/cc2-util.tld" %>
<%@ page errorPage="/jspError.do" %>
<%@ page import="com.copyright.ccc.web.WebConstants" %>

<!-- tile attribute declarations -->

<tiles:useAttribute id="mode" name="mode"/>

<tiles:useAttribute id="useAddButton" name="useAddButton" ignore="true" />

<tiles:useAttribute id="addWebTrendsScenarios" name="addWebTrendsScenarios" ignore="true" />

<!-- end tile attribute declarations -->

<bean:define id="formPath" name="courseDetailsForm" property="formPath" type="java.lang.String" />


<!-- client side validation -->
<util:ccJavascript formName="courseDetailsForm" />
<!-- end client side validation -->

<script src="<html:rewrite page="/resources/commerce/js/util.js"/>" type="text/javascript"></script>
<jsp:include page="/WEB-INF/jsp-modules/common/jQueryDatepickerSupport.jsp"/>
<script src="<html:rewrite page="/resources/commerce/js/validators.js"/>" type="text/javascript"></script>
<script src="<html:rewrite page="/resources/commerce/js/dateUtils.js"/>" type="text/javascript"></script>

<script type="text/javascript">

    var currentActiveElement = "";
    var origStartOfTermDateText = "";
    var isInSubmit = false;
    
    function setOnFocusForAllElements()
    {
        var allElements = document.getElementById("courseDetailsForm").elements;
        
        for( var i = 0; i < allElements.length; i++ )
        {
            allElements[i].onfocus = function(){ setOnFocusForElement( this ); };
        }
    }
    
    function setOnFocusForElement( element )
    {
        var elementId = element.id;
        
        if( elementId == "startOfTermDateText" )
            origStartOfTermDateText = element.value;
        else
        {
            //note: i don't need to check for the calendar link b/c links are not included 
            //in the elements array, so this won't be called by the calendar link
            if( currentActiveElement == "startOfTermDateText" )
                validateStartOfTermDate();
        }
        
        currentActiveElement = elementId;
    }
    
    function validateStartOfTermDate()
    {
        var startOfTermDate = document.getElementById("startOfTermDateText").value;
        
        if( startOfTermDate != origStartOfTermDateText )
        {
            if ( !isValidCCDate( startOfTermDate, false ) )
                alert("Please enter a valid date for Start of term in the format mm/dd/yyyy or m/d/yyyy");
            else if ( !isDateSixMonthsPrior( startOfTermDate ) )
                alert("Please enter a Start of term date that falls within 6 months of the current date");
            else if (!isDateTenMonthsLater( startOfTermDate ))
                alert( "Please enter a Start of Term date that falls within 10 months of the current date" );
          /*  else if ( !isDateInCurrentOrFutureYear( startOfTermDate ) )
                alert("Please enter a Start of term date that falls within the current year or a future year"); 
            else if ( !isMonthAndDayOnOrAfterToday( startOfTermDate ) )
                alert("The Start of term date that you entered occurs in the past. Please ensure that the date you entered is correct"); */
        }
       
    }
    
    function clearStartOfTermDateValue()
    {
        var startOfTermDateText = document.getElementById("startOfTermDateText");
        
        startOfTermDateText.value = "";
        origStartOfTermDateText = "";
        
        startOfTermDateText.onclick = null;
    }
    
     function cancelSubmit()
    {
        document.getElementById("operation").value = "cancel";
        document.getElementById('courseDetailsForm').submit();
    }
    

    function submitCourseDetails()
    {   
        if( !isInSubmit )
        {
            //alert("in submit");
            isInSubmit = true;
            
            var courseDetailsForm = document.getElementById("courseDetailsForm");
            
            document.getElementById("operation").value = "submit";
            
            var errors = validateCourseDetailsForm(courseDetailsForm);
            //var errors = "";
            //alert("Errors: " + errors);
    
             if(errors.length > 0) 
             {
                displayValidationErrors(errors, "clientSideValidationErrorsSection", "clientSideValidationErrorsListSection");
                
                isInSubmit = false;
             }
             else
             {
                hideValidationErrors("clientSideValidationErrorsSection");
                
                <logic:equal name="mode" value="edit">
                    
                    var cascadingChanges = document.getElementById("cascadingChanges");
                
                    if( document.getElementById("numberOfStudentsText").value != originalNumberOfStudents )
                    {
                        var confirmCascadeChangesString = "You have updated the \"Number of students \" within your course information only. " + 
                            "Would you like the items in this order to reflect this change? If you select OK, then all open items " +
                            "will be updated and their price will be automatically recalculated.";
    
                        if(confirm(confirmCascadeChangesString))
                            cascadingChanges.value = "true";
                        else
                            cascadingChanges.value = "false";
                    }
                    else cascadingChanges.value = "false";
                    
                </logic:equal>
                
                courseDetailsForm.submit();
             }
         }
         
         return false;
    }
    
   
    <logic:equal name="mode" value="edit">
    var originalNumberOfStudents = <bean:write name="courseDetailsForm" property="courseDetails.numberOfStudents" />;
      //Bug: CC-1939  var originalNumberOfStudents = <bean:write name="courseDetailsForm" property="courseDetails.estimatedQty" />;
    </logic:equal>
    
    
</script>

<html:form action="<%= formPath %>" styleId="courseDetailsForm">
    
    <html:hidden property="operation" styleId="operation" value="" />
    <html:hidden name="courseDetailsForm" property="cascadingChanges" styleId="cascadingChanges" />
    <html:hidden name="courseDetailsForm" property="cp" styleId="cpId" />
    <html:hidden name="courseDetailsForm" property="rp" styleId="rpId" />
    <html:hidden name="courseDetailsForm" property="sf" styleId="sfId" />
    
    <div style="color:red;padding-bottom:40px;padding-top:10px;display:none" id="clientSideValidationErrorsSection">
        <b>THERE WAS A PROBLEM COMPLETING YOUR REQUEST</b>
        <div style="color:red;padding-left:20px" id="clientSideValidationErrorsListSection"></div>
    </div>

    <!-- Begin Left Content -->
    <div id="ecom-boxcontent">
    
        <h1>
            <logic:equal name="mode" value="add">Course Information</logic:equal>
            <logic:notEqual name="mode" value="add">Edit Course Information</logic:notEqual>
        </h1>

		<bean:define id="cancelViaHistory" value='<%=request.getAttribute(WebConstants.RequestKeys.IS_CANCEL_VIA_HISTORY_ENABLED)==null?"":(String)request.getAttribute(WebConstants.RequestKeys.IS_CANCEL_VIA_HISTORY_ENABLED)%>'/>
		
		<logic:notEmpty name="cancelViaHistory">
	        <a href="javascript:history.go(-1)" class="icon-back" id="998"><logic:equal name="mode" value="add">Back</logic:equal><logic:notEqual name="mode" value="add">Cancel changes</logic:notEqual></a>		
		</logic:notEmpty>
		<logic:empty name="cancelViaHistory">		
	        <a href="/cart.do?operation=defaultOperation" class="icon-back" id="998"><logic:equal name="mode" value="add">Back</logic:equal><logic:notEqual name="mode" value="add">Cancel changes</logic:notEqual></a>
		</logic:empty>

        <div class="horiz-rule"></div>

        <h2>
            <logic:equal name="mode" value="add">
                Enter the following course details for this cart <span class="smalltype bold">(You only have to do this once)</span>:
            </logic:equal>
            <logic:notEqual name="mode" value="add">
                Edit the following course details:
            </logic:notEqual>
            <span class="required"> * Required</span> 
        </h2>

        <table border="0" class="indent-1">
            <logic:notEqual name="mode" value="add">
                <tr>
                    <td>Number of students:<span class="importanttype">*</span></td>
                    <td>
                        <logic:greaterEqual value="0" name="courseDetailsForm" property="courseDetails.numberOfStudents">
                            <html:text name="courseDetailsForm" property="courseDetails.numberOfStudents" styleClass="normal" maxlength="5" styleId="numberOfStudentsText"/>
                        </logic:greaterEqual>
                        <logic:lessThan value="0" name="courseDetailsForm" property="courseDetails.numberOfStudents">
                            <html:text name="courseDetailsForm" property="courseDetails.numberOfStudents" value="" styleClass="normal" maxlength="5" styleId="numberOfStudentsText"/>
                        </logic:lessThan>
                    </td>
                </tr>
            </logic:notEqual>
            <tr>
                <td>University/Institution:<span class="importanttype">*</span></td>
                <td>
                    <html:text name="courseDetailsForm" property="courseDetails.schoolCollege" styleClass="normal" maxlength="250"/>
                    <span class="smalltype">(Please do not abbreviate)</span>
                </td>
            </tr>
            <tr>
                <td>Start of term: <span class="importanttype">*</span></td>
                <td>
                    <logic:empty name="courseDetailsForm" property="courseDetails.startOfTermDate">
                        <html:text name="courseDetailsForm" property="courseDetails.startOfTermDate" styleClass="small" value="MM/DD/YYYY" 
                            size="15" styleId="startOfTermDateText" maxlength="10" onclick="javascript:clearStartOfTermDateValue()" />
                    </logic:empty>
                    <logic:notEmpty name="courseDetailsForm" property="courseDetails.startOfTermDate">
                        <bean:define id="formattedStartOfTermDate">
                            <bean:write name="courseDetailsForm" property="courseDetails.startOfTermDate" format="MM/dd/yyyy" />
                        </bean:define>
                        <html:text name="courseDetailsForm" property="courseDetails.startOfTermDate" styleClass="small" size="15" 
                            styleId="startOfTermDateText" value="<%= formattedStartOfTermDate %>" maxlength="10" />
                    </logic:notEmpty>
                </td>
            </tr>
            <tr>
                <td>Course name:<span class="importanttype">*</span></td>
                <td><div align="left">
                    <html:text name="courseDetailsForm" property="courseDetails.courseName" styleClass="normal" maxlength="250"/>
                </div></td>
            </tr>
            <tr>
                <td>Course number:</td>
                <td><html:text name="courseDetailsForm" property="courseDetails.courseNumber" styleClass="normal" maxlength="250"/></td>
            </tr>
            <tr>
                <td>Instructor:</td>
                <td><html:text name="courseDetailsForm" property="courseDetails.instructor" styleClass="normal" maxlength="250"/></td>
            </tr>
            <tr>
                <td>Your reference:</td>
                <td>
                    <html:text name="courseDetailsForm" property="courseDetails.reference" styleClass="normal" maxlength="50"/>
                    <span class="smalltype">(Examples: request123, prosmith456)</span>
                </td>
            </tr>
            <tr>
                <td>Your accounting reference:</td>
                <td>
                    <html:text name="courseDetailsForm" property="courseDetails.accountingReference" styleClass="normal" maxlength="50"/>
                    <span class="smalltype">(Examples: English Dept, Psych Dept)</span>
                </td>
            </tr>
            <tr>
                <td>Order entered by: </td>
                <td><html:text name="courseDetailsForm" property="courseDetails.orderEnteredBy" styleClass="normal" maxlength="30"/></td>
            </tr>
        </table>

        <div class="horiz-rule"></div>
        
        <a href="#" onclick="javascript:submitCourseDetails()" id="submitButtonImage" >
            <logic:equal name="useAddButton" value="true">
                <html:img src="/media/images/btn_add-cart.gif" alt="Add to Cart" width="91" height="21" align="right" />
            </logic:equal>
            <logic:notEqual name="useAddButton" value="true">
                <html:img src="/media/images/btn_save-changes.gif" alt="Save Changes" width="101" height="20" align="right" />
            </logic:notEqual>
        </a>
        
        <br clear="all" />

</div>

<div class="clearer"></div>

<!-- End Main Content -->

</html:form>

<script type="text/javascript">

    var isIE = navigator.userAgent.indexOf("MSIE") > 0;
 
    //set global onfocus event to determine which element is focused
    setOnFocusForAllElements();
    
 	$(document).ready(function(){

	 	jqCalendarDatepickerCreate('startOfTermDateText','<html:rewrite href='/media/images/calendar.gif'/>');
	 	jqCalendarDatepickerSetYearMenuRange('startOfTermDateText','-0:+10');
	 	jqCalendarDatepickerSetMinDate( 'startOfTermDateText', '+0y' );
	 	
	});

</script>

<!-- Webtrends tags for capturing scenarios -->
<logic:equal name="addWebTrendsScenarios" value="true">
    <META name="WT.si_n" content="ShoppingCart">
    <META name="WT.si_p" content="CartView">
</logic:equal>
<!-- end Webtrends tags -->
