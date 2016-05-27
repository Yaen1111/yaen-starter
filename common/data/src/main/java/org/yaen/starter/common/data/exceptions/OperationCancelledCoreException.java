package org.yaen.starter.common.data.exceptions;

/**
 * operation is cancelled by some reason
 * 
 * @author Yaen 2016年1月13日下午7:31:52
 */
public class OperationCancelledCoreException extends CoreException {
	private static final long serialVersionUID = -2869692681784416785L;

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public OperationCancelledCoreException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public OperationCancelledCoreException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public OperationCancelledCoreException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public OperationCancelledCoreException(Throwable cause) {
		super(cause);
	}

}
