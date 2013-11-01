package com.copyright.ccc.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.config.CC2Configuration;
import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.RedirectDescriptor;
import com.copyright.ccc.web.forms.MicroSiteForm;
import com.copyright.ccc.web.util.EmailThrottle;
import com.copyright.mail.MailDispatcher;
import com.copyright.mail.MailDispatcherImpl;
import com.copyright.mail.MailMessage;
import com.copyright.mail.MailSendFailedException;

/*
 * Please list your modifications to this module here.  Newest change at the
 * top of the list, please!
 * 
 * when         who             what
 * ------------ --------------- ----------------------------------------------
 * 2009-04-14   MJESSOP         Tried to make the redirects more secure by
 *                              using a dictionary-like class; now we use
 *                              keys to lookup a url to redirect to.
 * 2008/09/17   MJESSOP         Added condition for CCC Images email form.
 */

public class FormToEmailAction extends CCAction
{
    public ActionForward defaultOperation(ActionMapping mapping,
                                            ActionForm form,
                                            HttpServletRequest request,
                                            HttpServletResponse response )
    {
        MicroSiteForm microSiteForm = castForm( MicroSiteForm.class, form );
        ActionForward af = new ActionForward("/home.do",true);
        if (form!=null) {
            String microSiteFormLabel = microSiteForm.getLabel();
            String subject = "CCC MicroSite Email Form";
            String emailTo = null;
            String returnTo = "/home.do";
            String from = "CCC FormToEmailAction";
            String body = null;
            
            RedirectDescriptor redirector = 
                new RedirectDescriptor(microSiteFormLabel);
            
            //ResourceBundle myResources = ResourceBundle.getBundle("ApplicationResources");
            CC2Configuration configFile = CC2Configuration.getInstance();
            
            if ("CorporateGuideInquiry".equals(microSiteFormLabel)) {
                subject = "Corporate_Guide_Info_Request";
                emailTo = configFile.getGuideEmailTo();
                //myResources.getString("guide.email.to"); //"licensing@copyright.com";
                returnTo = redirector.getKey();
                from = "CorporateGuide@copyright.com";
                body = formatBodyForCorporateGuideInquiry(microSiteForm);
            } else if ("ResumeForm".equals(microSiteFormLabel)) {
                subject = "Resume Submission";
                emailTo = configFile.getResumeEmailTo();
                //myResources.getString("resume.email.to"); //"hr@copyright.com";
                returnTo = redirector.getKey();
                from = "ResumeForm@copyright.com";
                body = formatBodyForResumeEmail(microSiteForm);
            } else if ("PosterRequest".equals(microSiteFormLabel)) {
                subject = "Poster Request";
                emailTo = configFile.getPosterEmailTo();
                //myResources.getString("poster.email.to"); //"licensing@copyright.com";
                returnTo = redirector.getKey();
                from = "PosterRequestForm@copyright.com";
                body = formatBodyForPosterRequest(microSiteForm);
            } else if ("Feedback".equals(microSiteFormLabel)) {
                subject = "Feedback";
                emailTo = configFile.getFeedbackEmailTo();
                //myResources.getString("feedback.email.to"); //"info@copyright.com";
                returnTo = redirector.getKey();
                from = "FeedbackForm@copyright.com";
                body = formatBodyForFeedback(microSiteForm);
            } else if ("HAForm".equals(microSiteFormLabel)) {
                subject = "House Ad Request";
                emailTo = configFile.getHAEmailTo();
                returnTo = redirector.getKey();
                //myResources.getString("ha.email.to"); //"house_ads@copyright.com";
                from = "FeedbackForm@copyright.com";
                body = formatBodyForHouseAd(microSiteForm);
            } else if ("CCCImages".equals(microSiteFormLabel)) {
                subject = "CCC Images Inquiry";
                emailTo = configFile.getImagesEmailTo();
                returnTo = redirector.getKey();
                //myResources.getString("images.email.to"); //"images@copyright.com";
                from = "CCCImagesForm@copyright.com";
                String ip = determineClientIP(request);
                body = formatBodyForCCCImages(microSiteForm, ip);            
            }
                
            if (emailTo != null && body != null) {
                EmailThrottle throttle = new EmailThrottle(request);
                boolean okToSend = throttle.isOkToSend();
                if (okToSend) {
                   if (sendEmail(subject, emailTo, from, body)) {
                      throttle.increment(); // save the email count in session
                   }
                } else {
                   // keyLog error and let user know the email was choked off
                   Logger logger = Logger.getLogger(this.getClass());
                   logger.warn("FormToEmail disallowed email invocation - possible script abuse for auid:IP: " + throttle.getId());
                   //Add ActionMessage alerting user that the email was not sent
                   //ActionMessages messages = new ActionMessages();
                   //ActionMessage message = new ActionMessage("errors.email.delay");
                   //messages.add("email", message);
                   //this.saveErrors(request, messages);
                }
            }
            af = new ActionForward("/redirect.do?target="+returnTo, true);
        }   
        return af;
    }

    private boolean sendEmail(String subject, String to, String from, String body) 
    {
        boolean rtnValue = false;
        MailMessage message = new MailMessage();
        message.setSubject(subject);
        message.setRecipient(to);
        message.setFromEmail(from);        
        message.setBody(body);

        try
        {
           MailDispatcher dispatcher = new MailDispatcherImpl();
           dispatcher.send(message);
           rtnValue = true;
        }
        catch ( MailSendFailedException esfe )
        {
           _logger.error( ExceptionUtils.getFullStackTrace( esfe ) );
        }

        return rtnValue;
    }


    private String formatBodyForCorporateGuideInquiry(MicroSiteForm form) {
        StringBuffer buf = new StringBuffer();
        buf.append("First Name:    "); buf.append(form.getValue("FirstName")); buf.append("\n");
        buf.append("Last Name:     "); buf.append(form.getValue("LastName")); buf.append("\n");
        buf.append("Title:         "); buf.append(form.getValue("Title")); buf.append("\n");
        buf.append("Company:       "); buf.append(form.getValue("Organization")); buf.append("\n");
        buf.append("Address:       "); buf.append(form.getValue("Address")); buf.append("\n");
        buf.append("Address2:      "); buf.append(form.getValue("Address2")); buf.append("\n");
        buf.append("City:          "); buf.append(form.getValue("City")); buf.append("\n");
        buf.append("State:         "); buf.append(form.getValue("State")); buf.append("\n");
        buf.append("Province:      "); buf.append(form.getValue("Province")); buf.append("\n");
        buf.append("Zip:           "); buf.append(form.getValue("Zip")); buf.append("\n");
        buf.append("Country:       "); buf.append(form.getValue("Country")); buf.append("\n\n");
        buf.append("Phone:         "); buf.append(form.getValue("Phone")); buf.append("\n");
        buf.append("E-mail Address:"); buf.append(form.getValue("Email")); buf.append("\n");
        buf.append("Promotion Code:"); buf.append(form.getValue("PromoCode")); buf.append("\n\n");
            
        buf.append("Customer requests more info - "); 
        buf.append(form.getValue("RadioButton2")); buf.append("\n\n");

        buf.append("Area(s) of Interest:\n");
        buf.append("Business Solutions - "); 
        buf.append(form.getValue("CB1")); buf.append("\n");
        buf.append("Academic Solutions - "); 
        buf.append(form.getValue("CB2")); buf.append("\n");
        buf.append("Author Publisher Services - "); 
        buf.append(form.getValue("CB3")); buf.append("\n");
        buf.append("Integration Services - "); 
        buf.append(form.getValue("CB4")); buf.append("\n");
            
        if ("Yes".equals(form.getValue("CB5"))) {
                buf.append("Other (specific) - "); 
                buf.append(form.getValue("Other")); buf.append("\n\n");       
        }
            buf.append("Comments:  "); buf.append(form.getValue("Comments")); buf.append("\n");
            return buf.toString();
    }
    private String formatBodyForResumeEmail(MicroSiteForm form) {
        StringBuffer buf = new StringBuffer();
        buf.append("Job Title:    "); buf.append(form.getValue("Job_Title")); buf.append("\n");
        buf.append("Cover Letter Text: \n "); buf.append(form.getValue("Cover_Letter_Text")); buf.append("\n\n");
        buf.append("Resume Text: \n "); buf.append(form.getValue("Resume_Text")); buf.append("\n");
            return buf.toString();
    }


    private String formatBodyForPosterRequest(MicroSiteForm form) {
        StringBuffer buf = new StringBuffer();
        buf.append("First Name:    "); buf.append(form.getValue("firstName")); buf.append("\n");
        buf.append("Last Name:     "); buf.append(form.getValue("lastName")); buf.append("\n");
        buf.append("Title:         "); buf.append(form.getValue("title")); buf.append("\n");
        buf.append("Organization:  "); buf.append(form.getValue("organization")); buf.append("\n");
        buf.append("Address:       "); buf.append(form.getValue("address")); buf.append("\n");
        buf.append("Address2:      "); buf.append(form.getValue("address2")); buf.append("\n");
        buf.append("City:          "); buf.append(form.getValue("city")); buf.append("\n");
        buf.append("State:         "); buf.append(form.getValue("state")); buf.append("\n");
        buf.append("Zip:           "); buf.append(form.getValue("zip")); buf.append("\n");
        buf.append("Phone:         "); buf.append(form.getValue("phone")); buf.append("\n");
        buf.append("E-mail Address:"); buf.append(form.getValue("email")); buf.append("\n");
        return buf.toString();
        
    }

    private String formatBodyForHouseAd(MicroSiteForm form) {
        StringBuffer buf = new StringBuffer();
        buf.append("First Name:    "); buf.append(form.getValue("firstName")); buf.append("\n");
        buf.append("Last Name:     "); buf.append(form.getValue("lastName")); buf.append("\n");
        buf.append("Title:         "); buf.append(form.getValue("title")); buf.append("\n");
        buf.append("Company:       "); buf.append(form.getValue("company")); buf.append("\n");
        buf.append("Phone:         "); buf.append(form.getValue("phone")); buf.append("\n");
        buf.append("E-mail Address:"); buf.append(form.getValue("email")); buf.append("\n\n");
        buf.append("Publications  :"); buf.append(form.getValue("printAdLocation")); buf.append("\n\n");
        buf.append("Websites      :"); buf.append(form.getValue("webAdLocation")); buf.append("\n");
        return buf.toString();       
    }


    private String formatBodyForFeedback(MicroSiteForm form) {
        StringBuffer buf = new StringBuffer();
        buf.append("Email:         "); buf.append(form.getValue("Email")); buf.append("\n");
        buf.append("Account Number:"); buf.append(form.getValue("Account")); buf.append("\n");
        buf.append("Company:       "); buf.append(form.getValue("CompanyInstitution")); buf.append("\n");
        buf.append("Name:          "); buf.append(form.getValue("PersonalName")); buf.append("\n");
        buf.append("Subject:       "); buf.append(form.getValue("Subject")); buf.append("\n");
        buf.append("Comments:\n      "); buf.append(form.getValue("Comments")); buf.append("\n");
        return buf.toString();

    }
    
    private String formatBodyForCCCImages(MicroSiteForm form, String ip) {
        StringBuffer buf = new StringBuffer();
        buf.append("First Name:      "); buf.append(form.getValue("firstName")); buf.append("\n");
        buf.append("Last Name:       "); buf.append(form.getValue("lastName")); buf.append("\n");
        buf.append("Title:           "); buf.append(form.getValue("title")); buf.append("\n");
        buf.append("Company:         "); buf.append(form.getValue("company")); buf.append("\n");
        buf.append("Phone:           "); buf.append(form.getValue("phone")); buf.append("\n");
        buf.append("E-mail Address:  "); buf.append(form.getValue("email")); buf.append("\n");
        buf.append("Promotion Code:  "); buf.append(form.getValue("PromoCode")); buf.append("\n");
        buf.append("IP Address:      "); buf.append(ip); buf.append("\n\n");

        return buf.toString();       
    }
            
    public String determineClientIP( HttpServletRequest request )
    {
        String ipFromCLIENTHeader = request.getHeader( "CLIENT" );
        String sourceFromCLIENTHeader = "CLIENT Header";
        String ipFromCLIENTIPHeader = request.getHeader( "CLIENTIP" );
        String sourceFromCLIENTIPHeader = "CLIENTIP Header";
        String ipFromRequest = request.getRemoteAddr();
        String sourceFromRequest = "Request RemoteAddr";
        
        String ip = ipFromCLIENTHeader;
        String source = sourceFromCLIENTHeader;
        
        if ( StringUtils.isBlank( ip ) )
        {
            ip = ipFromCLIENTIPHeader;
            source = sourceFromCLIENTIPHeader;
        }
        
        if ( StringUtils.isBlank( ip ) )
        {
            ip = ipFromRequest;
            source = sourceFromRequest;
        }
                    
        return ip;
    }

}
