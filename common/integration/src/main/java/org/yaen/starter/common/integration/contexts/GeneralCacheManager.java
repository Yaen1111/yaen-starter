package org.yaen.starter.common.integration.contexts;

import org.yaen.starter.common.integration.clients.RedisClient;
import org.yaen.starter.common.util.utils.StringUtil;

import lombok.Setter;

/**
 * general cache manager, static mode
 * 
 * @author Yaen 2016年5月19日下午6:42:54
 */
public class GeneralCacheManager {

	/** redis shiro cache prefix */
	private static final String REDIS_GENERAL_CACHE = "gen-cache:";

	/** db index, default is 0, cache use 1 */
	private static final int DB_INDEX = 0;

	/** the redis client for injection */
	@Setter
	private static RedisClient redisClient;

	/**
	 * build key
	 * 
	 * @param key
	 * @return
	 */
	protected static String buildCacheKey(Object key) {
		return REDIS_GENERAL_CACHE + StringUtil.toString(key);
	}

	/**
	 * get object
	 * 
	 * @param key
	 * @return
	 */
	public static Object get(Object key) {
		if (key == null)
			return null;

		return redisClient.getObjectByKey(DB_INDEX, buildCacheKey(key));
	}

	/**
	 * set object with expire, if value is null, delete it
	 * 
	 * @param key
	 * @param value
	 * @param expireInSecond
	 */
	public static void set(Object key, Object value, int expireInSecond) {
		if (key == null)
			return;

		// delete or update
		if (value == null) {
			redisClient.deleteByKey(DB_INDEX, buildCacheKey(key));
		} else {
			redisClient.saveObjectByKey(DB_INDEX, buildCacheKey(key), value, expireInSecond);
		}
	}

}
