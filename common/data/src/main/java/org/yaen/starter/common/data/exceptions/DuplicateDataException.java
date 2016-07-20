package org.yaen.starter.common.data.exceptions;

import org.yaen.starter.common.data.enums.SystemCode;

/**
 * duplicate data found where is not allowed
 * 
 * @author Yaen 2016年1月13日下午7:31:52
 */
public class DuplicateDataException extends DataException {
	private static final long serialVersionUID = -373810400540795151L;

	/**
	 * @param message
	 * @param cause
	 */
	public DuplicateDataException(String message, Throwable cause) {
		super(SystemCode.E_DUPLICATE_DATA.getCode(), message, cause);
	}

	/**
	 * @param message
	 */
	public DuplicateDataException(String message) {
		super(SystemCode.E_DUPLICATE_DATA.getCode(), message);
	}

}
