package org.yaen.starter.common.integration.clients;

import redis.clients.jedis.JedisPool;

/**
 * redis client, can be used as session or cache
 * 
 * @author Yaen 2016年5月19日下午6:42:54
 */
public interface RedisClient {

	/** the jedis pool for injection */
	void setJedisPool(JedisPool jedisPool);

	/**
	 * get value by key
	 * 
	 * @param dbIndex
	 * @param key
	 * @return
	 */
	byte[] getValueByKey(int dbIndex, byte[] key);

	/**
	 * delete value by key
	 * 
	 * @param dbIndex
	 * @param key
	 */
	void deleteByKey(int dbIndex, byte[] key);

	/**
	 * save value by key
	 * 
	 * @param dbIndex
	 * @param key
	 * @param value
	 * @param expireTime
	 */
	void saveValueByKey(int dbIndex, byte[] key, byte[] value, int expireTime);

	/**
	 * get object by key
	 * 
	 * @param dbIndex
	 * @param key
	 * @return
	 */
	Object getObjectByKey(int dbIndex, Object key);

	/**
	 * delete value by key
	 * 
	 * @param dbIndex
	 * @param key
	 */
	void deleteByKey(int dbIndex, Object key);

	/**
	 * save object by key
	 * 
	 * @param dbIndex
	 * @param key
	 * @param value
	 * @param expireTime
	 */
	void saveObjectByKey(int dbIndex, Object key, Object value, int expireTime);

}
