package org.yaen.starter.common.data.objects;

import lombok.Getter;
import lombok.Setter;

/**
 * common request with minimum data
 * 
 * @author Yaen 2015年11月30日下午11:56:39
 */
public class ResponseT2<T1, T2> extends Response {
	private static final long serialVersionUID = 7255714109347640536L;

	/**
	 * the inner item 1
	 */
	@Getter
	@Setter
	private T1 item1;

	/**
	 * the inner item 2
	 */
	@Getter
	@Setter
	private T2 item2;

	/**
	 * common response
	 */
	public ResponseT2() {
		super();
	}

}
