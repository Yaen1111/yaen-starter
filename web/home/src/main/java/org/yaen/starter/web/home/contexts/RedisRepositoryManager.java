package org.yaen.starter.web.home.contexts;

import lombok.Setter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * redis manager, can be used as session or cache
 * 
 * @author Yaen 2016年5月19日下午6:42:54
 */
public class RedisRepositoryManager {

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
	 * get value by key
	 * 
	 * @param dbIndex
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public byte[] getValueByKey(int dbIndex, byte[] key) throws Exception {
		Jedis jedis = null;
		byte[] result = null;
		boolean isBroken = false;
		try {
			jedis = getJedis();
			jedis.select(dbIndex);
			result = jedis.get(key);
		} catch (Exception e) {
			isBroken = true;
			throw e;
		} finally {
			returnResource(jedis, isBroken);
		}
		return result;
	}

	/**
	 * delete value by key
	 * 
	 * @param dbIndex
	 * @param key
	 * @throws Exception
	 */
	public void deleteByKey(int dbIndex, byte[] key) throws Exception {
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
	 * save value by key
	 * 
	 * @param dbIndex
	 * @param key
	 * @param value
	 * @param expireTime
	 * @throws Exception
	 */
	public void saveValueByKey(int dbIndex, byte[] key, byte[] value, int expireTime) throws Exception {
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

}
