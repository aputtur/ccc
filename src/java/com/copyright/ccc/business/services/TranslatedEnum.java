package com.copyright.ccc.business.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.util.LabelValueBean;

import com.copyright.ccc.web.transaction.RepublicationConstants;

public enum TranslatedEnum {
	
	TRANSLATED(RepublicationConstants.TRANSLATED,"Yes"),
	NOT_TRANSLATED(RepublicationConstants.NOT_TRANSLATED,"No"),
	NO_TRANSLATION(RepublicationConstants.NO_TRANSLATION,properCase(RepublicationConstants.NO_TRANSLATION)),
	TRANSLATED_LANGUAGE_CHINESE(RepublicationConstants.TRANSLATED_LANGUAGE_CHINESE,properCase(RepublicationConstants.TRANSLATED_LANGUAGE_CHINESE)),
	TRANSLATED_LANGUAGE_ENGLISH(RepublicationConstants.TRANSLATED_LANGUAGE_ENGLISH,properCase(RepublicationConstants.TRANSLATED_LANGUAGE_ENGLISH)),
	TRANSLATED_LANGUAGE_FRENCH(RepublicationConstants.TRANSLATED_LANGUAGE_FRENCH,properCase(RepublicationConstants.TRANSLATED_LANGUAGE_FRENCH)),
	TRANSLATED_LANGUAGE_GERMAN(RepublicationConstants.TRANSLATED_LANGUAGE_GERMAN,properCase(RepublicationConstants.TRANSLATED_LANGUAGE_GERMAN)),
	TRANSLATED_LANGUAGE_ITALIAN(RepublicationConstants.TRANSLATED_LANGUAGE_ITALIAN,properCase(RepublicationConstants.TRANSLATED_LANGUAGE_ITALIAN)),
	TRANSLATED_LANGUAGE_JAPANESE(RepublicationConstants.TRANSLATED_LANGUAGE_JAPANESE,properCase(RepublicationConstants.TRANSLATED_LANGUAGE_JAPANESE)),
	TRANSLATED_LANGUAGE_PORTUGUESE(RepublicationConstants.TRANSLATED_LANGUAGE_PORTUGUESE,properCase(RepublicationConstants.TRANSLATED_LANGUAGE_PORTUGUESE)),
	TRANSLATED_LANGUAGE_RUSSIAN(RepublicationConstants.TRANSLATED_LANGUAGE_RUSSIAN,properCase(RepublicationConstants.TRANSLATED_LANGUAGE_RUSSIAN)),
	TRANSLATED_LANGUAGE_SPANISH(RepublicationConstants.TRANSLATED_LANGUAGE_SPANISH,properCase(RepublicationConstants.TRANSLATED_LANGUAGE_SPANISH)),
	TRANSLATED_LANGUAGE_OTHER(RepublicationConstants.TRANSLATED_LANGUAGE_OTHER,properCase(RepublicationConstants.TRANSLATED_LANGUAGE_OTHER));

	private String dbCode;
	private String userText;
	
	private static final String TRANSLATED_LANGUAGE_KEY = "TRANSLATED_LANGUAGE";
	
	private TranslatedEnum(String dbCode, String userText)
	{
		this.dbCode = dbCode;
		this.userText = userText;		
	}
    private static String properCase(String value) {
    	if ( StringUtils.isNotEmpty( value ) ) {
    		String pValue = value.substring(0,1)+value.substring(1).toLowerCase();
    		return pValue;
    	}
    	return "";
    }
	public static TranslatedEnum getEnumForName( String name ) {
		for ( TranslatedEnum en : TranslatedEnum.values() ) {
			if ( name.equalsIgnoreCase(en.name())) {
				return en;
			} 
		}
		return null;
	}
		
	public static List<LabelValueBean> getTranslatedList() {
		
		List<LabelValueBean> translatedCodes = new ArrayList<LabelValueBean>();
		translatedCodes.add(new LabelValueBean("Yes",RepublicationConstants.TRANSLATED));
		translatedCodes.add(new LabelValueBean("No",RepublicationConstants.NOT_TRANSLATED));
		return translatedCodes;
	}
	public static List<LabelValueBean> getTranslatedLanguageList() {
		
		List<LabelValueBean> translatedLanguageCodes = new ArrayList<LabelValueBean>();
		translatedLanguageCodes.add(new LabelValueBean("No Translation",RepublicationConstants.NO_TRANSLATION));
		for ( TranslatedEnum rs : TranslatedEnum.values() ) {
			if ( rs.name().indexOf(TRANSLATED_LANGUAGE_KEY) != -1 ) {
				translatedLanguageCodes.add(new LabelValueBean(rs.getUserText(),rs.getDbCode()));
			}
		}
		return translatedLanguageCodes;
	}

	public String getDbCode() {
		return dbCode;
	}

	public String getUserText() {
		return userText;
	}
}
