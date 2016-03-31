package org.yaen.ark.web.home.utils;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import org.springframework.util.StringUtils;

public class JedisUtil {

	
	private CacheManager cacheManager;
	
	private final static  Logger log = LoggerFactory.getLogger(JedisUtil.class);
	private Properties properties;
	private static String REDIS_FILE ="common.properties";
	private void init(){
		try { 
			cacheManager =	(CacheManager) SpringBeanHolder.getBean("cacheManager");
			
		} catch (Exception e) {
			log.error("读取配置文件出错",e);
		}
	}
	public static JedisUtil getInstance(){
		return SingleContainer.instance;
	}
	
	public static class SingleContainer{
	        private static JedisUtil instance = new JedisUtil();
	        
	}
	public JedisUtil(){
		
		log.info("初始化redisUtil开始读取配置文件。。。。");
		init();
		log.info("初始化redisUtil结束读取配置文件。。。。");
	}
	
	public CacheManager getCacheManager() {
		
		return cacheManager;
	}
	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}
	
	public String getProperty(String key){
		try{
			String value=properties.getProperty(key);
			if(StringUtils.hasLength(value)){
				return value.trim();
			}
			return value;
		}catch(Exception e) {
			log.error(" key: " + key + " not found.",e);
//			e.printStackTrace();
		}
		return "";
	}
}
