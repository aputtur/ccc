package com.copyright.ccc.web.forms.admin;

import com.copyright.ccc.web.CCValidatorForm;

public class PaidReportForm extends CCValidatorForm
{
	private static final long serialVersionUID = 1L;
	
	private String _fromDate;
	private String _toDate;
	
	/**
	 * @return the _fromDate
	 */
	public String getFromDate()
	{
		return _fromDate;
	}
	/**
	 * @param date the _fromDate to set
	 */
	public void setFromDate(String date)
	{
		_fromDate = date;
	}
	/**
	 * @return the _toDate
	 */
	public String getToDate()
	{
		return _toDate;
	}
	/**
	 * @param date the _toDate to set
	 */
	public void setToDate(String date)
	{
		_toDate = date;
	}
}
