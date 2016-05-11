package org.yaen.starter.common.util;


/**
 * native c++ dll loader
 * 
 * 
 * @author Yaen 2015年12月8日下午11:48:02
 */
public class DllUtil {

	public static void LoadDll(String dll)
	{
		System.loadLibrary(dll);
	}
}
