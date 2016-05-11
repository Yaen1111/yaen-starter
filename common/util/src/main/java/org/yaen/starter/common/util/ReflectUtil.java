package org.yaen.starter.common.util;

import java.lang.reflect.Field;

/**
 * reflect util, deal with reflections
 * 
 * 
 * @author Yaen 2016年1月8日下午1:39:13
 */
public class ReflectUtil {

	/**
	 * get all public field list
	 * 
	 * @param clazz
	 * @return
	 */
	public static Field[] getAllPublicFields(Class<?> clazz) throws SecurityException {
		if (clazz == null)
			return new Field[] {};

		return clazz.getFields();
	}

	/**
	 * get private field list, but not the base class
	 * 
	 * @param clazz
	 * @return
	 */
	public static Field[] getPrivateFields(Class<?> clazz) throws SecurityException {
		if (clazz == null)
			return new Field[] {};

		return clazz.getDeclaredFields();
	}
}
