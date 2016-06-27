package org.yaen.starter.common.data.objects;

import lombok.Getter;
import lombok.Setter;

/**
 * common name value with type of object
 * 
 * @author Yaen 2016年6月27日下午11:41:30
 */
@Getter
@Setter
public class NameValue {

	/** name */
	private String name;

	/** value */
	private Object value;

	/**
	 * constructor
	 */
	public NameValue(String name, Object value) {
		this.name = name;
		this.value = value;
	}
}
