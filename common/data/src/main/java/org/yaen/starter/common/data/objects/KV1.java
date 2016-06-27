package org.yaen.starter.common.data.objects;

import lombok.Getter;
import lombok.Setter;

/**
 * common key value pair
 * 
 * @param <K>
 * @param <V>
 * @author Yaen 2016年6月27日下午11:41:30
 */
@Getter
@Setter
public class KV1<K, V> {

	/** key */
	private K key;

	/** value */
	private V value;

	/**
	 * constructor
	 */
	public KV1(K key, V value) {
		this.key = key;
		this.value = value;
	}
}
