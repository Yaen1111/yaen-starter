/**
 * 
 */
package org.yaen.ark.core.model.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * table of element, contains all parent info
 * 
 * @author xl 2016年1月4日下午8:45:10
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ElementTable {

	/**
	 * the prefix of the sub_element
	 * 
	 * 
	 * @return
	 */
	String TableName();
}
