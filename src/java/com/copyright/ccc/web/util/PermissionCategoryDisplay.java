package com.copyright.ccc.web.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.xwork.StringUtils;

import com.copyright.base.CCCRuntimeException;
import com.copyright.base.enums.PermTypeEnum;
import com.copyright.base.enums.ProductEnum;
import com.copyright.ccc.business.data.Publication;
import com.copyright.ccc.business.data.RLinkPublisher;
import com.copyright.ccc.business.services.search.RightAdapter;
import com.copyright.data.inventory.UsageDescriptor;
import com.copyright.svc.rightsResolver.api.data.PermissionLevelEnum;
import com.copyright.svc.rightsResolver.api.data.PermissionSummaryCategory;
import com.copyright.svc.rightsResolver.api.data.PermissionSummaryTypeOfUse;
import com.copyright.svc.rightsResolver.api.data.Right;
import com.copyright.svc.rightsResolver.api.data.SourceCodeEnum;

public final class PermissionCategoryDisplay implements Serializable{
	
	private static final long serialVersionUID = 1L;
	PermissionSummaryCategory _permissionSummaryCategory;
	Publication _pub;
	private List<PermSummaryTouDisplay> _permissionTouDisplays = new ArrayList<PermSummaryTouDisplay>();
	String categoryDescription;
	private int index=-1;		
	private boolean globalRightsQualifier = true;

	public PermissionCategoryDisplay(Publication pub, PermissionSummaryCategory permissionSummaryCategory, int index){
		categoryDescription = permissionSummaryCategory.getCategory();
		_permissionSummaryCategory = permissionSummaryCategory;
		_pub = pub;
		
		String selectedQualStatement = "";
		if (permissionSummaryCategory.getTypeOfUseList().isEmpty()){
			globalRightsQualifier = false;
		}
		/*else {
			
			// new change CC-2605  rights with null qualifying statement is to be ignored
			if (permissionSummaryCategory.getTypeOfUseList().get(0).getTfRights() != null &&
					permissionSummaryCategory.getTypeOfUseList().get(0).getTfRights().size()>0){
				firstQualStatement =  permissionSummaryCategory.getTypeOfUseList().get(0).getTfRights().get(0).getQualifyingStmt();
				if (firstQualStatement == null || firstQualStatement.length()==0){
					globalRightsQualifier = false;
				}
			}
		}*/
		this.index = index;
		List<PermissionSummaryTypeOfUse> listPermissionSummaryTypeOfUse = permissionSummaryCategory.getTypeOfUseList();
		//create the list that will be shown as radio buttons
		for (PermissionSummaryTypeOfUse permissionSummaryTypeOfUse : listPermissionSummaryTypeOfUse){
			//Don't need to get most permissible anymore since tou has only one RRRight, unless it is bi-active
			//RightAdapter rightAdapter = ((WorkRightsAdapter)pub).findMostPermissableRight(getProduct());
			if (permissionSummaryTypeOfUse.getSourceCode() == SourceCodeEnum.RL) {
				_permissionTouDisplays.add(new PermSummaryTouDisplay(pub, permissionSummaryCategory.getProduct(), permissionSummaryTypeOfUse));
				//globalRightsQualifier = false;
			}
			else {
				PermSummaryTouDisplay permSummaryTouDisplay = new PermSummaryTouDisplay(pub, permissionSummaryCategory.getProduct(), permissionSummaryTypeOfUse);
				_permissionTouDisplays.add(permSummaryTouDisplay);
				//RightAdapter rightAdapter = new RightAdapter(pub,permSummaryTouDisplay.findMostPermissableRight());
				//if (rightAdapter.isAvailable() || rightAdapter.isSpecialOrder() || rightAdapter.isContactRHDirectly()) {
					//_permissionTouDisplays.add(permSummaryTouDisplay);
				//}
				if (globalRightsQualifier) {
					if ( permissionSummaryTypeOfUse.getTfRights() != null) {
						for (Right right : permissionSummaryTypeOfUse.getTfRights()){
							if(StringUtils.isEmpty(selectedQualStatement) && !StringUtils.isEmpty(right.getQualifyingStmt())){
								selectedQualStatement=right.getQualifyingStmt();
							}
							 if (right.getQualifyingStmt()!=null && !right.getQualifyingStmt().equalsIgnoreCase(selectedQualStatement)) {
								globalRightsQualifier = false;
							}
						}
					}
				}

				
			}
		}
    
		//TODO need logic to check if Category is TF ONly (flag on permissionSummaryCategory) if only Deny, Hold Pending, Contacts Rights HOlder for 
		//Permissions the need to call DB to check if publication is available for RightsLink
	}

	public PermissionSummaryCategory getPermissionSummaryCategory() {
		return _permissionSummaryCategory;
	}

	public String getUsageDescriptorText() {
		ProductEnum product = _permissionSummaryCategory.getProduct();
		if (product != null) {
			switch (product) {
			case APS:
				return (new UsageDescriptor(UsageDescriptor.ACADEMIC_TRX_PHOTOCOPY)).getDescription();
			case ECC:
				return (new UsageDescriptor(UsageDescriptor.ACADEMIC_TRX_SCAN)).getDescription();
			}
		}
		return "UNKNOWN";
	}
	public List<PermSummaryTouDisplay> getPermSummaryTouDisplays() {
		return _permissionTouDisplays;
	}

	public void setPermSummaryTouDisplay(List<PermSummaryTouDisplay> displays) {
		_permissionTouDisplays = displays;
	}
	
	public String getCategoryDescription() {
		return categoryDescription;
	}

	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	//Roll up Availability from underlying TF rights, RL is always available for purchase
    public String getAvailability() {
		List<PermissionSummaryTypeOfUse> listPermissionSummaryTypeOfUse = _permissionSummaryCategory.getTypeOfUseList();
		for (PermissionSummaryTypeOfUse permissionSummaryTypeOfUse : listPermissionSummaryTypeOfUse){
			if ((permissionSummaryTypeOfUse.getSourceCode()==SourceCodeEnum.RL) && (permissionSummaryTypeOfUse.getRlPermissionType() != null) && (permissionSummaryTypeOfUse.getRlPermissionType().getCd() == PermTypeEnum.DENY.getCode())) {
				return "Not Available";
			}
			if (permissionSummaryTypeOfUse.getSourceCode().compareTo(SourceCodeEnum.RL)==0){
				return "Available for Purchase";
			}
		}
		if ((_permissionTouDisplays == null ||_permissionTouDisplays.isEmpty()) && getIsLicense()) {
			return "Not Covered";
		}
		if (_permissionTouDisplays == null ||_permissionTouDisplays.isEmpty()) {
			return "Not Available";
		}
		
		if (getMostPermissablePermission()==null && getIsLicense()){
			return "Not Covered"; 
		}
		if (getMostPermissablePermission()==null && getIsNonAcademicPhotocopy())
		{
			return "Not Available"; 
		}
		//if we got this far with missing TF rights, then show it is available for Special Order
		if (getMostPermissablePermission()==null){
			return "Available for Special Order";
		}
		//OK there are TF rights, show the most permissable
		return getMostPermissablePermission().getAvailability();
    }
    
    /**
     * available is defined as: either sourced from Rightslink
     * OR
     * the most permissable TF permission is deemed available
     */
    public Boolean getIsAvailable() {
		List<PermissionSummaryTypeOfUse> listPermissionSummaryTypeOfUse = _permissionSummaryCategory.getTypeOfUseList();
		for (PermissionSummaryTypeOfUse permissionSummaryTypeOfUse : listPermissionSummaryTypeOfUse){
			if ((permissionSummaryTypeOfUse.getSourceCode()==SourceCodeEnum.RL) && (permissionSummaryTypeOfUse.getRlPermissionType() != null) && (permissionSummaryTypeOfUse.getRlPermissionType().getCd() == PermTypeEnum.DENY.getCode())) {
				return false;
			}
			if (permissionSummaryTypeOfUse.getSourceCode()==SourceCodeEnum.RL){
				return true;
			}
		}
		if (getAvailability().startsWith("Not")) {
   	       return false;
		}
    	return true;
    }
    
    public Boolean getIsAvailableRightslink(){
    	return getIsAvailableRightslink(_permissionSummaryCategory);
    }
    
    static private Boolean getIsAvailableRightslink(PermissionSummaryCategory permSumCat){
		List<PermissionSummaryTypeOfUse> listPermissionSummaryTypeOfUse = permSumCat.getTypeOfUseList();
		for (PermissionSummaryTypeOfUse permissionSummaryTypeOfUse : listPermissionSummaryTypeOfUse){
			if ((permissionSummaryTypeOfUse.getSourceCode()==SourceCodeEnum.RL) && (permissionSummaryTypeOfUse.getRlPermissionType() != null) && (permissionSummaryTypeOfUse.getRlPermissionType().getCd() == PermTypeEnum.DENY.getCode())) {
				return false;
			}
			if (permissionSummaryTypeOfUse.getSourceCode()==SourceCodeEnum.RL){
				return true;
			}
		}
		return false;    	
    }

    /**
     * isNotAvailable is defined as: not sourced from Rightslink
     * AND
     * the most permissable TF permission is NULL or it starts with "N"
     */    
    public Boolean getIsNotAvailable() {
		List<PermissionSummaryTypeOfUse> listPermissionSummaryTypeOfUse = _permissionSummaryCategory.getTypeOfUseList();
		for (PermissionSummaryTypeOfUse permissionSummaryTypeOfUse : listPermissionSummaryTypeOfUse){
			if ((permissionSummaryTypeOfUse.getSourceCode()==SourceCodeEnum.RL) && (permissionSummaryTypeOfUse.getRlPermissionType() != null) && (permissionSummaryTypeOfUse.getRlPermissionType().getCd() == PermTypeEnum.DENY.getCode())) {
				return true;
			}
			if (permissionSummaryTypeOfUse.getSourceCode()==SourceCodeEnum.RL){
				return false;
			}
		}
        String s = getAvailability();
        Boolean r = Boolean.FALSE;
        if (s == null || s.startsWith("N")) r = Boolean.TRUE;
        return r;
    }
    
    public Boolean getIntegrates() {
        for (PermSummaryTouDisplay permSummaryTouDisplay : _permissionTouDisplays){
        	if (permSummaryTouDisplay.getIntegrates()) return true;
        }
        return false;
    }
    
    public String getRhTerms() {
    	RightAdapter rightAdapter = getMostPermissablePermission();
    	if (rightAdapter==null) return null;
    	return rightAdapter.getRHTerms();
    }
    
    public Boolean getIsDigital() {
    	if (_permissionSummaryCategory.getProduct()== null) return false;
    	return _permissionSummaryCategory.getProduct().equals(ProductEnum.DPS);
    }
    
    public Boolean getIsRepublication() {
    	if (_permissionSummaryCategory.getProduct()== null) return false;
    	return _permissionSummaryCategory.getProduct().equals(ProductEnum.RLS);   	
    }
    
    public Boolean getIsPublicDomain() {
        String s = getAvailability();
        Boolean r = Boolean.FALSE;
        if (s != null && s.startsWith("Public Domain")) return Boolean.TRUE;
        return r;   	
    }
    
    public Boolean getIsContactRightsholder() {
        String s = getAvailability();
        Boolean r = Boolean.FALSE;
        if (s != null && s.startsWith("Con")) return Boolean.TRUE;
        return r;
    }
    public Boolean getIsSpecialOrder() {
	    String s = getAvailability();
        Boolean r = Boolean.FALSE;
        if (s != null && s.startsWith("Available for Sp")) return Boolean.TRUE;
        return r;
    }
    
    public Boolean getIsContactRHOrSpecialOrder() {
    	if (getIsContactRightsholder() || getIsSpecialOrder())
    		return true;
    	else return false;
    }
    public Boolean getGotTerms() {
    	RightAdapter rightAdapter = getMostPermissablePermission();
	    if (rightAdapter==null) return false;
        String s = rightAdapter.getRHTerms();
        Boolean r = Boolean.FALSE;
        if (!isNull(s)) r = Boolean.TRUE;
        return r;
	}
    
    public Boolean getGotRQualStmt() {
       	RightAdapter rightAdapter = getMostPermissablePermission();
	    if (rightAdapter==null) return false;
        String s = rightAdapter.getRightsQualifyingStatement();
        Boolean r = Boolean.FALSE;
        if (!isNull(s)) r = Boolean.TRUE;
        return r;
	}
    
    public String getRightsQualifyingStatement() {
       	RightAdapter rightAdapter = getMostPermissablePermission();
	    if (rightAdapter==null) return "";
        return rightAdapter.getRightsQualifyingStatement();
    }
    public Boolean getIsProductNull(){
    	return _permissionSummaryCategory.getProduct()==null;
    }
    
    public Boolean getIsValidCategory(){
    	if (getIsProductNull() && !getIsAvailableRightslink()) {
    		return false;
    	}
    	return true;
    }
    
    public Boolean getIsLicense() {
    	ProductEnum productEnum = _permissionSummaryCategory.getProduct();
        return productEnum != null && (productEnum.equals(ProductEnum.ARS) || productEnum.equals(ProductEnum.AAS) || productEnum.equals(ProductEnum.DRA));
    }
    
    public Boolean getIsNotLicense(){
    	return getIsProductNull() || !getIsLicense();
    }
    
    public Boolean getIsAcademic() {
    	ProductEnum productEnum = _permissionSummaryCategory.getProduct();
        return productEnum != null && (productEnum.equals(ProductEnum.ARS)||productEnum.equals(ProductEnum.ECC)||productEnum.equals(ProductEnum.APS));
    }

    public Boolean getIsNonAcademicLicense() {
    	ProductEnum productEnum = _permissionSummaryCategory.getProduct();
        return productEnum != null && (productEnum.equals(ProductEnum.AAS) || productEnum.equals(ProductEnum.DRA));
    }
    
    public Boolean getIsNonAcademicPhotoCopyLicense() {
    	ProductEnum productEnum = _permissionSummaryCategory.getProduct();
        return productEnum != null && productEnum.equals(ProductEnum.AAS);
    }
    
    public Boolean getIsNonAcademicEmailLicense() {
    	ProductEnum productEnum = _permissionSummaryCategory.getProduct();
        return productEnum != null && productEnum.equals(ProductEnum.DRA);
    }
    
    public Boolean getIsAcademicLicense() {
    	ProductEnum productEnum = _permissionSummaryCategory.getProduct();
        return productEnum != null && productEnum.equals(ProductEnum.ARS);
    }
    
    public Boolean getIsAcademicPhotocopy() {
    	ProductEnum productEnum = _permissionSummaryCategory.getProduct();
        return productEnum != null && productEnum.equals(ProductEnum.APS);    	
    }
    
    public Boolean getIsNonAcademicPhotocopy() {
    	ProductEnum productEnum = _permissionSummaryCategory.getProduct();
        return productEnum != null && productEnum.equals(ProductEnum.TRS);    	
    }
    
    public Boolean getIsEReserves() {
    	ProductEnum productEnum = _permissionSummaryCategory.getProduct();
        return productEnum != null && productEnum.equals(ProductEnum.ECC);    	
    }
    
    public long getRightsholderInst() {
    	if (getMostPermissablePermission() == null) return 0;
    	return getMostPermissablePermission().getRightsholderInst();
    }
    
    public Boolean getIsTitleLevelPermission(){
    	PermissionLevelEnum permissionLevelEnum = _permissionSummaryCategory.getPermissionLevel();
        return permissionLevelEnum != null && permissionLevelEnum.equals(PermissionLevelEnum.TITLE_LEVEL);    
    }
    public Boolean getIsChapterLevelPermission(){
    	PermissionLevelEnum permissionLevelEnum = _permissionSummaryCategory.getPermissionLevel();
        return permissionLevelEnum != null && permissionLevelEnum.equals(PermissionLevelEnum.CHAPTER_LEVEL); 
    }
    public Boolean getIsArticleLevelPermission(){
    	PermissionLevelEnum permissionLevelEnum = _permissionSummaryCategory.getPermissionLevel();
        return permissionLevelEnum != null && permissionLevelEnum.equals(PermissionLevelEnum.ARTICLE_LEVEL); 
    }
    private RightAdapter getMostPermissablePermission(){
    	Right highestRight = null;
		for (PermissionSummaryTypeOfUse permSumTOU : _permissionSummaryCategory.getTypeOfUseList()){
			if (permSumTOU.getTfRights() != null){
				for (Right right : permSumTOU.getTfRights()) {
	                if (highestRight == null) {
	                    highestRight = right;
	                }
	                else {
	                    if (right.getPermission().compareTo(highestRight.getPermission())<0){
	                        highestRight = right;
	                    }
	                }	
				}
			}
		}
		if (highestRight != null){
			RightAdapter highestRightAdapter = new RightAdapter(_pub,highestRight);
			return highestRightAdapter;
		}
		else {
			return null;
		}	
    }
    
    private RLinkPublisher getPublisher() {
    	//should be the same publisher for each TOU
    	for (PermSummaryTouDisplay permSummaryTouDisplay : _permissionTouDisplays){
        	if (permSummaryTouDisplay.getIntegrates()) {
        		return permSummaryTouDisplay.getPublisher();
        	}
        }
    	return null;
    }
    
    public String getPublisherURL() {
        String url = "";
        RLinkPublisher publisher = getPublisher();
        if (publisher != null) {
            url = publisher.getPubUrl();   
        }
        return url;
    }
    public String getPublisherName() {
        String name = "";
        RLinkPublisher publisher = getPublisher();
        if (publisher != null) {
            name = publisher.getPubName();   
        }
        return name;
    }
    public String getPermissionOptions() {
        String opts = "";
        RLinkPublisher publisher = getPublisher();
        if (publisher != null) {
            opts = publisher.getPermOptionDesc();   
        }
        return opts;
    }
    
    public String getLearnMore() {
        String learn = "";
        RLinkPublisher publisher = getPublisher();
        if (publisher != null) {
            learn = publisher.getLearnMoreDesc();   
        }
        return learn;
    }
	
	/**
	 * This method is used by the jsp to decide which background style to use as
	 * we alternate between light and dark
	 */
    public Boolean getIsOdd() {
        Boolean r = Boolean.FALSE;
        if ((index % 2) != 0) r = Boolean.TRUE;
        return r;
    }

    
    public ProductEnum getProduct() {
    	return  _permissionSummaryCategory.getProduct();
    }
    
    /**
     * Given the List of PermissionSummaryCategory create an order list of PermissionCategoryDisplay 
     * objects for one Publication.  The Permission Categories are meant to be displayed in the order
     * configured in the RightsResolver service
     */
    public static List<PermissionCategoryDisplay>  createOrderedList(Publication pub, List<PermissionSummaryCategory>permSumCategories, Map<Long,String>displayablePermissions){
 
    	if (pub==null) {
    		throw new IllegalArgumentException("createOrderedList called with a null Publication");
    	}
    	if (permSumCategories==null) {
    		throw new IllegalArgumentException("createOrderedList called with a null permSumCategories");
    	}
    	if (displayablePermissions==null) {
    		throw new IllegalArgumentException("createOrderedList called with a null displayablePermissions");
    	}
    	
    	List<PermissionCategoryDisplay> permCatDisplay = new ArrayList<PermissionCategoryDisplay>();
        
        int index=0;
        for(PermissionSummaryCategory permSumCat: permSumCategories) {
        	if (permSumCat==null) {
        		throw new CCCRuntimeException("encountered a null PermissionSummaryCategory while building perm list for display");
        	}
        	if (permSumCat.getCategory()==null) {
        		throw new CCCRuntimeException("PermissionSummaryCategory.getCategory() returned null");        		
        	}
            if (permSumCat.getProduct()==null && !getIsAvailableRightslink(permSumCat)){
            	continue;
            }
            Long catId = permSumCat.getCategoryId();
            String onOff = displayablePermissions.get(catId);
            Boolean contains = displayablePermissions.containsKey(permSumCat.getCategoryId());
            Boolean equals = onOff != null && onOff.equalsIgnoreCase("ON");
            Boolean displayablePermission = contains & equals;
            if ( displayablePermission )
            {
               PermissionCategoryDisplay permissionCategoryDisplay = new PermissionCategoryDisplay(pub,permSumCat,index++);
     		    permCatDisplay.add(permissionCategoryDisplay);  
            }
        
        }
     	return permCatDisplay;
    }
    
    public boolean isLastTou() {
    	return _permissionTouDisplays.size() - 1 == index;
    }
    
    
    //  Private method.
    
    public boolean isGlobalRightsQualifier() {
		return globalRightsQualifier;
	}

	public void setGlobalRightsQualifier(boolean globalRightsQualifier) {
		this.globalRightsQualifier = globalRightsQualifier;
	}

	
	
	private boolean isNull(String s)
    {
        return (s == null || s.equals(""));
    }
	
	public Long getCategoryId() {
		return _permissionSummaryCategory.getCategoryId();
	}
	
	public Boolean hasTfRights()
	{
		return _permissionSummaryCategory.hasTfRights();
	}
	
	public Boolean hasRlRights()
	{	
		return _permissionSummaryCategory.hasRlRights();
	}

	/*
	 * This method indicated the RL has permissions that span beginning
	 * of time to end of time and the UI may decide to not display TF rights
	 * in this category
	 * 
	 */
	public Boolean hasEncompassingRlRights(){
		return _permissionSummaryCategory.hasEncompassingRlRights();
	}
	
    public boolean getHasResponsiveRightDRA(){
    	
    	if (_permissionSummaryCategory.getProduct() == ProductEnum.DRA) {
    		if (_pub.getAnnualLicenseHelper().getHasResponsiveRightDRA()) {
    			return true;
    		}
    	}
    	return false;
    }

    //  Bi-active fix.
    
    public boolean getIsResponsiveRightDRA(String year)
    {
        if (_permissionSummaryCategory.getProduct() == ProductEnum.DRA) 
        {
            if (_pub.getAnnualLicenseHelper().getIsResponsiveRightDRA(year)) 
            {
                return true;
            }
        }
        return false;
    }
}
