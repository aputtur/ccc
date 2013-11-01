package com.copyright.ccc.config;

import java.util.Properties;

import com.copyright.base.config.ClasspathConfiguration;

public class CybersourceConfiguration extends ClasspathConfiguration
{
	private static final long serialVersionUID = 1L;

	private static CybersourceConfiguration _instance;
	
    public static final String CONFIG_FILE = "cybersource.properties";
    
	/**
	 * The CyberSource Simple Order API requires us to provide a java.util.Properties 
	 * object.  Because one of the values is exposure-specific, we cannot use the 
	 * inherited Properties object (see ConfigurationHelper for more detail).  So we 
	 * initialize another object, which contains the appropriate exposure-specific value 
	 * pair assigned to the generic key.
	 * 
	 * The motivating example is the "keysDirectory" property.
	 * 
	 * .properties file & Inherited Properties object: 
	 *     keysDirectory.EXT=/u02/ccc/{ENV}/config/EXTCC
	 *     keysDirectory.INT=/u02/ccc/{ENV}/config/INTCC
	 * 
     * this Properties object:
	 *     keysDirectory.EXT=/u02/ccc/{ENV}/config/EXTCC
	 *     keysDirectory.INT=/u02/ccc/{ENV}/config/INTCC
	 *     keysDirectory=/u02/ccc/{ENV}/config/{EXTCC|INTCC}
	 */
	private Properties mCybsProperties; 
	
	public CybersourceConfiguration( String file )
	{
		super( file );
		generateExposureSpecificProperties();
	}
	
	private void generateExposureSpecificProperties()
	{
    	Properties props = super.getProperties();
    	String keysDir = getKeysDirectory();
    	props.put( keysDirectory, keysDir );
		mCybsProperties = props;
	}
	
	@Override
	public Properties getProperties() 
	{
		return mCybsProperties;
	}

    /**
     * Client code should use this method to get a handle on the 
     * <code>CybersourceConfiguration</code> object.
     */
    public static synchronized CybersourceConfiguration getInstance()
    {
        if ( _instance == null )
        {
        	_instance = new CybersourceConfiguration( CONFIG_FILE );
        }
        
        return _instance;
    }

    private static final String hopURL = "hopURL";
    private static final String merchantID = "merchantID";
    private static final String serialNumber = "serialNumber";
    private static final String subscriptionTxResponseEmail = "subscriptionTxResponseEmail";
    private static final String publicKey = "publicKey";
    private static final String environment = "environment";
    private static final String ignoreErrors = "ignoreErrors";
    private static final String keysDirectory = "keysDirectory";

	public String getHopUrl() { return this.getString( hopURL ); }
	public String getMerchantID() { return this.getString( merchantID ); }
	public String getSerialNumber() { return this.getString( serialNumber ); }
	public String getSubscriptionTxResponseEmail() { return this.getString( subscriptionTxResponseEmail ); }
	public String getPublicKey() { return this.getString( publicKey ); }
	public String getEnvironment() { return this.getString( environment ); }
	public boolean isPROD(){ return "prod".equals( getEnvironment() ); }
	public boolean getIgnoreErrors() { return this.getBoolean( ignoreErrors ); }
	public String getKeysDirectory() { return this.getString( ConfigurationHelper.getExposureBasedKey(keysDirectory) ); }
}
