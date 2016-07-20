package org.yaen.starter.common.util.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;

/**
 * common string util, using apache.StringUtils
 * 
 * @author Yaen 2016年2月1日下午8:55:32
 */
public class StringUtil extends StringUtils {

	/** char digits */
	public static final char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
			'F' };

	/** the camel upper case separator */
	public static final char CAMEL_SEPERATOR_CHAR = '_';

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

	/**
	 * convert byte array to HEX str, like 0,1,2,3 = 00010203, 200,255 = CDFF
	 * 
	 * @param byteArray
	 * @return
	 */
	public static String byteToStr(byte[] byteArray) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < byteArray.length; i++) {
			sb.append(byteToHexStr(byteArray[i]));
		}
		return sb.toString();
	}

	/**
	 * convert byte to HEX
	 * 
	 * @param mByte
	 * @return
	 */
	public static String byteToHexStr(byte mByte) {
		char[] tempArr = new char[2];
		tempArr[0] = Digit[(mByte >>> 4) & 0x0F];
		tempArr[1] = Digit[mByte & 0x0F];

		return new String(tempArr);
	}

	/**
	 * convert input stream to string
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static String inputStream2String(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int len = -1;
		while ((len = is.read(buf)) != -1) {
			baos.write(buf, 0, len);
		}
		String str = new String(baos.toByteArray());
		baos.close();
		return str;
	}

	/**
	 * to camel upper, getUserName = GET_USER_NAME
	 * 
	 * @param s
	 * @return
	 */
	public static String toCamelUpper(String s) {
		if (s == null)
			return "";

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (Character.isUpperCase(c)) {
				sb.append(CAMEL_SEPERATOR_CHAR);
				sb.append(c);
			} else {
				sb.append(Character.toUpperCase(c));
			}
		}
		return sb.toString();
	}

	/**
	 * to camel lower, GET_USER_NAME = getUserName
	 * 
	 * @param s
	 * @return
	 */
	public static String toCamelLower(String s) {
		if (s == null)
			return "";

		StringBuilder sb = new StringBuilder();
		boolean is_upper = false;

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == CAMEL_SEPERATOR_CHAR) {
				is_upper = true;
			} else {
				if (is_upper) {
					sb.append(Character.toUpperCase(c));
					is_upper = false;
				} else {
					sb.append(Character.toLowerCase(c));
				}
			}
		}
		return sb.toString();
	}

	/**
	 * read string
	 * 
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	public static String readString(Reader reader) throws IOException {
		// reader to string
		BufferedReader br = new BufferedReader(reader);
		StringBuilder sb = new StringBuilder();
		String line = "";
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		return sb.toString();
	}
}
