<%@ page errorPage="/jspError.do" %>
<!-- NOTE:
	 This chunk of code relies on EACH iteration of the search results.
	 Depending on what was selected on the advanced search form, we will
	 iterate through the possible permissions for the work, displaying
	 the relevant ones.  Nothing will display, or perhaps this tile will
	 never activate, if we are in simple search.
	
	 This tile is also SHARED with the permissions landing page.  It will
	 appear slightly different (and will rely on a single search result).
-->
<div class="paysummary-title">
<p>Permission type </p> <!-- or <p class="paysummary-column"> -->
<p>Availablility</p>
<p>Rightsholder terms </p>
<div class="clearer"></div>
</div>
<div class="paysummary clearer">
<!-- LOGIC:  ITERATE THROUGH ALL PERMISSIONS BASED ON SEARCH CRITERIA -->
    <div class="paysummary-line"> <!-- or paysummary-line-alt -->
		<p>Photocopy for coursepacks, classroom handouts. <a href="#">More&hellip;</a></p>
		<p><span class="icon-check"><a href="#">Available for purchase</a></span></p>
		<p><a href="#">Terms apply</a></p>
		<a href="#"><img align="top" class="floatright" src="images/btn_priceorder_lblue.gif" width="96" height="18" /></a>
		<div class="clearer"></div>
<!-- LOGIC:  ALERT IS ONE-TIME ONLY in search results or NOT AT ALL in landing page. -->
		<div class="icon-alert"><strong>Only staff-produced materials may be used. Images may not be used.</strong></div>
    </div>
<!-- LOGIC:  APPEARS ONLY IF ANNUAL LICENSE PERMISSION -->
    <p><a href="#">Multinational exceptions to our annual license agreement</a></p>
</div>