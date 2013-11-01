package com.copyright.ccc.web.actions;

import com.copyright.data.order.OrderMgmtSearchSortConstants;

public class SaveOrderDateReportAction extends SaveDateReportAction
{
    public SaveOrderDateReportAction() { 
       super.setSearchBy(OrderMgmtSearchSortConstants.LICENSE_SEARCH_TYPE_PURCHASE_DATE);
    }
}
