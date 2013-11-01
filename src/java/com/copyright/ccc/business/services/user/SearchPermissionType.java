package com.copyright.ccc.business.services.user;

/**
 * Typesafe enumeration for <code>SearchPermissionType</code> type.
 */
public class SearchPermissionType
{
    private static final int CARDINALITY = 11;

    private static SearchPermissionType[] _valueMap = new SearchPermissionType[CARDINALITY];

    //  ACADEMIC_TRX_PHOTOCOPY maps to APS.
    //  BUSINESS_TRX_PHOTOCOPY maps to TRS.   Can be used by academia as well.
    //	BUSINESS_TRX_ILL       also maps to TRS.
    //  ACADEMIC_TRX_DIGITAL   maps to ECC.
    //  BUSINESS_TRX_DIGITAL   maps to DPS.
    //  REPUBLICATION          maps to RLS.
    //  BUSINESS_LIC_PHOTOCOPY maps to AAS.
    //  BUSINESS_LIC_DIGITAL   maps to DRA.
    //  ACADEMIC_LIC_DIGITAL   maps to AACL.  Similar to DRA (which extends AAS) but for academia.
	//	RIGHTSLINK_REPRINT     maps to rightslink, for ordering a printed copy of a work.
	//	RIGHTSLINK_DIGITAL	   maps to rightslink, for ordering a digital copy of an article.

    public static final SearchPermissionType ACADEMIC_TRX_PHOTOCOPY = new SearchPermissionType( 0, "ACADEMIC_TRX_PHOTOCOPY" );
    public static final SearchPermissionType BUSINESS_TRX_PHOTOCOPY = new SearchPermissionType( 1, "BUSINESS_TRX_PHOTOCOPY" );
    public static final SearchPermissionType ACADEMIC_TRX_DIGITAL = new SearchPermissionType( 2, "ACADEMIC_TRX_DIGITAL" );
    public static final SearchPermissionType BUSINESS_TRX_DIGITAL = new SearchPermissionType( 3, "BUSINESS_TRX_DIGITAL" );
    public static final SearchPermissionType REPUBLICATION = new SearchPermissionType( 4, "REPUBLICATION" );
    public static final SearchPermissionType BUSINESS_LIC_PHOTOCOPY = new SearchPermissionType( 5, "BUSINESS_LIC_PHOTOCOPY" );
    public static final SearchPermissionType BUSINESS_LIC_DIGITAL = new SearchPermissionType( 6, "BUSINESS_LIC_DIGITAL" );
    public static final SearchPermissionType ACADEMIC_LIC_DIGITAL = new SearchPermissionType( 7, "ACADEMIC_LIC_DIGITAL" );
    public static final SearchPermissionType RIGHTSLINK_REPRINT = new SearchPermissionType(8, "RIGHTSLINK_REPRINT" );
	public static final SearchPermissionType RIGHTSLINK_DIGITAL = new SearchPermissionType(9, "RIGHTSLINK_DIGITAL" );
    public static final SearchPermissionType BUSINESS_TRX_ILL = new SearchPermissionType( 10, "BUSINESS_TRX_ILL" );

    private int _value;
    private String _stringRep;

    /**
     * Private constructor to ensure that the static
     * <code>SearchPermissionType</code> members in this class are singleton
     * instances.
     */
    private SearchPermissionType( int value, String stringRep )
    {
        _value = value;
        _stringRep = stringRep;
        _valueMap[ value ] = this;
    }

    public int getValue()
    {
        return _value;
    }

    public static SearchPermissionType getByValue( int value )
    {
        return _valueMap[value];
    }

    public static int getCardinality()
    {
        return CARDINALITY;
    }

    public static boolean[] valueArrayInstance()
    {
        return new boolean[CARDINALITY];
    }

    /**
     * Implementation of <code>Object.toString()</code> that provides a
     * human-readable representation of each <code>SearchPermissionType</code>
     * instance.
     */
    @Override
    public String toString()
    {
        return _stringRep;
    }
}