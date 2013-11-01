package com.copyright.ccc.web.actions;

import com.copyright.ccc.web.forms.ViewReportsActionForm;
import com.copyright.ccc.web.util.ReportUtil;
import com.copyright.ccc.web.util.WebUtils;

import java.io.IOException;

import java.text.SimpleDateFormat;

import java.util.GregorianCalendar;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

/*
* Super class for processing reports into CSV files for reports
* using invoice and order date ranges.
*/
public class SaveDateReportAction extends Action
{
    protected Logger _logger = Logger.getLogger( this.getClass() );
    
    private String searchBy; // controls search by order or invoice date
    private ActionMessages errors;
    
    public SaveDateReportAction() { }

    @Override
    public ActionForward execute( ActionMapping mapping, ActionForm form, 
        HttpServletRequest request, HttpServletResponse response ) 
    {
        errors = new ActionMessages();   
        // Null the forward for a download so the browser doesn't open
        // a new window.  TODO: Figure out how to get the browser to show busy
        ActionForward forward = null; 
        ViewReportsActionForm viewForm = WebUtils.castForm( ViewReportsActionForm.class, form );

        ReportUtil util = getReportUtil();

        util.getCSVReport(viewForm);
        if (errors.isEmpty()) {
           this.downLoad(util, response);
        }

        return forward;
    }

    private void downLoad(ReportUtil util, HttpServletResponse resp) 
    {
       try {
           resp.setContentType("text/csv");
           SimpleDateFormat formatter = new SimpleDateFormat ("EEE, dd MMM yyyy HH:mm:ss zzz"); 

           GregorianCalendar objDate = new GregorianCalendar(); 
           objDate.add(Calendar.HOUR,1); 
           String dateString = formatter.format(objDate.getTime()); 

           resp.setHeader("Expires", dateString); 
           resp.setHeader("Cache-Control","store, cache"); 
           resp.setHeader("Pragma","cache"); 

           resp.setHeader("Content-disposition", "attachment;filename=report.csv");
                          
           //File file = new File("C:/temp/dateReport.dat");
           //FileOutputStream fileOut = new FileOutputStream(file);
           
           List<String> data = util.getReportData();
           if (data != null) 
           {
              java.io.PrintWriter out = resp.getWriter();
              Iterator<String> itr = data.iterator();
              while (itr.hasNext()) {
                 String aRow = itr.next();
                 out.print(aRow);
              }

              out.flush();
              out.close();
           }

       } catch (IOException ioe) {
          _logger.error("SaveDateReportAction.downLoad(): ", ioe);
       } catch (Exception exc) {
          _logger.error("SaveDateReportAction.downLoad(): ", exc);
       } 
    }

    public void setSearchBy(String searchBy) {
        this.searchBy = searchBy;
    }

    public String getSearchBy() {
        return searchBy;
    }
    
    protected ReportUtil getReportUtil()
    {
    	return new ReportUtil();
    }
}
