package org.yaen.starter.common.data.exceptions;

import org.yaen.starter.common.data.enums.SystemCode;

/**
 * none-layer test exception
 * 
 * @author Yaen 2016年8月1日下午3:51:53
 */
public class TestException extends StarterException {
	private static final long serialVersionUID = 4699519779213224496L;

	/**
	 * constructor with code
	 * 
	 * @param message
	 * @param cause
	 */
	public TestException(String message, Throwable cause) {
		super(SystemCode.E_TEST.getCode(), message, cause);
	}

	/**
	 * constructor with code
	 * 
	 * @param message
	 */
	public TestException(String message) {
		super(SystemCode.E_TEST.getCode(), message);
	}

}
