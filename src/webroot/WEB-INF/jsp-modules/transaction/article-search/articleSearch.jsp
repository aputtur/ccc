<%@ taglib prefix="bean" uri="/WEB-INF/tld/struts-bean.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/tld/struts-logic.tld" %>
<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/cc2-util.tld" %>

<%@ page errorPage="/jspError.do" %>

<script type="text/javascript" language="javascript">
    //  Change our cursor when search is hit...

    function setCursor()
    {
        document.body.style.cursor='wait';
        return true;
    }
</script>

<%-- article search --%>

<jsp:useBean id="articleSearchActionForm" scope="session" class="com.copyright.ccc.web.forms.ArticleSearchActionForm"/>
<html:form action="/articleSearch.do?operation=articleSearch" styleId="form" onsubmit="setCursor()">
<html:hidden name="articleSearchActionForm" property="hostIDno"/>
<bean:define id="isBiactive" name="articleSearchActionForm" property="isBiactive"/>
 <div class="item-details">
  <h2><bean:write name="articleSearchActionForm" property="selectedPublication.mainTitle"/></h2>
  <tiles:insert page="/WEB-INF/jsp-modules/search/tiles/publication.jsp"/>
  <div class="clearer"></div>
 </div>
 <div class="horiz-rule"></div>
     <table style="width: 100%;">
        <tbody><tr>
        	<td style="width: 175px;">
                <h2 class="floatleft"><strong>Permission type selected: </strong></h2>
            </td>
            <td>
            	<h2 style="margin-top: 11px;">
                    <span class="normaltype">
            	<bean:write name="articleSearchActionForm" property="selectedCategory"/>
            	</span>
            </h2>
            </td>
        </tr>
        <tr>
        	<td style="width: 145px;">
                <h2 class="floatleft"><strong>Type of use selected: </strong></h2>
            </td>
            <td>
            	<h2 style="margin-top: 11px;">
                    <span class="normaltype">
            	<bean:write name="articleSearchActionForm" property="selectedTou" />
            	</span>
            </h2>
            </td>
        </tr>
    </tbody></table>

 
   <p class="clearer">
                <a href="search.do?operation=detail&item=<bean:write name='articleSearchActionForm' property='selectedPublication.wrkInst'/>&selectedTou=<bean:write name="articleSearchActionForm" property="selectedTou" filter="true"/>" class="icon-back">Select different permission</a>
     </p>
 
<p class="clearer">
  <strong> Search for the article you want to use from: </strong><bean:write name="articleSearchActionForm" property="selectedPublication.mainTitle"/>
  </p>
  
  <html:hidden name="articleSearchActionForm" property="selectedPublicationWrkInst" />
  <html:hidden name="articleSearchActionForm" property="selectedTou"/>
  
  <table style="width:720px;">
   	<col style="width:100px">
	<col style="width:355px">
	<col style="width:145px">
   <tr>
      <td>Article title: </td>
      <td><html:text name="articleSearchActionForm" property="articleTitle" style="width:400px"/></td> 
      <td><input type="image" align="left" alt="Search" id="999" src="/media/images/btn_search.gif" name=""></td>
      <td></td> 
   </tr>
   <tr>
      <td>Article author:</td>
      <td><html:text name="articleSearchActionForm" property="articleAuthor" style="width:400px"/></td>
      <td align="right">(last name, first name)</td>
      <td></td>
   </tr>

  </table>
  <table style="width:720px;">
   <col style="width:100px">
   <col style="width:55px">
   <col style="width:30px">
   <col style="width:20px">
   <col style="width:20px">
   <col style="width:55px">
   <col style="width:50px">
   <col style="width:20px">
   <col style="width:20px">
  <tr><td>DOI:</td>
  <td><html:text name="articleSearchActionForm" property="articleIDno" style="width:75px"/></td>
  <td>Volume:</td>
  <td><html:text name="articleSearchActionForm"  size="4" maxlength="4"  property="itemVolume" /></td>
  <td>Issue:</td>
  <td><html:text name="articleSearchActionForm" property="itemIssue" size="4" maxlength="4" /></td>
  
  <td>Start page:</td>
  <td><html:text name="articleSearchActionForm" property="itemStartPage" size="4" maxlength="4" /></td>
  <td><a href="noValidationArticleSearch.do?operation=clearParameters&item=<bean:write name='articleSearchActionForm' property='selectedPublicationWrkInst'/>" >
	          <img align="left" src="/media/images/btn_clear.gif" alt="Clear"  border="0" />
	       </a></td>
  </tr>
   <tr>
      <td>Publication date:  </td>
      <td>
      <html:select name="articleSearchActionForm" property="articleStartMonth">
         <html:option value="">Month</html:option>
         <html:option value="1">January</html:option>
         <html:option value="2">February</html:option>
         <html:option value="3">March</html:option>
         <html:option value="4">April</html:option>
         <html:option value="5">May</html:option>
         <html:option value="6">June</html:option>
         <html:option value="7">July</html:option>
         <html:option value="8">August</html:option>
         <html:option value="9">September</html:option>
         <html:option value="10">October</html:option>
         <html:option value="11">November</html:option>
         <html:option value="12">December</html:option>
      </html:select>
      </td>
      <td>Year:</td>
      <td><html:text disabled="${isBiactive}" name="articleSearchActionForm" property="articleStartYear"  size="4" maxlength="4"/></td>
      <td>To </td>
      <td>
      
      <html:select name="articleSearchActionForm" property="articleEndMonth">
         <html:option value="">Month</html:option>
         <html:option value="1">January</html:option>
         <html:option value="2">February</html:option>
         <html:option value="3">March</html:option>
         <html:option value="4">April</html:option>
         <html:option value="5">May</html:option>
         <html:option value="6">June</html:option>
         <html:option value="7">July</html:option>
         <html:option value="8">August</html:option>
         <html:option value="9">September</html:option>
         <html:option value="10">October</html:option>
         <html:option value="11">November</html:option>
         <html:option value="12">December</html:option>
      </html:select>      
      </td>
      <td>Year:</td>
      <td><html:text disabled="${isBiactive}" name="articleSearchActionForm" property="articleEndYear" size="4" maxlength="4"/></td>
      <td></td>
      <td></td>
   </tr>
</table>
</html:form>
<br/>

 <div style="border-width:1px;border-style:solid">
 <div class="search-result-options floatleft">
 <logic:notEmpty name="articleSearchActionForm" property="searchParameters">
  <logic:greaterThan name="articleSearchActionForm" property="count" value="1">
  <label style="padding-left:0px">Sort results by:</label>
  <html:form action="noValidationArticleSearch.do?operation=sort" styleId="form">
   <html:select name="articleSearchActionForm" property="sort" size="1"  onchange="submit()">
    <html:optionsCollection name="articleSearchActionForm" property="sortOptions" label="text" value="value"/>
   </html:select> 
  </html:form>
  </logic:greaterThan>

  <logic:greaterThan name="articleSearchActionForm" property="count" value="6">
  <label>Results per page:</label>
  <html:form action="noValidationArticleSearch.do?operation=perPage" styleId="form">
   <html:select name="articleSearchActionForm" property="pageSize" size="1" onchange="submit()">
    <html:optionsCollection name="articleSearchActionForm" property="pageOptions" label="text" value="value" />
   </html:select>
  </html:form>
    
  </logic:greaterThan>

 <br/><br/><h1 class="floatleft clearer">Results:  <span class="subtitle">
 <logic:lessThan name="articleSearchActionForm" property="resultCountPageSizeCompare" value="1">
    <logic:lessThan name="articleSearchActionForm" property="count" value="1">
        <bean:write name="articleSearchActionForm" property="count" />
    </logic:lessThan>
    <logic:greaterThan name="articleSearchActionForm" property="count" value="0">
 	   <bean:write name="articleSearchActionForm" property="lowItem" /> 
 	</logic:greaterThan>
 	 to <bean:write name="articleSearchActionForm" property="count" /> of <bean:write name="articleSearchActionForm" property="count" />
 </logic:lessThan>
 <logic:greaterEqual name="articleSearchActionForm" property="resultCountPageSizeCompare" value="1">
 	<bean:write name="articleSearchActionForm" property="lowItem" /> to <bean:write name="articleSearchActionForm" property="highItem" />
 	  of <bean:write name="articleSearchActionForm" property="count" />
 </logic:greaterEqual>
  
  <logic:greaterThan name="articleSearchActionForm" property="count" value="0"> for 
  <logic:notEmpty name="articleSearchActionForm" property="articleTitle">
  <strong>Title: </strong>
  	<bean:write name="articleSearchActionForm" property="articleTitle"/>
  </logic:notEmpty> 
  <logic:notEmpty name="articleSearchActionForm" property="articleAuthor">
    <b>Author: </b>
    <bean:write name="articleSearchActionForm" property="articleAuthor"/>
  </logic:notEmpty>
  <logic:notEmpty name="articleSearchActionForm" property="articleIDno">
    <b>IDNO: </b>
    <bean:write name="articleSearchActionForm" property="articleIDno"/>
  </logic:notEmpty>
  <logic:notEmpty name="articleSearchActionForm" property="dateRange">
    <b>Date Range: </b>
     <bean:write name="articleSearchActionForm" property="dateRange"/>
  </logic:notEmpty>
  </logic:greaterThan>
  </span></h1>
  
  
 </logic:notEmpty>
 </div>
 
   <div style="margin:7px 0;float:right">
   <logic:notEqual name="articleSearchActionForm" property="pageRangeTotal" value="1">
      <logic:equal name="articleSearchActionForm" property="isLess" value="yes">
         <a href="noValidationArticleSearch.do?operation=prev">Prev</a> 
      </logic:equal>
   <logic:iterate name="articleSearchActionForm" property="pageRange" id="pp" indexId="index">
    <% if (index.intValue() > 0) { %>
            ,
    <% } %>
    <logic:equal name="pp" value="<%= articleSearchActionForm.getPageNo() %>">
      <logic:greaterThan name="pp" value="0">
        <bean:write name="pp" />
      </logic:greaterThan>    
    </logic:equal>
    <logic:notEqual name="pp" value="<%= articleSearchActionForm.getPageNo() %>">
       <logic:greaterThan name="pp" value="0">
           <a href="noValidationArticleSearch.do?operation=toPage&pno=<bean:write name='pp' />"><bean:write name="pp" /></a>
       </logic:greaterThan> 
    </logic:notEqual>
   </logic:iterate>
      <logic:equal name="articleSearchActionForm" property="isMore" value="yes">
          <a href="noValidationArticleSearch.do?operation=next"> Next</a>
      </logic:equal>
  </logic:notEqual>

  </div>
  </div>
  
<logic:notEmpty name="articleSearchActionForm" property="articles">
<div class="clearer"></div>
<div style="border-width:0px;border-style:solid;"></div>
 
</logic:notEmpty>
<logic:iterate name="articleSearchActionForm" property="articles" id="article" indexId="index">
  <bean:define id="number"><%= index.intValue() + articleSearchActionForm.getLowItem() %>.</bean:define>
  <div class="search-result-basic">
  
  <table style="width:100%">
  <col width="20px"/><col /><col width="175px"/>
  <tr><td><h2><bean:write name="number" /></h2></td><td><h2><bean:write name="article" property="fullTitle"/></h2></td>
  <td style="width:175px;text-alignment:right;">
   <h2> <a href="noValidationArticleSearch.do?operation=continuePurchase&articleIndex=<%= index %>&item=<bean:write name='articleSearchActionForm' property='selectedPublicationWrkInst'/>">
	  <img src="/media/images/btn_continue.gif" alt="Continue" class="floatright"/>
      </a>
      </h2>
  </td></tr>
    </table>

  <div class="item-list-details" style="padding-left: 35px;">
	  <p>
		 <strong> Author(s): </strong><bean:write name="article" property="authorName"/><br/>
		 <logic:notEmpty name="article" property="idnoTypeCode" >
		    <strong>
		    <bean:write name="article" property="idnoTypeCode" />: </strong><bean:write name="article" property="idno"/><br/>
		 </logic:notEmpty>
		 <strong> Date: </strong><bean:write name="article" property="runPubStartDate"/><br/>
		 <strong> Volume: </strong><bean:write name="article" property="itemVolume"/><br/>
		 <strong> Issue: </strong><bean:write name="article" property="itemIssue"/>  
	  </p>
	  <p>
		  <logic:notEmpty name="article" property="itemPageRange">
		      <strong> Page Range: </strong><bean:write name="article" property="itemPageRange"/><br/>
		  </logic:notEmpty>   
		  <logic:empty name="article" property="itemPageRange">  
		     <logic:notEmpty name="article" property="itemStartPage">
		        <strong> Start Page: </strong><bean:write name="article" property="itemStartPage"/><br/>
		     </logic:notEmpty>   
		     <logic:notEmpty name="article" property="itemEndPage">
		        <strong> End Page: </strong><bean:write name="article" property="itemEndPage"/><br/>
		     </logic:notEmpty>   	  
		  </logic:empty> 
		  <logic:notEmpty name="article" property="country">
		      <strong> Country: </strong><bean:write name="article" property="country"/><br/>
		  </logic:notEmpty>  
		  <logic:notEmpty name="article" property="country">
		     <strong>       Language: </strong><bean:write name="article" property="language"/><br/>
		  </logic:notEmpty>
	  </p>
	  <div class="clearer"></div>
  </div>
</div>
</logic:iterate>

<div style="float:right">
 <logic:notEqual name="articleSearchActionForm" property="pageRangeTotal" value="1">
    <logic:equal name="articleSearchActionForm" property="isLess" value="yes">
       <a href="noValidationArticleSearch.do?operation=prev">Prev</a> 
    </logic:equal>
 <logic:iterate name="articleSearchActionForm" property="pageRange" id="pp" indexId="index">
  <% if (index.intValue() > 0) { %>
          ,
  <% } %>
  <logic:equal name="pp" value="<%= articleSearchActionForm.getPageNo() %>">
    <logic:greaterThan name="pp" value="0">
        <bean:write name="pp" />
    </logic:greaterThan>
  </logic:equal>
  <logic:notEqual name="pp" value="<%= articleSearchActionForm.getPageNo() %>">
       <logic:greaterThan name="pp" value="0">
           <a href="noValidationArticleSearch.do?operation=toPage&pno=<bean:write name='pp' />"><bean:write name="pp" /></a>
       </logic:greaterThan> 
  </logic:notEqual>
 </logic:iterate>
  <logic:equal name="articleSearchActionForm" property="isMore" value="yes">
     <a href="noValidationArticleSearch.do?operation=next"> Next</a>
  </logic:equal>
</logic:notEqual>

</div>
