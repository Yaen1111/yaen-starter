package org.yaen.starter.common.data.exceptions;

/**
 * cross-layer data exception
 * 
 * @author Yaen 2016年1月13日下午7:31:52
 */
public class DataException extends Exception {
	private static final long serialVersionUID = 6778097027236207501L;

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public DataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DataException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public DataException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public DataException(Throwable cause) {
		super(cause);
	}

}
