package com.copyright.ccc.web.actions.ordermgmt.data;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class ProcessMessage implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public ProcessMessage() {
		super();
	}
	
	public ProcessMessage(String detailId, String errorLevel, String errorMessage) {
		super();
		setDetailId(detailId);
		setErrorLevel(errorLevel);
		setErrorMessage(errorMessage);
	}
	
	public ProcessMessage(String errorMessage) {
		super();
		setErrorLevel("error");
		setErrorMessage(errorMessage);
	}
	
	public String getFormattedMessage() {
		StringBuffer buf = new StringBuffer();
		buf.append(errorLevel.toUpperCase()).append(": ");
		buf.append("Detail: ").append(detailId).append(" - ");
		buf.append(errorMessage);
		return buf.toString();
	}
	
	public String getBriefFormattedMessage() {
		StringBuffer buf = new StringBuffer();
		buf.append(errorLevel.toUpperCase()).append(": ");
		buf.append(errorMessage);
		return buf.toString();
	}
	
	public String getBriefMessage() {
		StringBuffer buf = new StringBuffer();
		buf.append(errorMessage);
		return buf.toString();
	}
		
	public static boolean hasErrors(List<ProcessMessage> processMessages) {
		if ( processMessages != null ) {
			for ( ProcessMessage pm : processMessages) {
				if ( pm.isError() ) {
					return true;
				}
			}
		}
		return false;
	}
	
	private String detailId;
	private String errorLevel;
	private String errorMessage;
	private boolean error;
	
	public String getDetailId() {
		return detailId;
	}
	public void setDetailId(String detailId) {
		this.detailId = detailId;
	}
	public String getErrorLevel() {
		return errorLevel;
	}
	public void setErrorLevel(String errorLevel) {
		this.errorLevel = errorLevel;
		if ( StringUtils.isNotEmpty(errorLevel) ) {
			if ( errorLevel.equalsIgnoreCase("error") ) {
				setError(true);
			}
		}
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}
	
	

}
