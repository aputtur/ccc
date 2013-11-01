<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ page errorPage="/jspError.do" %>

<bean:define id="article" name="rightsLinkQuickPriceActionForm" property="selectedArticle" />


  
  <div class="articleBiblo">
	  	 <div><strong >Article title: </strong>
	  	 <span>
	  	  <logic:notEmpty name="article" property="fullTitle">
		  <bean:write name="article" property="fullTitle"/>
		  </logic:notEmpty>
		  <logic:empty name="article" property="fullTitle">
		  &nbsp;
		  </logic:empty>
	  	 </span>
	  	 </div>
		 <div><strong> Author(s): </strong>
		 <span>
		 <logic:notEmpty name="article" property="authorName">
		  <bean:write name="article" property="authorName"/>
		  </logic:notEmpty>
		  <logic:empty name="article" property="authorName">
		  &nbsp;
		  </logic:empty>
		 </span></div>
		 <logic:notEmpty name="article" property="idnoTypeCode" >
		  <div>  <strong><bean:write name="article" property="idnoTypeCode" />: </strong>
		  <span>
		  <logic:notEmpty name="article" property="idno">
		      <bean:write name="article" property="idno"/>
		  </logic:notEmpty>
		  <logic:empty name="article" property="idno">
		  &nbsp;
		  </logic:empty>
		  </span></div>
		 </logic:notEmpty>
		 <div><strong> Date: </strong><span>
		  <logic:notEmpty name="article" property="runPubStartDate">
		  <bean:write name="article" property="runPubStartDate"/>
		  </logic:notEmpty>
		  <logic:empty name="article" property="runPubStartDate">
		  &nbsp;
		  </logic:empty>
		 </span>
		 </div>
		  <div><strong> Volume: </strong><span>
		  <logic:notEmpty name="article" property="itemVolume">
		  <bean:write name="article" property="itemVolume"/>
		  </logic:notEmpty>
		  <logic:empty name="article" property="itemVolume">
		  &nbsp;
		  </logic:empty>
		  </span>
		  
		  </div>
		  <div><strong>Issue: </strong><span>
		   <logic:notEmpty name="article" property="itemIssue">
		  <bean:write name="article" property="itemIssue"/>
		  </logic:notEmpty>
		  <logic:empty name="article" property="itemIssue">
		  &nbsp;
		  </logic:empty>
		  
		  </span></div>
	  <div class="clearer"></div>
  </div>
