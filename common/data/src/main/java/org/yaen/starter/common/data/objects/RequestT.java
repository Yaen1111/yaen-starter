package org.yaen.starter.common.data.objects;

import lombok.Getter;
import lombok.Setter;

/**
 * common request with minimum data
 * 
 * @author Yaen 2015年11月30日下午11:56:39
 */
public class RequestT<T> extends Request {
	private static final long serialVersionUID = 2568184668139610378L;

	/** the inner item object */
	@Getter
	@Setter
	private T item;

	/**
	 * common request
	 */
	public RequestT() {
		super();
	}

}
