package org.yaen.starter.common.data.objects;

import lombok.Getter;
import lombok.Setter;

/**
 * common request with minimum data
 * 
 * @author Yaen 2015年11月30日下午11:56:39
 */
public class ResponseT<T> extends Response {
	private static final long serialVersionUID = 7255714109347640536L;

	/** the inner item object */
	@Getter
	@Setter
	private T item;

	/**
	 * common response
	 */
	public ResponseT() {
		super();
	}

}
