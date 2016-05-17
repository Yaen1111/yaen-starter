/**
 * 
 */
package org.yaen.starter.common.data.exceptions;

/**
 * web layer exception
 * 
 * @author Yaen 2016年1月13日下午7:31:52
 */
public class WebException extends Exception {
	private static final long serialVersionUID = -702765520278340647L;

	/**
	 * create an empty biz exception
	 */
	public WebException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public WebException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public WebException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public WebException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public WebException(Throwable cause) {
		super(cause);
	}

}
