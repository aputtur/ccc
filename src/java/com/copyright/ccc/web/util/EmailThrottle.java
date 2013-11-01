package com.copyright.ccc.web.util;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.copyright.ccc.business.security.CCUserContext;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.web.WebConstants;

/**
* Utility class for making sure an email form does not get excessively submitted,
* perhaps by a script, to prevent overloading a web page that invokes email.  The
* class saves the number of times email has been sent from this user and if more 
* than a given threshold of emails have been sent by this uesr at this IP the delay
* from when the last email was sent is checked.  If the last email was sent more 
* than the delay cut off of time this class sets okToSend to true.  Otherwise
* okToSend is set to false.  This has the affect of only allowing an email every 
* delay period after a threshold of emails have been sent.  This is to prevent a 
* script from sending thousands of emails potentially bogging the server down.
*/
public class EmailThrottle 
{
   

    private long DELAY = 20000; // 20 second default delay
    private HttpServletRequest request;
    private boolean okToSend; 
    private String id;

    public EmailThrottle(HttpServletRequest request) 
    {
       this.okToSend = false;

       this.request = request;
       CCUserContext userContext = UserContextService.getUserContext();
       long auid = userContext.getActiveAppUserID();

       // try to get the forwarded IP if it is available
       String ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
       if (ipAddress == null) {
          ipAddress = request.getRemoteAddr();
       }

       // Make an identifying token based on the user AUID and IP address
       this.id = auid + ":" + ipAddress;
       HttpSession session = request.getSession();

       EmailCounterBean emailData = (EmailCounterBean) session.getAttribute(WebConstants.SessionKeys.EMAIL_COUNT);
       if (emailData == null) {
          // If there is no session email data create one
          this.okToSend = true;
          emailData = new EmailCounterBean(this.id, 0);
          session.setAttribute(WebConstants.SessionKeys.EMAIL_COUNT, emailData);
       } else {
          String savedId = emailData.getId();
          if (id.equals(savedId)) {
             int count = emailData.getCount();
             if (count < 5) {
                 // Let the first 5 emails go without checking
                 this.okToSend = true;
             } else {
                 long lastSend = emailData.getTime();
                 Date today = new Date();
                 long now = today.getTime();
                 long delay = now - lastSend;
                 emailData.setTime(now);
                 // If the user has sent more than 5 enforce a 30 second delay
                 if (delay > DELAY) {
                    this.okToSend = true;
                 } else {
                    Logger logger = Logger.getLogger(this.getClass());
                    logger.warn("EmailThrottle suppressed email for delay constraint violation for email count: " + emailData.getCount() + " & auid:ipAddress = " + this.id);                     
                 }
             }
          } else {
              // Log the user:IP mismatch in the session object
              Logger logger = Logger.getLogger(this.getClass());
              logger.warn("EmailThrottle suppressed email for user/IP mismatch in session.  Found: " + savedId + " expected: " + this.id);
          }
       }
    }

    /**
    * Increment the email counter stored in the session. If no session object exists
    * create one and save it with an initial count of 0.  Note that calling increment()
    * saves the incremented value of the count attribute and also updates the time to 
    * reflect the current time in milliseconds.  If an email exception is encountered 
    * when sending the email the consuming application must either not call increment 
    * or should decrement the counter to accurately reflect the number of emails sent.
    */
    public void increment() 
    {
        HttpSession session = this.request.getSession();
        EmailCounterBean bean = (EmailCounterBean) session.getAttribute(WebConstants.SessionKeys.EMAIL_COUNT);
        if (bean != null) {
           bean.increment();
        } else {
           bean = new EmailCounterBean(this.id, 0);
           session.setAttribute(WebConstants.SessionKeys.EMAIL_COUNT, bean);
        }
    }

    /**
    * Decrement the persisted email counter.  Note since the last time of email
    * is not persisted decrementing does not restore the internal email time to 
    * the previous valid value.
    */
    public void decrement() 
    {
        HttpSession session = this.request.getSession();
        EmailCounterBean bean = (EmailCounterBean) session.getAttribute(WebConstants.SessionKeys.EMAIL_COUNT);
        if (bean != null) {
           bean.decrement();
        } else {
           bean = new EmailCounterBean(this.id, 0);
           session.setAttribute(WebConstants.SessionKeys.EMAIL_COUNT, bean);
        }
    }

    public void setOkToSend(boolean okToSend) {
       this.okToSend = okToSend;
    }

    public boolean isOkToSend() {
       return okToSend;
    }
    
    public void setDelay(long delay) {
       this.DELAY = delay;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}


