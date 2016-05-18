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
 * A: code type, E = error, W = warning, M = message, T = type enum
 * B: system type, 0 = default/starter/common, 9 = user define
 * CC: code group, can be letter
 * DD: code item, can be letter
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
	T0GEMA("MALE"), T0GEFE("FEMALE"), T0GE00("UNKNOWN"),

	;

	/** title */
	@Getter
	private final String title;

	/** title */
	@Getter
	private final String description;

	/**
	 * constructor
	 * 
	 * @param title
	 * @param description
	 */
	private SystemCode(String title, String description) {
		this.title = title;
		this.description = description;
	}

	/**
	 * constructor
	 * 
	 * @param title
	 */
	private SystemCode(String title) {
		this.title = title;
		this.description = "";
	}

	/**
	 * parse code object
	 * 
	 * @param code
	 * @return
	 */
	public static SystemCode parseSystemCode(String code) {
		try {
			return SystemCode.valueOf(code);
		} catch (IllegalArgumentException ex) {
			return SystemCode.E00000;
		}
	}

}
