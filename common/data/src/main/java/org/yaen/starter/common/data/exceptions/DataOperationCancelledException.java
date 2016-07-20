package org.yaen.starter.common.data.exceptions;

import org.yaen.starter.common.data.enums.SystemCode;

/**
 * data operation is canceled by trigger
 * 
 * @author Yaen 2016年1月13日下午7:31:52
 */
public class DataOperationCancelledException extends DataException {
	private static final long serialVersionUID = -2869692681784416785L;

	/**
	 * @param message
	 * @param cause
	 */
	public DataOperationCancelledException(String message, Throwable cause) {
		super(SystemCode.E_DATA_OPERATION_CANCELLED.getCode(), message, cause);
	}

	/**
	 * @param message
	 */
	public DataOperationCancelledException(String message) {
		super(SystemCode.E_DATA_OPERATION_CANCELLED.getCode(), message);
	}

}
