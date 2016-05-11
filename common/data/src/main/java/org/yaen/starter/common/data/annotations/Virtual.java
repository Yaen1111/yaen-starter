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
 * mark the method as virtual, just for design purpose
 * 
 * @author Yaen 2016年1月5日下午12:05:39
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface Virtual {

}
