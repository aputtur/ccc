<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page errorPage="/jspError.do" %>

<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/struts-nested.tld" prefix="nested" %>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
    <title>Adjustments > Error</title>
    
    <link href="<html:rewrite page="/resources/commerce/css/default.css"/>" rel="stylesheet" type="text/css" />
    <link href="<html:rewrite page="/resources/commerce/css/ccc-new.css"/>" rel="stylesheet" type="text/css" />
    <link href="<html:rewrite page="/resources/adjustment/css/adjustment.css"/>" rel="stylesheet" type="text/css" />

    <script src="<html:rewrite page="/resources/adjustment/script/adjustments.js"/>"></script>
    
    <script type="text/javascript">
         function initPage(){
          organizePageLayout();
         }
        
        if( typeof addOnLoadEvent == 'function' && typeof initPage == 'function' ) addOnLoadEvent( initPage );
    </script>

  </head>
  
  <style type="text/css">
    div#validationErrorsSection{ display: none;}
  </style> 
  
  <body>
      <div id="mainContainer" style="padding: 10px; width: 850px;">
      
        <h1 style="padding-bottom: 10px;">
            Adjustments&nbsp;&gt;&nbsp;Error
        </h1>
        
         <div id="linksContainer" class="container">
           <a href='<html:rewrite page="/admin/orderHistory.do"/>'><html:image page="/resources/adjustment/images/hourglass_go.png" style="vertical-align: middle;" title="Go to Order History"/></a>&nbsp;<a href='<html:rewrite page="/admin/orderHistory.do"/>'>Go to Order History</a>&nbsp;&nbsp;|&nbsp;&nbsp;<a href='<html:rewrite page="/adjustment/adjustment.do?operation=displayAdjustmentsForUser"/>'><html:image page="/resources/adjustment/images/page_save.png" style="vertical-align: middle;" title="View Pending Adjustments"/></a>&nbsp;<a href='<html:rewrite page="/adjustment/adjustment.do?operation=displayAdjustmentsForUser"/>'>View Pending Adjustments</a>
         </div>
        
         <div id="errorsContainer" class="container">
          
             <logic:messagesPresent >
                <span style="color: red;">
                  <html:errors bundle="adjustment" />
                </span>
             </logic:messagesPresent>
          
        </div>
        
        <script type="text/javascript">
          
          var closeErrorsLink = document.getElementById("closeErrorsLink");
          
          if(closeErrorsLink) closeErrorsLink.style.display = "none";
          
        </script>
    
  </body>

</html>