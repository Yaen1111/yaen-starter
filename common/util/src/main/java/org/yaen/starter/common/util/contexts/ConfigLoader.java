package org.yaen.starter.common.util.contexts;

import lombok.Getter;

/**
 * load config by spring injection
 * <p>
 * deprecated, just as a sample of static injection
 * 
 * @author Yaen 2016年6月3日下午4:19:00
 */
@Deprecated
//@Component
public class ConfigLoader {

	/** the outside properties file name */
	@Getter
	private static String propertiesFilename = "common.properties";

	/**
	 * setter for propertiesFilename
	 * 
	 * @param propertiesFilename
	 */
	//@Autowired
	public void setPropertiesFilename(String propertiesFilename) {
		ConfigLoader.propertiesFilename = propertiesFilename;
	}

}
