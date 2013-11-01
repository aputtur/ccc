package com.copyright.ccc.web.util;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.copyright.base.enums.ProductEnum;
import com.copyright.domain.data.WorkTagExternal;
import com.copyright.svc.rightsResolver.api.data.PermissionSummaryCategory;
import com.copyright.svc.rightsResolver.api.data.PermissionSummaryTypeOfUse;
import com.copyright.svc.rightsResolver.api.data.Right;
import com.copyright.svc.rightsResolver.api.data.SourceCodeEnum;

public class PermSummaryUtil {
	private final static  long REPRINTS_CATEGORY_ID=7;
	
	
	public static boolean hasRightsLinkRights(PermissionSummaryCategory permSumCat ){
		
		if(permSumCat.hasEncompassingRlRights()){
     		return true;
     	 }
     	 else{
         return !permSumCat.hasTfRights() && permSumCat.hasRlRights();
     	}
		
	}
	
	
	public static Boolean isRepublication(ProductEnum productEnum) {
		return productEnum != null && (productEnum.equals(ProductEnum.RLS));
	}
	public static Boolean isDigital(ProductEnum productEnum) {
		return productEnum != null && productEnum.equals(ProductEnum.DPS);
	}
	
	public static Boolean isReprints(Long categoryId) {
		return categoryId.intValue()==REPRINTS_CATEGORY_ID;
	}
	
	
	public static List<PermissionSummaryCategory> filterRights(List<PermissionSummaryCategory> permissionSummaryCategories,String strYYYY) {
    	boolean checkForRLRightsOnly=false;
    	List<PermissionSummaryTypeOfUse> permSumTOUs=null;
    	boolean skipDateCheck=false;
    	Date beginYear =null;
    	Date endYear =null ;
        // IF permCatDisplay.hasEncompassingRlRights() then render everything that is RL
        //if ( hasRLRights and hasTFRights & NO ncompassingRlRights
        // check for PUB Date if first one if RL rights then ignore all  TF  for lookup vice versa
        // if pubdate is null then if only RL  else TF right
        //if pubdate is not null then if only RL check for RL else check for TF if
    	if(StringUtils.isEmpty(strYYYY)){
    		skipDateCheck=true;
    	}else{
    		beginYear =WebUtils.getBeginningOfTime(Integer.valueOf(strYYYY));
    		endYear  = WebUtils.getEndOfTime(Integer.valueOf(strYYYY));
    	}
        for (PermissionSummaryCategory permSumCat :permissionSummaryCategories)
        {
        	checkForRLRightsOnly=false;
        	
        	if(isRepublication(permSumCat.getProduct())  ||  isDigital(permSumCat.getProduct()) || isReprints(permSumCat.getCategoryId()) ){
        		// only then Check for Rgihts Link
        		checkForRLRightsOnly=hasRightsLinkRights(permSumCat);
	         	  
	        	permSumTOUs = permSumCat.getTypeOfUseList();
	        	if(!skipDateCheck){
		        	for (int index=permSumTOUs.size()-1; index>=0;index--){
		        		PermissionSummaryTypeOfUse permTOU = permSumTOUs.get(index);
			        		if (permTOU.getSourceCode().equals(SourceCodeEnum.RL)){
			        			//check if this TOU is outside the request year (1/1/YYYY - 12/31/YYYY)
			        			
			        			if ((endYear.compareTo(permTOU.getRlBiactiveStartDate())<0) ||(beginYear.compareTo(permTOU.getRlBiactiveEndDate())>0 )){
			        				permSumTOUs.remove(index);
			        			}else{
			        				checkForRLRightsOnly=true;
			        			}
			        		}
		        		 
		        	}
	        	}
        	}
		       // now check for TF rights if not found in RL
		        	if(checkForRLRightsOnly==false){
		        		permSumTOUs = permSumCat.getTypeOfUseList();
			        	for (int index=permSumTOUs.size()-1; index>=0;index--)
			        	{
			        		PermissionSummaryTypeOfUse permTFTOU = permSumTOUs.get(index);
			    				List<Right> tfRights = permTFTOU.getTfRights();
			    				
			    				if (tfRights !=null) {
			    			
				    				for (int rightIndex = tfRights.size()-1; rightIndex>=0; rightIndex--){
				    					Right tfRight = tfRights.get(rightIndex);
				    					
				    					/*if(isRepublication(tfRight.getProduct()) && tfRight.getLicenseeClass().equals(LicenseeClassEnum.MATERIAL_TO_BE_DISTRIBUTED_FOR_PROFIT)){
				    						 tfRights.remove(rightIndex);
				    						 continue;
				    					}*/
					    					if(!skipDateCheck && tfRight.getIsBiactiveRight()){
						    					//Check if the right is outside the requested year
						            			if ((endYear.compareTo(tfRight.getPubBegDtm())<0) ||(beginYear.compareTo(tfRight.getPubEndDtm())>0 )){
						            				   tfRights.remove(rightIndex);
						                		}
					    					}
				    				}
			    				}
			    				
			    				if (tfRights !=null && isRepublication(permSumCat.getProduct())) {
		    							removeLeastPermissibleRights(tfRights);
		    						}	
			    				
			        	}	
		
		        	}
        	
		        	if(checkForRLRightsOnly){// remove all TF rights if checking RL rights
	        			for (int index=permSumTOUs.size()-1; index>=0;index--){
		    	        	PermissionSummaryTypeOfUse permRemoveTFTOU = permSumTOUs.get(index);
		    	        	if(permRemoveTFTOU.getRlOfferId()==null){
		    	        		permSumTOUs.remove(index);
		    	        	}
	        			}
        	     	}
    				
        }
        return permissionSummaryCategories;
    }
	/**
	 * 
	 */
	 public static boolean checkIfBiactive (List<WorkTagExternal> rrTagList, List<PermissionSummaryCategory> lstPermSumCategories) {
    	
	    	if(rrTagList.size()>1L){
	    		return true;
	    	}
	    	// multiple tf rights return true;
	    	for (PermissionSummaryCategory permSumCat : lstPermSumCategories) {
	    		if(permSumCat.hasTfRights() && permSumCat.hasRlRights() &&  !permSumCat.hasEncompassingRlRights()){
	    			return true;
	    		}
	    		for (PermissionSummaryTypeOfUse permSummaryTypeOfUse :  permSumCat.getTypeOfUseList()) {
	    			if(permSummaryTypeOfUse.getTfRights()!=null){
	    				for(Right tfRight :permSummaryTypeOfUse.getTfRights()){
		    				if (tfRight !=null) {
			    					if(tfRight.getIsBiactiveRight()){
				    				return true;
			    					}
		    					}
		    			}
	    			}
	    		}

	    }
	    	return false;
	 }
	 public static void removeLeastPermissibleRights(List<Right> tfRights){
		 Right leastPermissible=null;
		 int leastPermissibleIndex=0;
		 for (int rightIndex = tfRights.size()-1; rightIndex>=0; rightIndex--){
				  if (leastPermissible == null) {
					  leastPermissible = tfRights.get(rightIndex);
					  leastPermissibleIndex=rightIndex;
				  }
		           else {
			             if (tfRights.get(rightIndex).getPermission().compareTo(leastPermissible.getPermission())>0){
			            	   tfRights.remove(rightIndex);
			            	   break;
			                  }
			             else{
			            	 tfRights.remove(leastPermissibleIndex);
			            	 break;
			             }
		               }
		          }
	 }
	       
}