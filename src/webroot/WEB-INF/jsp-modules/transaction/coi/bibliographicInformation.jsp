<%@ taglib prefix="bean" uri="/WEB-INF/tld/struts-bean.tld" %>
<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/tld/struts-logic.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/cc2-util.tld" %>
<%@ page errorPage="/jspError.do" %>
<%@ page import="com.copyright.ccc.web.util.PubDateDisplayUtil" %>
<%@ page import="com.copyright.data.account.Organization" %>
<%@ page import="com.copyright.data.account.OrganizationNotFoundException" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>

<script src="<html:rewrite page="/resources/commerce/js/contextualHelp.js"/>" type="text/javascript"></script>

<tiles:useAttribute id="transactionItem" name="transactionItem" classname="com.copyright.ccc.business.data.TransactionItem"/>
<tiles:useAttribute id="couldShowFees" name="couldShowFees" ignore="true"/>
<tiles:useAttribute id="displayPublicationYearRange" name="displayPublicationYearRange" ignore="true" />

    <table style="float: right;">
    <tr>
        <td>
            <a href="http://chat.copyright.com/webchat/" target="_blank" class="icon-right floatleft"><strong>Live Help</strong></a>
        </td>
    </tr>
    <tr>
        <td>
            <a href="/media/pdfs/online-orders-tutorial.pdf" target="_blank" Class="icon-right"><strong>Tutorial</strong></a>
        </td>
    </tr>
    </table>
   
<div class="item-details" >

    <bean:define id="middleStyle">biblo_right</bean:define>
       <logic:equal name="transactionItem" property="rightPermissionType" value="G"> 
      <logic:equal name="transactionItem" property="specialOrder" value="false">
        <logic:equal name="couldShowFees" value="true">
            <logic:equal name="transactionItem" property="academic" value="true">
            <bean:define id="middleStyle">biblo_middle</bean:define>
            </logic:equal>
            </logic:equal>
            </logic:equal>
            </logic:equal>
    
    <logic:equal name="transactionItem" property="photocopy" value="true">
    <bean:define id="middleStyle">biblo_middle</bean:define>
    </logic:equal>

    <h2><bean:write name="transactionItem" property="publicationTitle"/></h2>
    <div style="padding-bottom: 10px;"></div>
    
    <div class="biblo_left" style="float:left;width:300px;">
    <strong><bean:write name="transactionItem" property="idnoLabel"/>:</strong> <span><bean:write name="transactionItem" property="standardNumber"/></span>
    <bean:define id="dateRange"><%= PubDateDisplayUtil.computeYearRangeDisplay( transactionItem.getPublicationStartDate(), transactionItem.getPublicationEndDate() )%></bean:define>
    <!-- Date range is valid and can be displayed. -->
    <logic:notEqual name="dateRange" value="Through present">
        <logic:notEqual name="dateRange" value="-">
            <logic:equal name="displayPublicationYearRange" value="false">
                <div id="publicationYearRange" style="display:none"><strong>Publication year(s):</strong> <span id="publicationYearRangeValue"><bean:write name="dateRange"/></span></div>
            </logic:equal>
            <logic:notEqual name="displayPublicationYearRange" value="false">        
                <div id="publicationYearRange"><strong>Publication year(s):</strong> <span id="publicationYearRangeValue"><bean:write name="dateRange"/></span></div>
            </logic:notEqual>
        </logic:notEqual>
    </logic:notEqual>
    <!-- Date range should not be displayed in this case. -->
    <logic:equal name="dateRange" value="Through present">
        <div id="publicationYearRange" style="display:none"><strong>Publication year(s):</strong> <span id="publicationYearRangeValue"><bean:write name="dateRange"/></span></div>
    </logic:equal>
    <logic:equal name="dateRange" value="-">
        <div id="publicationYearRange" style="display:none"><strong>Publication year(s):</strong> <span id="publicationYearRangeValue"><bean:write name="dateRange"/></span></div>
    </logic:equal>
    <logic:notEmpty name="transactionItem" property="author">
      <div><strong>Author/Editor: </strong>
      <span><bean:write name="transactionItem" property="author"/></span>
      </div>
    </logic:notEmpty>
    <logic:empty name="transactionItem" property="author">
     <logic:notEmpty name="transactionItem" property="editor">
      <div><strong>Author/Editor: </strong>
      <span><bean:write name="transactionItem" property="editor"/></span>
      </div>
     </logic:notEmpty>
    </logic:empty>
    <logic:notEmpty name="transactionItem" property="publicationType">
        <div><strong>Publication type: </strong><span><bean:write name="transactionItem" property="publicationType"/></span></div>
    </logic:notEmpty>
    <logic:notEmpty name="transactionItem" property="publisher">
        <div><strong>Publisher:</strong><span> <bean:write name="transactionItem" property="publisher"/></span></div>
    </logic:notEmpty>
    </div>
    
    <div class="<%=middleStyle%>"  style="float:left;">
     <logic:notEmpty name="transactionItem" property="volume">
      <div><strong>Volume: </strong>
      <span><bean:write name="transactionItem" property="volume"/></span>
      </div>
     </logic:notEmpty>
      
     <logic:notEmpty name="transactionItem" property="edition">
      <div><strong>Edition: </strong>
      <span><bean:write name="transactionItem" property="edition"/></span>
      </div>
     </logic:notEmpty>
     
     <logic:notEmpty name="transactionItem" property="pages">
      <div><strong>Pagination: </strong><span><bean:write name="transactionItem" property="pages"/></span></div>
     </logic:notEmpty>
     
     <logic:notEmpty name="transactionItem" property="series">
      <div><strong>Series: </strong><span><bean:write name="transactionItem" property="series"/>
      <logic:notEmpty name="transactionItem" property="seriesNumber">
         ; <bean:write name="transactionItem" property="seriesNumber"/>
      </logic:notEmpty>
      </span></div>
     </logic:notEmpty>
     <logic:notEmpty name="transactionItem" property="language">
      <div><strong>Language: </strong><span><bean:write name="transactionItem" property="language"/></span></div>
     </logic:notEmpty>
     <logic:notEmpty name="transactionItem" property="country">
        <div><strong>Country of publication: </strong><span><bean:write name="transactionItem" property="country"/></span></div>
     </logic:notEmpty>
    </div>
    

    
    
    
   <logic:equal name="transactionItem" property="rightPermissionType" value="G"> 
      <logic:equal name="transactionItem" property="specialOrder" value="false">
        <logic:equal name="couldShowFees" value="true">
            <logic:equal name="transactionItem" property="academic" value="true">
                <div  class="biblo_price" id="biblio_price" style="float: left;">
                <logic:notEmpty name="transactionItem" property="perPageFeeMoneyFormat">
                    <logic:notEqual name="transactionItem" property="perPageFeeMoneyFormat" value="$ 0.00">
                        <div><strong>Per Page Fee:</strong><span><bean:write name="transactionItem" property="perPageFeeMoneyFormat" /></span> </div>
                    </logic:notEqual>
                </logic:notEmpty>
                <logic:notEmpty name="transactionItem" property="baseFeeMoneyFormat">
                    <logic:notEqual name="transactionItem" property="baseFeeMoneyFormat" value="$ 0.00">
                        <div><strong>Per Copy Fee:</strong> <span><bean:write name="transactionItem" property="baseFeeMoneyFormat" /></span></div>
                    </logic:notEqual>
                </logic:notEmpty>
                <logic:notEmpty name="transactionItem" property="flatFeeMoneyFormat">
                    <logic:notEqual name="transactionItem" property="flatFeeMoneyFormat" value="$ 0.00">
                        <div><strong>Flat Fee:</strong> <span><bean:write name="transactionItem" property="flatFeeMoneyFormat" /></span></div>
                    </logic:notEqual>
                </logic:notEmpty>
                <logic:equal name="transactionItem" property="hasVolPriceTiers" value="Y">
                    <div><strong style="font-weight: normal;width:125px; ">Volume discount may apply</strong></div><br/>
                    <div><util:contextualHelp helpId="31" rollover="true" styleId="0">
                    Click here for details
                    </util:contextualHelp></div>
                </logic:equal>
                </div>
                
                <div  class="biblo_price" id="oop_price" style="float: left;display: none;">
                	<logic:notEqual name="transactionItem" property="entireBookFeeMoneyFormat" value="$ 0.00">
                		<div><strong>Entire Book Fee:</strong><span><bean:write name="transactionItem" property="entireBookFeeMoneyFormat" /></span></div>
                	</logic:notEqual>
                </div>
                
            </logic:equal>
            <logic:equal name="transactionItem" property="photocopy" value="true">
                 <div  class="biblo_price"  style="float: left; ">
                <logic:notEmpty name="transactionItem" property="perPageFeeMoneyFormat">
                    <logic:notEqual name="transactionItem" property="perPageFeeMoneyFormat" value="$ 0.00">
                        <div><strong>Per Page Fee:</strong><span> <bean:write name="transactionItem" property="perPageFeeMoneyFormat" /></span></div>
                    </logic:notEqual>
                </logic:notEmpty>
                <logic:notEmpty name="transactionItem" property="baseFeeMoneyFormat">
                    <logic:notEqual name="transactionItem" property="baseFeeMoneyFormat" value="$ 0.00">
                        <div><strong>Per Copy Fee:</strong><span> <bean:write name="transactionItem" property="baseFeeMoneyFormat" /></span></div>
                    </logic:notEqual>
                </logic:notEmpty>
                <logic:equal name="transactionItem" property="hasVolPriceTiers" value="Y">
                    <div><strong style="font-weight: normal;width:125px; " >Volume discount may apply</strong></div><br/>
                    <div><util:contextualHelp helpId="31" rollover="true" styleId="0">
                    Click here for details
                    </util:contextualHelp></div>
                </logic:equal>
                </div>
            </logic:equal>
        </logic:equal>
    </logic:equal>
    </logic:equal>

    
<logic:notEmpty name="transactionItem" property="rightsholder">
<div class="biblo_left" style="float:left;width:600px">
     <div><strong>Rightsholder: </strong><span style="width:420px;"><bean:write name="transactionItem" property="rightsholder"/></span></div>
</div>
</logic:notEmpty>
<div class="clearer"></div>
<br/>
    
</div>
