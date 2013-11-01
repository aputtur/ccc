package com.copyright.ccc.business.services;

import java.util.List;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.copyright.svc.searchRetrieval.api.SearchRetrievalService;
import com.copyright.svc.searchRetrieval.api.data.SearchRetrievalConsumerContext;
import com.copyright.domain.data.RightExternal;
import com.copyright.ccc.web.util.WebUtils;

/***********************************************************************
 *  A class to quickly surface repertory rights, used in search results.
 *
 *  Author:  mjessop
 *
 *  We want to be able to have a way of surfacing annual license info.
 *  without referring to the rights resolver.  Rights resolver is
 *  awesome, but it does so much that is rather slow and we need to be
 *  able to display the annual license rights as quickly as possible.
 *
 *  SEE WorkRightsAdapter.java
 *  SEE S:\Catalog Consolidation\ConsolidatedIndex\NewIndexStructure.doc
 *
 *  NOTE:  I am only including the public methods that *I* needed to
 *         complete the task at hand - I only needed the permission,
 *         whether or not there were terms, and the terms themselves.
 *
 *  CHANGELOG:
 *  when        who         what
 *  ----------  ----------  --------------------------------------------
 *  2013-10-07  mjessop     Turns out some of our "beginning of time" dates
 *                          are unexpectedly variable.  (not 1000 or 1900).
 *                          Changing BOT check to <= 1900.
 *  2013-08-15  mjessop     Added additional bi-active checks to try and
 *                          appropriately guess when annual licenses
 *                          should be flagged as bi-active based on
 *                          transactional rights.
 *  2013-06-24  mjessop     Another minor refactor to take into account
 *                          bi-active dates for responsive rights.
 *  2013-01-15  mjessop     Added logic to check for public domain.
 *                          Refactored test for responsive rights.
 *  2012-04-09  mjessop     Added logic to check for biactives.
 *                          Added debugging statements.
 */
public class WRAnnualLicenseHelper
{
    private final static int TOU = 0;
    private final static int PERMISSION = 1;
    private final static int RGT_INST = 2;
    private final static int RGH_INST = 3;
    private final static int START_DATE = 4;
    private final static int END_DATE = 5;
    private final static int TERMS_FLAG = 6;
    private final static int RESPONSIVE_RIGHTS_FLAG = 7;
    
    private String aasRight = null;
    private String draRight = null;
    private String arsRight = null;
    private String trsRight = null;
    private boolean isBiactive = false;
    private boolean isPublicDomain = false;
    private boolean hasResponsiveRightDRA = false;
    private ArrayList<String> responsiveRightsStartDates = null;
    private ArrayList<String> responsiveRightsEndDates = null;
    private ArrayList<Boolean> responsiveRightsFlags = null;

    protected static final Logger _logger = Logger.getLogger( WRAnnualLicenseHelper.class );
    
    public WRAnnualLicenseHelper() {}
    public WRAnnualLicenseHelper(List<String> baseRights)
    {
        int aasCount = 0;
        int draCount = 0;
        int arsCount = 0;

        int apsCount = 0;
        int eccCount = 0;
        int trsCount = 0;
        int dpsCount = 0;

        responsiveRightsStartDates = new ArrayList<String>(3);
        responsiveRightsEndDates = new ArrayList<String>(3);
        responsiveRightsFlags = new ArrayList<Boolean>(3);

        //  This list of rights comes from the WorkExternal object and
        //  is generated by the SearchRetrievalService when works are
        //  obtained.
        
        if (baseRights != null && baseRights.size() > 0)
        {
            for (String right : baseRights) 
            {
                if (_logger.isDebugEnabled()) _logger.info("\n" + right);

                if (right.startsWith("AAS")) 
                {
                    aasRight = right;
                    aasCount += 1;
                    continue;
                }
                if (right.startsWith("DRA"))
                { 
                    draRight = right;
                    draCount += 1;

                    //  Since we never mapped the responsive right flag in our rights
                    //  object coming back from the service, we will do it here.

                    responsiveRightsStartDates.add(getField(draRight, START_DATE));
                    responsiveRightsEndDates.add(getField(draRight, END_DATE));

                    if (("Y".equals(getField(draRight, RESPONSIVE_RIGHTS_FLAG))))
                    {
                        //  Check for responsive rights flag.  This flag does
                        //  not occur on every right, but the getField method
                        //  returns an empty string if that is the case.

                        hasResponsiveRightDRA = true;
                        responsiveRightsFlags.add(Boolean.TRUE);
                    }
                    else
                    {
                        responsiveRightsFlags.add(Boolean.FALSE);
                    }
                    continue;
                }
                if (right.startsWith("ARS")) 
                { 
                    arsRight = right;
                    arsCount += 1;
                    continue;
                }

                //  2013-08-15 MSJ
                //  Adding the transactional right counts to help
                //  in the decision to flag bi-active or not.

                if (right.startsWith("APS"))
                {
                    apsCount += 1;
                    continue;
                }
                if (right.startsWith("TRS"))
                {
                    trsCount += 1;
                    continue;
                }
                if (right.startsWith("ECC"))
                {
                    eccCount += 1;
                    continue;
                }
                if (right.startsWith("DPS"))
                {
                    dpsCount += 1;
                    continue;
                }
            }
            if ((aasCount > 1) || (draCount > 1) || (arsCount > 1))
            {
                //  Quick check to see if any of the TOUs has
                //  more than one right associated with it.
                
                isBiactive = true;
            }
            if (!isBiactive && ((apsCount > 1) || (trsCount > 1) || (eccCount > 1) || (dpsCount > 1)))
            {
                //  Check to see if we MIGHT be biactive, based on transactional
                //  counts.  If we are here we need to check to see if we have
                //  limited date ranges on one of our three rights.  If we do then
                //  we flag as biactive, if not, let it stand.

                String startDate = null;
                String endDate = null;

                if (aasRight != null)
                {
                    startDate = getField(aasRight, START_DATE);
                    endDate = getField(aasRight, END_DATE);

                    if (!isDateBeginningOfTimeToEndOfTime(startDate, endDate))
                    {
                        isBiactive = true;
                    }
                }
                if (draRight != null)
                {
                    startDate = getField(draRight, START_DATE);
                    endDate = getField(draRight, END_DATE);

                    if (!isDateBeginningOfTimeToEndOfTime(startDate, endDate))
                    {
                        isBiactive = true;
                    }
                }
                if (arsRight != null)
                {
                    startDate = getField(arsRight, START_DATE);
                    endDate = getField(arsRight, END_DATE);

                    if (!isDateBeginningOfTimeToEndOfTime(startDate, endDate))
                    {
                        isBiactive = true;
                    }
                }
            }
            if (("P".equals(getField(aasRight, PERMISSION))) ||
                ("P".equals(getField(draRight, PERMISSION))) ||
                ("P".equals(getField(arsRight, PERMISSION))))
            {
                //  Theoretically we should be able to pick one and
                //  go with it, instead of testing all three.  But
                //  it IS possible that one of the rights strings might
                //  be null... so we need to test all three.

                isPublicDomain = true;
            }
            if (_logger.isDebugEnabled()) 
            {
                if (isBiactive) {
                    _logger.info( "\nFINAL RIGHTS: BIACTIVE");
                }
                else {
                    _logger.info( "\nFINAL RIGHTS:" +
                        "\n" + aasRight +
                        "\n" + draRight +
                        "\n" + arsRight
                    );
                }
            }
        }
    }

    private RightExternal fetchExtendedRight(String baseRight)
    {
        RightExternal extendedRight = null;
        List<RightExternal> extendedRights;
        List<String> rgtInsts = new ArrayList<String>();
        String[] fields;
        String rgtInst;
        String rghInst;
        
        //  Our base right strings provide us a flag that indicates
        //  whether or not the right has terms and conditions... but
        //  we don't have the actual terms.  So if we end up having a
        //  flag set to "Y" we need to go out and grab the terms.  To
        //  do this we have to make another call to the SearchRetrievalService.
        
        if (baseRight != null) {
            fields = baseRight.split("_");
            rgtInst = fields[2];
            rghInst = fields[3];
            rgtInsts.add(rgtInst);
            
            SearchRetrievalService svc = 
                ServiceLocator.getSearchRetrievalService();
            
            extendedRights = svc.searchRights(
                new SearchRetrievalConsumerContext(),
                rgtInsts, rghInst, 1, 10
            );
            if (extendedRights != null && extendedRights.size() > 0) {
                extendedRight = extendedRights.get(0);
            }
        }
        return extendedRight;
    }
    
    private String getField(String right, int pos) 
    {
        String[] fields;
        String field = "";
        
        if (right != null) {
            fields = right.split("_");
            if (fields.length > pos) {
                //  This test was added by Bill M. because a new
                //  field was added, but doesn't necessarily exist
                //  for every permission row.

                field = fields[pos];
            }
        }
        return field;
    }
    
    //  ********** PUBLIC METHODS **********
    
    public boolean getIsGrantedAAS() 
    {
        return getField(aasRight, PERMISSION).equals("G");
    }
    
    public boolean getIsGrantedDRA() 
    {
        return getField(draRight, PERMISSION).equals("G");
    }
    
    public boolean getIsGrantedARS() 
    {
        return getField(arsRight, PERMISSION).equals("G");
    }
    
    public boolean getHasTermsAAS() 
    {
        return getField(aasRight, TERMS_FLAG).equals("Y");
    }
    
    public boolean getHasTermsDRA() 
    {
        return getField(draRight, TERMS_FLAG).equals("Y");
    }
    
    public boolean getHasTermsARS() 
    {
        return getField(arsRight, TERMS_FLAG).equals("Y");
    }
    
    public String getTermsAAS() 
    {
        String terms = null;
        String rqs = null;

        RightExternal extendedRight = fetchExtendedRight(aasRight);
    
        if (extendedRight != null) {
            terms = extendedRight.getTerms();
            rqs = extendedRight.getQualifier();

            if (terms != null) {
                terms = terms
                    .replaceAll("[^\\p{ASCII}]", "?")
                    .replaceAll("\n", "<br/>")
                    .replaceAll("\r", "");
            }

            if (rqs != null) {
                rqs = rqs
                    .replaceAll("[^\\p{ASCII}]", "?")
                    .replaceAll("\n", "<br/>")
                    .replaceAll("\r", "");

                if (terms == null || terms.equals(rqs)) {
                    terms = rqs;
                }
                else {
                    terms = rqs + "<br/><br/>" + terms;
                }
            }
        }
        return terms;
    }
    
    public String getTermsDRA() 
    {
        String terms = null;
        String rqs = null;

        RightExternal extendedRight = fetchExtendedRight(draRight);
    
        if (extendedRight != null) {
            terms = extendedRight.getTerms();
            rqs = extendedRight.getQualifier();

            if (terms != null) {
                terms = terms
                    .replaceAll("[^\\p{ASCII}]", "?")
                    .replaceAll("\n", "<br/>")
                    .replaceAll("\r", "");
            }

            if (rqs != null) {
                rqs = rqs
                    .replaceAll("[^\\p{ASCII}]", "?")
                    .replaceAll("\n", "<br/>")
                    .replaceAll("\r", "");

                if (terms == null || terms.equals(rqs)) {
                    terms = rqs;
                }
                else {
                    terms = rqs + "<br/><br/>" + terms;
                }
            }
        }
        return terms;
    }
    
    public String getTermsARS() 
    {
        String terms = null;
        String rqs = null;

        RightExternal extendedRight = fetchExtendedRight(arsRight);
    
        if (extendedRight != null) {
            terms = extendedRight.getTerms();
            rqs = extendedRight.getQualifier();

            if (terms != null) {
                terms = terms
                    .replaceAll("[^\\p{ASCII}]", "?")
                    .replaceAll("\n", "<br/>")
                    .replaceAll("\r", "");
            }

            if (rqs != null) {
                rqs = rqs
                    .replaceAll("[^\\p{ASCII}]", "?")
                    .replaceAll("\n", "<br/>")
                    .replaceAll("\r", "");

                if (terms == null || terms.equals(rqs)) {
                    terms = rqs;
                }
                else {
                    terms = rqs + "<br/><br/>" + terms;
                }
            }
        }
        return terms;
    }

    public boolean getIsBiactive()            { return isBiactive;            }
    public boolean getHasResponsiveRightDRA() { return hasResponsiveRightDRA; }
    public boolean getIsPublicDomain()        { return isPublicDomain;        }

    //  MSJ Adding this to account for bi-active effect on
    //  responsive rights.  In reality the responsive rights flag
    //  should be added to the Right.java class but... that would require
    //  changing maps, services, etc.  This seems a more expedient solution
    //  that also affects no consumers of the rights service.

    public boolean getIsResponsiveRightDRA(String year)
    {
        Boolean response = Boolean.FALSE;

        int x = Integer.valueOf(year);
        int startYear = 0;
        int endYear = 0;

        for (int i = 0; i < responsiveRightsFlags.size(); i++)
        {
            startYear = Integer.valueOf(responsiveRightsStartDates.get(i).substring(0, 4));
            endYear = Integer.valueOf(responsiveRightsEndDates.get(i).substring(0, 4));

            if (_logger.isDebugEnabled())
            {
                _logger.info(
                    "\nWRAnnualLicenseHelper: getIsResponsiveRightDRA" +
                    "\n    Iteration : " + String.valueOf(i) +
                    "\n    For Year  : " + String.valueOf(x) +
                    "\n    Start Year: " + String.valueOf(startYear) +
                    "\n    End Year  : " + String.valueOf(endYear) + 
                    "\n    FLAG      : " + responsiveRightsFlags.get(i).toString() + "\n"
                );
            }

            if (x >= startYear && x <= endYear)
            {
                response = responsiveRightsFlags.get(i);
            }
        }
        return response.booleanValue();
    }

    private boolean isDateBeginningOfTimeToEndOfTime(String startDate, String endDate)
    {
        //  Assume failure.  This will only be called if we have a right
        //  to begin with because only valid rights will be considered
        //  in determining whether or not we are bi-active.

        boolean retval = false;

        if (startDate.length() > 4 && endDate.length() > 4)
        {
            String startYear = startDate.substring(0, 4);
            String endYear = endDate.substring(0, 4);
            int bot;
            int eot;

            try 
            {
                bot = Integer.parseInt(startYear);
                eot = Integer.parseInt(endYear);
            }
            catch (NumberFormatException nfe)
            {
                //  Set beginning of time to some ridiculous value.

                bot = 3000;
                eot = 0;
            }

            if ((bot < 1901) && (eot == 3000))
            {
                retval = true;
            }
        }
        return retval;
    }
}

//  EXAMPLE OF RIGHT STRING:
//
//  01234567890123456789012345678901234567890
//  12345678901234567890123456789012345678901
//  
//  AAS_G_385815_181228_19920101_30001231_N_N
//  PROD_PERM_RGT_RGH_START_END_TERMS_RRDRA
//
//  Product AAS
//  Permission G[ranted]
//  Rgt_Inst 385815
//  Rgh_Inst 181228
//  Valid Start Date 01/01/1992
//  Valid End Date 12/31/3000