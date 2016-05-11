/**
 * 
 */
package org.yaen.starter.common.data.enums;

import lombok.Getter;

/**
 * system code definition
 * 
 * <pre>
 * format: ABCCDD
 * A: code type: E = error, W = warning, M = message, T = type enum
 * B: business: 0 = framework/common, 9 = user define
 * CC: code group
 * DD: code item
 * </pre>
 * 
 * @author Yaen 2016年1月13日上午11:52:48
 */
public enum SystemCode {

	/**
	 * system error
	 */
	E00000("code not exists"),

	E00001("code not exists"),

	E00002("code not exists"),

	E00003("code not exists"),

	E00004("code not exists"),

	/**
	 * system warning
	 */

	/**
	 * system message
	 */

	/**
	 * system type
	 */
	/** gender */
	T00001("MALE"), T00002("FEMALE"), T00099("UNKNOWN"),

	;

	/** title */
	@Getter
	private final String title;

	/**
	 * constructor
	 * 
	 * @param title
	 */
	private SystemCode(String title) {
		this.title = title;
	}

	/**
	 * get code object
	 * 
	 * @param code
	 * @return
	 */
	public static SystemCode getSystemCode(String code) {
		try {
			return SystemCode.valueOf(code);
		} catch (IllegalArgumentException ex) {
			return SystemCode.E00000;
		}
	}

}
