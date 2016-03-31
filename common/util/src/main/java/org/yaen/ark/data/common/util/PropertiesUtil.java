package org.yaen.ark.data.common.util;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class PropertiesUtil {
	/** logger */
	public static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
	private static Properties properties = null;
	
	static  { 
			String COMMON_PROPERTIES_FILE="common.properties";
			properties = new Properties();
			Resource resource = new ClassPathResource(COMMON_PROPERTIES_FILE);
			try {
				properties.load(resource.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
	}
	public static String getProperty(String key){ 
		return properties.getProperty(key);
		
	}
	
	public static void main(String[] args) {
		System.out.println(PropertiesUtil.getProperty("wx.corpsecret"));
	}
	
}
