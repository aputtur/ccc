<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/tld/struts-logic.tld" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/cc2-util.tld" %>
<%@ page errorPage="/jspError.do" %>

<tiles:useAttribute id="transactionItem" name="transactionItem" classname="com.copyright.ccc.business.data.TransactionItem"/>
<tiles:useAttribute id="rollover" name="rollover" classname="java.lang.String" />

<logic:equal name="transactionItem" property="APS" value="true">
    Photocopy for academic coursepacks, classroom handouts.&nbsp;<util:contextualHelp helpId="1" rollover="<%= Boolean.parseBoolean(rollover) %>">More...</util:contextualHelp>
</logic:equal>

<logic:equal name="transactionItem" property="photocopy" value="true">
    Photocopy for general business use, library reserves, ILL/document delivery.&nbsp;<util:contextualHelp helpId="2" rollover="<%= Boolean.parseBoolean(rollover) %>">More...</util:contextualHelp>
</logic:equal>

<logic:equal name="transactionItem" property="ECCS" value="true">
    Posting e-reserves, course management systems, e-coursepacks.&nbsp;<util:contextualHelp helpId="3" rollover="<%= Boolean.parseBoolean(rollover) %>">More...</util:contextualHelp>
</logic:equal>

<logic:equal name="transactionItem" property="digital" value="true">
    Use in e-mail, intranet/extranet/Internet postings.&nbsp;<util:contextualHelp helpId="4" rollover="<%= Boolean.parseBoolean(rollover) %>">More...</util:contextualHelp>
</logic:equal>

<logic:equal name="transactionItem" property="republication" value="true">
    Republish into a book, journal, newsletter.&nbsp;<util:contextualHelp helpId="5" rollover="<%= Boolean.parseBoolean(rollover) %>">More...</util:contextualHelp>
</logic:equal>