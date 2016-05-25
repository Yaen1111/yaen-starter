package org.yaen.starter.web.home.shiro;

import java.util.Collection;
import java.util.Set;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.yaen.starter.common.util.utils.SerializeUtil;
import org.yaen.starter.web.home.contexts.RedisRepositoryManager;

/**
 * shiro cache object
 * 
 * @author Yaen 2016年5月19日下午6:42:54
 */
public class ShiroRedisCache<K, V> implements Cache<K, V> {

	private static final String REDIS_SHIRO_CACHE = "shiro-cache:";

	// cache指定redis缓存在哪个数据库：pc员工版启用1，pc站长版启用2
	private static final int DB_INDEX = 1;

	/** the redis manager from constructor */
	private RedisRepositoryManager repositoryManager;

	private String name;

	public ShiroRedisCache(String name, RedisRepositoryManager repositoryManager) {
		this.name = name;
		this.repositoryManager = repositoryManager;
	}

	/**
	 * 自定义relm中的授权/认证的类名加上授权/认证英文名字
	 */
	public String getName() {
		if (name == null)
			return "";
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@SuppressWarnings("unchecked")
	@Override
	public V get(K key) throws CacheException {
		byte[] byteKey = SerializeUtil.serialize(buildCacheKey(key));
		byte[] byteValue = new byte[0];
		try {
			byteValue = repositoryManager.getValueByKey(DB_INDEX, byteKey);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("get cache error");
		}
		return (V) SerializeUtil.deserialize(byteValue);
	}

	@Override
	public V put(K key, V value) throws CacheException {
		V previos = get(key);
		try {
			repositoryManager.saveValueByKey(DB_INDEX, SerializeUtil.serialize(buildCacheKey(key)),
					SerializeUtil.serialize(value), -1);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("put cache error");
		}
		return previos;
	}

	@Override
	public V remove(K key) throws CacheException {
		V previos = get(key);
		try {
			repositoryManager.deleteByKey(DB_INDEX, SerializeUtil.serialize(buildCacheKey(key)));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("remove cache error");
		}
		return previos;
	}

	@Override
	public void clear() throws CacheException {
		// TODO
	}

	@Override
	public int size() {
		if (keys() == null)
			return 0;
		return keys().size();
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

	private String buildCacheKey(Object key) {
		return REDIS_SHIRO_CACHE + getName() + ":" + key;
	}

}
