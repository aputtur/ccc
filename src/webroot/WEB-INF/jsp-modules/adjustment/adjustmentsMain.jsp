<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page errorPage="/jspError.do" %>

<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/struts-nested.tld" prefix="nested" %>

<%@ page import="com.copyright.ccc.web.transaction.RepublicationConstants"  %>
<%@ page import="com.copyright.data.order.UsageDataNet"%>
<%@ page import="com.copyright.ccc.business.services.adjustment.OrderAdjustmentConstants" %> 
<%@ page import="com.copyright.ccc.business.services.adjustment.OrderAdjustmentSummary" %> 
<%@ page import="com.copyright.ccc.business.services.adjustment.OrderAdjustment" %>

<html>
    
    <head>
   
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
    
    <title>
        Adjustments Application -&nbsp;
        
        <logic:equal value="true" name="adjustmentForm" property="adjustment.invoiceAdjustment">
          Invoice View
        </logic:equal>
        
        <logic:equal value="true" name="adjustmentForm" property="adjustment.purchaseAdjustment">
          Purchase View
        </logic:equal>
        
        <logic:equal value="true" name="adjustmentForm" property="adjustment.detailAdjustment">
          Detail View
        </logic:equal>
        
        <logic:equal value="true" name="adjustmentForm" property="UIModeConfirmation">
            &gt; Confirmation
        </logic:equal>
        
    </title>
    
    <link href="<html:rewrite page="/resources/commerce/css/default.css"/>" rel="stylesheet" type="text/css" />
    <link href="<html:rewrite page="/resources/commerce/css/ccc-new.css"/>" rel="stylesheet" type="text/css" />
    
    <link href="<html:rewrite page="/resources/adjustment/css/adjustment.css"/>" rel="stylesheet" type="text/css" />

    <script src="<html:rewrite page="/resources/adjustment/script/adjustments.js"/>"></script>
    
    <script src="<html:rewrite page="/resources/commerce/script/util.js"/>"></script>
    
    <script type="text/javascript">
    
         function initAdjustmentsPage(){
          organizePageLayout();
          formatDecimalInputs("decimalFormat");
          initExclusiveQtyAndFeeGroups();
          scrollToLastVisitedAnchor();
        }
        
        if( typeof addOnLoadEvent == 'function' && typeof initAdjustmentsPage == 'function' ) addOnLoadEvent( initAdjustmentsPage );
    </script>
  </head>

  <style type="text/css">
    div#validationErrorsSection{ display: none;}
  </style>  
    
  <body>
  
     <%
        boolean enterAdjustmentAllowed = com.copyright.ccc.business.security.UserContextService.hasPrivilege( com.copyright.ccc.business.security.CCPrivilegeCode.ENTER_ORDER_ADJUSTMENT ); 
        boolean commitAdjustmentAllowed = com.copyright.ccc.business.security.UserContextService.hasPrivilege( com.copyright.ccc.business.security.CCPrivilegeCode.COMMIT_ORDER_ADJUSTMENT ); 
     %>
  
        
    <div id="mainContainer" style="padding: 10px; width: 850px;">
      
        <h1 style="padding-bottom: 10px;">
            
            Adjustments&nbsp;&gt;&nbsp;
            <logic:equal value="true" name="adjustmentForm" property="adjustment.invoiceAdjustment">Invoice</logic:equal>
            <logic:equal value="true" name="adjustmentForm" property="adjustment.purchaseAdjustment">Purchase</logic:equal>
            <logic:equal value="true" name="adjustmentForm" property="adjustment.detailAdjustment">Detail</logic:equal>
            Number&nbsp;
            <bean:write name="adjustmentForm" property="adjustment.sourceID"/>
                    
            <logic:equal value="true" name="adjustmentForm" property="UIModeConfirmation">
             &gt; Confirmation
            </logic:equal>
            
        </h1>
        
        <logic:messagesPresent >

            <span style="color: red;">
              <html:errors bundle="adjustment" />
            </span>
            
        </logic:messagesPresent>
        
        <logic:messagesPresent message="true">
          <div style="padding-left: 10px; padding-right: 10px;">
          <div id="messages" class="container" style="color: #3F493D; background-color:#D1D8CC; padding:10px; border:1px solid gray;">
            <script type="text/javascript">
              function hideMessages(){ var messages = document.getElementById("messages"); if(messages){ messages.style.display="none"; } }
            </script>
            <a id="closeMessagesLink" href="javascript:hideMessages()" style="position: relative; float: right; color: gray; font-weight: bold;">Close</a>
            <html:messages bundle="adjustment" id="message" message="true">
                <bean:write name="message"/>
            </html:messages>
            
          </div>
          </div>
        
        </logic:messagesPresent>  
        
        
        
      <div id="linksContainer" class="container">
       <a href='<html:rewrite page="/admin/orderHistory.do"/>'><html:image page="/resources/adjustment/images/hourglass_go.png" style="vertical-align: middle;" title="Go to Order History"/></a>&nbsp;<a href='<html:rewrite page="/admin/orderHistory.do"/>'>Go to Order History</a>&nbsp;&nbsp;|&nbsp;&nbsp;<a href='<html:rewrite page="/adjustment/adjustment.do?operation=displayAdjustmentsForUser"/>'><html:image page="/resources/adjustment/images/page_save.png" style="vertical-align: middle;" title="View Pending Adjustments"/></a>&nbsp;<a href='<html:rewrite page="/adjustment/adjustment.do?operation=displayAdjustmentsForUser"/>'>View Pending Adjustments</a>
      </div>
         
      <%//Insert Customer information, full credit and global adjsutments sections%>
      <jsp:include page="custInfoFullCreditGlobalAdjFragment.jsp"/>

      
      <div id="individualAdjustmentsContainer" class="container">
        
        <form method="post" action="" id="adjustmentsForm" >
              <html:hidden name="adjustmentForm" property="adjustmentIDToRecalculate" styleId="adjustmentIDToRecalculate"/>
        
              <fieldset style="padding-right: 0px; padding-left: 0px; padding-top: 10px; padding-bottom: 10px; background-color: #FFFFFF;">
                  
                  <legend>Individual Adjustments</legend>
                  
                  <%// Paginator controls fragment inserted here %>
                  <jsp:include page="paginatorControlsFragment.jsp"/>
                  
                  <nested:iterate name="adjustmentForm" property="UIModeAwareBody" id="bodyItem" indexId="bodyItemIndex">
                  
                    <bean:define id="RLSTypeOfContent" name="bodyItem" property="value.originalOrderDetails.typeOfContent" type="java.lang.String" />
                  
                    <div id="bodyItemContainer" class="container  <%if((bodyItemIndex.intValue() % 2) == 0){%>rowEven<%}else{%>rowOdd<%}%>">
                    
                        <span style="line-height: 150%">
                    
                          <span style="font-weight: bold; font-size: 9pt;">
                            <a id="adj_<%=bodyItemIndex%>" name="adj_<%=bodyItemIndex%>"></a>Order Detail Number:&nbsp;
                          </span>
                          <span style="color: #990000; font-weight: bold; font-size: 9pt;">
                            <bean:write name="bodyItem" property="value.detailID"  /><br/>
                          </span>
                          
                          
                          <logic:equal value="true" name="adjustmentForm" property="adjustment.purchaseAdjustment">
                            <logic:notEmpty name="bodyItem" property="value.originalOrderDetails.invoiceId">
                              <span style="font-weight: bold;">
                                Invoice Number:&nbsp;
                              </span>
                              <bean:write name="bodyItem" property="value.originalOrderDetails.invoiceId" /><br/>
                            </logic:notEmpty>
                          </logic:equal>
                          
                          <logic:equal value="true" name="adjustmentForm" property="adjustment.invoiceAdjustment">
                            <logic:notEmpty name="bodyItem" property="value.originalOrderDetails.purchaseId">
                              <span style="font-weight: bold;">
                                Purchase Number:&nbsp;
                              </span>
                              <bean:write name="bodyItem" property="value.originalOrderDetails.purchaseId" /><br/>
                            </logic:notEmpty>
                          </logic:equal>
                          
                          <logic:equal value="true" name="adjustmentForm" property="adjustment.detailAdjustment">
                            <logic:notEmpty name="bodyItem" property="value.originalOrderDetails.purchaseId">
                              <span style="font-weight: bold;">
                                Purchase Number:&nbsp;
                              </span>
                              <bean:write name="bodyItem" property="value.originalOrderDetails.purchaseId" /><br/>
                            </logic:notEmpty>
                            
                            <logic:notEmpty name="bodyItem" property="value.originalOrderDetails.invoiceId">
                              <span style="font-weight: bold;">
                                Invoice Number:&nbsp;
                              </span>
                              <bean:write name="bodyItem" property="value.originalOrderDetails.invoiceId" /><br/>
                            </logic:notEmpty>
                          </logic:equal>
                          
                          <span style="font-weight: bold;">
                            Publication Title:&nbsp;
                          </span>
                          <bean:write name="bodyItem" property="value.publicationTitle" /><br/>
                          
                          <span style="font-weight: bold;">
                            ISSN/ISBN:&nbsp;
                          </span>
                          <bean:write name="bodyItem" property="value.standardNumber" /><br/>
                          
                          <span style="font-weight: bold;">
                            Publisher:&nbsp;
                          </span>
                          <bean:write name="bodyItem" property="value.publisher" /><br/>
                          
                          <span style="font-weight: bold;">
                            Product:&nbsp;
                          </span>
                          
                          <logic:equal name="bodyItem" property="value.originalOrderDetails.photocopy" value="true">
                              TRS
                          </logic:equal>
                          
                          <logic:equal name="bodyItem" property="value.originalOrderDetails.email" value="true">
                              DPS Email
                          </logic:equal>
                          
                          <logic:equal name="bodyItem" property="value.originalOrderDetails.net" value="true">
                              DPS Net
                          </logic:equal>
                          
                          <logic:equal name="bodyItem" property="value.originalOrderDetails.republication" value="true">
                              RLS
                          </logic:equal>
                          
                          <logic:equal name="bodyItem" property="value.originalOrderDetails.APS" value="true">
                              APS
                          </logic:equal>
                          
                          <logic:equal name="bodyItem" property="value.originalOrderDetails.ECCS" value="true">
                              ECCS
                          </logic:equal>
                          
                          <br/>
                          
                          <span style="font-weight: bold;">
                            Payment Method:&nbsp;
                          </span>
                          <bean:write name="bodyItem" property="value.paymentMethod" />&nbsp;&nbsp;&nbsp;
                          
                          <span style="font-weight: bold;">
                            Distributed:&nbsp;
                          </span>
                          
                          <span class="distributed">
                            <logic:equal value="true" name="bodyItem" property="value.distributed">
                              YES
                            </logic:equal>
                            
                            <logic:equal value="false" name="bodyItem" property="value.distributed">
                              NO
                            </logic:equal>
                          </span>
                          
                          <br/>
                          
                        </span>
                        
                        <p/>
                        
                        <table style="border-collapse: collapse; width: 100%;" >
                        
                          <tr>
                            <th>Detail No.</th>
                            <th>Date</th>
                            <th>Status</th>
                            <th>Reason</th>
                            
                            <logic:equal value="true" name="bodyItem" property="value.TRS">
                              <th>Pages</th>
                              <th>Copies</th>
                            </logic:equal>
                            
                            <logic:equal value="true" name="bodyItem" property="value.DPSEMail">
                              <th colspan="2">Recipients</th>
                            </logic:equal>
                            
                            <logic:equal value="true" name="bodyItem" property="value.DPSInternet">
                              <th colspan="2">Duration</th>
                            </logic:equal>
                            
                            <logic:equal value="true" name="bodyItem" property="value.DPSIntranet">
                              <th colspan="2">Duration</th>
                            </logic:equal>
                            
                            <logic:equal value="true" name="bodyItem" property="value.DPSExtranet">
                              <th colspan="2">Duration</th>
                            </logic:equal>
                            
                            <logic:equal value="true" name="bodyItem" property="value.RLS">
                                                   
                              <th colspan="2">
                              
                                <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_FULL_ARTICLE_CHAPTER ) ) { %>
                                    Full Article Chapter
                                <%}%>
                                
                                <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_EXCERPT ) ) { %>
                                  Excerpts
                                <%}%>
                                
                                <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_QUOTATION ) ) { %>
                                  Quotations
                                <%}%>
                                
                                <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_CHART ) ) { %>
                                  Charts
                                <%}%>
                                
                                <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_FIGURE_DIAGRAM_TABLE ) ) { %>
                                  Figures
                                <%}%>
                                
                                <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_PHOTOGRAPH ) ) { %>
                                  Photographs
                                <%}%>
                                
                                <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_ILLUSTRATION ) ) { %>
                                  Illustrations
                                <%}%> 
                                
                                <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_GRAPH ) ) { %>
                                  Graphs
                                <%}%>
                                
                                <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_SELECTED_PAGES ) ) { %>
                                  Pages
                                <%}%>
                                
                                <% if (RLSTypeOfContent.equals( RepublicationConstants.CONTENT_CARTOONS ) ) { %>
                                  Cartoons
                                <%}%>
                                
                                <% if (RLSTypeOfContent.equals( RepublicationConstants.CONTENT_LOGOS ) ) { %>
                                  Logos
                                <%}%>
                                
                              </th>
                                                    
                            </logic:equal>
                            
                            <logic:equal value="true" name="bodyItem" property="value.APS">
                            
                              <th>Students</th>
                              <th>Pages</th>
                                                    
                            </logic:equal>
                            
                            <logic:equal value="true" name="bodyItem" property="value.ECCS">
                            
                              <th>Students</th>
                              <th>Pages</th>
                                                    
                            </logic:equal>
                                                  
                            <th>Licensee Fee</th>
                            <th>Royalty</th>
                            <th>Discount</th>
                            <th>Total</th>
                            
                          </tr>
                          
                           <logic:equal value="true" name="adjustmentForm" property="UIEditCurrentAdjustmentMode">                          
                          
                              <tr>
                                
                                <td class="origAdjustCell"> <bean:write name="bodyItem" property="value.originalOrderDetails.ID"/> </td>
                                
                                <td class="origAdjustCell"> <bean:write name="bodyItem" property="value.originalOrderDetails.createDateStr"/> </td>
                                
                                <td class="origAdjustCell"> <bean:write name="bodyItem" property="value.originalOrderDetails.billingStatus"/> </td>
                                
                                <td class="origAdjustCell"> <!-- No reason here --> N/A </td>
                                
                                <logic:equal value="true" name="bodyItem" property="value.TRS">
                                  <td class="origAdjustCell"> <bean:write name="bodyItem" property="value.originalOrderDetails.numberOfPages"/> </td>
                                  <td class="origAdjustCell"> <bean:write name="bodyItem" property="value.originalOrderDetails.numberOfCopies"/> </td>
                                </logic:equal>
                                
                                <logic:equal value="true" name="bodyItem" property="value.DPSEMail">
                                  <td class="origAdjustCell" colspan="2"> <bean:write name="bodyItem" property="value.originalOrderDetails.numberOfRecipients"/> </td>
                                </logic:equal>
                                
                                <logic:equal value="true" name="bodyItem" property="value.DPSInternet">
                                  <td class="origAdjustCell" colspan="2"> <bean:write name="bodyItem" property="value.originalOrderDetails.durationString"/> </td>
                                </logic:equal>
                                
                                <logic:equal value="true" name="bodyItem" property="value.DPSIntranet">
                                  <td class="origAdjustCell" colspan="2"> <bean:write name="bodyItem" property="value.originalOrderDetails.durationString"/> </td>
                                </logic:equal>
                                
                                <logic:equal value="true" name="bodyItem" property="value.DPSExtranet">
                                  <td class="origAdjustCell" colspan="2"> <bean:write name="bodyItem" property="value.originalOrderDetails.durationString"/> </td>
                                </logic:equal>
                                
                                 <logic:equal value="true" name="bodyItem" property="value.RLS">
                                
                                  <td class="origAdjustCell" colspan="2">
                                  
                                    <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_FULL_ARTICLE_CHAPTER ) ) { %>
                                      Yes
                                    <%}%>
                                    
                                    <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_EXCERPT ) ) { %>
                                      <bean:write name="bodyItem" property="value.originalOrderDetails.numberOfExcerpts"/>
                                    <%}%>
                                    
                                    <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_QUOTATION ) ) { %>
                                      <bean:write name="bodyItem" property="value.originalOrderDetails.numberOfQuotes"/>
                                    <%}%>
                                    
                                    <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_CHART ) ) { %>
                                      <bean:write name="bodyItem" property="value.originalOrderDetails.numberOfCharts"/>
                                    <%}%>
                                    
                                    <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_FIGURE_DIAGRAM_TABLE ) ) { %>
                                      <bean:write name="bodyItem" property="value.originalOrderDetails.numberOfFigures"/>
                                    <%}%>
                                    
                                    <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_PHOTOGRAPH ) ) { %>
                                      <bean:write name="bodyItem" property="value.originalOrderDetails.numberOfPhotos"/>
                                    <%}%>
                                    
                                    <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_ILLUSTRATION ) ) { %>
                                      <bean:write name="bodyItem" property="value.originalOrderDetails.numberOfIllustrations"/>
                                    <%}%> 
                                    
                                    <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_GRAPH ) ) { %>
                                      <bean:write name="bodyItem" property="value.originalOrderDetails.numberOfGraphs"/>
                                    <%}%>
                                    
                                    <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_SELECTED_PAGES ) ) { %>
                                      <bean:write name="bodyItem" property="value.originalOrderDetails.numberOfPages"/>
                                    <%}%>
                                    
                                    <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_CARTOONS ) ) { %>
                                      <bean:write name="bodyItem" property="value.originalOrderDetails.numberOfCartoons"/>
                                    <%}%>
                                    
                                    <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_LOGOS ) ) { %>
                                      <bean:write name="bodyItem" property="value.originalOrderDetails.numberOfLogos"/>
                                    <%}%>
                                  
                                  </td>
                                                        
                                </logic:equal>
                                
                                <logic:equal value="true" name="bodyItem" property="value.APS">
                                  <td class="origAdjustCell"> <bean:write name="bodyItem" property="value.originalOrderDetails.numberOfStudents"/> </td>
                                  <td class="origAdjustCell"> <bean:write name="bodyItem" property="value.originalOrderDetails.numberOfPages"/> </td>
                                </logic:equal>
                                
                                <logic:equal value="true" name="bodyItem" property="value.ECCS">
                                  <td class="origAdjustCell"> <bean:write name="bodyItem" property="value.originalOrderDetails.numberOfStudents"/> </td>
                                  <td class="origAdjustCell"> <bean:write name="bodyItem" property="value.originalOrderDetails.numberOfPages"/> </td>
                                </logic:equal>
                                
                                <td class="origAdjustCell"> <script type="text/javascript">decimalFormat( <bean:write name="bodyItem" property="value.originalOrderDetails.licenseeFee"/> );</script>  </td>
                                <td class="origAdjustCell"> <script type="text/javascript">decimalFormat( <bean:write name="bodyItem" property="value.originalOrderDetails.royaltyComposite"/> );</script> </td>
                                <td class="origAdjustCell"> <script type="text/javascript">decimalFormat( <bean:write name="bodyItem" property="value.originalOrderDetails.discount"/> );</script>  </td>
                                <td class="origAdjustCell"> <bean:write name="bodyItem" property="value.originalOrderDetails.price"/> </td>
                                
                              </tr>
                                            
                              <logic:iterate id="previousAdjustment" name="bodyItem" property="value.previousAdjustmentsDetails" indexId="previousAdjustmentIndex" >
                              
                                <tr>
                                
                                  <td class="prevAdjustCell"> <bean:write name="previousAdjustment" property="ID"/> </td>
                                  
                                  <td  class="prevAdjustCell"> <bean:write name="previousAdjustment" property="createDateStr"/> </td>
                                  
                                  <td class="prevAdjustCell"> <bean:write name="previousAdjustment" property="billingStatus"/> </td>
                                  
                                  <td class="prevAdjustCell"> <bean:write name="previousAdjustment" property="reasonCd"/> </td> <!-- reasonDesc -->
                                  
                                  <logic:equal value="true" name="bodyItem" property="value.TRS">
                                    <td class="prevAdjustCell"> <bean:write name="previousAdjustment" property="numberOfPages"/> </td>
                                    <td class="prevAdjustCell"> <bean:write name="previousAdjustment" property="numberOfCopies"/> </td>
                                  </logic:equal>
                                  
                                  <logic:equal value="true" name="previousAdjustment" property="email">
                                    <td class="prevAdjustCell" colspan="2"> <bean:write name="previousAdjustment" property="numberOfRecipients"/> </td>
                                  </logic:equal>
                                  
                                  <logic:equal value="true" name="previousAdjustment" property="internet">
                                    <td class="prevAdjustCell" colspan="2"> <bean:write name="previousAdjustment" property="duration"/> </td>
                                  </logic:equal>
                                  
                                  <logic:equal value="true" name="previousAdjustment" property="intranet">
                                    <td class="prevAdjustCell" colspan="2"> <bean:write name="previousAdjustment" property="duration"/> </td>
                                  </logic:equal>
                                  
                                  <logic:equal value="true" name="previousAdjustment" property="extranet">
                                    <td class="prevAdjustCell" colspan="2"> <bean:write name="previousAdjustment" property="duration"/> </td>
                                  </logic:equal>
                                  
                                   <logic:equal value="true" name="previousAdjustment" property="republication">
                                  
                                    <td class="prevAdjustCell" colspan="2">
                                    
                                     
                                      <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_FULL_ARTICLE_CHAPTER ) ) { %>
                                         Yes
                                      <%}%>
                                                                       
                                      <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_EXCERPT ) ) { %>
                                        <bean:write name="previousAdjustment" property="numberOfExcerpts"/>
                                      <%}%>
                                      
                                      <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_QUOTATION ) ) { %>
                                        <bean:write name="previousAdjustment" property="numberOfQuotes"/>
                                      <%}%>
                                      
                                      <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_CHART ) ) { %>
                                        <bean:write name="previousAdjustment" property="numberOfCharts"/>
                                      <%}%>
                                      
                                      <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_FIGURE_DIAGRAM_TABLE ) ) { %>
                                        <bean:write name="previousAdjustment" property="numberOfFigures"/>
                                      <%}%>
                                      
                                      <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_PHOTOGRAPH ) ) { %>
                                        <bean:write name="previousAdjustment" property="numberOfPhotos"/>
                                      <%}%>
                                      
                                      <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_ILLUSTRATION ) ) { %>
                                        <bean:write name="previousAdjustment" property="numberOfIllustrations"/>
                                      <%}%> 
                                      
                                      <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_GRAPH ) ) { %>
                                        <bean:write name="previousAdjustment" property="numberOfGraphs"/>
                                      <%}%>
                                      
                                      <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_SELECTED_PAGES ) ) { %>
                                        <bean:write name="previousAdjustment" property="numberOfPages"/>
                                      <%}%>
                                      
                                      <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_CARTOONS ) ) { %>
                                        <bean:write name="previousAdjustment" property="numberOfCartoons"/>
                                      <%}%>
                                      
                                      <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_LOGOS ) ) { %>
                                        <bean:write name="previousAdjustment" property="numberOfLogos"/>
                                      <%}%>
                                      
                                    </td>
                                                          
                                  </logic:equal>
                                  
                                  <logic:equal value="true" name="previousAdjustment" property="APS">
                                    <td class="prevAdjustCell"> <bean:write name="previousAdjustment" property="numberOfStudents"/> </td>
                                    <td class="prevAdjustCell"> <bean:write name="previousAdjustment" property="numberOfPages"/> </td>
                                  </logic:equal>
                                  
                                  <logic:equal value="true" name="previousAdjustment" property="ECCS">
                                    <td class="prevAdjustCell"> <bean:write name="previousAdjustment" property="numberOfStudents"/> </td>
                                    <td class="prevAdjustCell"> <bean:write name="previousAdjustment" property="numberOfPages"/> </td>
                                  </logic:equal>
                                  
                                  <td class="prevAdjustCell"> <script type="text/javascript">decimalFormat( <bean:write name="previousAdjustment" property="licenseeFee"/> ); </script> </td>
                                  <td class="prevAdjustCell"> <script type="text/javascript">decimalFormat( <bean:write name="previousAdjustment" property="royaltyComposite"/> ); </script> </td>
                                  <td class="prevAdjustCell"> <script type="text/javascript">decimalFormat( <bean:write name="previousAdjustment" property="discount"/> ); </script> </td>
                                  <td class="prevAdjustCell"> <bean:write name="previousAdjustment" property="price"/> </td>
                                
                              </tr>
                              
                              </logic:iterate>
                                                        
                              <logic:equal value="true" name="bodyItem" property="value.previousAdjustmentsPresent">
                                
                                <tr style="font-weight: bold;" >
                                  
                                    <td class="prevAdjustTotalCell"> &nbsp; </td>
                                    
                                    <td class="prevAdjustTotalCell"> &nbsp; </td>
                                    
                                    <td class="prevAdjustTotalCell"> &nbsp; </td>
                                    
                                    <td class="prevAdjustTotalCell"> &nbsp; </td>
                                    
                                    <logic:equal value="true" name="bodyItem" property="value.TRS">
                                      <td class="prevAdjustTotalCell"> <bean:write name="bodyItem" property="value.subTotalNumberOfPages"/> </td>
                                      <td class="prevAdjustTotalCell"> <bean:write name="bodyItem" property="value.subTotalNumberOfCopies"/> </td>
                                    </logic:equal>
                                    
                                    <logic:equal value="true" name="bodyItem" property="value.DPSEMail">
                                    
                                      <td class="prevAdjustTotalCell" colspan="2"> <bean:write name="bodyItem" property="value.subTotalNumberOfRecipients"/> </td>
                                                            
                                    </logic:equal>
                                    
                                    <logic:equal value="true" name="bodyItem" property="value.DPSInternet">
                                    
                                      <td class="prevAdjustTotalCell" colspan="2"> N/A </td>
                                                            
                                    </logic:equal>
                                    
                                    <logic:equal value="true" name="bodyItem" property="value.DPSIntranet">
                                    
                                      <td class="prevAdjustTotalCell" colspan="2"> N/A </td>
                                                            
                                    </logic:equal>
                                    
                                    <logic:equal value="true" name="bodyItem" property="value.DPSExtranet">
                                    
                                      <td class="prevAdjustTotalCell" colspan="2"> N/A </td>
                                                            
                                    </logic:equal>
                                    
                                     <logic:equal value="true" name="bodyItem" property="value.RLS">
                                    
                                      <td class="prevAdjustTotalCell" colspan="2">
                                      
                                        <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_FULL_ARTICLE_CHAPTER ) ) { %>
                                         N/A
                                        <%}%>
                                        
                                        <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_EXCERPT ) ) { %>
                                          <bean:write name="bodyItem" property="value.subTotalNumberOfExcerpts"/>
                                        <%}%>
                                        
                                        <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_QUOTATION ) ) { %>
                                          <bean:write name="bodyItem" property="value.subTotalNumberOfQuotes"/>
                                        <%}%>
                                        
                                        <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_CHART ) ) { %>
                                          <bean:write name="bodyItem" property="value.subTotalNumberOfCharts"/>
                                        <%}%>
                                        
                                        <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_FIGURE_DIAGRAM_TABLE ) ) { %>
                                          <bean:write name="bodyItem" property="value.subTotalNumberOfFigures"/>
                                        <%}%>
                                        
                                        <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_PHOTOGRAPH ) ) { %>
                                          <bean:write name="bodyItem" property="value.subTotalNumberOfPhotos"/>
                                        <%}%>
                                        
                                        <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_ILLUSTRATION ) ) { %>
                                          <bean:write name="bodyItem" property="value.subTotalNumberOfIllustrations"/>
                                        <%}%> 
                                        
                                        <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_GRAPH ) ) { %>
                                          <bean:write name="bodyItem" property="value.subTotalNumberOfGraphs"/>
                                        <%}%>
                                        
                                        <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_SELECTED_PAGES ) ) { %>
                                          <bean:write name="bodyItem" property="value.subTotalNumberOfPages"/>
                                        <%}%>
                                      
                                        <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_CARTOONS ) ) { %>
                                          <bean:write name="bodyItem" property="value.subTotalNumberOfCartoons"/>
                                        <%}%>
                                        
                                        <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_LOGOS ) ) { %>
                                          <bean:write name="bodyItem" property="value.subTotalNumberOfLogos"/>
                                        <%}%>
                                      </td>
                                                            
                                    </logic:equal>
                                    
                                    <logic:equal value="true" name="bodyItem" property="value.APS">
                                    
                                      <td class="prevAdjustTotalCell"> <bean:write name="bodyItem" property="value.subTotalNumberOfStudents"/> </td>
                                      <td class="prevAdjustTotalCell"> <bean:write name="bodyItem" property="value.subTotalNumberOfPages"/> </td>
                                                            
                                    </logic:equal>
                                    
                                    <logic:equal value="true" name="bodyItem" property="value.ECCS">
                                    
                                      <td class="prevAdjustTotalCell"> <bean:write name="bodyItem" property="value.subTotalNumberOfStudents"/> </td>
                                      <td class="prevAdjustTotalCell"> <bean:write name="bodyItem" property="value.subTotalNumberOfPages"/> </td>
                                                            
                                    </logic:equal>
                                    
                                    <td class="prevAdjustTotalCell"> <script type="text/javascript">decimalFormat( <bean:write name="bodyItem" property="value.subTotalLicenseeFee"/> ); </script> </td>
                                    <td class="prevAdjustTotalCell"> <script type="text/javascript">decimalFormat( <bean:write name="bodyItem" property="value.subTotalRoyaltyComposite"/> ); </script> </td>
                                    <td class="prevAdjustTotalCell"> <script type="text/javascript">decimalFormat( <bean:write name="bodyItem" property="value.subTotalDiscount"/> ); </script> </td>
                                    <td class="prevAdjustTotalCell"> <script type="text/javascript">decimalFormat( <bean:write name="bodyItem" property="value.subTotal"/> ); </script> </td>
                                  
                                </tr>

                            </logic:equal>
                          
                          </logic:equal>
                           
                          <bean:define id="currentBodyItem" >
                            adjustment.body(<bean:write name="bodyItem" property="key" />)
                          </bean:define>
                          
                          <logic:equal value="true"  name="bodyItem" property="value.adjustable"  >
                          
                              <tr id="currentAdjustmentRow_<%=bodyItemIndex%>" class="currentAdjustmentRow" >
                              
                                  <bean:define id="currentAdjustment">
                                      adjustment.body(<bean:write name="bodyItem" property="key" />).currentAdjustmentsDetails
                                  </bean:define>
                                
                                      <td class="currAdjustCell" > 
                                        <logic:equal value="true" name="adjustmentForm" property="UIModeAdjustment"> 
                                          <a title="Zeroes this adjustment's quantities and fees fields" href='javascript:resetAdjustmentValues("currentAdjustmentRow_<%=bodyItemIndex%>")' >Reset</a> 
                                        </logic:equal>
                                        
                                        <logic:equal value="true" name="adjustmentForm" property="UIModeConfirmation">
                                          <bean:write name="bodyItem" property="value.originalOrderDetails.ID"/>
                                        </logic:equal>
                                        
                                      </td>
                                      
                                      <td class="currAdjustCell"> 
                                        &nbsp;
                                        
                                        <logic:equal value="true" name="adjustmentForm" property="UIModeConfirmation">
                                          <!--date here--><%= new java.text.SimpleDateFormat("MM/dd/yy").format(new java.util.Date())%>
                                        </logic:equal>
                                        
                                      </td>
                                      
                                      <td class="currAdjustCell" style="font-weight: bold;"> New </td>
                                      
                                      <td class="currAdjustCell"> 
                                            <logic:equal value="true" name="adjustmentForm" property="UIEditCurrentAdjustmentMode"> 
        
                                              <%//TODO 02/23/2007 lalberione : Do we have to use reasonCd instead?%>
                                              <html:select name="adjustmentForm" property='<%=currentAdjustment + ".reasonCd"%>' style="width: 70px;">
                                                  <html:option value="<%=String.valueOf(OrderAdjustmentConstants.REASON_REVISED_NUM_COPIES_CD)%>"><%=OrderAdjustmentConstants.REASON_REVISED_NUM_COPIES%></html:option>
                                                  <html:option value="<%=String.valueOf(OrderAdjustmentConstants.REASON_REVISED_NUM_PAGES_CD)%>"><%=OrderAdjustmentConstants.REASON_REVISED_NUM_PAGES%></html:option>
                                                  <html:option value="<%=String.valueOf(OrderAdjustmentConstants.REASON_DUPLICATE_ORDER_CD)%>"><%=OrderAdjustmentConstants.REASON_DUPLICATE_ORDER%></html:option>
                                                  <html:option value="<%=String.valueOf(OrderAdjustmentConstants.REASON_MATERIAL_NOT_USED_CD)%>"><%=OrderAdjustmentConstants.REASON_MATERIAL_NOT_USED%></html:option>
                                                  <html:option value="<%=String.valueOf(OrderAdjustmentConstants.REASON_PARTIAL_INVOICE_PAYMENT_ONGOING_CD)%>"><%=OrderAdjustmentConstants.REASON_PARTIAL_INVOICE_PAYMENT_ONGOING%></html:option>
                                                  <html:option value="<%=String.valueOf(OrderAdjustmentConstants.REASON_NON_PAYMENT_INVOICE_ONGOING_CD)%>"><%=OrderAdjustmentConstants.REASON_NON_PAYMENT_INVOICE_ONGOING%></html:option>
                                                  <html:option value="<%=String.valueOf(OrderAdjustmentConstants.REASON_LICENSEE_FEE_ADJUSTMENT_CD)%>"><%=OrderAdjustmentConstants.REASON_LICENSEE_FEE_ADJUSTMENT%></html:option>
                                                  <html:option value="<%=String.valueOf(OrderAdjustmentConstants.REASON_REVISED_REPORTING_ERROR_CD)%>"><%=OrderAdjustmentConstants.REASON_REVISED_REPORTING_ERROR%></html:option>
                                                  <html:option value="<%=String.valueOf(OrderAdjustmentConstants.REASON_PARTIAL_INVOICE_PAYMENT_DISCONTINUED_CD)%>"><%=OrderAdjustmentConstants.REASON_PARTIAL_INVOICE_PAYMENT_DISCONTINUED%></html:option>
                                                  <html:option value="<%=String.valueOf(OrderAdjustmentConstants.REASON_NON_PAYMENT_INVOICE_DISCONTINUED_CD)%>"><%=OrderAdjustmentConstants.REASON_NON_PAYMENT_INVOICE_DISCONTINUED%></html:option>
                                                  <html:option value="<%=String.valueOf(OrderAdjustmentConstants.REASON_ROYALTY_FEE_ADJUSTMENT_CD)%>"><%=OrderAdjustmentConstants.REASON_ROYALTY_FEE_ADJUSTMENT%></html:option>
                                              </html:select>
                                              
                                            </logic:equal>
                                            
                                            <logic:equal value="true" name="adjustmentForm" property="UIModeConfirmation">
                                              <bean:write name="bodyItem" property="value.currentAdjustmentsDetails.reasonCd"/>
                                            </logic:equal>
                                          
                                      </td>
                                      
                                      <logic:equal value="true" name="bodyItem" property="value.currentAdjustmentsDetails.photocopy">
                                        
                                        <td class="currAdjustCell">
                                          <logic:equal value="true" name="adjustmentForm" property="UIEditCurrentAdjustmentMode">
                                          
                                            <logic:equal value="0" name="bodyItem" property="value.subTotalNumberOfPages">
                                              <bean:write name="adjustmentForm" property='<%=currentAdjustment + ".numberOfPages"%>' />
                                            </logic:equal>
                                            
                                            <logic:notEqual value="0" name="bodyItem" property="value.subTotalNumberOfPages">
                                              <span id="qty"><html:text name="adjustmentForm" property='<%=currentAdjustment + ".numberOfPages"%>' styleClass="adjustmentInput" onkeypress="return numbersOnly(event, this, DO_NOT_ALLOW_DECIMAL_POINT)" /></span>
                                              <br/><a title="Recalculates this adjustment's price for the provided quantities" href="javascript:setLastVisitedAnchor('adj_<%=bodyItemIndex%>');recalculateAdjustment(<bean:write name="bodyItem" property="key" />);" >Recalculate</a>
                                            </logic:notEqual>
                                            
                                          </logic:equal>
                                          
                                          <logic:equal value="true" name="adjustmentForm" property="UIModeConfirmation">
                                            <bean:write name="adjustmentForm" property='<%=currentAdjustment + ".numberOfPages"%>' />
                                          </logic:equal>
                                        </td class="currAdjustCell">
                                        
                                        <td class="currAdjustCell">
                                          <logic:equal value="true" name="adjustmentForm" property="UIEditCurrentAdjustmentMode">
                                          
                                            <logic:equal value="0" name="bodyItem" property="value.subTotalNumberOfCopies">
                                              <bean:write name="adjustmentForm" property='<%=currentAdjustment + ".numberOfCopies"%>' />
                                            </logic:equal>
                                            
                                            <logic:notEqual value="0" name="bodyItem" property="value.subTotalNumberOfCopies">
                                              <span id="qty"><html:text name="adjustmentForm" property='<%=currentAdjustment + ".numberOfCopies"%>' styleClass="adjustmentInput" onkeypress="return numbersOnly(event, this, DO_NOT_ALLOW_DECIMAL_POINT)"/></span>
                                            </logic:notEqual>
                                                                                        
                                          </logic:equal>
                                          
                                          <logic:equal value="true" name="adjustmentForm" property="UIModeConfirmation">
                                            <bean:write name="adjustmentForm" property='<%=currentAdjustment + ".numberOfCopies"%>' />
                                          </logic:equal>
                                        </td>
                                      
                                      </logic:equal>
                                      
                                      <logic:equal value="true" name="bodyItem" property="value.currentAdjustmentsDetails.email">
                                      
                                        <td class="currAdjustCell" colspan="2"> 
                                          <logic:equal value="true" name="adjustmentForm" property="UIEditCurrentAdjustmentMode">
                                          
                                            <logic:equal value="0" name="bodyItem" property="value.subTotalNumberOfRecipients">
                                              <bean:write name="adjustmentForm" property='<%=currentAdjustment + ".numberOfRecipients"%>' /> 
                                            </logic:equal>
                                            
                                            <logic:notEqual value="0" name="bodyItem" property="value.subTotalNumberOfRecipients">
                                              <span id="qty"><html:text  name="adjustmentForm" property='<%=currentAdjustment + ".numberOfRecipients"%>' styleClass="adjustmentInput" onkeypress="return numbersOnly(event, this, DO_NOT_ALLOW_DECIMAL_POINT)"/> </span>
                                            </logic:notEqual>
                                            
                                            <br/><a title="Recalculates this adjustment's price for the provided quantities" href="javascript:setLastVisitedAnchor('adj_<%=bodyItemIndex%>');recalculateAdjustment(<bean:write name="bodyItem" property="key" />);" >Recalculate</a>
                                            
                                          </logic:equal>
                                          
                                          <logic:equal value="true" name="adjustmentForm" property="UIModeConfirmation">
                                            <bean:write name="adjustmentForm" property='<%=currentAdjustment + ".numberOfRecipients"%>' /> 
                                          </logic:equal>
                                        </td>
                                                              
                                      </logic:equal>
                                      
                                      <logic:equal value="true" name="bodyItem" property="value.currentAdjustmentsDetails.internet">
                                      
                                        <td class="currAdjustCell" colspan="2"> 
                                            
                                            <logic:equal value="true" name="adjustmentForm" property="UIEditCurrentAdjustmentMode">
                                              <span id="qty">
                                                <html:select   name="adjustmentForm" property='<%=currentAdjustment + ".duration"%>'>
                                                  <html:option value="<%=String.valueOf(OrderAdjustmentConstants.UNSPECIFIED_DURATION)%>">Not Specified</html:option>
                                                  <html:option value="<%=String.valueOf(UsageDataNet.TO_30_DAYS_FEE)%>">Up to 30 Days</html:option>
                                                  <html:option value="<%=String.valueOf(UsageDataNet.TO_180_DAYS_FEE)%>">Up to 180 Days</html:option>
                                                  <html:option value="<%=String.valueOf(UsageDataNet.TO_365_DAYS_FEE)%>">Up to 365 Days</html:option>
                                                  <html:option value="<%=String.valueOf(UsageDataNet.UNLIMITED_DAYS_FEE)%>">Unlimited</html:option>
                                                </html:select>
                                              </span>
                                            
                                            <br/><a title="Recalculates this adjustment's price for the provided quantities" href="javascript:setLastVisitedAnchor('adj_<%=bodyItemIndex%>');recalculateAdjustment(<bean:write name="bodyItem" property="key" />);" >Recalculate</a>
                                            
                                            </logic:equal>
                                            
                                            <logic:equal value="true" name="adjustmentForm" property="UIModeConfirmation">
                                              <bean:write  name="adjustmentForm" property='<%=currentAdjustment + ".durationString"%>'/>
                                            </logic:equal>
                                            
                                        </td>
                                                              
                                      </logic:equal>
                                      
                                      <logic:equal value="true" name="bodyItem" property="value.currentAdjustmentsDetails.intranet">
                                      
                                        <td class="currAdjustCell" colspan="2"> 
                                            
                                            <logic:equal value="true" name="adjustmentForm" property="UIEditCurrentAdjustmentMode">
                                              
                                              <span id="qty">
                                                <html:select  name="adjustmentForm" property='<%=currentAdjustment + ".duration"%>'>
                                                  <html:option value="<%=String.valueOf(OrderAdjustmentConstants.UNSPECIFIED_DURATION)%>">Not Specified</html:option>
                                                  <html:option value="<%= String.valueOf(UsageDataNet.TO_30_DAYS_FEE)  %>">Up to 30 Days</html:option>
                                                  <html:option value="<%= String.valueOf(UsageDataNet.TO_180_DAYS_FEE)  %>">Up to 180 Days</html:option>
                                                  <html:option value="<%= String.valueOf(UsageDataNet.TO_365_DAYS_FEE)  %>">Up to 365 Days</html:option>
                                                  <html:option value="<%= String.valueOf(UsageDataNet.UNLIMITED_DAYS_FEE) %>">Unlimited</html:option>
                                                </html:select>
                                              </span>
                                            
                                              <br/><a title="Recalculates this adjustment's price for the provided quantities" href="javascript:setLastVisitedAnchor('adj_<%=bodyItemIndex%>');recalculateAdjustment(<bean:write name="bodyItem" property="key" />);" >Recalculate</a>
                                            
                                            </logic:equal>
                                            
                                            <logic:equal value="true" name="adjustmentForm" property="UIModeConfirmation">
                                              <bean:write  name="adjustmentForm" property='<%=currentAdjustment + ".durationString"%>'/>
                                            </logic:equal>
                                            
                                        </td>
                                                              
                                      </logic:equal>
                                      
                                      <logic:equal value="true" name="bodyItem" property="value.currentAdjustmentsDetails.extranet">
                                      
                                        <td class="currAdjustCell" colspan="2"> 
                                            
                                            <logic:equal value="true" name="adjustmentForm" property="UIEditCurrentAdjustmentMode">
                                              
                                              <span id="qty">
                                                <html:select  name="adjustmentForm" property='<%=currentAdjustment + ".duration"%>'>
                                                  <html:option value="<%=String.valueOf(OrderAdjustmentConstants.UNSPECIFIED_DURATION)%>">Not Specified</html:option>
                                                  <html:option value="<%= String.valueOf(UsageDataNet.TO_30_DAYS_FEE)  %>">Up to 30 Days</html:option>
                                                  <html:option value="<%= String.valueOf(UsageDataNet.TO_180_DAYS_FEE)  %>">Up to 180 Days</html:option>
                                                  <html:option value="<%= String.valueOf(UsageDataNet.TO_365_DAYS_FEE)  %>">Up to 365 Days</html:option>
                                                  <html:option value="<%= String.valueOf(UsageDataNet.UNLIMITED_DAYS_FEE) %>">Unlimited</html:option>
                                                </html:select>
                                              </span>
                                            
                                              <br/><a title="Recalculates this adjustment's price for the provided quantities" href="javascript:setLastVisitedAnchor('adj_<%=bodyItemIndex%>');recalculateAdjustment(<bean:write name="bodyItem" property="key" />);" >Recalculate</a>
                                            
                                            </logic:equal>
                                            
                                            
                                            <logic:equal value="true" name="adjustmentForm" property="UIModeConfirmation">
                                              <bean:write  name="adjustmentForm" property='<%=currentAdjustment + ".durationString"%>'/>
                                            </logic:equal>
                                            
                                        </td>
                                                              
                                      </logic:equal>
                                      
                                       <logic:equal value="true" name="bodyItem" property="value.currentAdjustmentsDetails.republication">
                                      
                                        <td class="currAdjustCell" colspan="2">
                                      
                                          <logic:equal value="true" name="adjustmentForm" property="UIEditCurrentAdjustmentMode">    
                                      
                                            <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_FULL_ARTICLE_CHAPTER ) ) {%>
                                               Yes <%//Quantity not adjustable. Only fees are allowed to be adjusted.%>
                                            <%}%>
                                            
                                            <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_EXCERPT ) ) { %>
                                            
                                              <logic:equal value="0" name="bodyItem" property="value.subTotalNumberOfExcerpts">
                                                <bean:write name="adjustmentForm" property='<%=currentAdjustment + ".numberOfExcerpts"%>'/>
                                              </logic:equal>
                                              
                                              <logic:notEqual value="0" name="bodyItem" property="value.subTotalNumberOfExcerpts">
                                                <span id="qty"><html:text name="adjustmentForm" property='<%=currentAdjustment + ".numberOfExcerpts"%>' styleClass="adjustmentInput" onkeypress="return numbersOnly(event, this, DO_NOT_ALLOW_DECIMAL_POINT)"/></span>
                                              </logic:notEqual>
                                              
                                            <%}%>
                                            
                                            <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_QUOTATION ) ) { %>
                                            
                                              <logic:equal value="0" name="bodyItem" property="value.subTotalNumberOfQuotes">
                                                <bean:write name="adjustmentForm" property='<%=currentAdjustment + ".numberOfQuotes"%>'/>
                                              </logic:equal>
                                              
                                              <logic:notEqual value="0" name="bodyItem" property="value.subTotalNumberOfQuotes">
                                                <span id="qty"><html:text name="adjustmentForm" property='<%=currentAdjustment + ".numberOfQuotes"%>' styleClass="adjustmentInput" onkeypress="return numbersOnly(event, this, DO_NOT_ALLOW_DECIMAL_POINT)"/></span>
                                              </logic:notEqual>
                                              
                                            <%}%>
                                            
                                            <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_CHART ) ) { %>
                                            
                                              <logic:equal value="0" name="bodyItem" property="value.subTotalNumberOfCharts">
                                                <bean:write name="adjustmentForm" property='<%=currentAdjustment + ".numberOfCharts"%>'/>
                                              </logic:equal>
                                              
                                              <logic:notEqual value="0" name="bodyItem" property="value.subTotalNumberOfCharts">
                                                <span id="qty"><html:text name="adjustmentForm" property='<%=currentAdjustment + ".numberOfCharts"%>' styleClass="adjustmentInput" onkeypress="return numbersOnly(event, this, DO_NOT_ALLOW_DECIMAL_POINT)"/></span>
                                              </logic:notEqual>
                                              
                                            <%}%>
                                            
                                            <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_FIGURE_DIAGRAM_TABLE ) ) { %>
                                            
                                              <logic:equal value="0" name="bodyItem" property="value.subTotalNumberOfFigures">
                                                <bean:write name="adjustmentForm" property='<%=currentAdjustment + ".numberOfFigures"%>'/>
                                              </logic:equal>
                                              
                                              <logic:notEqual value="0" name="bodyItem" property="value.subTotalNumberOfFigures">
                                                <span id="qty"><html:text name="adjustmentForm" property='<%=currentAdjustment + ".numberOfFigures"%>' styleClass="adjustmentInput" onkeypress="return numbersOnly(event, this, DO_NOT_ALLOW_DECIMAL_POINT)"/></span>
                                              </logic:notEqual>
                                              
                                            <%}%>
                                            
                                            <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_PHOTOGRAPH ) ) { %>
                                            
                                                <logic:equal value="0" name="bodyItem" property="value.subTotalNumberOfPhotos">
                                                  <bean:write name="adjustmentForm" property='<%=currentAdjustment + ".numberOfPhotos"%>'/>
                                                </logic:equal>
                                                
                                                <logic:notEqual value="0" name="bodyItem" property="value.subTotalNumberOfPhotos">
                                                  <span id="qty"><html:text name="adjustmentForm" property='<%=currentAdjustment + ".numberOfPhotos"%>' styleClass="adjustmentInput" onkeypress="return numbersOnly(event, this, DO_NOT_ALLOW_DECIMAL_POINT)"/></span>
                                                </logic:notEqual>
                                              
                                            <%}%>
                                            
                                            <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_ILLUSTRATION ) ) { %>
                                            
                                              <logic:equal value="0" name="bodyItem" property="value.subTotalNumberOfIllustrations">
                                                <bean:write name="adjustmentForm" property='<%=currentAdjustment + ".numberOfIllustrations"%>'/>
                                              </logic:equal>
                                              
                                              <logic:notEqual value="0" name="bodyItem" property="value.subTotalNumberOfIllustrations">
                                                <span id="qty"><html:text name="adjustmentForm" property='<%=currentAdjustment + ".numberOfIllustrations"%>' styleClass="adjustmentInput" onkeypress="return numbersOnly(event, this, DO_NOT_ALLOW_DECIMAL_POINT)"/></span>
                                              </logic:notEqual>
                                              
                                            <%}%> 
                                            
                                            <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_GRAPH ) ) { %>
                                            
                                              <logic:equal value="0" name="bodyItem" property="value.subTotalNumberOfGraphs">
                                                <bean:write name="adjustmentForm" property='<%=currentAdjustment + ".numberOfGraphs"%>'/>
                                              </logic:equal>
                                              
                                              <logic:notEqual value="0" name="bodyItem" property="value.subTotalNumberOfGraphs">
                                                 <span id="qty"><html:text name="adjustmentForm" property='<%=currentAdjustment + ".numberOfGraphs"%>' styleClass="adjustmentInput" onkeypress="return numbersOnly(event, this, DO_NOT_ALLOW_DECIMAL_POINT)"/></span>
                                              </logic:notEqual>
                                              
                                            <%}%>
                                            
                                            <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_SELECTED_PAGES ) ) { %>
                                            
                                              <logic:equal value="0" name="bodyItem" property="value.subTotalNumberOfPages">
                                                <bean:write name="adjustmentForm" property='<%=currentAdjustment + ".numberOfPages"%>'/>
                                              </logic:equal>
                                              
                                              <logic:notEqual value="0" name="bodyItem" property="value.subTotalNumberOfPages">
                                                 <span id="qty"><html:text name="adjustmentForm" property='<%=currentAdjustment + ".numberOfPages"%>' styleClass="adjustmentInput" onkeypress="return numbersOnly(event, this, DO_NOT_ALLOW_DECIMAL_POINT)"/></span>
                                              </logic:notEqual>
                                              
                                            <%}%>
                                            
                                            <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_CARTOONS ) ) { %>
                                            
                                              <logic:equal value="0" name="bodyItem" property="value.subTotalNumberOfCartoons">
                                                <bean:write name="adjustmentForm" property='<%=currentAdjustment + ".numberOfCartoons"%>'/>
                                              </logic:equal>
                                              
                                              <logic:notEqual value="0" name="bodyItem" property="value.subTotalNumberOfCartoons">
                                                 <span id="qty"><html:text name="adjustmentForm" property='<%=currentAdjustment + ".numberOfCartoons"%>' styleClass="adjustmentInput" onkeypress="return numbersOnly(event, this, DO_NOT_ALLOW_DECIMAL_POINT)"/></span>
                                              </logic:notEqual>
                                              
                                            <%}%>
                                            
                                            <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_LOGOS ) ) { %>
                                            
                                              <logic:equal value="0" name="bodyItem" property="value.subTotalNumberOfLogos">
                                                <bean:write name="adjustmentForm" property='<%=currentAdjustment + ".numberOfLogos"%>'/>
                                              </logic:equal>
                                              
                                              <logic:notEqual value="0" name="bodyItem" property="value.subTotalNumberOfLogos">
                                                 <span id="qty"><html:text name="adjustmentForm" property='<%=currentAdjustment + ".numberOfLogos"%>' styleClass="adjustmentInput" onkeypress="return numbersOnly(event, this, DO_NOT_ALLOW_DECIMAL_POINT)"/></span>
                                              </logic:notEqual>
                                              
                                            <%}%>
                                            
                                            <% if ( !RLSTypeOfContent.equals( RepublicationConstants.CONTENT_FULL_ARTICLE_CHAPTER ) ) {%>
                                               <br/><a title="Recalculates this adjustment's price for the provided quantities" href="javascript:setLastVisitedAnchor('adj_<%=bodyItemIndex%>');recalculateAdjustment(<bean:write name="bodyItem" property="key" />);" >Recalculate</a>
                                            <%}%>
                                          
                                          </logic:equal>
                                          
                                            
                                          
                                          
                                          
                                          <logic:equal value="true" name="adjustmentForm" property="UIModeConfirmation">    
                                      
                                            <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_FULL_ARTICLE_CHAPTER ) ) {%>
                                              Yes
                                            <%}%>
                                            
                                            <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_EXCERPT ) ) { %>
                                              <bean:write name="adjustmentForm" property='<%=currentAdjustment + ".numberOfExcerpts"%>' />
                                            <%}%>
                                            
                                            <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_QUOTATION ) ) { %>
                                              <bean:write name="adjustmentForm" property='<%=currentAdjustment + ".numberOfQuotes"%>'/>
                                            <%}%>
                                            
                                            <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_CHART ) ) { %>
                                              <bean:write name="adjustmentForm" property='<%=currentAdjustment + ".numberOfCharts"%>'/>
                                            <%}%>
                                            
                                            <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_FIGURE_DIAGRAM_TABLE ) ) { %>
                                              <bean:write name="adjustmentForm" property='<%=currentAdjustment + ".numberOfFigures"%>' />
                                            <%}%>
                                            
                                            <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_PHOTOGRAPH ) ) { %>
                                              <bean:write name="adjustmentForm" property='<%=currentAdjustment + ".numberOfPhotographs"%>' />
                                            <%}%>
                                            
                                            <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_ILLUSTRATION ) ) { %>
                                              <bean:write name="adjustmentForm" property='<%=currentAdjustment + ".numberOfIllustrations"%>' />
                                            <%}%> 
                                            
                                            <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_GRAPH ) ) { %>
                                              <bean:write name="adjustmentForm" property='<%=currentAdjustment + ".numberOfGraphs"%>' />
                                            <%}%>
                                            
                                            <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_SELECTED_PAGES ) ) { %>
                                              <bean:write name="adjustmentForm" property='<%=currentAdjustment + ".numberOfPages"%>' />
                                            <%}%>

                                            <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_CARTOONS ) ) { %>
                                              <bean:write name="adjustmentForm" property='<%=currentAdjustment + ".numberOfCartoons"%>' />
                                            <%}%>
                                            
                                            <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_LOGOS ) ) { %>
                                              <bean:write name="adjustmentForm" property='<%=currentAdjustment + ".numberOfLogos"%>' />
                                            <%}%>
                                          
                                          </logic:equal>
                                          
                                        </td>
                                                              
                                      </logic:equal>
                                      
                                      <logic:equal value="true" name="bodyItem" property="value.currentAdjustmentsDetails.APS">
                                      
                                        <td class="currAdjustCell"> 
                                          <logic:equal value="true" name="adjustmentForm" property="UIEditCurrentAdjustmentMode">
                                            
                                            <logic:equal value="0" name="bodyItem" property="value.subTotalNumberOfStudents">
                                              <bean:write name="adjustmentForm" property='<%=currentAdjustment + ".numberOfStudents"%>'/>
                                            </logic:equal>
                                            
                                            <logic:notEqual value="0" name="bodyItem" property="value.subTotalNumberOfStudents">
                                               <span id="qty"><html:text name="adjustmentForm" property='<%=currentAdjustment + ".numberOfStudents"%>' styleClass="adjustmentInput" onkeypress="return numbersOnly(event, this, DO_NOT_ALLOW_DECIMAL_POINT)"/></span>
                                            </logic:notEqual>
                                            
                                            <br/><a title="Recalculates this adjustment's price for the provided quantities" href="javascript:setLastVisitedAnchor('adj_<%=bodyItemIndex%>');recalculateAdjustment(<bean:write name="bodyItem" property="key" />);" >Recalculate</a>
                                            
                                          </logic:equal>
                                          
                                          <logic:equal value="true" name="adjustmentForm" property="UIModeConfirmation">
                                              <bean:write name="adjustmentForm" property='<%=currentAdjustment + ".numberOfStudents"%>'/> 
                                          </logic:equal>
                                        </td>
                                        
                                        <td class="currAdjustCell">
                                          <logic:equal value="true" name="adjustmentForm" property="UIEditCurrentAdjustmentMode">
                                               <span id="qty"><html:text name="adjustmentForm" property='<%=currentAdjustment + ".numberOfPages"%>' styleClass="adjustmentInput" onkeypress="return numbersOnly(event, this, DO_NOT_ALLOW_DECIMAL_POINT)"/> </span>
                                          </logic:equal>
                                          
                                          <logic:equal value="true" name="adjustmentForm" property="UIModeConfirmation">
                                              <bean:write name="adjustmentForm" property='<%=currentAdjustment + ".numberOfPages"%>'/> 
                                          </logic:equal>
                                        </td>
                                                              
                                      </logic:equal>
                                      
                                      <logic:equal value="true" name="bodyItem" property="value.currentAdjustmentsDetails.ECCS">
                                      
                                         <td class="currAdjustCell"> 
                                          <logic:equal value="true" name="adjustmentForm" property="UIEditCurrentAdjustmentMode">
                                            
                                            <logic:equal value="0" name="bodyItem" property="value.subTotalNumberOfStudents">
                                              <bean:write name="adjustmentForm" property='<%=currentAdjustment + ".numberOfStudents"%>'/>
                                            </logic:equal>
                                            
                                            <logic:notEqual value="0" name="bodyItem" property="value.subTotalNumberOfStudents">
                                               <span id="qty"><html:text name="adjustmentForm" property='<%=currentAdjustment + ".numberOfStudents"%>' styleClass="adjustmentInput" onkeypress="return numbersOnly(event, this, DO_NOT_ALLOW_DECIMAL_POINT)"/></span>
                                            </logic:notEqual>
                                            
                                            <br/><a title="Recalculates this adjustment's price for the provided quantities" href="javascript:setLastVisitedAnchor('adj_<%=bodyItemIndex%>');recalculateAdjustment(<bean:write name="bodyItem" property="key" />);" >Recalculate</a>
                                            
                                          </logic:equal>
                                          
                                          <logic:equal value="true" name="adjustmentForm" property="UIModeConfirmation">
                                              <bean:write name="adjustmentForm" property='<%=currentAdjustment + ".numberOfStudents"%>'/> 
                                          </logic:equal>
                                        </td>                                     
                                         
                                         <td class="currAdjustCell">
                                            <logic:equal value="true" name="adjustmentForm" property="UIEditCurrentAdjustmentMode">
                                            
                                              <logic:equal value="0" name="bodyItem" property="value.subTotalNumberOfPages">
                                                <bean:write name="adjustmentForm" property='<%=currentAdjustment + ".numberOfPages"%>'/>
                                              </logic:equal>
                                              
                                              <logic:notEqual value="0" name="bodyItem" property="value.subTotalNumberOfPages">
                                                 <span id="qty"><html:text name="adjustmentForm" property='<%=currentAdjustment + ".numberOfPages"%>' styleClass="adjustmentInput" onkeypress="return numbersOnly(event, this, DO_NOT_ALLOW_DECIMAL_POINT)"/></span>
                                              </logic:notEqual>
                                              
                                            </logic:equal>
                                            
                                            <logic:equal value="true" name="adjustmentForm" property="UIModeConfirmation">
                                                <bean:write name="adjustmentForm" property='<%=currentAdjustment + ".numberOfPages"%>' /> 
                                            </logic:equal>
                                        </td>
                                                              
                                      </logic:equal>
                                      
                                      <td class="currAdjustCell">
                                        <logic:equal value="true" name="adjustmentForm" property="UIEditCurrentAdjustmentMode">
                                          
                                          <logic:equal value="0" name="bodyItem" property="value.subTotalLicenseeFee">
                                            <script type="text/javascript">decimalFormat( <bean:write name="adjustmentForm" property='<%=currentAdjustment + ".licenseeFee"%>' /> ); </script>
                                          </logic:equal>
                                          
                                          <logic:notEqual value="0" name="bodyItem" property="value.subTotalLicenseeFee">
                                             <span id="fee"><html:text name="adjustmentForm" property='<%=currentAdjustment + ".licenseeFee"%>' styleClass="adjustmentInput decimalFormat" onkeypress="return numbersOnly(event, this, ALLOW_DECIMAL_POINT)"/></span>
                                          </logic:notEqual>
                                         
                                        </logic:equal>
                                        
                                        <logic:equal value="true" name="adjustmentForm" property="UIModeConfirmation">
                                          <script type="text/javascript">decimalFormat( <bean:write name="adjustmentForm" property='<%=currentAdjustment + ".licenseeFee"%>' /> ); </script>
                                        </logic:equal>
                                      </td>
                                      
                                      <td class="currAdjustCell"> 
                                        <logic:equal value="true" name="adjustmentForm" property="UIEditCurrentAdjustmentMode"> 
                                                                                
                                          <logic:equal value="0" name="bodyItem" property="value.subTotalRoyaltyComposite"> 
                                            <script type="text/javascript">decimalFormat( <bean:write name="adjustmentForm" property='<%=currentBodyItem + ".currentAdjustmentRoyaltyComposite"%>' /> ); </script>
                                          </logic:equal>
                                          
                                          <logic:notEqual value="0" name="bodyItem" property="value.subTotalRoyaltyComposite">
                                            <span id="fee"><html:text name="adjustmentForm" property='<%=currentBodyItem + ".currentAdjustmentRoyaltyComposite"%>' styleClass="adjustmentInput decimalFormat"  onkeypress="return numbersOnly(event, this, ALLOW_DECIMAL_POINT)"/></span>
                                          </logic:notEqual>
                                          
                                        </logic:equal>
                                        
                                        <logic:equal value="true" name="adjustmentForm" property="UIModeConfirmation">
                                          <script type="text/javascript">decimalFormat( <bean:write name="adjustmentForm" property='<%=currentBodyItem + ".currentAdjustmentRoyaltyComposite"%>'/> ); </script>
                                        </logic:equal>
                                      </td>
                                      
                                      <td class="currAdjustCell"> 
                                        <logic:equal value="true" name="adjustmentForm" property="UIEditCurrentAdjustmentMode">
                                        
                                          <logic:equal value="0" name="bodyItem" property="value.subTotalDiscount">
                                            <script type="text/javascript">decimalFormat( <bean:write name="adjustmentForm" property='<%=currentAdjustment + ".discount"%>' /> ); </script>
                                          </logic:equal>
                                          
                                          <logic:notEqual value="0" name="bodyItem" property="value.subTotalDiscount">
                                            <span id="fee"><html:text name="adjustmentForm" property='<%=currentAdjustment + ".discount"%>' styleClass="adjustmentInput decimalFormat" onkeypress="return numbersOnly(event, this, ALLOW_DECIMAL_POINT)"/></span>
                                          </logic:notEqual>
                                          
                                        </logic:equal>
                                        
                                        <logic:equal value="true" name="adjustmentForm" property="UIModeConfirmation">
                                          <script type="text/javascript">decimalFormat( <bean:write name="adjustmentForm" property='<%=currentAdjustment + ".discount"%>' /> ); </script>
                                        </logic:equal>
                                      </td>
                                      
                                      <td class="currAdjustCell"> 
                                            
                                           <script type="text/javascript">decimalFormat( <bean:write name="bodyItem" property="value.currentAdjustmentTotal"/> ); </script> <br/> <!-- value.currentAdjustmentsDetails.price -->
                                           
                                           <logic:equal value="true" name="adjustmentForm" property="UIEditCurrentAdjustmentMode" >
                                              <div style="padding-top: 4px;" ><a title="Refreshes this adjustment's total including changes made to its fees fields only" href="#" onclick="javascript:setLastVisitedAnchor('adj_<%=bodyItemIndex%>');refresh();">Refresh</a></div>
                                           </logic:equal>
                                            
                                           <logic:equal value="true" name="adjustmentForm" property="UIModeConfirmation" >
                                              <a title="Modifes this adjustment" href='javascript:modifyAdjustment(<bean:write name="bodyItem" property="key" />)' >Modify</a>&nbsp;|&nbsp;<a title="Deletes this adjustment" href='javascript:deleteAdjustment(<bean:write name="bodyItem" property="key" />)' >Delete</a>
                                           </logic:equal>
                                           
                                      </td>
                                      
                              </tr>
                          </logic:equal>
                          
                          <logic:equal value="false"  name="bodyItem" property="value.adjustable"  >
                            <tr> <td colspan="10" class="notAdjustableCell"> 
                                    <img src="<html:rewrite page="/resources/adjustment/images/info.gif"/>" style="vertical-align: middle;" />
                                    At the moment, this detail <span style="text-decoration: underline">cannot</span> be adjusted. Please see the status of original order detail and/or previous adjustments.
                                  </td>
                            </tr>
                          </logic:equal>                          
                          
                          <%//<logic:equal value="true" name="adjustmentForm" property="UIEditCurrentAdjustmentMode"> %>
                          
                              <tr>
                                
                                  <td class="totalsCell" colspan="4" style="text-align: left; padding-left: 5px;">Totals After Adjustments</td>
                                  
                                  <logic:equal value="true" name="bodyItem" property="value.TRS">
                                    <td class="totalsCell"> <bean:write name="bodyItem" property="value.totalNumberOfPages"/> </td>
                                    <td class="totalsCell"> <bean:write name="bodyItem" property="value.totalNumberOfCopies"/> </td>
                                  </logic:equal>
                                  
                                  <logic:equal value="true" name="bodyItem" property="value.DPSEMail">
                                    <td class="totalsCell" colspan="2"> <bean:write name="bodyItem" property="value.totalNumberOfRecipients"/> </td>
                                  </logic:equal>
                                  
                                  <logic:equal value="true" name="bodyItem" property="value.DPSInternet">
                                    <td class="totalsCell" colspan="2"> N/A </td>
                                  </logic:equal>
                                  
                                  <logic:equal value="true" name="bodyItem" property="value.DPSIntranet">
                                    <td class="totalsCell" colspan="2"> N/A </td>
                                  </logic:equal>
                                  
                                  <logic:equal value="true" name="bodyItem" property="value.DPSExtranet">
                                    <td class="totalsCell" colspan="2"> N/A </td>
                                  </logic:equal>
                                  
                                   <logic:equal value="true" name="bodyItem" property="value.RLS">
                                    <td class="totalsCell" colspan="2">
                                  
                                      <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_FULL_ARTICLE_CHAPTER ) ) { %>
                                        N/A
                                      <%}%>
                                      
                                      <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_EXCERPT ) ) { %>
                                        <bean:write name="bodyItem" property="value.totalNumberOfExcerpts"/>
                                      <%}%>
                                      
                                      <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_QUOTATION ) ) { %>
                                        <bean:write name="bodyItem" property="value.totalNumberOfQuotes"/>
                                      <%}%>
                                      
                                      <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_CHART ) ) { %>
                                        <bean:write name="bodyItem" property="value.totalNumberOfCharts"/>
                                      <%}%>
                                      
                                      <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_FIGURE_DIAGRAM_TABLE ) ) { %>
                                        <bean:write name="bodyItem" property="value.totalNumberOfFigures"/>
                                      <%}%>
                                      
                                      <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_PHOTOGRAPH ) ) { %>
                                        <bean:write name="bodyItem" property="value.totalNumberOfPhotos"/>
                                      <%}%>
                                      
                                      <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_ILLUSTRATION ) ) { %>
                                        <bean:write name="bodyItem" property="value.totalNumberOfIllustrations"/>
                                      <%}%> 
                                      
                                      <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_GRAPH ) ) { %>
                                        <bean:write name="bodyItem" property="value.totalNumberOfGraphs"/>
                                      <%}%>
                                      
                                      <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_SELECTED_PAGES ) ) { %>
                                        <bean:write name="bodyItem" property="value.totalNumberOfPages"/>
                                      <%}%>
                                      
                                      <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_CARTOONS ) ) { %>
                                        <bean:write name="bodyItem" property="value.totalNumberOfCartoons"/>
                                      <%}%>
                                      
                                      <% if ( RLSTypeOfContent.equals( RepublicationConstants.CONTENT_LOGOS ) ) { %>
                                        <bean:write name="bodyItem" property="value.totalNumberOfLogos"/>
                                      <%}%> 
                                    
                                    </td>
                                  </logic:equal>
                                  
                                  <logic:equal value="true" name="bodyItem" property="value.APS">
                                    <td class="totalsCell"> <bean:write name="bodyItem" property="value.totalNumberOfStudents"/> </td>
                                    <td class="totalsCell"> <bean:write name="bodyItem" property="value.totalNumberOfPages"/> </td>
                                  </logic:equal>
                                  
                                  <logic:equal value="true" name="bodyItem" property="value.ECCS">
                                    <td class="totalsCell"> <bean:write name="bodyItem" property="value.totalNumberOfStudents"/> </td>
                                    <td class="totalsCell"> <bean:write name="bodyItem" property="value.totalNumberOfPages"/> </td>
                                  </logic:equal>
                                  
                                  <td class="totalsCell"> <script type="text/javascript">decimalFormat( <bean:write name="bodyItem" property="value.totalLicenseeFee"/> ); </script>  </td>
                                  <td class="totalsCell"> <script type="text/javascript">decimalFormat( <bean:write name="bodyItem" property="value.totalRoyaltyComposite"/> ); </script> </td>
                                  <td class="totalsCell"> <script type="text/javascript">decimalFormat( <bean:write name="bodyItem" property="value.totalDiscount"/> ); </script> </td>
                                  <td class="totalsCell"> <script type="text/javascript">decimalFormat( <bean:write name="bodyItem" property="value.total"/> ); </script> </td>
                                
                              </tr>
                          
                         <%// </logic:equal> %>
                        
                        </table>
                      
                        <div style="padding-top: 20px;">
                          <hr/>
                        </div>
                      
                    </div>
                    
                    <!--<hr style="background-color: gray"/>-->
                  
                  </nested:iterate>
                  
                  <%// Paginator controls fragment inserted here %>
                  <jsp:include page="paginatorControlsFragment.jsp"/>
                  
              </fieldset>
            
          </form>
          
      </div>
  
      <%// Adjustment summary JSP fragment%>
      <jsp:include page="summaryFragment.jsp" />
        
      
      <div id="actionButtonsContainer" class="container" align="center">
        
          <logic:equal value="true" name="adjustmentForm" property="UIModeAdjustment"> 
          
            <%if( enterAdjustmentAllowed ){%>
              <img title="Saves this adjustment for later completion" src="<html:rewrite page="/resources/adjustment/images/save_adjustment.png" />" onclick="javascript:saveAdjustment()" />
            <%}%>
            
             <%if( commitAdjustmentAllowed ){%>
              &nbsp;
              <img title="Goes to confirmation page for adjustment review and completion" src="<html:rewrite page="/resources/adjustment/images/complete_adjustment.png" />" onclick="javascript:completeAdjustment()" />
             <%}%>
            
          </logic:equal>
          
          <logic:equal value="true" name="adjustmentForm" property="UIModeIndividualAdjustmentEdit"> 
            <%if( commitAdjustmentAllowed ){%>
              <img title="Goes to confirmation page for adjustment review and completion" src="<html:rewrite page="/resources/adjustment/images/complete_adjustment.png" />" onclick="javascript:completeAdjustment()" />
             <%}%>
          </logic:equal>
          
          <logic:equal value="true" name="adjustmentForm" property="UIModeConfirmation">
            
            <%if( enterAdjustmentAllowed ){%>
              <img title="Goes to back to Adjustment page" src="<html:rewrite page="/resources/adjustment/images/go_back.png" />" onclick="javascript:editAdjustment()" />
            <%}%>  
              
            <%if( commitAdjustmentAllowed ){%>
               &nbsp;&nbsp;<img title="Commits adjustment" src="<html:rewrite page="/resources/adjustment/images/make_adjustment.png" />" onclick="javascript:makeAdjustment()" />
             <%}%>
          
          </logic:equal>
                    
          &nbsp;
          <img title="Cancels this adjustment and goes to Order History page" src="<html:rewrite page="/resources/adjustment/images/cancel_adjustment.png" />" onclick="javascript:cancelAdjustment()" />
      
      </div>
        
    </div>
  
    <script type="text/javascript" >
      lastVisitedAnchor = null;
    </script>

    <%//UI Mode sensitive JS artifacts%>    
    <jsp:include page="dynamicJavascriptFragment.jsp"/>

    <script type="text/javascript" >
      if( document.getElementById("fieldToAdjustDropDown") ) handleFieldToAdjustChange();
    </script>
    
    <script type="text/javascript" >
    //Paginator - BEGIN

    function goToPage( pageNumber ){
        var adjustmentsForm = document.getElementById("adjustmentsForm");
      
        if( adjustmentsForm ){
                        
          adjustmentsForm.action = '<html:rewrite action="/adjustment/adjustment.do?operation=goToUIPage"/>&page=' + pageNumber;
          
          adjustmentsForm.submit();
        }
    }
    
    function forwardPage(){
        var adjustmentsForm = document.getElementById("adjustmentsForm");
      
        if( adjustmentsForm ){
                        
          adjustmentsForm.action = '<html:rewrite action="/adjustment/adjustment.do?operation=forwardUIPage"/>';
          
          adjustmentsForm.submit();
        }
    }
    
    function reversePage(){
        var adjustmentsForm = document.getElementById("adjustmentsForm");
      
        if( adjustmentsForm ){
                        
          adjustmentsForm.action = '<html:rewrite action="/adjustment/adjustment.do?operation=reverseUIPage"/>';
          
          adjustmentsForm.submit();
        }
    }
    
    //Paginator - END
    
    </script>
      
  </body>   
         
</html>

