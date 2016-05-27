package org.yaen.starter.common.data.exceptions;

/**
 * duplicate data found where is not allowed for biz
 * 
 * @author Yaen 2016年1月13日下午7:31:52
 */
public class DuplicateDataBizException extends BizException {
	private static final long serialVersionUID = -373810400540795151L;

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public DuplicateDataBizException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DuplicateDataBizException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public DuplicateDataBizException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public DuplicateDataBizException(Throwable cause) {
		super(cause);
	}

}
