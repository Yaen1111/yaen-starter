/**
 * 
 */
package org.yaen.starter.common.util.utils;

import org.springframework.util.Assert;

/**
 * assert util mostly for check parameters
 * 
 * @author Yaen 2016年5月17日下午2:52:30
 */
public class AssertUtil extends Assert {

	/**
	 * assert not empty
	 * 
	 * <pre>
	 * null = no
	 * "" = no
	 * " " = ok
	 * "a" = ok
	 * </pre>
	 * 
	 * @param s
	 */
	public static void notEmpty(String s) {
		AssertUtil.notNull(s);

		if (StringUtil.isEmpty(s)) {
			throw new IllegalArgumentException(
					"[Assertion failed] - this argument is required; it must not be null or empty");
		}
	}

	/**
	 * assert not blank
	 * 
	 * <pre>
	 * null = no
	 * "" = no
	 * " " = no
	 * "a" = ok
	 * </pre>
	 * 
	 * @param s
	 */
	public static void notBlank(String s) {
		AssertUtil.notNull(s);

		if (StringUtil.isBlank(s)) {
			throw new IllegalArgumentException(
					"[Assertion failed] - this argument is required; it must not be null or empty or blank");
		}
	}

}
