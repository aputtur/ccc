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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class SaveInvoiceNumReportAction extends Action 
{
    public SaveInvoiceNumReportAction() { }
    
    @Override
    public ActionForward execute( ActionMapping mapping, ActionForm form, 
        HttpServletRequest request, HttpServletResponse response ) 
    {
        ActionMessages errors = new ActionMessages();   
        ActionForward forward = null;
        
        ViewReportsActionForm viewForm = WebUtils.castForm( ViewReportsActionForm.class, form );

        // The user should have entered one or more invoice numbers separated
        // by commas.  If this field is blank raise a validation error message
        // indicating the the user should enter one or more invoice numbers.
        String invNumbers = request.getParameter("invoiceNum");
        if (invNumbers != null && !"".equals(invNumbers)) 
        {
            ReportUtil util = new ReportUtil();   
            util.getCSVReport(viewForm);
            this.downLoad(util, response);
        } else {
           ActionMessage error = new ActionMessage("admin.error.bad.invoice.num");
           errors.add("missInvNum", error);
           this.saveErrors(request,errors);
        }

        return forward;
    }

    private void downLoad(ReportUtil util, HttpServletResponse resp) {
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

            List<String> data = util.getReportData();
            
            if (data != null) {
//                int rowCount = data.size();
//                if (false) { //rowCount >= 5000
//                    // For big reports write out to file first then download
//                    // to browser using print writer
//                    // TODO: Talk to Keith about file path and name generation
//                    File file = new File("C:\temp\report.dat");
//                    FileOutputStream fileOut = new FileOutputStream(file);
//                    
//                    byte[] bytes;
//                    Iterator itr = data.iterator();
//                    while (itr.hasNext()) {
//                       String aRow = (String) itr.next();
//                       bytes = aRow.getBytes();
//                       fileOut.write(bytes);
//                    }
//    
//                    fileOut.flush();
//                    fileOut.close();             
//                    java.io.PrintWriter out = resp.getWriter();
//                    FileInputStream fileIn = new FileInputStream(file);
//                    int character;
//                    while ((character = fileIn.read()) != -1) 
//                    {
//                       out.write(character);
//                    }
//                    fileIn.close();
//                    out.flush();
//                    out.close();
//                }  else {
                    // write directly to print writer
                java.io.PrintWriter out = resp.getWriter();
                Iterator<String> itr = data.iterator();
                while (itr.hasNext()) {
                    String row = itr.next();
                    out.println(row);
                }
//                    while ((character = fileIn.read()) != -1) 
//                    {
//                       out.write(character);
//                    }

                out.flush();
                out.close();
                //}
                
                data = null;
            }
        } catch (IOException ioe) {
            Logger logger = Logger.getLogger( this.getClass() );
            logger.error("SaveInvoiceNumReportAction.download(): ", ioe);
        } catch (Exception exc) {
            Logger logger = Logger.getLogger( this.getClass() );
            logger.error("SaveInvoiceNumReportAction.download(): ", exc);
        }        
    }  

/*
    private void downLoad(String report, HttpServletResponse resp) {
        try {
            resp.setContentType("text/csv");

            SimpleDateFormat formatter = new SimpleDateFormat ("EEE, dd MMM yyyy HH:mm:ss zzz"); 
            GregorianCalendar objDate = new GregorianCalendar(); 
            objDate.add(objDate.HOUR,1); 
            String dateString = formatter.format(objDate.getTime()); 
            resp.setHeader("Expires", dateString); 
            resp.setHeader("Cache-Control","store, cache"); 
            resp.setHeader("Pragma","cache"); 
            resp.setHeader("Content-disposition", "attachment;filename=report.csv");
                          
            java.io.PrintWriter out = resp.getWriter();
            out.print(report);
            out.flush();
            out.close();

        } catch (IOException ioe) {
            //_logger.error("OrderDetailAction.showOrderDetails(): ", ioe);
            //return null;   
        }        
    } 
*/
}
