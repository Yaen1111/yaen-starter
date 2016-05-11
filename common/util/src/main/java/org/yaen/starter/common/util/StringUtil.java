package org.yaen.starter.common.util;

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

		return s1.trim().compareToIgnoreCase(s2.trim()) == 0;
	}

	/**
	 * s1 is the same as s2, case sensitive and care space
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static boolean sameAs(String s1, String s2) {
		if (s1 == null) {
			return s2 == null;
		} else {
			return s1.compareTo(s2) == 0;
		}
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
