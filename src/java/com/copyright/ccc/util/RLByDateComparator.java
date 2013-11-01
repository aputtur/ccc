package com.copyright.ccc.util;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

import com.copyright.ccc.business.data.RLOrder;

public class RLByDateComparator implements Comparator<RLOrder>,Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public int compare(RLOrder o1, RLOrder o2) {
		Date d1 = o1.getRawCreateDate();
		Date d2 = o2.getRawCreateDate();

		//	NOTE:  This can conceivably break.
		//
		return d1.compareTo(d2);
	}
}