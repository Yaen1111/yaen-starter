package org.yaen.starter.common.data.objects;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * query builder for query service
 * 
 * @author Yaen 2015年12月1日上午12:27:12
 */
@Getter
@Setter
public class QueryBuilder {

	/** where/and name = value */
	private List<NameValue> whereEquals = new ArrayList<NameValue>();

	/** order by list */
	private List<String> orders = new ArrayList<String>();

	/** pager for limit */
	private Pager pager = new Pager(0);
}
