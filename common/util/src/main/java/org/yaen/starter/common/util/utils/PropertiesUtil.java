package org.yaen.starter.common.util.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;

import lombok.extern.slf4j.Slf4j;

/**
 * get properties from file in runtime, in utf-8
 * <p>
 * use context with injection is better?
 * 
 * @author Yaen 2016年5月11日下午1:54:15
 */
@Slf4j
public class PropertiesUtil {
	/** the FIXED filename for properties */
	public static final String PROPERTIES_FILENAME = "common.properties";

	/** the properties */
	private static Properties properties = null;

	/**
	 * static constructor
	 */
	static {
		properties = new Properties();
		String propertiesfilename = "";
		try {
			properties.load(
					new InputStreamReader((new ClassPathResource(PROPERTIES_FILENAME)).getInputStream(), "UTF-8"));
		} catch (IOException ex) {
			log.error("can not load properties file {}", propertiesfilename);
			log.error("exception is ", ex);
		}
	}

	/**
	 * get property by key, return null if not found
	 * 
	 * @param key
	 * @return
	 */
	public static String getProperty(String key) {
		return properties.getProperty(key);
	}

	/**
	 * get property by key, return defaultValue if not found
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getProperty(String key, String defaultValue) {
		String value = getProperty(key);
		return value == null ? defaultValue : value;
	}
}