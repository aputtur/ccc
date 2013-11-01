<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/struts-nested.tld" prefix="nested" %>

<bean:define id="numberOfUIPages" name="adjustmentForm" property="paginator.numberOfPages" type="Integer" />
<bean:define id="currentUIPageNumber" name="adjustmentForm" property="paginator.currentPageNumber" type="Integer" />
  
<div id="paginatorControls" style="position: relative; float: right; font-size: 8pt; padding-right:10px;">
  
  <% if( numberOfUIPages.intValue() > 1 ){ %>
  
    <% boolean notInFirstUIPage = currentUIPageNumber.intValue() > 1;
      if( notInFirstUIPage ){%>
       <a title="Go to first page" href='javascript:goToPage(1)'>&lt;&lt;</a>&nbsp;
        <a title="Go to previous page" href='javascript:reversePage()'>&lt;</a>&nbsp;
    <%}%>
  
    <% for( int pgNum = 1; pgNum <=  numberOfUIPages.intValue() ; pgNum++ ){ %>

      <%if( pgNum == currentUIPageNumber.intValue() ){%>
        <span title="You are viewing page <%=pgNum%>" style="font-weight: bold; color: #990000; border: solid 1px #c0c0c0; padding: 2px;"><%=pgNum%></span>&nbsp;
      <%}else{%>
        <a title="Go to page <%=pgNum%>" href='javascript:goToPage(<%=pgNum%>)'><%=pgNum%></a>&nbsp;
      <%}%>
      
    <%}%>
    
     <% boolean notInLastUIPage = currentUIPageNumber.intValue() < numberOfUIPages.intValue();
      if( notInLastUIPage ){%>
        <a title="Go to next page" href='javascript:forwardPage()'>&gt;</a>&nbsp;
        <a title="Go to last page" href='javascript:goToPage(<%=numberOfUIPages.intValue()%>)'>&gt;&gt;</a>&nbsp;
     <%}%>
   
      &nbsp;|&nbsp;
   <%}%>
   
   <a href="javascript:refresh()"><html:image page="/resources/adjustment/images/page_refresh.png" style="vertical-align: middle;" title="Refresh Totals"/></a>&nbsp;<a href="javascript:refresh()">Refresh Totals</a>

</div>