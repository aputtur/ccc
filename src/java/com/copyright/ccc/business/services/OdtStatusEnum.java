package com.copyright.ccc.business.services;

public enum OdtStatusEnum {

	N0("0", "Order Detail Not Saved", "NOT_COMPLETELY_ENTERED", null, null, "Not Completely Entered", null),
	N100("100", "Alert Research - Conflicting ID/Title Information", "AWAITING_RESEARCH", "ERROR", null, "Requires Research", null),
	N310("310","Requires Additional Customer Information","AWAITING_LCN_REPLY","FIRST_REQUEST", null, "Requires Additional Licensee Information", "Awaiting Licensee Reply"),
	N320("320","Awaiting Customer Reply","AWAITING_LCN_REPLY","SECOND_REQUEST", null, "Requires Additional Licensee Information", "Awaiting Licensee Reply"),
	N330("330","Pending Gateway Confirmation","unmapped", null, null, null, null),
	N410("410","Forward To Rightsholder","AWAITING_RH","FIRST_REQUEST",null,"Requires Rightsholder Response", "Awaiting Rightsholder Reply/1st Request"),
	N420("420","Awaiting Rightsholder Reply/2nd Request","AWAITING_RH","SECOND_REQUEST", null , "Requires Rightsholder Response", "Awaiting Rightsholder Reply/2nd Request" ),
	N430("430","Awaiting Rightsholder Reply/3rd Request (not used anymore)","AWAITING_RH","FINAL_REQUEST", null , "Requires Rightsholder Response", "Awaiting Rightsholder Reply/Final Request"),
	N440("440","Awaiting Rightsholder Reply/Final Request","AWAITING_RH","FINAL_REQUEST", null, "Requires Rightsholder Response", "Awaiting Rightsholder Reply/Final Request"),
	N510("510","Survey data not pinned","unmapped",null, null, null, null),
	N600("600","Work Not Pinned","AWAITING_RESEARCH","ERROR",null, "Requires Research", null),
	N610("610","Work Not Found","AWAITING_RESEARCH","ERROR","MANUAL_SPECIAL_ORDER", "Requires Research", null),
	N620("620","Multiple Works Found","AWAITING_RESEARCH", "ERROR", null, "Requires Research", null),
	N630("630","May Contain Excluded Work","AWAITING_RESEARCH", "ERROR", null, "Requires Research", null),
	N640("640","Incorrect Publication Date","AWAITING_RESEARCH","ERROR",null, "Requires Research", null),
	N700("700","Right Not Pinned","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N710("710","Right Not Found","AWAITING_RESEARCH","ERROR","RIGHT_NOT_FOUND_SPECIAL_ORDER", "Requires Research", null),
	N720("720","Multiple Rights Found","AWAITING_RESEARCH","ERROR","MULTIPLE_RIGHTS_FOUND_SPECIAL_ORDER", "Requires Research", null),
	N730("730","Further Research Needed","AWAITING_RESEARCH","ERROR","RESEARCH_FURTHER_SPECIAL_ORDER", "Requires Research",null),
	N740("740","Hold/Pending","NOT_COMPLETELY_ENTERED_HALT","ERROR", null, "Requires Research", null),
	N800("800","Fees Not Calculated","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N805("805","Fees Not Calculated - Exceeds System Limit","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N810("810","Fee Formula Not Found","AWAITING_RESEARCH","ERROR",null, "Requires Research", null),
	N820("820","Missing Fee Information","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N830("830","Reported Fee Exceeds Tolerance Percent","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N840("840","Reported Fee Exceeds Tolerance Amount","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N850("850","Fee Parameter Exceeds Limit","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N860("860","RLS Parent Formula Not Found","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N865("865","RLS Licensee Formula Not Found","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N870("870","Country Not Found","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N880("880","Unable to Convert Parent Parameter to Child Value","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N890("890","Parent Formulas Value was null","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N900("900","Can't Find Parent Formulas Value for Substitution","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N901("901","Missing Circulation Type","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N905("905","Missing Volume","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N910("910","No Volume Fee Range Found","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N915("915","Print Multiplier Not Found","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N920("920","Electronic Placement Multiplier Not Found","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N925("925","Electronic Size Multiplier Not Found","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N930("930","WWW Base Fee Not Found","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N935("935","WWW Page Setting Not Found","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N940("940","WWW Duration Multiplier Not Found","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N945("945","WWW Class Multiplier Not Found","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N950("950","No License Fee Minimum Threshold Found ","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N955("955","No Fee Range Calculation Type Found","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N960("960","No Publication Type Found (Required for Editorial)","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N970("970","Missing Total Pages-Can't Validate Page Pct Limit ","AWAITING_RESEARCH","ERROR","BIBLIO_PAGES_MISSING_SPECIAL_ORDER", "Requires Research", null ),
//	N1010A("1010","Awaiting Invoicing","AWAITING_LCN_CONFIRM","FIRST_REQUEST", null, null, null),
//	N1010B("1010","Awaiting Invoicing","INVOICE_READY","DENIED_BY_LCN_NO_RESPONSE", null, null, null),
//	N1010C("1010","Awaiting Invoicing","INVOICE_READY","DENIED_BY_RH_NO_RESPONSE", null, null, null),
	N1010("1010","Awaiting Invoicing","INVOICE_READY","INVOICE_READY_INVOICE", null, "Awaiting Invoicing", null),
//	N1010E("1010","Awaiting Invoicing","CANCELLED","CANCELLED_BY_LCN", null, null, null),
	N1011("1011","AAS Detail Completely Processed","unmapped", null, null, null,null),
//	N1020A("1020","Awaiting Invoicing","INVOICE_READY","INVOICE_READY_CREDIT_CARD", null, null, null),
	N1020("1020","Invoicing in Progress","INVOICING_IN_PROGRESS", null, null, "Invoicing in Progress",null),
	N1100("1100","Licensee Account Number Not Found","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N1110("1110","Licensee A/R Account Number Not Found","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N1250("1250","Total Amount of Invoice Equals 0","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N1300("1300","No Invoice Line Items Found","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N1425("1425","Fee Formula GL Account not found in Carillon","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N1900("1900","Withdrawn by CCC","CANCELLED","CANCELLED_BY_LCN", null, "Canceled", null),
	N2000A("2000","Successfully Invoiced","INVOICED","INVOICED", null, "Invoiced", null),
//	N2000B("2000","Successfully Invoiced","INVOICED","PAID_CREDIT_CARD", null, null, null),
//	N2000C("2000","Successfully Invoiced","DISTRIBUTED", null, null, null,null),
	N2020("2020","Zero $ Invoice","unmapped", null, null, null,null),
	N2300("2300","Migrated TRS Transaction ","unmapped", null, null, null,null),
	N2310("2310","Migrated APS Transaction","unmapped", null, null, null,null),
	N2320("2320","Legacy Rightsholder Discrepancy","unmapped", null, null, null,null),
	N9000("9000","Unknown Grant","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N9100("9100","Order Detail Not Found","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N9105("9105","Order Not Found","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N9110("9110","More than 1 WRK_INST found","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N9200("9200","Licensee Does Not Have A Licensee Class","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N9300("9300","More Than One Order Detail Exists For This ODT_IN","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N9310("9310","Fee Error - Right Not Specified","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N9320("9320","Fee Error - Order Capture Date Not Specified","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N9330("9330","Fee Error - Right Does Not Exist","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N9340("9340","Fee Error - Multiple Rights Found","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N9350("9350","Fee Error - TPU/TPU Set Not Specified","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N9360("9360","Fee Error - Fee Calculation Is Invalid SQL","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N9370("9370","Product/Use/Formula Row Not Found","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N9380("9380","Right TPU is within more than one Type Use Set ","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N9400("9400","Error Opening Cursor","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N9410("9410","Error Opening Dynamic Cursor","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N9420("9420","Error While Running Dynamic Cursor","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N9500("9500","Max Rows Exceeded - All Rights For A Work","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N9600("9600","Greater Than Limit","AWAITING_RESEARCH","ERROR","LIMITS_EXCEEDED_SPECIAL_ORDER", "Requires Research", null),
	N9610("9610","Less Than Limit","AWAITING_RESEARCH", "ERROR","LIMITS_EXCEEDED_SPECIAL_ORDER", "Requires Research", null),
	N9620("9620","Not Equal To Required Amount","AWAITING_RESEARCH","ERROR","LIMITS_EXCEEDED_SPECIAL_ORDER", "Requires Research", null),
	N9630("9630","Fee Parm Limit Record Found But No Limit Specified","AWAITING_RESEARCH","ERROR","LIMITS_EXCEEDED_SPECIAL_ORDER", "Requires Research", null),
	N9645("9645","Invalid Total Pages-Can't Validate Page Pct Limit","AWAITING_RESEARCH","ERROR","LIMITS_EXCEEDED_SPECIAL_ORDER", "Requires Research", null),
	N9650("9650","Pages Requested Exceeds Percentage Limit","AWAITING_RESEARCH","ERROR","LIMITS_EXCEEDED_SPECIAL_ORDER", "Requires Research", null),
	N9655("9655","Multiple RHs found when checking exclusion table","AWAITING_RESEARCH","ERROR", null, "Requires Research", null),
	N(null,"N/A","CART", null, null, null, null);

	
	private String odtStatusCd;
	private String odtStatusMsg;
	private String itemStatusCd;
	private String itemStatusQualifier;
	private String specialOrderTypeCd;
	private String itemStatusDescription;
	private String itemCycleDescription;
		
	private OdtStatusEnum(String odtStatusCd, String odtStatusMsg, String itemStatusCd, 
						  String itemStatusQualifier, String specialOrderTypeCd,
						  String itemStatusDescription, String itemCycleDescription)
	{
		setOdtStatusCd(odtStatusCd);
		setOdtStatusMsg(odtStatusMsg);
		setItemStatusCd(itemStatusCd);
		setItemStatusQualifier(itemStatusQualifier);
		setSpecialOrderTypeCd(specialOrderTypeCd);
		setItemStatusDescription(itemStatusDescription);
		setItemCycleDescription(itemCycleDescription);
	}
	
	public static OdtStatusEnum getEnumForName( String name ) {
		for ( OdtStatusEnum os : OdtStatusEnum.values() ) {
			if ( name.equalsIgnoreCase(os.name())) {
				return os;
			} 
		}
		return null;
	}

	public static OdtStatusEnum getEnumForOdtStatusCd( String odtStatusCd ) {
		for ( OdtStatusEnum os : OdtStatusEnum.values() ) {
			if ( odtStatusCd.equalsIgnoreCase(os.getOdtStatusCd())) {
				return os;
			} 
		}
		return null;
	}
	
	public String getOdtStatusMsg() {
		return odtStatusMsg;
	}

	public void setOdtStatusMsg(String odtStatusMsg) {
		this.odtStatusMsg = odtStatusMsg;
	}

	public String getItemStatusCd() {
		return itemStatusCd;
	}

	public void setItemStatusCd(String itemStatusCd) {
		this.itemStatusCd = itemStatusCd;
	}

	public String getItemStatusQualifier() {
		return itemStatusQualifier;
	}

	public void setItemStatusQualifier(String itemStatusQualifier) {
		this.itemStatusQualifier = itemStatusQualifier;
	}

	public String getSpecialOrderTypeCd() {
		return specialOrderTypeCd;
	}

	public void setSpecialOrderTypeCd(String specialOrderTypeCd) {
		this.specialOrderTypeCd = specialOrderTypeCd;
	}

	public String getOdtStatusCd() {
		return odtStatusCd;
	}

	public void setOdtStatusCd(String odtStatusCd) {
		this.odtStatusCd = odtStatusCd;
	}

	public String getItemStatusDescription() {
		return itemStatusDescription;
	}

	public void setItemStatusDescription(String itemStatusDescription) {
		this.itemStatusDescription = itemStatusDescription;
	}

	public String getItemCycleDescription() {
		return itemCycleDescription;
	}

	public void setItemCycleDescription(String itemCycleDescription) {
		this.itemCycleDescription = itemCycleDescription;
	}
	

	
}
