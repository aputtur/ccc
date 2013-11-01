<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ page errorPage="/jspError.do" %>

<bean:define id="chapter" name="rightsLinkQuickPriceActionForm" property="selectedChapter" />


  <div class="item-list-details" style="padding-left:115px">
	
	  	 <strong >Chapter title :</strong><bean:write name="chapter" property="mainTitle"/><br/>
		 <strong> Author(s): </strong><bean:write name="chapter" property="authorName"/><br/>
		 <logic:notEmpty name="chapter" property="idnoTypeCd" >
		    <strong><bean:write name="chapter" property="idnoTypeCd" />: </strong><bean:write name="chapter" property="idno"/><br/>
		 </logic:notEmpty>
	  

	  <div class="clearer"></div>
  </div>
