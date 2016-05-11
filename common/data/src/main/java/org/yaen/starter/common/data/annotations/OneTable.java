/**
 * 
 */
package org.yaen.starter.common.data.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * table of one, contains all table info
 * 
 * @author Yaen 2016年1月4日下午8:45:10
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface OneTable {

	/**
	 * the name of table, if not set, use class name
	 * 
	 * @return
	 */
	String TableName();
}
