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
    <title>Adjustments > Thank You</title>
    
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
  <body>
      <div id="mainContainer" style="padding: 10px; width: 850px;">
      
        <h1 style="padding-bottom: 10px;">
            Adjustments&nbsp;&gt;&nbsp;Thank You
        </h1>
        
         <div id="originalDetailsContainer" class="container">
       
           <fieldset style="padding-left: 10px;">
              <legend>Adjustment Processed Successfully</legend>
                <span style="font-weight: bold;">Thank You!</span><p>
            &nbsp;
          </p>
          Your adjustment has been processed successfully.
         
          <div style="padding-top: 5px; padding-bottom: 5px;">
            <hr/>
          </div>
          
          <div style="padding-bottom: 10px;">
                What would you like to do next?<p/>
                <a href='<html:rewrite page="/admin/orderHistory.do"/>'><html:image page="/resources/adjustment/images/hourglass_go.png" style="vertical-align: middle;" title="Go to Order History"/></a>&nbsp;<a href='<html:rewrite page="/admin/orderHistory.do"/>'>Go to Order History</a>&nbsp;&nbsp;|&nbsp;&nbsp;<a href='<html:rewrite page="/adjustment/adjustment.do?operation=displayAdjustmentsForUser"/>'><html:image page="/resources/adjustment/images/page_save.png" style="vertical-align: middle;" title="View Pending Adjustments"/></a>&nbsp;<a href='<html:rewrite page="/adjustment/adjustment.do?operation=displayAdjustmentsForUser"/>'>View Pending Adjustments</a>
          </div>
              </fieldset>
              
        </div>
              
  </body>
</html>