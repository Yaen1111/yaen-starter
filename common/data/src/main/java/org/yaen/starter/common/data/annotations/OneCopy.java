package org.yaen.starter.common.data.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * copy to one, means contain whole field info
 * 
 * @author Yaen 2016年1月4日下午8:45:10
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface OneCopy {

	/**
	 * the prefix of the sub_element
	 * 
	 * @return
	 */
	String Prefix();
}
