package org.yaen.starter.web.home.contexts.shiro;

import java.util.Collection;
import java.util.Set;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.yaen.starter.common.integration.clients.RedisClient;
import org.yaen.starter.common.util.utils.StringUtil;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * shiro cache object
 * 
 * @author Yaen 2016年5月19日下午6:42:54
 */
@Slf4j
public class ShiroRedisCache<K, V> implements Cache<K, V> {

	/** redis shiro cache prefix */
	private static final String REDIS_SHIRO_CACHE = "shiro-cache:";

	/** the redis manager from constructor */
	private RedisClient redisClient;

	/** the name used for key */
	@Getter
	private String name;

	/**
	 * build cache key with prefix and name
	 * 
	 * @param key
	 * @return
	 */
	protected String buildCacheKey(Object key) {
		return REDIS_SHIRO_CACHE + this.name + ":" + StringUtil.toString(key);
	}

	/**
	 * constructor
	 * 
	 * @param name
	 * @param redisClient
	 */
	public ShiroRedisCache(String name, RedisClient redisClient) {
		this.name = name;
		this.redisClient = redisClient;
	}

	/**
	 * @see org.apache.shiro.cache.Cache#get(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public V get(K key) throws CacheException {
		try {
			return (V) redisClient.getObjectByKey(this.buildCacheKey(key));
		} catch (CacheException ex) {
			throw ex;
		} catch (Exception ex) {
			log.error("get from redis error", ex);
			return null;
		}
	}

	@Override
	public V put(K key, V value) throws CacheException {
		V previos = this.get(key);
		try {
			redisClient.saveObjectByKey(key, value, -1);
		} catch (CacheException ex) {
			throw ex;
		} catch (Exception ex) {
			log.error("put to redis error", ex);
		}
		return previos;
	}

	@Override
	public V remove(K key) throws CacheException {
		V previos = get(key);
		try {
			redisClient.deleteByKey(this.buildCacheKey(key));
		} catch (CacheException ex) {
			throw ex;
		} catch (Exception ex) {
			log.error("remove from redis error", ex);
		}
		return previos;
	}

	@Override
	public void clear() throws CacheException {
		// TODO
	}

	@Override
	public int size() {
		if (this.keys() == null)
			return 0;
		return this.keys().size();
	}

	@Override
	public Set<K> keys() {
		// TODO
		return null;
	}

	@Override
	public Collection<V> values() {
		// TODO
		return null;
	}

}
