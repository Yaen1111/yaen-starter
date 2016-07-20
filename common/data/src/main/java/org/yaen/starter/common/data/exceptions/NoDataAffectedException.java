package org.yaen.starter.common.data.exceptions;

import org.yaen.starter.common.data.enums.SystemCode;

/**
 * no data affected during UPDATE/INSERT/DELETE
 * 
 * @author Yaen 2016年1月13日下午7:31:52
 */
public class NoDataAffectedException extends DataException {
	private static final long serialVersionUID = 5639853326661722991L;

	/**
	 * @param message
	 * @param cause
	 */
	public NoDataAffectedException(String message, Throwable cause) {
		super(SystemCode.E_NO_DATA_AFFECTED.getCode(), message, cause);
	}

	/**
	 * @param message
	 */
	public NoDataAffectedException(String message) {
		super(SystemCode.E_NO_DATA_AFFECTED.getCode(), message);
	}

}
