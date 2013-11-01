package com.copyright.ccc.web.transaction;

public class TransactionConstants
{
    //type of use codes
    public static final int TYPE_OF_USE_PHOTOCOPY = 0;
    public static final int TYPE_OF_USE_APS = 1;
    public static final int TYPE_OF_USE_ECCS = 2;
    public static final int TYPE_OF_USE_EMAIL = 3;
    public static final int TYPE_OF_USE_EXTRANET = 4;
    public static final int TYPE_OF_USE_INTRANET = 5;
    public static final int TYPE_OF_USE_INTERNET = 6;
    public static final int TYPE_OF_USE_BROCHURE = 7;
    public static final int TYPE_OF_USE_CDROM = 8;
    public static final int TYPE_OF_USE_DISSERTATION = 9;
    public static final int TYPE_OF_USE_DVD = 10;
    public static final int TYPE_OF_USE_JOURNAL = 11;
    public static final int TYPE_OF_USE_MAGAZINE = 12;
    public static final int TYPE_OF_USE_NEWSLETTER = 13;
    public static final int TYPE_OF_USE_NEWSPAPER = 14;
    public static final int TYPE_OF_USE_OTHERBOOK = 15;
    public static final int TYPE_OF_USE_PAMPHLET = 16;
    public static final int TYPE_OF_USE_PRESENTATION = 17;
    public static final int TYPE_OF_USE_TEXTBOOK = 18;
    public static final int TYPE_OF_USE_TRADEBOOK = 19;
    public static final int TYPE_OF_USE_DEFAULT = -1;
    
    //tpu Inst translation
    public static final int TPU_INST_PHOTOCOPY = 1;
    public static final int TPU_INST_APS = 1;
    public static final int TPU_INST_ECCS = 8;
    public static final int TPU_INST_EMAIL = 133;
    public static final int TPU_INST_EXTRANET = 134;
    public static final int TPU_INST_INTRANET = 204;
    public static final int TPU_INST_INTERNET = 203;
    public static final int TPU_INST_BROCHURE = 187;
    public static final int TPU_INST_CDROM = 189;
    public static final int TPU_INST_DISSERTATION = 186;
    public static final int TPU_INST_DVD = 190;
    public static final int TPU_INST_JOURNAL = 177;
    public static final int TPU_INST_MAGAZINE = 178;
    public static final int TPU_INST_NEWSLETTER = 185;
    public static final int TPU_INST_NEWSPAPER = 184;
    public static final int TPU_INST_OTHERBOOK = 209;
    public static final int TPU_INST_PAMPHLET = 188;
    public static final int TPU_INST_PRESENTATION = 214;
    public static final int TPU_INST_TEXTBOOK = 172;
    public static final int TPU_INST_TRADEBOOK = 173;
    public static final int TPU_INST_DEFAULT = -1;
    
    //pricing error actions
    public static final int PRICING_ERROR_ACTION_NONE = 0;
    public static final int PRICING_ERROR_ACTION_SPECIAL_ORDER = 1;
    public static final int PRICING_ERROR_ACTION_REGULAR_ORDER = 2;
  
    //offending attributes
    public static final String ATTRIBUTE_NOT_PRESENT = "ATTRIBUTE_NOT_PRESENT";
    public static final String ATTRIBUTE_NUMBER_OF_COPIES = "numCopies";
    public static final String ATTRIBUTE_NUMBER_OF_PAGES = "numPages";
    public static final String ATTRIBUTE_NUMBER_OF_RECIPIENTS = "numRecip";
    public static final String ATTRIBUTE_DURATION = "duration";
    public static final String ATTRIBUTE_NUMBER_OF_STUDENTS = "numStudents";
    public static final String ATTRIBUTE_CIRCULATION = "circulation";
    
    public static final String CONTENT_FULL_ARTICLE_CHAPTER = "Full article/chapter";
    public static final String CONTENT_EXCERPT = "Excerpt";
    public static final String CONTENT_QUOTATION = "Quotation";
    public static final String CONTENT_SELECTED_PAGES = "Selected pages";
    public static final String CONTENT_CHART = "Chart";
    public static final String CONTENT_GRAPH = "Graph";
    public static final String CONTENT_FIGURE_DIAGRAM_TABLE = "Figure, diagram, or table";
    public static final String CONTENT_PHOTOGRAPH = "Photograph";
    public static final String CONTENT_ILLUSTRATION = "Illustration";
    public static final String CONTENT_CARTOONS = "Cartoons";
    public static final String CONTENT_LOGOS = "Logos";
    
    //offending attribute reasons
    public static final String REASON_NUMBER_OF_COPIES = "number of copies";
    public static final String REASON_NUMBER_OF_PAGES = "number of pages";
    public static final String REASON_NUMBER_OF_RECIPIENTS = "number of recipients";
    public static final String REASON_DURATION = "duration";
    public static final String REASON_NUMBER_OF_STUDENTS = "number of students";
    public static final String REASON_CIRCULATION = "circulation";
    public static final String REASON_UNKNOWN = "quantities";
    
    //validation item types
    public static final String VALIDATION_ITEM_TYPE_ACADEMIC = "academic";
    public static final String VALIDATION_ITEM_TYPE_PHOTOCOPY = "photocopy";
    public static final String VALIDATION_ITEM_TYPE_EMAIL = "email";
    public static final String VALIDATION_ITEM_TYPE_NET = "net";
    public static final String VALIDATION_ITEM_TYPE_REPUBLICATION = "republication";

    // business status
    public static final String FOR_PROFIT = "For profit";
    public static final String NOT_FOR_PROFIT = "Not for profit";
    
    public static final String CC_ORDER_SOURCE_CODE = "WWW";
    public static final String PERMISSIONS_DIRECT_ORDER_SOURCE_CODE = "PD";
}
