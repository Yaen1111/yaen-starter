package org.yaen.starter.common.data.exceptions;

/**
 * incorrect credentials, usually is password error, ssh check error, etc, for biz
 * 
 * @author Yaen 2016年1月13日下午7:31:52
 */
public class IncorrectCredentialsBizException extends BizException {
	private static final long serialVersionUID = -373810400540795151L;

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public IncorrectCredentialsBizException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public IncorrectCredentialsBizException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public IncorrectCredentialsBizException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public IncorrectCredentialsBizException(Throwable cause) {
		super(cause);
	}

}
