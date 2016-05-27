package org.yaen.starter.common.data.exceptions;

/**
 * data not exists for biz
 * 
 * @author Yaen 2016年1月13日下午7:31:52
 */
public class DataNotExistsBizException extends BizException {
	private static final long serialVersionUID = -928810488442832704L;

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public DataNotExistsBizException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DataNotExistsBizException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public DataNotExistsBizException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public DataNotExistsBizException(Throwable cause) {
		super(cause);
	}

}
