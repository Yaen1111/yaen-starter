package org.yaen.starter.common.data.objects;

import lombok.Getter;
import lombok.Setter;

/**
 * 1-dimension, a line
 * 
 * @param <X>
 * @author Yaen 2016年8月3日下午1:22:18
 */
@Getter
@Setter
public class Dim1<X> {

	/** x */
	private X x;

	/**
	 * @param x
	 */
	public Dim1(X x) {
		this.x = x;
	}
}
