package com.copyright.ccc.business.services.cart;

import com.copyright.workbench.i18n.Money;

/**
 * Set of e-commerce-related constants.
 */
public final class ECommerceConstants
{
  public static final int CONTENT_TYPE_UNITARY_QUANTITY = 1;
  public static final int CONTENT_TYPE_ZERO_QUANTITY = 0;
  
  public static final int ORDER_DATA_SOURCE_TF = 1;	
  public static final String ORDER_DATA_SOURCE_TF_DISPLAY = "TF";	
  public static final int ORDER_DATA_SOURCE_COI = 2;	
  public static final String ORDER_DATA_SOURCE_COI_DISPLAY = "OMS";	

  public static final String RIGHT_DATA_SOURCE_TF = "TF";
  
  public static final long INVALID_NUMBER_OF_PAGES = -1;
  public static final int INVALID_NUMBER_OF_STUDENTS = -1;
  public static final long INVALID_NUMBER_OF_COPIES = -1;
  public static final long INVALID_NUMBER_OF_RECIPIENTS = -1;
  public static final int INVALID_DURATION = -1;
  public static final int INVALID_CIRCULATION_DISTRIBUTION = -1;
  
  public static final long INVALID_RLS_QUANTITY = -1;
    
  public static final long INVALID_WORK_INST = 0;
  
  public static final String FULL_ARTICLE_YES = "Y";
  public static final String FULL_ARTICLE_NO = "N";

  static final long INVALID_CART_ID = 0;
  
  static final int NO_ITEMS_IN_CART_QUANTITY = 0;
  static final Money NO_ITEMS_IN_CART_AMOUNT = new Money(0);
  
  static final boolean IS_SPECIAL_ORDER_FROM_SCRATCH = true;
  static final boolean IS_NOT_SPECIAL_ORDER_FROM_SCRATCH = false;
  
  public static final long EMAIL_TPU_CODE = 133;
  public static final long PHOTOCOPY_TPU_CODE = 1;
  public static final long INTRANET_TPU_CODE = 204;
  public static final long EXTRANET_TPU_CODE = 134;
  public static final long INTERNET_TPU_CODE = 203;
    
  public static final long REPUBLICATION_INTERNET_TPU_CODE = 203;
  public static final long REPUBLICATION_INTRANET_TPU_CODE = 204;
  public static final long REPUBLICATION_LINKING_TPU_CODE = 205;
  public static final long REPUBLICATION_FRAMING_TPU_CODE = 206;
  public static final long REPUBLICATION_EMAIL_TPU_CODE = 207;
  public static final long REPUBLICATION_CDROM_TPU_CODE = 189;
  public static final long REPUBLICATION_DVD_TPU_CODE = 190;
  public static final long REPUBLICATION_JOURNAL_TPU_CODE = 177;
  public static final long REPUBLICATION_MAGAZINE_TPU_CODE = 178;
  public static final long REPUBLICATION_NEWSPAPER_TPU_CODE = 184;
  public static final long REPUBLICATION_NEWSLETTER_TPU_CODE = 185;
  public static final long REPUBLICATION_DISSERTATION_TPU_CODE = 186;
  public static final long REPUBLICATION_BROCHURE_TPU_CODE = 187;
  public static final long REPUBLICATION_PAMPHLET_TPU_CODE = 188;
  public static final long REPUBLICATION_ADVERTISEMENT_TPU_CODE = 211;
  public static final long REPUBLICATION_PRESENTATION_TPU_CODE = 214;
  public static final long REPUBLICATION_TEXTBOOK_TPU_CODE = 172;
  public static final long REPUBLICATION_TRADEBOOK_TPU_CODE = 173;
  public static final long REPUBLICATION_OTHERBOOK_TPU_CODE = 209;
  
  public static final long DPS_PRODUCT_CODE = 36;
  public static final long TRS_PRODUCT_CODE = 1;
  public static final long APS_PRODUCT_CODE = 2;
  public static final long ECCS_PRODUCT_CODE = 12;
  public static final long RLS_PRODUCT_CODE = 44 ;

  public static final String DPS_PRODUCT_CD = "DPS";
  public static final String TRS_PRODUCT_CD = "TRS";
  public static final String APS_PRODUCT_CD = "APS";
  public static final String ECCS_PRODUCT_CD = "ECCS";
  public static final String RLS_PRODUCT_CD = "RLS" ;

  public static final String DPS_PRODUCT_NAME = "Digital Permissions Service";
  public static final String TRS_PRODUCT_NAME = "Transactional Reporting Service";
  public static final String APS_PRODUCT_NAME = "Academic Permissions Service";
  public static final String ECCS_PRODUCT_NAME = "Electronic Course Content Service";
  public static final String RLS_PRODUCT_NAME = "Republication License Service" ;
  
  public static final String PRICE_NOT_AVAILABLE = "N/A";
  public static final String PRICE_NOT_CALCULATED = "$ 0.00";
  public static final String CONTACT_RIGHTSHOLDER_DIRECTLY = "CRD";
  
  public static final long SPECIAL_ORDER_RIGHT_INST = -1;
  
  public static final long APS_TPU_CODE = 1L;
  public static final long ECCS_TPU_CODE = 8L;
  
  

  private ECommerceConstants()
  {
  }
}
