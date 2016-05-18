package org.yaen.starter.common.data.exceptions;

/**
 * data not exists
 * 
 * @author Yaen 2016年1月13日下午7:31:52
 */
public class DataNotExistsException extends Exception {
	private static final long serialVersionUID = 8154004478287341494L;

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public DataNotExistsException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DataNotExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public DataNotExistsException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public DataNotExistsException(Throwable cause) {
		super(cause);
	}

}
