package com.copyright.ccc.business.services.search;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.copyright.base.enums.PermTypeEnum;
import com.copyright.base.enums.ProductEnum;
import com.copyright.base.enums.TypeOfUseEnum;
import com.copyright.ccc.business.data.Publication;
import com.copyright.data.inventory.LicenseeClass;
import com.copyright.data.inventory.UsageDescriptor;
import com.copyright.internal.inventory.ProductUsePair;
import com.copyright.internal.inventory.UsageDescriptorMapper;
import com.copyright.svc.rightsResolver.api.data.PermissionSummaryCategory;

/**
 * An implementation of the PublicationPermission that extends
 * RightAdapter. This class 
 * represents a Right for a category that doesn't have any of its
 * own TF rights. This is needed because the downstream code requires
 * an instance of PublicationPermission when saving a cart and the 
 * RightsResolver won't return a Permission where there are no TF Rights.
 * 
 * @author jarbo
 *
 */
public class SpecialOrderNoRightAdapter extends RightAdapter {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PermissionSummaryCategory permissionSummaryCategory;
    private String _availability = "Available for Special Order";
    private Publication pub = null;
    private boolean isRRRightAdapter = false;
    private ProductEnum product;
    
    /**
     * Returns the PermissionSummaryCategory that 
     * this "Right" belongs to
     */
    @Override
    public PermissionSummaryCategory getPermissionSummaryCategory() {
		return permissionSummaryCategory;
	}
    /**
     * sets the PermissionSummaryCategory that 
     * this "Right" belongs to
     */
    @Override
    public void setPermissionSummaryCategory(
			PermissionSummaryCategory permissionSummaryCategory) {
		this.permissionSummaryCategory = permissionSummaryCategory;
	}
	/**
	 * Constructs a SpecialOrderNoRightAdapter that is backed by the Publication
	 * and the PermissionSummaryCategory provided
	 * @param pub
	 * @param category
	 */
	public SpecialOrderNoRightAdapter(Publication pub, PermissionSummaryCategory category) {
		super();
        this.pub = pub;
        this.permissionSummaryCategory = category;
        isRRRightAdapter = true;
        product = category.getProduct();
	}
	
	@Override
    public String getTypeOfUse(){
        return "";
    }

    /**
     * Returns the availability text that gets displayed for this
     * Special Order Right
     */
	@Override
    public String getAvailability(){
        return _availability;
    }

	@Override
    public String getRHTerms(){
		return "";
    }

	@Override
    public long getRgtInst(){
		return 0L;
    }

	@Override
    public long getRightsholderInst(){
    	return 0L;
    }
	@Override
    public long getTpuInst(){
		return 0L;
    }

	@Override
    public long getPrdInst(){
		return 0L;
    }
    
  
	@Override
    public String getRightsholderName(){
		return "";
    }
    
	@Override
    public String getRightsQualifyingStatement(){
		return "";
    }
    
    /**
     * Return the Publication associated with this Permission
     */
	@Override
    public Publication getPublication(){
        return pub;
    }
    
    /**
     * Returns the publication start date for which this
     * Permission applies. Returns the current date if
     * the permissions actual pub start date cannot be
     * determined.
     */
	@Override
    public Date getPubBeginDate(){

     	try {
     		Date pubDate;
     	
			DateFormat formatter;
			formatter = new SimpleDateFormat("yyyyMMdd");
			pubDate = formatter.parse(pub.getPublicationStartDate());
 
     		return pubDate;
     	} catch (Exception e) {
     		return new Date();
     	}


    }
    
    /**
     * Returns the publication end date for which this
     * Permission applies. Returns the current date if
     * the permissions actual pub end date cannot be
     * determined.
     */
	@Override
    public Date getPubEndDate(){

     	try {
     		Date pubDate;
     	
			DateFormat formatter;
			formatter = new SimpleDateFormat("yyyyMMdd");
			pubDate = formatter.parse(pub.getPublicationEndDate());
 
     		return pubDate;
     	} catch (Exception e) {
     		return null;
     	}
    }
    
	@Override
    public Boolean getIsAcademic(){
       if (product.compareTo(ProductEnum.APS) == 0 ||
           product.compareTo(ProductEnum.ARS) == 0 ||
           product.compareTo(ProductEnum.ECC) == 0){
               return true;
           }
       else {
           return false;
       }
    }

	@Override
    public boolean isSpecialOrder(){
        return true; 		
    }
    
	@Override
    public boolean isContactRHDirectly(){
    	return false; 
    }
    
	@Override
    public boolean isDenied(){
    	return false;
    }
    
	@Override
    public boolean isAvailable(){
		return false;
    }
    
	@Override
    public boolean getIsNotAvailable() {
    	return false;
    }
    
	@Override
    public Boolean getIsBiactiveRight() {
    	return false;
    }
    
	@Override
    public int getLicenseeClass(){
		return LicenseeClass.LIC_CLASS_INST_HIGHER_ED;
    }
    
	@Override
    public boolean getIsRRRightAdapter() {
    	return isRRRightAdapter;
    }
    
	@Override
    public PermTypeEnum getPermission() {
    	return PermTypeEnum.CONTACT_RIGHTSHOLDER;
    }
    
	@Override
    public String getExternalComments() {
    	return "";
    }
    
	@Override
    public boolean getHasVolumePrice() {
    	return false;
    }
    
	@Override
    public String getPubBegDtm(){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    	return sdf.format( getPubBeginDate() );
    }
    
	@Override
    public String getPubEndDtm(){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    	return sdf.format( getPubEndDate() );
    }
    
	@Override
    public String getQualifyingStmt(){
    	return ""; 
    }
    
	@Override
    public List<RightFeeAdapter>getRightFees() {
    	return new ArrayList<RightFeeAdapter>();
    }
	
	@Override
    public List<String>getRightFeeNames() {
    	return new ArrayList<String>();
    }
    
	@Override
    public Long getRightsholderId(){
    	return 0L; 
    }
    
	@Override
    public void setRightsholderId(Long rightsholderInst){ 
    }
    
	@Override
    public ProductEnum getProduct() {
    	return product; 
    }
    /**
     * Returns the TypeOfUseEnum value of this Permission. Since this 
     * particular Permission is not backed by a specific Right, this method
     * doesn't have a TypeOfUse to return. Instead, this method will 
     * arbitrarily choose one of the TypesOfUse corresponding to the
     * Product and return that.
     * This method can return null if the Product has not been set.
     */
	@Override
    public TypeOfUseEnum getTypeOfUseEnum() {
    	UsageDescriptor ud = getUsageDescriptor();
    	if (ud == null) {
    		return null;
    	}
    	ProductUsePair pair = UsageDescriptorMapper.getInstance().getProductUsePairFor(ud);
    	if (pair != null) {
	    	for(TypeOfUseEnum tou: TypeOfUseEnum.values()) {
	    		if (((Integer)tou.getId()).longValue() == pair.getTpuInst()) {
	    			return tou;
	    		}
	    	}
    	}
    	return null; 
    }

	@Override
	public RightAdapter getRightAdapter() {
		return this;
	}

    /**
     * Returns the UsageDescriptor of this Permission. Since this 
     * particular Permission is not backed by a specific Right, this method
     * doesn't have a TypeOfUse on which to build a UsageDescriptor. 
     * Instead, this method will arbitrarily choose one of the TypesOfUse corresponding to 
     * the Product and return that.
     * This method can return null if the Product has not been set or if the Product
     * is not recognized.
     */
	@Override
	public UsageDescriptor getUsageDescriptor() {
		if (product==null) {
			return null;
		}
    	switch (product) {
    	case ECC:
        	return new UsageDescriptor(UsageDescriptor.ACADEMIC_TRX_SCAN);
    	case APS:
        	return new UsageDescriptor(UsageDescriptor.ACADEMIC_TRX_PHOTOCOPY);
    	case TRS:
        	return new UsageDescriptor(UsageDescriptor.NON_ACADEMIC_TRX_PHOTOCOPY);
    	case DPS:
        	return new UsageDescriptor(UsageDescriptor.NON_ACADEMIC_TRX_EMAIL);
    	case RLS:
        	return new UsageDescriptor(UsageDescriptor.NON_ACADEMIC_TRX_REPUB_BROCHURE);
    	}
    	return null;
	}

}
