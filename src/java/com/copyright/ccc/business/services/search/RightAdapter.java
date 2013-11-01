/*
 * RightAdapter.java
 * Copyright (c) 2008, Copyright Clearance Center, Inc. All rights reserved.
 * ----------------------------------------------------------------------------
 * Revision History
 * 2008-09-25   wmurphy    Created.
 * ----------------------------------------------------------------------------
 */
package com.copyright.ccc.business.services.search;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.copyright.base.CCCRuntimeException;
import com.copyright.base.enums.PermTypeEnum;
import com.copyright.base.enums.ProductEnum;
import com.copyright.base.enums.TypeOfUseEnum;
import com.copyright.ccc.business.data.Publication;
import com.copyright.ccc.business.data.PublicationPermission;
import com.copyright.ccc.web.util.PubDateDisplayUtil;
import com.copyright.data.inventory.UsageDescriptor;
import com.copyright.service.inventory.RightServiceFactory;
import com.copyright.svc.rightsResolver.api.data.PermissionSummaryCategory;
import com.copyright.svc.rightsResolver.api.data.Right;
import com.copyright.svc.rightsResolver.api.data.RightFee;

/**
 * An implementation of the PublicationPermission. Represents
 * a single Permission for a single Work (Publication). This
 * object is backed by a com.copyright.svc.rightsResolver.api.data.Right.
 * 
 * @author jarbo
 *
 */
public class RightAdapter implements PublicationPermission{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PermissionSummaryCategory permissionSummaryCategory;
    private Right rrRight = null;
    String _availability = "";
    Publication pub = null;
    String _typeofuse = "";
    boolean isRRRightAdapter = false;

    
    public PermissionSummaryCategory getPermissionSummaryCategory() {
		return permissionSummaryCategory;
	}
	public void setPermissionSummaryCategory(
			PermissionSummaryCategory permissionSummaryCategory) {
		this.permissionSummaryCategory = permissionSummaryCategory;
	}
	public RightAdapter() {}
	
	public RightAdapter(Publication pub,Right rrRight, PermissionSummaryCategory category) {
        this.rrRight = rrRight;
        this.pub = pub;
        this.permissionSummaryCategory = category;
        setAvailability(rrRight);
        if (rrRight != null) {
        	_typeofuse = rrRight.getTypeOfUse().getDesc();
        }
        isRRRightAdapter = true;
	}
	public RightAdapter(Publication pub,Right rrRight) {
        this.rrRight = rrRight;
        this.pub = pub;
        setAvailability(rrRight);
        _typeofuse = rrRight.getTypeOfUse().getDesc();
        isRRRightAdapter = true;
        /*if (getUsageDescriptor() == null) {
            _typeofuse = "";
        }
        else {
            _typeofuse = getUsageDescriptor().getDescription();
        }*/
    }
    public String getTypeOfUse(){
        return _typeofuse;
    }

    public String getAvailability(){
        return _availability;
    }

    public String getRHTerms(){
		return rrRight.getExternalComments();
    }

    public long getRgtInst(){
		return rrRight.getRightId();
    }

    public long getRightsholderInst(){
    	return rrRight.getRightsholderId();
    }
    
    public long getTpuInst(){
		return rrRight.getTypeOfUse().getId();
    }

    public long getPrdInst(){
		return rrRight.getProduct().getId();
    }
    
    public UsageDescriptor getUsageDescriptor(){
        UsageDescriptor usageDescriptor;
        try {
         usageDescriptor =
          RightServiceFactory.getInstance().getService().GetUsageDescriptor( (long) rrRight.getProduct().getId(), (long) rrRight.getTypeOfUse().getId());
        }
        catch (CCCRuntimeException cccRuntimeException) {
           //protect against when combination of TPU and Product ID is invalid which causes exception to be thrown
           return null;
        }
         //ProductUsePair prdUsePair = ProductUsePair.getInstanceOf(right.getProduct().getId(),   right.getTypeOfUse().getId());
         //UsageDescriptor usageDescriptor = UsageDescriptorMapper.getInstance().getUsageDescriptorFor(prdUsePair);
         return usageDescriptor;
    }
    
    public String getRightsholderName(){
		return rrRight.getRightsholderName();
    }
    
    public String getRightsQualifyingStatement(){
		return rrRight.getQualifyingStmt();
    }
    
    public Publication getPublication(){
        return pub;
    }
    
    /**
     * Returns the publication start date for which this
     * Permission applies. Returns the current date if
     * the permissions actual pub start date cannot be
     * determined.
     */
    public Date getPubBeginDate(){
    	if (rrRight == null || rrRight.getPubBegDtm() == null) {
    		return new Date();
    	}
		return rrRight.getPubBegDtm();
    }
    
    /**
     * Returns the publication end date for which this
     * Permission applies. Returns the current date if
     * the permissions actual pub end date cannot be
     * determined.
     */
    public Date getPubEndDate(){
    	if (rrRight == null || rrRight.getPubEndDtm() == null) {
    		return new Date();
    	}
		return rrRight.getPubEndDtm();
    }
    
    public String getPermDateRange(){
        Calendar calendar = Calendar.getInstance();
        
        try {
        calendar.setTime(getPubBeginDate());
        Date beginDate = calendar.getTime();
                
        calendar.setTime(getPubEndDate());
        Date endDate = calendar.getTime();
        
        return PubDateDisplayUtil.computeYearRangeDisplay(beginDate, endDate);
        }
        catch (Exception e){
            return "";
        }
    }
    
    public Boolean getIsAcademic(){
       if (rrRight.getProduct().compareTo(ProductEnum.APS) == 0 ||
           rrRight.getProduct().compareTo(ProductEnum.ARS) == 0 ||
           rrRight.getProduct().compareTo(ProductEnum.ECC) == 0){
               return true;
           }
       else {
           return false;
       }
    }

    public boolean isSpecialOrder(){
        //returns true if permission code != G, I, H, or D

        return rrRight.getPermission() != PermTypeEnum.GRANT &&
        rrRight.getPermission() != PermTypeEnum.CONTACT_RIGHTSHOLDER_DIRECTLY &&
        rrRight.getPermission() != PermTypeEnum.DENY &&
        rrRight.getPermission() != PermTypeEnum.HOLD_PENDING;    		
    }
    
    public boolean isContactRHDirectly(){
    	return rrRight.getPermission()== PermTypeEnum.CONTACT_RIGHTSHOLDER_DIRECTLY;
    }
    
    public boolean isDenied(){
		return rrRight.getPermission()== PermTypeEnum.DENY || rrRight.getPermission()== PermTypeEnum.HOLD_PENDING;    		
    }
    
    public boolean isAvailable(){
		return rrRight.getPermission()== PermTypeEnum.GRANT;
    }
    
    public boolean getIsNotAvailable() {
    	String s = getAvailability();
        Boolean r = Boolean.FALSE;
        if (s == null || s.startsWith("N")) r = Boolean.TRUE;
        return r;
    }
    
    public Boolean getIsBiactiveRight() {
	   return rrRight.getIsBiactiveRight();
    }
    
    public int getLicenseeClass(){
		return rrRight.getLicenseeClass().getId();
    }
    
    /**
     * Given a Rightsresolver Right object, this method computes the "availability" 
     * text that will display on the permission summary
     * for the supplied Right eg. "Available for purchase, Not Available, etc.."
     * 
     * @param right
     */
    private void setAvailability(Right right) {
    	if (right == null) {
            _availability = "Available for Special Order";    		
    	} else if (right.getProduct() == ProductEnum.DPS || right.getProduct() == ProductEnum.RLS){
    		switch(right.getPermission()) {
    		case DENY:
    		case HOLD_PENDING:
                _availability = "Not available"; break;
    		case GRANT:
                _availability = "Available for Purchase"; break;
    		case CONTACT_RIGHTSHOLDER_DIRECTLY:
                _availability = "Contact rightsholder directly"; break;
    		case PUBLIC_DOMAIN:
            	_availability = "Public Domain"; break;
    		default:
                _availability = "Available for Special Order"; break;    			    			
    		}
        }
        else if (right.getPermission()==PermTypeEnum.GRANT) {
        	switch (right.getProduct()) {
        	case AAS:
        	case DRA:
                _availability = "Covered by CCC Annual License - Business"; break;
        	case ARS:
                _availability = "Covered by CCC Annual License - Academic"; break;
            default:
                _availability = "Available for Purchase"; break;            	        		        		
        	}
        }
        else if (right.getPermission()==PermTypeEnum.CONTACT_RIGHTSHOLDER_DIRECTLY) {
            _availability = "Contact rightsholder directly";
        } 
        else if (right.getPermission()==PermTypeEnum.DENY ||
                    right.getPermission()==PermTypeEnum.HOLD_PENDING) {
        	switch (right.getProduct()) {
        	case AAS:
        	case DRA:
        	case ARS:
                _availability = "Not covered"; break;
            default:
                _availability = "Not available"; break;            	
        	}
        }
        else if (right.getPermission()==PermTypeEnum.PUBLIC_DOMAIN) {
            _availability = "Public Domain";
        }
        else {
            _availability = "Available for Special Order";
        }
    }
    
    public Right getRRRight() {
    	return rrRight;
    }
    
    public void setRRRight(Right rrRight) {
    	this.rrRight = rrRight;
    }
    
    public boolean getIsRRRightAdapter() {
    	return isRRRightAdapter;
    }
    
    public PermTypeEnum getPermission() {
    	return rrRight.getPermission();
    }
    
    public String getExternalComments() {
    	return rrRight.getExternalComments();
    }
    
    public boolean getHasVolumePrice() {
    	return rrRight.getHasVolumePrice();
    }
    
    public String getPubBegDtm(){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    	return sdf.format( rrRight.getPubBegDtm() );
    }
    
    public String getPubEndDtm(){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    	return sdf.format( rrRight.getPubBegDtm() );
    }
    
    public String getQualifyingStmt(){
    	return rrRight.getQualifyingStmt(); 
    }
    public List<RightFeeAdapter>getRightFees() {
    	List<RightFeeAdapter>rightFees = new ArrayList<RightFeeAdapter>();
		for (RightFee rrrightFee : rrRight.getRightFees()){
			RightFeeAdapter rightFeeAdapter = new RightFeeAdapter(rrrightFee);
			rightFees.add(rightFeeAdapter);
		}   		

    	return rightFees;
    }
    public List<String>getRightFeeNames() {
    	List<String>feeNames = new ArrayList<String>();
		for (RightFee rightFee : rrRight.getRightFees()){
			feeNames.add(rightFee.getFeeName());
		}

    	return feeNames;
    }
    
    public Long getRightsholderId(){
    	return rrRight.getRightsholderId(); 
    }
    
    public void setRightsholderId(Long rightsholderInst){
    	rrRight.setRightsholderId(rightsholderInst); 

    }
    
    public RightAdapter getRightAdapter() {
    	return this;
    }
    
    public ProductEnum getProduct() {
    	return rrRight.getProduct(); 
    }
    
    public TypeOfUseEnum getTypeOfUseEnum() {
    	return rrRight.getTypeOfUse(); 
    }

}
