package com.copyright.ccc.web.util;

import java.util.Date;

import com.copyright.ccc.business.data.Publication;
import com.copyright.ccc.business.data.PublicationPermission;
import com.copyright.ccc.business.services.search.RightAdapter;
import com.copyright.data.inventory.LicenseeClass;
import com.copyright.data.inventory.UsageDescriptor;
import com.copyright.svc.rightsResolver.api.data.Right;

/**
 * <<Class summary>>
 *
 * @author Michael Jessop &lt;&gt;
 * @version $Rev: 94011 $
 */
public final class FauxPublicationPermission implements PublicationPermission {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UsageDescriptor _typeOfUse = null;
    private Publication _publication = null;
    private Date _pubBeginDate = null;
    private Date _pubEndDate = null;
    private Right rrright = null;
    private boolean isRRRightAdapter;

    public FauxPublicationPermission(Publication original, UsageDescriptor typeOfUse) {
        _typeOfUse = typeOfUse;
        _publication = original;
    }
    
    public String getTypeOfUse()                 { return _typeOfUse.getDescription(); }
    public String getAvailability() 
    {
        switch (_typeOfUse.getTypeOfUse()) {
            case UsageDescriptor.ACADEMIC_LICENSE_PHOTOCOPY:
            case UsageDescriptor.NON_ACADEMIC_LICENSE_EMAIL:
            case UsageDescriptor.NON_ACADEMIC_LICENSE_PHOTOCOPY:
                return "Not covered";
            default:
                /*if (_publication.getRights()==null ||_publication.getRights().size()==0) {
                    return "Not covered";
                }*/
                return "Available for Special Order";
        }
    }

    public String getRHTerms()                   { return ""; }
    public long getRgtInst()                     { return 0; }
    public long getRightsholderInst()            { return 0; }
    public long getTpuInst()                     { return 0; }
    public long getPrdInst()                     { return 0; }

    public String getRightsholderName()          { return ""; }
    public String getRightsQualifyingStatement() { return ""; }
    public Publication getPublication()          { return _publication; }
    public String getPermDateRange()             { return ""; }
    public boolean isSpecialOrder()              { return true; } 
    public Date getPubBeginDate()                { return _pubBeginDate; }
    public Date getPubEndDate()                  { return _pubEndDate; }  
    public void setPubBeginDate(Date beginDate)  { _pubBeginDate = beginDate; }
    public void setPubEndDate(Date endDate)      { _pubEndDate = endDate; }
    public Boolean getIsAcademic() {
   		int tou = _typeOfUse.getTypeOfUse();
   		if ((tou == UsageDescriptor.ACADEMIC_TRX_PHOTOCOPY) ||
   		    (tou == UsageDescriptor.ACADEMIC_TRX_SCAN) ||
   		    (tou == UsageDescriptor.ACADEMIC_LICENSE_PHOTOCOPY)) {
   			return Boolean.TRUE;
   		} else {
   			return Boolean.FALSE;
   		}
    }
    
    public boolean isDenied()                    { return false; }
    public boolean isContactRHDirectly()         { return false; }
    public boolean isAvailable()                 { return false; }
    public int getLicenseeClass()                { return LicenseeClass.LIC_CLASS_ALL; }
    public UsageDescriptor getUsageDescriptor()  { return _typeOfUse; }
    public void setRRRight(Right right)        { this.rrright = right; }
    public Right getRRRight()                  {return rrright; }
    public boolean getIsRRRightAdapter()         {return isRRRightAdapter; }
    public RightAdapter getRightAdapter()        {return null; }
    public boolean getIsNotAvailable()           { return false; }
    
}
