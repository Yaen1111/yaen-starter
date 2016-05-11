/**
 * 
 */
package org.yaen.starter.web.home.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 文件操作工具类
 * 
 * @author Yaen 2015年9月18日下午4:19:25
 */
public class FileUtil {

	/** 路径分隔符Linux */
	public static final String PATH_SEPERIATOR_LINUX = "/";

	/** 路径分隔符Linux */
	public static final String PATH_SEPERIATOR_WINDOWS = "\\";

	/** 文件后缀名分隔符 */
	public static final String SUFFIX_SPLITOR = ".";

	/**
	 * 将路径名称统一为Linux格式
	 * 
	 * @param path
	 * @return
	 */
	public static String toLinuxPath(String path) {
		return StringUtils.replace(path, PATH_SEPERIATOR_WINDOWS, PATH_SEPERIATOR_LINUX);
	}

	/**
	 * 取得文件后缀名
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFileNameSuffix(String fileName) {
		if (StringUtils.isEmpty(fileName)) {
			return StringUtils.EMPTY;
		}
		int index = StringUtils.lastIndexOf(fileName, SUFFIX_SPLITOR);
		if (index < 0) {
			return StringUtils.EMPTY;
		}
		return StringUtils.substring(fileName, index + 1).toLowerCase();
	}
}
