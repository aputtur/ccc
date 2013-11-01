
package com.copyright.ccc.business.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import com.copyright.base.enums.OrderSourceEnum;
import com.copyright.base.enums.ProductEnum;

public class EnumHelper {
	
	private EnumHelper() {}
	
	public static List<LabelValueBean> getOrderSourcesForSearch() {
		
		List<LabelValueBean> osList = new ArrayList<LabelValueBean>();
		for ( OrderSourceEnum ods : OrderSourceEnum.values() ) {
			LabelValueBean labelValueBean = new LabelValueBean();
			labelValueBean.setLabel(ods.getDesc());
			labelValueBean.setValue(Integer.toString(ods.getId()));
			osList.add(labelValueBean);
		}
		return osList;
	}

	public static List<LabelValueBean> getProductsForAddDetail() {
		
		List<LabelValueBean> osList = new ArrayList<LabelValueBean>();
		for ( ProductEnum ods : ProductEnum.values() ) {
			LabelValueBean labelValueBean = new LabelValueBean();
			labelValueBean.setLabel(ods.getAbbreviation());
			labelValueBean.setValue(Integer.toString(ods.getId()));
			osList.add(labelValueBean);
		}
		return osList;
	}

}
