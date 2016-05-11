/**
 * 
 */
package org.yaen.starter.common.data.exceptions;

import lombok.Getter;
import lombok.Setter;

/**
 * biz(business) exception, usually is logical/dependence exception
 * 
 * @author Yaen 2016年1月13日下午7:31:52
 */
public class BizException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4511287732017780755L;

	/**
	 * the biz name, optional
	 */
	@Getter
	@Setter
	private String bizName;

	/**
	 * create an empty biz exception
	 */
	public BizException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public BizException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public BizException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public BizException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public BizException(Throwable cause) {
		super(cause);
	}

}
