package org.yaen.starter.common.integration.clients.impl;

import org.yaen.starter.common.integration.clients.RedisClient;
import org.yaen.starter.common.util.utils.SerializeUtil;

import lombok.Setter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * redis client, can be used as session or cache
 * <p>
 * inject by bean, not service
 * 
 * @author Yaen 2016年5月19日下午6:42:54
 */
// @Service
public class RedisClientImpl implements RedisClient {

	/** the jedis pool for injection */
	@Setter
	private JedisPool jedisPool;

	/** the redis db index for injection, optional */
	@Setter
	private int dbIndex = 0;

	/**
	 * get jedis object from pool, and select given index
	 * 
	 * @return
	 */
	private Jedis getJedis() {
		Jedis jedis = null;
		try {
			jedis = this.jedisPool.getResource();
			jedis.select(this.dbIndex);
		} catch (Exception e) {
			throw new JedisConnectionException(e);
		}
		return jedis;
	}

	/**
	 * return jedis to pool
	 * 
	 * @param jedis
	 * @param isBroken
	 */
	private void returnResource(Jedis jedis, boolean isBroken) {
		if (jedis == null)
			return;
		if (isBroken)
			this.jedisPool.returnBrokenResource(jedis);
		else
			this.jedisPool.returnResource(jedis);
	}

	/**
	 * @see org.yaen.starter.common.integration.clients.RedisClient#getValueByKey(byte[])
	 */
	@Override
	public byte[] getValueByKey(byte[] key) {
		Jedis jedis = null;
		byte[] result = null;
		boolean isBroken = false;

		try {
			jedis = getJedis();
			result = jedis.get(key);
		} catch (Exception ex) {
			isBroken = true;
			throw ex;
		} finally {
			returnResource(jedis, isBroken);
		}
		return result;
	}

	/**
	 * @see org.yaen.starter.common.integration.clients.RedisClient#deleteByKey(byte[])
	 */
	@Override
	public void deleteByKey(byte[] key) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = getJedis();
			jedis.del(key);
		} catch (Exception e) {
			isBroken = true;
			throw e;
		} finally {
			returnResource(jedis, isBroken);
		}
	}

	/**
	 * @see org.yaen.starter.common.integration.clients.RedisClient#saveValueByKey(byte[], byte[], int)
	 */
	@Override
	public void saveValueByKey(byte[] key, byte[] value, int expireTime) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = getJedis();
			jedis.set(key, value);
			if (expireTime > 0)
				jedis.expire(key, expireTime);
		} catch (Exception e) {
			isBroken = true;
			throw e;
		} finally {
			returnResource(jedis, isBroken);
		}
	}

	/**
	 * @see org.yaen.starter.common.integration.clients.RedisClient#getObjectByKey(java.lang.Object)
	 */
	@Override
	public Object getObjectByKey(Object key) {
		// string to byte[], byte[] to object
		return SerializeUtil.deserialize(this.getValueByKey(SerializeUtil.serialize(key)));
	}

	/**
	 * @see org.yaen.starter.common.integration.clients.RedisClient#deleteByKey(java.lang.Object)
	 */
	@Override
	public void deleteByKey(Object key) {
		// string to byte[]
		this.deleteByKey(SerializeUtil.serialize(key));
	}

	/**
	 * @see org.yaen.starter.common.integration.clients.RedisClient#saveObjectByKey(java.lang.Object, java.lang.Object,
	 *      int)
	 */
	@Override
	public void saveObjectByKey(Object key, Object value, int expireTime) {
		// string to byte[], object to byte[]
		this.saveValueByKey(SerializeUtil.serialize(key), SerializeUtil.serialize(value), expireTime);
	}

}
