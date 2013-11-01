package com.copyright.ccc.util;

import org.apache.commons.lang.exception.ExceptionUtils;

public class LogUtil {

	/**
	 * Returns a stack trace that can be appended to another
	 * string while keeping the stack trace on it's own line.
	 * @param t throwable
	 * @return
	 */
	public static String appendableStack(Throwable t) {
		if (t==null) return "";
		
		return "\n" + ExceptionUtils.getFullStackTrace(t);
	}
	public static String getStack(Throwable t) {
		if (t==null) return "";
		return ExceptionUtils.getFullStackTrace(t);
	}
}
