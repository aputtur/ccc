package com.copyright.ccc.web.forms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.struts.validator.ValidatorForm;

import com.copyright.ccc.web.util.DropDownElement;
import com.copyright.ccc.web.util.WebUtils;

public class ViewReportsActionForm extends ValidatorForm 
{
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
   private Collection<DropDownElement> months;
   private Collection<DropDownElement> days;
   private Collection<DropDownElement> years;
   
   private String beginDate;
   private String endDate;
   private String radio;
   private String invoiceNum;
   private String html; // report output as HTML
   private int range; // report output as HTML
   private List<String> reportData; // Holds report as a List of Strings

   private String enterpriseViewInd = "F";

   public ViewReportsActionForm() { this.initUI(); }

   public void setMonths(Collection<DropDownElement> months) {
      this.months = months;
   }

   public Collection<DropDownElement> getMonths() {
      return months;
   }

   public void setDays(Collection<DropDownElement> days) {
      this.days = days;
   }

   public Collection<DropDownElement> getDays() {
      return days;
   }

   public void setYears(Collection<DropDownElement> years) {
      this.years = years;
   }

   public Collection<DropDownElement> getYears() {
      return years;
   }

   /*
   * Define the selection lists for struts select tag
   */
   private void initUI() {
      this.months = WebUtils.getMonthOptions();
      this.days = WebUtils.getDayOptions();
      this.years = WebUtils.getYearOptions();
      this.radio = "invNum";
   }

    public void setRadio(String submit) {
      this.radio = submit;
    }

    public String getRadio() {
      return radio;
    }

    public void setInvoiceNum(String invoiceNum) {
       this.invoiceNum = invoiceNum;
    }

    public String getInvoiceNum() {
       if (this.invoiceNum != null) {
         this.invoiceNum = this.invoiceNum.trim();
       }
       
       return invoiceNum;
    }

    public void setHtml(String html) {
       this.html = html;
    }

    public String getHtml() {
       return html;
    }
    
    public String getBeginDate() { return this.beginDate; }
    public void setBeginDate(String date) { this.beginDate = date; }

    public String getEndDate() { return this.endDate; }
    public void setEndDate(String date) { this.endDate = date; }
    
    public Date getFirstDateObject() 
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = null;
        try {
            date = dateFormat.parse(this.beginDate);
        } catch (ParseException pex) {
           // do nothing 
        }

        return date;        
    }
    
    public Date getLastDateObject() 
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = null;
        try {
           date = dateFormat.parse(this.endDate);
        } catch (ParseException pex) {
           // do nothing 
        }

        return date;       
    }


    public void setEnterpriseViewInd(String enterpriseViewInd) {
        this.enterpriseViewInd = enterpriseViewInd;
    }

    public String getEnterpriseViewInd() {
        return enterpriseViewInd;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getRange() {
        return range;
    }

    public void setReportData(List<String> reportData) {
        this.reportData = reportData;
    }

    public List<String> getReportData() {
        return reportData;
    }
}
