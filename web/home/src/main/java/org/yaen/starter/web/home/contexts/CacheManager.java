package org.yaen.starter.web.home.contexts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.session.ExpiringSession;
import org.yaen.starter.web.home.utils.SerializeUtil;

import lombok.Setter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * session manager, support session and redis global session
 * 
 * @author Yaen 2016年5月19日下午6:42:54
 */
public class CacheManager {

	/** session key */
	public static final String SESSION_KEY = "session";

	/** session id key */
	public static final String SESSION_ID_KEY = "sessionid";

	/** the jedis pool bean */
	@Setter
	private JedisPool cacheJedisPool;

	/** the global name space in redis */
	@Setter
	private String namespace = "";

	/** ThreadLocal */
	private static ThreadLocal<Map<String, Object>> local = new ThreadLocal<Map<String, Object>>() {
		@Override
		protected java.util.Map<String, Object> initialValue() {
			return new HashMap<String, Object>();
		};
	};

	/**
	 * redis execute agent
	 * 
	 * @param <T>
	 * @author Yaen 2016年5月23日下午8:37:28
	 */
	private interface RedisOperation<T> {
		T execute(Jedis jedis);
	}

	/**
	 * get local object by key
	 * 
	 * @param key
	 * @return
	 */
	public static Object getLocal(String key) {
		return local.get().get(key);
	}

	/**
	 * put local object by key, set value to null to delete
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static void putLocal(String key, Object value) {
		local.get().put(key, value);
	}

	/**
	 * get local session
	 * 
	 * @return
	 */
	public static ExpiringSession getLocalSession() {
		return (ExpiringSession) getLocal(SESSION_KEY);
	}

	/**
	 * set local session
	 * 
	 * @param sessionId
	 */
	public static void setLocalSession(ExpiringSession session) {
		putLocal(SESSION_KEY, session);
	}

	/**
	 * set field by hashkey
	 * 
	 * @param hashKey
	 * @param fieldValue
	 * @param value
	 * @param seconds
	 * @return
	 */
	public String hset(final String hashKey, final String fieldValue, final String value, final int seconds) {
		return this.execute(new RedisOperation<String>() {
			@Override
			public String execute(Jedis jedis) {
				Long reply = jedis.hset(hashKey, fieldValue, value);
				Long reply_ex = jedis.expire(hashKey, seconds);
				if (reply == 1L && reply_ex == 1L) {
					return "OK";
				}
				return null;
			}
		});
	}

	/**
	 * get all value by haskey
	 * 
	 * @param hashkey
	 * @return
	 */
	public List<String> hgetAll(final String hashkey) {

		return this.execute(new RedisOperation<List<String>>() {
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
	}

	/**
	 * delete value by hashkey
	 * 
	 * @param hashKey
	 */
	public void hdel(final String hashKey) {
		this.execute(new RedisOperation<String>() {

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

}
