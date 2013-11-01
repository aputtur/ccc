package com.copyright.ccc.business.services.adjustment;

/**
 * Set of adjustment-related contants.
 */
public class OrderAdjustmentConstants
{
  static final String ADJUSTMENT_TYPE_INVOICE = "I";
  static final String ADJUSTMENT_TYPE_PURCHASE = "P";
  static final String ADJUSTMENT_TYPE_DETAIL = "D";
  
  static final String ADJUSTMENT_TYPE_INVOICE_VERBOSE = "Invoice";
  static final String ADJUSTMENT_TYPE_PURCHASE_VERBOSE = "Purchase";
  static final String ADJUSTMENT_TYPE_DETAIL_VERBOSE = "Detail";
  
  public static final int UNSPECIFIED_DURATION = -1;
  
  public static final String ADJUSTMENT_STATUS_NEW = "NEW";
  
  public static final String NUMBER_OF_PAGES = "Number Of Pages";
  public static final String NUMBER_OF_COPIES = "Number Of Copies";
  public static final String NUMBER_OF_RECIPIENTS = "Number Of Recipients";
  public static final String DURATION = "Duration";
  public static final String NUMBER_OF_STUDENTS = "Number of Students";
  //public static final String CONTENT_FULL_ARTICLE_CHAPTER = "FULL_ARTICLE_CHAPTER";
  public static final String NUMBER_OF_EXCERPTS = "Number Of Excerpts";
  public static final String NUMBER_OF_QUOTATIONS = "Number Of Quotations";
  public static final String NUMBER_OF_CHARTS = "Number Of Charts";
  public static final String NUMBER_OF_GRAPHS = "Number Of Graphs";
  public static final String NUMBER_OF_FIGURE_DIAGRAM_TABLES = "Number Of Figures";
  public static final String NUMBER_OF_PHOTOGRAPHS = "Number Of Photographs";
  public static final String NUMBER_OF_ILLUSTRATIONS = "Number Of Illustrations";
  public static final String NUMBER_OF_CARTOONS = "Number Of Cartoons";
  public static final String NUMBER_OF_LOGOS = "Number Of Logos";
  
  public static final String LICENSEE_FEE = "Licensee Fee";
  public static final String DISCOUNT = "Discount";
  public static final String ROYALTY = "Royalty";
  
  public static final String REASON_REVISED_NUM_COPIES = "100 - Revised # of copies/users/circulation/distribution";
  public static final String REASON_REVISED_NUM_PAGES = "110 - Revised # of pages";
  public static final String REASON_DUPLICATE_ORDER = "120 - Duplicate Order";
  public static final String REASON_MATERIAL_NOT_USED = "130 - Material Not Used";
  public static final String REASON_PARTIAL_INVOICE_PAYMENT_ONGOING = "140 - Partial invoice payment/Collection efforts ongoing (Finance use only)";
  public static final String REASON_NON_PAYMENT_INVOICE_ONGOING = "150 - Non payment of invoice/Collection efforts ongoing (Finance use only)";
  public static final String REASON_LICENSEE_FEE_ADJUSTMENT = "160 - Licensee Fee Adjustment";
  public static final String REASON_REVISED_REPORTING_ERROR = "170 - Reporting Error";
  public static final String REASON_PARTIAL_INVOICE_PAYMENT_DISCONTINUED = "180 - Partial Invoice Payment/Collection Efforts Discontinued";
  public static final String REASON_NON_PAYMENT_INVOICE_DISCONTINUED = "190 - Non Payment of Invoice/Collection Efforts Discontinued";
  public static final String REASON_ROYALTY_FEE_ADJUSTMENT = "200 - Royalty Fee Adjustment";

  public static final long REASON_REVISED_NUM_COPIES_CD = 100;
  public static final long REASON_REVISED_NUM_PAGES_CD = 110;
  public static final long REASON_DUPLICATE_ORDER_CD = 120;
  public static final long REASON_MATERIAL_NOT_USED_CD = 130;
  public static final long REASON_PARTIAL_INVOICE_PAYMENT_ONGOING_CD = 140;
  public static final long REASON_NON_PAYMENT_INVOICE_ONGOING_CD = 150;
  public static final long REASON_LICENSEE_FEE_ADJUSTMENT_CD = 160;
  public static final long REASON_REVISED_REPORTING_ERROR_CD = 170;
  public static final long REASON_PARTIAL_INVOICE_PAYMENT_DISCONTINUED_CD = 180;
  public static final long REASON_NON_PAYMENT_INVOICE_DISCONTINUED_CD = 190;
  public static final long REASON_ROYALTY_FEE_ADJUSTMENT_CD = 200;

  static final int DEFAULT_DECIMAL_PLACES_TO_ROUND = 2;
}
