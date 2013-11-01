<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/cc2-util.tld" %>
<%@ page errorPage="/jspError.do" %>

<link href="<html:rewrite page="/resources/commerce/css/contextualHelp.css"/>" rel="stylesheet" type="text/css" />
<script src="<html:rewrite page="/resources/commerce/js/contextualHelp.js"/>" type="text/javascript"></script>

<h2>The following fields are optional:</h2>
<p>Permission Type: <util:contextualHelp helpId="20" rollover="false" styleClass="smalltype">What are these?</util:contextualHelp></p>
<table border="0">
  <tr>
    <td><input type="checkbox" name="checkbox" value="checkbox" /></td>
    <td>All permission types</td>
  </tr>
  <tr>
    <td><input type="checkbox" name="checkbox" value="checkbox" /></td>
    <td>Photocopy for academic coursepacks, classroom handouts. <util:contextualHelp helpId="1" rollover="true">More&hellip;</util:contextualHelp></td>
  </tr>
  <tr>
    <td><input type="checkbox" name="checkbox" value="checkbox" /></td>
    <td>Photocopy for general business use, library reserves, ILL/document delivery. <util:contextualHelp helpId="2" rollover="true">More&hellip;</util:contextualHelp></td>
  </tr>
  <tr>
    <td><input type="checkbox" name="checkbox" value="checkbox" /></td>
    <td>Posting e-reserves, course management systems, e-coursepacks. <util:contextualHelp helpId="3" rollover="true">More&hellip;</util:contextualHelp></td>
  </tr>
  <tr>
    <td><input type="checkbox" name="checkbox" value="checkbox" /></td>
    <td>Use in e-mail, intranet/extranet/Internet postings. <util:contextualHelp helpId="4" rollover="true">More&hellip;</util:contextualHelp></td>
  </tr>
  <tr>
    <td><input type="checkbox" name="checkbox" value="checkbox" /></td>
    <td>Republish into a book, journal, newsletter. <util:contextualHelp helpId="5" rollover="true">More&hellip;</util:contextualHelp> </td>
  </tr>
</table>
<br />