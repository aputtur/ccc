<%@ page language="java" pageEncoding="utf8" contentType="text/html; charset=UTF8"%>
<%@ page errorPage="/jspError.do" %>

<%@ page import="java.io.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.lang.Boolean" %>
<%@ page import="com.copyright.ccc.business.security.UserContextService" %>
<%@ page import="com.copyright.ccc.business.security.CCPrivilegeCode" %>
<%@ page import="com.copyright.ccc.config.CC2Configuration" %>
<%@ page import="com.copyright.ccc.web.CC2RequestProcessor" %>
<%@ page import="com.copyright.ccc.business.services.cart.CartServices" %>

<%@ taglib uri="/WEB-INF/tld/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/cc2-util.tld"     prefix="util" %>
<%@ taglib uri="/WEB-INF/tld/cc2-security.tld" prefix="security" %>
<%@ taglib prefix="static" uri="/WEB-INF/tld/cc2-static.tld" %>
<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>
<%@ page import="com.copyright.ccc.business.security.CCUserContext" %>

<!--HEADER REGION-->
<script src="<html:rewrite page="/resources/commerce/js/util.js"/>" type="text/javascript"></script>

    <%
    String logoutLink = "/ccc/do/logout";
    %>
    <bean:cookie id="firstName" name="firstNameCookie" value="Undefined"/>

    <security:ifUserHasPrivilege code="any">
    </security:ifUserHasPrivilege>
    <util:else>
        <security:ifUserEmulating>
            <tiles:insert page="/WEB-INF/jsp-modules/admin/adminMenu.jsp"/>
        </security:ifUserEmulating>
    </util:else>

    <bean:define id="help_url"><%= CC2Configuration.getInstance().getHelpURL() %></bean:define>

    <div id="header" class="region">
        <div id="logo-bar" class="sub-region">
            <div id="logo">
                <html:img alt="Copyright Clearance Center" src="/media/images/home-headerlogo.gif" />
            </div>

            <div id="ecom-nav">
                <div id="welcome-wrap">
                    <security:ifUserAnonymous not="true">
                    <span id="welcome">Welcome,
                        <% CCUserContext ccUserContext = UserContextService.getContextFromSession();
                         String userFirstName = ccUserContext == null || ccUserContext.getActiveUser() == null ? "" : ccUserContext.getActiveUser().getFirstName(); %>
                        <%= userFirstName %>
                        <html:link style="font-size:11px; font-weight:normal;" page="/logout.do?operation=removeCookies">Not you?</html:link>
                    </span>
                    </security:ifUserAnonymous>
                    <util:else>
                    <span id="welcome">Welcome </span>
                    </util:else>
                </div>

                <div id="dynaNavOptions">
                    <security:ifUserAnonymous>
                        <html:link action="/loginBounce.do">Log in</html:link>
                    </security:ifUserAnonymous>
                    <util:else>
                        <security:ifUserAuthenticated>
                            <html:link page="/logout.do?operation=logoutAuthorizedUser">Log out</html:link>
                        </security:ifUserAuthenticated>
                        <util:else>
                            <html:link action="/loginBounce.do">Log in</html:link>
                        </util:else>
                    </util:else>
                    <span id="cart_manageaccount_help_links">
                        <security:ifAdminResourceRequested></security:ifAdminResourceRequested>
                        <util:else>
                              &nbsp;|
                              <html:link action="/cart.do" styleClass="icon-cart">Cart (<%=CartServices.getNumberOfItemsInCart()%>)</html:link>&nbsp;|
                               <html:link action="/manageAccount.do">Manage Account</html:link>&nbsp;|
                              <a href="http://guest.cvent.com/d/bdqgxd">Feedback</a>&nbsp;|
                              <a href="<bean:write name="help_url" />">Help</a>&nbsp;
                        </util:else>
                    </span>
                </div>
            </div>
        </div>
        <security:ifAdminResourceRequested></security:ifAdminResourceRequested>
        <util:else>
        <div id="navigation" class="sub-region">
            <ul id="main-menu">


                <li id="getpermission" class="parent-link"><a href="<html:rewrite page="/viewPage.do?pageCode=gp1" />"><img alt="Get Permission" id="getpermission" src="/media/images/nav_get-permission.jpg"/></a>
                </li>

                <li id="license" class="parent-link"><a target="_blank" href="<%= CC2Configuration.getInstance().getGPOURL() %>"><img alt="License Your Content" id="license" src="/media/images/nav_license-content.jpg"/></a>
                </li>

                <li id="products" class="parent-link"><a href="<html:rewrite page="/viewPage.do?pageCode=au1" />"><img alt="Products and Solutions" id="products" src="/media/images/nav_products_solutions.jpg"/></a>
                    <ul class="child-menu">
                        <li class="child-link"><a href="<html:rewrite page="/content/cc3/en/toolbar/productsAndSolutions/annualLicenseAcademic.html" />">Annual License &#151; Academic</a></li>
                        <li class="child-link"><a href="<html:rewrite page="/content/cc3/en/toolbar/productsAndSolutions/annualLicenseBusiness.html" />">Annual License &#151; Business</a></li>
                        <li class="child-link"><a href="<html:rewrite page="/content/cc3/en/toolbar/productsAndSolutions/rightslink.html" />">Rightslink</a></li>
                        <li class="child-link"><a href="<html:rewrite page="/viewPage.do?pageCode=bu11" />">Rightsphere</a></li>
                        <li class="child-link"><a target="_blank" href="<%= CC2Configuration.getInstance().getGPOURL() %>">Rights Central for <em>rightsholders</em></a></li>
                        <li class="child-link"><a href="<html:rewrite page="/readyimages/" />">ReadyImages</a></li>
                    </ul>
                </li>

                <li id="partners" class="parent-link"><a href="<html:rewrite page="/viewPage.do?pageCode=ci1-n" />"><img alt="Partners" id="partners" src="/media/images/nav_partners.jpg"/></a>
                    <ul class="child-menu">
                        <li class="child-link"><a href="<html:rewrite page="/viewPage.do?pageCode=ci1-n" />">Partner Programs</a></li>
                        <li class="child-link"><a href="<html:rewrite page="/viewPage.do?pageCode=ci12" />">Web Services</a></li>
                        <li class="child-link"><a href="<html:rewrite page="/viewPage.do?pageCode=ci5-n" />">Partner Directory</a></li>
                    </ul>
                </li>

                <li id="partners" class="parent-link"><a href="<html:rewrite page="/viewPage.do?pageCode=pu3-n" />"><img alt="Education" id="education" src="/media/images/nav_education.jpg"/></a>
                    <ul class="child-menu">
                        <li class="child-link"><a href="<html:rewrite page="/viewPage.do?pageCode=pu3-n" />">Overview</a></li>
                        <li class="child-link"><a href="<html:rewrite page="/viewPage.do?pageCode=ed2" />">Programming</a></li>
                        <li class="child-link"><a href="<html:rewrite page="/viewPage.do?pageCode=ed3" />">Resources</a></li>
                        <li class="child-link"><a target="_blank" href="http://www.beyondthebookcast.com">Beyond The Book</a></li>
                        <li class="child-link"><a href="<html:rewrite page="/viewPage.do?pageCode=cr2-n" />">Tools & Guidelines</a></li>
                        <li class="child-link"><a href="<html:rewrite page="/viewPage.do?pageCode=ed4" />">News</a></li>
                    </ul>
                </li>

                <li id="copyright-central" class="parent-link"><a href="<html:rewrite page="/content/cc3/en/toolbar/aboutUs.html" />"><img alt="About CCC" id="aboutccc" src="/media/images/nav_about-ccc.jpg"/></a>
                    <ul class="child-menu">
                        <li class="child-link"><a href="<html:rewrite page="/content/cc3/en/toolbar/aboutUs/managementTeam.html" />">Management Team</a></li>
                        <li class="child-link"><a href="<html:rewrite page="/content/cc3/en/toolbar/aboutUs/boardOfDirectors.html" />">Board of Directors</a></li>
                        <li class="child-link"><a href="<html:rewrite page="/viewPage.do?pageCode=au5-n" />">Careers</a></li>
                        <li class="child-link"><a href="<html:rewrite page="/content/cc3/en/toolbar/aboutUs/newsRoom.html" />">News Room</a></li>
                        <li class="child-link"><a href="<html:rewrite page="/content/cc3/en/toolbar/aboutUs/eventsAndTradeshows.html" />">Events & Tradeshows</a></li>
                    </ul>
                </li>
            </ul>
        </div>

        <div id="search-bar" class="sub-region">
            <div id="slogan">
                <html:img src="/media/images/home-logo-new2.gif" />
            </div>
            <div id="special-h1"><static:import file="home-page/h1-content.html" /></div>
            <div id="titlesearch">
                <security:ifUserAnonymous>
                    <html:link action="/loginBounce.do"><html:img alt="Log In" src="/media/images/bg-login.gif" /></html:link>
                </security:ifUserAnonymous>
                <util:else>
                    <security:ifUserAuthenticated>
                    </security:ifUserAuthenticated>
                    <util:else>
                        <html:link action="/loginBounce.do"><html:img alt="Log In" src="/media/images/bg-login.gif" /></html:link>
                    </util:else>
                </util:else>
                <html:link action=""></html:link>
            </div>
        </div>
    </div>
    </util:else>

