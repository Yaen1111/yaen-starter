package org.yaen.starter.core.model.services;

/**
 * cache service to read/write cache to redis
 * 
 * @author Yaen 2016年6月19日下午4:37:50
 */
public interface CacheService {

	/**
	 * get cache by key
	 * 
	 * @param key
	 * @return
	 */
	Object get(Object key);

	/**
	 * set/delete cache by key, delete cache if value is null. -1 means no expire
	 * 
	 * @param key
	 * @param value
	 * @param expireInSecond
	 */
	void set(Object key, Object value, int expireInSecond);
}
