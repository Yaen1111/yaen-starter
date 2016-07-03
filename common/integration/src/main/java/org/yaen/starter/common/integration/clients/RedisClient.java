package org.yaen.starter.common.integration.clients;

import redis.clients.jedis.JedisPool;

/**
 * redis client, can be used as session or cache
 * 
 * @author Yaen 2016年5月19日下午6:42:54
 */
public interface RedisClient {

	/**
	 * the jedis pool for injection
	 * 
	 * @param jedisPool
	 */
	void setJedisPool(JedisPool jedisPool);

	/**
	 * the redis db index for injection, optional
	 * 
	 * @param dbIndex
	 */
	void setDbIndex(int dbIndex);

	/**
	 * get value by key
	 * 
	 * @param key
	 * @return
	 */
	byte[] getValueByKey(byte[] key);

	/**
	 * delete value by key
	 * 
	 * @param key
	 */
	void deleteByKey(byte[] key);

	/**
	 * save value by key
	 * 
	 * @param key
	 * @param value
	 * @param expireTime
	 */
	void saveValueByKey(byte[] key, byte[] value, int expireTime);

	/**
	 * get object by key
	 * 
	 * @param key
	 * @return
	 */
	Object getObjectByKey(Object key);

	/**
	 * delete value by key
	 * 
	 * @param key
	 */
	void deleteByKey(Object key);

	/**
	 * save object by key
	 * 
	 * @param key
	 * @param value
	 * @param expireTime
	 */
	void saveObjectByKey(Object key, Object value, int expireTime);

}
