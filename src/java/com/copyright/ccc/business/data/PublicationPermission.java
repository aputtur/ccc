package com.copyright.ccc.business.data;

import java.io.Serializable;
import java.util.Date;

import com.copyright.ccc.business.services.search.RightAdapter;
import com.copyright.data.inventory.UsageDescriptor;
import com.copyright.svc.rightsResolver.api.data.Right;


/**
 * @author  Mike Tremblay
 * @version 1.0
 * Created 26-Oct-2006 2:01:18 PM
 */
 
public interface PublicationPermission extends Serializable {


	public String getTypeOfUse();

	public String getAvailability();

	public String getRHTerms();

	public long getRgtInst();

    public long getRightsholderInst();
        
	public long getTpuInst();

	public long getPrdInst();
        
    public UsageDescriptor getUsageDescriptor();
        
    public String getRightsholderName();
        
    public String getRightsQualifyingStatement();
        
    public Publication getPublication();
        
    public Date getPubBeginDate();
        
    public Date getPubEndDate();
        
    public String getPermDateRange();
        
    public Boolean getIsAcademic();

    public boolean isSpecialOrder();
    
    public boolean isContactRHDirectly();
    
    public boolean isDenied();
    
    public boolean isAvailable();
    
    public boolean getIsNotAvailable();
    
    public int getLicenseeClass();
    
    public Right getRRRight();
    
    public void setRRRight(Right rrright);
    
    public boolean getIsRRRightAdapter();
    
    public RightAdapter getRightAdapter();


}