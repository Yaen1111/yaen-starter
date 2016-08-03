package org.yaen.starter.common.data.objects;

import lombok.Getter;
import lombok.Setter;

/**
 * 3-dimension, cubic space
 * 
 * @param <X>
 * @param <Y>
 * @param <Z>
 * @author Yaen 2016年8月3日下午1:22:18
 */
@Getter
@Setter
public class Dim3<X, Y, Z> {

	/** x */
	private X x;

	/** y */
	private Y y;

	/** y */
	private Z z;

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Dim3(X x, Y y, Z z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
}
