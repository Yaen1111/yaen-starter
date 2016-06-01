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
 * index of one, used to create key by given columns, add on table level, as key maybe multi-columns
 * 
 * @author Yaen 2016年1月4日下午8:45:10
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface OneIndex {

	/**
	 * the columns, if multi-column, can be multi-column index
	 * 
	 * @return
	 */
	String[] value() default {};
}
