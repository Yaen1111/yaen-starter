package org.yaen.starter.common.data.exceptions;

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
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public NoDataAffectedException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public NoDataAffectedException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public NoDataAffectedException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public NoDataAffectedException(Throwable cause) {
		super(cause);
	}

}
