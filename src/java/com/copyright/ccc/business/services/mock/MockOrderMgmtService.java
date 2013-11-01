package com.copyright.ccc.business.services.mock;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.copyright.ccc.business.data.DisplaySpec;
import com.copyright.ccc.business.data.MockOrderPurchase;
import com.copyright.ccc.business.data.OrderLicense;
import com.copyright.ccc.business.data.OrderLicenses;
import com.copyright.ccc.business.data.OrderPurchase;
import com.copyright.ccc.business.data.OrderPurchases;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.DisplaySpecServices;
import com.copyright.ccc.business.services.order.OrderPurchasesException;
import com.copyright.ccc.web.mock.MockOrderLicense;
import com.copyright.data.order.OrderMgmtException;
import com.copyright.data.order.Purchase;

public class MockOrderMgmtService 
{
   private static final Logger _logger = Logger.getLogger( MockOrderMgmtService.class );
   private final int ALL = -1;
   //private final int ORDER_DATE = 0;
   //private final int CONF_NUM   = 1;	
   //private final int SCHOOL     = 2;	
   private final int TERM_START = 3;	
   //private final int COURSE_NAME = 4;  	
   //private final int COURSE_NUM  = 5;  	
   //private final int INSTRUCTOR  = 6;   
   //private final int YOUR_REF    = 7; 
   private final int ID = 8; // Order Detail Id
   
   public OrderPurchases getAllPurchases(int page)
   {
      return buildPurchaseList(ALL);
   }
   
   public OrderPurchases getPurchasesByOrderDate(Date dateFrom, Date dateTo, int page) 
   {
       SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
       OrderPurchases purchases = null;
       
       if (dateFrom != null && dateTo != null) {
          String date1 = format.format(dateFrom);
          String date2 = format.format(dateTo);
          purchases = this.getPurchasesByOrderDate(date1, date2, page);
       }
       
       return purchases;
   }
   
   public OrderPurchases getPurchasesByOrderDate(String date1, String date2, int page)
   { 
      OrderPurchases purchases = this.buildAll(page);
      Collection<OrderPurchase> dispPurchases = purchases.getDisplayPurchaseList();
      DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);
      try {
    
        Date d1 = format.parse(date1);
        long t1 = d1.getTime();
        
        Date d2 = format.parse(date2);
        long t2 = d2.getTime();
        
        if (t2 < t1) {
           long tmp = t1;
           t1 = t2;
           t2 = tmp;
        }
        
        List<OrderPurchase> results = new ArrayList<OrderPurchase>();
        
        Iterator<OrderPurchase> itr = dispPurchases.iterator();
        while (itr.hasNext()) 
        {
           OrderPurchase purchase = itr.next();
           Date purchDate = purchase.getPurchaseDate();
           long purchTime = purchDate.getTime();
           if (purchTime >= t1 && purchTime <= t2) {
           	  results.add(purchase);
           }
        }
        purchases = new OrderPurchases();
        purchases.setDisplayPurchaseList(results);
        
      } catch (ParseException pe) {
      	purchases = new OrderPurchases(); // empty return list
      	// should throw an exception here for the UI but this is a mock class
      }
      
      return purchases;
   }
   
   public OrderPurchases getPurchasesByConfNum(String confNum, int page)
   { 
      OrderPurchases purchases = this.buildAll(page);
      Collection<OrderPurchase> dispPurchases = purchases.getDisplayPurchaseList();
     
      List<OrderPurchase> results = new ArrayList<OrderPurchase>();
        
      Iterator<OrderPurchase> itr = dispPurchases.iterator();
      while (itr.hasNext()) 
      {
         OrderPurchase purchase = itr.next();
         long num = purchase.getPurInst();
         try {
            long confNumLong = Long.parseLong(confNum);
            if (num == confNumLong) {
               results.add(purchase);
            }
         } catch (NumberFormatException nfe) {
            purchases = new OrderPurchases(); // empty return list
      	    // should throw an exception here for the UI but this is a mock class
         }
      }

      purchases.setDisplayPurchaseList(results);

      return purchases;
   }

   public OrderPurchases getPurchasesBySchool(String school, int page)
   { 
       OrderPurchases purchases = this.buildAll(page);
       Collection<OrderPurchase> dispPurchases = purchases.getDisplayPurchaseList();

       List<OrderPurchase> results = new ArrayList<OrderPurchase>();

       Iterator<OrderPurchase> itr = dispPurchases.iterator();
       while (itr.hasNext()) 
       {
          OrderPurchase purchase = itr.next();
          String university = purchase.getSchool();
          if (university != null) {
              university = university.trim();
              if (university.equalsIgnoreCase(school)) {
                 results.add(purchase);
              } 
           }
       }

       purchases.setDisplayPurchaseList(results);
       return purchases;
   }
   	
   public OrderPurchases getPurchasesByTermStart(String date1, String date2)
   { 
      return buildPurchaseList(TERM_START);
   }
   
   public OrderPurchases getPurchasesByCourseName(String name, int page)
   { 
      if (name == null) {
      	name = "";
      } else {
      	name = name.trim();
      }
      OrderPurchases purchases = this.buildAll(page);
      Collection<OrderPurchase> dispPurchases = purchases.getDisplayPurchaseList();
     
      List<OrderPurchase> results = new ArrayList<OrderPurchase>();
        
      Iterator<OrderPurchase> itr = dispPurchases.iterator();
      while (itr.hasNext()) 
      {
         OrderPurchase purchase = itr.next();
         String courseName = purchase.getCourseName();
         if (courseName != null) {
         	courseName = courseName.trim();
         	if (name.equalsIgnoreCase(courseName)) {
         	   results.add(purchase);
            } 
         }
      }

      purchases.setDisplayPurchaseList(results);
        
      return purchases;
   }	
   
   public OrderPurchases getPurchasesByCourseNumber(String number, int page)
   { 
      if (number == null) {
      	number = "";
      } else {
      	number = number.trim();
      }

      OrderPurchases purchases = this.buildAll(page);
      Collection<OrderPurchase> dispPurchases = purchases.getDisplayPurchaseList();
     
      List<OrderPurchase> results = new ArrayList<OrderPurchase>();
        
      Iterator<OrderPurchase> itr = dispPurchases.iterator();
      while (itr.hasNext()) 
      {
         OrderPurchase purchase = itr.next();
         String courseNumber = purchase.getCourseNumber();
         if (courseNumber != null) {
             courseNumber = courseNumber.trim();
             if (number.equalsIgnoreCase(courseNumber)) {
                results.add(purchase);
             }
         } 
      }

      purchases.setDisplayPurchaseList(results);
        
      return purchases;
   }
   
   public OrderPurchases getPurchasesByInstructor(String instructor, int page)
   { 
      if (instructor == null) {
      	instructor = "";
      } else {
      	instructor = instructor.trim();
      }

      OrderPurchases purchases = this.buildAll(page);
      Collection<OrderPurchase> dispPurchases = purchases.getDisplayPurchaseList();
     
      List<OrderPurchase> results = new ArrayList<OrderPurchase>();
        
      Iterator<OrderPurchase> itr = dispPurchases.iterator();
      while (itr.hasNext()) 
      {
         OrderPurchase purchase = itr.next();
         String instr = purchase.getInstructor();
         if (instructor != null) {
            instructor = instructor.trim();
            if (instructor.equalsIgnoreCase(instr)) {
                results.add(purchase);
	    }    
         }
      }

      purchases.setDisplayPurchaseList(results);
        
      return purchases;
   }
   
   public OrderPurchases getPurchasesByYourRef(String reference, int page)
   { 
      if (reference == null) {
      	reference = "";
      } else {
      	reference = reference.trim();
      }

      OrderPurchases purchases = this.buildAll(page);
      Collection<OrderPurchase> dispPurchases = purchases.getDisplayPurchaseList();
     
      List<OrderPurchase> results = new ArrayList<OrderPurchase>();
        
      Iterator<OrderPurchase> itr = dispPurchases.iterator();
      while (itr.hasNext()) 
      {
         OrderPurchase purchase = itr.next();
         String ref = purchase.getYourReference();
         if (ref != null) {
            ref = ref.trim();
            if (reference.equalsIgnoreCase(ref)) {
	       results.add(purchase);
	    }    
         }
      }

      purchases.setDisplayPurchaseList(results);
        
      return purchases;
   }	
   
   public OrderPurchases getPurchasesByPubTitle(String title, int page)
   { 
       if (title == null) { title = ""; } 
       else { title = title.trim(); }

       OrderPurchases purchases = this.buildAll(page);
       Collection<OrderPurchase> dispPurchases = purchases.getDisplayPurchaseList();
      
       List<OrderPurchase> results = new ArrayList<OrderPurchase>();

       title = title.trim();  
       
       Iterator<OrderPurchase> itr = dispPurchases.iterator();
       while (itr.hasNext()) 
       {
          MockOrderPurchase purchase = (MockOrderPurchase) itr.next();
          String purchStr = purchase.getPurchaseTitles();

          if (purchStr != null && !"".equals(purchStr)) 
          {
              String[] purchTitles = purchStr.split(",");

              for (int i=0;i<purchTitles.length;i++) 
              {
                  String aTitle = purchTitles[i];
                  if (title.equalsIgnoreCase(aTitle)) {
                      results.add(purchase); 
                  }
              }              
          }
       }

       purchases.setDisplayPurchaseList(results);
         
       return purchases;
   }
    
   public OrderPurchases getPurchases(boolean closedNotOpen, int page)
   { 
         OrderPurchases purchases = this.buildAll(page);
         Collection<OrderPurchase> dispPurchases = purchases.getDisplayPurchaseList();
        
         List<OrderPurchase> results = new ArrayList<OrderPurchase>();

         Iterator<OrderPurchase> itr = dispPurchases.iterator();
         while (itr.hasNext()) 
         {
            MockOrderPurchase purchase = (MockOrderPurchase) itr.next();
            MockPurchase purch = (MockPurchase) purchase.getPurchase();
            boolean isOpen = purch.isOpen();

            if (!isOpen == closedNotOpen) {
               results.add(purchase); 
            }
         }
         
         purchases.setDisplayPurchaseList(results);
           
         return purchases;
   }

   public void cancelPurchase(String id, int page) 
   {
      if (id != null && !"".equals(id)) {
         OrderPurchases purchases = this.getPurchasesByConfNum(id, page);   
         
         long testId = Long.parseLong(id);
         
         List<OrderPurchase> list = purchases.getDisplayPurchaseList();
         Iterator<OrderPurchase> itr = list.iterator();
         while (itr.hasNext())
         {
             MockOrderPurchase mockPurchase = (MockOrderPurchase) itr.next();
             long purInst = mockPurchase.getPurInst();
             if (purInst == testId) {
                mockPurchase.setCancelable(false);
                this.cancelItems(mockPurchase);
             }
         }
      }
   }
   
   public OrderPurchases getOpenPurchases(int page)
   {      
      return this.getPurchases(false, page);
   }

   public OrderPurchases getClosedPurchases(int page)
   {      
      return this.getPurchases(true, page);
   }
    
   private void cancelItems(MockOrderPurchase purchase) 
   {
      long id = purchase.getPurInst();
      OrderLicenses licenses = this.getLicensesByConfNum(id);
      // ??? Should be display license list but this doesn't work yet
      List<OrderLicense> items = licenses.getOrderLicenseList();
      Iterator<OrderLicense> itr = items.iterator();
      while (itr.hasNext()) {
         MockOrderLicense license = (MockOrderLicense) itr.next();
         license.setBillingStatus("Canceled");
         license.setPrice("$0.00");
      }
   }
    
   private OrderPurchases buildPurchaseList(int currPage)
   {
   	 return this.buildAll(currPage);
   }
   
   private OrderPurchases buildAll(int page)
   {
          OrderPurchases all;
   
   	  all = new OrderPurchases();
   	  List<OrderPurchase> purchases = new ArrayList<OrderPurchase>();
   	     	  
   	  DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);
   	  Date orderDate = new Date();
   	  java.sql.Date purchDate = null;
   	  
   	  try {
   	    orderDate = format.parse("06/12/06");
   	    purchDate = new java.sql.Date(orderDate.getTime());
   	  } catch (ParseException pe) {
   		_logger.error( ExceptionUtils.getFullStackTrace(pe) );
   	  }
   	  
   	  String purchaseTitles = "Newsweek, Business Week";
          Purchase purch = new MockPurchase(1285511L,2L,purchDate);
          
   	  OrderPurchase purchase = new MockOrderPurchase(purch, purchaseTitles);
   	  
   	  // Academic attributes
   	  purchase.setSchool("");
   	  purchase.setCourseNumber("");
   	  purchase.setCourseName("");
   	  purchase.setNumberOfStudents(0);
   	  purchase.setInstructor("");
   	  purchase.setYourReference("");
   	  purchase.setAccountingReference("");
   	  purchase.setOrderEnteredBy("");
   	  purchases.add(purchase);

          // TODO: Remove this cast when OrderPurchase has closed implemented
          MockOrderPurchase mockPurchase = (MockOrderPurchase) purchase;
          mockPurchase.setCancelable(true);
          mockPurchase.setClosed(false);

   	  //////////////////////////////////////
   	  
   	  try {
   	    orderDate = format.parse("06/18/06");
   	    purchDate = new java.sql.Date(orderDate.getTime());
   	  } catch (ParseException pe) {
     		_logger.error( ExceptionUtils.getFullStackTrace(pe) );
   	  }
   	  
   	  purchaseTitles = "Wall Street Journal, Journal of Applied Physics, Sailing Today, This Old House";
          purch = new MockPurchase(1285512L, 4L, purchDate, "120106");
   	  purchase = new MockOrderPurchase(purch, purchaseTitles);
   	  //purchase.setConfirmNumber("1285512");
   	  //purchase.setPoNumber("120106");
   	  
   	  // Academic attributes
   	  purchase.setSchool("Loyola Marymount");
   	  try {
   	    Date termStart = format.parse("01/28/07");
   	    purchase.setStartOfTerm(termStart);
   	  } catch (ParseException pe) {
     		_logger.error( ExceptionUtils.getFullStackTrace(pe) );
   	  }

   	  purchase.setCourseNumber("1-28-2007");
   	  purchase.setCourseName("PHYS 201");
   	  purchase.setNumberOfStudents(14);
   	  purchase.setInstructor("John Smith");
   	  purchase.setYourReference("request123");
   	  purchase.setAccountingReference("Physics Dept");
   	  purchase.setOrderEnteredBy("Jane Smith");
          
          // TODO: Remove this cast when OrderPurchase has closed implemented
          mockPurchase = (MockOrderPurchase) purchase;
          mockPurchase.setClosed(false);
          
   	  purchases.add(purchase);

      ////////////////////////////////////// 
   
   	  try {
   	     orderDate = format.parse("07/2/2006");
   	     purchDate = new java.sql.Date(orderDate.getTime());
   	  } catch (ParseException pe) {
     		_logger.error( ExceptionUtils.getFullStackTrace(pe) );
   	  }
   	  
   	  purchaseTitles = "Professional Psychology, Journal of Clinical Psychology, dummy, dummy, dummy, dummy, dummy, dummy";
          purch = new MockPurchase(1285513L, 8L, purchDate);
   	  purchase = new MockOrderPurchase(purch, purchaseTitles);
   	  //purchase.setConfirmNumber("1285513");

   	  purchases.add(purchase);   	  
      //////////////////////////////////////
         
   	  try {
   	    orderDate = format.parse("07/04/2006");
   	    purchDate = new java.sql.Date(orderDate.getTime());
   	  } catch (ParseException pe) {
     		_logger.error( ExceptionUtils.getFullStackTrace(pe) );
   	  }


          purchaseTitles = "Harvard Business Review";
          purch = new MockPurchase(1285514L, 1L, purchDate);
   	  purchase = new MockOrderPurchase(purch, purchaseTitles);
          mockPurchase.setClosed(false);

   	  purchases.add(purchase);
   	  
      //////////////////////////////////////
          try {
   	    orderDate = format.parse("06/12/2006");
   	    purchDate = new java.sql.Date(orderDate.getTime());
   	  } catch (ParseException pe) {
     		_logger.error( ExceptionUtils.getFullStackTrace(pe) );
   	  }
   	  
          purchaseTitles = "Newsweek, Business Week";
          purch = new MockPurchase(1285517L, 2L, purchDate);
   	  mockPurchase = new MockOrderPurchase(purch, purchaseTitles);
   	  mockPurchase.setClosed(true);

   	  purchases.add(mockPurchase);
      
      //////////////////////////////////////
          try {
   	    orderDate = format.parse("9/1/06");
   	    purchDate = new java.sql.Date(orderDate.getTime());
   	  } catch (ParseException pe) {
     		_logger.error( ExceptionUtils.getFullStackTrace(pe) );
   	  }
   	  
          purchaseTitles = "Wall Street Journal, New England Journal of Medicine";
          purch = new MockPurchase(1285521L, 2L, purchDate, "2342354");
          
   	  purchase = new MockOrderPurchase(purch, purchaseTitles);
   	  
   	  // Academic attributes
   	  purchase.setSchool("Cushing Acedemy");
   	  
   	  try {
   	     Date termStart = format.parse("9/1/05");	
   	     purchase.setStartOfTerm(termStart);
   	  } catch (ParseException pe) {
     		_logger.error( ExceptionUtils.getFullStackTrace(pe) );
   	  }

   	  purchase.setCourseNumber("Chem105");
   	  purchase.setCourseName("Introductory Chemistry");
   	  purchase.setNumberOfStudents(30);
   	  purchase.setInstructor("Asic Asimov");
   	  purchase.setYourReference("My Order Reference");
   	  purchase.setAccountingReference("");
   	  purchase.setOrderEnteredBy("John Phillip Souza");

          // TODO: Remove this cast when OrderPurchase has closed implemented
          mockPurchase = (MockOrderPurchase) purchase;
          mockPurchase.setClosed(false);
       
   	  purchases.add(purchase);
   	  ///////////////////////////////////////////
   	  try {
               orderDate = format.parse("9/1/06");
               purchDate = new java.sql.Date(orderDate.getTime());
          } catch (ParseException pe) {
         		_logger.error( ExceptionUtils.getFullStackTrace(pe) );
          }
   	       
          purchaseTitles = "Wall Street Journal, New England Journal of Medicine";
          purch = new MockPurchase(1285529L, 2L, purchDate, "847394");
   	       
          purchase = new MockOrderPurchase(purch, purchaseTitles);
   	       
          // Academic attributes
          purchase.setSchool("Cushing Acedemy");
   	       
          try {
             Date termStart = format.parse("9/1/05");   
             purchase.setStartOfTerm(termStart);
          } catch (ParseException pe) {
         		_logger.error( ExceptionUtils.getFullStackTrace(pe) );
          }

          purchase.setCourseNumber("Chem124");
          purchase.setCourseName("Chemistry");
          purchase.setNumberOfStudents(30);
          purchase.setInstructor("Noam Chomsky");
          purchase.setYourReference("A Bogus Reference");
          purchase.setAccountingReference("");
          purchase.setOrderEnteredBy("Sade");

          mockPurchase = (MockOrderPurchase) purchase;
          mockPurchase.setClosed(false);
   	    
          purchases.add(purchase);
          ///////////////////////////////////////////
            
   	  try {
   	     orderDate = format.parse("2/2/06");
   	     purchDate = new java.sql.Date(orderDate.getTime());
   	  } catch (ParseException pe) {
     		_logger.error( ExceptionUtils.getFullStackTrace(pe) );
   	  }
   	  
   	  purchaseTitles = "Modern Rocketry, Whaling in New England";
          purch = new MockPurchase(1285525L, 2L, purchDate, "44443");
   	  purchase = new MockOrderPurchase(purch, purchaseTitles);
   	  
   	  // Academic attributes
   	  purchase.setSchool("Mt Holyoak University");
   	     	  
   	  try {
             Date termStart = format.parse("8/1/06");	
   	     purchase.setStartOfTerm(termStart);
   	  } catch (ParseException pe) {
     		_logger.error( ExceptionUtils.getFullStackTrace(pe) );
   	  }
   	  
   	  purchase.setCourseNumber("Psych100");
   	  purchase.setCourseName("Introductory Psychology");
   	  purchase.setNumberOfStudents(28);
   	  purchase.setInstructor("Carl Jung");
   	  purchase.setYourReference("Psych Order Reference");
   	  purchase.setAccountingReference("Psych Account Reference");
   	  purchase.setOrderEnteredBy("Francis Bacon");
   	  
   	  purchases.add(purchase);
   	  ///////////////////////////////////////////

          // For now set the display list and the total list the same 
          all.setOrderPurchaseList(purchases);
   	  all.setDisplayPurchaseList(purchases);

   	  return all;
   }
   
   public OrderLicenses getAllLicenses()
   { 
      return buildLicenseList(ID);
   }
   
   public OrderLicenses getLicensesById(String id)
   { 
      OrderLicenses licenses = this.buildAllLicenses();

      // TODO: use getDisplayLicenseList() ???
      List<OrderLicense> dispLicenses = licenses.getOrderLicenseList();
      List<OrderLicense> results = new ArrayList<OrderLicense>();
      long testId = Long.valueOf(id).longValue();

      Iterator<OrderLicense> itr = dispLicenses.iterator();
      while (itr.hasNext()) 
      {
         MockOrderLicense license = (MockOrderLicense) itr.next();
         long licId = license.getOrderId();
         id = id.trim();

         if (testId == licId) { results.add((OrderLicense)license); } 
      }

      //licenses.setDisplayLicenseList(results);
      licenses.setOrderLicenseList(results);
      return licenses;
   }

   public OrderLicenses getLicensesByConfNum(String num)
    {
       long confNum = Long.parseLong(num);

       return this.getLicensesByConfNum(confNum);
   }

   public OrderLicenses getLicensesByConfNum(long num)
   {
       OrderLicenses licenses = this.buildAllLicenses();
       
       // List dispLicenses = licenses.getDisplayLicenseList();
       // TODO: use getDisplayLicenseList() ???
       List<OrderLicense> dispLicenses = licenses.getOrderLicenseList();
       List<OrderLicense> results = new ArrayList<OrderLicense>();
       
       Iterator<OrderLicense> itr = dispLicenses.iterator();
       while (itr.hasNext()) 
       {
          MockOrderLicense license = (MockOrderLicense) itr.next();
          long confNum = license.getPurchaseId();
          if (confNum == num) {
             results.add((OrderLicense)license);
          } 
       }

       licenses.setOrderLicenseList(results);
       
       return licenses;
   }

   private OrderLicenses buildLicenseList(int option)
   {
       return this.buildAllLicenses();
   }

   private OrderLicenses buildAllLicenses()
   {
   	  OrderLicenses all = new OrderLicenses();
   	  DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);
   	  List<OrderLicense> licenses = new ArrayList<OrderLicense>();

   	  MockOrderLicense license = new MockOrderLicense();

   	  license.setPublicationTitle("Wall Street Journal");
          license.setOrderId(243564L);
          license.setPurchaseId(1285511L);
          license.setStandardNumber("78109655");
          license.setPublicationYear("2001");
          license.setPublisher("Wiley Coyote & Sons");
          license.setRightsholder("Elvis Costello");
          license.setAuthor("John Naismith");
          license.setEditor("Herb Tarlick");
          license.setVolume("2");
          license.setEdition("9th");
          license.setChapterArticle("221B Baker St.");
          license.setNumberOfStudents(20);
          license.setCustomerReference("Walmart Journal");
          license.setRightsQualifyingStatement("Only staff-produced materials may be used. Images may not be used.");

   	  try {
   	     Date orderDate = format.parse("06/01/06");
             license.setCreateDate(orderDate);
   	  } catch (ParseException pe) {
     		_logger.error( ExceptionUtils.getFullStackTrace(pe) );
   	  }
          
          license.setPageRange("3 through 5");

   	  // Academic attributes
          license.setPermissionStatus("Granted");
          license.setPermissionType("Photocopy for coursepacks, classroom handouts, class notes, etc., etc., ad nauseum");
          license.setBillingStatus("Billed");
          license.setInvoiceId("1234");
   	  license.setPrice("$7.30");
          license.setPhotocopy(true);
   	  
   	  licenses.add((OrderLicense)license);
   	  ////////////////////////////////////////
   	  
   	  license = new MockOrderLicense();
   	  
          license.setPublicationTitle("Journal of Ambulatory Care");
          license.setOrderId(243565L);
          license.setPurchaseId(1285511L);
          license.setStandardNumber("78109650");
          license.setPublicationYear("2002");
          license.setPublisher("Rheinhold Weige");
          license.setRightsholder("Paul McCartney");
          license.setAuthor("John Wooden");
          license.setEditor("Yoko Ono");
          license.setEdition("1st");
          license.setChapterArticle("Mobile Triage");
          license.setNumberOfStudents(15);
          license.setCustomerReference("Walmart Journal of Competitive Distribution");
          license.setPageRange("121 - 155");
          license.setPoNumber("120106");

   	  try {
   	    Date orderDate = format.parse("06/12/06");
            license.setCreateDate(orderDate);
   	  } catch (ParseException pe) {
     		_logger.error( ExceptionUtils.getFullStackTrace(pe) );
   	  }
   	  
   	  // Academic attributes

          license.setPermissionStatus("Special Order");
          license.setSpecialOrder(true);
          license.setPermissionType("Posting e-reserves, course management systems, e-coursepacks");
          license.setBillingStatus("Not billed");
          license.setPrice("TBD");
          license.setPhotocopy(true);
   	  
   	  licenses.add((OrderLicense)license);
   	  /////////////////////////////////////////
   	   
   	   license = new MockOrderLicense();
   	   
   	   license.setPublicationTitle("Journal of Entomology");
   	   license.setOrderId(243565L);
   	   license.setPurchaseId(1285511L);
   	   license.setStandardNumber("78325");
   	   license.setPublicationYear("2004");
   	   license.setPublisher("Rheinhold Weige");
   	   license.setRightsholder("Paul McCartney");
   	   license.setAuthor("James Valvono");
   	   license.setEditor("Jim Smith");
   	   //license.setEdition("1st");
   	   license.setChapterArticle("Flying Insects of South America");
   	   license.setNumberOfStudents(6);
   	   license.setCustomerReference("Science Digest Reference");
   	   license.setPageRange("50-71");
   	   license.setPoNumber("40139");

   	   try {
   	     Date orderDate = format.parse("04/21/06");
   	     license.setCreateDate(orderDate);
   	   } catch (ParseException pe) {
      		_logger.error( ExceptionUtils.getFullStackTrace(pe) );
   	   }
   	   
   	   // Academic attributes

   	   license.setPermissionStatus("Special Order");
   	   license.setSpecialOrder(true);
   	   license.setPermissionType("Posting for e-reserves, course management systems, e-coursepacks");
   	   license.setBillingStatus("Not billed");
   	   license.setPrice("TBD");
   	   license.setPhotocopy(true);
   	   
   	   licenses.add((OrderLicense)license);
   	   /////////////////////////////////////////
           
   	   license = new MockOrderLicense();
   	    
   	   license.setPublicationTitle("NEJM");
   	   license.setOrderId(243565L);
   	   license.setPurchaseId(1285511L);
   	   license.setStandardNumber("4334325");
   	   license.setPublicationYear("2003");
   	   license.setPublisher("Harkins Press");
   	   license.setRightsholder("George Harrison");
   	   license.setAuthor("James Cagney");
   	   license.setEditor("Jim Smith");
   	   //license.setEdition("1st");
   	   license.setChapterArticle("Viral Transmission Vectors");
   	   license.setNumberOfStudents(6);
   	   license.setCustomerReference("The Lancet Ref");
   	   license.setPageRange("2-14");
   	   license.setPoNumber("673419");

   	   try {
   	      Date orderDate = format.parse("04/24/06");
   	      license.setCreateDate(orderDate);
   	   } catch (ParseException pe) {
      		_logger.error( ExceptionUtils.getFullStackTrace(pe) );
   	   }
   	    
   	   // Academic attributes

   	   license.setPermissionStatus("Special Order");
   	   license.setSpecialOrder(true);
   	   license.setPermissionType("Posting e-reserves, course management systems, e-coursepacks");
   	   license.setBillingStatus("Not billed");
   	   license.setPrice("TBD");
   	   license.setPhotocopy(true);
   	    
   	   licenses.add((OrderLicense)license);
   	   /////////////////////////////////////////
             
   	   license = new MockOrderLicense();
   	     
   	   license.setPublicationTitle("Wondomus Journal of Culinary Arts");
   	   license.setOrderId(243565L);
   	   license.setPurchaseId(1285511L);
   	   license.setStandardNumber("2215450");
   	   license.setPublicationYear("2005");
   	   license.setPublisher("Platte Press");
   	   license.setRightsholder("Kirk Panenthene");
   	   license.setAuthor("Jimmy Stewart");
   	   license.setEditor("Jim Smith");
   	   //license.setEdition("1st");
   	   license.setChapterArticle("Sage Spices");
   	   license.setNumberOfStudents(6);
   	   license.setCustomerReference("Chef School Cooking");
   	   license.setPageRange("6");
   	   license.setPoNumber("4648129");

   	   try {
   	      Date orderDate = format.parse("04/26/06");
   	      license.setCreateDate(orderDate);
   	   } catch (ParseException pe) {
      		_logger.error( ExceptionUtils.getFullStackTrace(pe) );
   	   }
   	     
   	   // Academic attributes

   	   license.setPermissionStatus("Special Order");
   	   license.setSpecialOrder(true);
   	   license.setPermissionType("Posting e-reserves, course management systems, e-coursepacks");
   	   license.setBillingStatus("Not billed");
   	   license.setPrice("TBD");
   	   license.setPhotocopy(true);
   	     
   	   licenses.add((OrderLicense)license);
   	   /////////////////////////////////////////
             
   	   license = new MockOrderLicense();
   	   
   	   license.setPublicationTitle("Proceedings of Modern Rocketry");
   	   license.setOrderId(243565L);
   	   license.setPurchaseId(1285401L);
   	   license.setStandardNumber("2314353");
   	   license.setPublicationYear("2001");
   	   license.setPublisher("O'Reilley");
   	   license.setRightsholder("Robert Goddard");
   	   license.setAuthor("Werner Von Braun");
   	   license.setEditor("Bjork");
   	   license.setEdition("2nd");
   	   license.setChapterArticle("Staged Separation");
   	   license.setNumberOfStudents(10);
   	   license.setCustomerReference("Warhead Delivery Vehicle");
   	   license.setPageRange("44-48");
   	   license.setPoNumber("0674306");

   	   try {
   	     Date orderDate = format.parse("05/23/06");
   	     license.setCreateDate(orderDate);
   	   } catch (ParseException pe) {
      		_logger.error( ExceptionUtils.getFullStackTrace(pe) );
   	   }
   	   
   	   // Academic attributes

   	   license.setPermissionStatus("Special Order");
   	   license.setSpecialOrder(true);
   	   license.setPermissionType("Posting e-reserves, course management systems, e-coursepacks");
   	   license.setBillingStatus("Not billed");
   	   license.setPrice("TBD");
   	   license.setPhotocopy(true);
   	   
   	   licenses.add((OrderLicense)license);
   	   /////////////////////////////////////////
           
   	  license = new MockOrderLicense();
   	  
          license.setPublicationTitle("New York Times");
          license.setOrderId(243563L);
          license.setPurchaseId(1285521L);
          license.setStandardNumber("78109651");
          license.setPublicationYear("2002");
          license.setPublisher("Adison Wesley");
          license.setRightsholder("Micheal Jackson");
          license.setAuthor("Jim Valvano");
          license.setEditor("Beyonce Gomes");
          license.setChapterArticle("Chapter 3");
          license.setNumberOfStudents(12);
          license.setCustomerReference("Wallstreet Journal");
          license.setPageRange("4,5,8,10,12,20-25");
          license.setPoNumber("120106");

   	  try {
   	    Date orderDate = format.parse("05/07/06");
            license.setCreateDate(orderDate);
   	  } catch (ParseException pe) {
     		_logger.error( ExceptionUtils.getFullStackTrace(pe) );
   	  }
   	  
   	  // Academic attributes;
          license.setPermissionStatus("Granted");
          license.setPermissionType("Use in e-mail, intranet/extranet/internet postings...");
          license.setBillingStatus("Invoiced ");
          license.setInvoiceId("1235");
   	  license.setPrice("$7.45");
    
   	  licenses.add((OrderLicense)license);
   	  // Display and total list are the same for now 	  
   	  all.setOrderLicenseList(licenses);
   	  //all.setDisplayLicenseList(licenses);
        
          license = new MockOrderLicense();
          license.setPublicationTitle("New York Times");
          license.setOrderId(243563L);
          license.setPurchaseId(1285513L);
          license.setStandardNumber("78109653");
          license.setPublicationYear("2003");
          license.setPublisher("Van Nostran"); 
          license.setRightsholder("Baz Lerman");
          license.setAuthor("Digger Phelps");
          license.setEditor("Josie Whales");
          license.setVolume("4");
          license.setEdition("9th");
          license.setChapterArticle("House Cleaning");
          license.setNumberOfStudents(16);
          license.setCustomerReference("Gwondonna Land");
          license.setRightsQualifyingStatement("Copies may be distributed to only one company.");

          license.setPageRange("121, 122");
          license.setPoNumber("3354566");

          try {
              Date orderDate = format.parse("10/05/06");
              license.setCreateDate(orderDate);
          } catch (ParseException pe) {
         		_logger.error( ExceptionUtils.getFullStackTrace(pe) );
          }
 
          // Academic attributes
          license.setPermissionStatus("Denied");
          license.setPermissionType("Use in e-mail, intranet/extranet/internet postings...");
          license.setBillingStatus("Pending");
          license.setInvoiceId("43233");
          license.setPrice("TBD");
          licenses.add((OrderLicense)license);
          // Display and total list are the same for now          
          all.setOrderLicenseList(licenses);
          //all.setDisplayLicenseList(licenses);
 
////////////////////////////////////
          license = new MockOrderLicense();
         
          license.setPublicationTitle("New York Times");
          license.setOrderId(243563L);
          license.setPurchaseId(1285512L);
          license.setStandardNumber("78109648");
          license.setPublicationYear("2004");
          license.setPublisher("Riley");
          license.setRightsholder("Noam Chomsky");
          license.setAuthor("Gerry Tarkanian");
          license.setEditor("Herb Tarlick");
          license.setChapterArticle("Left Wing Conspiracy");
          license.setNumberOfStudents(22);
          license.setCustomerReference("NEJ or Medicine");
          license.setPoNumber("120106");
         
          try {
            Date orderDate = format.parse("8/15/06");
            license.setCreateDate(orderDate);
          } catch (ParseException pe) {
         		_logger.error( ExceptionUtils.getFullStackTrace(pe) );
          }
          license.setPageRange("221");
          // Academic attributes
          license.setPermissionStatus("Granted");
          license.setPermissionType("Use in e-mail, intranet/extranet/internet postings...");
          license.setBillingStatus("Pending");
          license.setInvoiceId("43233");
          //license.setCustomerReference("Petaflops");
          license.setPrice("$14.45");
          
          licenses.add((OrderLicense)license);
          // Display and total list are the same for now          
          all.setOrderLicenseList(licenses);
          
////////////////////////////////////

          license = new MockOrderLicense();
          license.setPublicationTitle("New York Times");
          license.setOrderId(243563L);
          license.setPurchaseId(1285514L);
          license.setStandardNumber("78109644");
          license.setPublicationYear("1961");
          license.setPublisher("Tascom Press");
          license.setAuthor("Noam Chomsky");
          license.setRightsholder("Phillipe Fourier");
          license.setEdition("3rd");
          license.setChapterArticle("National Voting Patterns");
          license.setNumberOfStudents(14);
          license.setCustomerReference("The Lancet");
          license.setPoNumber("120106");

          try {
            Date orderDate = format.parse("7/04/06");
            license.setCreateDate(orderDate);
          } catch (ParseException pe) {
         		_logger.error( ExceptionUtils.getFullStackTrace(pe) );
          }

          // Academic attributes
          license.setPermissionStatus("Contact rightsholder directly");
          license.setPermissionType("Use in e-mail, intranet/extranet/internet postings...");
          license.setBillingStatus("Pending");
          license.setInvoiceId("43233");
          license.setPrice("TBD");
         
          licenses.add((OrderLicense)license);
          // Display and total list are the same for now          
          all.setOrderLicenseList(licenses);
          all.setDisplayLicenseList(new ArrayList<OrderLicense>(licenses));
 
          ////////////////////////////////////
   	  return all;
 }
 
    /*
    * Duplicate of Keith's OrderPurchaseServices main method for getting purchases
    */
    public OrderPurchases getOrderPurchases(int page) throws OrderPurchasesException 
    {
        // Replace with get from User Context
        OrderPurchases orderPurchases = UserContextService.getOrderPurchases(); 
        DisplaySpec displaySpec = UserContextService.getPurchaseDisplaySpec(); 
    
        try {
            switch (displaySpec.getSortBy()) {
                // Publication Title
                case 8: 
                   String title = displaySpec.getSearch();
                   orderPurchases = this.getPurchasesByPubTitle(title, page);
                   //purchaseList = orderPurchases.getDisplayPurchaseList();
                break;
                // Order Date
                case 0: 
                   //purchaseList = Arrays.asList(orderService.getAllPurchasesUser(OrderMgmtServiceProxy.CONFIRM_NUM, OrderMgmtServiceProxy.DESCENDING));
                    Date from = displaySpec.getSearchFromDate();
                    Date to = displaySpec.getSearchToDate();
                    orderPurchases = this.getPurchasesByOrderDate(from, to, page);
                break;   
                // Order Detail ID
                case 9:
                     // Permission Type Sort
                     //displaySpec.getPermissionType();
                     //orderPurchases = orderService.getPurchasesByOrderDate(from, to);
                     break;
                case 10:
                // Permission Status
                case 11:
                // Billing status sort
                case 12:
                // Your Reference
                case 7:
                // Invoice Number
                case 13:
                // Republication Title
                case 14:
                // Republication dATE
                case 15:
                // Republication Publisher
                case 16:
    
            }
            // orderPurchases.setOrderPurchaseList(Arrays.asList(orderService.getAllPurchasesUser(OrderMgmtServiceProxy.CONFIRM_NUM, OrderMgmtServiceProxy.DESCENDING)));
            DisplaySpecServices.setMinDisplayRow(displaySpec);
        } catch (OrderMgmtException omex) {
             OrderPurchasesException orderPurchasesException = new OrderPurchasesException();
             throw orderPurchasesException;
        }

        return orderPurchases;
    }
}
