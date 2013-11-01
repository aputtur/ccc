package com.copyright.ccc.web.actions;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class OpenReportAction  extends Action
{
    public OpenReportAction() { }
    
    @Override
    public ActionForward execute( ActionMapping mapping, ActionForm form, 
        HttpServletRequest request, HttpServletResponse response ) 
    {
//        ActionForward forward = mapping.findForward("continue");
        //ActionMessages errors = new ActionMessages();   
        //ViewReportsActionForm viewForm = WebUtils.castForm( ViewReportsActionForm.class, form );
        
        // Download the visible screen contents in a CSV file
        String csvReport = this.getReport();
        try {
             response.setContentType("text/csv");
             response.setHeader("Content-disposition", "attachment;filename=report.csv");
                          
             java.io.PrintWriter out = response.getWriter();
             out.print(csvReport);
             out.flush();
             out.close();
//response.flushBuffer();

             //return null; // Remain on the current HMTL page
        } catch (IOException ioe) {
            //_logger.error("OrderDetailAction.showOrderDetails(): ", ioe);
            //return null;   
        }

        return null;
    }
    
    private String getReport() {
       StringBuffer report = new StringBuffer("header1, header2, header3, header4 \n");
       report.append("35,20,22,21\n");
       report.append("15,10,2,31\n");
       report.append("25,30,3,11\n");
       report.append("45,40,4,41\n");
       
       return report.toString();
    }
}
