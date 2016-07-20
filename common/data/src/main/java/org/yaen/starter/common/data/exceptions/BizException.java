package org.yaen.starter.common.data.exceptions;

/**
 * biz(business) layer exception
 * 
 * @author Yaen 2016年1月13日下午7:31:52
 */
public class BizException extends StarterException {
	private static final long serialVersionUID = -4511287732017780755L;

	/**
	 * constructor with code
	 * 
	 * @param code
	 * @param message
	 * @param cause
	 */
	public BizException(Integer code, String message, Throwable cause) {
		super(code, message, cause);
	}

	/**
	 * constructor with code
	 * 
	 * @param code
	 * @param message
	 */
	public BizException(Integer code, String message) {
		super(code, message);
	}

	/**
	 * constructor without code
	 * 
	 * @param message
	 * @param cause
	 */
	public BizException(String message, Throwable cause) {
		super(0, message, cause);
	}

	/**
	 * constructor without code
	 * 
	 * @param message
	 */
	public BizException(String message) {
		super(0, message);
	}

	/**
	 * constructor without code
	 * 
	 * @param cause
	 */
	public BizException(Throwable cause) {
		super(0, null, cause);
	}

}
