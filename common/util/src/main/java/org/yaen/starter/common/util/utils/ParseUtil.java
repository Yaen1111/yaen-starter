package org.yaen.starter.common.util.utils;

/**
 * parse util, used to parse string to int types
 * 
 * @author Yaen 2016年2月1日下午8:55:32
 */
public class ParseUtil {

	/**
	 * parse string to Integer
	 * 
	 * @param s
	 * @return
	 */
	public static Integer parseInt(String s) {
		return Integer.parseInt(s);
	}

	/**
	 * try parse string to Integer, return v for any error
	 * 
	 * @param s
	 * @param v
	 * @return
	 */
	public static Integer tryParseInt(String s, Integer v) {
		try {
			return parseInt(s);
		} catch (Exception ex) {
			return v;
		}
	}

	/**
	 * parse string to Long
	 * 
	 * @param s
	 * @return
	 */
	public static Long parseLong(String s) {
		return Long.parseLong(s);
	}

	/**
	 * try parse string to Long, return 0 for any error
	 * 
	 * @param s
	 * @param v
	 * @return
	 */
	public static Long tryParseLong(String s, Long v) {
		try {
			return parseLong(s);
		} catch (Exception ex) {
			return v;
		}
	}
}
