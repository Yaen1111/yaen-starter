package org.yaen.starter.common.integration.clients.impl;

import org.yaen.starter.common.integration.clients.RedisClient;
import org.yaen.starter.common.util.utils.SerializeUtil;

import lombok.Setter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * redis client, can be used as session or cache
 * 
 * @author Yaen 2016年5月19日下午6:42:54
 */
public class RedisClientImpl implements RedisClient {

	/** the jedis pool for injection */
	@Setter
	private JedisPool jedisPool;

	/**
	 * get jedis object from pool
	 * 
	 * @return
	 */
	protected Jedis getJedis() {
		Jedis jedis = null;
		try {
			jedis = this.jedisPool.getResource();
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
	protected void returnResource(Jedis jedis, boolean isBroken) {
		if (jedis == null)
			return;
		if (isBroken)
			this.jedisPool.returnBrokenResource(jedis);
		else
			this.jedisPool.returnResource(jedis);
	}

	/**
	 * empty constructor
	 */
	public RedisClientImpl() {
	}

	/**
	 * @see org.yaen.starter.common.integration.clients.RedisClient#getValueByKey(int, byte[])
	 */
	@Override
	public byte[] getValueByKey(int dbIndex, byte[] key) {
		Jedis jedis = null;
		byte[] result = null;
		boolean isBroken = false;

		try {
			jedis = getJedis();
			jedis.select(dbIndex);
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
	 * @see org.yaen.starter.common.integration.clients.RedisClient#deleteByKey(int, byte[])
	 */
	@Override
	public void deleteByKey(int dbIndex, byte[] key) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = getJedis();
			jedis.select(dbIndex);
			jedis.del(key);
		} catch (Exception e) {
			isBroken = true;
			throw e;
		} finally {
			returnResource(jedis, isBroken);
		}
	}

	/**
	 * @see org.yaen.starter.common.integration.clients.RedisClient#saveValueByKey(int, byte[], byte[], int)
	 */
	@Override
	public void saveValueByKey(int dbIndex, byte[] key, byte[] value, int expireTime) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = getJedis();
			jedis.select(dbIndex);
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
	 * @see org.yaen.starter.common.integration.clients.RedisClient#getObjectByKey(int, java.lang.Object)
	 */
	@Override
	public Object getObjectByKey(int dbIndex, Object key) {
		// string to byte[], byte[] to object
		return SerializeUtil.deserialize(this.getValueByKey(dbIndex, SerializeUtil.serialize(key)));
	}

	/**
	 * @see org.yaen.starter.common.integration.clients.RedisClient#deleteByKey(int, java.lang.Object)
	 */
	@Override
	public void deleteByKey(int dbIndex, Object key) {
		// string to byte[]
		this.deleteByKey(dbIndex, SerializeUtil.serialize(key));
	}

	/**
	 * @see org.yaen.starter.common.integration.clients.RedisClient#saveObjectByKey(int, java.lang.Object,
	 *      java.lang.Object, int)
	 */
	@Override
	public void saveObjectByKey(int dbIndex, Object key, Object value, int expireTime) {
		// string to byte[], object to byte[]
		this.saveValueByKey(dbIndex, SerializeUtil.serialize(key), SerializeUtil.serialize(value), expireTime);
	}

}
