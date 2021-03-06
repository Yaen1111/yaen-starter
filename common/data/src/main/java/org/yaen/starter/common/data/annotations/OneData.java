package org.yaen.starter.common.data.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.yaen.starter.common.data.enums.DataTypes;

/**
 * define data type for the one
 * 
 * @author Yaen 2016年1月4日下午8:45:10
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface OneData {

	/**
	 * the data field/column name, if not set, default to the field name
	 * 
	 * @return
	 */
	String FieldName() default "";

	/**
	 * the datatype, usually int, bigint, string/varchar, date, time, datetime, etc
	 * 
	 * @return
	 */
	String DataType() default DataTypes.VARCHAR64;

	/**
	 * the data size if need, 0 for default
	 * 
	 * @return
	 */
	int DataSize() default 0;

	/**
	 * the scale size if need, mainly for decimal type, 0 for not used
	 * 
	 * @return
	 */
	int ScaleSize() default 0;

}
