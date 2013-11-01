<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ page errorPage="/jspError.do" %>

<bean:define id="item" name="searchForm" property="selectedItem" />
<div class="biblo_left" style="float:left;width:300px;">
 <div><strong><bean:write name="item" property="idnoLabel"/>: </strong><span><bean:write name="item" property="mainIDNo"/></span></div>
 <logic:notEqual name="item" property="publicationYearRange" value="Through present">
   <logic:notEqual name="item" property="publicationYearRange" value="-">
     <div><strong>Publication year(s): </strong><span><bean:write name="item" property="publicationYearRange"/></span></div>
   </logic:notEqual>
 </logic:notEqual>
 <logic:notEmpty name="item" property="mainAuthor">
   <div><strong>Author/Editor: </strong>
   <span><bean:write name="item" property="mainAuthor"/></span></div>
 </logic:notEmpty>
 <logic:empty name="item" property="mainAuthor">
  <logic:notEmpty name="item" property="mainEditor">
   <div><strong>Author/Editor: </strong>
   <span><bean:write name="item" property="mainEditor"/></span></div>
   
  </logic:notEmpty>
 </logic:empty>
 <logic:notEmpty name="item" property="publicationType">
    <div><strong>Publication type: </strong><span><bean:write name="item" property="publicationType"/></span></div>
 </logic:notEmpty>
 <logic:notEmpty name="item" property="mainPublisher">
    <div><strong>Publisher: </strong><span><bean:write name="item" property="mainPublisher"/></span></div>
 </logic:notEmpty>
</div>
<div class="biblo_right" style="float:left;margin-left: 8px;">

 <logic:notEmpty name="item" property="volume">
   <div><strong>Volume: </strong>
  <span><bean:write name="item" property="volume"/></span></div>
 </logic:notEmpty>
  
 <logic:notEmpty name="item" property="edition">
  <div> <strong>Edition: </strong>
  <span><bean:write name="item" property="edition"/></span></div>
 </logic:notEmpty>
 
 <logic:notEmpty name="item" property="pages">
   <div><strong>Pagination: </strong><span><bean:write name="item" property="pages"/></span></div>
 </logic:notEmpty>
 
 <logic:notEmpty name="item" property="series">
   <div><strong>Series: </strong><span><bean:write name="item" property="series"/>
  <logic:notEmpty name="item" property="seriesNumber">
     ; <bean:write name="item" property="seriesNumber"/>
  </logic:notEmpty>
  </span></div>
 </logic:notEmpty>
 <logic:notEmpty name="item" property="language">
   <div><strong>Language: </strong><span><bean:write name="item" property="language"/></span></div>
 </logic:notEmpty>
 <logic:notEmpty name="item" property="country">
   <div><strong>Country of publication: </strong><span><bean:write name="item" property="country"/></span></div>
 </logic:notEmpty>
</div>
<div class="clearer"></div>

<logic:notEmpty name="item" property="rightsholderNames">
<div class="biblo_left" style="float:left;width:600px">
     <div><strong>Rightsholder: </strong><span style="width:420px;"><bean:write name="item" property="rightsholderNames"/></span></div>
</div>
</logic:notEmpty>
<div class="clearer"></div>
<br/>
