<%@ page language="java"%>
<%@ page errorPage="/jspError.do" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/cc2-util.tld" %>
<%@ page import="java.util.List" %>
<%@ page import="com.copyright.ccc.web.util.PermissionCategoryDisplay" %>
<%@ page import="com.copyright.ccc.web.util.PermSummaryTouDisplay" %>

<jsp:include page="/WEB-INF/jsp-modules/search/tiles/searchTouDetailPage.jsp" />

<%  if (!pageContext.getRequest().isSecure()) { %>
     
    <script type="text/javascript">

	function firePermissionsPage() {
	    // select a tou radio pick
	    var vSEL = 0;
        $("input[id^='pickme_radio_']").each(function(i){
	          var vid = $(this).attr('id');
	          var vCN = '#'+vid.replace('pickme_','');
	          if ( vSEL==0 ) {
	          	$(vCN).click();
	          	vSEL += 1;
	          }
        });
        if ( vSEL > 0 ) {
 			var idx = '#pr_'+'<bean:write name="searchForm" property="permissionDirectProduct"/>';
			$(idx).click();
		}
	}
	
    </script>

    <script type="text/javascript">
    	
    	$(document).ready( function() {
        
        showBookSearchLinkIfAvailable();
        firePermissionsPage();
	
	});
            
    </script>

<% } %>