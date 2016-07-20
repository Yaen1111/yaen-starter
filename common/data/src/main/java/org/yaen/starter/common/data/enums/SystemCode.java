package org.yaen.starter.common.data.enums;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

/**
 * system code definition
 * 
 * <pre>
 * format: ABCCDD
 * A: code type, E8 = error, W4 = warning, M2 = message, T1 = type enum, "X" = special code
 * B: system type, 0 = default, 1 = data, 2 = common, 3 = core, 4 = biz, 5 = web, 9 = user define
 * CC: code group
 * DD: code item
 * </pre>
 * 
 * @author Yaen 2016年1月13日上午11:52:48
 */
public enum SystemCode {

	/** special 0 for empty */
	EMPTY(0, ""),

	/**
	 * Error
	 */

	/** error:general */
	E(800000, "ERROR"),

	/**
	 * data exception
	 */

	/** data error */
	E_DATA(810000, "DATA ERROR"),
	/** data not exists */
	E_DATA_NOT_EXISTS(810001, "DATA NOT EXISTS"),
	/** duplicate data */
	E_DUPLICATE_DATA(810002, "DUPLICATE DATA"),
	/** no data affected */
	E_NO_DATA_AFFECTED(810003, "NO DATA AFFECTED"),
	/** data operation cancelled */
	E_DATA_OPERATION_CANCELLED(810004, "DATA OPERATION CANCELLED"),

	/**
	 * common exception
	 */

	/** error:general:common */
	E_COMMON(820000, "COMMON ERROR"),

	/**
	 * Waring
	 */

	/** warning:general */
	W(400000, "WARNING"),

	/**
	 * Message
	 */

	/** message:general */
	M(200000, "MESSAGE"),

	/**
	 * Type enum
	 */

	/** gender:male */
	T_GENDER_MALE(100101, "MALE"),
	/** gender:femail */
	T_GENDER_FEMAIL(100102, "FEMALE"),
	/** gender:unknown */
	T_GENDER_UNKNOWN(100103, "UNKNOWN"),

	/**
	 * Special
	 */

	/** http 200 */
	X_HTTP_OK(2000, "HTTP 200"),

	;

	private static Map<Integer, SystemCode> map;

	/** code */
	@Getter
	private final Integer code;

	/** message */
	@Getter
	private final String msg;

	/**
	 * constructor
	 * 
	 * @param code
	 * @param msg
	 */
	private SystemCode(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	/**
	 * static constructor for map
	 */
	static {
		if (map == null) {
			map = new HashMap<Integer, SystemCode>();

			for (SystemCode c : SystemCode.values()) {
				// should not be duplicate
				if (map.containsKey(c.code)) {
					throw new RuntimeException("duplicate code in SystemCode, code=" + c.code);
				}
				map.put(c.code, c);
			}
		}
	}

	/**
	 * get system code by code, if not found, return X0
	 * 
	 * @param code
	 * @return
	 */
	public static SystemCode getSystemCode(Integer code) {
		if (map.containsKey(code)) {
			return map.get(code);
		}
		return SystemCode.EMPTY;
	}

	/**
	 * get system code by name like E000001
	 * 
	 * @param name
	 * @return
	 */
	public static SystemCode getSystemCode(String name) {
		try {
			return SystemCode.valueOf(name);
		} catch (IllegalArgumentException ex) {
			return SystemCode.EMPTY;
		}
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return "code[" + this.code + "],msg[" + this.msg + "] ";
	}

}
