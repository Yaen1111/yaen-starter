package org.yaen.starter.common.data.exceptions;

import org.yaen.starter.common.data.enums.SystemCode;

import lombok.Getter;

/**
 * starter exception, base of whole starter system
 * 
 * @author Yaen 2016年7月20日上午10:23:36
 */
public class StarterException extends Exception {
	private static final long serialVersionUID = 4265649967698332620L;

	/** the inner code of the exception, see SystemCode */
	@Getter
	private Integer code;

	/**
	 * constructor with code
	 * 
	 * @param code
	 * @param message
	 * @param cause
	 */
	public StarterException(Integer code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	/**
	 * constructor with code
	 * 
	 * @param code
	 * @param message
	 */
	public StarterException(Integer code, String message) {
		super(message);
		this.code = code;
	}

	/**
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */
	@Override
	public String getLocalizedMessage() {
		// use this to get message of code
		String msg = null;

		if (this.code != null && this.code != 0) {
			// add code and message
			SystemCode c = SystemCode.getSystemCode(this.code);
			msg = c.toString() + "," + super.getLocalizedMessage();
		} else {
			msg = super.getLocalizedMessage();
		}

		return msg;
	}
}
