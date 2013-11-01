package com.copyright.ccc.web.util;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.copyright.base.enums.PermTypeEnum;
import com.copyright.base.enums.ProductEnum;
import com.copyright.ccc.business.data.Publication;
import com.copyright.ccc.business.data.PublicationPermission;
import com.copyright.ccc.business.data.RLinkPublisher;
import com.copyright.ccc.business.services.search.RightAdapter;
import com.copyright.ccc.business.services.user.RLinkPublisherServices;
import com.copyright.svc.rightsResolver.api.data.PermissionLevelEnum;
import com.copyright.svc.rightsResolver.api.data.PermissionSummaryTypeOfUse;
import com.copyright.svc.rightsResolver.api.data.Right;
import com.copyright.svc.rightsResolver.api.data.SourceCodeEnum;

/**
 * Wrapper class used to display PermissionSummaryTypeOfUse objects
 *
 * @author Bill Murphy
 */
public final class PermSummaryTouDisplay
{
   /* public static final String PPU_PHOTOCOPY_ACADEMIC = "Photocopy for academic coursepacks, classroom handouts.";
    public static final String PPU_PHOTOCOPY_BUSINESS = "Photocopy for general business use, library reserves, ILL/document delivery.";
    public static final String PPU_DIGITAL_ACADEMIC   = "Posting e-reserves, course management systems, e-coursepacks.";
    public static final String PPU_DIGITAL_BUSINESS   = "Use in e-mail, intranet/extranet/Internet postings.";
    public static final String PPU_REPUBLICATION      = "Republish into a book, journal, newsletter.";
    public static final String LIC_PHOTOCOPY_ACADEMIC = "Photocopy or share content electronically.";
    public static final String LIC_PHOTOCOPY_BUSINESS = "Photocopy and share with co-workers.";
    public static final String LIC_DIGITAL_BUSINESS   = "E-mail to co-workers or post to an intranet.";*/

    //private PublicationPermission _permission            = null;
    private PermissionSummaryTypeOfUse _permSumTou = null;
    private RightAdapter rightAdapter = null;  //hold the right for this type of use
    private ProductEnum productEnum = null;

	private RLinkPublisher        _publisher             = null;
    private boolean               _biactive              = false;
    private boolean               _biactiveDisplay       = false;
    private boolean               _globalRightsQualifier = false;
    private boolean               _integrates            = false;
//    private HashMap               _description           = null;
    private RLinkPublisherServices _rlSvc = null;
    
    
    
    public Boolean getIsTitleLevelPermission(){
    	PermissionLevelEnum permissionLevelEnum = _permSumTou.getPermissionLevel();
        return permissionLevelEnum != null && permissionLevelEnum.equals(PermissionLevelEnum.TITLE_LEVEL);    
    }
    public Boolean getIsChapterLevelPermission(){
    	PermissionLevelEnum permissionLevelEnum = _permSumTou.getPermissionLevel();
        return permissionLevelEnum != null && permissionLevelEnum.equals(PermissionLevelEnum.CHAPTER_LEVEL); 
    }
    public Boolean getIsArticleLevelPermission(){
    	PermissionLevelEnum permissionLevelEnum = _permSumTou.getPermissionLevel();
        return permissionLevelEnum != null && permissionLevelEnum.equals(PermissionLevelEnum.ARTICLE_LEVEL); 
    }
    
    public PermSummaryTouDisplay(Publication pub, ProductEnum productEnum, PermissionSummaryTypeOfUse perm) {
    	_permSumTou = perm;
    	this.productEnum = productEnum;
    	
    	//TODO need to address bi-activity, but we assume that the user is forced to choose a date on the search which will
    	//eliminate the multiple rights
    	if (_permSumTou.getSourceCode()==SourceCodeEnum.TF){
    		if (_permSumTou.getTfRights() != null && _permSumTou.getTfRights().size()>0){
    			Right right =_permSumTou.getTfRights().get(0);
    			rightAdapter = new RightAdapter(pub,right);
    			//  Find out if this publisher is also a RightsLink publisher.
    			//But only if the the right is not available, not covered or contact rights holder
    			//there maybe more permissive permissions in RightsLink
    			if ((rightAdapter.getAvailability().startsWith("Not") || rightAdapter.getAvailability().startsWith("Contact")) && !_integrates){

	    			_publisher = RLinkPublisherServices.getRLinkPublisherByPtyInst(
	    					rightAdapter.getRightsholderInst()
	    			);
	    			if (_publisher!=null) {
	    				setIntegrates(true);
	    			} else {
	    				setIntegrates(false);
	    			}	
    		  }
    		}
    	}
    }
    
    public PermSummaryTouDisplay(PermissionSummaryTypeOfUse perm
    		                 , boolean isBiactive
                             , boolean displayBiactive
                             )   {
        _permSumTou = perm;
        _biactive = isBiactive;
        _biactiveDisplay = displayBiactive;
        
    }
    /**
     *  Primary constructor for valid permissions.
     */
    private PermSummaryTouDisplay( PermissionSummaryTypeOfUse perm
                            , RLinkPublisher publshr
                            , boolean isBiactive
                            , boolean displayBiactive
                            )
    {
        _permSumTou = perm;
        _publisher = publshr;
        if (_publisher != null) _integrates = true;
        _biactive = isBiactive;
        _biactiveDisplay = displayBiactive;
        

        //  I had to sort of reverse engineer the UsageDescriptor.

       /* _description = new HashMap();

        //	Typical transactional rights.

        _description.put( new UsageDescriptor( UsageDescriptor.ACADEMIC_TRX_PHOTOCOPY ).getDescription(), PPU_PHOTOCOPY_ACADEMIC );
        _description.put( new UsageDescriptor( UsageDescriptor.ACADEMIC_TRX_SCAN ).getDescription(), PPU_DIGITAL_ACADEMIC );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_PHOTOCOPY ).getDescription(), PPU_PHOTOCOPY_BUSINESS );

        //	Digital rights (transactional).

        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_INTERNET ).getDescription(), PPU_DIGITAL_BUSINESS );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_EXTRANET ).getDescription(), PPU_DIGITAL_BUSINESS );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_INTRANET ).getDescription(), PPU_DIGITAL_BUSINESS );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_EMAIL ).getDescription(), PPU_DIGITAL_BUSINESS );

        //	Republication rights.

        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_REPUB_INTERNET ).getDescription(), PPU_REPUBLICATION );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_REPUB_INTRANET ).getDescription(), PPU_REPUBLICATION );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_REPUB_LINKING ).getDescription(), PPU_REPUBLICATION );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_REPUB_FRAMING ).getDescription(), PPU_REPUBLICATION );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_REPUB_EMAIL ).getDescription(), PPU_REPUBLICATION );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_REPUB_CDROM ).getDescription(), PPU_REPUBLICATION );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_REPUB_DVD ).getDescription(), PPU_REPUBLICATION );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_REPUB_JOURNAL ).getDescription(), PPU_REPUBLICATION );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_REPUB_MAGAZINE ).getDescription(), PPU_REPUBLICATION );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_REPUB_NEWSPAPER ).getDescription(), PPU_REPUBLICATION );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_REPUB_NEWSLETTER ).getDescription(), PPU_REPUBLICATION );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_REPUB_DISSERTATION ).getDescription(), PPU_REPUBLICATION );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_REPUB_BROCHURE ).getDescription(), PPU_REPUBLICATION );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_REPUB_PAMPHLET ).getDescription(), PPU_REPUBLICATION );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_REPUB_ADVERTISEMENT ).getDescription(), PPU_REPUBLICATION );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_REPUB_PRESENTATION ).getDescription(), PPU_REPUBLICATION );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_REPUB_TEXTBOOK ).getDescription(), PPU_REPUBLICATION );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_REPUB_TRADEBOOK ).getDescription(), PPU_REPUBLICATION );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_REPUB_OTHERBOOK ).getDescription(), PPU_REPUBLICATION );

		//	Annual licenses, both digital and traditional.

        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_LICENSE_PHOTOCOPY ).getDescription(), LIC_PHOTOCOPY_BUSINESS );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_LICENSE_INTERNET ).getDescription(), LIC_DIGITAL_BUSINESS );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_LICENSE_EXTRANET ).getDescription(), LIC_DIGITAL_BUSINESS );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_LICENSE_INTRANET ).getDescription(), LIC_DIGITAL_BUSINESS );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_LICENSE_EMAIL ).getDescription(), LIC_DIGITAL_BUSINESS );
        _description.put( new UsageDescriptor( UsageDescriptor.ACADEMIC_LICENSE_PHOTOCOPY ).getDescription(), LIC_PHOTOCOPY_ACADEMIC );*/
    }

   /* public PermissionDisplay( int index
                            , Publication original
                            , RLinkPublisher publshr
                            , UsageDescriptor typeOfUse )
    {
        _index = index;
        _publisher = publshr;
        if (_publisher != null) _integrates = true;
        _permission = new FauxPublicationPermission( original, typeOfUse );
        _biactive = false;
        _biactiveDisplay = false;
        

        //  I had to sort of reverse engineer the UsageDescriptor.

        _description = new HashMap();

        //	Typical transactional rights.

        _description.put( new UsageDescriptor( UsageDescriptor.ACADEMIC_TRX_PHOTOCOPY ).getDescription(), PPU_PHOTOCOPY_ACADEMIC );
        _description.put( new UsageDescriptor( UsageDescriptor.ACADEMIC_TRX_SCAN ).getDescription(), PPU_DIGITAL_ACADEMIC );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_PHOTOCOPY ).getDescription(), PPU_PHOTOCOPY_BUSINESS );

        //	Digital rights (transactional).

        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_INTERNET ).getDescription(), PPU_DIGITAL_BUSINESS );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_EXTRANET ).getDescription(), PPU_DIGITAL_BUSINESS );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_INTRANET ).getDescription(), PPU_DIGITAL_BUSINESS );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_EMAIL ).getDescription(), PPU_DIGITAL_BUSINESS );

        //	Republication rights.

        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_REPUB_INTERNET ).getDescription(), PPU_REPUBLICATION );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_REPUB_INTRANET ).getDescription(), PPU_REPUBLICATION );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_REPUB_LINKING ).getDescription(), PPU_REPUBLICATION );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_REPUB_FRAMING ).getDescription(), PPU_REPUBLICATION );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_REPUB_EMAIL ).getDescription(), PPU_REPUBLICATION );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_REPUB_CDROM ).getDescription(), PPU_REPUBLICATION );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_REPUB_DVD ).getDescription(), PPU_REPUBLICATION );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_REPUB_JOURNAL ).getDescription(), PPU_REPUBLICATION );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_REPUB_MAGAZINE ).getDescription(), PPU_REPUBLICATION );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_REPUB_NEWSPAPER ).getDescription(), PPU_REPUBLICATION );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_REPUB_NEWSLETTER ).getDescription(), PPU_REPUBLICATION );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_REPUB_DISSERTATION ).getDescription(), PPU_REPUBLICATION );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_REPUB_BROCHURE ).getDescription(), PPU_REPUBLICATION );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_REPUB_PAMPHLET ).getDescription(), PPU_REPUBLICATION );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_REPUB_ADVERTISEMENT ).getDescription(), PPU_REPUBLICATION );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_REPUB_PRESENTATION ).getDescription(), PPU_REPUBLICATION );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_REPUB_TEXTBOOK ).getDescription(), PPU_REPUBLICATION );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_REPUB_TRADEBOOK ).getDescription(), PPU_REPUBLICATION );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_REPUB_OTHERBOOK ).getDescription(), PPU_REPUBLICATION );

		//	Annual licenses, both digital and traditional.

        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_LICENSE_PHOTOCOPY ).getDescription(), LIC_PHOTOCOPY_BUSINESS );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_LICENSE_INTERNET ).getDescription(), LIC_DIGITAL_BUSINESS );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_LICENSE_EXTRANET ).getDescription(), LIC_DIGITAL_BUSINESS );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_LICENSE_INTRANET ).getDescription(), LIC_DIGITAL_BUSINESS );
        _description.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_LICENSE_EMAIL ).getDescription(), LIC_DIGITAL_BUSINESS );
        _description.put( new UsageDescriptor( UsageDescriptor.ACADEMIC_LICENSE_PHOTOCOPY ).getDescription(), LIC_PHOTOCOPY_ACADEMIC );
    }*/

    public PermissionSummaryTypeOfUse getPermSumTou() { return _permSumTou; }

    public long    getRgtInstRlOfferId()  { 
    	if (getIsAvailableRightslink()) return _permSumTou.getRlOfferId();
    	if (rightAdapter == null) return 0;
    	return rightAdapter.getRgtInst(); 
    };

    //added for pub to pub
    public String  getOfferChannel() 
    { 
    	if (_permSumTou.getOfferChannel() != null && _permSumTou.getOfferChannel().getCd() != null)
    	{
    		return _permSumTou.getOfferChannel().getCd();
    	}
    	else
    	{
    		return ""; 
    	}
    		
    }
    	
    public String  getRlPermissionType()           
    
    { 
    	if (_permSumTou.getRlPermissionType() != null && _permSumTou.getRlPermissionType().getCd() != null)
    	{
    		return _permSumTou.getRlPermissionType().getCd();
    	}
    	else
    	{
    		return "";
    	}
    }
    
    public String  getRlPubCode()           
    
    { 
    	if (_permSumTou.getRlPubCode() != null)
    	{
    		return _permSumTou.getRlPubCode();
    	}
    	else
    	{
    		return "";
    	}
    }
    
    public String  getDescription() { 
    	  return _permSumTou.getDisplayMessage(); 
    }
    
    public String getEscapedDescription() {
    	if (_permSumTou.getDisplayMessage().contains("'")){
    		String newDisplayMessage = _permSumTou.getDisplayMessage().replaceAll("'","\\\\'");
    		return newDisplayMessage;
    	}
    	  return _permSumTou.getDisplayMessage(); 
    }
    public List<Right> getTfRights()            { return _permSumTou.getTfRights(); }
    public String  getTpuDesc()                 { return _permSumTou.getRrTou(); }
    public String  getRhTerms()                   { return getIsAvailableRightslink()?null:rightAdapter.getRHTerms(); }
    public long    getRightsholderInst()          { return getIsAvailableRightslink()?0:rightAdapter.getRightsholderInst(); }
    public String  getRightsholderName()          { return getIsAvailableRightslink()?null:rightAdapter.getRightsholderName(); }
    public String  getRightsQualifyingStatement() { return getIsAvailableRightslink()?"":rightAdapter.getRightsQualifyingStatement(); }
    public String  getPermDateRange()             { return getIsAvailableRightslink()?null:rightAdapter.getPermDateRange(); }
    public Boolean getIsBiactive()                { return  Boolean.valueOf( _biactive ); }
    public Boolean getDisplayBiactive()           { return  Boolean.valueOf( _biactiveDisplay ); }
    public Boolean getGlobalRightsQualifier()     { return Boolean.valueOf( _globalRightsQualifier ); }
    public Boolean getIsAcademic()                { return productEnum != null && (productEnum.equals(ProductEnum.ARS)||productEnum.equals(ProductEnum.ECC)||productEnum.equals(ProductEnum.APS)); }
 
    public Boolean getIsValidTOU() {
    	if (productEnum == null && _permSumTou.getSourceCode()!=SourceCodeEnum.RL) {
    		return false;
    	}
    	return true;
    }
    
    
    public String getPermissionLevel()
    {
    	
    	if (_permSumTou.getPermissionLevel() != null)
    	{
    		return _permSumTou.getPermissionLevel().getDesc();
    	}
    	else
    	{
    		return "TF";
    	}
    	
    }

    public void setGlobalRightsQualifier( Boolean b ) { _globalRightsQualifier = b.booleanValue(); }
    public void setIntegrates( Boolean b )            { _integrates = b.booleanValue(); }

    //  The following methods should assist us in displaying our
    //  data to the user in the JSP.  This might have been better
    //  served with tags, but since it is only useable in the search
    //  results and landing pages I decided it would be easier to
    //  simply whip up a class that can be referenced by standard
    //  Struts logic and bean tags.
    public String  getAvailability() { 
    	if (getIsAvailableRightslink()) {
    		return "Available for Purchase";
    	}
    	if (rightAdapter==null) return "Not available";
    	return rightAdapter.getAvailability(); 
    }

    public Boolean getIsAvailable() {
        return !getIsAvailableRightslink() && isAvailableInternal();
    }
    public Boolean getIsAvailableRightslink() {
    	if (_permSumTou.getSourceCode() == null) return Boolean.FALSE;
    	if ((_permSumTou.getSourceCode()==SourceCodeEnum.RL) && (_permSumTou.getRlPermissionType() != null) && (_permSumTou.getRlPermissionType().getCd() == PermTypeEnum.DENY.getCode())) {
			return false;
		}
    	return (_permSumTou.getSourceCode().compareTo(SourceCodeEnum.RL)==0) ? true:false;
    }
    private Boolean isAvailableInternal() {
    	// Original implementation of getIsAvailable().
    	// Turned into internal method to support temporary implementation of getIsAvailableRightslink().
    	// Revert to getIsAvailable() once the Summit Rights Resolver is ready.
    	// [dstine 2/17/10]
        Boolean r = Boolean.FALSE;
    	if (rightAdapter == null) return r;
        String s = rightAdapter.getAvailability();
        if (s != null && (s.startsWith("Available for Pu") || s.startsWith("Cov") || s.startsWith("Public Domain"))) r = Boolean.TRUE;
        return r;
    }
    public Boolean getIsNotAvailable() {
        Boolean r = Boolean.FALSE;
    	if (rightAdapter == null) return r;
        String s = rightAdapter.getAvailability();
        if (s == null || s.startsWith("N")) r = Boolean.TRUE;
        return r;
    }
    public Boolean getIsContactRightsholder() {
        Boolean r = Boolean.FALSE;
    	if (rightAdapter == null) return r;
        String s = rightAdapter.getAvailability();
        if (s != null && s.startsWith("Con")) return Boolean.valueOf(true);
        return r;
    }
    public Boolean getIsSpecialOrder() {
        Boolean r = Boolean.FALSE;
    	if (rightAdapter == null) return r;
        String s = rightAdapter.getAvailability();
        if (s != null && s.startsWith("Available for Sp")) return Boolean.valueOf(true);
        return r;
    }
    public Boolean getGotTerms() {
        Boolean r = Boolean.FALSE;
    	if (rightAdapter == null) return r;
        String s = rightAdapter.getRHTerms();
        if (!isNull(s)) r = Boolean.TRUE;
        return r;
	}
    public Boolean getGotRQualStmt() {
        Boolean r = Boolean.FALSE;
    	if (rightAdapter == null) return r;
        String s = rightAdapter.getRightsQualifyingStatement();
        if (!isNull(s)) r = Boolean.TRUE;
        return r;
	}

    
	public Boolean getIsRepublication() {
		return productEnum != null && productEnum.equals(ProductEnum.RLS);
	}
	public Boolean getIsDigital() {
		return productEnum != null && productEnum.equals(ProductEnum.DPS);
	}
    public Boolean getIsLicense() {
        return productEnum != null && (productEnum.equals(ProductEnum.ARS) || productEnum.equals(ProductEnum.AAS) || productEnum.equals(ProductEnum.DRA));
    }
    public Boolean getIsPublicDomain() {
        Boolean r = Boolean.FALSE;
    	if (rightAdapter == null) return r;
        String s = rightAdapter.getAvailability();
        if (s != null && s.startsWith("Public Domain")) return Boolean.valueOf(true);
        return r;
    }
    
    //  RightsLink Integration methods.
    
    public RLinkPublisher getPublisher() {
    	return _publisher;
    }
    public Boolean getIntegrates() {
        //  Rightslink integration test.
        return Boolean.valueOf(_integrates);
    }
    public String getPublisherURL() {
        String url = "";
        
        if (_publisher != null) {
            url = _publisher.getPubUrl();   
        }
        return url;
    }
    public String getPublisherName() {
        String name = "";
        
        if (_publisher != null) {
            name = _publisher.getPubName();   
        }
        return name;
    }
    public String getPermissionOptions() {
        String opts = "";
        
        if (_publisher != null) {
            opts = _publisher.getPermOptionDesc();   
        }
        return opts;
    }
    public String getLearnMore() {
        String learn = "";
        
        if (_publisher != null) {
            learn = _publisher.getLearnMoreDesc();   
        }
        return learn;
    }
    
    public PublicationPermission getPublicationPermission() {
    	return rightAdapter;
    }
    
    public ProductEnum getProductEnum() {
		return productEnum;
	}

	public void setProductEnum(ProductEnum productEnum) {
		this.productEnum = productEnum;
	}
	
    public Right findMostPermissableRight(){
        Right previousHighestRight = null;
        if (_permSumTou.getTfRights() == null){
            return null;
        }
	     for (Right right: _permSumTou.getTfRights())
	     {
	        if (previousHighestRight == null) {
	            previousHighestRight = right;
	        }
	        else {
	            if (right.getPermission().compareTo(previousHighestRight.getPermission())<0){
	                previousHighestRight = right;
	            }
	        }
	     }
	     return previousHighestRight;
    }
    
    public Long getRrTouId() {
    	return _permSumTou.getRrTouId();
    }

    public Long getTpuInst() {
        
    	if (rightAdapter == null ) return 0L;
        return  rightAdapter.getTpuInst();
        
	}
    
        
    
        
    //  Private method.
    
    private boolean isNull(String s)
    {
        return (s == null || s.equals(""));
    }
    
    private  String getRLPermDateRange(){
        Calendar calendar = Calendar.getInstance();
        if (_permSumTou.getRlBiactiveStartDate()==null){
        	return null;
        }
        try {
        calendar.setTime(_permSumTou.getRlBiactiveStartDate());
        Date beginDate = calendar.getTime();
                
        calendar.setTime(_permSumTou.getRlBiactiveEndDate());
        Date endDate = calendar.getTime();
        
        return PubDateDisplayUtil.computeYearRangeDisplay(beginDate, endDate);
        }
        catch (Exception e){
            return "";
        }
    }
}
