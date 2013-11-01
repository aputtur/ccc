package com.copyright.ccc.web.forms.coi;

import java.util.ArrayList;

import com.copyright.ccc.business.data.OrderPurchase;
import com.copyright.ccc.business.services.cart.CourseDetails;
import com.copyright.ccc.business.services.cart.PurchasablePermission;
import com.copyright.ccc.web.CCValidatorForm;

public class CourseDetailsForm extends CCValidatorForm
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PurchasablePermission[] _purchasablePermissionsToAdd;
    private String[] _titlesThatCannotBeCopied;
    private OrderPurchase _orderPurchase;
    private CourseDetails _courseDetails;
    private boolean _cascadingChanges;
    private String _formPath;
    private String cp;
    private String rp;
    private String sf;
    private ArrayList<String> _rlTitlesThatCannotBeCopied;
    
    public void setCourseDetails(CourseDetails courseDetails)
    {
        this._courseDetails = courseDetails;
    }

    public CourseDetails getCourseDetails()
    {
        return _courseDetails;
    }

    public void setOrderPurchase(OrderPurchase orderPurchase)
    {
        this._orderPurchase = orderPurchase;
    }

    public OrderPurchase getOrderPurchase()
    {
        return _orderPurchase;
    }

    public void setCascadingChanges(boolean cascadingChanges)
    {
        this._cascadingChanges = cascadingChanges;
    }

    public boolean isCascadingChanges()
    {
        return _cascadingChanges;
    }

    public void setPurchasablePermissionsToAdd(PurchasablePermission[] purchasablePermissionsToAdd)
    {
        this._purchasablePermissionsToAdd = purchasablePermissionsToAdd;
    }

    public PurchasablePermission[] getPurchasablePermissionsToAdd()
    {
        return _purchasablePermissionsToAdd;
    }

    public void setFormPath(String formPath)
    {
        this._formPath = formPath;
    }

    public String getFormPath()
    {
        return _formPath;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getCp() {
        return cp;
    }

    public void setRp(String rp) {
        this.rp = rp;
    }

    public String getRp() {
        return rp;
    }

    public void setSf(String sf) {
        this.sf = sf;
    }

    public String getSf() {
        return sf;
    }

  public void setTitlesThatCannotBeCopied(String[] titlesThatCannotBeCopied)
  {
    this._titlesThatCannotBeCopied = titlesThatCannotBeCopied;
  }

  public String[] getTitlesThatCannotBeCopied()
  {
    return _titlesThatCannotBeCopied;
  }
  
  public void setRlTitlesThatCannotBeCopied(ArrayList<String> rlTitlesThatCannotBeCopied)
  {
    this._rlTitlesThatCannotBeCopied = rlTitlesThatCannotBeCopied;
  }

  public ArrayList<String> getRlTitlesThatCannotBeCopied()
  {
    return _rlTitlesThatCannotBeCopied;
  }
}
