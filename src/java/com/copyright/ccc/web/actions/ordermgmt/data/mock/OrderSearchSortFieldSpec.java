package com.copyright.ccc.web.actions.ordermgmt.data.mock;

import com.copyright.ccc.web.actions.ordermgmt.data.base.SortDirectionEnum;
import com.copyright.ccc.web.actions.ordermgmt.data.base.SortFieldSpec;

public class OrderSearchSortFieldSpec extends SortFieldSpec<OrderSearchSortableFieldEnum, SortDirectionEnum> {
    
	private static final long serialVersionUID = 1L;

	public OrderSearchSortFieldSpec(OrderSearchSortableFieldEnum field , SortDirectionEnum direction) {
		super(field, direction);
	}
}
