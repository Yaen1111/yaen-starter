/**
 * 
 */
package org.yaen.ark.common.dal.models;

import java.lang.reflect.Field;

import lombok.Getter;
import lombok.Setter;

/**
 * element info, collect from element annotations
 * 
 * @author xl 2016年1月4日下午8:38:45
 */
@Getter
@Setter
public class ElementInfo {

	private String columnName;

	private String dataType;

	private int dataSize;

	private Object value;

	private Field field;

}
