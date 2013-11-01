package com.copyright.ccc.util;

import java.io.Serializable;
import java.util.Comparator;

import com.copyright.svc.searchRetrieval.api.data.SrsLimiter;

public class LimiterComparator implements Comparator<SrsLimiter>,Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String valueThatRanksFirst = "";
	
	//	Get item in Limiter returns a String.
//	Compares its two arguments for order. 
//	Returns a negative integer, zero, or a positive integer 
//	as the first argument is less than, equal to, or greater than the second.


	public int compare(SrsLimiter l, SrsLimiter r) {
		if (l.getItem().equalsIgnoreCase(valueThatRanksFirst)) {
			return -1;
		} else if (r.getItem().equalsIgnoreCase(valueThatRanksFirst)) {
			return 1;
		}
		return l.getItem().compareToIgnoreCase(r.getItem());
	}

	public String getValueThatRanksFirst() {
		return valueThatRanksFirst;
	}

	public void setValueThatRanksFirst(String valueThatRanksFirst) {
		this.valueThatRanksFirst = valueThatRanksFirst;
	}
}