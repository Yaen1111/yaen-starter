package org.yaen.starter.common.data.exceptions;

/**
 * core layer exception
 * 
 * @author Yaen 2016年1月13日下午7:31:52
 */
public class CoreException extends StarterException {
	private static final long serialVersionUID = -7710308700640697529L;

	/**
	 * constructor with code
	 * 
	 * @param code
	 * @param message
	 * @param cause
	 */
	public CoreException(Integer code, String message, Throwable cause) {
		super(code, message, cause);
	}

	/**
	 * constructor with code
	 * 
	 * @param code
	 * @param message
	 */
	public CoreException(Integer code, String message) {
		super(code, message);
	}

	/**
	 * constructor without code
	 * 
	 * @param message
	 * @param cause
	 */
	public CoreException(String message, Throwable cause) {
		super(0, message, cause);
	}

	/**
	 * constructor without code
	 * 
	 * @param message
	 */
	public CoreException(String message) {
		super(0, message);
	}

	/**
	 * constructor without code
	 * 
	 * @param cause
	 */
	public CoreException(Throwable cause) {
		super(0, null, cause);
	}

}
