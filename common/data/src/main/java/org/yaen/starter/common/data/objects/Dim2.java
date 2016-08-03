package org.yaen.starter.common.data.objects;

import lombok.Getter;
import lombok.Setter;

/**
 * 2-dimension, a plane
 * 
 * @param <X>
 * @param <Y>
 * @author Yaen 2016年8月3日下午1:22:18
 */
@Getter
@Setter
public class Dim2<X, Y> {

	/** x */
	private X x;

	/** y */
	private Y y;

	/**
	 * @param x
	 * @param y
	 */
	public Dim2(X x, Y y) {
		this.x = x;
		this.y = y;
	}
}
