package org.yaen.starter.common.data.exceptions;

/**
 * none-layer data exception
 * 
 * @author Yaen 2016年1月13日下午7:31:52
 */
public class DataException extends StarterException {
	private static final long serialVersionUID = 6778097027236207501L;

	/**
	 * constructor with code
	 * 
	 * @param code
	 * @param message
	 * @param cause
	 */
	public DataException(Integer code, String message, Throwable cause) {
		super(code, message, cause);
	}

	/**
	 * constructor with code
	 * 
	 * @param code
	 * @param message
	 */
	public DataException(Integer code, String message) {
		super(code, message);
	}

}
