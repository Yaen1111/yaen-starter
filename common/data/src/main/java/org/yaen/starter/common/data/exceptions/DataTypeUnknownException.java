package org.yaen.starter.common.data.exceptions;

import org.yaen.starter.common.data.enums.SystemCode;

/**
 * data type unknown, mainly used for enum/code
 * 
 * @author Yaen 2016年1月13日下午7:31:52
 */
public class DataTypeUnknownException extends DataException {
	private static final long serialVersionUID = 1009476460186263618L;

	/**
	 * @param message
	 * @param cause
	 */
	public DataTypeUnknownException(String message, Throwable cause) {
		super(SystemCode.E_DATA_NOT_EXISTS.getCode(), message, cause);
	}

	/**
	 * @param message
	 */
	public DataTypeUnknownException(String message) {
		super(SystemCode.E_DATA_NOT_EXISTS.getCode(), message);
	}

}
