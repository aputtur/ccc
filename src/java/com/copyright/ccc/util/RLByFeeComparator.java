package com.copyright.ccc.util;

import java.io.Serializable;
import java.util.Comparator;

import com.copyright.ccc.business.data.RLOrder;

public class RLByFeeComparator implements Comparator<RLOrder>,Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public int compare(RLOrder o1, RLOrder o2) {
		String fee1 = strip(o1.getTotalFee());
		String fee2 = strip(o2.getTotalFee());

		return Float.compare(
			Float.valueOf(strip(fee1)), Float.valueOf(strip(fee2))
		);
	}

	private String strip(String feeString) {
		if (feeString == null || feeString.equals("")) {
			return "0.00";
		}

		char[] feeChars = feeString.toCharArray();
		StringBuffer stripped = new StringBuffer();

		for (int i = 0; i < feeChars.length; i++) {
			if ((feeChars[i] >= '0' && feeChars[i] <= '9') || feeChars[i] == '.') {
				stripped.append(feeChars[i]);
			}
		}
		return stripped.toString();
	}
}