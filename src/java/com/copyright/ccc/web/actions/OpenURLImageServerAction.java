package com.copyright.ccc.web.actions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.business.data.CCSearchCriteria;
import com.copyright.ccc.business.data.Publication;
import com.copyright.ccc.business.data.PublicationPermission;
import com.copyright.ccc.business.services.search.SearchCriteriaImpl;
import com.copyright.ccc.business.services.search.WebServiceSearch;
import com.copyright.ccc.config.CC2Configuration;
import com.copyright.ccc.web.CCAction;

/*
    title:          OpenURLImageServerAction (a.k.a. LinkManager V2)
    subtitle:       How to write a thousand lines of code to return an image.
    author:         Anonymous
    age:            42
    sex:            Male
    hobbies:        Hiking, reading, keeping fish and tarantulas, eating.
    weight:         180 lbs. give or take 40 lbs.  Mostly give.
    date:           March 2008
    
    description:    A rather monolithic module that encompasses the functionality
                    of the old LinkManager servlet.  The purpose is to accept a
                    search request and return either an image of a button, or a
                    single pixel image invisible to the requestor.

    parameters:     The servlet does expect certain values to be passed to it.  This
                    servlet is actually sort of an openURL subset handler.  It is not
                    designed to parse and handle all openURL parameters.  It also
                    expects some CCC specific paramters.  Here is what we expect/handle:

                    ISSN or ISBN    This becomes our idno_wop.
                    SERVICENAME     The product abbreviation for usage.
                    DATE            This is the publication year of the work.
                    [CRITERIA]      An option that defines what a "successful search"
                                    will be.

    requirements:   We will need to know the client's SID in advance if we want to set
                    up a subdirectory of images for said client.  Otherwise we will
                    default to our standard images.  No matter what the vendor, the
                    images must have names consistent with those defined in our
                    configuration properties file, eg. "available.jpg" and 
                    "unavailable.jpg."  It has been discussed that in the future
                    our response might not be binary in nature, but span the breadth
                    of the statuses we use when permissioning.

    caveats:        The design is simple and because of that I have purposely limited
                    the flexibility of the request criteria for the search.  Business
                    requirements demand an IDNO and PUB_YEAR.  We also allow the
                    criteria that DEFINES A SUCCESSFUL QUERY.  What?!  You say!
                    Copyright.com allows you to request rights to works we don't even
                    have in our database.  This might not sit well with some of our
                    customers who won't want to go through our 60 day process.  So I
                    have included the following possible criteria for success which must
                    be specified BY THE CLIENT or we default to NONE for criteria:

                    ALWAYS  No criteria.  We return an "available" image even if
                            the item does not exist in our database.
                    EXISTS  As it sounds.  If we find it in our database, it is all good.
                    RIGHTS  This means there MUST be RIGHTS associated with this
                            work.  We might have it in our database without rights.
                    GRANTS  This work must have one or more rights of GRANT on it.

    change log:     Enter changes here.  Most recent change at the top, please.
    
    who         when        what
    ----        ----------  -------------------------------------------------------
    Jessop      2008/06/09  Added handling in the date parsers for THROUGH and PRESENT.
*/

public class OpenURLImageServerAction extends CCAction
{
    //  Constants defining some of our session-safe variables.
    
    private static final String DEFAULT = "_default";
    private static final String VENDOR_PATH = "_vendorPath";
    private static final String IMAGE_SUCCESS = "_success";
    private static final String IMAGE_FAILURE = "_failure";
    private static final String IDNO_WOP = "_idno_wop";
    private static final String DATE = "_year";
    private static final String CRITERIUM = "_criterium";
    private static final String PRODUCTS = "_product";
    private static final String TYPE_OF_USE = "_typeOfUse";
    private static final String PATH_SEPARATOR = "/";
    private static final String ZERO = "0";
    private static final String EMPTY_STRING = "";
    
    //  Criteria constants.
    
    private static final int BEST = 0;
    private static final int ALWAYS = 0;
    private static final int EXISTS = 1;
    private static final int RIGHTS = 2;
    private static final int GRANTS = 4;
    
    //  URL parameters.
    
    private static final String PARM_SID = "sid";
    private static final String PARM_ISSN = "issn";
    private static final String PARM_ISBN = "isbn";
    private static final String PARM_DATE = "date";
    private static final String PARM_CRITERIA = "criteria";
    private static final String PARM_SERVICENAME = "servicename";
    
    //  Some of our "shared" variables can be declared here and
    //  initialized during class instantiation.  Others need to
    //  be "session" safe and will be loaded into the request
    //  object.
    
    private HashMap<String,String> _products;
    private HashMap<String,String> _criteria;
    private HashMap<String,byte[]> _imagesSuccess;
    private HashMap<String,byte[]> _imagesFailure;
    private String _mimeType;
    private String _baseImagePath;
    private String _pathSeparator;
    private String _success;
    private String _failure;
    private int _defaultCriteria;
    
    //  Our all-important search service.
    
    private WebServiceSearch webServiceSearch;
    
    //  Constructor.  Perform basic initialization.  Keep in
    //  mind that an instantiation could be shared across requests
    //  since struts/J2EE apparently tries to reuse as many objects
    //  as it can.
    
    public OpenURLImageServerAction()
    {
        super();
        
        //  Load up some basic path values.
        
        _products = getProducts();
        _pathSeparator = CC2Configuration.getInstance().getPathSeparator();
        _baseImagePath = CC2Configuration.getInstance().getImageDir() + _pathSeparator;
        _mimeType = CC2Configuration.getInstance().getMimetype();
        _success = CC2Configuration.getInstance().getURLImageAvailable();
        _failure = CC2Configuration.getInstance().getURLImageUnavailable();
        _imagesSuccess = new HashMap<String,byte[]>();
        _imagesFailure = new HashMap<String,byte[]>();
        
        //  Load up our default images.  Since classes are reused this
        //  is a good way to minimize some of the image loading and
        //  reloading that could potentially go on.
        
        loadDefaultImages();
        
        //  We already loaded our individual products, but we allow for
        //  keywords to indicate multiple products in our search, so tack
        //  these onto the product hashmap.
        
        _products.put( CC2Configuration.getInstance().getProductDefaultAbrv(), CC2Configuration.getInstance().getProductDefaultInst() );
        _products.put( CC2Configuration.getInstance().getProductAcademicAbrv(), CC2Configuration.getInstance().getProductAcademicInst() );
        _products.put( CC2Configuration.getInstance().getProductBusinessAbrv(), CC2Configuration.getInstance().getProductBusinessInst() );
        
        //  Our criteria are just word-mapped integer values used in
        //  recognizing which results are viable in our search.  The default
        //  criteria value is a fallback.
        
        _criteria = getCriteria();
        _defaultCriteria = Integer.parseInt( CC2Configuration.getInstance().getCriteriaDefault() );
        
        //  Get a handle on our search service.
        
        webServiceSearch = new WebServiceSearch();
    }
    
    //  Our default operation is to perform a search of the database
    //  based on the parameters provided by the caller.  Since we will
    //  be writing directly to the response object, we will always return
    //  a null action forward.
    
    public ActionForward defaultOperation( ActionMapping       mapping
                                         , ActionForm          form
                                         , HttpServletRequest  request
                                         , HttpServletResponse response )
    {
        return search( mapping, form, request, response );
    }
    
    //  The default operation is the way to go, but just in case, let's
    //  separate it out into a "search" method as well, to correspond to
    //  the old servlet method of search.
    
    public ActionForward search( ActionMapping       mapping
                               , ActionForm          form
                               , HttpServletRequest  request
                               , HttpServletResponse response )
    {
        //  Declare a couple of needed values.
        
        String idno = "";
        CCSearchCriteria searchOn = null;
        
        //  Grab our URL parameters.  The first set are supported by the OpenURL
        //  standard document.  You can find more information at this address:
        //  http://www.exlibrisgroup.com/category/sfxopenurlsyntax
        
        String sid = request.getParameter( PARM_SID );
        String issn = request.getParameter( PARM_ISSN );
        String isbn = request.getParameter( PARM_ISBN );
        String year = request.getParameter( PARM_DATE );
        
        //  The following two parameters are CCC specific, driven by our
        //  search requirements.
        
        String criteria = request.getParameter( PARM_CRITERIA );
        String products = request.getParameter( PARM_SERVICENAME );
        
        //  Perform some tests and conversions to make
        //  sure our parameters are up to snuff.
        
        pickIdno( request, issn, isbn );
        parseYear( request, year );
        mapProduct( request, products );
        checkCriteria( request, criteria );

        //  Log our query to the logfile.  This might be useful at some point
        //  in the future, and it just makes me happy to see something I wrote
        //  being used.

        StringBuffer logRequest = new StringBuffer( 255 );
        
        logRequest.append( "IBL LinkManager:  Request (sid=" ).append( sid );
        logRequest.append( "; idno=" ).append( (String) request.getAttribute( IDNO_WOP ) );
        logRequest.append( "; pubYear=" ).append( (Long) request.getAttribute( DATE ) );

        @SuppressWarnings("unchecked")
        Vector<Integer> prods = (Vector<Integer>) request.getAttribute( PRODUCTS );
        logRequest.append( "; prdInst=" ).append( prods );
        
        logRequest.append( "; criteria=" ).append( (Integer) request.getAttribute( CRITERIUM ) ).append( ");" );
        
        _logger.info( logRequest.toString() );
        
        //  Prepare to respond.  We set our headers, then create an empty byte buffer
        //  to receive our image (we will be spitting out a raw image).
        
        response.setContentType( _mimeType );
        response.setHeader( "pragma", "no-cache" );
        response.setHeader( "cache-control", "no-cache" );
        
        byte[] obuf = null;
        
        //  Perform our search, pick the image based on the success of our
        //  search (and of course the SID of the requestor - if it exists).
        //  Since our search is ISSN or ISBN based, it *should* return a
        //  single result.
        
         if (performSearch( request )) {
             obuf = lookupImage( IMAGE_SUCCESS, getKey( sid ) );
         }
         else {
             obuf = lookupImage( IMAGE_FAILURE, getKey( sid ) );       
         }
        
        //  Set our image, send it.
        
        try {
            response.setContentLength( obuf.length );
            response.getOutputStream().write( obuf );
        }
        catch (Exception e) {
            //  Add error handling.
        }
        
        return null;
    }
    
    //  **********************************************************************
    //  A variety of helper methods are below.
    //  **********************************************************************
    
    private void initializeRequestVariables( HttpServletRequest request )
    {
        request.setAttribute( VENDOR_PATH, "" );
    }
    
    //  **********************************************************************
    //  Load our product mappings.
    
    private HashMap<String,String> getProducts()
    {
        HashMap<String,String>   pmap = new HashMap<String,String>();
        String[]  vals = CC2Configuration.getInstance().getProductInst().split( ":" );
        String[]  keys = CC2Configuration.getInstance().getProductAbrv().split( ":" );

        for (int i = 0; i < keys.length; i++)
        {
            pmap.put( keys[i], vals[i] );
        }
        
        return pmap;
    }
    
    //  **********************************************************************
    //  Load our criteria mappings.
    
    public HashMap<String,String> getCriteria()
    {
        HashMap<String,String>   cmap = new HashMap<String,String>();
        String[]  vals = CC2Configuration.getInstance().getCriteriaInst().split( ":" );
        String[]  keys = CC2Configuration.getInstance().getCriteriaAbrv().split( ":" );

        for (int i = 0; i < keys.length; i++)
        {
            cmap.put( keys[i], vals[i] );
        }
        
        return cmap;
    }
    
    //  **********************************************************************
    //  Preload our default images.  These are the ones that will be used
    //  most often.
    
    private void loadDefaultImages()
    {
        //  We assume our images are where they should be.  If they
        //  are not, a message will be logged and trouble will ensue.
        
        try {
            _imagesSuccess.put( DEFAULT, fetch( _baseImagePath + _success ) );
            _imagesFailure.put( DEFAULT, fetch( _baseImagePath + _failure ) );
        }
        catch (Exception e) {
            //  Add error handling.
            
            _logger.debug( _baseImagePath + _success, e );
        }
    }
    
    //  **********************************************************************
    //  Lookup an image in our success/failure hashmap based on the
    //  key which will either be DEFAULT or the SID of a requestor.
    //  Return a byte array, but also place the image in our hashmap
    //  if it does not already exist there.
    
    private byte[] lookupImage( String imageType, String key )
    {
        byte[] image = null;
        String imageName = "";
        int successOrFailure = (IMAGE_SUCCESS.equals( imageType )) ? 1 : 0;
        
        //  Is our image in our hashmap[s]?
        
        switch (successOrFailure) {
            case 0: 
                image = _imagesFailure.get( key );
                imageName = _failure;
                break;
            case 1:
                image = _imagesSuccess.get( key );
                imageName = _success;
                break;
        }
        
        //  Look, if we tried to find the default image and could not, then
        //  we have problems.  Log it, return null to skip all the other
        //  image processing stuff that follows for non-default images.
        
        if (DEFAULT.equals( key ) && image == null) {
            _logger.error( "IBL LinkManager:  The Open URL Image Server could not find the default images!" );
            return null;
        }
        
        //  If we got this far we either have an image, OR we do NOT have
        //  an image but we are looking for a custom image for a given SID.
        
        if (image == null) {
            //  Looks like our image was not found.  Try again, this
            //  time load from file.
            
            StringBuffer path = new StringBuffer( _baseImagePath );
            path.append( key ).append( _pathSeparator ).append( imageName );
            
            try {
                image = fetch( path.toString() );
            }
            catch (IOException e) {
                //  Swallow this error.  Log it tho'.
                
                _logger.error( "IBL LinkManager:  Failed to load image file " 
                             + path.toString(), e );
            }
            
            //  If we got an image, we should stuff it into our hashmap.
            
            if (image != null) {
                switch (successOrFailure) {
                    case 0: _imagesFailure.put( key, image ); break;
                    case 1: _imagesSuccess.put( key, image ); break;
                }
            }
            else {
                //  If we STILL don't have anything, grab the default image.
                //  If that is null, there isn't anything else we can do 
                //  (it should already be preloaded).
            
                switch (successOrFailure) {
                    case 0: image = _imagesFailure.get( DEFAULT ); break;
                    case 1: image = _imagesSuccess.get( DEFAULT ); break;
                }
            }
        }
        
        return image;
    }
    
    //  **********************************************************************
    //  Load up our image as an array of bytes, we will eventually be writing
    //  it back to the requestor's browser.
    
    private byte[] fetch( String path )
        throws IOException
    {
        int i = 0;
        int x = 0;
        FileInputStream ifs = null;
        byte[] buf = null;

        //  Our image files are held remotely.  We might want
        //  to consider changing that, but they are accessible
        //  via a URL (http).
        
        try
        {
            //  Get our stream and drain it of bytes.
            
            ifs = new FileInputStream( path );

            long len = ifs.available();
            buf = new byte[(int) len];

            while ((x = ifs.read()) != -1)
            {
                buf[i++] = (byte) x;
            }

            //  Close up shop and return our image.

            ifs.close();
        }
        catch (FileNotFoundException e)
        {
            //  Swallow this one.
            
            _logger.warn( "IBL LinkManager:  Image path = " + path );
            _logger.warn( "IBL LinkManager:  Invalid path", e );
        }
        
        return buf;
    }
    
    //  **********************************************************************
    //  Pick issn or isbn that will be used in our search.
    
    private void pickIdno( HttpServletRequest request, String issn, String isbn )
    {    
        //  Did we get an ISSN or an ISBN?  Either way
        //  it becomes our standard number, idno_wop.

        if ((isbn != null) && !"".equals( isbn ))
        {
            request.setAttribute( IDNO_WOP, strip( isbn ) );
        }
        else if ((issn != null) && !"".equals( issn ))
        {
            request.setAttribute( IDNO_WOP, strip( issn ) );
        }
    }
    
    private String getKey( String sid )
    {
        String key = DEFAULT;
        
        if ((sid != null) && !EMPTY_STRING.equals( sid ))
        {
            String vendorIDAndDatabaseID[] = sid.split( ":" );
            key = vendorIDAndDatabaseID[0].toUpperCase();
        }
        
        return key;
    }
    
    //  **********************************************************************
    //  We could be handed our year in one of several different formats.  We
    //  want our year to simply be in the YYYY format.
    
    private void parseYear( HttpServletRequest request, String year )
    {
        Long   pubYear = null;
        String newYear = year;
        
        if ((newYear == null) || (newYear.equals(""))) {
            pubYear = 0L;
        }
        else {
            if (newYear.length() > 4) {
                //  According to the OpenURL standards, the date
                //  must come in one of the following formats:
                //  YYYY, YYYY-MM, YYYY-MM-DD.
                
                newYear = newYear.substring( 0, 4 );
            }
            
            if ((newYear != null) && !newYear.equals( "" )) {
                pubYear = new Long( newYear );
            }
        }
        request.setAttribute( DATE, pubYear );
    }
    
    //  **********************************************************************
    //  The user can specify one or more products they want to search for results.
    //  We map permissions by product, so this is really necessary.
    
    private void mapProduct( HttpServletRequest request, String products )
    {
        String p = null;
        String prd = null;
        String[] prds = null;
        String[] insts = null;
        Vector<Integer> mapped = new Vector<Integer>();
        
        //  If no products were specified, let us search ALL
        //  products... that will give us a better shot at meeting
        //  any specified criteria.
        
        if (products == null) {
            prds = new String[1];
            prds[0] = "all";
        }
        else {
            prds = products.split( "," );
        }
        
        //  We need to be able to process multiple items
        //  that might be specified on the URL.  Note:
        //  I do NOTHING to insure that a prd_inst is not
        //  repeated.  When querying for multiple products,
        //  I simply return ALL PRODUCTS and walk through them,
        //  so it does not really matter.
        
        for (int i = 0; i < prds.length; i++) {
            p = prds[i];
            prd = _products.get( p );
            
            if (prd != null) {
                insts = prd.split( ":" );

                for (int j = 0; j < insts.length; j++) {
                    mapped.add( new Integer( insts[j] ) );
                }
            }
        }
        request.setAttribute( PRODUCTS, mapped );
    }
    
    private void checkCriteria( HttpServletRequest request, String criterium )
    {
        Integer c = null;
        
        //  Map and/or default our criteria.
        
        if ((criterium != null) && !"".equals( criterium )) {
            c = new Integer( _criteria.get( criterium ) );
        }
        else {
            c = _defaultCriteria;
        }
        request.setAttribute( CRITERIUM, c );
    }
    
    //  **********************************************************************
    //  Remove unecessary elements of an issn/isbn handed to us by the caller.
    
    private String strip( String idno )
    {
        StringBuffer buf = new StringBuffer( 13 );

        for (int i = 0; i < idno.length(); i++) {
            switch ((int) idno.charAt( i )) {
                case 32 :
                case 39 :
                case 45 : break;
                default : buf.append( idno.charAt( i ) );
            }
        }
        return buf.toString();
    }
    
    //  **********************************************************************
    //  This is the meat of the action.  We take our parameters, perform a
    //  search, determine if we have a match with the provided parameters and
    //  return TRUE or FALSE.
    
    private boolean performSearch( HttpServletRequest request )
    {
        PublicationPermission[] permissions = null;
        boolean result = false;
        Iterator<Integer> iterator;
        String dateRange;
        int criterium = ((Integer) request.getAttribute( CRITERIUM )).intValue();
        int criteriaFound = 0;
        int compYear = 0;
        int yearLow = 0;
        int yearHigh = 0;
        int typeOfUse = 0;
        int count = 0;
        int i = 0;
        
        //  First test.  If our Criterium is 0 (that means ALWAYS) then
        //  we return true, because this means they always want the chance
        //  to go through our order process.  Otherwise we need to step
        //  through our publications/permissions.
        
        if (criterium != ALWAYS) {
            //  Perform our search.  We should not error out here but we still
            //  have to wrap it in an error handler.
        
            try {
            	String searchString = (String) request.getAttribute( IDNO_WOP );
                CCSearchCriteria ccSearchCriteria = new SearchCriteriaImpl();
                ccSearchCriteria.setTitleOrStdNo(searchString);
                ccSearchCriteria.setSearchType(CCSearchCriteria.BASIC_SEARCH_TYPE);
                ccSearchCriteria.setSortType(CCSearchCriteria.RELEVANCE_SORT);
                count = webServiceSearch.performBasicSearch(ccSearchCriteria,25);
                //count = webServiceSearch.performBasicSearch( (String) request.getAttribute( IDNO_WOP ) );
            }
            catch (Exception e) {
                _logger.error( "IBL LinkManager:  Search failed.", e );
            }
        
            //  Second short circuit.  If we got no results, obviously what the
            //  user requested is not available.
        
            if (count > 0) {
                //  Might as well pull the date now.  The compYear bit is necessary
                //  because the original logic is no longer 100% applicable because
                //  of the way we now search and return results.  I need two dates
                //  to use in the process.
                
                int year = ((Long) request.getAttribute( DATE )).intValue();
                
                if (year == 0) {
                    GregorianCalendar gc = new GregorianCalendar();
                    compYear = gc.get( Calendar.YEAR );
                }
                else compYear = year;
            
                //  Looks like we got something.  Return our results.  
        
                Publication[] publications = webServiceSearch.getResultsWindow(1, count);
                @SuppressWarnings("unchecked")
                Vector<Integer> typesOfUse = (Vector<Integer>) request.getAttribute( PRODUCTS );

                //  Label our loop for a quick getaway.  We want to do as little
                //  as possible, time == money.

                check: while (i++ < count) {
                    iterator = typesOfUse.iterator();

                    //  The beauty of this inner loop (at least compared to the
                    //  original servlet I wrote) is that we can query permissions
                    //  based on the types of use passed to us by the client.
                    //  This allows us to also take advantage of a method that
                    //  returns the most permissive result first, so we know we
                    //  don't have to check ALL the permissions, just the first
                    //  one we get back.

                    while (iterator.hasNext()) {
                        typeOfUse = iterator.next().intValue();
                        
                        //  We can, if no year was passed to us, short circuit the
                        //  permission checking process since we have an actual 
                        //  publication and all the caller is concerned with is
                        //  the fact that the publication is in our database
                        
                        if (year == 0) {
                            if (!publications[i-1].isBiactive( typeOfUse ) && (criterium == EXISTS)) {
                                result = true;
                                break check;
                            }
                        }
                        permissions = publications[i-1].getPublicationPermissionsByMostPermissable( typeOfUse );
                        
                        if (permissions != null) {
                            //  Since this returns the most permissable right first, this 
                            //  will make it easier to perform our comparison.  We just grab 
                            //  that one and we should be good (theoretically...  so long as
                            //  dates line up).
                        
                            dateRange = permissions[BEST].getPermDateRange();
                            yearLow = parseYearLow( dateRange );
                            yearHigh = parseYearHigh( dateRange );
                        
                            if ((compYear >= yearLow) && (compYear <= yearHigh)) {
                                //  This should be somewhat efficient using the switch statement to
                                //  drop through my checks as far as I can... then when I loop, I
                                //  might be able to perform one less check.

                                switch (criteriaFound)
                                {
                                    case 0: criteriaFound = 1;
                                    case 1: if (!permissions[0].isDenied()) criteriaFound <<= 1;
                                    case 2: 
                                        if (permissions[0].isAvailable()) criteriaFound <<= 1;
                                        break;
                                }
                            }
                        }
                        //  We may already have found a right that allows us to return
                        //  to the user.  If so, get out now, save some processing time.
                        
                        if (criterium <= criteriaFound) {
                            result = true;
                            break check;
                        }
                    }
                }
            }
        }
        else {
            result = true;
        }
        return result;
    }
    
    //  **********************************************************************
    //  Our date ranges have the format [[yyyy][-][yyyy]]
    //  If the date is a null string, we return the extremes of 0 and 9999.
    //  If a "-" is provided, we do the same (return 0 and 9999).
    //  Any other case and we extract the relative date for the low and
    //  the high.  For example 1922-1999 will return 1922 and 1999
    //  respectively.  -1999 will return 0 and 1999, and 1922- will
    //  return 1922 and 9999.
    
    private int parseYearLow( String range )
    {
        int year = 0;
        if (range == null) return year;
        String r = squash( range );
        
        int rlen = r.length();
        int dash = r.indexOf( "-" );

        if ((rlen > 0) && (dash > 0))
        {
            //  Note:  The end index in the substring method is the value you specify - 1.
            //  Our year has the format YYYY-[YYYY].
            
            year = new Integer( r.substring( 0, dash ) ).intValue();
        }
        if ((rlen > 0) && (dash < 0))
        {
            //  Our year has the format YYYY.
            
            year = new Integer( r ).intValue();
        }
        return year;
    }
    
    private int parseYearHigh( String range )
    {
        int year = 9999;
        if (range == null) return year;
        String r = squash( range );
        
        int rlen = r.length();
        int dash = r.indexOf( "-" );
        
        if ((dash > 0) && (rlen > (dash + 1)))
        {
            //  Our year has the format [YYYY]-YYYY.
            
            year = new Integer( r.substring( dash + 1 ) ).intValue();
        }
        if ((rlen > 0) && (dash < 0))
        {
            //  Our range has the format YYYY.
            
            year = new Integer( r ).intValue();
        }
        return year;
    }
    
    private String squash(String range) {
        if (range.length() == 0) return "0-9999";
        
        String r = range.replace(" ", "");
        
        if (r.indexOf("Through") >= 0) {
            r = "0-" + r.substring(r.indexOf("Through") + 7);
        }
        if (r.indexOf("present") >= 0) {
            r = r.substring(0, r.indexOf("present")) + "9999";
        }
        return r;
    }
}
