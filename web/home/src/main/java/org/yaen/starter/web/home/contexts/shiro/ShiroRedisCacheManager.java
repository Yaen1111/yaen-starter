package org.yaen.starter.web.home.contexts.shiro;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.yaen.starter.common.integration.clients.RedisClient;

import lombok.Setter;

/**
 * shiro cache manager
 * 
 * @author Yaen 2016年5月19日下午6:42:54
 */
public class ShiroRedisCacheManager implements CacheManager {

	/** the redis client for injection */
	@Setter
	private RedisClient redisClient;

	/**
	 * get cache from redis
	 * 
	 * @see org.apache.shiro.cache.CacheManager#getCache(java.lang.String)
	 */
	@Override
	public <K, V> Cache<K, V> getCache(String name) throws CacheException {
		return new ShiroRedisCache<K, V>(name, this.redisClient);
	}

}
