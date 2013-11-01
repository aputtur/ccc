package com.copyright.ccc.web.util;

import java.io.Serializable;
import java.util.Comparator;

import com.copyright.ccc.business.services.DisplaySpecServices;

/**
 * This class provides a mapping between the key values of the Order History
 * Search select labels that provides an ordering consistent with the wire frame
 * label order. This class is necessary for use in the TreeMap constructor
 * because the default label key values provide an inappropriate sort order for
 * the TreeMap.
 */
public class OHSearchComparator implements Comparator<Integer>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OHSearchComparator() {
	}

	public int compare(Integer one, Integer two) {
		int rtnValue = 0;
		try {
			int intOne = one.intValue();
			int intTwo = two.intValue();

			int oneMapped = this.mappedValue(intOne);
			int twoMapped = this.mappedValue(intTwo);

			if (oneMapped < twoMapped) {
				rtnValue = -1;
			} else if (oneMapped > twoMapped) {
				rtnValue = 1;
			}
		} catch (Exception exc) {
			rtnValue = 0;
		}

		return rtnValue;
	}

	/*
	 * Maps the keys of the Order History Search map so the order of the UI
	 * labels will meet the specified label order.
	 */
	private final int mappedValue(int value) {
		int mappedValue = 0;

		switch (value) {
		case DisplaySpecServices.CONFNUMFILTER: // 1
			mappedValue = 2;
			break;
		case DisplaySpecServices.ORDERDATEFILTER: // 2
			mappedValue = 1;
			break;
		case DisplaySpecServices.SCHOOLFILTER: // 3
			mappedValue = 3;
			break;
		case DisplaySpecServices.COURSENAMEFILTER: // 4
		case DisplaySpecServices.COURSENUMBERFILTER: // 5
		case DisplaySpecServices.INSTRUCTORFILTER: // 6
		case DisplaySpecServices.YOURREFERENCEFILTER: // 7
			//mappedValue = value + 1;
			mappedValue = 10;
			break;
		case DisplaySpecServices.STARTOFTERMFILTER: // 17
			mappedValue = 4;
			break;
		case DisplaySpecServices.YOURACCTREFERENCEFILTER: // 18
			//mappedValue = 9;
			mappedValue = 11;
			break;
			
		//Cases for new order history related changes.
		case DisplaySpecServices.JOB_TCKT_NUM: // 20
			mappedValue = 8;
			break;
		case DisplaySpecServices.LIC_NUM:
			mappedValue = 9;
			break;
			
		case DisplaySpecServices.CUST_REF_NUM:
			mappedValue = 12;
			break;
			
		}

		return mappedValue;
	}
}
