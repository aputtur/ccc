<!-- TODO: gcuevas 11/3/06: remove once integration with search is ready -->
<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<%@ page errorPage="/jspError.do" %>

<STYLE type="text/css">

@media print {
    #wrapper { border-bottom: solid 1px #B2B2B2; width:15cm; border-right: solid 1px #B2B2B2; }
    #menu { display:none; }
    #ecom-nav { display:none; }
    #titlebar { display:none; }
    #titlesearch { display:none; }
    #footer-content { display:none; }
    #bottomcorners { display:none; }
}

</STYLE>

<script src="<html:rewrite page="/resources/commerce/js/util.js"/>" type="text/javascript"></script>

<html:form action="/sampleTransactionStartPage.do" styleId="sampleTransactionStartPageForm">

    <table style="width:650px">
        <tr>
            <td style="width:90px">Go to:</td>
            <td style="width:560px">
                <html:select name="sampleTransactionStartPageForm" property="action">
                    <html:option value="addToCart">Add To Cart</html:option>
                    <html:option value="addToOrder">Add To Order</html:option>
                </html:select>
            </td>
        </tr>
        <tr>
            <td>Type of Use:</td>
            <td>
                <html:select name="sampleTransactionStartPageForm" property="typeOfUse">
                    <html:option value="trs">Photocopy</html:option>
                    <html:option value="dps">Digital</html:option>
                </html:select>
            </td>
        </tr>
        <tr>
            <td>Sample Value:</td>
            <td>
                <html:text name="sampleTransactionStartPageForm" property="sampleValue" />
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <input type="button" value="Create Regular Order" onclick="javascript:submitForm(document.getElementById('sampleTransactionStartPageForm'),'createRegularOrder')" />
                <input type="button" value="Create Special Order With Title" onclick="javascript:submitForm(document.getElementById('sampleTransactionStartPageForm'),'createSpecialOrderWithTitle')" />
                <input type="button" value="Create Special Order From Scratch" onclick="javascript:submitForm(document.getElementById('sampleTransactionStartPageForm'),'createSpecialOrderFromScratch')" />
            </td>
        </tr>
    </table>

</html:form>