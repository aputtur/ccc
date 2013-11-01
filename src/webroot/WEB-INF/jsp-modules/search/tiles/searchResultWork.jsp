<%@ page errorPage="/jspError.do" %>
<!-- NOTE:
	 This tile displays search results.  When results are returned
	 they will be ordered with a weight that indicates a frequently
	 searched for work vs. any other work.  IF the first result is
	 so weighted, we need to throw up indicators for both types of
	 results.
	
	 It can/will also be used in the permissions landing page with
	 some minor (logic) alterations (for instance, no number next
	 to it and the rightsholder included as part of the data).
-->
<!-- LOGIC:  ITERATE THROUGH ALL WORKS -->

<!-- LOGIC:  Are there any frequently requested items? -->
<p class="greytype clearer">Frequently requested publications that match your search terms:</p>
<!-- END LOGIC -->

<!-- LOGIC:  If we had frequently published, we need to display this as well. -->
<p class="greytype">Additional publications that match your search terms:</p>
<!-- END LOGIC -->

<div class="search-result-basic">

<div class="search-result-title">
<h2 class="floatleft">5. <a href="#">Lorem Ipsum</a></h2>
<a href="#"><img src="images/bt_permission-options.gif" alt="Permission Options" width="122" height="18" class="floatright" /></a>
<div class="clearer"></div>
</div>

<div class="item-list-details">
	
<!-- INSERT WORK WIDGET -->

</div>

</div>
<!-- LOGIC:  INSERT PERMISSION WIDGET IF APPLICABLE -->

<!-- END LOGIC:  ITERATE WORKS -->