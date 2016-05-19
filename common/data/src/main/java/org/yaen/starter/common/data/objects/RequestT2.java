package org.yaen.starter.common.data.objects;

import lombok.Getter;
import lombok.Setter;

/**
 * common request with minimum data
 * 
 * 
 * @author Yaen 2015年11月30日下午11:56:39
 */
public class RequestT2<T1, T2> extends Request {
	private static final long serialVersionUID = -557931216018852895L;

	/** the inner item 1 */
	@Getter
	@Setter
	private T1 item1;

	/** the inner item 2 */
	@Getter
	@Setter
	private T2 item2;

	/**
	 * common request
	 */
	public RequestT2() {
		super();
	}

}
