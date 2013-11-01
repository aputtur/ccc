<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%@ page import="java.util.*, java.lang.*"%>

<table width="100%" style="margin-bottom: 0px;">
	<tr style="background-color: #6F97BB;">
		<td valign="middle" style="width: 15px; text-align: center;">
			<a href='#'
				id='SEL<s:property value="item.iD"/>'
				onclick="toggleDetail(this, '<s:property value="confNumber"/>','<s:property value="bundleId"/>', '<s:property value="item.iD"/>');return false;">
				<img alt="Collapse" border="0"
					src="<s:url value='/resources/ordermgmt/images/collapse.gif'/>" /></a>
		</td>
		<td valign="middle" style="height: 26px; width: 907px;">

			<div id="detailSectionHeader"
				style="padding-top: 4px; font-size: 14px; color: white; width: 45%; float: left;">
				Detail&nbsp;#&nbsp;
			<s:property value="item.iD" />
				&nbsp;&nbsp;&nbsp;Detail&nbsp;Date:&nbsp;
				<s:property value="detailDateFMT"/>
			</div>
			<jsp:include
				page="/WEB-INF/jsp-modules-s2/ordermgmt/orderhistory/include/viewDetailTopMenu.jsp" />
		</td>
	</tr>
</table>

<div style="background-color: #FFFFFF;" style="display: inline;"
	id="showing_detail_<s:property value="bundleId"/>_order_<s:property value="confNumber"/>_detail_<s:property value="item.iD"/>">

<table width="100%"
	style="border-color: #6F97BB; border-style: solid; border-width: 2px;">
	<jsp:include page="/WEB-INF/jsp-modules-s2/ordermgmt/orderhistory/include/viewOneDetailLong.jsp" />
</table>

</div>
<div style="background-color: #FFFFFF; display: none;"
	id="editing_detail_<s:property value="bundleId"/>_order_<s:property value="confNumber"/>_detail_<s:property value="item.iD"/>">
<s:form theme="simple" id="savedetailform_%{item.ID}" action="viewOrderHistory!saveDetails" namespace="/om/history" >	
   	<s:if test="hasLastEdit">
		<input type="hidden" id='changeddetail<s:property value="item.ID"/>' value="1"/>
   	</s:if>
	<s:else>
		<input type="hidden" id='changeddetail<s:property value="item.ID"/>' value="0"/>
   	</s:else>
	<table width="100%"
		style="border-color: #6F97BB; border-style: solid; border-width: 2px;">
		<jsp:include page="/WEB-INF/jsp-modules-s2/ordermgmt/orderhistory/include/editOneDetailLong.jsp"></jsp:include>
	</table>
</s:form>
</div>
