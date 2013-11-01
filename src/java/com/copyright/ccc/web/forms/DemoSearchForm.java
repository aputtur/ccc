package com.copyright.ccc.web.forms;

import com.copyright.ccc.web.CCValidatorForm;


public class DemoSearchForm extends CCValidatorForm
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String _searchCriteria;

    public void setSearchCriteria(String searchCriteria) {
        this._searchCriteria = searchCriteria;
    }

    public String getSearchCriteria() {
        return _searchCriteria;
    }
}
