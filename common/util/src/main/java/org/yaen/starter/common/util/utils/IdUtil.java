package org.yaen.starter.common.util.utils;

/**
 * id util, generate none-unique id
 * 
 * @author Yaen 2016年2月1日下午8:55:32
 */
public class IdUtil {

	/**
	 * generate id with prefix, mostly use [P]yyyyMMddHHmmssSSS
	 * 
	 * @param prefix
	 * @return
	 */
	public static String generateId(String prefix) {
		String p = StringUtil.trimToEmpty(prefix);
		if (p.length() > 2)
			p = p.substring(0, 2);

		return p + DateUtil.formatDateSeq();
	}

	public static void main(String[] args) {
		System.out.println(generateId("A"));
	}

}
