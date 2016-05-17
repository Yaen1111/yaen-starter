/**
 * 
 */
package org.yaen.starter.common.data.exceptions;

/**
 * core layer exception
 * 
 * @author Yaen 2016年1月13日下午7:31:52
 */
public class CoreException extends Exception {
	private static final long serialVersionUID = -7710308700640697529L;
	
	/**
	 * create an empty biz exception
	 */
	public CoreException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public CoreException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public CoreException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public CoreException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public CoreException(Throwable cause) {
		super(cause);
	}

}
