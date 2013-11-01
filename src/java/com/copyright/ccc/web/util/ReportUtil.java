package com.copyright.ccc.web.util;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.copyright.ccc.business.data.OrderBundle;
import com.copyright.ccc.business.data.OrderLicense;
import com.copyright.ccc.business.data.OrderPurchase;
import com.copyright.ccc.business.data.OrderSearchResult;
import com.copyright.ccc.business.services.ProductEnum;
import com.copyright.ccc.business.services.order.OrderItemBundleImpl;
import com.copyright.ccc.business.services.order.OrderLicenseServices;
import com.copyright.ccc.business.services.order.OrderLicensesException;
import com.copyright.ccc.business.services.order.OrderPurchaseServices;
import com.copyright.ccc.business.services.user.User;
import com.copyright.ccc.web.forms.ViewReportsActionForm;
import com.copyright.data.order.OrderMgmtSearchSortConstants;
import com.copyright.data.order.ProductUsageConstants;
import com.copyright.svc.telesales.api.data.ARAccount;
import com.copyright.svc.telesales.api.data.Organization;
import com.copyright.svc.tf.api.data.ArAccountInfo;
import com.copyright.svc.tf.api.data.Party;

/**
 * Helper class to provide a common code base for generating activity reports
 * either for saving as a CSV file or for displaying as an HTML file.
 */
public class ReportUtil 
{
    protected Logger _logger = Logger.getLogger( this.getClass() );

    protected final static String quote = "\"";
    protected final static String HTML_END_TABLE_ROW = "</td></tr>\n";
    protected final static String CSV_END_ROW = "\r\n";
    protected final static String HTML_END_TABLE = "</table>\n";
    protected final static String HTML_EMPTY_STRING = "&nbsp;";
    protected final static String CSV_EMPTY_STRING = "";
    protected final static String HTML_FIELD_SEPARATOR = "</td><td>";
    protected final static String CSV_FIELD_SEPARATOR = ",";
    
    private final static String tbd = "TBD";
    protected StringBuffer buffer;
    protected List<String> reportData; // The report data as a list of Strings

    public static final boolean CSV = true;
    public static final boolean HTML = false;
    protected boolean outputFormat;
    private final static int BLOCK_SIZE = 10000; // Report block size is 10,000
    protected SimpleDateFormat dateFormat; //
    protected NumberFormat currencyFormat;
    protected String lastRowCSV = "There are no licenses for the given search criteria";
    protected String lastRowHTML = "<tr><td colspan=\"12\">There are no licenses for the given search criteria</td><td colspan=\"27\">&nbsp;</td></tr></table>";
    
    public ReportUtil() { 
      this.dateFormat = new SimpleDateFormat("MM/dd/yyyy");
      this.currencyFormat = NumberFormat.getCurrencyInstance();
      this.reportData = new ArrayList<String>();
      this.buffer = new StringBuffer();
    }

    public boolean formatIsCSV() {
    	return this.outputFormat == CSV;
    }
    
    public boolean formatIsHTML() {
    	return this.outputFormat == HTML;
    }
    
    public String getEmptyString() {
    	if ( formatIsHTML() ) {
    		return HTML_EMPTY_STRING;
    	} else {
    		return CSV_EMPTY_STRING;
    	}
    }
    
    public String getFieldSeparator() {
    	if ( formatIsCSV() ) {
    		return CSV_FIELD_SEPARATOR;
    	} else {
    		return HTML_FIELD_SEPARATOR;
    	}
    }
    
    //public String getCSVReport(ViewReportsActionForm form) 
    public void getCSVReport(ViewReportsActionForm form) 
    {
       this.outputFormat = CSV;
       
       this.getCSVHeader(); 
       // Get the data for each report row as a List of ReportRow objects
       List<ReportRow> rows = this.getReportRows(form);
       this.getReport(rows);
       form.setReportData(this.reportData);    
    }

    public void getHTMLReport(ViewReportsActionForm form) 
    {
       this.outputFormat = HTML;

       this.getHTMLHeader();
       // Get the data for each report row as a List of ReportRow objects
       List<ReportRow> rows = this.getReportRows(form);
       this.getReport(rows);
       form.setReportData(this.reportData);
    }

    /**
     * 
    * Main getter called by consuming Struts action
    */
    public List<ReportRow> getReportRows(ViewReportsActionForm form) 
    {
        List<ReportRow> rows = null;
        String searchBy = null;
        String search = null;
        
        //String enterpriseView = form.getEnterpriseViewInd();
        //boolean adminView = false;
        //if ("T".equals(enterpriseView)) {
        //    adminView = true;
        //}
        
        boolean adminView = false; // Set to true since range selection is disabled

        String radio = form.getRadio();

        if ("invNum".equals(radio)) {
           searchBy = OrderMgmtSearchSortConstants.LICENSE_SEARCH_TYPE_INVOICE_NUMBER;
           search = form.getInvoiceNum();
           if (adminView) {
              int rangeIdx = form.getRange();
              rows = this.getReportRows(search, rangeIdx);   
           } else {
              rows = this.getReportRows(search);
           }
        } else if ("invDate".equals(radio)) {
           searchBy = OrderMgmtSearchSortConstants.LICENSE_SEARCH_TYPE_INVOICE_DATE;
           search = this.getDateSearchString(form);
           if (adminView) {
              int rangeIdx = form.getRange();
              rows = this.getReportRows(searchBy, search, rangeIdx); 
           } else {
              rows = this.getReportRows(searchBy, search);  
           }
        } else if ("orderDate".equals(radio)) {
           searchBy = OrderMgmtSearchSortConstants.LICENSE_SEARCH_TYPE_PURCHASE_DATE;
           search = this.getDateSearchString(form);
           if (adminView) {
              int rangeIdx = form.getRange();
              rows = this.getReportRows(searchBy, search, rangeIdx); 
           } else {
              rows = this.getReportRows(searchBy, search);  
           }
        } else if ("sotDate".equals(radio)) {
           searchBy = OrderMgmtSearchSortConstants.LICENSE_SEARCH_TYPE_START_OF_TERM;
           search = this.getDateSearchString(form);
           if (adminView) {
              int rangeIdx = form.getRange();
              rows = this.getReportRows(searchBy, search, rangeIdx); 
           } else {
              rows = this.getReportRows(searchBy, search);  
           }
        }

        return rows;
    }

    /*
    * Return a List of ReportRow objects holding purchase and license information
    */
    private final List<ReportRow> getReportRows(String searchBy, String value)
    {
       return this.getReportRows(searchBy, value, 0); // return all report rows
    }

    /*
    * Return a List of ReportRow objects holding purchase and license information
    * @param searchBy is the shared service search string.
    * @param searchBy is the shared service search value.
    * @param rangeIndex is a result set range index that breaks the report 
    * rows returned into 10,000 row blocks.  This allows the administrative user 
    * to choose which part of a big report is downloaded to avoid timeouts.
    */
    private final List<ReportRow> getReportRows(String searchBy, String value, int rangeIndex)
    {
        List<ReportRow> rows = new ArrayList<ReportRow>();        
//        List<OrderReportResults> reportContents = new ArrayList<OrderReportResults>();
        OrderSearchResult orderSearchResult;
        
        try {
           if (rangeIndex == 0) {
        	  orderSearchResult = OrderLicenseServices.getOrderLicensesForActivityReport(searchBy, value); 
           } else {
              // only return a 10,000 row block of licenses
              int fromRow = (rangeIndex - 1) * BLOCK_SIZE + 1;
              int toRow = rangeIndex * BLOCK_SIZE;
              orderSearchResult = OrderLicenseServices.getOrderLicensesForActivityReport(searchBy, value, fromRow, toRow);                
           }

           for (int i=0; i < orderSearchResult.getOrderLicenses().size(); i++) {

	           OrderPurchase aPurchase;
	           OrderBundle aBundle;
	           OrderLicense aLicense;
	           User aUser;
	           ReportRow aRow;
	           
	           aLicense = orderSearchResult.getOrderLicenseByDisplaySequence(i);
	           if (aLicense.getProductCd().equals(ProductEnum.RL.name()) || 
	        	   aLicense.getProductCd().equals(ProductEnum.RLR.name())) {
	        	   continue;
	           }
	           aPurchase = orderSearchResult.getOrderPurchaseByDisplaySequence(i);
	           aBundle = orderSearchResult.getOrderBundleByDisplaySequence(i);
	           aUser = aPurchase.getUser();
	           
      		   aRow = new ReportRow(aPurchase, aBundle, aLicense, aUser);
      		   rows.add(aRow);

           }
        } catch (OrderLicensesException olex) {
           _logger.error("ReportUtil.getReportRows(): ", olex);
        } catch (Exception exc) {
           _logger.error("ReportUtil.getReportRows(): ", exc);
        }

        return rows;
    }

    /*
    * Return a List of ReportRow objects based on a String array
    * of invoice numbers.  Numbers are alpha numeric and can't contain commas
    * since commas are used in the UI as a separator
    * @param invoiceNumbers is a comma separated list of invoice numbers.
    */
    private final List<ReportRow> getReportRows(String invoiceNumbers) 
    {
        return this.getReportRows(invoiceNumbers, 0); // return all report rows
    }

    /*
    * Return a List of ReportRow objects based on a String array
    * of invoice numbers.  Numbers are alpha numeric and can't contain commas
    * since commas are used in the UI as a separator
    * @param invoiceNumbers is a comma separated list of invoice numbers.
    * @param rangeIndex is a result set range index that breaks the report 
    * rows returned into 10,000 row blocks.  This allows the administrative user 
    * to choose which part of a big report is downloaded to avoid timeouts.
    */
    private final List<ReportRow> getReportRows(String invoiceNumbers, int rangeIndex) 
    {
        List<ReportRow> rows = new ArrayList<ReportRow>();
        String searchBy = OrderMgmtSearchSortConstants.LICENSE_SEARCH_TYPE_INVOICE_NUMBER;
        OrderSearchResult orderSearchResult;
        
        try {
           if (rangeIndex == 0) {
              // return all Licenses
        	   orderSearchResult = OrderLicenseServices.getOrderLicensesForActivityReport(searchBy, invoiceNumbers);
           } else {
              // only return a 10,000 row block of licenses
              int fromRow = (rangeIndex - 1) * BLOCK_SIZE + 1;
              int toRow = rangeIndex * BLOCK_SIZE;
              orderSearchResult = OrderLicenseServices.getOrderLicensesForActivityReport(searchBy, invoiceNumbers, fromRow, toRow);
           }
           for (int i=0; i < orderSearchResult.getOrderLicenses().size(); i++) {

	           OrderPurchase aPurchase;
	           OrderBundle aBundle;
	           OrderLicense aLicense;
	           User aUser;
	           ReportRow aRow;
	           
	           aLicense = orderSearchResult.getOrderLicenseByDisplaySequence(i);
	           if (aLicense.getProductCd().equals(ProductEnum.RL.name()) || 
	        	   aLicense.getProductCd().equals(ProductEnum.RLR.name())) {
	        	   continue;
	           }
	           
	           aPurchase = orderSearchResult.getOrderPurchaseByDisplaySequence(i);
	           aBundle = orderSearchResult.getOrderBundleByDisplaySequence(i);
	           aUser = aPurchase.getUser();
	           
      		   aRow = new ReportRow(aPurchase, aBundle, aLicense, aUser);
      		   rows.add(aRow);

           }
        } catch (OrderLicensesException olex) {
           _logger.error("ReportUtil.getReportRows(): ", olex);
        } catch (Exception exc) {
           // add an ActionMessage for the user 
           _logger.error("ReportUtil.getReportRows(): ", exc);
        }             

        return rows;
    }
////////////////////////Experimental getReport()/////////////////////////////////

 private final void getReport(List<ReportRow> rows)
 {
    String lastRow = "";
    Iterator<ReportRow> itr = rows.iterator();

    while (itr.hasNext()) 
    {
       this.getRow(itr.next());
    }

    int rowCount = rows.size();

    if ( formatIsCSV() ) 
    {
       if (rowCount == 0) {
          lastRow = lastRowCSV;
       } 
    } else {
        if (rowCount > 0) {
           lastRow = HTML_END_TABLE;
       } else {
          lastRow = lastRowHTML;
       }
    }
    
    this.reportData.add(lastRow);
 }
///////////////////END Experimental getReport()////////////////////////////////

/////////////////////////// Experimental getRow() /////////////////////////////
/*
 * Get each row of the report and add it as a String to the reportData List
 */
 public void getRow(Object objectRow) 
 {
     ReportRow row = (ReportRow)objectRow;
     this.buffer.setLength(0); // clear the buffer

     try 
     {
         if ( formatIsHTML() ) { this.buffer.append("<tr><td>"); }
         
         OrderPurchase purchase = row.getOrderPurchase();
         OrderLicense license = row.getOrderLicense();
         User user = row.getUser();
         OrderBundle bundle = row.getOrderBundle();
         
         Date orderDate = purchase.getOrderDate(); 
         // Convert the beginDate and endDate to Date objects parsing the input Strings
         String dateStr = StringUtils.defaultIfEmpty(this.dateFormat.format(orderDate),getEmptyString());
         this.buffer.append(dateStr).append(getFieldSeparator());

         long confNum = purchase.getConfirmationNumber();
         this.buffer.append(confNum).append(getFieldSeparator());

         String acctNumStr = getEmptyString();
         String accountName = getEmptyString();
         String channel = getEmptyString();
         
        if (user != null) {
            ARAccount account = user.getAccount();
            ArAccountInfo accountInfo = user.getAccountInfo();
            Long acctNum = accountInfo.getArAccountNumber();

            if (acctNum != null) {
                acctNumStr = acctNum.toString();  
            }
            channel = user.getUserChannel();
        }

         // TODO: Is accounting reference the "Reference Number"
         this.buffer.append(acctNumStr).append(getFieldSeparator());

         if (OrderPurchaseServices.ORG.equals(channel) || OrderPurchaseServices.ORGADD.equals(channel)) 
         {    
             if (user != null) {
                 Organization org = user.getOrganization();
                 if (org != null) {
                    accountName =  org.getOrganizationName();
                    if (accountName != null && !"".equals(accountName)) {
                       if ( formatIsCSV() ) {
                          accountName =  this.filter(accountName);
                       }                    
                    } else { accountName =  getEmptyString(); }
                 } 
                 else { accountName = getEmptyString(); }
             }    
         } 
         else if (OrderPurchaseServices.IND.equals(channel)) 
         {
             if (user != null) {
                accountName = user.getDisplayName();
                if (accountName != null && !"".equals(accountName)) {
                   if (formatIsCSV()) {
                      accountName =  this.filter(accountName);
                   } 
                } else { accountName =  getEmptyString(); }
             }
         } 
         else 
         { 
        	 Party orgParty = user.getOrgParty();
        	 accountName = getEmptyString();
        	 if (orgParty != null) {
        		 if(!(orgParty.getAbbreviation() == null)) {
        			 accountName = orgParty.getAbbreviation();
        		 }
        	 }
        }

         this.buffer.append(accountName + getFieldSeparator() );

         // TODO: Is this the best way to map to Type of Use Strings?
         String usage = "";
         if (license.isAcademic()) {
            if (license.isAPS()) {
                usage = "\"Photocopy for academic coursepacks, classroom handouts.\"";
            } else  if (license.isECCS()) {
                if ( formatIsCSV() ) {
                   usage = "\"Posting for E-reserves, course management systems, e-coursepacks...\"";
                } else {
                   usage = "\"Posting for E-reserves, course management systems, e-coursepacks&hellip;\"";
                }
            } 
         } else {
            if (license.isPhotocopy()) {
               usage = "\"Photocopy for general business use, library reserves, ILL/document delivery.\"";
            } else if (license.isDigital()) {
               usage = "\"Use in e-mail, intranet/extranet/Internet postings.\"";
            } else if (license.isRepublication()) {
               if ( formatIsCSV() ) {
                  usage = "\"Republish into a book, journal, newsletter...\"";
               } else {
                   usage = "\"Republish into a book, journal, newsletter&hellip;\"";  
               }
            }  
         }

         this.buffer.append(usage).append(getFieldSeparator()); // type of use

         long prdInst = license.getPrdInst();
//         String lineRefNum = getEmptyString();
         
         // Only show reference number for APS and ECCS products
//         if (prdInst == ProductUsageConstants.APS_PRODUCT || 
//             prdInst == ProductUsageConstants.ECC_PRODUCT) 
//         {
//         }          
        
         boolean hasBundle = bundle != null;
         if ( hasBundle ) {
        	 String yourRef = StringUtils.defaultIfEmpty(this.filter(bundle.getYourReference()),getEmptyString());  // Reference for APS/ECC Header
        	 this.buffer.append(yourRef).append(getFieldSeparator());
         } else {
        	 this.buffer.append(getEmptyString()).append(getFieldSeparator());
         }
         
         if ( hasBundle ) {
        	 String accountRef = StringUtils.defaultIfEmpty(this.filter(bundle.getAccountingReference()),getEmptyString());
        	 this.buffer.append(accountRef).append(getFieldSeparator());
         } else {
        	 this.buffer.append(getEmptyString()).append(getFieldSeparator());
         }
         
         if ( hasBundle ) {
        	 String university = StringUtils.defaultIfEmpty(this.filter(bundle.getOrganization()),getEmptyString());
        	 this.buffer.append(university + getFieldSeparator());
         } else {
        	 this.buffer.append(getEmptyString()).append(getFieldSeparator());
         }
         
         if ( hasBundle ) {
        	 String courseName = StringUtils.defaultIfEmpty(this.filter(bundle.getCourseName()),getEmptyString());
        	 this.buffer.append(courseName).append(getFieldSeparator());
         } else {
        	 this.buffer.append(getEmptyString()).append(getFieldSeparator());
         }
         
         if ( hasBundle ) {
        	 String courseNumber = StringUtils.defaultIfEmpty(this.filter(bundle.getCourseNumber()),getEmptyString());
        	 this.buffer.append(courseNumber).append(getFieldSeparator());      
         } else {
        	 this.buffer.append(getEmptyString()).append(getFieldSeparator());
         }

         if ( hasBundle ) {
        	 String instructor = StringUtils.defaultIfEmpty(this.filter(bundle.getInstructor()),getEmptyString());
        	 this.buffer.append(instructor).append(getFieldSeparator());
         } else {
        	 this.buffer.append(getEmptyString()).append(getFieldSeparator());
         }
         String str;
         if ( hasBundle ) {
        	 
        	 Date startOfTerm = bundle.getStartOfTerm();

        	 if (startOfTerm != null) {
        		 str = StringUtils.defaultIfEmpty(this.dateFormat.format(startOfTerm),getEmptyString());
        		 this.buffer.append(str).append(getFieldSeparator());
        	 } else {
        		 this.buffer.append(getEmptyString()).append(getFieldSeparator());
        	 }
         } else {
        	 this.buffer.append(getEmptyString()).append(getFieldSeparator());
         }

         String url = this.filter(license.getWebAddress()); 
         
         Date dateOfPosting = license.getDateOfUse(); // TODO: Is this correct?

         // URL is only populated for DPS detail
         if (prdInst == ProductUsageConstants.DPS_PRODUCT) {
            this.buffer.append(url).append(getFieldSeparator());

            if (dateOfPosting != null) {
               str = StringUtils.defaultIfEmpty(this.dateFormat.format(dateOfPosting),getEmptyString());
            } else { str = getEmptyString(); }
 
            this.buffer.append(str).append(getFieldSeparator());
         } else {
            this.buffer.append(getEmptyString()).append(getFieldSeparator()); // URL
            this.buffer.append(getEmptyString()).append(getFieldSeparator()); // Date of Posting 
         }

         String repubWork = this.filter(license.getNewPublicationTitle());
         this.buffer.append(repubWork).append(getFieldSeparator());

         Date repubDate = license.getRepublicationDate();
         if (repubDate != null) {
            str = StringUtils.defaultIfEmpty(this.dateFormat.format(repubDate),getEmptyString());
         } else { str = getEmptyString(); }

         this.buffer.append(str).append(getFieldSeparator());

         long id = license.getID();
         this.buffer.append(id).append(getFieldSeparator());

//         String notes = this.filter(license.getCustomerRef());
         String notes = this.filter(license.getYourReference());
         this.buffer.append(notes).append(getFieldSeparator());

         String stdNumber = license.getStandardNumber();

         if ( formatIsCSV() ) {
            stdNumber = "'" + stdNumber + "'";
         }
         this.buffer.append(stdNumber).append(getFieldSeparator());

         String publication = StringUtils.defaultIfEmpty(this.filter(license.getPublicationTitle()),getEmptyString());
         this.buffer.append(publication).append(getFieldSeparator()); // Requested Publication??

//         String pubYear = StringUtils.defaultIfEmpty(license.getPublicationYear(),getEmptyString());
         String pubYear = StringUtils.defaultIfEmpty(license.getPublicationYearOfUse(),getEmptyString());
         this.buffer.append(pubYear).append(getFieldSeparator());
         
         String chapArticle = StringUtils.defaultIfEmpty(this.filter(license.getChapterArticle()),getEmptyString());
         this.buffer.append(chapArticle).append(getFieldSeparator());
         
         long numOfPages = license.getNumberOfPages();
         if (numOfPages > 0) {
            this.buffer.append(numOfPages).append(getFieldSeparator());
         } else {
            this.buffer.append(getEmptyString()).append(getFieldSeparator());  
         }

         long num;
         if (license.isAcademic()) {
            num = license.getNumberOfStudents();
         } else {
            num = license.getNumberOfCopies();
         }

         if (num >= 0) {
            this.buffer.append(num).append(getFieldSeparator());  
         } else {
            this.buffer.append(getEmptyString()).append(getFieldSeparator()); 
         }
         
         if (prdInst == ProductUsageConstants.DPS_PRODUCT) 
         {
             int duration = license.getDuration();
             if (duration >= 0) 
             {
                String durationStr = "";
                switch (duration) {
                    case 0:
                      durationStr = "Up to 30 days";
                    break;
                    case 1:
                      durationStr = "Up to 180 days";
                    break;
                    case 2:
                      durationStr = "Up to 365 days";
                    break;
                    case 3:
                      durationStr = "Unlimited";
                    break;
                }
                
                this.buffer.append(durationStr).append(getFieldSeparator()); 
             } else {
                this.buffer.append(getEmptyString()).append(getFieldSeparator()); 
             }
         } else {
            this.buffer.append(getEmptyString()).append(getFieldSeparator());  
         }
 
         long recipients = license.getNumberOfRecipients();
         if (recipients >= 0) {
            this.buffer.append(recipients).append(getFieldSeparator()); 
         } else {
            this.buffer.append(getEmptyString()).append(getFieldSeparator()); 
         }
         
         // URL is only populated for DPS detail
         if (prdInst == ProductUsageConstants.RLS_PRODUCT) {
             int circDist = license.getCirculationDistribution();
             if (circDist > 0) {
                this.buffer.append(circDist).append(getFieldSeparator());
             } else {
                this.buffer.append(getEmptyString()).append(getFieldSeparator()); 
             }
         } else {
            this.buffer.append(getEmptyString()).append(getFieldSeparator()); 
         }

         String userName = getEmptyString();
         if (user != null) userName = this.filter(user.getUsername());
         this.buffer.append(userName).append(getFieldSeparator());

         double royalty = license.getRoyaltyComposite();
         String fee = this.currencyFormat.format(royalty);
         if ( formatIsCSV() ) { 
            fee = quote + fee + quote;
         }
         this.buffer.append(fee).append(getFieldSeparator());

         double cccFee = license.getLicenseeFee();
         fee = this.currencyFormat.format(cccFee);
         if ( formatIsCSV() ) { 
            fee = quote + fee + quote;
         }
         this.buffer.append(fee).append(getFieldSeparator());

         double discount = license.getDiscount();
         fee = this.currencyFormat.format(discount);
         if ( formatIsCSV() ) { 
            fee = quote + fee + quote;
         }
         this.buffer.append(fee).append(getFieldSeparator());
         
         String licensePrice = license.getPrice();
         if ( formatIsCSV() ) { 
            licensePrice = quote + licensePrice + quote;
         }
         this.buffer.append(licensePrice).append(getFieldSeparator());
         
         String creditAuth = license.getCreditAuth();
         String payMethod = "Invoice";
         if (creditAuth != null && !"".equals(creditAuth)) {
            payMethod = "Credit Card";
         }

         this.buffer.append(payMethod).append(getFieldSeparator());

         Date invoiceDate = license.getInvoiceDate();
         if (invoiceDate != null) {
            str = StringUtils.defaultIfEmpty(this.dateFormat.format(invoiceDate),getEmptyString());
         } else { str = getEmptyString(); }
         this.buffer.append(str).append(getFieldSeparator());
         
         // Invoice Number only shows for Invoices not CC orders
         String invId = license.getInvoiceId();
         if (creditAuth == null || "".equals(creditAuth)) {
            if (invId == null) { invId = getEmptyString(); }
         } else {
            invId = getEmptyString(); // suppress invoice id for cc orders 
         }
         this.buffer.append(invId);

         if ( formatIsCSV() ) { 
            this.buffer.append(CSV_END_ROW); 
         } else {
            this.buffer.append(HTML_END_TABLE_ROW);
         }

         this.reportData.add(this.buffer.toString());

     } catch (Exception exc) {
 		_logger.error( ExceptionUtils.getFullStackTrace(exc) );
     }
 }  
////////////////////////////END Experimental getRow()////////////////////////////////

    public final void  getRowSave(ReportRow row) 
    {
        try 
        {
            if (formatIsHTML()) { this.buffer.append("<tr><td>"); }
            
            OrderPurchase purchase = row.getOrderPurchase();
            OrderLicense license = row.getOrderLicense();
            OrderBundle bundle = row.getOrderBundle();
            if (bundle == null) {
            	bundle = new OrderItemBundleImpl();
            }
            User user = row.getUser();
            
            Date orderDate = license.getCreateDate(); // TODO: Is this the same as the order date?
            // Convert the beginDate and endDate to Date objects parsing the input Strings
//SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            String dateStr = StringUtils.defaultIfEmpty(this.dateFormat.format(orderDate),getEmptyString());
            buffer.append(dateStr).append(getFieldSeparator());

            long confNum = purchase.getPurInst();
            buffer.append(confNum).append(getFieldSeparator());

            String accountName = getEmptyString();
            String acctNumStr = getEmptyString();
            String channel = getEmptyString();

            if (user != null) {
                ARAccount account = user.getAccount(); 
                Long acctNum = Long.valueOf(account.getAccountNumber());
                if (acctNum != null) {
                   acctNumStr = acctNum.toString();  
                }
                channel = user.getUserChannel();
            }

            // TODO: Is accounting reference the "Reference Number"
            buffer.append(acctNumStr).append(getFieldSeparator());
            
            if (OrderPurchaseServices.ORG.equals(channel) || OrderPurchaseServices.ORGADD.equals(channel)) 
            {
                if (user != null) {
                    Organization org = user.getOrganization();
                    if (org != null) {
                       accountName =  org.getOrganizationName();
                       if (accountName != null && !"".equals(accountName)) {
                          if ( formatIsCSV() ) {
                             accountName =  this.filter(accountName);
                          }                    
                       } else { accountName =  getEmptyString(); }
                    } else { accountName = getEmptyString(); }
                }         
            } 
            else if (OrderPurchaseServices.IND.equals(channel)) 
            {  
                if (user != null) {    
                    accountName = user.getDisplayName();
                    if (accountName != null && !"".equals(accountName)) {
                        if ( formatIsCSV() ) {
                            accountName =  this.filter(accountName);
                        } 
                    } else { accountName =  getEmptyString(); }
                }
            } else { accountName = getEmptyString(); }

            buffer.append(accountName).append(getFieldSeparator());

            // TODO: Is this the best way to map to Type of Use Strings?
            String usage = "";
            if (license.isAcademic()) {
               if (license.isAPS()) {
                   usage = "\"Photocopy for academic coursepacks, classroom handouts.\"";
               } else  if (license.isECCS()) {
                   if ( formatIsCSV() ) {
                      usage = "\"Posting for E-reserves, course management systems, e-coursepacks...\"";
                   } else {
                      usage = "\"Posting for E-reserves, course management systems, e-coursepacks&hellip;\"";
                   }
               } 
            } else {
               if (license.isPhotocopy()) {
                  usage = "\"Photocopy for general business use, library reserves, ILL/document delivery.\"";
               } else if (license.isDigital()) {
                  usage = "\"Use in e-mail, intranet/extranet/Internet postings.\"";
               } else if (license.isRepublication()) {
                  if (formatIsCSV()) {
                     usage = "\"Republish into a book, journal, newsletter...\"";
                  } else {
                      usage = "\"Republish into a book, journal, newsletter&hellip;\"";  
                  }
               }  
            }

            buffer.append(usage).append(getFieldSeparator()); // type of use

            long prdInst = license.getPrdInst();
//            String lineRefNum = getEmptyString();
            
            // Only show reference number for APS and ECCS products
//            if (prdInst == ProductUsageConstants.APS_PRODUCT || 
//                prdInst == ProductUsageConstants.ECC_PRODUCT) 
//            {
//               lineRefNum = this.filter(bundle.getYourReference());
//            }          

            String refNum = this.filter(bundle.getYourReference());  // Ref Num on APS & ECCS header
            buffer.append(refNum).append(getFieldSeparator());

            String accountRef = this.filter(bundle.getAccountingReference());
            buffer.append(accountRef).append(getFieldSeparator());
            
            String university = this.filter(bundle.getOrganization());
            buffer.append(university).append(getFieldSeparator());
            
            String courseName = this.filter(bundle.getCourseName());
            buffer.append(courseName).append(getFieldSeparator());
            
            String courseNumber = this.filter(bundle.getCourseNumber());
            buffer.append(courseNumber).append(getFieldSeparator());        

            String instructor = this.filter(bundle.getInstructor());
            buffer.append(instructor).append(getFieldSeparator());

            String str;
//SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            Date startOfTerm = bundle.getStartOfTerm();

            if (startOfTerm != null) {
               str = StringUtils.defaultIfEmpty(this.dateFormat.format(startOfTerm),getEmptyString());
               buffer.append(str).append(getFieldSeparator());
            } else {
               buffer.append(getEmptyString()).append(getFieldSeparator());
            }

            String url = this.filter(license.getWebAddress()); 
            
            Date dateOfPosting = license.getDateOfUse(); // TODO: Is this correct?

            // URL is only populated for DPS detail
            if (prdInst == ProductUsageConstants.DPS_PRODUCT) {
               buffer.append(url).append(getFieldSeparator()); 

               if (dateOfPosting != null) {
                  str = StringUtils.defaultIfEmpty(this.dateFormat.format(dateOfPosting),getEmptyString());
               } else { str = getEmptyString(); }
    
               buffer.append(str).append(getFieldSeparator());
            } else {
               buffer.append(getEmptyString()).append(getFieldSeparator()); // URL
               buffer.append(getEmptyString()).append(getFieldSeparator()); // Date of Posting 
            }

            String repubWork = this.filter(license.getNewPublicationTitle());
            buffer.append(repubWork).append(getFieldSeparator());

            Date repubDate = license.getRepublicationDate();
            if (repubDate != null) {
               str = StringUtils.defaultIfEmpty(this.dateFormat.format(repubDate),getEmptyString());
            } else { str = getEmptyString(); }

            buffer.append(str).append(getFieldSeparator());

            long id = license.getID();
            buffer.append(id).append(getFieldSeparator());

//            String notes = this.filter(license.getCustomerRef());
            String notes = this.filter(license.getYourReference());
            buffer.append(notes).append(getFieldSeparator());

            String stdNumber = license.getStandardNumber();

            if ( formatIsCSV() ) {
               stdNumber = "'" + stdNumber + "'";
            }
            buffer.append(stdNumber).append(getFieldSeparator());

            String publication = this.filter(license.getPublicationTitle());
            buffer.append(publication).append(getFieldSeparator()); // Requested Publication??

            String pubYear = license.getPublicationYear();
            buffer.append(pubYear).append(getFieldSeparator());;
            
            String chapArticle = this.filter(license.getChapterArticle());
            buffer.append(chapArticle).append(getFieldSeparator());
            
            long numOfPages = license.getNumberOfPages();
            if (numOfPages > 0) {
               buffer.append(numOfPages).append(getFieldSeparator());
            } else {
               buffer.append(getEmptyString()).append(getFieldSeparator());  
            }

            long num;
            if (license.isAcademic()) {
               num = license.getNumberOfStudents();
            } else {
               num = license.getNumberOfCopies();
            }
            if (num >= 0) {
               buffer.append(num).append(getFieldSeparator());
            } else {
                buffer.append(getEmptyString()).append(getFieldSeparator()); 
            }
            
            if (prdInst == ProductUsageConstants.DPS_PRODUCT) 
            {
                int duration = license.getDuration();
                if (duration >= 0) 
                {
                   String durationStr = "";
                   switch (duration) {
                       case 0:
                         durationStr = "Up to 30 days";
                       break;
                       case 1:
                         durationStr = "Up to 180 days";
                       break;
                       case 2:
                         durationStr = "Up to 365 days";
                       break;
                       case 3:
                         durationStr = "Unlimited";
                       break;
                   }
                   
                   buffer.append(durationStr).append(getFieldSeparator());
                } else {
                   buffer.append(getEmptyString()).append(getFieldSeparator()); 
                }
            } else {
                buffer.append(getEmptyString()).append(getFieldSeparator()); 
            }
    
            long recipients = license.getNumberOfRecipients();
            if (recipients >= 0) {
               buffer.append(recipients).append(getFieldSeparator());
            } else {
               buffer.append(getEmptyString()).append(getFieldSeparator()); 
            }
            
            // URL is only populated for DPS detail
            if (prdInst == ProductUsageConstants.RLS_PRODUCT) {
                int circDist = license.getCirculationDistribution();
                if (circDist > 0) {
                   buffer.append(circDist).append(getFieldSeparator());
                } else {
                   buffer.append(getEmptyString()).append(getFieldSeparator());  
                }
            } else {
               buffer.append(getEmptyString()).append(getFieldSeparator()); 
            }

            String userName = getEmptyString();
            if (user != null) userName = this.filter(user.getUsername());
            buffer.append(userName).append(getFieldSeparator());

//NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
            double royalty = license.getRoyaltyComposite();
            String fee = this.currencyFormat.format(royalty);
            if ( formatIsCSV() ) { 
               fee = quote + fee + quote;
            }
            buffer.append(fee).append(getFieldSeparator());

            double cccFee = license.getLicenseeFee();
            fee = this.currencyFormat.format(cccFee);
            if ( formatIsCSV() ) { 
               fee = quote + fee + quote;
            }
            buffer.append(fee).append(getFieldSeparator());

            double discount = license.getDiscount();
            fee = this.currencyFormat.format(discount);
            if ( formatIsCSV() ) { 
               fee = quote + fee + quote;
            }
            buffer.append(fee).append(getFieldSeparator());
            
            String licensePrice = license.getPrice();
            if ( formatIsCSV() ) { 
               licensePrice = quote + licensePrice + quote;
            }
            buffer.append(licensePrice).append(getFieldSeparator());
            
            String creditAuth = license.getCreditAuth();
            String payMethod = "Invoice";
            if (creditAuth != null && !"".equals(creditAuth)) {
               payMethod = "Credit Card";
            }

            buffer.append(payMethod).append(getFieldSeparator());

            Date invoiceDate = license.getInvoiceDate();
            if (invoiceDate != null) {
               str = StringUtils.defaultIfEmpty(this.dateFormat.format(invoiceDate),getEmptyString());
            } else { str = getEmptyString(); }
            buffer.append(str).append(getFieldSeparator());
            
            // Invoice Number only shows for Invoices not CC orders
            String invId = license.getInvoiceId();
            if (creditAuth == null || "".equals(creditAuth)) {
               if (invId == null) { invId = getEmptyString(); }
            } else {
               invId = getEmptyString(); // suppress invoice id for cc orders 
            }
            buffer.append(invId);

            if ( formatIsCSV() ) { 
               buffer.append(CSV_END_ROW); 
            } else {
               buffer.append(HTML_END_TABLE_ROW);
            }
        } catch (Exception exc) {
    		_logger.error( ExceptionUtils.getFullStackTrace(exc) );
        }
    }  

    protected void getCSVHeader()
    {
        //StringBuffer buffer =
        //new StringBuffer("Order Date,Confirm. #,CCC Acct. #,CCC Acct. Name,Type of Use,Ref. #,Acct. Ref. #,University,Course Name,Course Number,Instructor,Start of Term,URL,Date of Posting,Repub. Work,Repub. Date,Detail ID,Your Notes,Standard #,Requested Publication,Pub. Year,Chapter/Article Name,Pages,Copies/Num. of Students,Max. Duration - days,Recipients,Circ/Dist,Username,Royalty,CCC fee,Discount,License Price,Payment Method,Invoice Date,Invoice #\n");
        String header = "Order Date,Confirm. #,CCC Acct. #,CCC Acct. Name,Type of Use,Ref. #,Acct. Ref. #,University,Course Name,Course Number,Instructor,Start of Term,URL,Date of Posting,Repub. Work,Repub. Date,Detail ID,Your Notes,Standard #,Requested Publication,Pub. Year,Chapter/Article Name,Pages,Copies/Num. of Students,Max. Duration - days,Recipients,Circ/Dist,Username,Royalty,CCC fee,Discount,License Price,Payment Method,Invoice Date,Invoice #\n";
        //return buffer;
        this.reportData.add(header);
    }
    
    protected void getHTMLHeader() 
    {
       //StringBuffer buffer = 
       //new StringBuffer("<table border=\"1\" align=\"center\"><tr><th>Order Date</th><th>Confirm. #</th><th>CCC Acct. #</th><th>CCC Acct. Name</th><th>Type of Use</th><th>Ref. #</th><th>Acct. Ref. #</th><th>University</th><th>Course Name</th><th>Course Number</th><th>Instructor</th><th>Start of Term</th><th>URL</th><th>Date of Posting</th><th>Repub. Work</th><th>Repub. Date</th><th>Detail ID</th><th>Your Notes</th><th>Standard #</th><th>Requested Publication</th><th>Pub. Year</th><th>Chapter/Article</th><th>Pages</th><th>Copies/Num. of Students</th><th>Max. Duration - days</th><th>Recipients</th><th>Circ/Dist</th><th>Username</th><th>Royalty</th><th>CCC fee</th><th>Discount</th><th>License Price</th><th>Payment Method</th><th>Invoice Date</th><th>Invoice #</th></tr>");
       String header = "<table border=\\\"1\\\" align=\\\"center\\\"><tr><th>Order Date</th><th>Confirm. #</th><th>CCC Acct. #</th><th>CCC Acct. Name</th><th>Type of Use</th><th>Ref. #</th><th>Acct. Ref. #</th><th>University</th><th>Course Name</th><th>Course Number</th><th>Instructor</th><th>Start of Term</th><th>URL</th><th>Date of Posting</th><th>Repub. Work</th><th>Repub. Date</th><th>Detail ID</th><th>Your Notes</th><th>Standard #</th><th>Requested Publication</th><th>Pub. Year</th><th>Chapter/Article</th><th>Pages</th><th>Copies/Num. of Students</th><th>Max. Duration - days</th><th>Recipients</th><th>Circ/Dist</th><th>Username</th><th>Royalty</th><th>CCC fee</th><th>Discount</th><th>License Price</th><th>Payment Method</th><th>Invoice Date</th><th>Invoice #</th></tr>";
       //return buffer;
       this.reportData.add(header);
    }
    
    private final String filter(String s) 
    {
       if (s != null && !"".equals(s)) {
          s = filterCSV(s);
       } else { s = getEmptyString(); }
       
       return s;
    }
    
    private final String getDateSearchString(ViewReportsActionForm form) 
    {
        Date d1 = form.getFirstDateObject();
        Date d2 = form.getLastDateObject();

        return this.getSearchString(d1, d2);
    }
    
    private final String getSearchString(Date d1, Date d2) 
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String dateStr1 = dateFormat.format(d1);
        String dateStr2 = dateFormat.format(d2);
        
        return dateStr1 + ":" + dateStr2;   
    }
    
    /*
    * Filter a CSV string for commas since a comma will prematurely terminate
    * the String in Excel causing column shifting.  A String with an embedded
    * comma must be surrounded by double quotes.  Any embedded double quotes
    * should be escaped by another double quote.
    */
    private final String filterCSV(String csvString) 
    {
        String twoQuotes = "\"\"";
        if (csvString != null) 
        {
            int idx = csvString.indexOf(CSV_FIELD_SEPARATOR);
            if (idx >= 0) {
               // The String has an embedded comma so check for double quotes
               int idx2 = csvString.indexOf(quote);
               if (idx2 >= 0) {
                  // escape the double quotes with an extra double quote
                  csvString = csvString.replaceAll(quote, twoQuotes); 
               } 
               // Surround the String in double quotes  
               csvString = quote + csvString + quote;
            }    
        }
        return csvString;
    }

    public void setReportData(List<String> reportData) { this.reportData = reportData; }
    public List<String> getReportData() { return reportData; }

	/**
	 * @param lastRowCSV the lastRowCSV to set
	 */
	public void setLastRowCSV(String lastRowCSV)
	{
		this.lastRowCSV = lastRowCSV;
	}

	

	/**
	 * @param lastRowHTML the lastRowHTML to set
	 */
	public void setLastRowHTML(String lastRowHTML)
	{
		this.lastRowHTML = lastRowHTML;
	}
}
