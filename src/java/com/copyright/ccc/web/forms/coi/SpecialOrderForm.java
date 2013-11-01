package com.copyright.ccc.web.forms.coi;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.business.data.TransactionItem;
import com.copyright.ccc.business.services.cart.PurchasablePermission;
import com.copyright.ccc.web.CCValidatorForm;
import com.copyright.ccc.web.transaction.PricingError;
import com.copyright.ccc.web.transaction.TransactionConstants;


public class SpecialOrderForm extends CCValidatorForm
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TransactionItem _specialOrderItem;
    private PurchasablePermission _specialPurchasablePermissionInCart; //for editing cart items
    private int _specialOrderPurchaseID; //for adding orders
    private int _specialTypeOfUseCode; //for digital types

    private String _specialCancelPath;
    private String _specialFormPath;
    private String _selectPermissionPath;
    private boolean _specialFirstAcademicItem;
    
    private String _selectPermissionPathFromSpecial;
    private PricingError _pricingError;
    private boolean _calculatePriceOnLoad;

    private boolean _specialDisplayPublicationYearRange;

    // For order history tab/paging persistance on cancel path link
    private int cp; // current scroll page parameter
    private String rp; // return page parameter identifying source tab ('main' or 'detail')
    private boolean sf; // search flag to indicate if source page has an active search
    
    private boolean missingTF = false;
    
    private String circulationDistributionTitle = "Total Circulation/Distribution";
    private String circulationDistributionText = "The total number of copies of the new publications which will " + 
                                                  "contain republished copyrighted content. A few examples:<br/>" + 
                                                  "<br/>-If a book excerpt is republished in a magazine with a print " + 
                                                  "run of 500, the total circulation/distribution would be 500.<br/>" + 
                                                  "<br/>-If a newspaper article is republished in a book and 20,000 " + 
                                                  "copies of that book are to be printed, the total " + 
                                                  "circulation/distribution would be 20000.<br/>" + 
                                                  "<br/>-If you are producing an electronic version of a book and you " + 
                                                  "want permission for 5,000 downloads, the total " + 
                                                  "circulation/distribution would be 5000";
    
    private String republishPortionTitle = "Portion";
    private String republishPortionText  = "The portion refers to the piece of the copyrighted content that will be republished. Please keep the following rules in mind when<br/>"+
    									   "choosing portion type:<br/><br/>"+
    									   "&#8226; A quotation can contain no more than five contiguous sentences<br/>"+
    									   "&#8226; An excerpt can contain no more than five contiguous paragraphs<br/>"+
    									   "&#8226; Selected pages can be any selection more than five contiguous paragraphs but less than a full article<br/>";

    
    private boolean isBiactive = false;
        
  
    public void setSpecialOrderItem(TransactionItem specialOrderItem)
    {
        this._specialOrderItem = specialOrderItem;
    }

    public TransactionItem getSpecialOrderItem()
    {
        return _specialOrderItem;
    }

    public void setSpecialTypeOfUseCode(int specialTypeOfUseCode)
    {
        this._specialTypeOfUseCode = specialTypeOfUseCode;
    }

    public int getSpecialTypeOfUseCode()
    {
        return _specialTypeOfUseCode;
    }
    
    @Override
    public String getValidationKey(ActionMapping mapping, 
                                   HttpServletRequest request)
    {
        String originalValidationKey = super.getValidationKey(mapping, request);
        
        if(_specialOrderItem == null) return originalValidationKey;
        
        else
        {
            if(_specialOrderItem.isAcademic())
                return originalValidationKey + "_" + TransactionConstants.VALIDATION_ITEM_TYPE_ACADEMIC;
            else if(_specialOrderItem.isPhotocopy())
                return originalValidationKey + "_" + TransactionConstants.VALIDATION_ITEM_TYPE_PHOTOCOPY;
            else if(_specialOrderItem.isDigital())
            {
                if(_specialTypeOfUseCode == TransactionConstants.TYPE_OF_USE_EMAIL)
                    return originalValidationKey + "_" + TransactionConstants.VALIDATION_ITEM_TYPE_EMAIL;
                else
                    return originalValidationKey + "_" + TransactionConstants.VALIDATION_ITEM_TYPE_NET;
            }
            else if(_specialOrderItem.isRepublication())
                return originalValidationKey + "_" + TransactionConstants.VALIDATION_ITEM_TYPE_REPUBLICATION;
            else return originalValidationKey;
        }
    }

    public void setSpecialOrderPurchaseID(int specialOrderPurchaseID)
    {
        this._specialOrderPurchaseID = specialOrderPurchaseID;
    }

    public int getSpecialOrderPurchaseID()
    {
        return _specialOrderPurchaseID;
    }


    public void setSpecialPurchasablePermissionInCart(PurchasablePermission specialPurchasablePermissionInCart)
    {
        this._specialPurchasablePermissionInCart = specialPurchasablePermissionInCart;
    }

    public PurchasablePermission getSpecialPurchasablePermissionInCart()
    {
        return _specialPurchasablePermissionInCart;
    }

    public void setSpecialFormPath(String specialFormPath)
    {
        this._specialFormPath = specialFormPath;
    }

    public String getSpecialFormPath()
    {
        return _specialFormPath;
    }

    public void setSpecialCancelPath(String specialCancelPath)
    {
        this._specialCancelPath = specialCancelPath;
    }

    public String getSpecialCancelPath()
    {
        return _specialCancelPath;
    }

    public void setSelectPermissionPath(String selectPermissionPath)
    {
        this._selectPermissionPath = selectPermissionPath;
    }

    public String getSelectPermissionPath()
    {
        return _selectPermissionPath;
    }

    public void setSpecialDisplayPublicationYearRange(boolean specialDisplayPublicationYearRange)
    {
        this._specialDisplayPublicationYearRange = specialDisplayPublicationYearRange;
    }

    public boolean isSpecialDisplayPublicationYearRange()
    {
        return _specialDisplayPublicationYearRange;
    }

    public void setSpecialFirstAcademicItem(boolean specialFirstAcademicItem)
    {
        this._specialFirstAcademicItem = specialFirstAcademicItem;
    }

    public boolean isSpecialFirstAcademicItem()
    {
        return _specialFirstAcademicItem;
    }

    public void setCp(int cp) {
        this.cp = cp;
    }

    public int getCp() {
        return cp;
    }

    public void setRp(String rp) {
        this.rp = rp;
    }

    public String getRp() {
        return rp;
    }

    public void setSf(boolean sf) {
        this.sf = sf;
    }

    public boolean isSf() {
        return sf;
    }

    public void setCirculationDistributionTitle(String circulationDistributionTitle) {
        this.circulationDistributionTitle = circulationDistributionTitle;
    }

    public String getCirculationDistributionTitle() {
        return circulationDistributionTitle;
    }

    public void setCirculationDistributionText(String circulationDistributionText) {
        this.circulationDistributionText = circulationDistributionText;
    }

    public String getCirculationDistributionText() {
        return circulationDistributionText;
    }

    public void setMissingTF(Boolean missingTF) {
        if (missingTF == null) {
            this.missingTF = false;
        }
        else {
            this.missingTF = missingTF;
        }
    }
    
    public boolean getMissingTF() {
        return missingTF;
    }

    public boolean isMissingTF() {
        return missingTF;
    }
    
    public boolean getIsBiactive() {
		return isBiactive;
	}

	public void setBiactive(boolean isBiactive) {
		this.isBiactive = isBiactive;
	}
	
	public void setSelectPermissionPathFromSpecial(String selectPermissionPathFromSpecial)
    {
        this._selectPermissionPathFromSpecial = selectPermissionPathFromSpecial;
    }

    public String getSelectPermissionPathFromSpecial()
    {
        return _selectPermissionPathFromSpecial;
    }
    
    public void setPricingError(PricingError pricingError)
    {
        this._pricingError = pricingError;
    }

    public PricingError getPricingError()
    {
        return _pricingError;
    }
    
    public void setCalculatePriceOnLoad(boolean calculatePriceOnLoad)
    {
        this._calculatePriceOnLoad = calculatePriceOnLoad;
    }

    public boolean isCalculatePriceOnLoad()
    {
        return _calculatePriceOnLoad;
    }
    
    public void setRepublishPortionTitle(String republishPortionTitle) {
        this.republishPortionTitle = republishPortionTitle;
    }

    public String getRepublishPortionTitle() {
        return republishPortionTitle;
    }
    public void setRepublishPortionText(String republishPortionText) {
        this.republishPortionText = republishPortionText;
    }

    public String getRepublishPortionText() {
        return republishPortionText;
    }
}
