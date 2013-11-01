package com.copyright.ccc.web;

import java.util.Hashtable;

import org.apache.log4j.Logger;

/**
 *  This class is simply an attempt at making our
 *  form email redirects a bit more secure.  The drawbacks
 *  are that links are hard coded, app prefixes are hardcoded
 *  and I rushed this in a bit.  Sorry.
 */

public class RedirectDescriptor
{
    private String _key;
    private Hashtable<String, String> _map;
    private Logger logger;
    
    public RedirectDescriptor(String key)
    {
        _key = key;
        _map = new Hashtable<String, String>();
        _map.put("CorporateGuideInquiry", "/Services/CorporateGuide/InquiryThankYou.htm");
        _map.put("ResumeForm", "/ccc/viewPage.do?pageCode=au73");
        _map.put("PosterRequest", "/ccc/viewPage.do?pageCode=bu16");
        _map.put("Feedback", "/ccc/viewPage.do?pageCode=h18");
        _map.put("HAForm", "/Services/PublishersResource/HouseAdsDLP.html");
        _map.put("CCCImages", "/ccc-images/inquirythankyou.html");
        logger = Logger.getLogger(this.getClass());
    }
    
    public String getURL() {
        String url = _map.get(_key);
        if (url == null) url = "/ccc/home.do";
        if (logger.isDebugEnabled()) {
            logger.debug("RedirectDescriptor.KEY == " + _key);
            logger.debug("RedirectDescriptor.URL == " + _map.get(_key));
        }
        return url;
    }
    public String getKey() { return _key; }
}