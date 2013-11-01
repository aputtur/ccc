       
       <logic:equal name="cartFormCOI" property="hasCartErrors" value="true">
       <br/> 
        <div style="width:100%" align="center">
                <div class="calloutbox" style="width:80%" align="left">
                    <p style="color:red">
                   Unexpected error occurred while repricing your cart Items.
                    </p>
                    </div>
                    </div>
                    <br/>
</logic:equal>
        
         <logic:equal name="cartFormCOI" property="hasCartItemUpdates" value="true">
         <br/>
            <div style="width:100%" align="center">
                <div class="calloutbox" style="width:80%" align="left">
                    <p>
                    
                    <logic:equal name="cartFormCOI" property="isCartItemEdited" value="true">
                       The changes you made to one of your cart items has altered the price or permission status of other items.<br><br>
                       </logic:equal>
                       <logic:equal name="cartFormCOI" property="isCartItemEdited" value="false">
                       Some of the items in your cart have been updated. Pricing or permission status may have been changed by the rightsholder after you originally added the items to your cart.<br><br>
                       </logic:equal>
                        <logic:equal name="cartFormCOI" property="hasCartUpdatedToPriceChangedItems" value="true">
                        <p>.&nbsp;&nbsp; Items with a new price are marked "UPDATED PRICE" below.</p>
           						<!-- Check previous version 84202 for price update item -->
		                        <br/>
                        </logic:equal>
                        
                           <logic:equal name="cartFormCOI" property="hasCartUpdatedToSpecialOrderItems" value="true">
                       			 The following item(s) are now available only through Special Order:<br/><br/>
		                        <logic:equal  name="cartFormCOI" property="hasAcademicChangedToSOItems" value="true">
		                        <b>Course Items:</b>
		                        <logic:iterate name="cartFormCOI" property="updatedAcademicCartItems" id="updateToSOItem" indexId="updateToSOItemIndex" type="UpdatedCartItem">
		                        
		                        		<logic:equal name="updateToSOItem" property="isUpdatedToSpecialOrder" value="true">
		                        				<p>&nbsp;&nbsp; <b><bean:write name="updateToSOItem" property="cartIndex" />.&nbsp;&nbsp;</b><bean:write name="updateToSOItem" property="publicationTitle" /></p>
		                        		</logic:equal>
		                        
		                        </logic:iterate>
		                        </logic:equal>
		                        
		                        <logic:equal  name="cartFormCOI" property="hasNonAcademicChangedToSOItems" value="true">
		                       <b>Single Items:</b>
		                        <logic:iterate name="cartFormCOI" property="updatedNonAcademicCartItems" id="updateToSOItem" indexId="updateToSOItemIndex" type="UpdatedCartItem">
		                        
		                        		<logic:equal name="updateToSOItem" property="isUpdatedToSpecialOrder" value="true">
		                        			<p>&nbsp;&nbsp; <b><bean:write name="updateToSOItem" property="cartIndex" />.&nbsp;&nbsp;</b><bean:write name="updateToSOItem" property="publicationTitle" /></p>
		                        			</logic:equal>
		                              </logic:iterate>
		                        </logic:equal>
		                        <br/>
                        </logic:equal>
                               <logic:equal name="cartFormCOI" property="hasCartUpdatedToRemovedItems" value="true">
                       			<p>The following item(s) were removed from your cart because they are no longer available as requested.</p>
		                        <logic:equal  name="cartFormCOI" property="hasAcademicRemovedItems" value="true">
		                      <b>Course Items:</b>
		                        <logic:iterate name="cartFormCOI" property="updatedAcademicCartItems" id="updateToRemovetem" indexId="updateToRemovetemIndex" type="UpdatedCartItem">
		                        			<p>&nbsp;&nbsp;&nbsp;&nbsp;<bean:write name="updateToRemovetem" property="publicationTitle" /></p>
		                        </logic:iterate>
		                        </logic:equal>
		                        
		                        <logic:equal  name="cartFormCOI" property="hasNonAcademicRemovedItems" value="true">
		                       <b>Single Items:</b>
		                        <logic:iterate name="cartFormCOI" property="updatedNonAcademicCartItems" id="updateToRemovetem" indexId="updateToRemovetemIndex" type="UpdatedCartItem">
		                        			<p>&nbsp;&nbsp;&nbsp;&nbsp;<bean:write name="updateToRemovetem" property="publicationTitle" /></p>
		                        </logic:iterate>
		                        </logic:equal>
                        </logic:equal>
                        
                        
                    </p>
                </div>
                <br>
            </div>
        </logic:equal>