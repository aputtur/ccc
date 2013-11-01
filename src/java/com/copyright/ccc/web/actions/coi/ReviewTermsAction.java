package com.copyright.ccc.web.actions.coi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.business.data.TransactionItem;
import com.copyright.ccc.business.services.cart.CartServices;
import com.copyright.ccc.business.services.cart.PurchasablePermission;
import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.coi.ReviewTermsActionForm;


public class ReviewTermsAction extends CCAction
{
    private final static  String FORWARD = "forward";
    
    public ReviewTermsAction() { }
    
    /**
     * If no operation is specified, go to <code>reviewTerms()</code>.
     */
    public ActionForward defaultOperation( ActionMapping mapping, 
                                           ActionForm form, 
                                           HttpServletRequest request, 
                                           HttpServletResponse response )
    {
        return reviewTerms( mapping, form, request, response );
    }
    
    /**
    * Struts action that displays a list of order item terms and conditions. 
    * @param mapping is the struts ActionMapping passed in by the struts controller
    * @param form is the struts ActionForm passed in by the struts controller
    * @param request is the HttpServletRequest object
    * @param response is the HttpServletResponse object
    * @return the ActionForward object to the "forward" action mapping.
    */
    public ActionForward reviewTerms(ActionMapping mapping, 
                                         ActionForm form, 
                                         HttpServletRequest request, 
                                         HttpServletResponse response) 
    {
        ActionForward forward = mapping.findForward(FORWARD);
        
        // Get the right_inst from the request object and call the Right Service
        // to get the rights terms.  Don't return a terms list but just the term
        // text for a popup term display window.

        // Get the cart from the UserContext and return a list of 
        // Order Item titles with their rights terms.
        ReviewTermsActionForm reviewForm = castForm( ReviewTermsActionForm.class, form );        
        boolean academicFlag = false;
        boolean nonAcademicFlag = false;
        boolean rightsLnkFlag = false;
                
        if ( !CartServices.isCartEmpty())
        {
           // List cartItems = new ArrayList();
            List<PurchasablePermission> formItems = new ArrayList<PurchasablePermission>();
            
            //List items = CartServices.getItemsInCart();
//            List<PurchasablePermission> items = CartServices.getRegularOrderItemsInCart();
            List<PurchasablePermission> items = CartServices.getTFItemsInCart();
            
            for (PurchasablePermission perm : CartServices.getRightsLinkItemsInCart()) {
            	//if(perm.isSpecialOrder()){
            		items.add(perm);
            	//}
            	
            }
            
            
            Iterator<PurchasablePermission> itr = items.iterator();
            
            while (itr.hasNext())
            
            {
                PurchasablePermission per = itr.next();
                
                if(per.isAcademic()){
                    academicFlag = per.isAcademic();
                    }
                    if(!per.isRightsLink()  && !per.isAcademic()){
                    	nonAcademicFlag = true;
                        }
              
                    if(per.isRightsLink()){
                     rightsLnkFlag=per.isRightsLink();
                    }
                
                if (((per.getExternalCommentTerm() != null) && ( per.getExternalCommentTerm().length() > 0)) || ((per.getRightsQualifyingStatement() != null) && (per.getRightsQualifyingStatement().length() > 0)))
                {
                    //if (per.get)
                    //if
                    if (!rightInstExists(per.getRgtInst(), formItems))
                    {
                        formItems.add(per);
                    }
                    
                }else if(per.isRightsLink() && per.getLicenseTerms()!=null){
                	formItems.add(per);
                }
            }
            
            if (formItems.size() > 0)
            {
                //reviewForm.setTerms(CartServices.getItemsInCart());
                reviewForm.setTerms(items);
                reviewForm.setHasTerms(true);
            }
            else
            {
                reviewForm.setTerms(items);
                reviewForm.setHasTerms(false);
            }
            
            reviewForm.setAcademicCart( academicFlag );
            reviewForm.setNonAcademicCart( nonAcademicFlag );
            reviewForm.setRightslnkCart(rightsLnkFlag);
            reviewForm.setMixedCart(false);
            if(rightsLnkFlag && academicFlag && nonAcademicFlag){
            	reviewForm.setMixedCart(true);
            }
        }

        
        //reviewForm.setTerms(termsList);
        return forward;
    }
    
  private boolean rightInstExists(long rgtInst, List<PurchasablePermission> frmItems)
  {
	  
      if (frmItems != null)
      {
      
        Iterator<PurchasablePermission> itr = frmItems.iterator();
        while (itr.hasNext())
        {
            PurchasablePermission per = itr.next();
            //long rgtInst = null;
             if(!per.isRightsLink()){
            	 long rightInst = per.getRgtInst();
                 if (rightInst == rgtInst ){
                     return true;  //Right already exists in the array
                 }
            	 
             }
            
        }
          
      }
      else
      {
          return false;
      }
      
      return false;
      
  }
  
    public ActionForward showTermsForOrder(ActionMapping mapping, 
                                         ActionForm form, 
                                         HttpServletRequest request, 
                                         HttpServletResponse response) 
    {
        ArrayList<TransactionItem> terms = new ArrayList<TransactionItem>();
        boolean isAcademic = false;
        
        Object transactionItemObj = request.getAttribute( WebConstants.RequestKeys.TERMS_ITEM );
        if( transactionItemObj != null)
        {
            TransactionItem transactionItem = (TransactionItem)transactionItemObj;
            terms.add( transactionItem );
            
            isAcademic = transactionItem.isAcademic();
        }
        
        ReviewTermsActionForm reviewForm = castForm( ReviewTermsActionForm.class, form );
        reviewForm.setTerms( terms );
        reviewForm.setHasTerms(terms.size()>0);
        reviewForm.setAcademicCart( isAcademic );
        
        return mapping.findForward( FORWARD );
    }
}
