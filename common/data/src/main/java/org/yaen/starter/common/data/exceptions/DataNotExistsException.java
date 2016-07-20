package org.yaen.starter.common.data.exceptions;

import org.yaen.starter.common.data.enums.SystemCode;

/**
 * data not exists
 * 
 * @author Yaen 2016年1月13日下午7:31:52
 */
public class DataNotExistsException extends DataException {
	private static final long serialVersionUID = -928810488442832704L;

	/**
	 * @param message
	 * @param cause
	 */
	public DataNotExistsException(String message, Throwable cause) {
		super(SystemCode.E_DATA_NOT_EXISTS.getCode(), message, cause);
	}

	/**
	 * @param message
	 */
	public DataNotExistsException(String message) {
		super(SystemCode.E_DATA_NOT_EXISTS.getCode(), message);
	}

}
