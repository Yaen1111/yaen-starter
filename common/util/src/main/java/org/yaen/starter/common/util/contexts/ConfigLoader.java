package org.yaen.starter.common.util.contexts;

import org.springframework.stereotype.Component;

import lombok.Getter;

/**
 * load config by spring injection
 * 
 * @author Yaen 2016年6月3日下午4:19:00
 */
@Component
public class ConfigLoader {

	/** the outside properties file name */
	@Getter
	private static String propertiesFilename = "common.properties";

	/**
	 * setter for propertiesFilename
	 * 
	 * @param propertiesFilename
	 */
	public void setPropertiesFilename(String propertiesFilename) {
		ConfigLoader.propertiesFilename = propertiesFilename;
	}

}
