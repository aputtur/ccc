package com.mindprod.entities;

import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 * <pre>
 *                 Strips HTML entities such as &quot; from a file, replacing
 * them by their
 *                 Unicode equivalents. Methods can be used on text strings as
 * well. Does not
 *                 strip Tags, just Entities. No longer requires
 * entitiestochar.ser in the jar!
 * <p/>
 *                 @author Roedy Green
 *                 @version 1.6
 *                 @since 2002 July 14 version
 * <p/>
 *                 version 1.0 - initial version
 * <p/>
 *                 version 1.1 - optimise using
 *                        text.indexOf('&amp;') and sb.append(string) rather
 * than processing
 *                        character by character.
 * <p/>
 *                 version 1.2 2004-07-21 - add stripHTMLTags -
 *                        stripFile also strips tags - add stripNbsp
 * <p/>
 *                 version 1.3 2005-06-20 - fix bug in possEntityToChar
 *                        - exposed possEntityToChar as public
 * <p/>
 *                 Version 1.4 2005-07-02 - check for null input
 * <p/>
 *                 Version 1.5 2005-07-29 - no longer needs entitiestochar.ser
 * file.
 *                        Converted to JDK 1.5 back to 1,2
 *                 Version 1.6 2005-09-05 - faster code for stripHTMLTags that
 * returns
 *                        original string if nothing changed.
 *                 Version 1.8 2007-02-26 - fix bug. hex entitly it &#xffff;
 * not
 * &x#ffff;
 *                 Version 2.2 2007-05-14 - StripHTMLTags now strips applet,
 * style, script pairs.
 * </pre>
 */
public class StripEntities {

    // ------------------------------ FIELDS ------------------------------

	protected static final Logger _logger = Logger.getLogger( StripEntities.class );
    /**
     * true to enable the testing code.
     */
    private static final boolean DEBUGGING = true;

    /**
     * Longest an entity can be {@value #LONGEST_ENTITY}, at least in our
     * tables, including the lead & and trail ;.
     *
     * @noinspection WeakerAccess,JavadocReference,WeakerAccess
     */
    public static final int LONGEST_ENTITY = 10;/* &thetasym; */

    /**
     * The shortest an entity can be {@value #SHORTEST_ENTITY}, at least in our
     * tables, including the lead & and trailing ;.
     *
     * @noinspection WeakerAccess,JavadocReference,WeakerAccess
     */
    public static final int SHORTEST_ENTITY = 4;/* &#1; &lt; */

    /**
     * allows lookup by entity name, to get the corresponding char.
     *
     * @noinspection CanBeFinal
     */
    private static HashMap<String,Character> entityToChar;

    // -------------------------- PUBLIC STATIC METHODS --------------------------

    /**
     * convert an entity to a single char.
     *
     * @param entity String entity to convert convert. must have lead & and
     *               trail ; stripped; may be a #x12ff or #123 style entity.
     *               Works faster if entity in lower case.
     *
     * @return equivalent character. 0 if not recognised.
     *
     * @noinspection WeakerAccess
     */
    public static char entityToChar( String entity )
        {
        // first check for alpha entity
        Character code = entityToChar.get( entity );
        if ( code != null )
            {
            return code.charValue();
            }
        code = entityToChar.get( entity.toLowerCase() );
        if ( code != null )
            {
            return code.charValue();
            }
        // check at least have &_#1_;  (no & or ; at this point )
        if ( entity.length() < 2 )
            {
            return 0;
            }
        try
            {
            if ( entity.charAt( 0 ) == '#' )
                {
                final char secondChar = entity.charAt( 1 );
                if ( secondChar == 'x' || secondChar == 'X' )
                    {
                    // handle hex entities  of form  &_#x12ff_;
                    // ensure at least have &_#xf_;
                    if ( entity.length() < 3 )
                        {
                        return 0;
                        }
                    // had &_#x123D_;
                    return (char) Integer.parseInt( entity.substring( 2 ),
                                                    /* hex */
                                                    16 );
                    }
                else
                    {
                    // handle decimal entities
                    // had &_#123_;
                    return (char) Integer.parseInt( entity.substring( 1 ) );
                    }
                }
            else
                {
                // some unrecognized/malformed entity
                return 0;
                }
            }
        catch ( NumberFormatException e )
            {
            return 0;
            }
        }// end entityToChar

    /**
     * Checks a number of gauntlet conditions to ensure this is a valid entity.
     * Converts Entity to corresponding char.
     *
     * @param possEntity string that may hold an entity. Lead & must be
     *                   stripped, but may contain text past the ;
     *
     * @return corresponding unicode character, or 0 if the entity is invalid.
     *
     * @noinspection WeakerAccess
     */
    public static char possEntityToChar( String possEntity )
        {
        if ( possEntity.length() < SHORTEST_ENTITY - 1 )
            {
            return 0;
            }

        // find the trailing ;
        int whereSemi = possEntity
                .indexOf( ';', SHORTEST_ENTITY - 2/* where start looking */ );
        if ( whereSemi < SHORTEST_ENTITY - 2 )
            {
            return 0;
            }

        // we found a potential entity, at least it has &xxxx;
        // lead & already stripped, now strip trailing ;
        // and look it up in a table.
        // Will return 0 for an invalid entity.
        return entityToChar( possEntity.substring( 0, whereSemi ) );
        }// end possEntityToChar

    /**
     * Converts HTML to text converting entities such as &quot; back to " and
     * &lt; back to &lt; Ordinary text passes unchanged.
     *
     * @param text raw text to be processed. Must not be null.
     *
     * @return translated text. It also handles HTML 4.0 entities such as
     *         &hearts; &#123; and &#xffff; &nbsp; -> 160. null input returns
     *         null.
     *
     * @noinspection WeakerAccess
     */
    public static String stripEntities( String text )
        {
        if ( text == null )
            {
            return null;
            }
        if ( text.indexOf( '&' ) < 0 )
            {
            // are no entities, nothing to do
            return text;
            }
        int originalTextLength = text.length();
        StringBuffer sb = new StringBuffer( originalTextLength );
        for ( int i = 0; i < originalTextLength; i++ )
            {
            int whereAmp = text.indexOf( '&', i );
            if ( whereAmp < 0 )
                {
                // no more &s, we are done
                // append all remaining text
                sb.append( text.substring( i ) );
                break;
                }
            else
                {
                // append all text to left of next &
                sb.append( text.substring( i, whereAmp ) );
                // avoid reprocessing those chars
                i = whereAmp;
                // text.charAt(i) is an &
                // possEntity has lead & stripped.
                String possEntity =
                        text.substring( i + 1,
                                        Math.min( i + LONGEST_ENTITY,
                                                  text.length() ) );
                char t = possEntityToChar( possEntity );
                if ( t != 0 )
                    {
                    // was a good entity, keep its equivalent char.
                    sb.append( t );
                    // avoid reprocessing chars forming the entity
                    int whereSemi =
                            possEntity.indexOf( ";", SHORTEST_ENTITY - 2 );
                    i += whereSemi + 1;
                    }
                else
                    {
                    // treat & just as ordinary character
                    sb.append( '&' );
                    }
                }// end else
            }// end for
        // if result is not shorter, we did not do anything. Saves RAM.
        return ( sb.length() == originalTextLength ) ? text : sb.toString();
        }// end stripEntities

    /**
     * Removes tags from HTML leaving just the raw text. Leaves entities as is,
     * e.g. does not convert &amp; back to &. similar to code in Quoter. Also
     * removes &lt;!-- --&gt; comments. Presumes perfectly formed HTML, no &gt;
     * in comments, all &lt;...&gt; balanced. Also removes text between applet,
     * style and script tag pairs.
     *
     * @param html input HTML
     *
     * @return raw text, with whitespaces collapsed to a single space, trimmed.
     *
     * @noinspection WeakerAccess
     */
    public static String stripHTMLTags( String html )
        {
        html = stripHTMLTagPairs( html );
        html = html.trim();
        int numChars = html.length();
        StringBuffer result = new StringBuffer( numChars );
        /**
         * are we inside a tag
         */
        boolean inside = false;
        /**
         * Have we cleaned any White Space?
         */
        boolean cleanedAnyWhitespace = false;
        /**
         * Was the last char we saw a space? We use this to collapse spaces.
         */
        boolean lastCharSpace = false;
        for ( int i = 0; i < numChars; i++ )
            {
            char c = html.charAt( i );
            switch ( c )
                {
                default:
                    if ( c < ' ' )
                        {
                        // handle stray whitespace
                        if ( !inside )
                            {
                            lastCharSpace = true;
                            cleanedAnyWhitespace = true;
                            }
                        }
                    else
                        {
                        // ordinary character, ignored inside a tag
                        if ( !inside )
                            {
                            if ( lastCharSpace )
                                {
                                // deal with pending whitespace
                                result.append( ' ' );
                                lastCharSpace = false;
                                }
                            result.append( c );
                            }
                        }
                    break;

                case'<':
                    inside = true;
                    // ignore
                    break;

                case'>':
                    inside = false;
                    // ignore
                    break;

                case' ':
                    if ( !inside )
                        {
                        lastCharSpace = true;
                        }
                    break;

                    // whitespace
                case'\r':
                case'\t':
                case'\n':
                case 127:
                case 160:

                    if ( !inside )
                        {
                        lastCharSpace = true;
                        cleanedAnyWhitespace = true;
                        }
                    break;
                }
            }// end for
        // return original string trimmed if we did not really change anything
        return ( cleanedAnyWhitespace || result.length() != numChars ) ? result
                .toString().trim() : html;
        }

    /**
     * converts all 160-style spaces (result of stripEntities on &nbsp;) to
     * ordinary space.
     *
     * @param text Text to convert
     *
     * @return Text with 160-style spaces converted to ordinary spaces
     */
    public static String stripNbsp( String text )
        {
        return text.replace( (char) 160, ' ' );
        }

    // -------------------------- STATIC METHODS --------------------------

    static
        {
        // build HashMap to look up entity name to get corresponding Unicode
        // char number. Following code generated by Entities.
        String[] entityKeys = {
                "quot"
                /*   34 : quotation mark */,
                "amp"
                /*   38 : ampersand */,
                "lt"
                /*   60 : less-than sign */,
                "gt"
                /*   62 : greater-than sign */,
                "nbsp"
                /*  160 : no-break space */,
                "iexcl"
                /*  161 : inverted exclamation mark */,
                "cent"
                /*  162 : cent sign */,
                "pound"
                /*  163 : pound sign */,
                "curren"
                /*  164 : currency sign */,
                "yen"
                /*  165 : yen sign */,
                "brvbar"
                /*  166 : broken bar */,
                "sect"
                /*  167 : section sign */,
                "uml"
                /*  168 : diaeresis */,
                "copy"
                /*  169 : copyright sign */,
                "ordf"
                /*  170 : feminine ordinal indicator */,
                "laquo"
                /*  171 : left-pointing double angle quotation mark */,
                "not"
                /*  172 : not sign */,
                "shy"
                /*  173 : soft hyphen */,
                "reg"
                /*  174 : registered sign */,
                "macr"
                /*  175 : macron */,
                "deg"
                /*  176 : degree sign */,
                "plusmn"
                /*  177 : plus-minus sign */,
                "sup2"
                /*  178 : superscript two */,
                "sup3"
                /*  179 : superscript three */,
                "acute"
                /*  180 : acute accent */,
                "micro"
                /*  181 : micro sign */,
                "para"
                /*  182 : pilcrow sign */,
                "middot"
                /*  183 : middle dot */,
                "cedil"
                /*  184 : cedilla */,
                "sup1"
                /*  185 : superscript one */,
                "ordm"
                /*  186 : masculine ordinal indicator */,
                "raquo"
                /*  187 : right-pointing double angle quotation mark */,
                "frac14"
                /*  188 : vulgar fraction one quarter */,
                "frac12"
                /*  189 : vulgar fraction one half */,
                "frac34"
                /*  190 : vulgar fraction three quarters */,
                "iquest"
                /*  191 : inverted question mark */,
                "Agrave"
                /*  192 : Latin capital letter a with grave */,
                "Aacute"
                /*  193 : Latin capital letter a with acute */,
                "Acirc"
                /*  194 : Latin capital letter a with circumflex */,
                "Atilde"
                /*  195 : Latin capital letter a with tilde */,
                "Auml"
                /*  196 : Latin capital letter a with diaeresis */,
                "Aring"
                /*  197 : Latin capital letter a with ring above */,
                "AElig"
                /*  198 : Latin capital letter ae */,
                "Ccedil"
                /*  199 : Latin capital letter c with cedilla */,
                "Egrave"
                /*  200 : Latin capital letter e with grave */,
                "Eacute"
                /*  201 : Latin capital letter e with acute */,
                "Ecirc"
                /*  202 : Latin capital letter e with circumflex */,
                "Euml"
                /*  203 : Latin capital letter e with diaeresis */,
                "Igrave"
                /*  204 : Latin capital letter i with grave */,
                "Iacute"
                /*  205 : Latin capital letter i with acute */,
                "Icirc"
                /*  206 : Latin capital letter i with circumflex */,
                "Iuml"
                /*  207 : Latin capital letter i with diaeresis */,
                "ETH"
                /*  208 : Latin capital letter eth */,
                "Ntilde"
                /*  209 : Latin capital letter n with tilde */,
                "Ograve"
                /*  210 : Latin capital letter o with grave */,
                "Oacute"
                /*  211 : Latin capital letter o with acute */,
                "Ocirc"
                /*  212 : Latin capital letter o with circumflex */,
                "Otilde"
                /*  213 : Latin capital letter o with tilde */,
                "Ouml"
                /*  214 : Latin capital letter o with diaeresis */,
                "times"
                /*  215 : multiplication sign */,
                "Oslash"
                /*  216 : Latin capital letter o with stroke */,
                "Ugrave"
                /*  217 : Latin capital letter u with grave */,
                "Uacute"
                /*  218 : Latin capital letter u with acute */,
                "Ucirc"
                /*  219 : Latin capital letter u with circumflex */,
                "Uuml"
                /*  220 : Latin capital letter u with diaeresis */,
                "Yacute"
                /*  221 : Latin capital letter y with acute */,
                "THORN"
                /*  222 : Latin capital letter thorn */,
                "szlig"
                /*  223 : Latin small letter sharp s, German Eszett */,
                "agrave"
                /*  224 : Latin small letter a with grave */,
                "aacute"
                /*  225 : Latin small letter a with acute */,
                "acirc"
                /*  226 : Latin small letter a with circumflex */,
                "atilde"
                /*  227 : Latin small letter a with tilde */,
                "auml"
                /*  228 : Latin small letter a with diaeresis */,
                "aring"
                /*  229 : Latin small letter a with ring above */,
                "aelig"
                /*  230 : Latin lowercase ligature ae */,
                "ccedil"
                /*  231 : Latin small letter c with cedilla */,
                "egrave"
                /*  232 : Latin small letter e with grave */,
                "eacute"
                /*  233 : Latin small letter e with acute */,
                "ecirc"
                /*  234 : Latin small letter e with circumflex */,
                "euml"
                /*  235 : Latin small letter e with diaeresis */,
                "igrave"
                /*  236 : Latin small letter i with grave */,
                "iacute"
                /*  237 : Latin small letter i with acute */,
                "icirc"
                /*  238 : Latin small letter i with circumflex */,
                "iuml"
                /*  239 : Latin small letter i with diaeresis */,
                "eth"
                /*  240 : Latin small letter eth */,
                "ntilde"
                /*  241 : Latin small letter n with tilde */,
                "ograve"
                /*  242 : Latin small letter o with grave */,
                "oacute"
                /*  243 : Latin small letter o with acute */,
                "ocirc"
                /*  244 : Latin small letter o with circumflex */,
                "otilde"
                /*  245 : Latin small letter o with tilde */,
                "ouml"
                /*  246 : Latin small letter o with diaeresis */,
                "divide"
                /*  247 : division sign */,
                "oslash"
                /*  248 : Latin small letter o with stroke */,
                "ugrave"
                /*  249 : Latin small letter u with grave */,
                "uacute"
                /*  250 : Latin small letter u with acute */,
                "ucirc"
                /*  251 : Latin small letter u with circumflex */,
                "uuml"
                /*  252 : Latin small letter u with diaeresis */,
                "yacute"
                /*  253 : Latin small letter y with acute */,
                "thorn"
                /*  254 : Latin small letter thorn */,
                "yuml"
                /*  255 : Latin small letter y with diaeresis */,
                "OElig"
                /*  338 : Latin capital ligature oe */,
                "oelig"
                /*  339 : Latin small ligature oe */,
                "Scaron"
                /*  352 : Latin capital letter s with caron */,
                "scaron"
                /*  353 : Latin small letter s with caron */,
                "Yuml"
                /*  376 : Latin capital letter y with diaeresis */,
                "fnof"
                /*  402 : Latin small letter f with hook */,
                "circ"
                /*  710 : modifier letter circumflex accent */,
                "tilde"
                /*  732 : small tilde */,
                "Alpha"
                /*  913 : Greek capital letter alpha */,
                "Beta"
                /*  914 : Greek capital letter beta */,
                "Gamma"
                /*  915 : Greek capital letter gamma */,
                "Delta"
                /*  916 : Greek capital letter delta */,
                "Epsilon"
                /*  917 : Greek capital letter epsilon */,
                "Zeta"
                /*  918 : Greek capital letter zeta */,
                "Eta"
                /*  919 : Greek capital letter eta */,
                "Theta"
                /*  920 : Greek capital letter theta */,
                "Iota"
                /*  921 : Greek capital letter iota */,
                "Kappa"
                /*  922 : Greek capital letter kappa */,
                "Lambda"
                /*  923 : Greek capital letter lambda */,
                "Mu"
                /*  924 : Greek capital letter mu */,
                "Nu"
                /*  925 : Greek capital letter nu */,
                "Xi"
                /*  926 : Greek capital letter xi */,
                "Omicron"
                /*  927 : Greek capital letter omicron */,
                "Pi"
                /*  928 : Greek capital letter pi */,
                "Rho"
                /*  929 : Greek capital letter rho */,
                "Sigma"
                /*  931 : Greek capital letter sigma */,
                "Tau"
                /*  932 : Greek capital letter tau */,
                "Upsilon"
                /*  933 : Greek capital letter upsilon */,
                "Phi"
                /*  934 : Greek capital letter phi */,
                "Chi"
                /*  935 : Greek capital letter chi */,
                "Psi"
                /*  936 : Greek capital letter psi */,
                "Omega"
                /*  937 : Greek capital letter omega */,
                "alpha"
                /*  945 : Greek small letter alpha */,
                "beta"
                /*  946 : Greek small letter beta */,
                "gamma"
                /*  947 : Greek small letter gamma */,
                "delta"
                /*  948 : Greek small letter delta */,
                "epsilon"
                /*  949 : Greek small letter epsilon */,
                "zeta"
                /*  950 : Greek small letter zeta */,
                "eta"
                /*  951 : Greek small letter eta */,
                "theta"
                /*  952 : Greek small letter theta */,
                "iota"
                /*  953 : Greek small letter iota */,
                "kappa"
                /*  954 : Greek small letter kappa */,
                "lambda"
                /*  955 : Greek small letter lambda */,
                "mu"
                /*  956 : Greek small letter mu */,
                "nu"
                /*  957 : Greek small letter nu */,
                "xi"
                /*  958 : Greek small letter xi */,
                "omicron"
                /*  959 : Greek small letter omicron */,
                "pi"
                /*  960 : Greek small letter pi */,
                "rho"
                /*  961 : Greek small letter rho */,
                "sigmaf"
                /*  962 : Greek small letter final sigma */,
                "sigma"
                /*  963 : Greek small letter sigma */,
                "tau"
                /*  964 : Greek small letter tau */,
                "upsilon"
                /*  965 : Greek small letter upsilon */,
                "phi"
                /*  966 : Greek small letter phi */,
                "chi"
                /*  967 : Greek small letter chi */,
                "psi"
                /*  968 : Greek small letter psi */,
                "omega"
                /*  969 : Greek small letter omega */,
                "thetasym"
                /*  977 : Greek theta symbol */,
                "upsih"
                /*  978 : Greek upsilon with hook symbol */,
                "piv"
                /*  982 : Greek pi symbol */,
                "ensp"
                /* 8194 : en space */,
                "emsp"
                /* 8195 : em space */,
                "thinsp"
                /* 8201 : thin space */,
                "zwnj"
                /* 8204 : zero width non-joiner */,
                "zwj"
                /* 8205 : zero width joiner */,
                "lrm"
                /* 8206 : left-to-right mark */,
                "rlm"
                /* 8207 : right-to-left mark */,
                "ndash"
                /* 8211 : en dash */,
                "mdash"
                /* 8212 : em dash */,
                "lsquo"
                /* 8216 : left single quotation mark */,
                "rsquo"
                /* 8217 : right single quotation mark */,
                "sbquo"
                /* 8218 : single low-9 quotation mark */,
                "ldquo"
                /* 8220 : left double quotation mark */,
                "rdquo"
                /* 8221 : right double quotation mark */,
                "bdquo"
                /* 8222 : double low-9 quotation mark */,
                "dagger"
                /* 8224 : dagger */,
                "Dagger"
                /* 8225 : double dagger */,
                "bull"
                /* 8226 : bullet */,
                "hellip"
                /* 8230 : horizontal ellipsis */,
                "permil"
                /* 8240 : per mille sign */,
                "prime"
                /* 8242 : prime */,
                "Prime"
                /* 8243 : double prime */,
                "lsaquo"
                /* 8249 : single left-pointing angle quotation mark */,
                "rsaquo"
                /* 8250 : single right-pointing angle quotation mark */,
                "oline"
                /* 8254 : overline */,
                "frasl"
                /* 8260 : fraction slash */,
                "euro"
                /* 8364 : euro sign */,
                "image"
                /* 8465 : black-letter capital i */,
                "weierp"
                /* 8472 : script capital p, Weierstrass p */,
                "real"
                /* 8476 : black-letter capital r */,
                "trade"
                /* 8482 : trademark sign */,
                "alefsym"
                /* 8501 : alef symbol */,
                "larr"
                /* 8592 : leftwards arrow */,
                "uarr"
                /* 8593 : upwards arrow */,
                "rarr"
                /* 8594 : rightwards arrow */,
                "darr"
                /* 8595 : downwards arrow */,
                "harr"
                /* 8596 : left right arrow */,
                "crarr"
                /* 8629 : downwards arrow with corner leftwards */,
                "lArr"
                /* 8656 : leftwards double arrow */,
                "uArr"
                /* 8657 : upwards double arrow */,
                "rArr"
                /* 8658 : rightwards double arrow */,
                "dArr"
                /* 8659 : downwards double arrow */,
                "hArr"
                /* 8660 : left right double arrow */,
                "forall"
                /* 8704 : for all */,
                "part"
                /* 8706 : partial differential */,
                "exist"
                /* 8707 : there exists */,
                "empty"
                /* 8709 : empty set */,
                "nabla"
                /* 8711 : nabla */,
                "isin"
                /* 8712 : element of */,
                "notin"
                /* 8713 : not an element of */,
                "ni"
                /* 8715 : contains as member */,
                "prod"
                /* 8719 : n-ary product */,
                "sum"
                /* 8721 : n-ary summation */,
                "minus"
                /* 8722 : minus sign */,
                "lowast"
                /* 8727 : asterisk operator */,
                "radic"
                /* 8730 : square root */,
                "prop"
                /* 8733 : proportional to */,
                "infin"
                /* 8734 : infinity */,
                "ang"
                /* 8736 : angle */,
                "and"
                /* 8743 : logical and */,
                "or"
                /* 8744 : logical or */,
                "cap"
                /* 8745 : intersection */,
                "cup"
                /* 8746 : union */,
                "int"
                /* 8747 : integral */,
                "there4"
                /* 8756 : therefore */,
                "sim"
                /* 8764 : tilde operator */,
                "cong"
                /* 8773 : congruent to */,
                "asymp"
                /* 8776 : almost equal to */,
                "ne"
                /* 8800 : not equal to */,
                "equiv"
                /* 8801 : identical to, equivalent to */,
                "le"
                /* 8804 : less-than or equal to */,
                "ge"
                /* 8805 : greater-than or equal to */,
                "sub"
                /* 8834 : subset of */,
                "sup"
                /* 8835 : superset of */,
                "nsub"
                /* 8836 : not a subset of */,
                "sube"
                /* 8838 : subset of or equal to */,
                "supe"
                /* 8839 : superset of or equal to */,
                "oplus"
                /* 8853 : circled plus */,
                "otimes"
                /* 8855 : circled times */,
                "perp"
                /* 8869 : up tack */,
                "sdot"
                /* 8901 : dot operator */,
                "lceil"
                /* 8968 : left ceiling */,
                "rceil"
                /* 8969 : right ceiling */,
                "lfloor"
                /* 8970 : left floor */,
                "rfloor"
                /* 8971 : right floor */,
                "lang"
                /* 9001 : left-pointing angle bracket */,
                "rang"
                /* 9002 : right-pointing angle bracket */,
                "loz"
                /* 9674 : lozenge */,
                "spades"
                /* 9824 : black spade suit */,
                "clubs"
                /* 9827 : black club suit */,
                "hearts"
                /* 9829 : black heart suit */,
                "diams"
                /* 9830 : black diamond suit */,};
        char[] entityValues = {
                34
                /* &quot;     : quotation mark */,
                38
                /* &amp;      : ampersand */,
                60
                /* &lt;       : less-than sign */,
                62
                /* &gt;       : greater-than sign */,
                160
                /* &nbsp;     : no-break space */,
                161
                /* &iexcl;    : inverted exclamation mark */,
                162
                /* &cent;     : cent sign */,
                163
                /* &pound;    : pound sign */,
                164
                /* &curren;   : currency sign */,
                165
                /* &yen;      : yen sign */,
                166
                /* &brvbar;   : broken bar */,
                167
                /* &sect;     : section sign */,
                168
                /* &uml;      : diaeresis */,
                169
                /* &copy;     : copyright sign */,
                170
                /* &ordf;     : feminine ordinal indicator */,
                171
                /* &laquo;    : left-pointing double angle quotation mark */,
                172
                /* &not;      : not sign */,
                173
                /* &shy;      : soft hyphen */,
                174
                /* &reg;      : registered sign */,
                175
                /* &macr;     : macron */,
                176
                /* &deg;      : degree sign */,
                177
                /* &plusmn;   : plus-minus sign */,
                178
                /* &sup2;     : superscript two */,
                179
                /* &sup3;     : superscript three */,
                180
                /* &acute;    : acute accent */,
                181
                /* &micro;    : micro sign */,
                182
                /* &para;     : pilcrow sign */,
                183
                /* &middot;   : middle dot */,
                184
                /* &cedil;    : cedilla */,
                185
                /* &sup1;     : superscript one */,
                186
                /* &ordm;     : masculine ordinal indicator */,
                187
                /* &raquo;    : right-pointing double angle quotation mark */,
                188
                /* &frac14;   : vulgar fraction one quarter */,
                189
                /* &frac12;   : vulgar fraction one half */,
                190
                /* &frac34;   : vulgar fraction three quarters */,
                191
                /* &iquest;   : inverted question mark */,
                192
                /* &Agrave;   : Latin capital letter a with grave */,
                193
                /* &Aacute;   : Latin capital letter a with acute */,
                194
                /* &Acirc;    : Latin capital letter a with circumflex */,
                195
                /* &Atilde;   : Latin capital letter a with tilde */,
                196
                /* &Auml;     : Latin capital letter a with diaeresis */,
                197
                /* &Aring;    : Latin capital letter a with ring above */,
                198
                /* &AElig;    : Latin capital letter ae */,
                199
                /* &Ccedil;   : Latin capital letter c with cedilla */,
                200
                /* &Egrave;   : Latin capital letter e with grave */,
                201
                /* &Eacute;   : Latin capital letter e with acute */,
                202
                /* &Ecirc;    : Latin capital letter e with circumflex */,
                203
                /* &Euml;     : Latin capital letter e with diaeresis */,
                204
                /* &Igrave;   : Latin capital letter i with grave */,
                205
                /* &Iacute;   : Latin capital letter i with acute */,
                206
                /* &Icirc;    : Latin capital letter i with circumflex */,
                207
                /* &Iuml;     : Latin capital letter i with diaeresis */,
                208
                /* &ETH;      : Latin capital letter eth */,
                209
                /* &Ntilde;   : Latin capital letter n with tilde */,
                210
                /* &Ograve;   : Latin capital letter o with grave */,
                211
                /* &Oacute;   : Latin capital letter o with acute */,
                212
                /* &Ocirc;    : Latin capital letter o with circumflex */,
                213
                /* &Otilde;   : Latin capital letter o with tilde */,
                214
                /* &Ouml;     : Latin capital letter o with diaeresis */,
                215
                /* &times;    : multiplication sign */,
                216
                /* &Oslash;   : Latin capital letter o with stroke */,
                217
                /* &Ugrave;   : Latin capital letter u with grave */,
                218
                /* &Uacute;   : Latin capital letter u with acute */,
                219
                /* &Ucirc;    : Latin capital letter u with circumflex */,
                220
                /* &Uuml;     : Latin capital letter u with diaeresis */,
                221
                /* &Yacute;   : Latin capital letter y with acute */,
                222
                /* &THORN;    : Latin capital letter thorn */,
                223
                /* &szlig;    : Latin small letter sharp s, German Eszett */,
                224
                /* &agrave;   : Latin small letter a with grave */,
                225
                /* &aacute;   : Latin small letter a with acute */,
                226
                /* &acirc;    : Latin small letter a with circumflex */,
                227
                /* &atilde;   : Latin small letter a with tilde */,
                228
                /* &auml;     : Latin small letter a with diaeresis */,
                229
                /* &aring;    : Latin small letter a with ring above */,
                230
                /* &aelig;    : Latin lowercase ligature ae */,
                231
                /* &ccedil;   : Latin small letter c with cedilla */,
                232
                /* &egrave;   : Latin small letter e with grave */,
                233
                /* &eacute;   : Latin small letter e with acute */,
                234
                /* &ecirc;    : Latin small letter e with circumflex */,
                235
                /* &euml;     : Latin small letter e with diaeresis */,
                236
                /* &igrave;   : Latin small letter i with grave */,
                237
                /* &iacute;   : Latin small letter i with acute */,
                238
                /* &icirc;    : Latin small letter i with circumflex */,
                239
                /* &iuml;     : Latin small letter i with diaeresis */,
                240
                /* &eth;      : Latin small letter eth */,
                241
                /* &ntilde;   : Latin small letter n with tilde */,
                242
                /* &ograve;   : Latin small letter o with grave */,
                243
                /* &oacute;   : Latin small letter o with acute */,
                244
                /* &ocirc;    : Latin small letter o with circumflex */,
                245
                /* &otilde;   : Latin small letter o with tilde */,
                246
                /* &ouml;     : Latin small letter o with diaeresis */,
                247
                /* &divide;   : division sign */,
                248
                /* &oslash;   : Latin small letter o with stroke */,
                249
                /* &ugrave;   : Latin small letter u with grave */,
                250
                /* &uacute;   : Latin small letter u with acute */,
                251
                /* &ucirc;    : Latin small letter u with circumflex */,
                252
                /* &uuml;     : Latin small letter u with diaeresis */,
                253
                /* &yacute;   : Latin small letter y with acute */,
                254
                /* &thorn;    : Latin small letter thorn */,
                255
                /* &yuml;     : Latin small letter y with diaeresis */,
                338
                /* &OElig;    : Latin capital ligature oe */,
                339
                /* &oelig;    : Latin small ligature oe */,
                352
                /* &Scaron;   : Latin capital letter s with caron */,
                353
                /* &scaron;   : Latin small letter s with caron */,
                376
                /* &Yuml;     : Latin capital letter y with diaeresis */,
                402
                /* &fnof;     : Latin small letter f with hook */,
                710
                /* &circ;     : modifier letter circumflex accent */,
                732
                /* &tilde;    : small tilde */,
                913
                /* &Alpha;    : Greek capital letter alpha */,
                914
                /* &Beta;     : Greek capital letter beta */,
                915
                /* &Gamma;    : Greek capital letter gamma */,
                916
                /* &Delta;    : Greek capital letter delta */,
                917
                /* &Epsilon;  : Greek capital letter epsilon */,
                918
                /* &Zeta;     : Greek capital letter zeta */,
                919
                /* &Eta;      : Greek capital letter eta */,
                920
                /* &Theta;    : Greek capital letter theta */,
                921
                /* &Iota;     : Greek capital letter iota */,
                922
                /* &Kappa;    : Greek capital letter kappa */,
                923
                /* &Lambda;   : Greek capital letter lambda */,
                924
                /* &Mu;       : Greek capital letter mu */,
                925
                /* &Nu;       : Greek capital letter nu */,
                926
                /* &Xi;       : Greek capital letter xi */,
                927
                /* &Omicron;  : Greek capital letter omicron */,
                928
                /* &Pi;       : Greek capital letter pi */,
                929
                /* &Rho;      : Greek capital letter rho */,
                931
                /* &Sigma;    : Greek capital letter sigma */,
                932
                /* &Tau;      : Greek capital letter tau */,
                933
                /* &Upsilon;  : Greek capital letter upsilon */,
                934
                /* &Phi;      : Greek capital letter phi */,
                935
                /* &Chi;      : Greek capital letter chi */,
                936
                /* &Psi;      : Greek capital letter psi */,
                937
                /* &Omega;    : Greek capital letter omega */,
                945
                /* &alpha;    : Greek small letter alpha */,
                946
                /* &beta;     : Greek small letter beta */,
                947
                /* &gamma;    : Greek small letter gamma */,
                948
                /* &delta;    : Greek small letter delta */,
                949
                /* &epsilon;  : Greek small letter epsilon */,
                950
                /* &zeta;     : Greek small letter zeta */,
                951
                /* &eta;      : Greek small letter eta */,
                952
                /* &theta;    : Greek small letter theta */,
                953
                /* &iota;     : Greek small letter iota */,
                954
                /* &kappa;    : Greek small letter kappa */,
                955
                /* &lambda;   : Greek small letter lambda */,
                956
                /* &mu;       : Greek small letter mu */,
                957
                /* &nu;       : Greek small letter nu */,
                958
                /* &xi;       : Greek small letter xi */,
                959
                /* &omicron;  : Greek small letter omicron */,
                960
                /* &pi;       : Greek small letter pi */,
                961
                /* &rho;      : Greek small letter rho */,
                962
                /* &sigmaf;   : Greek small letter final sigma */,
                963
                /* &sigma;    : Greek small letter sigma */,
                964
                /* &tau;      : Greek small letter tau */,
                965
                /* &upsilon;  : Greek small letter upsilon */,
                966
                /* &phi;      : Greek small letter phi */,
                967
                /* &chi;      : Greek small letter chi */,
                968
                /* &psi;      : Greek small letter psi */,
                969
                /* &omega;    : Greek small letter omega */,
                977
                /* &thetasym; : Greek theta symbol */,
                978
                /* &upsih;    : Greek upsilon with hook symbol */,
                982
                /* &piv;      : Greek pi symbol */,
                8194
                /* &ensp;     : en space */,
                8195
                /* &emsp;     : em space */,
                8201
                /* &thinsp;   : thin space */,
                8204
                /* &zwnj;     : zero width non-joiner */,
                8205
                /* &zwj;      : zero width joiner */,
                8206
                /* &lrm;      : left-to-right mark */,
                8207
                /* &rlm;      : right-to-left mark */,
                8211
                /* &ndash;    : en dash */,
                8212
                /* &mdash;    : em dash */,
                8216
                /* &lsquo;    : left single quotation mark */,
                8217
                /* &rsquo;    : right single quotation mark */,
                8218
                /* &sbquo;    : single low-9 quotation mark */,
                8220
                /* &ldquo;    : left double quotation mark */,
                8221
                /* &rdquo;    : right double quotation mark */,
                8222
                /* &bdquo;    : double low-9 quotation mark */,
                8224
                /* &dagger;   : dagger */,
                8225
                /* &Dagger;   : double dagger */,
                8226
                /* &bull;     : bullet */,
                8230
                /* &hellip;   : horizontal ellipsis */,
                8240
                /* &permil;   : per mille sign */,
                8242
                /* &prime;    : prime */,
                8243
                /* &Prime;    : double prime */,
                8249
                /* &lsaquo;   : single left-pointing angle quotation mark */,
                8250
                /* &rsaquo;   : single right-pointing angle quotation mark */,
                8254
                /* &oline;    : overline */,
                8260
                /* &frasl;    : fraction slash */,
                8364
                /* &euro;     : euro sign */,
                8465
                /* &image;    : black-letter capital i */,
                8472
                /* &weierp;   : script capital p, Weierstrass p */,
                8476
                /* &real;     : black-letter capital r */,
                8482
                /* &trade;    : trademark sign */,
                8501
                /* &alefsym;  : alef symbol */,
                8592
                /* &larr;     : leftwards arrow */,
                8593
                /* &uarr;     : upwards arrow */,
                8594
                /* &rarr;     : rightwards arrow */,
                8595
                /* &darr;     : downwards arrow */,
                8596
                /* &harr;     : left right arrow */,
                8629
                /* &crarr;    : downwards arrow with corner leftwards */,
                8656
                /* &lArr;     : leftwards double arrow */,
                8657
                /* &uArr;     : upwards double arrow */,
                8658
                /* &rArr;     : rightwards double arrow */,
                8659
                /* &dArr;     : downwards double arrow */,
                8660
                /* &hArr;     : left right double arrow */,
                8704
                /* &forall;   : for all */,
                8706
                /* &part;     : partial differential */,
                8707
                /* &exist;    : there exists */,
                8709
                /* &empty;    : empty set */,
                8711
                /* &nabla;    : nabla */,
                8712
                /* &isin;     : element of */,
                8713
                /* &notin;    : not an element of */,
                8715
                /* &ni;       : contains as member */,
                8719
                /* &prod;     : n-ary product */,
                8721
                /* &sum;      : n-ary summation */,
                8722
                /* &minus;    : minus sign */,
                8727
                /* &lowast;   : asterisk operator */,
                8730
                /* &radic;    : square root */,
                8733
                /* &prop;     : proportional to */,
                8734
                /* &infin;    : infinity */,
                8736
                /* &ang;      : angle */,
                8743
                /* &and;      : logical and */,
                8744
                /* &or;       : logical or */,
                8745
                /* &cap;      : intersection */,
                8746
                /* &cup;      : union */,
                8747
                /* &int;      : integral */,
                8756
                /* &there4;   : therefore */,
                8764
                /* &sim;      : tilde operator */,
                8773
                /* &cong;     : congruent to */,
                8776
                /* &asymp;    : almost equal to */,
                8800
                /* &ne;       : not equal to */,
                8801
                /* &equiv;    : identical to, equivalent to */,
                8804
                /* &le;       : less-than or equal to */,
                8805
                /* &ge;       : greater-than or equal to */,
                8834
                /* &sub;      : subset of */,
                8835
                /* &sup;      : superset of */,
                8836
                /* &nsub;     : not a subset of */,
                8838
                /* &sube;     : subset of or equal to */,
                8839
                /* &supe;     : superset of or equal to */,
                8853
                /* &oplus;    : circled plus */,
                8855
                /* &otimes;   : circled times */,
                8869
                /* &perp;     : up tack */,
                8901
                /* &sdot;     : dot operator */,
                8968
                /* &lceil;    : left ceiling */,
                8969
                /* &rceil;    : right ceiling */,
                8970
                /* &lfloor;   : left floor */,
                8971
                /* &rfloor;   : right floor */,
                9001
                /* &lang;     : left-pointing angle bracket */,
                9002
                /* &rang;     : right-pointing angle bracket */,
                9674
                /* &loz;      : lozenge */,
                9824
                /* &spades;   : black spade suit */,
                9827
                /* &clubs;    : black club suit */,
                9829
                /* &hearts;   : black heart suit */,
                9830
                /* &diams;    : black diamond suit */,};
        entityToChar = new HashMap<String,Character>( 511 );
        for ( int i = 0; i < entityKeys.length; i++ )
            {
            entityToChar.put( entityKeys[ i ],
                              Character.valueOf( entityValues[ i ] ) );
            }
        }

    /**
     * remove all text between &lt;applet.. &lt;/applet&gt;, &lt;style...
     * &lt;/style&gt;  &lt;script... &lt;/script&gt;
     *
     * @param s HTML string to strip tag pairs out of.
     *
     * @return string with tag pairs stripped out.
     */
    private static String stripHTMLTagPairs( String s )
        {
        String[] tags =
                {"applet", "APPLET", "style", "STYLE", "script", "SCRIPT"};
        for ( int i = 0; i < tags.length; i++ )
            {
            final String tag = tags[ i ];
            final String beginTag = "<" + tag;
            final String endTag = "</" + tag + ">";
            int begin = 0;
            while ( begin < s.length()
                    && ( begin = s.indexOf( beginTag, begin ) ) >= 0 )
                {
                final int end;
                if ( ( end = s.indexOf( endTag, begin + beginTag.length() ) )
                     > 0 )
                    {
                    // chop out the <applet ... </applet>

                    s =
                            s.substring( 0, begin ) + s.substring( end
                                                                   + endTag.length() );
                    }
                else
                    {
                    // no matching end tag, chop off entire end
                    s = s.substring( 0, begin );
                    }
                }
            }
        return s;
        }

    // --------------------------- main() method ---------------------------

    /**
     * Test harness.
     *
     * @param args not used.
     *
     * @noinspection ConstantConditions
     */
    public static void main( String[] args )
        {
        if ( DEBUGGING )
            {
        	_logger.info( stripEntities( " Bed &amp; Breakfast " ) );

        	_logger.info( stripHTMLTags(
                            " <a href=\"ibm.html\">big blue</a> " ) );

        	_logger.info( stripHTMLTags(
                            "<a href=\"ibm.html\">big\nblue</a>" ) );

        	_logger.info( stripHTMLTags( "big\nblue" ) );

        	_logger.info( stripHTMLTags( "big blue" ) );
            }
        }
}

// end StripEntitiesz
