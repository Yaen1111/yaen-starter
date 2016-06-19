package org.yaen.starter.common.dal.entities;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * event model for event engine
 * 
 * @author Yaen 2016年1月6日下午7:57:22
 */
@Getter
@Setter
public class QueryEntity {

	private long rowid;

	private String id;

	private String tableName;

	private Map<String, Object> columns;

	private String whereClause;
}
