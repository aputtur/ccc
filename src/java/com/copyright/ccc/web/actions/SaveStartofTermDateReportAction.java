package com.copyright.ccc.web.actions;

import com.copyright.data.order.OrderMgmtSearchSortConstants;

public class SaveStartofTermDateReportAction extends SaveDateReportAction
{
    public SaveStartofTermDateReportAction() { 
       super.setSearchBy(OrderMgmtSearchSortConstants.LICENSE_SEARCH_TYPE_START_OF_TERM);
    }
}
