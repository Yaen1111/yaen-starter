package org.yaen.starter.web.home.utils;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * 
 * 
 * @author Yaen 2016年5月11日下午1:54:15
 */
public class PropertiesUtil {

	private static Properties properties = null;

	static {
		String COMMON_PROPERTIES_FILE = "common.properties";
		properties = new Properties();
		Resource resource = new ClassPathResource(COMMON_PROPERTIES_FILE);
		try {
			properties.load(resource.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getProperty(String key) {
		return properties.getProperty(key);

	}

	public static void main(String[] args) {
		System.out.println(PropertiesUtil.getProperty("wx.corpsecret"));
	}

}
