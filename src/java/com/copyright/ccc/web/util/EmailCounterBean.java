package com.copyright.ccc.web.util;

import java.util.Date;

/**
* Java Bean class that associates an identifier with a count for keeping
* track of how many emails a given user has sent.  This class could be saved
* in the session and checked each time a user attempts to send an email so 
* abusive scripts can be thwarted from sending excessive emails.  This class
* also has a time attribute that allows one to interrogate when the last 
* email was sent.  This allows throttling of email submissions by number and
* frequency.
*/
public class EmailCounterBean
{
    private String identifier;
    private int count;
    private long time;

    /**
    * Constructor that takes a user ID as a parameter.  The internal count value
    * is set to 0 and the internal time of email is set to now as a long.
    * @param id identifies the user through some unique String value.
    */
    public EmailCounterBean(String id) {
       this.identifier = id;
       this.count = 0;
       Date now = new Date();
       this.time = now.getTime();
    }

    /**
    * Constructor that a user ID and an initial email count as parameters.
    * @param id identifies the user through some unique String value.
    * @param count is the intial email count as an int
    */
    public EmailCounterBean(String id, int count) {
       this.identifier = id;
       this.count = count;
       Date now = new Date();
       this.time = now.getTime();
    }

    /** 
    * Increment the email counter and update the time of email to now
    */
    public void increment() {
       Date today = new Date();
       this.time = today.getTime();
       this.count++;
    }

    /**
    * Decrement the email counter.  Do not alter the time of email.  This method
    * is to allow a consuming class to adjust the email counter if an email 
    * invocation failed with an exception.  The consuming class should also persist
    * the last email time and set that value by calling setTime().
    */
    public void decrement() {
       this.count--;
    }

    public String getId() { return this.identifier; }
    public int getCount() { return this.count; }

    public void setTime(long time) {
       this.time = time;
    }
    public long getTime() { return time; }
}
