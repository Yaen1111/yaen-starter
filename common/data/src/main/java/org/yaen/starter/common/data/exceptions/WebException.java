package org.yaen.starter.common.data.exceptions;

/**
 * web layer exception
 * 
 * @author Yaen 2016年1月13日下午7:31:52
 */
public class WebException extends StarterException {
	private static final long serialVersionUID = -702765520278340647L;

	/**
	 * constructor with code
	 * 
	 * @param code
	 * @param message
	 * @param cause
	 */
	public WebException(Integer code, String message, Throwable cause) {
		super(code, message, cause);
	}

	/**
	 * constructor with code
	 * 
	 * @param code
	 * @param message
	 */
	public WebException(Integer code, String message) {
		super(code, message);
	}

	/**
	 * constructor without code
	 * 
	 * @param message
	 * @param cause
	 */
	public WebException(String message, Throwable cause) {
		super(0, message, cause);
	}

	/**
	 * constructor without code
	 * 
	 * @param message
	 */
	public WebException(String message) {
		super(0, message);
	}

	/**
	 * constructor without code
	 * 
	 * @param cause
	 */
	public WebException(Throwable cause) {
		super(0, null, cause);
	}

}
