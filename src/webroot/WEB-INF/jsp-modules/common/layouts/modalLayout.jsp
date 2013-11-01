<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<style>

#popup_inner h1 {width:661px;}

</style>
<div class="cc2">
  <div id="popup_pane">
    <div id="popup_wrapper" style="background:url('/media/images/popup-shadow-repeat.GIF') repeat-y scroll 673px 0 transparent;width:674px;">
      <div id="popup_inner" style="width:670px;">
        <div id="popup">
               <tiles:insert attribute="pageContent"/>       
        </div>
        <div class="clearer"></div>

      </div>
    </div>
 </div>
</div>



        