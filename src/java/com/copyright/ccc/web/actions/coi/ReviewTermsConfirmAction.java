package com.copyright.ccc.web.actions.coi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.business.data.OrderLicense;
import com.copyright.ccc.business.data.OrderSearchResult;
import com.copyright.ccc.business.data.TransactionItem;
import com.copyright.ccc.business.services.order.OrderLicenseServices;
import com.copyright.ccc.business.services.order.OrderLicensesException;
import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.actions.ArticleSearchAction;
import com.copyright.ccc.web.forms.coi.ReviewTermsActionForm;
import com.copyright.ccc.web.transaction.coi.TransactionItemTypeOfUseMapper;


public class ReviewTermsConfirmAction extends CCAction
{

    private static final  String FORWARD = "forward";
    private static final  String GRANT   = "Grant";
    protected static final Logger _logger = Logger.getLogger( ReviewTermsConfirmAction.class );
    
    public ReviewTermsConfirmAction() { }
    
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
        
        ActionForward forward = new ActionForward();
        
        String confirmationNumber = request.getParameter("confirmNum"); // Confirmation number of License
        String showCitation = request.getParameter("showCitation");
        String showTCCitation = request.getParameter("showTCCitation");
        long confNumber = Long.parseLong(confirmationNumber);
                
        if (showCitation == null)
        {
            forward = mapping.findForward(FORWARD);      
            showCitation="";
        }
        else if (showCitation.equalsIgnoreCase("TRUE"))
        {
            forward = mapping.findForward("showCitation");
        }
        else
        {
            forward = mapping.findForward(FORWARD);
        }
        
        if (showTCCitation==null) {
        	showTCCitation="";
        }
        // Get the right_inst from the request object and call the Right Service
        // to get the rights terms.  Don't return a terms list but just the term
        // text for a popup term display window.

        // Get the cart from the UserContext and return a list of 
        // Order Item titles with their rights terms.
        
        ReviewTermsActionForm termsForm = castForm( ReviewTermsActionForm.class, form );
        termsForm.setConfirmNumber(confNumber);

        OrderSearchResult orderSearchResult = null;
        boolean academicFlag = false;
        boolean nonAcademicFlag = false;
        boolean rightsLnkFlag = false;
        
        try
        {
        	orderSearchResult = OrderLicenseServices.searchAllOrderLicensesForConfirmationNumber(Long.valueOf(confNumber));
        }
        catch ( OrderLicensesException ole )
        {
          _logger.error( "Unable to retrieve order details for confirmation number: " + confNumber );          
        }
        

        if ( orderSearchResult != null)
        {
            List<OrderLicense> formItems = new ArrayList<OrderLicense>();
            
            List<OrderLicense> orderLicensesList = orderSearchResult.getOrderLicenses();
            
            Iterator<OrderLicense> itr = orderLicensesList.iterator();
            
            boolean addItem = false;
            while (itr.hasNext())
            
            {
                OrderLicense per = itr.next();
                if(per.isAcademic()){
                academicFlag = per.isAcademic();
                }
                if(!per.isRightsLink()  && !per.isAcademic()){
                	nonAcademicFlag = true;
                    }
          
                if(per.isRightsLink()){
                 rightsLnkFlag=per.isRightsLink();
                }
                
                per.setCustomerReference(Long.toString(per.getID()));
                
                //For Review T & C filter out those where there are duplicate rights
                if (showCitation.isEmpty() || showTCCitation == "TRUE")  
                {
                    if (((per.getExternalCommentTerm() != null) && (per.getExternalCommentTerm().length() > 0 )) || ((per.getRightsQualifyingStatement() != null) && (per.getRightsQualifyingStatement().length() >0)))
                    {
                        if (!rightInstExists(per.getRgtInst(), per.getID(), formItems))
                        {
                        	addItem = true;
                        }
                    
                    }else if(per.isRightsLink() && per.getLicenseTerms()!=null){
                    	addItem = true;
                    }
                }
                //This is for Citation
                if (showCitation.equalsIgnoreCase("TRUE") || showTCCitation == "TRUE")
                {
                	if (per.getDisplayPermissionStatus()!=null && per.getDisplayPermissionStatus().equalsIgnoreCase(GRANT))
                    {
                        String tpuTrans = getTPUTranslation(per);
                        
                        //just use this field to store this value and display
                        //in the Citation page
                        per.setEditor(tpuTrans);
                        addItem = true;
                    }
                }
                
                if (addItem){
                	formItems.add(per);
                	addItem=false;
                }
            }
            
            if (formItems.size() > 0)
            {
                termsForm.setTerms(orderLicensesList);
                termsForm.setHasTerms(true);
            }
            else
            {
                termsForm.setTerms(orderLicensesList);
                termsForm.setHasTerms(false);
            }
            
            
            termsForm.setAcademicCart( academicFlag );
            termsForm.setNonAcademicCart( nonAcademicFlag );
            termsForm.setRightslnkCart(rightsLnkFlag);
        	termsForm.setMixedCart(false);
            if(rightsLnkFlag && academicFlag && nonAcademicFlag){
            	termsForm.setMixedCart(true);
            }
        }
        
        return forward;
    }
    
  private String getTPUTranslation ( OrderLicense odl)
  {
      int tpuType;
      String tpuTrans = "";
      
      tpuType  = TransactionItemTypeOfUseMapper.getTypeOfUseCodeForTransactionItem(odl);
      
      switch (tpuType)
      {
                          
          case 0:
            tpuTrans = "Copy";
            break;
          
          case 1:
            tpuTrans = "Copy";
            break;
          
          case 2:
            tpuTrans = "electronic usage";
            break;
          
          case 3:
            tpuTrans = "email";
            break;
          
          case 4:
            tpuTrans = "extranet posting";
            break;
          
          case 5:
            tpuTrans = "intranet posting";
            break;
          
          case 6:
            tpuTrans = "Internet posting";
            break;
          
          case 7:
            tpuTrans = "Brochure";
            break;
          
          case 8:
            tpuTrans = "CD ROM";
            break;
          
          case 9:
            tpuTrans = "Dissertation";
            break;
          
          case 10:
            tpuTrans = "DVD";
            break;
          
          case 11:
            tpuTrans = "Journal";
            break;
          
          case 12:
            tpuTrans = "Magazine";
            break;
          
          case 13:
            tpuTrans = "Newsletter";
            break;
          
          case 14:
            tpuTrans = "Newspaper";
            break;
          
          case 15:
            tpuTrans = "Other book";
            break;
          
          case 16:
            tpuTrans = "Pamphlet";
            break;
          
          case 17:
            tpuTrans = "Presentation";
            break;
          
          case 18:
            tpuTrans = "Textbook";
            break;
            
          case 19:
            tpuTrans = "Tradebook";
            break;
          
          default:
            tpuTrans = "";                                 
                    
      }
      
      return tpuTrans;       
      
  }
  
  private boolean rightInstExists(long rgtInst, long odt, List<OrderLicense> frmItems)
  {
      if (frmItems != null)
      {
      
        Iterator<OrderLicense> itr = frmItems.iterator();
        int i = 0;
        String odtString = "";
        while (itr.hasNext())
        {
            OrderLicense per = itr.next();
            
            if(!per.isRightsLink()){
		            //long rgtInst = null;
		            long rightInst = per.getRgtInst();
		            if (rightInst == rgtInst )
		            {
		                //Right inst exists in the List. So, don't add it. Instead
		                //Update the list with the Order details concatenated
		                
		                odtString = per.getCustomerReference();
		                odtString = odtString + ", " + Long.toString(odt);
		                per = frmItems.remove(i);
		                
		                per.setCustomerReference(odtString);
		                
		                frmItems.add(per);
		                
		                return true;  //Right already exists in the arry
		            }
		          	 
            }

            
            i++;
        }
          
      }
      else
      {
          return false;
      }
      
      return false;
      
  }
}
