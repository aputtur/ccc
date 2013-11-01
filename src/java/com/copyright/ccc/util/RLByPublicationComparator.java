package com.copyright.ccc.util;

import java.io.Serializable;
import java.util.Comparator;

import com.copyright.ccc.business.data.RLOrder;

public class RLByPublicationComparator implements Comparator<RLOrder>,Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public int compare(RLOrder o1, RLOrder o2) {
		return o1.getPublication().compareToIgnoreCase(o2.getPublication());
	}
}