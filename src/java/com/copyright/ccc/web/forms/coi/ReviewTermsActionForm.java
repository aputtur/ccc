package com.copyright.ccc.web.forms.coi;

import java.util.List;

import com.copyright.ccc.business.data.TransactionItem;
import com.copyright.ccc.web.CCValidatorForm;

public class ReviewTermsActionForm extends CCValidatorForm
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<? extends TransactionItem> terms;
    long id;
    boolean hasTerms;
    private boolean _academicCart;
    private boolean  _nonAcademicCart;
    private boolean _mixedCart;
    private boolean _rightslnkCart;
    
    private long confirmNumber;

    
    public ReviewTermsActionForm() { }

    public void setTerms(List<? extends TransactionItem> terms) {
        this.terms = terms;
    }

    public List<? extends TransactionItem> getTerms() {
        return terms;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
    
    public void setAcademicCart( boolean academicCart )
    {
        this._academicCart = academicCart;
    }

    public boolean isAcademicCart()
    {
        return _academicCart;
    }
    
    public void setMixedCart( boolean mixedCart )
    {
        this._mixedCart = mixedCart;
    }

    public boolean isMixedCart()
    {
        return _mixedCart;
    }
    
    
    public void setRightslnkCart( boolean rightslnkCart )
    {
        this._rightslnkCart = rightslnkCart;
    }

    public boolean isRightslnkCart()
    {
        return _rightslnkCart;
    }
    public void setNonAcademicCart( boolean nonAcademicCart )
    {
        this._nonAcademicCart = nonAcademicCart;
    }

    public boolean isNonAcademicCart()
    {
        return _nonAcademicCart;
    }
    
    
    
    
    public void setConfirmNumber( long confNumber )
    {
        this.confirmNumber = confNumber;
    }

    public long getConfirmNumber()
    {
        return confirmNumber;
    }
    
    public void setHasTerms(boolean terms) {
        this.hasTerms = terms ;
    }

    public boolean isHasTerms() {
     /*   boolean hasTerms = false;
        if (this.terms != null) {
          int size = this.terms.size();
          if (size > 0) {
             hasTerms = true;
          }
        } */
        
        return hasTerms;
    }
    

}
