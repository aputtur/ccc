package com.copyright.ccc.web.actions;

import com.copyright.data.order.OrderMgmtSearchSortConstants;

public class SaveInvoiceDateReportAction extends SaveDateReportAction
{
    
    public SaveInvoiceDateReportAction() { 
        super.setSearchBy(OrderMgmtSearchSortConstants.LICENSE_SEARCH_TYPE_INVOICE_DATE);
    }
}
