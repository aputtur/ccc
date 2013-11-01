package com.copyright.ccc.web.forms;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.ValidatorForm;

import com.copyright.ccc.business.data.DisplaySpec;

public class OrderHistoryValidatorForm extends ValidatorForm
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String beginDate;
    private String endDate;
    private Date beginDateObj;
    private Date endDateObj;

    private boolean beginDateFailed; // If true warn the user the from date field had parsing problems
    private boolean endDateFailed; // If true warn the user the to date field had parsing problems
    private ActionMessages errors; // Error messages for date validation

    public OrderHistoryValidatorForm() { }
    
    public void setBeginDateFailed(boolean beginDateFailed) {
       this.beginDateFailed = beginDateFailed;
    }

    public boolean isBeginDateFailed() {
       return beginDateFailed;
    }

    public void setEndDateFailed(boolean endDateFailed) {
       this.endDateFailed = endDateFailed;
    }

    public boolean isEndDateFailed() {
       return endDateFailed;
    }

    public void setErrors(ActionMessages errors) {
       this.errors = errors;
    }

    public ActionMessages getErrors() {
       return errors;
    }

    public boolean hasErrors() 
    {
       boolean rtnValue = false;
       if (this.errors != null) {
           if (!errors.isEmpty()) {
              rtnValue = true;
           }
       }
        
       return rtnValue;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void scrubBeginDate() {
       this.beginDate = this.stripBrackets(this.beginDate);
    }

    public void scrubEndDate() {
       this.endDate = this.stripBrackets(this.endDate); 
    }

    /** 
    * Test method to determine if the last validation check was successful or not
    * If any validation check failed this method returns false so the consuming action
    * knows to not perform a call to the service layer.
    */
    public boolean isValid() {
       return (!this.beginDateFailed && !this.endDateFailed);   
    }
    
    /**
    * Validates the date fields and sets the appropriate Date in the display spec.  Because
    * the display spec is dependent on this code to set the display spec dates this validation
    * is not optional.
    * @param spec is the display spec whose date values are subject to validation.
    * If this method determines that validation fails the DisplaySpec has it's appropriate
    * display field updated by reference to the calling form's DisplaySpec object.
    * @return true if the user supplied date fields parsed without failure or if the
    * search option selected is a non date search
    */
    public boolean validateDates(DisplaySpec spec) 
    {
        boolean rtnValue = false;

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        int fromLength = 0;
        int toLength = 0;
        int idx = 0;
        ParsePosition position = new ParsePosition(0);
        String date = this.getBeginDate();
        if (date != null && !"".equals(date)) 
        {
            fromLength = date.length();

            try {
               Date fromDate = format.parse(date, position);
               idx = position.getIndex();
               if (idx < fromLength) {
                  this.setBeginDateFailed(true);
                  this.scrubBeginDate();
               } else {
                   rtnValue = true;
                   this.beginDateObj = fromDate;
               }

               spec.setSearchFromDate(fromDate);
            } catch (Exception exc) {
               rtnValue = false;
            }            
        } else {
           rtnValue = false;
           this.beginDateFailed = true;
        } 

        date = this.getEndDate();
        if (date != null && !"".equals(date))
        {
            toLength = date.length();

            try {
               position.setIndex(0);
               Date toDate = format.parse(date, position);
               idx = position.getIndex();
               if (idx < toLength) {
                  this.setEndDateFailed(true);
                  this.scrubEndDate(); 
                  rtnValue = false;
               } else {
                   this.endDateObj = toDate;
               }

               spec.setSearchToDate(toDate);
            } catch (Exception exc) {
               rtnValue = false;
            }            
        } else {
           rtnValue = false;
           this.endDateFailed = true;
        }

        if (rtnValue) 
        {
           // Set the search string range for the shared service call
           format = new SimpleDateFormat("dd-MMM-yyyy");
           String d1 = format.format(this.beginDateObj);
           String d2 = format.format(this.endDateObj);
           spec.setSearch(d1 + ":" + d2); 
        }

        return rtnValue;
    }

    /*
    * Remove all occurances of '<' or '>' from a String
    */
    private String stripBrackets(String dateString) 
    {
        // There was a parsing problem.  Eliminate any <> characters
        int idx = dateString.indexOf("<");
        if (idx > -1) {
           dateString = dateString.replaceAll("<", ""); 
        } 
        idx = dateString.indexOf(">");
        if (idx > -1) {
           dateString = dateString.replaceAll(">", ""); 
        }

        return dateString;
    }

    public void setBeginDateObj(Date beginDateObj) {
        this.beginDateObj = beginDateObj;
    }

    public Date getBeginDateObj() {
        return beginDateObj;
    }

    public void setEndDateObj(Date endDateObj) {
        this.endDateObj = endDateObj;
    }

    public Date getEndDateObj() {
        return endDateObj;
    }
}
