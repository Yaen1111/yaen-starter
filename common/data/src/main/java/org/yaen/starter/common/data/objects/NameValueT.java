package org.yaen.starter.common.data.objects;

import lombok.Getter;
import lombok.Setter;

/**
 * common name value with type of V
 * 
 * @param <V>
 * @author Yaen 2016年6月27日下午11:47:33
 */
@Getter
@Setter
public class NameValueT<V> {

	/** name */
	private String name;

	/** value */
	private V value;

	/**
	 * constructor
	 */
	public NameValueT(String name, V value) {
		this.name = name;
		this.value = value;
	}
}
