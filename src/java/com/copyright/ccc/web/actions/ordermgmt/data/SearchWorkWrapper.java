package com.copyright.ccc.web.actions.ordermgmt.data;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.copyright.ccc.web.actions.ordermgmt.util.PublicationDateUtils;
import com.copyright.svc.worksremote.api.search.SearchResult;

public class SearchWorkWrapper implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public SearchWorkWrapper() {
		super();
	}
	
	public SearchWorkWrapper( SearchResult work ) {
		super();
		this.work = work;
	}

	private SearchResult work;
	
	public SearchResult getWork() {
		return work;
	}

	public void setWork(SearchResult work) {
		this.work = work;
	}

	public String getTitle()
	{
		if ( getWork() != null ) {
			return StringUtils.trimToEmpty( getWork().getMainSubTitle() );
		} else {
			return "";
		}
	}
	
	public String getPublicationDates()
	{
		if ( getWork() != null ) {
			return PublicationDateUtils.convertToDisplayFormat(getWork().getRunPubStartDate(), getWork().getRunPubEndDate());
		} else {
			return "";
		}
	}
	
	public String getIdnoTypeDisplay() {
		if ( getWork() != null ) {
			if ( StringUtils.isNotEmpty(work.getMainIdnoType())) {
				return work.getMainIdnoType().replaceAll("Valid ", "").replaceAll("-13", "");
			}
		} 
		return "IDNO";
	}
	
	public String getDnar() {
		if ( getWork() != null ) {
			if ( getWork().getDoNotUseInd() ) {
				return "_@ERROR@_DNAR_";
			}
		} else {
			return "_@ERROR@_NOT_FOUND";
		}
		return "";
	}
	
	public String getMainContributor() {
		//the wrwrkinst search/DB search, was not returning contributor
		//the code below should fix it.
		SearchResult w = getWork();
		if (w != null)
		{
			if (!StringUtils.isEmpty(w.getMainContributor()))
				return w.getMainContributor();
			else if (!StringUtils.isEmpty(w.getAuthorName()))
				return w.getAuthorName();
			else 
				return w.getEditorName();
		}
		
		return null;
	}

}
