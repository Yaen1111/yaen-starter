/**
 * 
 */
package org.yaen.starter.common.data.pos;

import java.lang.reflect.Field;

import lombok.Getter;
import lombok.Setter;

/**
 * one column, can be customized by One annotation
 * 
 * @author Yaen 2016年1月4日下午8:38:45
 */
@Getter
@Setter
public class OneColumnPO {

	private String columnName;

	private String dataType;

	private int dataSize;

	private Object value;

	private Field field;

}
