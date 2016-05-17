/**
 * 
 */
package org.yaen.starter.common.data.exceptions;

/**
 * common layer exception
 * 
 * @author Yaen 2016年1月13日下午7:31:52
 */
public class CommonException extends Exception {
	private static final long serialVersionUID = -3212540352141901386L;

	/**
	 * create an empty biz exception
	 */
	public CommonException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public CommonException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public CommonException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public CommonException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public CommonException(Throwable cause) {
		super(cause);
	}

}
