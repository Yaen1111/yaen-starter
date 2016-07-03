package org.yaen.starter.core.service.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaen.starter.common.integration.clients.RedisClient;
import org.yaen.starter.common.util.utils.StringUtil;
import org.yaen.starter.core.model.services.CacheService;

/**
 * cache service to read/write cache to redis
 * 
 * @author Yaen 2016年6月19日下午4:37:50
 */
@Service
public class CacheServiceImpl implements CacheService {
	/** redis shiro cache prefix */
	private static final String REDIS_GENERAL_CACHE = "gen-cache:";

	/** injection redis client, by field name */
	@Autowired
	private RedisClient generalCacheClient;

	/**
	 * build key
	 * 
	 * @param key
	 * @return
	 */
	private String buildCacheKey(Object key) {
		return REDIS_GENERAL_CACHE + StringUtil.toString(key);
	}

	/**
	 * @see org.yaen.starter.core.model.services.CacheService#get(java.lang.Object)
	 */
	@Override
	public Object get(Object key) {
		if (key == null)
			return null;

		return this.generalCacheClient.getObjectByKey(this.buildCacheKey(key));
	}

	/**
	 * @see org.yaen.starter.core.model.services.CacheService#set(java.lang.Object, java.lang.Object, int)
	 */
	@Override
	public void set(Object key, Object value, int expireInSecond) {
		if (key == null)
			return;

		// delete or update
		if (value == null) {
			this.generalCacheClient.deleteByKey(this.buildCacheKey(key));
		} else {
			this.generalCacheClient.saveObjectByKey(this.buildCacheKey(key), value, expireInSecond);
		}
	}

}
