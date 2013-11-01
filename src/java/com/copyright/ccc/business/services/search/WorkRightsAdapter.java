package com.copyright.ccc.business.services.search;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.copyright.base.CCCRuntimeException;
import com.copyright.base.enums.LicenseeClassEnum;
import com.copyright.base.enums.PermTypeEnum;
import com.copyright.base.enums.ProductEnum;
import com.copyright.ccc.business.data.Publication;
import com.copyright.ccc.business.data.PublicationPermission;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.business.services.SystemStatus;
import com.copyright.ccc.business.services.WRAnnualLicenseHelper;
import com.copyright.ccc.config.ApplicationResources;
import com.copyright.ccc.web.util.FauxPublicationPermission;
import com.copyright.ccc.web.util.PermSummaryUtil;
import com.copyright.ccc.web.util.PubDateDisplayUtil;
import com.copyright.ccc.web.util.WebUtils;
import com.copyright.data.inventory.UsageDescriptor;
import com.copyright.domain.data.WorkExternal;
import com.copyright.domain.data.WorkTagExternal;
import com.copyright.service.inventory.RightServiceFactory;
import com.copyright.svc.rightsResolver.api.RightsResolverService;
import com.copyright.svc.rightsResolver.api.data.PermissionSummaryCategory;
import com.copyright.svc.rightsResolver.api.data.PermissionSummaryRollup;
import com.copyright.svc.rightsResolver.api.data.PermissionSummaryTypeOfUse;
import com.copyright.svc.rightsResolver.api.data.Right;
import com.copyright.svc.rightsResolver.api.data.RightsResolverConsumerContext;


/**
 * Retrieves and maps works and rights data.
 *
 * This class is used to pull back works (usually in conjunction
 * with a search) and map the data contained in the Work.java
 * service class with something local to the business side of
 * things (UI).
 *
 * Change Log   --   Please list most recent entries on TOP.
 * ----------------------------------------------------------
 * when         who         what
 * ----------   ----------- -------------------------------------
 * 2012-05-30   mjessop     Cleaned up code a bit, made var out of helper.
 * 2011-07-12   mjessop     Added getItemURL for search+2 proj.
 * 2009-09-17   mjessop     Mapped to new fields for summit proj.
 *                          first sprint.
 */
public class WorkRightsAdapter implements Publication {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private WorkExternal work = null;
    private WRAnnualLicenseHelper helper = null;

    /*
     * rights resolver rights
     */
    List<Right> rrrights = null;
    List<RightAdapter> adaptedRights = new ArrayList<RightAdapter>();
    List<PermissionSummaryCategory> permCategories;

    protected static final Logger _logger = Logger.getLogger(
            WorkRightsAdapter.class);

    static 
    {
        try {
            SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MMM-yyyy");
					
            DATE_FORMAT.parse("01-JAN-1000");
            DATE_FORMAT.parse("02-JAN-2000");

            DATE_FORMAT.parse("01-JAN-2000");
            DATE_FORMAT.parse("31-DEC-3000");
            Calendar.getInstance().getTime();
        } catch (ParseException e) {
            throw new CCCRuntimeException(e);
        }
    }

    /**
     * Returns a "default" PublicationPermission for the PermissionSummaryCategory
     * provided. The default is just the first PublicationPermission found within the
     * category.
     * @param cat the PermissionSummaryCategory for which you want the default PublicationPermission
     * @return the default PublicationPermission
     */
    public PublicationPermission getDefaultPermission(PermissionSummaryCategory cat) 
    {
        if (getAdaptedRights() == null) {
            return null;
        }
        for (RightAdapter ra: getAdaptedRights()) {
            if (ra.getPermissionSummaryCategory() == cat) {
                return ra;
            }
        }
        return null;
    }
		
    public WorkRightsAdapter(WorkExternal work) 
    {
        this.work = work;
    }
		
    public WorkRightsAdapter(WorkExternal work, boolean getRights) 
    {
        this(work);

        if (getRights) {
            setRRRights();
        }
    }

    public WorkExternal getWork() {
        return work;
    }

    public void setWork(WorkExternal work) {
        this.work = work;
    }

    public void setRRRights() {
		
        RightsResolverService rrService = ServiceLocator.getRightsResolverService();
        boolean rightsLinkDown = !SystemStatus.isRightslinkUp();

        if (rightsLinkDown) {
            _logger.error(
                    "Rightslink Status: Down - Rendering Permission Summary page.");
        }

        PermissionSummaryRollup permissionSummaryRollup = rrService.getPermissionSummaryRollupCheckRlDown(
                new RightsResolverConsumerContext(), work, rightsLinkDown);
			
        if (_logger.isTraceEnabled()) {
            _logger.trace(permissionSummaryRollup);
        }			 

        permCategories = permissionSummaryRollup.getCategoryList();

        if (permCategories.size() > 0) {
            rrrights = new ArrayList<Right>();
            for (PermissionSummaryCategory permissionSummaryCategory: permCategories) {
                for (PermissionSummaryTypeOfUse permSumTou : permissionSummaryCategory.getTypeOfUseList()) {
                    if (permSumTou.getTfRights() != null) {
                        for (Right right : permSumTou.getTfRights()) {

                            /*
                             * for each PublicationPermission created, ensure it knows what category
                             * it belongs to.
                             */
                            RightAdapter ra = new RightAdapter(this, right,
                                    permissionSummaryCategory);

                            adaptedRights.add(ra);
                            rrrights.add(right);
                            if (_logger.isDebugEnabled()) {
                                _logger.debug(
                                        "Right Permission: "
                                                + right.getPermission().getDesc()
                                                + "   TOU: "
                                                + right.getTypeOfUse().getDesc()
                                                + " Category: "
                                                + permissionSummaryCategory.getProduct().getAbbreviation());
                            }
                        }
                    }
                }
            }
        } else {
            rrrights = null;
            return;
        }
    }
		
    /*
     * This method filters the set of rights on the work to included only those
     * for the passed in year.
     * 
     */
	
		
    public void filterRights(String strYYYY) {
			
        // make sure publication has full set of rights before we tear them down with the filtering
        // done below that eliminates rights that are not in the desired date year
        setRRRights();
        // IF permCatDisplay.hasEncompassingRlRights() then render everything that is RL
        // if ( hasRLRights and hasTFRights & NO ncompassingRlRights
        // check for PUB Date if first one if RL rights then ignore all  TF  for lookup vice versa
        // if pubdate is null then if only RL  else TF right
        // if pubdate is not null then if only RL check for RL else check for TF if
        // Date beginYear =WebUtils.getBeginningOfTime(Integer.valueOf(strYYYY));
        // Date endYear  = WebUtils.getEndOfTime(Integer.valueOf(strYYYY));
        setPermCategories(
                PermSummaryUtil.filterRights(getPermCategories(), strYYYY));
		
    }

    public String getMainTitle()    { return work.getFullTitle();    }
    public String getMainIDNo()     { return work.getIdno();         }
    public String getMainIDNoType() { return work.getIdnoTypeCode(); }
    public String getMainAuthor()   { return work.getAuthorName();   }
    public String getMainEditor()   { return work.getEditorName();   }
    public String getVolume()       { return work.getVolume();       }
    public String getEdition()      { return work.getEdition();      }

    public String getPublicationStartDate() {
        return work.getRunPubStartDate() == null
                ? null
                : work.getRunPubStartDate().toString();
    }

    public String getPublicationEndDate() {
        return work.getRunPubEndDate() == null
                ? null
                : work.getRunPubEndDate().toString();
    }

    public String getPublicationYearRange() {
        try {
            Date beginDate = null;
            Date endDate = null;
						
            if (getPublicationStartDate() != null) {
                beginDate = PubDateDisplayUtil.parseDate(
                        getPublicationStartDate());
            }
            if ((work.getPublicationType() == null
                && getPublicationEndDate() == null)
                || (work.getPublicationType().equals("Book")
                || work.getPublicationType().equals("e-Book")
                && getPublicationEndDate() == null)) 
            {
                endDate = beginDate;
            } 
            else if (getPublicationEndDate() != null)
            {
                endDate = PubDateDisplayUtil.parseDate(getPublicationEndDate());
            } 
            else if (work.getPublicationType() != null
                    && !(work.getPublicationType().equals("Book")
                    || work.getPublicationType().equals("e-Book"))
                    && getPublicationEndDate() == null) 
            {
                endDate = PubDateDisplayUtil.parseDate("30000101");
            }

            return PubDateDisplayUtil.computeYearRangeDisplay(beginDate, endDate);
        } 
        catch (Exception exc) {
            return "";
        }
    }

    public String getMainPublisher() 
    {
        if ("null".equals(work.getPublisherName())) {
            // Workaround for the string "null" that
            // sometimes gets returned instead of an
            // actual null value.
						
            return null;
        }
        return work.getPublisherName();
    }

    public String getRightsholderNames() 
    {
        if (getHasBiactiveTfRights()) {
            ArrayList <String> arrlstRghtsHldrNames = new ArrayList<String>();
            StringBuilder stringBuilder = new StringBuilder();

            for (RightAdapter right : adaptedRights) {
                // sort out unique strings
                if (!arrlstRghtsHldrNames.contains(right.getRightsholderName())) {
                    arrlstRghtsHldrNames.add(right.getRightsholderName());
                    stringBuilder.append(right.getRightsholderName()).append(",");
                }
            }
            return stringBuilder.toString();
        } else {
            // since it is not biactive then the rights holder is the same for all rights
            if (adaptedRights == null || adaptedRights.size() == 0
                    || adaptedRights.get(0).getRightsholderName() == null) {
                if (_logger.isDebugEnabled()) {
                    _logger.warn(
                        "Rightsholder for first item in Rights List IS NULL "
                        + "-OR- the Right IS NULL!");
                    _logger.warn(
                        "Rights List SIZE == "
                        + Integer.toString(adaptedRights.size()));   
                }
                return "";
            }
            return adaptedRights.get(0).getRightsholderName();
        }
    }

    public boolean isBiactive(int usageDescriptorTypeOfUse) 
    {
        // go through all the rights, if any are biactive, then the publication is biactive
        if (adaptedRights == null) {
            _logger.error(
                "Missing Rights for work!  TF work inst: "
                + Long.toString(getTFWrkInst()));
            return false;
        }
        for (RightAdapter right: adaptedRights) {
            UsageDescriptor usageDescriptor;

            try {
                usageDescriptor = RightServiceFactory.getInstance().getService().GetUsageDescriptor(
                        (long) right.getProduct().getId(), right.getTpuInst());
            } 
            catch (CCCRuntimeException cccRuntimeException) {
                // protect against when combination of TPU and Product ID is invalid which causes exception to be thrown
                continue;
            }
            if (usageDescriptor.getTypeOfUse() == usageDescriptorTypeOfUse
                    && right.getIsBiactiveRight() != null
                    && right.getIsBiactiveRight()) {
                return true;
            }
        }
        return false;
    }

    public boolean getHasBiactiveTfRights() {
        if (adaptedRights == null) {
            _logger.error(
                "Missing Rights for work!  TF work inst: "
                + Long.toString(getTFWrkInst()));
            return false;
        }
        for (RightAdapter right: adaptedRights) {
            if (right.getIsBiactiveRight() != null && right.getIsBiactiveRight()) {
                return true;
            }
        }
        return false;
    }

    public PublicationPermission[] getPublicationPermissions(int usageDescriptorTypeOfUse) {
        return getPublicationPermissions(usageDescriptorTypeOfUse,
                LicenseeClassEnum.MATERIAL_TO_BE_DISTRIBUTED_FOR_PROFIT.getId());
    }

    public PublicationPermission[] getPublicationPermissionsByMostPermissable(int usageDescriptorTypeOfUse) {
        // Calendar calendar = Calendar.getInstance();
        // if (true) { //FOR DEBUGGING ONLY -- TO SIMULATE A MISSING TF Wrk Inst
        if (adaptedRights == null || adaptedRights.size() == 0) {
            PublicationPermission[] publicationPermission = new PublicationPermission[1];
            FauxPublicationPermission fauxPublicationPermission = new FauxPublicationPermission(
                    this, new UsageDescriptor(usageDescriptorTypeOfUse));

            publicationPermission[0] = fauxPublicationPermission;

            Date beginDate = PubDateDisplayUtil.parseDate(
                    getPublicationStartDate());

            fauxPublicationPermission.setPubBeginDate(beginDate);

            Date endDate = PubDateDisplayUtil.parseDate(getPublicationEndDate());

            fauxPublicationPermission.setPubEndDate(endDate);

            return publicationPermission;
        }
        switch (usageDescriptorTypeOfUse) 
        {
            case UsageDescriptor.NON_ACADEMIC_TRX_REPUB_ADVERTISEMENT: // pass
            case UsageDescriptor.NON_ACADEMIC_TRX_REPUB_BROCHURE: // pass
            case UsageDescriptor.NON_ACADEMIC_TRX_REPUB_CDROM: // pass
            case UsageDescriptor.NON_ACADEMIC_TRX_REPUB_DISSERTATION:
            case UsageDescriptor.NON_ACADEMIC_TRX_REPUB_DVD:
            case UsageDescriptor.NON_ACADEMIC_TRX_REPUB_EMAIL:
            case UsageDescriptor.NON_ACADEMIC_TRX_REPUB_FRAMING:
            case UsageDescriptor.NON_ACADEMIC_TRX_REPUB_INTERNET:
            case UsageDescriptor.NON_ACADEMIC_TRX_REPUB_INTRANET:
            case UsageDescriptor.NON_ACADEMIC_TRX_REPUB_JOURNAL:
            case UsageDescriptor.NON_ACADEMIC_TRX_REPUB_LINKING:
            case UsageDescriptor.NON_ACADEMIC_TRX_REPUB_MAGAZINE:
            case UsageDescriptor.NON_ACADEMIC_TRX_REPUB_NEWSLETTER:
            case UsageDescriptor.NON_ACADEMIC_TRX_REPUB_NEWSPAPER:
            case UsageDescriptor.NON_ACADEMIC_TRX_REPUB_OTHERBOOK:
            case UsageDescriptor.NON_ACADEMIC_TRX_REPUB_PAMPHLET:
            case UsageDescriptor.NON_ACADEMIC_TRX_REPUB_PRESENTATION:
            case UsageDescriptor.NON_ACADEMIC_TRX_REPUB_TEXTBOOK:
            case UsageDescriptor.NON_ACADEMIC_TRX_REPUB_TRADEBOOK:
                return getMostPermissableRepublicationPublicationPermission();

            case UsageDescriptor.NON_ACADEMIC_TRX_EMAIL:
            case UsageDescriptor.NON_ACADEMIC_TRX_EXTRANET:
            case UsageDescriptor.NON_ACADEMIC_TRX_INTERNET:
            case UsageDescriptor.NON_ACADEMIC_TRX_INTRANET:
                return getMostPermissableDigitalPublicationPermission();

            default:
                return getApplicablePermissions(usageDescriptorTypeOfUse);
        }
    }

    private PublicationPermission[] getApplicablePermissions(int usageDescriptorTypeOfUse) 
    {
        List<RightAdapter> applicableRights = new ArrayList<RightAdapter>();

        for (RightAdapter right: adaptedRights) {
            UsageDescriptor usageDescriptor;

            try {
                usageDescriptor = RightServiceFactory.getInstance().getService().GetUsageDescriptor(
                        (long) right.getProduct().getId(), right.getTpuInst());
            } catch (CCCRuntimeException cccRuntimeException) {
                // protect against when combination of TPU and Product ID is invalid which causes exception to be thrown
                continue;
            }
            if ((usageDescriptor.getTypeOfUse() == usageDescriptorTypeOfUse)) {
                applicableRights.add(right);
            }
        }
        if (applicableRights.size() > 0) {
            PublicationPermission[] applicablePermissions = new PublicationPermission[applicableRights.size()];

            for (int i = 0; i < applicableRights.size(); i++) {
                applicablePermissions[i] = applicableRights.get(i);
            }
            return  applicablePermissions;
        }
        return null;
    }

    /**
     *
     * @param usageDescriptorTypeOfUse
     * @param licenseeClass
     */
    public PublicationPermission[] getPublicationPermissions(int usageDescriptorTypeOfUse, int licenseeClass) {
        ArrayList<PublicationPermission> arrLstPublicationPermissions = new ArrayList<PublicationPermission>();

        if (adaptedRights == null) {
            _logger.error(
                    "Missing Rights for work!  TF work inst: "
                            + Long.toString(getTFWrkInst()));
            return null;
        }
        for (RightAdapter right: adaptedRights) {
            UsageDescriptor usageDescriptor;

            try {
                usageDescriptor = RightServiceFactory.getInstance().getService().GetUsageDescriptor(
                        (long) right.getProduct().getId(), right.getTpuInst());
            } catch (CCCRuntimeException cccRuntimeException) {
                // protect against when combination of TPU and Product ID is invalid which causes exception to be thrown
                continue;
            }
            if ((usageDescriptor.getTypeOfUse() == usageDescriptorTypeOfUse
                    || usageDescriptorTypeOfUse == 0)
                            && (right.getLicenseeClass() == licenseeClass
                                    || licenseeClass
                                            == LicenseeClassEnum.NO_LICENSEE_CLASS_SPECIFIED.getId())) {
                arrLstPublicationPermissions.add(right);
            }
        }
        if (arrLstPublicationPermissions.size() > 0) {
            PublicationPermission[] arrPublicationPermission = new PublicationPermission[arrLstPublicationPermissions.size()];

            return arrLstPublicationPermissions.toArray(arrPublicationPermission);
        } else {
            return new PublicationPermission[0];
        }
    }

    public PublicationPermission[] getPublicationPermissions() {
        // get all permissions
        return getPublicationPermissions(0,
                LicenseeClassEnum.NO_LICENSEE_CLASS_SPECIFIED.getId());
    }

    public PublicationPermission getPermission(UsageDescriptor usageDescriptor) {
        if (adaptedRights == null) {
            _logger.error(
                    "Missing Rights for work!  TF work inst: "
                            + Long.toString(getTFWrkInst()));
            return null;
        }
        for (RightAdapter right: adaptedRights) {
            UsageDescriptor curDescriptor;

            try {
                curDescriptor = RightServiceFactory.getInstance().getService().GetUsageDescriptor(
                        (long) right.getProduct().getId(), right.getTpuInst());
            } catch (CCCRuntimeException cccRuntimeException) {
                // protect against when combination of TPU and Product ID is invalid which causes exception to be thrown
                continue;
            }
            if (usageDescriptor.equals(curDescriptor)) {
                return right;
            }
        }
        return null; // new RightAdapter(this.getWork(), null);
    }

    public long getWrkInst() {
        return work.getPrimaryKey();
    }

    public long getTFWrkInst() {
        return work.getTfWrkInst() != null
                ? work.getTfWrkInst().longValue()
                : 0L;
    }

    public Boolean getIsPublicDomain() {
        if (adaptedRights == null) {
            _logger.error(
                    "Missing Rights for work!  TF work inst: "
                            + Long.toString(getTFWrkInst()));
            return false;
        }
        for (RightAdapter right: adaptedRights) {
            if (right.getPermission().equals(PermTypeEnum.PUBLIC_DOMAIN)) {
                return true;
            }
        }
        return false;
    }

    public Boolean getIsFrequentlyRequested() {
        return work.getIsFrequentlyRequested() != null
                ? work.getIsFrequentlyRequested()
                : Boolean.FALSE;
    }

    public String getMostPermissableDigitalPermission() {
        return getMostPermissablePermission(ProductEnum.DPS);
    }

    public String getMostPermissableRepublicationPermission() {
        return getMostPermissablePermission(ProductEnum.RLS);
    }

    private PublicationPermission[] getMostPermissableDigitalPublicationPermission() {
        return getMostPermissablePublicationPermission(ProductEnum.DPS);
    }

    private PublicationPermission[] getMostPermissableRepublicationPublicationPermission() {
        return getMostPermissablePublicationPermission(ProductEnum.RLS);
    }

    private String getMostPermissablePermission(ProductEnum productEnum) {
        RightAdapter highestRight = null;

        highestRight = findMostPermissableRight(productEnum);
        if (highestRight != null) {
            return highestRight.getPermission().getCode();
        }
        return null;
    }

    private PublicationPermission[] getMostPermissablePublicationPermission(ProductEnum productEnum) {
        PublicationPermission[] publicationPermission = new PublicationPermission[1];
        RightAdapter highestRight = null;

        highestRight = findMostPermissableRight(productEnum);
        if (highestRight != null) {
            publicationPermission[0] = highestRight;
            return publicationPermission;
        }
        return null;
    }
		
    public RightAdapter findMostPermissableRight(PermissionSummaryCategory category, String strYYYY) {
        RightAdapter previousHighestRight = null;
        Date beginYear = null;
        Date endYear = null;
        boolean skipDateCheck = false;
			
        if (StringUtils.isEmpty(strYYYY)) {
            skipDateCheck = true;
        } else {
            beginYear = WebUtils.getBeginningOfTime(Integer.valueOf(strYYYY));
            endYear = WebUtils.getEndOfTime(Integer.valueOf(strYYYY));
        }
				
        if (adaptedRights == null) {
            _logger.error(
                    "Missing Rights for work!  TF work inst: "
                            + Long.toString(getTFWrkInst()));
            return null;
        }
        for (RightAdapter right: adaptedRights) {
            if (right.getPermissionSummaryCategory().equals(category)) {
                if (!skipDateCheck) {
                    if ((endYear.compareTo(right.getPubBeginDate()) < 0)
                            || (beginYear.compareTo(right.getPubEndDate()) > 0)) {
                        continue;
                    }
                }
                if (previousHighestRight == null) {
                    previousHighestRight = right;
                } else {
                    if (right.getPermission().compareTo(
                            previousHighestRight.getPermission())
                                    < 0) {
                        previousHighestRight = right;
                    }
                }
							
            }
        }
        if (previousHighestRight == null) {
            return new SpecialOrderNoRightAdapter(this, category);
        }
        return previousHighestRight;
    }
		
    public RightAdapter findMostPermissableRight(ProductEnum productEnum) {
        RightAdapter previousHighestRight = null;

        if (adaptedRights == null) {
            _logger.error(
                    "Missing Rights for work!  TF work inst: "
                            + Long.toString(getTFWrkInst()));
            return null;
        }
        for (RightAdapter right: adaptedRights) {
            if (right.getProduct().equals(productEnum)) {
                if (previousHighestRight == null) {
                    previousHighestRight = right;
                } else {
                    if (right.getPermission().compareTo(
                            previousHighestRight.getPermission())
                                    < 0) {
                        previousHighestRight = right;
                    }
                }
            }
        }
        return previousHighestRight;
    }

    public boolean getIsPublicDomainNotBiactive() {
        if (adaptedRights == null) {
            _logger.error(
                    "Missing Rights for work!  TF work inst: "
                            + Long.toString(getTFWrkInst()));
            return false;
        }
        boolean publicDomainFound = false;
        boolean publicDomain_BiactiveFound = false;

        for (RightAdapter right: adaptedRights) {
            // GO through all the rights and see if one is both public domain and biactive
            if (right.getPermission() != null
                    && right.getPermission().equals(PermTypeEnum.PUBLIC_DOMAIN)) {
                publicDomainFound = true;
                if (right.getIsBiactiveRight() == Boolean.TRUE) {
                    publicDomain_BiactiveFound = true;
                }
            }
        }
        if (publicDomain_BiactiveFound) {
            return false;
        }
        if (publicDomainFound) {
            // this means that none of the public domain rights were biactive
            return true;
        }
        // none of the rights were public domain so return false
        return false;
    }

    public boolean isDigitalBiactive() {
        if (adaptedRights == null) {
            _logger.error(
                    "Missing Rights for work!  TF work inst: "
                            + Long.toString(getTFWrkInst()));
            return false;
        }
        for (RightAdapter right: adaptedRights) {
            if (right.getIsBiactiveRight() != null && right.getIsBiactiveRight()
                    && right.getProduct() == ProductEnum.DPS) {
                return true;
            }
        }
        return false;
    }

    public boolean isRepublicationBiactive() {
        if (adaptedRights == null) {
            _logger.error(
                    "Missing Rights for work!  TF work inst: "
                            + Long.toString(getTFWrkInst()));
            return false;
        }

        for (RightAdapter right: adaptedRights) {
            if (right.getIsBiactiveRight() != null && right.getIsBiactiveRight()
                    && right.getProduct() == ProductEnum.RLS) {
                return true;
            }
        }
        return false;

    }

    public boolean isAllDigitalDeny() {
        return false; // _sharedResultItem.isAllDigitalDeny();
    }

    public boolean isAllDigitalGrant() {
        return false; // _sharedResultItem.isAllDigitalGrant();
    }

    public boolean isAllRepubDeny() {
        return false; // _sharedResultItem.isAllRepubDeny();
    }

    public boolean isAllRepubGrant() {
        return false; // _sharedResultItem.isAllRepubGrant();
    }

    public boolean getIsAllSpecialOrder() {
        return (getTFWrkInst() == 0L);
    }
		
    public List<RightAdapter> getAdaptedRights() {
        return adaptedRights;
    }

    public List<Long> getTfWksInstList() {
        return work.getTfWksInstList();
    }

    // Starting with getSeries... mapped new Work fields for the first
    // sprint of the summit project.

    public String getIdnoWop() {
        return work.getIdnoWop();
    }

    public String getOclcNum() {
        return work.getOclcNum();
    }

    public String getSeries() {
        return work.getSeries();
    }

    public String getSeriesNumber() {
        return work.getSeriesNumber();
    }

    public String getIdnoTypeCd() {
        return work.getIdnoTypeCode();
    }

    public String getPublicationType() {
        return work.getPublicationType();
    }

    public String getCountry() {
        return work.getCountry();
    }

    public String getLanguage() {
        return work.getLanguage();
    }

    public String getIdnoLabel() {
        try {
            return ApplicationResources.getInstance().getIdnoLabel(
                    getIdnoTypeCd());
        } catch (Exception e) {
            // We are seeing a failure to generate work values
            // for order licenses.  Temporary fix to get through this.
						
            return ApplicationResources.getInstance().getIdnoLabel("NOIDNO");
        }
    }

    public String getPages() {
        return work.getPages() != null ? work.getPages().toString() : null;
    }
		
    public List<WorkTagExternal> getRrTagList() {
        return work.getRrTagList();
    }
		
    public Long getTfWrkInst() {
        return work.getTfWrkInst();
    }

    public String getTfWksInst() {
        return work.getTfWksInst();
    }

    public List<PermissionSummaryCategory> getPermCategories() {
        return permCategories;
    }

    public void setPermCategories(List<PermissionSummaryCategory> permCategories) {
        this.permCategories = permCategories;
    }
	
    public PublicationPermission getPermission(int permissionType) {
        return getPermission(new UsageDescriptor(permissionType));
    }

    // 2011-07-12 MSJ
    // Adding URL for works... for search results page.
	
    public String getItemURL() {
        return work.getItemUrl();
    }

    // 2011-12-19	MSJ
    // Adding code to quick parse annual license rights. Rights are held in
    // a list of strings containing fields delimited by underscores.  While
    // the rights lists are not as extensive as those in the rights resolver,
    // they are FAST and meet our needs for returning information regarding
    // repertory rights.  Fields, from the documentation:
    // <product>_<permission>_<rgt_inst>_<rgh_inst>_<start_date> ...
    // ... _<end_date>_<terms_flag>

    public WRAnnualLicenseHelper getAnnualLicenseHelper() 
    {
        if (work != null && helper == null) 
        {
            helper = new WRAnnualLicenseHelper(work.getRights());
        }
        return helper;
    }
}
