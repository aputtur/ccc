package com.copyright.ccc.web.forms.coi;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.copyright.ccc.business.data.TransactionItem;
import com.copyright.ccc.web.CCValidatorForm;
import com.copyright.ccc.web.util.PermSummaryTouDisplay;
import com.copyright.ccc.web.util.PermissionCategoryDisplay;

public class RLinkSpecialOrderForm extends CCValidatorForm
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String _permissionType;
    private String _formPath;
    private String _purchaseId;
    private String _radioButton;
    private String _publicationName;
    private String _issn;
    private String _publisher;
    private String _volume;
    private String _edition;
    private String _typeOfUse;
    private String _specialFormPath = "/specialOrderRLink.do";
    private String _author;
    private String _pubDate;
    private String _catID;
    private String _categoryDescription;
    private String _typeOfUseDescription;
    private String _typeOfUseId;
    private TransactionItem _specialOrderItem;
    private PermissionCategoryDisplay _permissionCategoryDisplay;
    private String _selectedWrkInst = null;
    private String _rightId;
    private boolean _isSpecialOrderFromScratch = false;
    
    private String _selectedRlPermissionType = "";
    private String _selectedOfferChannel = "";
    private String _selectedRlPubCode = "";
    private boolean _specialOrder = true;

    public String getRadioButton() {
		return _radioButton;
	}

	public void setRadioButton(String button) {
		_radioButton = button;
	}

	public void setFormPath(String formPath)
    {
        this._formPath = formPath;
    }

    public String getFormPath()
    {
        return _formPath;
    }

    public void setPermissionType(String permissionType)
    {
        this._permissionType = permissionType;
    }

    public String getPermissionType()
    {
        return _permissionType;
    }

    public void setPurchaseId(String purchaseId)
    {
        this._purchaseId = purchaseId;
    }

    public String getPurchaseId()
    {
        return _purchaseId;
    }

	public void setPublicationName(String publicationName) {
		this._publicationName = publicationName;
	}

	public String getPublicationName() {
		return _publicationName;
	}

	public void setIssn(String issn) {
		this._issn = issn;
	}

	public String getIssn() {
		return _issn;
	}

	public void setPublisher(String publisher) {
		this._publisher = publisher;
	}

	public String getPublisher() {
		return _publisher;
	}

	public void setVolume(String volume) {
		this._volume = volume;
	}

	public String getVolume() {
		return _volume;
	}

	public void setEdition(String edition) {
		this._edition = edition;
	}

	public String getEdition() {
		return _edition;
	}

	public void setTypeOfUse(String typeOfUse) {
		this._typeOfUse = typeOfUse;
	}

	public String getTypeOfUse() {
		return _typeOfUse;
	}

	public void setSpecialFormPath(String specialFormPath) {
		this._specialFormPath = specialFormPath;
	}

	public String getSpecialFormPath() {
		return _specialFormPath;
	}

	public void setSpecialOrderItem(TransactionItem specialOrderItem) {
		this._specialOrderItem = specialOrderItem;
	}

	public TransactionItem getSpecialOrderItem() {
		return _specialOrderItem;
	}

	public void setPermissionSummaryCat(PermissionCategoryDisplay permissionCat) {
		this._permissionCategoryDisplay = permissionCat;
	}

	public PermissionCategoryDisplay getPermissionSummaryCat() {
		return _permissionCategoryDisplay;
	}
	
	public List<PermSummaryTouDisplay> getPermissionSummaryTypeOfUse()
	{
		return this.getPermissionSummaryCat().getPermSummaryTouDisplays();
	}

	public void setAuthor(String author) {
		this._author = author;
	}

	public String getAuthor() {
		return _author;
	}

	public void setPubDate(String pubDate) {
		this._pubDate = pubDate;
	}

	public String getPubDate() {
		return _pubDate;
	}
		
	public void setCategoryDescription(String categoryDescription) {
		this._categoryDescription = categoryDescription;
	}

	public String getCategoryDescription() {
		return _categoryDescription;
	}

	public void setTypeOfUseDescription(String typeOfUseDescription) {
		this._typeOfUseDescription = typeOfUseDescription;
	}

	public String getTypeOfUseDescription() {
		return _typeOfUseDescription;
	}

	public void setTypeOfUseId(String typeOfUseId) {
		this._typeOfUseId = typeOfUseId;
	}

	public String getTypeOfUseId() {
		return _typeOfUseId;
	}

	public void setCatID(String catID) {
		this._catID = catID;
	}

	public String getCatID() {
		return _catID;
	}

	public void setSelectedWrkInst(String selectedWrkInst) {
		this._selectedWrkInst = selectedWrkInst;
	}

	public String getSelectedWrkInst() {
		return _selectedWrkInst;
	}

	public void setRightId(String rightId) {
		this._rightId = rightId;
	}

	public String getRightId() {
		return _rightId;
	}

	public void setSelectedRlPermissionType(String selectedRlPermissionType) {
		this._selectedRlPermissionType = selectedRlPermissionType;
	}

	public String getSelectedRlPermissionType() {
		return _selectedRlPermissionType;
	}

	public void setSelectedOfferChannel(String selectedOfferChannel) {
		this._selectedOfferChannel = selectedOfferChannel;
	}

	public String getSelectedOfferChannel() {
		return _selectedOfferChannel;
	}

	public void setSelectedRlPubCode(String selectedRlPubCode) {
		this._selectedRlPubCode = selectedRlPubCode;
	}

	public String getSelectedRlPubCode() {
		return _selectedRlPubCode;
	}

	public void setIsSpecialOrderFromScratch(boolean _isSpecialOrderFromScratch) {
		this._isSpecialOrderFromScratch = _isSpecialOrderFromScratch;
	}

	public boolean isSpecialOrderFromScratch() {
		return _isSpecialOrderFromScratch;
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	  {
	      ActionErrors errors = super.validate(mapping, request);
		//ActionErrors errors = new ActionErrors();
	      
	      String parameter = mapping.getParameter();
	      String parameterValue = "";
	        if ( parameter != null ) 
	        {
	            // Did the request specify a value for the parameter named in 
	            // the action definition?
	            parameterValue = request.getParameter( parameter );
	        }
		
		//if (!mapping.getPath().equalsIgnoreCase("/specialOrderRLink")) {
	        
	      if (parameterValue.equalsIgnoreCase("goToSpecialOrder")) {
	    	  
	    	  if (_typeOfUseId == null || _typeOfUseId.equals("")) 
	          {
	              errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("errors.required","Type Of Use "));
	          }
	      
	    	  if (!this.getSpecialOrder()) {
	    		  if (_publicationName == null || _publicationName.equals("")) 
	    		  {
	    			  errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("errors.required","Publication Name "));
	    		  }
		      
	    		  if (_publisher == null || _publisher.equals("")) 
	    		  {
	    			  errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("errors.required","Publisher "));
	    		  }
	    	  }
	      	            
		}
	      
	      return errors;
	  }

	public void setSpecialOrder(boolean spOrder) {
		this._specialOrder = spOrder;
	}

	public boolean getSpecialOrder() {
		return _specialOrder;
	}
}
