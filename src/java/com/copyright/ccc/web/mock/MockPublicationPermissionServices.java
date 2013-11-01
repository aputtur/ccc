package com.copyright.ccc.web.mock;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.copyright.ccc.business.data.CCSearchCriteria;
import com.copyright.ccc.business.data.Publication;
import com.copyright.ccc.business.data.PublicationPermission;
import com.copyright.ccc.business.data.PublicationPermissionSearchResult;
import com.copyright.ccc.business.security.CCUserContext;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.search.SearchCriteriaImpl;
import com.copyright.ccc.business.services.search.WebServiceSearch;
import com.copyright.data.inventory.UsageDescriptor;

public class MockPublicationPermissionServices
{
	private static final Logger LOGGER = Logger.getLogger( MockPublicationPermissionServices.class );
    static final String PHOTOCOPY_STD_NUM  = "10500545"; //solee if num copies >= 26
    static final String APS_STD_NUM = "41009633";
    static final String APS_STD_NUM_SPEC_ORDER_LEE = "9780871714664"; //year 1990, num pages = 1
    static final String APS_STD_NUM_SPEC_ORDER = "64017858"; //aps special order
    static final String DIGITAL_STD_NUM_NET_DENIED = "82643334"; //email is good, net is denied lee
    static final String DIGITAL_STD_NUM_SO = "00030953"; //email is good, net is unvailable so
    static final String DIGITAL_STD_NUM_CRH = "59007620"; //email is good, extranet or internet is contact rh directly, not sure about intranet
    static final String NET_STD_NUM = "59007620"; //solee if extranet && duration >= 3? i don't think this works
    static final String REPUB_BROCHURE = "american indians and the law";
    
    
    static final long INTRANET_TPU_CODE = 204;
    static final long EXTRANET_TPU_CODE = 134;
    static final long INTERNET_TPU_CODE = 203;
    
    
    public static PublicationPermission createPublicationPermissionForUnitTest(String typeOfUse, String searchString, boolean isSpecialOrder)
    {
        boolean hasOriginalCtx = true;
        CCUserContext originalCtx = null;
        try
        {
            originalCtx = (CCUserContext)UserContextService.getCurrentAsInterface();
        }
        catch(Exception e)
        {
            hasOriginalCtx = false;
        }
        
        
        CCUserContext ctx =  UserContextService.createUserContext(null, null);
        UserContextService.setCurrent(ctx);
        
        PublicationPermission publicationPermission = 
            MockPublicationPermissionServices.createPublicationPermission(typeOfUse,searchString,isSpecialOrder);
        
        if(hasOriginalCtx) 
            UserContextService.setCurrent(originalCtx);
            
        return publicationPermission;
    }

    public static PublicationPermission createPublicationPermission(String typeOfUse, String searchString, boolean isSpecialOrder)
    {
        if(StringUtils.isEmpty(searchString))
        {
            if(typeOfUse.equals("trs")) searchString = PHOTOCOPY_STD_NUM;
            else if(typeOfUse.equals("email")) searchString = NET_STD_NUM;
            else if(typeOfUse.equals("aps")) searchString = APS_STD_NUM;
            else if(typeOfUse.equals("republication")) searchString = REPUB_BROCHURE;
        }

        LOGGER.info("Creating Publication Permission " );
        
        
        try
        {
            WebServiceSearch searchServices = new WebServiceSearch();
            //searchServices.performBasicSearch(searchString);
            CCSearchCriteria ccSearchCriteria = new SearchCriteriaImpl();
            ccSearchCriteria.setTitleOrStdNo(searchString);
            ccSearchCriteria.setSearchType(CCSearchCriteria.ADVANCED_SEARCH_TYPE);
            ccSearchCriteria.setSortType(CCSearchCriteria.RELEVANCE_SORT);
            LOGGER.info("Searching for String " + searchString);
            
            searchServices.performAdvancedSearch(ccSearchCriteria, 25);
            //performBasicSearch(ccSearchCriteria);


            PublicationPermissionSearchResult result = UserContextService.getSearchState().getSearchResults();

            LOGGER.info("Returned Results: " + result.getNumResultsReturned());

            Publication[] publications = result.getResultSet();
                 	
                       
            if(publications == null || publications.length == 0)
                return null;
            
            LOGGER.info("Publications in Array " + publications.length)  ;
            
            for (Publication pub : publications) {
            	
            	LOGGER.info("Publication Found " + pub.getMainTitle())  ;         	
                LOGGER.info("Publication TF Wrk Inst " + pub.getTFWrkInst())  ;         	

                try {
                  LOGGER.info("Permissions Found " + pub.getAdaptedRights().size())  ;         	             	
//                  PublicationPermission[] publicationPermissions = pub.getPublicationPermissions(UsageDescriptor.ACADEMIC_TRX_PHOTOCOPY);
                  PublicationPermission[] publicationPermissions = pub.getPublicationPermissionsByMostPermissable(UsageDescriptor.ACADEMIC_TRX_PHOTOCOPY);
                  if (publicationPermissions.length > 0 ) {
                      LOGGER.info("APS Permission Found " + publicationPermissions[0].getRgtInst()) ; 
                      return publicationPermissions[0];
                  }
                  LOGGER.info("Permissions Found " + pub.getAdaptedRights().size())  ;         	             	
                } catch (Exception e) {
                  LOGGER.info("No Permissions Found " + pub.getMainTitle())  ;         	             	
                	
                }
//                PublicationPermission[] publicationPermissions = pub.getPublicationPermissions(UsageDescriptor.ACADEMIC_TRX_PHOTOCOPY);
//                LOGGER.info("APS Permissions Found " + publicationPermissions.length)  ;         	
               
            }
            Publication publication = publications[0];

            LOGGER.info("Publication Found " + publication.getMainTitle());

            
            PublicationPermission[] publicationPermissions = null;
            
            if(typeOfUse.equals("trs"))
            {
                publicationPermissions = publication.getPublicationPermissions(UsageDescriptor.NON_ACADEMIC_TRX_PHOTOCOPY);
                if(publicationPermissions == null || publicationPermissions.length == 0)
                    publicationPermissions = publication.getPublicationPermissions(UsageDescriptor.NON_ACADEMIC_LICENSE_PHOTOCOPY);
            }
            else if(typeOfUse.equals("email"))
            {
                publicationPermissions = publication.getPublicationPermissions(UsageDescriptor.NON_ACADEMIC_TRX_EMAIL);
                if(publicationPermissions == null || publicationPermissions.length == 0)
                    publicationPermissions = publication.getPublicationPermissions(UsageDescriptor.NON_ACADEMIC_LICENSE_EMAIL);
            }
            else if(typeOfUse.equals("aps"))
            {
                publicationPermissions = publication.getPublicationPermissions(UsageDescriptor.ACADEMIC_TRX_PHOTOCOPY);
            }
            else if(typeOfUse.equals("republication"))
            {
                publicationPermissions = publication.getPublicationPermissions(UsageDescriptor.NON_ACADEMIC_TRX_REPUB_BROCHURE);
            }
            
            if(publicationPermissions == null || publicationPermissions.length == 0)
                return null;
            
            return publicationPermissions[0];
            
            //return new MockPublicationPermissionImpl(publication, publicationPermissions[0], (int)publicationPermissions[0].getTpuInst());
            
        }
        catch(Exception e)
        {
            LOGGER.info( ExceptionUtils.getFullStackTrace( e ) );         	             	
            return null;
        }
    }
    
    public static void main(String[] args)
    {
        PublicationPermission publicationPermission = createPublicationPermissionForUnitTest("trs", PHOTOCOPY_STD_NUM, false);
        LOGGER.info(publicationPermission.toString());
    }
}
