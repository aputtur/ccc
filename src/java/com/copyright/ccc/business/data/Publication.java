package com.copyright.ccc.business.data;

import java.io.Serializable;
import java.util.List;

import com.copyright.ccc.business.services.search.RightAdapter;
import com.copyright.ccc.business.services.WRAnnualLicenseHelper;


/**
 * Represents a single work and includes methods for accessing the works
 * permissions. The permissions are represented by the PublicationPermission interface.
 * 
 * @author  Mike Tremblay
 * @version 1.0
 * Created 26-Oct-2006
 *
 * Changes
 * ----------------------------------------------------------------
 * 2011-12-20   MSJ     Added getAnnualLicenseHelper method for rights.
 * 2011-07-12   MSJ     Added getItemURL method for search results.
 */
 
public interface Publication extends Serializable {

	public String getMainTitle();

	public String getMainIDNo();

	public String getMainIDNoType();

	public String getMainAuthor();

	public String getMainEditor();

	public String getVolume();

	public String getEdition();

	public String getPublicationStartDate();

	public String getPublicationEndDate();

	public String getPublicationYearRange();

	public String getMainPublisher();
	
	public String getRightsholderNames();
        
        public String getOclcNum();
        
        public String getIdnoWop();
        
    public boolean isBiactive(int typeOfUse);
    
    public boolean getHasBiactiveTfRights();

	public PublicationPermission[] getPublicationPermissions(int typeOfUse);
        
    public PublicationPermission[] getPublicationPermissionsByMostPermissable(int typeOfUse);
    
    public PublicationPermission[] getPublicationPermissions(int typeOfUse, int licenseeClass);
	
    public PublicationPermission[] getPublicationPermissions();

    public PublicationPermission getPermission(int permissionType);
        
	public long getWrkInst();
        
    public long getTFWrkInst();
    
    public boolean getIsPublicDomainNotBiactive();
        
    public Boolean getIsPublicDomain();

    public Boolean getIsFrequentlyRequested();
        
    public boolean isAllDigitalDeny();
        
    public boolean isAllDigitalGrant();
        
    public boolean isAllRepubDeny();
        
    public boolean isAllRepubGrant();
    
    public String getMostPermissableDigitalPermission();

    public String getMostPermissableRepublicationPermission();
    
    public boolean isDigitalBiactive();
    
    public boolean isRepublicationBiactive();
    
    public boolean getIsAllSpecialOrder();

    public List<RightAdapter> getAdaptedRights();
    
    //  2009-09-16  MSJ
    //  Methods added for Jan 2010 release.  GetIdnoLabel
    //  translates the getIdnoTypeCd value to a readable
    //  value mapped to the CC2Configuration.properties
    //  file.
    
    public String getIdnoTypeCd();
    public String getIdnoLabel();
    public String getSeries();
    public String getSeriesNumber();
    public String getPublicationType();
    public String getPages();
    public String getCountry();
    public String getLanguage();
    
    //  2011-07-12 MSJ
    //  We need this to slap up into search results, part of search+2.
    
    public String getItemURL();

    //  2011-12-20 MSJ
    //  Surfacing basic (on the external work) rights through a helper class.

    public WRAnnualLicenseHelper getAnnualLicenseHelper();
}
