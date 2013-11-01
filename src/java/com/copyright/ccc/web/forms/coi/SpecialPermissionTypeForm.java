package com.copyright.ccc.web.forms.coi;

import com.copyright.ccc.web.CCValidatorForm;

public class SpecialPermissionTypeForm extends CCValidatorForm
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String _permissionType;
    private String _formPath;
    private String _purchaseId;
    private String _radioButton;

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
}
