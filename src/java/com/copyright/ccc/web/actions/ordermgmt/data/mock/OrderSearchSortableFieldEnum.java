package com.copyright.ccc.web.actions.ordermgmt.data.mock;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import com.copyright.ccc.web.actions.ordermgmt.data.base.SortableFieldEnum;


public enum OrderSearchSortableFieldEnum implements SortableFieldEnum {
	
	 ACCOUNTING_REF("COLUMN-SPEC")
	,COURSE_NAME("COLUMN-SPEC")
	,COURSE_NUMBER("COLUMN-SPEC")
	,LOGGED_IN_USER("COLUMN-SPEC")
	,ORDER_DETAIL_REF("COLUMN-SPEC")
	,PO_NUMBER("COLUMN-SPEC")
	,PROFESSOR("COLUMN-SPEC")
	,START_OF_TERM("COLUMN-SPEC")
	,TITLE("COLUMN-SPEC")
	,CONFIRM_NUMBER("COLUMN-SPEC")
	,ORDER_NUMBER("COLUMN-SPEC")
	,ORDER_DATE("COLUMN-SPEC")
	,DETAIL_NUMBER("COLUMN-SPEC")
	,DEFAULT("COMUMN_SPEC")
	;
	
	private String dbColumnFieldSortExpression;
	
	private OrderSearchSortableFieldEnum(String key) {
		dbColumnFieldSortExpression = key;
	}

	/**
	 * @return the FieldName
	 */
	public String getColumnName() {
		return dbColumnFieldSortExpression;
	}
	/**
	 * @return the FieldName
	 */
	public String getFieldName() {
		return dbColumnFieldSortExpression;
	}	
	
	private volatile static List<LabelValueBean> orderSortFields;
	static {
		getOrderSortFieldSelects();
	}
	public static List<LabelValueBean> getOrderSortFieldSelects() {
		if ( orderSortFields == null ) {
			List<LabelValueBean> orderSortFieldsLocal = new ArrayList<LabelValueBean>();
			String[] fields = {
					"Default"
					,"Accounting Ref"
					,"Course Name"
					,"Course Number"
					,"Logged in user"
					,"Order Detail Ref"
					,"PO Number"
					,"Professor"
					,"Start of Term"
					,"Title"
                    };
			for ( String fld : fields ) {
				orderSortFieldsLocal.add( new LabelValueBean(fld, fld.toUpperCase().replaceAll(" ","_")));
			}
			orderSortFields=orderSortFieldsLocal;
		}
		return orderSortFields;
	}
	
	public static OrderSearchSortableFieldEnum getOrderSearchSortableEnumFor( String userSelection ) {
		for ( OrderSearchSortableFieldEnum en : OrderSearchSortableFieldEnum.values() ) {
			if ( userSelection.equalsIgnoreCase(en.name())) {
				return en;
			}
		}
		return null;
	}
}
