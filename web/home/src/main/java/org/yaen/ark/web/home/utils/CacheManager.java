package org.yaen.ark.web.home.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 缓存操作工具类
 * 
 * @author pengpeng
 *
 */
public class CacheManager {

	public static final Logger logger = LoggerFactory
			.getLogger(CacheManager.class);

	private JedisPool cacheJedisPool;

	private String namespace = "";

	public String hset(final String hashKey, final String fieldValue,
			final String value, final int seconds) {
		String result = this.execute(new RedisOperation<String>() {
			@Override
			public String execute(Jedis jedis) {
				Long reply = jedis.hset(hashKey, fieldValue, value);
				Long reply_ex = jedis.expire(hashKey, seconds);
				if (1L == reply && 1L == reply_ex) {
					return "OK";
				}
				return null;

			}

		});
		return result;
	}

	public List<String> hgetAll(final String hashkey) {

		List<String> result = this.execute(new RedisOperation<List<String>>() {
			List<String> list = new ArrayList<String>();

			@Override
			public List<String> execute(Jedis jedis) {
				Set<String> uriKeys = jedis.hkeys(hashkey);
				Map<String, String> uriMap = jedis.hgetAll(hashkey);
				for (String uriKey : uriKeys) {
					list.add(uriKey + uriMap.get(uriKey));
				}
				return list;
			}

		});
		return result;
	}

	public void hdel(final String hashKey) {
		String s = this.execute(new RedisOperation<String>() {

			@Override
			public String execute(Jedis jedis) {
				Set<String> uriKeys = jedis.hkeys(hashKey);
				for (String string : uriKeys) {
					jedis.hdel(hashKey, string);
				}
				return null;
			}

		});
	}

	/**
	 * 
	 * @param key
	 * @param seconds
	 * @param value
	 * @return 成功是OK
	 */
	public String set(String key, final int seconds, final Object value) {
		final String cacheKey = keyWithNamespace(key);
		final byte[] v = SerializeUtil.serialize(value);

		String result = this.execute(new RedisOperation<String>() {

			@Override
			public String execute(Jedis jedis) {
				return jedis.setex(cacheKey.getBytes(), seconds, v);
			}

		});
		return result;
	}

	/**
	 * 
	 * @param key
	 * @param value
	 * @return 成功是OK
	 */
	public String set(String key, final Object value) {
		final String cacheKey = keyWithNamespace(key);
		final byte[] v = SerializeUtil.serialize(value);

		String result = this.execute(new RedisOperation<String>() {

			@Override
			public String execute(Jedis jedis) {
				return jedis.set(cacheKey.getBytes(), v);// OK
			}

		});
		return result;
	}

	public Object get(String key) {
		Object result = null;
		try {
			final String cacheKey = keyWithNamespace(key);
			byte[] bytes = this.execute(new RedisOperation<byte[]>() {

				@Override
				public byte[] execute(Jedis jedis) {
					return jedis.get(cacheKey.getBytes());
				}

			});
			result = SerializeUtil.unserialize(bytes);
		} catch (Exception e) { 
			result = null;
		}

		return result;
	}

	public Long delete(String key) {
		final String cacheKey = keyWithNamespace(key);
		final byte[] bk = cacheKey.getBytes();
		return this.execute(new RedisOperation<Long>() {

			@Override
			public Long execute(Jedis jedis) {
				return jedis.del(bk);
			}

		});

	}

	public String getString(String key) {
		final String cacheKey = keyWithNamespace(key);
		return this.execute(new RedisOperation<String>() {

			@Override
			public String execute(Jedis jedis) {

				return jedis.get(cacheKey);
			}
		});

		// return result;
	}

	/**
	 * redis操作模板方法
	 * 
	 * @param operation
	 * @return
	 */
	private <T> T execute(RedisOperation<T> operation) {
		Jedis jedis = null;
		T result = null;
		try {
			jedis = cacheJedisPool.getResource();
			result = operation.execute(jedis);
		} catch (Exception e) {
			returnBrokenResource(cacheJedisPool, jedis);
			logger.error("redis operate error!", e);
			throw new RuntimeException(e);
		} finally {
			returnResource(cacheJedisPool, jedis);
		}

		return result;
	}

	/**
	 * 为防止不同应用缓存key冲突，强制添加命名空间前缀
	 * 
	 * @param key
	 * @return
	 */
	private String keyWithNamespace(String key) {
		if (StringUtils.isEmpty(namespace)) {
			throw new RuntimeException("namespace can not be empty!");
		}
		return namespace + "." + key;
	}

	/**
	 * 当连接异常时，归还“坏”连接，连接池会销毁该连接，并重新创建新连接，以保持足够的连接数
	 * 
	 * @param cacheJedisPool
	 * @param jedis
	 */
	private void returnBrokenResource(JedisPool cacheJedisPool, Jedis jedis) {
		if (jedis != null && cacheJedisPool != null) {
			cacheJedisPool.returnBrokenResource(jedis);
		}
	}

	/**
	 * CacheManager bean销毁的时候，也关闭所有reds客户端连接。 在spring里调用
	 * 
	 */
	public void destroy() {
		this.cacheJedisPool.destroy();
	}

	/**
	 * 当连接使用完毕后，必须归还连接到连接池
	 * 
	 * @param cacheJedisPool
	 * @param jedis
	 */
	private static void returnResource(JedisPool cacheJedisPool, Jedis jedis) {
		if (jedis != null && cacheJedisPool != null) {
			cacheJedisPool.returnResource(jedis);
		}
	}

	public void setCacheJedisPool(JedisPool cacheJedisPool) {
		this.cacheJedisPool = cacheJedisPool;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	/**
	 * redis 操作接口
	 * 
	 * @author alex
	 *
	 * @param <T>
	 */
	private interface RedisOperation<T> {
		T execute(Jedis jedis);
	}
}
