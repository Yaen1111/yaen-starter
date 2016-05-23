package org.yaen.starter.common.util.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * common string util, using apache.StringUtils
 * 
 * @author Yaen 2016年2月1日下午8:55:32
 */
public class StringUtil extends StringUtils {

	/**
	 * s1 like s2, null = empty = "  "
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static boolean like(String s1, String s2) {
		if (StringUtil.isBlank(s1) && StringUtil.isBlank(s2)) {
			return true;
		}

		return StringUtil.equalsIgnoreCase(s1.trim(), s2.trim());
	}

	/**
	 * any type to string
	 * 
	 * @param o
	 * @return
	 */
	public static String toString(Object o) {
		return o == null ? "" : o.toString();
	}

	/**
	 * to lower, null = ""
	 * 
	 * @param s
	 * @return
	 */
	public static String toLower(String s) {
		if (s == null) {
			return "";
		} else {
			return s.toLowerCase();
		}
	}

	/**
	 * to upper, null = ""
	 * 
	 * @param s
	 * @return
	 */
	public static String toUpper(String s) {
		if (s == null) {
			return "";
		} else {
			return s.toUpperCase();
		}
	}

}
