package com.copyright.ccc.util;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

import com.copyright.svc.artransaction.api.data.ARTransaction;

public class ARTransactionByDateComparator implements Comparator<ARTransaction>, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public int compare(ARTransaction t1, ARTransaction t2) {
		Date d1 = t1.getDueDate();
		Date d2 = t2.getDueDate();

		return d1.compareTo(d2);
	}
}