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

	/** where/and fieldname = value */
	private List<NameValue> whereEquals = new ArrayList<NameValue>();

	/** order by list, true as asc, false as desc */
	private List<NameValueT<Boolean>> orders = new ArrayList<NameValueT<Boolean>>();

	/** pager for limit */
	private Pager pager = new Pager(0);
}
