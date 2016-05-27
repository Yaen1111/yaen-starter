package org.yaen.starter.common.util.contexts;

import java.util.HashSet;
import java.util.Set;

import com.alibaba.fastjson.serializer.PropertyFilter;

import lombok.Getter;

/**
 * the filter for fast json
 *
 * @author Yaen
 *
 */
public class FastJsonFilter implements PropertyFilter {

	/** the includes, all is consider included if empty, check excludes first, than includes */
	@Getter
	private final Set<String> includes = new HashSet<String>();

	/** the excludes, check excludes first, than includes */
	@Getter
	private final Set<String> excludes = new HashSet<String>();

	/**
	 * filter, excludes first, then includes
	 * 
	 * @see com.alibaba.fastjson.serializer.PropertyFilter#apply(java.lang.Object, java.lang.String, java.lang.Object)
	 */
	@Override
	public boolean apply(Object source, String name, Object value) {

		if (excludes.contains(name)) {
			return false;
		}
		if (excludes.contains(source.getClass().getSimpleName() + "." + name)) {
			return false;
		}
		if ((includes.size() == 0) || includes.contains(name)) {
			return true;
		}
		if (includes.contains(source.getClass().getSimpleName() + "." + name)) {
			return true;
		}
		return false;
	}

}
