package org.yaen.starter.common.data.objects;

import lombok.Getter;
import lombok.Setter;

/**
 * 4-dimension, cubic space with time
 * 
 * @param <X>
 * @param <Y>
 * @param <Z>
 * @param <T>
 * @author Yaen 2016年8月3日下午1:22:18
 */
@Getter
@Setter
public class Dim4<X, Y, Z, T> {

	/** x */
	private X x;

	/** y */
	private Y y;

	/** y */
	private Z z;

	/** t */
	private T t;

	/**
	 * @param x
	 * @param y
	 * @param z
	 * @param t
	 */
	public Dim4(X x, Y y, Z z, T t) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.t = t;
	}
}
