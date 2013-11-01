package com.copyright.ccc.web.forms.coi;


import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.business.data.TransactionItem;
import com.copyright.ccc.business.services.cart.PurchasablePermission;
import com.copyright.ccc.web.CCValidatorForm;
import com.copyright.ccc.web.transaction.PricingError;
import com.copyright.ccc.web.transaction.TransactionConstants;

public class BasicTransactionForm extends CCValidatorForm
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TransactionItem _transactionItem;
    private PurchasablePermission _purchasablePermissionInCart; //for editing cart items
    private int _orderPurchaseID; //for adding orders
    private int _typeOfUseCode; //for digital types
    
    private boolean _expanded;
    private PricingError _pricingError;
    
    private String _cancelPath;
    private String _formPath;
    private String _selectPermissionPathFromSpecial;
    private boolean _skipQuickpriceFlag;
    private boolean _firstAcademicItem;
    private boolean _useOnlyInNA;
    
    private String _rightPermissionType;
    
    private boolean _displayPublicationYearRange;
    private boolean _calculatePriceOnLoad;
        
    // For order history tab/paging persistance on cancel path link
    private int cp; // current scroll page parameter
    private String rp; // return page parameter identifying source tab ('main' or 'detail')
    private boolean sf; // search flag to indicate if source page has an active search

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
             
    public void setUseOnlyInNA(boolean flag) {
        _useOnlyInNA = flag;
    }
    
    public boolean getUseOnlyInNA() {
        return _useOnlyInNA;
    }
    
    public void setRightPermissionType(String rightPermTye)
    {
        this._rightPermissionType = rightPermTye; 
    }
    
    public String getRightPermissionType()
    {
        return _rightPermissionType;  
    }
    
    public void setTransactionItem(TransactionItem transactionItem)
    {
        this._transactionItem = transactionItem;
    }

    public TransactionItem getTransactionItem()
    {
        return _transactionItem;
    }

    public void setExpanded(boolean expanded)
    {
        this._expanded = expanded;
    }

    public boolean isExpanded()
    {
        return _expanded;
    }

    public void setPricingError(PricingError pricingError)
    {
        this._pricingError = pricingError;
    }

    public PricingError getPricingError()
    {
        return _pricingError;
    }

    public void setTypeOfUseCode(int typeOfUseCode)
    {
        this._typeOfUseCode = typeOfUseCode;
    }

    public int getTypeOfUseCode()
    {
        return _typeOfUseCode;
    }

    @Override
    public String getValidationKey(ActionMapping mapping, 
                                   HttpServletRequest request)
    {
        String originalValidationKey = super.getValidationKey(mapping, request);
        
        if(_transactionItem == null) return originalValidationKey;
        
        else
        {
            if(_transactionItem.isAcademic())
                return originalValidationKey + "_" + TransactionConstants.VALIDATION_ITEM_TYPE_ACADEMIC;
            else if(_transactionItem.isPhotocopy())
                return originalValidationKey + "_" + TransactionConstants.VALIDATION_ITEM_TYPE_PHOTOCOPY;
            else if(_transactionItem.isDigital())
            {
                if(_typeOfUseCode == TransactionConstants.TYPE_OF_USE_EMAIL)
                    return originalValidationKey + "_" + TransactionConstants.VALIDATION_ITEM_TYPE_EMAIL;
                else
                    return originalValidationKey + "_" + TransactionConstants.VALIDATION_ITEM_TYPE_NET;
            }
            else if(_transactionItem.isRepublication())
                return originalValidationKey + "_" + TransactionConstants.VALIDATION_ITEM_TYPE_REPUBLICATION;
            else return originalValidationKey;
        }
    }

    public void setOrderPurchaseID(int orderPurchaseID)
    {
        this._orderPurchaseID = orderPurchaseID;
    }

    public int getOrderPurchaseID()
    {
        return _orderPurchaseID;
    }


    public void setPurchasablePermissionInCart(PurchasablePermission purchasablePermissionInCart)
    {
        this._purchasablePermissionInCart = purchasablePermissionInCart;
    }

    public PurchasablePermission getPurchasablePermissionInCart()
    {
        return _purchasablePermissionInCart;
    }

    public void setFormPath(String formPath)
    {
        this._formPath = formPath;
    }

    public String getFormPath()
    {
        return _formPath;
    }

    public void setCancelPath(String cancelPath)
    {
        this._cancelPath = cancelPath;
    }

    public String getCancelPath()
    {
        return _cancelPath;
    }

    public void setDisplayPublicationYearRange(boolean displayPublicationYearRange)
    {
        this._displayPublicationYearRange = displayPublicationYearRange;
    }

    public boolean isDisplayPublicationYearRange()
    {
        return _displayPublicationYearRange;
    }

    public void set_displayPublicationYearRange(boolean _displayPublicationYearRange)
    {
        this._displayPublicationYearRange = _displayPublicationYearRange;
    }

    public boolean is_displayPublicationYearRange()
    {
        return _displayPublicationYearRange;
    }

    public void setCalculatePriceOnLoad(boolean calculatePriceOnLoad)
    {
        this._calculatePriceOnLoad = calculatePriceOnLoad;
    }

    public boolean isCalculatePriceOnLoad()
    {
        return _calculatePriceOnLoad;
    }

    public void setSelectPermissionPathFromSpecial(String selectPermissionPathFromSpecial)
    {
        this._selectPermissionPathFromSpecial = selectPermissionPathFromSpecial;
    }

    public String getSelectPermissionPathFromSpecial()
    {
        return _selectPermissionPathFromSpecial;
    }

    public void setFirstAcademicItem(boolean firstAcademicItem)
    {
        this._firstAcademicItem = firstAcademicItem;
    }

    public boolean isFirstAcademicItem()
    {
        return _firstAcademicItem;
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
    
    public void setSkipQuickprice(boolean flag) {
        _skipQuickpriceFlag = flag;
    }
    
    public boolean getSkipQuickprice() {
        return _skipQuickpriceFlag;
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
    
    public boolean getIsBiactive() {
		return isBiactive;
	}

	public void setBiactive(boolean isBiactive) {
		this.isBiactive = isBiactive;
	}	
}
