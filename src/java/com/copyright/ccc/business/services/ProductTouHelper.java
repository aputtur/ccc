package com.copyright.ccc.business.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import com.copyright.base.enums.TypeOfUseEnum;

public class ProductTouHelper {

	public static List<LabelValueBean> getTousForProduct(ProductEnum productEnum) {
		
		List<LabelValueBean> touCodes = new ArrayList<LabelValueBean>();

		if (productEnum.name().equalsIgnoreCase(ProductEnum.APS.name())){
			addTouCode(touCodes, TypeOfUseEnum.RIGHT_TO_PHOTOCOPY);
		}
		if (productEnum.name().equalsIgnoreCase(ProductEnum.ECC.name())){
			addTouCode(touCodes, TypeOfUseEnum.RIGHT_TO_SCAN);
		}
		if (productEnum.name().equalsIgnoreCase(ProductEnum.TRS.name())){
			addTouCode(touCodes, TypeOfUseEnum.RIGHT_TO_PHOTOCOPY);
		}
		if (productEnum.name().equalsIgnoreCase(ProductEnum.DPS.name())){
			addTouCode(touCodes, TypeOfUseEnum.EMAIL_DIGITAL_TRANSMISSION);
			addTouCode(touCodes, TypeOfUseEnum.INTERNET);
			addTouCode(touCodes, TypeOfUseEnum.EXTRANET);
			addTouCode(touCodes, TypeOfUseEnum.INTRANET);
		}
		if (productEnum.name().equalsIgnoreCase(ProductEnum.RLS.name())){
			addTouCode(touCodes, TypeOfUseEnum.ADVERTISMENT);
			addTouCode(touCodes, TypeOfUseEnum.BROCHURE);
			addTouCode(touCodes, TypeOfUseEnum.CD_ROM);
			addTouCode(touCodes, TypeOfUseEnum.DIGITAL_VIDEO_DISC);
			addTouCode(touCodes, TypeOfUseEnum.DISSERTATION);
			addTouCode(touCodes, TypeOfUseEnum.ELECTRONIC_MAIL);
			addTouCode(touCodes, TypeOfUseEnum.FRAMING);
			addTouCode(touCodes, TypeOfUseEnum.INTERNET);
			addTouCode(touCodes, TypeOfUseEnum.INTRANET);
			addTouCode(touCodes, TypeOfUseEnum.JOURNAL);
			addTouCode(touCodes, TypeOfUseEnum.LINKING);
			addTouCode(touCodes, TypeOfUseEnum.MAGAZINE);
			addTouCode(touCodes, TypeOfUseEnum.NEWSPAPER);
			addTouCode(touCodes, TypeOfUseEnum.NEWSLETTER_E_NEWSLETTER);
			addTouCode(touCodes, TypeOfUseEnum.PAMPHLET);
			addTouCode(touCodes, TypeOfUseEnum.PRESENTATION_MATERIALS_HANDOUT);
			addTouCode(touCodes, TypeOfUseEnum.TEXTBOOK);
			addTouCode(touCodes, TypeOfUseEnum.TRADE_BOOK);
			addTouCode(touCodes, TypeOfUseEnum.OTHER_BOOK);
		
		}

		return touCodes;
	}

	public static void addTouCode(List<LabelValueBean> touCodes, TypeOfUseEnum typeOfUseEnum) {
		LabelValueBean labelValueBean = new LabelValueBean();
		labelValueBean.setLabel(typeOfUseEnum.getDesc());
		labelValueBean.setValue(Integer.valueOf(typeOfUseEnum.getId()).toString());
		touCodes.add(labelValueBean);
	}

	
}
