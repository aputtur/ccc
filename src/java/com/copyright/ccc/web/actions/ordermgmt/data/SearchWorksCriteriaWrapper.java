package com.copyright.ccc.web.actions.ordermgmt.data;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.copyright.ccc.web.actions.ordermgmt.util.SearchWorkNoCriteriaException;
import com.copyright.svc.worksremote.api.search.SearchParms;
import com.copyright.svc.worksremote.api.search.SearchWorksFieldsEnum;

public class SearchWorksCriteriaWrapper implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String idnoNumber;
	private String wrWorkId;
	private String tfWorkId;
	private String title;
	
	private CustomPageControl pageControl = new CustomPageControl(5, true);
		
	SearchParms workSearchParms;

	private void setWorkSearchParmsField(SearchWorksFieldsEnum field, String value)
	{
		String trimmed = StringUtils.trim(value);
		
		if(StringUtils.isNotEmpty(trimmed))
			workSearchParms.setParameter(field, trimmed);
		else
			workSearchParms.removeParameter(field);
	}
	
	public SearchParms getWorkSearchParms(List<ProcessMessage> processMessages) throws SearchWorkNoCriteriaException {
		workSearchParms = new SearchParms();
		workSearchParms.setSearchAllTitleTypes(true);
		workSearchParms.setRowsRequested(getPageControl().getPageSize());
		
		int gotCriteria = 0;
		if ( StringUtils.isNotEmpty( getIdnoNumber()) ) {
			setWorkSearchParmsField(SearchWorksFieldsEnum.SEARCH_IDNO_FIELD, getIdnoNumber() );
			if ( workSearchParms.getNameValuePairs().containsKey(SearchWorksFieldsEnum.SEARCH_IDNO_FIELD)) {
				gotCriteria++;
			}
		} else {
			workSearchParms.removeParameter(SearchWorksFieldsEnum.SEARCH_IDNO_FIELD);
		}
		if ( StringUtils.isNotEmpty( getWrWorkId() ) ) {
			if ( StringUtils.isNumeric( getWrWorkId().trim() ) ) {
				setWorkSearchParmsField(SearchWorksFieldsEnum.SEARCH_WR_WRK_INST, getWrWorkId() );
				if ( workSearchParms.getNameValuePairs().containsKey(SearchWorksFieldsEnum.SEARCH_WR_WRK_INST)) {
					gotCriteria++;
				}
			} else {
				processMessages.add(new ProcessMessage("WR Work Id is not numeric"));
			}
		} else {
			workSearchParms.removeParameter(SearchWorksFieldsEnum.SEARCH_WR_WRK_INST);
		}
		if ( StringUtils.isNotEmpty( getTfWorkId() ) ) {
			if ( StringUtils.isNumeric( getTfWorkId().trim() ) ) {
				setWorkSearchParmsField(SearchWorksFieldsEnum.SEARCH_TF_WRK_INST, getTfWorkId() );
				if ( workSearchParms.getNameValuePairs().containsKey(SearchWorksFieldsEnum.SEARCH_TF_WRK_INST)) {
					gotCriteria++;
				}
			} else {
				processMessages.add(new ProcessMessage("TF Work Id is not numeric"));
			}			
		} else {
			workSearchParms.removeParameter(SearchWorksFieldsEnum.SEARCH_TF_WRK_INST);
		}
		if ( StringUtils.isNotEmpty( getTitle() ) ) {
			setWorkSearchParmsField(SearchWorksFieldsEnum.SEARCH_MAINSUB_TITLE_FIELD, getTitle() );
			if ( workSearchParms.getNameValuePairs().containsKey(SearchWorksFieldsEnum.SEARCH_MAINSUB_TITLE_FIELD)) {
				gotCriteria++;
			}
		} else {
			workSearchParms.removeParameter(SearchWorksFieldsEnum.SEARCH_MAINSUB_TITLE_FIELD);
		}
		
		if ( gotCriteria == 0 ) {
			throw new SearchWorkNoCriteriaException("No criteria found");
		}
		
		setStartEndDisplayRows();
		
		return workSearchParms;
	}
	
	public void setStartEndDisplayRows() {
			workSearchParms.setStartingRow( getPageControl().getNextStartingRow() );
			if ( getPageControl().isPageSizeChanged() ) {
				workSearchParms.setRowsRequested( getPageControl().getNextRowsRequested() );
			} 
	}

	public String getIdnoNumber() {
		
		return idnoNumber;
	}
	public void setIdnoNumber(String idnoNumber) {
		this.idnoNumber = idnoNumber;
	}
	public String getWrWorkId() {
		return wrWorkId;
	}
	public void setWrWorkId(String wrWorkId) {
		this.wrWorkId = wrWorkId;
	}
	public String getTfWorkId() {
		return tfWorkId;
	}
	public void setTfWorkId(String tfWorkId) {
		this.tfWorkId = tfWorkId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public CustomPageControl getPageControl() {
		return pageControl;
	}

	public void setPageControl(CustomPageControl pageControl) {
		this.pageControl = pageControl;
	}
}
