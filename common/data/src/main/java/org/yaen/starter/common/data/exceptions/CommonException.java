package org.yaen.starter.common.data.exceptions;

/**
 * common layer exception
 * 
 * @author Yaen 2016年1月13日下午7:31:52
 */
public class CommonException extends StarterException {
	private static final long serialVersionUID = -3212540352141901386L;

	/**
	 * constructor with code
	 * 
	 * @param code
	 * @param message
	 * @param cause
	 */
	public CommonException(Integer code, String message, Throwable cause) {
		super(code, message, cause);
	}

	/**
	 * constructor with code
	 * 
	 * @param code
	 * @param message
	 */
	public CommonException(Integer code, String message) {
		super(code, message);
	}

	/**
	 * constructor without code
	 * 
	 * @param message
	 * @param cause
	 */
	public CommonException(String message, Throwable cause) {
		super(0, message, cause);
	}

	/**
	 * constructor without code
	 * 
	 * @param message
	 */
	public CommonException(String message) {
		super(0, message);
	}

	/**
	 * constructor without code
	 * 
	 * @param cause
	 */
	public CommonException(Throwable cause) {
		super(0, null, cause);
	}

}
