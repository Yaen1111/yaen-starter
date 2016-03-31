/**
 * 
 */
package org.yaen.ark.common.dal.models;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * element model for mybatis dal
 * 
 * @author xl 2016年1月6日下午7:57:22
 */
@Getter
@Setter
public class ElementModel {

	private long id;

	private long baseId;

	private String tableName;
	
	/**
	 * the given field name should be add to column
	 */
	private String addFieldName;

	/**
	 * the given field name should be modified with type
	 */
	private String modifyFieldName;

	/**
	 * columns, with key of field name
	 */
	private Map<String, ElementInfo> columns;
}
