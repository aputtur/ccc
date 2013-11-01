package com.copyright.ccc.web.util;

import java.io.Serializable;

import com.copyright.domain.data.WorkExternal;
import com.copyright.domain.data.WorkExternal.IdnoTypeCdEnum;

public class ArticleDisplay  implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WorkExternal work;
	public ArticleDisplay(WorkExternal work){
		this.work = work;
	}
	
	
	public String getWrWorkInst() {
		return (work.getPrimaryKey()==null)? "" : work.getPrimaryKey().toString();  
    }  
	
	public String getTfWorkInst() {
		return (work.getTfWrkInst() == null) ? "" : work.getTfWrkInst().toString();
	}
    
	public String getIdno() {
		return work.getIdno();
	}
	
	
	public String getIdnoTypeCode() {
		
		/* 
		 * avoid NPE when idnoTypeCd is null
		 */
		if (work.getIdnoTypeCode() == null) {
			return null;
		}
		if (work.getIdnoTypeCode() != null && work.getIdnoTypeCode().equals("WRWRKINST")) {
			return "CCC System Number";
		}
		if(work.getIdnoTypeCode().compareTo("NOIDNO")==0){// added to display ID number instead of NOIDNO
			return "ID Number";
		}
		return work.getIdnoTypeCode();
	}
	
	public String getFullTitle() {
		return work.getFullTitle();
	}

	public String getEdition() {
		return work.getEdition();
	}
	
	public String getPublisherName() {
		return work.getPublisherName();
	}
	
	public String getAuthorName() {
		return work.getAuthorName();
	}

	public String getEditorName() {
		return work.getEditorName();
	}
	
	public String getRunPubStartDate() {
		String date = (work.getRunPubStartDate() == null) ? "" : work.getRunPubStartDate().toString();
		return WebUtils.convertYYYYMMDDtoMediumDate(date);
		
		/*Calendar calendar = Calendar.getInstance();
		String strYear = date.substring(0, 4);
		String strMonth = date.substring(4,6);
		String strDay = date.substring(6);
		calendar.set(Integer.parseInt(strYear),Integer.parseInt(strMonth),Integer.parseInt(strDay));
		Date pubDate = calendar.getTime();
		DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
		String strRunPubDate = dateFormat.format(pubDate);*/
		//return strRunPubDate;
	}
	
	public String getRunPubEndDate(){
		String date = (work.getRunPubEndDate()) == null ? "" : work.getRunPubEndDate().toString();
		return WebUtils.convertYYYYMMDDtoMediumDate(date);
	}
	
	public String getPublicationType() {
		return work.getPublicationType();
	}
	
	public String getCatalogRank() {
		return (work.getIsFrequentlyRequested() == null) ? "" : work.getIsFrequentlyRequested().toString();
	}
	
	public String getTfWksInst(){
		return work.getTfWrkInst().toString();
	}
	
	public String getLanguage(){
		return work.getLanguage();
	}
	
	public String getOclcnum() {
		return work.getOclcNum();
	}
	
	public String getIdnoWop() {
		return work.getIdnoWop();
	}
	
	public String getPages() {
		return (work.getPages() == null) ? "" : work.getPages().toString();
	}
	
	public String getCountry() {
		return work.getCountry();
	}
	
	public String getSeries(){
		return work.getSeries();
	}
	
	public String getSeriesNumber(){
		return work.getSeriesNumber();
	}
	
	public String getItemStartPage(){
		return work.getItemStartPage();
	}
	
	public String getItemEndPage(){
		return work.getItemEndPage();
	}
		
	public String getItemNumber(){
		return work.getItemNumber();
	}
	
	public String getItemVolume(){
		return work.getItemVolume();
	}
	
	public String getItemIssue(){
		return work.getItemIssue();
	}
	
	public String getItemPageRange() {
		return work.getItemPageRange();
	}
	
	public String getItemSeason() {
		return work.getItemSeason();
	}
	
	public String getItemQuarter(){
		return work.getItemQuarter();
	}
	
	public String getItemWeek(){
		return work.getItemWeek();
	}
	
	public String getItemSection(){
		return work.getItemSection();
	}


	public WorkExternal getWork() {
		return work;
	}
	

}
