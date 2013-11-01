package com.copyright.ccc.web.forms;

import com.copyright.ccc.business.data.Publication;
import com.copyright.ccc.business.data.PublicationPermission;
import com.copyright.ccc.web.CCValidatorForm;
import com.copyright.data.inventory.UsageDescriptor;

public class LandingForm extends CCValidatorForm {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Publication _publication = null;

    public LandingForm() {
        _publication = null;
    }

    public LandingForm(Publication p) {
        _publication = p;
    }

    public Publication getPublication() {
        return _publication;
    }

    public void setPublication(Publication p) {
        _publication = p;
    }

    public String getMainTitle() {
        return _publication.getMainTitle();
    }

    public String getMainIDNo() {
        return _publication.getMainIDNo();
    }

    public String getPublicationYearRange() {
        return _publication.getPublicationYearRange();
    }

    public String getMainPublisher() {
        return _publication.getMainPublisher();
    }

    public String getMainAuthor() {
        return _publication.getMainAuthor();
    }

    public String getMainEditor() {
        return _publication.getMainEditor();
    }

    public String getVolume() {
        return _publication.getVolume();
    }

    public String getEdition() {
        return _publication.getEdition();
    }

    
    public PublicationPermission[] getNonAcademicTRXPhotocopy() {
    
        PublicationPermission[] permissions = 
            new PublicationPermission[_publication.getPublicationPermissions(UsageDescriptor.NON_ACADEMIC_TRX_PHOTOCOPY).length];
        
        for (int i=0; i < _publication.getPublicationPermissions(UsageDescriptor.NON_ACADEMIC_TRX_PHOTOCOPY).length; i++){
            permissions[i] = 
                (_publication.getPublicationPermissions(UsageDescriptor.NON_ACADEMIC_TRX_PHOTOCOPY)[i]);
        }
      
        return permissions;
    }
    
    public String getNonAcademicTRXPhotocopyTerms(){
        return _publication.getPublicationPermissions(UsageDescriptor.NON_ACADEMIC_TRX_PHOTOCOPY)[0].getRHTerms();
    }
    
    


}
