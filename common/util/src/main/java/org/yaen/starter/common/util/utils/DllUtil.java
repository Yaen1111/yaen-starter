package org.yaen.starter.common.util.utils;

/**
 * native dll/so loader
 * 
 * @author Yaen 2015年12月8日下午11:48:02
 */
public class DllUtil {

	/**
	 * load native dll/so for java
	 * 
	 * @param dll
	 */
	public static void LoadDll(String dll) {
		System.loadLibrary(dll);
	}
}
