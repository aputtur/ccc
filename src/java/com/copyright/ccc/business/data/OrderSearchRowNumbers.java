package com.copyright.ccc.business.data;

import java.io.Serializable;

public class OrderSearchRowNumbers implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int lastSearchFromRow=0;
	private int lastSearchToRow=0;	
	private int tfStart=0;
	private int tfEnd=0;
	private int lastTfStart=0;
	private int lastTfEnd=0;	
	private int tfRowsFound=0;
	private int coiStart=0;
	private int coiEnd=0;
	private int lastCoiStart=0;
	private int lastCoiEnd=0;	
	private int coiRowsFound=0;
	private boolean isAscendingMerge = true;
	
	public int getTfStart() {
		return tfStart;
	}
	public void setTfStart(int tfStart) {
		this.tfStart = tfStart;
	}
	public int getTfEnd() {
		return tfEnd;
	}
	public void setTfEnd(int tfEnd) {
		this.tfEnd = tfEnd;
	}
	public int getTfRowsFound() {
		return tfRowsFound;
	}
	public void setTfRowsFound(int tfRowsFound) {
		this.tfRowsFound = tfRowsFound;
	}
	public int getCoiStart() {
		return coiStart;
	}
	public void setCoiStart(int coiStart) {
		this.coiStart = coiStart;
	}
	public int getCoiEnd() {
		return coiEnd;
	}
	public void setCoiEnd(int coiEnd) {
		this.coiEnd = coiEnd;
	}
	public int getCoiRowsFound() {
		return coiRowsFound;
	}
	public void setCoiRowsFound(int coiRowsFound) {
		this.coiRowsFound = coiRowsFound;
	}
	public int getLastSearchFromRow() {
		return lastSearchFromRow;
	}
	public void setLastSearchFromRow(int lastSearchFromRow) {
		this.lastSearchFromRow = lastSearchFromRow;
	}
	public int getLastSearchToRow() {
		return lastSearchToRow;
	}
	public void setLastSearchToRow(int lastSearchToRow) {
		this.lastSearchToRow = lastSearchToRow;
	}
	public int getLastTfStart() {
		return lastTfStart;
	}
	public void setLastTfStart(int lastTfStart) {
		this.lastTfStart = lastTfStart;
	}
	public int getLastTfEnd() {
		return lastTfEnd;
	}
	public void setLastTfEnd(int lastTfEnd) {
		this.lastTfEnd = lastTfEnd;
	}
	public int getLastCoiStart() {
		return lastCoiStart;
	}
	public void setLastCoiStart(int lastCoiStart) {
		this.lastCoiStart = lastCoiStart;
	}
	public int getLastCoiEnd() {
		return lastCoiEnd;
	}
	public void setLastCoiEnd(int lastCoiEnd) {
		this.lastCoiEnd = lastCoiEnd;
	}
	public boolean isAscendingMerge() {
		return isAscendingMerge;
	}
	public void setAscendingMerge(boolean isAscendingMerge) {
		this.isAscendingMerge = isAscendingMerge;
	}
}
